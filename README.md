# Loan Lending Backend Service

This project implements a backend REST service for evaluating loan applications and generating loan offers based on predefined eligibility rules.

The service accepts loan application requests, validates the input, evaluates eligibility conditions, calculates EMI and interest rates, and returns either an approved loan offer or rejection reasons.

The application is built using **Java 17 and Spring Boot**, following a layered architecture consisting of Controller, Service, Domain, and Repository layers.

---

## Tech Stack

- Java 17  
- Spring Boot 4.0.3
- Maven  
- Jakarta Bean Validation  
- JUnit for unit testing  

---

## Project Structure

The project follows a layered architecture with the following main components:

- **Controller Layer** – Handles REST endpoints and request validation  
- **Service Layer** – Orchestrates the loan evaluation workflow  
- **Domain Layer** – Contains core business logic and domain models  
- **Repository Layer** – Handles storage of processed loan applications  

Detailed explanations of architectural decisions, design patterns used, SOLID principles applied, and assumptions made during implementation are available in:

```
src/Docs/DEVELOPMENT_NOTES.md
```

---

## API Endpoint

### Create Loan Application

```
POST /applications
```

### Example Request

```json
{
  "applicant": {
    "name": "John Doe",
    "age": 30,
    "monthlyIncome": 75000,
    "employmentType": "SALARIED",
    "creditScore": 720
  },
  "loan": {
    "amount": 500000,
    "tenureMonths": 36,
    "purpose": "PERSONAL"
  }
}
```

---

## Example Approved Response

```json
{
  "applicationId": "UUID",
  "status": "APPROVED",
  "riskBand": "MEDIUM",
  "offer": {
    "interestRate": 13.5,
    "tenureMonths": 36,
    "emi": 16234.23,
    "totalPayable": 584432.23
  }
}
```

---

## Example Rejected Response

```json
{
  "applicationId": "UUID",
  "status": "REJECTED",
  "riskBand": null,
  "rejectionReasons": [
    "EMI_EXCEEDS_60_PERCENT"
  ]
}
```

---

## Running the Application

### Prerequisites

- Java 17  
- Maven  

### Run the service

```
mvn spring-boot:run
```

The application will start on:

```
http://localhost:8080
```

---

## Notes

For simplicity and ease of running the project across different environments, the current implementation uses **in-memory storage** to persist processed loan applications.

More detailed design explanations and future improvement considerations are documented in:

```
src/Docs/DEVELOPMENT_NOTES.md
```
