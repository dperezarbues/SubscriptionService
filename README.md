# Subscription Service

Simple implementation of a subscription REST interface based on Spring Boot and Spring Cloud.

### Objective:

Main objective is to provide with a self-containing solution being able to offer examples of different types of 
microservices interactions. 

### Implementation:
Up until now, three different approaches have been implemented for the interactions between services:

- REST API interface, implemented using Feign, which provides Hystrix circuit breaker capabilities and Reactive capabilities. 
Mainly focused on retrieving non essential information for the main application. Therefore assuming latency and up-time
not being relevant. Used to retrieve subscription campaign information.
- Pure reactive approach, implemented with Spring Reactor, for services whose latency might be high but assuming up-time
is also high. Spring Reactor was chosen for the ease of implementation and not needing an external queue technology like
RabbitMQ. Events are stored in memory and therefore lost if disconnection occurs. Used to send notification emails to the users.
(Please note that email sending capabilities have not yet been implemented)
- Event driven approach using Spring AMQP and RabbitMQ, used to communicate with critical services that need to receive
the information regardless of their current present status (up or down) and their latency. This approach allows to keep
a copy of the message sent to the service until the service is up again and ready to process the message. It also allows
to send one to many events, so any service can later on subscribe to the Subscriptions Events. (Please note that only 
publisher has been implemented)

In addition to the previous architecture, two more microservices are used:
- Eureka: Used for service Discovery and
- Cloud Config: Used as a single point of configuration for all the microservices developed. (For the sake of simplicity
and for ease of development, cloud config is currently storing configuration files locally (native), be aware that this implementation is not suitable for production, where it might be advisable to have several instances running sharing configuration files)

To be able to persist data and to communicate between services, a mysql database and a rabbitMQ messaging system are used.

Finally, documentation is provided via swagger and can be found in the following URLS:

- [SwaggerUI](https://localhost:8443/swagger-ui.html)
- [SwaggerAPIDoc](https://localhost:8443/v2/api-docs)

Please consider that this URLs may vary if you are running the dockerized version or running the microservice through the your IDE of choice

Although the main entry point Subscription Service is secure through SSL, it's not the desired solution. With the addition
of an OAuth2 microservice and Spring Security, it could be easy to allow normal users to only use the Thymeleaf form, whereas
admin users would be offered the full CRUD experience for both the subscriptions and the campaigns API. Due to time 
restrictions this could not be accomplished


## Building the project

For building and running the project you might need:
- A suitable IDE (Intellij was used during development)
- Plugin for Lombok (otherwise the IDE will complain of not found methods and won't be able to autocomplete)
- Git
- Maven
- Docker + Docker-compose, used to build the project within containers, specially needed when starting database and RabbitMQ
- Postman to be able to send REST request to the services
- MySQL client
- Java 8 & Java 11 (java 11 was meant to be used all thorough the project but there are still incompatibilities both with
Eureka (JAXBContext not found in java 11) and between Spring Cloud and JPA as described [here](https://github.com/spring-cloud/spring-cloud-config/issues/1142))

### Running the project

There are multiple ways of running the project, the easiest one would be to just run:

    docker-compose build;
    docker-compose run;

Nonetheless, you might find dependency issues while running it that way, although most of those dependencies are already
taken care of. Another option is to run the docker mysql and rabbitMQ images separately:

    //DATABASE:
    docker run -e MYSQL_ROOT_PASSWORD=changeme MYSQL_DATABASE=subscriptions MYSQL_USER=subscriptions MYSQL_PASSWORD=changeme -d mysql:latest

    //RABBITMQ
    docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:management

And later on start the rest of the services through your IDE or by building and executing the cointainers provided.

Take into account that the two REST interfaces are configured to fail-fast if no configuration is found, and, being 
Configuration Server found through Eureka, it's mandatory that both Eureka and Config Server are started prior to the
rest of interfaces.
        
### Using the Subscriptions API

The usage of the API is basically the same of every CRUD REST API, that being said, these are the main entrypoints:

- [https://localhost:8443/v1/subscriptions](https://localhost:8443/v1/subscriptions)
- [http://localhost:8442/campaign](http://localhost:8442/campaign)

Please be aware that the IP addresses my vary.

Both APIs implement HATEOAS and you can find bellow a couple test Json files to communicate with subscriptions and campaigns REST endpoints

    //Campaign JSON
	{"name": "FirstCampaign", "description": "description for first Campaign", "active": true}
	
	//Subscription JSON
	{"name":"test", "surname":"surname", "gender":"genero", "email":"email@gmail.com" , "dateOfBirth":"2018-01-23", "consent":true, "newsLetterId":1}
	
### Libraries used:

- Spring boot: Used to create a self-contained running application for every microservice
- Spring config: Used as a centralized configuration Service (for simplicity currently storing configuration natively)
- Eureka: Used as Service discovery
- Feign: To create clients for REST APIs
- Spring APMQ: to allow communication with messaging queues like RabbitMQ
- [Reactor](https://projectreactor.io/) for creating quick reactive connections between Services
- ThymeLeaf: to create small forms and web pages to display data to the user
- Spring Data Rest: to create simple CRUD REST interfaces out of the box (like the one in campaign Service)
- Spring JPA: To communicate to the persistence layer, in this case a mysql database
- Spring HATEOAS: To provide REST interfaces that comply the HATEOAS principles (Hypermedia As The Engine Of Application State)
- Lombok: To ease development, as it provides automatic logging capabilities and code completion (getters and setters)
- Swagger: For documentation purposes
- Mockito: For testing purposes

### Considerations
  - All certificates generated are self-signed, so you would need to disable SSL verification in Postman, you might also
  get a warning message in the browser.
  - Certificates were generated with JDK11, and JDK 9 and later ship with, and use by default, the unlimited policy 
  files, therefore there's no need for JCE Unlimited policy files.
  - All keystores have been generated with password "changeme" 
  - Tests are disabled in docker files to speed build process. Also, docker files are not meant for testing.

License
----
[GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)

