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

package com.pinocchio.security.controller;

import com.pinocchio.security.mapper.SysDeptMapper;
import com.pinocchio.security.mapper.SysMenuMapper;
import com.pinocchio.security.mapper.SysRoleMenuMapper;
import com.pinocchio.security.model.*;
import com.pinocchio.security.service.SysDeptService;
import com.pinocchio.security.service.SysMenuService;
import com.pinocchio.security.shiro.ShiroTag;
import com.pinocchio.security.util.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * 部门管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {
	private static final Logger logger = LoggerFactory
			.getLogger(SysUserController.class);
	@Autowired
	private SysMenuMapper sysMenuMapper;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysRoleMenuMapper roleMenuMapper;
	@RequestMapping("/index")
	public ModelAndView index(Model model){
		ModelAndView mv = new ModelAndView("modules/sys/menu/index");
		ShiroTag shiroTag = new ShiroTag();
		model.addAttribute("shiro", shiroTag);
		mv.addObject(model);
		return mv;
	}

	@RequestMapping("/menuTreeList")
	public R menuTreeList(){
		List<SysMenu> menuList = sysMenuMapper.selectAll();
		R r = R.ok().put("count", menuList.size());
		r.put("data", menuList);
		r.put("code", 0);
		r.put("msg", "请求成功");
		return r;
	}

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public List<SysMenu> list(){
		List<SysMenu> menuList = sysMenuMapper.selectAll();
		return menuList;
	}

	@RequestMapping("/listTree")
	public List<MenuVo> listTree(){
		List<MenuVo> menuList = sysMenuService.getUserMenuList(1L);
		return menuList;
	}

	@RequestMapping("/roleMenuList")
	public List<SysRoleMenu> roleMenuList(Long roleId){
		SysRoleMenu sysRoleMenu = new SysRoleMenu();
		List<SysRoleMenu> roleMenuList = roleMenuMapper.selectAll();
		return roleMenuList;
	}


	@RequestMapping("/edit")
	public ModelAndView edit(Model model,Long menuId){
		logger.debug("menuId="+menuId);
		ModelAndView mv = new ModelAndView("modules/sys/menu/edit");
		if (menuId != -1){
			MenuVo menu  = sysMenuMapper.findById(menuId);
			model.addAttribute("menu", menu);
			model.addAttribute("flag", "update");
		}else{
			model.addAttribute("menu", new MenuVo());
			model.addAttribute("flag", "add");
		}
		return mv;
	}


	@RequestMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public R save(SysMenu menu){
		if (menu.getId() != null && !menu.getId().equals("")) {
			sysMenuMapper.updateByPrimaryKey(menu);
		} else {
			sysMenuMapper.insert(menu);
		}
		return R.ok();
	}

	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public R delete(SysMenu menu){
		SysMenu sysMenu = new SysMenu();
		sysMenu.setPid(menu.getId());
		sysMenuMapper.delete(sysMenu);
		sysMenuMapper.delete(menu);
		return R.ok();
	}
}
