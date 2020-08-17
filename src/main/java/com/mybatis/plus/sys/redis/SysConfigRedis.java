

package com.mybatis.plus.sys.redis;


import com.mybatis.plus.sys.entity.Config;
import com.mybatis.plus.utils.RedisKeys;
import com.mybatis.plus.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 描述: 系统配置Redis
 *
 * @author 官萧何
 * @date 2020/8/17 11:28
 * @version V1.0
 */
@Component
public class SysConfigRedis {
    @Autowired
    private RedisUtils redisUtils;

    public void saveOrUpdate(Config config) {
        if(config == null){
            return ;
        }
        String key = RedisKeys.getSysConfigKey(config.getParamKey());
        redisUtils.set(key, config);
    }

    public void delete(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        redisUtils.delete(key);
    }

    public Config get(String configKey){
        String key = RedisKeys.getSysConfigKey(configKey);
        return redisUtils.get(key, Config.class);
    }
}
