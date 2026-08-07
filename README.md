# 🚀 TaskPilot AI - Backend

An AI-powered Task Management System built using **Spring Boot**, **Spring AI**, **Groq LLM**, **Spring Security (JWT)**, and **MySQL**.

This backend provides secure REST APIs for task management and integrates AI to understand natural language commands for creating, updating, completing, deleting, and searching tasks.

---

## ✨ Features

- 🔐 JWT Authentication & Authorization
- 👤 User Registration & Login
- ✅ Task CRUD Operations
- 🤖 AI Chat Assistant
- 📝 Create tasks using natural language
- ✏️ Update task details
- ✔️ Mark tasks as completed
- 🗑️ Delete tasks
- 🔍 Search and filter tasks
- 📊 Dashboard summary
- ⚡ Spring AI Tool Calling
- 🗄️ MySQL Database Integration

---

## 🛠️ Tech Stack

### Backend

- Java 24
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Spring AI
- Maven

### Database

- MySQL

### AI

- Groq API
- Spring AI Tool Calling

---

## 📁 Project Structure

```
src
├── ai
│   ├── controller
│   ├── service
│   ├── session
│   └── tool
│
├── auth
├── config
├── dto
├── exception
├── jwt
├── security
├── task
└── user
```

---

## 🔑 Authentication

The application uses **JWT (JSON Web Token)** for authentication.

Workflow:

```
Register
      │
      ▼
Login
      │
      ▼
Receive JWT Token
      │
      ▼
Send JWT in Authorization Header
      │
      ▼
Access Protected APIs
```

---

## 🤖 AI Features

TaskPilot AI understands natural language such as:

```
Create a task to learn Docker tomorrow

Show my pending tasks

Complete Docker

Delete Kafka task

Change React priority to HIGH

Plan my day

Recommend what I should work on
```

The AI automatically invokes backend tools using **Spring AI Tool Calling**.

---

## 📡 REST APIs

### Authentication

```
POST /auth/register

POST /auth/login
```

### Tasks

```
GET    /tasks

POST   /tasks

PUT    /tasks/{id}

DELETE /tasks/{id}
```

### AI Chat

```
POST /ai/chat
```

---

## ⚙️ Environment Variables

Configure the following:

```properties
DB_URL=
DB_USERNAME=
DB_PASSWORD=

GROQ_API_KEY=
```

---

## ▶️ Running the Project

Clone the repository

```bash
git clone https://github.com/akhileshwebdev/taskpilot-backend.git
```

Go to project

```bash
cd taskpilot-backend
```

Run

```bash
./mvnw spring-boot:run
```

---

## 📷 Screenshots

### Login

(Add screenshot)

### Dashboard

(Add screenshot)

### AI Chat

(Add screenshot)

### Tasks

(Add screenshot)

---

## 🚀 Future Improvements

- Model Context Protocol (MCP)
- Notifications
- Email reminders
- Calendar Integration
- Docker Support
- Kubernetes Deployment
- Microservices Architecture
- AI Productivity Analytics

---

## 👨‍💻 Author

**Akhilesh B**

GitHub:
https://github.com/akhileshwebdev

---

⭐ If you like this project, give it a star!
