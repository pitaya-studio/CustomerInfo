/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.service.impl;

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
import com.trekiz.admin.modules.temp.stock.dao.AirticketreservefileTempDao;
import com.trekiz.admin.modules.temp.stock.entity.AirticketreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.AirticketreservefileTempInput;
import com.trekiz.admin.modules.temp.stock.query.AirticketreservefileTempQuery;
import com.trekiz.admin.modules.temp.stock.service.AirticketreservefileTempService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketreservefileTempServiceImpl  extends BaseService implements AirticketreservefileTempService{
	@Autowired
	private AirticketreservefileTempDao airticketreservefileTempDao;

	public void save (AirticketreservefileTemp airticketreservefileTemp){
		super.setOptInfo(airticketreservefileTemp, BaseService.OPERATION_ADD);
		airticketreservefileTempDao.saveObj(airticketreservefileTemp);
	}
	
	public void save (AirticketreservefileTempInput airticketreservefileTempInput){
		AirticketreservefileTemp airticketreservefileTemp = airticketreservefileTempInput.getAirticketreservefileTemp();
		super.setOptInfo(airticketreservefileTemp, BaseService.OPERATION_ADD);
		airticketreservefileTempDao.saveObj(airticketreservefileTemp);
	}
	
	public void update (AirticketreservefileTemp airticketreservefileTemp){
		super.setOptInfo(airticketreservefileTemp, BaseService.OPERATION_UPDATE);
		airticketreservefileTempDao.updateObj(airticketreservefileTemp);
	}
	
	public AirticketreservefileTemp getById(java.lang.Integer value) {
		return airticketreservefileTempDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketreservefileTemp obj = airticketreservefileTempDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketreservefileTemp> find(Page<AirticketreservefileTemp> page, AirticketreservefileTempQuery airticketreservefileTempQuery) {
		DetachedCriteria dc = airticketreservefileTempDao.createDetachedCriteria();
		
	   	if(airticketreservefileTempQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketreservefileTempQuery.getId()));
	   	}
	   	if(airticketreservefileTempQuery.getAirticketActivityId()!=null){
	   		dc.add(Restrictions.eq("airticketActivityId", airticketreservefileTempQuery.getAirticketActivityId()));
	   	}
	   	if(airticketreservefileTempQuery.getAgentId()!=null){
	   		dc.add(Restrictions.eq("agentId", airticketreservefileTempQuery.getAgentId()));
	   	}
	   	if(airticketreservefileTempQuery.getSrcDocId()!=null){
	   		dc.add(Restrictions.eq("srcDocId", airticketreservefileTempQuery.getSrcDocId()));
	   	}
		if (StringUtils.isNotEmpty(airticketreservefileTempQuery.getFileName())){
			dc.add(Restrictions.eq("fileName", airticketreservefileTempQuery.getFileName()));
		}
		if(airticketreservefileTempQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketreservefileTempQuery.getCreateDate()));
		}
	   	if(airticketreservefileTempQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketreservefileTempQuery.getCreateBy()));
	   	}
		if(airticketreservefileTempQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketreservefileTempQuery.getUpdateDate()));
		}
	   	if(airticketreservefileTempQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketreservefileTempQuery.getUpdateBy()));
	   	}
		if (StringUtils.isNotEmpty(airticketreservefileTempQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketreservefileTempQuery.getDelFlag()));
		}
	   	if(airticketreservefileTempQuery.getReserveOrderId()!=null){
	   		dc.add(Restrictions.eq("reserveOrderId", airticketreservefileTempQuery.getReserveOrderId()));
	   	}
		
		//dc.addOrder(Order.desc("id"));
		return airticketreservefileTempDao.find(page, dc);
	}
	
	public List<AirticketreservefileTemp> find( AirticketreservefileTempQuery airticketreservefileTempQuery) {
		DetachedCriteria dc = airticketreservefileTempDao.createDetachedCriteria();
		
	   	if(airticketreservefileTempQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketreservefileTempQuery.getId()));
	   	}
	   	if(airticketreservefileTempQuery.getAirticketActivityId()!=null){
	   		dc.add(Restrictions.eq("airticketActivityId", airticketreservefileTempQuery.getAirticketActivityId()));
	   	}
	   	if(airticketreservefileTempQuery.getAgentId()!=null){
	   		dc.add(Restrictions.eq("agentId", airticketreservefileTempQuery.getAgentId()));
	   	}
	   	if(airticketreservefileTempQuery.getSrcDocId()!=null){
	   		dc.add(Restrictions.eq("srcDocId", airticketreservefileTempQuery.getSrcDocId()));
	   	}
		if (StringUtils.isNotEmpty(airticketreservefileTempQuery.getFileName())){
			dc.add(Restrictions.eq("fileName", airticketreservefileTempQuery.getFileName()));
		}
		if(airticketreservefileTempQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketreservefileTempQuery.getCreateDate()));
		}
	   	if(airticketreservefileTempQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketreservefileTempQuery.getCreateBy()));
	   	}
		if(airticketreservefileTempQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketreservefileTempQuery.getUpdateDate()));
		}
	   	if(airticketreservefileTempQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketreservefileTempQuery.getUpdateBy()));
	   	}
		if (StringUtils.isNotEmpty(airticketreservefileTempQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketreservefileTempQuery.getDelFlag()));
		}
	   	if(airticketreservefileTempQuery.getReserveOrderId()!=null){
	   		dc.add(Restrictions.eq("reserveOrderId", airticketreservefileTempQuery.getReserveOrderId()));
	   	}
		
		//dc.addOrder(Order.desc("id"));
		return airticketreservefileTempDao.find(dc);
	}
	
	public AirticketreservefileTemp getByUuid(String uuid) {
		return airticketreservefileTempDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketreservefileTemp airticketreservefileTemp = getByUuid(uuid);
		airticketreservefileTemp.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketreservefileTemp);
	}
	
	public boolean batchDelete(String[] uuids) {
		return airticketreservefileTempDao.batchDelete(uuids);
	}
	
}
