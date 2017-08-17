package com.trekiz.admin.modules.order.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderExitGroupReviewVO;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.service.TransferMoneyService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewLogService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;

/**
 * 
 * @author ruyi.chen
 *参团订单审核类Controller(退团审核列表、退团审核详情)
 */
@Controller
@RequestMapping(value = "${adminPath}/orderReview/manage")
public class OrderReviewController extends BaseController{

	@Autowired
    private OrderCommonService orderService;
	@Autowired
    private OrderReviewService orderReviewService;
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
    @Autowired
    private ActivityGroupService activityGroupService;
    @Autowired
    TransferMoneyService transferMoneyService;
    @Autowired
	private MoneyAmountService moneyAmountService;
    @Autowired
	private AreaService areaServce; 
    @Autowired
	private TravelerService travelerService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ReviewLogService reviewLogService;
    @Autowired
    private OfficeService officeService;
    
    private static Logger logger = LoggerFactory.getLogger(OrderReviewController.class);
	/**
	 * add by ruyi.chen
	 * add date 2014-12-15
	 * 获取退团审核列表信息
	 * @param vo
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="getExitGroupReviewList")
	public String getExitGroupReviewList( @ModelAttribute OrderExitGroupReviewVO vo, HttpServletResponse response,
	        Model model, HttpServletRequest request){
				
		//获取部门职务列表		
		List<UserJob> userJobs = reviewCommonService. getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		Set<Integer> set = Sets.newHashSet();
		List <UserJob> userJobList = Lists.newArrayList();
		Page<Map<Object, Object>> pageInfo = null;
		List<User> userList =  systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
		String returnStr = "modules/review/exitGroupList";
		if(null != userJobs && 0 < userJobs.size()){
			if(!StringUtils.isNotBlank(vo.getOrderType())){
				for( UserJob uj : userJobs){
					set.add(uj.getOrderType());
				}
				vo.setOrderType(set.toArray()[0].toString());
				
			}
			for( UserJob uj : userJobs){
				if(uj.getOrderType().toString().equals(vo.getOrderType())){
					userJobList.add(uj);
				}
			}
			if(null !=vo.getOrderType() && vo.getOrderType().equals("12")){
				pageInfo=orderReviewService.getExitGroupReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
				returnStr = "modules/review/exitGroupList";
			}else {
				pageInfo=orderReviewService.getExitGroupReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
			}
				
		}
		model.addAttribute("page", pageInfo);
		model.addAttribute("userJobs", userJobList);
		model.addAttribute("reviewVO", vo);
		model.addAttribute("userList", userList);
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		
		return returnStr;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2014-12-15
	 * 获取退团审核列表信息
	 * @param vo
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="getIslandRefundReviewList")
	public String getIslandRefundReviewList( @ModelAttribute OrderExitGroupReviewVO vo, HttpServletResponse response,
	        Model model, HttpServletRequest request){
				
		//获取部门职务列表		
		List<UserJob> userJobs = reviewCommonService. getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		Page<Map<Object, Object>> pageInfo=orderReviewService.getExitGroupReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobs);
		model.addAttribute("page", pageInfo);
		model.addAttribute("userJobs", userJobs);
		model.addAttribute("reviewVO", vo);
		return "modules/review/islandRefundList";
	}
	/**
	 * add by ruyi.chen
	 * add date 2014-12-15
	 * 获取退团审核列表信息
	 * @param vo
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="getHotelRefundReviewList")
	public String getHotelRefundReviewList( @ModelAttribute OrderExitGroupReviewVO vo, HttpServletResponse response,
	        Model model, HttpServletRequest request){
				
		//获取部门职务列表		
		List<UserJob> userJobs = reviewCommonService. getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		Page<Map<Object, Object>> pageInfo=orderReviewService.getExitGroupReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobs);
		model.addAttribute("page", pageInfo);
		model.addAttribute("userJobs", userJobs);
		model.addAttribute("reviewVO", vo);
		return "modules/review/exitGroupList";
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2014-12-30
	 * 获取转团审核列表信息
	 * @param vo
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="getChangeGroupReviewList")
	public String getChangeGroupReviewList( @ModelAttribute OrderExitGroupReviewVO vo, HttpServletResponse response,
	        Model model, HttpServletRequest request){
		
		//获取部门职务列表		
		List<UserJob> userJobs = reviewCommonService. getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP);
		Set<Integer> set = Sets.newHashSet();
		List <UserJob> userJobList = Lists.newArrayList();
		Page<Map<Object, Object>> pageInfo = null;
		List<User> userList =  systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
		String returnStr = "modules/review/changeGroupList";
		if(null != userJobs && 0 < userJobs.size()){
			if(!StringUtils.isNotBlank(vo.getOrderType())){
				for( UserJob uj : userJobs){
					set.add(uj.getOrderType());
				}
				vo.setOrderType(set.toArray()[0].toString());
				
			}
			for( UserJob uj : userJobs){
				//mod by hongyi.cao
//				if(uj.getOrderType().toString().equals(vo.getOrderType())){
					userJobList.add(uj);
//				}
			}
			if(null !=vo.getOrderType() && vo.getOrderType().equals("12")){
				pageInfo=orderReviewService.getTransferGroupReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
				returnStr = "modules/review/changeGroupList";
			}else {
				pageInfo=orderReviewService.getTransferGroupReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
			}
				
		}
		model.addAttribute("page", pageInfo);
		model.addAttribute("userJobs", userJobList);
		model.addAttribute("reviewVO", vo);
		model.addAttribute("userList", userList);
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		return returnStr;
	}
	/**
	 * add by ruyi.chen
	 * add date 2014-12-30
	 * 获取转团转款审核列表信息
	 * @param vo
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="getTransFerMoneyReviewList")
	public String getTransFerMoneyReviewList( @ModelAttribute OrderExitGroupReviewVO vo, HttpServletResponse response,
	        Model model, HttpServletRequest request){
		
		//获取部门职务列表		
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY);
		
		Set<Integer> set = Sets.newHashSet();
		List <UserJob> userJobList = Lists.newArrayList();
		Page<Map<Object, Object>> pageInfo = null;
		String returnStr = "modules/review/transFerMoneyList";
		if(null != userJobs && 0 < userJobs.size()){
			if(!StringUtils.isNotBlank(vo.getOrderType())){
				for( UserJob uj : userJobs){
					set.add(uj.getOrderType());
				}
				vo.setOrderType(set.toArray()[0].toString());
				
			}
			for( UserJob uj : userJobs){
				if(uj.getOrderType().toString().equals(vo.getOrderType())){
					userJobList.add(uj);
				}
			}
			if(null !=vo.getOrderType() && vo.getOrderType().equals("12")){
				pageInfo=orderReviewService.getTransferMoneyReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
				returnStr = "modules/review/transFerMoneyList";
			}else {
				pageInfo=orderReviewService.getTransferMoneyReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
			}
				
		}
		model.addAttribute("page", pageInfo);
		model.addAttribute("userJobs", userJobList);
		model.addAttribute("reviewVO", vo);
		model.addAttribute("users", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		return returnStr;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2014-12-16
	 * 审核退团申请信息
	 */
	@RequestMapping(value ="reviewExitGroup")
	public String reviewExitGroup(  HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel){
		Map<String, Object> travelerList=orderService.getExitGroupReviewInfoById(rid);
		List<ReviewLog> rLog=reviewService.findReviewLog(rid);
		Review review = reviewService.findReviewInfo(rid);
		model.addAttribute("review",review);
		model.addAttribute("rLog",rLog);
		model.addAttribute("orderInfo",travelerList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("rid",rid);
		model.addAttribute("roleId",roleId);
		model.addAttribute("userLevel",userLevel);
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		return "modules/review/reviewExitGroupInfo";
	}
	/**
	 * add by ruyi.chen
	 * add date 2014-12-16
	 * 查看退团申请审核信息
	 */
	@RequestMapping(value ="viewExitGroupReviewInfo")
	public String viewExitGroupReviewInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel){
		Map<String, Object> travelerList=orderService.getExitGroupReviewInfoById(rid);
		List<ReviewLog> rLog=reviewService.findReviewLog(rid);
		model.addAttribute("rLog",rLog);
		Review review = reviewService.findReviewInfo(rid);
		model.addAttribute("review",review);
		model.addAttribute("orderInfo",travelerList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("rid",rid);
		model.addAttribute("roleId",roleId);
		model.addAttribute("userLevel",userLevel);
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		return "modules/review/viewExitGroupReviewInfo";
	}
	/**
	 * add by ruyi.chen
	 * add date 2014-12-31
	 * 审核     转团申请信息
	 */
	@RequestMapping(value ="reviewChangeGroup")
	public String reviewChangeGroup(  HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel){
		Map<String, Object> travelerList=orderService.getExitGroupReviewInfoById(rid);
		//添加订单详情信息
		
		model.addAttribute("orderInfo",travelerList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("rid",rid);
		model.addAttribute("roleId",roleId);
		model.addAttribute("userLevel",userLevel);
		
		ProductOrderCommon proOrder = orderService.getProductorderById(Long.valueOf(orderId));
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
		
		Map<String,String> map=reviewService.findReview(rid);
		// 转入团的信息
		ActivityGroup newGroup =  activityGroupService.findByGroupCode(map.get("newGroupCode"));
		model.addAttribute("newGroup",newGroup);
					
		// 游客信息
		Traveler tra = travelerService.findTravelerById(Long.valueOf(map.get("travelerId")));
		String money = moneyAmountService.getMoney(tra.getPayPriceSerialNum());
		tra.setPayPriceSerialNumInfo(money);
		model.addAttribute("travel",tra);
					
		// 审核动态
		List<ReviewLog> logList = reviewService.findReviewLog(proOrder.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP,orderId);
		model.addAttribute("logList",logList);
		// 审核实体
		Review review =reviewService.findReviewInfo(Long.valueOf(rid));
		model.addAttribute("review",review);
		return "modules/review/reviewChangeGroupInfo";
	}
	/**
	 * add by ruyi.chen
	 * add date 2014-12-31
	 * 查看转团申请审核信息
	 */
	@RequestMapping(value ="viewChangeGroupReviewInfo")
	public String viewChangeGroupReviewInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel){
		Map<String, Object> travelerList=orderService.getExitGroupReviewInfoById(rid);
		//添加订单详情信息
		
		model.addAttribute("orderInfo",travelerList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("rid",rid);
		model.addAttribute("roleId",roleId);
		model.addAttribute("userLevel",userLevel);
		
		ProductOrderCommon proOrder = orderService.getProductorderById(Long.valueOf(orderId));
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
		
		Map<String,String> map=reviewService.findReview(rid);
		// 转入团的信息
		String newGroupCode=map.get("newGroupCode");
		ActivityGroup newGroup =  activityGroupService.findByGroupCode(newGroupCode);
		model.addAttribute("newGroup",newGroup);
					
		// 游客信息
		Traveler tra = travelerService.findTravelerById(Long.valueOf(map.get("travelerId")));
		String money = moneyAmountService.getMoney(tra.getPayPriceSerialNum());
		tra.setPayPriceSerialNumInfo(money);
		model.addAttribute("travel",tra);
					
		// 审核动态
		List<ReviewLog> logList = reviewService.findReviewLog(Long.valueOf(rid));
		model.addAttribute("logList",logList);
		// 审核实体
		Review review =reviewService.findReviewInfo(Long.valueOf(rid));
		model.addAttribute("review",review);
		model.addAttribute("user",UserUtils.getUser());
		return "modules/review/viewChangeGroupReviewInfo";
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
	 * add by ruyi.chen
	 * add date 2014-12-31
	 * 审核转团转款申请信息
	 */
	@RequestMapping(value ="reviewTransFerMoney")
	public String reviewTransFerMoney(  HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel){
		Map<String, Object> travelerList=orderService.getExitGroupReviewInfoById(rid);
		Map<String, Long> condition = new HashMap<String, Long>();
		condition.put("reviewId", rid);
		Map<String, Object> baseInfoMap = transferMoneyService.transferMoneyDetails(condition);
		Map<String, Object> reviewInfoMap = transferMoneyService.transferMoneyReviewInfo(condition);
		model.addAllAttributes(reviewInfoMap);
		model.addAllAttributes(baseInfoMap);
		model.addAttribute("orderInfo",travelerList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("rid",rid);
		model.addAttribute("roleId",roleId);
		model.addAttribute("userLevel",userLevel);
		return "modules/review/reviewTransFerMoneyInfo";
	}
	/**
	 * add by ruyi.chen
	 * add date 2014-12-30
	 * 查看转团转款申请审核信息
	 */
	@RequestMapping(value ="viewTransFerMoneyReviewInfo")
	public String viewTransFerMoneyReviewInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel){
		Map<String, Object> travelerList=orderService.getExitGroupReviewInfoById(rid);
		Map<String, Long> condition = new HashMap<String, Long>();
		condition.put("reviewId", rid);
		Map<String, Object> baseInfoMap = transferMoneyService.transferMoneyDetails(condition);
		model.addAllAttributes(baseInfoMap);
		Map<String, Object> reviewInfoMap = transferMoneyService.transferMoneyReviewInfo(condition);
		model.addAllAttributes(reviewInfoMap);
		model.addAttribute("orderInfo",travelerList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("rid",rid);
		model.addAttribute("roleId",roleId);
		model.addAttribute("userLevel",userLevel);
		return "modules/review/viewTransFerMoneyReviewInfo";
	}
	/**
	 * add by ruyi.chen
	 * add date 2014-12-16
	 * 根据订单类型获取用户对应的审核层级角色(暂不可用)
	 */
	@ResponseBody
	@RequestMapping(value ="getReviewRoleInfoByType")
	public JSONArray getReviewRoleInfoByType(HttpServletResponse response,
	        Model model, HttpServletRequest request,Integer orderType){

		List<Role> roleList=reviewCommonService.getWorkFlowRolesByFlowType(orderType, 8);
		if(roleList.size()>0){
			JSONArray js=JSONArray.fromObject(roleList);
//			System.out.println(js);
			model.addAttribute("roleInfo",js);
			return js;
		}else{
			return null;
		}
	}
	/**
	 * add by ruyi.chen
	 * add date 2014-12-17
	 * 审核Controller,审核通过或者驳回,当最后一个人审核通过后，自动进入实际退团操作
	 * @param response
	 * @param model
	 * @param request
	 * @param rid
	 * @param roleId
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="reviewExitGroupInfo")
	public Map<String,Object> reviewExitGroupInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,long roleId,Integer result,String denyReason,Integer userLevel){

		Map<String, Object> map =new HashMap<String,Object>();
		try {
			map = orderReviewService.reviewExitGroup(rid, roleId, result, denyReason, userLevel, request);
		} catch (NumberFormatException e) {
			map.put("flag", 0);
			map.put("message", "操作失败！");
			return map;
		} catch (Exception e) {
			map.put("flag", 0);
			map.put("message", "操作失败！");
			return map;
		}
		
		
		return map;
	}
	/**
	 * add date 20160805 批量退团申请审核通过
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="batchReviewExitGroupInfo")
	public Map<String,Object> batchReviewExitGroupInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String userLevel = request.getParameter("userLevel"); // 当前用户审核等级
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		
		int num = 0;
		Map<String, Object> back =new HashMap<String,Object>();
		if (StringUtils.isBlank(result)) {
			back.put("res", "error");
			back.put("msg", "审批结果不能为空");
			return back;
		}
		for (int i = 0; i < levelandrevids.length; i++) {
			String revid = levelandrevids[i].split("@")[1];

			try {
				orderReviewService.reviewExitGroup(Long.valueOf(revid), Long.valueOf(0), Integer.valueOf(result), remarks, Integer.valueOf(userLevel), request);
				num++;
			} catch (NumberFormatException e) {
				logger.error("批量退团申请: 审核ID为："+revid+"；  报错原因："+e.getMessage());
			} catch (Exception e) {
				logger.error("批量退团申请: 审核ID为："+revid+"；  报错原因："+e.getMessage());
			}
		}
		back.put("res", "success");
		back.put("msg", "共有"+num+"条记录操作成功。");
		return back;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2014-12-31
	 * 审核Controller,审核通过或者驳回,当最后一个人审核通过后，自动进入实际转团操作
	 * @param response
	 * @param model
	 * @param request
	 * @param rid
	 * @param roleId
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="reviewChangeGroupInfo")
	public Map<String,Object> reviewChangeGroupInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,long roleId,Integer result,String denyReason,Integer userLevel){
		Map<String, Object> map =new HashMap<String,Object>();
		try {
			map = orderReviewService.reviewChangeGroup(rid, roleId, result, denyReason, userLevel, request);
		} catch (Exception e) {
			map.put("flag", 0);
			map.put("message", e.getMessage());
			return map;
		}
		
		return map;
	}
	
	/**
	 * 20160811 批量转团申请审核通过
	 * @author gao
	*  2015年8月11日
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="batchReviewChangeGroupInfo")
	public Map<String,Object> batchReviewChangeGroupInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String userLevel = request.getParameter("userLevel"); // 当前用户审核等级
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		int num = 0;
		Map<String, Object> back =new HashMap<String,Object>();
		if (StringUtils.isBlank(result)) {
			back.put("res", "error");
			back.put("msg", "审批结果不能为空");
			return back;
		}
		for (int i = 0; i < levelandrevids.length; i++) {
			String revid = levelandrevids[i].split("@")[1];
			
			try {
				if("1".equals(result)){
					orderReviewService.reviewChangeGroup(Long.valueOf(revid), Long.valueOf(0), Integer.valueOf(result), "", Integer.valueOf(userLevel), request);
				}else{
					orderReviewService.reviewChangeGroup(Long.valueOf(revid), Long.valueOf(0), Integer.valueOf(result), remarks, Integer.valueOf(userLevel), request);
				}
				ReviewLog reviewLog = new ReviewLog();
				reviewLog.setReviewId(Long.valueOf(revid));
				reviewLog.setNowLevel(Integer.valueOf(userLevel));
				reviewLog.setResult(result);
				reviewLog.setRemark(remarks);
				reviewLogService.saveReviewLog(reviewLog);
				num++;
			} catch (Exception e) {
				logger.error("批量审核转团申请: 审核ID为："+revid+"；  报错原因："+e.getMessage());
				back.put("res", "error");
				back.put("msg", "批量审核转团申请: 审核ID为："+revid+"；  报错原因："+e.getMessage());
				return back;
			}
		}
		back.put("res", "success");
		back.put("msg", "共有"+num+"条记录操作成功。");
		return back;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2014-12-31
	 * 审核Controller,审核通过或者驳回,当最后一个人审核通过后，自动进入实际转团转款操作
	 * @param response
	 * @param model
	 * @param request
	 * @param rid
	 * @param roleId
	 * @param result
	 * @param denyReason
	 * @param userLevel
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="reviewTransFerMoneyInfo")
	public Map<String,Object> reviewTransFerMoneyInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,long roleId,Integer result,String denyReason,Integer userLevel){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = orderReviewService.reviewTransFerMoney(rid, roleId, result, denyReason, userLevel);
		} catch (Exception e) {
			map.put("flag", 0);
			map.put("message", e.getMessage());
			return map;
		}
		
		return map;
	}
	
	/**
	 * 20150810 批量审核转团转款申请信息
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="batchReviewTransFerMoneyInfo")
	public Map<String,Object> batchReviewTransFerMoneyInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String userLevel = request.getParameter("userLevel"); // 当前用户审核等级
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		int num = 0;
		Map<String, Object> back =new HashMap<String,Object>();
		if (StringUtils.isBlank(result)) {
			back.put("res", "error");
			back.put("msg", "审批结果不能为空");
			return back;
		}
		for (int i = 0; i < levelandrevids.length; i++) {
			String revid = levelandrevids[i].split("@")[1];
			
			try {
				if("1".equals(result)){
					orderReviewService.reviewTransFerMoney(Long.valueOf(revid), Long.valueOf(0), Integer.valueOf(result), "", Integer.valueOf(userLevel));
				}else{
					orderReviewService.reviewTransFerMoney(Long.valueOf(revid), Long.valueOf(0), Integer.valueOf(result), remarks, Integer.valueOf(userLevel));
				}
				ReviewLog reviewLog = new ReviewLog();
				reviewLog.setReviewId(Long.valueOf(revid));
				reviewLog.setNowLevel(Integer.valueOf(userLevel));
				reviewLog.setResult(result);
				reviewLog.setRemark(remarks);
				reviewLogService.saveReviewLog(reviewLog);
				num++;
			} catch (Exception e) {
				logger.error("批量审核转团转款申请: 审核ID为："+revid+"；  报错原因："+e.getMessage());
			}
		}
		back.put("res", "success");
		back.put("msg", "共有"+num+"条记录操作成功。");
		return back;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2015-05-05
	 * 获取借款审核列表信息
	 * @param vo
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="getBorrowingList")
	public String getBorrowingList( @ModelAttribute OrderExitGroupReviewVO vo, HttpServletResponse response,
	        Model model, HttpServletRequest request){
		
		//获取部门职务列表		
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_BORROWMONEY);
		Set<Integer> set = Sets.newHashSet();
		List <UserJob> userJobList = Lists.newArrayList();
		Page<Map<Object, Object>> pageInfo = null;
		String returnStr = "modules/review/borrowingList";
		if(null != userJobs && 0 < userJobs.size()){
			if(!StringUtils.isNotBlank(vo.getOrderType())){
				for( UserJob uj : userJobs){
					set.add(uj.getOrderType());
				}
				vo.setOrderType(set.toArray()[0].toString());
				
			}
			for( UserJob uj : userJobs){
				if(uj.getOrderType().toString().equals(vo.getOrderType())){
					userJobList.add(uj);
				}
			}
			if(null !=vo.getOrderType() && vo.getOrderType().equals("7")){
				pageInfo=orderReviewService.getPlaneBorrowingReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
				returnStr = "modules/review/planeBorrowingList";
			}else if(null !=vo.getOrderType() && vo.getOrderType().equals("6")){
				///visa/workflow/borrowmoney
				return "redirect:" + Global.getAdminPath() + "/visa/workflow/borrowmoney/visaBorrowMoney4XXZReviewList?flowType=20";
			}else {
				pageInfo=orderReviewService.getBorrowingReviewList(new Page<Map<Object, Object>>(request,response), vo,userJobList);
			}
				
		}else if(null !=vo.getOrderType() && vo.getOrderType().equals("6")){
			 return "redirect:" + Global.getAdminPath() + "/workflow/borrowmoney/visaBorrowMoney4XXZReviewList?flowType=20";
		}else if(null !=vo.getOrderType() && vo.getOrderType().equals("7")){
			returnStr = "modules/review/planeBorrowingList";
		}else if(null !=vo.getOrderType() && vo.getOrderType().equals("11")){
			returnStr = "modules/review/hotelBorrowingList";
			
		}else if(null !=vo.getOrderType() && vo.getOrderType().equals("12")){
			returnStr = "modules/review/islandBorrowingList";
			
		}		
		
		Office office = officeService.get(UserUtils.getUser().getCompany().getId());
		if(office!=null){
			model.addAttribute("confirmPay", office.getConfirmPay());		
		}
		
		model.addAttribute("page", pageInfo);
		model.addAttribute("userJobs", userJobList);
		model.addAttribute("reviewVO", vo);
		model.addAttribute("userList", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		return returnStr;
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-05-12
	 * 审核借款申请信息
	 */
	@ResponseBody
	@RequestMapping(value ="reviewBorrowing")
	public Map<String, Object> reviewBorrowing(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,long roleId,Integer result,String denyReason,Integer userLevel){
		
		Map<String, Object> map =new HashMap<String,Object>();
		try {
			map = orderReviewService.reviewBorrowing(rid, roleId, result, denyReason, userLevel, request);
		} catch (Exception e) {
			map.put("flag", 0);
			map.put("message", e.getMessage());
			return map;
		}
		return map;
	}
	/**
	 * 20150810 批量审核借款申请信息
	 * @author gao
	*  2015年8月11日
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="batchReviewBorrowing")
	public Map<String,Object> batchReviewBorrowing(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String userLevel = request.getParameter("userLevel"); // 当前用户审核等级
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		int num = 0;
		Map<String, Object> back =new HashMap<String,Object>();
		if (StringUtils.isBlank(result)) {
			back.put("res", "error");
			back.put("msg", "审批结果不能为空");
			return back;
		}
		for (int i = 0; i < levelandrevids.length; i++) {
			String revid = levelandrevids[i].split("@")[1];
			
			try {
				if("1".equals(result)){
					orderReviewService.reviewBorrowing(Long.valueOf(revid), Long.valueOf(0), Integer.valueOf(result), "", Integer.valueOf(userLevel), request);
				}else{
					orderReviewService.reviewBorrowing(Long.valueOf(revid), Long.valueOf(0), Integer.valueOf(result), remarks, Integer.valueOf(userLevel), request);
				}
				ReviewLog reviewLog = new ReviewLog();
				reviewLog.setReviewId(Long.valueOf(revid));
				reviewLog.setNowLevel(Integer.valueOf(userLevel));
				reviewLog.setResult(result);
				reviewLog.setRemark(remarks);
				reviewLogService.saveReviewLog(reviewLog);
				num++;
			} catch (Exception e) {
				logger.error("批量审核借款申请: 审核ID为："+revid+"；  报错原因："+e.getMessage());
			}
		}
		back.put("res", "success");
		back.put("msg", "共有"+num+"条记录操作成功。");
		return back;
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2015-05-12
	 * 查看借款申请信息
	 */
	@RequestMapping(value ="viewBorrowingReviewInfo")
	public String viewBorrowingReviewInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel){
		String reviewId = rid+"";
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		
		List<ReviewDetail> rdlist = new ArrayList<ReviewDetail>();
		try{
			if(reviewId!=null){
				rdlist= reviewService.queryReviewDetailList(reviewId);
			}
		}catch(Exception e){
			logger.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
		List<ReviewDetail> borrowPricesList = new ArrayList<ReviewDetail>();
		if(rdlist!=null&&rdlist.size()>0){
			for(int i = 0;i<rdlist.size();i++){
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
				if("currencyMarks".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
			}
		}
		
		List<BorrowingBean> blist = BorrowingBean.transferReviewDetail2BorrowingBean(rdlist);
		List<BorrowingBean> tralist = new ArrayList<BorrowingBean>();
		List<BorrowingBean> teamlist= new ArrayList<BorrowingBean>();
		List<BorrowingBean> borrowList = BorrowingBean.transferReviewDetail2BorrowingBean(borrowPricesList);
		//将blist 拆分为团队借款和游客借款两部分
		if(blist!=null&&blist.size()>0){
			int size = blist.size();
			model.addAttribute("applyDate", blist.get(0).getApplyDate());
			for(int i = 0;i<size;i++){
				BorrowingBean bean = blist.get(i);
				if("0".equals(bean.getTravelerId())){//团队借款
					teamlist.add(bean);
				}else{//游客借款
					tralist.add(bean);
				}
			}
			
		}
		
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLog> rLog=reviewService.findReviewLog(Long.parseLong(reviewId));
			model.addAttribute("rLog",rLog);
	    }
		
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist",teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("orderId", orderId);
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("productType", productOrder.getOrderStatus());
		return "modules/review/viewBorrowingReviewInfo";
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-05-12
	 * 审核借款申请信息
	 */
	@RequestMapping(value ="reviewBorrowingInfo")
	public String reviewBorrowingInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,String orderId,long roleId,Integer userLevel,String orderType){
		String reviewId = rid+"";
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		
		List<ReviewDetail> rdlist = new ArrayList<ReviewDetail>();
		try{
			if(reviewId!=null){
				rdlist= reviewService.queryReviewDetailList(reviewId);
			}
		}catch(Exception e){
			logger.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
		List<ReviewDetail> borrowPricesList = new ArrayList<ReviewDetail>();
		if(rdlist!=null&&rdlist.size()>0){
			for(int i = 0;i<rdlist.size();i++){
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
				if("currencyMarks".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
			}
		}
		
		List<BorrowingBean> blist = BorrowingBean.transferReviewDetail2BorrowingBean(rdlist);
		List<BorrowingBean> tralist = new ArrayList<BorrowingBean>();
		List<BorrowingBean> teamlist= new ArrayList<BorrowingBean>();
		List<BorrowingBean> borrowList = BorrowingBean.transferReviewDetail2BorrowingBean(borrowPricesList);
		//将blist 拆分为团队借款和游客借款两部分
		if(blist!=null&&blist.size()>0){
			int size = blist.size();
			model.addAttribute("applyDate", blist.get(0).getApplyDate());
			for(int i = 0;i<size;i++){
				BorrowingBean bean = blist.get(i);
				if("0".equals(bean.getTravelerId())){//团队借款
					teamlist.add(bean);
				}else{//游客借款
					tralist.add(bean);
				}
			}
			
		}
		
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLog> rLog=reviewService.findReviewLog(Long.parseLong(reviewId));
			model.addAttribute("rLog",rLog);
	    }
		
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist",teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("orderId", orderId);
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("rid",rid);
		model.addAttribute("roleId",roleId);
		model.addAttribute("userLevel",userLevel);
		model.addAttribute("orderType", productOrder.getOrderStatus());
		return "modules/review/reviewBorrowingInfo";
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-07-10
	 * 撤销审核
	 */
	@ResponseBody
	@RequestMapping(value ="backOutReview")
	public Map<String, Object> backOutReview(HttpServletResponse response,
	        Model model, HttpServletRequest request,long rid,Integer userLevel){
		
		Map<String, Object> map =new HashMap<String,Object>();
		StringBuffer sbf = new StringBuffer();
		String denyReason = "撤消审核";
		try {
			int sign = reviewService.backOutReview(rid, userLevel, denyReason, sbf);
			map.put("result", sign);
			map.put("message", sbf.toString());
		} catch (Exception e) {
			map.put("result", 0);
			map.put("message", e.getMessage());
			return map;
		}
		return map;
	}
}
