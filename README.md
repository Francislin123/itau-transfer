# Transfer API
- Application responsible for controlling account transfer

# Technologies
- Java 17;
- Springframework Boot 3.2.4;
- HttpClient (Integration);
- Lombok;
- Google gson 2.10.1;
- Spring doc open api 2.5.0;
- Design Patterns (Facade Pattern);
- AWS (API Gateway, Elastic Load Balancing (ELB), EC2 Auto Scaling e CloudWatch);

### To run the application just follow the steps below.
- 1 - Enter the folder where the project jar is
- 2 - Execute the following command by cmd
- 3 - java -jar transfer-api-0.0.1-SNAPSHOT.jar --spring.config.location = file: C: /Config/application.yml

### Documentation for testing api (Swagger)
- http://localhost:8077/api/v1/swagger-ui/index.html#/

### Aws Solution Architecture
![Captura de Tela 2019-05-12 às 15 18 49](https://res.cloudinary.com/duep7y7ve/image/upload/v1712892067/l84vma0nozvuoidlshzg.png)

- API Gateway: Utilizado como ponto de entrada para a aplicação. Recebe as solicitações dos clientes e encaminha para os serviços correspondentes.
- Elastic Load Balancing (ELB): Distribui o tráfego de entrada entre as instâncias EC2. Garante alta disponibilidade e escalabilidade, distribuindo o tráfego somente para instâncias saudáveis.
- EC2 Auto Scaling: Dimensiona automaticamente a capacidade de computação EC2 com base na demanda. Garante que o número correto de instâncias EC2 esteja em execução para lidar com a carga de trabalho atual.
- CloudWatch: Utilizado para monitorar a saúde e o desempenho da infraestrutura AWS. Coleta métricas, logs e eventos que podem ser usados para tomar decisões informadas sobre a escalabilidade e o desempenho da aplicação.

