package com.trekiz.admin.modules.visa.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.service.OrderServiceForSaveAndModify;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.order.service.OrderStockService;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.service.StockService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;

@Controller
@RequestMapping(value = "${adminPath}/order/manager/visa/joinGroup")
public class JoinGroupContoller extends BaseController {

	@Autowired
    private OrderServiceForSaveAndModify orderServiceForSaveAndModify;
	
	@Autowired
	private OrderStockService orderStockService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private TravelActivityService travelActivityService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private SysIncreaseService sysIncreaseService;
	
	@Autowired
	private StockService stockService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	
	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private AgentinfoService agentInfoService;

	@Autowired
	private ActivityGroupDao activityGroupDao;
	
	@Autowired
	private OrderContactsDao orderContactsDao;
	
	
	@RequestMapping(value = {"joinGroupForm",""})
	public String joinGroupForm(@RequestParam(value = "orderId", required = true) String orderId,@RequestParam(value="travelerId",required=false) String travelerId, Model model) {
		
		//获取订单信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(orderId));
		//获取产品信息
		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Currency currency = currencyService.findCurrency(visaOrder.getProOriginCurrencyId().longValue());
		model.addAttribute("currency", currency);
		
		//获取游客签证信息
		List<Object[]> travelerInfoList = null;
		/*if(null != travelerId && !"".equals(travelerId)){
			travelerInfoList = visaService.findJoinGroupTravelerVisaForOne(orderId,StringUtils.toLong(travelerId));//根据travelerId取游客
		}else{
			travelerInfoList = visaService.findJoinGroupTravelerVisa(orderId);
		}*/
		
		//如果是单独点击的游客申请参团
		if(StringUtils.isNotBlank(travelerId)) {
			travelerInfoList = visaService.findJoinGroupByTravelerId(travelerId);
		}else{
			travelerInfoList = visaService.findJoinGroupTravelerVisa(orderId);
		}
		
		for(Object[] o : travelerInfoList) {
			if(null != o[4]) {
				o[4] = visaOrderService.getMoney(o[4].toString(), "true");
			}
		}
		
		
		//List<Agentinfo> agentList = agentinfoService.findAllAgentinfo();
		//------wxw 2015-08-26 added 取该订单销售的跟踪渠道------
		List<Agentinfo> agentList = agentInfoService.findAllAgentinfoBySalerId(visaOrder.getSalerId());
		model.addAttribute("agentInfoList", agentList);
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("visaProducts", visaProducts);
		model.addAttribute("travelerInfoList", travelerInfoList);
		model.addAttribute("loginuserDeptid", UserUtils.getUser().getCompany().getId());
		model.addAttribute("visaCostPriceFlag", UserUtils.getUser().getCompany().getVisaCostPrice());
		//model.addAttribute("agentInfoList", agentInfoService.findAllAgentinfo());
		return "modules/visa/joinGroupForm";
	}
	
	@Transactional
	@ResponseBody
	@RequestMapping(value = "joinGroupApply")
	public Object joinGroup(@RequestParam(value = "visaOrderId", required = true) String visaOrderId, 
			String[] travelerIdArr, HttpServletRequest request, Model model) {
		

		String groupId = request.getParameter("groupId");
		//操作结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//如果用户选择的团期没有填写价格或者价格为0时，则不允许参团
		ActivityGroup activityGroup = activityGroupService.findById(StringUtils.toLong(groupId));
		if(null == activityGroup || null == activityGroup.getSettlementAdultPrice()
			 || (null != activityGroup.getSettlementAdultPrice() && 0 == activityGroup.getSettlementAdultPrice().compareTo(new BigDecimal(0)))) {
			resultMap.put("errorMsg", "该团期价格无效，请先确认！");
			return resultMap;
		}
		
		//生成一个新的产品订单
		//必须参数：团期id、付款方式、预定渠道、人数、订单号、(成人、儿童、特殊人群)游客数量
		if(StringUtils.isNotBlank(groupId)) {
			//不允许参已过期的团期
			//获取当前日期年份和月份
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
			ActivityGroup group = activityGroupService.findById(StringUtils.toLong(groupId));
			if(null != group) {
				Calendar groupCal = Calendar.getInstance();
				groupCal.setTime(group.getGroupOpenDate());
				if(1 == cal.compareTo(groupCal)) {
					resultMap.put("errorMsg", "该团期已过期，无法进行参团操作！");
					return resultMap;
				}
			}
		}
		//当前请求的结果集
		if(StringUtils.isNotBlank(groupId)) {
			//不允许参已过期的团期
			//获取当前日期年份和月份
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
			ActivityGroup group = activityGroupService.findById(StringUtils.toLong(groupId));
			if(null != group) {
				Calendar groupCal = Calendar.getInstance();
				groupCal.setTime(group.getGroupOpenDate());
				if(1 == cal.compareTo(groupCal)) {
					resultMap.put("errorMsg", "该团期已过期，无法进行参团操作！");
					return resultMap;
				}
			}
		}
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(visaOrderId));
		String agentId = request.getParameter("agentId");
		String payMode = request.getParameter("payMode");
		String reserve = request.getParameter("reserve");
		String companyName = UserUtils.getUser().getCompany().getName();
		String orderNum = sysIncreaseService.updateSysIncrease(companyName.length() > 3 ? companyName.substring(0, 3) : companyName, 
				UserUtils.getUser().getCompany().getId(),null, Context.ORDER_NUM_TYPE);

		
		//根据每个游客类型personType，统计各类(成人、儿童、特殊人群)游客数量
		int adultNum = 0;
		int childNum = 0;
		int specialNum = 0;
//		List<Traveler> travelerList = travelerService.findTravelerByOrderIdAndOrderType(StringUtils.toLong(visaOrderId), Context.ORDER_TYPE_QZ);
		//获取用户选中的游客参团
		List<Traveler> travelerList = travelerService.findByIds(travelerIdArr);
		StringBuffer travelerIds = new StringBuffer();
		//统计三种游客数
		for(Traveler t : travelerList) {
			Integer person = t.getPersonType();
			//默认是成人
			if(null != person) {
				switch (person) {
					//成人
					case 1:
						adultNum++;
						break;
					//儿童
					case 2:
						childNum++;
						break;
					//特殊人群
					case 3:
						specialNum++;
						break;
				}
			}else{
				t.setPersonType(1);
				adultNum++;
			}
			travelerIds.append(t.getId() + ",");
		}
		//创建新订单
		ProductOrderCommon productOrder = new ProductOrderCommon();
		//填充生成订单所必要的信息
		productOrder.setProductGroupId(StringUtils.toLong(groupId));
		//订单种类,默认是单团类型
		TravelActivity activity = travelActivityService.findById(activityGroup.getSrcActivityId().longValue());
		productOrder.setPayMode(payMode);

		// 支付状态：1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消
		// 支付方式：1定金占位，2预占位，3全款支付，4资料占位，5担保占位，6确认单占位
		if ("1".equals(payMode)) {
			productOrder.setPayStatus(1);
		} else if ("2".equals(payMode)) {
			productOrder.setPayStatus(3);
		} else if ("3".equals(payMode)) {
			productOrder.setPayStatus(1);
		} else if ("4".equals(payMode)) {
			productOrder.setPayStatus(3);
		} else if ("5".equals(payMode)) {
			productOrder.setPayStatus(3);
		} else if ("6".equals(payMode)) {
			productOrder.setPayStatus(3);
		} else if ("7".equals(payMode)) {
			productOrder.setPayStatus(3);
		} else if ("8".equals(payMode)) {
			productOrder.setPayStatus(8);
		} else {
			productOrder.setPayStatus(99);
		}
		
		productOrder.setOrderNum(orderNum);
		productOrder.setOrderPersonNumAdult(adultNum);
		productOrder.setOrderPersonNumChild(childNum);
		productOrder.setOrderPersonNumSpecial(specialNum);
		Integer orderPersonNum = adultNum + childNum + specialNum;
		productOrder.setPlaceHolderType(StringUtils.toInteger(reserve));
		productOrder.setSalerId(visaOrder.getSalerId());
		productOrder.setSalerName(visaOrder.getSalerName());
		
		if(null != activity) {
			productOrder.setProductId(activity.getId());
			productOrder.setOrderStatus(activity.getActivityKind());
		}else{
			productOrder.setOrderStatus(StringUtils.toInteger(Context.PRODUCT_TYPE_DAN_TUAN));
		}
		
		try {
			productOrder.setOrderTime(new Date());
			productOrder.setOrderPersonNum(orderPersonNum);
			resultMap = orderServiceForSaveAndModify.saveOrder(productOrder, null, request);
			orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.JOIN_GROUP);
		} catch (OptimisticLockHandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PositionOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//如果余位不足
		if(null != resultMap.get("errorMsg")) {
			return resultMap;
		}else{
			Map<String, String> returnMap = new HashMap<String, String>();
			try {
				returnMap = orderStatisticsService.copyTravelersInfoForVisaJoinGroup(productOrder, travelerList);
				if("0".equals(returnMap.get(Context.RESULT))) {
					resultMap.put("errorMsg", returnMap.get(Context.MESSAGE));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(agentId)) {
				Agentinfo agentinfo = agentInfoService.findAgentInfoById(StringUtils.toLong(agentId));
				List<User> salers = UserUtils.getUserListByIds(agentinfo.getAgentSalerId());
				agentinfo.setAgentSalerUser(salers);
				OrderContacts contact = new OrderContacts();
				contact.setOrderId(productOrder.getId());
				contact.setContactsName(agentinfo.getAgentContact());
				contact.setContactsTel(agentinfo.getAgentContactMobile());
				contact.setOrderType(activity.getActivityKind());
				orderContactsDao.save(contact);
				
				productOrder.setOrderCompany(agentinfo.getId());
				productOrder.setOrderCompanyName(agentinfo.getAgentName());
				User user = UserUtils.getUser();
				// TODO 由于渠道销售业务及表字段发生变化，且此处签证参团是未指定销售，故默认获取第一销售
				productOrder.setOrderSaler(agentinfo.getAgentSalerUser().get(0) != null ? agentinfo.getAgentSalerUser().get(0) : user);
			}
			Long orderId = productOrder.getId();
			resultMap.put("orderId", orderId);
			resultMap.put("travelerIds", travelerIds);
			return resultMap;
		}
		
	}
	
	@ResponseBody
	@RequestMapping("getGroupInfo")
	public Map<String, String> getGroupInfo(@RequestParam(value = "groupCode", required = true) String groupCode, 
			@RequestParam(value = "orderAgentId", required = true) String orderAgentId) {
		
		Map<String, String> infoMap = null;
		ActivityGroup group = activityGroupService.findByGroupCode(groupCode);
		
		if(group != null) {
			infoMap = new HashMap<String, String>();
		
			Long groupId = group.getId();
			TravelActivity activity = group.getTravelActivity();
			
			//团期id
			infoMap.put("groupId", groupId.toString());
			Map<String, String> orderTypeMap = DictUtils.getSysDicMap("order_type");
			//团队类型
			infoMap.put("activityKind", orderTypeMap.get(activity.getActivityKind().toString()));
			//团号
			infoMap.put("groupCode", group.getGroupCode());
			//余位
			infoMap.put("freePosition", group.getFreePosition() == null ? "" : group.getFreePosition().toString());
			//参团计调

			User createBy = group.getCreateBy();
			if(null != createBy) {
				infoMap.put("createBy", group.getCreateBy().getName());
			}else{
				infoMap.put("createBy", "-");
			}

			//产品名称
			infoMap.put("activityName", activity.getAcitivityName());
			//产品占位类型
			infoMap.put("payMode", activity.getPayMode());
			//团期切位情况
			ActivityGroupReserve activityGroupReserve = stockService.findByActivityGroupIdAndAgentId(groupId.toString(), orderAgentId);
			if(activityGroupReserve != null) {
				infoMap.put("reserve", activityGroupReserve.getPayReservePosition().toString());
			}
			//遍历Map，处理为空的数据
			Iterator<String> iterator = infoMap.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				if(null == infoMap.get(key)) {
					infoMap.put(key, "");
				}
			}
		}
		return infoMap;
	}

	// 根据条件查询团号列表
	@ResponseBody
	@RequestMapping("findGroupListByCondition")
	public List<Map<String, String>> findGroupListByCondition(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "groupOpenDate", required = true) String groupOpenDate, String groupCode, String orderType) {

		DetachedCriteria dc = activityGroupDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));
		// 出团日期
		if(StringUtils.isNotBlank(groupOpenDate)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				Date date = sdf.parse(groupOpenDate);
				dc.add(Restrictions.eq("groupOpenDate", date));
			} catch (ParseException e) {
				e.printStackTrace();
//				System.out.println(e.getMessage());
			}
		}

		// 团号
		if(StringUtils.isNotBlank(groupCode)) {
			dc.add(Restrictions.eq("groupCode", groupCode));
		}

		// 产品类型
		if(StringUtils.isNotBlank(orderType)) {
			dc.add(Restrictions.sqlRestriction("{alias}.srcActivityId in (select id from travelactivity where activity_kind = " + orderType + ")"));
		}

		//区分批发商
		dc.add(Restrictions.sqlRestriction("{alias}.srcActivityId in (select id from travelactivity where proCompany = " + UserUtils.getUser().getCompany().getId() + " and delFlag = 0)" ));
		// 根据查询条件查询列表
		List<Map<String, String>> groupInfoList = new ArrayList<Map<String, String>>();
		List<ActivityGroup> groupList = new ArrayList<ActivityGroup>();
		groupList = activityGroupDao.find(new Page<ActivityGroup>(request, response), dc).getList();
		for(ActivityGroup activityGroup:groupList){
			Map<String, String> groupInfoMap = new HashMap<String, String>();
			// 团号
			groupInfoMap.put("groupCode", activityGroup.getGroupCode());
			// 格式化日期
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
			String dateString = formatter.format(activityGroup.getGroupOpenDate()); 
			groupInfoMap.put("groupOpenDate", dateString);
			// 获取订单类型标签
			Dict type = DictUtils.getDict(activityGroup.getTravelActivity().getActivityKind().toString(), "order_type");
			groupInfoMap.put("type", type.getLabel());
			// 计调(避免垃圾数据引发的错误)
			User createBy = activityGroup.getCreateBy();
			if(null != createBy) {
				groupInfoMap.put("createBy", activityGroup.getCreateBy().getName());
			}else{
				groupInfoMap.put("createBy", "-");
			}
			// 余位
			groupInfoMap.put("freePosition", activityGroup.getFreePosition().toString());
			groupInfoList.add(groupInfoMap);
		}
		return groupInfoList;
	}
}
