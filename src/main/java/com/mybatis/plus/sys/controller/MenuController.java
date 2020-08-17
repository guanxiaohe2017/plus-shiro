package com.mybatis.plus.sys.controller;


import com.mybatis.plus.config.exception.RRException;
import com.mybatis.plus.sys.entity.Menu;
import com.mybatis.plus.sys.service.IMenuService;
import com.mybatis.plus.sys.service.ShiroService;
import com.mybatis.plus.utils.R;
import com.mybatis.plus.utils.constraint.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
@RestController
@RequestMapping("/sys/menu")
@Api(tags = "菜单相关操作")
public class MenuController extends AbstractController{
    @Autowired
    private IMenuService menuService;
    @Autowired
    private ShiroService shiroService;

    /**
     * 导航菜单
     */
    @GetMapping("/nav")
    @ApiOperation("导航菜单")
    public R nav(){
        List<Menu> menuList = menuService.getUserMenuList(getUserId());
        Set<String> permissions = shiroService.getUserPermissions(getUserId());
        return R.ok().put("menuList", menuList).put("permissions", permissions);
    }

    /**
     * 所有菜单列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    @ApiOperation("所有菜单列表")
    public List<Menu> list(){
        List<Menu> menuList = menuService.list();
        Map<Long, Menu> parentMenuMap = menuList.stream().collect(Collectors.toMap(Menu::getParentId, a -> a, (k1, k2) -> k1));
        Set<Long> parentIds = parentMenuMap.keySet();
        Collection<Menu> sysMenuEntities = menuService.listByIds(parentIds);
        Iterator<Menu> iterator = sysMenuEntities.iterator();
        while(iterator.hasNext()){
            Menu sysMenuEntity = iterator.next();
            parentMenuMap.put(sysMenuEntity.getMenuId(),sysMenuEntity);
        }
        for(Menu menu : menuList){
            Menu parentMenuEntity =parentMenuMap.get(menu.getParentId());
            if(parentMenuEntity != null){
                menu.setParentName(parentMenuEntity.getName());
            }
        }
        return menuList;
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:menu:select")
    @ApiOperation("选择菜单")
    public R select(){
        //查询列表数据
        List<Menu> menuList = menuService.queryNotButtonList();

        //添加顶级菜单
        Menu root = new Menu();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        menuList.add(root);

        return R.ok().put("menuList", menuList);
    }

    /**
     * 菜单信息
     */
    @GetMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    @ApiOperation("菜单信息")
    public R info(@PathVariable("menuId") Long menuId){
        Menu menu = menuService.getById(menuId);
        return R.ok().put("menu", menu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    @ApiOperation("保存菜单")
    public R save(@RequestBody Menu menu){
        //数据校验
        verifyForm(menu);

        menuService.save(menu);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    @ApiOperation("修改菜单")
    public R update(@RequestBody Menu menu){
        //数据校验
        verifyForm(menu);

        menuService.updateById(menu);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{menuId}")
    @RequiresPermissions("sys:menu:delete")
    @ApiOperation("删除菜单")
    public R delete(@PathVariable("menuId") long menuId){
        if(menuId <= 31){
            return R.error("系统菜单，不能删除");
        }

        //判断是否有子菜单或按钮
        List<Menu> menuList = menuService.queryListParentId(menuId);
        if(menuList.size() > 0){
            return R.error("请先删除子菜单或按钮");
        }

        menuService.delete(menuId);

        return R.ok();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(Menu menu){
        if(StringUtils.isBlank(menu.getName())){
            throw new RRException("菜单名称不能为空");
        }

        if(menu.getParentId() == null){
            throw new RRException("上级菜单不能为空");
        }

        //菜单
        if(menu.getType() == Constant.MenuType.MENU.getValue()){
            if(StringUtils.isBlank(menu.getUrl())){
                throw new RRException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if(menu.getParentId() != 0){
            Menu parentMenu = menuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()){
            if(parentType != Constant.MenuType.CATALOG.getValue()){
                throw new RRException("上级菜单只能为目录类型");
            }
            return ;
        }

        //按钮
        if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
            if(parentType != Constant.MenuType.MENU.getValue()){
                throw new RRException("上级菜单只能为菜单类型");
            }
            return ;
        }
    }

}

