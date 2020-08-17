package com.mybatis.plus.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.mybatis.plus.config.exception.RRException;
import com.mybatis.plus.sys.entity.Config;
import com.mybatis.plus.sys.mapper.ConfigMapper;
import com.mybatis.plus.sys.redis.SysConfigRedis;
import com.mybatis.plus.sys.service.IConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.plus.utils.page.PageUtils;
import com.mybatis.plus.utils.page.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 系统配置信息表 服务实现类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

    @Autowired
    private SysConfigRedis sysConfigRedis;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String paramKey = (String)params.get("paramKey");

        IPage<Config> page = this.page(
                new Query<Config>().getPage(params),
                new QueryWrapper<Config>()
                        .like(StringUtils.isNotBlank(paramKey),"param_key", paramKey)
                        .eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Override
    public void saveConfig(Config config) {
        this.save(config);
        sysConfigRedis.saveOrUpdate(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Config config) {
        this.updateById(config);
        sysConfigRedis.saveOrUpdate(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateValueByKey(String key, String value) {
        baseMapper.updateValueByKey(key, value);
        sysConfigRedis.delete(key);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] ids) {
        for(Long id : ids){
            Config config = this.getById(id);
            sysConfigRedis.delete(config.getParamKey());
        }

        this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public String getValue(String key) {
        Config config = sysConfigRedis.get(key);
        if(config == null){
            config = baseMapper.queryByKey(key);
            sysConfigRedis.saveOrUpdate(config);
        }

        return config == null ? null : config.getParamValue();
    }

    @Override
    public <T> T getConfigObject(String key, Class<T> clazz) {
        String value = getValue(key);
        if(StringUtils.isNotBlank(value)){
            return new Gson().fromJson(value, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RRException("获取参数失败");
        }
    }

}
