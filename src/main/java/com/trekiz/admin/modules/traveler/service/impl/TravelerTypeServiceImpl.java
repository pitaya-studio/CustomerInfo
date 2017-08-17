/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.traveler.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.util.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.NumberUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.traveler.dao.TravelerTypeDao;
import com.trekiz.admin.modules.traveler.entity.TravelerType;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class TravelerTypeServiceImpl  extends BaseService implements TravelerTypeService{
	@Autowired
	private TravelerTypeDao travelerTypeDao;

	public void save (TravelerType travelerType){
		super.setOptInfo(travelerType, BaseService.OPERATION_ADD);
		travelerTypeDao.saveObj(travelerType);
	}
	
	public void update (TravelerType travelerType){
		super.setOptInfo(travelerType, BaseService.OPERATION_UPDATE);
		travelerTypeDao.updateObj(travelerType);
	}
	
	public TravelerType getById(java.lang.Integer value) {
		return travelerTypeDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		TravelerType obj = travelerTypeDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<TravelerType> find(Page<TravelerType> page, TravelerType travelerType) {
		DetachedCriteria dc = travelerTypeDao.createDetachedCriteria();
		
	   	if(travelerType.getId()!=null){
	   		dc.add(Restrictions.eq("id", travelerType.getId()));
	   	}
		if (StringUtils.isNotEmpty(travelerType.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+travelerType.getUuid()+"%"));
		}
	   	if(travelerType.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", travelerType.getSort()));
	   	}
		if (StringUtils.isNotEmpty(travelerType.getName())){
			dc.add(Restrictions.like("name", "%"+travelerType.getName()+"%"));
		}
	   	if(travelerType.getRangeFrom()!=null){
	   		dc.add(Restrictions.eq("rangeFrom", travelerType.getRangeFrom()));
	   	}
	   	if(travelerType.getRangeTo()!=null){
	   		dc.add(Restrictions.eq("rangeTo", travelerType.getRangeTo()));
	   	}
		if (StringUtils.isNotEmpty(travelerType.getSysTravelerType())){
			dc.add(Restrictions.like("sysTravelerType", "%"+travelerType.getSysTravelerType()+"%"));
		}
		if (StringUtils.isNotEmpty(travelerType.getStatus())){
			dc.add(Restrictions.like("status", "%"+travelerType.getStatus()+"%"));
		}
		if (StringUtils.isNotEmpty(travelerType.getDescription())){
			dc.add(Restrictions.like("description", "%"+travelerType.getDescription()+"%"));
		}
		if (StringUtils.isNotEmpty(travelerType.getShortName())){
			dc.add(Restrictions.like("shortName", "%"+travelerType.getShortName()+"%"));
		}
		if (travelerType.getPersonType()!=null){
			dc.add(Restrictions.eq("personType", travelerType.getPersonType()));
		}
	   	if(travelerType.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", travelerType.getWholesalerId()));
	   	}
	   	if(travelerType.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", travelerType.getCreateBy()));
	   	}
		if(travelerType.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", travelerType.getCreateDate()));
		}
	   	if(travelerType.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", travelerType.getUpdateBy()));
	   	}
		if(travelerType.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", travelerType.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(travelerType.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+travelerType.getDelFlag()+"%"));
		}
		if(StringUtils.isNotEmpty(travelerType.getApplyProduct())){
			dc.add(Restrictions.eq("applyProduct", travelerType.getApplyProduct()));
		}

		dc.addOrder(Order.asc("sort"));
		return travelerTypeDao.find(page, dc);
	}
	
	public List<TravelerType> find( TravelerType travelerType) {
		DetachedCriteria dc = travelerTypeDao.createDetachedCriteria();
		
	   	if(travelerType.getId()!=null){
	   		dc.add(Restrictions.eq("id", travelerType.getId()));
	   	}
		if (StringUtils.isNotEmpty(travelerType.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+travelerType.getUuid()+"%"));
		}
	   	if(travelerType.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", travelerType.getSort()));
	   	}
		if (StringUtils.isNotEmpty(travelerType.getName())){
			dc.add(Restrictions.like("name", "%"+travelerType.getName()+"%"));
		}
	   	if(travelerType.getRangeFrom()!=null){
	   		dc.add(Restrictions.eq("rangeFrom", travelerType.getRangeFrom()));
	   	}
	   	if(travelerType.getRangeTo()!=null){
	   		dc.add(Restrictions.eq("rangeTo", travelerType.getRangeTo()));
	   	}
		if (StringUtils.isNotEmpty(travelerType.getSysTravelerType())){
			dc.add(Restrictions.like("sysTravelerType", "%"+travelerType.getSysTravelerType()+"%"));
		}
		if (StringUtils.isNotEmpty(travelerType.getStatus())){
			dc.add(Restrictions.like("status", "%"+travelerType.getStatus()+"%"));
		}
		if (StringUtils.isNotEmpty(travelerType.getDescription())){
			dc.add(Restrictions.like("description", "%"+travelerType.getDescription()+"%"));
		}
		if (StringUtils.isNotEmpty(travelerType.getShortName())){
			dc.add(Restrictions.like("shortName", "%"+travelerType.getShortName()+"%"));
		}
		if (travelerType.getPersonType()!=null){
			dc.add(Restrictions.eq("personType", travelerType.getPersonType()));
		}
	   	if(travelerType.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", travelerType.getWholesalerId()));
	   	}
	   	if(travelerType.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", travelerType.getCreateBy()));
	   	}
		if(travelerType.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", travelerType.getCreateDate()));
		}
	   	if(travelerType.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", travelerType.getUpdateBy()));
	   	}
		if(travelerType.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", travelerType.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(travelerType.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+travelerType.getDelFlag()+"%"));
		}
		if(StringUtils.isNotEmpty(travelerType.getApplyProduct())){
			dc.add(Restrictions.eq("applyProduct", travelerType.getApplyProduct()));
		}
		dc.addOrder(Order.asc("sort"));
		return travelerTypeDao.find(dc);
	}
	
	public TravelerType getByUuid(String uuid) {
		return travelerTypeDao.getByUuid(uuid);
	}
	@Override
	public TravelerType getTravelerName(String uuid) {
		return travelerTypeDao.getTravelerName(uuid);
	}
	
	public void removeByUuid(String uuid) {
		TravelerType travelerType = getByUuid(uuid);
		travelerType.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(travelerType);
	}
	
	public boolean findIsExist(String uuid, String name, Long wholesalerId) {
		StringBuffer sb = new StringBuffer("from TravelerType travelerType where travelerType.uuid != ? and travelerType.name = ? and travelerType.wholesalerId = ? and travelerType.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		
		List<TravelerType> travelerTypes = travelerTypeDao.find(sb.toString(), uuid, name, wholesalerId.intValue());
		
		if(travelerTypes == null || travelerTypes.size() == 0) {
			return false;
		}
		return true;
	}
	/**
	 * UUid="",false 表示不包含 ,true 表示包含
	 */
	public boolean findIsExistBySysTravelerType(String uuid, String sysTravelerType, int companyId) {
		StringBuffer sb = new StringBuffer();
		sb.append("from TravelerType travelerType where travelerType.uuid != ? and travelerType.sysTravelerType = ? and travelerType.wholesalerId = ? and travelerType.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		
		List<TravelerType> travelerTypes = travelerTypeDao.find(sb.toString(), uuid, sysTravelerType, companyId);
		
		if(travelerTypes == null || travelerTypes.size() == 0) {
			return false;
		}
		return true;
	}
	
	public List<TravelerType> getTravelerTypesByWholesalerId(Integer wholesalerId) {
		return travelerTypeDao.getTravelerTypesByWholesalerId(wholesalerId);
	}
	
	/**
	 * 根据批发商将游客类型sort进行累加操作
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 下午1:48:25
	 */
	public boolean cumulationSortByWholesalerId(Integer wholesalerId) {
		List<TravelerType> travelerTypes = travelerTypeDao.findAllByWholesalerId(wholesalerId);
		
		if(CollectionUtils.isEmpty(travelerTypes)) {
			return false;
		}
		
		//待排序的sort集合
		List<Integer> sortList = new ArrayList<Integer>();
		for(TravelerType travelerType : travelerTypes) {
			sortList.add(travelerType.getSort());
		}
		
		//排序后的sort集合
		List<Integer> sortedList = NumberUtils.cumulationNumber(sortList);
		if(CollectionUtils.isEmpty(sortedList)) {
			return false;
		}
		
		//修改游客类型集合的sort属性
		for(int i=0; i<travelerTypes.size(); i++) {
			TravelerType travelerType = travelerTypes.get(i);
			travelerType.setSort(sortedList.get(i));
			super.setOptInfo(travelerType, OPERATION_UPDATE);
		}
		travelerTypeDao.batchUpdate(travelerTypes);
		
		return true;
	}
}
