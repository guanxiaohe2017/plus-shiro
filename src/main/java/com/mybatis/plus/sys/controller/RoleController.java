package com.mybatis.plus.sys.controller;


import com.mybatis.plus.sys.entity.Role;
import com.mybatis.plus.sys.entity.User;
import com.mybatis.plus.sys.service.IRoleMenuService;
import com.mybatis.plus.sys.service.IRoleService;
import com.mybatis.plus.utils.R;
import com.mybatis.plus.utils.constraint.BaseConstants;
import com.mybatis.plus.utils.page.PageUtils;
import com.mybatis.plus.utils.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@RestController
@RequestMapping("/sys/role")
@Api(tags = "角色管理相关操作")
public class RoleController extends AbstractController{
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleMenuService roleMenuService;

    /**
     * 角色列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    @ApiOperation("角色列表")
    public R list(@RequestParam Map<String, Object> params){
        //如果不是超级管理员，则只查询自己创建的角色列表
        User loginUser = getUser();
        if(!BaseConstants.UserType.ADMIN.equalsIgnoreCase(loginUser.getType())){
            params.put("createUserId", loginUser.getUserId());
        }
        PageUtils page = roleService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 角色列表
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    @ApiOperation("角色列表")
    public R select(){
        Map<String, Object> map = new HashMap<>();

        //如果不是超级管理员，则只查询自己所拥有的角色列表
        User loginUser = getUser();
        if(!BaseConstants.UserType.ADMIN.equalsIgnoreCase(loginUser.getType())){
            map.put("create_user_id", loginUser.getUserId());
        }
        List<Role> list = (List<Role>) roleService.listByMap(map);
        return R.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    @ApiOperation("角色信息")
    public R info(@PathVariable("roleId") Long roleId){
        Role role = roleService.getById(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = roleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);
        return R.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    @ApiOperation("保存角色")
    public R save(@RequestBody Role role){
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        roleService.saveRole(role);
        return R.ok();
    }

    /**
     * 修改角色
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    @ApiOperation("修改角色")
    public R update(@RequestBody Role role){
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        roleService.update(role);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    @ApiOperation("删除角色")
    public R delete(@RequestBody Long[] roleIds){
        roleService.deleteBatch(roleIds);
        return R.ok();
    }

}

