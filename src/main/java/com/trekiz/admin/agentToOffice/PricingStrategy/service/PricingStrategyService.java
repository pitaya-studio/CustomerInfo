package com.trekiz.admin.agentToOffice.PricingStrategy.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.AgentPriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.AgentPriceStrategyDao;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.PriceStrategyDao;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;

@Service
public class PricingStrategyService extends BaseService{
	@Autowired
	private PriceStrategyDao priceStrategyDao;
	@Autowired
	private AgentPriceStrategyDao agentPriceStrategyDao;
	
	
	public  Page<PriceStrategy> searchPriceStrategyBySupplyid(Page<PriceStrategy> page, Map<String,String> parmMap){
		DetachedCriteria dc = priceStrategyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		if(parmMap != null){
			if(parmMap.get("companyId") != null && parmMap.get("companyId").trim()!=""){
				dc.add(Restrictions.eq("supplyId", Long.parseLong(parmMap.get("companyId"))));
			}
			if(parmMap.get("fromArea") != null && parmMap.get("fromArea").trim() !=""){
				dc.add(Restrictions.like("fromAreaIds", "%,"+parmMap.get("fromArea")+",%"));
			}
			if(parmMap.get("targetArea") != null && parmMap.get("targetArea").trim() != ""){
				dc.add(Restrictions.like("targetAreaIds", "%,"+parmMap.get("targetArea")+",%"));
			}
			
			if(parmMap.get("travelType") != null && parmMap.get("travelType").trim() != ""){
				dc.add(Restrictions.like("travelTypeIds", "%,"+parmMap.get("travelType")+",%"));
			}
			if(parmMap.get("productType") != null && parmMap.get("productType") != ""){
				dc.add(Restrictions.like("activityTypeIds", "%,"+parmMap.get("productType")+",%"));
			}
			if(parmMap.get("productLevel") != null && parmMap.get("productLevel") != ""){
				dc.add(Restrictions.like("productLevelIds", "%,"+parmMap.get("productLevel")+",%"));
			}
			boolean initflag = false;
			DetachedCriteria agentPriceStrategy = DetachedCriteria.forClass(AgentPriceStrategy.class, "agentPriceStrategy");
			if(parmMap.get("agentType") != null && parmMap.get("agentType").trim() != ""){
				initflag = true;
				agentPriceStrategy.add(Restrictions.like("agentTypeIds", "%,"+parmMap.get("agentType")+",%"));
			}
			if(parmMap.get("agentLevel") != null && parmMap.get("agentLevel").trim() != ""){
				initflag = true;
				agentPriceStrategy.add(Restrictions.like("agentLevelIds", "%,"+parmMap.get("agentLevel")+",%"));
			}
			if(parmMap.get("favorableType") != null && parmMap.get("favorableType").trim() != ""){
				initflag = true;
				agentPriceStrategy.add(Restrictions.like("discountIds", "%,"+parmMap.get("favorableType")+",%"));
			}
			if(initflag){
				agentPriceStrategy.setProjection(Property.forName("priceStrategy"));
				dc.add(Property.forName("id").in(agentPriceStrategy));
			}
		}
		return priceStrategyDao.find(page,dc);
	}

	/**
	 * 根据查询条件查询是否存在价格策略
	 * @param companyId
	 * @param fromArea
	 * @param targetArea
	 * @param travelType
	 * @param productType
	 * @param productLevel
	 */
	public boolean existPriceStrategy(Long companyId, String fromArea,
			String targetArea, String travelType, String productType,
			String productLevel,Long StrategyId) {
		DetachedCriteria dc = priceStrategyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", BaseEntity.DEL_FLAG_NORMAL));
		if(companyId != null){
			dc.add(Restrictions.eq("supplyId",companyId));
		}
		if(StrategyId != null){
			dc.add(Restrictions.ne("id",StrategyId));
		}
		if(fromArea != null){
			dc.add(Restrictions.like("fromAreaIds", "%,"+fromArea+",%"));
		}
		if(targetArea != null){
			dc.add(Restrictions.like("targetAreaIds", "%,"+targetArea+",%"));
		}
		
		if(travelType != null){
			dc.add(Restrictions.like("travelTypeIds", "%,"+travelType+",%"));
		}
		if(productType != null){
			dc.add(Restrictions.like("activityTypeIds", "%,"+productType+",%"));
		}
		if(productLevel != null){
			dc.add(Restrictions.like("productLevelIds", "%,"+productLevel+",%"));
		}
		List<PriceStrategy> objList =priceStrategyDao.find(dc);
		if(objList != null && objList.size() > 0){
			return true;
		}
		return false;
	}

	public void add(PriceStrategy priceStrategy) {
		priceStrategyDao.saveObj(priceStrategy);
	}

	public List<PriceStrategy> getPriceStrategy(Long priceStrategyId) {
		DetachedCriteria dc = priceStrategyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("id",priceStrategyId));
		return priceStrategyDao.find(dc);
	}
	
	public List<PriceStrategy> getAllPriceStrategyByWholeSalerId(Long wholeSalerId){
		List<PriceStrategy> list = priceStrategyDao.getAllPriceStrategyByWholeSalerId(wholeSalerId);
		return list;
	}

	public PriceStrategy findPriceStrategy(Long priceStrategyId) {
		return priceStrategyDao.getById(priceStrategyId);
	}

	public void updatePriceStrate(PriceStrategy priceStratety) {
		priceStrategyDao.updateObj(priceStratety);
	}

	/**
	 * 查询某批发商下所有的渠道策略
	 * @param companyId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<PriceStrategy> findAllStrategyByCompanyId(Long companyId){
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM price_strategy WHERE supplyId=? AND delFlag=? ");
		List<PriceStrategy> list=priceStrategyDao.findBySql(sbf.toString(), PriceStrategy.class,companyId.intValue(),0);
		return list;
	}


	public void deleteAgentPriceStrates(Set<AgentPriceStrategy> agentPriceStrategySet) {
		for(AgentPriceStrategy priceStrategy:agentPriceStrategySet){
			agentPriceStrategyDao.deleteObj(priceStrategy);
		}
		
	}


}
