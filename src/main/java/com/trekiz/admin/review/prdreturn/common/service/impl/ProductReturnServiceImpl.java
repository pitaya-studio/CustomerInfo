package com.trekiz.admin.review.prdreturn.common.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.service.IGroupControlBoardService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderServiceForSaveAndModify;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.prdreturn.common.dao.IProductReturnDao;
import com.trekiz.admin.review.prdreturn.common.service.IProductReturnService;

@Service("productReturnServiceImpl")
public class ProductReturnServiceImpl extends BaseService implements IProductReturnService {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private OrderServiceForSaveAndModify orderServiceForSaveAndModify;
	@Autowired 
	IProductReturnDao productReturnDao;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private AirticketActivityReserveDao activityReserveDao;
	
	@Autowired
	private AirticketPreOrderDao airticketPreOrderDao;
	@Autowired
	private TravelerDao travelerDao;
	
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	
	@Autowired
	private OrderStatisticsService orderStatusticsService;
	
	@Autowired
	private TravelerService travelerService;
	
	@Autowired
    private ProductOrderCommonDao productorderDao;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private IAirticketPreOrderService airticketPreOrderService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private IGroupControlBoardService groupControlBoardService;
	

	/**
	 * 查询退票审核列表 可能会加上退团、撤签
	 */
	@Override
	public Page<Map<String, Object>> queryReturnReviewList(
			Map<String, Object> params) {
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		List<Integer> typeList = new ArrayList<Integer>();
		typeList.add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN);//退票
		typeList.add(Context.REVIEW_FLOWTYPE_EXIT_GROUP);//退团
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
		StringBuffer querySql = new StringBuffer("");
		querySql.append("select r.order_no orderno,r.id reviewid ").//订单编号
			append(",r.order_id orderid ").//订单id
			append(",r.group_code groupcode ").//团号
			append(",r.product_name productname ").//产品名称
			append(",r.product_id productid ").//产品id
			append(",r.product_type producttype ").//产品类型
			append(",r.create_date createdate ").//申请时间
			append(",r.create_by createby ").//审批发起人
			append(",r.agent agent ").//渠道商
			append(",t.original_payPriceSerialNum payPriceSerialNum ").//游客原始应收价序列号
			append(",t.payPriceSerialNum settleSerialNum ").//游客结算价序列号
			append(",r.operator operator ").//计调
			append(",r.traveller_id travellerid ").//游客id
			append(",r.traveller_name travelername ").//游客姓名
			append(",r.last_reviewer lastreviewer ").//上一环节审批人
//			append("").//原应付金额 TODO 预留 待产品确认
//			append("").//退团后应付 TODO 预留 待产品确认
			append(",r.status status ").//审批状态
			append(" from review_new r left join traveler t on r.traveller_id = t.id ").//
			append(" where 1 = 1 and r.company_id = '" + companyUuid + "' and r.need_no_review_flag=0 and (r.process_type = '" + Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN + "' or r.process_type = " + Context.REVIEW_FLOWTYPE_EXIT_GROUP + ") ").
			append(" and ((r.product_type in(" + prdStr + ") and r.dept_id in (" + deptIdStr + ")) or FIND_IN_SET ('" + userId + "', r.all_reviewer)) ");//
		/*拼装查询条件*/
		Object groupCode = params.get("groupCode");
		if(groupCode != null && !"".equals(groupCode.toString().trim())){
			querySql.append(" and (r.group_code like '%" + groupCode.toString().trim()).
				append("%' or r.product_name like '%" + groupCode.toString().trim()).
				append("%' or r.order_no like '%" + groupCode.toString().trim() + "%') ");
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
		Object travelerId = params.get("travelerId");
		if(travelerId != null && !"".equals(travelerId.toString())){
			querySql.append(" and r.traveller_name like '%" + travelerId.toString() + "%' ");
		}
		/*审批状态  空 为全部 1 审批中 2 已通过 0 未通过*/
		Object reviewStatus = params.get("reviewStatus");
		if(reviewStatus != null && !"".equals(reviewStatus.toString()) && NumberUtils.isNumber(reviewStatus.toString())){
			querySql.append(" and r.status = " + Integer.parseInt(reviewStatus.toString()) + " ");
		}
		Object cashConfirm = params.get("cashConfirm");
		if(cashConfirm != null && !"".equals(cashConfirm.toString()) && NumberUtils.isNumber(cashConfirm.toString())){
			querySql.append(" and r.pay_status = " + Integer.parseInt(cashConfirm.toString()) + " ");
		}
		Object printStatus = params.get("printStatus");
		if(printStatus != null && !"".equals(printStatus.toString()) && NumberUtils.isNumber(printStatus.toString())){
			querySql.append(" and r.print_status = " + Integer.parseInt(printStatus.toString()) + " ");
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
			} else if( Integer.parseInt(Context.REVIEW_TAB_OTHER_REVIEWED) == tabStatusInt){
				querySql.append(" and not FIND_IN_SET ('" + userId + "', r.all_reviewer) ");
			}
		}
		Object paymentType = params.get("paymentType");
		if(paymentType != null && !"".equals(paymentType.toString())){
			querySql.append(" and r.agent in (select id from agentinfo WHERE paymentType = "+paymentType+")");
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
		Page<Map<String, Object>> page = productReturnDao.findBySql((Page<Map<String, Object>>)params.get("pageP"), querySql.toString(), Map.class);
//				为列表数据组装审核变量
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
			if(status == null || StringUtils.isBlank(status.toString()) || !NumberUtils.isNumber(status.toString())){
				map.put("statusdesc" ,"无审批状态");
				continue;
			}
			if(ReviewConstant.REVIEW_STATUS_PROCESSING == Integer.parseInt(map.get("status").toString())){
				Object cReviewer = map.get("currentReviewer");
				String person = "未分配审批人";
				if(cReviewer != null && !StringUtil.isBlank(cReviewer.toString())){
					person = getReviewerDesc(cReviewer);
					map.put("statusdesc" ,"待" + person + "审批");
				} else {
					map.put("statusdesc" ,person);
				}
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
	 * 调用机票订单模块的方法 还余位
	 */
	@Override
	public int returnPosition(String orderId, String travelId) {
		
		
		AirticketOrder airticketOrder = new AirticketOrder();
		ActivityAirTicket activityAirTicket = new ActivityAirTicket();
		AirticketActivityReserve activityReserve = new  AirticketActivityReserve();
		Traveler traveler = new Traveler();
		
		airticketOrder = airticketPreOrderDao.findOne(Long.parseLong(orderId));
		activityAirTicket = activityAirTicketDao.findOne(airticketOrder.getAirticketId());
		activityReserve = activityReserveDao.findAgentReserve(airticketOrder.getAirticketId(), airticketOrder.getAgentinfoId());
		traveler = travelerDao.findOne(Long.parseLong(travelId));
//	    Transaction beginTransaction = airticketPreOrderDao.getSession().beginTransaction();
		Map<String,String> rMap = orderStatisticsService.getPlaceHolderInfo(airticketOrder.getId(), Context.ORDER_STATUS_AIR_TICKET);
        try {
        	if (traveler.getDelFlag() == Context.TRAVELER_DELFLAG_TURNROUNDED) {
    			throw new Exception("游客已改签，不能退票");
    		}
        	if (null != rMap && null != rMap.get(Context.RESULT)) {
            	String resultP = rMap.get(Context.RESULT);
            	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
            		//当订单占位时归还余位
            		Map<String,String> pMap = orderStatisticsService.saveAirTicketActivityPlaceHolderChange(
            				airticketOrder.getId(), Context.ORDER_STATUS_AIR_TICKET, 1, activityAirTicket);
            		//余位处理失败
            		if (null != pMap && Context.ORDER_PLACEHOLDER_ERROR.toString().equals( pMap.get(Context.RESULT))) {
            			throw new Exception(Context.MESSAGE);
            		}
            		//余位处理成功
            		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))) {
            			
            		} else {
            			throw new Exception("归还余位失败！");
            		}
            	} else if (Context.ORDER_PLACEHOLDER_ERROR.toString().equals(resultP)) {
            		throw new Exception(rMap.get(Context.MESSAGE));
            	}
            } else {
            	throw new Exception("游客退票失败！");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        airticketOrder.setPersonNum(airticketOrder.getPersonNum() - 1);
        /*C401需求 退票通过后,同时更新订单对应的游客类型的数量 by chy 2015年12月5日10:01:06*/
        Integer personType = traveler.getPersonType();
        if(personType != null){
        	if( Context.NumberDef.NUMER_ONE == personType){//成人
        		airticketOrder.setAdultNum(airticketOrder.getAdultNum() - Context.NumberDef.NUMER_ONE);
        	} else if( Context.NumberDef.NUMER_TWO == personType){//儿童
        		airticketOrder.setChildNum(airticketOrder.getChildNum() - Context.NumberDef.NUMER_ONE);
        	} else if( Context.NumberDef.NUMER_THREE == personType){//特殊人群
        		airticketOrder.setSpecialNum(airticketOrder.getSpecialNum() - Context.NumberDef.NUMER_ONE);
        	}
        }
        airticketPreOrderDao.save(airticketOrder);
		activityAirTicketDao.save(activityAirTicket);
		if(activityReserve != null){
			activityReserveDao.save(activityReserve);
		}
		//更改游客的退票标示 0 代表已退票
		traveler.setIsAirticketFlag("0");
		travelerDao.save(traveler);
		String serialNum = traveler.getPayPriceSerialNum();
		/*C401需求 退票通过后，清空游客的结算价，订单中应收价需减去游客结算价，同时更新订单对应的游客类型的数量 by chy 2015年12月5日10:01:06*/
		//1.清空游客结算价
		if(serialNum!=null && !"".equals(serialNum)){
			List<MoneyAmount> findAmountBySerialNum = moneyAmountService.findAmountBySerialNum(serialNum);
			if(findAmountBySerialNum != null && findAmountBySerialNum.size() > 0){
				//订单应收价减去游客结算价
				String totalMoney = airticketOrder.getTotalMoney();
				List<MoneyAmount> totalAmounts = new ArrayList<MoneyAmount>();
				if(totalMoney != null && !"".equals(totalMoney)){
					totalAmounts = moneyAmountService.findAmountBySerialNum(totalMoney);
					if(totalAmounts != null && totalAmounts.size() > 0){
						for(MoneyAmount mTotalAmount : totalAmounts){
							Integer currencyIdT = mTotalAmount.getCurrencyId();
							BigDecimal amountT = mTotalAmount.getAmount();
							for(MoneyAmount moneyAmount : findAmountBySerialNum){
								if(currencyIdT == moneyAmount.getCurrencyId()){
									amountT = amountT.subtract(moneyAmount.getAmount());
								}
							}
							mTotalAmount.setAmount(amountT);
						}
					}
				}
				//游客结算价清空置0
				for(MoneyAmount moneyAmount : findAmountBySerialNum){
					moneyAmount.setAmount(new BigDecimal(0.00));
				}
				findAmountBySerialNum.addAll(totalAmounts);
				moneyAmountService.saveMoneyAmounts(findAmountBySerialNum);
			}
		}
//		beginTransaction.commit();
		
		return 0;
	}
	
	/**
	 * 根据订单id查询订单详情内容
	 */
	@Override
	public Map<String, Object> queryAirTicketReturnInfoById(String id) {
		Map<String, Object> orderDetailInfoMap = productReturnDao
				.queryAirTicketReturnDetailInfoById(id);
		return orderDetailInfoMap;
	}

	@Override
	@Transactional(readOnly = false)
	public void handleFreePositionAndTraveler(String travelerIdStr,
			ProductOrderCommon productOrder, String reviewId, HttpServletRequest request) throws Exception {
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerIdStr));
		if (traveler.getDelFlag() == Context.TRAVELER_DELFLAG_TURNROUNDED) {
			throw new Exception("游客已转团，不能退团");
		}
		// 更新余位
        if (productOrder.getPayStatus() != null 
				&& !Context.ORDER_PAYSTATUS_YQX.equals(productOrder.getPayStatus().toString())
				&& !Context.ORDER_PAYSTATUS_DEL.equals(productOrder.getPayStatus().toString())) {
        	Map<String,String> rMap = orderStatusticsService.getPlaceHolderInfo(productOrder.getId(), productOrder.getOrderStatus().toString());
        	if (null != rMap && null != rMap.get(Context.RESULT)) {
            	//当等于1时，占位
            	String resultP = rMap.get(Context.RESULT);
            	//如果订单占位，要归还余位
            	if(Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)){
            		//当订单占位时归还余位
            		Map<String,String> pMap = orderStatusticsService.saveActivityGroupPlaceHolderChange(
            				productOrder.getId(), productOrder.getOrderStatus().toString(), 1, request, 5);
            		
            		// 0524需求 团期余位变化,记录在团控板中
    				ReviewNew reviewNew = reviewService.getReview(reviewId);
    				groupControlBoardService.insertGroupControlBoard(5, 1, "订单号"+productOrder.getOrderNum()+",1人退团", productOrder.getProductGroupId(), Long.parseLong(reviewNew.getCreateBy()));
    				// 0524需求 团期余位变化,记录在团控板中
    				
            		//余位处理失败
            		if(null != pMap && Context.ORDER_PLACEHOLDER_ERROR.equals( pMap.get(Context.RESULT))) {
            			throw new Exception(pMap.get(Context.MESSAGE));
            		}
            		//余位处理成功
            		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))) {
            			
            		}else{
            			throw new Exception("归还余位失败！");
            		}
            	}else if(Context.ORDER_PLACEHOLDER_ERROR.toString().equals(resultP)){
            		throw new Exception(rMap.get(Context.MESSAGE));
            	}
            } else {
            	throw new Exception("操作失败！");
            }
        }

		handleOrderAndTravelerInfo(productOrder, traveler);

		if (productOrder.getPayStatus() != null
				&& !Context.ORDER_PAYSTATUS_YQX.equals(productOrder.getPayStatus().toString())
				&& !Context.ORDER_PAYSTATUS_DEL.equals(productOrder.getPayStatus().toString())) {
        	//第三步，更新机票余位，调用机票余位处理接口
            if(StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZFDJ, productOrder.getPayStatus().toString())	
               || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZF, productOrder.getPayStatus().toString())
               || StringUtils.equalsIgnoreCase(Context.ORDER_PAYSTATUS_YZW, productOrder.getPayStatus().toString())
            		){
            	airticketPreOrderService.returnFreePosionByProductOrderId(productOrder.getId(), 1);
            }
        }

	}

	/**
	 * 订单金额与人数、游客金额修改（退团和转团共用）
	 * @param productOrder
	 * @param traveler
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private void handleOrderAndTravelerInfo(ProductOrderCommon productOrder, Traveler traveler) {
		 /** 扣减订单人数 */
        int personType = traveler.getPersonType();
        switch(personType){
        	case Context.PERSON_TYPE_ADULT:
        		productOrder.setOrderPersonNumAdult(productOrder.getOrderPersonNumAdult() - 1);
        	break;
        	case Context.PERSON_TYPE_CHILD:
        		productOrder.setOrderPersonNumChild(productOrder.getOrderPersonNumChild() - 1);
        	break;
        	case Context.PERSON_TYPE_SPECIAL:
        		productOrder.setOrderPersonNumSpecial(productOrder.getOrderPersonNumSpecial() - 1);
        	break;
        	default:
        		productOrder.setOrderPersonNumAdult(productOrder.getOrderPersonNumAdult() - 1);
            break;
        }
        productOrder.setOrderPersonNum(productOrder.getOrderPersonNum() - 1);
        productorderDao.save(productOrder);
        
        //第二步，更新游客状态，改为已退团，并扣减相应金额
        reducePrice(productOrder, traveler);
	}
	
	/**
	 * 修改游客成本、结算价；修改订单成本、结算价
	 * 订单成本价、结算价保留扣减金额；游客成本、结算价改成扣减金额
	 * @param productOrder
	 * @param traveler
	 * @author yunpeng.zhang
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void reducePrice(ProductOrderCommon productOrder, Traveler traveler) {
		// 由于真正退给游客的金额没有从review中传到此处，这里使用 原金额减去扣减金额 计算得出
		// 获取游客扣减金额
		List<MoneyAmount> subtractMoneyList = moneyAmountService.findAmountBySerialNum(traveler.getSubtractMoneySerialNum());
		// 获取游客的结算价
		List<MoneyAmount> travelerPayPriceList = moneyAmountService.findAmountBySerialNum(traveler.getPayPriceSerialNum());
		String oldPayPriceString = moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum());
		// 游客成本价
		List<MoneyAmount> travelerCostMoneyList = moneyAmountService.findAmountBySerialNum(traveler.getCostPriceSerialNum());
		// 游客退团退还金额
		List<MoneyAmount> travelerOwnAmounts = new ArrayList<>();
		
		//修改订单成本价、结算价：先减去游客结算价，如果有扣减金额，再加上扣减金额
		if (CollectionUtils.isNotEmpty(travelerPayPriceList)) {
			for (MoneyAmount moneyAmonut : travelerPayPriceList) {
				MoneyAmount tempOwnMoney = new MoneyAmount(); 
				//如果游客有扣减金额，则需要在订单成本价、结算价加上此金额再减去游客成本、结算价
				if (CollectionUtils.isNotEmpty(subtractMoneyList)) {
					for (MoneyAmount subtractMoneyAmount : subtractMoneyList) {					
						if (moneyAmonut.getCurrencyId().equals(subtractMoneyAmount.getCurrencyId())) {
							tempOwnMoney.setAmount(moneyAmonut.getAmount().subtract(subtractMoneyAmount.getAmount()));
							tempOwnMoney.setCurrencyId(moneyAmonut.getCurrencyId());
							break;
						}
					}
				} else {
					tempOwnMoney.setAmount(BigDecimal.ZERO);
					tempOwnMoney.setCurrencyId(moneyAmonut.getCurrencyId());
				}
				travelerOwnAmounts.add(tempOwnMoney);
			}
			//订单应收价要减去的价格：游客结算价
			handlePrice(travelerOwnAmounts, productOrder.getTotalMoney(), false, Context.MONEY_TYPE_YSH, productOrder.getOrderStatus());
			//订单成本价要减去的价格：游客成本价
			handlePrice(travelerOwnAmounts, productOrder.getCostMoney(), false, Context.MONEY_TYPE_CBJ, productOrder.getOrderStatus());
		}
		
		//修改游客结算价、成本价：如果有游客扣减金额，则改为扣减金额，如果没有则都置为0
		if (CollectionUtils.isNotEmpty(subtractMoneyList)) {
			for (MoneyAmount subtractMoneyAmount : subtractMoneyList) {
				//修改游客结算价
				if (CollectionUtils.isNotEmpty(travelerPayPriceList)) {
					for (MoneyAmount payMoneyAmount : travelerPayPriceList) {
						if (payMoneyAmount.getCurrencyId().equals(subtractMoneyAmount.getCurrencyId())) {
							payMoneyAmount.setAmount(subtractMoneyAmount.getAmount());
							moneyAmountService.saveOrUpdateMoneyAmount(payMoneyAmount);
							break;
						}
					}
				}
				//修改游客成本价
				if (CollectionUtils.isNotEmpty(travelerCostMoneyList)) {					
					for (MoneyAmount costMoneyAmount : travelerCostMoneyList) {
						if (costMoneyAmount.getCurrencyId().equals(subtractMoneyAmount.getCurrencyId())) {
							costMoneyAmount.setAmount(subtractMoneyAmount.getAmount());
							moneyAmountService.saveOrUpdateMoneyAmount(costMoneyAmount);
							break;
						}
					}
				}
			}
		}
		//保存游客
		traveler.setDelFlag(Context.TRAVELER_DELFLAG_EXITED);
		travelerDao.save(traveler);
		// 重新计算quauq订单的总额和服务费
		if (Context.PRICE_TYPE_QUJ == productOrder.getPriceType()) {
			orderServiceForSaveAndModify.setOrderChargePrice(productOrder, false);
		}
		// 记录结算价变动 for quauq
		String newPayPriceString = moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum());
		if (Context.PRICE_TYPE_QUJ == productOrder.getPriceType()) {
			// 记录结算价变动日志
			StringBuffer logContent = new StringBuffer();
			logContent.append("QUAUQ订单");
			logContent.append("###");
			logContent.append("退团");
			logContent.append("###");
			logContent.append(traveler.getName() + "游客结算价从" + oldPayPriceString + "修改为" + newPayPriceString);
			this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, logContent.toString(), Context.log_state_add, traveler.getOrderType(), traveler.getOrderId());
		}
	}
	
	/**
	 * 金额相加或相减并保存
	 * @param priceList 要进行计算的金额List
	 * @param serialNum 进行相加减的金额序列号
	 * @param isAdd 是否进行相加
	 * @param moneyType 金额种类
	 * @param orderType 订单类型
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private void handlePrice(List<MoneyAmount> priceList, String serialNum, boolean isAdd, Integer moneyType, Integer orderType) {
		for (MoneyAmount subtractMoneyAmount : priceList) {
			MoneyAmount moneyAmount = new MoneyAmount();
			BeanUtils.copyProperties(subtractMoneyAmount, moneyAmount);
			moneyAmount.setId(null);
			moneyAmount.setSerialNum(serialNum);
			moneyAmount.setMoneyType(moneyType);
			moneyAmount.setOrderType(orderType);
			moneyAmount.setBusindessType(1);
			if (!isAdd) {
				moneyAmount.setAmount(subtractMoneyAmount.getAmount().multiply(new BigDecimal(-1)));
			}
			moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount, serialNum, "add", moneyType);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateTravelerStatus(Integer status, long travelerId) throws Exception {
		try {
			travelerDao.updateTravelerDelFlag(status, travelerId);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

}
