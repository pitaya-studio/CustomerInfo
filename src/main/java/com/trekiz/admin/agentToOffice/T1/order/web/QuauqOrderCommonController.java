package com.trekiz.admin.agentToOffice.T1.order.web;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.common.collect.Lists;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.service.IntermodalStrategyService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandle;
import com.trekiz.admin.modules.grouphandle.service.GroupHandleService;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.TransFerGroup;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.receipt.entity.OrderReceipt;
import com.trekiz.admin.modules.receipt.service.OrderReceiptService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.service.StockService;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierContactsView;
import com.trekiz.admin.modules.supplier.service.SupplierContactsService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.entity.TravelerFile;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.entity.TravelerVisaInfo;
import com.trekiz.admin.modules.traveler.repository.TravelerFileDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.traveler.service.TravelerVisaService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
 
 /**
  * 订单管理：单团、散拼、游学、大客户、自由行
  * @author yakun.bai
  *
  */
@Controller
@RequestMapping(value = "${adminPath}/QuauqOrderCommon/manage")
public class QuauqOrderCommonController extends BaseController {
	
    protected static final Logger logger = LoggerFactory.getLogger(QuauqOrderCommonController.class);
    
    @Autowired
    private OrderCommonService orderService;
    @Autowired
    StockService StockService;
    @Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
    @Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
    @Autowired
    private AgentinfoService agentinfoService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
	private com.quauq.review.core.engine.ReviewService reviewNewService;
    @Autowired
    private VisaProductsService visaProductsService;
    @Autowired
    private DictService dictService;
    @Autowired
    private MoneyAmountService moneyAmountService;
    @Autowired
    private TravelerService travelerService;
    @Autowired
    private TravelerVisaService travelerVisaService;
    @Autowired
    private OrderContactsService orderContactsService;
    @Autowired
    private IntermodalStrategyService intermodalStrategyService;
    @Autowired
    private TravelerFileDao travelerFileDao;
    @Autowired
    private VisaDao visaDao;
    @Autowired
    private OrderinvoiceService orderinvoiceService;
    @Autowired
    private OrderReceiptService orderreceiptService;
    @Autowired
    private ActivityGroupService groupService;
    @Autowired
    private GroupHandleService groupHandleService;
    @Autowired
    private SupplierContactsService supplierContactsService;

    
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 80;
	}

	/**
	 * 订单详情
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value ="orderDetail/{orderId}")
	public String orderDetail(@PathVariable Long orderId, Model model, HttpServletRequest request) {
		model.addAttribute("orderId", orderId);
		ProductOrderCommon product = getOrderInfoCommon(orderId.toString(), model);
		getOrderPayByOrderId(orderId.toString(), product.getOrderStatus(), model);
		if (product != null && product.getPriceType() != null) {
			model.addAttribute("priceType", product.getPriceType());
		} else {
			model.addAttribute(0);
		}
		model.addAttribute("ctxs",1);
		model.addAttribute("orderTitle","详情");
		// 20150728 获取预定团队返佣金额
		if(product!=null && StringUtils.isNotBlank(product.getScheduleBackUuid())){
			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(product.getScheduleBackUuid());
			
			if(mo!=null){
				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
				if(currency!=null){
					model.addAttribute("currencyName",currency.getCurrencyName());
					model.addAttribute("currencyMark",currency.getCurrencyMark());
					model.addAttribute("amount",mo.getAmount());
				}
			}
		}
		return "/agentToOffice/T1/order/t1Orderdetail";
	}

	
	/**
	 * 订单修改和详情公用方法 -----------------------
	 * @param orderId
	 * @param model
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private ProductOrderCommon getOrderInfoCommon(String orderId, Model model) {
		ProductOrderCommon productOrder = new ProductOrderCommon();
		String activityKind = "";
		String agentId = "";
        if(StringUtils.isNotBlank(orderId)){
        	productOrder = orderService.getProductorderById(Long.parseLong(orderId));
        	activityKind = productOrder.getOrderStatus().toString();
        	agentId = productOrder.getOrderCompany().toString();

        	// 20150728 获取预定团队返佣金额
    		if(productOrder!=null && StringUtils.isNotBlank(productOrder.getScheduleBackUuid())){
    			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(productOrder.getScheduleBackUuid());
    			
    			if(mo!=null){
    				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
    				if(currency!=null){
    					model.addAttribute("currencyName",currency.getCurrencyName());
    					model.addAttribute("currencyMark",currency.getCurrencyMark());
    					model.addAttribute("amount",mo.getAmount());
    				}
    			}
    		}
        }
        getInfoByOrderId(orderId, model, productOrder, activityKind, agentId);
        return productOrder;
	}
	
	/**
	 * 根据订单id查询支付订单----------------------
	 * @param orderId
	 * @param model
	 */
	private void getOrderPayByOrderId(String orderId, Integer orderType, Model model) {
		List<Long> idList = Lists.newArrayList();
		idList.add(Long.parseLong(orderId));
		List<Orderpay> orderpayList = orderService.findOrderpayByOrderIds(idList, orderType);
		if (CollectionUtils.isNotEmpty(orderpayList)) {
			for (Orderpay orderPay : orderpayList) {
				if (StringUtils.isNotBlank(orderPay.getMoneySerialNum())) {
					orderService.clearObject(orderPay);
					orderPay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderPay.getMoneySerialNum()));
	            }
			}
		}
		model.addAttribute("orderPayList", orderpayList);
	}
	
	
	
	
	/**
	 * 设置model  通过orderId获取该订单的信息：订单修改和详情会用到此方法
	 * @param productOrderId
	 * @param model
	 * @param productOrder
	 * @param activityKind
	 * @param agentId
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
    private void getInfoByOrderId(String productOrderId, Model model, ProductOrderCommon productOrder, String activityKind, String agentId) {
    	TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		GroupHandle groupHandle = groupHandleService.findByOrderId(productOrder.getId());
		model.addAttribute("groupHandleId", groupHandle == null ? null : groupHandle.getId());
		setControlModel(model, product);
		//报名时 价格类型
		Integer priceType = productOrder.getPriceType();
		List<String> seriNumList = new ArrayList<String>();
		seriNumList.add(productOrder.getSettlementAdultPrice());//成人价
		seriNumList.add(productOrder.getSettlementcChildPrice());//儿童价
		seriNumList.add(productOrder.getSettlementSpecialPrice());//特殊价
		seriNumList.add(productOrder.getSingleDiff());//单房差
		seriNumList.add(productOrder.getPayDeposit());//下订单时收的订金
		setCurrency(model,productOrderId, productGroup, activityKind, seriNumList, priceType);  //add by jyang for priceType

		//后台计算同行价（成人价*人数+儿童价*人数+特殊价*人数） added by zhenxing.yan
		calculateSumPrice(model,productOrder);

		//查询正常游客和已成功转团游客
		List<Integer> delFlag = Lists.newArrayList();
		delFlag.add(Context.TRAVELER_DELFLAG_NORMAL);
		delFlag.add(Context.TRAVELER_DELFLAG_EXIT);
		delFlag.add(Context.TRAVELER_DELFLAG_EXITED);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUND);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUNDED);
		List<Traveler> travelerList = travelerService.findTravelerByOrderIdAndOrderType(Long.parseLong(productOrderId), productOrder.getOrderStatus(), delFlag);
		if (StringUtils.isNotBlank(productOrder.getTotalMoney())) {
			//根据游客查询金额币种id和数组
			getMoneyIdAndPrice(productOrder, travelerList, model);
			orderService.clearObject(productOrder);
			String totalMoneySerilNum=productOrder.getTotalMoney();
			productOrder.setTotalMoney(moneyAmountService.getMoneyStr(totalMoneySerilNum));

			//设置币种名称总结算价
			model.addAttribute("travelerSumClearPrice",moneyAmountService.getMoneyNameStr(totalMoneySerilNum));
		}
		
		List<HashMap<String, Object>> travelerMapList = Lists.newArrayList();
		HashMap<String, Object> travelerMap = null;
		List<Costchange> allCostChangeList = Lists.newArrayList();
		for (Traveler traveler : travelerList) {
			
			travelerMap = new HashMap<String, Object>();
			
			//转团成功后查询新团期信息
			if (Context.TRAVELER_DELFLAG_TURNROUNDED == traveler.getDelFlag()) {
				getChangeGroupInfo(productOrder, traveler, travelerMap);
			}
			
			List<Costchange> costChangeList = orderService.findCostchangeByTravelerId(traveler.getId());
			allCostChangeList.addAll(costChangeList);
			List<TravelerVisa> visaList = travelerVisaService.findVisaListByPid(traveler.getId());
			if(null != traveler.getIntermodalType() && traveler.getIntermodalType() == 1){
				if(traveler.getIntermodalId() != null){
					IntermodalStrategy intermodal = intermodalStrategyService.getOne(traveler.getIntermodalId());
					travelerMap.put("intermodal", intermodal);
				}
			}
			List<MoneyAmount> moneyList = moneyAmountService.findAmountBySerialNum(traveler.getPayPriceSerialNum());
			JSONArray payPriceArr = new JSONArray();
			for(MoneyAmount money:moneyList){
				JSONObject moneyJson = new JSONObject();
				moneyJson.put("currencyId", money.getCurrencyId());
				moneyJson.put("price", money.getAmount());
				payPriceArr.put(moneyJson);
			}
			travelerMap.put("payPriceJson", payPriceArr);
			travelerMap.put("traveler", traveler);
			travelerMap.put("costChangeList", costChangeList);
			
			List<MoneyAmount> travelerTotalWithCharge = travelerService.getTravelerTotalWithCharge(traveler.getId(), traveler.getCompanyId());
//			Currency travelercurrency = null;
//			String traveleTotalMoney = "";
//			for (MoneyAmount moneyAmount : travelerTotalWithCharge) {
//				travelercurrency = currencyService.findById(moneyAmount.getCurrencyId().longValue());
//				traveleTotalMoney = travelercurrency.getCurrencyMark() + moneyAmount.getAmount();
//			}
			// 
			String traveleTotalMoney = moneyAmountService.getMoneyStrFromAmountList("mark", travelerTotalWithCharge);
			travelerMap.put("traveleTotalMoney", traveleTotalMoney);
			
		    // 下次改版上线要走的逻辑
//		    boolean isAllowModifyDiscount = true;  //是否允许手动修改优惠
//		    List<ReviewNew> reviewNewList = reviewServiceNew.getOrderReviewList(productOrder.getId().toString(), productOrder.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString());
//		    if (CollectionUtils.isNotEmpty(reviewNewList)) {
//		    	Set<String> travelerIdsSet = new HashSet<>(); 
//		    	for (ReviewNew reviewNew : reviewNewList) {
//		    		// 只要订单有一条优惠审批是在处理中的，就不允许修改
//					if (reviewNew.getStatus() == ReviewConstant.REVIEW_STATUS_PROCESSING) {
//						isAllowModifyDiscount = false;
//						break;
//					}
//					// 获取订单中被审批通过的所有游客
//					if (reviewNew.getStatus() == ReviewConstant.REVIEW_STATUS_PASSED) {
//						String[] tempStr = null;
//						if (StringUtils.isNotBlank(reviewNew.getTravellerId())) {
//							tempStr = reviewNew.getTravellerId().split(",");
//							travelerIdsSet.addAll(new HashSet<>(Arrays.asList(tempStr)));
//						}
//					}
//				}
//		    	// “通过的游客集合”中包含的游客不允许修改
//		    	if (travelerIdsSet.contains(traveler.getId().toString())) {
//		    		isAllowModifyDiscount = false;
//				}
//			}
//		    traveler.setIsAllowModifyDiscount(isAllowModifyDiscount);
		    
			//某游客的总优惠金额 = 输入优惠金额 + 累计审批通过优惠金额（只有当优惠审批通过之后才算）
		    BigDecimal totalDiscountPrice = BigDecimal.ZERO;
			BigDecimal fixeDiscountPrice = traveler.getFixedDiscountPrice() == null ? BigDecimal.ZERO : traveler.getFixedDiscountPrice();  //输入优惠价 
			BigDecimal reviewedDiscountPrice = traveler.getReviewedDiscountPrice() == null ? BigDecimal.ZERO : traveler.getReviewedDiscountPrice();  //累计审批通过优惠金额
			totalDiscountPrice = fixeDiscountPrice.add(reviewedDiscountPrice);
			travelerMap.put("totalDiscountPrice", totalDiscountPrice.toString());
			//同行结算价
			List<MoneyAmount> costmoneyList = moneyAmountService.findAmountBySerialNum(traveler.getCostPriceSerialNum());  //同行价
			BigDecimal settleClearPrice;
			if (CollectionUtils.isEmpty(costmoneyList)) {
				settleClearPrice = BigDecimal.ZERO;
			} else {
				settleClearPrice = costmoneyList.get(0).getAmount();
			}
			travelerMap.put("settleClearPrice", settleClearPrice.subtract(totalDiscountPrice).toString());
			//查询返佣费用信息   add by zhangcl 2015年3月6日
			MoneyAmount rebatesMoney = moneyAmountService.findOneAmountBySerialNum(traveler.getRebatesMoneySerialNum());
			travelerMap.put("rebatesMoney", rebatesMoney);
			if(rebatesMoney != null && rebatesMoney.getCurrencyId() != null){
				Currency currency = currencyService.findCurrency(rebatesMoney.getCurrencyId().longValue());
				travelerMap.put("rebatesCurrency", currency);	//设置返佣币种的详情信息
			}
			
			//查询成本价信息   add by zhangcl 2015年3月6日
			List<MoneyAmount> costMoneyList = moneyAmountService.findAmountBySerialNum(traveler.getCostPriceSerialNum());
			JSONArray costPriceArr = new JSONArray();
			for(MoneyAmount money:costMoneyList){
				JSONObject moneyJson = new JSONObject();
				moneyJson.put("currencyId", money.getCurrencyId());
				moneyJson.put("price", money.getAmount());
				costPriceArr.put(moneyJson);
			}
			travelerMap.put("costPriceJson", costPriceArr);
			
			
			for (TravelerVisa tv: visaList){
				if (tv.getApplyCountry()!=null&&CountryUtils.getCountryId(tv.getApplyCountry().getName()) != null) {
					Long countryId = CountryUtils.getCountryId(tv.getApplyCountry().getName()).getId();
					tv.setSysCountryId(countryId);
					List<VisaProducts> visaProductList = visaProductsService.findVisaProductsByCountryId(countryId.intValue());
					Map<String,String> manorMap = new HashMap<String, String>();
				    List<Dict> manorList = Lists.newArrayList();
				    List<Dict> visaTypeList = Lists.newArrayList();
				    for (VisaProducts vp : visaProductList){
						String manorKey = vp.getCollarZoning();
						Dict manorDict = dictService.findByValueAndType(manorKey, Context.FROM_AREA);
						if (!manorMap.containsKey(manorKey)  && manorDict != null) {
							manorMap.put(manorKey, manorDict.getLabel());
							manorList.add(manorDict);
						}
				    }
				    tv.setManorList(manorList);
				    if (manorList.size() > 0) {
				    	String manorId =  tv.getManorId() != null ? tv.getManorId().toString() : "";
				    	if (StringUtils.isBlank(manorId)) {
				    		manorId =  manorList.get(0).getValue();
				    	}
				    	List<VisaProducts> vpList = visaProductsService.findVisaProductsByCountryIdAndManor(countryId.intValue(), manorId);
						for (VisaProducts vp: vpList) {
							Dict dict = dictService.findByValueAndType(vp.getVisaType().toString(), Context.DICT_TYPE_NEW_VISATYPE);
							if (!visaTypeList.contains(dict)) {
								visaTypeList.add(dict);
							}
						}
				    }
					tv.setVisaTypeList(visaTypeList);
				}
			}
			
			StringBuffer docIds = new StringBuffer("");
			List<TravelerFile> passportfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.PASSPORTS_TYPE,"0");
			List<TravelerFile> idcardfrontfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.IDCARD_FRONT_TYPE,"0");
			List<TravelerFile> entryformfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.ENTRY_FORM_TYPE,"0");
			List<TravelerFile> housefile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.HOUSE_TYPE,"0");
			List<TravelerFile> photofile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.PHOTO_TYPE,"0");
			List<TravelerFile> idcardbackfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.IDCARD_BACK_TYPE,"0");
			List<TravelerFile> residencefile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.RESIDENCE_TYPE,"0");
			List<TravelerFile> otherfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.OTHER_TYPE,"0");
			List<TravelerFile> visaannexfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.VISA_TYPE,"0");
			travelerMap.put("visaList", visaList);
			travelerMap.put("passportfile", passportfile);
			travelerMap.put("passportfileNameStr", getFileNameStr(passportfile));
			travelerMap.put("idcardfrontfile", idcardfrontfile);
			travelerMap.put("idcardfrontfileNameStr", getFileNameStr(idcardfrontfile));
			travelerMap.put("entryformfile", entryformfile);
			travelerMap.put("entryformfileNameStr", getFileNameStr(entryformfile));
			travelerMap.put("housefile", housefile);
			travelerMap.put("housefileNameStr", getFileNameStr(housefile));
			travelerMap.put("photofile", photofile);
			travelerMap.put("photofileNameStr", getFileNameStr(photofile));
			travelerMap.put("idcardbackfile", idcardbackfile);
			travelerMap.put("idcardbackfileNameStr", getFileNameStr(idcardbackfile));
			travelerMap.put("residencefile", residencefile);
			travelerMap.put("residencefileNameStr", getFileNameStr(residencefile));
			travelerMap.put("otherfile", otherfile);
			travelerMap.put("otherfileNameStr", getFileNameStr(otherfile));
			travelerMap.put("visaannexfile", visaannexfile);
			travelerMap.put("visaannexfileNameStr", getFileNameStr(visaannexfile));
			if (passportfile != null && passportfile.size() > 0) {
				for (TravelerFile travelerFile : passportfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (photofile != null && photofile.size() > 0) {
				for (TravelerFile travelerFile : photofile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (idcardfrontfile != null && idcardfrontfile.size() > 0) {
				for (TravelerFile travelerFile : idcardfrontfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (idcardbackfile != null && idcardbackfile.size() > 0) {
				for (TravelerFile travelerFile : idcardbackfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (entryformfile != null && entryformfile.size() > 0) {
				for (TravelerFile travelerFile : entryformfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (housefile != null && housefile.size() > 0) {
				for (TravelerFile travelerFile : housefile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (residencefile != null && residencefile.size() > 0) {
				for (TravelerFile travelerFile : residencefile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (otherfile != null && otherfile.size() > 0) {
				for (TravelerFile travelerFile : otherfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (visaannexfile != null && visaannexfile.size() > 0) {
				for (TravelerFile travelerFile : visaannexfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (StringUtils.isNotBlank(docIds)) {
				travelerMap.put("docIds", docIds.substring(0, docIds.length()-1));
			}
			//查询签证子订单游客：一个单团游客可能有多个签证，所以会有多个签证对应游客
			List<Traveler> visaTravelerList = travelerService.findTravelersByMainTravelerId(traveler.getId());
			if (CollectionUtils.isNotEmpty(visaTravelerList)) {
				List<String> serialNumList = Lists.newArrayList();
				String visaOrderIdStr = "";
				for (int i=0;i<visaTravelerList.size();i++) {
					Traveler visaTraveler = visaTravelerList.get(i);
					Visa visa = visaDao.findByTravelerId(visaTraveler.getId());
					if (visa != null && visa.getTotalDeposit() != null) {
						serialNumList.add(visa.getTotalDeposit());
					}
					if (i != visaTravelerList.size()-1) {
						visaOrderIdStr += visaTraveler.getOrderId() + ",";
					} else {
						visaOrderIdStr += visaTraveler.getOrderId();
					}
				}
				travelerMap.put("visaOrderIdStr", visaOrderIdStr);
				
				if (CollectionUtils.isNotEmpty(serialNumList)) {
					travelerMap.put("visaDeposit", moneyAmountService.getMoneyStr(serialNumList));
				}
			}
			
			
			travelerMapList.add(travelerMap);
		}
		getCostChangeStr(allCostChangeList, model);
		model.addAttribute("travelers", travelerMapList);
		List<OrderContacts> orderContacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(productOrder.getId(), productOrder.getOrderStatus());
		if (CollectionUtils.isEmpty(orderContacts)) {
			orderContacts.add(new OrderContacts());
		}
		model.addAttribute("orderContactsSrc", orderContacts);  //保存的是最原始的联系人---供非签约渠道订单修改使用
		if (CollectionUtils.isNotEmpty(orderContacts)) {
			model.addAttribute("orderContacts", orderContacts);
			net.sf.json.JSONArray currencyListJsonArray = net.sf.json.JSONArray.fromObject(orderContacts);
	        model.addAttribute("orderContactsListJsonArray", currencyListJsonArray.toString());
		}
		//add by yang.jiang
		//渠道商的联系地址
		List<SupplierContacts> contacts = supplierContactsService.findAllContactsByAgentInfo(Long.parseLong(agentId));  //取出渠道商所有联系人（包括第一联系人）
		String address = agentinfoService.getAddressStrById(Long.parseLong(agentId));
		model.addAttribute("address", address == null ? "" : address);
		//渠道商转换为json
		for (SupplierContacts supplierContacts : contacts) {
			supplierContacts.setAgentAddressFull(address);
		}
		//转换成view实体
		List<SupplierContactsView> contactsView = Lists.newArrayList();
		for (SupplierContacts supplierContacts : contacts) {			
			SupplierContactsView splContactsView = new SupplierContactsView();
			BeanUtils.copyProperties(splContactsView, supplierContacts);
			contactsView.add(splContactsView);
		}
		model.addAttribute("contacts", contacts);
		model.addAttribute("contactsView", contactsView);
		String contactsJsonStr = supplierContactsService.contacts2Json(contacts);
		org.json.JSONArray contactArray = supplierContactsService.contacts2JsonArray(contacts);
		model.addAttribute("contactsJsonStr", contactsJsonStr);
		model.addAttribute("contactArray", contactArray);
		
				
		//订单是否允许添加多个渠道联系人信息
		Integer allowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();
		model.addAttribute("allowAddAgentInfo", allowAddAgentInfo);
		//订单是否允许渠道联系人信息输入和修改
		Integer allowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();
		model.addAttribute("allowModifyAgentInfo", allowModifyAgentInfo);
		Agentinfo agentinfo = agentinfoService.loadAgentInfoById(Long.parseLong(agentId));
		agentinfo.setAgentAddressFull(address);
		model.addAttribute("agentinfo", agentinfo);
        ActivityGroupReserve groupReserve = StockService.findGroupReserve(productOrder.getOrderCompany(), product.getId(), productGroup.getId());
        model.addAttribute("groupReserve",groupReserve);
        
        model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(activityKind));
	    model.addAttribute("product",product);
	    model.addAttribute("productGroup",productGroup);
	    model.addAttribute("productorder",productOrder);
	    model.addAttribute("agentId",agentId);
	    model.addAttribute("activityKind", activityKind);
	    // 团队类型----djw---
	 	if(product.getActivityKind()!=null){
	 		model.addAttribute("groupType", OrderCommonUtil.getChineseOrderType(product.getActivityKind().toString()));// 团队类型
	 	}
	    List<Agentinfo>  agentinfoList = agentinfoService.findAllAgentinfoBySalerId(productOrder.getSalerId());
	    model.addAttribute("agentinfoList", agentinfoList);	//add by zhangcl
	    //add by yang.jiang 团期优惠额度
	    Map<String, Object> discountMap = groupService.getDiscountMapByGroupId(productGroup.getId().toString());
	    model.addAttribute("discountMap", discountMap);
	    // 发票
	    List<Orderinvoice> invoices = orderinvoiceService.findCreatedInvoiceByOrder(Integer.parseInt(productOrder.getId().toString()), productOrder.getOrderStatus());
	    model.addAttribute("invoices", invoices);
	    // 收据
	    List<OrderReceipt> receipts = orderreceiptService.findCreatedReceiptByOrder(Integer.parseInt(productOrder.getId().toString()), productOrder.getOrderStatus());
	    model.addAttribute("receipts", receipts);
	    //供应商
	    User creatUser = productGroup.getCreateBy();
	    Office office = creatUser.getCompany();
	    model.addAttribute("office", office);
	    
    }
    

	/**
     * 获取文件名字字符串
     * @param passportfile
     * @return
     */
    private String getFileNameStr(List<TravelerFile> passportfile) {
    	String nameStr = "";
    	for (TravelerFile travelerFile : passportfile) {			
    		nameStr += travelerFile.getFileName() + ";";
		}
		return nameStr;
	}

    
    /**
     * 查询转团后团期信息
     * @param order
     * @param traveler
     * @param travelerMap
     */
    private void getChangeGroupInfo(ProductOrderCommon order, Traveler traveler, HashMap<String, Object> travelerMap) {
    	List<Review> listReview = reviewService.findReview(order.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, 
    			order.getId().toString(), traveler.getId(), true);
    	if (CollectionUtils.isNotEmpty(listReview)) {
    		Map<String,String> map = reviewService.findReview(listReview.get(0).getId());
    		String groupCode = map.get(TransFerGroup.KEY_NEW_GROUPCODE);
    		if (StringUtils.isNotBlank(groupCode)) {
    			ActivityGroup group = activityGroupService.findByGroupCode(groupCode);
    			if (group != null) {
					travelerMap.put("changeGroupCode", group.getGroupCode());
					travelerMap.put("changeProductType", OrderCommonUtil.getChineseOrderType(group.getTravelActivity().getActivityKind().toString()));
					travelerMap.put("changeProductName", group.getTravelActivity().getAcitivityName());
					travelerMap.put("changeCreateName", UserUtils.getUser(listReview.get(0).getCreateBy()).getName());
				}
    		}
    	} else {
    		List<ReviewNew> reviewNewList = reviewNewService.getReviewList(order.getId().toString(),
    				traveler.getId().toString(), order.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());
    		if (CollectionUtils.isNotEmpty(reviewNewList)) {
    			ReviewNew review = reviewNewService.getReview(reviewNewList.get(0).getId());
    			String groupId = review.getExtend3();
    			if (StringUtils.isNoneBlank(groupId)) {
    				ActivityGroup group = activityGroupService.findById(Long.parseLong(groupId));
    				if (group != null) {
    					travelerMap.put("changeGroupCode", group.getGroupCode());
    					travelerMap.put("changeProductType", OrderCommonUtil.getChineseOrderType(group.getTravelActivity().getActivityKind().toString()));
    					travelerMap.put("changeProductName", group.getTravelActivity().getAcitivityName());
    					travelerMap.put("changeCreateName", UserUtils.getUser(review.getCreateBy()).getName());
    				}
    			}
    		}
    	}
    }
    
    /**
     * 计算其他费用总和
     * @param allCostChangeList
     * @param model
     */
    private void getCostChangeStr(List<Costchange> allCostChangeList, Model model) {
    	if (CollectionUtils.isNotEmpty(allCostChangeList)) {
			String costChangeStr = "";
			BigDecimal costChangePrice = null;
			List<String> currencyMarkList = Lists.newArrayList();
			List<String> currencyMarkTempList = Lists.newArrayList();
			List<BigDecimal> currencyPriceList = Lists.newArrayList();
			for (Costchange cost : allCostChangeList) {
				if (cost.getCostSum() != null && cost.getPriceCurrency() != null) {
					currencyMarkList.add(cost.getPriceCurrency().getCurrencyMark());
					currencyPriceList.add(cost.getCostSum());
				}
			}
			if (CollectionUtils.isNotEmpty(currencyMarkList)) {
				for (int i=0;i<currencyMarkList.size();i++) {
					String mark = currencyMarkList.get(i);
					if (i==0 || !currencyMarkTempList.contains(mark)) {
						costChangePrice = currencyPriceList.get(i);
						for (int j=i+1;j<currencyMarkList.size();j++) {
							String markTwo = currencyMarkList.get(j);
							if (mark.equals(markTwo)) {
								costChangePrice = costChangePrice.add(currencyPriceList.get(j));
							}
						}
						costChangeStr += mark + costChangePrice.toString() + " ";
					}
					currencyMarkTempList.add(mark);
				}
			}
			model.addAttribute("costChangeStr", costChangeStr);
		}
    }
    
    /**
     * 获取游客结算价总和与订单金额差价
     * @param travelerList
     * @param model
     * @return
     */
    private void getMoneyIdAndPrice(ProductOrderCommon order, List<Traveler> travelerList, Model model) {
    	List<String> serialNumList = Lists.newArrayList();
    	if (CollectionUtils.isNotEmpty(travelerList)) {
    		for (Traveler traveler : travelerList) {
    			serialNumList.add(traveler.getPayPriceSerialNum());
    		}
    	}
    }
  
	
	/**
	 * 
	 *  功能: 根据出境游或者国内游的城市列表
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-10-22 下午4:25:47
	 *  @param targetAreaIdList
	 *  @return
	 */
	private List<TravelerVisaInfo> getTargetForeignCountry(List<Area> targetAreaIdList, Model model){
		List<TravelerVisaInfo> targetList = Lists.newArrayList();
		StringBuilder sbOriginal = new StringBuilder();
		String strOriginalOther = "";
		StringBuilder sbCopyOriginal = new StringBuilder();
		String strCopyOriginalOther = "";
		List<Long> countryIds = Lists.newArrayList();
		for(Area area: targetAreaIdList){
			//外国区域祖类都为"出境游"，编号为100000，国内区域祖类为"国内游"，编号为200000
			if(area.getParentIds().indexOf(Context.FREE_TRAVEL_FOREIGN) > 0){
				TravelerVisaInfo tv = null;
				Long countryId = null;
				Map<String,String> manorMap = new HashMap<String, String>();
			    List<Dict> manorList =Lists.newArrayList(); 
			    List<Dict> visaTypeList =Lists.newArrayList();
			    //判断国家字典中是否存在目的地国家
			    if (("3".equals(area.getType()) || "4".equals(area.getType())) && area.getParent() != null) {
			    	area = area.getParent();
			    	if (countryIds.contains(area.getId())) {
			    		continue;
			    	} else {
			    		countryIds.add(area.getId());
			    	}
			    } else if ("2".equals(area.getType())) {
			    	countryIds.add(area.getId());
			    } else if ("1".equals(area.getType())) {
			    	continue;
			    }
				if(CountryUtils.getCountryId(area.getName()) == null){
				/** 不是国家的地区给忽略 */
//					tv = new TravelerVisaInfo();
//					tv.setApplyCountryId(area.getId());
//					tv.setApplyCountryName(area.getName());
//				    tv.setManorList(manorList);
//					tv.setVisaTypeList(visaTypeList);
//					targetList.add(tv);
				}else{
					countryId = CountryUtils.getCountryId(area.getName()).getId();
					List<VisaProducts> visaProductList = visaProductsService.findVisaProductsByCountryId(countryId.intValue());
					if(visaProductList != null && visaProductList.size() > 0){
						tv = new TravelerVisaInfo();
						tv.setApplyCountryId(area.getId());
						tv.setSysCountryId(countryId);
						tv.setApplyCountryName(area.getName());
						for(VisaProducts vp: visaProductList){
							String manorKey = vp.getCollarZoning();
							Dict manorDict = dictService.findByValueAndType(manorKey, Context.FROM_AREA);
							if(!manorMap.containsKey(manorKey) && manorDict != null){
								manorMap.put(manorKey, manorDict.getLabel());
								manorList.add(manorDict);
							} 
							if(StringUtils.isNotBlank(vp.getOriginal_Project_Type())){
								if(sbOriginal.length() == 0){
									sbOriginal.append(vp.getOriginal_Project_Type());
								}else{
									sbOriginal.append("," + vp.getOriginal_Project_Type());
								}
							}
							if(StringUtils.isNotBlank(vp.getOriginal_Project_Name())){
								if(StringUtils.isNotBlank(strOriginalOther)){
									if(StringUtils.isNotBlank(vp.getOriginal_Project_Name())){
										if(!strOriginalOther.contains(vp.getOriginal_Project_Name())){
											strOriginalOther += "、" + vp.getOriginal_Project_Name();
										}
									}
								}else{
									strOriginalOther += vp.getOriginal_Project_Name();
								}
							}
							if(StringUtils.isNotBlank(vp.getCopy_Project_Type())){
								if(sbCopyOriginal.length() == 0){
									sbCopyOriginal.append(vp.getCopy_Project_Type());
								}else{
									sbCopyOriginal.append("," + vp.getCopy_Project_Type());
								}
							}
							if(StringUtils.isNotBlank(vp.getCopy_Project_Name())){
								if(StringUtils.isNotBlank(strCopyOriginalOther)){
									if(StringUtils.isNotBlank(vp.getCopy_Project_Name())){
										if(!strCopyOriginalOther.contains(vp.getCopy_Project_Name())){
											strCopyOriginalOther  +=  "、" + vp.getCopy_Project_Name();
										}
									}
								}else{
									strCopyOriginalOther  += vp.getCopy_Project_Name();
								}
							}
						}
						tv.setManorList(manorList);
						List<VisaProducts> vpList = visaProductsService.findVisaProductsByCountryIdAndManor(countryId.intValue(), manorList.get(0).getValue());
						for(VisaProducts vp: vpList){
							Dict dict = dictService.findByValueAndType(vp.getVisaType().toString(), Context.DICT_TYPE_NEW_VISATYPE);
							if (!visaTypeList.contains(dict)) {
								visaTypeList.add(dict);
							}
						}
						tv.setVisaTypeList(visaTypeList);
						targetList.add(tv);
					}else{
						tv = new TravelerVisaInfo();
						tv.setApplyCountryId(area.getId());
						tv.setApplyCountryName(area.getName());
					    tv.setManorList(manorList);
						tv.setVisaTypeList(visaTypeList);
						targetList.add(tv);
					}
				}
			}
		}
		String strOriginal = "";
		if(sbOriginal.length() > 0){
			String[] arrOriginal = sbOriginal.toString().split(",");
			for(String original : arrOriginal){
				if(StringUtils.isBlank(strOriginal)){
					strOriginal += original;
				}else{
					if(!strOriginal.contains(original) && !"2".equals(original)){
						strOriginal += "," + original;
					}
				}
			}
		}
		if(StringUtils.isNotBlank(strOriginal)){
			if(StringUtils.isNotBlank(strOriginalOther)){
				strOriginal = strOriginal.replace("0", "护照").replace("1", "身份证")
						.replace("3", "电子照片").replace("4", "申请表格").replace("5", "户口本").replace("6", "房产证")
						.replace(",", "、") + "、" + strOriginalOther;
			}else{
				strOriginal = strOriginal.replace("0", "护照").replace("1", "身份证")
						.replace("3", "电子照片").replace("4", "申请表格").replace("5", "户口本").replace("6", "房产证")
						.replace(",", "、");
			}
		}
		else{
			if(StringUtils.isNotBlank(strOriginalOther)){
				strOriginal = strOriginalOther;
			}
		}
		model.addAttribute("original", strOriginal);
		String strCopyOriginal = "";
		if(sbCopyOriginal.length() > 0){
			String[] arrCopyOriginal = sbCopyOriginal.toString().split(",");
			for(String copyOriginal : arrCopyOriginal){
				if(StringUtils.isBlank(strCopyOriginal)){
					strCopyOriginal += copyOriginal;
				}else{
					if(!strCopyOriginal.contains(copyOriginal) && !"2".equals(copyOriginal)){
						strCopyOriginal += "," + copyOriginal;
					}
				}
			}
		}
		if(StringUtils.isNotBlank(strCopyOriginal)){
			if(StringUtils.isNotBlank(strCopyOriginalOther)){
				strCopyOriginal = strCopyOriginal.replace("0", "户口本").replace("1", "房产证")
						.replace("3", "护照").replace("4", "身份证").replace("5", "电子照片").replace("6", "申请表格")
						.replace(",", "、") + "、" + strCopyOriginalOther;
			}else{
				strCopyOriginal = strCopyOriginal.replace("0", "户口本").replace("1", "房产证")
						.replace("3", "护照").replace("4", "身份证").replace("5", "电子照片").replace("6", "申请表格")
						.replace(",", "、");
			}
		}else{
			if(StringUtils.isNotBlank(strCopyOriginalOther)){
				strCopyOriginal = strCopyOriginalOther;
			}
		}
		model.addAttribute("copyoriginal", strCopyOriginal);
		return targetList;
	}

	
	/**
	 * 
	 *  功能: 设置预定页面控件数据源
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-11-12 下午2:32:05
	 *  @param model
	 *  @param productId
	 */
	private void setControlModel(Model model,TravelActivity product){
		//获取联运信息
		if(product.getActivityAirTicket() != null){
			List<IntermodalStrategy> intermodalList =  product.getActivityAirTicket().getIntermodalStrategies();
		    model.addAttribute("intermodalList", intermodalList);
		}
	    
		//获取国籍信息
	    List<Country> countryList = CountryUtils.getCountrys();
        model.addAttribute("countryList", countryList);
	    
	    //用于显示签证国家
        model.addAttribute("targetForeignCountry", getTargetForeignCountry(areaService.findAreasByActivity(product.getId()),model));
	    
        //获取护照类型信息
        Map<String,String> passportTypeList = DictUtils.getVisaTypeMap(Context.PASSPORT_TYPE);
        model.addAttribute("passportTypeList", passportTypeList);
        
        //获取产品行程单
        String docInfoId = "";
        Set<ActivityFile> activityFiles = product.getActivityFiles();
        for (ActivityFile activityFile : activityFiles) {
        	docInfoId += activityFile.getSrcDocId();
		}
        model.addAttribute("docInfoId", docInfoId);
	}
	
	/**
	 *  功能: 设置币种相关信息
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-11-12 下午2:33:00
	 *  @param model
	 *  @param product 产品对象
	 *  @param activityKind 产品类型
	 *  @param priceType 报名时使用的价格类型 0：同行价 1：直客价
	 */
	private void setCurrency(Model model, String orderId, ActivityGroup productGroup, String activityKind, List<String> serialNumList, Integer priceType){
		//同行价：币种id、金额（成人、儿童、特殊）
		Long adultCurrencyId = null;
		BigDecimal adultPrice = null;
		Long childCurrencyId = null;
		BigDecimal childPrice = null;
		Long specialCurrencyId = null;
		BigDecimal specialPrice = null;
		//直客价：币种id、金额（成人、儿童、特殊）
		Long suggestAdultCurrencyId = null;
		BigDecimal suggestAdultPrice = null;
		Long suggestChildCurrencyId = null;
		BigDecimal suggestChildPrice = null;
		Long suggestSpecialCurrencyId = null;
		BigDecimal suggestSpecialPrice = null;
		//单房差
		Long singleDiffCurrencyId = null;
		BigDecimal singleDiff = null;
		//定金
		Long payDepositCurrencyId = null;
		BigDecimal payDeposit = null;
		if(StringUtils.isNotBlank(orderId)){
			//(订单使用价类型)默认同行价
			MoneyAmount adultMoneyAmount = moneyAmountService.findAmountBySerialNum(serialNumList.get(0)).get(0);
			adultCurrencyId = adultMoneyAmount.getCurrencyId().longValue();
			adultPrice = adultMoneyAmount.getAmount();//
			if(adultPrice==null){
				adultPrice=new BigDecimal(0);
			}
			MoneyAmount childMoneyAmount = moneyAmountService.findAmountBySerialNum(serialNumList.get(1)).get(0);
			childCurrencyId = childMoneyAmount.getCurrencyId().longValue();
			childPrice = childMoneyAmount.getAmount();//
			if(childPrice==null){
				childPrice=new BigDecimal("0");
			}
			List<MoneyAmount> specialMoneyAmountList = moneyAmountService.findAmountBySerialNum(serialNumList.get(2));
			if (CollectionUtils.isNotEmpty(specialMoneyAmountList)) {
				MoneyAmount specialMoneyAmount = specialMoneyAmountList.get(0);
				specialCurrencyId = specialMoneyAmount.getCurrencyId().longValue();
				specialPrice = specialMoneyAmount.getAmount();//
			} else {
				specialPrice = new BigDecimal(0);
			}
			
			//单房差、定金
			MoneyAmount singleDiffMoneyAmount = moneyAmountService.findAmountBySerialNum(serialNumList.get(3)).get(0);
			singleDiffCurrencyId = singleDiffMoneyAmount.getCurrencyId().longValue();
			singleDiff = singleDiffMoneyAmount.getAmount();//
			MoneyAmount payDepositMoneyAmount = moneyAmountService.findAmountBySerialNum(serialNumList.get(4)).get(0);
			payDepositCurrencyId = payDepositMoneyAmount.getCurrencyId().longValue();
			payDeposit = payDepositMoneyAmount.getAmount();//
		}else{
			String currency = productGroup.getCurrencyType();
		    String[] currencyArr = {"1","1","1","1","1","1","1","1"};
		    if(StringUtils.isNotBlank(currency)){
		    	currencyArr = currency.split(",");
		    	//游轮产品有6个curreny值，分别是 同行价1/2、同行价3/4、直客价1/2、直客价3/4、定金、单房差
		    	if (Context.ORDER_TYPE_CRUISE.toString().equals(activityKind)) {
		    		adultCurrencyId = Long.parseLong(currencyArr[0]);
					childCurrencyId = Long.parseLong(currencyArr[1]);
					specialCurrencyId = 1L;
					suggestAdultCurrencyId = Long.parseLong(currencyArr[2]);
					suggestChildCurrencyId = Long.parseLong(currencyArr[3]);
					suggestSpecialCurrencyId = 1L;
					payDepositCurrencyId = Long.parseLong(currencyArr[4]);
		    		singleDiffCurrencyId = Long.parseLong(currencyArr[5]);
				//散拼产品有8个值，分别是 同行价（成人、儿童、特殊）、直客价（成人、儿童、特殊）、定金、单房差	
				} else if (Context.ORDER_TYPE_SP.toString().equals(activityKind)) {
					adultCurrencyId = Long.parseLong(currencyArr[0]);
					childCurrencyId = Long.parseLong(currencyArr[1]);
					specialCurrencyId = Long.parseLong(currencyArr[2]);
					suggestAdultCurrencyId = Long.parseLong(currencyArr[3]);
					suggestChildCurrencyId = Long.parseLong(currencyArr[4]);
					suggestSpecialCurrencyId = Long.parseLong(currencyArr[5]);
					payDepositCurrencyId = Long.parseLong(currencyArr[6]);
		    		singleDiffCurrencyId = Long.parseLong(currencyArr[7]);
		    	//其他团期类型产品有5个值，同行价（成人、儿童、特殊）、定金、单房差
				} else {
					adultCurrencyId = Long.parseLong(currencyArr[0]);
					childCurrencyId = Long.parseLong(currencyArr[1]);
					specialCurrencyId = Long.parseLong(currencyArr[2]);
					payDepositCurrencyId = Long.parseLong(currencyArr[3]);
		    		singleDiffCurrencyId = Long.parseLong(currencyArr[4]);
				}
		    }else{
		    	adultCurrencyId = 1L;
				childCurrencyId = 1L;
				specialCurrencyId = 1L;
				suggestAdultCurrencyId = 1L;
				suggestChildCurrencyId = 1L;
				suggestSpecialCurrencyId = 1L;
				singleDiffCurrencyId = 1L;
				payDepositCurrencyId = 1L;
		    }
		    adultPrice = productGroup.getSettlementAdultPrice();
		    childPrice = productGroup.getSettlementcChildPrice();
		    specialPrice = productGroup.getSettlementSpecialPrice();		    
		    suggestAdultPrice = productGroup.getSuggestAdultPrice();
		    suggestChildPrice = productGroup.getSuggestChildPrice();
		    suggestSpecialPrice = productGroup.getSuggestSpecialPrice();
		    singleDiff = productGroup.getSingleDiff();
		    payDeposit = productGroup.getPayDeposit();
		}
		this.setCurrencyModel(model, "adult", adultCurrencyId, adultPrice);
		this.setCurrencyModel(model, "child", childCurrencyId, childPrice);
		this.setCurrencyModel(model, "special", specialCurrencyId, specialPrice);
		this.setCurrencyModel(model, "suggestAdult", suggestAdultCurrencyId, suggestAdultPrice);
		this.setCurrencyModel(model, "suggestChild", suggestChildCurrencyId, suggestChildPrice);
		this.setCurrencyModel(model, "suggestSpecial", suggestSpecialCurrencyId, suggestSpecialPrice);
		this.setCurrencyModel(model, "singleDiff", singleDiffCurrencyId, singleDiff);

	    model.addAttribute("payDepositCurrencyId", payDepositCurrencyId);
	    model.addAttribute("payDeposit", payDeposit);
	    //获取币种信息
        List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
        StringBuilder sbBuilder = new StringBuilder();
        for(Currency curr:currencyList){
        	sbBuilder.append("<option ");
        	sbBuilder.append(" value=\"");
        	sbBuilder.append(curr.getId()).append("\" lang=\"" + curr.getCurrencyMark() + "\">").append(curr.getCurrencyName()).append("</option>");
        }
        model.addAttribute("currencyOptions", sbBuilder.toString()); 
        model.addAttribute("currencyList", currencyList); 
        JsonConfig currencyconfig = new JsonConfig();
        currencyconfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				if (arg1.equals("id") || arg1.equals("currencyName")|| arg1.equals("currencyMark")) {
					return false;
				} else {
					return true;
				}
			}
		});
        net.sf.json.JSONArray currencyListJsonArray = net.sf.json.JSONArray.fromObject(currencyList, currencyconfig);
        model.addAttribute("currencyListJsonArray", currencyListJsonArray.toString());
        model.addAttribute("priceType", priceType==null?0:priceType);
	}

	 /**
	  * 计算订单同行价（成人价*数量+儿童价*数量+特殊价*数量）
	  * @return
      */
	 private void calculateSumPrice(Model model,ProductOrderCommon productOrder){
		 Map<String,BigDecimal> totalPrice=new LinkedHashMap<>();
		 Map<String,String> currencyMarkMap=new HashMap<>();
		 Map<String,String> currencyNameMap=new HashMap<>();

		 Map<String,Object> modelMap=model.asMap();
		 if(modelMap.containsKey("adultCurrencyId")){//计算成人价格
			 String currencyId=modelMap.get("adultCurrencyId").toString();
			 currencyMarkMap.put(currencyId,modelMap.get("adultCurrencyMark").toString());
			 currencyNameMap.put(currencyId,modelMap.get("adultCurrencyName").toString());
			 totalPrice.put(currencyId,((BigDecimal)modelMap.get("adultPrice")).multiply(new BigDecimal(productOrder.getOrderPersonNumAdult())));
		 }
		 if(modelMap.containsKey("childCurrencyId")){//计算儿童价
			 String currencyId=modelMap.get("childCurrencyId").toString();
			 if(currencyMarkMap.containsKey(currencyId)){
				 totalPrice.put(currencyId,totalPrice.get(currencyId).add(((BigDecimal)(modelMap.get("childPrice")==null?BigDecimal.ZERO:modelMap.get("childPrice"))).multiply(new BigDecimal(productOrder.getOrderPersonNumChild()))));
			 }else{
				 currencyMarkMap.put(currencyId,modelMap.get("childCurrencyMark").toString());
				 currencyNameMap.put(currencyId,modelMap.get("childCurrencyName").toString());
				 totalPrice.put(currencyId,((BigDecimal)modelMap.get("childPrice")).multiply(new BigDecimal(productOrder.getOrderPersonNumChild())));
			 }
		 }
		 if(modelMap.containsKey("specialCurrencyId")){//计算特殊价
			 Object specialCurrencyIdObject = modelMap.get("specialCurrencyId");
			 String currencyId= null;
			 if (specialCurrencyIdObject != null && StringUtils.isNotBlank(specialCurrencyIdObject.toString())) {
				 currencyId = specialCurrencyIdObject.toString();
				 if(currencyMarkMap.containsKey(currencyId)){
					 totalPrice.put(currencyId,totalPrice.get(currencyId).add(((BigDecimal)(modelMap.get("specialPrice")==null?BigDecimal.ZERO:modelMap.get("specialPrice"))).multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()))));
				 }else{
					 currencyMarkMap.put(currencyId,modelMap.get("specialCurrencyMark").toString());
					 currencyNameMap.put(currencyId,modelMap.get("specialCurrencyName").toString());
					 totalPrice.put(currencyId,((BigDecimal)(modelMap.get("specialPrice")==null?BigDecimal.ZERO:modelMap.get("specialPrice"))).multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()==null?0:productOrder.getOrderPersonNumSpecial())));
				 }
			 }
		 }

		 //组装同行价字符串
		 StringBuilder stringBuilder=new StringBuilder();
		 for (String currencyId : totalPrice.keySet()) {
			 stringBuilder.append(currencyNameMap.get(currencyId)).append(totalPrice.get(currencyId).toString()).append("+");
		 }
		 //设置同行价
		 model.addAttribute("travelerSumPrice",stringBuilder.subSequence(0,stringBuilder.length()-1));
	 }
	
    /**
     * 
     * @param model
     * @param keyName
     * @param currencyArr
     */
	private void setCurrencyModel(Model model, String keyName, Long currencyId, BigDecimal price){
		Currency currency = currencyService.findCurrency(currencyId);
		if(currency != null){
			model.addAttribute(keyName + "CurrencyId", currencyId);
			model.addAttribute(keyName + "CurrencyMark", currency.getCurrencyMark());
			model.addAttribute(keyName + "CurrencyName", currency.getCurrencyName());
			model.addAttribute(keyName + "Price", price);
		}
	}

	
}