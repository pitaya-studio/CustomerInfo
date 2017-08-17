/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.exception.common.ExceptionConstants;
import com.trekiz.admin.common.exception.util.LogMessageUtil;
import com.trekiz.admin.common.exception.util.QuauqExceptionFactory;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipAnnexDao;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockDao;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockDetailDao;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockGroupRelDao;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockOrderDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStock;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockLog;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockOrder;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockInput;
import com.trekiz.admin.modules.cruiseship.jsonbean.CruiseshipOrderQueryJsonBean;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockLogService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CruiseshipStockServiceImpl  extends BaseService implements CruiseshipStockService{
	@Autowired
	private CruiseshipStockDao cruiseshipStockDao;
	@Autowired
	private CruiseshipStockGroupRelDao cruiseshipStockGroupRelDao;
	@Autowired
	private CruiseshipStockOrderDao cruiseshipStockOrderDao;
	@Autowired
	private CruiseshipStockDetailDao cruiseshipStockDetailDao;
	@Autowired
	private CruiseshipAnnexDao cruiseshipAnnexDao;
	@Autowired
	private CruiseshipStockLogService cruiseshipStockLogService;
	

	public void save (CruiseshipStock cruiseshipStock){
		super.setOptInfo(cruiseshipStock, BaseService.OPERATION_ADD);
		cruiseshipStockDao.saveObj(cruiseshipStock);
	}
	
	public void save (CruiseshipStockInput cruiseshipStockInput){
		CruiseshipStock cruiseshipStock = cruiseshipStockInput.getCruiseshipStock();
		super.setOptInfo(cruiseshipStock, BaseService.OPERATION_ADD);
		cruiseshipStockDao.saveObj(cruiseshipStock);
	}
	
	public void update (CruiseshipStock cruiseshipStock){
		super.setOptInfo(cruiseshipStock, BaseService.OPERATION_UPDATE);
		cruiseshipStockDao.updateObj(cruiseshipStock);
	}
	
	public CruiseshipStock getById(java.lang.Integer value) {
		return cruiseshipStockDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		CruiseshipStock obj = cruiseshipStockDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<CruiseshipStock> find(Page<CruiseshipStock> page, CruiseshipStockQuery cruiseshipStockQuery) {
		DetachedCriteria dc = cruiseshipStockDao.createDetachedCriteria();
		
	   	if(cruiseshipStockQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockQuery.getCruiseshipInfoUuid())){
			dc.add(Restrictions.eq("cruiseshipInfoUuid", cruiseshipStockQuery.getCruiseshipInfoUuid()));
		}
		if(cruiseshipStockQuery.getShipDate()!=null){
			dc.add(Restrictions.eq("shipDate", cruiseshipStockQuery.getShipDate()));
		}
	   	if(cruiseshipStockQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockQuery.getMemo())){
			dc.add(Restrictions.eq("memo", cruiseshipStockQuery.getMemo()));
		}
	   	if(cruiseshipStockQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockQuery.getCreateBy()));
	   	}
		if(cruiseshipStockQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockQuery.getCreateDate()));
		}
	   	if(cruiseshipStockQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockQuery.getDelFlag()));
		}

		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockDao.find(page, dc);
	}
	
	public List<CruiseshipStock> find( CruiseshipStockQuery cruiseshipStockQuery) {
		DetachedCriteria dc = cruiseshipStockDao.createDetachedCriteria();
		
	   	if(cruiseshipStockQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockQuery.getCruiseshipInfoUuid())){
			dc.add(Restrictions.eq("cruiseshipInfoUuid", cruiseshipStockQuery.getCruiseshipInfoUuid()));
		}
		if(cruiseshipStockQuery.getShipDate()!=null){
			dc.add(Restrictions.eq("shipDate", cruiseshipStockQuery.getShipDate()));
		}
	   	if(cruiseshipStockQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockQuery.getMemo())){
			dc.add(Restrictions.eq("memo", cruiseshipStockQuery.getMemo()));
		}
	   	if(cruiseshipStockQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockQuery.getCreateBy()));
	   	}
		if(cruiseshipStockQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockQuery.getCreateDate()));
		}
	   	if(cruiseshipStockQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockDao.find(dc);
	}
	
	public CruiseshipStock getByUuid(String uuid) {
		return cruiseshipStockDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		CruiseshipStock cruiseshipStock = getByUuid(uuid);
		cruiseshipStock.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(cruiseshipStock);
	}
	
	public boolean batchDelete(String[] uuids) {
		return cruiseshipStockDao.batchDelete(uuids);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void saveCruiseshipStock(String jsontext) throws BaseException4Quauq {
		
		Integer companyId = UserUtils.getUser().getCompany().getId().intValue();
		List<CruiseshipStock> stockList = new ArrayList<CruiseshipStock>();
		List<CruiseshipStockDetail> stocDetailList = new ArrayList<CruiseshipStockDetail>();
		List<CruiseshipAnnex> attachmentList = new ArrayList<CruiseshipAnnex>();
		try{
			JSONArray  jsonArray = JSONObject.parseArray(jsontext);
			for (int i = 0; i < jsonArray.size(); i++) {
				//库存表
				JSONObject cruiseshipInfo = jsonArray.getJSONObject(i);
				String cruiseshipInfoUuid = cruiseshipInfo.getString("cruiseshipInfoUuid");
				String shipDate = cruiseshipInfo.getString("shipDate");
				CruiseshipStock stock = new CruiseshipStock();
				stock.setCruiseshipInfoUuid(cruiseshipInfoUuid);
				stock.setMemo(cruiseshipInfo.getString("memo"));
				stock.setShipDateString(shipDate);
				stock.setWholesalerId(companyId);
				super.setOptInfo(stock, BaseService.OPERATION_ADD);
				stockList.add(stock);
				// 附件信息
				JSONArray attachmentInfo = cruiseshipInfo.getJSONArray("attachment");
				if (attachmentInfo != null) {
					for (int k = 0; k < attachmentInfo.size(); k++) {
						JSONObject attachment = attachmentInfo.getJSONObject(k);
						CruiseshipAnnex annex = new CruiseshipAnnex();
						annex.setMainUuid(stock.getUuid());
						annex.setDocId(attachment.getInteger("docId"));
						annex.setDocName(attachment.getString("docName"));
						annex.setDocPath(attachment.getString("docPath"));
						annex.setType(2);
						annex.setWholesalerId(companyId);
						super.setOptInfo(annex, BaseService.OPERATION_ADD);
						attachmentList.add(annex);
					}
				}
				// 库存明细信息
				JSONArray detailInfo = cruiseshipInfo.getJSONArray("stockInfoList");
				if (detailInfo != null) {
					for (int j = 0; j < detailInfo.size(); j++) {
						JSONObject datail = detailInfo.getJSONObject(j);
						CruiseshipStockDetail stockDetail = new CruiseshipStockDetail();
						stockDetail.setCruiseshipInfoUuid(cruiseshipInfoUuid);
						stockDetail.setCruiseshipStockUuid(stock.getUuid());
						stockDetail.setShipDateString(shipDate);
						stockDetail.setFreePosition(datail.getInteger("freePosition"));
						stockDetail.setStockAmount(datail.getInteger("stockAmount"));
						stockDetail.setCruiseshipCabinUuid(datail.getString("cruiseshipCabinUuid"));
						stockDetail.setWholesalerId(companyId);
						super.setOptInfo(stockDetail,BaseService.OPERATION_ADD);
						stocDetailList.add(stockDetail);
					}
				}
			}
			cruiseshipStockDao.batchSave(stockList);
			cruiseshipStockDetailDao.batchSave(stocDetailList);
			cruiseshipAnnexDao.batchSave(attachmentList);
		} catch (Exception e) {
			//TODO  LOG is error 
			//记录错误日志 
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING);
			logger.error(message);
			//并抛出异常
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING, message);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Page<Map<Object, Object>> findStockList(Map<String, String> parameters, Page<Map<Object, Object>> page) {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("stock.uuid AS uuid, ");		//库存uuid
		sb.append("info.name AS name, ");
		sb.append("stock.ship_date AS shipDate ");
//		sb.append("stockGroupRel.status AS status ");
		sb.append("FROM cruiseship_info info LEFT JOIN cruiseship_stock stock ON info.uuid=stock.cruiseship_info_uuid ");
//		sb.append("LEFT JOIN cruiseship_stock_group_rel stockGroupRel ON stock.uuid=stockGroupRel.cruiseship_stock_uuid ");
		sb.append("WHERE 1=1 AND stock.delflag = 0 ");
		
		List<Object> listser = new ArrayList<Object>();
		
		Long wholesalerId = UserUtils.getUser().getCompany().getId();
		sb.append(" AND info.wholesaler_id=" + wholesalerId.intValue());
		
		if(StringUtils.isNotBlank(parameters.get("curiseshipName"))){
			sb.append(" AND name like '%").append(parameters.get("curiseshipName")).append("%'");
//			listser
		}
		if(StringUtils.isNotBlank(parameters.get("shipDateBegin"))){
			sb.append(" AND ship_date >= '").append(parameters.get("shipDateBegin")).append("'");
		}
		if(StringUtils.isNotBlank(parameters.get("shipDateEnd"))){
			sb.append(" AND ship_date <= '").append(parameters.get("shipDateEnd")).append("'");
		}

		return cruiseshipStockDao.findPageBySql(page, sb.toString(), Map.class);
	}


	/**
	 * 根据库存uuid查询关联状态
	 * @date 2016年3月17日
	 */
	@Override
	public int getStatusByStockUuid(String cruiseshipStockUuid){
		StringBuffer sb = new StringBuffer();
		sb.append("select * ");
		sb.append("from cruiseship_stock_group_rel where cruiseship_stock_uuid='");
		sb.append(cruiseshipStockUuid);
		sb.append("'");
		List<CruiseshipStockGroupRel> list =  cruiseshipStockGroupRelDao.findBySql(sb.toString(), CruiseshipStockGroupRel.class);
		if(list != null && list.size() == 1) {
			return list.get(0).getStatus();
		}
		if(list != null && list.size() > 1) {
			int countZero = 0;
			int countOne = 0;
			for (CruiseshipStockGroupRel cruiseshipStockGroupRel : list) {
				if(cruiseshipStockGroupRel.getStatus() == 1) {
					countOne++;
				}
				if(cruiseshipStockGroupRel.getStatus() == 0) {
					countZero++;
				}
			}
			if(countZero > 0) {
				return 0;
			} else {
				return 1;
			}
		}
		return -1;
	}


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
	public List<CruiseshipStock> getStocksByShipInfoUuid(String cruiseshipInfoUuid) {
		List<CruiseshipStock> stocks = cruiseshipStockDao.getStocksByShipInfoUuid(cruiseshipInfoUuid);
		if(CollectionUtils.isNotEmpty(stocks)) {
			for(CruiseshipStock stock : stocks) {
				stock.setCruiseshipCabinList(cruiseshipStockDetailDao.getStockDetailInfos(stock.getCruiseshipStockUuid()));
			}
		}
		
		return stocks;
	}
	
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
	public List<CruiseshipStockDetail> getStockDetailInfos(String shipInfoUuid, Date shipDate) {
		return cruiseshipStockDetailDao.getStockDetailInfos(shipInfoUuid, shipDate);
	}
	
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
	public List<CruiseshipStockGroupRel> queryRelProducts(String cruiseshipStockUuid, String queryStatus) {
		return cruiseshipStockGroupRelDao.queryRelProducts(cruiseshipStockUuid, queryStatus);
	}
	
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
	public boolean delRelProduct(String relUuid) {
		if(StringUtils.isEmpty(relUuid)) {
			return false;
		}
		CruiseshipStockGroupRel groupRel = cruiseshipStockGroupRelDao.getByUuid(relUuid);
		if(BaseEntity.DEL_FLAG_DELETE.equals(groupRel.getDelFlag())) {
			return true;
		}
		groupRel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		return true;
	}
	
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
	public List<Map<String,Object>> queryCreateUsersByStockUuid(String cruiseshipStockUuid) {
		return cruiseshipStockOrderDao.queryCreateUsersByStockUuid(cruiseshipStockUuid);
	}
	
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
	public Map<String, List<Map<String, List<Map<String,Object>>>>> queryStockOrderInfos(String queryStockOrderInfo) {
		CruiseshipOrderQueryJsonBean queryStockOrder = JSON.parseObject(queryStockOrderInfo, CruiseshipOrderQueryJsonBean.class);
		if(StringUtils.isEmpty(queryStockOrder.getCruiseshipStockUuid())) {
			return null;
		}
		String aId="";
		List<CruiseshipStockOrder> activityList = queryStockOrder.getActivityList();
		for(CruiseshipStockOrder cruiseshipStockOrder:activityList){
			if(aId==""){
				aId=cruiseshipStockOrder.getActivityId().toString();
			}else{
				aId=aId+","+cruiseshipStockOrder.getActivityId().toString();
			}
		}
		
		//数据库结构：Map<"下单人ID1", Map<库存明细UUID1, 库存订单集合>>
		Map<String, List<Map<String, List<Map<String,Object>>>>> stockOrderDataMap = new HashMap<String, List<Map<String, List<Map<String,Object>>>>>();
		
		List<CruiseshipStockOrder> stockOrders =  cruiseshipStockOrderDao.getStockOrdersByOrderQueryJsonBean(queryStockOrder);
		Set<CruiseshipStockOrder> stock=new HashSet<CruiseshipStockOrder>();
		for(CruiseshipStockOrder stockOrder:stockOrders){
			stock.add(stockOrder);
		}
//		List<String> createBys=new ArrayList<String>();
		String createBy="";
		
		if(CollectionUtils.isNotEmpty(stock)) {
			for(CruiseshipStockOrder stockOrder : stock) {
				List<Map<String, List<Map<String,Object>>>> list2=new ArrayList<Map<String, List<Map<String,Object>>>>();
				Map<String,List<Map<String,Object>>> map2=new HashMap<String,List<Map<String,Object>>>();
				createBy = String.valueOf(stockOrder.getCreateBy());
				String stockDetailUuid = stockOrder.getCruiseshipStockDetailUuid();
				StringBuffer sbf=new StringBuffer();
				sbf.append("SELECT ");
				sbf.append("uuid," +
						"activity_id as activityId," +
						"departureCityName," +
						"sex," +
						"f_num as fNum," +
						"m_num as mNum," +
						"f_piece as fPiece," +
						"m_piece as mPiece from cruiseship_stock_order where cruiseship_stock_uuid=? ");
				sbf.append("AND cruiseship_stock_detail_uuid=? ");
				sbf.append("AND wholesaler_id=? ");
				sbf.append("AND delFlag=? ");	
				sbf.append("AND createBy=? ");
				sbf.append("AND activity_id in ( "+aId+")");
				List<Map<String,Object>> list = cruiseshipStockOrderDao.findBySql(sbf.toString(), Map.class,queryStockOrder.getCruiseshipStockUuid(),stockDetailUuid,UserUtils.getUser().getCompany().getId().intValue(),0,createBy);
				map2.put(stockDetailUuid, list);
				list2.add(map2);
				if(stockOrderDataMap.containsKey(createBy)){
					stockOrderDataMap.get(createBy).add(map2);
				}else{
					stockOrderDataMap.put(createBy, list2);
				}
			}
		}
	
		return stockOrderDataMap;
	}

	
	/**
	 * 关联记录列表
	 * @param stockUuid 库存Uuid
	 * @return
	 */
	
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<Map<String,Object>> cruiseshipStockGroupRelList(String stockUuid) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT ");
		stringBuffer.append("c.`STATUS` as cstatus, ");
		stringBuffer.append("a.groupCode as groupCode, ");
		stringBuffer.append("c.createBy as createBy, ");
		stringBuffer.append("c.createDate as createDate, ");
		stringBuffer.append("u.name ");
		stringBuffer.append("FROM ");
		stringBuffer.append("cruiseship_stock_group_rel c ");
		stringBuffer.append("LEFT JOIN travelactivity t ON c.activity_id = t.id ");
		stringBuffer.append("LEFT JOIN activitygroup a ON a.id = c.activitygroup_id ");
		stringBuffer.append("LEFT JOIN sys_user u on c.createBy=u.id ");
		stringBuffer.append("WHERE ");
		stringBuffer.append("c.cruiseship_stock_uuid = ? and c.delFlag=0 and c.wholesaler_id=? and c.status=0 ");
		return cruiseshipStockDao.findBySql(stringBuffer.toString(), Map.class,stockUuid,UserUtils.getUser().getCompany().getId().intValue());
	}
	
	
	@Override
	@Transactional(readOnly = true ,rollbackFor = Exception.class)
	public List<Map<String,Object>> stockUpdateList (String uuid,String stockUuid,String cruiseshipInfoUuid,String shipDate){
		StringBuffer buffer  = new StringBuffer();
		buffer.append("select  c.createBy,u.name,c.createDate,c.operate_source,c.operate_type,c.operate_num,c.stock_amount,c.free_position,c.stock_amount_after,c.free_position_after ");
		buffer.append(" from  cruiseship_stock_log c left join sys_user u on c.createBy=u.id where c.cruiseship_cabin_uuid = ? " );
		buffer.append("and c.cruiseship_stock_uuid=? and c.cruiseship_info_uuid=? ");
		buffer.append("and c.ship_date=? and c.wholesaler_id=? ");
		return cruiseshipStockDao.findBySql(buffer.toString(),Map.class, uuid,stockUuid,cruiseshipInfoUuid,shipDate,UserUtils.getUser().getCompany().getId().intValue());
	}
	
	
	/**
	 * 库存修改
	 * songyang
	 */
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public void cruiseshipStockGroupRelUpdate(HttpServletRequest request,boolean isOk) {
		String stockUuid = request.getParameter("stockUuid");
		String uuidss=request.getParameter("uuid");
		String[] uuids=uuidss.split(",");
		int ok=Integer.parseInt(request.getParameter("isOk"));
		for(String uuid:uuids){
			CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailDao.getByUuid(uuid);
	 		int stockAmount = cruiseshipStockDetail.getStockAmount();//修改前库存数
			int freePosition = cruiseshipStockDetail.getFreePosition(); //修改前余位数
			int stockAmountAfter = cruiseshipStockDetail.getStockAmount();//修改后库存数
			int freePositionAfter = cruiseshipStockDetail.getFreePosition();//修改后余位数
			int inputNum =  Integer.parseInt(request.getParameter("operateCount"));//增加/减少的数量
			int stockOrFree  = Integer.parseInt(request.getParameter("cabinOrStock"));//0 库存 1余位
			int addOrCut = Integer.parseInt(request.getParameter("operate"));//1增加 2减少
		
			if(stockOrFree == 0){
				if(addOrCut==1){
					stockAmountAfter += inputNum;
					if(ok==1){
						freePositionAfter+=inputNum;
					}
				}else{
					stockAmountAfter -= inputNum;
					if(ok==1){
						freePositionAfter-=inputNum;
					}
				}
			}else{
				if(addOrCut==1){
					freePositionAfter += inputNum ;	
					if(ok==1){
						stockAmountAfter+=inputNum;
					}
				}else{
					freePositionAfter -= inputNum ;	
					if(ok==1){
						stockAmountAfter-=inputNum;
					}
				}
			}
			try {
				String sql =  "";
				if(isOk){
					//已关联
					sql ="UPDATE cruiseship_stock_detail SET stock_amount = '"+stockAmountAfter+"', free_position='"+freePositionAfter+"' WHERE uuid = '"+uuid+"'";
				}else{
					//未关联
					sql ="UPDATE cruiseship_stock_detail SET stock_amount = '"+stockAmountAfter+"', free_position='"+stockAmountAfter+"' WHERE uuid = '"+uuid+"'";
				}
	 			cruiseshipStockDao.updateBySql(sql);
				// 保存操作日志
				CruiseshipStockLog cruiseshipStockLog = new CruiseshipStockLog();
				cruiseshipStockLog.setUuid(UuidUtils.generUuid());//UUID
				cruiseshipStockLog.setCruiseshipStockUuid(cruiseshipStockDetail.getCruiseshipStockUuid());//库存UUID
				cruiseshipStockLog.setCruiseshipInfoUuid(cruiseshipStockDetail.getCruiseshipInfoUuid());//游轮UUID
				cruiseshipStockLog.setShipDate(cruiseshipStockDetail.getShipDate());//船期
				cruiseshipStockLog.setCruiseshipCabinUuid(cruiseshipStockDetail.getCruiseshipCabinUuid());//舱型
				cruiseshipStockLog.setStockAmount(stockAmount);//修改前库存
				cruiseshipStockLog.setFreePosition(freePosition);//修改前余位
				cruiseshipStockLog.setWholesalerId(cruiseshipStockDetail.getWholesalerId());//批发商id
				cruiseshipStockLog.setOperateSource(stockOrFree);//操作源 1：库存；2：余位；
				cruiseshipStockLog.setOperateType(addOrCut);//操作类型1：增加；2：减少；
				cruiseshipStockLog.setOperateNum(inputNum);//操作数量
				cruiseshipStockLog.setStockAmountAfter(stockAmountAfter);//修改后库存
				cruiseshipStockLog.setFreePositionAfter(freePositionAfter);//修改后余位
				cruiseshipStockLog.setCreateBy(UserUtils.getUser().getId().intValue());//创建人
				cruiseshipStockLog.setCreateDate(new java.util.Date());//创建时间
				cruiseshipStockLog.setUpdateBy(UserUtils.getUser().getId().intValue());//更新人
				cruiseshipStockLog.setUpdateDate(new java.util.Date());//更新时间
				cruiseshipStockLog.setDelFlag("0");//删除标记0 正常
				cruiseshipStockLogService.save(cruiseshipStockLog);
			} catch (Exception e) {
				//记录错误日志 
				//String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_);
				//logger.error(message);
				//并抛出异常
				//throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING, message);
			}
		}
		
	}
	

	
	/**
	 * 验证是否关联产品
	 * songyang
	 */
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public boolean checkStockActivity(String stockUuid) {
		boolean isOk = false;
		String sql  ="select t.* from  travelactivity  t LEFT JOIN cruiseship_stock_group_rel c  on t.id = c.activity_id  where  c.`STATUS`=0  and c.cruiseship_stock_uuid =? and t.delFlag=0";
		List<Object> list =cruiseshipStockDao.findBySql(sql,TravelActivity.class,stockUuid);
		if(list!=null&&list.size()>0){
			isOk = true;
		}
		return isOk;
	}
	
	
	
	/**
	 * 验证产品是否已关联团期
	 * songyang
	 */
	@Override
	public boolean checkStockActivityId(String activityId,String stockUuid) {
		boolean isOk = false;
		String sql  ="select DISTINCT t.* from  travelactivity  t LEFT JOIN cruiseship_stock_group_rel c  on t.id = c.activity_id  where  c.`STATUS`=0  and t.id =? and t.delFlag=0 and c.cruiseship_stock_uuid=?";
		List<Object> list =cruiseshipStockDao.findBySql(sql,TravelActivity.class,activityId,stockUuid);
		if(list!=null&&list.size()>0){
			isOk = true;
		}
		return isOk;
	}
	

	@Override
	public List<Map<String, Object>> getCabinList(String cruiseshipUuid) {
		String sql = "SELECT cc.uuid AS cruiseshipCabinUuid ,cc.name AS cruiseshipCabinName FROM cruiseship_cabin cc WHERE cc.delFlag = 0 AND cc.cruiseship_info_uuid =? AND cc.wholesaler_id =?";
		return cruiseshipStockDao.findBySql(sql, Map.class, cruiseshipUuid,UserUtils.getUser().getCompany().getId());
	}

	@Override
	public List<Object> getShipDate(String cruiseshipUuid) {
		String sql = "SELECT  cs.ship_date  FROM cruiseship_stock cs WHERE cs.delFlag = 0 AND cs.cruiseship_info_uuid = ?";
		return cruiseshipStockDao.findBySql(sql, cruiseshipUuid);
	}

	@Override
	public void editStockDetail(String memo, String stockUuid, List<CruiseshipAnnex> annexList) {
		CruiseshipStock stock = cruiseshipStockDao.getByUuid(stockUuid);
		//备注的处理
		if(!memo.equals(stock.getMemo())){
			stock.setMemo(memo);
			super.setOptInfo(stock, BaseService.OPERATION_UPDATE);
			cruiseshipStockDao.saveObj(stock);
		}
	   //上传的附件的处理
		int wholesalerId = UserUtils.getUser().getCompany().getId().intValue();
		cruiseshipAnnexDao.synDocInfo(stockUuid, 2, wholesalerId, annexList);
	}

    /**
     * 获得游轮的库存信息(游轮名称,uuid)-223-tgy
     */
	@Override
	public List<Map<String,Object>> getCruiseshipNamesUuids() {
		
		return cruiseshipStockDao.getCruiseshipNamesUuids();
	}


	@Override
	public List<Map<String, Object>> getShipInfo() {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("info.uuid as uuid,info.name as name from cruiseship_info info left join cruiseship_stock s on info.id=s.cruiseship_info_uuid ");
		sbf.append("where s.cruiseship_info_uuid is not null and s.wholesaler_id=? and s.delFlag=0 and info.delFlag=0 ");
		List<Map<String,Object>> list=cruiseshipStockDao.findBySql(sbf.toString(),Map.class,UserUtils.getUser().getCompany().getId().intValue());
		return list;
	}
    /**
     * 223-tgy
     * 根据游轮的uuid获取该游轮的所有效船期.
     *  查询表cruiseship_stock表
     */
	@Override
	public List<Map<String, Object>> getShipDateByCruiseUuid(String cruiseshipUUid) {
		return cruiseshipStockDao.getShipDateByCruiseUuid(cruiseshipUUid);
	}

	@Override
	public List<CruiseshipStockDetail> getDetailByStockUuid(String stockUuid) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("select id,uuid,cruiseship_stock_uuid,cruiseship_info_uuid,ship_date,cruiseship_cabin_uuid,stock_amount,free_position,wholesaler_id,createBy,createDate,updateBy,updateDate,delFlag ");
		sbf.append("from cruiseship_stock_detail where cruiseship_stock_uuid=? and delFlag=0 and wholesaler_id=?");
		List<CruiseshipStockDetail> findBySql = cruiseshipStockDao.findBySql(sbf.toString(),CruiseshipStockDetail.class, stockUuid,UserUtils.getUser().getCompany().getId().intValue());
		return findBySql;
	}

	
}
