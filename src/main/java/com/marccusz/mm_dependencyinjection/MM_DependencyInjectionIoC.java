package com.marccusz.mm_dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.marccusz.mm_dependencyinjection.Cache.CacheManager;
import com.marccusz.mm_dependencyinjection.Cache.ICacheManager;
import com.marccusz.mm_dependencyinjection.Database.IMongoDbGenericRepository;
import com.marccusz.mm_dependencyinjection.Database.IMongoDbManager;
import com.marccusz.mm_dependencyinjection.Database.MongoDbGenericRepository;
import com.marccusz.mm_dependencyinjection.Database.MongoDbManager;

public class MM_DependencyInjectionIoC extends AbstractModule
{
    private final MM_DependencyInjection _plugin;

    public MM_DependencyInjectionIoC(MM_DependencyInjection plugin)
    {
        _plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(MM_DependencyInjection.class).toInstance(_plugin);

        this.bind(ICacheManager.class).to(CacheManager.class);
        this.bind(IMongoDbManager.class).to(MongoDbManager.class);
        this.bind(IMongoDbGenericRepository.class).to(MongoDbGenericRepository.class);
    }
}
