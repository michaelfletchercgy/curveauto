# curveauto

## Starting

### Easiest

If you have docker and docker-compose installed you should be able to

    docker-compose up --build

    Browse to http://localhost:5050/

### Easier

I

If you checkout


## Design Choice



The "library" was implemented as a HTTP API assuming that 

The REST API was implemented in Java, using Ratpack largely because it was familiar to me. 

The backend data is stored in a PostGres database. PostGres is a reasonably representative of any number of SQL databases. I chose a relational database over something that might scale (MongoDb, Cassandra) because I am assuming that the volume of data will be less than 100 GB for a long long time.  Additionally it is easy candidates with deep SQL knowledge in the Calgary area.
