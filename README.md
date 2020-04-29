# Private Kit Service
![Build and Tests](https://github.com/imanzano/private-kit-service/workflows/Build%20and%20Tests/badge.svg)
![Docker Image CI](https://github.com/imanzano/private-kit-service/workflows/Docker%20Image%20CI/badge.svg)

An `mvn` wrapper is bundled.

## Configuration


## Maven Profiles

`spring.profiles.active=${ACTIVE_PROFILE:dev}`

There are three profiles used dev, prod , container.

_dev_ :      Used only for development without authentication enabled.

_prod_:      Authentication enabled

_container_: Used for docker-compose deployment 

#Authentication

Private-Kit Server uses anonymous authentication through Captcha token.

By default there is a Captcha implementation based on [HCaptcha | https://www.hcaptcha.com/]

# Database
## PostgreSQL
#spring.datasource.url=jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1
spring.liquibase.change-log=classpath:/db/changelog/changelog-master.xml
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/privatekitservicedb}
spring.datasource.username=${DATABASE_USERNAME:privatekit}
spring.datasource.password=${DATABASE_PASSWORD:privatekit}
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
#spring.jpa.show-sql=true
#spring.jpa.properties.hibernate.format_sql=false
#logging.level.org.hibernate.SQL=DEBUG
#logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
spring.jpa.open-in-view=false

#drop n create table again, good for testing, comment this in production
#spring.jpa.hibernate.ddl-auto=create

management.endpoints.web.exposure.include=health

captcha.site-key=${CAPTCHA_SITE_KEY:YOUR-SITE-KEY}
captcha.secret=${CAPTCHA_SECRET:YOUR-SECRET}
captcha.verify-url=${CAPTCHA_VERIFY_URL:https://hcaptcha.com/siteverify}

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


