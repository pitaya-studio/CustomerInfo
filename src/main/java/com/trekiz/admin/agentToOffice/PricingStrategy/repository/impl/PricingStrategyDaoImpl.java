package com.trekiz.admin.agentToOffice.PricingStrategy.repository.impl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.ActivityPricingStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.ActivityPricingStrategyDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Repository
@Component
public class PricingStrategyDaoImpl extends BaseDaoImpl<ActivityPricingStrategy> implements ActivityPricingStrategyDao{


}
