package com.trekiz.admin.modules.review.depositereview.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.depositereview.repository.IDepositeRefundReviewDao;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.repository.VisaDao;

@Service
@Transactional(readOnly = true)
public class DepositeRefundReviewServiceImpl implements
		IDepositeRefundReviewService {

	@Autowired
	private IDepositeRefundReviewDao depositeRefundReviewDao;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private VisaDao visaDao;
	
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private ReviewLogDao reviewLogDao;
	
	/**
	 * 财务审核--退签证押金审核
	 */
	@Override
	public Page<Map<String, Object>> queryCostRefundReviewList(Map<String, String> params, 
			Page<Map<String, Object>> page) {
		// 1 获取参数start
		String userJobIdStr = params.get("userJobId");
		UserJob userJob = null;
		if(StringUtils.isNotBlank(userJobIdStr)){
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		//获取第一层级部门id
		Long pDeptId;
		//子部门id
		List<Long> subIds = new ArrayList<Long>();
		//如果userJob为null则表明是第一次进入审核页面  默认取第一个userJob
		if(userJob == null){
			userJob = userJobs.get(userJobs.size()-1);
		}
		
		if(userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
			subIds = departmentDao.findSubidsByParentId(pDeptId);
		} else {
			pDeptId = userJob.getParentDept();
			subIds.add(userJob.getDeptId());
		}
		//获取reviewComppanyid
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, 
				Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), 
				Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
		if(levels == null || levels.size() == 0){
			return null;
		}
		// 1 获取参数end
		// 2 查询数据 start
		// 2.1查询审核表数据  start
		Page<Map<String, Object>> refundReviewPageData = depositeRefundReviewDao
				.findCostRefundReviewList(params, page, levels, userJob, reviewCompanyId, subIds);
		// 2.1查询审核表数据 end
		// 查询款项信息 start
		List<Map<String, String>> reviewCompanyListMap = reviewService
				.findReviewCompanyListMap(userJob.getOrderType().intValue(), 
						Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND, false, subIds);
		// 查询款项信息 end
		// 3.组织审批信息和款项信息 返回页面展示 start
		List<Map<String, Object>> refundReviewList = refundReviewPageData
				.getList();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 3.组织审批信息和款项信息 返回页面展示 end
		// 整合信息数据返回
		for (Map<String, Object> temp : refundReviewList) {
			temp.put("revLevel", levels.get(0));
			for (Map<String, String> tempReview : reviewCompanyListMap) {
				if (temp.get("revid") == null
						|| "".equals(temp.get("revid").toString().trim())
						|| tempReview.get("id") == null
						|| "".equals(tempReview.get("id").toString().trim())) {
					continue;
				}
				if (temp.get("revid").toString()
						.equals(tempReview.get("id").toString().trim())) {
					temp.put("depmoney", moneyAmountService.getMoney(temp.get("depmoney").toString()));
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.putAll(tempReview);
					tempMap.putAll(temp);
					list.add(tempMap);
				}
			}
		}
		refundReviewPageData.setList(list);
		return refundReviewPageData;
	}
	/**
	 * 根据条件查询退签证押金审批列表 
	 */
	@Override
	public Page<Map<String, Object>> queryRefundReviewList(
			HttpServletRequest request, HttpServletResponse response) {
		// 1 获取参数start
		String groupCode = request.getParameter("groupCode");// 团号
		String statusChoose = request.getParameter("statusChoose");// 状态过滤 默认为全部
		String productType = request.getParameter("orderType");// 团队类型 即产品类型
		String channel = request.getParameter("channel");// 渠道
		String saler = request.getParameter("saler");// 销售
		String meter = request.getParameter("meter");// 计调
		if(statusChoose == null){
			statusChoose = "1";
		}
		if (statusChoose != null && ("all".equals(statusChoose.trim()) || "".equals(statusChoose.trim()))) {
			statusChoose = null;// null即全部0 已驳回 1 待审核 2 审核成功 3 操作完成
		}
		if (productType != null && "all".equals(productType.trim())) {
			productType = null;
		}
		String startTime = null;
		if (request.getParameter("startTime") != null
				&& !"".equals(request.getParameter("startTime").trim())) {
			startTime = request.getParameter("startTime");
		}
		String endTime = null;
		if (request.getParameter("endTime") != null
				&& !"".equals(request.getParameter("endTime").trim())) {
			endTime = request.getParameter("endTime");
		}
		if (channel != null && "-99999".equals(channel.trim())) {
			channel = null;
		}
		if (saler != null && "-99999".equals(saler.trim())) {
			saler = null;
		}
		if (meter != null && "-99999".equals(meter.trim())) {
			meter = null;
		}
//		String orderBy = request.getParameter("orderBy");//
//		if (orderBy == null || "".equals(orderBy.trim())) {
//			orderBy = null;
//		}
		String cOrderBy  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String uOrderBy = request.getParameter("orderUpdateDateSort");// 更新日期排序标识

		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		//获取当前用户的职位信息
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
		if(userJobs == null || userJobs.size() == 0) {
			return null;
		}
		//获取第一层级部门id
		Long pDeptId;
		//子部门id
		List<Long> subIds = new ArrayList<Long>();
		//如果userJob为null则表明是第一次进入审核页面  默认取第一个userJob
		if(userJob == null){
			userJob = userJobs.get(userJobs.size()-1);
		}
		
		if(userJob.getDeptLevel() == 1){
			pDeptId = userJob.getDeptId();
			subIds = departmentDao.findSubidsByParentId(pDeptId);
		} else {
			pDeptId = userJob.getParentDept();
			subIds.add(userJob.getDeptId());
		}
		//获取reviewComppanyid
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
		if(levels == null || levels.size() == 0){
			return null;
		}
		// 1 获取参数end
		// 2 查询数据 start
		// 2.1查询审核表数据  start
		Page<Map<String, Object>> refundReviewPageData = depositeRefundReviewDao
				.findRefundReviewList(request, response, groupCode,
						productType, startTime, endTime, channel, saler, meter,
						statusChoose, levels, Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND.toString(), cOrderBy, uOrderBy, userJob, reviewCompanyId, subIds);
		// 2.1查询审核表数据 end
		// 查询款项信息 start
		List<Map<String, String>> reviewCompanyListMap = reviewService
				.findReviewCompanyListMap(userJob.getOrderType().intValue(), Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND, false, subIds);
		// 查询款项信息 end
		// 3.组织审批信息和款项信息 返回页面展示 start
		List<Map<String, Object>> refundReviewList = refundReviewPageData
				.getList();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 3.组织审批信息和款项信息 返回页面展示 end
		// 整合信息数据返回
		for (Map<String, Object> temp : refundReviewList) {
			temp.put("revLevel", levels.get(0));
			for (Map<String, String> tempReview : reviewCompanyListMap) {
				if (temp.get("revid") == null
						|| "".equals(temp.get("revid").toString().trim())
						|| tempReview.get("id") == null
						|| "".equals(tempReview.get("id").toString().trim())) {
					continue;
				}
				if (temp.get("revid").toString()
						.equals(tempReview.get("id").toString().trim())) {
					temp.put("depmoney", moneyAmountService.getMoney(temp.get("depmoney").toString()));
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.putAll(tempReview);
					tempMap.putAll(temp);
					list.add(tempMap);
				}
			}
		}
//		Iterable<ReviewLog> all = reviewLogDao.findAll();
//		Iterator<ReviewLog> iterator = all.iterator();
//			while(iterator.hasNext()){
//				ReviewLog next = iterator.next();
//				Long rid = next.getReviewId();
//				Integer nowLevel = next.getNowLevel();
//				String revid; 
//				for(Map<String, Object> temp : list){
//					revid = temp.get("revid").toString();
//					if (revid != null && !"".equals(revid.trim()) && revid.equals(rid.toString())) {
//						if(!isInLevel(nowLevel, levels)){//如果审核层级不在要审核的list中 则证明这条记录是审核记录
//							temp.remove("revstatus");
//							temp.put("revstatus", next.getResult());
//							if(levels.get(0) == 1){
//								temp.remove("lastoperator");//上一环节操作人
//							}
//						}
//						if(nowLevel == levels.get(0) - 1){
//							temp.put("lastoperator", next.getCreateBy());
//						}
//					}
//				}
//			}
		refundReviewPageData.setList(list);
		return refundReviewPageData;		
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
	public void doDepositeRefund(Review review) {
		Map<String, String> reviewDetail = reviewService.findReview(review.getId());
		MoneyAmount ma = new MoneyAmount();
		String uuid = UUID.randomUUID().toString();
		ma.setSerialNum(uuid);
		String amount = reviewDetail.get("depositPrice");
		if(amount != null && !"".equals(amount)){
			ma.setAmount(BigDecimal.valueOf(Double.valueOf(amount)));//款数
		} else {
			ma.setAmount(BigDecimal.valueOf(0));//款数
		}
		ma.setOrderType(review.getProductType());//订单类型 即 产品类型
		ma.setMoneyType(Context.MONEY_TYPE_TYJ);//款项类型退签证押金 是15
		ma.setUid(Long.parseLong(reviewDetail.get("travelerId")));//游客id
		ma.setCurrencyId(Integer.parseInt(reviewDetail.get("depositPriceCurrency")));//币种
		ma.setBusindessType(2);//2标示游客退款
		//moneyamount表存储一条数据
		moneyAmountService.saveOrUpdateMoneyAmount(ma);
		//visa表的已退押金字段
		Visa visa = visaDao.findByTravelerId(Long.parseLong(reviewDetail.get("travelerId")));
		visa.setReturnedDeposit(uuid);
		visaDao.save(visa);
		String payedUUID = visa.getPayedDeposit();//已收押金UUID
		String accountedUUID = visa.getAccountedDeposit();//达账押金UUID
		List<MoneyAmount> payedMoneys = moneyAmountDao.findAmountBySerialNum(payedUUID);
		List<MoneyAmount> accountedMoneys = moneyAmountDao.findAmountBySerialNum(accountedUUID);
		for(MoneyAmount temp : payedMoneys){//更改已付押金
			if(temp.getCurrencyId() == Integer.parseInt(reviewDetail.get("depositPriceCurrency"))){
				temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(amount))));
				moneyAmountService.saveOrUpdateMoneyAmount(temp);
				break;
			}
		}
		for(MoneyAmount temp : accountedMoneys){//更改达账押金
			if(temp.getCurrencyId() == Integer.parseInt(reviewDetail.get("depositPriceCurrency"))){
				temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(amount))));
				moneyAmountService.saveOrUpdateMoneyAmount(temp);
				break;
			}
		}
	
	}
	
}
