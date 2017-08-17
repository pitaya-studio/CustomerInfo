package com.trekiz.admin.modules.order.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.T1.order.entity.T1PreOrder;
import com.trekiz.admin.agentToOffice.T1.order.repository.T1PreOrderDao;
import com.trekiz.admin.agentToOffice.personnalInfo.service.PersonInfoService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderProgressTracking;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.OrderProgressTrackingDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class OrderProgressTrackingService extends BaseService {
    
    @Autowired
    private OrderProgressTrackingDao progressDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MoneyAmountService moneyAmountService;
    @Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService activityService;
    @Autowired
	private OrderpayDao orderpayDao;
    @Autowired
	private ProductOrderCommonDao productOrderDao;
    @Autowired
	private PersonInfoService personInfoService;
    @Autowired
    private T1PreOrderDao preOrderDao;
    @Autowired
    private TravelActivityDao activityDao;
    
    
    /**
     * 查询订单统计数：今日新增、所有订单、超时订单
     * @return
     * @author yakun.bai
     * @Date 2016-12-1
     */
    public String loadOrderCountNum() {
    	
    	String where = "";
    	// 如果是quauq批发商查看，则能查看所有订单进度；如果是其他批发商则只能查看自己订单进度
    	if (!UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_QUAUQ)) {
    		where += "AND opt.company_id = " + UserUtils.getUser().getCompany().getId() + " ";
    	} else {
    		if ("1".equals(UserUtils.getUser().getIsQuauqAgentLoginUser())) {
    			where += "AND opt.ask_user_id = " + UserUtils.getUser().getId() + " ";
    		}
    	}

    	// 查询今日新增数
    	String newAddSql = "SELECT count(opt.id) FROM order_progress_tracking opt " +
    				" WHERE opt.orderStatus != " + Context.ORDER_ZZ_DELETE + " AND (ask_time >= CURDATE() OR order_create_time >= CURDATE()) " + where;
    	List<BigInteger> newAddList = progressDao.findBySql(newAddSql);
    	
    	// 查询所有订单数（不包含已删除订单）
    	String allSql = "SELECT count(opt.id) FROM order_progress_tracking opt WHERE orderStatus != " + Context.ORDER_ZZ_DELETE + " " + where;
    	List<BigInteger> allList = progressDao.findBySql(allSql);
    	
    	/** 查询所有超时订单数 */
    	
    	String outTimeSql = getOutTimeSql(where, 1);
    	List<BigInteger> outTimeList = progressDao.findBySql(outTimeSql);
    	
    	// 结果按分号分割传递
    	DecimalFormat d = new DecimalFormat(",##0");
    	String result = d.format(newAddList.get(0)) + ";" + d.format(allList.get(0)) + ";" + d.format(outTimeList.get(0));
    	return result;
    }
    
    private String getOutTimeSql(String where, Integer type) {
    	
    	String outTimeSql = null;
    	
    	// 销售超时订单
    	String outTimeSqlForSaler = "SELECT opt.id FROM order_progress_tracking opt, order_tracking_setting tracking " +
    						"WHERE opt.orderStatus != " + Context.ORDER_ZZ_DELETE + " AND opt.company_id = tracking.company_id AND order_create_time IS NULL AND setting_type = 1 " +
    						"AND timestampdiff(MINUTE, ask_time, CURRENT_TIMESTAMP) > " +
    						"(CASE red_light_time_type WHEN 1 THEN 24 * 60 * red_light_time_start WHEN 2 THEN 60 * red_light_time_start " +
    						"ELSE red_light_time_start END)" + where;
    	
    	// 计调超时订单
    	String outTimeSqlForOp = "SELECT opt.id FROM order_progress_tracking opt, order_tracking_setting tracking " +
    						"WHERE opt.orderStatus != " + Context.ORDER_ZZ_DELETE + " AND opt.company_id = tracking.company_id AND order_create_time IS NOT NULL " +
    						"AND confirmation_file_saler_time IS NULL AND setting_type = 2 " +
    						"AND timestampdiff(MINUTE, order_create_time, CURRENT_TIMESTAMP) > " +
    						"(CASE red_light_time_type WHEN 1 THEN 24 * 60 * red_light_time_start WHEN 2 THEN 60 * red_light_time_start " +
    						"ELSE red_light_time_start END)" + where;
    	
    	// 财务超时订单
    	String outTimeSqlForCw = "SELECT opt.id FROM order_progress_tracking opt, order_tracking_setting tracking " +
    						"WHERE opt.orderStatus != " + Context.ORDER_ZZ_DELETE + " AND opt.company_id = tracking.company_id AND first_order_pay_time IS NOT NULL AND setting_type = 3 " +
    						"AND last_order_pay_time IS NULL AND timestampdiff(MINUTE, first_order_pay_time, CURRENT_TIMESTAMP) > " +
    						"(CASE red_light_time_type WHEN 1 THEN 24 * 60 * red_light_time_start WHEN 2 THEN 60 * red_light_time_start " +
    						"ELSE red_light_time_start END)" + where;
    	
    	if (type == 1) {
    		outTimeSql = "SELECT count(distinct(id)) FROM (" + outTimeSqlForSaler + " UNION " + outTimeSqlForOp + " UNION " + outTimeSqlForCw + ") outTime";
    	} else if (type == 2) {
    		outTimeSql = "(" + outTimeSqlForSaler + " UNION " + outTimeSqlForOp + " UNION " + outTimeSqlForCw + ") outTime";
    	} else {
    		outTimeSql = "(" + outTimeSqlForSaler + " UNION " + outTimeSqlForOp + " UNION " + outTimeSqlForCw + ") outTime, ";
    	}
    	return outTimeSql;
    }
    
    /**
     * 查询各个渠道订单数
     * @return
     * @author yakun.bai
     * @Date 2016-12-1
     */
    public Object loadAgentCountNum(HttpServletRequest request) {
    	
    	String selectType = request.getParameter("selectType");
    	
    	String where = "";
    	// 如果是quauq批发商查看，则能查看所有订单进度；如果是其他批发商则只能查看自己订单进度
    	if (!UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_QUAUQ)) {
    		where += "AND opt.company_id = " + UserUtils.getUser().getCompany().getId() + " ";
    	} else {
    		if ("1".equals(UserUtils.getUser().getIsQuauqAgentLoginUser())) {
    			where += "AND ask_user_id = " + UserUtils.getUser().getId() + " ";
    		}
    	}
    	
    	// 查询所有
    	String sql = "SELECT GROUP_CONCAT(DISTINCT(agt.id)) agentIds, agt.agentBrand, substring_index(agt.agentName, '-', 1) agentName, count(opt.id) countNum, agentAddressStreet agentAddress " +
    			"FROM agentinfo agt, order_progress_tracking opt WHERE opt.orderStatus != " + Context.ORDER_ZZ_DELETE + " AND agt.is_quauq_agent = 1 AND agt.id = opt.ask_agent_id " + where + 
    			"GROUP BY agt.agentBrand, substring_index(agt.agentName, '-', 1)  ORDER BY count(opt.id) DESC";
    	
    	if (StringUtils.isNotBlank(selectType)) {
    		if ("add".equals(selectType)) {
    			sql = "SELECT GROUP_CONCAT(DISTINCT(agt.id)) agentIds, agt.agentBrand, substring_index(agt.agentName, '-', 1) agentName, count(opt.id) countNum, agentAddressStreet agentAddress " +
    	    			"FROM agentinfo agt, order_progress_tracking opt " +
    	    			"WHERE opt.orderStatus != " + Context.ORDER_ZZ_DELETE + " AND " +
    	    			"(ask_time >= CURDATE() OR order_create_time >= CURDATE()) AND is_quauq_agent = 1 AND agt.id = opt.ask_agent_id " + where + 
    	    			"GROUP BY agt.agentBrand, substring_index(agt.agentName, '-', 1) ORDER BY count(opt.id) DESC";
    		} else if ("outTime".equals(selectType)) {
    			
    			String outTimeSql = getOutTimeSql(where, 2);
    			
    	    	sql = "SELECT GROUP_CONCAT(DISTINCT(agt.id)) agentIds, agt.agentBrand, substring_index(agt.agentName, '-', 1) agentName, count(opt.id) countNum, agentAddressStreet agentAddress " +
    	    			"FROM agentinfo agt, order_progress_tracking opt, " + outTimeSql + " " + 
    	    			"WHERE opt.id = outTime.id AND is_quauq_agent = 1 AND agt.id = opt.ask_agent_id " + where + 
    	    			"GROUP BY agt.agentBrand, substring_index(agt.agentName, '-', 1) ORDER BY count(opt.id) DESC";
    		}
    	}

    	List<Map<Object, Object>> list = progressDao.findBySql(sql, Map.class);
    	
    	if (CollectionUtils.isNotEmpty(list)) {
    		for (Map<Object, Object> map : list) {
    			Object agentBrand = map.get("agentBrand");
    			Object agentName = map.get("agentName");
    			if (null != agentBrand && null != agentName) {
    				String brand = agentBrand.toString();
    				//判断渠道名称是否会出现“世界3”的情况
    				if(agentName.toString().indexOf("-") < 0){
    					String name = agentName.toString();
    					map.put("agentName", name);
    				}else{
    					String name = agentName.toString().split("-")[0];
        				
        				if (name.indexOf(brand) != -1) {
        					String lastName = name.substring(name.indexOf(brand) + brand.length());
        					if (lastName.length() > 0) {
        						map.put("agentName", name.substring(name.indexOf(brand) + brand.length()));
        					}
        				}
    				}
    				
    			}
    		}
    	}
    	
    	JSONArray josnList = JSONArray.fromObject(list);
    	JSONObject results = new JSONObject();
    	results.put("josnList", josnList);
    	results.put("randomForAgent", request.getParameter("randomForAgent"));
    	
    	return results;
    }
    
    /**
     * 查询订单进度跟踪列表
     * @return
     * @author yakun.bai
     * @Date 2016-12-2
     */
    public String loadProgressByAgentIds(HttpServletRequest request) {
    	
    	String agentIds = request.getParameter("agentIds");
    	String selectType = request.getParameter("selectType");
    	
    	String where = "";
    	// 如果是quauq批发商查看，则能查看所有订单进度；如果是其他批发商则只能查看自己订单进度
    	if (!UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_QUAUQ)) {
    		where += "AND pro.company_id = " + UserUtils.getUser().getCompany().getId() + " ";
    	} else {
    		if ("1".equals(UserUtils.getUser().getIsQuauqAgentLoginUser())) {
    			where += "AND pro.ask_user_id = " + UserUtils.getUser().getId() + " ";
    		}
    	}

    	String outTimeSql = "";
    	if (StringUtils.isNotBlank(selectType)) {
    		String tempWhere = "";
    		if (!UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_QUAUQ)) {
    			tempWhere += "AND opt.company_id = " + UserUtils.getUser().getCompany().getId() + " ";
        	} else {
        		if ("1".equals(UserUtils.getUser().getIsQuauqAgentLoginUser())) {
        			tempWhere += "AND pro.ask_user_id = " + UserUtils.getUser().getId() + " ";
        		}
        	}
    		if ("add".equals(selectType)) {
    			where += "AND (ask_time >= CURDATE() OR order_create_time >= CURDATE())";
    		} else if ("outTime".equals(selectType)) {
    			outTimeSql = getOutTimeSql(tempWhere, 3);
    		}
    	}
    	
    	// 查询sql
    	String sql = "SELECT pro.id, pro.ask_num askNum, concat(pro.ask_time) askTime, pro.ask_user_id askUserId, pro.company_id companyId, pro.t1_flag t1Flag,  " +
    			"pro.activity_id activityId, pro.group_id groupId, pro.order_id orderId, pro.order_num orderNum, pro.ask_agent_id askAgentId, " +
    			"concat(pro.order_create_time) orderCreateTime, pro.orderStatus, " + 
    			"pro.confirmation_file_saler_id confirmationFileSalerId, concat(pro.confirmation_file_saler_time) confirmationFileSalerTime, " + 
    			"pro.activity_create_name activityCreateName, " + 
    			"'' firstOrderPayTime, '' lastOrderPayTime, " +
    			"activity.acitivityName, gro.groupCode, concat(gro.groupOpenDate) groupOpenDate, gro.freePosition, office.name officeName, contacts.contactPeople, " +
    			"agent.agentName, " +
    			
    				// 订单跟踪批发商设置
    				"tracking.company_id, tracking.setting " +
    				
    				"FROM order_progress_tracking pro," + outTimeSql + " travelactivity activity, activitygroup gro, agentinfo agent, sys_office office LEFT JOIN " + 
    				
    				// 批发商联系人查询
    				"(SELECT off.id , GROUP_CONCAT(CONCAT(us.name,' ',us.mobile) ORDER BY us.id SEPARATOR '+') contactPeople " + 
    				"FROM sys_user us, sys_office off WHERE quauqBookOrderPermission = 1 AND us.companyId = off.id AND off.delFlag = 0 " +
    				"GROUP BY us.companyId) contacts ON contacts.id = office.id " + 
    				"LEFT JOIN " + 
    				
    				// 订单跟踪批发商设置
    				"(SELECT company_id, GROUP_CONCAT(" +
    				"CONCAT(setting_type, ' ', green_light_time_type, ' ', green_light_time_start, ' ', green_light_time_end, ' ', " + 
    				"yellow_light_time_type, ' ', yellow_light_time_start, ' ', yellow_light_time_end, ' ', " +
    				"red_light_time_type, ' ', red_light_time_start) ORDER BY company_id SEPARATOR '+') setting " + 
    				"FROM order_tracking_setting tracking GROUP BY company_id) tracking ON tracking.company_id = office.id " + 
    				
    				"WHERE pro.activity_id = activity.id AND pro.group_id = gro.id AND agent.id = pro.ask_agent_id AND pro.company_id = office.id " +
    				("outTime".equals(selectType) ? "AND pro.id = outTime.id " : "") +
    				"AND pro.orderStatus != " + Context.ORDER_ZZ_DELETE + " AND agent.id in (" + agentIds + ") ";
    	List<Map<Object, Object>> list = progressDao.findBySql(sql + where + " order by pro.updateDate desc", Map.class);
		
		for (Map<Object, Object> listin : list) {
			if (listin.get("orderId") != null && listin.get("orderId").toString().trim() != "") {
				Long orderId = Long.valueOf(listin.get("orderId").toString());
				List<Orderpay> orderPayList = orderpayDao.findOrderPayOrderById(orderId, 2);
				// 获取首次支付时间
				if (CollectionUtils.isNotEmpty(orderPayList)) {
					Orderpay firstOrderPay = orderPayList.get(0);
					listin.put("firstOrderPayTime", DateUtils.formatDate(firstOrderPay.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
				}
				// 获取最后支付时间
				ProductOrderCommon productOrder = productOrderDao.findOne(orderId);
				List<Orderpay> DZList = orderpayDao.findOrderPayOrderByUpdate(orderId, productOrder.getOrderStatus());
				if (CollectionUtils.isNotEmpty(DZList) && isFullPayed(productOrder)) {
					listin.put("lastOrderPayTime", DateUtils.formatDate(DZList.get(0).getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
					listin.put("lastOrderPayName", DZList.get(0).getUpdateBy().getName());
				}
				listin.put("orderStatusStr", DictUtils.getDictLabel(productOrder.getPayStatus().toString(), "order_pay_status", ""));
				
				// 获取订单人数
				String orderPersonNumStr = "";
				Integer adultNum = productOrder.getOrderPersonNumAdult();
				Integer childNum = productOrder.getOrderPersonNumChild();
				Integer specialNum = productOrder.getOrderPersonNumSpecial();
				if (adultNum != null && adultNum.intValue() != 0) {
					orderPersonNumStr += "成人×" + adultNum + " ";
				}
				if (childNum != null && childNum.intValue() != 0) {
					orderPersonNumStr += "儿童×" + childNum + " ";
				}
				if (specialNum != null && specialNum.intValue() != 0) {
					orderPersonNumStr += "特殊人群×" + specialNum + " ";
				}
				listin.put("orderPersonNumStr", orderPersonNumStr);
				
			}
			
			// 获取渠道联系人
			if ("0".equals(UserUtils.getUser().getIsQuauqAgentLoginUser())) {
				Integer agentId = Integer.valueOf(listin.get("askAgentId").toString());
				List<SupplyContacts> contactList = personInfoService.getSupplyContactsByAgentId(agentId, 0);
				Map<String,String> contactSb = new HashMap<String,String>();
				if (CollectionUtils.isNotEmpty(contactList)) {
					SupplyContacts contact = contactList.get(0);
					contactSb.put("contactName", contact.getContactName() );
					contactSb.put("contactMobile", contact.getContactMobile());
					listin.put("contactList", contactSb);
				}
			}
		}
		
		JSONArray josnList = JSONArray.fromObject(list);
		
    	return josnList.toString();
    }
    
	public boolean isFullPayed(ProductOrderCommon productOrder) {
		boolean flag = false;
		BigDecimal accountedMoney = new BigDecimal(0);
		BigDecimal totalMoney = new BigDecimal(0);
		
		//应收
		if (StringUtils.isNotBlank(productOrder.getTotalMoney())) {
			List<Object[]> list = moneyAmountService.getMoneyAmonut(productOrder.getTotalMoney());
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i=0;i<list.size();i++) {
					if (list.get(i)[3] != null && list.get(i)[4] != null) {
						//转换成人民币
						totalMoney = totalMoney.add(new BigDecimal(list.get(i)[3].toString()).multiply(new BigDecimal(list.get(i)[4].toString())));
					}
    			}
			}
		}
		
		if (StringUtils.isNotBlank(productOrder.getPayedMoney())) {
			List<Object[]> list = moneyAmountService.getMoneyAmonut(productOrder.getAccountedMoney());
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i=0;i<list.size();i++) {
					if (list.get(i)[3] != null && list.get(i)[4] != null) {
						//转换成人民币
						accountedMoney = accountedMoney.add(new BigDecimal(list.get(i)[3].toString()).multiply(new BigDecimal(list.get(i)[4].toString())));
					}
    			}
			}
		}
		
		// 如果达帐金额大于或等于订单金额则添加时间
		if (totalMoney.compareTo(accountedMoney) <= 0) {
			flag = true;
		}
		
		return flag;
	}
    
	
	public Object getOrderLinkPerson(HttpServletRequest request) {
		JSONObject results = new JSONObject();
		
		JSONArray salerPersonArr = new JSONArray();
		JSONArray opPersonArr = new JSONArray();
		JSONArray cwPersonArr = new JSONArray();
		
		
		String orderId = request.getParameter("orderId");
		if (StringUtils.isNotBlank(orderId)) {
			OrderProgressTracking progress = progressDao.findOne(Long.parseLong(orderId));
			Long productOrderId = progress.getOrderId();
			// 如果已经生成订单则获取订单创建人姓名、手机号、订单创建时间
			if (null != productOrderId) {
				Integer orderStatus = progress.getOrderStatus();
				// 如果订单已取消则记录取消人及取消时间
				JSONObject obj = new JSONObject();
				User user;
				if (orderStatus != 0) {
					user = userDao.findById(progress.getUpdateById());
				}else{
					user = userDao.findById(progress.getAskSalerId());
				}
				if(user != null){
					obj.put("name", user.getName());
					obj.put("mobile", user.getMobile());
					if (orderStatus != 0) {
						obj.put("updateDate", DateUtils.formatDate(progress.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
					} 
					salerPersonArr.add(obj);
				}
			} else {
				Integer orderStatus = progress.getOrderStatus();
				// 如果是询批发商，则需要展示批发商所有有T1渠道报名权限销售；如果是预报名，则需展示询销售姓名
				if (orderStatus == 0) {
					/*List<User> userList = userDao.findCompanyT1User(progress.getCompanyId());
					if (CollectionUtils.isNotEmpty(userList)) {
						JSONObject obj = null;
						for (User user : userList) {
							obj = new JSONObject();
							obj.put("name", user.getName());
							obj.put("mobile", user.getMobile());
							salerPersonArr.add(obj);
						}
					}*/
					if(progress.getAskSalerId() != null){
						User user = userDao.getById(progress.getAskSalerId());
						if(user != null){
							JSONObject obj = null;
							obj = new JSONObject();
							obj.put("name", user.getName());
							obj.put("mobile", user.getMobile());
							salerPersonArr.add(obj);
						}
					}
				} else {
					// 如果预报单已被取消或删除，则记录删除人及删除时间
					/*if (orderStatus == 4 || orderStatus == 5) {
						JSONObject obj = new JSONObject();
						User user = userDao.findById(progress.getUpdateById());
						obj.put("name", user.getName());
						obj.put("mobile", user.getMobile());
						obj.put("updateDate", DateUtils.formatDate(progress.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
						salerPersonArr.add(obj);
					} else {*/
						T1PreOrder preOrder = preOrderDao.getById(progress.getPreOrderId());
						if (null != preOrder) {
							JSONObject obj = new JSONObject();
							User user = preOrder.getUpdateBy();
							obj.put("name", user.getName());
							obj.put("mobile", user.getMobile());
							salerPersonArr.add(obj);
						}
//					}
				}
			}
			results.put("salerPersonInfo", salerPersonArr);
			
			// 添加计调信息
			if (null != progress.getOrderId()) {
				if (null == progress.getConfirmationFileSalerId()) {
					User user = activityDao.findOne(progress.getActivityId()).getCreateBy();
					JSONObject obj = new JSONObject();
					obj.put("name", user.getName());
					obj.put("mobile", user.getMobile());
					opPersonArr.add(obj);
				} else {
					User user = userDao.findOne(progress.getConfirmationFileSalerId());
					JSONObject obj = new JSONObject();
					obj.put("name", user.getName());
					obj.put("mobile", user.getMobile());
					opPersonArr.add(obj);
				}
			}
			results.put("opPersonInfo", opPersonArr);
			
			// 添加财务信息
			if (null != progress.getOrderId()) {
				String firstOrderPayTime = null;
				String lastOrderPayTime = null;
//				String firstOrderPayName = null;
				Orderpay lastOrderPayUser = null;
				List<Orderpay> orderPayList = orderpayDao.findOrderPayOrderById(progress.getOrderId(), 2);
				if (CollectionUtils.isNotEmpty(orderPayList)) {
					// 获取首次支付时间
					Orderpay firstOrderPay = orderPayList.get(0);
					firstOrderPayTime = DateUtils.formatDate(firstOrderPay.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
//					firstOrderPayName = firstOrderPay.getCreateBy().getName();
				}
				// 获取最后支付时间
				ProductOrderCommon productOrder = productOrderDao.findOne(progress.getOrderId());
				List<Orderpay> DZList = orderpayDao.findOrderPayOrderByUpdate(progress.getOrderId(), productOrder.getOrderStatus());
				if (CollectionUtils.isNotEmpty(DZList) && isFullPayed(productOrder)) {
					lastOrderPayTime = DateUtils.formatDate(DZList.get(0).getUpdateDate(), "yyyy-MM-dd HH:mm:ss");
					lastOrderPayUser = DZList.get(0);
				}
				
				Long companyId = progress.getCompanyId();
				Integer orderStatus = progress.getOrderStatus();
				if (orderStatus == Context.ORDER_ZZ_BH_CANCEL || orderStatus == Context.ORDER_ZZ_BH2_CANCEL) {
					JSONObject obj = new JSONObject();
					User user = userDao.findById(progress.getUpdateById());
					obj.put("name", user.getName());
					obj.put("mobile", user.getMobile());
					obj.put("updateDate", DateUtils.formatDate(progress.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
					cwPersonArr.add(obj);
				} else {
					if (null != firstOrderPayTime) {
						if (null == lastOrderPayTime) {
							List<User> cwUserList = userDao.getCwUserByCompanyId(companyId);
							if (CollectionUtils.isNotEmpty(cwUserList)) {
								JSONObject obj = null;
								for (User user : cwUserList) {
									obj = new JSONObject();
									obj.put("name", user.getName());
									obj.put("mobile", user.getMobile());
									cwPersonArr.add(obj);
								}
							}
						} else {
							JSONObject obj = new JSONObject();
							obj.put("name", lastOrderPayUser.getUpdateBy().getName());
							obj.put("mobile", lastOrderPayUser.getUpdateBy().getMobile());
							obj.put("updateDate", DateUtils.formatDate(lastOrderPayUser.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
							cwPersonArr.add(obj);
						}
					}
				}
			}
			
			results.put("cwPersonInfo", cwPersonArr);
		}
			
		return results;
	}
    
    /**
     * 查询订单进度跟踪列表
     * @author yakun.bai
     * @Date 2016-8-12
     */
    public Page<Map<Object, Object>> find(Page<Map<Object, Object>> page, String selectType) {
    	
    	page.setPageSize(50);
    	
    	String where = "";
    	// 如果是quauq批发商查看，则能查看所有订单进度；如果是其他批发商则只能查看自己订单进度
    	if (!UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_QUAUQ)) {
    		where += "AND pro.company_id = " + UserUtils.getUser().getCompany().getId() + " ";
    	} else {
    		if ("1".equals(UserUtils.getUser().getIsQuauqAgentLoginUser())) {
    			where += "AND pro.ask_user_id = " + UserUtils.getUser().getId() + " ";
    		}
    	}
    	if ("0".equals(selectType)) {
    		where += "AND pro.order_id IS NULL ";
    	} else if ("1".equals(selectType)) {
    		where += "AND pro.order_id IS NOT NULL ";
    	}
    	// 查询sql
    	String sql = "SELECT pro.id, pro.ask_num askNum, concat(pro.ask_time) askTime, pro.ask_user_id askUserId, pro.company_id companyId, " +
    				"pro.activity_id activityId, pro.group_id groupId, pro.order_id orderId, pro.order_num orderNum, pro.ask_agent_id askAgentId, " +
    				"concat(pro.order_create_time) orderCreateTime, " + 
    				"pro.order_create_name orderCreateName, pro.confirmation_file_saler_id confirmationFileSalerId, concat(pro.confirmation_file_saler_time) confirmationFileSalerTime, " + 
    				"pro.activity_create_name activityCreateName, " + 
    				"'' firstOrderPayTime, '' lastOrderPayTime, " +
    				"activity.acitivityName, gro.groupCode, concat(gro.groupOpenDate) groupOpenDate, gro.freePosition, office.name officeName, contacts.contactPeople, " +
    				"agent.agentName, " +
    				
    				// 订单跟踪批发商设置
    				"tracking.company_id, tracking.setting " +
    				
    				"FROM order_progress_tracking pro, travelactivity activity, activitygroup gro, agentinfo agent, sys_office office LEFT JOIN " + 
    				
    				// 批发商联系人查询
    				"(SELECT off.id , GROUP_CONCAT(CONCAT(us.name,' ',us.mobile) ORDER BY us.id SEPARATOR '+') contactPeople " + 
    				"FROM sys_user us, sys_office off WHERE quauqBookOrderPermission = 1 AND us.companyId = off.id AND off.delFlag = 0 " +
    				"GROUP BY us.companyId) contacts ON contacts.id = office.id " + 
    				"LEFT JOIN " + 
    				
    				// 订单跟踪批发商设置
    				"(SELECT company_id, GROUP_CONCAT(" +
    				"CONCAT(setting_type, ' ', green_light_time_type, ' ', green_light_time_start, ' ', green_light_time_end, ' ', " + 
    				"yellow_light_time_type, ' ', yellow_light_time_start, ' ', yellow_light_time_end, ' ', " +
    				"red_light_time_type, ' ', red_light_time_start) ORDER BY company_id SEPARATOR '+') setting " + 
    				"FROM order_tracking_setting tracking GROUP BY company_id) tracking ON tracking.company_id = office.id " + 
    				
    				"WHERE pro.activity_id = activity.id AND pro.group_id = gro.id AND agent.id = pro.ask_agent_id AND pro.company_id = office.id ";
    	return progressDao.findPageBySql(page, sql + where + " order by pro.id desc", Map.class);
    }
    
    /**
     * 根据产品Id、销售Id、询单时间间隔小于60秒时间，进行查询询单记录
     * @param askUserId
     * @param activityId
     * @param orderType
     * @param askSalerId
     * @return
     */
    public List<OrderProgressTracking> findByAskUserIdAndAactivityId(Long askUserId, Long activityId, Integer orderType,Long askSalerId) {
    	//return progressDao.findByAskUserIdAndGroupId(askUserId, groupId, orderType, askSalerId);
    	return progressDao.findByAskUserIdAndProductIdAndSalerIdAndAskTime(askUserId, activityId, orderType, askSalerId,0);
    }
    
    /**
     * 查询订单进度跟踪列表
     * @author yakun.bai
     * @Date 2016-12-7
     */
    public OrderProgressTracking findByPreOrderId(Long preOrderId) {
    	return progressDao.findByPreOrderId(preOrderId);
    }
    
    /**
     * 查询订单进度跟踪
     * @author yakun.bai
     * @Date 2016-8-12
     */
    public OrderProgressTracking findById(Long settingId) {
    	return progressDao.findOne(settingId);
    }
    
    /**
     * 保存订单进度跟踪
     * @author yakun.bai
     * @Date 2016-8-12
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void save(OrderProgressTracking OrderProgressTracking) {
    	progressDao.save(OrderProgressTracking);
    }
    
    /**
     * 删除订单进度跟踪
     * @author yakun.bai
     * @Date 2016-12-7
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void delete(OrderProgressTracking OrderProgressTracking) {
    	progressDao.delete(OrderProgressTracking);
    }
    
    /**
     * 保存订单时添加修改或添加订单跟踪记录
     * flag 是否关联询单
     * @author yakun.bai
     * @Date 2016-8-16
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addOrderProgrssTracking(ProductOrderCommon productOrder, boolean flag) {
    	Integer orderType = productOrder.getOrderStatus();
    	Integer priceType = productOrder.getPriceType();
    	Long agentId = productOrder.getOrderCompany();
    	Long productId = productOrder.getProductId();
    	Long groupId = productOrder.getProductGroupId();
    	// 如果不是散拼订单并且是quauq渠道订单，则忽略
    	if (Context.ORDER_STATUS_LOOSE.equals(orderType.toString()) && priceType == 2) {
    		// 查询是否有对应的询价记录，查询条件为（渠道ID、产品ID、销售ID），按创建时间降序排列
    		Integer progressOrderType = (null != productOrder.getPreOrderId() ? 2 : 1);
    		Long salerId = productOrder.getSalerId().longValue();
    		
    		// 若填写了补单的“创建时间”，则关联早于等创建时间但不超过创建日期之前的30天的最近的一条询单数据
    		List<OrderProgressTracking> progressList = null;
    		if (productOrder.getIsAfterSupplement() == 1) {
    			progressList = progressDao.findEntityForBD(agentId, productId, progressOrderType, salerId, productOrder.getOrderTime());
    		} else {
    			progressList = progressDao.findByAgentIdAndProductIdAndSalerId(agentId, productId, progressOrderType, 
    					salerId,  productOrder.getOrderTime());
    		}
    		
    		if (!flag) {
    			progressList = null;
    		}
    		
    		if (CollectionUtils.isNotEmpty(progressList)) {
    			OrderProgressTracking progress = null;
    			if (progressOrderType == 2) {
    				progress = progressDao.findByPreOrderId(productOrder.getPreOrderId());
        		} else {
        			// 获取最近的询单记录
        			progress = progressList.get(0);
        		}
    			progress.setOrderId(productOrder.getId());
    			progress.setOrderNum(productOrder.getOrderNum());
    			progress.setOrderCreateTime(productOrder.getOrderTime());
    			progress.setOrderCreateName(productOrder.getCreateBy().getName());
    			progress.setActivityCreateName(activityService.findById(productOrder.getProductId()).getCreateBy().getName());
    			progress.setUpdateById(UserUtils.getUser().getId());
    			progress.setAskSalerId(productOrder.getSalerId().longValue());
				progress.setUpdateDate(new Date());
    			save(progress);
    		} else {
    			OrderProgressTracking progress = new OrderProgressTracking();
    			
    			List<User> userList = userDao.findByAgentId(agentId);
    			if (CollectionUtils.isNotEmpty(userList)) {
    				progress.setAskUserId(userList.get(0).getId());
    				progress.setAskAgentId(userList.get(0).getAgentId());
    			}
    			progress.setCompanyId(UserUtils.getCompanyIdForData());
    			progress.setActivityId(productOrder.getProductId());
    			progress.setGroupId(groupId);
    			progress.setOrderId(productOrder.getId());
    			progress.setOrderNum(productOrder.getOrderNum());
    			progress.setOrderCreateTime(productOrder.getOrderTime());
    			progress.setOrderCreateName(productOrder.getCreateBy().getName());
    			progress.setActivityCreateName(activityService.findById(productOrder.getProductId()).getCreateBy().getName());
    			progress.setUpdateById(UserUtils.getUser().getId());
				progress.setUpdateDate(new Date());
				progress.setAskSalerId(productOrder.getSalerId().longValue());
    			save(progress);
    		}
    	}
    }
    
    /**
     * 如果确认单都被删除，则订单跟踪确认单信息也被删除
     * @author yakun.bai
     * @Date 2016-8-29
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void removeConfirmationInfo(ProductOrderCommon productOrder) {
    	Integer orderType = productOrder.getOrderStatus();
    	Integer priceType = productOrder.getPriceType();
    	Long orderId = productOrder.getId();
    	// 如果不是散拼订单并且是quauq渠道订单，则忽略
    	if (Context.ORDER_STATUS_LOOSE.equals(orderType.toString()) && priceType == 2) {
    		// 查询是否有对应的询价记录
    		OrderProgressTracking progress = progressDao.findByOrderId(orderId);
    		if (progress != null) {
				progress.setConfirmationFileSalerId(null);
				progress.setConfirmationFileSalerTime(null);
				progress.setUpdateById(UserUtils.getUser().getId());
				progress.setUpdateDate(new Date());
				save(progress);
    		}
    	}
    }
    
    /**
     * 上传确认单
     * @author yakun.bai
     * @Date 2016-8-17
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void addConfirmationInfo(ProductOrderCommon productOrder) {
    	Integer orderType = productOrder.getOrderStatus();
    	Integer priceType = productOrder.getPriceType();
    	Long orderId = productOrder.getId();
    	// 如果不是散拼订单并且是quauq渠道订单，则忽略
    	if (Context.ORDER_STATUS_LOOSE.equals(orderType.toString()) && priceType == 2) {
    		// 查询是否有对应的询价记录
    		OrderProgressTracking progress = progressDao.findByOrderId(orderId);
    		if (null != progress && null == progress.getConfirmationFileSalerId()) {
				progress.setConfirmationFileSalerId(UserUtils.getUser().getId());
				progress.setConfirmationFileSalerTime(new Date());
				progress.setUpdateById(UserUtils.getUser().getId());
				progress.setUpdateDate(new Date());
				save(progress);
    		}
    	}
    }
    
    /**
     * 修改订单状态
     * @author yakun.bai
     * @Date 2016-12-4
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void updateOrderStatus(ProductOrderCommon productOrder, Integer orderStatus) {
    	Integer orderType = productOrder.getOrderStatus();
    	Integer priceType = productOrder.getPriceType();
    	Long orderId = productOrder.getId();
    	// 如果不是散拼订单并且是quauq渠道订单，则忽略
    	if (Context.ORDER_STATUS_LOOSE.equals(orderType.toString()) && priceType == 2) {
    		// 查询是否有对应的询价记录
    		OrderProgressTracking progress = progressDao.findByOrderId(orderId);
    		if (progress != null) {
    			if (orderStatus == Context.ORDER_ZZ_BH2_CANCEL) {
    				List<Orderpay> orderPayList = orderpayDao.findOrderPayOrderById(progress.getOrderId(), 2);
    				if (CollectionUtils.isEmpty(orderPayList)) {
    					progress.setFirstOrderPayTime(null);
    					progress.setLastOrderPayTime(null);
    					progress.setOrderStatus(orderStatus);
            			progress.setUpdateById(UserUtils.getUser().getId());
            			progress.setUpdateDate(new Date());
            			save(progress);
    				}
    			} else {
    				progress.setOrderStatus(orderStatus);
        			progress.setUpdateById(UserUtils.getUser().getId());
        			progress.setUpdateDate(new Date());
        			save(progress);
    			}
    		}
    	}
    }
    
    /**
     * 添加订单收款、达帐日期
     * @author yakun.bai
     * @Date 2016-8-17
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class) 
    void addPayInfoTime(ProductOrderCommon productOrder, boolean isFirstPay) {
    	Integer orderType = productOrder.getOrderStatus();
    	Integer priceType = productOrder.getPriceType();
    	Long orderId = productOrder.getId();
    	// 如果不是散拼订单并且是quauq渠道订单，则忽略
    	if (Context.ORDER_STATUS_LOOSE.equals(orderType.toString()) && priceType != null && priceType == 2) {
    		// 查询是否有对应的询价记录
    		OrderProgressTracking progress = progressDao.findByOrderId(orderId);
    		if (null != progress) {
    			List<Orderpay> orderPayList = orderpayDao.findOrderPayOrderById(progress.getOrderId(), 2);
        		if (CollectionUtils.isEmpty(orderPayList) || isFullPayed(productOrder)) {
        			if (CollectionUtils.isEmpty(orderPayList)) {
        				progress.setFirstOrderPayTime(new Date());
        			} else {
        				progress.setLastOrderPayTime(new Date());
        			}
        			progress.setOrderStatus(Context.ORDER_ZZ_NORMAL);
        			progress.setUpdateById(UserUtils.getUser().getId());
    				progress.setUpdateDate(new Date());
    				save(progress);
        		}
    		}
    	}
    }
}