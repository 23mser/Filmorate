# Filmorate

Imagine that you decide to relax and spend the evening watching a movie. Delicious food is already being prepared, your favorite blanket curled up comfortably on an armchair - and you still haven't chosen what to watch!

There are many films - and every year there are more and more. The more of them, the more different estimates. The more ratings, the more difficult it is to make a choice. However, this is not the time to give up! Filmorate is a service that works with movies and user ratings, and returns the top 5 movies recommended for viewing. Now neither you nor your friends will have to think for a long time what to see in the evening.

## Key Features

- Adding Directors to Movies
- List of the most popular films by genre and year
- "Search" functionality
- Shared Movies functionality
- "Recommendations" functionality
- Deleting Movies and Users
- Functionality "Reviews"
- Functionality "Event Feed"

## Database diagram

<img alt="Database diagram" src="diagram.png">

## Technologies Used

- Spring Boot
- Spring Data (JDBC)
- H2
- JUnit

## Getting Started

### Prerequisites

Ensure you have the following installed on your system:

- Java Development Kit (JDK) 11 or later

### Building the Project

1. Clone the repository:

   `git clone https://github.com/kirpinev/java-filmorate.git`

2. Change the current directory to the project root:

   `cd java-filmorate`

3. Build the project using Maven:

   `./mvnw clean install`

### Running the Application

Run the application using the following command:

`./mvnw spring-boot:run`

The application will start on port 8080. Access the public API at
`http://localhost:8080`.
