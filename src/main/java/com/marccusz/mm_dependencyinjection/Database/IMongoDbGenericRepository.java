package com.marccusz.mm_dependencyinjection.Database;

import org.bson.types.ObjectId;

public interface IMongoDbGenericRepository
{
    <T> T GetById (Class<T> clazz, ObjectId id);
    <T> void Create (T entity);
    <T> void Update(T entity);
    <T> void Delete(T entity);
}
