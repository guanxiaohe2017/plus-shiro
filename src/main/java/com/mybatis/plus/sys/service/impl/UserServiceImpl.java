package com.mybatis.plus.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mybatis.plus.config.exception.RRException;
import com.mybatis.plus.sys.entity.Role;
import com.mybatis.plus.sys.entity.User;
import com.mybatis.plus.sys.form.UserDTO;
import com.mybatis.plus.sys.form.UserInfoDTO;
import com.mybatis.plus.sys.mapper.UserMapper;
import com.mybatis.plus.sys.service.IRoleService;
import com.mybatis.plus.sys.service.IUserRoleService;
import com.mybatis.plus.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.plus.utils.constraint.Constant;
import com.mybatis.plus.utils.oauth.ShiroUtils;
import com.mybatis.plus.utils.page.PageUtils;
import com.mybatis.plus.utils.page.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRoleService roleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        Long createUserId = (Long) params.get("createUserId");
        String type = (String) params.get("type");
        Long dataId = (Long) params.get("dataId");

        IPage<User> page = this.page(
                new Query<User>().getPage(params),
                new QueryWrapper<User>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .eq(createUserId != null, "create_user_id", createUserId)
                        .eq(StringUtils.isNotBlank(type), "type", type)
                        .eq(dataId != null, "data_id", dataId)
        );

        return new PageUtils(page);
    }

    @Override
    public UserDTO userInfo() {
        User loginUser = ShiroUtils.getUserEntity();
        if (loginUser == null) {
            throw new RRException("用户不存在");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(loginUser, userDTO);
        return userDTO;
    }

    @Override
    public UserInfoDTO updateUserInfo(UserInfoDTO userInfoDTO) {
        User existUser = getById(userInfoDTO.getUserId());
        if (StringUtils.isNotBlank(userInfoDTO.getName())) {
            existUser.setDataName(userInfoDTO.getName());
        }
        if (StringUtils.isNotBlank(userInfoDTO.getEmail())) {
            existUser.setEmail(userInfoDTO.getEmail());
        }
        if (StringUtils.isNotBlank(userInfoDTO.getMobile())) {
            existUser.setMobile(userInfoDTO.getMobile());
        }
        updateById(existUser);
        return userInfoDTO;
    }

    @Override
    public List<String> queryAllPerms(Long userId) {
        return baseMapper.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    public User queryByUserName(String username) {
        return baseMapper.queryByUserName(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        //获取用户的角色类型
        Role sysRoleEntity = roleService.getById(user.getRoleIdList().get(0));
        if (sysRoleEntity == null) {
            throw new RRException("用户角色不存在");
        }
        user.setType(sysRoleEntity.getRoleCode());

        this.save(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        userRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }

        //获取用户的角色类型
        Role sysRoleEntity = roleService.getById(user.getRoleIdList().get(0));
        if (sysRoleEntity == null) {
            throw new RRException("用户角色不存在");
        }
        user.setType(sysRoleEntity.getRoleCode());
        this.updateById(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        userRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    public void deleteBatch(Long[] userIds) {
        this.removeByIds(Arrays.asList(userIds));
        //删除用户与角色的关系
        for (Long userId : userIds) {
            userRoleService.saveOrUpdate(userId, null);
        }
    }

    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        User userEntity = new User();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<User>().eq("user_id", userId).eq("password", password));
    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(User user) {
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (user.getCreateUserId() == Constant.SUPER_ADMIN) {
            return;
        }

        //改为当前用户分配的角色是否不存
        Collection<Role> sysRoleEntities = roleService.listByIds(user.getRoleIdList());
        if (CollectionUtils.isNotEmpty(sysRoleEntities) && sysRoleEntities.size() == (user.getRoleIdList().size())) {
            return;
        } else {
            throw new RRException("用户所需要分配的角色不存在");
        }
    }

    @Override
    @Transactional
    public void batchSaveUser(List<User> sysUserEntityList) {
        if (CollectionUtils.isNotEmpty(sysUserEntityList)) {
            for (User sysUserEntity : sysUserEntityList) {
                saveUser(sysUserEntity);
            }
        }
    }

    @Override
    public List<User> listByDataIdAndType(Long schoolId,String type) {
        return this.list((new QueryWrapper<User>())
                .eq("data_id", schoolId)
                .eq("type", type)
                .eq("status", User.STATUS_YES)
        );
    }

    @Override
    public List<User> listByType(String type) {
        return this.list((new QueryWrapper<User>())
                .eq("type", type)
                .eq("status", User.STATUS_YES)
        );
    }

    @Override
    public List<User> listByTypeAndDataIds(String roleSchool, List<Long> schoolIds) {
        return this.list((new QueryWrapper<User>())
                .eq("type", roleSchool)
                .in("data_id", schoolIds)
                .eq("status", User.STATUS_YES)
        );
    }

    @Override
    public User findByUserName(String name) {
        return getOne((new QueryWrapper<User>()).eq("username", name));
    }
}
