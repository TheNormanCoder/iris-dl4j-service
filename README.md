# Iris DL4J Service

Un servizio REST che espone il modello di classificazione Iris addestrato con DeepLearning4J.

## 📋 Descrizione

Questo progetto fornisce un'API REST per utilizzare un modello di rete neurale pre-addestrato che classifica i fiori Iris in una delle tre specie (Setosa, Versicolor, Virginica) in base alle misurazioni dei sepali e petali.

Il servizio è basato su:
- Spring Boot per l'API REST
- DeepLearning4J per l'inferenza del modello
- Spring Actuator per il monitoraggio

## 🔧 Prerequisiti

- Java 17+
- Maven
- File del modello addestrato `irisModel.zip` (dal progetto di training)

## 🚀 Installazione e avvio

1. Clona il repository:
   ```bash
   git clone https://github.com/yourusername/iris-dl4j-service.git
   cd iris-dl4j-service
   ```

2. Copia il modello addestrato nella cartella `models/`:
   ```bash
   mkdir -p models
   cp /path/to/irisModel.zip models/
   ```

3. Compila il progetto:
   ```bash
   mvn clean package
   ```

4. Avvia il servizio:
   ```bash
   java -jar target/iris-dl4j-service-0.0.1-SNAPSHOT.jar
   ```

Il servizio sarà disponibile all'indirizzo `http://localhost:8080`.

## 📊 Utilizzo dell'API

### Effettuare una predizione

**Endpoint**: `POST /api/predictions`

**Esempio di richiesta**:
```bash
curl -X POST http://localhost:8080/api/predictions \
  -H "Content-Type: application/json" \
  -d '{
    "sepalLength": 5.1,
    "sepalWidth": 3.5,
    "petalLength": 1.4,
    "petalWidth": 0.2
  }'
```

**Esempio di risposta**:
```json
{
  "classIndex": 0,
  "className": "Setosa",
  "probabilities": {
    "Setosa": 0.98,
    "Versicolor": 0.01,
    "Virginica": 0.01
  }
}
```

### Verificare lo stato del servizio

**Endpoint**: `GET /actuator/health`

```bash
curl http://localhost:8080/actuator/health
```

## 🔄 Adattabilità ad altri modelli

Questo servizio può essere facilmente adattato per esporre altri modelli di classificazione:

1. Sostituisci il file `irisModel.zip` con il tuo modello addestrato
2. Modifica la classe `ModelService` per adattarla alle feature e alle classi del tuo specifico problema
3. Aggiorna i DTO nella classe `DTOs.java` per riflettere il formato dei dati di input/output
4. Aggiorna il test di salute in `ModelHealthIndicator.java` con esempi appropriati per il tuo modello

## 🖥️ Containerizzazione con Docker

È possibile containerizzare il servizio utilizzando Docker:

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/iris-dl4j-service-0.0.1-SNAPSHOT.jar app.jar
COPY models/irisModel.zip /app/models/irisModel.zip

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

Costruisci e avvia il container:
```bash
docker build -t iris-dl4j-service .
docker run -p 8080:8080 iris-dl4j-service
```

## 📝 Note

- Il servizio include un monitoraggio periodico del modello che verifica che le predizioni siano corrette su esempi noti
- Utilizza Spring Actuator per fornire endpoint di monitoraggio e health check
- Il modello viene caricato all'avvio dell'applicazione e resta in memoria