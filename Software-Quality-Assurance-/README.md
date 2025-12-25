# Spring Boot & Angular – QA and Testing Documentation

## Project Description
This is a full-stack web application developed using:
- **Backend**: Spring Boot with JWT authentication
- **Frontend**: Angular
- **Database**: H2 / MySQL

The project includes manual testing, unit tests, integration tests, and end-to-end (E2E) tests.

---

## Environment Setup

### Prerequisites
- Java 17+
- Maven
- Node.js (v18+)
- Angular CLI
- Git
- Google Chrome

---

## Backend Setup (Spring Boot)

```bash
cd backend
mvn clean install
mvn spring-boot:run
Backend runs on 8081
Frontend Setup (Angular):
npm install
ng serve
http://localhost:4200
Backend Unit Tests: mvn test

backend/target/site/jacoco/index.html
http://localhost:8081/swagger-ui.html
Manual Testing & QA Process

Manual test cases were executed

Test cases include:

Preconditions

Steps

Expected result

Actual result

Bugs were reported using GitHub Issues

Release readiness was evaluated using QA reports
