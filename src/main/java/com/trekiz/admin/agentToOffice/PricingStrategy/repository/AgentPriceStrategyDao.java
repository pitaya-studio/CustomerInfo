package com.trekiz.admin.agentToOffice.PricingStrategy.repository;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.AgentPriceStrategy;
import com.trekiz.admin.common.persistence.BaseDao;

public interface AgentPriceStrategyDao extends BaseDao<AgentPriceStrategy>{

	void deleteObj(AgentPriceStrategy priceStrategy);

}
