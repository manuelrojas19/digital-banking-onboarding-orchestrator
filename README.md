# Digital Onboarding Orchestrator API

This project simulates an Orchestrator API that communicates with Keycloak to register a user simulating a digital banking onboarding process and also provides authentication capabilities. The API is built using Spring with Kotlin, and the project includes integration testing with Testcontainers and deployment using Docker Compose.

## Features

- User registration via API
- Communication with Keycloak for user management
- Password generation and retrieval
- Integration testing with Testcontainers
- Docker Compose setup for easy deployment

## Technologies Used

- **Spring Boot**: Framework for building the API
- **Kotlin**: Programming language for the project
- **Keycloak**: Identity and access management for user registration
- **Testcontainers**: Integration testing with containers
- **Docker Compose**: Container orchestration for deployment

## Getting Started

### Prerequisites

- Docker
- Docker Compose
- Java 11+
- Keycloak server

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/digital-onboarding-api.git
    cd digital-onboarding-api
    ```

2. Set up environment variables:
   Create a `.env` file in the project root and add the necessary environment variables for Keycloak:
    ```env
    KEYCLOAK_URL=http://localhost:8080/auth
    KEYCLOAK_REALM=your-realm
    KEYCLOAK_CLIENT_ID=your-client-id
    KEYCLOAK_CLIENT_SECRET=your-client-secret
    ```

3. Build and start the application with Docker Compose:
    ```sh
    docker-compose up --build
    ```

### Usage

The API provides endpoints for user registration. The main endpoint is:

- **POST /api/register**: Registers a new user and returns a generated password.

#### Example Request

```http
POST /api/register
Content-Type: application/json

{
    "username": "newuser",
    "email": "newuser@example.com"
}
```

#### Example Response

```http
{
    "username": "newuser",
    "password": "generatedPassword123"
}
```

#### Running Tests

The project includes integration tests using Testcontainers. To run the tests, use the following command:
```sh
./gradlew test
```

#### Deployment

The application is containerized using Docker. You can deploy it using Docker Compose with the following command:

```sh
cd docker-compose
docker-compose up -d
```

#### Contributing

Contributions are welcome! Please fork the repository and submit a pull request

#### License

This project is licensed under the MIT License.