/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.HotelTravelerDao;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.input.HotelTravelerInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelerQuery;
import com.trekiz.admin.modules.hotel.service.HotelTravelerService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelTravelerServiceImpl  extends BaseService implements HotelTravelerService{
	@Autowired
	private HotelTravelerDao hotelTravelerDao;

	public void save (HotelTraveler hotelTraveler){
		super.setOptInfo(hotelTraveler, BaseService.OPERATION_ADD);
		hotelTravelerDao.saveObj(hotelTraveler);
	}
	
	public void save (HotelTravelerInput hotelTravelerInput){
		HotelTraveler hotelTraveler = hotelTravelerInput.getHotelTraveler();
		super.setOptInfo(hotelTraveler, BaseService.OPERATION_ADD);
		hotelTravelerDao.saveObj(hotelTraveler);
	}
	
	public void update (HotelTraveler hotelTraveler){
		super.setOptInfo(hotelTraveler, BaseService.OPERATION_UPDATE);
		hotelTravelerDao.updateObj(hotelTraveler);
	}
	
	public HotelTraveler getById(java.lang.Integer value) {
		return hotelTravelerDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelTraveler obj = hotelTravelerDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelTraveler> find(Page<HotelTraveler> page, HotelTravelerQuery hotelTravelerQuery) {
		DetachedCriteria dc = hotelTravelerDao.createDetachedCriteria();
		
	   	if(hotelTravelerQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelTravelerQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelTravelerQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", hotelTravelerQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getName())){
			dc.add(Restrictions.eq("name", hotelTravelerQuery.getName()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getNameSpell())){
			dc.add(Restrictions.eq("nameSpell", hotelTravelerQuery.getNameSpell()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getPersonType())){
			dc.add(Restrictions.eq("personType", hotelTravelerQuery.getPersonType()));
		}
	   	if(hotelTravelerQuery.getSex()!=null){
	   		dc.add(Restrictions.eq("sex", hotelTravelerQuery.getSex()));
	   	}
	   	if(hotelTravelerQuery.getNationality()!=null){
	   		dc.add(Restrictions.eq("nationality", hotelTravelerQuery.getNationality()));
	   	}
		if(hotelTravelerQuery.getBirthDay()!=null){
			dc.add(Restrictions.eq("birthDay", hotelTravelerQuery.getBirthDay()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getTelephone())){
			dc.add(Restrictions.eq("telephone", hotelTravelerQuery.getTelephone()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getRemark())){
			dc.add(Restrictions.eq("remark", hotelTravelerQuery.getRemark()));
		}
	   	if(hotelTravelerQuery.getSrcPrice()!=null){
	   		dc.add(Restrictions.eq("srcPrice", hotelTravelerQuery.getSrcPrice()));
	   	}
	   	if(hotelTravelerQuery.getSrcPriceCurrency()!=null){
	   		dc.add(Restrictions.eq("srcPriceCurrency", hotelTravelerQuery.getSrcPriceCurrency()));
	   	}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getOriginalPayPriceSerialNum())){
			dc.add(Restrictions.eq("originalPayPriceSerialNum", hotelTravelerQuery.getOriginalPayPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getCostPriceSerialNum())){
			dc.add(Restrictions.eq("costPriceSerialNum", hotelTravelerQuery.getCostPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getPayPriceSerialNum())){
			dc.add(Restrictions.eq("payPriceSerialNum", hotelTravelerQuery.getPayPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getRebatesMoneySerialNum())){
			dc.add(Restrictions.eq("rebatesMoneySerialNum", hotelTravelerQuery.getRebatesMoneySerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getJkSerialNum())){
			dc.add(Restrictions.eq("jkSerialNum", hotelTravelerQuery.getJkSerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getStatus())){
			dc.add(Restrictions.eq("status", hotelTravelerQuery.getStatus()));
		}
	   	if(hotelTravelerQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelTravelerQuery.getCreateBy()));
	   	}
		if(hotelTravelerQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelTravelerQuery.getCreateDate()));
		}
	   	if(hotelTravelerQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelTravelerQuery.getUpdateBy()));
	   	}
		if(hotelTravelerQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelTravelerQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelTravelerQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelTravelerDao.find(page, dc);
	}
	
	public List<HotelTraveler> find( HotelTravelerQuery hotelTravelerQuery) {
		DetachedCriteria dc = hotelTravelerDao.createDetachedCriteria();
		
	   	if(hotelTravelerQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelTravelerQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelTravelerQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", hotelTravelerQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getName())){
			dc.add(Restrictions.eq("name", hotelTravelerQuery.getName()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getNameSpell())){
			dc.add(Restrictions.eq("nameSpell", hotelTravelerQuery.getNameSpell()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getPersonType())){
			dc.add(Restrictions.eq("personType", hotelTravelerQuery.getPersonType()));
		}
	   	if(hotelTravelerQuery.getSex()!=null){
	   		dc.add(Restrictions.eq("sex", hotelTravelerQuery.getSex()));
	   	}
	   	if(hotelTravelerQuery.getNationality()!=null){
	   		dc.add(Restrictions.eq("nationality", hotelTravelerQuery.getNationality()));
	   	}
		if(hotelTravelerQuery.getBirthDay()!=null){
			dc.add(Restrictions.eq("birthDay", hotelTravelerQuery.getBirthDay()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getTelephone())){
			dc.add(Restrictions.eq("telephone", hotelTravelerQuery.getTelephone()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getRemark())){
			dc.add(Restrictions.eq("remark", hotelTravelerQuery.getRemark()));
		}
	   	if(hotelTravelerQuery.getSrcPrice()!=null){
	   		dc.add(Restrictions.eq("srcPrice", hotelTravelerQuery.getSrcPrice()));
	   	}
	   	if(hotelTravelerQuery.getSrcPriceCurrency()!=null){
	   		dc.add(Restrictions.eq("srcPriceCurrency", hotelTravelerQuery.getSrcPriceCurrency()));
	   	}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getOriginalPayPriceSerialNum())){
			dc.add(Restrictions.eq("originalPayPriceSerialNum", hotelTravelerQuery.getOriginalPayPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getCostPriceSerialNum())){
			dc.add(Restrictions.eq("costPriceSerialNum", hotelTravelerQuery.getCostPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getPayPriceSerialNum())){
			dc.add(Restrictions.eq("payPriceSerialNum", hotelTravelerQuery.getPayPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getRebatesMoneySerialNum())){
			dc.add(Restrictions.eq("rebatesMoneySerialNum", hotelTravelerQuery.getRebatesMoneySerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getJkSerialNum())){
			dc.add(Restrictions.eq("jkSerialNum", hotelTravelerQuery.getJkSerialNum()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getStatus())){
			dc.add(Restrictions.eq("status", hotelTravelerQuery.getStatus()));
		}
	   	if(hotelTravelerQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelTravelerQuery.getCreateBy()));
	   	}
		if(hotelTravelerQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelTravelerQuery.getCreateDate()));
		}
	   	if(hotelTravelerQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelTravelerQuery.getUpdateBy()));
	   	}
		if(hotelTravelerQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelTravelerQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelTravelerQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelTravelerDao.find(dc);
	}
	
	public HotelTraveler getByUuid(String uuid) {
		return hotelTravelerDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelTraveler hotelTraveler = getByUuid(uuid);
		hotelTraveler.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelTraveler);
	}

	/**
	 * @Description 查询游客
	 * @param isNormal 是否要查询正常游客（不包括已退团和已转团游客）
	 * @author yakun.bai
	 * @Date 2015-11-18
	 */
	@Override
	public List<HotelTraveler> findTravelerByOrderUuid(String orderUuid, boolean isNormal) {
		return hotelTravelerDao.findTravelerByOrderUuid(orderUuid, isNormal);
	}
}
