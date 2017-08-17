package com.trekiz.admin.modules.cost.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cost.entity.CostRecordHotel;
import com.trekiz.admin.modules.cost.entity.CostRecordIsland;
import com.trekiz.admin.modules.cost.entity.HotelGroupView;
import com.trekiz.admin.modules.cost.entity.IslandGroupView;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.cost.repository.HotelGroupViewDao;
import com.trekiz.admin.modules.cost.repository.IslandGroupViewDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.pay.dao.PayGroupDao;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;

@Service
public class CostIslandService extends BaseService {

	@Autowired
	private IslandGroupViewDao islandGroupViewDao;
	
	@Autowired
	private HotelGroupViewDao hotelGroupViewDao;
	
	@Autowired
	private CostRecordIslandDao costRecordIslandDao;
	@Autowired
	private CostRecordHotelDao costRecordHotelDao;
	
	@Autowired
	private PayGroupDao payGroupDao;
	
	public  Page<IslandGroupView> findIslandGroupView(Page<IslandGroupView> page,ActivityIslandGroup activityIslandGroup, 
		String activityName, Long supplierId,Long companyId, DepartmentCommon common) {		
	
		DetachedCriteria dc = islandGroupViewDao.createDetachedCriteria();		
		if(StringUtils.isNotBlank(activityName)) {			
			dc.add(Restrictions.like("activityName", "%"+ activityName.trim()+"%"));
		}	
		
		dc.add(Restrictions.eq("wholesalerId",  companyId));	
		
		if(StringUtils.isNotBlank(activityIslandGroup.getGroupCode())) {			
			dc.add(Restrictions.like("groupCode", "%"+ activityIslandGroup.getGroupCode().trim()+"%"));
		}			
		
		if(supplierId != null) {			
		    dc.add(Restrictions.sqlRestriction("{alias}.uuid in (select activity_uuid  from cost_record_island where supplyId =(?)  and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
		}
		if(activityIslandGroup.getGroupOpenDate()!=null ) {
			dc.add(Restrictions.ge("groupOpenDate",activityIslandGroup.getGroupOpenDate()));
		}		
		if (activityIslandGroup.getGroupEndDate()!=null) {
			dc.add(Restrictions.le("groupOpenDate",activityIslandGroup.getGroupEndDate()));
		}			
		if (!StringUtils.isNotEmpty(page.getOrderBy())) {
			dc.addOrder(Order.desc("id"));
		}
		
	    return islandGroupViewDao.find(page, dc);
	}
	
	public  Page<HotelGroupView> findHotelGroupView(Page<HotelGroupView> page,ActivityHotelGroup activityHotelGroup, 
			String activityName, Long supplierId,Long companyId, DepartmentCommon common) {			
			DetachedCriteria dc = hotelGroupViewDao.createDetachedCriteria();				
			if(StringUtils.isNotBlank(activityName)) {			
				dc.add(Restrictions.like("activityName", "%"+ activityName.trim()+"%"));
			}				
			if(StringUtils.isNotBlank(activityHotelGroup.getGroupCode())) {			
				dc.add(Restrictions.like("groupCode", "%"+ activityHotelGroup.getGroupCode().trim()+"%"));
			}	
			
			dc.add(Restrictions.eq("wholesalerId",  companyId));	
			
			if(supplierId != null) {			
			    dc.add(Restrictions.sqlRestriction("{alias}.uuid in (select activity_uuid  from cost_record_island where supplyId =(?)  and supplyType=0)",supplierId.toString(), StringType.INSTANCE));
			}			
			if(activityHotelGroup.getGroupOpenDate()!=null ) {
				dc.add(Restrictions.ge("groupOpenDate",activityHotelGroup.getGroupOpenDate()));
			}			
			if (activityHotelGroup.getGroupEndDate()!=null) {
				dc.add(Restrictions.le("groupOpenDate",activityHotelGroup.getGroupEndDate()));
			}				
			if (!StringUtils.isNotEmpty(page.getOrderBy())) {
				dc.addOrder(Order.desc("id"));
			}			
		    return hotelGroupViewDao.find(page, dc);
		}
	
	
	public List<CostRecordIsland> findCostIslandList(String activityUuid,
			Integer budgetType, Integer overseas) {

		return costRecordIslandDao.findCostIslandList(activityUuid, budgetType,overseas);
	}
	

	public List<CostRecordHotel> findCostHotelList(String activityUuid,
			Integer budgetType, Integer overseas) {

		return costRecordHotelDao.findCostHotelList(activityUuid, budgetType,overseas);
	}
	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public CostRecordIsland saveCostRecordIsland(CostRecordIsland costRecordIsland) {
		return costRecordIslandDao.save(costRecordIsland);
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public CostRecordHotel saveCostRecordHotel(CostRecordHotel costRecordHotel) {
		return costRecordHotelDao.save(costRecordHotel);
	}
	
	public CostRecordIsland copyCostRecordIsland(CostRecordIsland cri) {
		CostRecordIsland costRecordIsland = new CostRecordIsland();
		costRecordIsland.setUuid(UUID.randomUUID().toString().replace("-", ""));
		costRecordIsland.setActivityUuid(cri.getActivityUuid());
		costRecordIsland.setOrderType(cri.getOrderType());
		costRecordIsland.setName(cri.getName());
		costRecordIsland.setPrice(cri.getPrice());
		costRecordIsland.setQuantity(cri.getQuantity());
		costRecordIsland.setComment(cri.getComment());
		costRecordIsland.setSupplierType(cri.getSupplierType());
		costRecordIsland.setCurrencyId(cri.getCurrencyId());
		costRecordIsland.setOverseas(cri.getOverseas());
		costRecordIsland.setSupplyType(cri.getSupplyType());

		costRecordIsland.setSupplyId(cri.getSupplyId());
		costRecordIsland.setSupplyName(cri.getSupplyName());
		costRecordIsland.setCreateBy(cri.getCreateBy());
		costRecordIsland.setUpdateBy(cri.getUpdateBy());
		costRecordIsland.setBudgetType(cri.getBudgetType());

		costRecordIsland.setReview(cri.getReview());
		costRecordIsland.setPayStatus(cri.getPayStatus());
		costRecordIsland.setDelFlag(cri.getDelFlag());
		costRecordIsland.setCurrencyAfter(cri.getCurrencyAfter());

		costRecordIsland.setRate(cri.getRate());
		costRecordIsland.setPriceAfter(cri.getPriceAfter());
		costRecordIsland.setNowLevel(cri.getNowLevel());
		costRecordIsland.setReviewCompanyId(cri.getReviewCompanyId());

		costRecordIsland.setBankName(cri.getBankName());
		costRecordIsland.setBankAccount(cri.getBankAccount());
		costRecordIsland.setReviewType(cri.getReviewType());
		costRecordIsland.setReviewCompanyId(cri.getReviewCompanyId());
		
		costRecordIsland.setUpdateBy(cri.getUpdateBy());
		costRecordIsland.setCreateDate(cri.getCreateDate());
		costRecordIsland.setUpdateDate(cri.getUpdateDate());
		costRecordIsland.setPayApplyDate(cri.getPayApplyDate());
		costRecordIsland.setPayUpdateBy(cri.getPayUpdateBy());
		costRecordIsland.setPayUpdateDate(cri.getPayUpdateDate());

		return costRecordIsland;
	}
	
	public CostRecordHotel copyCostRecordHotel(CostRecordHotel crh) {
		CostRecordHotel costRecordHotel = new CostRecordHotel();
		costRecordHotel.setActivityUuid(crh.getActivityUuid());
		costRecordHotel.setUuid(UUID.randomUUID().toString().replace("-", ""));
		costRecordHotel.setOrderType(crh.getOrderType());
		costRecordHotel.setName(crh.getName());
		costRecordHotel.setPrice(crh.getPrice());
		costRecordHotel.setQuantity(crh.getQuantity());
		costRecordHotel.setComment(crh.getComment());
		costRecordHotel.setSupplierType(crh.getSupplierType());
		costRecordHotel.setCurrencyId(crh.getCurrencyId());
		costRecordHotel.setOverseas(crh.getOverseas());
		costRecordHotel.setSupplyType(crh.getSupplyType());

		costRecordHotel.setSupplyId(crh.getSupplyId());
		costRecordHotel.setSupplyName(crh.getSupplyName());
		costRecordHotel.setCreateBy(crh.getCreateBy());
		costRecordHotel.setUpdateBy(crh.getUpdateBy());
		costRecordHotel.setBudgetType(crh.getBudgetType());

		costRecordHotel.setReview(crh.getReview());
		costRecordHotel.setPayStatus(crh.getPayStatus());
		costRecordHotel.setDelFlag(crh.getDelFlag());
		costRecordHotel.setCurrencyAfter(crh.getCurrencyAfter());

		costRecordHotel.setRate(crh.getRate());
		costRecordHotel.setPriceAfter(crh.getPriceAfter());
		costRecordHotel.setNowLevel(crh.getNowLevel());
		costRecordHotel.setReviewCompanyId(crh.getReviewCompanyId());

		costRecordHotel.setBankName(crh.getBankName());
		costRecordHotel.setBankAccount(crh.getBankAccount());
		costRecordHotel.setReviewType(crh.getReviewType());
		costRecordHotel.setReviewCompanyId(crh.getReviewCompanyId());
		
		costRecordHotel.setUpdateBy(crh.getUpdateBy());
		costRecordHotel.setCreateDate(crh.getCreateDate());
		costRecordHotel.setUpdateDate(crh.getUpdateDate());
		costRecordHotel.setPayApplyDate(crh.getPayApplyDate());
		costRecordHotel.setPayUpdateBy(crh.getPayUpdateBy());
		costRecordHotel.setPayUpdateDate(crh.getPayUpdateDate());

		return costRecordHotel;
	}
	
	/**
	 * 
	 * @Title: pay
	 * @Description: TODO(实际成本付款，批发商或渠道商)
	 * @param @param id
	 * @return OrderPayInput 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public OrderPayInput payHotel(String id, String groupId, String payType, String activityUuid,
			String orderType, boolean totalCurrencyFlag) {
		// 构造入参对象
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();
		// 订单详情的页面
		// orderPayInput.setOrderDetailUrl();
		// 后置方法名(更新实际成本付款出纳确认状态)
		orderPayInput.setServiceAfterMethodName("payConfirmStatus");
		// //后置类名
//		orderPayInput.setServiceClassName("com.trekiz.admin.modules.cost.service.CostManageService");
		// 付款信息
		OrderPayDetail orderPayDetail = new OrderPayDetail();
		CostRecordHotel costRecordHotel = this.findOneHotel(Long.parseLong(id));

		orderPayDetail.setPayCurrencyId(costRecordHotel.getCurrencyId() + "");
		BigDecimal price = costRecordHotel.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecordHotel.getQuantity());
		BigDecimal amount = price.multiply(quantity);

		orderPayDetail.setRefundMoneyType(Context.REFUNDMONEYTYPE);
		orderPayDetail.setTotalCurrencyId(costRecordHotel.getCurrencyId() + "");
		orderPayDetail.setTotalCurrencyPrice(amount.toString());
		orderPayDetail.setGroupId(Integer.parseInt(groupId));
		orderPayDetail.setOrderType(Integer.parseInt(orderType));
		orderPayDetail.setCostRecordId(Integer.parseInt(id));
		// 根据recordid获取成本的已付金额
		List<Object[]> paydmoney = new ArrayList<Object[]>();
		List<PayGroup> list = payGroupDao.findById(Integer.parseInt(id));
		StringBuffer buffer = new StringBuffer();
		if (CollectionUtils.isNotEmpty(list)) {
			int num = list.size();
			for (int i = 0; i < num; i++) {
				if (i == num - 1) {
					buffer.append("'").append(list.get(i).getPayPrice())
							.append("'");
				} else {
					buffer.append("'").append(list.get(i).getPayPrice())
							.append("',");
				}
			}
		}
		if (StringUtils.isNotBlank(buffer.toString())) {
			String serialsql = "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from hotel_money_amount m,currency c, pay_group p where m.currencyId=c.currency_id and " +
					"(p.isAsAccount is null or p.isAsAccount != 102) and p.payPrice = m.serialNum " + " and m.serialNum in ("
					+buffer.toString()+ ") GROUP BY m.currencyId ORDER BY m.currencyId";
			paydmoney = payGroupDao.getSession().createSQLQuery(serialsql).list();
		}
		if (CollectionUtils.isNotEmpty(paydmoney)) {
			// 成本付款是单币种，如果不是第一次支付，支付页面的显示的总金额=应付总金额-已付金额
			BigDecimal payeDecimal = (BigDecimal) paydmoney.get(0)[2];
			// int pm = payeDecimal.intValue();
			// if(pm<=0){
			// orderPayDetail.setPayCurrencyPrice("0");
			// }else{
			orderPayDetail.setPayCurrencyPrice(amount.subtract(payeDecimal)
					.toString());
			// }
		} else {
			orderPayDetail.setPayCurrencyPrice(amount.toString());
		}
		orderPayDetail.setProjectId(Long.parseLong(id));
		detailList.add(orderPayDetail);
		orderPayInput.setSupplyType(costRecordHotel.getSupplyType());
		orderPayInput.setAgentId(costRecordHotel.getSupplyId());
		orderPayInput.setPayType("3");
		orderPayInput.setRefundMoneyTypeDesc(costRecordHotel.getName());
		orderPayInput.setOrderPayDetailList(detailList);
		orderPayInput.setMoneyFlag(1);
		orderPayInput.setTotalCurrencyFlag(totalCurrencyFlag);
		return orderPayInput;
	}
	
	private CostRecordHotel findOneHotel(Long id) {
		return costRecordHotelDao.findOne(id);
	}

	/**
	 * 
	 * @Title: pay
	 * @Description: TODO(实际成本付款，批发商或渠道商)
	 * @param @param id
	 * @return OrderPayInput 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public OrderPayInput payIsland(String id, String groupId, String payType, String activityUuid,
			String orderType, boolean totalCurrencyFlag) {
		// 构造入参对象
		OrderPayInput orderPayInput = new OrderPayInput();
		List<OrderPayDetail> detailList = new ArrayList<OrderPayDetail>();
		// 订单详情的页面
		// orderPayInput.setOrderDetailUrl();
		// 后置方法名(更新实际成本付款出纳确认状态)
		orderPayInput.setServiceAfterMethodName("payConfirmStatus");
		// //后置类名
//		orderPayInput
//				.setServiceClassName("com.trekiz.admin.modules.cost.service.CostIslandService");
		// 付款信息
		OrderPayDetail orderPayDetail = new OrderPayDetail();
		CostRecordIsland costRecordIsland = this.findOneIsland(Long.parseLong(id));

		orderPayDetail.setPayCurrencyId(costRecordIsland.getCurrencyId() + "");
		BigDecimal price = costRecordIsland.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecordIsland.getQuantity());
		BigDecimal amount = price.multiply(quantity);

		orderPayDetail.setRefundMoneyType(Context.REFUNDMONEYTYPE);
		orderPayDetail.setTotalCurrencyId(costRecordIsland.getCurrencyId() + "");
		orderPayDetail.setTotalCurrencyPrice(amount.toString());
		orderPayDetail.setGroupId(Integer.parseInt(groupId));
		orderPayDetail.setOrderType(Integer.parseInt(orderType));
		orderPayDetail.setCostRecordId(Integer.parseInt(id));
		// 根据recordid获取成本的已付金额
		List<Object[]> paydmoney = new ArrayList<Object[]>();
		List<PayGroup> list = payGroupDao.findById(Integer.parseInt(id));
		StringBuffer buffer = new StringBuffer();
		if (CollectionUtils.isNotEmpty(list)) {
			int num = list.size();
			for (int i = 0; i < num; i++) {
				if (i == num - 1) {
					buffer.append("'").append(list.get(i).getPayPrice())
							.append("'");
				} else {
					buffer.append("'").append(list.get(i).getPayPrice())
							.append("',");
				}
			}
		}

		if(StringUtils.isNotBlank(buffer.toString())){
			String serialsql = "SELECT c.currency_id id, c.currency_mark, sum(m.amount) from island_money_amount m,currency c, pay_group p where m.currencyId=c.currency_id and " +
					"(p.isAsAccount is null or p.isAsAccount != 102) and p.payPrice = m.serialNum " + " and m.serialNum in ("
					+buffer.toString()+ ") GROUP BY m.currencyId ORDER BY m.currencyId";
			paydmoney = payGroupDao.getSession().createSQLQuery(serialsql).list();
		}

		
		if (CollectionUtils.isNotEmpty(paydmoney)) {
			// 成本付款是单币种，如果不是第一次支付，支付页面的显示的总金额=应付总金额-已付金额
			BigDecimal payeDecimal = (BigDecimal) paydmoney.get(0)[2];
			// int pm = payeDecimal.intValue();
			// if(pm<=0){
			// orderPayDetail.setPayCurrencyPrice("0");
			// }else{
			orderPayDetail.setPayCurrencyPrice(amount.subtract(payeDecimal)
					.toString());
			// }
		} else {
			orderPayDetail.setPayCurrencyPrice(amount.toString());
		}
		orderPayDetail.setProjectId(Long.parseLong(id));
		detailList.add(orderPayDetail);
		orderPayInput.setSupplyType(costRecordIsland.getSupplyType());
		orderPayInput.setAgentId(costRecordIsland.getSupplyId());
		orderPayInput.setPayType("3");
		orderPayInput.setRefundMoneyTypeDesc(costRecordIsland.getName());
		orderPayInput.setOrderPayDetailList(detailList);
		orderPayInput.setMoneyFlag(1);
		orderPayInput.setTotalCurrencyFlag(totalCurrencyFlag);
		return orderPayInput;
	}
	
	private CostRecordIsland findOneIsland(Long id) {
		return costRecordIslandDao.findOne(id);
	}
	
	/**
	 * 海岛游已收金额
	 * @param id
	 * @return
	 */
	public Object getPayedMoneyForIsland(Long id) {
		String sqlStr = "SELECT FORMAT(sum(m.amount),2) from pay_group p, island_money_amount m, currency c " +
				"where m.currencyId=c.currency_id and p.cost_record_id = " + id + " and p.orderType = 12" +
				" and (p.isAsAccount is null or p.isAsAccount != 102)" + 
				" and p.payPrice = m.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
		return payGroupDao.getSession().createSQLQuery(sqlStr).uniqueResult();
	}
	
	/**
	 * 海岛游达帐金额
	 * @param id
	 * @return
	 */
	public Object getConfirmMoneyForIsland(Long id) {
		String sqlStr = "SELECT FORMAT(sum(m.amount),2) from pay_group p, island_money_amount m, currency c " +
				"where m.currencyId=c.currency_id and p.cost_record_id = " + id + " and p.isAsAccount = 1 " + " and p.orderType = 12" +
				" and p.payPrice = m.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
		return payGroupDao.getSession().createSQLQuery(sqlStr).uniqueResult();
	}
	
	/**
	 * 酒店已收金额
	 * @param id
	 * @return
	 */
	public Object getPayedMoneyForHotel(Long id) {
		String sqlStr = "SELECT FORMAT(sum(m.amount),2) from pay_group p, hotel_money_amount m, currency c " +
				"where m.currencyId=c.currency_id and p.cost_record_id = " + id + " and p.orderType = 11" +
				" and (p.isAsAccount is null or p.isAsAccount != 102)" + 
				" and p.payPrice = m.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
		return payGroupDao.getSession().createSQLQuery(sqlStr).uniqueResult();
	}
	
	/**
	 * 酒店达帐金额
	 * @param id
	 * @return
	 */
	public Object getConfirmMoneyForHotel(Long id) {
		String sqlStr = "SELECT FORMAT(sum(m.amount),2) from pay_group p, hotel_money_amount m, currency c " +
				"where m.currencyId=c.currency_id and p.cost_record_id = " + id + " and p.isAsAccount = 1 " + " and p.orderType = 11" +
				" and p.payPrice = m.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
		return payGroupDao.getSession().createSQLQuery(sqlStr).uniqueResult();
	}
	
	public List<Object> getRoomAndMealForIsland(String groupUuid) {
		String sqlStr = "SELECT b.room_name, b.nights, b.meal_name mlabel, hm.meal_name mrlabel, aigmr.currency_id, aigmr.price" +
				" FROM(	SELECT a.room_name,	a.nights,	hm.meal_name,	aigm.uuid aigmuuid,	hm.uuid hmuuid" +
				" FROM(	SELECT hr.room_name, aigr.nights,	aigr.uuid aigruuid FROM" +
				" hotel_room hr,	activity_island_group_room aigr	WHERE	hr.uuid = aigr.hotel_room_uuid" +
				" AND aigr.activity_island_group_uuid = ?" +
				" ) a	LEFT JOIN activity_island_group_meal aigm ON a.aigruuid = aigm.activity_island_group_room_uuid" +
				" LEFT JOIN hotel_meal hm ON hm.uuid = aigm.hotel_meal_uuid	" +
				" ) b LEFT JOIN activity_island_group_meal_rise aigmr ON aigmr.activity_island_group_meal_uuid = b.aigmuuid" +
				" LEFT JOIN hotel_meal hm ON hm.uuid = b.hmuuid " +	" GROUP BY	room_name, nights";
		return costRecordIslandDao.findBySql(sqlStr, Map.class, groupUuid);
	}
	
	public List<Object> getRoomAndMealForHotel(String groupUuid) {
		String sqlStr = "SELECT b.room_name, b.nights,	b.meal_name mlabel,	hm.meal_name mrlabel, ahgmr.currency_id, ahgmr.price" +
				" FROM(SELECT a.room_name, a.nights, hm.meal_name, ahgm.uuid ahgmuuid, hm.uuid hmuuid" +
				" FROM(	SELECT hr.room_name,ahgr.nights,ahgr.uuid ahgruuid	FROM" +
				" hotel_room hr, activity_hotel_group_room ahgr" +
				" WHERE	hr.uuid = ahgr.hotel_room_uuid AND ahgr.activity_hotel_group_uuid = ?" +
				" ) a	LEFT JOIN activity_hotel_group_meal ahgm ON a.ahgruuid = ahgm.activity_hotel_group_room_uuid" +
				" LEFT JOIN hotel_meal hm ON hm.uuid = ahgm.hotel_meal_uuid " +
				" ) b LEFT JOIN activity_hotel_group_meal_rise ahgmr ON ahgmr.activity_hotel_group_meal_uuid = b.ahgmuuid" +
				" LEFT JOIN hotel_meal hm ON hm.uuid = b.hmuuid " +	" GROUP BY room_name, nights";
		return costRecordIslandDao.findBySql(sqlStr, Map.class, groupUuid);
	}
	
	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteCostIsland(Long id) {
		costRecordIslandDao.deleteById(id);
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteCostHotel(Long id) {
		costRecordHotelDao.deleteById(id);
	}
	
	public List<Map<String, String>> findPayedRecordById(String id, String flag) {
		String sqlStr = null;
		if("island".equals(flag)) {
			sqlStr = "SELECT p.payTypeName, c.currency_mark, m.amount, p.createDate, p.isAsAccount, p.payVoucher" +
					" from island_money_amount m,currency c, pay_group p " +
					"where m.currencyId = c.currency_id and m.serialNum = p.payPrice and p.cost_record_id = " + id;
		} else if("hotel".equals(flag)) {
			sqlStr = "SELECT p.payTypeName, c.currency_mark, m.amount, p.createDate, p.isAsAccount, p.payVoucher" +
					" from hotel_money_amount m,currency c, pay_group p " +
					"where m.currencyId = c.currency_id and m.serialNum = p.payPrice and p.cost_record_id = " + id;
		}
		return payGroupDao.findBySql(sqlStr, Map.class);
	}
	
	public List<Map<String, Object>> getRefundSumForIsland(String activityUuid, Integer budgetType, Integer orderType) {
		String sql = "select sum(c.price) totalRefund from cost_record_island c where c.activity_uuid = ?" +
				" and c.reviewType = 1 and c.budgetType = ? and c.orderType = ?";
		if(budgetType == 1) {
			sql += " and c.reviewStatus not in ('已取消', '已驳回')";
		}
		List<Map<String, Object>> list = costRecordIslandDao.findBySql(sql, Map.class, activityUuid, budgetType, orderType);
		return list;
	}
	
	public List<Map<String, Object>> getRefundSumForHotel(String activityUuid, Integer budgetType, Integer orderType) {
		String sql = "select sum(c.price) totalRefund from cost_record_hotel c where c.activity_uuid = ?" +
				" and c.reviewType = 1 and c.budgetType = ? and c.orderType = ?";
		if(budgetType == 1) {
			sql += " and c.reviewStatus not in ('已取消', '已驳回')";
		}
		List<Map<String, Object>> list = costRecordHotelDao.findBySql(sql, Map.class, activityUuid, budgetType, orderType);
		return list;
	}
	
	public Page<Map<Object, Object>> getIslandOrderInfos(Page<Map<Object, Object>> page, String groupUuid) {
		String sql = "SELECT activity.activitySerNum,activity.activityName,activity.id activityId,users. NAME AS activityCreateUserName,agp.groupOpenDate,agp.groupEndDate,	agp.groupCode," +
				" agp.id gruopId,	agp.lockStatus settleLockStatus,	pro.uuid orderUuid,	pro.orderTime,	pro.createBy,	pro.orderNum,	pro.orderSalerId,	pro.orderPersonName," +
				" pro.orderPersonPhoneNum,	pro.orderStatus,	pro.orderCompanyName,	pro.orderPersonNum,	pro.isPayment,	pro.front_money,	pro.total_money,	pro.payed_money," +
				" pro.accounted_money,	totalOuter.moneyStr AS totalMoney,	payedOuter.moneyStr AS payedMoney,	accountedOuter.moneyStr AS accountedMoney,	pro.placeHolderType," +
				" pro.activationDate,	pro.orderCompany,	pro.payMode proPayMode,	pro.lockStatus,	pro.remainDays proRemainDays,	pro.cancel_description AS cancelDescription,pro.paymentType " +//, pro.apply_time AS applyTime新加转报名时间查询 by chy 2016-1-6 17:00:29 E423需求
				" FROM	activity_island activity,	activity_island_group agp,	sys_user users,	island_order pro" +
				" LEFT JOIN (	SELECT mao.serialNum,	GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount)	ORDER BY	mao.currencyId SEPARATOR '+') moneyStr" +
				" FROM island_money_amount mao	LEFT JOIN currency c ON mao.currencyId = c.currency_id	WHERE	mao.moneyType = 13	AND mao.businessType = 1" +
				" GROUP BY	mao.serialNum) totalOuter ON totalOuter.serialNum = pro.total_money" +
				" LEFT JOIN (	SELECT mao.serialNum,	GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount)	ORDER BY	mao.currencyId SEPARATOR '+') moneyStr" +
				" FROM island_money_amount mao	LEFT JOIN currency c ON mao.currencyId = c.currency_id	WHERE	mao.moneyType = 5	AND mao.businessType = 1" +
				" GROUP BY	mao.serialNum) payedOuter ON payedOuter.serialNum = pro.payed_money" +
				" LEFT JOIN (	SELECT mao.serialNum,	GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount)	ORDER BY	mao.currencyId SEPARATOR '+') moneyStr" +
				" FROM island_money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id	WHERE	mao.moneyType = 4	AND mao.businessType = 1" +
				" GROUP BY mao.serialNum) accountedOuter ON accountedOuter.serialNum = pro.accounted_money" +
				" WHERE	pro.delFlag = '0' AND activity.uuid = pro.activity_island_uuid AND activity.createBy = users.id AND agp.uuid = '" + groupUuid + "'" +
				" AND agp.uuid = pro.activity_island_group_uuid and agp.delFlag = 0 and activity.delFlag = 0 order by orderTime";
		Page<Map<Object, Object>> pageMap = costRecordIslandDao.findBySql(page, sql, Map.class);
		return pageMap;
	}
	
	public Page<Map<Object, Object>> getHotelOrderInfos(Page<Map<Object, Object>> page, String groupUuid) {
		String sql = "SELECT activity.activitySerNum,activity.activityName,activity.id activityId,users. NAME AS activityCreateUserName,agp.groupOpenDate,agp.groupEndDate,	agp.groupCode," +
				" agp.id gruopId,	agp.lockStatus settleLockStatus,	pro.uuid orderUuid,	pro.orderTime,	pro.createBy,	pro.orderNum,	pro.orderSalerId,	pro.orderPersonName," +
				" pro.orderPersonPhoneNum,	pro.orderStatus,	pro.orderCompanyName,	pro.orderPersonNum,	pro.isPayment,	pro.front_money,	pro.total_money,	pro.payed_money," +
				" pro.accounted_money,	totalOuter.moneyStr AS totalMoney,	payedOuter.moneyStr AS payedMoney,	accountedOuter.moneyStr AS accountedMoney,	pro.placeHolderType," +
				" pro.activationDate,	pro.orderCompany,	pro.payMode proPayMode,	pro.lockStatus,	pro.remainDays proRemainDays,	pro.cancel_description AS cancelDescription,pro.paymentType " +//, pro.apply_time AS applyTime新加转报名时间查询 by chy 2016年1月7日10:33:20 E423需求
				" FROM	activity_hotel activity,	activity_hotel_group agp,	sys_user users,	hotel_order pro" +
				" LEFT JOIN (	SELECT mao.serialNum,	GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount)	ORDER BY	mao.currencyId SEPARATOR '+') moneyStr" +
				" FROM hotel_money_amount mao	LEFT JOIN currency c ON mao.currencyId = c.currency_id	WHERE	mao.moneyType = 13	AND mao.businessType = 1" +
				" GROUP BY	mao.serialNum) totalOuter ON totalOuter.serialNum = pro.total_money" +
				" LEFT JOIN (	SELECT mao.serialNum,	GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount)	ORDER BY	mao.currencyId SEPARATOR '+') moneyStr" +
				" FROM hotel_money_amount mao	LEFT JOIN currency c ON mao.currencyId = c.currency_id	WHERE	mao.moneyType = 5	AND mao.businessType = 1" +
				" GROUP BY	mao.serialNum) payedOuter ON payedOuter.serialNum = pro.payed_money" +
				" LEFT JOIN (	SELECT mao.serialNum,	GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount)	ORDER BY	mao.currencyId SEPARATOR '+') moneyStr" +
				" FROM hotel_money_amount mao LEFT JOIN currency c ON mao.currencyId = c.currency_id	WHERE	mao.moneyType = 4	AND mao.businessType = 1" +
				" GROUP BY mao.serialNum) accountedOuter ON accountedOuter.serialNum = pro.accounted_money" +
				" WHERE	pro.delFlag = '0' AND activity.uuid = pro.activity_hotel_uuid AND activity.createBy = users.id AND agp.uuid = '" + groupUuid + "'" +
				" AND agp.uuid = pro.activity_hotel_group_uuid and agp.delFlag = 0 and activity.delFlag = 0 order by orderTime";
		Page<Map<Object, Object>> pageMap = costRecordIslandDao.findBySql(page, sql, Map.class);
		return pageMap;
	}
	
	// 海岛游 预计 实际总收入
	public List<Map<String, Object>> getIslandForCastList(String groupUuid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append("SELECT	t1.activity_island_group_uuid,	t1.saler,	t1.agentName,	IFNULL(sum(t1.totalMoney), 0) AS totalMoney,	IFNULL(sum(t1.accountedMoney), 0) AS accountedMoney,")
			.append(" IFNULL((totalMoney - accountedMoney),0) AS notAccountedMoney,	IFNULL(costrecord.refundprice, 0) AS refundprice")
			.append(" FROM (SELECT	o.activity_island_group_uuid,	(SELECT	su. NAME	FROM	sys_user su	WHERE	su.id = o.createBy) saler,")
			.append(" (	SELECT	ai.agentName FROM	agentinfo ai WHERE ai.id = o.orderCompany) agentName,")
			.append(" (SELECT	sum(ma1.amount * IFNULL(ma1.exchangerate, 1))	FROM	island_money_amount ma1	WHERE ma1.serialNum = o.total_money) AS totalMoney,")
			.append(" (	SELECT sum(ma2.amount * IFNULL(ma2.exchangerate, 1))	FROM island_money_amount ma2 WHERE	ma2.serialNum = o.accounted_money")
			.append(" ) AS accountedMoney	FROM island_order o	WHERE	o.delFlag = '0'	AND o.activity_island_group_uuid =	'").append(groupUuid).append("') t1")
			.append(" LEFT JOIN (SELECT		cri.activity_uuid,	sum(cri.price) AS refundprice	FROM	cost_record_island cri")
			.append(" WHERE	cri.reviewType = 1	AND cri.reviewStatus LIKE '审核通过'	GROUP BY cri.activity_uuid")
			.append(" ) costrecord ON costrecord.activity_uuid = t1.activity_island_group_uuid GROUP BY	t1.activity_island_group_uuid ORDER BY	saler;");

		list = costRecordIslandDao.findBySql(sBuffer.toString(), Map.class);
		return list;
	}
	
	// 酒店 预计 实际总收入
	public List<Map<String, Object>> getHotelForCastList(String groupUuid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append("SELECT	t1.activity_hotel_group_uuid,	t1.saler,	t1.agentName,	IFNULL(sum(t1.totalMoney), 0) AS totalMoney,	IFNULL(sum(t1.accountedMoney), 0) AS accountedMoney,")
			.append(" IFNULL((totalMoney - accountedMoney),0) AS notAccountedMoney,	IFNULL(costrecord.refundprice, 0) AS refundprice")
			.append(" FROM (SELECT	o.activity_hotel_group_uuid,	(SELECT	su. NAME	FROM	sys_user su	WHERE	su.id = o.createBy) saler,")
			.append(" (	SELECT	ai.agentName FROM	agentinfo ai WHERE ai.id = o.orderCompany) agentName,")
			.append(" (SELECT	sum(ma1.amount * IFNULL(ma1.exchangerate, 1))	FROM	hotel_money_amount ma1	WHERE ma1.serialNum = o.total_money) AS totalMoney,")
			.append(" (	SELECT sum(ma2.amount * IFNULL(ma2.exchangerate, 1))	FROM hotel_money_amount ma2 WHERE	ma2.serialNum = o.accounted_money")
			.append(" ) AS accountedMoney	FROM hotel_order o	WHERE	o.delFlag = '0'	AND o.activity_hotel_group_uuid =	'").append(groupUuid).append("') t1")
			.append(" LEFT JOIN (SELECT		crh.activity_uuid,	sum(crh.price) AS refundprice	FROM	cost_record_hotel crh")
			.append(" WHERE	crh.reviewType = 1	AND crh.reviewStatus LIKE '审核通过'	GROUP BY crh.activity_uuid")
			.append(" ) costrecord ON costrecord.activity_uuid = t1.activity_hotel_group_uuid GROUP BY	t1.activity_hotel_group_uuid ORDER BY	saler;");

		list = costRecordIslandDao.findBySql(sBuffer.toString(), Map.class);
		return list;
	}
	
}
