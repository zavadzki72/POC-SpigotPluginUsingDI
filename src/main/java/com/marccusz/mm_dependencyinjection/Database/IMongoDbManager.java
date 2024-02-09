package com.marccusz.mm_dependencyinjection.Database;

import dev.morphia.Datastore;

public interface IMongoDbManager
{
    Datastore GetConnection();
    Datastore SetupConnection();
}
