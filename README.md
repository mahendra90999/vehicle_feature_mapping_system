# Vehicle Feature Mapping System

## 📌 Overview

DriveMap is a Spring Boot application designed to manage vehicle-feature mappings across different countries. It demonstrates real-world backend development practices such as secure authentication, dynamic filtering, and scalable architecture.

---

## ✨ Key Features

*  JWT Authentication (Access & Refresh Token)
*  Dynamic Filtering using JPA Specifications
*  Case-insensitive & Partial Search
*  Pagination & Sorting
*  DTO Mapping using ModelMapper
*  Caching support
*  Actuator for monitoring
*  Swagger UI for API documentation
*  Role-based authorization

---

## 🛠️ Tech Stack

### 🔹 Backend

* Java 21
* Spring Boot
* Spring MVC
* Spring Data JPA (Hibernate)

### 🔹 Security

* Spring Security
* JWT (JSON Web Token)
* Role-based authorization

### 🔹 Database

* MySQL

### 🔹 API Documentation

* Springdoc OpenAPI (Swagger UI)

### 🔹 Performance

* Spring Cache
* JPA Specifications

### 🔹 Utilities

* Lombok
* ModelMapper

### 🔹 Monitoring & Logging

* Spring Boot Actuator
* SLF4J / Logback

### 🔹 Validation

* Jakarta Bean Validation

### 🔹 Build Tool

* Maven

---

## 📂 Architecture

Layered Architecture (MVC):
Controller → Service → Repository → Database

---

## 📁 Project Structure

```
src/main/java/com/example/jpa1
│
├── controller
├── service
├── repository
├── entity
├── Dao
├── dto
├── specification
├── security
├── config
└── exception
```

---

## 🔗 API Endpoints

### 🔐 Authentication APIs

* `POST /api/signup` → Register new user
* `POST /api/login` → Authenticate user & generate JWT
* `POST /api/refresh-token` → Refresh access token
* `POST /api/admin/promote/{username}` → Promote user to admin

---

### 🚗 Vehicle APIs

* `POST /vehicles/add` → Add a new vehicle

---

### 🧩 Feature APIs

* `POST /features/add` → Add a new feature

---

### 🌍 Country APIs

* `POST /countries/add` → Add a new country

---

### 🔗 Mapping APIs (Core Module)

* `POST /mappings/add` → Create vehicle-feature-country mapping
* `GET /mappings` → Fetch mappings (supports filtering, pagination, sorting)

---

## ⚙️ Run Locally

```bash
git clone https://github.com/mahendra90999/vehicle_feature_mapping_system.git
cd vehicle_feature_mapping_system
mvn clean install
mvn spring-boot:run
```

---

## 📘 Swagger UI

http://localhost:8080/swagger-ui/index.html#/vehicle-controller

---

## 📈 Future Improvements

* Docker support
* CI/CD pipeline
* Redis caching

---

## 👨‍💻 Author

Mahendra Patil

---

## ⭐ Final Note

This project demonstrates real-world backend development practices including secure authentication, scalable architecture, and efficient data handling.
