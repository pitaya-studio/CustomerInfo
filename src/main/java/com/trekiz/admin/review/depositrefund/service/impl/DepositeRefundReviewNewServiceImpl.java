package com.trekiz.admin.review.depositrefund.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.depositrefund.dao.IDepositeRefundReviewNewDao;
import com.trekiz.admin.review.depositrefund.service.IDepositeRefundReviewNewService;

@Service
@Transactional(readOnly = true)
public class DepositeRefundReviewNewServiceImpl implements
		IDepositeRefundReviewNewService {

	@Autowired
	private IDepositeRefundReviewNewDao depositeRefundReviewDao;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private VisaDao visaDao;
	
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private SystemService systemService;
	
	/**
	 * 财务审核--退签证押金审核
	 */
	@Override
	public Page<Map<String, Object>> queryCostRefundReviewList(Map<String, String> params, 
			Page<Map<String, Object>> page) {
		return null;
	}
	/**
	 * 根据条件查询退签证押金审批列表 
	 */
	@Override
	public Page<Map<String, Object>> queryRefundReviewList(Map<String, Object> params) {

		//获取当前登录用户的id
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		//获取有查看权限的部门和产品
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowType(userId, companyUuid, Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
		Set<String> deptIds = userReviewPermissionResultForm.getDeptId();
		String deptIdStr = "";
		if(deptIds != null && deptIds.size() > 0){
			int n = 0;
			for(String str : deptIds){
				if(n == 0){
					deptIdStr += str;
					n++;
				} else {
					deptIdStr += "," + str;
				}
			}
		}
		if("".equals(deptIdStr)){//给默认值
			deptIdStr = "-1,-2";
		}
//		userReviewPermissionResultForm.getProductTypeId();
		/*声明查询SQL*/
		StringBuffer querySql = new StringBuffer();
		querySql.append("select r.order_no orderno,r.id reviewid ").//订单编号
			append(",r.order_id orderid ").//订单id
			append(",r.group_code groupcode ").//团号
			append(",r.group_id groupid ").//团期id
			append(",r.product_name productname ").//产品名称
			append(",r.product_id productid ").//产品id
			append(",r.product_type producttype ").//产品类型
			append(",r.create_date createdate ").//申请时间
			append(",r.create_by createby ").//审批发起人
			append(",r.agent agent ").//渠道商
			append(",r.operator operator ").//计调
			append(",r.traveller_id traveler ").//游客
			append(",r.traveller_name travelername ").//游客姓名
			append(",m.currencyId currencyid ").//金额币种
			append(",m.amount amount ").//押金金额
			append(",r.last_reviewer lastreviewer ").//上一环节审批人
			append(",r.status status ").//审批状态
			append(",r.pay_status paystatus ").//出纳确认
			append(",r.print_status printstatus ").//打印状态
			append(" from review_new r left join review_process_money_amount m on r.id = m.reviewId ").//
			append(" where 1 = 1 and r.company_id = '" + companyUuid + "' and r.product_type = " + Context.PRODUCT_TYPE_QIAN_ZHENG + " ").
			append("and r.process_type = '" + Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND + "' and (r.dept_id in (" + deptIdStr + ") ").//
			append("or FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");
		/*拼装查询条件*/
		Object groupCode = params.get("groupCode");
		if(groupCode != null && !"".equals(groupCode.toString())){
			querySql.append(" and (r.group_code like '%" + groupCode.toString()).
				append("%' or r.product_name like '%" + groupCode.toString()).
				append("%' or r.order_no like '%" + groupCode.toString() + "%') ");
		}
		Object agentId = params.get("agentId");
		if(agentId != null && !"".equals(agentId.toString())){
			querySql.append(" and r.agent = " + agentId.toString() + " ");
		}
		Object applyDateFrom = params.get("applyDateFrom");
		if(applyDateFrom != null && !"".equals(applyDateFrom.toString())){
			querySql.append(" and r.create_date >= '" + applyDateFrom.toString() + " 00:00:00' ");
		}
		Object applyDateTo = params.get("applyDateTo");
		if(applyDateTo != null && !"".equals(applyDateTo.toString())){
			querySql.append(" and r.create_date <= '" + applyDateTo.toString() + " 23:59:59' ");
		}
		Object applyPerson = params.get("applyPerson");
		if(applyPerson != null && !"".equals(applyPerson.toString())){
			querySql.append(" and r.create_by = " + applyPerson.toString() + " ");
		}
		/*游客姓名*/
		Object traveler = params.get("traveler");
		if(traveler != null && !"".equals(traveler.toString().trim())){
			querySql.append(" and r.traveller_name like '%" + traveler.toString() + "%' ");
		}
		/*审批状态  空 为全部 1 审批中 2 已通过 0 未通过*/
		Object reviewStatus = params.get("reviewStatus");
		if(reviewStatus != null && !"".equals(reviewStatus.toString()) && NumberUtils.isNumber(reviewStatus.toString())){
			querySql.append(" and r.status = " + Integer.parseInt(reviewStatus.toString()) + " ");
		}
		/*状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批*/
		Object tabStatus = params.get("tabStatus");
		if(tabStatus != null && !"".equals(tabStatus.toString()) && NumberUtils.isNumber(tabStatus.toString()) && !Context.REVIEW_TAB_ALL.equals(tabStatus.toString())){
			int tabStatusInt = Integer.parseInt(tabStatus.toString());
			if( Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == tabStatusInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt){
//				querySql.append(" and FIND_IN_SET ('" + userId + "', (select l.create_by from review_log_new l where l.review_id = r.id and operation = 1 and l.active_flag = 1)) ");
				querySql.append(" and r.id in (select review_id from review_log_new  where operation in (1,2) and active_flag = 1 and create_by = '" + userId + "') ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt){//不在审核中
				querySql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");//and FIND_IN_SET('" + userId + "', r.skipped_assignee)
			}
		} else if(tabStatus == null || "".equals(tabStatus.toString())){//默认待本人审核
			querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
		}
		//排序 默认按重要程度降序 按创建时间降序 
		querySql.append(" order by r.critical_level desc ");
		
		Object orderCreateDateSort = params.get("orderCreateDateSort");
		Object orderUpdateDateSort = params.get("orderUpdateDateSort");
		if(orderCreateDateSort != null && !"".equals(orderCreateDateSort.toString())){
			querySql.append(" ,r.create_date " + orderCreateDateSort.toString() + " ");
		} else if(orderUpdateDateSort != null && !"".equals(orderUpdateDateSort.toString())){
			querySql.append(" ,r.update_date " + orderUpdateDateSort.toString() + " ");
		} else {
			querySql.append(" ,r.create_date desc ");
		}
		//执行SQL查询出列表数据
		@SuppressWarnings("unchecked")
		Page<Map<String, Object>> page = depositeRefundReviewDao.findBySql((Page<Map<String, Object>>)params.get("pageP"), querySql.toString());
//		为列表数据组装审核变量
		List<Map<String, Object>> list = page.getList();
		Object reviewid = null;
		Object status = null;
		for(Map<String, Object> map : list){
			reviewid = map.get("reviewid");
			if(reviewid == null || "".equals(reviewid.toString())){
				continue;
			}
			
			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewid.toString());
			map.putAll(reviewMap);
			map.put("isBackReview", ReviewUtils.isBackReview(reviewid.toString()));
			map.put("isCurReviewer", ReviewUtils.isCurReviewer(reviewMap.get("currentReviewer")));
			status = map.get("status");
			if(status == null || StringUtils.isBlank(status.toString()) || NumberUtils.isDigits(status.toString())){
				map.put("statusdesc" ,"无审批状态");
				continue;
			}
			if(ReviewConstant.REVIEW_STATUS_PROCESSING == Integer.parseInt(map.get("status").toString())){
				Object cReviewer = map.get("currentReviewer");
				String person = "";
				if(cReviewer != null){
					person = getReviewerDesc(cReviewer);
				}
				map.put("statusdesc" ,"待" + person + "审批");
			} else {
				map.put("statusdesc" ,getReviewStatus(Integer.parseInt(map.get("status").toString())));
			}
			reviewid = null;
			status = null;
		}
//		page.setList(list);
		return page;
	}
	
	/**
	 * 获取当前审核人描述 由id转化为name
	 * @param cReviewer
	 * @return
	 */
	private String getReviewerDesc(Object cReviewer) {
		String reviewers = cReviewer.toString();
		String[] reviewArr = reviewers.split(",");
		String result = "";
		int n = 0;
		String tName = "";
		for(String temp : reviewArr){
			if(StringUtils.isBlank(temp)){
				continue;
			}
			tName = UserUtils.getUserNameById(Long.parseLong(temp));
			if(n == 0){
				result += tName;
			} else {
				result += "," + tName;
			}
		}
		return result;
	}
	
	private String getReviewStatus(Integer status) {
		if(ReviewConstant.REVIEW_STATUS_CANCELED == status){
			return ReviewConstant.REVIEW_STATUS_CANCELED_DES;
		} else if(ReviewConstant.REVIEW_STATUS_PASSED == status){
			return ReviewConstant.REVIEW_STATUS_PASSED_DES;
		} else if(ReviewConstant.REVIEW_STATUS_REJECTED == status){
			return ReviewConstant.REVIEW_STATUS_REJECTED_DES;
		} 
		return "无";
	}
	
	/**
	 * 根据条件查询签证订单详情 
	 */
	@Override
	public Map<String, Object> queryVisaorderDeatail(String prdOrderId) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return depositeRefundReviewDao.queryVisaReviewOrderDetail(prdOrderId);
	}

	/**
	 * 出纳确认功能 因为一个游客只有一个签证 所以可以通过游客去找签证 更改签证的出纳确认状态
	 */
	@Override
	public void cashConfirm(String travelerId) {
		Visa visa = visaDao.findByTravelerId(Long.parseLong(travelerId));
		visa.setReturnedDepositStatus(1);//1标示确认已付款 即 已退签证押金
		visaDao.save(visa);
	}

	/**
	 * 退签证押金公共接口实现类
	 */
	@Override
	public void doDepositeRefund(ReviewNew review) {
		Map<String, Object> reviewDetail = reviewService.getReviewDetailMapByReviewId(review.getId());
		MoneyAmount ma = new MoneyAmount();
		String uuid = UUID.randomUUID().toString();
		ma.setSerialNum(uuid);
		Object amount = reviewDetail.get("depositPrice");
		if(amount != null && !"".equals(amount.toString().trim())){
			ma.setAmount(BigDecimal.valueOf(Double.valueOf(amount.toString())));//款数
		} else {
			ma.setAmount(BigDecimal.valueOf(0));//款数
		}
		ma.setOrderType(Integer.parseInt(review.getProductType().toString()));//订单类型 即 产品类型
		ma.setMoneyType(Context.MONEY_TYPE_TYJ);//款项类型退签证押金 是15
		ma.setUid(Long.parseLong(reviewDetail.get("travelerId").toString()));//游客id
		ma.setCurrencyId(Integer.parseInt(reviewDetail.get("depositPriceCurrency").toString()));//币种
		ma.setBusindessType(2);//2标示游客退款
		//moneyamount表存储一条数据
		moneyAmountService.saveOrUpdateMoneyAmount(ma);
		//visa表的已退押金字段
		Visa visa = visaDao.findByTravelerId(Long.parseLong(reviewDetail.get("travelerId").toString()));
		visa.setReturnedDeposit(uuid);
		visaDao.save(visa);
		String payedUUID = visa.getPayedDeposit();//已收押金UUID
		String accountedUUID = visa.getAccountedDeposit();//达账押金UUID
		List<MoneyAmount> payedMoneys = moneyAmountDao.findAmountBySerialNum(payedUUID);
		List<MoneyAmount> accountedMoneys = moneyAmountDao.findAmountBySerialNum(accountedUUID);
		for(MoneyAmount temp : payedMoneys){//更改已付押金
			if(temp.getCurrencyId() == Integer.parseInt(reviewDetail.get("depositPriceCurrency").toString())){
				temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(amount.toString()))));
				moneyAmountService.saveOrUpdateMoneyAmount(temp);
				break;
			}
		}
		for(MoneyAmount temp : accountedMoneys){//更改达账押金
			if(temp.getCurrencyId() == Integer.parseInt(reviewDetail.get("depositPriceCurrency").toString())){
				temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(amount.toString()))));
				moneyAmountService.saveOrUpdateMoneyAmount(temp);
				break;
			}
		}
	
	}
	
}
