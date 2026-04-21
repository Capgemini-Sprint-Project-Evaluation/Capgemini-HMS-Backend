# SonarQube Quick Start Setup

## 1. Start the SonarQube Server
Use Docker to quickly stand up a local SonarQube instance. Open a terminal and run:

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts
```

> [!NOTE]
> SonarQube can take 1-2 minutes to fully initialize. Once ready, you can access the dashboard at: http://localhost:9000

## 2. Generate a Login Token
1. Go to **http://localhost:9000**
2. Login with standard default credentials:
   - Username: `admin`
   - Password: `admin` (You will be prompted to change this immediately)
3. In the top right corner, click your profile icon -> **My Account**.
4. Navigate to the **Security** tab.
5. Generate a new Token (give it a name like 'HMS-Local') and **copy it**.

## 3. Run the Analysis
To properly analyze the HMS application, you must first run the test suite to generate the JaCoCo coverage reports, and then push the results to SonarQube.

From the project root directory (`C:\Capgemini-HMS-Backend`), run:

```powershell
.\mvnw.cmd clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=YOUR_GENERATED_TOKEN_HERE
```

## 4. View Your Results
Once the command completes, return to **http://localhost:9000**. Your project `HMS System` will appear on the dashboard displaying full metrics including:
- Bugs & Vulnerabilities
- Technical Debt (Code Smells)
- Test Coverage %
