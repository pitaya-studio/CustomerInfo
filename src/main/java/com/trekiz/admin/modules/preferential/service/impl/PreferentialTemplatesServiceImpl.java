/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.preferential.entity.*;
import com.trekiz.admin.modules.preferential.dao.*;
import com.trekiz.admin.modules.preferential.service.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PreferentialTemplatesServiceImpl  extends BaseService implements PreferentialTemplatesService{
	@Autowired
	private PreferentialTemplatesDao preferentialTemplatesDao;
	@Autowired
	private PreferentialDictTemplatesRelDao preferentialDictTemplatesRelDao;
	@Autowired
	private PreferentialDictUnitRelDao preferentialDictUnitRelDao;

	public void save (PreferentialTemplates preferentialTemplates){
		super.setOptInfo(preferentialTemplates, BaseService.OPERATION_ADD);
		preferentialTemplatesDao.saveObj(preferentialTemplates);
	}
	
	public void update (PreferentialTemplates preferentialTemplates){
		super.setOptInfo(preferentialTemplates, BaseService.OPERATION_UPDATE);
		preferentialTemplatesDao.updateObj(preferentialTemplates);
	}
	
	public PreferentialTemplates getById(java.lang.Integer value) {
		return preferentialTemplatesDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PreferentialTemplates obj = preferentialTemplatesDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	

	public Page<PreferentialTemplates> find(Page<PreferentialTemplates> page, PreferentialTemplates preferentialTemplates) {
		DetachedCriteria dc = preferentialTemplatesDao.createDetachedCriteria();
		
	   	if(preferentialTemplates.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialTemplates.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialTemplates.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialTemplates.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialTemplates.getName())){
			dc.add(Restrictions.eq("name", preferentialTemplates.getName()));
		}
		if (StringUtils.isNotEmpty(preferentialTemplates.getType())){
			dc.add(Restrictions.eq("type", preferentialTemplates.getType()));
		}
		if (StringUtils.isNotEmpty(preferentialTemplates.getOutHtml())){
			dc.add(Restrictions.eq("outHtml", preferentialTemplates.getOutHtml()));
		}
	   	if(preferentialTemplates.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialTemplates.getCreateBy()));
	   	}
		if(preferentialTemplates.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialTemplates.getCreateDate()));
		}
	   	if(preferentialTemplates.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialTemplates.getUpdateBy()));
	   	}
		if(preferentialTemplates.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialTemplates.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialTemplates.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialTemplates.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialTemplatesDao.find(page, dc);
	}
	
	public List<PreferentialTemplates> find(PreferentialTemplates preferentialTemplates) {
		DetachedCriteria dc = preferentialTemplatesDao.createDetachedCriteria();
		
	   	if(preferentialTemplates.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialTemplates.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialTemplates.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialTemplates.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialTemplates.getName())){
			dc.add(Restrictions.eq("name", preferentialTemplates.getName()));
		}
		if (StringUtils.isNotEmpty(preferentialTemplates.getType())){
			dc.add(Restrictions.eq("type", preferentialTemplates.getType()));
		}
		if (StringUtils.isNotEmpty(preferentialTemplates.getOutHtml())){
			dc.add(Restrictions.eq("outHtml", preferentialTemplates.getOutHtml()));
		}
	   	if(preferentialTemplates.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialTemplates.getCreateBy()));
	   	}
		if(preferentialTemplates.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialTemplates.getCreateDate()));
		}
	   	if(preferentialTemplates.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialTemplates.getUpdateBy()));
	   	}
		if(preferentialTemplates.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialTemplates.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialTemplates.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialTemplates.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialTemplatesDao.find(dc);
	}
	
	public PreferentialTemplates getByUuid(String uuid) {
		return preferentialTemplatesDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PreferentialTemplates preferentialTemplates = getByUuid(uuid);
		preferentialTemplates.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(preferentialTemplates);
	}
	
	@SuppressWarnings("unchecked")
	public void saveTemplatesAndRel(PreferentialTemplates preferentialTemplates, Map<String, Object> datas) {
		List<PreferentialDictTemplatesRel> tempsRels = (List<PreferentialDictTemplatesRel>)datas.get("tempsRels");
		preferentialTemplates.setOutHtml(datas.get("outHtml").toString());
		//保存模板表数据
		this.save(preferentialTemplates);
		
		//保存模板关联表数据
		batchSaveTemplatesRel(tempsRels, preferentialTemplates.getUuid());
	}
	
	@SuppressWarnings("unchecked")
	public void updateTemplatesAndRel(PreferentialTemplates preferentialTemplates, Map<String, Object> datas) {
		List<PreferentialDictTemplatesRel> tempsRels = (List<PreferentialDictTemplatesRel>)datas.get("tempsRels");
		
		//更新模板表数据
		PreferentialTemplates entity = preferentialTemplatesDao.getByUuid(preferentialTemplates.getUuid());
		entity.setName(preferentialTemplates.getName());
		entity.setOutHtml(datas.get("outHtml").toString());
		this.update(entity);
		
		//批量删除原有关联表数据
		int count = preferentialDictTemplatesRelDao.removeByTemplatesUuid(entity.getUuid());
		
		batchSaveTemplatesRel(tempsRels, entity.getUuid());
	}
	
	/**
	 * 批量保存模板关联表数据
	 * @param tempsRels
	 */
	private void batchSaveTemplatesRel(List<PreferentialDictTemplatesRel> tempsRels, String tempsUuid) {
		//根据模板表uuid，保存字典单位关联和模板关联表
		if(CollectionUtils.isNotEmpty(tempsRels)) {
			for(PreferentialDictTemplatesRel tempsRel : tempsRels) {
				tempsRel.setTemplatesUuid(tempsUuid);
				super.setOptInfo(tempsRel, OPERATION_ADD);
			}
		}
		//批量保存
		preferentialDictTemplatesRelDao.batchSave(tempsRels);
	}
	
	public Page<Map<String, String>> findTemplatesInfos(Page<Map<String, String>> page, String tempName) {
		return preferentialDictTemplatesRelDao.findTemplatesInfos(page, tempName);
	}
	
	public List<Object[]> findTempsInfoByUuid(String uuid) {
		return preferentialDictTemplatesRelDao.findTempsInfoByTempsUuid(uuid);
	}
	
	public List<Object[]> findRelInfoByTempUuid(String tempUuid) {
		return preferentialDictTemplatesRelDao.findRelInfoByTempUuid(tempUuid);
	}
}
