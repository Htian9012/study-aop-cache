package com.aspect;

import com.base.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public abstract class BaseCacheAspect implements CacheAspectInterface{

    private RedissonClient template;

    public BaseCacheAspect(RedissonClient template){
        this.template = template;
    }

    public abstract Object doAround(ProceedingJoinPoint joinPoint) throws Throwable;

    @Override
    public Object get(String o) {
        try {
            RBucket<Result>  bucket = template.getBucket(o);
            Result value = bucket.get();
            return value;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(String o, Object o2,long expire) {
        try {
            template.getBucket(o).set(o2,expire, TimeUnit.SECONDS);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
