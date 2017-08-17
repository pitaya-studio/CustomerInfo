package com.trekiz.admin.modules.order.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.agentToOffice.personnalInfo.service.PersonInfoService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderProgressTracking;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.service.OrderProgressTrackingService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
 
/**
 * 订单跟踪设置表
 * @author yakun.bai
 * @Date 2016-8-12
 */
@Controller
@RequestMapping(value = "${adminPath}/orderProgressTracking/manage")
public class OrderProgressTrackingController extends BaseController {
    
    /** 订单跟踪设置列表地址 */
	private static final String LIST_PAGE = "/modules/order/orderProgressTracking/orderProgressTrackingList";
	
	@Autowired
	private OrderProgressTrackingService orderProgressTrackingService;
	
	@Autowired
	@Qualifier("activityGroupSyncService")
	private IActivityGroupService activityGroupService;
	
	@Autowired
	private OrderPayService orderPayService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private ProductOrderCommonDao productOrderDao;
	
	@Autowired
	private PersonInfoService personInfoService;
   
   
	/**
	 * 订单跟踪设置列表
	 * @author yakun.bai
	 * @Date 2016-8-12
	 */
	@RequestMapping(value ="list")
	public String list() {
		return LIST_PAGE;
	}
	
    /**
     * 查询订单统计数：今日新增、所有订单、超时订单
     * @return
     * @author yakun.bai
     * @Date 2016-12-1
     */
	@ResponseBody
	@RequestMapping(value ="loadOrderCountNum")
	public String loadOrderCountNum(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return orderProgressTrackingService.loadOrderCountNum();
	}
	
	/**
     * 查询各个渠道订单数
     * @return
     * @author yakun.bai
     * @Date 2016-12-1
     */
	@ResponseBody
	@RequestMapping(value ="loadAgentCountNum")
	public Object loadAgentCountNum(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return orderProgressTrackingService.loadAgentCountNum(request);
	}
	
	/**
     * 查询各个渠道订单数
     * @return
     * @author yakun.bai
     * @Date 2016-12-1
     */
	@ResponseBody
	@RequestMapping(value ="loadProgressByAgentIds")
	public Object loadProgressByAgentIds(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return orderProgressTrackingService.loadProgressByAgentIds(request);
	}
	
	/**
	 * 查询各个渠道订单数
	 * @return
	 * @author yakun.bai
	 * @Date 2016-12-1
	 */
	@ResponseBody
	@RequestMapping(value ="getOrderLinkPerson")
	public Object getOrderLinkPerson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return orderProgressTrackingService.getOrderLinkPerson(request);
	}
	
	/**
	 * 订单跟踪设置列表:ajax获取
	 * @author yakun.bai
	 * @Date 2016-8-12
	 */
	@ResponseBody
	@RequestMapping(value ="ajaxList")
	public String ajaxList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long oneTime = System.currentTimeMillis();
		
		String selectType = request.getParameter("selectType");
		
		Page<Map<Object, Object>> page = orderProgressTrackingService.find(new Page<Map<Object, Object>>(request, response), selectType);
		
		long twoTime = System.currentTimeMillis();
		
		List<Map<Object, Object>> list = page.getList();
		
		for (Map<Object, Object> listin : list) {
			if (listin.get("orderId") != null) {
				Long orderId = Long.valueOf(listin.get("orderId").toString());
				List<Orderpay> orderPayList = orderPayService.findOrderPayOrderById(orderId, 2);
				// 获取首次支付时间
				if (CollectionUtils.isNotEmpty(orderPayList)) {
					Orderpay firstOrderPay = orderPayList.get(0);
					listin.put("firstOrderPayTime", DateUtils.formatDate(firstOrderPay.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
				}
				// 获取最后支付时间
				ProductOrderCommon productOrder = productOrderDao.findOne(orderId);
				List<Orderpay> DZList = orderPayService.findOrderPayOrderByUpdate(orderId, productOrder.getOrderStatus());
				if (CollectionUtils.isNotEmpty(DZList) && isFullPayed(productOrder)) {
					listin.put("lastOrderPayTime", DateUtils.formatDate(DZList.get(0).getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
					listin.put("lastOrderPayName", DZList.get(0).getUpdateBy().getName());
				}
				listin.put("orderStatus", productOrder.getPayStatus());
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
				StringBuffer contactSb = new StringBuffer("");
				for (SupplyContacts contact : contactList) {
					contactSb.append(contact.getContactName() + " " + contact.getContactMobile() + "+");
				}
				if (contactSb.length() > 0) {
					listin.put("contactList", contactSb.deleteCharAt(contactSb.length()-1).toString());
				}
			}
			
			listin.put("isLastPage", page.getPageNo() * page.getPageSize() >= page.getCount() ? "1" : "0");
		}
		
		long threeTime = System.currentTimeMillis();
		
		JSONArray josnList = JSONArray.fromObject(list);
		
		System.out.println("用户查询时间：" + (twoTime - oneTime) + "ms");
        System.out.println("渠道查询时间：" + (threeTime - twoTime) + "ms");
		
		return josnList.toString();
	}	
	
	private boolean isFullPayed(ProductOrderCommon productOrder) {
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
	
	@ResponseBody
	@RequestMapping(value ="save")
	public Object save(Model model, HttpServletRequest request) {
		
		// 询单团号
	    String groupId = request.getParameter("groupId");
	    // 订单类型： 1 询批发商；2 预报名订单
	    String orderType = request.getParameter("orderType");
	    // 预报名订单ID
	    String preOrderId = request.getParameter("preOrderId");
	    //销售ID
	    String salerId = request.getParameter("salerId");
	    
	    if (StringUtils.isBlank(orderType)) {
	    	orderType = "1";
	    }
	    
	    // 验证团号是否为空
	    if (StringUtils.isBlank(groupId)) {
	    	return "error";
	    } else {
	    	// 询单时间
			Date askTime = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			// 询单用户
			Long userId = UserUtils.getUser().getId();
			// 询单渠道
			Long agentId = UserUtils.getUser().getAgentId();
			// 询单编号（时间戳）
			String askNum = sdf.format(askTime);
			// 询单团期实体类
	    	ActivityGroup group = activityGroupService.findById(Long.parseLong(groupId));
	    	OrderProgressTracking tracking = new OrderProgressTracking();
	    	tracking.setAskNum(askNum);
			tracking.setAskTime(askTime);
			tracking.setAskUserId(userId);
			tracking.setAskAgentId(agentId);
			tracking.setCompanyId(group.getTravelActivity().getProCompany());
			tracking.setActivityId(group.getTravelActivity().getId());
			tracking.setGroupId(group.getId());
			tracking.setUpdateById(UserUtils.getUser().getId());
			tracking.setUpdateDate(askTime);
			tracking.setOrderType(Integer.parseInt(orderType));
			tracking.setPreOrderId(null == preOrderId ? null : Long.parseLong(preOrderId));
			tracking.setAskSalerId(salerId == null ? null : Long.parseLong(salerId));//被选择询单销售Id
			tracking.setT1Flag(0);
	    	
	    	// 如果没有此团期询单记录或如果已经存在询单数据但已经生成订单则添加一条询单记录
			/*List<OrderProgressTracking> trackingList = orderProgressTrackingService.findByAskUserIdAndGroupId(userId, group.getId(),
					Integer.parseInt(orderType),Long.parseLong(salerId));*/
			//vesion2：根据产品Id，被询单销售ID和询单时间小于60秒查询询单记录
			if(new Integer(orderType) == 2){
				orderProgressTrackingService.save(tracking);
			}else{
				List<OrderProgressTracking> trackingList = orderProgressTrackingService.findByAskUserIdAndAactivityId(userId, group.getTravelActivity().getId(),
						Integer.parseInt(orderType),Long.parseLong(salerId));
				if (CollectionUtils.isEmpty(trackingList)) {
					// 如果是思锐创途账号添加则直接忽略
					if (UserUtils.getUser().getId() != 2062 && UserUtils.getUser().getId() != 2610 && UserUtils.getUser().getId() != 2614) {
						orderProgressTrackingService.save(tracking);
					}else{
						orderProgressTrackingService.save(tracking);
					}
				}
			}
			/*List<OrderProgressTracking> trackingList = orderProgressTrackingService.findByAskUserIdAndAactivityId(userId, group.getTravelActivity().getId(),
					Integer.parseInt(orderType),Long.parseLong(salerId));
			if (CollectionUtils.isEmpty(trackingList)) {
				// 如果是思锐创途账号添加则直接忽略
				if (UserUtils.getUser().getId() != 2062 && UserUtils.getUser().getId() != 2610 && UserUtils.getUser().getId() != 2614) {
					orderProgressTrackingService.save(tracking);
				}else{
					orderProgressTrackingService.save(tracking);
				}
			}else if(new Integer(orderType) == 2){
				orderProgressTrackingService.save(tracking);
			}*/
			/*if (CollectionUtils.isEmpty(trackingList)) {
				// 如果是思锐创途账号添加则直接忽略
				if (UserUtils.getUser().getId() != 2062 && UserUtils.getUser().getId() != 2610 && UserUtils.getUser().getId() != 2614) {
					orderProgressTrackingService.save(tracking);
				}else{
					orderProgressTrackingService.save(tracking);
				}
			} else {
				// 如果是询批发商则需要更新询单时间；如果是预报名则需重新生成订单
				if (Integer.parseInt(orderType) == 1) {
					OrderProgressTracking orderProgress = trackingList.get(0);
					orderProgress.setAskTime(askTime);
					orderProgress.setUpdateById(UserUtils.getUser().getId());
					orderProgress.setUpdateDate(askTime);
					orderProgressTrackingService.save(orderProgress);
				} else {
					orderProgressTrackingService.save(tracking);
				}
			}*/
	    }
	    return "success";
	}
}