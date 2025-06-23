# Usamos una imagen base oficial de OpenJDK (Java 17)
FROM eclipse-temurin:17-jdk-jammy

# Definimos el directorio de trabajo dentro del contenedor
ENV APP_HOME=/app
RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME

# Copiamos el .jar al contenedor con un nombre fijo
COPY target/lab-clinico-0.0.1-SNAPSHOT.jar app.jar

# Creamos un usuario no-root por seguridad
RUN useradd -m appuser && chown -R appuser:appuser $APP_HOME
USER appuser

# Exponemos el puerto de la aplicación
EXPOSE 8080

# Health check para verificar si el contenedor está saludable
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
