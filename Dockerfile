FROM maven:3.8.1-openjdk-17

COPY target/TP-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java" , "-jar" , "/app.jar"]
