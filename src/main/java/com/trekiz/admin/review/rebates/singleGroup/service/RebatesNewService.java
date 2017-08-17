package com.trekiz.admin.review.rebates.singleGroup.service;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.trekiz.admin.modules.rebatesupplier.service.Rebatedable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Context.ProductType;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
//import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
//import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;
import com.trekiz.admin.modules.order.rebates.entity.RebatesReviewCond;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.order.util.ReviewCommonUtil;
//import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
//import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
//import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
//import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.rebates.singleGroup.repository.RebatesNewDao;

/**
 * 返佣service
 * @author yakun.bai
 * @Date 2015-11-26
 */
@Service
@Transactional(readOnly = true)
public class RebatesNewService extends BaseService {
	@Autowired
	private RebatesNewDao rebatesNewDao;
	@Autowired
	private ReviewLogDao reviewLogDao;
//	@Autowired
//	private RoleDao roleDao;
//	@Autowired
//    private AgentinfoDao agentinfoDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
    private OrderCommonService orderService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CostManageService costManageService;
//	@Autowired
//	private ReviewCommonService reviewCommonService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
//	@Autowired
//	private IAirticketOrderDao airticketOrderDao;
//	@Autowired
//	private VisaOrderService visaOrderService;
//	@Autowired
//	private VisaProductsDao visaProductsDao;
	@Autowired
    private UserReviewPermissionChecker permissionChecker;
	@Autowired
	private ReviewMutexService reviewMutexService;

	/**
	 * @Description 保存返佣申请
	 * @author yakun.bai
	 * @Date 2015-12-1
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String save(List<RebatesNew> rebatesList,Map<String,String> rebatesObjectInfo) {
		
		// 流程互斥校验
		if (CollectionUtils.isNotEmpty(rebatesList)) {
			for (RebatesNew rebates : rebatesList) {
				Traveler traveler = rebates.getTraveler();
				String orderId = rebates.getOrderId().toString();
				String orderType = rebates.getOrderType().toString();
				CommonResult result;
				if (traveler != null) {
					result = reviewMutexService.check(orderId, traveler.getId().toString(), orderType, 
							Context.REBATES_FLOW_TYPE.toString(), false);
				} else {
					result = reviewMutexService.check(orderId, "0", orderType, 
							Context.REBATES_FLOW_TYPE.toString(), true);
				}
				// 如果校验失败则返回错误结果
				if (!result.getSuccess()) {
					return result.getMessage()+"<br>请重新选择游客！";
				}
			}
		}

		if(rebatesList!=null&&rebatesList.size()>0){

			//获取订单、团期、产品信息
			ProductOrderCommon productOrder = orderService.getProductorderById(rebatesList.get(0).getOrderId());
			ActivityGroup group = activityGroupService.findById(productOrder.getProductGroupId());
			TravelActivity product = travelActivityService.findById(productOrder.getProductId());

			//获取订单信息数据
			Map<String, Object> orderInfo = OrderCommonUtil.getOrderInfo(productOrder, group);
			//获取产品信息
			Map<String, Object> productInfo = OrderCommonUtil.getProductInfo(product);

			/**
			 * 返佣对象信息(返佣供应商或者渠道)
			 */
			Map<String, Object> rebatesObjectInfo4Review=new HashMap<>();
			if(rebatesObjectInfo!=null&&rebatesObjectInfo.containsKey(Context.REBATES_OBJECT_TYPE)&&rebatesObjectInfo.get(Context.REBATES_OBJECT_TYPE).equals(Rebatedable.REBATEDABLE_TYPE_SUPPLIER.toString())){
				//返佣对象为供应商
				rebatesObjectInfo4Review.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_TYPE,Rebatedable.REBATEDABLE_TYPE_SUPPLIER.toString());//审批相关对象类型
				if(rebatesObjectInfo.containsKey(Context.REBATES_OBJECT_ID)){
					rebatesObjectInfo4Review.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT,rebatesObjectInfo.get(Context.REBATES_OBJECT_ID));//审批相关对象id
				}
				if (rebatesObjectInfo.containsKey(Context.REBATES_OBJECT_NAME)){
					rebatesObjectInfo4Review.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_NAME,rebatesObjectInfo.get(Context.REBATES_OBJECT_NAME));//审批相关对象名称
				}

				//自定义属性：账户id、类型、开户行名称、账户号码
				if (rebatesObjectInfo.containsKey(Context.REBATES_OBJECT_ACCOUNT_ID)){// 账号id
					rebatesObjectInfo4Review.put(Context.REBATES_OBJECT_ACCOUNT_ID,rebatesObjectInfo.get(Context.REBATES_OBJECT_ACCOUNT_ID));
				}
				if (rebatesObjectInfo.containsKey(Context.REBATES_OBJECT_ACCOUNT_TYPE)){// 账号类型
					rebatesObjectInfo4Review.put(Context.REBATES_OBJECT_ACCOUNT_TYPE,rebatesObjectInfo.get(Context.REBATES_OBJECT_ACCOUNT_TYPE));
				}
				if (rebatesObjectInfo.containsKey(Context.REBATES_OBJECT_ACCOUNT_BANK)){// 开户行名称
					rebatesObjectInfo4Review.put(Context.REBATES_OBJECT_ACCOUNT_BANK,rebatesObjectInfo.get(Context.REBATES_OBJECT_ACCOUNT_BANK));
				}
				if (rebatesObjectInfo.containsKey(Context.REBATES_OBJECT_ACCOUNT_CODE)){// 账户号码
					rebatesObjectInfo4Review.put(Context.REBATES_OBJECT_ACCOUNT_CODE,rebatesObjectInfo.get(Context.REBATES_OBJECT_ACCOUNT_CODE));
				}

			}else{//默认为渠道
				rebatesObjectInfo4Review.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_TYPE,Rebatedable.REBATEDABLE_TYPE_AGENT.toString());//审批相关对象类型
				if(orderInfo.containsKey(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT)){
					rebatesObjectInfo4Review.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT,orderInfo.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT));//审批相关对象id
				}
				if (orderInfo.containsKey(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME)){
					rebatesObjectInfo4Review.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_NAME,orderInfo.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME));//审批相关对象名称
				}
			}

			for (RebatesNew rebates : rebatesList) {

				//获取改后返佣金额
				if (StringUtils.isNotBlank(rebates.getNewRebates())) {
					JSONArray newRebatesArr = JSONArray.fromObject(rebates.getNewRebates());
					List<MoneyAmount> moneyList = Lists.newArrayList();
					MoneyAmount moneyAmount = null;
					String newRebatesSerialNum = UUID.randomUUID().toString();
					for (int i = 0; i < newRebatesArr.size(); i++) {
						JSONObject rebatesJSONObj = newRebatesArr.getJSONObject(i);
						Integer currencyId = rebatesJSONObj.getInt("currencyId");
						BigDecimal amount = new BigDecimal(rebatesJSONObj.getString("amount"));
						moneyAmount = new MoneyAmount(newRebatesSerialNum,currencyId,amount,rebates.getTraveler() != null ? rebates.getTraveler().getId() : rebates.getOrderId(), Context.MONEY_TYPE_FY, null, 2, UserUtils.getUser().getId());
						moneyList.add(moneyAmount);
					}
					moneyAmountService.saveMoneyAmounts(moneyList);
					rebates.setNewRebates(newRebatesSerialNum);
				}
				//获取团队返佣原返佣金额 每次获取都是前一次团队返佣的金额合计
				if (rebates.getTraveler() == null) {
					List<Object[]> teamList = getTeamRebates(rebates.getOrderId());
					List<MoneyAmount> moneyList = Lists.newArrayList();
					MoneyAmount moneyAmount = null;
					String oldRebatesSerialNum = UUID.randomUUID().toString();
					for (Object[] money: teamList) {
						moneyAmount = new MoneyAmount(oldRebatesSerialNum, Integer.parseInt(money[0].toString()), new BigDecimal(money[2].toString()), rebates.getOrderId(), Context.MONEY_TYPE_FY, null, 1, UserUtils.getUser().getId());
						moneyList.add(moneyAmount);
					}
					moneyAmountService.saveMoneyAmounts(moneyList);
					rebates.setOldRebates(oldRebatesSerialNum);
				}
				// 获取个人返佣原返佣金额 每次获取都是之前个人返佣的金额合计
				else {
					// 计算原返佣金额
					Long orderId = rebates.getOrderId();
					Long travelerId = rebates.getTraveler().getId();
					// 查询到指定订单指定游客的全部历史返佣记录
					List<RebatesNew> list = rebatesNewDao.findRebatesByTravelerAndStatus(travelerId, orderId);
					List<MoneyAmount> moneyList = Lists.newArrayList();
					MoneyAmount moneyAmount = null;
					String oldRebatesSerialNum = UUID.randomUUID().toString();
					// 将该游客本次返佣之前的返佣差额全部查出，进行累加，其值便是当前返佣操作的原返佣金额
					for (RebatesNew rebateTemp : list) {
						moneyAmount = new MoneyAmount(oldRebatesSerialNum, Integer.valueOf(rebateTemp.getCurrencyId().toString()), rebateTemp.getRebatesDiff(), rebates.getTravelerId(), Context.MONEY_TYPE_FY, null, 1, UserUtils.getUser().getId());
						moneyList.add(moneyAmount);
					}

					// 原返佣值变为累计值
					moneyAmountService.saveNewMoneyAmounts(moneyList);
					rebates.setOldRebates(oldRebatesSerialNum);
					// 返佣应收金额
	//				rebates.setTotalMoney(moneyAmountService.getMoneyStr(rebates.getTraveler().getPayPriceSerialNum()));
				}
				Currency currency = currencyService.findCurrency(rebates.getCurrencyId());
				rebates.setCurrency(currency);
				rebates.setCurrencyExchangerate(currency.getCurrencyExchangerate());

				//新建审核记录的 deptId 来自产品的部门编号
				Long ProductId = productOrder.getProductId();
				Long deptId = travelActivityService.findById(ProductId).getDeptId();

				boolean yubao_locked = false; //预报单是否锁定标识
				boolean jiesuan_locked = false; //结算单是否锁定标识
				//对预报单状态进行判断
				if ("10".equals(group.getForcastStatus())) {
					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == group.getLockStatus()) {
					yubao_locked = true;
					return "结算单已锁定，不能发起申请";
				}

				//调用申请接口
				String userId = UserUtils.getUser().getId().toString();
				String companyId = UserUtils.getUser().getCompany().getUuid();
				// 组织 review 数据
				Map<String, Object> variables = new HashMap<String, Object>();

				/** 通用参数赋值 */

				// 流程类型
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REBATES_FLOW_TYPE);
				// 产品类型
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, product.getActivityKind());
				// 部门id
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, product.getDeptId());
				// 返佣差额
				//千位符
				DecimalFormat d = new DecimalFormat(",##0.00");
				String markTotalMoney = d.format(rebates.getRebatesDiff());
				variables.put(Context.REVIEW_VARIABLE_KEY_REBATES_MARK_TOTAL_MONEY,
						currencyService.findCurrency(rebates.getCurrencyId()).getCurrencyMark() + " " + markTotalMoney);
				//如果不是团队返佣则需要保存游客ID和名称
				if (rebates.getTraveler() != null) {
					// 游客id
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, rebates.getTraveler().getId());
					// 游客名称
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, rebates.getTraveler().getName());
				}
				variables.putAll(orderInfo);
				variables.putAll(productInfo);

				/**
				 * 参数中添加返佣相关信息
				 */
				variables.putAll(rebatesObjectInfo4Review);

				/** 自定义参数赋值 */
				variables.put(Context.REVIEW_VARIABLE_KEY_REBATES_COST_NAME, rebates.getCostname());

				ReviewResult reviewResult = reviewService.start(userId, companyId, permissionChecker, null, productOrder.getOrderStatus(),
						Context.REBATES_FLOW_TYPE, deptId, "", variables);
				//如果返佣申请成功
				if (reviewResult.getSuccess()) {
					ReviewNew reviewInfo = reviewService.getReview(reviewResult.getReviewId());
					rebates.setReview(reviewInfo);
					rebatesNewDao.save(rebates);
					// 返佣发起申请之后对成本进行同步插入
					costManageService.saveRebatesCostRecordNew(reviewInfo, rebates, productOrder, yubao_locked, jiesuan_locked);
				} else {
					if (StringUtils.isNotBlank(reviewResult.getMessage())) {
						return reviewResult.getMessage();
					} else {
						return "error";
					}
				}
			}
		}
		return "success";
	}
	
	
	
	/**
	 * 改变审核没有通过的累计金额值
	 */
	
	public void reviewNotInStutus2(String orderId,String rid){
		
		List<String> notStatus2AllCumulative = getNotStatus2AllCumulative(orderId);
		if(null!=notStatus2AllCumulative && notStatus2AllCumulative.size()>0){
			for(String cumulative : notStatus2AllCumulative){
				String sql = "update rebates set allCumulative = ? where orderId = ? and orderType = 7";
				rebatesNewDao.updateBySql(sql, cumulative,orderId);
			}
		}
		
	}
	
	/**
	 * 返回最后通过的累计总金额
	 * @param orderId
	 * @return
	 */
	public List<String> getNotStatus2AllCumulative(String orderId){
		String sql = "select rebates.allCumulative from review_new  as review,rebates as rebates  where review.STATUS=2 and review.product_type = '7'  and review.process_type = '9' and review.id=rebates.rid  AND review.order_id = rebates.orderId AND rebates.orderId =? limit 1";
		return rebatesNewDao.findBySql(sql,orderId);
	} 
	

	/**
	 * 返佣审核通过操作
	 */
	@Transient
	public void reviewSuccess(Map<String,String> reviewMap) {
		
		//now_cumulative 
		//all_cumulative
		
		//获取订单最新已通过流程的总差额
		List<Object[]> newtotalList = getnewTeamRebatesList(Long.parseLong(reviewMap.get("orderId").toString()));
		List<Object[]> oldtotalList = getoldTeamRebatesList(Long.parseLong(reviewMap.get("orderId").toString()));
		List<Object[]> onetotalList = getoneTeamRebatesList(Long.parseLong(reviewMap.get("orderId").toString()));
		
		String newtotal = "";
		String oldtotal = "";
		
		if(oldtotalList!= null && oldtotalList.size()>0 && null!=oldtotalList.get(0)[1]){
			for(Object[] rebates : oldtotalList){
				if(("").equals(rebates[1].toString()) ){
					for(Object[] o1 : onetotalList){
						oldtotal = o1[0].toString();
					}
				}else{
					//oldtotal = rebates[1].toString();
					oldtotal = rebates[1].toString();
				}
			}
		}
		
		for (Object[] rebates : newtotalList ) {
			
			if(!("").equals(oldtotal)){
				
				newtotal = oldtotal + "+" + rebates[1].toString();
			
			}else{
				
				newtotal =  rebates[1].toString();
				
			}
			
		}
		
		List<RebatesNew> rebatesList =  rebatesNewDao.findOneByRidList(reviewMap.get("id"));
	
		for (RebatesNew rebates : rebatesList) {
			//更新所有订单累计的总金额
			String sql = "update   order_rebates   set all_cumulative = ? where orderId = ? and order_type = 7";
			rebatesNewDao.updateBySql(sql, newtotal,rebates.getOrderId());
			
		}
		
	}
	
	public RebatesNew findRebatesById(Long id){
		return rebatesNewDao.findOne(id);
	}
	
	public RebatesNew findRebatesByRid(String id){
		return rebatesNewDao.findOneByRid(id);
	}
	
	public List<RebatesNew> findRebatesListByRid(String id){
		return rebatesNewDao.findListByRid(id);
	}
	
	/**
	 * @Description 根据订单id和订单类型查询返佣记录
	 * @author yakun.bai
	 * @Date 2015-11-27
	 */
	public List<RebatesNew> findRebatesList(Long orderId, int orderType){
		return rebatesNewDao.findRebatesList(orderId, orderType);
	}
	
	public List<RebatesNew> findRebatesListAir(Long orderId, int orderType){
		return rebatesNewDao.findRebatesListAir(orderId, orderType);
	}
	
	public List<RebatesNew> findTravelerRebatesList(Long orderId){
		return rebatesNewDao.findTravelerRebatesListByOrderId(orderId);
	}
	
	public List<RebatesNew> findRebatesListByStatus(String productType, String flowType, String orderId){
		return rebatesNewDao.findRebatesListByStatus(productType, flowType, orderId);
	}
	
	public List<?> findVisaRebatesListByStatus(Integer productType, Integer flowType, String orderId)
	{
		StringBuffer sbf = new StringBuffer();
		sbf.append("select id from review where productType = ? and flowType=? and orderId=? and active=1 and status = 1");
		return rebatesNewDao.findBySql(sbf.toString(), Map.class, productType,flowType,orderId);
	}
	
	
	public Page<Map<Object, Object>> getRebatesReviewList(Page<Map<Object, Object>>page, RebatesReviewCond vo){
		if (StringUtils.isBlank(page.getOrderBy())){
			page.setOrderBy(" a.id DESC ");
		}
		StringBuffer sbf = new StringBuffer();
		List<Object> paramList = new ArrayList<Object>();
		sbf.append(" SELECT " +
					"a.id,"+
					"a.rid,"+
					"ag.id groupid," +
					"ag.groupCode," +
					"ag.createBy meter, " +
					"ta.id acitivityId,"+
					"ta.acitivityName," +
					"p.orderStatus," +
					"p.orderCompanyName," +
					"a.costname," +
					"p.createBy saler," +
					"a.currencyId,"+
					"c.currency_mark currencyMark,"+
					"a.rebatesDiff," +
					"r1.createBy beforeReview," +
					"IFNULL(r2.result, '未审核') result,"+
					"r.`status`  ")
		.append(" FROM order_rebates a ") 
		.append(" INNER JOIN productorder p on a.orderId = p.id ")
		.append(" and a.delFlag="+Context.DEL_FLAG_TEMP+" ");
		// 添加页面条件查询控制(销售、计调、日期等)
		if(StringUtils.isNotBlank(vo.getChannel())){
			sbf.append(" and p.orderCompany=? ");
			paramList.add(vo.getChannel());
		}
		if(StringUtils.isNotBlank(vo.getOrderType())){
			sbf.append(" and p.orderStatus=? ");
			paramList.add(vo.getOrderType());
		}
		if(StringUtils.isNotBlank(vo.getSaler())){
			sbf.append(" and p.createBy=? ");
			paramList.add(vo.getSaler());
		}
		if(StringUtils.isNotBlank(vo.getOrderTimeBegin())){
			sbf.append(" and p.orderTime>=? ");
			paramList.add(vo.getOrderTimeBegin() + " 00:00:00");
		}
		if(StringUtils.isNotBlank(vo.getOrderTimeEnd())){
			sbf.append(" and p.orderTime<=? ");
			paramList.add(vo.getOrderTimeEnd() + " 23:59:59");
		}
		sbf.append(" INNER JOIN travelactivity ta on ta.id = p.productId ");
		if(StringUtils.isNotBlank(vo.getMeter())){
			sbf.append(" and ta.createBy=? ");
			paramList.add(vo.getMeter());
		}
		//添加部门过滤(暂支持一个角色对应一个部门)
		Role r = systemService.getRole(vo.getRid());
		if(null != r && r.getId() > 0){
			sbf.append(" and ta.deptId =? ");
			
			paramList.add(r.getDepartment().getId().intValue());
		}		
		sbf.append("INNER JOIN activitygroup ag on ag.id = p.productGroupId ");
		if(StringUtils.isNotBlank(vo.getGroupCode())){
			sbf.append(" and ag.groupCode like'%" + vo.getGroupCode()+"%' ");
		}
		sbf.append(" LEFT JOIN agentinfo e on e.id = p.orderCompany ")
		.append(" INNER JOIN review r on r.id = a.rid ")
		.append(" INNER JOIN sys_user sys on r.createby = sys.id ")
		.append(" and sys.companyId = ?")
		// 添加审核查询控制(全部、未审核、已驳回、已通过)
		.append(ReviewCommonUtil.getReviewCheckSql(vo.getReviewStatus(),vo.getUserLevel()))
		.append(" LEFT JOIN review_log r1 on r1.rid = a.rid and r1.nowLevel=").append(vo.getUserLevel() - 1)
		.append(" LEFT JOIN review_log r2 on r2.rid = a.rid and r2.nowLevel=").append(vo.getUserLevel())
		.append(" LEFT JOIN currency c on c.currency_id = a.currencyId ");
		paramList.add(UserUtils.getUser().getCompany().getId());
		Page<Map<Object, Object>> pageInfo;
		if(paramList.size() > 0){
			pageInfo= rebatesNewDao.findPageBySql(page,sbf.toString(), Map.class, paramList.toArray());
		}else{
			pageInfo= rebatesNewDao.findPageBySql(page,sbf.toString(), Map.class);
		}
		List<Map<Object,Object>>list = pageInfo.getList();
		if(list.size() > 0){
			for(Map<Object,Object> map: list){
				String result = map.get("result").toString();
				Integer myStatus = 0;
				//判断前台返回针对于当前审核层级的审核状态
				if(StringUtils.isNotBlank(result)){
					switch(result){
						case "驳回":
							myStatus = 0;
							break;
						case "未审核":
							myStatus = 5;
							break;
						case "审核通过":
							myStatus = 2;
							break;
						case "操作完成":
							myStatus = 3;
							break;	
						case "已取消":
							myStatus = 4;
							break;	
						default:
							break;
					}
					map.put("myStatus", myStatus);
				}
			}
		}
		return pageInfo;
	}
	
	public List<ReviewLog> validReviewRebates(long rid, Integer userLevel){
		return reviewLogDao.findReviewLogByNowLevel(rid, userLevel);
	}
	
	/**
	 * @Description 查询审核通过的返佣记录
	 * @author yakun.bai
	 * @Date 2015-12-1
	 */
	public List<RebatesNew> findRebatesByTravelerAndStatus(Long travelerId){
		return rebatesNewDao.findRebatesByTravelerAndStatus(travelerId);
	}
	
	/**
	 * 根据订单判断是否处于返佣流程中
	 * @param orderId
	 * @return true表示不在返佣流程中可以申请其他相关流程
	 */
	public boolean findRebatesProcessStateByOrderId(Long orderId){
		List<RebatesNew> rebatesList = rebatesNewDao.findRebatesProcessStateByOrderId(orderId);
		if(rebatesList.size() > 0){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * @Description 根据订单获取原团队返佣费用
	 * @author yakun.bai
	 * @Date 2015-11-27
	 */
	public List<Object[]> getTeamRebates(Long orderId) {
		String sql = "SELECT m.currencyId, c.currency_name, sum(m.amount), c.currency_mark " +
					 "FROM money_amount m, currency c, (" +
					 	"SELECT a.newRebates AS serialNum " +
					 	"FROM rebates a " +
					 	"LEFT JOIN review_new b " +
					 	"ON a.rid = b.id " +
					 	"WHERE a.orderId = " + orderId + " " +
					 			"AND b.status = " + ReviewConstant.REVIEW_STATUS_PASSED + " ) tmp " +
					 "WHERE m.currencyId = c.currency_id AND m.serialNum = tmp.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
		return rebatesNewDao.findBySql(sql);
	}
	/**
	 * 根据订单获取原团队返佣费用
	 * @param orderId 订单主键
	 * @return 
	 */
	public List<Object[]> getIslandTeamRebates(Long orderId){
		String sql = "SELECT m.currencyId, c.currency_name, sum(m.amount), c.currency_mark " +
				 "FROM island_money_amount m,currency c,(" +
				 	"SELECT a.new_rebates AS serialNum " +
				 	"FROM order_rebates a " +
				 	"LEFT JOIN review b " +
				 	"ON a.rid = b.id " +
				 	"WHERE a.orderId = " + orderId + " AND a.travelerId is null  AND b.status = 2 AND a.delFlag="+Context.DEL_FLAG_TEMP+")tmp " +
				 "WHERE m.currencyId = c.currency_id AND m.serialNum = tmp.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
	return rebatesNewDao.findBySql(sql);
	}
	/**
	 * 根据订单获取原团队返佣费用
	 * @param orderId 订单主键
	 * @return 
	 */
	public List<Object[]> getHotelTeamRebates(Long orderId){
		String sql = "SELECT m.currencyId, c.currency_name, sum(m.amount), c.currency_mark " +
				 "FROM hotel_money_amount m,currency c,(" +
				 	"SELECT a.new_rebates AS serialNum " +
				 	"FROM order_rebates a " +
				 	"LEFT JOIN review b " +
				 	"ON a.rid = b.id " +
				 	"WHERE a.orderId = " + orderId + " AND a.travelerId is null  AND b.status = 2 AND a.delFlag="+Context.DEL_FLAG_TEMP+")tmp " +
				 "WHERE m.currencyId = c.currency_id AND m.serialNum = tmp.serialNum GROUP BY m.currencyId ORDER BY m.currencyId";
	return rebatesNewDao.findBySql(sql);
	}
	
	/**
	 * （新）机票返佣获取返佣费用
	 * @param orderId
	 * @return
	 */
	public List<RebatesNew> getRebates(Long orderId){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  w.currencyId AS currencyId,w.old_rebates AS old_rebates,w.rebatesDiff AS rebatesDiff,w.new_rebates AS new_rebates,w.travelerId AS travelerId");
		sql.append(" FROM (SELECT  o.currencyId AS currencyId,o.oldRebates AS old_rebates,o.rebatesDiff AS rebatesDiff,o.newRebates AS new_rebates,o.travelerId AS travelerId");
		sql.append(" FROM rebates o   LEFT JOIN review_new r ON o.rid = r.id  WHERE o.orderid = ?  AND  r.status = 2 AND r.process_type = '9' AND r.product_type = '7' AND o.delFlag = 0  ORDER BY o.id DESC) w");
		List<Map<String,Object>> mapList = rebatesNewDao.findBySql(sql.toString(),Map.class,orderId);
		List<RebatesNew> result = new ArrayList<RebatesNew>();
		for(Map<String,Object> map:mapList){
			RebatesNew rb = new RebatesNew();
			rb.setCurrencyId(Long.parseLong(map.get("currencyId").toString()));
			rb.setOldRebates(map.get("old_rebates").toString());
			rb.setRebatesDiff(new BigDecimal(map.get("rebatesDiff").toString()));
			rb.setNewRebates(map.get("new_rebates").toString());
			if(map.get("travelerId")==null){
				rb.setTravelerId(0l);
			}else{
				rb.setTravelerId(Long.parseLong(map.get("travelerId").toString()));
			}
			result.add(rb);
		}
		return result;
	} 
	
	/***
	 * （新）机票返佣审核申请
	 * @param rebatesList
	 * @return
	 */
	@Transactional
	public String airticketReabesSave(List<RebatesNew> rebatesList,String rebatesTotal,String travelerIds,String supplyId,String supplyName,String accountId){
		Long theOrderID = null;
		if(CollectionUtils.isNotEmpty(rebatesList)){
			theOrderID=rebatesList.get(0).getOrderId();
		}else{
			return null;
		}
		AirticketOrder productOrder = airTicketOrderService.getAirticketorderById(theOrderID);//机票订单
		Long ProductId =productOrder.getAirticketId();
		ActivityAirTicket activityAirTicket =  activityAirTicketService.getActivityAirTicketById(ProductId);//订单对应的机票产品
		Long deptId = activityAirTicket.getDeptId();
		//调用申请接口
		User user = UserUtils.getUser();
		String userId = user.getId().toString();
		String companyUuid = user.getCompany().getUuid();
		//组织数据
		Map<String, Object> variables = new HashMap<String, Object>();
		String travellerId = "0";//团队申请时游客ID置为0
		boolean isGroup = true;
		if(rebatesList.size()==1&&null!=rebatesList.get(0).getTraveler()&&null!=rebatesList.get(0).getTraveler().getId()){//单个游客的申请
			travellerId = rebatesList.get(0).getTraveler().getId().toString();
			isGroup = false;
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travellerId);
	    	variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, rebatesList.get(0).getTraveler().getName());
		}else {
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, 0);//团队的返佣
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, "团队");
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, travelerIds);
		}
		//获取渠道ID
		Long agentId = productOrder.getAgentinfoId();
		String agentName;
 		/** 通用参数赋值 */
		// 流程类型
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REBATES_FLOW_TYPE);
		// 产品类型
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, ProductType.PRODUCT_AIR_TICKET);
		// 部门id
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
		//累计返佣金额显示使用
		variables.put(Context.REVIEW_VARIABLE_KEY_REBATES_MARK_TOTAL_MONEY, rebatesTotal);
		//渠道名称
		if(agentId!=-1){
			//根据渠道ID获取渠道信息
			Agentinfo agentInfo = agentinfoService.loadAgentInfoById(agentId);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, agentInfo.getAgentName());
			agentName = agentInfo.getAgentName();
		}else{
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, productOrder.getNagentName());
			agentName = productOrder.getNagentName();
		}
		//返佣对象相关的数据
		if(user.getCompany().getIsAllowMultiRebateObject()==1){
			if(StringUtils.isNotBlank(supplyId)&&!"请选择".equals(supplyId)){
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT, supplyId);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_NAME, supplyName);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_TYPE, "2");//审批相关对象类型(1渠道；2供应商)
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_2, accountId);// 账号id
			}else{
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT, agentId);//返佣对象为渠道
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_NAME, agentName);
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_RELATED_OBJECT_TYPE, "1");//审批相关对象类型(1渠道；2供应）
			}
		}
		//
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, agentId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, theOrderID);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,ProductId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK,rebatesList.get(0).getRemark());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER,userId);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME,user.getName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, productOrder.getOrderNo());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityAirTicket.getGroupCode());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, activityAirTicket.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, productOrder.getCreateBy().getId());
		//流程互斥检查
		CommonResult  commonResult;
		String[] travelerIdArray = travelerIds.split(",");
		if(travelerIdArray.length>1){
			commonResult =  reviewMutexService.check(theOrderID.toString(),Arrays.asList(travelerIdArray),Context.ORDER_STATUS_AIR_TICKET,Context.REBATES_FLOW_TYPE.toString());
		}else{
			commonResult =  reviewMutexService.check(theOrderID.toString(), travellerId, Context.ORDER_STATUS_AIR_TICKET, Context.REBATES_FLOW_TYPE.toString(), isGroup);
		}
		if(!commonResult.getSuccess()){
			return  commonResult.getMessage();
		}
		ReviewResult reviewResult = reviewService.start(userId, companyUuid, permissionChecker, "bates_airticket", 7,Context.REBATES_FLOW_TYPE, activityAirTicket.getDeptId(), "", variables);
		//如果返佣申请未成功
		if (!reviewResult.getSuccess()) {
			return ReviewErrorCode.ERROR_CODE_MSG_CN.get(reviewResult.getCode());
		}
		//
		String ruuid = reviewResult.getReviewId();
		//当前订单所有游客的返佣最新记录 
		List<RebatesNew> teamList = this.getRebates(theOrderID);
		Map<Long,RebatesNew> rebatesMap = new HashMap<Long, RebatesNew>();
		for(RebatesNew rb :teamList){	
			rebatesMap.put(rb.getTravelerId(), rb);
		}
		for(RebatesNew rebates : rebatesList){
			//累计返佣金额
			String oldRebatesSerialNum="";
			String newRebatesSerialNum="";
			if(rebates.getTraveler()==null||rebates.getTraveler().getId()==null){
				rebates.setTravelerId(0l);
			}else{
				rebates.setTravelerId(rebates.getTraveler().getId());
			}
			//如果没有返佣记录的情况 
			if(!rebatesMap.containsKey(rebates.getTravelerId())||rebates.getTravelerId()==0){
				newRebatesSerialNum=UUID.randomUUID().toString();
				MoneyAmount moneyAmount = new MoneyAmount();
				moneyAmount.setSerialNum(newRebatesSerialNum);
				moneyAmount.setCurrencyId(Integer.parseInt(rebates.getCurrencyId().toString()));
				moneyAmount.setAmount(new BigDecimal(rebates.getRebatesDiff().toString()));
				//根据币种ID获取汇率
				Currency currency = currencyService.findCurrency(Long.parseLong(rebates.getCurrencyId().toString()));
				moneyAmount.setExchangerate(currency.getCurrencyExchangerate());
				moneyAmount.setUid(rebates.getTravelerId());
				moneyAmount.setMoneyType(Context.MONEY_TYPE_FY);
				moneyAmount.setOrderType(7);
				moneyAmount.setBusindessType(1);
				moneyAmount.setCreatedBy(UserUtils.getUser().getId());
				moneyAmount.setReviewUuid(ruuid);
				moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
			}else{
				oldRebatesSerialNum = rebatesMap.get(rebates.getTravelerId()).getNewRebates();
				newRebatesSerialNum=UUID.randomUUID().toString();
				
				List<MoneyAmount> payedMoney = moneyAmountDao.findAmountBySerialNum(rebatesMap.get(rebates.getTravelerId()).getNewRebates());
				Map<Integer,MoneyAmount> moneyMap = new HashMap<Integer,MoneyAmount>();
				List<MoneyAmount> newMoneyList = new ArrayList<MoneyAmount>();
				for( MoneyAmount ma:payedMoney){
					
					MoneyAmount moneyAmount = new MoneyAmount();
					moneyAmount.setCurrencyId(ma.getCurrencyId());
					moneyAmount.setAmount(ma.getAmount());
					moneyAmount.setExchangerate(ma.getExchangerate());
					moneyAmount.setUid(ma.getUid());
					moneyAmount.setMoneyType(ma.getMoneyType());
					moneyAmount.setOrderType(ma.getOrderType());
					moneyAmount.setBusindessType(ma.getBusindessType());
					moneyAmount.setCreatedBy(ma.getCreatedBy());
					moneyAmount.setReviewId(ma.getReviewId());
					newMoneyList.add(moneyAmount);
					
					moneyMap.put(ma.getCurrencyId(), ma);
				}
				if(!moneyMap.containsKey(Integer.parseInt(rebates.getCurrencyId().toString()))){
				
					MoneyAmount moneyAmount = new MoneyAmount();
					moneyAmount.setSerialNum(newRebatesSerialNum);
					moneyAmount.setCurrencyId(Integer.parseInt(rebates.getCurrencyId().toString()));
					moneyAmount.setAmount(new BigDecimal("0"));
					//根据币种ID获取汇率
					Currency currency = currencyService.findCurrency(Long.parseLong(rebates.getCurrencyId().toString()));
					moneyAmount.setExchangerate(currency.getCurrencyExchangerate());
					moneyAmount.setUid(rebates.getTravelerId());
					moneyAmount.setMoneyType(Context.MONEY_TYPE_FY);
					moneyAmount.setOrderType(7);
					moneyAmount.setBusindessType(1);
					moneyAmount.setCreatedBy(UserUtils.getUser().getId());
					moneyAmount.setReviewUuid(ruuid);
					newMoneyList.add(moneyAmount);
				}
				for(MoneyAmount moneyAmount : newMoneyList ){
					if(Long.parseLong(moneyAmount.getCurrencyId().toString())==rebates.getCurrencyId()){
						BigDecimal rnewm= rebates.getRebatesDiff();
						BigDecimal roldm= moneyAmount.getAmount();
						BigDecimal val= rnewm.add(roldm);
						moneyAmount.setAmount(val);
						
					}
					moneyAmount.setSerialNum(newRebatesSerialNum);
					moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
				}
			}
			rebates.setOldRebates(oldRebatesSerialNum);
			rebates.setNewRebates(newRebatesSerialNum);
			rebates.setCurrency(currencyService.findCurrency(rebates.getCurrencyId()));
			boolean yubao_locked = false; //预报单是否锁定标识
			boolean jiesuan_locked = false; //结算单是否锁定标识
			//对结算单状态进行判断
			if (1 == activityAirTicket.getLockStatus()) {
				jiesuan_locked = true;
				return "结算单已锁定，不能发起申请";
			}
			//对预报单状态进行判断
			if ("10".equals(activityAirTicket.getForcastStatus())) {
				yubao_locked = true;
			}
			ReviewNew review = reviewService.getReview(reviewResult.getReviewId());
			rebates.setReview(review);
			rebatesNewDao.save(rebates);
			costManageService.saveRebatesCostRecordNew(review, rebates, productOrder, yubao_locked, jiesuan_locked);
			this.reviewNotInStutus2(rebates.getOrderId().toString(), ruuid);
		}
		return "success";
	}
	
	/**
	 * 机票返佣根据订单获取最新已通过流程的总差额
	 * @param orderId
	 * @return
	 */
	public List<Object[]> getnewTeamRebatesList(Long orderId){
//		String sql = "select order_rebates.id,order_rebates.now_cumulative,order_rebates.all_cumulative from order_rebates as order_rebates where rid in (select max(r.id) from order_rebates as o,  review as r  where o.orderId = r.orderId  and r.`status`=2 and o.orderId = ? and o.delFlag="+Context.DEL_FLAG_TEMP+") and order_rebates.delFlag="+Context.DEL_FLAG_TEMP;
		String sql = "select order_rebates.id,order_rebates.now_cumulative,order_rebates.all_cumulative from order_rebates as order_rebates where rid in (select max(r.id) from order_rebates as o,  review as r  where o.orderId = r.orderId  and r.`status`=2 and o.orderId = ? )";
		return rebatesNewDao.findBySql(sql,orderId);
	} 
	
	/**
	 * 机票返佣根据订单获取最新流程的总差额
	 * @param orderId
	 * @return
	 */
	public List<Object[]> getoldTeamRebatesList(Long orderId){
//		String sql = "SELECT order_rebates.now_cumulative, 	order_rebates.all_cumulative,review.id FROM 	order_rebates AS order_rebates ,review as review WHERE review.id = order_rebates.rid and 	rid IN ( 		SELECT 			r.id 		FROM 			order_rebates AS o, 			review AS r 		WHERE 			o.orderId = r.orderId 		AND r.`status` = 2 		AND o.orderId = ? 	AND o.delFlag="+Context.DEL_FLAG_TEMP+") order_rebates.delFlag="+Context.DEL_FLAG_TEMP+" GROUP BY review.id  ORDER BY review.id DESC limit 1,1 ";
		String sql = "SELECT order_rebates.now_cumulative, 	order_rebates.all_cumulative,review.id FROM 	order_rebates AS order_rebates ,review as review WHERE review.id = order_rebates.rid and 	rid IN ( 		SELECT 			r.id 		FROM 			order_rebates AS o, 			review AS r 		WHERE 			o.orderId = r.orderId 		AND r.status = 2 		AND o.orderId = ? 	)  GROUP BY review.id  ORDER BY review.id DESC limit 1,1 ";
		return rebatesNewDao.findBySql(sql,orderId);
	} 
	
	
	/**
	 * 机票返佣根据订单获取最新流程的总差额
	 * @param orderId
	 * @return
	 */
	public List<Object[]> getoneTeamRebatesList(Long orderId){
//		String sql  ="SELECT 	order_rebates.now_cumulative, 	order_rebates.all_cumulative, 	review.id FROM 	order_rebates AS order_rebates, 	review AS review WHERE 	review.id = order_rebates.rid AND rid IN ( 	SELECT 		r.id 	FROM 		order_rebates AS o, 		review AS r 	WHERE 		o.orderId = r.orderId 	AND r.`status` = 2 	AND o.orderId = ? o.delFlag="+Context.DEL_FLAG_TEMP+" ORDER BY r.id DESC ) order_rebates.delFlag="+Context.DEL_FLAG_TEMP+" GROUP BY 	review.id limit 1";
		String sql  ="SELECT 	order_rebates.now_cumulative, 	order_rebates.all_cumulative, 	review.id FROM 	order_rebates AS order_rebates, 	review AS review WHERE 	review.id = order_rebates.rid AND rid IN ( 	SELECT 		r.id 	FROM 		order_rebates AS o, 		review AS r 	WHERE 		o.orderId = r.orderId 	AND r.`status` = 2 	AND o.orderId = ?  ORDER BY r.id DESC ) GROUP BY 	review.id limit 1";
		return rebatesNewDao.findBySql(sql,orderId);
	} 
	
	
	public File createRebatesSheetDownloadFile(Map<String,Object> map) throws Exception {
		
		return FreeMarkerUtil.generateFile("rebatesPay.ftl", "rebatesPay.doc", map);
	}
	
	/**
	 * 20150716 获取报名时填写的团队返佣参考值
	 * @author gao
	 * @param orderId
	 * @return
	 */
	public List<RebatesNew> getGroupReList(Long orderId){
		List<RebatesNew> reList = rebatesNewDao.findListByOrderId(orderId);
		return reList;
	}
	
	public List<String> getNewRebatesList(Long orderId ,String travelerId){
		String sql = "SELECT re.newRebates FROM  rebates re LEFT JOIN review_new rn  ON re.rid = rn.id WHERE rn.status=2 AND re.orderId = ? AND re.travelerId = ? AND re.delFlag = 0";
		List<String> reList = rebatesNewDao.findBySql(sql,orderId,travelerId);
		return reList;
	}
	
	public List<Map<String,Object>> getSupplyInfoList(){
		String sqlString = "SELECT name name,id id FROM rebate_supplier rs where rs.del_flag = 0 AND rs.company_id = "+UserUtils.getUser().getCompany().getId()+" AND rs.operator_id = "+UserUtils.getUser().getId();
		List<Map<String,Object>> reList = rebatesNewDao.findBySql(sqlString, Map.class);
		return reList;
	}
	
	public List<Map<String,Object>> getAccountNameList(String supplyId ,String accountType){
		String sqlString = "SELECT bankName name,id id FROM plat_bank_info WHERE platType=3 AND beLongPlatId=? AND belongType = ?";
		List<Map<String,Object>> reList = rebatesNewDao.findBySql(sqlString, Map.class ,supplyId ,accountType);
		return reList;
	}
	
	public List<Map<String,Object>> getAccountNoList(String supplyId ,String accountType ,String accountName){
		String sqlString = "SELECT bankAccountCode no,id id FROM plat_bank_info WHERE platType=3 AND beLongPlatId=? AND belongType = ? AND bankName = ?";
		List<Map<String,Object>> reList = rebatesNewDao.findBySql(sqlString, Map.class ,supplyId ,accountType ,accountName);
		return reList;
	}
	
	public List<Map<String,Object>> getRebateSupplier(String accountId){
		String sqlString = "SELECT   pbi.bankName,pbi.bankAccountCode,case when  pbi.belongType=1 then '境内账户' ELSE '境外账户' END AS platType,pbi.id,pbi.beLongPlatId,rs.name  FROM plat_bank_info pbi LEFT JOIN rebate_supplier rs ON pbi.beLongPlatId = rs.id WHERE pbi.id = ?";
		List<Map<String,Object>> reList = rebatesNewDao.findBySql(sqlString, Map.class ,accountId);
		return reList;
	}

	public List<Map<String, Object>> getPayedMoney(String reviewUuid) {
		String sql = "select r.currencyId, SUM(r.rebatesDiff) rebatesDiff, currencyExchangerate rate from rebates r where r.rid = '" + reviewUuid + "' group by r.currencyId";
		List<Map<String, Object>> list = rebatesNewDao.findBySql(sql, Map.class);
		return list;
	}

	public void updateRebateRate(String reviewUuid, String rate, String price, Long rebatesId) {
		//更新rebates表中汇率
		String sql = "UPDATE rebates SET currencyExchangerate = ? WHERE rid = ? and id = ?";
		rebatesNewDao.updateBySql(sql, rate, reviewUuid, rebatesId);
		//更新cost_record表中汇率
		sql = "update cost_record set price = ?, priceAfter = ? where reviewUuid = ? and rebatesId = ? ";
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())){
			////预算的返佣不修改汇率，0448需求，团队管理那应付总额取基础信息表的汇率
			sql += " and budgetType = 1 ";
		}
		rebatesNewDao.updateBySql(sql, price, price, reviewUuid, rebatesId);
	}

//	public void updateRebateRate(String reviewUuid, String rate, String price, BigDecimal bd, BigDecimal amount, BigDecimal mrate) {
//		//更新rebates表中汇率
//		String sql = "UPDATE rebates SET currencyExchangerate = ? WHERE rid = ? and rebatesDiff = ? and currencyExchangerate = ?";
//		rebatesNewDao.updateBySql(sql, rate, reviewUuid, amount, mrate);
//		//更新cost_record表中汇率
//		sql = "update cost_record set price = ?, priceAfter = ? where reviewUuid = ? and price = ?";
//		rebatesNewDao.updateBySql(sql, price, price, reviewUuid, bd);
//	}

//	public void updateRebateRate(String rid, String cid, String rate, String price) {
//		//更新rebates表中汇率
//		String sql = "UPDATE rebates SET currencyExchangerate = ? WHERE id = ?";
//		rebatesNewDao.updateBySql(sql, rate, rid);
//		//更新cost_record表中汇率
//		sql = "update cost_record set price = ?, priceAfter = ? where id = ?";
//		rebatesNewDao.updateBySql(sql, price, price, cid);
//	}

	public void updateRefundRate(String reviewUuid, String rate, String price) {
		//更新money_amount表中汇率
		String sql = "UPDATE money_amount set exchangerate = ? where review_uuid = ?";
		rebatesNewDao.updateBySql(sql, rate, reviewUuid);
		//更新cost_record表中汇率
		sql = "update cost_record set price = ?, priceAfter = ? where reviewUuid = ?";
		rebatesNewDao.updateBySql(sql, price, price, reviewUuid);
	}
}
