package com.trekiz.admin.review.transfersMoney.singlegroup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ibm.icu.math.BigDecimal;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.TransferMoneyApplyForm;
import com.trekiz.admin.modules.order.entity.TransferMoneyShowBean;
import com.trekiz.admin.modules.order.entity.TravelerShowBean;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.repository.NewProcessMoneyAmountDao;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;


@Service
@Transactional(readOnly = true)
public class NewTransferMoneyService extends BaseService {
	
	private static final Integer OPERATE_SUCCESS=ReviewConstant.REVIEW_STATUS_PASSED;//操作完成 

	@Autowired
    private ProductOrderCommonDao productorderDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private CurrencyService currenceService;
	@Autowired
	private NewProcessMoneyAmountService newAmountService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private NewProcessMoneyAmountDao newAmountDao;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private NewProcessMoneyAmountService newProcessMoneyAmountService;
	@Autowired
	private NewProcessMoneyAmountDao newProcessMoneyAmountDao;
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;

	/**
	 * 前往转款申请页面，获取转团信息，及可以转款的游客信息
	 * @author yang.jiang 2015-12-9 20:55:45
	 * @param condition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getResultMap(Map<String, String> condition, Map<String, Object> result) {
		// TODO Auto-generated method stub
		String orderId = condition.get("orderId");
//		Map<String, Object> result = new HashMap<String, Object>();
		result.put("err", "suc");
		result.put("orderId", orderId);
		if(StringUtils.isBlank(orderId)){
			result.put("err", "转出（oldOrderId)订单id不存在");
			return result;
		}
		ProductOrderCommon productOrder = productorderDao.findOne(Long.parseLong(orderId));
		ActivityGroup activityGroup = new ActivityGroup();
		if(productOrder!=null){
			activityGroup = activityGroupDao.findOne(productOrder.getProductGroupId());
		}
		//转出订单详情(oldOrder)
		TransferMoneyShowBean showBean = new TransferMoneyShowBean();
		showBean.setProductOrderCommon(productOrder);
		showBean.setActivitygroup(activityGroup);
		showBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(productOrder.getTotalMoney()));
		if (StringUtils.isNotBlank(productOrder.getAccountedMoney())) {
			showBean.setOrderAccountedMoney(moneyAmountService.getMoneyStr(productOrder.getAccountedMoney()));
		}
		//转入订单详情(newOrder)
		
		List<TransferMoneyShowBean> listOutBean = new ArrayList<TransferMoneyShowBean>();
		List<TravelerShowBean> listTraveler = new ArrayList<TravelerShowBean>();
		
		//获取订单创建者所在公司的币种列表
		List<Currency> creatorCurrencyList = currencyService.findCurrencyList(productOrder.getCreateBy().getCompany().getId());
		List<ReviewNew> listReview = reviewService.getOrderReviewList(orderId,productOrder.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());
		for(ReviewNew review : listReview){
			if(ReviewConstant.REVIEW_STATUS_PASSED!=review.getStatus()){
				continue;
			}
			Map<String,Object> map = reviewService.getReviewDetailMapByReviewId(review.getId());
			
			String newOrderId = null;
			if (map.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1) != null) {
				newOrderId = map.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1).toString();  //转团成功后产生新订单id，存放在review_new的第一个扩展字段
			}
			if(StringUtils.isBlank(newOrderId)){
				result.put("err", "找不到新订单id");
				return result;
			}
			List<ActivityGroup>  listGroup = activityGroupDao.findByGroupIdAndCompany(Long.parseLong(map.get(Context.REVIEW_VARIABLE_TRANSFER_GROUP_NEW_GROUP_ID).toString()), UserUtils.getUser().getCompany().getId());
			if(CollectionUtils.isEmpty(listGroup)){
				result.put("err", "找不到转入团信息");
				return result;
			}
			
			TransferMoneyShowBean outBean = new TransferMoneyShowBean();
			ProductOrderCommon order = productorderDao.findOne(Long.parseLong(newOrderId));
			outBean.setProductOrderCommon(order);
			ActivityGroup  group  = listGroup.get(0);
			outBean.setActivitygroup(group);
			outBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
			if (StringUtils.isNotBlank(order.getAccountedMoney())) {
				outBean.setOrderAccountedMoney(moneyAmountService.getMoneyStr(order.getAccountedMoney()));
			}
			listOutBean.add(outBean);
			
			String travelerIds = map.get(Context.REVIEW_VARIABLE_TRANSFER_GROUP_TRAVELLERID).toString();
			String newTravelerIds = map.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_2).toString();
			String[] travelerArr = travelerIds.split(",");
			String[] newTravelerArr = newTravelerIds.split(",");
			for (int i = 0; i < travelerArr.length; i++) {
				
				String travelerId = travelerArr[i];
				String newTravelerId = newTravelerArr[i];
				
				//此游客是转团之前游客entity
				Traveler oldTraveler = travelerDao.findById(Long.parseLong(travelerId));
				//此游客是转团之后游客entity
				Traveler newTraveler = new Traveler();  //转团成功后产生新游客id，存放在review_new的第二个扩展字段
				String newTravellerId = newTravelerId;
				if (StringUtils.isNotBlank(newTravellerId)) {				
					newTraveler = travelerDao.findById(Long.parseLong(newTravellerId));  //转团成功后产生新游客id，存放在review_new的第二个扩展字段
				}
				//转款游客及其他信息
				TravelerShowBean bean = new TravelerShowBean();
				bean.setTraveler(oldTraveler);
				bean.setNewTraveler(newTraveler);
				bean.setGroupNo(group==null?"":group.getGroupCode());
				bean.setInOrderId(order.getId());
				bean.setNewOrder(order);
				bean.setOrderMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
				bean.setOldPayPriceMoney(moneyAmountService.getMoneyStr(oldTraveler.getPayPriceSerialNum()));
				bean.setNewPayPriceMoney(moneyAmountService.getMoneyStr(newTraveler.getPayPriceSerialNum()));
				List<Currency> currencyList;
				//获取订单到账金额中包含的币种
				if (StringUtils.isNotBlank(productOrder.getAccountedMoney())) {
					currencyList = currencyService.findCurrcyListBySerium(productOrder.getAccountedMoney());
				}else{
					currencyList = creatorCurrencyList;
				}
				bean.setCurrencyList(currencyList);
				listTraveler.add(bean);
			}
		}
		//如果新审批没有转团游客，则返回旧审批result
		if (CollectionUtils.isEmpty(listTraveler)) {
			return result;
		}		
		//如果旧审批也没有游客，则直接返回不必合并新旧审批的转团信息result
		List<TravelerShowBean> oldTravelerList = (List<TravelerShowBean>) (result.get("listTraveler"));
		if (CollectionUtils.isNotEmpty(oldTravelerList)) {
			//如果只有新审批有待转款游客，直接覆盖放入result集合。  do nothing
			//如果新旧都有待转款的游客，则合并数据集合
			creatorCurrencyList.addAll((List<Currency>) (result.get("currencyList")));
			listOutBean.addAll((List<TransferMoneyShowBean>) (result.get("listOutBean")));
			listTraveler.addAll(oldTravelerList);
		}
		result.put("fromBean", showBean);
		result.put("currencyList", creatorCurrencyList);		
		result.put("listOutBean", listOutBean);
		result.put("listTraveler", listTraveler);
		return result;
	}
	
	@Transactional
	public Map<String, String> transfersMoneyCancel(@SuppressWarnings("rawtypes") Map condition) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String companyUuid = condition.get("companyUuid").toString();
		String userId = condition.get("userId").toString();
		String reviewId = condition.get("reviewId").toString();
		String travellerId = condition.get("travellerId").toString();
		// 取消退团
		ReviewResult reviewResult = reviewService.cancel(userId, companyUuid, "", reviewId, "", null);
		//如果返回为 success
		if (reviewResult.getSuccess()) {						
			//改变游客状态
			Traveler traveler = travelerService.findTravelerById(Long.parseLong(travellerId));
			traveler.setDelFlag(Integer.valueOf(Context.DEL_FLAG_NORMAL));
			travelerService.saveTraveler(traveler);
			//金额状态改变
			NewProcessMoneyAmount moneyAmount = newAmountService.findByReviewId(reviewId);
			moneyAmount.setDelFlag(Context.DEL_FLAG_DELETE);

			resultMap.put("res", "success");
			resultMap.put("reply", reviewResult.getMessage());
		} else {
			resultMap.put("res", "faild");
			resultMap.put("reply", reviewResult.getMessage());
		}

		return resultMap;		
	}
	
	public Map<String, Object> transferMoneyDetails(Map<String, String> condition) {
		Map<String, Object> map = new HashMap<String, Object>();
		String reviewId = condition.get("reviewId");
		String err = "success";
		ReviewNew review = reviewService.getReview(reviewId);
		if(review==null){
			err="找不到对应的审批信息";
			map.put("err", err);
			return map;
		}
		Map<String, Object> reviewDetials = reviewService.getReviewDetailMapByReviewId(reviewId);
		String newOrderId = reviewDetials.get(Context.REVIEW_VARIABLE_KEY_NEW_ORDERID).toString();
		String oldOrderId =  reviewDetials.get(Context.REVIEW_VARIABLE_KEY_OLD_ORDERID).toString();
		String oldTravelId =  reviewDetials.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID) == null ? "" :reviewDetials.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString();  //旧订单中游客id
		String newTravelId =  reviewDetials.get(Context.REVIEW_VARIABLE_KEY_NEW_TRAVELLER_ID) == null ? "" : reviewDetials.get(Context.REVIEW_VARIABLE_KEY_NEW_TRAVELLER_ID).toString();  //新订单中游客id
		String uuid =  reviewDetials.get(Context.REVIEW_VARIABLE_KEY_TRANSFER_MONEY_UUID).toString();
		
		ProductOrderCommon productOrder = productorderDao.findOne(Long.parseLong(oldOrderId));
		ActivityGroup activityGroup = null;
		if(productOrder!=null){
			activityGroup = activityGroupDao.findOne(productOrder.getProductGroupId());
		}
		//转出订单详情(oldOrder) TODO 转移到new
		TransferMoneyShowBean oldBean = new TransferMoneyShowBean();
		oldBean.setProductOrderCommon(productOrder);
		oldBean.setActivitygroup(activityGroup);
		oldBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(productOrder.getTotalMoney()));
		
		//转入订单详情(newOrder) TODO 转移到new
		ProductOrderCommon newOrder = productorderDao.findOne(Long.parseLong(newOrderId));
		ActivityGroup newGroup = null;
		if(newOrder!=null){
			newGroup = activityGroupDao.findOne(newOrder.getProductGroupId());
		}
		
		TransferMoneyShowBean newBean = new TransferMoneyShowBean();
		newBean.setProductOrderCommon(newOrder);
		newBean.setActivitygroup(newGroup);
		newBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(newOrder.getTotalMoney()));
		
		//游客信息
		Traveler traveler = new Traveler();
		if (StringUtils.isNotBlank(oldTravelId)) {			
			traveler = travelerDao.findById(Long.parseLong(oldTravelId));
		}
		//此游客是转团之后游客entity
		Traveler newTraveler = new Traveler();
		if (StringUtils.isNotBlank(newTravelId)) {
			newTraveler = travelerDao.findById(Long.parseLong(newTravelId));			
		}
		TravelerShowBean bean = new TravelerShowBean();
		bean.setTraveler(traveler);
		bean.setNewTraveler(newTraveler);
		bean.setGroupNo(newGroup==null?"":newGroup.getGroupCode());
		bean.setNewOrder(newOrder);
		bean.setInOrderId(newOrder.getId());
		bean.setOldPayPriceMoney(moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum()));
		//如果没有新游客或者对应结算价信息
		String newPayPrice = ""; 
		if (newTraveler == null || StringUtils.isBlank(newTraveler.getPayPriceSerialNum())) {
			if (Context.PERSON_TYPE_ADULT == traveler.getPersonType()) {
				newPayPrice = moneyAmountService.getMoneyStr(newOrder.getSettlementAdultPrice());
			} else if (Context.PERSON_TYPE_CHILD == traveler.getPersonType()) {
				newPayPrice = moneyAmountService.getMoneyStr(newOrder.getSettlementcChildPrice());
			} else if (Context.PERSON_TYPE_SPECIAL == traveler.getPersonType()) {
				newPayPrice = moneyAmountService.getMoneyStr(newOrder.getSettlementSpecialPrice());
			}
		} else {
			newPayPrice = moneyAmountService.getMoneyStr(newTraveler.getPayPriceSerialNum());
		}
		bean.setNewPayPriceMoney(newPayPrice);  //新订单中游客记录的结算价
		bean.setTransferMoney(newAmountService.getMoneyStr(uuid));
		bean.setRemark(reviewDetials.get(Context.REVIEW_VARIABLE_KEY_TRANSFER_MONEY_REMARK)==null?null:reviewDetials.get(Context.REVIEW_VARIABLE_KEY_TRANSFER_MONEY_REMARK).toString());
		map.put("traveler", bean);
		map.put("oldBean", oldBean);
		map.put("newBean", newBean);
		return map;
	}

	public Map<String, Object> transferMoneyReviewInfo(Map<String, String> condition) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String reviewId = condition.get("reviewId");
		ReviewNew review = reviewService.getReview(reviewId);
		List<ReviewLogNew> rLog=reviewService.getReviewLogByReviewId(reviewId);
		map.put("review", review);//denyReason
		map.put("rLog", rLog);
		return map;
	}
	
	@Transactional
	public Map<String, Object> batchTransferMoneyReview(Map<String, Object> param){
		//初始返回flag及信息
		Map<String, Object> map =new HashMap<String,Object>();				
		String msgStr = "</br>";				
		boolean flag = true;
		//取出参数
		String revIds = param.get("revIds").toString();
		String remark = param.get("remark").toString();
		String strResult = param.get("strResult").toString();
		//获取并判断要执行的操作
		Integer reviewOperation = 0;
		if("2".equals(strResult)){
			reviewOperation = ReviewConstant.REVIEW_OPERATION_PASS;
		}
		//获取参与批量审批的诸reviewId 遍历之
		String[] revidArr = revIds.split(",");
		for(String revid : revidArr){
			if(revid == null || "".equals(revid)){
				map.put("flag", 0);
				map.put("message", msgStr += "错误的参数,reviewid不能为空");
				return map;
			}
			String companyId = UserUtils.getUser().getCompany().getUuid();
			ReviewResult reviewResult;
			// 2 调用审批接口处理
			if(ReviewConstant.REVIEW_OPERATION_PASS == reviewOperation){  
				//批量审批通过
				reviewResult = reviewService.approve(UserUtils.getUser().getId().toString(), companyId, null, userReviewPermissionChecker, revid, remark, null);
				//审批通过进行转款（旧订单已付/到账划扣，新订单已付/到账增加，新订单支付状态变化）
				if(reviewResult.getSuccess() && reviewResult.getReviewStatus()==ReviewConstant.REVIEW_STATUS_PASSED){
					map = reviewTransferMoneyDone(reviewResult.getReviewId());
				}
			} else {  
				//批量审批驳回
				reviewResult = reviewService.reject(UserUtils.getUser().getId().toString(), companyId, null, revid, remark, null);
			}
			
			if(reviewResult.getSuccess()){
				map.put("flag", 1);
				map.put("message", msgStr += "Success:" + reviewResult.getReviewId() + "</br>");
			} else {
				flag = false;
				map.put("message", msgStr += "Faild:" + reviewResult.getMessage() + "</br>");
			}
		}
		
		map.put("flag", flag ? 1 : 0);
		return map;
	}

	/**
	 * describe 转款审批通过，处理业务数据
	 * add by jyang
	 * add date 2015年11月6日09:41:16
	 * @param rid 审批ID
	 * @param request
	 * @return
	 */
//	@Transactional
//	public  Map<String,Object> reviewTransferMoneyDone(String rid){
//		Map<String,Object>map = new HashMap<String,Object>();
//		ReviewNew r = reviewService.getReview(rid);
//		if(null != r && OPERATE_SUCCESS.intValue() == r.getStatus().intValue()){
//			boolean flag = false;
//			try {
//				 flag = transferMoneyApplyDone(rid);
//			} catch (Exception e) {
//				map.put("flag", "0");
//				map.put("message", "操作失败!");
//				return map;
//			}
//			if(flag){
//				map.put("flag", "1");
//				map.put("message", "操作成功！");	
//			}else{             
//				map.put("flag", "0");
//				map.put("message", "操作失败!");
//			}
//		}else{
//			map.put("flag", "0");
//			map.put("message", "操作失败!");
//		}
//		
//		return map;
//	}	
	
	/**
	 * describe 转款审批通过，处理业务数据
	 * add by jyang
	 * add date 2015年11月6日09:41:16
	 * @param rid 审批ID
	 * @param request
	 * @return
	 */
	@Transactional
	public  Map<String,Object> reviewTransferMoneyDone(String reviewId){
		Map<String,Object>map = new HashMap<String,Object>();
		ReviewNew review = reviewService.getReview(reviewId);
		boolean flag = false;
		if(null != review && OPERATE_SUCCESS.intValue() == review.getStatus().intValue()){
			try {
				String orderType = review.getProductType();
				//当前只支持团期类
				if (Context.PRODUCT_TYPE_DAN_TUAN.equals(orderType) || 
						Context.PRODUCT_TYPE_SAN_PIN.equals(orderType) || 
						Context.PRODUCT_TYPE_YOU_XUE.equals(orderType) || 
						Context.PRODUCT_TYPE_DA_KE_HU.equals(orderType) || 
						Context.PRODUCT_TYPE_ZI_YOU_XING.equals(orderType) ||
						Context.ACTIVITY_KINDS_YL.equals(orderType)) {
					
					//获取审批详情 MAP 新旧订单
					Map<String, Object> reviewDetials = reviewService.getReviewDetailMapByReviewId(reviewId);
					String newOrderId = reviewDetials.get(Context.REVIEW_VARIABLE_KEY_NEW_ORDERID).toString();
					String oldOrderId =  reviewDetials.get(Context.REVIEW_VARIABLE_KEY_OLD_ORDERID).toString();
//					String travelId =  reviewDetials.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString();
					String transferMoneyUuid =  reviewDetials.get(Context.REVIEW_VARIABLE_KEY_TRANSFER_MONEY_UUID).toString();
					//修改新订单支付状态 `payStatus` tinyint(4) DEFAULT NULL COMMENT '支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消',
					ProductOrderCommon oldOrder = productorderDao.findOne(Long.parseLong(oldOrderId));
					ProductOrderCommon newOrder = productorderDao.findOne(Long.parseLong(newOrderId));
					newOrder.setPayStatus(oldOrder.getPayStatus());
					productorderDao.getSession().update(newOrder);		
					/** 转款 */
					if (StringUtils.isEmpty(newOrder.getPayedMoney())) {  //新单已付
						newOrder.setPayedMoney(UuidUtils.generUuid());
					}
					if (StringUtils.isEmpty(newOrder.getAccountedMoney())) {  //新单到账
						newOrder.setAccountedMoney(UuidUtils.generUuid());
					}
					//遍历转款金额 moneyAmount（多币种），旧单减去，新单增加
					List<NewProcessMoneyAmount> transferMoneyList = newAmountDao.findAmountBySerialNum(transferMoneyUuid);
					if (CollectionUtils.isNotEmpty(transferMoneyList)) {
						for (NewProcessMoneyAmount transferMoney : transferMoneyList) {
							if (moneyAmountService.saveOrUpdateMoneyAmount(transferMoney, oldOrder.getAccountedMoney(), "subtract", Context.MONEY_TYPE_DZ, oldOrder.getOrderStatus()) &&
									moneyAmountService.saveOrUpdateMoneyAmount(transferMoney, newOrder.getAccountedMoney(), "add", Context.MONEY_TYPE_DZ, newOrder.getOrderStatus()) &&
									moneyAmountService.saveOrUpdateMoneyAmount(transferMoney, oldOrder.getPayedMoney(), "subtract", Context.MONEY_TYPE_YS, oldOrder.getOrderStatus()) &&
									moneyAmountService.saveOrUpdateMoneyAmount(transferMoney, newOrder.getPayedMoney(), "add", Context.MONEY_TYPE_YS, newOrder.getOrderStatus())) {
								flag = true;
							}
						}
					}
				}
				
			} catch (Exception e) {
				map.put("flag", "0");
				map.put("message", "操作失败!");
				return map;
			}			
		}
		
		if(flag){
			map.put("flag", "1");
			map.put("message", "操作成功！");	
		}else{             
			map.put("flag", "0");
			map.put("message", "操作失败!");
		}
		
		return map;
	}
	
	/*
	 * 转团转款审批通过
	 * param: reviewId 
	 * return err 错误信息 success:成功 else: 错误信息
	 */
	@Transactional
    public boolean transferMoneyApplyDone(String reviewId){
    	boolean flag = true;
		ReviewNew review = reviewService.getReview(reviewId);
		if(review == null){
			flag = false;
			return flag;
		}
		Map<String, Object> reviewDetials = reviewService.getReviewDetailMapByReviewId(reviewId);
		String newOrderId = reviewDetials.get(Context.REVIEW_VARIABLE_KEY_NEW_ORDERID).toString();
		String oldOrderId =  reviewDetials.get(Context.REVIEW_VARIABLE_KEY_OLD_ORDERID).toString();
//		String travelId =  reviewDetials.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString();
		String uuid =  reviewDetials.get(Context.REVIEW_VARIABLE_KEY_TRANSFER_MONEY_UUID).toString();
		//`payStatus` tinyint(4) DEFAULT NULL COMMENT '支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消',
		ProductOrderCommon oldOrder = productorderDao.findOne(Long.parseLong(oldOrderId));
		ProductOrderCommon newOrder = productorderDao.findOne(Long.parseLong(newOrderId));
		newOrder.setPayStatus(oldOrder.getPayStatus());
		productorderDao.getSession().update(newOrder);		
		
		flag = transferMoney(Long.parseLong(newOrderId), Long.parseLong(oldOrderId), uuid, review.getProductType());
		return flag;
	}
    
    /**
	 * 转款
	 * 
	 * @param newOrderId
	 * @param oldOrderId
	 * @param uuid
	 * @param orderType
	 * @return
	 */
	@Transactional
	public boolean transferMoney(Long newOrderId, Long oldOrderId, String uuid, String orderType) {
		boolean flag = true;
		if (Context.PRODUCT_TYPE_DAN_TUAN.equals(orderType) || 
				Context.PRODUCT_TYPE_SAN_PIN.equals(orderType) || 
				Context.PRODUCT_TYPE_YOU_XUE.equals(orderType) || 
				Context.PRODUCT_TYPE_DA_KE_HU.equals(orderType) || 
				Context.PRODUCT_TYPE_ZI_YOU_XING.equals(orderType) ||
				Context.ACTIVITY_KINDS_YL.equals(orderType)) {
			// 单团订单
			ProductOrderCommon oldOrder = (ProductOrderCommon) productorderDao.getSession().get(ProductOrderCommon.class, oldOrderId);
			ProductOrderCommon newOrder = (ProductOrderCommon) productorderDao.getSession().get(ProductOrderCommon.class, newOrderId);
			if (StringUtils.isEmpty(newOrder.getAccountedMoney())) {
				newOrder.setAccountedMoney(UuidUtils.generUuid());
			}
			if (StringUtils.isEmpty(newOrder.getPayedMoney())) {
				newOrder.setPayedMoney(UuidUtils.generUuid());
			}
			//到账,需从新审批moneyamount表中获取 ？ 不是转款吗？
			List<NewProcessMoneyAmount> list = newAmountDao.findAmountBySerialNum(uuid);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					flag = moneyAmountService.saveOrUpdateMoneyAmount(list.get(i), oldOrder.getAccountedMoney(), "subtract", Context.MONEY_TYPE_DZ);//查询出来的金额只取了第一个why?					
					flag = moneyAmountService.saveOrUpdateMoneyAmount(list.get(i), newOrder.getAccountedMoney(), "add", Context.MONEY_TYPE_DZ);
					flag = moneyAmountService.saveOrUpdateMoneyAmount(list.get(i), oldOrder.getPayedMoney(), "subtract", Context.MONEY_TYPE_YS);
					flag = moneyAmountService.saveOrUpdateMoneyAmount(list.get(i), newOrder.getPayedMoney(), "add", Context.MONEY_TYPE_YS);
				}
			}
		}
		
		return flag;

	}

	/**
	 * 判断某订单是否可以转款(本方法仅从转团角度判断)
	 * @author yang.jiang 2015-12-9 20:52:32
	 * @param orderStatus
	 * @param orderId
	 * @return
	 */
	public boolean isTransferMoney(ProductOrderCommon order) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (order == null) {
			return flag;
		}
		//获取转款审批流程
		List<ReviewNew> processList =  reviewService.getOrderReviewList(String.valueOf(order.getId()), order.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());
		if (CollectionUtils.isEmpty(processList)) {
			return flag;
		}
		//只要有一个审批记录是 审批中或审批完成，就可成为转款的必要条件
		for (ReviewNew reviewNew : processList) {
			if (reviewNew.getStatus() == ReviewConstant.REVIEW_STATUS_PASSED) {
				flag = true;
				break;
			}
		}
		
		return flag;
	}

	/**
	 * 提交转款申请后，校验表单主要数据的合法性
	 * @author yang.jiang 2015年12月10日 10:27:29
	 * @param appForm
	 * @param resultMap
	 * @return
	 */
	public boolean validateApplyForm(TransferMoneyApplyForm appForm, Map<String, String> resultMap) {
		// TODO Auto-generated method stub
		if (appForm == null) {
			resultMap.put("res", "申请信息获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		/** 出团信息校验 */
		String orderId = appForm.getOrderId();
		if (StringUtils.isBlank(orderId)) {
			resultMap.put("res", "订单ID获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		ProductOrderCommon order = orderService.getProductorderById(Long.valueOf(orderId));
		if (order == null) {
			resultMap.put("res", "订单信息获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		if (order.getProductGroupId() == null) {
			resultMap.put("res", "团期信息获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		ActivityGroup group = activityGroupService.findById(order.getProductGroupId());
		if (group == null) {
			resultMap.put("res", "团期信息获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		if (order.getProductId() == null) {
			resultMap.put("res", "产品信息获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		TravelActivity product = travelActivityService.findById(order.getProductId());
		if (product == null) {
			resultMap.put("res", "产品信息获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		Long deptId = product.getDeptId();
		if (deptId == null) {
			resultMap.put("res", "部门信息获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		if (order.getOrderStatus() == null) {
			resultMap.put("res", "订单产品类型获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		if (StringUtils.isBlank(order.getAccountedMoney())) {
			resultMap.put("res", "订单无到帐金额，不支持转款！");
			resultMap.put("msg", "faild");
			return false;
		}
		/** 入团信息校验 */
		Long[] inOrderIds = appForm.getInOrderId();  //转入订单Id
		Integer[] travolerIds = appForm.getTravelorId();  //游客Id
		String[] travolerNames = appForm.getTravelorName();  //游客name
		String[] tranMoneys = appForm.getTransferMoney();  //转款金额
		if (inOrderIds == null || inOrderIds.length <= 0) {
			resultMap.put("res", "订单ID获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		if (travolerIds == null || travolerIds.length <= 0) {
			resultMap.put("res", "游客ID获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		if (tranMoneys == null || tranMoneys.length <= 0) {
			resultMap.put("res", "转款金额获取失败！");
			resultMap.put("msg", "faild");
			return false;
		}
		if (inOrderIds.length != travolerIds.length) {
			resultMap.put("res", "游客与订单信息不相符！");
			resultMap.put("msg", "faild");
			return false;
		}		
		//申请数目（应该与travolerIds.length、travolerIds.length保持一致）
		Integer applyNum = travolerIds.length;
		//遍历每条申请，进一步校验订单、团期、产品、部门、游客等
		for (int i = 0; i < applyNum; i++) {
			Long inOrderId = inOrderIds[i];
			if (inOrderId == null) {
				resultMap.put("res", "订单ID获取失败！");
				resultMap.put("msg", "faild");
				return false;
			}
			//转入订单
			ProductOrderCommon inOrder = productorderDao.findOne(inOrderId);
			if(inOrder == null){
				resultMap.put("res", "转入订单信息获取失败");
				resultMap.put("msg", "faild");
				return false;
			}
			//转入团
			if (inOrder.getProductGroupId() == null) {
				resultMap.put("res", "团期信息获取失败！");
				resultMap.put("msg", "faild");
				return false;
			}
			ActivityGroup inGroup = activityGroupService.findById(inOrder.getProductGroupId());
			if (inGroup == null) {
				resultMap.put("res", "团期信息获取失败！");
				resultMap.put("msg", "faild");
				return false;
			}
			//转入团产品
			if (inOrder.getProductId() == null) {
				resultMap.put("res", "产品信息获取失败！");
				resultMap.put("msg", "faild");
				return false;
			}
			TravelActivity inProduct = travelActivityService.findById(inOrder.getProductId());
			if (inProduct == null) {
				resultMap.put("res", "产品信息获取失败！");
				resultMap.put("msg", "faild");
				return false;
			}
			//TODO 转入团部门是否需要？
			//游客
			Long travellerId = (long)travolerIds[i];
			if (travellerId == null) {
				resultMap.put("res", "获取游客" + travolerNames==null?"":travolerNames[i] + "的Id失败！");
				resultMap.put("msg", "faild");
				return false;
			}
			Traveler traveler = travelerDao.findOne(travellerId) ;
			if(traveler==null){
				resultMap.put("res", "获取游客" + travolerNames==null?"":travolerNames[i] + "的信息失败-"+travolerIds[i]);
				resultMap.put("msg", "faild");
				return false;
			}
		}
		return true;
	}

	/**
	 * 本订单已审批中的转款款项总额
	 * @author yang.jiang 2015年12月10日 15:47:02
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<NewProcessMoneyAmount> getProcessingTransferMoney(long orderId, Integer orderType) {
		// TODO Auto-generated method stub
		List<NewProcessMoneyAmount> newMoneyAmounts = Lists.newArrayList();
		//获取订单所有转款审批
		List<ReviewNew> reviewNewList = reviewService.getOrderReviewList(String.valueOf(orderId), orderType.toString(), Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());
		//遍历，，获取转款金额list，
		for (ReviewNew reviewNew : reviewNewList) {
			String transferMoneyUuid = "";
			//正在审批中的审批数据
			if (reviewNew.getId() != null && reviewNew.getStatus() == ReviewConstant.REVIEW_STATUS_PROCESSING) {
				Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewNew.getId());
				if (reviewDetailMap != null) {
					//转款金额uuid（在新审批moneyAmount表）
					transferMoneyUuid = reviewDetailMap.get(Context.REVIEW_VARIABLE_KEY_TRANSFER_MONEY_UUID).toString();
				}
			}
			//查询出amount并添加到集合
			if (StringUtils.isNotBlank(transferMoneyUuid)) {				
				newMoneyAmounts.addAll(newProcessMoneyAmountDao.findAmountBySerialNum(transferMoneyUuid));
			}
		}
		
		return newMoneyAmounts;
	}

	/**
	 * 订单累计申请转款金额（多币种）初始化累计金额 -- 审批中金额
	 * TODO
	 * @author yang.jiang 2015年12月10日 16:18:18
	 * @param processingMoneyList 所有审批中转款金额
	 * @param accountedMoneyList 到账金额
	 * @return
	 */
	public List<Map<String, Object>> initialTotalApplyMoney(List<NewProcessMoneyAmount> processingMoneyList, List<MoneyAmount> accountedMoneyList) {
		// TODO Auto-generated method stub
		//组织累计金额的基础结构
		List<Map<String, Object>> totalApplyMoneyList = Lists.newArrayList();
		//获取达账金额的所有币种，去重
		Set<Integer> tempCurr = Sets.newHashSet();
		for (MoneyAmount mount : accountedMoneyList) {
			tempCurr.add(mount.getCurrencyId());
		}
		//如果参数为空，则返回空币种，0值
		if (CollectionUtils.isNotEmpty(processingMoneyList)) {
			//获取所有审批中转款金额的所有币种，去重
			for (NewProcessMoneyAmount newAmount : processingMoneyList) {
				tempCurr.add(newAmount.getCurrencyId());
			}			
		}
		//为每种货币添加 0 值
		for (Integer curr : tempCurr) {
			Map<String, Object> tempAmount = Maps.newHashMap();
			tempAmount.put("currencyId", curr);
			tempAmount.put("amount", BigDecimal.ZERO);
			totalApplyMoneyList.add(tempAmount);
		}
		//遍历审批中金额，分币种累加金额
		if (CollectionUtils.isNotEmpty(processingMoneyList)) {
			for (NewProcessMoneyAmount newAmount : processingMoneyList) {
				for (Map<String, Object> everyCurrencyAmount : totalApplyMoneyList) {
					if (Integer.valueOf(everyCurrencyAmount.get("currencyId").toString()) == newAmount.getCurrencyId()) {
						everyCurrencyAmount.put("amount", new BigDecimal(everyCurrencyAmount.get("amount").toString()).add(new BigDecimal(newAmount.getAmount())));  //金额
					}
				}
			}
		}

		return totalApplyMoneyList;
	}
	
	

}
