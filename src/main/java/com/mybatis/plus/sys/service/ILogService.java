package com.mybatis.plus.sys.service;

import cn.hutool.core.date.DateTime;
import com.mybatis.plus.sys.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.plus.utils.page.PageUtils;

import java.util.Map;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface ILogService extends IService<Log> {
    PageUtils queryPage(Map<String, Object> params);

    void clearLog(DateTime dateTime);
}
