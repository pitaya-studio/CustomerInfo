package com.trekiz.admin.review.borrowing.visahqxborrowmoney.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.order.formBean.PrintParamBean;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.RefundService;
//import com.trekiz.admin.modules.review.visaborrowmoney.web.VisaBorrowMoneyController;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
//import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
//import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.borrowing.airticket.web.ActivityAirTicketOrderLendMoneyController;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexService;

import freemarker.template.TemplateException;

/**  
 * @Title: ActivityVisaHQXBorrowMoneyService.java
 * @Package com.trekiz.admin.review.borrowing.visahqxborrowmoney.service
 * @Description: 环球行签证借款service
 * @author xinwei.wang  
 * @date 2015-2015年11月19日 上午11:29:05
 * @version V1.0  
 */
@Service
@Transactional(readOnly = true)
public class ActivityVisaHQXBorrowMoneyService extends BaseService{
	
	private static final Logger log = Logger.getLogger(ActivityAirTicketOrderLendMoneyController.class);
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy 年 MM 月 dd 日");
	
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private ReviewService processReviewService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
//	@Autowired
//	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	@Autowired
	private ReviewMutexService reviewMutexService;

	@Autowired
	private RefundService refundService;
	
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	@Autowired
	private CurrencyService currencyService;

	/**
	 * @Description: 签证批量借款
	 * @author xinwei.wang
	 * @date 2015年11月19日下午2:30:16
	 * @param batchNo
	 * @param visaIds  签证id 以','分割
	 * @param travellerIds  游客id 以','分割
	 * @param persons  借款人 以','分割
	 * @param dates  日期 以','分割
	 * @param moneys  金额 以','分割
	 * @param others  备注 以','分割
	 * @return    
	 * @throws
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Map<String, Object> visaBatchJk4activiti(String batchNo,String visaIds, String travellerIds,
			String persons, String dates, String moneys, String others) {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap= null;
		
		String[] travelerIDS = travellerIds.split(",");
		String[] borrowAmounts = moneys.split(",");
		String[] borrowRemarks = others.split(",");
		String[] borrowTimes = dates.split(",");
		
		//String travellerIds1 = "1344,1532,";
		//String[] travelerIDS1 = travellerIds1.split(",");
		
		//在正式申请前处理   流程互斥hqx
		resultMap = processMutex4HQXVisaBorrowmoney(travelerIDS);
		if (null!=resultMap) {
			return resultMap;
		}
		
		
		//获取人民币币种id
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name,c.currency_exchangerate,c.convert_lowest FROM currency c WHERE 1=1");
		buffer.append("  AND c.del_flag = 0 AND c.create_company_id=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
		Integer borrowtotalcurrencyId = 0;
		BigDecimal borrowtotalcurrencyExchangerate = null;
		for (int i = 0; i < currencylist.size(); i++) {
			if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
				borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
				borrowtotalcurrencyExchangerate = new BigDecimal((currencylist.get(i).get("currency_exchangerate")).toString());
				break;
			}
		}
		
		User user =  UserUtils.getUser();
		String userId = user.getId().toString();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		
	    /*
	     * 同一批次的借款用同一个批次号，作为业务数据保存在表review_detail中，mykey的值为visaBorrowMoneyBatchNo
	     * 批次中多模板如何选取
	     * 取第一个游客所在  订单  关联产品    的    deptId， 以用之来确定审核模板。 
	     */
		Traveler travelerfirst = travelerService.findTravelerById(Long.parseLong(travelerIDS[0]));
		Long deptId = visaOrderService.getProductPept(travelerfirst.getOrderId());
		
		try{
			    /*
			     * 签证批量借款
			     * 同一批次的借款用同一个批次号，作为业务数据保存在表review_new表extend_1中，mykey的值为  batchNo
			     */
		        for (int i = 0; i < travelerIDS.length; i++) {
		        	//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
		        	if (!"0".equals(travelerIDS[i])) { 
		        		//申请时的addReview 要用添加dept
		        		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
		        		VisaOrder visaOrder = visaOrderService.findVisaOrder(traveler.getOrderId());
		        		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		        		
		        		//List<Detail> listDetail = new ArrayList<Detail>();
		        		Map<String, Object> hqxborrowMoneyvariables = new HashMap<String, Object>();
		        		hqxborrowMoneyvariables.put("borrowAmount", borrowAmounts[i]);
		        		/**
		        		 * 默认取借款申请者所在渠道的人民币为借款币种
		        		 */
		        		hqxborrowMoneyvariables.put("currencyId", borrowtotalcurrencyId+"");//默认借款币种为RMB
		        		hqxborrowMoneyvariables.put("borrowRemark", borrowRemarks[i]);
		        		hqxborrowMoneyvariables.put("borrowTime", borrowTimes[i]);//新增借款时间
		        		hqxborrowMoneyvariables.put("visaBorrowMoneyBatchNo", batchNo);
		        		hqxborrowMoneyvariables.put("borrowtotalcurrencyExchangerate", borrowtotalcurrencyExchangerate);
		        		 
		        		
		        		//收集审核主表记录保信息    -------- 后曾字段-------
		        		Map<String, Object> variables = new HashMap<String, Object>();
		        		variables.putAll(hqxborrowMoneyvariables);//添加游客借款相关信息
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, visaOrder.getAgentinfoId()); //渠道id
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, traveler.getOrderId()); //订单id
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "6"); //产品类型
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,visaOrder.getVisaProductId()); // 产品Id
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProducts.getProductName());
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo()); // 订单编号
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaOrder.getGroupCode()); //订单团号
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, visaOrder.getCreateBy().getId()); //计调
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName()); //计调姓名
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, visaOrder.getCreateBy().getId());//下单人
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName());//下单人姓名
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, visaOrder.getSalerId());//销售
		        		if(null!=visaOrder.getSalerId()){
		        			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, UserUtils.getUser(visaOrder.getSalerId().longValue()).getName());//销售姓名
		        		}else{
		        			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, "");//销售姓名
		        		}
		        			
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, traveler.getId()); //游客id
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, traveler.getName()); //游客姓名
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_BATCH_NO, batchNo); //批次号
		        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, borrowRemarks[i]); //游客借款备注
		        		
		        		//如有没申请成功的直接返回
		        	    //processReviewService.start(userId, companyId, checker, businessKey, productType*6, processType*5, deptId23, comment, variables, callbackService);
		        	    ReviewResult result = processReviewService.start(userId, companyId, permissionChecker, "", Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, deptId, "", variables);
		        	    
		    			//审核申请金额保存到新审批流程金额表
		        	    if (result.getSuccess()) {
		        	    	 NewProcessMoneyAmount newProcessMoneyAmount = new NewProcessMoneyAmount();
			    			 newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
			    			 newProcessMoneyAmount.setCurrencyId(borrowtotalcurrencyId);
			    			 newProcessMoneyAmount.setAmount(new BigDecimal(borrowAmounts[i]));
			    			 newProcessMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_BORROWMONEY);
			    			 newProcessMoneyAmount.setOrderType(6);
			    			 newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
			    			 newProcessMoneyAmount.setCreateTime(new Date());
			    			 newProcessMoneyAmount.setExchangerate(borrowtotalcurrencyExchangerate);
			    			 newProcessMoneyAmount.setReviewId(result.getReviewId());
			    			 newProcessMoneyAmount.setCompanyId(companyId);
			    			 processMoneyAmountService.saveNewProcessMoneyAmount(newProcessMoneyAmount);
						}
		        	   
		        	    
		        	    if(resultMap==null){
		 				   resultMap = new HashMap<String,Object>();
		 			    }
		        	    if (!result.getSuccess()) {
		        		   resultMap.put("msg", result.getMessage());
		        		   return resultMap;
					    }
				   }
				}
		        
		 }catch(Exception e){
			 e.printStackTrace();
			 if(resultMap==null){
				 resultMap = new HashMap<String,Object>();
			 }
			 resultMap.put("msg", "批量借款申请失败！");
		 }
		
		
		return resultMap;
	}
	
	
	/**
	 * @Description: 环球行签证借款处理流程互斥 的 方法,规则如下
	 *               
	 * @author xinwei.wang
	 * @date 2015年12月14日下午3:02:42
	 * @param travelerIDS
	 * @return    
	 * @throws
	 */
	private Map<String, Object>  processMutex4HQXVisaBorrowmoney(String[] travelerIDS){
		Map<String,Object> resultMap= null;	
		for (int i = 0; i < travelerIDS.length; i++) {
        	//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
        	if (!"0".equals(travelerIDS[i])) { 
        		//申请时的addReview 要用添加dept
        		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
        		VisaOrder visaOrder = visaOrderService.findVisaOrder(traveler.getOrderId());
        		CommonResult commonResult = reviewMutexService.check(visaOrder.getId().toString(),traveler.getId().toString() , "6", "5", false);
        		if (!commonResult.getSuccess()) {
        			 if(resultMap==null){
		 				   resultMap = new HashMap<String,Object>();
		 			 }
        			resultMap.put("msg", commonResult.getMessage());
        			break;
				}
        	}
		}
		return resultMap;
	}
	
	/**
	 * @Description: 生成签证借款单文件
	 * @author xinwei.wang
	 * @date 2015年12月10日上午11:53:45
	 * @param revid : 新审核Id
	 * @param batchno ： 批次号
	 * @return
	 * @throws IOException
	 * @throws TemplateException    
	 * @throws
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public File createBatchVisaBorrowMoneySheetDownloadFile4Activiti(String revid,String batchno, String payId, String option) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();


		//获取activiti审核信息
		Map<String,Object>  reviewAndDetailInfoMap = null;
		try{
			if(revid!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
			}
		}catch(Exception e){
			log.error("根据revid： " + revid + " 查询出来reviewDetail明细报错 ",e);
		}
						
		if (reviewAndDetailInfoMap != null) {
		
			root.put("revCreateDate", sdf.format(reviewAndDetailInfoMap.get("createDate")));// 填写日期
			// --- wangxinwei 20151008 added：处理确认付款日期
			root.put("revUpdateDate", sdf.format(reviewAndDetailInfoMap.get("updateDate")));// 更新日期
			root.put("revBorrowRemark",reviewAndDetailInfoMap.get("borrowRemark"));// 申报原因
			
			/**
			 * 经办人显示应为产品发布人员/
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId").toString();
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);
			
			String operatorName = reviewAndDetailInfoMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME).toString();
			
			if (null != operatorName) {
				root.put("operatorName", operatorName);// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			
			if("order".equals(option)) {
				//获取批次借款金额
				VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"2");
				String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();
				
				root.put("revBorrowAmount", MoneyNumberFormat.getThousandsMoney(Double.valueOf(batchborrowtotalMoney), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 借款金额   改为批次的借款金额
				root.put("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(batchborrowtotalMoney)));// 借款金额大写   改为批次借款金额
			}
			
			
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				root.put("payStatus","0");
			}else {
				root.put("payStatus", reviewAndDetailInfoMap.get("payStatus").toString());
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
			root.put("revBorrowAmount", revBorrowAmount);
			root.put("revBorrowAmountDx", revBorrowAmountDx);
		}
			
		/**
		 * 通过性方式获取审核人职务 
		 */
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
		
		//取最后一个审批人	
		root.put("majorCheckPerson", general_manager);//复合，主管审批  都是最后一个的审批人
		
		root.put("cwmanager", financial_executive);//财务主管		
		root.put("cw", financial);	
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		root.put("cashier", cashier);//出纳		
		root.put("deptmanager", reviewer);//部门经理（审核）
				
	   return FreeMarkerUtil.generateFile("visabatchborrowmoney4activiti.ftl", "visabatchborrowmoney.doc", root);
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
	 * 把原有的在Controller里面的新审批的签证打印方法，提取到Service层里面，为了进行批量打印。该方法获取一个打印信息的map，
	 * 供总控方法使用。
	 * @param paramBean
	 * @return
     */
	public Map<String,Object> getVisaBorrowMoney4HQXBatchFeePrint(PrintParamBean paramBean){
		Map<String,Object> result = new HashMap<>(32,0.75f); // 初始容量32
		String revId = paramBean.getReviewId();
		String batchNo = paramBean.getBatchNo();
		String isPrintFlag = paramBean.getIsPrintFlag();
		String payId = paramBean.getPayId();
		String option = paramBean.getOption();

		result.put("revid", revId); //把revid 传到模板模板的html页面，下载模板时使用
		result.put("batchno",batchNo);
		result.put("isPrintFlag",isPrintFlag);
		result.put("payId",payId);
		result.put("option",option);
		result.put("reviewFlag",2);

		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		//获取activiti审核信息
		if(StringUtils.isNotBlank(revId)) {
			Map<String, Object> reviewDetail = processReviewService.getReviewDetailMapByReviewId(revId);
			if (reviewDetail != null){
				result.put("revCreateDate", reviewDetail.get("createDate"));
				result.put("revUpdateDate", reviewDetail.get("updateDate"));
				result.put("revBorrowRemark", reviewDetail.get("borrowRemark"));

				String orderId = reviewDetail.get("orderId").toString();
				VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
				VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
				result.put("groupCode", visaProducts.getGroupCode());
				result.put("productCreator", visaProducts.getCreateBy().getName());

				Object operatorName = reviewDetail.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME);
				operatorName = operatorName == null ? "未知" : operatorName;// 经办人、领款人都为借款申请人
				result.put("operatorName", operatorName);


				if ("order".equals(option)) {
					//获取批次借款金额,金额大写
					VisaFlowBatchOpration visaFlowBatchOperation = visaFlowBatchOprationDao.findByBatchNo(batchNo, "2");
					String totalMoney = visaFlowBatchOperation.getBatchTotalMoney();
					result.put("revBorrowAmount", MoneyNumberFormat.getThousandsMoney(Double.valueOf(totalMoney), MoneyNumberFormat.THOUSANDST_POINT_TWO));
					result.put("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(totalMoney)));
				}

				Object currencyId = reviewDetail.get("currencyId");
				if (null != currencyId) {
					Currency currency = currencyService.findCurrency(Long.parseLong(currencyId.toString()));
					result.put("revCurrency", currency);
				}

				Object printFlag = reviewDetail.get("printStatus");//printStatus //获取单条的打印状态：与整个批次的一样
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
				String printDateStr;
				if (null == printFlag || "0".equals(printFlag.toString())) {
					printDateStr = simpleDateFormat.format(new Date());
				} else {
					try {
						printDateStr = simpleDateFormat.format(reviewDetail.get("printDate"));//printDate
					} catch (Exception e) {
						e.printStackTrace();
						printDateStr = simpleDateFormat.format(new Date());
					}
				}
				result.put("printDate", printDateStr);

				//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则

				if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
					result.put("payStatus", "0");
				} else {
					result.put("payStatus", reviewDetail.get("payStatus"));
				}
			}
		}

		if("pay".equals(option)) {
			//45需求，借款金额以每次的支付金额为标准
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			if(com.trekiz.admin.common.utils.StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, Context.ORDER_STATUS_VISA);
				if (payDetail != null) {
					if (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() != BigDecimal.ZERO.doubleValue()) {
						revBorrowAmount = payDetail.getRefundRMBDispStyle();
						revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.parseDouble(revBorrowAmount.replaceAll(",", "")));// 借款金额大写
					}
				}
			}
			result.put("revBorrowAmount", revBorrowAmount);
			result.put("revBorrowAmountDx", revBorrowAmountDx);
		}

		// 获取单据审批人员Map,出纳以外的最后一个审批人：对签证借款流程来说level为3
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUuid, ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY, revId);
		List<User> generalManagers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.GENERAL_MANAGER);//总经理
		List<User> financialExecutives = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.FINANCIAL_EXECUTIVE);//财务主管
		List<User> cashiers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.CASHIER);//出纳
		List<User> financials = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.FINANCIAL);//总经理
		List<User> reviewers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.REVIEWER);//审核

		String generalManager = getNames(generalManagers); // 总经理
		String financialExecutive = getNames(financialExecutives); // 财务主管
		String cashier = getNames(cashiers); // 出纳
		String financial = getNames(financials); // 财务
		String reviewer = getNames(reviewers); // 审核

		result.put("cw",financial); // 处理财务的显示
		result.put("cashier",cashier); // 出纳
		result.put("majorCheckPerson",generalManager); // 最后一个环节审批人
		result.put("deptmanager",reviewer); // 部门经理（审核）
		result.put("cwmanager",financialExecutive); // 财务主管

		return result;
	}

	/**
	 * 更新签证借款付款新审批的打印状态。
	 * @param batchNo
	 * @param printDateStr
     */
	@Transactional(readOnly = false)
	public boolean updatePrintStatus(String batchNo,String printDateStr){
		Date printDate = DateUtils.dateFormat(printDateStr,"yyyy/ MM /dd HH:mm");
		if (printDate == null || StringUtils.isBlank(batchNo)){ // 验证
			return false;
		}

		Long userId = UserUtils.getUser().getId();
		VisaFlowBatchOpration batchOperation = visaFlowBatchOprationDao.findByBatchNo(batchNo,"2");
		visaFlowBatchOprationDao.updateVisaFlowBatchUpdateTime(batchNo,"2",new Date(),userId);

		if (batchOperation != null && !"1".equals(batchOperation.getPrintStatus())){
			List<Map<String,Object>> reviewInfo = visaFlowBatchOprationDao.getVisaFlowReviewNewInfo(batchNo);
			if (CollectionUtils.isNotEmpty(reviewInfo)){
				for (Map<String, Object> map : reviewInfo) {
					processReviewService.updatePrintFlag(userId.toString(), "1", map.get("reviewid").toString());
				}
				visaFlowBatchOprationDao.updateVisaFlowBatchPrintTimeAndPrintStatus(batchNo, "2", printDate);
			}
		}
		return true;
	}
}
