package com.mybatis.plus.sys.service;

import com.mybatis.plus.sys.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<Menu> queryListParentId(Long parentId, List<Long> menuIdList);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<Menu> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<Menu> queryNotButtonList();

    /**
     * 获取用户菜单列表
     */
    List<Menu> getUserMenuList(Long userId);

    /**
     * 删除
     */
    void delete(Long menuId);

}
