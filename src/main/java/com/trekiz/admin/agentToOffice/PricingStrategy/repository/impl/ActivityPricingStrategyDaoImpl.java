package com.trekiz.admin.agentToOffice.PricingStrategy.repository.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PricingStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.PricingStrategyDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Repository
@Component
public class ActivityPricingStrategyDaoImpl extends BaseDaoImpl<PricingStrategy> implements PricingStrategyDao{

	@Override
	public List<PricingStrategy> getPricingStrategyList(String srcActivityId,String id) {
		String sql = "select a.* from  activity_pricingStrategy b,pricingStrategy a where b.travelactivityId = "+srcActivityId+" and b.activitygroupId = "+id+" and b.usageState = 0 and a.id = b.pricingStrategyId order by a.id";
		return this.findBySql(sql,PricingStrategy.class);
	}

	@Override
	public List<PricingStrategy> getPricingStrategyList(String srcActivityId) {
		String sql = "select a.* from  activity_pricingStrategy b,pricingStrategy a where b.travelactivityId = "+srcActivityId+" and b.usageState = 0 and a.id = b.pricingStrategyId order by a.id";
		return this.findBySql(sql,PricingStrategy.class);
	}
}
