

package com.mybatis.plus.sys.service;

import com.mybatis.plus.sys.entity.User;
import com.mybatis.plus.sys.entity.UserToken;

import java.util.Set;

/**
 *
 * 描述: shiro相关接口
 *
 * @author 官萧何
 * @date 2020/8/17 11:29
 * @version V1.0
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    UserToken queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    User queryUser(Long userId);
}
