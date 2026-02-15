# Airport Domain Demo - Domain-Driven Design with Spring Boot & MongoDB

This project demonstrates Domain-Driven Design (DDD) principles through a simplified airport operations system using MongoDB as the persistence layer. It showcases how DDD concepts translate into practical, real-world Spring Boot applications with NoSQL databases.

## 🏗️ Domain-Driven Design Architecture

### Core DDD Concepts Implemented

- **Entities**: `Flight` (Aggregate Root), `Passenger`
- **Value Objects**: `SeatAssignment`
- **Repositories**: `FlightRepository`, `PassengerRepository`
- **Domain Services**: `FlightService`
- **Factories**: `FlightFactory`
- **Ubiquitous Language**: Aviation terminology used consistently throughout

### Project Structure

```
src/main/java/com/example/airport/
├── domain/              # Domain layer - core business logic
│   ├── Flight.java      # Aggregate Root (MongoDB Document)
│   ├── Passenger.java   # Entity (Embedded in Flight)
│   └── SeatAssignment.java # Value Object
├── repository/          # Data access layer
│   ├── FlightRepository.java (MongoDB Repository)
│   └── PassengerRepository.java (MongoDB Repository)
├── service/             # Domain services
│   └── FlightService.java
├── factory/             # Object creation
│   └── FlightFactory.java
├── controller/          # Application layer
│   └── FlightController.java
└── dto/                 # Data transfer objects
    ├── FlightRequest.java
    └── PassengerRequest.java
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+ (running locally on port 27017)

### MongoDB Setup

1. **Install MongoDB:**
   - **macOS:** `brew install mongodb-community`
   - **Ubuntu:** Follow [MongoDB Ubuntu Installation Guide](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/)
   - **Windows:** Download from [MongoDB Download Center](https://www.mongodb.com/try/download/community)

2. **Start MongoDB:**
   ```bash
   # macOS/Linux
   brew services start mongodb-community
   # or
   mongod --config /usr/local/etc/mongod.conf
   
   # Windows
   net start MongoDB
   ```

3. **Verify MongoDB is running:**
   ```bash
   mongo --eval "db.adminCommand('ismaster')"
   ```

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Ensure MongoDB is running on localhost:27017
4. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### MongoDB Database

- **Database Name:** `airportdb`
- **Collections:** `flights`, `passengers`
- **Connection:** `mongodb://localhost:27017/airportdb`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/flights
```

### Endpoints

#### 1. Create Flight
**POST** `/api/flights`

Creates a new flight with validation.

**Request Body:**
```json
{
  "flightNumber": "AB123",
  "origin": "JFK",
  "destination": "LAX",
  "scheduledDeparture": "2024-12-25T10:00:00",
  "scheduledArrival": "2024-12-25T13:00:00"
}
```

**Response:** `201 Created`
```json
{
  "flightNumber": "AB123",
  "origin": "JFK",
  "destination": "LAX",
  "scheduledDeparture": "2024-12-25T10:00:00",
  "scheduledArrival": "2024-12-25T13:00:00",
  "passengers": []
}
```

#### 2. Get All Flights
**GET** `/api/flights`

**Response:** `200 OK`
```json
[
  {
    "flightNumber": "AB123",
    "origin": "JFK",
    "destination": "LAX",
    "scheduledDeparture": "2024-12-25T10:00:00",
    "scheduledArrival": "2024-12-25T13:00:00",
    "passengers": []
  }
]
```

#### 3. Get Flight by Number
**GET** `/api/flights/{flightNumber}`

**Response:** `200 OK` or `404 Not Found`

#### 4. Add Passenger to Flight
**POST** `/api/flights/{flightNumber}/passengers`

Adds a passenger to a specific flight with seat assignment validation.

**Request Body:**
```json
{
  "name": "John Doe",
  "seatNumber": "12A",
  "seatClass": "Economy"
}
```

**Response:** `200 OK` - "Passenger added successfully"
**Error:** `409 Conflict` - "Seat 12A is already assigned"

#### 5. Remove Passenger from Flight
**DELETE** `/api/flights/{flightNumber}/passengers/{passengerId}`

**Response:** `200 OK` - "Passenger removed successfully"

#### 6. Get Flights by Route
**GET** `/api/flights/route?origin=JFK&destination=LAX`

**Response:** `200 OK` - Array of flights matching the route

#### 7. Get Flights by Departure Range
**GET** `/api/flights/departures?start=2024-12-25T00:00:00&end=2024-12-25T23:59:59`

**Response:** `200 OK` - Array of flights within the time range

#### 8. Delete Flight
**DELETE** `/api/flights/{flightNumber}`

**Response:** `200 OK` - "Flight deleted successfully"

## 🧪 Testing with Postman

### Sample Postman Collection

Import the following requests into Postman:

#### 1. Create Flight
```
POST http://localhost:8080/api/flights
Content-Type: application/json

{
  "flightNumber": "UA101",
  "origin": "JFK",
  "destination": "LAX",
  "scheduledDeparture": "2024-12-25T10:00:00",
  "scheduledArrival": "2024-12-25T13:00:00"
}
```

#### 2. Add Passenger
```
POST http://localhost:8080/api/flights/UA101/passengers
Content-Type: application/json

{
  "name": "Alice Johnson",
  "seatNumber": "12A",
  "seatClass": "Economy"
}
```

#### 3. Add Another Passenger (Different Seat)
```
POST http://localhost:8080/api/flights/UA101/passengers
Content-Type: application/json

{
  "name": "Bob Smith",
  "seatNumber": "12B",
  "seatClass": "Economy"
}
```

#### 4. Try Duplicate Seat (Should Fail)
```
POST http://localhost:8080/api/flights/UA101/passengers
Content-Type: application/json

{
  "name": "Charlie Brown",
  "seatNumber": "12A",
  "seatClass": "Economy"
}
```

#### 5. Get Flight with Passengers
```
GET http://localhost:8080/api/flights/UA101
```

### Demo Scenario

1. **Create multiple flights** for different routes
2. **Add passengers** with various seat assignments
3. **Test seat conflict** by trying to assign the same seat twice
4. **Query flights** by route and time range
5. **Remove passengers** and verify changes
6. **Delete flights** and confirm removal

## 🏃‍♂️ Running Tests

Execute all tests:
```bash
mvn test
```

### Test Coverage

- **Unit Tests**: Domain logic and business rules
- **Integration Tests**: REST API endpoints
- **Repository Tests**: Data persistence layer

## 🎯 DDD Principles Demonstrated

### 1. Ubiquitous Language
- Consistent use of aviation terminology
- Domain concepts reflected in code structure
- Clear business vocabulary in method names

### 2. Bounded Context
- Flight Operations context clearly defined
- Separation of concerns between layers
- Domain logic isolated from infrastructure

### 3. Aggregate Design
- `Flight` as aggregate root managing passengers
- Consistency boundaries properly maintained
- Business invariants enforced

### 4. Domain Services
- Complex business logic in `FlightService`
- Cross-aggregate operations handled properly
- Transaction boundaries respected

### 5. Value Objects
- `SeatAssignment` as immutable value object
- Proper equality semantics implemented
- No identity, only descriptive attributes

## 🔧 Key Features

- **Seat Assignment Validation**: Prevents double-booking of seats
- **Business Rule Enforcement**: Validates flight timing and passenger data
- **RESTful API**: Clean, resource-oriented endpoints
- **Comprehensive Testing**: Unit, integration, and domain tests with embedded MongoDB
- **MongoDB Integration**: NoSQL document database for flexible data modeling
- **Embedded Documents**: Passengers stored as embedded documents within Flight aggregates

## 🚨 Error Handling

The application handles various error scenarios:

- **Invalid flight data**: Returns 400 Bad Request
- **Flight not found**: Returns 404 Not Found
- **Seat conflicts**: Returns 409 Conflict
- **Business rule violations**: Returns appropriate HTTP status codes

## 🍃 MongoDB Features Demonstrated

### Document-Based Storage
- **Flexible Schema**: MongoDB's document model allows for easy schema evolution
- **Embedded Documents**: Passengers are stored as embedded documents within Flight aggregates
- **Rich Queries**: MongoDB queries using JSON-like syntax for complex operations
- **Indexing**: Unique index on flight numbers for fast lookups

### MongoDB Queries Used
```javascript
// Find flights by departure time range
{'scheduledDeparture': {$gte: startTime, $lte: endTime}}

// Find flights by route
{'origin': 'JFK', 'destination': 'LAX'}

// Find passengers by seat class
{'seatAssignment.seatClass': 'Economy'}
```

### Data Model Benefits
- **Aggregate Consistency**: Flight and its passengers stored together
- **Atomic Operations**: Updates to flight and passengers happen atomically
- **Natural Relationships**: Embedded documents reflect domain relationships

## 🔍 MongoDB Administration

### Useful MongoDB Commands
```bash
# Connect to MongoDB
mongo

# Switch to airport database
use airportdb

# View all flights
db.flights.find().pretty()

# Find flights by origin
db.flights.find({"origin": "JFK"}).pretty()

# Count total flights
db.flights.count()

# View passengers in a specific flight
db.flights.find({"flightNumber": "UA101"}, {"passengers": 1}).pretty()

# Create index on flight number (done automatically)
db.flights.createIndex({"flightNumber": 1}, {"unique": true})
```

## 📈 Extending the Demo

Consider adding these features to explore more DDD concepts:

1. **Domain Events**: `FlightDelayedEvent`, `BoardingStartedEvent`
2. **Additional Aggregates**: `Gate`, `Aircraft`, `Crew`
3. **More Bounded Contexts**: Baggage Handling, Security, Ground Services
4. **CQRS Pattern**: Separate read and write models
5. **Event Sourcing**: Track all changes as events
6. **MongoDB Features**: GridFS for documents, Change Streams for real-time updates

## 🤝 Contributing

This is a demo project for learning DDD concepts. Feel free to fork and experiment with additional features!

## 📝 License

This project is for educational purposes and demonstrates DDD principles in a Spring Boot application.