# curveauto

## Meta

I chose to implement a full stack application with a database, REST API and a javascript front-end.  This might have
been excessive for the development lead position but I don't want to take any chances.

The major assumptions I have made in this problem are

 * That I should demonstrate something that could go to production (at least in theory).  I didn't want it to be too
   much of toy.  I used a real database and separated the HTTP, Business Logic and Data Access layers.  It would have
   been simpler to combine them but in real life projects get a lot more complicated and those layers help to keep
   the code manageable and adaptable..
 * The API would need to be available to multiple devices.  I implemented this as an HTTP API that could be used by
   desktops, phones and tablets with multiple independent client implementations.
 * The application would not need to scale too big.  I am using a traditional database instead of Cassandra or some
   other NoSQL database.  An auto dealer would not have a very large amount of data.
 * The library was more important than the UI for this problem.  I spent a lot more time on the library and getting a
   design that I liked.  I have another project that demonstrates a better UI (https://theplanet.ca/birthdays-uat with
   userid 'michaelfletcher' and password 'helloworld')

I did cheat a bit in a few areas

 * The server recreates all the data on startup.  That would never work in real life.
 * There is no security.
 * The UI looks pretty boring.

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


## Web Interface Design

I originally planned on using Angular however I blew my "boilerplate code" budget on the HTTP API so I went with
Vue.js.  I found vue reasonably easy to follow how it works, it worked with vanilla javascript and was on a CDN.  I
didn't need Webpack or Babel to make it function.

These three files are the web front-end and the HTTP api will serve them out.

    web/index.html
    web/index.js
    web/index.css

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
 * Postgresql

These instructions are for Linux but are adaptable to Windows

 * Java 8 will need to be installed.
 * Postgres needs to be up and running.
 * You will need a user-id, password and database which you can connect to via TCP/IP.
 * Set these environment variables.

    export DATABASE_URL=jdbc:postgresql://<your database host>/<the name of the database>
    export DATABASE_USER_ID=<user id>
    export DATABASE_PASSWORD=<password>

 * Make sure that java is in the path or a JAVA_HOME is set.

     java -version

 * Then you should be able to

    git clone https://github.com/michaelfletchercgy/curveauto.git
    cd curveauto
    ./gradlew run

Browse to http://localhost:5050/