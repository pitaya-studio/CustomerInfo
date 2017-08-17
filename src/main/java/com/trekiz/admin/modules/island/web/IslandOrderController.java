/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.text.SimpleDateFormat;
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
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.service.SupplyContactsService;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.model.HotelControlDetailModel;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelControlService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandOrderPrice;
import com.trekiz.admin.modules.island.entity.IslandRebates;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.entity.IslandTravelerPapersType;
import com.trekiz.admin.modules.island.input.IslandOrderInput;
import com.trekiz.admin.modules.island.query.ActivityIslandQuery;
import com.trekiz.admin.modules.island.query.IslandMoneyAmountQuery;
import com.trekiz.admin.modules.island.query.IslandOrderQuery;
import com.trekiz.admin.modules.island.query.IslandTravelerPapersTypeQuery;
import com.trekiz.admin.modules.island.query.IslandTravelerQuery;
import com.trekiz.admin.modules.island.query.IslandTravelervisaQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupPriceService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupRoomService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderPriceService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandReviewService;
import com.trekiz.admin.modules.island.service.IslandTravelerPapersTypeService;
import com.trekiz.admin.modules.island.service.IslandTravelerService;
import com.trekiz.admin.modules.island.service.IslandTravelervisaService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.pay.entity.PayIslandOrder;
import com.trekiz.admin.modules.pay.service.PayIslandOrderService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.AirlineInfoDao;
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
@RequestMapping(value = "${adminPath}/islandOrder")
public class IslandOrderController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/island/islandorder/orderList";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/islandOrder/list";
	protected static final String FORM_PAGE = "modules/island/islandorder/form";
	protected static final String SHOW_PAGE = "modules/island/islandorder/show";
	
	protected static final String SAVE_ISLAND_ORDER_PAGE = "modules/island/islandorder/saveIslandOrder";
	
	protected static final String ORDER_ISLAND_PRODUCT_LIST = "modules/island/islandorder/orderIslandProductList";
	protected static final String ORDER_ISLAND_GROUP_LIST = "modules/island/islandorder/orderIslandGroupList";
	protected static final String ORDER_ISLAND_ORDER_DETAIL = "modules/island/islandorder/orderIslandDetail";
	protected static final String ORDER_ISLAND_ORDER_UPDATE = "modules/island/islandorder/orderIslandUpdate";
	
	//修改支付凭证跳转链接
	protected static final String MODIFY_PAY_VOUCHER = "modules/island/islandorder/modifyPayVoucher";
	//修改支付凭证成功后跳转链接
	protected static final String UPLOAD_VO_SUCCESS = "modules/island/islandorder/uploadVoSuccess";
	
	//退团列表
	protected static final String VIEW_EXIT_GROUP = "modules/island/islandorder/viewExitGroup";
	//退团申请页
	protected static final String VIEW_EXIT_GROUP_INFO = "modules/island/islandorder/viewExitGroupInfo";
	//申请退团
	protected static final String APPLY_EXIT_GROUP = "modules/island/islandorder/applyExitGroup";
	
	//审核中状态
	private static final Integer REVIEW_UNAUDITED = 1;
	
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private PayIslandOrderService payIslandOrderService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ActivityIslandGroupPriceService activityIslandGroupPriceService;
	@Autowired
    private DepartmentService departmentService;
	@Autowired
    private ReviewService reviewService;
	@Autowired
    private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private IslandTravelerService islandTravelerService;
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;
	@Autowired
	private ActivityIslandGroupRoomService activityIslandGroupRoomService;
	@Autowired
	private ActivityIslandGroupMealService activityIslandGroupMealService;
	@Autowired
	private HotelStarService hotelStarService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OrderContactsService orderContactsService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private IslandOrderPriceService islandOrderPriceService;
	@Autowired
	private HotelControlService hotelControlService;
	@Autowired
	private IslandTravelervisaService islandTravelervisaService;
	@Autowired
	private IslandTravelerPapersTypeService islandTravelerPapersTypeService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	@Autowired
	private SysGeographyService sysGeographyService;
	@Autowired
	private AirlineInfoDao airlineInfoDao;
	@Autowired
	private LogOperateService logOperateService;
	@Autowired
	private IslandReviewService islandReviewService;
	@Autowired
	private SupplyContactsService supplyContactsService;
	
	private IslandOrder dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=islandOrderService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list/{orderStatus}")
	public String list(@PathVariable Integer orderStatus, IslandOrderQuery islandOrderQuery, HttpServletRequest request, 
			HttpServletResponse response, Model model) {
		
		//设置订单查询类型
		islandOrderQuery.setOrderStatus(orderStatus);
		
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("order", model);
		
        //根据订单编号查询此订单对应所在的部门
        getDepartmentIdByOrderNum(common, request, model);
        
        //订单或团期查询
        Page<Map<Object, Object>> pageOrder = islandOrderService.findOrderList(new Page<Map<Object, Object>>(request, response), islandOrderQuery, common);
        model.addAttribute("page", pageOrder);
        List<Map<Object, Object>> listorder = pageOrder.getList();
        islandOrderQuery.setOrderBy(pageOrder.getOrderBy().replace("agp", "pro"));
        pageOrder.setOrderBy(islandOrderQuery.getOrderBy());
	        
        List<String> groupIdList = Lists.newArrayList();
        List<String> orderUUIDList = Lists.newArrayList();
        for (Map<Object, Object> listin : listorder) {
        	if (!islandOrderQuery.getIsOrder() && listin.get("groupUuid") != null) {
        		groupIdList.add(listin.get("groupUuid").toString());
        	}
        	if (listin.get("createBy") != null) {
        		listin.put("carateUserName", UserUtils.getUser(Long.parseLong(listin.get("createBy").toString())).getName());
        	}
        	if (islandOrderQuery.getIsOrder() && listin.get("orderUuid") != null) {
        		orderUUIDList.add(listin.get("orderUuid").toString());
        	}
            
        	//金额处理
        	handlePrice(listin);
        	
            //转团转款标志位 1-可以转款  0-不可转款
            listin.put("transferMoneyCheck", "0");
            if (islandOrderQuery.getIsOrder()) {
            	IslandOrder order = islandOrderService.getById(Integer.parseInt(String.valueOf(listin.get("id"))));
            	if (order != null) {
	            	List<Review> list = reviewService.findReview(order.getOrderStatus(),Context.REVIEW_FLOWTYPE_TRANSFER_GROUP,String.valueOf(listin.get("id")),Context.REVIEW_STATUS_DONE,Context.REVIEW_ACTIVE_EFFECTIVE);
	            	if (list!=null&&list.size()>0){
	            		listin.put("transferMoneyCheck", "1");
	            	}
            	}
            }
           
        }
	        
        List<Map<Object, Object>> orderList = null;
        if (!islandOrderQuery.getIsOrder() && CollectionUtils.isNotEmpty(groupIdList)) {
        	orderList = islandOrderService.findByGroupIds(new Page<Map<Object, Object>>(request, response), groupIdList, islandOrderQuery.getOrderSql()).getList();
        	for (Map<Object, Object> listin : orderList) {
        		//金额处理
        		handlePrice(listin);
        		
        		//转团转款标志位 1-可以转款  0-不可转款
                listin.put("transferMoneyCheck", "0");
                IslandOrder order = islandOrderService.getById(Integer.parseInt(String.valueOf(listin.get("id"))));
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
        model.addAttribute("users",systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
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
        model.addAttribute("islandOrderQuery", islandOrderQuery);
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
        	IslandOrderQuery islandOrderQuery = new IslandOrderQuery();
        	islandOrderQuery.setOrderNum(orderNumOrGroupCode);
        	List<IslandOrder> list = islandOrderService.find(islandOrderQuery);
        	if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
        		String productUUID = list.get(0).getActivityIslandUuid();
        		Integer createById = activityIslandService.getByUuid(productUUID).getCreateBy();
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
				listin.put("notPayedMoney", islandMoneyAmountService.addOrSubtract(totalMoney, payedMoney, false));
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
							String moneyAmonut = d.format(new BigDecimal(currencyMoney));
							listin.put(para, currencyMark + moneyAmonut);
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
			List<PayIslandOrder> orderPayList = Lists.newArrayList();
			orderPayList = payIslandOrderService.findOrderPayByOrderUuids(orderUUIDList);
            for (Map<Object, Object> map : listorder) {
            	Integer isAsAccount = 0;// 空为未达帐 0为撤销 1为达帐 2为驳回
                List<PayIslandOrder> listTempOrderPay = Lists.newArrayList();
                boolean bCheckFlg = false;
                for (PayIslandOrder orderpay : orderPayList) {
                    //如果orderpay的订单id  等于  pro的订单id
                    if (orderpay.getOrderUuid().equals(map.get("orderUuid").toString())) {
                        listTempOrderPay.add(orderpay);
                        if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
                        	islandOrderService.clearObject(orderpay);
                        	orderpay.setMoneySerialNum(islandMoneyAmountService.getMoneyStr(orderpay.getMoneySerialNum(), true));
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
                	isCanceledOrder = Context.ISLAND_ORDER_STATUS_YQX.equals(map.get("orderStatus").toString());
                }
                map.put("promptStr", islandOrderService.getOrderPrompt(map.get("orderUuid").toString(), isCanceledOrder));
            }
        }
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
        	 this.islandOrderService.lockOrder(uuid);
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
        	 this.islandOrderService.unLockOrder(uuid);
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
        	 this.islandOrderService.setOrderFiles(orderUuid, fileIds);
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
			docInfoList = this.islandOrderService.getFilesInfo(orderUuid);
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
	    	List<IslandOrder> islandOrderList = islandOrderService.getByUuids(orderUuidList);
	    	if (CollectionUtils.isNotEmpty(islandOrderList)) {
	    		for (IslandOrder islandOrder : islandOrderList) {
					if (StringUtils.isNotBlank(islandOrder.getFileIds())) {
						docIds += islandOrder.getFileIds();
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
	    	IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
	    	if (islandOrder != null) {
    			Integer orderStatus = islandOrder.getOrderStatus();
    			if (Context.ISLAND_ORDER_STATUS_YQX == orderStatus) {
    				flag = islandOrderService.invokeOrder(orderUuid, request);
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
        	IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
    		if (islandOrder != null) {
    			Integer orderStatus = islandOrder.getOrderStatus();
    			//只有待确认的订单可以取消，已确认订单不能取消
    			if (Context.ISLAND_ORDER_STATUS_DQR == orderStatus) {
    				islandOrderService.cancelOrder(islandOrder, description, request);
    			} else {
    				if (Context.ISLAND_ORDER_STATUS_YQR == orderStatus) {
    					return "订单已确认，不能取消";
    				} else if (Context.ISLAND_ORDER_STATUS_YQX == orderStatus) {
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
		
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		
		//如果订单为空则返回错误
		if (islandOrder == null) {
			resobj.put("flag", "false");
			resobj.put("warning", "查询不到此订单");
			results.add(resobj);
			return results;
		}
		
		List<String> totalCurreney = Lists.newArrayList();
		BigDecimal payedMoney = new BigDecimal(0);
		BigDecimal totalMoney = new BigDecimal(0);
			
		//应收
		if (StringUtils.isNotBlank(islandOrder.getTotalMoney())) {
			List<Object[]> list = islandMoneyAmountService.getMoneyAmonut(islandOrder.getTotalMoney());
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
		if (StringUtils.isNotBlank(islandOrder.getPayedMoney())) {
			List<Object[]> list = islandMoneyAmountService.getMoneyAmonut(islandOrder.getPayedMoney());
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
		List<String> result = islandMoneyAmountService.subtract(totalCurreney, payedCurreney);
			
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
		
		Map<String, String> totalMoneyMap = moneyContactVal(islandMoneyAmountService.getMoneyAmonut(islandOrder.getTotalMoney()));
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
		
		PayIslandOrder payIslandOrder = payIslandOrderService.getByUuid(payUuid);
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
	    
	    //支付订单金额千位符处理
	    if (StringUtils.isNotBlank(payIslandOrder.getMoneySerialNum())) {
	    	islandOrderService.clearObject(payIslandOrder);
	    	payIslandOrder.setMoneySerialNum(islandMoneyAmountService.getMoneyStr(payIslandOrder.getMoneySerialNum(), true));
	    }
	    
	    //订单金额千位符处理
	    if (StringUtils.isNotBlank(islandOrder.getTotalMoney())) {
	    	islandOrderService.clearObject(islandOrder);
	    	islandOrder.setTotalMoney(islandMoneyAmountService.getMoneyStr(islandOrder.getTotalMoney(), true));
	    }
	  
		model.addAttribute("orderpay",payIslandOrder);
		model.addAttribute("islandOrder",islandOrder);
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
		 PayIslandOrder payIslandOrder = payIslandOrderService.getByUuid(payUuid);
		 if (StringUtils.isNotBlank(remarks)) {
			 payIslandOrder.setRemarks(remarks);
		 }
		 islandOrderService.updatepayVoucherFile(infoList, payIslandOrder, orderUuid, model, request);
		
	    return UPLOAD_VO_SUCCESS;
	   
	}
	
	
	/**
	 * 查看退团列表
	 * @author  chenry
	 */
	@RequestMapping(value = "viewExitGroup")
	public String viewExitGroup(Model model,HttpServletRequest request,HttpServletResponse response, String orderUuid) {
		
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		List<Map<String, String>> pageOrder = islandOrderService.getExitGroupReviewInfo(
				Context.REVIEW_FLOWTYPE_EXIT_GROUP, islandOrder.getId().toString());
		
		model.addAttribute("page", pageOrder);
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("productType", Context.ProductType.PRODUCT_ISLAND);
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
		//游客状态改为正常 //add by WangXK 20151020添加非空指针判断
		if(null != r && r.getFlowType()!=null && r.getFlowType()!=null && r.getTravelerId()!=null){
			if (r.getFlowType().intValue() == Context.REVIEW_FLOWTYPE_EXIT_GROUP.intValue()  || r.getFlowType().intValue() == Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.intValue()) {
				IslandTraveler islandTraveler = islandTravelerService.getById(r.getTravelerId().intValue());
				islandTraveler.setDelFlag(Context.TRAVELER_DELFLAG_NORMAL.toString());
				islandTravelerService.update(islandTraveler);
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
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		Map<String, Object> travelerList = islandOrderService.getExitGroupReviewInfoById(rid);
		model.addAttribute("hashMap",travelerList);
		model.addAttribute("productType",productType);
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
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		List<Map<Object, Object>> travelerList = islandOrderService.getTravelerByOrderUuid(orderUuid);
		model.addAttribute("orderId", islandOrder.getId());
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		model.addAttribute("productType", Context.ProductType.PRODUCT_ISLAND);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("islandOrder", islandOrder);
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
			map = islandOrderService.saveExitGroupReviewInfo(
					Context.ProductType.PRODUCT_ISLAND, flowType, travelerId, travelerName, exitReason, orderUuid);
			map.put(Context.RESULT, 2);
		} catch (Exception e) {
			map.put(Context.RESULT, 0);
			map.put(Context.MESSAGE, e.getMessage());
			return map;
		}
		return map;
	}
	
	
	@RequestMapping(value = "form")
	public String form(IslandOrderInput islandOrderInput, Model model) {
		model.addAttribute("islandOrderInput", islandOrderInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(IslandOrderInput islandOrderInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			islandOrderService.save(islandOrderInput);
		} catch (Exception e) {
			result="0";
		}
		return result;
		
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("islandOrder", islandOrderService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		IslandOrder islandOrder = islandOrderService.getByUuid(uuid);
		IslandOrderInput islandOrderInput = new IslandOrderInput(islandOrder);
		model.addAttribute("islandOrderInput", islandOrderInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(IslandOrderInput islandOrderInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, islandOrderInput,true);
			islandOrderService.update(dataObj);
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
					islandOrderService.removeByUuid(uuid);
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
	 * 跳转到海岛游预报名页面
	*<p>Title: toSaveIslandOrder</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-14 下午6:37:07
	* @throws
	 */
	@RequestMapping(value = "toSaveIslandOrder/{activityIslandGroupUuid}")
	public String toSaveIslandOrder(@PathVariable String activityIslandGroupUuid, IslandOrderInput islandOrderInput, Model model) {
		//初始化预报名页面数据
		islandOrderService.initIslandOrderPageData(activityIslandGroupUuid,model);
		
		Integer isAllowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();//是否允许添加渠道联系人
		Integer isAllowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();//是否允许修改联系人信息
		model.addAttribute("isAllowAddAgentInfo", isAllowAddAgentInfo);
		model.addAttribute("isAllowModifyAgentInfo", isAllowModifyAgentInfo);
		
		return SAVE_ISLAND_ORDER_PAGE;
	}
	
	/**
	 * 海岛游预报名
	*<p>Title: saveIslandOrder</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-14 下午6:37:07
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "saveIslandOrder")
	public Object saveIslandOrder(IslandOrderInput islandOrderInput, Model model, HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		try{
			//原有逻辑在保存数据时，没有保存签约渠道的orderCompanyName，只保存了orderCompany(渠道的id)，关联查询agentinfo表 将正确数据写入orderCompanyName
			if(islandOrderInput.getOrderCompanyName()=="") {
				Integer orderCompanyTmp = islandOrderInput.getOrderCompany();
				Agentinfo agentinfo = agentinfoService.findAgentInfoById(Long.valueOf(orderCompanyTmp));
				islandOrderInput.setOrderCompanyName(agentinfo.getAgentName());
			}

			result = islandOrderService.saveIslandOrder(islandOrderInput);
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
	 * 海岛游订单支付
	*<p>Title: payIslandOrder</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-14 下午6:38:08
	* @throws
	 */
	@RequestMapping(value = "payIslandOrder/{orderUuid}")
	public String payIslandOrder(@PathVariable(value="orderUuid") String orderUuid,HttpServletRequest request,HttpServletResponse response)
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

		OrderPayInput orderPayInput = islandOrderService.buildOrderPayData(orderUuids, resultCurrency.split(";"), resultAmount.split(";"), cancelPayUrl);
		request.setAttribute("pay", orderPayInput);
		return "forward:"+Global.getAdminPath()+"/orderPayMore/pay";
	}
	
	@ResponseBody
	@RequestMapping(value = "loadAgentInfo")
	public Object loadAgentInfo(long id) {
		Map<String, Object> datas = new HashMap<String, Object>();
		Agentinfo agentinfo = agentinfoService.findAgentInfoById(id);
		if(agentinfo != null) {
//			agentinfo.setAgentAddress(agentinfoService.getAddressStrById(id));   
			datas.put("agentinfo", agentinfo);
		}
		
		String address = agentinfoService.getAddressStrById(id);
		datas.put("address", address);
		
		//加载联系人信息
		List<SupplyContacts> supplyContacts = supplyContactsService.findContactsByAgentInfo(id);
		if(supplyContacts != null){
			datas.put("supplyContacts", supplyContacts);
		}
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value="findSupplyContacts")
	public SupplyContacts findSupplyContacts(long id){
		//加载联系人信息
		SupplyContacts supplyContacts = supplyContactsService.findOne(id);
//		if(supplyContacts == null){
//			//第一联系人 在agentInfo表中
//			SupplyContacts firstContact = new SupplyContacts();
//			Agentinfo agentinfo = agentinfoService.findAgentInfoById(id);
//			firstContact.setId(agentinfo.getId());  //id ???
//			firstContact.setContactName(agentinfo.getAgentContact());  //name
//			firstContact.setContactMobile(agentinfo.getAgentContactMobile());  //手机
//			firstContact.setContactPhone(agentinfo.getAgentContactTel()); //电话
//			firstContact.setContactFax(agentinfo.getAgentContactFax());  //传真
//			firstContact.setContactEmail(agentinfo.getAgentContactEmail());   //邮箱
//			firstContact.setContactQQ(agentinfo.getAgentContactQQ());   //QQ
//			firstContact.setRemarks(agentinfo.getRemarks());   //备注
//			return firstContact;
//		}
		return supplyContacts;
	}
	
	// ajax请求，同上，但用于海岛游 订单修改
	@ResponseBody
	@RequestMapping(value = "loadAgentInfoUpdate")
	public Object loadAgentInfoUpdate(long id) {
		Map<String, Object> datas = new HashMap<String, Object>();
		Agentinfo agentinfo = agentinfoService.findAgentInfoById(id);
		if(agentinfo != null) {
			datas.put("agentinfo", agentinfo);
		}
		
		String address = agentinfoService.getAddressStrById(id);
		datas.put("address", address);
		
		//加载联系人信息
		List<OrderContacts> orderContacts = orderContactsService.findContactsByAgentInfo(id);
		if(orderContacts != null){
			datas.put("orderContacts", orderContacts);
		}
		return datas;
	}
	
	// ajax请求，同上，但用于海岛游 订单修改
	@ResponseBody
	@RequestMapping(value="findSupplyContactsUpdate")
	public OrderContacts findSupplyContactsUpdate(long id){
		//加载联系人信息
		OrderContacts orderContacts = orderContactsService.findOne(id);
		return orderContacts;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "loadAsynchronousData")
	public Object loadAsynchronousData(String islandOrderId) {
		Map<String, List<?>> datas = Maps.newHashMap();
		//修改数据
		List<Object[]> modifyData = Lists.newArrayList();
		modifyData = logOperateService.queryByParas(Context.ProductType.PRODUCT_ISLAND, Long.parseLong(islandOrderId), Context.log_state_up);
		datas.put("modifyData", modifyData);
		
		//转团记录
		if (StringUtils.isNotBlank(islandOrderId)) {
			// 海岛游转团申请记录
			IslandOrder order = islandOrderService.getById(Integer.parseInt(islandOrderId));
			ActivityIsland activityIsland = activityIslandService.getByUuid(order.getActivityIslandUuid());
			List<Map<String, String>> reviewList= new ArrayList<Map<String, String>>();
			reviewList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_ISLAND, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, order.getId().toString(), true,Long.valueOf(activityIsland.getDeptId()));
		
			datas.put("changeGroupData", reviewList);
		}
		
		//退团记录
		IslandOrder islandOrder = islandOrderService.getById(Integer.parseInt(islandOrderId));
		List<Map<String, String>> exitGroupData = islandOrderService.getExitGroupReviewInfo(
				Context.REVIEW_FLOWTYPE_EXIT_GROUP, islandOrder.getId().toString());
		datas.put("exitGroupData", exitGroupData);
		
		//改价记录
		List<Map<String, Object>> changePriceData = Lists.newArrayList();
		//changePriceData = reviewService.findReviewListMap(Context.ProductType.PRODUCT_ISLAND, 10, islandOrderId, false, "");
		changePriceData = getChangePriceList(islandOrderId);
		datas.put("changePriceData", changePriceData);
		
		//返佣记录
		List<IslandRebates> rebatesData = islandReviewService.findRebatesList(Long.parseLong(islandOrderId), Context.ORDER_TYPE_ISLAND);
		List<IslandRebates> tempData = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(rebatesData)) {
			Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
			String totalMoney = "";//累计返佣金额
			for (IslandRebates islandRebates : rebatesData) {
				IslandRebates temp = new IslandRebates();
				if (islandRebates.getTraveler() != null && StringUtils.isNotBlank(islandRebates.getTraveler().getUuid())) {
					String currencyMark = currencyService.findCurrency(islandRebates.getCurrencyId()).getCurrencyMark();
					String currencyMoney = islandRebates.getRebatesDiff().toString();
					//获取累计返佣金额
					String travelerId = islandRebates.getTravelerId().toString();
					if (t4priceMap.containsKey(travelerId)) {
						totalMoney = t4priceMap.get(travelerId);
					} else {
						totalMoney = "";
					}
					totalMoney = islandMoneyAmountService.addOrSubtract(currencyMark + " " + currencyMoney, totalMoney, true);
					t4priceMap.put(travelerId, totalMoney);
					temp.setRebatesdiffString1(totalMoney);
				}
				temp.setCreateDate(islandRebates.getCreateDate());
				temp.setCostname(islandRebates.getCostname());
				temp.setTotalMoney(islandRebates.getTotalMoney());
				temp.setRemark(islandRebates.getRemark());
				temp.setRebatesDiff(islandRebates.getRebatesDiff());
				temp.setTraveler(islandRebates.getTraveler());
				temp.setCurrency(islandRebates.getCurrency());
				tempData.add(temp);
			}
		}
		
		//对结果集进行翻转（前台按创建时间倒叙排列）
		List<Map <String,Object>> refundData = getRefundList(islandOrderId);
		List<Map <String,Object>> borrowingData = getBorrowingList(islandOrderId);
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
				return islandMoneyAmountService.getMoneyStr(queryValue, true);
			}
			return islandOrderService.getData(tableName, queryName, queryValue, getName);
		}
		return "";
	}
	@ResponseBody
	@RequestMapping(value = "getSpaceGradeTypeData")
	public String getSpaceGradeTypeData(String type, String queryName, String queryValue, String getName) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(queryName) 
				&& StringUtils.isNotBlank(queryValue) && StringUtils.isNotBlank(getName)) {
			
			return islandOrderService.getSpaceGradeTypeData(type, queryName, queryValue, getName);
		}
		return "";
	}
	/**
	 * 获取借款信息
	 * @param islandOrderId
	 * @return
	 */
	private List<Map <String,Object>> getBorrowingList(String islandOrderId) {
		IslandOrder islandOrder = islandOrderService.getById(Integer.parseInt(islandOrderId));
		ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(
				Context.ProductType.PRODUCT_ISLAND, 19, islandOrder.getId() + "", false, activityIsland.getDeptId().longValue());
		
		List<BorrowingBean> reviewList = getBorrowingBeanList(reviewMapList);
		for (BorrowingBean borr : reviewList) {
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
		List<IslandTraveler> travelerList = islandTravelerService.findTravelerByOrderUuid(islandOrder.getUuid());
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
			sumLendPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getLendPrice(), sumLendPrice, true);
			t4priceMap.put(travelerId, sumLendPrice);
			map.put("borrowingTotal", sumLendPrice);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (borr.getApplyDate() != null) {
				String datestr = sdf.format(borr.getApplyDate());
				map.put("applyDate", datestr);
			}
			map.put("remark", borr.getRemark());
			for (IslandTraveler t : travelerList) {
				if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("spaceLevel", t.getSpaceLevel());
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
	 * @param islandOrderId
	 * @return
	 */
	private List<Map <String,Object>> getRefundList(String islandOrderId) {

		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_ISLAND, 1, islandOrderId, false);
		Collections.reverse(reviewMapList);
		List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
		// 定义订单应收币种集合  取订单达帐金额所有的币种
		IslandOrder islandOrder = islandOrderService.getById(Integer.parseInt(islandOrderId));
		List<IslandTraveler> travelerList = islandTravelerService.findTravelerByOrderUuid(islandOrder.getUuid());
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
			sumRefundPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getRefundPrice(), sumRefundPrice, true);
			t4priceMap.put(travelerId, sumRefundPrice);
			map.put("refundTotal", sumRefundPrice);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (borr.getApplyDate() != null) {
				String datestr = sdf.format(borr.getApplyDate());
				map.put("applyDate", datestr);
			}
			map.put("remark", borr.getRemark());
			map.put("createBy", borr.getCreateBy());
			for (IslandTraveler t : travelerList) {
				if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("spaceLevel", t.getSpaceLevel());
				}
				
			}
			rMap.add(map);
		}
		return rMap;
	}
	/**
	 * 获取改价记录
	 * @param islandOrderId
	 * @return
	 */
	private List<Map <String,Object>> getChangePriceList(String islandOrderId) {

		List<Map<String, Object>> reviewMapList = reviewService.findReviewListMap(Context.ProductType.PRODUCT_ISLAND, 10, islandOrderId, false, "");
		
		// 定义订单应收币种集合  取订单达帐金额所有的币种
		IslandOrder islandOrder = islandOrderService.getById(Integer.parseInt(islandOrderId));
		List<IslandTraveler> travelerList = islandTravelerService.findTravelerByOrderUuid(islandOrder.getUuid());
		List<Map<String,Object>> rMap = Lists.newArrayList();
		for (Map<String, Object> borr : reviewMapList) {
			
			for (IslandTraveler t : travelerList) {
				if (null != borr.get("travelerId") && t.getId().toString().equals(borr.get("travelerId").toString())) {
					borr.put("totalMoney", t.getPayPriceSerialNum());
					borr.put("personType", t.getPersonType());
					borr.put("travelerName", t.getName());
					borr.put("travelerId", t.getId());
					borr.put("spaceLevel", t.getSpaceLevel());
				}
				
			}
			rMap.add(borr);
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
	
	/**
	 * 海岛游产品列表展示   产品&团期
	 * @param activityIslandQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderIslandList")
	public String islandProductList(ActivityIslandQuery activityIslandQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		String orderBy = request.getParameter("orderBy");
		//String status = request.getParameter("status");
		String showType = request.getParameter("showType");// 1是团期列表，2是产品列表	
		Long companyId = UserUtils.getUser().getCompany().getId();
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,response);
		activityIslandQuery.setStatus("1");
		int rowspanNum = 0;
		if ("2".equals(showType)) {
			//根据showType返回列表
			page = activityIslandService.getlandProductList(activityIslandQuery,request, response);
			//获取单个产品下的团期列表
			List<List<Map<String, Object>>> groupList = new ArrayList<List<Map<String, Object>>>();
			//获取单个产品对应的起价列表
			List<List<Map<String, Object>>> lowPriceList = new ArrayList<List<Map<String, Object>>>();
			for(Map<String, Object> map:page.getList()){
				String uuid = (String) map.get("uuid");
				String hotelUuid = (String)map.get("hotel_uuid");
				//String groupUuids = (String) map.get("aigUuids");
				//获取单个产品对应的起价列表
				List<Map<String, Object>> lowpricelist = activityIslandService.getIslandLowPriceList(uuid);
				lowPriceList.add(lowpricelist);
				//获取单个产品下的团期列表
				List<Map<String, Object>> grouplist = activityIslandService.getIslandGroupList(uuid,activityIslandQuery);
				groupList.add(grouplist);
				
				for(Map<String, Object> submap:grouplist){
					rowspanNum = 0;
					String subGroupUuid = (String) submap.get("uuid");
					//房型
					List<ActivityIslandGroupRoom> groupRooms = activityIslandGroupRoomService.getRoomListByGroupUuid(subGroupUuid);
					//基础餐型
					for(ActivityIslandGroupRoom room:groupRooms){
						List<ActivityIslandGroupMeal> groupMeallist = activityIslandGroupMealService.getByactivityIslandGroupUuid(room.getUuid());
						rowspanNum += groupMeallist.size();
						room.setActivityIslandGroupMealList(groupMeallist);
					}
					submap.put("rowspanNum", rowspanNum==0?1:rowspanNum);
					submap.put("groupRoomList", groupRooms);
					//航班
					List<ActivityIslandGroupAirline> groupAirlines=activityIslandGroupAirlineService.getByactivityIslandGroup(subGroupUuid);
					submap.put("groupAirlines", groupAirlines);
					//舱位等级
					Map<String, List<Map<String,Object>>> spaceMap = activityIslandService.getTravelerPriceList(uuid,subGroupUuid,hotelUuid);
					submap.put("spaceMap", spaceMap);
					//余位
					submap.put("remNumber", activityIslandGroupService.getRemNumberByGroupAirlineList(groupAirlines));
				}
			}
			if(CollectionUtils.isNotEmpty(lowPriceList)){
				model.addAttribute("lowPriceList",lowPriceList);
			}
			if(CollectionUtils.isNotEmpty(groupList)){
				model.addAttribute("groupList",groupList);
			}
		}else{
			//根据showType返回列表
			page = activityIslandService.getlandProductList(activityIslandQuery,request, response);
			for(Map<String, Object> map:page.getList()){
				rowspanNum = 0;
				String uuid = (String) map.get("ai_uuid");
				String groupUuid = (String) map.get("uuid");
				String hotelUuid = (String)map.get("hotel_uuid");
				//房型
				List<ActivityIslandGroupRoom> groupRooms = activityIslandGroupRoomService.getRoomListByGroupUuid(groupUuid);
				//基础餐型
				if(CollectionUtils.isNotEmpty(groupRooms)){
					for(ActivityIslandGroupRoom room:groupRooms){
						List<ActivityIslandGroupMeal> groupMeallist = activityIslandGroupMealService.getByactivityIslandGroupUuid(room.getUuid());
						room.setActivityIslandGroupMealList(groupMeallist);
						rowspanNum += groupMeallist.size();
					}
				}
				map.put("rowspanNum", rowspanNum==0?1:rowspanNum);
				map.put("groupRoomList", groupRooms);
				//同行价
				List<ActivityIslandGroupPrice> groupPrices = activityIslandGroupPriceService.getPriceListByGroupUuid(groupUuid);
				map.put("groupPrices", groupPrices);
				//航班信息
				List<ActivityIslandGroupAirline> groupAirlines=activityIslandGroupAirlineService.getByactivityIslandGroup(groupUuid);
				map.put("groupAirlines", groupAirlines);
				//舱位等级
				Map<String, List<Map<String,Object>>> spaceMap = activityIslandService.getTravelerPriceList(uuid,groupUuid,hotelUuid);
				map.put("spaceMap", spaceMap);
				//余位
				map.put("remNumber", activityIslandGroupService.getRemNumberByGroupAirlineList(groupAirlines));
			}
		}
		String pageStr = page.toString();
		pageStr = pageStr.replace("<div style=\"clear:both;\"></div>", "");
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("page", page);
		model.addAttribute("count", page.getCount());
		model.addAttribute("activityIslandQuery", activityIslandQuery);
		//model.addAttribute("status", status);
		model.addAttribute("showType", showType);
		model.addAttribute("orderBy", orderBy);
		//添加币种列表
		List<Currency> curencyList = currencyService.findCurrencyList(companyId);
		model.addAttribute("curencyList", curencyList);
		//添加酒店星级列表
		HotelStar hotelStar = new HotelStar();
		hotelStar.setWholesalerId(companyId.intValue());
		List<HotelStar> hotelStarList = hotelStarService.find(hotelStar);
		model.addAttribute("hotelStarList", hotelStarList);
		
		if ("2".equals(showType)) {// 产品列表	
			return ORDER_ISLAND_PRODUCT_LIST;
		} else {// 团期列表
			return ORDER_ISLAND_GROUP_LIST;
		}
	}
	
	/**
	 * 跳转到海岛游订单详情页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="islandOrderDetail/{orderUuid}")
	public String toIslandOrderDetailPage(@PathVariable(value="orderUuid") String orderUuid,HttpServletRequest request, HttpServletResponse response, Model model){
		
		setIslandOrderDetail(orderUuid,model);

		return  ORDER_ISLAND_ORDER_DETAIL;
	}
	
	/**
	 * 跳转到海岛游订单修改页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toIslandOrderUpdatePage")
	public String toIslandOrderUpdatePage(String orderUuid, boolean isTransfer, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		setIslandOrderDetail(orderUuid,model);
		
		Integer isAllowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();//是否允许添加渠道联系人
		Integer isAllowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();//是否允许修改联系人信息
		model.addAttribute("isAllowAddAgentInfo", isAllowAddAgentInfo);
		model.addAttribute("isAllowModifyAgentInfo", isAllowModifyAgentInfo);
		
		model.addAttribute("isTransfer", isTransfer);
		return  ORDER_ISLAND_ORDER_UPDATE;
	}
	
	/**
	 * 封装海岛游公共数据信息
	 * @param orderId
	 * @param model
	 */
	public void setIslandOrderDetail(String orderUuid,Model model) {
		//获取订单信息
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		String activityIslandGroupUuid = islandOrder.getActivityIslandGroupUuid();
		//加载海岛游团期产品信息
		ActivityIslandGroup activityIslandGroup = activityIslandGroupService.getByUuid(activityIslandGroupUuid);
		//加载海岛游产品信息
		ActivityIsland activityIsland = activityIslandService.getByUuid(activityIslandGroup.getActivityIslandUuid());
		//获取酒店信息
		Hotel hotel = hotelService.getByUuid(activityIsland.getHotelUuid());
		Integer hotelLevel = null;
		if (hotel != null) {
			HotelStar hotelStar = hotelStarService.getByUuid(hotel.getStar());
			if (hotelStar != null) {
				hotelLevel = hotelStar.getValue();
			}
		}
		
		List<IslandMoneyAmount> payedMoneyList = islandMoneyAmountService.getMoneyAmonutBySerialNum(islandOrder.getPayedMoney());
		model.addAttribute("payedMoneyList", payedMoneyList);	//订单已付金额
		
		List<IslandOrderPrice> groupPrices = new ArrayList<IslandOrderPrice>();
		List<IslandOrderPrice> travelerTypeList = new ArrayList<IslandOrderPrice>();
		//获取订单价格列表
		List<IslandOrderPrice> islandOrderPriceList = islandOrderPriceService.getByOrderUuid(orderUuid);
		if(CollectionUtils.isNotEmpty(islandOrderPriceList)){
			for(IslandOrderPrice islandOrderPrice : islandOrderPriceList){
				//获取团期价格
				if(islandOrderPrice != null && "1".equals(islandOrderPrice.getPriceType().toString())){
					int num = islandOrderPrice.getNum() == null ? 0:islandOrderPrice.getNum();	//人数
					Double price = islandOrderPrice.getPrice() == null ? 0 : islandOrderPrice.getPrice();	//价钱
					Double total = price * num;		//小计
					islandOrderPrice.setSubTotal(new BigDecimal(total));	//设置小计金额
					groupPrices.add(islandOrderPrice);
					
					//去重游客类型
					if(travelerTypeList.size() == 0){
						travelerTypeList.add(islandOrderPrice);
					}else{
						int i = 0; 
						int size = travelerTypeList.size();
						for(i = 0 ;i< size;i++){
							IslandOrderPrice orderPrice = travelerTypeList.get(i);
							if(StringUtils.isNotEmpty(orderPrice.getTravelerType())){
								if(orderPrice.getTravelerType().equals(islandOrderPrice.getTravelerType())){
									break;
								}
								if(i == size -1){
									travelerTypeList.add(islandOrderPrice);
								}
							}
						}
					}
					
				}
			}
		}
		//加载团期下所有的参考航班数据
		List<ActivityIslandGroupAirline> groupAirlines = activityIslandGroupAirlineService.getByactivityIslandGroup(activityIslandGroupUuid);
		//取出航班表信息，为前台获取舱位等级对应的余位提供数据
		for (IslandOrderPrice islandOrderPrice : groupPrices) {
			for (ActivityIslandGroupAirline activityIslandGroupAirline : groupAirlines) {
				if (islandOrderPrice.getSpaceLevel() != null){
					if (islandOrderPrice.getSpaceLevel().equals(activityIslandGroupAirline.getSpaceLevel()) || (StringUtils.isBlank(islandOrderPrice.getSpaceLevel()) && StringUtils.isBlank(activityIslandGroupAirline.getSpaceLevel()))) {
						islandOrderPrice.setActivityIslandGroupAirline(activityIslandGroupAirline);
					}
				}
			}
		}
		//加载渠道信息
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();// 渠道信息列表
		//签证类型
		List<SysDict> visaTypes = sysDictService.findByType(SysDict.TYPE_NEW_VISA_TYPE);

		//舱位等级列表(去重操作)
		List<ActivityIslandGroupAirline> spaceLevelList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(groupAirlines)){
			for(ActivityIslandGroupAirline groupAirline : groupAirlines  ){
				if(!spaceLevelList.contains(groupAirline)){
					spaceLevelList.add(groupAirline);
				}
			}
		}
		
		//初始化团期余位数
		activityIslandGroup.setRemNumber(activityIslandGroupService.getRemNumberByGroupAirlineList(groupAirlines));
		activityIslandGroup.setOrderNum(islandOrderService.getBookingPersonNum(activityIslandGroup.getUuid()));
		
		//加载团期下所有的房型数据
		List<ActivityIslandGroupRoom> groupRooms = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityIslandGroupUuid);
		
		//预订人联系人信息
		List<OrderContacts> orderContactsList = orderContactsService.findOrderContactsByOrderIdAndOrderType((long)islandOrder.getId(), Context.ORDER_TYPE_ISLAND.intValue());

		//非签约渠道的联系人信息
		List<OrderContacts> tempContacts = Lists.newArrayList();
		tempContacts.add(new OrderContacts());
		Agentinfo defaultFirstAgentinfo = agentinfoService.findAgentInfoById(Long.parseLong(islandOrder.getOrderCompany().toString()));
		List<OrderContacts> orderContactsListNon = defaultFirstAgentinfo.getId() != -1 ? tempContacts : orderContactsList;
		model.addAttribute("orderContactsListNon", orderContactsListNon);    //预订人联系人信息
		
		//酒店控房详情数据
		List<HotelControlDetailModel> hotelControlDetailList = hotelControlService.getControlDetailsByHotelUuid(activityIsland.getHotelUuid());
		
		//查询游客信息列表
		IslandTravelerQuery islandTravelerQuery = new IslandTravelerQuery();
		islandTravelerQuery.setOrderUuid(islandOrder.getUuid());
		islandTravelerQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		islandTravelerQuery.setStatus("0");
		List<IslandTraveler> travelerList = islandTravelerService.find(islandTravelerQuery);
		if(travelerList != null && travelerList.size() > 0){
			//海岛游游客签证信息查询条件
			IslandTravelervisaQuery islandTravelervisaQuery = new IslandTravelervisaQuery();	
			islandTravelervisaQuery.setIslandOrderUuid(orderUuid);
			//海岛游游客证件类型查询条件
			IslandTravelerPapersTypeQuery islandTravelerPapersTypeQuery = new  IslandTravelerPapersTypeQuery();
			islandTravelerPapersTypeQuery.setOrderUuid(orderUuid);
			//附件信息查询条件
			HotelAnnex hotelAnnex = new HotelAnnex();
			//游客金额信息查询条件
			IslandMoneyAmountQuery islandMoneyAmountQuery  = new IslandMoneyAmountQuery();
			for(IslandTraveler traveler: travelerList ){
				islandTravelervisaQuery.setIslandTravelerUuid(traveler.getUuid());
				islandTravelervisaQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				//游客签证信息查询
				traveler.setIslandTravelervisaList(islandTravelervisaService.find(islandTravelervisaQuery));
				//证件类型
				islandTravelerPapersTypeQuery.setIslandTravelerUuid(traveler.getUuid());
				islandTravelerPapersTypeQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				List<IslandTravelerPapersType> islandTravelerPapersTypes = islandTravelerPapersTypeService.find(islandTravelerPapersTypeQuery);
				//证件信息为空的时候，添加一个null的对象，配合前台展示
				if (CollectionUtils.isEmpty(islandTravelerPapersTypes)) {
					IslandTravelerPapersType islandTravelerPapersType = new IslandTravelerPapersType();
					islandTravelerPapersTypes.add(islandTravelerPapersType);
				}
				traveler.setIslandTravelerPapersTypeList(islandTravelerPapersTypes);
				//附件信息
				hotelAnnex.setMainUuid(traveler.getUuid());
				hotelAnnex.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				traveler.setIslandTravelerFilesList(hotelAnnexService.find(hotelAnnex));
				//游客金额信息
				islandMoneyAmountQuery.setBusinessUuid(traveler.getUuid());
				islandMoneyAmountQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				islandMoneyAmountQuery.setMoneyType(Context.MONEY_TYPE_JSJ);
				traveler.setIslandMoneyAmountList(islandMoneyAmountService.find(islandMoneyAmountQuery));
			}
		}
		
		//获取订单总额(成本价)
		String costMoneyStr =  islandMoneyAmountService.getMoneyStr(islandOrder.getCostMoney(), true);
		//获取订单结算总额(结算价,应收金额)
		String totalMoneyStr = islandMoneyAmountService.getMoneyStr(islandOrder.getTotalMoney(), true);
		//获取订单已付金额(已付金额)
		String payedMoneyStr = islandMoneyAmountService.getMoneyStr(islandOrder.getPayedMoney(), true);
		//订单未收金额( totalMoneyStr - payedMoneyStr  )
		String noPayMoneyStr = "";
		if(!StringUtils.isEmpty(totalMoneyStr) && !StringUtils.isEmpty(payedMoneyStr) ){
			 noPayMoneyStr =  islandMoneyAmountService.addOrSubtract(totalMoneyStr, payedMoneyStr, false);
		}
		
		//签证国家
		List<SysGeography> sysGeographyList = sysGeographyService.getAllCountryName();
		model.addAttribute("sysGeographyList", sysGeographyList);
		
		if(groupAirlines != null && groupAirlines.size() > 0) {
			//加载所有余位数
			ActivityIslandGroupAirline activityIslandGroupAirline = groupAirlines.get(0);
			model.addAttribute("groupAirline", activityIslandGroupAirline);
			//查询航班
			List<AirlineInfo> airlineInfoList = airlineInfoDao.findByCompanyIdAndAirlineCodeAndFiightNumber(UserUtils.getUser().getCompany().getId(), 
					activityIslandGroupAirline.getAirline(), activityIslandGroupAirline.getFlightNumber());
			if (CollectionUtils.isNotEmpty(airlineInfoList)) {
				model.addAttribute("airlineInfo", airlineInfoList.get(0));
			}
		}
		
		//读取公司配置币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("islandOrder", islandOrder);
		model.addAttribute("agentList", agentInfos);
		model.addAttribute("groupPrices", groupPrices);
		model.addAttribute("activityIslandGroup", activityIslandGroup);
		model.addAttribute("activityIsland", activityIsland);
		model.addAttribute("hotelLevel", hotelLevel);
		model.addAttribute("visaTypes", visaTypes);
		model.addAttribute("groupRooms", groupRooms);
		model.addAttribute("hotelStar", hotelService.getHotelStarValByHotelUuid(activityIsland.getHotelUuid()));
		model.addAttribute("orderContactsList", orderContactsList);
		model.addAttribute("islandOrderPriceList", islandOrderPriceList);
		model.addAttribute("hotelControlDetailList", hotelControlDetailList);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("spaceLevelList", spaceLevelList);	//舱位等级列表(所有的)
		List<ActivityIslandGroupAirline> usedSpaceLevelList = Lists.newArrayList();
		for (int i = 0; i < groupPrices.size(); i++) {
			String spaceLevel = groupPrices.get(i).getSpaceLevel();
			for (ActivityIslandGroupAirline activityIslandGroupAirline : spaceLevelList) {
				String templevel = activityIslandGroupAirline.getSpaceLevel();
				if ((templevel == null?"":templevel).equals(spaceLevel)) {					
					if (!usedSpaceLevelList.contains(spaceLevel)) {
						usedSpaceLevelList.add(activityIslandGroupAirline);
					}
				}
			}
		}
		model.addAttribute("usedSpaceLevelList", usedSpaceLevelList);	//舱位等级列表(使用的)
		model.addAttribute("travelerTypeList", travelerTypeList);//游客类型列表
		
		model.addAttribute("costMoneyStr", handlerCurrencyStr(costMoneyStr));	//订单总额(成本价)
		model.addAttribute("totalMoneyStr", handlerCurrencyStr(totalMoneyStr));	//订单结算总额
		model.addAttribute("payedMoneyStr", handlerCurrencyStr(payedMoneyStr)); //订单已付金额
		model.addAttribute("noPayMoneyStr", handlerCurrencyStr(noPayMoneyStr)); //订单未收金额
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
	 * 修改海岛游订单
	 * @param datas
	 * @throws Exception 
	 */
	@RequestMapping(value="updateIslandOrder")
	@ResponseBody
	public Object updateIslandOrder(@RequestBody String jsonData) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("code","0");	//返回码,默认失败
		resultMap.put("message","false");//返回消息
		try {
			int res = islandOrderService.updateIslandOrder(jsonData);
			if (res == 1) {				
				resultMap.put("code", "1");
				resultMap.put("message","success");//返回消息
			}
		} catch (Exception e) {
			resultMap.put("message","");//返回消息
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 删除订单联系人
	 * @param orderContactsId
	 */
	@RequestMapping(value = "deleteOrderContacts")
	public void deleteOrderContacts(String orderContactsId) {
		if(StringUtils.isNotEmpty(orderContactsId)){
			orderContactsService.deleteOrderContactsById(Long.valueOf(orderContactsId));
		}
	}
	
}
