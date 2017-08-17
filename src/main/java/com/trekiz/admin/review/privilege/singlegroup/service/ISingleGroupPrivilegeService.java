package com.trekiz.admin.review.privilege.singlegroup.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public interface ISingleGroupPrivilegeService {
	
	//优惠申请
	Map<String, String>  ApplyPrivilege(HttpServletRequest request,HttpServletResponse response);
	
}
