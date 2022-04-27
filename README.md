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
# Course
POST - Create course
----
	localhost:6868/v1/courses
----
Payload
----
	{
        "name": "Git lab hands on!",
        "description": "The best course of school app"
    }
----
Example Request
----
	curl --location --request POST 'localhost:6868/v1/courses' \
    --data-raw '{
        "name": "Git lab hands on!",
        "description": "The best course of school app"
    }'
----
Example Response
----
    {
        "id":11,
        "name":"Git lab hands on!",
        "description":"The best course of school app",
        "_links":{
            "self":{
                "href":"http://localhost:6868/v1/courses/11"
            },
            "courses":{
                "href":"http://localhost:6868/v1/courses?detailed=false"
            }
        }
    }
----
PUT - Update course
----
	localhost:6868/v1/courses/21
----
Payload
----
	{
        "name": "Git lab hands on!",
        "description": "Full course of git"
    }
----
Example Request
----
	curl --location --request PUT 'localhost:6868/v1/courses/11' \
    --data-raw '{
        "name": "Git lab hands on",
        "description": "Full course of git"
    }'
----
Example Response
----
    {
        "id": 11,
        "name": "Git lab hands on",
        "description": "Full course of git",
        "_links": {
            "self": {
                "href": "http://localhost:6868/v1/courses/11"
            },
            "courses": {
                "href": "http://localhost:6868/v1/courses?detailed=false"
            }
        }
    }
----
DELETE - Update course
----
	localhost:6868/v1/courses/13
----

Example Request
----
	curl --location --request DELETE 'localhost:6868/v1/courses/13'
----
GET - Find all courses
----
	localhost:6868/v1/courses/21
----
Parameters
----
	detailed = true or false
----
If the detailed parameter value is equal to true, the answer will include the students enrolled in the course.  

Example Request
----
	curl --location --request GET 'localhost:6868/v1/courses?detailed=true'
----
Example Response
----
    {
    "_embedded": {
        "courses": [
            {
                "id": 2,
                "name": "Git lab hands on!",
                "description": "The best course of school app",
                "students": [
                    {
                        "id": 1,
                        "email": "joegold@gmail.com",
                        "_links": {
                            "self": {
                                "href": "http://localhost:6868/v1/students/1"
                            }
                        },
                        "full_name": "Vini",
                        "phone_number": "+5534998874599",
                        "identification_document": "87895206310"
                    }
                ],
                "_links": {
                    "self": {
                        "href": "http://localhost:6868/v1/courses/2"
                    },
                    "courses": {
                        "href": "http://localhost:6868/v1/courses?detailed=false"
                    }
                }
            },
            {
                "id": 3,
                "name": "Git lab hands on!",
                "description": "The best course of school app",
                "students": [],
                "_links": {
                    "self": {
                        "href": "http://localhost:6868/v1/courses/3"
                    },
                    "courses": {
                        "href": "http://localhost:6868/v1/courses?detailed=false"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:6868/v1/courses?detailed=false"
        }
    }
}
----