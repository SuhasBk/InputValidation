# Instructions


### Running the web application

To build and run the application inside a container using Docker on the host machine, first `cd` into the directory where the `Dockerfile` is located and execute:

`docker build -t sxk7070_project:prod .`

Then,

`docker run -p 8080:8080 sxk7070_project:prod`

Now, the application will be up and running in your local machine on the port - `8080`.

### Running the unit tests

All the tests from the problem statement (including additional student test cases) have been added in a single file: `src/test/java/com/cse5382/assignment/Controller/ControllerTest.java`.

For unit tests, this project uses Maven's Surefire report to get insights about the classes and methods under test.

To trigger the unit tests and view the report, execute the following:

`docker build -t sxk7070_project:tests . --target tests -o <OUTPUT_DIRECTORY>`

NOTE: While building the application, the Dockerfile stores the test results under: `target/site/surefire-report.html` during the `tests` stage. To copy the report from the container to host machine, above command is used. The copied directory is `target/site` because it will require additional static files (CSS and images) to correctly view the final HTML report - `surefire-report.html`.
Remember, `OUTPUT_DIRECTORY` is a location on your local host machine and not the container.

# Description

This web application is written in Spring Boot and follows the classic DAO pattern to interact with SQLite database for persistence of phone book entries.

REST API endpoints (`Controller.java`):

- `/phoneBook/add`: POST
- `/phoneBook/deleteByName`: PUT
- `/phoneBook/deleteByNumber`: PUT
- `/phoneBook/list`: GET

Each endpoint returns a generic `PhoneBookResponse.java` POJO which encapsulates HTTP status code and business logic feedback to the client.

### Database

This application uses SQLite relational DB to permanently store data on the disk. This is achieved using a lightweight open-source ORM library - [ORMLite Documentation](https://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html) to interact with the database using connection pool.

The name of the database for this application is - `phoneBook.db`. The URL for the same is pre-defined in `application.properties` file.

The database has two tables:

1) `phonebook` -> This table is mapped to the `PhoneBookEntry.java` POJO and the columns - `name` (primary key) and `phoneNumber` are clearly defined in that class for ORM. All columns are of String type.
2) `users` -> This table is mapped to the `PhoneBookUser.java` POJO and the columns - `username` (primary key), `password` (bcrypt encoded) and `role` are clearly defined in that class for ORM. All columns are of String type.

The application initializes the database at startup (`AssignmentApplication.java`) with required tables and data.

### Main Operations

- The `/phoneBook/list` retrieves all the entries currently in the phoneBook database.
- The `/phoneBook/add` tries to add a new phone book entry of the type - `PhoneBookEntry.java` (POJO with `name` and `phoneNumber` properties) using the ORMLite's PreparedStatement API.
- The `/phoneBook/deleteByName` tries to delete an existing entry from the database by searching for the given `name` as input.
- The `/phoneBook/deleteByNumber` tries to delete an existing entry by searching for the given `phoneNumber` as input.

### Authentication & Authorization

The REST APIs are defined such that each and every request should be authenticated with a valid user JWT token. This is implemented using Spring Security. The authentication mechanism used is a stateless - `JWT Authentication` which requires every incoming HTTP request to have a `Authorization` header with `Bearer <jwt_token>` value. A user can get the token using the following endpoint by providing correct credentials: `/phoneBook/api/auth/authenticate`

Every request is validated before it reaches the service layer / persistence layer.

For the sake of simplicity, this application pre-defines two types of users who are authorized to access the endpoints. The user details can be found in the `application.properties` file. One of the users has only `READ` authority and the other user has the `READ_WRITE` authority. These user instances are initialized in the database during startup as discussed before:

```
READ_USER = User_R
READ_USER_PWD = PWD_R

READ_WRITE_USER = User_RW
READ_WRITE_USER_PWD = PWD_RW
```

- `/phoneBook/list` only requires `READ` authority to access the resource. It can also be accessed using `READ_WRITE` authority.
- `phoneBook/add`, `/phoneBook/deleteByName` and `/phoneBook/deleteByNumber` specifically requires `READ_WRITE` authority. If a user without this authority tries to alter the contents of the database using those endpoints, an error is thrown with `401 Unauthorized` status.

These rules are declared in `SecurityConfig.java` which also uses `CustomAuthprovider.java` for the implementation of authentication.

NOTE: to access the endpoints, one needs to pass the Basic Authentication header with any one of the above user details.

### Input Validation

The name and phone number uses the following Regular Expressions to valdiate the HTTP requests at the controller layer itself:

```
Name: ^[A-Z][a-zA-Z]*[-']?[a-zA-Z]+,? ?[a-zA-Z]*[-']?[a-zA-Z]+ ?[a-zA-Z]*[-']?[a-zA-Z]*[.]?$
Phone Number: ^\d{5}$|^\d{5}[. ]\d{5}$|^\d{3}[-. ]\d{4}$|^\+?\b([1-9]|[1-9][0-9]|[1-9][0-9][0-8])\b[-.\( ]{0,2}\d{2,3}[ \-.\)]{0,2}\d{3}[-. ]\d{4}$|^[-.\( ]?\d{2,3}[ \-.\)]\d{3}[-. ]\d{4}$|^(00|011)[-.\( ]?\d{0,3}[ -.\)][-.\( ]?\d{2,3}[ -.\)]\d{3}[-. ]\d{4}$|^[+45. ]{0,4}\d{4}[. ]\d{4}$|^[+45. ]{0,4}\d{2}[. ]\d{2}[. ]\d{2}[. ]\d{2}$
```

These patterns are globally defined in `AppConstants.java` and appropirately used in the `PhoneBookEntry.java` POJO and Controller (`@Pattern` annotation) methods to prevent injection based attacks.

The above expressions work well with all the inputs given in the document as well as some of the additional test cases added by me.

### Logging

This application logs the phone book operations in the `audits.log` which records all the 4 operations being performed in the Service layer (`PhoneBookServiceImpl.java`). The configuration for the logger is defined in `logback.xml`. The operations are also logged to the console for convenience.

### Testing

Unit tests are written using jUnit framework using which the Controller methods are extensively tested for acceptable and non-acceptable string combinations. 

NOTE: While testing, appropriate configurations are isolated: `application-test.properties` using Spring Profiles functionality.

### Errors and Exceptions

The application handles all errors and exceptions globally in the file - `PhonebookControllerAdvice.java`. This file encapsulates both runtime exceptions such as `SQLException` and business logic specific exceptions such as invalid inputs and phone book inconsistent inputs.

# Assumptions

1) As stated earlier, we assume the two types users are already in the database and there is no functionality to "register" new users with different roles.
2) PhoneBook logs are cleared every time the application is restarted.
3) While testing, an in-memory H2 database is used for isolated and faster testing.
4) Database connections are handled using thread pooling by `JdbcPooledConnectionSource` for efficient performance.
5) JWT Authentication is used with BCrypt password encoding. Each token is valid for 30 minutes.

# Pros

1) Validates requests before malicious data reaches the service or database layer using `@Pattern` and `@Valid` annotations.
2) Uses a separate in-memory H2 database for testing to isolate prod and test databases.
3) Uses a light-weight ORM framework - ORMLite instead of standard Hibernate/Spring Data JPA for enhanced performance. Even though, there is no direct support for `INSERT` using a prepared statement, the `create` API internally uses PreparedStatements to deserialize the POJO and insert the values into the tables.
4) Authentication is stateless, meaning, each and every request must contain the JWT token.
5) Centralized errors and exception handling using - `PhoneBookControllerAdvice.java`.

# Cons
1) Even though the username and password are not hardcoded and taken from the properties file during runtime, a better and safer approach is to centralize the config in a separate secure server.
