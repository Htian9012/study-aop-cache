package com.aspect;

public interface CacheAspectInterface {

    void set(String k, Object v, long expire);

    Object get(String k);
}
