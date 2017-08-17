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
import com.trekiz.admin.modules.hotel.dao.SysGuestTravelerTypeRelDao;
import com.trekiz.admin.modules.hotel.entity.SysGuestTravelerTypeRel;
import com.trekiz.admin.modules.hotel.input.SysGuestTravelerTypeRelInput;
import com.trekiz.admin.modules.hotel.query.SysGuestTravelerTypeRelQuery;
import com.trekiz.admin.modules.hotel.service.SysGuestTravelerTypeRelService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysGuestTravelerTypeRelServiceImpl  extends BaseService implements SysGuestTravelerTypeRelService{
	@Autowired
	private SysGuestTravelerTypeRelDao sysGuestTravelerTypeRelDao;

	public void save (SysGuestTravelerTypeRel sysGuestTravelerTypeRel){
		super.setOptInfo(sysGuestTravelerTypeRel, BaseService.OPERATION_ADD);
		sysGuestTravelerTypeRelDao.saveObj(sysGuestTravelerTypeRel);
	}
	
	public void save (SysGuestTravelerTypeRelInput sysGuestTravelerTypeRelInput){
		SysGuestTravelerTypeRel sysGuestTravelerTypeRel = sysGuestTravelerTypeRelInput.getSysGuestTravelerTypeRel();
		super.setOptInfo(sysGuestTravelerTypeRel, BaseService.OPERATION_ADD);
		sysGuestTravelerTypeRelDao.saveObj(sysGuestTravelerTypeRel);
	}
	
	public void update (SysGuestTravelerTypeRel sysGuestTravelerTypeRel){
		super.setOptInfo(sysGuestTravelerTypeRel, BaseService.OPERATION_UPDATE);
		sysGuestTravelerTypeRelDao.updateObj(sysGuestTravelerTypeRel);
	}
	
	public SysGuestTravelerTypeRel getById(java.lang.Integer value) {
		return sysGuestTravelerTypeRelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		SysGuestTravelerTypeRel obj = sysGuestTravelerTypeRelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<SysGuestTravelerTypeRel> find(Page<SysGuestTravelerTypeRel> page, SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery) {
		DetachedCriteria dc = sysGuestTravelerTypeRelDao.createDetachedCriteria();
		
	   	if(sysGuestTravelerTypeRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysGuestTravelerTypeRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysGuestTravelerTypeRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", sysGuestTravelerTypeRelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(sysGuestTravelerTypeRelQuery.getSysGuestTypeUuid())){
			dc.add(Restrictions.eq("sysGuestTypeUuid", sysGuestTravelerTypeRelQuery.getSysGuestTypeUuid()));
		}
		if (StringUtils.isNotEmpty(sysGuestTravelerTypeRelQuery.getSysTravelerTypeUuid())){
			dc.add(Restrictions.eq("sysTravelerTypeUuid", sysGuestTravelerTypeRelQuery.getSysTravelerTypeUuid()));
		}
	   	if(sysGuestTravelerTypeRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysGuestTravelerTypeRelQuery.getCreateBy()));
	   	}
		if(sysGuestTravelerTypeRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysGuestTravelerTypeRelQuery.getCreateDate()));
		}
	   	if(sysGuestTravelerTypeRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysGuestTravelerTypeRelQuery.getUpdateBy()));
	   	}
		if(sysGuestTravelerTypeRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysGuestTravelerTypeRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysGuestTravelerTypeRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", sysGuestTravelerTypeRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysGuestTravelerTypeRelDao.find(page, dc);
	}
	
	public List<SysGuestTravelerTypeRel> find( SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery) {
		DetachedCriteria dc = sysGuestTravelerTypeRelDao.createDetachedCriteria();
		
	   	if(sysGuestTravelerTypeRelQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysGuestTravelerTypeRelQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysGuestTravelerTypeRelQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", sysGuestTravelerTypeRelQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(sysGuestTravelerTypeRelQuery.getSysGuestTypeUuid())){
			dc.add(Restrictions.eq("sysGuestTypeUuid", sysGuestTravelerTypeRelQuery.getSysGuestTypeUuid()));
		}
		if (StringUtils.isNotEmpty(sysGuestTravelerTypeRelQuery.getSysTravelerTypeUuid())){
			dc.add(Restrictions.eq("sysTravelerTypeUuid", sysGuestTravelerTypeRelQuery.getSysTravelerTypeUuid()));
		}
	   	if(sysGuestTravelerTypeRelQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysGuestTravelerTypeRelQuery.getCreateBy()));
	   	}
		if(sysGuestTravelerTypeRelQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysGuestTravelerTypeRelQuery.getCreateDate()));
		}
	   	if(sysGuestTravelerTypeRelQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysGuestTravelerTypeRelQuery.getUpdateBy()));
	   	}
		if(sysGuestTravelerTypeRelQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysGuestTravelerTypeRelQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysGuestTravelerTypeRelQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", sysGuestTravelerTypeRelQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysGuestTravelerTypeRelDao.find(dc);
	}
	
	public SysGuestTravelerTypeRel getByUuid(String uuid) {
		return sysGuestTravelerTypeRelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		SysGuestTravelerTypeRel sysGuestTravelerTypeRel = getByUuid(uuid);
		sysGuestTravelerTypeRel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(sysGuestTravelerTypeRel);
	}
	
	public void removeBySysGuestTypeUuid(String sysGuestTypeUuid){
		sysGuestTravelerTypeRelDao.removeBySysGuestTypeUuid(sysGuestTypeUuid);
	}
	
}
