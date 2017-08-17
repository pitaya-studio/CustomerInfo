package com.trekiz.admin.modules.activity.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.DateJsonValueConfig;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.GroupcodeModifiedRecord;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.GroupcodeModifiedRecordDao;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.GroupControlBoardService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ILogProductService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.utils.TravelActivityUtil;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.service.IReceivePayService;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderListService;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Remind;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.IRemindService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/activity/manager")
public class ActivityGroupController extends BaseController {

	private static final Log logger = LogFactory
			.getLog(ActivityGroupController.class);

	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private IRemindService remindService;
	@Autowired
	@Qualifier("activityGroupSyncService")
	private IActivityGroupService activityGroupService;

	@Autowired
	private OrderCommonService orderService;
	@Autowired
	IReceivePayService ireceivePayServiceImpl;
	@Autowired
	private ActivityGroupService groupService;
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private OrderListService orderListService;

	@Autowired
	private DocInfoService docInfoService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private GroupcodeModifiedRecordDao groupcodeModifiedRecordDao;

	@Autowired
	private ActivityGroupDao activityGroupDao;

	@Autowired
	private ProductOrderService productOrderService;
	
	@Autowired
	private GroupControlBoardService groupControlBoardService;

	@ModelAttribute("menuId")
	protected Integer getMenuId() {
		return 160;
	}

	@Autowired
	private ILogProductService logProductService;

	@Autowired
	private AgentinfoService agentinfoService;

	/**
	 * 判断团期是否有对应游客信息或订单信息
	 * 
	 * @param groupId
	 * @param status
	 *            游客或订单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "existExportData", method = RequestMethod.POST)
	public Object existExportData(
			@RequestParam(value = "groupId") String groupId,
			@RequestParam(value = "orderType") String orderType,
			@RequestParam(value = "status", required = false) String status) {
		System.out.println("----exitExportData----");
		List<Object[]> travelerList = new ArrayList<Object[]>();
		JSONArray results = new JSONArray();
		JSONObject resobj = new JSONObject();
		if (groupId != null && !"".equals(groupId)) {
			List<ProductOrderCommon> list = orderService
					.findByProductGroupIdOrderByCompany(Long.parseLong(groupId));
			if (list != null && list.size() > 0) {
				if ("customer".equals(status)) {
					for (ProductOrderCommon productOrder : list) {
						List<Object[]> tempList = orderService
								.selectTravelerByOrderId(productOrder.getId(),
										Integer.parseInt(orderType));
						if (tempList != null && tempList.size() > 0) {
							travelerList.addAll(tempList);
						}
					}
					if (travelerList.size() <= 0) {
						resobj.put("flag", "false");
						resobj.put("warning", "此团期没有对应的游客信息");
					} else {
						resobj.put("flag", "true");
					}
				} else {
					resobj.put("flag", "true");
				}
			} else {
				resobj.put("flag", "false");
				resobj.put("warning", "此团期没有对应的订单");
			}
		} else {
			resobj.put("flag", "false");
			resobj.put("warning", "没有团期ID");
		}
		results.add(resobj);
		return results;
	}

	/**
	 * 校验团空单
	 * 
	 * @param groupId
	 * @param orderType
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "existExportGroupData", method = RequestMethod.POST)
	public Object existExportGroupData(
			@RequestParam(value = "groupId") String groupId,
			@RequestParam(value = "orderType") String orderType,
			@RequestParam(value = "orderIds") String orderIds) {
		JSONArray results = new JSONArray();
		JSONObject resobj = new JSONObject();
		if (groupId != null && !"".equals(groupId)) {
			if (orderIds != null && !"".equals(orderIds)) {
				resobj.put("flag", "true");
			} else {
				resobj.put("flag", "false");
				resobj.put("warning", "没有订单");
			}
		} else {
			resobj.put("flag", "false");
			resobj.put("warning", "没有团期ID");
		}
		results.add(resobj);
		return results;
	}

	/**
	 * 导出团期对应游客信息
	 * 
	 * @param groupId
	 */
	@RequestMapping(value = "exportExcel", method = RequestMethod.POST)
	public void exportExcel(@RequestParam(value = "groupId") String groupId,
			@RequestParam(value = "orderType") String orderType,
			@RequestParam(value = "groupCode") String groupCode,
			HttpServletRequest request, HttpServletResponse response) {
		List<ProductOrderCommon> productOrderList = null; // 团期产品id
		List<Object[]> travelerList = new ArrayList<Object[]>();
		// 2016年4月12日 update by pengfei.shang 需求0228
		if ("dfafad3ebab448bea81ca13b2eb0673e".equals(UserUtils.getUser()
				.getCompany().getUuid())) {
			try {
				if (groupId != null && !"".equals(groupId)) {
					productOrderList = orderService
							.findByProductGroupIdOrderByCompany(Long
									.parseLong(groupId));
				}
				if (productOrderList != null && productOrderList.size() > 0) {
					List<Object[]> tempList = new ArrayList<Object[]>();
					int i = 0;
					for (ProductOrderCommon productOrder : productOrderList) {
						tempList = orderService.selectRdhytTravelerByOrderId(
								productOrder.getId(),
								Integer.parseInt(orderType));
						if (tempList != null && tempList.size() > 0) {
							if (i == 0) {
								/** 第一行数据领队以后数据置为空 */
								Object[] oneLine = new Object[21];
								oneLine[0] = tempList.get(0)[0];
								oneLine[1] = tempList.get(0)[1];
								oneLine[2] = tempList.get(0)[2];
								oneLine[3] = tempList.get(0)[3];
								oneLine[4] = tempList.get(0)[4];
								oneLine[5] = tempList.get(0)[5];
								oneLine[6] = "领队";
								travelerList.add(oneLine);
							}
							for (Object[] o : tempList) {
								o[0] = "";
								o[1] = "";
								o[2] = "";
								o[3] = "";
								o[4] = "";
								o[5] = "";
								o[6] = ++i;
								String temp = null;
								if (o[17] != null
										&& o[17].toString().length() >= 1) {
									temp = o[17].toString();
									temp = temp.replace("1", "因公护照").replace(
											"2", "因私护照");
									o[17] = temp;
								}
							}
							travelerList.addAll(tempList);
						}
					}
				}

				// 文件名称
				String fileName = groupCode + "-游客信息";
				// Excel各行名称
				String[] cellTitle = { "组团社序号", "团队编号", "年份", "领队姓名", "领队证号",
						"编号", "序号", "中文姓名", "英文/拼音", "性别", "出生日期", "出生地",
						"身份证号码", "护照号码", "发证机关", "发证日期", "护照有效期", "护照类型",
						"电话号码", "渠道名字", "渠道跟进销售", "备注" };
				// 文件首行标题
				String firstTitle = groupCode;
				ExportExcel.createExcle(fileName, travelerList, cellTitle,
						firstTitle, request, response);
			} catch (Exception e) {
				logger.error("团期导出信息出错");
				e.printStackTrace();
			}
		} else {
			try {
				if (groupId != null && !"".equals(groupId)) {
					// 获取订单信息
					productOrderList = orderService
							.findByProductGroupIdOrderByCompany(Long
									.parseLong(groupId));
				}
				if (productOrderList != null && productOrderList.size() > 0) {
					List<Object[]> tempList = new ArrayList<Object[]>();
					int i = 0;
					for (ProductOrderCommon productOrder : productOrderList) {
						// 获取游客信息
						tempList = orderService.selectTravelerByOrderId(
								productOrder.getId(),
								Integer.parseInt(orderType));
						if (tempList != null && tempList.size() > 0) {
							for (Object[] o : tempList) {
								i++;
								o[0] = i;
								String temp = null;
								if (o[9] != null
										&& o[9].toString().length() >= 1) {
									temp = o[9].toString();
									temp = temp.replace("1", "因公护照").replace(
											"2", "因私护照");
									o[9] = temp;
								}
							}
							travelerList.addAll(tempList);
						}
					}
				}

				// 文件名称
				String fileName = groupCode + "-游客信息";
				// Excel各行名称
				String[] cellTitle = { "序号", "中文姓名", "英文姓名", "性别", "出生日期",
						"出生地", "身份证号码", "护照号码", "护照有效期", "护照类型", "电话号码",
						"渠道名字", "渠道跟进销售", "备注" };
				// 文件首行标题
				String firstTitle = groupCode;
				ExportExcel.createExcle(fileName, travelerList, cellTitle,
						firstTitle, request, response);
			} catch (Exception e) {
				logger.error("团期导出信息出错");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 在订单页面导出团控单
	 * 
	 * @param groupId
	 * @param request
	 * @param response
	 * @author zhanyu.gu
	 * @updateTime 2016-11-02
	 */
	@RequestMapping(value = "exportActivityGroupExcel", method = RequestMethod.POST)
	public void exportActivityGroupExcel(
			@RequestParam(value = "groupId") String groupId,
			@RequestParam(value = "orderType") String orderType,
			@RequestParam(value = "groupCode") String groupCode,
			@RequestParam(value = "orderIds") String orderIds,
			HttpServletRequest request, HttpServletResponse response) {
		List<ProductOrderCommon> productOrderList = new ArrayList<ProductOrderCommon>(); // 团期产品id
		List<Object[]> travelerList = new ArrayList<Object[]>();
		String[] countrys = null;
		try {
			if (groupId != null && !"".equals(groupId)) {
				String[] allorderIds = orderIds.split(" ");
				
				//将订单按照销售进行分组排序
				List<Object> productIds = orderService.findProductIdByProductGroupIdOrderByCompany(Long.parseLong(groupId));
				List<Long> orderIdList = new ArrayList<Long>();
				for(int a = 0; a < productIds.size(); a++){
					Object object = productIds.get(a);
					String[] string = object.toString().split(",");
					for(int j = 0; j< string.length; j++){
						String string2 = string[j];
						orderIdList.add(Long.parseLong(string2) );
					}
				}
				//根据登陆人，进行有选择输出
				List<Long> orderIdsList = new ArrayList<Long>();
				for(int j = 0; j < orderIdList.size(); j++){
					for(int i = 0; i< allorderIds.length ;i++){
					long parseLongid = Long.parseLong(allorderIds[i]); 
						if(parseLongid == orderIdList.get(j)){
							orderIdsList.add(orderIdList.get(j));
						} 
					}
				}
					
				// 获取订单信息
				for(Long orderId :orderIdsList){
					ProductOrderCommon productorderById = orderService.getProductorderById(orderId);
					productOrderList.add(productorderById);
				}
				
				/*productOrderList = orderService
						.findByProductGroupIdOrderByCompany(Long
								.parseLong(groupId));*/
			}
			if (productOrderList != null && productOrderList.size() > 0) {
				List<Object[]> tempList = new ArrayList<Object[]>();
				int i = 0;
				for (ProductOrderCommon productOrder : productOrderList) {
					//获取产品ID
					Long productId = productOrder.getProductId();
					TravelActivity travelActivity = travelActivityService.findById(productId);
					List<Area> targetAreaList = travelActivity.getTargetAreaList();
					countrys = new String[targetAreaList.size()];
					for(int j =0; j < targetAreaList.size(); j++ ){
						countrys[j] = targetAreaList.get(j).getName();
					}
					// 订单人数
					int orderPersonNum = productOrder.getOrderPersonNum();
					// 获取游客信息
					tempList = orderService.selectTravelerAndOrderByOrderId(
							productOrder.getId(), Integer.parseInt(orderType));
					if(!tempList.isEmpty()){
					
						// 判断订单人数与游客人数是否相等，如果不相等，则需要添加空白游客的信息
						if (orderPersonNum <= tempList.size()) {
							if (tempList != null && tempList.size() > 0) {
								for (Object[] o : tempList) {
									i++;
									o[1] = "";
								}
								travelerList.addAll(tempList);
							}
						} else {
							// 缺少的游客数量
							int a = orderPersonNum - tempList.size();
							for (int m = 0; m < a; m++) {
								Object[] obj1 = tempList.get(0);
								obj1[1] = "";
								Object[] obj2 = new Object[obj1.length];
								obj2[0] = obj1[0];
								obj2[1] = "";
								
								tempList.add(obj2);
							}
							travelerList.addAll(tempList);
						}
					}else{
						//当没有游客信息时
						for(int a =0;a < orderPersonNum; a++){
							Object[] obj = new Object[14];
							obj[0] = productOrder.getSalerName();
							obj[1] = "";
							
							tempList.add(obj);
						}
						travelerList.addAll(tempList);
					}
				}
			}

			// 文件名称
			String fileName = groupCode + "-团控单";
			// 生成excel表格
			ExportExcel.newExportExcel(fileName, groupCode,countrys, travelerList,
					request, response);
		} catch (Exception e) {
			logger.error("团期导出信息出错");
			e.printStackTrace();
		}

		
/*		  productOrderList =
		  orderService.findByProductGroupIdOrderByCompany(Long
		  .parseLong(groupId)); //根据团期的id查询订单的ids 
		  orderIds = getOrderIds(groupId, response, request);
		 
		 String[] split = orderIds.split(" "); List<Long> ordersID = new
		  ArrayList<Long>();
		  
		 for(int i = 0; i < split.length; i++ ){ Long a = Long.parseLong(
		 split[i]); for(ProductOrderCommon porductOrder :productOrderList){
		  Long id = porductOrder.getId(); if(id.equals(a) || id == a ){
		  ordersID.add(a); } } }
		  
		  List<Object[]> activityList = new ArrayList<Object[]>(); try{
		  //查询团号、同行价及领队的信息 activityList =
		  orderService.selectActivityGroupTopByActivityId
		  (Long.parseLong(groupId));
		 
		 //查询销售及渠道，游客的信息 List<Object[]> tempList = new ArrayList<Object[]>();
		  for(Long oid : ordersID){ ProductOrderCommon order =
		  orderService.getProductorderById(oid); 
		  tempList =orderService.selectActivityGroupByActivityId
		  (Long.parseLong(groupId),Integer.parseInt(orderType),oid); int
		  travelerSize = 0; if(activityList != null && activityList.size() >0
		  && tempList != null && tempList.size() > 0) {
		travelerList.addAll(tempList); travelerSize = tempList.size(); }
		  //判断游客人数 if (order.getOrderPersonNum().intValue() - travelerSize > 0)
		  { for (int i = 0; i < order.getOrderPersonNum().intValue() -
		  travelerSize; i++) { Object[] o = new Object[18]; o[0] =
		  tempList.get(0)[0]; o[1] = tempList.get(0)[1]; o[2] =
		  tempList.get(0)[2]; o[3] = tempList.get(0)[3]; o[4] =
		  tempList.get(0)[4]; o[16] = tempList.get(0)[16]; travelerList.add(o);
		  } } } //文件名称 String fileName = groupCode + "-团控单"; //生成excel表格
		 ExportExcel.createGroupExcel(fileName,activityList, travelerList,
		  request, response); }catch(Exception e){ logger.error("团控订单导出信息出错");
		  e.printStackTrace(); }*/
		 
	}

	/**
	 * 根据团期的id查询该团期下面的所有订单
	 * 
	 * @param groupId
	 * @param response
	 * @param request
	 * @return
	 */

	private String getOrderIds(String groupId, HttpServletResponse response,
			HttpServletRequest request) {
		String orderIds = "";
		List<String> groupIdList = Lists.newArrayList();
		// 按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("order",
				null);

		ActivityGroup group = activityGroupDao.findOne(Long.parseLong(groupId));
		groupIdList.add(group.getId().toString());

		// 查询条件
		Map<String, String> mapRequest = Maps.newHashMap();

		mapRequest.put("orderNumOrGroupCode", group.getGroupCode());
		mapRequest.put("orderOrGroup", "group");
		mapRequest.put("orderBy", "groupOpenDate DESC");

		String orderStatus = group.getTravelActivity().getActivityKind()
				.toString();
		TravelActivity travelActivity = new TravelActivity();

		Page<Map<Object, Object>> page = new Page<Map<Object, Object>>(request,
				response);
		page.setPageSize(Integer.MAX_VALUE);

		// 订单或团期查询
		orderListService.findOrderListByPayType("0", orderStatus, page,
				travelActivity, mapRequest, common);
		List<Map<Object, Object>> orderList = orderListService.findByGroupIds(
				new Page<Map<Object, Object>>(request, response), groupIdList,
				mapRequest.get("orderSql")).getList();
		for (Map<Object, Object> listin : orderList) {
			orderIds += listin.get("id").toString() + " ";
		}
		return orderIds;
	}

	// @RequiresPermissions("group:manager:delete")
	@RequestMapping(value = "delgroup", method = RequestMethod.POST)
	public String delgroup(
			@RequestParam(value = "srcActivityId", required = false, defaultValue = "") String srcActivityId,
			@RequestParam(value = "id", required = false, defaultValue = "") String id)
			throws Exception {

		ActivityGroup activityGroup = activityGroupService.findById(Long
				.parseLong(id));
		activityGroupService.delGroup(activityGroup);
		TravelActivity travelActivity = travelActivityService.findById(Long
				.parseLong(srcActivityId));
		Set<ActivityGroup> groups = travelActivity.getActivityGroups();
		ActivityGroup group = new ActivityGroup();
		if (groups != null && groups.size() != 0) {
			Date[] dates = new Date[groups.size()];
			Iterator<ActivityGroup> ite = groups.iterator();
			int i = 0;
			while (ite.hasNext()) {
				group = ite.next();
				dates[i] = group.getGroupOpenDate();
				i++;

			}
			travelActivity.setGroupOpenDate(TravelActivityUtil.getMinDate(null,
					dates));
			travelActivity.setGroupCloseDate(TravelActivityUtil.getMaxDate(
					null, dates));
		} else {
			travelActivity.setGroupOpenDate(null);
			travelActivity.setGroupCloseDate(null);
		}
		travelActivityService.save(travelActivity);
		return "redirect:" + Global.getAdminPath()
				+ "/activity/manager/?repage";
	}

	// @RequiresPermissions("group:manager:delete")
	@ResponseBody
	@RequestMapping(value = "delgroup2/{activityStatus}", method = RequestMethod.GET)
	public Object delgroup2(
			@PathVariable String activityStatus,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,
			@RequestParam(value = "proId", required = false, defaultValue = "") String proId)
			throws Exception {

		Map<String, String> data = new HashMap<String, String>();
		ActivityGroup activityGroup = activityGroupService.findById(Long
				.parseLong(id));

		activityGroupService.delGroup(activityGroup);
		TravelActivity travelActivity = travelActivityService.findById(Long
				.parseLong(proId));
		Set<ActivityGroup> groups = travelActivity.getActivityGroups();
		ActivityGroup group = new ActivityGroup();

		if (groups != null && groups.size() != 0) {
			Date[] dates = new Date[groups.size()];
			BigDecimal[] adultprices = new BigDecimal[groups.size()];
			BigDecimal[] suggestprices = new BigDecimal[groups.size()];
			Iterator<ActivityGroup> ite = groups.iterator();
			int i = 0;
			while (ite.hasNext()) {
				group = ite.next();
				// 团期比较最小值的时候，不应该把要删除的团期计算在内
				if (String.valueOf(group.getId()).equals(id)) {
					continue;
				} else {
					dates[i] = group.getGroupOpenDate();
					adultprices[i] = group.getSettlementAdultPrice();
					suggestprices[i] = group.getSuggestAdultPrice();
					i++;
				}
			}
			// 处理 产品 同行价 和 直客 价显示 后 币种符号显示不正确的问题
			BigDecimal settlementAdultMinPrice = TravelActivityUtil
					.getMinPrice(null, adultprices);
			BigDecimal suggestAdultMinPrice = TravelActivityUtil.getMinPrice(
					null, suggestprices);
			List<ActivityGroup> list = new ArrayList<ActivityGroup>(
					travelActivity.getActivityGroups());
			// 当产品没有団期时,币种类型为null 执行split方法报错,放到for里面处理
			// String[] str = travelActivity.getCurrencyType().split(",");
			String suggestAdultCurrencyType = "";
			for (ActivityGroup activityGroups : list) {
				if (activityGroup != null) {
					String[] str = travelActivity.getCurrencyType().split(",");
					// 当没有填写价格时，值为null 调用compareTo时报错，针对该情况加个判断
					if (activityGroup.getSettlementAdultPrice() != null) {
						if (settlementAdultMinPrice.compareTo(activityGroups
								.getSettlementAdultPrice()) == 0) {
							String settlementAdultCurrencyType = activityGroups
									.getCurrencyType();
							str[0] = settlementAdultCurrencyType.split(",")[0];
							String nameFlag = CurrencyUtils
									.getCurrencyNameOrFlag(
											Long.parseLong(str[0]), "0");
							data.put("settlementAdultPriceCMark", nameFlag);
							if (activityGroups.getSuggestAdultPrice() == null) {
								suggestAdultCurrencyType = travelActivity
										.getCurrencyType();
								data.put("suggestAdultPriceCMark", "");
							} else if (suggestAdultMinPrice
									.compareTo(activityGroups
											.getSuggestAdultPrice()) == 0) {
								suggestAdultCurrencyType = activityGroups
										.getCurrencyType();
								if (Context.ACTIVITY_KINDS_SP
										.equals(travelActivity
												.getActivityKind().toString())) {
									if (str != null) {
										str[1] = suggestAdultCurrencyType
												.split(",")[3];
									}
								} else if (Context.ACTIVITY_KINDS_YL
										.equals(travelActivity
												.getActivityKind().toString())) {
									if (str != null) {
										str[1] = suggestAdultCurrencyType
												.split(",")[2];
									}
								} else {
									if (str != null) {
										travelActivity.setCurrencyType(str[0]);
									}
								}
								String currencyFlag = CurrencyUtils
										.getCurrencyNameOrFlag(
												Long.parseLong(str[1]), "0");
								data.put("suggestAdultPriceCMark", currencyFlag);
								break;
							}
						}
					}
				}
			}
			travelActivity.setSettlementAdultPrice(TravelActivityUtil
					.getMinPrice(null, adultprices));
			travelActivity.setSuggestAdultPrice(TravelActivityUtil.getMinPrice(
					null, suggestprices));
			travelActivity.setGroupOpenDate(TravelActivityUtil.getMinDate(null,
					dates));
			travelActivity.setGroupCloseDate(TravelActivityUtil.getMaxDate(
					null, dates));
		} else {
			travelActivity.setSettlementAdultPrice(new BigDecimal(0));
			travelActivity.setSuggestAdultPrice(new BigDecimal(0));
			travelActivity.setGroupOpenDate(null);
			travelActivity.setGroupCloseDate(null);
		}
		travelActivityService.save(travelActivity);
		data.put("flag", "success");
		if (travelActivity.getGroupOpenDate() != null
				&& travelActivity.getGroupCloseDate() != null) {
			data.put("groupOpenDate", DateUtils.formatDate(
					travelActivity.getGroupOpenDate(), "yyyy-MM-dd"));
			data.put("groupCloseDate", DateUtils.formatDate(
					travelActivity.getGroupCloseDate(), "yyyy-MM-dd"));
		}
		if (travelActivity.getSettlementAdultPrice().intValue() != 0)
			data.put("settlementAdultPrice", String.valueOf(travelActivity
					.getSettlementAdultPrice().intValue()));
		if (travelActivity.getSuggestAdultPrice().intValue() != 0)
			data.put("suggestAdultPrice", String.valueOf(travelActivity
					.getSuggestAdultPrice().intValue()));
		return data;
	}

	@RequestMapping(value = "savegroup", method = RequestMethod.POST)
	public String savegroup(
			@RequestParam(value = "srcActivityId", required = false, defaultValue = "") String srcActivityId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TravelActivity travelActivity;
		List<ActivityGroup> grouplist = new ArrayList<ActivityGroup>();

		String[] groupids = request.getParameterValues("groupid");
		String[] groupOpenDates = request.getParameterValues("groupOpenDate");
		String[] groupCloseDates = request.getParameterValues("groupCloseDate");
		String[] settlementAdultPrices = request
				.getParameterValues("settlementAdultPrice");
		String[] settlementcChildPrices = request
				.getParameterValues("settlementcChildPrice");
		String[] suggestAdultPrices = request
				.getParameterValues("suggestAdultPrice");
		String[] suggestChildPrices = request
				.getParameterValues("suggestChildPrice");
		String[] payDeposits = request.getParameterValues("payDeposit");
		// 单房差
		String[] singleDiffs = request.getParameterValues("singleDiff");
		String[] planPositions = request.getParameterValues("planPosition");
		String[] freePositions = request.getParameterValues("freePosition");
		String[] visaDates = request.getParameterValues("visaDate");

		if (groupids != null) {
			int len = groupids.length;
			for (int i = 0; i < len; i++) {

				ActivityGroup group = activityGroupService.findById(Long
						.parseLong(groupids[i]));
				group.setGroupOpenDate(DateUtils.parseDate(groupOpenDates[i]));
				group.setGroupCloseDate(DateUtils.parseDate(groupCloseDates[i]));
				group.setSettlementAdultPrice(new BigDecimal(
						settlementAdultPrices[i]));
				group.setSettlementcChildPrice(new BigDecimal(
						settlementcChildPrices[i]));
				group.setSuggestAdultPrice(new BigDecimal(suggestAdultPrices[i]));
				group.setSuggestChildPrice(new BigDecimal(suggestChildPrices[i]));
				group.setPayDeposit(new BigDecimal(payDeposits[i]));
				group.setSingleDiff(new BigDecimal(singleDiffs[i]));
				group.setPlanPosition(StringNumFormat
						.getIntegerValue(planPositions[i]));
				group.setFreePosition(StringNumFormat
						.getIntegerValue(freePositions[i]));
				group.setVisaDate(DateUtils.parseDate(visaDates[i]));
				group.setVisaCountry("");
				grouplist.add(group);
			}
		}

		activityGroupService.saveGroups(grouplist);
		travelActivity = travelActivityService.findById(Long
				.parseLong(srcActivityId));
		Set<ActivityGroup> groups = travelActivity.getActivityGroups();
		if (groups != null && groups.size() != 0) {
			Date[] dates = new Date[groups.size()];
			Iterator<ActivityGroup> ite = groups.iterator();
			int i = 0;
			while (ite.hasNext()) {
				ActivityGroup group = ite.next();
				if (group.getGroupOpenDate() != null) {
					dates[i] = group.getGroupOpenDate();
					i++;
				}
			}
			travelActivity.setGroupOpenDate(TravelActivityUtil.getMinDate(null,
					dates));
			travelActivity.setGroupCloseDate(TravelActivityUtil.getMaxDate(
					null, dates));
		} else {
			travelActivity.setGroupOpenDate(null);
			travelActivity.setGroupCloseDate(null);
		}
		travelActivityService.save(travelActivity);
		return "redirect:" + Global.getAdminPath()
				+ "/activity/manager/?repage";
	}

	/**
	 * 判断是否修改了同行价或直客价
	 * 
	 * @author jiawei.du
	 * @param flag
	 * @param map
	 * @return flag
	 */
	public Integer isModPrice(Integer flag, Map map, ActivityGroup group) {
		if (group.getSettlementAdultPrice() != null) {
			if (!group.getSettlementAdultPrice().toString()
					.equals(map.get("settlementAdultPrice"))) {
				flag++;
			}
		} else {
			if (map.get("settlementAdultPrice") != null) {
				flag++;
			}
		}
		if (group.getSettlementcChildPrice() != null) {
			if (!group.getSettlementcChildPrice().toString()
					.equals(map.get("settlementcChildPrice"))) {
				flag++;
			}
		} else {
			if (map.get("settlementcChildPrice") != null) {
				flag++;
			}
		}
		if (group.getSettlementSpecialPrice() != null) {
			if (!group.getSettlementSpecialPrice().toString()
					.equals(map.get("settlementSpecialPrice"))) {
				flag++;
			}
		} else {
			if (map.get("settlementSpecialPrice") != null) {
				flag++;
			}
		}
		if (group.getSuggestAdultPrice() != null) {
			if (!group.getSuggestAdultPrice().toString()
					.equals(map.get("suggestAdultPrice"))) {
				flag++;
			}
		} else {
			if (map.get("suggestAdultPrice") != null) {
				flag++;
			}
		}
		if (group.getSuggestChildPrice() != null) {
			if (!group.getSuggestChildPrice().toString()
					.equals(map.get("suggestChildPrice"))) {
				flag++;
			}
		} else {
			if (map.get("suggestChildPrice") != null) {
				flag++;
			}
		}
		if (group.getSuggestSpecialPrice() != null) {
			if (!group.getSuggestSpecialPrice().toString()
					.equals(map.get("suggestSpecialPrice"))) {
				flag++;
			}
		} else {
			if (map.get("suggestSpecialPrice") != null) {
				flag++;
			}
		}
		return flag;
	}

	/**
	 * @param srcActivityId
	 * @param request
	 * @param response
	 * @return Object
	 * @throws Exception
	 * @Description: 产品 展开 异步修改 团号
	 * @date 2016年3月22日下午8:26:12 重构于2016年09月02日 需求518
	 */
	@ResponseBody
	@RequestMapping(value = "savegroup2/{srcActivityId}", method = RequestMethod.POST)
	public Object savegroup2(@PathVariable String srcActivityId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Map<String, Object> datas = new HashMap<String, Object>();
		// 获取参数
		String param = "groupid,recommend,groupOpenDate,groupCloseDate,groupCode,settlementAdultPrice,settlementcChildPrice,settlementSpecialPrice"
				+ ",settlementAdultCurrencyType,suggestAdultCurrencyType,suggestAdultPrice,suggestChildPrice,suggestSpecialPrice,maxChildrenCount,maxPeopleCount"
				+ ",payDeposit,visaCountry,singleDiff,planPosition,freePosition,invoiceTax,visaDate,groupRemark";
		Map<String, String> mapRequest = Maps.newHashMap();
		TravelActivityUtil.handlePara(param, mapRequest, request);
		String groupCode = mapRequest.get("groupCode");
		String groupid = mapRequest.get("groupid");
		// TravelActivity travelActivitySource =
		// travelActivityService.findById(Long.parseLong(srcActivityId));;

		// 获取团期剩余儿童人数、剩余特殊人数
		Map<String, Object> counts = activityGroupService
				.countOrderChildAndSpecialNum(new Long(groupid), null);
		if (StringUtils.isNotBlank(mapRequest.get("maxChildrenCount"))) {
			int orderPersonNumChild = counts.get("orderPersonNumChild") == null ? 0
					: new Integer(counts.get("orderPersonNumChild").toString());
			int maxChildrenCountTemp = new Integer(
					mapRequest.get("maxChildrenCount"));
			if (orderPersonNumChild > maxChildrenCountTemp) {
				datas.put("error", "errorChildNum");
				return datas;
			}
		}
		if (StringUtils.isNotBlank(mapRequest.get("maxPeopleCount"))) {
			int orderPersonNumSpecial = counts.get("orderPersonNumSpecial") == null ? 0
					: new Integer(counts.get("orderPersonNumSpecial")
							.toString());
			int maxPeopleCountTemp = new Integer(
					mapRequest.get("maxPeopleCount"));
			if (orderPersonNumSpecial > maxPeopleCountTemp) {
				datas.put("error", "errorSpecialNum");
				return datas;
			}
		}

		TravelActivity travelActivity = travelActivityService.findById(Long
				.parseLong(srcActivityId));
		Integer activityTypeId = travelActivity.getActivityKind();// 产品类型ID
		// bug12826
		datas.put("activityKind", activityTypeId);
		Set<ActivityGroup> groups = travelActivity.getActivityGroups();
		ActivityGroup group = null;
		if (CollectionUtils.isNotEmpty(groups)
				&& CollectionUtils.isNotEmpty(groups)) {
			for (ActivityGroup activityGroup : groups) {
				if (Long.parseLong(mapRequest.get("groupid")) == activityGroup
						.getId()) {
					group = activityGroup;
				}
			}
		}

		// 判断是否修改了同行价或直客价 需求518
		int priceFlag = 0;
		if (Context.PRODUCT_TYPE_SAN_PIN.equals(travelActivity
				.getActivityKind().toString())) {
			priceFlag = isModPrice(priceFlag, mapRequest, group);
			if (priceFlag > 0) {
				group.setPricingStrategyStatus(Context.PRICING_NEED_RESET_STATUS);
				activityGroupService.updatePSStatusById(group.getId(),
						Context.PRICING_NEED_RESET_STATUS);
			}
		}
		// 将同行价和直客价的修改记录日志
		travelActivityService.setFreeLog(travelActivity, group, mapRequest);

		// 0524需求 团期余位变化,记录在团控板中
		Integer amount = StringNumFormat.getIntegerValue(mapRequest.get("freePosition")) - group.getFreePosition();
		if(0 != amount){
			String remarks = "余位从" + group.getFreePosition() + "调整为" + mapRequest.get("freePosition");
			groupControlBoardService.insertGroupControlBoard(3, Math.abs(amount), remarks, group.getId(), -1);
		}
		// 0524需求 团期余位变化,记录在团控板中

		/* 189优加国际,起航假期 团号-修改 */
		// 修改前的团号
		String oldGroupCode = group.getGroupCode();
		// bug12826 ----- begin -------
		datas.put("groupCodeOld", oldGroupCode);

		// 处理参数
		handleActivityGroupPara(mapRequest, group);

		/* 189 优加国际,起航假期 验证团号是否重复 */

		if ("7a81c5d777a811e5bc1e000c29cf2586".equals(UserUtils.getUser()
				.getCompany().getUuid()) // 优加国际
				|| "5c05dfc65cd24c239cd1528e03965021".equals(UserUtils
						.getUser().getCompany().getUuid()) // 起航假期
				|| "f5c8969ee6b845bcbeb5c2b40bac3a23".equals(UserUtils
						.getUser().getCompany().getUuid()) // 懿洋假期
				|| "7a81a26b77a811e5bc1e000c29cf2586".equals(UserUtils
						.getUser().getCompany().getUuid()) // 拉美途
				|| UserUtils.getUser().getCompany().getGroupCodeRuleDT() == 0) { // 对应需求
																					// c460
			// 新、旧团号不相等时，视为修改了团号，再走校验团号重复逻辑
			if (!groupCode.equals(oldGroupCode)) {
				boolean flag = groupService.groupCodeValidator(groupCode,
						groupid);
				if (flag) {
					datas.put("groupCode", "groupCodeRepeat");
					return datas;
				}
			}
		}

		Date[] dates = new Date[groups.size()];
		BigDecimal[] adultprices = new BigDecimal[groups.size()];
		BigDecimal[] suggestprices = new BigDecimal[groups.size()];

		if (groups != null && groups.size() != 0) {
			Iterator<ActivityGroup> ite = groups.iterator();
			int i = 0;
			while (ite.hasNext()) {
				group = ite.next();
				if (group.getGroupOpenDate() != null) {
					dates[i] = group.getGroupOpenDate();
					adultprices[i] = group.getSettlementAdultPrice();
					suggestprices[i] = group.getSuggestAdultPrice();
					i++;
				}
			}
			// 处理 产品 同行价 和 直客 价显示 后 币种符号显示不正确的问题
			BigDecimal adultminPrice = TravelActivityUtil.getMinPrice(null,
					adultprices);
			BigDecimal suggestminPrice = TravelActivityUtil.getMinPrice(null,
					suggestprices);

			String[] str = travelActivity.getCurrencyType().split(",");
			String currencyStr = null;
			List<ActivityGroup> groupList = activityGroupService
					.getGroupListByActivityId(Integer.parseInt(srcActivityId));
			if (adultminPrice.toString().equals(
					mapRequest.get("settlementAdultPrice"))) {
				datas.put("settlementAdultPriceCMark", "-1");
				Map<String, Object> map = CurrencyUtils
						.getCurrnecyIdByMarks(mapRequest
								.get("settlementAdultCurrencyType"));
				str[0] = map.get("currency_id").toString();

			} else {
				for (ActivityGroup activityGroup : groupList) {
					BigDecimal settlementAdultPrices = activityGroup
							.getSettlementAdultPrice();
					if (settlementAdultPrices != null
							&& adultminPrice.compareTo(settlementAdultPrices) == 0) {
						str[0] = activityGroup.getCurrencyType().split(",")[0];
						String nameFlag = CurrencyUtils.getCurrencyNameOrFlag(
								Long.parseLong(str[0]), "0");
						datas.put("settlementAdultPriceCMark", nameFlag);
						break;
					}
				}
			}

			if (10 == activityTypeId || 2 == activityTypeId) {

				if (suggestminPrice.toString().equals(
						mapRequest.get("suggestAdultPrice"))) {
					datas.put("suggestAdultPriceCMark", "-1");
					Map<String, Object> map = CurrencyUtils
							.getCurrnecyIdByMarks(mapRequest
									.get("suggestAdultCurrencyType"));
					if (str.length >= 2) {
						str[1] = map.get("currency_id").toString();
					}
				} else {
					for (ActivityGroup activityGroup : groupList) {
						BigDecimal suggestAdultPrices = activityGroup
								.getSuggestAdultPrice();
						if (suggestAdultPrices != null
								&& suggestminPrice
										.compareTo(suggestAdultPrices) == 0) {

							// 处理bug 14155
							if (10 == activityTypeId) {
								if (str.length >= 2) {
									str[1] = activityGroup.getCurrencyType()
											.split(",")[2];// 游轮
								}
							}
							if (2 == activityTypeId) {
								if (str.length >= 2) {
									str[1] = activityGroup.getCurrencyType()
											.split(",")[3];// 散拼
								}
							}

							String currencyFlag = null;
							if (str.length >= 2) {
								currencyFlag = CurrencyUtils
										.getCurrencyNameOrFlag(
												Long.parseLong(str[1]), "0");
							} else {
								currencyFlag = CurrencyUtils
										.getCurrencyNameOrFlag(
												Long.parseLong(str[0]), "0");
							}
							datas.put("suggestAdultPriceCMark", currencyFlag);
							break;
						}
					}
				}
				if (str.length < 2) {
					currencyStr = str[0];
				} else {
					currencyStr = str[0] + "," + str[1];
				}
			} else {
				currencyStr = str[0];
			}
			travelActivity.setCurrencyType(currencyStr);
			travelActivity.setSettlementAdultPrice(TravelActivityUtil
					.getMinPrice(null, adultprices));
			travelActivity.setSuggestAdultPrice(TravelActivityUtil.getMinPrice(
					null, suggestprices));
			travelActivity.setGroupOpenDate(TravelActivityUtil.getMinDate(null,
					dates));
			travelActivity.setGroupCloseDate(TravelActivityUtil.getMaxDate(
					null, dates));

			// 更新产品最近团号-----begin--------
			Set<ActivityGroup> activityGroupset = travelActivity
					.getActivityGroups();
			Map<Long, String> tempMap = new HashMap<Long, String>();
			Long minLong = 0l;
			int count = 0;
			for (ActivityGroup activityGroup : activityGroupset) {
				Long temp = activityGroup.getGroupOpenDate().getTime();
				if (count == 0) {
					minLong = temp;
				}
				count++;
				if (temp < minLong) {
					minLong = temp;
				}
				tempMap.put(temp, activityGroup.getGroupCode());
			}
			travelActivity.setGroupOpenCode(tempMap.get(minLong));
			// 更新产品最近团号-----end--------
		} else {
			travelActivity.setSettlementAdultPrice(new BigDecimal(0));
			travelActivity.setSuggestAdultPrice(new BigDecimal(0));
			travelActivity.setGroupOpenDate(null);
			travelActivity.setGroupCloseDate(null);
		}

		// travelActivitySource =
		// travelActivityService.findById(Long.parseLong(srcActivityId));
		// 将数据存库
		travelActivityService.save(travelActivity);
		// travelActivityService.setFreeLog(Long.parseLong(srcActivityId),mapRequest);
		/* 优加国际,起航假期 团号修改后产生团号记录 */
		if ("7a81c5d777a811e5bc1e000c29cf2586".equals(UserUtils.getUser()
				.getCompany().getUuid()) // 优加国际
				|| "5c05dfc65cd24c239cd1528e03965021".equals(UserUtils
						.getUser().getCompany().getUuid()) // 起航假期
				|| "f5c8969ee6b845bcbeb5c2b40bac3a23".equals(UserUtils
						.getUser().getCompany().getUuid()) // 懿洋假期
				|| "7a81a26b77a811e5bc1e000c29cf2586".equals(UserUtils
						.getUser().getCompany().getUuid()) // 拉美途
				|| UserUtils.getUser().getCompany().getGroupCodeRuleDT() == 0) { // 对应需求
																					// c460
			// 新、旧团号不相等时，视为修改了团号，新增团号库记录并更新审批表中的团号
			if (!groupCode.equals(oldGroupCode)) {
				// bug12826 ----- begin -------
				/* 游轮产品新增舱型产生的不同团期，团号修改时同步修改(新增舱型団期的团号要保持一致) */
				// 查询同一产品下的团号相同(仅游轮产品)団期的id
				List<Object> groupIdList = groupService.getGroupIdByGroupCode(
						groupCode, oldGroupCode, srcActivityId,
						mapRequest.get("groupOpenDate"));
				for (Object o : groupIdList) {
					GroupcodeModifiedRecord groupcodeModifiedRecord = new GroupcodeModifiedRecord();
					groupcodeModifiedRecord.setCreateBy(UserUtils.getUser()
							.getId().intValue());
					groupcodeModifiedRecord.setGroupcodeNew(groupCode);
					groupcodeModifiedRecord.setGroupcodeOld(oldGroupCode);
					groupcodeModifiedRecord.setActivityGroupId((Integer) o);
					groupcodeModifiedRecord.setProductId(Integer
							.valueOf(srcActivityId));
					groupcodeModifiedRecord.setUpdateByName(UserUtils.getUser()
							.getName());
					groupcodeModifiedRecord.setProductType(activityTypeId);
					groupcodeModifiedRecordDao.saveObj(groupcodeModifiedRecord);

					String sql = "update review_new set group_code=? where group_code=? and group_id=? and company_id='"
							+ UserUtils.getUser().getCompany().getUuid() + "'";
					groupcodeModifiedRecordDao.updateBySql(sql, groupCode,
							oldGroupCode, o);

				}

				// 同步更新游轮新增舱型产生的相同出团日期的团号
				String sql1 = "UPDATE activitygroup SET groupCode=? WHERE groupCode=? AND srcActivityId=? AND groupOpenDate=? AND delFlag=0";
				groupcodeModifiedRecordDao.updateBySql(sql1, groupCode,
						oldGroupCode, srcActivityId,
						mapRequest.get("groupOpenDate"));
				// ---------- end ----------

				// 更新发票团号
				activityGroupDao
						.updateBySql("update   orderinvoice o LEFT JOIN sys_user su  ON o.createBy = su.id  "
								+ "SET o.groupCode='"
								+ groupCode
								+ "' WHERE o.groupCode='"
								+ oldGroupCode
								+ "' AND su.companyId="
								+ UserUtils.getUser().getCompany().getId());
				// 更新收据团号
				activityGroupDao
						.updateBySql("update   orderreceipt o LEFT JOIN sys_user su  ON o.createBy = su.id  "
								+ "SET o.groupCode='"
								+ groupCode
								+ "' WHERE o.groupCode='"
								+ oldGroupCode
								+ "' AND su.companyId="
								+ UserUtils.getUser().getCompany().getId());

			}
		}

		// datas.put("priceFlag",priceFlag);

		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "activityGroupReserve",
				"activityGroupReserveList", "travelActivity", "createBy",
				"updateBy", "createDate", "delFlag", "updateDate",
				"pricingStrategy" });
		config.registerJsonValueProcessor(java.util.Date.class,
				new DateJsonValueConfig());
		ActivityGroup tmp = activityGroupService.findById(Long
				.parseLong(groupid));
		datas.put("group", JSONObject.fromObject(tmp, config));
		if (travelActivity.getGroupCloseDate() != null
				&& travelActivity.getGroupOpenDate() != null) {
			datas.put("groupopendate", DateUtils.formatDate(
					travelActivity.getGroupOpenDate(), "yyyy-MM-dd"));
			datas.put("groupclosedate", DateUtils.formatDate(
					travelActivity.getGroupCloseDate(), "yyyy-MM-dd"));
		}

		datas.put("settlementadultprice",
				travelActivity.getSettlementAdultPrice());
		datas.put("suggestadultprice", travelActivity.getSuggestAdultPrice());

		return datas;
	}

	/**
	 * 参数处理：用mapRequest取值、放入group,datas中
	 * 
	 * @param mapRequest
	 * @param group
	 * @author jiawei.du
	 */
	public void handleActivityGroupPara(Map<String, String> mapRequest,
			ActivityGroup group) {
		// 推荐
		if (StringUtils.isNotBlank(mapRequest.get("recommend"))) {
			group.setRecommend(Integer.valueOf(mapRequest.get("recommend")));
		} else {
			group.setRecommend(null);
		}

		// 开团日期

		if (StringUtils.isNotBlank(mapRequest.get("groupOpenDate"))) {
			group.setGroupOpenDate(DateUtils.parseDate(mapRequest
					.get("groupOpenDate")));
		} else {
			group.setGroupOpenDate(null);
		}

		// 截团日期
		if (StringUtils.isNotBlank(mapRequest.get("groupCloseDate"))) {
			group.setGroupCloseDate(DateUtils.parseDate(mapRequest
					.get("groupCloseDate")));
			// group.setGroupRemark(mapRequest.get("groupCloseDate"));
		} else {
			group.setGroupRemark(null);
		}

		// 备注
		if (StringUtils.isNotBlank(mapRequest.get("groupRemark"))) {
			group.setGroupRemark(mapRequest.get("groupRemark"));
		} else {
			group.setGroupRemark(null);
		}

		// 团号可修改的批发商
		if ("7a81c5d777a811e5bc1e000c29cf2586".equals(UserUtils.getUser()
				.getCompany().getUuid())// 优加国际
				|| "5c05dfc65cd24c239cd1528e03965021".equals(UserUtils
						.getUser().getCompany().getUuid()) // 起航假期
				|| "f5c8969ee6b845bcbeb5c2b40bac3a23".equals(UserUtils
						.getUser().getCompany().getUuid()) // 懿洋假期
				|| "7a81a26b77a811e5bc1e000c29cf2586".equals(UserUtils
						.getUser().getCompany().getUuid()) // 拉美途
				|| UserUtils.getUser().getCompany().getGroupCodeRuleDT() == 0) { // 对应需求
																					// c460
			group.setGroupCode(mapRequest.get("groupCode"));
		}
		// 同行价
		if (StringUtils.isNotBlank(mapRequest.get("settlementAdultPrice"))) {
			group.setSettlementAdultPrice(new BigDecimal(mapRequest
					.get("settlementAdultPrice")));
		} else {
			group.setSettlementAdultPrice(null);
		}
		if (StringUtils.isNotBlank(mapRequest.get("settlementcChildPrice"))) {
			group.setSettlementcChildPrice(new BigDecimal(mapRequest
					.get("settlementcChildPrice")));
		} else {
			group.setSettlementcChildPrice(null);
		}
		if (StringUtils.isNotBlank(mapRequest.get("settlementSpecialPrice"))) {
			group.setSettlementSpecialPrice(new BigDecimal(mapRequest
					.get("settlementSpecialPrice")));
		} else {
			group.setSettlementSpecialPrice(null);
		}
		// 直客价
		if (StringUtils.isNotBlank(mapRequest.get("suggestAdultPrice"))) {
			group.setSuggestAdultPrice(new BigDecimal(mapRequest
					.get("suggestAdultPrice")));
		} else {
			group.setSuggestAdultPrice(null);
		}
		if (StringUtils.isNotBlank(mapRequest.get("suggestChildPrice"))) {
			group.setSuggestChildPrice(new BigDecimal(mapRequest
					.get("suggestChildPrice")));
		} else {
			group.setSuggestChildPrice(null);
		}
		if (StringUtils.isNotBlank(mapRequest.get("suggestSpecialPrice"))) {
			group.setSuggestSpecialPrice(new BigDecimal(mapRequest
					.get("suggestSpecialPrice")));
		} else {
			group.setSuggestSpecialPrice(null);
		}
		// group.setTrekizPrice(StringNumFormat.getIntegerValue(trekizPrice));
		// group.setTrekizChildPrice(StringNumFormat.getIntegerValue(trekizChildPrice));
		// 单房差
		if (StringUtils.isNotBlank(mapRequest.get("singleDiff"))) {
			group.setSingleDiff(new BigDecimal(mapRequest.get("singleDiff")));
		} else {
			group.setSingleDiff(null);
		}
		// 定金
		if (StringUtils.isNotBlank(mapRequest.get("payDeposit"))) {
			group.setPayDeposit(new BigDecimal(mapRequest.get("payDeposit")));
		} else {
			group.setPayDeposit(null);
		}

		// 预收
		group.setPlanPosition(StringNumFormat.getIntegerValue(mapRequest
				.get("planPosition")));
		// 余位
		group.setFreePosition(StringNumFormat.getIntegerValue(mapRequest
				.get("freePosition")));

		// 发票税（懿洋假期）
		if (StringUtils.isNoneBlank(mapRequest.get("invoiceTax"))) {
			group.setInvoiceTax(StringNumFormat.getBigDecimalValue(mapRequest
					.get("invoiceTax")));
		} else { // 如果发票税为空,则设置发票税的值为0
			group.setInvoiceTax(StringNumFormat.getBigDecimalValue("0"));
		}

		// 资料截止日期
		group.setVisaDate(DateUtils.parseDate(mapRequest.get("visaDate")));
		// 签证国家
		group.setVisaCountry(mapRequest.get("visaCountry"));
		// 儿童最高人数
		if (StringUtils.isNotBlank(mapRequest.get("maxChildrenCount"))) {
			group.setMaxChildrenCount(Integer.valueOf(mapRequest
					.get("maxChildrenCount")));
		} else {
			group.setMaxChildrenCount(null);
		}
		// 特殊人群最高人数
		if (StringUtils.isNotBlank(mapRequest.get("maxPeopleCount"))) {
			group.setMaxPeopleCount(Integer.valueOf(mapRequest
					.get("maxPeopleCount")));
		} else {
			group.setMaxPeopleCount(null);
		}

		// group.setPlusFreePosition(StringNumFormat.getIntegerValue(mapRequest.get("freePosition"))
		// - group.getFreePosition());
		// group.setPlusPlanPosition(StringNumFormat.getIntegerValue(mapRequest.get("planPosition"))
		// - group.getPlanPosition());
	}

	@ResponseBody
	@RequestMapping("groupOpenDateRepeat")
	public String groupOpenDateRepeat(
			@RequestParam(value = "groupOpenDate") String groupOpenDate,
			@RequestParam(value = "activityId") String activityId,
			@RequestParam(value = "groupid") String groupid) {
		if (!activityGroupService.groupOpenDateRepeat(activityId,
				groupOpenDate, groupid).isEmpty())
			return "false";
		return "true";
	}

	@ResponseBody
	@RequestMapping("checkAirTicketGroupCode")
	public Map<String, Object> checkAirTicketGroupCode(
			@RequestParam(value = "groupCode", required = false) String groupCode) {
		boolean flag = groupService.groupNoCheck(groupCode);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!flag) {
			map.put("result", "1");
		} else {
			map.put("result", "0");
		}
		return map;
	}

	@ResponseBody
	@RequestMapping("checkGroupeSurname")
	public Map<String, Object> checkGroupeSurname() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (UserUtils.getUser().getCompany().getUuid()
				.contains("7a8177e377a811e5bc1e000c29cf2586")) {
			User user = userDao.findById(UserUtils.getUser().getId());
			if (StringUtils.isBlank(user.getGroupeSurname())) {
				map.put("result", "0");
			} else {
				map.put("result", "1");
			}
		} else {
			map.put("result", "1");
		}
		return map;
	}

	@RequestMapping("uploadFileForm")
	public String uploadFileForm(
			@RequestParam(value = "docId", required = false) String docId,
			Model model) {
		if (StringUtils.isNotBlank(docId)) {
			model.addAttribute("docInfo",
					docInfoService.getDocInfo(StringUtils.toLong(docId)));
		}
		return "modules/activity/uploadform";
	}

	@RequestMapping("uploadGroupFile")
	public String uploadGroupFile(
			@RequestParam(value = "groupFile", required = false) MultipartFile groupFile,
			Model model) {
		String docId = activityGroupService.uploadGroupFile(groupFile);
		model.addAttribute("docId", docId);
		return "redirect:" + Global.getAdminPath()
				+ "/activity/manager/uploadFileForm";
	}

	/**
	 * TODO 获取所有产品信息
	 */
	@ResponseBody
	@RequestMapping(value = "getAllActivityInfo")
	public Map<String, Object> getAllActivityInfo(Model model,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		String activityTypeStr = request.getParameter("activityType"); // 产品类型

		if (StringUtils.isBlank(activityTypeStr)) {
			resultMap.put("flag", "faild");
			resultMap.put("message", "产品类型为空");
		}
		Integer activityType = Integer.parseInt(activityTypeStr);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("activityType", request.getParameter("activityType")); // 产品类型
		paramMap.put("groupCode", request.getParameter("groupCode")); // 团号
		paramMap.put("activityName", request.getParameter("activityName")); // 产品名称
		paramMap.put("groupOpenDate", request.getParameter("groupOpenDate")); // 出团日期
		paramMap.put("groupCloseDate", request.getParameter("groupCloseDate")); // 截团日期
		paramMap.put("creator", request.getParameter("creator")); // 创建者

		// 获取代替下单用户列表
		JSONArray actJson = activityGroupService.getAllActivityByType(paramMap);
		resultMap.put("flag", "success");
		resultMap.put("data", actJson);
		List<Map<String, Object>> createByList = activityGroupService
				.getAllCreators(activityType);
		resultMap.put("creator", createByList);
		return resultMap;
	}

	/**
	 * TODO 获取所有产品信息
	 */
	@ResponseBody
	@RequestMapping(value = "getActivityInfoByRemind")
	public Map<String, Object> getActivityInfoByRemind(Model model,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		String remindId = request.getParameter("remindId"); // 产品类型
		if (StringUtils.isBlank(remindId)) {
			resultMap.put("flag", "faild");
			return resultMap;
		}
		Remind remind = remindService.get(Integer.parseInt(remindId));
		if (remind == null) {
			resultMap.put("flag", "faild");
			return resultMap;
		}
		if (StringUtils.isBlank(remind.getSelectedRemindOrderType())) {
			resultMap.put("flag", "faild");
			return resultMap;
		}
		Integer activityType = Integer.parseInt(remind
				.getSelectedRemindOrderType());
		// 校验 产品、团期id
		// String activityIdStr = remind.getProductIds();
		// String groupIdStr = remind.getActivityGroupIds();
		// if (StringUtils.isBlank(activityIdStr) ||
		// StringUtils.isBlank(groupIdStr)) {
		// resultMap.put("flag", "faild");
		// return resultMap;
		// }

		// 获取产品创建者用户列表
		JSONArray actJson = activityGroupService
				.getActivityInfoByRemind(remind);
		resultMap.put("flag", "success");
		resultMap.put("data", actJson);
		List<Map<String, Object>> createByList = activityGroupService
				.getAllCreators(activityType);
		resultMap.put("creator", createByList);
		return resultMap;
	}

	/**
	 * TODO 获取所有产品信息
	 */
	@ResponseBody
	@RequestMapping(value = "createMsgRightNow")
	public Map<String, Object> createMsgRightNow(Model model,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		ireceivePayServiceImpl.getRemaindListByRefundDate();
		resultMap.put("flag", "success");
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "getGroupDetail")
	public Map<String, Object> getGroupDetail(Long groupId) {
		Map<String, Object> resultMap = new HashMap<>();
		ActivityGroup group = activityGroupDao.findOne(groupId);
		resultMap.put("num", group.getFreePosition());
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "getGroupPlanAndFreePositon")
	public Map<String, Object> getGroupPlanAndFreePositon(Long groupId) {
		Map<String, Object> resultMap = new HashMap<>();
		ActivityGroup group = activityGroupDao.findOne(groupId);
		resultMap.put("freePositon", group.getFreePosition());
		resultMap.put("planPositon", group.getPlanPosition());
		return resultMap;
	}

	/**
	 * 检查预收和余位的改变情况
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkGroupPlanAndFreePositonChange")
	public Map<String, Object> checkGroupPlanAndFreePositonChange(
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		// 数据处理
		String groupIds = request.getParameter("groupId");
		String proId = request.getParameter("proId");
		String plans = request.getParameter("plans");
		String frees = request.getParameter("frees");
		if (StringUtils.isNotBlank(plans) && StringUtils.isNotBlank(frees)
				&& StringUtils.isNotBlank(proId)) {
			String[] groupArray = groupIds.split(",");
			String[] planArray = plans.split(",");
			String[] freeArray = frees.split(",");
			List<ActivityGroup> groupList = activityGroupService
					.getGroupListByActivityId(Integer.parseInt(proId));
			for (int i = 0; i < groupList.size(); i++) {
				boolean deleted = true;
				for (int j = 0; j < groupArray.length; ++j) {
					String temp1 = groupArray[j];
					String temp2 = groupList.get(i).getId().toString();
					String temp3 = groupList.get(i).getGroupCode();
					if (temp1.equals(temp2)
							&& !planArray[j].equals(groupList.get(i)
									.getPlanPosition().toString())
							&& freeArray[j].equals(groupList.get(i)
									.getFreePosition().toString())) {
						// 只修改了预收数，没有修改余位数
						deleted = false;
						resultMap.put("changeGroupId", temp3);
						resultMap.put("changePlan", true);
						resultMap.put("data", "success");
						return resultMap;
					} else if (temp1.equals(temp2)
							&& !freeArray[j].equals(groupList.get(i)
									.getFreePosition().toString())
							&& planArray[j].equals(groupList.get(i)
									.getPlanPosition().toString())) {
						// 只修改了余位数，没有修改预收数时
						deleted = false;
						resultMap.put("changeGroupId", temp3);
						resultMap.put("changeFree", true);
						resultMap.put("data", "success");
						return resultMap;
					} else if (temp1.equals(temp2)) {
						deleted = false;
					}
				}
				// bug15341
				// if(deleted){
				// //检查被删除的团期是否有 订单
				// boolean hasOrder =
				// productOrderService.hasOrder(groupList.get(i).getId());
				// if(hasOrder){
				// resultMap.put("fialMsg",
				// "团号为"+groupList.get(i).getGroupCode()+"团期已经有占位，不能删除" );
				// resultMap.put("data", "fail" );
				// return resultMap;
				// }
				// }
			}

			// 预收数和余位数都不变化或都变化时
			resultMap.put("allOrNoChange", true);
			resultMap.put("data", "success");
		} else {
			resultMap.put("data", "fail");
		}
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "hasOrder")
	public Map<String, Object> hasOrder(HttpServletRequest request) {
		String groupId = request.getParameter("groupId");
		boolean hasOrder = false;
		if (!StringUtil.isBlank(groupId)) {
			hasOrder = productOrderService.hasOrder(new Long(groupId));

		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", hasOrder);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "findAllAgentName")
	public List<String> findAllAgentName() {
		/*
		 * List<Agentinfo> agentinfoList = agentinfoService.findAll();
		 * List<String> list = new ArrayList<>(); if (agentinfoList != null) {
		 * for (Agentinfo agentinfo : agentinfoList) { if
		 * ("1".equals(agentinfo.getIsQuauqAgent()) &&
		 * "1".equals(agentinfo.getEnableQuauqAgent())) {
		 * list.add(agentinfo.getAgentName()); } } }
		 */
		return agentinfoService.findAllQuauqAgentinfoName();
	}

	@ResponseBody
	@RequestMapping(value = "findPriceTable")
	public List<Map<String, String>> findPriceTable(HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, String>> data = Lists.newArrayList();
		String groupId = request.getParameter("groupId");
		String searchInput = request.getParameter("searchInputVal");
		if (StringUtils.isBlank(searchInput)) {
			return null;
		}
		Long groupid = null;
		BigDecimal quauqAdi = null;
		BigDecimal quauqChi = null;
		BigDecimal quauqSpe = null;
		// 查找该团期的信息
		if (StringUtils.isNotBlank(groupId)) {
			groupid = Long.parseLong(groupId);
		}
		ActivityGroup group = activityGroupService.findById(groupid);
		String[] currencyType = group.getCurrencyType().split(",");
		if (null != group) {
			// 成人quauq价
			quauqAdi = group.getQuauqAdultPrice();
			// 儿童quauq价
			quauqChi = group.getQuauqChildPrice();
			// 特殊人群quauq价
			quauqSpe = group.getQuauqSpecialPrice();
		}

		Rate rate = null;
		if (StringUtils.isNotBlank(searchInput)) {

			// 获取所有的quauq渠道
			List<Agentinfo> agentinfoList = agentinfoService
					.getAgentinfoListByName(searchInput, "1");
			for (Agentinfo agentinfo : agentinfoList) {

				BigDecimal supplyAdi = quauqAdi; 	 //成人
				BigDecimal supplyChi = quauqChi;	//儿童
				BigDecimal supplySpe = quauqSpe;	//特殊人群

				// 获取quauq渠道下的quauq费率和渠道费率
				rate = RateUtils.getRate(groupid,
						Context.ProductType.PRODUCT_LOOSE, agentinfo.getId());
				// if(quauqAdi == null && supplyChi == null && supplySpe ==
				// null){
				Map<String, String> map = new HashMap<String, String>();
				map.put("agentName", agentinfo.getAgentName());
				//计算成人供应价
				if (quauqAdi != null) {
					supplyAdi = OrderCommonUtil.getRetailPrice(group.getSettlementAdultPrice(), quauqAdi, rate, Long.parseLong(currencyType[0]));
					map.put("rateAdlut", supplyAdi.toString());
				} else {
					map.put("rateAdlut", "");
				}
				//计算儿童供应价
				if (supplyChi != null) {
					supplyChi = OrderCommonUtil.getRetailPrice(group.getSettlementcChildPrice(), quauqChi, rate, Long.parseLong(currencyType[1]));
					map.put("rateChild", supplyChi.toString());
				} else {
					map.put("rateChild", "");
				}
				//计算特殊人群供应价
				if (supplySpe != null) {
					supplySpe = OrderCommonUtil.getRetailPrice(group.getSettlementSpecialPrice(), quauqSpe, rate, Long.parseLong(currencyType[2]));
					map.put("rateSpecil", supplySpe.toString());
				} else {
					map.put("rateSpecil", "");
				}
				data.add(map);
				// }
			}
		}
		return data;
	}

	public BigDecimal getSupply(BigDecimal supply, BigDecimal quauq, Rate rate) {
		if (rate.getQuauqRate() != null) {
			// quauq渠道费率的类型判断：0为百分比 1为直减
			if (rate.getQuauqRateType() == 0) {
				supply = quauq.multiply(rate.getQuauqRate()).add(quauq);
			} else {
				supply = quauq.add(rate.getQuauqRate());
			}
		}
		if (rate.getAgentRate() != null) {
			// 渠道费率的判断：0 为百分比 1为直减
			if (rate.getAgentRateType() == 0) {
				supply = supply.add(quauq.multiply(rate.getAgentRate()));
			} else {
				supply = supply.add(rate.getAgentRate());
			}
		}
		return supply;
	}
}
