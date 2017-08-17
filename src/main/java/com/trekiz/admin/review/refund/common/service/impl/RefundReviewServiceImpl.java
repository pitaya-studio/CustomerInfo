package com.trekiz.admin.review.refund.common.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.refund.common.dao.impl.IRefundReviewDao;
import com.trekiz.admin.review.refund.common.service.IRefundReviewService;

@Service
public class RefundReviewServiceImpl implements IRefundReviewService {

	@Autowired
	private IRefundReviewDao refundReviewDao;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private VisaProductsService visaProductsService;
	
	/**
	 * 查询退款审批列表
	 * @param params
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> queryRefundReviewListNew(
			Map<String, Object> params) {
		//获取当前登录用户的id
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowType(userId, companyUuid, Context.REVIEW_FLOWTYPE_REFUND);
		//部门
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
		//产品
		Set<String> prds = userReviewPermissionResultForm.getProductTypeId();
		String prdStr = "";
		if(prds != null && prds.size() > 0){
			int n = 0;
			for(String str : prds){
				if(n == 0){
					prdStr += str;
					n++;
				} else {
					prdStr += "," + str;
				}
			}
		}
		if("".equals(prdStr)){//给默认值
			prdStr = "-1,-2";
		}
		/*声明查询SQL*/
		StringBuffer querySql = new StringBuffer();
		querySql.append("select r.order_no orderno,r.id reviewid ").//订单编号
			append(",r.order_id orderid ").//订单id
			append(",r.group_code as groupcode ").//团号
//			append(",(select vpr.groupCode from visa_products vpr where vpr.id = r.product_id) as proGroupCode ").// 产品团号
//			append(",(select vo.group_code from visa_order vo where vo.id = r.group_id) as voGroupCode ").// 订单团号
			append(",r.group_id groupid ").//团期id
			append(",r.product_name productname ").//产品名称
			append(",r.product_id productid ").//产品id
			append(",r.product_type producttype ").//产品类型
			append(",r.create_date createdate ").//申请时间
			append(",r.create_by createby ").//审批发起人
			append(",r.agent agent ").//渠道商
			append(",r.operator operator ").//计调
			append(",m.currencyId currencyid ").//金额币种
			append(",m.amount amount ").//退款金额
			append(",r.last_reviewer lastreviewer ").//上一环节审批人
			append(",r.status status ").//审批状态
			append(",r.pay_status paystatus ").//出纳确认
			append(",r.print_status printstatus ").//打印状态
			append(" from review_new r left join review_process_money_amount m on r.id = m.reviewId ").//
			append(" where 1 = 1 and r.company_id = '" + companyUuid + "' and r.need_no_review_flag=0 and r.process_type = '" + Context.REVIEW_FLOWTYPE_REFUND + "' ").
			append(" and ((r.product_type in(" + prdStr + ") and r.dept_id in (" + deptIdStr + ")) or FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");//
		/*拼装查询条件*/
		Object groupCode = params.get("groupCode");
		if(groupCode != null && !"".equals(groupCode.toString())){
			querySql.append(" and (r.group_code like '%" + groupCode.toString()).
				append("%' or r.product_name like '%" + groupCode.toString()).
				append("%' or r.order_no like '%" + groupCode.toString() + "%') ");
		}
		Object productType = params.get("productType");
		if(productType != null && !"".equals(productType.toString()) && !"0".equals(productType.toString())){
			querySql.append(" and r.product_type = " + productType.toString() + " ");
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
		Object operator = params.get("operator");
		if(operator != null && !"".equals(operator.toString())){
			querySql.append(" and r.operator = " + operator.toString() + " ");
		}
		Object refundMoneyFrom = params.get("refundMoneyFrom");
		if(refundMoneyFrom != null && !"".equals(refundMoneyFrom.toString()) && NumberUtils.isNumber(refundMoneyFrom.toString())){
			querySql.append(" and m.amount >= " + Integer.parseInt(refundMoneyFrom.toString()) + " ");
		}
		Object refundMoneyTo = params.get("refundMoneyTo");
		if(refundMoneyTo != null && !"".equals(refundMoneyTo.toString()) && NumberUtils.isNumber(refundMoneyTo.toString())){
			querySql.append(" and m.amount <= " + Integer.parseInt(refundMoneyTo.toString()) + " ");
		}
		/*审批状态  空 为全部 1 审批中 2 已通过 0 未通过*/
		Object reviewStatus = params.get("reviewStatus");
		if(reviewStatus != null && !"".equals(reviewStatus.toString()) && !"-1".equals(reviewStatus.toString()) && NumberUtils.isNumber(reviewStatus.toString())){
			querySql.append(" and r.status = " + Integer.parseInt(reviewStatus.toString()) + " ");
		}
		Object cashConfirm = params.get("cashConfirm");
		if(cashConfirm != null && !"".equals(cashConfirm.toString()) && !"-1".equals(cashConfirm.toString()) && NumberUtils.isNumber(cashConfirm.toString())){
			querySql.append(" and r.pay_status = " + Integer.parseInt(cashConfirm.toString()) + " ");
		}
		Object printStatus = params.get("printStatus");
		if(printStatus != null && !"".equals(printStatus.toString()) && !"-1".equals(printStatus.toString()) && NumberUtils.isNumber(printStatus.toString())){
			querySql.append(" and r.print_status = " + Integer.parseInt(printStatus.toString()) + " ");
		}
		/*状态选择 0 全部 1 待本人审批 2 本人已审批 3 非本人审批*/
		Object tabStatus = params.get("tabStatus");
		if(tabStatus != null && !"".equals(tabStatus.toString()) && NumberUtils.isNumber(tabStatus.toString()) && !Context.REVIEW_TAB_ALL.equals(tabStatus.toString())){
			int tabStatusInt = Integer.parseInt(tabStatus.toString());
			if( Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == tabStatusInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt){
//				querySql.append(" and FIND_IN_SET ('" + userId + "', (select l.create_by from review_log_new l where l.review_id = r.id and operation = 1 and l.active_flag = 1)) ");
				querySql.append(" and r.id in (select review_id from review_log_new  where operation in (1,2) and active_flag = 1 and create_by = '" + userId + "') ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt){
				querySql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");
			}
		}
		
		Object paymentType = params.get("paymentType");
		if(paymentType != null && !"".equals(paymentType.toString())){
			querySql.append(" and 	r.agent in (select id from agentinfo where paymentType = "+paymentType+")");
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
		Page<Map<String, Object>> page = refundReviewDao.findBySql((Page<Map<String, Object>>)params.get("pageP"), querySql.toString(), Map.class);
//		为列表数据组装审批变量
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
			if(status == null || StringUtils.isBlank(status.toString()) || !NumberUtils.isDigits(status.toString())){
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
			//对应需求号   签证订单团号  统一取订单所关联产品团号（环球行除外）
			if (Context.PRODUCT_TYPE_QIAN_ZHENG.equals(map.get("producttype"))) {
				if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
					if (map.get("orderid") != null && StringUtils.isNotBlank(map.get("orderid").toString())) {						
						VisaOrder visaOrder =  visaOrderService.findVisaOrder(Long.parseLong(map.get("orderid").toString()));
						if (visaOrder != null) {					
							map.put("groupcode", visaOrder.getGroupCode());
						}
					} else {
						System.out.println(map.get("id"));
					}
				} else {
					if (map.get("productid") != null && StringUtils.isNotBlank(map.get("productid").toString())) {						
						VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(Long.parseLong(map.get("productid").toString()));
						if (visaProducts != null) {								
							map.put("groupcode",visaProducts.getGroupCode());
						}
					}
				}
			}
			
			reviewid = null;
			status = null;
		}
		page.setList(list);
		return page;
	}
	
	/**
	 * 获取当前审批人描述 由id转化为name
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

	@Override
	public Map<String, Object> queryAirticketorderDeatail(String prdOrderId,
			String prdType) {
		if(StringUtils.isBlank(prdOrderId) || StringUtils.isBlank(prdType)){
			return null;
		}
		StringBuffer airticketSql = new StringBuffer();
		// 查询机票订单详情
		if("7".equals(prdType.trim())){
			airticketSql.append("SELECT a.id as orderid, a.create_by as ordermaker, a.salerId as ordercreate,").
				append("a.create_date as orderdate, b.createBy as updateby, b.airType as type, a.order_no as orderno,a.product_type_id as prdtype,").
				append("a.group_code as groupno, b.settlementAdultPrice,a.total_money as totalmoney,a.order_state as orderstatus, b.departureCity,b.arrivedCity,b.reservationsNum,").
				append("b.settlementcChildPrice, b.settlementSpecialPrice,b.currency_id currid,").
				append("b.taxamt, a.adult_num, a.child_num, ").
				append("a.special_num FROM airticket_order a,").
				append("activity_airticket b ").
				append("WHERE a.airticket_id = b.id ").
				append(" and a.id = " + Long.parseLong(prdOrderId));
		}else{//机票切位 目前已不使用
			airticketSql.append("SELECT a.id as orderid, a.saleId as ordercreate,").
					append("a.createDate as orderdate, b.createBy as updateby, b.airType as type, a.orderNum as orderno,8 as prdtype,").
					append("a.groupCode as groupno, b.settlementAdultPrice,a.orderMoney as totalmoney,a.orderStatus as orderstatus, b.departureCity,b.arrivedCity,b.reservationsNum,").
					append("b.settlementcChildPrice, b.settlementSpecialPrice,").
					append("b.taxamt FROM activityreserveorder a,").
					append("activity_airticket b ").
					append("WHERE a.srcActivityId = b.id ").
					append(" and a.id = " + Long.parseLong(prdOrderId));
		}
		
		// 查询航班信息
		StringBuffer flightInfoSql = new StringBuffer("SELECT b.airType, b.departureCity, ");
				flightInfoSql.append("b.arrivedCity,  ").
				append("c.leaveAirport, c.destinationAirpost, ").
				append("c.startTime, c.arrivalTime, c.airlines, c.ticket_area_type,").//
				append("c.spaceGrade, c.airspace, c.number FROM ").
				append("airticket_order a, activity_airticket b, ").
				append("activity_flight_info c WHERE a.airticket_id = b.id ").
				append("AND a.airticket_id = c.airticketId AND a.id = " + Long.parseLong(prdOrderId)).
				append(" order by c.number");
		
		
		List<Map<String, Object>> airticketOrderDetail = refundReviewDao.findBySql(airticketSql.toString(),
				Map.class);
		
		List<Map<String, Object>> airticketFlightDetail = refundReviewDao.findBySql(flightInfoSql.toString(),
				Map.class);
		if (airticketOrderDetail == null || airticketOrderDetail.size() != 1) {
			return null;
		}
		Map<String, Object> orderDetail = airticketOrderDetail.get(0);
		orderDetail.put("flightInfoList", airticketFlightDetail);
		return orderDetail;
	}

	/**
	 * 查询签证订单详情
	 */
	@Override
	public Map<String, Object> queryVisaorderDeatail(String prdOrderId) {
		if(StringUtils.isBlank(prdOrderId)){
			return null;
		}
		StringBuffer visaSql = new StringBuffer("SELECT a.id as orderid,a.create_by as ordermaker,a.salerId as ordercreate,a.create_date as orderdate,a.product_type_id as prdtype,");
		visaSql.append("a.order_no as orderno,a.group_code as groupno,a.total_money as totalmoney,a.visa_order_status as orderstatus, ").
			append("b.createBy as updateby, b.id as visaproductid,b.productCode as prdcode,b.productName as prdname,b.sysCountryId as countryid, ").
			append("b.visaType as visatype,b.collarZoning as collarea,b.currencyId as visaCurrency, b.visaPay as visapay,a.travel_num as tnum ").
			append(" FROM visa_order a,visa_products b ").
			append(" WHERE a.visa_product_id = b.id and a.id=" + prdOrderId);
		List<Map<String, Object>> visaOrderDetail = refundReviewDao.findBySql(visaSql.toString(),
				Map.class);
		if (visaOrderDetail == null || visaOrderDetail.size() != 1) {
			return null;
		}
		return visaOrderDetail.get(0);
	}

	@Override
	public Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId) {
		if(StringUtils.isBlank(prdOrderId)){
			return null;
		}
		//组织散拼查询SQL
		StringBuffer sanPinSql = new StringBuffer("SELECT ");
		sanPinSql.append("a.id AS orderid, ").
			append("b.id AS cpid, ").
			append("a.createBy AS ordermaker, ").
			append("a.salerId AS ordercreate, ").
			append("a.createDate AS orderdate, ").
			append("b.createBy AS updateby, ").
			append("a.orderNum AS orderno, ").
			append("a.orderstatus AS prdtype, ").
			append("c.groupCode AS groupno, ").
			append("b.acitivityName as prdname, ")./*产品名称*/
			append("a.total_money AS totalmoney, ").
			append("a.payStatus AS orderstatus, ").
			append("c.groupCode AS grpcode, ").
			append("c.groupOpenDate AS opendate, ")./*出团日期*/
//					+ "b.targetArea as targetarea, "/*目的地*/
			append( "b.fromArea as fromarea, ")./*出发城市*/
			append("b.activityDuration as tradays, ")./*行程天数*/
			append("b.createDate as tracreatedate, ")./*创建时间*/
			append("c.planPosition as pposition, ")./*预收人数*/
			append("c.freePosition as fposition, ")./*余位*/
//					+ "a.orderPersonnum as pnum, "/*预报名数*/
			append("a.orderPersonNumAdult as nadult, ")./*成人数量*/
			append("a.orderPersonNumChild as nchild, ")./*儿童数量*/
			append("a.orderPersonNumSpecial as nspecial, ")./*特殊人数量*/
			append("a.settlementAdultPrice, ").
			append("a.settlementcChildPrice, ").
			append("a.settlementSpecialPrice ").
			append("FROM ").
			append("productorder a, ")./*团期订单表*/
			append("travelactivity b, ")./*团期产品表*/
			append("activitygroup c ")./*团期表*/
			append("WHERE ").
			append("a.productId = b.id ").
			append("AND a.productGroupId = c.id and a.id = " + Long.parseLong(prdOrderId));
			//查询SQL
			List<Map<String, Object>> sanPinOrderDetail = refundReviewDao.findBySql(sanPinSql.toString(),
					Map.class);
			if (sanPinOrderDetail == null || sanPinOrderDetail.size() != 1) {
				return null;
			}
			Map<String, Object> groupDetail = sanPinOrderDetail.get(0);
			if(groupDetail == null){
				return null;
			}
			String targetAreasSql = "select id,srcActivityId,targetAreaId from activitytargetarea where srcActivityId = " + groupDetail.get("cpid").toString();
			List<Map<String, Object>> targetAreas = refundReviewDao.findBySql(targetAreasSql, Map.class);
			groupDetail.put("targetAreas", targetAreas);
			//返回数据
			return groupDetail;
	}

	@Override
	public Map<String, Object> queryGrouporderDeatail(String prdOrderId) {

		String groupSql = "SELECT "
			+ "a.id as orderid, "/*订单id*/
			+ "b.id as cpid, "/*订单id*/	
			+ "a.createBy AS ordermaker, "/*下单人*/
			+ "a.salerId AS ordercreate, "/*销售*/
			+ "a.createDate AS orderdate, "/*下单时间*/
			+ "b.createBy AS updateby, "/*操作人*/
			+ "a.orderNum AS orderno, "/*订单编号*/
			+ "a.orderstatus AS prdtype, "/*产品类型*/
			+ "c.groupCode AS groupno, "/*团号*/
			+ "a.total_money AS totalmoney, "/*订单总额*/
			+ "a.payStatus AS orderstatus, "/*订单状态*/
			+ "b.acitivityName as prdname, "/*产品名称*/
			+ "b.groupOpenDate AS opendate, "/*出团日期*/
//			+ "b.targetArea as targetarea, "/*目的地*/
			+ "b.fromArea as fromarea, "/*出发城市*/
			+ "b.activityDuration as tradays, "/*行程天数*/
			+ "d.outArea as outarea, "/*离境城市*/
			+ "d.intermodalType as modaltype, "/*联运类型*/
			+ "a.orderPersonNumAdult as nadult, "/*成人数量*/
			+ "a.orderPersonNumChild as nchild, "/*儿童数量*/
			+ "a.orderPersonNumSpecial as nspecial, "/*特殊人数量*/
			+ "a.roomNumber as roomNumber, "/*房间数*/
			+ "a.settlementAdultPrice, " 
			+ "a.settlementcChildPrice, "
			+ "a.settlementSpecialPrice " 
		+ "FROM "
			+ "productorder a, "/*团期订单表*/
			+ "activitygroup c, "/*团期表*/
			+ "travelactivity b LEFT JOIN  "/*团期产品表*/
			+ "activity_airticket d on b.airticket_id = d.id "/*机票产品表*/
		+ "where a.productId = b.id "
		 + "and a.productGroupId = c.id "
		 + "and a.id = " + prdOrderId;
		List<Map<String, Object>> groupOrderDetail = refundReviewDao.findBySql(groupSql,
				Map.class);
		if (groupOrderDetail == null || groupOrderDetail.size() != 1) {
			return null;
		}
		Map<String, Object> groupDetail = groupOrderDetail.get(0);
		String targetAreasSql = "select * from activitytargetarea where srcActivityId = " + groupDetail.get("cpid").toString();
		List<Map<String, Object>> targetAreas = refundReviewDao.findBySql(targetAreasSql, Map.class);
		groupDetail.put("targetAreas", targetAreas);
		return groupDetail;
	}
	
	
}
