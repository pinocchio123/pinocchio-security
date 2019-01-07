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



import com.pinocchio.security.model.MenuVo;
import com.pinocchio.security.model.SysUser;
import com.pinocchio.security.service.SysMenuService;
import com.pinocchio.security.util.R;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pinocchio.security.shiro.ShiroUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 登录相关
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@Controller
public class SysLoginController {

	@Autowired
	private SysMenuService sysMenuService;
	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public R login(String username, String password) {
		try{
			Subject subject = ShiroUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		}catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			return R.error("账号或密码不正确");
		}catch (LockedAccountException e) {
			return R.error("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}
	    
		return R.ok();
	}

	/**
	 * 登录页面
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	/**
	 * 首页跳转
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main() {
		return "main";
	}

	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "login";
	}

	/**
	 * 首页
	 * @return
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView index(Model model,HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index");
		SysUser user = (SysUser) request.getSession().getAttribute("sessionUser");
		model.addAttribute("user", user);
		mv.addObject(model);
		return mv;
	}

	@RequestMapping("/menu/MenuList")
	@ResponseBody
	public R MenuList(HttpServletRequest request){
//		SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
		SysUser user = (SysUser) request.getSession().getAttribute("sessionUser");
		System.out.println("user:"+user.getUserId());
		List<MenuVo> menuList = sysMenuService.getUserMenuList(user.getUserId());
		return R.ok().put("menuList", menuList);
	}
}
