package com.trekiz.admin.modules.order.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.finance.service.IServiceChargeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.cost.service.PayManagerService;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.receipt.service.OrderReceiptService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.form.VisaOrderForm;
import com.trekiz.admin.modules.visa.service.VisaOrderService;

 /**
  * 订单统计
  * @author yakun.bai
  *
  */
@Controller
@RequestMapping(value = "${adminPath}/orderStatistics/manage")
public class OrderStatisticsController extends BaseController {
    
    protected static final Logger logger = LoggerFactory.getLogger(OrderStatisticsController.class);
    
    @Autowired
    private OrderStatisticsService orderStatisticsService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private PayManagerService payManagerService;
    @Autowired
    private CostManageService costManageService;
    @Autowired
    private OrderReceiptService orderReceiptService;
    @Autowired
    private OrderinvoiceService orderInvoiceService;
    @Autowired
    private OrderCommonService orderService;
    @Autowired
    private VisaOrderService visaOrderService;
    @Autowired
	private IServiceChargeService serviceChargeService;
    
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 80;
	}
	
	/**
	 * 订单统计
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="orderStatistics/{orderStatus}")
	public String orderStatistics(@PathVariable String orderStatus, Model model, HttpServletRequest request) {
		
		//按部门展示
		User user = UserUtils.getUser();
		DepartmentCommon common = departmentService.setDepartmentPara("orderStatistics", model);
		
		//查询条件
		String salerId = request.getParameter("salerId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String dateFlag = request.getParameter("dateFlag");//标识是否是页面主动查询
        Map<String,String> mapRequest = new HashMap<String,String>();
        if(StringUtils.isNotBlank(startDate)) {
        	startDate += " 00:00:00";
        } else {
        	//如果不是页面主动查询，则默认开始时间为当前日期
        	if(StringUtils.isBlank(dateFlag)) {
        		startDate = DateUtils.getDate() + " 00:00:00";
        	}
        }
        if(StringUtils.isNotBlank(endDate)) {
        	endDate += " 23:59:59";
        } else {
        	//如果不是页面主动查询，则默认开始时间为当前日期
        	if(StringUtils.isBlank(dateFlag)) {
        		endDate = DateUtils.getDate() + " 23:59:59";
        	}
        }
        mapRequest.put("orderStatus", orderStatus);
        mapRequest.put("startDate", startDate);
        mapRequest.put("endDate", endDate);
        mapRequest.put("salerId", salerId);
        
        //查询此部门下所有销售
        List<User> salerList = Lists.newArrayList();
        salerList = getSalerUsers(user.getCompany().getId(), common);
        
        //订单查询
        Map<String, List<Object[]>> orderMap = new LinkedHashMap<String, List<Object[]>>();
		List<Object[]> orderSum = orderStatisticsService.orderStatistics(mapRequest, common, salerList);
		
		//查看当前用户所在部门及其子部门
		Set<Department> deptList = common.getShowAreaList();
		if(CollectionUtils.isNotEmpty(deptList) && StringUtils.isNotBlank(common.getDepartmentId())) {
			Department parentDept = departmentService.findById(Long.parseLong(common.getDepartmentId()));
			orderMap.put(parentDept.getName(), orderSum);
			if(CollectionUtils.isNotEmpty(parentDept.getChildList())) {
				for(Department dept : parentDept.getChildList()) {
					orderMap.put(dept.getName(), orderSum);
				}
			}
		} else {
			if(StringUtils.isNotBlank(common.getDepartmentId())) {
				Department dept = departmentService.findById(Long.parseLong(common.getDepartmentId()));
				orderMap.put(dept.getName(), orderSum);
			} else {
				orderMap.put("数据统计", orderSum);
			}
		}

		model.addAttribute("isParentsAndChildren", common.getIsParentsAndChildren());
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("orderType", OrderCommonUtil.getChineseOrderType(orderStatus));
		model.addAttribute("salerList", salerList);
		model.addAttribute("orderMap", orderMap);
        model.addAttribute("startDate", StringUtils.isNotBlank(startDate) ? startDate.substring(0, 10) : "");
        model.addAttribute("endDate", StringUtils.isNotBlank(endDate) ? endDate.substring(0, 10) : "");
        model.addAttribute("salerId", salerId);
        
        /*
         * 订单 > 单团订单 > 订单统计，当点击订单统计时，显示导航菜单。开始
         * 以下代码原先没有，后来，为了能点击订单统计时，显示导航菜单而添加
         */
        model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(orderStatus));
        //订单或团期列表标识：order为订单、group为团期，默认查询订单列表
        String orderOrGroup = request.getParameter("orderOrGroup");
        if(StringUtils.isBlank(orderOrGroup)) {
        	orderOrGroup = "order";
        }
        model.addAttribute("orderOrGroup",orderOrGroup);
        /*
         * 订单 > 单团订单 > 订单统计，当点击订单统计时，显示导航菜单。结束
         */
	        
	    return "modules/order/orderStatistics";
	}
	
	/**
	 * Ajax获取部门下销售人员
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "getSalerByDept")
	public String getSalerByDept(HttpServletRequest request) throws IOException {
		String departmentId = request.getParameter("departmentId");
		String isParentsAndChildren = request.getParameter("isParentsAndChildren");
		DepartmentCommon common = new DepartmentCommon();
		common.setDepartmentId(departmentId);
		common.setIsParentsAndChildren(Boolean.parseBoolean(isParentsAndChildren));
		List<User> userList = getSalerUsers(UserUtils.getUser().getCompany().getId(), common);
		if(CollectionUtils.isNotEmpty(userList)) {
			JSONArray array = new JSONArray();
			for(User user : userList) {
				JSONObject userJson = new JSONObject();
				userJson.put("salerId", user.getId());
				userJson.put("salerName", user.getName());
				array.add(userJson);
			}
			return array.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * 获取当前用户所在部门下所有子部门销售用户
	 * @param companyId
	 * @param departmentId
	 * @return
	 */
	private List<User> getSalerUsers(Long companyId, DepartmentCommon common) {
		String departmentId = common.getDepartmentId();
		List<User> userList = Lists.newArrayList();
		List<Role> roleList = UserUtils.getUser().getRoleList();
		//获取当前用户所在部门下所有人员包括自己部门下人员
		if(StringUtils.isNotBlank(departmentId)) {
			for(Role role : roleList) {
	        	String type = role.getRoleType();
	        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
	        		if(common.getIsParentsAndChildren() || 
	        				(role.getDepartment() != null && role.getDepartment().getId().toString().equals(departmentId))) {
	        			userList = systemService.getAllUserByDepartment(companyId, Long.parseLong(departmentId), "%," + departmentId + ",%");
	        			break;
	        		} 
	        	} else {
	        		boolean isContains = false;
	        		//因为一个销售用户可能同属于两个部门
	        		if(CollectionUtils.isNotEmpty(userList)) {
	        			for(User user : userList) {
	        				if(user.getId() == UserUtils.getUser().getId()) {
	        					isContains = true;
	        					break;
	        				}
	        			}
	        		}
	        		if(!isContains) {
	        			userList.add(UserUtils.getUser());
	        		}
	        	}
			}
		} else {
			//获取部门下销售人员：自己部门和子级部门销售
	    	for(Role role : roleList) {
	        	String type = role.getRoleType();
	        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
	        		userList = systemService.getUserByCompany(UserUtils.getUser().getCompany());
	        		break;
	        	} else {
	        		boolean isContains = false;
	        		//因为一个销售用户可能同属于两个部门
	        		if(CollectionUtils.isNotEmpty(userList)) {
	        			for(User user : userList) {
	        				if(user.getId() == UserUtils.getUser().getId()) {
	        					isContains = true;
	        					break;
	        				}
	        			}
	        		}
	        		if(!isContains) {
	        			userList.add(UserUtils.getUser());
	        		}
	        	}
			}
		}
		//获取部门下销售人员：自己部门和子级部门销售
		if(CollectionUtils.isNotEmpty(userList)) {
			Iterator<User> it = userList.iterator();
			while(it.hasNext()) {
				User user = it.next();
				List<Role> roleLists = user.getRoleList();
				boolean isSaler = false;
				for(Role role : roleLists) {
					if(Context.ROLE_TYPE_SALES.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
						isSaler = true;
						break;
					}
				}
				if(!isSaler) {
					it.remove();
				}
			}
		} 
		return userList;
	}
	
	
	/**
	 * 获取单团没有查看订单数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "getDTNotSeenOrderNum")
	public String getDTNotSeenOrderNum(HttpServletRequest request) {
		String compnayUuid = UserUtils.getUser().getCompany().getUuid();
		// 如果是日信观光用户，则使用定制规则：但凡能看到订单的用户，都会收到提醒
		if (Context.SUPPLIER_UUID_RXGG.equals(compnayUuid)) {
			return orderStatisticsService.getRXGG_DTNotSeenOrderNum();
		} else {
			// 如果是计调主管，则能查看对应计调发布产品产生订单
			if (UserUtils.isOpManager()) {
				return orderStatisticsService.getRXGG_DTNotSeenOrderNum();
			} else {
				return orderStatisticsService.getDTNotSeenOrderNum();
			}
		}
	}
	
	/**
	 * 获取机票没有查看订单数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "getJPNotSeenOrderNum")
	public String getJPNotSeenOrderNum(HttpServletRequest request) {
		String compnayUuid = UserUtils.getUser().getCompany().getUuid();
		// 如果是日信观光用户，则使用定制规则：但凡能看到订单的用户，都会收到提醒
		if (Context.SUPPLIER_UUID_RXGG.equals(compnayUuid)) {
			return orderStatisticsService.getRXGG_JPNotSeenOrderNum();
		} else {
			// 如果是计调主管，则能查看对应计调发布产品产生订单
			if (UserUtils.isOpManager()) {
				return orderStatisticsService.getRXGG_JPNotSeenOrderNum();
			} else {
				return orderStatisticsService.getJPNotSeenOrderNum();
			}
		}
		
	}
	
	/**
	 * 获取签证没有查看订单数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "getQZNotSeenOrderNum")
	public String getQZNotSeenOrderNum(HttpServletRequest request) {
		
		if (UserUtils.getUser().getId() == 1) {
			return "0" + ";" + "0";
		}
		
		DepartmentCommon common = departmentService.setDepartmentPara();
		common.setPermission("visaOrderForOp:list:allorder");
		StringBuffer qianWuSql = new StringBuffer("");
		visaOrderService.getMainSql(null, qianWuSql, new VisaOrderForm());
		StringBuffer whereSql = visaOrderService.addWhereSql(null, common, null, "qianwu", null);
		qianWuSql.append(whereSql);
		
		String qwOrderNum = orderStatisticsService.getQZNotSeenOrderNum(qianWuSql);
		
		String compnayUuid = UserUtils.getUser().getCompany().getUuid();
		String xsOrderNum = "0";
		if (Context.SUPPLIER_UUID_RXGG.equals(compnayUuid)) {
			common.setPermission("visaOrderForSale:list:allorder");
			StringBuffer xsSql = new StringBuffer("");
			visaOrderService.getMainSql(null, xsSql, new VisaOrderForm());
			StringBuffer whereSb = visaOrderService.addWhereSql(null, common, null, "xiaoshou", null);
			xsSql.append(whereSb);
			
			xsOrderNum = orderStatisticsService.getQZNotSeenOrderNum(xsSql);
		}
		
		
		return qwOrderNum + ";" + xsOrderNum;
	}
	
	/**
	 * 获取T1预报名订单没有查看数量
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "getT1NotSeenOrderNum")
	public String getT1NotSeenOrderNum(HttpServletRequest request) {
		return orderStatisticsService.getT1NotSeenOrderNum();
	}
	
	/**
	 * 获取财务统计数据
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "getCWNotSeenNum")
	public Map<String, Integer> getCWNotSeenNum(HttpServletRequest request) {
		
		// 新增成本付款总数
		Integer orderPayCount = reviewService.getOrderPayCount();
		// 新增退款付款总数
		Integer refundCount = reviewService.getRefundCount();
		// 新增返佣付款总数
		Integer rebateCount = reviewService.getRebateCount();
		// 公版借款付款未付款的数目
		Long borrowMoneyCount = payManagerService.getBorrowMoneyNotPayedCount();
		// 新增代收服务费付款总数
		Integer serviceChargeCount = serviceChargeService.getServiceChargeCount();
		// 其他收入收款未达账金额的数目
		Long paidother = costManageService.findNotAccountedCountOtherIncome();
		// 待处理的收据数量
		Integer receiptNumAll = orderReceiptService.getToBeReviewReceiptNum() + orderReceiptService.getReviewedReceiptNum();
		// 待处理的发票数量
		Integer invoiceNumAll = orderInvoiceService.getToBeReviewInvoiceNum() + orderInvoiceService.getReviewedInvoiceNum();
		// 1:订单收款 ,2:切位收款,3:签证押金收款，4:签证订单收款
		Integer countForOrderListDZ1 = orderService.getCountForOrderListDZ(1);
		Integer countForOrderListDZ2 = orderService.getCountForOrderListDZ(2);
		Integer countForOrderListDZ3 = orderService.getCountForOrderListDZ(3);
		Integer countForOrderListDZ4 = orderService.getCountForOrderListDZ(4);
		
		Map<String, Integer> map = Maps.newHashMap();
		map.put("orderPayCount", orderPayCount != null ? orderPayCount : 0);
		map.put("refundCount", refundCount != null ? refundCount : 0);
		map.put("rebateCount", rebateCount != null ? rebateCount : 0);
		map.put("borrowMoneyCount", borrowMoneyCount != null ? borrowMoneyCount.intValue() : 0);
		map.put("serviceChargeCount", serviceChargeCount != null ? serviceChargeCount : 0);
		map.put("paidother", paidother != null ? paidother.intValue() : 0);
		map.put("receiptNumAll", receiptNumAll != null ? receiptNumAll : 0);
		map.put("invoiceNumAll", invoiceNumAll != null ? invoiceNumAll : 0);
		map.put("countForOrderListDZ1", countForOrderListDZ1 != null ? countForOrderListDZ1 : 0);
		map.put("countForOrderListDZ2", countForOrderListDZ2 != null ? countForOrderListDZ2 : 0);
		map.put("countForOrderListDZ3", countForOrderListDZ3 != null ? countForOrderListDZ3 : 0);
		map.put("countForOrderListDZ4", countForOrderListDZ4 != null ? countForOrderListDZ4 : 0);
		
		return map;
	}
	
}
