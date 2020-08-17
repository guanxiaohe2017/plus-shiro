package com.mybatis.plus.sys.service;

import com.mybatis.plus.sys.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.plus.utils.page.PageUtils;

import java.util.Map;

/**
 * <p>
 * 系统配置信息表 服务类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface IConfigService extends IService<Config> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存配置信息
     */
    public void saveConfig(Config config);

    /**
     * 更新配置信息
     */
    public void update(Config config);

    /**
     * 根据key，更新value
     */
    public void updateValueByKey(String key, String value);

    /**
     * 删除配置信息
     */
    public void deleteBatch(Long[] ids);

    /**
     * 根据key，获取配置的value值
     *
     * @param key           key
     */
    public String getValue(String key);

    /**
     * 根据key，获取value的Object对象
     * @param key    key
     * @param clazz  Object对象
     */
    public <T> T getConfigObject(String key, Class<T> clazz);

}
