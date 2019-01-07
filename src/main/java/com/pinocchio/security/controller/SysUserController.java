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


import com.github.pagehelper.PageInfo;
import com.pinocchio.security.mapper.SysRoleMapper;
import com.pinocchio.security.mapper.SysUserMapper;
import com.pinocchio.security.mapper.SysUserRoleMapper;
import com.pinocchio.security.model.SysUser;
import com.pinocchio.security.model.SysUserRole;
import com.pinocchio.security.model.UserVo;
import com.pinocchio.security.service.SysUserService;
import com.pinocchio.security.shiro.ShiroTag;
import com.pinocchio.security.shiro.ShiroUtils;
import com.pinocchio.security.util.R;
import jdk.nashorn.internal.runtime.Undefined;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
	private static final Logger logger = LoggerFactory
			.getLogger(SysUserController.class);

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

	@RequestMapping("/index")
	public ModelAndView index(Model model){
		ModelAndView mv = new ModelAndView("modules/sys/user/index");
		ShiroTag shiroTag = new ShiroTag();
		model.addAttribute("shiro", shiroTag);
		mv.addObject(model);
		return mv;
	}

	@RequestMapping("/listByPage")
	public R list(UserVo user){
//		PageInfo<ApkVersionUpgrade> pageInfo = apkVersionService.list(request,paramMap);
		PageInfo<UserVo> pageInfo = sysUserService.pageList(user);
		R r = R.ok().put("count", pageInfo.getTotal());
		r.put("data", pageInfo.getList());
		r.put("code", 0);
		r.put("msg", "请求成功");
		return r;
	}

	@RequestMapping("/edit")
	public ModelAndView edit(Model model,Long userId){
		logger.debug("userId="+userId);
		ModelAndView mv = new ModelAndView("modules/sys/user/edit");
		if (userId != 0){
			UserVo user = sysUserMapper.queryUserOne(userId);
			model.addAttribute("user", user);
			model.addAttribute("flag", "update");
		}else{
			model.addAttribute("user", new UserVo());
			model.addAttribute("flag", "add");
		}
		model.addAttribute("roleList", sysRoleMapper.selectAll());
		model.addAttribute("roleIdList", sysUserRoleMapper.queryRoleIdList(userId));
		return mv;
	}

	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(SysUser user,Long[] roleId){
		if (user.getUserId() != null && !user.getUserId().equals("")) {
			sysUserMapper.updateByPrimaryKey(user);
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(user.getUserId());
			sysUserRoleMapper.delete(userRole);
			if (roleId != null && roleId.length != 0) {
				for (int i = 0; i < roleId.length; i++) {
					SysUserRole sysUserRole = new SysUserRole();
					sysUserRole.setUserId(user.getUserId());
					sysUserRole.setRoleId(roleId[i]);
					sysUserRoleMapper.insert(sysUserRole);
				}
			}
		} else {
			user.setCreateTime(new Date());
			//sha256加密
			String salt = RandomStringUtils.randomAlphanumeric(20);
			user.setSalt(salt);
			user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
			sysUserMapper.insert(user);
			if (roleId != null && roleId.length != 0) {
				for (int i = 0; i < roleId.length; i++) {
					SysUserRole sysUserRole = new SysUserRole();
					sysUserRole.setUserId(user.getUserId());
					sysUserRole.setRoleId(roleId[i]);
					sysUserRoleMapper.insert(sysUserRole);
				}
			}
		}
		return R.ok();
	}

	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(SysUser user){
		sysUserMapper.delete(user);
		SysUserRole userRole = new SysUserRole();
		userRole.setUserId(user.getUserId());
		sysUserRoleMapper.delete(userRole);
		return R.ok();
	}
}
