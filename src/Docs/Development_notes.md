# Digital Loan Lending Application – Development Notes

## Overview

This project implements a backend REST service for evaluating loan applications and generating loan offers based on predefined eligibility rules. The service accepts loan application requests, evaluates them against validation and eligibility criteria, calculates financial metrics such as EMI and interest rate, and returns either an approved offer or rejection reasons.

The application is implemented using **Spring Boot with a layered architecture**, ensuring separation of concerns and maintainability. The design emphasizes clean domain modeling, extensibility, and adherence to **OOP and SOLID principles**.

---

# Architecture

The application follows a **layered architecture** :
Domain Layer, Controller Layer, Service Layer, Repository Layer with Centralized Exception handling, unit testing


### Controller Layer
Handles REST API requests and responses. It performs request validation and delegates business logic execution to the service layer.

### Service Layer
Orchestrates the overall application flow including:

- Risk classification
- Interest rate calculation
- EMI calculation
- Eligibility evaluation
- Response generation
- Persistence of decision records

### Domain Layer
Contains the core business logic and domain models including:

- Risk classification strategies
- Interest premium calculation
- EMI calculation logic
- Eligibility rules
- Domain models and enums

### Repository Layer
Handles persistence of processed loan application decisions.

For the purpose of this exercise, an **in-memory repository** backed by a `ConcurrentHashMap` is used to store application results.

---

# Key Design Decisions

## 1. Use of In-Memory Storage

To keep the project simple and runnable without additional setup, the application currently uses **in-memory storage** to persist processed loan applications.

This ensures that the project can be cloned, unzipped, and executed immediately without requiring any external database configuration.

In a production environment, this repository layer would be replaced with a relational database such as **MySQL using JPA/Hibernate ORM**, which would allow persistent storage, indexing, and audit tracking of loan decisions.

---

# Business Rule Clarification

During implementation, the exercise description references two slightly different EMI thresholds:

- One section specifies that the loan should be rejected if **EMI exceeds 50% of monthly income**.
- Another section specifies rejection if **EMI exceeds 60% of monthly income**.

To maintain consistency with the eligibility rules section and ensure a single deterministic rule, the implementation assumes **EMI ≤ 60% of monthly income** as the eligibility threshold for loan approval.

This assumption was applied uniformly across the eligibility evaluation logic.

---

# Design Patterns Used

The application uses several design patterns to ensure modularity, extensibility, and maintainability.

---

# Strategy Pattern

The **Strategy Pattern** is used for calculating different **interest premium components**.

Current premium factors include:

- Risk premium
- Employment premium
- Loan size premium

Each premium factor is implemented as an independent strategy implementing the `InterestPremiumStrategy` interface.

### Benefits

- New premium rules can be added without modifying existing logic.
- Each strategy encapsulates a single calculation rule.
- Strategies are resolved dynamically at runtime.

This design follows the **Open-Closed Principle (OCP)** from SOLID:

> Software entities should be open for extension but closed for modification.

If new factors such as **region-based risk**, **existing customer discount**, or **loan purpose premium** are introduced in the future, they can simply be implemented as new strategy classes.

---

# Factory Pattern

The **Factory Pattern** is used to obtain the applicable interest premium strategies.

The `InterestPremiumStrategyFactory` dynamically provides the list of strategies that contribute to the final interest rate.

### Benefits

- Centralized management of strategy creation
- Loose coupling between service layer and strategy implementations
- Enables runtime polymorphism when resolving strategies

Together with the Strategy Pattern, this allows the system to remain easily extensible as new premium factors are introduced.

---

# Template Method Design Pattern

The **Template Method Design Pattern** is used in the EMI calculation component.

An abstract base class defines the structure of the EMI calculation algorithm, while the concrete implementation provides the calculation logic.

Although currently only a single EMI calculation implementation exists, this structure allows future extension.

### Example Future Scenarios

Different EMI calculation strategies may be required for:

- Promotional interest offers
- Special lending schemes
- Reducing balance vs flat interest calculations
- Partner bank integrations

With the Template Method pattern, new calculation implementations can be added **without modifying the existing base algorithm structure**.

This again supports the **Open-Closed Principle (OCP)**.

---

# Specification Pattern

Eligibility rules are implemented using the **Specification Pattern**.

Each eligibility rule is encapsulated in its own class:

- Credit score validation
- Age + tenure validation
- EMI to income ratio validation

The `EligibilityEvaluator` aggregates these specifications and evaluates them against the loan context.

### Benefits

- Each rule has a single responsibility
- Rules are easy to test independently
- New eligibility rules can be added without modifying existing logic

This approach follows the **Single Responsibility Principle (SRP)** and improves maintainability.

---

# Object Oriented Design Principles

The implementation emphasizes core OOP principles:

### Encapsulation
Domain models encapsulate the loan context and related attributes used across the business logic.

### Abstraction
Interfaces such as strategy and specification abstractions allow the service layer to interact with business rules without knowing implementation details.

### Polymorphism
Strategy implementations allow runtime polymorphic behavior when calculating interest premiums.

### Modularity
Clear separation of layers ensures components can evolve independently.

---

# SOLID Principles Applied

### Single Responsibility Principle (SRP)
Each class has a clearly defined responsibility.

Examples:
- EMI calculator handles only EMI calculation
- Strategy classes handle only premium calculations
- Specification classes handle only eligibility rules

---

### Open-Closed Principle (OCP)
The system is designed to allow extensions without modifying existing code.

Examples:
- Adding new premium strategies
- Adding new eligibility rules
- Supporting additional EMI calculation algorithms

---

### Liskov Substitution Principle (LSP)
Strategy and specification implementations can be substituted wherever their interfaces are used.

---

### Interface Segregation Principle (ISP)
Interfaces (InterestPremium, RiskClassification) are small and focused, ensuring classes only implement methods relevant to them.

---

### Dependency Inversion Principle (DIP)
High-level service components depend on abstractions (interfaces) rather than concrete implementations.

---

# Validation and Error Handling

The application uses **Bean Validation (Jakarta Validation)** to validate incoming requests.

Invalid requests return **HTTP 400 responses** with meaningful validation messages.

Unexpected failures are handled through a centralized **global exception handler**, ensuring consistent error responses.

Loan rejections due to eligibility rules are treated as **valid business outcomes**, not errors, and therefore return a normal API response with status `"REJECTED"`.

---

# Unit Testing

Unit tests have been implemented to verify key business logic components including:

- EMI calculation
- Risk band classification
- Eligibility rule evaluation

Testing these components independently ensures correctness of the financial calculations and eligibility logic.

---

# Improvements With More Time

With additional time, the following enhancements would be implemented:

- Replace in-memory repository with **MySQL database using JPA/Hibernate ORM**
- Add caching layer using **Redis** for frequently accessed application results
- Introduce **event-based audit logging** for loan decisions
- Implement **rate limiting and security controls** for production environments
- Containerize the application using **Docker** for easier deployment

---

# Conclusion

The system is designed with extensibility and maintainability in mind. By leveraging well-known design patterns, layered architecture, and SOLID principles, the implementation provides a clear and scalable foundation for a digital lending backend service.