# Employee Management System

A full-stack Employee Management System built using Spring Boot, MySQL, HTML, CSS, and JavaScript. The application provides secure employee management functionality with JWT authentication and Role-Based Access Control.

The system allows administrators to manage employee records while normal users have restricted access to view employee information.

## 🚀 Live Demo

🌐 **Live Application:**  
https://employee-management-system-ovmu.onrender.com

📖 **Swagger API Documentation:**  
https://employee-management-system-ovmu.onrender.com/swagger-ui/index.html

---

## 📌 Project Overview

The Employee Management System is a web-based application designed to manage employee information efficiently.

The application provides functionality for:

- Secure user authentication
- JWT-based authorization
- Role-Based Access Control
- Employee CRUD operations
- Employee search functionality
- Input validation
- Secure password encryption
- RESTful APIs
- Cloud database integration
- Live deployment

The project follows a layered architecture to maintain separation of concerns and improve maintainability.

---

# 🛠️ Technologies Used

## Backend

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- Hibernate
- JWT Authentication
- Maven

## Frontend

- HTML5
- CSS3
- JavaScript

## Database

- MySQL
- Aiven Cloud MySQL

## Deployment

- Render

## API Documentation

- Swagger / OpenAPI

## Version Control

- Git
- GitHub

## ✨ Features

### 🔐 Authentication & Security

- User Registration
- User Login
- JWT Authentication
- BCrypt Password Encryption
- Stateless Session Management
- Role-Based Authorization
- Protected REST APIs

### 👨‍💼 Admin Features

Administrators can:

- Add employees
- View employees
- Update employee details
- Delete employees
- Search employees
- Access the complete employee management dashboard

### 👤 User Features

Normal users can:

- Login securely
- View employee information
- Search employee records

Users have restricted permissions and cannot perform administrative operations such as adding, updating, or deleting employees.

### 👥 Employee Management

The system manages the following employee information:

- Employee Code
- First Name
- Last Name
- Email
- Phone Number
- Department
- Designation
- Salary
- Joining Date
- Employment Status

---

# 🏗️ System Architecture

The application follows a layered architecture.

```text
                ┌──────────────────┐
                │     Frontend     │
                │ HTML / CSS / JS  │
                └────────┬─────────┘
                         │
                         │ REST API
                         ▼
                ┌──────────────────┐
                │   Controller     │
                │ Spring Boot REST │
                └────────┬─────────┘
                         │
                         ▼
                ┌──────────────────┐
                │     Service      │
                │ Business Logic   │
                └────────┬─────────┘
                         │
                         ▼
                ┌──────────────────┐
                │   Repository     │
                │ Spring Data JPA  │
                └────────┬─────────┘
                         │
                         ▼
                ┌──────────────────┐
                │   MySQL Database │
                │      Aiven       │
                └──────────────────┘


☁️ Deployment Architecture

The application is deployed using cloud services.

                 User
                  │
                  ▼
        ┌─────────────────────┐
        │   Render Hosting    │
        │                     │
        │  Employee Management│
        │     Application     │
        └──────────┬──────────┘
                   │
                   │ JDBC Connection
                   ▼
        ┌─────────────────────┐
        │     Aiven Cloud     │
        │    MySQL Database   │
        └─────────────────────┘
