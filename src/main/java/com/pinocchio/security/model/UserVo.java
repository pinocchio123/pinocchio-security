package com.pinocchio.security.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserVo extends SysUser implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    private String deptName;


	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public String toString() {
		return "SysUser{" +
				"userId=" + super.getStatus() +
				", username='" + super.getUsername() + '\'' +
				", password='" + super.getPassword() + '\'' +
				", salt='" + super.getSalt() + '\'' +
				", email='" + super.getEmail() + '\'' +
				", mobile='" + super.getMobile() + '\'' +
				", status=" + super.getStatus() +
				", roleIdList=" + super.getRoleIdList() +
				", createTime=" + super.getCreateTime() +
				", deptId=" + super.getDeptId() +
				", deptName='" + deptName + '\'' +
				'}';
	}
}
