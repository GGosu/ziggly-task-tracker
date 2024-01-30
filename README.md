# Java Microservices Portfolio Project

## Introduction
This project demonstrates a suite of microservices built using Spring Boot, showcasing my skills as a Java Backend Developer. It features services like authentication, user profile management, project and task management, team management, and time entry tracking, all secured with JWT authentication.

## Features
- **Authentication Service:** Manages user authentication, token generation, and session management.
- **User Profile Service:** Handles user profile operations such as updates and password changes.
- **Project Service:** Manages the lifecycle of projects.
- **Task Service:** Allows task creation, updates, and tracking within projects.
- **Team Service:** Manages team details and membership.
- **Time Entry Service:** Tracks time spent on individual tasks.

## Technologies Used
- **Spring Boot** for the framework.
- **Spring Cloud Gateway** for routing.
- **Spring Security** and **JWT** for authentication.
- **JPA & Hibernate** for database interaction.
- **MySQL** The primary database for production
- **PostgreSQL & TestContainers** Employed for integration testing, ensuring a reliable and isolated test environment.

## Getting Started
These instructions help set up the project locally for development and testing.

### Prerequisites
- **Java 11 or higher**
- **Maven:** For project management and dependencies.
- **MySQL:** Primary database for production.
- **Docker:** For running PostgreSQL in TestContainers during testing.

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/GGosu/ziggly-task-tracker.git
    ```
2. Update application.properties in each service directory with your database credentials.
3. Start each microservice

## API Endpoints

Below are some of the key endpoints available in the services:
### Auth Service

#### Auth:
1. **POST** `http://localhost:9001/api/auth/sign-up` - Register a new user.
2. **POST** `http://localhost:9001/api/auth/login` - User login.
3. **POST** `http://localhost:9001/api/auth/logout` - User logout.
4. **GET** `http://localhost:9001/api/auth/validate` - Validate user token.
5. **GET** `http://localhost:9001/api/auth/sessions` - Get user sessions.
6. **DELETE** `http://localhost:9001/api/auth/sessions/{sessionId}` - Delete a user session by session ID.


#### User:
1. **GET** `http://localhost:9001/api/profile/{userID}` - Get user profile by user ID.
2. **PUT** `http://localhost:9001/api/profile` - Update user profile.
3. **PUT** `http://localhost:9001/api/profile/change-password` - Change user password.



### Project Service
1. **POST** `http://localhost:9001/api/projects` - Create a new project.
2. **GET** `http://localhost:9001/api/projects/{projectId}` - Get project by project ID.
3. **PUT** `http://localhost:9001/api/projects/{projectId}` - Update project by project ID.
4. **DELETE** `http://localhost:9001/api/projects/{projectId}` - Delete project by project ID.
5. **GET** `http://localhost:9001/api/projects` - Get all projects.


### Task Service
1. **POST** `http://localhost:9001/api/tasks` - Create a new task.
2. **GET** `http://localhost:9001/api/tasks/{taskId}` - Get a specific task by task ID.
3. **PUT** `http://localhost:9001/api/tasks/{taskId}` - Update a specific task by task ID.
4. **DELETE** `http://localhost:9001/api/tasks/{taskId}` - Delete a specific task by task ID.
5. **GET** `http://localhost:9001/api/tasks` - Get all tasks.


### Team Service
#### Teams
1. **POST** `http://localhost:9001/api/team` - Create a new team.
2. **GET** `http://localhost:9001/api/team/{teamId}` - Get team information by team ID.
3. **PUT** `http://localhost:9001/api/team/{teamId}` - Update team information by team ID.
4. **DELETE** `http://localhost:9001/api/team/{teamId}` - Delete a team by team ID.

#### Users
1. **POST** `http://localhost:9001/api/team/{teamId}/users` - Add a user to a team.
2. **PUT** `http://localhost:9001/api/team/{teamId}/users/{userId}` - Update user's role in a team.
3. **DELETE** `http://localhost:9001/api/team/{teamId}/users/{userId}` - Remove a user from a team.
4. **GET** `http://localhost:9001/api/team/{teamId}/users` - Get users in a team.

### Time Entry Service
1. **POST** `http://localhost:9001/api/time-entries` - Create a new time entry.
2. **GET** `http://localhost:9001/api/time-entries/{timeEntryId}` - Get a specific time entry by ID.
3. **PUT** `http://localhost:9001/api/time-entries/{timeEntryId}` - Update a specific time entry by ID.
4. **DELETE** `http://localhost:9001/api/time-entries/{timeEntryId}` - Delete a specific time entry by ID.
5. **GET** `http://localhost:9001/api/time-entries` - Get all time entries.



## Testing
Unit and Integration tests are written using JUnit and Mockito.
TestContainers are used for database-related tests.

For example, testing is currently implemented in the 'team' service.

## Contributing
### Author
Yaroslav Shamarin

### License
This project is licensed under the MIT License 