# ACME College Management System

Welcome to the ACME College Management System! This project is designed to manage various entities within the college, including Courses, Course Registrations, Professors, Students, Club Memberships, Membership Cards, Student Clubs, as well as handling security and user-related functionalities.

## Introduction

The ACME College Management System aims to streamline the management of different aspects within the college, providing a comprehensive set of CRUD (Create, Read, Update, Delete) REST APIs for efficient data handling. This README serves as a guide to understand, set up, and contribute to the project.

## Roles and Privileges

The system has two roles: 
- **Admin**: Has full access to all CRUD operations on all entities.
- **User**: Has restricted access based on specific privileges for each entity.
  
## Entities

### Student

- **GET /api/v1/student**: Retrieve all students
- **GET /api/v1/student/{id}**: Retrieve a specific student
- **POST /api/v1/student**: Create a new student
- **PUT /api/v1/student/{id}**: Update an existing student
- **DELETE /api/v1/student/{id}**: Delete a student

### Course

- **GET /api/v1/course**: Retrieve all courses
- **GET /api/v1/course/{id}**: Retrieve a specific course
- **POST /api/v1/course**: Create a new course
- **PUT /api/v1/course/{id}**: Update an existing course
- **DELETE /api/v1/course/{id}**: Delete a course
  
### CourseRegistration

- **GET /api/v1/courseregistration**: Retrieve all course registrations
- **GET /api/v1/courseregistration/{id}**: Retrieve a specific course registration
- **POST /api/v1/courseregistration**: Create a new course registration
- **PUT /api/v1/courseregistration/{id}**: Update an existing course registration
- **DELETE /api/v1/courseregistration/{id}**: Delete a course registration

### Professor

- **GET /api/v1/professor**: Retrieve all professors
- **GET /api/v1/professor/{id}**: Retrieve a specific professor
- **POST /api/v1/professor**: Create a new professor
- **PUT /api/v1/professor/{id}**: Update an existing professor
- **DELETE /api/v1/professor/{id}**: Delete a professor

### MembershipCard

- **GET /api/v1/membershipcard**: Retrieve all membership cards
- **GET /api/v1/membershipcard/{id}**: Retrieve a specific membership card
- **POST /api/v1/membershipcard**: Create a new membership card
- **PUT /api/v1/membershipcard/{id}**: Update an existing membership card
- **DELETE /api/v1/membershipcard/{id}**: Delete a membership card

### ClubMembershipCard

- **GET /api/v1/clubmembership**: Retrieve all club memberships
- **GET /api/v1/clubmembership/{id}**: Retrieve a specific club membership
- **POST /api/v1/clubmembership**: Create a new club membership
- **PUT /api/v1/clubmembership/{id}**: Update an existing club membership
- **DELETE /api/v1/clubmembership/{id}**: Delete a club membership

### StudentClub

- **GET /api/v1/studentclub**: Retrieve all student clubs
- **GET /api/v1/studentclub/{id}**: Retrieve a specific student club
- **POST /api/v1/studentclub**: Create a new student club
- **PUT /api/v1/studentclub/{id}**: Update an existing student club
- **DELETE /api/v1/studentclub/{id}**: Delete a student club
  
## Technologies Used

- **Java**: Programming language
- **JPA**: Java Persistence API for managing relational data in Java applications
- **Hibernate**: Object-relational mapping for database interaction
- **JUnit**: Testing framework for unit tests


## Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/YuanyuanLiu2304/rest-acmecollege.git
    cd rest-acmecollege
    ```

2. Build and run the project.

3. Access the APIs at `http://localhost:8080/rest-acmecollege/api/v1/student`


## Postman Collection

Use the provided [Postman Collection] to test the project's APIs. Import the collection into Postman and execute the requests to validate the functionality.

## Running Tests

Execute JUnit tests to ensure the correctness of your APIs:

```bash
mvn test
