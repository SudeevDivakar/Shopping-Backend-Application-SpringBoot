FROM openjdk:21
WORKDIR /app
COPY target/reviews-0.0.1-SNAPSHOT.jar review-service.jar
CMD ["java", "-jar", "review-service.jar"]
EXPOSE 8085