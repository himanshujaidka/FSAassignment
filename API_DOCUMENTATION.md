# API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication

All protected endpoints require a Bearer token in the Authorization header:
```
Authorization: Bearer <token>
```

---

## User Service API

### 1. Register User
Register a new user account.

**Endpoint:** `POST /auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "username": "john_doe", 
  "email": "john@example.com",
  "role": "USER"
}
```

**Errors:**
- 400: Invalid input or username already exists
- 500: Server error

---

### 2. Login User
Authenticate user and receive JWT token.

**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "SecurePassword123!"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTY4Njc3MTIwMCwiZXhwIjoxNjg2ODU3NjAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

**Errors:**
- 401: Invalid credentials
- 404: User not found
- 500: Server error

---

### 3. Get User Profile
Get authenticated user's profile information.

**Endpoint:** `GET /auth/profile`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

**Errors:**
- 401: Unauthorized (invalid or expired token)
- 404: User not found
- 500: Server error

---

## Equipment Service API

### 1. Get All Equipment
Retrieve list of all available equipment.

**Endpoint:** `GET /equipment`

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort by field (e.g., `name,asc`)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop - Dell XPS 13",
      "description": "High-performance laptop for development",
      "quantity": 5,
      "available": 3,
      "categoryId": 1
    },
    {
      "id": 2,
      "name": "Projector - Epson EH-TW7000",
      "description": "Conference room projector",
      "quantity": 2,
      "available": 1,
      "categoryId": 2
    }
  ],
  "totalElements": 15,
  "totalPages": 1,
  "currentPage": 0
}
```

**Errors:**
- 500: Server error

---

### 2. Get Equipment by ID
Get details of a specific equipment.

**Endpoint:** `GET /equipment/{id}`

**Path Parameters:**
- `id` (required): Equipment ID

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Laptop - Dell XPS 13",
  "description": "High-performance laptop for development",
  "quantity": 5,
  "available": 3,
  "categoryId": 1,
  "createdAt": "2026-05-01T10:00:00",
  "specifications": {
    "processor": "Intel i7",
    "ram": "16GB",
    "storage": "512GB SSD"
  }
}
```

**Errors:**
- 404: Equipment not found
- 500: Server error

---

### 3. Create Equipment (Admin Only)
Add new equipment to inventory.

**Endpoint:** `POST /equipment`

**Headers:**
```
Authorization: Bearer <admin-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Projector - Epson EH-TW7000",
  "description": "Conference room projector",
  "quantity": 2,
  "categoryId": 2,
  "specifications": {
    "brightness": "3000 ANSI Lumens",
    "resolution": "1024 x 768"
  }
}
```

**Response (201 Created):**
```json
{
  "id": 10,
  "name": "Projector - Epson EH-TW7000",
  "description": "Conference room projector",
  "quantity": 2,
  "available": 2,
  "categoryId": 2
}
```

**Errors:**
- 400: Invalid input
- 401: Unauthorized
- 403: Forbidden (not admin)
- 500: Server error

---

### 4. Update Equipment (Admin Only)
Update existing equipment details.

**Endpoint:** `PUT /equipment/{id}`

**Headers:**
```
Authorization: Bearer <admin-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Projector - Epson EH-TW7000",
  "description": "Updated description",
  "quantity": 3,
  "categoryId": 2
}
```

**Response (200 OK):**
```json
{
  "id": 10,
  "name": "Projector - Epson EH-TW7000",
  "description": "Updated description",
  "quantity": 3,
  "available": 2,
  "categoryId": 2
}
```

**Errors:**
- 400: Invalid input
- 401: Unauthorized
- 403: Forbidden (not admin)
- 404: Equipment not found
- 500: Server error

---

### 5. Delete Equipment (Admin Only)
Remove equipment from inventory.

**Endpoint:** `DELETE /equipment/{id}`

**Headers:**
```
Authorization: Bearer <admin-token>
```

**Response (204 No Content)**

**Errors:**
- 401: Unauthorized
- 403: Forbidden (not admin)
- 404: Equipment not found
- 500: Server error

---

## Borrowing Service API

### 1. Create Borrowing
Request to borrow equipment.

**Endpoint:** `POST /borrowings`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "equipmentId": 1,
  "userId": 1,
  "borrowDate": "2026-05-09T09:00:00Z",
  "returnDate": "2026-05-16T09:00:00Z",
  "reason": "Project requirement"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "equipmentId": 1,
  "userId": 1,
  "borrowDate": "2026-05-09T09:00:00Z",
  "returnDate": "2026-05-16T09:00:00Z",
  "actualReturnDate": null,
  "status": "ACTIVE",
  "createdAt": "2026-05-09T08:30:00Z"
}
```

**Errors:**
- 400: Invalid input or equipment not available
- 401: Unauthorized
- 404: Equipment or user not found
- 409: Quantity not available
- 500: Server error

---

### 2. Get User's Borrowings
Retrieve borrowing history for authenticated user.

**Endpoint:** `GET /borrowings/user/{userId}`

**Headers:**
```
Authorization: Bearer <token>
```

**Path Parameters:**
- `userId` (required): User ID

**Query Parameters:**
- `status` (optional): Filter by status (ACTIVE, RETURNED, OVERDUE)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "equipmentId": 1,
      "equipment": {
        "id": 1,
        "name": "Laptop - Dell XPS 13",
        "description": "High-performance laptop"
      },
      "userId": 1,
      "borrowDate": "2026-05-09T09:00:00Z",
      "returnDate": "2026-05-16T09:00:00Z",
      "actualReturnDate": null,
      "status": "ACTIVE",
      "daysRemaining": 7,
      "isOverdue": false
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "currentPage": 0
}
```

**Errors:**
- 401: Unauthorized
- 403: Forbidden (not own borrowings)
- 404: User not found
- 500: Server error

---

### 3. Get Single Borrowing
Get details of a specific borrowing record.

**Endpoint:** `GET /borrowings/{borrowingId}`

**Headers:**
```
Authorization: Bearer <token>
```

**Path Parameters:**
- `borrowingId` (required): Borrowing ID

**Response (200 OK):**
```json
{
  "id": 1,
  "equipmentId": 1,
  "equipment": {
    "id": 1,
    "name": "Laptop - Dell XPS 13",
    "description": "High-performance laptop"
  },
  "userId": 1,
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com"
  },
  "borrowDate": "2026-05-09T09:00:00Z",
  "returnDate": "2026-05-16T09:00:00Z",
  "actualReturnDate": null,
  "status": "ACTIVE",
  "daysRemaining": 7,
  "isOverdue": false
}
```

**Errors:**
- 401: Unauthorized
- 403: Forbidden
- 404: Borrowing not found
- 500: Server error

---

### 4. Return Equipment
Mark equipment as returned.

**Endpoint:** `PUT /borrowings/{borrowingId}/return`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Path Parameters:**
- `borrowingId` (required): Borrowing ID

**Request Body (optional):**
```json
{
  "condition": "GOOD",  // GOOD, FAIR, DAMAGED
  "notes": "Small scratch on keyboard"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "equipmentId": 1,
  "userId": 1,
  "borrowDate": "2026-05-09T09:00:00Z",
  "returnDate": "2026-05-16T09:00:00Z",
  "actualReturnDate": "2026-05-15T14:30:00Z",
  "status": "RETURNED",
  "condition": "GOOD",
  "notes": "Small scratch on keyboard"
}
```

**Errors:**
- 400: Invalid state (already returned)
- 401: Unauthorized
- 403: Forbidden
- 404: Borrowing not found
- 500: Server error

---

### 5. Get All Borrowings (Admin Only)
Retrieve all borrowing records in system.

**Endpoint:** `GET /borrowings`

**Headers:**
```
Authorization: Bearer <admin-token>
```

**Query Parameters:**
- `status` (optional): Filter by status
- `page` (optional): Page number
- `size` (optional): Page size
- `startDate` (optional): Filter from date (ISO-8601)
- `endDate` (optional): Filter to date (ISO-8601)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "equipmentId": 1,
      "equipment": { "id": 1, "name": "Laptop - Dell XPS 13" },
      "userId": 1,
      "user": { "id": 1, "username": "john_doe" },
      "borrowDate": "2026-05-09T09:00:00Z",
      "returnDate": "2026-05-16T09:00:00Z",
      "actualReturnDate": null,
      "status": "ACTIVE",
      "isOverdue": false
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0
}
```

**Errors:**
- 401: Unauthorized
- 403: Forbidden (not admin)
- 500: Server error

---

## Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Request successful but no content |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Access denied |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Resource conflict (e.g., already borrowed) |
| 500 | Internal Server Error - Server error |

---

## Common Response Errors

All error responses follow this format:

```json
{
  "timestamp": "2026-05-09T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Username already exists",
  "path": "/api/auth/register"
}
```

---

## Rate Limiting

API endpoints are rate-limited to prevent abuse:
- 100 requests per minute per user
- 1000 requests per hour per IP

---

## Pagination

List endpoints support pagination:

**Query Parameters:**
- `page`: 0-based page number (default: 0)
- `size`: Number of items per page (default: 20, max: 100)
- `sort`: Sort criteria (format: `field,direction` e.g., `name,asc`)

**Response Format:**
```json
{
  "content": [ /* items */ ],
  "totalElements": 50,
  "totalPages": 3,
  "currentPage": 0,
  "pageSize": 20,
  "hasNext": true,
  "hasPrev": false
}
```

---

## Examples

### Example 1: Login and Borrow Equipment

```bash
# 1. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePassword123!"
  }'

# Response contains token

# 2. Borrow equipment
curl -X POST http://localhost:8080/api/borrowings \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "equipmentId": 1,
    "userId": 1,
    "borrowDate": "2026-05-09T09:00:00Z",
    "returnDate": "2026-05-16T09:00:00Z"
  }'
```

### Example 2: Get Equipment List

```bash
curl -X GET "http://localhost:8080/api/equipment?page=0&size=10&sort=name,asc" \
  -H "Content-Type: application/json"
```

### Example 3: Return Equipment

```bash
curl -X PUT http://localhost:8080/api/borrowings/1/return \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "condition": "GOOD",
    "notes": "Equipment is in good condition"
  }'
```

---

## Webhooks (Future Enhancement)

Coming soon...

---

## API Versioning

Current API version: **v1**

Future versions will be accessed at `/api/v2`, `/api/v3`, etc.

---

## Support

For API support and questions, please contact the development team or refer to the main README.md

