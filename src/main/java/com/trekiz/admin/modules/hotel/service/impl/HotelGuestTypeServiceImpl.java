/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.HotelGuestTypeDao;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotel.service.HotelGuestTypeService;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelGuestTypeServiceImpl  extends BaseService implements HotelGuestTypeService{
	@Autowired
	private HotelGuestTypeDao hotelGuestTypeDao;

	public void save (HotelGuestType hotelGuestType){
		super.setOptInfo(hotelGuestType, BaseService.OPERATION_ADD);
		hotelGuestTypeDao.saveObj(hotelGuestType);
	}
	
	public void update (HotelGuestType hotelGuestType){
		super.setOptInfo(hotelGuestType, BaseService.OPERATION_UPDATE);
		hotelGuestTypeDao.updateObj(hotelGuestType);
	}
	
	public HotelGuestType getById(java.lang.Integer value) {
		return hotelGuestTypeDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelGuestType obj = hotelGuestTypeDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelGuestType> find(Page<HotelGuestType> page, HotelGuestType hotelGuestType) {
		DetachedCriteria dc = hotelGuestTypeDao.createDetachedCriteria();
		
	   	if(hotelGuestType.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelGuestType.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelGuestType.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelGuestType.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelGuestType.getName())){
			dc.add(Restrictions.like("name", "%"+hotelGuestType.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelGuestType.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotelGuestType.getDescription()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelGuestType.getStatus())){
			dc.add(Restrictions.like("status", "%"+hotelGuestType.getStatus()+"%"));
		}
	   	if(hotelGuestType.getUseRange()!=null){
	   		dc.add(Restrictions.eq("useRange", hotelGuestType.getUseRange()));
	   	}
	   	if(hotelGuestType.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelGuestType.getSort()));
	   	}
	   	if(hotelGuestType.getWholesalerId() != null) {
	   		dc.add(Restrictions.eq("wholesalerId", hotelGuestType.getWholesalerId()));
	   	}
	   	if(hotelGuestType.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelGuestType.getCreateBy()));
	   	}
		if(hotelGuestType.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelGuestType.getCreateDate()));
		}
	   	if(hotelGuestType.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelGuestType.getUpdateBy()));
	   	}
		if(hotelGuestType.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelGuestType.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelGuestType.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelGuestType.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return hotelGuestTypeDao.find(page, dc);
	}
	
	public List<HotelGuestType> find( HotelGuestType hotelGuestType) {
		DetachedCriteria dc = hotelGuestTypeDao.createDetachedCriteria();
		
	   	if(hotelGuestType.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelGuestType.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelGuestType.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelGuestType.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelGuestType.getName())){
			dc.add(Restrictions.like("name", "%"+hotelGuestType.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelGuestType.getDescription())){
			dc.add(Restrictions.like("description", "%"+hotelGuestType.getDescription()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelGuestType.getStatus())){
			dc.add(Restrictions.like("status", "%"+hotelGuestType.getStatus()+"%"));
		}
	   	if(hotelGuestType.getUseRange()!=null){
	   		dc.add(Restrictions.eq("useRange", hotelGuestType.getUseRange()));
	   	}
	   	if(hotelGuestType.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", hotelGuestType.getSort()));
	   	}
	   	if(hotelGuestType.getWholesalerId() != null) {
	   		dc.add(Restrictions.eq("wholesalerId", hotelGuestType.getWholesalerId()));
	   	}
	   	if(hotelGuestType.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelGuestType.getCreateBy()));
	   	}
		if(hotelGuestType.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelGuestType.getCreateDate()));
		}
	   	if(hotelGuestType.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelGuestType.getUpdateBy()));
	   	}
		if(hotelGuestType.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelGuestType.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelGuestType.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelGuestType.getDelFlag()+"%"));
		}
		
		dc.addOrder(Order.asc("sort"));
		return hotelGuestTypeDao.find(dc);
	}
	
	public HotelGuestType getByUuid(String uuid) {
		return hotelGuestTypeDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		HotelGuestType hotelGuestType = getByUuid(uuid);
		hotelGuestType.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelGuestType);
	}
	
	public boolean findIsExist(String uuid, String name, Long wholesalerId) {
		StringBuffer sb = new StringBuffer("from HotelGuestType hotelGuestType where hotelGuestType.uuid != ? and hotelGuestType.name = ? and hotelGuestType.wholesalerId = ? and hotelGuestType.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		
		List<HotelGuestType> hotelGuestTypes = hotelGuestTypeDao.find(sb.toString(), uuid, name, wholesalerId.intValue());
		
		if(hotelGuestTypes == null || hotelGuestTypes.size() == 0) {
			return false;
		}
		return true;
	}
	
	public boolean findIsExistBySysGuestType(String uuid, String sysGuestType, int companyId) {
		StringBuffer sb = new StringBuffer("from HotelGuestType hotelGuestType where hotelGuestType.uuid != ? and hotelGuestType.sysGuestType = ? and hotelGuestType.wholesalerId = ? and hotelGuestType.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		
		List<TravelerType> travelerTypes = hotelGuestTypeDao.find(sb.toString(), uuid, sysGuestType, companyId);
		
		if(travelerTypes == null || travelerTypes.size() == 0) {
			return false;
		}
		return true;
	}
	@Override
	public List<HotelGuestType> findByWholesalerId(Integer wholesalerId) {
		// TODO Auto-generated method stub
		return hotelGuestTypeDao.findByWholesalerId(wholesalerId);
	}
	
	/**
	 * 查询批发商下 第N人类型 的游客类型简写和批发商住客类型UUID的集合 add by zhanghao
	 * 自动报价模块使用
	 * @param wholesalerId
	 * @return List<HotelGuestType> 集合对象中的uuid是hotel_guest_type表中的uuid，简写来源系统游客类型的简写
	 * 			
	 */
	public List<HotelGuestType> findShortNameAndGuestTypeUuidList(Integer wholesalerId,List<String> shortNameParams){
		return hotelGuestTypeDao.findShortNameAndGuestTypeUuidList(wholesalerId,shortNameParams);
	}
	
	/**
	 * 根据批发商将酒店住客类型sort进行累加操作
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 下午12:02:17
	 */
	public boolean cumulationSortByWholesalerId(Integer wholesalerId) {
		List<HotelGuestType> hotelGuestTypes = hotelGuestTypeDao.findAllByWholesalerId(wholesalerId);
		if(CollectionUtils.isEmpty(hotelGuestTypes)) {
			return false;
		}
		
		//待排序的sort集合
		List<Integer> sortList = new ArrayList<Integer>();
		for(HotelGuestType hotelGuestType : hotelGuestTypes) {
			sortList.add(hotelGuestType.getSort());
		}
		
		//排序后的sort集合
		List<Integer> sortedList = NumberUtils.cumulationNumber(sortList);
		if(CollectionUtils.isEmpty(sortedList)) {
			return false;
		}
		
		//修改酒店住客集合的sort属性
		for(int i=0; i<hotelGuestTypes.size(); i++) {
			HotelGuestType hotelGuestType = hotelGuestTypes.get(i);
			hotelGuestType.setSort(sortedList.get(i));
			super.setOptInfo(hotelGuestType, OPERATION_UPDATE);
		}
		hotelGuestTypeDao.batchUpdate(hotelGuestTypes);
		
		return true;
	}
	
}
