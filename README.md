# 🚀 Ambiente de Desenvolvimento Local

Este projeto utiliza scripts de automação para facilitar o ciclo de vida do ambiente de desenvolvimento local. 

O script **`build-deploy-to-kind.sh`** é responsável por provisionar a infraestrutura e a aplicação, enquanto o **`takedown-all-services.sh`** garante a limpeza e destruição segura dos recursos criados.

## 📋 Pré-requisitos

Antes de executar os scripts, certifique-se de ter as seguintes ferramentas instaladas na sua máquina:

* **[Docker](https://docs.docker.com/get-docker/)**: Para execução dos contêineres e do cluster.
* **[Kind](https://kind.sigs.k8s.io/docs/user/quick-start/)** (Kubernetes in Docker): Para gerenciar o cluster local.
* **[kubectl](https://kubernetes.io/docs/tasks/tools/)**: Para interagir com o cluster Kubernetes.

---

## 🏗️ Subindo o Ambiente (`build-deploy-to-kind.sh`)

Este script sobe o ambiente completo de forma automatizada. Ao executá-lo, as seguintes etapas são realizadas:

1. **Criação do broker kafka:** Inicia o broker kafka.
2. **Criação do cluster postgres:** Inicia o banco de dados postgres.
3. **Criação do Cluster:** Inicia um cluster Kubernetes local utilizando o Kind.
4. **Build da Aplicação:** Realiza o build das imagens Docker da aplicação a partir do código-fonte atual.
5. **Carregamento de Imagens:** Faz o *load* das imagens recém-criadas diretamente para os *nodes* do cluster Kind (evitando a necessidade de um *registry* externo).
6. **Deploy de Dependências:** Aplica os manifestos Kubernetes para subir a infraestrutura base e dependências da aplicação (ex: Banco de Dados, Mensageria, Cache, etc.).
7. **Deploy da Aplicação:** Aplica os manifestos da aplicação principal no cluster.
8. **Healthcheck / Aguardo:** Aguarda (via `kubectl wait`) até que os *Pods* da aplicação e das dependências estejam com o status `Ready`.

### ▶️ Como executar:

```bash
# Dá permissão de execução ao script (necessário apenas na primeira vez)
chmod +x build-deploy-to-kind.sh

# Executa o provisionamento do ambiente
./build-deploy-to-kind.sh