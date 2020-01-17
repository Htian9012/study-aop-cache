package com.config;

import com.aspect.CacheAspect;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.FstCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redis配置类
 * @author lqh
 * @date 2019/4/16
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.database}")
    private Integer database;
    @Value("${spring.redis.host}")
    private String address;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.password}")
    private String password;


    @Bean("redissonClient")
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.setCodec(new FstCodec());
        SingleServerConfig server =  config.useSingleServer();
        server.setDatabase(database);
        server.setAddress("redis://"+address+":"+port);
        server.setPassword(password);
        return Redisson.create(config);
    }

    @Bean
    public CacheAspect cacheAspect(@Qualifier("redissonClient") RedissonClient template){
        return new CacheAspect(template);
    }
}
