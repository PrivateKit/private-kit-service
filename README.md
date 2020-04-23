# Private Kit Service
![Build and Test](https://github.com/imanzano/private-kit-service/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)

An `mvn` wrapper is bundled. 

## To run server
```
./mvnw spring-boot:run
```

## To query local endpoint
```
curl localhost:8080
```

## Docker instructions

### Build Dockerfile
```
docker build -t privatekit/self-reporting-server .
```

### Run container
```
docker run -p 8080:8080 privatekit/self-reporting-server
```


