# MSChain-CA
CA application for MSChain

## Prerequisits
To run MSChain-CA you should have following installed

* Java 8
* Maven
* Docker
* Docker-compose

## How to run MSChain-CA

Use docker to run MSChain-CA


To build and create the docker image, go to the project directory and use following command.
```
$ mvn clean package docker:build
```

To run the container, go to the project directory and use following command.
```
$ docker-compose up -d
```

Good to go!
