/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.epriceDistribution.service.impl;

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
import com.trekiz.admin.modules.epriceDistribution.dao.EstimatePriceDistributionDao;
import com.trekiz.admin.modules.epriceDistribution.entity.EstimatePriceDistribution;
import com.trekiz.admin.modules.epriceDistribution.input.EstimatePriceDistributionInput;
import com.trekiz.admin.modules.epriceDistribution.query.EstimatePriceDistributionQuery;
import com.trekiz.admin.modules.epriceDistribution.service.EstimatePriceDistributionService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class EstimatePriceDistributionServiceImpl  extends BaseService implements EstimatePriceDistributionService{
	@Autowired
	private EstimatePriceDistributionDao estimatePriceDistributionDao;

	public void save (EstimatePriceDistribution estimatePriceDistribution){
		super.setOptInfo(estimatePriceDistribution, BaseService.OPERATION_ADD);
		estimatePriceDistributionDao.saveObj(estimatePriceDistribution);
	}
	
	public void save (EstimatePriceDistributionInput estimatePriceDistributionInput){
		EstimatePriceDistribution estimatePriceDistribution = estimatePriceDistributionInput.getEstimatePriceDistribution();
		super.setOptInfo(estimatePriceDistribution, BaseService.OPERATION_ADD);
		estimatePriceDistributionDao.saveObj(estimatePriceDistribution);
	}
	
	public void update (EstimatePriceDistribution estimatePriceDistribution){
		super.setOptInfo(estimatePriceDistribution, BaseService.OPERATION_UPDATE);
		estimatePriceDistributionDao.updateObj(estimatePriceDistribution);
	}
	
	public EstimatePriceDistribution getById(java.lang.Integer value) {
		return estimatePriceDistributionDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		EstimatePriceDistribution obj = estimatePriceDistributionDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<EstimatePriceDistribution> find(Page<EstimatePriceDistribution> page, EstimatePriceDistributionQuery estimatePriceDistributionQuery) {
		DetachedCriteria dc = estimatePriceDistributionDao.createDetachedCriteria();
		
	   	if(estimatePriceDistributionQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", estimatePriceDistributionQuery.getId()));
	   	}
	   	if(estimatePriceDistributionQuery.getEstimateBaseId()!=null){
	   		dc.add(Restrictions.eq("estimateBaseId", estimatePriceDistributionQuery.getEstimateBaseId()));
	   	}
	   	if(estimatePriceDistributionQuery.getEstimateRecordId()!=null){
	   		dc.add(Restrictions.eq("estimateRecordId", estimatePriceDistributionQuery.getEstimateRecordId()));
	   	}
	   	if(estimatePriceDistributionQuery.getOpManagerId()!=null){
	   		dc.add(Restrictions.eq("opManagerId", estimatePriceDistributionQuery.getOpManagerId()));
	   	}
	   	if(estimatePriceDistributionQuery.getOpId()!=null){
	   		dc.add(Restrictions.eq("opId", estimatePriceDistributionQuery.getOpId()));
	   	}
	   	if(estimatePriceDistributionQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", estimatePriceDistributionQuery.getType()));
	   	}
		if(estimatePriceDistributionQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", estimatePriceDistributionQuery.getCreateDate()));
		}
		if(estimatePriceDistributionQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", estimatePriceDistributionQuery.getUpdateDate()));
		}
	   	if(estimatePriceDistributionQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", estimatePriceDistributionQuery.getCreateBy()));
	   	}
	   	if(estimatePriceDistributionQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", estimatePriceDistributionQuery.getUpdateBy()));
	   	}
		if (StringUtils.isNotEmpty(estimatePriceDistributionQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", estimatePriceDistributionQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return estimatePriceDistributionDao.find(page, dc);
	}
	
	public List<EstimatePriceDistribution> find( EstimatePriceDistributionQuery estimatePriceDistributionQuery) {
		DetachedCriteria dc = estimatePriceDistributionDao.createDetachedCriteria();
		
	   	if(estimatePriceDistributionQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", estimatePriceDistributionQuery.getId()));
	   	}
	   	if(estimatePriceDistributionQuery.getEstimateBaseId()!=null){
	   		dc.add(Restrictions.eq("estimateBaseId", estimatePriceDistributionQuery.getEstimateBaseId()));
	   	}
	   	if(estimatePriceDistributionQuery.getEstimateRecordId()!=null){
	   		dc.add(Restrictions.eq("estimateRecordId", estimatePriceDistributionQuery.getEstimateRecordId()));
	   	}
	   	if(estimatePriceDistributionQuery.getOpManagerId()!=null){
	   		dc.add(Restrictions.eq("opManagerId", estimatePriceDistributionQuery.getOpManagerId()));
	   	}
	   	if(estimatePriceDistributionQuery.getOpId()!=null){
	   		dc.add(Restrictions.eq("opId", estimatePriceDistributionQuery.getOpId()));
	   	}
	   	if(estimatePriceDistributionQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", estimatePriceDistributionQuery.getType()));
	   	}
		if(estimatePriceDistributionQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", estimatePriceDistributionQuery.getCreateDate()));
		}
		if(estimatePriceDistributionQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", estimatePriceDistributionQuery.getUpdateDate()));
		}
	   	if(estimatePriceDistributionQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", estimatePriceDistributionQuery.getCreateBy()));
	   	}
	   	if(estimatePriceDistributionQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", estimatePriceDistributionQuery.getUpdateBy()));
	   	}
		if (StringUtils.isNotEmpty(estimatePriceDistributionQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", estimatePriceDistributionQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return estimatePriceDistributionDao.find(dc);
	}
	
	public EstimatePriceDistribution getByUuid(String uuid) {
		return estimatePriceDistributionDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		EstimatePriceDistribution estimatePriceDistribution = getByUuid(uuid);
		estimatePriceDistribution.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(estimatePriceDistribution);
	}
}
