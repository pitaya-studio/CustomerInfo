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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.preferential.entity.*;
import com.trekiz.admin.modules.preferential.dao.*;
import com.trekiz.admin.modules.preferential.service.*;
import com.trekiz.admin.modules.preferential.utils.PreferentialRuleUtil;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PreferentialDictUnitRelServiceImpl  extends BaseService implements PreferentialDictUnitRelService{
	@Autowired
	private PreferentialDictUnitRelDao preferentialDictUnitRelDao;
	@Autowired
	private PreferentialDictDao preferentialDictDao;
	@Autowired
	private PreferentialUnitDao preferentialUnitDao;

	public void save (PreferentialDictUnitRel preferentialDictUnitRel){
		super.setOptInfo(preferentialDictUnitRel, BaseService.OPERATION_ADD);
		preferentialDictUnitRelDao.saveObj(preferentialDictUnitRel);
	}
	
	public void update (PreferentialDictUnitRel preferentialDictUnitRel){
		super.setOptInfo(preferentialDictUnitRel, BaseService.OPERATION_UPDATE);
		preferentialDictUnitRelDao.updateObj(preferentialDictUnitRel);
	}
	
	public PreferentialDictUnitRel getById(java.lang.Integer value) {
		return preferentialDictUnitRelDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PreferentialDictUnitRel obj = preferentialDictUnitRelDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PreferentialDictUnitRel> find(Page<PreferentialDictUnitRel> page, PreferentialDictUnitRel preferentialDictUnitRel) {
		DetachedCriteria dc = preferentialDictUnitRelDao.createDetachedCriteria();
		
	   	if(preferentialDictUnitRel.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialDictUnitRel.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialDictUnitRel.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialDictUnitRel.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictUnitRel.getDictUuid())){
			dc.add(Restrictions.eq("dictUuid", preferentialDictUnitRel.getDictUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictUnitRel.getUnitUuid())){
			dc.add(Restrictions.eq("unitUuid", preferentialDictUnitRel.getUnitUuid()));
		}
	   	if(preferentialDictUnitRel.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialDictUnitRel.getCreateBy()));
	   	}
		if(preferentialDictUnitRel.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialDictUnitRel.getCreateDate()));
		}
	   	if(preferentialDictUnitRel.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialDictUnitRel.getUpdateBy()));
	   	}
		if(preferentialDictUnitRel.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialDictUnitRel.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialDictUnitRel.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialDictUnitRel.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialDictUnitRelDao.find(page, dc);
	}
	
	public List<PreferentialDictUnitRel> find( PreferentialDictUnitRel preferentialDictUnitRel) {
		DetachedCriteria dc = preferentialDictUnitRelDao.createDetachedCriteria();
		
	   	if(preferentialDictUnitRel.getId()!=null){
	   		dc.add(Restrictions.eq("id", preferentialDictUnitRel.getId()));
	   	}
		if (StringUtils.isNotEmpty(preferentialDictUnitRel.getUuid())){
			dc.add(Restrictions.eq("uuid", preferentialDictUnitRel.getUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictUnitRel.getDictUuid())){
			dc.add(Restrictions.eq("dictUuid", preferentialDictUnitRel.getDictUuid()));
		}
		if (StringUtils.isNotEmpty(preferentialDictUnitRel.getUnitUuid())){
			dc.add(Restrictions.eq("unitUuid", preferentialDictUnitRel.getUnitUuid()));
		}
	   	if(preferentialDictUnitRel.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", preferentialDictUnitRel.getCreateBy()));
	   	}
		if(preferentialDictUnitRel.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", preferentialDictUnitRel.getCreateDate()));
		}
	   	if(preferentialDictUnitRel.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", preferentialDictUnitRel.getUpdateBy()));
	   	}
		if(preferentialDictUnitRel.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", preferentialDictUnitRel.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(preferentialDictUnitRel.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", preferentialDictUnitRel.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return preferentialDictUnitRelDao.find(dc);
	}
	
	public PreferentialDictUnitRel getByUuid(String uuid) {
		return preferentialDictUnitRelDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PreferentialDictUnitRel preferentialDictUnitRel = getByUuid(uuid);
		preferentialDictUnitRel.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(preferentialDictUnitRel);
	}
	
	public List<Map<String, String>> getUnitsByDictUuid(String uuid) {
		return preferentialDictUnitRelDao.getUnitsByDictUuid(uuid);
	}
	
	public String findNamesByDictUuidAndUnitUuid(String dictUuid, String unitUuid) {
		PreferentialDict preferentialDict = preferentialDictDao.getByUuid(dictUuid);
		PreferentialUnit preferentialUnit = preferentialUnitDao.getByUuid(unitUuid);
		
		if(preferentialDict == null || preferentialUnit == null) {
			return "";
		}
		
		return findDescByNamesAndDataType(preferentialDict.getName(), preferentialUnit.getName(), preferentialDict.getDataType());
	}
	
	public String findDescByNamesAndDataType(String dictName, String unitName, int dataType) {
		//返回日期格式
		if(dataType == PreferentialDict.DATA_TYPE_DATE) {
			return PreferentialRuleUtil.dictNameJoinUnitNameAsDate(dictName, unitName);
		}
		
		//返回数值格式
		return PreferentialRuleUtil.dictNameJoinUnitName(dictName, unitName);
	}
	
	/**
	 * 根据字典uuid和单位uuid,获取模板的html代码(type表示因果类型)
	*<p>Title: getOutHtmlByDictUuidAndUnitUuid</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午10:23:09
	* @throws
	 */
	public String getOutHtmlByDictUuidAndUnitUuid(String dictUuid, String unitUuid, int type) {
		PreferentialDict preferentialDict = preferentialDictDao.getByUuid(dictUuid);
		PreferentialUnit preferentialUnit = preferentialUnitDao.getByUuid(unitUuid);
		
		if(preferentialDict == null || preferentialUnit == null) {
			return "";
		}
		
		return getOutHtmlByNamesAndDataType(preferentialDict.getName(), preferentialUnit.getName(), preferentialDict.getDataType(), type);
	}
	
	/**
	 * 根据字典名称和单位名称,获取模板的html代码(type表示因果类型)
	*<p>Title: getOutDataByNamesAndDataType</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午10:22:19
	* @throws
	 */
	public String getOutHtmlByNamesAndDataType(String dictName, String unitName, int dataType, int type) {
		//返回日期格式
		if(dataType == PreferentialDict.DATA_TYPE_DATE) {
			return PreferentialRuleUtil.dictNameJoinUnitNameAsDateInput(dictName, unitName);
		}
		
		//返回数值格式
		return PreferentialRuleUtil.dictNameJoinUnitNameByType(dictName, unitName, type);
	}
	
}
