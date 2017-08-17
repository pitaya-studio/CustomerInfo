package com.trekiz.admin.review.returnvisareceipt.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
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
 * @Title: ActivityVisaReturnReceiptService.java
 * @Package com.trekiz.admin.review.returnvisareceipt.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xinwei.wang  
 * @date 2015-2015年12月3日 下午12:03:00
 * @version V1.0  
 */
@Service
@Transactional(readOnly = true)
public class ActivityVisaReturnReceiptService extends BaseService{
	
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
	@Autowired
	private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	@Autowired
	private ReviewMutexService reviewMutexService;
	
	

	
	/**
	 * 签证批量还收据
	 * xinwei.wang  added
	 * @param visaIds：签证Ids 结构[]
	 * @param travellerIds：游客Ids
	 * @param returnReceiptJe：还收据金额，结构[56,78,0]
	 * @param returnReceiptName：还收据人
	 * @param returnReceiptTime：归还时间
	 * @param returnReceiptRemark：备注信息
	 * @return
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Map<String, Object> visaBatchHsj4activiti(String batchNo, String visaIds,
			String travellerIds, String returnReceiptJe,
			String returnReceiptName, String returnReceiptTime,
			String returnReceiptRemark,String borrowamount) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = null;
		String[] travelerIDS = travellerIds.split(",");
		String[] returnReceiptNames = returnReceiptName.split(",");
		String[] returnReceiptJes = returnReceiptJe.split(",");
		String[] returnReceiptTimes = returnReceiptTime.split(",");
		String[] returnReceiptRemarks = returnReceiptRemark.split(",");
		String[] borrowamounts = borrowamount.split(",");
		// String batchNo = sysBatchNoService.getVisaBorrowMoneyBatchNo();
		
		//String travellerIds1 = "1542,1539,";
		//String[] travelerIDS1 = travellerIds1.split(",");
		
		//在正式申请前处理   流程互斥xxz
		resultMap = processMutex4HQXVisaReturnReceipt(travelerIDS);
		if (null!=resultMap) {
			return resultMap;
		}
		
		
		//获取人民币币种id
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name,c.currency_exchangerate,c.convert_lowest FROM currency c WHERE 1=1");
		buffer.append(" AND c.create_company_id=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
		Integer currencyId = 0;
		BigDecimal currencyExchangerate = null;
		for (int i = 0; i < currencylist.size(); i++) {
			if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
				currencyId = (Integer)currencylist.get(i).get("currency_id");
				currencyExchangerate = new BigDecimal((currencylist.get(i).get("currency_exchangerate")).toString());
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
		
		try {
			/*
			 * 同一批次的还收据用同一个批次号，作为业务数据保存在表review_detail中，
			 * mykey的值为visaReturnReceiptBatchNo
			 */
			//String batchNo = sysBatchNoService.getVisaReturnReceiptBatchNo();
			// System.out.println(2/0);
			// 签证批量借款
			for (int i = 0; i < travelerIDS.length; i++) {
				// travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
				if (!"0".equals(travelerIDS[i])) {

					// 申请时的addReview 要用添加dept的方法
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
					VisaOrder visaOrder = visaOrderService.findVisaOrder(traveler.getOrderId());
	        		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
	        		
                    //还收据申请业务数据
					Map<String, Object> hqxreturnreceiptvariables = new HashMap<String, Object>();
					hqxreturnreceiptvariables.put("receiptAmount", returnReceiptJes[i]);
					hqxreturnreceiptvariables.put("currencyId", currencyId.toString());// 默认收据币种为RMB
					hqxreturnreceiptvariables.put("currencyExchangerate", currencyExchangerate);// 默认收据币种为RMB
					hqxreturnreceiptvariables.put("receiptor", returnReceiptNames[i]);
					hqxreturnreceiptvariables.put("returnTime", returnReceiptTimes[i]);
					hqxreturnreceiptvariables.put("returnReceiptRemark",returnReceiptRemarks[i]== null ? "": returnReceiptRemarks[i]);
					hqxreturnreceiptvariables.put("borrowAmount_return",borrowamounts[i]== null ? "": borrowamounts[i]);
					hqxreturnreceiptvariables.put("visaReturnReceiptBatchNo", batchNo);


					//收集审核主表记录保信息    ------- 后曾字段-------
	        		Map<String, Object> variables = new HashMap<String, Object>();
	        		variables.putAll(hqxreturnreceiptvariables);//添加游客还收据申请业务数据
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, visaOrder.getAgentinfoId()); //渠道id
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, traveler.getOrderId()); //订单id
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "6"); //产品类型
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,visaOrder.getVisaProductId()); // 产品Id
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProducts.getProductName());
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo()); // 订单编号
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaProducts.getGroupCode()); //订单团号
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, visaOrder.getCreateBy().getId()); //计调
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName()); //计调姓名
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, visaOrder.getCreateBy().getId());//下单人
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName());//下单人姓名
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, visaOrder.getSalerId());//销售
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, UserUtils.getUser(visaOrder.getSalerId().longValue()).getName());//销售姓名
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, traveler.getId()); //游客id
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, traveler.getName()); //游客姓名
	        		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_BATCH_NO, batchNo); //批次号
	        		
	        		//System.out.println(deptId +","+Context.ORDER_TYPE_QZ+","+Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT+","+companyId);
	        		
				    ReviewResult result = processReviewService.start(userId, companyId, permissionChecker, "", Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT, deptId, "", variables);
				    
				    //审核申请金额保存到新审批流程金额表
				    if (result.getSuccess()) {
		        	    NewProcessMoneyAmount newProcessMoneyAmount = new NewProcessMoneyAmount();
		    			newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
		    			newProcessMoneyAmount.setCurrencyId(currencyId);
		    			newProcessMoneyAmount.setAmount(new BigDecimal(returnReceiptJes[i]));
		    			newProcessMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_BORROWMONEY);
		    			newProcessMoneyAmount.setOrderType(6);
		    			newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
		    			newProcessMoneyAmount.setCreateTime(new Date());
		    			newProcessMoneyAmount.setExchangerate(currencyExchangerate);
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

		} catch (Exception e) {
			e.printStackTrace();
			if (resultMap == null) {
				resultMap = new HashMap<String, Object>();
			}
			resultMap.put("msg", "批量还收据申请失败！");
		}
		return resultMap;
	}
	
	/**
	 * @Description: 环球行还签证收据   流程互斥hqx
	 *               申请前只要有一个不符合  就 返回整个批次的，要重新申请
	 * @author xinwei.wang
	 * @date 2015年12月16日上午10:30:31
	 * @param travelerIDS
	 * @return    
	 * @throws
	 */
	private Map<String, Object>  processMutex4HQXVisaReturnReceipt(String[] travelerIDS){
		Map<String,Object> resultMap= null;	
		for (int i = 0; i < travelerIDS.length; i++) {
        	//travelerIDS的数据格式为：[475,476,0]，此处过滤掉ID为0的游客
        	if (!"0".equals(travelerIDS[i])) { 
        		//申请时的addReview 要用添加dept
        		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIDS[i]));
        		VisaOrder visaOrder = visaOrderService.findVisaOrder(traveler.getOrderId());
        		CommonResult commonResult = reviewMutexService.check(visaOrder.getId().toString(),traveler.getId().toString() , "6", "4", false);
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
	 * @Description:生成签证费批次还款单下载文件
	 * @author xinwei.wang
	 * @date 2015年12月11日上午9:54:52
	 * @param revid
	 * @param batchno
	 * @return
	 * @throws IOException
	 * @throws TemplateException    
	 * @throws
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public File createBatchVisaReturnMoneySheetDownloadFile4Activiti(String revid,String batchno) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证还收据申请相关信息
		//Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(revid);
		Map<String,Object>  reviewAndDetailInfoMap = null;
		try{
			if(revid!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + revid + " 查询出来reviewDetail明细报错 ",e);
		}
		
		//获取批次借款金额
		VisaFlowBatchOpration visaFlowBatchOpration = visaFlowBatchOprationDao.findByBatchNo(batchno,"1");
		String batchborrowtotalMoney = visaFlowBatchOpration.getBatchTotalMoney();
		
		
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(reviewAndDetailInfoMap.get("createDate")));// 填写日期
			String tmp = reviewAndDetailInfoMap.get("returnReceiptRemark").toString();
			if (null!=tmp) {
				root.put("returnReceiptRemark",reviewAndDetailInfoMap.get("returnReceiptRemark"));// 申报原因(变动)
			}else {
				root.put("returnReceiptRemark","");// 申报原因(变动)
			}
			
			root.put("receiptAmount",reviewAndDetailInfoMap.get("receiptAmount"));// 申报原因(变动)
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			
			if (null!=reviewAndDetailInfoMap.get("updateDate")) {
				root.put("payDate", sdf.format(reviewAndDetailInfoMap.get("updateDate")));// 付款日期
			}else{
				root.put("payDate", sdf.format(new Date()));// 付款日期
			}
			
			root.put("receiptAmount",fmtMicrometer(batchborrowtotalMoney));// 借款金额    改为批次总金额
		}
		
		
		String companyUUid = UserUtils.getUser().getCompany().getUuid();
		//获取单据审批人员Map
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_VISA_RETURN_MONEY, revid);//e5dbd01ec2f649e39d458540a91aa03b
		List<User> general_managers = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.GENERAL_MANAGER);//总经理
		List<User> financial_executives = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.FINANCIAL_EXECUTIVE);//财务主管
		List<User> cashiers = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.CASHIER);//出纳
		List<User> financials = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.FINANCIAL);//财务
		List<User> reviewers = valueMap.get(ReviewReceiptContext.VisaReturnMoneyReviewElement.REVIEWER);//审批
		
		
		String generalmanager = getNames(general_managers);//总经理 
		String financialexecutive = getNames(financial_executives);//财务主管
		String cashier = getNames(cashiers);//出纳
		String financial = getNames(financials);//财务
		String  reviewer = getNames(reviewers);//审核
		
		//总经理
		root.put("majorCheckPerson",generalmanager);//复合，主管审批  都是最后一个的审批人
		

		/**
		 * 需求变更2015-04-22：如果为环球行用户财务主管为空
		 */
		root.put("cwmanager", financialexecutive);//财务主管
		
		root.put("cw", financial);//财务
		
		/**
		 * 需求变更2015-06-01：如果为环球行用户出纳为空
		 */
		root.put("cashier", cashier);//出纳
		
		root.put("deptmanager", reviewer);//部门经理
		
		root.put("revReturnAmountDx", digitUppercase(Double.parseDouble(batchborrowtotalMoney)));// 借款金额大写   改为批次总金额
		
	    return FreeMarkerUtil.generateFile("visabatchreturnmoney4activiti.ftl", "visabatchreturnmoney.doc", root);
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
	 * 数字格式化
	 * @param text
	 * @return
	 */
	private String fmtMicrometer(String text){
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
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
    private  String digitUppercase(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = n < 0? "红字": ""; ////负 -》红字
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
	
	
	
}
