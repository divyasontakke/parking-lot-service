# Smart Parking Lot Management System

A RESTful Parking Lot Management Backend built using **Java, Spring Boot, Spring Data JPA, and PostgreSQL**. This project provides APIs to manage parking slots, vehicle entry and exit, parking fee calculation, booking records, and daily revenue reports.

The application follows a layered architecture to separate API handling, business logic, database operations, and exception management.

## Project Overview

Managing parking spaces manually can lead to double booking, incorrect fee calculations, and difficulty tracking available slots. This project aims to simplify parking operations through a centralized backend service.

The system allows vehicles to be assigned available parking slots, tracks their parking duration, calculates fees based on predefined pricing rules, and maintains booking and payment records.

## Objectives

- Build a complete RESTful backend service using Spring Boot.
- Manage parking slots and vehicle bookings.
- Retrieve real-time parking slot availability.
- Automatically allocate available slots when vehicles enter.
- Track currently occupied parking slots.
- Calculate parking fees based on vehicle parking duration.
- Process vehicle checkout and release parking slots.
- Generate revenue reports for specified dates.
- Implement request validation and centralized exception handling.
- Maintain persistent records using a relational database.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming |
| Spring Boot 3.3.5 | Backend application framework |
| Spring Web | REST API development |
| Spring Data JPA | Database interaction |
| Hibernate | ORM and entity management |
| PostgreSQL | Relational database |
| Flyway | Database migration management |
| Maven | Dependency management and build automation |
| Jakarta Bean Validation | Request validation |
| JUnit 5 | Unit testing |
| Postman | API testing |

## Core Features

### 1. Parking Slot Management
- Retrieve all available parking slots.
- Track slot occupancy status.
- Automatically assign an available slot to an incoming vehicle.
- Release the slot after vehicle checkout.

### 2. Vehicle Booking Management
- Register vehicle entry.
- Assign a parking slot.
- Record booking and entry timestamps.
- Retrieve active parking records.
- Maintain booking history.

### 3. Parking Fee Calculation

The system follows a fixed pricing structure:

**₹50 for every 3 hours or fraction thereof.**

| Parking Duration | Parking Fee |
|---|---:|
| 30 minutes | ₹50 |
| 2 hours 15 minutes | ₹50 |
| 3 hours | ₹50 |
| 3 hours 5 minutes | ₹100 |
| 6 hours | ₹100 |
| 6 hours 1 minute | ₹150 |

The fee is calculated by rounding the parking duration up to the next 3-hour billing block.

Formula:

`Total Fee = Ceiling(Parking Duration in Minutes / 180) × 50`

### 4. Checkout and Payment Management
- Record vehicle exit time.
- Calculate the final parking fee.
- Update payment status.
- Release the occupied parking slot.
- Maintain completed booking records.

### 5. Revenue Reporting
- Calculate aggregate parking revenue.
- Retrieve revenue for a specified date.
- Use completed booking records for revenue calculations.

### 6. Validation and Exception Handling
- Validate vehicle registration numbers.
- Validate parking slot and booking identifiers.
- Handle unavailable parking slots.
- Prevent invalid booking operations.
- Return structured JSON error responses.
- Handle exceptions centrally using Spring's REST exception handling.

## System Architecture

The application follows a layered architecture:

```text
             API CLIENT
          (Postman / Swagger)
                  |
                  v
          CONTROLLER LAYER
                  |
                  v
            SERVICE LAYER
                  |
                  v
          REPOSITORY LAYER
                  |
                  v
            POSTGRESQL
```

### Layer Responsibilities

| Layer | Responsibility |
|---|---|
| Controller | Handles HTTP requests and responses |
| Service | Implements business logic and fee calculations |
| Repository | Performs database operations |
| Model | Defines database entities |
| DTO | Transfers request and response data |
| Exception | Handles application errors |

## API Endpoints

The following is the intended API design. Confirm the actual routes against the implemented controllers before publishing them as verified endpoints.

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/api/slots/available` | Retrieve available parking slots |
| POST | `/api/bookings/entry` | Register vehicle entry |
| GET | `/api/bookings/active` | Retrieve active bookings |
| GET | `/api/bookings/{id}` | Retrieve booking details |
| GET | `/api/bookings/{id}/fare` | Calculate current parking fee |
| POST | `/api/bookings/{id}/checkout` | Process vehicle checkout |
| GET | `/api/reports/revenue?date=YYYY-MM-DD` | Retrieve daily revenue |

## Database Design

The database is designed around two primary entities:

### Parking Slots

Stores parking slot information.

- Slot ID
- Slot Number
- Slot Status

### Bookings

Stores vehicle parking and payment information.

- Booking ID
- Vehicle Number
- Parking Slot ID
- Entry Timestamp
- Exit Timestamp
- Total Fee
- Payment Status

The booking entity maintains a relationship with the parking slot entity.

## Installation and Setup

### Prerequisites

Make sure the following software is installed:

- Java Development Kit (JDK)
- Apache Maven
- PostgreSQL
- Visual Studio Code or another Java IDE
- Postman (for API testing)

### 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

### 2. Navigate to the Project Directory

```bash
cd parking-lot-service
```

### 3. Create the PostgreSQL Database

Open pgAdmin or the PostgreSQL command-line tool and execute:

```sql
CREATE DATABASE parking_db;
```

### 4. Configure Database Connection

Open:

`src/main/resources/application.properties`

Configure your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

Replace `YOUR_PASSWORD` with your local PostgreSQL password.

Keep database migration and JPA settings consistent with the project's existing configuration.

**Important:** Do not upload real database passwords or credentials to GitHub.

### 5. Build the Application

```bash
mvn clean install
```

### 6. Run the Application

```bash
mvn spring-boot:run
```

The application is expected to run on:

`http://localhost:8080`

The actual startup port depends on the application configuration.

## Testing

The project can be verified using:

- Postman for testing REST API requests and responses.
- JUnit 5 for unit testing.
- Fee calculation test cases for different parking durations.
- Validation tests for invalid vehicle numbers and booking identifiers.
- Database checks to verify slot allocation and release.

### Important Fee Calculation Test Cases

| Test Case | Expected Fee |
|---|---:|
| 1 minute | ₹50 |
| 179 minutes | ₹50 |
| 180 minutes | ₹50 |
| 181 minutes | ₹100 |
| 360 minutes | ₹100 |
| 361 minutes | ₹150 |

## Error Handling

The application is designed to use appropriate HTTP response codes.

| HTTP Status | Meaning |
|---|---|
| 200 | Request successful |
| 201 | Resource created |
| 400 | Invalid request |
| 404 | Resource not found |
| 409 | Booking or slot conflict |
| 500 | Internal server error |

## Future Enhancements

- Online payment gateway integration.
- Parking slot reservation before arrival.
- Vehicle category-based pricing.
- Admin dashboard for parking management.
- Parking usage analytics and reports.
- Automated notifications for vehicle checkout.
- Docker-based application deployment.
- Cloud deployment and monitoring.

## Project Purpose

This project demonstrates practical backend development concepts, including REST API design, layered architecture, database persistence, transaction management, validation, exception handling, and unit testing using the Spring Boot ecosystem.

## Author

**Your Name**

GitHub: [Your GitHub Profile](https://github.com/)

---

**License:** Add an appropriate open-source license before distributing this project.
