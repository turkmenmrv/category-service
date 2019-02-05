# Category Service
Maintains and exposes the category information. Categories created, searched, and patched with the help of this service

##Tech Stack Used
    SpringBoot
    PostgreSQL DB
    Docker
    
##Api Description
    ./api/category-service.yaml

## Available Endpoints
    /categories
        POST -> Creates a new category
    
    /categories/{id}
        PATCH -> patches(updates) visibility of category
    
    /categories?{id|slug}
        GET-> Returns categories
    
    /categories/{id}/tree
        GET-> Returns a category and its children as a tree

##Test Coverage
    ./docs/coverage/index.html
    
##Postman Scripts for testing Enpoints
    .docs/postman/CategoryService.postman_collection.json

## Starting/Testing the application
###To bootstrap postgres db
     $ git clone https://github.com/turkmenmrv/category-service.git
     $ cd  ./category-service/src/main/resources/db/
     $ ./local_bootstrap.sh  
To start the application (in project root directory):
    $ docker build -t category-service .
    $ docker run -t --name category-service --link postgres-docker -p 8081:8081 category-service
    
To run tests:
    
    $ mvn clean test

## Todo & Future Work
    No Security implemented!

