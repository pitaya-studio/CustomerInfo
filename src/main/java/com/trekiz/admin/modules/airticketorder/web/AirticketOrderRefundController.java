package com.trekiz.admin.modules.airticketorder.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/order/refund")
public class AirticketOrderRefundController {

	private static final int REFUND_PRODUCTTYPE_AIRTICKET = 7;

//	private static final int REFUND_TYPE_AIRTICKET = 1;

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private AirlineInfoService airlineInfoService;

	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
    private AreaService areaService;
	
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private AgentinfoDao agentinfoDao;

	@RequestMapping(value = "viewList")
	public String viewRefundRecords(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		//当 ReviewService.getOperTotal() 是 0 时（不是计调），查询 flowType=1 的审核记录；否则查询 flowType=16 的审核记录
		Integer flowType;
		if(71 == UserUtils.getUser().getCompany().getId() && 0  != reviewService.getOperTotal()){
			flowType = 16;
		}else{
			flowType=1;
		}
		AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.valueOf(orderId));
		Long pid = order.getAirticketId();
		ActivityAirTicket p = activityAirTicketDao.findOne(pid);
		List<Map<String, String>> reviewMapList = reviewService
				.findReviewListMap(REFUND_PRODUCTTYPE_AIRTICKET,
						flowType, orderId, false,p.getDeptId());//以前传入的参数为true,后改为false
		List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
		model.addAttribute("viewMap", getShowMap(reviewList));
		model.addAttribute("orderId", orderId);
		return "modules/order/airticket/airticketOrderRefundList";
	}
	
	/**
	 * 查询单个退款信息(退款详情页面)
	 */
	@RequestMapping(value="refundPrice/{orderId}/{idStr}")
	public String querySingleRefundPrice(Model model,HttpServletResponse response,HttpServletRequest request,
		   @PathVariable String orderId,@PathVariable String idStr){
		
		Long id = 0L;
		if(null != idStr && !"".equals(idStr) && idStr.length() > 0){
			id = Long.parseLong(idStr);
		}
		
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		if(id > 0){
			List<Integer> flowtypes = new ArrayList<Integer>();
			flowtypes.add(Context.REVIEW_FLOWTYPE_REFUND);
			flowtypes.add(Context.REVIEW_FLOWTYPE_OPER_REFUND);
			List<Map<String, String>> reviewMapList = reviewService.findReviewListMapSingle(REFUND_PRODUCTTYPE_AIRTICKET,
					flowtypes,orderId,id);
			List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
			model.addAttribute("viewMap", getShowMap(reviewList));

			//=========订单详情开始
			Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
			model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
			//=========订单详情结束
			model.addAttribute("rid", id);
			return "modules/order/airticket/airticketRefundPrices";
		}else{
			return "redirect:" + Global.getAdminPath() + "/order/refund/viewList?orderId=" + orderId;
		}
		
		
		
	}

	/**
	 * 按游客Id转换为map分组
	 * 
	 * @param reviewList
	 * @return
	 */
	private LinkedHashMap<String, List<RefundBean>> getShowMap(
			List<RefundBean> reviewList) {
		LinkedHashMap<String, List<RefundBean>> map = new LinkedHashMap<String, List<RefundBean>>();
		// 根据游客id排序
		Collections.sort(reviewList, new Comparator<RefundBean>() {
			@Override
			public int compare(RefundBean o1, RefundBean o2) {
				if (Integer.valueOf(o1.getCurrencyId()) > Integer.valueOf(o2
						.getCurrencyId())) {
					return 1;
				} else if (Integer.valueOf(o1.getCurrencyId()) < Integer
						.valueOf(o2.getCurrencyId())) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		String key = null;
		List<RefundBean> aList = null;
		for (RefundBean bean : reviewList) {
			key = bean.getTravelerId();
			aList = map.get(key);
			if (aList == null) {
				aList = new ArrayList<RefundBean>();
				map.put(key, aList);
			}
			aList.add(bean);
		}
		return map;
	}

	/**
	 * 获取退款bean列表
	 * 
	 * @param reviewMapList
	 * @return
	 */
	private List<RefundBean> getRefundBeanList(
			List<Map<String, String>> reviewMapList) {
		List<RefundBean> aList = new ArrayList<RefundBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, String> map : reviewMapList) {
			aList.add(new RefundBean(map));
		}
		return aList;
	}

	@RequestMapping(value = "createRefund")
	public String createRefund(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");

		Map<String, Object> orderDetail = airTicketOrderService
				.queryAirticketOrderDetailInfoById(orderId);
		RefundBean reviewObj = null;
		String refundRemark = null;
		if (StringUtils.isNotEmpty(reviewId)) {
			List<Review> review = reviewService.findReview(
					Long.valueOf(reviewId), true);
			refundRemark = review.get(0).getCreateReason();
			reviewObj = new RefundBean(reviewService.findReview(Long
					.valueOf(reviewId)));
		}
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("reviewObj", reviewObj);
		model.addAttribute("refundRemark", refundRemark);
		List<Currency> currencyList = currencyService
				.findCurrencyList(companyId);
		model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("spaceGradelist",
				DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("airspacelist",
				DictUtils.getDictList("airspace_Type"));// 舱位
        
        model.addAttribute("fromAreas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市

		model.addAttribute("airlineList",
				airlineInfoService.getAirlineInfoList(companyId));// 航空公司

		return "modules/order/airticket/airticketOrderRefund";
	}

	@ResponseBody
	@RequestMapping(value = "cancelRefund")
	public Map<String, Object> cancelRefund(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String reviewId = request.getParameter("reviewId");
		reviewService.removeReview(Long.valueOf(reviewId));
		map.put("success", "success");
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "currencyJson")
	public Map<String, Object> getCurrencyJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		List<Currency> currencyList = currencyService
				.findCurrencyList(companyId);
		map.put("currencyList", currencyList);
		return map;
	}

	@SuppressWarnings("unchecked")
    @ResponseBody
	@RequestMapping(value = "submitReview")
	public Map<String, Object> submitReviewflow(HttpServletRequest request,
			HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String createReason = request.getParameter("refundRemark");
		String refundRecordsStr = request.getParameter("refundRecords");
		String reviewId = request.getParameter("reviewId");
		@SuppressWarnings("deprecation")
        List<RefundBean> refundRecords = JSONArray.toList(
				JSONArray.fromObject(refundRecordsStr), RefundBean.class);

		if (StringUtils.isEmpty(reviewId)) {
			reviewId = "0";
		}
		AirticketOrder airOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
		
		Long airticketId = airOrder.getAirticketId();
		if(airticketId == null){
			return null;
		}
		ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(airticketId);
		if(activityAirTicket == null){
			return null;
		}
		Long deptId = activityAirTicket.getDeptId();
		if(deptId == null){
			return null;
		}
		StringBuffer reply = new StringBuffer();
		Long result=0L; //返回的是reviewId
		
		Long agentId = airOrder.getAgentinfoId();
		String agentName = "";
		if (agentId == -1) {
			agentName = airOrder.getNagentName();
		} else {
			agentName = agentinfoDao.findOne(agentId).getAgentName();
		}
		boolean yubao_locked = false; //预报单是否锁定标识
		boolean jiesuan_locked = false; //结算单是否锁定标识
		//对预报单状态进行判断
		if ("10".equals(activityAirTicket.getForcastStatus())) {//!=改为了== by chy 2015年6月8日17:34:52 1为锁 0 标示未锁
			yubao_locked = true;
		}
		//对结算单状态进行判断
		if (1 == activityAirTicket.getLockStatus()) {//00改为了10 by chy 2015年6月8日17:35:24 00标示未锁 10标示锁定
			jiesuan_locked = true;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "结算单已锁定，不能发起审核");
			return map;
		} 
		for (RefundBean bean : refundRecords) {

			//新行者计调 退款流程 是16，其他退款都是1 
			int reveiwFlowType ;
			//当用户的公司编号 UserUtils.getUser().getCompany().getId() 等于 71 时说明是新行者，调用 ReviewService.getOperTotal() 函数，
			//如果返回 0 时（不是计调）
			if(71 == UserUtils.getUser().getCompany().getId() && 0  != reviewService.getOperTotal()){
				reveiwFlowType = 16;
			}else{
				reveiwFlowType=1;
			}
			result = reviewService.addReview(
					Integer.valueOf(REFUND_PRODUCTTYPE_AIRTICKET),
					reveiwFlowType, orderId,
					Long.valueOf(bean.getTravelerId()), Long.valueOf(reviewId),
					createReason, reply, getDetail(bean), deptId);
			if (result != 0) {
				costManageService.saveRefundCostRecord(Context.ORDER_TYPE_JP, bean, airOrder, agentName, result, deptId, yubao_locked, jiesuan_locked);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(result==0L){
			map.put("error", reply.toString());
//		}
//		
//		if (reply.length() != 0) {
//			map.put("error", map.toString());
		} else {
			map.put("success", "success");
		}

		return map;
	}

	private List<Detail> getDetail(RefundBean bean) {
		List<Detail> detailList = new ArrayList<Detail>();

		Map<String, String> map = bean.getReviewDetailMap();
		for (Entry<String, String> entry : map.entrySet()) {
			detailList.add(new Detail(entry.getKey(), entry.getValue()));
		}

		return detailList;
	}
}
