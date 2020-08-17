package com.mybatis.plus.sys.mapper;

import com.mybatis.plus.sys.entity.UserToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 系统用户Token Mapper 接口
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface UserTokenMapper extends BaseMapper<UserToken> {
    UserToken queryByToken(String token);
}
