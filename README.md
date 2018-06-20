# curveauto

## Meta

I chose to implement a full stack application with a database, REST API and a javascript front-end.  This might have
been excessive for the development lead position but I don't want to take any chances.

I made a number of assumptions for this programming problem.  I'm documenting the key assumptions I made as different
assumptions would have produced a different solution.

 * This is an application that matters.  It requires a supportable storage backend.  I stayed away from making it too
   much of a toy.  I tried to make my solution realistic.  After filling in a few more bits it is conceivable to put it into production.
 * I assumed that "CurveAuto" will grow 
 * The application would not need to scale excessively.  I assumed 100 GB of data would be an extremely large customer
   for this application.
 * There is a desire to keep tenants separated from each other.
 * The "business logic" of this application will be the complicated parts.
 * There will be many users of these services.

I did cheat a bit in a few areas

 * The server will created the database (with test data) on startup.  In real life that would be too slow to manage
   pretty quickly.
 * There is no security.
 * In real life I would have separated out integration tests into their own projects.

## Library / API Design

The "library/api" was implemented as a HTTP API so that it can be used by different front-ends (web, android, apple)
and by different departments as needed.

The API was implemented in Java, using Ratpack and Postgres largely because they are familiar to me.

There are three layers to this code

 * a frontend "HTTP API" layer (in HttpServer.java)
 * a middle "business logic" layer (in API.java)
 * and a data access layer (HibernateDataAccess.java) using Hibernate and Postgres.

The main reasons I chose to split out the API layer are:

 * I wanted to keep the business logic separated so it can be easily changed, tested and repurposed.  It should be
   possible to make changes without becoming an expect in the HTTP or data access layers.
 * Make it would be possible to change the data access strategy and HTTP layers over time.

The backend data is stored in a PostGres database. PostGres is a reasonably representative of any number of SQL
databases. I chose a relational database over something that might scale (MongoDb, Cassandra) because I am assuming
that the volume of data will be less than 100 GB for a long long time.  Additionally it is easy candidates with deep
SQL knowledge in the Calgary area.

The code is implemented as

### src/main/java/curveauto/model/*

This contains the main models (Car, CarMaintenance, CarType, CarTypeMaintenance and MaintenanceType).  These are mapped
to the Postgres database using Hibernate and map Json objects using Jackson.

### src/main/java/curveauto/API.java

This class contains the main "Business Logic" API.  It is honestly pretty sparse but that is because there is not a lot
of business logic required for this programming problem.  In real life this can be the largest and most complicated
part of the system.

### src/main/java/curveauto/da/HibernateDataAccess.java

This contains the Hibernate data access layer that talks to the database.

## Web Interface Design

I originally planned on using Angular however I blew my "boilerplate code" budget on the HTTP API so I went with
something a bit simpler.  I found Vue and it seems reasonably easy to follow how it works.  I didn't need Webpack
or Babel to keep it going.

These three files are the web front-end and the HTTP api will serve them out.

    index.html
    index.js
    index.css

## Tests

There are tests in HttpTests and APITests.

## Quick Start

### Easiest

If you have Linux docker and docker-compose installed you should be able to

    git clone https://github.com/michaelfletchercgy/curveauto.git
    cd curveauto
    docker-compose up --build

Browse to http://localhost:5050/

### Easier

Otherwise if you do not have docker-compose you will require

 * a Java 8 JDK
 * a working postgresql environment

These instructions are for Linux but are adaptable to Windows

 * In PostGres you will need to have a user id, password and database you can connect to via TCP.
 * First set these environment variables
    export DATABASE_URL=jdbc:postgresql://<your database host>/<the name of the database>
    export DATABASE_USER_ID=<user id>
    export DATABASE_PASSWORD=<password>

 * Then make sure java is working

     java -version

 * Then run this
    ./gradlew run

Browse to http://localhost:5050/

