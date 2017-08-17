package com.trekiz.admin.review.returnvisareceipt.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.BatchTravelerRelation;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.BatchRecordDao;
import com.trekiz.admin.modules.sys.repository.BatchTravelerRelationDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.SysBatchNoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.review.borrowing.airticket.service.NewOrderReviewService;
import com.trekiz.admin.review.borrowing.airticket.web.ActivityAirTicketOrderLendMoneyController;
import com.trekiz.admin.review.returnvisareceipt.service.ActivityVisaReturnReceiptService;

/**  
 * @Title: ActivityVisaReturnReceiptController.java
 * @Package com.trekiz.admin.review.returnvisareceipt.web
 * @Description: 还签证收据controller
 * @author xinwei.wang  
 * @date 2015-2015年12月3日 下午12:02:25
 * @version V1.0  
 */
@Controller
@RequestMapping(value = "${adminPath}/visa/hqx/returnvisareceipt")
public class ActivityVisaReturnReceiptController {
	
	private static final Logger log = Logger.getLogger(ActivityAirTicketOrderLendMoneyController.class);
	
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private SysBatchNoService sysBatchNoService;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private BatchRecordDao batchRecordDao;
	@Autowired
    private TravelerService travelerService;
	@Autowired
	private BatchTravelerRelationDao batchTravelerRelationDao;
	@Autowired
	private ActivityVisaReturnReceiptService activityVisaReturnReceiptService;
	@Autowired
	private ReviewService processReviewService;
	@Autowired
	private MoneyAmountService  moneyAmountService;
	@Autowired
    private VisaProductsService visaProductsService;
	@Autowired
	private DictService dictService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private VisaService visaService;
	@Autowired
	private NewOrderReviewService newOrderReviewService;
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	
	
	@RequestMapping(value = "checkBatchHsjHqx")
	@ResponseBody
	public Object checkBatchHsjHqx(HttpServletRequest request){
		String visaIds = request.getParameter("visaIds")+"0";
		String travellerIds = request.getParameter("travelerIds")+"0";
		Map<String, Object> map  = visaOrderService.checkBatchHsjHqx4activiti(visaIds,travellerIds);
		return map;
	}
	
	
	/**
	 * @Description: 批量还收据列表（环球行）
	 * @author xinwei.wang
	 * @date 2015年12月3日下午2:41:18
	 * @param req
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "batchHsj4activiti")
	@ResponseBody
	public Object batchHsj4activiti(HttpServletRequest req){
		Map<String, Object> resultMap = null;
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 环球行的情况下
		//if (companyId.intValue() == 68) {
			String type = req.getParameter("type");
			String visaIds = req.getParameter("visaIds")+"0";
			String orderIds = req.getParameter("orderIds")+"0";
			String travelerIds = req.getParameter("travellerIds")+"0";
			String returnReceiptJes = req.getParameter("returnReceiptJe")+"0";
			String returnReceiptNames = req.getParameter("returnReceiptName")+"0";
			String returnReceiptTimes = req.getParameter("returnReceiptTime")+"0";
			String returnReceiptRemarks = req.getParameter("returnReceiptRemark")+"0";
			String borrowamounts = req.getParameter("borrowamounts")+"0";
			
			String[] travelerIDS = travelerIds.split(",");
			String[] visaIDs = visaIds.split(",");
			String[] orderIDs = orderIds.split(",");
			String[] jes = returnReceiptJes.split(",");
			String[] remarks = returnReceiptRemarks.split(",");
		
			
			// 生成批次号
			String batchNo = sysBatchNoService.getVisaReturnReceiptBatchNo();
			// 批次总人数
			int batchPersonCount = travelerIDS.length - 1;
			// 批次总金额
			BigDecimal batchtotalMoney = BigDecimal.ZERO;
			for (int i = 0; i < travelerIDS.length - 1; i++) {
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					batchtotalMoney = batchtotalMoney.add(new BigDecimal(jes[i]));
				}
			}
			//获取人民币币种id
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
			buffer.append(" AND c.del_flag = 0 AND c.create_company_id=");
			buffer.append(UserUtils.getUser().getCompany().getId());
			List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
			Integer borrowtotalcurrencyId = 0;
			String borrowtotalcurrencyName="人民币";
			for (int i = 0; i < currencylist.size(); i++) {
				if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
					borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
					borrowtotalcurrencyName = (String)currencylist.get(i).get("currency_name");
					break;
				}
			}
			//生成uuid
			String uuid = UUID.randomUUID().toString();
	
			if ("2".equals(type)) {
//				try{
					resultMap=activityVisaReturnReceiptService.visaBatchHsj4activiti(batchNo,visaIds,travelerIds,returnReceiptJes,returnReceiptNames,returnReceiptTimes,returnReceiptRemarks,borrowamounts);
//				}catch(Exception e){
//					e.printStackTrace();
//					if(resultMap==null){
//						resultMap = new HashMap<String,Object>();
//					}
//					resultMap.put("msg", "签证批量还收据申请失败！");
//					return resultMap;
//				}
			}
			
			VisaFlowBatchOpration record = new VisaFlowBatchOpration();
			record.setUuid(uuid);
			record.setBatchNo(batchNo);
			record.setBusynessType("1");
			record.setBatchPersonCount(batchPersonCount);
			record.setBatchTotalMoney(batchtotalMoney.toString());
			record.setCreateUserId(UserUtils.getUser().getId());
			record.setCreateUserName(UserUtils.getUser().getName());
			record.setCreateTime(new Date());
			record.setPrintStatus("0");
			record.setCurrencyId(borrowtotalcurrencyId);
			record.setCurrencyName(borrowtotalcurrencyName);
			record.setIsNewReview(2);//用于表示是新审核
			if ("1".equals(type)) {
				record.setReviewStatus("99");
				record.setIsSubmit("1");
			} else if ("2".equals(type)) {
				record.setReviewStatus("1");
				record.setIsSubmit("2");
			}
	
			batchRecordDao.getSession().save(record);
	
			for(int i = 0; i < travelerIDS.length - 1; i++){
				//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {
					uuid = UUID.randomUUID().toString();
					BatchTravelerRelation relation = new BatchTravelerRelation();
					relation.setUuid(uuid);
					relation.setBatchUuid(record.getUuid());
					relation.setBatchRecordNo(batchNo);
					relation.setTravelerId(Long.parseLong(travelerIDS[i]));
					relation.setVisaId(Long.parseLong(visaIDs[i]));
					relation.setOrderId(Long.parseLong(orderIDs[i]));
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
					relation.setTravelerName(traveler.getName());
					relation.setBusinessType(2);//业务类型 1:借款 2：还收据  3：借护照 4：还护照
					relation.setTravellerBorrowMoney(new BigDecimal(jes[i]));
					relation.setRemark(remarks[i]);
					relation.setCreatebyId(UserUtils.getUser().getId());
					relation.setCreatebyName(UserUtils.getUser().getName());
					relation.setIsSubmit("1");
					relation.setSaveTime(new Date());
					if ("2".equals(type)) {
						relation.setSubmitbyId(UserUtils.getUser().getId());
						relation.setSubmitbyName(UserUtils.getUser().getName());
						relation.setIsSubmit("2");
						relation.setSubmitTime(new Date());
					}
	
					batchTravelerRelationDao.getSession().save(relation);
				}
			}
		//}
		return resultMap;
	}
	
	/**
	 * tempdesc：BJH151014003 有记录
	 * @Description: 签务签证订单-还收据记录
	 * @author xinwei.wang
	 * @date 2015年12月4日上午17:30:17
	 * @param request
	 * @param response
	 * @param model
	 * @param orderId
	 * @param flowType
	 * @param productType
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "returnReceiptRecord4HQXactivity")
	public String returnReceiptRecord4HQXactivity( HttpServletRequest request, HttpServletResponse response, Model model, String orderId) {
		VisaOrder order = visaOrderService.findVisaOrder(Long.valueOf(orderId));
		//Long pid = order.getVisaProductId();
		Long deptId = visaOrderService.getProductPept(Long.valueOf(orderId));//通过orderId获取产品的发布部门
		                                       //processReviewService.getReviewDetailMapListByOrderId(deptId, productType, processType, orderId, orderByProperty, orderByDirection)
		List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(deptId, 6, 4, order.getId().toString(),OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
		
		List<Map<String, String>> returnReceiptRecordList = new ArrayList<Map<String, String>>();
		// 通过订单ID查询reviewId
		//List<Review> reviewList = reviewDao.findReviewSortByCreateDate(Context.ORDER_TYPE_QZ, 20, orderId);
		if (processList != null && processList.size() > 0) {
			Map<String, String> reviewAndDetailInfoMap = null;
			for (Map<String, Object> map : processList) {
				reviewAndDetailInfoMap= new HashMap<String, String>();
				
				//1.获取报批日期
				reviewAndDetailInfoMap.put("createDate", DateUtils.date2String((Date)map.get("createDate"), "yyyy-MM-dd hh:mm:ss"));
				
				//2.游客/团队
				String  travellerName = (String)map.get("travellerName");
				reviewAndDetailInfoMap.put("travelerName", travellerName);
				
				//3.币种
				reviewAndDetailInfoMap.put("currencyName", "人民币");
				//4. 借款金额
				DecimalFormat df = new DecimalFormat("#,##0.00");
				String receiptAmount = df.format(new BigDecimal((String)map.get("receiptAmount")));
				reviewAndDetailInfoMap.put("receiptAmount", receiptAmount);
				//5. 申请人
				reviewAndDetailInfoMap.put("createBy", (String)(map.get("createBy")));
				
				//6. 借款审批状态
				reviewAndDetailInfoMap.put("status", (map.get("status").toString()));
				//8.其他
				reviewAndDetailInfoMap.put("orderId", orderId);//订单id
				reviewAndDetailInfoMap.put("id", (map.get("id").toString()));//review_new id  procInstId
				reviewAndDetailInfoMap.put("travellerId", (map.get("travellerId").toString()));//review_new id  procInstId
				
				returnReceiptRecordList.add(reviewAndDetailInfoMap);
				
			}
		}
	
		// 公司ID
		model.addAttribute("companyId",UserUtils.getUser().getCompany().getId());
		// 订单ID
		model.addAttribute("orderId", orderId);
		// 借款记录List
		model.addAttribute("returnReceiptRecordList", returnReceiptRecordList);
		
		return "review/returnvisareceipt/returnReceiptRecord4HQXactivity";
	}
	
	
	/**
	 * @Description:环球行   还签证收据 单  详情页
	 * @author xinwei.wang
	 * @date 2015年12月4日下午12:07:29
	 * @param model
	 * @param request
	 * @param response
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "visaReturnReceipt4HQXReviewDetail")
	public String visaReturnReceipt4HQXReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String reviewId = request.getParameter("reviewId");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		
		//订单相关信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));

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
		
		//还签证收据申请相关信息
		//Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		
		//获取activiti审核信息 还签证收据申请相关信息
		Map<String,Object>  reviewAndDetailInfoMap = null;
		try{
			if(reviewId!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		
		if (reviewAndDetailInfoMap!=null) {
			model.addAttribute("revCreateDate", DateUtils.date2String((Date)reviewAndDetailInfoMap.get("createDate"), "yyyy-MM-dd hh:mm:ss"));//报批日期
			model.addAttribute("revCreateReason", reviewAndDetailInfoMap.get("createReason"));//申报原因
			model.addAttribute("revReceiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));//收据金额
			model.addAttribute("revReceiptor", reviewAndDetailInfoMap.get("receiptor"));//还收据人
			String currencyId = reviewAndDetailInfoMap.get("currencyId").toString();
			if (null!=currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);//还收据人
			}
		}
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",reviewId);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",reviewId);
		
		//处理审核动态信息
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
	    	model.addAttribute("rLog",rLog);
	    }
		
		return "review/returnvisareceipt/visaReturnReceipt4HQXReviewDetail";
	}
	
	/**
	 * 还签证收据批次审批列表
	 * 
	 * @author yang.jiang 2015年12月5日 10:42:29
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnReceiptBatchReviewList/{menuName}")
	public String visaReturnReceiptBatchReviewList(@PathVariable String menuName, Model model, 
			HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> conditionsMap = prepareParams(request, response);
		//获取批次列表数据（已包含游客列表页所需数据）
		Page<Map<String, Object>> page = newOrderReviewService.visaReturnReceiptBatchReviewList4CW(conditionsMap);
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("page", page);
		
		// for  C460V5
		model.addAttribute("companeyUUID", UserUtils.getUser().getCompany().getUuid());
		
		List<String> createByNameList = visaOrderService.findVisaOrderCreateBy();
		model.addAttribute("createByList", createByNameList);
		if (Context.MENU_FLAG_REVIEW.equals(menuName)) {  //审核模块
			//显示游客列表页（现已停用）
			if ("traveller".equals(conditionsMap.get("isTravellerList"))) {
				return "review/returnvisareceipt/visaReturnReceiptReviewList";
			}
			//显示批次列表页
			return "review/returnvisareceipt/visaReturnReceiptBatchReviewList";
		} else if (Context.MENU_FLAG_REVIEW4CW.equals(menuName)) {  //财务审核模块			
			//显示游客列表页（现已停用）
			if ("traveller".equals(conditionsMap.get("isTravellerList"))) {
				return "review/returnvisareceipt/visaReturnReceiptReviewList4CW";
			}
			//显示批次列表页
			return "review/returnvisareceipt/visaReturnReceiptBatchReviewList4CW";
		} else {
			return "";
		}		
	}

	/**
	 * 还签证审批详情页 
	 * @date 2015年12月17日
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnReceiptReviewDetail4CW")
	public String visaReturnReceiptReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String reviewId = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		
		//订单相关信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
		model.addAttribute("visaOrder", visaOrder);

		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		model.addAttribute("totalMoney", totalMoney);
		
		//产品相关信息
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Dict collarZoning = dictService.findByValueAndType(visaProduct.getCollarZoning().toString(), "from_area");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		
		String companeyUUID = UserUtils.getUser().getCompany().getUuid();
		if ("7a816f5077a811e5bc1e000c29cf2586".equals(companeyUUID)) {
			model.addAttribute("groupCode", visaOrder.getGroupCode());
		}else{
			model.addAttribute("groupCode", visaProduct.getGroupCode());
		}
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
		
		//获取activiti审核信息 还签证收据申请相关信息
		Map<String,Object>  reviewAndDetailInfoMap = null;
		try{
			if(reviewId!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		
		if (reviewAndDetailInfoMap!=null) {
			model.addAttribute("revCreateDate", DateUtils.date2String((Date)reviewAndDetailInfoMap.get("createDate"), "yyyy-MM-dd hh:mm:ss"));//报批日期
			model.addAttribute("revCreateReason", reviewAndDetailInfoMap.get("returnReceiptRemark"));//申报原因
			model.addAttribute("revReceiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));//收据金额
			model.addAttribute("revReceiptor", reviewAndDetailInfoMap.get("receiptor"));//还收据人
			String currencyId = reviewAndDetailInfoMap.get("currencyId").toString();
			if (null!=currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);//还收据人
			}
		}
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",reviewId);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",reviewId);
		
		//处理审核动态信息
//		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
//	    	List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
//	    	model.addAttribute("rLog",rLog);
//	    }
		
		return "review/returnvisareceipt/visaReturnReceiptReviewDetail4CW";
	}
	
	/**
	 * 组织操作参数
	 * @param request
	 * @return
	 */
	private Map<String, Object> prepareParams(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		/**获取参数 start*/
		//团号/产品名称/订单号
		String orderCdGroupCdBatchNo = request.getParameter("orderCdGroupCdBatchNo") == null ? null : request.getParameter("orderCdGroupCdBatchNo").toString().trim();
		//游客姓名(id)
		String travlerName = request.getParameter("travlerName") == null ? null : request.getParameter("travlerName").toString();
		//产品类型(id)
		String productType = request.getParameter("productType") == null ? null : request.getParameter("productType").toString();
		//渠道商(id)
		String agentId = request.getParameter("agentId") == null ? null : request.getParameter("agentId").toString();
		//申请日期（from）
		String applyDateFrom = request.getParameter("applyDateFrom") == null ? null : request.getParameter("applyDateFrom").toString();
		//申请日期（to）
		String applyDateTo = request.getParameter("applyDateTo") == null ? null : request.getParameter("applyDateTo").toString();		
		//下单人(id)
		String order_creator = request.getParameter("order_creator") == null ? null : request.getParameter("order_creator").toString();
		//审批发起人(id)
		String applyPerson = request.getParameter("applyPerson") == null ? null : request.getParameter("applyPerson").toString();
		//计调(id)
		String operator = request.getParameter("operator") == null ? null : request.getParameter("operator").toString();
		//转款金额 (from)
		String transferMoneyFrom = request.getParameter("transferMoneyFrom") == null ? null : request.getParameter("transferMoneyFrom").toString();
		//转款金额 (to)
		String transferMoneyTo = request.getParameter("transferMoneyTo") == null ? null : request.getParameter("transferMoneyTo").toString();
		//审批状态
		String reviewStatus = StringUtils.isBlank(request.getParameter("reviewStatus")) ? null : request.getParameter("reviewStatus").toString();
		//出纳确认
		String cashConfirm = request.getParameter("cashConfirm") == null ? null : request.getParameter("cashConfirm").toString();
		//打印状态
		String printStatus = request.getParameter("printStatus") == null ? null : request.getParameter("printStatus").toString();
		//页签选择状态
		String statusChoose = request.getParameter("statusChoose") == null ? "1" : request.getParameter("statusChoose").toString();
		// 创建日期排序标识
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");
		// 更新日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");
		//订单更新日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");
		//订单创建日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss") == null ? "activitylist_paixu_moren" : StringUtils.isBlank(orderUpdateDateCss) ? "activitylist_paixu_moren" : request.getParameter("orderCreateDateCss"); //默认按照创建日期排序
		// 是否展示的是游客列表
		String isTravellerList = request.getParameter("isTravellerList") == null ? "batch" : !"traveller".equals(request.getParameter("isTravellerList").toString()) ? "batch" : "traveller";
		//page对象
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		/**获取参数 end*/
		/**组装参数 start*/
		result.put("orderCdGroupCdBatchNo", orderCdGroupCdBatchNo);
		result.put("travlerName", travlerName);
		result.put("productType", productType);
		result.put("agentId", agentId);
		result.put("applyDateFrom", applyDateFrom);
		result.put("applyDateTo", applyDateTo);
		result.put("order_creator", order_creator);
		result.put("applyPerson", applyPerson);
		result.put("operator", operator);
		result.put("transferMoneyFrom", transferMoneyFrom);
		result.put("transferMoneyTo", transferMoneyTo);
		result.put("reviewStatus", reviewStatus);
		result.put("cashConfirm", cashConfirm);
		result.put("printStatus", printStatus);
		result.put("statusChoose", statusChoose);
		result.put("orderCreateDateSort", orderCreateDateSort);
		result.put("orderUpdateDateSort", orderUpdateDateSort);
		result.put("orderCreateDateCss", orderCreateDateCss);
		result.put("orderUpdateDateCss", orderUpdateDateCss);
		result.put("isTravellerList", isTravellerList);
		result.put("pageP", page);
		/**组装参数 end*/
		return result;
	}
	
	/**
	 * 还签证收据批次审批列表
	 * 
	 * @author jyang 2015年12月5日 10:42:29
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
//	@RequestMapping(value = "visaReturnReceiptBatchReviewList4CW")
//	public String visaReturnReceiptBatchReviewList4CW(Model model, HttpServletRequest request, HttpServletResponse response) {
//		
//		//获取流程相关角色IDs
//		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
//
//		Page<Map<String, Object>> page=null;
//		if (userJobs==null||userJobs.size()<1) {
//			 page = visaReturnReceiptService.queryVisaReturnReceiptBatchReviewInfo(request, response, "0");
//		}else {
//			 page = visaReturnReceiptService.queryVisaReturnReceiptBatchReviewInfo(request, response, userJobs.get(userJobs.size()-1).getId()+"");
//		}
//		
//		model.addAttribute("page", page);
//		// 处理参数返回
//		Map<String, Object> conditionsMap = prepareQueryCond(request);
//		String userJobId = request.getParameter("userJobId");
//		//处理页面从菜单进入审核列表时  默认角色的处理
//		if (null==userJobId) {
//			if (null!=userJobs&&userJobs.size()>0) {
//				conditionsMap.put("userJobId",  userJobs.get(userJobs.size()-1).getId()+"");
//			}
//		}else {
//			conditionsMap.put("userJobId",userJobId);
//		}
//		model.addAttribute("conditionsMap", conditionsMap);
//		
//        Collections.sort(userJobs, new Comparator<UserJob>() {  
//            public int compare(UserJob arg0, UserJob arg1) {  
//                long hits0 = arg0.getId();  
//                long hits1 = arg1.getId();  
//                if (hits1 > hits0) {  
//                    return 1;  
//                } else if (hits1 == hits0) {  
//                    return 0;  
//                } else {  
//                    return -1;  
//                }  
//            }  
//        });
//    	//---wxw added 处理批量审核时角色显示的待审核记录不正确的问题 2015-09-29 ---
//        for (UserJob userJob : userJobs) {
//        	int userjobReviewCount = visaReturnReceiptService.getJobReviewCountbyUserJob(userJob);
//        	userJob.setCount(userjobReviewCount);
//		}
//		model.addAttribute("userJobs", userJobs);
//		
//		//处理筛选自动关闭的
//		//是否显示搜索条件的关闭与收起
//		String isShowSearch = request.getParameter("showFlag");
//		model.addAttribute("flag", isShowSearch);
//		
//		return "modules/visareturnreceipt/visaReturnReceiptBatchReviewList4CW";
//	}
	
	
	/**
	 * @Description: 还签证收据批次审批详情页 
	 * @author xinwei.wang
	 * @date 2015年12月9日下午7:39:24
	 * @param model
	 * @param request
	 * @param response
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "visaReturnReceipt4HQXBatchReviewDetail")
	public String visaReturnReceipt4HQXBatchReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String reviewId = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		
		String batchno = request.getParameter("batchno");
		
		String fromflag = request.getParameter("fromflag");
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT revn.batch_no AS batchno, revn.create_by AS createBy, revn.id AS reviewid, revn.order_id AS orderid, ");
		buffer.append(" revn.traveller_id AS travelerid, revn.traveller_name AS travelername ");
		buffer.append("FROM review_new revn, visa_flow_batch_opration vfbo ");
		buffer.append("WHERE revn.batch_no= vfbo.batch_no ");
		buffer.append(" AND vfbo.busyness_type = 1  AND revn.process_type = '4'  AND revn.batch_no=");
		buffer.append("'"+batchno+"'");
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		//Map<String, Object>  mp =  list.get(0);
		//totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ct[i]);
		
		StringBuilder revids = new StringBuilder("");
		StringBuilder remarks = new StringBuilder("");
		for (Map<String, Object> map : list) {
			//签证借款申请相关信息
			//Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong((Integer)map.get("reviewid")+""));
			
			Map<String,Object>  reviewAndDetailInfoMap = null;
			try{
				if(reviewId!=null){
					reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(map.get("reviewid").toString());
				}
			}catch(Exception e){
				log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
			}
			
			map.put("receiptAmount", reviewAndDetailInfoMap.get("receiptAmount"));
			map.put("returnReceiptRemark", reviewAndDetailInfoMap.get("returnReceiptRemark"));
			map.put("receiptor", reviewAndDetailInfoMap.get("receiptor"));
			revids.append(reviewAndDetailInfoMap.get("id")).append(",");
			remarks.append(reviewAndDetailInfoMap.get("returnReceiptRemark")).append(",");
			
			
		}
		//Map<String, String> reviewAndDetailInfoMapforone = reviewService.findReview(Long.parseLong(revid));
		Map<String,Object>  reviewAndDetailInfoMapforone = null;
		try{
			if(reviewId!=null){
				reviewAndDetailInfoMapforone = processReviewService.getReviewDetailMapByReviewId(reviewId);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		model.addAttribute("revCreateDate", reviewAndDetailInfoMapforone.get("createDate"));//报批日期
		
	
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",reviewId);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",reviewId);
		
		//批量审批用到
		model.addAttribute("returnreceiptinfolist", list);
		model.addAttribute("batchno",batchno);
		model.addAttribute("revids",revids.toString());
		model.addAttribute("remarks",remarks.toString());
		
		model.addAttribute("fromflag",fromflag);
		
		//model.addAttribute("airticketReturnDetailInfoMap",airticketReturnDetailInfoMap);
		return "review/returnvisareceipt/visaReturnReceipt4HQXBatchReviewDetail";
	}
	
	
	
	//-----------------------签证费    还款单开始 -------------------------
	
	/**
	 * @Description:签证费还款单批次
	 * @author xinwei.wang
	 * @date 2015年12月10日下午6:27:10
	 * @param model
	 * @param request
	 * @param response
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "visaReturnMoney4HQXBatchFeePrint")
	public String visaReturnMoney4HQXBatchFeePrint(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		/*
		 * 填写日期取签务（计调人员）发起借款申请的日期；（ok） 
		 * 借款单位指签务（计调人员）所在部门；（ok） ---默认 签证部（暂不从系统取） 
		 * 经办人指申请人；（ok） 
		 * 审核暂时为空； ----------- 
		 * 财务主管暂时为空；-----------  
		 * 复核（审批）指最后一个审批人； （ok） 
		 * 借款金额人民币大写；（ok）  
		 * 领款人指申请人；（ok）  
		 * 主管审批（总经理）指最后一个审批人； （ok） 
		 * 出纳暂时为空；------------ 
		 * 确认付款日期即财务人员最后确认付款的日期； （ok）
		 */
		
		String revid = request.getParameter("revid");
		String batchno = request.getParameter("batchno");
		model.addAttribute("revid", revid);//吧revid 传到模板模板的html页面，下载模板时使用
		model.addAttribute("batchno", batchno);
		
		//获取批次借款金额
		VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"1");
		String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();
		
		
		/*listDetail.add(new Detail("receiptAmount", receiptAmount));
		listDetail.add(new Detail("currencyId", "1"));//默认收据币种为RMB
		listDetail.add(new Detail("receiptor", receiptor));
		listDetail.add(new Detail("returnTime", returnTime));
		listDetail.add(new Detail("returnReceiptRemark", returnReceiptRemark));
		listDetail.add(new Detail("visaReturnReceiptBatchNo", null));*/
		
		// 签证借款申请相关信息
		//Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		
		Map<String,Object>  reviewAndDetailInfoMap = null;
		try{
			if(revid!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + revid + " 查询出来reviewDetail明细报错 ",e);
		}
		
		if (reviewAndDetailInfoMap != null) {
			model.addAttribute("revCreateDate",(Date)reviewAndDetailInfoMap.get("createDate"));// 填写日期
			model.addAttribute("returnReceiptRemark",reviewAndDetailInfoMap.get("returnReceiptRemark"));// 申报原因(变动)
			
			
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());
			String operatorName = user.getName();
			if (null != operatorName) {
				model.addAttribute("operatorName", operatorName);// 经办人、领款人都为还收据申请人
			} else {
				model.addAttribute("operatorName", "未知");
			}
			model.addAttribute("payDate", (Date)reviewAndDetailInfoMap.get("updateDate"));// 付款日期
			model.addAttribute("receiptAmount",fmtMicrometer(batchborrowtotalMoney));// 借款金额   借款金额 改为批次金额
			String currencyId = reviewAndDetailInfoMap.get("currencyId").toString();
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency.getRemark());
			}
		}
		
		
		model.addAttribute("revReturnAmountDx", digitUppercase(Double.parseDouble(batchborrowtotalMoney)));// 借款金额大写     借款金额 改为批次金额
		
		
		String companyUUid = UserUtils.getUser().getCompany().getUuid();
		//获取单据审批人员Map
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_VISA_RETURN_MONEY, revid);//e5dbd01ec2f649e39d458540a91aa03b
		List<User> general_managers = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.GENERAL_MANAGER);//总经理
		List<User> financial_executives = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.FINANCIAL_EXECUTIVE);//财务主管
		List<User> cashiers = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.CASHIER);//出纳
		List<User> financials = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.FINANCIAL);//财务
		List<User> reviewers = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.REVIEWER);//审批
		
		
		String generalmanagers = getNames(general_managers);//总经理 
		String financialexecutive = getNames(financial_executives);//财务主管
		String cashier = getNames(cashiers);//出纳
		String financial = getNames(financials);//财务
		String  reviewer = getNames(reviewers);//审核
		
		
		model.addAttribute("majorCheckPerson", generalmanagers);//总经理     也是最后一个审批人
		
		/**
		 * 财务主管
		 * 需求变更2015-04-22：如果为环球行用户财务主管为空
		 */
		model.addAttribute("cwmanager", financialexecutive);//财务主管
		
		model.addAttribute("cw", financial);//财务
		
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		model.addAttribute("cashier", cashier);//出纳
		
		model.addAttribute("deptmanager", reviewer);//deptmanager 部门经理      即审批
		
		String printFlag = reviewAndDetailInfoMap.get("printStatus").toString();
		if (null==printFlag||"0".equals(printFlag)) { 
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			model.addAttribute("printDate",printDateStr );
		}else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(reviewAndDetailInfoMap.get("printDate"));
			model.addAttribute("printDate",printDateStr);
		}
	
		return "review/returnvisareceipt/visaReturnMoney4HQXBatchFeePrint";
	}
	
	
	/**
	 * 获取user的名称
	 * @param Users
	 * @return
	 */
	private String getNames(List<User> users) {
		String res = " ";
		int n = 0;
		if(users == null || users.size() == 0){
			return res;
		}
		for(User user : users){
			if(n==0){
				res = res.trim();
				res += user.getName();
				n++;
			} else {
				res += "," + user.getName();
			}
		}
		return res;
	}
	
	 /**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
    public static String digitUppercase(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = n < 0? "红字": ""; //负 -》红字
        n = Math.abs(n);  
        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";   
        }
        int integerPart = (int)Math.floor(n);
 
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
    
	
	
	/**
	 * 数字格式化
	 * @param text
	 * @return
	 */
	public String fmtMicrometer(String text){
		DecimalFormat df = null;
		df = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}
	
	
	/**
	 * @Description: 点击打印按钮后更新打印日期
	 * @author xinwei.wang
	 * @date 2015年12月10日下午9:47:59
	 * @param model
	 * @param request
	 * @param response
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "visaReturnMoneyBatchFeePrintAjax4Activiti")
	@ResponseBody
	@Transactional
	public Map<String, Object> visaReturnMoneyBatchFeePrintAjax4Activiti(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//String revid = request.getParameter("revid");
		String printDatestr = request.getParameter("printDate");
		
		String batchno = request.getParameter("batchno");
		VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"1");
		
		if (null!=visaFlowBatchOpration&&!"1".equals(visaFlowBatchOpration.getPrintStatus())) {
			//Date printDate = new Date();
			//model.addAttribute("printDate",printDate );
			
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("SELECT revn.batch_no AS batchno, revn.create_by AS createBy, revn.id AS reviewid, revn.order_id AS orderid, ");
    		buffer.append(" revn.traveller_id AS travelerid, revn.traveller_name AS travelername ");
    		buffer.append("FROM review_new revn, visa_flow_batch_opration vfbo ");
    		buffer.append("WHERE revn.batch_no = vfbo.batch_no and revn.process_type = '4' ");
    		buffer.append(" AND vfbo.busyness_type = 1 AND vfbo.batch_no=");
    		buffer.append("'"+batchno+"'");
    		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		
    		Date printDate = null;
    		try {
    			
    			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
    			printDate = simpleDateFormat.parse(printDatestr);
    			
    			if (null!=list&&list.size()>0) {
    				for (Map<String, Object> map2 : list) {
    					//visaReturnReceiptService.updateReviewPrintInfoById(Long.parseLong((Integer)map2.get("reviewid")+""),printDate);
    					//processReviewService.updatePrintFlag(userId, printFlag, reviewId);
    					processReviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", map2.get("reviewid").toString());
    				}
    				visaFlowBatchOprationDao.updateVisaFlowBatchPrintTimeAndPrintStatus(batchno, "1", printDate);
    			}
    			
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("签证费还款单日期格式化错", e);
				throw e;
			} catch (Exception e) {
				map.put("result",2);
				e.printStackTrace();
				
			}
		}
		
		//map.put("error", "签证借款申请失败！");
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	
	/**
	 * @Description: 打印签证费还款单
	 * @author xinwei.wang
	 * @date 2015年12月11日上午9:47:11
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception    
	 * @throws
	 */
	@RequestMapping(value="batchDownloadVisaReturnMoneySheet4Activiti")
	public ResponseEntity<byte[]> batchDownloadVisaReturnMoneySheet4Activiti(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		String batchno = request.getParameter("batchno");
		File file = activityVisaReturnReceiptService.createBatchVisaReturnMoneySheetDownloadFile4Activiti(revid,batchno);
		
		//签证费借款单生成后,更新借款单 的  打印状态和首次打印时间
/*		if(file != null && file.exists()){
			visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid));
		} */
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证费批次还款单" + nowDate + ".doc";
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
	
	
	
	//-----------------------签证费    还款单结束 ------------------------
	

}
