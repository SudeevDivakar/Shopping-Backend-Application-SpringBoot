FROM openjdk:21
WORKDIR /app
COPY target/productapp-0.0.1-SNAPSHOT.jar product-service.jar
CMD ["java", "-jar", "product-service.jar"]
EXPOSE 8080