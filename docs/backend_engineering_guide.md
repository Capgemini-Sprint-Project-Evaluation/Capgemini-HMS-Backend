# Capgemini Hospital Management System (HMS) - Backend Engineering Guide

This document serves as a comprehensive technical guide for the **Hospital Management System (HMS) Backend**. It covers the architecture, technology stack, module responsibilities, security implementation, and development workflow.

---

## 1. Project Overview
The HMS Backend is a robust, Spring Boot-based RESTful API designed to manage hospital operations including patients, staff (physicians/nurses), appointments, prescriptions, room allocations, and billing. It prioritizes security, scalability, and auditability.

---

## 2. Technology Stack
The project leverages modern Java technologies to ensure high performance and maintainability:

- **Core Framework**: `Spring Boot 3.2.5`
- **Language**: `Java 17` (Temurin)
- **Data Persistence**: `Spring Data JPA` with `Hibernate 6.x`
- **Database**: `MySQL 8.0+`
- **Security**: `Spring Security 6.x` with `JJWT` (JSON Web Token)
- **API Documentation**: `Springdoc OpenAPI (Swagger UI)`
- **Testing**: `JUnit 5`, `Mockito`, and `Postman`
- **Infrastructure**: `Spring Boot Actuator`, `Micrometer Tracing`, and `Zipkin` (Distributed Tracing)
- **Build Tool**: `Maven`

---

## 3. Core Files & Package Responsibilities

### Infrastructure & Cross-Cutting Layers
- **`com.capgemini.hms.auth`**: 
    - Handles authentication logic and system bootstrapping.
    - **`SystemInitializer.java`**: Automatically seeds roles (`ROLE_ADMIN`, `ROLE_DOCTOR`, etc.) and the primary administrator account upon startup.
- **`com.capgemini.hms.security`**: 
    - Manages the Security Filter Chain.
    - **`WebSecurityConfig.java`**: Configures path-based permissions and stateless session management.
    - **`jwt/JwtAuthenticationFilter.java`**: Intercepts requests to validate JWT tokens in the `Authorization` header.
- **`com.capgemini.hms.exception`**: 
    - **`GlobalExceptionHandler.java`**: Intercepts all exceptions across the board and transforms them into standardized JSON error responses.
- **`com.capgemini.hms.logging`**: 
    - Uses AOP (Aspect-Oriented Programming) to log method execution times and API request/response details without cluttering business logic.

### Functional Modules (Domain Layer)
Each module follows a structured 4-layer architecture: `Controller` -> `Service` -> `Repository` -> `Entity`.

- **`appointment`**: Manages scheduling, prep nurses, and physician assignments.
- **`patient`**: Handles patient records, insurance details, and contact info.
- **`physician` / `nurse`**: Managed primarily through the shared `StaffService` in the `common` package.
- **`room` / `stay`**: Handles physical block/room management and patient hospitalization tracking.
- **`medication` / `prescription`**: Manages pharmacy inventory and patient treatment plans.

---

## 4. Key Configurations (`application.properties`)
- **`spring.jpa.hibernate.ddl-auto=update`**: Ensures changes to Java entities are reflected in the database without wiping existing data.
- **`spring.profiles.active=mysql`**: Activates the MySQL-specific configuration.
- **`server.port=8080`**: Default port for the backend service.

---

## 5. Backend Workflows

### 1. System Bootstrap (Startup)
1. Spring Boot initializes the Application Context.
2. `SystemInitializer` runs:
    - Checks if `ADMIN`, `DOCTOR`, `NURSE` roles exist; creates them if missing.
    - Ensures a default `admin` user is present for first-time login.
    - Seeds physical infrastructure (e.g., Clinical Block/Floor).

### 2. Authentication Flow
1. **Login**: User sends credentials to `/api/v1/auth/login`.
2. **Token Injection**: On success, the server generates a JWT signed with a secret key.
3. **Request validation**: For subsequent calls, the client sends the token in the `Bearer` token header.
4. **Filter Interception**: `JwtAuthenticationFilter` extracts the token, validates its signature/expiry, and populates the `SecurityContext`.

### 3. API Request Lifecycle
1. **Controller**: Receives the request, validates DTOs (`@Valid`), and checks permissions (`@PreAuthorize`).
2. **Service**: Contains business logic (e.g., "A physician cannot be deleted if they have active appointments").
3. **Repository**: Uses Spring Data JPA to perform CRUD operations on MySQL.
4. **Response**: Standardized via `ApiResponse<T>` to ensure consistent JSON structure:
   ```json
   {
     "success": true,
     "message": "Operation successful",
     "data": { ... }
   }
   ```

---

## 6. Development Guidelines
- **Soft Deletion**: Most entities use an `isDeleted` flag instead of hard-deleting rows. Ensure your JPA queries filter these out (typically via `@Where` or custom repository methods like `findAllActive`).
- **DTOs**: Always move data between the API and Service layer using DTOs to avoid exposing sensitive entity fields.
- **Transactional Integrity**: Use `@Transactional` on service methods that perform multiple database updates to ensure atomicity.
- **API Versioning**: All endpoints must be prefixed with `/api/v1/`.

---

## 7. Useful Commands
- **Run Application**: `mvn spring-boot:run`
- **Build Package**: `mvn clean package`
- **View Swagger Documentation**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
