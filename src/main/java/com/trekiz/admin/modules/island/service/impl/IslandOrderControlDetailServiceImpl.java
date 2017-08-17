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
import com.trekiz.admin.modules.island.dao.IslandOrderControlDetailDao;
import com.trekiz.admin.modules.island.entity.IslandOrderControlDetail;
import com.trekiz.admin.modules.island.input.IslandOrderControlDetailInput;
import com.trekiz.admin.modules.island.query.IslandOrderControlDetailQuery;
import com.trekiz.admin.modules.island.service.IslandOrderControlDetailService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class IslandOrderControlDetailServiceImpl  extends BaseService implements IslandOrderControlDetailService{
	@Autowired
	private IslandOrderControlDetailDao islandOrderControlDetailDao;

	public void save (IslandOrderControlDetail islandOrderControlDetail){
		super.setOptInfo(islandOrderControlDetail, BaseService.OPERATION_ADD);
		islandOrderControlDetailDao.saveObj(islandOrderControlDetail);
	}
	
	public void save (IslandOrderControlDetailInput islandOrderControlDetailInput){
		IslandOrderControlDetail islandOrderControlDetail = islandOrderControlDetailInput.getIslandOrderControlDetail();
		super.setOptInfo(islandOrderControlDetail, BaseService.OPERATION_ADD);
		islandOrderControlDetailDao.saveObj(islandOrderControlDetail);
	}
	
	public void update (IslandOrderControlDetail islandOrderControlDetail){
		super.setOptInfo(islandOrderControlDetail, BaseService.OPERATION_UPDATE);
		islandOrderControlDetailDao.updateObj(islandOrderControlDetail);
	}
	
	public IslandOrderControlDetail getById(java.lang.Integer value) {
		return islandOrderControlDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		IslandOrderControlDetail obj = islandOrderControlDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<IslandOrderControlDetail> find(Page<IslandOrderControlDetail> page, IslandOrderControlDetailQuery islandOrderControlDetailQuery) {
		DetachedCriteria dc = islandOrderControlDetailDao.createDetachedCriteria();
		
	   	if(islandOrderControlDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandOrderControlDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderControlDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandOrderControlDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderControlDetailQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", islandOrderControlDetailQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderControlDetailQuery.getHotelControlDetailUuid())){
			dc.add(Restrictions.eq("hotelControlDetailUuid", islandOrderControlDetailQuery.getHotelControlDetailUuid()));
		}
	   	if(islandOrderControlDetailQuery.getNum()!=null){
	   		dc.add(Restrictions.eq("num", islandOrderControlDetailQuery.getNum()));
	   	}
	   	if(islandOrderControlDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandOrderControlDetailQuery.getCreateBy()));
	   	}
		if(islandOrderControlDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandOrderControlDetailQuery.getCreateDate()));
		}
	   	if(islandOrderControlDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandOrderControlDetailQuery.getUpdateBy()));
	   	}
		if(islandOrderControlDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandOrderControlDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandOrderControlDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandOrderControlDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandOrderControlDetailDao.find(page, dc);
	}
	
	public List<IslandOrderControlDetail> find( IslandOrderControlDetailQuery islandOrderControlDetailQuery) {
		DetachedCriteria dc = islandOrderControlDetailDao.createDetachedCriteria();
		
	   	if(islandOrderControlDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", islandOrderControlDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(islandOrderControlDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", islandOrderControlDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderControlDetailQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", islandOrderControlDetailQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(islandOrderControlDetailQuery.getHotelControlDetailUuid())){
			dc.add(Restrictions.eq("hotelControlDetailUuid", islandOrderControlDetailQuery.getHotelControlDetailUuid()));
		}
	   	if(islandOrderControlDetailQuery.getNum()!=null){
	   		dc.add(Restrictions.eq("num", islandOrderControlDetailQuery.getNum()));
	   	}
	   	if(islandOrderControlDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", islandOrderControlDetailQuery.getCreateBy()));
	   	}
		if(islandOrderControlDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", islandOrderControlDetailQuery.getCreateDate()));
		}
	   	if(islandOrderControlDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", islandOrderControlDetailQuery.getUpdateBy()));
	   	}
		if(islandOrderControlDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", islandOrderControlDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(islandOrderControlDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", islandOrderControlDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return islandOrderControlDetailDao.find(dc);
	}
	
	public IslandOrderControlDetail getByUuid(String uuid) {
		return islandOrderControlDetailDao.getByUuid(uuid);
	}
	
	public IslandOrderControlDetail getByOrderUuid(String orderUuid) {
		return islandOrderControlDetailDao.getByOrderUuid(orderUuid);
	}
	
	public void removeByUuid(String uuid) {
		IslandOrderControlDetail islandOrderControlDetail = getByUuid(uuid);
		islandOrderControlDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(islandOrderControlDetail);
	}
}
