[![DevSecOps Pipeline (AWS ECS Blue/Green)](https://github.com/Francislin123/itau-transfer/actions/workflows/deploy.yml/badge.svg?branch=master)](https://github.com/Francislin123/itau-transfer/actions/workflows/deploy.yml)

[![Security Nightly Scan](https://github.com/Francislin123/itau-transfer/actions/workflows/security-nightly.yml/badge.svg)](https://github.com/Francislin123/itau-transfer/actions/workflows/security-nightly.yml)

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

- **Java 17 & Spring Boot 3.2.4**: Utilizing the latest versions for performance and record support.
- **HttpClient**: Robust implementation for external integrations.
- **Design Patterns**: Application of **Facade** to simplify complex interfaces and **Builder** for creating immutable objects.
- **Caching Layer**: Caching strategy with **Ehcache** and integration via **Hibernate Second-level Cache** for optimizing database queries.

### Cache
- Caching was used for account and client search calls for better performance and to reduce the impact if the third-party API is out

### To run the application just follow the steps below.
- 1 - Enter the folder where the project jar is
- 2 - Execute the following command by cmd
- 3 - java -jar transfer-api-0.0.1-SNAPSHOT.jar --spring.config.location = file: C: /Config/application.yml

### Documentation for testing api (Swagger)
- http://localhost:8090/swagger-ui/index.html#/

### Building Containers with Docker Compose
- In the project root, where the docker-compose.yml file is located, run the command below to build and start all Wiremock containers defined in Docker Compose:
```bash
  docker-compose up --build -d
```

## GET Mock Client

http://localhost:9090/clientes/bcdd1048-a501-4608-bc82-66d7b4db3600

http://localhost:9090/clientes/2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f

+ Response 200 (application/json)

    + Body

            {
                "id": "bcdd1048-a501-4608-bc82-66d7b4db3600",
                "nome": "João Silva",
                "telefone": "912348765",
                "tipoPessoa": "Fisica"
            }

## GET Mock Contas

http://localhost:9090/contas/d0d32142-74b7-4aca-9c68-838aeacef96b

http://localhost:9090/contas/41313d7b-bd75-4c75-9dea-1f4be434007f

+ Response 200 (application/json)

    + Body

            {
                "id": "d0d32142-74b7-4aca-9c68-838aeacef96b,
                "saldo": 5000.00
                "ativo": true
                "limiteDiario": 500.00
            }

## PUT Mock Contas - Atualiza Saldo

http://localhost:9090/contas/saldos

+ Request (application/json)

    + Body

            {
              "valor": 1000.00,
              "conta": {
                  "idOrigem": "d0d32142-74b7-4aca-9c68-838aeacef96b",
                  "idDestino": "41313d7b-bd75-4c75-9dea-1f4be434007f"
              }
            }

+ Response 204 - No content (application/json)

## POST Mock Bacen

http://localhost:9090/notificacoes

+ Request (application/json)

    + Body

            {
              "valor": 1000.00,
              "conta": {
                  "idOrigem": "d0d32142-74b7-4aca-9c68-838aeacef96b",
                  "idDestino": "41313d7b-bd75-4c75-9dea-1f4be434007f"
              }
            }

+ Response 204 - No Content (application/json)

## POST API Transferência

http://localhost:8090/transfer

+ Request (application/json)

    + Body

            {
              "idCliente": "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
              "valor": 1000.00,
              "conta": {
                  "idOrigem": "d0d32142-74b7-4aca-9c68-838aeacef96b",
                  "idDestino": "41313d7b-bd75-4c75-9dea-1f4be434007f"
              }
            }

+ Response 200 (application/json)

    + Body

            {
                "idTransferencia": "410bb5b0-429f-46b1-8621-b7da101b1e28"
                "limiteDiario": 500.0,
                "msg": "Transfer completed successfully."
            }

### Actuator health
- http://localhost:8090/actuator/health

### Aws Solution Architecture
![Captura de Tela 2019-05-12 às 15 18 49](https://res.cloudinary.com/duep7y7ve/image/upload/v1777245365/11335669-9e27-41c3-ba7e-2ea2d397a55c_zeh2wn.png)

- API Gateway: Used as an entry point for the application. It receives client requests and forwards them to the corresponding services.
- Elastic Load Balancing (ELB): Distributes incoming traffic among EC2 instances. Ensures high availability and scalability by directing traffic only to healthy instances.
- EC2 Auto Scaling: Automatically scales the EC2 computing capacity based on demand. Ensures the correct number of EC2 instances are running to handle the current workload.
- CloudWatch: Used to monitor the health and performance of the AWS infrastructure. Collects metrics, logs, and events that can be used to make informed decisions about the scalability and performance of the application.

### DevSecOps CI/CD Pipeline com Blue/Green Deployment em AWS ECS
![Captura de Tela 2019-05-12 às 15 18 49](https://res.cloudinary.com/duep7y7ve/image/upload/v1777246352/3d31efb7-900a-4045-a123-6c81d72268ea_chbug2.png)

### About this Architecture
- This architecture provides a highly scalable and resilient solution, capable of handling variable workloads and ensuring high availability. Additionally, the use of CloudWatch enables proactive monitoring of application health and performance, enabling informed decisions about infrastructure adjustments..

### 📊 Observability
![Captura de Tela 2019-05-12 às 15 18 49](https://res.cloudinary.com/duep7y7ve/image/upload/v1777239127/urk5tgbotg9bopujlsgc.png)

- OpenTelemetry
- Standardized telemetry collection (metrics, logs, traces).
- OpenTelemetry Collector (Sidecar Pattern)
- Processes and exports telemetry data.
- Datadog

---

# 👨‍💻 Autor

**Francislin Dos Reis**

**Software Engineering**

---

# ⭐ If you liked the project

Leave a **⭐ on the repository** to support development.

---
