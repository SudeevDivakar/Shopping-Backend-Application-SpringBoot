# Shopping Application

## Overview

This is a microservices-based shopping application built using Java v21, Spring Boot, Kafka, Docker, MongoDB, MySQL, Keycloak, Spring Cloud Gateway, Resilience4j, OpenAPI Swagger Documentation, Grafana, Prometheus, Loki, Tempo, JUnit5 and Mockito. The project follows a microservices architecture with multiple services for handling products, orders, inventory, authentication, and notifications.

## Key Features

- **Microservices Architecture**: The application is structured into independent services for better scalability and maintainability.
- **Product Management**: Allows users to browse, add, update, and delete products.
- **Order Management**: Handles order placement, processing, and tracking.
- **Inventory Management**: Manages product stock levels and updates.
- **Authentication & Authorization**: Uses Keycloak for secure authentication with role-based access control (RBAC).
- **API Gateway**: Acts as a single entry point for all services and routes API requests.
- **Event-Driven Notifications**: Uses Kafka for real-time notifications, such as order status updates.
- **Observability**: Implements logging, monitoring, and distributed tracing using Loki, Prometheus and Tempo respectively.
- **Resilience & Fault Tolerance**: Uses Resilience4J for circuit breaking to handle failures.
- **Swagger API Documentation**: Provides an interactive API documentation using Swagger.
- **Docker & Docker Compose**: The entire system is containerized for easy deployment.
- **Pagination**: Supports pagination for API responses to improve performance.
- **Testing**: JUnit5 and Mockito used for writing test cases along with Postman for API testing.

## Microservices Overview

### 1. **Product Service**
- Manages product details.
- Uses MongoDB as the database.

### 2. **Order Service**
- Handles order creation and tracking.
- Uses MySQL for order storage.
- Communicates with the Inventory and Product Service before confirming orders.
- Publishes events to Kafka for notifications.

### 3. **Inventory Service**
- Keeps track of product stock.
- Uses MongoDB to store inventory data.

### 4. **Notification Service**
- Listens to Kafka topics and sends email notifications.

### 5. **API Gateway**
- Routes requests to the respective microservices.
- Handles authentication with Keycloak.

### 6. **Authentication (Keycloak)**
- Provides secure authentication & role-based authorization.
- Integrated with API Gateway.

## Tech Stack

- **Backend**: Java, Spring Boot, Spring Cloud
- **Authentication**: Keycloak
- **Database**: MySQL (Order Service), MongoDB (Product & Inventory Services)
- **Message Queue**: Kafka
- **API Documentation**: Swagger
- **Monitoring & Logging**: Spring Boot Actuator, Prometheus, Grafana, Loki, Tempo
- **Containerization**: Docker, Docker Compose
- **Fault Tolerance**: Resilience4J
- **Sending EMails**: Java Mail Sender
- **Testing**: JUnit5 and Mockito, Postman

## Running the Project

### Cloning the Repository

To clone the repository onto your machine, run:

```
git clone https://github.com/SudeevDivakar/Shopping-Backend-Application-SpringBoot.git
cd Shopping-Backend-Application-SpringBoot/backend
```

### Starting the Application

```
# Pull the latest versions of the images and start the services
docker-compose up --build

# To start the services in detached mode
docker-compose up -d

# To stop the running services
docker-compose down
```
