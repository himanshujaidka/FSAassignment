# Setup & Installation Guide

## Prerequisites

Ensure you have the following installed:

- **Java 21 JDK** or higher
- **Maven 3.8.1** or higher
- **Node.js 16+** and npm 8+
- **MySQL 8.0+**
- **Git** latest version
- **Docker** (optional, for containerized setup)

### Verify Installations

```bash
# Check Java
java -version

# Check Maven
mvn -version

# Check Node & npm
node -v
npm -v

# Check MySQL
mysql --version

# Check Git
git --version
```

---

## Step-by-Step Installation

### 1. Clone Repository

```bash
git clone https://github.com/yourusername/equipment-borrowing-system.git
cd assignment
```

### 2. Database Setup

#### Option A: Using Docker (Recommended)

```bash
# Start MySQL container
docker-compose -f docker-compose.yml up -d

# This creates a MySQL 8.0 container with:
# - Database: equipment_borrowing_db
# - User: root
# - Password: root
# - Port: 3306
```

#### Option B: Manual MySQL Installation

```bash
# Connect to MySQL as root
mysql -u root -p

# Create database and user
CREATE DATABASE equipment_borrowing_db;
CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'app_password';
GRANT ALL PRIVILEGES ON equipment_borrowing_db.* TO 'app_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### Option C: Update Service Configurations

Edit each service's `application.properties` or `application.yml`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/equipment_borrowing_db
spring.datasource.username=app_user
spring.datasource.password=app_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 3. Build Backend Services

Navigate to each service directory and build:

```bash
# Build User Service
cd user-service/
mvn clean package -DskipTests
cd ../

# Build Equipment Service
cd equipment-service/
mvn clean package -DskipTests
cd ../

# Build Borrowing Service
cd borrowing-service/
mvn clean package -DskipTests
cd ../

# Build API Gateway
cd api-gateway/api-gateway/
mvn clean package -DskipTests
cd ../../
```

### 4. Install Frontend Dependencies

```bash
cd frontend/
npm install
cd ../
```

---

## Running the Application

### Option A: Run Locally (Recommended for Development)

Start services in separate terminals:

**Terminal 1 - User Service (Port 8001)**
```bash
cd user-service
mvn spring-boot:run
# or
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

**Terminal 2 - Equipment Service (Port 8002)**
```bash
cd equipment-service
mvn spring-boot:run
# or
java -jar target/equipment-service-0.0.1-SNAPSHOT.jar
```

**Terminal 3 - Borrowing Service (Port 8003)**
```bash
cd borrowing-service
mvn spring-boot:run
# or
java -jar target/borrowing-service-0.0.1-SNAPSHOT.jar
```

**Terminal 4 - API Gateway (Port 8080)**
```bash
cd api-gateway/api-gateway
mvn spring-boot:run
# or
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

**Terminal 5 - Frontend (Port 3000)**
```bash
cd frontend
npm start
```

### Option B: Run with Docker

```bash
# Build Docker images
docker build -t equipment-borrow/user-service ./user-service
docker build -t equipment-borrow/equipment-service ./equipment-service
docker build -t equipment-borrow/borrowing-service ./borrowing-service
docker build -t equipment-borrow/api-gateway ./api-gateway/api-gateway
docker build -t equipment-borrow/frontend ./frontend

# Or use provided docker-compose
docker-compose up -d
```

### Verify Services are Running

```bash
# Check API Gateway (main entry point)
curl http://localhost:8080/actuator/health

# Check each service health
curl http://localhost:8001/actuator/health  # User Service
curl http://localhost:8002/actuator/health  # Equipment Service
curl http://localhost:8003/actuator/health  # Borrowing Service
```

---

## Access the Application

- **Frontend UI**: http://localhost:3000
- **API Gateway**: http://localhost:8080/api
- **User Service**: http://localhost:8001
- **Equipment Service**: http://localhost:8002
- **Borrowing Service**: http://localhost:8003

---

## Configuration Files

### User Service Configuration

**File:** `user-service/src/main/resources/application.properties`

```properties
# Server Configuration
server.port=8001
spring.application.name=user-service

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/equipment_borrowing_db
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=your-secret-key-change-in-production
jwt.expiration=86400000

# Logging
logging.level.root=INFO
logging.level.com.himanshu=DEBUG
```

### API Gateway Configuration

**File:** `api-gateway/api-gateway/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8001
          predicates:
            - Path=/api/auth/**
          filters:
            - RewritePath=/api/auth(?<segment>/?.*), $\{segment}
            
        - id: equipment-service
          uri: http://localhost:8002
          predicates:
            - Path=/api/equipment/**
          filters:
            - RewritePath=/api/equipment(?<segment>/?.*), $\{segment}
            
        - id: borrowing-service
          uri: http://localhost:8003
          predicates:
            - Path=/api/borrowings/**
          filters:
            - RewritePath=/api/borrowings(?<segment>/?.*), $\{segment}

server:
  port: 8080
```

### Equipment Service Configuration

**File:** `equipment-service/src/main/resources/application.properties`

```properties
server.port=8002
spring.application.name=equipment-service

spring.datasource.url=jdbc:mysql://localhost:3306/equipment_borrowing_db
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

### Borrowing Service Configuration

**File:** `borrowing-service/src/main/resources/application.properties`

```properties
server.port=8003
spring.application.name=borrowing-service

spring.datasource.url=jdbc:mysql://localhost:3306/equipment_borrowing_db
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

---

## Frontend Configuration

**File:** `frontend/src/api/axios.js`

```javascript
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default axiosInstance;
```

---

## Environment Variables

Create `.env` file in frontend directory:

```bash
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_JWT_STORAGE_KEY=authToken
```

---

## Testing the Setup

### 1. Test User Registration

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "TestPassword123!"
  }'
```

### 2. Test User Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "TestPassword123!"
  }'
```

### 3. Test Get Equipment

```bash
curl -X GET http://localhost:8080/api/equipment \
  -H "Content-Type: application/json"
```

### 4. Test Create Borrowing

```bash
curl -X POST http://localhost:8080/api/borrowings \
  -H "Authorization: Bearer <your_token_here>" \
  -H "Content-Type: application/json" \
  -d '{
    "equipmentId": 1,
    "userId": 1,
    "borrowDate": "2026-05-09T09:00:00Z",
    "returnDate": "2026-05-16T09:00:00Z"
  }'
```

---

## Troubleshooting

### Port Already in Use

```bash
# On Linux/Mac
lsof -i :8080  # Find process ID
kill -9 <PID>

# On Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Database Connection Failed

```bash
# Test MySQL connection
mysql -u root -p -h localhost

# Check MySQL is running
# Linux: systemctl status mysql
# Mac: brew services list
# Windows: Check Services in Admin Task Manager
```

### Maven Build Fails

```bash
# Clear local repository
rm -rf ~/.m2/repository

# Update Maven
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

### Frontend Not Loading

```bash
# Clear npm cache
npm cache clean --force
rm -rf node_modules package-lock.json
npm install

# Restart development server
npm start
```

### JWT Token Issues

- Token expired: Log in again to get new token
- Invalid token: Check token format and expiration
- Missing token: Ensure it's being sent in Authorization header

### CORS Issues

Update API Gateway or add CORS configuration to services:

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## Next Steps

1. ✅ Create test users and equipment
2. ✅ Test all API endpoints
3. ✅ Test frontend application
4. ✅ Review and customize configurations
5. ✅ Set up logging and monitoring
6. ✅ Configure production environment

For more information, see:
- [README.md](README.md) - Project overview
- [API_DOCUMENTATION.md](API_DOCUMENTATION.md) - API reference
- [ARCHITECTURE.md](ARCHITECTURE.md) - System architecture

