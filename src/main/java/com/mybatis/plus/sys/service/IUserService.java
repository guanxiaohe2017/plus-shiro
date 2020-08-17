package com.mybatis.plus.sys.service;

import com.mybatis.plus.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.plus.sys.form.UserDTO;
import com.mybatis.plus.sys.form.UserInfoDTO;
import com.mybatis.plus.utils.page.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface IUserService extends IService<User> {
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询登录用户的详细信息
     * @return
     */
    UserDTO userInfo();

    /**
     * 更新用户信息
     * @param userInfoDTO
     * @return
     */
    UserInfoDTO updateUserInfo(UserInfoDTO userInfoDTO);

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

    /**
     * 保存用户
     */
    void saveUser(User user);

    /**
     * 修改用户
     */
    void update(User user);

    /**
     * 删除用户
     */
    void deleteBatch(Long[] userIds);

    /**
     * 修改密码
     * @param userId       用户ID
     * @param password     原密码
     * @param newPassword  新密码
     */
    boolean updatePassword(Long userId, String password, String newPassword);

    /**
     * 批量创建用户
     * @param sysUserEntityList 用户数据
     */
    void batchSaveUser(List<User> sysUserEntityList);

    List<User> listByDataIdAndType(Long dataId, String type);

    List<User> listByType(String type);

    List<User> listByTypeAndDataIds(String roleSchool, List<Long> schoolIds);

    User findByUserName(String name);
}
