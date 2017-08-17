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
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderChangePriceDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderChangePrice;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderChangePriceInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderChangePriceQuery;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderChangePriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketOrderChangePriceServiceImpl  extends BaseService implements AirticketOrderChangePriceService{
	@Autowired
	private AirticketOrderChangePriceDao airticketOrderChangePriceDao;

	public void save (AirticketOrderChangePrice airticketOrderChangePrice){
		super.setOptInfo(airticketOrderChangePrice, BaseService.OPERATION_ADD);
		airticketOrderChangePriceDao.saveObj(airticketOrderChangePrice);
	}
	
	public void save (AirticketOrderChangePriceInput airticketOrderChangePriceInput){
		AirticketOrderChangePrice airticketOrderChangePrice = airticketOrderChangePriceInput.getAirticketOrderChangePrice();
		super.setOptInfo(airticketOrderChangePrice, BaseService.OPERATION_ADD);
		airticketOrderChangePriceDao.saveObj(airticketOrderChangePrice);
	}
	
	public void update (AirticketOrderChangePrice airticketOrderChangePrice){
		super.setOptInfo(airticketOrderChangePrice, BaseService.OPERATION_UPDATE);
		airticketOrderChangePriceDao.updateObj(airticketOrderChangePrice);
	}
	
	public AirticketOrderChangePrice getById(java.lang.Integer value) {
		return airticketOrderChangePriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketOrderChangePrice obj = airticketOrderChangePriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketOrderChangePrice> find(Page<AirticketOrderChangePrice> page, AirticketOrderChangePriceQuery airticketOrderChangePriceQuery) {
		DetachedCriteria dc = airticketOrderChangePriceDao.createDetachedCriteria();
		
	   	if(airticketOrderChangePriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderChangePriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderChangePriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderChangePriceQuery.getUuid()));
		}
	   	if(airticketOrderChangePriceQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderChangePriceQuery.getAirticketOrderId()));
	   	}
	   	if(airticketOrderChangePriceQuery.getChangeType()!=null){
	   		dc.add(Restrictions.eq("changeType", airticketOrderChangePriceQuery.getChangeType()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderChangePriceQuery.getFundsName())){
			dc.add(Restrictions.eq("fundsName", airticketOrderChangePriceQuery.getFundsName()));
		}
	   	if(airticketOrderChangePriceQuery.getComputeType()!=null){
	   		dc.add(Restrictions.eq("computeType", airticketOrderChangePriceQuery.getComputeType()));
	   	}
	   	if(airticketOrderChangePriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", airticketOrderChangePriceQuery.getCurrencyId()));
	   	}
	   	if(airticketOrderChangePriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", airticketOrderChangePriceQuery.getPrice()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderChangePriceQuery.getMemo())){
			dc.add(Restrictions.eq("memo", airticketOrderChangePriceQuery.getMemo()));
		}
	   	if(airticketOrderChangePriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderChangePriceQuery.getCreateBy()));
	   	}
		if(airticketOrderChangePriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderChangePriceQuery.getCreateDate()));
		}
	   	if(airticketOrderChangePriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderChangePriceQuery.getUpdateBy()));
	   	}
		if(airticketOrderChangePriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderChangePriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderChangePriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderChangePriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderChangePriceDao.find(page, dc);
	}
	
	public List<AirticketOrderChangePrice> find( AirticketOrderChangePriceQuery airticketOrderChangePriceQuery) {
		DetachedCriteria dc = airticketOrderChangePriceDao.createDetachedCriteria();
		
	   	if(airticketOrderChangePriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderChangePriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderChangePriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderChangePriceQuery.getUuid()));
		}
	   	if(airticketOrderChangePriceQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderChangePriceQuery.getAirticketOrderId()));
	   	}
	   	if(airticketOrderChangePriceQuery.getChangeType()!=null){
	   		dc.add(Restrictions.eq("changeType", airticketOrderChangePriceQuery.getChangeType()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderChangePriceQuery.getFundsName())){
			dc.add(Restrictions.eq("fundsName", airticketOrderChangePriceQuery.getFundsName()));
		}
	   	if(airticketOrderChangePriceQuery.getComputeType()!=null){
	   		dc.add(Restrictions.eq("computeType", airticketOrderChangePriceQuery.getComputeType()));
	   	}
	   	if(airticketOrderChangePriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", airticketOrderChangePriceQuery.getCurrencyId()));
	   	}
	   	if(airticketOrderChangePriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", airticketOrderChangePriceQuery.getPrice()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderChangePriceQuery.getMemo())){
			dc.add(Restrictions.eq("memo", airticketOrderChangePriceQuery.getMemo()));
		}
	   	if(airticketOrderChangePriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderChangePriceQuery.getCreateBy()));
	   	}
		if(airticketOrderChangePriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderChangePriceQuery.getCreateDate()));
		}
	   	if(airticketOrderChangePriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderChangePriceQuery.getUpdateBy()));
	   	}
		if(airticketOrderChangePriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderChangePriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderChangePriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderChangePriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderChangePriceDao.find(dc);
	}
	
	public AirticketOrderChangePrice getByUuid(String uuid) {
		return airticketOrderChangePriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketOrderChangePrice airticketOrderChangePrice = getByUuid(uuid);
		airticketOrderChangePrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketOrderChangePrice);
	}
	
	/**
	 * 删除操作
	 * @param uuid
	 */
	public void deleteByOrderId(Long id){
		String sql = "delete from airticket_order_changePrice where airticket_order_id = "+id;
		airticketOrderChangePriceDao.getSession().createSQLQuery(sql).executeUpdate();
	}
}
