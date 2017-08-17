/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.SysDictDao;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysDictServiceImpl  extends BaseService implements SysDictService{
	@Autowired
	private SysDictDao sysDictDao;
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;
	

	public void save (SysDict sysDict) {
		sysDictDao.saveObj(sysDict);
	}

	public void save (SysCompanyDictView sysCompanyDictView){
		SysDict sysDict = new SysDict();
		sysDict.setLabel(sysCompanyDictView.getLabel());
		sysDict.setSort(sysCompanyDictView.getSort());
		sysDict.setType(sysCompanyDictView.getType());
		sysDict.setDescription(sysCompanyDictView.getDescription());
		
		super.setOptInfo(sysDict,null);
		
		if(sysCompanyDictView.getSort() == null) {
			sysDict.setSort(50);
		}
		
		sysDict.setUuid(UuidUtils.generUuid());

		int count = 0;
		while(count < 3) {
			try{
				
				//从view中查询value的值(value的值取特定类型的最大值，并加1)
				int value = sysCompanyDictViewDao.findMaxValueByCompanyIdAndType(sysDict.getType());
				sysDict.setValue(String.valueOf(value+1));

				sysDictDao.saveObj(sysDict);
				count = 3;
			} catch(Exception e) {
				logger.info("新建字典记录错误（第" + count + "次），error:",e);
				count++;
			}
		}
	}
	
	public void update (SysDict sysDict){
		sysDictDao.updateObj(sysDict);
	}
	
	public void update (SysCompanyDictView sysCompanyDictView){
		SysDict entity = getById(sysCompanyDictView.getId().longValue());
		
		User user = UserUtils.getUser();
		if(user != null) {
			entity.setUpdateBy(user.getId());
		}
		entity.setLabel(sysCompanyDictView.getLabel());
		entity.setSort(sysCompanyDictView.getSort());
		entity.setDescription(sysCompanyDictView.getDescription());
		if(StringUtils.isNotEmpty(sysCompanyDictView.getValue())) {
			entity.setValue(sysCompanyDictView.getValue());
		}
		
		sysDictDao.updateObj(entity);
	}
	
	public SysDict getById(java.lang.Long value) {
		return sysDictDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		SysDict entity = sysDictDao.getById(value);
		entity.setDelFlag("1");
		sysDictDao.updateObj(entity);
	}	
	public void removeByUuid(String uuid){
		SysDict entity = sysDictDao.getByUuid(uuid);
		entity.setDelFlag("1");
		sysDictDao.updateObj(entity);
	}
	
	public Page<SysDict> find(Page<SysDict> page, SysDict sysDict) {
		DetachedCriteria dc = sysDictDao.createDetachedCriteria();
		
	   	if(sysDict.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysDict.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysDict.getLabel())){
			dc.add(Restrictions.like("label", "%"+sysDict.getLabel()+"%"));
		}
		if (StringUtils.isNotEmpty(sysDict.getValue())){
			dc.add(Restrictions.like("value", "%"+sysDict.getValue()+"%"));
		}
		if (StringUtils.isNotEmpty(sysDict.getType())){
			dc.add(Restrictions.like("type", "%"+sysDict.getType()+"%"));
		}
		if (StringUtils.isNotEmpty(sysDict.getDescription())){
			dc.add(Restrictions.like("description", "%"+sysDict.getDescription()+"%"));
		}
	   	if(sysDict.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", sysDict.getSort()));
	   	}
	   	if(sysDict.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysDict.getCreateBy()));
	   	}
		if(sysDict.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysDict.getCreateDate()));
		}
	   	if(sysDict.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysDict.getUpdateBy()));
	   	}
		if(sysDict.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysDict.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysDict.getRemarks())){
			dc.add(Restrictions.like("remarks", "%"+sysDict.getRemarks()+"%"));
		}
		if (StringUtils.isNotEmpty(sysDict.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysDict.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return sysDictDao.find(page, dc);
	}
	
	public List<SysDict> find(SysDict sysDict){
		DetachedCriteria dc = sysDictDao.createDetachedCriteria();
		
	   	if(sysDict.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysDict.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysDict.getLabel())){
			dc.add(Restrictions.like("label", "%"+sysDict.getLabel()+"%"));
		}
		if (StringUtils.isNotEmpty(sysDict.getValue())){
			dc.add(Restrictions.like("value", "%"+sysDict.getValue()+"%"));
		}
		if (StringUtils.isNotEmpty(sysDict.getType())){
			dc.add(Restrictions.like("type", "%"+sysDict.getType()+"%"));
		}
		if (StringUtils.isNotEmpty(sysDict.getDescription())){
			dc.add(Restrictions.like("description", "%"+sysDict.getDescription()+"%"));
		}
	   	if(sysDict.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", sysDict.getSort()));
	   	}
	   	if(sysDict.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysDict.getCreateBy()));
	   	}
		if(sysDict.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysDict.getCreateDate()));
		}
	   	if(sysDict.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysDict.getUpdateBy()));
	   	}
		if(sysDict.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysDict.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysDict.getRemarks())){
			dc.add(Restrictions.like("remarks", "%"+sysDict.getRemarks()+"%"));
		}
		if (StringUtils.isNotEmpty(sysDict.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysDict.getDelFlag()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return sysDictDao.find(dc);
	}
	

	public List<SysDict> findAll() {
		return sysDictDao.findAll();
	}
	
	public void updateSysDicts(List<SysDict> sysDicts) {
		sysDictDao.updateSysDicts(sysDicts);
	}
	
	public List<SysDict> findByType(String type) {
		return sysDictDao.findByType(type);
	}

	@Override
	public SysDict getByUuId(String uuid) {
		return sysDictDao.getByUuid(uuid);
	}
	
}
