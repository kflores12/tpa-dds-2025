FROM maven:3.8.1-openjdk-8 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /app/src/
RUN mvn clean package -DskipTests

FROM eclipse-temurin:8-jre-alpine


WORKDIR /app

COPY --from=build /app/target/main-app-jar-with-dependencies.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
