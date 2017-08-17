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
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderPnrAirlinePriceDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirlinePrice;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderPnrAirlinePriceInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderPnrAirlinePriceQuery;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderPnrAirlinePriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketOrderPnrAirlinePriceServiceImpl  extends BaseService implements AirticketOrderPnrAirlinePriceService{
	@Autowired
	private AirticketOrderPnrAirlinePriceDao airticketOrderPnrAirlinePriceDao;

	public void save (AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice){
		super.setOptInfo(airticketOrderPnrAirlinePrice, BaseService.OPERATION_ADD);
		airticketOrderPnrAirlinePriceDao.saveObj(airticketOrderPnrAirlinePrice);
	}
	
	public void save (AirticketOrderPnrAirlinePriceInput airticketOrderPnrAirlinePriceInput){
		AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice = airticketOrderPnrAirlinePriceInput.getAirticketOrderPnrAirlinePrice();
		super.setOptInfo(airticketOrderPnrAirlinePrice, BaseService.OPERATION_ADD);
		airticketOrderPnrAirlinePriceDao.saveObj(airticketOrderPnrAirlinePrice);
	}
	
	public void update (AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice){
		super.setOptInfo(airticketOrderPnrAirlinePrice, BaseService.OPERATION_UPDATE);
		airticketOrderPnrAirlinePriceDao.updateObj(airticketOrderPnrAirlinePrice);
	}
	
	public AirticketOrderPnrAirlinePrice getById(java.lang.Integer value) {
		return airticketOrderPnrAirlinePriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketOrderPnrAirlinePrice obj = airticketOrderPnrAirlinePriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketOrderPnrAirlinePrice> find(Page<AirticketOrderPnrAirlinePrice> page, AirticketOrderPnrAirlinePriceQuery airticketOrderPnrAirlinePriceQuery) {
		DetachedCriteria dc = airticketOrderPnrAirlinePriceDao.createDetachedCriteria();
		
	   	if(airticketOrderPnrAirlinePriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderPnrAirlinePriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlinePriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderPnrAirlinePriceQuery.getUuid()));
		}
	   	if(airticketOrderPnrAirlinePriceQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderPnrAirlinePriceQuery.getAirticketOrderId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlinePriceQuery.getAirticketOrderPnrUuid())){
			dc.add(Restrictions.eq("airticketOrderPnrUuid", airticketOrderPnrAirlinePriceQuery.getAirticketOrderPnrUuid()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlinePriceQuery.getAirticketOrderPnrAirlineUuid())){
			dc.add(Restrictions.eq("airticketOrderPnrAirlineUuid", airticketOrderPnrAirlinePriceQuery.getAirticketOrderPnrAirlineUuid()));
		}
	   	if(airticketOrderPnrAirlinePriceQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", airticketOrderPnrAirlinePriceQuery.getPriceType()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", airticketOrderPnrAirlinePriceQuery.getCurrencyId()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getPersonNum()!=null){
	   		dc.add(Restrictions.eq("personNum", airticketOrderPnrAirlinePriceQuery.getPersonNum()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", airticketOrderPnrAirlinePriceQuery.getPrice()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", airticketOrderPnrAirlinePriceQuery.getFrontMoney()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderPnrAirlinePriceQuery.getCreateBy()));
	   	}
		if(airticketOrderPnrAirlinePriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderPnrAirlinePriceQuery.getCreateDate()));
		}
	   	if(airticketOrderPnrAirlinePriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderPnrAirlinePriceQuery.getUpdateBy()));
	   	}
		if(airticketOrderPnrAirlinePriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderPnrAirlinePriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlinePriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderPnrAirlinePriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderPnrAirlinePriceDao.find(page, dc);
	}
	
	public List<AirticketOrderPnrAirlinePrice> find( AirticketOrderPnrAirlinePriceQuery airticketOrderPnrAirlinePriceQuery) {
		DetachedCriteria dc = airticketOrderPnrAirlinePriceDao.createDetachedCriteria();
		
	   	if(airticketOrderPnrAirlinePriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderPnrAirlinePriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlinePriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderPnrAirlinePriceQuery.getUuid()));
		}
	   	if(airticketOrderPnrAirlinePriceQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderPnrAirlinePriceQuery.getAirticketOrderId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlinePriceQuery.getAirticketOrderPnrUuid())){
			dc.add(Restrictions.eq("airticketOrderPnrUuid", airticketOrderPnrAirlinePriceQuery.getAirticketOrderPnrUuid()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlinePriceQuery.getAirticketOrderPnrAirlineUuid())){
			dc.add(Restrictions.eq("airticketOrderPnrAirlineUuid", airticketOrderPnrAirlinePriceQuery.getAirticketOrderPnrAirlineUuid()));
		}
	   	if(airticketOrderPnrAirlinePriceQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", airticketOrderPnrAirlinePriceQuery.getPriceType()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", airticketOrderPnrAirlinePriceQuery.getCurrencyId()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getPersonNum()!=null){
	   		dc.add(Restrictions.eq("personNum", airticketOrderPnrAirlinePriceQuery.getPersonNum()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", airticketOrderPnrAirlinePriceQuery.getPrice()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getFrontMoney()!=null){
	   		dc.add(Restrictions.eq("frontMoney", airticketOrderPnrAirlinePriceQuery.getFrontMoney()));
	   	}
	   	if(airticketOrderPnrAirlinePriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderPnrAirlinePriceQuery.getCreateBy()));
	   	}
		if(airticketOrderPnrAirlinePriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderPnrAirlinePriceQuery.getCreateDate()));
		}
	   	if(airticketOrderPnrAirlinePriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderPnrAirlinePriceQuery.getUpdateBy()));
	   	}
		if(airticketOrderPnrAirlinePriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderPnrAirlinePriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderPnrAirlinePriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderPnrAirlinePriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderPnrAirlinePriceDao.find(dc);
	}
	
	public AirticketOrderPnrAirlinePrice getByUuid(String uuid) {
		return airticketOrderPnrAirlinePriceDao.getByUuid(uuid);
	}
	
	public AirticketOrderPnrAirlinePrice getByCostRecordUuid(String costRecordUuid) {
		return airticketOrderPnrAirlinePriceDao.getByCostRecordUuid(costRecordUuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice = getByUuid(uuid);
		airticketOrderPnrAirlinePrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketOrderPnrAirlinePrice);
	}
}
