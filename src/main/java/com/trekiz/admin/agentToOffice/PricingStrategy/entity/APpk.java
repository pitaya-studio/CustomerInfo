package com.trekiz.admin.agentToOffice.PricingStrategy.entity;

import java.io.Serializable;

class APpk implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long travelactivityId;
	private Long activitygroupId;
	private Long pricingStrategyId;
	public Long getTravelactivityId() {
		return travelactivityId;
	}
	public void setTravelactivityId(Long travelactivityId) {
		this.travelactivityId = travelactivityId;
	}
	public Long getActivitygroupId() {
		return activitygroupId;
	}
	public void setActivitygroupId(Long activitygroupId) {
		this.activitygroupId = activitygroupId;
	}
	public Long getPricingStrategyId() {
		return pricingStrategyId;
	}
	public void setPricingStrategyId(Long pricingStrategyId) {
		this.pricingStrategyId = pricingStrategyId;
	}
	
	@Override  
    public boolean equals(Object obj) {  
        if(obj instanceof APpk){  
        	APpk pk = (APpk)obj;  
            if(this.travelactivityId==pk.getTravelactivityId()&&this.activitygroupId.equals(pk.getActivitygroupId())&&this.pricingStrategyId.equals(pk.getPricingStrategyId()))  
                return true;  
        }  
        return false;  
    }  
  
    @Override  
    public int hashCode() {  
        return (""+(this.activitygroupId==null?"":this.activitygroupId)+(this.travelactivityId==null?"":this.activitygroupId)+(this.pricingStrategyId==null?"":this.pricingStrategyId)).hashCode();  
    }  
}
