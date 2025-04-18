# Guida Completa: Progetto iris-dl4j-service

Questa guida fornisce istruzioni dettagliate su come configurare, avviare e utilizzare il servizio REST per la classificazione Iris.

## Indice
1. [Panoramica del progetto](#panoramica-del-progetto)
2. [Setup dell'ambiente](#setup-dellambiente)
3. [Configurazione del progetto](#configurazione-del-progetto)
4. [Avvio del servizio](#avvio-del-servizio)
5. [Test delle API](#test-delle-api)
6. [Containerizzazione](#containerizzazione)
7. [Monitoraggio](#monitoraggio)
8. [Sicurezza](#sicurezza)
9. [Personalizzazione per altri modelli](#personalizzazione-per-altri-modelli)
10. [Risoluzione problemi](#risoluzione-problemi)

## Panoramica del progetto

Il progetto `iris-dl4j-service` espone un modello di rete neurale addestrato per la classificazione dei fiori Iris tramite un'API REST. Permette di inviare le caratteristiche di un fiore (lunghezza e larghezza di sepalo e petalo) e ricevere la classificazione in una delle tre specie (Setosa, Versicolor, Virginica).

**Componenti principali**:
- **ModelService**: Carica e gestisce il modello di rete neurale
- **PredictionController**: Fornisce l'endpoint REST per le predizioni
- **ModelHealthIndicator**: Monitora la salute del modello
- **ModelMonitorScheduler**: Esegue controlli periodici sul modello

## Setup dell'ambiente

### Prerequisiti
- Java 17 o superiore
- Maven 3.6 o superiore
- Git
- (Opzionale) Docker e Docker Compose

### Installazione delle dipendenze
1. Installa Java 17:
   ```bash
   # Ubuntu/Debian
   sudo apt-get install openjdk-17-jdk
   
   # MacOS
   brew install openjdk@17
   
   # Windows
   # Scarica e installa da https://adoptium.net/
   ```

2. Installa Maven:
   ```bash
   # Ubuntu/Debian
   sudo apt-get install maven
   
   # MacOS
   brew install maven
   
   # Windows
   # Scarica e installa da https://maven.apache.org/download.cgi
   ```

## Configurazione del progetto

1. **Clona il repository**:
   ```bash
   git clone https://github.com/yourusername/iris-dl4j-service.git
   cd iris-dl4j-service
   ```

2. **Copia il modello addestrato**:
   Copia il file `irisModel.zip` dalla cartella `modelli` del progetto di training nella cartella `models` di questo progetto:
   ```bash
   mkdir -p models
   cp /path/to/previous-project/modelli/irisModel.zip models/
   ```

3. **Configura le proprietà dell'applicazione** (opzionale):
   Se necessario, modifica il file `src/main/resources/application.properties` per personalizzare il comportamento del servizio:
   ```properties
   # Cambia la porta del server
   server.port=9090
   
   # Percorso personalizzato del modello
   model.file.path=/altro/percorso/modello.zip
   
   # Aumenta il livello di logging
   logging.level.com.example.irisservice=TRACE
   ```

## Avvio del servizio

### Tramite Maven
```bash
# Compila e crea il JAR
mvn clean package

# Avvia l'applicazione
java -jar target/iris-dl4j-service-0.0.1-SNAPSHOT.jar
```

### Tramite Maven spring-boot plugin
```bash
mvn spring-boot:run
```

### Tramite Docker
```bash
# Costruisci l'immagine
docker build -t iris-dl4j-service .

# Avvia il container
docker run -p 8080:8080 -v $(pwd)/models:/app/models iris-dl4j-service
```

### Tramite Docker Compose
```bash
docker-compose up -d
```

## Test delle API

### Utilizzando lo script di test
1. Rendi eseguibile lo script:
   ```bash
   chmod +x test-api.sh
   ```

2. Esegui lo script:
   ```bash
   ./test-api.sh
   ```

### Utilizzando curl manualmente
```bash
# Verifica che il servizio sia attivo
curl http://localhost:8080/actuator/health

# Effettua una predizione per Iris Setosa
curl -X POST http://localhost:8080/api/predictions \
  -H "Content-Type: application/json" \
  -d '{
    "sepalLength": 5.1,
    "sepalWidth": 3.5,
    "petalLength": 1.4,
    "petalWidth": 0.2
  }'
```

### Utilizzando un client HTTP (es. Postman)
1. Crea una nuova richiesta POST a `http://localhost:8080/api/predictions`
2. Imposta l'header `Content-Type: application/json`
3. Nel corpo della richiesta, inserisci:
   ```json
   {
     "sepalLength": 5.1,
     "sepalWidth": 3.5,
     "petalLength": 1.4,
     "petalWidth": 0.2
   }
   ```
4. Invia la richiesta

## Containerizzazione

### Creazione dell'immagine Docker
```bash
docker build -t iris-dl4j-service .
```

### Avvio con Docker
```bash
docker run -p 8080:8080 iris-dl4j-service
```

### Avvio con Docker Compose
```bash
docker-compose up -d
```

### Push dell'immagine su Docker Hub
```bash
docker tag iris-dl4j-service yourusername/iris-dl4j-service:latest
docker push yourusername/iris-dl4j-service:latest
```

## Monitoraggio

### Endpoint Actuator
Il servizio espone diversi endpoint di monitoraggio tramite Spring Actuator:

- **Health**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`
- **Metrics**: `http://localhost:8080/actuator/metrics`

### Log
I log dell'applicazione sono disponibili:
- Nel terminale durante l'esecuzione
- Nel file di log (il percorso dipende dalla configurazione)

### Scheduler di monitoraggio
Il servizio include uno scheduler (`ModelMonitorScheduler`) che verifica periodicamente il funzionamento del modello. I risultati di queste verifiche sono visibili nei log.

## Sicurezza

> Nota: Questa versione base del servizio non include funzionalità di sicurezza. In un ambiente di produzione, dovresti implementare:

1. **Autenticazione e autorizzazione**:
    - Aggiungi Spring Security
    - Implementa autenticazione JWT o OAuth2

2. **HTTPS**:
    - Configura un certificato SSL
    - Forza il reindirizzamento da HTTP a HTTPS

3. **Rate limiting**:
    - Limita il numero di richieste per IP/utente
    - Proteggi da attacchi DoS

## Personalizzazione per altri modelli

Per adattare il servizio a un altro tipo di modello:

1. **Sostituisci il modello**:
    - Addestra un nuovo modello con DeepLearning4J
    - Salva il modello e copialo nella cartella `models/`
    - Aggiorna il percorso in `application.properties`

2. **Modifica i DTO**:
    - Aggiorna `PredictionRequest` per riflettere le feature del tuo nuovo modello
    - Modifica `PredictionResponse` per rappresentare le classi del tuo problema

3. **Aggiorna il ModelService**:
    - Modifica la classe `PredictionResult` con i nomi delle classi del tuo modello
    - Adatta la logica di predizione se necessario

4. **Aggiorna lo health check**:
    - Modifica `ModelHealthIndicator` con esempi appropriati per il tuo modello

## Risoluzione problemi

### Il servizio non si avvia

**Problema**: `java.io.FileNotFoundException: models/irisModel.zip`
**Soluzione**: Verifica che il file del modello sia presente nella cartella `models/` o aggiorna il percorso in `application.properties`

**Problema**: `Port 8080 is already in use`
**Soluzione**: Cambia la porta in `application.properties` (es. `server.port=9090`)

**Problema**: `java.lang.OutOfMemoryError`
**Soluzione**: Aumenta la memoria heap di Java: `java -Xmx1g -jar target/iris-dl4j-service-0.0.1-SNAPSHOT.jar`

### Le predizioni non sono corrette

**Problema**: Il modello restituisce classificazioni errate
**Soluzione**:
- Verifica che stai utilizzando il modello corretto
- Assicurati che l'ordine delle feature sia corretto
- Controlla che la normalizzazione dei dati sia coerente con quella usata durante l'addestramento

### Errori nelle richieste API

**Problema**: `415 Unsupported Media Type`
**Soluzione**: Verifica di aver impostato l'header `Content-Type: application/json`

**Problema**: `400 Bad Request`
**Soluzione**: Controlla il formato JSON della richiesta e assicurati che tutti i campi richiesti siano presenti