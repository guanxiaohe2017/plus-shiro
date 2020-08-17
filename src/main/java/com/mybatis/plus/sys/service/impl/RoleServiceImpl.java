package com.mybatis.plus.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mybatis.plus.config.exception.RRException;
import com.mybatis.plus.sys.entity.Role;
import com.mybatis.plus.sys.mapper.RoleMapper;
import com.mybatis.plus.sys.service.IRoleMenuService;
import com.mybatis.plus.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.plus.sys.service.IUserRoleService;
import com.mybatis.plus.sys.service.IUserService;
import com.mybatis.plus.utils.constraint.Constant;
import com.mybatis.plus.utils.page.PageUtils;
import com.mybatis.plus.utils.page.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String roleName = (String)params.get("roleName");
        Long createUserId = (Long)params.get("createUserId");

        IPage<Role> page = this.page(
                new Query<Role>().getPage(params),
                new QueryWrapper<Role>()
                        .like(StringUtils.isNotBlank(roleName),"role_name", roleName)
                        .eq(createUserId != null,"create_user_id", createUserId)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(Role role) {
        role.setCreateTime(new Date());
        this.save(role);

        //检查权限是否越权
        checkPrems(role);

        //保存角色与菜单关系
        roleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role role) {
        this.updateById(role);

        //检查权限是否越权
        checkPrems(role);

        //更新角色与菜单关系
        roleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        roleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        userRoleService.deleteBatch(roleIds);
    }


    @Override
    public List<Long> queryRoleIdList(Long createUserId) {
        return baseMapper.queryRoleIdList(createUserId);
    }

    /**
     * 检查权限是否越权
     */
    private void checkPrems(Role role){
        //如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
        if(role.getCreateUserId() == Constant.SUPER_ADMIN){
            return ;
        }

        //查询用户所拥有的菜单列表
        List<Long> menuIdList = userService.queryAllMenuId(role.getCreateUserId());

        //判断是否越权
        if(!menuIdList.containsAll(role.getMenuIdList())){
            throw new RRException("新增角色的权限，已超出你的权限范围");
        }
    }

    @Override
    public Role getRoleByCode(String code) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code",code);
        Role roleEntity = getOne(queryWrapper);
        return roleEntity;
    }

}
