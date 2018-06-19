# curveauto

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

These instructions are for Linux but are fairly adaptable to Windows

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

## Design Choice

The "library" was implemented as a REST-ish HTTP API because CurveAuto will have a number of front-end user interfaces
including phones, tables and desktop computers.

The REST API was implemented in Java, using Ratpack largely because it was familiar to me. 

The backend data is stored in a PostGres database. PostGres is a reasonably representative of any number of SQL databases. I chose a relational database over something that might scale (MongoDb, Cassandra) because I am assuming that the volume of data will be less than 100 GB for a long long time.  Additionally it is easy candidates with deep SQL knowledge in the Calgary area.
