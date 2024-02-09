package com.marccusz.mm_dependencyinjection.Cache;

public interface ICacheManager
{
    <V> V GetByKey(String key);
    <V> void Set(String key, V value);
    void ClearByKey(String key);
    void ClearAll();
}
