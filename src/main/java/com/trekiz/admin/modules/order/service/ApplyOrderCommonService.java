package com.trekiz.admin.modules.order.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.mapper.BeanMapper;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.PreProductOrderCommon;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.PreProductOrderCommonDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.stock.repository.ActivityGroupReserveDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

@Service
@Transactional(readOnly = true)
public class ApplyOrderCommonService  extends BaseService {
	@Autowired
    private PreProductOrderCommonDao preproductorderDao;
	@Autowired
	private OrderContactsDao orderContactsDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
    private AgentinfoDao agentinfoDao;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
	private OrderCommonService orderCommonService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private ActivityGroupReserveDao activityGroupReserveDao;
	@Autowired
    private OrderStockService orderStockService;
	
	public PreProductOrderCommon findOne(Long id){
		return preproductorderDao.findOne(id);
	}
	
	/**
     * 根据分组id查询报名人数
     * @param orderNum
     * @return
     */
    public List<Object[]> sumOrderPersonNumByGroupId(Long groupId) {
    	StringBuilder sql =  new StringBuilder(" select IFNULL(sum(orderPersonNum),0),sum(payMode) from preproductorder where orderType = 0 and productGroupId = ").append(groupId);
		return preproductorderDao.findBySql(sql.toString());
	}
    
	
	 /**
     * 根据预报名订单编号查询预报名订单
     * @param orderNum
     * @return
     */
    public List<PreProductOrderCommon> findByPreOrderNum(String orderNum) {
    	String companyUuid = UserUtils.getUser().getCompany().getUuid();
    	return preproductorderDao.findByOrderNum(orderNum, companyUuid);
    }
	
	/**
     * 根据订单ID修改订单类型
     * @param orderType 订单类型
     * @param id 订单ID
     */
    public void updateOrderTypeById(Integer orderType, Long id){
    	preproductorderDao.updateOrderTypeById(orderType, id);
    }
    
    /**
     *  功能:  点击预订按钮后  保存简单的预订信息
     *
     *  @author xuziqian
     *  @DateTime 2014-1-16 上午11:48:03
     *  @param productOrder   订单对象
     *  @param productId    产品id
     *  @param productGroupId   产品对应团期id
     *  @param payMode   订单付款方式
     */
    @Transactional
    public PreProductOrderCommon savePreProductorderOnReserve (PreProductOrderCommon productOrder, TravelActivity activity, String agentId) {
        productOrder.setProductId(activity.getId());
        productOrder = OrderCommonUtil.setPreOrderRemainDays(productOrder, activity);
        if(StringUtils.isNotBlank(agentId)){
        	Agentinfo agentinfo = agentinfoDao.findOne(Long.parseLong(agentId));
	        if(agentinfo != null){
	          productOrder.setOrderCompany(agentinfo.getId());
	          productOrder.setOrderCompanyName(agentinfo.getAgentName());
	        }
        }
        return productOrder;
    }
    
    /**
     *  功能:  预报名 保存旅客信息   联系人信息
     *
     *  @author xuziqian
     *  @DateTime 2014-1-16 下午9:23:03
     *  @param list
     *  @param listcontact
     *  @throws OptimisticLockHandleException 
     *  @throws PositionOutOfBoundException 
     */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
    public Map<String, Object> saveApplyOrder(PreProductOrderCommon productOrder, List<OrderContacts> orderContacts, int orderPersonNum)
            throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        Integer srcOrderPersonNum = productOrder.getOrderPersonNum();
        ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
        BigDecimal adultPrice = activityGroup.getSettlementAdultPrice() == null ? BigDecimal.ZERO : activityGroup.getSettlementAdultPrice();
    	BigDecimal childPrice = activityGroup.getSettlementcChildPrice() == null ? BigDecimal.ZERO : activityGroup.getSettlementcChildPrice();
    	BigDecimal specialPrice = activityGroup.getSettlementSpecialPrice() == null ? BigDecimal.ZERO : activityGroup.getSettlementSpecialPrice();
    	String currency = activityGroup.getCurrencyType();
    	String payDepositCurrencyId = null;
    	String adultCurrencyId = null;
	    String childCurrencyId = null;
	    String specialCurrencyId = null;
	    String singleDiffCurrencyId = null;
	    String[] currencyArr = {"1","1","1","1","1","1","1","1"};
	    if(currency != null){
	    	currencyArr = currency.split(",");
	    }
    	adultCurrencyId = currencyArr[0];
		childCurrencyId = currencyArr[1];
		specialCurrencyId = currencyArr[2];
        if(productOrder.getId() == null){
	    	if(productOrder.getOrderStatus() == 2){
	    		singleDiffCurrencyId = currencyArr[6];
	    		payDepositCurrencyId = currencyArr[7];
	    	}else{
	    		singleDiffCurrencyId = currencyArr[3];
	    		payDepositCurrencyId = currencyArr[4];
	    	}
        	String settlementAdultPriceSeriNum = UUID.randomUUID().toString();
        	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(settlementAdultPriceSeriNum, Integer.parseInt(adultCurrencyId), adultPrice, null, null, null, 1, UserUtils.getUser().getId()));
            productOrder.setSettlementAdultPrice(settlementAdultPriceSeriNum);
            String settlementChildPriceSeriNum = UUID.randomUUID().toString();
            moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(settlementChildPriceSeriNum, Integer.parseInt(childCurrencyId), childPrice, null, null, null, 1, UserUtils.getUser().getId()));
            productOrder.setSettlementcChildPrice(settlementChildPriceSeriNum);
            String settlementSpecialPriceSeriNum = UUID.randomUUID().toString();
            moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(settlementSpecialPriceSeriNum, Integer.parseInt(specialCurrencyId), specialPrice, null, null, null, 1, UserUtils.getUser().getId()));
            productOrder.setSettlementSpecialPrice(settlementSpecialPriceSeriNum);
            String singleDiffSeriNum = UUID.randomUUID().toString();
            moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(singleDiffSeriNum, Integer.parseInt(singleDiffCurrencyId), activityGroup.getSingleDiff() == null ? BigDecimal.ZERO : activityGroup.getSingleDiff(), null, null, null, 1, UserUtils.getUser().getId()));
            productOrder.setSingleDiff(singleDiffSeriNum);
            String payDepositSeriNum = UUID.randomUUID().toString();
            moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(payDepositSeriNum, Integer.parseInt(payDepositCurrencyId), activityGroup.getPayDeposit() == null ? BigDecimal.ZERO : activityGroup.getPayDeposit(), null, null, null, 1, UserUtils.getUser().getId()));
            productOrder.setPayDeposit(payDepositSeriNum);
            BigDecimal frontMoney = StringNumFormat.getBigDecimalMultiply(new BigDecimal(orderPersonNum),activityGroup.getPayDeposit());
            String frontMoneySeriNum = UUID.randomUUID().toString();
        	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(frontMoneySeriNum, Integer.parseInt(payDepositCurrencyId), frontMoney, null, null, null, 1, UserUtils.getUser().getId()));
        	productOrder.setFrontMoney(frontMoneySeriNum);
        }
        else{
        	if(orderPersonNum != srcOrderPersonNum){
	    		List<MoneyAmount> payDepositAmountList = moneyAmountService.findAmountBySerialNum(productOrder.getPayDeposit());
	    		BigDecimal frontMoney = StringNumFormat.getBigDecimalMultiply(new BigDecimal(orderPersonNum),payDepositAmountList.get(0).getAmount());
	    		List<MoneyAmount> frontMoneyAmountList = moneyAmountService.findAmountBySerialNum(productOrder.getFrontMoney());
	    		for(MoneyAmount moneyAmount: frontMoneyAmountList){
	    			moneyAmount.setAmount(frontMoney);
	    		}
	    		moneyAmountService.saveOrUpdateMoneyAmounts(productOrder.getFrontMoney(), frontMoneyAmountList);
        	}
        }
        //计算订单总额即应付价
        Map<String,BigDecimal> totalMoneyMap = new HashMap<String, BigDecimal>();
    	if(productOrder.getOrderPersonNumAdult() > 0){
    		totalMoneyMap.put(adultCurrencyId, adultPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumAdult())));
    	}
    	if(productOrder.getOrderPersonNumChild() > 0){
        	BigDecimal childTotalMoney = childPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumChild()));
        	if(!totalMoneyMap.containsKey(childCurrencyId)){
        		totalMoneyMap.put(childCurrencyId, childTotalMoney);
        	}else{
        		totalMoneyMap.put(childCurrencyId, totalMoneyMap.get(childCurrencyId).add(childTotalMoney));
        	}
    	}
    	if(productOrder.getOrderPersonNumSpecial() > 0){
        	BigDecimal specialTotalMoney = specialPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()));
        	if(!totalMoneyMap.containsKey(specialCurrencyId)){
        		totalMoneyMap.put(specialCurrencyId, specialTotalMoney);
        	}else{
        		totalMoneyMap.put(specialCurrencyId, totalMoneyMap.get(specialCurrencyId).add(specialTotalMoney));
        	}
    	}
    	productOrder.setTotalMoney(UUID.randomUUID().toString());
    	productOrder.setOriginalTotalMoney(UUID.randomUUID().toString());
        productOrder.setOrderType(0);
        productOrder.setOrderPersonNum(orderPersonNum);
        saveOrder(productOrder, totalMoneyMap, orderContacts);
//        List<MoneyAmount> moneyAmountList= Lists.newArrayList();
//		MoneyAmount moneyAmount = null;
//		List<MoneyAmount> oldMoneyAmountList= Lists.newArrayList();
//		MoneyAmount oldMoneyAmount = null;
//		String totalMoneySerialNum = productOrder.getTotalMoney();
//		String oldTotalMoneySerialNum = productOrder.getOriginalTotalMoney();
//		Long orderId = productOrder.getId();
//		Long userId = UserUtils.getUser().getId();
//		int orderStatus = productOrder.getOrderStatus();
//		for (String key : totalMoneyMap.keySet()) {
//			int currencyId = Integer.parseInt(key);
//			BigDecimal money = totalMoneyMap.get(key);
//			moneyAmount = new MoneyAmount(totalMoneySerialNum, currencyId, money, orderId, 13, orderStatus, 1, userId);
//			moneyAmountList.add(moneyAmount);
//			oldMoneyAmount = new MoneyAmount(oldTotalMoneySerialNum, currencyId, money, orderId, 21, orderStatus, 1, userId);
//			oldMoneyAmountList.add(oldMoneyAmount);
//		}
//		moneyAmountService.saveOrUpdateMoneyAmounts(totalMoneySerialNum, moneyAmountList);
//		moneyAmountService.saveOrUpdateMoneyAmounts(oldTotalMoneySerialNum, oldMoneyAmountList);
//        preproductorderDao.save(productOrder);
        mapResult.put("productOrder", productOrder);
        return mapResult;
    }
	
	 /**
	  * 功能:  预报名 转正式订单 
	  * @author taoxiaoyang
      * @DateTime 2014-12-19
	  * @param applyOrderId
	  * @param productOrder
	  * @param request
	  * @return
	  * @throws OptimisticLockHandleException
	  * @throws PositionOutOfBoundException
	  * @throws Exception
	  */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
    public Map<String, Object> saveApplyToOrder(Long applyOrderId, ProductOrderCommon productOrder, HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {    	
    	Map<String, Object> mapResult = new HashMap<String, Object>();
 		productOrder.setPayedMoney(UUID.randomUUID().toString());
 		productOrder.setAccountedMoney(UUID.randomUUID().toString()); 
        
		// 扣减余位
		orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.CREATE);
        
		orderCommonService.saveProductorder(productOrder);
		Long orderId = productOrder.getId();
		String currencyId = request.getParameter("currencyId");
		String currencyPrice = request.getParameter("currenctPrice");
		String[] currencyIdArr = currencyId.split(",");
		String[] currencyPrcieArr = currencyPrice.split(",");
		List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		MoneyAmount moneyAmount = null;
		for(int i = 0; i < currencyIdArr.length; i++){
			moneyAmount = new MoneyAmount(productOrder.getTotalMoney(), Integer.parseInt(currencyIdArr[i]), 
					new BigDecimal(currencyPrcieArr[i]), orderId, 13, 
					productOrder.getOrderStatus(), 1, UserUtils.getUser().getId());
			moneyAmountList.add(moneyAmount);
		}
		moneyAmountService.saveOrUpdateMoneyAmounts(productOrder.getTotalMoney(), moneyAmountList);
		
		orderContactsDao.updateOrderContactsByApplyOrderId(orderId, applyOrderId);
		travelerDao.updateTravelerByApplyOrderId(orderId, applyOrderId);
		//把已报名订单修改未已转正
		preproductorderDao.updateOrderTypeById(1, applyOrderId);
		mapResult.put("productOrder", productOrder);
		return mapResult;
    }
	
	/**
	 * 保存订单及订单费用
	 * @param productOrder 订单实体
	 * @param moneyAmountList 订单应收价
	 * @param oldMoneyAmountList 订单原始应收价
	 * @return
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public PreProductOrderCommon saveOrder(PreProductOrderCommon productOrder, Map<String,BigDecimal> totalMoneyMap, List<OrderContacts> orderContacts){
		//保存预报名订单
		preproductorderDao.save(productOrder);
		
		//保存预报名订单费用
    	List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		MoneyAmount moneyAmount = null;
		List<MoneyAmount> oldMoneyAmountList= Lists.newArrayList();
		MoneyAmount oldMoneyAmount = null;
		String totalMoneySerialNum = productOrder.getTotalMoney();
		String oldTotalMoneySerialNum = productOrder.getOriginalTotalMoney();
		Long orderId = productOrder.getId();
		Long userId = UserUtils.getUser().getId();
		int orderStatus = productOrder.getOrderStatus();
		for (String key : totalMoneyMap.keySet()) {
			int currencyId = Integer.parseInt(key);
			BigDecimal money = totalMoneyMap.get(key);
			moneyAmount = new MoneyAmount(totalMoneySerialNum, currencyId, money, orderId, Context.MONEY_TYPE_YSH, orderStatus, 1, userId);
			moneyAmountList.add(moneyAmount);
			oldMoneyAmount = new MoneyAmount(oldTotalMoneySerialNum, currencyId, money, orderId, Context.MONEY_TYPE_YSYSH, orderStatus, 1, userId);
			oldMoneyAmountList.add(oldMoneyAmount);
		}
		moneyAmountService.saveOrUpdateMoneyAmounts(totalMoneySerialNum, moneyAmountList);
		moneyAmountService.saveOrUpdateMoneyAmounts(oldTotalMoneySerialNum, oldMoneyAmountList);
		
		//预定联系人 循环处理
	    if(orderContacts != null && orderContacts.size() > 0){
	    	StringBuffer contactsName = saveOrderContacts(orderContacts, orderId, 0);
        	productOrder.setOrderPersonName(contactsName.toString());
	    }
		return productOrder;
	}
	
	/**
     * 保存预定联系人
     * @param OrderContactsJSON  联系人数据 json
     * @param orderId	订单id
     */
    private StringBuffer saveOrderContacts(List<OrderContacts> contactsList, Long orderId, int orderType){
    	
    	StringBuffer contactsName = new StringBuffer(",");
    	/**
    	 * 删除订单原有联系人
    	 */
    	orderContactsDao.deleteOrderContactsByOrderIdAndOrderType(orderId, orderType);
    	
    	for(OrderContacts contacts : contactsList){
    		if (StringUtils.isNotBlank(contacts.getContactsName())) {
    			contactsName.append(contacts.getContactsName() + ",");
    		}
    		if(contacts.getId() != null){
                OrderContacts loadcontact = orderContactsDao.findOne(contacts.getId());
                try {
                    BeanMapper.copy(contacts, loadcontact);
                    contacts = loadcontact;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("拷贝对象错误"+e.getMessage());
                }
            }
            contacts.setOrderId(orderId);
            contacts.setOrderType(orderType);
    		orderContactsDao.save(contacts);
    	}
    	return contactsName;
    }
}
