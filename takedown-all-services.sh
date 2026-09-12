#!/bin/bash
set -euo pipefail

echo "Derrubando dependencias..."

echo "1. derrubando kafka..."
docker compose -f ./compose/kafka/docker-compose.yml down -v

echo "2. derrubando postgres..."
docker compose -f ./compose/postgres/docker-compose.yml down -v

CLUSTER="mbausp-cluster"
echo "derrubando kinD... cluster $CLUSTER"
kind delete cluster --name "$CLUSTER"

echo "--------------------------------------------------------"
echo "✅ SERVIÇOS DOWN!"
echo "--------------------------------------------------------"
