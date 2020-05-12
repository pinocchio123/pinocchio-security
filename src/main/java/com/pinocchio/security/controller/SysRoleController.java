package com.pinocchio.security.controller;

import com.github.pagehelper.PageInfo;
import com.pinocchio.security.mapper.SysRoleMapper;
import com.pinocchio.security.mapper.SysRoleMenuMapper;
import com.pinocchio.security.mapper.SysUserRoleMapper;
import com.pinocchio.security.model.*;
import com.pinocchio.security.service.SysRoleService;
import com.pinocchio.security.shiro.ShiroTag;
import com.pinocchio.security.shiro.ShiroUtils;
import com.pinocchio.security.util.R;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.logging.Logger;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(SysUserController.class);
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @RequestMapping("/index")
    public ModelAndView index(Model model) {
        ModelAndView mv = new ModelAndView("modules/sys/role/index");
        ShiroTag shiroTag = new ShiroTag();
        model.addAttribute("shiro", shiroTag);
        mv.addObject(model);
        return mv;
    }

    @RequestMapping("/listByPage")
    public R list(SysRole role) {
//		PageInfo<ApkVersionUpgrade> pageInfo = apkVersionService.list(request,paramMap);
        PageInfo<SysRole> pageInfo = sysRoleService.pageList(role);
        R r = R.ok().put("count", pageInfo.getTotal());
        r.put("data", pageInfo.getList());
        r.put("code", 0);
        r.put("msg", "请求成功");
        return r;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(Model model, Long roleId) {
        logger.debug("roleId=" + roleId);
        ModelAndView mv = new ModelAndView("modules/sys/role/edit");
        if (roleId != 0) {
            SysRole role = sysRoleMapper.selectByPrimaryKey(roleId);
            model.addAttribute("role", role);
            model.addAttribute("flag", "update");
        } else {
            model.addAttribute("role", new SysRole());
            model.addAttribute("flag", "add");
        }
        return mv;
    }

    @RequestMapping("/perm")
    public ModelAndView perm(Model model, Long roleId) {
        logger.debug("roleId=" + roleId);
        ModelAndView mv = new ModelAndView("modules/sys/role/perm");
        model.addAttribute("roleId", roleId);
        return mv;
    }

    @RequestMapping("/save")
    @RequiresPermissions("sys:role:save")
    public R save(SysRole role) {
        if (role.getRoleId() != null && role.getRoleId().equals("")) {
            sysRoleMapper.updateByPrimaryKey(role);
        } else {
            role.setCreateTime(new Date());
            sysRoleMapper.insert(role);
        }
        return R.ok();
    }

    @RequestMapping("/saveRoleMenu")
    public R saveRoleMenu(Long roleId, String menuIds) {
        String[] ids = menuIds.split(",");
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setRoleId(roleId);
        sysRoleMenuMapper.delete(sysRoleMenu);
        for (String menuId : ids) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setMenuId(Long.valueOf(menuId));
            roleMenu.setRoleId(roleId);
            sysRoleMenuMapper.insert(roleMenu);
        }
        return R.ok();
    }

    @RequestMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public R delete(SysRole role) {
        sysRoleMapper.delete(role);
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(role.getRoleId());
        sysUserRoleMapper.delete(userRole);
        return R.ok();
    }


}
