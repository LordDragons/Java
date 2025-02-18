FROM openjdk:24-jdk-slim as build

LABEL authors="grado"

COPY target/SafetyNet-0.0.1-SNAPSHOT.jar SafetyNet-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar" ,"/SafetyNet-0.0.1-SNAPSHOT.jar"]