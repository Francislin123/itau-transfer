# Transfer API

- Application responsible for controlling account transfer

# Technologies

- Java 17
- HttpClient (Integration)
- Lombok
- Google gson
- Springframework Boot 3.2.4
- Spring doc open api 2.5.0

### To run the application just follow the steps below.

- 1 - Enter the folder where the project jar is
- 2 - Execute the following command by cmd

- java -jar transfer-api-0.0.1-SNAPSHOT.jar --spring.config.location = file: C: /Config/application.yml
- OBS: This start command in the application, it finds the configurations of the configuration file of the database
- among other configurations (application.yml)
- -Dspring.profiles.active=local

### Documentation for testing api

- http://localhost:8077/api/v1/swagger-ui/index.html#/
