# Installation
1. ``sbt assembly ``
2. ``docker build -t contoller . && docker run -p 0.0.0.0:8080:8080 --name chess -d  -i  contoller ``

The controller is now running as a microservice in a docker container.
Use [this](https://github.com/Menkir/de.htwg.se.chess.client) thin Cient to play chess.

## Persistence
We use a Mysql database for storing game sessions. [Here](https://github.com/Menkir/de.htwg.se.chess/blob/software-architecture/resources/DB.sql) you will find the sql file to create an own db.

## Additional Notes
Be aware that this game is still WIP and it's just for demonstration purpose. 