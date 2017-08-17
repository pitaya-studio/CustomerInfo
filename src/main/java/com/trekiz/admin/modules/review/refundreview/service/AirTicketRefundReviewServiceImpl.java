package com.trekiz.admin.modules.review.refundreview.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelDao;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.dao.ActivityIslandDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.PayRemittance;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.repository.PayRemittanceDao;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.pay.dao.PayBanktransferDao;
import com.trekiz.admin.modules.pay.dao.PayDraftDao;
import com.trekiz.admin.modules.pay.dao.PayPosDao;
import com.trekiz.admin.modules.pay.entity.PayBanktransfer;
import com.trekiz.admin.modules.pay.entity.PayDraft;
import com.trekiz.admin.modules.pay.entity.PayPos;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.refundreview.repository.IAirticketRefundReviewDao;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewFlow;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewFlowDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

import freemarker.template.TemplateException;

@Service
@Transactional(readOnly = true)
public class AirTicketRefundReviewServiceImpl implements
		IAirTicketRefundReviewService {

	@Autowired
	private IAirticketRefundReviewDao airticketRefundReviewDao;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private ProductOrderService productOrderService;
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private ReviewFlowDao reviewFlowDao;
	
	@Autowired
	public MoneyAmountService moneyAmountService;
	
	@Autowired
	private HotelOrderService hotelOrderService;
	
	@Autowired
	private ActivityHotelDao activityHotelDao;
	
	@Autowired
	private IslandOrderService islandOrderService;
	
	@Autowired
	private ActivityIslandDao activityIslandDao;
	
	@Autowired
	private ActivityHotelGroupDao activityHotelGroupDao;
	
	@Autowired
	private ActivityIslandGroupDao activityIslandGroupDao;
	
	@Autowired
	private RefundDao refundDao;
	
	@Autowired
	private PayRemittanceDao payRemittanceDao;
	
	@Autowired
	private PayBanktransferDao payBanktransferDao;
	
	@Autowired
	private PayDraftDao payDraftDao;
	
	@Autowired
	private PayPosDao payPosDao;
	
	/**
	 * 根据条件查询退款审批列表 现在进入这个退款审批的产品有机票、签证、团期、机票切位、散拼切位 由于业务表不同。需提供不同的SQL去单独查询
	 * 现在的思路是 写多个DAO方法 每个DAO查询一个产品的产品信息 再有一个DAO去查询审批表(为了翻页) 查询出所有的退款审批信息
	 * 然后再去关联产品信息 这样做的前提理解是：有一条审批记录 必然会有对应的产品订单信息
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
		String refundPriceStart = request.getParameter("refundPriceStart");// 金额开始范围
		String refundPriceEnd = request.getParameter("refundPriceEnd");// 金额结束范围
		String refundName = request.getParameter("refundName"); 
		String print = request.getParameter("print");
//		String roleId = request.getParameter("roleId");//当前角色
		if(statusChoose == null){
			statusChoose = "1";
		}
		if (statusChoose != null && ("all".equals(statusChoose.trim()) || "".equals(statusChoose.trim()))) {
			statusChoose = null;// null即全部0 已驳回 1 待审核 2 审核成功 3 操作完成
		}
		if (productType != null && "-99999".equals(productType.trim())) {
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
		String orderBy = request.getParameter("orderBy");//
		if (orderBy == null || "".equals(orderBy.trim())) {
			orderBy = null;
		}
		/*新的审核 start*/
		Page<Map<String, Object>> resultPageData = new Page<>(request, response);
		//获取参数
		//选中的userJob的id
		String cOrderBy  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String uOrderBy = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		String userJobIdStr = request.getParameter("userJobId");
		UserJob userJob = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJob = userJobDao.findOne(Long.parseLong(userJobIdStr));
		}
		List<Integer> flowTypes = new ArrayList<Integer>();
		flowTypes.add(Context.REVIEW_FLOWTYPE_REFUND);
		flowTypes.add(Context.REVIEW_FLOWTYPE_OPER_REFUND);
		//获取当前用户的职位信息
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
		if(userJobs == null || userJobs.size() == 0) {
			return resultPageData;
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
		List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_REFUND, pDeptId);
		if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
			return null;
		}
		Long reviewCompanyId = reviewCompanyList.get(0);
		//获取userJob的审核层级
		List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_REFUND);
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(levels != null && levels.size() != 0){
			//处理销售退款 start
			Page<Map<String, Object>> refundReviewPageData = airticketRefundReviewDao.findRefundReviewList(request, response, groupCode, startTime, endTime, channel, saler, meter,
					statusChoose, levels, Context.REVIEW_FLOWTYPE_REFUND.toString(), cOrderBy, uOrderBy, userJob, reviewCompanyId, subIds);
			// 查询退款款项信息(主要为review_detail信息 和review信息) start
			List<Map<String, String>> reviewCompanyListMap = reviewService
					.findReviewCompanyListMap(userJob.getOrderType().intValue(), Context.REVIEW_FLOWTYPE_REFUND, false, subIds);
			// 查询退款款项信息 end
			List<Map<String, Object>> refundReviewList = refundReviewPageData
					.getList();
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
						//过滤金额范围 add by chy 2015-8-5 14:57:27
						String refundPrice = null;
						if(tempReview.get("refundPrice") != null && !"".equals(tempReview.get("refundPrice"))){
							refundPrice = tempReview.get("refundPrice");
						}
						
						if(StringUtils.isNotBlank(refundPriceStart)){//金额开始范围
							if(refundPrice == null){
								continue;
							}
							if(Double.parseDouble(refundPriceStart) > Double.parseDouble(refundPrice)){
								continue;
							}
						}
						if(StringUtils.isNotBlank(refundPriceEnd)){//金额结束范围
							if(refundPrice == null){
								continue;
							}
							if(Double.parseDouble(refundPriceEnd) < Double.parseDouble(refundPrice)){
								continue;
							}
						}
						if(StringUtils.isNotBlank(refundName) && !refundName.equals(tempReview.get("refundName").toString())){
							continue;
						}
						String printFlag="";
						if(temp.get("printFlag")!=null){
						  printFlag=temp.get("printFlag").toString();
						}
						if(StringUtils.isNotBlank(print) && !print.equals(printFlag)){
							    if(print.equals("0") && printFlag.equals("")){}
							    else continue;							
						}

						//过滤金额范围 结束
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.putAll(tempReview);
						tempMap.putAll(temp);
						list.add(tempMap);
					}
				}

			}
//			if("1".equals(statusChoose)){
//				Iterable<ReviewLog> all = reviewLogDao.findAll();
//				Iterator<ReviewLog> iterator = all.iterator();
//				for(Map<String, Object> temp : list){
////					temp.put("revstatusText", temp.get("revstatus"));
//					String revid; 
//					revid = temp.get("revid").toString();
//					while(iterator.hasNext()){
//						ReviewLog next = iterator.next();
//						Long rid = next.getReviewId();
//						Integer nowLevel = next.getNowLevel();
//						if (revid != null && !"".equals(revid.trim()) && revid.equals(rid.toString())) {
//							if(nowLevel == levels.get(0)){
////								temp.remove("revstatusText");
////								temp.put("revstatusText", next.getResult());
//								if(levels.get(0) == 1){
//									temp.remove("lastoperator");
//								}
//							} 
//							if(nowLevel == levels.get(0) - 1){
//								temp.put("lastoperator", next.getCreateBy());
//							}
//						}
//
//					}
//					iterator = all.iterator();
//				}
//			}
			//处理销售退款 end
		}
		Page<Map<String, Object>> refundReviewPageData2;
		//获取userJob的审核层级(计调退款)
		levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_OPER_REFUND);
		if(levels != null && levels.size() != 0){
			//处理计调退款 start
			refundReviewPageData2 = airticketRefundReviewDao.findRefundReviewList(request, response, groupCode, startTime, endTime, channel, saler, meter,
					statusChoose, levels, Context.REVIEW_FLOWTYPE_OPER_REFUND.toString(), cOrderBy, uOrderBy, userJob, reviewCompanyId, subIds);
			// 查询退款款项信息(主要为review_detail信息 和review信息) start
			List<Map<String, String>> reviewCompanyListMap = reviewService
					.findReviewCompanyListMap(userJob.getOrderType().intValue(), Context.REVIEW_FLOWTYPE_OPER_REFUND, false, subIds);
			// 查询退款款项信息 end
			List<Map<String, Object>> refundReviewList = refundReviewPageData2
					.getList();
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
					
					String refundPrice = null;
					if(tempReview.get("refundPrice") != null && !"".equals(tempReview.get("refundPrice"))){
						refundPrice = tempReview.get("refundPrice");
					}
					if(StringUtils.isNotBlank(refundPriceStart)){//金额开始范围
						if(refundPrice == null){
							continue;
						}
						if(Double.parseDouble(refundPriceStart) > Double.parseDouble(refundPrice)){
							continue;
						}
					}
					if(StringUtils.isNotBlank(refundPriceEnd)){//金额结束范围
						if(refundPrice == null){
							continue;
						}
						if(Double.parseDouble(refundPriceEnd) < Double.parseDouble(refundPrice)){
							continue;
						}
					}
					if(StringUtils.isNotBlank(refundName) && !refundName.equals(tempReview.get("refundName").toString())){
						continue;
					}
					String printFlag="";
					if(temp.get("printFlag")!=null){
					  printFlag=temp.get("printFlag").toString();
					}
					if(StringUtils.isNotBlank(print) && !print.equals(printFlag)){
						    if(print.equals("0") && printFlag.equals("")){}
						    else continue;
						
					}					
					if (temp.get("revid").toString()
							.equals(tempReview.get("id").toString().trim())) {
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.putAll(tempReview);
						tempMap.putAll(temp);
						list.add(tempMap);
					}
				}
			}
//			if("1".equals(statusChoose)){
//				Iterable<ReviewLog> all = reviewLogDao.findAll();
//				Iterator<ReviewLog> iterator = all.iterator();
//				for(Map<String, Object> temp : list){
//	//				temp.put("revstatusText", temp.get("revstatus"));
//					String revid; 
//					revid = temp.get("revid").toString();
//					while(iterator.hasNext()){
//						ReviewLog next = iterator.next();
//						Long rid = next.getReviewId();
//						Integer nowLevel = next.getNowLevel();
//						if (revid != null && !"".equals(revid.trim()) && revid.equals(rid.toString())) {
//							if(nowLevel == levels.get(0)){
//	//							temp.remove("revstatusText");
//	//							temp.put("revstatusText", next.getResult());
//								if(levels.get(0) == 1){
//									temp.remove("lastoperator");
//								}
//							} 
//							if(nowLevel == levels.get(0) - 1){
//								temp.put("lastoperator", next.getCreateBy());
//							}
//						}
//	
//					}
//					iterator = all.iterator();
//				}
//			}
			//处计调退款 end
		}
		if(list == null || list.size() == 0){
			return resultPageData;
		}
		String pageNoStr = request.getParameter("pageNo");
		int pageNo = 1;
		if(pageNoStr != null && !"".equals(pageNoStr)){
			pageNo = Integer.parseInt(pageNoStr);
		}
		String pageSizeStr = request.getParameter("pageSize");
		int pageSize = 10;
		if(pageSizeStr != null && !"".equals(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (pageSize >= Long.valueOf(list.size())){
			pageNo = 1;
		}
		int endNum = pageSize*pageNo;
		if(pageSize*pageNo > list.size()){
			endNum = list.size();
		}
		for(int i = pageSize*(pageNo-1); i < endNum; i++ ){
			resultList.add(list.get(i));
		}
//		resultPageData.setPageNo(pageNo);
		resultPageData.setList(resultList);
		resultPageData.setCount(Long.valueOf(list.size()));
		return resultPageData;
	}
	
	/**
	 * 更改支付状态（退款的付款状态 ） 记录支付表id
	 */
	public void updatePayStatus(OrderPayForm orderPayForm){
		
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();
		OrderPayDetail orderPayDetail = orderPayDetailList.get(0);
		
		String payId = orderPayDetail.getRefundId();
		
		Long rId = orderPayDetail.getReviewId();
		reviewService.cashConfirm(rId, "payStatus", "1");
		reviewService.cashConfirm(rId, "payId", payId);
	}
	
	/**
	 * 私有方法 判断这个level是否在level层级中
	 * @param nowLevel
	 * @param list2
	 * @return
	 */
//	private boolean isInLevel(Integer nowLevel, List<Integer> list2) {
//		for(Integer tempNum : list2){
//			if(nowLevel == tempNum){
//				return false;
//			}
//		}
//		return true;
//	}

	/**
	 * 根据条件查询参团订单详情
	 */
	@Override
	public Map<String, Object> queryGrouporderDeatail(String prdOrderId) {
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return airticketRefundReviewDao.queryGroupReviewOrderDetail(prdOrderId);
	}

	/**
	 * 根据条件查询签证订单详情 
	 */
	@Override
	public Map<String, Object> queryVisaorderDeatail(String prdOrderId) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return airticketRefundReviewDao.queryVisaReviewOrderDetail(prdOrderId);
	}
	
	/**
	 * 根据条件查询机票订单详情
	 */
	@Override
	public Map<String, Object> queryAirticketorderDeatail(String prdOrderId, String prdType) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return airticketRefundReviewDao.queryAirticketReviewOrderDetail(prdOrderId, prdType);
	}

	@Override
	public Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId) {
		
		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return airticketRefundReviewDao.querySanPinReviewOrderDetail(prdOrderId);
	}

	/**
	 * 根据订单id查询散拼切位订单详情
	 */
	@Override
	public Map<String, Object> querySanPinReserveOrderDetail(String prdOrderId) {

		if (prdOrderId == null || "".equals(prdOrderId)) {
			return null;
		}
		return airticketRefundReviewDao.querySanPinReserveOrderDetail(prdOrderId);
	}

	/**
	 * 组织退款单数据  
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public File createDownloadFile(HttpServletRequest request) throws IOException, TemplateException {
		Map<String,String> refundInfo = productOrderService.findProductOrderById(request);
		String bankInfo = refundInfo.get("bankInfo");
		if(StringUtils.isNotBlank(bankInfo)) {
			bankInfo = bankInfo.replace("</br>", " ");
			refundInfo.put("bankInfo", bankInfo);
		}
		
		String option = request.getParameter("option");
		String ftl = "refundlist_new.ftl";
		if("order".equals(option)){
			ftl = "refundlist.ftl";
		}
		
		return FreeMarkerUtil.generateFile(ftl, "refundlist.doc", refundInfo);
	}

	@Override
	public String beforeAddReview(Map<String, Object> params) {
		// 取出业务数据
		String reviewFlowId = params.get("reviewFlowId").toString();
		String orderId = params.get("orderId").toString();
		String travelerids = params.get("travelerids").toString();
		//update by zhanghao at 2015-04-24 04:45 空字符串split的时候 长度会是“1”。无法判断游客为空的情况
		if(StringUtils.isBlank(travelerids)){
			return "游客不能为空";
		}
		String[] tras = travelerids.split(",");
//		if(tras == null || tras.length == 0){
//			return "游客不能为空";
//		}
//		String rType = params.get("rType").toString();
		List<Long> travelerIds = new ArrayList<Long>();
		for(String temp : tras){
			travelerIds.add(Long.parseLong(temp));
		}
		//初始化返回值
		String result = "";
		List<Review> list;
		Set<Integer> sets = new HashSet<Integer>();
		//根据不同的审核流程进行不同的校验
		if("3".equals(reviewFlowId)){//如果是退票
			List<Integer> flowTypes = new ArrayList<Integer>();
			flowTypes.add(1);//退款
			flowTypes.add(10);//改价
			flowTypes.add(14);//改签
			flowTypes.add(16);//计调退款

			travelerIds.add(Long.parseLong("0"));
//			travelerIds.add(Long.parseLong(travelerid));
			//查询这个订单是否有退票以外的其它流程(团队改价、团队退款) 游客是否有其它审核流程
			list = reviewDao.findReview4Airticket(Context.ORDER_TYPE_JP, flowTypes, travelerIds, orderId);
			if(list == null || list.size() == 0){
				result = "";
			} else {
				result = "有";
				String tempStr = "";
				int n = 0;
				List<ReviewFlow> reviewFlow;
				for(Review rev : list){
					if(sets.contains(rev.getFlowType())){
						continue;
					}
					sets.add(rev.getFlowType());
					reviewFlow = reviewFlowDao.findReviewFlow(rev.getFlowType());
					
					if(reviewFlow != null && reviewFlow.size() != 0){
						if(n != 0){
							tempStr += "、";
						}
						tempStr += reviewFlow.get(0).getFlowName();
						n++;
					}
				}
				result += tempStr + "正在审核中，您确定退票并取消这些审核流程吗？";
			}
		} else if("1".equals(reviewFlowId) || "16".equals(reviewFlowId)){//如果是退款
			List<Integer> flowTypes = new ArrayList<Integer>();
			flowTypes.add(1);//退款
			flowTypes.add(16);//计调退款
//			List<Long> travelerIds = new ArrayList<Long>();
//			travelerIds.add(Long.parseLong(travelerid));
			//查询这个订单或游客是否有其它退款流程
			list = reviewDao.findReview4Airticket(Context.ORDER_TYPE_JP, flowTypes, travelerIds, orderId);
			if(list == null || list.size() == 0){
				result = "";
			} else {
				result = "已经有退款流程在审核了，不能再次进行申请";
			}
		}	else if("19".equals(reviewFlowId)){//如果是借款
			List<Integer> flowTypes = new ArrayList<Integer>();
			flowTypes.add(3);//退票
			flowTypes.add(19);//借款
//			List<Long> travelerIds = new ArrayList<Long>();
//			travelerIds.add(Long.parseLong(travelerid));
			//查询这个订单或游客是否有其它借款流程或退票流程
			list = reviewDao.findReview4Airticket(Context.ORDER_TYPE_JP, flowTypes, travelerIds, orderId);
			if(list == null || list.size() == 0){
				result = "";
			} else {
				int n = 0;
				String tempStr = "";
				List<ReviewFlow> reviewFlow;
				for(Review rev : list) {
					if(sets.contains(rev.getFlowType())){
						continue;
					}
					sets.add(rev.getFlowType());
					reviewFlow = reviewFlowDao.findReviewFlow(rev.getFlowType());
					if(reviewFlow != null && reviewFlow.size() != 0){
						if(n != 0){
							tempStr += "、";
						}
						tempStr += reviewFlow.get(0).getFlowName();
						n++;
					}
				}
				result = "已经有"+ tempStr +"在审核了，不能继续申请";
			}
		} else if("10".equals(reviewFlowId)){//如果是改价
			List<Integer> flowTypes = new ArrayList<Integer>();
			flowTypes.add(3);//退票
			flowTypes.add(10);//改价
//			List<Long> travelerIds = new ArrayList<Long>();
//			travelerIds.add(Long.parseLong(travelerid));
			//查询这个游客是否已有申请改价流程 和退票流程
			list = reviewDao.findReview4Airticket(Context.ORDER_TYPE_JP, flowTypes, travelerIds, orderId);
			if(list == null || list.size() == 0){
				result = "";
			} else {
				String tempStr = "";
				int n = 0;
				List<ReviewFlow> reviewFlow;
				for(Review rev : list) {
					if(sets.contains(rev.getFlowType())){
						continue;
					}
					sets.add(rev.getFlowType());
					reviewFlow = reviewFlowDao.findReviewFlow(rev.getFlowType());
					if(reviewFlow != null && reviewFlow.size() != 0){
						if(n != 0){
							tempStr += "、";
						}
						tempStr += reviewFlow.get(0).getFlowName();
						n++;
					}
				}
				result += result = "有" + tempStr + "在审核中，不能继续申请";
			}
		} else if("14".equals(reviewFlowId)){//如果是改签
			List<Integer> flowTypes = new ArrayList<Integer>();
			flowTypes.add(3);//退票
			flowTypes.add(14);//改签
//			List<Long> travelerIds = new ArrayList<Long>();
//			travelerIds.add(Long.parseLong(travelerid));
			//查询这个游客是否已有申请改价流程 和退票流程
			list = reviewDao.findReview4Airticket(Context.ORDER_TYPE_JP, flowTypes, travelerIds, orderId);
			if(list == null || list.size() == 0){
				result = "";
			} else {
				String tempStr = "";
				int n = 0;
				List<ReviewFlow> reviewFlow;
				for(Review rev : list) {
					if(sets.contains(rev.getFlowType())){
						continue;
					}
					sets.add(rev.getFlowType());
					reviewFlow = reviewFlowDao.findReviewFlow(rev.getFlowType());
					if(reviewFlow != null && reviewFlow.size() != 0){
						if(n != 0){
							tempStr += "、";
						}
						tempStr += reviewFlow.get(0).getFlowName();
						n++;
					}
				}
				result += result = "您有" + tempStr + "在审核中，不能继续申请";
			}
		} 
		return result;
	}
	/**
	 * 取消其它审核流程
	 */
	@Override
	public String cancelOtherReview(Map<String, Object> params) {
		// 取出业务数据
		String reviewFlowId = params.get("reviewFlowId").toString();
		String orderId = params.get("orderId").toString();
		String travelerids = params.get("travelerids").toString();
		String[] tras = travelerids.split(",");
		if(tras == null || tras.length == 0){
			return "游客不能为空";
		}
//		String rType = params.get("rType").toString();
		List<Long> travelerIds = new ArrayList<Long>();
		for(String temp : tras){
			travelerIds.add(Long.parseLong(temp));
		}
		if("3".equals(reviewFlowId)){//如果是退票
			List<Integer> flowTypes = new ArrayList<Integer>();
			flowTypes.add(1);//退款
			flowTypes.add(10);//改价
			flowTypes.add(14);//改签
			flowTypes.add(16);//计调退款
			flowTypes.add(19);//借款
//			List<Long> travelerIds = new ArrayList<Long>();
			travelerIds.add(Long.parseLong("0"));
//			travelerIds.add(Long.parseLong(travelerid));
			//查询这个订单是否有退票以外的其它流程(团队改价、团队退款) 游客是否有其它审核流程
			List<Review> list = reviewDao.findReview4Airticket(Context.ORDER_TYPE_JP, flowTypes, travelerIds, orderId);
			if(list == null || list.size() == 0){
				return "success";
			}
			for(Review r : list){
				r.setStatus(Context.REVIEW_STATUS_CANCEL);
			}
			reviewDao.save(list);
		}
		return "success";
	}

	/**
	 * 退款业务数据处理
	 */
	@Override
	public boolean doRefund(Review review) {
		Map<String, String> reviewDetail = reviewService.findReview(review.getId());
		List<MoneyAmount> moneyList = new ArrayList<MoneyAmount>();
		MoneyAmount ma = new MoneyAmount();
		ma.setAmount(BigDecimal.valueOf(Double.valueOf(reviewDetail.get("refundPrice"))));//款数
		ma.setOrderType(review.getProductType());//订单类型 即 产品类型
		ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
		ma.setUid(Long.parseLong(review.getOrderId()));//订单id
		ma.setCurrencyId(Integer.parseInt(reviewDetail.get("currencyId")));//币种
		ma.setBusindessType(1);//1标示订单退款
		moneyList.add(ma);
		moneyAmountService.saveMoneyAmounts(moneyList);
		return true;
	}

	/**
	 * 付款审核退款单
	 * @author jiachen
	 * @DateTime 2015年6月15日 下午2:39:49
	 * @param map
	 * @param reviewId
	 * @return void
	 */
	@Override
	public void refundTable(String reviewId, Map<String, Object> map) {
		
		//获取审核信息
		Map<String, String> review = reviewService.findReview(Long.valueOf(reviewId));
		String orderType = review.get("productType");
		String orderId = review.get("orderId");
		map.put("remarks", FreeMarkerUtil.StringFilter(review.get("remark") == null ? "" : review.get("remark")));//退款说明
		String currencyMark = review.get("currencyMark").toString().replace(",", "");
		String strMoney = review.get("refundPrice").toString().replace(",", "");
		BigDecimal multiply = new BigDecimal(1).multiply(BigDecimal.valueOf(Double.parseDouble(strMoney)));
		map.put("refundPrice", currencyMark+" " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(new DecimalFormat("#.00").format(multiply)), 
				MoneyNumberFormat.THOUSANDST_POINT_TWO));//退款金额
		String date = review.get("createDate");
		map.put("createDate", date.substring(0, 4) + "年" + date.substring(5, 7) + "月" + date.substring(8, 10) + "日");
		
		//获取订单和产品信息，其中包含酒店和海岛游订单
		String groupCode = "";	//团编号
		String productName = "";		//产品名称
		String agentName = "";	//渠道名称
		
		//酒店订单
		if(Context.ORDER_STATUS_HOTEL.equals(orderType)) {
			HotelOrder hotelOrder = hotelOrderService.getById(Integer.valueOf(orderId));
			if(null != hotelOrder) {
				ActivityHotelGroup activityHotelGroup = activityHotelGroupDao.getByUuid(hotelOrder.getActivityHotelGroupUuid());
				groupCode = activityHotelGroup.getGroupCode();
				agentName = hotelOrder.getOrderCompanyName();
				productName = activityHotelDao.getByUuid(hotelOrder.getActivityHotelUuid()).getActivityName();
			}
		//海岛游订单
		}else if(Context.ORDER_STATUS_ISLAND.equals(orderType)){
			IslandOrder islandOrder = islandOrderService.getById(Integer.valueOf(orderId));
			if(null != islandOrder) {
				ActivityIslandGroup activityIslandGroup = activityIslandGroupDao.getByUuid(islandOrder.getActivityIslandGroupUuid());
				groupCode = activityIslandGroup.getGroupCode();
				agentName = islandOrder.getOrderCompanyName();
				productName = activityIslandDao.getByUuid(islandOrder.getActivityIslandUuid()).getActivityName();
			}
		}
		
		map.put("orderNum", groupCode);
		map.put("productName", productName);
		map.put("agentName", agentName);
		List<Refund> list = refundDao.findLastPayByRecordId(Long.parseLong(reviewId),2,Integer.parseInt(orderType));
		if(null != list && list.size()>0) {
			map.put("payerName", list.get(0).getPayee());
			//支票和现金支付
			if(list.get(0).getPayType()==1 || list.get(0).getPayType()==3){
				map.put("bankName", "");
				map.put("bankAccount", "");
			//汇款
			}else if(list.get(0).getPayType()==4){
				PayRemittance payRemittance = payRemittanceDao.findPayRemittanceInfoById(list.get(0).getPayTypeId());
				map.put("bankName", payRemittance.getTobankName());
				map.put("bankAccount", payRemittance.getTobankAccount());
			//银行转账
			}else if(list.get(0).getPayType()==6){
				PayBanktransfer payBanktransfer = payBanktransferDao.getByUuid(list.get(0).getPayTypeId());
				map.put("bankName", payBanktransfer.getReceiveBankName());
				map.put("bankAccount", payBanktransfer.getReceiveAccount());
			//汇票
			}else if(list.get(0).getPayType()==7){
				PayDraft payDraft = payDraftDao.getByUuid(list.get(0).getPayTypeId());
				map.put("bankName", payDraft.getReceiveBankName());
				map.put("bankAccount", payDraft.getReceiveAccount());
			//POS机刷卡
			}else if(list.get(0).getPayType()==8){
				PayPos payPos = payPosDao.getByUuid(list.get(0).getPayTypeId());
				map.put("bankName", payPos.getReceiveBankName());
				map.put("bankAccount", payPos.getReceiveAccount());
			}
		}
		map.put("reviewId", review.get("id"));
	}
	
	/**
	 * 下载退款审核退款单word版
	 * @author jiachen
	 * @DateTime 2015年6月16日 上午10:24:26
	 * @param reviewId
	 * @return File
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	@Override
	public File downLoadRefundTable(String reviewId) throws IOException, TemplateException {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(reviewId)) {
			Map<String, Object> sourceDataMap = new HashMap<String, Object>();
			refundTable(reviewId, sourceDataMap);
			dataMap.put("createDate", sourceDataMap.get("createDate") == null ? "" : sourceDataMap.get("createDate"));
			dataMap.put("orderNum", sourceDataMap.get("orderNum") == null ? "" : sourceDataMap.get("orderNum"));
			dataMap.put("productName", sourceDataMap.get("productName") == null ? "" : sourceDataMap.get("productName"));
			dataMap.put("agentName", sourceDataMap.get("agentName") == null ? "" : sourceDataMap.get("agentName"));
			dataMap.put("remarks", sourceDataMap.get("remarks") == null ? "" : sourceDataMap.get("remarks"));
			dataMap.put("refundPrice", sourceDataMap.get("refundPrice") == null ? "" : sourceDataMap.get("refundPrice"));
			dataMap.put("payerName", sourceDataMap.get("payerName") == null ? "" : sourceDataMap.get("payerName"));
			dataMap.put("bankName", sourceDataMap.get("bankName") == null ? "" : sourceDataMap.get("bankName"));
			dataMap.put("bankAccount", sourceDataMap.get("bankAccount") == null ? "" : sourceDataMap.get("bankAccount"));
		}
		return FreeMarkerUtil.generateFile("refundTable.ftl", "refundTable.doc", dataMap);
	}
	
}
