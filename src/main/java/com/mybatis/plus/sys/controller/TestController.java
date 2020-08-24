package com.mybatis.plus.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatis.plus.excel.ExcelUtil;
import com.mybatis.plus.sys.dto.SysLogDTO;
import com.mybatis.plus.sys.entity.Log;
import com.mybatis.plus.sys.service.ILogService;
import com.mybatis.plus.utils.DateUtils;
import com.mybatis.plus.utils.R;
import com.mybatis.plus.utils.page.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * 描述: 测试接口
 *
 * @author 官萧何
 * @date 2020/8/24 9:50
 * @version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ILogService logService;

    /**
     * 测试mybatisPlus更新null值问题
     */
    @ResponseBody
    @RequestMapping("/test")
    public R test(){
        Log one = logService.getOne((new QueryWrapper<Log>())
                .select("username","id")
                .eq("id", 3040)
        );
        one.setUsername("测试");
        logService.updateById(one);
        log.info(JSONObject.toJSONString(one));
        return R.ok();
    }

}

