# Imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Configurar el directorio de trabajo dentro del contenedor
WORKDIR /tarjetas

# Copiar el JAR al contenedor
COPY target/tarjetas-1.0.0.jar tarjetas.jar

# Exponer el puerto en el que se ejecutará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "tarjetas.jar", "--spring.profiles.active=prod"]
