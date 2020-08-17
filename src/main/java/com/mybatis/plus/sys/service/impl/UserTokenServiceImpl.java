package com.mybatis.plus.sys.service.impl;

import com.mybatis.plus.sys.entity.UserToken;
import com.mybatis.plus.sys.mapper.UserTokenMapper;
import com.mybatis.plus.config.oauth2.TokenGenerator;
import com.mybatis.plus.sys.service.IUserTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.plus.utils.R;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 系统用户Token 服务实现类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@Service
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken> implements IUserTokenService {
    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Override
    public R createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //判断是否生成过token
        UserToken tokenEntity = this.getById(userId);
        if(tokenEntity == null){
            tokenEntity = new UserToken();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            this.save(tokenEntity);
        }else{
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //更新token
            this.updateById(tokenEntity);
        }
        R r = R.ok().put("token", token).put("expire", EXPIRE);
        return r;
    }

    @Override
    public void logout(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //修改token
        UserToken tokenEntity = new UserToken();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        this.updateById(tokenEntity);
    }
}
