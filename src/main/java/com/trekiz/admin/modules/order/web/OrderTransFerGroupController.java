package com.trekiz.admin.modules.order.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.TransFerGroup;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.service.TransferMoneyService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;

@Controller
@RequestMapping(value = "${adminPath}/orderTransFerGroup/")
public class OrderTransFerGroupController{

	@Autowired
	@Qualifier("orderCommonService")
    private OrderCommonService orderCommonService;
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private AreaService areaServce; 
	@Autowired
	private TransferMoneyService transferMoneyService;
	@Autowired
	private OrderReviewService orderReviewService;
	/**
	 * 转团记录列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="transFerGroupList/{orderId}")
	public String transFerGroupList(@PathVariable String orderId, Model model, HttpServletRequest request){
		
		// 判断订单ID是否合法，不合法返回原页面
		if(NumberUtils.isDigits(orderId)){
			ProductOrderCommon proOrder = orderCommonService.getProductorderById(Long.valueOf(orderId));
			// 判断订单是否存在
			if(proOrder!=null){
				List<Map<String, String>>  list = reviewService.findReviewListMap(proOrder.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, orderId, Context.REVIEW_ACTIVE_INEFFECTIVE);
				// 将审批信息导入
				if(!list.isEmpty()){
					List<TransFerGroup> backMapList = new ArrayList<TransFerGroup>();
					Iterator<Map<String,String>> iter  = list.iterator();
					while(iter.hasNext()){
						Map<String,String> map = iter.next();
						TransFerGroup back = new TransFerGroup();
						// 判断游客是否存在
						if(StringUtils.isNotBlank(map.get("travelerId"))){
							Traveler traveler = travelerService.findTravelerById(Long.valueOf(map.get("travelerId")));
							back.setTravelerId(map.get("travelerId"));		// 游客Id
							back.setTravelerName(traveler.getName());	// 游客姓名
							String money = map.get("money");
							if (StringUtils.isBlank(money)) {
								money = moneyAmountService.getMoney(traveler.getOriginalPayPriceSerialNum());
							}
							back.setMoney(money);
							// 转团后应付金额
							back.setSubtractMoney(map.get("subtractMoney"));
						}
						back.setCreateDate(proOrder.getCreateDate());	// 创建时间
						back.setApplyDate(DateUtils.dateFormat(map.get("applyDate")));	// 报批日期
						back.setRemark(map.get("createReason"));	// 转团原因
						back.setNewGroupCode(map.get("newGroupCode"));	// 新团期code
						back.setStatus(Integer.valueOf(map.get("status")));// 审批状态
						back.setReviewId(map.get("id"));
						backMapList.add(back);
					}
					model.addAttribute("list",backMapList);
				}
				model.addAttribute("orderId",orderId);
				// 订单类型
				model.addAttribute("orderStatus", OrderCommonUtil.getChineseOrderType(proOrder.getOrderStatus().toString()));
				return "modules/order/transferGroup/transferGroupOrder";
			}
		}
		return "modules/order/transferGroup/transferGroupOrder";
	}
	
	/**
	 * 转团申请处理页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="transferGroupApply/{orderId}")
	public String transferGroupApply(@PathVariable String orderId,Model model, HttpServletRequest request){
		
		if(NumberUtils.isDigits(orderId)){
			// 获取订单实体
			ProductOrderCommon proOrder = orderCommonService.getProductorderById(Long.valueOf(orderId));
			// 获取订单应付款总额
			String totalMoney = moneyAmountService.getMoney(proOrder.getTotalMoney());
			model.addAttribute("totalMoney",totalMoney);
			model.addAttribute("order",proOrder);
			// 订单类型
			model.addAttribute("orderStatus", OrderCommonUtil.getChineseOrderType(proOrder.getOrderStatus().toString()));
			
			//订单价格千位符处理
			//千位符处理：订单总金额、已付金额、到账金额
			List<String> priceList = Lists.newArrayList();
			priceList.add("totalMoney");
			priceList.add("payedMoney");
			priceList.add("accountedMoney");
			handlePrice(proOrder, model);
			
			// 获取产品实体
			TravelActivity travelActivity = travelActivityService.findById(proOrder.getProductId());
			model.addAttribute("product",travelActivity);
			// 获取团期实体
			ActivityGroup activityGroup = activityGroupService.findById(proOrder.getProductGroupId());
			model.addAttribute("group",activityGroup);
			// 出发城市
			if(travelActivity.getFromArea()!=null){
				Area fromArea = areaServce.get(Long.valueOf(travelActivity.getFromArea()));
				model.addAttribute("fromArea",fromArea);
			}
			// 离境城市
			if(travelActivity.getOutArea()!=null){
				Area outArea = areaServce.get(Long.valueOf(travelActivity.getOutArea()));
				model.addAttribute("outArea",outArea);
			}
			// 获取游客信息
			List<Traveler> list = travelerService.findTravelerByOrderIdAndOrderType(Long.valueOf(orderId), proOrder.getOrderStatus());
			List<Traveler> backList = Lists.newArrayList();
			// 获取每个游客的结算价
			if(!list.isEmpty()){
				Iterator<Traveler>  iter = list.iterator();
				while(iter.hasNext()){
					Traveler tra = iter.next();
					// 判断游客状态是否可以转团
					if(tra.getDelFlag().equals(0)){
						String money = moneyAmountService.getMoney(tra.getPayPriceSerialNum());
						List<Object[]> objList = moneyAmountService.getMoneyAmonut(tra.getPayPriceSerialNum());
						if (CollectionUtils.isNotEmpty(objList)) {
							String currencyId = "";
							for (Object[] obj : objList) {
								currencyId += obj[0] + ",";
							}
							tra.setSubtractMoneySerialNum(currencyId);
						}
						tra.setPayPriceSerialNumInfo(money);
						backList.add(tra);
					}
				}
			}
			if(!backList.isEmpty()){
				model.addAttribute("travelList",backList);
			}
			model.addAttribute("user",UserUtils.getUser());
			model.addAttribute("orderId",orderId);
			model.addAttribute("orderStatusNum",proOrder.getOrderStatus().toString());
			return "modules/order/transferGroup/transferGroupApply";
		}
		return "modules/order/transferGroup/transferGroupOrder";
	}
	
	/**
	 * 订单金额千位符处理：成人价、儿童价、特殊人群价格
	 * @param listin
	 * @param paraList
	 */
	private void handlePrice(ProductOrderCommon order, Model model) {
		
		if (StringUtils.isNotBlank(order.getSettlementAdultPrice())) {
			String settlementAdultPrice = moneyAmountService.getMoneyStr(order.getSettlementAdultPrice());
			model.addAttribute("settlementAdultPrice", settlementAdultPrice);
		}
		
		if (StringUtils.isNotBlank(order.getSettlementcChildPrice())) {
			String settlementcChildPrice = moneyAmountService.getMoneyStr(order.getSettlementcChildPrice());
			model.addAttribute("settlementcChildPrice", settlementcChildPrice);
		}
		
		if (StringUtils.isNotBlank(order.getSettlementSpecialPrice())) {
			String settlementSpecialPrice = moneyAmountService.getMoneyStr(order.getSettlementSpecialPrice());
			model.addAttribute("settlementSpecialPrice", settlementSpecialPrice);
		}
	}
	
	/**
	 * 跳转到转团详情页面
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="transferGroupInfo/{newGroupCode}/{travelerId}/{orderId}/{reviewId}")
	public String transferGroupInfo(@PathVariable String newGroupCode,@PathVariable String travelerId,@PathVariable String orderId,@PathVariable String reviewId,Model model, HttpServletRequest request){
		if(NumberUtils.isDigits(orderId) && NumberUtils.isDigits(travelerId) && StringUtils.isNotBlank(newGroupCode) && StringUtils.isNotBlank(reviewId)){
			// 获取订单实体
			ProductOrderCommon proOrder = orderCommonService.getProductorderById(Long.valueOf(orderId));
			// 获取订单应付款总额
			String totalMoney = moneyAmountService.getMoney(proOrder.getTotalMoney());
			model.addAttribute("totalMoney",totalMoney);
			model.addAttribute("order",proOrder);
			// 获取产品实体
			TravelActivity travelActivity = travelActivityService.findById(proOrder.getProductId());
			model.addAttribute("product",travelActivity);
			// 获取团期实体
			ActivityGroup activityGroup = activityGroupService.findById(proOrder.getProductGroupId());
			model.addAttribute("group",activityGroup);
			// 出发城市
			if(travelActivity.getFromArea()!=null){
				Area fromArea = areaServce.get(Long.valueOf(travelActivity.getFromArea()));
				model.addAttribute("fromArea",fromArea);
			}
			// 离境城市
			if(travelActivity.getOutArea()!=null){
				Area outArea = areaServce.get(Long.valueOf(travelActivity.getOutArea()));
				model.addAttribute("outArea",outArea);
			}
			// 转入团的信息
			ActivityGroup newGroup =  activityGroupService.findByGroupCode(newGroupCode);
			model.addAttribute("newGroup",newGroup);
			
			// 游客信息
			Traveler tra = travelerService.findTravelerById(Long.valueOf(travelerId));
			String money = moneyAmountService.getMoney(tra.getPayPriceSerialNum());
			tra.setPayPriceSerialNumInfo(money);
			model.addAttribute("travel",tra);
			
			// 审核动态
			List<ReviewLog> logList = reviewService.findReviewLog(Long.valueOf(reviewId));
			model.addAttribute("logList",logList);
			// 审核实体
			Review review =reviewService.findReviewInfo(Long.valueOf(reviewId));
			model.addAttribute("review",review);
			model.addAttribute("user",UserUtils.getUser());
			
			return "modules/order/transferGroup/transferGroupInfo";
		}
		return null;
	}
	
	/**
	 * 通过指定团号，获取团信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="newGroupInfoAjax")
	public Map<String,String> newGroupInfoAjax(HttpServletResponse response,HttpServletRequest request){
		response.setContentType("application/json; charset=UTF-8");
		String groupId = request.getParameter("groupId");
		Map<String, String> map = new HashMap<String,String>();
		// 获取新的团期实体 srcActivityId
		ActivityGroup activityGroup = activityGroupService.findByGroupCode(groupId);
		if(activityGroup==null){
			map.put("res","data_error");
			return map;
		}
		// 获取产品实体
		TravelActivity travelActivity = travelActivityService.findById(Long.valueOf(activityGroup.getSrcActivityId()));
		if(activityGroup!=null && travelActivity!=null){
			map.put("res","success");
			// 团队类型
			if(travelActivity.getActivityKind()!=null){
				map.put("groupType", OrderCommonUtil.getChineseOrderType(travelActivity.getActivityKind().toString()));// 团队类型
			}
			
			map.put("leaveSit", activityGroup.getFreePosition().toString());	// 余位
			map.put("groupCode", activityGroup.getGroupCode().toString());	// 团号
			map.put("createBy", travelActivity.getCreateBy().getName()); // 创建人
			map.put("productName", travelActivity.getAcitivityName().toString()); // 产品名称
			//map.put("activityGroup", activityGroup); // 产品实体
			map.put("payMode_advance",travelActivity.getPayMode_advance()==1?"1":"0"); 	// 预占位标识
			map.put("payMode_deposit",travelActivity.getPayMode_deposit()==1?"1":"0"); // 订金占位标识
			map.put("payMode_full",travelActivity.getPayMode_full()==1?"1":"0" ); // 全款占位标识
			map.put("payMode_op",travelActivity.getPayMode_op()==1?"1":"0" ); // 计调确认占位标识
			map.put("payMode_cw",travelActivity.getPayMode_cw()==1?"1":"0" ); // 计调确认占位标识
			map.put("payMode_data",travelActivity.getPayMode_data()==1?"1":"0" ); // 资料占位标识
			map.put("payMode_guarantee",travelActivity.getPayMode_guarantee()==1?"1":"0" ); // 担保占位标识
			map.put("payMode_express",travelActivity.getPayMode_express()==1?"1":"0"); // 确认单占位标识
			map.put("remainDays_advance", travelActivity.getRemainDays_advance()!=null&&travelActivity.getRemainDays_advance()>0?travelActivity.getRemainDays_advance().toString():"0"); // 预占位保留天数
			map.put("remainDays_deposit", travelActivity.getRemainDays_deposit()!=null&&travelActivity.getRemainDays_deposit()>0?travelActivity.getRemainDays_deposit().toString():"0"); // 订金占位保留天数
			map.put("remainDays_guarantee", travelActivity.getRemainDays_guarantee()!=null&&travelActivity.getRemainDays_guarantee()>0?travelActivity.getRemainDays_guarantee().toString():"0"); // 担保占位保留天数
			map.put("remainDays_express", travelActivity.getRemainDays_express()!=null&&travelActivity.getRemainDays_express()>0?travelActivity.getRemainDays_express().toString():"0"); // 确认单占位保留天数
			map.put("remainDays_data", travelActivity.getRemainDays_data()!=null&&travelActivity.getRemainDays_data()>0?travelActivity.getRemainDays_data().toString():"0"); // 资料占位保留天数
		}else{
			map.put("res","data_error");
		}		
		return map;
	}
	
	/**
	 * 转团申请
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="changeGroup", method=RequestMethod.POST)
	public Map<String,String> changeGroup(HttpServletResponse response,HttpServletRequest request,Model model){
		response.setContentType("application/json; charset=UTF-8");
		Map<String,String> map = new HashMap<String,String>();
		String[] travelId = request.getParameterValues("paramTravelId");
		String[] travelIds = travelId[0].split(",");
		String groupCode = request.getParameter("paramGroupCode");
		String subtractMoneyArr = request.getParameter("subtractMoneyArr"); // 转团后应付金额
		String[] remarks = request.getParameterValues("paramRemark"); // 转团理由
		String[] remark = remarks[0].split(",");
		String payType = request.getParameter("paramPayType"); // 支付方式
		String remainDays = request.getParameter("paramRemainDays"); // 保留天数
		
		// 如果游客ID和团期code为空，返回error
		if(StringUtils.isBlank(travelIds[0])){
			map.put("res", "data_error");
			map.put("message", "申请失败，转团游客不存在。");
			return map;
		}
		if(StringUtils.isBlank(groupCode)){
			map.put("res", "data_error");
			map.put("message", "申请失败，转入团期不存在。");
			return map;
		}
		
		
		Long oldOrderId = null;
		// 获取全部要转团的游客
		List<Traveler> travelList = new ArrayList<Traveler>();
		// 获取新团期实体
		ActivityGroup activityGroup = activityGroupService.findByGroupCode(groupCode);
		// 如果团期为空，返回错误
		if(activityGroup==null){
			map.put("res", "data_error");
			map.put("message", "申请失败，请核实转团信息。");
			return map;
		}
		for(String id : travelIds){
			Traveler tra = travelerService.findTravelerById(Long.valueOf(id));
			// 判断新团期里是否有该游客类型的报价（成人同行价，儿童同行价，特殊同行价）
			if(tra.getPersonType()==1){
				if(activityGroup.getSettlementAdultPrice()==null){
					map.put("res", "data_error");
					map.put("message", "申请失败，新团期不存在成人价格。");
					return map;
				}
			}
			if(tra.getPersonType()==2){
				if(activityGroup.getSettlementcChildPrice()==null){
					map.put("res", "data_error");
					map.put("message", "申请失败，新团期不存在儿童价格。");
					return map;
				}
			}
			if(tra.getPersonType()==3){
				if(activityGroup.getSettlementSpecialPrice()==null){
					map.put("res", "data_error");
					map.put("message", "申请失败，新团期不存在特殊人群价格。");
					return map;
				}
			}
//			tra.setDelFlag(4); // 设为转团审核
//			travelerService.saveTraveler(tra);
			oldOrderId = tra.getOrderId();	// 获取原订单ID
			travelList.add(tra);
		}
		// 获取原订单实体
		ProductOrderCommon oldOrder = new ProductOrderCommon();
		if(oldOrderId!=null){
			oldOrder = orderCommonService.getProductorderById(oldOrderId);
		}
		// 获取原产品实体
		TravelActivity ta = travelActivityService.findById(oldOrder.getProductId());
		if(ta==null){
			map.put("res", "data_error");
			map.put("message", "申请失败，原订单产品不存在。");
			return map;
		}
		// 如果游客为空，返回错误
		if(travelList.isEmpty()){
			map.put("res", "data_error");
			map.put("message", "申请失败，请核实转团信息。");
			return map;
		}
		
		// 判断余位，如果该团期没有余位，返回为空
		if(!StringUtils.isNumeric(activityGroup.getFreePosition().toString()) || activityGroup.getFreePosition()<1){
			map.put("res", "data_error");
			map.put("message", "申请失败，团期余位不足。");
			return map;
		}
		
		////////////////////////////////////////////////////////////////
		/**
		 * add by ruyi.chen 
		 * add date 2014-04-13
		 * 转款申请增加流程互斥判断
		 */
		String[] travelNames = request.getParameterValues("paramTravelerName");
		String[] travelName = travelNames[0].split(",");
		//组装申请转款判断游客Map
		Map<Long,String> travelerMap = Maps.newHashMap();
		List<Long> travelerIds = Lists.newArrayList();
		if(travelIds.length > 0){
			for(int i =0;i<travelIds.length;i++){
				travelerMap.put(Long.parseLong(travelIds[i]), travelName[i]);
				travelerIds.add(Long.parseLong(travelIds[i]));
			}
			
		}
		Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(oldOrder.getId(), oldOrder.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, travelerIds);
		@SuppressWarnings("unchecked")
		Map<String,Object> travelerResultMap = (Map<String,Object>) rMap.get(Context.MUTEX_RESULT_lIST);
		boolean flag = false;
		StringBuffer sf = new StringBuffer();
		for(Long tid : travelerIds){
			if("1".equals(travelerResultMap.get(tid.toString()).toString().split("/")[0])){
				flag = true;
				sf.append(travelerMap.get(tid)+" "+travelerResultMap.get(tid.toString()).toString().split("/")[2]+" ");
			}
		}
		if(flag){
			map.put("res", "data_error");
			map.put("message", sf.toString());
			map.put(Context.RESULT, "1");
			return map;
		}
		//流程互斥部分结束
		////////////////////////////////////////////////////////////////		
		// 申请转团
		StringBuffer reply = new StringBuffer();	// 失败时返回原因
		try{
			map = transferMoneyService.addReviewList(travelList, oldOrder, remark, ta, groupCode, payType, remainDays, reply, map, subtractMoneyArr);
		}catch(Exception e){
			map.put("res", "data_error");
			map.put("message", e.getMessage());
		}
		/**
		 * 
		Long back=  new Long(0);
		for(int n=0;n<travelList.size();n++){
			Traveler t = travelList.get(n);
			// 转团实体
			TransFerGroup tran = getTransFerGroup(t, oldOrder, n, remark, groupCode,payType,remainDays);
			List<Detail> listDetail = getDetailList(tran);	// 申请细节
			
			// 提交申请(包含原产品部门)
			back= reviewService.addReview(oldOrder.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, oldOrder.getId().toString(), t.getId(), Long.valueOf(n), remark[n], reply, listDetail,ta.getDeptId());
		}
		if(back!=0){
			map.put("res", "success");
			map.put("message", "申请成功，请等待审核");
		}else{
			map.put("res", "data_error");
			map.put("message",reply.);
		}*/
		for(Traveler t : travelList){
			t.setDelFlag(4); // 设为转团审核
			travelerService.saveTraveler(t);
		}
		
		return map;
	}
	/**
	 * 创建申请实体类TransFerGroup
	 * @param traveler		游客实体
	 * @param order			原订单实体
	 * @param index			数组序号
	 * @param remarks		转团说明数组
	 * @param groupCode	新团期号
	 * @return
	 
	private  TransFerGroup getTransFerGroup(Traveler traveler,ProductOrderCommon order,int index,String[] remarks,String groupCode,String payType,String remainDays){
		TransFerGroup bean = new TransFerGroup();
		// 游客ID
		if(traveler.getId()!=null){
			bean.setTravelerId(traveler.getId().toString());
		}
		// 游客姓名
		if(StringUtils.isNotBlank(traveler.getName())){
			bean.setTravelerName(traveler.getName());
		}
		// 老订单创建日期
		if(order.getCreateDate()!=null){
			bean.setCreateDate(order.getCreateDate());
		}
		// 申请日期
		bean.setApplyDate(new Date());
		// 转团说明
		if(StringUtils.isNotBlank(remarks[index])){
			bean.setRemark(remarks[index]);
		}
		// 应付金额
		if(StringUtils.isNotBlank(traveler.getPayPriceSerialNum())){
			String money = moneyAmountService.getMoneys("'"+traveler.getPayPriceSerialNum()+"'");
			bean.setMoney(money);
		}
		// 原订单ID
		if(order.getId()!=null){
			bean.setOldOrderId(order.getId().toString());
		}
		// 新团期
		if(StringUtils.isNotBlank(groupCode)){
			bean.setNewGroupCode(groupCode);
		}
		// 支付方式
		if(StringUtils.isNotBlank(payType)){
			bean.setPayType(payType);
		}
		// 保留天数
		if(StringUtils.isNotBlank(remainDays)){
			bean.setRemainDays(remainDays);
		}
		return bean;
	}*/
	/**
	 * 获取申请细节
	 * @return
	
	private List<Detail> getDetailList(TransFerGroup bean){
		List<Detail> detailList = new ArrayList<Detail>();

		Map<String, String> map = bean.getReviewDetailMap();
		for (Entry<String, String> entry : map.entrySet()) {
			if(!"newOrderId".equals(entry.getKey())){
				detailList.add(new Detail(entry.getKey(), entry.getValue()));
			}
		}
		
		return detailList;
	} */
}
