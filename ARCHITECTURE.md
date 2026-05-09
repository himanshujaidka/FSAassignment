# System Architecture Documentation

## Overview

The Equipment Borrowing Management System is a distributed microservices application designed with scalability, maintainability, and resilience in mind. The system is built using Spring Boot, Spring Cloud, and React.

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                             │
│                    (React Web Application)                       │
│                      Port: 3000                                  │
└──────────────────────────┬──────────────────────────────────────┘
                           │ HTTP/REST/JSON
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API GATEWAY LAYER                             │
│              (Spring Cloud Gateway)                              │
│            Port: 8080  (Entry Point)                            │
│  • Request Routing                                              │
│  • Load Balancing                                               │
│  • CORS Configuration                                           │
│  • Authentication/Authorization                                │
└────┬────────────────┬────────────────┬────────────────┬────────┘
     │                │                │                │
     │                │                │                │
     ▼                ▼                ▼                ▼
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  USER    │    │EQUIPMENT │    │BORROWING │    │  OTHER   │
│ SERVICE  │    │ SERVICE  │    │ SERVICE  │    │SERVICES  │
│Port:8001 │    │Port:8002 │    │Port:8003 │    │          │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
     │                │                │
     └────────────────┴────────────────┘
                      │
                      ▼
         ┌─────────────────────────┐
         │   DATABASE LAYER        │
         │   MySQL 8.0             │
         │   Port: 3306            │
         │                         │
         │ ┌─────────────────────┐ │
         │ │ equipment_borrowing │ │
         │ │ _db                 │ │
         │ └─────────────────────┘ │
         └─────────────────────────┘
```

---

## Component Details

### 1. Frontend Layer (React)

**Location:** `/frontend`

**Responsibilities:**
- User authentication/login interface
- Equipment browser and search
- Borrowing request interface
- Borrowing history tracking
- Responsive UI

**Key Components:**
- `App.jsx` - Main application component
- `components/` - Reusable UI components
  - `Navbar.jsx` - Navigation
  - `ProtectedRoute.jsx` - Auth guard
- `pages/` - Page components
  - `Login.jsx` - Authentication
  - `EquipmentList.jsx` - Equipment viewer
  - `BorrowEquipment.jsx` - Borrowing interface
  - `MyBorrowings.jsx` - History
- `context/AuthContext.js` - Global auth state
- `api/axios.js` - HTTP client

**Technology:**
- React 18+
- Axios
- React Router
- CSS3
- Context API

### 2. API Gateway Layer (Spring Cloud Gateway)

**Location:** `/api-gateway/api-gateway`

**Responsibilities:**
- Single entry point for all client requests
- Route requests to appropriate microservices
- Handle cross-cutting concerns (CORS, logging)
- JWT token validation
- Load balancing

**Configuration:**
```yaml
Routes:
- /api/auth/** → User Service (8001)
- /api/equipment/** → Equipment Service (8002)
- /api/borrowings/** → Borrowing Service (8003)
```

**Key Classes:**
- `ApiGatewayApplication.java` - Main application
- `security/JWTAuthFilter.java` - JWT validation
- `config/CorsGlobalConfig.java` - CORS handling

**Dependencies:**
- spring-cloud-starter-gateway
- spring-boot-starter-actuator

---

## 3. Microservices

### 3.1 User Service (Port 8001)

**Location:** `/user-service`

**Responsibilities:**
- User registration and authentication
- JWT token generation and validation
- User profile management
- Role-based access control

**Key Entities:**
```java
User {
  id: Long
  username: String (unique)
  email: String (unique)
  password: String (hashed)
  role: Role (ADMIN, USER)
  createdAt: Timestamp
}
```

**Key APIs:**
```
POST /auth/register         - User registration
POST /auth/login            - User login (returns JWT)
GET /auth/profile           - Get user profile
PUT /auth/profile           - Update profile
```

**Key Classes:**
- `controller/AuthController.java`
- `service/AuthService.java`
- `entity/User.java`
- `repository/UserRepository.java`

**Database:**
- Table: `users`

---

### 3.2 Equipment Service (Port 8002)

**Location:** `/equipment-service`

**Responsibilities:**
- Equipment catalog management
- Equipment inventory tracking
- Equipment availability management
- CRUD operations for equipment

**Key Entities:**
```java
Equipment {
  id: Long
  name: String
  description: String
  quantity: Integer (total)
  status: EquipmentStatus (AVAILABLE, ISSUED, MAINTENANCE)
  createdAt: Timestamp
}
```

**Key APIs:**
```
GET /equipment                  - List all equipment
GET /equipment/{id}             - Get specific equipment
POST /equipment                 - Create equipment (Admin)
PUT /equipment/{id}             - Update equipment (Admin)
DELETE /equipment/{id}          - Delete equipment (Admin)
```

**Key Classes:**
- `controller/EquipmentController.java`
- `service/EquipmentService.java`
- `entity/Equipment.java`
- `repository/EquipmentRepository.java`
- `mapper/EquipmentMapper.java`

**Database:**
- Table: `equipment`

---

### 3.3 Borrowing Service (Port 8003)

**Location:** `/borrowing-service`

**Responsibilities:**
- Manage equipment borrowing requests
- Track borrowing history
- Handle equipment returns
- Calculate overdue items

**Key Entities:**
```java
BorrowRecord {
  id: Long
  userId: Long
  equipmentId: Long
  borrowDate: Timestamp
  returnDate: Timestamp
  actualReturnDate: Timestamp (nullable)
  status: Status (ACTIVE, RETURNED, OVERDUE)
  createdAt: Timestamp
}
```

**Key APIs:**
```
POST /borrowings                    - Create borrowing
GET /borrowing/user/{userId}        - Get user's borrowings
GET /borrowings/{borrowingId}       - Get specific borrowing
PUT /borrowings/{borrowingId}/return - Return equipment
GET /borrowings                     - Get all borrowings (Admin)
```

**Key Classes:**
- `controller/BorrowController.java`
- `service/BorrowService.java`
- `entity/BorrowRecord.java`
- `repository/BorrowRecordRepository.java`
- `client/EquipmentClient.java` (REST client)
- `dto/BorrowRequestDTO.java`
- `dto/BorrowResponseDTO.java`

**Database:**
- Table: `borrowing_records`
- Foreign Keys: `user_id`, `equipment_id`

---

## 4. Database Layer

**Database:** MySQL 8.0  
**Port:** 3306

### Schema

```sql
-- Users Table
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY unique_username (username),
  UNIQUE KEY unique_email (email)
);

-- Equipment Table
CREATE TABLE equipment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  quantity INT NOT NULL,
  status VARCHAR(50) DEFAULT 'AVAILABLE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Borrowing Records Table
CREATE TABLE borrowing_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  equipment_id BIGINT NOT NULL,
  borrow_date TIMESTAMP NOT NULL,
  return_date TIMESTAMP NOT NULL,
  actual_return_date TIMESTAMP NULL,
  status VARCHAR(50) DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_equipment_id (equipment_id),
  INDEX idx_status (status),
  INDEX idx_borrow_date (borrow_date)
);
```

### Indexes

- `users`: ON `username`, `email`
- `equipment`: ON `status`, `name`
- `borrowing_records`: ON `user_id`, `equipment_id`, `status`, `borrow_date`

---

## Communication Patterns

### 1. Synchronous Communication (REST)

Services communicate via HTTP REST APIs:

```
Frontend → API Gateway → Microservice → Database
```

**Example:** Get Equipment
```
GET /api/equipment/1
→ API Gateway routes to Equipment Service
→ Equipment Service queries Database
→ Returns JSON response
```

### 2. Service-to-Service Communication

Microservices call each other when needed:

```
Borrowing Service → Equipment Service
(Check equipment availability)
```

**Implementation:** RestTemplate or WebClient

```java
@FeignClient(name = "equipment-service")
public interface EquipmentClient {
    @GetMapping("/equipment/{id}")
    Equipment getEquipment(@PathVariable Long id);
}
```

### 3. Asynchronous Communication (Future)

For scalability, consider adding:
- Message Queue (RabbitMQ/Kafka)
- Event-driven architecture
- Email notifications for overdue items

---

## Authentication & Security

### JWT Token Flow

```
1. User Login
   POST /auth/login → User Service
   
2. Generate JWT Token
   User Service creates token with:
   - User ID
   - Username
   - Role
   - Expiration (24 hours default)
   
3. Client stores token (localStorage)
   
4. Subsequent Requests
   Header: Authorization: Bearer <token>
   
5. API Gateway validates token
   Using JWTAuthFilter
   
6. Request routed to appropriate service
```

### Token Structure

```
Header.Payload.Signature

Payload contains:
{
  "sub": "john_doe",
  "userId": 1,
  "role": "USER",
  "iat": 1686771200,
  "exp": 1686857600
}
```

### Security Layers

1. **API Gateway**: JWT validation, CORS handling
2. **Services**: Spring Security configuration
3. **Database**: Encrypted passwords (bcrypt)
4. **Transport**: HTTPS (production)

---

## Data Flow Scenarios

### Scenario 1: User Registration & Login

```
1. User submits registration form
   Frontend: POST /api/auth/register
   
2. API Gateway routes to User Service
   
3. User Service:
   - Validates input
   - Hashes password
   - Saves to database
   - Returns user info
   
4. Frontend stores token from login

5. User authenticated for subsequent requests
```

### Scenario 2: Borrow Equipment

```
1. User selects equipment
   Frontend: POST /api/borrowings
   
2. API Gateway routes to Borrowing Service
   
3. Borrowing Service:
   - Validates user (from JWT)
   - Calls Equipment Service via HTTP
   - Checks availability
   - Creates borrow record
   - Returns confirmation
   
4. Frontend updates UI with borrow details
```

### Scenario 3: Return Equipment

```
1. User initiates return
   Frontend: PUT /api/borrowings/{id}/return
   
2. Borrowing Service:
   - Updates borrow record
   - Sets actual_return_date
   - Changes status to RETURNED
   - Equipment automatically available
   
3. Frontend shows success message
```

---

## Scalability Considerations

### Current Architecture (Dev/Test)
- Single instance per service
- Shared MySQL database
- All on localhost

### Production Scaling

1. **Horizontal Scaling**
   - Run multiple instances of each service
   - Use load balancer (nginx, HAProxy)
   - Service registration (Eureka)

2. **Database Scaling**
   - Master-Slave replication
   - Database connection pooling
   - Query optimization & indexing

3. **Caching**
   - Redis for session management
   - Cache equipment catalog
   - Cache user data

4. **Message Queue**
   - RabbitMQ for async operations
   - Notification service
   - Event sourcing

5. **Monitoring & Logging**
   - ELK Stack (Elasticsearch, Logstash, Kibana)
   - Prometheus for metrics
   - Distributed tracing (Sleuth, Zipkin)

---

## Deployment Architecture

### Docker Deployment

```
┌─────────────────────────────────────────┐
│          Docker Container Layer         │
├─────────────────────────────────────────┤
│                                         │
│  ┌──────────┐  ┌──────────┐ ┌────────┐ │
│  │ User Svc │  │ Equip Svc│ │Borrow│ │
│  │Container │  │Container │ │Svc   │ │
│  └──────────┘  └──────────┘ └────────┘ │
│                                         │
│  ┌────────────────┐ ┌────────────────┐ │
│  │  API Gateway   │ │    Frontend    │ │
│  │   Container    │ │   Container    │ │
│  └────────────────┘ └────────────────┘ │
│                                         │
└─────────────────────────────────────────┘
           ▼ (shared network)
┌─────────────────────────────────────────┐
│     MySQL Container (Database)          │
└─────────────────────────────────────────┘
```

### Kubernetes Deployment (Advanced)

```
┌────────────────────────────────────┐
│      Kubernetes Cluster            │
├────────────────────────────────────┤
│  ┌──────────────────────────────┐  │
│  │ API Gateway Service & Pod    │  │
│  └──────────────────────────────┘  │
│  ┌─────────┐ ┌─────────┐ ┌──────┐ │
│  │User Svc │ │Equip Svc│ │Borrow│ │
│  │Deployment        Pod │ │Pods  │ │
│  └─────────┘ └─────────┘ └──────┘ │
│  ┌──────────────────────────────┐  │
│  │  MySQL StatefulSet           │  │
│  └──────────────────────────────┘  │
└────────────────────────────────────┘
```

---

## Technology Stack Summary

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Frontend | React 18 | User interface |
| Frontend | Axios | HTTP client |
| Gateway | Spring Cloud Gateway | Request routing |
| Services | Spring Boot 3.2/4.0 | Microservices framework |
| Services | Spring Security | Authentication |
| Services | Spring Data JPA | ORM |
| Database | MySQL 8.0 | Persistence |
| Auth | JWT (JJWT) | Token-based auth |
| Utils | Lombok | Code generation |
| Build | Maven | Build tool |
| Container | Docker | Containerization |
| Orchestration | Docker Compose | Multi-container |

---

## Future Enhancements

1. **Event-Driven Architecture**
   - Add Kafka for event streaming
   - Implement event sourcing

2. **Advanced Features**
   - Equipment reservations
   - Predictive maintenance
   - Analytics dashboard
   - Mobile app

3. **Operational Excellence**
   - Kubernetes deployment
   - Service mesh (Istio)
   - Distributed tracing
   - Advanced monitoring

4. **Security Enhancements**
   - OAuth 2.0 / OpenID Connect
   - Two-factor authentication
   - Rate limiting per user
   - API key management

---

## Conclusion

This microservices architecture provides:
- ✅ Scalability - Services can scale independently
- ✅ Maintainability - Clear service boundaries
- ✅ Resilience - Failure isolation
- ✅ Flexibility - Technology choice per service
- ✅ Testability - Service-level testing

For questions or improvements, please open an issue on GitHub.

---

Last Updated: May 9, 2026
Version: 1.0.0

