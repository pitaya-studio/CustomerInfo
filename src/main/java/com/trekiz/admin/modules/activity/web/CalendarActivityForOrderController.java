package com.trekiz.admin.modules.activity.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.ActivityGroupCompare;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupCompareService;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.order.service.ApplyOrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 旅游产品信息控制器
 * 
 * @author liangjingming
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/calendarforOrder")
public class CalendarActivityForOrderController extends BaseController {
	@Autowired
	private ActivityGroupCompareService activityGroupCompareService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private AgentinfoService agentinfoService;
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
    private SystemService systemService;

	@ModelAttribute("menuId")
	protected Integer getMenuId() {
		return 156;
	}

	/**
	 * @param showType
	 *            预定、预报名
	 * @param activityKind
	 *            产品种类：单团、散拼...
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list/{showType}/{activityKind}" })
	public String list(@PathVariable String showType,
			@PathVariable String activityKind,
			@ModelAttribute TravelActivity travelActivity,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		// 按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("bookOrder", model);

		// 查询条件
		Map<String, String> mapRequest = Maps.newHashMap();

		// 参数处理：起始同行价格、结束同行价格、产品种类、舱型、是否有余位、是否有切位
		String paras = "settlementAdultPriceStart,settlementAdultPriceEnd,orderNumOrGroupCode,wholeSalerKey,spaceType,haveYw,haveQw,activityCreateCalendar";
		OrderCommonUtil.handlePara(paras, mapRequest, model, request);

		String remainDays = request.getParameter("remainDays");// 保留天数
		String activityDuration = request.getParameter("activityDuration");
		String groupOpenDate = request.getParameter("groupOpenDate");
		String groupCloseDate = request.getParameter("groupCloseDate");
		String targetAreaNames = request.getParameter("targetAreaNameList");

		if (StringUtils.isNotBlank(remainDays))
			travelActivity.setRemainDays(Integer.parseInt(remainDays));
		else
			travelActivity.setRemainDays(null);
		String agentId = request.getParameter("agentId");// 获取渠道id
		travelActivity.setActivityStatus(2);// 只查询上架产品
		if (StringUtils.isNotBlank(activityKind)) {
			travelActivity.setActivityKind(Integer.valueOf(activityKind));// 产品种类
		}
		Page<TravelActivity> pages = new Page<TravelActivity>(request, response);
		String orderBy = request.getParameter("orderBy");
		// 排序方式
		if (StringUtils.isBlank(orderBy)) {
			orderBy = "groupOpenDate";
			pages.setOrderBy(orderBy);
		}
		Page<TravelActivity> page = travelActivityService.findTravelActivity(
				pages, travelActivity, common, mapRequest);
		if (page.getList() != null) {
			for (TravelActivity tmp : page.getList()) {
				Set<ActivityGroup> ags = tmp.getActivityGroups();
				if (CollectionUtils.isNotEmpty(ags)) {
					if (ags.size() > 1) {
						travelActivity.setId(tmp.getId());
						List<ActivityGroup> groupList = travelActivityService
								.findGroupsByActivityId(travelActivity,
										mapRequest);
						Set<ActivityGroup> groupSet = Sets.newHashSet();
						for (ActivityGroup ag : groupList) {
							List<Object[]> orderPersonNumList = applyOrderCommonService
									.sumOrderPersonNumByGroupId(ag.getId());
							Object[] orderPersonNumArr = orderPersonNumList
									.get(0);
							ag.setOrderPersonNum(Integer
									.parseInt(orderPersonNumArr[0].toString()));
							 //通过团期 id 和产品 id 来查找当前团期下 剩余的切位人数(即除了占切位的人数)
					    	 Integer leftpayReservePosition = travelActivityService.getAllLeftpayReservePosition(ag.getId(), tmp.getId());
					    	 //为每一个团期设置剩余的切位人数
					    	 ag.setLeftpayReservePosition(leftpayReservePosition);
							groupSet.add(ag);
						}
						tmp.setActivityGroups(groupSet);
					} else {
						Long activityGroupId = ags.iterator().next().getId();
			    		 //通过团期 id 和产品 id 来查找当前团期下 剩余的切位人数(即除了占切位的人数)
				    	 Integer leftpayReservePosition = travelActivityService.getAllLeftpayReservePosition(activityGroupId, tmp.getId());
				    	 //为每一个团期设置剩余的切位人数
						List<Object[]> orderPersonNumList = applyOrderCommonService
								.sumOrderPersonNumByGroupId(ags.iterator()
										.next().getId());
						Object[] orderPersonNumArr = orderPersonNumList.get(0);
						ags.iterator()
								.next()
								.setOrderPersonNum(
										Integer.parseInt(orderPersonNumArr[0]
												.toString()));
						ags.iterator().next().setLeftpayReservePosition(leftpayReservePosition);
					}
				}
			}
		}
		// 排序方式
		if (StringUtils.isBlank(orderBy)) {
			orderBy = "groupOpenDate";
			page.setOrderBy(orderBy);
		}

		model.addAttribute("activityDuration", activityDuration);
		model.addAttribute("fromAreas",
				DictUtils.findUserDict(companyId, "fromarea"));
		model.addAttribute("groupOpenDate", groupOpenDate);
		model.addAttribute("groupCloseDate", groupCloseDate);

		model.addAttribute("targetAreaNames", targetAreaNames);
		model.addAttribute("trafficNames",
				iActivityAirTicketService.findAirlineByComid(companyId));

		model.addAttribute("userList", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("travelActivity", travelActivity);
		model.addAttribute("page", page);
		model.addAttribute("showType", showType);
		model.addAttribute("activityKind", activityKind);
		model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(activityKind));

		model.addAttribute("agentId", agentId);
		model.addAttribute("remainDays", remainDays);
		model.addAttribute("travelTypes",
				DictUtils.getValueAndLabelMap("travel_type", companyId));
		model.addAttribute("productLevels",
				DictUtils.getValueAndLabelMap("product_level", companyId));
		model.addAttribute("productTypes",
				DictUtils.getValueAndLabelMap("product_type", companyId));
		model.addAttribute("trafficModes",
				DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("payTypes", DictUtils.getSysDicMap(Context.PAY_TYPE));
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		model.addAttribute("showAreaList", common.getShowAreaList());
		model.addAttribute("cruiseTypeList",
				DictUtils.getDictList("cruise_type")); // 舱型列表

		String url = "";
		if ("1".equals(activityKind)) {// 单团 single
			url = "modules/order/single/calendarForSingleOrder";
		} else if ("2".equals(activityKind)) {// 散拼
			url = "modules/order/loose/calendarForLooseOrder";
		} else if ("3".equals(activityKind)) {// 游行 parade
			url = "modules/order/study/calendarForStudyOrder";
		} else if ("4".equals(activityKind)) {// 大客户 bigAccount
			url = "modules/order/bigcustomer/calendarForBigCustomerOrder";
		} else if ("5".equals(activityKind)) {// 自由行 freeTravel
			url = "modules/order/free/calendarForFreeTravelOrder";
		} else if ("10".equals(activityKind)) {// 游轮 freeTravel
			url = "modules/order/pulley/calendarForLoosePulley";
		} else {
			url = "modules/order/loose/calendarForLooseOrder";
		}
		return url;
	}

	/**
	 * 渠道商登陆只显示所属批发商录过的目标区域 批发商登陆只显示自己录过的目标区域 创建人：liangjingming 创建时间：2014-2-10
	 * 下午3:11:19
	 * 
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "filterTreeData")
	public List<Map<String, Object>> filterTreeData(
			@RequestParam(required = false) Long extId,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList();
		// 批发商ID
		Long companyId = UserUtils.getUser().getCompany().getId();
		targetAreaIds = travelActivityService.findAreaIds(companyId);
		if (targetAreaIds != null && targetAreaIds.size() != 0) {
			for (Map<String, Object> map : targetAreaIds) {
				childAreaIds.add(Long.parseLong(String.valueOf(map
						.get("targetAreaId"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds, areaIds, targetAreas);
		// 目的地
		Map<String, Object> map = null;
		for (int i = 0; i < targetAreas.size(); i++) {
			Area e = targetAreas.get(i);
			if (extId == null
					|| (extId != null && !extId.equals(e.getId()) && e
							.getParentIds().indexOf("," + extId + ",") == -1)) {
				map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent() != null ? e.getParent().getId()
						: 0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 批量导出团期余位表
	 * 
	 * @param showType
	 * @param activityKind
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = { "downloadAllYw/{showType}/{activityKind}" })
	public void downloadAllYw(@PathVariable String showType,
			@PathVariable String activityKind,
			@ModelAttribute TravelActivity travelActivity,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		// 按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("bookOrder", model);

		// 查询条件
		Map<String, String> mapRequest = Maps.newHashMap();

		// 参数处理：起始同行价格、结束同行价格、产品种类、舱型、是否有余位、是否有切位
		String paras = "settlementAdultPriceStart,settlementAdultPriceEnd,orderNumOrGroupCode,wholeSalerKey,spaceType,haveYw,haveQw";
		OrderCommonUtil.handlePara(paras, mapRequest, model, request);

		if (travelActivity.getGroupOpenDate() == null) {
			travelActivity.setGroupOpenDate(new Date());
		} else {
			String tempDate = DateUtils.date2String(travelActivity
					.getGroupOpenDate()) + " 23:59:59";
			if (new Date().after(DateUtils.dateFormat(tempDate,
					DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS))) {
				travelActivity.setGroupOpenDate(new Date());
			}
		}

		String remainDays = request.getParameter("remainDays");// 保留天数

		if (StringUtils.isNotBlank(remainDays))
			travelActivity.setRemainDays(Integer.parseInt(remainDays));
		else
			travelActivity.setRemainDays(null);
		travelActivity.setActivityStatus(2);// 只查询上架产品
		if (StringUtils.isNotBlank(activityKind)) {
			travelActivity.setActivityKind(Integer.valueOf(activityKind));// 产品种类
		}
		Page<TravelActivity> pages = new Page<TravelActivity>(request, response);
		String orderBy = request.getParameter("orderBy");
		// 排序方式
		if (StringUtils.isBlank(orderBy)) {
			orderBy = "groupOpenDate";
			pages.setOrderBy(orderBy);
		}
		pages.setPageSize(10000);
		Page<TravelActivity> page = travelActivityService.findTravelActivity(
				pages, travelActivity, common, mapRequest);
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
						List<ActivityGroup> groupList = travelActivityService
								.findGroupsByActivityId(travelActivity,
										mapRequest);
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
	 * 
	 * @param groupId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "downloadYw", method = RequestMethod.POST)
	public void downloadYw(@RequestParam(value = "groupId") String groupId,
			HttpServletRequest request, HttpServletResponse response) {
		List<Long> groupIdList = Lists.newArrayList();
		if (StringUtils.isNotBlank(groupId)) {
			groupIdList.add(Long.parseLong(groupId));
		} else {
			return;
		}
		downloadYwCommon(groupIdList, request, response);
	}

	private void downloadYwCommon(List<Long> groupIdList,
			HttpServletRequest request, HttpServletResponse response) {

		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Currency> curreycyList = currencyService
				.findCurrencyList(companyId);
		Map<String, String> currencyMap = Maps.newHashMap();
		for (Currency currency : curreycyList) {
			currencyMap.put(currency.getId().toString(),
					currency.getCurrencyMark());
		}

		List<Object[]> groupYwList = Lists.newArrayList();
		try {
			List<ActivityGroup> groupList = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(groupIdList)) {
				groupList = travelActivityService.getYwByGroupIds(groupIdList);
			}
			if (CollectionUtils.isNotEmpty(groupList)) {
				for (ActivityGroup agp : groupList) {
					Object[] obj = new Object[18];
					// 团队类型
					if (agp.getTravelActivity().getActivityKind() != null) {
						obj[0] = OrderCommonUtil.getChineseOrderType(agp
								.getTravelActivity().getActivityKind()
								.toString());
					} else {
						obj[0] = "无";
					}
					// 产品名称
					if (StringUtils.isNotBlank(agp.getTravelActivity()
							.getAcitivityName())) {
						obj[1] = agp.getTravelActivity().getAcitivityName();
					} else {
						obj[1] = "无";
					}
					// 团号
					if (StringUtils.isNotBlank(agp.getGroupCode())) {
						obj[2] = agp.getGroupCode();
					} else {
						obj[2] = "无";
					}
					// 出团日期
					if (agp.getGroupOpenDate() != null) {
						obj[3] = DateUtils.date2String(agp.getGroupOpenDate());
					} else {
						obj[3] = "无";
					}
					// 截团日期
					if (agp.getGroupCloseDate() != null) {
						obj[4] = DateUtils.date2String(agp.getGroupCloseDate());
					} else {
						obj[4] = "无";
					}
					// 航空公司
					if (agp.getTravelActivity().getActivityAirTicket() != null
							&& CollectionUtils.isNotEmpty(agp
									.getTravelActivity().getActivityAirTicket()
									.getFlightInfos())) {

						ActivityAirTicket airticket = agp.getTravelActivity()
								.getActivityAirTicket();
						List<FlightInfo> flighInfoList = airticket
								.getFlightInfos();
						String airType = airticket.getAirType();// 单程、往返、多段，值分别为3、2、1
						if ("3".equals(airType)) {
							String airlines = flighInfoList.get(0)
									.getAirlines();
							if (StringUtils.isNotBlank(airlines)
									&& !"-1".equals(airlines)) {
								List<AirlineInfo> airlineInfoList = airlineInfoService
										.getByAirlineCode(companyId, 0,
												flighInfoList.get(0)
														.getAirlines());
								if (CollectionUtils.isNotEmpty(airlineInfoList)) {
									obj[5] = "单程："
											+ airlineInfoList.get(0)
													.getAirlineName();
								} else {
									obj[5] = "单程：" + "无";
								}
							} else {
								obj[5] = "单程：" + "无";
							}
							String flightNumber = flighInfoList.get(0)
									.getFlightNumber();
							if (StringUtils.isNotBlank(flightNumber)) {
								obj[6] = "单程："
										+ flighInfoList.get(0)
												.getFlightNumber();
							} else {
								obj[6] = "单程：" + "无";
							}
						} else if ("2".equals(airType)) {
							obj[5] = "";
							obj[6] = "";
							for (FlightInfo flighInfo : flighInfoList) {
								if (flighInfo.getNumber() == 1) {
									String airlines = flighInfo.getAirlines();
									if (StringUtils.isNotBlank(airlines)
											&& !"-1".equals(airlines)) {
										List<AirlineInfo> airlineInfoList = airlineInfoService
												.getByAirlineCode(companyId, 0,
														flighInfo.getAirlines());
										if (CollectionUtils
												.isNotEmpty(airlineInfoList)) {
											obj[5] += "去程："
													+ airlineInfoList.get(0)
															.getAirlineName();
										} else {
											obj[5] += "去程：" + "无";
										}
									} else {
										obj[5] += "去程：" + "无";
									}
									String flightNumber = flighInfo
											.getFlightNumber();
									if (StringUtils.isNotBlank(flightNumber)) {
										obj[6] += "去程："
												+ flighInfo.getFlightNumber();
									} else {
										obj[6] += "去程：" + "无";
									}
								} else {
									String airlines = flighInfo.getAirlines();
									if (StringUtils.isNotBlank(airlines)
											&& !"-1".equals(airlines)) {
										List<AirlineInfo> airlineInfoList = airlineInfoService
												.getByAirlineCode(companyId, 0,
														flighInfo.getAirlines());
										if (CollectionUtils
												.isNotEmpty(airlineInfoList)) {
											obj[5] += "返程："
													+ airlineInfoList.get(0)
															.getAirlineName();
										} else {
											obj[5] += "返程：" + "无";
										}
									} else {
										obj[5] += "返程：" + "无";
									}
									String flightNumber = flighInfo
											.getFlightNumber();
									if (StringUtils.isNotBlank(flightNumber)) {
										obj[6] += "返程："
												+ flighInfo.getFlightNumber();
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
								if (StringUtils.isNotBlank(airlines)
										&& !"-1".equals(airlines)) {
									List<AirlineInfo> airlineInfoList = airlineInfoService
											.getByAirlineCode(companyId, 0,
													flighInfo.getAirlines());
									if (CollectionUtils
											.isNotEmpty(airlineInfoList)) {
										obj[5] += "第"
												+ i
												+ "段："
												+ airlineInfoList.get(0)
														.getAirlineName();
									} else {
										obj[5] += "无";
									}
								} else {
									obj[5] += "无";
								}
								String flightNumber = flighInfo
										.getFlightNumber();
								if (StringUtils.isNotBlank(flightNumber)) {
									obj[6] += "第" + i + "段："
											+ flighInfo.getFlightNumber();
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
					String[] currencyArr = { "1", "1", "1", "1", "1", "1", "1",
							"1" };
					currencyArr = currency.split(",");
					if (StringUtils.isNotBlank(currency)
							&& currencyArr.length >= 3) {
						// 成人同行价
						if (agp.getSettlementAdultPrice() != null) {
							obj[8] = currencyMap.get(currencyArr[0])
									+ agp.getSettlementAdultPrice();
						} else {
							obj[8] = "无";
						}
						// 儿童同行价
						if (agp.getSettlementcChildPrice() != null) {
							obj[9] = currencyMap.get(currencyArr[1])
									+ agp.getSettlementcChildPrice();
						} else {
							obj[9] = "无";
						}
						// 特殊人群同行价
						if (agp.getSettlementSpecialPrice() != null) {
							obj[10] = currencyMap.get(currencyArr[2])
									+ agp.getSettlementSpecialPrice();
						} else {
							obj[10] = "无";
						}
					} else {
						obj[8] = "无";
						obj[9] = "无";
						obj[10] = "无";
					}
					// 预收
					if (agp.getPlanPosition() != null) {
						obj[11] = agp.getPlanPosition();
					} else {
						obj[11] = "无";
					}
					// 余位
					if (agp.getFreePosition() != null) {
						obj[12] = agp.getFreePosition();
					} else {
						obj[12] = "无";
					}
					// 占位
					obj[13] = agp.getNopayReservePosition();
					// 切位
					obj[14] = agp.getPayReservePosition();
					// 售出切位
					obj[15] = agp.getSoldPayPosition();
					// 签证国家
					if (StringUtils.isNotBlank(agp.getVisaCountry())) {
						obj[16] = agp.getVisaCountry();
					} else {
						obj[16] = "无";
					}
					// 签证资料截止提前天数
					if (agp.getGroupOpenDate() != null
							&& agp.getVisaDate() != null) {
						obj[17] = ((agp.getGroupOpenDate().getTime() - agp
								.getVisaDate().getTime()) / (24 * 60 * 60 * 1000)) - 1;
					} else {
						obj[17] = "无";
					}
					groupYwList.add(obj);
				}
			}

			// 文件名称
			String fileName = "团期余位下载";
			// Excel各行名称
			String[] cellTitle = { "团队类型", "产品名称", "团号", "出团日期", "截团日期",
					"航空公司", "航班号", "参考航空公司", "成人同行价", "儿童同行价", "特殊人群同行价", "预收",
					"余位", "占位", "切位", "售出切位", "签证国家", "签证资料截止提前天数" };
			// 文件首行标题
			String firstTitle = "团期余位表信息";
			ExportExcel.createExcle(fileName, groupYwList, cellTitle,
					firstTitle, request, response);
		} catch (Exception e) {
			logger.error("下载出错");
			e.printStackTrace();
		}
	}

	/**
	 * 保存产品团期对比信息
	 * @author zhangyp
	 * @param groupCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveActivityGroupCompare")
	public String saveActivityGroupCompare(@RequestParam(value = "groupCode") String groupCode) {
		try {
			// 获取操作员id
			Long operatorId = UserUtils.getUser().getId();

			ActivityGroup activityGroup = activityGroupService.findByGroupCode(groupCode);

			ActivityGroupCompare activityGroupCompare = new ActivityGroupCompare();

			activityGroupCompare.setActivityGroupId(activityGroup.getId());

			activityGroupCompare.setOperatorId(operatorId);

			activityGroupCompareService.saveActivityGroupCompare(activityGroupCompare);
		
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "faild";
		}

		return "success";
	}

	/**
	 * 删除产品团期对比信息
	 * @author zhangyp
	 * @param groupCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delActivityGroupCompare")
	public String delActivityGroupCompare(@RequestParam(value = "groupCode") String groupCode) {
		try {
			// 获取操作员id
			Long operatorId = UserUtils.getUser().getId();
			// 通过团号获取团期产品
			ActivityGroup activityGroup = activityGroupService.findByGroupCode(groupCode);
			// 根据 操作员和产品团期团号来删除对应的产品团期对比信息
			activityGroupCompareService.delActivityGroupCompare(operatorId, activityGroup.getId());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "faild";
		}
		return "success";
	}
	
	/**
	 * 将一个产品下可以加入对比的团期全部加入产品团期对比信息表
	 * @author zhangyp
	 * @param groupCodes:全部加入对比的所有团期团号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveAllActivityGroupCompares")
	public String saveAllActivityGroupCompares(@RequestParam(value="groupCodes") String[] groupCodes) {
		try {
			// 获取当前操作员id
			Long operatorId = UserUtils.getUser().getId();
			// 执行批量保存时，要保存的产品团期对比实体 List
			List<ActivityGroupCompare> activityGroupCompares = new ArrayList<>();
			// 根据团期号获取对应的团期信息，添加到  activityGroupCompares
			for (int i = 0; i < groupCodes.length; i++) {
				ActivityGroup activityGroup = activityGroupService.findByGroupCode(groupCodes[i]);
				ActivityGroupCompare activityGroupCompare = new ActivityGroupCompare();
				activityGroupCompare.setActivityGroupId(activityGroup.getId());
				activityGroupCompare.setOperatorId(operatorId);
				activityGroupCompares.add(activityGroupCompare);
			}
			// 执行批量的保存操作
			activityGroupCompareService.saveAllActivityGroupCompares(activityGroupCompares);
		} catch (Exception e) {
			logger.error("全部加入对比失败!");
			return "faild";
		}
		
		return "success";
	}
	
	/**
	 * 将一个产品下可以加入对比的团期全部从产品团期对比信息表删除
	 * @author zhangyp
	 * @param groupCodes:所有要取消团期团号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delAllActivityGroupCompares")
	public String delAllActivityGroupCompares(@RequestParam(value="groupCodes") String[] groupCodes) {
		try {
			// 获取当前操作员id
			Long operatorId = UserUtils.getUser().getId();
			// 执行批量删除时，要删除的产品团期对比实体 List
			List<ActivityGroupCompare> activityGroupCompares = new ArrayList<>();
			// 根据团期号获取对应的团期信息，添加到  activityGroupCompares
			for (int i = 0; i < groupCodes.length; i++) {
				ActivityGroup activityGroup = activityGroupService.findByGroupCode(groupCodes[i]);
				ActivityGroupCompare activityGroupCompare = activityGroupCompareService.findByProperties(operatorId, activityGroup.getId());
				activityGroupCompares.add(activityGroupCompare);
			}
			// 执行批量的删除操作
			activityGroupCompareService.delAllActivityGroupCompares(activityGroupCompares);
		} catch (Exception e) {
			logger.error("全部取消对比失败!");
			return "faild";
		}
		
		return "success";
	}
	
	/**
	 * 将当前操作员所有产品团期对比信息删除
	 * @author zhangyp
	 * @param groupCodes:所有要取消团期团号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/clearAllActivityGroupCompares")
	public String clearAllActivityGroupCompares() {
		Long operatorId = UserUtils.getUser().getId();
		try {
			activityGroupCompareService.clearAllActivityGroupCompares(operatorId);
		} catch (Exception e) {
			logger.error("清除全部数据失败！");
			return "faild!";
		}
		return "success";
	}
	
	/**
	 * @Description 保存对比团期列表的排序字段供再次加载时调用，如果排序字段为空则不做处理
	 * @author yakun.bai
	 * @Date 2015-10-30
	 */
	@ResponseBody
	@RequestMapping(value = "/saveCompareSortName")
	public String saveCompareSortName(@RequestParam(value="sortName") String sortName) {
		Long userId = UserUtils.getUser().getId();
		activityGroupCompareService.saveCompareSortName(userId, sortName);
		return "success";
	}
	
	/**
	 * @Description 获取对比团期的id用于页面初始加载对比团期
	 * @author yakun.bai
	 * @Date 2015-10-23
	 */
	@ResponseBody
	@RequestMapping(value = "/getCompareGroupIds")
	public String getCompareGroupIds() {
		
		//根据用户id查询对比团期
		Long userId = UserUtils.getUser().getId();
		List<ActivityGroup> activityGroupCompareList = activityGroupCompareService.getCompareGroup(userId);
		
		//json数组：元素为对比团期对象
		JSONArray results = new JSONArray();
		if (CollectionUtils.isNotEmpty(activityGroupCompareList)) {
			//查询用户所在批发商所有币种信息并放入map中：key为币种id；value为币种标识符，其用来获取对比团期成人、儿童、特殊人群币种标识符，避免反复查询
			List<Currency> currencyList = CurrencyUtils.findCurrencyList();
			Map<String, String> currencyMap = Maps.newHashMap();
			for (Currency currency : currencyList) {
				currencyMap.put(currency.getId().toString(), currency.getCurrencyMark());
			}
			//查询用户第一条对比团期的排序值：因为用户所有对比排序值都相同
			ActivityGroupCompare groupCompare = activityGroupCompareService.findByProperties(userId, activityGroupCompareList.get(0).getId());
			String sortName = groupCompare.getSortName();
			for (ActivityGroup group : activityGroupCompareList) {
				TravelActivity activity = group.getTravelActivity();
				ActivityAirTicket ticket = activity.getActivityAirTicket();
				String currencyArr [] = group.getCurrencyType().split(",");
				JSONObject obj = new JSONObject();
				obj.put("sortName", sortName); //排序名称
				obj.put("activityId", activity.getId()); //产品名称
				obj.put("productName", activity.getAcitivityName()); //产品名称
				obj.put("activityTypeName", activity.getActivityLevelName() != null ? activity.getActivityLevelName() : ""); //产品系列
				obj.put("startPlace", activity.getFromAreaName()); //出发地
				if (ticket != null && ticket.getAirlines() != null && !"-1".equals(ticket.getAirlines())) {
					obj.put("airComp", ticket != null ? ticket.getAirlines() : ""); //航空公司
				} else {
					obj.put("airComp", ""); //航空公司
				}
				obj.put("visaCountry", group.getVisaCountry()); //签证国家
				obj.put("operater", activity.getCreateBy().getName()); //操作员
				obj.put("groupCode", group.getGroupCode()); //团期编号
				obj.put("groupOpenDate", DateUtils.date2String(group.getGroupOpenDate())); //出团日期
				obj.put("groupCloseDate", DateUtils.date2String(group.getGroupCloseDate())); //截团日期
				if (group.getVisaDate() != null) {
					obj.put("deadlineDate", DateUtils.date2String(group.getVisaDate())); //资料截止日期
				} else {
					obj.put("deadlineDate", ""); //资料截止日期
				}
				
				int settlementAdultPrice = 0;
				if (group.getSettlementAdultPrice() != null) {
					settlementAdultPrice = group.getSettlementAdultPrice().intValue();
					obj.put("customerPrice", currencyMap.get(currencyArr[0]) + settlementAdultPrice); //成人同行价
					obj.put("convertedSettlementPrice", settlementAdultPrice); //转换之后的同行价
				} else {
					obj.put("customerPrice", ""); //成人同行价
					obj.put("convertedSettlementPrice", ""); //转换之后的同行价
				}
				if (group.getSettlementcChildPrice() != null) {
					obj.put("customerchildPrice", currencyMap.get(currencyArr[1]) + group.getSettlementcChildPrice().intValue()); //儿童同行价
				} else {
					obj.put("customerchildPrice", ""); //儿童同行价
				}
				if (group.getSettlementSpecialPrice() != null) {
					obj.put("customerSpecialPrice", currencyMap.get(currencyArr[2]) + group.getSettlementSpecialPrice().intValue()); //特殊人群同行价
				} else {
					obj.put("customerSpecialPrice", ""); //特殊人群同行价
				}
				
				int settlementcChildPrice = 0;
				if (group.getSuggestAdultPrice() != null) {
					settlementcChildPrice = group.getSuggestAdultPrice().intValue();
					obj.put("channelPrice", currencyMap.get(currencyArr[3]) + settlementcChildPrice); //成人直客价
					obj.put("convertedSuggestPrice", settlementcChildPrice); //转换之后的直客价
				} else {
					obj.put("channelPrice", ""); //成人直客价
					obj.put("convertedSuggestPrice", ""); //转换之后的直客价
				}
				if (group.getSuggestChildPrice() != null) {
					obj.put("channelchildPrice", currencyMap.get(currencyArr[4]) + group.getSuggestChildPrice().intValue()); //儿童直客价
				} else {
					obj.put("channelchildPrice", ""); //儿童直客价
				}
				if (group.getSuggestSpecialPrice() != null) {
					obj.put("channelSpecialPrice", currencyMap.get(currencyArr[5]) + group.getSuggestSpecialPrice().intValue()); //特殊人群直客价
				} else {
					obj.put("channelSpecialPrice", ""); //特殊人群直客价
				}
				if (group.getSingleDiff() != null) {
					obj.put("roompriceDiff", currencyMap.get(currencyArr[6]) + group.getSingleDiff().intValue()); //单房差
				} else {
					obj.put("roompriceDiff", ""); //单房差
				}
				int freePosition = group.getFreePosition();
				int payReservePosition = group.getPayReservePosition();
				int soldPayPosition = group.getSoldPayPosition();
				obj.put("prePay", group.getPlanPosition()); //预收
				obj.put("selledPositions", soldPayPosition); //售出切位
				obj.put("positions", payReservePosition);  //已切位
				obj.put("freePositions", freePosition); //余位
				
				//查询对比团期的预报名人数
				List<Object[]> orderPersonNumList = applyOrderCommonService.sumOrderPersonNumByGroupId(group.getId());
				Object[] orderPersonNumArr = orderPersonNumList.get(0);
				obj.put("preSign", orderPersonNumArr[0].toString());
				
				//预定html和支付方式html，因不能使用前端信息，故把加载数据根据前端逻辑重新整理
				StringBuffer bookHtml = new StringBuffer("");
				StringBuffer selectHtml = new StringBuffer("<select style='display:none;'>");
				
				if (1 == activity.getPayMode_full()) {
					bookHtml.append("<a style='display:none;' href='javascript:void(0)' class='normalPayType' " +
							"onClick='occupied(" + group.getId() + "," + activity.getId() + ",3," + group.getFreePosition() + ")'>付全款</a>");
					selectHtml.append("<option value='3'>全款支付</option>");
				}
				if (1 == activity.getPayMode_op()) {
					bookHtml.append("<a style='display:none;' href='javascript:void(0)' class='opPayType' " +
							"onClick='occupied(" + group.getId() + "," + activity.getId() + ",7," + group.getFreePosition() + ")'>计调确认占位</a>");
					selectHtml.append("<option value='7'>计调确认占位</option>");
				}
				if (1 == activity.getPayMode_cw()) {
					bookHtml.append("<a style='display:none;' href='javascript:void(0)' class='cwPayType' " +
							"onClick='occupied(" + group.getId() + "," + activity.getId() + ",8," + group.getFreePosition() + ")'>财务确认占位</a>");
					selectHtml.append("<option value='8'>财务确认占位</option>");
				}
				if (1 == activity.getPayMode_deposit()) {
					bookHtml.append("<a style='display:none;' href='javascript:void(0)' class='dingjin_PayType' " +
							"onClick='occupied(" + group.getId() + "," + activity.getId() + ",1," + group.getFreePosition() + ")'>订金占位</a>");
					selectHtml.append("<option value='1'>订金占位</option>");
				}
				if (1 == activity.getPayMode_advance()) {
					bookHtml.append("<a style='display:none;' href='javascript:void(0)' class='yuzhan_PayType' " +
							"onClick='occupied(" + group.getId() + "," + activity.getId() + ",2," + group.getFreePosition() + ")'>预占位</a>");
					selectHtml.append("<option value='2'>预占位</option>");
				}
				if (1 == activity.getPayMode_data()) {
					bookHtml.append("<a style='display:none;' href='javascript:void(0)' class='data_PayType' " +
							"onClick='occupied(" + group.getId() + "," + activity.getId() + ",4," + group.getFreePosition() + ")'>资料占位</a>");
					selectHtml.append("<option value='4'>资料占位</option>");
				}
				if (1 == activity.getPayMode_guarantee()) {
					bookHtml.append("<a style='display:none;' href='javascript:void(0)' class='guarantee_PayType' " +
							"onClick='occupied(" + group.getId() + "," + activity.getId() + ",5," + group.getFreePosition() + ")'>担保占位</a>");
					selectHtml.append("<option value='5'>担保占位</option>");
				}
				if (1 == activity.getPayMode_express()) {
					bookHtml.append("<a style='display:none;' href='javascript:void(0)' class='express_PayType' " +
							"onClick='occupied(" + group.getId() + "," + activity.getId() + ",6," + group.getFreePosition() + ")'>确认单占位</a>");
					selectHtml.append("<option value='6'>确认单占位</option>");
				}
				int leftdays = group.getLeftdays();
				Long isAllowSupplement = UserUtils.getUser().getCompany().getIsAllowSupplement();
				if ((freePosition > 0 || (payReservePosition > 0 && payReservePosition - soldPayPosition > 0)) 
     					&& (settlementAdultPrice > 0 || settlementcChildPrice > 0)) {
					if (leftdays > 0) {
						bookHtml.append("<input class='btn btn-primary' type='button' value='预 定' onClick='agentType(this)'/>");
					} else {
						if (isAllowSupplement == 1) {
							bookHtml.append("<input class='btn btn-primary' type='button' value='补 单' onClick='agentType(this)'/>");
						} else {
							bookHtml.append("<input class='btn gray' type='button' value='预 定'/>");
						}
					}
				} else {
					bookHtml.append("<input class='btn gray' type='button' value='预 定'/>");
				}
				selectHtml.append("</select>");
				bookHtml.append(selectHtml);
				obj.put("bookInputHtml", bookHtml.toString()); //操作html
				results.add(obj);
			}
		}
		return results.toString();
	}

	/**
	 * 将当前产品团期对比信息列表进行下载
	 * @auto zhangyp
	 * @param groupCodes
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/downloadActivityGroupCompares")
	public String downloadActivityGroupCompares(@RequestParam(value="groupCodes") String[] groupCodes, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// 封装产品团期下载数据
			List<Object[]> activityGroupCompareList = activityGroupCompareService.getActivityGroupCompareList(groupCodes);
			
			// 文件名称
			String fileName = "产品团期对比列表";
			// Excel各行名称
			String[] cellTitle = { "团号", "产品名称", "出团日期", "截团日期","资料截止日期", "成人同行价", 
					"儿童同行价", "特殊人群同行价","成人直客价", "儿童直客价", "特殊人群直客价", "单房差/间夜", 
					"预报名", "预收", "售出切位", "已切位", "余位", "产品系列", "出发地", "航空公司",
					"签证国家", "操作员"};
			// 文件首行标题
			String firstTitle = "产品团期对比列表";
			ExportExcel.createExcle(fileName, activityGroupCompareList, cellTitle,
					firstTitle, request, response);
		} catch (Exception e) {
			logger.error("下载出错了");
			return "faild";
		}
		
		return "success";
	}
}
