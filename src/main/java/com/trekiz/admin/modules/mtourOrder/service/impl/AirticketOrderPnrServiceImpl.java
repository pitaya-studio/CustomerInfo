/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderPnrDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnr;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderPnrInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderPnrQuery;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderPnrService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketOrderPnrServiceImpl  extends BaseService implements AirticketOrderPnrService{
	@Autowired
	private AirticketOrderPnrDao airticketOrderPnrDao;

	public void save (AirticketOrderPnr airticketOrderPnr){
		super.setOptInfo(airticketOrderPnr, BaseService.OPERATION_ADD);
		airticketOrderPnrDao.saveObj(airticketOrderPnr);
	}
	
	public void save (AirticketOrderPnrInput airticketOrderPnrInput){
		AirticketOrderPnr airticketOrderPnr = airticketOrderPnrInput.getAirticketOrderPnr();
		super.setOptInfo(airticketOrderPnr, BaseService.OPERATION_ADD);
		airticketOrderPnrDao.saveObj(airticketOrderPnr);
	}
	
	public void update (AirticketOrderPnr airticketOrderPnr){
		super.setOptInfo(airticketOrderPnr, BaseService.OPERATION_UPDATE);
		airticketOrderPnrDao.updateObj(airticketOrderPnr);
	}
	
	public AirticketOrderPnr getById(java.lang.Integer value) {
		return airticketOrderPnrDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketOrderPnr obj = airticketOrderPnrDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketOrderPnr> find(Page<AirticketOrderPnr> page, AirticketOrderPnrQuery airticketOrderPnrQuery) {
		DetachedCriteria dc = airticketOrderPnrDao.createDetachedCriteria();
		
	   	if(airticketOrderPnrQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderPnrQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderPnrQuery.getUuid()));
		}
	   	if(airticketOrderPnrQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderPnrQuery.getAirticketOrderId()));
	   	}
	   	if(airticketOrderPnrQuery.getCodeType()!=null){
	   		dc.add(Restrictions.eq("codeType", airticketOrderPnrQuery.getCodeType()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrQuery.getFlightPnr())){
			dc.add(Restrictions.eq("flightPnr", airticketOrderPnrQuery.getFlightPnr()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrQuery.getAirline())){
			dc.add(Restrictions.eq("airline", airticketOrderPnrQuery.getAirline()));
		}
		if(airticketOrderPnrQuery.getTicketDeadline()!=null){
			dc.add(Restrictions.eq("ticketDeadline", airticketOrderPnrQuery.getTicketDeadline()));
		}
		if(airticketOrderPnrQuery.getFrontDeadline()!=null){
			dc.add(Restrictions.eq("frontDeadline", airticketOrderPnrQuery.getFrontDeadline()));
		}
		if(airticketOrderPnrQuery.getListDeadline()!=null){
			dc.add(Restrictions.eq("listDeadline", airticketOrderPnrQuery.getListDeadline()));
		}
		if(airticketOrderPnrQuery.getRenameDeadline()!=null){
			dc.add(Restrictions.eq("renameDeadline", airticketOrderPnrQuery.getRenameDeadline()));
		}
		if(airticketOrderPnrQuery.getCancelDeadline()!=null){
			dc.add(Restrictions.eq("cancelDeadline", airticketOrderPnrQuery.getCancelDeadline()));
		}
	   	if(airticketOrderPnrQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderPnrQuery.getCreateBy()));
	   	}
		if(airticketOrderPnrQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderPnrQuery.getCreateDate()));
		}
	   	if(airticketOrderPnrQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderPnrQuery.getUpdateBy()));
	   	}
		if(airticketOrderPnrQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderPnrQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderPnrQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderPnrDao.find(page, dc);
	}
	
	public List<AirticketOrderPnr> find( AirticketOrderPnrQuery airticketOrderPnrQuery) {
		DetachedCriteria dc = airticketOrderPnrDao.createDetachedCriteria();
		
	   	if(airticketOrderPnrQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderPnrQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderPnrQuery.getUuid()));
		}
	   	if(airticketOrderPnrQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderPnrQuery.getAirticketOrderId()));
	   	}
	   	if(airticketOrderPnrQuery.getCodeType()!=null){
	   		dc.add(Restrictions.eq("codeType", airticketOrderPnrQuery.getCodeType()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrQuery.getFlightPnr())){
			dc.add(Restrictions.eq("flightPnr", airticketOrderPnrQuery.getFlightPnr()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrQuery.getAirline())){
			dc.add(Restrictions.eq("airline", airticketOrderPnrQuery.getAirline()));
		}
		if(airticketOrderPnrQuery.getTicketDeadline()!=null){
			dc.add(Restrictions.eq("ticketDeadline", airticketOrderPnrQuery.getTicketDeadline()));
		}
		if(airticketOrderPnrQuery.getFrontDeadline()!=null){
			dc.add(Restrictions.eq("frontDeadline", airticketOrderPnrQuery.getFrontDeadline()));
		}
		if(airticketOrderPnrQuery.getListDeadline()!=null){
			dc.add(Restrictions.eq("listDeadline", airticketOrderPnrQuery.getListDeadline()));
		}
		if(airticketOrderPnrQuery.getRenameDeadline()!=null){
			dc.add(Restrictions.eq("renameDeadline", airticketOrderPnrQuery.getRenameDeadline()));
		}
		if(airticketOrderPnrQuery.getCancelDeadline()!=null){
			dc.add(Restrictions.eq("cancelDeadline", airticketOrderPnrQuery.getCancelDeadline()));
		}
	   	if(airticketOrderPnrQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderPnrQuery.getCreateBy()));
	   	}
		if(airticketOrderPnrQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderPnrQuery.getCreateDate()));
		}
	   	if(airticketOrderPnrQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderPnrQuery.getUpdateBy()));
	   	}
		if(airticketOrderPnrQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderPnrQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderPnrQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderPnrDao.find(dc);
	}
	
	public AirticketOrderPnr getByUuid(String uuid) {
		return airticketOrderPnrDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketOrderPnr airticketOrderPnr = getByUuid(uuid);
		airticketOrderPnr.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketOrderPnr);
	}
	
	public List<Map<String,Object>> queryAirticketOrderPNCByOrderUuid(Integer orderId){
		return airticketOrderPnrDao.queryAirticketOrderPNCByOrderUuid(orderId);
	}

	@Override
	public List<AirticketOrderPnr> findByPNRGroupUuid(String pnrGroupUuid) {
		
		return airticketOrderPnrDao.findByPNRGroupUuid(pnrGroupUuid);
	}
	
	public List<AirticketOrderPnr> findByOrderId(Long id) {
		return airticketOrderPnrDao.find("from AirticketOrderPnr where airticket_order_id=?",id);
	}
	
}
