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

package com.pinocchio.security.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;


/**
 * 部门管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */

public class SysDept extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//部门ID
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	//上级部门ID，一级部门为0
	private Long pid;
	//部门名称
	private String name;

	//排序
	private Integer orderNum;

	private Integer delFlag;

	private List<?> children;


	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	/**
	 * 设置：上级部门ID，一级部门为0
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * 获取：上级部门ID，一级部门为0
	 */
	public Long getPid() {
		return pid;
	}
	/**
	 * 设置：部门名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：部门名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public List<?> getChildren() {
		return children;
	}

	public void setChildren(List<?> children) {
		this.children = children;
	}
}
