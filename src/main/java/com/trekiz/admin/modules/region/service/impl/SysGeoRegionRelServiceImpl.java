/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.region.service.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.region.dao.SysGeoRegionRelDao;
import com.trekiz.admin.modules.region.entity.SysGeoRegionRel;
import com.trekiz.admin.modules.region.service.SysGeoRegionRelService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysGeoRegionRelServiceImpl  extends BaseService implements SysGeoRegionRelService{
	@Autowired
	private SysGeoRegionRelDao sysGeoRegionRelDao;


	public void save (SysGeoRegionRel sysGeoRegionRel){
		super.setOptInfo(sysGeoRegionRel, BaseService.OPERATION_ADD);
		sysGeoRegionRelDao.saveObj(sysGeoRegionRel);
	}
	
	public void update (SysGeoRegionRel sysGeoRegionRel){
		super.setOptInfo(sysGeoRegionRel, BaseService.OPERATION_UPDATE);
		sysGeoRegionRelDao.updateObj(sysGeoRegionRel);
	}
	
	public SysGeoRegionRel getById(java.lang.Integer value) {
		return sysGeoRegionRelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		SysGeoRegionRel obj = sysGeoRegionRelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<SysGeoRegionRel> find(Page<SysGeoRegionRel> page, SysGeoRegionRel sysGeoRegionRel) {
		DetachedCriteria dc = sysGeoRegionRelDao.createDetachedCriteria();
		
	   	if(sysGeoRegionRel.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysGeoRegionRel.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoRegionRel.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysGeoRegionRel.getUuid()+"%"));
		}
	   	if(sysGeoRegionRel.getGeoId()!=null){
	   		dc.add(Restrictions.eq("geoId", sysGeoRegionRel.getGeoId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoRegionRel.getGeoUuid())){
			dc.add(Restrictions.like("geoUuid", "%"+sysGeoRegionRel.getGeoUuid()+"%"));
		}
	   	if(sysGeoRegionRel.getRegionId()!=null){
	   		dc.add(Restrictions.eq("regionId", sysGeoRegionRel.getRegionId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoRegionRel.getRegionUuid())){
			dc.add(Restrictions.like("regionUuid", "%"+sysGeoRegionRel.getRegionUuid()+"%"));
		}
	   	if(sysGeoRegionRel.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysGeoRegionRel.getCreateBy()));
	   	}
		if(sysGeoRegionRel.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysGeoRegionRel.getCreateDate()));
		}
	   	if(sysGeoRegionRel.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysGeoRegionRel.getUpdateBy()));
	   	}
		if(sysGeoRegionRel.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysGeoRegionRel.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysGeoRegionRel.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysGeoRegionRel.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysGeoRegionRelDao.find(page, dc);
	}
	
	public List<SysGeoRegionRel> find( SysGeoRegionRel sysGeoRegionRel) {
		DetachedCriteria dc = sysGeoRegionRelDao.createDetachedCriteria();
		
	   	if(sysGeoRegionRel.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysGeoRegionRel.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoRegionRel.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysGeoRegionRel.getUuid()+"%"));
		}
	   	if(sysGeoRegionRel.getGeoId()!=null){
	   		dc.add(Restrictions.eq("geoId", sysGeoRegionRel.getGeoId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoRegionRel.getGeoUuid())){
			dc.add(Restrictions.like("geoUuid", "%"+sysGeoRegionRel.getGeoUuid()+"%"));
		}
	   	if(sysGeoRegionRel.getRegionId()!=null){
	   		dc.add(Restrictions.eq("regionId", sysGeoRegionRel.getRegionId()));
	   	}
		if (StringUtils.isNotEmpty(sysGeoRegionRel.getRegionUuid())){
			dc.add(Restrictions.like("regionUuid", "%"+sysGeoRegionRel.getRegionUuid()+"%"));
		}
	   	if(sysGeoRegionRel.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysGeoRegionRel.getCreateBy()));
	   	}
		if(sysGeoRegionRel.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysGeoRegionRel.getCreateDate()));
		}
	   	if(sysGeoRegionRel.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysGeoRegionRel.getUpdateBy()));
	   	}
		if(sysGeoRegionRel.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysGeoRegionRel.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysGeoRegionRel.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysGeoRegionRel.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return sysGeoRegionRelDao.find(dc);
	}
	@Override
	 public void delRel(int  id){
		String hql = " UPDATE SysGeoRegionRel SET delFlag = '1' WHERE regionId = "+id;
		sysGeoRegionRelDao.getSession().createQuery(hql).executeUpdate();
	 }
}
