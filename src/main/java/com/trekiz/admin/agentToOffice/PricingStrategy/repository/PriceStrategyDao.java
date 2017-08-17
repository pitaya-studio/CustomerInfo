package com.trekiz.admin.agentToOffice.PricingStrategy.repository;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PriceStrategy;
import com.trekiz.admin.common.persistence.BaseDao;

public interface PriceStrategyDao extends BaseDao<PriceStrategy>{
	/**
	 * 根据批发上id查询所有的价格策略
	 * @param wholeSalerId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<PriceStrategy> getAllPriceStrategyByWholeSalerId(Long wholeSalerId);
	
	/**
	 * 查询价格策略使用的所有的渠道等级和渠道类型
	 * @author chao.zhang@quauq.com
	 */
	public List<Map<String,Object>> getGradeAndType();
}
