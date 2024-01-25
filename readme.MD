# Introduction
TReg is Transaction Registry management tool build using Java String boot.
### Tech stack
1. Java
2. Spring boot
3. PostgreSQL
4. In-memory cache

# High-level Design
## TReg tool has 3 main components:
1. Account: This is the account vai which transactions are performed.
2. Category: These are like tags which help in managing and grouping transactions.
3. Transaction: Transaction is the process which modifies the Account on which it is executed. Transaction can of type **DEBIT** or **CREDIT**.

![TReg High-level Design](https://github.com/pramod-janardhana/TReg/blob/master/artifacts/TReg%20Design.svg)

## Database Design
![TReg Database](https://github.com/pramod-janardhana/TReg/blob/master/artifacts/Database%20design.png)

# Steps to build and run
1. Install Java 
2. Clone the project
3. Open project in any IDE (Recommended IDE is IntelliJ)
4. Update application.properties file
5. Run the project from the IDE

# API collection
1. [Postman collection](https://github.com/pramod-janardhana/TReg/blob/master/artifacts/TReg%20APIs.postman_collection) 
2. [Swagger collection](http://localhost:8080/swagger-ui/index.html) (Swagger collection can be opened when the project is running.)