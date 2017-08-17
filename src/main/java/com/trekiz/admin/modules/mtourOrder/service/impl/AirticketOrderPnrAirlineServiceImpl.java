/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service.impl;

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
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderPnrAirlineDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirline;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderPnrAirlineInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderPnrAirlineQuery;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderPnrAirlineService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketOrderPnrAirlineServiceImpl  extends BaseService implements AirticketOrderPnrAirlineService{
	@Autowired
	private AirticketOrderPnrAirlineDao airticketOrderPnrAirlineDao;

	public void save (AirticketOrderPnrAirline airticketOrderPnrAirline){
		super.setOptInfo(airticketOrderPnrAirline, BaseService.OPERATION_ADD);
		airticketOrderPnrAirlineDao.saveObj(airticketOrderPnrAirline);
	}
	
	public void save (AirticketOrderPnrAirlineInput airticketOrderPnrAirlineInput){
		AirticketOrderPnrAirline airticketOrderPnrAirline = airticketOrderPnrAirlineInput.getAirticketOrderPnrAirline();
		super.setOptInfo(airticketOrderPnrAirline, BaseService.OPERATION_ADD);
		airticketOrderPnrAirlineDao.saveObj(airticketOrderPnrAirline);
	}
	
	public void update (AirticketOrderPnrAirline airticketOrderPnrAirline){
		super.setOptInfo(airticketOrderPnrAirline, BaseService.OPERATION_UPDATE);
		airticketOrderPnrAirlineDao.updateObj(airticketOrderPnrAirline);
	}
	
	public AirticketOrderPnrAirline getById(java.lang.Integer value) {
		return airticketOrderPnrAirlineDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketOrderPnrAirline obj = airticketOrderPnrAirlineDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketOrderPnrAirline> find(Page<AirticketOrderPnrAirline> page, AirticketOrderPnrAirlineQuery airticketOrderPnrAirlineQuery) {
		DetachedCriteria dc = airticketOrderPnrAirlineDao.createDetachedCriteria();
		
	   	if(airticketOrderPnrAirlineQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderPnrAirlineQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlineQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderPnrAirlineQuery.getUuid()));
		}
	   	if(airticketOrderPnrAirlineQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderPnrAirlineQuery.getAirticketOrderId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlineQuery.getAirticketOrderPnrUuid())){
			dc.add(Restrictions.eq("airticketOrderPnrUuid", airticketOrderPnrAirlineQuery.getAirticketOrderPnrUuid()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlineQuery.getAirlineName())){
			dc.add(Restrictions.eq("airlineName", airticketOrderPnrAirlineQuery.getAirlineName()));
		}
	   	if(airticketOrderPnrAirlineQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderPnrAirlineQuery.getCreateBy()));
	   	}
		if(airticketOrderPnrAirlineQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderPnrAirlineQuery.getCreateDate()));
		}
	   	if(airticketOrderPnrAirlineQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderPnrAirlineQuery.getUpdateBy()));
	   	}
		if(airticketOrderPnrAirlineQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderPnrAirlineQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlineQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderPnrAirlineQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderPnrAirlineDao.find(page, dc);
	}
	
	public List<AirticketOrderPnrAirline> find( AirticketOrderPnrAirlineQuery airticketOrderPnrAirlineQuery) {
		DetachedCriteria dc = airticketOrderPnrAirlineDao.createDetachedCriteria();
		
	   	if(airticketOrderPnrAirlineQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderPnrAirlineQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlineQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderPnrAirlineQuery.getUuid()));
		}
	   	if(airticketOrderPnrAirlineQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderPnrAirlineQuery.getAirticketOrderId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlineQuery.getAirticketOrderPnrUuid())){
			dc.add(Restrictions.eq("airticketOrderPnrUuid", airticketOrderPnrAirlineQuery.getAirticketOrderPnrUuid()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlineQuery.getAirlineName())){
			dc.add(Restrictions.eq("airlineName", airticketOrderPnrAirlineQuery.getAirlineName()));
		}
	   	if(airticketOrderPnrAirlineQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderPnrAirlineQuery.getCreateBy()));
	   	}
		if(airticketOrderPnrAirlineQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderPnrAirlineQuery.getCreateDate()));
		}
	   	if(airticketOrderPnrAirlineQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderPnrAirlineQuery.getUpdateBy()));
	   	}
		if(airticketOrderPnrAirlineQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderPnrAirlineQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlineQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderPnrAirlineQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderPnrAirlineDao.find(dc);
	}
	
	public AirticketOrderPnrAirline getByUuid(String uuid) {
		return airticketOrderPnrAirlineDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketOrderPnrAirline airticketOrderPnrAirline = getByUuid(uuid);
		airticketOrderPnrAirline.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketOrderPnrAirline);
	}
}
