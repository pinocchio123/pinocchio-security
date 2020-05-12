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

import com.pinocchio.security.model.DeptVo;
import com.pinocchio.security.model.SysDept;


import com.pinocchio.security.model.SysUser;
import com.pinocchio.security.model.SysUserRole;
import com.pinocchio.security.service.SysDeptService;
import com.pinocchio.security.shiro.ShiroTag;
import com.pinocchio.security.shiro.ShiroUtils;
import com.pinocchio.security.util.R;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;


/**
 * 部门管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {
    private static final Logger logger = LoggerFactory
            .getLogger(SysUserController.class);
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysDeptService sysDeptService;

    @RequestMapping("/index")
    public ModelAndView index(Model model) {
        ModelAndView mv = new ModelAndView("modules/sys/dept/index");
        ShiroTag shiroTag = new ShiroTag();
        model.addAttribute("shiro", shiroTag);
        mv.addObject(model);
        return mv;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public List<DeptVo> list() {
        List<DeptVo> deptList = sysDeptService.getAllDeptList();
        return deptList;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(Model model, Long deptId) {
        logger.debug("deptId=" + deptId);
        ModelAndView mv = new ModelAndView("modules/sys/dept/edit");
        if (deptId != -1) {
            DeptVo dept = sysDeptMapper.findById(deptId);
            model.addAttribute("dept", dept);
            model.addAttribute("flag", "update");
        } else {
            model.addAttribute("dept", new DeptVo());
            model.addAttribute("flag", "add");
        }
        return mv;
    }

    @RequestMapping("/gridtreelist")
    public R gridtreelist() {
        List<SysDept> deptList = sysDeptMapper.selectAll();
        R r = R.ok().put("count", deptList.size());
        r.put("data", deptList);
        r.put("code", 0);
        r.put("msg", "请求成功");
        return r;
    }

    @RequestMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public R save(SysDept dept) {
        if (dept.getId() != null && !dept.getId().equals("")) {
            sysDeptMapper.updateByPrimaryKey(dept);
        } else {
            sysDeptMapper.insert(dept);
        }
        return R.ok();
    }

    @RequestMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public R delete(SysDept dept) {
        SysDept sysdept = new SysDept();
        sysdept.setPid(dept.getId());
        sysDeptMapper.delete(sysdept);
        sysDeptMapper.delete(dept);
        return R.ok();
    }
}
