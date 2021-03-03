## Why I chose JDBC (and stored procedures) over JPA(ORM)
    using stored procedures and JDBC help you to achieve high performance and avoid unnessary overhead with JPA

## Why I chose integration test over unit test
    using integration test help to have end to end test of the entire service from controller layer to database layer    

## Getting Started 
    1. setup MS SQL Server and run it. This demo service was tested against MS SQL Version 2019
    2. Create a database and update application.yml and application-test.yml files
    3. setup RabbitMQ and update application.yml and application-test.yml files 
    4. Make sure Java 11 SDK or above is installed
    5. update application.yml and application-test.yml files to replace mail properties (I tested using mailtrap(https://mailtrap.io))
    NB: sample properties were left for guidance purpose

    Sample docker command to setup MS SQL Server 2019
    docker run --name my-mssql19 -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=Password@1" -p 1433:1433 -d mcr.microsoft.com/mssql/server:2019-latest

    Sample docker command to setup RabbitMQ
    docker run -d --hostname my-host-rabbit --name my-rabbit -p 15672:15672 rabbitmq:3-management


### How to access Swagger/OpenAPI documentation
        http://localhost:8080/demo/swagger-ui.html
        http://localhost:8080/demo/v3/api-docs

### How to use the API with curl with sample commands
    Retrieve users with default pagination settings
    curl -X GET "http://localhost:8080/demo/api/users"
    
    Retrieve users with specific pagination values
    curl -X GET "http://localhost:8080/demo/api/users?pageNumber=1&pageSize=10"
    
    Create a new user
    curl -X POST "http://localhost:8080/demo/api/user" -H "accept: */*" -H "Content-Type: application/json" -d "{\"title\":\"Mr\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@yahoo.com\",\"mobile\":\"+2347087760744\",\"password\":\"string\",\"role\":\"USER\"}"
    
    Verify user email with verification token received in the mail box. e.g. xxxx-xxx-xxxx
    curl -X PUT "http://localhost:8080/demo/api/user/verify/xxxx-xxx-xxxx"
    
    Deactivate existing user
    curl -X DELETE "http://localhost:8080/demo/api/user/1" 

    Update existing user
    curl -X PUT "http://localhost:8080/demo/api/user/1" -H "accept: */*" -H "Content-Type: application/json" -d "{\"title\":\"Mr\",\"firstName\":\"Sammy\",\"lastName\":\"Doe\",\"mobile\":\"+2347089760744\",\"role\":\"USER\"}"



