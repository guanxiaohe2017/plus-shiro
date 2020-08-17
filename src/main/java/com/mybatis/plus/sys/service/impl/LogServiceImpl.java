package com.mybatis.plus.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mybatis.plus.sys.entity.Log;
import com.mybatis.plus.sys.mapper.LogMapper;
import com.mybatis.plus.sys.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.plus.utils.page.PageUtils;
import com.mybatis.plus.utils.page.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String)params.get("username");
        String operation = (String)params.get("operation");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");


        IPage<Log> page = this.page(
                new Query<Log>().getPage(params),
                new QueryWrapper<Log>()
                        .like(StringUtils.isNotBlank(username),"username", username)
                        .like(StringUtils.isNotBlank(operation),"operation", operation)
                        .ge(StringUtils.isNotBlank(startDate),"create_date", startDate)
                        .le(StringUtils.isNotBlank(endDate),"create_date", startDate)

        );

        return new PageUtils(page);
    }

    @Override
    public void clearLog(DateTime dateTime) {
        List<Log> list = this.list((new QueryWrapper<Log>())
                .le("create_date", dateTime)
        );
        if (CollectionUtil.isNotEmpty(list)) {
            this.removeByIds(list.stream().map(e -> e.getId()).collect(Collectors.toList()));
        }
    }

}
