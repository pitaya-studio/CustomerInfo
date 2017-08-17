package com.trekiz.admin.review.airticketChange.dao;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.BaseDao;

public interface INewAirticketChangeDao extends BaseDao<Map<String,Object>>{
	
	public Map<String,Object> queryApprovalDetailTravel(HttpServletRequest request, HttpServletResponse response,String reviewId);
}
