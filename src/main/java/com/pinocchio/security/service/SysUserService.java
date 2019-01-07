package com.pinocchio.security.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinocchio.security.mapper.SysUserMapper;
import com.pinocchio.security.model.SysUser;
import com.pinocchio.security.model.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    public PageInfo<UserVo> pageList(UserVo user) {
        Integer pno=user.getPage();
        Integer pageSize=user.getLimit();
        PageHelper.startPage(pno, pageSize);
        List<UserVo> list =sysUserMapper.queryUser();
        PageInfo<UserVo> pageInfo = new PageInfo<UserVo>(list);
        return pageInfo;

    }

    public SysUser getById(Long id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    public List<UserVo> queryUser(){
        return sysUserMapper.queryUser();
    }
}
