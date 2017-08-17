/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
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
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockOrderDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockOrder;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockOrderInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockOrderQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockDetailService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockOrderService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CruiseshipStockOrderServiceImpl  extends BaseService implements CruiseshipStockOrderService{
	@Autowired
	private CruiseshipStockOrderDao cruiseshipStockOrderDao;
	
	@Autowired
	private TravelActivityService travelActivityService;
	
	@Autowired 
	private CruiseshipStockDetailService cruiseshipStockDetailService;

	public void save (CruiseshipStockOrder cruiseshipStockOrder){
		super.setOptInfo(cruiseshipStockOrder, BaseService.OPERATION_ADD);
		cruiseshipStockOrderDao.saveObj(cruiseshipStockOrder);
	}
	
	public void save (CruiseshipStockOrderInput cruiseshipStockOrderInput){
		CruiseshipStockOrder cruiseshipStockOrder = cruiseshipStockOrderInput.getCruiseshipStockOrder();
		super.setOptInfo(cruiseshipStockOrder, BaseService.OPERATION_ADD);
		cruiseshipStockOrderDao.saveObj(cruiseshipStockOrder);
	}
	
	public void update (CruiseshipStockOrder cruiseshipStockOrder){
		super.setOptInfo(cruiseshipStockOrder, BaseService.OPERATION_UPDATE);
		cruiseshipStockOrderDao.updateObj(cruiseshipStockOrder);
	}
	
	public CruiseshipStockOrder getById(java.lang.Integer value) {
		return cruiseshipStockOrderDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		CruiseshipStockOrder obj = cruiseshipStockOrderDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<CruiseshipStockOrder> find(Page<CruiseshipStockOrder> page, CruiseshipStockOrderQuery cruiseshipStockOrderQuery) {
		DetachedCriteria dc = cruiseshipStockOrderDao.createDetachedCriteria();
		
	   	if(cruiseshipStockOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockOrderQuery.getUuid()));
		}
	   	if(cruiseshipStockOrderQuery.getActivityId()!=null){
	   		dc.add(Restrictions.eq("activityId", cruiseshipStockOrderQuery.getActivityId()));
	   	}
	   	if(cruiseshipStockOrderQuery.getActivityType()!=null){
	   		dc.add(Restrictions.eq("activityType", cruiseshipStockOrderQuery.getActivityType()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getActivityName())){
			dc.add(Restrictions.eq("activityName", cruiseshipStockOrderQuery.getActivityName()));
		}
	   	if(cruiseshipStockOrderQuery.getDepartureCityId()!=null){
	   		dc.add(Restrictions.eq("departureCityId", cruiseshipStockOrderQuery.getDepartureCityId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getDepartureCityName())){
			dc.add(Restrictions.eq("departureCityName", cruiseshipStockOrderQuery.getDepartureCityName()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getCruiseshipStockUuid())){
			dc.add(Restrictions.eq("cruiseshipStockUuid", cruiseshipStockOrderQuery.getCruiseshipStockUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getCruiseshipStockDetailUuid())){
			dc.add(Restrictions.eq("cruiseshipStockDetailUuid", cruiseshipStockOrderQuery.getCruiseshipStockDetailUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getCruiseshipCabinName())){
			dc.add(Restrictions.eq("cruiseshipCabinName", cruiseshipStockOrderQuery.getCruiseshipCabinName()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getSex())){
			dc.add(Restrictions.eq("sex", cruiseshipStockOrderQuery.getSex()));
		}
	   	if(cruiseshipStockOrderQuery.getFnum()!=null){
	   		dc.add(Restrictions.eq("fnum", cruiseshipStockOrderQuery.getFnum()));
	   	}
	   	if(cruiseshipStockOrderQuery.getMnum()!=null){
	   		dc.add(Restrictions.eq("mnum", cruiseshipStockOrderQuery.getMnum()));
	   	}
	   	if(cruiseshipStockOrderQuery.getFpiece()!=null){
	   		dc.add(Restrictions.eq("fpiece", cruiseshipStockOrderQuery.getFpiece()));
	   	}
	   	if(cruiseshipStockOrderQuery.getMpiece()!=null){
	   		dc.add(Restrictions.eq("mpiece", cruiseshipStockOrderQuery.getMpiece()));
	   	}
	   	if(cruiseshipStockOrderQuery.getAllNum()!=null){
	   		dc.add(Restrictions.eq("allNum", cruiseshipStockOrderQuery.getAllNum()));
	   	}
	   	if(cruiseshipStockOrderQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockOrderQuery.getWholesalerId()));
	   	}
	   	if(cruiseshipStockOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockOrderQuery.getCreateBy()));
	   	}
		if(cruiseshipStockOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockOrderQuery.getCreateDate()));
		}
	   	if(cruiseshipStockOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockOrderQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockOrderQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockOrderDao.find(page, dc);
	}
	
	public List<CruiseshipStockOrder> find( CruiseshipStockOrderQuery cruiseshipStockOrderQuery) {
		DetachedCriteria dc = cruiseshipStockOrderDao.createDetachedCriteria();
		
	   	if(cruiseshipStockOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockOrderQuery.getUuid()));
		}
	   	if(cruiseshipStockOrderQuery.getActivityId()!=null){
	   		dc.add(Restrictions.eq("activityId", cruiseshipStockOrderQuery.getActivityId()));
	   	}
	   	if(cruiseshipStockOrderQuery.getActivityType()!=null){
	   		dc.add(Restrictions.eq("activityType", cruiseshipStockOrderQuery.getActivityType()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getActivityName())){
			dc.add(Restrictions.eq("activityName", cruiseshipStockOrderQuery.getActivityName()));
		}
	   	if(cruiseshipStockOrderQuery.getDepartureCityId()!=null){
	   		dc.add(Restrictions.eq("departureCityId", cruiseshipStockOrderQuery.getDepartureCityId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getDepartureCityName())){
			dc.add(Restrictions.eq("departureCityName", cruiseshipStockOrderQuery.getDepartureCityName()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getCruiseshipStockUuid())){
			dc.add(Restrictions.eq("cruiseshipStockUuid", cruiseshipStockOrderQuery.getCruiseshipStockUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getCruiseshipStockDetailUuid())){
			dc.add(Restrictions.eq("cruiseshipStockDetailUuid", cruiseshipStockOrderQuery.getCruiseshipStockDetailUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getCruiseshipCabinName())){
			dc.add(Restrictions.eq("cruiseshipCabinName", cruiseshipStockOrderQuery.getCruiseshipCabinName()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getSex())){
			dc.add(Restrictions.eq("sex", cruiseshipStockOrderQuery.getSex()));
		}
	   	if(cruiseshipStockOrderQuery.getFnum()!=null){
	   		dc.add(Restrictions.eq("fnum", cruiseshipStockOrderQuery.getFnum()));
	   	}
	   	if(cruiseshipStockOrderQuery.getMnum()!=null){
	   		dc.add(Restrictions.eq("mnum", cruiseshipStockOrderQuery.getMnum()));
	   	}
	   	if(cruiseshipStockOrderQuery.getFpiece()!=null){
	   		dc.add(Restrictions.eq("fpiece", cruiseshipStockOrderQuery.getFpiece()));
	   	}
	   	if(cruiseshipStockOrderQuery.getMpiece()!=null){
	   		dc.add(Restrictions.eq("mpiece", cruiseshipStockOrderQuery.getMpiece()));
	   	}
	   	if(cruiseshipStockOrderQuery.getAllNum()!=null){
	   		dc.add(Restrictions.eq("allNum", cruiseshipStockOrderQuery.getAllNum()));
	   	}
	   	if(cruiseshipStockOrderQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockOrderQuery.getWholesalerId()));
	   	}
	   	if(cruiseshipStockOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockOrderQuery.getCreateBy()));
	   	}
		if(cruiseshipStockOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockOrderQuery.getCreateDate()));
		}
	   	if(cruiseshipStockOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockOrderQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockOrderQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockOrderDao.find(dc);
	}
	
	public CruiseshipStockOrder getByUuid(String uuid) {
		return cruiseshipStockOrderDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		CruiseshipStockOrder cruiseshipStockOrder = getByUuid(uuid);
		cruiseshipStockOrder.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(cruiseshipStockOrder);
	}
	
	public boolean batchDelete(String[] uuids) {
		return cruiseshipStockOrderDao.batchDelete(uuids);
	}

	/**
	 * 包名生成库存游轮订单 by chy 2016年2月2日17:04:32
	 * @param params
	 */
	@Override
	public  void makeOrder(Map<String, Object> params) throws BaseException4Quauq {
		try {
			//组织订单对象
			CruiseshipStockOrder cruiseshipStockOrder = new CruiseshipStockOrder();
			cruiseshipStockOrder.setUuid(UuidUtils.generUuid());//UUID
			String activityIdStr = params.get("activityId") == null ? "" : params.get("activityId").toString();
			if("".equals(activityIdStr) || !NumberUtils.isNumber(activityIdStr)){
				throw new RuntimeException("产品id不能为空");
			}
			Integer activityId = Integer.parseInt(activityIdStr);
			cruiseshipStockOrder.setActivityId(activityId);//"产品表ID"
			cruiseshipStockOrder.setActivityType(CruiseshipStockOrder.ACTIVITY_TYPE_SINGLE);//"产品团期表类型（1：activitygroup表；）" 
			//获取产品信息
			TravelActivity travelActivity = travelActivityService.findById(activityId.longValue());
			if(travelActivity == null){
				throw new RuntimeException("找不到产品信息");
			}
			cruiseshipStockOrder.setActivityName(travelActivity.getAcitivityName());//"产品名称"
			Integer fromArea = travelActivity.getFromArea();
			cruiseshipStockOrder.setDepartureCityId(fromArea);//"产品出发地ID"
			String departureCityName = DictUtils.getDictLabel(fromArea.toString(), Context.FROM_AREA, "");
			cruiseshipStockOrder.setDepartureCityName(departureCityName);//"产品出发地名称"
			String cruiseshipStockUuid = params.get("cruiseshipStockUuid").toString();
			cruiseshipStockOrder.setCruiseshipStockUuid(cruiseshipStockUuid);//"库存UUID"
			String cruiseshipStockDetailUuid = params.get("cruiseshipStockDetailUuid").toString();
			cruiseshipStockOrder.setCruiseshipStockDetailUuid(cruiseshipStockDetailUuid);//"库存明细UUID"
			String cruiseshipCabinName = params.get("cruiseshipCabinName").toString();
			cruiseshipStockOrder.setCruiseshipCabinName(cruiseshipCabinName);//"舱位名称"
			Integer fnum = Integer.parseInt(params.get("fnum").toString().isEmpty() ? "0" : params.get("fnum").toString());
			cruiseshipStockOrder.setFnum(fnum);//"女人数"
			Integer mnum = Integer.parseInt(params.get("mnum").toString().isEmpty() ? "0" : params.get("mnum").toString());
			cruiseshipStockOrder.setMnum(mnum);//"男人数"
			Integer fpiece = Integer.parseInt(params.get("fpiece").toString().isEmpty() ? "1" : params.get("fpiece").toString());
			cruiseshipStockOrder.setFpiece(fpiece);//"女拼（拼：0；不拼：1；）"
			Integer mpiece = Integer.parseInt(params.get("mpiece").toString().isEmpty() ? "1" : params.get("mpiece").toString());
			cruiseshipStockOrder.setMpiece(mpiece);//"男拼（拼：0；不拼：1；）"
			Integer allNum = Integer.parseInt(params.get("allNum").toString().isEmpty() ? "0" : params.get("allNum").toString());
			cruiseshipStockOrder.setAllNum(allNum);//"总人数"
			Integer wholesalerId = Integer.parseInt(params.get("wholesalerId").toString());
			cruiseshipStockOrder.setWholesalerId(wholesalerId);//"批发商id"
			cruiseshipStockOrder.setCreateBy(UserUtils.getUser().getId().intValue());//"创建人"
			cruiseshipStockOrder.setCreateDate(new Date());//"创建时间"
			cruiseshipStockOrder.setUpdateBy(UserUtils.getUser().getId().intValue());//"修改人"
			cruiseshipStockOrder.setUpdateDate(new Date());//"修改时间"
			cruiseshipStockOrder.setDelFlag("0");//"删除状态"
			//保存订单对象
			cruiseshipStockOrderDao.saveObj(cruiseshipStockOrder);
			//扣减余位
			CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getByUuid(cruiseshipStockDetailUuid);
			cruiseshipStockDetail.setFreePosition(cruiseshipStockDetail.getFreePosition() - allNum);//扣减余位
			cruiseshipStockDetail.setUpdateBy(UserUtils.getUser().getId().intValue());//更新人
			cruiseshipStockDetail.setUpdateDate(new Date());//更新时间
			cruiseshipStockDetailService.save(cruiseshipStockDetail);
		} catch (Exception e) {
			//记录错误日志 
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING);
			logger.error(message);
			//并抛出异常
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING, message);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getOrderByGroup(JSONObject json) {
		String activityId = "";
		if (json.get("activityId")!=null) {
		List<String> list	=(List<String>)json.get("activityId");
			for (String string : list) {
				if (activityId == "") {
					activityId = string;
				} else {
					activityId = activityId + "," + string;
				}
			}
		}
		String cruiseshipCabinUuid = "";
		if (json.get("cruiseshipCabinUuid")!=null) {
		List<String> list	=(List<String>)json.get("cruiseshipCabinUuid");
			for (String string : list) {
				if (cruiseshipCabinUuid == "") {
					cruiseshipCabinUuid = "'"+string+"'";
				} else {
					cruiseshipCabinUuid = cruiseshipCabinUuid + ",'" + string+"'";
				}
			}
		}
		String departureCityId = "";
		if (json.get("departureCityId")!=null) {
		List<String> list	=(List<String>)json.get("departureCityId");
			for (String string : list) {
				if (departureCityId == "") {
					departureCityId = string;
				} else {
					departureCityId = departureCityId + "," + string;
				}
			}
		}
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT distinct  detail.cruiseship_cabin_uuid as cruiseshipCabinUuid, stock.cruiseship_cabin_name as cruiseshipCabinName,");
		sbf.append("detail.free_position as freePosition ,");
		sbf.append("stock.cruiseship_stock_detail_uuid  as cruiseshipStockDetailUuid ");
//		sbf.append("detail.cruiseship_cabin_uuid as cruiseshipCabinUuid ");
		sbf.append("FROM cruiseship_stock_order stock ");
		sbf.append("LEFT JOIN cruiseship_stock_detail detail ON stock.cruiseship_stock_detail_uuid = detail.uuid ");
		sbf.append("WHERE  stock.createBy= "+UserUtils.getUser().getId().intValue()+" ");
//		System.out.println(json.get("cruiseshipStockUuid").toString());
		if(StringUtils.isNotBlank(json.get("cruiseshipStockUuid").toString())){
			sbf.append("And stock.cruiseship_stock_uuid= '"+json.get("cruiseshipStockUuid").toString()+"' ");
		}
		if(StringUtils.isNotBlank(activityId)){
			sbf.append("AND stock.activity_id in ( "+activityId+") ");
		}
		if(StringUtils.isNotBlank(cruiseshipCabinUuid)){
			sbf.append("AND detail.cruiseship_cabin_uuid in ( " +cruiseshipCabinUuid+") ");
		}
		if(StringUtils.isNotBlank(departureCityId)){
			sbf.append("AND stock.departureCityId in ( "+departureCityId+") ");
		}
		sbf.append("AND stock.wholesaler_id=? AND stock.delFlag=0 ");
		//sbf.append("GROUP BY stock.cruiseship_cabin_name");
		List<Map<String,Object>> stockOrderMaps=cruiseshipStockOrderDao.findBySql(sbf.toString(),Map.class,UserUtils.getUser().getCompany().getId().intValue());
		
		return stockOrderMaps;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getStockOrders(String name,
			String detailUuid,JSONObject  json) {
		String activityId = "";
		if (json.get("activityId")!=null) {
		List<String> list	=(List<String>)json.get("activityId");
			for (String string : list) {
				if (activityId == "") {
					activityId = string;
				} else {
					activityId = activityId + "," + string;
				}
			}
		}
		String cruiseshipCabinUuid = "";
		if (json.get("cruiseshipCabinUuid")!=null) {
		List<String> list	=(List<String>)json.get("cruiseshipCabinUuid");
			for (String string : list) {
				if (cruiseshipCabinUuid == "") {
					cruiseshipCabinUuid = "'"+string+"'";
				} else {
					cruiseshipCabinUuid = cruiseshipCabinUuid + ",'" + string+"'";
				}
			}
		}
		String departureCityId = "";
		if (json.get("departureCityId")!=null) {
		List<String> list	=(List<String>)json.get("departureCityId");
			for (String string : list) {
				if (departureCityId == "") {
					departureCityId = string;
				} else {
					departureCityId = departureCityId + "," + string;
				}
			}
		}
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT stock.uuid,");
		sbf.append("stock.activity_name as activityName,stock.activity_id as activityId,");
		sbf.append("stock.f_num as fNum,stock.f_piece as fPiece,");
		sbf.append("stock.m_num as mNum,stock.m_piece as mPiece,stock.departureCityId as departureCityId,");
		sbf.append("stock.departureCityName as departureCityName ");
		sbf.append("FROM cruiseship_stock_order stock LEFT JOIN cruiseship_stock_detail detail ON stock.cruiseship_stock_detail_uuid = detail.uuid  ");
		sbf.append("WHERE stock.cruiseship_cabin_name=? AND stock.cruiseship_stock_detail_uuid=? AND stock.wholesaler_id=? AND stock.delFlag=0 and stock.createBy=? ");
		if(StringUtils.isNotBlank(activityId)){
			sbf.append("AND stock.activity_id in ( "+activityId+") ");
		}
		if(StringUtils.isNotBlank(cruiseshipCabinUuid)){
			sbf.append("AND detail.cruiseship_cabin_uuid in ( " +cruiseshipCabinUuid+") ");
		}
		if(StringUtils.isNotBlank(departureCityId)){
			sbf.append("AND stock.departureCityId in ( "+departureCityId+") ");
		}
		List<Map<String,Object>> stockOrders=cruiseshipStockOrderDao.findBySql(sbf.toString(),Map.class, name,detailUuid,UserUtils.getUser().getCompany().getId().intValue(),UserUtils.getUser().getId().intValue());
		return stockOrders;
	}
	
}
