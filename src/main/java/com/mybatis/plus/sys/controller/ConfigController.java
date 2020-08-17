package com.mybatis.plus.sys.controller;


import com.mybatis.plus.sys.entity.Config;
import com.mybatis.plus.sys.service.IConfigService;
import com.mybatis.plus.utils.page.PageUtils;
import com.mybatis.plus.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 系统配置信息表 前端控制器
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@RestController
@RequestMapping("/sys/config")
@Api(tags = "配置管理")
public class ConfigController {

    @Autowired
    private IConfigService configService;

    /**
     * 所有配置列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:config:list")
    @ApiOperation("所有配置列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = configService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 配置信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:config:info")
    @ApiOperation("配置信息")
    public R info(@PathVariable("id") Long id){
        Config config = configService.getById(id);

        return R.ok().put("config", config);
    }

    /**
     * 保存配置
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:config:save")
    @ApiOperation("保存配置")
    public R save(@RequestBody Config config){
        configService.saveConfig(config);
        return R.ok();
    }

    /**
     * 修改配置
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:config:update")
    @ApiOperation("修改配置")
    public R update(@RequestBody Config config){
        configService.update(config);
        return R.ok();
    }

    /**
     * 删除配置
     */
    @PostMapping("/delete")
    @RequiresPermissions("sys:config:delete")
    @ApiOperation("删除配置")
    public R delete(@RequestBody Long[] ids){
        configService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}

