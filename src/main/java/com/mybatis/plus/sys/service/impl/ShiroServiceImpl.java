

package com.mybatis.plus.sys.service.impl;

import com.mybatis.plus.sys.entity.Menu;
import com.mybatis.plus.sys.entity.User;
import com.mybatis.plus.sys.entity.UserToken;
import com.mybatis.plus.sys.mapper.MenuMapper;
import com.mybatis.plus.sys.mapper.UserMapper;
import com.mybatis.plus.sys.mapper.UserTokenMapper;
import com.mybatis.plus.sys.service.ShiroService;
import com.mybatis.plus.utils.constraint.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 *
 * 描述: shiro服务实现类
 *
 * @author 官萧何
 * @date 2020/8/17 11:28
 * @version V1.0
 */
@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTokenMapper userTokenMapper;

    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            //查询菜单权限
            List<Menu> menuList = menuMapper.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for(Menu menu : menuList){
                permsList.add(menu.getPerms());//获取权限列表
            }
        }else{
            permsList = userMapper.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public UserToken queryByToken(String token) {
        return userTokenMapper.queryByToken(token);
    }

    @Override
    public User queryUser(Long userId) {
        return userMapper.selectById(userId);
    }
}
