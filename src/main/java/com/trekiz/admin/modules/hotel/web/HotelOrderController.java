/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupLowprice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelOrderPrice;
import com.trekiz.admin.modules.hotel.entity.HotelRoom;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.input.HotelOrderInput;
import com.trekiz.admin.modules.hotel.model.HotelControlDetailModel;
import com.trekiz.admin.modules.hotel.query.ActivityHotelQuery;
import com.trekiz.admin.modules.hotel.query.HotelMoneyAmountQuery;
import com.trekiz.admin.modules.hotel.query.HotelOrderQuery;
import com.trekiz.admin.modules.hotel.query.HotelTravelerPapersTypeQuery;
import com.trekiz.admin.modules.hotel.query.HotelTravelerQuery;
import com.trekiz.admin.modules.hotel.query.HotelTravelervisaQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupLowpriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealRiseService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupPriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupRoomService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelControlService;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderPriceService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.hotel.service.HotelRoomService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerPapersTypeService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerService;
import com.trekiz.admin.modules.hotel.service.HotelTravelervisaService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.island.entity.HotelRebates;
import com.trekiz.admin.modules.island.service.IslandReviewService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;
import com.trekiz.admin.modules.pay.service.PayHotelOrderService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierContactsView;
import com.trekiz.admin.modules.supplier.service.SupplierContactsService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelOrder")
public class HotelOrderController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelorder/orderList";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelOrder/list";
	protected static final String FORM_PAGE = "modules/hotel/hotelorder/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelorder/show";
	protected static final String HOTEL_ORDER_PRODUCT_LIST = "modules/hotel/hotelorder/hotelOrderProductList";
	protected static final String HOTEL_ORDER_GROUP_LIST = "modules/hotel/hotelorder/hotelOrderGroupList";
	protected static final String PRE_REPORT_HOTEL_PRODUCT_PAGE = "modules/hotel/hotelorder/preReportHotelProduct";
	
	//修改支付凭证跳转链接
	protected static final String MODIFY_PAY_VOUCHER = "modules/hotel/hotelorder/modifyPayVoucher";
	//修改支付凭证成功后跳转链接
	protected static final String UPLOAD_VO_SUCCESS = "modules/hotel/hotelorder/uploadVoSuccess";
	
	//退团列表
	protected static final String VIEW_EXIT_GROUP = "modules/hotel/hotelorder/viewExitGroup";
	//退团申请页
	protected static final String VIEW_EXIT_GROUP_INFO = "modules/hotel/hotelorder/viewExitGroupInfo";
	//申请退团
	protected static final String APPLY_EXIT_GROUP = "modules/hotel/hotelorder/applyExitGroup";
	
	//详情
	protected static final String ORDER_HOTEL_ORDER_DETAIL = "modules/hotel/hotelorder/orderHotelDetail";
	//修改
	protected static final String ORDER_HOTEL_ORDER_UPDATE = "modules/hotel/hotelorder/orderHotelUpdate";
	//产品
	protected static final String ORDER_HOTEL_PRODUCT_LIST = "modules/hotel/hotelorder/orderHotelProductList";
	
	//审核中状态
	private static final Integer REVIEW_UNAUDITED = 1;
	
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupRoomService activityHotelGroupRoomService;
	@Autowired
	private ActivityHotelGroupMealService activityHotelGroupMealService;
	@Autowired
	private ActivityHotelGroupPriceService activityHotelGroupPriceService;
	@Autowired
	private ActivityHotelGroupLowpriceService activityHotelGroupLowpriceService;
	@Autowired
	private ActivityHotelGroupMealRiseService activityHotelGroupMealRiseService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private HotelStarService hotelStarService;
	@Autowired
    private DepartmentService departmentService;
	@Autowired
    private ReviewService reviewService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
    private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private HotelTravelerService hotelTravelerService;
	@Autowired
	private PayHotelOrderService payHotelOrderService;
	@Autowired
	private HotelRoomService hotelRoomService;
	@Autowired
	private HotelMealService hotelMealService;
	@Autowired
	private IslandService islandService;
	@Autowired
	private SysGeographyService sysGeographyService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private HotelOrderPriceService hotelOrderPriceService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private OrderContactsService orderContactsService;
	@Autowired
	private HotelControlService hotelControlService;
	@Autowired
	private HotelTravelervisaService hotelTravelervisaService;
	@Autowired
	private HotelTravelerPapersTypeService hotelTravelerPapersTypeService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private LogOperateService logOperateService;
	@Autowired
	private IslandReviewService islandReviewService;
	@Autowired
    private SupplierContactsService supplierContactsService;
	private HotelOrder dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request) {
		String uuid = request.getParameter("uuid");
		if (StringUtils.isNotBlank(uuid)) {
			dataObj = hotelOrderService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list/{orderStatus}")
	public String list(@PathVariable Integer orderStatus, HotelOrderQuery hotelOrderQuery, HttpServletRequest request, 
			HttpServletResponse response, Model model) {
		
		//设置订单查询类型
		hotelOrderQuery.setOrderStatus(orderStatus);
		
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("order", model);
		
        //根据订单编号查询此订单对应所在的部门
        getDepartmentIdByOrderNum(common, request, model);
        
        //订单或团期查询
        Page<Map<Object, Object>> pageOrder = hotelOrderService.findOrderList(new Page<Map<Object, Object>>(request, response), hotelOrderQuery, common);
        model.addAttribute("page", pageOrder);
        List<Map<Object, Object>> listorder = pageOrder.getList();
//        hotelOrderQuery.setOrderBy(pageOrder.getOrderBy().replace("agp", "pro"));
        pageOrder.setOrderBy(hotelOrderQuery.getOrderBy());
	        
        List<String> groupIdList = Lists.newArrayList();
        List<String> orderUUIDList = Lists.newArrayList();
        for (Map<Object, Object> listin : listorder) {
        	if (!hotelOrderQuery.getIsOrder()) {
        		if (listin.get("groupUuid") != null) {
        			groupIdList.add(listin.get("groupUuid").toString());
        		}
        	} else {
        		if (listin.get("orderUuid") != null) {
        			orderUUIDList.add(listin.get("orderUuid").toString());
        		}
        	}
        	getHotelOtherInfo(listin);
        	//订单：下单人；团期：计调
        	if (listin.get("createBy") != null) {
        		listin.put("carateUserName", UserUtils.getUser(Long.parseLong(listin.get("createBy").toString())).getName());
        	}
            
        	//金额处理
        	handlePrice(listin);
        	
            //转团转款标志位 1-可以转款  0-不可转款
            listin.put("transferMoneyCheck", "0");
            if (hotelOrderQuery.getIsOrder()) {
            	HotelOrder order = hotelOrderService.getById(Integer.parseInt(String.valueOf(listin.get("id"))));
            	if (order != null) {
	            	List<Review> list = reviewService.findReview(order.getOrderStatus(),Context.REVIEW_FLOWTYPE_TRANSFER_GROUP,String.valueOf(listin.get("id")),Context.REVIEW_STATUS_DONE,Context.REVIEW_ACTIVE_EFFECTIVE);
	            	if (list!=null&&list.size()>0){
	            		listin.put("transferMoneyCheck", "1");
	            	}
            	}
            }
           
        }
	        
        List<Map<Object, Object>> orderList = null;
        if (!hotelOrderQuery.getIsOrder() && CollectionUtils.isNotEmpty(groupIdList)) {
        	orderList = hotelOrderService.findByGroupIds(new Page<Map<Object, Object>>(request, response), groupIdList, hotelOrderQuery.getOrderSql()).getList();
        	for (Map<Object, Object> listin : orderList) {
        		//金额处理
        		handlePrice(listin);
        		
        		//转团转款标志位 1-可以转款  0-不可转款
                listin.put("transferMoneyCheck", "0");
                HotelOrder order = hotelOrderService.getById(Integer.parseInt(String.valueOf(listin.get("id"))));
            	if (order != null) {
	            	List<Review> list = reviewService.findReview(order.getOrderStatus(),Context.REVIEW_FLOWTYPE_TRANSFER_GROUP,String.valueOf(listin.get("id")),Context.REVIEW_STATUS_DONE,Context.REVIEW_ACTIVE_EFFECTIVE);
	            	if (list!=null&&list.size()>0){
	            		listin.put("transferMoneyCheck", "1");
	            	}
            	}
            	
            	if (listin.get("orderUuid") != null) {
            		orderUUIDList.add(listin.get("orderUuid").toString());
            	}
                
	        	if (listin.get("createBy") != null) {
	        		listin.put("carateUserName", UserUtils.getUser(Long.parseLong(listin.get("createBy").toString())).getName());
	        	}
	        }
        	listorder = orderList;
        }
        //支付订单查询
        selectPayOrder(orderUUIDList, listorder, model);
        
        String userType = UserUtils.getUser().getUserType();
	        
        model.addAttribute("userType", userType);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type", UserUtils.getUser().getCompany().getId()));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level", UserUtils.getUser().getCompany().getId()));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type", UserUtils.getUser().getCompany().getId()));
        model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
        model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name", UserUtils.getUser().getCompany().getId()));
        model.addAttribute("users", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
        model.addAttribute("countryList", getCountrys());
        model.addAttribute("hotelStarList", getHotelStar());
        model.addAttribute("islandList", islandService.findListByCompanyId(UserUtils.getUser().getCompany().getId().intValue()));
        model.addAttribute("payTypes", DictUtils.getDicMap(Context.PAY_TYPE));
        model.addAttribute("orders",orderList);
        //渠道
        model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
        //部门
        Set<Department> departmentSet = UserUtils.getUserDepartment();
		model.addAttribute("departmentSet", departmentSet);
		//内部销售人员的名单
		model.addAttribute("agentSalers", agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId()));
        model.addAttribute("pageOrder", pageOrder);
        model.addAttribute("hotelOrderQuery", hotelOrderQuery);
        return LIST_PAGE;
	}
	
	/**
	 * 根据订单编号查询此订单对应所在的部门
	 * @param common
	 * @param request
	 * @param model
	 */
	private void getDepartmentIdByOrderNum(DepartmentCommon common, HttpServletRequest request, Model model) {
		
		String orderNumOrGroupCode = request.getParameter("orderNumOrGroupCode");
        //解决下完订单后不能跳到相应区域问题
        if (StringUtils.isNotBlank(orderNumOrGroupCode)) {
        	HotelOrderQuery hotelOrderQuery = new HotelOrderQuery();
        	hotelOrderQuery.setOrderNum(orderNumOrGroupCode);
        	List<HotelOrder> list = hotelOrderService.find(hotelOrderQuery);
        	if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
        		String productUUID = list.get(0).getActivityHotelUuid();
        		Integer createById = activityHotelService.getByUuid(productUUID).getCreateBy();
        		User tempUser = UserUtils.getUser(createById.longValue());
        		if (tempUser != null) {
        			List<Role> roleList = tempUser.getRoleList();
        			if (CollectionUtils.isNotEmpty(roleList)) {
        				for (Role role : roleList) {
        					if (role.getDepartment() != null && (Context.ROLE_TYPE_OP.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES.equals(role.getRoleType()))) {
        						common.setDepartmentId(role.getDepartment().getId().toString());
        						model.addAttribute("departmentId", common.getDepartmentId());
        						break;
        					}
        				}
        			}
        		}
        	}
        }
	}
	
	/**
	 * 获取团期其他信息：房型*晚数；基础餐型；预报名人数
	 * @param listin
	 */
	private void getHotelOtherInfo(Map<Object, Object> listin) {
		String groupUuid = listin.get("groupUuid").toString();
		if (listin.get("activityUuid") != null ) {
			/** 房型*晚数 */
			List<ActivityHotelGroupRoom> roomList = activityHotelGroupRoomService.getRoomListByGroupUuid(groupUuid);
			List<Map<String, String>> roomInfoList = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(roomList)) {
				for (ActivityHotelGroupRoom room : roomList) {
					HotelRoom hotelRoom = hotelRoomService.getByUuid(room.getHotelRoomUuid());
					if (hotelRoom != null) {
						Map<String, String> roomMap = Maps.newHashMap();
						roomMap.put("roomName", hotelRoom.getRoomName());
						roomMap.put("occupancyRate", hotelRoom.getOccupancyRate());
						roomMap.put("roomNights", room.getNights() != null ? room.getNights().toString() : "");
						roomInfoList.add(roomMap);
					}
				}
			}
			
			
			
			//房型
			listin.put("roomInfo", roomInfoList);
			
			/** 基础餐型 */
			for (ActivityHotelGroupRoom room : roomList) {
				List<ActivityHotelGroupMeal> mealList = activityHotelGroupMealService.getMealListByRoomUuid(room.getUuid());
				List<String> meals = Lists.newArrayList();
				if (CollectionUtils.isNotEmpty(mealList)) {
					for (ActivityHotelGroupMeal meal : mealList) {
						HotelMeal hotelMeal = hotelMealService.getByUuid(meal.getHotelMealUuid());
						if (hotelMeal != null) {
							meals.add(hotelMeal.getMealName());
						}
					}
				}
				listin.put("meals", meals);
			}
			
			
			/** 同行价/人 */
			List<ActivityHotelGroupPrice> activityHotelGroupPriceList = activityHotelGroupPriceService.getPriceFilterTravel(groupUuid, listin.get("hotelUuid").toString());
			listin.put("activityHotelGroupPriceList", activityHotelGroupPriceList);
		}
		
		/** 酒店星级 */
		if (listin.get("hotelStar") != null) {
			HotelStar hotelStar = hotelStarService.getByUuid(listin.get("hotelStar").toString());
			if (hotelStar != null) {
				listin.put("hotelLevel", hotelStar.getValue());
			}
		}
		
		//预报名人数
		Integer bookingRoomNum = hotelOrderService.getForecaseReportNum(groupUuid);
		listin.put("bookingRoomNum", bookingRoomNum != null ? bookingRoomNum : 0);
	}
	
	/**
	 * 金额处理：金额千位符与金额多币种id和数值读取
	 * @param listin
	 */
	private void handlePrice(Map<Object, Object> listin) {
		//千位符处理：订单总金额、已付金额、到账金额
		List<String> priceList = Lists.newArrayList();
		priceList.add("costMoney");
		priceList.add("totalMoney");
		priceList.add("payedMoney");
		priceList.add("accountedMoney");
		//获取未收金额
		if (listin.get("totalMoney") != null) {
			String totalMoney = listin.get("totalMoney").toString();
			if (listin.get("payedMoney") != null) {
				String payedMoney = listin.get("payedMoney").toString();
				listin.put("notPayedMoney", hotelMoneyAmountService.addOrSubtract(totalMoney, payedMoney, false));
				priceList.add("notPayedMoney");
			} else {
				listin.put("notPayedMoney", totalMoney);
				priceList.add("notPayedMoney");
			}
		}
		handlePrice(listin, priceList);
	}
	
	/**
	 * 订单金额千位符处理：订单总金额、已付金额、到账金额
	 * @param listin
	 * @param paraList
	 */
	private void handlePrice(Map<Object, Object> listin, List<String> paraList) {
		
		//千位符
		DecimalFormat d = new DecimalFormat(",##0.00");
		if (CollectionUtils.isNotEmpty(paraList)) {
			for (String para : paraList) {
				if (listin.get(para) != null) {
					String moneyStr = listin.get(para).toString();
					String allMoney [] = moneyStr.split("\\+");
					if (allMoney.length > 1) {
						String tempMoneyStr = "";
						for (int i=0;i<allMoney.length;i++) {
							String money [] = allMoney[i].split(" ");
							//币种价格等于0的时候不显示
							if (money.length > 1 && !"0.00".equals(money[1])) {
								tempMoneyStr += money[0] + d.format(new BigDecimal(money[1])) + "+";
							}
						}
						if(StringUtils.isNotBlank(tempMoneyStr)) {
							listin.put(para, tempMoneyStr.substring(0, tempMoneyStr.length()-1));
						}
					} else {
						String money [] = allMoney[0].split(" ");
						if (money.length > 1) {
							String currencyMark = money[0].toString();
							String currencyMoney = money[1].toString();
							if (StringUtils.isNotBlank(currencyMark) && StringUtils.isNotBlank(currencyMoney)) {
								String moneyAmonut = d.format(new BigDecimal(currencyMoney));
								listin.put(para, currencyMark + moneyAmonut);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 查询达帐支付订单与支付订单
	 * @param listProId 订单ids
	 * @param listorder 订单list
	 * @param model
	 */
	private void selectPayOrder(List<String> orderUUIDList, List<Map<Object, Object>> listorder, Model model) {
		
		if (CollectionUtils.isNotEmpty(orderUUIDList)) {
			List<PayHotelOrder> orderPayList = Lists.newArrayList();
			orderPayList = payHotelOrderService.findOrderPayByOrderUuids(orderUUIDList);
            for (Map<Object, Object> map : listorder) {
            	Integer isAsAccount = 0;// 空为未达帐 0为撤销 1为达帐 2为驳回
                List<PayHotelOrder> listTempOrderPay = Lists.newArrayList();
                boolean bCheckFlg = false;
                for (PayHotelOrder orderpay : orderPayList) {
                    //如果orderpay的订单id  等于  pro的订单id
                    if (orderpay.getOrderUuid().equals(map.get("orderUuid").toString())) {
                        listTempOrderPay.add(orderpay);
                        if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
                        	hotelOrderService.clearObject(orderpay);
                        	orderpay.setMoneySerialNum(hotelMoneyAmountService.getMoneyStr(orderpay.getMoneySerialNum(), false));
                        	//判断订单是否有达帐支付记录：用户退款判断
                        	if (isAsAccount != 1 && orderpay.getIsAsAccount() != null && orderpay.getIsAsAccount() == 1) {
                        		isAsAccount = 1;
                        		map.put("isAsAccount", isAsAccount);
                        	}
                        }
                        
                        if (!bCheckFlg) {
							if (orderpay.getPaymentStatus() != null && orderpay.getPaymentStatus() != 0) {
								map.put("paymentStatus", orderpay.getPaymentStatus());
								bCheckFlg = true;
							}
						}
                    }
                }
                
                //支付信息
                map.put("orderPayList", listTempOrderPay);
                //达帐和撤销提示
                boolean isCanceledOrder = false;
                if (map.get("orderStatus") != null) {
                	isCanceledOrder = Context.HOTEL_ORDER_STATUS_YQX.equals(map.get("orderStatus").toString());
                }
                map.put("promptStr", hotelOrderService.getOrderPrompt(map.get("orderUuid").toString(), isCanceledOrder));
            }
        }
	}
	
	/**
	 * 获取国家列表
	 * @return
	 */
	private List<SysGeography> getCountrys() {
		SysGeography sysGeography = new SysGeography();
        List<SysGeography> list = Lists.newArrayList();
		sysGeography.setDelFlag(Context.DEL_FLAG_NORMAL);
		sysGeography.setLevel(1);
		list = sysGeographyService.find(sysGeography);
		return list;
	}
	
	/**
	 * 获取酒店星级
	 * @return
	 */
	private List<HotelStar> getHotelStar() {
		HotelStar hotelStar = new HotelStar();
		hotelStar.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
		List<HotelStar> hotelStarList = hotelStarService.find(hotelStar);
		return hotelStarList;
	}
	
	/**
	 * 订单锁定
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "lockOrder")
    @ResponseBody
    public Object lockOrder(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String uuid = request.getParameter("uuid");
        if (StringUtils.isNotBlank(uuid)) {
        	 this.hotelOrderService.lockOrder(uuid);
             map.put("success", "success");
        } else {
        	map.put("error", "订单UUID为空");
        }
        return map;
    }
	
	/**
	 * 订单解锁
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "unLockOrder")
    @ResponseBody
    public Object unLockOrder(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
        String uuid = request.getParameter("uuid");
        if (StringUtils.isNotBlank(uuid)) {
        	 this.hotelOrderService.unLockOrder(uuid);
             map.put("success", "success");
        } else {
        	map.put("error", "订单UUID为空");
        }
        return map;
    }
	
	/**
	 * 订单关联文件（文件上传）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "setOrderFiles")
    @ResponseBody
    public Object setOrderFiles(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
        String orderUuid = request.getParameter("orderUuid");
        String fileIds = request.getParameter("fileIds");
        if (StringUtils.isNotBlank(orderUuid) && StringUtils.isNotBlank(fileIds)) {
        	 this.hotelOrderService.setOrderFiles(orderUuid, fileIds);
             map.put("success", "success");
        } else {
        	map.put("error", "订单UUID为空或文件为空");
        }
        return map;
    }
	
	/**
	 * 获取关联订单文件信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getFilesInfo")
	@ResponseBody
	public Object getFilesInfo(Model model, HttpServletRequest request) {
		String orderUuid = request.getParameter("orderUuid");
		List<DocInfo> docInfoList = Lists.newArrayList();
		if (StringUtils.isNotBlank(orderUuid)) {
			docInfoList = this.hotelOrderService.getFilesInfo(orderUuid);
		}
		return docInfoList;
	}
	
	/**
	 * 下载团期资料
	 * @param model
	 * @param request
	 * @return string
	 */
	@ResponseBody
	@RequestMapping(value ="downloadGroupFiles")
	public String downloadGroupFiles(Model model, HttpServletRequest request) {
		String docIds = "";
		String orderUuids = request.getParameter("orderUuids");
		List<String> orderUuidList = Lists.newArrayList();
		if (StringUtils.isNotBlank(orderUuids)) {
			String uuids [] = orderUuids.split(",");
			for (String uuid : uuids) {
				if (StringUtils.isNotBlank(uuid)) {
					orderUuidList.add(uuid);
				}
			}
		}
	    if (CollectionUtils.isNotEmpty(orderUuidList)) {
	    	List<HotelOrder> hotelOrderList = hotelOrderService.getByUuids(orderUuidList);
	    	if (CollectionUtils.isNotEmpty(hotelOrderList)) {
	    		for (HotelOrder hotelOrder : hotelOrderList) {
					if (StringUtils.isNotBlank(hotelOrder.getFileIds())) {
						docIds += hotelOrder.getFileIds();
					}
				}
    		}	    	
	    }
	    return docIds;
	}
	
	
	/**
	 * 激活订单
	 * @param model
	 * @param request
	 * @return string
	 */
	@ResponseBody
	@RequestMapping(value ="invokeOrder")
	public String invokeOrder(Model model, HttpServletRequest request) 
			throws NumberFormatException, OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		String flag = "fail";
		String orderUuid = request.getParameter("orderUuid");
	    if (StringUtils.isNotBlank(orderUuid)) {
	    	HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
	    	if (hotelOrder != null) {
    			Integer orderStatus = hotelOrder.getOrderStatus();
    			if (Context.HOTEL_ORDER_STATUS_YQX == orderStatus) {
    				flag = hotelOrderService.invokeOrder(orderUuid, request);
    			} else {
    				return "此订单已激活";
    			}
    		}	    	
	    }
	    if (StringUtils.isBlank(flag)){
	    	flag = "fail";
	    }
	    return flag;
	}
	
	/**
	 * 取消订单
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="cancelOrder")
	public String cancelOrder(Model model, HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		String orderUuid = request.getParameter("orderUuid");
	    String description = request.getParameter("description");
	    if(StringUtils.isNotBlank(orderUuid)){
        	HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
    		if (hotelOrder != null) {
    			Integer orderStatus = hotelOrder.getOrderStatus();
    			//只有待确认的订单可以取消，已确认订单不能取消
    			if (Context.HOTEL_ORDER_STATUS_DQR == orderStatus) {
    				hotelOrderService.cancelOrder(hotelOrder, description, request);
    			} else {
    				if (Context.HOTEL_ORDER_STATUS_YQR == orderStatus) {
    					return "订单已确认，不能取消";
    				} else if (Context.HOTEL_ORDER_STATUS_YQX == orderStatus) {
    					return "订单已取消，不能再次取消";
    				}
    			}
    		}
	    }
	    return "ok";
	}
	
	/**
	 * 判断订单是否可以支付 
	 * @param orderId
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value="whetherCanPay",method=RequestMethod.POST)
	public Object whetherCanPay(@RequestParam(value="orderUuid") String orderUuid) throws JSONException {
		
		net.sf.json.JSONArray results = new net.sf.json.JSONArray();
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
		
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		
		//如果订单为空则返回错误
		if (hotelOrder == null) {
			resobj.put("flag", "false");
			resobj.put("warning", "查询不到此订单");
			results.add(resobj);
			return results;
		}
		
		List<String> totalCurreney = Lists.newArrayList();
		BigDecimal payedMoney = new BigDecimal(0);
		BigDecimal totalMoney = new BigDecimal(0);
			
		//应收
		if (StringUtils.isNotBlank(hotelOrder.getTotalMoney())) {
			List<Object[]> list = hotelMoneyAmountService.getMoneyAmonut(hotelOrder.getTotalMoney());
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i=0;i<list.size();i++) {
					totalCurreney.add(list.get(i)[0].toString() + " " + list.get(i)[3]);
					if (list.get(i)[3] != null && list.get(i)[4] != null) {
						//转换成人民币
						totalMoney = totalMoney.add(new BigDecimal(list.get(i)[3].toString()).multiply(new BigDecimal(list.get(i)[4].toString())));
					}
    			}
			}
		}
			
		//已收
		List<String> payedCurreney = Lists.newArrayList();
		if (StringUtils.isNotBlank(hotelOrder.getPayedMoney())) {
			List<Object[]> list = hotelMoneyAmountService.getMoneyAmonut(hotelOrder.getPayedMoney());
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i=0;i<list.size();i++) {
					payedCurreney.add(list.get(i)[0].toString() + " " + list.get(i)[3]);
					if (list.get(i)[3] != null && list.get(i)[4] != null) {
						//转换成人民币
						payedMoney = payedMoney.add(new BigDecimal(list.get(i)[3].toString()).multiply(new BigDecimal(list.get(i)[4].toString())));
					}
    			}
			}
		}
		List<String> result = hotelMoneyAmountService.subtract(totalCurreney, payedCurreney);
			
		//如果有负值，则把尾款转换成人民币，如果为正则可支付，如果为负则不允许支付
		if (CollectionUtils.isNotEmpty(result)) {
			resobj.put("flag", "true");
			resobj.put("moneyCurrencyId", result.get(0));
			resobj.put("moneyCurrencyPrice", result.get(1));
		} else {
			if (CollectionUtils.isNotEmpty(totalCurreney)) {
				resobj.put("flag", "true");
				resobj.put("moneyCurrencyId", totalCurreney.get(0).split(" ")[0]);
				resobj.put("moneyCurrencyPrice", "0.00");
			} else {
				resobj.put("flag", "false");
				resobj.put("warning", "订单没有支付金额");
				results.add(resobj);
				return results;
			}
		}
		
		Map<String, String> totalMoneyMap = moneyContactVal(hotelMoneyAmountService.getMoneyAmonut(hotelOrder.getTotalMoney()));
		resobj.put("totalMoneyCurrencyId", totalMoneyMap.get("currencyId"));
		resobj.put("totalMoneyCurrencyPrice",totalMoneyMap.get("currencyPrice"));
		
		results.add(resobj);
		return results;
	}
	
	/**
	 * 取得金额连接的字符串
	 * @param list
	 * @return
	 */
	private Map<String, String> moneyContactVal(List<Object[]> list) {
		Map<String, String> map = new HashMap<String, String>();

		if (CollectionUtils.isNotEmpty(list)) {
			String currencyId = "";
			String currencyPrice = "";
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					currencyId += list.get(i)[0];
					currencyPrice += list.get(i)[3];
				} else {
					currencyId += list.get(i)[0] + ",";
					currencyPrice += list.get(i)[3] + ",";
				}
			}
			map.put("currencyId", currencyId);
			map.put("currencyPrice", currencyPrice);
		}

		return map;
	}
	
	
	/**
	 * 根据订单id和订单支付id查询支付凭证
	 * @param payUuid 支付订单UUID
	 * @param orderUuid 订单UUID
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="modifypayVoucher/{payUuid}/{orderUuid}")
	public String modifypayVoucher(@PathVariable String payUuid,@PathVariable String orderUuid, Model model,HttpServletRequest request) {
		
		PayHotelOrder payHotelOrder = payHotelOrderService.getByUuid(payUuid);
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
	    
	    //支付订单金额千位符处理
	    if (StringUtils.isNotBlank(payHotelOrder.getMoneySerialNum())) {
	    	hotelOrderService.clearObject(payHotelOrder);
	    	payHotelOrder.setMoneySerialNum(hotelMoneyAmountService.getMoneyStr(payHotelOrder.getMoneySerialNum(), false));
	    }
	    
	    //订单金额千位符处理
	    if (StringUtils.isNotBlank(hotelOrder.getTotalMoney())) {
	    	hotelOrderService.clearObject(hotelOrder);
	    	hotelOrder.setTotalMoney(hotelMoneyAmountService.getMoneyStr(hotelOrder.getTotalMoney(), false));
	    }
	  
		model.addAttribute("orderpay",payHotelOrder);
		model.addAttribute("hotelOrder",hotelOrder);
	    return MODIFY_PAY_VOUCHER;
	}
	
	/**
	 * 上传支付凭证
	 * @param files
	 * @param request
	 * @param model
	 * @return
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException
	 * @throws Exception
	 */
	@RequestMapping(value ="modifypayVoucherFile")
	public String modifypayVoucherFile(@RequestParam(value = "payVoucher", required = false) MultipartFile[] files,
			HttpServletRequest request, ModelMap model) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
	    ArrayList<DocInfo> infoList = new ArrayList<DocInfo>();
		if (files != null && files.length > 0) {
	    	for (int i = 0; i < files.length; i++) {
	    		MultipartFile file = files[i];
	    		if (file != null) {
	    			DocInfo docInfo = null;
	    	        String fileName = file.getOriginalFilename();
	    	        docInfo = new DocInfo();
	    	        try {
	    	            String path = FileUtils.uploadFile(file.getInputStream(),fileName);
	    	            docInfo.setDocName(fileName);
	    	            docInfo.setDocPath(path);
	    	            infoList.add(docInfo);
	    	        } catch (Exception e) {  
	    	            e.printStackTrace();  
	    	        }
	    		}
	    	}
	    }

		 String remarks = request.getParameter("remarks");
		 String payUuid = request.getParameter("payUuid");
		 String orderUuid = request.getParameter("orderUuid");
		 PayHotelOrder payHotelOrder = payHotelOrderService.getByUuid(payUuid);
		 if (StringUtils.isNotBlank(remarks)) {
			 payHotelOrder.setRemarks(remarks);
		 }
		 hotelOrderService.updatepayVoucherFile(infoList, payHotelOrder, orderUuid, model, request);
		
	    return UPLOAD_VO_SUCCESS;
	   
	}
	
	
	/**
	 * 查看退团列表
	 * @author  chenry
	 */
	@RequestMapping(value = "viewExitGroup")
	public String viewExitGroup(Model model,HttpServletRequest request,HttpServletResponse response, String orderUuid) {
		
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		//update by WangXK 20151020 添加空指针的判断
		String id = "";
		if(hotelOrder!=null){
			id = hotelOrder.getId().toString();
		}
		
		List<Map<String, String>> pageOrder = hotelOrderService.getExitGroupReviewInfo(
				Context.REVIEW_FLOWTYPE_EXIT_GROUP, id);
		
		if (null != hotelOrder && hotelOrder.getId() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			model.addAttribute("orderTime", sdf.format(hotelOrder.getOrderTime()));
		}
		model.addAttribute("page", pageOrder);
		model.addAttribute("orderUuid", orderUuid);
		if(hotelOrder!=null){
			model.addAttribute("orderId", hotelOrder.getId());
		}
		model.addAttribute("productType", Context.ProductType.PRODUCT_HOTEL);
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		return VIEW_EXIT_GROUP;
	}
	
	/**
	 * 取消审核
	 * @param model
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="cancleAudit")
	public int cancleAudit(Model model,HttpServletRequest request, HttpServletResponse response, Long id) {
		Review r = reviewService.findReviewInfo(id);
		int sign = 0;
		if (null != r && REVIEW_UNAUDITED.intValue() == r.getStatus().intValue()) {
			sign = reviewService.removeReview(id);
		}
		//游客状态改为正常
		//update by WangXK 20151020 添加空指针的判断
		if(null != r && r.getFlowType()!=null && r.getFlowType()!=null && r.getTravelerId()!=null){
			if (r.getFlowType().intValue() == Context.REVIEW_FLOWTYPE_EXIT_GROUP.intValue()  || r.getFlowType().intValue() == Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.intValue()) {
				HotelTraveler hotelTraveler = hotelTravelerService.getById(r.getTravelerId().intValue());
				hotelTraveler.setDelFlag(Context.TRAVELER_DELFLAG_NORMAL.toString());
				hotelTravelerService.update(hotelTraveler);
			} 		
		}
		
		return sign;
	}
	
	/**
	 * 查看退团审核详情
	 * @param model
	 * @param request
	 * @param response
	 * @param productType
	 * @param rid
	 * @param orderUuid
	 * @return
	 */
	@RequestMapping(value = "viewApplyExitGroupInfo")
	public String viewApplyExitGroupInfo(Model model, HttpServletRequest request, HttpServletResponse response, 
			Integer productType, Long rid, String orderUuid) {
		Map<String, Object> travelerList = hotelOrderService.getExitGroupReviewInfoById(rid);
		model.addAttribute("hashMap",travelerList);
		model.addAttribute("productType",productType);
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		model.addAttribute("hotelOrder", hotelOrder);
		return VIEW_EXIT_GROUP_INFO;
	}
	
	/**
	 * 申请退团
	 * @param model
	 * @param request
	 * @param response
	 * @param orderUuid
	 * @return
	 */
	@RequestMapping(value = "applyExitGroup")
	public String applyExitGroup(Model model,HttpServletRequest request,HttpServletResponse response, String orderUuid) {
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		List<Map<Object, Object>> travelerList = hotelOrderService.getTravelerByOrderId(orderUuid);
		model.addAttribute("orderId", hotelOrder.getId());
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		model.addAttribute("productType", Context.ProductType.PRODUCT_HOTEL);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("hotelOrder", hotelOrder);
		return APPLY_EXIT_GROUP;
	}
	
	/**
	 * 提交保存退团申请
	 * @param travelerName
	 * @param travelerId
	 * @param exitReason
	 * @param flowType
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveExitGroupInfo")
	public Map<String,Object> saveExitGroupInfo(Model model, HttpServletRequest request, HttpServletResponse response, 
			String[] travelerName, Long[] travelerId, String[] exitReason, Integer flowType, String orderUuid) {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = hotelOrderService.saveExitGroupReviewInfo(
					Context.ProductType.PRODUCT_HOTEL, flowType, travelerId, travelerName, exitReason, orderUuid);
			map.put(Context.RESULT, 2);
		} catch (Exception e) {
			map.put(Context.RESULT, 0);
			map.put(Context.MESSAGE, e.getMessage());
			return map;
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value = "loadAsynchronousData")
	public Object loadAsynchronousData(String hotelOrderId) {
		Map<String, List<?>> datas = Maps.newHashMap();
		//修改数据
		List<Object[]> modifyData = Lists.newArrayList();
		modifyData = logOperateService.queryByParas(Context.ProductType.PRODUCT_HOTEL, Long.parseLong(hotelOrderId), Context.log_state_up);
		datas.put("modifyData", modifyData);
		
		//转团记录
		if (StringUtils.isNotBlank(hotelOrderId)) {
			// 海岛游转团申请记录
			HotelOrder order = hotelOrderService.getById(Integer.parseInt(hotelOrderId));
			ActivityHotel activityHotel = activityHotelService.getByUuid(order.getActivityHotelUuid());
			List<Map<String, String>> reviewList= new ArrayList<Map<String, String>>();
			reviewList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_HOTEL, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, order.getId().toString(), true,Long.valueOf(activityHotel.getDeptId()));
		
			datas.put("changeGroupData", reviewList);
		}
		
		//退团记录
		HotelOrder hotelOrder = hotelOrderService.getById(Integer.parseInt(hotelOrderId));
		List<Map<String, String>> exitGroupData = hotelOrderService.getExitGroupReviewInfo(
				Context.REVIEW_FLOWTYPE_EXIT_GROUP, hotelOrder.getId().toString());
		datas.put("exitGroupData", exitGroupData);
		
		//改价记录
		List<Map<String, Object>> changePriceData = Lists.newArrayList();
		changePriceData = reviewService.findReviewListMap(Context.ProductType.PRODUCT_HOTEL, 10, hotelOrderId, false, "");
		datas.put("changePriceData", changePriceData);
		
		//返佣记录
		List<HotelRebates> rebatesData = islandReviewService.findHotelRebatesList(Long.parseLong(hotelOrderId), Context.ORDER_TYPE_HOTEL);
		List<HotelRebates> tempData = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(rebatesData)) {
			Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
			String totalMoney = "";//累计返佣金额
			for (HotelRebates hotelRebates : rebatesData) {
				HotelRebates temp = new HotelRebates();
				if (hotelRebates.getTraveler() != null && StringUtils.isNotBlank(hotelRebates.getTraveler().getUuid())) {
					String currencyMark = currencyService.findCurrency(hotelRebates.getCurrencyId()).getCurrencyMark();
					String currencyMoney = hotelRebates.getRebatesDiff().toString();
					//获取累计退款金额
					String travelerId = hotelRebates.getTravelerId().toString();
					if (t4priceMap.containsKey(travelerId)) {
						totalMoney = t4priceMap.get(travelerId);
					} else {
						totalMoney = "";
					}
					totalMoney = hotelMoneyAmountService.addOrSubtract(currencyMark + " " + currencyMoney, totalMoney, true);
					t4priceMap.put(travelerId, totalMoney);
					temp.setRebatesdiffString1(totalMoney);
				}
				temp.setCreateDate(hotelRebates.getCreateDate());
				temp.setCostname(hotelRebates.getCostname());
				temp.setTotalMoney(hotelRebates.getTotalMoney());
				temp.setRemark(hotelRebates.getRemark());
				temp.setRebatesDiff(hotelRebates.getRebatesDiff());
				temp.setTraveler(hotelRebates.getTraveler());
				temp.setCurrency(hotelRebates.getCurrency());
				tempData.add(temp);
			}
		}
		
		//对结果集进行翻转（前台按创建时间倒叙排列）
		List<Map <String,Object>> refundData = getRefundList(hotelOrderId);
		List<Map <String,Object>> borrowingData = getBorrowingList(hotelOrderId);
		Collections.reverse(tempData);
		Collections.reverse(refundData);
		Collections.reverse(borrowingData);
		
		//返佣记录
		datas.put("rebatesData", tempData);
		//退款记录
		datas.put("refundData", refundData);
		//借款记录
		datas.put("borrowingData", borrowingData);
		
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "getData")
	public String getData(String tableName, String queryName, String queryValue, String getName) {
		if (StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(queryName) 
				&& StringUtils.isNotBlank(queryValue) && StringUtils.isNotBlank(getName)) {
			if (tableName.contains("money_amount")) {
				return hotelMoneyAmountService.getMoneyStr(queryValue, true);
			}
			return hotelOrderService.getData(tableName, queryName, queryValue, getName);
		}
		return "";
	}
	
	/**
	 * 获取借款信息
	 * @param hotelOrderId
	 * @return
	 */
	private List<Map <String,Object>> getBorrowingList(String hotelOrderId) {
		HotelOrder hotelOrder = hotelOrderService.getById(Integer.parseInt(hotelOrderId));
		ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(
				Context.ProductType.PRODUCT_HOTEL, 19, hotelOrder.getId() + "", false, activityHotel.getDeptId().longValue());
		
		List<BorrowingBean> reviewList = getBorrowingBeanList(reviewMapList);
		for (BorrowingBean borr :reviewList) {
			if (borr.getTravelerId().contains(BorrowingBean.REGEX)|| "0".equals(borr.getTravelerId())) {
				borr.setTravelerName("团队");
			}
			if (StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(BorrowingBean.REGEX)) {
				String compPrice = "";
				if (StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())) {
					String[] cMarks = borr.getCurrencyMarks().split(BorrowingBean.REGEX);	
					String[] cPrices = borr.getBorrowPrices().split(BorrowingBean.REGEX);
					for (int i=0;i<cMarks.length;i++) {
						compPrice+=cMarks[i]+cPrices[i]+"+";
					}
					borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
				}
			} else {
			    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
			}
		}
		List<Map <String,Object>> rMap = Lists.newArrayList();
		List<HotelTraveler> travelerList = hotelTravelerService.findTravelerByOrderUuid(hotelOrder.getUuid(), false);
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumLendPrice = ""; //累计借款金额
		for (BorrowingBean borr : reviewList) {
			Map <String,Object> map = Maps.newHashMap();
			map.put("currencyName", borr.getCurrencyName());
			map.put("currencyMark", borr.getCurrencyMark());
			map.put("lendPrice", borr.getLendPrice());
			map.put("lendName", borr.getLendName());
			map.put("reviewId", borr.getReviewId());
			map.put("createBy", borr.getCreateBy());

			//获取累计借款金额
			String travelerId = borr.getTravelerId();
			if (t4priceMap.containsKey(travelerId)) {
				sumLendPrice = t4priceMap.get(travelerId);
			} else {
				sumLendPrice = "";
			}
			sumLendPrice = hotelMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getLendPrice(), sumLendPrice, true);
			t4priceMap.put(travelerId, sumLendPrice);
			map.put("borrowingTotal", sumLendPrice);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (borr.getApplyDate() != null) {
				String datestr = sdf.format(borr.getApplyDate());
				map.put("applyDate", datestr);
			}
			map.put("remark", borr.getRemark());
			for (HotelTraveler t : travelerList) {
				if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("travelerUuid", t.getUuid());
				}
			}
			rMap.add(map);
		}
		return rMap;
	}
	
	/**
	 * 组装BorrowingBean对象
	 */
	private List<BorrowingBean> getBorrowingBeanList(List<Map<String, String>> reviewMapList) {
		List<BorrowingBean> aList = new ArrayList<BorrowingBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, String> map : reviewMapList) {
			aList.add(new BorrowingBean(map));
		}
		return aList;
	}
	
	/**
	 * 获取退款记录
	 * @param hotelOrderId
	 * @return
	 */
	private List<Map <String,Object>> getRefundList(String hotelOrderId) {

		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_HOTEL, 1, hotelOrderId, false);
		Collections.reverse(reviewMapList);
		List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
		// 定义订单应收币种集合  取订单达帐金额所有的币种
		HotelOrder hotelOrder = hotelOrderService.getById(Integer.parseInt(hotelOrderId));
		List<HotelTraveler> travelerList = hotelTravelerService.findTravelerByOrderUuid(hotelOrder.getUuid(), false);
		List<Map<String,Object>> rMap = Lists.newArrayList();
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumRefundPrice = "";//累计退款金额
		for (RefundBean borr : reviewList) {
			Map <String,Object> map = Maps.newHashMap();
			map.put("currencyName", borr.getCurrencyName());
			map.put("currencyMark", borr.getCurrencyMark());
			map.put("refundPrice", borr.getRefundPrice());
			map.put("refundName", borr.getRefundName());
			map.put("reviewId", borr.getReviewId());

			//获取累计退款金额
			String travelerId = borr.getTravelerId();
			if (t4priceMap.containsKey(travelerId)) {
				sumRefundPrice = t4priceMap.get(travelerId);
			} else {
				sumRefundPrice = "";
			}
			sumRefundPrice = hotelMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getRefundPrice(), sumRefundPrice, true);
			t4priceMap.put(travelerId, sumRefundPrice);
			map.put("refundTotal", sumRefundPrice);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (borr.getApplyDate() != null) {
				String datestr = sdf.format(borr.getApplyDate());
				map.put("applyDate", datestr);
			}
			map.put("remark", borr.getRemark());
			map.put("createBy", borr.getCreateBy());
			for (HotelTraveler t : travelerList) {
				if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
				}
				
			}
			rMap.add(map);
		}
		return rMap;
	}
	
	/**
	 * 获取退款bean列表
	 * 
	 * @param reviewMapList
	 * @return
	 */
	private List<RefundBean> getRefundBeanList(List<Map<String, String>> reviewMapList) {
		List<RefundBean> aList = new ArrayList<RefundBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, String> map : reviewMapList) {
			aList.add(new RefundBean(map));
		}
		return aList;
	}
	
	

	@RequestMapping(value = "form")
	public String form(HotelOrderInput hotelOrderInput, Model model) {
		model.addAttribute("hotelOrderInput", hotelOrderInput);
		return FORM_PAGE;
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("hotelOrder", hotelOrderService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		HotelOrder hotelOrder = hotelOrderService.getByUuid(uuid);
		HotelOrderInput hotelOrderInput = new HotelOrderInput(hotelOrder);
		model.addAttribute("hotelOrderInput", hotelOrderInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(HotelOrderInput hotelOrderInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, hotelOrderInput,true);
			hotelOrderService.update(dataObj);
		} catch (Exception e) {
			result="0";
		}
		return result;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				for(String uuid:uuidArray){
					hotelOrderService.removeByUuid(uuid);
				}
				
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "系统发生异常，请重新操作!");
		}
		if(b){
			datas.put("result", "1");
			datas.put("message", "success");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	/**
	 * 酒店产品预报名
	*<p>Title: preReportHotelProduct</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-17 下午10:33:11
	* @throws
	 */
	@RequestMapping(value = "preReportHotelProduct/{activityHotelGroupUuid}")
	public String preReportHotelProduct(@PathVariable String activityHotelGroupUuid, Model model) {
		//初始化预报名页面数据
		hotelOrderService.initHotelOrderPageData(activityHotelGroupUuid,model);
		
		return PRE_REPORT_HOTEL_PRODUCT_PAGE;
	}
	
	/**
	 * 酒店产品预报名
	*<p>Title: saveHotelOrder</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-14 下午6:37:07
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "saveHotelOrder")
	public Object saveHotelOrder(HotelOrderInput hotelOrderInput, Model model, HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		try{
			result = hotelOrderService.saveHotelOrder(hotelOrderInput);
			
			result.put("message", "1");
		} catch (Exception e) {
			e.printStackTrace();
			if(!"3".equals(result.get("message"))) {
				result.put("message", "3");
				result.put("error", "系统异常，请重新操作!");
			}
			return result;
		}
		return result;
	}
	
	/**
	 * @throws Exception 
	 * 酒店订单支付
	*<p>Title: payHotelOrder</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-18 下午6:38:08
	* @throws
	 */
	@RequestMapping(value = "payHotelOrder/{orderUuid}")
	public String payHotelOrder(@PathVariable(value="orderUuid") String orderUuid,HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		String resultCurrency = request.getParameter("resultCurrency");
		String resultAmount = request.getParameter("resultAmount");
		String cancelPayUrl = request.getParameter("cancelPayUrl");
		List<String> orderUuids = new ArrayList<String>();
		orderUuids.add(orderUuid);
		
		//兼容预定和收款
		if (StringUtils.isNotBlank(resultCurrency) && StringUtils.isNotBlank(resultAmount)) {
			resultCurrency = resultCurrency.replace(",", ";");
			resultAmount = resultAmount.replace(",", ";");
		} else {
			throw new Exception("参数错误");
		}

		OrderPayInput orderPayInput = hotelOrderService.buildOrderPayData(orderUuids, resultCurrency.split(";"), resultAmount.split(";"), cancelPayUrl);
		request.setAttribute("pay", orderPayInput);
		return "forward:"+Global.getAdminPath()+"/orderPayMore/pay";
	}
	
	/**
	 * 构造酒店产品报名列表add by hhx
	 * 
	 * @param query
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "hotelOrderList")
	public ModelAndView getActivityHotelList(ActivityHotelQuery query,
			HttpServletRequest request, HttpServletResponse response) {
		String orderBy = request.getParameter("orderBy");
		String status = "1";
		query.setStatus(status);
		String showType = request.getParameter("showType");
		Long companyId = UserUtils.getUser().getCompany().getId();
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,
				response);
		ModelAndView mav = new ModelAndView();
		Set<Department> deptSet = UserUtils.getDepartmentByJob();
		mav.addObject("canPublish", deptSet.size()>0?true:false);
		List<ActivityHotelGroupLowprice> lowPriceList = new ArrayList<ActivityHotelGroupLowprice>();
		int baseMealNum = 0;
		if ("2".equals(showType)) {
			page = activityHotelService.getActivityHotelList(query, request,
					response);
			// 获取单个产品下的团期列表
			List<List<Map<String, Object>>> groupList = new ArrayList<List<Map<String, Object>>>();
			for (Map<String, Object> map : page.getList()) {
				String uuid = (String) map.get("uuid");
				String hotelUuid = (String) map.get("hotel_uuid");
				//activityHotelGroupLowpriceService.getPriceList(uuid);
				lowPriceList = activityHotelGroupLowpriceService.getLowprice(uuid);
				map.put("lowPriceList", lowPriceList);
				// 获取单个产品下的团期列表
				List<Map<String, Object>> grouplist = activityHotelService
						.getActivityHotelGroupList(uuid,query);
				groupList.add(grouplist);
				
				
				for (Map<String, Object> submap : grouplist) {
					String subGroupUuid = (String) submap.get("uuid");
					baseMealNum = 0;
					//房型
					List<ActivityHotelGroupRoom> groupRoomlist = activityHotelGroupRoomService.getRoomListByGroupUuid(subGroupUuid);
					//餐型    升级餐型
					if(CollectionUtils.isNotEmpty(groupRoomlist)){
						for(ActivityHotelGroupRoom room : groupRoomlist){
				      	    List<ActivityHotelGroupMeal> groupMeallist = activityHotelGroupMealService.getMealListByRoomUuid(room.getUuid());
				      	    if (CollectionUtils.isNotEmpty(groupMeallist)) {
								for (ActivityHotelGroupMeal meal : groupMeallist) {
									List<ActivityHotelGroupMealRise> activitygroupMealRise = activityHotelGroupMealRiseService.getMealRiseByMealUuid(meal.getUuid());
									meal.setActivityHotelGroupMealsRiseList(activitygroupMealRise);
								}
							}
				      	    baseMealNum = baseMealNum + groupMeallist.size();
				      	    room.setActivityHotelGroupMealList(groupMeallist);
						}
					}
					if(baseMealNum==0){
		      	    	baseMealNum = 1;
		      	    }
					//同行价
					List<ActivityHotelGroupPrice> prices=activityHotelGroupPriceService.getPriceFilterTravel(subGroupUuid,hotelUuid);
					submap.put("groupRoomList", groupRoomlist);
					submap.put("prices",prices);
					submap.put("baseMealNum", baseMealNum);
					Integer orderNum=hotelOrderService.getForecaseReportNum(subGroupUuid);
					submap.put("orderNum", orderNum);
				}
			}
			if (CollectionUtils.isNotEmpty(groupList)) {
				mav.addObject("groupList", groupList);
			}
		} else {
			// 根据showType返回列表
			page = activityHotelService.getActivityHotelList(query, request,
					response);
			for (Map<String, Object> map : page.getList()) {
				String groupUuid = (String) map.get("uuid");
				String hotelUuid = (String) map.get("hotel_uuid");
				baseMealNum = 0;
				//房型
				List<ActivityHotelGroupRoom> groupRoomList = activityHotelGroupRoomService.getRoomListByGroupUuid(groupUuid);
				//餐型    升级餐型
				if(CollectionUtils.isNotEmpty(groupRoomList)){
					for(ActivityHotelGroupRoom room : groupRoomList){
			      	    List<ActivityHotelGroupMeal> groupMeallist = activityHotelGroupMealService.getMealListByRoomUuid(room.getUuid());
			      	    if (CollectionUtils.isNotEmpty(groupMeallist)) {
							for (ActivityHotelGroupMeal meal : groupMeallist) {
								List<ActivityHotelGroupMealRise> activitygroupMealRise = activityHotelGroupMealRiseService.getMealRiseByMealUuid(meal.getUuid());
								meal.setActivityHotelGroupMealsRiseList(activitygroupMealRise);
							}
						}
			      	    baseMealNum = baseMealNum + groupMeallist.size();
			      	    room.setActivityHotelGroupMealList(groupMeallist);
					}
				}
				if(baseMealNum==0){
	      	    	baseMealNum = 1;
	      	    }
				//同行价
				List<ActivityHotelGroupPrice> prices=activityHotelGroupPriceService.getPriceFilterTravel(groupUuid,hotelUuid);
				map.put("groupRoomList", groupRoomList);
				map.put("baseMealNum", baseMealNum);
				map.put("prices",prices);
				Integer orderNum=hotelOrderService.getForecaseReportNum(groupUuid);
				map.put("orderNum", orderNum);
			}
		}
		String pageStr = page.toString();
		pageStr = pageStr.replace("<div style=\"clear:both;\"></div>", "");
		mav.addObject("pageStr", pageStr);
		mav.addObject("page", page);
		mav.addObject("count", page.getCount());
		mav.addObject("activityHotelQuery", query);
		mav.addObject("status", status);
		mav.addObject("showType", showType);
		mav.addObject("orderBy", orderBy);
		// 添加币种列表
		List<Currency> curencyList = currencyService
				.findCurrencyList(companyId);
		mav.addObject("currencyList", curencyList);
		// 添加酒店星级列表
		HotelStar hotelStar = new HotelStar();
		hotelStar.setWholesalerId(companyId.intValue());
		List<HotelStar> hotelStarList = hotelStarService.find(hotelStar);
		mav.addObject("hotelStarList", hotelStarList);
		mav.setViewName("2".equals(showType) ? HOTEL_ORDER_PRODUCT_LIST
				: HOTEL_ORDER_GROUP_LIST);
		return mav;
	}
	
	/**
	 * 跳转到酒店订单详情页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="hotelOrderDetail/{orderUuid}")
	public String toHotelOrderDetailPage(@PathVariable(value="orderUuid") String orderUuid, 
			HttpServletRequest request, HttpServletResponse response, Model model){
		
		setHotelOrderDetail(orderUuid,model);

		return  ORDER_HOTEL_ORDER_DETAIL;
	}
	
	/**
	 * 跳转到酒店订单修改页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value="toHotelOrderUpdatePage")
	public String toHotelOrderUpdatePage(String orderUuid, boolean isTransfer, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取订单信息
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		
//		//获取订单总额(成本价)
//		String costMoneyStr =  hotelMoneyAmountService.getMoneyStr(hotelOrder.getCostMoney(), true);
//		//获取订单结算总额(结算价,应收金额)
//		String totalMoneyStr = hotelMoneyAmountService.getMoneyStr(hotelOrder.getTotalMoney(), true);
//		//获取订单已付金额(已付金额)
//		String payedMoneyStr = hotelMoneyAmountService.getMoneyStr(hotelOrder.getPayedMoney(), true);
//		//订单未收金额( totalMoneyStr - payedMoneyStr  )
//		String noPayMoneyStr = "";
//		if(!StringUtils.isEmpty(totalMoneyStr) && !StringUtils.isEmpty(payedMoneyStr) ){
//			 noPayMoneyStr =  hotelMoneyAmountService.addOrSubtract(totalMoneyStr, payedMoneyStr, false);
//		}
		
//		//获取订单结算总额(结算价,应收金额)
//		List<HotelMoneyAmount> totalMoneyList = hotelMoneyAmountService.getMoneyAmonutBySerialNum(hotelOrder.getTotalMoney());
//		model.addAttribute("totalMoneyList", totalMoneyList);	//订单结算总额
		
		//获取订单已付金额(已付金额)
		if (StringUtils.isNotBlank(hotelOrder.getPayedMoney())) {
			List<HotelMoneyAmount> payedMoneyList = hotelMoneyAmountService.getMoneyAmonutBySerialNum(hotelOrder.getPayedMoney());
			model.addAttribute("payedMoneyList", payedMoneyList);	//订单已付金额
		}
		
		String activityHotelGroupUuid = hotelOrder.getActivityHotelGroupUuid();
		//加载酒店团期产品信息
		ActivityHotelGroup activityHotelGroup = activityHotelGroupService.getByUuid(activityHotelGroupUuid);
		if (activityHotelGroup == null) {
			return ORDER_HOTEL_PRODUCT_LIST;
		}
		//加载酒店产品信息
		ActivityHotel activityHotel = activityHotelService.getByUuid(activityHotelGroup.getActivityHotelUuid());
		//预报名
		activityHotelGroup.setOrderNum(hotelOrderService.getForecaseReportNum(activityHotelGroup.getUuid()));
		//获取酒店信息
		Hotel hotel = hotelService.getByUuid(activityHotel.getHotelUuid());
		Integer hotelLevel = null;
		if (hotel != null) {
			HotelStar hotelStar = hotelStarService.getByUuid(hotel.getStar());
			if (hotelStar != null) {
				hotelLevel = hotelStar.getValue();
			}
		}

		//加载酒店产品团期价格表
//		List<ActivityHotelGroupPrice> groupPrices = activityHotelGroupPriceService.getPriceListByGroupUuid(activityHotelGroupUuid);
//		List<ActivityHotelGroupPrice> travelerTypeList = Lists.newArrayList();
//		if (CollectionUtils.isNotEmpty(groupPrices)) {
//			for (ActivityHotelGroupPrice groupPrice : groupPrices) {
//				HotelOrderPrice hotelOrderPrice = hotelOrderPriceService.getByOrderUuidAndGroupPriceUuid(hotelOrder.getUuid(), groupPrice.getUuid());
//				if (hotelOrderPrice == null) {
//					hotelOrderPrice = new HotelOrderPrice();
//				}
//				int num = hotelOrderPrice.getNum() == null ? 0 : hotelOrderPrice.getNum();	//订单人数
//				Double price = groupPrice.getPrice() == null ? 0 : groupPrice.getPrice();	//订单单价（从团期获取）
//				Double total = price * num;		//小计
//				groupPrice.setSubTotal(new BigDecimal(total));	//设置小计金额
//				groupPrice.setNum(num);	//设置人数
//				//去重游客类型
//				if (!travelerTypeList.contains(groupPrice.getType())) {
//					travelerTypeList.add(groupPrice);
//				}
//			}
//		}
		
		List<HotelOrderPrice> groupPrices = new ArrayList<HotelOrderPrice>();
		List<HotelOrderPrice> travelerTypeList = Lists.newArrayList();
		//获取订单价格列表
		List<HotelOrderPrice> hotelOrderPriceList = hotelOrderPriceService.getByOrderUuid(orderUuid);
		if(CollectionUtils.isNotEmpty(hotelOrderPriceList)){
			for(HotelOrderPrice hotelOrderPrice : hotelOrderPriceList){
				//获取团期价格
				if(hotelOrderPrice != null && "1".equals(hotelOrderPrice.getPriceType().toString())){
					int num = hotelOrderPrice.getNum() == null ? 0:hotelOrderPrice.getNum();	//人数
					Double price = hotelOrderPrice.getPrice() == null ? 0 : hotelOrderPrice.getPrice();	//价钱
					Double total = price * num;		//小计
					hotelOrderPrice.setSubTotal(new BigDecimal(total));	//设置小计金额
					groupPrices.add(hotelOrderPrice);
					
					//去重游客类型
					if(travelerTypeList.size() == 0){
						travelerTypeList.add(hotelOrderPrice);
					}else{
						int i = 0; 
						int size = travelerTypeList.size();
						for(i = 0 ;i< size;i++){
							HotelOrderPrice orderPrice = travelerTypeList.get(i);
							if(StringUtils.isNotEmpty(orderPrice.getTravelerType())){
								if(orderPrice.getTravelerType().equals(hotelOrderPrice.getTravelerType())){
									break;
								}
								if(i == size -1){
									travelerTypeList.add(hotelOrderPrice);
								}
							}
						}
					}
					
				}
			}
		}
		
		//加载渠道信息
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();
		//签证类型
		List<SysDict> visaTypes = sysDictService.findByType(SysDict.TYPE_NEW_VISA_TYPE);
		
		//加载团期下所有的基础餐型数据
		List<ActivityHotelGroupMeal> groupMeals = activityHotelGroupMealService.getMealListByGroupUuid(activityHotelGroupUuid);
		//加载团期下所有的房型数据
		List<ActivityHotelGroupRoom> groupRooms = activityHotelGroupRoomService.getByactivityHotelGroupUuid(activityHotelGroupUuid);
		
		//预订人联系人信息
		List<OrderContacts> orderContactsList = orderContactsService.findOrderContactsByOrderIdAndOrderType((long)hotelOrder.getId(), Context.ORDER_TYPE_HOTEL.intValue());
		
		//酒店控房详情数据
		List<HotelControlDetailModel> hotelControlDetailList = hotelControlService.getControlDetailsByHotelUuid(activityHotel.getHotelUuid());
		
		//查询游客信息列表
		HotelTravelerQuery hotelTravelerQuery = new HotelTravelerQuery();
		hotelTravelerQuery.setOrderUuid(hotelOrder.getUuid());
		hotelTravelerQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		List<HotelTraveler> travelerList = hotelTravelerService.find(hotelTravelerQuery);
		if (CollectionUtils.isNotEmpty(travelerList)) {
			//酒店游客签证信息查询条件
			HotelTravelervisaQuery hotelTravelervisaQuery = new HotelTravelervisaQuery();	
			hotelTravelervisaQuery.setHotelOrderUuid(orderUuid);
			//酒店游客证件类型查询条件
			HotelTravelerPapersTypeQuery hotelTravelerPapersTypeQuery = new  HotelTravelerPapersTypeQuery();
			hotelTravelerPapersTypeQuery.setOrderUuid(orderUuid);
			//附件信息查询条件
			HotelAnnex hotelAnnex = new HotelAnnex();
			//游客金额信息查询条件
			HotelMoneyAmountQuery hotelMoneyAmountQuery  = new HotelMoneyAmountQuery();
			for (HotelTraveler traveler: travelerList) {
				hotelTravelervisaQuery.setHotelTravelerUuid(traveler.getUuid());
				hotelTravelervisaQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				//游客签证信息查询
				traveler.setHotelTravelervisaList(hotelTravelervisaService.find(hotelTravelervisaQuery));
				//证件类型
				hotelTravelerPapersTypeQuery.setHotelTravelerUuid(traveler.getUuid());
				hotelTravelerPapersTypeQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				traveler.setHotelTravelerPapersTypeList(hotelTravelerPapersTypeService.find(hotelTravelerPapersTypeQuery));
				//附件信息
				hotelAnnex.setMainUuid(traveler.getUuid());
				hotelAnnex.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				traveler.setHotelTravelerFilesList(hotelAnnexService.find(hotelAnnex));
				//游客金额信息
				hotelMoneyAmountQuery.setBusinessUuid(traveler.getUuid());
				hotelMoneyAmountQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				hotelMoneyAmountQuery.setMoneyType(Context.MONEY_TYPE_JSJ);
				traveler.setHotelMoneyAmountList(hotelMoneyAmountService.find(hotelMoneyAmountQuery));
			}
		}
		//签证国家
		List<SysGeography> sysGeographyList = Lists.newArrayList();
		sysGeographyList.add(new SysGeography());
		sysGeographyList.addAll(sysGeographyService.getAllCountryName());
		model.addAttribute("sysGeographyList", sysGeographyList);
		
		//读取公司配置币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("hotelOrder", hotelOrder);
		model.addAttribute("isTransfer", isTransfer);
		model.addAttribute("hotelLevel", hotelLevel);
		
//		model.addAttribute("costMoneyStr", costMoneyStr);	//订单总额(成本价)
//		model.addAttribute("totalMoneyStr", totalMoneyStr);	//订单结算总额
//		model.addAttribute("payedMoneyStr", payedMoneyStr); //订单已付金额
//		model.addAttribute("noPayMoneyStr", noPayMoneyStr); //订单未收金额
		model.addAttribute("agentList", agentInfos);
		if (CollectionUtils.isEmpty(agentInfos)) {
			agentInfos.add(new Agentinfo());
		}
		model.addAttribute("groupPrices", groupPrices);
		model.addAttribute("activityHotelGroup", activityHotelGroup);
		model.addAttribute("activityHotel", activityHotel);
		model.addAttribute("visaTypes", visaTypes);
//		if(groupAirlines != null && groupAirlines.size() > 0) {
//			//加载所有余位数
//			model.addAttribute("groupAirline", groupAirlines.get(0));
//		}
		model.addAttribute("groupMeals", groupMeals);
		model.addAttribute("groupRooms", groupRooms);
		model.addAttribute("hotelStar", hotelService.getHotelStarValByHotelUuid(activityHotel.getHotelUuid()));
		if (CollectionUtils.isEmpty(orderContactsList)) {
			orderContactsList.add(new OrderContacts());
		}
		List<OrderContacts> tempContacts = Lists.newArrayList();
		tempContacts.add(new OrderContacts());
		//签约渠道列表
		List<Agentinfo>  agentinfoList = agentinfoService.findAllAgentinfoBySalerId(hotelOrder.getOrderSalerId());
	    model.addAttribute("agentinfoList", agentinfoList);	//add by zhangcl
	    model.addAttribute("firstAgent", CollectionUtils.isEmpty(agentinfoList) ? new Agentinfo() : agentinfoList.get(0));
		//如果未取到渠道商id，则默认为 非签约渠道-1
		if (hotelOrder.getOrderCompany() == null) {
			hotelOrder.setOrderCompany(-1);
		}
		//默认渠道（）
		Agentinfo defaultFirstAgentinfo;
		if (hotelOrder.getOrderCompany() != -1) {			
			defaultFirstAgentinfo =  agentinfoService.findAgentInfoById(Long.parseLong(hotelOrder.getOrderCompany().toString()));
		} else {
			defaultFirstAgentinfo = CollectionUtils.isEmpty(agentinfoList) ? new Agentinfo() : agentinfoList.get(0);
		}
		List<OrderContacts> orderContactsListSig = defaultFirstAgentinfo.getId() == -1 ? tempContacts : orderContactsList;
		model.addAttribute("orderContactsList", orderContactsListSig);
		model.addAttribute("hotelOrderPriceList", hotelOrderPriceList);
		model.addAttribute("hotelControlDetailList", hotelControlDetailList);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("travelerTypeList", travelerTypeList);//游客类型列表
		
		//add start by yang.jiang 2016-01-17 20:30:39
		//获取默认的第一个渠道
		if (CollectionUtils.isEmpty(agentInfos)) {
			agentInfos.add(new Agentinfo());
		}
		List<OrderContacts> orderContactsListNon = defaultFirstAgentinfo.getId() != -1 ? tempContacts : orderContactsList;
		model.addAttribute("orderContactsListNon", orderContactsListNon);	//预订人联系人信息
		List<OrderContacts> orderContacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(Long.parseLong(hotelOrder.getId().toString()), Context.ORDER_TYPE_HOTEL);
		if (CollectionUtils.isEmpty(orderContacts)) {
			orderContacts.add(new OrderContacts());
		}
		model.addAttribute("orderContactsSrc", orderContacts);  //保存的是最原始的联系人---供非签约渠道订单修改使用
		if (CollectionUtils.isNotEmpty(orderContactsListSig)) {			
			net.sf.json.JSONArray orderContactListJsonArray = net.sf.json.JSONArray.fromObject(orderContactsListSig);
			model.addAttribute("orderContactsListJsonArray", orderContactListJsonArray.toString());
		}
		//渠道商的联系地址
		List<SupplierContacts> contacts = supplierContactsService.findAllContactsByAgentInfo(defaultFirstAgentinfo.getId());  //取出渠道商所有联系人（包括第一联系人）
		String address = agentinfoService.getAddressStrById(defaultFirstAgentinfo.getId());
		model.addAttribute("agent", defaultFirstAgentinfo);
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
		
	//				String contactsJsonStr = orderContactsService.contacts2Json(orderContacts);
	//				org.json.JSONArray contactArray = orderContactsService.contacts2JsonArray(orderContacts);
	//				model.addAttribute("contactsJsonStr", contactsJsonStr);
	//				model.addAttribute("contactArray", contactArray);
				
		//订单是否允许添加多个渠道联系人信息
		Integer allowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();
		model.addAttribute("allowAddAgentInfo", allowAddAgentInfo);
		//订单是否允许渠道联系人信息输入和修改
		Integer allowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();
		model.addAttribute("allowModifyAgentInfo", allowModifyAgentInfo);				
		//end
		
		return  ORDER_HOTEL_ORDER_UPDATE;
	}
	
	/**
	 * 封装酒店公共数据信息
	 * @param orderId
	 * @param model
	 */
	public void setHotelOrderDetail(String orderUuid,Model model){
		//获取订单信息
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		//获取产品信息
		ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
		//获取产品团期
		ActivityHotelGroup activityHotelGroup = activityHotelGroupService.getByUuid(hotelOrder.getActivityHotelGroupUuid());
		//获取酒店信息
		Hotel hotel = hotelService.getByUuid(activityHotel.getHotelUuid());
		Integer hotelLevel = null;
		if (hotel != null) {
			HotelStar hotelStar = hotelStarService.getByUuid(hotel.getStar());
			if (hotelStar != null) {
				hotelLevel = hotelStar.getValue();
			}
		}
		
		List<HotelOrderPrice> groupPrices = new ArrayList<HotelOrderPrice>();
		List<HotelOrderPrice> travelerTypeList = Lists.newArrayList();
		//获取订单价格列表
		List<HotelOrderPrice> hotelOrderPriceList = hotelOrderPriceService.getByOrderUuid(orderUuid);
		if(CollectionUtils.isNotEmpty(hotelOrderPriceList)){
			for(HotelOrderPrice hotelOrderPrice : hotelOrderPriceList){
				//获取团期价格
				if(hotelOrderPrice != null && "1".equals(hotelOrderPrice.getPriceType().toString())){
					int num = hotelOrderPrice.getNum() == null ? 0:hotelOrderPrice.getNum();	//人数
					Double price = hotelOrderPrice.getPrice() == null ? 0 : hotelOrderPrice.getPrice();	//价钱
					Double total = price * num;		//小计
					hotelOrderPrice.setSubTotal(new BigDecimal(total));	//设置小计金额
					groupPrices.add(hotelOrderPrice);
					
					//去重游客类型
					if(travelerTypeList.size() == 0){
						travelerTypeList.add(hotelOrderPrice);
					}else{
						int i = 0; 
						int size = travelerTypeList.size();
						for(i = 0 ;i< size;i++){
							HotelOrderPrice orderPrice = travelerTypeList.get(i);
							if(StringUtils.isNotEmpty(orderPrice.getTravelerType())){
								if(orderPrice.getTravelerType().equals(hotelOrderPrice.getTravelerType())){
									break;
								}
								if(i == size -1){
									travelerTypeList.add(hotelOrderPrice);
								}
							}
						}
					}
				}
			}
		}
		
		//渠道商信息
		Agentinfo agentinfo = agentinfoService.findAgentInfoById((long)hotelOrder.getOrderCompany());
		//预订人联系人信息
		List<OrderContacts> orderContactsList = orderContactsService.findOrderContactsByOrderIdAndOrderType((long)hotelOrder.getId(), Context.ORDER_TYPE_HOTEL.intValue());
		if (CollectionUtils.isEmpty(orderContactsList)) {
			orderContactsList.add(new OrderContacts());
		}
		
		//查询游客信息列表
		HotelTravelerQuery hotelTravelerQuery = new HotelTravelerQuery();
		hotelTravelerQuery.setOrderUuid(hotelOrder.getUuid());
		hotelTravelerQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		hotelTravelerQuery.setStatus("0");
		List<HotelTraveler> travelerList = hotelTravelerService.find(hotelTravelerQuery);
		if(travelerList != null && travelerList.size() > 0){
			//酒店游客签证信息查询条件
			HotelTravelervisaQuery hotelTravelervisaQuery = new HotelTravelervisaQuery();	
			hotelTravelervisaQuery.setHotelOrderUuid(orderUuid);
			//酒店游客证件类型查询条件
			HotelTravelerPapersTypeQuery hotelTravelerPapersTypeQuery = new  HotelTravelerPapersTypeQuery();
			hotelTravelerPapersTypeQuery.setOrderUuid(orderUuid);
			//附件信息查询条件
			HotelAnnex hotelAnnex = new HotelAnnex();
			//游客金额信息查询条件
			HotelMoneyAmountQuery hotelMoneyAmountQuery  = new HotelMoneyAmountQuery();
			for(HotelTraveler traveler: travelerList ){
				hotelTravelervisaQuery.setHotelTravelerUuid(traveler.getUuid());
				hotelTravelervisaQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				//游客签证信息查询
				traveler.setHotelTravelervisaList(hotelTravelervisaService.find(hotelTravelervisaQuery));
				//证件类型
				hotelTravelerPapersTypeQuery.setHotelTravelerUuid(traveler.getUuid());
				hotelTravelerPapersTypeQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				traveler.setHotelTravelerPapersTypeList(hotelTravelerPapersTypeService.find(hotelTravelerPapersTypeQuery));
				//附件信息
				hotelAnnex.setMainUuid(traveler.getUuid());
				hotelAnnex.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				traveler.setHotelTravelerFilesList(hotelAnnexService.find(hotelAnnex));
				//游客金额信息
				hotelMoneyAmountQuery.setBusinessUuid(traveler.getUuid());
				hotelMoneyAmountQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				hotelMoneyAmountQuery.setMoneyType(Context.MONEY_TYPE_JSJ);
				traveler.setHotelMoneyAmountList(hotelMoneyAmountService.find(hotelMoneyAmountQuery));
			}
		}
				
		//加载团期下所有的基础餐型数据
		List<ActivityHotelGroupMeal> groupMeals = activityHotelGroupMealService.getMealListByGroupUuid(activityHotelGroup.getUuid());
		//加载团期下所有的房型数据
		List<ActivityHotelGroupRoom> groupRooms = activityHotelGroupRoomService.getByactivityHotelGroupUuid(activityHotelGroup.getUuid());
		//预报名
		activityHotelGroup.setOrderNum(hotelOrderService.getForecaseReportNum(activityHotelGroup.getUuid()));
		
		//获取订单总额(成本价)
		String costMoneyStr =  hotelMoneyAmountService.getMoneyStr(hotelOrder.getCostMoney(), true);
		//获取订单结算总额(结算价,应收金额)
		String totalMoneyStr = hotelMoneyAmountService.getMoneyStr(hotelOrder.getTotalMoney(), true);
		//获取订单已付金额(已付金额)
		String payedMoneyStr = hotelMoneyAmountService.getMoneyStr(hotelOrder.getPayedMoney(), true);
		//订单未收金额( totalMoneyStr - payedMoneyStr  )
		String noPayMoneyStr = "";
		if(!StringUtils.isEmpty(totalMoneyStr) && !StringUtils.isEmpty(payedMoneyStr) ){
			 noPayMoneyStr =  hotelMoneyAmountService.addOrSubtract(totalMoneyStr, payedMoneyStr, false);
		}
		
		//加载渠道信息
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();// 渠道信息列表
		model.addAttribute("agentList", agentInfos);
		
		//签证国家
		List<SysGeography> sysGeographyList = Lists.newArrayList();
		sysGeographyList.add(new SysGeography());
		sysGeographyList.addAll(sysGeographyService.getAllCountryName());
		model.addAttribute("sysGeographyList", sysGeographyList);
		//签证类型
		List<SysDict> visaTypes = sysDictService.findByType(SysDict.TYPE_NEW_VISA_TYPE);
		model.addAttribute("visaTypes", visaTypes);
		
		//读取公司配置币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		
		model.addAttribute("hotelOrder", hotelOrder);	//获取订单信息
		model.addAttribute("activityHotel", activityHotel);	//获取产品信息
		model.addAttribute("activityHotelGroup", activityHotelGroup);//获取产品团期
		model.addAttribute("hotelLevel", hotelLevel);
		model.addAttribute("agentinfo", agentinfo);	//渠道商信息
		model.addAttribute("orderContactsList", orderContactsList);	//预订人联系人信息
		model.addAttribute("travelerList", travelerList);	//查询游客信息列表
		model.addAttribute("groupPrices", groupPrices);	//查询游客信息列表
		model.addAttribute("groupMeals", groupMeals);	//加载团期下所有的基础餐型数据
		model.addAttribute("groupRooms", groupRooms);	//加载团期下所有的基础餐型数据
		model.addAttribute("hotelOrderPriceList", hotelOrderPriceList);
		
		;
		
		model.addAttribute("costMoneyStr", handlerCurrencyStr(costMoneyStr));	//订单总额(成本价)
		model.addAttribute("totalMoneyStr", handlerCurrencyStr(totalMoneyStr));	//订单结算总额
		model.addAttribute("payedMoneyStr", handlerCurrencyStr(payedMoneyStr)); //订单已付金额
		model.addAttribute("noPayMoneyStr", handlerCurrencyStr(noPayMoneyStr)); //订单未收金额
		
		model.addAttribute("travelerTypeList", travelerTypeList);//游客类型列表
	}
	
	private String handlerCurrencyStr(String currencyStr) {
		String returnStr = new String();
		if (StringUtils.isNotBlank(currencyStr)) {
			String[] srcCurrencyArr = currencyStr.split("\\+");
			if (srcCurrencyArr != null && srcCurrencyArr.length > 0) {
				for (int i = 0; i < srcCurrencyArr.length; i++) {
					String temp = srcCurrencyArr[i].trim();
					String tempCurrency = "";
					if (StringUtils.isNotBlank(temp) && temp.split(" ").length == 2) {
						tempCurrency = temp.split(" ")[0] + temp.split(" ")[1].replace(",", "");
					}
					returnStr += tempCurrency + " + ";
				}
			}
		}
		String substring = returnStr.substring(0, returnStr.length() - 3);
		return substring;
	}
	
	/**
	 * 修改酒店订单
	 * @param datas
	 * @throws Exception 
	 */
	@RequestMapping(value="updateHotelOrder")
	@ResponseBody
	public Object updateHotelOrder(@RequestBody String jsonData) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("code", "0");	//返回码,默认失败
		resultMap.put("message","失败");//返回消息
		try {
			int resultCode = hotelOrderService.updateHotelOrder(jsonData);
			if (resultCode == 1) {
				resultMap.put("code", "1");
				resultMap.put("message","成功");//返回消息
			}
		} catch (Exception e) {
			resultMap.put("message","");//返回消息
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 加载渠道商联系人信息
	 * @author gao
	*  2015年8月18日
	 * @param id 渠道商ID
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="loadAgentInfo", method=RequestMethod.POST)
	public Map<String,Object> loadAgentInfo(String id,HttpServletRequest req){
		Map<String,Object> map = new HashMap<String,Object>();
		Agentinfo agentinfo = new Agentinfo();
		if(StringUtils.isNotBlank(id)){
			agentinfo = agentinfoService.loadAgentInfoById(Long.valueOf(id));
			if(agentinfo!=null){
				map.put("res", "success");
				map.put("agentinfo", agentinfo);
			}else{
				map.put("res", "error");
			}
		}else{
			map.put("res", "error");
		}
		return map;
	}
}