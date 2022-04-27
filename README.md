# School Registration API

A RESTFul API that provides a series of functionality related to students and courses.

- Create students CRUD
- Create courses CRUD
- Create API for students to register to courses
- Create abilities for user to view all relationships between students and courses
+ Filter all students with a specific course
+ Filter all courses for a specific student
+ Filter all courses without any students
+ Filter all students without any courses

# Technology stack

- Java 17
- MySQL
- Gradle
- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Validation
- Spring Hateoas
- Lombok
- Flyway
- Docker
- Docker Compose

## Building and running  the project

#### Requirements 
- Docker(https://docs.docker.com/engine/install/)
- Docker Compose(https://docs.docker.com/compose/install/)
- Java 17, If you have sdkman installed, just run sdk env install (https://sdkman.io/install)

### Fill the .env file like the below example to run the project:

----
    MYSQLDB_USER=root
    MYSQLDB_ROOT_PASSWORD=123456
    MYSQLDB_DATABASE=school_registration_db
    MYSQLDB_LOCAL_PORT=3307
    MYSQLDB_DOCKER_PORT=3306
    SPRING_LOCAL_PORT=6868
    SPRING_DOCKER_PORT=8080
----
 It will configure the environment variables for the docker and the application.properties of the app.  

### Running project
 On the root folder execute the following command to build mysql and app images.

----
	docker-compose build
----

On the root folder execute the following command to run mysql and app containers.

----
	docker-compose up
----

# Provided endpoints
