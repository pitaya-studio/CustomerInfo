/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.service.impl;

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
import com.trekiz.admin.modules.mtourCommon.dao.CountryLineDao;
import com.trekiz.admin.modules.mtourCommon.entity.CountryLine;
import com.trekiz.admin.modules.mtourCommon.input.CountryLineInput;
import com.trekiz.admin.modules.mtourCommon.query.CountryLineQuery;
import com.trekiz.admin.modules.mtourCommon.service.CountryLineService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CountryLineServiceImpl  extends BaseService implements CountryLineService{
	@Autowired
	private CountryLineDao countryLineDao;

	public void save (CountryLine countryLine){
		super.setOptInfo(countryLine, BaseService.OPERATION_ADD);
		countryLineDao.saveObj(countryLine);
	}
	
	public void save (CountryLineInput countryLineInput){
		CountryLine countryLine = countryLineInput.getCountryLine();
		super.setOptInfo(countryLine, BaseService.OPERATION_ADD);
		countryLineDao.saveObj(countryLine);
	}
	
	public void update (CountryLine countryLine){
		super.setOptInfo(countryLine, BaseService.OPERATION_UPDATE);
		countryLineDao.updateObj(countryLine);
	}
	
	public CountryLine getById(java.lang.Integer value) {
		return countryLineDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		CountryLine obj = countryLineDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<CountryLine> find(Page<CountryLine> page, CountryLineQuery countryLineQuery) {
		DetachedCriteria dc = countryLineDao.createDetachedCriteria();
		
	   	if(countryLineQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", countryLineQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(countryLineQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", countryLineQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getCountryUuid())){
			dc.add(Restrictions.eq("countryUuid", countryLineQuery.getCountryUuid()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getCountryName())){
			dc.add(Restrictions.eq("countryName", countryLineQuery.getCountryName()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getCountryShort())){
			dc.add(Restrictions.eq("countryShort", countryLineQuery.getCountryShort()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getRemark())){
			dc.add(Restrictions.eq("remark", countryLineQuery.getRemark()));
		}
	   	if(countryLineQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", countryLineQuery.getCreateBy()));
	   	}
		if(countryLineQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", countryLineQuery.getCreateDate()));
		}
	   	if(countryLineQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", countryLineQuery.getUpdateBy()));
	   	}
		if(countryLineQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", countryLineQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", countryLineQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return countryLineDao.find(page, dc);
	}
	
	public List<CountryLine> find( CountryLineQuery countryLineQuery) {
		DetachedCriteria dc = countryLineDao.createDetachedCriteria();
		
	   	if(countryLineQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", countryLineQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(countryLineQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", countryLineQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getCountryUuid())){
			dc.add(Restrictions.eq("countryUuid", countryLineQuery.getCountryUuid()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getCountryName())){
			dc.add(Restrictions.eq("countryName", countryLineQuery.getCountryName()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getCountryShort())){
			dc.add(Restrictions.eq("countryShort", countryLineQuery.getCountryShort()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getRemark())){
			dc.add(Restrictions.eq("remark", countryLineQuery.getRemark()));
		}
	   	if(countryLineQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", countryLineQuery.getCreateBy()));
	   	}
		if(countryLineQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", countryLineQuery.getCreateDate()));
		}
	   	if(countryLineQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", countryLineQuery.getUpdateBy()));
	   	}
		if(countryLineQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", countryLineQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(countryLineQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", countryLineQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return countryLineDao.find(dc);
	}
	
	public CountryLine getByUuid(String uuid) {
		return countryLineDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		CountryLine countryLine = getByUuid(uuid);
		countryLine.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(countryLine);
	}
	
	public boolean batchDelete(String[] uuids) {
		return countryLineDao.batchDelete(uuids);
	}
	
}
