# PLAYER MARKET

***Details***

Player market include two microservices:
 - proxy by spring boot with zuul proxy
 - transfer by spring boot + H2 db

Both developed in Java8 and Spring Boot.
Transfer service use Hibernate, Lombok and OpenApi Swagger.
The definition API you can see in transfer/definitions. 
I used Swagger OpenApi and this file is API description.
You can check on 
```https://editor.swagger.io/ ```

The tests:
- Controllers and Repositories are test by components (integration tests) tests with spring boot context
- Mappers,Services and others are test by unit test


***RUN***

You can run this with maven and java8 by next commands:
``` 
mvn package 
java -jar transfer/target/transfer-0.0.1-SNAPSHOT.jar 
java -jar proxy/target/proxy-0.0.1-SNAPSHOT.jar 
 ```
