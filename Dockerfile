# Imagen base con Java 17 estable
FROM eclipse-temurin:17-jdk-jammy

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el .jar construido
COPY target/main-app-jar-with-dependencies.jar app.jar

# Exponer el puerto 9001
EXPOSE 9001

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]