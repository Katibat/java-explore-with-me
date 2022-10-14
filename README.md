## The project Explore-With-Me (programming language - Java):
The application project is a poster where you can offer any event from an exhibition to going 
to the cinema and recruit a company to participate in it.
Users can offer their events for publication in the service, as well as send applications for participation in 
other published events, confirm or reject other people's applications for participation in user's events.
The service administrator makes decisions about publishing events and pinning collections of events 
on the main page of the service.
Each user (including unauthorized ones) can receive information about published events. As a result, 
the service administrator collect statistics of event views by category.

## PR request link:
https://github.com/Katibat/java-explore-with-me/pull/1#issue-1402033542

## Project structure:
- Main service  - [Swagger file](https://github.com/Katibat/java-explore-with-me/blob/main/ewm-main-service-spec.json)
- Data Base main service - [ER-diagram](./ER_diagram_EWM-SERVER.png)
- Stats service - [Swagger file](https://github.com/Katibat/java-explore-with-me/blob/main/ewm-stats-service-spec.json)
- Data Base stats service - [ER-diagram](./ER_diagram_EWM-STATS.png)

## Technology used:
Amazon Corretto 11.0.14, Spring-Boot 2.7.2, Spring-Web 5.3.22, Lombok 1.18.24, 
Postgresql 14, Driver PSQL 42.5.0, Driver H2 2.1.214, Docker-compose 3.8,
GSON 2.9.0, HttpClient 5.0, Hibernate 6.2.3

## Project start should using Docker Dekstop:
with script
- for Window:
%~dp0mvnw.cmd clean package -DskipTests
docker-compose up --build
- for Linux / MacOS:
./mvnw clean package -DskipTests
docker-compose up --build