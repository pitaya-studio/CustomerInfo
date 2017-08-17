package com.trekiz.admin.modules.review.changeprice.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.review.changeprice.service.IChangePriceReviewService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**
 * 改价审批列表页
 * 
 * @author chy
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/changePrice")
public class ChangePriceReviewController {

//	private static final Log log = LogFactory
//			.getLog(ChangePriceReviewController.class);

	@Autowired
	private IChangePriceReviewService changePriceReviewService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private OrderContactsDao orderContactsDao;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
    private VisaProductsService visaProductsService;
	
	@Autowired
	private DictService dictService;

	/**
	 * 查询改价审批列表
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return+3
	 */
	@RequestMapping(value = "changePriceReviewList")
	public String queryRefundReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Page<Map<String, Object>> refundReviewList = changePriceReviewService
				.queryRefundReviewList(request, response);
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		if(refundReviewList == null){
			model.addAttribute("conditionsMap", conditionsMap);
			return "modules/changeprice/reviewChangePriceList";
		}
		List<UserJob> userJobsAll = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_CHANGE_PRICE);
		List<UserJob> userJobs = new ArrayList<UserJob>();
		String headPrd = request.getParameter("headPrd");
		if(headPrd == null){
			headPrd = "1";
		}
		for(UserJob temp : userJobsAll){
			if(temp.getOrderType() == Integer.parseInt(headPrd)){
				userJobs.add(temp);
			}
		}
		String userJobId = request.getParameter("userJobId");
		if((userJobId == null || "".equals(userJobId)) && userJobs.size() > 0){//如果userJobs为空的  则说明前台没有传递这个参数 取所有userJobs的第一个的Id
			UserJob userJob = userJobs.get(userJobs.size()-1);
			conditionsMap.put("userJobId", userJob.getId());
		}
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
		model.addAttribute("page", refundReviewList);
		model.addAttribute("conditionsMap", conditionsMap);
		
		return "modules/changeprice/reviewChangePriceList";
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
		//产品类型 三级菜单上的
		String headPrd = request.getParameter("headPrd");
		if(headPrd == null){
			headPrd = "1";
		}
		conditionsMap.put("headPrd", headPrd);
		conditionsMap.put("channel", request.getParameter("channel"));
		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler);
		String truesaler = request.getParameter("truesaler");
		conditionsMap.put("truesaler", truesaler);
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter);
		conditionsMap.put("startTime", request.getParameter("startTime"));
		conditionsMap.put("endTime", request.getParameter("endTime"));
		conditionsMap.put("orderBy", request.getParameter("orderBy"));
		String userJobIdStr = request.getParameter("userJobId");
		Long userJobId = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJobId = Long.parseLong(userJobIdStr);
		}
		conditionsMap.put("userJobId", userJobId);
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
	 * 进入改价审批详情页
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "changePriceReviewDetail")
	public String queryRefundReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderid");// 订单id
		String reviewId = request.getParameter("revid");// 审核表id
		String nowlevel = request.getParameter("nowlevel");
		// 查询审批详情信息
		// 产品类型 单办 还是参团 所查询的信息是不同的
		String prdType = request.getParameter("prdType");
		if (prdType == null || "".equals(prdType.trim())) {
			return null;
		}
		if ("7".equals(prdType.trim())) {// 7代表机票 相当于单办 查询单办信息 
			// 由于这个内容和申请改价一致 所以调用了申请改价的这个查询
			Map<String, Object> orderDetail = changePriceReviewService.queryAirticketorderDeatail(orderId, prdType);
			//处理多币种信息 start
			String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			orderDetail.remove("totalmoney");
			orderDetail.put("totalmoney", totalMoney);
			//处理多币种信息 end
			model.addAttribute("orderDetail", orderDetail);
			
		} else if ("6".equals(prdType.trim())) {// 6代表签证 查询签证信息
			Map<String, Object> orderDetail = changePriceReviewService.queryVisaorderDeatail(orderId);
			// 处理多币种信息 start
			String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			orderDetail.remove("totalmoney");
			orderDetail.put("totalmoney", totalMoney);
//			String visapay = orderDetail.get("visapay") == null ? null : orderDetail.get("visapay").toString();
//			visapay = moneyAmountService.getMoney(visapay);
//			orderDetail.remove("visapay");
//			orderDetail.put("visapay", visapay);
			//产品相关信息
			VisaProducts visaProduct = visaProductsService.findByVisaProductsId(Long.parseLong(orderDetail.get("visaproductid").toString()));
			Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
			Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
			model.addAttribute("visaProduct", visaProduct);
			model.addAttribute("visaType", visaType);
			model.addAttribute("country", country);
			//处理多币种 end
			model.addAttribute("orderDetail", orderDetail);
		} else if ("2".equals(prdType.trim())){// 2代表 散拼 10 游轮
			Map<String, Object> orderDetail = changePriceReviewService.querySanPinReviewOrderDetail(orderId);
			// 处理多币种信息 start
			String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			orderDetail.remove("totalmoney");
			orderDetail.put("totalmoney", totalMoney);
			// 处理多币种信息 end
			// 处理目的地信息 start
			List<Map<String, Object>> targetArea = orderDetail.get("targetAreas") == null ? null : (List<Map<String, Object>>)orderDetail.get("targetAreas");
			if (targetArea != null && targetArea.size()!=0) {
				String areaString = "";
				int tempN = 0;
				for (Map<String, Object> tempS : targetArea) {
					if (tempN != 0) {
						areaString += ",";
					}
					areaString += AreaUtil.findAreaNameById(Long.parseLong(tempS.get("targetAreaId").toString()));
					tempN++;
				}
				orderDetail.remove("targetarea");
				orderDetail.put("targetarea", areaString);
			}
			// 处理目的地信息 end
			model.addAttribute("orderDetail", orderDetail);
		} else {// 查询参团信息 1、3、4、5
			Map<String, Object> grouporderDeatail = changePriceReviewService
					.queryGrouporderDeatail(orderId);
			// 处理多币种信息
			String totalMoney = grouporderDeatail.get("totalmoney") == null ? null : grouporderDeatail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			grouporderDeatail.remove("totalmoney");
			grouporderDeatail.put("totalmoney", totalMoney);
			// 处理targetArea 目标城市 是数组
			List<Map<String, Object>> targetArea = grouporderDeatail.get("targetAreas") == null ? null : (List<Map<String, Object>>)grouporderDeatail.get("targetAreas");
			if (targetArea != null && targetArea.size()!=0) {
				String areaString = "";
				int tempN = 0;
				for (Map<String, Object> tempS : targetArea) {
					if (tempN != 0) {
						areaString += ",";
					}
					areaString += AreaUtil.findAreaNameById(Long.parseLong(tempS.get("targetAreaId").toString()));
					tempN++;
				}
				grouporderDeatail.remove("targetarea");
				grouporderDeatail.put("targetarea", areaString);
			}
			//处理targetArea end
			model.addAttribute("orderDetail", grouporderDeatail);
		}
		//查询订单联系人信息
		List<OrderContacts> orderContacts = orderContactsDao.findOrderContactsByOrderIdAndOrderType(Long.parseLong(orderId), Integer.parseInt(prdType.trim()));
		model.addAttribute("orderContacts", orderContacts);
		// 查询改价信息
		Map<String, Object> review = reviewService.findReviewObject(Long
				.parseLong(reviewId));
		model.addAttribute("flag", request.getParameter("flag"));
		model.addAttribute("reviewdetail", review);
		model.addAttribute("nowlevel", nowlevel);
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("rid",reviewId);
		return "modules/changeprice/reviewChangePriceDetail";
	}
	
	/**
	 * 改价审核的审核通过或驳回
	 */
	@RequestMapping(value = "changePriceReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String refundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		// 1 组织参数
		String revId = request.getParameter("revId");//审核表id
		String curLevel = request.getParameter("nowlevel");//当前层级
		String strResult = request.getParameter("result");//审批结果 1 通过 0 驳回
		String denyReason = request.getParameter("denyReason");//驳回原因
		String amount = request.getParameter("moneyAmount");//改价数额
		String orderType = request.getParameter("orderTypeSub");//产品类型
		String orderId = request.getParameter("orderId");//订单id
		String currencyId = request.getParameter("currencyId");//币种ids
		String travelerId = request.getParameter("travelerId");//游客id
		// 2 调用审核接口处理
		int lastLevelFlagNum = reviewService.UpdateReview(Long.parseLong(revId), Integer.parseInt(curLevel), Integer.parseInt(strResult), denyReason);
		// 3如果审核通过并且当前层级为最高层级 则更改对应业务数据
//		List<Review> list = reviewDao.findReviewActive(Long.parseLong(revId));
//		if(Integer.parseInt(strResult) == 1 && list.get(0).getTopLevel() == Integer.parseInt(curLevel)){
		if(lastLevelFlagNum == 1){
			Map<String, String> params = new HashMap<String, String>();
			params.put("travelerId", travelerId);
			params.put("orderId", orderId);
			params.put("orderType", orderType);
			params.put("currencyId", currencyId);
			params.put("amount", amount);
			changePriceReviewService.doChangePrice(params);
		}
		return "redirect:"+Global.getAdminPath()+"/changePrice/changePriceReviewList";
	}
	
	/**
	 * 改价审核的批量审核通过或驳回
	 */
	@ResponseBody
	@RequestMapping(value = "batchchangePriceReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Map<String,Object> batchChangePriceReview(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		// 1 组织参数
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
				System.err.println("错误的参数reviewid不能为空 changepriceReviewController line 329");
				map.put("res", "error");
				map.put("message", "错误的参数,审核ID为空");
				continue;
			}
			Map<String, String> review = reviewService.findReview(Long.parseLong(revid));
			// 2 调用审核接口处理
			int lastLevelFlagNum = reviewService.UpdateReview(Long.parseLong(revid), Integer.parseInt(review.get("curLevel")), Integer.parseInt(strResult), remark);
			// 3如果审核通过并且当前层级为最高层级 则更改对应业务数据
	//		List<Review> list = reviewDao.findReviewActive(Long.parseLong(revId));
	//		if(Integer.parseInt(strResult) == 1 && list.get(0).getTopLevel() == Integer.parseInt(curLevel)){
			if(lastLevelFlagNum == 1){
				Map<String, String> params = new HashMap<String, String>();
				params.put("travelerId", review.get("travelerid"));
				params.put("orderId", review.get("orderId"));
				params.put("orderType", review.get("productType"));
				params.put("currencyId", review.get("currencyid"));
				params.put("amount", review.get("changedprice"));
				boolean back = changePriceReviewService.doChangePrice(params);
				if(back){
					map.put("res", "success"); // 改价成功( 1：审核成功时，当前审核是最后一层)
				}else{
					map.put("res", "error"); // 改价失败
					map.put("message", "改价失败，请稍候再试");
				}
			}else if(lastLevelFlagNum == 0){
				map.put("res", "success"); // 改价成功(返回 0：审核成功时，当前审核不是最后一层)
			}else{
				map.put("res", "error"); // 改价失败
				map.put("message", "不是待审核的记录，不需要审核");
			}
		}
		map.put("res", "error"); // 改价失败
		map.put("message", "改价失败，请检查输入参数");
		return map;
	}
	
	/**
	 * 改价审核-撤销
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "revokeChangePrice")
	public void revokeChangePrice(Model model, HttpServletRequest request, HttpServletResponse response){
		String json = "{\"flag\":\"success\"}";
		String revId = request.getParameter("reviewId");      //审核表id
		String curLevel = request.getParameter("myCheckLevel");//当前层级
		if(StringUtils.isNotBlank(curLevel) && StringUtils.isNotBlank(revId)){
			Integer currentLevel = Integer.parseInt(curLevel);
			Long reviewId = Long.valueOf(revId);
			if(currentLevel > 0){
				StringBuffer str = new StringBuffer();
				int index = reviewService.backOutReview(reviewId, currentLevel, "", str);
				if(index != 1 && str.toString().length() != 0){
					json = "{\"flag\":\"fail\",\"msg\":\""+str.toString()+"\"}";
				}
			}else{
				json = "{\"flag\":\"fail\",\"msg\":\"当前层级不能进行撤销操作\"}";
			}
		}else{
			json = "{\"flag\":\"fail\",\"msg\":\"审核ID或当前层级不能为空\"}";
		}
		ServletUtil.print(response, json);
	}
}
