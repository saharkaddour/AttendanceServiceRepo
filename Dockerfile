FROM openjdk:17-jdk-slim

# répertoire de travail à l'intérieur du conteneur
WORKDIR /app

# Copier le fichier JAR de l'application dans le conteneur
COPY target/attendance-service-0.0.1-SNAPSHOT.jar attendance-service.jar

# port
EXPOSE 8082

# Commande pour exécuter l'application Spring Boot lorsque le conteneur démarre
ENTRYPOINT ["java", "-jar", "attendance-service.jar"]