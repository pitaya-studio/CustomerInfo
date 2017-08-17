package com.trekiz.admin.modules.review.refundreview.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.refundreview.service.IAirTicketRefundReviewService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

import freemarker.template.TemplateException;

/**
 * 退款审批列表页
 * 
 * @author chy
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/refundReview")
public class AirTicketRefundReviewController {

	@Autowired
	private IAirTicketRefundReviewService airTicketRefundReviewService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private MoneyAmountService moneyAmountService;

	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
    private VisaProductsService visaProductsService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	/**
	 * 查询退款审批列表
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "refundReviewList")
	public String queryRefundReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Page<Map<String, Object>> refundReviewList = airTicketRefundReviewService
				.queryRefundReviewList(request, response);
		List<Integer> flowTypes = new ArrayList<Integer>();
		flowTypes.add(Context.REVIEW_FLOWTYPE_REFUND);
		flowTypes.add(Context.REVIEW_FLOWTYPE_OPER_REFUND);
		List<UserJob> userJobsAll = reviewCommonService.getWorkFlowJobByFlowType(flowTypes);
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
		// 获取查询参数
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		if((userJobId == null || "userJobId".equals(userJobId)) && userJobs.size() > 0){//如果userJobs为空的  则说明前台没有传递这个参数 取所有userJobs的第一个的Id
			conditionsMap.put("userJobId", userJobs.get(userJobs.size()-1).getId());
			userJobId = userJobs.get(userJobs.size()-1).getId().toString();
			UserJob userJob = userJobs.get(userJobs.size()-1);
			List<Integer> jobs = reviewService.getJobLevel(userJob.getDeptId(),userJob.getJobId(),
					Context.REVIEW_FLOWTYPE_REFUND);
			if(jobs != null && jobs.size() > 0){//新加判空 by chy 2015年8月3日18:19:10
				conditionsMap.put("myCheckLevel", jobs.get(0));
			}
		}else{
			for (UserJob userJob:userJobs) {
				if(userJob.getId().intValue() == 
						Long.valueOf(String.valueOf(conditionsMap.get("userJobId"))).intValue()){
					List<Integer> jobs = reviewService.getJobLevel(userJob.getDeptId(),userJob.getJobId(),
							Context.REVIEW_FLOWTYPE_REFUND);
					if(jobs != null && jobs.size() > 0){//新加判空 by chy 2015年8月3日18:19:10
						conditionsMap.put("myCheckLevel", jobs.get(0));
					}
					break;
				}
			}
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
		UserJob curUserJob = new UserJob();
		for(UserJob userJob : userJobs){
			if(userJobId == null || "".equals(userJobId)){
				continue;
			}
			if(userJob.getId() == Long.parseLong(userJobId)){
				curUserJob = userJob;
			}
		}
		model.addAttribute("isConfirm", UserUtils.getUser().getCompany().getConfirmPay());//代表是否需要出纳确认
		model.addAttribute("userJob", curUserJob);
		model.addAttribute("page", refundReviewList);
		model.addAttribute("conditionsMap", conditionsMap);
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		
		return "modules/refundreview/refundReviewList";
	}
	
	
	/**
	 * 
	 */
	@RequestMapping(value="cashConfirm")
	public String cashConfirm(Model model,HttpServletRequest request, HttpServletResponse response){
		Long rId = request.getParameter("rId") == null ? null : Long.parseLong(request.getParameter("rId"));
		if(rId == null){
			return "error";
		}
		reviewService.cashConfirm(rId, "cashconfirm", "1");
		return "success";
	}
	
   /**
    * 退款单详情
    * */
	@RequestMapping(value="refundReviewInfo")
	public String printRefundReviewInfo(Model model,HttpServletRequest request, HttpServletResponse response){
		
	    Map<String,String> refundInfo = productOrderService.findProductOrderById(request);
		model.addAttribute("refundInfo", refundInfo);
		return "modules/refundreview/refundReviewInfo";
	}
	
	/**
	 * 更新打印标志
	 * @param request
	 * @return
	 */
	@RequestMapping(value="updatePrint")
	public String updatePrint(HttpServletRequest request, HttpServletResponse response) throws Exception{
		 Review review = reviewService.findReviewInfo(Long.parseLong(request.getParameter("reviewId")));
		 if(review.getPrintFlag() == null || review.getPrintFlag() == 0){
			 Date date = new Date();
			 review.setPrintFlag(1);//第一次打印更改标志
			 review.setPrintTime(date);//设置打印时间
			 review.setUpdateDate(date);
			 review.setUpdateBy(UserUtils.getUser().getId());
		 }
		reviewService.updateRivew(review);
		return "success";
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="downloadList")
	public ResponseEntity<byte[]> downloadList(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{

		File file = airTicketRefundReviewService.createDownloadFile(request);
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "退款单" + nowDate + ".doc" ;
		OutputStream os = null;
    	try {
			if(file != null && file.exists()){
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
	            os.flush();
			}       		
    		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(os!=null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
		return null;
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
		// conditionsMap.put("flowType", request.getParameter("flowType"));
		conditionsMap.put("channel", request.getParameter("channel"));
		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler);
		String truesaler = request.getParameter("truesaler");
		conditionsMap.put("truesaler", truesaler);
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter);
		// conditionsMap.put("active", request.getParameter("active"));
		conditionsMap.put("startTime", request.getParameter("startTime"));
		conditionsMap.put("endTime", request.getParameter("endTime"));
		conditionsMap.put("orderBy", request.getParameter("orderBy"));
		String userJobIdStr = request.getParameter("userJobId");//职位
		Long userJobId = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJobId = Long.parseLong(userJobIdStr);
		}
		conditionsMap.put("userJobId", userJobId);
		String refundPriceStart = request.getParameter("refundPriceStart");// 金额开始范围
		conditionsMap.put("refundPriceStart", refundPriceStart);
		String refundPriceEnd = request.getParameter("refundPriceEnd");// 金额结束范围
		conditionsMap.put("refundPriceEnd", refundPriceEnd);
		String printFlag = request.getParameter("printFlag");// 打印标志
		conditionsMap.put("printFlag", printFlag);
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
	 * 进入退款审批详情页
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "refundReviewDetail")
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
		if ("7".equals(prdType.trim()) || "8".equals(prdType.trim())) {// 7代表机票 相当于单办 查询单办信息 8 代表机票切位
			// 由于这个内容和申请退款一致 所以调用了申请退款的这个查询
			Map<String, Object> orderDetail = airTicketRefundReviewService.queryAirticketorderDeatail(orderId, prdType);
			//处理多币种信息 start
			String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			orderDetail.remove("totalmoney");
			orderDetail.put("totalmoney", totalMoney);
			//处理多币种信息 end
			model.addAttribute("orderDetail", orderDetail);
			
		} else if ("6".equals(prdType.trim())) {// 6代表签证 查询签证信息
			Map<String, Object> orderDetail = airTicketRefundReviewService.queryVisaorderDeatail(orderId);
			
			
			
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
		} else if ("2".equals(prdType.trim())){// 2代表 散拼
			Map<String, Object> orderDetail = airTicketRefundReviewService.querySanPinReviewOrderDetail(orderId);
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
		} else if ("9".equals(prdType.trim())){// 9代表散拼切位
			Map<String, Object> orderDetail = airTicketRefundReviewService.querySanPinReserveOrderDetail(orderId);
			// 处理目的地信息 start
			String targetArea = orderDetail.get("targetArea") == null ? null : orderDetail.get("targetarea").toString();
			if (targetArea != null && !"".equals(targetArea.trim())) {
				String[] strings = targetArea.split(",");
				String areaString = "";
				int tempN = 0;
				for (String tempS : strings) {
					if (tempN != 0) {
						areaString += ",";
					}
					areaString += AreaUtil.findAreaNameById(Long.parseLong(tempS));
					tempN++;
				}
				orderDetail.remove("targetarea");
				orderDetail.put("targetarea", areaString);
			}
			// 处理目的地信息 end
			model.addAttribute("orderDetail", orderDetail);
		} else {// 查询参团信息 1、3、4、5
			Map<String, Object> grouporderDeatail = airTicketRefundReviewService
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
//		String travelerId = request.getParameter("travelerId");
		// 如果travelerId为空 或者0 标示为团队退款审批
		// if (travelerId == null || "".equals(travelerId.trim())
		// || "0".equals(travelerId.trim())) {
		//
		// } else {// 否则就是游客审批
		//
		// }
		// 查询退款信息
		Map<String, String> review = reviewService.findReview(Long
				.parseLong(reviewId));
//		String travelerId = review.get("travelerId");
//		if(Long.parseLong(travelerId) > 0){//游客id>0标示是游客退款 取游客结算价
//			String traUUID = travelerDao.findOne(Long.parseLong(travelerId)).getPayPriceSerialNum();
//			review.put("traMoney", moneyAmountService.getMoney(traUUID));
//		}
		model.addAttribute("flag", request.getParameter("flag"));
		model.addAttribute("reviewdetail", review);
		model.addAttribute("nowlevel", nowlevel);
		model.addAttribute("rid",reviewId);
//		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList());// 到达城市
		// 把travelerId传回去 用于区分是游客退款 还是团队退款
//		model.addAttribute("travelerId", travelerId);
		return "modules/refundreview/refundReviewDetail";
	}
	
	/**
	 * 退款审核的审核通过或驳回
	 */
	@ResponseBody
	@RequestMapping(value = "refundReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String refundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		// 1 组织参数
		String revId = request.getParameter("revId");//审核表id
		String curLevel = request.getParameter("nowlevel");//当前层级
		String strResult = request.getParameter("result");//审批结果 1 通过 0 驳回
		String denyReason = request.getParameter("denyReason");//驳回原因
		String amount = request.getParameter("moneyAmount");//退款数额
		String orderType = request.getParameter("orderTypeSub");//产品类型
		String orderId = request.getParameter("orderId");//订单id
		String currencyId = request.getParameter("currencyId");//币种id
		// 2 调用审核接口处理
		int lastLevelFlagNum = reviewService.UpdateReview(Long.parseLong(revId), Integer.parseInt(curLevel), Integer.parseInt(strResult), denyReason);
		// 3如果审核通过并且当前层级为最高层级 则直接调用退款接口退款
//		List<Review> list = reviewDao.findReviewActive(Long.parseLong(revId));
//		if(Integer.parseInt(strResult) == 1 && list.get(0).getTopLevel() == Integer.parseInt(curLevel)){
		if(Integer.parseInt(strResult) == 1 && lastLevelFlagNum == 1){
			List<MoneyAmount> moneyList = new ArrayList<MoneyAmount>();
			MoneyAmount ma = new MoneyAmount();
			ma.setAmount(BigDecimal.valueOf(Double.valueOf(amount)));//款数
			ma.setOrderType(Integer.parseInt(orderType));//订单类型 即 产品类型
			ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
			ma.setUid(Long.parseLong(orderId));//订单id
			ma.setReviewId(Long.parseLong(revId));//revId
			ma.setCurrencyId(Integer.parseInt(currencyId));//币种
//			if(traid > 0){//游客id > 0 标示 是游客退款
//				ma.setBusindessType(2);//2标示游客退款
//			} else {
				ma.setBusindessType(1);//1标示订单退款
//			}
			moneyList.add(ma);
			moneyAmountService.saveMoneyAmounts(moneyList);
			//退款不减游客结算价  另 签证应额外处理
//			if(Context.ORDER_STATUS_VISA.equals(orderType)){//如果是签证类型 则 减少游客的达账金额和已付金额
//				//获取游客信息
//				Traveler traveler = travelerDao.findOne(traid);
//				if(traveler != null){
//					//获取达账金额UUID
//					String accountUUID = traveler.getAccountedMoney();
//					//获取已付金额UUID
//					String payedUUID = traveler.getPayedMoneySerialNum();
//					//处理达账金额
//					List<MoneyAmount> accountMoneys = moneyAmountService.findAmountBySerialNum(accountUUID);
//					if(accountMoneys != null){
//						for(MoneyAmount tempA : accountMoneys){
//							if(currencyId.equals(tempA.getCurrencyId())){
//								tempA.setAmount(tempA.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount)).negate()));
//								moneyAmountService.saveOrUpdateMoneyAmount(tempA);
//								break;
//							}
//						}
//					}
//					//处理已付金额
//					List<MoneyAmount> payedMoneys = moneyAmountService.findAmountBySerialNum(payedUUID);
//					if(payedMoneys != null){
//						for(MoneyAmount tempB : payedMoneys){
//							if(currencyId.equals(tempB.getCurrencyId())){
//								tempB.setAmount(tempB.getAmount().add(BigDecimal.valueOf(Double.parseDouble(amount)).negate()));
//								moneyAmountService.saveOrUpdateMoneyAmount(tempB);
//								break;
//							}
//						}
//					}
//				}
//			}
		}
		return "success";
	}
	
	/**
	 * 退款的付款按钮功能  组织数据 调用付款接口
	 */
	@RequestMapping(value = "refundPay")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String refundPay(Model model, HttpServletRequest request, HttpServletResponse response){
		
		//调用支付接口的参数
		List<OrderPayDetail> orderPays = new ArrayList<OrderPayDetail>();
		OrderPayDetail orderPayDetail = new OrderPayDetail();
		orderPayDetail.setProjectId(request.getParameter("orderId") == null ? null : Long.parseLong(request.getParameter("orderId")));
		orderPayDetail.setTravelerId(request.getParameter("travelerId") == null ? null : Long.parseLong(request.getParameter("travelerId")));
		orderPayDetail.setOrderType(request.getParameter("orderType") == null ? null : Integer.parseInt(request.getParameter("orderType")));
		orderPayDetail.setPayCurrencyId(request.getParameter("currencyId") == null ? null : request.getParameter("currencyId"));
		orderPayDetail.setPayCurrencyPrice(request.getParameter("payPrice") == null ? null : request.getParameter("payPrice"));
		orderPayDetail.setReviewId(Long.parseLong(request.getParameter("reviewId")));
		/*组织应收价数据 start*/
		String totalCurrencyId = "";
		String totalCurrencyPrice = "";
		String serialNums = "";
		if("7".equals(request.getParameter("orderType"))){//机票  PS : 暂时不处理切位订单
			AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(request.getParameter("orderId")));
			serialNums = airticketOrder.getTotalMoney();
		} else if("6".equals(request.getParameter("orderType"))){//签证
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(request.getParameter("orderId")));
			serialNums = visaOrder.getTotalMoney();
		} else {//参团
			ProductOrderCommon productOrderCommon = productOrderService.getProductorderById(Long.parseLong(request.getParameter("orderId")));
			serialNums = productOrderCommon.getTotalMoney();
		}
		List<MoneyAmount> list = moneyAmountService.findAmountBySerialNum(serialNums);
		int n = 0;
		for(MoneyAmount temp : list) {
			if(n == 0) {
				totalCurrencyId +=temp.getCurrencyId();
				totalCurrencyPrice += temp.getAmount();
			}else{
				totalCurrencyId +="," + temp.getCurrencyId();
				totalCurrencyPrice += "," + temp.getAmount();
			}
			n++;
			 
		}
		/*组织应收价数据 end*/
		orderPayDetail.setTotalCurrencyId(totalCurrencyId);
		orderPayDetail.setTotalCurrencyPrice(totalCurrencyPrice);
		
		orderPays.add(orderPayDetail);
		
		OrderPayInput orderPayInput = new OrderPayInput();
		
		orderPayInput.setAgentId(request.getParameter("agentId") == null ? null : Integer.parseInt(request.getParameter("agentId")));
		orderPayInput.setOrderPayDetailList(orderPays);
		orderPayInput.setPayType("2");
		orderPayInput.setAgentId(request.getParameter("agentId") == null ? null : Integer.parseInt(request.getParameter("agentId")));
		
		orderPayInput.setServiceClassName("com.trekiz.admin.modules.review.refundreview.service.AirTicketRefundReviewServiceImpl");
		orderPayInput.setServiceAfterMethodName("updatePayStatus");//更改付款标志的方法
		
		request.setAttribute("pay", orderPayInput);
		return "forward:"+Global.getAdminPath()+"/orderPayMore/pay";
	}
	
	/**
	 * 申请审核之前的校验
	 * 返回map 若result为空表示校验通过
	 * 返回map 若result不为空表示校验不通过
	 */
	@RequestMapping(value = "beforeAddReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	@ResponseBody
	public Map<String, Object> beforeAddReview(Model model, HttpServletRequest request, HttpServletResponse response){
		//获取当前要申请的审核流程id
		String reviewFlowId = request.getParameter("reviewFlowId");
		//获取订单的id
		String orderId = request.getParameter("orderId");
		//获取游客的id
		String travelerids = request.getParameter("travelerids");
		
		//获取类型 是订单(1)还是游客(2)
		String rType = request.getParameter("rType");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reviewFlowId", reviewFlowId);
		params.put("orderId", orderId);
		params.put("travelerids", travelerids);
		params.put("rType", rType);
		String result = airTicketRefundReviewService.beforeAddReview(params);
		params.put("result", result);
		return params;
	}
	
	/**
	 * 取消退票之外的其它审核流程
	 */
	@RequestMapping(value = "cancelOtherReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	@ResponseBody
	public String cancelOtherReview(Model model, HttpServletRequest request, HttpServletResponse response){
		//获取当前要申请的审核流程id
		String reviewFlowId = request.getParameter("reviewFlowId");
		//获取订单的id
		String orderId = request.getParameter("orderId");
		//获取游客的id
		String travelerids = request.getParameter("travelerids");
		//获取类型 是订单(1)还是游客(2)
		String rType = request.getParameter("rType");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reviewFlowId", reviewFlowId);
		params.put("orderId", orderId);
		params.put("travelerids", travelerids);
		params.put("rType", rType);
		String result = airTicketRefundReviewService.cancelOtherReview(params);
		return result;
	}
	
	/**
	 * 付款审核退款单
	 * @author jiachen
	 * @DateTime 2015年6月15日 下午2:39:49
	 * @param reviewId
	 * @return String
	 */
	@RequestMapping("refundTable")
	public String refundTable(String reviewId, Model model) {
		
		if(StringUtils.isNotBlank(reviewId)) {
			Map<String, Object> map = new HashMap<String, Object>();
			airTicketRefundReviewService.refundTable(reviewId, map);
			
			for(String key : map.keySet()) {
				model.addAttribute(key, map.get(key));
			}
		}
		return "modules/payreview/refundTable";
	}
	
	/**
	 * 下载退款审核退款单word版
	 * @author jiachen
	 * @DateTime 2015年6月16日 上午10:24:26
	 * @param reviewId
	 * @return File
	 */
	@RequestMapping(value ="downLoadRefundTable")
	public ResponseEntity<byte[]> downLoadRefundTable(String reviewId,
			HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName="";
    	fileName =  "请款单" + nowDate + ".doc"  ;
    	File file = airTicketRefundReviewService.downLoadRefundTable(reviewId);
		OutputStream os = null;
    	try {
			if(file != null && file.exists()){
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
	            os.flush();
			}       		
    		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(os!=null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
		return null;
	}
	
	/**
	 * 批量退款审核的审核通过或驳回
	 */
	@ResponseBody
	@RequestMapping(value = "batchrefundReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String batchRefundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		// 1 组织参数
		String revIds = request.getParameter("revIds");//审核表ids
		String remark = request.getParameter("remark");//通过/驳回原因
		String strResult = request.getParameter("result");//通过/驳回原因
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
			// 2 调用审核接口处理
			int lastLevelFlagNum = reviewService.UpdateReview(Long.parseLong(revid), Integer.parseInt(review.get("curLevel")), Integer.parseInt(strResult), remark);
			// 3如果审核通过并且当前层级为最高层级 则直接调用退款接口退款
			if(Integer.parseInt(strResult) == 1 && lastLevelFlagNum == 1){
				List<MoneyAmount> moneyList = new ArrayList<MoneyAmount>();
				MoneyAmount ma = new MoneyAmount();
				ma.setAmount(BigDecimal.valueOf(Double.valueOf(review.get("refundPrice"))));//款数
				ma.setOrderType(Integer.parseInt(review.get("productType")));//订单类型 即 产品类型
				ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
				ma.setUid(Long.parseLong(review.get("orderId")));//订单id
				ma.setReviewId(Long.parseLong(revid));//revId
				ma.setCurrencyId(Integer.parseInt(review.get("currencyId")));//币种
	//			if(traid > 0){//游客id > 0 标示 是游客退款
	//				ma.setBusindessType(2);//2标示游客退款
	//			} else {
					ma.setBusindessType(1);//1标示订单退款
	//			}
				moneyList.add(ma);
				moneyAmountService.saveMoneyAmounts(moneyList);
			}
		}
		return "success";
	}
}
