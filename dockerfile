FROM maven:3.8.6-openjdk-17-slim AS build
WORKDIR /app
  
  # Copia il pom.xml per scaricare le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline -B
  
  # Copia il codice sorgente e compila
COPY src ./src
RUN mvn package -DskipTests
  
  # Crea l'immagine finale
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
  
  # Copia il JAR compilato
COPY --from=build /app/target/iris-dl4j-service-0.0.1-SNAPSHOT.jar app.jar
  
  # Crea la directory per i modelli
RUN mkdir -p /app/models
  
  # Copia il modello (durante il build o attraverso un volume)
COPY models/irisModel.zip /app/models/irisModel.zip
  
  # Esponi la porta
EXPOSE 8080
  
  # Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]