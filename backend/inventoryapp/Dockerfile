FROM openjdk:21
WORKDIR /app
COPY target/inventoryapp-0.0.1-SNAPSHOT.jar inventory-service.jar
CMD ["java", "-jar", "inventory-service.jar"]
EXPOSE 8082