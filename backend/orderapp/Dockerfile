FROM openjdk:21
WORKDIR /app
COPY target/orderapp-0.0.1-SNAPSHOT.jar order-service.jar
CMD ["java", "-jar", "order-service.jar"]
EXPOSE 8081