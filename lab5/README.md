# JWT Authentication Lab

This project demonstrates JWT authentication in Spring Boot, including protected endpoints and cookie-based token storage.

## Features

- JWT Authentication with secure HTTP-only cookies
- Role-based authorization using Spring Security
- Protected endpoints with different authorization levels
- Docker containerization with PostgreSQL and Redis
- User registration and login endpoints

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 21
- Maven

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Build and run using Docker Compose:

```bash
docker-compose up -d
```

### Testing the Application

#### Default Users

The application initializes with two users:

- Username: `admin` / Password: `admin` (Role: ADMIN)
- Username: `user` / Password: `user` (Role: USER)

#### API Endpoints

Public Endpoints:
- `GET /api/public`: Accessible to everyone
- `POST /auth/register`: Register a new user
- `POST /auth/login`: Log in with existing credentials

Protected Endpoints:
- `GET /api/protected`: Requires authentication
- `GET /api/admin`: Requires ADMIN role
- `GET /api/user`: Requires USER role
- `GET /api/profile`: Requires authentication, shows current user info

#### Testing with curl

1. Login to get JWT token:

```bash
curl -X POST http://localhost:8081/auth/login -H "Content-Type: application/json" -d '{"username":"hassan","password":"hassan"}' -c cookies.txt
```

2. Access protected endpoint with the cookie:

```bash
curl http://localhost:8081/api/protected -b cookies.txt
```

3. Access protected endpoint with Authorization header:

```bash
curl http://localhost:8081/api/protected -H "Authorization: Bearer <token>"
```

4. Access endpoint without authentication (should fail):

```bash
curl http://localhost:8081/api/protected
```

## Security Implementation

- JWT tokens stored in HTTP-only cookies for XSS protection
- Support for both cookie and Authorization header authentication
- Role-based access control
- Password encryption with BCrypt 