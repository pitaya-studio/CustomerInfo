/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.island.entity.*;
import com.trekiz.admin.modules.island.dao.*;
import com.trekiz.admin.modules.island.service.*;
import com.trekiz.admin.modules.island.input.*;
import com.trekiz.admin.modules.island.query.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class IslandTravelerServiceImpl  extends BaseService implements IslandTravelerService{
	@Autowired
	private IslandTravelerDao islandTravelerDao;
	@Autowired
	private IslandTravelervisaService islandTravelervisaService;
	@Autowired
	private IslandTravelerPapersTypeService islandTravelerPapersTypeService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private HotelAnnexService hotelAnnexService;

	public void save (IslandTraveler islandTraveler){
		super.setOptInfo(islandTraveler, BaseService.OPERATION_ADD);
		islandTravelerDao.saveObj(islandTraveler);
	}
	
	public void save (IslandTravelerInput islandTravelerInput){
		IslandTraveler islandTraveler = islandTravelerInput.getIslandTraveler();
		super.setOptInfo(islandTraveler, BaseService.OPERATION_ADD);
		islandTravelerDao.saveObj(islandTraveler);
	}
	
	public void update (IslandTraveler islandTraveler){
		super.setOptInfo(islandTraveler, BaseService.OPERATION_UPDATE);
		islandTravelerDao.updateObj(islandTraveler);
	}
	
	public IslandTraveler getById(java.lang.Integer value) {
		return islandTravelerDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		IslandTraveler obj = islandTravelerDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	

	public Page<IslandTraveler> find(Page<IslandTraveler> page, IslandTravelerQuery islandTravelerQuery) {
		DetachedCriteria dc = islandTravelerDao.createDetachedCriteria();
		
	   	if(islandTravelerQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandTravelerQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandTravelerQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", islandTravelerQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getName())){
			dc.add(Restrictions.eq("name", islandTravelerQuery.getName()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getNameSpell())){
			dc.add(Restrictions.eq("nameSpell", islandTravelerQuery.getNameSpell()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getPersonType())){
			dc.add(Restrictions.eq("personType", islandTravelerQuery.getPersonType()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getSpaceLevel())){
			dc.add(Restrictions.eq("spaceLevel", islandTravelerQuery.getSpaceLevel()));
		}
	   	if(islandTravelerQuery.getSex()!=null){
	   		dc.add(Restrictions.eq("sex", islandTravelerQuery.getSex()));
	   	}
	   	if(islandTravelerQuery.getNationality()!=null){
	   		dc.add(Restrictions.eq("nationality", islandTravelerQuery.getNationality()));
	   	}
		if(islandTravelerQuery.getBirthDay()!=null){
			dc.add(Restrictions.eq("birthDay", islandTravelerQuery.getBirthDay()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getTelephone())){
			dc.add(Restrictions.eq("telephone", islandTravelerQuery.getTelephone()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getRemark())){
			dc.add(Restrictions.eq("remark", islandTravelerQuery.getRemark()));
		}
	   	if(islandTravelerQuery.getSrcPrice()!=null){
	   		dc.add(Restrictions.eq("srcPrice", islandTravelerQuery.getSrcPrice()));
	   	}
	   	if(islandTravelerQuery.getSrcPriceCurrency()!=null){
	   		dc.add(Restrictions.eq("srcPriceCurrency", islandTravelerQuery.getSrcPriceCurrency()));
	   	}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getOriginalPayPriceSerialNum())){
			dc.add(Restrictions.eq("originalPayPriceSerialNum", islandTravelerQuery.getOriginalPayPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getCostPriceSerialNum())){
			dc.add(Restrictions.eq("costPriceSerialNum", islandTravelerQuery.getCostPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getPayPriceSerialNum())){
			dc.add(Restrictions.eq("payPriceSerialNum", islandTravelerQuery.getPayPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getRebatesMoneySerialNum())){
			dc.add(Restrictions.eq("rebatesMoneySerialNum", islandTravelerQuery.getRebatesMoneySerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getJkSerialNum())){
			dc.add(Restrictions.eq("jkSerialNum", islandTravelerQuery.getJkSerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getStatus())){
			dc.add(Restrictions.eq("status", islandTravelerQuery.getStatus()));
		}
	   	if(islandTravelerQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandTravelerQuery.getCreateBy()));
	   	}
		if(islandTravelerQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandTravelerQuery.getCreateDate()));
		}
	   	if(islandTravelerQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandTravelerQuery.getUpdateBy()));
	   	}
		if(islandTravelerQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandTravelerQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandTravelerQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandTravelerDao.find(page, dc);
	}
	
	public List<IslandTraveler> find( IslandTravelerQuery islandTravelerQuery) {
		DetachedCriteria dc = islandTravelerDao.createDetachedCriteria();
		
	   	if(islandTravelerQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandTravelerQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandTravelerQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", islandTravelerQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getName())){
			dc.add(Restrictions.eq("name", islandTravelerQuery.getName()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getNameSpell())){
			dc.add(Restrictions.eq("nameSpell", islandTravelerQuery.getNameSpell()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getPersonType())){
			dc.add(Restrictions.eq("personType", islandTravelerQuery.getPersonType()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getSpaceLevel())){
			dc.add(Restrictions.eq("spaceLevel", islandTravelerQuery.getSpaceLevel()));
		}
	   	if(islandTravelerQuery.getSex()!=null){
	   		dc.add(Restrictions.eq("sex", islandTravelerQuery.getSex()));
	   	}
	   	if(islandTravelerQuery.getNationality()!=null){
	   		dc.add(Restrictions.eq("nationality", islandTravelerQuery.getNationality()));
	   	}
		if(islandTravelerQuery.getBirthDay()!=null){
			dc.add(Restrictions.eq("birthDay", islandTravelerQuery.getBirthDay()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getTelephone())){
			dc.add(Restrictions.eq("telephone", islandTravelerQuery.getTelephone()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getRemark())){
			dc.add(Restrictions.eq("remark", islandTravelerQuery.getRemark()));
		}
	   	if(islandTravelerQuery.getSrcPrice()!=null){
	   		dc.add(Restrictions.eq("srcPrice", islandTravelerQuery.getSrcPrice()));
	   	}
	   	if(islandTravelerQuery.getSrcPriceCurrency()!=null){
	   		dc.add(Restrictions.eq("srcPriceCurrency", islandTravelerQuery.getSrcPriceCurrency()));
	   	}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getOriginalPayPriceSerialNum())){
			dc.add(Restrictions.eq("originalPayPriceSerialNum", islandTravelerQuery.getOriginalPayPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getCostPriceSerialNum())){
			dc.add(Restrictions.eq("costPriceSerialNum", islandTravelerQuery.getCostPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getPayPriceSerialNum())){
			dc.add(Restrictions.eq("payPriceSerialNum", islandTravelerQuery.getPayPriceSerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getRebatesMoneySerialNum())){
			dc.add(Restrictions.eq("rebatesMoneySerialNum", islandTravelerQuery.getRebatesMoneySerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getJkSerialNum())){
			dc.add(Restrictions.eq("jkSerialNum", islandTravelerQuery.getJkSerialNum()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getStatus())){
			dc.add(Restrictions.eq("status", islandTravelerQuery.getStatus()));
		}
	   	if(islandTravelerQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandTravelerQuery.getCreateBy()));
	   	}
		if(islandTravelerQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandTravelerQuery.getCreateDate()));
		}
	   	if(islandTravelerQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandTravelerQuery.getUpdateBy()));
	   	}
		if(islandTravelerQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandTravelerQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandTravelerQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandTravelerQuery.getDelFlag()));
		}

		List<IslandTraveler> travelList = islandTravelerDao.find(dc);
		if (StringUtils.isNotEmpty(islandTravelerQuery.getOrderUuid())){
			 travelList = addVisaPaper(islandTravelerQuery.getOrderUuid(),travelList);
		}
		//dc.addOrder(Order.desc("id"));
		return travelList;
	}
	
	public IslandTraveler getByUuid(String uuid) {
		return islandTravelerDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		IslandTraveler islandTraveler = getByUuid(uuid);
		islandTraveler.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(islandTraveler);
	}

	@Override
	public List<IslandTraveler> findTravelerByOrderUuid(String orderUuid) {
		
		List<IslandTraveler> travelerList = islandTravelerDao.findTravelerByOrderUuid(orderUuid, false);
		List<IslandTraveler> backList = addVisaPaper(orderUuid,travelerList);
		return backList;
	}
	/**
	 * 为游客增加签证信息、证件信息、附件信息、游客金额信息
	 * @author gao
	 * @param order
	 * @param travelList
	 */
	private List<IslandTraveler> addVisaPaper(String orderUuid,List<IslandTraveler> travelList){
		if(travelList!=null && !travelList.isEmpty()){
			// 遍历游客列表，将 签证信息、证件信息、附件信息、游客金额信息装入
			for(IslandTraveler travel : travelList){
				if(travel!=null){
					IslandTravelervisaQuery visaquery = new IslandTravelervisaQuery();
					visaquery.setIslandOrderUuid(orderUuid);
					visaquery.setIslandTravelerUuid(travel.getUuid());
					List<IslandTravelervisa> visaList = islandTravelervisaService.find(visaquery);
					// 写入签证信息
					travel.setIslandTravelervisaList(visaList);
					IslandTravelerPapersTypeQuery paper = new IslandTravelerPapersTypeQuery();
					paper.setIslandTravelerUuid(travel.getUuid());
					paper.setOrderUuid(orderUuid);
					List<IslandTravelerPapersType> paperList = islandTravelerPapersTypeService.find(paper);
					// 写入证件信息
					travel.setIslandTravelerPapersTypeList(paperList);
					HotelAnnex hotelAnnex = new HotelAnnex();
					hotelAnnex.setMainUuid(travel.getUuid());
					List<HotelAnnex> annexList = hotelAnnexService.find(hotelAnnex);
					// 写入附件信息
					travel.setIslandTravelerFilesList(annexList);
					IslandMoneyAmountQuery amount = new IslandMoneyAmountQuery();
					amount.setBusinessUuid(travel.getUuid());
					List<IslandMoneyAmount> amountList = islandMoneyAmountService.find(amount);
					travel.setIslandMoneyAmountList(amountList);
				}
			}
		}
		return travelList;
	}
}
