# HMS (Hospital Management System)

A decentralized Hospital Management System built as a Developer Showcase and API Demonstration Tool.
This project is designed to highlight backend API design, frontend integration, security practices, testing discipline, and code quality engineering in a distributed setup.

---

## 📌 Project Overview

HMS is not intended to be a production-ready hospital user interface. Instead, it is a technical showcase project that demonstrates how a hospital domain can be modeled and exposed through a secure, testable, and maintainable distributed application.

The system is intentionally split into independent components:

- Frontend: Thymeleaf-based developer-facing UI
- Backend: Spring Boot REST API
- Database: Configurable relational database layer

This decentralized approach allows the application to run across multiple machines, making it useful for:

- API demonstrations
- developer onboarding
- architecture discussions
- distributed deployment testing
- endpoint ownership showcase by team members

### Why this project exists

HMS was built to demonstrate:

- clean layered backend architecture
- secure session-based authentication
- interactive endpoint exploration
- developer-to-endpoint ownership mapping
- maintainable code with SonarQube-driven improvements
- integration between independently deployable frontend and backend services

---

## 🏗️ Architecture Diagram

```text
+-----------------------+        HTTP / Session / CSRF        +-----------------------+        JDBC / JPA        +----------------------+
| Frontend (Laptop A)   |  --------------------------------> | Backend (Laptop B)    |  --------------------> | Database (Laptop C)  |
| Thymeleaf UI          |                                     | Spring Boot API       |                        | MySQL / PostgreSQL   |
| API Testing Console   | <---------------------------------  | Business Logic        | <--------------------  | Persistent Storage   |
+-----------------------+        JSON / HTML / Errors         +-----------------------+        Queries          +----------------------+
```

### Communication Flow

1. A user accesses the frontend application from a browser.
2. The frontend renders a developer showcase UI and endpoint explorer using Thymeleaf.
3. When the user triggers an API action, the frontend proxies the request to the backend service over HTTP.
4. The backend handles:
   - authentication
   - authorization
   - validation
   - business logic
   - persistence
5. The backend communicates with the database using Spring Data JPA / Hibernate.
6. The response is returned to the frontend and displayed in the interactive API testing interface.

---

## ⚙️ Tech Stack

### Backend

- Java 17
- Spring Boot 3.2.5
- Spring Web
- Spring Security
- Spring Data JPA
- Hibernate
- Bean Validation

### Frontend

- Thymeleaf
- Spring Boot MVC
- HTML/CSS/JavaScript
- RestClient for backend communication

### Database

- MySQL
- PostgreSQL
- Configurable via application properties

### Testing

- JUnit 5
- Mockito
- Spring Test

### Code Quality

- SonarQube
- JaCoCo coverage reports

### Build Tool

- Maven

---

## 🔐 Security Implementation

Security in HMS is designed for a server-rendered, session-oriented distributed system.

### Implemented Security Features

- Session-based authentication
- HttpOnly cookie-based session storage
- Role-based authorization
- CSRF protection
- Password hashing with BCrypt
- No hardcoded credentials
- Server-side access control

### Authentication Model

After login:

- the backend creates a session
- the browser stores the session identifier in an HttpOnly cookie
- subsequent requests automatically carry the authenticated session
- protected endpoints are authorized based on assigned roles

### Authorization Model

The system supports role-based access control such as:

- Admin
- User / domain roles depending on endpoint scope

This allows the backend to restrict administrative and management operations while still exposing safe endpoints for demonstration and testing.

### CSRF Protection

Because HMS uses session cookies, CSRF protection is required and is enabled.
The frontend retrieves and forwards the CSRF token when sending state-changing requests.

### Why JWT is NOT used here

JWT is intentionally avoided in this project because:

- this is a server-rendered application
- session-based auth is simpler and safer for this architecture
- session invalidation is easier to control
- the system is focused on backend governance and API demonstration, not stateless public API federation

For this use case, server-managed sessions + HttpOnly cookies provide a more appropriate and controlled model.

---

## 📂 Project Structure

## Backend Structure

```text
hms-backend/
└── src/main/java/com/capgemini/hms/
    ├── auth/
    ├── clinical/
    ├── common/
    ├── exception/
    ├── infrastructure/
    ├── nursing/
    ├── patient/
    ├── scheduling/
    └── security/
```

### Layer Responsibilities

- controller
  - defines REST endpoints
  - handles request/response orchestration
  - delegates business logic to services

- service
  - contains business logic
  - performs validation and workflow coordination
  - maps entities and DTOs where appropriate

- repository
  - data access layer
  - powered by Spring Data JPA

- dto
  - request/response transfer objects
  - used to keep API contracts stable and explicit

- entity
  - domain persistence models
  - mapped with JPA/Hibernate annotations

- config
  - security configuration
  - bean definitions
  - initialization and environment-driven setup

- exception
  - centralized exception model
  - global exception handling
  - consistent API error responses

## Frontend Structure

```text
hms-frontend/
└── src/main/java/com/capgemini/hms/
    ├── config/
    ├── controller/
    ├── model/
    └── service/

hms-frontend/src/main/resources/
    ├── templates/
    └── application.properties
```

### Frontend Responsibilities

- render developer-facing pages
- display team/member endpoint ownership
- build dynamic request forms
- proxy API requests to backend
- render structured API responses and errors

---

## 🚀 Features

### Core Features

- Developer showcase landing page
- Member-wise endpoint mapping
- Interactive endpoint exploration
- Dynamic API testing UI
- Form-based request builder
- Real-time API response display
- Validation handling
- Consistent error handling
- Role-protected backend APIs
- Distributed deployment support

### Functional Highlights

- browse available endpoints by developer/member
- inspect request fields and endpoint metadata
- execute requests directly from the UI
- view structured success/error responses instantly
- demonstrate backend flows without Postman or Swagger dependency

---

## 🔌 API Documentation System

HMS includes a lightweight documentation and ownership model tailored for team demonstrations.

### How it works

Endpoints are associated with developers/members so that the frontend can show:

- which developer owns which endpoint
- what each endpoint does
- how to test it
- what input is required

### Mapping Strategy

The system supports config-driven / CSV-style endpoint ownership mapping, making it easy to maintain:

- developer name
- endpoint path
- HTTP method
- feature/domain
- description

This turns the frontend into a developer showcase dashboard, not just a UI layer.

### Interactive Testing UI

The frontend provides an embedded testing experience:

- select an endpoint
- view its metadata
- fill form inputs
- send request to backend
- inspect JSON/error response in real time

This makes HMS useful as both:

- a demo platform
- an internal API discovery tool

---

## 🧪 Testing

Testing is a core part of the project and was expanded to improve reliability and coverage.

### Test Stack

- JUnit 5
- Mockito
- Spring Test
- MockMvc / service-layer mocks where applicable

### What is tested

- service layer business logic
- controller behavior
- exception handling
- security-sensitive flows
- frontend proxy/service behavior
- configuration classes
- initialization flows

### Coverage Focus

Coverage was intentionally improved in areas such as:

- service-layer branching logic
- exception paths
- authentication-related initialization
- frontend proxy request handling
- edge cases and error flows

This ensures the application remains stable while refactors and quality improvements are introduced.

---

## 📊 Code Quality

HMS uses SonarQube as part of its code quality process.

### Quality Improvements Completed

- removed hardcoded credentials
- improved security configuration
- centralized repeated error messages
- modernized Java stream usage
- reduced duplicate literals
- cleaned imports and minor smells
- strengthened exception handling consistency
- improved test coverage significantly

### Quality Goals

- high maintainability
- low duplication
- strong security posture
- safe refactoring without changing business contracts

---

## 🛠️ Setup Instructions

## A. Backend Setup

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd HMS
```

### 2. Configure the backend database connection

Update backend configuration in:

```properties
hms-backend/src/main/resources/application.properties
```

Example for MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hms_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

Example for PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hms_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Configure admin credentials

```properties
admin.username=${ADMIN_USERNAME}
admin.password=${ADMIN_PASSWORD}
admin.email=admin@local.test
```

For local development, environment variables are recommended.

### 4. Run the backend

```bash
cd hms-backend
mvn spring-boot:run
```

Default backend URL:

```text
http://localhost:8080
```

---

## B. Frontend Setup

The frontend can run on a different machine or different port.

### 1. Configure backend base URL

Update frontend configuration to point to the backend host:

```properties
hms.backend.url=http://localhost:8080
```

If running on another machine:

```properties
hms.backend.url=http://192.168.x.x:8080
```

### 2. Run the frontend

```bash
cd hms-frontend
mvn spring-boot:run
```

Example frontend URL:

```text
http://localhost:8081
```

---

## C. Database Setup

You can use either MySQL or PostgreSQL.

### Requirements

- create a database
- configure JDBC URL, username, password, and dialect
- ensure the backend machine can reach the database host

Example database creation:

```sql
CREATE DATABASE hms_db;
```

---

## 🌐 Running in Decentralized Mode

HMS is designed to work across separate machines.

### Example Deployment

- Frontend: `http://192.168.1.10:8081`
- Backend: `http://192.168.1.20:8080`
- Database: `192.168.1.30:3306`

### Example frontend configuration

```properties
hms.backend.url=http://192.168.1.20:8080
```

### Example backend database configuration

```properties
spring.datasource.url=jdbc:mysql://192.168.1.30:3306/hms_db
```

### Notes

- ensure machines can communicate over the network
- open required ports in firewall/security group settings
- use reachable IP addresses or hostnames
- configure CORS/backend security as needed for deployment topology

---

## 🔑 Authentication Flow

HMS uses session-based authentication.

### Flow

1. User submits login credentials.
2. Backend validates credentials.
3. Backend creates an authenticated session.
4. Session ID is stored in an HttpOnly cookie.
5. Browser automatically includes the cookie in future requests.
6. Frontend proxy forwards the request context to backend.
7. Protected APIs execute if the session and role checks succeed.

### Request Lifecycle

```text
Login Request
   ↓
Backend Authentication
   ↓
Session Created
   ↓
HttpOnly Cookie Stored in Browser
   ↓
Subsequent API Calls Carry Session Automatically
   ↓
Backend Authorizes Request
```

This model keeps credentials out of client-side JavaScript and aligns well with a server-rendered UI.

---

## 📸 Screenshots

> Replace these placeholders with actual screenshots from your project.

### Landing Page

```text
[ Screenshot Placeholder - Landing Page ]
```

### Member Page

```text
[ Screenshot Placeholder - Member Endpoint Mapping Page ]
```

### Endpoint Detail Page

```text
[ Screenshot Placeholder - Endpoint Detail / Documentation Page ]
```

### API Testing Form

```text
[ Screenshot Placeholder - Interactive API Testing UI ]
```

---

## 📈 Future Improvements

Planned or recommended enhancements:

- Dockerization
  - containerize frontend, backend, and database
- CI/CD pipeline
  - automated build, test, quality gate, and deployment
- API Gateway
  - centralized routing and cross-cutting concerns
- Monitoring
  - Prometheus
  - Grafana
  - Zipkin / distributed tracing
- Environment profiles
  - dedicated dev, test, staging, and production config sets
- Enhanced documentation
  - OpenAPI/Swagger for raw REST contract export
- Observability
  - structured logging and request tracing

---

## 👨‍💻 Team

This project is structured as a 6-developer showcase, where endpoint ownership and contribution visibility are part of the frontend experience.

### Example Team Layout

- Developer 1 — Authentication & Security
- Developer 2 — Patient Module
- Developer 3 — Scheduling Module
- Developer 4 — Nursing Module
- Developer 5 — Clinical / Infrastructure Module
- Developer 6 — Frontend Integration & API Showcase UI

> Replace these placeholders with actual developer names and responsibilities.

---

## 📜 License

This project is currently provided for educational, demonstration, and portfolio purposes.

You may choose to add a formal open-source license such as:

- MIT License
- Apache License 2.0
- GPLv3

Example:

```text
Copyright (c) 2026
Licensed under the MIT License.
```

---

## Final Notes

HMS demonstrates that a hospital domain can be modeled as a secure, testable, decentralized Java system while also serving as a developer portfolio artifact and interactive API demonstration platform.

It is best understood not as a hospital-facing product, but as a technical showcase of distributed architecture, Spring Boot engineering, secure session management, and maintainable API design.
