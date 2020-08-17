package com.mybatis.plus.sys.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mybatis.plus.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mybatis.plus.sys.form.UserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据用户名，查询系统用户
     */
    User queryByUserName(String username);


}
