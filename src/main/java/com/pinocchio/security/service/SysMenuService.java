/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.pinocchio.security.service;



import com.pinocchio.security.mapper.SysMenuMapper;
import com.pinocchio.security.mapper.SysUserMapper;
import com.pinocchio.security.model.MenuVo;
import com.pinocchio.security.model.SysMenu;
import com.pinocchio.security.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service("sysMenuService")
public class SysMenuService {
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysMenuMapper sysMenuMapper;

	public List<MenuVo> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}

		//用户菜单列表
		List<Long> menuIdList = sysUserMapper.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	private List<MenuVo> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<MenuVo> menuList = queryListParentId(new Long(0), menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);

		return menuList;
	}

	private List<MenuVo> getMenuTreeList(List<MenuVo> menuList, List<Long> menuIdList){
		List<MenuVo> subMenuList = new ArrayList<MenuVo>();

		for(MenuVo entity : menuList){
			//目录
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
				List<MenuVo> childListMenu = new ArrayList<MenuVo>();
				childListMenu = queryListParentId(entity.getId(), menuIdList);
				entity.setChildren(getMenuTreeList(childListMenu, menuIdList));
			}
			subMenuList.add(entity);
		}

		return subMenuList;
	}

	public List<MenuVo> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<MenuVo> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}

		List<MenuVo> userMenuList = new ArrayList<>();
		for(MenuVo menu : menuList){
			if(menuIdList.contains(menu.getId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	public List<MenuVo> queryListParentId(Long parentId) {
		return sysMenuMapper.queryListParentId(parentId);
	}
}
