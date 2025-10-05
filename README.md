# BarberShop - Appointment Management System

## Project Description
BarberShop is a desktop application built in Java with JavaFX that enables comprehensive management of a barbershop. The system allows barbers to manage appointments, services, and communications with customers, while customers can book appointments, choose services, and select payment methods.

## System Requirements
- Java 23 or later
- PostgreSQL
- Maven

## Installation and Setup

### Prerequisites
1. Ensure you have Java 23 or later installed
2. Install PostgreSQL and create a database named `BarberShop_DB`
3. Create a PostgreSQL user with the following credentials:
   - Username: `SWEuser`
   - Password: `swepass`
4. Make sure the user has the necessary privileges on the `BarberShop_DB` database

### Installation
1. Clone the repository
2. Navigate to the project directory
3. Run the Maven command to build the project:
   ```
   mvn clean install
   ```
4. Start the application:
   ```
   mvn javafx:run
   ```

## Key Features

### For Barbers
- **Appointment Management**: View all booked appointments
- **Service Management**: Add, edit, and remove offered services
- **Communications**: Send messages to customers
- **Profile**: Manage personal and professional information

### For Customers
- **Book Appointments**: Select date, time, services, and barber
- **Payments**: Pay using different methods (PayPal, Credit Card, in-store)
- **View Appointments**: Past and upcoming appointments
- **Notifications**: Receive communications and availability notifications

## How to Use the Application

### Logging In
1. Start the application
2. On the login screen, enter your email and password
3. Select the user type (Barber or Customer)
4. Click "Sign In"

### Booking an Appointment (Customer)
1. Log in as a customer
2. Go to the "Appointments" section
3. Click "New Appointment"
4. Select the desired date, time, barber, and services
5. Choose the payment method
6. Confirm the booking

### Managing Appointments (Barber)
1. Log in as a barber
2. Go to the "Appointments" section
3. View all booked appointments
4. Optionally filter by date or customer

## Project Structure

### Main Packages
- **Authentication**: User authentication and session management
- **Model**: Domain model classes (User, Appointment, Service, etc.)
- **PageControllers**: Controllers for the application's views
- **Payment**: Implementation of different payment methods (Strategy Pattern)
- **Persistence**: Database access and data persistence
- **Services**: Application business logic

### Design Patterns Used
- **Singleton**: Used for SessionManager and DBManager
- **DAO**: For access to persistent data
- **Factory**: For creating payment objects
- **Strategy**: To implement different payment methods

## Database
The application uses PostgreSQL as the production database. The connection is configured with the following parameters:
- URL: `jdbc:postgresql://localhost:5432/BarberShop_DB`
- Username: `SWEuser`
- Password: `swepass`

For tests, an in-memory H2 database is used.

## Technologies Used
- **JavaFX**: UI framework
- **MaterialFX**: UI components library
- **PostgreSQL**: Relational database
- **JBCrypt**: Secure password handling
- **JUnit, Mockito, TestFX**: Testing frameworks