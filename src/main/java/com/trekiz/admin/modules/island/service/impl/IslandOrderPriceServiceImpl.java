/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service.impl;

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
import com.trekiz.admin.modules.island.dao.IslandOrderPriceDao;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;
import com.trekiz.admin.modules.island.input.IslandOrderPriceInput;
import com.trekiz.admin.modules.island.query.IslandOrderPriceQuery;
import com.trekiz.admin.modules.island.service.IslandOrderPriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class IslandOrderPriceServiceImpl  extends BaseService implements IslandOrderPriceService{
	@Autowired
	private IslandOrderPriceDao islandOrderPriceDao;

	public void save (IslandOrderPrice islandOrderPrice){
		super.setOptInfo(islandOrderPrice, BaseService.OPERATION_ADD);
		islandOrderPriceDao.saveObj(islandOrderPrice);
	}
	
	public void save (IslandOrderPriceInput islandOrderPriceInput){
		IslandOrderPrice islandOrderPrice = islandOrderPriceInput.getIslandOrderPrice();
		super.setOptInfo(islandOrderPrice, BaseService.OPERATION_ADD);
		islandOrderPriceDao.saveObj(islandOrderPrice);
	}
	
	public void update (IslandOrderPrice islandOrderPrice){
		super.setOptInfo(islandOrderPrice, BaseService.OPERATION_UPDATE);
		islandOrderPriceDao.updateObj(islandOrderPrice);
	}
	
	public IslandOrderPrice getById(java.lang.Integer value) {
		return islandOrderPriceDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		IslandOrderPrice obj = islandOrderPriceDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<IslandOrderPrice> find(Page<IslandOrderPrice> page, IslandOrderPriceQuery islandOrderPriceQuery) {
		DetachedCriteria dc = islandOrderPriceDao.createDetachedCriteria();
		
	   	if(islandOrderPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandOrderPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandOrderPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", islandOrderPriceQuery.getOrderUuid()));
		}
	   	if(islandOrderPriceQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", islandOrderPriceQuery.getPriceType()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getPriceName())){
			dc.add(Restrictions.eq("priceName", islandOrderPriceQuery.getPriceName()));
		}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getActivityIslandGroupPriceUuid())){
			dc.add(Restrictions.eq("activityIslandGroupPriceUuid", islandOrderPriceQuery.getActivityIslandGroupPriceUuid()));
		}
	   	if(islandOrderPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", islandOrderPriceQuery.getCurrencyId()));
	   	}
	   	if(islandOrderPriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", islandOrderPriceQuery.getPrice()));
	   	}
	   	if(islandOrderPriceQuery.getNum()!=null){
	   		dc.add(Restrictions.eq("num", islandOrderPriceQuery.getNum()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getRemark())){
			dc.add(Restrictions.eq("remark", islandOrderPriceQuery.getRemark()));
		}
	   	if(islandOrderPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandOrderPriceQuery.getCreateBy()));
	   	}
		if(islandOrderPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandOrderPriceQuery.getCreateDate()));
		}
	   	if(islandOrderPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandOrderPriceQuery.getUpdateBy()));
	   	}
		if(islandOrderPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandOrderPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandOrderPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandOrderPriceDao.find(page, dc);
	}
	
	public List<IslandOrderPrice> find( IslandOrderPriceQuery islandOrderPriceQuery) {
		DetachedCriteria dc = islandOrderPriceDao.createDetachedCriteria();
		
	   	if(islandOrderPriceQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandOrderPriceQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandOrderPriceQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", islandOrderPriceQuery.getOrderUuid()));
		}
	   	if(islandOrderPriceQuery.getPriceType()!=null){
	   		dc.add(Restrictions.eq("priceType", islandOrderPriceQuery.getPriceType()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getPriceName())){
			dc.add(Restrictions.eq("priceName", islandOrderPriceQuery.getPriceName()));
		}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getActivityIslandGroupPriceUuid())){
			dc.add(Restrictions.eq("activityIslandGroupPriceUuid", islandOrderPriceQuery.getActivityIslandGroupPriceUuid()));
		}
	   	if(islandOrderPriceQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", islandOrderPriceQuery.getCurrencyId()));
	   	}
	   	if(islandOrderPriceQuery.getPrice()!=null){
	   		dc.add(Restrictions.eq("price", islandOrderPriceQuery.getPrice()));
	   	}
	   	if(islandOrderPriceQuery.getNum()!=null){
	   		dc.add(Restrictions.eq("num", islandOrderPriceQuery.getNum()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getRemark())){
			dc.add(Restrictions.eq("remark", islandOrderPriceQuery.getRemark()));
		}
	   	if(islandOrderPriceQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandOrderPriceQuery.getCreateBy()));
	   	}
		if(islandOrderPriceQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandOrderPriceQuery.getCreateDate()));
		}
	   	if(islandOrderPriceQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandOrderPriceQuery.getUpdateBy()));
	   	}
		if(islandOrderPriceQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandOrderPriceQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandOrderPriceQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandOrderPriceQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandOrderPriceDao.find(dc);
	}
	
	public IslandOrderPrice getByUuid(String uuid) {
		return islandOrderPriceDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		IslandOrderPrice islandOrderPrice = getByUuid(uuid);
		islandOrderPrice.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(islandOrderPrice);
	}
	
	public IslandOrderPrice getByOrderUuidAndGroupPriceUuid(String orderUuid,String groupPriceUuid) {
		return islandOrderPriceDao.getByOrderUuidAndGroupPriceUuid(orderUuid,groupPriceUuid);
	}
	
	public List<IslandOrderPrice> getByOrderUuid(String orderUuid){
		return islandOrderPriceDao.getByOrderUuid(orderUuid);
	}
	
	/**
	 * 根据订单uuid获取订单价格信息
	 * @param groupUuid
	 * @return
	 */
	public List<IslandOrderPrice> getOrderPriceByOrderUuid(String orderUuid){
		return islandOrderPriceDao.find("from IslandOrderPrice where orderUuid=? and delFlag=?",orderUuid,BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
