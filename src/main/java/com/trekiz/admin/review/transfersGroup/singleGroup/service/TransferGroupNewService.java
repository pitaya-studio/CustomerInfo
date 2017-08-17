package com.trekiz.admin.review.transfersGroup.singleGroup.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.review.transfersGroup.common.service.TransferGroupReviewNewService;
import com.trekiz.admin.review.transfersGroup.singleGroup.dao.ApplyTransferGroupDao;
import com.trekiz.admin.review.transfersGroup.singleGroup.entity.TransferInfo;
import com.trekiz.admin.review.transfersGroup.singleGroup.form.TransferForm;

@Service
@Transactional(readOnly = true)
public class TransferGroupNewService {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private ApplyTransferGroupDao applyTransferGroupDao;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
	@Autowired
	private TransferGroupReviewNewService transferGroupReviewService;

	/**
	 * @Description  转团申请
	 * @author yakun.bai
	 * @throws Exception 
	 * @Date 2015-12-7
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Map<String, String> reviewApply(String subtractMoneys, Map<String,String> map, List<Traveler> travelList,
			ActivityGroup targetGroup, String[] remark, String payType, String remainDays,
			ProductOrderCommon sourceOrder, TravelActivity sourceProduct, HttpServletRequest request) throws Exception {
		
		String userId = UserUtils.getUser().getId().toString();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		
		// 游客ID、姓名
		List<String> travelerIdList = new ArrayList<>();
		List<String> travelerNameList = new ArrayList<>();
		List<String> substractSerialList = new ArrayList<>();  // 扣减金额流水号
		List<String> payPriceSerialList = new ArrayList<>();  // 游客结算流水号
		//修改游客扣减金额
		Map<Long, String> subtractMoneyMap = Maps.newHashMap();
		if (StringUtils.isNotBlank(subtractMoneys)) {
			String[] subtractMoneyArr = subtractMoneys.split(",");
			if (subtractMoneyArr != null && subtractMoneyArr.length > 0) {
				List<MoneyAmount> moneyAmountList = new ArrayList<MoneyAmount>();
				
				for (int j = 0; j<subtractMoneyArr.length; j++) {
					String uuid = UuidUtils.generUuid();
					String currencyId_money =  subtractMoneyArr[j];
					String[] moneyArr = currencyId_money.split("#");
					if (moneyArr != null && moneyArr.length == 3) {
						MoneyAmount ma = new MoneyAmount();
						// 币种ID
						ma.setCurrencyId(Integer.valueOf(moneyArr[1]));
						// 金额
						ma.setAmount(new BigDecimal(moneyArr[2]));
						ma.setSerialNum(uuid);
						moneyAmountList.add(ma);
						moneyAmountService.saveMoneyAmounts(moneyAmountList);
						travelerDao.updateTravelerSubtractMoneySerialNum(uuid, Long.parseLong(moneyArr[0].toString()));
						subtractMoneyMap.put(Long.parseLong(moneyArr[0].toString()), uuid);
						//
						for (int i = 0; i < travelList.size(); i++) {
							if (travelList.get(i).getId().toString().equals(moneyArr[0].toString())) {
								Traveler t = travelList.get(i);
								t.setSubtractMoneySerialNum(uuid);
								travelerIdList.add(t.getId().toString());
								travelerNameList.add(t.getName());
								t = travelerDao.findById(t.getId());
								payPriceSerialList.add(t.getPayPriceSerialNum());
								substractSerialList.add(uuid);
								break;
							}
						}
					}
				}
			}
		}
		
		// 转出团 产品、团期、订单 信息
		ActivityGroup sourceGroup = activityGroupService.findById(sourceOrder.getProductGroupId());
		Map<String, Object> sourceOrderInfo = OrderCommonUtil.getOrderInfo(sourceOrder, sourceGroup);  // 获取订单、团期信息
		Map<String, Object> sourceProductInfo = OrderCommonUtil.getProductInfo(sourceProduct);  // 获取产品信息
		// 转入团 产品、团期
		TravelActivity targetProduct = travelActivityService.findById(Long.parseLong(targetGroup.getSrcActivityId().toString()));
		
		/** 通用参数赋值 */
		Map<String, Object> variables = new HashMap<String,Object>(); // 业务数据
		variables.putAll(sourceOrderInfo);
		variables.putAll(sourceProductInfo);
		// 是否是新型转团申请（新：多位游客可以作为同一转团审批，生成在同一订单中。旧：一位游客一条转团审批，生成在订单中只有此一位游客。）
		// 由于新旧转团审批的主要数据组织结构不同，特此区分方便后续审批处理。		
		variables.put(Context.REVIEW_VARIABLE_TRANSGROUP_MANY2ONE_FLAG, true);
		// 流程类型
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP);
		// 部门id
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, sourceProduct.getDeptId());
		// 产品类型（转入团）
		variables.put(Context.REVIEW_VARIABLE_TRANSFER_GROUP_NEW_PRODUCT_TYPE, targetProduct.getActivityKind());
		// 产品ID（转入团）
		variables.put(Context.REVIEW_VARIABLE_TRANSFER_GROUP_NEW_PRODUCT_ID, targetProduct.getId());
		// 转入团ID
		variables.put(Context.REVIEW_VARIABLE_TRANSFER_GROUP_NEW_GROUP_ID, targetGroup.getId());
		// 转入团团号
		variables.put(Context.REVIEW_VARIABLE_TRANSFER_GROUP_NEW_GROUP_CODE, targetGroup.getGroupCode());
		// 游客 IDs
		variables.put(Context.REVIEW_TRANSGROUP_OLD_TRAVELERIDS, StringUtils.join(travelerIdList, ","));
		// 游客 IDs
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, StringUtils.join(travelerIdList, ","));
		//支付方式
		variables.put(Context.REVIEW_VARIABLE_KEY_PAY_TYPE, payType);
		//保留天数
		variables.put(Context.REVIEW_VARIABLE_KEY_REMAIN_DAYS, remainDays);	
		// 组织游客信息（key规则：下划线 + travelerId作为后缀）
		for (int i = 0; i < travelList.size(); i++) {
			// 游客id
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID + "_" + travelerIdList.get(i), travelerIdList.get(i));
			// 游客名称
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME + "_" + travelerIdList.get(i), travelerNameList.get(i));
			//应付金额
			variables.put(Context.REVIEW_VARIABLE_KEY_PAY_PRICE + "_" + travelerIdList.get(i), payPriceSerialList.get(i)); 
			//扣减金额(转团后应付)
			variables.put(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE + "_" + travelerIdList.get(i), substractSerialList.get(i));
			// 备注
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK + "_" + travelerIdList.get(i), remark[i]);
		}
		
		//转团申请
		ReviewResult reviewResult = reviewService.start(userId, companyId, permissionChecker, null, sourceProduct.getActivityKind(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, sourceProduct.getDeptId(), "", variables);
		
		// 立即判断是否通过，然后走业务处理。（因为如果流程为无需审批，在申请接口中审批状态即刻发生改变）
		if (reviewResult.getSuccess()) {
			map.put("result", "success");
			// 修改游客状态
			for (int i = 0; i < travelList.size(); i++) {				
				travelList.get(i).setDelFlag(4);
				travelerDao.save(travelList.get(i));
			}
			// 如果审核通过并且当前层级为最高层级 则更改对应业务数据
			if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
				//查询转团申请信息
				Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewResult.getReviewId());
				// 转团审核成功后调用
				transferGroupReviewService.changeGroupSuccess(reviewDetailMap, request);
			}
		} else {
			map.put("result", "error");
			map.put("msg", reviewResult.getMessage());
		}
		
		return map;
	}
	
	/**
	 * 根据status 得到审核状态
	 * @param status
	 * @return
	 */
	public String status_cn(Integer status){
		if(ReviewConstant.REVIEW_STATUS_REJECTED == status){
			return "已驳回";
		}else if(ReviewConstant.REVIEW_STATUS_PROCESSING==status){
			return "处理中";
		}else if(ReviewConstant.REVIEW_STATUS_PASSED==status){
			return "已通过";
		}else if(ReviewConstant.REVIEW_STATUS_CANCELED==status){
			return "已取消";
		}else{
			return "已驳回";
		}
	}
	/**
	 * 查询审核列表
	 * @param vo
	 * @return
	 */
	public List<TransferInfo> transferOrderSingle(TransferForm vo){
		
		List<TransferInfo> backList = new ArrayList<TransferInfo>();
		// 获取按指定条件查询到的审核记录列表
		List<Map<String,Object>> list = applyTransferGroupDao.getReviewNew(vo);
		if(!list.isEmpty()){
			// 
			for(Map<String,Object> map : list){
				TransferInfo info = new TransferInfo();
				info.setOrderNo(map.get("order_no")!=null?map.get("order_no").toString():null);
				info.setGroupCode(map.get("group_code")!=null?map.get("group_code").toString():null);
				info.setProductName(map.get("product_name")!=null?map.get("product_name").toString():null);
				if(map.get("product_type")!=null){
					Integer type = Integer.valueOf(map.get("product_type").toString());
					info.setProductType(changeProductType(type));
				}
				if(map.get("create_date")!=null){
					String date = DateUtils.formatCustomDate((Date)map.get("create_date"), DateUtils.DATE_PATTERN_YYYY_MM_DD);
					info.setCreateDate(date);
				}
				if(map.get("create_by")!=null){
					String userName = UserUtils.getUserNameById(Long.valueOf(map.get("create_by").toString()));
					info.setCreateBy(userName);
				}
				if(map.get("agent")!=null){
					Agentinfo ag = agentinfoService.findOne(Long.valueOf(map.get("agent").toString()));
					if(ag!=null&&StringUtils.isNotBlank(ag.getAgentName())){
						info.setAgentName(ag.getAgentName());
					}
				}
				info.setTravelerName(map.get("traveller_name")!=null?map.get("traveller_name").toString():null);
				// 获取应付金额，转团后应付金额
//				if(map.get("uuid")!=null){
//					String reviewUuid = map.get("uuid").toString();
//					// 获取审核详情
//					Map<String, Object> reviewMap = reviewService.getReviewDetailMapByReviewId(reviewUuid);
//					if(reviewMap.get(Context.REVIEW_VARIABLE_KEY_PAYPRICESERIALNUM)!=null){
//						// 获取应付金额uuid
//						String oldAmountMoneyUUID = reviewMap.get(Context.REVIEW_VARIABLE_KEY_PAYPRICESERIALNUM).toString();
//						String oldAmountMoney = moneyAmountService.getMoney(oldAmountMoneyUUID);
//						info.setOldAmountMoney(oldAmountMoney);
//					}
//					if(reviewMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACTMONEYSERIALNUM)!=null){
//						// 获取转团后应付金额
//						String newAmountMoneyUUID = reviewMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACTMONEYSERIALNUM).toString();
//						String newAmountMoney = moneyAmountService.getMoney(newAmountMoneyUUID);
//						info.setOldAmountMoney(newAmountMoney);
//					}
//				}
				if(map.get("status")!=null){
					Integer status = Integer.valueOf(map.get("status").toString());
					info.setStatus(changeProductType(status));
				}
				backList.add(info);
			}
		}
		return backList;
	}
	/**
	 * 根据产品类型获取产品类型名称
	 * @param productType
	 * @return
	 */
	public String changeProductType(Integer productType){
		if(Context.ProductType.PRODUCT_SINGLE.equals(productType)){
			return "单团";
		}else if(Context.ProductType.PRODUCT_LOOSE.equals(productType)){
			return "散拼";
		}else if(Context.ProductType.PRODUCT_STUDY.equals(productType)){
			return "游学";
		}else if(Context.ProductType.PRODUCT_BIG_CUSTOMER.equals(productType)){
			return "大客户";
		}else if(Context.ProductType.PRODUCT_FREE.equals(productType)){
			return "自由行";
		}else if(Context.ProductType.PRODUCT_AIR_TICKET.equals(productType)){
			return "机票";
		}else{
			return null;
		}
	}
	
	/**
	 * @Description  转团申请
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Map<String, Object> cancelTransferGroup(String reviewId, String travelerId) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		Map<String, Object> result = Maps.newHashMap();

		//取消转团申请
		ReviewResult reviewResult = reviewService.cancel(userId, companyId, "", reviewId, "", null);

		boolean flag = reviewResult.getSuccess();
		if (flag) {
			result.put("result", "success");
			result.put("msg", reviewResult.getMessage());
			// 游客状态改变
			String [] travelerIdArray = travelerId.split(",");
			if (travelerIdArray != null && travelerIdArray.length > 0) {
				for (int j = 0; j < travelerIdArray.length; j++) {					
					Traveler traveler = travelerDao.findById(Long.parseLong(travelerIdArray[j]));
					traveler.setDelFlag(Integer.parseInt(Context.DEL_FLAG_NORMAL));
					travelerDao.save(traveler);
				}
			}
		} else {
			result.put("result", "faild");
			result.put("msg", reviewResult.getMessage());
		}

		return result;
	}
	
	/**
	 * 处理游客多对一转团(多个map构成的List)
	 * @param transferGroupList
	 */
	public void handleData4ManyTraveler(List<Map<String, Object>> transferGroupList) {
		if (CollectionUtils.isEmpty(transferGroupList)) {
			return;
		}
		for (Map<String, Object> transGroupMap : transferGroupList) {
			handleMap4ManyTraveler(transGroupMap);
		}
		return;
	}
	
	/**
	 * 处理游客多对一转团(单个map)
	 * @param transferGroupList
	 */
	public void handleMap4ManyTraveler(Map<String, Object> transGroupMap) {
		// 如果是多游客转入同一订单，则重新组织名字、价格等
		Object transGroupMany2One = transGroupMap.get(Context.REVIEW_VARIABLE_TRANSGROUP_MANY2ONE_FLAG);
		List<Map<String, Object>> travelerMapList = new ArrayList<>();  // 诸位游客信息 map
		if (transGroupMany2One != null && (boolean)transGroupMany2One) {
			// 获取 游客id
			String travelerIDString = transGroupMap.get(Context.REVIEW_TRANSGROUP_OLD_TRAVELERIDS).toString();
			if (StringUtils.isNotBlank(travelerIDString) && travelerIDString.split(",").length > 0) {
				String[] travelerIDArray = travelerIDString.split(",");  // 诸位游客 IDs
				// 要覆盖的信息（逗号隔开）
				String travelerNames = "";  // 游客姓名
				List<MoneyAmount> payPriceSum = new ArrayList<>();  // 游客应收总和
				List<MoneyAmount> subtractSum = new ArrayList<>();  // 游客扣减金额总和	
				// 组织转团游客信息
				for (int i = 0; i < travelerIDArray.length; i++) {
					Map<String, Object> travelerInfoMap = new HashMap<>();
					Long travelerId = Long.parseLong(travelerIDArray[i]);
					
					// 游客姓名
					if (StringUtils.isNotBlank(travelerNames)) {							
						travelerNames += ",";
					}
					String travelerName = transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME + "_" + travelerId.toString()).toString();
					travelerNames += travelerName;
					
					// 游客id
					travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId.toString());
					// 游客 name
					travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME + "_" + travelerId.toString()));
					// 游客 结算价
					String payPriceSerial = transGroupMap.get(Context.REVIEW_VARIABLE_KEY_PAY_PRICE + "_" + travelerId.toString()).toString();
					travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_PAY_PRICE, moneyAmountService.getMoneyStr(payPriceSerial));
					payPriceSum.addAll(moneyAmountService.getMoneyAmonutListIgnoreDelflag(payPriceSerial));
					// 游客 扣减金额
					String subtractSerial = transGroupMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE + "_" + travelerId.toString()).toString();
					travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE, moneyAmountService.getMoneyStr(subtractSerial));
					subtractSum.addAll(moneyAmountService.getMoneyAmonutListIgnoreDelflag(subtractSerial));
					// 游客 备注
					travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK + "_" + travelerId.toString()));
					// 游客实体
					Traveler traveler = travelerDao.findById(travelerId);  // 校验游客是否为空
					travelerInfoMap.put("traveler", traveler);
					travelerMapList.add(travelerInfoMap);
				}
				// 覆盖原有数据
				transGroupMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, travelerNames);
				transGroupMap.put("payPriceSumString", moneyAmountService.getMoneyStrFromAmountList("mark", moneyAmountService.sameCurrencySum(payPriceSum)));
				transGroupMap.put("subtractSumString", moneyAmountService.getMoneyStrFromAmountList("mark", moneyAmountService.sameCurrencySum(subtractSum)));
				transGroupMap.put("travelerMapList", travelerMapList);
			}
		} else {
			Map<String, Object> travelerInfoMap = new HashMap<>();
			Long travelerId = Long.parseLong(transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString());
			// 游客id
			travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId.toString());
			// 游客 name
			travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME));
			// 游客 结算价
			String payPriceMoneyStr = moneyAmountService.getMoneyStr(transGroupMap.get(Context.REVIEW_VARIABLE_KEY_PAY_PRICE).toString());
			travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_PAY_PRICE, payPriceMoneyStr);
			// 游客 扣减金额
			String substractMoneyStr = moneyAmountService.getMoneyStr(transGroupMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE) == null ? null : transGroupMap.get(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE).toString());
			travelerInfoMap.put(Context.REVIEW_VARIABLE_KEY_SUBTRACT_PRICE, substractMoneyStr);
			// 游客 备注
			travelerInfoMap.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, transGroupMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK));
			
			//游客信息
			Traveler traveler = travelerDao.findById(travelerId);
			travelerInfoMap.put("traveler", traveler);
			travelerMapList.add(travelerInfoMap);
			transGroupMap.put(Context.REVIEW_TRANSGROUP_OLD_TRAVELERIDS, travelerId.toString());
			transGroupMap.put("payPriceSumString", payPriceMoneyStr);
			transGroupMap.put("subtractSumString", substractMoneyStr);
			transGroupMap.put("travelerMapList", travelerMapList);
		}
	}
	
}
