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
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface CruiseshipStockGroupRelDao  extends BaseDao<CruiseshipStockGroupRel> {
	
	public CruiseshipStockGroupRel getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据库存uuid和关联状态获取关联产品信息
	 * @Description: 
	 * @param @param cruiseshipStockUuid
	 * @param @param queryStatus
	 * @param @return   
	 * @return List<CruiseshipStockGroupRel>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public List<CruiseshipStockGroupRel> queryRelProducts(String cruiseshipStockUuid, String queryStatus);
	
	/**
	 * @Description: 根据cruiseshipstockdetailId 获取  CruiseshipStockGroupRel
	 * @author xinwei.wang
	 * @date 2016年3月9日下午4:42:32
	 * @param cruiseshipUUid
	 * @param shipdate
	 * @return    
	 * @throws
	 */
	public CruiseshipStockGroupRel getCruiseShipRelByActivityGroupId(Integer activitygroupid);
    /**
     * 223:tgy
     * 根据团期id查询表cruiseship_group_rel和activitygroup表,获得关联状态,cruiseship_stock_detail表的id,关联日期和操作人
     * @param agId
     * @return
     */
	public List<Map<String, Object>> getRelInfo(String agId);
	
}
