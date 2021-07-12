# bank-graalvm-quarkus project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Install GraalVM
The easiest way to install and manage multiple java machines is using [sdkman](https://sdkman.io/)

```shell script
sdk use java 21.1.0.r11-grl
gu install native-image
 
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

## Creating a native executable

You can create a native executable using: 
```shell script
  ./gradlew build -Dquarkus.package.type=native
```

## Build image
```
  docker build -t bank-graalvm-quarkus-jvm .
```

## RUN image
```
  docker run -i --rm -p 8080:8080 bank-graalvm-quarkus 
```

## Run DB locally
```
docker build --no-cache -t bank-postgres-db ./docker/db/
docker run --name bank-postgres-db -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d bank-postgres-db
```

## RUN app + DB using docker-compose
```
  docker-compose up -d
```

### Test
Create client
```
 curl -X POST http://localhost:8080/client/new/100 
```
Get balance
```
 curl -X GET http://localhost:8080/client/0/balance 
```
Add Money
```
 curl -H "Content-type: application/json" -X POST http://localhost:8080/transaction  -d '{"amount":1000,"from_client_id":0,"to_client_id":1}'
```



