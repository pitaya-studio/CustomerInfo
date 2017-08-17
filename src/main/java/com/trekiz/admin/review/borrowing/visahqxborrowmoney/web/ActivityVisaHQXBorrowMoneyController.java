package com.trekiz.admin.review.borrowing.visahqxborrowmoney.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.trekiz.admin.modules.order.formBean.PrintParamBean;
import com.trekiz.admin.modules.review.visaborrowmoney.service.IVisaBorrowMoneyService;
import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.BatchTravelerRelation;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.Office;
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
import com.trekiz.admin.review.borrowing.airticket.web.ActivityAirTicketOrderLendMoneyController;
import com.trekiz.admin.review.borrowing.visahqxborrowmoney.service.ActivityVisaHQXBorrowMoneyService;

/**  
 * @Title: AcrivityVisaHQXBorrowMoneyController.java
 * @Package com.trekiz.admin.review.borrowing.visahqxborrowmoney.web
 * @Description: 环球行借款controller
 * @author xinwei.wang  
 * @date 2015-2015年11月19日 上午10:22:18
 * @version V1.0  
 */
@Controller
@RequestMapping(value = "${adminPath}/visa/hqx/borrowmoney")
public class ActivityVisaHQXBorrowMoneyController {
	
	
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
	private ActivityVisaHQXBorrowMoneyService activityVisaHQXBorrowMoneyService;
	@Autowired
	private ReviewService processReviewService;
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
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	
	@Autowired
	private RefundService refundService;

	@Autowired
	private IVisaBorrowMoneyService visaBorrowMoneyService;
		
	/**
	 * -------------------------------------需过滤什么样的游客符合规则----------------------
	 * @Description: 环球行签证批量借款申请页面
	 * @author xinwei.wang
	 * @date 2015年11月19日下午9:17:36
	 * @param request
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "checkBatchJkHqx")
	@ResponseBody
	public Object checkBatchJkHqx(HttpServletRequest request){
		String visaIds = request.getParameter("visaIds")+"0";
		String travelerIds = request.getParameter("travelerIds")+"0";
		//---0064需求,游客加入批次-s-批发商环球行--//
		String joinRefundDate=request.getParameter("joinRefundDate");
		//System.out.println("------"+joinRefundDate);
		//---0064需求,游客加入批次-e--//
		Map<String, Object> resultMap  = visaOrderService.checkBatchJkHqx4activiti(visaIds,travelerIds);
		
		//***270需求--添加"还款日期"项-s-将还款日期是否为必填的配置结果放入resultMap中****//
	    //TODO-270需求,临时性屏蔽,以后上用
		Office office=UserUtils.getUser().getCompany();
		//System.out.println("-----"+office.getIsMustRefundDate());
		resultMap.put("refundDateOption",office.getIsMustRefundDate());//配置结果:1-是,0-否.
		//***270需求--添加"还款日期"项-e****//	
		//---0064需求,游客加入批次-s-批发商环球行--//
		resultMap.put("joinRefundDate", joinRefundDate);
		//---0064需求,游客加入批次-e--//
		return resultMap;
	}
	
	
	/**
	 * @Description: 
	 *  1.创建借款记录
	 *  2.保存借款的批次信息
	 *  3.保存借款的游客与批次的关系信息
	 * @author xinwei.wang
	 * @date 2015年11月19日下午8:58:25
	 * @param req
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "batchJk4activiti")
	@ResponseBody
	public Object batchJk4activiti(HttpServletRequest req){
		Map<String, Object> resultMap = null;
		
		String type = req.getParameter("type");
		String visaIds = req.getParameter("visaIds") + "0";
		String orderIds = req.getParameter("orderIds") + "0";
		String travelerIds = req.getParameter("travelerIds") + "0";
		String persons = req.getParameter("passportOperator") + "0";
		String dates = req.getParameter("passportOperateTime") + "0";
		String moneys = req.getParameter("moneys") + "0";
		String others = req.getParameter("passportOperateRemark") + "0";
		// ***270需求--添加"还款日期"项-s****//
		// TODO-270需求临时性屏蔽以后上线用
		String refundDate = req.getParameter("refundDate");// 获得选择的还款日期
		// ***270需求--添加"还款日期"项-e****//
		String[] travelerIDS = travelerIds.split(",");
		String[] visaIDs = visaIds.split(",");
		String[] orderIDs = orderIds.split(",");
		String[] borrowAmounts = moneys.split(",");
		String[] borrowRemarks = others.split(",");
		// 生成批次号
		String batchNo = sysBatchNoService.getVisaBorrowMoneyBatchNo();
		// 批次总人数
		int batchPersonCount = travelerIDS.length - 1;
		// 批次总金额
		BigDecimal batchtotalMoney = BigDecimal.ZERO;
		for (int i = 0; i < travelerIDS.length - 1; i++) {
			// travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
			if (!"0".equals(travelerIDS[i])) {
				batchtotalMoney = batchtotalMoney.add(new BigDecimal(
						borrowAmounts[i]));
			}
		}
		// 获取人民币币种id
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
		buffer.append(" And c.del_flag = 0 AND c.create_company_id=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> currencylist = visaOrderDao.findBySql(
				buffer.toString(), Map.class);
		Integer borrowtotalcurrencyId = 0;
		String borrowtotalcurrencyName = "人民币";
		for (int i = 0; i < currencylist.size(); i++) {
			if ("￥".equals(currencylist.get(i).get("currency_mark"))
					|| "人民币".equals(currencylist.get(i).get("currency_name"))) {
				borrowtotalcurrencyId = (Integer) currencylist.get(i).get(
						"currency_id");
				borrowtotalcurrencyName = (String) currencylist.get(i).get(
						"currency_name");
				break;
			}
		}
		// 生成uuid
		String uuid = UUID.randomUUID().toString();

		// 1.创建审核记录
		if ("2".equals(type)) {

			try {
				resultMap = activityVisaHQXBorrowMoneyService
						.visaBatchJk4activiti(batchNo, visaIds, travelerIds,
								persons, dates, moneys, others);
			} catch (Exception e) {
				if (resultMap == null) {
					resultMap = new HashMap<String, Object>();
				}
				resultMap.put("msg", "流程启动失败!");
				return resultMap;
			}
		}

		VisaFlowBatchOpration record = new VisaFlowBatchOpration();
		record.setUuid(uuid);
		record.setBatchNo(batchNo);
		record.setBusynessType("2");
		record.setBatchPersonCount(batchPersonCount);
		record.setBatchTotalMoney(batchtotalMoney.toString());
		record.setCreateUserId(UserUtils.getUser().getId());
		record.setCreateUserName(UserUtils.getUser().getName());
		record.setCreateTime(new Date());
		// ----------------0064----批次初始更新时间为创建时间为当前系统时间-s----//
		record.setUpdateTime(new Date());
		// ----------------0064----批次初始更新时间为创建时间为当前系统时间-e----//
		record.setPrintStatus("0");
		// 270-qyl-begin把还款日期保存到后台
		try {
			// --0064需求处理还款日期为空的情况.
			if (StringUtils.isNoneEmpty(refundDate)) {
				record.setRefundDate(new SimpleDateFormat("yyyy-MM-dd")
						.parse(refundDate));
			} else {
				record.setRefundDate(null);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 270-qyl-end
		record.setCurrencyId(borrowtotalcurrencyId);
		record.setCurrencyName(borrowtotalcurrencyName);
		record.setIsNewReview(2);// 用于表示是新审核
		if ("1".equals(type)) {
			record.setReviewStatus("99");
			record.setIsSubmit("1");
		} else if ("2".equals(type)) {
			record.setReviewStatus("1");
			record.setIsSubmit("2");
		}

		// 2.创建批次借款记录
		batchRecordDao.getSession().save(record);

		// 3.保存游客与批次的关系
		for (int i = 0; i < travelerIDS.length - 1; i++) {
			// travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
			if (!"0".equals(travelerIDS[i])) {
				uuid = UUID.randomUUID().toString();
				BatchTravelerRelation relation = new BatchTravelerRelation();
				relation.setUuid(uuid);
				relation.setBatchUuid(record.getUuid());
				relation.setBatchRecordNo(batchNo);
				relation.setTravelerId(Long.parseLong(travelerIDS[i]));
				relation.setVisaId(Long.parseLong(visaIDs[i]));
				relation.setOrderId(Long.parseLong(orderIDs[i]));
				Traveler traveler = travelerService.findTravelerById(Long
						.parseLong(travelerIDS[i]));
				relation.setTravelerName(traveler.getName());
				relation.setBusinessType(1);// 业务类型 1:借款 2：还收据 3：借护照 4：还护照
				relation.setTravellerBorrowMoney(new BigDecimal(
						borrowAmounts[i]));
				relation.setRemark(borrowRemarks[i]);
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
		
		return resultMap;
	}
	
	
	
	/**
	 * tempdesc：BJH151014003 有记录
	 * @Description: 签务签证订单-借款记录,新行者签证借款记录单
	 * @author xinwei.wang
	 * @date 2015年11月18日上午10:05:38
	 * @param request
	 * @param response
	 * @param model
	 * @param orderId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "borrowMoneyRecord4HQXactivity")
	public String borrowMoneyRecord4HQXactivity( HttpServletRequest request, HttpServletResponse response, Model model, String orderId) {
		VisaOrder order = visaOrderService.findVisaOrder(Long.valueOf(orderId));
		//Long pid = order.getVisaProductId();
		Long deptId = visaOrderService.getProductPept(Long.valueOf(orderId));//通过orderId获取产品的发布部门
		                                       //processReviewService.getReviewDetailMapListByOrderId(deptId, productType, processType, orderId, orderByProperty, orderByDirection)
		List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(deptId, 6, 5, order.getId().toString(),OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
		
		List<Map<String, String>> borrowMoneyRecordList = new ArrayList<Map<String, String>>();
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
				String borrowAmount = df.format(new BigDecimal((String)map.get("borrowAmount")));
				reviewAndDetailInfoMap.put("borrowAmount", borrowAmount);
				//5. 申请人
				reviewAndDetailInfoMap.put("createBy", (String)(map.get("createBy")));
				
				//6. 借款审批状态
				reviewAndDetailInfoMap.put("status", (map.get("status").toString()));
				//8.其他
				reviewAndDetailInfoMap.put("orderId", orderId);//订单id
				reviewAndDetailInfoMap.put("id", (map.get("id").toString()));//review_new id  procInstId
				reviewAndDetailInfoMap.put("travellerId", (map.get("travellerId").toString()));//review_new id  procInstId
				
				borrowMoneyRecordList.add(reviewAndDetailInfoMap);
			}
		}
	
		// 公司ID
		model.addAttribute("companyId",UserUtils.getUser().getCompany().getId());
		// 订单ID
		model.addAttribute("orderId", orderId);
		// 借款记录List
		model.addAttribute("borrowMoneyRecordList", borrowMoneyRecordList);
		
		return "review/borrowing/visahqx/borrowMoneyRecord4HQXactivity";
	}
	

	/**
	 * @Description:签证借款审批详情页4activiti
	 * @author xinwei.wang
	 * @date 2015年11月20日上午12:23:33
	 * @param model
	 * @param request
	 * @param response
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "visaBorrowMoney4HQXReviewDetail")
	public String visaBorrowMoney4HQXReviewDetail(Model model,
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
		model.addAttribute("country", country);
		model.addAttribute("collarZoning", collarZoning);
		
		//游客相关信息
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
		model.addAttribute("traveler", traveler);
		
		//签证相关信息
		Visa visa = visaService.findVisaByTravlerId(Long.parseLong(travelerId));
		Dict visaStauts = dictService.findByValueAndType(visa.getVisaStauts()+"", "visa_status");
		model.addAttribute("visa", visa);
		model.addAttribute("visaStauts", visaStauts);
		
		
		//获取activiti审核信息
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
			model.addAttribute("revBorrowRemark", reviewAndDetailInfoMap.get("borrowRemark"));//申报原因
			model.addAttribute("revBorrowAmount", reviewAndDetailInfoMap.get("borrowAmount"));//收据金额
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
		
		return "review/borrowing/visahqx/visaBorrowMoney4HQXReviewDetail";
	}
	
	
	//--------------打印下载相关  beggin----------------
	
	/**
	 * @Description: 签证费借款单  批次
	 * @author xinwei.wang
	 * @date 2015年12月8日下午8:32:15
	 * @param model
	 * @param request
	 * @param response
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "visaBorrowMoney4HQXBatchFeePrint")
	public String visaBorrowMoney4HQXBatchFeePrint(Model model,
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
		String isPrintFlag = request.getParameter("isPrintFlag");
		String payId = request.getParameter("payId");	
		String option = request.getParameter("option");
		
		model.addAttribute("revid", revid);//把revid 传到模板模板的html页面，下载模板时使用
		model.addAttribute("batchno", batchno);
		model.addAttribute("isPrintFlag", isPrintFlag);
		model.addAttribute("payId", payId);
		model.addAttribute("option", option);
				
		//获取activiti审核信息
		if(StringUtils.isNotBlank(revid)) {
			Map<String,Object>  reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
			if (reviewAndDetailInfoMap != null) {
				model.addAttribute("revCreateDate",(Date)reviewAndDetailInfoMap.get("createDate"));// 填写日期createDate				
				//----- wxw added 20151008 ----- 需求C221， 处理付款确认时间
				model.addAttribute("revUpdateDate",(Date)reviewAndDetailInfoMap.get("updateDate"));// 更新日期updateDate				
				model.addAttribute("revBorrowRemark",reviewAndDetailInfoMap.get("borrowRemark"));// 申报原因
				
				/**
				 * 经办人显示应为产品发布人员
				 */
				String orderid = reviewAndDetailInfoMap.get("orderId").toString();
				VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
				VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
				String productCreater = visaProducts.getCreateBy().getName();
				model.addAttribute("groupCode",visaProducts.getGroupCode());// 团号
				model.addAttribute("productCreater", productCreater);
								
				String operatorName = reviewAndDetailInfoMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME).toString();				
				if (null != operatorName) {
					model.addAttribute("operatorName", operatorName);// 经办人、领款人都为借款申请人
				} else {
					model.addAttribute("operatorName", "未知");
				}
				
				if("order".equals(option)) {
					//获取批次借款金额
					VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"2");
					String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();
					
					model.addAttribute("revBorrowAmount", MoneyNumberFormat.getThousandsMoney(Double.valueOf(batchborrowtotalMoney), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 借款金额   改为批次总金额
					model.addAttribute("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(batchborrowtotalMoney)));// 借款金额大写  改为批次总金额
				}
				
				String currencyId = reviewAndDetailInfoMap.get("currencyId").toString();
				if (null != currencyId) {
					Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
					model.addAttribute("revCurrency", currency);
				}
				
				String printFlag = reviewAndDetailInfoMap.get("printStatus").toString();//printStatus //获取单条的打印状态：与整个批次的一样			
				if (null==printFlag||"0".equals(printFlag)) { 
					Date printDate = new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
					String printDateStr = simpleDateFormat.format(printDate);
					model.addAttribute("printDate",printDateStr );
				}else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
					String printDateStr = null;
					try {
						   printDateStr = simpleDateFormat.format(reviewAndDetailInfoMap.get("printDate"));//printDate
					} catch (Exception e) {
						e.printStackTrace();
						printDateStr = simpleDateFormat.format(new Date()); 
					}
					model.addAttribute("printDate",printDateStr);
				}
				
				//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
				String companyUuid = UserUtils.getUser().getCompany().getUuid();
				if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
					model.addAttribute("payStatus","0");
				}else {
					model.addAttribute("payStatus", reviewAndDetailInfoMap.get("payStatus"));
				}
			}			
		}			
		
		if("pay".equals(option)) {
			//45需求，借款金额以每次的支付金额为标准
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			if(StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, Context.ORDER_STATUS_VISA);
				if (payDetail != null) {
					if (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
						revBorrowAmount = payDetail.getRefundRMBDispStyle();
						revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.parseDouble(revBorrowAmount.replaceAll(",", "")));// 借款金额大写
					}
				}
			}
			model.addAttribute("revBorrowAmount", revBorrowAmount);
			model.addAttribute("revBorrowAmountDx", revBorrowAmountDx);
		}
		
		
		//出纳以外的最后一个审批人：对签证借款流程来说level为3	
		String companyUUid = UserUtils.getUser().getCompany().getUuid();
		//获取单据审批人员Map
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY, revid);//e5dbd01ec2f649e39d458540a91aa03b
		List<User> general_managers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.GENERAL_MANAGER);//总经理
		List<User> financial_executives = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.FINANCIAL_EXECUTIVE);//财务主管
		List<User> cashiers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.CASHIER);//出纳
		List<User> financials = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.FINANCIAL);//总经理
		List<User> reviewers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.REVIEWER);//审核
		String general_manager = getNames(general_managers);//总经理 
		String financial_executive = getNames(financial_executives);//财务主管
		String financial = getNames(financials);//财务
		String cashier = getNames(cashiers);//出纳
		String  reviewer = getNames(reviewers);//审核
		
		model.addAttribute("cw", financial); // 处理财务的显示		
		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		model.addAttribute("cashier", cashier);//出纳		
		model.addAttribute("majorCheckPerson",general_manager);//最后一个环节审批人		
		model.addAttribute("deptmanager", reviewer);//部门经理（审核）		
		model.addAttribute("cwmanager", financial_executive);//财务主管		
		
		return "review/borrowing/visahqx/visaBorrowMoney4HQXBatchFeePrint";
	}
	
	/**
	 * 获取user的名称
	 * @param users
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
     * @Description: 签证费批次借款单: 在点击打印或下载按钮时更新打印状态  和   打印时间
     * @author xinwei.wang
     * @date 2015年12月10日上午9:46:56
     * @param model
     * @param request
     * @param response
     * @return    
     * @throws
     */
	@RequestMapping(value = "visaBorrowMoneyBatchFeePrintAjax4Activiti")
	@ResponseBody
	@Transactional
	public Map<String, Object> visaBorrowMoneyBatchFeePrintAjax4Activiti(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String printDatestr = request.getParameter("printDate");
		String batchno = request.getParameter("batchno");
		VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"2");
		
		
		visaFlowBatchOprationDao.updateVisaFlowBatchUpdateTime(batchno, "2", new Date(), UserUtils.getUser().getId());//不知？？？
		//第一次打印需要更新状态
        if (null!=visaFlowBatchOpration&&!"1".equals(visaFlowBatchOpration.getPrintStatus())) {
    		
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("SELECT revn.batch_no AS batchno, revn.create_by AS createBy, revn.id AS reviewid, revn.order_id AS orderid, ");
    		buffer.append(" revn.traveller_id AS travelerid, revn.traveller_name AS travelername ");
    		buffer.append("FROM review_new revn, visa_flow_batch_opration vfbo ");
    		buffer.append("WHERE  revn.batch_no = vfbo.batch_no ");
    		buffer.append(" AND vfbo.busyness_type = 2 AND vfbo.batch_no=");
    		buffer.append("'"+batchno+"'");
    		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
    		
    		Date printDate = null;
    		try {
    			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
    			printDate = simpleDateFormat.parse(printDatestr);
    			
    			if (null!=list&&list.size()>0) {
    				for (Map<String, Object> map2 : list) {
    					processReviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", map2.get("reviewid").toString());//(Long.parseLong((Integer)map2.get("reviewid")+""),printDate);
    				}
    				visaFlowBatchOprationDao.updateVisaFlowBatchPrintTimeAndPrintStatus(batchno, "2", printDate);
    			}
    			
    		} catch (NumberFormatException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			log.error("签证费借款单日期格式化错", e);
    			throw e;
    		} catch (Exception e) {
    			map.put("result",2);
    			e.printStackTrace();
    		}	
			
		}
			
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}

	/**
	 * 签证批量付款，批量打印付款单时，点击打印进行批量更新打印状态的方法。 yudong.xu 2016.10.27
	 * @param request
	 * @return
     */
	@RequestMapping(value = "batchUpdatePrintStatus")
	@ResponseBody
	public Map<String, Object> batchUpdatePrintStatus(HttpServletRequest request) {
		Map<String, Object> info = new HashMap<>();
		String updateInfo = request.getParameter("updateInfo");
		JSONArray updateArr = JSONArray.parseArray(updateInfo);
		if (updateArr == null){
			info.put("result",2);//1成功   2为申请失败
			return info;
		}

		boolean isOK; // 更新标识
		for (int i = 0; i < updateArr.size(); i++) {
			String itemStr = updateArr.getString(i);
			String[] itemArr = itemStr.split("_");
			String reviewFlag = itemArr[0];
			String batchNo = itemArr[1];
			String printDateStr = itemArr[2];

			if ("1".equals(reviewFlag)){
				isOK = visaBorrowMoneyService.updatePrintStatus(batchNo, printDateStr);
			} else {
				isOK = activityVisaHQXBorrowMoneyService.updatePrintStatus(batchNo, printDateStr);
			}

			if (!isOK){
				info.put("result",2);//1成功   2为申请失败
				return info;
			}
		}

		info.put("result",1);//1成功   2为申请失败
		return info;
	}
	
	
	/**
	 * @Description: 新审核：签证费借款单下载
	 * @author xinwei.wang
	 * @date 2015年12月10日上午11:50:33
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception    
	 * @throws
	 */
	@RequestMapping(value="batchdownloadVisaBorrowMoneySheet4Activiti")
	public ResponseEntity<byte[]> batchdownloadVisaBorrowMoneySheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		String batchno = request.getParameter("batchno");
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		File file = activityVisaHQXBorrowMoneyService.createBatchVisaBorrowMoneySheetDownloadFile4Activiti(revid,batchno, payId, option);
		
		//签证费借款单生成后,更新借款单 的  打印状态和首次打印时间
/*		if(file != null && file.exists()){
			visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid));
		} */
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证费借款单" + nowDate + ".doc";
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


	@RequestMapping(value="visaBorrowMoneyBatchPrint")
	public String visaBorrowMoneyBatchPrint(HttpServletRequest request){
		List<Map<String,Object>> printList = new ArrayList<>();

		String printInfo = request.getParameter("printInfo");
		List<PrintParamBean> paramBeanList = parseBatchPrintParam(printInfo);

		for (PrintParamBean paramBean : paramBeanList) {
			Map<String,Object> printMap;
			if (paramBean.getReviewFlag() == 1){ // 老审批
				printMap = visaBorrowMoneyService.getVisaBorrowMoneyBatchFeePrint(paramBean);
				printList.add(printMap);
			}else if (paramBean.getReviewFlag() == 2){ // 新审批
				printMap = activityVisaHQXBorrowMoneyService.getVisaBorrowMoney4HQXBatchFeePrint(paramBean);
				printList.add(printMap);
			}
		}
		request.setAttribute("printList",printList);
		return "review/borrowing/visahqx/visaBorrowMoney4HQXBatchPrint";
	}

	private List<PrintParamBean> parseBatchPrintParam(String printInfo){
		List<PrintParamBean> paramBeanList = new ArrayList<>();
		if (StringUtils.isBlank(printInfo)){
			return paramBeanList;
		}

		String[] printArr = printInfo.split(",");
		for (int i = 0; i < printArr.length; i++) {
			String printItem = printArr[i];
			String[] paramArr = printItem.split("_");

			PrintParamBean paramBean = new PrintParamBean();
			paramBean.setBatchNo(paramArr[0]); // 和前端传入的数据顺序一致
			paramBean.setOrderType(Integer.parseInt(paramArr[1]));
			paramBean.setReviewFlag(Integer.parseInt(paramArr[2]));
			paramBean.setReviewId(paramArr[3]);
			paramBean.setIsPrintFlag(paramArr[4]);
			paramBean.setOption("order");
			paramBeanList.add(paramBean);
		}
		return paramBeanList;
	}
}
