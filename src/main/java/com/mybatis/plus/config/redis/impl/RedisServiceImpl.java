package com.mybatis.plus.config.redis.impl;

import com.mybatis.plus.config.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * 描述: redis操作Service的实现类
 *
 * @author 官萧何
 * @date 2020/8/17 11:20
 * @version V1.0
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setExpire(String key, Object value, long expire) {
    	redisTemplate.opsForValue().set(key, value, expire);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean expire(String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public <T> T getMapField(String key, String field, Class<T> clazz) {
        return (T) redisTemplate.boundHashOps(key).get(field);
    }

    @Override
    public void delMapField(String key, String... field) {
        BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(key);
        boundHashOperations.delete(field);
    }

    @Override
    public <T> void addMap(String key, String field, T obj) {
        redisTemplate.opsForHash().put(key, field, obj);
    }


    @Override
    public Map<Object, Object> getMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public void setMap(String key, Map<Object, Object> value) {
        redisTemplate.opsForHash().putAll(key, value);
    }

    @Override
    public void addStringAndExpire(String key, String value, long expire) {
        set(key, value);
        expire(key, expire);
    }

    @Override
    public long getExpireSeconds(String key) {

        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }
}
