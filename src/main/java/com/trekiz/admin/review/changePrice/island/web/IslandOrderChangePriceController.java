package com.trekiz.admin.review.changePrice.island.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "${adminPath}/islandOrder/changePrice")
public class IslandOrderChangePriceController {
	
//	@Autowired
//	private ReviewService reviewService;
//	
//	@Autowired
//	private com.quauq.review.core.engine.ReviewService processReviewService;
//	
//	@Autowired
//	private IslandOrderService islandOrderService;
//	
//	@Autowired
//	private ActivityIslandService activityIslandService;
	
	/**
	 * 查询海岛游改价记录
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
//	@RequestMapping(value="islandChangePriceRecorderList")
//	public String islandChangePriceList(Model model,HttpServletResponse response,HttpServletRequest request){
//		String orderId = request.getParameter("orderId") ; // 订单编号
//		String productType = request.getParameter("productType"); //订单类型
//		String flowType = request.getParameter("flowType");  // 流程类型
//		String orderUuid = request.getParameter("orderUuid");
//		if(StringUtils.isBlank(flowType)) {
//			flowType = "10";
//		}
//		IslandOrder islandOrder = islandOrderService.getById(Integer.valueOf(orderId));
//		ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
//		Long deptId = Long.parseLong(String.valueOf(activityIsland.getDeptId()));
//		boolean flag = reviewService.verifyWorkFlowStatus(orderId, Integer.parseInt(productType), Integer.parseInt(flowType));
//		model.addAttribute("flag", flag);
//		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
////		List<Map<String, Object>> list = reviewService.findReviewListMap(Integer.parseInt(productType), Integer.parseInt(flowType),orderId, false,"");
//		List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(11L, Context.ORDER_TYPE_ISLAND, Context.REVIEW_FLOWTYPE_CHANGE_PRICE, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("orderId", orderId);
//		map.put("orderUuid", orderUuid);
//		map.put("productType",productType);
//		map.put("flowType", flowType);
//		model.addAttribute("changePriceList", processList);
//		model.addAllAttributes(map);
//		return "review/changePrice/island/islandChangePriceList";
//	}
	

}
