package com.pinocchio.security.mapper;

import com.pinocchio.security.model.MenuVo;
import com.pinocchio.security.model.SysMenu;
import com.pinocchio.security.util.MyMapper;

import java.util.List;


public interface SysMenuMapper extends MyMapper<SysMenu> {

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     */
    List<MenuVo> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenu> queryNotButtonList();

    MenuVo findById(Long id);
}
