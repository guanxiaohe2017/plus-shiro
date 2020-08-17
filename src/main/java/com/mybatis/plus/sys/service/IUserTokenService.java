package com.mybatis.plus.sys.service;

import com.mybatis.plus.sys.entity.UserToken;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.plus.utils.R;

/**
 * <p>
 * 系统用户Token 服务类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface IUserTokenService extends IService<UserToken> {
    /**
     * 生成token
     * @param userId  用户ID
     */
    R createToken(long userId);

    /**
     * 退出，修改token值
     * @param userId  用户ID
     */
    void logout(long userId);
}
