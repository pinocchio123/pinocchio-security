package com.pinocchio.security.mapper;

import com.pinocchio.security.model.SysUserRole;
import com.pinocchio.security.util.MyMapper;

import java.util.List;


public interface SysUserRoleMapper extends MyMapper<SysUserRole>{

    List<Long> queryRoleIdList(Long userId);
}
