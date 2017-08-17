/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.preferential.dao.PreferentialLogicOperationDao;
import com.trekiz.admin.modules.preferential.entity.PreferentialLogicOperation;
import com.trekiz.admin.modules.preferential.service.PreferentialLogicOperationService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PreferentialLogicOperationServiceImpl  extends BaseService implements PreferentialLogicOperationService{
	@Autowired
	private PreferentialLogicOperationDao preferentialLogicOperationDao;

	public void save (PreferentialLogicOperation preferentialLogicOperation){
		super.setOptInfo(preferentialLogicOperation, BaseService.OPERATION_ADD);
		preferentialLogicOperationDao.saveObj(preferentialLogicOperation);
	}
	
	public void update (PreferentialLogicOperation preferentialLogicOperation){
		PreferentialLogicOperation oldObj = preferentialLogicOperationDao.getByUuid(preferentialLogicOperation.getUuid());
		BeanUtil.copySimpleProperties(oldObj, preferentialLogicOperation,true);
		super.setOptInfo(oldObj, BaseService.OPERATION_UPDATE);
		preferentialLogicOperationDao.updateObj(oldObj);
	}
	
	public PreferentialLogicOperation getById(java.lang.Integer value) {
		return preferentialLogicOperationDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PreferentialLogicOperation obj = preferentialLogicOperationDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PreferentialLogicOperation> find(Page<PreferentialLogicOperation> page, PreferentialLogicOperation preferentialLogicOperation) {
		DetachedCriteria dc = preferentialLogicOperationDao.createDetachedCriteria();
		
	   	if(preferentialLogicOperation.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialLogicOperation.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialLogicOperation.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getName())){
			dc.add(Restrictions.eq("name", preferentialLogicOperation.getName()));
		}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getRunClass())){
			dc.add(Restrictions.eq("runClass", preferentialLogicOperation.getRunClass()));
		}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getRunMethod())){
			dc.add(Restrictions.eq("runMethod", preferentialLogicOperation.getRunMethod()));
		}
	   	if(preferentialLogicOperation.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialLogicOperation.getCreateBy()));
	   	}
		if(preferentialLogicOperation.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialLogicOperation.getCreateDate()));
		}
	   	if(preferentialLogicOperation.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialLogicOperation.getUpdateBy()));
	   	}
		if(preferentialLogicOperation.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialLogicOperation.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialLogicOperation.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialLogicOperationDao.find(page, dc);
	}
	
	public List<PreferentialLogicOperation> find( PreferentialLogicOperation preferentialLogicOperation) {
		DetachedCriteria dc = preferentialLogicOperationDao.createDetachedCriteria();
		
	   	if(preferentialLogicOperation.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialLogicOperation.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialLogicOperation.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getName())){
			dc.add(Restrictions.eq("name", preferentialLogicOperation.getName()));
		}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getRunClass())){
			dc.add(Restrictions.eq("runClass", preferentialLogicOperation.getRunClass()));
		}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getRunMethod())){
			dc.add(Restrictions.eq("runMethod", preferentialLogicOperation.getRunMethod()));
		}
	   	if(preferentialLogicOperation.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialLogicOperation.getCreateBy()));
	   	}
		if(preferentialLogicOperation.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialLogicOperation.getCreateDate()));
		}
	   	if(preferentialLogicOperation.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialLogicOperation.getUpdateBy()));
	   	}
		if(preferentialLogicOperation.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialLogicOperation.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialLogicOperation.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialLogicOperation.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialLogicOperationDao.find(dc);
	}
	
	public PreferentialLogicOperation getByUuid(String uuid) {
		return preferentialLogicOperationDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PreferentialLogicOperation preferentialLogicOperation = getByUuid(uuid);
		preferentialLogicOperation.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(preferentialLogicOperation);
	}
}
