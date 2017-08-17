package com.trekiz.admin.modules.activity.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.GroupcodeModifiedRecord;

public interface IGroupcodeModifiedRecordService { 
	
	public Page<Map<String, Object>> queryGroupCodeLibrary4Airticket(HttpServletRequest request, HttpServletResponse response);
	
	//对应需求   c463
	public Page<Map<String, Object>> queryGroupCodeLibrary4Visa(HttpServletRequest request, HttpServletResponse response);
	
	public Page<Map<String, Object>> queryGroupCodeLibrary4Tuanqi(HttpServletRequest request, HttpServletResponse response);
	
	public Map<String, List<GroupcodeModifiedRecord>> getGroupCodeByProductId(Long productId);
	
	//public void saveActivityGroupCompare(GroupcodeModifiedRecord groupcodeModifiedRecord) throws Exception;

	//public List<GroupcodeModifiedRecord> getGroupcodeModifiedRecordsByProductId(String[] groupCodes);
	
	//public List<GroupcodeModifiedRecord> getGroupcodeModifiedRecordsByActivityGroupId(String[] groupCodes);

	
}
