package com.trekiz.admin.modules.order.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.mapper.BeanMapper;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.utils.JSONUtils;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.activity.service.GroupControlBoardService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.utils.AirTicketUtils;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.airticketorder.service.AirTicketOrderLendMoneyService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.entity.CostRecordHotel;
import com.trekiz.admin.modules.cost.entity.CostRecordIsland;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.finance.FinanceUtils;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.island.dao.IslandTravelerDao;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.PreProductOrderCommon;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.order.rebates.repository.RebatesDao;
import com.trekiz.admin.modules.order.repository.CostchangeDao;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.PreProductOrderCommonDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.StatisticAnalysisDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.repository.ActivityGroupReserveDao;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.repository.IReviewDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.entity.TravelerFile;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.repository.TravelerFileDao;
import com.trekiz.admin.modules.traveler.repository.TravelerVisaDao;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.review.common.exception.CommonReviewException;
import com.trekiz.admin.review.common.service.impl.CommonReviewServiceImpl;

import freemarker.template.TemplateException;

@Service
@Transactional(readOnly = true)
public class OrderCommonService  extends BaseService {
	private static final Logger log = Logger.getLogger(CommonReviewServiceImpl.class);
    @Autowired
    private ProductOrderCommonDao productorderDao;
    @Autowired
    private ActivityAirTicketDao activityAirTicketDao;

    @Autowired
    private PreProductOrderCommonDao preproductorderDao;
    @Autowired
    private TravelActivityDao travelActivityDao;
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private AreaService areaService;    
    @Autowired
    private OrderContactsDao orderContactsDao;
    
    @Autowired
    private TravelerDao travelerDao;
    
    @Autowired
    private TravelerVisaDao travelerVisaDao;
    
    @Autowired
    private TravelerFileDao travelerFileDao;
    
    @Autowired
    private CostchangeDao costchangeDao;
    
    @Autowired
    private OrderpayDao orderpayDao;
    
    @Autowired
    private DocInfoDao docInfoDao;

	@Autowired
	private StatisticAnalysisDao statisticAnalysisDao;
    
    @Autowired
    private VisaProductsDao visaProductsDao;
    
    @Autowired
    private VisaOrderDao visaOrderDao;
    
    @Autowired
    private SystemService systemService;
    
    @Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
    
    @Autowired
    private AgentinfoDao agentinfoDao;
    
    @Autowired
    private ActivityGroupReserveDao activityGroupReserveDao;
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private VisaOrderService visaOrderService;
    
    @Autowired
	private MoneyAmountService moneyAmountService;
    
    @Autowired
    private OrderStatisticsService orderStatusticsService;
    
    @Autowired
    private OrderStockService orderStockService;
    
    @Autowired
    private CostRecordDao costRecordDao;
    @Autowired 
    private CostRecordIslandDao costRecordIslandDao;
    @Autowired 
    private CostRecordHotelDao costRecordHotleDao;
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private IReviewDao iReviewDao;
    @Autowired
    private HotelTravelerDao hotelTravelerDao;
    @Autowired
    private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
    
    @Autowired
    private IslandTravelerDao islandTravelerDao;
    
    @Autowired
	private CostManageService costManageService;
    
    @Autowired
    private CurrencyService currencyService;
    @Autowired
	private AirTicketOrderLendMoneyService airTicketOrderLendMoneyService;
    @Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private OrderinvoiceService orderinvoiceService;
    @Autowired
    private MoneyAmountDao moneyAmountDao;
    @Autowired
    private RebatesDao rebatesDao;
    @Autowired
    private ActivityGroupDao activityGroupDao;
    @Autowired
    private GroupControlBoardService groupControlBoardService;
    @Autowired
    private OrderProgressTrackingService progressService;
    @Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;

	public ProductOrderCommon getProductorderById(Long id){
        return productorderDao.findOne(id);
    }
    
    public PreProductOrderCommon getPreProductorderById(Long id){
        return preproductorderDao.findOne(id);
    }
    
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void lockOrder(Long orderId) {
    	ProductOrderCommon order = getProductorderById(orderId);
    	order.setLockStatus(Context.LOCK_ORDER);
    	saveProductorder(order);
        //添加操作日志
        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, 
        		"订单" + orderId + "锁死成功", "2", order.getOrderStatus(), order.getId());
    }
    
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void unLockOrder(Long orderId){
        ProductOrderCommon order = getProductorderById(orderId);
    	order.setLockStatus(Context.UNLOCK_ORDER);
    	saveProductorder(order);
        //添加操作日志
        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
        		"订单" + orderId + "解锁成功", "2", order.getOrderStatus(), order.getId());
    }
    
    /**
	 * 修改未查看订单状态
	 * @param notSeenOrderIdList
	 * @return
	 */
    public Integer changeNotSeenOrderFlag(Set<Long> notSeenOrderIdList) {
    	if (CollectionUtils.isNotEmpty(notSeenOrderIdList)) {
    		int i = productorderDao.changeNotSeenOrderFlag(notSeenOrderIdList);
    		return i;
    	}
    	return 0;
    }
    
    /**
     *  功能:  点击预订按钮后  保存简单的预订信息
     *
     *  @author xuziqian
     *  @DateTime 2014-1-16 上午11:48:03
     *  @param productOrder   订单对象
     */
    @Transactional
    public ProductOrderCommon saveProductorderOnReserve (ProductOrderCommon productOrder, TravelActivity activity, Agentinfo agentinfo) {
        productOrder.setProductId(activity.getId());
        productOrder = OrderCommonUtil.setOrderRemainDays(productOrder, activity);
        if(agentinfo != null){
          productOrder.setOrderCompany(agentinfo.getId());
          productOrder.setOrderCompanyName(agentinfo.getAgentName());
        }
        return productOrder;
    }
    
    /**
     * 
     *  功能: 保存预订订单
     *
     *  @author taoxiaoyang
     *  @DateTime 2014-10-22 上午9:44:35
     *  @param productOrder 订单对象
     *  @return
     */
    @Transactional
    public ProductOrderCommon saveProductorder (ProductOrderCommon productOrder) {
        return productorderDao.save(productOrder);
    }
    
    public List<Traveler> findTravelerByOrderId(Long orderId){
        return travelerDao.findTravelerByOrderId(orderId);
    }
    
    public List<Costchange> findCostchangeByTravelerId(Long travelerId){
        return costchangeDao.findCostchangeBytravelerId(travelerId);
    }
    
    /**
     * 签证相关，保存单一签证游客
     */
    public Traveler saveTraveler(Traveler traveler){
    	Traveler travel = new Traveler();
    	if(traveler!=null && StringUtils.isNotBlank(traveler.getName()) && StringUtils.isNotBlank(traveler.getOrderId()+"")){
    		travel = travelerDao.save(traveler);
    	}
		return travel;
    }
    
    
    /**
     * 修改  凭证 上传图片文件
     * @param docInfoList
     * @param orderPay
     * @param orderId
     * @param mode
     * @param request
     * @return
     * @throws OptimisticLockHandleException
     * @throws PositionOutOfBoundException
     * @throws Exception
     */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> updatepayVoucherFile(
			ArrayList<DocInfo> docInfoList, Orderpay orderPay, String orderId,
			ModelMap mode, HttpServletRequest request)
			throws OptimisticLockHandleException, PositionOutOfBoundException,
			Exception {

		ArrayList<DocInfo> old_docInfoList=(ArrayList<DocInfo>) docInfoDao.findDocInfoByPayOrderId(orderPay.getId());
		if (old_docInfoList != null && old_docInfoList.size() > 0) {
			Iterator<DocInfo> iter = old_docInfoList.iterator();
			while (iter.hasNext()) {
				DocInfo old_docInfo = iter.next();
				docInfoDao.delete(old_docInfo);
			}
		}
		ProductOrderCommon pra = productorderDao.findOne(new Long(orderId));

		if (docInfoList != null && docInfoList.size() > 0) {
			Iterator<DocInfo> iter = docInfoList.iterator();
			String docInfoIds = null;
			while (iter.hasNext()) {
				DocInfo docInfo = iter.next();
				docInfo.setPayOrderId(orderPay.getId());
				docInfoDao.save(docInfo);
				// save 保存之后，docInfo对象在数据库生成的主键ID居然也加入了docInfo对象中。
				// save之前，getId()应该是null，save后getId()有值了。
				if(StringUtils.isNotBlank(docInfoIds)){
					docInfoIds += "," + docInfo.getId();
				}else{
					docInfoIds =  docInfo.getId().toString();
				}
			}
			orderPay.setPayVoucher(docInfoIds);
		}
		orderpayDao.updateBySql(
				"update orderpay set payVoucher = ?,remarks=? where id = ?", orderPay
						.getPayVoucher(),orderPay.getRemarks(), orderPay.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();

		//支付订单金额千位符处理
	    if (StringUtils.isNotBlank(orderPay.getMoneySerialNum())) {
	 	    clearObject(orderPay);
	 	   orderPay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderPay.getMoneySerialNum()));
	    }
		mode.addAttribute("orderPay", orderPay);
		mode.addAttribute("pra", pra);
		map.put("isSuccess", true);
		return map;
	}
    
    /**
     * 确认占位
     * @param orderId
     * @param request
     * @throws Exception 
     */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public String confirmOrder(Long orderId, HttpServletRequest request) throws Exception {
		
		// 查询订单、团期
		ProductOrderCommon productOrder = productorderDao.findOne(orderId);
		
		// 如果是天马运通，则添加保留时限
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if (Context.SUPPLIER_UUID_TMYT.equals(companyUuid)) {
			Double remainDays = productOrder.getRemainDays();
			productOrder.setRemainDays(getRemainDay(remainDays, request));
		} else {
			// 添加补单属性（不被系统自动取消）
			productOrder.setIsAfterSupplement(1);
		}
		productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
		productOrder.setPayMode(Context.PAY_MODE_BEFOREHAND);
		orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.JD_CONFIRM);
		
		//-------by------junhao.zhao-----2017-03-22-----向表order_data_statistics中添加数据---开始-------------------------------------------------------------
		// “计调确认占位”确认之后加到表order_data_statistics中。
		orderDateSaveOrUpdateService.addOrderDataStatistics(productOrder);
		
		//-------by------junhao.zhao-----2017-03-22-----向表order_data_statistics中添加数据---结束-------------------------------------------------------------
	
		
		//添加操作日志
		this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, "订单" + productOrder.getOrderNum() + "确认占位成功", 
				"2", productOrder.getOrderStatus(), productOrder.getId());
		return "success";
	}
    
    /**
     * 激活订单
     * @param orderId
     * @param request
     */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public String invokeOrder(Long orderId, HttpServletRequest request) {
		
		//查询订单、团期、产品
		ProductOrderCommon productOrder = productorderDao.findOne(orderId);
		ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
		
		//查询订单最后一次支付记录的支付款类型：1：全款、3：订金、2：尾款
		Integer payPriceType = null;
		List<Orderpay> orderPayList = orderpayDao.findOrderpayByOrderId(orderId, productOrder.getOrderStatus());
		if (CollectionUtils.isNotEmpty(orderPayList)) {
			Orderpay orderPay = orderPayList.get(0);
			payPriceType = orderPay.getPayPriceType();
		}
		
		//激活条件：支付方式为订金占位或预占位的订单：包括已取消和没有取消的
		Set<Integer> payStatusSet = Sets.newHashSet();
		payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));//已取消
		payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));//订金未占位
		payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));//已占位 非订金占位
		
		// 订单支付状态与付款类型
		Integer payStatus = productOrder.getPayStatus();
		
		if (payStatusSet.contains(payStatus)) {
			try {
				// 判断团期是否可扣减余位
				orderStockService.ifCanCut(productOrder, Context.StockOpType.INVOKE);
				// 如果订单支付状态为已取消，则需要分情况占位
				if (Integer.parseInt(Context.ORDER_PAYSTATUS_YQX) == productOrder.getPayStatus()) {
					String payModeType = productOrder.getPayMode();
					setOrderPayStatusAndActivationDate(activityGroup, productOrder, payModeType, payPriceType);
				}
				Double remainDays = productOrder.getRemainDays();
				productOrder.setRemainDays(getRemainDay(remainDays, request));
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
			productorderDao.invokeOrder(productOrder.getId(), productOrder.getRemainDays(), productOrder.getPayStatus(), productOrder.getActivationDate());
			//-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为激活---开始---
			if (orderDateSaveOrUpdateService.whetherAddOrUpdate(productOrder)) {
				orderDateSaveOrUpdateDao.updateOrderStatusInvoke(productOrder.getId(),productOrder.getPayStatus());
			}
	        //-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为激活---结束--- 
			
			
			
			// 修改订单跟踪状态
			progressService.updateOrderStatus(productOrder, Context.ORDER_ZZ_NORMAL);
		}
		//添加操作日志
		this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, "订单" + productOrder.getOrderNum() + "激活成功", 
				"2", productOrder.getOrderStatus(), productOrder.getId());
		return "success";
	}
	
	/**
	 * 获取要延长的时限
	 * @author yakun.bai
	 * @Date 2016-5-20
	 */
	private Double getRemainDay(Double remainDays, HttpServletRequest request) {
		// 获取延长时限
		String type = request.getParameter("type");
		String invokeDay = request.getParameter("invokeDay");
		String invokeHour = request.getParameter("invokeHour");
		String invokeMin = request.getParameter("invokeMin");
		Double day = 0.00;
		Double hour = 0.00;
		Double minute = 0.00;
		if (StringUtils.isNotBlank(invokeDay)) {
			day = Double.valueOf(invokeDay);
		}
		if (StringUtils.isNotBlank(invokeHour)) {
			hour = Double.valueOf(invokeHour);
		}
		if (StringUtils.isNotBlank(invokeMin)) {
			minute = Double.valueOf(invokeMin);
		}
		
		Double invokeRemainDay = OrderCommonUtil.getRemainDay(day, hour, minute);
		
		// 如果是激活，则保留时限为激活设定时限，如果是延时，则在原先保留时限基础上相加
		if ("invoke".equals(type)) {
			return invokeRemainDay;
		} else {
			if (remainDays != null) {
				remainDays = remainDays + invokeRemainDay;
			} else {
				remainDays = invokeRemainDay;
			}
			return remainDays;
		}
	}

	/**
	 * 判断订单是否因超过保留时限而取消
	 * @author yakun.bai
	 * @Date 2016-5-18
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private boolean isOutOfDate(ProductOrderCommon productOrder) {
		boolean flag = false;
		// 激活日期：如果没有被激活过，则是订单时间
		Date activationDate = productOrder.getActivationDate();
		// 订单保留时间
		Double remainDays = productOrder.getRemainDays();
		if (activationDate != null && remainDays != null) {
			// 当前时间
			Calendar currentDate = Calendar.getInstance();
			// 激活时间
			Calendar invokeDate = Calendar.getInstance();
			invokeDate.setTime(activationDate);
			// 保留时间
			long remains = (long) (remainDays  * (1000*3600*24));
			// 如果激活时间加上保留时间小于当前时间则证明订单因超过保留时限而取消
			if (currentDate.getTimeInMillis() - invokeDate.getTimeInMillis() - remains > 0) {
				flag = true;
			}
		} else {
			flag = true;
		}
		return flag;
	}
	
	
	/**
	 * 设置订单支付状态
	 * @param activityGroup
	 * @param productOrder
	 * @param payModeType 占位方式	订金占位 1；预占位 2；全款占位 3；资料占位 4；担保占位 5；确认单占位 6；
	 * @param payPriceType 付款方式类型	全款：1；尾款 2；订金 3
	 * @throws Exception
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	private void setOrderPayStatusAndActivationDate(ActivityGroup activityGroup, ProductOrderCommon productOrder, String payModeType,
			Integer payPriceType) throws Exception {
		
		Integer paymentType = productOrder.getPaymentType();
		
		if (Context.MONEY_TYPE_DJ == payPriceType) {
			productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));//改为已支付订金
			orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.INVOKE);
		} else if (Context.MONEY_TYPE_WK == payPriceType || Context.MONEY_TYPE_QK == payPriceType) {
			productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));//改为已支付全款
			orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.INVOKE);
		} else {
			if (Context.PAY_MODE_FULL.equals(payModeType)) {
				productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF));
			} else if (Context.PAY_MODE_EARNEST.equals(payModeType)) {
				productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));
			} else if (Context.PAY_MODE_CW.equals(payModeType)) {
				productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_CW));
			} else if (Context.PAY_MODE_OP.equals(payModeType)) {
				productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_OP));
			} else {
				productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
			}
			if (Context.PLACEHOLDERTYPE_QW.equals(productOrder.getPlaceHolderType().toString()) || Context.PAYMENT_TYPE_JS != paymentType) {
				orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.INVOKE);
			} else {
				if (!Context.PAY_MODE_FULL.equals(payModeType) 
						&& !Context.PAY_MODE_EARNEST.equals(payModeType)
						&& !Context.ORDER_PAYSTATUS_CW.equals(payModeType)
						&& !Context.ORDER_PAYSTATUS_OP.equals(payModeType)) {
					orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.INVOKE);
				}
			}
		}
		productOrder.setActivationDate(new Date());
	}
    
    /**
     * 订单查询
     * @param type
     * @param orderStatus
     * @param page
     * @param travelActivity
     * @param orderBy
     * @param orderShowType
     * @param map
     * @param common
     * @return
     */
    public Page<Map<Object, Object>> findOrderListByPayType(String type, String orderStatus, Page<Map<Object, Object>> page,
    		TravelActivity travelActivity, String orderBy, String orderShowType, Map<String,String> map, DepartmentCommon common) {
    	if(map!=null&&"group".equals(map.get("orderOrGroup")) && orderBy.contains("pro")) {
    		orderBy = orderBy.replace("pro", "agp");
    	} 
    	page.setOrderBy(orderBy);
        List<Integer> list = new ArrayList<Integer>();
        String where = this.getTraveActivitySql(type, orderStatus, travelActivity, map, common);

        String orderSql = "";
        
        if(StringUtils.isNotBlank(type) && 
        		!Context.ORDER_PAYSTATUS_ALL.equals(type) && 
        		!Context.ORDER_PAYSTATUS_SYNC_CHECK.equals(type) &&
        		!Context.ORDER_PAYSTATUS_FINANCE.equals(type)){
            list.add(Integer.parseInt(type));
            orderSql = getOrderSql(list, where, orderStatus);
            map.put("orderSql", orderSql);
            if("order".equals(map.get("orderOrGroup"))) {
            	return getOrderList(page, orderSql);
            } else {
            	return this.getGroupListByOrder(list, page, orderSql);
            }
            
        } else if(Context.ORDER_PAYSTATUS_ALL.equals(type)) {//全部订单
        	orderSql = getOrderSql(list, where, orderStatus);
            map.put("orderSql", orderSql);
            if("order".equals(map.get("orderOrGroup"))) {
            	return getOrderList(page, orderSql);
            } else {
            	return this.getGroupListByOrder(list, page, orderSql);
            }
        } else if(Context.ORDER_PAYSTATUS_SYNC_CHECK.equalsIgnoreCase(type)) {//正反向同步查看状态
            if(StringUtils.isNotBlank(orderShowType)){
                list.add(Integer.parseInt(orderShowType));
            }else{
                //未达账金额
                list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
                list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
            }
            String ordertmp = page.getOrderBy();
            ordertmp = ordertmp.replace("pro.", "tmp.");
            ordertmp = ordertmp.replace("activity.", "tmp.");
            ordertmp = ordertmp.replace("agp.", "tmp.");
            ordertmp = ordertmp.replace("tmp.", "t1.");
            page.setOrderBy(ordertmp);

            
            /**
             * @author wuqiang 2015-03-18
             * 增加计调人员查询条件
             */
            String jdUserId = map.get("jdUserId");//获取计调人员id
            if(StringUtils.isNotBlank(jdUserId)){
            	if((Context.ORDER_STATUS_AIR_TICKET).equals(orderStatus)){
            		//机票类型
            	//	where = where + " and aa.createBy = " + jdUserId + " ";
            	}else if((Context.ORDER_STATUS_VISA).equals(orderStatus)){
            		where = where + " and vp.createBy = " + jdUserId + " ";
            	}else if(String.valueOf(Context.ORDER_TYPE_ALL).equals(orderStatus)){
            		//类型选择为全部的时候，不添加类型的条件，此处不添加任何处理 add by shijun.liu 2015.04.09
            	}else {
            		where = where + " and agp.createBy = " + jdUserId + " ";
            	}
            }
            return this.getByPayStatusAndNoatAccount( page,orderStatus, map);
        } else if(Context.ORDER_PAYSTATUS_FINANCE.equalsIgnoreCase(type)) {//财务查看订单
        	if(StringUtils.isNotBlank(orderShowType)) {
                list.add(Integer.parseInt(orderShowType));
            } else {
            	if("detail".equals(map.get("option"))) {
            		list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
            	}
                list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
                list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF));
                list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));
                list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
            }
            //return this.getOrderRecord(list, page, where, orderStatus);
            return this.getOrderRecord(page, orderStatus, map);
        } else {
            return page;
        }
    }
    
    
    
    public  List<Map<Object, Object>> getActivityPayPrint(String payid, Integer orderType) {
    	
    	Long companyId = UserUtils.getCompanyIdForData();
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT pro.orderTime createDate, agp.groupCode, agp.id, agp.groupOpenDate, pro.id as orderid, us.name, ")
    	   .append(" activity.acitivityName, pay.orderNum,users.name createName, pay.isAsAccount, pay.toBankAccount, pay.toBankNname, ")
    	   .append(" pay.bankName, pay.bankAccount, pay.payerName, pay.remarks, CONCAT(CAST(pay.payPriceType AS CHAR), ")
    	   .append(" IF (pay.isAsAccount IS NULL or pay.isAsAccount = 0 or pay.isAsAccount = 2 OR pay.isAsAccount = 99, 0, 1)) payPriceType, ")
    	   .append(" (SELECT GROUP_CONCAT(CONCAT(c.currency_name,FORMAT(ma.amount, 2)) SEPARATOR '+') total_money FROM money_amount ma, ")
    	   .append(" currency c WHERE ma.businessType = 1 and ma.currencyId = c.currency_id AND c.create_company_id = ")
    	   .append(companyId)
    	   .append(" AND moneySerialNum = ma.serialNum GROUP BY ma.serialNum) as payedMoney,pay.updateDate conDate,pay.accountDate, ")
    	   .append(" pay.createDate payDate,pay.to_alipay_account AS toAlipayAccount, ")
    	   .append(" pay.to_alipay_name AS toAlipayName ")//224因公支付宝
    	   .append(" FROM orderpay pay  ")
    	   .append(" LEFT JOIN  productorder pro  ON pay.orderNum= pro.orderNum and pay.orderId = pro.id and pro.orderStatus = ").append(orderType)
    	   .append(" LEFT JOIN activitygroup agp ON agp.id=pro.productGroupId  ")
    	   .append(" LEFT JOIN travelactivity activity ON activity.id = pro.productId   ")
    	   .append(" LEFT JOIN sys_user us ON us.id=pro.createby  LEFT JOIN sys_user users ON pay.updateBy = users.id ")
    	   .append(" WHERE pay.id=").append(payid);
    	return  productorderDao.findBySql(sql.toString(), Map.class);
    }
   
    public  List<Map<Object, Object>> getAirPayPrint(String payid) {
    	
    	Long companyId = UserUtils.getCompanyIdForData();
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT pro.create_Date createDate,air.group_code as groupCode,air.id,air.airType, air.departureCity,air.arrivedCity, ")
    	   .append(" if(air.ticket_area_type,'',' ') as ticket_area_type,air.startingDate as groupOpenDate, us.name,users.name createName, ")
    	   .append(" pro.id AS orderid,pay.orderNum, pay.isAsAccount,pay.toBankAccount,pay.toBankNname,pay.bankName,pay.bankAccount, ")
    	   .append(" pay.payerName,pay.remarks,(SELECT GROUP_CONCAT(CONCAT(c.currency_name,FORMAT(ma.amount, 2)) SEPARATOR '+') total_money ")
    	   .append(" FROM money_amount ma, currency c WHERE ma.businessType = 1 and ma.currencyId = c.currency_id ") 
    	   .append(" AND c.create_company_id = ")
    	   .append(companyId)
    	   .append(" AND moneySerialNum = ma.serialNum GROUP BY ma.serialNum) as payedMoney,CONCAT(CAST(pay.payPriceType AS CHAR), ")
    	   .append(" IF (pay.isAsAccount IS NULL or pay.isAsAccount = 0 or pay.isAsAccount = 2 OR pay.isAsAccount = 99, 0, 1)) payPriceType, ")
    	   .append(" pay.updateDate conDate,pay.accountDate,pay.createDate payDate,pay.to_alipay_account AS toAlipayAccount, ")
    	   .append(" pay.to_alipay_name AS toAlipayName ")
    	   .append(" FROM orderpay pay ")
    	   .append("  LEFT JOIN  airticket_order pro  ON pay.orderNum= pro.order_no  ")
    	   .append("  LEFT JOIN activity_airticket air ON air.id=pro.airticket_id    ") 
    	   .append("  LEFT JOIN sys_user us ON us.id=pro.create_by  LEFT JOIN sys_user users ON pay.updateBy = users.id ")
    	   .append("  WHERE pay.id= ").append(payid);    	
    	return  productorderDao.findBySql(sql.toString(), Map.class);
   }
   
   // 取得酒店打印信息
  public  List<Map<Object, Object>> getHotelPayPrint(String payid) {
   String Sql="SELECT pho.createDate, ahgp.groupCode, ahgp.id, ahgp.groupOpenDate, ah.id orderid, us.name, ah.activityName, pho.orderNum, ho.uuid orderUuid, " +
  "users.name createName, pho.isAsAccount, pho.toBankAccount, pho.toBankNname, pho.bankName, pho.bankAccount, pho.payerName, pho.remarks," +
  "CONCAT(CAST(pho.payPriceType AS CHAR), IF (pho.isAsAccount IS NULL or pho.isAsAccount = 0 or pho.isAsAccount = 2 OR pho.isAsAccount = 99, 0, 1)) payPriceType, t2.total_money as payedMoney, pho.updateDate conDate, pho.accountDate "
   +" FROM pay_hotel_order pho "
   +" LEFT JOIN hotel_order ho ON ho.uuid = pho.order_uuid "
   +" LEFT JOIN activity_hotel ah ON ah.uuid = ho.activity_hotel_uuid "
   +" LEFT JOIN activity_hotel_group ahgp ON ahgp.uuid = ho.activity_hotel_group_uuid "
   +" LEFT JOIN sys_user us ON us.id = ho.createBy "
   +" LEFT JOIN sys_user users ON users.id = pho.updateBy "
   +" LEFT JOIN (SELECT ma.serialNum, GROUP_CONCAT(CONCAT(c.currency_name,FORMAT(ma.amount,2)) SEPARATOR '+') total_money "
  		 +" 	FROM hotel_money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id WHERE "
  		 +" 	ma.businessType = 1   GROUP BY ma.serialNum ) t2 ON pho.moneySerialNum = t2.serialNum "
   +" WHERE pho.id="+payid+" and ah.wholesaler_id='"+UserUtils.getUser().getCompany().getId()+"'";
  	
  	  return  productorderDao.findBySql(Sql,Map.class);
  }
  
   // 取得海岛游打印信息
 public  List<Map<Object, Object>> getIslandPayPrint(String payid) {
   String Sql="SELECT pio.createDate, aigp.groupCode, aigp.id, aigp.groupOpenDate, ai.id orderid, us.name, ai.activityName, pio.orderNum, io.uuid orderUuid, " +
  "users.name createName, pio.isAsAccount, pio.toBankAccount, pio.toBankNname, pio.bankName, pio.bankAccount, pio.payerName, pio.remarks," +
  "CONCAT(CAST(pio.payPriceType AS CHAR), IF (pio.isAsAccount IS NULL or pio.isAsAccount = 0 or pio.isAsAccount = 2 or pio.isAsAccount = 99, 0, 1)) payPriceType, t2.total_money as payedMoney, pio.updateDate conDate, pio.accountDate "
   +" FROM pay_island_order pio "
   +" LEFT JOIN island_order io ON io.uuid = pio.order_uuid "
   +" LEFT JOIN activity_island ai ON ai.uuid = io.activity_island_uuid "
   +" LEFT JOIN activity_island_group aigp ON aigp.uuid = io.activity_island_group_uuid "
   +" LEFT JOIN sys_user us ON us.id = io.createBy "
   +" LEFT JOIN sys_user users ON users.id = pio.updateBy "
   +" LEFT JOIN (SELECT ma.serialNum, GROUP_CONCAT(CONCAT(c.currency_name,FORMAT(ma.amount,2)) SEPARATOR '+') total_money "
  		 +" 	FROM island_money_amount ma LEFT JOIN currency c ON ma.currencyId = c.currency_id WHERE "
  		 +" 	ma.businessType = 1   GROUP BY ma.serialNum ) t2 ON pio.moneySerialNum = t2.serialNum "
   +" WHERE pio.id="+payid+" and ai.wholesaler_id='"+UserUtils.getUser().getCompany().getId()+"'";
  	
  	  return  productorderDao.findBySql(Sql,Map.class);
  }
   
   /**
	 * 
	* @Title: createDownloadFile
	* @Description: TODO(生成预报单和结算单word文档)
	* @param @param id
	* @param @param orderType
	* @param @param type
	* @param @return
	* @param @throws IOException
	* @param @throws TemplateException
	* @return File    返回类型
	* @throws
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public File createReceiptFile(Long payid, Integer orderType) throws IOException, TemplateException {
		
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
        String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();

		List<Map<Object, Object>> payList = null;
		if (orderType == Context.ORDER_TYPE_JP) {
			payList = getAirPayPrint(payid.toString());
			root.put("moneyType", "机票款");
		} else if (orderType < Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE) {
			payList = getActivityPayPrint(payid.toString(),orderType);
			root.put("moneyType", "团款");
		} else if (orderType == Context.ORDER_TYPE_HOTEL) {
			payList = getHotelPayPrint(payid.toString());
			root.put("moneyType", "团款");
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			payList = getIslandPayPrint(payid.toString());
			root.put("moneyType", "团款");
		}
        boolean isHQX = false;
        if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
            isHQX = true;
        }
		if (payList.size() == 1) {
			if (orderType == Context.ORDER_TYPE_JP) {
				root.put("groupCode", FinanceUtils.blankReturnEmpty(payList.get(0).get("groupCode")));
				String departureCity = DictUtils.getDictLabel(
						String.valueOf(payList.get(0).get("departureCity")),
						"from_area", "");
				String arrivedCity = areaService.get(
						Long.parseLong(payList.get(0).get("arrivedCity")
								.toString())).getName();

				String airTypeValue = FinanceUtils.blankReturnEmpty(payList.get(0).get("airType"));

                String airType = "";
                if(StringUtils.isNotBlank(airTypeValue)){
                    airType = AirTicketUtils.getAirType(Integer.parseInt(airTypeValue));
                }
				String ticket_area_type = "";
				String ticketAreaType = payList.get(0).get("ticket_area_type").toString();
				if(ticketAreaType.equals("1")) ticket_area_type = "（内陆）";
				else if(ticketAreaType.equals("2")) ticket_area_type = "（国际）";
				else if(ticketAreaType.equals("3")) ticket_area_type = "（内陆+国际）";
				//root.put("groupName", departureCity + " &mdash; " + arrivedCity + airType);// “&mdash;” 在word打不开
				String tmp = departureCity + " 一 " + arrivedCity + airType + ticket_area_type;
				root.put("groupName", tmp.replaceAll("&", " "));

			} else if (orderType < Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE) {
				root.put("groupCode", FinanceUtils.blankReturnEmpty(payList.get(0).get("groupCode")));
				root.put("groupName", payList.get(0).get("acitivityName").toString().replaceAll("&", " "));
			} else if (orderType == Context.ORDER_TYPE_HOTEL || orderType == Context.ORDER_TYPE_ISLAND) {
				root.put("groupCode", FinanceUtils.blankReturnEmpty(payList.get(0).get("groupCode")));
				root.put("groupName", payList.get(0).get("activityName").toString().replaceAll("&", " "));
			}

			root.put("pay", payList.get(0));

			String orderid = FinanceUtils.blankReturnEmpty(payList.get(0).get("orderid"));
			String traveler = "";
			if (orderType != Context.ORDER_TYPE_HOTEL && orderType != Context.ORDER_TYPE_ISLAND) {
				List<Map<Object, Object>> list = getTraveler(orderid, orderType);
				for (int i = 0; i < list.size(); i++) {
					if (i == 0)
						traveler = list.get(i).get("name").toString();
					else
						traveler += "；" + list.get(i).get("name");
				}
			} else if (orderType == Context.ORDER_TYPE_HOTEL) {
				List<HotelTraveler> hotelTravelerList=hotelTravelerDao.findTravelerByOrderUuid(payList.get(0).get("orderUuid").toString(), false);
				for(int i = 0;i < hotelTravelerList.size(); i ++){
					if (i==0) traveler =hotelTravelerList.get(i).getName();
					else  traveler +="； "+hotelTravelerList.get(i).getName();
				}
			} else if (orderType == Context.ORDER_TYPE_ISLAND) {
				List<IslandTraveler> islandTravelerList=islandTravelerDao.findTravelerByOrderUuid(payList.get(0).get("orderUuid").toString(), false);
				for(int i = 0;i < islandTravelerList.size(); i ++){
					if (i==0) traveler =islandTravelerList.get(i).getName();
					else  traveler +="； "+islandTravelerList.get(i).getName();
				}
			}
		  StringBuffer str = new StringBuffer();
		   if(StringUtils.isNotBlank(traveler)) {
				String[] array = traveler.split("；");
				for (int i=0;i<array.length;i++) {
					if(i%10!=0 || i==0){
						str.append(" " + array[i]);
					}else{
						str.append(" " + array[i]).append("").append("<w:br />");//去掉了分号 by chy 2016年1月27日10:43:21 bug号 12230
					}
				}
		   }
			root.put("traveler", str.toString());
			String capitalMoney = "";
			String payPriceType = FinanceUtils.blankReturnEmpty(payList.get(0).get("payPriceType"));
			if (payPriceType != null
					&& (payPriceType.equals("11") || payPriceType.equals("21") || payPriceType
							.equals("31"))) {
			String payedMoney = payList.get(0).get("payedMoney").toString();
			 //当前批发商的美元、加元汇率（目前环球行）
			  List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
				//其它币种汇率
				BigDecimal currencyExchangerate = null;
			    //人民币计算
				BigDecimal amountCHN = new BigDecimal("0");
			//增加多币种金额判断20150422
				String [] moneys = payedMoney.split("\\+");
				  if(moneys.length > 0) {
					  for(int i = 0 ; i < moneys.length ; i++) {
						  //韩元-2,000.00
						  Pattern p = Pattern.compile("\\-?\\d+\\.\\d+");
						  String notThundsMoney = moneys[i].replaceAll(",", "");
						  Matcher matcher = p.matcher(notThundsMoney);
						  matcher.find();
						  String money = matcher.group();
						  String currencyName = notThundsMoney.replaceAll(money, "").trim();
						  for (Currency currency : currencylist) {
							  if(currency.getCurrencyName().equals(currencyName)) {
								  currencyExchangerate = currency.getConvertLowest();
								  break;
							  }
						  }
						  if(null == currencyExchangerate){
							  currencyExchangerate = new BigDecimal(1);
						  }
						  amountCHN = amountCHN.add(BigDecimal.valueOf(Double.parseDouble(money)).multiply(currencyExchangerate));
					  }
					  if(amountCHN.doubleValue() < 0) {
						  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
					  }else {
						  capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
					  }
					  root.put("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
					  root.put("capitalMoney", capitalMoney);
				  }
			} else {
				root.put("payedMoney", "");
				root.put("capitalMoney",capitalMoney);
			}
			// 填写日期
			String payCreate = FinanceUtils.blankReturnEmpty(payList.get(0).get("createDate"));
			if (StringUtils.isNotBlank(payCreate)) {
				Date groupDate = DateUtils.dateFormat(payCreate);
				payCreate = DateUtils.formatCustomDate(groupDate, "yyyy年MM月dd日");
			} else {
				payCreate = "年       月       日";
			}
			root.put("payCreate", payCreate);
			// 出发/签证日期
			Date groupOpenDate = (Date) payList.get(0).get("groupOpenDate");
			if (groupOpenDate != null) {
				String groupDate = DateUtils.formatCustomDate(groupOpenDate, "MM月dd日");
				root.put("groupOpenDate", groupDate);
			} else {
				String groupDate = "年       月       日";
				root.put("groupOpenDate", groupDate);
			}
			// 银行到账日期
			String accountDate = "";
			if(isHQX){
//				String priceType =  payList.get(0).get("priceType") == null ? "" : payList.get(0).get("priceType").toString();
//				if(priceType.equals("2")) {
//					accountDate = payList.get(0).get("accountDate") == null ? "" : payList.get(0).get("accountDate").toString();
//				}else {
//					accountDate = payList.get(0).get("payDate") == null ? "" : payList.get(0).get("payDate").toString();
//				}
				accountDate = FinanceUtils.blankReturnEmpty(payList.get(0).get("accountDate"));
				if (StringUtils.isNotBlank(accountDate)) {
					Date groupDate = DateUtils.dateFormat(accountDate);
					accountDate = DateUtils.formatCustomDate(groupDate, "yyyy年M月d日");
					String isAsAccount = FinanceUtils.blankReturnEmpty(payList.get(0).get("isAsAccount"));
					if(!isAsAccount.equals("1")) {
						accountDate = "年       月       日";
					}
				} else {
					accountDate = "年       月       日";
				}
				
			}else {
				accountDate = FinanceUtils.blankReturnEmpty(payList.get(0).get("accountDate"));
				if (StringUtils.isNotBlank(accountDate)) {
					Date groupDate = DateUtils.dateFormat(accountDate);
					accountDate = DateUtils.formatCustomDate(groupDate, "yyyy年M月d日");
				} else {
					accountDate = "年       月       日";
				}
			}
			root.put("accountDate", accountDate);
			// 确认收款日期(20151011 环球行、拉美途客户确认到账时间为空，其它客户当财务撤销确认后，确认收款日期消失)
			Integer revoke = payList.get(0).get("isAsAccount") == null ? -1 :Integer.parseInt(payList.get(0).get("isAsAccount").toString());
			String conDate = FinanceUtils.blankReturnEmpty(payList.get(0).get("conDate"));
			if(isHQX || Context.SUPPLIER_UUID_LAMEITOUR.equals(currentCompanyUuid)) {
				conDate = "年       月       日";
			}else {
				if(revoke == 0 || revoke == -1 || revoke == 2) {
					conDate = "年       月       日";
				}else {
					if (StringUtils.isNotBlank(conDate)) {
						Date groupDate = DateUtils.dateFormat(conDate);
						conDate = DateUtils.formatCustomDate(groupDate, "yyyy年M月d日");
					} else {
						conDate = "年       月       日";
					}
				}
			}
			root.put("conDate", conDate);
			//来款单位
			root.put("payerName", FinanceUtils.blankReturnEmpty(payList.get(0).get("payerName")));
			root.put("bankName", FinanceUtils.blankReturnEmpty(payList.get(0).get("bankName")));
			root.put("bankAccount", FinanceUtils.blankReturnEmpty(payList.get(0).get("bankAccount")));
			//收款账户
			root.put("tobankName", FinanceUtils.blankReturnEmpty(payList.get(0).get("toBankNname")));
			root.put("tobankAccount", FinanceUtils.blankReturnEmpty(payList.get(0).get("toBankAccount")));
			//备注
			root.put("remarks", FinanceUtils.blankReturnEmpty(payList.get(0).get("remarks")));
			//0002 需求 新增开票状态和开票金额 by chy 2016年1月5日19:57:08 start
			if(isHQX){
			    //开票状态
				String invoiceStatus = "已开票";
				List<Orderinvoice> list = orderinvoiceService.findOrderinvoiceByOrderIdOrderType(Integer.parseInt(orderid), orderType); 
				List<Orderinvoice> list2 = orderinvoiceService.findOrderinvoiceByOrderIdOrderType2(Integer.parseInt(orderid), orderType); 
				if(list == null || list.size() == 0){//如果没有已开票的记录 则 肯定是待开票 或空
					if(list2 == null || list2.size() == 0){//如果没有待开票的记录 则为空
						invoiceStatus = "";
					} else {//否则就是待开票状态
						invoiceStatus = "待开票";
					}
				}
				root.put("invoiceStatus", invoiceStatus);
				//开票金额
				String invoiceMoney = OrderCommonUtil.getOrderInvoiceMoney(orderType.toString(), orderid);
				if("0.00".equals(invoiceMoney) && "".equals(invoiceStatus)){
					invoiceMoney = "";
				} else {
					invoiceMoney = "¥ " + invoiceMoney;
				}
				root.put("invoiceMoney", invoiceMoney);
			}
			//0002 需求 新增开票状态和开票金额 by chy 2016年1月5日19:57:08 end
			//收款人
			String isAsAccount = FinanceUtils.blankReturnEmpty(payList.get(0).get("isAsAccount"));
			if(StringUtils.isNotBlank(isAsAccount)) {
				if(Integer.parseInt(isAsAccount) == Context.ORDERPAY_ACCOUNT_STATUS_YDZ) {
					root.put("createName", FinanceUtils.blankReturnEmpty(payList.get(0).get("createName")));
				}else {
					root.put("createName", "");
				}
			}else {
				root.put("createName", "");
			}
		}
		String fileName = "receiptList.ftl";
		if(isHQX){
			fileName = "receiptListHQX.ftl";
		}
		return FreeMarkerUtil.generateFile(fileName, "receiptList.doc", root);
	}
   
	/**
	 * @Description 过滤已转团、已退团游客
	 * @author yakun.bai
	 * @Date 2015-11-20
	 */
	public List<Map<Object, Object>> getTraveler(String orderId, Integer orderType) {
		String Sql = "SELECT name FROM traveler WHERE orderId = " + orderId + " AND order_type = " + orderType + 
					" AND delFlag IN (" + Context.TRAVELER_DELFLAG_NORMAL + "," + Context.TRAVELER_DELFLAG_EXIT + "," + 
					Context.TRAVELER_DELFLAG_TURNROUND + ") and (isAirticketFlag <> '0' or isAirticketFlag is null)";
		return  productorderDao.findBySql(Sql,Map.class);
   }
    
    /**
     *  功能:
     *          通过订单状态查询订单
     *  @author xuziqian
     *  @DateTime 2014-1-18 下午4:01:48
     */
    public Page<Map<Object, Object>> findAgentOrderListByPayType(Integer agentId,Page<Map<Object, Object>> page) {
        page.setOrderBy(" orderTime desc ");
        List<Integer> list = new ArrayList<Integer>();
        String where = this.getAgentOrderSql(agentId);       
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));
        return getOrderList(page, getOrderSql(list, where, ""));
    }
    
    public Page<Map<Object, Object>> findOrderListByPayType(Page<Map<Object, Object>> page, Long activityGroupId) {
        page.setOrderBy(" orderTime desc ");
        List<Integer> list = new ArrayList<Integer>();     
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_CW));
        list.add(Integer.parseInt(Context.ORDER_PAYSTATUS_CW_CX));
        return getOrderList(page, getOrderSql(list, " and agp.id=" + activityGroupId + " ", ""));
    }
    
    public Page<Map<Object, Object>> getByPayStatusAndNoatAccount( Page<Map<Object, Object>> page,  String orderType, Map<String,String> requestMap) {
    	StringBuilder sqlBuffer = new StringBuilder();
    	StringBuilder sb = new StringBuilder();
    	Long companyId = UserUtils.getUser().getCompany().getId();
    	//团期
		String groupCode =  requestMap.get("groupCode");
        //订单编号
        String orderNum = requestMap.get("orderNum");
        //渠道
        String agentId = requestMap.get("agentId");
        //销售
        String saler = requestMap.get("saler");
        //下单人
        String creator = requestMap.get("creator");
        //计调
        String jdString = requestMap.get("jdUserId");
       //下单开始时间
        String orderTimeBegin = requestMap.get("orderTimeBegin");
        //下单结束时间
        String orderTimeEnd = requestMap.get("orderTimeEnd");
        //打印状态ii
        String printFlag = requestMap.get("printFlag");
        //付款日期
        String createDateBegin = requestMap.get("createDateBegin");
        String createDateEnd = requestMap.get("createDateEnd");
        //来款单位
        String payerName = requestMap.get("payerName");
        //收款行
        String toBankNname = requestMap.get("toBankNname");
        //确认收款日期
        String receiptConfirmationDateBegin = requestMap.get("receiptConfirmationDateBegin");
        String receiptConfirmationDateEnd = requestMap.get("receiptConfirmationDateEnd");
        //达账时间
        String accountDateBegin = requestMap.get("accountDateBegin");
        String accountDateEnd = requestMap.get("accountDateEnd");
        //付款金额
        String payAmountStrat = requestMap.get("payAmountStrat");
        String payAmountEnd = requestMap.get("payAmountEnd");
        String payType = requestMap.get("payType");
        //出团日期
        String groupOpenDateStart = requestMap.get("groupOpenDateBegin");
        String groupOpenDateEnd = requestMap.get("groupOpenDateEnd");
        boolean inputMoneyFlag = isInputMoney(payAmountStrat, payAmountEnd);
        //渠道结算方式
        String paymentType = requestMap.get("paymentType");
        //查询条件sql20150616
        StringBuffer queryCon = new StringBuffer();
        //团期
        if(StringUtils.isNotBlank(groupCode)){
        	queryCon.append(" and agp.groupCode like '%").append(groupCode.trim()).append("%' ");
         }
        //订单编号
        if(StringUtils.isNotBlank(orderNum)){
        	queryCon.append(" and pro.orderNum like '%").append(orderNum.trim()).append("%' ");
        }
        //渠道
        if(StringUtils.isNotBlank(agentId)){
        	queryCon.append(" and pro.orderCompany =").append(agentId+" ");
        }
        //销售
        if(StringUtils.isNotBlank(saler)){
        	if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString()) || orderType.equals(Context.ORDER_TYPE_ISLAND.toString()) ){
        	    queryCon.append(" and pro.orderSalerId = ").append(saler+" ");
        	}else{
        		queryCon.append(" and pro.salerId = ").append(saler+" ");
        	}
        }
        //下单人
        if(StringUtils.isNotBlank(creator)){
        	queryCon.append(" and pro.createBy = ").append(creator+" ");
        }
        
        //计调
        if(StringUtils.isNotBlank(jdString)){
        	queryCon.append(" and agp.createBy =").append(jdString+" ");
        }
       //下单开始时间
        if (StringUtils.isNotBlank(orderTimeBegin)) {
        	queryCon.append(" and pro.orderTime >= '").append(orderTimeBegin.trim()).append(" 00:00:00'");
        }
        //下单结束时间
        if (StringUtils.isNotBlank(orderTimeEnd)) {
        	queryCon.append(" and pro.orderTime <= '").append(orderTimeEnd.trim()).append(" 23:59:59'");
        }
        //出团日期开始
        if (StringUtils.isNotBlank(groupOpenDateStart)) {
        	queryCon.append(" and agp.groupOpenDate >= '").append(groupOpenDateStart.trim()).append(" 00:00:00'");
        }
        //出团日期结束
        if (StringUtils.isNotBlank(groupOpenDateEnd)) {
        	queryCon.append(" and agp.groupOpenDate <= '").append(groupOpenDateEnd.trim()).append(" 23:59:59'");
        }
    	//添加海岛游、酒店业务20150615
    	if (null != orderType && Integer.parseInt(orderType) == Context.ORDER_TYPE_HOTEL) {
    		sqlBuffer.append(" SELECT payType,accountDate, createDate, updateDate, payerName, toBankNname, opCreateBy, " +
				"acitivityName, activityId, proCompany,groupOpenDate, groupCloseDate, orderNum, payStatus,orderCompanyName," +
				"groupCode, totalMoney, payedMoney, accountedmoney, id, orderUuid,orderTime, activationDate, placeHolderType,"+
				"agentinfo_id, createUserName, orderCreateDate, orderUpdateDate, orderStatus, payid,payUuid, printTime," +
				"printFlag, ordertype, payPriceType,account,apply_time FROM (SELECT tb.payType,tb.accountDate," +
				"tb.createDate, tb.updateDate, tb.payerName,tb.toBankNname,tmp.createBy AS opCreateBy,tmp.acitivityName," +
				"tmp.activityId, tmp.proCompany, tmp.groupOpenDate,tmp.groupCloseDate, tmp.orderNum, tmp.payStatus," +
				"tmp.orderCompanyName, tmp.groupCode, tmp.total_money AS totalMoney,tb.moneySerialNum  as payedMoney," +
				"tmp.accounted_money AS accountedmoney,tmp.id,tmp.orderUuid,tmp.orderTime,tmp.activationDate," +
				"tmp.placeHolderType,tmp.orderCompany AS agentinfo_id,tmp.createUserName,tmp.createDate orderCreateDate," +
				"tmp.updateDate orderUpdateDate,tmp.orderStatus,tb.id payid,tb.payUuid,tb.printTime,tb.printFlag," +
				Context.ORDER_TYPE_HOTEL + " ordertype,tb.payPriceType,tb.isAsAccount AS account,tmp.apply_time " +
				"FROM ( SELECT pro.accounted_money,agp.createBy,activity.activitySerNum, activity.activityName acitivityName," +
				"activity.uuid AS activityId, activity.wholesaler_id proCompany,activity.createBy AS activityCreateBy," +
				"agp.groupOpenDate, agp.groupEndDate groupCloseDate, pro.orderNum, pro.orderPersonName," +
				"pro.orderPersonPhoneNum, pro.orderPersonNum,pro.orderStatus payStatus, pro.payMode proPayMode," +
				"pro.remainDays proRemainDays, pro.orderCompanyName, agp.groupCode,pro.total_money total_money,pro.payed_money," +
				"pro.id,pro.uuid orderUuid , pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany," +
				"(SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy ) createUserName, pro.createDate," +
				"pro.updateDate, pro.orderStatus,pro.apply_time FROM " +
				"activity_hotel activity, activity_hotel_group agp, hotel_order pro " +
				"WHERE activity.uuid = pro.activity_hotel_uuid AND agp.uuid = pro.activity_hotel_group_uuid " +
				"AND agp.activity_hotel_uuid = pro.activity_hotel_uuid  AND pro.delFlag=").append(ProductOrderCommon.DEL_FLAG_NORMAL)
				.append(" AND pro.orderStatus in (1,2) and activity.wholesaler_id =").append(companyId);
			sqlBuffer.append(queryCon);
			sqlBuffer.append(" ) tmp,")
			.append("(select pho.payType,pho.accountDate,pho.createDate, pho.updateDate, pho.payerName, pho.toBankNname,")
			.append("pho.id,pho.uuid payUuid,pho.orderNum,pho.printTime,pho.printFlag,pho.isAsAccount,CONCAT(cast(pho.payPriceType as char),")
			.append("IF (isAsAccount IS NULL or isAsAccount = 0 or isAsAccount = 2, 0, 1)) payPriceType,")
			.append("moneySerialNum from pay_hotel_order pho");
			if(inputMoneyFlag) sqlBuffer.append(",hotel_money_amount hma  ");
			sqlBuffer.append(" where pho.orderNum is not null");
			 if(StringUtils.isNotBlank(payType)){
					if("3".equals(payType)){
						sqlBuffer.append(" and (pho.payType=3 or pho.payType=5)");
					}else{
						sqlBuffer.append(" and pho.payType =").append(payType);
					}
				}
			if(StringUtils.isNotBlank(payerName)){
				sqlBuffer.append(" and pho.payerName like'%").append(payerName.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(toBankNname)){
				sqlBuffer.append(" and pho.toBankNname like'%").append(toBankNname.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(createDateBegin)){
				sqlBuffer.append(" and pho.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(createDateEnd)){
				sqlBuffer.append(" and pho.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
			}
			if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
				sqlBuffer.append(" and pho.isAsAccount=1");
				if(StringUtils.isNotBlank(accountDateBegin)){
					sqlBuffer.append(" and pho.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
				}
				if(StringUtils.isNotBlank(accountDateEnd)){
					sqlBuffer.append(" and pho.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
				}
			}
			if(inputMoneyFlag){
				sqlBuffer.append(" AND pho.moneySerialNum = hma.serialNum AND hma.serialNum in ")
				.append("(select serialNum from hotel_money_amount " + payAmountWhere(payAmountStrat, payAmountEnd) + ")");
			}
			if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
				sqlBuffer.append(" and printFlag=").append(printFlag.trim()).append(" ");
			}else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
				sqlBuffer.append(" and  printFlag=").append(printFlag.trim());
			}
			if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
				sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
			}else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
				sqlBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
				  .append(" or isAsAccount is null)");
			}else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
				sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
			}
			sqlBuffer.append(" ) tb where tmp.orderNum = tb.orderNum) payment ");
    		return  productorderDao.findBySql(page, sqlBuffer.toString(),Map.class);
    	}else if (null != orderType && Integer.parseInt(orderType) == Context.ORDER_TYPE_ISLAND) {
    		sqlBuffer.append("SELECT payType,accountDate, createDate, updateDate, payerName, toBankNname, opCreateBy," +
				"acitivityName, activityId, proCompany, groupOpenDate,groupCloseDate, orderNum, payStatus, orderCompanyName," +
				"groupCode, totalMoney, payedMoney, accountedmoney, id, orderUuid, orderTime, activationDate," +
				"placeHolderType, agentinfo_id, createUserName, orderCreateDate, orderUpdateDate, orderStatus, payid," +
				"payUuid, printTime, printFlag, ordertype,payPriceType, account,apply_time FROM (SELECT tb.payType," +
				"tb.accountDate,tb.createDate, tb.updateDate, tb.payerName, tb.toBankNname,tmp.createBy AS opCreateBy, " +
				"tmp.acitivityName, tmp.activityId, tmp.proCompany, tmp.groupOpenDate,tmp.groupCloseDate, tmp.orderNum," +
				"tmp.payStatus, tmp.orderCompanyName, tmp.groupCode, tmp.total_money AS totalMoney," +
				"tb.moneySerialNum  as payedMoney, tmp.accounted_money  AS accountedmoney,tmp.id,tmp.orderUuid," +
				"tmp.orderTime,tmp.activationDate,tmp.placeHolderType,tmp.orderCompany AS agentinfo_id,tmp.createUserName," +
				"tmp.createDate orderCreateDate,tmp.updateDate orderUpdateDate,tmp.orderStatus,tb.id payid,tb.payUuid," +
				"tb.printTime,tb.printFlag," + Context.ORDER_TYPE_ISLAND + " ordertype,tb.payPriceType," +
				"tb.isAsAccount AS account,tmp.apply_time FROM(SELECT pro.accounted_money,agp.createBy, " +
				"activity.activitySerNum, activity.activityName acitivityName, activity.uuid AS activityId," +
				"activity.wholesaler_id proCompany, activity.createBy AS activityCreateBy,agp.groupOpenDate," +
				"agp.groupEndDate groupCloseDate, pro.orderNum, pro.orderPersonName, pro.orderPersonPhoneNum," +
				"pro.orderPersonNum,pro.orderStatus payStatus, pro.payMode proPayMode, pro.remainDays proRemainDays," +
				"pro.orderCompanyName, agp.groupCode,pro.total_money, pro.payed_money," +
				"pro.id,pro.uuid orderUuid , pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany," +
				"( SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy ) createUserName, pro.createDate," +
				"pro.updateDate, pro.orderStatus,pro.apply_time FROM activity_island activity, activity_island_group agp," +
				"island_order pro WHERE activity.uuid = pro.activity_island_uuid AND agp.uuid = pro.activity_island_group_uuid " +
				" AND agp.activity_island_uuid = pro.activity_island_uuid  AND pro.delFlag='0'" +
				" AND pro.orderStatus in (" + Context.ISLAND_ORDER_STATUS_DQR+"," + Context.ISLAND_ORDER_STATUS_YQR + ")" +
				" AND activity.wholesaler_id =").append(companyId).append(" ");
			sqlBuffer.append(queryCon);
			sqlBuffer.append(" ) tmp,")
			.append("(select pio.payType,pio.accountDate,pio.createDate, pio.updateDate, pio.payerName, pio.toBankNname,")
			.append("pio.id,pio.uuid payUuid, pio.orderNum,pio.printTime,pio.printFlag,pio.isAsAccount,")
			.append("CONCAT(cast(pio.payPriceType as char),")
			.append("IF (pio.isAsAccount IS NULL or pio.isAsAccount = 0 or pio.isAsAccount = 2, 0, 1)) payPriceType,")
			.append("moneySerialNum from pay_island_order pio");
			if(inputMoneyFlag) sqlBuffer.append(",island_money_amount ima  ");
			sqlBuffer.append(" where pio.orderNum is not null");
			if(StringUtils.isNotBlank(payType)){
				if("3".equals(payType)){
					sqlBuffer.append(" and (pio.payType=3 or pio.payType=5)");
				}else{
					sqlBuffer.append(" and pio.payType =").append(payType);
				}
			}
			if(StringUtils.isNotBlank(payerName)){
				sqlBuffer.append(" and pio.payerName like'%").append(payerName.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(toBankNname)){
				sqlBuffer.append(" and pio.toBankNname like'%").append(toBankNname.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(createDateBegin)){
				sqlBuffer.append(" and pio.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(createDateEnd)){
				sqlBuffer.append(" and pio.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
			}
			if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
				sqlBuffer.append(" and pio.isAsAccount=1");
				if(StringUtils.isNotBlank(accountDateBegin)){
					sqlBuffer.append(" and pio.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
				}
				if(StringUtils.isNotBlank(accountDateEnd)){
					sqlBuffer.append(" and pio.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
				}
			}
			if(inputMoneyFlag){
				sqlBuffer.append(" AND pio.moneySerialNum = ima.serialNum AND ima.serialNum in (select serialNum from island_money_amount ")
					.append(payAmountWhere(payAmountStrat, payAmountEnd)).append(")");
			}
			if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
				sqlBuffer.append(" and printFlag=").append(printFlag.trim()).append(" ");
			}else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
				sqlBuffer.append(" and printFlag=").append(printFlag.trim());
			}
			if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
				sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
			}else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
				sqlBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
				  .append(" or isAsAccount is null)");
			}else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
				sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
			}
			sqlBuffer.append(" ) tb where tmp.orderNum = tb.orderNum) payment ");
    		return  productorderDao.findBySql(page, sqlBuffer.toString(),Map.class);
    	}
    	else if (null != orderType && Integer.parseInt(orderType) == Context.ORDER_TYPE_JP) {
    		/**
    		 * 增加isAsAccount查询字段
    		 * 用于驳回操作判断
    		 */
    		sb.append("select tb.payType, tb.accountDate, tmp.salerId, tmp.saler, tmp.creator, tb.createDate,")
    		  .append(" tb.updateDate, tb.payerName, tb.toBankNname, tmp.opCreateBy, tmp.agentinfo_id, tmp.id, tmp.orderNum,")
    		  .append(" tmp.createDate orderCreateDate, tmp.updateDate orderUpdateDate, tmp.createDate orderTime, tmp.comments,")
    		  .append(" tmp.personNum, tmp.orderType ordertype, tmp.createUserName, tmp.groupCode, tmp.agentName orderCompanyName,")
    		  .append(" tmp.airlines, tmp.airticketId, tmp.airType, tmp.remark, tmp.groupOpenDate, tmp.groupCloseDate, tb.id payid,")
    		  .append(" tb.payPriceType, tb.moneySerialNum AS payedMoney, tb.printTime, tb.printFlag, tb.isAsAccount account,")
    		  .append(" tmp.total_money AS totalMoney, tmp.accounted_money AS accountedmoney, tmp.payStatus from ")
    		  .append(" (select ao.accounted_money, ao.salerId, ao.salerName saler, (SELECT su.NAME FROM sys_user su WHERE ")
    		  .append(" su.id = ao.create_by) creator, aa.createBy opCreateBy, ao.agentinfo_id, ao.id id,")
    		  .append(" ao.order_no orderNum, ao.order_state payStatus,")
    		  .append(" ao.create_date createDate, ao.update_date updateDate, ao.comments comments, ao.person_num personNum,")
    		  .append(" 7 orderType, aa.group_code groupCode, ao.total_money, ai.agentName agentName,")
    		  .append(" su.name createUserName, aa.airlines airlines, aa.id airticketId, cast(aa.airType as signed) airType, aa.remark remark,")
    		  //180  出团日期取出票日期 update by shijun.liu 2016.02.26
    		  .append(" aa.outTicketTime as groupOpenDate, ' ' groupCloseDate from airticket_order ao ")
    		  .append(" LEFT JOIN agentinfo ai ON ao.agentinfo_id = ai.id LEFT JOIN sys_user su ON ao.create_by = su.id ")
    		  .append(" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id LEFT JOIN sys_user aasu ON aa.createBy = aasu.id ")
    		  .append(" WHERE ao.product_type_id = ").append(Context.ORDER_TYPE_JP).append(" and aa.proCompany=").append(companyId);
	    	  //团期
		      if(StringUtils.isNotBlank(groupCode)){
		       	  sb.append(" and aa.group_code like '%").append(groupCode.trim()).append("%' ");
		      }
		      //订单编号
		      if(StringUtils.isNotBlank(orderNum)){
		    	  sb.append(" and ao.order_no like '%").append(orderNum.trim()).append("%' ");
		      }
		      //渠道
		      if(StringUtils.isNotBlank(agentId)){
		    	  sb.append(" and ao.agentinfo_id =").append(agentId.trim());
		      }
		      //销售
		      if(StringUtils.isNotBlank(saler)){
		    	  sb.append(" and ao.salerId = ").append(saler.trim());
		      }
		      //下单人
		      if(StringUtils.isNotBlank(creator)){
		    	  sb.append(" and ao.create_by = ").append(creator.trim());
		      }
		      //计调
		      if(StringUtils.isNotBlank(jdString)){
		    	  sb.append(" and aa.createBy =").append(jdString.trim());
		      }
		      //下单开始时间
		      if (StringUtils.isNotBlank(orderTimeBegin)) {
		    	  sb.append(" and ao.create_date >= '").append(orderTimeBegin.trim()).append(" 00:00:00'");
		      }
		      //下单结束时间
		      if (StringUtils.isNotBlank(orderTimeEnd)) {
		    	  sb.append(" and ao.create_date <= '").append(orderTimeEnd.trim()).append(" 23:59:59'");
		      }
		      //出团日期
		      if(StringUtils.isNotBlank(groupOpenDateStart)){
		    	  sb.append(" and aa.outTicketTime >= '").append(groupOpenDateStart.trim()).append(" 00:00:00'");
		      }
		      if(StringUtils.isNotBlank(groupOpenDateEnd)){
		    	  sb.append(" and aa.outTicketTime <= '").append(groupOpenDateEnd.trim()).append(" 23:59:59'");
		      }
		      sb.append(" ) tmp, (select op.payType, op.accountDate, op.createDate, op.updateDate, op.payerName, ")
    			.append(" op.toBankNname, op.id, op.orderNum, op.ordertype, op.printTime, op.printFlag, op.isAsAccount,")
    			.append(" CONCAT(cast(op.payPriceType as char), IF (op.isAsAccount IS NULL or op.isAsAccount = 0 or op.isAsAccount = 2, 0, 1)) ")
    		    .append(" payPriceType, moneySerialNum from orderpay op");
		      if(inputMoneyFlag){
		    	  sb.append(",money_amount mam ");
		      }
		      sb.append(" where op.orderNum IS NOT NULL and op.orderType = ").append(orderType);
		      if(StringUtils.isNotBlank(payType)){
		    	  if("3".equals(payType)){
		    		  sb.append(" and op.payType in (3,5) ");
		    	  }else{
		    		  sb.append(" and op.payType =").append(payType);
		    	  }
		      }
		      if(StringUtils.isNotBlank(payerName)){
		    	  sb.append(" and op.payerName like'%").append(payerName.trim()).append("%'");
		      }
		      if(StringUtils.isNotBlank(toBankNname)){
		    	  sb.append(" and op.toBankNname like'%").append(toBankNname.trim()).append("%'");
		      }
		      if(StringUtils.isNotBlank(createDateBegin)){
		    	  sb.append(" and op.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
		      }
		      if(StringUtils.isNotBlank(createDateEnd)){
		    	  sb.append(" and op.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
		      }
		      if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
		    	  sb.append(" and op.isAsAccount=1");
		    	  if(StringUtils.isNotBlank(accountDateBegin)){
		    		  sb.append(" and op.accountDate >= '").append(accountDateBegin.trim()).append(" 00:00:00'");
		    	  }
		    	  if(StringUtils.isNotBlank(accountDateEnd)){
		    		  sb.append(" and op.accountDate <= '").append(accountDateEnd.trim()).append(" 23:59:59'");
		    	  }
		      }
		      if(inputMoneyFlag){
		    	  sb.append(" AND op.moneySerialNum = mam.serialNum AND  mam.serialNum in (select serialNum from money_amount ")
		    	    .append(payAmountWhere(payAmountStrat, payAmountEnd)).append(" ) ");
		      }
		      if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
		    	  sb.append(" and printFlag=").append(printFlag).append(" ");
		      }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
		    	  sb.append(" and printFlag=").append(printFlag);
		      }
    		  if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
    			  sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
    		  }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
    			  sb.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
			    	.append(" or isAsAccount is null)");
			  }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
				  sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
			  }
    		  sb.append(" ) tb where tmp.orderNum = tb.orderNum");
    		  return  productorderDao.findBySql(page, sb.toString(),Map.class);
    	}else if(null != orderType && Integer.parseInt(orderType) == Context.ORDER_TYPE_ALL){//Context.ORDER_TYPE_ALL
    		sb.append("select payType, accountDate, salerId, saler, createDate, updateDate, payerName, ")
    		  .append(" toBankNname,receiptConfirmationDate, opCreateBy, acitivityName, activityId, proCompany, groupOpenDate, ")
    		  .append(" groupCloseDate, orderNum, payStatus, orderCompanyName, groupCode, totalMoney, ")
    		  .append(" payedMoney, accountedmoney, id, orderUuid, orderTime, activationDate,")
    		  .append(" placeHolderType, agentinfo_id, creator, orderCreateDate, orderUpdateDate,")
    		  .append(" orderStatus, payid, payUuid, printTime, printFlag, ordertype, payPriceType, account,apply_time from ")
    		  .append(" (SELECT tb.payType, tb.accountDate,tmp.salerId, tmp.salerName saler,tb.createDate,")
    		  .append(" tb.updateDate, tb.payerName, tb.toBankNname,tb.receiptConfirmationDate, tmp.createBy AS opCreateBy,")
    		  .append(" ' ' AS acitivityName, tmp.airticketId AS activityId, tmp.proCompany, tmp.groupOpenDate, ")
    		  .append(" tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.agentName orderCompanyName, tmp.groupCode, ")
    		  .append(" tmp.total_money totalMoney, tb.moneySerialNum payedMoney,tmp.accounted_money AS accountedmoney,")
    		  .append(" tmp.id,tmp.orderUuid, tmp.createDate orderTime, tmp.activationDate, tmp.placeHolderType, ")
    		  .append(" tmp.agentinfo_id, tmp.creator, tmp.createDate orderCreateDate, tmp.updateDate orderUpdateDate,")
    		  .append(" tmp.orderType orderStatus, tb.id payid,tb.payUuid, tb.printTime, tb.printFlag, tmp.orderType ordertype,")
    		  .append(" tb.payPriceType, tb.isAsAccount AS account,tmp.apply_time FROM (SELECT ao.salerId, ao.salerName, ")
    		  .append(" aa.createBy, ao.agentinfo_id, ao.id id,'abc' orderUuid, ao.order_no orderNum, ao.order_state payStatus,")
    		  .append(" ao.create_date createDate, ao.update_date updateDate, ao.comments comments, ao.person_num personNum, 7 orderType,")
    		  .append(" aa.group_code groupCode, ao.total_money, ao.payed_money, ao.accounted_money, ao.place_holder_type placeHolderType,")
    		  .append(" ao.activationDate, ai.agentName agentName, su. NAME creator, aa.airlines airlines, aa.id airticketId,")
    		  .append(" cast(aa.airType AS signed) airType, aa.remark remark, aa.proCompany, aa.outTicketTime as groupOpenDate,")
    		  .append(" null groupCloseDate, ' ' as apply_time FROM airticket_order ao LEFT JOIN agentinfo ai ON ")
    		  .append(" ao.agentinfo_id = ai.id LEFT JOIN sys_user su ON ao.create_by = su.id ")
    		  .append(" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id LEFT JOIN sys_user aasu ")
    		  .append(" ON aa.createBy = aasu.id WHERE  ao.product_type_id =").append(Context.ORDER_TYPE_JP)
    		  .append(" and aa.proCompany = ").append(companyId);
    		//团期
    		if(StringUtils.isNotBlank(groupCode)){
    			sb.append(" and aa.group_code like '%").append(groupCode.trim()).append("%' ");
    		}
    		//订单编号
    		if(StringUtils.isNotBlank(orderNum)){
    			sb.append(" and ao.order_no like '%").append(orderNum.trim()).append("%' ");
    		}
    		//渠道
    		if(StringUtils.isNotBlank(agentId)){
    			sb.append(" and ao.agentinfo_id =").append(agentId.trim()+" ");
    		}
    		//销售
    		if(StringUtils.isNotBlank(saler)){
    			sb.append(" and ao.salerId = ").append(saler.trim()+" ");
    		}
    		//下单人
    		if(StringUtils.isNotBlank(creator)){
    		  	sb.append(" and ao.create_by = ").append(creator.trim()+" ");
    		}
    		//计调
    		if(StringUtils.isNotBlank(jdString)){
    			sb.append(" and aa.createBy =").append(jdString.trim()+" ");
    		}
    		//下单开始时间
    		if (StringUtils.isNotBlank(orderTimeBegin)) {
    			sb.append(" and ao.create_date >= '").append(orderTimeBegin.trim()).append(" 00:00:00'");
    		}
    		//下单结束时间
    		if (StringUtils.isNotBlank(orderTimeEnd)) {
    			sb.append(" and ao.create_date <= '").append(orderTimeEnd.trim()).append(" 23:59:59'");
    		}
    		//出团日期
		    if(StringUtils.isNotBlank(groupOpenDateStart)){
		    	sb.append(" and aa.outTicketTime >= '").append(groupOpenDateStart.trim()).append(" 00:00:00'");
		    }
		    if(StringUtils.isNotBlank(groupOpenDateEnd)){
		    	sb.append(" and aa.outTicketTime <= '").append(groupOpenDateEnd.trim()).append(" 23:59:59'");
		    }
    		sb.append(" ) tmp,(SELECT op.payType,op.accountDate,op.createDate, op.updateDate, op.payerName, op.toBankNname,op.receiptConfirmationDate,op.id, ")
    		  .append(" 'abc' payUuid, orderNum, op.ordertype, printTime, printFlag, isAsAccount, CONCAT( cast(payPriceType AS CHAR),")
    		  .append(" IF ( isAsAccount IS NULL OR isAsAccount = 0 OR isAsAccount = 2, 0, 1 )) payPriceType,moneySerialNum FROM orderpay op");
    		if(inputMoneyFlag) {
    			sb.append(", money_amount mam");
    		}
    		sb.append(" WHERE op.orderNum IS NOT NULL ");
			if(StringUtils.isNotBlank(payType)){
            	if("3".equals(payType)){
            		sb.append(" and op.payType in (3,5) ");
            	}else{
            		sb.append(" and op.payType =").append(payType);
            	}
            }
			if(StringUtils.isNotBlank(payerName)){
				sb.append(" and op.payerName like'%").append(payerName.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(toBankNname)){
				sb.append(" and op.toBankNname like'%").append(toBankNname.trim()).append("%'");
			}
			//确认收款时间
			if(StringUtils.isNotBlank(receiptConfirmationDateBegin)){
				sb.append(" and op.receiptConfirmationDate >='").append(receiptConfirmationDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(receiptConfirmationDateEnd)){
				sb.append(" and op.receiptConfirmationDate <='").append(receiptConfirmationDateEnd.trim()).append(" 23:59:59' ");
			}
			if(StringUtils.isNotBlank(createDateBegin)){
				sb.append(" and op.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(createDateEnd)){
				sb.append(" and op.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
			}
			if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
	        	sb.append(" and op.isAsAccount=1");
	        	if(StringUtils.isNotBlank(accountDateBegin)){
	        		sb.append(" and op.accountDate >= '").append(accountDateBegin.trim()).append(" 00:00:00'");
	        	}
	        	if(StringUtils.isNotBlank(accountDateEnd)){
	        		sb.append(" and op.accountDate <= '").append(accountDateEnd.trim()).append(" 23:59:59'");
	        	}
	        } 
			sb.append(" AND op.orderType =").append(Context.ORDER_TYPE_JP);
			if(inputMoneyFlag) {
				sb.append(" AND op.moneySerialNum = mam.serialNum AND  mam.serialNum in (select serialNum from money_amount ")
				  .append(payAmountWhere(payAmountStrat, payAmountEnd)).append(" ) ");
			}
			if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
	        	sb.append(" and printFlag=").append(printFlag).append(" ");
	        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
	        	sb.append(" and  printFlag=").append(printFlag);
	        }
    		 if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
 		    	sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
 		    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    	sb.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
		    	  .append(" or isAsAccount is null)");
		    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    	sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
		    }
    		sb.append(" ) tb WHERE tmp.orderNum = tb.orderNum ")
    		  .append(" UNION ")
		      .append(" SELECT tb.payType, tb.accountDate, tmp.salerId, tmp.salerName saler, tb.createDate, tb.updateDate, ")
		      .append("  tb.payerName, tb.toBankNname,tb.receiptConfirmationDate, tmp.createBy AS opCreateBy, tmp.acitivityName, tmp.activityId, ")
		      .append(" tmp.proCompany, tmp.groupOpenDate, tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.orderCompanyName, ")
		      .append(" tmp.groupCode, tmp.total_money AS totalMoney, tb.moneySerialNum as payedMoney,")
		      .append(" tmp.accounted_money AS accountedmoney, tmp.id,tmp.orderUuid,tmp.orderTime,tmp.activationDate,")
		      .append(" tmp.placeHolderType, tmp.orderCompany AS agentinfo_id, tmp.creator,")
		      .append(" tmp.createDate orderCreateDate, tmp.updateDate orderUpdateDate, tmp.orderStatus,")
		      .append(" tb.id payid,tb.payUuid,tb.printTime,tb.printFlag,tb.ordertype,tb.payPriceType,")
		      .append(" tb.isAsAccount AS account,tmp.apply_time FROM ( SELECT pro.salerId, pro.salerName,agp.createBy, ")
		      .append(" activity.activitySerNum, activity.acitivityName, activity.id AS activityId, activity.proCompany,")
		      .append(" activity.createBy AS activityCreateBy, agp.groupOpenDate, agp.groupCloseDate, pro.orderNum, ")
		      .append(" pro.orderPersonName, pro.orderPersonPhoneNum, pro.orderPersonNum, pro.payStatus,")
		      .append(" pro.payMode proPayMode, pro.remainDays proRemainDays, pro.orderCompanyName, agp.groupCode,")
		      .append(" pro.total_money total_money, pro.payed_money,pro.accounted_money, pro.id, 'abc' orderUuid, ")
		      .append(" pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany, ")
		      .append(" (SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy ) creator, pro.createDate, ")
		      .append(" pro.updateDate, pro.orderStatus,' ' as apply_time FROM travelactivity activity,")
		      .append(" activitygroup agp, productorder pro WHERE activity.id = pro.productId ")
		      .append(" AND agp.id = pro.productGroupId AND agp.srcActivityId = pro.productId  AND pro.delFlag= ")
    		  .append(ProductOrderCommon.DEL_FLAG_NORMAL).append(" and activity.proCompany=").append(companyId)
    		  .append(queryCon).append(" ) tmp,").append("(select op.payType,op.accountDate,op.createDate, op.updateDate, ")
    		  .append(" op.payerName, op.toBankNname,op.receiptConfirmationDate, op.id, op.orderid, 'abc' payUuid, ")
    		  .append(" orderNum, printTime, printFlag, op.ordertype, isAsAccount,")
    		  .append(" CONCAT(cast(payPriceType as char),")
    		  .append(" IF (isAsAccount IS NULL or isAsAccount = 0 or isAsAccount = 2, 0, 1)) payPriceType,")
    		  .append(" moneySerialNum from orderpay op ");
		   if(inputMoneyFlag){
			   sb.append(", money_amount mam");
		   }
		   sb.append(" where op.orderNum is not null AND op.orderType in (1,2,3,4,5,10) ");
		   if(StringUtils.isNotBlank(payType)){
	        	if("3".equals(payType)){
	        		sb.append(" and op.payType in (3,5) ");
	        	}else{
	        		sb.append(" and op.payType =").append(payType);
	        	}
	        }
			if(StringUtils.isNotBlank(payerName)){
				sb.append(" and op.payerName like'%").append(payerName.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(toBankNname)){
				sb.append(" and op.toBankNname like'%").append(toBankNname.trim()).append("%'");
			}
			//确认收款时间
			if(StringUtils.isNotBlank(receiptConfirmationDateBegin)){
				sb.append(" and op.receiptConfirmationDate >='").append(receiptConfirmationDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(receiptConfirmationDateEnd)){
				sb.append(" and op.receiptConfirmationDate <='").append(receiptConfirmationDateEnd.trim()).append(" 23:59:59' ");
			}
			if(StringUtils.isNotBlank(createDateBegin)){
				sb.append(" and op.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(createDateEnd)){
				sb.append(" and op.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
			}
			if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
	        	sb.append(" and op.isAsAccount=1");
	        	if(StringUtils.isNotBlank(accountDateBegin)){
	        		sb.append(" and op.accountDate >= '").append(accountDateBegin.trim()).append(" 00:00:00'");
	        	}
	        	if(StringUtils.isNotBlank(accountDateEnd)){
	        		sb.append(" and op.accountDate <= '").append(accountDateEnd.trim()).append(" 23:59:59'");
	        	}
	        } 
			if(inputMoneyFlag) {
				sb.append(" AND op.moneySerialNum = mam.serialNum AND  mam.serialNum in (select serialNum from money_amount ")
				  .append(payAmountWhere(payAmountStrat, payAmountEnd)).append(" ) ");
			}
			if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
	        	sb.append(" and printFlag=").append(printFlag).append(" ");
	        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
	        	sb.append(" and  printFlag=").append(printFlag);
	        }
    		if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
    			sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
		    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    	sb.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
		    	  .append(" or isAsAccount is null)");
		    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    	sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
		    }
		    sb.append(" ) tb where tmp.orderNum = tb.orderNum and tmp.id = tb.orderid ");
		    //添加海岛游、酒店业务20150616
		    StringBuffer hotelStr = new StringBuffer();
		    StringBuffer islandStr = new StringBuffer();
		    Integer ordertype = Context.ORDER_TYPE_HOTEL;
		    hotelStr.append(" UNION ")
		    		.append("SELECT tb.payType,tb.accountDate,tmp.salerId, tmp.saler,tb.createDate, tb.updateDate, ")
		    		.append(" tb.payerName, tb.toBankNname,tb.receiptConfirmationDate,tmp.createBy AS opCreateBy, tmp.acitivityName, tmp.activityId, tmp.proCompany, ")
		    		.append(" tmp.groupOpenDate, tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.orderCompanyName,")
		    		.append(" tmp.groupCode, tmp.total_money AS totalMoney, tb.moneySerialNum AS payedMoney, tmp.accounted_money ")
		    		.append(" AS accountedmoney, tmp.id,tmp.orderUuid, tmp.orderTime, tmp.activationDate, tmp.placeHolderType,")
		    		.append(" tmp.orderCompany AS agentinfo_id, tmp.creator, tmp.createDate orderCreateDate,")
		    		.append(" tmp.updateDate orderUpdateDate, tmp.orderStatus, tb.id payid,tb.payUuid,")
		    		.append(" tb.printTime, tb.printFlag, ").append(ordertype).append(" ordertype, ")
		    		.append(" tb.payPriceType,tb.isAsAccount AS account,tmp.apply_time FROM ")
		    		.append(" (SELECT pro.orderSalerId salerId, NULL saler,agp.createBy, activity.activitySerNum, ")
		    		.append("  activity.activityName acitivityName, activity.uuid AS activityId, activity.wholesaler_id proCompany, ")
		    		.append(" activity.createBy AS activityCreateBy,agp.groupOpenDate, agp.groupEndDate groupCloseDate,	")
		    		.append(" pro.orderNum,pro.orderPersonName,pro.orderPersonPhoneNum,pro.orderPersonNum, ")
		    		.append(" pro.orderStatus payStatus,pro.payMode proPayMode,pro.remainDays proRemainDays,pro.orderCompanyName,")
		    		.append(" agp.groupCode,  pro.total_money, pro.payed_money, pro.accounted_money, pro.id, pro.uuid orderUuid,")
		    		.append(" pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany,")
		    		.append(" (SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy) creator,")
		    		.append(" pro.createDate, pro.updateDate, pro.orderStatus, pro.apply_time FROM activity_hotel activity,")
		    		.append(" activity_hotel_group agp, hotel_order pro WHERE activity.uuid = pro.activity_hotel_uuid ")
		    		.append(" AND agp.uuid = pro.activity_hotel_group_uuid AND agp.activity_hotel_uuid = pro.activity_hotel_uuid ")
		    		.append(" AND pro.delFlag = 0 AND pro.orderStatus IN (1, 2) AND activity.wholesaler_id = ").append(companyId)
		    		.append(queryCon).append(" ) tmp,")
		    		.append(" (SELECT pho.payType,pho.accountDate,pho.createDate, pho.updateDate, pho.payerName, ")
		    		.append(" pho.toBankNname,pho.receiptConfirmationDate,pho.id,pho.uuid payUuid, orderNum, printTime,")
		    		.append(" printFlag, isAsAccount,CONCAT( cast(payPriceType AS CHAR),")
		    		.append(" IF (isAsAccount IS NULL OR isAsAccount = 0 OR isAsAccount = 2, 0,1)) payPriceType,")
		    		.append(" moneySerialNum FROM pay_hotel_order pho ");
		    if(inputMoneyFlag) {
		    	hotelStr.append(", hotel_money_amount hma");
		    }
		    hotelStr.append(" WHERE pho.orderNum IS NOT NULL ");
		    if(StringUtils.isNotBlank(payType)){
			    if("3".equals(payType)){
			    	hotelStr.append(" and pho.payType in (3,5)");
			    }else{
			    	hotelStr.append(" and pho.payType =").append(payType);
			   	}
		 	}
		    if(StringUtils.isNotBlank(payerName)){
		    	hotelStr.append(" and pho.payerName like'%").append(payerName.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(toBankNname)){
				hotelStr.append(" and pho.toBankNname like'%").append(toBankNname.trim()).append("%'");
			}
			//确认收款时间
			if(StringUtils.isNotBlank(receiptConfirmationDateBegin)){
				hotelStr.append(" and pho.receiptConfirmationDate >='").append(receiptConfirmationDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(receiptConfirmationDateEnd)){
				hotelStr.append(" and pho.receiptConfirmationDate <='").append(receiptConfirmationDateEnd.trim()).append(" 23:59:59' ");
			}
			if(StringUtils.isNotBlank(createDateBegin)){
				hotelStr.append(" and pho.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(createDateEnd)){
				hotelStr.append(" and pho.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
			}
		    if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
		    	hotelStr.append(" and pho.isAsAccount=1");
	        	if(StringUtils.isNotBlank(accountDateBegin)){
	        		hotelStr.append(" and pho.accountDate >= '").append(accountDateBegin.trim()).append(" 00:00:00'");
	        	}
	        	if(StringUtils.isNotBlank(accountDateEnd)){
	        		hotelStr.append(" and pho.accountDate <= '").append(accountDateEnd.trim()).append(" 23:59:59' ");
	        	}
	        } 
		    if(inputMoneyFlag) {
		    	hotelStr.append(" AND pho.moneySerialNum = hma.serialNum AND hma.serialNum in (select serialNum from hotel_money_amount ")
		    	        .append(payAmountWhere(payAmountStrat, payAmountEnd)).append(" ) ");
		    }
		    if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
		    	hotelStr.append(" and printFlag=").append(printFlag).append(" ");
	        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
	        	hotelStr.append(" and printFlag=").append(printFlag);
	        }
		    if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    	hotelStr.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
		    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    	hotelStr.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
		    	        .append(" or isAsAccount is null)");
		    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    	hotelStr.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
		    }
		    hotelStr.append(") tb WHERE tmp.orderNum = tb.orderNum");
		    //海岛游sql替换酒店hotel字符20150616
		    islandStr.append(hotelStr.toString()
		    		.replaceAll(Context.ORDER_TYPE_HOTEL + " ordertype,", Context.ORDER_TYPE_ISLAND + " ordertype,")
		    		.replaceAll("hotel", "island").replace("pho", "pio").replaceAll("hma", "ima"));
		    sb.append(hotelStr).append(islandStr).append(") refundList ");
		    if(StringUtils.isNotBlank(paymentType)){
		    	sb.append(" where agentinfo_id in ( select id from agentinfo where paymentType = " + paymentType +" ) ");
		    }
		    return  productorderDao.findBySql(page, sb.toString(),Map.class);
    		
    	}else {
    		sqlBuffer.append("select payType, accountDate, salerId, saler, createDate, updateDate, payerName, toBankNname,")
    				 .append("opCreateBy, acitivityName, activityId, proCompany, groupOpenDate, groupCloseDate, orderNum, ")
    				 .append(" payStatus, orderCompanyName, groupCode, totalMoney, payedMoney, accountedmoney, id, orderTime,")
    				 .append(" activationDate, placeHolderType, agentinfo_id, creator, orderCreateDate, ")
    				 .append(" orderUpdateDate, orderStatus, payid, printTime, printFlag, ordertype, payPriceType, account from ")
    				 .append(" (SELECT tb.payType, tb.accountDate, tmp.salerId, tmp.salerName saler, tb.createDate, tb.updateDate,")
    				 .append(" tb.payerName, tb.toBankNname, tmp.createBy AS opCreateBy, tmp.acitivityName, tmp.activityId,")
    				 .append(" tmp.proCompany, tmp.groupOpenDate, tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.orderCompanyName, ")
    				 .append(" tmp.groupCode, tmp.total_money AS totalMoney, tb.moneySerialNum as payedMoney,")
    				 .append(" tmp.accounted_money AS accountedmoney, tmp.id, tmp.orderTime, tmp.activationDate, tmp.placeHolderType,")
    				 .append(" tmp.orderCompany AS agentinfo_id, tmp.creator, tmp.createDate orderCreateDate,")
    				 .append(" tmp.updateDate orderUpdateDate, tmp.orderStatus, tb.id payid, tb.printTime, tb.printFlag, tb.ordertype,")
    				 .append(" tb.payPriceType, tb.isAsAccount AS account FROM ")
    				 .append(" (SELECT pro.accounted_money, pro.salerId, pro.salerName, agp.createBy, ")
    				 .append(" activity.activitySerNum, activity.acitivityName, activity.id AS activityId, activity.proCompany,")
    				 .append(" activity.createBy AS activityCreateBy, agp.groupOpenDate, agp.groupCloseDate, pro.orderNum, pro.orderPersonName, ")
    				 .append(" pro.orderPersonPhoneNum, pro.orderPersonNum, pro.payStatus, pro.payMode proPayMode, ")
    				 .append(" pro.remainDays proRemainDays, pro.orderCompanyName, agp.groupCode, pro.total_money total_money, ")
    				 .append(" pro.payed_money, pro.id, pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany,")
    				 .append(" (SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy) creator, pro.createDate,")
    				 .append(" pro.updateDate, pro.orderStatus FROM travelactivity activity, activitygroup agp, productorder pro ")
    				 .append(" WHERE activity.id = pro.productId AND agp.id = pro.productGroupId AND ")
    				 .append(" agp.srcActivityId = pro.productId  AND pro.delFlag=").append(ProductOrderCommon.DEL_FLAG_NORMAL)
					 .append(" AND pro.orderStatus = ").append(orderType).append(" and activity.proCompany=").append(companyId)  
			         .append(queryCon).append(" ) tmp,")
			         .append("(select op.payType, op.accountDate, op.createDate, op.updateDate, op.payerName, op.toBankNname,")
			         .append(" op.id, op.orderid, op.orderNum, op.printTime, op.printFlag, op.ordertype, op.isAsAccount, CONCAT(cast(op.payPriceType as char),")
			         .append(" IF (op.isAsAccount IS NULL or op.isAsAccount = 0 or op.isAsAccount = 2 OR isAsAccount = 99, 0, 1)) payPriceType,")
			         .append(" moneySerialNum from orderpay op");
    			if(inputMoneyFlag){
    				sqlBuffer.append(", money_amount mam ");
    			}
    			sqlBuffer.append(" where op.orderNum is not null and op.orderType =").append(orderType);
                if(StringUtils.isNotBlank(payType)){
                	if("3".equals(payType)){
                		sqlBuffer.append(" and op.payType in (3,5)");
                	}else{
                		sqlBuffer.append(" and op.payType =").append(payType);
                	}
                }
    			if(StringUtils.isNotBlank(payerName)){
    				sqlBuffer.append(" and op.payerName like'%").append(payerName.trim()).append("%'");
    			}
    			if(StringUtils.isNotBlank(toBankNname)){
    				sqlBuffer.append(" and op.toBankNname like'%").append(toBankNname.trim()).append("%'");
    			}
    			if(StringUtils.isNotBlank(createDateBegin)){
    				sqlBuffer.append(" and op.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
    			}
    			if(StringUtils.isNotBlank(createDateEnd)){
    				sqlBuffer.append(" and op.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
    			}
    			if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
    				sqlBuffer.append(" and op.isAsAccount=1");
    	        	if(StringUtils.isNotBlank(accountDateBegin)){
    	        		sqlBuffer.append(" and op.accountDate >= '").append(accountDateBegin.trim()).append(" 00:00:00'");
    	        	}
    	        	if(StringUtils.isNotBlank(accountDateEnd)){
    	        		sqlBuffer.append(" and op.accountDate <= '").append(accountDateEnd.trim()).append(" 23:59:59'");
    	        	}
    	        } 
    			if(inputMoneyFlag) {
    				sqlBuffer.append(" AND op.moneySerialNum = mam.serialNum AND  mam.serialNum in (select serialNum from money_amount ")
    				         .append(payAmountWhere(payAmountStrat, payAmountEnd)).append(" )");
    			}
    		    if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
		        	sqlBuffer.append(" and printFlag=").append(printFlag).append(" ");
		        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
		        	sqlBuffer.append(" and  printFlag=").append(printFlag);
		        }
    			
	    		if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
	    			sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
			    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
			    	sqlBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
			    	  .append(" or isAsAccount is null)");
			    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
			    	sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
			    }
    		    sqlBuffer.append(" ) tb where tmp.orderNum = tb.orderNum and tmp.id = tb.orderid) payment ");
    		    
    			return  productorderDao.findBySql(page, sqlBuffer.toString(),Map.class);
    	}
    }
    
    
    
    public List<Map<Object, Object>> getByPayStatusAndNoatAccount(String orderBy,  String orderType, Map<String,String> requestMap) {
    	StringBuilder sqlBuffer = new StringBuilder();
    	StringBuilder sb = new StringBuilder();
    	Long companyId = UserUtils.getUser().getCompany().getId();
    	//团期
		String groupCode =  requestMap.get("groupCode");
        //订单编号
        String orderNum = requestMap.get("orderNum");
        //渠道
        String agentId = requestMap.get("agentId");
        //销售
        String saler = requestMap.get("saler");
        //下单人
        String creator = requestMap.get("creator");
        //计调
        String jdString = requestMap.get("jdUserId");
       //下单开始时间
        String orderTimeBegin = requestMap.get("orderTimeBegin");
        //下单结束时间
        String orderTimeEnd = requestMap.get("orderTimeEnd");
        //打印状态ii
        String printFlag = requestMap.get("printFlag");
        //付款日期
        String createDateBegin = requestMap.get("createDateBegin");
        String createDateEnd = requestMap.get("createDateEnd");
        //来款单位
        String payerName = requestMap.get("payerName");
        //收款行
        String toBankNname = requestMap.get("toBankNname");
        //确认收款日期
        String receiptConfirmationDateBegin = requestMap.get("receiptConfirmationDateBegin");
        String receiptConfirmationDateEnd = requestMap.get("receiptConfirmationDateEnd");
        //达账时间
        String accountDateBegin = requestMap.get("accountDateBegin");
        String accountDateEnd = requestMap.get("accountDateEnd");
        //付款金额
        String payAmountStrat = requestMap.get("payAmountStrat");
        String payAmountEnd = requestMap.get("payAmountEnd");
        String payType = requestMap.get("payType");
        //出团日期
        String groupOpenDateStart = requestMap.get("groupOpenDateBegin");
        String groupOpenDateEnd = requestMap.get("groupOpenDateEnd");
        boolean inputMoneyFlag = isInputMoney(payAmountStrat, payAmountEnd);
        //查询条件sql20150616
        StringBuffer queryCon = new StringBuffer();
        //团期
        if(StringUtils.isNotBlank(groupCode)){
        	queryCon.append(" and agp.groupCode like '%").append(groupCode.trim()).append("%' ");
         }
        //订单编号
        if(StringUtils.isNotBlank(orderNum)){
        	queryCon.append("and pro.orderNum like '%").append(orderNum.trim()).append("%' ");
        }
        //渠道
        if(StringUtils.isNotBlank(agentId)){
        	queryCon.append("and pro.orderCompany =").append(agentId+" ");
        }
        //销售
        if(StringUtils.isNotBlank(saler)){
        	if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString()) || orderType.equals(Context.ORDER_TYPE_ISLAND.toString()) ){
        	    queryCon.append("and pro.orderSalerId = ").append(saler+" ");
        	}else{
        		queryCon.append("and pro.salerId = ").append(saler+" ");
        	}
        }
        //下单人
        if(StringUtils.isNotBlank(creator)){
        	queryCon.append("and pro.createBy = ").append(creator+" ");
        }
        
        //计调
        if(StringUtils.isNotBlank(jdString)){
        	queryCon.append("and agp.createBy =").append(jdString+" ");
        }
       //下单开始时间
        if (StringUtils.isNotBlank(orderTimeBegin)) {
        	queryCon.append( "and pro.orderTime >= '" + orderTimeBegin.trim() + " 00:00:00" + "' ");
        }
        //下单结束时间
        if (StringUtils.isNotBlank(orderTimeEnd)) {
        	queryCon.append( "and pro.orderTime <= '" + orderTimeEnd.trim() + " 23:59:59" + "' ");
        }
        //出团日期开始
        if (StringUtils.isNotBlank(groupOpenDateStart)) {
            queryCon.append(" and agp.groupOpenDate >= '").append(groupOpenDateStart.trim()).append(" 00:00:00'");
        }
        //出团日期结束
        if (StringUtils.isNotBlank(groupOpenDateEnd)) {
            queryCon.append(" and agp.groupOpenDate <= '").append(groupOpenDateEnd.trim()).append(" 23:59:59'");
        }
    	//添加海岛游、酒店业务20150615
    	if (null != orderType && Integer.parseInt(orderType) == Context.ORDER_TYPE_HOTEL) {
    		sqlBuffer.append(" select payType,accountDate, createDate, updateDate, payerName, toBankNname, opCreateBy, acitivityName, activityId, proCompany, " +
    				"groupOpenDate, groupCloseDate, orderNum, payStatus, orderCompanyName, groupCode, totalMoney, payedMoney, accountedmoney, id, orderUuid, " +
    				"orderTime, activationDate, placeHolderType, agentinfo_id, createUserName, orderCreateDate, orderUpdateDate, orderStatus, payid, " +
    				"payUuid, printTime, printFlag, ordertype, payPriceType,account from (SELECT tb.payType,tb.accountDate,tb.createDate, tb.updateDate, tb.payerName, " +
    				"tb.toBankNname,tmp.createBy AS opCreateBy, tmp.acitivityName, tmp.activityId, tmp.proCompany, tmp.groupOpenDate, " +
    				"tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.orderCompanyName, tmp.groupCode, tmp.total_money AS totalMoney, " +
    				"tb.moneySerialNum  as payedMoney,tmp.accounted_money AS accountedmoney,tmp.id," +
    						"tmp.orderUuid,tmp.orderTime,tmp.activationDate,tmp.placeHolderType," +
					"tmp.orderCompany AS agentinfo_id,tmp.createUserName,tmp.createDate orderCreateDate,tmp.updateDate orderUpdateDate,tmp.orderStatus," +
					"tb.id payid,tb.payUuid,tb.printTime,tb.printFlag," + Context.ORDER_TYPE_HOTEL + " ordertype,tb.payPriceType,tb.isAsAccount AS account FROM ( SELECT pro.accounted_money,agp.createBy, " +
					"activity.activitySerNum, activity.activityName acitivityName, activity.uuid AS activityId, activity.wholesaler_id proCompany, activity.createBy AS activityCreateBy, " +
					"agp.groupOpenDate, agp.groupEndDate groupCloseDate, pro.orderNum, pro.orderPersonName, pro.orderPersonPhoneNum, pro.orderPersonNum," +
					" pro.orderStatus payStatus, pro.payMode proPayMode, pro.remainDays proRemainDays, pro.orderCompanyName, agp.groupCode, " +
					"pro.total_money  total_money, pro.payed_money, " +
					"pro.id,pro.uuid orderUuid , pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany, " +
					"( SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy ) createUserName, pro.createDate, " +
					"pro.updateDate, pro.orderStatus FROM activity_hotel activity, activity_hotel_group agp, hotel_order pro " +
					"WHERE activity.uuid = pro.activity_hotel_uuid AND agp.uuid = pro.activity_hotel_group_uuid AND agp.activity_hotel_uuid = pro.activity_hotel_uuid  AND pro.delFlag=").append(ProductOrderCommon.DEL_FLAG_NORMAL)
	    			.append(" AND pro.orderStatus in (1,2) and activity.wholesaler_id =" + companyId ).append(" ");
    				sqlBuffer.append(queryCon);
			        sqlBuffer.append(" ) tmp,")

	    			.append("(select pho.payType,pho.accountDate,pho.createDate, pho.updateDate, pho.payerName, pho.toBankNname,pho.id,pho.uuid payUuid,pho.orderNum,pho.printTime,pho.printFlag,pho.isAsAccount,CONCAT(cast(pho.payPriceType as char),")
	    			.append("IF (isAsAccount IS NULL or isAsAccount = 0 or isAsAccount = 2, 0, 1)) payPriceType,")

	    			.append("moneySerialNum from pay_hotel_order pho");
			        if(inputMoneyFlag) sqlBuffer.append(",hotel_money_amount hma  ");
	    			sqlBuffer.append(" where pho.orderNum is not null");
	    			 if(StringUtils.isNotBlank(payType)){
		                	if("3".equals(payType)){
		                		sqlBuffer.append(" and (pho.payType=3 or pho.payType=5)");
		                	}else{
		                		sqlBuffer.append(" and pho.payType =").append(payType);
		                	}
		                }
	    			if(StringUtils.isNotBlank(payerName)){
	    				sqlBuffer.append(" and pho.payerName like'%").append(payerName.trim()).append("%'");
	    			}
	    			if(StringUtils.isNotBlank(toBankNname)){
	    				sqlBuffer.append(" and pho.toBankNname like'%").append(toBankNname.trim()).append("%'");
	    			}
	    			if(StringUtils.isNotBlank(createDateBegin)){
	    				sqlBuffer.append(" and pho.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
	    			}
	    			if(StringUtils.isNotBlank(createDateEnd)){
	    				sqlBuffer.append(" and pho.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
	    			}
	    			if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
			        	sqlBuffer.append(" and pho.isAsAccount=1");
			        	if(StringUtils.isNotBlank(accountDateBegin)){
			        		sqlBuffer.append(" and pho.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
			        	}
			        	if(StringUtils.isNotBlank(accountDateEnd)){
			        		sqlBuffer.append(" and pho.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
			        	}
			        }
	    			if(inputMoneyFlag) sqlBuffer.append(" AND pho.moneySerialNum = hma.serialNum AND hma.serialNum in (select serialNum from hotel_money_amount " + payAmountWhere(payAmountStrat, payAmountEnd) + ")");
			        if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
			        	sqlBuffer.append(" and printFlag=").append(printFlag.trim()).append(" ");
			        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
			        	sqlBuffer.append(" and ( printFlag=").append(printFlag.trim()).append(" or printFlag is null) ");
			        }
		    		if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    			sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
				    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
				    	sqlBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
				    	  .append(" or isAsAccount is null)");
				    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
				    	sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
				    }
	    		    sqlBuffer.append(" ) tb where tmp.orderNum = tb.orderNum) payment ")
	    		    .append(" order by ").append(orderBy);
    		return  productorderDao.findBySql(sqlBuffer.toString(),Map.class);
    	}else if (null != orderType && Integer.parseInt(orderType) == Context.ORDER_TYPE_ISLAND) {
    		sqlBuffer.append("select payType,accountDate, createDate, updateDate, payerName, toBankNname, opCreateBy, acitivityName, activityId, proCompany, groupOpenDate, " +
    				"groupCloseDate, orderNum, payStatus, orderCompanyName, groupCode, totalMoney, payedMoney, accountedmoney, id, orderUuid, orderTime, activationDate, " +
    				"placeHolderType, agentinfo_id, createUserName, orderCreateDate, orderUpdateDate, orderStatus, payid, payUuid, printTime, printFlag, ordertype, " +
    				"payPriceType, account from (SELECT tb.payType,tb.accountDate,tb.createDate, tb.updateDate, tb.payerName, tb.toBankNname,tmp.createBy AS opCreateBy, " +
    				"tmp.acitivityName, tmp.activityId, tmp.proCompany, tmp.groupOpenDate, " +
    				"tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.orderCompanyName, tmp.groupCode, tmp.total_money AS totalMoney, " +
    				"tb.moneySerialNum  as payedMoney, tmp.accounted_money  AS accountedmoney,tmp.id,tmp.orderUuid," +
    						"tmp.orderTime,tmp.activationDate,tmp.placeHolderType," +
					"tmp.orderCompany AS agentinfo_id,tmp.createUserName,tmp.createDate orderCreateDate,tmp.updateDate orderUpdateDate,tmp.orderStatus," +
					"tb.id payid,tb.payUuid,tb.printTime,tb.printFlag," + Context.ORDER_TYPE_ISLAND + " ordertype,tb.payPriceType,tb.isAsAccount AS account FROM(SELECT pro.accounted_money,agp.createBy, " +
					"activity.activitySerNum, activity.activityName acitivityName, activity.uuid AS activityId, activity.wholesaler_id proCompany, activity.createBy AS activityCreateBy, " +
					"agp.groupOpenDate, agp.groupEndDate groupCloseDate, pro.orderNum, pro.orderPersonName, pro.orderPersonPhoneNum, pro.orderPersonNum," +
					" pro.orderStatus payStatus, pro.payMode proPayMode, pro.remainDays proRemainDays, pro.orderCompanyName, agp.groupCode, " +
					"pro.total_money, pro.payed_money, " +
					"pro.id,pro.uuid orderUuid , pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany, " +
					"( SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy ) createUserName, pro.createDate, " +
					"pro.updateDate, pro.orderStatus FROM activity_island activity, activity_island_group agp, island_order pro " +
					"WHERE activity.uuid = pro.activity_island_uuid AND agp.uuid = pro.activity_island_group_uuid AND agp.activity_island_uuid = pro.activity_island_uuid  AND pro.delFlag=").append(ProductOrderCommon.DEL_FLAG_NORMAL)
	    			.append(" AND pro.orderStatus in (" + Context.ISLAND_ORDER_STATUS_DQR+"," + Context.ISLAND_ORDER_STATUS_YQR + ") and activity.wholesaler_id =" + companyId ).append(" ");
    				sqlBuffer.append(queryCon);
			        sqlBuffer.append(" ) tmp,")

	    			.append("(select pio.payType,pio.accountDate,pio.createDate, pio.updateDate, pio.payerName, pio.toBankNname,pio.id,pio.uuid payUuid, pio.orderNum,pio.printTime,pio.printFlag,pio.isAsAccount,CONCAT(cast(pio.payPriceType as char),")
	    			.append("IF (pio.isAsAccount IS NULL or pio.isAsAccount = 0 or pio.isAsAccount = 2, 0, 1)) payPriceType,")

	    			.append("moneySerialNum from pay_island_order pio");
			        if(inputMoneyFlag) sqlBuffer.append(",island_money_amount ima  ");
			        sqlBuffer.append(" where pio.orderNum is not null");
			        if(StringUtils.isNotBlank(payType)){
	                	if("3".equals(payType)){
	                		sqlBuffer.append(" and (pio.payType=3 or pio.payType=5)");
	                	}else{
	                		sqlBuffer.append(" and pio.payType =").append(payType);
	                	}
	                }
			        if(StringUtils.isNotBlank(payerName)){
	    				sqlBuffer.append(" and pio.payerName like'%").append(payerName.trim()).append("%'");
	    			}
	    			if(StringUtils.isNotBlank(toBankNname)){
	    				sqlBuffer.append(" and pio.toBankNname like'%").append(toBankNname.trim()).append("%'");
	    			}
	    			if(StringUtils.isNotBlank(createDateBegin)){
	    				sqlBuffer.append(" and pio.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
	    			}
	    			if(StringUtils.isNotBlank(createDateEnd)){
	    				sqlBuffer.append(" and pio.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
	    			}
			        if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
			        	sqlBuffer.append(" and pio.isAsAccount=1");
			        	if(StringUtils.isNotBlank(accountDateBegin)){
			        		sqlBuffer.append(" and pio.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
			        	}
			        	if(StringUtils.isNotBlank(accountDateEnd)){
			        		sqlBuffer.append(" and pio.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
			        	}
			        }
			        if(inputMoneyFlag) sqlBuffer.append(" AND pio.moneySerialNum = ima.serialNum AND ima.serialNum in (select serialNum from island_money_amount " + payAmountWhere(payAmountStrat, payAmountEnd) + ")");
			        if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
			        	sqlBuffer.append(" and printFlag=").append(printFlag.trim()).append(" ");
			        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
			        	sqlBuffer.append(" and ( printFlag=").append(printFlag.trim()).append(" or printFlag is null) ");
			        }
		    		if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    			sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
				    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
				    	sqlBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
				    	  .append(" or isAsAccount is null)");
				    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
				    	sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
				    }
	    		    sqlBuffer.append(" ) tb where tmp.orderNum = tb.orderNum) payment ").append(" order by ").append(orderBy);;
    		return  productorderDao.findBySql(sqlBuffer.toString(),Map.class);
    	}
    	else if (null != orderType && Integer.parseInt(orderType) == Context.ORDER_TYPE_JP) {
    		/**
    		 * 增加isAsAccount查询字段
    		 * 用于驳回操作判断
    		 */

    		sb.append("select tb.payType,tb.accountDate,tmp.salerId, tmp.saler,tmp.creator,tb.createDate, tb.updateDate, tb.payerName, tb.toBankNname,tmp.opCreateBy,tmp.agentinfo_id,tmp.id,tmp.orderNum,tmp.createDate orderCreateDate,tmp.updateDate orderUpdateDate,tmp.createDate orderTime,tmp.comments,")

    			.append("tmp.personNum,tmp.orderType  ordertype,tmp.createUserName,tmp.orderGroupCode groupCode,tmp.agentName orderCompanyName,")
    			.append("tmp.airlines,tmp.airticketId,tmp.airType,tmp.remark,tmp.groupOpenDate,tmp.groupCloseDate,tb.id payid,tb.payPriceType,tb.moneySerialNum  AS payedMoney," )
    			.append("tb.printTime,tb.printFlag,tb.isAsAccount account,")
    			.append(" tmp.total_money AS totalMoney,tmp.accounted_money  AS accountedmoney,tmp.payStatus from (select ao.accounted_money,ao.salerId, ao.salerName saler,( SELECT su. NAME FROM sys_user su WHERE su.id = ao.create_by ) creator,aa.createBy opCreateBy," )
    			.append("ao.agentinfo_id,ao.id id,ao.order_no orderNum," )
    			.append("ao.order_state payStatus,")
    			.append("ao.create_date createDate,ao.update_date updateDate,ao.comments comments,ao.person_num personNum,")
    			.append("7 orderType,ao.group_code orderGroupCode,ao.total_money,ai.agentName agentName,")
    			.append("su.name createUserName,aa.airlines airlines,aa.id airticketId,cast(aa.airType as signed) airType,aa.remark remark,")
    			.append(" aa.startingDate as groupOpenDate,' ' groupCloseDate from airticket_order ao ")
    			.append("LEFT JOIN agentinfo ai ON ao.agentinfo_id = ai.id LEFT JOIN sys_user su ON ao.create_by = su.id ")
    			.append("LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id LEFT JOIN sys_user aasu ON aa.createBy = aasu.id ")
    			.append(" WHERE ao.product_type_id ="+Context.ORDER_TYPE_JP+" and aa.proCompany = ").append(""+companyId+" ");
	    		 //团期
		        if(StringUtils.isNotBlank(groupCode)){
		        	 sb.append(" and ao.group_code like '%").append(groupCode.trim()).append("%' ");
		         }
		        //订单编号
		        if(StringUtils.isNotBlank(orderNum)){
		        	sb.append("and ao.order_no like '%").append(orderNum.trim()).append("%' ");
		        }
		        //渠道
		        if(StringUtils.isNotBlank(agentId)){
		        	sb.append("and ao.agentinfo_id =").append(agentId.trim()+" ");
		        }
		        //销售
		        if(StringUtils.isNotBlank(saler)){
		        	sb.append("and ao.salerId = ").append(saler.trim()+" ");
		        }
		      //下单人
		        if(StringUtils.isNotBlank(creator)){
		        	sb.append("and ao.create_by = ").append(creator.trim()+" ");
		        }
		        //计调
		        if(StringUtils.isNotBlank(jdString)){
		        	sb.append(" and aa.createBy =").append(jdString.trim()+" ");
		        }
		        //下单开始时间
		        if (StringUtils.isNotBlank(orderTimeBegin)) {
		            sb.append( "and ao.create_date >= '" + orderTimeBegin.trim() + " 00:00:00" + "' ");
		        }
		        //下单结束时间
		        if (StringUtils.isNotBlank(orderTimeEnd)) {
		        	sb.append( "and ao.create_date <= '" + orderTimeEnd.trim() + " 23:59:59" + "' ");
		        }
                //出团日期
                if(StringUtils.isNotBlank(groupOpenDateStart)){
                    sb.append(" and aa.outTicketTime >= '").append(groupOpenDateStart.trim()).append(" 00:00:00'");
                }
                if(StringUtils.isNotBlank(groupOpenDateEnd)){
                    sb.append(" and aa.outTicketTime <= '").append(groupOpenDateEnd.trim()).append(" 23:59:59'");
                }
    			sb.append(" ) tmp,(select op.payType,op.accountDate,op.createDate, op.updateDate, op.payerName, op.toBankNname,op.id,op.orderNum,op.ordertype,op.printTime,op.printFlag,op.isAsAccount,CONCAT(cast(op.payPriceType as char),IF (op.isAsAccount IS NULL or op.isAsAccount = 0 or op.isAsAccount = 2, 0, 1)) payPriceType,")

    		    .append("moneySerialNum from orderpay op");
    		if(inputMoneyFlag) sb.append(",money_amount mam  ");
    		sb.append(" where op.orderNum IS NOT NULL and op.orderType = ").append(orderType);
    		if(StringUtils.isNotBlank(payType)){
            	if("3".equals(payType)){
            		sb.append(" and (op.payType=3 or op.payType=5)");
            	}else{
            		sb.append(" and op.payType =").append(payType);
            	}
            }
    		if(StringUtils.isNotBlank(payerName)){
				sb.append(" and op.payerName like'%").append(payerName.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(toBankNname)){
				sb.append(" and op.toBankNname like'%").append(toBankNname.trim()).append("%'");
			}
			if(StringUtils.isNotBlank(createDateBegin)){
				sb.append(" and op.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
			}
			if(StringUtils.isNotBlank(createDateEnd)){
				sb.append(" and op.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
			}
    		if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
	        	sb.append(" and op.isAsAccount=1");
	        	if(StringUtils.isNotBlank(accountDateBegin)){
	        		sb.append(" and op.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
	        	}
	        	if(StringUtils.isNotBlank(accountDateEnd)){
	        		sb.append(" and op.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
	        	}
	        }
    		if(inputMoneyFlag) sb.append(" AND op.moneySerialNum = mam.serialNum AND  mam.serialNum in (select serialNum from money_amount " + payAmountWhere(payAmountStrat, payAmountEnd) + ")");
	    		if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
		        	sb.append(" and printFlag=").append(printFlag).append(" ");
		        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
		        	sb.append(" and ( printFlag=").append(printFlag).append(" or printFlag is null) ");
		        }
    		    if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
    		    	sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
    		    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
			    	sb.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
			    	  .append(" or isAsAccount is null)");
			    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
			    	sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
			    }
    		    sb.append(" ) tb where tmp.orderNum = tb.orderNum").append(" order by ").append(orderBy);;
    		return  productorderDao.findBySql(sb.toString(),Map.class);
    	}else if(null != orderType && Integer.parseInt(orderType) == Context.ORDER_TYPE_ALL){//Context.ORDER_TYPE_ALL
    		sb.append("select payType,accountDate, salerId, saler, createDate, updateDate, payerName, toBankNname, opCreateBy, acitivityName, activityId, proCompany, groupOpenDate, " +
    				"groupCloseDate, orderNum, payStatus, orderCompanyName, groupCode, totalMoney, payedMoney, accountedmoney, id, orderUuid, orderTime, activationDate, " +
    				"placeHolderType, agentinfo_id, creator, orderCreateDate, orderUpdateDate, orderStatus, payid, payUuid, printTime, printFlag, ordertype, " +
    				"payPriceType, account from (SELECT tb.payType, tb.accountDate,tmp.salerId, tmp.salerName saler,tb.createDate, tb.updateDate, tb.payerName, tb.toBankNname,tb.receiptConfirmationDate," +
    				"tmp.createBy AS opCreateBy, ' ' AS acitivityName, tmp.airticketId AS activityId, tmp.proCompany, tmp.groupOpenDate, " +
    				"tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.agentName orderCompanyName, tmp.orderGroupCode groupCode, " +
    				" tmp.total_money totalMoney, tb.moneySerialNum payedMoney,tmp.accounted_money AS accountedmoney," +
    				"tmp.id,tmp.orderUuid, tmp.createDate orderTime, tmp.activationDate, tmp.placeHolderType, tmp.agentinfo_id, tmp.creator, " +
    				"tmp.createDate orderCreateDate, tmp.updateDate orderUpdateDate, tmp.orderType orderStatus, tb.id payid,tb.payUuid, tb.printTime, tb.printFlag, tmp.orderType ordertype, tb.payPriceType," +
    				"tb.isAsAccount AS account FROM ( SELECT ao.salerId, ao.salerName,aa.createBy, ao.agentinfo_id, ao.id id,'abc' orderUuid, ao.order_no orderNum, ao.order_state payStatus, " +
    				"ao.create_date createDate, ao.update_date updateDate, ao.comments comments, ao.person_num personNum, 7 orderType," +
    				" ao.group_code orderGroupCode, ao.total_money, ao.payed_money, ao.accounted_money, ao.place_holder_type placeHolderType, " +
    				"ao.activationDate, ai.agentName agentName, su. NAME creator, aa.airlines airlines, aa.id airticketId, " +
    				"cast(aa.airType AS signed) airType, aa.remark remark, aa.proCompany, aa.startingDate as groupOpenDate, " +
    				"null groupCloseDate FROM airticket_order ao LEFT JOIN agentinfo ai ON ao.agentinfo_id = ai.id LEFT JOIN " +
    				"sys_user su ON ao.create_by = su.id LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id LEFT JOIN sys_user aasu " +
    				"ON aa.createBy = aasu.id WHERE  ao.product_type_id ="+Context.ORDER_TYPE_JP+" and aa.proCompany = ").append(""+companyId+" ");
    		        //团期
    		        if(StringUtils.isNotBlank(groupCode)){
    		        	 sb.append(" and ao.group_code like '%").append(groupCode.trim()).append("%' ");
    		         }
    		        //订单编号
    		        if(StringUtils.isNotBlank(orderNum)){
    		        	sb.append("and ao.order_no like '%").append(orderNum.trim()).append("%' ");
    		        }
    		        //渠道
    		        if(StringUtils.isNotBlank(agentId)){
    		        	sb.append("and ao.agentinfo_id =").append(agentId.trim()+" ");
    		        }
    		        //销售
    		        if(StringUtils.isNotBlank(saler)){
    		        	sb.append("and ao.salerId = ").append(saler.trim()+" ");
    		        }
    		      //下单人
    		        if(StringUtils.isNotBlank(creator)){
    		        	sb.append("and ao.create_by = ").append(creator.trim()+" ");
    		        }
    		        //计调
    		        if(StringUtils.isNotBlank(jdString)){
    		        	sb.append(" and aa.createBy =").append(jdString.trim()+" ");
    		        }
    		        //下单开始时间
    		        if (StringUtils.isNotBlank(orderTimeBegin)) {
    		            sb.append( "and ao.create_date >= '" + orderTimeBegin.trim() + " 00:00:00" + "' ");
    		        }
    		        //下单结束时间
    		        if (StringUtils.isNotBlank(orderTimeEnd)) {
    		        	sb.append( "and ao.create_date <= '" + orderTimeEnd.trim() + " 23:59:59" + "' ");
    		        }
                    //出团日期
                    if(StringUtils.isNotBlank(groupOpenDateStart)){
                        sb.append(" and aa.outTicketTime >= '").append(groupOpenDateStart.trim()).append(" 00:00:00'");
                    }
                    if(StringUtils.isNotBlank(groupOpenDateEnd)){
                        sb.append(" and aa.outTicketTime <= '").append(groupOpenDateEnd.trim()).append(" 23:59:59'");
                    }
    				sb.append(") tmp,(SELECT op.payType,op.accountDate,op.createDate, op.updateDate, op.payerName, op.toBankNname,op.receiptConfirmationDate,op.id, 'abc' payUuid, orderNum, op.ordertype, printTime, printFlag, isAsAccount, CONCAT( cast(payPriceType AS CHAR), " +
    				"IF ( isAsAccount IS NULL OR isAsAccount = 0 OR isAsAccount = 2, 0, 1 )) payPriceType,moneySerialNum FROM orderpay op");

    				if(inputMoneyFlag) sb.append(", money_amount mam");
    				sb.append(" WHERE op.orderNum IS NOT NULL");
    				if(StringUtils.isNotBlank(payType)){
	                	if("3".equals(payType)){
	                		sb.append(" and (op.payType=3 or op.payType=5)");
	                	}else{
	                		sb.append(" and op.payType =").append(payType);
	                	}
	                }
    				if(StringUtils.isNotBlank(payerName)){
    					sb.append(" and op.payerName like'%").append(payerName.trim()).append("%'");
    				}
    				if(StringUtils.isNotBlank(toBankNname)){
    					sb.append(" and op.toBankNname like'%").append(toBankNname.trim()).append("%'");
    				}
                    if(StringUtils.isNotBlank(receiptConfirmationDateBegin)){
                        sb.append(" and op.receiptConfirmationDate >='").append(receiptConfirmationDateBegin.trim()).append(" 00:00:00' ");
                    }
                    if(StringUtils.isNotBlank(receiptConfirmationDateEnd)){
                        sb.append(" and op.receiptConfirmationDate <='").append(receiptConfirmationDateEnd.trim()).append(" 23:59:59' ");
                    }
    				if(StringUtils.isNotBlank(createDateBegin)){
    					sb.append(" and op.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
    				}
    				if(StringUtils.isNotBlank(createDateEnd)){
    					sb.append(" and op.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
    				}
    				if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
    		        	sb.append(" and op.isAsAccount=1");
    		        	if(StringUtils.isNotBlank(accountDateBegin)){
    		        		sb.append(" and op.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
    		        	}
    		        	if(StringUtils.isNotBlank(accountDateEnd)){
    		        		sb.append(" and op.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
    		        	}
    		        } 

    				sb.append(" AND op.orderType =").append(" "+Context.ORDER_TYPE_JP+" ");
    				if(inputMoneyFlag) sb.append(" AND op.moneySerialNum = mam.serialNum AND  mam.serialNum in (select serialNum from money_amount " + payAmountWhere(payAmountStrat, payAmountEnd) + ")");
    				if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
    		        	sb.append(" and printFlag=").append(printFlag).append(" ");
    		        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
    		        	sb.append(" and ( printFlag=").append(printFlag).append(" or printFlag is null) ");
    		        }
		    		 if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
		 		    	sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
		 		    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
				    	sb.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
				    	  .append(" or isAsAccount is null)");
				    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
				    	sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
				    }

		    		sb.append(" ) tb WHERE tmp.orderNum = tb.orderNum UNION ");
		    		sb.append("SELECT tb.payType,tb.accountDate,tmp.salerId, tmp.salerName saler,tb.createDate, tb.updateDate, tb.payerName, tb.toBankNname,tb.receiptConfirmationDate,tmp.createBy AS opCreateBy," +
		    				  " tmp.acitivityName, tmp.activityId, tmp.proCompany, tmp.groupOpenDate, " +
    				          "tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.orderCompanyName, tmp.groupCode, tmp.total_money AS totalMoney, " +
    				        "tb.moneySerialNum as payedMoney,tmp.accounted_money AS accountedmoney,tmp.id,tmp.orderUuid,tmp.orderTime,tmp.activationDate,tmp.placeHolderType," +
    						"tmp.orderCompany AS agentinfo_id,tmp.creator,tmp.createDate orderCreateDate,tmp.updateDate orderUpdateDate,tmp.orderStatus," +
    						"tb.id payid,tb.payUuid,tb.printTime,tb.printFlag,tb.ordertype,tb.payPriceType,tb.isAsAccount AS account FROM(SELECT pro.salerId, pro.salerName,agp.createBy, " +
    						"activity.activitySerNum, activity.acitivityName, activity.id AS activityId, activity.proCompany, activity.createBy AS activityCreateBy, " +
    						"agp.groupOpenDate, agp.groupCloseDate, pro.orderNum, pro.orderPersonName, pro.orderPersonPhoneNum, pro.orderPersonNum," +
    						" pro.payStatus, pro.payMode proPayMode, pro.remainDays proRemainDays, pro.orderCompanyName, agp.groupCode, " +
    						"pro.total_money total_money, pro.payed_money,pro.accounted_money, " +
    						"pro.id, 'abc' orderUuid,pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany, " +
    						"( SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy ) creator, pro.createDate, " +
    						"pro.updateDate, pro.orderStatus FROM travelactivity activity, activitygroup agp, productorder pro " +
    						"WHERE activity.id = pro.productId AND agp.id = pro.productGroupId AND agp.srcActivityId = pro.productId  AND pro.delFlag=")
    						.append(ProductOrderCommon.DEL_FLAG_NORMAL)
    						.append(" and activity.proCompany=")
		    			    .append("" + companyId +" ")  ;
			    			sb.append(queryCon);

		    			sb.append(" ) tmp,")

		    			.append("(select op.payType,op.accountDate,op.createDate, op.updateDate, op.payerName, op.toBankNname,op.receiptConfirmationDate,op.id,op.orderid,'abc' payUuid,orderNum,printTime,printFlag,op.ordertype,isAsAccount,CONCAT(cast(payPriceType as char),")
		    			.append("IF (isAsAccount IS NULL or isAsAccount = 0 or isAsAccount = 2, 0, 1)) payPriceType,")

		    			.append("moneySerialNum from orderpay op ");
		    			if(inputMoneyFlag) sb.append(", money_amount mam");
		    			sb.append(" where op.orderNum is not null AND op.orderType in(1,2,3,4,5,10)");
		    			if(StringUtils.isNotBlank(payType)){
		                	if("3".equals(payType)){
		                		sb.append(" and (op.payType=3 or op.payType=5)");
		                	}else{
		                		sb.append(" and op.payType =").append(payType);
		                	}
		                }
		    			if(StringUtils.isNotBlank(payerName)){
		    				sb.append(" and op.payerName like'%").append(payerName.trim()).append("%'");
		    			}
		    			if(StringUtils.isNotBlank(toBankNname)){
		    				sb.append(" and op.toBankNname like'%").append(toBankNname.trim()).append("%'");
		    			}
                        if(StringUtils.isNotBlank(receiptConfirmationDateBegin)){
                            sb.append(" and op.receiptConfirmationDate >='").append(receiptConfirmationDateBegin.trim()).append(" 00:00:00' ");
                        }
                        if(StringUtils.isNotBlank(receiptConfirmationDateEnd)){
                            sb.append(" and op.receiptConfirmationDate <='").append(receiptConfirmationDateEnd.trim()).append(" 23:59:59' ");
                        }
		    			if(StringUtils.isNotBlank(createDateBegin)){
		    				sb.append(" and op.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
		    			}
		    			if(StringUtils.isNotBlank(createDateEnd)){
		    				sb.append(" and op.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
		    			}
		    			if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
		    	        	sb.append(" and op.isAsAccount=1");
		    	        	if(StringUtils.isNotBlank(accountDateBegin)){
		    	        		sb.append(" and op.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
		    	        	}
		    	        	if(StringUtils.isNotBlank(accountDateEnd)){
		    	        		sb.append(" and op.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
		    	        	}
		    	        } 

		    			if(inputMoneyFlag) sb.append(" AND op.moneySerialNum = mam.serialNum AND  mam.serialNum in (select serialNum from money_amount " + payAmountWhere(payAmountStrat, payAmountEnd) + ")");
		    			if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
				        	sb.append(" and printFlag=").append(printFlag).append(" ");
				        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
				        	sb.append(" and ( printFlag=").append(printFlag).append(" or printFlag is null) ");
				        }
			    		if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
			    			sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
					    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
					    	sb.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
					    	  .append(" or isAsAccount is null)");
					    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
					    	sb.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
					    }
		    		    sb.append(" ) tb where tmp.orderNum = tb.orderNum and tmp.id = tb.orderid ");
		    		    //添加海岛游、酒店业务20150616
		    		    StringBuffer hotelStringBuffer = new StringBuffer();
		    		    StringBuffer islandStringBuffer = new StringBuffer();
		    		    Integer ordertype = Context.ORDER_TYPE_HOTEL;
		    		    hotelStringBuffer.append(" UNION ")
		    		    .append("SELECT tb.payType,tb.accountDate,tmp.salerId, tmp.saler,tb.createDate, tb.updateDate, tb.payerName, tb.toBankNname,tb.receiptConfirmationDate,tmp.createBy AS opCreateBy, tmp.acitivityName, tmp.activityId, tmp.proCompany, ")
		    		    .append("tmp.groupOpenDate, tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.orderCompanyName,")
		    		    .append("tmp.groupCode, tmp.total_money AS totalMoney,")
		    		    .append("tb.moneySerialNum AS payedMoney,tmp.accounted_money AS accountedmoney,")
		    		    .append("tmp.id,tmp.orderUuid, tmp.orderTime, tmp.activationDate, tmp.placeHolderType, tmp.orderCompany AS agentinfo_id, ")
		    		    .append("tmp.creator, tmp.createDate orderCreateDate, tmp.updateDate orderUpdateDate, tmp.orderStatus, tb.id payid,tb.payUuid,")
		    		    .append("tb.printTime, tb.printFlag," +  ordertype +  " ordertype, tb.payPriceType,tb.isAsAccount AS account FROM ")
		    		    .append("( SELECT pro.orderSalerId salerId, NULL saler,agp.createBy, activity.activitySerNum, activity.activityName acitivityName,")
		    		    .append("activity.uuid AS activityId, activity.wholesaler_id proCompany, ")
		    		    .append("activity.createBy AS activityCreateBy,agp.groupOpenDate, agp.groupEndDate groupCloseDate,	")
		    		    .append("pro.orderNum,pro.orderPersonName,pro.orderPersonPhoneNum,pro.orderPersonNum, ")
		    		    .append("pro.orderStatus payStatus,pro.payMode proPayMode,pro.remainDays proRemainDays,pro.orderCompanyName,")
		    		    .append("agp.groupCode,  pro.total_money,")
		    		    .append("pro.payed_money,pro.accounted_money,pro.id,pro.uuid orderUuid , pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany,")
		    		    .append("( SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy ) creator,")
		    		    .append("pro.createDate, pro.updateDate, pro.orderStatus FROM activity_hotel activity,")
		    		    .append("activity_hotel_group agp, hotel_order pro WHERE activity.uuid = pro.activity_hotel_uuid ")
		    		    .append("AND agp.uuid = pro.activity_hotel_group_uuid AND agp.activity_hotel_uuid = pro.activity_hotel_uuid ")
		    		    .append("AND pro.delFlag = 0 AND pro.orderStatus IN (1, 2) AND activity.wholesaler_id = ").append(companyId + " ")
		    		    .append(queryCon)
		    		    .append(" ) tmp,")
		    		    .append("(SELECT pho.payType,pho.accountDate,pho.createDate, pho.updateDate, pho.payerName, pho.toBankNname,pho.receiptConfirmationDate,pho.id,pho.uuid payUuid, orderNum, printTime, printFlag, isAsAccount,CONCAT( cast(payPriceType AS CHAR),")
		    		    .append("IF (isAsAccount IS NULL OR isAsAccount = 0 OR isAsAccount = 2, 0,1)) payPriceType,")
		    		    .append(" moneySerialNum FROM pay_hotel_order pho");
		    		    if(inputMoneyFlag) hotelStringBuffer.append(", hotel_money_amount hma");
		    		    hotelStringBuffer.append(" WHERE pho.orderNum IS NOT NULL");
		    		    if(StringUtils.isNotBlank(payType)){
		                	if("3".equals(payType)){
		                		hotelStringBuffer.append(" and (pho.payType=3 or pho.payType=5)");
		                	}else{
		                		hotelStringBuffer.append(" and pho.payType =").append(payType);
		                	}
		                }
		    		    if(StringUtils.isNotBlank(payerName)){
		    		    	hotelStringBuffer.append(" and pho.payerName like'%").append(payerName.trim()).append("%'");
		    			}
		    			if(StringUtils.isNotBlank(toBankNname)){
		    				hotelStringBuffer.append(" and pho.toBankNname like'%").append(toBankNname.trim()).append("%'");
		    			}
                        if(StringUtils.isNotBlank(receiptConfirmationDateBegin)){
                            hotelStringBuffer.append(" and pho.receiptConfirmationDate >='").append(receiptConfirmationDateBegin.trim()).append(" 00:00:00' ");
                        }
                        if(StringUtils.isNotBlank(receiptConfirmationDateEnd)){
                            hotelStringBuffer.append(" and pho.receiptConfirmationDate <='").append(receiptConfirmationDateEnd.trim()).append(" 23:59:59' ");
                        }
		    			if(StringUtils.isNotBlank(createDateBegin)){
		    				hotelStringBuffer.append(" and pho.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
		    			}
		    			if(StringUtils.isNotBlank(createDateEnd)){
		    				hotelStringBuffer.append(" and pho.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
		    			}
		    		    if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
		    		    	hotelStringBuffer.append(" and pho.isAsAccount=1");
		    	        	if(StringUtils.isNotBlank(accountDateBegin)){
		    	        		hotelStringBuffer.append(" and pho.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
		    	        	}
		    	        	if(StringUtils.isNotBlank(accountDateEnd)){
		    	        		hotelStringBuffer.append(" and pho.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
		    	        	}
		    	        } 

		    		    if(inputMoneyFlag) hotelStringBuffer.append(" AND pho.moneySerialNum = hma.serialNum AND hma.serialNum in (select serialNum from hotel_money_amount " + payAmountWhere(payAmountStrat, payAmountEnd) + ")");
		    		    if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
		    		    	hotelStringBuffer.append(" and printFlag=").append(printFlag).append(" ");
				        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
				        	hotelStringBuffer.append(" and ( printFlag=").append(printFlag).append(" or printFlag is null) ");
				        }
		    		    if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
		    		    	hotelStringBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
					    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
					    	hotelStringBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
					    	  .append(" or isAsAccount is null)");
					    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
					    	hotelStringBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
					    }
		    		    hotelStringBuffer.append(") tb WHERE tmp.orderNum = tb.orderNum");
		    		    //海岛游sql替换酒店hotel字符20150616
		    		    islandStringBuffer.append(hotelStringBuffer.toString()
		    		    		.replaceAll(Context.ORDER_TYPE_HOTEL + " ordertype,", Context.ORDER_TYPE_ISLAND + " ordertype,")
		    		    		.replaceAll("hotel", "island").replace("pho", "pio").replaceAll("hma", "ima"));
		    		    sb.append(hotelStringBuffer).append(islandStringBuffer);
		    		    sb.append(") refundList ").append(" order by ").append(orderBy);;
		    		    return  productorderDao.findBySql(sb.toString(),Map.class);
    		
    	}else {
    		sqlBuffer.append("select payType,accountDate, salerId, saler, createDate, updateDate, payerName, toBankNname, opCreateBy, acitivityName, " +
    				"activityId, proCompany, groupOpenDate, groupCloseDate, orderNum, payStatus, orderCompanyName, groupCode, totalMoney, payedMoney, " +
    				"accountedmoney, id, orderTime, activationDate, placeHolderType, agentinfo_id, creator, orderCreateDate, " +
    				"orderUpdateDate, orderStatus, payid, printTime, printFlag, ordertype, payPriceType, account from (SELECT " +
    				"tb.payType,tb.accountDate,tmp.salerId, tmp.salerName saler,tb.createDate, tb.updateDate, tb.payerName, tb.toBankNname,tmp.createBy AS opCreateBy, tmp.acitivityName, tmp.activityId, tmp.proCompany, tmp.groupOpenDate, " +
    				"tmp.groupCloseDate, tmp.orderNum, tmp.payStatus, tmp.orderCompanyName, tmp.groupCode, tmp.total_money AS totalMoney, " +
    				"tb.moneySerialNum  as payedMoney,tmp.accounted_money AS accountedmoney,tmp.id,tmp.orderTime,tmp.activationDate,tmp.placeHolderType," +
					"tmp.orderCompany AS agentinfo_id,tmp.creator,tmp.createDate orderCreateDate,tmp.updateDate orderUpdateDate,tmp.orderStatus," +
					"tb.id payid,tb.printTime,tb.printFlag,tb.ordertype,tb.payPriceType,tb.isAsAccount AS account FROM ( SELECT pro.accounted_money,pro.salerId, pro.salerName,agp.createBy, " +
					"activity.activitySerNum, activity.acitivityName, activity.id AS activityId, activity.proCompany, activity.createBy AS activityCreateBy, " +
					"agp.groupOpenDate, agp.groupCloseDate, pro.orderNum, pro.orderPersonName, pro.orderPersonPhoneNum, pro.orderPersonNum," +
					" pro.payStatus, pro.payMode proPayMode, pro.remainDays proRemainDays, pro.orderCompanyName, agp.groupCode, " +
					" pro.total_money total_money, pro.payed_money, " +
					"pro.id, pro.orderTime, pro.activationDate, pro.placeHolderType, pro.orderCompany, " +
					"( SELECT su. NAME FROM sys_user su WHERE su.id = pro.createBy ) creator, pro.createDate, " +
					"pro.updateDate, pro.orderStatus FROM travelactivity activity, activitygroup agp, productorder pro " +
					"WHERE activity.id = pro.productId AND agp.id = pro.productGroupId AND agp.srcActivityId = pro.productId  AND pro.delFlag=")
					.append(ProductOrderCommon.DEL_FLAG_NORMAL)
					.append(" AND pro.orderStatus = ").append(orderType)
					.append(" and activity.proCompany=")
		    	    .append("" + companyId +" ")  
			        .append(queryCon)
    					    
    			.append(" ) tmp,")
    			.append("(select op.payType,op.accountDate,op.createDate, op.updateDate, op.payerName, op.toBankNname,op.id,op.orderNum,op.printTime,op.printFlag,op.ordertype,op.isAsAccount,CONCAT(cast(op.payPriceType as char),")
    			.append("IF (op.isAsAccount IS NULL or op.isAsAccount = 0 or op.isAsAccount = 2 OR isAsAccount = 99, 0, 1)) payPriceType,")
    			.append("moneySerialNum from orderpay op");
    			if(inputMoneyFlag) sqlBuffer.append(",money_amount mam  ");
    			sqlBuffer.append(" where op.orderNum is not null and op.orderType =").append(orderType);
                if(StringUtils.isNotBlank(payType)){
                	if("3".equals(payType)){
                		sqlBuffer.append(" and (op.payType=3 or op.payType=5)");
                	}else{
                		sqlBuffer.append(" and op.payType =").append(payType);
                	}
                }
    			if(StringUtils.isNotBlank(payerName)){
    				sqlBuffer.append(" and op.payerName like'%").append(payerName.trim()).append("%'");
    			}
    			if(StringUtils.isNotBlank(toBankNname)){
    				sqlBuffer.append(" and op.toBankNname like'%").append(toBankNname.trim()).append("%'");
    			}
    			if(StringUtils.isNotBlank(createDateBegin)){
    				sqlBuffer.append(" and op.createDate >='").append(createDateBegin.trim()).append(" 00:00:00' ");
    			}
    			if(StringUtils.isNotBlank(createDateEnd)){
    				sqlBuffer.append(" and op.createDate <='").append(createDateEnd.trim()).append(" 23:59:59' ");
    			}
    			if(StringUtils.isNotBlank(accountDateBegin) || StringUtils.isNotBlank(accountDateEnd)){
    				sqlBuffer.append(" and op.isAsAccount=1");
    	        	if(StringUtils.isNotBlank(accountDateBegin)){
    	        		sqlBuffer.append(" and op.accountDate >= '"+accountDateBegin.trim()+" 00:00:00" + "' ");
    	        	}
    	        	if(StringUtils.isNotBlank(accountDateEnd)){
    	        		sqlBuffer.append(" and op.accountDate <= '"+accountDateEnd.trim()+" 23:59:59" + "' ");
    	        	}
    	        } 

    			if(inputMoneyFlag) sqlBuffer.append(" AND op.moneySerialNum = mam.serialNum AND  mam.serialNum in (select serialNum from money_amount " + payAmountWhere(payAmountStrat, payAmountEnd) + ")");
    		    if(StringUtils.isNotBlank(printFlag) && "1".equals(printFlag)){
		        	sqlBuffer.append(" and printFlag=").append(printFlag).append(" ");
		        }else if(StringUtils.isNotBlank(printFlag) && "0".equals(printFlag)){
		        	sqlBuffer.append(" and ( printFlag=").append(printFlag).append(" or printFlag is null) ");
		        }
    			
	    		if("Y".equalsIgnoreCase(requestMap.get("isAccounted"))){
	    			sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
			    }else if("N".equalsIgnoreCase(requestMap.get("isAccounted"))){
			    	sqlBuffer.append(" and (isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YCX)
			    	  .append(" or isAsAccount is null)");
			    }else if("C".equalsIgnoreCase(requestMap.get("isAccounted"))){
			    	sqlBuffer.append(" and isAsAccount = ").append(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
			    }
    		    sqlBuffer.append(" ) tb where tmp.orderNum = tb.orderNum) payment ").append(" order by ").append(orderBy);
    			if(StringUtils.isNotBlank(payerName)){
//    				sqlBuffer.append(" where payment")
    			}
	    return  productorderDao.findBySql(sqlBuffer.toString(),Map.class);
    	}
    }
    
    /**
	 * 
	 * 获取订单收款、切位收款、签证押金收款、签证订单收款未进行收款确认操作的条数 
	 * @author haiming.zhao
	 * @param type 1:订单收款 ,2:切位收款,3:签证押金收款，4:签证订单收款
	 * @return Integer
	 * */
    public Integer getCountForOrderListDZ(Integer type){
    	StringBuffer sql = new StringBuffer();
    	List<Object> list;
    	Long companyId = UserUtils.getUser().getCompany().getId();
       try{	
    	if(type != null){
    		if(1==type){
    			sql.append("SELECT COUNT(u.id) from (SELECT tb1.id id FROM ( SELECT pro.orderNum FROM travelactivity activity, activitygroup agp, productorder")
    			.append(" pro WHERE activity.id = pro.productId AND agp.id = pro.productGroupId AND agp.srcActivityId = pro.productId ")
    			.append("AND pro.delFlag = 0 AND activity.proCompany =")
    			.append(companyId)
    			.append(" ) tmp,(SELECT id, orderNum, orderType, isAsAccount FROM orderpay a WHERE a.orderNum IS NOT NULL AND ( a.isAsAccount = 99 OR a.isAsAccount = 0 OR a.isAsAccount IS NULL )")
    			.append(") tb1 WHERE tmp.orderNum = tb1.orderNum")
    			.append(" UNION ")
    			.append(" SELECT op.id id FROM activity_airticket aa, ( SELECT c.id id, orderNum, orderType, isAsAccount FROM orderpay c WHERE ")
    			.append("c.orderNum IS NOT NULL AND ( c.isAsAccount = 0 OR c.isAsAccount IS NULL )) op,")
    			.append(" airticket_order ao WHERE ao.airticket_id = aa.id AND aa.proCompany = " ).append(companyId)
    			.append(" AND aa.delflag = 0 AND ao.order_no = op.orderNum");
    			if(companyId !=68){
    				sql.append(" UNION ").append("SELECT tb2.id id FROM ( SELECT pro.orderNum FROM activity_island activity, activity_island_group agp, island_order pro WHERE ")
    				.append("activity.uuid = pro.activity_island_uuid AND agp.uuid = pro.activity_island_group_uuid AND agp.activity_island_uuid = pro.activity_island_uuid ")
    				.append("AND pro.delFlag = 0 AND pro.orderStatus IN (1, 2) AND activity.wholesaler_id = ")
    				.append(companyId)
    				.append( " ) tmp, ( SELECT pio.id, pio.orderNum FROM pay_island_order pio ")
    				.append("WHERE pio.orderNum IS NOT NULL AND ( pio.isAsAccount = 0 OR pio.isAsAccount IS NULL )) tb2 WHERE tmp.orderNum = tb2.orderNum")
    				.append(" UNION ")
    				.append("SELECT tba.id id FROM ( SELECT pro.orderNum FROM activity_hotel activity, activity_hotel_group agp, hotel_order pro WHERE activity.uuid = " )
    				.append("pro.activity_hotel_uuid AND agp.uuid = pro.activity_hotel_group_uuid AND agp.activity_hotel_uuid = pro.activity_hotel_uuid AND pro.delFlag = 0 " )
    				.append("AND pro.orderStatus IN (1, 2) AND activity.wholesaler_id = " )
    				.append(companyId)
    				.append(" ) tmp, ( SELECT pho.id, pho.orderNum FROM pay_hotel_order pho WHERE pho.orderNum IS NOT NULL AND (pho.isAsAccount = 0 OR pho.isAsAccount IS NULL)) " )
    				.append("tba WHERE tmp.orderNum = tba.orderNum");
    			}
    			sql.append(") u");
    		}else if(2==type){
    			sql.append("SELECT count(id) FROM ( SELECT aro.id id, aro.confirm FROM travelactivity activity, activitygroup agp, activityreserveorder aro, sys_user sysuser, ")
    				.append("agentinfo ai WHERE activity.id = aro.srcActivityId AND agp.id = aro.activityGroupId AND agp.srcActivityId = aro.srcActivityId AND sysuser.id = aro.saleId ")
    				.append("AND ai.id = aro.agentId AND activity.proCompany = ")
    				.append(companyId)
    				.append(" AND aro.reserveType = 0 UNION SELECT aro.id id, aro.confirm FROM activity_airticket aat, ")
    				.append("activityreserveorder aro, activitygroup agp, sys_user sysuser, agentinfo ai WHERE aat.id = aro.srcActivityId AND aro.activityGroupId = agp.id AND ")
    				.append("sysuser.id = aro.saleId AND ai.id = aro.agentId AND aat.proCompany = ")
    				.append(companyId)
    				.append(" AND aro.reserveType = 1 ) tmp WHERE 1 = 1 AND ( tmp.confirm != 1 OR " )
    				.append("tmp.confirm IS NULL )");
    		}else if(3==type){
    			sql.append("SELECT count(1) FROM ( SELECT t.id travelerId, vo.order_no orderNum, v.total_deposit " )
    				.append("FROM visa v, visa_order vo, visa_products vp, traveler t WHERE v.traveler_id = t.id  " )
    				.append("AND vo.visa_product_id = vp.id AND t.orderId = vo.id AND t.order_type = 6 AND vp.proCompanyId = ")
    				.append(companyId)
    				.append(" ) a, orderpay b, money_amount ma , ( SELECT ma.serialNum, ma.uid " )
    				.append("FROM money_amount ma, currency c, orderpay op WHERE ma.currencyId = c.currency_id AND ma.moneyType " )
    				.append("IN (16, 101) AND ma.orderType = 6 AND ma.businessType = 2 AND op.moneySerialNum = ma.serialNum GROUP BY ma.serialNum, ma.uid ) d WHERE " )
    				.append("a.orderNum = b.orderNum AND b.moneySerialNum = d.serialNum AND a.travelerId = d.uid AND a.total_deposit = ma.serialNum AND ( b.isAsAccount =99 " )
    				.append("OR b.isAsAccount IS NULL )");
    		}else if(4==type){
    			sql.append("SELECT count(1) FROM ( SELECT o.isAsAccount, o.id, o.payType, o.orderPaySerialNum FROM ( SELECT id, orderNum, orderType, isAsAccount, " )
    				.append("orderPaySerialNum, payType, payPriceType, delFlag, createDate, printFlag, printTime, createBy FROM orderpay a WHERE NOT EXISTS ( SELECT b.id " )
    				.append("FROM orderpay b WHERE a.id = b.id AND ( b.isAsAccount = 1 OR b.isAsAccount = 2 OR b.isAsAccount = 0 )) and a.orderType=6) o LEFT JOIN visa_order vo ON " )
    				.append("o.orderNum = vo.order_no LEFT JOIN visa_products vp ON vo.visa_product_id = vp.id LEFT JOIN sys_user su ON o.createBy = su.id WHERE o.orderType = 6 " )
    				.append("AND o.payPriceType != 16 AND o.delFlag = 0 AND su.companyId = ")
    				.append(companyId)
    				.append(" AND vo.total_money IS NOT NULL AND isAsAccount != 1 GROUP BY o.orderPaySerialNum, ")
    				.append("o.isAsAccount, o.payType ) aaa");
    		}
    	}
    	 list =  orderpayDao.findBySql(sql.toString());
       }catch(Exception e){
    	  e.printStackTrace();
    	   return 0;
       } 
    	return Integer.valueOf(list.get(0).toString());
    }
    private boolean isInputMoney(String start, String end) {
    	if(StringUtils.isNotBlank(start) || StringUtils.isNotBlank(end)) {
    		return true;
    	}
    	return false;
    }
    
    private String payAmountWhere(String start, String end) {
    	StringBuffer sb = new StringBuffer(" where 1 =1 ");
    	if(StringUtils.isNotBlank(start)) {
    		sb.append(" and amount >= " + start);
    	}
    	if(StringUtils.isNotBlank(end)) {
    		sb.append(" and amount <= " + end);
    	}
    	return sb.toString();
    }
    /**
     * 设置查询订单sql语句
     * @param listIds
     * @param where
     * @return
     */
    private String getOrderSql(List<Integer> listIds, String where, String orderStatus) {
    	boolean b = false;
    	if(StringUtils.isNotBlank(orderStatus) && Context.ORDER_TYPE_ALL != Integer.parseInt(orderStatus)){
    		b = true;
    	}
    	//订单,销售取productorder表中的salerId，为了减少上层代码修改，使用别名orderSalerId。
    	StringBuilder sql = new StringBuilder("");
    	sql.append("SELECT activity.activitySerNum, activity.acitivityName, activity.id activityId, ")
    				.append("activity.payMode, activity.remainDays, activity.proCompany, users.name AS activityCreateUserName, ")
    				.append("agp.groupOpenDate, agp.groupCloseDate, agp.groupCode, agp.open_date_file AS open_date_file, ")
    				.append("agp.id gruopId, agp.planPosition, agp.freePosition, agp.remarks, agp.lockStatus settleLockStatus, ")
    				.append("pro.id, pro.orderTime, pro.createBy, pro.orderNum, pro.salerId AS orderSalerId, pro.orderPersonName, pro.orderPersonPhoneNum, ")
    				.append("pro.payStatus, pro.orderCompanyName,  pro.orderPersonNum, pro.confirmationFileId,")
    				.append("pro.front_money, pro.total_money,  pro.payed_money, pro.accounted_money, ")
//    				.append("totalOuter.moneyStr AS totalMoney, payedOuter.moneyStr AS payedMoney, accountedOuter.moneyStr AS accountedMoney, ")
    				.append("pro.placeHolderType, pro.activationDate, ")
    				.append("pro.orderCompany, pro.payMode proPayMode, pro.lockStatus,")
    				.append("pro.remainDays proRemainDays, pro.cancel_description AS cancelDescription, pro.paymentType, ")
    				.append("limits.id invoiceid, limits.createStatus createStatus, limits.verifyStatus verifyStatus, ")
                    //使用select子查询来进行totalmoney，payedmoney,accountedmoney多币种查询。这样可以使用索引，并且避免全表查询。yudong.xu
                    //订单应收金额多币种查询
                    .append("(SELECT GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount) ORDER BY mao.currencyId SEPARATOR '+') FROM money_amount mao")
                    .append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id WHERE mao.moneyType =").append(Context.MONEY_TYPE_YSH)
                    .append( b ? " AND mao.orderType = " + orderStatus + " " : " ")
                    .append(" AND mao.businessType = ").append(Context.MONEY_BUSINESSTYPE_ORDER)
                    .append(" AND mao.serialNum=pro.total_money GROUP BY mao.serialNum) AS totalMoney,")
                    //订单实收金额多币种查询
                    .append("(SELECT GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount) ORDER BY mao.currencyId SEPARATOR '+') FROM money_amount mao")
                    .append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id WHERE mao.moneyType =").append(Context.MONEY_TYPE_YS)
                    .append( b ? " AND mao.orderType = " + orderStatus + " " : " ")
                    .append(" AND mao.businessType = ").append(Context.MONEY_BUSINESSTYPE_ORDER)
                    .append(" AND mao.serialNum=pro.payed_money GROUP BY mao.serialNum) AS payedMoney,")
                    //订单已达账金额多币种查询
                    .append("(SELECT GROUP_CONCAT(CONCAT(c.currency_mark,' ',mao.amount) ORDER BY mao.currencyId SEPARATOR '+') FROM money_amount mao")
                    .append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id WHERE mao.moneyType =").append(Context.MONEY_TYPE_DZ)
                    .append( b ? " AND mao.orderType = " + orderStatus + " " : " ")
                    .append(" AND mao.businessType = ").append(Context.MONEY_BUSINESSTYPE_ORDER)
                    .append(" AND mao.serialNum=pro.accounted_money GROUP BY mao.serialNum) AS accountedMoney,")
                    //订单应收金额转换成人民币,其他金额转人民币暂时无需求，未写。
                    .append("(SELECT FORMAT(SUM(mao.amount*IFNULL(mao.exchangerate,0)),2) FROM money_amount mao WHERE mao.moneyType =")
                    .append(Context.MONEY_TYPE_YSH)
                    .append( b ? " AND mao.orderType = " + orderStatus + " " : " ")
                    .append(" AND mao.businessType = ").append(Context.MONEY_BUSINESSTYPE_ORDER)
                    .append(" AND mao.serialNum=pro.total_money) AS totalMoneyRMB ")
    			.append("FROM travelactivity activity, ")
    				.append("activitygroup agp, ")
    				.append("sys_user users, ")
    				.append("productorder pro ")
    			.append("LEFT JOIN orderinvoice limits ON limits.orderId = pro.id ")
    				.append("AND limits.id = ( ")
    					.append("SELECT MAX(id) ")
    					.append("FROM orderinvoice ")
    					.append("GROUP BY orderId ")
    					.append("HAVING pro.id = orderId ")
    					.append(") ")
    			.append("WHERE pro.delFlag = '" + ProductOrderCommon.DEL_FLAG_NORMAL + "' ")
    				.append("AND activity.id = pro.productId ")
    				.append("AND activity.createBy = users.id ")
    				.append(where);
    	
    	
    	// 判断订单支付类型是否为空，如果不为空则加入判断条件
    	String statusSql = "";
    	if(CollectionUtils.isNotEmpty(listIds)) {
    		statusSql += " AND pro.payStatus in(";
    		int size = listIds.size();
            while(size>0) {
                size--;
                statusSql += listIds.get(size) + ",";
            }
            statusSql = statusSql.substring(0,statusSql.length()-1);
            statusSql += ")";
    	}
    	sql.append(statusSql);
    	sql.append(" AND agp.id = pro.productGroupId AND agp.srcActivityId = pro.productId ");
    	return sql.toString();
    }
    
    /**
     * 查询订单列表
     * @param page
     * @param sql
     * @return
     */
    private Page<Map<Object, Object>> getOrderList(Page<Map<Object, Object>> page, String sql) {
    	Page<Map<Object, Object>> pageMap = productorderDao.findPageBySql(page, sql, Map.class);
        if(pageMap != null) {
        	for(Map<Object, Object> m : pageMap.getList()) {
        		Object orderSalerId = m.get("orderSalerId");
        		if(orderSalerId!=null) {
	        		m.put("orderSalerId", UserUtils.getUser(StringUtils.toLong(orderSalerId)).getName());
        		}else{
        			m.put("orderSalerId","");
        		}
        		
        		//详情金额设置为千分位表示S----- modify by wangyang 2016.8.17
        		//订单总额
        		String totalMoney = (String) m.get("totalMoney");
        		setThousandReg(m, totalMoney, "totalMoney");
        		//已收金额
        		String payedMoney = (String) m.get("payedMoney");
        		setThousandReg(m, payedMoney, "payedMoney");
        		//到账金额
				String accountedMoney = (String) m.get("accountedMoney");
				setThousandReg(m, accountedMoney, "accountedMoney");
				//详情金额设置为千分位表示E----- modify by wangyang 2016.8.17
        	}
        }
        return pageMap;
    }
    
    /**
     * 订单总额、已收金额、到账金额设置为千分位格式
     * @author wangyang
     * @date 2016.8.19
     * */
    private void setThousandReg(Map<Object, Object> map, String moneyStr, String type) {
    	
    	if (StringUtils.isNotBlank(moneyStr)) {
			if (moneyStr.indexOf("+") == -1) { //单币种情况
				String mark = moneyStr.split(" ")[0];
    			String number = moneyStr.split(" ")[1];
    			map.put(type, mark + " " + MoneyNumberFormat.getThousandsByRegex(number, 2));
			} else { //多币种情况
				String money = "";
				String[] prices = moneyStr.split("\\+");
				for (String price : prices) {
					String mark = price.split(" ")[0];
        			String number = price.split(" ")[1];
        			money += mark + " " + MoneyNumberFormat.getThousandsByRegex(number, 2) + "+";
				}
				// 去掉末尾的“+”
				if (StringUtils.isNotBlank(money)) {
					money = money.substring(0, money.length() - 1);
					map.put(type, money);
				}
			}
		}
    }
    /**
     * 根据订单查询团期列表
     * @param listIds
     * @param page
     * @return
     */
    private Page<Map<Object, Object>> getGroupListByOrder(List<Integer> listIds, Page<Map<Object, Object>> page, String orderSql) {
    	StringBuffer sql = new StringBuffer("");
    	orderSql = "SELECT distinct productGroupId " + orderSql.substring(orderSql.indexOf("FROM"));
    	orderSql = orderSql.split("LEFT JOIN")[0] + orderSql.substring(orderSql.lastIndexOf("WHERE"));
    	
    	sql.append("SELECT ")
				.append("agp.id, agp.groupCode groupCode, activity.acitivityName, activity.id activityId, activity.createBy createBy, agp.groupOpenDate, ")
				.append("agp.groupCloseDate, agp.planPosition, agp.freePosition, NOW() nowDate,agp.remarks, agp.open_date_file," +
						"activity.activityDuration, activity.activityStatus ")
			.append("FROM activitygroup agp, travelactivity activity ")
			.append("WHERE agp.srcActivityId = activity.id ")
			.append("AND agp.id in ")
			.append("(")
			.append(orderSql)
			.append(") ");
        Page<Map<Object, Object>> pageMap = productorderDao.findBySql(page, sql.toString(), Map.class);
        return pageMap;
    }
    
	private Page<Map<Object, Object>> getByPrePayStatus(
			Page<Map<Object, Object>> page, String where) {

		String sql = " select "
				+ " activity.activitySerNum,"
				+ " activity.acitivityName,"
				+ " activity.id activityId,"
				+ " activity.payMode,"
				+ " activity.remainDays,"
				+ " activity.proCompany,"
				+ " activity.createBy as activityCreateBy,"
				+ " agp.groupOpenDate,"
				+ " agp.groupCloseDate,"
				+ " agp.groupCode,"
				+ " agp.id gruopId,"
				+ " agp.planPosition,"
	            + " agp.freePosition,"
	            + " agp.remarks,"
				+ "  pro.createBy,"
				+ "  pro.salerId,"
				+ "  pro.salerName,"
				+ "  pro.orderNum,"
				+ "  pro.orderSalerId,"
				+ "  pro.orderPersonName,"
				+ "  pro.orderPersonPhoneNum,"
				+ "  pro.payStatus,"
				+ "  pro.orderCompanyName,"
				+ "  pro.total_money AS totalMoney,"
				+ "  pro.orderPersonNum,"
				+ "  pro.orderType,"
				+ "  pro.id,"
				+ "  pro.orderTime,"
				+ "  pro.activationDate,"
				+ "  pro.placeHolderType,"
				+ "  pro.accounted_money AS accountedMoney,"
				+ "  pro.orderCompany,"
				+ "  pro.payMode proPayMode,"
				+ "  pro.remainDays proRemainDays,"
				+ "  pro.cancel_description as cancelDescription "
				+ " from travelactivity activity,activitygroup agp,preproductorder pro "
				+ (where.indexOf("activitytargetarea.") > -1 ? ",activitytargetarea "
						: "")
				+ " where pro.delFlag='"
				+ ProductOrderCommon.DEL_FLAG_NORMAL
				+ "'and activity.id = pro.productId  "
				+ where
				+ " and agp.id=pro.productGroupId and agp.srcActivityId=pro.productId";

		Page<Map<Object, Object>> pageMap = preproductorderDao.findBySql(page,
				sql, Map.class);
		if (pageMap != null) {
			for (Map<Object, Object> m : pageMap.getList()) {
				Object orderSalerId = m.get("orderSalerId");
				if (orderSalerId != null) {
					m.put("orderSalerId", UserUtils.getUser(
							StringUtils.toLong(orderSalerId)).getName());
				} else {
					m.put("orderSalerId", "");
				}
			}
		}
		return pageMap;
	}
	
	/**
	 * 查询签证的订单记录
	 * @param page
	 * @param orderType
	 * @param mapRequest
	 * @param column
	 * @param commonSQL
	 * @return
	 */
	private Page<Map<Object, Object>> getQZOrderRecord(Page<Map<Object, Object>> page, String orderType, 
			Map<String, String> mapRequest, StringBuffer column, String commonSQL) {
		String orderTimeBegin = mapRequest.get("orderTimeBegin");//下单时间开始
        String orderTimeEnd = mapRequest.get("orderTimeEnd");//下单时间结束
        String groupCode = mapRequest.get("groupCode");//团号
        String orderNum = mapRequest.get("orderNum");//订单编号
        String operator = mapRequest.get("jdUserId");//计调
        String agentId = mapRequest.get("agentId");//渠道
        String saler = mapRequest.get("saler");//销售
        String deptId = mapRequest.get("queryDepartmentId");//部门
        String orderPersonId = mapRequest.get("orderPersonId");//下单人
        String groupOpenDateBegin = mapRequest.get("groupOpenDateBegin");//出团日期开始
        String groupOpenDateEnd = mapRequest.get("groupOpenDateEnd");//出团日期结束
        String payStatus = mapRequest.get("payStatus");//付款状态
        String accountStatus = mapRequest.get("accountStatus");//达账状态
        String isSeizedConfirmed = mapRequest.get("isSeizedConfirmed");//是否占位确认
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        //页面option参数
        String option = mapRequest.get("option");
        //签证国家
        String sysCountryId = mapRequest.get("sysCountryId");
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        StringBuffer sql = new StringBuffer();
        StringBuffer whereSQL = new StringBuffer();
		//团号
		if(StringUtils.isNotBlank(groupCode)){
            if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
                whereSQL.append(" and o.group_code like '%").append(groupCode).append("%'");
            }else{
                whereSQL.append(" and p.groupCode like '%").append(groupCode).append("%'");
            }
		}
		//订单编号
		if(StringUtils.isNotBlank(orderNum)){
			whereSQL.append(" and o.order_no like '%").append(orderNum).append("%'");
		}
		//开始下单时间
		if(StringUtils.isNotBlank(orderTimeBegin)){
			whereSQL.append(" and o.create_date >= '").append(orderTimeBegin).append(" 00:00:00'");
		}else{
			whereSQL.append(" and o.create_date >= '").append(Context.DEFAULT_BEGIN_TIME).append("'");
		}
		//结束下单时间
		if(StringUtils.isNotBlank(orderTimeEnd)){
			whereSQL.append(" and o.create_date <= '").append(orderTimeEnd).append(" 23:59:59'");
		}else{
			whereSQL.append(" and o.create_date <= '").append(Context.DEFAULT_END_TIME).append("'");
		}
		//计调人员
		if(StringUtils.isNotBlank(operator)){
			whereSQL.append(" and p.createBy = ").append(operator);
		}
		//渠道
		if(StringUtils.isNotBlank(agentId)){
			whereSQL.append(" and o.agentinfo_id =").append(agentId);
		}
		//销售
		if(StringUtils.isNotBlank(saler)){
			whereSQL.append(" and o.salerId = ").append(saler);
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			whereSQL.append(" and p.deptId = ").append(deptId);
		}
		if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(UserUtils.getUser().getUserType())){
			whereSQL.append(" and p.proCompanyId=").append(userCompanyId)
			        .append(" AND o.payStatus IN (").append(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF))
			        .append(",").append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW))
			        .append(",").append(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF))
			        .append(",").append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
			//bug16678 账龄查询 不查已收全款的
			 if("detail".equals(mapRequest.get("option"))) {
				 whereSQL.append(",").append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
			}
			 whereSQL.append(")");
		}
		//下单人
		if(StringUtils.isNotBlank(orderPersonId)){
			whereSQL.append(" and o.create_by = ").append(orderPersonId);
		}
		//签证国家
		if(StringUtils.isNotBlank(sysCountryId)) {
			whereSQL.append(" and p.sysCountryId = ").append(sysCountryId);
		}
		
        //付款状态
        if("2".equals(payStatus)){
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.payed_money ) = 0 ");
        }
        if("4".equals(payStatus)){
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.payed_money ) > 0 ");
        }

        //达账状态
        if("2".equals(accountStatus)){
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.accounted_money ) = 0 ");
        }
        if("4".equals(accountStatus)){
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.accounted_money ) > 0 ");
        }
        sql.append("SELECT ").append(column.toString()).append(" FROM ( SELECT o.id, o.order_no AS orderNum,");
        if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
            sql.append(" o.group_code AS groupCode, ");
        }else{
            sql.append(" p.groupCode, ");
        }
        sql.append(" p.productName AS acitivityName,o.create_by AS createUserId,o.create_date AS orderTime,")
		   .append("o.travel_num orderPersonNum, p.createDate AS groupOpenDate, p.updateDate AS groupCloseDate,o.payStatus AS payStatus,")
		   .append("o.total_money as totalMoney,o.payed_money as payedMoney,o.accounted_money as accountedMoney,6 AS orderType,")
		   .append("p.id as activityId, o.create_date AS createDate,o.update_date AS updateDate, ")
		   .append(" p.proCompanyId AS w_proCompany, p.createBy AS w_operator, o.agentinfo_id AS w_agent,")
		   .append(" o.salerId AS w_saler, p.deptId AS w_deptId, ' ' as orderCompanyName, p.sysCountryId, 0 AS isSeizedConfirmed ")
		   .append(" FROM visa_order o, visa_products p WHERE o.visa_product_id = p.id AND ")
		   .append(" o.del_flag = '").append(ProductOrderCommon.DEL_FLAG_NORMAL).append("' AND o.visa_order_status <> 100 ")
		   .append(" AND o.mainOrderId is null ");//签证子订单不在交易明细显示，Bug:8988
        sql.append(whereSQL.toString()).append(" ) t1 WHERE 1=1 ");
        
        //占位确认
        if("1".equals(isSeizedConfirmed)){
            sql.append(" AND t1.isSeizedConfirmed = 0 ");
        }else if("2".equals(isSeizedConfirmed)){
            sql.append(" AND t1.isSeizedConfirmed = 1 ");
        }
        //出团日期开始
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
            sql.append(" AND t1.groupOpenDate IS NOT NULL ");
        }
        //出团日期结束
        if (StringUtils.isNotBlank(groupOpenDateEnd)) {
            sql.append(" AND t1.groupOpenDate IS NOT NULL ");
        }
        if(StringUtils.isNotBlank(option)) {
			if(option.equals("detail")) {
				return productorderDao.findPageBySqlEx(page, sql.toString(),mapRequest,moneyAmountService);
			}else {
				return productorderDao.findBySql(page, sql.toString(),Map.class);
			}
		}
        return null;
	}
	
	/**
	 * 查询机票的订单记录
	 * @param page
	 * @param orderType
	 * @param mapRequest
	 * @param column
	 * @param commonSQL
	 * @return
	 */
	private Page<Map<Object, Object>> getJPOrderRecord(Page<Map<Object, Object>> page, String orderType, 
			Map<String, String> mapRequest, StringBuffer column, String commonSQL) {
		String orderTimeBegin = mapRequest.get("orderTimeBegin");//下单时间开始
        String orderTimeEnd = mapRequest.get("orderTimeEnd");//下单时间结束
        String groupCode = mapRequest.get("groupCode");//团号
        String orderNum = mapRequest.get("orderNum");//订单编号
        String operator = mapRequest.get("jdUserId");//计调
        String agentId = mapRequest.get("agentId");//渠道
        String saler = mapRequest.get("saler");//销售
        String deptId = mapRequest.get("queryDepartmentId");//部门
        String orderPersonId = mapRequest.get("orderPersonId");//下单人
        String groupOpenDateBegin = mapRequest.get("groupOpenDateBegin");//出团日期开始
        String groupOpenDateEnd = mapRequest.get("groupOpenDateEnd");//出团日期结束
        String payStatus = mapRequest.get("payStatus");//付款状态
        String accountStatus = mapRequest.get("accountStatus");//达账状态
        String isSeizedConfirmed = mapRequest.get("isSeizedConfirmed");//是否已确认占位
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        //页面option参数
        String option = mapRequest.get("option");
		StringBuffer sql = new StringBuffer();
        StringBuffer whereSQL = new StringBuffer();
		//团号
		if(StringUtils.isNotBlank(groupCode)){
			whereSQL.append(" and p.group_code like '%").append(groupCode).append("%'");
		}
		//订单编号
		if(StringUtils.isNotBlank(orderNum)){
			whereSQL.append(" and o.order_no like '%").append(orderNum).append("%'");
		}
		//开始下单时间
		if(StringUtils.isNotBlank(orderTimeBegin)){
			whereSQL.append(" and o.create_date >= '").append(orderTimeBegin).append(" 00:00:00'");
		}else{
			whereSQL.append(" and o.create_date >= '").append(Context.DEFAULT_BEGIN_TIME).append("'");
		}
		//结束下单时间
		if(StringUtils.isNotBlank(orderTimeEnd)){
			whereSQL.append(" and o.create_date <= '").append(orderTimeEnd).append(" 23:59:59'");
		}else{
			whereSQL.append(" and o.create_date <= '").append(Context.DEFAULT_END_TIME).append("'");
		}
		//计调人员
		if(StringUtils.isNotBlank(operator)){
			whereSQL.append(" and p.createBy = ").append(operator);
		}
		//渠道
		if(StringUtils.isNotBlank(agentId)){
			whereSQL.append(" and o.agentinfo_id =").append(agentId);
		}
		//销售
		if(StringUtils.isNotBlank(saler)){
			whereSQL.append(" and o.salerId = ").append(saler);
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			whereSQL.append(" and p.deptId = ").append(deptId);
		}
		if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(UserUtils.getUser().getUserType())){
			whereSQL.append(" and p.proCompany=").append(userCompanyId);
		}
		//下单人
		if(StringUtils.isNotBlank(orderPersonId)){
			whereSQL.append(" and o.create_by = ").append(orderPersonId);
		}
        //付款状态
        if (StringUtils.isNotBlank(payStatus) && !payStatus.equals("0")) {
        	if(payStatus.equals("2"))
        		whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.payed_money ) = 0 ");
        	if(payStatus.equals("4"))
        		whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.payed_money ) > 0 ");
        }
        //达账状态
        if (StringUtils.isNotBlank(accountStatus) && !accountStatus.equals("0")) {
        	if(accountStatus.equals("2"))
        		whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.accounted_money ) = 0 ");
        	if(accountStatus.equals("4"))
        		whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.accounted_money ) > 0 ");
        }
        
		//确认占位
		if(StringUtils.isNotBlank(isSeizedConfirmed) && !"0".equals(isSeizedConfirmed)){
			if(isSeizedConfirmed.equals("1")){
				whereSQL.append(" AND (o.seized_confirmation_status IS NULL OR o.seized_confirmation_status = 0) ");
			}else if(isSeizedConfirmed.equals("2")){
				whereSQL.append(" AND o.seized_confirmation_status = 1 ")
						.append(" AND o.order_state <> 99 ");
			}
		}
        //出团日期开始
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
            whereSQL.append(" and p.outTicketTime >= '").append(groupOpenDateBegin).append(" 00:00:00' ");
        }
        //出团日期结束
        if (StringUtils.isNotBlank(groupOpenDateEnd)) {
            whereSQL.append(" and p.outTicketTime <= '").append(groupOpenDateEnd).append(" 23:59:59' ");
        }
		sql.append("SELECT ").append(column.toString()).append(" FROM ( SELECT o.id,");//
		sql.append("order_no AS orderNum,p.group_code groupCode,' ' acitivityName, o.create_by AS createUserId,o.create_date AS orderTime,")
		   .append("o.person_num orderPersonNum,p.outTicketTime AS groupOpenDate,p.updateDate AS groupCloseDate,o.order_state AS payStatus,")
		   .append("o.total_money as totalMoney,o.payed_money as payedMoney,o.accounted_money as accountedMoney,7 AS orderType, p.id AS activityId,")
		   .append("o.create_date AS createDate,o.update_date AS updateDate,")
		   .append(" p.proCompany AS w_proCompany, p.createBy AS w_operator,o.agentinfo_id AS w_agent, o.salerId AS w_saler,")
		   .append(" p.deptId AS w_deptId,' ' as orderCompanyName, ' ' sysCountryId, o.seized_confirmation_status AS isSeizedConfirmed FROM")
		   .append(" airticket_order o,activity_airticket p WHERE o.airticket_id = p.id AND o.del_flag = '")
		   .append(ProductOrderCommon.DEL_FLAG_NORMAL).append("' and o.order_state <> 111 ");
		sql.append(whereSQL.toString()).append(" ) t1 ");

		if(StringUtils.isNotBlank(option)) {
			if(option.equals("detail")) {
				return productorderDao.findPageBySqlEx(page, sql.toString(),mapRequest,moneyAmountService);
			}else {
				return productorderDao.findBySql(page, sql.toString(),Map.class);
			}
		}
        return null;
	}
	
	/**
	 * 查询单团的订单记录
	 * @param page
	 * @param orderType
	 * @param mapRequest
	 * @return
	 */
	private Page<Map<Object, Object>> getDTOrderRecord(Page<Map<Object, Object>> page, String orderType, 
			Map<String, String> mapRequest, StringBuffer column, String commonSQL) {
		String orderTimeBegin = mapRequest.get("orderTimeBegin");//下单时间开始
        String orderTimeEnd = mapRequest.get("orderTimeEnd");//下单时间结束
        String groupCode = mapRequest.get("groupCode");//团号
        String orderNum = mapRequest.get("orderNum");//订单编号
        String operator = mapRequest.get("jdUserId");//计调
        String agentId = mapRequest.get("agentId");//渠道
        String saler = mapRequest.get("saler");//销售
        String deptId = mapRequest.get("queryDepartmentId");//部门
        String orderPersonId = mapRequest.get("orderPersonId");//下单人
        String groupOpenDateBegin = mapRequest.get("groupOpenDateBegin");//出团日期开始
        String groupOpenDateEnd = mapRequest.get("groupOpenDateEnd");//出团日期结束
        String payStatus = mapRequest.get("payStatus");//付款状态
        String accountStatus = mapRequest.get("accountStatus");//达账状态
        String isSeizedConfirmed = mapRequest.get("isSeizedConfirmed");//是否确认占位
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        //页面option参数
        String option = mapRequest.get("option");
        StringBuffer sql = new StringBuffer();
        StringBuffer whereSQL = new StringBuffer();
        int whereOrderType = 0;
		if(StringUtils.isNotBlank(orderType) && StringUtils.isNotEmpty(orderType)){
			whereOrderType = Integer.parseInt(orderType);
		}
        if(Context.ORDER_TYPE_ALL != whereOrderType){
			whereSQL.append(" and o.orderStatus=").append(whereOrderType);
		}
		//团号
		if(StringUtils.isNotBlank(groupCode)){
			whereSQL.append(" and g.groupCode like '%").append(groupCode).append("%'");
		}
		//订单编号
		if(StringUtils.isNotBlank(orderNum)){
			whereSQL.append(" and o.orderNum like '%").append(orderNum).append("%'");
		}
		//开始下单时间
		if(StringUtils.isNotBlank(orderTimeBegin)){
			whereSQL.append(" and o.orderTime >= '").append(orderTimeBegin).append(" 00:00:00'");
		}else{
			whereSQL.append(" and o.orderTime >= '").append(Context.DEFAULT_BEGIN_TIME).append("'");
		}
		//结束下单时间
		if(StringUtils.isNotBlank(orderTimeEnd)){
			whereSQL.append(" and o.orderTime <= '").append(orderTimeEnd).append(" 23:59:59'");
		}else{
			whereSQL.append(" and o.orderTime <= '").append(Context.DEFAULT_END_TIME).append("'");
		}
		//计调人员
		if(StringUtils.isNotBlank(operator)){
			whereSQL.append(" and g.createBy = ").append(operator);
		}
		//渠道
		if(StringUtils.isNotBlank(agentId)){
			whereSQL.append(" and o.orderCompany =").append(agentId);
		}
		//销售
		if(StringUtils.isNotBlank(saler)){
			whereSQL.append(" and o.salerId = ").append(saler);
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			whereSQL.append(" and p.deptId = ").append(deptId);
		}
		whereSQL.append(" AND o.payStatus IN (");
		if("detail".equals(mapRequest.get("option"))) {
    		whereSQL.append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF)).append(",");
    	}
		whereSQL.append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ)).append(",")
		        .append(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF)).append(",")
		        .append(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF)).append(",")
		        .append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW)).append(")");
		if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(UserUtils.getUser().getUserType())){
			whereSQL.append(" and p.proCompany=").append(userCompanyId);
		}
		//下单人
		if(StringUtils.isNotBlank(orderPersonId)){
			whereSQL.append(" and o.createBy = ").append(orderPersonId);
		}
		//出团日期开始
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
        	whereSQL.append(" and g.groupOpenDate >= '").append(groupOpenDateBegin).append(" 00:00:00'");
        }
        //出团日期结束
        if (StringUtils.isNotBlank(groupOpenDateEnd)) {
        	whereSQL.append(" and g.groupOpenDate <= '").append(groupOpenDateEnd).append(" 23:59:59'");
        }
        //付款状态
        if("2".equals(payStatus)) {
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.payed_money ) = 0 ");
        }
        if("4".equals(payStatus)){
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.payed_money ) > 0 ");
        }
        //达账状态
        if("2".equals(accountStatus)) {
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.accounted_money ) = 0 ");
        }
        if("4".equals(accountStatus)) {
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = o.accounted_money ) > 0 ");
        }

		//占位确认
		if(StringUtils.isNotBlank(isSeizedConfirmed) && !"0".equals(isSeizedConfirmed)){
			if(isSeizedConfirmed.equals("1")){
				whereSQL.append(" AND (o.seized_confirmation_status IS NULL OR o.seized_confirmation_status = 0) ");
			}else if(isSeizedConfirmed.equals("2")){
				whereSQL.append(" AND o.seized_confirmation_status = 1 ");
			}
		}
		
		sql.append("SELECT ").append(column).append(" FROM ( SELECT o.id,");//
		sql.append("o.orderNum, g.groupCode, p.acitivityName as acitivityName, o.createBy AS createUserId, o.orderTime, o.orderPersonNum, g.groupOpenDate,")
		   .append("g.groupCloseDate, o.payStatus, o.total_money as totalMoney, o.payed_money as payedMoney, o.accounted_money ")
		   .append(" as accountedMoney, o.orderStatus AS orderType, p.id AS activityId,")
		   .append(" o.createDate, o.updateDate, p.proCompany AS w_proCompany, g.createBy AS w_operator, ")
		   .append(" o.orderCompany AS w_agent,o.salerId as w_saler ,p.deptId AS w_deptId, o.orderCompanyName, ' ' sysCountryId,")
           .append(" o.seized_confirmation_status AS isSeizedConfirmed  FROM productorder o,")
		   .append("activitygroup g,travelactivity p WHERE o.delFlag = '")
		   .append(ProductOrderCommon.DEL_FLAG_NORMAL).append("'")
		   .append(" AND o.productId = p.id AND o.productGroupId = g.id AND p.id = g.srcActivityId");
		sql.append(whereSQL.toString()).append(" ) t1 ");
		if(StringUtils.isNotBlank(option)) {
			if(option.equals("detail")) {
				return productorderDao.findPageBySqlEx(page, sql.toString(),mapRequest,moneyAmountService);
			}else {
				return productorderDao.findBySql(page, sql.toString(),Map.class);
			}
		}
		return null;
	}

	/**
	 * 查询所有订单数据信息
	 * @param page
	 * @param orderType
	 * @param mapRequest
	 * @param commonSQL
	 * @return
	 */
	private Page<Map<Object, Object>> getAllOrderRecord(Page<Map<Object, Object>> page, String orderType, 
			Map<String, String> mapRequest, String commonSQL) {
		String orderTimeBegin = mapRequest.get("orderTimeBegin");//下单时间开始
        String orderTimeEnd = mapRequest.get("orderTimeEnd");//下单时间结束
        String groupCode = mapRequest.get("groupCode");//团号
        String orderNum = mapRequest.get("orderNum");//订单编号
        String operator = mapRequest.get("jdUserId");//计调
        String agentId = mapRequest.get("agentId");//渠道
        String saler = mapRequest.get("saler");//销售
        String deptId = mapRequest.get("queryDepartmentId");//部门
        String orderPersonId = mapRequest.get("orderPersonId");//下单人
        String groupOpenDateBegin = mapRequest.get("groupOpenDateBegin");//出团时间开始
        String groupOpenDateEnd = mapRequest.get("groupOpenDateEnd");//出团时间结束
        String payStatus = mapRequest.get("payStatus");//付款状态
        String accountStatus = mapRequest.get("accountStatus");//达账状态
        String isSeizedConfirmed = mapRequest.get("isSeizedConfirmed");//是否已确认占位
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        String paymentType = mapRequest.get("paymentType");
        String orderBy = mapRequest.get("orderBy");//排序
        //页面option参数
        String option = mapRequest.get("option");
		StringBuffer str = new StringBuffer();
		str.append(" SELECT id, orderNum, groupCode, acitivityName, createUserId, orderTime,")
		   .append(" orderPersonNum, groupOpenDate, groupCloseDate, payStatus, totalMoney, payedMoney,")
		   .append(" accountedMoney, orderType, activityId, createDate, updateDate, w_proCompany, w_operator,")
		   .append(" w_agent, w_saler, w_deptId, orderCompanyName, isSeizedConfirmed FROM ( ")
		   .append(" SELECT t1.id, t1.orderNum, t1.groupCode, t1.acitivityName, t1.createUserId, t1.orderTime,")
		   .append(" t1.orderPersonNum, t1.groupOpenDate, t1.groupCloseDate, t1.payStatus, t1.totalMoney, t1.payedMoney,")
		   .append(" t1.accountedMoney, t1.orderType, t1.activityId, t1.createDate, t1.updateDate, t1.w_proCompany, t1.w_operator,")
		   .append(" t1.w_agent, t1.w_saler, t1.w_deptId, t1.orderCompanyName, t1.isSeizedConfirmed FROM ")
		   .append(" ( SELECT o.id, o.orderNum, g.groupCode, p.acitivityName, o.createBy AS createUserId, o.orderTime,")
		   .append(" o.orderPersonNum, g.groupOpenDate, g.groupCloseDate, o.payStatus, o.total_money AS totalMoney, o.payed_money AS payedMoney,")
		   .append(" o.accounted_money AS accountedMoney, o.orderStatus AS orderType, p.id AS activityId, o.createDate, o.updateDate, ")
		   .append(" p.proCompany AS w_proCompany, g.createBy AS w_operator, o.orderCompany AS w_agent, o.salerId AS w_saler,")
		   .append(" p.deptId AS w_deptId, o.orderCompanyName, o.seized_confirmation_status AS isSeizedConfirmed ")
		   .append(" FROM productorder o, activitygroup g, travelactivity p WHERE o.delFlag = '0' ")
		   .append(" AND o.productId = p.id AND o.productGroupId = g.id AND p.id = g.srcActivityId AND o.payStatus IN (");
		
		if("detail".equals(mapRequest.get("option"))) {
		    str.append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF)).append(",");
		}
		str.append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ)).append(",")
		   .append(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF)).append(",")
		   .append(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF)).append(",")
		   .append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW)).append(") AND p.proCompany = " + userCompanyId + ") t1 ")
		   .append(" UNION ")
		   .append("SELECT t1.id, t1.orderNum, t1.groupCode, t1.acitivityName, t1.createUserId, t1.orderTime,")
		   .append(" t1.orderPersonNum, t1.groupOpenDate, t1.groupCloseDate, t1.payStatus, t1.totalMoney, t1.payedMoney,")
		   .append(" t1.accountedMoney, t1.orderType, t1.activityId, t1.createDate, t1.updateDate, t1.w_proCompany, t1.w_operator,")
		   .append(" t1.w_agent, t1.w_saler, t1.w_deptId, t1.orderCompanyName, t1.isSeizedConfirmed FROM ")
		   .append(" ( SELECT o.id, order_no AS orderNum, p.group_code AS groupCode, ' ' AS acitivityName, o.create_by as createUserId, ")
		   .append(" o.create_date AS orderTime, o.person_num AS orderPersonNum, p.createDate AS groupOpenDate, p.updateDate AS groupCloseDate,")
		   .append(" o.order_state AS payStatus, o.total_money AS totalMoney, o.payed_money AS payedMoney, ")
		   .append(" o.accounted_money AS accountedMoney, 7 AS orderType, p.id AS activityId, o.create_date AS createDate, ")
		   .append(" o.update_date AS updateDate, p.proCompany AS w_proCompany, p.createBy AS w_operator,")
		   .append(" o.agentinfo_id AS w_agent, o.salerId AS w_saler, p.deptId AS w_deptId,' ' as orderCompanyName, ")
		   .append(" o.seized_confirmation_status AS isSeizedConfirmed ")
		   .append(" FROM airticket_order o , ")
		   .append(" activity_airticket p WHERE o.airticket_id = p.id AND o.del_flag = '0' ")
		   .append("  and o.order_state <> 111 AND p.proCompany = " + userCompanyId + ") t1")
		   .append(" UNION ")
		   .append(" SELECT t1.id, t1.orderNum, t1.groupCode, t1.acitivityName, t1.createUserId, t1.orderTime,")
		   .append(" t1.orderPersonNum, t1.groupOpenDate, t1.groupCloseDate, t1.payStatus, t1.totalMoney, t1.payedMoney,")
		   .append(" t1.accountedMoney, t1.orderType, t1.activityId, t1.createDate, t1.updateDate, t1.w_proCompany,")
		   .append(" t1.w_operator, t1.w_agent, t1.w_saler, t1.w_deptId, t1.orderCompanyName, NULL AS isSeizedConfirmed FROM ")
		   .append(" ( SELECT o.id, o.order_no AS orderNum, ");
        // C460V3Plus  环球行使用订单表团号
        if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
            str.append(" o.group_code AS groupCode, p.productName AS acitivityName, o.create_by AS createUserId, ");
        }else {
            str.append(" p.groupCode, p.productName AS acitivityName, o.create_by AS createUserId, ");
        }
        str.append(" o.create_date AS orderTime, o.travel_num AS orderPersonNum, p.createDate AS groupOpenDate, p.updateDate AS groupCloseDate, ")
		   .append(" o.payStatus, o.total_money AS totalMoney, o.payed_money AS payedMoney, o.accounted_money AS accountedMoney,")
		   .append(" 6 AS orderType, p.id as activityId, o.create_date AS createDate, o.update_date AS updateDate,")
		   .append(" p.proCompanyId AS w_proCompany, p.createBy AS w_operator, o.agentinfo_id AS w_agent,")
		   .append(" o.salerId AS w_saler, p.deptId AS w_deptId, ' ' as orderCompanyName FROM visa_order o, ")
		   .append(" visa_products p WHERE o.visa_product_id = p.id AND o.del_flag = '").append(Context.DEL_FLAG_NORMAL).append("' ")
		   .append(" AND o.visa_order_status <> 100 ").append(" AND o.mainOrderId is null ")//签证子订单不在交易明细显示，Bug:8988
		   .append(" AND o.payStatus IN ('1','3','5') AND p.proCompanyId = " + userCompanyId + ") t1 ) AS tt where 1=1 ");
        int whereOrderType = 0;
		if(StringUtils.isNotBlank(orderType) && StringUtils.isNotEmpty(orderType)){
			whereOrderType = Integer.parseInt(orderType);
		}
        if(Context.ORDER_TYPE_ALL != whereOrderType){
			str.append(" and tt.orderType=").append(whereOrderType);
		}
		//团号
		if(StringUtils.isNotBlank(groupCode)){
			str.append(" and tt.groupCode like '%").append(groupCode).append("%'");
		}
		//订单编号
		if(StringUtils.isNotBlank(orderNum)){
			str.append(" and tt.orderNum like '%").append(orderNum).append("%'");
		}
		//开始下单时间
		if(StringUtils.isNotBlank(orderTimeBegin)){
			str.append(" and tt.orderTime >= '").append(orderTimeBegin).append(" 00:00:00'");
		}else{
			str.append(" and tt.orderTime >= '").append(Context.DEFAULT_BEGIN_TIME).append("'");
		}
		//结束下单时间
		if(StringUtils.isNotBlank(orderTimeEnd)){
			str.append(" and tt.orderTime <= '").append(orderTimeEnd).append(" 23:59:59'");
		}else{
			str.append(" and tt.orderTime <= '").append(Context.DEFAULT_END_TIME).append("'");
		}
		//计调人员
		if(StringUtils.isNotBlank(operator)){
			str.append(" and tt.w_operator = ").append(operator);
		}
		//渠道
		if(StringUtils.isNotBlank(agentId)){
			str.append(" and tt.w_agent =").append(agentId);
		}
		//销售
		if(StringUtils.isNotBlank(saler)){
			str.append(" and tt.w_saler = ").append(saler);
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			str.append(" and tt.w_deptId = ").append(deptId);
		}
		if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(UserUtils.getUser().getUserType())){
			str.append(" and tt.w_proCompany = ").append(userCompanyId);
		}
		//下单人
		if(StringUtils.isNotBlank(orderPersonId)){
			str.append(" and tt.createUserId = ").append(orderPersonId);
		}
		//出团日期
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
        	str.append(" and tt.groupOpenDate >= '").append(groupOpenDateBegin).append(" 00:00:00'");
        }
        //出团日期结束
        if (StringUtils.isNotBlank(groupOpenDateEnd)) {
        	str.append(" and tt.groupOpenDate <= '").append(groupOpenDateEnd).append(" 23:59:59'");
        }
        
        //付款状态
        if (StringUtils.isNotBlank(payStatus) && !payStatus.equals("0")) {
        	if(payStatus.equals("2"))
        		str.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = tt.payedMoney ) = 0 ");
        	if(payStatus.equals("4"))
        		str.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = tt.payedMoney ) > 0 ");
        }
        
        //达账状态
        if (StringUtils.isNotBlank(accountStatus) && !accountStatus.equals("0")) {
        	if(accountStatus.equals("2"))
        		str.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = tt.accountedMoney ) = 0 ");
        	if(accountStatus.equals("4"))
        		str.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = tt.accountedMoney ) > 0 ");
        }
        
		//占位确认
		if(StringUtils.isNotBlank(isSeizedConfirmed) && !"0".equals(isSeizedConfirmed)){
			if(isSeizedConfirmed.equals("1")){
				str.append(" and ( tt.isSeizedConfirmed = 0 or tt.isSeizedConfirmed is null ) ");
			}else if(isSeizedConfirmed.equals("2")){
				str.append(" and tt.isSeizedConfirmed = 1 ")
				   .append(" AND tt.payStatus IN( ")
				   .append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW)).append(",")
				   .append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ)).append(",")
				   .append(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF)).append(") ");
			}
		}
		//渠道结算方式
		if(StringUtils.isNotBlank(paymentType)){
			str.append(" and tt.w_agent in (select id from agentinfo where paymentType = " + paymentType +" ) ");
		}
		//------bug15459增加默认的创建时间排序---------
		if (StringUtils.isNotBlank(orderBy)) {
			str.append(" order by ").append(orderBy).append(" ");
		}
		
		if(StringUtils.isNotBlank(option)) {
			if(option.equals("detail")) {
				return productorderDao.findPageBySqlEx(page, str.toString(),mapRequest,moneyAmountService);
			}else {
				return productorderDao.findBySql(page, str.toString(),Map.class);
			}
		}
//		return productorderDao.findBySql(page, str.toString(),Map.class);
		return null;
	}
	/**
	 * 不分类别查询所有订单的信息（之前分单团，机票，签证）
	 * @param page         
	 * @param orderType    订单类型
	 * @param mapRequest   条件参数
	 * @return
	 */
	public Page<Map<Object, Object>> getOrderRecord(Page<Map<Object, Object>> page, String orderType,
											Map<String, String> mapRequest) {
		Page<Map<Object, Object>> pageMap = null;
		StringBuffer column = new StringBuffer();
		StringBuffer commonSQL = new StringBuffer();
		column.append("t1.id,t1.orderNum,t1.groupCode,t1.acitivityName,t1.createUserId,t1.orderTime,t1.orderPersonNum,")
		        .append("t1.groupOpenDate,t1.groupCloseDate,t1.payStatus,t1.totalMoney,t1.payedMoney,t1.accountedMoney,")
		        .append("t1.orderType, t1.activityId, t1.createDate,t1.updateDate, t1.w_proCompany, t1.w_operator,")
		        .append(" t1.w_agent,t1.w_saler,t1.w_deptId,t1.orderCompanyName,t1.sysCountryId,t1.isSeizedConfirmed ");
		int whereOrderType = -1;
		if(StringUtils.isNotBlank(orderType) && StringUtils.isNotEmpty(orderType)){
			whereOrderType = Integer.parseInt(orderType);
		}
		if(Context.ORDER_TYPE_ALL == whereOrderType){
			//查询所有
			pageMap = getAllOrderRecord(page, orderType, mapRequest, commonSQL.toString());
		}else if(whereOrderType == Context.ORDER_TYPE_QZ){
			pageMap = getQZOrderRecord(page, orderType, mapRequest, column, commonSQL.toString());
		}else if(whereOrderType == Context.ORDER_TYPE_JP){
			pageMap = getJPOrderRecord(page, orderType, mapRequest, column, commonSQL.toString());
		}else{
			pageMap = getDTOrderRecord(page, orderType, mapRequest, column, commonSQL.toString());
		}
		return pageMap;
	}
    
    public List<Map<String, Object>> findOrderpayByOrderIdAndType(Long orderId, Integer orderType) {
    	
    	 String Sql = "SELECT id,payTypeName,moneySerialNum,differenceUuid,createDate,payPriceType,isAsAccount,reject_reason rejectReason,payVoucher " +
    	 				"FROM orderpay WHERE orderId = " + orderId + " AND orderType = " + orderType + 
    			 		" AND delFlag = '" + Orderpay.DEL_FLAG_NORMAL + 
    			 		"' AND orderPaySerialNum IS NOT NULL ORDER BY id DESC"; 	
    			    	
    	 return  productorderDao.findBySql(Sql,Map.class);
    }
    
    public List<Orderpay> findOrderpayByOrderId(Long orderId) {
    	return orderpayDao.findOrderpayByOrderId(orderId);
    }
    
    @Transactional
    public boolean deleteCostChange(Long costChange) {
        Costchange costc = costchangeDao.findOne(costChange);
        Traveler trave = travelerDao.findOne(costc.getTravelerId());
        //costchangeDao.deleteCostchangeByTravelerId(trave.getOrderId());
        
        costchangeDao.delete(costChange);
        this.recountByOrderId(trave.getOrderId());
        //重新计算价格
       // this.recountByOrderId(orderId);
        return true;
    }
    
    /**
     * 根据订单ids查询支付订单
     * @param orderIds
     * @return
     */
    public List<Orderpay> findOrderpayByOrderIds(List<Long> orderIds, Integer orderType) {
        return orderpayDao.findOrderpayByOrderIds(orderIds, orderType);
     }
    
    /**
	 * 清除session中Orderpay的缓存
	 * @param object
	 * @return
	 */
	public Object clearObject(Object object){
		orderpayDao.getSession().evict(object);
		return object;
	}
    
    private String getTraveActivitySql(String type, String orderStatus, TravelActivity travelActivity, Map<String,String> map, DepartmentCommon common) {
        StringBuffer sqlWhere = new StringBuffer("");
        String userType = UserUtils.getUser().getUserType();
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        Long agentIdForUser = null;
        Integer orderType =  Integer.parseInt(orderStatus);
        if(UserUtils.getUser().getAgentId()!=null)agentIdForUser=UserUtils.getUser().getAgentId();
        //交款日期
        String payCreateDateBegin = map.get("payCreateDateBegin");
        String payCreateDateEnd = map.get("payCreateDateEnd");
        if(StringUtils.isNotBlank(payCreateDateBegin)&&StringUtils.isNotBlank(payCreateDateEnd)){
        	sqlWhere.append(" and exists (select 1 from orderpay where createDate between '" + payCreateDateBegin + "' and '" + payCreateDateEnd+"')");
        }
        
        //出团日期
        String groupOpenDateBegin = map.get("groupOpenDateBegin");
        String groupOpenDateEnd = map.get("groupOpenDateEnd");
        if(StringUtils.isNotBlank(groupOpenDateBegin)) {
        	sqlWhere.append(" and agp.groupOpenDate >= '" + groupOpenDateBegin + "'");
        }
        
        if(StringUtils.isNotBlank(groupOpenDateEnd)){
            sqlWhere.append(" and agp.groupOpenDate  <= '" + groupOpenDateEnd + "'");
        }
        
        String activityCreateName = map.get("activityCreateName");
        if (StringUtils.isNotBlank(activityCreateName)){
        	sqlWhere.append(" and users.name  = '" + activityCreateName + "'");
        }
        
        //截团日期
        String groupCloseDateBegin = map.get("groupCloseDateBegin");
        String groupCloseDateEnd = map.get("groupCloseDateEnd");
        
        if(StringUtils.isNotBlank(groupCloseDateBegin)&&StringUtils.isNotBlank(groupCloseDateEnd)){
        	sqlWhere.append(" and agp.groupCloseDate  between '" + groupCloseDateBegin + "' and '" + groupCloseDateEnd + "'");
        }
        
        //创建时间
        String orderTimeBegin = map.get("orderTimeBegin");
        String orderTimeEnd = map.get("orderTimeEnd");
        String jd = map.get("jdUserId");
        if(orderType == Context.ORDER_TYPE_JP) {
        	sqlWhere.append(" and ao.product_type_id = " + orderType);
        	if(StringUtils.isNotBlank(jd)){
        		sqlWhere.append(" and aa.createBy = " + jd + " ");
        	}
        }else if (orderType == Context.ORDER_TYPE_QZ) {
        	sqlWhere.append(" and vo.product_type_id = " + orderType);
        	if(StringUtils.isNotBlank(jd)){
        		sqlWhere.append(" and vp.createBy = " + jd + " ");
        	}
        } else if(orderType == Context.ORDER_TYPE_ALL){
        	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
        }else {        	
        	/*增加交易明细、账龄查询的计调查询条件*/
        	if("detail".equals(map.get("option")) || "account".equals(map.get("option"))){
            	if(StringUtils.isNotBlank(jd)){
            		sqlWhere.append(" and activity.createBy = " + jd + " ");
            	}
        	}
        	
        	/*
        	 * author:wuqiang
        	 * 增加按部门查询条件，开始
        	 */
        	//要查询的部门ID
        	String queryDepartmentId = map.get("queryDepartmentId");
        	if(StringUtils.isNotBlank(queryDepartmentId)){
        		sqlWhere.append(" and pro.orderSalerId in(select su.id from sys_user su where su.id in(select srf.userId from sys_user_role srf where srf.roleId in(select sr.id from sys_role sr where sr.deptId = " + queryDepartmentId + ")))");
        	}
        	//增加按部门查询条件，结束
        	
        	sqlWhere.append(" and pro.orderStatus = " + orderType);
        }
        
        if (StringUtils.isNotBlank(orderTimeBegin)) {
        	if (orderType == Context.ORDER_TYPE_JP) {
        		sqlWhere.append( " and ao.create_date >= '" + orderTimeBegin + " 00:00:00" + "'");
            } else if (orderType == Context.ORDER_TYPE_QZ) {
            	sqlWhere.append( " and vo.create_date >= '" + orderTimeBegin + " 00:00:00" + "'");
            } else if(orderType == Context.ORDER_TYPE_ALL){
            	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
            } else {
            	sqlWhere.append( " and pro.orderTime >= '" + orderTimeBegin + " 00:00:00" + "'");
            }
        	
        }
        
        if (StringUtils.isNotBlank(orderTimeEnd)) {
        	if (orderType == Context.ORDER_TYPE_JP) {
        		sqlWhere.append( " and ao.create_date <= '" + orderTimeEnd + " 23:59:59" + "'");
            } else if (orderType == Context.ORDER_TYPE_QZ) {
            	sqlWhere.append( " and vo.create_date <= '" + orderTimeEnd + " 23:59:59" + "'");
            } else if(orderType == Context.ORDER_TYPE_ALL){
            	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
            } else {
            	sqlWhere.append( " and pro.orderTime <= '" + orderTimeEnd + " 23:59:59" + "'");
            }
        }
        
        if(Context.USER_TYPE_MAINOFFICE.equalsIgnoreCase(userType)){
            //总社 查询本单位下的订单
        	sqlWhere.append(" and pro.orderCompany = " + agentIdForUser + " ");
        }else if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
            //接待社  查询自己添加的产品 对应的订单
        	if(orderType == Context.ORDER_TYPE_JP) {
        		sqlWhere.append(" and aa.proCompany = " + userCompanyId + " ");
            }else if (orderType == Context.ORDER_TYPE_QZ) {
            	sqlWhere.append(" and vp.proCompanyId = " + userCompanyId + " ");
            } else if(orderType == Context.ORDER_TYPE_ALL){
            	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
            } else {
            	sqlWhere.append(" and activity.proCompany = " + userCompanyId + " ");
            }
            //如果是接待社  那么要考虑当前查询的是哪个批发商
            
            Agentinfo agentinfo = this.agentinfoDao.findSpecialAgentinfo(userCompanyId);
            Long specialAgentId = null;
            
            String agentId = map.get("agentId");
            
            if(StringUtils.isNotBlank(agentId)) {
	            if(agentinfo != null && agentId.equals(agentinfo.getId().toString())){
	            	specialAgentId = agentinfo.getId();
	            	if(orderType == Context.ORDER_TYPE_JP) {
	            		sqlWhere.append(" and ao.agentinfo_id = " + specialAgentId);
	                }else if (orderType == Context.ORDER_TYPE_QZ) {
	                	sqlWhere.append(" and vo.agentinfo_id = " + specialAgentId);
	                }else if(orderType == Context.ORDER_TYPE_ALL){
	                	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
	                }else {
	                	sqlWhere.append(" and pro.orderCompany = " + specialAgentId);
	                }
	            } else {
	            	if(orderType == Context.ORDER_TYPE_JP) {
	            		sqlWhere.append(" and ao.agentinfo_id = " + agentId);
	                }else if (orderType == Context.ORDER_TYPE_QZ) {
	                	sqlWhere.append(" and vo.agentinfo_id = " + agentId);
	                }else if(orderType == Context.ORDER_TYPE_ALL){
	                	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
	                }else {
	                	sqlWhere.append(" and pro.orderCompany = " + agentId);
	                }
	            }
        	}
        }
        if(travelActivity != null){
        	String targetAreaIds = travelActivity.getTargetAreaIds();
        	if( StringUtils.isNotBlank(targetAreaIds) ){
        		
        		sqlWhere.append(" and exists(select 1 from activitytargetarea where activity.id=activitytargetarea.srcActivityId " +
        				" and activitytargetarea.targetAreaId in(" + targetAreaIds + "))");
        	}
        	if(StringUtils.isNotBlank(travelActivity.getTrafficName())){
        		sqlWhere.append(" and activity.trafficName = " + travelActivity.getTrafficName());
        	}
        	if(travelActivity.getActivityTypeId()!=null){
        		sqlWhere.append(" and activity.activityTypeId = " + travelActivity.getActivityTypeId());
        	}
        	if(travelActivity.getActivityDuration()!=null){
        		sqlWhere.append(" and activity.activityDuration = " + travelActivity.getActivityDuration());
        	}
        	if(StringUtils.isNotBlank(travelActivity.getAcitivityName())){
        		sqlWhere.append(" and activity.acitivityName like '%" + travelActivity.getAcitivityName() + "%' ");
        	}
        	if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()!=null){
        		String strDateopen = DateUtils.formatDate(travelActivity.getGroupOpenDate());
        		String strDateclose = DateUtils.formatDate(travelActivity.getGroupCloseDate());
        		sqlWhere.append(" and agp.groupOpenDate between '" + strDateopen + "' and '" + strDateclose + "'");
        	}
        }
        
        //订单号
        String orderNum = map.get("orderNum");
        if(StringUtils.isNotBlank(orderNum)){
        	if(orderType == Context.ORDER_TYPE_JP) {
        		sqlWhere.append(" and (ao.order_no like '%" + orderNum + "%' ) ");
            }else if (orderType == Context.ORDER_TYPE_QZ) {
            	sqlWhere.append(" and (vo.order_no like '%" + orderNum + "%' ) ");
            }else if(orderType == Context.ORDER_TYPE_ALL){
            	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
            }else {
            	sqlWhere.append(" and pro.orderNum like '%" + orderNum + "%' ");
            }
        }
        //团号
        String groupCode = map.get("groupCode");
        if(StringUtils.isNotBlank(groupCode)){
        	if(orderType == Context.ORDER_TYPE_JP) {
        		sqlWhere.append(" and (ao.group_code like '%" + groupCode + "%' ) ");
            }else if (orderType == Context.ORDER_TYPE_QZ) {
            	sqlWhere.append(" and (vo.group_code like '%" + groupCode + "%' ) ");
            }else if(orderType == Context.ORDER_TYPE_ALL){
            	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
            }else {
            	sqlWhere.append(" and (agp.groupCode like '%" + groupCode + "%' ) ");
            }
        }
        
        //订单号或团号
        String orderNumOrGroupCode = map.get("orderNumOrGroupCode");
        if(StringUtils.isNotBlank(orderNumOrGroupCode)){
        	sqlWhere.append(" and (pro.orderNum like '%" + orderNumOrGroupCode + "%' or agp.groupCode like '%" + orderNumOrGroupCode + "%') ");
        }
        
        //联系人
        String orderPersonName = map.get("orderPersonName");
        if(StringUtils.isNotBlank(orderPersonName)){
        	sqlWhere.append(" and pro.orderPersonName like '%" + orderPersonName + "%' ");
        }
        
        String createBy = map.get("createBy");
        if(StringUtils.isNotBlank(createBy)) {
        	
        	List<User> createUserList = userDao.findByName(createBy.trim(), UserUtils.getUser().getCompany().getId());
        	StringBuffer sqlBuffer = new StringBuffer("");
        	if(createUserList!=null&&createUserList.size() >= 1) {
        		for(User u : createUserList) {
        			sqlBuffer.append(u.getId().toString()+",");
        		}
        		sqlBuffer.deleteCharAt(sqlBuffer.length()-1);
                sqlWhere.append(" and pro.createBy in(" + sqlBuffer + ") ");
        	}else if(createUserList!=null && createUserList.size()==1) {
                sqlWhere.append(" and pro.createBy = " + createUserList.get(0).getId());
        	}else {
                sqlWhere.append(" and pro.createBy = 'a'");
        	}
        }
        
        String saler = map.get("saler");
        if(StringUtils.isNotBlank(saler)){
        	if(orderType == Context.ORDER_TYPE_JP) {
        		sqlWhere.append(" and ao.salerId = " + saler);
            }else if (orderType == Context.ORDER_TYPE_QZ) {
            	sqlWhere.append(" and vo.create_by = " + saler);
            }else if(orderType == Context.ORDER_TYPE_ALL){
            	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
            }else if(orderType == Context.ORDER_TYPE_HOTEL || orderType == Context.ORDER_TYPE_ISLAND){
            	sqlWhere.append(" and pro.orderSalerId=" + saler);
            }else{
            	sqlWhere.append(" and pro.salerId="+saler);
            }
        }
        String creator = map.get("creator");
        if(StringUtils.isNotBlank(creator)){
        	if(orderType == Context.ORDER_TYPE_JP) {
        		sqlWhere.append(" and ao.create_by = " + creator);
            }else if (orderType == Context.ORDER_TYPE_QZ) {
            	sqlWhere.append(" and vo.create_by = " + creator);
            }else if(orderType == Context.ORDER_TYPE_ALL){
            	//如果选择是全部，则不进行任何处理 add by shijun.liu 2015.04.09
            }else {
            	sqlWhere.append(" and pro.createBy=" + creator);
            }
        }
        
        //分部门显示
        if(orderType < Context.ORDER_TYPE_QZ) {
        	systemService.getDepartmentSql("order", null, sqlWhere, common, orderType);
        }
        
        return sqlWhere.toString();
    }
    
    private String getPreTraveActivitySql(String orderStatus, TravelActivity travelActivity,
            String orderNumOrGroupCode, String agentId,
            String orderPersonName, Map<String,String> map, DepartmentCommon common) {
        StringBuffer sqlWhere = new StringBuffer("");
        String userType = UserUtils.getUser().getUserType();
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        Long agentIdForUser = null;
        if(UserUtils.getUser().getAgentId()!=null)agentIdForUser=UserUtils.getUser().getAgentId();
       
        
        //出团日期
        String groupOpenDateBegin = map.get("groupOpenDateBegin");
        String groupOpenDateEnd = map.get("groupOpenDateEnd");
        if(StringUtils.isNotBlank(groupOpenDateBegin)) {
        	sqlWhere.append(" and agp.groupOpenDate >= '" + groupOpenDateBegin + "'");
        }
        
        if(StringUtils.isNotBlank(groupOpenDateEnd)){
            sqlWhere.append(" and agp.groupOpenDate  <= '" + groupOpenDateEnd + "'");
        }
        
        //截团日期
        String groupCloseDateBegin = map.get("groupCloseDateBegin");
        String groupCloseDateEnd = map.get("groupCloseDateEnd");
        
        if(StringUtils.isNotBlank(groupCloseDateBegin) && StringUtils.isNotBlank(groupCloseDateEnd)){
        	sqlWhere.append(" and agp.groupCloseDate  between '" + groupCloseDateBegin+"' and '" + groupCloseDateEnd+"'");
        }
        
        //创建时间
        String orderTimeBegin = map.get("orderTimeBegin");
        String orderTimeEnd = map.get("orderTimeEnd");
        
        if(StringUtils.isNotBlank(orderTimeBegin)&&StringUtils.isNotBlank(orderTimeEnd)) {
        	sqlWhere.append(" and pro.orderTime  between '" + orderTimeBegin + " 00:00:00" + "' and '" + orderTimeEnd + " 23:59:59" + "'");
        }
        
        if(StringUtils.isNotBlank(orderStatus)) {
        	sqlWhere.append(" and pro.orderStatus = " + orderStatus);
        }
        
        if(Context.USER_TYPE_MAINOFFICE.equalsIgnoreCase(userType)){
            //总社 查询本单位下的订单
        	sqlWhere.append(" and pro.orderCompany = " + agentIdForUser + " ");
        } else if(Context.USER_TYPE_RECEPTION.equalsIgnoreCase(userType)){
            //接待社  查询自己添加的产品 对应的订单
        	sqlWhere.append(" and activity.proCompany = "+userCompanyId+" ");
            //如果是接待社  那么要考虑当前查询的是哪个批发商
            
            Agentinfo agentinfo = this.agentinfoDao.findSpecialAgentinfo(userCompanyId);
            Long specialAgentId = null;
            
            if(StringUtils.isNotBlank(agentId)) {
	            if(agentinfo != null && agentId.equals(agentinfo.getId().toString())){
	            	specialAgentId = agentinfo.getId();
	            	sqlWhere.append(" and pro.orderCompany = " + specialAgentId);
	            }else{
	            	sqlWhere.append(" and pro.orderCompany = " + agentId);
	            }
        	}
        }
        
        String targetAreaIds = travelActivity.getTargetAreaIds();
        if( StringUtils.isNotBlank(targetAreaIds) ){
            
        	sqlWhere.append(" and exists(select 1 from activitytargetarea where activity.id=activitytargetarea.srcActivityId " +
                    " and activitytargetarea.targetAreaId in(" + targetAreaIds + "))");
        }
        if(StringUtils.isNotBlank(travelActivity.getTrafficName())){
        	sqlWhere.append(" and activity.trafficName = " + travelActivity.getTrafficName());
        }
        if(travelActivity.getActivityTypeId()!=null){
        	sqlWhere.append(" and activity.activityTypeId = " + travelActivity.getActivityTypeId());
        }
        if(travelActivity.getActivityDuration()!=null){
        	sqlWhere.append(" and activity.activityDuration = " + travelActivity.getActivityDuration());
        }
        if(StringUtils.isNotBlank(travelActivity.getAcitivityName())){
        	sqlWhere.append(" and activity.acitivityName like '%" + travelActivity.getAcitivityName() + "%' ");
        }
        
        if(travelActivity.getGroupOpenDate()!=null && travelActivity.getGroupCloseDate()!=null){
            String strDateopen = DateUtils.formatDate(travelActivity.getGroupOpenDate());
            String strDateclose = DateUtils.formatDate(travelActivity.getGroupCloseDate());
            sqlWhere.append(" and agp.groupOpenDate between '" + strDateopen + "' and '" + strDateclose + "'");
        }
        
        //订单号
        String orderNum = map.get("orderNum");
        if(StringUtils.isNotBlank(orderNum)){
        	sqlWhere.append(" and pro.orderNum like '%" + orderNum + "%' ");
        }
        
        //订单号或团期号
        if(StringUtils.isNotBlank(orderNumOrGroupCode)){
        	sqlWhere.append(" and (pro.orderNum like '%" + orderNumOrGroupCode + "%' or agp.groupCode like '%" + orderNumOrGroupCode + "%') ");
        }
        
        //预定人
        if(StringUtils.isNotBlank(orderPersonName)){
            sqlWhere.append(" and pro.orderPersonName like '%" + orderPersonName + "%' ");
        }
        
        String saler = map.get("saler");
        if(StringUtils.isNotBlank(saler)){
        	sqlWhere.append(" and pro.orderSalerId=" + saler);
        }
        
        //分部门显示
        systemService.getDepartmentSql("order", null, sqlWhere, common, Integer.parseInt(orderStatus));
        return sqlWhere.toString();
    }
    
    
    private String getAgentOrderSql(Integer agentId) {
        String sqlWhere = "";        
        //渠道商ID
        sqlWhere+=" and pro.orderCompany = "+agentId+" ";       
        return sqlWhere;
    }
    
     /**
     *  功能:
     *          通过orderId 重新计算需要支付的价格
     *  @author xuziqian
     *  @DateTime 2014-1-26 下午9:11:44
     *  @param orderId
     *  @return
     */
    @Transactional
    private boolean recountByOrderId(Long orderId) {
        ProductOrderCommon productorder = productorderDao.findOne(orderId);
        List<Traveler> listTraveler = travelerDao.findTravelerByOrderId(orderId);
//        BigDecimal proOrderPrice = BigDecimal.ZERO;
        for(Traveler tra : listTraveler){
            List<Costchange> listcostChange = costchangeDao.findCostchangeBytravelerId(tra.getId());
            BigDecimal travalCost = BigDecimal.ZERO;
            for(Costchange costChnage : listcostChange){
                if(costChnage.getCostSum()!=null){
                    travalCost = StringNumFormat.getBigDecimalAdd(travalCost, costChnage.getCostSum());
                }
            }
            /* 2014-11-28, 数据库改用多币种表后，此处报编译错误，需要 亚昆 修改
            tra.setPayPrice(StringNumFormat.getBigDecimalAdd(StringNumFormat.getBigDecimalAdd(tra.getSrcPrice(),travalCost),tra.getSingleDiff()));
            proOrderPrice = StringNumFormat.getBigDecimalAdd(proOrderPrice,tra.getPayPrice());
            */
        }
        int travelerNum = listTraveler.size();
        Integer orderPersonNum = productorder.getOrderPersonNum();
        if(orderPersonNum>travelerNum){
            //proOrderPrice+=(orderPersonNum-travelerNum)*productorder.getSettlementAdultPrice();
//        	proOrderPrice = StringNumFormat.getBigDecimalAdd(proOrderPrice,StringNumFormat.getBigDecimalMultiply(new BigDecimal(orderPersonNum-travelerNum),productorder.getSettlementAdultPrice()));
        }
      //注释productorder.setFrontMoney(productorder.getPayDeposit().multiply(new BigDecimal(orderPersonNum)));
        travelerDao.save(listTraveler);
        productorderDao.save(productorder);
        return true;
    }
    
    
     /**
     *  功能:
     *  取消订单
     *  @author xuziqian
     *  @DateTime 2014-2-12 下午9:57:44
     *  @param productOrder
     *  @param flag  0524需求 标识：8订单取消 9订单删除
     * @throws OptimisticLockHandleException 
     * @throws PositionOutOfBoundException 
     * update  by ruyi.chen
     *  update Datetime 2014-11-26
     *  describe:取消订单时，无子订单，参团未付款情况可直接取消，付款后按退团流程走，无取消按钮
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void cancelOrder(ProductOrderCommon productOrder, String description, int flag, HttpServletRequest request)
    				throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
    	
        //判断是否占位
        Map<String,String> rMap = orderStatusticsService.getPlaceHolderInfo(productOrder.getId(), productOrder.getOrderStatus().toString());
        if (null != rMap && null != rMap.get(Context.RESULT)) {
        	String resultP = rMap.get(Context.RESULT);
        	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
        		//当订单占位时归还余位
        		Map<String,String> pMap = orderStatusticsService.saveActivityGroupPlaceHolderChange(productOrder.getId(), productOrder.getOrderStatus().toString(), productOrder.getOrderPersonNum(), request, flag);
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
        	//添加操作日志
	        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, 
	        		"订单" + productOrder.getOrderNum() + "取消失败", "2", productOrder.getOrderStatus(), productOrder.getId());
        	throw new Exception("取消订单失败！");
        }
        productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));
        productOrder.setCancelDescription(description);
        productorderDao.save(productOrder);
      //-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为99（取消）---开始---
        orderDateSaveOrUpdateDao.updateOrderStatus(productOrder.getId(), productOrder.getOrderStatus().toString());
        
      //-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为99（取消）---结束---        
        
        //添加操作日志
        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
        		"订单" + productOrder.getOrderNum() + "取消成功", "2", productOrder.getOrderStatus(), productOrder.getId());
        // 修改订单跟踪状态
		progressService.updateOrderStatus(productOrder, Context.ORDER_ZZ_CANCEL);
    }
    
    /**
     * 已撤销占位
     * @Description 
     * @author yakun.bai
     * @Date 2015-11-16
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void revokeOrder(ProductOrderCommon productOrder, HttpServletRequest request)
    				throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
    	
        //判断是否占位
        Map<String,String> rMap = orderStatusticsService.getPlaceHolderInfo(productOrder.getId(), productOrder.getOrderStatus().toString());
        if (null != rMap && null != rMap.get(Context.RESULT)) {
        	String resultP = rMap.get(Context.RESULT);
        	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
        		//当订单占位时归还余位
        		Map<String,String> pMap = orderStatusticsService.saveActivityGroupPlaceHolderChange(productOrder.getId(), productOrder.getOrderStatus().toString(), productOrder.getOrderPersonNum(), request, 0);
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
        	//添加操作日志
	        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, 
	        		"订单" + productOrder.getOrderNum() + "撤销占位失败", "2", productOrder.getOrderStatus(), productOrder.getId());
        	throw new Exception("取消订单失败！");
        }
        productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_CW_CX));
        productorderDao.save(productOrder);
        //添加操作日志
        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
        		"订单" + productOrder.getOrderNum() + "撤销占位成功", "2", productOrder.getOrderStatus(), productOrder.getId());
    }
    
    
    /**
	 * 订单驳回：财务确认达账为入口
	 * @param orderId		订单Id
	 * @param payOrderId	支付订单Id
	 * @param isHoldPosition	是否保留位置
	 * @return String
	 * @throws Exception
	 */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public String rejectOrder(Long orderId, Long payOrderId, boolean isHoldPosition, HttpServletRequest request) throws Exception {
    	
    	ProductOrderCommon productOrder = getProductorderById(orderId);
		Orderpay orderPay = orderpayDao.getOrderPayById(payOrderId);
		
		/** 如果保留位置：订单状态不变；如果不保留位置：订单状态变为已取消、支付记录状态变为已驳回； 保留与否，已付金额都要减去此支付记录中金额 */
		if (productOrder != null && orderPay != null) {
			
	        //如果不保留位置则取消订单
			if (!isHoldPosition) {
				
				// 获取要归还人数（邮轮归还的是房间数）
				int orderNum = Integer.parseInt(Context.ORDER_STATUS_CRUISE) != productOrder.getOrderStatus() 
						 ? productOrder.getOrderPersonNum() : productOrder.getRoomNumber() != null ? productOrder.getRoomNumber() : 0;
				
				//如果订单支付状态不是取消，则需要归还余位（因为财务可能会同一订单两次驳回）
				if (Integer.parseInt(Context.ORDER_PAYSTATUS_YQX) != productOrder.getPayStatus()) {
					ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
					//占位订单
			        if (productOrder.getPlaceHolderType() == null || StringUtils.equals(Context.PLACEHOLDERTYPE_ZW, productOrder.getPlaceHolderType().toString())) {
		            	//返还余位数
		            	activityGroup.setPlusFreePosition(orderNum);
		                activityGroup.setFreePosition(activityGroup.getFreePosition() + orderNum);
		                //返还售出占位
		                activityGroup.setSoldNopayPosition(activityGroup.getSoldNopayPosition() - orderNum);
		                activityGroup.setPlusSoldNopayPosition(-orderNum);
		                //返还已占位人数
		                activityGroup.setPlusNopayReservePosition(-orderNum);
		                activityGroup.setNopayReservePosition(activityGroup.getNopayReservePosition() - orderNum);
		                activityGroupService.updatePositionNumByOptLock(activityGroup, com.trekiz.admin.common.utils.StringUtils.getVersionNumber(request));
		                
		                //占位订单驳回的时候将 order_data_statistic里的order_status改为已取消状态 add by chao.zhang 542需求
		                List<Map<String,Object>> statisticsId = orderDateSaveOrUpdateDao.getOrderDataStatisticsId(productOrder.getId());
		                if(statisticsId.size() > 0){
		                	orderDateSaveOrUpdateDao.updateOrderStatus(productOrder.getId(),Integer.parseInt(Context.ORDER_PAYSTATUS_YQX),productOrder.getOrderStatus());
		                }
		               
			        }
			        //切位订单
			        else {
			        	 ActivityGroupReserve groupReserve = new ActivityGroupReserve();
			             groupReserve = activityGroupReserveDao.findByAgentIdAndSrcActivityIdAndActivityGroupId
			                     (productOrder.getOrderCompany(), productOrder.getProductId(),activityGroup.getId());
			             
			             //更新切位余位
			             groupReserve.setLeftpayReservePosition(groupReserve.getLeftpayReservePosition() + orderNum);
			             //更新切位售出人数
			 			groupReserve.setSoldPayPosition(groupReserve.getSoldPayPosition() - orderNum);
			             //保存切位
			             activityGroupReserveDao.save(groupReserve);
			             //更新售出切位
			             activityGroup.setSoldPayPosition(activityGroup.getSoldPayPosition() - orderNum);
			        }
				}
				productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));
				// 0524需求 团期余位变化,记录在团控板中
		        // 财务驳回取消占位
		        groupControlBoardService.insertGroupControlBoard(10, orderNum, "订单号"+productOrder.getOrderNum()+"取消"+orderNum+"人占位", productOrder.getProductGroupId(), -1);
		        // 0524需求 团期余位变化,记录在团控板中
			}
			
			//支付订单置为已驳回状态
			orderPay.setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
            String reason = request.getParameter("reason");
            if(reason != null) {
                reason = reason.replaceAll(" ", "");
            }
            orderPay.setRejectReason(reason);
			
			//已付金额减去驳回金额
			List<MoneyAmount> list = moneyAmountService.findAmountBySerialNum(orderPay.getMoneySerialNum());
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					moneyAmountService.saveOrUpdateMoneyAmount(list.get(i), productOrder.getPayedMoney(), "subtract", list.get(i).getMoneyType());
				}
			}
			
			
			//如果不保留位置则取消订单
			if (!isHoldPosition) {
				// 修改订单跟踪状态
		     	progressService.updateOrderStatus(productOrder, Context.ORDER_ZZ_BH_CANCEL);
			} else {
				// 修改订单跟踪状态
				progressService.updateOrderStatus(productOrder, Context.ORDER_ZZ_BH2_CANCEL);
			}
			
		} else {
			return "订单查询错误";
		}
	    return "ok";
    }
    
    /**
     * 单团达账撤销
     * @param orderpay
     * @throws Exception 
     * @throws PositionOutOfBoundException 
     * @throws OptimisticLockHandleException 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    void DTcancelOrder(Orderpay orderpay) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
    	//单团订单达账金额修改
		Integer orderType = orderpay.getOrderType();
		if (Context.ORDER_TYPE_DT == orderType
				|| Context.ORDER_TYPE_SP == orderType
				|| Context.ORDER_TYPE_YX == orderType
				|| Context.ORDER_TYPE_DKH == orderType
				|| Context.ORDER_TYPE_ZYX == orderType
				|| Context.ORDER_TYPE_CRUISE == orderType) {
			Long orderId = orderpay.getOrderId();
			if (orderId != null) {
				
				ProductOrderCommon productOrder = getProductorderById(orderId);
				
				boolean updateFlag = false;
		    	Integer priceType = productOrder.getPriceType();
		    	// 如果不是散拼订单并且是quauq渠道订单，则忽略
		    	if (Context.ORDER_STATUS_LOOSE.equals(orderType.toString()) && priceType == 2) {
		    		if (progressService.isFullPayed(productOrder)) {
		    			updateFlag = true;
		    		}
		    	}
				
				
				//达账金额减去撤销金额
				List<MoneyAmount> list = moneyAmountService.findAmountBySerialNum(orderpay.getMoneySerialNum());
				//判断达帐金额是否为空：如果为空则针对财务确认订单需要恢复其原来订单状态
				boolean flag = true;
				if (CollectionUtils.isNotEmpty(list)) {
					for (int i = 0; i < list.size(); i++) {
						MoneyAmount amount = moneyAmountService.saveOrUpdateMoneyAmount(list.get(i), productOrder.getAccountedMoney(), "subtract", list.get(i).getMoneyType());
						//如果金额为0则删除
						if (amount != null && amount.getAmount().doubleValue() == 0.00) {
							moneyAmountService.deleteById(amount.getId());
						} else {
							flag = false;
						}
						
					}
				}
				if (flag && productOrder.getPayMode().equals(Context.ORDER_PAYSTATUS_CW)) {
					revokeOrder(productOrder, null);
 					productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_CW));
					productorderDao.save(productOrder);
					
					//订单状态为8的不统计，所以将表order_data_statistics中order_status改为99（取消）
					//-------by------junhao.zhao-----2017-03-22-----表order_data_statistics中order_status改为99（取消）---开始---
					orderDateSaveOrUpdateDao.updateOrderStatus(productOrder.getId(), productOrder.getOrderStatus().toString());
			        //-------by------junhao.zhao-----2017-03-22-----表order_data_statistics中order_status改为99（取消）---结束--- 
				}
				
				
				// 修改订单跟踪状态
				if (updateFlag && !progressService.isFullPayed(productOrder)) {
					progressService.updateOrderStatus(productOrder, Context.ORDER_ZZ_NORMAL);
				}
			}
		}
    }
    
    /**
     * 订单删除
     * @param orderId
     * @param request
     * @throws OptimisticLockHandleException
     * @throws PositionOutOfBoundException
     * @throws Exception
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public void deleteOrder(long orderId, HttpServletRequest request)
			throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		ProductOrderCommon productOrder = productorderDao.findOne(orderId);
		//如果订单状态从取消到删除，则不需要归还余位
		if (productOrder.getPayStatus() != null 
				&& !Context.ORDER_PAYSTATUS_YQX.equals(productOrder.getPayStatus().toString())
				&& !Context.ORDER_PAYSTATUS_DEL.equals(productOrder.getPayStatus().toString())) {
			cancelOrder(productOrder, "", 9, request);
		}
		productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_DEL));
		productorderDao.save(productOrder);
		//-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为删除---开始---
        orderDateSaveOrUpdateDao.updateOrderStatusInvoke(productOrder.getId(),productOrder.getPayStatus());
        
        //-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为删除---结束--- 
		
		//添加操作日志
        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
        		"订单" + productOrder.getOrderNum() + "删除成功", "2", productOrder.getOrderStatus(), productOrder.getId());
        // 修改订单跟踪状态
     	progressService.updateOrderStatus(productOrder, Context.ORDER_ZZ_DELETE);
	}
    
    
    /**
     * 根据订单剩余天数和付款状态来取消订单：新需求
     * @param pageNum
     * @return
     */
    private List<Map<Object, Object>> getBeoverdueOrder(int curpage, int pageNum) {
    	int limitstartindex = 0;
    	if (curpage > 0) {
    		limitstartindex = (curpage - 1) * pageNum;
    	} else {
    		limitstartindex = 0;
    	}
    	String sql = "SELECT"
						+ " pro.id proid, pro.remainDays, pro.orderPersonNum, pro.payMode, pro.activationDate, agp.groupOpenDate,"
						+ " tra.is_after_supplement, datediff(CURRENT_DATE,agp.groupOpenDate) leftdays,"
						+ " (INTERVAL pro.remainDays*24*60 MINUTE + pro.activationDate) AS offTime"
				+ " FROM productorder pro,travelactivity tra,activitygroup agp,sys_office office"
			+ " WHERE pro.productId=tra.id AND pro.productGroupId=agp.id AND office.id = tra.proCompany AND office.is_cancle_order = 1 AND"
			+ " pro.payStatus in(1,2,3) AND tra.is_after_supplement = 0 AND (pro.is_after_supplement != 1 OR pro.is_after_supplement is null) AND"
			+ " pro.delFlag='0' having (offTime < NOW() and payMode != 3 AND pro.remainDays is not null and tra.is_after_supplement <> 1 ) OR (leftdays>=0 AND tra.is_after_supplement <> 1)"
			+ " limit " + limitstartindex + "," + pageNum;
    	return productorderDao.findBySql(sql, Map.class);
    }
    
     /**
     *  功能:
     *       定时任务重置订单状态  
     *  @author xuziqian
     *  @DateTime 2014-2-18 下午5:52:15
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void cancelOrderWithRemainDays() {
        int curpage = 0;
        int pageNum = 100;
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        do{
        	//TODO
            list = this.getBeoverdueOrder(curpage, pageNum);
            for (Map<Object, Object> map : list) {
            	cancelOrderBySql(Long.parseLong(map.get("proid").toString()), "系统自动取消订单，原因：已过保留天数");
            }
            curpage++;
        }while(list.size()>=pageNum);
    }
    
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    private void cancelOrderBySql(long orderId, String description) {
        ProductOrderCommon productOrder = productorderDao.getById(orderId);
        try {
			cancelOrder(productOrder, description, 11, null);
		} catch (OptimisticLockHandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PositionOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 根据团期id查询团期订单
     * @param productGroupId
     * @return
     */
	public List<ProductOrderCommon> findByProductGroupIdOrderByCompany(Long productGroupId) {
		List<ProductOrderCommon> list = productorderDao.findByProductGroupIdOrderByCompany(productGroupId);
		return list;
	}
	/**
	 * 根据销售ID获取订单id分组
	 * @param productGroupId
	 * @return
	 */
	public List<Object> findProductIdByProductGroupIdOrderByCompany(Long productGroupId){
		List<Object> productIds = productorderDao.findOrderIdsByProductGrouopId(productGroupId);
		return productIds;
	}
	
	/**
	 * 根据产品id查询游客信息
	 * @param orderId
	 * @return
	 */
	public List<Object[]> selectTravelerByOrderId(Long orderId, Integer orderType) {
		List<Object[]> list = travelerDao.findByOrderId(orderId, orderType);
		return list;
	}
	/**
	 * 根据产品id查询游客信息和订单信息
	 * @param orderId
	 * @return
	 * @date 2016-11-1
	 */
	public List<Object[]> selectTravelerAndOrderByOrderId(Long orderId, Integer orderType) {
		List<Object[]> list = travelerDao.findTravelAndOrderIdByOrderId(orderId, orderType);
		return list;
	}
	
	/**	
	 * 2016年4月12日  update by pengfei.shang 需求0228
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<Object[]> selectRdhytTravelerByOrderId(Long orderId, Integer orderType) {
		List<Object[]> list = travelerDao.findRdhytByOrderId(orderId, orderType);
		return list;
	}
	
	public List<String[]> findSum1(String showType, String orderStatus, String type,
			TravelActivity travelActivity, Map<String,String> mapRequest, DepartmentCommon common){
		String whereSQL = createWhereSQL(mapRequest,orderStatus,common);
		String totalMoneySQL = createMoneySQL(Context.MONEY_TYPE_YSH, whereSQL, mapRequest);
		String payedMoneySQL = createMoneySQL(Context.MONEY_TYPE_YS, whereSQL, mapRequest);
		String accountedMoneySQL = createMoneySQL(Context.MONEY_TYPE_DZ, whereSQL, mapRequest);
		//查询应收总金额
		List<String> totalMoney = getTotalMoney(totalMoneySQL);
		//查询已达账金额
		List<Object[]> accountedMoney = getAccountedOrPayedMoney(accountedMoneySQL);
		//查询已付金额
		List<Object[]> payedMoney = getAccountedOrPayedMoney(payedMoneySQL);
		String notAccountMoney = getNotAccountedMoney(payedMoney, accountedMoney);
		//应收总金额
		List<Object[]> totalMoneyArray = getTotalMoneyArray(totalMoneySQL);
		//未收总金额
		String notPayedMoney = getNotAccountedMoney(totalMoneyArray, payedMoney);
		//查询总人数
		List<BigDecimal> totalPersonNum = getAllPersonNum(whereSQL, mapRequest);
		List<String[]> list = new ArrayList<String[]>();
		String[] array = new String[4];
		array[0] = totalMoney.get(0) == null ? "0" :totalMoney.get(0);
		array[1] = ("".equals(notAccountMoney) || null == notAccountMoney)? "0":notAccountMoney;
		array[2] = totalPersonNum.get(0) == null ?"0":String.valueOf(totalPersonNum.get(0));
		array[3] = ("".equals(notPayedMoney) || null == notPayedMoney)? "0":notPayedMoney;
		list.add(array);
		return list;
	}
	
	/**
	 * 获取未达账金额（未达账金额 = 已付金额 - 已达账金额）
	 * @param accountedMoney      已达账金额
	 * @param payedMoney          已付金额
	 * @return
	 */
	private String getNotAccountedMoney(List<Object[]> payedMoney, List<Object[]> accountedMoney){
		StringBuffer str = new StringBuffer();
		int payedMoneySize = 0;
		int accountedMoneySize = 0;
		if(null != payedMoney && payedMoney.size() > 0){
			payedMoneySize = payedMoney.size();
		}
		if(null != accountedMoney && accountedMoney.size()>0){
			accountedMoneySize = accountedMoney.size();
		}
		if(payedMoneySize - accountedMoneySize >=0 ){
			if(payedMoneySize==0 && accountedMoneySize == 0){//已付和已达账均为空
				return str.toString();
			}else if(accountedMoneySize == 0){
				for(int i = 0 ; i < payedMoneySize ; i ++ ){
					Object[] payedMoneyObj = payedMoney.get(i);
					BigDecimal payedMoneyValue = new BigDecimal(String.valueOf(payedMoneyObj[0]));
					BigDecimal notAccountedMoneyValue = payedMoneyValue;
					String formatMoney = getFormatMoney(notAccountedMoneyValue.toString(), String.valueOf(payedMoneyObj[1]));
					if(!"0.00".equals(notAccountedMoneyValue.toString())) {
						formatMoney = getFormatMoney(notAccountedMoneyValue.toString(), String.valueOf(payedMoneyObj[1]));
						if(null != formatMoney){
							str.append(formatMoney).append("+");
						}
					}
				}
			}else{//查询出已付金额的行数大于或者等于查询出已达账金额的行数
				for (int i=0;i<payedMoneySize;i++){
					Object[] payedMoneyObj = payedMoney.get(i);
//					if(i < accountedMoney.size()){//数据库查询达账金额的行数小于已付金额的行数
						Object[] accountedMoneyObj = new Object[2];
						for (int j = 0; j < accountedMoneySize; j++) {
							if(String.valueOf(payedMoneyObj[1]).equals(String.valueOf(accountedMoney.get(j)[1]))) {
								accountedMoneyObj = accountedMoney.get(j);
//								break;
								if(String.valueOf(payedMoneyObj[1]).equals(String.valueOf(accountedMoneyObj[1]))){
									BigDecimal payedMoneyValue = new BigDecimal(String.valueOf(payedMoneyObj[0]));
									BigDecimal accountedMoneyValue = new BigDecimal(String.valueOf(accountedMoneyObj[0]));
									BigDecimal notAccountedMoneyValue = payedMoneyValue.subtract(accountedMoneyValue);
		//							String formatMoney = getFormatMoney(notAccountedMoneyValue.toString(), 
		//									String.valueOf(payedMoneyObj[1]));
		//							if(null != formatMoney){
		//								str.append(formatMoney).append("+");
		//							}
									String formatMoney = getFormatMoney(notAccountedMoneyValue.toString(), String.valueOf(payedMoneyObj[1]));;
									if(!"0.00".equals(notAccountedMoneyValue.toString())) {
										formatMoney = getFormatMoney(notAccountedMoneyValue.toString(), String.valueOf(payedMoneyObj[1]));
										if(null != formatMoney){
											str.append(formatMoney).append("+");
										}
									}
									break;
								}
							}
							if(j == accountedMoneySize - 1) {
								String formatMoney = getFormatMoney(String.valueOf(payedMoneyObj[0]), String.valueOf(payedMoneyObj[1]));
								if(null != formatMoney && !"0.00".equals(String.valueOf(payedMoneyObj[0]))){
									str.append(formatMoney).append("+");
								}
							}
						}
						
//					}else{
//						String formatMoney = getFormatMoney(String.valueOf(payedMoneyObj[0]), 
//															String.valueOf(payedMoneyObj[1]));
//						if(null != formatMoney && !"0.00".equals(String.valueOf(payedMoneyObj[0]))){
//							str.append(formatMoney).append("+");
//						}
//					}
				}
			}
		}else{//
			for (int i=0;i<accountedMoneySize;i++){
				Object[] accountedMoneyObj = accountedMoney.get(i);
				if(i < payedMoney.size()){//数据库查询已付金额的行数小于达账金额的行数
					Object[] payedMoneyObj = payedMoney.get(i);
					if(String.valueOf(payedMoneyObj[1]).equals(String.valueOf(accountedMoneyObj[1]))){
						BigDecimal payedMoneyValue = new BigDecimal(String.valueOf(payedMoneyObj[0]));
						BigDecimal accountedMoneyValue = new BigDecimal(String.valueOf(accountedMoneyObj[0]));
						BigDecimal notAccountedMoneyValue = payedMoneyValue.subtract(accountedMoneyValue);
						String formatMoney = getFormatMoney(notAccountedMoneyValue.toString(), 
								String.valueOf(payedMoneyObj[1]));
						if(null != formatMoney){
							str.append(formatMoney).append("+");
						}
					}
				}else{
					String formatMoney = getFormatMoney("-" + String.valueOf(accountedMoneyObj[0]), 
														String.valueOf(accountedMoneyObj[1]));
					if(null != formatMoney){
						str.append(formatMoney).append("+");
					}
				}
			}
		}
		if(str.toString().length() != 0){
			str.delete(str.toString().length()-1, str.toString().length());
		}
		return str.toString();
	}
	
	/**
	 * 获取格式化之后的金额数据
	 * @param money             格式化之前的金额数值
	 * @param currencyId        币种ID
	 * @return
	 */
	private String getFormatMoney(String money, String currencyId){
		String sql = "select GROUP_CONCAT(cny.currency_mark, FORMAT("+money+",2) SEPARATOR '+') from currency cny where del_flag='0' "
				+ "and currency_id = "+currencyId+" and create_company_id=" + UserUtils.getUser().getCompany().getId();
		List<String> list = productorderDao.findBySql(sql);
		return list.get(0);
	}
	
	/**
	 * 根据SQL语句不同查询已达账或者已付的金额
	 * @param moneySQL      已付金额的SQL
	 * @return
	 */
	private List<Object[]> getAccountedOrPayedMoney(String moneySQL){
		return productorderDao.findBySql(moneySQL);
	}
	
	/**
	 * 查询应收总金额
	 * @param totalMoneySQL 应收总金额的SQL
	 * @return
	 */
	private List<Object[]> getTotalMoneyArray(String totalMoneySQL){
		return productorderDao.findBySql(totalMoneySQL);
	}
	
	/**
	 * 查询总人数
	 * @param whereSQL
	 * @return
	 */
	private List<BigDecimal> getAllPersonNum(String whereSQL, Map<String, String> mapRequest){
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sum(sumPersonNum) AS sumPersonNum FROM ( SELECT o.orderPersonNum AS sumPersonNum,")
		   .append(" g.groupCode,o.orderStatus AS orderType,o.orderNum,o.orderTime,o.orderCompany AS agentId,")
		   .append(" o.salerId AS salerId,p.deptId,g.createBy AS operatorId,p.proCompany,o.payStatus ,")
		   .append(" o.createBy as orderPersonId,g.groupOpenDate, o.accounted_money, o.payed_money ")
		   .append(" FROM productorder o,activitygroup g,travelactivity p WHERE o.delFlag = '")		
		   .append(ProductOrderCommon.DEL_FLAG_NORMAL).append("' AND o.productId = p.id ")
		   .append(" AND p.proCompany = " + userCompanyId)
		   .append(" AND o.productGroupId = g.id AND p.id = g.srcActivityId ");
		if("detail".equals(mapRequest.get("option"))){
	    	sql.append(" and o.payStatus in (1,2,3,4,5) ");
	    }else{
	    	sql.append(" and o.payStatus in (1,2,3,4) ");
	    }
		sql.append(" UNION ")
		   .append("SELECT o.person_num AS sumPersonNum,o.group_code AS groupCode,o.product_type_id AS orderType,")
		   .append("o.order_no AS orderNum,o.create_date AS orderTime,o.agentinfo_id AS agentId,o.salerId AS salerId,")
		   .append("p.deptId,p.createBy AS operatorId,p.proCompany,o.order_state AS payStatus,")
		   .append(" o.create_by as orderPersonId, p.createDate groupOpenDate, o.accounted_money, o.payed_money FROM airticket_order o,")
		   .append(" activity_airticket p WHERE o.airticket_id = p.id AND o.del_flag = '")
		   .append(ProductOrderCommon.DEL_FLAG_NORMAL).append("' and o.order_state<>111 ")
		   .append(" AND p.proCompany = " + userCompanyId)
		   .append(" UNION ")
		   .append("SELECT o.travel_num AS sumPersonNum, p.groupCode AS groupCode,o.product_type_id AS orderType,")
		   .append("o.order_no AS orderNum,o.create_date AS orderTime,o.agentinfo_id AS agentId,o.salerId AS salerId,")
		   .append(" p.deptId,p.createBy AS operatorId,p.proCompanyId AS proCompany,o.payStatus,")
		   .append(" o.create_by as orderPersonId,p.createDate groupOpenDate, o.accounted_money, o.payed_money FROM visa_order o ")
		   .append(" LEFT JOIN visa_products p ON o.visa_product_id = p.id WHERE o.del_flag = '")
		   .append(ProductOrderCommon.DEL_FLAG_NORMAL).append("' and o.visa_order_status <> 100 and o.mainOrderId is null ")//C310需求，签证子订单不计入交易明细
		   .append(" AND p.proCompanyId = " + userCompanyId)
		   .append(" and o.payStatus in (1,3,5)) tt where 1=1 ").append(whereSQL);
		return productorderDao.findBySql(sql.toString());
	}
	
	/**
	 * 查询应收总金额
	 * @param totalMoneySQL 应收总金额的SQL
	 * @return
	 */
	private List<String> getTotalMoney(String totalMoneySQL){
		StringBuffer sql = new StringBuffer();
		sql.append("select GROUP_CONCAT(cny.currency_mark, FORMAT(tt.amount,2) SEPARATOR '+') sumtotalmoney from ( ")
		   .append(totalMoneySQL).append(" ) tt ,currency cny where tt.currencyId = cny.currency_id ")
		   .append(" and create_company_id = ").append(UserUtils.getUser().getCompany().getId());
		return productorderDao.findBySql(sql.toString());
	}
	
	/**
	 * 生成应收总金额，到账金额，已付金额的SQL
	 * @param moneyType          金额类型，13：totlaMoney ,5： payedMoney ,4：accountedMoney
	 * @param whereSQL           条件语句
	 * @return
	 */
    private String createMoneySQL(Integer moneyType, String whereSQL, Map<String, String> mapRequest){
    	Long userCompanyId = UserUtils.getUser().getCompany().getId();
    	String flag = "";
    	if(Context.MONEY_TYPE_YS == moneyType){
    		flag = "o.payed_money";
    	}else if(Context.MONEY_TYPE_DZ == moneyType){
    		flag = "o.accounted_money";
    	}else if(Context.MONEY_TYPE_YSH == moneyType){
    		flag = "o.total_money";
    	}
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT sum(t1.amount) AS amount, currencyId FROM ( SELECT")
    											//-IFNULL(m.amount,0) 减去差额 538需求
    	   .append(" ma.amount-IFNULL(m.amount,0) AS amount,ma.currencyId,g.groupCode,o.orderStatus AS orderType,")
    	   .append("o.orderNum,o.orderTime,o.orderCompany AS agentId,o.salerId AS salerId,")
    	   .append("p.deptId,g.createBy AS operatorId,p.proCompany,o.payStatus, ")
    	   .append(" o.createBy as orderPersonId, g.groupOpenDate,o.accounted_money,o.payed_money FROM productorder o ")
    	   //538需求 减去应收中的差额
    	   .append(" LEFT JOIN money_amount m ON o.differenceMoney = m.serialNum,")
    	   .append(" activitygroup g,travelactivity p,money_amount ma WHERE o.delFlag = '")
    	   .append(ProductOrderCommon.DEL_FLAG_NORMAL).append("' AND o.productId = p.id ")
    	   .append(" AND o.productGroupId = g.id AND p.id = g.srcActivityId ")
    	   .append(" AND p.proCompany = ").append(userCompanyId)
    	   .append(" AND ma.serialNum = ").append(flag).append(" AND ma.moneyType = ").append(moneyType);
    	if("detail".equals(mapRequest.get("option"))){
    		sql.append(" and o.payStatus in (1,2,3,4,5) ");
    	}else{
    		sql.append(" and o.payStatus in (1,2,3,4) ");
    	}
    	sql.append(" UNION ")
    	   .append(" SELECT ma.amount AS amount,ma.currencyId,o.group_code AS groupCode,")
    	   .append(" o.product_type_id AS orderType,o.order_no AS orderNum,o.create_date AS orderTime,")
    	   .append(" o.agentinfo_id AS agentId,o.salerId AS salerId,p.deptId,p.createBy AS operatorId,")
    	   .append(" p.proCompany,o.order_state AS payStatus,o.create_by as orderPersonId, ")
           .append(" p.createDate AS groupOpenDate, o.accounted_money, o.payed_money ")
    	   .append(" FROM airticket_order o,activity_airticket p,")
    	   .append(" money_amount ma WHERE o.airticket_id = p.id AND o.del_flag = '")
    	   .append(ProductOrderCommon.DEL_FLAG_NORMAL).append("' AND ma.serialNum =").append(flag)
    	   .append(" AND ma.moneyType = ").append(moneyType).append(" and o.order_state<>111 ")
    	   .append(" AND p.proCompany = ").append(userCompanyId)
    	   .append(" UNION ")
    	   .append("SELECT ma.amount AS amount,ma.currencyId,p.groupCode AS groupCode,o.product_type_id AS orderType,")
    	   .append(" o.order_no AS orderNum,o.create_date AS orderTime,o.agentinfo_id AS agentId,o.salerId AS salerId,")
    	   .append(" p.deptId,p.createBy AS operatorId,p.proCompanyId AS proCompany,o.payStatus,")
    	   .append(" o.create_by as orderPersonId, p.createDate AS groupOpenDate, o.accounted_money, o.payed_money FROM")
    	   .append(" visa_order o LEFT JOIN visa_products p ON o.visa_product_id = p.id,money_amount ma")
    	   .append(" WHERE o.del_flag = '").append(ProductOrderCommon.DEL_FLAG_NORMAL).append("' ")
    	   .append(" AND ma.serialNum = ").append(flag) //签证不需要此条件.append(" AND ma.moneyType =").append(moneyType)
    	   .append(" and o.payStatus in (1,3,5) and o.mainOrderId is null ")//C310需求，签证子订单不计入交易明细
    	   .append(" AND p.proCompanyId = ").append(userCompanyId)
    	   .append(" ) t1 where 1 = 1 ").append(whereSQL).append(" GROUP BY t1.currencyId ");
    	return sql.toString();
    } 
    
    /**
     * 生成条件查询的SQL 只有 where后面的部分
     * @param mapRequest    输入条件
     * @param orderType     订单类型 单团，机票，签证
     * @return
     */
	private String createWhereSQL(Map<String,String> mapRequest, String orderType,DepartmentCommon common){
		String orderTimeBegin = mapRequest.get("orderTimeBegin");//下单时间开始
        String orderTimeEnd = mapRequest.get("orderTimeEnd");//下单时间结束
        String groupCode = mapRequest.get("groupCode");//团号
        String orderNum = mapRequest.get("orderNum");//订单编号
        String operatorId = mapRequest.get("jdUserId");//计调
        String agentId = mapRequest.get("agentId");//渠道
        String salerId = mapRequest.get("saler");//销售
        String deptId = mapRequest.get("queryDepartmentId");//部门
        String orderPersonId = mapRequest.get("orderPersonId");//下单人
        String groupOpenDateBegin = mapRequest.get("groupOpenDateBegin");//出团日期开始
        String groupOpenDateEnd = mapRequest.get("groupOpenDateEnd");//出团日期结束
        String payStatus = mapRequest.get("payStatus");//付款状态
        String accountStatus = mapRequest.get("accountStatus");//达账状态
        Long userCompanyId = UserUtils.getUser().getCompany().getId();
        StringBuffer whereSQL = new StringBuffer();
		int whereOrderType = 0;
		if(StringUtils.isNotBlank(orderType) && StringUtils.isNotEmpty(orderType)){
			whereOrderType = Integer.parseInt(orderType);
		}
  		whereSQL.append(" and proCompany = ").append(userCompanyId);
  		//团号
		if(StringUtils.isNotBlank(groupCode)){
			whereSQL.append(" and groupCode like '%").append(groupCode).append("%'");
		}
		//类型，单团Or签证Or机票
        if(Context.ORDER_TYPE_ALL != whereOrderType){
        	whereSQL.append(" and orderType=").append(whereOrderType);
		}
		//订单编号
		if(StringUtils.isNotBlank(orderNum)){
			whereSQL.append(" and orderNum like '%").append(orderNum).append("%'");
		}
		//开始下单时间
		if(StringUtils.isNotBlank(orderTimeBegin)){
			whereSQL.append(" and orderTime >= '").append(orderTimeBegin).append(" 00:00:00'");
		}
		//结束下单时间
		if(StringUtils.isNotBlank(orderTimeEnd)){
			whereSQL.append(" and orderTime <= '").append(orderTimeEnd).append(" 23:59:59'");
		}
		//渠道
		if(StringUtils.isNotBlank(agentId)){
			whereSQL.append(" and agentId =").append(agentId);
		}
		//销售
		if(StringUtils.isNotBlank(salerId)){
			whereSQL.append(" and salerId = ").append(salerId);
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			whereSQL.append(" and deptId = ").append(deptId);
		}
		//计调人员
		if(StringUtils.isNotBlank(operatorId)){
			whereSQL.append(" and operatorId = ").append(operatorId);
		}
		//下单人
		if(StringUtils.isNotBlank(orderPersonId)){
			whereSQL.append(" and orderPersonId = ").append(orderPersonId);
		}
        //出团日期开始
        if (StringUtils.isNotBlank(groupOpenDateBegin)) {
            whereSQL.append(" and groupOpenDate >= '").append(groupOpenDateBegin).append(" 00:00:00'");
        }
        //出团日期结束
        if (StringUtils.isNotBlank(groupOpenDateEnd)) {
            whereSQL.append(" and groupOpenDate <= '").append(groupOpenDateEnd).append(" 23:59:59'");
        }
        //付款状态
        if("2".equals(payStatus)) {
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = payed_money ) = 0 ");
        }
        if("4".equals(payStatus)){
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = payed_money ) > 0 ");
        }
        //达账状态
        if("2".equals(accountStatus)) {
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = accounted_money ) = 0 ");
        }
        if("4".equals(accountStatus)) {
            whereSQL.append(" and ( SELECT count(id) FROM money_amount ma WHERE ma.serialNum = accounted_money ) > 0 ");
        }
		return whereSQL.toString();
	}
	
	public Orderpay findOrderpayById(Long id){
		return orderpayDao.findOne(id);
	}
	
	 /**
     *  功能:
     *          通过订单状态查询订单
     *  @author xuziqian
     *  @DateTime 2014-1-18 下午4:01:48
     */
    public Page<Map<Object, Object>> findPreOrderListByPayType(String orderStatus, Page<Map<Object, Object>> page, 
    		TravelActivity travelActivity, String orderNumOrGroupCode, String agentId, String orderPersonName, String orderBy, String orderShowType, Map<String,String> map, DepartmentCommon common) {
        page.setOrderBy(orderBy);
        String where = this.getPreTraveActivitySql(orderStatus, travelActivity, orderNumOrGroupCode, agentId, orderPersonName, map, common);
        return this.getByPrePayStatus(page,where);

    }

    /**
     * 根据订单编号查询订单
     * @param orderNum
     * @return
     */
    public List<ProductOrderCommon> findByOrderNum(String orderNum) {
    	String companyUuid = UserUtils.getUser().getCompany().getUuid();
    	return productorderDao.findByOrderNum(orderNum, companyUuid);
    }

    /**
	 * 根据订单唯一标识,订单类型查询游客信息
	 * @param orderId
	 * @param orderType
	 * @author chenry
     *  @DateTime 2014-11-04
	 */
	public List<Map<Object, Object>> getTravelerByOrderId(Long orderId,Integer orderType) {
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT p.orderTime,tt.id,tt.orderId,tt.name,tt.sex,tt.payPriceSerialNum,tt.personType,tt.delFlag,")
		.append("IFNULL(a.accounted_money,0) as payPrice,a.currencyId from  traveler tt LEFT JOIN productorder p on tt.orderId= p.id ")
		.append(" LEFT JOIN(select t.id,")
		.append(" CONCAT(IFNULL(ma.amount, 0),c.currency_name) as accounted_money,ma.currencyId")
		.append(" from traveler t LEFT  JOIN money_amount ma on t.payPriceSerialNum=ma.serialNum")
		.append(" JOIN currency c on ma.currencyId=c.currency_id")
		.append(" where t.orderId =? and t.delFlag in(0,4))a on tt.id=a.id  WHERE tt.orderId=? and tt.delFlag in(0,4) and tt.order_type=?  order by tt.id asc");
		List<Map<Object, Object>> ls = productorderDao.findBySql(sbf.toString(), Map.class, orderId, orderId, orderType);
		for (Map<Object, Object> m : ls) {
			String payPrice=m.get("payPrice").toString();
			if (StringUtils.isNotBlank(payPrice)) {
				m.put("payPrice", getMoneyStr(payPrice));
			}
		}
		// 处理 ls，在多币种情况下，将相同订单进行合并，返回带有多币种的 currencyId
		List<Map<Object,Object>> list = mergerTravelerList(ls);
		return list;
	}	

	/**
	 * 处理根据订单id，查询出来的游客信息，返回带有多币种的查询结果
	 * @param travelerList
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<Object, Object>> mergerTravelerList(List<Map<Object, Object>> travelerList) {
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
		Map<Integer, Map> resultMap = new LinkedHashMap();
		
		// 根据id获取对应Map
		for (int i = 0; i < travelerList.size(); i++) {
			Map item = travelerList.get(i);
			Integer id = (Integer) item.get("id");
			Map result = resultMap.get(id);
			
			if (result != null) {
				String payPrice = (String) result.get("payPrice");
				String currencyId = result.get("currencyId").toString();
				//Integer currencyId = (Integer)result.get("currencyId");
				result.put("payPrice", payPrice + "+" + item.get("payPrice"));
				result.put("currencyId", currencyId + "," + item.get("currencyId"));
				resultMap.put(id, result);
			}else {
				resultMap.put(id, item);
			}
			
		}

		for (Entry entry : resultMap.entrySet()) {
			resultList.add((Map<Object, Object>) entry.getValue());
		}
		return resultList;
	}
    
    
	 /**
		 * 根据订单唯一标识、产品类型查询游客信息
		 * @param orderId
		 * @param productType
		 * @author chenry
	     *  @DateTime 2014-11-04
		 */
		public List<Map<String, Object>> getRefundTravelerByOrderId(Long orderId,Integer productType) {
			StringBuffer sbf=new StringBuffer();
			sbf
			.append("SELECT p.orderTime,tt.id,tt.orderId,tt.name,tt.sex,tt.payPriceSerialNum,tt.personType,tt.delFlag,")
			.append("IFNULL(a.accounted_money,0) as payPrice from  traveler tt LEFT JOIN productorder p on tt.orderId= p.id ")
			.append(" LEFT JOIN(select t.id,")
			.append(" group_concat(FORMAT(IFNULL(ma.amount, 0),2),c.currency_name,'money' ORDER BY c.currency_id ) AS accounted_money")
			.append(" from traveler t LEFT  JOIN money_amount ma on t.payPriceSerialNum=ma.serialNum")
			.append(" and t.orderId =? and t.order_type=? and t.delFlag <>1 inner JOIN currency c on ma.currencyId= c.currency_id ")
			.append(" GROUP BY t.id)a on tt.id=a.id  WHERE tt.orderId=? and tt.order_type =? and tt.delFlag <>1 order by tt.id asc");
			List<Map<String, Object>> ls=productorderDao.findBySql(sbf.toString(), Map.class,orderId,productType,orderId,productType);
			for(Map<String, Object>m:ls){
				String payPrice=m.get("payPrice").toString();
				if(StringUtils.isNotBlank(payPrice)){
					m.put("payPrice", getMoneyStr(payPrice));
				}
				
			}
			return ls;
		}

	private  static String getMoneyStr(String moneyStr){
		moneyStr=moneyStr.replaceAll("-", "del");
		moneyStr=moneyStr.replaceAll("money,", "add");
		moneyStr=moneyStr.replaceAll("adddel", "-");
		moneyStr=moneyStr.replaceAll("add", "+");
		moneyStr=moneyStr.replaceAll("money", "");
		moneyStr=moneyStr.replaceAll("del", "-");
		return moneyStr;
	}
//	public List<Map<Object, Object>>getAllCurrency(Page<Map<Object, Object>> page){
//		String sql="select currency_id,currency_name from currency";
//		page.setOrderBy(" currency_id asc");
//		page.setPageSize(9999);
//		Page<Map<Object, Object>> curs = productorderDao.findBySql(page,sql,Map.class);
//		return curs.getList();
//	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-02-13
	 * 保存团队退款审核信息
	 * @param refundRecords
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> saveGroupRefundReviewInfo(List<RefundBean> refundRecords,Integer productType,Integer flowType,Long orderId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		ProductOrderCommon order = productorderDao.findOne(orderId);
		if(null == order || order.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到订单！");
			return map;
			
		}
		TravelActivity activity = travelActivityDao.findOne(order.getProductId());
		if(null == activity || activity.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到产品！");
			return map;
		}
		
		if(null == activity.getDeptId() || 0 >= activity.getDeptId()){
			map.put("sign", 0);
			map.put("result", "产品没有部门！");
			return map;
		}
		StringBuffer reply = new StringBuffer();
		int sign = 1;
		String refundStr = "";
		int companyId = UserUtils.getUser().getCompany().getId().intValue();
		//添加新行者退款审核新需求判断
		if(companyId == 71){
			int type = reviewService.getOperTotal();
			if( type == 0){
				flowType = 1;
			}else{
				flowType = 16;
			}
		}
		
		Long agentId = order.getOrderCompany();
		String agentName = "";
		if (agentId == -1) {
			agentName = order.getOrderCompanyName();
		} else {
			agentName = agentinfoDao.findOne(agentId).getAgentName();
		}
		
		boolean yubao_locked = true; //预报单是否锁定标识
		boolean jiesuan_locked = true; //结算单是否锁定标识
		ActivityGroup group = activityGroupService.findById(order.getProductGroupId());
		//对预报单状态进行判断
		if (!"10".equals(group.getForcastStatus())) {
			yubao_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		}
		//对结算单状态进行判断
		if (1 != group.getLockStatus()) {
			jiesuan_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		} else {
			map.put("sign", 0);
			map.put("result", "结算单已锁定，不能提交申请");
			if(sign == 0){
				throw new Exception(reply.toString());
			}
		}
		
		for (RefundBean bean : refundRecords) {
			if(sign == 1){
				long rid = reviewService.addReview(
						productType,flowType, orderId+"",
						Long.valueOf(bean.getTravelerId()), Long.valueOf(0),
						bean.getRemark(), reply, getDetail(bean),activity.getDeptId());
				if(rid == 0){
					sign = 0;
				}else{
					refundStr = refundStr + bean.getTravelerName()+":"+bean.getCurrencyMark()+bean.getRefundPrice()+" ";
					costManageService.saveRefundCostRecord(productType, bean, order, agentName, rid, activity.getDeptId(), yubao_locked, jiesuan_locked);
					////////////////////////////////////////////////
					/**
					* add by ruyi.chen
					* add date 2015-05-28
					* describe 过滤低空游轮直接通过审核
					*/
					Review review = reviewService.findReview(rid, false).get(0);
					if(Context.ORDER_STATUS_CRUISE.equals(productType.toString())){
						reviewService.UpdateReview(rid, review.getTopLevel(), 1, "");
						List<MoneyAmount> moneyList = new ArrayList<MoneyAmount>();
						MoneyAmount ma = new MoneyAmount();
						ma.setAmount(BigDecimal.valueOf(Double.valueOf(bean.getRefundPrice())));//款数
						ma.setOrderType(productType);//订单类型 即 产品类型
						ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
						ma.setUid(orderId);//订单id
						ma.setReviewId(rid);//revId
						ma.setCurrencyId(Integer.parseInt(bean.getCurrencyId()));//币种
//						if(traid > 0){//游客id > 0 标示 是游客退款
//							ma.setBusindessType(2);//2标示游客退款
//						} else {
							ma.setBusindessType(1);//1标示订单退款
//						}
						moneyList.add(ma);
						moneyAmountService.saveMoneyAmounts(moneyList);
					}
				}
			}
			
		}
		
		map.put("sign", sign);
		map.put("result", reply.toString());
		if(sign == 0){
			throw new Exception(reply.toString());
		}
		//添加操作日志项
		StringBuffer content = new StringBuffer();
		content.append("订单号为："+order.getOrderNum()+" 的订单 发起退款申请流程,")
		.append("具体退款信息为【"+refundStr+"】");
		this.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, content.toString(), Context.log_state_add, order.getOrderStatus(), order.getId());
		return map;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-06-18
	 * 保存海岛游团队退款审核信息
	 * @param refundRecords
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> saveIslandGroupRefundReviewInfo(List<RefundBean> refundRecords,Integer productType,Integer flowType,Long orderId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		IslandOrder order = islandOrderService.getById(orderId.intValue());
		if(null == order || order.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到订单！");
			return map;
			
		}
		ActivityIsland activity = activityIslandService.getByUuid(order.getActivityIslandUuid());
		if(null == activity || activity.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到产品！");
			return map;
		}
		
		if(null == activity.getDeptId() || 0 >= activity.getDeptId()){
			map.put("sign", 0);
			map.put("result", "产品没有部门！");
			return map;
		}
		StringBuffer reply = new StringBuffer();
		int sign = 1;
		String refundStr = "";
		int companyId = UserUtils.getUser().getCompany().getId().intValue();
		//添加新行者退款审核新需求判断
		if(companyId == 71){
			int type = reviewService.getOperTotal();
			if( type == 0){
				flowType = 1;
			}else{
				flowType = 16;
			}
		}
		
		Long agentId = order.getOrderCompany().longValue();
		String agentName = "";
		if (agentId == -1) {
			agentName = order.getOrderCompanyName();
		} else {
//			agentName = agentinfoDao.findOne(agentId).getAgentName();
			agentName = order.getOrderCompanyName();
		}
		
		boolean yubao_locked = true; //预报单是否锁定标识
		boolean jiesuan_locked = true; //结算单是否锁定标识
		ActivityIslandGroup group = activityIslandGroupService.getByUuid(order.getActivityIslandGroupUuid());		
		
		//对预报单状态进行判断
		if (!"10".equals(group.getForcastStatus())) {
			yubao_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		}
		//对结算单状态进行判断
		if (1 != group.getLockStatus()) {
			jiesuan_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		} else {
			map.put("sign", 0);
			map.put("result", "结算单已锁定，不能提交申请");
			if(sign == 0){
				throw new Exception(reply.toString());
			}
		}
		
		for (RefundBean bean : refundRecords) {
			bean.setCreateBy(UserUtils.getUser().getId().toString());
			if(sign == 1){
				long rid = reviewService.addSuccessReview(
						productType,flowType, orderId+"",
						Long.valueOf(bean.getTravelerId()), Long.valueOf(0),
						bean.getRemark(), reply, getDetail(bean),activity.getDeptId().longValue());
				if(rid == 0){
					sign = 0;
				}else{
					refundStr = refundStr + bean.getTravelerName()+":"+bean.getCurrencyMark()+bean.getRefundPrice()+" ";
					costManageService.saveIslandRefundCostRecord(productType, bean, order, agentName, rid, activity.getDeptId().longValue(), yubao_locked, jiesuan_locked);
					////////////////////////////////////////////////
					/**
					* add by ruyi.chen
					* add date 2015-06-18
					* describe 过滤海岛游退款申请直接通过审核
					*/
//					Review review = reviewService.findReview(rid, false).get(0);
					if(Context.ORDER_TYPE_ISLAND.toString().equals(productType.toString())){
//						reviewService.UpdateReviewLast(rid, review.getTopLevel(), 1, "");
						IslandMoneyAmount ma = new IslandMoneyAmount();
						ma.setAmount(Double.valueOf(bean.getRefundPrice()));//款数
						ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
						ma.setBusinessUuid(order.getUuid());//订单uuid
						ma.setReviewId((int)rid);//revId
						ma.setCurrencyId(Integer.parseInt(bean.getCurrencyId()));//币种
//						if(traid > 0){//游客id > 0 标示 是游客退款
//							ma.setBusindessType(2);//2标示游客退款
//						} else {
							ma.setBusinessType(2);//1标示订单退款
//						}
						islandMoneyAmountService.save(ma);
					}
				}
			}
			
		}
		
		map.put("sign", sign);
		map.put("result", reply.toString());
		if(sign == 0){
			throw new Exception(reply.toString());
		}
		//添加操作日志项
		StringBuffer content = new StringBuffer();
		content.append("订单号为："+order.getOrderNum()+" 的订单 发起退款申请流程,")
		.append("具体退款信息为【"+refundStr+"】");
		this.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, content.toString(), Context.log_state_add, order.getOrderStatus(), order.getId().longValue());
		return map;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-06-18
	 * 保存海岛游借款审核信息
	 * @param refundRecords
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> saveIslandBorrowing(List<BorrowingBean> refundRecords,Integer productType,Integer flowType,Long orderId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		IslandOrder order = islandOrderService.getById(orderId.intValue());
		if(null == order || order.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到订单！");
			return map;
			
		}
		ActivityIsland activity = activityIslandService.getByUuid(order.getActivityIslandUuid());
		if(null == activity || activity.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到产品！");
			return map;
		}
		
		if(null == activity.getDeptId() || 0 >= activity.getDeptId()){
			map.put("sign", 0);
			map.put("result", "产品没有部门！");
			return map;
		}
		StringBuffer reply = new StringBuffer();
		int sign = 1;
		String refundStr = "";
		int companyId = UserUtils.getUser().getCompany().getId().intValue();
		//添加新行者退款审核新需求判断
		if(companyId == 71){
			int type = reviewService.getOperTotal();
			if( type == 0){
				flowType = 1;
			}else{
				flowType = 16;
			}
		}
		
		Long agentId = order.getOrderCompany().longValue();
//		String agentName = "";
		if (agentId == -1) {
//			agentName = order.getOrderCompanyName();
		} else {
//			agentName = agentinfoDao.findOne(agentId).getAgentName();
//			agentName = order.getOrderCompanyName();
		}
		
//		boolean yubao_locked = true; //预报单是否锁定标识
//		boolean jiesuan_locked = true; //结算单是否锁定标识
		ActivityIslandGroup group = activityIslandGroupService.getByUuid(order.getActivityIslandGroupUuid());		
		
		//对预报单状态进行判断
		if (!"10".equals(group.getForcastStatus())) {
//			yubao_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		}
		//对结算单状态进行判断
		if (1 != group.getLockStatus()) {
//			jiesuan_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		} else {
			map.put("sign", 0);
			map.put("result", "结算单已锁定，不能提交申请");
			if(sign == 0){
				throw new Exception(reply.toString());
			}
		}
		
		for (BorrowingBean bean : refundRecords) {
			bean.setCreateBy(UserUtils.getUser().getId());
			if(sign == 1){
				long rid = reviewService.addSuccessReview(
						productType,flowType, orderId+"",
						Long.valueOf(bean.getTravelerId()), Long.valueOf(0),
						bean.getRemark(), reply, getDetail(bean),activity.getDeptId().longValue());
				if(rid == 0){
					sign = 0;
				}else{
					refundStr = refundStr + bean.getTravelerName()+":"+bean.getCurrencyMark()+bean.getLendPrice()+" ";
					////////////////////////////////////////////////
					/**
					* add by ruyi.chen
					* add date 2015-06-18
					* describe 过滤海岛游借款申请直接通过审核
					*/
					Review review = reviewService.findReview(rid, false).get(0);
					if(Context.ORDER_TYPE_ISLAND.toString().equals(productType.toString())){
//						reviewService.UpdateReviewLast(rid, review.getTopLevel(), 1, "");
//						boolean flag = false;
						try {
							 airTicketOrderLendMoneyService.saveIslandLendMoney2MoneyAmount(rid, review.getOrderId(), review.getProductType(),order.getUuid());
						} catch (Exception e) {
							map.put("sign", 0);
							map.put("result", "保存错误！");
							return map;
						}
					}
				}
			}
			
		}
		
		map.put("sign", sign);
		map.put("result", reply.toString());
		if(sign == 0){
			throw new Exception(reply.toString());
		}
		//添加操作日志项
		StringBuffer content = new StringBuffer();
		content.append("订单号为："+order.getOrderNum()+" 的订单 发起借款申请流程,")
		.append("具体借款信息为【"+refundStr+"】");
		this.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, content.toString(), Context.log_state_add, order.getOrderStatus(), order.getId().longValue());
		return map;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-06-18
	 * 保存酒店借款审核信息
	 * @param refundRecords
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> saveHotelBorrowing(List<BorrowingBean> refundRecords,Integer productType,Integer flowType,Long orderId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		HotelOrder order = hotelOrderService.getById(orderId.intValue());
		if(null == order || order.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到订单！");
			return map;
			
		}
		ActivityHotel activity = activityHotelService.getByUuid(order.getActivityHotelUuid());
		if(null == activity || activity.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到产品！");
			return map;
		}
		
		if(null == activity.getDeptId() || 0 >= activity.getDeptId()){
			map.put("sign", 0);
			map.put("result", "产品没有部门！");
			return map;
		}
		StringBuffer reply = new StringBuffer();
		int sign = 1;
		String refundStr = "";
		int companyId = UserUtils.getUser().getCompany().getId().intValue();
		//添加新行者退款审核新需求判断
		if(companyId == 71){
			int type = reviewService.getOperTotal();
			if( type == 0){
				flowType = 1;
			}else{
				flowType = 16;
			}
		}
		
		Long agentId = order.getOrderCompany().longValue();
//		String agentName = "";
		if (agentId == -1) {
//			agentName = order.getOrderCompanyName();
		} else {
//			agentName = agentinfoDao.findOne(agentId).getAgentName();
//			agentName = order.getOrderCompanyName();
		}
		
//		boolean yubao_locked = true; //预报单是否锁定标识
//		boolean jiesuan_locked = true; //结算单是否锁定标识
		ActivityHotelGroup group = activityHotelGroupService.getByUuid(order.getActivityHotelGroupUuid());		
		
		//对预报单状态进行判断
		if (!"10".equals(group.getForcastStatus())) {
//			yubao_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		}
		//对结算单状态进行判断
		if (1 != group.getLockStatus()) {
//			jiesuan_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		} else {
			map.put("sign", 0);
			map.put("result", "结算单已锁定，不能提交申请");
			if(sign == 0){
				throw new Exception(reply.toString());
			}
		}
		
		for (BorrowingBean bean : refundRecords) {
			bean.setCreateBy(UserUtils.getUser().getId());
			if(sign == 1){
				long rid = reviewService.addSuccessReview(
						productType,flowType, orderId+"",
						Long.valueOf(bean.getTravelerId()), Long.valueOf(0),
						bean.getRemark(), reply, getDetail(bean),activity.getDeptId().longValue());
				if(rid == 0){
					sign = 0;
				}else{
					refundStr = refundStr + bean.getTravelerName()+":"+bean.getCurrencyMark()+bean.getLendPrice()+" ";
					////////////////////////////////////////////////
					/**
					* add by ruyi.chen
					* add date 2015-06-18
					* describe 过滤酒店借款申请直接通过审核
					*/
					Review review = reviewService.findReview(rid, false).get(0);
					if(Context.ORDER_TYPE_HOTEL.toString().equals(productType.toString())){
//						reviewService.UpdateReviewLast(rid, review.getTopLevel(), 1, "");
//						boolean flag = false;
						try {
							 airTicketOrderLendMoneyService.saveHotelLendMoney2MoneyAmount(rid, review.getOrderId(), review.getProductType(),order.getUuid());
						} catch (Exception e) {
							map.put("sign", 0);
							map.put("result", "保存错误！");
							return map;
						}
					}
				}
			}
			
		}
		
		map.put("sign", sign);
		map.put("result", reply.toString());
		if(sign == 0){
			throw new Exception(reply.toString());
		}
		//添加操作日志项
		StringBuffer content = new StringBuffer();
		content.append("订单号为："+order.getOrderNum()+" 的订单 发起借款申请流程,")
		.append("具体借款信息为【"+refundStr+"】");
		this.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, content.toString(), Context.log_state_add, order.getOrderStatus(), order.getId().longValue());
		return map;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-06-18
	 * 保存酒店团队退款审核信息
	 * @param refundRecords
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> saveHotelGroupRefundReviewInfo(List<RefundBean> refundRecords,Integer productType,Integer flowType,Long orderId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		HotelOrder order = hotelOrderService.getById(orderId.intValue());
		if(null == order || order.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到订单！");
			return map;
			
		}
		ActivityHotel activity = activityHotelService.getByUuid(order.getActivityHotelUuid());
		if(null == activity || activity.getId() == 0){
			map.put("sign", 0);
			map.put("result", "未找到产品！");
			return map;
		}
		
		if(null == activity.getDeptId() || 0 >= activity.getDeptId()){
			map.put("sign", 0);
			map.put("result", "产品没有部门！");
			return map;
		}
		StringBuffer reply = new StringBuffer();
		int sign = 1;
		String refundStr = "";
		int companyId = UserUtils.getUser().getCompany().getId().intValue();
		//添加新行者退款审核新需求判断
		if(companyId == 71){
			int type = reviewService.getOperTotal();
			if( type == 0){
				flowType = 1;
			}else{
				flowType = 16;
			}
		}
		
		Long agentId = order.getOrderCompany().longValue();
		String agentName = "";
		if (agentId == -1) {
			agentName = order.getOrderCompanyName();
		} else {
//			agentName = agentinfoDao.findOne(agentId).getAgentName();
			agentName = order.getOrderCompanyName();
		}
		
		boolean yubao_locked = true; //预报单是否锁定标识
		boolean jiesuan_locked = true; //结算单是否锁定标识
		ActivityHotelGroup group = activityHotelGroupService.getByUuid(order.getActivityHotelGroupUuid());		
		
		//对预报单状态进行判断
		if (!"10".equals(group.getForcastStatus())) {
			yubao_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		}
		//对结算单状态进行判断
		if (1 != group.getLockStatus()) {
			jiesuan_locked = false;//true 改为了 false by chy 2015年6月8日17:38:17
		} else {
			map.put("sign", 0);
			map.put("result", "结算单已锁定，不能提交申请");
			if(sign == 0){
				throw new Exception(reply.toString());
			}
		}
		
		for (RefundBean bean : refundRecords) {
			bean.setCreateBy(UserUtils.getUser().getId().toString());
			if(sign == 1){
				long rid = reviewService.addSuccessReview(
						productType,flowType, orderId+"",
						Long.valueOf(bean.getTravelerId()), Long.valueOf(0),
						bean.getRemark(), reply, getDetail(bean),activity.getDeptId().longValue());
				if(rid == 0){
					sign = 0;
				}else{
					refundStr = refundStr + bean.getTravelerName()+":"+bean.getCurrencyMark()+bean.getRefundPrice()+" ";
					costManageService.saveHotelRefundCostRecord(productType, bean, order, agentName, rid, activity.getDeptId().longValue(), yubao_locked, jiesuan_locked);
					////////////////////////////////////////////////
					/**
					* add by ruyi.chen
					* add date 2015-06-18
					* describe 过滤酒店退款申请直接通过审核
					*/
//					Review review = reviewService.findReview(rid, false).get(0);
					if(Context.ORDER_TYPE_HOTEL.toString().equals(productType.toString())){
//						reviewService.UpdateReviewLast(rid, review.getTopLevel(), 1, "");
						HotelMoneyAmount ma = new HotelMoneyAmount();
						ma.setAmount(Double.valueOf(bean.getRefundPrice()));//款数
						ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
						ma.setBusinessUuid(order.getUuid());//订单uuid
						ma.setReviewId((int)rid);//revId
						ma.setCurrencyId(Integer.parseInt(bean.getCurrencyId()));//币种
//						if(traid > 0){//游客id > 0 标示 是游客退款
//							ma.setBusindessType(2);//2标示游客退款
//						} else {
							ma.setBusinessType(2);//1标示订单退款
//						}
						hotelMoneyAmountService.save(ma);
					}
				}
			}
			
		}
		
		map.put("sign", sign);
		map.put("result", reply.toString());
		if(sign == 0){
			throw new Exception(reply.toString());
		}
		//添加操作日志项
		StringBuffer content = new StringBuffer();
		content.append("订单号为："+order.getOrderNum()+" 的订单 发起退款申请流程,")
		.append("具体退款信息为【"+refundStr+"】");
		this.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, content.toString(), Context.log_state_add, order.getOrderStatus(), order.getId().longValue());
		return map;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-02-13
	 * 退款实体类获取相应的审核业务数据
	 * @param bean
	 * @return
	 */
	private List<Detail> getDetail(RefundBean bean) {
		List<Detail> detailList = new ArrayList<Detail>();

		Map<String, String> map = bean.getReviewDetailMap();
		for (Entry<String, String> entry : map.entrySet()) {
			detailList.add(new Detail(entry.getKey(), entry.getValue()));
		}

		return detailList;
	}
	private List<Detail> getDetail(BorrowingBean bean) {
		List<Detail> detailList = new ArrayList<Detail>();

		Map<String, String> map = bean.getReviewDetailMap();
		for (Entry<String, String> entry : map.entrySet()) {
			detailList.add(new Detail(entry.getKey(), entry.getValue()));
		}

		return detailList;
	}
	/**
	 * 保存退团申请信息
	 * add by ruyi.chen
	 * create date 2014-11-10
	 * @param productType
	 * @param flowType
	 * @param travelerId
	 * @param exitReason
	 * @param orderId
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Map<String,Object> saveExitGroupReviewInfo(Integer productType,Integer flowType,Long[]travelerId,String[]travelerName,String[] exitReason, Long orderId,String[] subtractMoneyArr) throws Exception{
//		reviewService.removeOrderAllReview(orderId, productType);
		Map<String,Object> map = new HashMap<String,Object>();
		ProductOrderCommon order = productorderDao.findOne(orderId);
		if (null == order || order.getId() == 0) {
			map.put("sign", 0);
			map.put("result", "未找到订单！");
			return map;
		} 
		TravelActivity activity = travelActivityDao.findOne(order.getProductId());
		if (null == activity || activity.getId() == 0) {
			map.put("sign", 0);
			map.put("result", "未找到产品！");
			return map;
		}
		
		if (null == activity.getDeptId() || 0 == activity.getDeptId().intValue()) {
			map.put("sign", 0);
			map.put("result", "产品没有部门！");
			return map;
		}
		
		//修改游客扣减金额
		Map<Long, String> subtractMoneyMap = Maps.newHashMap();
		if (subtractMoneyArr != null && subtractMoneyArr.length > 0) {
			List<MoneyAmount> moneyAmountList = new ArrayList<MoneyAmount>();
			
			for (int j = 0; j<subtractMoneyArr.length; j++) {
				String uuid = UuidUtils.generUuid();
				String currencyId_money =  subtractMoneyArr[j];
				String[] moneyArr = currencyId_money.split("#");
				if (moneyArr != null && moneyArr.length == 3) {
					MoneyAmount ma = new MoneyAmount();
					ma.setCurrencyId(Integer.valueOf(moneyArr[1]));
					ma.setAmount(new BigDecimal(moneyArr[2]));
					ma.setSerialNum(uuid);
					
					moneyAmountList.add(ma);
					moneyAmountService.saveMoneyAmounts(moneyAmountList);
					travelerDao.updateTravelerSubtractMoneySerialNum(uuid, Long.parseLong(moneyArr[0].toString()));
					subtractMoneyMap.put(Long.parseLong(moneyArr[0].toString()), uuid);
				}
			}
			
		}
		
		int sign = 1;
		StringBuffer sbf = new StringBuffer();
		String travelerNames = "";
		for (int i=0;i<travelerId.length;i++) {
			if (sign == 1) {
				Traveler traveler = travelerDao.findById(travelerId[i]);
				if (null != traveler && traveler.getId() > 0) {
					List<Detail> detailList = new ArrayList<Detail>();
					detailList.add(new Detail("travelerId", travelerId[i].toString()));
					detailList.add(new Detail("travelerName", travelerName[i]));
					detailList.add(new Detail("exitReason", exitReason[i]));
					detailList.add(new Detail("payPrice", moneyAmountService.getMoney(traveler.getPayPriceSerialNum())));
					detailList.add(new Detail("subtractMoney", moneyAmountService.getMoney(subtractMoneyMap.get(traveler.getId()))));
					travelerNames = travelerNames + travelerName[i]+" ";
					long rid = reviewService.addReview(productType, flowType, orderId+"", travelerId[i], (long)0, exitReason[i],sbf, detailList,activity.getDeptId());
					
					if (rid > 0 && sign == 1) {
						travelerDao.updateTravelerDelFlag(Context.TRAVELER_DELFLAG_EXIT, travelerId[i]);
					} else {
						sign = 0;
					}
				} else {
					sign = 0;
					throw new Exception("找不到游客信息！");
				}
			}
		}
		map.put("sign", sign);
		map.put("result", sbf.toString());
		if (sign == 0) {
			throw new Exception(sbf.toString());
		}
		//添加操作日志项
		StringBuffer content = new StringBuffer();
		content.append("订单号为："+order.getOrderNum()+" 的订单 发起退团申请流程,")
		.append("具体退团游客有【"+travelerNames+"】");
		this.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, content.toString(), 1 + "", order.getOrderStatus(), order.getId().longValue());
		return map;
	}
	/**获取退团审核信息列表
	 * add by ruyi.chen
	 * 2014-12-15
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @return
	 */
	public List<Map<String, String>> getExitGroupReviewInfo(Integer productType,Integer flowType,long orderId) {		
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType, flowType, orderId + "", false);
		String checksql = "";
		if (reviewMapList.size() > 0) {
			for (Map<String,String>m:reviewMapList) {
				checksql = checksql+m.get("travelerId") + ",";
			}
		}
		if (!"".equals(checksql) && null != checksql) {
			checksql = checksql.substring(0, checksql.length()-1);
			checksql = "(" + checksql + ")";
			List<Map<Object,Object>> map = getTravelerInfoForReview(checksql);
			for (Map<String,String> m : reviewMapList) {
				for (Map<Object,Object> travelerMap : map) {
					if (m.get("travelerId").equals(travelerMap.get("travelerId").toString())) {
						m.put("orderTime", travelerMap.get("orderTime") + "");
						//游客结算价
//						if (travelerMap.get("payPriceSerialNum") != null) {
//							m.put("payPrice", moneyAmountService.getMoneyStr(travelerMap.get("payPriceSerialNum").toString()));
//						} else {
//							m.put("payPrice", "");
//						}
					}
				}
			}
		}	
		return reviewMapList;
	}
	/**获取退团审核详情
	 * add by ruyi.chen
	 * 2014-12-15
	 * @param rid
	 * @return
	 */
	public Map<String, Object> getExitGroupReviewInfoById(long rid){		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<Review> list=reviewService.findReview(rid, false);
		if(list.size()>0){
			resultMap.put("reviewInfo", list.get(0));
			String checksql="";
			checksql="("+list.get(0).getTravelerId()+")";
			List<Map<Object,Object>> map=getTravelerInfoForReview(checksql);
			Map<Object,Object>mapResult=map.get(0);
			String payPrice=mapResult.get("payPrice").toString();
			mapResult.put("payPrice", getMoneyStr(payPrice));
			resultMap.put("travelerInfo",mapResult);
			List<ReviewLog> rLog=reviewService.findReviewLog(rid);
			resultMap.put("reviewLogInfo", rLog);
		}
	
		return resultMap;
	}
	/**获取切位退款审核详情
	 * add by ruyi.chen
	 * 2015-01-08
	 * @param rid
	 * @return
	 */
	public Map<String, Object> getReserveRefundInfoById(long rid){		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		Map<String,String>reviewInfo=reviewService.findReview(rid);
			resultMap.put("reviewInfo", reviewInfo);
//			String checksql="";
//			checksql="("+list.get(0).getTravelerId()+")";
//			List<Map<Object,Object>> map=getTravelerInfoForReview(checksql);
//			Map<Object,Object>mapResult=map.get(0);
//			String payPrice=mapResult.get("payPrice").toString();
//			mapResult.put("payPrice", getMoneyStr(payPrice));
//			resultMap.put("travelerInfo",mapResult);
			List<ReviewLog> rLog=reviewService.findReviewLog(rid);
			resultMap.put("reviewLogInfo", rLog);
	
		return resultMap;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-01-05
	 * 获取退款审核详情
	 * @param rid
	 * @return
	 */
	public Map<String,Object>getReviewRefundInfo(long rid){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		
		Map<String,String>map=reviewService.findReview(rid);
		if(null!=map&&map.size()>0){
			resultMap.put("reviewInfo",map);
			String travelerId=map.get("travelerId");
			if(null!=travelerId&&Long.parseLong(travelerId)>0){
				String checksql="("+travelerId+")";
				List<Map<Object,Object>> m=getTravelerInfoForReview(checksql);
				Map<Object,Object>mapResult=m.get(0);
				String payPrice=mapResult.get("payPrice").toString();
				mapResult.put("payPrice", getMoneyStr(payPrice));
				resultMap.put("travelerInfo",mapResult);
			}else{
				resultMap.put("travelerInfo", null);
			}
			List<ReviewLog> rLog=reviewService.findReviewLog(rid);
			resultMap.put("reviewLogInfo", rLog);
		}
		return resultMap;
		
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-02-13
	 * 获取审核部分所需游客信息(根据审核查询条件)
	 * @param checksql
	 * @return
	 */
	private List<Map<Object,Object>> getTravelerInfoForReview(String checksql) {
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT p.orderTime, tt.id as travelerId, tt.orderId, tt.name, tt.payPriceSerialNum, tt.personType, tt.subtract_moneySerialNum, ")
		.append("IFNULL(a.accounted_money,0) as payPrice FROM traveler tt LEFT JOIN productorder p on tt.orderId= p.id ")
		.append(" LEFT JOIN(select t.id,t.orderId,t.payPriceSerialNum,group_concat(format(IFNULL(ma.amount, 0),2),c.currency_name,'money'")
		.append(" ORDER BY c.currency_id) AS accounted_money from traveler t LEFT JOIN money_amount ma on t.payPriceSerialNum = ma.serialNum ")
		.append(" and t.id in " + checksql + " inner JOIN currency c on ma.currencyId= c.currency_id ")
		.append("GROUP BY t.id)a on tt.id = a.id  WHERE tt.id in " + checksql);
		return productorderDao.findBySql(sbf.toString(), Map.class);
	}
	public Map<String,Object>getHotelReviewRefundInfo(long rid){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		
		Map<String,String>map=reviewService.findReview(rid);
		if(null!=map&&map.size()>0){
			resultMap.put("reviewInfo",map);
			String travelerId=map.get("travelerId");
			if(null!=travelerId&&Long.parseLong(travelerId)>0){
				String checksql="("+travelerId+")";
				List<Map<Object,Object>> m=getHotelTravelerInfoForReview(checksql);
				Map<Object,Object>mapResult=m.get(0);
				String payPrice=mapResult.get("payPrice").toString();
				mapResult.put("payPrice", getMoneyStr(payPrice));
				resultMap.put("travelerInfo",mapResult);
			}else{
				resultMap.put("travelerInfo", null);
			}
			List<ReviewLog> rLog=reviewService.findReviewLog(rid);
			resultMap.put("reviewLogInfo", rLog);
		}
		return resultMap;
		
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-02-13
	 * 获取审核部分所需游客信息(根据审核查询条件)
	 * @param checksql
	 * @return
	 */
	private List<Map<Object,Object>>getHotelTravelerInfoForReview(String checksql){
		StringBuffer sbf=new StringBuffer();
		sbf
		.append("SELECT p.orderTime, tt.id as travelerId,tt.name,tt.payPriceSerialNum,tt.personType,")
		.append("IFNULL(a.accounted_money,0) as payPrice from  hotel_traveler tt LEFT JOIN hotel_order p on tt.order_uuid= p.uuid ")
		.append(" LEFT JOIN(select t.id,t.payPriceSerialNum,group_concat(format(IFNULL(ma.amount, 0),2),c.currency_name,'money'")
		.append(" ORDER BY c.currency_id) AS accounted_money from hotel_traveler t LEFT JOIN hotel_money_amount ma on t.payPriceSerialNum=ma.serialNum ")
		.append(" and t.id in "+checksql+" inner JOIN currency c on ma.currencyId= c.currency_id ")
		.append("GROUP BY t.id)a on tt.id=a.id  WHERE tt.id in "+checksql);
		return productorderDao.findBySql(sbf.toString(), Map.class);
	}
	public Map<String,Object>getIslandReviewRefundInfo(long rid){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		
		Map<String,String>map=reviewService.findReview(rid);
		if(null!=map&&map.size()>0){
			resultMap.put("reviewInfo",map);
			String travelerId=map.get("travelerId");
			if(null!=travelerId&&Long.parseLong(travelerId)>0){
				String checksql="("+travelerId+")";
				List<Map<Object,Object>> m=getIslandTravelerInfoForReview(checksql);
				Map<Object,Object>mapResult=m.get(0);
				String payPrice=mapResult.get("payPrice").toString();
				mapResult.put("payPrice", getMoneyStr(payPrice));
				resultMap.put("travelerInfo",mapResult);
			}else{
				resultMap.put("travelerInfo", null);
			}
			List<ReviewLog> rLog=reviewService.findReviewLog(rid);
			resultMap.put("reviewLogInfo", rLog);
		}
		return resultMap;
		
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-02-13
	 * 获取审核部分所需游客信息(根据审核查询条件)
	 * @param checksql
	 * @return
	 */
	private List<Map<Object,Object>>getIslandTravelerInfoForReview(String checksql){
		StringBuffer sbf=new StringBuffer();
		sbf
		.append("SELECT p.orderTime, tt.id as travelerId,tt.name,tt.payPriceSerialNum,tt.personType,")
		.append("IFNULL(a.accounted_money,0) as payPrice from  island_traveler tt LEFT JOIN island_order p on tt.order_uuid= p.uuid ")
		.append(" LEFT JOIN(select t.id,t.payPriceSerialNum,group_concat(format(IFNULL(ma.amount, 0),2),c.currency_name,'money'")
		.append(" ORDER BY c.currency_id) AS accounted_money from island_traveler t LEFT JOIN island_money_amount ma on t.payPriceSerialNum=ma.serialNum ")
		.append(" and t.id in "+checksql+" inner JOIN currency c on ma.currencyId= c.currency_id ")
		.append("GROUP BY t.id)a on tt.id=a.id  WHERE tt.id in "+checksql);
		return productorderDao.findBySql(sbf.toString(), Map.class);
	}
	/**
	 * update by ruyi.chen
	 * 2015-02-03
	 * 获取退款记录列表(增加批次审核)
	 * @param productType
	 * @param flowType
	 * @param orderId
	 * @param sign(批次审核是否完毕，0：全部结束，1：未结束)
	 * @return
	 */
	public LinkedHashMap<String, List<RefundBean>> getGroupRefundReviewInfo(Integer productType,Integer flowType,long orderId,StringBuffer sign){
		
		List<Map<String, String>> reviewMapList = reviewService
				.findReviewListMap(productType,flowType, orderId+"", false);
		List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
		if(null != reviewList && 0 < reviewList.size()){
			for( RefundBean bean :reviewList){
				if(1 == bean.getStatus()){
					sign.append("审核中");
				}
			}
		}
		return getRefundBeanShowMap(reviewList);
	}
	
	/**
	 * add by ruyi.chen 
	 * add date 2015-02-13
	 * 退款记录列表分人展示处理
	 * @param reviewList
	 * @return
	 */
	private LinkedHashMap<String, List<RefundBean>> getRefundBeanShowMap(
			List<RefundBean> reviewList) {
		LinkedHashMap<String, List<RefundBean>> map = new LinkedHashMap<String, List<RefundBean>>();
		// 根据游客id排序
		Collections.sort(reviewList, new Comparator<RefundBean>() {
			@Override
			public int compare(RefundBean o1, RefundBean o2) {
				if (Integer.valueOf(o1.getReviewId()) > Integer.valueOf(o2
						.getReviewId())) {
					return 1;
				} else if (Integer.valueOf(o1.getReviewId()) < Integer
						.valueOf(o2.getReviewId())) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		String key = null;
		List<RefundBean> aList = null;
		for (RefundBean bean : reviewList) {
			key = bean.getReviewId();
			aList = map.get(key);
			if (aList == null) {
				aList = new ArrayList<RefundBean>();
				map.put(key, aList);
			}
			aList.add(bean);
		}
		return map;
	}

	/**
	 * 获取退款bean列表
	 * 
	 * @param reviewMapList
	 * @return
	 */
	private List<RefundBean> getRefundBeanList(
			List<Map<String, String>> reviewMapList) {
		List<RefundBean> aList = new ArrayList<RefundBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, String> map : reviewMapList) {
			String orderId=map.get("orderId");
			String travelerId=map.get("travelerId");
			if(StringUtils.isNotBlank(orderId)&&StringUtils.isNotBlank(travelerId)){
				if(Long.parseLong(travelerId)>0){
					Traveler traveler=travelerDao.findById(Long.parseLong(travelerId));
					if(null!=traveler&&traveler.getId()>0){
						String payPriceSerialNum=traveler.getPayPriceSerialNum();
						String moneyStr=moneyAmountService.getMoneyStr(payPriceSerialNum);
						map.put("payPrice", moneyStr);
					}
					
				}else{
					ProductOrderCommon order=productorderDao.findOne(Long.parseLong(orderId));
					if(order.getId()>0){
						String totalMoney=order.getTotalMoney();
						String moneyStr=moneyAmountService.getMoneyStr(totalMoney);
						map.put("payPrice", moneyStr);
					}
				}
			}
		}
		for (Map<String, String> map : reviewMapList) {
			aList.add(new RefundBean(map));
		}
		return aList;
	}
	 
	 /**
	  * 生成或更新签证子订单
	  * @param orderId 单团订单id
	  */
	 @Transactional(readOnly=false, rollbackFor=Exception.class)
	 public void createSingleSubOrder(Long orderId) {
		 
		 //根据订单id查询订单
		 ProductOrderCommon productOrderCommon = productorderDao.findOne(orderId);
		 //根据订单id和订单类型查询游客列表
		 List<Traveler> travelerList = travelerDao.findTravelerByOrderIdAndOrderType(orderId, productOrderCommon.getOrderStatus());
		 
		 for (Traveler traveler: travelerList) {
			 //查询游客上传资料，并放到map中
			 Map<String, Long> travelerDocInfoIds = new HashMap<String, Long>();
			 
			 // 护照首页
			 List<TravelerFile> passportPhotoList = travelerFileDao.findBySrcTravelerIdAndFileType(traveler.getId(), TravelerFile.PASSPORTS_TYPE);
			 if (CollectionUtils.isNotEmpty(passportPhotoList)) {
				 travelerDocInfoIds.put("passport_photo_id", passportPhotoList.get(0).getSrcDocId());
			 } else {
				 travelerDocInfoIds.put("passport_photo_id", null);
			 }
			 
			 // 电子照片
			 List<TravelerFile> personPhotoList = travelerFileDao.findBySrcTravelerIdAndFileType(traveler.getId(), TravelerFile.PHOTO_TYPE);
			 if (CollectionUtils.isNotEmpty(personPhotoList)) {
				 travelerDocInfoIds.put("person_photo_id", personPhotoList.get(0).getSrcDocId());
			 } else {
				 travelerDocInfoIds.put("person_photo_id", null);
			 }
			 
			 // 身份证正面
			 List<TravelerFile> identityFrontList = travelerFileDao.findBySrcTravelerIdAndFileType(traveler.getId(), TravelerFile.IDCARD_FRONT_TYPE);
			 if (CollectionUtils.isNotEmpty(identityFrontList)) {
				 travelerDocInfoIds.put("identity_front_photo_id", identityFrontList.get(0).getSrcDocId());
			 } else {
				 travelerDocInfoIds.put("identity_front_photo_id", null);
			 }
			 
			 // 身份证反面
			 List<TravelerFile> identityBackList = travelerFileDao.findBySrcTravelerIdAndFileType(traveler.getId(), TravelerFile.IDCARD_BACK_TYPE);
			 if (CollectionUtils.isNotEmpty(identityBackList)) {
				 travelerDocInfoIds.put("identity_back_photo_id", identityBackList.get(0).getSrcDocId());
			 } else {
				 travelerDocInfoIds.put("identity_back_photo_id", null);
			 }
			 
			 // 申请表格
			 List<TravelerFile> tablePhotoList = travelerFileDao.findBySrcTravelerIdAndFileType(traveler.getId(), TravelerFile.ENTRY_FORM_TYPE);
			 if (CollectionUtils.isNotEmpty(tablePhotoList)) {
				 travelerDocInfoIds.put("table_photo_id", tablePhotoList.get(0).getSrcDocId());
			 } else {
				 travelerDocInfoIds.put("table_photo_id", null);
			 }
			 
			 // 房产证
			 List<TravelerFile> housePhotoList = travelerFileDao.findBySrcTravelerIdAndFileType(traveler.getId(), TravelerFile.HOUSE_TYPE);
			 if (CollectionUtils.isNotEmpty(housePhotoList)) {
				 travelerDocInfoIds.put("house_photo_id", housePhotoList.get(0).getSrcDocId());
			 } else {
				 travelerDocInfoIds.put("house_photo_id", null);
			 }
			 
			 // 户口本
			 List<TravelerFile> residencePhotoList = travelerFileDao.findBySrcTravelerIdAndFileType(traveler.getId(), TravelerFile.RESIDENCE_TYPE);
			 if (CollectionUtils.isNotEmpty(residencePhotoList)) {
				 travelerDocInfoIds.put("residence_photo_id", residencePhotoList.get(0).getSrcDocId());
			 } else {
				 travelerDocInfoIds.put("residence_photo_id", null);
			 }
			 
			 // 其他
			 List<TravelerFile> otherPhotoList = travelerFileDao.findBySrcTravelerIdAndFileType(traveler.getId(), TravelerFile.OTHER_TYPE);
			 if (CollectionUtils.isNotEmpty(otherPhotoList)) {
				 travelerDocInfoIds.put("other_photo_id", otherPhotoList.get(0).getSrcDocId());
			 } else {
				 travelerDocInfoIds.put("other_photo_id", null);
			 }
			 
			 //根据游客id查询游客签证资料信息
			 List<TravelerVisa> travelerVisaList = travelerVisaDao.findApplyVisaListByTravelerId(traveler.getId());
			 
			 for (TravelerVisa travelerVisa : travelerVisaList) {
				 if (travelerVisa.getApplyCountry() != null && travelerVisa.getManorId() != null && travelerVisa.getVisaTypeId() != null) {
					 
					 //根据国家、签证类型、领区查询签证产品
					 VisaProducts visaProducts = null;
					 Integer countryId = CountryUtils.getCountryId(travelerVisa.getApplyCountry().getName()).getId().intValue();
					 Integer visaType = travelerVisa.getVisaTypeId();
					 String collarZoning = travelerVisa.getManorId().toString();
					 List<VisaProducts> visaProductsList = visaProductsDao.findVisaProductsByCountryTypeCollarZonID(countryId,
							 visaType, collarZoning, UserUtils.getUser().getCompany().getId());
					 if (CollectionUtils.isNotEmpty(visaProductsList)) {
						 visaProducts = visaProductsList.get(0);
					 }
					 
					 //如果存在此产品且有根据这个游客与产品生成的签证子订单，则进行订单更新，否则进行签证子订单创建
					 if (visaProducts != null) {
						 //根据单团游客id与签证产品id查询生成的签证子订单
						 List<VisaOrder> visaOrderList = visaOrderDao.findByProductIdAndMainOrderTravelerId(visaProducts.getId(), traveler.getId());
						 //有签证子订单，则进行订单更新
						 if (CollectionUtils.isNotEmpty(visaOrderList)) {
							 VisaOrder visaOrder = visaOrderList.get(0);
							//if (UserUtils.getUser().getCompany().getId() == 88) { //
							 /**
							  * wangxinwei modified 使用UUID 20151026
							  * 对应需求号C310
							  */
							 if ("7a81a26b77a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())) {
								 visaOrderService.subVisaOrderCreate4LMT(visaProducts, visaOrder, travelerVisa, 
										 productOrderCommon, traveler, travelerDocInfoIds);
							 } else {
								 visaOrderService.subVisaOrderCreate(visaProducts, visaOrder, travelerVisa, 
										 productOrderCommon, traveler, travelerDocInfoIds);
							 }
						 } 
						 //没有签证子订单，进行签证子订单创建
						 else {
							 //if (UserUtils.getUser().getCompany().getId() == 88) { //
							 /**
							  * wangxinwei modified 使用UUID 20151026
							  * 对应需求号C310 
							  */
							 if ("7a81a26b77a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())) {
								 visaOrderService.subVisaOrderCreate4LMT(visaProducts, null, travelerVisa, 
										 productOrderCommon, traveler, travelerDocInfoIds);
							 } else {
								 visaOrderService.subVisaOrderCreate(visaProducts, null, travelerVisa, 
										 productOrderCommon, traveler, travelerDocInfoIds);
							 }
						 }
					 }
				 }
			 }
		 }
	 }
	 
	/**
	 * 保存订单金额和订单联系人：修改订单使用
	 * 
	 * @param productOrder
	 * @param orderContacts
	 * @param newTotalPersonNum
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> saveOrderMoneyAndPosition(ProductOrderCommon productOrder, int newTotalPersonNum, Integer NewRoomNumber, List<OrderContacts> orderContacts) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("Code", "0");
		resultMap.put("Message", "Success!");
		// 查询订单原人数		
		Integer orderPersonNum = productOrder.getOrderPersonNum();//修改后人数

		// 根据团期查询成人、儿童、特殊人群价格
		ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
		BigDecimal adultPrice = activityGroup.getSettlementAdultPrice() == null ? BigDecimal.ZERO
				: activityGroup.getSettlementAdultPrice();
		BigDecimal childPrice = activityGroup.getSettlementcChildPrice() == null ? BigDecimal.ZERO
				: activityGroup.getSettlementcChildPrice();
		BigDecimal specialPrice = activityGroup.getSettlementSpecialPrice() == null ? BigDecimal.ZERO
				: activityGroup.getSettlementSpecialPrice();
		//由于某一条数据的special价格为null  add by yang.jiang   //是要取min还是取0？？
		// 根据团期查询价格币种：成人、儿童、特殊人群币种
		String currency = activityGroup.getCurrencyType();
		String adultCurrencyId = null;
		String childCurrencyId = null;
		String specialCurrencyId = null;
		String[] currencyArr = { "1", "1", "1", "1", "1", "1", "1", "1" };
		if (currency != null) {
			currencyArr = currency.split(",");
		}
		if (Context.ORDER_TYPE_SP == productOrder.getOrderStatus()) {  //散拼
			if (Context.PRICE_TYPE_ZKJ == productOrder.getPriceType()) {  //直客价
				adultCurrencyId = currencyArr[3];
				childCurrencyId = currencyArr[4];
				specialCurrencyId = currencyArr[5];
			} else {
				adultCurrencyId = currencyArr[0];
				childCurrencyId = currencyArr[1];
				specialCurrencyId = currencyArr[2];
			}
		} else if (Context.ORDER_TYPE_CRUISE == productOrder.getOrderStatus()) {  //游轮
			if (Context.PRICE_TYPE_ZKJ == productOrder.getPriceType()) {  //直客价
				adultCurrencyId = currencyArr[2];
				childCurrencyId = currencyArr[3];
				specialCurrencyId = "1";
			} else {
				adultCurrencyId = currencyArr[0];
				childCurrencyId = currencyArr[1];
				specialCurrencyId = "1";
			}
		} else {  //单团、游学、大客户、自由行
			adultCurrencyId = currencyArr[0];
			childCurrencyId = currencyArr[1];
			specialCurrencyId = currencyArr[2];
		}
		// 订金:如果页面提交订单人数不等于原人数则修改订单订金金额：订金总金额=订金单价金额*人数
		if (orderPersonNum != newTotalPersonNum) {
			List<MoneyAmount> payDepositAmountList = moneyAmountService.findAmountBySerialNum(productOrder.getPayDeposit());			
			BigDecimal frontMoney = StringNumFormat.getBigDecimalMultiply(new BigDecimal(newTotalPersonNum), payDepositAmountList.get(0).getAmount());			
			List<MoneyAmount> frontMoneyAmountList = moneyAmountService.findAmountBySerialNum(productOrder.getFrontMoney());			
			for (MoneyAmount moneyAmount : frontMoneyAmountList) {
				moneyAmount.setAmount(frontMoney);
			}
			moneyAmountService.saveOrUpdateMoneyAmounts(productOrder.getFrontMoney(), frontMoneyAmountList);
		}

		// 计算订单成本价（同行价*人数）
		Map<String, BigDecimal> costMoneyMap = new HashMap<String, BigDecimal>();
		if (productOrder.getOrderPersonNumAdult() > 0) {
			costMoneyMap.put(adultCurrencyId, adultPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumAdult())));
		}
		if (productOrder.getOrderPersonNumChild() > 0) {
			BigDecimal childCostMoney = childPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumChild()));
			if (!costMoneyMap.containsKey(childCurrencyId)) {
				costMoneyMap.put(childCurrencyId, childCostMoney);
			} else {
				costMoneyMap.put(childCurrencyId, costMoneyMap.get(childCurrencyId).add(childCostMoney));
			}
		}
		if (productOrder.getOrderPersonNumSpecial() > 0) {
			BigDecimal specialCostMoney = specialPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()));
			if (!costMoneyMap.containsKey(specialCurrencyId)) {
				costMoneyMap.put(specialCurrencyId, specialCostMoney);
			} else {
				costMoneyMap.put(specialCurrencyId, costMoneyMap.get(specialCurrencyId).add(specialCostMoney));
			}
		}
		productOrder.setCostMoney(productOrder.getCostMoney() != null ? productOrder.getCostMoney() : UUID.randomUUID().toString());
		List<MoneyAmount> moneyAmountList = Lists.newArrayList();
		MoneyAmount moneyAmount = null;
		String costMoneySerialNum = productOrder.getCostMoney();
		for (String key : costMoneyMap.keySet()) {
			moneyAmount = new MoneyAmount(costMoneySerialNum, Integer.parseInt(key), costMoneyMap.get(key),
					productOrder.getId(), Context.MONEY_TYPE_CBJ, productOrder.getOrderStatus(), 1, UserUtils.getUser().getId());
			moneyAmountList.add(moneyAmount);
		}
		moneyAmountService.saveOrUpdateMoneyAmounts(costMoneySerialNum, moneyAmountList);

		// 保存订单联系人信息
		if (orderContacts != null && orderContacts.size() > 0) {
			StringBuffer contactsName = saveOrderContactsNew(orderContacts, productOrder.getId(), productOrder.getOrderStatus(), productOrder.getOrderCompany());
			productOrder.setOrderPersonName(contactsName.toString());
		}
		// 修改订单，对售出占位和余位进行修改。
		resultMap = changeFreePositionAndSoldPosition(productOrder, newTotalPersonNum, NewRoomNumber);
		
		productorderDao.save(productOrder);
		
		return resultMap;
	}
	
	private StringBuffer saveOrderContactsNew(List<OrderContacts> contactsList, Long orderId, int orderType, Long agentId) {
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
            contacts.setAgentId(agentId);
    		orderContactsDao.save(contacts);
    	}
    	return contactsName;
	}

	/**
	 * 订单修改情况下，对售出占位和余位进行修改。
	 * add by yunpeng.zhang
	 * @param productOrder
	 * @param orgOrderPersonNum
	 */
		private Map<String, Object> resultMap = new HashMap<>();
		private Map<String, Object> changeFreePositionAndSoldPosition(ProductOrderCommon productOrder, int newTotalPersonNum, Integer NewRoomNumber) {
		resultMap.put("Code", "0");
		resultMap.put("Message", "Success!");
		int balancePersonNum = 0;
		//订单修改前人数
		Integer orderPersonNum = productOrder.getOrderPersonNum();
		
		// 邮轮扣减房间差
		if (Integer.valueOf(Context.ORDER_STATUS_CRUISE) == productOrder.getOrderStatus()) {
			if(NewRoomNumber != null && productOrder.getRoomNumber() != null) {
				balancePersonNum = NewRoomNumber - productOrder.getRoomNumber();
			}
		// 单团、散拼、大客户、自由行、游学，扣减人数差
		} else if (Integer.valueOf(Context.ORDER_STATUS_SINGLE) == productOrder.getOrderStatus() ||
				Integer.valueOf(Context.ORDER_STATUS_LOOSE) == productOrder.getOrderStatus() ||
				Integer.valueOf(Context.ORDER_STATUS_BIG_CUSTOMER) == productOrder.getOrderStatus() ||
				Integer.valueOf(Context.ORDER_STATUS_FREE) == productOrder.getOrderStatus() ||
				Integer.valueOf(Context.ORDER_STATUS_STUDY) == productOrder.getOrderStatus()) {
			balancePersonNum = newTotalPersonNum - orderPersonNum;
		}
		//把订单人数、房间数设置为新值
		productOrder.setOrderPersonNum(newTotalPersonNum);
		productOrder.setRoomNumber(NewRoomNumber);
		
		if (balancePersonNum > 0) {
			try {
				orderStockService.changeGroupFreeNum(productOrder, balancePersonNum, Context.StockOpType.MODIFY);
			} catch (Exception e) {
				resultMap.put("Code", "101");
				resultMap.put("Message", e.getMessage());
				return resultMap;
			}
		}
		
		return resultMap;
	}
	
	/**
	 * add by ruyi.chen 
	 * add date 2015-01-05
	 * 修改游客状态
	 */
	public void updateTravelerStatus(Integer status,Long travelerId){
		
		travelerDao.updateTravelerDelFlag(status, travelerId);
	}
	
	/**
	 * 获取订单关联的产品的部门id
	 * @author jiachen
	 * @DateTime 2015年2月5日 下午3:43:05
	 * @return Long
	 */
	public Long getProductPept(Object orderId) {
		Long deptId = null;
		if(null != orderId) {
			ProductOrderCommon productOrderCommon = getProductorderById(StringUtils.toLong(orderId.toString()));
					
			if(null != productOrderCommon) {
				deptId = travelActivityDao.findOne(productOrderCommon.getProductId()).getDeptId();
				return deptId == null ? 0L : deptId;
			}
		}
		return deptId;
	}
	
	Map<String, Object> changeOrderStatusAndGroupFreeNum(ProductOrderCommon order, Orderpay orderPay, 
			HttpServletRequest request) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", true);
		
		//只有当订单不是再次支付的时候才会扣减余位；
		//只有当订单是立即支付时才扣减余位售出占位（针对月结、后付费，因为订单初始是立即支付，扣减余位再次支付时订单为月结或后付费）；
		if (!Context.ORDER_PAYSTATUS_YZFDJ.equals(order.getPayStatus().toString()) 
				&& !Context.ORDER_PAYSTATUS_YZF.equals(order.getPayStatus().toString())
				&& !Context.ORDER_PAYSTATUS_CW.equals(order.getPayStatus().toString()) && order.getPaymentType() == 1) {
			orderStockService.changeGroupFreeNum(order, null, Context.StockOpType.PAY); // 余位扣减操作
		}
		
		// 全款-1 尾款-2 订金-3
		Integer payPriceType = orderPay != null ? orderPay.getPayPriceType() : null;
		Integer paymentStatus = orderPay != null ? orderPay.getPaymentStatus() : null;
		
		if (payPriceType != null && paymentStatus == Context.PAYMENT_TYPE_JS) {

			// 如果是订金支付，则订单支付后状态为"订金已支付"，且支付订单状态改为"订金已经支付 "
			if (Context.ORDER_ORDERTYPE_ZFDJ.equals(payPriceType.toString())) {
				if (order.getPayStatus() != null && Context.ORDER_PAYSTATUS_YZFDJ.equals(order.getPayStatus().toString())) {
					map.put("isSuccess", false);
					return map;
				}
				orderPay.setPayPriceType(Integer.parseInt(Context.ORDER_ORDERTYPE_ZFDJ));
				//如果是财务确认占位，则订单状态不变
				if (!Context.ORDER_PAYSTATUS_CW.equals(order.getPayMode())) {
					order.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
				}
			}

			// 如果是全款支付，则订单支付后状态为"已支付"，且支付订单状态改为"支付全款"
			else if (Context.ORDER_ORDERTYPE_ZFQK.equals(payPriceType.toString())) {
				if (order.getPayStatus() != null && Context.ORDER_PAYSTATUS_YZF.equals(order.getPayStatus().toString())) {
					map.put("errorMsg", "订单已支付全款，不能再次支付全款");
					map.put("isSuccess", false);
					return map;
				}
				//如果是财务确认占位，则订单状态不变
				if (!Context.ORDER_PAYSTATUS_CW.equals(order.getPayMode())) {
					order.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
				}
				orderPay.setPayPriceType(Integer.parseInt(Context.ORDER_ORDERTYPE_ZFQK));
			}

			// 如果是尾款支付，则订单支付后状态为"已支付"，且支付订单状态改为"支付尾款"
			else if (Context.ORDER_ORDERTYPE_ZFWK.equals(payPriceType.toString())) {
				//如果是财务确认占位，则订单状态不变
				if (!Context.ORDER_PAYSTATUS_CW.equals(order.getPayMode())) {
					order.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
				}
				orderPay.setPayPriceType(Integer.parseInt(Context.ORDER_ORDERTYPE_ZFWK));
			}
		} else {
			// 如果不是立即支付订单，则系统不对该订单进行自动取消
			order.setIsAfterSupplement(1);
		}
		
		order.setPaymentType(paymentStatus);
		saveProductorder(order);
		return map;
	}
	
	@SuppressWarnings("unused")
    private Map<String, Object> addFree(ProductOrderCommon order, Orderpay orderPay, 
			HttpServletRequest request) throws Exception {
		return null;
	}
	
	/**
	 * 订单财务收款提示信息： 确认达账例子：财务已确认订金/尾款/全款达账金额$100；
	 * 						撤销达账例子：财务已撤销订金/尾款/全款达账金额$100；
	 * 						驳回且保留占位例子：财务驳回订金/尾款/全款付款￥12500元，需重新发起付款；
	 * 						驳回且不保留占位例子：财务驳回订金/尾款/全款付款￥12500元，并取消占位，需重新发起预定；
	 * @param orderType 订单种类
	 * @param orderId 订单ID
	 * @param isCanceledOrder 订单是否因驳回取消
	 * @return
	 */
	public String getOrderPrompt(String orderType, Long orderId, boolean isCanceledOrder) {
		
		if (StringUtils.isNotBlank(orderType) && orderId != null) {
			List<Orderpay> orderPayList = orderpayDao.findLastDateOrderpay(orderId, Integer.parseInt(orderType));
			String prompt = "财务已确认";
			
			Orderpay orderPay = null;
			if (CollectionUtils.isNotEmpty(orderPayList)) {
				orderPay = orderPayList.get(0);
			}
			
			//达账和撤销提示
	        if (orderPay != null && orderPay.getPayPriceType() != null && orderPay.getIsAsAccount() != null) {
				String payPriceType = orderPay.getPayPriceType().toString();
				Integer isAsAccount = orderPay.getIsAsAccount();
				if (Context.ORDER_ORDERTYPE_ZFQK.equals(payPriceType)) {
					prompt += "全款";
				} else if (Context.ORDER_ORDERTYPE_ZFWK.equals(payPriceType)) {
					prompt += "尾款";
				} else if (Context.ORDER_ORDERTYPE_ZFDJ.equals(payPriceType)) {
					prompt += "订金";
				}
				
				if (Context.ORDERPAY_ACCOUNT_STATUS_YCX == isAsAccount) {
					prompt = prompt.replace("确认", "撤销") + "达账金额";
				} else if (Context.ORDERPAY_ACCOUNT_STATUS_YDZ.equals(isAsAccount)) {
					prompt += "达账金额";
				} else if (Context.ORDERPAY_ACCOUNT_STATUS_YBH.equals(isAsAccount)) {
					prompt = prompt.replace("已确认", "驳回") + "付款";
				}
				
				prompt += moneyAmountService.getMoneyStr(orderPay.getMoneySerialNum());
				
				if (Context.ORDERPAY_ACCOUNT_STATUS_YBH.equals(isAsAccount)) {
					if (isCanceledOrder) {
						prompt += "，并取消占位，需重新发起预定";
					} else {
						prompt += "，需重新发起付款";
					}
				}
				
				return prompt;
	        }
		}
		return null;
	}
	/**
	 * 查询 订单类型(单团、散拼、游学...)
	 * @return 
	 */
	public Object findOrderStatusByOrderId(Long visaOrderId){
		String sql = "select po.orderStatus as group_type from traveler t LEFT JOIN visa_order vo on vo.id = t.orderId LEFT JOIN productorder po on po.id = t.main_order_id where t.order_type = 6 and vo.id="+visaOrderId;
		Object value = productorderDao.findBySql(sql);
		return value;
	}
	/**
	 * 根据订单唯一标识、产品类型查询游客信息
	 * @param orderId
	 * @param productType
	 * @author chenry
     *  @DateTime 2014-11-04
	 */
	public List<Map<String, Object>> getBorrowingTravelerByOrderId(Long orderId,Integer productType) {
		StringBuffer sbf=new StringBuffer();
		sbf
		.append("SELECT p.orderTime,tt.id,tt.orderId,tt.name,tt.sex,tt.srcPrice,tt.applied_discount_price,tt.org_discount_price,tt.fixed_discount_price,tt.srcPriceCurrency,tt.payPriceSerialNum,tt.personType,tt.delFlag,")
		.append("IFNULL(a.accounted_money,0) as payPrice from  traveler tt LEFT JOIN productorder p on tt.orderId= p.id ")
		.append(" LEFT JOIN(select t.id,")
		.append(" group_concat(FORMAT(IFNULL(ma.amount, 0),2),c.currency_name,'money' ORDER BY c.currency_id ) AS accounted_money")
		.append(" from traveler t LEFT  JOIN money_amount ma on t.payPriceSerialNum=ma.serialNum")
		.append(" and t.orderId =? and t.order_type=? and t.delFlag in(0,2,4) inner JOIN currency c on ma.currencyId= c.currency_id ")
		.append(" GROUP BY t.id)a on tt.id=a.id  WHERE tt.orderId=? and tt.order_type =? and tt.delFlag in(0,2,4) order by tt.id asc");
		List<Map<String, Object>> ls=productorderDao.findBySql(sbf.toString(), Map.class,orderId,productType,orderId,productType);
		for(Map<String, Object>m:ls){
			String payPrice=m.get("payPrice").toString();
			if(StringUtils.isNotBlank(payPrice)){
				m.put("payPrice", getMoneyStr(payPrice));
			}
			
		}
		return ls;
	}
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public String saveBorrowing(HttpServletRequest request){
		
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
//		String[] travelerIds = request.getParameterValues("travelerId");
		String travelerId="0";
		String orderType = request.getParameter("orderType");
		List<String> paramNamesList = new ArrayList<String>();
		paramNamesList.add("travelerId");
		paramNamesList.add("travelerName");
		paramNamesList.add("currencyId");
		paramNamesList.add("currencyName");
		paramNamesList.add("currencyMark");
		paramNamesList.add("currencyExchangerate");
		paramNamesList.add("payPrice");
		paramNamesList.add("lendPrice");
		paramNamesList.add("remark");
		paramNamesList.add("lendName");
		paramNamesList.add("borrowRemark");
		List<String> paramList = new ArrayList<String>();
		paramList.add("currencyIds");
		paramList.add("currencyNames");
		paramList.add("currencyMarks");
		paramList.add("borrowPrices");
		//paramList.add("currencyExchangerates");
				
		ProductOrderCommon order = productorderDao.findOne(Long.parseLong(orderId));
		if(null == order || order.getId() == 0){
			return "未找到订单！";
			
		}
		TravelActivity activity = travelActivityDao.findOne(order.getProductId());
		if(null == activity || activity.getId() == 0){
			return "未找到产品！";
		}
		
		if(null == activity.getDeptId() || 0 >= activity.getDeptId()){
			return "产品没有部门！";
		}
		
		List<Detail> listReview = BorrowingBean.exportDetail4Request(request, paramNamesList,paramList,"lendPrice");
		if(request.getParameter("borrowPrices") != null&& request.getParameter("currencyIds")!= null ){
			float  fc = currencyConverter(request.getParameter("borrowPrices"),request.getParameter("currencyIds"));
			Detail d = new Detail();
			d.setKey("currencyConverter");
			d.setValue(fc+"");
			listReview.add(d);
		}
		if(request.getParameter("currencyExchangerates") != null ){
			Detail d = new Detail();
			d.setKey("currencyExchangerates");
			d.setValue(request.getParameter("currencyExchangerates"));
			listReview.add(d);
		}
		if (StringUtils.isEmpty(reviewId)) {
			reviewId = "0";
		}
		StringBuffer reply = new StringBuffer();
		Long result=0L; //返回的是reviewId
		
		String[] travels =  listReview.get(0).getValue().split("#");
		if(travels!=null&&travels.length>0){
			if(travels.length>1){
				travelerId="0";
			}else if(travels.length==1 && ("0").equals(travels[0])){
				travelerId="0";
			}else{
				travelerId=travels[0];
			}
		}
		
//		if(travelerIds!=null&&travelerIds.length>0){
//			if(travelerIds.length>1 ){
//				travelerId="0";
//			}else{
//				travelerId=travelerIds[0];
//			}
//		}
		String borrowRemark = request.getParameter("borrowRemark");
		result = reviewService.addReview(Integer.parseInt(orderType), 19, orderId, Long.parseLong(travelerId), Long.parseLong(reviewId), borrowRemark, reply, listReview, activity.getDeptId());
		
		//Map<String, Object> map = new HashMap<String, Object>();
		String msg="";
		if(result==0L){
			//map.put("error", reply.toString());
			msg=reply.toString();
		} else {
			//map.put("success", "success");
			msg="申请成功!";
		////////////////////////////////////////////////
			/**
			* add by ruyi.chen
			* add date 2015-05-28
			* describe 过滤低空游轮直接通过审核
			*/
//			Review r=reviewService.findReviewInfo(result);
//			if(Context.ORDER_STATUS_CRUISE.equals(r.getProductType().toString())){
//				reviewService.UpdateReview(result, r.getTopLevel(), 1, "");
//				boolean flag = false;
//				try {
//					 flag = airTicketOrderLendMoneyService.saveLendMoney2MoneyAmount(result, r.getOrderId(), r.getProductType());
//				} catch (Exception e) {
//					return "操作失败";
//				}
//				if(flag){
//					return "操作成功";
//				}else{
//					return "操作失败";
//				}
//			}
			
			
			//////////////////////////////////////////////////
		}
		
		return msg;
	}
	/**
	 * 其他币种转换成人民币
	 */
	@ResponseBody
	private float currencyConverter(String count,String currencyId)
	{
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		String [] ct= count.split("#");
		String [] ci= currencyId.split("#");
		double totalMoney =0;
		for(int i=0;i<ct.length;i++)
		{
			if(StringUtils.isNotBlank(ct[i])){
				String ctStr = ct[i].replaceAll(",", "");
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=?");
				//buffer.append(ci[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = productorderDao.findBySql(buffer.toString(), Map.class,ci[i]);
				Map<String, Object>  mp =  list.get(0);
				totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ctStr);
			}
			
		}
		 BigDecimal   b   =   new   BigDecimal(totalMoney); 
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	/**
	 * 付款确认,取消付款确认
	 * @param id
	 * @param type 1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款
	 * @param status 0:未付款  1：已付款
	 * @param orderType
	 * */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void confirmOrCancelPay(String id,String type,String status,String orderType,String rate,String priceAfter,Date confirmPayDate){
		Long userId = UserUtils.getUser().getId();
		if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(type)){
			if(type.equals("1")){
				if(rate != null && rate != ""){
					if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ISLAND==Integer.valueOf(orderType)){
						//海岛游付款确认
						costRecordIslandDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id),new BigDecimal(rate),new BigDecimal(priceAfter));
					}else if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_HOTEL == Integer.valueOf(orderType)){
						//酒店付款确认
						costRecordHotleDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id),new BigDecimal(rate),new BigDecimal(priceAfter));
					}else{
						costRecordDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id),new BigDecimal(rate),new BigDecimal(priceAfter), confirmPayDate);
					}
				}else{
					if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ISLAND==Integer.valueOf(orderType)){
						//海岛游付款确认
						costRecordIslandDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id));
					}else if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_HOTEL == Integer.valueOf(orderType)){
						//酒店付款确认
						costRecordHotleDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id));
					}else{
						costRecordDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id));						
					}
				}
			}else{//其他付款

				reviewDao.confirmOrCancelPay(Integer.valueOf(status), userId, new Date(), Long.valueOf(id), confirmPayDate);
                if("1".equals(status)) {
                    if("3".equals(type) && !"6".equals(orderType) && !"7".equals(orderType)) {
                        rebatesDao.updateRate(new BigDecimal(rate), Long.parseLong(id));
                    }else{
                        moneyAmountDao.updateRate(new BigDecimal(rate), Long.parseLong(id));
                    }
                    costRecordDao.updateRate(new BigDecimal(rate), Long.parseLong(id));
                }
			}
		}
	}
	
	/**
	 * 批量付款确认
	 * @param id
	 * @param type 1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款
	 * @param status 0:未付款  1：已付款
	 * @param orderType
	 * */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void batchConfirmOrCancelPay(String id,String type,String status,String orderType,String rate,String afterPrice,Date confirmCashierDate){
		Long userId = UserUtils.getUser().getId();
		if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(type)){
			if(type.equals("1")){//成本付款
				if(rate != null && rate != ""){
					if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ISLAND==Integer.valueOf(orderType)){
						//海岛游付款确认
						costRecordIslandDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id),new BigDecimal(rate),new BigDecimal(afterPrice));
					}else if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_HOTEL == Integer.valueOf(orderType)){
						//酒店付款确认
						costRecordHotleDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id),new BigDecimal(rate),new BigDecimal(afterPrice));
					}else{
						costRecordDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id),new BigDecimal(rate),new BigDecimal(afterPrice),confirmCashierDate);
					}
				}else{
					if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ISLAND==Integer.valueOf(orderType)){
						//海岛游付款确认
						costRecordIslandDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id));
					}else if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_HOTEL == Integer.valueOf(orderType)){
						//酒店付款确认
						costRecordHotleDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id));
					}else{
						costRecordDao.confirmOrCancelPay(Integer.valueOf(status), userId,
								new Date(), Long.valueOf(id));
					}
				}
			}else{//其他付款
				iReviewDao.confirmOrCancelPay(Integer.parseInt(status), userId, new Date(), id);
			}
		}
	}
	
	/**
	 * 订单返佣总额
	 * @author jyang
	 * @param orderNo
	 * @return
	 */
	public List<String> queryOrderRebates(String orderNo) {
		String orderRebatesSql = "" +
				
" 	select GROUP_CONCAT(yy.money separator '+') as prebt" +
" 	from (" +
" 		select ma.currencyId, CONCAT(c.currency_mark, ' ', sum(ma.amount)) as money, r.`status`, serial.odno as orderNo " +
" 		from money_amount ma " +
" 			left join review r on ma.reviewId = r.id" +
" 			left join currency c on c.currency_id = ma.currencyId  " +
" 			inner join (" +
"				select ao.orderNum as odno, t.rebates_moneySerialNum as sr " +
" 				from productorder ao left join traveler t on t.orderId = ao.id where ao.delFlag = '0' and ao.orderNum = '" + orderNo + "' " +
" 				UNION " +
" 				select ao.orderNum as odno, ao.schedule_back_uuid as sr " +
" 				from productorder ao where ao.orderNum = '" + orderNo + "'" +
" 			) serial on serial.sr = ma.serialNum 	" +		
" 		where ma.amount != 0.00 and serial.odno='" +
orderNo + 
" '" +
" 		group by r.`status`, ma.currencyId " +
" 	) yy " +
" 	where yy.`status` <> 2 and yy.`status` <> 3 or yy.`status` is NULL " +
" 	group by yy.status";				
		
		List<String> orderRebatesList = productorderDao.findBySql(orderRebatesSql);
		return orderRebatesList;
	}
	
	/**
	 * 订单返佣总额
	 * @author jyang
	 * @param orderNo
	 * @return
	 */
	public List<Map<String, Object>> queryOrderRebatesInf(String orderNo) {
		String orderRebatesSql = "" +
				
"	select GROUP_CONCAT(inf.money SEPARATOR '+') as infbt" +
" 	from(" +
" 		select orb.currencyId,CONCAT(c.currency_mark, ' ', sum(orb.rebatesDiff)) as money, r.`status`, ao.orderNum as order_no from " +
				"productorder ao" +
" 			left join order_rebates orb on ao.id = orb.orderId" +
" 			left join review r on orb.rid =  r.id" +
" 			left join currency c on c.currency_id = orb.currencyId" +
" 		where ao.orderNum = '" +
orderNo +
"' and (r.`status` = 2 or r.`status` = 3) and orb.delFlag = 0 " +
"   		and (orb.order_type = 1 or orb.order_type = 2 or orb.order_type = 3 or orb.order_type = 4 or orb.order_type = 5 or orb.order_type = 10)" +
" 		group by orb.currencyId" +
" 	) inf";			
		
		List<Map<String, Object>> orderRebatesList = productorderDao.findBySql(orderRebatesSql, Map.class);
		return orderRebatesList;
	}

	/**
	 * 根据订单唯一标识,订单类型查询游客退款信息
	 * @param orderId
	 * @param orderType
	 * @author yunpeng.zhang
     * @createDate 2015年12月23日14:24:19
	 */
	public List<Map<Object, Object>> getTravelerByOrderId4ExitRefund(Long orderId,Integer orderType) {
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT p.orderTime,tt.id,tt.orderId,tt.name,tt.sex,tt.payPriceSerialNum,tt.personType,tt.delFlag,")
		.append("IFNULL(a.accounted_money,0) as payPrice,a.currencyId from  traveler tt LEFT JOIN productorder p on tt.orderId= p.id ")
		.append(" LEFT JOIN(select t.id,")
		.append(" CONCAT(IFNULL(ma.amount, 0),c.currency_name) as accounted_money,ma.currencyId")
		.append(" from traveler t LEFT  JOIN money_amount ma on t.payPriceSerialNum=ma.serialNum")
		.append(" JOIN currency c on ma.currencyId=c.currency_id")
		.append(" where t.orderId =? and t.delFlag in(0,2,3,4,5))a on tt.id=a.id  WHERE tt.orderId=? and tt.delFlag in(0,2,3,4,5) and tt.order_type=?  order by tt.id asc");
		List<Map<Object, Object>> ls = productorderDao.findBySql(sbf.toString(), Map.class, orderId, orderId, orderType);
		for (Map<Object, Object> m : ls) {
			String payPrice=m.get("payPrice").toString();
			if (StringUtils.isNotBlank(payPrice)) {
				m.put("payPrice", getMoneyStr(payPrice));
			}
		}
		// 处理 ls，在多币种情况下，将相同订单进行合并，返回带有多币种的 currencyId
		List<Map<Object,Object>> list = mergerTravelerList(ls);
		return list;
	}


	/**
	 * 订单返佣总额
	 * @author yunpeng.zhang
	 * @param orderNo
	 * @return
	 */
	public List<Map<String, Object>> queryOrderNewRebatesInf(String orderNo) {
		String orderRebatesSql = "" +

				"	select GROUP_CONCAT(inf.money SEPARATOR '+') as infbt" +
				" 	from(" +
				" 		select orb.currencyId,CONCAT(c.currency_mark,' ', sum(orb.rebatesDiff)) as money, r.`status`, ao.orderNum as order_no from productorder ao" +
				" 			left join rebates orb on ao.id = orb.orderId" +
				" 			left join review_new r on orb.rid =  r.id" +
				" 			left join currency c on c.currency_id = orb.currencyId" +
				" 		where ao.orderNum = '" +
				orderNo +
				"' and (r.`status` = 2 ) and orb.delFlag = 0 " +
				"   		and (orb.orderType = 1 or orb.orderType = 2 or orb.orderType = 3 or orb.orderType = 4 or orb.orderType = 5 or" +
				" orb.orderType = 10)" +
				" 		group by orb.currencyId" +
				" 	) inf";

		List<Map<String, Object>> orderRebatesList = productorderDao.findBySql(orderRebatesSql, Map.class);
		return orderRebatesList;
	}

	public List<ProductOrderCommon> findByOrderNum4Discount(String orderNum) {
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		return productorderDao.findByOrderNum4Discount(orderNum, companyUuid);
	}

	/**
	 * 获取已付款游客列表(签证)
	 * @author wangyang
	 * @date 2016.3.15
	 * @param orderNum 团号
	 * @return namelist已付款游客名字
	 * */
	public List<String> findTravelerNameList(String orderNum){
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("select t.name as name from traveler t,orderpay op,visa_order vo ")
		  	  .append(" where t.id=op.traveler_id ")
		      .append(" and op.orderType = '").append(Context.ORDER_TYPE_QZ).append("'")
		      .append(" and op.isAsAccount = '1' ")
		      .append(" and op.orderNum = vo.order_no ")
		      .append(" and vo.order_no = '").append(orderNum).append("'");
		
		List<String> namelist = travelerDao.findBySql(buffer.toString());
		return namelist;
		
	}
	
    /**
     * 变更确认状态
     */
    @Transactional(readOnly = false)
    public void handleConfirmStatus(ProductOrderCommon productOrder) {
        if(productOrder != null) {
            productOrder.setSeizedConfirmationStatus(Context.SEIZEDCONFIRMATIONSTATUS_1);
            productOrder.setIsAfterSupplement(Context.IS_AFTER_SUPPLEMENT_1);
            productorderDao.updateObj(productOrder);
        }

    }

	public void confimOrCancelInvoice(String id, String status) {
		if(StringUtils.isBlank(id)){
			log.error("ID不能为空");
			throw new CommonReviewException("ID不能为空");
		}
		if(StringUtils.isBlank(status)){
			log.error("状态不能为空");
			throw new CommonReviewException("状态不能为空");
		}
		Long userId = UserUtils.getUser().getId();
		ReviewResult result = updateInvoiceStatus(userId + "", status, id);
		if(!result.getSuccess()){
			log.error("操作失败：" + result.getMessage());
			throw new CommonReviewException("操作失败：" + result.getMessage());
		}
		
		
	}

	private ReviewResult updateInvoiceStatus(String string, String status,
			String id) {
		ReviewResult result = new ReviewResult();
		CostRecord costRecord = costRecordDao.getById(Long.parseLong(id));
		CostRecordHotel costRecordHotle = costRecordHotleDao.findOne(Long.parseLong(id));
		CostRecordIsland costRecordIsland = costRecordIslandDao.findOne(Long.parseLong(id));
		if(costRecord != null ){
			// 更新发票状态   status 0:未收发票   1：已收发票
			if("0".equals(status)){
				costRecord.setInvoiceStatus(1);
			}else{
				costRecord.setInvoiceStatus(0);
			}
			// 设置更新人
			costRecord.setUpdateBy(UserUtils.getUser().getId());
			Date currentDate=new Date();
			// 设置更新时间
			costRecord.setUpdateDate(currentDate);
			costRecordDao.updateObj(costRecord);
		}
		if(costRecordHotle != null){
			if("0".equals(status)){
				costRecordHotle.setInvoiceStatus(1);
			}else{
				costRecordHotle.setInvoiceStatus(0);
			}
			costRecordHotle.setUpdateBy(UserUtils.getUser().getId());
			Date currentDate=new Date();
			costRecordHotle.setUpdateDate(currentDate);
			costRecordHotleDao.updateObj(costRecordHotle);
		}
		if(costRecordIsland != null){
			if("0".equals(status)){
				costRecordIsland.setInvoiceStatus(1);
			}else{
				costRecordIsland.setInvoiceStatus(0);
			}
			costRecordIsland.setUpdateBy(UserUtils.getUser().getId());
			Date currentDate=new Date();
			costRecordIsland.setUpdateDate(currentDate);
			costRecordIslandDao.updateObj(costRecordIsland);
		}
		// 组织数据返回
		result.setCode(ReviewErrorCode.ERROR_CODE_SUCCESS);
		result.setReviewId(id);
		result.setSuccess(true);
		return result;
	}

	public void confimOrCancelInvoiceAll(String id) {
		if(StringUtils.isBlank(id)){
			log.error("ID不能为空");
			throw new CommonReviewException("ID不能为空");
		}
		Long userId = UserUtils.getUser().getId();
		ReviewResult result = updateInvoiceStatusAll(userId + "", id);
		if(!result.getSuccess()){
			log.error("操作失败：" + result.getMessage());
			throw new CommonReviewException("操作失败：" + result.getMessage());
		}
		
		
	}

	private ReviewResult updateInvoiceStatusAll(String userId, String id) {
		ReviewResult result = new ReviewResult();
		CostRecord costRecord = costRecordDao.getById(Long.parseLong(id));
		CostRecordHotel costRecordHotle = costRecordHotleDao.findOne(Long.parseLong(id));
		CostRecordIsland costRecordIsland = costRecordIslandDao.findOne(Long.parseLong(id));
		if(costRecord != null ){
			// 更新发票状态   status 0:未收发票   1：已收发票
			costRecord.setInvoiceStatus(1);
			// 设置更新人
			costRecord.setUpdateBy(UserUtils.getUser().getId());
			Date currentDate=new Date();
			// 设置更新时间
			costRecord.setUpdateDate(currentDate);
			costRecordDao.updateObj(costRecord);
		}
		if(costRecordHotle != null){
			// 更新发票状态   status 0:未收发票   1：已收发票
			costRecordHotle.setInvoiceStatus(1);
			costRecordHotle.setUpdateBy(UserUtils.getUser().getId());
			Date currentDate=new Date();
			costRecordHotle.setUpdateDate(currentDate);
			costRecordHotleDao.updateObj(costRecordHotle);
		}
		if(costRecordIsland != null){
			// 更新发票状态   status 0:未收发票   1：已收发票
			costRecordIsland.setInvoiceStatus(1);
			costRecordIsland.setUpdateBy(UserUtils.getUser().getId());
			Date currentDate=new Date();
			costRecordIsland.setUpdateDate(currentDate);
			costRecordIslandDao.updateObj(costRecordIsland);
		}
		// 组织数据返回
		result.setCode(ReviewErrorCode.ERROR_CODE_SUCCESS);
		result.setReviewId(id);
		result.setSuccess(true);
		return result;
	}

	/**
     * 根据团期id查询该团期下的订单id 和 订单orderNo
     * @param productGroupId
     * @return
     * @author xianglei.dong
     */
	public List<Map<String, Object>> findOrderIdAndNoByGroupId(Long productGroupId) {
		List<Map<String, Object>> list = productorderDao.findOrderIdAndNoByGroupId(productGroupId);
		return list;
	}

	/**
	 * 获取产品表中团号
	 * @author yang.jiang 2016-04-26 20:40:48
	 * @param proType 产品类型
	 * @param proId 产品ID
	 * @param groId 团期ID
	 * @return
	 */
	public String getProductGroupCode(Object proType, Object proId, Object groId) {
		String result = "";
		if (proType == null || StringUtils.isBlank(proType.toString())) {  // 产品类型不能为空
			return result;
		}
		//
		Integer productType = Integer.parseInt(proType.toString());
		if (Context.ORDER_TYPE_DT == productType
				|| Context.ORDER_TYPE_SP == productType
				|| Context.ORDER_TYPE_YX == productType
				|| Context.ORDER_TYPE_ZYX == productType
				|| Context.ORDER_TYPE_DKH == productType
				|| Context.ORDER_TYPE_CRUISE == productType) {
			
			if (groId == null || StringUtils.isBlank(groId.toString())) {  // 团期类产品的团id不能为空
				return result;
			}
			Long groupId = Long.parseLong(groId.toString());
			ActivityGroup group = activityGroupDao.findById(groupId);
			if (group != null) {
				result = group.getGroupCode();
			}
		} else if (Context.ORDER_TYPE_QZ == productType) {
			
			if (proId == null || StringUtils.isBlank(proId.toString())) {  // 签证产品的产品id不能为空
				return result;
			}
			Long productId = Long.parseLong(proId.toString());
			VisaProducts visa = visaProductsDao.findOne(productId);
			if (visa != null) {
				result = visa.getGroupCode();
			}
		} else if (Context.ORDER_TYPE_JP == productType) {
			
			if (proId == null || StringUtils.isBlank(proId.toString())) {  // 机票产品的产品id不能为空
				return result;
			}
			Long productId = Long.parseLong(proId.toString());
			ActivityAirTicket airTicket = activityAirTicketDao.findOne(productId);
			if (airTicket != null) {
				result = airTicket.getGroupCode();
			}
		} else {
			// TODO 其他产品线未知
		}
		
		return result;
	}

	/**
	 * 由退团游客信息转json
	 * @param travelerList
	 * @return
	 */
	public JSONArray handleRefundMoneyTravelerJson(
			List<Map<Object, Object>> travelerList) {
		org.json.JSONArray results = new org.json.JSONArray();
		org.json.JSONObject resobj = new org.json.JSONObject();
		for (Map<Object, Object> traveler : travelerList) {
			try {
				resobj = JSONUtils.objectToJson(traveler);
				results.put(resobj);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public Long getOrderIdByPreOrderId(Long id){
        return productorderDao.getOrderIdByPreOrderId(id);
    }
	
	public ProductOrderCommon getBySerialNum(String serialNum){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM productorder WHERE total_money = ? and delFlag = 0 ");
		List<ProductOrderCommon> list = productorderDao.findBySql(sbf.toString(),ProductOrderCommon.class, serialNum);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 补位订单生成后，更新order_data_statistic表中相关数据  ----mbmr,2017-01-18
	 * @param pro
     */
	public void updateOrderDataStatistic(ProductOrderCommon pro){
       if(pro!=null){
		OrderDataStatistics ods = statisticAnalysisDao.getEntityByOrderIdAndType(pro.getId(),pro.getOrderStatus());
			if (ods == null){
				ods = new OrderDataStatistics();
				ods.setUuid(UUID.randomUUID().toString());
			}
		//数据库不能为空值，初始化数据

				if(pro.getId()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getId().toString())){
					ods.setOrderId(pro.getId());
				}else{
					ods.setOrderId(Long.valueOf(0));
				}

				if(pro.getProductId()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getProductId().toString())){
					ods.setProductId(pro.getProductId());
				}else{
					ods.setProductId(Long.valueOf(0));
				}

				if(pro.getOrderStatus()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getOrderStatus().toString())){
					ods.setOrderType(Integer.valueOf(pro.getOrderStatus().toString()));
				}else{
					ods.setOrderType(0);
				}

				if(pro.getTotalMoney()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getTotalMoney().toString())){
					ods.setAmountUuid(pro.getTotalMoney());
				}else{
					ods.setAmountUuid("");
				}

				if(pro.getOrderTime()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getOrderTime().toString())){
					ods.setOrderCreatetime(pro.getOrderTime());
				}else{
					ods.setOrderCreatetime(new Date());
				}

				if(pro.getOrderCompany()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getOrderCompany().toString())){
					ods.setAgentinfoId(pro.getOrderCompany());
				}else{
					ods.setAgentinfoId(Long.valueOf(0));
				}

				if(pro.getOrderPersonNum()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getOrderPersonNum().toString())){
					ods.setOrderPersonNum(pro.getOrderPersonNum());
				}else{
					ods.setOrderPersonNum(0);
				}


				if(pro.getOrderCompanyName()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getOrderCompanyName().toString())){
					ods.setAgentinfoName(pro.getOrderCompanyName());
				}else{
					ods.setAgentinfoName("");
				}

				if(pro.getSalerId()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getSalerId().toString())){
					ods.setSalerId(pro.getSalerId());
				}else{
					ods.setSalerId(0);
				}

				if(pro.getSalerName()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getSalerName().toString())){
					ods.setSalerName(pro.getSalerName());
				}else{
					ods.setSalerName("");
				}

				ods.setCreateDate(new Date());

				if(pro.getPayStatus()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getPayStatus().toString())){
					ods.setOrderStatus(pro.getPayStatus());
				}else{
					ods.setOrderStatus(0);
				}

				if(pro.getDelFlag()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(pro.getDelFlag().toString())){
					ods.setDelFlag(pro.getDelFlag());
				}else{
					ods.setDelFlag("");
				}


			//从四张表查询UUID()、product_name、company_uuid、amount
			List<Map<String, Object>> addDate = orderDateSaveOrUpdateDao.getAddDate(pro.getId());
			for(Map<String,Object> mapKey : addDate){

				if(mapKey.get("UUID()")!= null && org.apache.commons.lang3.StringUtils.isNotBlank(mapKey.get("UUID()").toString())){
					ods.setUuid(mapKey.get("UUID()").toString());
				}

				if(mapKey.get("product_name")!= null && org.apache.commons.lang3.StringUtils.isNotBlank(mapKey.get("product_name").toString())){
					ods.setProductName(mapKey.get("product_name").toString());
				}else{
					ods.setProductName("");
				}

				if(mapKey.get("company_uuid")!= null && org.apache.commons.lang3.StringUtils.isNotBlank(mapKey.get("company_uuid").toString())){
					ods.setCompanyUuid(mapKey.get("company_uuid").toString());
				}else{
					ods.setCompanyUuid("");
				}

				if(mapKey.get("amount")!= null && org.apache.commons.lang3.StringUtils.isNotBlank(mapKey.get("amount").toString())){
					ods.setAmount(new BigDecimal(mapKey.get("amount").toString()));
				}else{
					ods.setAmount(new BigDecimal("0"));
				}
			}

			statisticAnalysisDao.saveOrUpdateEntity(ods);
		}
	}

}