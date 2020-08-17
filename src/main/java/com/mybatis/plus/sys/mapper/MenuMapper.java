package com.mybatis.plus.sys.mapper;

import com.mybatis.plus.sys.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author gch
 * @since 2020-08-15
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<Menu> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<Menu> queryNotButtonList();

}
