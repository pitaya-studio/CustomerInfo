/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockOrder;
import com.trekiz.admin.modules.cruiseship.jsonbean.CruiseshipOrderQueryJsonBean;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface CruiseshipStockOrderDao  extends BaseDao<CruiseshipStockOrder> {
	
	public CruiseshipStockOrder getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据库存uuid获取创建用户信息集合
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @return   
	 * @return List<CruiseshipStockOrder>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public List<Map<String,Object>> queryCreateUsersByStockUuid(String cruiseshipStockUuid);
	
	/**
	 * 根据游轮库存jsonBean获取游轮库存订单集合
	 * @Description: 
	 * @param @param jsonBean
	 * @param @return   
	 * @return List<CruiseshipStockOrder>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public List<CruiseshipStockOrder> getStockOrdersByOrderQueryJsonBean(CruiseshipOrderQueryJsonBean jsonBean);
	
}
