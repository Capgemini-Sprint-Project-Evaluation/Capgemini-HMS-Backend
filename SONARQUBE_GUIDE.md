# 🏥 HMS SonarQube Analytics Guide

This document outlines the professional static analysis and code coverage setup for the **Hospital Management System (HMS)**. Our project maintains a high-quality standard by enforcing strict rules on reliability, security, and test coverage.

## 🚀 Execution Command

To run a full system scan, open your **Command Prompt** (not PowerShell) as an Administrator and execute these two lines:

```cmd
set "JAVA_HOME=C:\Users\akash\.jdks\temurin-17.0.18"
.\mvnw.cmd clean verify sonar:sonar "-Dsonar.host.url=http://localhost:9000" "-Dsonar.login=squ_3343c529628106192c20a4ff7e6679872d11cc7e" "-Dsonar.projectKey=hms-project" "-Dsonar.projectName=HMS System"
```

> [!TIP]
> **Why `clean verify`?**
> The `clean` command removes old reports, while `verify` triggers the **JaCoCo** engine to record fresh test coverage. Without `verify`, SonarQube will show 0% coverage.

---

## 🏗️ The 3-Tier Setup

### 1. The Server (Docker)
We run SonarQube inside a sealed Docker container to ensure environment consistency.
*   **Port:** `9000`
*   **Access:** [http://localhost:9000](http://localhost:9000)
*   **Status Check:** Run `docker ps` to verify the `sonarqube` container is up.

### 2. The Engine (JaCoCo)
**JaCoCo (Java Code Coverage)** is the spy in our system. It watches our JUnit tests run and marks which lines of code were executed. It saves this data in:
*   `hms-backend/target/site/jacoco/jacoco.xml`
*   `hms-frontend/target/site/jacoco/jacoco.xml`

### 3. The Filter (Lombok Config)
To maintain an honest coverage score, we use a `lombok.config` file at the root.
```properties
config.stopBubbling = true
lombok.addLombokGeneratedAnnotation = true
```
**Effect:** This tells SonarQube to ignore automatic Getters/Setters/Constructors. This prevents "noise" and ensures we are only measuring the coverage of the actual logic we wrote.

---

## 🛡️ Quality Gate Standards

Our project is configured to **PASS** only if it meets these industry-standard KPIs:

| Metric | Threshold | Purpose |
| :--- | :--- | :--- |
| **New Code Coverage** | **> 80%** | Ensures every new feature is safe and tested. |
| **Bugs** | **0** | Zero tolerance for logic errors. |
| **Vulnerabilities** | **0** | Protects patient data from SQL injection/XSS. |
| **Code Smells** | **Grade A** | Ensures the code is easy for others to read and maintain. |

---

## 🛠️ Troubleshooting

> [!WARNING]
> **Connection Refused:**
> If the command fails with a connection error, your Docker container is likely stopped. Start it with:
> `docker start sonarqube`

---

*This guide was generated for the HMS Project.*
