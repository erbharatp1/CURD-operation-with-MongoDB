# Airport Domain Demo

This project is a simple demonstration of a Spring Boot application using Domain-Driven Design (DDD) principles to manage airport flights and passengers, with MongoDB as the database.

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Docker and Docker Compose

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/CURD-operation-with-MongoDB.git
    cd CURD-operation-with-MongoDB
    ```

2.  **Start the MongoDB container:**
    ```bash
    mise run start-mongo
    ```

3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

The application will be available at `http://localhost:8080`.

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

To run the test suite, use the following command:

```bash
./mvnw test
```
