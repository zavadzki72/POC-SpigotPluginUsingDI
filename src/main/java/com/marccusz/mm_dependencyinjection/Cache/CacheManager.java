package com.marccusz.mm_dependencyinjection.Cache;

import com.google.inject.Singleton;

import java.util.HashMap;

@Singleton
public class CacheManager implements ICacheManager
{
    HashMap<String, Object> _cache = new HashMap<>();

    @Override
    public <V> V GetByKey(String key)
    {
        Object cacheValue = _cache.get(key);

        if(cacheValue == null)
        {
            return null;
        }

        return (V)cacheValue;
    }

    @Override
    public <V> void Set(String key, V value)
    {
        _cache.put(key, value);
    }

    @Override
    public void ClearByKey(String key)
    {
        _cache.remove(key);
    }

    @Override
    public void ClearAll()
    {
        _cache.clear();
    }
}
