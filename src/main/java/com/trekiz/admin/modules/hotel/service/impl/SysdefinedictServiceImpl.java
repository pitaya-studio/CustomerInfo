/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotel.dao.SysdefinedictDao;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.hotel.service.SysdefinedictService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysdefinedictServiceImpl  extends BaseService implements SysdefinedictService{
	@Autowired
	private SysdefinedictDao sysdefinedictDao;
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;
	

	public void save(Sysdefinedict sysdefinedict) {
		sysdefinedictDao.saveObj(sysdefinedict);
	}

	public void save (SysCompanyDictView sysCompanyDictView, long companyId){
		Sysdefinedict sysdefinedict = new Sysdefinedict();
		
		sysdefinedict.setLabel(sysCompanyDictView.getLabel());
		sysdefinedict.setSort(sysCompanyDictView.getSort());
		sysdefinedict.setType(sysCompanyDictView.getType());
		sysdefinedict.setDescription(sysCompanyDictView.getDescription());
		
		sysdefinedict.setCompanyId(Integer.parseInt(companyId+""));
		
		User user = UserUtils.getUser();
		if(user != null) {
			sysdefinedict.setCreateBy(user.getId());
			sysdefinedict.setUpdateBy(user.getId());
		}
		sysdefinedict.setCreateDate(new Date());
		sysdefinedict.setUpdateDate(new Date());
		
		if(sysCompanyDictView.getSort() == null) {
			sysdefinedict.setSort(50);
		}
		
		if(StringUtils.isEmpty(sysCompanyDictView.getDefaultFlag())) {
			sysdefinedict.setDefaultFlag("0");
		} else {
			sysdefinedict.setDefaultFlag(sysCompanyDictView.getDefaultFlag());
		}
		
		sysdefinedict.setDelFlag("0");
		sysdefinedict.setUuid(UuidUtils.generUuid());

		//为视图设置当前uuid，回调时使用
		sysCompanyDictView.setUuid(sysdefinedict.getUuid());
		int count =0;
		while(count < 3) {
			try{
				
				//从view中查询value的值(value的值取特定类型的最大值，并加1)
				int value = sysCompanyDictViewDao.findMaxValueByCompanyIdAndType(sysdefinedict.getType());
				sysdefinedict.setValue(String.valueOf(value + 1));
				
				sysdefinedictDao.saveObj(sysdefinedict);
				count = 3;
			} catch(Exception e) {
				logger.info("新建字典记录错误（第" + count + "次），error:",e);
				count++;
			}
		}
	}
	
	public void update (Sysdefinedict sysdefinedict){
		sysdefinedictDao.updateObj(sysdefinedict);
	}
	
	public void update (SysCompanyDictView sysCompanyDictView){
		
		Sysdefinedict entity = getById(sysCompanyDictView.getId().longValue());
		
		User user = UserUtils.getUser();
		if(user != null) {
			entity.setUpdateBy(user.getId());
		}
		entity.setLabel(sysCompanyDictView.getLabel());
		entity.setSort(sysCompanyDictView.getSort());
		entity.setDescription(sysCompanyDictView.getDescription());
		
		
		sysdefinedictDao.updateObj(entity);
	}
	
	public Sysdefinedict getById(java.lang.Long value) {
		return sysdefinedictDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		Sysdefinedict entity = sysdefinedictDao.getById(value);
		entity.setDelFlag("1");
		sysdefinedictDao.updateObj(entity);
	}	
	
	public void removeByUuid(String uuid){
		Sysdefinedict entity = sysdefinedictDao.getByUuid(uuid);
		entity.setDelFlag("1");
		sysdefinedictDao.updateObj(entity);
	}
	
	public Page<Sysdefinedict> find(Page<Sysdefinedict> page, Sysdefinedict sysdefinedict) {
		DetachedCriteria dc = sysdefinedictDao.createDetachedCriteria();
		
	   	if(sysdefinedict.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysdefinedict.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysdefinedict.getLabel())){
			dc.add(Restrictions.like("label", "%"+sysdefinedict.getLabel()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getValue())){
			dc.add(Restrictions.like("value", "%"+sysdefinedict.getValue()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getType())){
			dc.add(Restrictions.like("type", "%"+sysdefinedict.getType()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getDefaultFlag())){
			dc.add(Restrictions.like("defaultFlag", "%"+sysdefinedict.getDefaultFlag()+"%"));
		}
	   	if(sysdefinedict.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", sysdefinedict.getSort()));
	   	}
		if (StringUtils.isNotEmpty(sysdefinedict.getDescription())){
			dc.add(Restrictions.like("description", "%"+sysdefinedict.getDescription()+"%"));
		}
	   	if(sysdefinedict.getCompanyId()!=null){
	   		dc.add(Restrictions.eq("companyId", sysdefinedict.getCompanyId()));
	   	}
	   	if(sysdefinedict.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysdefinedict.getCreateBy()));
	   	}
		if(sysdefinedict.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysdefinedict.getCreateDate()));
		}
	   	if(sysdefinedict.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysdefinedict.getUpdateBy()));
	   	}
		if(sysdefinedict.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysdefinedict.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysdefinedict.getDelFlag()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getRemarks())){
			dc.add(Restrictions.like("remarks", "%"+sysdefinedict.getRemarks()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysdefinedict.getUuid()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return sysdefinedictDao.find(page, dc);
	}
	
	public List<Sysdefinedict> find( Sysdefinedict sysdefinedict) {
		DetachedCriteria dc = sysdefinedictDao.createDetachedCriteria();
		
	   	if(sysdefinedict.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysdefinedict.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysdefinedict.getLabel())){
			dc.add(Restrictions.like("label", "%"+sysdefinedict.getLabel()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getValue())){
			dc.add(Restrictions.like("value", "%"+sysdefinedict.getValue()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getType())){
			dc.add(Restrictions.like("type", "%"+sysdefinedict.getType()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getDefaultFlag())){
			dc.add(Restrictions.like("defaultFlag", "%"+sysdefinedict.getDefaultFlag()+"%"));
		}
	   	if(sysdefinedict.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", sysdefinedict.getSort()));
	   	}
		if (StringUtils.isNotEmpty(sysdefinedict.getDescription())){
			dc.add(Restrictions.like("description", "%"+sysdefinedict.getDescription()+"%"));
		}
	   	if(sysdefinedict.getCompanyId()!=null){
	   		dc.add(Restrictions.eq("companyId", sysdefinedict.getCompanyId()));
	   	}
	   	if(sysdefinedict.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysdefinedict.getCreateBy()));
	   	}
		if(sysdefinedict.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysdefinedict.getCreateDate()));
		}
	   	if(sysdefinedict.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysdefinedict.getUpdateBy()));
	   	}
		if(sysdefinedict.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysdefinedict.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysdefinedict.getDelFlag()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getRemarks())){
			dc.add(Restrictions.like("remarks", "%"+sysdefinedict.getRemarks()+"%"));
		}
		if (StringUtils.isNotEmpty(sysdefinedict.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysdefinedict.getUuid()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return sysdefinedictDao.find(dc);
	}
	
	public List<Sysdefinedict> findAll() {
		return sysdefinedictDao.findAll();
	}
	
	public Sysdefinedict findByUUid(String uuid){
		return sysdefinedictDao.getByUuid(uuid);
	}
	
	public void updateSysdefinedicts(List<Sysdefinedict> sysdefinedicts) {
		sysdefinedictDao.updateSysdefinedicts(sysdefinedicts);
	}
}
