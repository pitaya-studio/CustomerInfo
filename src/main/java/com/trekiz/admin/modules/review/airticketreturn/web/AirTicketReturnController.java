package com.trekiz.admin.modules.review.airticketreturn.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.airticketreturn.entity.AirticketReturn;
import com.trekiz.admin.modules.review.airticketreturn.service.IAirTicketReturnService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;

/**
 * 机票退票处理
 * 
 * @author chy
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/airticketreturn")
public class AirTicketReturnController {

//	private static final Log log = LogFactory
//			.getLog(AirTicketReturnController.class);

	@Autowired
	private IAirTicketReturnService airTicketReturnService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewCommonService reviewCommonService;

	@Autowired
	private TravelerService travelerService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;

	/**
	 * 根据订单id查询退票详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "airticketReturnDetail")
	public String queryOrderDetail(Model model, HttpServletRequest request,
			HttpServletResponse response) {

		String orderId = request.getParameter("orderId");
		Map<String, Object> airticketReturnDetailInfoMap = airTicketReturnService
				.queryAirTicketReturnInfoById(orderId);
		@SuppressWarnings({"unchecked"})
		List<Map<String, Object>> travelInfoList = (List<Map<String, Object>>) airticketReturnDetailInfoMap
				.get("travelInfoList");
		// 新加了多币种处理 add by chy 2014年11月28日16:02:43
		String srcmoney = "";
		if(travelInfoList == null){
			travelInfoList = new ArrayList<Map<String, Object>>();
		}
		for (Map<String, Object> temp : travelInfoList) {
			String srcPrice = temp.get("payPrice") == null ? null : temp.get(
					"payPrice").toString();
			srcmoney = moneyAmountService.getMoney(srcPrice);
			temp.remove("payPrice");
			temp.put("payPrice", srcmoney);
		}
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		airticketReturnDetailInfoMap.remove("travelInfoList");
		airticketReturnDetailInfoMap.put("travelInfoList", travelInfoList);
		model.addAttribute("airticketReturnDetailInfoMap",
				airticketReturnDetailInfoMap);
		return "modules/airticketreturn/airticketReturnDetail";
	}

	/**
	 * 机票退票审批详情页 add by chy 2014年12月2日15:37:34
	 */
	@RequestMapping(value = "airticketReturnReviewDetail")
	public String queryReviewOrderDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String traveleriId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String flag = request.getParameter("flag");
		Map<String, Object> airticketReturnDetailInfoMap = airTicketReturnService
				.queryAirTicketReturnInfoById(orderId);
		Page<Map<String, Object>> page = airTicketReturnService
				.queryAirticketReturnReviewInfo(request, response);
		List<Map<String, Object>> list = page.getList();
		for (Map<String, Object> tMap : list) {
			String strOrderId = tMap.get("orderid") == null ? null : tMap.get(
					"orderid").toString();
			String strTId = tMap.get("tid") == null ? null : tMap.get("tid")
					.toString();
			String strRevid = tMap.get("revid") == null ? null : tMap.get(
					"revid").toString();
			if (orderId.equals(strOrderId) && traveleriId.equals(strTId)
					&& revid.equals(strRevid)) {
				// airticketReturnDetailInfoMap.put("reviewTraveler", tMap);
				model.addAttribute("reviewTraveler", tMap);
				break;
			}
		}
		model.addAttribute("rid",revid);
		model.addAttribute("flag", flag);
		model.addAttribute("airticketReturnDetailInfoMap",
				airticketReturnDetailInfoMap);
		return "modules/airticketreturn/airticketReturnReviewDetail";
	}
	/**
	 * 根据productType, flowType, orderId, travelerId取消游客退票申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "cancelTravlerReturnRequest")
	public String cancelTravlerReturnRequest(Model model,
			HttpServletRequest request, HttpServletResponse response) {
//		String productType = request.getParameter("orderType");
//		String flowType = request.getParameter("flowType");
//		String orderId = request.getParameter("orderId");
//		String travelerId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		reviewService.removeReview(Long.parseLong(revid));//(Integer.parseInt(productType),Integer.parseInt(flowType), orderId,Long.parseLong(travelerId));
		// 重新查询退票游客信息审核列表
		List<AirticketReturn> airticketReturnList = getAirticketReturnReviewList(
				request, response);
		model.addAttribute("airticketReturnList", airticketReturnList);
		return queryTicketReturnList(model, request, response);//"modules/airticketreturn/airticketReturnReviewList";
	}

	/**
	 * 机票退票审批列表页 add by chy 2014年12月1日16:11:49
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "airticketReturnReviewList")
	public String queryTicketReturnReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {

		Page<Map<String, Object>> page = airTicketReturnService.queryAirticketReturnReviewInfo(request, response);
		//查询退票流程的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		if((userJobId == null || "userJobId".equals(userJobId)) && userJobs.size() > 0){//如果roleId为空的  则说明前台没有传递这个参数 取所有roles的第一个的Id
			conditionsMap.put("userJobId", userJobs.get(userJobs.size()-1).getId());
		}
		//排序
	    Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
		model.addAttribute("userJobs", userJobs);//当前用户的职位
		model.addAttribute("page", page);
		model.addAttribute("conditionsMap", conditionsMap);
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		
		return "modules/airticketreturn/airticketReturnReviewList";
	}

	private Map<String, Object> prepareQueryCond(HttpServletRequest request) {
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("orderType", request.getParameter("orderType"));
		conditionsMap.put("groupCode", request.getParameter("groupCode"));
		String statusChoose = request.getParameter("statusChoose");
		if(statusChoose == null){
			statusChoose = "1";
		}
		conditionsMap.put("statusChoose", statusChoose);
//		conditionsMap.put("statusChoose", request.getParameter("statusChoose"));
		// conditionsMap.put("flowType", request.getParameter("flowType"));
		conditionsMap.put("channel", request.getParameter("channel") == null
				|| "".equals(request.getParameter("channel").trim())
				? null
				: Integer.parseInt(request.getParameter("channel")));
		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler == null || "".equals(saler.trim())
				? null
				: Integer.parseInt(saler));
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter == null || "".equals(meter.trim())
				? null
				: Integer.parseInt(meter));
		String truesaler = request.getParameter("truesaler");
		conditionsMap.put("truesaler", truesaler);
		// conditionsMap.put("active", request.getParameter("active"));
		conditionsMap.put("startTime", request.getParameter("startTime"));
		conditionsMap.put("endTime", request.getParameter("endTime"));
		conditionsMap.put("orderBy", request.getParameter("orderBy"));
		String userJobIdStr = request.getParameter("userJobId");//选择的角色
		Long userJobIdNum = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJobIdNum = Long.parseLong(userJobIdStr);
		}
		conditionsMap.put("userJobId", userJobIdNum);
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");//订单创建日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");//订单更新日期排序标识
		conditionsMap.put("orderCreateDateSort", orderCreateDateSort);
		conditionsMap.put("orderUpdateDateSort", orderUpdateDateSort);
		conditionsMap.put("orderCreateDateCss", orderCreateDateCss);
		conditionsMap.put("orderUpdateDateCss", orderUpdateDateCss);
		return conditionsMap;
	}

	/**
	 * 根据订单id 业务类型orderType 流程类型 flowType active： 是否只查询 active=1的记录 查询退票审核列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "airticketReturnList")
	public String queryTicketReturnList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		List<AirticketReturn> airticketReturnList = getAirticketReturnReviewList(
				request, response);
		model.addAttribute("orderId", request.getParameter("orderId"));
		model.addAttribute("airticketReturnList", airticketReturnList);
		return "modules/airticketreturn/airticketReturnList";
	}

	private List<AirticketReturn> getAirticketReturnReviewList(
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String orderType = request.getParameter("orderType");
		String flowType = request.getParameter("flowType");
		Boolean active = false;//Boolean.valueOf("0".equals(request.getParameter("active"))|| "false".equals(request.getParameter("active")) ? false : true);

		List<Review> airticketReturnReviewList = reviewService.findReview(
				Integer.parseInt(orderType), Integer.parseInt(flowType),
				orderId, active);
		Map<String, Object> airticketReturnDetailInfoMap = airTicketReturnService
				.queryAirTicketReturnInfoById(orderId);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> travelInfoList = (List<Map<String, Object>>) airticketReturnDetailInfoMap
				.get("travelInfoList");
		List<AirticketReturn> airticketReturnList = new ArrayList<AirticketReturn>();
		if (airticketReturnReviewList != null// 如果审核记录为空或者size为0则无记录 不走下面的操作
				&& airticketReturnReviewList.size() != 0) {
			for (Review r : airticketReturnReviewList) {
				AirticketReturn temp = new AirticketReturn();
				temp.setId(r.getId());
				temp.setCpid(r.getReviewCompanyId());
				temp.setTopLevel(r.getTopLevel());
				temp.setNowLevel(r.getNowLevel());
				temp.setProductType(r.getProductType());
				temp.setOrderId(Long.valueOf(r.getOrderId()));
				temp.setFlowType(r.getFlowType());
				temp.setTravelerId(r.getTravelerId());
				temp.setCreateReason(r.getCreateReason());
				temp.setDenyReason(r.getDenyReason());
				temp.setCreateBy(r.getCreateBy());
				temp.setCreateDate(r.getCreateDate());
				temp.setUpdateDate(r.getUpdateDate());
				temp.setUpdateByName(r.getUpdateByName());
				temp.setStatus(r.getStatus());
				temp.setActive(r.getActive());
				Traveler travelerTemp = travelerService.findTravelerById(r
						.getTravelerId());
				temp.setOrderCreateDate((Date) airticketReturnDetailInfoMap
						.get("create_date"));
				// 新加了多币种处理 add by chy 2014年11月28日16:02:43
				String srcPrice = moneyAmountService.getMoney(travelerTemp
						.getPayPriceSerialNum());
				temp.setPayPrice(srcPrice);//
				if (travelInfoList == null || travelInfoList.size() != 0) {
					for (Map<String, Object> traveler : travelInfoList) {
						if (Long.parseLong(traveler.get("id").toString()) == r
								.getTravelerId()) {
							traveler.put("reviewId", temp.getId());
						}
					}
				}
				airticketReturnList.add(temp);
			}
			// 移除travelInfoList
			airticketReturnDetailInfoMap.remove("travelInfoList");
			// 重新put travelInfoList
			airticketReturnDetailInfoMap.put("travelInfoList", travelInfoList);
		}
		return airticketReturnList;
	}

	/**
	 * 提交退票申请
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "submitAirticketReturnReq")
	public String submitAirticketReturnReq(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		// 退票原因数组
		String[] reasons = request.getParameterValues("returnReason");
		// 旅客id数组
		String[] tIds = request.getParameterValues("travelerId");
		// checkbox数组
		String activityIds = request.getParameter("activityIds");
		// orderId
		String orderId = request.getParameter("orderId");
		// reviewId数组
		String[] reviewIds = request.getParameterValues("reviewId");
		// productType产品类型
		Integer productType = Integer.parseInt(request
				.getParameter("productType"));
		// flowType流程类型 (退款，改签...)
		Integer flowType = Integer.parseInt(request.getParameter("flowType"));
		// reply 返回错误信息记录
		StringBuffer reply = new StringBuffer();
		String[] temps = activityIds.split(",");
		Long airticketId = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId)).getAirticketId();
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
		for (int i = 0; i < temps.length; i++) {
			if (temps[i] == null || "".equals(temps[i])) {
				continue;
			}
			if ("1".equals(temps[i])) {
				// 组织review数据
				Long travelerId = Long.parseLong(tIds[i]);
				Long reviewId = Long.parseLong(reviewIds[i]);
				String createReason = reasons[i];
				// 声明数组detail
				List<Detail> listDetail = new ArrayList<Detail>();
				// 构建detail
				Detail detail = new Detail();
				detail.setKey("createReason");
				detail.setValue(createReason);
				// 把detail添加到list中
				listDetail.add(detail);
				// 调用reviewService添加review记录
				reviewService.addReview(productType, flowType, orderId,
						travelerId, reviewId, createReason, reply, listDetail, deptId);
			}
			if (!"".equals(reply.toString())) {
				model.addAttribute("reply", reply);
				return queryOrderDetail(model, request, response);
			}
		}
		//return queryOrderDetail(model, request, response);// "modules/airticketreturn/airticketReturnDetail";
		return "redirect:"+Global.getAdminPath()+"/airticketreturn/airticketReturnList?orderId=" + orderId + "&flowType=3&orderType=7";
	}

	/**
	 * 审核 驳回 或通过
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "reviewAirticketReturn")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String reviewAirticketReturn(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		StringBuffer reply = new StringBuffer();
		String revid = request.getParameter("revid");
		if (revid == null || "".equals(revid)) {
			reply.append("审批id不能为空");
		}
		String nowLevel = request.getParameter("nowlevel");
		if (nowLevel == null || "".equals(nowLevel)) {
			reply.append("审核 层级不能为空");
		}
		String denyReason = request.getParameter("denyReason");
		String result = request.getParameter("result");
		if (result == null || "".equals(result)) {
			reply.append("审批结果不能为空");
		}
		if (reply != null && !"".equals(reply.toString())) {
			model.addAttribute("reply", reply);
			return queryReviewOrderDetail(model, request, response);
		}
		int num = reviewService.UpdateReview(Long.parseLong(revid),
				Integer.parseInt(nowLevel), Integer.parseInt(result),
				denyReason);
		if(num == 1 && Integer.parseInt(result) == 1){//num == 1 代表退票审批通过
			//还余位
			airTicketReturnService.returnPosition(request.getParameter("orderId"), request.getParameter("travelerId"));
		}
//		return queryTicketReturnReviewList(model, request, response);
		return "redirect:"+Global.getAdminPath()+"/airticketreturn/airticketReturnReviewList";
	}

	/**
	 * 审核 驳回 或通过
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchreturnReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String batchReviewAirticketReturn(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String revIds = request.getParameter("revIds");//审核表ids
		String remark = request.getParameter("remark");//通过/驳回原因
		String strResult = request.getParameter("result");//通过/驳回
		if("2".equals(strResult)){
			strResult = "1";
		}else {
			strResult = "0";
		}
		String[] revidArr = revIds.split(",");
		for(String revid : revidArr){
			if(revid == null || "".equals(revid)){
				System.err.println("错误的参数reviewid不能为空 airticketRefundReviewContriller line 718");
				continue;
			}
			Map<String, String> review = reviewService.findReview(Long.parseLong(revid));
			int num = reviewService.UpdateReview(Long.parseLong(revid),
					Integer.parseInt(review.get("curLevel")), Integer.parseInt(strResult),
					remark);
			if(num == 1 && Integer.parseInt(strResult) == 1){//num == 1 代表退票审批通过
				//还余位
				airTicketReturnService.returnPosition(review.get("orderId"), review.get("travelerId"));
			}
		}
		return "success";
	}
}
