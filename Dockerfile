FROM maven:3.8.1-openjdk-17

COPY target/main-app-jar-with-dependencies.jar app.jar

ENTRYPOINT ["java" , "-jar" , "/app.jar"]
