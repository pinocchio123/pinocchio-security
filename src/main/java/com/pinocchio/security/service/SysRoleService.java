package com.pinocchio.security.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinocchio.security.mapper.SysRoleMapper;
import com.pinocchio.security.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    public PageInfo<SysRole> pageList(SysRole role) {
        Integer pno = role.getPage();
        Integer pageSize = role.getLimit();
        PageHelper.startPage(pno, pageSize);
        List<SysRole> list = sysRoleMapper.selectAll();
        PageInfo<SysRole> pageInfo = new PageInfo<SysRole>(list);
        return pageInfo;

    }
}
