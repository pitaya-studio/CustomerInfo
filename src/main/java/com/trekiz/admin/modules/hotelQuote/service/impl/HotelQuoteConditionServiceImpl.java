/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.service.impl;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotelQuote.dao.HotelQuoteConditionDao;
import com.trekiz.admin.modules.hotelQuote.entity.HotelQuoteCondition;
import com.trekiz.admin.modules.hotelQuote.input.HotelQuoteConditionInput;
import com.trekiz.admin.modules.hotelQuote.query.HotelQuoteConditionQuery;
import com.trekiz.admin.modules.hotelQuote.service.HotelQuoteConditionService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelQuoteConditionServiceImpl  extends BaseService implements HotelQuoteConditionService{
	@Autowired
	private HotelQuoteConditionDao hotelQuoteConditionDao;

	public void save (HotelQuoteCondition hotelQuoteCondition){
		super.setOptInfo(hotelQuoteCondition, BaseService.OPERATION_ADD);
		hotelQuoteConditionDao.saveObj(hotelQuoteCondition);
	}
	
	public void save (HotelQuoteConditionInput hotelQuoteConditionInput){
		HotelQuoteCondition hotelQuoteCondition = hotelQuoteConditionInput.getHotelQuoteCondition();
		super.setOptInfo(hotelQuoteCondition, BaseService.OPERATION_ADD);
		hotelQuoteConditionDao.saveObj(hotelQuoteCondition);
	}
	
	public void update (HotelQuoteCondition hotelQuoteCondition){
		super.setOptInfo(hotelQuoteCondition, BaseService.OPERATION_UPDATE);
		hotelQuoteConditionDao.updateObj(hotelQuoteCondition);
	}
	
	public HotelQuoteCondition getById(java.lang.Integer value) {
		return hotelQuoteConditionDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelQuoteCondition obj = hotelQuoteConditionDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelQuoteCondition> find(Page<HotelQuoteCondition> page, HotelQuoteConditionQuery hotelQuoteConditionQuery) {
		DetachedCriteria dc = hotelQuoteConditionDao.createDetachedCriteria();
		
	   	if(hotelQuoteConditionQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteConditionQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteConditionQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteConditionQuery.getHotelQuoteUuid()));
		}
	   	if(hotelQuoteConditionQuery.getPosition()!=null){
	   		dc.add(Restrictions.eq("position", hotelQuoteConditionQuery.getPosition()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getCountry())){
			dc.add(Restrictions.eq("country", hotelQuoteConditionQuery.getCountry()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuoteConditionQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuoteConditionQuery.getHotelUuid()));
		}
	   	if(hotelQuoteConditionQuery.getSupplierInfoId()!=null){
	   		dc.add(Restrictions.eq("supplierInfoId", hotelQuoteConditionQuery.getSupplierInfoId()));
	   	}
	   	if(hotelQuoteConditionQuery.getPurchaseType()!=null){
	   		dc.add(Restrictions.eq("purchaseType", hotelQuoteConditionQuery.getPurchaseType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getDepartureIslandWay())){
			dc.add(Restrictions.eq("departureIslandWay", hotelQuoteConditionQuery.getDepartureIslandWay()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getArrivalIslandWay())){
			dc.add(Restrictions.eq("arrivalIslandWay", hotelQuoteConditionQuery.getArrivalIslandWay()));
		}
	   	if(hotelQuoteConditionQuery.getRoomNum()!=null){
	   		dc.add(Restrictions.eq("roomNum", hotelQuoteConditionQuery.getRoomNum()));
	   	}
	   	if(hotelQuoteConditionQuery.getMixliveNum()!=null){
	   		dc.add(Restrictions.eq("mixliveNum", hotelQuoteConditionQuery.getMixliveNum()));
	   	}
	   	if(hotelQuoteConditionQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelQuoteConditionQuery.getSort()));
	   	}
	   	if(hotelQuoteConditionQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteConditionQuery.getCreateBy()));
	   	}
		if(hotelQuoteConditionQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteConditionQuery.getCreateDate()));
		}
	   	if(hotelQuoteConditionQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteConditionQuery.getUpdateBy()));
	   	}
		if(hotelQuoteConditionQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteConditionQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteConditionQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteConditionDao.find(page, dc);
	}
	
	public List<HotelQuoteCondition> find( HotelQuoteConditionQuery hotelQuoteConditionQuery) {
		DetachedCriteria dc = hotelQuoteConditionDao.createDetachedCriteria();
		
	   	if(hotelQuoteConditionQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelQuoteConditionQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", hotelQuoteConditionQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getHotelQuoteUuid())){
			dc.add(Restrictions.eq("hotelQuoteUuid", hotelQuoteConditionQuery.getHotelQuoteUuid()));
		}
	   	if(hotelQuoteConditionQuery.getPosition()!=null){
	   		dc.add(Restrictions.eq("position", hotelQuoteConditionQuery.getPosition()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getCountry())){
			dc.add(Restrictions.eq("country", hotelQuoteConditionQuery.getCountry()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getIslandUuid())){
			dc.add(Restrictions.eq("islandUuid", hotelQuoteConditionQuery.getIslandUuid()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getHotelUuid())){
			dc.add(Restrictions.eq("hotelUuid", hotelQuoteConditionQuery.getHotelUuid()));
		}
	   	if(hotelQuoteConditionQuery.getSupplierInfoId()!=null){
	   		dc.add(Restrictions.eq("supplierInfoId", hotelQuoteConditionQuery.getSupplierInfoId()));
	   	}
	   	if(hotelQuoteConditionQuery.getPurchaseType()!=null){
	   		dc.add(Restrictions.eq("purchaseType", hotelQuoteConditionQuery.getPurchaseType()));
	   	}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getDepartureIslandWay())){
			dc.add(Restrictions.eq("departureIslandWay", hotelQuoteConditionQuery.getDepartureIslandWay()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getArrivalIslandWay())){
			dc.add(Restrictions.eq("arrivalIslandWay", hotelQuoteConditionQuery.getArrivalIslandWay()));
		}
	   	if(hotelQuoteConditionQuery.getRoomNum()!=null){
	   		dc.add(Restrictions.eq("roomNum", hotelQuoteConditionQuery.getRoomNum()));
	   	}
	   	if(hotelQuoteConditionQuery.getMixliveNum()!=null){
	   		dc.add(Restrictions.eq("mixliveNum", hotelQuoteConditionQuery.getMixliveNum()));
	   	}
	   	if(hotelQuoteConditionQuery.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelQuoteConditionQuery.getSort()));
	   	}
	   	if(hotelQuoteConditionQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelQuoteConditionQuery.getCreateBy()));
	   	}
		if(hotelQuoteConditionQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelQuoteConditionQuery.getCreateDate()));
		}
	   	if(hotelQuoteConditionQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelQuoteConditionQuery.getUpdateBy()));
	   	}
		if(hotelQuoteConditionQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelQuoteConditionQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelQuoteConditionQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", hotelQuoteConditionQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelQuoteConditionDao.find(dc);
	}
	
	public HotelQuoteCondition getByUuid(String uuid) {
		return hotelQuoteConditionDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelQuoteCondition hotelQuoteCondition = getByUuid(uuid);
		hotelQuoteCondition.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelQuoteCondition);
	}

	@Override
	public HotelQuoteCondition findByHotelQuoteUuid(String uuid) {
		String qlString = " from HotelQuoteCondition where delFlag='0' and hotelQuoteUuid=? ";
		List<HotelQuoteCondition> hotelQuoteConditionList=hotelQuoteConditionDao.find(qlString, uuid);
		if(CollectionUtils.isNotEmpty(hotelQuoteConditionList)){
			return hotelQuoteConditionList.get(0);
		}
		return new HotelQuoteCondition();
	}

	@Override
	public List<HotelQuoteCondition> findByQuoteUuid(String uuid) {
		String qlString = " from HotelQuoteCondition where delFlag='0' and hotelQuoteUuid=? ";
		return hotelQuoteConditionDao.find(qlString, uuid);
	}
	
	@Override
	public List<HotelQuoteCondition> findByQuoteConditionUuid(String uuid) {
		String qlString = " from HotelQuoteCondition where delFlag='0' and uuid=? ";
		return hotelQuoteConditionDao.find(qlString, uuid);
	}
}
