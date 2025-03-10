FROM openjdk:21
WORKDIR /app
COPY target/gateway-0.0.1-SNAPSHOT.jar api-gateway.jar
CMD ["java", "-jar", "api-gateway.jar"]
EXPOSE 8084