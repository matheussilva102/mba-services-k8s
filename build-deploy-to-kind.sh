#!/bin/bash
set -euo pipefail

echo "0. Limpando ambiente..."
kind delete clusters --all

echo "1. Criando cluster KinD..."
kind create cluster --config kind-config.yaml

echo "2. Instalando Nginx Ingress Controller..."
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

echo "2.1. Aguardando Ingress ficar pronto..."
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=300s

echo "3. Instalando Stack de Monitoramento (Prometheus/Grafana)..."
# Adiciona e atualiza o repositório Helm
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

# Instala a stack configurada para encontrar seus ServiceMonitors automaticamente
helm install prometheus-stack prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --create-namespace \
  --set prometheus.prometheusSpec.serviceMonitorSelectorNilUsesHelmValues=false
  
helm upgrade prometheus-stack prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --set grafana."grafana.ini".server.root_url="http://apps.local:8888/grafana/" \
  --set grafana."grafana.ini".server.serve_from_sub_path=true \
  --set kubeEtcd.enabled=false \
  --set kubeControllerManager.enabled=false \
  --set kubeScheduler.enabled=false \
  --set kubeProxy.enabled=false
  
echo "3.1. Aguardando CRDs do Prometheus serem registrados..."
kubectl wait --for condition=established --timeout=60s crd/servicemonitors.monitoring.coreos.com  

echo "4. Compilando aplicações e Gerando Imagens..."
mvn clean package -DskipTests

SERVICES=("service-conta" "service-auto" "service-oferta" "service-acl-evento")

for SVC in "${SERVICES[@]}"; do
  echo "--- Build e Load: $SVC ---"
  docker build -t "${SVC}:latest" -f "./${SVC}/Dockerfile" "./${SVC}"
  kind load docker-image "${SVC}:latest"
done

echo "5. Aplicando Manifesto de Configurações e Deployments..."
kubectl apply -f config-map.yaml
# Aplica o arquivo que contém seus Deployments, Services e o ServiceMonitor
kubectl apply -f app-deployment.yaml 

echo "6. Aguardando estabilização dos pods..."
kubectl wait --for=condition=ready pod --all --timeout=120s

echo "--------------------------------------------------------"
echo "✅ AMBIENTE CONFIGURADO COM SUCESSO!"
echo "--------------------------------------------------------"
echo "Prometheus: kubectl port-forward -n monitoring svc/prometheus-stack-kube-prom-prometheus 9090:9090"
echo "Grafana:    kubectl port-forward -n monitoring svc/prometheus-stack-grafana 3000:80"
echo "Credenciais Grafana: admin "
kubectl get secret --namespace monitoring -l app.kubernetes.io/component=admin-secret -o jsonpath="{.items[0].data.admin-password}" | base64 --decode ; echo
echo "--------------------------------------------------------"