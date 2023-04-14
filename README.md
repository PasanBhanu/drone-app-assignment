# MusalaSoft Drone Assignment

## System Requirements
- Java 17 or later
- Apache Maven 3.x or later
- Postman (optional)

## Build Instructions
Clone this repository to your local machine.

Open a terminal or command prompt and navigate to the project directory.

Run the following command to build the application:

```
mvn package
```
This command will compile the Java source code, run the tests, and package the application into a JAR file.

## Run Instructions
Make sure that you have met the system requirements and built the application according to the instructions above.

Navigate to the project directory in the terminal or command prompt.

Run the following command to start the application:
```
java -jar target/droneapp.jar
```
This command will start the application and listen for incoming requests on port 8080. You can access the API endpoints using the base URL http://localhost:8080.

Use Postman or any other HTTP client to test the API endpoints. See the API documentation below for more details.

## Postman Collection
You can import the following Postman collection to quickly test the API endpoints:

[Drone APIs.postman_collection](/postman/Drone APIs.postman_collection)