# Struttura del Progetto iris-dl4j-service

Ecco come dovresti organizzare la struttura delle directory del progetto:

```
iris-dl4j-service/
├── pom.xml
├── models/
│   └── irisModel.zip   <-- Copia qui il modello addestrato
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── irisservice/
│   │   │               ├── IrisServiceApplication.java
│   │   │               ├── controller/
│   │   │               │   └── PredictionController.java
│   │   │               ├── dto/
│   │   │               │   └── DTOs.java
│   │   │               ├── health/
│   │   │               │   └── ModelHealthIndicator.java
│   │   │               ├── scheduler/
│   │   │               │   └── ModelMonitorScheduler.java
│   │   │               └── service/
│   │   │                   └── ModelService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── irisservice/
│                       └── ... <-- Test classes
├── README.md
└── .gitignore
```

## Passi per l'avvio

1. **Preparazione del modello**:
   Copia il file del modello addestrato (`irisModel.zip`) dal progetto originale nella cartella `models/` del nuovo progetto.

2. **Compilazione**:
   ```bash
   mvn clean package
   ```

3. **Esecuzione**:
   ```bash
   java -jar target/iris-dl4j-service-0.0.1-SNAPSHOT.jar
   ```

4. **Verifica dell'API**:
   Effettua una chiamata POST all'endpoint `/api/predictions` con un payload JSON come:
   ```json
   {
     "sepalLength": 5.1,
     "sepalWidth": 3.5,
     "petalLength": 1.4,
     "petalWidth": 0.2
   }
   ```

5. **Verifica del monitoraggio**:
   Accedi agli endpoint di Actuator per verificare lo stato del servizio:
   ```
   http://localhost:8080/actuator/health
   http://localhost:8080/actuator/info
   http://localhost:8080/actuator/metrics
   ```