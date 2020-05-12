package com.pinocchio.security.mapper;

import com.pinocchio.security.model.DeptVo;
import com.pinocchio.security.model.SysDept;
import com.pinocchio.security.model.SysMenu;
import com.pinocchio.security.util.MyMapper;

import java.util.List;

public interface SysDeptMapper extends MyMapper<SysDept> {

    /**
     * 根据父部门，查询子部门
     *
     * @param parentId 父部门ID
     */
    List<DeptVo> queryListParentId(Long parentId);


    DeptVo findById(Long id);
}
