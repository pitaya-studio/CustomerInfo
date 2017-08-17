package com.trekiz.admin.modules.activity.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.modules.sys.entity.*;
import com.trekiz.admin.modules.sys.service.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.groupCover.service.GroupCoverService;
import com.trekiz.admin.modules.mtourCommon.utils.ThreadVariable;
import com.trekiz.admin.modules.order.entity.ActivityInfo;
import com.trekiz.admin.modules.order.service.ApplyOrderCommonService;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 旅游产品信息控制器
 * @author liangjingming
 *
 */
@Controller
@RequestMapping(value="${adminPath}/activity/managerforOrder")
public class TravelActivityControllerForOrder extends BaseController{

	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private AreaService areaService;
	@Autowired
    private AgentinfoService agentinfoService;
	@Autowired
    private QuauqAgentInfoService quauqAgentinfoService;
	@Autowired
    private DepartmentService departmentService;
	@Autowired
	private ApplyOrderCommonService applyOrderCommonService;
	@Autowired
    private IActivityAirTicketService iActivityAirTicketService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private AirlineInfoService airlineInfoService;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
    private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private GroupCoverService groupCoverService;
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 156;
	}
	
	/**
	 * @param showType 预定、预报名
	 * @param activityKind  产品种类：单团、散拼...
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"list/{showType}/{activityKind}"})
	public String list(@PathVariable String showType, @PathVariable String activityKind, @ModelAttribute TravelActivity travelActivity,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		// 按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("bookOrder", model);
		
		// 查询条件
        Map<String,String> mapRequest = Maps.newHashMap();
		
		// 参数处理：保留天数、行程天数、出团、截团日期、起始同行价格、结束同行价格、产品种类、舱型、是否有余位、是否有切位、产品查询还是团期查询
        String paras = "remainDays,activityDuration,groupOpenDate,groupCloseDate,agentId,settlementAdultPriceStart,settlementAdultPriceEnd," +
        		"orderNumOrGroupCode,wholeSalerKey,spaceType,haveYw,haveQw,activityCreate,productType";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
        // 订单或团期列表标识：order为订单、group为团期，默认查询订单列表
        String productOrGroup = request.getParameter("productOrGroup");
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        if (StringUtils.isBlank(productOrGroup)) {
        	//如果是优加批发商，则默认按团期展示
        	if (companyUuid != null && (Context.SUPPLIER_UUID_CPLY.equals(companyUuid) || Context.SUPPLIER_UUID_YJ.equals(companyUuid)) ){
        		productOrGroup = "group";
        	} else {
        		productOrGroup = "product";
        	}
        }
        model.addAttribute("productOrGroup", productOrGroup);
		
        // 只查询上架产品
		travelActivity.setActivityStatus(2);
		// 产品种类
		if (StringUtils.isNotBlank(activityKind)) {
			travelActivity.setActivityKind(Integer.valueOf(activityKind));
		}
		// 产品或团期列表查询
		if ("product".equals(productOrGroup)) {
			Page<TravelActivity> pages = new Page<TravelActivity>(request, response);
			String orderBy = request.getParameter("orderBy");
			// 排序方式
	        if (StringUtils.isBlank(orderBy)) {
	            orderBy = "groupOpenDate";
	            pages.setOrderBy(orderBy);
	        }
	        Page<TravelActivity> page = travelActivityService.findTravelActivity(pages, travelActivity, common, mapRequest);
	        // 处理查询数据
	        handleTravelActivityPage(page, mapRequest, travelActivity,activityKind);
	        
			// 排序方式
	        if (StringUtils.isBlank(orderBy)) {
	            orderBy = "groupOpenDate";
	            page.setOrderBy(orderBy);
	        }
	        model.addAttribute("page", page);
		} else {
			Page<ActivityGroup> pages = new Page<ActivityGroup>(request, response);
			String orderBy = request.getParameter("orderBy");
			// 排序方式
	        if (StringUtils.isBlank(orderBy)) {
	            orderBy = "groupOpenDate";
	            pages.setOrderBy(orderBy);
	        }
	        Page<ActivityGroup> page = travelActivityService.findActivityGroup(pages, travelActivity, common, mapRequest);
	        // 处理查询数据
	        handleActivityGroupPage(page, mapRequest, travelActivity);
			// 排序方式
	        if (StringUtils.isBlank(orderBy)) {
	            orderBy = "groupOpenDate";
	            page.setOrderBy(orderBy);
	        }
	        model.addAttribute("page", page);
		}
		
        // 销售明细参数
        salerPara(model);
        
        // 参数传递
        formPara(model, request, showType, companyId, travelActivity, activityKind, common);
        model.addAttribute("productType", mapRequest.get("productType"));
		Office office = officeService.get(companyId);
		if(office != null) {
			model.addAttribute("officeShelfRightsStatus", office.getShelfRightsStatus()); // 批发商上架权限开启状态
		}
		model.addAttribute("user", UserUtils.getUser());
//		model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(activityKind));
		boolean permitted = SecurityUtils.getSubject().isPermitted(OrderCommonUtil.getStringOrdeType(activityKind) + "Order:operation:customerConfirm");
		model.addAttribute("isSeizedConfirmation", permitted);
		return getReturnUrl(activityKind);
	}
	
	/**
	 * @Description 处理产品查询结果
	 * @author yakun.bai
	 * @Date 2016-1-9
	 */
	private void handleTravelActivityPage(Page<TravelActivity> page, Map<String,String> mapRequest, TravelActivity travelActivity,String activityKind) {
		if (page.getList() != null) {
			String activityNameOrGroupCode = mapRequest.get("wholeSalerKey");
			for (TravelActivity tmp : page.getList()) {
				//处理‘单房差’的单位，去掉“/”
				if (StringUtils.isNotBlank(tmp.getSingleDiffUnit())) {
					tmp.setSingleDiffUnit(tmp.getSingleDiffUnit().replace("/", "").replaceAll(" ", ""));
				}
				//团期相关处理
			     Set<ActivityGroup> ags = tmp.getActivityGroups();
			     if (CollectionUtils.isNotEmpty(ags)) {
			    	 if (ags.size() > 1) {
			    		 travelActivity.setId(tmp.getId());
			    		 List<ActivityGroup> groupList = travelActivityService.findGroupsByActivityId(travelActivity, mapRequest);
			    		 Set<ActivityGroup> groupSet = Sets.newLinkedHashSet();
			    		 for (ActivityGroup ag : groupList) {
					    	 List<Object[]> orderPersonNumList = applyOrderCommonService.sumOrderPersonNumByGroupId(ag.getId());
					    	 Object[] orderPersonNumArr = orderPersonNumList.get(0);
					    	 ag.setOrderPersonNum(Integer.parseInt(orderPersonNumArr[0].toString()));
					    	 //通过团期 id 和产品 id 来查找当前团期下 剩余的切位人数(即除了占切位的人数)
					    	 Integer leftpayReservePosition = travelActivityService.getAllLeftpayReservePosition(ag.getId(), tmp.getId());
					    	 //为每一个团期设置剩余的切位人数
					    	 ag.setLeftpayReservePosition(leftpayReservePosition);
					    	//通过团期ID和产品ID 来查当前团期是否有补位
					    	 if(activityKind != null && activityKind.equals("2")){
					    		 Integer groupCoverCount = groupCoverService.getAllGroupcoverNumOfgroupid(ag.getId());
					    		 ag.setGroupcoverNum(groupCoverCount);
					    	 }
					    	 groupSet.add(ag);
					     }
			    		 tmp.setActivityGroups(groupSet);
			    	 } else {
			    		 Long activityGroupId = ags.iterator().next().getId();
			    		 //通过团期 id 和产品 id 来查找当前团期下 剩余的切位人数(即除了占切位的人数)
				    	 Integer leftpayReservePosition = travelActivityService.getAllLeftpayReservePosition(activityGroupId, tmp.getId());
				    	 //为每一个团期设置剩余的切位人数
			    		 List<Object[]> orderPersonNumList = applyOrderCommonService.sumOrderPersonNumByGroupId(activityGroupId);
				    	 Object[] orderPersonNumArr = orderPersonNumList.get(0);
				    	 ags.iterator().next().setOrderPersonNum(Integer.parseInt(orderPersonNumArr[0].toString()));
				    	 ags.iterator().next().setLeftpayReservePosition(leftpayReservePosition);
				    	 //通过团期ID和产品ID 来查当前团期是否有补位
				    	 if(activityKind != null && activityKind.equals("2")){
				    		 Integer groupCoverCount = groupCoverService.getAllGroupcoverNumOfgroupid(activityGroupId);
				    		 ags.iterator().next().setGroupcoverNum(groupCoverCount);
				    	 }
				    	 
			    	 }
			     }
			     
			     //当输入团号搜索时，只显示该团号对应的団期
			     if (StringUtils.isNotBlank(activityNameOrGroupCode)) {
			    	 boolean flag = false;
			    	 Set<ActivityGroup> resultSet = new HashSet<ActivityGroup>();
			    	 Set<ActivityGroup> errSet = new HashSet<ActivityGroup>();
					 Set<ActivityGroup> groups = tmp.getActivityGroups();
					 for (ActivityGroup g : groups) {
						 if (g.getGroupCode().contains(activityNameOrGroupCode.trim())) {
							 resultSet.add(g);
							 flag =true;
						 } else {
							 errSet.add(g);
						 }
					 }
					 if (flag) {
						 tmp.setActivityGroups(resultSet);	
					 } else {
						 tmp.setActivityGroups(errSet);	
					 }
				}
			}
		}
	}
	
	/**
	 * @Description 处理团期查询结果
	 * @author yakun.bai
	 * @Date 2016-1-9
	 */
	private void handleActivityGroupPage(Page<ActivityGroup> page, Map<String,String> mapRequest, TravelActivity travelActivity) {
		if (page.getList() != null) {
			for (ActivityGroup group : page.getList()) {
				TravelActivity tmp = group.getTravelActivity();
				//处理‘单房差’的单位，去掉“/”
				if (StringUtils.isNotBlank(tmp.getSingleDiffUnit())) {
					tmp.setSingleDiffUnit(tmp.getSingleDiffUnit().replace("/", "").replaceAll(" ", ""));
				}
				Long activityGroupId = group.getId();
				// 通过团期 id 和产品 id 来查找当前团期下 剩余的切位人数(即除了占切位的人数)
				Integer leftpayReservePosition = travelActivityService.getAllLeftpayReservePosition(activityGroupId, tmp.getId());
				// 为每一个团期设置剩余的切位人数
				List<Object[]> orderPersonNumList = applyOrderCommonService.sumOrderPersonNumByGroupId(activityGroupId);
				Object[] orderPersonNumArr = orderPersonNumList.get(0);
				group.setOrderPersonNum(Integer.parseInt(orderPersonNumArr[0].toString()));
				group.setLeftpayReservePosition(leftpayReservePosition);
				//为每一个团期设置补位数 
				

				/** 查询已收人数，即报名总人数(当前团期的所有订单的预定人数之和) */
				Long totalOrderPersonNum = productOrderService.findTotalNumByGroupId(group.getId());
				group.setTotalOrderPersonNum(totalOrderPersonNum.toString());
				
				//通过团期ID和产品ID 来查当前团期是否有补位
				if(group.getTravelActivity().getActivityKind() == 2){
					Integer groupCoverCount = groupCoverService.getAllGroupcoverNumOfgroupid(group.getId());
					group.setGroupcoverNum(groupCoverCount);
				}
			}
		}
	}
	
	/**
	 * @Description 销售明细参数
	 * @author yakun.bai
	 * @Date 2016-1-9
	 */
	private void salerPara(Model model) {
		if (UserUtils.getUser().getCompany().getQueryCommonFields() != null) {
			// 明细详情
			String[] queryCommonFields = UserUtils.getUser().getCompany().getQueryCommonFields().split(",");
			if(queryCommonFields!=null && queryCommonFields.length>0){
				for(String str : queryCommonFields){
					if(str.equals("1")){// 订单号码
						model.addAttribute("orderNo",str);
					}else if(str.equals("2")){// 渠道名称
						model.addAttribute("agentName",str);
					}else if(str.equals("3")){// 销售人
						model.addAttribute("shell",str);
					}else if(str.equals("4")){ // 下单人
						model.addAttribute("orderUser",str);
					}else if(str.equals("5")){// 预订时间
						model.addAttribute("reserveDate",str);
					}else if(str.equals("6")){// 订单人数
						model.addAttribute("personNum",str);
					}else if(str.equals("7")){// 订单状态
						model.addAttribute("orderStatus",str);
					}else if(str.equals("8")){// 订单总额
						model.addAttribute("totalAmount",str);
					}else if(str.equals("9")){//已付金额&达帐金额
						model.addAttribute("payedAmount",str);
					}
				}
			}
		}
		// 获取产品明细显示详情设置
		Long queryCommonOrderList = UserUtils.getUser().getCompany().getQueryCommonOrderList(); //是否查看明细
		model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		model.addAttribute("youjiaCompanyUuid", Context.YOU_JIA_COMPANYUUID);
		model.addAttribute("queryCommonOrderList",queryCommonOrderList);
	}
	
	/**
	 * @Description 参数传递到前台
	 * @author yakun.bai
	 * @Date 2016-1-9
	 */
	private void formPara(Model model, HttpServletRequest request, String showType, Long companyId, TravelActivity travelActivity, 
			String activityKind, DepartmentCommon common) {
		model.addAttribute("fromAreas", DictUtils.findUserDict(companyId ,"fromarea"));
        model.addAttribute("targetAreaNames", request.getParameter("targetAreaNameList"));
        model.addAttribute("trafficNames", iActivityAirTicketService.findAirlineByComid(companyId));
		model.addAttribute("travelActivity", travelActivity);
        model.addAttribute("showType", showType);
        model.addAttribute("activityKind", activityKind);
        model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(activityKind));
        model.addAttribute("userList", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",companyId));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",companyId));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",companyId));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("payTypes", DictUtils.getSysDicMap(Context.PAY_TYPE));
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		model.addAttribute("quauqAgentinfoList", quauqAgentinfoService.getAllQuauqAgentinfos());
		model.addAttribute("showAreaList", common.getShowAreaList());
		model.addAttribute("cruiseTypeList", DictUtils.getDictList("cruise_type"));	//舱型列表
	}
	
	/**
	 * @Description 获取返回URL
	 * @author yakun.bai
	 * @Date 2016-1-9
	 */
	private String getReturnUrl(String activityKind) {
		String url = "";
    	if ("1".equals(activityKind)) {//单团  single
    		url = "modules/order/single/activityListForSingleOrder";
    	} else if ("2".equals(activityKind)) {//散拼    		
    		url = "modules/order/loose/activityListForLooseOrder";
    	} else if ("3".equals(activityKind)) {//游行  parade
    		url = "modules/order/study/activityListForStudyOrder";
    	} else if ("4".equals(activityKind)) {//大客户   bigAccount 
    		url = "modules/order/bigcustomer/activityListForBigCustomerOrder";
    	} else if ("5".equals(activityKind)) {//自由行  freeTravel
    		url = "modules/order/free/activityListForFreeTravelOrder";
    	} else if ("10".equals(activityKind)) {//游轮  freeTravel
    		url = "modules/order/pulley/activityListForLoosePulley";
    	} else {
    		url = "modules/order/loose/activityListForLooseOrder";
    	}
    	return url;
	}
	
	
	/**
	 * 获取所有渠道商
	 * @author yang.jiang
	 * @date 2015-12-19 14:36:30
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"getAgentinfoList"})
	public String getAgentinfoList(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		//获取渠道商列表
		List<Agentinfo> agentinfos = agentinfoService.findAllAgentinfo();
		//目标渠道商（由于太重，只存id和name）
		List<Agentinfo> targetAgents = new LinkedList<Agentinfo>();
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(companyId != 68) {
			Agentinfo myAgentinfo = new Agentinfo();
			myAgentinfo.setId(-1L);
			myAgentinfo.setAgentName("非签约渠道");
			targetAgents.add(myAgentinfo);
		}
		if (CollectionUtils.isNotEmpty(agentinfos)) {			
			for (Agentinfo agentinfo : agentinfos) {
				Agentinfo myAgentinfo = new Agentinfo();
				myAgentinfo.setId(agentinfo.getId());
				myAgentinfo.setAgentName(agentinfo.getAgentName());
				targetAgents.add(myAgentinfo);
			}
		}
		
		//装载信息
		if(CollectionUtils.isNotEmpty(targetAgents)){
			map.put("agentinfos", targetAgents);
			map.put("res", "success");
		} else {
			map.put("res", "faild");
		}
		//转json到ajax响应
		String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
		ThreadVariable.setMtourAjaxResponse(json);
		return json;
	}
	
	/**
	 * 0518需求 获取团期平台上架状态和批发商上架权限状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value={"getGroupAndOfficeT1PermissionStatus"})
	@ResponseBody
	public Object getGroupAndOfficeT1PermissionStatus(HttpServletRequest request, HttpServletResponse response){
		String groupId = request.getParameter("groupId");
		Map<String, String> map = new HashMap<>();
		if(StringUtils.isNotBlank(groupId)){
			List<Map<String,String>> list = travelActivityService.getGroupAndOfficeT1PermissionStatus(groupId);
			map = list.get(0);
			map.put("result", "success");
		}else{
			map.put("result", "fail");
		}
		return map;
	}
	
	/**
	 * 单团报名：销售明细
	 * @author gao
	 * @date 20151113
	 * @param request
	 * @param response
	 * @param activityID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"activityInfo"})
	public String activityInfo(HttpServletRequest request, HttpServletResponse response){
		String activityGroupID = request.getParameter("activityGroupID");
		Map<String,Object> map = new HashMap<String,Object>();
		List<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>();
		if(activityGroupID!=null){
			Long groupID = Long.valueOf(activityGroupID);
			activityInfoList = productOrderService.getActivityInfo(groupID);
			if(!activityInfoList.isEmpty()){
				map.put("activityInfoList", activityInfoList);
				map.put("res", "success");
				String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
				ThreadVariable.setMtourAjaxResponse(json);
				return json;
			}
		}
		map.put("res", "error");
		String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
		ThreadVariable.setMtourAjaxResponse(json);
		return json;
	}
	
	/**
	 * 渠道商登陆只显示所属批发商录过的目标区域
	 * 批发商登陆只显示自己录过的目标区域   
	 * 创建人：liangjingming   
	 * 创建时间：2014-2-10 下午3:11:19      
	 *
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "filterTreeData")
	public List<Map<String, Object>> filterTreeData(@RequestParam(required=false) Long extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList(); 
		//批发商ID
		Long companyId = UserUtils.getUser().getCompany().getId();
		targetAreaIds = travelActivityService.findAreaIds(companyId);
		if(targetAreaIds!=null && targetAreaIds.size()!=0){
			for(Map<String, Object> map:targetAreaIds){			
				childAreaIds.add(Long.parseLong(String.valueOf(map.get("targetAreaId"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds,areaIds,targetAreas);
		//目的地
		Map<String, Object> map = null;
		for (int i=0; i<targetAreas.size(); i++){
			Area e = targetAreas.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 批量导出团期余位表
	 * @param showType
	 * @param activityKind
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value={"downloadAllYw/{showType}/{activityKind}"})
	public void downloadAllYw(@PathVariable String showType, @PathVariable String activityKind, @ModelAttribute TravelActivity travelActivity,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("bookOrder", model);
		
		//查询条件
        Map<String,String> mapRequest = Maps.newHashMap();
		
		//参数处理：起始同行价格、结束同行价格、产品种类、舱型、是否有余位、是否有切位
        String paras = "settlementAdultPriceStart,settlementAdultPriceEnd,orderNumOrGroupCode,wholeSalerKey,spaceType,haveYw,haveQw";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
        if (travelActivity.getGroupOpenDate() == null) {
        	travelActivity.setGroupOpenDate(new Date());
        } else {
        	String tempDate = DateUtils.date2String(travelActivity.getGroupOpenDate()) + " 23:59:59";
        	if (new Date().after(DateUtils.dateFormat(tempDate, DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS))) {
        		travelActivity.setGroupOpenDate(new Date());
        	}
        }
		
		String remainDays = request.getParameter("remainDays");//保留天数
		
		if(StringUtils.isNotBlank(remainDays))
			travelActivity.setRemainDays(Integer.parseInt(remainDays));
		else
			travelActivity.setRemainDays(null);
		travelActivity.setActivityStatus(2);//只查询上架产品
		if(StringUtils.isNotBlank(activityKind)){
			travelActivity.setActivityKind(Integer.valueOf(activityKind));//产品种类
		}
		Page<TravelActivity> pages = new Page<TravelActivity>(request, response);
		String orderBy = request.getParameter("orderBy");
		//排序方式
        if(StringUtils.isBlank(orderBy)){
            orderBy = "groupOpenDate";
            pages.setOrderBy(orderBy);
        }
        pages.setPageSize(10000);
		Page<TravelActivity> page = travelActivityService.findTravelActivity(pages, travelActivity, common, mapRequest);
		List<Long> groupIdList = Lists.newArrayList();
		if (page.getList() != null) {
			for (TravelActivity tmp : page.getList()) {
				
				//当输入团号搜索时，只显示该团号对应的団期
				String activityNameOrGroupCode = mapRequest.get("wholeSalerKey");
				if (StringUtils.isNotBlank(activityNameOrGroupCode)) {
					boolean flag = false;
					Set<ActivityGroup> resultSet = new HashSet<ActivityGroup>();
					Set<ActivityGroup> errSet = new HashSet<ActivityGroup>();
					Set<ActivityGroup> groups = tmp.getActivityGroups();
					for (ActivityGroup g : groups) {
						if (g.getGroupCode().contains(activityNameOrGroupCode.trim())) {
							resultSet.add(g);
							flag =true;
						} else {
							errSet.add(g);
						}
					}
					if (flag) {
						tmp.setActivityGroups(resultSet);	
					} else {
						tmp.setActivityGroups(errSet);	
					}
				}
				
			     Set<ActivityGroup> ags = tmp.getActivityGroups();
			     if (CollectionUtils.isNotEmpty(ags)) {
			    	 if (ags.size() > 1) {
			    		 travelActivity.setId(tmp.getId());
			    		 List<ActivityGroup> groupList = travelActivityService.findGroupsByActivityId(travelActivity, mapRequest);
			    		 for (ActivityGroup ag : groupList) {
					    	 groupIdList.add(ag.getId());
					     }
			    	 } else {
			    		 groupIdList.add(ags.iterator().next().getId());
			    	 }
			     }
			}
		}
		downloadYwCommon(groupIdList, request, response);
	}
	
	/**
	 * 导出团期余位表
	 * @param groupId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="downloadYw",method=RequestMethod.POST)
	public void downloadYw(@RequestParam(value="groupId") String groupId, HttpServletRequest request, HttpServletResponse response) {
		List<Long> groupIdList = Lists.newArrayList();
		if (StringUtils.isNotBlank(groupId)) {
			groupIdList.add(Long.parseLong(groupId));
		} else {
			return;
		}
		downloadYwCommon(groupIdList, request, response);
	}
	
	/**
	 * 导出团期余位表(指定的团期，可以属于不同产品)
	 * @param groupId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="downloadYwChosed",method=RequestMethod.POST)
	public void downloadYwChosed(@RequestParam(value="paramGroupIds") List<String> paramGroupIds, HttpServletRequest request, HttpServletResponse response) {
		List<Long> groupIdList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(paramGroupIds)) {
			for (String groupId : paramGroupIds) {				
				if (StringUtils.isNotBlank(groupId)) {
					groupIdList.add(Long.parseLong(groupId));
				}
			}
		}
		if (CollectionUtils.isNotEmpty(groupIdList)) {			
			downloadYwCommon(groupIdList, request, response);
		}
	}

	/**
	 * 余位下载共通方法
	 * @param groupIdList
	 * @param request
	 * @param response
	 */
	private void downloadYwCommon(List<Long> groupIdList, HttpServletRequest request, HttpServletResponse response) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Currency> curreycyList = currencyService.findCurrencyList(companyId);
		Map<String, String> currencyMap = Maps.newHashMap();
		for (Currency currency : curreycyList) {
			currencyMap.put(currency.getId().toString(), currency.getCurrencyMark());
		}
		
		List<Object[]> groupYwList = Lists.newArrayList();
		try {
			List<ActivityGroup> groupList = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(groupIdList)) {
				groupList = travelActivityService.getYwByGroupIds(groupIdList);
			}
			if (CollectionUtils.isNotEmpty(groupList)) {
				for (ActivityGroup agp : groupList) {
					Object [] obj = new Object[18];
					//团队类型
					if (agp.getTravelActivity().getActivityKind() != null) {
						obj[0] = OrderCommonUtil.getChineseOrderType(agp.getTravelActivity().getActivityKind().toString());
					} else {
						obj[0] = "无";
					}
					//产品名称
					if (StringUtils.isNotBlank(agp.getTravelActivity().getAcitivityName())) {
						obj[1] = agp.getTravelActivity().getAcitivityName();
					} else {
						obj[1] = "无";
					}
					//团号
					if (StringUtils.isNotBlank(agp.getGroupCode())) {
						obj[2] = agp.getGroupCode();
					} else {
						obj[2] = "无";
					}
					//出团日期
					if (agp.getGroupOpenDate() != null) {
						obj[3] = DateUtils.date2String(agp.getGroupOpenDate());
					} else {
						obj[3] = "无";
					}
					//截团日期
					if (agp.getGroupCloseDate() != null) {
						obj[4] = DateUtils.date2String(agp.getGroupCloseDate());
					} else {
						obj[4] = "无";
					}
					//航空公司
					if (agp.getTravelActivity().getActivityAirTicket() != null 
							&& CollectionUtils.isNotEmpty(agp.getTravelActivity().getActivityAirTicket().getFlightInfos())) {
						
						ActivityAirTicket airticket = agp.getTravelActivity().getActivityAirTicket();
						List<FlightInfo> flighInfoList = airticket.getFlightInfos();
						String airType = airticket.getAirType();// 单程、往返、多段，值分别为3、2、1
						if ("3".equals(airType)) {
							String airlines = flighInfoList.get(0).getAirlines();
							if (StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
								List<AirlineInfo>  airlineInfoList = airlineInfoService.getByAirlineCode(companyId, 0, flighInfoList.get(0).getAirlines());
								if (CollectionUtils.isNotEmpty(airlineInfoList)) {
									obj[5] = "单程：" + airlineInfoList.get(0).getAirlineName();
								} else {
									obj[5] = "单程：" + "无";
								}
							} else {
								obj[5] = "单程：" + "无";
							}
							String flightNumber = flighInfoList.get(0).getFlightNumber();
							if (StringUtils.isNotBlank(flightNumber)) {
								obj[6] = "单程：" + flighInfoList.get(0).getFlightNumber();
							} else {
								obj[6] = "单程：" + "无";
							}
						} else if ("2".equals(airType)) {
							obj[5] = "";
							obj[6] = "";
							for (FlightInfo flighInfo : flighInfoList) {
								if (flighInfo.getNumber() == 1) {
									String airlines = flighInfo.getAirlines();
									if (StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
										List<AirlineInfo>  airlineInfoList = airlineInfoService.getByAirlineCode(companyId, 0, flighInfo.getAirlines());
										if (CollectionUtils.isNotEmpty(airlineInfoList)) {
											obj[5] += "去程：" + airlineInfoList.get(0).getAirlineName();
										} else {
											obj[5] += "去程：" + "无";
										}
									} else {
										obj[5] += "去程：" + "无";
									}
									String flightNumber = flighInfo.getFlightNumber();
									if (StringUtils.isNotBlank(flightNumber)) {
										obj[6] += "去程：" + flighInfo.getFlightNumber();
									} else {
										obj[6] += "去程：" + "无";
									}
								} else {
									String airlines = flighInfo.getAirlines();
									if (StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
										List<AirlineInfo>  airlineInfoList = airlineInfoService.getByAirlineCode(companyId, 0, flighInfo.getAirlines());
										if (CollectionUtils.isNotEmpty(airlineInfoList)) {
											obj[5] += "返程：" + airlineInfoList.get(0).getAirlineName();
										} else {
											obj[5] += "返程：" + "无";
										}
									} else {
										obj[5] += "返程：" + "无";
									}
									String flightNumber = flighInfo.getFlightNumber();
									if (StringUtils.isNotBlank(flightNumber)) {
										obj[6] += "返程：" + flighInfo.getFlightNumber();
									} else {
										obj[6] += "返程：" + "无";
									}
								}
							}
						} else if ("1".equals(airType)) {
							obj[5] = "";
							obj[6] = "";
							int i = 1;
							for (FlightInfo flighInfo : flighInfoList) {
								String airlines = flighInfo.getAirlines();
								if (StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
									List<AirlineInfo>  airlineInfoList = airlineInfoService.getByAirlineCode(companyId, 0, flighInfo.getAirlines());
									if (CollectionUtils.isNotEmpty(airlineInfoList)) {
										obj[5] += "第" + i + "段：" + airlineInfoList.get(0).getAirlineName();
									} else {
										obj[5] += "无";
									}
								} else {
									obj[5] += "无";
								}
								String flightNumber = flighInfo.getFlightNumber();
								if (StringUtils.isNotBlank(flightNumber)) {
									obj[6] += "第" + i + "段：" + flighInfo.getFlightNumber();
								} else {
									obj[6] += "无";
								}
								i++;
							}
						}
					} else {
						obj[5] = "无";
						obj[6] = "无";
					}
					obj[7] = "无";
					String currency = agp.getCurrencyType();
					String[] currencyArr = {"1","1","1","1","1","1","1","1"};
					currencyArr = currency.split(",");
					if (StringUtils.isNotBlank(currency) && currencyArr.length >= 3) {
						//成人同行价
						if (agp.getSettlementAdultPrice() != null) {
							obj[8] = currencyMap.get(currencyArr[0]) + agp.getSettlementAdultPrice();
						} else {
							obj[8] = "无";
						}
						//儿童同行价
						if (agp.getSettlementcChildPrice() != null) {
							obj[9] = currencyMap.get(currencyArr[1]) + agp.getSettlementcChildPrice();
						} else {
							obj[9] = "无";
						}
						//特殊人群同行价
						if (agp.getSettlementSpecialPrice() != null) {
							obj[10] = currencyMap.get(currencyArr[2]) + agp.getSettlementSpecialPrice();
						} else {
							obj[10] = "无";
						}
					} else {
						obj[8] = "无";
						obj[9] = "无";
						obj[10] = "无";
					}
					//预收
					if (agp.getPlanPosition() != null) {
						obj[11] = agp.getPlanPosition();
					} else {
						obj[11] = "无";
					}
					//余位
					if (agp.getFreePosition() != null) {
						obj[12] = agp.getFreePosition();
					} else {
						obj[12] = "无";
					}
					//占位
					obj[13] = agp.getNopayReservePosition();
					//切位
					obj[14] = agp.getPayReservePosition();
					//售出切位
					obj[15] = agp.getSoldPayPosition();
					//签证国家
					if (StringUtils.isNotBlank(agp.getVisaCountry())) {
						obj[16] = agp.getVisaCountry();
					} else {
						obj[16] = "无";
					}
					//签证资料截止提前天数
					if (agp.getGroupOpenDate() != null && agp.getVisaDate() != null) {
						obj[17] = ((agp.getGroupOpenDate().getTime() - agp.getVisaDate().getTime()) / (24*60*60*1000)) -1; 
					} else {
						obj[17] = "无";
					}
					groupYwList.add(obj);
				}
			}
			
			//文件名称
			String fileName = "团期余位下载";
			//Excel各行名称
			String[] cellTitle =  {"团队类型","产品名称","团号","出团日期","截团日期","航空公司","航班号","参考航空公司","成人同行价",
					"儿童同行价","特殊人群同行价","预收","余位","占位","切位","售出切位","签证国家","签证资料截止提前天数"};
			//文件首行标题
			String firstTitle = "团期余位表信息";
			ExportExcel.createExcle(fileName, groupYwList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			logger.error("下载出错");
			e.printStackTrace();
		}
	}
	
	public void isHasGroupOnT1( Page<TravelActivity> page, Model model){
		List<Integer> flagList = new ArrayList<Integer>();
		if(page.getList()!=null) {
			for(TravelActivity tmp : page.getList()) {
				//判断是否有团期上架到T1
				int isT1 = 0;
				Set<ActivityGroup> groupTemp = tmp.getActivityGroups();
				for(ActivityGroup group : groupTemp){
					if(group.getIsT1() == Context.QUAUQ_T1_ON){
						isT1 = 1;
					}
				}
				flagList.add(isT1);
		
			}
			model.addAttribute("flagList", flagList);
		}
	}
	
	/**
	 * 查询所有的渠道（用于预报名的渠道选择）
	 * @param request
	 * @param response
	 * @return
	 * @author chao.zhang
	 * @date 2017-03-24
	 */
	@ResponseBody
	@RequestMapping(value = "getAgenList")
	public String getAgenList(HttpServletRequest request,HttpServletResponse response){
		String param = request.getParameter("param");
		JSONObject jsonObject = JSON.parseObject(param);
		String agentType = jsonObject.getString("agentType");
		Integer pageNo = jsonObject.getInteger("pageNo");
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request,response);
		page.setPageNo(pageNo);
		page.setPageSize(100);
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String agentName = jsonObject.getString("agentName");
		if(StringUtils.isEmpty(agentType) && agentType.equals("1")){
			page = agentinfoService.getAgentForSelfByCompanyId(page, agentName, companyId);
		}else{
			page = agentinfoService.getAgentOfQuauq(page, agentName);
		}
		return JSON.toJSONString(page.getList());
	}
}

