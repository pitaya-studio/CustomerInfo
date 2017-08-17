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
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderPnrGroupDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrGroup;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderPnrGroupInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderPnrGroupQuery;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderPnrGroupService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketOrderPnrGroupServiceImpl  extends BaseService implements AirticketOrderPnrGroupService{
	@Autowired
	private AirticketOrderPnrGroupDao airticketOrderPnrGroupDao;

	public void save (AirticketOrderPnrGroup airticketOrderPnrGroup){
		super.setOptInfo(airticketOrderPnrGroup, BaseService.OPERATION_ADD);
		airticketOrderPnrGroupDao.saveObj(airticketOrderPnrGroup);
	}
	
	public void save (AirticketOrderPnrGroupInput airticketOrderPnrGroupInput){
		AirticketOrderPnrGroup airticketOrderPnrGroup = airticketOrderPnrGroupInput.getAirticketOrderPnrGroup();
		super.setOptInfo(airticketOrderPnrGroup, BaseService.OPERATION_ADD);
		airticketOrderPnrGroupDao.saveObj(airticketOrderPnrGroup);
	}
	
	public void update (AirticketOrderPnrGroup airticketOrderPnrGroup){
		super.setOptInfo(airticketOrderPnrGroup, BaseService.OPERATION_UPDATE);
		airticketOrderPnrGroupDao.updateObj(airticketOrderPnrGroup);
	}
	
	public AirticketOrderPnrGroup getById(java.lang.Integer value) {
		return airticketOrderPnrGroupDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketOrderPnrGroup obj = airticketOrderPnrGroupDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketOrderPnrGroup> find(Page<AirticketOrderPnrGroup> page, AirticketOrderPnrGroupQuery airticketOrderPnrGroupQuery) {
		DetachedCriteria dc = airticketOrderPnrGroupDao.createDetachedCriteria();
		
	   	if(airticketOrderPnrGroupQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderPnrGroupQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrGroupQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderPnrGroupQuery.getUuid()));
		}
	   	if(airticketOrderPnrGroupQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderPnrGroupQuery.getAirticketOrderId()));
	   	}
	   	if(airticketOrderPnrGroupQuery.getTicketPersonNum()!=null){
	   		dc.add(Restrictions.eq("ticketPersonNum", airticketOrderPnrGroupQuery.getTicketPersonNum()));
	   	}
	   	if(airticketOrderPnrGroupQuery.getPersonNum()!=null){
	   		dc.add(Restrictions.eq("personNum", airticketOrderPnrGroupQuery.getPersonNum()));
	   	}
	   	if(airticketOrderPnrGroupQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderPnrGroupQuery.getCreateBy()));
	   	}
		if(airticketOrderPnrGroupQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderPnrGroupQuery.getCreateDate()));
		}
	   	if(airticketOrderPnrGroupQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderPnrGroupQuery.getUpdateBy()));
	   	}
		if(airticketOrderPnrGroupQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderPnrGroupQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrGroupQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderPnrGroupQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderPnrGroupDao.find(page, dc);
	}
	
	public List<AirticketOrderPnrGroup> find( AirticketOrderPnrGroupQuery airticketOrderPnrGroupQuery) {
		DetachedCriteria dc = airticketOrderPnrGroupDao.createDetachedCriteria();
		
	   	if(airticketOrderPnrGroupQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderPnrGroupQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrGroupQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderPnrGroupQuery.getUuid()));
		}
	   	if(airticketOrderPnrGroupQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderPnrGroupQuery.getAirticketOrderId()));
	   	}
	   	if(airticketOrderPnrGroupQuery.getTicketPersonNum()!=null){
	   		dc.add(Restrictions.eq("ticketPersonNum", airticketOrderPnrGroupQuery.getTicketPersonNum()));
	   	}
	   	if(airticketOrderPnrGroupQuery.getPersonNum()!=null){
	   		dc.add(Restrictions.eq("personNum", airticketOrderPnrGroupQuery.getPersonNum()));
	   	}
	   	if(airticketOrderPnrGroupQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderPnrGroupQuery.getCreateBy()));
	   	}
		if(airticketOrderPnrGroupQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderPnrGroupQuery.getCreateDate()));
		}
	   	if(airticketOrderPnrGroupQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderPnrGroupQuery.getUpdateBy()));
	   	}
		if(airticketOrderPnrGroupQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderPnrGroupQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrGroupQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderPnrGroupQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderPnrGroupDao.find(dc);
	}
	
	public AirticketOrderPnrGroup getByUuid(String uuid) {
		return airticketOrderPnrGroupDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketOrderPnrGroup airticketOrderPnrGroup = getByUuid(uuid);
		airticketOrderPnrGroup.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketOrderPnrGroup);
	}
	
	/**
	 * 根据订单id返回pnr组
	 * @param orderId
	 * @return
	 */
	public List<AirticketOrderPnrGroup> getListByOrderId(String orderId) {
		String sql = "select id,uuid,airticket_order_id,ticket_personnum,person_num,pnr_group_index,createby,createdate,updateby,updatedate,delflag "
				   + "from airticket_order_pnrGroup where delflag='0' and airticket_order_id = ? ";
		return airticketOrderPnrGroupDao.findBySql(sql, orderId);
	}
}
