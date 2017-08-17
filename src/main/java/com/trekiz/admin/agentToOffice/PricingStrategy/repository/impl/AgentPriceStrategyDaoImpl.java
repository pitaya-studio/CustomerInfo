package com.trekiz.admin.agentToOffice.PricingStrategy.repository.impl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.AgentPriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.AgentPriceStrategyDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Repository
@Component
public class AgentPriceStrategyDaoImpl extends BaseDaoImpl<AgentPriceStrategy> implements AgentPriceStrategyDao{

	@Override
	public void deleteObj(AgentPriceStrategy priceStrategy) {
		getSession().delete(priceStrategy);
	}

}
