package com.mybatis.plus.config.redis;

import java.util.Map;

/**
 *
 * 描述:  redis操作Service,对象和数组都以json形式进行存储
 *
 * @author 官萧何
 * @date 2020/8/17 11:20
 * @version V1.0
 */
public interface RedisService {
    /**
     * 存储数据
     */
    void set(String key, String value);

    /**
     * 存储数据
     */
    void setExpire(String key, Object value, long expire);

    /**
     * 获取数据
     */
    String get(String key);

    long getExpireSeconds(String key);

    /**
     * 设置超期时间
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 删除数据
     */
    void addStringAndExpire(String key, String value, long expire);

    /**
     * 自增操作
     *
     * @param delta 自增步长
     */
    Long increment(String key, long delta);

    <T> T getMapField(String key, String field, Class<T> clazz);

    void delMapField(String key, String... field);

    <T> void addMap(String key, String field, T obj);

    void setMap(String key, Map<Object, Object> value);
    Map<Object, Object> getMap(String key);


}
