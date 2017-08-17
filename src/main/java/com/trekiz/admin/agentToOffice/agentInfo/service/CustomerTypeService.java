package com.trekiz.admin.agentToOffice.agentInfo.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;

public interface CustomerTypeService {

	/**
	 * 获取客户类型分页列表
	 * @param page 分页
	 * @author wangyang
	 * @date 2016-08-09
	 * */
	public Page<Map<String, Object>> getCustomerTypeList(Page<Map<String, Object>> page);
	
	/**
	 * 添加客户分类
	 * @param name 分类名称
	 * @param remark 分类描述
	 * @author wangyang
	 * @date 2016-08-10
	 * */
	public Long addCustomerType(String name, String remark);
	
	/**
	 * 删除客户分类
	 * @param id 渠道商id
	 * @author wangyang
	 * @date 2016-08-12
	 * */
	public void delCustomerType(Long id);
	
	/**
	 * 获取客户类型列表（下拉框）
	 * @author wangyang
	 * @date 2016-08-12
	 * */
	public List<Map<String, Object>> getCustomerTypeList4Select();
	
	/**
	 * 检查客户类型名是否重复，重复为true，无重复为false
	 * @param name 类型名称
	 * @author wangyang
	 * @date 2016-08-12
	 * */
	public boolean checkRepeat(String name);
	
	/**
	 * 检查客户类型是否被使用，被使用为true，未被使用未false
	 * @param id 类型的id
	 * @author wangyang
	 * @date 2016-08-15
	 * */
	public boolean isUsed(Long id);
}
