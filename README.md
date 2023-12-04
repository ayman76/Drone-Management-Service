# Drone-Management-Service

This project implements a REST API service for managing a fleet of drones used for medication delivery. The service allows registration of drones, loading medications onto drones, checking loaded medications, monitoring drone battery levels, and more.

## Documentation

https://documenter.getpostman.com/view/26688798/2s9YeK39u2

## Requirements
- Java (version 17 or higher)
- Maven
- MySQL
- Docker (optional, for running a containerized database)


## Setup Instructions

## 1- Clone the repository 
```bash
git clone https://github.com/ayman76/Drone-Management-Service
```
## 2- Build the Project:
```bash
mvn clean package install
```
- Open Project in your editor
- Configure application.properties
- Add database-name, user and password to configure your database

```bash
spring.datasource.url=jdbc:mysql://${RDS_HOSTNAME:localhost}:${RDS_PORT:3306}/${RDS_DBNAME:{database-name}}?createDatabaseIfNotExist=true
spring.datasource.username=${RDS_USERNAME:{user}}
spring.datasource.password=${RDS_PASSWORD:{password}}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# Allows To Hibernate to generate SQL optimized for a particular DBMS
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
# To Avoid preloading data change values of this two lines to update and never
spring.jpa.hibernate.ddl-auto=none #update
spring.sql.init.mode=always #never
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB


```

## 3- Run the Application:
For local testing 

```bash
maven spring-boot:run
```
If using Docker

```bash
docker build -t technical-task-api .
docker-compose up -d
```

## 4- Testing the API:
The API endpoints can be tested using tools like Postman, cURL, or any API testing tool.
## - Drone
  - Register a Drone:
    - Endpoint: POST /api/v1/drone/register
    - Payload: JSON object with drone details (model, battery capacity)
  
  - Load Medication onto a Drone:
    - Endpoint: POST /api/v1/drone/{droneSerialNumber}/load
    - Payload: JSON object with medication details (list of medications code)
  
  - Check Loaded Medications on a Drone:
    - Endpoint: GET /api/v1/drone/{droneSerialNumber}/loaded-medications
  
  - Check Available Drones for Loading:
    - Endpoint: GET /api/v1/drone/available-for-loading
  
  - Check Drone Battery Level:
    - Endpoint: GET /api/v1/drone/{droneSerialNumber}/battery-level

## - Medication
 - Create Medication:
    - Endpoint: POST /api/v1/medications/create
    - Payload: form data with medication details (name, weight, imageFile)
 - Get All Medication
   - Endpoint: GET /api/v1/medications/all  

## 5- Run Tests
Testing all unit and integraion tests
```bash
mvn test
```

The app will start running at http://localhost:8080
