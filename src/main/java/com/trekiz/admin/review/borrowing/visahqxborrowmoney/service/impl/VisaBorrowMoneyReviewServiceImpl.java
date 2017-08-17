package com.trekiz.admin.review.borrowing.visahqxborrowmoney.service.impl;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.borrowing.visahqxborrowmoney.repository.IVisaBorrowMoneyReviewDao;
import com.trekiz.admin.review.borrowing.visahqxborrowmoney.service.IVisaBorrowMoneyReviewService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class VisaBorrowMoneyReviewServiceImpl implements IVisaBorrowMoneyReviewService {
	@Autowired
	ReviewService reviewService;
	@Autowired
	private IVisaBorrowMoneyReviewDao visaBorrowMoneyReviewDao;
	@Autowired
	private ReviewService processReviewService;
	@Autowired
	private TravelerService travelerService;
    @Autowired
    private MoneyAmountService moneyAmountService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    public static DecimalFormat df = new DecimalFormat("#,##0.00");
    
	@Override
    @Transactional(readOnly = true)
	public Page<Map<String, Object>> queryBorrowMoneyReviewList(Map<String, Object> params) {
		// 获取当前登录用户的id
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowType(userId, companyUuid, Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY);
		
		/* 声明查询SQL */
		StringBuffer querySql = new StringBuffer();
		querySql.append("select r.batch_no batchNo , r.id reviewId ")
				.append(",r.create_date createdate ")											// 申请时间
				.append(",r.create_by createby ")												// 审批发起人
				.append(",m.amount amount ") 													// 借款单价
				.append(",vsbo.batch_total_money totalBorrowMoney ") 							// 借款总额
				.append(",m.currencyId currencyId ") 											// 借款金额币种符号
				.append(",r.last_reviewer lastreviewer ") 										// 上一环节审批人
				.append(",r.status status ") 													// 审批状态
				.append(",r.pay_status paystatus ") 											// 出纳确认
				.append(",r.print_status printstatus ")											// 打印状态
				.append(", vsbo.batch_person_count batchPersonCount ")							// 借款人数
				.append("from review_new r, visa_order vorder, visa_products vp, ")
				.append("review_process_money_amount m, visa_flow_batch_opration vsbo ")
				.append("where r.del_flag = '0' and r.order_id = vorder.id and vorder.visa_product_id = vp.id ")
				.append("and r.id = m.reviewId and r.need_no_review_flag = '0' ")
				.append("and r.batch_no = vsbo.batch_no and vsbo.busyness_type = '2' ")
				.append("and r.process_type = '").append(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY).append("' ");
		/* 拼装查询条件 */
		Object groupCode = params.get("groupCode");
		if(groupCode != null && !"".equals(groupCode.toString().trim())){
			querySql.append(" and (r.batch_no = '" + groupCode.toString().trim() + "'")
					.append(" or (select v.groupCode from visa_products v where v.id = r.product_id) like '%" + groupCode.toString().trim() +"%' ")
					.append(" or r.order_no like '%" + groupCode.toString().trim() + "%') ");
		}
		// 游客名称
		Object travellerName = params.get("travellerName");
		if (travellerName != null && !"".equals(travellerName.toString().trim())) {
			querySql.append(" and r.traveller_name like '%" + travellerName.toString().trim()+"%' ");
		}
		// 下单人
		Object createBy = params.get("createBy");
		if (createBy != null && !"".equals(createBy.toString())) {
			querySql.append(" and r.order_creator = " + createBy.toString() + " ");
		}
		// 申请日期
		Object applyDateFrom = params.get("applyDateFrom");
		if (applyDateFrom != null && !"".equals(applyDateFrom.toString())) {
			querySql.append(" and r.create_date >= '" + applyDateFrom.toString() + " 00:00:00' ");
		}
		Object applyDateTo = params.get("applyDateTo");
		if (applyDateTo != null && !"".equals(applyDateTo.toString())) {
			querySql.append(" and r.create_date <= '" + applyDateTo.toString() + " 23:59:59' ");
		}
		// 审批发起人
		Object applyPerson = params.get("applyPerson");
		if (applyPerson != null && !"".equals(applyPerson.toString())) {
			querySql.append(" and r.create_by = " + applyPerson.toString() + " ");
		}
		// 签证国家
		Object sysCountryId = params.get("sysCountryId");
		if (applyPerson != null && !"".equals(sysCountryId.toString())) {
			querySql.append(" and vp.sysCountryId = " + sysCountryId.toString() + " ");
		}
		// 签证类型
		Object visaType = params.get("visaType");
		if (visaType != null && !"".equals(visaType.toString())) {
			querySql.append(" and vp.visaType = " + visaType.toString() + " ");
		}
		/* 审批状态 空 为全部 1 审批中 2 已通过 0 未通过 */
		Object reviewStatus = params.get("reviewStatus");
		if (reviewStatus != null && !"".equals(reviewStatus.toString())
				&& !"-1".equals(reviewStatus.toString())
				&& NumberUtils.isNumber(reviewStatus.toString())) {
			querySql.append(" and r.status = " + Integer.parseInt(reviewStatus.toString()) + " ");
		}
		// 出纳确认
		Object cashConfirm = params.get("cashConfirm");
		if (cashConfirm != null && !"".equals(cashConfirm.toString())
				&& !"-1".equals(cashConfirm.toString())
				&& NumberUtils.isNumber(cashConfirm.toString())) {
			querySql.append(" and r.pay_status = " + Integer.parseInt(cashConfirm.toString()) + " ");
		}
		// 打印状态
		Object printStatus = params.get("printStatus");
		if (printStatus != null && !"".equals(printStatus.toString())
				&& !"-1".equals(printStatus.toString())
				&& NumberUtils.isNumber(printStatus.toString())) {
			querySql.append(" and r.print_status = " + Integer.parseInt(printStatus.toString()) + " ");
		}
		
		//产品
		Set<String> prds = userReviewPermissionResultForm.getProductTypeId();
		String prdStr = StringUtils.join(prds.toArray(), ",");
		
		//部门
		Set<String> deptIds = userReviewPermissionResultForm.getDeptId();
		String deptIdStr = StringUtils.join(deptIds.toArray(), ",");

		/* 状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批 */
		Object tabStatus = params.get("tabStatus");
		if (tabStatus != null && !"".equals(tabStatus.toString()) && NumberUtils.isNumber(tabStatus.toString())
				&& !Context.REVIEW_TAB_ALL.equals(tabStatus.toString())) {
			int tabStatusInt = Integer.parseInt(tabStatus.toString());
			if (Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == tabStatusInt) {
				// 待本人审批
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt) {
				// 本人审批通过
				querySql.append(" and r.id in (select review_id from review_log_new ")
						.append(" where operation = 1 or operation = 2 and active_flag = 1 and create_by = '" + userId + "') ");
			} else if (Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt) {
				// 非本人审批
				if((prds != null && prds.size() > 0) && (deptIds != null && deptIds.size() > 0)){
					querySql.append(" and product_type in ('"+ prdStr +"') and dept_id in ('"+ deptIdStr +"') ");
				}
				querySql.append(" and FIND_IN_SET('" + userId + "', r.skipped_assignee)");
			} else if (Integer.parseInt("0") == tabStatusInt) {
				// 全部
				querySql.append(" and FIND_IN_SET('" + userId + "', r.skipped_assignee)");
				if((prds != null && prds.size() > 0) && (deptIds != null && deptIds.size() > 0)){
					querySql.append(" and FIND_IN_SET('" + userId + "', r.all_reviewer) or (product_type in ('"+ prdStr +"') ")
							.append(" and dept_id in ('"+ deptIdStr +"') and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
				}else{
					querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
				}
			}
		}
		//bug15015环球行账号审批模块中出现拉美途的数据
		querySql.append(" and r.company_id = '" + companyUuid + "'");
		// 排序 默认按重要程度降序 按创建时间降序
		querySql.append(" GROUP BY r.batch_no order by r.critical_level desc ");
		Object orderCreateDateSort = params.get("orderCreateDateSort");
		Object orderUpdateDateSort = params.get("orderUpdateDateSort");
		if (orderCreateDateSort != null && !"".equals(orderCreateDateSort.toString())) {
			querySql.append(" ,r.create_date " + orderCreateDateSort.toString() + " ");
		} else if (orderUpdateDateSort != null && !"".equals(orderUpdateDateSort.toString())) {
			querySql.append(" ,r.update_date " + orderUpdateDateSort.toString() + " ");
		} else {
			querySql.append(" ,r.create_date desc ");
		}

		// 执行SQL查询出列表数据
		@SuppressWarnings("unchecked")
		Page<Map<String, Object>> page = visaBorrowMoneyReviewDao
			.findBySql((Page<Map<String, Object>>) params.get("pageP"), querySql.toString(), Map.class);
		// 为列表数据组装审核变量
		List<Map<String, Object>> list = page.getList();
		Object reviewid = null;
		Object status = null;
		for (Map<String, Object> map : list) {
			reviewid = map.get("reviewId");
			if (reviewid == null || "".equals(reviewid.toString())) {
				continue;
			}

			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewid.toString());
			map.putAll(reviewMap);
			map.put("isBackReview", ReviewUtils.isBackReview(reviewid.toString()));
			map.put("isCurReviewer", ReviewUtils.isCurReviewer(reviewMap.get("currentReviewer")));
			status = map.get("status");
			if (status == null || StringUtils.isBlank(status.toString())
					|| !NumberUtils.isDigits(status.toString())) {
				map.put("statusdesc", "无审核状态");
				continue;
			}
			if (ReviewConstant.REVIEW_STATUS_PROCESSING == Integer.parseInt(map.get("status").toString())) {
				Object cReviewer = map.get("currentReviewer");
				String person = "";
				if (cReviewer != null) {
					person = getReviewerDesc(cReviewer);
				}
				map.put("statusdesc", "待" + person + "审核");
			} else {
				map.put("statusdesc", getReviewStatus(Integer.parseInt(map.get("status").toString())));
			}
			reviewid = null;
			status = null;
		}
		page.setList(list);
		return page;
	}

	/**
	 * 通过批次号查询该批次下游客的信息(针对签证借款)
	 *
	 * @param batchNo
	 * @param travelerList
	 * @return List<Map<String, String>>
	 * @author yunpeng.zhang
	 * @DateTime 2015年12月4日17:02:48
	 */
	@Override
    @Transactional(readOnly = true)
	public void getTravelerList(String batchNo, List<Map<String, String>> travelerList) {
		//获取批次下的游客
		StringBuffer buffer = new StringBuffer();
		buffer.append("select r.id reviewId, r.batch_no batchNo, vo.create_by createBy, r.order_no orderNo, ")
		      .append(" r.group_code groupCode, vp.groupCode vpGroupCode, vo.group_code as voGroupCode, ")//对应需求号  C463V3   签证订单团号统一取  签证订单关联产品的团号(环球行除外) 
		      .append(" m.amount amount ,m.currencyId currencyId ,r.order_id orderId, ")
		      .append(" r.traveller_name travellerName, vp.visaType visaType, vp.sysCountryId sysCountryId, r.remark remark, r.traveller_id travellerId ")
		      .append("from review_new r , visa_products vp, visa_flow_batch_opration vfbo, review_process_money_amount m, visa_order vo ")
		      .append("where r.product_id = vp.id and r.batch_no = vfbo.batch_no and vfbo.busyness_type = '2' ")
		      .append("and vo.id = r.order_id and r.id = m.reviewId and r.process_type='5' and r.batch_no = ")
		      .append("'"+batchNo+"'");
		List<Map<String, Object>> list = visaBorrowMoneyReviewDao.findBySql(buffer.toString(), Map.class);
		Integer i = 0;
		for(Map<String, Object> map : list) {
			if(null != map.get("batchNo")) {
				Map<String, String> travelerMap = new HashMap<String, String>();
				//审批流信息
				if (map.get("reviewId") != null) {
					travelerMap.put("reviewId", map.get("reviewId").toString());
				}
				//序号
				i++;
				travelerMap.put("num", i.toString());
				//订单编号
				if (map.get("orderNo") != null) {
					travelerMap.put("orderNo", map.get("orderNo").toString());
				}
				//团号
				if (map.get("groupCode") != null) {
					String companeyUUID = UserUtils.getUser().getCompany().getUuid();
					if (Context.SUPPLIER_UUID_HQX.equals(companeyUUID)) {  // 环球行 "7a816f5077a811e5bc1e000c29cf2586"
						travelerMap.put("groupCode", map.get("voGroupCode").toString());
					}else{
						travelerMap.put("groupCode", map.get("vpGroupCode").toString());
					}
				}
				//订单id
				if (map.get("orderId") != null){
					travelerMap.put("orderId", map.get("orderId").toString());
				}
				//游客id
				if(map.get("travellerId") != null){
					travelerMap.put("tid", map.get("travellerId").toString());
				}
				//下单人
				if (map.get("createBy") != null) {
                    String createByName = "";
                    if (StringUtils.isNotBlank(map.get("createBy").toString())) {
                        createByName = UserUtils.getUserNameById(Long.parseLong(map.get("createBy").toString()));
                    }
                    travelerMap.put("createBy", createByName);
				}
				//游客
				if (map.get("travellerName") != null) {
					travelerMap.put("travellerName", map.get("travellerName").toString());
				}
				//签证类型
				if (map.get("visaType") != null) {
					travelerMap.put("visaType", DictUtils.getDictLabel(map.get("visaType").toString(), "new_visa_type", ""));
				}
				//签证国家
				if (map.get("sysCountryId") != null) {
					travelerMap.put("visaCountry", CountryUtils.getCountryName(Long.parseLong(map.get("sysCountryId").toString())));
				}
				//借款原因
				if (map.get("remark") != null) {
					travelerMap.put("remark", map.get("remark").toString());
				}
				//币种
				travelerMap.put("borrowCurrency", "¥");
                // 借款金额
				Map<String, Object> reviewDetailMap = Maps.newHashMap();
				if (StringUtils.isNotBlank(map.get("reviewId").toString())) {
					reviewDetailMap = reviewService.getReviewDetailMapByReviewId(map.get("reviewId").toString());
				}
				String borrowAmount = "0.00";
				if (reviewDetailMap.get("borrowAmount") != null) {
					borrowAmount = reviewDetailMap.get("borrowAmount").toString();
				}
				travelerMap.put("borrowAmount", df.format(Double.valueOf(borrowAmount)));
				//借款金额币种
                if(map.get("currencyId") != null) {
                    travelerMap.put("currencyId", map.get("currencyId").toString());
                }
				// 借款金额
				if(map.get("amount") != null) {
					travelerMap.put("amount", map.get("amount").toString());
				}
				travelerList.add(travelerMap);
			}
		}
	}

	/**
	 * 通过批次号查询该批次下游客的信息(针对签证还收据)
	 *
	 * @param batchNo
	 * @param travelerList
	 * @return List<Map<String, String>>
	 * @author yang.jiang
	 * @DateTime 2015-12-5 16:36:47
	 */
	@Override
	public void getTravelerList4Receipt(String batchNo, List<Map<String, String>> travelerList) {
		//获取批次下的游客
		StringBuffer buffer = new StringBuffer();
		buffer.append("select r.id reviewId, r.batch_no batchNo, r.order_creator order_creator, r.order_no orderNo, ")
			  .append(" r.group_code groupCode, vp.groupCode vpGroupCode, vo.group_code as voGroupCode, r.order_id orderId, ")//对应需求  C460V3 签证订单团号统一取点单所关联的产品团号
			  .append(" r.traveller_name travellerName, vp.visaType visaType, vp.sysCountryId sysCountryId, r.remark remark, r.traveller_id travellerId ")
			  .append("from review_new r , visa_products vp, visa_flow_batch_opration vfbo, visa_order vo ")
			  .append("where r.process_type='4' and r.product_id = vp.id and r.batch_no = vfbo.batch_no and vfbo.busyness_type = '1' and vo.id = r.order_id and r.batch_no = ")
			  .append("'"+batchNo+"'");
		List<Map<String, Object>> list = visaBorrowMoneyReviewDao.findBySql(buffer.toString(), Map.class);
		Integer i = 0;
		for(Map<String, Object> map : list) {
			if(null != map.get("batchNo")) {
				Map<String, String> travelerMap = new HashMap<String, String>();
				//审批流信息
				if (map.get("reviewId") != null) {
					travelerMap.put("reviewId", map.get("reviewId").toString());
				}
				//序号
				i++;
				travelerMap.put("num", i.toString());
				//订单编号
				if (map.get("orderNo") != null) {
					travelerMap.put("orderNo", map.get("orderNo").toString());
				}
				//团号
				if (map.get("groupCode") != null) {
					String companeyUUID = UserUtils.getUser().getCompany().getUuid();
					if (Context.SUPPLIER_UUID_HQX.equals(companeyUUID)) {  // 环球行 "7a816f5077a811e5bc1e000c29cf2586"
						travelerMap.put("groupCode", map.get("voGroupCode").toString());
					}else{
						travelerMap.put("groupCode", map.get("vpGroupCode").toString());
					}
					
				}
				//订单id
				if (map.get("orderId") != null){
					travelerMap.put("orderId", map.get("orderId").toString());
				}
				//游客id
				if(map.get("travellerId") != null){
					travelerMap.put("tid", map.get("travellerId").toString());
				}
				//下单人
				if (map.get("order_creator") != null) {
					String createByName = "";
					if (StringUtils.isNotBlank(map.get("order_creator").toString())) {
						createByName = UserUtils.getUserNameById(Long.parseLong(map.get("order_creator").toString()));
					}
					travelerMap.put("order_creator", createByName);
				}
				//游客
				if (map.get("travellerName") != null) {
					travelerMap.put("travellerName", map.get("travellerName").toString());
				}
				//签证类型
				if (map.get("visaType") != null) {
					travelerMap.put("visaType", DictUtils.getDictLabel(map.get("visaType").toString(), "new_visa_type", ""));
				}
				//签证国家
				if (map.get("sysCountryId") != null) {
					travelerMap.put("visaCountry", CountryUtils.getCountryName(Long.parseLong(map.get("sysCountryId").toString())));
				}
				//借款原因
				if (map.get("remark") != null) {
					travelerMap.put("remark", map.get("remark").toString());
				}
				//币种
				travelerMap.put("receiptCurrency", "¥");
				//收据金额
				Map<String, Object> reviewDetailMap = Maps.newHashMap();
				if (StringUtils.isNotBlank(map.get("reviewId").toString())) {
					reviewDetailMap = reviewService.getReviewDetailMapByReviewId(map.get("reviewId").toString());
				}
				String receiptMoney = "0.00";
				if (reviewDetailMap.get("receiptAmount") != null) {
					receiptMoney = reviewDetailMap.get("receiptAmount").toString();
				}
				travelerMap.put("receiptAmount", df.format(Double.valueOf(receiptMoney)));
				//借款金额
				String borrowMoney = "0.00";
				if (reviewDetailMap.get("borrowAmount_return") != null) {
					borrowMoney = reviewDetailMap.get("borrowAmount_return").toString();
				}
				travelerMap.put("borrowAmount_return", df.format(Double.valueOf(borrowMoney)));
				travelerList.add(travelerMap);
			}
		}
	}

	/**
	 * 获取当前审核人描述 由id转化为name
	 *
	 * @param cReviewer
	 * @return
	 */
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

	private String getReviewStatus(Integer status) {
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
	 * 签证借款 游客明细下载
	 * @author Bin
	 * @DateTime 2015年12月5日
	 * @param batchNo
	 * @return void
	 */
	public void exportTravelerInfo(String batchNo, HttpServletRequest request, HttpServletResponse response) {
		// 获取数据
		List<Map<String, String>> travelerList = new ArrayList<Map<String, String>>();
		String busynessType = request.getParameter("busynessType");
		
		// 根据批次号类型判断是哪种批量操作
		// 签证借款
		if ("2".equals(busynessType)) {
			getTravelerList(batchNo, travelerList);
			if (!travelerList.isEmpty()) {
				List<Object[]> travelerInfoList = new ArrayList<Object[]>();
				for (int i = 0; i < travelerList.size(); i++) {
					Object[] o = new Object[9];
					// 序号
					o[0] = i + 1;
					Map<String, String> travelerInfoMap = travelerList.get(i);
					// 订单
					o[1] = travelerInfoMap.get("orderNo");
					// 订单团号
					o[2] = travelerInfoMap.get("groupCode");
					// 下单人
					o[3] = travelerInfoMap.get("createBy");
					// 游客姓名
					o[4] = travelerInfoMap.get("travellerName");
					// 签证类型
					o[5] = travelerInfoMap.get("visaType");
					// 签证国家
					o[6] = travelerInfoMap.get("visaCountry");
					//借款金额
					o[7] = "¥ " + travelerInfoMap.get("borrowAmount");
					// 借款原因
					o[8] = travelerInfoMap.get("remark");
					travelerInfoList.add(o);
				}

				// Excel各行名称
				String[] cellTitle = new String[9];
				cellTitle[0] = "序号";
				cellTitle[1] = "订单号";
				//for C460V5  wxw added
				if ("7a816f5077a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())) {
					cellTitle[2] = "订单团号";
				}else {
					cellTitle[2] = "团号";
				}
				cellTitle[3] = "下单人";
				cellTitle[4] = "游客姓名";
				cellTitle[5] = "签证类型";
				cellTitle[6] = "签证国家";
				cellTitle[7] = "借款金额";
				cellTitle[8] = "借款原因";

				// 文件首行标题
				String firstTitle = batchNo;
				// 文件名
				String fileName = batchNo + "-游客信息";
				try {
					ExportExcel.createExcle(fileName, travelerInfoList, cellTitle, firstTitle, request, response);
				} catch (Exception e) {
					new Exception("下载游客明细时发生错误");
				}
			}
		}

		// 还签证收据审批
		else if ("1".equals(busynessType)) {
			getTravelerList4Receipt(batchNo, travelerList);
			if (!travelerList.isEmpty()) {
				List<Object[]> travelerInfoList = new ArrayList<Object[]>();
				for (int i = 0; i < travelerList.size(); i++) {
					Object[] o = new Object[9];
					// 序号
					o[0] = i + 1;
					Map<String, String> travelerInfoMap = travelerList.get(i);
					// 订单号
					o[1] = travelerInfoMap.get("orderNo");
					// 订单团号
					o[2] = travelerInfoMap.get("groupCode");
					// 下单人
					o[3] = travelerInfoMap.get("order_creator");
					// 游客姓名
					o[4] = travelerInfoMap.get("travellerName");
					// 签证类型
					o[5] = travelerInfoMap.get("visaType");
					// 签证国家
					o[6] = travelerInfoMap.get("visaCountry");
					// 收据金额
					o[7] = "¥ " + travelerInfoMap.get("receiptAmount");
					// 借款金额
					o[8] = "¥ " + travelerInfoMap.get("borrowAmount_return");
					travelerInfoList.add(o);
				}

				// Excel各行名称
				String[] cellTitle = new String[9];
				cellTitle[0] = "序号";
				cellTitle[1] = "订单号";
				//for C460V5  wxw added
				if ("7a816f5077a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())) {
					cellTitle[2] = "订单团号";
				}else {
					cellTitle[2] = "团号";
				}
				cellTitle[3] = "下单人";
				cellTitle[4] = "游客姓名";
				cellTitle[5] = "签证类型";
				cellTitle[6] = "签证国家";
				cellTitle[7] = "收据金额";
				cellTitle[8] = "借款金额";

				// 文件首行标题
				String firstTitle = batchNo;
				// 文件名
				String fileName = batchNo + "-游客信息";
				try {
					ExportExcel.createExcle(fileName, travelerInfoList, cellTitle, firstTitle, request, response);
				} catch (Exception e) {
					new Exception("下载游客明细时发生错误");
				}
			}
		}

	}

	/**
	 * 环球行签证借款审核
	 *
	 * @param result     1 审核 0 驳回
	 * @param batchNo    批次号
	 * @param denyReason 备注或驳回原因
	 * @return
	 * @author yunpeng.zhang
	 * @createDate 2015年12月8日13:54:00
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> reviewVisaBorrowMoneybyBatchNo(String result, String batchNo, String denyReason) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, String>> travelerList = new ArrayList<Map<String, String>>();
		getTravelerList(batchNo, travelerList);
		try {
			for (Map<String, String> travelerMap : travelerList) {
				Map<String, Object> variables = new HashMap<String, Object>();
				ReviewResult reviewResult;
				String companyUuid = UserUtils.getUser().getCompany().getUuid();
				if (Context.REVIEW_ACTION_PASS.equals(result)) {                  //通过
					reviewResult = processReviewService
							.approve(UserUtils.getUser().getId().toString(), companyUuid, null, userReviewPermissionChecker, travelerMap.get("reviewId"),
									 denyReason, variables);
				} else {                                                        //驳回
					reviewResult = processReviewService
							.reject(UserUtils.getUser().getId().toString(), companyUuid, null, travelerMap.get("reviewId"),
									denyReason, variables);
				}
				if (!reviewResult.getSuccess()) {
					if("1".equals(result)) {
						throw new Exception("审批失败！");
					} else if("0".equals(result)) {
						throw new Exception("驳回失败！");
					}
				}
				// 3如果审核通过并且当前层级为最高层级 则保存借款信息，更改批次信息
				if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
					String tid = travelerMap.get("tid");
					Traveler traveler = null;
					if(StringUtils.isNotBlank(tid)) {
						traveler = travelerService.findTravelerById(Long.parseLong(tid));
					}
					String borrowMoneySerialNum = UUID.randomUUID().toString();
					if (traveler != null) {
						if (null == traveler.getJkSerialNum() || "".equals(traveler.getJkSerialNum())) {
							travelerService.updateJkSerialNumByTravelerId(borrowMoneySerialNum, traveler.getId());
							traveler.setJkSerialNum(borrowMoneySerialNum);
						}
						String reviewId = reviewResult.getReviewId();
						
						MoneyAmount borrowMoneyAmount = new MoneyAmount();
						borrowMoneyAmount.setSerialNum(traveler.getJkSerialNum());
						String currencyId = travelerMap.get("currencyId");
						if(StringUtils.isNotBlank(currencyId)) {
							borrowMoneyAmount.setCurrencyId(Integer.parseInt(currencyId));
						}
						String amount = travelerMap.get("amount");
						if(StringUtils.isNotBlank(amount)) {
							borrowMoneyAmount.setAmount(new BigDecimal(amount));
						}
						borrowMoneyAmount.setUid(traveler.getId());
						borrowMoneyAmount.setMoneyType(Context.MONEY_TYPE_JK);
						borrowMoneyAmount.setOrderType(Context.ORDER_TYPE_QZ);
						borrowMoneyAmount.setBusindessType(Context.MONEY_BUSINESSTYPE_TRAVELER);
						borrowMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
						borrowMoneyAmount.setReviewUuid(reviewId);
						// 借款通过后，保存借款金额
						moneyAmountService.saveOrUpdateMoneyAmount(borrowMoneyAmount);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}

		if("1".equals(result)) {
			resultMap.put("msg", "审批成功！");
		} else if("0".equals(result)) {
			resultMap.put("msg", "驳回成功！");
		}
		resultMap.put("result", "success");
		return resultMap;
	}

	/**
	 * 根据批次号对签证借款审核进行撤销
	 *
	 * @param batchNo 批次号
	 * @return
	 * @author yunpeng.zhang
	 * @createDate 2015年12月9日11:25:23
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> backReivew(String batchNo) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		List<Map<String, String>> travelerList = new ArrayList<Map<String, String>>();
		getTravelerList(batchNo, travelerList);
		try {
			if(travelerList == null || travelerList.size() <= 0) {
				throw new Exception("撤销失败!");
			}
			for (Map<String, String> travelerMap : travelerList) {
                String reviewId = travelerMap.get("reviewId");
                ReviewResult reviewResult = processReviewService.back(UserUtils.getUser().getId().toString(), companyUuid, null, reviewId, null, null);
                if(!reviewResult.getSuccess()) {
                    throw new Exception("撤销失败!");
                }
            }
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}

		resultMap.put("msg", "撤销成功！");
		resultMap.put("result", "success");
		return resultMap;

	}

	@Override
	public List<String> findMatchConditionBatchNos(String batchNos) {
		String[] batchNoStrs = batchNos.split(",");
		Long userId = UserUtils.getUser().getId();
		List<String> canbeApprovedBatchNo = processReviewService.findCanbeApprovedBatchNo(userId.toString(), Arrays.asList(batchNoStrs));
		return canbeApprovedBatchNo;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> batchReview(List<String> canBeBatchNos, String result, String remark) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		for (String canBeBatchNo : canBeBatchNos) {
			Map<String, Object> tempResult = reviewVisaBorrowMoneybyBatchNo(result, canBeBatchNo, remark);
			if("fail".equals(tempResult.get("result"))) {
				throw new Exception("审批失败!");
			}
		}
		resultMap.put("msg", "审批成功！");
		resultMap.put("result", "success");
		return resultMap;
	}


}
