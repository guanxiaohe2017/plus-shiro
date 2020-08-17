package com.mybatis.plus.sys.mapper;

import com.mybatis.plus.sys.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统配置信息表 Mapper 接口
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface ConfigMapper extends BaseMapper<Config> {

    /**
     * 根据key，查询value
     */
    Config queryByKey(String paramKey);

    /**
     * 根据key，更新value
     */
    int updateValueByKey(@Param("paramKey") String paramKey, @Param("paramValue") String paramValue);

}
