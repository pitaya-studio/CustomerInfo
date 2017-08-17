package com.trekiz.admin.review.borrowing.airticket.service;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.order.entity.OrderExitGroupReviewVO;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.PrintFormBean;
import com.trekiz.admin.modules.order.formBean.PrintParamBean;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.service.TransferMoneyService;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.borrowing.airticket.formbean.NewBorrowingBean;
import com.trekiz.admin.review.borrowing.visahqxborrowmoney.repository.IVisaBorrowMoneyReviewDao;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.refund.common.dao.impl.IRefundReviewDao;

import freemarker.template.TemplateException;


/**
 * 
 * @author ruyi.chen 
 * update date 2014-11-19
 * describe   订单，补充  OrderCommonService
 *
 */
@Service
@Transactional(readOnly = true)
public class NewOrderReviewService extends BaseService{

	@SuppressWarnings("unused")
	private static final Integer REVIEW_UNAUDITED = ReviewConstant.REVIEW_STATUS_PROCESSING;//审批中状态
	private static final Integer OPERATE_SUCCESS = ReviewConstant.REVIEW_STATUS_PASSED;//操作完成 
	@Autowired
	private ProductOrderCommonDao productorderDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	TransferMoneyService transferMoneyService;
	@Autowired
	OrderPayService orderPayService;
	@Autowired
	CurrencyService currencyService;
	@Autowired
	private NewAirTicketOrderLendMoneyService newAirTicketOrderLendMoneyService;
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	@Autowired
	private IRefundReviewDao refundReviewDao;
	@Autowired
	private IVisaBorrowMoneyReviewDao visaBorrowMoneyReviewDao;
	@Autowired
	private NewProcessMoneyAmountService newMoneyAmountService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private IAirTicketOrderService ariticketOrderService;
	@Autowired
	private OrderCommonService orderService;

	/**
	 * 新审批信息列表
	 * @author songyang 2015年11月2日16:20:07
	 * @param page
	 * @param vo
	 * @param reviewType
	 * @param orderType
	 * @return
	 */
	public Page<Map<Object, Object>> getPlaneCommonReviewList(Page<Map<Object, Object>>page, 
			OrderExitGroupReviewVO vo, Integer reviewType,Integer orderType){
		Page<Map<Object, Object>> pageInfo;
		List<Object> ls=new ArrayList<Object>();
		//督察
		//获取当前登录用户的id
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService
				.findPermissionByUserIdAndCompanyUuidAndFlowType(userId, companyUuid, Context.REVIEW_FLOWTYPE_BORROWMONEY);
		//部门
		Set<String> deptIds = userReviewPermissionResultForm.getDeptId();
		String deptIdStr = StringUtils.join(deptIds.toArray(), ",");
		if(StringUtils.isBlank(deptIdStr)){//给默认值
			deptIdStr = "-1,-2";
		}
		
		//产品
		Set<String> prds = userReviewPermissionResultForm.getProductTypeId();
		String prdStr = StringUtils.join(prds.toArray(), ",");
		if(StringUtils.isBlank(prdStr)){//给默认值
			prdStr = "-1,-2";
		}
		
		//督察
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT DISTINCT r.* ")
				.append(" FROM review_new r  ")
				.append(" RIGHT JOIN review_process_money_amount m on r.id = m.reviewId  ")
				.append(" WHERE 1 = 1  and r.need_no_review_flag=0 and r.process_type = '" + Context.REVIEW_FLOWTYPE_BORROWMONEY + "' ")
				.append(" AND ((r.product_type in(" + prdStr + ") and r.dept_id in(" + deptIdStr + ")) ")
				.append(" OR FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");
		/*拼装查询条件*/
		if(StringUtils.isNotBlank(vo.getSearchGOP())){
			String searchGOP = vo.getSearchGOP().toString().trim();
			querySql.append(" and (r.group_code like '%" + searchGOP)
					.append("%' or r.product_name like '%" + searchGOP)
					.append("%' or r.order_no like '%" + searchGOP + "%') ");
		}
		if(StringUtils.isNotBlank(vo.getProductType())&&!"0".equals(vo.getProductType())){
			querySql.append(" and r.product_type = " + vo.getProductType().toString() + " ");
		}
		// 添加页面条件查询控制(销售、计调、日期等)
		if(StringUtils.isNotBlank(vo.getOrderNum())){
			querySql.append(" and r.order_no like '%"+vo.getOrderNum()+"%' ");
		}
		if(StringUtils.isNotBlank(vo.getChannel())){
			querySql.append(" and r.agent = " + vo.getChannel().toString() + " ");
		}
		if(StringUtils.isNotBlank(vo.getGroupCode())){
			querySql.append(" and (r.group_code like '%" + vo.getGroupCode().toString().trim())
					.append("%' or r.product_name like '%" + vo.getGroupCode().toString().trim())
					.append("%' or r.order_no like '%" + vo.getGroupCode().toString().trim() + "%') ");
		}
		if(StringUtils.isNotBlank(vo.getApplyDateFrom())){
			querySql.append(" and r.create_date >= '" + vo.getApplyDateFrom().toString() + " 00:00:00' ");
		}
		if(StringUtils.isNotBlank(vo.getApplyDateTo())){
			querySql.append(" and r.create_date <= '" + vo.getApplyDateTo().toString() + " 23:59:59' ");
		}
		if(StringUtils.isNotBlank(vo.getApplyPerson())){
			querySql.append(" and r.create_by = " + vo.getApplyPerson() + " ");
		}
		if(StringUtils.isNotBlank(vo.getPicker())){
			querySql.append(" and r.create_by = " + vo.getMeter().toString() + " ");
		}
		// 出纳确认
		if(StringUtils.isNotEmpty(vo.getCashConfirm()) && (!"-1".equals(vo.getCashConfirm()))) {
			querySql.append(" and r.pay_status = " + vo.getCashConfirm() + " ");
		}
		//销售
		if(StringUtils.isNotBlank(vo.getSaler())){
			querySql.append(" and r.saler = " + vo.getSaler()+" ");
		}

		if(vo.getLendMoneyFrom() != null && !"".equals(vo.getLendMoneyFrom().toString()) && NumberUtils.isNumber(vo.getLendMoneyFrom().toString())){
			querySql.append(" and m.amount >= " + Integer.parseInt(vo.getLendMoneyFrom().toString()) + " ");
		}

		if(vo.getLendMoneyTo() != null && !"".equals(vo.getLendMoneyTo() .toString()) && NumberUtils.isNumber(vo.getLendMoneyTo() .toString())){
			querySql.append(" and m.amount <= " + Integer.parseInt(vo.getLendMoneyTo() .toString()) + " ");
		}

		/*审批状态  空 为全部 1 审批中 2 已通过 0 未通过*/
		if(StringUtils.isNotBlank(vo.getSelectReviewStatus())){
			querySql.append(" and r.status = " + vo.getSelectReviewStatus()+ " ");
		}

		if(vo.getPayStatus() != null && !"".equals(vo.getPayStatus().toString()) && NumberUtils.isNumber(vo.getPayStatus().toString())){
			querySql.append(" and r.pay_status = " + Integer.parseInt(vo.getPayStatus().toString()) + " ");
		}
		if(vo.getPrintFlag() != null && !"".equals(vo.getPrintFlag().toString()) && NumberUtils.isNumber(vo.getPrintFlag().toString())){
			querySql.append(" and r.print_status = " + Integer.parseInt(vo.getPrintFlag().toString()) + " ");
		}
		//		/*状态选择 0 全部 1 待本人审批 2 本人已审批 3 非本人审批*/
		int tabStatusInt = vo.getReviewStatus();
		if( Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == tabStatusInt){
			querySql.append(" and FIND_IN_SET ('" + UserUtils.getUser().getId() + "', r.current_reviewer) ");
		} else if( Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == tabStatusInt){
			querySql.append(" and r.id in (select review_id from review_log_new  where (operation = 1 or operation=2) and active_flag = 1 and create_by = '" +  UserUtils.getUser().getId() + "') " );
		} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt){
			querySql.append(" and not FIND_IN_SET ('" + UserUtils.getUser().getId() + "', r.all_reviewer) ");
		}
		
		if(StringUtils.isNotBlank(vo.getPaymentType())){
			querySql.append(" and r.agent in(select id from agentinfo where paymentType = "+vo.getPaymentType()+") ");
		}
		StringBuffer orderBySql = new StringBuffer();
		// 排序 默认按重要程度降序 按创建时间降序
		orderBySql.append(" r.critical_level desc ");

		//排序包含create_date和update_date
		if(StringUtils.isNotEmpty(vo.getOrderBy())) {
			orderBySql.append(", r." + vo.getOrderBy());
			if(StringUtils.isNotEmpty(vo.getAscOrDesc())) {
				orderBySql.append(" " + vo.getAscOrDesc()+" ");
			} else {
				orderBySql.append(" asc ");
			}
		}
		//排序封装
		page.setOrderBy(orderBySql.toString());
		pageInfo= productorderDao.findPageBySql(page,querySql.toString(), Map.class,ls.toArray());
		return pageInfo;
	}

	/**
	 * 获取机票借款审批信息列表
	 * create by songyang 2015年11月4日13:47:46
	 * @return
	 */
	public Page<Map<Object, Object>> getPlaneBorrowingReviewList(Page<Map<Object, Object>>page,OrderExitGroupReviewVO vo){
		// TODO 添加借款审批业务数据
		Page<Map<Object,Object>> pageInfo = getPlaneCommonReviewList(page, vo, Context.REVIEW_FLOWTYPE_BORROWMONEY,Context.PRODUCT_TYPE_AIRTICKET);
		if (null != pageInfo && pageInfo.getList().size() > 0) {
			
			List<Map<Object,Object>> list =pageInfo.getList();
			for (Map<Object,Object> map:list) {
				//				String rid = map.get("id").toString();
				String rid = map.get("id").toString();
				if(StringUtils.isNotBlank(rid)){
					Map<String, Object> mapPrecess =  processReviewService.getReviewDetailMapByReviewId(rid);
					NewBorrowingBean borr = new NewBorrowingBean(mapPrecess);
					if(StringUtils.isNotEmpty(borr.getTravelerId()) && borr.getTravelerId().contains(NewBorrowingBean.REGEX)){
						borr.setTravelerName("团队");
					}
					if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(NewBorrowingBean.REGEX)){
						String compPrice = "";
						if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
							String[] cMarks = borr.getCurrencyMarks().split(NewBorrowingBean.REGEX);	
							String[] cPrices = borr.getBorrowPrices().split(NewBorrowingBean.REGEX);
							for(int i=0;i<cMarks.length;i++){
								compPrice+="<i>"+cMarks[i]+cPrices[i]+"</i>+";
							}
							borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
						}

					}else{
						borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
					}
					
					
					String strlendName = "";
					//签证产品做特殊处理
					if("6".equals(mapPrecess.get("productType").toString())){ 
						//groupborrownames=其他01#@!#!@#其他02#@!#!@#
						if (null!=mapPrecess.get("groupborrownames")) {
							strlendName = mapPrecess.get("groupborrownames").toString().replaceAll("#@!#!@#", " ");
						}
					}else {
						if (mapPrecess.get("lendName") != null) {
							strlendName = mapPrecess.get("lendName").toString().replaceAll("#_", " ");
						}
					}
					map.put("lendName", strlendName);
					
//					if(!("7").equals(mapPrecess.get("productType"))){
//						if(mapPrecess.get("groupId")!=null){
//							map.put("groupCode", groupService.findById(Long.parseLong(mapPrecess.get("groupId").toString())).getGroupCode());
//						}
//					}else{
//						map.put("groupCode", mapPrecess.get("groupCode"));
//					}
					
					map.put("groupCode", mapPrecess.get("groupCode"));
					
					map.put("status", mapPrecess.get("status"));
					//获取渠道名称
					map.put("agentName", mapPrecess.get("agent"));
					//获取非签约渠道名称
					map.put("nagentName", mapPrecess.get("agentName"));
					
					map.put("boproductType",mapPrecess.get("productType"));
					
					//签证产品做特殊处理   
					if("6".equals(mapPrecess.get("productType").toString())){ 
						if (null!=mapPrecess.get("totalborrowamount")) {
							map.put("payPrice", mapPrecess.get("totalborrowamount").toString());
						}
					}else {
						map.put("payPrice", borr.getCurrencyIds());
					}
					
					//map.put("payPrice", borr.getCurrencyIds());
					map.put("currentReviewer", borr.getCurrentReviewer());
					map.put("reviewId",  mapPrecess.get("id"));
					//判断能否撤销
					map.put("isBackReview", ReviewUtils.isBackReview(rid));
					//判断当前登陆人是否有审批权限
					map.put("isCurReviewer", ReviewUtils.isCurReviewer(mapPrecess.get("currentReviewer")));
					//map.put("travelerName", borr.getTravelerName());
					
					//借款审批借款金额增加千分位格式S---- modify by wangyang 2016.8.17
					String payPrice = (String) map.get("payPrice");
					if (payPrice != null && !"".equals(payPrice)) {
						if (payPrice.indexOf("+") == -1) { //单币种情况
							
							String mark = MoneyNumberFormat.resolveMoney(payPrice)[0];
							String number = MoneyNumberFormat.resolveMoney(payPrice)[1];
							map.put("payPrice",mark + MoneyNumberFormat.getThousandsByRegex(number, 2));
					
						} else { //多币种情况

							String money = "";
							String[] prices = payPrice.split("\\+");
							for (int i = 0; i < prices.length; i++) {
								String price = prices[i].replace("<i>", "").replace("</i>", "");
								
								String mark = MoneyNumberFormat.resolveMoney(price)[0];
								String number = MoneyNumberFormat.resolveMoney(price)[1];
								money += "<i>" + mark + MoneyNumberFormat.getThousandsByRegex(number, 2) + "</i>+";
							}
							//去掉末尾的“+”
							if (money != null && !"".equals(money)) {
								money = money.substring(0, money.length() - 1);
								map.put("payPrice", money);
							}
						}
					}
					//借款审批借款金额增加千分位格式E---- modify by wangyang 2016.8.17
				}
			}
		}
		return pageInfo;
	}

	/**
	 * 查询转款审批列表
	 * @author yang.jiang 2015年11月26日 14:01:01
	 * @param params
	 * @return
	 */
	public Page<Map<String, Object>> getTransferMoneyReviewList(Map<String, Object> params) {
		//获取当前登录用户的id
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		List<Integer> typeList = new ArrayList<Integer>();
		typeList.add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY);  //转款
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowTypeList(userId, companyUuid, typeList);
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
		append(",r.group_code groupcode ").//团号
		append(",r.group_id groupid ").//团期id
		append(",r.product_name acitivityName ").//产品名称
		append(",r.product_id productId ").//产品id
		append(",r.product_type producttype ").//产品类型
		append(",r.create_date createdate ").//申请时间
		append(",r.create_by createby ").//审批发起人
		append(",r.agent agent ").//渠道商
		append(",r.saler saler ").//销售
		append(",r.operator operator ").//计调
		append(",m.currencyId currencyid ").//金额币种
		append(",m.amount amount ").//转款金额
		append(",r.last_reviewer lastreviewer ").//上一环节审批人
		append(",r.status status ").//审批状态
		append(",r.print_status printstatus ").//打印状态
		append(" from review_new r left join review_process_money_amount m on r.id = m.reviewId ").//
		append(" where 1 = 1 and r.del_flag = " + Context.DEL_FLAG_NORMAL + " and r.company_id = '" + companyUuid + "' and r.need_no_review_flag=0 and r.process_type = '" + Context.REVIEW_FLOWTYPE_TRANSFER_MONEY + "' ").
		append(" and ((r.product_type in(" + prdStr + ") and r.dept_id in (" + deptIdStr + ")) or FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");//
		/*拼装查询条件*/
		Object orderCdGroupCdProductNm = params.get("orderCdGroupCdProductNm");
		if(orderCdGroupCdProductNm != null && !"".equals(orderCdGroupCdProductNm.toString())){
			querySql.append(" and (r.group_code like '%" + orderCdGroupCdProductNm.toString()).
			append("%' or r.product_name like '%" + orderCdGroupCdProductNm.toString()).
			append("%' or r.order_no like '%" + orderCdGroupCdProductNm.toString() + "%') ");
		}
		Object productType = params.get("productType");
		if(productType != null && !"".equals(productType.toString()) && !"0".equals(productType.toString())){
			querySql.append(" and r.product_type = " + productType.toString() + " ");
		}
		Object agentId = params.get("agentId");
		if(agentId != null && !"".equals(agentId.toString()) && !"-99999".equals(agentId.toString())){
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
		if(applyPerson != null && !"".equals(applyPerson.toString()) && !"-99999".equals(applyPerson.toString())){
			querySql.append(" and r.create_by = " + applyPerson.toString() + " ");
		}
		Object operator = params.get("operator");
		if(operator != null && !"".equals(operator.toString())){
			querySql.append(" and r.agent = " + operator.toString() + " ");
		}
		Object transferMoneyFrom = params.get("transferMoneyFrom");
		if(transferMoneyFrom != null && !"".equals(transferMoneyFrom.toString()) && NumberUtils.isNumber(transferMoneyFrom.toString())){
			querySql.append(" and m.amount >= " + Integer.parseInt(transferMoneyFrom.toString()) + " ");
		}
		Object transferMoneyTo = params.get("transferMoneyTo");
		if(transferMoneyTo != null && !"".equals(transferMoneyTo.toString()) && NumberUtils.isNumber(transferMoneyTo.toString())){
			querySql.append(" and m.amount <= " + Integer.parseInt(transferMoneyTo.toString()) + " ");
		}
		/*审批状态  -1 为全部 1 审批中 2 已通过 0 已驳回 3已取消*/
		Object reviewStatus = params.get("reviewStatus");
		if(reviewStatus != null && !"-99999".equals(reviewStatus.toString()) && NumberUtils.isNumber(reviewStatus.toString())){
			querySql.append(" and r.status = " + Integer.parseInt(reviewStatus.toString()) + " ");
		}
		Object cashConfirm = params.get("cashConfirm");
		if(cashConfirm != null && StringUtils.isNotBlank(cashConfirm.toString()) && NumberUtils.isNumber(cashConfirm.toString())){
			querySql.append(" and r.pay_status = " + Integer.parseInt(cashConfirm.toString()) + " ");
		}
		Object printStatus = params.get("printStatus");
		if(printStatus != null && !"".equals(printStatus.toString()) && NumberUtils.isNumber(printStatus.toString())){
			querySql.append(" and r.print_status = " + Integer.parseInt(printStatus.toString()) + " ");
		}
		/*状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批*/
		Object statusChoose = params.get("statusChoose");
		if(statusChoose != null && !"".equals(statusChoose.toString()) && NumberUtils.isNumber(statusChoose.toString()) && !Context.REVIEW_TAB_ALL.equals(statusChoose.toString())){
			int statusChooseInt = Integer.parseInt(statusChoose.toString());
			if( Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == statusChooseInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == statusChooseInt){
				querySql.append(" and r.id in (select review_id from review_log_new  where operation in (1,2) and active_flag = 1 and create_by = '" + userId + "') ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == statusChooseInt){
				querySql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");
			}
		}
		Object paymentType = params.get("paymentType");
		if(paymentType != null && !"".equals(paymentType.toString())){
			querySql.append(" and 	r.agent in (select id from agentinfo WHERE paymentType = "+paymentType+")");
		}
		//按审批id分组
		querySql.append(" group by r.id ");
		//排序 默认按重要程度降序 按创建时间降序 
		querySql.append(" order by r.critical_level desc ");

		Object orderCreateDateSort = params.get("orderCreateDateSort");
		Object orderUpdateDateSort = params.get("orderUpdateDateSort");
		if(orderCreateDateSort != null && !"".equals(orderCreateDateSort.toString())){
			querySql.append(" ,r.create_date " + orderCreateDateSort.toString() + " ");
		} else if(orderUpdateDateSort != null && !"".equals(orderUpdateDateSort.toString())){
			querySql.append(" ,r.update_date " + orderUpdateDateSort.toString() + " ");
		} else {
			querySql.append(" ,r.create_date desc "); //默认按照创建日期排序
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

	/**
	 * add by songyang
	 * add date 2015年11月6日09:41:16
	 * describe 借款审批机票
	 * @param rid
	 * @param result
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public  Map<String,Object> reviewBorrowing(String rid,Integer result,HttpServletRequest request){
		Map<String,Object>map = new HashMap<String,Object>();
		ReviewNew r = processReviewService.getReview(rid);
		if(null != r && OPERATE_SUCCESS.intValue() == r.getStatus().intValue()){
			boolean flag = false;
			try {
				flag = newAirTicketOrderLendMoneyService.saveLendMoney2MoneyAmount(rid, r.getOrderId() , new Integer(r.getProductType()));
			} catch (Exception e) {
				map.put("flag", "0");
				map.put("message", "操作失败!");
				return map;
			}
			if(flag){
				map.put("flag", "1");
				map.put("message", "操作成功！");	
			}else{
				map.put("flag", "0");
				map.put("message", "操作失败!");
			}
		}else{
			map.put("flag", "0");
			map.put("message", "操作失败!");
		}

		return map;

	}

	/**
	 * 查询还签证押金收据审批list
	 * @author yang.jiang 2015-12-2 17:56:01
	 * @param conditionsMap
	 * @return
	 */
	public Page<Map<String, Object>> getReturnDepositReceiptReviewList(
			Map<String, Object> conditionsMap) {
		
		//获取当前登录用户的id
		Long userId = UserUtils.getUser().getId();
		/*声明查询SQL*/
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT r.order_no orderno,r.id revid ")//订单编号
				.append(",r.order_id orderid ")//订单id
				.append(",r.group_code groupcode ")//团号
				.append(",r.group_id groupid ")//团期id
				.append(",r.product_name acitivityName ")//产品名称
				.append(",r.product_id productId ")//产品id
				.append(",r.product_type producttype ")//产品类型
				.append(",r.create_date createtime ")//申请时间
				.append(",r.create_by createby ")//审批发起人
				.append(",r.agent agent ")//渠道商
				.append(",r.operator operator ")//计调
				.append(",m.currencyId currencyid ")//金额币种
				.append(",m.amount amount ")//退款金额
				.append(",r.last_reviewer lastreviewer ")//上一环节审批人
				.append(",r.status status ")//审批状态
				.append(",r.pay_status paystatus ")//出纳确认
				.append(",r.print_status printstatus ")//打印状态
				.append(" FROM review_new r LEFT JOIN review_process_money_amount m ON r.id = m.reviewId ")//
				.append(" WHERE 1 = 1 and r.process_type = '" + Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT + "' ")
				.append(" AND FIND_IN_SET ('" + userId + "', r.all_reviewer) ");//
				
		/*拼装查询条件*/
		Object orderCdGroupCd = conditionsMap.get("orderCdGroupCd");
		if(orderCdGroupCd != null && !"".equals(orderCdGroupCd.toString())){
			querySql.append(" and (r.group_code like '%" + orderCdGroupCd.toString())
					.append("%' or r.order_no like '%" + orderCdGroupCd.toString() + "%') ");
		}
		Object productType = conditionsMap.get("productType");
		if(productType != null && !"".equals(productType.toString()) && !"0".equals(productType.toString())){
			querySql.append(" and r.product_type = " + productType.toString() + " ");
		}
		Object agentId = conditionsMap.get("agentId");
		if(agentId != null && !"".equals(agentId.toString()) && !"-99999".equals(agentId.toString())){
			querySql.append(" and r.agent = " + agentId.toString() + " ");
		}
		Object applyDateFrom = conditionsMap.get("applyDateFrom");
		if(applyDateFrom != null && !"".equals(applyDateFrom.toString())){
			querySql.append(" and r.create_date >= '" + applyDateFrom.toString() + " 00:00:00' ");
		}
		Object applyDateTo = conditionsMap.get("applyDateTo");
		if(applyDateTo != null && !"".equals(applyDateTo.toString())){
			querySql.append(" and r.create_date <= '" + applyDateTo.toString() + " 23:59:59' ");
		}
		Object applyPerson = conditionsMap.get("applyPerson");
		if(applyPerson != null && !"".equals(applyPerson.toString()) && !"-99999".equals(applyPerson.toString())){
			querySql.append(" and r.create_by = " + applyPerson.toString() + " ");
		}
		Object travlerName = conditionsMap.get("travlerName");
		if(travlerName != null && !"".equals(travlerName.toString())){
			querySql.append(" and r.traveller_name like '%" + travlerName.toString() + "%' ");
		}
		Object refundMoneyFrom = conditionsMap.get("refundMoneyFrom");
		if(refundMoneyFrom != null && !"".equals(refundMoneyFrom.toString()) && NumberUtils.isNumber(refundMoneyFrom.toString())){
			querySql.append(" and m.amount >= " + Integer.parseInt(refundMoneyFrom.toString()) + " ");
		}
		Object refundMoneyTo = conditionsMap.get("refundMoneyTo");
		if(refundMoneyTo != null && !"".equals(refundMoneyTo.toString()) && NumberUtils.isNumber(refundMoneyTo.toString())){
			querySql.append(" and m.amount <= " + Integer.parseInt(refundMoneyTo.toString()) + " ");
		}
		/*审批状态  空 为全部 1 审批中 2 已通过 0 未通过*/
		Object reviewStatus = conditionsMap.get("reviewStatus");
		if(reviewStatus != null && !"-99999".equals(reviewStatus.toString()) && NumberUtils.isNumber(reviewStatus.toString())){
			querySql.append(" and r.status = " + Integer.parseInt(reviewStatus.toString()) + " ");
		}
		Object cashConfirm = conditionsMap.get("cashConfirm");
		if(cashConfirm != null && StringUtils.isNotBlank(cashConfirm.toString()) && NumberUtils.isNumber(cashConfirm.toString())){
			querySql.append(" and r.pay_status = " + Integer.parseInt(cashConfirm.toString()) + " ");
		}
		Object printStatus = conditionsMap.get("printStatus");
		if(printStatus != null && !"".equals(printStatus.toString()) && NumberUtils.isNumber(printStatus.toString())){
			querySql.append(" and r.print_status = " + Integer.parseInt(printStatus.toString()) + " ");
		}
		/*状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批*/
		Object statusChoose = conditionsMap.get("statusChoose");
		if(statusChoose != null && !"".equals(statusChoose.toString()) && NumberUtils.isNumber(statusChoose.toString()) 
				&& !Context.REVIEW_TAB_ALL.equals(statusChoose.toString())){
			int statusChooseInt = Integer.parseInt(statusChoose.toString());
			if( Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == statusChooseInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
				querySql.append(" and r.status = " + Context.REVIEW_STATUS_WAIT + " ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == statusChooseInt){
				querySql.append(" and r.id in (select review_id from review_log_new  where operation = 1 and active_flag = 1 and create_by = '" + userId + "') ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == statusChooseInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.all_reviewer) and FIND_IN_SET('" + userId + "', r.skipped_assignee)");
			}
		}
		//排序 默认按重要程度降序 按创建时间降序 
		querySql.append(" order by r.critical_level desc ");

		Object orderCreateDateSort = conditionsMap.get("orderCreateDateSort");
		Object orderUpdateDateSort = conditionsMap.get("orderUpdateDateSort");
		if(orderCreateDateSort != null && !"".equals(orderCreateDateSort.toString())){
			querySql.append(" ,r.create_date " + orderCreateDateSort.toString() + " ");
		} else if(orderUpdateDateSort != null && !"".equals(orderUpdateDateSort.toString())){
			querySql.append(" ,r.update_date " + orderUpdateDateSort.toString() + " ");
		} else {
			querySql.append(" ,r.create_date desc ");
		}
		//执行SQL查询出列表数据
		@SuppressWarnings("unchecked")
		Page<Map<String, Object>> page = refundReviewDao.findBySql((Page<Map<String, Object>>)conditionsMap.get("page"), querySql.toString(), Map.class);
		//		为列表数据组装审批变量
		List<Map<String, Object>> list = page.getList();
		Object reviewid = null;
		Object status = null;
		for(Map<String, Object> map : list){
			reviewid = map.get("revid");
			if(reviewid == null || "".equals(reviewid.toString())){
				continue;
			}

			Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewid.toString());
			map.putAll(reviewMap);
			map.put("isBackReview", ReviewUtils.isBackReview(reviewid.toString()));
			map.put("isCurReviewer", ReviewUtils.isCurReviewer(reviewMap.get("currentReviewer")));
			status = map.get("status");
			if(status == null || StringUtils.isBlank(status.toString())){
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
		page.setList(list);
		return page;
		
	}
	
	/**
	 * 查询还签证收据审批list (后来游客列表也使用此方法进行查询, group by batch_no)
	 * @author yang.jiang 2015年12月5日 11:08:35
	 * @param conditionsMap
	 * @return
	 */
	public Page<Map<String, Object>> visaReturnReceiptBatchReviewList4CW(Map<String, Object> conditionsMap) {
		
		//获取当前登录用户的id
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		List<Integer> typeList = new ArrayList<Integer>();
		typeList.add(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);  //转款
		UserReviewPermissionResultForm userReviewPermissionResultForm = systemService.findPermissionByUserIdAndCompanyUuidAndFlowTypeList(userId, companyUuid, typeList);
		//部门
		Set<String> deptIds = userReviewPermissionResultForm.getDeptId();
		String deptIdStr = "";
		if(deptIds != null && deptIds.size() > 0){
			deptIdStr = StringUtils.join(deptIds, ",");
		}
		if("".equals(deptIdStr)){//给默认值
			deptIdStr = "-1,-2";
		}
		//产品
		Set<String> prds = userReviewPermissionResultForm.getProductTypeId();
		String prdStr = "";
		if(prds != null && prds.size() > 0){
			prdStr = StringUtils.join(prds, ",");
		}
		if("".equals(prdStr)){//给默认值
			prdStr = "-1,-2";
		}
		/*声明查询SQL*/
		StringBuffer querySql = new StringBuffer();
		querySql.append("select r.order_no orderno,r.id revid ")//订单编号
				.append(",r.batch_no batchNo ")//批次号
				.append(",r.order_id orderid ")//订单id
				.append(",(select v.groupCode from visa_products v where v.id = r.product_id) as groupcode ")//团号
				.append(",r.group_id groupid ")//团期id
				.append(",r.product_name acitivityName ")//产品名称
				.append(",r.product_id productId ")//产品id
				.append(",r.product_type producttype ")//产品类型
				.append(",r.create_date createdate ")//申请时间
				.append(",r.create_by createby ")//审批发起人
				.append(",r.agent agent ")//渠道商
				.append(",r.saler saler ")// 销售
				.append(",r.operator operator ")//计调
				.append(",r.order_creator order_creator ")//下单人Id
				.append(",r.order_creator_name order_creator_name ")//下单人姓名		
				.append(",m.currencyId currencyid ")//金额币种
				.append(",m.amount amount ")//借款金额
				.append(",vsbo.batch_person_count batchPersonCount ")// 还收据人数
				.append(",r.last_reviewer lastreviewer ")//上一环节审批人
				.append(",r.status status ")//审批状态
				.append(",r.pay_status paystatus ")//出纳确认
				.append(",r.print_status printStatus ")//打印状态
				.append(" FROM review_new r ")
				.append(" LEFT JOIN review_process_money_amount m on r.id = m.reviewId ")
				.append(" LEFT JOIN visa_flow_batch_opration vsbo on r.batch_no = vsbo.batch_no ")//
				.append(" WHERE 1 = 1 and r.del_flag = " + Context.DEL_FLAG_NORMAL + " and r.company_id = '" + companyUuid + "' ")
				.append(" AND r.need_no_review_flag=0 and r.process_type = '" + Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT + "' AND vsbo.busyness_type = 1 ")
				.append(" AND ((r.product_type in(" + prdStr + ") and r.dept_id in (" + deptIdStr + ")) or FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");
		
		/*拼装查询条件*/
		Object orderCdGroupCdBatchNo = conditionsMap.get("orderCdGroupCdBatchNo");
		if(orderCdGroupCdBatchNo != null && !"".equals(orderCdGroupCdBatchNo.toString())){
			querySql.append(" and ((select v.groupCode from visa_products v where v.id = r.product_id) like '%" + orderCdGroupCdBatchNo.toString())
					.append("%' or r.order_no like '%" + orderCdGroupCdBatchNo.toString())
					.append("%' or r.batch_no like '%" + orderCdGroupCdBatchNo.toString() + "%') ");
		}
		Object productType = conditionsMap.get("productType");
		if(productType != null && !"".equals(productType.toString()) && !"0".equals(productType.toString())){
			querySql.append(" and r.product_type = " + productType.toString() + " ");
		}
		Object agentId = conditionsMap.get("agentId");
		if(agentId != null && !"".equals(agentId.toString()) && !"-99999".equals(agentId.toString())){
			querySql.append(" and r.agent = " + agentId.toString() + " ");
		}
		Object applyDateFrom = conditionsMap.get("applyDateFrom");
		if(applyDateFrom != null && !"".equals(applyDateFrom.toString())){
			querySql.append(" and r.create_date >= '" + applyDateFrom.toString() + " 00:00:00' ");
		}
		Object applyDateTo = conditionsMap.get("applyDateTo");
		if(applyDateTo != null && !"".equals(applyDateTo.toString())){
			querySql.append(" and r.create_date <= '" + applyDateTo.toString() + " 23:59:59' ");
		}
		Object order_creator = conditionsMap.get("order_creator");
		if(order_creator != null && !"".equals(order_creator.toString()) && !"-99999".equals(order_creator.toString())){
			querySql.append(" and r.create_by = " + order_creator.toString() + " ");
		}
		Object applyPerson = conditionsMap.get("applyPerson");
		if(applyPerson != null && !"".equals(applyPerson.toString()) && !"-99999".equals(applyPerson.toString())){
			querySql.append(" and r.create_by = " + applyPerson.toString() + " ");
		}
		Object travlerName = conditionsMap.get("travlerName");
		if(travlerName != null && !"".equals(travlerName.toString())){
			querySql.append(" and r.traveller_name like '%" + travlerName.toString() + "%' ");
		}
		Object refundMoneyFrom = conditionsMap.get("refundMoneyFrom");
		if(refundMoneyFrom != null && !"".equals(refundMoneyFrom.toString()) && NumberUtils.isNumber(refundMoneyFrom.toString())){
			querySql.append(" and m.amount >= " + Integer.parseInt(refundMoneyFrom.toString()) + " ");
		}
		Object refundMoneyTo = conditionsMap.get("refundMoneyTo");
		if(refundMoneyTo != null && !"".equals(refundMoneyTo.toString()) && NumberUtils.isNumber(refundMoneyTo.toString())){
			querySql.append(" and m.amount <= " + Integer.parseInt(refundMoneyTo.toString()) + " ");
		}
		/*审批状态  空 为全部 1 审批中 2 已通过 0 未通过*/
		Object reviewStatus = conditionsMap.get("reviewStatus");
		if(reviewStatus != null && !"-99999".equals(reviewStatus.toString()) && NumberUtils.isNumber(reviewStatus.toString())){
			querySql.append(" and r.status = " + Integer.parseInt(reviewStatus.toString()) + " ");
		}
		Object cashConfirm = conditionsMap.get("cashConfirm");
		if(cashConfirm != null && StringUtils.isNotBlank(cashConfirm.toString()) && NumberUtils.isNumber(cashConfirm.toString())){
			querySql.append(" and r.pay_status = " + Integer.parseInt(cashConfirm.toString()) + " ");
		}
		Object printStatus = conditionsMap.get("printStatus");
		if(printStatus != null && !"".equals(printStatus.toString()) && !"-99999".equals(printStatus.toString()) && NumberUtils.isNumber(printStatus.toString())){
			querySql.append(" and r.print_status = " + Integer.parseInt(printStatus.toString()) + " ");
		}
		/*状态选择 0 全部 1 待本人审批 2 本人审批通过 3 非本人审批*/
		Object statusChoose = conditionsMap.get("statusChoose");
		if(statusChoose != null && !"".equals(statusChoose.toString()) && NumberUtils.isNumber(statusChoose.toString()) && !Context.REVIEW_TAB_ALL.equals(statusChoose.toString())){
			int statusChooseInt = Integer.parseInt(statusChoose.toString());
			if( Integer.parseInt(Context.REVIEW_TAB_TO_BE_REVIEWED) == statusChooseInt){
				querySql.append(" and FIND_IN_SET ('" + userId + "', r.current_reviewer) ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_REVIEWED) == statusChooseInt){
				querySql.append(" and r.id in (select review_id from review_log_new  where operation in (1,2) and active_flag = 1 and create_by = '" + userId + "') ");
			} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == statusChooseInt){
				querySql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");
			}
		}
		//判断是否是游客列表，如果不是，则无需添加分组
		Object isTravellerList = conditionsMap.get("isTravellerList");
		if(isTravellerList == null || !"traveller".equals(isTravellerList.toString())){
			querySql.append(" group by r.batch_no ");
		}
		//排序 默认按重要程度降序 按创建时间降序 
		querySql.append(" order by r.critical_level desc ");

		Object orderCreateDateSort = conditionsMap.get("orderCreateDateSort");
		Object orderUpdateDateSort = conditionsMap.get("orderUpdateDateSort");
		if(orderCreateDateSort != null && !"".equals(orderCreateDateSort.toString())){
			querySql.append(" ,r.create_date " + orderCreateDateSort.toString() + " ");
		} else if(orderUpdateDateSort != null && !"".equals(orderUpdateDateSort.toString())){
			querySql.append(" ,r.update_date " + orderUpdateDateSort.toString() + " ");
		} else {
			querySql.append(" ,r.create_date desc ");
		}
		//执行SQL查询出列表数据
		@SuppressWarnings("unchecked")
		Page<Map<String, Object>> page = refundReviewDao.findBySql((Page<Map<String, Object>>)conditionsMap.get("pageP"), querySql.toString(), Map.class);
		//		为列表数据组装审批变量
		if (page != null && CollectionUtils.isNotEmpty(page.getList())) {					
			List<Map<String, Object>> list = page.getList();
			Object reviewid = null;
			Object status = null;
			Object batchNo = null;
			for(Map<String, Object> map : list){

				reviewid = map.get("revid");
				if(reviewid == null || "".equals(reviewid.toString())){
					continue;
				}
				
				Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewid.toString());
				
				batchNo = map.get("batchNo");
				//借款总额，还款总额
				Map<String, Object> totalMap = culReceiptAndLendToalMoney(batchNo);
				map.put("totalReceipt", totalMap.get("totalReceipt") == null ? "" : handleForNumber(totalMap.get("totalReceipt").toString()));
				map.put("totalLend", totalMap.get("totalLend") == null ? "" : handleForNumber(totalMap.get("totalLend").toString()));
				
				map.putAll(reviewMap);
				map.put("isBackReview", ReviewUtils.isBackReview(reviewid.toString()));
				map.put("isCurReviewer", ReviewUtils.isCurReviewer(reviewMap.get("currentReviewer")));
				status = map.get("status");
				if(status == null || StringUtils.isBlank(status.toString())){
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
			page.setList(list);
		}
		return page;
		
	}
	
	/**
	 * 处理金额显示小数点后两位 TODO（待完善）
	 * @return
	 */
	private String handleForNumber(String srcNumber){
		String result = "";
		if (StringUtils.isBlank(srcNumber)) {
			return result;
		}
		//小数点后不足两位的，补足0
		String[] numberStr = srcNumber.split("\\.");
		if (numberStr.length == 1) {  //整数 + 0.00
			result = srcNumber + ".00";
		} else if (numberStr[1].length() == 1) {  //一位小数 + 0
			result = srcNumber + "0";
		} else {  //默认2位（未考虑3位及以上）
			result = srcNumber;
		}
		
		return result;
	}

	/**
	 * 查询批次号下所有人的借款金额总和、收据金额总和
	 * @author yang.jiang 2015年12月5日 19:02:49
	 * @param batchNo
	 * @return
	 */
	private Map<String, Object> culReceiptAndLendToalMoney(Object batchNo) {
		Map<String, Object> totalMap = Maps.newHashMap();
		totalMap.put("totalReceipt", null);
		totalMap.put("totalLend", null);
		
		if (batchNo != null && !"".equals(batchNo.toString())) {
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("select r.id reviewId, r.batch_no batchNo, r.traveller_name travellerName, r.traveller_id travellerId ").
					append("from review_new r where r.batch_no = ").
					append("'"+batchNo.toString()+"'");
			List<Map<String, Object>> list = visaBorrowMoneyReviewDao.findBySql(buffer.toString(), Map.class);
			
			//收据总额
			Double receiptMoney = Double.valueOf(0);
			//借款总额
			Double lendMoney = Double.valueOf(0);
			
			if (CollectionUtils.isNotEmpty(list)) {
				for(Map<String, Object> map : list) {
					if (null != map.get("reviewId") && !"".equals(map.get("reviewId"))) {
						Map<String, Object> reviewDetailMap = Maps.newHashMap();
						if (StringUtils.isNotBlank(map.get("reviewId").toString())) {
							reviewDetailMap = reviewService.getReviewDetailMapByReviewId(map.get("reviewId").toString());
						}						
						Double receiptEvery = Double.valueOf(0);
						Double lendEvery = Double.valueOf(0);
						//收据金额
						if (reviewDetailMap.get("receiptAmount") != null && StringUtils.isNotBlank(reviewDetailMap.get("receiptAmount").toString())) {
							receiptEvery = Double.parseDouble(reviewDetailMap.get("receiptAmount").toString());							
						}												
						//借款金额
						if (reviewDetailMap.get("borrowAmount_return") != null && StringUtils.isNotBlank(reviewDetailMap.get("borrowAmount_return").toString())) {
							lendEvery = Double.parseDouble(reviewDetailMap.get("borrowAmount_return").toString());
						}
						//累加收据总额、借款总额
						receiptMoney += receiptEvery;
						lendMoney += lendEvery;						
					}					
				}
			}
			totalMap.put("totalReceipt", receiptMoney);
			totalMap.put("totalLend", lendMoney);			
		}		
		return totalMap;
	}

	/**
	 * 封装借款单打印数据
	 * @param reviewId 审批id
	 * @param resultMap
     * @return
     */
	public PrintFormBean buildPrintFormBean(String reviewId, MultiValueMap<Integer, User> resultMap, Map<String, Object> map) {
		PrintFormBean printFormBean = new PrintFormBean();
		// 借款申请相关信息
		ReviewNew reviewNew = reviewService.getReview(reviewId);
		if (reviewNew != null) {
			printFormBean.setGroupCode(reviewNew.getGroupCode());
			printFormBean.setOrderId(Long.parseLong(reviewNew.getOrderId()));
			printFormBean.setPayStatus(reviewNew.getPayStatus().toString());
			printFormBean.setPrintDate(reviewNew.getPrintDate());
			printFormBean.setRevCreateDate(reviewNew.getCreateDate());// 填写日期
			String remark = reviewNew.getRemark().replace("#_", " ");
			printFormBean.setRevBorrowRemark(remark);// 申报原因
			User user = UserUtils.getUser(reviewNew.getCreateBy());
			String lastReviewerName = UserUtils.getUser(reviewNew.getLastReviewer()).getName();
			if (null != user) {
				printFormBean.setOperatorName(user.getName());// 经办人、领款人都为借款申请人
			} else {
				printFormBean.setOperatorName("未知");
			}
			printFormBean.setPayDate(reviewNew.getUpdateDate());// 付款日期
			//借款金额（人民币）
			BigDecimal lendMoneyRMB = BigDecimal.ZERO;
			//查询借款金额（多币种）
			List<NewProcessMoneyAmount> newMoneyAmountList = newMoneyAmountService.findListByReviewId(reviewId);			
			//依照汇率转换其他币种为人民币
			if (CollectionUtils.isNotEmpty(newMoneyAmountList)) {
				for (NewProcessMoneyAmount newProcessMoneyAmount : newMoneyAmountList) {
					//获取moneyAmount中自带汇率
					BigDecimal exchangerate = newProcessMoneyAmount.getExchangerate();
					//如果没有汇率，则去currency表中获取最低汇率
					if (exchangerate == null || exchangerate == BigDecimal.ZERO) {
						exchangerate = getCurrencyLowExchangerate(newProcessMoneyAmount.getCurrencyId());
					}
					lendMoneyRMB = lendMoneyRMB.add(newProcessMoneyAmount.getAmount().multiply(exchangerate));
				}				
			}

			// 设置借款金额
			printFormBean.setRevBorrowAmount(MoneyNumberFormat.fmtMicrometer(lendMoneyRMB.toString(), MoneyNumberFormat.FMT_MICROMETER));
			// 设置借款金额大写
			printFormBean.setRevBorrowAmountDx(MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.fmtMicrometer(lendMoneyRMB.toString(), MoneyNumberFormat.POINT_TWO))));
//			printFormBean.setRevBorrowAmountDx(StringNumFormat.changeAmount(MoneyNumberFormat.fmtMicrometer(lendMoneyRMB.toString(), MoneyNumberFormat.POINT_TWO).toString().replaceAll("-","")));
			String supervisor = "";
			String manager = "";
			String financeSupervisor = "";
			String cashier = "";
			String approver = "";
			//主管		key=1
			List<User> users1 = resultMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);
			if(users1 != null && users1.size() > 0) {
				for (User user1 : users1) {
					supervisor += user1.getName() + " ";
				}
			}
			//总经理 		key=2
			List<User> users2 = resultMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);
			if(users2 != null && users2.size() > 0) {
				for (User user2 : users2) {
					manager += user2.getName() + " ";
				}
			}
			//财务主管 	key=3
			List<User> users3 = resultMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);
			if(users3 != null && users3.size() > 0) {
				for (User user3 : users3) {
					financeSupervisor += user3.getName() + " ";
				}
			}
			//出纳		key=4
			List<User> users4 = resultMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);
			if(users4 != null && users4.size() > 0) {
				for (User user4 : users4) {
					cashier += user4.getName() + " ";
				}
			}
			//审批		key=5
			List<User> users5 = resultMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);
			if(users5 != null && users5.size() > 0) {
				for (User user5 : users5) {
					approver += user5.getName() + " ";
				}
			}
			printFormBean.setCw(supervisor);
			printFormBean.setCwmanager(financeSupervisor);
			printFormBean.setMajorCheckPerson(manager);		//总经理
			printFormBean.setCashier(cashier);				//出纳
			map.put("majorCheckPerson", lastReviewerName);	//审批人，从review_new 表中获取，取最后一个审批人
			map.put("approver", approver); 					//审批人，从配置中获取
		}

		return printFormBean;

	}

	/**
	 * 依据币种id，获取对应的换汇汇率-公司最低汇率标准
	 * @param currencyId
	 * @return
	 */
	private BigDecimal getCurrencyLowExchangerate(Integer currencyId) {
		// TODO Auto-generated method stub
		Currency targetCurrency = currencyService.findCurrency(Long.parseLong(currencyId.toString()));
		if (targetCurrency != null) {
			return targetCurrency.getConvertLowest();
		}
		return BigDecimal.ZERO;
	}

	public File createBorrowMoneySheetDownloadFile(PrintFormBean printFormBean) throws IOException, TemplateException {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("revCreateDate", DateUtils.date2String(printFormBean.getRevCreateDate(), "yyyy 年 MM 月 dd 日"));
		root.put("revBorrowRemark", printFormBean.getRevBorrowRemark());
		root.put("productCreater", printFormBean.getProductCreater());
		root.put("operatorName", printFormBean.getOperatorName());
		root.put("payDate", DateUtils.date2String(printFormBean.getPayDate(), "yyyy 年 MM 月 dd 日"));
		root.put("revBorrowAmount", printFormBean.getRevBorrowAmount());
		root.put("majorCheckPerson", printFormBean.getMajorCheckPerson());
		root.put("revBorrowAmountDx", printFormBean.getRevBorrowAmountDx());
		root.put("cwmanager", printFormBean.getCwmanager());
		root.put("cw", printFormBean.getCw());
		root.put("deptmanager", printFormBean.getDeptmanager());
		root.put("cashier", printFormBean.getCashier());
		root.put("printFormName", printFormBean.getPrintFormName());
		root.put("borrowDept", printFormBean.getBorrowDept());
		root.put("payDateStr", printFormBean.getPayDateStr());

		return FreeMarkerUtil.generateFile("borrowmoney.ftl", "borrowmoney.doc", root);

	}

	public String getProductCreaterByReviewId(Long orderId, String orderType) {
		String productCreater = "";
		if(orderId == null) {
			return productCreater;
		}

		if(Context.ORDER_STATUS_AIR_TICKET.equals(orderType)) {
			AirticketOrder airticketOrder = ariticketOrderService.getAirticketorderById(orderId);
			productCreater = airticketOrder.getCreateBy().getName();
		} else {
			ProductOrderCommon pro = orderService.getProductorderById(orderId);
			productCreater = pro.getCreateBy().getName();
		}

		return productCreater;
	}

	/**
	 * 由于需要批量打印借款单，所以该方法是把Controller里面的单一打印的方法提取出来到Service层，供总控方法调用。
	 * @param paramBean
	 * @return
     */
	public Map<String, Object> getBorrowMoneyFormMap(PrintParamBean paramBean){
		String reviewId = paramBean.getReviewId();
		String orderType = paramBean.getOrderType().toString();
		String payId = paramBean.getPayId();
		String option = paramBean.getOption();

		Map<String, Object> result = new HashMap<>(); // 用于返回数据的map

		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		MultiValueMap<Integer, User> resultMap = reviewReceiptService.obtainReviewer4Receipt(companyUuid, ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY, reviewId);

		//根据审批表ID获取借款单数据
		PrintFormBean printFormBean = buildPrintFormBean(reviewId, resultMap, result);

		if("pay".equals(option)) {
			//45需求，以每次借款的金额为打印凭单的借款金额
			String revBorrowAmount = "";
			String revBorrowAmountDx = "";
			if(org.apache.commons.lang3.StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, orderType);
				if(payDetail!=null && (Double.valueOf(payDetail.getRefundRMBDispStyle().replaceAll(",", "")).doubleValue() !=
						BigDecimal.ZERO.doubleValue())) {
					revBorrowAmount = payDetail.getRefundRMBDispStyle();
					revBorrowAmountDx = MoneyNumberFormat.digitUppercase(Double.valueOf(revBorrowAmount.replaceAll(",", "")));
				}
			}

			printFormBean.setRevBorrowAmount(revBorrowAmount);
			printFormBean.setRevBorrowAmountDx(revBorrowAmountDx);
		}

		//根据订单类型，获取打印单名称
		printFormBean.setPrintFormName(OrderUtil.getOrderTypeName(orderType)+"借款单");
		printFormBean.setBorrowDept(OrderUtil.getOrderTypeName(orderType)+"部");
		printFormBean.setProductCreater(getProductCreaterByReviewId(printFormBean.getOrderId(), orderType));//经办人
		result.put("reviewId", reviewId);
		result.put("reviewFlag", 2); // 新审核
		result.put("payId", payId);
		result.put("option", option);
		result.put("printFormBean", printFormBean);
		result.put("orderType", orderType);

		return result;
	}
}
