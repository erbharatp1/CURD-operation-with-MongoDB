# Airport Domain Demo

This project is a simple demonstration of a Spring Boot application using Domain-Driven Design (DDD) principles to manage airport flights and passengers, with MongoDB as the database.

## 🚀 Swagger UI
Once the application is running, you can access the Swagger UI at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🚀 Getting Started

### Prerequisites

- [mise](https://mise.jdx.dev/)

### Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/CURD-operation-with-MongoDB.git
    cd CURD-operation-with-MongoDB
    ```

2.  **Install project-specific tools:**
    This command will use `mise` to install the correct versions of Java, Maven, and MongoDB as defined in the `.mise.toml` file.
    ```bash
    mise run setup
    ```

### Running the Application

1.  **Start the MongoDB database:**
    This command will start MongoDB using Docker Compose.
    ```bash
    docker compose up -d
    ```

2.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
    *Note: If you have Docker installed, the application will automatically start MongoDB using `spring-boot-docker-compose` if it's not already running.*

## 📚 API Endpoints

The base URL for the API is `/api/flights`.

| Method | Endpoint                               | Description                     |
| ------ | -------------------------------------- | ------------------------------- |
| POST   | `/`                                    | Create a new flight             |
| GET    | `/`                                    | Get all flights                 |
| GET    | `/{flightNumber}`                      | Get a flight by its number      |
| DELETE | `/{flightNumber}`                      | Delete a flight                 |
| POST   | `/{flightNumber}/passengers`           | Add a passenger to a flight     |
| DELETE | `/{flightNumber}/passengers/{passengerId}` | Remove a passenger from a flight |
| GET    | `/{flightNumber}/passengers/{passengerId}` | Get passenger details           |

### Example: Create a Flight

**POST** `/api/flights`

```json
{
  "flightNumber": "AB123",
  "origin": "JFK",
  "destination": "LAX",
  "scheduledDeparture": "2024-12-25T10:00:00",
  "scheduledArrival": "2024-12-25T13:00:00"
}
```

## 🧪 Running Tests

To run the entire test suite, including integration tests, use the following `mise` command. The integration tests use a separate, containerized database that is managed automatically.

```bash
mise run test
```
