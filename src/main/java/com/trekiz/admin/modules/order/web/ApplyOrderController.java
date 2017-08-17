package com.trekiz.admin.modules.order.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.PreProductOrderCommon;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.ApplyOrderCommonService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.stock.service.StockService;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/applyOrderCommon/manage")
public class ApplyOrderController {
	protected static final Logger logger = LoggerFactory.getLogger(ApplyOrderController.class);
	@Autowired
    private DepartmentService departmentService;
	@Autowired
    private AgentinfoService agentinfoService;
	@Autowired
	private OrderCommonService orderCommonService; 
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	@Autowired
	private ApplyOrderCommonService applyOrderCommonService;
    @Autowired
    private OrderCommonService orderService;
    @Autowired
    SysIncreaseService sysIncreaseService;
    @Autowired
    StockService StockService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ActivityGroupService activityGroupService;
    @Autowired
    private MoneyAmountService moneyAmountService;
	
    /**
	 * 预报名列表
	 * @param showType
	 * @param orderStatus
	 * @param travelActivity
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="showApplyOrderList/{showType}/{orderStatus}")
	public String showApplyOrderList(@PathVariable String showType, @PathVariable String orderStatus, 
	        @ModelAttribute TravelActivity travelActivity, HttpServletResponse response,
	        Model model, HttpServletRequest request) throws Exception {
		
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("order", model);
		//查询条件
        Map<String,String> mapRequest = new HashMap<String,String>();
		//查询条件
        String agentId = request.getParameter("agentId");//获取渠道id
        String orderNumOrGroupCode = request.getParameter("orderNumOrGroupCode");
        //解决下完订单后不能跳到相应区域问题
        if(StringUtils.isNotBlank(orderNumOrGroupCode)) {
        	List<ProductOrderCommon> list = orderCommonService.findByOrderNum(orderNumOrGroupCode);
        	if(CollectionUtils.isNotEmpty(list) && list.size() == 1) {
        		Long productId = list.get(0).getProductId();
        		User tempUser = travelActivityService.findById(productId).getCreateBy();
        		if(tempUser != null) {
        			List<Role> roleList = tempUser.getRoleList();
        			if(CollectionUtils.isNotEmpty(roleList)) {
        				for(Role role : roleList) {
        					if(role.getDepartment() != null && (Context.ROLE_TYPE_OP.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES.equals(role.getRoleType()))) {
        						common.setDepartmentId(role.getDepartment().getId().toString());
        						model.addAttribute("departmentId", common.getDepartmentId());
        						break;
        					}
        				}
        			}
        		}
        	}
        }
        String wholeSalerKey = request.getParameter("wholeSalerKey");
        String groupCode= request.getParameter("groupCode");
        if(groupCode!=null) groupCode = groupCode.trim();
        String orderPersonName= request.getParameter("orderPersonName");
        String orderShowType = request.getParameter("orderShowType");
        String orderBy = request.getParameter("orderBy");
        String saler = request.getParameter("saler");
        
        mapRequest.put("payCreateDateBegin", request.getParameter("payCreateDateBegin"));
        mapRequest.put("payCreateDateEnd", request.getParameter("payCreateDateEnd"));
        mapRequest.put("groupOpenDateBegin", request.getParameter("groupOpenDateBegin"));
        mapRequest.put("groupOpenDateEnd", request.getParameter("groupOpenDateEnd"));
        mapRequest.put("groupCloseDateBegin", request.getParameter("groupCloseDateBegin"));
        mapRequest.put("groupCloseDateEnd", request.getParameter("groupCloseDateEnd"));
        mapRequest.put("orderTimeBegin",request.getParameter("orderTimeBegin"));
        mapRequest.put("orderTimeEnd",request.getParameter("orderTimeEnd"));
        mapRequest.put("groupOpenDateBegin", request.getParameter("groupOpenDateBegin"));
        mapRequest.put("groupOpenDateEnd", request.getParameter("groupOpenDateEnd"));
        
        if(StringUtils.isNotEmpty(saler)){
        	mapRequest.put("saler", saler);
        }
        
        //排序方式
        if(StringUtils.isBlank(orderBy)){
            orderBy = "pro.id DESC";
        }
        
        if(StringUtils.isNotBlank(orderShowType) && 
        		!StringUtils.equalsIgnoreCase("199", showType) && 
        		!StringUtils.equalsIgnoreCase("101", showType)) {
            showType = orderShowType;
        }
        
        travelActivity.setAcitivityName(wholeSalerKey);
        
        Page<Map<Object, Object>> pageOrder = orderCommonService.findPreOrderListByPayType(orderStatus, new Page<Map<Object, Object>>(request, response),travelActivity,orderNumOrGroupCode,agentId, orderPersonName, orderBy, orderShowType, mapRequest, common);
        model.addAttribute("page", pageOrder);
        List<Map<Object, Object>> listorder = pageOrder.getList();

        for(Map<Object, Object> listin : listorder){
        	if(listin.get("createBy") != null) {
        		listin.put("carateUserName", UserUtils.getUser(StringUtils.toLong(listin.get("createBy"))).getName());
        	}
        	if(listin.get("activityCreateBy") != null) {
        		listin.put("activityCreateUserName", UserUtils.getUser(StringUtils.toLong(listin.get("activityCreateBy"))).getName());
        	}
        }
        String userType = UserUtils.getUser().getUserType();


        model.addAttribute("agentId", agentId);
        model.addAttribute("userType", userType);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
        model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
        model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
        model.addAttribute("orderNum", orderNumOrGroupCode);
        model.addAttribute("payTypes", DictUtils.getDicMap(Context.PAY_TYPE));
        model.addAttribute("showType",showType);
        model.addAttribute("orderShowType",orderShowType);
        model.addAttribute("groupCode",groupCode);
        model.addAttribute("orderPersonName",orderPersonName);
        model.addAttribute("payCreateDateBegin",request.getParameter("payCreateDateBegin"));
        model.addAttribute("payCreateDateEnd",request.getParameter("payCreateDateEnd"));
        model.addAttribute("groupOpenDateBegin",request.getParameter("groupOpenDateBegin"));
        model.addAttribute("groupOpenDateEnd",request.getParameter("groupOpenDateEnd"));
        model.addAttribute("groupCloseDateBegin",request.getParameter("groupCloseDateBegin"));
        model.addAttribute("groupCloseDateEnd",request.getParameter("groupCloseDateEnd"));
        model.addAttribute("orderTimeBegin",request.getParameter("orderTimeBegin"));
        model.addAttribute("orderTimeEnd",request.getParameter("orderTimeEnd"));
        model.addAttribute("orderOrGroup", "order");
        model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(orderStatus));
        model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
        
        return "modules/order/list/orderList";
	}
    
	/**
	 * 
	 *  功能:预报名保存第一步
	 *	保存信息仅包括，订单基本信息及预定联系人
	 *	游客信息和特殊需求在第二部保存
	 *  @DateTime 2014-11-11 下午3:18:22
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value = "appFirstSave",method = RequestMethod.POST)
	public Object appFirstSave(
			Model model, 
			HttpServletResponse response,
	        HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		Map<String, Object> result = new  HashMap<String, Object>();
	    String orderPersonNumChilds = request.getParameter("orderPersonNumChild");
	    String orderPersonNumAdults = request.getParameter("orderPersonNumAdult");
	    String orderPersonNumSpecials = request.getParameter("orderPersonNumSpecial");
	    String productOrderIds = request.getParameter("productOrderId");
	    String productId = request.getParameter("productId");
	    String productGroupId = request.getParameter("productGroupId");
	    String salerId = request.getParameter("salerId");
	    
	    PreProductOrderCommon productOrder = new PreProductOrderCommon();
	    Long productOrderId = StringUtils.isNotBlank(productOrderIds) ? Long.parseLong(productOrderIds) : null;
	    if(productOrderId != null){
	    	productOrder = applyOrderCommonService.findOne(productOrderId);
	    }else{
        	Long orderCompany = Long.parseLong(request.getParameter("orderCompany"));
        	productOrder.setOrderCompanyName(request.getParameter("orderCompanyName"));
            productOrder.setOrderCompany(orderCompany);
            if(orderCompany <= 0){
            	productOrder.setOrderSaler(UserUtils.getUser());
            }else{
            	// 由于此段代码过于久远未知其业务逻辑。此处渠道销售表结构发生变动，暂取第一销售人为订单销售 TODO 
            	Agentinfo agent = agentinfoService.loadAgentInfoById(orderCompany);
            	List<User> salersList = Lists.newArrayList();
            	if (agent != null && StringUtils.isNotBlank(agent.getAgentSalerId())) {
            		salersList = UserUtils.getUserListByIds(agent.getAgentSalerId());
            		agent.setAgentSalerUser(salersList);
				}
            	if (CollectionUtils.isNotEmpty(salersList)) {					
            		productOrder.setOrderSaler(salersList.get(0));
				}
            }
	        productOrder.setProductId(Long.parseLong(productId));
	        productOrder.setProductGroupId(Long.parseLong(productGroupId));
		    productOrder.setOrderStatus(Integer.parseInt(request.getParameter("activityKind")));
		    TravelActivity activity = travelActivityService.findById(Long.parseLong(productId));
		    String companyName = officeService.get(activity.getProCompany()).getName();
            String orderNum = sysIncreaseService.updateSysIncrease(companyName.length() > 3 ? companyName.substring(0, 3) : companyName, activity.getProCompany(),null, Context.ORDER_NUM_TYPE);
            productOrder.setOrderNum(orderNum);
            productOrder.setOrderTime(new Date());
            if(StringUtils.isNotBlank(salerId)){
            	productOrder.setSalerId(Integer.parseInt(salerId));
            	productOrder.setSalerName(UserUtils.getUserNameById(Long.parseLong(salerId)));
            }
	    }
	    
	    if(StringUtils.isNotBlank(orderPersonNumChilds)){
	        productOrder.setOrderPersonNumChild(Integer.parseInt(orderPersonNumChilds));
	    }
	    if(StringUtils.isNotBlank(orderPersonNumAdults)){
	        productOrder.setOrderPersonNumAdult(Integer.parseInt(orderPersonNumAdults));
	    }
	    if(StringUtils.isNotBlank(orderPersonNumSpecials)){
	    	productOrder.setOrderPersonNumSpecial(Integer.parseInt(orderPersonNumSpecials));
	    }
	    List<OrderContacts> orderCotacts = OrderUtil.getContactsList(request.getParameter("orderContactsJSON"));
        try {
        	result = applyOrderCommonService.saveApplyOrder(productOrder, orderCotacts, Integer.parseInt(request.getParameter("orderPersonNum")));
			String errorMsg =(String)result.get("errorMsg");
			result.put("errorMsg", errorMsg);
		} catch (NumberFormatException e) {
			logger.error("数据格式错误", e);
			throw new NumberFormatException("数据格式错误，请重试。");
		} catch (OptimisticLockHandleException e) {
			logger.error("存入团期内容失败", e);
			throw new OptimisticLockHandleException("存入团期内容失败，请重试。");
		}
	    return result;
	}
	
	/**
	 * 
	 *  功能:预报名最后一步保存 
	 *  保存订单的特殊需求
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-11-11 下午4:18:22
	 *  @param model
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value = "appLastSave")
	public Object appLastSave(Model model,HttpServletRequest request){
		Map<String, Object> result = new  HashMap<String, Object>();
		String productOrderId = request.getParameter("productOrderId");
		PreProductOrderCommon productOrder = orderService.getPreProductorderById(Long.parseLong(productOrderId));
		String currencyId = request.getParameter("currencyId");
		String currencyPrice = request.getParameter("currenctPrice");
		String[] currencyIdArr = currencyId.split(",");
		String[] currencyPrcieArr = currencyPrice.split(",");
		Map<String,BigDecimal> totalMoneyMap = new HashMap<String, BigDecimal>();
		for(int i = 0; i < currencyIdArr.length; i++){
			totalMoneyMap.put(currencyIdArr[i], new BigDecimal(currencyPrcieArr[i]));
		}
		String specialDemand = request.getParameter("specialDemand");
		productOrder.setSpecialDemand(specialDemand);
		productOrder = applyOrderCommonService.saveOrder(productOrder, totalMoneyMap, null);
		result.put("productOrder", productOrder);
		return result;
	}

	/**
	 * 修改预报名订单状态 取消、恢复预报名订单
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "changeOrderType")
	public Object changeOrderType(Model model, HttpServletRequest request) {
		Map<String, Object> result = new  HashMap<String, Object>();
		try{
			applyOrderCommonService.updateOrderTypeById(Integer.parseInt(request.getParameter("orderType")), Long.parseLong(request.getParameter("orderId")));
			result.put("sucess", true);
		}
		catch(Exception e){
			result.put("error", e.getMessage());
			result.put("sucess", false);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 预报名转正式订单
	 * @param model
	 * @param request
	 * @return
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "applyToOrder")
	public Object saveApplyToOrderManage(Model model,HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		String productOrderId = request.getParameter("productOrderId");
		PreProductOrderCommon preProductOrder = orderService.getPreProductorderById(Long.parseLong(productOrderId));
		ProductOrderCommon productOrder = applyEntityToOrderEntity(preProductOrder);
		String payMode = request.getParameter("payMode");
		productOrder.setPayMode(payMode);
		String payStatus = request.getParameter("payStatus");
		productOrder.setPayStatus(Integer.parseInt(payStatus));
		String placeHolderType = request.getParameter("placeHolderType");
		productOrder.setPlaceHolderType(Integer.parseInt(placeHolderType));
		String specialDemand = request.getParameter("specialDemand");
		productOrder.setSpecialDemand(specialDemand);
		return applyOrderCommonService.saveApplyToOrder(Long.parseLong(productOrderId), productOrder, request);
	}
	
	/**
	 * 
	 *  功能:获取占位余位信息
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-10-31 下午6:18:22
	 *  @param model
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value="getFreePosition")
	public Object getFreePosition(Model model,HttpServletRequest request){
		Map<String, Object> result = new  HashMap<String, Object>();
		String productGroupId = request.getParameter("productGroupId");
		ActivityGroup activityGroup = activityGroupService.findById(Long.parseLong(productGroupId));
		if(activityGroup != null){
			result.put("leftNum", activityGroup.getFreePosition());
		}else{
			result.put("leftNum", 0);
		}
		return result;
	}

	private ProductOrderCommon applyEntityToOrderEntity(PreProductOrderCommon preProductOrder){
		ProductOrderCommon productOrder = new ProductOrderCommon();
		productOrder.setProductId(preProductOrder.getProductId());
		productOrder.setProductGroupId(preProductOrder.getProductGroupId());
		productOrder.setOrderCompany(preProductOrder.getOrderCompany());
		productOrder.setOrderCompanyName(preProductOrder.getOrderCompanyName());
		productOrder.setOrderSaler(preProductOrder.getOrderSaler());
		productOrder.setPlaceHolderType(preProductOrder.getPlaceHolderType());
		productOrder.setSettlementAdultPrice(preProductOrder.getSettlementAdultPrice());
		productOrder.setSettlementcChildPrice(preProductOrder.getSettlementcChildPrice());
		productOrder.setSettlementSpecialPrice(preProductOrder.getSettlementSpecialPrice());
		productOrder.setSingleDiff(preProductOrder.getSingleDiff());
		productOrder.setPayDeposit(preProductOrder.getPayDeposit());
		productOrder.setFrontMoney(preProductOrder.getFrontMoney());
		productOrder.setTotalMoney(preProductOrder.getTotalMoney());
		productOrder.setOriginalTotalMoney(preProductOrder.getOriginalTotalMoney());
		productOrder.setAccountedMoney(preProductOrder.getAccountedMoney());
		productOrder.setPayedMoney(preProductOrder.getPayedMoney());
        productOrder.setRemainDays(preProductOrder.getRemainDays());
	    productOrder.setOrderStatus(2);
	    TravelActivity activity = travelActivityService.findById(preProductOrder.getProductId());
	    String companyName = officeService.get(activity.getProCompany()).getName();
        String orderNum = sysIncreaseService.updateSysIncrease(companyName.length() > 3 ? companyName.substring(0, 3) : companyName, activity.getProCompany(),null, Context.ORDER_NUM_TYPE);
        applyOrderCommonService.savePreProductorderOnReserve(preProductOrder, activity, null);
        productOrder.setOrderNum(orderNum);
        productOrder.setOrderTime(new Date());
        productOrder.setOrderPersonNumChild(preProductOrder.getOrderPersonNumChild());
        productOrder.setOrderPersonNumAdult(preProductOrder.getOrderPersonNumAdult());
    	productOrder.setOrderPersonNumSpecial(preProductOrder.getOrderPersonNumSpecial());
        productOrder.setOrderPersonNum(preProductOrder.getOrderPersonNum());
		return productOrder;
	}

}
