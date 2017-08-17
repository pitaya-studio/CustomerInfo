package com.trekiz.admin.agentToOffice.PricingStrategy.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.PricingStrategy.entity.ActivityPricingStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PricingStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.ActivityPricingStrategyDao;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.PricingStrategyDao;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.repository.LogProductDao;
import com.trekiz.admin.modules.activity.service.sync.ActivityGroupSyncService;

@Service
public class ActivityPricingStrategyService {
	
	@Autowired
	private PricingStrategyDao pricingStrategyDao;
	@Autowired
	private ActivityPricingStrategyDao activityPricingStrategyDao;
	@Autowired
	private ActivityGroupSyncService activityGroupSyncService;
	@Autowired
	private LogProductDao logProductDao;

	public Map<String, String> getPricingStrategy(String srcActivityId, String id) {
		List<PricingStrategy> pricingStrategyList =pricingStrategyDao.getPricingStrategyList(srcActivityId, id);
		Map<String,String> pricingStrategys = new HashMap<String,String>();
		String adultPricingStrategy = "";
		String childrenPricingStrategy = "";
		String specialPricingStrategy = "";
		for(PricingStrategy entity:pricingStrategyList){
			if(entity.getPersonType()==0){//1成人  2  直减 3 折扣
				adultPricingStrategy = adultPricingStrategy + entity.getFavorableTypeStr()+":"+entity.getFavorableNum()+(entity.getFavorableType()==3?"%":"")+",";
			}else if(entity.getPersonType()==1){//1儿童
				childrenPricingStrategy = childrenPricingStrategy + entity.getFavorableTypeStr()+":"+entity.getFavorableNum()+(entity.getFavorableType()==3?"%":"")+",";
			}else if(entity.getPersonType()==2){//1特殊人群
				specialPricingStrategy = specialPricingStrategy + entity.getFavorableTypeStr()+":"+entity.getFavorableNum()+(entity.getFavorableType()==3?"%":"")+",";
			}
		}
		pricingStrategys.put("adultPricingStrategy", (adultPricingStrategy.length()>1?adultPricingStrategy.substring(0,adultPricingStrategy.length()-1):adultPricingStrategy));
		pricingStrategys.put("childrenPricingStrategy",  (childrenPricingStrategy.length()>1?childrenPricingStrategy.substring(0,childrenPricingStrategy.length()-1):childrenPricingStrategy));
		pricingStrategys.put("specialPricingStrategy",  (specialPricingStrategy.length()>1?specialPricingStrategy.substring(0,specialPricingStrategy.length()-1):specialPricingStrategy));
		return pricingStrategys;
	}
	
	public List<PricingStrategy> getActivityPrining(String srcActivityId, String id){
		return pricingStrategyDao.getPricingStrategyList(srcActivityId, id);
	}

	public void updateUsageState(String groupid, String activityid, Long id) {
		ActivityPricingStrategy entity = new ActivityPricingStrategy(new Long(activityid),new Long(groupid),id);
		activityPricingStrategyDao.updateObj(entity);
	}

	public Long addPricingStrategy(PricingStrategy strategyEntity) {
		pricingStrategyDao.saveObj(strategyEntity);
		return strategyEntity.getId();
	}

	public void saveActivityStrategy(ActivityPricingStrategy activityStrategy) {
		activityPricingStrategyDao.saveObj(activityStrategy);
	}

	public void changeQuauqPrice(String groupid, String activityid) {
		//获取团期信息
		ActivityGroup activityGroup = activityGroupSyncService.findById(new Long(groupid));
		//获取团期对应的价格策略
		List<PricingStrategy> strategys = pricingStrategyDao.getPricingStrategyList(activityid, groupid);
		
		BigDecimal adultPrice = activityGroup.getSettlementAdultPrice();
		BigDecimal childrenPrice = activityGroup.getSettlementcChildPrice();
		BigDecimal specialPrice = activityGroup.getSettlementSpecialPrice();
		
		int adultCount = 0;
		int childrenCount = 0;
		int specailCount = 0;
		
		for(PricingStrategy strategy:strategys){
			if(strategy.getPersonType() == 0 && adultPrice != null && adultPrice.compareTo(BigDecimal.ZERO) > 0){
				adultCount++;
				if(strategy.getFavorableType()==2){
					adultPrice = adultPrice.subtract(strategy.getFavorableNum());
					if(adultPrice.compareTo(BigDecimal.ZERO) < 0){
						adultPrice = new BigDecimal(0);
					}
				}else if(strategy.getFavorableType()==3){
					adultPrice = adultPrice.multiply(computeValue(strategy.getFavorableNum()));
				}
			}else if(strategy.getPersonType() == 1 && childrenPrice!= null && childrenPrice.compareTo(BigDecimal.ZERO) > 0){
				childrenCount++;
				if(strategy.getFavorableType()==2){
					childrenPrice = childrenPrice.subtract(strategy.getFavorableNum());
					if(childrenPrice.compareTo(BigDecimal.ZERO) < 0){
						childrenPrice = new BigDecimal(0);
					}
				}else if(strategy.getFavorableType()==3){
					childrenPrice = childrenPrice.multiply(computeValue(strategy.getFavorableNum()));
				}
			}else if(strategy.getPersonType() == 2 && specialPrice!=null && specialPrice.compareTo(BigDecimal.ZERO) > 0){
				specailCount++;
				if(strategy.getFavorableType()==2){
					specialPrice = specialPrice.subtract(strategy.getFavorableNum());
					if(specialPrice.compareTo(BigDecimal.ZERO) < 0){
						specialPrice = new BigDecimal(0);
					}
				}else if(strategy.getFavorableType()==3){
					specialPrice = specialPrice.multiply(computeValue(strategy.getFavorableNum()));
				}
			}
		}
		activityGroup.setQuauqAdultPrice(adultCount == 0?null:adultPrice);
		activityGroup.setQuauqChildPrice(childrenCount == 0?null:childrenPrice);
		activityGroup.setQuauqSpecialPrice(specailCount == 0?null:specialPrice);
		
		if(activityGroup.getIsT1()==0 && (adultCount != 0 || childrenCount != 0 || specailCount != 0)){
			activityGroup.setIsT1(1);
			logProductDao.updateBySql("update log_product set is_read = 1 where business_type = 2 and activity_id ="+activityid+" and group_id = "+groupid +" and field_name like '%Price'", null);
		}else if(activityGroup.getIsT1()==1 && adultCount == 0 && childrenCount == 0 && specailCount == 0){
			activityGroup.setIsT1(0);
			logProductDao.updateBySql("update log_product set is_read = 0 where business_type = 2 and activity_id ="+activityid+" and group_id = "+groupid +" and field_name like '%Price'", null);
		}
		
		
		//币种信息
		/*String currency_type = activityGroup.getCurrencyType();
		String[] currencys = currency_type.split(",");
		for(String currency:currencys){
			
		}*/
		
		activityGroupSyncService.updateObj(activityGroup);
	}
	
	private BigDecimal computeValue(BigDecimal val1){
		//strategy.getFavorableNum().divide(new BigDecimal("100"))
		BigDecimal temp = new BigDecimal("100");
		val1 = temp.subtract(val1);
		return val1.divide(new BigDecimal("100"));
	}
	
	/**
	 * 获取根据价格策略计算出的quauq价.
	 * @param groupid
	 * @param activityid
	 * @return
	 */
	public String getQuauqPrice(String groupid, String activityid,String inputName,BigDecimal srcPrice){
		
		
		//获取团期信息
		ActivityGroup activityGroup = activityGroupSyncService.findById(new Long(groupid));
		//获取团期对应的价格策略
		List<PricingStrategy> strategys = pricingStrategyDao.getPricingStrategyList(activityid, groupid);
		
		if(null != strategys && strategys.size() > 0) {
			for (PricingStrategy strategy : strategys) {
				if (strategy.getPersonType() == 0 && srcPrice != null && srcPrice.compareTo(BigDecimal.ZERO) > 0 && "settlementAdultPrice".equalsIgnoreCase(inputName)) {
					if (strategy.getFavorableType() == 2) {
						srcPrice = srcPrice.subtract(strategy.getFavorableNum());
					} else if (strategy.getFavorableType() == 3) {
						srcPrice = srcPrice.multiply(computeValue(strategy.getFavorableNum()));
					}
				} else if (strategy.getPersonType() == 1 && srcPrice != null && srcPrice.compareTo(BigDecimal.ZERO) > 0 && "settlementcChildPrice".equalsIgnoreCase(inputName)) {
					if (strategy.getFavorableType() == 2) {
						srcPrice = srcPrice.subtract(strategy.getFavorableNum());
					} else if (strategy.getFavorableType() == 3) {
						srcPrice = srcPrice.multiply(computeValue(strategy.getFavorableNum()));
					}
				} else if (strategy.getPersonType() == 2 && srcPrice != null && srcPrice.compareTo(BigDecimal.ZERO) > 0 && "settlementSpecialPrice".equalsIgnoreCase(inputName)) {
					if (strategy.getFavorableType() == 2) {
						srcPrice = srcPrice.subtract(strategy.getFavorableNum());
					} else if (strategy.getFavorableType() == 3) {
						srcPrice = srcPrice.multiply(computeValue(strategy.getFavorableNum()));
					}
				}
			}
		}else {
			srcPrice = new BigDecimal("0.001");
		}
				
		return srcPrice.toString();
	}
	
	/**
	 * 获取根据价格策略计算出的quauq价.
	 * @param activityid
	 * @param inputName
	 * @param srcPrice
	 * @return
	 */
	public String getQuauqPrice(String activityid,String inputName,BigDecimal srcPrice){

		//获取团期对应的价格策略
		List<PricingStrategy> strategys = pricingStrategyDao.getPricingStrategyList(activityid);

		if(null != strategys && strategys.size() > 0) {
			for(PricingStrategy strategy:strategys){
				if(strategy.getPersonType() == 0 && srcPrice != null && srcPrice.compareTo(BigDecimal.ZERO) > 0 && "settlementAdultPrice".equalsIgnoreCase(inputName)){
					if(strategy.getFavorableType()==2){
						srcPrice = srcPrice.subtract(strategy.getFavorableNum());
					}else if(strategy.getFavorableType()==3){
						srcPrice = srcPrice.multiply(computeValue(strategy.getFavorableNum()));
					}
				}else if(strategy.getPersonType() == 1 && srcPrice!= null && srcPrice.compareTo(BigDecimal.ZERO) > 0 && "settlementcChildPrice".equalsIgnoreCase(inputName)){
					if(strategy.getFavorableType()==2){
						srcPrice = srcPrice.subtract(strategy.getFavorableNum());
					}else if(strategy.getFavorableType()==3){
						srcPrice = srcPrice.multiply(computeValue(strategy.getFavorableNum()));
					}
				}else if(strategy.getPersonType() == 2 && srcPrice!=null && srcPrice.compareTo(BigDecimal.ZERO) > 0 && "settlementSpecialPrice".equalsIgnoreCase(inputName)){
					if(strategy.getFavorableType()==2){
						srcPrice = srcPrice.subtract(strategy.getFavorableNum());
					}else if(strategy.getFavorableType()==3){
						srcPrice = srcPrice.multiply(computeValue(strategy.getFavorableNum()));
					}
				}
			}
		}else {
			srcPrice = new BigDecimal("0.001");
		}

		return srcPrice.toString();
	}

	public boolean checkAdd(String proId, String groupId) {
		//获取团期信息
		ActivityGroup activityGroup = activityGroupSyncService.findById(new Long(groupId));
		
		BigDecimal adultPrice = activityGroup.getSettlementAdultPrice();
		BigDecimal childrenPrice = activityGroup.getSettlementcChildPrice();
		BigDecimal specialPrice = activityGroup.getSettlementSpecialPrice();
		if(adultPrice == null && childrenPrice == null && specialPrice == null){
			return false;
		}
		return true;
	}
	
	
}
