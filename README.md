# Equipment Borrowing Management System

A microservices-based application for managing equipment borrowing, user authentication, and equipment inventory. Built with Spring Boot, Spring Cloud, React, and MySQL.

---

## System Architecture

This is a distributed microservices architecture consisting of:

### Backend Services
1. **API Gateway** - Entry point for all client requests
2. **User Service** - User authentication and management
3. **Equipment Service** - Equipment inventory management
4. **Borrowing Service** - Equipment borrowing and return management

### Frontend
- **React Application** - User-facing web interface

### Database
- **MySQL** - Persisting user, equipment, and borrowing data

---

## Project Structure

```
assignment/
├── api-gateway/                    # Spring Cloud Gateway
│   ├── api-gateway/
│   │   ├── src/
│   │   │   ├── main/java/com/      # Gateway controllers & configuration
│   │   │   └── resources/
│   │   │       └── application.yml  # Gateway configuration
│   │   └── pom.xml                 # Maven dependencies
│   └── target/                      # Compiled binaries
│
├── user-service/                   # User authentication & management
│   ├── src/
│   │   ├── main/java/com/          # User controllers, services, models
│   │   └── resources/
│   │       └── application.properties
│   └── pom.xml
│
├── equipment-service/              # Equipment catalog management
│   ├── src/
│   │   ├── main/java/com/
│   │   └── resources/
│   └── pom.xml
│
├── borrowing-service/              # Borrowing operations
│   ├── src/
│   │   ├── main/java/com/
│   │   └── resources/
│   └── pom.xml
│
├── frontend/                       # React web application
│   ├── src/
│   │   ├── App.jsx                 # Main application component
│   │   ├── components/             # Reusable components
│   │   ├── pages/                  # Page components
│   │   ├── context/                # React context (Auth)
│   │   └── api/                    # Axios API client
│   ├── public/                     # Static assets
│   ├── package.json                # NPM dependencies
│   └── README.md                   # Frontend-specific docs
│
├── docker-compose.yml              # Database setup
└── README.md                       # This file
```

---

## Technology Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.2.5/4.0.5** - Framework
- **Spring Cloud 2023.0.1** - Microservices
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - ORM
- **MySQL** - Database
- **JWT (JJWT)** - Token-based authentication
- **Lombok** - Reduce boilerplate code

### Frontend
- **React 18** - UI library
- **Axios** - HTTP client
- **React Context** - State management
- **CSS** - Styling

### Tools & Build
- **Maven 3+** - Build automation
- **Docker** - Containerization
- **Git** - Version control

---

## Getting Started

### Prerequisites

- Java 21 JDK
- Maven 3.8+
- Node.js 16+
- npm 8+
- MySQL 8.0+
- Git

### Installation & Setup

#### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd assignment
```

#### Step 2: Set Up MySQL Database

```bash
# Using docker-compose (recommended)
docker-compose -f docker-compose.yml up -d

# Or manually create database
mysql -u root -p
CREATE DATABASE equipment_borrowing_db;
```

Alternatively, update service `application.properties` with your MySQL connection details:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/equipment_borrowing_db
spring.datasource.username=root
spring.datasource.password=your_password
```

#### Step 3: Build Backend Services

```bash
# Build all backend services
cd api-gateway/api-gateway
mvn clean package
cd ../../

cd user-service
mvn clean package
cd ../

cd equipment-service
mvn clean package
cd ../

cd borrowing-service
mvn clean package
cd ../
```

#### Step 4: Run Backend Services

Start each service in a separate terminal:

**Terminal 1 - User Service** (Port 8001)
```bash
cd user-service
mvn spring-boot:run
```

**Terminal 2 - Equipment Service** (Port 8002)
```bash
cd equipment-service
mvn spring-boot:run
```

**Terminal 3 - Borrowing Service** (Port 8003)
```bash
cd borrowing-service
mvn spring-boot:run
```

**Terminal 4 - API Gateway** (Port 8080)
```bash
cd api-gateway/api-gateway
mvn spring-boot:run
```

#### Step 5: Set Up Frontend

```bash
cd frontend
npm install
npm start
```

The application will be available at `http://localhost:3000`

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register User
- **POST** `/auth/register`
- **Body:**
  ```json
  {
    "username": "john_doe",
    "email": "john@example.com",
    "password": "secure_password"
  }
  ```

#### Login
- **POST** `/auth/login`
- **Body:**
  ```json
  {
    "username": "john_doe",
    "password": "secure_password"
  }
  ```
- **Response:**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": { "id": 1, "username": "john_doe", "email": "john@example.com" }
  }
  ```

### Equipment Endpoints

#### Get All Equipment
- **GET** `/equipment`
- **Headers:** `Authorization: Bearer <token>`

#### Get Equipment by ID
- **GET** `/equipment/{id}`

#### Create Equipment (Admin only)
- **POST** `/equipment`
- **Headers:** `Authorization: Bearer <token>`, `Content-Type: application/json`
- **Body:**
  ```json
  {
    "name": "Laptop",
    "description": "Dell XPS 13",
    "quantity": 5
  }
  ```

#### Update Equipment (Admin only)
- **PUT** `/equipment/{id}`

#### Delete Equipment (Admin only)
- **DELETE** `/equipment/{id}`

### Borrowing Endpoints

#### Borrow Equipment
- **POST** `/borrowings`
- **Headers:** `Authorization: Bearer <token>`
- **Body:**
  ```json
  {
    "equipmentId": 1,
    "userId": 1,
    "borrowDate": "2026-05-09T10:00:00",
    "returnDate": "2026-05-16T10:00:00"
  }
  ```

#### Get User's Borrowings
- **GET** `/borrowings/user/{userId}`
- **Headers:** `Authorization: Bearer <token>`

#### Return Equipment
- **PUT** `/borrowings/{borrowingId}/return`
- **Headers:** `Authorization: Bearer <token>`

#### Get All Borrowings (Admin only)
- **GET** `/borrowings`

---

## Features

### User Management
- ✅ User registration and login
- ✅ JWT-based authentication
- ✅ Secure password storage
- ✅ User role management (Admin/User)

### Equipment Management
- ✅ View equipment catalog
- ✅ Equipment inventory tracking
- ✅ Equipment details (name, description, quantity)
- ✅ Admin equipment creation/modification

### Borrowing Management
- ✅ Borrow equipment
- ✅ Track borrowing history
- ✅ Return equipment
- ✅ Borrowing status tracking
- ✅ Due date management

### Frontend Features
- ✅ User login/registration
- ✅ Equipment listing & search
- ✅ Borrowing interface
- ✅ My borrowings tracking
- ✅ Responsive design
- ✅ Protected routes

---

## Configuration

### User Service (`user-service/src/main/resources/application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/equipment_borrowing_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
server.port=8001

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=86400000
```

### API Gateway (`api-gateway/api-gateway/src/main/resources/application.yml`)

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8001
          predicates:
            - Path=/api/auth/**
        - id: equipment-service
          uri: http://localhost:8002
          predicates:
            - Path=/api/equipment/**
        - id: borrowing-service
          uri: http://localhost:8003
          predicates:
            - Path=/api/borrowings/**
server:
  port: 8080
```

---

## Database Schema

### Users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Equipment Table
```sql
CREATE TABLE equipment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  quantity INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Borrowings Table
```sql
CREATE TABLE borrowings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  equipment_id BIGINT NOT NULL,
  borrow_date TIMESTAMP NOT NULL,
  return_date TIMESTAMP,
  status VARCHAR(50) DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (equipment_id) REFERENCES equipment(id)
);
```

---

## Development Guidelines

### Code Organization
- **Controllers**: Handle HTTP requests
- **Services**: Business logic (interfaces and implementations)
- **Models/Entities**: Database entity classes
- **Repositories**: Data access layer (JPA)
- **DTOs**: Data transfer objects for API responses
- **Security**: JWT, authentication filters

### Building & Testing

```bash
# Clean build
mvn clean install

# Run specific service tests
cd user-service
mvn test

# Build with specific profile
mvn clean package -P production
```

### Common Issues & Troubleshooting

#### Port Already in Use
```bash
# Kill process on port
lsof -i :8080  # Find process ID
kill -9 <PID>
```

#### Database Connection Failed
- Ensure MySQL is running
- Check connection credentials in `application.properties`
- Verify database name exists

#### JWT Token Expired
- Refresh token by logging in again
- Token expiration configured in service properties

#### CORS Issues
- Check API Gateway CORS configuration
- Ensure frontend URL is allowed

---

## Deployment

### Docker Deployment

Build Docker images:
```bash
docker build -t equipment-borrow/user-service user-service/.
docker build -t equipment-borrow/equipment-service equipment-service/.
docker build -t equipment-borrow/borrowing-service borrowing-service/.
docker build -t equipment-borrow/api-gateway api-gateway/api-gateway/.
docker build -t equipment-borrow/frontend frontend/.
```

Use docker-compose for full stack:
```bash
docker-compose up -d
```

### Production Checklist
- [ ] Set strong JWT secret
- [ ] Configure proper database backups
- [ ] Enable HTTPS/SSL
- [ ] Set up monitoring and logging
- [ ] Configure rate limiting
- [ ] Implement proper error handling
- [ ] Set up CI/CD pipeline

---

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a Pull Request

---

## License

This project is licensed under the MIT License.

---

## Support & Contact

For issues, questions, or suggestions, please open an issue on GitHub or contact the development team.

---

## Changelog

### Version 0.0.1 (May 9, 2026)
- ✅ Initial project setup
- ✅ Microservices architecture implemented
- ✅ User authentication with JWT
- ✅ Equipment inventory management
- ✅ Borrowing management system
- ✅ React frontend with authentication
- ✅ API Gateway integration

---

## Future Enhancements

- [ ] Email notifications for borrowing reminders
- [ ] Equipment reservation system
- [ ] User profile management
- [ ] Advanced search and filtering
- [ ] Reporting and analytics
- [ ] Mobile application
- [ ] Real-time notifications (WebSocket)
- [ ] Integration with payment gateway
- [ ] Equipment maintenance tracking

