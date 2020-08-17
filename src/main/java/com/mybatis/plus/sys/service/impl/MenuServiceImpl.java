package com.mybatis.plus.sys.service.impl;

import com.mybatis.plus.sys.entity.Menu;
import com.mybatis.plus.sys.mapper.MenuMapper;
import com.mybatis.plus.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.plus.sys.service.IRoleMenuService;
import com.mybatis.plus.sys.service.IUserService;
import com.mybatis.plus.utils.MapUtils;
import com.mybatis.plus.utils.constraint.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    public List<Menu> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<Menu> menuList = queryListParentId(parentId);
        if(menuIdList == null){
            return menuList;
        }

        List<Menu> userMenuList = new ArrayList<>();
        for(Menu menu : menuList){
            if(menuIdList.contains(menu.getMenuId())){
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<Menu> queryListParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }

    @Override
    public List<Menu> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }

    @Override
    public List<Menu> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<Long> menuIdList = userService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    @Override
    public void delete(Long menuId){
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        roleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
    }

    /**
     * 获取所有菜单列表
     */
    private List<Menu> getAllMenuList(List<Long> menuIdList){
        //查询根菜单列表
        List<Menu> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<Menu> getMenuTreeList(List<Menu> menuList, List<Long> menuIdList){
        List<Menu> subMenuList = new ArrayList<Menu>();

        for(Menu entity : menuList){
            //目录
            if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

}
