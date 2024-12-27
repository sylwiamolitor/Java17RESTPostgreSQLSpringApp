# Spring and PostgreSQL application
Spring application, which uses REST, DTOs, Spring Security (using JWT), React and PostgreSQL database.

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Screenshots](#screenshots)
* [Setup](#setup)
* [Usage](#usage)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)


## General Information
This project was created to test the capabilities of Spring, Spring Security and PostgreSQL - especially JPA. 
OpenAPI allows to describe, consume and visualize RESTful web services.
React is responsible for the appearance of the application

## Technologies Used
Java, Spring, PostgreSQL, Docker, OpenAPI, Spring Security, React

## Features
- Adding student,
- deleting student,
- getting all students,
- updating student,
- getting one student by email,
- validation of dates,
- checking if the email is correct,
- getting region and subregion from internet API based on student's country,
- registering user,
- authenticating user.


## Screenshots
![img_1.png](img_1.png)

## Setup
Define global environment variables called `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `DATABASE_NAME` and `SECRET_KEY`.
Database credentials are needed for the application to function. Secret key is needed for app's security.
Start the application.
The operating port is 8090.
To start the application use command `docker compose up`.
A JWT token is needed to authorize requests. Token created after in `register` endpoint will be used in requests or to authenticate user in `authenticate` endpoint.

## Usage
Tool Postman or OpenAPI are recommended. The available options are:
- adding student
- deleting student - DELETE http://localhost:8090/api/v1/student/{{studentId}}
- getting all students (pagination) http://localhost:8090/api/v1/student; additional parameters shown below (offset, pageSize, sortBy) ![img_5.png](img_5.png)
- updating student
- getting one student by email - GET http://localhost:8090/api/v1/student/{{email}}
- getting region and subregion from outside API based on student's country GET http://localhost:8090/api/v1/student/regionsByCountry/{studentId}
- OpenAPI option operates on different port:
- http://localhost/

## Project Status
Complete.

## Room for Improvement
React.


