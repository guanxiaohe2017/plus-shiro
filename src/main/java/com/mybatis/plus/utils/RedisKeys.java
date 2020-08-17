

package com.mybatis.plus.utils;

/**
 *
 * 描述: Redis所有Keys
 *
 * @author 官萧何
 * @date 2020/8/17 11:35
 * @version V1.0
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }
}
