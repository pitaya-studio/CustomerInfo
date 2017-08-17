/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockLogDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockLog;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockLogInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockLogQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockLogService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CruiseshipStockLogServiceImpl  extends BaseService implements CruiseshipStockLogService{
	@Autowired
	private CruiseshipStockLogDao cruiseshipStockLogDao;

	public void save (CruiseshipStockLog cruiseshipStockLog){
		super.setOptInfo(cruiseshipStockLog, BaseService.OPERATION_ADD);
		cruiseshipStockLogDao.saveObj(cruiseshipStockLog);
	}
	
	public void save (CruiseshipStockLogInput cruiseshipStockLogInput){
		CruiseshipStockLog cruiseshipStockLog = cruiseshipStockLogInput.getCruiseshipStockLog();
		super.setOptInfo(cruiseshipStockLog, BaseService.OPERATION_ADD);
		cruiseshipStockLogDao.saveObj(cruiseshipStockLog);
	}
	
	public void update (CruiseshipStockLog cruiseshipStockLog){
		super.setOptInfo(cruiseshipStockLog, BaseService.OPERATION_UPDATE);
		cruiseshipStockLogDao.updateObj(cruiseshipStockLog);
	}
	
	public CruiseshipStockLog getById(java.lang.Integer value) {
		return cruiseshipStockLogDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		CruiseshipStockLog obj = cruiseshipStockLogDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<CruiseshipStockLog> find(Page<CruiseshipStockLog> page, CruiseshipStockLogQuery cruiseshipStockLogQuery) {
		DetachedCriteria dc = cruiseshipStockLogDao.createDetachedCriteria();
		
	   	if(cruiseshipStockLogQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockLogQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockLogQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getCruiseshipStockUuid())){
			dc.add(Restrictions.eq("cruiseshipStockUuid", cruiseshipStockLogQuery.getCruiseshipStockUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getCruiseshipInfoUuid())){
			dc.add(Restrictions.eq("cruiseshipInfoUuid", cruiseshipStockLogQuery.getCruiseshipInfoUuid()));
		}
		if(cruiseshipStockLogQuery.getShipDate()!=null){
			dc.add(Restrictions.eq("shipDate", cruiseshipStockLogQuery.getShipDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getCruiseshipCabinUuid())){
			dc.add(Restrictions.eq("cruiseshipCabinUuid", cruiseshipStockLogQuery.getCruiseshipCabinUuid()));
		}
	   	if(cruiseshipStockLogQuery.getStockAmount()!=null){
	   		dc.add(Restrictions.eq("stockAmount", cruiseshipStockLogQuery.getStockAmount()));
	   	}
	   	if(cruiseshipStockLogQuery.getFreePosition()!=null){
	   		dc.add(Restrictions.eq("freePosition", cruiseshipStockLogQuery.getFreePosition()));
	   	}
	   	if(cruiseshipStockLogQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockLogQuery.getWholesalerId()));
	   	}
	   	if(cruiseshipStockLogQuery.getOperateSource()!=null){
	   		dc.add(Restrictions.eq("operateSource", cruiseshipStockLogQuery.getOperateSource()));
	   	}
	   	if(cruiseshipStockLogQuery.getOperateType()!=null){
	   		dc.add(Restrictions.eq("operateType", cruiseshipStockLogQuery.getOperateType()));
	   	}
	   	if(cruiseshipStockLogQuery.getOperateNum()!=null){
	   		dc.add(Restrictions.eq("operateNum", cruiseshipStockLogQuery.getOperateNum()));
	   	}
	   	if(cruiseshipStockLogQuery.getStockAmountAfter()!=null){
	   		dc.add(Restrictions.eq("stockAmountAfter", cruiseshipStockLogQuery.getStockAmountAfter()));
	   	}
	   	if(cruiseshipStockLogQuery.getFreePositionAfter()!=null){
	   		dc.add(Restrictions.eq("freePositionAfter", cruiseshipStockLogQuery.getFreePositionAfter()));
	   	}
	   	if(cruiseshipStockLogQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockLogQuery.getCreateBy()));
	   	}
		if(cruiseshipStockLogQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockLogQuery.getCreateDate()));
		}
	   	if(cruiseshipStockLogQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockLogQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockLogQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockLogQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockLogQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockLogDao.find(page, dc);
	}
	
	public List<CruiseshipStockLog> find( CruiseshipStockLogQuery cruiseshipStockLogQuery) {
		DetachedCriteria dc = cruiseshipStockLogDao.createDetachedCriteria();
		
	   	if(cruiseshipStockLogQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockLogQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockLogQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getCruiseshipStockUuid())){
			dc.add(Restrictions.eq("cruiseshipStockUuid", cruiseshipStockLogQuery.getCruiseshipStockUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getCruiseshipInfoUuid())){
			dc.add(Restrictions.eq("cruiseshipInfoUuid", cruiseshipStockLogQuery.getCruiseshipInfoUuid()));
		}
		if(cruiseshipStockLogQuery.getShipDate()!=null){
			dc.add(Restrictions.eq("shipDate", cruiseshipStockLogQuery.getShipDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getCruiseshipCabinUuid())){
			dc.add(Restrictions.eq("cruiseshipCabinUuid", cruiseshipStockLogQuery.getCruiseshipCabinUuid()));
		}
	   	if(cruiseshipStockLogQuery.getStockAmount()!=null){
	   		dc.add(Restrictions.eq("stockAmount", cruiseshipStockLogQuery.getStockAmount()));
	   	}
	   	if(cruiseshipStockLogQuery.getFreePosition()!=null){
	   		dc.add(Restrictions.eq("freePosition", cruiseshipStockLogQuery.getFreePosition()));
	   	}
	   	if(cruiseshipStockLogQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockLogQuery.getWholesalerId()));
	   	}
	   	if(cruiseshipStockLogQuery.getOperateSource()!=null){
	   		dc.add(Restrictions.eq("operateSource", cruiseshipStockLogQuery.getOperateSource()));
	   	}
	   	if(cruiseshipStockLogQuery.getOperateType()!=null){
	   		dc.add(Restrictions.eq("operateType", cruiseshipStockLogQuery.getOperateType()));
	   	}
	   	if(cruiseshipStockLogQuery.getOperateNum()!=null){
	   		dc.add(Restrictions.eq("operateNum", cruiseshipStockLogQuery.getOperateNum()));
	   	}
	   	if(cruiseshipStockLogQuery.getStockAmountAfter()!=null){
	   		dc.add(Restrictions.eq("stockAmountAfter", cruiseshipStockLogQuery.getStockAmountAfter()));
	   	}
	   	if(cruiseshipStockLogQuery.getFreePositionAfter()!=null){
	   		dc.add(Restrictions.eq("freePositionAfter", cruiseshipStockLogQuery.getFreePositionAfter()));
	   	}
	   	if(cruiseshipStockLogQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockLogQuery.getCreateBy()));
	   	}
		if(cruiseshipStockLogQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockLogQuery.getCreateDate()));
		}
	   	if(cruiseshipStockLogQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockLogQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockLogQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockLogQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockLogQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockLogQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockLogDao.find(dc);
	}
	
	public CruiseshipStockLog getByUuid(String uuid) {
		return cruiseshipStockLogDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		CruiseshipStockLog cruiseshipStockLog = getByUuid(uuid);
		cruiseshipStockLog.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(cruiseshipStockLog);
	}
	
	public boolean batchDelete(String[] uuids) {
		return cruiseshipStockLogDao.batchDelete(uuids);
	}
	
}
