# Transfer API
- Application responsible for controlling account transfer

# Technologies
- Java 17;
- Springframework Boot 3.2.4;
- HttpClient (Integration);
- Lombok;
- Google gson 2.10.1;
- Spring doc open api 2.5.0;
- Integration Test: Rest Assured 5.4.0;
- Unit Test: Junit 4.13.2;
- Design Patterns (Facade/Builder);
- Ehcache core 2.6.11;
- Hibernate ehcache 6.0.0.Alpha7;
- AWS (API Gateway, Elastic Load Balancing (ELB), EC2 Auto Scaling e CloudWatch);

### To run the application just follow the steps below.
- 1 - Enter the folder where the project jar is
- 2 - Execute the following command by cmd
- 3 - java -jar transfer-api-0.0.1-SNAPSHOT.jar --spring.config.location = file: C: /Config/application.yml

### Documentation for testing api (Swagger)
- http://localhost:8077/api/v1/swagger-ui/index.html#/

### Aws Solution Architecture
![Captura de Tela 2019-05-12 às 15 18 49](https://res.cloudinary.com/duep7y7ve/image/upload/v1712892067/l84vma0nozvuoidlshzg.png)

- API Gateway: Used as an entry point for the application. It receives client requests and forwards them to the corresponding services.
- Elastic Load Balancing (ELB): Distributes incoming traffic among EC2 instances. Ensures high availability and scalability by directing traffic only to healthy instances.
- EC2 Auto Scaling: Automatically scales the EC2 computing capacity based on demand. Ensures the correct number of EC2 instances are running to handle the current workload.
- CloudWatch: Used to monitor the health and performance of the AWS infrastructure. Collects metrics, logs, and events that can be used to make informed decisions about the scalability and performance of the application.

### About this Architecture
- This architecture provides a highly scalable and resilient solution, capable of handling variable workloads and ensuring high availability. Additionally, the use of CloudWatch enables proactive monitoring of application health and performance, enabling informed decisions about infrastructure adjustments..