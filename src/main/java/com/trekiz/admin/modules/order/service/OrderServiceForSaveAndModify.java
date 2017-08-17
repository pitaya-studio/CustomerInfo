package com.trekiz.admin.modules.order.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.agentToOffice.T1.order.entity.T1PreOrder;
import com.trekiz.admin.agentToOffice.T1.order.repository.T1PreOrderDao;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.mapper.BeanMapper;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordService;
import com.trekiz.admin.modules.groupCover.entity.GroupCover;
import com.trekiz.admin.modules.groupCover.repository.GroupCoverDao;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleDao;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleVisaDao;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandle;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandleVisa;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.OrderServiceCharge;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.pojo.OrderUnitPrice;
import com.trekiz.admin.modules.order.repository.CostchangeDao;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderServiceChargeDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.repository.TravelerVisaDao;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexService;

@Service
@Transactional(readOnly = true)
public class OrderServiceForSaveAndModify  extends BaseService {
	
	@Autowired
	private OrderCommonService orderService;
	@Autowired
    private AgentinfoService agentinfoService;
	@Autowired
    private MoneyAmountService moneyAmountService;
	@Autowired
	private OrderStockService orderStockService;
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@Autowired
    private EstimatePriceRecordService estimatePriceRecordService;
	@Autowired
    private OrderProgressTrackingService progressService;
	@Autowired
    private ReviewMutexService reviewMutexService;
	@Autowired
    private com.quauq.review.core.engine.ReviewService processReviewService;
	@Autowired
    private UserReviewPermissionChecker userReviewPermissionChecker;
	@Autowired
    private NewProcessMoneyAmountService processMoneyAmountService;
	
	@Autowired
    private ProductOrderCommonDao orderDao;
	@Autowired
	private UserDao userDao;
	@Autowired
    private ActivityGroupDao activityGroupDao;
	@Autowired
    private TravelActivityDao travelActivityDao;
	@Autowired
	private OfficeDao officeDao;
    @Autowired
    private GroupCoverDao groupCoverDao;
    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
    private OrderContactsDao orderContactsDao;
    @Autowired
    private TravelerDao travelerDao;
    @Autowired
    private GroupHandleDao groupHandleDao;
    @Autowired
    private GroupHandleVisaDao groupHandleVisaDao;
    @Autowired
    private TravelerVisaDao travelerVisaDao;
    @Autowired
	private OrderServiceChargeDao orderServiceChargeDao;
    @Autowired
    private CostchangeDao costchangeDao;
    @Autowired
    private T1PreOrderDao t1PreOrderDao;
    @Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;
	
	/**
	 *  功能:订单保存第一步
	 *	保存信息仅包括，订单基本信息及预定联系人
	 *	游客信息和特殊需求在第二部保存
	 *  @return
	 * @throws Exception 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String, Object> firstSave(Model model, HttpServletRequest request) 
			throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		
		String productOrderId = request.getParameter("productOrderId");
		// 创建订单对象
	    ProductOrderCommon productOrder = null;
        Long orderId = StringUtils.isNotBlank(productOrderId) ? Long.parseLong(productOrderId) : null;
	    if (orderId != null) {
	    	productOrder = orderDao.findOne(orderId);
	    } else {
	    	productOrder = new ProductOrderCommon();
	    	// 保存订单基本信息
	    	setOrderBaseInfo(productOrder, request);
	    }
	    
	    // 返回对象
	    Map<String, Object> result = null;
	    // 保存联运类型
	    setIntermodalType(productOrder);
	    // 保存订单人数
	    setOrderPersonNum(productOrder, request);
	    // 保存订单其他信息
	    String tempString = request.getParameter("orderContactsJSON");
		List<OrderContacts> contactsList = OrderUtil.getContactsList(tempString);
		result = saveOrder(productOrder, contactsList, request);


        return result;
	}
	
	/**
	 * 保存订单基本信息 
	 * @author yakun.bai
	 * @Date 2016-9-7
	 */
	private void setOrderBaseInfo(ProductOrderCommon productOrder, HttpServletRequest request) {
		
		// 获取页面参数值
		Integer activityKind = Integer.parseInt(request.getParameter("activityKind"));
	    
	    String productId = request.getParameter("productId");
	    String productGroupId = request.getParameter("productGroupId");
	    // 订单占位类型：0为普通占位；1为切位占位
	    String placeHolderType = request.getParameter("placeHolderType");
	    String payStatus = request.getParameter("payStatus");
	    String salerId = request.getParameter("salerId");
	    String createDate = request.getParameter("createDate");
	    // 是否为补单订单：0为否；1为是
	    String isAfterSupplement = request.getParameter("isAfterSupplement");
	    // 报名时使用的价格类型 ：0为同行价；1为直客价；2为quauq价
	    String priceType = request.getParameter("priceType");
	    // 渠道ID
	    Long orderCompany = Long.parseLong(request.getParameter("orderCompany"));
	    
    	// 如果是新创建订单，默认为审核中，当最后一步保存时，把订单状态改为正常
    	productOrder.setDelFlag(ProductOrderCommon.DEL_FLAG_AUDIT);
    	// 设置订单价格类型：0为同行价；1为直客价；2为quauq价
    	productOrder.setPriceType(StringUtils.isBlank(priceType) ? 0 : Integer.parseInt(priceType));
    	// 如果供应商不允许订单提醒，则新生成的订单都设置为已经查看的订单，否则设置为没查看订单
    	if (UserUtils.getUser().getCompany().getIsNeedAttention() == 0) {
    		productOrder.setSeenFlag(1);
    	} else {
    		productOrder.setSeenFlag(0);
    	}
    	// 设置订单渠道和渠道名称
    	productOrder.setOrderCompanyName(request.getParameter("orderCompanyName"));
        productOrder.setOrderCompany(orderCompany);
        // 设置订单的渠道类型：0 普通渠道；1 门店；2 总社；3 集团客户
        Agentinfo agent = agentinfoService.loadAgentInfoById(orderCompany);
        productOrder.setAgentType(Integer.parseInt(agent.getAgentType()));
        // 如果订单渠道为非签约渠道，则销售为当前用户，订单默认结算方式为即时结算
        if (orderCompany <= 0) {
        	productOrder.setOrderSaler(UserUtils.getUser());
        } else {
        	// 由于表结构发生变动, 从前台传入userId。 当获取不到时，取渠道第一销售
        	if (StringUtils.isNotBlank(salerId)) {
        		productOrder.setOrderSaler(userDao.getById(Long.parseLong(salerId)));					
			} else {
				List<User> salers = UserUtils.getUserListByIds(agent.getAgentSalerId());
				if (CollectionUtils.isNotEmpty(salers)) {						
					productOrder.setOrderSaler(salers.get(0));
				}
			}
        }
        // 订单默认结算方式为即时结算
        productOrder.setPaymentType(Context.PAYMENT_TYPE_JS);
        productOrder.setPlaceHolderType(Integer.parseInt(placeHolderType));
        String payMode = request.getParameter("payMode");
        String remainDays = request.getParameter("remainDays");
        productOrder.setPayMode(payMode);
        if (StringUtils.isNotBlank(remainDays)) {
        	productOrder.setRemainDays(Double.parseDouble(remainDays));
        }
        productOrder.setProductId(Long.parseLong(productId));
        if (StringUtils.isNotBlank(productGroupId)) {
        	productOrder.setProductGroupId(Long.parseLong(productGroupId));
        	ActivityGroup group = activityGroupDao.findById(Long.parseLong(productGroupId));
        	if (group != null) {
        		if (group.getLockStatus() != null && group.getLockStatus() == 1) {
        			productOrder.setSettleLockedIn(1);
        		}
        		if ("10".equals(group.getForcastStatus())) {
        			productOrder.setForecastLockedIn(1);
        		}
        	}
        }
	    productOrder.setOrderStatus(activityKind);
	    
	    // 设置订单编号
	    TravelActivity activity = travelActivityDao.findOne(Long.parseLong(productId));
	    String companyName = officeDao.getById(activity.getProCompany()).getName();
        String orderNum = sysIncreaseService.updateSysIncrease(companyName.length() > 3 ? companyName.substring(0, 3) : companyName, activity.getProCompany(),null, Context.ORDER_NUM_TYPE);
        productOrder.setOrderNum(orderNum);
        
        // 补单时订单会有页面传来创建时间，其他情况订单时间为当前时间
        if (StringUtils.isNotBlank(createDate)) {
        	createDate = createDate + " " + DateUtils.getTime();
        	productOrder.setOrderTime(DateUtils.parseDate(createDate));
        } else {
        	productOrder.setOrderTime(new Date());
        }
        if (StringUtils.isNotBlank(isAfterSupplement)) {
        	productOrder.setIsAfterSupplement(Integer.parseInt(isAfterSupplement));
        } else {
        	productOrder.setIsAfterSupplement(0);
        }
        // 询价相关
        if (null != activity.getEstimatePriceRecord()) {
        	estimatePriceRecordService.releaseOrder(activity.getEstimatePriceRecord().getId());
        }
	    if(StringUtils.isNotBlank(payStatus)){
        	productOrder.setPayStatus(Integer.parseInt(payStatus));
        }
        if(StringUtils.isNotBlank(salerId)){
        	productOrder.setSalerId(Integer.parseInt(salerId));
        	productOrder.setSalerName(UserUtils.getUserNameById(Long.parseLong(salerId)));
        }
	}
	
	
	/**
	 * 设置订单联运类型 
	 * @author yakun.bai
	 * @Date 2016-9-5
	 */
	private void setIntermodalType(ProductOrderCommon productOrder) {
		if (productOrder.getProductId() != null) {
	    	TravelActivity activity = travelActivityDao.findOne(productOrder.getProductId());
	    	if (activity.getActivityAirTicket() != null) {
	    		ActivityAirTicket airTicket = activity.getActivityAirTicket();
	    		if (airTicket.getIntermodalType() != null) {
	    			productOrder.setIntermodalType(airTicket.getIntermodalType().longValue());
	    		}
	    	}
	    }
	}
	
	/**
	 * 设置订单人数：总人数、成人数、儿童数、特殊人群
	 * @author yakun.bai
	 * @Date 2016-9-5
	 */
	private void setOrderPersonNum(ProductOrderCommon productOrder, HttpServletRequest request) {
		
		// 订单总人数
	    String orderPersonNum = request.getParameter("orderPersonNum");
	    // 订单人数：总人数、成人、儿童、特殊人群
	    String orderPersonNumAdult = request.getParameter("orderPersonNumAdult");
	    String orderPersonNumChild = request.getParameter("orderPersonNumChild");
	    String orderPersonNumSpecial = request.getParameter("orderPersonNumSpecial");
	    // 如果是游轮订单，则有房间数
	    String roomNumber = request.getParameter("roomNumber");
		
		productOrder.setOrderPersonNum(Integer.parseInt(orderPersonNum));
		
		if (StringUtils.isNotBlank(orderPersonNumChild)) {
	        productOrder.setOrderPersonNumChild(Integer.parseInt(orderPersonNumChild));
	    }
	    if (StringUtils.isNotBlank(orderPersonNumAdult)) {
	        productOrder.setOrderPersonNumAdult(Integer.parseInt(orderPersonNumAdult));
	    }
	    if (StringUtils.isNotBlank(orderPersonNumSpecial)) {
	    	productOrder.setOrderPersonNumSpecial(Integer.parseInt(orderPersonNumSpecial));
	    }
        
        productOrder.setRoomNumber(StringUtils.isNotBlank(roomNumber) ? Integer.parseInt(roomNumber) : 0);
	}
	
	/**
     * 订单保存或修改：订单价格和联系人保存
     * @param request
     * @return
     * @throws Exception
     */
	public Map<String, Object> saveOrder(ProductOrderCommon productOrder, List<OrderContacts> contactsList, HttpServletRequest request)
			throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
	   
		// 判断余位
		String groupCoverId = request.getParameter("groupCoverId");
		try {
			if (StringUtils.isBlank(groupCoverId)) {
				orderStockService.ifCanCut(productOrder, Context.StockOpType.CREATE);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		// 保存订单价格
		saveOrderPrice(productOrder, request);
		
		// 保存订单联系人
		setOrderContacts(productOrder, contactsList);

		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("productOrder", productOrder);
		mapResult.put("agentId", productOrder.getOrderCompany());
		return mapResult;
	}
	
	/**
	 * 保存订单联系人 
	 * @author yakun.bai
	 * @Date 2016-9-7
	 */
	private void setOrderContacts(ProductOrderCommon productOrder, List<OrderContacts> contactsList) {

		// 如果联系人不为空，则保存
		if (CollectionUtils.isNotEmpty(contactsList)) {

			Integer orderType = productOrder.getOrderStatus();

			// 删除订单原有联系人
			orderContactsDao.deleteOrderContactsByOrderIdAndOrderType(productOrder.getId(), orderType);

	    	// 订单联系人（以逗号分隔）
			StringBuffer contactsName = new StringBuffer(",");

			for (OrderContacts contacts : contactsList) {
				if (StringUtils.isNotBlank(contacts.getContactsName())) {
					contactsName.append(contacts.getContactsName() + ",");
				}
				if (contacts.getId() != null) {
					OrderContacts loadcontact = orderContactsDao.findOne(contacts.getId());
					try {
						BeanMapper.copy(contacts, loadcontact);
						contacts = loadcontact;
					} catch (Exception e) {
						logger.error("拷贝对象错误" + e.getMessage());
						throw e;
					}
				}
				if (productOrder.getId() != contacts.getOrderId()) {
					OrderContacts temp = new OrderContacts();
					BeanMapper.copy(contacts, temp);
					temp.setId(null);
					temp.setOrderId(productOrder.getId());
					temp.setOrderType(orderType);
					temp.setAgentId(productOrder.getOrderCompany());
					orderContactsDao.save(temp);
				} else {
					contacts.setOrderId(productOrder.getId());
					contacts.setOrderType(orderType);
					contacts.setAgentId(productOrder.getOrderCompany());
					orderContactsDao.save(contacts);
				}
			}
			productOrder.setOrderPersonName(contactsName.toString());
		}
	}

	/**
	 * 保存订单价格 
	 * @author yakun.bai
	 * @throws Exception 
	 * @Date 2016-9-13
	 */
	private void saveOrderPrice(ProductOrderCommon productOrder, HttpServletRequest request) throws Exception {
		
		// 设置订单成人、儿童、特殊人群、订金、单房差币种
		List<String> unitCurrencyList = setOrderUnitCurrencyId(productOrder);
		
		String adultCurrencyId = unitCurrencyList.get(0);
		String childCurrencyId = unitCurrencyList.get(1);
		String specialCurrencyId = unitCurrencyList.get(2);
		
		// 设置订单单价金额
		List<BigDecimal> unitPriceList = setOrderUnitPrice(productOrder, unitCurrencyList);
		
		BigDecimal adultPrice = unitPriceList.get(0);
		BigDecimal childPrice = unitPriceList.get(1);
		BigDecimal specialPrice = unitPriceList.get(2);
		
		// 设置订单价格UUID：结算价、成本价、原始应收、已付、达帐
		productOrder.setTotalMoney(UuidUtils.generUuid());
	    productOrder.setCostMoney(UuidUtils.generUuid());
	    productOrder.setOriginalTotalMoney(UuidUtils.generUuid());
	    productOrder.setPayedMoney(UuidUtils.generUuid());
	    productOrder.setAccountedMoney(UuidUtils.generUuid());
	    // 设置订单服务费UUID：整体服务费、quauq服务费、渠道服务费
	    if (Context.PRICE_TYPE_QUJ == productOrder.getPriceType()) {
	    	productOrder.setServiceCharge(UuidUtils.generUuid()); // 整体服务费（包含quauq和渠道服务费）
	    	productOrder.setQuauqServiceCharge(UuidUtils.generUuid());  // quauq服务费
	    	productOrder.setPartnerServiceCharge(UuidUtils.generUuid()); // 渠道服务费
	    	productOrder.setCutServiceCharge(UuidUtils.generUuid()); // 抽成服务费
	    }

	    /** 计算订单总额即应收价（非quauq订单的总额  = 总结算价，quauq订单的总额 = 总结算价 + quauq服务费 + 渠道服务费） */ 
	    Map<String,BigDecimal> totalMoneyMap = Maps.newHashMap();
	    // 成人订单价格
	    if (adultPrice != null && productOrder.getOrderPersonNumAdult() > 0) {
	    	totalMoneyMap.put(adultCurrencyId, adultPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumAdult())));
	    }
	    // 儿童订单价格
	    if (childPrice != null && productOrder.getOrderPersonNumChild() > 0) {
	    	BigDecimal childTotalMoney = childPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumChild()));
	    	if (!totalMoneyMap.containsKey(childCurrencyId)) {
	    		totalMoneyMap.put(childCurrencyId, childTotalMoney);
	    	} else {
	    		totalMoneyMap.put(childCurrencyId, totalMoneyMap.get(childCurrencyId).add(childTotalMoney));
	    	}
	    }
	    // 特殊人群订单价格
	    if (specialPrice != null && productOrder.getOrderPersonNumSpecial() > 0) {
	    	BigDecimal specialTotalMoney = specialPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()));
	    	if (!totalMoneyMap.containsKey(specialCurrencyId)) {
	    		totalMoneyMap.put(specialCurrencyId, specialTotalMoney);
	    	} else {
	    		totalMoneyMap.put(specialCurrencyId, totalMoneyMap.get(specialCurrencyId).add(specialTotalMoney));
	    	}
	    }
	    
	    //保存订单及订单费用
	    saveOrderAndPrice(productOrder, totalMoneyMap, null, false);
	}
	
	/**
	 * 设置订单成人、儿童、特殊人群、订金、单房差币种 
	 * @author yakun.bai
	 * @Date 2016-9-18
	 */
	private List<String> setOrderUnitCurrencyId(ProductOrderCommon productOrder) throws Exception {
		
		// 获取订单对应团期
		ActivityGroup activityGroup = activityGroupDao.findById(productOrder.getProductGroupId());
		// 报名时使用的价格类型 0：同行价 1：直客价 2：QUAUQ价
		Integer priceType = productOrder.getPriceType();
		       
		// 如果团期价格没币种则报错
		String currency = activityGroup.getCurrencyType();
	    if (currency == null) {
	    	throw new Exception("团期价格没币种");
	    }
	    String[] currencyArr = currency.split(",");
	    
	    // 单团、游学、大客户、自由行币种排序为：成人同行、儿童同行、特殊人群同行、订金、单房差，五个值；
	    // 散拼币种排序为：成人同行、儿童同行、特殊人群同行、成人直客、儿童直客、特殊人群直客、订金、单房差，八个值；
	    // 游轮币种排序为：成人同行、儿童同行、成人直客、儿童直客、订金、单房差，六个值
		String adultCurrencyId = currencyArr[0];
		String childCurrencyId = currencyArr[1];
		String specialCurrencyId = currencyArr[2];
		String payDepositCurrencyId = currencyArr[currencyArr.length - 2];
		String singleDiffCurrencyId = currencyArr[currencyArr.length - 1];
	    
	    if (Context.ORDER_TYPE_SP == productOrder.getOrderStatus()) {
	    	if (Context.PRICE_TYPE_ZKJ == priceType) {
	    		adultCurrencyId = currencyArr[3];
	    		childCurrencyId = currencyArr[4];
	    		specialCurrencyId = currencyArr[5];
	    	}
	    } else if (Context.ORDER_TYPE_CRUISE == productOrder.getOrderStatus()) {
	    	specialCurrencyId = "1";
	    	if (Context.PRICE_TYPE_ZKJ == priceType) {
	    		adultCurrencyId = currencyArr[2];
	    		childCurrencyId = currencyArr[3];
	    	}
	    }
		
	    // 把币种放置在List中
	    List<String> unitCurrencyList = Lists.newArrayList();
	    unitCurrencyList.add(adultCurrencyId);
	    unitCurrencyList.add(childCurrencyId);
	    unitCurrencyList.add(specialCurrencyId);
	    unitCurrencyList.add(payDepositCurrencyId);
	    unitCurrencyList.add(singleDiffCurrencyId);
	    return unitCurrencyList;
	}
	
	/**
	 * 设置订单单价金额
	 * @author yakun.bai
	 * @Date 2016-9-18
	 */
	private List<BigDecimal> setOrderUnitPrice(ProductOrderCommon productOrder, List<String> unitCurrencyList) throws Exception {
		
		BigDecimal adultPrice = null;
		BigDecimal childPrice = null;
		BigDecimal specialPrice = null;
		
		// 设置订单占位数（游轮:房间数roomNum, 非游轮：人数personNum）
		boolean isCruiseFlag = false;
		if (productOrder.getOrderStatus().intValue() == Context.ORDER_TYPE_CRUISE.intValue()) {
			isCruiseFlag = true;
		}
		Integer holdNum = isCruiseFlag ? productOrder.getRoomNumber() : productOrder.getOrderPersonNum();
		if (holdNum == null) {
			holdNum = 0;
		}
		
		// 获取订单对应团期
		ActivityGroup activityGroup = activityGroupDao.findById(productOrder.getProductGroupId());
		// 报名时使用的价格类型 0：同行价 1：直客价 2：QUAUQ价
		Integer priceType = productOrder.getPriceType();
		
		String adultCurrencyId = unitCurrencyList.get(0);
		String childCurrencyId = unitCurrencyList.get(1);
		String specialCurrencyId = unitCurrencyList.get(2);
		String payDepositCurrencyId = unitCurrencyList.get(3);
		String singleDiffCurrencyId = unitCurrencyList.get(4);
		
	    
		if (Context.PRICE_TYPE_ZKJ == priceType) {
			adultPrice = activityGroup.getSuggestAdultPrice();
			childPrice = activityGroup.getSuggestChildPrice();
			specialPrice = activityGroup.getSuggestSpecialPrice();
		} else if (Context.PRICE_TYPE_QUJ == priceType) {
			Rate rate = RateUtils.getRate(activityGroup.getId(), 2, productOrder.getOrderCompany());
	    	// 获取推广价（包含渠道费率）
	    	adultPrice = OrderCommonUtil.getRetailPrice(activityGroup.getSettlementAdultPrice(), activityGroup.getQuauqAdultPrice(), rate, Long.parseLong(adultCurrencyId));
	    	childPrice = OrderCommonUtil.getRetailPrice(activityGroup.getSettlementcChildPrice(), activityGroup.getQuauqChildPrice(), rate, Long.parseLong(childCurrencyId));
	    	specialPrice = OrderCommonUtil.getRetailPrice(activityGroup.getSettlementSpecialPrice(), activityGroup.getQuauqSpecialPrice(), rate, Long.parseLong(specialCurrencyId));
		} else {
			adultPrice = activityGroup.getSettlementAdultPrice();
			childPrice = activityGroup.getSettlementcChildPrice();
			specialPrice = activityGroup.getSettlementSpecialPrice();
		}
	    
	    if (productOrder.getId() == null) {
	    	// 报名时 成人单价（取自当时团期中对应“价格类型”的价格。如：同行价、直客价、quauq价）
	    	String settlementAdultPriceSeriNum = UUID.randomUUID().toString();
	    	productOrder.setSettlementAdultPrice(settlementAdultPriceSeriNum);
	    	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(settlementAdultPriceSeriNum, Integer.parseInt(adultCurrencyId), adultPrice, null, null, null, 1, UserUtils.getUser().getId()));
	    	// 报名时 儿童单价
	    	String settlementChildPriceSeriNum = UUID.randomUUID().toString();
	    	productOrder.setSettlementcChildPrice(settlementChildPriceSeriNum);
	    	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(settlementChildPriceSeriNum, Integer.parseInt(childCurrencyId), childPrice, null, null, null, 1, UserUtils.getUser().getId()));
	    	// 报名时 特殊人群单价
	    	String settlementSpecialPriceSeriNum = UUID.randomUUID().toString();
	    	productOrder.setSettlementSpecialPrice(settlementSpecialPriceSeriNum);
	    	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(settlementSpecialPriceSeriNum, Integer.parseInt(specialCurrencyId), specialPrice, null, null, null, 1, UserUtils.getUser().getId()));
	    	// 报名时 单房差
	    	String singleDiffSeriNum = UUID.randomUUID().toString();
	    	productOrder.setSingleDiff(singleDiffSeriNum);
	    	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(singleDiffSeriNum, Integer.parseInt(singleDiffCurrencyId), activityGroup.getSingleDiff() == null ? BigDecimal.ZERO : activityGroup.getSingleDiff(), null, null, null, 1, UserUtils.getUser().getId()));
	    	// 报名时 单人定金
	    	String payDepositSeriNum = UUID.randomUUID().toString();
	    	productOrder.setPayDeposit(payDepositSeriNum);
	    	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(payDepositSeriNum, Integer.parseInt(payDepositCurrencyId), activityGroup.getPayDeposit() == null ? BigDecimal.ZERO : activityGroup.getPayDeposit(), null, Context.MONEY_TYPE_YSDJ, productOrder.getOrderStatus(), 1, UserUtils.getUser().getId()));
	    	// 定金总金额（总人数 * 定金单价）
	    	String frontMoneySeriNum = UUID.randomUUID().toString();
	    	productOrder.setFrontMoney(frontMoneySeriNum);
	    	BigDecimal frontMoney = StringNumFormat.getBigDecimalMultiply(new BigDecimal(holdNum),activityGroup.getPayDeposit());
	    	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(frontMoneySeriNum, Integer.parseInt(payDepositCurrencyId), frontMoney, null, Context.MONEY_TYPE_DJ, productOrder.getOrderStatus(), 1, UserUtils.getUser().getId()));
	    	// 原始订金金额
	    	String originalFrontMoneySeriNum = UUID.randomUUID().toString();
	    	productOrder.setOriginalFrontMoney(originalFrontMoneySeriNum);
	    	BigDecimal originalFrontMoney = StringNumFormat.getBigDecimalMultiply(new BigDecimal(holdNum),activityGroup.getPayDeposit());
	    	moneyAmountService.saveOrUpdateMoneyAmount(new MoneyAmount(originalFrontMoneySeriNum, Integer.parseInt(payDepositCurrencyId), originalFrontMoney, null, Context.MONEY_TYPE_YSDJ, productOrder.getOrderStatus(), 1, UserUtils.getUser().getId()));
	    }
	    
	    // 把币种放置在List中
	    List<BigDecimal> unitPriceList = Lists.newArrayList();
	    unitPriceList.add(adultPrice);
	    unitPriceList.add(childPrice);
	    unitPriceList.add(specialPrice);
	    return unitPriceList;
	}
	
   
	/**
	 * 保存订单及订单费用(包括成本价)
	 * @param productOrder 订单实体
	 * @return
	 */
	private ProductOrderCommon saveOrderAndPrice(ProductOrderCommon productOrder, 
			Map<String,BigDecimal> totalMoneyMap, Map<String,BigDecimal> costTotalMoneyMap, boolean isDelOldMoneyFlag) {
		
		// 保存订单
		orderDao.save(productOrder);
		//保存订单费用
    	List<MoneyAmount> moneyAmountList= Lists.newArrayList();  // 订单总结算价金额
		List<MoneyAmount> oldMoneyAmountList= Lists.newArrayList();  // 订单原始应收（原始总）
		
		String totalMoneySerialNum = productOrder.getTotalMoney();
		String oldTotalMoneySerialNum = productOrder.getOriginalTotalMoney();
		Long orderId = productOrder.getId();
		Long userId = UserUtils.getUser().getId();
		int orderStatus = productOrder.getOrderStatus();
		
		if (isDelOldMoneyFlag && StringUtils.isNotBlank(totalMoneySerialNum)) {
			moneyAmountService.delMoneyAmountBySerialNum(totalMoneySerialNum);
		}
		
		// 保存订单总额
		if (MapUtils.isNotEmpty(totalMoneyMap)) {
			MoneyAmount moneyAmount = null;
			MoneyAmount oldMoneyAmount = null;
			for (String key : totalMoneyMap.keySet()) {
				Integer currencyId = Integer.parseInt(key);
				Currency currency = currencyDao.findOne(Long.parseLong(currencyId.toString()));
				BigDecimal money = totalMoneyMap.get(key);
				// 应收价
				moneyAmount = new MoneyAmount(totalMoneySerialNum, currencyId, money, orderId, Context.MONEY_TYPE_YSH, orderStatus, 1, userId);
				moneyAmount.setExchangerate(currency.getConvertLowest());
				moneyAmountList.add(moneyAmount);
				// 原始应收
				oldMoneyAmount = new MoneyAmount(oldTotalMoneySerialNum, currencyId, money, orderId, Context.MONEY_TYPE_YSYSH, orderStatus, 1, userId);
				moneyAmount.setExchangerate(currency.getConvertLowest());
				oldMoneyAmountList.add(oldMoneyAmount);
			}
			moneyAmountService.saveOrUpdateMoneyAmounts(totalMoneySerialNum, moneyAmountList);
			moneyAmountService.saveOrUpdateMoneyAmounts(oldTotalMoneySerialNum, oldMoneyAmountList);
		}
		
		// 保存订单成本价
		if (MapUtils.isNotEmpty(costTotalMoneyMap)) {
			MoneyAmount moneyAmount = null;
			List<MoneyAmount> costMoneyAmountList= Lists.newArrayList();
			String costTotalMoneySerialNum = productOrder.getCostMoney();	//成本价UUID
			for (String key : costTotalMoneyMap.keySet()) {
				Integer currencyId = Integer.parseInt(key);
				Currency currency = currencyDao.findOne(Long.parseLong(currencyId.toString()));
				BigDecimal money = costTotalMoneyMap.get(key);
				moneyAmount = new MoneyAmount(costTotalMoneySerialNum, currencyId, money, orderId, Context.MONEY_TYPE_CBJ, orderStatus, 1, userId);
				moneyAmount.setExchangerate(currency.getConvertLowest());
				costMoneyAmountList.add(moneyAmount);
			}
			moneyAmountService.saveOrUpdateMoneyAmounts(costTotalMoneySerialNum, costMoneyAmountList);
		}
		
	    //添加操作日志
	    this.saveLogOperate(Context.log_type_schedule, Context.log_type_schedule_name,
	    		"添加一条订单信息，订单单号为" + productOrder.getOrderNum(), "1", productOrder.getOrderStatus(), productOrder.getId());
	    
		return productOrder;
	}
	
	/**
	 * T1预报名订单逻辑处理
	 * @param order
	 * @param request
	 * @param isCreate
	 * @author yakun.bai
	 * @Date 2016-10-24
	 */
	private void saveT1PreOrderInfo(ProductOrderCommon order, HttpServletRequest request, boolean isCreate) {
		
		String preOrderId = request.getParameter("preOrderId");
		// 保存门市结算价返还差额
		String differenceMoney = request.getParameter("differenceMoney");
		
		if (StringUtils.isNotBlank(preOrderId) && StringUtils.isNotBlank(differenceMoney)) {
			ActivityGroup group = activityGroupDao.findOne(order.getProductGroupId());
			Integer currencyId = Integer.parseInt(group.getCurrencyType().split(",")[0]);
			MoneyAmount moneyAmount = new MoneyAmount();
	        moneyAmount.setSerialNum(UUID.randomUUID().toString());
	        moneyAmount.setCurrencyId(currencyId);
	        BigDecimal differenceAmount = new BigDecimal(differenceMoney);
	        moneyAmount.setAmount(differenceAmount);
	        moneyAmount.setOrderType(order.getOrderStatus()); // 产品类型
	        moneyAmount.setUid(order.getId()); // 订单ID
	        moneyAmount.setBusindessType(Context.BUSINESS_TYPE_ORDER);  // 业务类型
	        moneyAmount.setCreatedBy(UserUtils.getUser().getId());
	        moneyAmount.setCreateTime(new Date());
	        moneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
	        moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
	        
			// 结算价添加差额
	        List<MoneyAmount> moneyList = moneyAmountService.findBySerialNum(order.getTotalMoney());
	        if (CollectionUtils.isNotEmpty(moneyList)) {
	        	for (MoneyAmount amount : moneyList) {
	        		if (amount.getCurrencyId().intValue() == currencyId.intValue()) {
	        			amount.setAmount(amount.getAmount().add(differenceAmount));
	        			moneyAmountService.saveOrUpdateMoneyAmount(amount);
	        			break;
	        		} else {
	        			continue;
	        		}
	        	}
	        }
	        
	        order.setDifferenceFlag(1);
			order.setPreOrderId(Long.parseLong(preOrderId));
	        order.setDifferenceMoney(moneyAmount.getSerialNum());
	        orderDao.save(order);
	        
	        // 预报名订单状态改为已下单
	        T1PreOrder preOrder = t1PreOrderDao.findOne(Long.parseLong(preOrderId));
	        preOrder.setOrderId(order.getId());
	        preOrder.setOrderTimeForT2(order.getCreateDate());
	        preOrder.setOrderStatus(2);
	        t1PreOrderDao.save(preOrder);
		}
	}
   
	/**
	 * 保存订单私有公共方法：订单最后一步保存和修改都会用
	 * @param mapResult
	 * @param request
	 * @throws JSONException 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void modSaveOrder(Map<String, Object> mapResult, HttpServletRequest request, boolean isCreate) throws JSONException {
		
		// 补位ID
		String groupCoverId = request.getParameter("groupCoverId");
		// 订单ID
		String productOrderId = request.getParameter("productOrderId");
		// 根据订单ID查询订单
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(productOrderId));
		// 如果订单时待审核状态，则改为正常状态
		if (ProductOrderCommon.DEL_FLAG_AUDIT.equals(productOrder.getDelFlag())) {
			productOrder.setDelFlag(ProductOrderCommon.DEL_FLAG_NORMAL);
		}
		// 跟新补位记录
		if (StringUtils.isNotBlank(groupCoverId)) {
			addGroupCoverNum(productOrder, groupCoverId);
			productOrder.setIsAfterSupplement(1);
		}
		// 如果是新生成订单并且不是补位订单则校验余位
		if (isCreate && StringUtils.isBlank(groupCoverId)) {
			try {
				orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.CREATE);
			} catch (Exception e) {
				e.printStackTrace();
				mapResult.put("errorMsg", e.getMessage());
				return;
			}
		}
		
		// 结算价信息
		String currencyId = request.getParameter("currencyId");
		String currencyPrice = request.getParameter("currenctPrice");
		Map<String,BigDecimal> totalMoneyMap = Maps.newHashMap();
		if (StringUtils.isNotBlank(currencyId) && StringUtils.isNotBlank(currencyPrice)) {
			String[] currencyIdArr = currencyId.trim().split(",");
			String[] currencyPrcieArr = currencyPrice.trim().split(",");
			for (int i = 0; i < currencyIdArr.length; i++) {
				totalMoneyMap.put(currencyIdArr[i], new BigDecimal(currencyPrcieArr[i]));
			}
		}
		
		// 成本价信息
		String costCurrencyId = request.getParameter("costCurrencyId");
		String costCurrencyPrice = request.getParameter("costCurrencyPrice");
		Map<String,BigDecimal> costTotalMoneyMap = new HashMap<String, BigDecimal>();
		if (StringUtils.isNotBlank(costCurrencyId) && StringUtils.isNotBlank(costCurrencyPrice)) {
			String[] costCurrencyIdArr = costCurrencyId.split(",");
			String[] costCurrencyPriceArr = costCurrencyPrice.split(",");
			for (int i = 0; i < costCurrencyIdArr.length; i++) {
				costTotalMoneyMap.put(costCurrencyIdArr[i], new BigDecimal(costCurrencyPriceArr[i]));
			}
		}
		
		// 特殊需求
		String specialDemand = request.getParameter("specialDemand");
		if (StringUtils.isNotBlank(specialDemand)) {
			productOrder.setSpecialDemand(specialDemand);
		}

		// 特殊需求文件ids
		String specialDemandFileIds = request.getParameter("specialDemandFileIds");
		if (StringUtils.isNotBlank(specialDemandFileIds)) {
			productOrder.setSpecialDemandFileIds(specialDemandFileIds);
		}

		// 预订团队返佣
		String rebatesCurrency = request.getParameter("rebatesCurrency");
		String rebatesMoney = request.getParameter("rebatesMoney");
        if (StringUtils.isNotBlank(rebatesMoney) && StringUtils.isNotBlank(rebatesCurrency)) {
            Currency currency = currencyDao.findById(Long.valueOf(rebatesCurrency));
        	if (currency != null) {
                MoneyAmount moneyAmount = new MoneyAmount();
                moneyAmount.setSerialNum(UUID.randomUUID().toString());
                moneyAmount.setCurrencyId(Integer.valueOf(rebatesCurrency));
                BigDecimal dec = new BigDecimal(rebatesMoney);
                moneyAmount.setAmount(dec);
                moneyAmount.setMoneyType(Integer.valueOf(Context.MONEY_TYPE_FYFY)); // 款项类型
                moneyAmount.setOrderType(productOrder.getOrderStatus()); // 产品类型
                moneyAmount.setUid(productOrder.getId()); // 订单ID
                moneyAmount.setBusindessType(Context.BUSINESS_TYPE_ORDER);  // 业务类型
                moneyAmount.setCurrencyId(Integer.valueOf(rebatesCurrency)); // 币种ID
                moneyAmount.setExchangerate(currency.getCurrencyExchangerate()); // 汇率
                moneyAmount.setCreatedBy(UserUtils.getUser().getId());
                moneyAmount.setCreateTime(new Date());
                moneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
                moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
                // 将预定返佣的uuid存入订单
                productOrder.setScheduleBackUuid(moneyAmount.getSerialNum());
        	}
        }
        Long orderCompany = Long.parseLong(request.getParameter("orderCompany"));
        if (orderCompany <= 0 && Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getCompanyUuid())) {
        	productOrder.setOrderSaler(UserUtils.getUser());
        	productOrder.setPaymentType(Context.PAYMENT_TYPE_JS);
        	//if the there is no office
        	List<OrderContacts> orderCotacts = OrderUtil.getContactsList(request.getParameter("orderContactsJSON"));
        	String salerId = request.getParameter("salerId");
			orderCompany = agentinfoService.saveAgent(orderCotacts,request.getParameter("orderCompanyNameShow"),salerId);
			if(orderCompany == null){
				mapResult.put("errorMsg", "渠道商已存在");// 增加报错内容;
				return;
			}
        }
		if(orderCompany != null && orderCompany != -1){
			Agentinfo agent = agentinfoService.loadAgentInfoById(orderCompany);
			productOrder.setOrderCompanyName(agent.getAgentName());
		}
		ProductOrderCommon order = saveOrderAndPrice(productOrder, totalMoneyMap, costTotalMoneyMap, true);  // 保存订单（总额/成本价/原始结算/服务费） （预定第一步）
		String groupHandleId = request.getParameter("groupHandleId");
		if(StringUtils.isNotBlank(groupHandleId)) {
			try {
				handleGroupHandleAndVisa(groupHandleId, order);
			} catch(Exception e) {
				e.printStackTrace();
				mapResult.put("errorMsg", e.getMessage());
			}
		}

		mapResult.put("productOrder", productOrder);
		
		// T1预报名订单逻辑处理
		saveT1PreOrderInfo(order, request, isCreate);
		
		// 服务费计算
		saveOrderChargePrice(productOrder, isCreate);
		
		// 添加订单跟踪记录
		if (isCreate) {
			progressService.addOrderProgrssTracking(productOrder,true);
		}

		
		//-------by------junhao.zhao-----2017-01-05-----主要通过productorder向表order_data_statistics中添加数据---开始-----------------------------------
		// 向表order_data_statistics中添加数据——生成“已占位”状态订单直接加到表order_data_statistics中（付款方式：“预占位”、“担保占位”、“确认单占位”、“资料占位”）;
		// “全款支付”、“订金占位”收款成功后加到表order_data_statistics中.“财务确认占位”、“计调确认占位”确认之后加到表order_data_statistics中。
		if (orderDateSaveOrUpdateService.whetherAddOrUpdate(productOrder)) {
			orderDateSaveOrUpdateService.addOrderDataStatistics(productOrder);
		}
	}
	
	/** 
	 * 补位记录更新
	 * @author yakun.bai
	 * @Date 2016-9-18
	 */
	private void addGroupCoverNum(ProductOrderCommon order, String groupCoverId) {
		GroupCover groupCover = groupCoverDao.getById(Long.parseLong(groupCoverId));
		groupCover.setCoverStatus(Context.COVER_STATUS_SCDD);
		groupCover.setOrderId(order.getId());
		groupCoverDao.save(groupCover);
	}
	
	/**
	 * 保存订单服务费 
	 * @author yakun.bai
	 * @Date 2016-9-1
	 */
	private void saveOrderChargePrice(ProductOrderCommon productOrder, boolean isCreate) {
		// 散拼产品使用quauq价报名生成的订单： 保存服务费、订单总额添加服务费
		if (productOrder.getOrderStatus() == Context.ORDER_TYPE_SP && productOrder.getPriceType() == Context.PRICE_TYPE_QUJ) {
			setOrderChargePrice(productOrder, isCreate);
		}
	}
	
	/**
	 * 设置订单费率：quauq费率、总社或集团费率、总体费率 
	 * @author yakun.bai
	 * @Date 2016-8-31
	 */
	public void setOrderChargePrice(ProductOrderCommon productOrder, boolean isCreate) {
		
		// 整体服务费（含渠道费用）
		String serviceCharge = productOrder.getServiceCharge();
		if (StringUtils.isBlank(serviceCharge)) {
			serviceCharge = UuidUtils.generUuid();
		}
		// quauq服务费
		String quauqServiceCharge = productOrder.getQuauqServiceCharge();
		if (StringUtils.isBlank(quauqServiceCharge)) {
			quauqServiceCharge = UuidUtils.generUuid();
		}
		// 渠道服务费
		String partnerServiceCharge = productOrder.getPartnerServiceCharge();
		if (StringUtils.isBlank(partnerServiceCharge)) {
			partnerServiceCharge = UuidUtils.generUuid();
		}
		// 抽成服务费
		String cutServiceCharge = productOrder.getCutServiceCharge();
		if (StringUtils.isBlank(partnerServiceCharge)) {
			cutServiceCharge = UuidUtils.generUuid();
		}
		
		// 查询订单对应团期
		ActivityGroup activityGroup = activityGroupDao.findById(productOrder.getProductGroupId());
		
		// 获取订单单价交互类
		OrderUnitPrice orderUnitPrice = setOrderChargeUnitCurrencyId(activityGroup, productOrder, isCreate);
		// 设置订单单价币种和金额及对应费率
		Rate rate = orderUnitPrice.getRate();
		
		// 整体服务费list
		List<MoneyAmount> allChargeList = Lists.newArrayList();
		// quauq服务费list
		List<MoneyAmount> quauqChargeList = Lists.newArrayList();
		// 渠道服务费list
		List<MoneyAmount> partnerChargeList = Lists.newArrayList();
		// 抽成服务费list
		List<MoneyAmount> cutChargeList = Lists.newArrayList();
		
		// 设置订单产品服务费（成人费率、儿童费率、特殊人群费率）
		setOrderProductCharge(activityGroup, productOrder, quauqChargeList, partnerChargeList, 
				orderUnitPrice, quauqServiceCharge, partnerServiceCharge);
		// 设置订单其他费用服务费
		setOrderTravlerCharge(productOrder, quauqChargeList, partnerChargeList, 
				orderUnitPrice, quauqServiceCharge, partnerServiceCharge);
		// 设置订单抽成服务费
		setOrderCutCharge(productOrder, cutChargeList, cutServiceCharge, orderUnitPrice);
		
		// 如果quauq服务费list不为空则需在保存前删除原有数据，如果为空且是订单修改操作，则删除原有数据
		if (CollectionUtils.isNotEmpty(quauqChargeList)) {
			moneyAmountService.delMoneyAmountBySerialNum(quauqServiceCharge);
			moneyAmountService.saveOrUpdateMoneyAmounts(quauqServiceCharge, moneyAmountService.sameCurrencySum(quauqChargeList));
		} else {
			if (!isCreate) {
				moneyAmountService.delMoneyAmountBySerialNum(quauqServiceCharge);
			}
		}
		// 如果渠道服务费list不为空则需在保存前删除原有数据，如果为空且是订单修改操作，则删除原有数据
		if (CollectionUtils.isNotEmpty(partnerChargeList)) {
			moneyAmountService.delMoneyAmountBySerialNum(partnerServiceCharge);
			moneyAmountService.saveOrUpdateMoneyAmounts(partnerServiceCharge, moneyAmountService.sameCurrencySum(partnerChargeList));
		} else {
			if (!isCreate) {
				moneyAmountService.delMoneyAmountBySerialNum(partnerServiceCharge);
			}
		}
		// 如果抽成服务费list不为空则需在保存前删除原有数据，如果为空且是订单修改操作，则删除原有数据
		if (CollectionUtils.isNotEmpty(cutChargeList)) {
			moneyAmountService.delMoneyAmountBySerialNum(cutServiceCharge);
			moneyAmountService.saveOrUpdateMoneyAmounts(cutServiceCharge, moneyAmountService.sameCurrencySum(cutChargeList));
		} else {
			if (!isCreate) {
				moneyAmountService.delMoneyAmountBySerialNum(cutServiceCharge);
			}
		}
		// 如果整体服务费list不为空则需在保存前删除原有数据，如果为空且是订单修改操作，则删除原有数据
		allChargeList.addAll(quauqChargeList);
		allChargeList.addAll(partnerChargeList);
		allChargeList.addAll(cutChargeList);
		for (MoneyAmount moneyAmount : allChargeList) {
			moneyAmount.setSerialNum(serviceCharge);
		}
		if (CollectionUtils.isNotEmpty(allChargeList)) {
			moneyAmountService.delMoneyAmountBySerialNum(serviceCharge);
			moneyAmountService.saveOrUpdateMoneyAmounts(serviceCharge, moneyAmountService.sameCurrencySum(allChargeList));
		} else {
			if (!isCreate) {
				moneyAmountService.delMoneyAmountBySerialNum(serviceCharge);
			}
		}
		
		// 订单保存费率（包含渠道费率）
		if (rate != null && isCreate) {
			productOrder.setQuauqProductChargeType(rate.getQuauqRateType());
			productOrder.setQuauqProductChargeRate(rate.getQuauqRate());
			productOrder.setQuauqOtherChargeType(rate.getQuauqOtherRateType());
			productOrder.setQuauqOtherChargeRate(rate.getQuauqOtherRate());
			productOrder.setPartnerProductChargeType(rate.getAgentRateType());
			productOrder.setPartnerProductChargeRate(rate.getAgentRate());
			productOrder.setPartnerOtherChargeType(rate.getAgentOtherRateType());
			productOrder.setPartnerOtherChargeRate(rate.getAgentOtherRate());
			productOrder.setCutChargeType(rate.getChouchengRateType());
			productOrder.setCutChargeRate(rate.getChouchengRate());
			// 如果是预报名订单，则需要按保存当初汇率
			if (null != productOrder.getPreOrderId()) {
				T1PreOrder preOrder = t1PreOrderDao.findOne(productOrder.getPreOrderId());
				if (null != preOrder.getQuauqProductChargeType()) {
					productOrder.setQuauqProductChargeType(preOrder.getQuauqProductChargeType());
					productOrder.setQuauqProductChargeRate(preOrder.getQuauqProductChargeRate());
					productOrder.setQuauqOtherChargeType(preOrder.getQuauqOtherChargeType());
					productOrder.setQuauqOtherChargeRate(preOrder.getQuauqOtherChargeRate());
					productOrder.setPartnerProductChargeType(preOrder.getPartnerProductChargeType());
					productOrder.setPartnerProductChargeRate(preOrder.getPartnerProductChargeRate());
					productOrder.setPartnerOtherChargeType(preOrder.getPartnerOtherChargeType());
					productOrder.setPartnerOtherChargeRate(preOrder.getPartnerOtherChargeRate());
					productOrder.setCutChargeType(preOrder.getCutChargeType());
					productOrder.setCutChargeRate(preOrder.getCutChargeRate());
				}
			}
		}
		
		productOrder.setServiceCharge(serviceCharge);
		productOrder.setQuauqServiceCharge(quauqServiceCharge);
		productOrder.setPartnerServiceCharge(partnerServiceCharge);
		productOrder.setCutServiceCharge(cutServiceCharge);
		
		// 保存订单费率（财务使用此表）
		Integer quauqChargeDelFlag = 0; // quauq服务费是否要保存：0 不需要；1 需要
		Integer partnerChargeDelFlag = 0; // 渠道服务费是否要保存：0 不需要；1 需要
		if (CollectionUtils.isNotEmpty(quauqChargeList)) {
			quauqChargeDelFlag = 0;
		} else {
			quauqChargeDelFlag = 1;
		}
		if (CollectionUtils.isNotEmpty(partnerChargeList)) {
			partnerChargeDelFlag = 0;
		} else {
			partnerChargeDelFlag = 1;
		}
		// 如果是预定生成新订单，则直接保存数据；如果是修改订单，则需更新删除标识
		if (isCreate) {
			saveOrderServiceCharge(productOrder, quauqServiceCharge, quauqChargeDelFlag, partnerChargeDelFlag);
		} else {
			updateOrderServiceCharge(productOrder, quauqServiceCharge, quauqChargeDelFlag, partnerChargeDelFlag);
		}
	}
	
	/**
	 * 设置订单单价币种和金额及对应费率
	 * @param productOrder 订单实体类
	 * @param isCreate 是否为预定
	 * @return OrderUnitPrice
	 * @author yakun.bai
	 * @Date 2016-9-20
	 */
	private OrderUnitPrice setOrderChargeUnitCurrencyId(ActivityGroup activityGroup, ProductOrderCommon productOrder, boolean isCreate) {
		
		// 订单单价实体类
		OrderUnitPrice orderUnitPrice = new OrderUnitPrice();
		// 订单单价对应费率
		Rate rate = null;
		// 订单单价币种及金额
		Long adultCurrencyId = null;
		Long childCurrencyId = null;
		Long specialCurrencyId = null;
		BigDecimal quauqAdultPrice = null;
		BigDecimal quauqChildPrice = null;
		BigDecimal quauqSpecialPrice = null;
		// 零售价是否大于同行价
		BigDecimal lsAdultPrice = null;
		BigDecimal lsChildPrice = null;
		BigDecimal lsSpecialPrice = null;
		boolean adultOverFlag = false;
		boolean childOverFlag = false;
		boolean specialOverFlag = false;
		
		// 如果是预定订单，则单价币种和金额都从团期获取；如果是订单修改，则从订单已有记录中获取
		if (isCreate) {
			// 团期币种字符串
			String currency = activityGroup.getCurrencyType();
			String[] currencyArr = currency.split(",");
			// 订单单价币种
			adultCurrencyId = Long.parseLong(currencyArr[0]);
			childCurrencyId = Long.parseLong(currencyArr[1]);
			specialCurrencyId = Long.parseLong(currencyArr[2]);
			// 订单单价金额
			quauqAdultPrice = activityGroup.getQuauqAdultPrice();
			quauqChildPrice = activityGroup.getQuauqChildPrice();
			quauqSpecialPrice = activityGroup.getQuauqSpecialPrice();
			// 订单对应服务费率
			rate = RateUtils.getRate(productOrder.getProductGroupId(), 2, productOrder.getOrderCompany());
			// 获取订单零售价
			lsAdultPrice = OrderCommonUtil.getRetailPrice(quauqAdultPrice, rate, adultCurrencyId);
			lsChildPrice = OrderCommonUtil.getRetailPrice(quauqChildPrice, rate, childCurrencyId);
			lsSpecialPrice = OrderCommonUtil.getRetailPrice(quauqSpecialPrice, rate, specialCurrencyId);
		} else {
			// 从订单获取服务费费率属性值
			rate = new Rate();
			rate.setAgentType(productOrder.getAgentType());
			rate.setQuauqRateType(productOrder.getQuauqProductChargeType());
			rate.setQuauqRate(productOrder.getQuauqProductChargeRate());
			rate.setQuauqOtherRateType(productOrder.getQuauqOtherChargeType());
			rate.setQuauqOtherRate(productOrder.getQuauqOtherChargeRate());
			rate.setAgentRateType(productOrder.getPartnerProductChargeType());
			rate.setAgentRate(productOrder.getPartnerProductChargeRate());
			rate.setAgentOtherRateType(productOrder.getPartnerOtherChargeType());
			rate.setAgentOtherRate(productOrder.getPartnerOtherChargeRate());
			rate.setChouchengRateType(productOrder.getCutChargeType());
			rate.setChouchengRate(productOrder.getCutChargeRate());
			
			// 从订单获取成人币种及金额
			List<MoneyAmount> aultMoneyAmountList = moneyAmountService.findAmountBySerialNum(productOrder.getSettlementAdultPrice());
			if (CollectionUtils.isNotEmpty(aultMoneyAmountList)) {
				adultCurrencyId = aultMoneyAmountList.get(0).getCurrencyId().longValue();
				lsAdultPrice = aultMoneyAmountList.get(0).getAmount();
				quauqAdultPrice = getQuauqPrice(productOrder, lsAdultPrice, adultCurrencyId.longValue());
			}
			// 从订单获取儿童币种及金额
			List<MoneyAmount> childMoneyAmountList = moneyAmountService.findAmountBySerialNum(productOrder.getSettlementcChildPrice());
			if (CollectionUtils.isNotEmpty(childMoneyAmountList)) {
				childCurrencyId = childMoneyAmountList.get(0).getCurrencyId().longValue();
				lsChildPrice = childMoneyAmountList.get(0).getAmount();
				quauqChildPrice = getQuauqPrice(productOrder, lsChildPrice, childCurrencyId.longValue());
			}
			// 从订单获取特殊人群币种及金额
			List<MoneyAmount> specialMoneyAmountList = moneyAmountService.findAmountBySerialNum(productOrder.getSettlementSpecialPrice());
			if (CollectionUtils.isNotEmpty(specialMoneyAmountList)) {
				specialCurrencyId = specialMoneyAmountList.get(0).getCurrencyId().longValue();
				lsSpecialPrice = specialMoneyAmountList.get(0).getAmount();
				quauqSpecialPrice = getQuauqPrice(productOrder, lsSpecialPrice, specialCurrencyId.longValue());
			}
		}
		
		// 判断零售价是否超过同行价：如果超过同行价，则需要记录；
		String overMoney = "";
		if (null != lsAdultPrice) {
			if (isCreate) {
				adultOverFlag = lsAdultPrice.compareTo(activityGroup.getSettlementAdultPrice()) == 1 ? true : false;
				if (adultOverFlag) {
					overMoney += activityGroup.getSettlementAdultPrice().subtract(quauqAdultPrice) + ";";
				}
			} else {
				overMoney = productOrder.getOverMoney();
				if (StringUtils.isNotBlank(overMoney) && StringUtils.isNotBlank(overMoney.split(";")[0])) {
					adultOverFlag = true;
				}
			}
		}
		if (null != lsChildPrice) {
			if (isCreate) {
				childOverFlag = lsChildPrice.compareTo(activityGroup.getSettlementcChildPrice()) == 1 ? true : false;
				if (childOverFlag) {
					if (adultOverFlag) {
						overMoney += activityGroup.getSettlementcChildPrice().subtract(quauqChildPrice) + ";";
					} else {
						overMoney += ";" + activityGroup.getSettlementcChildPrice().subtract(quauqChildPrice) + ";";
					}
				}
			} else {
				overMoney = productOrder.getOverMoney();
				if (StringUtils.isNotBlank(overMoney) && StringUtils.isNotBlank(overMoney.split(";")[1])) {
					childOverFlag = true;
				}
			}
		}
		if (null != lsSpecialPrice) {
			if (isCreate) {
				specialOverFlag = lsSpecialPrice.compareTo(activityGroup.getSettlementSpecialPrice()) == 1 ? true : false;
				if (specialOverFlag) {
					if (adultOverFlag && childOverFlag) {
						overMoney += activityGroup.getSettlementSpecialPrice().subtract(quauqSpecialPrice);
					} else if (adultOverFlag && !childOverFlag) {
						overMoney += ";" + activityGroup.getSettlementSpecialPrice().subtract(quauqSpecialPrice);
					} else if (!adultOverFlag && childOverFlag) {
						overMoney += activityGroup.getSettlementSpecialPrice().subtract(quauqSpecialPrice);
					} else {
						overMoney += ";;" + activityGroup.getSettlementSpecialPrice().subtract(quauqSpecialPrice);
					}
				}
			} else {
				overMoney = productOrder.getOverMoney();
				if (StringUtils.isNotBlank(overMoney) && StringUtils.isNotBlank(overMoney.split(";")[2])) {
					specialOverFlag = true;
				}
			}
		}
		productOrder.setOverMoney(overMoney);
		
		// 设置订单单价属性值
		orderUnitPrice.setAdultCurrencyId(adultCurrencyId);
		orderUnitPrice.setChildCurrencyId(childCurrencyId);
		orderUnitPrice.setSpecialCurrencyId(specialCurrencyId);
		orderUnitPrice.setAdultPrice(quauqAdultPrice);
		orderUnitPrice.setChildPrice(quauqChildPrice);
		orderUnitPrice.setSpecialPrice(quauqSpecialPrice);
		orderUnitPrice.setRate(rate);
		orderUnitPrice.setAdultOverFlag(adultOverFlag);
		orderUnitPrice.setChildOverFlag(childOverFlag);
		orderUnitPrice.setSpecialOverFlag(specialOverFlag);
		if (adultOverFlag && null != quauqAdultPrice) {
			if (isCreate) {
				orderUnitPrice.setAdultDifferencePrice(activityGroup.getSettlementAdultPrice().subtract(quauqAdultPrice));
			} else {
				orderUnitPrice.setAdultDifferencePrice(new BigDecimal(productOrder.getOverMoney().split(";")[0]));
			}
		}
		if (childOverFlag && null != quauqChildPrice) {
			if (isCreate) {
				orderUnitPrice.setChildDifferencePrice(activityGroup.getSettlementcChildPrice().subtract(quauqChildPrice));
			} else {
				orderUnitPrice.setChildDifferencePrice(new BigDecimal(productOrder.getOverMoney().split(";")[1]));
			}
		}
		if (specialOverFlag && null != quauqSpecialPrice) {
			if (isCreate) {
				orderUnitPrice.setSpecialDifferencePrice(activityGroup.getSettlementSpecialPrice().subtract(quauqSpecialPrice));
			} else {
				orderUnitPrice.setSpecialDifferencePrice(new BigDecimal(productOrder.getOverMoney().split(";")[2]));
			}
		}
		return orderUnitPrice;
	}
	
	/**
	 * 根据推广价获取quauq价 
	 * @author yakun.bai
	 * @Date 2016-9-19
	 */
	private BigDecimal getQuauqPrice(ProductOrderCommon order, BigDecimal retailPrice, Long currencyId) {
		
		if (retailPrice != null) {
			// 产品quauq费率
			Integer quauqProductChargeType = order.getQuauqProductChargeType();
			BigDecimal quauqProductChargeRate = order.getQuauqProductChargeRate();
			// 产品渠道费率
			Integer partnerProductChargeType = order.getPartnerProductChargeType();
			BigDecimal partnerProductChargeRate = order.getPartnerProductChargeRate();
			
			BigDecimal temp = new BigDecimal(1);
			
			if (quauqProductChargeType == 1) {
				Currency currency = currencyDao.findById(currencyId);
				retailPrice = retailPrice.subtract(quauqProductChargeRate.divide(currency.getConvertLowest(), 2));
			} else {
				temp = temp.add(quauqProductChargeRate);
			}
			if (partnerProductChargeType == 1) {
				Currency currency = currencyDao.findById(currencyId);
				retailPrice = retailPrice.subtract(partnerProductChargeRate.divide(currency.getConvertLowest(), 2));
			} else {
				temp = temp.add(partnerProductChargeRate);
			}
			return retailPrice.divide(temp, 2);
		} else {
			return new BigDecimal(0);
		}
	}
	
	/**
	 * 设置订单产品服务费（成人费率、儿童费率、特殊人群费率）
	 * @param productOrder 订单实体类
	 * @param quauqChargeList quauq服务费list
	 * @param partnerChargeList 渠道服务费list
	 * @param orderUnitPrice 订单单价实体类
	 * @param quauqServiceCharge quauq服务费UUID
	 * @param partnerServiceCharge 渠道服务费UUID
	 * @author yakun.bai
	 * @Date 2016-9-20
	 */
	private void setOrderProductCharge(ActivityGroup activityGroup, ProductOrderCommon productOrder, List<MoneyAmount> quauqChargeList,
			List<MoneyAmount> partnerChargeList, OrderUnitPrice orderUnitPrice, String quauqServiceCharge, String partnerServiceCharge) {
		
		Long orderId = productOrder.getId();
		Integer orderStatus = productOrder.getOrderStatus();
		Long userId = UserUtils.getUser().getId();
		
		// 设置订单单价币种和金额及对应费率
		Long adultCurrencyId = orderUnitPrice.getAdultCurrencyId();
		Long childCurrencyId = orderUnitPrice.getChildCurrencyId();
		Long specialCurrencyId = orderUnitPrice.getSpecialCurrencyId();
		BigDecimal quauqAdultPrice = orderUnitPrice.getAdultPrice();
		BigDecimal quauqChildPrice = orderUnitPrice.getChildPrice();
		BigDecimal quauqSpecialPrice = orderUnitPrice.getSpecialPrice();
		Rate rate = orderUnitPrice.getRate();
		
		if (productOrder.getOrderPersonNumAdult() != 0) {
			
			Long tempCurrencyId = adultCurrencyId;
			Long tempPartnerCurrencyId = adultCurrencyId;
			
			// 获取quauq服务费
			BigDecimal adultPrice;
			if (orderUnitPrice.isAdultOverFlag()) {
				adultPrice = orderUnitPrice.getAdultDifferencePrice().multiply(new BigDecimal(productOrder.getOrderPersonNumAdult()));
			} else {
				adultPrice = getQuauqChargePrice(quauqAdultPrice, rate, tempCurrencyId)
						.multiply(new BigDecimal(productOrder.getOrderPersonNumAdult()));
			}
			MoneyAmount chargeMoneyAmount = new MoneyAmount(quauqServiceCharge, tempCurrencyId.intValue(), adultPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
			chargeMoneyAmount.setExchangerate(currencyDao.findById(tempCurrencyId).getConvertLowest());
			chargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
			quauqChargeList.add(chargeMoneyAmount);
			// 获取渠道服务费
			if (!orderUnitPrice.isAdultOverFlag()) {
				BigDecimal partnerAdultPrice = getPartnerChargePrice(quauqAdultPrice, rate, tempPartnerCurrencyId)
						.multiply(new BigDecimal(productOrder.getOrderPersonNumAdult()));
				MoneyAmount partnerChargeMoneyAmount = new MoneyAmount(partnerServiceCharge, tempPartnerCurrencyId.intValue(), partnerAdultPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
				partnerChargeMoneyAmount.setExchangerate(currencyDao.findById(tempPartnerCurrencyId).getConvertLowest());
				partnerChargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
				partnerChargeList.add(partnerChargeMoneyAmount);
			}
		}
		if (productOrder.getOrderPersonNumChild() != 0) {
			Long tempCurrencyId = childCurrencyId;
			Long tempPartnerCurrencyId = childCurrencyId;
			// 获取quauq服务费
			BigDecimal childPrice;
			if (orderUnitPrice.isChildOverFlag()) {
				childPrice = orderUnitPrice.getChildDifferencePrice().multiply(new BigDecimal(productOrder.getOrderPersonNumChild()));
			} else {
				childPrice = getQuauqChargePrice(quauqChildPrice, rate, tempCurrencyId)
						.multiply(new BigDecimal(productOrder.getOrderPersonNumChild()));
			}
			MoneyAmount chargeMoneyAmount = new MoneyAmount(quauqServiceCharge, tempCurrencyId.intValue(), childPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
			chargeMoneyAmount.setExchangerate(currencyDao.findById(tempCurrencyId).getConvertLowest());
			chargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
			quauqChargeList.add(chargeMoneyAmount);
			// 获取渠道服务费
			if (!orderUnitPrice.isChildOverFlag()) {
				BigDecimal partnerChildPrice = getPartnerChargePrice(quauqChildPrice, rate, tempPartnerCurrencyId)
						.multiply(new BigDecimal(productOrder.getOrderPersonNumChild()));
				MoneyAmount partnerChargeMoneyAmount = new MoneyAmount(partnerServiceCharge, tempPartnerCurrencyId.intValue(), partnerChildPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
				partnerChargeMoneyAmount.setExchangerate(currencyDao.findById(tempPartnerCurrencyId).getConvertLowest());
				partnerChargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
				partnerChargeList.add(partnerChargeMoneyAmount);
			}
		}
		if (productOrder.getOrderPersonNumSpecial() != 0) {
			Long tempCurrencyId = specialCurrencyId;
			Long tempPartnerCurrencyId = specialCurrencyId;
			// 获取quauq服务费
			BigDecimal specialPrice;
			if (orderUnitPrice.isSpecialOverFlag()) {
				specialPrice = orderUnitPrice.getSpecialDifferencePrice().multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()));
			} else {
				specialPrice = getQuauqChargePrice(quauqSpecialPrice, rate, tempCurrencyId)
						.multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()));
			}
			MoneyAmount chargeMoneyAmount = new MoneyAmount(quauqServiceCharge, tempCurrencyId.intValue(), specialPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
			chargeMoneyAmount.setExchangerate(currencyDao.findById(tempCurrencyId).getConvertLowest());
			chargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
			quauqChargeList.add(chargeMoneyAmount);
			// 获取渠道服务费
			if (!orderUnitPrice.isSpecialOverFlag()) {
				BigDecimal partnerSpecialPrice = getPartnerChargePrice(quauqSpecialPrice, rate, tempPartnerCurrencyId)
						.multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()));
				MoneyAmount partnerChargeMoneyAmount = new MoneyAmount(partnerServiceCharge, tempPartnerCurrencyId.intValue(), partnerSpecialPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
				partnerChargeMoneyAmount.setExchangerate(currencyDao.findById(tempPartnerCurrencyId).getConvertLowest());
				partnerChargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
				partnerChargeList.add(partnerChargeMoneyAmount);
			}
		}
	}
	
	/**
	 * 设置订单其他费用服务费
	 * @param productOrder 订单实体类
	 * @param quauqChargeList quauq服务费list
	 * @param partnerChargeList 渠道服务费list
	 * @param orderUnitPrice 订单单价实体类
	 * @param quauqServiceCharge quauq服务费UUID
	 * @param partnerServiceCharge 渠道服务费UUID
	 * @author yakun.bai
	 * @Date 2016-9-20
	 */
	private void setOrderTravlerCharge(ProductOrderCommon productOrder, List<MoneyAmount> quauqChargeList, List<MoneyAmount> partnerChargeList,
			OrderUnitPrice orderUnitPrice, String quauqServiceCharge, String partnerServiceCharge) {
		
		Long orderId = productOrder.getId();
		Integer orderStatus = productOrder.getOrderStatus();
		Long userId = UserUtils.getUser().getId();
		
		// 获取服务费费率
		Rate rate = orderUnitPrice.getRate();
		
		// 获取游客其他费用
		List<Traveler> travelerList = travelerDao.findTravelerByOrderIdAndOrderType(orderId, Context.ORDER_TYPE_SP);
		if (CollectionUtils.isNotEmpty(travelerList)) {
			for (Traveler traveler : travelerList) {
				Long travelerId = traveler.getId();
				List<Costchange> costChangeList = costchangeDao.findCostchangeBytravelerId(travelerId);
				if (CollectionUtils.isNotEmpty(costChangeList)) {
					for (Costchange costChange : costChangeList) {
						BigDecimal costSum = costChange.getCostSum();
						if (costSum != null && costSum.doubleValue() < 0) {
							Long tempCurrencyId = costChange.getPriceCurrency().getId();
							Long tempPartnerCurrencyId = costChange.getPriceCurrency().getId();
							// 获取quauq服务费
							BigDecimal costPrice = getQuauqOtherChargePrice(costSum, rate, tempCurrencyId);
							MoneyAmount chargeMoneyAmount = new MoneyAmount(quauqServiceCharge, tempCurrencyId.intValue(), costPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
							chargeMoneyAmount.setExchangerate(currencyDao.findById(tempCurrencyId).getConvertLowest());
							chargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
							quauqChargeList.add(chargeMoneyAmount);
							// 获取渠道服务费
							BigDecimal partnerCostPrice = getPartnerOtherChargePrice(costSum, rate, tempPartnerCurrencyId);
							MoneyAmount partnerChargeMoneyAmount = new MoneyAmount(partnerServiceCharge, tempPartnerCurrencyId.intValue(), partnerCostPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
							partnerChargeMoneyAmount.setExchangerate(currencyDao.findById(tempPartnerCurrencyId).getConvertLowest());
							partnerChargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
							partnerChargeList.add(partnerChargeMoneyAmount);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 设置订单抽成服务费
	 * @param productOrder 订单实体类
	 * @param cutChargeList 抽成金额list
	 * @param cutServiceCharge 抽成UUID
	 * @author yakun.bai
	 * @Date 2016-12-14
	 */
	private void setOrderCutCharge(ProductOrderCommon productOrder, List<MoneyAmount> cutChargeList, String cutServiceCharge,
			OrderUnitPrice orderUnitPrice) {
		
		Long orderId = productOrder.getId();
		Integer orderStatus = productOrder.getOrderStatus();
		Long userId = UserUtils.getUser().getId();
		
		// 获取服务费费率
		Rate rate = orderUnitPrice.getRate();
		
		List<MoneyAmount> moneyAmountList = moneyAmountService.findBySerialNum(productOrder.getTotalMoney());
		if (CollectionUtils.isNotEmpty(moneyAmountList)) {
			Integer cutChargeType = rate.getChouchengRateType();
			BigDecimal cutChargeRate = rate.getChouchengRate();
			if (null != cutChargeType && null != cutChargeRate) {
				List<MoneyAmount> differenceList = null;
				if (productOrder.getDifferenceFlag() == 1) {
					differenceList = moneyAmountService.findBySerialNum(productOrder.getDifferenceMoney());
				}
				// 如果是百分比，则获取每个金额抽成比例；如果是固定金额，则直接添加固定金额
				if (cutChargeType == 0) {
					for (MoneyAmount moneyAmount : moneyAmountList) {
						BigDecimal costPrice = moneyAmount.getAmount().multiply(cutChargeRate);
						// 如果有返佣差额，则需去除
						if (CollectionUtils.isNotEmpty(differenceList)) {
							MoneyAmount difference = differenceList.get(0);
							if (difference.getCurrencyId().intValue() == moneyAmount.getCurrencyId().intValue()) {
								costPrice = moneyAmount.getAmount().subtract(difference.getAmount()).multiply(cutChargeRate);
							}
						}
						MoneyAmount chargeMoneyAmount = new MoneyAmount(cutServiceCharge, moneyAmount.getCurrencyId(), costPrice, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
						chargeMoneyAmount.setExchangerate(currencyDao.findById(moneyAmount.getCurrencyId().longValue()).getConvertLowest());
						chargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
						cutChargeList.add(chargeMoneyAmount);
					}
				} else {
					Currency rmbCurrency = currencyDao.findRMB(UserUtils.getUser().getCompany().getId());
					MoneyAmount chargeMoneyAmount = new MoneyAmount(cutServiceCharge, rmbCurrency.getId().intValue(), cutChargeRate, orderId, Context.MONEY_TYPE_CHARGE, orderStatus, 1, userId);
					chargeMoneyAmount.setExchangerate(currencyDao.findById(rmbCurrency.getId()).getConvertLowest());
					chargeMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
					cutChargeList.add(chargeMoneyAmount);
				}
			}
		}
	}
	
	/**
	 * 获取QUAUQ服务费
	 * @author yakun.bai
	 * @Date 2016-8-30
	 */
	private BigDecimal getQuauqChargePrice(BigDecimal quauqPrice, Rate rate, Long currencyId) {
		if (quauqPrice != null) {
			if (rate != null) {
				if (rate.getQuauqRateType() == 0) {
					return quauqPrice.multiply(rate.getQuauqRate());
				} else {
					Currency currency = currencyDao.findById(currencyId);
					return rate.getQuauqRate().divide(currency.getConvertLowest(), 2);
				}
			} else {
				return quauqPrice.multiply(new BigDecimal(0.01));
			}
		}
		return new BigDecimal(0);
	}
	
	/**
	 * 获取渠道服务费
	 * @author yakun.bai
	 * @Date 2016-8-30
	 */
	private BigDecimal getPartnerChargePrice(BigDecimal quauqPrice, Rate rate, Long currencyId) {
		if (quauqPrice != null) {
			if (rate != null) {
				if (rate.getAgentRateType() == 0) {
					return quauqPrice.multiply(rate.getAgentRate());
				} else {
					Currency currency = currencyDao.findById(currencyId);
					return rate.getAgentRate().divide(currency.getConvertLowest(), 2);
				}
			} else {
				return quauqPrice.multiply(new BigDecimal(0.01));
			}
		}
		return new BigDecimal(0);
	}
	
	/**
	 * 获取QUAUQ其他费用服务费
	 * @author yakun.bai
	 * @Date 2016-8-30
	 */
	private BigDecimal getQuauqOtherChargePrice(BigDecimal quauqPrice, Rate rate, Long currencyId) {
		if (quauqPrice != null) {
			if (rate != null) {
				if (rate.getQuauqOtherRateType() == 0) {
					return quauqPrice.multiply(rate.getQuauqOtherRate());
				} else {
					Currency currency = currencyDao.findById(currencyId);
					return rate.getQuauqOtherRate().divide(currency.getConvertLowest(), 2);
				}
			} else {
				return quauqPrice.multiply(new BigDecimal(0.01));
			}
		}
		return new BigDecimal(0);
	}
	
	/**
	 * 获取渠道其他费用服务费
	 * @author yakun.bai
	 * @Date 2016-8-30
	 */
	private BigDecimal getPartnerOtherChargePrice(BigDecimal quauqPrice, Rate rate, Long currencyId) {
		if (quauqPrice != null) {
			if (rate != null) {
				if (rate.getAgentOtherRateType() == 0) {
					return quauqPrice.multiply(rate.getAgentOtherRate());
				} else {
					Currency currency = currencyDao.findById(currencyId);
					return rate.getAgentOtherRate().divide(currency.getConvertLowest(), 2);
				}
			} else {
				return quauqPrice.multiply(new BigDecimal(0.01));
			}
		}
		return new BigDecimal(0);
	}
	
	/**
	 * 保存订单费率（供财务用，表为order_service_charge） 
	 * @author yakun.bai
	 * @Date 2016-9-5
	 */
	private void saveOrderServiceCharge(ProductOrderCommon productOrder, String quauqServiceCharge, Integer quauqChargeFlag, Integer partnerChargeFlag) {
		// 保存quauq费率
		if (quauqChargeFlag == 0) {
			OrderServiceCharge orderServiceCharge = new OrderServiceCharge();
			orderServiceCharge.setUuid(UuidUtils.generUuid());
			orderServiceCharge.setProductType(productOrder.getOrderStatus());
			orderServiceCharge.setOrderId(productOrder.getId());
			orderServiceCharge.setType(0);
			orderServiceCharge.setServiceChargeUuid(productOrder.getQuauqServiceCharge());
			orderServiceCharge.setDelFlag(0);
			orderServiceCharge.setCreateDate(new Date());
			orderServiceCharge.setUpdateDate(new Date());
			orderServiceCharge.setCreateBy(UserUtils.getUser().getId());
			orderServiceCharge.setUpdateBy(UserUtils.getUser().getId());
			
			orderServiceChargeDao.save(orderServiceCharge);
		}
		
		if (partnerChargeFlag == 0) {
			// 保存渠道费率
			OrderServiceCharge orderPartnerCharge = new OrderServiceCharge();
			orderPartnerCharge.setUuid(UuidUtils.generUuid());
			orderPartnerCharge.setProductType(productOrder.getOrderStatus());
			orderPartnerCharge.setOrderId(productOrder.getId());
			orderPartnerCharge.setType(1);
			orderPartnerCharge.setServiceChargeUuid(productOrder.getPartnerServiceCharge());
			orderPartnerCharge.setDelFlag(0);
			orderPartnerCharge.setCreateDate(new Date());
			orderPartnerCharge.setUpdateDate(new Date());
			orderPartnerCharge.setCreateBy(UserUtils.getUser().getId());
			orderPartnerCharge.setUpdateBy(UserUtils.getUser().getId());
			
			orderServiceChargeDao.save(orderPartnerCharge);
		}
	}
	
	/**
	 * 保存订单费率（供财务用，表为order_service_charge） 
	 * @author yakun.bai
	 * @Date 2016-9-5
	 */
	private void updateOrderServiceCharge(ProductOrderCommon productOrder, String quauqServiceCharge, 
			Integer quauqChargeDelFlag, Integer partnerChargeDelFlag) {
		
		List<OrderServiceCharge> quauqChargelist = orderServiceChargeDao.findAllByOrderIdAndType(productOrder.getId(), 0);
		if (CollectionUtils.isNotEmpty(quauqChargelist)) {
			OrderServiceCharge orderServiceCharge = quauqChargelist.get(0);
			orderServiceCharge.setDelFlag(quauqChargeDelFlag);
			orderServiceChargeDao.updateObj(orderServiceCharge);
		}
		
		List<OrderServiceCharge> partnerChargelist = orderServiceChargeDao.findAllByOrderIdAndType(productOrder.getId(), 1);
		if (CollectionUtils.isNotEmpty(partnerChargelist)) {
			OrderServiceCharge orderServiceCharge = partnerChargelist.get(0);
			orderServiceCharge.setDelFlag(partnerChargeDelFlag);
			orderServiceChargeDao.updateObj(orderServiceCharge);
		}
		
	}
	
	/**
     * 处理订单和发起优惠申请
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String handleOrderAndReview(Map<String, String> parameters, HttpServletRequest request) throws Exception {
        // 保存订单
        ProductOrderCommon order = saveOrderForDiscount(parameters, request, true);
        // 发起优惠申请
        applyReview(parameters, order);

        return activityGroupDao.findById(order.getProductGroupId()).getGroupCode();
    }
    
    /**
     * 发起优惠申请
     */
    private void applyReview(Map<String, String> parameters, ProductOrderCommon order) throws Exception {
        try {
            String applyTravelerIds = parameters.get("applyTravelerIds");
            TravelActivity product = travelActivityDao.findOne(order.getProductId());
            if(StringUtils.isNotBlank(applyTravelerIds)) {
                //优惠金额
                String yhNum = UUID.randomUUID().toString();
                //同行价
                String thNum = UUID.randomUUID().toString();
                String[] applyTravelerIdArrs = applyTravelerIds.split(",");
                String travelerNames = "";
                String travelerIds = "";
                String travelerTypes = "";
                String currencyIds = "";
                String thPrices = "";
                String activityDiscountPrices = "";
                String jsPrices = "";
                String appliedDiscountPrices = "";
                String privilegeReasons = "";

                //同行总价
                BigDecimal totalPeerPrice = new BigDecimal(0);
                //优惠总价
                BigDecimal totalDiscountPrice = new BigDecimal(0);
                //同行结算总价
                BigDecimal totalJsPrices = new BigDecimal(0);
                //申请优惠总价
                BigDecimal totalAppliedDiscountPrices = new BigDecimal(0);

//                String totalAppliedDiscountPricesStr = "";

                // 申请优惠总额
                String totalAppliedPrices = "";
                Map<String,Double> totalAppliedPricesMap = new HashMap<String,Double>();
                for(String applyTravelerId : applyTravelerIdArrs) {
                    Traveler traveler = travelerDao.findById(Long.parseLong(applyTravelerId));
                    travelerIds += traveler.getId() + ",";
                    travelerNames += traveler.getName() + ",";
                    travelerTypes += traveler.getPersonType() + ",";
                    currencyIds += traveler.getSrcPriceCurrency().getId() + ",";
                    thPrices += traveler.getSrcPrice() + ",";
                    activityDiscountPrices += traveler.getOrgDiscountPrice() + ",";// 产品优惠金额
                    jsPrices += moneyAmountService.findOneAmountBySerialNum(traveler.getPayPriceSerialNum()).getAmount() + ",";
                    appliedDiscountPrices += traveler.getAppliedDiscountPrice() + ",";// 申请优惠金额
                    totalPeerPrice.add(traveler.getSrcPrice());
                    totalDiscountPrice.add(traveler.getOrgDiscountPrice() == null ? new BigDecimal(0) : traveler.getOrgDiscountPrice());
                    totalJsPrices.add(moneyAmountService.findOneAmountBySerialNum(traveler.getPayPriceSerialNum()).getAmount());
                    totalAppliedDiscountPrices.add(traveler.getAppliedDiscountPrice());
                    totalAppliedPrices += traveler.getAppliedDiscountPrice().toString() + "+";

                    if(totalAppliedPricesMap.containsKey(totalAppliedPricesMap.get(traveler.getSrcPriceCurrency().getCurrencyMark()))){
                        totalAppliedPricesMap.put(traveler.getSrcPriceCurrency().getCurrencyMark(),
                                Double.parseDouble(totalAppliedPricesMap.get(traveler.getSrcPriceCurrency().getCurrencyMark()).toString()
                                )+Double.parseDouble(traveler.getAppliedDiscountPrice()+""));

                    }else{
                        totalAppliedPricesMap.put(traveler.getSrcPriceCurrency().getCurrencyMark(),Double.parseDouble(traveler
                                .getAppliedDiscountPrice()+""));
                    }

                    privilegeReasons += "@_@#_=,";
                }
                DecimalFormat d = new DecimalFormat(",##0.00");
                String zh = "";
                for (Object key : totalAppliedPricesMap.keySet()) {
                    zh = zh + key + d.format(totalAppliedPricesMap.get(key)) + "+";
                }

                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, yhNum);
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_2, thNum);
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, product.getId());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, product.getAcitivityName());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, order.getId());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, order.getCreateBy().getId());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, order.getOrderCompany());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, order.getProductGroupId());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, product.getGroupOpenCode());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, order.getOrderNum());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, product.getCreateBy().getId());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, order.getOrderCompanyName());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE);
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, Context.ProductType.PRODUCT_LOOSE);
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, UserUtils.getUser().getId());
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser().getName());
                variables.put("applyPrivilegePersonNum", applyTravelerIds.split(",").length);
                variables.put("orderPersonNum", order.getOrderPersonNum());

                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerIds);
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, travelerNames);
                variables.put("travelerType", travelerTypes);
                variables.put("currencyId", currencyIds);
                variables.put("thPrice", thPrices);
                variables.put("inpuryhprice", activityDiscountPrices);
                variables.put("thjsjinputprice", jsPrices);
                variables.put("sqyhPrice", appliedDiscountPrices);
                variables.put("privilegeReason", privilegeReasons);
                variables.put("thTotalPrice", totalPeerPrice.toString());
                variables.put("inputtxjstotalprice", totalDiscountPrice.toString());
                variables.put("inputyhtotalprice", totalJsPrices.toString());
                variables.put("inputsqyhtotalprice", zh.substring(0,zh.length()-1));
                variables.put("inputsqyhtotalpriceNoMark", totalAppliedPrices);

                CommonResult commonResult = reviewMutexService.check(order.getId().toString(), "0",
                        Context.ProductType.PRODUCT_LOOSE.toString(), Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString(), false);
                if(commonResult.getSuccess()) {
                    ReviewResult reviewResult = processReviewService.start(UserUtils.getUser().getId().toString(),
                            UserUtils.getUser().getCompany().getUuid().toString(), userReviewPermissionChecker, null, Context.ORDER_TYPE_SP,
                            Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE,product.getDeptId(),"",variables);
                    String reviewId = reviewResult.getReviewId();
                    boolean result = reviewResult.getSuccess();
                    if(result) {
                        for(String applyTravelerIdArr : applyTravelerIdArrs) {
                            Traveler traveler = travelerDao.findById(Long.parseLong(applyTravelerIdArr));

                            NewProcessMoneyAmount yhMoneyAmount = new NewProcessMoneyAmount();
                            yhMoneyAmount.setCurrencyId(traveler.getSrcPriceCurrency().getId().intValue());
                            yhMoneyAmount.setAmount(traveler.getAppliedDiscountPrice());
                            yhMoneyAmount.setUid(order.getId());
                            yhMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE);
                            yhMoneyAmount.setOrderType(Context.ProductType.PRODUCT_LOOSE);
                            yhMoneyAmount.setSerialNum(yhNum);
                            yhMoneyAmount.setBusindessType(1);
                            yhMoneyAmount.setCreateTime(new Date());
                            yhMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
                            yhMoneyAmount.setDelFlag("0");
                            yhMoneyAmount.setCompanyId(UserUtils.getCompanyUuid());
                            yhMoneyAmount.setReviewId(reviewId);
                            processMoneyAmountService.saveNewProcessMoneyAmount(yhMoneyAmount);

                            NewProcessMoneyAmount thMoneyAmount = new NewProcessMoneyAmount();
                            thMoneyAmount.setCurrencyId(traveler.getSrcPriceCurrency().getId().intValue());
                            thMoneyAmount.setAmount(moneyAmountService.findOneAmountBySerialNum(traveler.getPayPriceSerialNum()).getAmount());
                            thMoneyAmount.setUid(order.getId());
                            thMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE);
                            thMoneyAmount.setOrderType(Context.ProductType.PRODUCT_LOOSE);
                            thMoneyAmount.setSerialNum(thNum);
                            thMoneyAmount.setBusindessType(1);
                            thMoneyAmount.setCreateTime(new Date());
                            thMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
                            thMoneyAmount.setDelFlag("0");
                            thMoneyAmount.setCompanyId(UserUtils.getCompanyUuid());
                            thMoneyAmount.setReviewId(reviewId);
                            processMoneyAmountService.saveNewProcessMoneyAmount(thMoneyAmount);
                        }
                    } else {
                        throw new Exception(reviewResult.getMessage());
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }


    /**
     * 订单保存：针对优加与奢华
     * @param parameters
     * @param request
     * @param isCreate
     * @author yakun.bai
     * @Date 2016-9-19
     */
    private ProductOrderCommon saveOrderForDiscount(Map<String, String> parameters, HttpServletRequest request, boolean isCreate) throws Exception {
        ProductOrderCommon order = null;
        try {
            String productOrderId = parameters.get("productOrderId");
            ProductOrderCommon productOrder = orderDao.findOne(Long.parseLong(productOrderId));

            if (isCreate) {
                orderStockService.ifCanCut(productOrder, Context.StockOpType.CREATE);
                orderStockService.changeGroupFreeNum(productOrder, productOrder.getOrderPersonNum(), Context.StockOpType.CREATE);
            }
            //结算价信息
            String currencyId = parameters.get("currencyId");
            String currencyPrice = parameters.get("currencyPrice");
            Map<String,BigDecimal> totalMoneyMap = new HashMap<String, BigDecimal>();
            if(StringUtils.isNotBlank(currencyId) && StringUtils.isNotBlank(currencyPrice)){
                String[] currencyIdArr = currencyId.trim().split(",");
                String[] currencyPrcieArr = currencyPrice.trim().split(",");
                for(int i = 0; i < currencyIdArr.length; i++) {
                    totalMoneyMap.put(currencyIdArr[i], new BigDecimal(currencyPrcieArr[i]));
                }
            }
            //成本价信息
            String costCurrencyId = parameters.get("costCurrencyId");
            String costCurrencyPrice = parameters.get("costCurrencyPrice");
            Map<String,BigDecimal> costTotalMoneyMap = new HashMap<String, BigDecimal>();
            if(StringUtils.isNotBlank(costCurrencyId) && StringUtils.isNotBlank(costCurrencyPrice)){
                String[] costCurrencyIdArr = costCurrencyId.split(",");
                String[] costCurrencyPriceArr = costCurrencyPrice.split(",");
                for(int i = 0; i < costCurrencyIdArr.length; i++) {
                    costTotalMoneyMap.put(costCurrencyIdArr[i], new BigDecimal(costCurrencyPriceArr[i]));
                }
            }
            // 住房要求
            String specialDemand = parameters.get("specialDemand");
            if(specialDemand != null){
                productOrder.setSpecialDemand(specialDemand);
            }
            // 团队返佣信息
            String rebatesCurrency = parameters.get("rebatesCurrency");
            String rebatesMoney = parameters.get("rebatesMoney");
            if(StringUtils.isNotBlank(rebatesMoney) && StringUtils.isNotBlank(rebatesCurrency)){
                Currency currency = new Currency();
                currency = currencyDao.findById(Long.valueOf(rebatesCurrency));
                if(currency!=null){
                    MoneyAmount moneyAmount = new MoneyAmount();
                    moneyAmount.setSerialNum(UUID.randomUUID().toString());
                    moneyAmount.setCurrencyId(Integer.valueOf(rebatesCurrency));
                    BigDecimal dec = new BigDecimal(rebatesMoney);
                    moneyAmount.setAmount(dec);
                    moneyAmount.setMoneyType(Integer.valueOf(Context.MONEY_TYPE_FYFY)); // 款项类型
                    moneyAmount.setOrderType(productOrder.getOrderStatus()); // 产品类型
                    moneyAmount.setUid(productOrder.getId()); // 订单ID
                    moneyAmount.setBusindessType(Context.BUSINESS_TYPE_ORDER);  // 业务类型
                    moneyAmount.setCurrencyId(Integer.valueOf(rebatesCurrency)); // 币种ID
                    moneyAmount.setExchangerate(currency.getCurrencyExchangerate()); // 汇率
                    moneyAmount.setCreatedBy(UserUtils.getUser().getId());
                    moneyAmount.setCreateTime(new Date());
                    moneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
                    moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
                    // 将预定返佣的uuid存入订单
                    productOrder.setScheduleBackUuid(moneyAmount.getSerialNum());
                }
            }

            String orderCompany = parameters.get("orderCompany");
            if(StringUtils.isNotBlank(orderCompany)){
                productOrder.setOrderCompany(Long.parseLong(orderCompany));
            }
            String orderCompanyName = parameters.get("orderCompanyName");
            if(StringUtils.isNotBlank(orderCompanyName)){
                productOrder.setOrderCompanyName(orderCompanyName);
            }
            productOrder.setDelFlag("4");
            order = saveOrderAndPrice(productOrder, totalMoneyMap, costTotalMoneyMap, false);
            // 保存团控信息
            String groupHandleId = parameters.get("groupHandleId");
            if(StringUtils.isNotBlank(groupHandleId)) {
                handleGroupHandleAndVisa(groupHandleId, order);
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("保存游客信息出错!");
        }

        return order;
    }
    
    /**
     * 修改 GroupHandle 和 GroupHandleVisa 表信息
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private void handleGroupHandleAndVisa(String groupHandleId, ProductOrderCommon order) throws Exception {

        try {
            GroupHandle groupHandle = groupHandleDao.getById(Integer.parseInt(groupHandleId));
            if("2".equals(groupHandle.getDelFlag())) {
                groupHandle.setOrderId(order.getId().intValue());
                groupHandle.setOrderNum(order.getOrderNum());
                groupHandle.setDelFlag("0");
                groupHandleDao.save(groupHandle);
            }


            List<GroupHandleVisa> groupHandleVisaList = groupHandleVisaDao.findByGroupHandleId(groupHandle.getId());
            if(groupHandleVisaList != null && groupHandleVisaList.size() > 0) {
                for(GroupHandleVisa groupHandleVisa : groupHandleVisaList) {
                    if("2".equals(groupHandleVisa.getDelFlag())) {
                        groupHandleVisa.setOrderId(order.getId().intValue());
                        groupHandleVisa.setOrderNum(order.getOrderNum());
                        groupHandleVisa.setDelFlag("0");
                        groupHandleVisaDao.save(groupHandleVisa);
                    }
                }
            }

            List<Traveler> travelerList = orderService.findTravelerByOrderId(order.getId());
            if(travelerList != null && travelerList.size() > 0) {
                for(Traveler traveler : travelerList) {
                    List<TravelerVisa> travelerVisaList = travelerVisaDao.findByTravelerId(traveler.getId());
                    if(travelerVisaList != null && travelerVisaList.size() > 0) {
                        for(TravelerVisa travelerVisa : travelerVisaList) {
                            if("2".equals(travelerVisa.getDelFlag())) {
                                travelerVisa.setDelFlag("0");
                            }
                        }
                    }
                }
            }
        } catch(NumberFormatException e) {
            throw new Exception("修改团款列表信息出错!");
        }

    }
    
}