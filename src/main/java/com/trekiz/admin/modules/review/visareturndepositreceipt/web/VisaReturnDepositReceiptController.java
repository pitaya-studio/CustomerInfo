package com.trekiz.admin.modules.review.visareturndepositreceipt.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.visareturndepositreceipt.service.IVisaReturnDepositReceiptService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;

/**
 * xinwei.wang added
 * @author
 */
@Controller
@RequestMapping(value = "${adminPath}/visa/workflow/returndepositreceipt")
public class VisaReturnDepositReceiptController extends BaseController {
	
	public static final Integer VISA_PRODUCT_TYPE=6; //签证产品订单类型6表示
	public static final Integer VISA_HYJSJ_FLOW_TYPE=13; //还签证押金收据流程类型13表示，参见表 
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
	private IVisaReturnDepositReceiptService visaReturnDepositReceiptService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private MoneyAmountService  moneyAmountService;
	
	@Autowired
    private VisaProductsService visaProductsService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
//	@Autowired
//	private ReviewDao reviewDao;
//	
//	@Autowired
//	private ReviewLogDao reviewLogDao;
	
	/**
	 *还签证押金收据申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "createVisaHYJSJ")
	public Map<String, Object> createVisaHYJSJ(HttpServletRequest request,	
		HttpServletResponse response) throws JSONException {
		
		String travelerID = request.getParameter("travelerID");//游客ID
		//System.out.println(travelerID);
		String depositReceiptAmount = request.getParameter("depositReceiptAmount");//收据金额
		String depositReceiptor = request.getParameter("depositReceiptor");//接收人
		String depositReceiptReturnTime = request.getParameter("depositReceiptReturnTime");
		
		//申请时的addReview 要用添加dept的方法
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerID));
		Long deptId = visaOrderService.getProductPept(traveler.getOrderId());
		
		List<Detail> listDetail = new ArrayList<Detail>();
		listDetail.add(new Detail("depositReceiptAmount", depositReceiptAmount));
		listDetail.add(new Detail("currencyId", "1"));//默认还押金收据币种为RMB
		listDetail.add(new Detail("depositReceiptor", depositReceiptor));
		listDetail.add(new Detail("depositReceiptReturnTime", depositReceiptReturnTime));
		
		StringBuffer reply = new StringBuffer("还签证押金收据");
		long addresult = reviewService.addReview(VisaReturnDepositReceiptController.VISA_PRODUCT_TYPE, //产品类型 
				VisaReturnDepositReceiptController.VISA_HYJSJ_FLOW_TYPE,//流程类型  flowtype
				traveler.getOrderId()+"",//订单ID
				traveler.getId().longValue(),//游客ID
				Long.parseLong("0"), //新提交的审核请置 0. 重新提交审核时,等于上次审核记录的主键
				"",//创建原因
				reply, 
				listDetail,
				deptId);
		if (addresult!=0) {
			reply.append("申请成功");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("visaHYJSHreply",reply.toString());//1为申请成功 2为申请失败
		return map;
	}
	
	/**
	 * 还签证押金收据申请审批列表
	 * wxw added
	 *  2014年12月25日11:11:49
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnDepositReceiptReviewList")
	public String visaReturnDepositReceiptReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		//获取流程相关角色IDs
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT);
		
		Page<Map<String, Object>> page = null;
		if (userJobs == null || userJobs.size() < 1) {
			page = visaReturnDepositReceiptService.queryVisaReturnDepositReceiptReviewInfo(request, response,"0");
		}else {
			//如有审核职位，选取一个默认职位，以确认默认的审核层级
			page = visaReturnDepositReceiptService.queryVisaReturnDepositReceiptReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
		}
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null == userJobId) {
			if (null != userJobs && userJobs.size() > 0) {
				conditionsMap.put("userJobId", userJobs.get(userJobs.size()-1).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		model.addAttribute("conditionsMap", conditionsMap);
		
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
        
		model.addAttribute("userJobs", userJobs);
		//wxw added 20150819 添加下单人
		List<String> createByNameList = visaOrderService.findVisaOrderCreateBy();
		model.addAttribute("createByList", createByNameList);
		
		/**
		 * 测试URL
		 * http://localhost:8080/trekiz_wholesaler_tts/a/visa/workflow/returndepositreceipt/visaReturnDepositReceiptReviewList?flowType=13
		 */
		return "modules/visaReturndepositreceipt/visaReturnDepositReceiptReviewList";
	}
	
	
	
	/**
	 * 还签证押金收据申请审批列表
	 * wxw added
	 *  2014年12月25日11:11:49
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnDepositReceiptReviewList4CW")
	public String visaReturnDepositReceiptReviewList4CW(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		//获取流程相关角色IDs
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT);
		
		//获取按照审核level排序的userJobs
		//userJobs = visaReturnDepositReceiptService.getOrdedUserJobList (Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT,userJobs);
		
		Page<Map<String, Object>> page = null;
		if (userJobs==null||userJobs.size()<1) {
			page = visaReturnDepositReceiptService.queryVisaReturnDepositReceiptReviewInfo(request, response,"0");
		}else {
			//如有审核职位，选取一个默认职位，以确认默认的审核层级
			page = visaReturnDepositReceiptService.queryVisaReturnDepositReceiptReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
		}
		model.addAttribute("page", page);
		// 处理参数返回
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		//处理页面从菜单进入审核列表时  默认角色的处理
		if (null==userJobId) {
			if (null!=userJobs&&userJobs.size()>0) {
				conditionsMap.put("userJobId",  userJobs.get(userJobs.size()-1).getId()+"");
			}
		}else {
			conditionsMap.put("userJobId",userJobId);
		}
		model.addAttribute("conditionsMap", conditionsMap);
		
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
		model.addAttribute("userJobs", userJobs);
		
		//wxw added 20150819 添加下单人
		List<String> createByNameList = visaOrderService.findVisaOrderCreateBy();
		model.addAttribute("createByList", createByNameList);
		
		/**
		 * 测试URL
		 * http://localhost:8080/trekiz_wholesaler_tts/a/visa/workflow/returndepositreceipt/visaReturnDepositReceiptReviewList?flowType=13
		 */
		return "modules/visaReturndepositreceipt/visaReturnDepositReceiptReviewList4CW";
	}
	
	/**
	 * 查询条件处理 
	 * @param request
	 * @return
	 */
	private Map<String, Object> prepareQueryCond(HttpServletRequest request) {
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("orderType", request.getParameter("orderType"));
		conditionsMap.put("groupCode", request.getParameter("groupCode"));
		String statusChoose = request.getParameter("statusChoose");
		if (null==statusChoose) {
			statusChoose = "1";
		}
		conditionsMap.put("statusChoose", statusChoose);
		
		conditionsMap.put("channel", request.getParameter("channel") == null || "".equals(request.getParameter("channel").trim())
				? null : Integer.parseInt(request.getParameter("channel")));
		//-----wxw 2015-07-29 added 游客姓名------
		conditionsMap.put("travlerName", request.getParameter("travlerName") == null || "".equals(request.getParameter("travlerName").trim())
				? null: request.getParameter("travlerName"));
		//-----wxw 2015-07-29 added 订单编号------
		conditionsMap.put("orderNum", request.getParameter("orderNum") == null|| "".equals(request.getParameter("orderNum").trim())
				? null: request.getParameter("orderNum"));
		//-----wxw 2015-07-29 added 押金金额------
		conditionsMap.put("depositeAmount", request.getParameter("depositeAmount") == null|| "".equals(request.getParameter("depositeAmount").trim())
				? null: request.getParameter("depositeAmount"));
		//------wxw 2015-08-19 added 下单人------
		String orderCreateBy = request.getParameter("orderCreateBy");
		conditionsMap.put("orderCreateBy", orderCreateBy == null || "".equals(orderCreateBy.trim())? null: Integer.parseInt(orderCreateBy));
		//------wxw 2015-08-19 added 销售------
		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler == null || "".equals(saler.trim())? null: Integer.parseInt(saler));
		
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter == null || "".equals(meter.trim())? null: Integer.parseInt(meter));
		
		
		// conditionsMap.put("active", request.getParameter("active"));
		conditionsMap.put("startTime", request.getParameter("startTime"));
		conditionsMap.put("endTime", request.getParameter("endTime"));
		conditionsMap.put("orderBy", request.getParameter("orderBy"));
		
		//处理从url传递的这两个字段
		conditionsMap.put("userJobId", request.getParameter("userJobId"));
		conditionsMap.put("flowType", request.getParameter("flowType"));
		
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");//订单创建日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");//订单更新日期排序标识
		conditionsMap.put("orderCreateDateSort", orderCreateDateSort);
		conditionsMap.put("orderUpdateDateSort", orderUpdateDateSort);
		conditionsMap.put("orderCreateDateCss", orderCreateDateCss);
		conditionsMap.put("orderUpdateDateCss", orderUpdateDateCss);
		conditionsMap.put("paymentType", request.getParameter("paymentType"));
		
		//conditionsMap.put("fromflag", request.getParameter("fromflag"));
		
		return conditionsMap;
	}
	
	
	/**
	 * 还签证押金收据审批详情页 
	 * wangxinwei
	 *  2014年12月24日15:15:00
	 */
	@RequestMapping(value = "visaReturnDepositeReceiptReviewDetail")
	public String visaReturnDepositeReceiptReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		String fromflag = request.getParameter("fromflag");
		
		//订单相关信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
/*		if (null==visaOrder.getMainOrderCode()||0==visaOrder.getMainOrderCode()) {
			//处理是否为单团的情况
			visaOrder.setMainOrderCode(0);
		}*/
		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("visaOrder", visaOrder);
		
		//产品相关信息
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Dict collarZoning = dictService.findByValueAndType(visaProduct.getCollarZoning().toString(), "from_area");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("visaType", visaType);
		model.addAttribute("collarZoning", collarZoning);
		model.addAttribute("country", country);
		
		//游客相关信息
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
		model.addAttribute("traveler", traveler);
		
		//签证相关信息
		Visa visa = visaService.findVisaByTravlerId(Long.parseLong(travelerId));
		Dict visaStauts = dictService.findByValueAndType(visa.getVisaStauts()+"", "visa_status");
		model.addAttribute("visa", visa);
		model.addAttribute("visaStauts", visaStauts);
		
		//还签证押金收据申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		if (reviewAndDetailInfoMap!=null) {
			model.addAttribute("revCreateDate", reviewAndDetailInfoMap.get("createDate").subSequence(0, 19));//报批日期
			model.addAttribute("revCreateReason", reviewAndDetailInfoMap.get("createReason"));//申报原因
			model.addAttribute("revDepositReceiptAmount", reviewAndDetailInfoMap.get("depositReceiptAmount"));//收据金额
			model.addAttribute("revReceiptor", reviewAndDetailInfoMap.get("receiptor"));//还收据人
			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			if (null!=currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);//还收据人
			}
		}
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",revid);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",revid);
		
		model.addAttribute("fromflag",fromflag);
		
		//model.addAttribute("airticketReturnDetailInfoMap",airticketReturnDetailInfoMap);
		return "review/visaReturndepositreceipt/visaReturnDepositReceiptReviewDetail";
	}
	
	
	/**
	 * 还签证押金收据审核：驳回 或 通过
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "reviewVisaDepositReturnReceipt")
	public String reviewVisaDepositReturnReceipt(Model model,HttpServletRequest request, HttpServletResponse response) {
		StringBuffer reply = new StringBuffer();
		String revid = request.getParameter("revid");
		if (revid == null || "".equals(revid)) {
			reply.append("审批id不能为空");
		}
		String nowLevel = request.getParameter("nowLevel");
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
			return visaReturnDepositeReceiptReviewDetail(model, request, response);
		}
		//num == 1 审核通过
		//int num = 
		reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),denyReason);
		/*List<Review> list = reviewDao.findReviewActive(Long.parseLong(revid));
		if(list.get(0).getTopLevel() == Integer.parseInt(nowLevel)){//如果当前层级为最高层级  说明该流程已经审核结束
			if (1==num) {//审核成功
				reviewService.reviewOperationDone(Long.parseLong(revid),3); //在业务上没有后续操作：整个流程审批及后续操作完毕
			}
		}*/
		String fromflag = request.getParameter("fromflag");
		if ("CW".equalsIgnoreCase(fromflag)) {
			return visaReturnDepositReceiptReviewList4CW(model, request, response);
		}else {
			return visaReturnDepositReceiptReviewList(model, request, response);
		}
		
	}
	
	
	/**
	 * 审核回退：根据revid进行批次回退操作,即返回上一级审核
	 * 规则如下：
	 * 1.第一层级 和 最后一层级没有退回操作
	 * 2.隔级审核后不能再进行退回操作
	 * 3.只有审核中状态的才可能进行退回操作
	 * wangxinwei 2015年06月01日21:23:00
	 */
	@RequestMapping(value = "returnDepositReceiptCancelAjax")
	@ResponseBody
	public Map<String, Object> returnDepositReceiptCancelAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		String revid = request.getParameter("revid");
		try {
			
			//审核回退
		    if(null!=revid) {
		    	reviewService.CancelReview(Long.parseLong(revid));
			}
				
		}catch (Exception e) {
			map.put("result",2);
			e.printStackTrace();
			return map;
		}	
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	
	 //-------------------多项选择审批   开始---------------------
		/**
		 * wxw added 2015-08-11
		 * 还签证押金收据   不定个数审核
		 * @param model
		 * @param request 
		 * @param response
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "multiReviewVisaDepositReturnReceipt")
		public Object multiReviewVisaDepositReturnReceipt(Model model,HttpServletRequest request, HttpServletResponse response) {
			
	        String result =  request.getParameter("result"); //1审核通过，0，驳回
			String remarks = request.getParameter("remarks");//批量审核驳回原因
			/**
			 * 参数结构：
			 * 1@110,1@112
			 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
			 */
			String revids = request.getParameter("revids");
			String[] levelandrevids = revids.split(",");	
			
			
			for (int i = 0; i < levelandrevids.length; i++) {
				StringBuffer reply = new StringBuffer();
				if (result == null || "".equals(result)) {
					reply.append("审批结果不能为空");
				}
				
				String nowLevel = levelandrevids[i].split("@")[0];
				String revid = levelandrevids[i].split("@")[1];
				
				
				/**
				 * 2015-08-10 wxw added 不定量多个还签证收据同时审批
				 */
				//UpdateReview返回 0：审核成功时，当前审核不是最后一层， 1：审核成功时，当前审核是最后一层
				reviewService.UpdateReview(Long.parseLong(revid),Integer.parseInt(nowLevel), Integer.parseInt(result),remarks);
				
				//3.更新review ，  并创建驳回日志
				/*Date updateDate = new Date();
				long updateBy = UserUtils.getUser().getId();	
				ReviewLog reviewLog = new ReviewLog(Long.parseLong(revid), Integer.parseInt(nowLevel), updateBy, updateDate,"审核通过",remarks);
				reviewLogDao.save(reviewLog);*/
				
			}
			
			 Map<String, Object> resultMap = new HashMap<String, Object>();
			 resultMap.put("msg", "操作成功！");
			 return resultMap;
			
	    }
		//-------------------多项选择审批   结束---------------------

}
