package com.trekiz.admin.modules.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.TransFerGroup;
import com.trekiz.admin.modules.order.entity.TransferMoneyAppShow;
import com.trekiz.admin.modules.order.entity.TransferMoneyApplyForm;
import com.trekiz.admin.modules.order.entity.TransferMoneyShowBean;
import com.trekiz.admin.modules.order.entity.TravelerShowBean;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;


@Service
@Transactional(readOnly = true)
public class TransferMoneyService   extends BaseService {

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
	

	public Map<String, Object> getResultMap(Map<String, String> condition) {
		// TODO Auto-generated method stub
		String orderId = condition.get("orderId");
		Map<String, Object> result = new HashMap<String, Object>();
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
		//转入订单详情(newOrder)
		
		List<TransferMoneyShowBean> listOutBean = new ArrayList<TransferMoneyShowBean>();
		List<TravelerShowBean> listTraveler = new ArrayList<TravelerShowBean>();
		List<Review> listReview = reviewService.findReview(productOrder.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, orderId, Context.REVIEW_ACTIVE_EFFECTIVE);
		for(Review review:listReview){
			if(Context.REVIEW_STATUS_DONE!=review.getStatus()){
				continue;
			}
			Map<String,String> map = reviewService.findReview(review.getId());
			TransferMoneyShowBean outBean = new TransferMoneyShowBean();
			String newOrderId = map.get(TransFerGroup.KEY_NEW_ORDERID);
			if(StringUtils.isBlank(newOrderId)){
				result.put("err", "找不到新订单id");
				return result;
			}
			ProductOrderCommon order = productorderDao.findOne(Long.parseLong(newOrderId));
			outBean.setProductOrderCommon(order);
			List<ActivityGroup>  listGroup = activityGroupDao.findByGroupCodeAndCompany(map.get(TransFerGroup.KEY_NEW_GROUPCODE), UserUtils.getUser().getCompany().getId());
			if(CollectionUtils.isEmpty(listGroup)){
				result.put("err", "找不到转入团信息");
				return result;
			}
			ActivityGroup  group  = listGroup.get(0);
//			ActivityGroup  group= activityGroupDao.findOne(Long.parseLong(map.get(TransFerGroup.KEY_NEW_GROUPCODE)));
			outBean.setActivitygroup(group);
			outBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
			listOutBean.add(outBean);
			Traveler traveler = travelerDao.findById(Long.parseLong(map.get(TransFerGroup.KEY_TRAVELERID)));
			TravelerShowBean bean = new TravelerShowBean();
			bean.setTraveler(traveler);
			bean.setGroupNo(group==null?"":group.getGroupCode());
			bean.setInOrderId(order.getId());
			bean.setOrderMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
			bean.setPayPriceMoney(moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum()));
			bean.setCurrencyList(currenceService.findCurrcyListBySerium(traveler.getPayPriceSerialNum()));
			listTraveler.add(bean);
		}
		
		List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		result.put("currencyList", currencyList);
		
		result.put("fromBean", showBean);
		result.put("listOutBean", listOutBean);
		result.put("listTraveler", listTraveler);
		return result;
	}
	
	/**
	 * 依据orderid获取旧转团信息，组织新转款申请页面数据
	 * @author yang.jiang 2016-01-11 11:03:06
	 * @param condition
	 * @return
	 */
	public Map<String, Object> getResultMapOld2New(Map<String, String> condition) {
		// TODO Auto-generated method stub
		String orderId = condition.get("orderId");
		Map<String, Object> result = new HashMap<String, Object>();
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
		List<Review> listReview = reviewService.findReview(productOrder.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, orderId, Context.REVIEW_ACTIVE_EFFECTIVE);
		for(Review review:listReview){
			if(Context.REVIEW_STATUS_DONE!=review.getStatus()){
				continue;
			}
			Map<String,String> map = reviewService.findReview(review.getId());
			TransferMoneyShowBean outBean = new TransferMoneyShowBean();
			String newOrderId = map.get(TransFerGroup.KEY_NEW_ORDERID);
			if(StringUtils.isBlank(newOrderId)){
				result.put("err", "找不到新订单id");
				return result;
			}
			ProductOrderCommon order = productorderDao.findOne(Long.parseLong(newOrderId));
			outBean.setProductOrderCommon(order);
			List<ActivityGroup>  listGroup = activityGroupDao.findByGroupCodeAndCompany(map.get(TransFerGroup.KEY_NEW_GROUPCODE), UserUtils.getUser().getCompany().getId());
			if(CollectionUtils.isEmpty(listGroup)){
				result.put("err", "找不到转入团信息");
				return result;
			}
			ActivityGroup  group  = listGroup.get(0);
//			ActivityGroup  group= activityGroupDao.findOne(Long.parseLong(map.get(TransFerGroup.KEY_NEW_GROUPCODE)));
			outBean.setActivitygroup(group);
			outBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
			if (StringUtils.isNotBlank(order.getAccountedMoney())) {
				outBean.setOrderAccountedMoney(moneyAmountService.getMoneyStr(order.getAccountedMoney()));
			}
			listOutBean.add(outBean);
			Traveler traveler = travelerDao.findById(Long.parseLong(map.get(TransFerGroup.KEY_TRAVELERID)));  //这是转团之前游客实体
			TravelerShowBean bean = new TravelerShowBean();
			bean.setTraveler(traveler);
			bean.setGroupNo(group==null?"":group.getGroupCode());
			bean.setInOrderId(order.getId());
			bean.setNewOrder(order);
			bean.setOrderMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
			bean.setOldPayPriceMoney(moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum()));  //旧结算
			//由于旧转团没有转入团游客id，故而依据游客类型和新订单的对应同行价来确定（彼时还没有直客价可报名的情况，忽略）
			String newPayPrice = ""; 
			if (Context.PERSON_TYPE_ADULT == traveler.getPersonType()) {
				newPayPrice = moneyAmountService.getMoneyStr(order.getSettlementAdultPrice());
			} else if (Context.PERSON_TYPE_CHILD == traveler.getPersonType()) {
				newPayPrice = moneyAmountService.getMoneyStr(order.getSettlementcChildPrice());
			} else if (Context.PERSON_TYPE_SPECIAL == traveler.getPersonType()) {
				newPayPrice = moneyAmountService.getMoneyStr(order.getSettlementSpecialPrice());
			}
			bean.setNewPayPriceMoney(newPayPrice);  //新结算
//			bean.setPayPriceMoney(moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum()));
			bean.setCurrencyList(currenceService.findCurrcyListBySerium(traveler.getPayPriceSerialNum()));
			listTraveler.add(bean);
		}
		
		List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		result.put("currencyList", currencyList);
		
		result.put("fromBean", showBean);
		result.put("listOutBean", listOutBean);
		result.put("listTraveler", listTraveler);
		return result;
	}
	
	//暂时使用旧转团数据，但是部分新转团业务，增加了到账金额
	public Map<String, Object> getResultMapUseAcc(Map<String, String> condition) {
		String orderId = condition.get("orderId");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("err", "suc");
		result.put("orderId", orderId);
		ProductOrderCommon productOrder = productorderDao.findOne(Long.parseLong(orderId));
		String accountedMoneyUuid = productOrder.getAccountedMoney();
		List<MoneyAmount> accountedMoneyList = moneyAmountService.findAmountBySerialNum(accountedMoneyUuid);
		String accountedMoneyStr = moneyAmountService.getMoneyStr(accountedMoneyUuid);
		List<Currency> accountedCurrency = currenceService.findCurrcyListBySerium(accountedMoneyUuid);
		
		ActivityGroup activityGroup = new ActivityGroup();
		if(productOrder!=null){
			activityGroup = activityGroupDao.findOne(productOrder.getProductGroupId());
		}
		//转出订单详情(oldOrder)
		TransferMoneyShowBean fromBean = new TransferMoneyShowBean();
		fromBean.setProductOrderCommon(productOrder);
		fromBean.setActivitygroup(activityGroup);
		fromBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(productOrder.getTotalMoney()));
		if (StringUtils.isNotBlank(productOrder.getAccountedMoney())) {
			fromBean.setOrderAccountedMoney(moneyAmountService.getMoneyStr(productOrder.getAccountedMoney()));
		}
		//转入订单详情(newOrder)
		
		List<TransferMoneyShowBean> listOutBean = new ArrayList<TransferMoneyShowBean>();
		List<TravelerShowBean> listTraveler = new ArrayList<TravelerShowBean>();
		List<Review> listReview = reviewService.findReview(productOrder.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, orderId, Context.REVIEW_ACTIVE_EFFECTIVE);
		for(Review review : listReview){
			if(Context.REVIEW_STATUS_DONE!=review.getStatus()){
				continue;
			}
			Map<String,String> map = reviewService.findReview(review.getId());
			TransferMoneyShowBean outBean = new TransferMoneyShowBean();
			String newOrderId = map.get(TransFerGroup.KEY_NEW_ORDERID);
			if(StringUtils.isBlank(newOrderId)){
				result.put("err", "找不到新订单id");
				return result;
			}
			ProductOrderCommon order = productorderDao.findOne(Long.parseLong(newOrderId));
			outBean.setProductOrderCommon(order);
			List<ActivityGroup>  listGroup = activityGroupDao.findByGroupCodeAndCompany(map.get(TransFerGroup.KEY_NEW_GROUPCODE), UserUtils.getUser().getCompany().getId());
			if(CollectionUtils.isEmpty(listGroup)){
				result.put("err", "找不到转入团信息");
				return result;
			}
			ActivityGroup  group  = listGroup.get(0);
//			ActivityGroup  group= activityGroupDao.findOne(Long.parseLong(map.get(TransFerGroup.KEY_NEW_GROUPCODE)));
			outBean.setActivitygroup(group);
			outBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
			if (StringUtils.isNotBlank(productOrder.getAccountedMoney())) {				
				outBean.setOrderAccountedMoney(moneyAmountService.getMoneyStr(order.getAccountedMoney()));
			}
			listOutBean.add(outBean);
			Traveler traveler = travelerDao.findById(Long.parseLong(map.get(TransFerGroup.KEY_TRAVELERID)));
			TravelerShowBean bean = new TravelerShowBean();
			bean.setTraveler(traveler);
			bean.setGroupNo(group==null?"":group.getGroupCode());
			bean.setInOrderId(order.getId());
			bean.setOrderMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
			bean.setPayPriceMoney(moneyAmountService.getMoneyStr(traveler.getPayPriceSerialNum()));
			bean.setCurrencyList(currenceService.findCurrcyListBySerium(traveler.getPayPriceSerialNum()));			
			listTraveler.add(bean);
		}
		
//		List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		//使用订单到账金额存在的币种
		result.put("currencyList", accountedCurrency);
		result.put("accountedMoneyStr", accountedMoneyStr);
		result.put("accountedMoneyList", accountedMoneyList);
		result.put("fromBean", fromBean);
		result.put("listOutBean", listOutBean);
		result.put("listTraveler", listTraveler);
		return result;
	}

	public Map<String, String> transfersMoneyApplySub(TransferMoneyApplyForm appForm) {
		// TODO Auto-generated method stub
		String orderId = appForm.getOrderId();
		ProductOrderCommon order = productorderDao.findOne(Long.parseLong(orderId));
		String[] tranMoneys = appForm.getTransferMoney();
		Integer[] travolerIds = appForm.getTravelorId();
		String[] travolerNames = appForm.getTravelorName();
		Long[] inOrderIds = appForm.getInOrderId();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res", "success");
		List<Review> transferGroupReviewList = reviewService.findReview(order.getOrderStatus(),Context.REVIEW_FLOWTYPE_TRANSFER_GROUP,orderId,Context.REVIEW_STATUS_DONE,Context.REVIEW_ACTIVE_EFFECTIVE);
		//所属部门Id
		Long depId = transferGroupReviewList.get(0).getDeptId();
		
		if(inOrderIds==null){
			resultMap.put("res", "未找到转入团信息");
			return resultMap	;
		}
		
		if(tranMoneys==null||tranMoneys.length==0){
			resultMap.put("res", "请填写要申请转款的金额");
			return resultMap	;
		}
		//判断所有money 如果都为0 则说明没有添值
		boolean t = false;
		for(int i = 0 ;i<inOrderIds.length;i++){
			//判断money是否存在
			List<Detail> detailsList = new ArrayList<Detail>();
			String bigMoneys = tranMoneys[i];
			if(StringUtils.isBlank(bigMoneys)||bigMoneys.trim().equals("0")){
				continue;
			}
			t = true;
			String uuid = UUID.randomUUID().toString();
			bigMoneys = bigMoneys.replaceFirst(":", "");
			int size = bigMoneys.split(":").length;
			String[] moneys = bigMoneys.split(":");
			ProductOrderCommon inOrder = productorderDao.findOne(inOrderIds[i]);
			for(int j=0;j<size;j++){
				String[] money = moneys[j].split("\\|");
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(uuid);
				amount.setAmount(new BigDecimal(money[1]));
				amount.setCreatedBy(UserUtils.getUser().getId());
				amount.setCreateTime(new Date());
				amount.setCurrencyId(Integer.parseInt(money[0]));
				amount.setBusindessType(1);
				amount.setOrderType(order.getOrderStatus());
				amount.setUid(inOrder.getId());
//				amount.setMoneyType(4and5);
				moneyAmountService.saveOrUpdateMoneyAmount(amount);
			}
			//比较金额  
//			ProductOrderCommon inOrder = productorderDao.findOne(inOrderIds[i]);
//			if(inOrder==null){
//				resultMap.put("res", "找不到转入订单信息-"+inOrderIds[i]);
//				return resultMap;
//			}
//			int check = moneyAmountService.checkTransferMoney(inOrder.getTotalMoney(),uuid );
			Traveler traveler = travelerDao.findOne( (long)travolerIds[i]) ;
			if(traveler==null){
				resultMap.put("res", "找不到游客信息-"+travolerIds[i]);
				return resultMap;
			}
//			int check = moneyAmountService.checkTransferMoney(traveler.getPayPriceSerialNum(),uuid );
//			if(check==1){
//				resultMap.put("res",  travolerNames[i]+"的转款金额不能大于订单金额，请重新填写");
//				return resultMap;
//			}

			
			 detailsList.add(new Detail( TransferMoneyApplyForm.KEY_REFUND_MONEY,  uuid));
			 detailsList.add(new Detail(TransferMoneyApplyForm.KEY_OLD_ORDERID,orderId));
			 detailsList.add(new Detail(TransferMoneyApplyForm.KEY_NEW_ORDERID,String.valueOf(inOrderIds[i])));
			 detailsList.add(new Detail(TransferMoneyApplyForm.KEY_TRAVELERID,String.valueOf(travolerIds[i])));
			 detailsList.add(new Detail(TransferMoneyApplyForm.KEY_TRAVELERNAME,String.valueOf(travolerNames[i])));
//			Long rid =  reviewService.addReview(travelactivity.getActivityKind(),  Context.REVIEW_FLOWTYPE_TRANSFER_MONEY, 
//					 orderId, (long)travolerIds[i],(long)0, "转团转款申请", null, detailsList);
			StringBuffer replayMsg = new StringBuffer();
			Long rid =  reviewService.addReview(order.getOrderStatus(),  Context.REVIEW_FLOWTYPE_TRANSFER_MONEY, 
					 orderId, (long)travolerIds[i],(long)0, "转团转款申请", replayMsg, detailsList,depId);
			if(rid==0){
				resultMap.put("res", replayMsg.toString());
				return resultMap;
			}
		}
		if(!t){
			resultMap.put("res", "请填写要申请转款的金额");
		}
		
		return resultMap;
	}
	
	public List<TransferMoneyAppShow> getTransferMoneyAppList(Map<String, String> condition) {
		// TODO Auto-generated method stub
		String orderId = (String)condition.get("orderId");
		ProductOrderCommon order = productorderDao.findOne(Long.parseLong(orderId));
		List<Review> reviewList = reviewService.findReview(order.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_MONEY, orderId, Context.REVIEW_ACTIVE_INEFFECTIVE);
		List<TransferMoneyAppShow> listShow = new ArrayList<TransferMoneyAppShow>();
		for(Review view:reviewList){
			TransferMoneyAppShow show  = new TransferMoneyAppShow();
			show.setReview(view);
			listShow.add(show);
		}
		return listShow;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public Page<Map<String, Object>> getTransferMoneyAppList(HttpServletRequest request, HttpServletResponse response, User user, Map<String, String> condition) {
		// TODO Auto-generated method stub
			Page<Map<String,Object>> page =  reviewService.getTransferMoneyAppList(request,response,UserUtils.getUser(),condition);
			List list = page.getList();
			List list1 = new ArrayList();
			for(int i = 0 ;i<list.size();i++){
				TransferMoneyAppShow show = new TransferMoneyAppShow();
				Review review = (Review)list.get(i);
				Map detailsMap= reviewService.findReview(review.getId());
				String newOrderId = (String)detailsMap.get(TransferMoneyApplyForm.KEY_NEW_ORDERID);
				ProductOrderCommon newOrder =productorderDao.findOne(Long.parseLong(newOrderId==null?"0":newOrderId));
				show.setReview(review);
				show.setTraveler(travelerDao.findOne(review.getTravelerId()));
				show.setNewOrder(newOrder);
				show.setOrderTotalMoney(moneyAmountService.getMoneyStr(newOrder==null?"":newOrder.getTotalMoney()));
				show.setTransMoney(moneyAmountService.getMoneyStr((String)detailsMap.get(TransferMoneyApplyForm.KEY_REFUND_MONEY)));
				list1.add(show);
			}
			page.setList(list1);
			return page;
	}

	public Map<String, String> transfersMoneyCancel(@SuppressWarnings("rawtypes") Map condition) {
		// TODO Auto-generated method stub
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res", "success");
		String reviewId = (String)condition.get("reviewId");
		List<Review> reviewList  = reviewService.findReview(Long.parseLong(reviewId),  Context.REVIEW_ACTIVE_INEFFECTIVE);
		if(reviewList==null||reviewList.size()==0){
			resultMap.put("res", "未找到符合条件的申请记录");
			return resultMap;
		}
		Review review   =  reviewList.get(0);
		if(Context.REVIEW_STATUS_WAIT!=review.getStatus()){
			resultMap.put("res", "该申请记录状态不符合要求，请刷新界面重试");
			return resultMap;
		}
		review.setStatus(Context.REVIEW_STATUS_CANCEL);
		review.setActive(0);
		reviewService.updateRivew(review);
		return resultMap;
	}
	
	/*
	 * 转团转款审批通过
	 * param: reviewId 
	 * return err 错误信息 success:成功 else: 错误信息
	 */
	@SuppressWarnings("rawtypes")
    public String transferMoneyApplyDone(Long reviewId){
		String err = "success";
		Review review = reviewService.findReviewInfo(reviewId);
		if(review==null){
			err="找不到对应的审批信息";
			return err;
		}
		Map reviewDetials = reviewService.findReview(reviewId);
		String newOrderId = (String)reviewDetials.get(TransferMoneyApplyForm.KEY_NEW_ORDERID);
		String oldOrderId =  (String)reviewDetials.get(TransferMoneyApplyForm.KEY_OLD_ORDERID);
		String uuid =  (String)reviewDetials.get(TransferMoneyApplyForm.KEY_REFUND_MONEY);
		//`payStatus` tinyint(4) DEFAULT NULL COMMENT '支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消',
		ProductOrderCommon oldOrder = productorderDao.findOne(Long.parseLong(oldOrderId));
		ProductOrderCommon newOrder = productorderDao.findOne(Long.parseLong(newOrderId));
		newOrder.setPayStatus(oldOrder.getPayStatus());
		productorderDao.getSession().update(newOrder);
		
		
		err = moneyAmountService.transferMoney(Long.parseLong(newOrderId), Long.parseLong(oldOrderId), uuid, review.getProductType());
		return err;
	}

	public Map<String, Object> transferMoneyDetails(Map<String, Long> condition) {
		Map<String, Object> map = new HashMap<String, Object>();
		Long reviewId = condition.get("reviewId");
		String err = "success";
		Review review = reviewService.findReviewInfo(reviewId);
		if(review==null){
			err="找不到对应的审批信息";
			map.put("err", err);
			return map;
		}
		Map<String, String> reviewDetials = reviewService.findReview(reviewId);
		String newOrderId = reviewDetials.get(TransferMoneyApplyForm.KEY_NEW_ORDERID);
		String oldOrderId =  reviewDetials.get(TransferMoneyApplyForm.KEY_OLD_ORDERID);
		String travelId =  reviewDetials.get(TransferMoneyApplyForm.KEY_TRAVELERID);
		String uuid =  reviewDetials.get(TransferMoneyApplyForm.KEY_REFUND_MONEY);
		
		ProductOrderCommon productOrder = productorderDao.findOne(Long.parseLong(oldOrderId));
		ActivityGroup activityGroup = null;
		if(productOrder!=null){
			activityGroup = activityGroupDao.findOne(productOrder.getProductGroupId());
		}
		//转出订单详情(oldOrder)
		TransferMoneyShowBean oldBean = new TransferMoneyShowBean();
		oldBean.setProductOrderCommon(productOrder);
		oldBean.setActivitygroup(activityGroup);
		oldBean.setOrderTotalMoney(moneyAmountService.getMoneyStr(productOrder.getTotalMoney()));
		
		//转入订单详情(newOrder)
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
		Traveler traveler = travelerDao.findById(Long.parseLong(travelId));
		TravelerShowBean bean = new TravelerShowBean();
		bean.setTraveler(traveler);
		bean.setGroupNo(newGroup==null?"":newGroup.getGroupCode());
		bean.setInOrderId(newOrder.getId());
		bean.setOrderMoney(moneyAmountService.getMoneyStr(newOrder.getTotalMoney()));
		bean.setTransferMoney(moneyAmountService.getMoneyStr(uuid));
		map.put("traveler", bean);
		map.put("oldBean", oldBean);
		map.put("newBean", newBean);
		return map;
	}

	public Map<String, Object> transferMoneyReviewInfo(Map<String, Long> condition) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Long reviewId = condition.get("reviewId");
		Review review = reviewService.findReviewInfo(reviewId);
		List<ReviewLog> rLog=reviewService.findReviewLog(reviewId);
		map.put("review", review);//denyReason
		map.put("rLog", rLog);
		return map;
	}

	/**
	 * 转团申请操作，为一个或多个游客进行转团申请，如果申请失败，需要整体回滚。
	 * @author gao
	 * @param travelList 全部要转团的游客
	 * @param oldOrder 原订单实体
	 * @param  String[] remark 转团理由
	 * @param  String groupCode
	 * @param  TravelActivity ta 原产品实体
	 * @param  String remainDays 保留天数
	 * @param listDetail
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Map<String,String> addReviewList(List<Traveler> travelList ,ProductOrderCommon oldOrder,String[] remark,TravelActivity ta,
			String groupCode,String payType,String remainDays,StringBuffer reply, Map<String,String> map, String subtractMoneys) throws Exception {
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
					}
				}
				
			}
		}
		
		for(int n=0;n<travelList.size();n++){
			Traveler t = travelList.get(n);
			t.setSubtractMoneySerialNum(subtractMoneyMap.get(t.getId()));
			// 转团实体
			TransFerGroup tran = getTransFerGroup(t, oldOrder, n, remark, groupCode, payType, remainDays);
			List<Detail>  listDetail = getDetailList(tran);	// 申请细节
			// 提交申请(包含原产品部门)
			Long back = reviewService.addReview(oldOrder.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, oldOrder.getId().toString(), t.getId(), Long.valueOf(n), remark[n], reply, listDetail,ta.getDeptId());
			if(back==0){
				throw new Exception(reply.toString());
			}
		}
		map.put("res", "success");
		map.put("message", "申请成功，请等待审核");
		return map;
	}
	
	/**
	 * 获取申请细节
	 * @return
	 */
	private List<Detail> getDetailList(TransFerGroup bean){
		List<Detail> detailList = new ArrayList<Detail>();

		Map<String, String> map = bean.getReviewDetailMap();
		for (Entry<String, String> entry : map.entrySet()) {
			if(!"newOrderId".equals(entry.getKey())){
				detailList.add(new Detail(entry.getKey(), entry.getValue()));
			}
		}
		
		return detailList;
	}
	/**
	 * 创建申请实体类TransFerGroup
	 * @param traveler		游客实体
	 * @param order			原订单实体
	 * @param index			数组序号
	 * @param remarks		转团说明数组
	 * @param groupCode	新团期号
	 * @return
	 */
	private  TransFerGroup getTransFerGroup(Traveler traveler,ProductOrderCommon order,int index,String[] remarks,String groupCode,String payType,String remainDays){
		TransFerGroup bean = new TransFerGroup();
		// 游客ID
		if(traveler.getId()!=null){
			bean.setTravelerId(traveler.getId().toString());
		}
		// 游客姓名
		if(StringUtils.isNotBlank(traveler.getName())){
			bean.setTravelerName(traveler.getName());
		}
		// 老订单创建日期
		if(order.getCreateDate()!=null){
			bean.setCreateDate(order.getCreateDate());
		}
		// 申请日期
		bean.setApplyDate(new Date());
		// 转团说明
		if(StringUtils.isNotBlank(remarks[index])){
			bean.setRemark(remarks[index]);
		}
		// 应付金额
		if(StringUtils.isNotBlank(traveler.getPayPriceSerialNum())){
			String money = moneyAmountService.getMoney(traveler.getPayPriceSerialNum());
			bean.setMoney(money);
		}
		// 扣减金额
		if(StringUtils.isNotBlank(traveler.getSubtractMoneySerialNum())){
			String money = moneyAmountService.getMoney(traveler.getSubtractMoneySerialNum());
			bean.setSubtractMoney(money);
		}
		// 原订单ID
		if(order.getId()!=null){
			bean.setOldOrderId(order.getId().toString());
		}
		// 新团期
		if(StringUtils.isNotBlank(groupCode)){
			bean.setNewGroupCode(groupCode);
		}
		// 支付方式
		if(StringUtils.isNotBlank(payType)){
			bean.setPayType(payType);
		}
		// 保留天数
		if(StringUtils.isNotBlank(remainDays)){
			bean.setRemainDays(remainDays);
		}
		return bean;
	}
}
