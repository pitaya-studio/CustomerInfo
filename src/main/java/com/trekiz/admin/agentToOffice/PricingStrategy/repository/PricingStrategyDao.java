package com.trekiz.admin.agentToOffice.PricingStrategy.repository;

import java.util.List;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PricingStrategy;
import com.trekiz.admin.common.persistence.BaseDao;

public interface PricingStrategyDao extends BaseDao<PricingStrategy>{
	List<PricingStrategy> getPricingStrategyList(String srcActivityId, String id);

	List<PricingStrategy> getPricingStrategyList(String srcActivityId);
}

