package com.mybatis.plus.sys.service;

import com.mybatis.plus.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.plus.utils.page.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface IRoleService extends IService<Role> {
    PageUtils queryPage(Map<String, Object> params);

    void saveRole(Role role);

    void update(Role role);

    void deleteBatch(Long[] roleIds);

    /**
     * 根据角色编码获取角色信息
     * @param code
     * @return
     */
    Role getRoleByCode(String code);


    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);
}
