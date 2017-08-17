package com.trekiz.admin.review.borrowing.visaxxzborrowmoney.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.visaborrowmoney.web.VisaBorrowMoneyController;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.web.VisaPreOrderController;
import com.trekiz.admin.review.borrowing.airticket.web.ActivityAirTicketOrderLendMoneyController;
import com.trekiz.admin.review.borrowing.visaxxzborrowmoney.pojo.VisaXXZBorrowMoneyFormBean;
import com.trekiz.admin.review.borrowing.visaxxzborrowmoney.service.ActivityVisaXXZBorrowMoneyService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexService;

/**  
 * @Title: AcrivityVisaXXZBorrowMoney.java
 * @Package com.trekiz.admin.review.borrowing.visaxxzborrowmoney
 * @Description: 环球行签证借款
 * @author xinwei.wang  
 * @date 2015-2015年11月17日 上午9:51:55
 * @version V1.0  
 */
@Controller
@RequestMapping(value = "${adminPath}/visa/xxz/borrowmoney")
public class ActivityVisaXXZBorrowMoneyController {
	private static final Logger log = Logger.getLogger(ActivityAirTicketOrderLendMoneyController.class);
	public static final String SPLITMARK = "#@!#!@#";
	
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
    private VisaProductsService visaProductsService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ActivityVisaXXZBorrowMoneyService activityVisaXXZBorrowMoneyService;
	@Autowired
	private ReviewService processReviewService;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	@Autowired
	private TravelerDao travelerDao;
    @Autowired
    private SystemService systemService;
    @Autowired
	private MoneyAmountService  moneyAmountService;
    @Autowired
    private UserReviewPermissionChecker permissionChecker;
    @Autowired 
    private ReviewReceiptService reviewReceiptService;
    @Autowired
    private ReviewMutexService reviewMutexService;

	
	/**
	 * @Description: 环球行签证借款申请页面
	 * @author xinwei.wang
	 * @date 2015年11月17日上午10:10:00
	 * @param response
	 * @param request
	 * @return    
	 * @throws
	 */
	@RequestMapping(value ="toVisaXXZBorrowMoneyAppPage")
	public String toVisaXXZBorrowMoneyAppPage(@RequestParam(value="visaOrderId", required=true)String visaOrderId, Model model){
		if(StringUtils.isNotBlank(visaOrderId)) {
			//签证订单
			VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(visaOrderId));
			model.addAttribute("visaOrder", visaOrder);
			model.addAttribute("visaOrderId", visaOrderId);
			
			
			//签证产品
			Long proId = visaOrder.getVisaProductId();
			if(null != proId && 0 != proId) {
				VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
				model.addAttribute("visaProduct", visaProduct);
			}
			
			//游客列表
			List<Traveler> travelerList = activityVisaXXZBorrowMoneyService.getTravelerList(visaOrderId);
			model.addAttribute("travelerList", travelerList);
			
			//币种列表
			List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
			model.addAttribute("currencyList", currencyList);
		}
		
		//270-djy-将还款日期的配置放入model中:1-必填,0-非必填
		model.addAttribute("refunDateSel",UserUtils.getUser().getCompany().getIsMustRefundDate());
		return "review/borrowing/visaxxz/visaXXZBorrowMoneyForm";
	}
	
	/**
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @author xinwei.wang
	 * @date 2015年11月17日上午11:55:04
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException    
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "createVisaXXZBorrowMoney")
	public Map<String, Object> createVisaXXZBorrowMoney(HttpServletRequest request,
		HttpServletResponse response) throws JSONException {
		
		/**
		 * 1.新行者字符串分割符号    #@!#!@#
		 * 
		 * 2.游客借款数据
		 *   游客id：trvids
		 *   游客姓名：trvnames
		 *   游客币种：trvcurrents
		 *   游客借款额：trvamounts
		 *   游客结算价：trvsettlementprices
		 *   游客借款备注：trvborrownotes
		 *   
		 * 3.团队借款数据
		 *   订单id：orderid
		 *   团队款项名称：groupborrownames
		 *   团队款项币种：groupborrowcurrents
		 *   团队款项借款额：groupborrowamounts
		 *   团队借款备注：groupborrownodes
		 *   
		 * 4.借款总额：totalborrowamount  币种，符号，额度 #@!#!@#币种，符号，额度
		 * 5.借款总备注：grouptotalborrownode
		 */
		
		Map<String, Object> resultMap = null;
		String orderid = request.getParameter("visaOrderId");//订单id
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		
		//游客借款信息
		String[] trvids = request.getParameterValues("trvids");//游客ID
		String[] trvnames = request.getParameterValues("trvnames");//游客姓名
		String[] borrowtrvcurrents = request.getParameterValues("refundCurrency");//游客币种
		String[] trvamounts = request.getParameterValues("lendPrice");//借款金额  trvamounts
		String[] trvsettlementprices = request.getParameterValues("trvsettlementprices");//游客结算价
		String[] trvborrownotes = request.getParameterValues("trvborrownotes");//游客借款备注
		
		//团队借款信息
		String[] groupborrownames = request.getParameterValues("groupborrownames");//团队款项名称
		String[] groupborrowcurrents = request.getParameterValues("teamCurrency");//团队款项币种  groupborrowcurrents
		String[] groupborrowamounts = request.getParameterValues("teamMoney");//团队款项借款额  groupborrowamounts
		String[] groupborrownodes = request.getParameterValues("groupborrownodes");//团队借款备注
		
		String grouptotalborrownode = request.getParameter("otherRemarks");//总团队借款备注
		//String totalborrowamount = request.getParameter("totalborrowamount");//团队借款总额
		
		
		/*270_增加还款日期_djw*/
		String refundDate = request.getParameter("refundDate");//还款日期
		
		//在正式申请前处理   借款审核4visa（除环球行 签证  借款）  ，改互斥条件暂不使用
		/*resultMap = processMutex4VisaBorrowmoney(orderid);
		if (null!=resultMap) {
			resultMap.put("appResult", 2);
			return resultMap;
		}*/
		
		
		
		//处理订单 游客的 借款信息-----------------------------
		StringBuilder sb_trvids = new StringBuilder("");//游客ID
		
		StringBuilder sb_trvids4extends = new StringBuilder("");//游客ID
		
		StringBuilder sb_trvnames = new StringBuilder("");//游客姓名
		StringBuilder sb_borrowtrvcurrents = new StringBuilder("");//游客币种
		StringBuilder sb_trvamounts = new StringBuilder("");//借款金额
		StringBuilder sb_trvsettlementprices = new StringBuilder("");//游客结算价
		StringBuilder sb_trvborrownotes = new StringBuilder("");//游客借款备注
		
		//处理保存游客借款汇率、币种名称、币种符号
		StringBuilder sb_trvborrowexchangerates = new StringBuilder("");//游客借款备注
		StringBuilder sb_trvcurrencyNames = new StringBuilder("");//游客借款名称
		StringBuilder sb_trvcurrencyMarks = new StringBuilder("");//游客借款备注
		
		//1.游客借款信息
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		for (int i = 0; i < trvamounts.length; i++) {
			if (null!=trvamounts[i]&&!"".equals(trvamounts[i].trim())) {
				sb_trvids.append(trvids[i]).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//游客ID
				sb_trvids4extends.append(trvids[i]).append(",");//游客ID
				sb_trvnames.append(trvnames[i]).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//游客姓名
				sb_borrowtrvcurrents.append(borrowtrvcurrents[i]).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//游客币种
				sb_trvamounts.append(trvamounts[i]).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//借款金额
				sb_trvsettlementprices.append(trvsettlementprices[i]).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//游客结算价
				String trvborrownote = "".equals(trvborrownotes[i])?" ":trvborrownotes[i];
				sb_trvborrownotes.append(trvborrownote).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//游客借款备注
				
				//获取游客借款汇率
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.currency_name,c.currency_mark,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
				buffer.append(borrowtrvcurrents[i]);
				buffer.append(" AND c.del_flag = 0 AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
				Map<String, Object>  mp =  list.get(0);
				sb_trvborrowexchangerates.append(mp.get("convert_lowest")).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
				sb_trvcurrencyNames.append(mp.get("currency_name")).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
				sb_trvcurrencyMarks.append(mp.get("currency_mark")).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
			}
		}
		
		Map<String,String> mapReview4travelers = new HashMap<String,String>(); 
		//1.1游客信息
		mapReview4travelers.put("trvids", sb_trvids.toString());//游客ID
		mapReview4travelers.put("trvnames", sb_trvnames.toString());//游客姓名
		mapReview4travelers.put("borrowtrvcurrents", sb_borrowtrvcurrents.toString());//游客币种
		mapReview4travelers.put("trvamounts", sb_trvamounts.toString());//借款金额
		mapReview4travelers.put("trvsettlementprices", sb_trvsettlementprices.toString());//游客结算价
		mapReview4travelers.put("trvborrownotes", sb_trvborrownotes.toString());//游客借款备注
		//1.2保存游客借款汇率、币种名称、币种符号
		mapReview4travelers.put("trvborrowexchangerates", sb_trvborrowexchangerates.toString());//游客借款汇率
		mapReview4travelers.put("trvcurrencyNames", sb_trvcurrencyNames.toString());//游客借款币种名称
		mapReview4travelers.put("trvcurrencyMarks", sb_trvcurrencyMarks.toString());//游客借款币种符号
		
		//处理订单的  借款信息-------------------------
		StringBuilder sb_groupborrownames = new StringBuilder("");//团队款项名称
		StringBuilder sb_groupborrowcurrents = new StringBuilder("");//团队款项币种
		StringBuilder sb_groupborrowamounts = new StringBuilder("");//团队款项借款额
		StringBuilder sb_groupborrownodes = new StringBuilder("");//团队借款备注
		
		//保存订单借款汇率、币种名称、币种符号
		StringBuilder sb_groupborrowexchangerates = new StringBuilder("");//游客借款备注
		StringBuilder sb_groupcurrencyNames = new StringBuilder("");//游客借款备注
		StringBuilder sb_groupcurrencyMarks = new StringBuilder("");//游客借款备注
		
		//2.订单团队借款信息
		for (int i = 0; i < groupborrowamounts.length; i++) {
			if (null!=groupborrowamounts[i]&&!"".equals(groupborrowamounts[i].trim())) {
				String groupborrowname = "".equals(groupborrownames[i])?" ":groupborrownames[i];
				sb_groupborrownames.append(groupborrowname).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//团队款项名称
				sb_groupborrowcurrents.append(groupborrowcurrents[i]).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//团队款项币种
				sb_groupborrowamounts.append(groupborrowamounts[i]).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//团队款项借款额
				String groupborrownode = "".equals(groupborrownodes[i])?" ":groupborrownodes[i];
				sb_groupborrownodes.append(groupborrownode).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);//团队借款备注
				
				//获取订单借款汇率
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.currency_name,c.currency_mark,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
				buffer.append(groupborrowcurrents[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
				Map<String, Object>  mp =  list.get(0);
				sb_groupborrowexchangerates.append(mp.get("convert_lowest")).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
				sb_groupcurrencyNames.append(mp.get("currency_name")).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
				sb_groupcurrencyMarks.append(mp.get("currency_mark")).append(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
			}
		}
		
		Map<String,String> mapReview4groups = new HashMap<String,String>(); 
		//2.1订单信息
		mapReview4groups.put("groupborrownames", sb_groupborrownames.toString());//团队款项名称
		mapReview4groups.put("groupborrowcurrents", sb_groupborrowcurrents.toString());//团队款项币种
		mapReview4groups.put("groupborrowamounts", sb_groupborrowamounts.toString());//团队款项借款额
		mapReview4groups.put("groupborrownodes", sb_groupborrownodes.toString());//团队借款备注
		mapReview4groups.put("orderid", orderid);//订单ID
		mapReview4groups.put("grouptotalborrownode", grouptotalborrownode);//总团队借款备注
		
		/*270_增加还款日期_djw*/
		mapReview4groups.put("refundDate", refundDate);//添加还款时间
		
		//2.2保存游客借款汇率、币种名称、币种符号
		mapReview4groups.put("groupborrowexchangerates", sb_groupborrowexchangerates.toString());//游客借款备注
		mapReview4groups.put("groupcurrencyNames", sb_groupcurrencyNames.toString());//游客借款币种名称
		mapReview4groups.put("groupcurrencyMarks", sb_groupcurrencyMarks.toString());//游客借款币种符号
		
		//3.将借款多币种总和根据汇率转换成人民币
		String count = (sb_trvamounts.toString()+sb_groupborrowamounts.toString()).replace(ActivityVisaXXZBorrowMoneyController.SPLITMARK, ",");
		String currencyId = (sb_borrowtrvcurrents.toString()+sb_groupborrowcurrents.toString()).replace(ActivityVisaXXZBorrowMoneyController.SPLITMARK, ",");
		float totalborrow4rmb = activityVisaXXZBorrowMoneyService.currencyConverter(count, currencyId);
		mapReview4groups.put("borrowAmount", totalborrow4rmb+"");//按照汇率转为人民币的团队借款总额
		
		//4.获取人民币币种id
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.currency_mark,c.currency_name FROM currency c WHERE 1=1");
		buffer.append(" AND c.create_company_id=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		List<Map<String, Object>> currencylist = visaOrderDao.findBySql(buffer.toString(), Map.class);
		Integer borrowtotalcurrencyId = 0;
		for (int i = 0; i < currencylist.size(); i++) {
			if ("￥".equals(currencylist.get(i).get("currency_mark"))||"人民币".equals(currencylist.get(i).get("currency_name"))) {
				borrowtotalcurrencyId = (Integer)currencylist.get(i).get("currency_id");
				break;
			}
		}
		mapReview4groups.put("currencyId", borrowtotalcurrencyId+"");//汇总后各个渠道的人民币币种id
		
		//5.将币种和金额汇总后转换成如下格式  借款金额：￥800+$800
		StringBuilder sbcurentcy = new StringBuilder("");
		Map<String, BigDecimal> currentMap =  getTotalMoney(currencyId.split(","),count.split(","));
		Set<String> keys = currentMap.keySet();
		for (String key : keys) {
			Currency currency = currencyService.findCurrency(Long.parseLong(key));
			sbcurentcy.append(currency.getCurrencyMark()).append(currentMap.get(key).toString()).append(" ");
		}
		String totalborrowamount = sbcurentcy.toString().replace(" ", "+");
		mapReview4groups.put("totalborrowamount", totalborrowamount.substring(0,totalborrowamount.length()-1));//团队借款总额str.subString(0,str.length()-1)
		
		/**
		 * 处理review表中是否需要保存游客id
		 * 规则如下：
		 * 1.如果有团队借款则不保存
		 * 2.如没有团队借款则看游客借款是否为一条，如为一条则保存
		 */
		String[] travelerIds = sb_trvids.toString().split(VisaBorrowMoneyController.SPLITMARK);
		String travelerId = "0";
		if (null==sb_groupborrowamounts||sb_groupborrowamounts.toString().length()==0) {
			travelerId = travelerIds.length==1?travelerIds[0]:"0";
		}
		mapReview4groups.put("isgroupborrowamount", travelerId);//判断是否为团队借款
		
		
		//6.收集审核主表记录保信息
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.putAll(mapReview4travelers);//添加游客借款相关信息
		variables.putAll(mapReview4groups);//添加团队借款相关信息
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, visaOrder.getAgentinfoId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderid);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "6");
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,visaOrder.getVisaProductId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProducts.getProductName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaOrder.getGroupCode());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, visaOrder.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName()); //计调姓名
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, visaOrder.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName());//下单人姓名
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, visaOrder.getSalerId());//销售
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, UserUtils.getUser(visaOrder.getSalerId().longValue()).getName());//销售姓名
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, sb_trvids4extends.toString());//销售姓名
		
		//7.创建新行者签证借款审核记录
		Long deptId = visaOrderService.getProductPept(orderid);//通过orderId获取产品的发布部门
		User user =  UserUtils.getUser();
		String userId = user.getId().toString();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		                                                                                               //dept_id,product_type,review_flow
		                               //processReviewServicestart.start(userId, companyId, checker, businessKey, productType*6, processType*20, deptId23, comment, variables, callbackService);	
		//ReviewResult result = null;//= processReviewService.start(userId, companyId, null, "", Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY, deptId, "", variables);
		//result = new ReviewResult();
		//result.setReviewId("8888888888");
		//result.setSuccess(true);
		//TODO
		//ReviewResult result = processReviewService.start(userId, companyId, permissionChecker, "", Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY, deptId, "", variables);
		ReviewResult result = processReviewService.start(userId, companyId, permissionChecker, "", Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_BORROWMONEY, deptId, "", variables);
		
		//8.保存审核  审核相关的 MoneyAmount 到  review_process_money_amount 表
		String  rid = result.getReviewId();
		NewProcessMoneyAmount newProcessMoneyAmount = null;
		
		//合并游客和团队借款的 currencyIds
		String[] currencyIdsBefore = null;//解决为[23, ] 这种问题
		if ("".equals(mapReview4travelers.get("borrowtrvcurrents"))) {
			currencyIdsBefore = mapReview4groups.get("groupborrowcurrents").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
		}else if("".equals(mapReview4groups.get("groupborrowcurrents"))){
			currencyIdsBefore = mapReview4travelers.get("borrowtrvcurrents").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
		}else{
			currencyIdsBefore = concatStringArray(mapReview4travelers.get("borrowtrvcurrents").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK),
                       mapReview4groups.get("groupborrowcurrents").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK));
		}
		
		//合并游客和团队借款的 groupborrowexchangerates
		String[] borrowPricesBefore = null;//解决为[23, ] 这种问题
		if ("".equals(mapReview4travelers.get("trvamounts"))) {
			borrowPricesBefore = mapReview4groups.get("groupborrowamounts").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
		}else if("".equals(mapReview4groups.get("groupborrowamounts"))){
			borrowPricesBefore =mapReview4travelers.get("trvamounts").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
		}else{
			borrowPricesBefore = concatStringArray(mapReview4travelers.get("trvamounts").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK),
                    mapReview4groups.get("groupborrowamounts").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK));
		}
		
		//合并游客和团队借款的 borrowPrices
		String[] currencyExchangeratesBefore = null;//解决为[23, ] 这种问题
		if ("".equals(mapReview4travelers.get("trvborrowexchangerates"))) {
			currencyExchangeratesBefore = mapReview4groups.get("groupborrowexchangerates").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
		}else if("".equals(mapReview4groups.get("groupborrowexchangerates"))){
			currencyExchangeratesBefore =mapReview4travelers.get("trvborrowexchangerates").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK);
		}else{
			currencyExchangeratesBefore = concatStringArray(mapReview4travelers.get("trvborrowexchangerates").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK),
                    mapReview4groups.get("groupborrowexchangerates").split(ActivityVisaXXZBorrowMoneyController.SPLITMARK));
		}
	
		Map<String, String[]> getCurrencyIdsAndBorrowPrices = getBorrowedMoneyKeysAndValues(currencyIdsBefore,borrowPricesBefore,currencyExchangeratesBefore);
		String[] currencyIds = getCurrencyIdsAndBorrowPrices.get("currencyIds");
		String[] borrowPrices = getCurrencyIdsAndBorrowPrices.get("currencyPrices");
		String[] currencyExchangerates = getCurrencyIdsAndBorrowPrices.get("currencyExchangerates");
		
		for (int i = 0; i < currencyIds.length; i++) {
			//审核申请金额保存到新审批流程金额表
			newProcessMoneyAmount =new NewProcessMoneyAmount();
			newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
			newProcessMoneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
			newProcessMoneyAmount.setAmount(new BigDecimal(borrowPrices[i]));
			newProcessMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_BORROWMONEY);
			newProcessMoneyAmount.setOrderType(6);
			newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
			newProcessMoneyAmount.setCreateTime(new Date());
			newProcessMoneyAmount.setExchangerate(new BigDecimal(currencyExchangerates[i]));
			newProcessMoneyAmount.setReviewId(rid);
			newProcessMoneyAmount.setCompanyId(companyId);
			processMoneyAmountService.saveNewProcessMoneyAmount(newProcessMoneyAmount);
		}
		
		boolean isSuccess = result.getSuccess();
		if (isSuccess) {
			if(null==resultMap){
				resultMap = new HashMap<String, Object>();
			}
			resultMap.put("appResult", 1);//1为申请成功 2为申请失败
		}else {
			if(null==resultMap){
				resultMap = new HashMap<String, Object>();
			}
			resultMap.put("appResult", 2);//1为申请成功 2为申请失败
		}
		resultMap.put("orderId", orderid);
		return resultMap;
	}
	
	
	/**
	 * @Description: 新行者借款4viava(除了环球行)处理流程互斥 的 方法（？？？？ 待定，该接口暂时不支持团队类型）
	 * @author xinwei.wang
	 * @date 2015年12月16日下午1:44:45
	 * @param orderId
	 * @return    
	 * @throws
	 */
	private Map<String, Object>  processMutex4VisaBorrowmoney(String orderId){
		Map<String,Object> resultMap= null;	
        CommonResult commonResult = reviewMutexService.check(orderId,"0", "6", "19", true);
		if (!commonResult.getSuccess()) {
			 if(resultMap==null){
 				   resultMap = new HashMap<String,Object>();
 			 }
			resultMap.put("msg", commonResult.getMessage());
		}
		return resultMap;
	}
	
	
	
	/**
	 * @Description: 将币种和金额汇总后转换成如下格式  借款金额：￥800+$800
	 * @author xinwei.wang
	 * @date 2015年11月17日下午2:37:13
	 * @param currencyIds:{"1,2,34,5","1,3,34,7"};
	 * @param currencyPrices:{"120.00,50.00,40.00,70.00","100.00,50.00,40.00,50.00"}
	 * @return    
	 * @throws
	 */
	private Map<String, BigDecimal> getTotalMoney(String[] currencyIds,String[] currencyPrices ){
		//合并所有游客各个币种的钱数
		Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
		for (int i = 0; i < currencyIds.length; i++) {
			String currencyId = currencyIds[i];
			String currencyPrice = currencyPrices[i];
			String[] currIds = currencyId.split(",");
			String[] currPrices = currencyPrice.split(",");
			for (int j = 0; j < currIds.length; j++) {
				String currId = currIds[j];
				String currPrice = currPrices[j];

				if (!map.containsKey(currId)) {
					map.put(currId, new BigDecimal(currPrice));
				} else {
					map.put(currId,map.get(currId).add(new BigDecimal(currPrice)));
				}
			}
		}
		return map;
	}
	
	/**
	 * @Description: 将游客和团队借款的金额按币种合并
	 * @author xinwei.wang
	 * @date 2015年11月17日下午3:23:54
	 * @param currencyIds
	 * @param currencyPrices
	 * @return    
	 * @throws
	 */
	private Map<String, String[]> getBorrowedMoneyKeysAndValues(String[] currencyIds,String[] currencyPrices, String[] currencyExchangerates){
		//
		Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
		Map<String, BigDecimal> map4rate = new LinkedHashMap<String, BigDecimal>();
		for (int i = 0; i < currencyIds.length; i++) {
			String currencyId = currencyIds[i];
			String currencyPrice = currencyPrices[i];
			String currencyExchangerate = currencyExchangerates[i];
			if (!map.containsKey(currencyId)) {
				map.put(currencyId, new BigDecimal(currencyPrice));
				map4rate.put(currencyId, new BigDecimal(currencyExchangerate));
			} else {
				map.put(currencyId,map.get(currencyId).add(new BigDecimal(currencyPrice)));
			}
			
		}
		
        String[] currencyIdsAfter = new String[map.size()];
        String[] currencyPricesAfter = new String[map.size()];
        String[] currencyExchangerateAfter = new String[map.size()];
        int mapindex = 0;
		for (String key : map.keySet()) {  
	    	currencyIdsAfter[mapindex] = key;
	    	currencyPricesAfter[mapindex] = map.get(key).toString();
	    	currencyExchangerateAfter[mapindex] =map4rate.get(key).toString();
	    	mapindex = mapindex+1;
	    } 
		Map<String, String[]> keysandvalues = new LinkedHashMap<String, String[]>();
		keysandvalues.put("currencyIds", currencyIdsAfter);
		keysandvalues.put("currencyPrices", currencyPricesAfter);
		keysandvalues.put("currencyExchangerates", currencyExchangerateAfter);
		return keysandvalues;
	}
	
	/**
	 * @Description: string[] 链接
	 * @author xinwei.wang
	 * @date 2015年11月17日下午3:37:04
	 * @param a
	 * @param b
	 * @return    
	 * @throws
	 */
	 private String[] concatStringArray(String[] a, String[] b) { 
		   String[] c= new String[a.length+b.length];  
		   System.arraycopy(a, 0, c, 0, a.length);  
		   System.arraycopy(b, 0, c, a.length, b.length);  
		   return c;  
    }
	 
	/**
	 * @Description: 签务签证订单-借款记录,新行者签证借款记录单
	 * @author xinwei.wang
	 * @date 2015年11月18日上午10:05:38
	 * @param request
	 * @param response
	 * @param model
	 * @param orderId
	 * @param flowType
	 * @param productType
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "borrowMoneyRecord4XXZactivity")
	public String borrowMoneyRecord4XXZactivity( HttpServletRequest request, HttpServletResponse response, Model model, String orderId) {
		VisaOrder order = visaOrderService.findVisaOrder(Long.valueOf(orderId));
		//Long pid = order.getVisaProductId();
		Long deptId = visaOrderService.getProductPept(Long.valueOf(orderId));//通过orderId获取产品的发布部门
		                                       //processReviewService.getReviewDetailMapListByOrderId(deptId, productType, processType, orderId, orderByProperty, orderByDirection)
		List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(deptId, 6, 19, order.getId().toString(),OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
		
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
				String  isgroupborrowamount = (String)map.get("isgroupborrowamount");
				if (null!=isgroupborrowamount) {
					if (!"0".equals(isgroupborrowamount)) {
						Traveler traveler = travelerDao.findById(Long.parseLong(isgroupborrowamount));
						reviewAndDetailInfoMap.put("travelerName", traveler.getName());
					}else{
						reviewAndDetailInfoMap.put("travelerName", "团队");
					}
				}else{
					reviewAndDetailInfoMap.put("travelerName", "团队");
				}
				
				//3.币种
				reviewAndDetailInfoMap.put("currencyName", "人民币");
				
				//4. 借款金额
				DecimalFormat df = new DecimalFormat("#,##0.00");
				String borrowAmount = df.format(new BigDecimal((String)map.get("borrowAmount")));
				reviewAndDetailInfoMap.put("borrowAmount", borrowAmount);
				//5. 借款人
				reviewAndDetailInfoMap.put("createBy", (String)(map.get("createBy")));
				//6. 借款金额
				reviewAndDetailInfoMap.put("borrowAmount", (String)(map.get("borrowAmount")));
				//7. 借款状态
				reviewAndDetailInfoMap.put("status", (map.get("status").toString()));
				//8.其他
				reviewAndDetailInfoMap.put("orderId", orderId);//订单id
				reviewAndDetailInfoMap.put("id", (map.get("id").toString()));//review_new id  procInstId
				

				borrowMoneyRecordList.add(reviewAndDetailInfoMap);
				
			}
		}
	
		// 公司ID
		model.addAttribute("companyId",UserUtils.getUser().getCompany().getId());
		// 订单ID
		model.addAttribute("orderId", orderId);
		// 借款记录List
		model.addAttribute("borrowMoneyRecordList", borrowMoneyRecordList);
		
		return "review/borrowing/visaxxz/borrowMoneyRecord4XXZactivity";
	}
	
	/**
	 * @Description: activiti 新型者借款详情页
	 * @author xinwei.wang
	 * @date 2015年11月18日下午4:26:32
	 * @param model
	 * @param request
	 * @param response
	 * @return    
	 * @throws
	 */
	@RequestMapping(value = "visaBorrowMoney4XXZReviewDetail")
	public String visaBorrowMoney4XXZReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String reviewId = request.getParameter("reviewId");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		
		//签证订单
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(orderId));
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("visaOrderId", orderId);
		
		//签证产品
		Long proId = visaOrder.getVisaProductId();
		if(null != proId && 0 != proId) {
			VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
			model.addAttribute("visaProduct", visaProduct);
		}
		
		//获取activiti审核信息
		Map<String,Object>  reviewAndDetailInfoMap = null;
		try{
			if(reviewId!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		
		//String[] trvids = reviewAndDetailInfoMap.get("trvids").split(VisaBorrowMoneyController.SPLITMARK);//游客ID
		String[] trvnames = (reviewAndDetailInfoMap.get("trvnames")).toString().split(VisaBorrowMoneyController.SPLITMARK);//游客姓名
		String[] borrowtrvcurrents = (reviewAndDetailInfoMap.get("borrowtrvcurrents")).toString().split(VisaBorrowMoneyController.SPLITMARK);//游客币种
		String[] trvamounts = (reviewAndDetailInfoMap.get("trvamounts")).toString().split(VisaBorrowMoneyController.SPLITMARK);//借款金额  trvamounts
		String[] trvsettlementprices = (reviewAndDetailInfoMap.get("trvsettlementprices")).toString().split(VisaBorrowMoneyController.SPLITMARK);//游客结算价
		String[] trvborrownotes = (reviewAndDetailInfoMap.get("trvborrownotes")).toString().split(VisaBorrowMoneyController.SPLITMARK);//游客借款备注
		//1.游客列表
		List<Map<String, String>> travelerList = new ArrayList<Map<String,String>>();
		for (int i = 0; i < trvamounts.length; i++) {
			if (!"".equals(trvamounts[i])) {
				Map<String, String> trvborrowmap = new HashMap<String, String>();
				trvborrowmap.put("tname", trvnames[i]);
				Currency currency = currencyService.findCurrency(Long.parseLong(borrowtrvcurrents[i]));
				trvborrowmap.put("crrencyName", currency.getCurrencyName());
				trvborrowmap.put("trvsettlementprice", trvsettlementprices[i]);
				trvborrowmap.put("trvamount", currency.getCurrencyMark()+trvamounts[i]);
				trvborrowmap.put("trvborrownote", trvborrownotes[i]);
				travelerList.add(trvborrowmap);
			}

		}
		model.addAttribute("travelerList", travelerList);
		
		//2.团队借款列表
		String[] groupborrownames = (reviewAndDetailInfoMap.get("groupborrownames")).toString().split(VisaBorrowMoneyController.SPLITMARK);//团队款项名称
		String[] groupborrowcurrents = (reviewAndDetailInfoMap.get("groupborrowcurrents")).toString().split(VisaBorrowMoneyController.SPLITMARK);//团队款项币种  groupborrowcurrents
		String[] groupborrowamounts = (reviewAndDetailInfoMap.get("groupborrowamounts")).toString().split(VisaBorrowMoneyController.SPLITMARK);//团队款项借款额  groupborrowamounts
		String[] groupborrownodes = (reviewAndDetailInfoMap.get("groupborrownodes")).toString().split(VisaBorrowMoneyController.SPLITMARK);//团队借款备注
		List<Map<String, String>> groupList = new ArrayList<Map<String,String>>();
		//System.out.println(groupborrowcurrents.length);
		
		for (int i = 0; i < groupborrowamounts.length; i++) {
			if (!"".equals(groupborrowamounts[i])) {
				Map<String, String> groupborrowmap = new HashMap<String, String>();
				groupborrowmap.put("groupborrowname", groupborrownames[i]);
				Currency currency = currencyService.findCurrency(Long.parseLong(groupborrowcurrents[i]));
				groupborrowmap.put("groupborrowcurrent", currency.getCurrencyName());
				groupborrowmap.put("groupborrowamount", currency.getCurrencyMark()+groupborrowamounts[i]);
				groupborrowmap.put("groupborrownode", groupborrownodes[i]);
				groupList.add(groupborrowmap);
			}
		}
			
		model.addAttribute("totalMoney", visaOrder.getTotalMoney());//用于前台的标签计算订单金额
		model.addAttribute("groupList", groupList);
		
		//处理总额的显示
		String totalborrowamount = (String)reviewAndDetailInfoMap.get("totalborrowamount");
	/*	if (totalborrowamount!=null&&totalborrowamount.trim().length()>0) {
			for (int i = 0; i < groupborrownodes.length; i++) {
				
				
			}
		}*/
		
		
		model.addAttribute("totalborrowamount", totalborrowamount);//借款单总额
		model.addAttribute("grouptotalborrownode", reviewAndDetailInfoMap.get("grouptotalborrownode"));//借款单总额
		
		/*270_新增借款日期__djw*/
		String refundDate = (String) reviewAndDetailInfoMap.get("refundDate");
		model.addAttribute("refundDate", refundDate);
		
		if (reviewAndDetailInfoMap!=null) {
			//reviewAndDetailInfoMap.put("createDate", DateUtils.date2String((Date)map.get("createDate"), "yyyy-MM-dd hh:mm:ss"));
			model.addAttribute("revCreateDate", DateUtils.date2String((Date)reviewAndDetailInfoMap.get("createDate"), "yyyy-MM-dd hh:mm:ss"));//报批日期
			model.addAttribute("revBorrowRemark", (String)reviewAndDetailInfoMap.get("borrowRemark"));//申报原因
			model.addAttribute("revBorrowAmount",  (String)reviewAndDetailInfoMap.get("borrowAmount"));//收据金额
			String currencyId =  (String)reviewAndDetailInfoMap.get("currencyId");
			if (null!=currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);//还收据人
			}
		}
		
		//处理审核动态信息
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
	    	model.addAttribute("rLog",rLog);
	    }
		
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",reviewId);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",reviewId);
		
		//model.addAttribute("airticketReturnDetailInfoMap",airticketReturnDetailInfoMap);
		return "review/borrowing/visaxxz/visaBorrowMoney4XXZReviewDetail";
	}
	
	
	/**
	 * @Description: 新行者借款取消：借款记录中的取消操作
	 * @author xinwei.wang
	 * @date 2015年11月18日下午4:27:35
	 * @param request
	 * @return    
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "cancelXXZBorrowAmount")
	public Map<String, Object> cancelXXZBorrowAmount( HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String reviewId = request.getParameter("reviewId");
		String cancelmark = request.getParameter("cancelmark");
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		ReviewResult reviewResult = null;
		//reviewResult =processReviewService.back(UserUtils.getUser().getId().toString(), companyUuid, "", reviewId, null, null);
		                                             //processReviewService.cancel(userId, companyId, consigner, reviewId, comment, variables, callbackService)
		reviewResult =processReviewService.cancel(UserUtils.getUser().getId().toString(), companyUuid, "", reviewId, cancelmark,null);
	
		boolean isReviewSuccess = reviewResult.getSuccess();
		if(isReviewSuccess){
			map.put("success", "取消成功！");
		}else{
			return null;
		}
        return map;
	}
	
	/**
	 * @Description: 跳转到新行者借款列表
	 * @param @param request
	 * @param @param response
	 * @param @param model
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-4 下午8:20:50
	 */
	@RequestMapping(value = "visaXXZBorrowMoneyNewReviewList")
	public String visaXXZBorrowNewReviewList(HttpServletRequest request, HttpServletResponse response, Model model, VisaXXZBorrowMoneyFormBean formBean) {
		//初始化排序方式
		if(StringUtils.isEmpty(formBean.getOrderBy())) {
			formBean.setOrderBy("create_date");
			formBean.setAscOrDesc("desc");
		}
		//初始化审批
		if(formBean.getTabStatus() == null) {
			formBean.setTabStatus(0);
		}
		
		Page<Map<String, Object>> page = activityVisaXXZBorrowMoneyService.queryXXZBorrowMoneyReviewList(request, response, formBean);
		model.addAttribute("formBean", formBean);
		model.addAttribute("page", page);
		model.addAttribute("userList", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("orderType", Context.ORDER_TYPE_QZ);
		return "/review/borrowing/visaxxz/newVisaBorrowMoneyReviewList";
	}
	
	
	/**
	 * 签证借款审核：驳回 或 通过
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "review4XXZVisaBorrowMoneyNew")
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String review4XXZVisaBorrowMoney(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String revid = request.getParameter("revid");
		String denyReason = request.getParameter("denyReason");
		String result = request.getParameter("result");//0:驳回，1:通过
		String orderId = request.getParameter("orderId");
		
		StringBuffer reply = new StringBuffer();
		
		if (revid == null || "".equals(revid)) {
			reply.append("审批id不能为空");
		}
		
		if (result == null || "".equals(result)) {
			reply.append("审批结果不能为空");
		}
		if (reply != null && !"".equals(reply.toString())) {
			model.addAttribute("reply", reply);
			return visaBorrowMoney4XXZReviewDetail(model,request, response);
		}
		
		VisaOrder visaOrder = visaOrderDao.findOne(Long.parseLong(orderId));
		/*
		 * 2015-05-07 wxw added
		 * 1.审核通过后在MoneyAmount中保存借款信息
		 * 2.处理与老数据兼容的相关问题：如果traveler的jkSerialNum为空，生成新的UUID并进行更新
		 * uuid取游客的jkSerialNum
		 */
		String jkSerialNum = UUID.randomUUID().toString();
		if (null == visaOrder.getJkSerialnum() || "".equals(null == visaOrder.getJkSerialnum())) {
			visaOrderService.updateOrderJkSerialnum(jkSerialNum,visaOrder.getId());
			visaOrder.setJkSerialnum(jkSerialNum);
		}
		
		Map<String, Object> variables = new HashMap<String, Object>();
		ReviewResult reviewResult;
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if (Context.REVIEW_ACTION_PASS.equals(result)) { //通过
			reviewResult = processReviewService
					.approve(UserUtils.getUser().getId().toString(), companyUuid, null, permissionChecker, revid,
							 denyReason, variables);
		} else {    //驳回
			reviewResult = processReviewService
					.reject(UserUtils.getUser().getId().toString(), companyUuid, null, revid,
							denyReason, variables);
		}
		if (!reviewResult.getSuccess()) {
			throw new Exception("审核或驳回失败！");
		}
		// 3如果审核通过并且当前层级为最高层级 则保存借款信息
		if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
			//获取activiti审核信息
			Map<String,Object>  reviewAndDetailInfoMap = null;
			try{
				if(revid!=null){
					reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
				}
			}catch(Exception e){
				log.error("根据reviewid： " + revid + " 查询出来reviewDetail明细报错 ",e);
			}
			//获取总额
			String totalborrowamount = (String)reviewAndDetailInfoMap.get("borrowAmount");
			//获取币种
			String currencyId =  (String)reviewAndDetailInfoMap.get("currencyId");
			
		    BigDecimal price = new BigDecimal(totalborrowamount);
		    
			MoneyAmount costMoneyAmount = new MoneyAmount(visaOrder.getJkSerialnum(), //款项UUID
					Integer.parseInt(currencyId),//币种ID
					price,//相应币种的金额
					visaOrder.getId(), //借款订单或游客ID
		    		Context.MONEY_TYPE_JK, //款项类型: 借款
		    		Context.ORDER_TYPE_QZ,//订单类型
		    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
		    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
			costMoneyAmount.setReviewUuid(revid);
			moneyAmountService.addMoneyAmount(costMoneyAmount);
		}
		
		 //return visaXXZBorrowNewReviewList(request, response,model,new VisaXXZBorrowMoneyFormBean());
		
		return "redirect:/a/newOrderReview/manage/getBorrowingList";
	}
	
	
	/**
	 * 审核回退：根据revid进行批次回退操作,即返回上一级审核
	 * 规则如下：
	 * 1.第一层级 和 最后一层级没有退回操作
	 * 2.隔级审核后不能再进行退回操作
	 * 3.只有审核中状态的才可能进行退回操作
	 */
	@RequestMapping(value = "visaBorrowMoneyCancelAjaxNew")
	@ResponseBody
	public Map<String, Object> visaBorrowMoneyCancelAjaxNew(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		String reviewId = request.getParameter("revid");
		try {
			//审核回退
		    if(StringUtils.isNotBlank(reviewId)) {
		       ReviewResult reviewResult = processReviewService.back(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), null, reviewId, null, null);
		       if(reviewResult.getSuccess()){
					map.put("result",1);//1成功   2为申请失败
				}else {
					map.put("result",2);//1成功   2为申请失败
				}
			}
		}catch (Exception e) {
			map.put("result",2);
			e.printStackTrace();
			return map;
		}	
		
		return map;
	}
	
	
	/**
	 * 新行者签证费借款单 wangxinwei 2015年05月12日9:41:00
	 */
	@RequestMapping(value = "visaBorrowMoney4XXZFeePrintNew")
	public String visaBorrowMoney4XXZFeePrintNew(Model model,
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
		
		String revid = request.getParameter("reviewId");
		String payStatus = null; //签证借款审核付款状态
		
		model.addAttribute("revid", revid);//把revid 传到模板模板的html页面，下载模板时使用
		
		// 签证借款申请相关信息
		//Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		Map<String, Object> reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
		if (reviewAndDetailInfoMap != null) {
			model.addAttribute("revCreateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate").toString()));// 填写日期
			
			//----- wxw added 20151008 -----单需求C221 ， 处理付款确认时间
			if(reviewAndDetailInfoMap.get("updateDate") != null){
				model.addAttribute("revUpdateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate").toString()));// 更新日期	
				model.addAttribute("payDate", DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate").toString()));// 付款日期
			}
			
			model.addAttribute("grouptotalborrownode",reviewAndDetailInfoMap.get("grouptotalborrownode"));// 申报原因（对新行者来说取总的备注信息）
			
			/*270_新增还款日期_djw*/
			model.addAttribute("refundDate",reviewAndDetailInfoMap.get("refundDate"));
			
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId").toString();
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			model.addAttribute("productCreater", productCreater);
			
			
			if (null != user) {
				model.addAttribute("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				model.addAttribute("operatorName", "未知");
			}
			
			model.addAttribute("revBorrowAmount",activityVisaXXZBorrowMoneyService.fmtMicrometer(reviewAndDetailInfoMap.get("borrowAmount").toString()));// 借款金额
			String currencyId = reviewAndDetailInfoMap.get("currencyId").toString();
			if (null != currencyId) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);
			}
		}
		
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		
		//Long userCompanyId = UserUtils.getUser().getCompany().getId();
		//int toplivelLong = reviewCompanyDao.findTopLevel(userCompanyId, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		
		/*------------------------------------------------------------------------------*/
		//List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
		//List<ReviewLogNew> reviewLogs = reviewLogService.findReviewLogByReviewId(revid);
		/*------------------------------------------------------------------------------*/
		
		//List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
	    
		/*if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}*/
		
		
		model.addAttribute("revBorrowAmountDx", digitUppercase(Double.parseDouble(reviewAndDetailInfoMap.get("borrowAmount").toString())));// 借款金额大写
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
//		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY,reviewLogs);
		/**
		 * 通过性方式获取审核人职务 
		 */
		String companyUUid = UserUtils.getUser().getCompany().getUuid();
		//获取单据审批人员Map
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY, revid);//e5dbd01ec2f649e39d458540a91aa03b
		List<User> finance = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.FINANCIAL);//财务
		List<User> managers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.GENERAL_MANAGER);//总经理
		List<User> financesManager = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.FINANCIAL_EXECUTIVE);//财务主管
		List<User> cashiers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.CASHIER);//出纳
		List<User> reviewers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.REVIEWER);//审核
		
		String cwManager = activityVisaXXZBorrowMoneyService.getNames(financesManager);//财务主管
		String cashier = activityVisaXXZBorrowMoneyService.getNames(cashiers);//出纳
		String manager = activityVisaXXZBorrowMoneyService.getNames(managers);//总经理 
		String  reviewer = activityVisaXXZBorrowMoneyService.getNames(reviewers);//审核
		String cw = activityVisaXXZBorrowMoneyService.getNames(finance);//财务
		
		if (StringUtils.isNotBlank(cw)) {//财务
			model.addAttribute("cw", cw);
		}else {
			model.addAttribute("cw", "");
		}
		
		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (!companyUUid.equals("7a816f5077a811e5bc1e000c29cf2586") || 68!=UserUtils.getUser().getCompany().getId()) {
			model.addAttribute("cashier", cashier);//出纳
		}else {
			model.addAttribute("cashier", "");
		}
		
		//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
		if (StringUtils.isNotBlank(manager)) {//总经理
			model.addAttribute("majorCheckPerson", manager);
		}else {
			model.addAttribute("majorCheckPerson", "");
		}
		
		if (StringUtils.isNotBlank(reviewer)) {//审核
			model.addAttribute("deptmanager", reviewer);//审核
		}else {
			model.addAttribute("deptmanager", "");
		}
		
		
	/*	Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&null==review.getPrintFlag()) { 
			Date printDate = new Date();
			model.addAttribute("printDate",printDate );
			try {
				visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid),printDate);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("签证费借款单日期格式化错", e);
				throw e;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}else {
			model.addAttribute("printDate",review.getPrintTime());
		}*/
		
		ReviewNew review =  processReviewService.getReview(revid);
		if (null != review && 0 == review.getPrintStatus()) { 
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			model.addAttribute("printDate",printDateStr );
		}else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(review.getPrintDate());
			model.addAttribute("printDate",printDateStr);
		}
		
		//----- wxw added 20151008 -----需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT rev.pay_status FROM review_new rev WHERE rev.id =");
		buffer.append("'"+revid+"'");
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		if (null!=list&&list.size()>0) {
			payStatus = list.get(0).get("pay_status").toString();
			if (companyUUid.equals("7a816f5077a811e5bc1e000c29cf2586")||companyUUid.equals("7a81a26b77a811e5bc1e000c29cf2586")) {
				model.addAttribute("payStatus","0");
			}else {
				model.addAttribute("payStatus","1");
			}
		}else {
			model.addAttribute("payStatus","0");
		}
		
		return "review/borrowing/visaxxz/newVisaBorrowMoney4XXZFeePrint";
	}
	

	/**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
    public static String digitUppercase(double n){
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
    
	/**
	 * 签证费借款单打印  wangxinwei 2014年2月3日11:30:00
	 */
	@RequestMapping(value = "visaBorrowMoneyFeePrintAjaxNew")
	@ResponseBody
	public Map<String, Object> visaBorrowMoneyFeePrintAjaxNew(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String revid = request.getParameter("revid");
		String printDatestr = request.getParameter("printDate");
		
		ReviewNew review =  processReviewService.getReview(revid);
		if (null != review && 0 == review.getPrintStatus()) { 
			//Date printDate = new Date();
			//model.addAttribute("printDate",printDate );
		
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
				Date printDate = simpleDateFormat.parse(printDatestr);
				processReviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", revid, printDate);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("签证费借款单日期格式化错", e);
				throw e;
			} catch (Exception e) {
				map.put("result",2);
				e.printStackTrace();
				return map;
			}
		}
		
		//map.put("error", "签证借款申请失败！");
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	/**
	 * 签证费借款单下载
	 * @param request
	 * @param response
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value="downloadVisaBorrowMoney4XXZSheetNew")
	public ResponseEntity<byte[]> downloadVisaBorrowMoney4XXZSheetNew(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		File file = activityVisaXXZBorrowMoneyService.createVisaBorrowMoney4XXZSheetDownloadFile(revid);
		
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
	  
}
