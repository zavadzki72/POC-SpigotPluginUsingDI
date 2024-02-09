package com.marccusz.mm_dependencyinjection.Database;

import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

@Singleton
public class MongoDbManager implements IMongoDbManager
{

    private Datastore _mongoConnection = null;

    @Override
    public Datastore GetConnection()
    {
        if(_mongoConnection != null)
        {
            return _mongoConnection;
        }

        return SetupConnection();
    }

    @Override
    public Datastore SetupConnection()
    {
        final String connString = "mongodb://localhost:27017";
        MongoClientURI uri = new MongoClientURI(connString);

        final Morphia morphia = new Morphia();
        morphia.mapPackage("dev.morphia.example");

        _mongoConnection = morphia.createDatastore(new MongoClient(uri), "MM_DependencyInjection");
        _mongoConnection.ensureIndexes();

        return _mongoConnection;
    }
}
