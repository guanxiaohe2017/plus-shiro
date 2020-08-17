package com.mybatis.plus.sys.controller;


import com.mybatis.plus.excel.ExcelUtil;
import com.mybatis.plus.sys.dto.SysLogDTO;
import com.mybatis.plus.sys.entity.Log;
import com.mybatis.plus.sys.service.ILogService;
import com.mybatis.plus.utils.DateUtils;
import com.mybatis.plus.utils.R;
import com.mybatis.plus.utils.page.PageUtils;
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
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@RestController
@RequestMapping("/sys/log")
public class LogController {

    @Autowired
    private ILogService logService;

    /**
     * 列表
     */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:log:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = logService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @ResponseBody
    @GetMapping("/export")
//	@RequiresPermissions("sys:log:export")
    public void list(@RequestParam Map<String, Object> params, HttpServletResponse response){
        params.put("limit", "10000");
        PageUtils pageUtils = logService.queryPage(params);
        List<Log> list = (List<Log>) pageUtils.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            List<SysLogDTO> sysLogDTOList = new ArrayList<>();
            for (Log sysLogEntity : list) {
                SysLogDTO sysLogDTO = new SysLogDTO();
                BeanUtils.copyProperties(sysLogEntity, sysLogDTO);
                sysLogDTOList.add(sysLogDTO);
            }
            String key = DateUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss");
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            ExcelUtil.writeExcel(response, sysLogDTOList, key, "sheet1", new SysLogDTO());
        }
    }

}

