# Product Management System

Professional Java Product Management System built with Java 17, Maven, SQLite, and JUnit 5, following OOP principles and SOLID architecture.

## Features
- **CRUD Operations**: Create, Read, Update, and Delete products with ease.
- **Search & Filter**: Find products quickly by name or category.
- **Sorting**: Sort products dynamically by name, price, or stock level.
- **Inventory Management**: Track and adjust stock quantities safely with validation.
- **Robust Error Handling**: Comprehensive exception handling preventing crashes.
- **Data Persistence**: Lightweight embedded SQLite database.
- **Unit Testing**: Fully tested with JUnit 5.

## Prerequisites
- Java 17 or higher
- Apache Maven 3.6+

## Building and Running

1. **Clone or Download** the repository.
2. **Compile and Package** the application using Maven:
   ```bash
   mvn clean package
   ```
3. **Run Unit Tests**:
   ```bash
   mvn test
   ```
4. **Run the Application**:
   ```bash
   java -jar target/product-management-system-1.0.0.jar
   ```
