/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.dao;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface CruiseshipStockDetailDao  extends BaseDao<CruiseshipStockDetail> {
	
	public CruiseshipStockDetail getByUuid(String uuid);
	
	public CruiseshipStockDetail getByStockUuid(String stockUuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据邮轮uuid和船期日期获取所有的邮轮切位和余位信息
	 * @Description: 
	 * @param @param shipInfoUuid
	 * @param @param shipDate
	 * @param @return   
	 * @return List<CruiseshipStock>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public List<CruiseshipStockDetail> getStockDetailInfos(String shipInfoUuid, Date shipDate);
	
	/**
	 * 根据游轮库存uuid获取所有的船期信息
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @return   
	 * @return List<CruiseshipStockDetail>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public List<CruiseshipStockDetail> getStockDetailInfos(String cruiseshipStockUuid);
	/**
     * 223-tgy:
     * 获得cruiseship_stock_detail表中的信息.
     */
	public List<Map<String, Object>> getShipStockDetailByUuidAndShipdate(String cruiseshipUUid,String shipdate);
    /**
     *  根据csd表的主键id查询船期信息
     * @param keyId
     * @return
     */
	public List<Map<String, Object>> doGetDetailsById(String keyId);
	
}
