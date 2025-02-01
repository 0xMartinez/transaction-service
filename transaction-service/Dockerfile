# Użycie obrazu OpenJDK jako bazowego
FROM openjdk:17-jdk-slim

# Ustawienie katalogu roboczego w kontenerze
WORKDIR /spring-kafka-app

# Kopiowanie pliku JAR do kontenera
COPY target/transaction-service-0.0.1-SNAPSHOT.jar app.jar

# Ekspozycja portu aplikacji
EXPOSE 8080

# Uruchamianie aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]