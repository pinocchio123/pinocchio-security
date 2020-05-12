package com.pinocchio.security.service;

import com.pinocchio.security.mapper.SysDeptMapper;
import com.pinocchio.security.model.DeptVo;
import com.pinocchio.security.model.SysDept;
import com.pinocchio.security.model.SysDept;
import com.pinocchio.security.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("sysDeptService")
public class SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public List<DeptVo> getAllDeptList() {
        //查询根部门列表
        List<DeptVo> deptList = queryListParentId(new Long(-1));
        //递归获取子部门
        getDeptTreeList(deptList);

        return deptList;
    }

    public List<DeptVo> queryListParentId(Long parentId) {
        List<DeptVo> deptList = queryListParentIdBySql(parentId);
        return deptList;
    }

    public List<DeptVo> getDeptTreeList(List<DeptVo> deptList) {
        List<DeptVo> subDeptList = new ArrayList<DeptVo>();

        for (DeptVo entity : deptList) {
            List<DeptVo> childListDept = new ArrayList<DeptVo>();
            childListDept = queryListParentId(entity.getId());
            entity.setChildren(getDeptTreeList(childListDept));
            subDeptList.add(entity);
        }

        return subDeptList;
    }

    public List<DeptVo> queryListParentIdBySql(Long parentId) {
        return sysDeptMapper.queryListParentId(parentId);
    }
}
