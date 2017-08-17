package com.trekiz.admin.review.borrowing.visaxxzborrowmoney.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.quauq.review.core.engine.ReviewLogService;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.borrowing.airticket.formbean.NewBorrowingBean;
import com.trekiz.admin.review.borrowing.visaxxzborrowmoney.pojo.VisaXXZBorrowMoneyFormBean;
import com.trekiz.admin.review.common.utils.ReviewUtils;

import freemarker.template.TemplateException;

/**  
 * @Title: AcrivityVisaXXZBorrowMoneyService.java
 * @Package com.trekiz.admin.review.borrowing.visaxxzborrowmoney.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xinwei.wang  
 * @date 2015-2015年11月17日 上午10:21:04
 * @version V1.0  
 */
@Service
@Transactional(readOnly = true)
public class ActivityVisaXXZBorrowMoneyService extends BaseService{
	
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private MoneyAmountService moneyAmountService;
    @Autowired
    private ReviewLogService reviewLogService;
    @Autowired 
    private ReviewReceiptService reviewReceiptService;
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy 年 MM 月 dd 日");
	
	/**
	 * @Description: 根据签证订单ID，获取订单的游客列表
	 * @author xinwei.wang
	 * @date 2015年11月17日上午10:24:25
	 * @param orderId
	 * @return    
	 * @throws
	 */
	public List<Traveler> getTravelerList(String orderId) {
		List<Traveler> travelerList = null;
		if(StringUtils.isNotBlank(orderId)) {
			travelerList = travelerDao.findTravelerByOrderIdAndOrderType(Long.valueOf(orderId), Integer.valueOf(Context.PRODUCT_TYPE_QIAN_ZHENG));
		}
		return travelerList;
	}
	
	/**
	 * @Description: 其他币种转换成人民币
	 * @author xinwei.wang
	 * @date 2015年11月17日下午2:29:42
	 * @param count:币种金额
	 * @param currencyId：币种ID
	 * @return    
	 * @throws
	 */
	public float currencyConverter(String count,String currencyId)
	{
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		String [] ct= count.split(",");
		String [] ci= currencyId.split(",");
		double totalMoney =0;
		for(int i=0;i<ct.length;i++)
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT c.currency_id,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
			buffer.append(ci[i]);
			buffer.append(" AND c.create_company_id=");
			buffer.append(userCompanyId);
			List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
			Map<String, Object>  mp =  list.get(0);
			totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ct[i]);
		}
		 BigDecimal   b   =   new   BigDecimal(totalMoney); 
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	/**
	 * @Description: 根据表单查询条件获取新行者的借款列表信息
	 * @param @param formBean
	 * @param @return   
	 * @return Page<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-4 下午8:51:15
	 */
	public Page<Map<String, Object>> queryXXZBorrowMoneyReviewList(HttpServletRequest request, HttpServletResponse response, VisaXXZBorrowMoneyFormBean formBean) {
		// 获取当前登录用户的id
		Long userId = UserUtils.getUser().getId();
		/* 声明查询SQL */
		StringBuffer querySql = new StringBuffer();
		querySql.append("select r.order_no orderno,r.id reviewid ").//订单编号
		append(",r.id_long idLong").
		append(",r.order_id orderid ").//订单id
		append(",r.group_code groupcode ").//团号
		append(",r.product_name productname ").//产品名称
		append(",r.product_id productid ").//产品id
		append(",r.product_type producttype ").//产品类型
		append(",r.create_date createdate ").//申请时间
		append(",r.create_by createby ").//审批发起人
		append(",r.agent agent ").//渠道商
		append(",r.saler saler ").//销售
		append(",r.operator operator ").//计调
		append(",m.currencyId currencyid ").//金额币种
		append(",m.amount amount ").//借款金额
		append(",r.last_reviewer lastreviewer ").//上一环节审批人
		append(",r.status status ").//审批状态
		append(",r.pay_status paystatus ").//出纳确认
		append(",r.print_status printstatus ").//打印状态
		append(",vorder.total_money totalMoney ").
		append(",vorder.payed_money payedMoney ").
		append(",vorder.accounted_money accountedMoney ").
		append(" from review_new r left join review_process_money_amount m on r.id = m.reviewId ").//左连接查询
		append(" left join visa_order vorder on r.order_id = vorder.id ").
		append(" where 1 = 1 and r.process_type = '" + Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY + "' and FIND_IN_SET ('" + UserUtils.getUser().getId() + "', r.all_reviewer) ");
		
		//模糊查询条件:团号；产品名称；订单号
		if(StringUtils.isNotEmpty(formBean.getSearchLike())) {
			querySql.append(" and (r.group_code like '%" +formBean.getSearchLike()+ "%' or r.order_no like '%" + formBean.getSearchLike() + "%' or r.product_name like '%"+ formBean.getSearchLike() +"%') ");
		}
		//渠道
		if(StringUtils.isNotEmpty(formBean.getAgent())) {
			querySql.append(" and r.agent = " + formBean.getAgent() + " ");
		}
		// 申请日期
		if (formBean.getApplyDateFrom() != null) {
			querySql.append(" and r.create_date >= '" + DateUtils.date2String(formBean.getApplyDateFrom(), DateUtils.DATE_PATTERN_YYYY_MM_DD) + " 00:00:00' ");
		}
		if (formBean.getApplyDateTo() != null) {
			querySql.append(" and r.create_date <= '" + DateUtils.date2String(formBean.getApplyDateFrom(), DateUtils.DATE_PATTERN_YYYY_MM_DD) + " 23:59:59' ");
		}
		// 审批发起人
		if(StringUtils.isNotEmpty(formBean.getApplyPerson())) {
			querySql.append(" and r.create_by = " + formBean.getApplyPerson()+ " ");
		}
		//销售
		if(StringUtils.isNotBlank(formBean.getSaler())){
			querySql.append(" and r.saler = " + formBean.getSaler()+" ");
		}
		/* 审批状态 空 为全部 1 审批中 2 已通过 0 未通过 */
		if(formBean.getReviewStatus() != null && formBean.getReviewStatus() != -1) {
			querySql.append(" and r.status = " + formBean.getReviewStatus() + " ");
		}
		// 出纳确认
		if(formBean.getCashConfirm() != null && formBean.getCashConfirm() != -1) {
			querySql.append(" and r.pay_status = " + formBean.getCashConfirm() + " ");
		}
		// 打印状态
		if(formBean.getPrintStatus() != null && formBean.getPrintStatus() != -1) {
			querySql.append(" and r.print_status = " + formBean.getPrintStatus() + " ");
		}
		//借款金额
		if(formBean.getMinBorrowMoney() != null) {
			querySql.append(" and m.amount >= " +formBean.getMinBorrowMoney()+" ");
		}
		if(formBean.getMaxBorrowMoney() != null) {
			querySql.append(" and m.amount <= " +formBean.getMaxBorrowMoney()+" ");
		}
		
		/* 状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批 */
		if(formBean.getTabStatus() != null) {
			if (Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == formBean.getTabStatus()) {
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == formBean.getTabStatus()) {
				querySql.append(" and r.id in (select review_id from review_log_new  where operation = 1 and active_flag = 1 and create_by = '" + userId + "') ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == formBean.getTabStatus()) {
				querySql.append(" and FIND_IN_SET('" + userId + "', r.skipped_assignee)");
			}
		}
		StringBuffer orderBySql = new StringBuffer();
		// 排序 默认按重要程度降序 按创建时间降序
		orderBySql.append(" r.critical_level desc ");

		//排序包含create_date和update_date
		if(StringUtils.isNotEmpty(formBean.getOrderBy())) {
			orderBySql.append(", r." + formBean.getOrderBy());
			if(StringUtils.isNotEmpty(formBean.getAscOrDesc())) {
				orderBySql.append(" " + formBean.getAscOrDesc()+" ");
			} else {
				orderBySql.append(" asc ");
			}
		}
		//排序封装
		Page<Map<String, Object>> reqPage = new Page<Map<String, Object>>(request, response);
		reqPage.setOrderBy(orderBySql.toString());
		
		// 执行SQL查询出列表数据
		Page<Map<String, Object>> pageInfo = visaOrderDao.findBySql(reqPage, querySql.toString(), Map.class);
		// 为列表数据组装审核变量
		if(CollectionUtils.isNotEmpty(pageInfo.getList())){
			List<Map<String,Object>> list =pageInfo.getList();
			for(Map<String,Object> map:list){
				String rid = map.get("reviewid").toString();
				if(StringUtils.isNotBlank(rid)){
					Map<String, Object> mapPrecess =  reviewService.getReviewDetailMapByReviewId(rid);
					NewBorrowingBean borr = new NewBorrowingBean(mapPrecess);
					
					if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(NewBorrowingBean.REGEX)){
						String compPrice = "";
						if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
							String[] cMarks = borr.getCurrencyMarks().split(NewBorrowingBean.REGEX);	
							String[] cPrices = borr.getBorrowPrices().split(NewBorrowingBean.REGEX);
							for(int i=0;i<cMarks.length;i++){
								compPrice+=cMarks[i]+cPrices[i]+"+";
							}
							borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
						}

					}else{
						borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
					}
					//获取渠道名称
					map.put("agentName", mapPrecess.get("agentName"));
					map.put("payPrice", borr.getCurrencyIds());
					map.put("currentReviewer", borr.getCurrentReviewer());
					map.put("reviewId",  mapPrecess.get("id"));
					//判断能否撤销
					map.put("isBackReview", ReviewUtils.isBackReview(rid));
					//付款金额（已经做了累加）
					map.put("payedMoneyStr", moneyAmountService.getMoney(map.get("payedMoney").toString()));
					map.put("accountedMoneyStr", moneyAmountService.getMoney(map.get("accountedMoney").toString()));
				}
			}
		}
		return pageInfo;
	}
	
	/**
	 * 获取当前审核人描述 由id转化为name
	 * 
	 * @param cReviewer
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getReviewerDesc(Object cReviewer) {
		String reviewers = cReviewer.toString();
		String[] reviewArr = reviewers.split(",");
		String result = "";
		int n = 0;
		String tName = "";
		for (String temp : reviewArr) {
			if (StringUtils.isBlank(temp)) {
				continue;
			}
			tName = UserUtils.getUserNameById(Long.parseLong(temp));
			if (n == 0) {
				result += tName;
			} else {
				result += "," + tName;
			}
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	private String getReviewStatus(int status) {
		if (ReviewConstant.REVIEW_STATUS_CANCELED == status) {
			return ReviewConstant.REVIEW_STATUS_CANCELED_DES;
		} else if (ReviewConstant.REVIEW_STATUS_PASSED == status) {
			return ReviewConstant.REVIEW_STATUS_PASSED_DES;
		} else if (ReviewConstant.REVIEW_STATUS_REJECTED == status) {
			return ReviewConstant.REVIEW_STATUS_REJECTED_DES;
		}
		return "无";
	}
	
	
	
	/**
	 * 生成签证借款单文件
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public File createVisaBorrowMoney4XXZSheetDownloadFile(String revid) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证借款申请相关信息
		//Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(revid);
		Map<String,Object> reviewAndDetailInfoMap = reviewService.getReviewDetailMapByReviewId(revid);
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate").toString())));// 填写日期
			
			// --- wangxinwei 20151008 added：处理确认付款日期
			root.put("revUpdateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate").toString())));// 更新日期
			
			root.put("grouptotalborrownode",reviewAndDetailInfoMap.get("grouptotalborrownode"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId").toString();
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);
			
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			root.put("payDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate").toString())));// 付款日期
			root.put("revBorrowAmount",fmtMicrometer(reviewAndDetailInfoMap.get("borrowAmount").toString()));// 借款金额
		}
		
		
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		//Long userCompanyId = UserUtils.getUser().getCompany().getId();
		//int toplivelLong = reviewCompanyDao.findTopLevel(userCompanyId, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);//
		/*List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(revid);*/
		List<ReviewLogNew> reviewLogs = reviewLogService.findReviewLogByReviewId(revid);
		
		if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				root.put("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}
		root.put("revBorrowAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(reviewAndDetailInfoMap.get("borrowAmount").toString())));// 借款金额大写
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		/*Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY,reviewLogs);*/
		
		/**
		 * 通过性方式获取审核人职务 
		 */
		String companyUUid = UserUtils.getUser().getCompany().getUuid();
		//获取单据审批人员Map
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY, revid);//e5dbd01ec2f649e39d458540a91aa03b
		List<User> managers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.GENERAL_MANAGER);//总经理
		List<User> financesManager = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.FINANCIAL_EXECUTIVE);//财务主管
		List<User> cashiers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.CASHIER);//出纳
		List<User> finance = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.FINANCIAL);//财务
		//List<User> reviewers = valueMap.get(ReviewReceiptContext.BorrowMoneyReviewElement.REVIEWER);//审核
		String cwManager = getNames(financesManager);//财务主管
		String cashier = getNames(cashiers);//出纳
		String manager = getNames(managers);//总经理 
		String cw = getNames(finance);//财务
		//String  reviewer = getNames(reviewers);//审核
		
		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户财务主管为空
		 */
		//财务主管
		if ( !UserUtils.getUser().getCompany().getUuid().equals("7a816f5077a811e5bc1e000c29cf2586")) {
			root.put("cwmanager", cwManager);
		}else {
			root.put("cwmanager", "");
		}
		
		//财务
		root.put("cw", cw);
		
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (!UserUtils.getUser().getCompany().getUuid().equals("7a816f5077a811e5bc1e000c29cf2586")) {
			root.put("cashier", cashier);
		}else {
			root.put("cashier", "");
		}
		
		//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
		//总经理
		root.put("majorCheckPerson", manager);
		
		//部门经理
		root.put("deptmanager", cwManager);
		
		//----- wxw added 20151008 ----- 处理付款确认时间，payStatus：1 显示update时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT rev.pay_status FROM review_new rev WHERE rev.id =");
		buffer.append("'"+revid+"'");
		String payStatus = null;
		List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class);
		if (null != list && list.size() > 0) {
			payStatus = list.get(0).get("pay_status").toString();
			if (companyUUid.equals("7a816f5077a811e5bc1e000c29cf2586") || companyUUid.equals("7a81a26b77a811e5bc1e000c29cf2586")) {
				root.put("payStatus","0");
			}else {
				root.put("payStatus",payStatus);
			}
		}else {
			root.put("payStatus","0");
		}
		
	   return FreeMarkerUtil.generateFile("visaborrowmoney4xxz.ftl", "visaborrowmoney4xxz.doc", root);
	}
	
	
	/**
	 * 金额格式转化
	 * copy from visaBorrowMoneyController
	 * @param text
	 * @return
	 */
	public String fmtMicrometer(String text){
		DecimalFormat df = null;
/*		if (text.indexOf(".") > 0) {
			if (text.length() - text.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (text.length() - text.indexOf(".") - 1 == 1){
				df = new DecimalFormat("###,##0.0");
			} else{
				df = new DecimalFormat("###,##0.00");
			}

		} else{
			df = new DecimalFormat("###,##0");
		}*/
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
	 * 获取user的名称
	 * @param Users
	 * @return
	 */
	public String getNames(List<User> users) {
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
	
}
