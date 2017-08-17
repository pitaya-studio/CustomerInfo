/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockOrder;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockOrderInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockOrderQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CruiseshipStockOrderService{
	
	public void save (CruiseshipStockOrder cruiseshipStockOrder);
	
	public void save (CruiseshipStockOrderInput cruiseshipStockOrderInput);
	
	public void update (CruiseshipStockOrder cruiseshipStockOrder);
	
	public CruiseshipStockOrder getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<CruiseshipStockOrder> find(Page<CruiseshipStockOrder> page, CruiseshipStockOrderQuery cruiseshipStockOrderQuery);
	
	public List<CruiseshipStockOrder> find( CruiseshipStockOrderQuery cruiseshipStockOrderQuery);
	
	public CruiseshipStockOrder getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);

	/**
	 * 包名生成库存游轮订单 by chy 2016年2月2日17:04:32
	 * @param params
	 * @throws BaseException4Quauq 
	 */
	public void makeOrder(Map<String, Object> params) throws BaseException4Quauq;
	/**
	 * @author chao.zhang
	 */
	public List<Map<String,Object>> getOrderByGroup(JSONObject json);
	/**
	 * @author chao.zhang
	 */
	public List<Map<String,Object>> getStockOrders(String name,String detailUuid,JSONObject  json);
}
