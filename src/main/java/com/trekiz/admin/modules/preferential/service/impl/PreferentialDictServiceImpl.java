/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.preferential.dao.PreferentialDictDao;
import com.trekiz.admin.modules.preferential.dao.PreferentialDictUnitRelDao;
import com.trekiz.admin.modules.preferential.entity.PreferentialDict;
import com.trekiz.admin.modules.preferential.entity.PreferentialDictUnitRel;
import com.trekiz.admin.modules.preferential.input.PreferentialDictInput;
import com.trekiz.admin.modules.preferential.service.PreferentialDictService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PreferentialDictServiceImpl  extends BaseService implements PreferentialDictService{
	@Autowired
	private PreferentialDictDao preferentialDictDao;
	@Autowired
	private PreferentialDictUnitRelDao preferentialDictUnitRelDao;

	public void save (PreferentialDict preferentialDict){
		super.setOptInfo(preferentialDict, BaseService.OPERATION_ADD);
		preferentialDictDao.saveObj(preferentialDict);
	}
	
	public void update (PreferentialDict preferentialDict){
		super.setOptInfo(preferentialDict, BaseService.OPERATION_UPDATE);
		preferentialDictDao.updateObj(preferentialDict);
	}
	
	public PreferentialDict getById(java.lang.Integer value) {
		return preferentialDictDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PreferentialDict obj = preferentialDictDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PreferentialDict> find(Page<PreferentialDict> page, PreferentialDict preferentialDict) {
		DetachedCriteria dc = preferentialDictDao.createDetachedCriteria();
		
	   	if(preferentialDict.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialDict.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialDict.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialDict.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDict.getName())){
			dc.add(Restrictions.eq("name", preferentialDict.getName()));
		}
	   	if(preferentialDict.getDataType()!=null){
	   		dc.add(Restrictions.eq("dataType", preferentialDict.getDataType()));
	   	}
	   	if(preferentialDict.getType()!=null){
	   		dc.add(Restrictions.eq("type", preferentialDict.getType()));
	   	}
	   	if(preferentialDict.getRelationalOperator()!=null){
	   		dc.add(Restrictions.eq("relationalOperator", preferentialDict.getRelationalOperator()));
	   	}
		if (StringUtils.isNotEmpty(preferentialDict.getLogicOperationUuid())){
			dc.add(Restrictions.eq("logicOperationUuid", preferentialDict.getLogicOperationUuid()));
		}
	   	if(preferentialDict.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialDict.getCreateBy()));
	   	}
		if(preferentialDict.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialDict.getCreateDate()));
		}
	   	if(preferentialDict.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialDict.getUpdateBy()));
	   	}
		if(preferentialDict.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialDict.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialDict.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialDict.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialDictDao.find(page, dc);
	}
	
	public List<PreferentialDict> find( PreferentialDict preferentialDict) {
		DetachedCriteria dc = preferentialDictDao.createDetachedCriteria();
		
	   	if(preferentialDict.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialDict.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialDict.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialDict.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDict.getName())){
			dc.add(Restrictions.eq("name", preferentialDict.getName()));
		}
	   	if(preferentialDict.getDataType()!=null){
	   		dc.add(Restrictions.eq("dataType", preferentialDict.getDataType()));
	   	}
	   	if(preferentialDict.getType()!=null){
	   		dc.add(Restrictions.eq("type", preferentialDict.getType()));
	   	}
	   	if(preferentialDict.getRelationalOperator()!=null){
	   		dc.add(Restrictions.eq("relationalOperator", preferentialDict.getRelationalOperator()));
	   	}
		if (StringUtils.isNotEmpty(preferentialDict.getLogicOperationUuid())){
			dc.add(Restrictions.eq("logicOperationUuid", preferentialDict.getLogicOperationUuid()));
		}
	   	if(preferentialDict.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialDict.getCreateBy()));
	   	}
		if(preferentialDict.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialDict.getCreateDate()));
		}
	   	if(preferentialDict.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialDict.getUpdateBy()));
	   	}
		if(preferentialDict.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialDict.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialDict.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialDict.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialDictDao.find(dc);
	}
	
	public PreferentialDict getByUuid(String uuid) {
		return preferentialDictDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PreferentialDict preferentialDict = getByUuid(uuid);
		preferentialDict.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(preferentialDict);
	}
	
	/**
	 * 保存字典接口
	 * 关联保存相关的关联数据
	 * add by zhanghao
	 * @param input
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public String saveObj(PreferentialDictInput input){
		
		PreferentialDict dict = input.getDict();
		super.setOptInfo(dict, BaseService.OPERATION_ADD);
		preferentialDictDao.saveObj(dict);
		
		List<PreferentialDictUnitRel> list = new ArrayList<PreferentialDictUnitRel>();
		String[] unitUuids = input.getUnitUuids();
		if(ArrayUtils.isNotEmpty(unitUuids)){
			for(String unitUuid:unitUuids){
				PreferentialDictUnitRel rel = new PreferentialDictUnitRel();
				rel.setDictUuid(dict.getUuid());
				rel.setUnitUuid(unitUuid);
				super.setOptInfo(rel, BaseService.OPERATION_ADD);
				list.add(rel);
			}
		}
		preferentialDictUnitRelDao.batchSave(list);
		
		return "1";
		
	}
	
	/**
	 * 更新字典接口
	 * 关联保存相关的关联数据
	 * add by zhanghao
	 * @param input
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public String updateObj(PreferentialDictInput input){
		PreferentialDict dict = input.getDict(preferentialDictDao.getByUuid(input.getUuid()));
		super.setOptInfo(dict, BaseService.OPERATION_UPDATE);
		preferentialDictDao.updateObj(dict);
		
		preferentialDictUnitRelDao.removeByDictUuids(new String[]{dict.getUuid()});
		List<PreferentialDictUnitRel> list = new ArrayList<PreferentialDictUnitRel>();
		String[] unitUuids = input.getUnitUuids();
		if(ArrayUtils.isNotEmpty(unitUuids)){
			for(String unitUuid:unitUuids){
				PreferentialDictUnitRel rel = new PreferentialDictUnitRel();
				rel.setDictUuid(dict.getUuid());
				rel.setUnitUuid(unitUuid);
				super.setOptInfo(rel, BaseService.OPERATION_ADD);
				list.add(rel);
			}
		}
		preferentialDictUnitRelDao.batchSave(list);
		
		return "2";
	}
	
}
