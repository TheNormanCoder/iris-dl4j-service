#!/bin/bash
# Script per testare l'API del servizio Iris DL4J

# Colori per l'output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Verifica se il servizio è in esecuzione
echo -e "${BLUE}Verifica dello stato del servizio...${NC}"
health_response=$(curl -s http://localhost:8080/actuator/health)

if [[ $health_response == *"UP"* ]]; then
  echo -e "${GREEN}✓ Il servizio è attivo!${NC}"
else
  echo -e "${RED}✗ Il servizio non sembra essere in esecuzione. Verificare che sia avviato sulla porta 8080.${NC}"
  exit 1
fi

# Test con un esempio di ogni classe di Iris
echo -e "\n${BLUE}Test di predizione con tre esempi diversi...${NC}"

# Esempio 1: Iris Setosa
echo -e "\n${BLUE}Test Iris Setosa:${NC}"
setosa_response=$(curl -s -X POST http://localhost:8080/api/predictions \
  -H "Content-Type: application/json" \
  -d '{
    "sepalLength": 5.1,
    "sepalWidth": 3.5,
    "petalLength": 1.4,
    "petalWidth": 0.2
  }')

echo "Risposta: $setosa_response"
if [[ $setosa_response == *"Setosa"* ]]; then
  echo -e "${GREEN}✓ Test Setosa riuscito!${NC}"
else
  echo -e "${RED}✗ Test Setosa fallito!${NC}"
fi

# Esempio 2: Iris Versicolor
echo -e "\n${BLUE}Test Iris Versicolor:${NC}"
versicolor_response=$(curl -s -X POST http://localhost:8080/api/predictions \
  -H "Content-Type: application/json" \
  -d '{
    "sepalLength": 6.4,
    "sepalWidth": 3.2,
    "petalLength": 4.5,
    "petalWidth": 1.5
  }')

echo "Risposta: $versicolor_response"
if [[ $versicolor_response == *"Versicolor"* ]]; then
  echo -e "${GREEN}✓ Test Versicolor riuscito!${NC}"
else
  echo -e "${RED}✗ Test Versicolor fallito!${NC}"
fi

# Esempio 3: Iris Virginica
echo -e "\n${BLUE}Test Iris Virginica:${NC}"
virginica_response=$(curl -s -X POST http://localhost:8080/api/predictions \
  -H "Content-Type: application/json" \
  -d '{
    "sepalLength": 7.7,
    "sepalWidth": 3.8,
    "petalLength": 6.7,
    "petalWidth": 2.2
  }')

echo "Risposta: $virginica_response"
if [[ $virginica_response == *"Virginica"* ]]; then
  echo -e "${GREEN}✓ Test Virginica riuscito!${NC}"
else
  echo -e "${RED}✗ Test Virginica fallito!${NC}"
fi

# Verifica delle metriche
echo -e "\n${BLUE}Verifica delle metriche disponibili:${NC}"
metrics_response=$(curl -s http://localhost:8080/actuator/metrics)
echo "Metriche disponibili: $metrics_response"

echo -e "\n${GREEN}Test completati!${NC}"