[![DevSecOps Pipeline (AWS ECS Blue/Green)](https://github.com/Francislin123/itau-transfer/actions/workflows/deploy.yml/badge.svg?branch=master)](https://github.com/Francislin123/itau-transfer/actions/workflows/deploy.yml)

# Transfer API 🚀
- Application responsible for controlling account transfer

### Tech Stack

| Categoria | Tecnologias |
| :--- | :--- |
| **Linguagem** | ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) |
| **Framework** | ![Spring Boot 3.2.4](https://img.shields.io/badge/Spring%20Boot-3.2.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) |
| **API Doc** | ![OpenAPI](https://img.shields.io/badge/Spring%20Doc-2.5.0-6DB33F?style=for-the-badge&logo=openapi-initiative&logoColor=white) |
| **Cloud (AWS)** | ![AWS Gateway](https://img.shields.io/badge/API%20Gateway-FF9900?style=for-the-badge&logo=amazon-api-gateway&logoColor=white) ![ELB](https://img.shields.io/badge/ELB-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white) ![CloudWatch](https://img.shields.io/badge/CloudWatch-FF9900?style=for-the-badge&logo=amazon-cloudwatch&logoColor=white) |
| **Testes** | ![JUnit 4](https://img.shields.io/badge/JUnit-4.13.2-25A162?style=for-the-badge&logo=junit5&logoColor=white) ![Rest-Assured](https://img.shields.io/badge/Rest--Assured-5.4.0-blue?style=for-the-badge) |
| **Cache** | ![Ehcache](https://img.shields.io/badge/Ehcache-2.6.11-orange?style=for-the-badge) |
| **Tools** | ![Lombok](https://img.shields.io/badge/Lombok-red?style=for-the-badge) ![GSON](https://img.shields.io/badge/GSON-2.10.1-blue?style=for-the-badge) |

---

### 🛠️ Especificações Técnicas

- **Java 17 & Spring Boot 3.2.4**: Utilização das versões mais recentes para performance e suporte a records.
- **HttpClient**: Implementação robusta para integrações externas.
- **Design Patterns**: Aplicação de **Facade** para simplificar interfaces complexas e **Builder** para criação de objetos imutáveis.
- **Caching Layer**: Estratégia de cache com **Ehcache** e integração via **Hibernate Second-level Cache** para otimização de consultas ao banco de dados.
- **Cloud Infrastructure**: Arquitetura preparada para escalabilidade com **EC2 Auto Scaling** e monitoramento detalhado via **CloudWatch Logs & Metrics**.

### Cache
- Caching was used for account and client search calls for better performance and to reduce the impact if the third-party API is out

### To run the application just follow the steps below.
- 1 - Enter the folder where the project jar is
- 2 - Execute the following command by cmd
- 3 - java -jar transfer-api-0.0.1-SNAPSHOT.jar --spring.config.location = file: C: /Config/application.yml

### Documentation for testing api (Swagger)
- http://localhost:8077/swagger-ui/index.html#/

### Registration APIs, accounts, and Central Bank of Brazil (BACEN). 
- To run this application, it is necessary to have 
- another API running on your machine that provides account information from the Central Bank of Brazil (BACEN). 
- This API can be found at the link below.
- https://github.com/mllcarvalho/DesafioItau

### Actuator health
- http://localhost:8080/actuator/health

### Aws Solution Architecture
![Captura de Tela 2019-05-12 às 15 18 49](https://res.cloudinary.com/duep7y7ve/image/upload/v1776192524/mmfkxrihlnn7bglvloqd.png)

- API Gateway: Used as an entry point for the application. It receives client requests and forwards them to the corresponding services.
- Elastic Load Balancing (ELB): Distributes incoming traffic among EC2 instances. Ensures high availability and scalability by directing traffic only to healthy instances.
- EC2 Auto Scaling: Automatically scales the EC2 computing capacity based on demand. Ensures the correct number of EC2 instances are running to handle the current workload.
- CloudWatch: Used to monitor the health and performance of the AWS infrastructure. Collects metrics, logs, and events that can be used to make informed decisions about the scalability and performance of the application.

### DevSecOps CI/CD Pipeline com Blue/Green Deployment em AWS ECS
![Captura de Tela 2019-05-12 às 15 18 49](https://res.cloudinary.com/duep7y7ve/image/upload/fl_preserve_transparency/v1776607924/qvcmeeqe0i0udq5znksu.jpg?_s=public-apps)

### About this Architecture
- This architecture provides a highly scalable and resilient solution, capable of handling variable workloads and ensuring high availability. Additionally, the use of CloudWatch enables proactive monitoring of application health and performance, enabling informed decisions about infrastructure adjustments..

# 👨‍💻 Autor

**Francislin**

**Engenharia de Software**

---

# ⭐ Se gostou do projeto

Deixe uma **⭐ no repositório** para apoiar o desenvolvimento.

---
