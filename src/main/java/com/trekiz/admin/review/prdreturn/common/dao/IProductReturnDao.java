package com.trekiz.admin.review.prdreturn.common.dao;

import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;

public interface IProductReturnDao extends BaseDao<Map<String, Object>>{

	Map<String, Object> queryAirTicketReturnDetailInfoById(String id);
}
