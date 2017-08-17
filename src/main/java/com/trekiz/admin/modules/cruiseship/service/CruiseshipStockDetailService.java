/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockDetailInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockDetailQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CruiseshipStockDetailService{
	
	public void save (CruiseshipStockDetail cruiseshipStockDetail);
	
	public void save (CruiseshipStockDetailInput cruiseshipStockDetailInput);
	
	public void update (CruiseshipStockDetail cruiseshipStockDetail);
	
	public CruiseshipStockDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<CruiseshipStockDetail> find(Page<CruiseshipStockDetail> page, CruiseshipStockDetailQuery cruiseshipStockDetailQuery);
	
	public List<CruiseshipStockDetail> find( CruiseshipStockDetailQuery cruiseshipStockDetailQuery);
	
	public CruiseshipStockDetail getByUuid(String uuid);
	
	public CruiseshipStockDetail getByStockUuid(String stockUuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
    /**223-tgy:
     * 获得cruiseship_stock_detail表中的库存信息ByUUid和船期
     * @return
     */
	public List<Map<String, Object>> getShipStockDetailByUuidAndShipdate(String cruiseshipUUid,String shipdate);
    /**
     * 223:tgy
     * 根据csd表的主键id查询船期信息
     * @param keyId
     * @return
     */
	public List<Map<String, Object>> doGetDetailsById(String keyId);
   

	/** 已废弃 已废弃 已废弃
	 * 库存余位扣减service
	 * by chy 2016-2-3 10:34:23
	 * @param uuid
	 * @param type
	 * @param operate
	 * @param num
	 */
//	public void stockNumManage(String uuid, String type, String operate,
//			String num);
	public CruiseshipStockDetail getByUuidAndCreateBy(String uuid);
}
