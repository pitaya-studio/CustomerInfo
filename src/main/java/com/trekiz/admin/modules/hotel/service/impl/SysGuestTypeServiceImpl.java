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
import com.trekiz.admin.modules.hotel.dao.SysGuestTypeDao;
import com.trekiz.admin.modules.hotel.entity.SysGuestType;
import com.trekiz.admin.modules.hotel.input.SysGuestTypeInput;
import com.trekiz.admin.modules.hotel.query.SysGuestTypeQuery;
import com.trekiz.admin.modules.hotel.service.SysGuestTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysGuestTypeServiceImpl  extends BaseService implements SysGuestTypeService{
	@Autowired
	private SysGuestTypeDao sysGuestTypeDao;

	public void save (SysGuestType sysGuestType){
		super.setOptInfo(sysGuestType, BaseService.OPERATION_ADD);
		sysGuestTypeDao.saveObj(sysGuestType);
	}
	
	public void save (SysGuestTypeInput sysGuestTypeInput){
		SysGuestType sysGuestType = sysGuestTypeInput.getSysGuestType();
		super.setOptInfo(sysGuestType, BaseService.OPERATION_ADD);
		sysGuestTypeDao.saveObj(sysGuestType);
	}
	
	public void update (SysGuestType sysGuestType){
		super.setOptInfo(sysGuestType, BaseService.OPERATION_UPDATE);
		sysGuestTypeDao.updateObj(sysGuestType);
	}
	
	public SysGuestType getById(java.lang.Integer value) {
		return sysGuestTypeDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		SysGuestType obj = sysGuestTypeDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<SysGuestType> find(Page<SysGuestType> page, SysGuestTypeQuery sysGuestTypeQuery) {
		DetachedCriteria dc = sysGuestTypeDao.createDetachedCriteria();
		
	   	if(sysGuestTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysGuestTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysGuestTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", sysGuestTypeQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(sysGuestTypeQuery.getName())){
			dc.add(Restrictions.like("name", "%"+sysGuestTypeQuery.getName()+"%"));
		}
	   	if(sysGuestTypeQuery.getValue()!=null){
	   		dc.add(Restrictions.eq("value", sysGuestTypeQuery.getValue()));
	   	}
	   	if(sysGuestTypeQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", sysGuestTypeQuery.getType()));
	   	}
		if (StringUtils.isNotEmpty(sysGuestTypeQuery.getDescription())){
			dc.add(Restrictions.eq("description", sysGuestTypeQuery.getDescription()));
		}
	   	if(sysGuestTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysGuestTypeQuery.getCreateBy()));
	   	}
		if(sysGuestTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysGuestTypeQuery.getCreateDate()));
		}
	   	if(sysGuestTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysGuestTypeQuery.getUpdateBy()));
	   	}
		if(sysGuestTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysGuestTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysGuestTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", sysGuestTypeQuery.getDelFlag()));
		}
	   	if(sysGuestTypeQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", sysGuestTypeQuery.getWholesalerId()));
	   	}
	   	if(sysGuestTypeQuery.getPersonType()!=null){
	   		dc.add(Restrictions.eq("personType", sysGuestTypeQuery.getPersonType()));
	   	}
	   	
		
		//dc.addOrder(Order.desc("id"));
		return sysGuestTypeDao.find(page, dc);
	}
	
	public List<SysGuestType> find( SysGuestTypeQuery sysGuestTypeQuery) {
		DetachedCriteria dc = sysGuestTypeDao.createDetachedCriteria();
		
	   	if(sysGuestTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysGuestTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysGuestTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", sysGuestTypeQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(sysGuestTypeQuery.getName())){
			dc.add(Restrictions.eq("name", sysGuestTypeQuery.getName()));
		}
	   	if(sysGuestTypeQuery.getValue()!=null){
	   		dc.add(Restrictions.eq("value", sysGuestTypeQuery.getValue()));
	   	}
	   	if(sysGuestTypeQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", sysGuestTypeQuery.getType()));
	   	}
		if (StringUtils.isNotEmpty(sysGuestTypeQuery.getDescription())){
			dc.add(Restrictions.eq("description", sysGuestTypeQuery.getDescription()));
		}
	   	if(sysGuestTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysGuestTypeQuery.getCreateBy()));
	   	}
		if(sysGuestTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysGuestTypeQuery.getCreateDate()));
		}
	   	if(sysGuestTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysGuestTypeQuery.getUpdateBy()));
	   	}
		if(sysGuestTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysGuestTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysGuestTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", sysGuestTypeQuery.getDelFlag()));
		}
	   	if(sysGuestTypeQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", sysGuestTypeQuery.getWholesalerId()));
	   	}
	   	if(sysGuestTypeQuery.getPersonType()!=null){
	   		dc.add(Restrictions.eq("personType", sysGuestTypeQuery.getPersonType()));
	   	}
		
		//dc.addOrder(Order.desc("id"));
		return sysGuestTypeDao.find(dc);
	}
	
	public SysGuestType getByUuid(String uuid) {
		return sysGuestTypeDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		SysGuestType sysGuestType = getByUuid(uuid);
		sysGuestType.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(sysGuestType);
	}
	
	/**
	 * 查询批发商下所有的住客类型集合 add by zhanghao
	 * 自动报价模块使用
	 * @param wholesalerId
	 * @return List<SysGuestType> 集合对象中的uuid是hotel_guest_type表中的uuid，其余属性来源于sys_guest_type表
	 * 			
	 */
	public List<SysGuestType> findAllListByCompanyIdAndHotelUuid(int wholesalerId ,String hotelUuid){
		return sysGuestTypeDao.findAllListByCompanyIdAndHotelUuid(wholesalerId,hotelUuid);
	}
	
}
