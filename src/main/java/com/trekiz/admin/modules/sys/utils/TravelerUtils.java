/**
 *
 */
package com.trekiz.admin.modules.sys.utils;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerDao;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.island.dao.IslandTravelerDao;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.CostchangeDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户工具类
 * 
 * @author zj
 * @version 2013-11-19
 */
public class TravelerUtils extends BaseService {

	@SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(TravelerUtils.class);

	private static TravelerDao travelerDao = SpringContextHolder
			.getBean(TravelerDao.class);
	private static HotelTravelerDao hotelTravelerDao = SpringContextHolder
			.getBean(HotelTravelerDao.class);
	private static IslandTravelerDao islandTravelerDao = SpringContextHolder
			.getBean(IslandTravelerDao.class);
	private static CostchangeDao costchangeDao = SpringContextHolder
			.getBean(CostchangeDao.class);
	private static MoneyAmountDao moneyAmountDao = SpringContextHolder.getBean(MoneyAmountDao.class);
	private static ProductOrderCommonDao productOrderCommonDao = SpringContextHolder.getBean(ProductOrderCommonDao.class);
	private static CurrencyDao currencyDao = SpringContextHolder.getBean(CurrencyDao.class);
	
	private static ITravelActivityService iTravelActivityService = SpringContextHolder.getBean("travelActivitySyncService");
	/**
	 * 查询游客姓名 add by chy 2014年11月11日14:35:12
	 * 
	 * @param travelerId
	 * @return
	 */
	public static String getTravelerNameById(Long travelerId) {
		if(travelerId==null ){
			return "";
		}
		/*游客/定金/团队 id ，如果是游客改价 travelerid存储游客的id 如为订金改价存储-1 团队改价 存储 0*/
		if(travelerId == -1){ // 定金
			return "订金";
		}else if(travelerId == 0){ //团队
			return "团队";
		}else if(travelerId == -2){ //签证费
			return "签证费";
		}
		Traveler traveler = travelerDao.findOne(travelerId);
		if(traveler != null) {
			return traveler.getName();
		}
		return "";
	}
	public static String getSysTravelerNameById(Long travelerId,String orderType) {
		if(travelerId==null ){
			return "";
		}
		/*游客/定金/团队 id ，如果是游客改价 travelerid存储游客的id 如为订金改价存储-1 团队改价 存储 0*/
		if(travelerId == -1){ // 定金
			return "订金";
		}else if(travelerId == 0){ //团队
			return "团队";
		}else if(travelerId == -2){ //签证费
			return "签证费";
		}
		String name="";
		switch(orderType){
			case "11":
			HotelTraveler traveler = hotelTravelerDao.getById(travelerId.intValue());
			name = traveler.getName();
				break;
			case "12":
			IslandTraveler t = islandTravelerDao.getById(travelerId.intValue());
			name = t.getName();
				break;
		default:
			HotelTraveler tr = hotelTravelerDao.getById(travelerId.intValue());
			name = tr.getName();
		}
		return name;
	}

	public static List<Map<String, String>> getTravelerList() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String sql = "select distinct name id,name from (" +
				" select t.id,t.`name` from traveler t, productorder o, travelactivity p" +
				" where t.orderId = o.id and o.productId = p.id" +
				" and t.order_type in (1, 2, 3, 4, 5, 10)" +
				" and p.proCompany = " + companyId +
				" UNION" +
				" select t.id,t.`name` from traveler t, visa_order o, visa_products p" +
				" where t.orderId = o.id and o.visa_product_id = p.id" +
				" and t.order_type = 6" +
				" and p.proCompanyId = " + companyId +
				" UNION" +
				" select t.id,t.`name` from traveler t, airticket_order o, activity_airticket p" +
				" where t.orderId = o.id and o.airticket_id = p.id" +
				" and t.order_type = 7 " +
				" and p.proCompany = " + companyId + ") t;";
		return travelerDao.findBySql(sql, Map.class);
	}
	
	public static Boolean getChangedCount(){
		return iTravelActivityService.getChangedCount();
	}
	
	public static Integer hasChanged(String groupId,String srcId){
		return iTravelActivityService.hasChanged(groupId,srcId);
	}

	/**
	 * 改价--其他费用
	 * @param travelerId 游客id
	 * @param status 审批状态
	 * @return
	 */
	public static List<Map<String, Object>> getCosts(Long travelerId, Integer status) {
		String sql = "SELECT " +
				" sum(costSum) sum, " +
				" price_currency currencyId " +
				"FROM " +
				" costchange c " +
				"WHERE " +
				" c.travelerId = ? ";
		if (status == null) {
			sql += " and (c.review_uuid is null or c.review_uuid = '') GROUP BY c.price_currency";
			return costchangeDao.findBySql(sql, Map.class, travelerId);
		} else {
			sql += " and c.review_uuid is not null and c.review_uuid <> '' " +
					"AND c.status = ?  GROUP BY c.price_currency";
			return costchangeDao.findBySql(sql, Map.class, travelerId, status);
		}
	}

	public static List<Map<String, Object>> getCosts4Review(String reviewUuid) {
		String sql = "SELECT " +
				" sum(costSum) sum, " +
				" price_currency currencyId, r.product_type productType, r.order_id orderId " +
				"FROM " +
				" costchange c, " +
				" review_new r " +
				"WHERE " +
				" c.review_uuid = r.id " +
				"AND r.id = ? " +
				"GROUP BY " +
				" c.price_currency";
		List<Map<String, Object>> list = costchangeDao.findBySql(sql, Map.class, reviewUuid);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> map : list) {
				String orderId = map.get("orderId").toString();
				String orderType = map.get("productType").toString();
				String sum = map.get("sum").toString();
				String currencyId = map.get("currencyId").toString();
				if (Context.ORDER_STATUS_LOOSE.equals(orderType)) {
					ProductOrderCommon orderCommon = productOrderCommonDao.findOne(Long.parseLong(orderId));
					if (orderCommon.getPriceType() == 2) {
						Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
						Double endSum = getTravelerChargeRate(orderCommon, currency, sum);
						map.put("sum", endSum);
					}
				}
			}
		}
		return list;
	}

	// 改前金额的其他费用
    public static List<Map<String, Object>> getCosts2(Long travelerId, Integer status, String createDate) {
        String sql = "SELECT " +
                " SUM(costSum) sum, " +
                " price_currency currencyId " +
                "FROM " +
                " costchange c, " +
                " review_new r " +
                "WHERE " +
                " c.travelerId = r.traveller_id " +
                " and c.review_uuid = r.id " +
                " and c.review_uuid is not null and c.review_uuid <> '' " +
                " AND c.travelerId = ? " +
                " AND c. STATUS = ? " +
                " AND r.process_type = 10 " +
                " AND r.create_date >= ? " +
                " GROUP BY c.price_currency";
        return costchangeDao.findBySql(sql, Map.class, travelerId, status, createDate);
    }

	// 其他费用服务费
	public static List<Map<String, Object>> getCosts4ServiceFee(Long travelerId, Integer status, String createDate) {
		String sql = "SELECT " +
				" costSum sum, " +
				" price_currency currencyId " +
				"FROM " +
				" costchange c, " +
				" review_new r " +
				"WHERE " +
				" c.travelerId = r.traveller_id " +
				" and c.review_uuid = r.id " +
				" and c.review_uuid is not null and c.review_uuid <> '' " +
				" AND c.travelerId = ? " +
				" AND c. STATUS = ? " +
				" AND r.process_type = 10 " +
				" AND r.create_date >= ? " +
				"UNION " +
				" SELECT " +
				"  costSum sum, " +
				"  price_currency currencyId " +
				" FROM " +
				"  costchange c " +
				" WHERE " +
				"  c.travelerId = ? " +
				" AND ( " +
				"  c.review_uuid IS NULL " +
				"  OR c.review_uuid = '' " +
				" )";
		return costchangeDao.findBySql(sql, Map.class, travelerId, status, createDate, travelerId);
	}

	/**
	 * 改价审批通过后的当前应收价
	 * @param travelerId 游客id
	 * @return
	 */
	public static List<Map<String, Object>> getMoneyAfterCP(Long travelerId) {
		Traveler traveler = travelerDao.findById(travelerId);
		//游客结算价
		List<MoneyAmount> payMoneyList = moneyAmountDao.findAmountBySerialNum(traveler.getPayPriceSerialNum());
//		//订单修改添加的其他费用
//		List<Map<String, Object>> otherList = getCosts(travelerId, null);
//		//审批通过的改价费用
//		List<Map<String, Object>> costList = getCosts(travelerId, 2);

		Map<String, Object> moneyMap = new HashMap<String, Object>();
		for (MoneyAmount m : payMoneyList) {
			String currencyId = m.getCurrencyId().toString();
			BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
			moneyMap.put(currencyId, priceSum.add(new BigDecimal(m.getAmount().toString())));
		}

//		for (Map<String, Object> map : otherList) {
//			String currencyId = map.get("currencyId").toString();
//			BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
//			moneyMap.put(currencyId, priceSum.add(new BigDecimal(map.get("sum").toString())));
//		}

//		for (Map<String, Object> map : costList) {
//			String currencyId = map.get("currencyId").toString();
//			BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
//			moneyMap.put(currencyId, priceSum.add(new BigDecimal(map.get("sum").toString())));
//		}

		List<Map<String, Object>> moneyList = new ArrayList<Map<String, Object>>();
		for (Map.Entry<String, Object> entry : moneyMap.entrySet()) {
			Map<String, Object> oneMap = new HashMap<String, Object>();
			oneMap.put("key", entry.getKey());
			oneMap.put("value", entry.getValue());
			moneyList.add(oneMap);
		}

		return moneyList;
	}

    /**
     * 改价审批通过前的当前应收价
     * @param travelerId 游客id
     * @return
     */
    public static List<Map<String, Object>> getMoneyBeforeCP(Long travelerId, Long orderId, String createDate) {
        Traveler traveler = travelerDao.findById(travelerId);
        //游客结算价
        List<MoneyAmount> payMoneyList = moneyAmountDao.findAmountBySerialNum(traveler.getPayPriceSerialNum());
        //订单修改添加的其他费用
//        List<Map<String, Object>> otherList = getCosts(travelerId, null);
        //审批通过的改价费用
        List<Map<String, Object>> costList = getCosts2(travelerId, 2, createDate);

		Map<String, Object> moneyMap = new HashMap<String, Object>();
        for (MoneyAmount m : payMoneyList) {
            String currencyId = m.getCurrencyId().toString();
            BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
            moneyMap.put(currencyId, priceSum.add(new BigDecimal(m.getAmount().toString())));
        }

//        for (Map<String, Object> map : otherList) {
//            String currencyId = map.get("currencyId").toString();
//            BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
//            moneyMap.put(currencyId, priceSum.add(new BigDecimal(map.get("sum").toString())));
//        }

        for (Map<String, Object> map : costList) {
            String currencyId = map.get("currencyId").toString();
            BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
            moneyMap.put(currencyId, priceSum.subtract(new BigDecimal(map.get("sum").toString())));
        }

        // 减去服务费
		ProductOrderCommon orderCommon = productOrderCommonDao.findOne(orderId);
		// 改价费用
		List<Map<String, Object>> costs4ServiceFee = getCosts4ServiceFee(travelerId, 2, createDate);
		for (Map<String, Object> map : costs4ServiceFee) {
			String currencyId = map.get("currencyId").toString();
			String sum = map.get("sum").toString();

			Currency currency = currencyDao.findOne(Long.parseLong(currencyId));
			Double serviceFee = getServiceFee(orderCommon, currency, sum);
			BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
			BigDecimal result = priceSum.subtract(new BigDecimal(serviceFee.toString())).setScale(2, BigDecimal.ROUND_CEILING);
			if (result.equals(new BigDecimal("0.00"))) {
				moneyMap.remove(currencyId);
			} else {
				moneyMap.put(currencyId, result);
			}
		}

        List<Map<String, Object>> moneyList = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, Object> entry : moneyMap.entrySet()) {
            Map<String, Object> oneMap = new HashMap<String, Object>();
            oneMap.put("key", entry.getKey());
            oneMap.put("value", entry.getValue());
            moneyList.add(oneMap);
        }

        return moneyList;
    }

	/**
	 * 获取游客单一币种其他费用
	 * @author yakun.bai
	 * @Date 2016-9-5
	 */
	public static Double getTravelerChargeRate(ProductOrderCommon order, Currency currency, String amount) {
		Double travlerPrice = Double.parseDouble(amount);
		// 如果单笔金额为负值或0，则不添加服务费
		if (travlerPrice < 0) {
			Double tempPrice = 0.00;
			Integer quauqOtherChargeType = order.getQuauqOtherChargeType();
			BigDecimal quauqOtherChargeRate = order.getQuauqOtherChargeRate();
			Integer partnerOtherChargeType = order.getPartnerOtherChargeType();
			BigDecimal partnerOtherChargeRate = order.getPartnerOtherChargeRate();
			if (quauqOtherChargeType == 0) {
				tempPrice = travlerPrice * quauqOtherChargeRate.doubleValue();
			} else {
				tempPrice = quauqOtherChargeRate.divide(currency.getConvertLowest(), 2).doubleValue();
			}
			if (partnerOtherChargeType == 0) {
				tempPrice = tempPrice + travlerPrice * partnerOtherChargeRate.doubleValue();
			} else {
				tempPrice = tempPrice + partnerOtherChargeRate.divide(currency.getConvertLowest(), 2).doubleValue();
			}
			return travlerPrice + tempPrice;
		} else {
			return travlerPrice;
		}
	}
	
	/**
	 * 服务费
	 * @param order 订单
	 * @param currency 币种
	 * @param sum 其他费用
	 * @return
	 */
	public static Double getServiceFee(ProductOrderCommon order, Currency currency, String sum) {
		Double travlerPrice = Double.parseDouble(sum);
		// 如果单笔金额为负值或0，则不添加服务费
        Double tempPrice = 0.00;
		if (travlerPrice < 0) {
			Integer quauqOtherChargeType = order.getQuauqOtherChargeType();
			BigDecimal quauqOtherChargeRate = order.getQuauqOtherChargeRate();
			Integer partnerOtherChargeType = order.getPartnerOtherChargeType();
			BigDecimal partnerOtherChargeRate = order.getPartnerOtherChargeRate();
			if (quauqOtherChargeType != null && quauqOtherChargeType == 0) {
				tempPrice = travlerPrice * quauqOtherChargeRate.doubleValue();
			} else if (quauqOtherChargeType != null) {
				tempPrice = quauqOtherChargeRate.divide(currency.getConvertLowest(), 2).doubleValue();
			}
			if (partnerOtherChargeType != null && partnerOtherChargeType == 0) {
				tempPrice = tempPrice + travlerPrice * partnerOtherChargeRate.doubleValue();
			} else if (partnerOtherChargeType != null) {
				tempPrice = tempPrice + partnerOtherChargeRate.divide(currency.getConvertLowest(), 2).doubleValue();
			}
			return tempPrice;
		} else {
			return tempPrice;
		}
	}
	
}
