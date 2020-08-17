package com.mybatis.plus.sys.mapper;

import com.mybatis.plus.sys.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);
}
