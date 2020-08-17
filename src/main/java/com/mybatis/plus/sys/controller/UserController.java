package com.mybatis.plus.sys.controller;


import com.mybatis.plus.sys.entity.User;
import com.mybatis.plus.sys.form.PasswordForm;
import com.mybatis.plus.sys.form.UserInfoDTO;
import com.mybatis.plus.sys.service.IUserRoleService;
import com.mybatis.plus.sys.service.IUserService;
import com.mybatis.plus.utils.R;
import com.mybatis.plus.utils.constraint.Constant;
import com.mybatis.plus.utils.page.PageUtils;
import com.mybatis.plus.utils.validator.Assert;
import com.mybatis.plus.utils.validator.ValidatorUtils;
import com.mybatis.plus.utils.validator.group.AddGroup;
import com.mybatis.plus.utils.validator.group.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = "用户管理相关操作")
public class UserController extends AbstractController{

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;


    /**
     * 所有用户列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    @ApiOperation("所有用户列表")
    public R list(@RequestParam Map<String, Object> params){
        //只有超级管理员，才能查看所有管理员列表
        if(getUserId() != Constant.SUPER_ADMIN){
            params.put("createUserId", getUserId());
        }
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    @ApiOperation("获取登录的用户信息")
    public R info(){
        return R.ok().put("user", userService.userInfo());
    }


    @PostMapping("/update-info")
//	@RequiresPermissions("sys:user:update")
    @ApiOperation("更新用户信息")
    public R updateInfo(@RequestBody UserInfoDTO userInfoDTO){
        UserInfoDTO data = userService.updateUserInfo(userInfoDTO);
        return R.ok().put("user", data);

    }

    /**
     * 修改登录用户密码
     */
    @PostMapping("/password")
    @ApiOperation("修改登录用户密码")
    public R password(@RequestBody PasswordForm form){
        Assert.isBlank(form.getNewPassword(), "新密码不为能空");

        //sha256加密
        String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

        //更新密码
        boolean flag = userService.updatePassword(getUserId(), password, newPassword);
        if(!flag){
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    @ApiOperation("用户信息")
    public R info(@PathVariable("userId") Long userId){
        User user = userService.getById(userId);
        user.setPassword(null);
        //获取用户所属的角色列表
        List<Long> roleIdList = userRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    @ApiOperation("保存用户")
    public R save(@RequestBody User user){
        ValidatorUtils.validateEntity(user, AddGroup.class);

        user.setCreateUserId(getUserId());
        userService.saveUser(user);

        return R.ok();
    }

    /**
     * 修改用户
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    @ApiOperation("修改用户")
    public R update(@RequestBody User user){
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        user.setCreateUserId(getUserId());
        userService.update(user);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    @ApiOperation("删除用户")
    public R delete(@RequestBody Long[] userIds){
        if(ArrayUtils.contains(userIds, 1L)){
            return R.error("系统管理员不能删除");
        }

        if(ArrayUtils.contains(userIds, getUserId())){
            return R.error("当前用户不能删除");
        }

        userService.deleteBatch(userIds);

        return R.ok();
    }

}

