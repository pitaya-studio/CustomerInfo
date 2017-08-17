package com.trekiz.admin.agentToOffice.T1.order.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.trekiz.admin.agentToOffice.T1.money.entity.T1MoneyAmount;
import com.trekiz.admin.agentToOffice.T1.money.service.T1MoneyAmountService;
import com.trekiz.admin.agentToOffice.T1.order.entity.T1PreOrder;
import com.trekiz.admin.agentToOffice.T1.order.repository.T1PreOrderDao;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.agentToOffice.personnalInfo.service.PersonInfoService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.order.entity.OrderProgressTracking;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderProgressTrackingService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class T1PreOrderService  extends BaseService {
    
	@Autowired
	private PersonInfoService personInfoService;
    @Autowired
    private ProductOrderCommonDao productorderDao;
    @Autowired
	private AgentinfoService agentinfoService;
    @Autowired
	private ActivityGroupDao groupDao;
    @Autowired
	private OfficeDao officeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
    private T1PreOrderDao t1PreOrderDao;
    @Autowired
    private T1MoneyAmountService moneyAmountService;
    @Autowired
	private QuauqAgentInfoService quauqAgentInfoService;
    @Autowired
    private OrderProgressTrackingService orderProgressTrackingService;
    
    public T1PreOrder findById(Long orderId) {
    	return t1PreOrderDao.findOne(orderId);
    }
    
	/**
	 * 订单查询：T1平台
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
    public Object showOrderList(HttpServletResponse response, HttpServletRequest request) throws Exception {
    	//查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        //参数处理：去除空格和处理特殊字符并传递到后台
        //参数解释：出团开始日期、出团结束日期、下单开始时间、下单结束时间、批发商ID、金额最小值、金额最大值、订单状态、产品名称或团号、渠道、登陆账号
        String paras = "groupOpenDateBegin,groupOpenDateEnd,orderTimeBegin,orderTimeEnd,companyIds,moneyStrMin,moneyStrMax,orderStatus," +
        				"activityNameOrGroupCode,agentId,userId";
        OrderCommonUtil.handlePara(paras, mapRequest, request);
        
        //排序方式：默认为出团日期降序排列
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "orderTime DESC";
        }
        mapRequest.put("orderBy", orderBy);
        
        Page<Map<Object, Object>> page = new Page<Map<Object, Object>>(request, response);
        
		page.setOrderBy(orderBy);
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		if (StringUtils.isNotBlank(pageNo)) {
			page.setPageNo(Integer.valueOf(pageNo));
		}
		if (StringUtils.isNotBlank(pageSize)) {
			page.setPageSize(Integer.valueOf(pageSize));
		}
        
        Page<Map<Object, Object>> pageOrder = (Page<Map<Object, Object>>) getOrderList(page, mapRequest);
        
        List<Map<Object, Object>> listorder = pageOrder.getList();

        JSONObject result = new JSONObject();
        
        JSONArray orderArr = new JSONArray();
        JSONObject orderInfo = null;
        
        User currentUser = UserUtils.getUser();
        boolean isT1User = "1".equals(currentUser.getIsQuauqAgentLoginUser());
        
        Date nowDate = DateUtils.string2Date(DateUtils.date2String(new Date()));
        
        for (Map<Object, Object> map : listorder) {
        	orderInfo = new JSONObject();
        	setJSONData(orderInfo, "id", map.get("id"));
        	setJSONData(orderInfo, "orderNum", map.get("orderNum"));
        	setJSONData(orderInfo, "productId", map.get("productId"));
        	setJSONData(orderInfo, "acitivityName", map.get("acitivityName"));
        	setJSONData(orderInfo, "officeName", map.get("officeName"));
        	setJSONData(orderInfo, "groupId", map.get("groupId"));
        	setJSONData(orderInfo, "groupCode", map.get("groupCode"));
        	setJSONData(orderInfo, "groupOpenDate", map.get("groupOpenDate"));
        	setJSONData(orderInfo, "orderPersonNum", map.get("orderPersonNum"));
        	setJSONData(orderInfo, "differenceMoney", handlePrice(map.get("differenceMoney")));
        	setJSONData(orderInfo, "salerId", map.get("salerId"));
        	setJSONData(orderInfo, "agentId", map.get("agentId"));
        	setJSONData(orderInfo, "orderTimeForT2", map.get("orderTimeForT2"));
        	setJSONData(orderInfo, "remark", map.get("remark"));
        	setJSONData(orderInfo, "isT1", map.get("isT1"));
        	
        	setJSONData(orderInfo, "payMode", map.get("payMode"));
        	
        	orderInfo.put("totalMoney", handlePrice(map.get("totalMoney")));
        	orderInfo.put("companyMoney", handlePrice(map.get("companyMoney")));
        	
        	if (isT1User) {
        		User user = UserUtils.getUser(Long.parseLong(map.get("salerId").toString()));
            	// 批发商联系人、电话
            	orderInfo.put("salerName", user.getName());
            	orderInfo.put("salerPhone", user.getMobile());
        	} else {
        		User user = UserUtils.getUser(Long.parseLong(map.get("createBy").toString()));
            	orderInfo.put("agentName", user.getName());
            	// 批发商联系人、电话
            	orderInfo.put("loginName", user.getLoginName());
        	}
        	orderInfo.put("orderStatus", getOrderStatus(Integer.parseInt(map.get("orderStatus").toString())));
        	String orderTime = map.get("orderTime").toString();
        	String hasSeen = map.get("hasSeen").toString();
        	orderInfo.put("orderTime", orderTime.substring(0, orderTime.length() -2));
        	if (DateUtils.dateFormat(orderTime).after(nowDate) && "0".equals(hasSeen)) {
        		orderInfo.put("newOrderFlag", "1");
        	} else {
        		orderInfo.put("newOrderFlag", "0");
        	}
        	
        	orderArr.add(orderInfo);
        }
        result.put("orderInfo", orderArr);
        
        JSONObject pageInfo = new JSONObject();
        pageInfo.put("pageNo", pageOrder.getPageNo());
        pageInfo.put("pageSize", pageOrder.getPageSize());
        pageInfo.put("count", pageOrder.getCount());
        result.put("pageInfo", pageInfo);
        
        return result;
    }
    
	private void setJSONData(JSONObject json, String key, Object obj) {
		if (null == obj) {
			json.put(key, "");
		} else {
			json.put(key, obj.toString());
		}
	}
	
	/**
	 * 订单金额千位符处理：订单总金额、已付金额、到账金额
	 * @param listin
	 * @param paraList
	 */
	private String handlePrice(Object moneyStr) {
		
		if (null == moneyStr || StringUtils.isBlank(moneyStr.toString())) {
			return "";
		} else {
			//千位符
			DecimalFormat d = new DecimalFormat(",##0.00");
			String allMoney [] = moneyStr.toString().split("\\+");
			if (allMoney.length > 1) {
				String tempMoneyStr = "";
				for (int i=0;i<allMoney.length;i++) {
					String money [] = allMoney[i].split(" ");
					//币种价格等于0的时候不显示
					if (money.length > 1 && !"0.00".equals(money[1])) {
						tempMoneyStr += money[0] + d.format(new BigDecimal(money[1])) + "+";
					}
				}
				if(StringUtils.isNotBlank(tempMoneyStr)) {
					return tempMoneyStr.substring(0, tempMoneyStr.length()-1);
				}
			} else {
				String money [] = allMoney[0].split(" ");
				if (money.length > 1) {
					String currencyMark = money[0].toString();
					String currencyMoney = money[1].toString();
					String moneyAmonut = d.format(new BigDecimal(currencyMoney));
					return currencyMark + moneyAmonut;
				}
			}
		}
		return "";
	}
	
    /**
     * 订单列表查询
     * @param type 订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5 已经支付；99 已取消；111 已删除订单
     * @param orderStatus 订单类型：1 单团；2 散拼；3 游学；4 大客户；5 自由行
     * @param page
     * @param travelActivity
     * @param orderBy 排序字段
     * @param map
     * @param common
     * @return
     */
    private Page<Map<Object, Object>> getOrderList(Page<Map<Object, Object>> page, Map<String,String> map) {
    	
    	//排序
    	String orderBy = map.get("orderBy");
    	page.setOrderBy(orderBy);
    	
    	//sql语句where条件
        String where = this.getWhereSql(map);

        //获取订单查询sql语句
        String orderStatus = map.get("orderStatus");
        return getOrderList(page, where, orderStatus);
    }     
    
    /**
     * 获取订单查询where条件
     * @author yakun.bai
     * @Date 2016-5-10
     */
    private String getWhereSql(Map<String,String> map) {
    	
        StringBuffer sqlWhere = new StringBuffer("");
        
        User currentUser = UserUtils.getUser();
        boolean isT1User = "1".equals(currentUser.getIsQuauqAgentLoginUser());
        
        if (isT1User) {
        	sqlWhere.append(" and pro.createBy = " + currentUser.getId() + " ");
        } else {
        	sqlWhere.append(" and activity.proCompany = " + currentUser.getCompany().getId() + " ");
        	if (!UserUtils.hasRole(currentUser, Context.ROLE_TYPE_MANAGER)) {
        		sqlWhere.append(" and pro.salerId = " + currentUser.getId() + " ");
        	}
        }
        
        // 订单状态
        String orderStatus = map.get("orderStatus");
        if (StringUtils.isNotBlank(orderStatus) && !"0".equals(orderStatus)) {
        	if (isT1User) {
        		sqlWhere.append(" and pro.orderStatus in (" + orderStatus + ") ");
        	} else {
        		sqlWhere.append(" and pro.orderStatus = " + orderStatus + " ");
        	}
        } else {
        	sqlWhere.append(" and pro.orderStatus != 4 ");
        }
        
        // 产品名称或团期团号
        String activityNameOrGroupCode = map.get("activityNameOrGroupCode");
        if (StringUtils.isNotBlank(activityNameOrGroupCode) && !"产品名称 / 提交编号".equals(activityNameOrGroupCode)) {
        	
        	if (isT1User) {
        		sqlWhere.append(" and (activity.acitivityName like '%" + activityNameOrGroupCode + "%' or pro.orderNum like '%" + activityNameOrGroupCode + "%') ");
            } else {
            	sqlWhere.append(" and (activity.acitivityName like '%" + activityNameOrGroupCode + "%' or agp.groupCode like '%" + activityNameOrGroupCode + "%') ");
            }
        }
        
        // 团期出团日期
        String groupOpenDateBegin = map.get("groupOpenDateBegin");
        String groupOpenDateEnd = map.get("groupOpenDateEnd");
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
        	sqlWhere.append(" and agp.groupOpenDate >= '" + groupOpenDateBegin + "'");
        }
        if (StringUtils.isNotBlank(groupOpenDateEnd)){
            sqlWhere.append(" and agp.groupOpenDate  <= '" + groupOpenDateEnd + "'");
        }
        
        // 订单创建时间
        String orderTimeBegin = map.get("orderTimeBegin");
        String orderTimeEnd = map.get("orderTimeEnd");
        if (StringUtils.isNotBlank(orderTimeBegin)) {
            sqlWhere.append( " and pro.orderTime >= '" + orderTimeBegin + " 00:00:00" + "'");
        }
        if (StringUtils.isNotBlank(orderTimeEnd)) {
            sqlWhere.append( " and pro.orderTime <= '" + orderTimeEnd + " 23:59:59" + "'");
        }
        
        // 供应商
        String companyIds = map.get("companyIds");
        if (StringUtils.isNotBlank(companyIds)){
        	sqlWhere.append(" and office.id in (" + companyIds + ") ");
        }
        
        //订单金额搜索
        String moneyStrMin = map.get("moneyStrMin");
        String moneyStrMax = map.get("moneyStrMax");
        if (StringUtils.isNotBlank(moneyStrMin) && StringUtils.isNotBlank(moneyStrMax)) {
        	sqlWhere.append(" and ((totalOuter.moneyStrMin >= " + moneyStrMin + " and totalOuter.moneyStrMax <= " + moneyStrMax + ") or ");
        	sqlWhere.append(" (companyOuter.companyMin >= " + moneyStrMax + " and companyOuter.companyMax <= " + moneyStrMax + "))");
        } else {
        	if (StringUtils.isNotBlank(moneyStrMin)) {
            	sqlWhere.append(" and (totalOuter.moneyStrMin >= " + moneyStrMin + " or companyOuter.companyMin >= " + moneyStrMin + ") ");
            }
            if(StringUtils.isNotBlank(moneyStrMax)){
            	sqlWhere.append(" and (totalOuter.moneyStrMax <= " + moneyStrMax + " or companyOuter.companyMax <= " + moneyStrMax + ")");
            }
        }
        
        // 渠道
        String agentId = map.get("agentId");
        if (StringUtils.isNotBlank(agentId)){
        	sqlWhere.append(" and users.agentId = " + agentId + " ");
        }
        // 用户
        String userId = map.get("userId");
        if (StringUtils.isNotBlank(userId)){
        	sqlWhere.append(" and pro.createBy = " + userId + " ");
        }
        
        return sqlWhere.toString();
    }
    
    /**
     *  订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5 已经支付；7 计调占位；99 已取消；111 已删除订单
     * 设置查询订单sql语句
     * @param orderStatus
     * @param where 订单查询where语句
     * @return
     */
    private Page<Map<Object, Object>> getOrderList(Page<Map<Object, Object>> page, String where, String orderStatus) {
		String payStatusSql = "";
    	
    	
    	//订单查询sql语句
    	StringBuilder sql = new StringBuilder("");
    	sql.append("SELECT pro.id, pro.orderNum, pro.orderTime orderTime, activity.id productId, activity.acitivityName, office.name officeName, ")
    				.append("agp.id groupId, agp.groupCode, agp.groupOpenDate, agp.is_t1 isT1, pro.orderPersonNum, pro.hasSeen, pro.orderTimeForT2, ")
    				.append("totalOuter.moneyStr AS totalMoney, companyOuter.moneyStr AS companyMoney, differenceOuter.moneyStr differenceMoney, ")
    				.append("salerId, pro.orderStatus, pro.remark, pro.createBy, users.agentId, ")
    				.append("activity.payMode, ")
					.append("totalOuter.moneyStrMin, ")
					.append("totalOuter.moneyStrMax ")
				.append("FROM travelactivity activity, ")
					.append("activitygroup agp, ")
					.append("sys_office office, ")
					.append("sys_user users, ")
					.append("t1_pre_order pro ") 

				//订单应收金额多币种查询
				.append(" LEFT JOIN ( ")
					.append("SELECT mao.serialNum, ")
						.append("MIN(mao.amount ) as moneyStrMin, ")
						.append("MAX(mao.amount ) as moneyStrMax, ")
						.append("GROUP_CONCAT(CONCAT ( ")
							.append("c.currency_mark, ' ', ")
							.append("mao.amount ")
							.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
					.append("FROM t1_money_amount mao ")
					.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
					.append("GROUP BY mao.serialNum ")
					.append(") totalOuter ON totalOuter.serialNum = pro.total_money ")
					
				//订单系统结算价金额查询
				.append("LEFT JOIN ( ")
					.append("SELECT mao.serialNum, ")
						.append("MIN(mao.amount ) as companyMin, ")
						.append("MAX(mao.amount ) as companyMax, ")
						.append("GROUP_CONCAT(CONCAT ( ")
							.append("c.currency_mark, ' ', ")
							.append("mao.amount ")
							.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
					.append("FROM t1_money_amount mao ")
					.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
					.append("GROUP BY mao.serialNum ")
					.append(") companyOuter ON companyOuter.serialNum = pro.company_money ")
					
				// 订单门店结算价差额返还金额查询
				.append("LEFT JOIN ( ")
				.append("SELECT mao.serialNum, ")
				.append("GROUP_CONCAT(CONCAT ( ")
				.append("c.currency_mark, ' ', ")
				.append("mao.amount ")
				.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
				.append("FROM t1_money_amount mao ")
				.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
				.append("GROUP BY mao.serialNum ")
				.append(") differenceOuter ON differenceOuter.serialNum = pro.difference_money ")
				
				.append("WHERE activity.id = pro.productId ")
					.append("AND office.id = activity.proCompany ")
					.append("AND pro.createBy = users.id ")
					.append("AND agp.id = pro.productGroupId AND agp.srcActivityId = pro.productId ")
					.append(payStatusSql)
					.append(where);
    	Page<Map<Object, Object>> pageMap = productorderDao.findPageBySql(page, sql.toString(), Map.class);
        return pageMap;
    }
    
	/**
	 * 获取T2有quauq报名权限用户
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-18
	 */
	public Object getCompanySaler(HttpServletRequest request, HttpServletResponse response) {
		
		String groupId = request.getParameter("groupId");
		
		ActivityGroup group = groupDao.findById(Long.parseLong(groupId));
		
		TravelActivity activity = group.getTravelActivity();
		
		
		/** 联系人信息 */
		JSONObject contacts = new JSONObject();
		// 联系人
		List<User> userList = userDao.findCompanyT1User(activity.getProCompany());
		JSONArray userArr = new JSONArray();
		if (CollectionUtils.isNotEmpty(userList)) {
			JSONObject userInfo = null;
			for (User user : userList) {
				userInfo = new JSONObject();
				userInfo.put("salerId", user.getId());
				userInfo.put("salerName", user.getName());
				userInfo.put("salerPhone", user.getPhone());
				userArr.add(userInfo);
			}
		}
		contacts.put("contactsDetail", userArr);
		
		return contacts;
	}
	
	/**
	 * 获取T2批发商
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-18
	 */
	public Object getCompany(HttpServletRequest request, HttpServletResponse response) {
		
		JSONObject result = new JSONObject();
		
		List<Office> officeList = officeDao.findByShelfRightsStatus(0);
        JSONArray officeArr = new JSONArray();
        JSONObject officeInfo = null;
        for (Office office : officeList) {
        	officeInfo = new JSONObject();
        	officeInfo.put("officeId", office.getId());
        	officeInfo.put("officeName", office.getName());
        	officeArr.add(officeInfo);
        }
        result.put("officeInfo", officeArr);
        
        return result;
	}
	
	/**
	 * 获取T2批发商
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-18
	 */
	public Object getSearchInfo(HttpServletRequest request, HttpServletResponse response) {
		
		JSONObject result = new JSONObject();
		
		// 获取渠道信息
		List<Agentinfo> agentList = quauqAgentInfoService.getAllQuauqAgentinfos();
		JSONArray agentArr = new JSONArray();
		JSONObject agentInfo = null;
		Set<Long> notInSet = Sets.newHashSet();
		for (Agentinfo agent : agentList) {
			if ("1".equals(agent.getEnableQuauqAgent())) {
				agentInfo = new JSONObject();
				agentInfo.put("agentId", agent.getId());
				agentInfo.put("agentName", agent.getAgentName());
				agentArr.add(agentInfo);
			} else {
				notInSet.add(agent.getId());
			}
		}
		result.put("agentInfo", agentArr);
		
		// 获取登陆账号信息
		List<User> userList = userDao.findQuauqUser();
		JSONArray userArr = new JSONArray();
		JSONObject userInfo = null;
		for (User user : userList) {
			if (!notInSet.contains(user.getAgentId())) {
				userInfo = new JSONObject();
				userInfo.put("userId", user.getId());
				userInfo.put("loginName", user.getLoginName());
				userArr.add(userInfo);
			}
		}
		result.put("userInfo", userArr);
		
		// 获取订单状态信息
		JSONObject orderStatusInfo = new JSONObject();
		orderStatusInfo.put("1", "待处理");
		orderStatusInfo.put("2", "已下单");
		orderStatusInfo.put("3", "已取消");
		orderStatusInfo.put("4", "已删除");
		result.put("orderStatusInfo", orderStatusInfo);
		
		return result;
	}
	
	
	/**
	 * 获取订单详情
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-18
	 */
	public Object getOrderDetail(HttpServletRequest request, HttpServletResponse response) {
		
		String groupId = request.getParameter("groupId"); // 团期ID
		String salerId = request.getParameter("salerId"); // 销售ID
		String orderId = request.getParameter("orderId"); // 订单ID
		
		// 如果团期ID和销售ID不为空则表示下单请求；如果订单ID不为空则表示订单详情请求
		if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(salerId)) {
			return getOrderDetailForSaveOrder(groupId, salerId, request);
		} else if (StringUtils.isNotBlank(orderId)) {
			return getOrderDetailByOrderId(orderId);
		} else {
			return null;
		}
		
	}
	
	/**
	 * 获取订单详情：未生成订单
	 * @param groupId
	 * @param salerId
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	private Object getOrderDetailForSaveOrder(String groupId, String salerId, HttpServletRequest request) {
		
		JSONObject results = new JSONObject();
		//用于支付信息判断是T1还是T2
		results.put("user", "t1");
		ActivityGroup group = groupDao.findById(Long.parseLong(groupId));
		
		TravelActivity activity = group.getTravelActivity();
		
		/** 产品信息 */
		setActivityInfo(activity, group, results);
		
		/** 联系人信息 */
		setContactsInfo(activity, Long.parseLong(salerId), results);
		
		/** 交易信息 */
		JSONObject transDetail = new JSONObject();
		// 团期价格
		String currency = group.getCurrencyType();
		String[] currencyArr = currency.split(",");
		Rate rate = RateUtils.getRate(group.getId(), 2, UserUtils.getUser().getAgentId());
		
		Currency adultCurrency = currencyDao.findById(Long.parseLong(currencyArr[0]));
		Currency childCurrency = currencyDao.findById(Long.parseLong(currencyArr[1]));
		Currency specialCurrency = currencyDao.findById(Long.parseLong(currencyArr[2]));
		
		BigDecimal adultPrice = null;
		String adultPriceStr = request.getParameter("adultPrice");
		if (StringUtils.isNotBlank(adultPriceStr)) {
			adultPrice = new BigDecimal(adultPriceStr);
		}
		BigDecimal childPrice = null;
		String childPriceStr = request.getParameter("childPrice");
		if (StringUtils.isNotBlank(childPriceStr)) {
			childPrice = new BigDecimal(childPriceStr);
		}
		BigDecimal specialPrice = null;
		String specialPriceStr = request.getParameter("specialPrice");
		if (StringUtils.isNotBlank(specialPriceStr)) {
			specialPrice = new BigDecimal(specialPriceStr);
		}
		
		BigDecimal companyAdultPrice = OrderCommonUtil.getRetailPrice(group.getSettlementAdultPrice(), group.getQuauqAdultPrice(), rate, adultCurrency.getId());
		if (null != companyAdultPrice) {
			companyAdultPrice = companyAdultPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		BigDecimal companyChildPrice = OrderCommonUtil.getRetailPrice(group.getSettlementcChildPrice(), group.getQuauqChildPrice(), rate, childCurrency.getId());
		if (null != companyChildPrice) {
			companyChildPrice = companyChildPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		BigDecimal companySpecialPrice = OrderCommonUtil.getRetailPrice(group.getSettlementSpecialPrice(), group.getQuauqSpecialPrice(), rate, specialCurrency.getId());
		if (null != companySpecialPrice) {
			companySpecialPrice = companySpecialPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		transDetail.put("adultCurrencyId", adultCurrency.getId());
		transDetail.put("adultPrice", adultPrice == null ? "--" : adultPrice);
		transDetail.put("adultCurrencyMark", adultCurrency.getCurrencyMark());
		transDetail.put("childCurrencyId", childCurrency.getId());
		transDetail.put("childPrice", childPrice == null ? "--" : childPrice);
		transDetail.put("childCurrencyMark", childCurrency.getCurrencyMark());
		transDetail.put("specialCurrencyId", specialCurrency.getId());
		transDetail.put("specialPrice", specialPrice == null ? "--" : specialPrice);
		transDetail.put("specialCurrencyMark", specialCurrency.getCurrencyMark());
		
		transDetail.put("companyAdultCurrencyId", adultCurrency.getId());
		transDetail.put("companyAdultPrice", companyAdultPrice == null ? "--" : companyAdultPrice);
		transDetail.put("companyAdultCurrencyMark", adultCurrency.getCurrencyMark());
		transDetail.put("companyChildCurrencyId", childCurrency.getId());
		transDetail.put("companyChildPrice", companyChildPrice == null ? "--" : companyChildPrice);
		transDetail.put("companyChildCurrencyMark", childCurrency.getCurrencyMark());
		transDetail.put("companySpecialCurrencyId", specialCurrency.getId());
		transDetail.put("companySpecialPrice", companySpecialPrice == null ? "--" : companySpecialPrice);
		transDetail.put("companySpecialCurrencyMark", specialCurrency.getCurrencyMark());
		
		// 成人人数：如果为空则默认为0
		Integer adultNum = null;
		if (StringUtils.isNotBlank(request.getParameter("adultNum"))) {
			adultNum = Integer.parseInt(request.getParameter("adultNum"));
		} else {
			adultNum = 0;
		}
		// 儿童人数：如果为空则默认为0
		Integer childNum = null;
		if (StringUtils.isNotBlank(request.getParameter("childNum"))) {
			childNum = Integer.parseInt(request.getParameter("childNum"));
		} else {
			childNum = 0;
		}
		// 特殊人群人数：如果为空则默认为0
		Integer specialNum = null;
		if (StringUtils.isNotBlank(request.getParameter("specialNum"))) {
			specialNum = Integer.parseInt(request.getParameter("specialNum"));
		} else {
			specialNum = 0;
		}
		
		transDetail.put("adultNum", adultNum);
		transDetail.put("childNum", childNum);
		transDetail.put("specialNum", specialNum);
		
		// 实际成人、儿童、特殊人群结算价
		String adultSum = moneyAmountService.getSumMoneyStr(adultPrice, adultNum);
		String childSum = moneyAmountService.getSumMoneyStr(childPrice, childNum);
		String specialSum = moneyAmountService.getSumMoneyStr(specialPrice, specialNum);
		// 系统成人、儿童、特殊人群结算价
		String companyAdultSum = moneyAmountService.getSumMoneyStr(companyAdultPrice, adultNum);
		String companyChildSum = moneyAmountService.getSumMoneyStr(companyChildPrice, childNum);
		String companySpecialSum = moneyAmountService.getSumMoneyStr(companySpecialPrice, specialNum);
		
		transDetail.put("adultSum", adultSum);
		transDetail.put("childSum", childSum);
		transDetail.put("specialSum", specialSum);
		
		// 获取利润
		String profitsSum = moneyAmountService.getProfitsStr(adultSum, companyAdultSum, childSum, companyChildSum, specialSum, companySpecialSum);
		
		transDetail.put("profitsSum", profitsSum);
		results.put("transDetail", transDetail);
		results.put("remarks", "");
		
		// 支付信息
		setPayInfo(results, true);
		
		return results;
	}
	
	/**
	 * 获取订单详情：已生成订单
	 * @param groupId
	 * @param salerId
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	private Object getOrderDetailByOrderId(String orderId) {
		
		JSONObject results = new JSONObject();
		//用于支付信息判断是T1还是T2
		results.put("user", "t2"); 
		T1PreOrder order = t1PreOrderDao.findOne(Long.parseLong(orderId));
		results.put("orderCode", order.getOrderNum());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		results.put("orderSubmitDate", format.format(order.getCreateDate()));
		ActivityGroup group = groupDao.findById(order.getProductGroupId());
		
		TravelActivity activity = group.getTravelActivity();
		
		/** 产品信息 */
		setActivityInfo(activity, group, results);
		
		/** 联系人信息 */
		setContactsInfo(activity, order.getSalerId(), results);
		
		/** 交易信息 */
		JSONObject transDetail = new JSONObject();
		// 团期价格
		String currency = group.getCurrencyType();
		String[] currencyArr = currency.split(",");
		
		Currency adultCurrency = currencyDao.findById(Long.parseLong(currencyArr[0]));
		Currency childCurrency = currencyDao.findById(Long.parseLong(currencyArr[1]));
		Currency specialCurrency = currencyDao.findById(Long.parseLong(currencyArr[2]));
		
		List<T1MoneyAmount> audultMoneyList = moneyAmountService.findBySerialNum(order.getAdultPrice());
		List<T1MoneyAmount> childMoneyList = moneyAmountService.findBySerialNum(order.getChildPrice());
		List<T1MoneyAmount> specialMoneyList = moneyAmountService.findBySerialNum(order.getSpecialPrice());
		
		BigDecimal adultPrice = CollectionUtils.isEmpty(audultMoneyList) ? null : audultMoneyList.get(0).getAmount();
		BigDecimal childPrice = CollectionUtils.isEmpty(childMoneyList) ? null : childMoneyList.get(0).getAmount();
		BigDecimal specialPrice = CollectionUtils.isEmpty(specialMoneyList) ? null : specialMoneyList.get(0).getAmount();
		
		List<T1MoneyAmount> companyAdultPriceList = moneyAmountService.findBySerialNum(order.getCompanyAdultPrice());
		List<T1MoneyAmount> companyChildPriceList = moneyAmountService.findBySerialNum(order.getCompanyChildPrice());
		List<T1MoneyAmount> companySpecialPriceList = moneyAmountService.findBySerialNum(order.getCompanySpecialPrice());
		
		BigDecimal companyAdultPrice = CollectionUtils.isEmpty(companyAdultPriceList) ? null : companyAdultPriceList.get(0).getAmount();
		BigDecimal companyChildPrice = CollectionUtils.isEmpty(companyChildPriceList) ? null : companyChildPriceList.get(0).getAmount();
		BigDecimal companySpecialPrice = CollectionUtils.isEmpty(companySpecialPriceList) ? null : companySpecialPriceList.get(0).getAmount();
		
		transDetail.put("adultCurrencyId", adultCurrency.getId());
		transDetail.put("adultPrice", adultPrice == null ? "--" : adultPrice);
		transDetail.put("adultCurrencyMark", adultCurrency.getCurrencyMark());
		transDetail.put("childCurrencyId", childCurrency.getId());
		transDetail.put("childPrice", childPrice == null ? "--" : childPrice);
		transDetail.put("childCurrencyMark", childCurrency.getCurrencyMark());
		transDetail.put("specialCurrencyId", specialCurrency.getId());
		transDetail.put("specialPrice", specialPrice == null ? "--" : specialPrice);
		transDetail.put("specialCurrencyMark", specialCurrency.getCurrencyMark());
		
		transDetail.put("companyAdultCurrencyId", adultCurrency.getId());
		transDetail.put("companyAdultPrice", companyAdultPrice == null ? "--" : companyAdultPrice);
		transDetail.put("companyAdultCurrencyMark", adultCurrency.getCurrencyMark());
		transDetail.put("companyChildCurrencyId", childCurrency.getId());
		transDetail.put("companyChildPrice", companyChildPrice == null ? "--" : companyChildPrice);
		transDetail.put("companyChildCurrencyMark", childCurrency.getCurrencyMark());
		transDetail.put("companySpecialCurrencyId", specialCurrency.getId());
		transDetail.put("companySpecialPrice", companySpecialPrice == null ? "--" : companySpecialPrice);
		transDetail.put("companySpecialCurrencyMark", specialCurrency.getCurrencyMark());
		
		transDetail.put("adultNum", order.getOrderPersonNumAdult());
		transDetail.put("childNum", order.getOrderPersonNumChild());
		transDetail.put("specialNum", order.getOrderPersonNumSpecial());
		
		transDetail.put("adultSum", moneyAmountService.getSumMoneyStr(adultPrice, order.getOrderPersonNumAdult()));
		transDetail.put("childSum", moneyAmountService.getSumMoneyStr(childPrice, order.getOrderPersonNumChild()));
		transDetail.put("specialSum", moneyAmountService.getSumMoneyStr(specialPrice, order.getOrderPersonNumSpecial()));
		
		transDetail.put("profitsSum", moneyAmountService.findBySerialNum(order.getDifferenceMoney()).get(0).getAmount());
		results.put("transDetail", transDetail);
		results.put("remarks", order.getRemark());
		results.put("payId", order.getPayId());
		results.put("orderStatus", getOrderStatus(order.getOrderStatus()));
		results.put("orderAgentId",order.getAgentId());
		Date orderTimeForT2 = order.getOrderTimeForT2();
		if (order.getOrderStatus().intValue() != 2) {
			results.put("orderTimeForT2", null == order.getUpdateDate() ? "" : DateUtils.date2String(order.getUpdateDate()));
		} else {
			results.put("orderTimeForT2", null == order.getOrderTimeForT2() ? "" : DateUtils.date2String(orderTimeForT2));
		}
		
		// 支付信息
		setPayInfo(results, false);
		
		return results;
	}
	
	/**
	 * 获取产品信息
	 * @param activity
	 * @param group
	 * @param results
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	private void setActivityInfo(TravelActivity activity, ActivityGroup group, JSONObject results) {
		
		/** 产品信息 */
		JSONObject productInfo = new JSONObject();
		// 产品名称
		productInfo.put("productName", activity.getAcitivityName());
		// 团号
		productInfo.put("groupCode", group.getGroupCode());
		// 出团日期
		productInfo.put("groupOpenDate", DateUtils.date2String(group.getGroupOpenDate()));
		// 出发城市
		productInfo.put("fromArea", activity.getFromAreaName() == null ? "" : activity.getFromAreaName());
		// 行程天数
		productInfo.put("activityDuration", activity.getActivityDuration());
		// 交通工具
		productInfo.put("trafficMode", activity.getTrafficModeName() == null ? "" : activity.getTrafficModeName());
		results.put("productInfo", productInfo);
	}
	
	/**
	 * 获取联系人
	 * @param activity 产品
	 * @param salerId 销售ID
	 * @param results 返回结果
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	private void setContactsInfo(TravelActivity activity, Long salerId, JSONObject results) {
		
		/** 联系人信息 */
		JSONObject contacts = new JSONObject();
		// 批发商名称
		Office office = officeDao.findById(activity.getProCompany());
		contacts.put("companyName", office.getName());
		// 联系人
		User user = userDao.findById(salerId);
		contacts.put("salerId", user.getId());
		contacts.put("salerName", user.getName());
		contacts.put("salerPhone", user.getMobile());
		results.put("contacts", contacts);
	}
	
	/**
	 * 获取产品信息
	 * @param activity
	 * @param group
	 * @param results
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	private void setPayInfo(JSONObject results, boolean isCreate) {
		/** 支付信息 */
		List<PlatBankInfo> platBankInfos = null;
		if(results.getString("user").equals("t1")){
			platBankInfos = personInfoService.getPlatBankInfoByAgentIdForT1T2(UserUtils.getUser().getAgentId().intValue());
		}else{
			platBankInfos = personInfoService.getPlatBankInfoByAgentIdForT1T2(Integer.parseInt(results.getString("orderAgentId")));
		}
		
		JSONObject payInfo = new JSONObject();
		JSONArray bankArr = new JSONArray();
		JSONArray ZFBArr = new JSONArray();
		JSONArray WXArr = new JSONArray();
		
		if (CollectionUtils.isNotEmpty(platBankInfos)) {
			User loginUser = UserUtils.getUser();
			for (PlatBankInfo bank : platBankInfos) {
				if (isCreate && bank.getDelFlag().equals("1")) {
					continue;
				}
				Integer accountPayType = bank.getAccountPayType();
				JSONObject payObject = new JSONObject();
				payObject.put("payId", bank.getId());
				if (null == accountPayType || accountPayType == 3) {
					payObject.put("accountPayType", 3);
					payObject.put("bankName", bank.getBankName());
					String bankCode = bank.getBankAccountCode();
					payObject.put("bankCodeEnd", null == bankCode ? "" : bankCode);
//					bankCode.substring(bankCode.length()-4, bankCode.length())
					payObject.put("userName", loginUser.getName());
					bankArr.add(payObject);
				} else if (1 == accountPayType) {
					payObject.put("accountPayType", 1);
					payObject.put("accountCode", bank.getBankAccountCode());
					payObject.put("userName", loginUser.getName());
					WXArr.add(payObject);
				} else if (2 == accountPayType) {
					payObject.put("accountPayType", 2);
					payObject.put("accountCode", bank.getBankAccountCode());
					payObject.put("userName", loginUser.getName());
					ZFBArr.add(payObject);
				}
			}
		}
		payInfo.put("bankInfo", bankArr);
		payInfo.put("zfbInfo", ZFBArr);
		payInfo.put("wxInfo", WXArr);
		results.put("payInfo", payInfo);
	}
	
	/**
	 * 获取订单状态
	 * @param orderStatus
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	private String getOrderStatus(Integer orderStatus) {
		if (1 == orderStatus) {
			return "待处理";
		} else if (2 == orderStatus) {
			return "已下单";
		} else if (3 == orderStatus) {
			return "已取消";
		} else if (4 == orderStatus) {
			return "已删除";
		} else {
			return "待处理";
		}
	}
	
	/**
	 * 保存订单
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-17
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Object saveOrder(HttpServletRequest request, HttpServletResponse response) {
		
		JSONObject results = new JSONObject();
		
		String groupId = request.getParameter("groupId");
		String orderPersonNum = request.getParameter("orderPersonNum");
		String payId = request.getParameter("payId");
		
		if (StringUtils.isBlank(groupId) || StringUtils.isBlank(orderPersonNum) || "0".equals(orderPersonNum)) {
			results.put("result", "error");
			return results;
		}
		
		ActivityGroup group = groupDao.findById(Long.parseLong(groupId));
		
		String currency = group.getCurrencyType();
		String[] currencyArr = currency.split(",");
		User user = UserUtils.getUser();
		Long userId = user.getId();
		
		
		
		String orderPersonNumAdult = request.getParameter("adultNum");
		if (StringUtils.isBlank(orderPersonNumAdult)) {
			orderPersonNumAdult = "0";
		}
		String orderPersonNumChild = request.getParameter("childNum");
		if (StringUtils.isBlank(orderPersonNumChild)) {
			orderPersonNumChild = "0";
		}
		String orderPersonNumSpecial = request.getParameter("specialNum");
		if (StringUtils.isBlank(orderPersonNumSpecial)) {
			orderPersonNumSpecial = "0";
		}
		String adultPrice = request.getParameter("adultPrice");
		String childPrice = request.getParameter("childPrice");
		String specialPrice = request.getParameter("specialPrice");
		
		Rate rate = RateUtils.getRate(group.getId(), 2, user.getAgentId());
    	// 获取推广价（包含渠道费率）
		BigDecimal companyAdultPrice = OrderCommonUtil.getRetailPrice(group.getSettlementAdultPrice(), group.getQuauqAdultPrice(), rate, Long.parseLong(currencyArr[0]));
		BigDecimal companyChildPrice = OrderCommonUtil.getRetailPrice(group.getSettlementcChildPrice(), group.getQuauqChildPrice(), rate, Long.parseLong(currencyArr[1]));
		BigDecimal companySpecialPrice = OrderCommonUtil.getRetailPrice(group.getSettlementSpecialPrice(), group.getQuauqSpecialPrice(), rate, Long.parseLong(currencyArr[2]));
		
		String salerId = request.getParameter("salerId");
		String remark = request.getParameter("remark");
		
		BigDecimal adultPersonNum = (StringUtils.isBlank(orderPersonNumAdult) ? new BigDecimal(0) : new BigDecimal(orderPersonNumAdult));
		BigDecimal childPersonNum = (StringUtils.isBlank(orderPersonNumChild) ? new BigDecimal(0) : new BigDecimal(orderPersonNumChild));
		BigDecimal specialPersonNum = (StringUtils.isBlank(orderPersonNumSpecial) ? new BigDecimal(0) : new BigDecimal(orderPersonNumSpecial));
		
		String moneyAmountUuid = UuidUtils.generUuid();
		String companyMontyAmountUuid = UuidUtils.generUuid();
		String differenceMoney = UuidUtils.generUuid();
		
		// 单价
		T1MoneyAmount adultAmount = null;
		T1MoneyAmount allAdultAmount = null;
		T1MoneyAmount differenceAdultAmount = null;
		String adultPriceUuid = UuidUtils.generUuid();
		if (StringUtils.isNotBlank(adultPrice)) {
			adultAmount = new T1MoneyAmount(adultPriceUuid, Integer.parseInt(currencyArr[0]), new BigDecimal(adultPrice), userId);
			allAdultAmount = new T1MoneyAmount(moneyAmountUuid, 
					Integer.parseInt(currencyArr[0]), new BigDecimal(adultPrice).multiply(adultPersonNum), userId);
			if (null == companyAdultPrice) {
				differenceAdultAmount = new T1MoneyAmount(differenceMoney, Integer.parseInt(currencyArr[0]), 
						new BigDecimal(adultPrice).multiply(adultPersonNum), userId);
			} else {
				differenceAdultAmount = new T1MoneyAmount(differenceMoney, Integer.parseInt(currencyArr[0]), 
						new BigDecimal(adultPrice).subtract(companyAdultPrice).multiply(adultPersonNum), userId);
			}
			
		}
		String childPriceUuid = UuidUtils.generUuid();
		T1MoneyAmount childAmount = null;
		T1MoneyAmount allChildAmount = null;
		T1MoneyAmount differenceChildAmount = null;
		if (StringUtils.isNotBlank(childPrice)) {
			childAmount = new T1MoneyAmount(childPriceUuid, Integer.parseInt(currencyArr[1]), new BigDecimal(childPrice), userId);
			allChildAmount = new T1MoneyAmount(moneyAmountUuid, 
					Integer.parseInt(currencyArr[1]), new BigDecimal(childPrice).multiply(childPersonNum), userId);
			if (null == companyChildPrice) {
				differenceChildAmount = new T1MoneyAmount(differenceMoney, Integer.parseInt(currencyArr[1]), 
						new BigDecimal(childPrice).multiply(childPersonNum), userId);
			} else {
				differenceChildAmount = new T1MoneyAmount(differenceMoney, Integer.parseInt(currencyArr[1]), 
						new BigDecimal(childPrice).subtract(companyChildPrice).multiply(childPersonNum), userId);
			}
		}
		String specialPriceUuid = UuidUtils.generUuid();
		T1MoneyAmount specialPriceAmount = null;
		T1MoneyAmount allSpecialPriceAmount = null;
		T1MoneyAmount differenceSpecialAmount = null;
		if (StringUtils.isNotBlank(specialPrice)) {
			specialPriceAmount = new T1MoneyAmount(specialPriceUuid, 
					Integer.parseInt(currencyArr[2]), new BigDecimal(specialPrice), userId);
			allSpecialPriceAmount = new T1MoneyAmount(moneyAmountUuid, 
					Integer.parseInt(currencyArr[2]), new BigDecimal(specialPrice).multiply(specialPersonNum), userId);
			if (null == companySpecialPrice) {
				differenceSpecialAmount = new T1MoneyAmount(differenceMoney, Integer.parseInt(currencyArr[0]), 
						new BigDecimal(specialPrice).multiply(specialPersonNum), userId);
			} else {
				differenceSpecialAmount = new T1MoneyAmount(differenceMoney, Integer.parseInt(currencyArr[0]), 
						new BigDecimal(specialPrice).subtract(companySpecialPrice).multiply(specialPersonNum), userId);
			}
		}
		
		// 系统单价
		String companyAdultPriceUuid = UuidUtils.generUuid();
		T1MoneyAmount companyAdultAmount = null;
		T1MoneyAmount allCompanyAdultAmount = null;
		if (null != companyAdultPrice) {
			companyAdultAmount = new T1MoneyAmount(companyAdultPriceUuid, 
					Integer.parseInt(currencyArr[0]), companyAdultPrice, userId);
			allCompanyAdultAmount = new T1MoneyAmount(companyMontyAmountUuid, 
					Integer.parseInt(currencyArr[0]), companyAdultPrice.multiply(adultPersonNum), userId);
		}
		String companyChildPriceUuid = UuidUtils.generUuid();
		T1MoneyAmount companyChildAmount = null;
		T1MoneyAmount allCompanyChildAmount = null;
		if (null != companyChildPrice) {
			companyChildAmount = new T1MoneyAmount(companyChildPriceUuid, 
					Integer.parseInt(currencyArr[1]), companyChildPrice, userId);
			allCompanyChildAmount = new T1MoneyAmount(companyMontyAmountUuid, 
					Integer.parseInt(currencyArr[1]), companyChildPrice.multiply(childPersonNum), userId);
		}
		String companySpecialPriceUuid = UuidUtils.generUuid();
		T1MoneyAmount companySpecialPriceAmount = null;
		T1MoneyAmount allCompanySpecialPriceAmount = null;
		if (null != companySpecialPrice) {
			companySpecialPriceAmount = new T1MoneyAmount(companySpecialPriceUuid, 
					Integer.parseInt(currencyArr[2]), companySpecialPrice, userId);
			allCompanySpecialPriceAmount = new T1MoneyAmount(companyMontyAmountUuid, 
					Integer.parseInt(currencyArr[2]), companySpecialPrice.multiply(specialPersonNum), userId);
		}
		
		T1PreOrder preOrder = new T1PreOrder();
		preOrder.setOrderNum(getOrderNum());
		preOrder.setOrderTime(new Date());
		preOrder.setProductId(group.getTravelActivity().getId());
		preOrder.setProductGroupId(group.getId());
		preOrder.setAgentId(UserUtils.getUser().getAgentId());
		preOrder.setOrderPersonNum(Integer.parseInt(orderPersonNum));
		preOrder.setOrderPersonNumAdult(Integer.parseInt(orderPersonNumAdult));
		preOrder.setOrderPersonNumChild(Integer.parseInt(orderPersonNumChild));
		preOrder.setOrderPersonNumSpecial(Integer.parseInt(orderPersonNumSpecial));
		preOrder.setAdultPrice(adultPriceUuid);
		preOrder.setChildPrice(childPriceUuid);
		preOrder.setSpecialPrice(specialPriceUuid);
		preOrder.setCompanyAdultPrice(companyAdultPriceUuid);
		preOrder.setCompanyChildPrice(companyChildPriceUuid);
		preOrder.setCompanySpecialPrice(companySpecialPriceUuid);
		preOrder.setTotalMoney(moneyAmountUuid);
		preOrder.setCompanyMoney(companyMontyAmountUuid);
		preOrder.setDifferenceMoney(differenceMoney);
		preOrder.setQuauqProductChargeType(rate.getQuauqRateType());
		preOrder.setQuauqProductChargeRate(rate.getQuauqRate());
		preOrder.setQuauqOtherChargeType(rate.getQuauqOtherRateType());
		preOrder.setQuauqOtherChargeRate(rate.getQuauqOtherRate());
		preOrder.setPartnerProductChargeType(rate.getAgentRateType());
		preOrder.setPartnerProductChargeRate(rate.getAgentRate());
		preOrder.setPartnerOtherChargeType(rate.getAgentOtherRateType());
		preOrder.setPartnerOtherChargeRate(rate.getAgentOtherRate());
		preOrder.setCutChargeType(rate.getChouchengRateType());
		preOrder.setCutChargeRate(rate.getChouchengRate());
		preOrder.setOrderStatus(1);
		preOrder.setSalerId(Long.parseLong(salerId));
		preOrder.setPayId(StringUtils.isNotBlank(payId) ? Long.parseLong(payId) : null);
		preOrder.setRemark(remark);
		preOrder.setDelFlag("0");
		
		t1PreOrderDao.save(preOrder);
		moneyAmountService.save(adultAmount);
		moneyAmountService.save(childAmount);
		moneyAmountService.save(specialPriceAmount);
		moneyAmountService.save(companyAdultAmount);
		moneyAmountService.save(companyChildAmount);
		moneyAmountService.save(companySpecialPriceAmount);
		
		List<T1MoneyAmount> moneyAmountList = Lists.newArrayList();
		moneyAmountList.add(allAdultAmount);
		moneyAmountList.add(allChildAmount);
		moneyAmountList.add(allSpecialPriceAmount);
		moneyAmountService.saveOrUpdateMoneyAmounts(moneyAmountService.mergeMoneyAmount(moneyAmountList));
		
		List<T1MoneyAmount> companyMoneyAmountList = Lists.newArrayList();
		companyMoneyAmountList.add(allCompanyAdultAmount);
		companyMoneyAmountList.add(allCompanyChildAmount);
		companyMoneyAmountList.add(allCompanySpecialPriceAmount);
		moneyAmountService.saveOrUpdateMoneyAmounts(moneyAmountService.mergeMoneyAmount(companyMoneyAmountList));
		
		List<T1MoneyAmount> differenceMoneyAmountList = Lists.newArrayList();
		differenceMoneyAmountList.add(differenceAdultAmount);
		differenceMoneyAmountList.add(differenceChildAmount);
		differenceMoneyAmountList.add(differenceSpecialAmount);
		moneyAmountService.saveOrUpdateMoneyAmounts(moneyAmountService.mergeMoneyAmount(differenceMoneyAmountList));
		
		results.put("result", "success");
		results.put("preOrderId", preOrder.getId());
		results.put("groupId", preOrder.getProductGroupId());
		
		return results;
	}
	
	/**
	 * 获取订单号
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-17
	 */
	private String getOrderNum() {
		
		// 当前时间
		Date createDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// 询单编号（时间戳）
		String dateNum = sdf.format(createDate);
		
		String nextNum = null;
		String nowOrderNum = t1PreOrderDao.findLastPreOrder();
		if (StringUtils.isBlank(nowOrderNum)) {
			nextNum = "0001";
		} else {
			String lastOrderNum = nowOrderNum;
			Integer currentNum = Integer.parseInt(lastOrderNum.substring(lastOrderNum.length()-4, lastOrderNum.length())) + 1;
			Integer length = currentNum.toString().length();
			if (length == 1) {
				nextNum = "000" + currentNum;
			} else if (length == 2) {
				nextNum = "00" + currentNum;
			} else if (length == 3) {
				nextNum = "0" + currentNum;
			} else {
				nextNum = "0" + currentNum;
			}
		}
		String orderNum = "TJ" + dateNum + nextNum;
		return orderNum;
	}
	
	/**
	 * 取消订单
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-18
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Object cancleOrder(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject object = new JSONObject();
		if (StringUtils.isNotBlank(orderId)) {
			T1PreOrder order = t1PreOrderDao.findOne(Long.parseLong(orderId));
			order.setOrderStatus(3);
			t1PreOrderDao.save(order);
			object.put("result", "success");
			object.put("msg", "");
			
			// 修改订单跟踪状态
			OrderProgressTracking tracking = orderProgressTrackingService.findByPreOrderId(order.getId());
			if (null != tracking) {
				tracking.setUpdateDate(new Date());
				tracking.setUpdateById(UserUtils.getUser().getId());
				tracking.setOrderStatus(4);
				orderProgressTrackingService.save(tracking);
			}
		} else {
			object.put("result", "false");
			object.put("msg", "订单ID不能为空");
		}
		return object;
	}
	
	/**
	 * 预定订单价格校验
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-12-22
	 */
	public Object validatePrice(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject object = new JSONObject();
		if (StringUtils.isNotBlank(orderId)) {
			T1PreOrder order = t1PreOrderDao.findOne(Long.parseLong(orderId));
			object.put("productId", order.getProductId());
			object.put("productGroupId", order.getProductGroupId());
			User user = order.getCreateBy();
			object.put("agentId", user.getAgentId());
			object.put("preOrderId", order.getId());
			
			// 获取现团期汇率对象
			Rate rate = RateUtils.getRate(order.getProductGroupId(), 2, order.getAgentId());
			ActivityGroup group = groupDao.findOne(order.getProductGroupId());
			String[] currencyArr = group.getCurrencyType().split(",");
			
			// 获取现团期成人、儿童、特殊人群价格币种
			Currency adultCurrency = currencyDao.findById(Long.parseLong(currencyArr[0]));
			Currency childCurrency = currencyDao.findById(Long.parseLong(currencyArr[1]));
			Currency specialCurrency = currencyDao.findById(Long.parseLong(currencyArr[2]));
			
			// 成人价：现团期零售价与预报名零售价对比
			boolean flag = true;
			BigDecimal companyAdultPrice = OrderCommonUtil.getRetailPrice(group.getSettlementAdultPrice(), group.getQuauqAdultPrice(), rate, adultCurrency.getId());
			List<T1MoneyAmount> companyAdultPriceList = moneyAmountService.findBySerialNum(order.getCompanyAdultPrice());
			if (null != companyAdultPrice && CollectionUtils.isNotEmpty(companyAdultPriceList)) {
				int result = companyAdultPrice.compareTo(companyAdultPriceList.get(0).getAmount());
				if (result != 0) {
					flag = false;
				}
			}
			
			// 儿童价：现团期零售价与预报名零售价对比
			if (flag) {
				BigDecimal companyChildPrice = OrderCommonUtil.getRetailPrice(group.getSettlementcChildPrice(), group.getQuauqChildPrice(), rate, childCurrency.getId());
				List<T1MoneyAmount> companyChildPriceList = moneyAmountService.findBySerialNum(order.getCompanyChildPrice());
				if (null != companyChildPrice && CollectionUtils.isNotEmpty(companyChildPriceList)) {
					int result = companyChildPrice.compareTo(companyChildPriceList.get(0).getAmount());
					if (result != 0) {
						flag = false;
					}
				}
			}
			
			// 特殊人群价：现团期零售价与预报名零售价对比
			if (flag) {
				BigDecimal companySpecialPrice = OrderCommonUtil.getRetailPrice(group.getSettlementSpecialPrice(), group.getQuauqSpecialPrice(), rate, specialCurrency.getId());
				List<T1MoneyAmount> companySpecialPriceList = moneyAmountService.findBySerialNum(order.getCompanySpecialPrice());
				if (null != companySpecialPrice && CollectionUtils.isNotEmpty(companySpecialPriceList)) {
					int result = companySpecialPrice.compareTo(companySpecialPriceList.get(0).getAmount());
					if (result != 0) {
						flag = false;
					}
				}
			}
			
			if (flag) {
				object.put("result", "success");
				object.put("msg", "");
			} else {
				object.put("result", "false");
				object.put("msg", "产品价格有变动");
			}
		} else {
			object.put("result", "false");
			object.put("msg", "订单ID不能为空");
		}
		return object;
	}
	
	/**
	 * 预报名校验
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-18
	 */
	public Object validateBookOrder(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject object = new JSONObject();
		if (StringUtils.isNotBlank(orderId)) {
			T1PreOrder order = t1PreOrderDao.findOne(Long.parseLong(orderId));
			object.put("productId", order.getProductId());
			object.put("productGroupId", order.getProductGroupId());
			User user = order.getCreateBy();
			object.put("agentId", user.getAgentId());
			object.put("preOrderId", order.getId());
			
//			ActivityGroup group = groupDao.findOne(order.getProductGroupId());
//			if (order.getOrderPersonNum().intValue() < group.getFreePosition()) {
//				object.put("result", "error");
//				object.put("msg", "余位不足");
//			} else {
				object.put("result", "success");
				object.put("msg", "");
//			}
		} else {
			object.put("result", "false");
			object.put("msg", "订单ID不能为空");
		}
		return object;
	}
	
	/**
	 * 删除订单
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-18
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Object deleteOrder(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject object = new JSONObject();
		if (StringUtils.isNotBlank(orderId)) {
			T1PreOrder order = t1PreOrderDao.findOne(Long.parseLong(orderId));
			order.setOrderStatus(4);
			order.setDelFlag("1");
			t1PreOrderDao.save(order);
			object.put("result", "success");
			object.put("msg", "");
			
			// 修改订单跟踪状态
			OrderProgressTracking tracking = orderProgressTrackingService.findByPreOrderId(order.getId());
			if (null != tracking) {
				orderProgressTrackingService.delete(tracking);
			}
		} else {
			object.put("result", "false");
			object.put("msg", "订单ID不能为空");
		}
		return object;
	}
	
	/**
	 * 修改订单是否已查看状态
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-25
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Object changeNotSeenOrderFlag(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject object = new JSONObject();
		if (StringUtils.isNotBlank(orderId)) {
			T1PreOrder order = t1PreOrderDao.findOne(Long.parseLong(orderId));
			order.setHasSeen(1);
			t1PreOrderDao.save(order);
			object.put("result", "success");
			object.put("msg", "");
		} else {
			object.put("result", "false");
			object.put("msg", "订单ID不能为空");
		}
		return object;
	}
	
	/**
	 * 获取渠道名称
	 * @param request
	 * @param response
	 * @return
	 * @author yakun.bai
	 * @Date 2016-10-25
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Object getAgentName(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		JSONObject object = new JSONObject();
		if (StringUtils.isNotBlank(orderId)) {
			T1PreOrder order = t1PreOrderDao.findOne(Long.parseLong(orderId));
			Agentinfo agent = AgentInfoUtils.getAgentById(order.getAgentId());
			object.put("agentName", agent.getAgentName());
			object.put("result", "success");
			object.put("msg", "");
		} else {
			object.put("result", "false");
			object.put("msg", "订单ID不能为空");
		}
		return object;
	}
}