package com.marccusz.mm_dependencyinjection.Database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;

@Singleton
public class MongoDbGenericRepository implements IMongoDbGenericRepository
{
    private final IMongoDbManager _mongoDbManager;

    @Inject
    public MongoDbGenericRepository(IMongoDbManager mongoDbManager)
    {
        _mongoDbManager = mongoDbManager;
    }

    @Override
    public <T> T GetById(Class<T> clazz, ObjectId id)
    {
        Datastore connection = _mongoDbManager.GetConnection();
        return connection
                .find(clazz)
                .field("Id")
                .equal(id)
                .first();
    }

    @Override
    public <T> void Create(T entity)
    {
        Datastore connection = _mongoDbManager.GetConnection();
        connection.save(entity);

    }

    @Override
    public <T> void Update(T entity)
    {
        Datastore connection = _mongoDbManager.GetConnection();
        connection.merge(entity);
    }

    @Override
    public <T> void Delete(T entity)
    {
        Datastore connection = _mongoDbManager.GetConnection();
        connection.delete(entity);
    }
}
