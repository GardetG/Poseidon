# Poseidon
![alt text](https://user.oc-static.com/upload/2019/10/21/15716625873741_image1.png)  
Poseidon is a web-based enterprise software that aims to generate more transactions for institutional investors buying and selling fixed income securities.
To do this, Poseidon aggregates information from many sources in the fixed income markets and essentially streamlines the communication and use of post-trade information between the front and back office.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.  
The documentation of the project is available here: [JavaDoc](https://gardetg.github.io/Poseidon/)

### Prerequisities

This project is built with:

1. Framework: Spring Boot v2.6.2
2. Maven: 3.8.3
3. Java 8
4. Thymeleaf
5. Bootstrap v.4.3.1
6. MySQL 8.0.26

### Setup the Application

#### Database:
Execute the SQL script locate in: `.\docs\doc` to create the database `Demo` and add a default user and admin to the application:
[data.sql](https://github.com/GardetG/Poseidon/blob/release/1.0/docs/doc/data.sql).  
/!\ This account are for demonstration purpose only, remove them in production.

As part of this demo, credentials are:
- Admin account: `admin` / `123456`
- User account: `user` / `123456`

#### OAuth2:
Authentication with OAuth2 required a client Id and client secret from GitHub.  
Register the application on GitHub to retrieve a new client Id and secret, or use the already configure ones with secret : `4f33cfed465a3a6b3bd37670c36428f2fdd7b672`.  
/!\ The already configure ones is for demonstration purpose only, don't use it in production.

- <ins>Client Id</ins> is defined in application.properties
- <ins>Client secret</ins> is passed by environment variable

### Running the Application

To run the application, we need some arguments:
- `db.username` : username of the MySQL database
- `db.password` : password of the MySQL database
- `oauth2.secret` : OAuth2 client secret from GitHub

From the root folder containing the pom.xml, run the application with maven command:  
`mvn spring-boot:run "-Dspring-boot.run.arguments='--db.username=<user>' '--db.password=<password>' '--oauth2.secret=<secret>'"`