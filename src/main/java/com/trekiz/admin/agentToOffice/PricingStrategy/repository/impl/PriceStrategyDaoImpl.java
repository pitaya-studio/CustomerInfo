package com.trekiz.admin.agentToOffice.PricingStrategy.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.PriceStrategyDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Repository
@Component
public class PriceStrategyDaoImpl extends BaseDaoImpl<PriceStrategy> implements PriceStrategyDao{
	
	/**
	 * 根据批发商id查询所有的渠道策略
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<PriceStrategy> getAllPriceStrategyByWholeSalerId(
			Long wholeSalerId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM price_strategy ");
		sbf.append("WHERE supplyId=? AND delFlag=? AND state=? ");
		List<PriceStrategy> list=this.findBySql(sbf.toString(),PriceStrategy.class, wholeSalerId.intValue(),0,1);
		return list;
	}
	
	/**
	 * 查询所有被使用的渠道等级和渠道类型
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<Map<String,Object>> getGradeAndType() {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM agent_price_strategy ");
		List<Map<String,Object>> list=this.findBySql(sbf.toString(),Map.class);
		return list;
	}
	
}
