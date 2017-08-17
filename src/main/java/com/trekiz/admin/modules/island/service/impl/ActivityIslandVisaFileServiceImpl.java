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
import com.trekiz.admin.modules.island.dao.ActivityIslandVisaFileDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandVisaFile;
import com.trekiz.admin.modules.island.input.ActivityIslandVisaFileInput;
import com.trekiz.admin.modules.island.query.ActivityIslandVisaFileQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandVisaFileService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class ActivityIslandVisaFileServiceImpl  extends BaseService implements ActivityIslandVisaFileService{
	@Autowired
	private ActivityIslandVisaFileDao activityIslandVisaFileDao;

	public void save (ActivityIslandVisaFile activityIslandVisaFile){
		super.setOptInfo(activityIslandVisaFile, BaseService.OPERATION_ADD);
		activityIslandVisaFileDao.saveObj(activityIslandVisaFile);
	}
	
	public void save (ActivityIslandVisaFileInput activityIslandVisaFileInput){
		ActivityIslandVisaFile activityIslandVisaFile = activityIslandVisaFileInput.getActivityIslandVisaFile();
		super.setOptInfo(activityIslandVisaFile, BaseService.OPERATION_ADD);
		activityIslandVisaFileDao.saveObj(activityIslandVisaFile);
	}
	
	public void update (ActivityIslandVisaFile activityIslandVisaFile){
		super.setOptInfo(activityIslandVisaFile, BaseService.OPERATION_UPDATE);
		activityIslandVisaFileDao.updateObj(activityIslandVisaFile);
	}
	
	public ActivityIslandVisaFile getById(java.lang.Integer value) {
		return activityIslandVisaFileDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		ActivityIslandVisaFile obj = activityIslandVisaFileDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<ActivityIslandVisaFile> find(Page<ActivityIslandVisaFile> page, ActivityIslandVisaFileQuery activityIslandVisaFileQuery) {
		DetachedCriteria dc = activityIslandVisaFileDao.createDetachedCriteria();
		
	   	if(activityIslandVisaFileQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandVisaFileQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandVisaFileQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandVisaFileQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFileQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandVisaFileQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFileQuery.getCountry())){
			dc.add(Restrictions.eq("country", activityIslandVisaFileQuery.getCountry()));
		}
	   	if(activityIslandVisaFileQuery.getVisaTypeId()!=null){
	   		dc.add(Restrictions.eq("visaTypeId", activityIslandVisaFileQuery.getVisaTypeId()));
	   	}
	   	if(activityIslandVisaFileQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandVisaFileQuery.getCreateBy()));
	   	}
	   	if(activityIslandVisaFileQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandVisaFileQuery.getUpdateBy()));
	   	}
		if(activityIslandVisaFileQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandVisaFileQuery.getUpdateDate()));
		}
		if(activityIslandVisaFileQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandVisaFileQuery.getCreateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFileQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandVisaFileQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandVisaFileDao.find(page, dc);
	}
	
	public List<ActivityIslandVisaFile> find( ActivityIslandVisaFileQuery activityIslandVisaFileQuery) {
		DetachedCriteria dc = activityIslandVisaFileDao.createDetachedCriteria();
		
	   	if(activityIslandVisaFileQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandVisaFileQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandVisaFileQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandVisaFileQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFileQuery.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandVisaFileQuery.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFileQuery.getCountry())){
			dc.add(Restrictions.eq("country", activityIslandVisaFileQuery.getCountry()));
		}
	   	if(activityIslandVisaFileQuery.getVisaTypeId()!=null){
	   		dc.add(Restrictions.eq("visaTypeId", activityIslandVisaFileQuery.getVisaTypeId()));
	   	}
	   	if(activityIslandVisaFileQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandVisaFileQuery.getCreateBy()));
	   	}
	   	if(activityIslandVisaFileQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandVisaFileQuery.getUpdateBy()));
	   	}
		if(activityIslandVisaFileQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandVisaFileQuery.getUpdateDate()));
		}
		if(activityIslandVisaFileQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandVisaFileQuery.getCreateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFileQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandVisaFileQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandVisaFileDao.find(dc);
	}
	
	public List<ActivityIslandVisaFile> find( ActivityIslandVisaFile activityIslandVisaFile) {
		DetachedCriteria dc = activityIslandVisaFileDao.createDetachedCriteria();
		
	   	if(activityIslandVisaFile.getId()!=null){
	   		dc.add(Restrictions.eq("id", activityIslandVisaFile.getId()));
	   	}
		if (StringUtils.isNotEmpty(activityIslandVisaFile.getUuid())){
			dc.add(Restrictions.eq("uuid", activityIslandVisaFile.getUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFile.getActivityIslandUuid())){
			dc.add(Restrictions.eq("activityIslandUuid", activityIslandVisaFile.getActivityIslandUuid()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFile.getCountry())){
			dc.add(Restrictions.eq("country", activityIslandVisaFile.getCountry()));
		}
	   	if(activityIslandVisaFile.getVisaTypeId()!=null){
	   		dc.add(Restrictions.eq("visaTypeId", activityIslandVisaFile.getVisaTypeId()));
	   	}
	   	if(activityIslandVisaFile.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", activityIslandVisaFile.getCreateBy()));
	   	}
	   	if(activityIslandVisaFile.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", activityIslandVisaFile.getUpdateBy()));
	   	}
		if(activityIslandVisaFile.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", activityIslandVisaFile.getUpdateDate()));
		}
		if(activityIslandVisaFile.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", activityIslandVisaFile.getCreateDate()));
		}
		if (StringUtils.isNotEmpty(activityIslandVisaFile.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", activityIslandVisaFile.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return activityIslandVisaFileDao.find(dc);
	}
	
	public ActivityIslandVisaFile getByUuid(String uuid) {
		return activityIslandVisaFileDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		ActivityIslandVisaFile activityIslandVisaFile = getByUuid(uuid);
		activityIslandVisaFile.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(activityIslandVisaFile);
	}
}
