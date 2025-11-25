# E-Commerce Backend (Java Spring Boot)

Production-style e-commerce backend built as a monolith using **Java Spring Boot**.  
The goal of this project is to mirror a real-world backend: authentication, authorization, domain separation, and a clean path to future microservices.

---

## 1. Features

- **User & Auth**
  - User registration & login
  - JWT-based authentication
  - Role-based access control (e.g. `ROLE_USER`, `ROLE_ADMIN`)
- **Product Management**
  - Create / update / delete products (admin)
  - List products with basic filtering
- **Cart**
  - Add / remove items from cart
  - View user cart with totals
- **Order Management**
  - Place order from cart
  - View user orders
- **Technical**
  - Layered architecture: controller → service → repository
  - Global exception handling for clean API responses
  - DTOs to separate API layer from entities
  - Prepared for future split into microservices (gateway, services, workers)

> 🔧 Status: Actively improving. This is my main backend learning & showcase project.

---

## 2. Tech Stack

- **Language:** Java  
- **Framework:** Spring Boot  
- **Security:** Spring Security, JWT  
- **Build Tool:** Maven  
- **Database:** [MySQL / PostgreSQL / H2] (update this)  
- **Others:** Lombok, Validation, etc.

---

## 3. Architecture Overview

Current phase: **Monolithic backend** with clear domain modules.

Planned evolution:

1. Start as a single Spring Boot app (fast to build & iterate).
2. Introduce API gateway and split modules into microservices (User/Auth, Product, Cart, Order).
3. Add async communication (e.g. Kafka) and proper observability.

**Packages (example):**

```text
com.rishikesh.app
 ├─ config/          # Security & app configuration
 ├─ auth/            # JWT, filters, login/signup
 ├─ user/            # User entity, repo, service
 ├─ product/         # Product domain
 ├─ cart/            # Cart domain
 ├─ order/           # Order domain
 ├─ common/          # Exceptions, constants, utils
 └─ ECommerceApplication.java
