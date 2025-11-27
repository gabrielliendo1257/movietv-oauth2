#!/bin/bash

echo "🟢 Levantando entorno de DESARROLLO..."

#docker compose -f ./docker/docker-compose-dev.yml up --build

docker compose \
  -f docker/docker-compose-dev.yml \
  --env-file envs/.env-dev \
  down -v --remove-orphans


docker compose -f docker/docker-compose-dev.yml \
  --env-file envs/.env-dev \
  up

#echo "✅ Entorno levantado correctamente"

#export $(grep -v '^#' .env | xargs)

#echo "✅ Variables de entorno cargadas correctamente"


# export $(grep -v '^#' ./envs/.env-dev | xargs)

#mvn spring-boot:run
