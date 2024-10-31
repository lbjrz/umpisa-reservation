# Testing the Reservation System API

## Unit Tests
Unit tests are written to verify the functionality of individual components within the application. Spring Boot provides excellent support for writing unit tests.

### Sample Unit Tests
1. **ReservationServiceTest**
  - Test the `createReservation()` method.
  - Test the `getUpcomingReservations()` method.
  - Test the `updateReservation()` method.
  - Test the `cancelReservation()` method.

### Running Unit Tests
Run the following command in the terminal to execute the unit tests:

mvn clean install

## API Tests
Postman collection used for testing will be sent via email