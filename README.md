# Dropbox File Manager

This project consists of two main components:

1. **Frontend**: A React application for the user interface.
2. **Backend**: A Java Spring Boot application for the server-side logic.

---

## Running the Frontend React Application

### Prerequisites

- Node.js (v18 or later)
- npm (Node Package Manager)

### Steps to Run

In the project directory:

   ```bash
   cd file-dropbox-ui
   npm install
   npm start
   ```

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser

## Running the Backend Java application

### Prerequisites

- maven
- Java 17 (openjdk-17)

### Steps to Run

In the project directory, you can run:

```bash
cd backend
mvn clean install
mvn clean package
java -jar target/myproject-0.0.1-SNAPSHOT.jar
```

Runs the app in the development mode.\
Open [http://localhost:8080](http://localhost:8080) to view it in your browser

## Running the application using Docker

From project directory, run:

For React App

```bash
cd file-dropbox-ui
docker build -t file-dropbox-ui .
docker run -p 3000:80 file-dropbox-ui
```

For SpringBoot App

```bash
cd backend
mvn clean package
docker build -t backend .
docker run -p 8080:8080 backend
```
