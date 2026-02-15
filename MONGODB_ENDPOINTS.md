# Airport Domain Demo - MongoDB REST API Endpoints

This document provides a comprehensive guide to testing the MongoDB-powered Airport Domain Demo API with Postman.

## 🚀 Quick Start

### Prerequisites
1. **MongoDB** running on `localhost:27017`
2. **Java 17+** and **Maven 3.6+**
3. **Postman** for API testing

### Start the Application
```bash
# Ensure MongoDB is running
mongod

# Start the Spring Boot application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 📋 REST API Endpoints

### Base URL
```
http://localhost:8080/api/flights
```

---

## ✈️ Flight Management Endpoints

### 1. **Create Flight**
- **Method:** `POST`
- **URL:** `/api/flights`
- **Purpose:** Create a new flight document in MongoDB
- **MongoDB Operation:** `db.flights.insertOne()`

**Request Body:**
```json
{
  "flightNumber": "UA101",
  "origin": "JFK",
  "destination": "LAX",
  "scheduledDeparture": "2024-12-25T10:00:00",
  "scheduledArrival": "2024-12-25T13:00:00"
}
```

**Response:** `201 Created`
```json
{
  "_id": "UA101",
  "flightNumber": "UA101",
  "origin": "JFK",
  "destination": "LAX",
  "scheduledDeparture": "2024-12-25T10:00:00",
  "scheduledArrival": "2024-12-25T13:00:00",
  "passengers": []
}
```

---

### 2. **Get All Flights**
- **Method:** `GET`
- **URL:** `/api/flights`
- **Purpose:** Retrieve all flight documents from MongoDB
- **MongoDB Operation:** `db.flights.find()`

**Response:** `200 OK`
```json
[
  {
    "_id": "UA101",
    "flightNumber": "UA101",
    "origin": "JFK",
    "destination": "LAX",
    "scheduledDeparture": "2024-12-25T10:00:00",
    "scheduledArrival": "2024-12-25T13:00:00",
    "passengers": [...]
  }
]
```

---

### 3. **Get Flight by Number**
- **Method:** `GET`
- **URL:** `/api/flights/{flightNumber}`
- **Example:** `/api/flights/UA101`
- **Purpose:** Find specific flight document by flight number
- **MongoDB Operation:** `db.flights.findOne({"flightNumber": "UA101"})`

**Response:** `200 OK` or `404 Not Found`

---

### 4. **Get Flights by Route**
- **Method:** `GET`
- **URL:** `/api/flights/route?origin={origin}&destination={destination}`
- **Example:** `/api/flights/route?origin=JFK&destination=LAX`
- **Purpose:** Query flights by origin and destination
- **MongoDB Operation:** `db.flights.find({"origin": "JFK", "destination": "LAX"})`

---

### 5. **Get Flights by Departure Time Range**
- **Method:** `GET`
- **URL:** `/api/flights/departures?start={startTime}&end={endTime}`
- **Example:** `/api/flights/departures?start=2024-12-25T00:00:00&end=2024-12-25T23:59:59`
- **Purpose:** Find flights within a departure time window
- **MongoDB Operation:** `db.flights.find({"scheduledDeparture": {$gte: startTime, $lte: endTime}})`

---

### 6. **Delete Flight**
- **Method:** `DELETE`
- **URL:** `/api/flights/{flightNumber}`
- **Example:** `/api/flights/UA101`
- **Purpose:** Remove flight document from MongoDB
- **MongoDB Operation:** `db.flights.deleteOne({"_id": "UA101"})`

**Response:** `200 OK` - "Flight deleted successfully"

---

## 👥 Passenger Management Endpoints

### 7. **Add Passenger to Flight**
- **Method:** `POST`
- **URL:** `/api/flights/{flightNumber}/passengers`
- **Example:** `/api/flights/UA101/passengers`
- **Purpose:** Add passenger as embedded document within flight
- **MongoDB Operation:** `db.flights.updateOne({"_id": "UA101"}, {$push: {"passengers": passengerDoc}})`

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

---

### 8. **Add Passenger without Seat**
- **Method:** `POST`
- **URL:** `/api/flights/{flightNumber}/passengers`
- **Purpose:** Add passenger without pre-assigned seat

**Request Body:**
```json
{
  "name": "Jane Smith"
}
```

---

### 9. **Remove Passenger from Flight**
- **Method:** `DELETE`
- **URL:** `/api/flights/{flightNumber}/passengers/{passengerId}`
- **Example:** `/api/flights/UA101/passengers/507f1f77bcf86cd799439011`
- **Purpose:** Remove passenger from flight's embedded passengers array
- **MongoDB Operation:** `db.flights.updateOne({"_id": "UA101"}, {$pull: {"passengers": {"_id": ObjectId("507f1f77bcf86cd799439011")}}})`

**Note:** Passenger IDs are MongoDB ObjectIds (String format in this implementation)

---

## 🧪 Testing Scenarios

### Scenario 1: Complete Flight Booking Flow
```bash
# 1. Create a flight
POST /api/flights
{
  "flightNumber": "DL456",
  "origin": "ATL",
  "destination": "SEA",
  "scheduledDeparture": "2024-12-25T14:00:00",
  "scheduledArrival": "2024-12-25T17:00:00"
}

# 2. Add passengers with different seat classes
POST /api/flights/DL456/passengers
{
  "name": "Alice Johnson",
  "seatNumber": "1A",
  "seatClass": "Business"
}

POST /api/flights/DL456/passengers
{
  "name": "Bob Smith",
  "seatNumber": "12B",
  "seatClass": "Economy"
}

# 3. Try duplicate seat (should fail)
POST /api/flights/DL456/passengers
{
  "name": "Charlie Brown",
  "seatNumber": "1A",
  "seatClass": "Business"
}

# 4. Get flight with all passengers
GET /api/flights/DL456
```

### Scenario 2: MongoDB Query Testing
```bash
# Search by route
GET /api/flights/route?origin=JFK&destination=LAX

# Search by time range
GET /api/flights/departures?start=2024-12-25T00:00:00&end=2024-12-25T23:59:59

# Get all flights
GET /api/flights
```

---

## 🍃 MongoDB Document Structure

### Flight Document Example
```javascript
{
  "_id": "UA101",
  "flightNumber": "UA101",
  "origin": "JFK",
  "destination": "LAX",
  "scheduledDeparture": ISODate("2024-12-25T10:00:00.000Z"),
  "scheduledArrival": ISODate("2024-12-25T13:00:00.000Z"),
  "passengers": [
    {
      "_id": ObjectId("507f1f77bcf86cd799439011"),
      "name": "John Doe",
      "seatAssignment": {
        "seatNumber": "12A",
        "seatClass": "Economy"
      }
    },
    {
      "_id": ObjectId("507f1f77bcf86cd799439012"),
      "name": "Jane Smith",
      "seatAssignment": {
        "seatNumber": "12B",
        "seatClass": "Economy"
      }
    }
  ],
  "_class": "domain.com.bharat.airport.Flight"
}
```

---

## 🔍 MongoDB Administration Commands

### Useful MongoDB Shell Commands
```javascript
// Connect to the database
use airportdb

// View all flights
db.flights.find().pretty()

// Find flights by origin
db.flights.find({"origin": "JFK"}).pretty()

// Count total flights
db.flights.countDocuments()

// Find flights with passengers in Business class
db.flights.find({"passengers.seatAssignment.seatClass": "Business"}).pretty()

// Find flights departing today
db.flights.find({
  "scheduledDeparture": {
    $gte: ISODate("2024-12-25T00:00:00.000Z"),
    $lt: ISODate("2024-12-26T00:00:00.000Z")
  }
}).pretty()

// View indexes
db.flights.getIndexes()

// Drop all flights (for testing)
db.flights.deleteMany({})
```

---

## 📊 Sample Data

The application initializes with 3 sample flights:
- **UA101:** JFK → LAX (2 passengers)
- **UA102:** LAX → JFK (1 passenger)  
- **AA203:** ORD → MIA (3 passengers)

---

## 🎯 DDD with MongoDB Benefits

1. **Aggregate Consistency:** Flight and passengers stored together as a single document
2. **Atomic Operations:** Updates to flight and passengers happen atomically
3. **Natural Relationships:** Embedded documents reflect domain relationships
4. **Flexible Schema:** Easy to evolve the data model as business requirements change
5. **Rich Queries:** MongoDB's query language supports complex business queries
6. **Performance:** Related data stored together reduces joins and improves read performance

---

## 🚨 Error Responses

- `400 Bad Request` - Invalid input data
- `404 Not Found` - Flight not found
- `409 Conflict` - Business rule violation (e.g., duplicate seat assignment)
- `500 Internal Server Error` - Unexpected server error

All errors return a structured JSON response with timestamp and error details.