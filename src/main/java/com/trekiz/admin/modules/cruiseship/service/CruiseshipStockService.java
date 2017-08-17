/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStock;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CruiseshipStockService{
	
	public void save (CruiseshipStock cruiseshipStock);
	
	public void save (CruiseshipStockInput cruiseshipStockInput);
	
	public void update (CruiseshipStock cruiseshipStock);
	
	public CruiseshipStock getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<CruiseshipStock> find(Page<CruiseshipStock> page, CruiseshipStockQuery cruiseshipStockQuery);
	
	public List<CruiseshipStock> find( CruiseshipStockQuery cruiseshipStockQuery);
	
	public CruiseshipStock getByUuid(String uuid);
	
	public boolean checkStockActivity(String stockUuid);

	public boolean checkStockActivityId(String activityId,String stockUuid);
	
	public void removeByUuid(String uuid);
	
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
	public List<CruiseshipStock> getStocksByShipInfoUuid(String shipInfoUuid);
	
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
	 * 库存关联产品删除
	 * @Description: 
	 * @param @param relUuid
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-16
	 */
	public boolean delRelProduct(String relUuid);
	
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
	 * 根据库存订单信息获取库存订单信息集合
	 * @Description: 
	 * @param @param queryStockOrderInfo
	 * @param @return   
	 * @return Map<String,Object>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-2
	 */
	public Map<String, List<Map<String, List<Map<String,Object>>>>> queryStockOrderInfos(String queryStockOrderInfo);
	
	public void saveCruiseshipStock(String jsontext) throws BaseException4Quauq;

	/**
	 * @Description 库存列表
	 * @param parameters
	 * @param page
     * @return
	 * @Date 2016年2月3日
     */
	Page<Map<Object, Object>> findStockList(Map<String, String> parameters, Page<Map<Object, Object>> page);
	
	
	/**
	 * 关联记录列表
	 * @param stockUuid 库存Uuid
	 * @return
	 */
	List<Map<String,Object>> cruiseshipStockGroupRelList(String stockUuid);
	
	
	/**
	 * 库存修改
	 * @param request
	 * @param isOk
	 */
	public void cruiseshipStockGroupRelUpdate(HttpServletRequest request,boolean isOk);
	
	
	/**
	 * 库存修改记录查询
	 * @param stockUuid
	 * @return
	 */
	public List<Map<String,Object>> stockUpdateList (String uuid,String stockUuid,String cruiseshipInfoUuid,String shipDate);
	
	

	public List<Map<String, Object>> getCabinList(String cruiseshipUuid);

	public List<Object> getShipDate(String cruiseshipUuid);

	public void editStockDetail(String memo, String stockUuid, List<CruiseshipAnnex> annexList);
    /**
     * 获得游轮的库存信息(游轮名称,uuid)
     * @return
     */
	public List<Map<String, Object>> getCruiseshipNamesUuids();

	
	public List<Map<String,Object>> getShipInfo();
    /**
     * 根据游轮的uuid获取该游轮的所有效船期.
     * 查询表cruiseship_stock表
     * @param cruiseshipUUid
     * @return
     */
	public List<Map<String, Object>> getShipDateByCruiseUuid(String cruiseshipUUid);
	
	public List<CruiseshipStockDetail> getDetailByStockUuid(String stockUuid);

	/**
	 * 根据库存uuid查询status(关联状态)
	 * @date 2016年3月17日
	 */
	public int getStatusByStockUuid(String stockUuid);
}
