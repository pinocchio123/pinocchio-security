package com.pinocchio.security.mapper;

import com.pinocchio.security.model.SysUser;
import com.pinocchio.security.model.UserVo;
import com.pinocchio.security.util.MyMapper;

import java.util.List;

public interface SysUserMapper extends MyMapper<SysUser> {

    List<UserVo> queryUser();

    UserVo queryUserOne(Long userId);

    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);
}
