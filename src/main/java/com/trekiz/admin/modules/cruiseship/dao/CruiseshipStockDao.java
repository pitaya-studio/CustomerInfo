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
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStock;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface CruiseshipStockDao  extends BaseDao<CruiseshipStock> {
	
	public CruiseshipStock getByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据游轮信息uuid获取该游轮下所有的船期
	 * @Description: 
	 * @param @param shipInfoUuid
	 * @param @return   
	 * @return List<Date>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public List<CruiseshipStock> getStocksByShipInfoUuid(String cruiseshipInfoUuid);
    /**
     * 获得游轮的库存信息(游轮名称,uuid)-223-tgy
     * @return
     */
	public List<Map<String,Object>>getCruiseshipNamesUuids();
    /**
     * 223-tgy
     *根据游轮的uuid获取该游轮的所有效船期.
     * @param cruiseshipUUid
     * @return
     */
	public List<Map<String, Object>> getShipDateByCruiseUuid(String cruiseshipUUid);
	
}
