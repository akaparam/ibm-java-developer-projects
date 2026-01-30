# Smart Clinic Management System – Architecture Design
## Architecture Design Overview

- The Smart Clinic Management System is built as a three-tier web application using Spring Boot. The system combines both MVC and REST-based approaches to support different user interactions. Admin and Doctor dashboards are rendered using Thymeleaf templates, while RESTful APIs are exposed for modules such as patients, appointments, and medical records.
- The backend is structured using a layered architecture consisting of controllers, services, and repositories. All incoming requests pass through controllers, which delegate business logic to a centralized service layer. Data persistence is handled through repositories connected to two databases: MySQL for structured relational data (such as users, doctors, patients, and appointments) and MongoDB for flexible, document-based data (such as prescriptions).
- This architecture ensures separation of concerns, easier maintenance, scalability, and smooth integration with containerization and CI/CD pipelines.

## Numbered Flow of Data and Control

1. Users (Admin, Doctor, or Patient) access the system through a web browser or REST API client.
2. Requests for dashboards are routed to Thymeleaf controllers, while API requests are routed to REST controllers based on URL and HTTP method.
3. The controller validates the request and forwards it to the appropriate service layer method.
4. The service layer applies business rules, validations, and coordinates workflows between entities.
5. The service layer calls the repository layer to perform database operations.
6. MySQL repositories interact with the MySQL database using JPA entities, while MongoDB repositories interact with MongoDB using document models.
7. Retrieved or updated data is returned back through the service and controller layers, and finally sent to the user as either rendered HTML pages or JSON responses.
