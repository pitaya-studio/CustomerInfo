package com.trekiz.admin.modules.activity.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Decoder.BASE64Decoder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.agentToOffice.PricingStrategy.service.ActivityPricingStrategyService;
import com.trekiz.admin.agentToOffice.T1.order.service.T1OrderListService;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.GroupcodeModifiedRecord;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.entity.TravelActivityProperty;
import com.trekiz.admin.modules.activity.pojo.MatchLine;
import com.trekiz.admin.modules.activity.repository.GroupcodeModifiedRecordDao;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.IGroupcodeModifiedRecordService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.service.IntermodalStrategyService;
import com.trekiz.admin.modules.activity.service.sync.ActivityGroupSyncService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockDetailService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockGroupRelService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockService;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.log.entity.SearchLog;
import com.trekiz.admin.modules.log.service.T1SearchLogService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.repository.UserDeptJobDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.t1.utils.T1Utils;
import com.trekiz.admin.modules.visa.entity.Activityvisafile;
import com.trekiz.admin.modules.visa.service.VisaService;
/**
 * 旅游产品信息控制器
 * @author liangjingming
 *
 */
@Controller
@RequestMapping(value="${adminPath}/activity/manager")
public class TravelActivityController extends BaseController {

	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Resource
	private DocInfoService docInfoService;
	@Resource
	private VisaService visaService;
	@Resource
	private SysIncreaseService sysIncreaseService;
	@Autowired
	private AreaService areaService;
	@Autowired
	AgentinfoService agentinfoService;
	@Autowired
	private ActivityGroupService groupService;
	@Autowired
	private IntermodalStrategyService intermodalStrategyService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private IActivityAirTicketService iActivityAirTicketService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserDeptJobDao userDeptJobDao;
	@Autowired
	private GroupcodeModifiedRecordDao  groupcodeModifiedRecordDao;
	@Autowired
	private IGroupcodeModifiedRecordService iGroupcodeModifiedRecordService;
	//-----223---tgy-s--//
	@Autowired
	private CruiseshipStockService cruiseshipStockService;
	@Autowired
	private CruiseshipStockDetailService cruiseshipStockDetailService;
	@Autowired
	private CruiseshipStockGroupRelService cruiseshipStockGroupRelService;
	@Autowired
	private T1OrderListService orderListService;
	//-----223---tgy-e--//
	//------426--T1---s--//
    /*@Autowired
    private OfficeDao officeDao;*/
	@Autowired
	private OfficeService officeService;
	@Autowired
	private ActivityPricingStrategyService activityPricingStrategyService;
	@Autowired
	private ActivityGroupSyncService activityGroupSyncService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private T1SearchLogService searchLogService;
	@Autowired
	private TouristLineService touristLineService;

	//------426--T1---e--//
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 160;
	}

	/**
	 * 上架产品列表 
	 * 创建人：liangjingming   
	 * activityKind   产品种类
	 * activityStatus 产品状态   上架  下架
	 * 创建时间：2014-3-3 上午10:02:17
	 *
	 */
	@RequestMapping(value={"list/{activityStatus}/{activityKind}"})
	public String list(@PathVariable String activityStatus, @PathVariable String activityKind, @ModelAttribute TravelActivity travelActivity,
					   HttpServletRequest request, HttpServletResponse response,Model model) {

		//产品种类和权限的关系   
		model.addAttribute("shiroType", OrderCommonUtil.getStringOrdeType(activityKind));

		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);

		String activityIds = request.getParameter("activityIds");
		String orderBy = request.getParameter("orderBy");
		// 排序方式默认为逆序创建时间
		if (StringUtils.isBlank(orderBy)) {
			orderBy = "groupOpenDate DESC";
		}

		//518需求，添加上架状态搜索
		String groundingStatus = request.getParameter("groundingStatus");
		try {
			List<Long> ids = new ArrayList<Long>();

			//获取查询参数start
			String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
			String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
			String wholeSalerKey = request.getParameter("wholeSalerKey");
			String remainDays = request.getParameter("remainDays");
			//创建人，计调
			String createName = request.getParameter("createName");
			if(StringUtils.isNotBlank(createName)) {
				User transientUser = new User();
				transientUser.setName(createName);
				travelActivity.setCreateBy(transientUser);
			}
			//询价销售
			String estimatePriceRecordUserName = request.getParameter("estimatePriceRecordUserName");
			if(StringUtils.isNotBlank(estimatePriceRecordUserName)) {
				EstimatePriceRecord estimatePriceRecord = new EstimatePriceRecord();
				estimatePriceRecord.setUserName(estimatePriceRecordUserName);
				travelActivity.setEstimatePriceRecord(estimatePriceRecord);
			}
			if(StringUtils.isNotBlank(remainDays))
				travelActivity.setRemainDays(Integer.parseInt(remainDays));
			else
				travelActivity.setRemainDays(null);
			travelActivity.setAcitivityName(wholeSalerKey);
			travelActivity.setActivityStatus(Integer.parseInt(activityStatus));
			if(StringUtils.isNotBlank(activityKind))
				travelActivity.setActivityKind(StringUtils.toInteger(activityKind));
			//获取查询参数end

			//获取查询结果列表
			Page<TravelActivity> page = travelActivityService.findTravelActivity(new Page<TravelActivity>(request, response), travelActivity,
					settlementAdultPriceStart, settlementAdultPriceEnd, common, groundingStatus);


			if(page.getList()!=null) {
				for(TravelActivity tmp : page.getList()) {

					Set<ActivityGroup> groupTemp = tmp.getActivityGroups();
					//团期是否上架标识
					//String  pricingStrategy_flag = "0";
					for(ActivityGroup group : groupTemp){
						tmp.getActivityGroupList().add(group);
						Map<String, String> pricingStrategy = activityPricingStrategyService.getPricingStrategy(tmp.getId().toString(), group.getId().toString());
						/*for(String key : pricingStrategy.keySet()){
							if(pricingStrategy.get(key) != null && pricingStrategy.get(key) != ""){
								pricingStrategy_flag = "1";
							}
						}*/
						group.setPricingStrategy(pricingStrategy);

						/*String quauqAdultPrice = (group.getQuauqAdultPrice()==null?"-":group.getQuauqAdultPrice().toString());
						String quauqChildPrice = (group.getQuauqChildPrice()==null?"-":group.getQuauqChildPrice().toString());
						String quauqSpecialPrice = (group.getQuauqSpecialPrice()==null?"-":group.getQuauqSpecialPrice().toString());*/
						if(group.getQuauqAdultPrice() != null||group.getQuauqChildPrice() != null||group.getQuauqSpecialPrice() != null){
						/*if(currencys.length > 8 &&(!StringUtil.isBlank(quauqAdultPrice)||!StringUtil.isBlank(quauqChildPrice)||!StringUtil.isBlank(quauqSpecialPrice))){*/
							group.getPricingStrategy().put("alertFlag", "true");
						}else{
							group.getPricingStrategy().put("alertFlag", "false");
						}
						//团期处于未上架状态时，不显示状态标签，pricingStrategy_flag为“0”时，标识团期未上架
						//group.getPricingStrategy().put("pricingStrategy_flag",pricingStrategy_flag);
					}

					//flagList.add(isT1);
					//flagList.add(pricingStrategy_flag);
					//当输入团号搜索时，只显示该团号对应的団期  add by ang.gao 20150813
					/*if(StringUtils.isNotBlank(wholeSalerKey)){
						boolean flag = false;
						Set<ActivityGroup> resultSet = new HashSet<ActivityGroup>();
						Set<ActivityGroup> errSet = new HashSet<ActivityGroup>();
						Set<ActivityGroup> groups = tmp.getActivityGroups();
						for(ActivityGroup g : groups){
							if(g.getGroupCode().contains(wholeSalerKey.trim())){
								resultSet.add(g);
								flag =true;
							}else{
								errSet.add(g);
							}
						}
						if(flag){
							tmp.setActivityGroups(resultSet);
						}else{
							tmp.setActivityGroups(errSet);
						}

					}*/
					if(StringUtils.isNotBlank(wholeSalerKey)){
						boolean flag = false;
						List<ActivityGroup> resultList = new ArrayList<>();
						List<ActivityGroup> errList = new ArrayList<ActivityGroup>();
						List<ActivityGroup> groups = tmp.getActivityGroupList();
						for(ActivityGroup g : groups){
							if(g.getGroupCode().contains(wholeSalerKey.trim())){
								resultList.add(g);
								flag =true;
							}else{
								errList.add(g);
							}
						}
						if(flag){
							tmp.setActivityGroupList(resultList);
						}else{
							tmp.setActivityGroupList(errList);
						}

					}
					ids.add(Long.parseLong(tmp.getId().toString()));
					//需求533 起航假期团期降序排列
					if("5c05dfc65cd24c239cd1528e03965021".equals(UserUtils.getUser().getCompany().getUuid())){
						descSort(tmp);
					}
				}
			}
			//产品是否有团期上架到T1
			//model.addAttribute("flagList",flagList);
			//文件上传下载相关
			Map<Long, List<Map<String, List<DocInfo>>>> visaMap = new HashMap<Long, List<Map<String,List<DocInfo>>>>();
			List<?> visaMapList = visaService.findVisasNew(ids);

			model.addAttribute("visaMapList", visaMapList);
			findFileListByIds(ids, visaMap);
			if(StringUtils.isNotBlank(activityIds)) {
				model.addAttribute("activityIds", activityIds);
			}else{
				model.addAttribute("activityIds", "");
			}
			model.addAttribute("visaMap", visaMap);
			//0518需求,是否有t1上架权限 0表示启用，1表示禁用
			model.addAttribute("shelfRightsStatus", UserUtils.getUser().getCompany().getShelfRightsStatus());
			//回传搜索条件
			model.addAttribute("travelActivity", travelActivity);
			model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
			model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
			model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",companyId));
			model.addAttribute("remainDays", remainDays);
			model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",companyId));
			model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",companyId));
			model.addAttribute("trafficModes", DictUtils.getValueAndLabelMap(Context.TRAFFIC_MODE,companyId));
			model.addAttribute("airlines", iActivityAirTicketService.findAirlineByComid(companyId));
			model.addAttribute("fromAreas", DictUtils.findUserDict(companyId,"fromarea"));
			model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));
			model.addAttribute("groundingStatus", groundingStatus);
			model.addAttribute("activityKind", activityKind);
			//回传产品列表查询结果
			model.addAttribute("page", page);
			//选择币种，服务于查询中同行价格币种选择
			model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
			model.addAttribute("orderBy", orderBy);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获得批发商的uuid,放入model中,根据uuid判断该用户是否具有修改手动输入团号的权限-s
		model.addAttribute("uuid4ManualModifyGroupcode", UserUtils.getUser().getCompany().getUuid());
		//获得批发商的uuid,放入model中,根据uuid判断该用户是否具有修改手动输入团号的权限-e

		//   对应需求  c460   是否可以手动输入
		String groupCodeRuleDT = UserUtils.getUser().getCompany().getGroupCodeRuleDT().toString();
		model.addAttribute("groupCodeRuleDT", groupCodeRuleDT);
		
		// 直客价必填权限 王洋 2017.1.6
		boolean requiredStraightPrice = SecurityUtils.getSubject().isPermitted("looseProduct:operation:requiredStraightPrice");
		model.addAttribute("requiredStraightPrice", requiredStraightPrice);

		if(Context.PRODUCT_TEMP_STATUS.equals(activityStatus)) {
			return "modules/activity/activityListTmp";
		}else if (Context.PRODUCT_ONLINE_STATUS.equals(activityStatus)){
			model.addAttribute("activityStatusValue","online");

			if (Context.PRODUCT_TYPE_SAN_PIN.equals(activityKind)) {
				//需求518  新做散品页面
				return "modules/activity/activityListOnLineForLoose";
			}else {   //不是散拼时，如果是大洋，走大洋页面
				if("7a81a03577a811e5bc1e000c29cf2586".equals(user.getCompany().getUuid())) {
					return "modules/activity/activityListOnLineDayang";
				}else {   //不是大洋，走老页面
					return "modules/activity/activityListOnLine";
				}
			}
		}else if (Context.PRODUCT_OFFLINE_STATUS.equals(activityStatus)) {
			model.addAttribute("activityStatusValue", "offline");
			if ("7a81a03577a811e5bc1e000c29cf2586".equals(user.getCompany().getUuid())){
				return "modules/activity/activityListOffLineDayang";
			}else {
				return "modules/activity/activityListOffLine";
			}
		}
		else
			return "";
	}

	//产品的所有团期降序排列
	private void descSort(TravelActivity activity){

		ActivityGroup activityGroup = null;
		ActivityGroup[] activityGroupArray = new ActivityGroup[activity.getActivityGroupList().size()];
		for(int i=0;i<activity.getActivityGroupList().size();i++){
			activityGroupArray[i] = activity.getActivityGroupList().get(i);
		}
		for(int i=0;i<activityGroupArray.length;i++){
			for(int j=i+1;j<activityGroupArray.length;j++){
				if(activityGroupArray[i].getGroupOpenDate().compareTo(activityGroupArray[j].getGroupOpenDate()) == -1){
					activityGroup = activityGroupArray[i];
					activityGroupArray[i] = activityGroupArray[j];
					activityGroupArray[j] = activityGroup;
				}
			}
		}

		if(activityGroupArray.length>0) {
			List list1 =Arrays.asList(activityGroupArray);
			activity.getActivityGroupList().clear();
			activity.getActivityGroupList().addAll(list1);
		}
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
		List<Map<String, Object>> mapList = null;
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList();
		//批发商ID
		Long companyId = UserUtils.getUser().getCompany().getId();
		targetAreaIds = travelActivityService.findAreaIds(companyId);
		if(targetAreaIds!=null && targetAreaIds.size()!=0){
			for(Map<String, Object> map:targetAreaIds){
				childAreaIds.add(Long.parseLong(String.valueOf(map.get("id"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds,areaIds,targetAreas);
		//判断是否有自定义目标区域
		if(mapList == null || mapList.size() == 0){
			mapList = Lists.newArrayList();
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
		}
		return mapList;
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
	@RequestMapping(value = "filterTreeData1")
	public List<Map<String, Object>> filterTreeData1(@RequestParam(required=false) Long extId,@RequestParam(required=false) Integer kind, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = null;
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList();
		//批发商ID
		Long companyId = UserUtils.getUser().getCompany().getId();
		if("7a81b21a77a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())&&kind != null && (kind == 1 || kind == 2 || kind == 4 || kind == 5)){
			targetAreaIds = travelActivityService.findAreaIdsEndCountry(companyId);
		}else{
			targetAreaIds = travelActivityService.findAreaIds(companyId);
		}
		if(targetAreaIds!=null && targetAreaIds.size()!=0){
			for(Map<String, Object> map:targetAreaIds){
				childAreaIds.add(Long.parseLong(String.valueOf(map.get("id"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds,areaIds,targetAreas);
		//判断是否有自定义目标区域
		if(mapList == null || mapList.size() == 0){
			mapList = Lists.newArrayList();
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
		}
		return mapList;
	}


	/**
	 * 获取批发商下的用户并以部门为树形展示
	 * @param extId
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "filterOpUserTreeData")
	public List<Map<String, Object>> filterOpUserTreeData(@RequestParam(required=false) Long extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		//查询此批发商下的所有用户
//		List<User> userList =  systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
//		if (CollectionUtils.isNotEmpty(userList)) {
//			Map<String, Object> map = null;
//			for (int i=0; i<userList.size(); i++){
//				User user = userList.get(i);
//				if (extId == null || (extId != null && !extId.equals(user.getId()) && "0".indexOf("," + extId + ",")==-1)) {
//					map = Maps.newHashMap();
//					if (i == 0) {
//						map.put("id", 1);
//						map.put("pId", 0);
//						map.put("name", "全部用户");
//						mapList.add(map);
//					} else {
//						map.put("id", user.getId());
//						map.put("pId", 1);
//						map.put("name", user.getName());
//						mapList.add(map);
//					}
//				}
//			}
//		}
//		return mapList;

		/*C209 按公司-部门-用户 形式展示  changed by ang.gao 20150917*/
		//查询此批发商下的所有部门
		//List<Department> deptList = departmentDao.findByOfficeIdOrderByLevel(UserUtils.getUser().getCompany().getId());
		List<Department> deptList = departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId());
		for (int i=0; i<deptList.size(); i++){
			Department e = deptList.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)){

				//查询此部门下所有角色所属用户
				String sql = " SELECT DISTINCT su.id,su.name FROM sys_user_role sur  LEFT JOIN sys_role sr ON sur.roleId= sr.id "
						+" LEFT JOIN sys_user su ON sur.userId=su.id WHERE su.delFlag='0' AND su.name is not NULL  AND sr.deptId="+e.getId();
				List<Map<String,Object>> resultMap = userDao.findBySql(sql,Map.class);
				//若部门下无关联的账号，还需判断子部门下是否有账号，若也没有，该部门不展示
				if(CollectionUtils.isEmpty(resultMap)){
					//查询子部门的id集合
					List<Object> deptIdStr = departmentService.findByOfficeIdAndParentId(UserUtils.getUser().getCompany().getId(), e.getId());
					boolean flag = true;//是否不显示该部门：如 该部门下无账号，且子部门下也无账号，就不显示。
					for(Object id : deptIdStr){
						//查询此部门下所有角色所属用户
						String sql1 = " SELECT DISTINCT su.id,su.name FROM sys_user_role sur  LEFT JOIN sys_role sr ON sur.roleId= sr.id "
								+" LEFT JOIN sys_user su ON sur.userId=su.id WHERE su.delFlag='0' AND su.name is not NULL  AND sr.deptId="+id.toString();
						List<Map<String,Object>> resultMap1 = userDao.findBySql(sql1,Map.class);
						if(CollectionUtils.isNotEmpty(resultMap1)){
							flag = false;
							break;
						}
					}
					if(flag){
						continue;//若该部门以及所有子部门下没有配置用户，则不显示该部门
					}
				}

				//封装部门节点
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);

				if(CollectionUtils.isNotEmpty(resultMap)){
					//数据处理以便调用姓氏排序接口
					String[] userArray = new String[resultMap.size()];
					Map<String,String> userMap = new HashMap<String,String>();
					for(int j = 0 ; j<resultMap.size();j++){
						userArray[j] = resultMap.get(j).get("name").toString();
						userMap.put(resultMap.get(j).get("name").toString(), resultMap.get(j).get("id").toString());
					}
					UserUtils.getSortOfChinese(userArray);//按姓氏拼音排序显示

					//封装子节点-用户
					for(String name : userArray){
						Map<String, Object> map1 = new HashMap<String, Object>();
						map1.put("id", Long.parseLong(userMap.get(name)));
						map1.put("pId", e.getId());
						map1.put("name", name);
						mapList.add(map1);
					}
				}
			}
		}

		return mapList;
	}



	//	@RequiresPermissions("product:manager:add")
	@RequestMapping(value="form")
	public String form(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,Model model) {
		List<Country> countryList = CountryUtils.getCountrys();
		//获取产品编号
		try {
			travelActivity.setActivitySerNum(sysIncreaseService.updateSysIncrease(UserUtils.getUser().getCompany().getName(),UserUtils.getUser().getCompany().getId(),
					null,Context.PRODUCT_NUM_TYPE));
		} catch (Exception e) {
			new ServiceException("获取产品编号出错,请联系管理员");
			return null;
		}

		//获取审核业务 部门列表
		List<Long> deptList = UserUtils.getDepartmentByJobNew();
		Map<String,String> deptMap = new HashMap<String,String>();
		if(CollectionUtils.isNotEmpty(deptList)){
			deptMap.put("dept_id", deptList.get(0).toString());//默认显示第一个部门
			deptMap.put("deptName", departmentService.findById(deptList.get(0)).getName());//获取部门名称
		}
		if(travelActivity.getDeptId()!=null && travelActivity.getDeptId()!=0){
			deptMap.put("dept_id", travelActivity.getDeptId().toString());
			deptMap.put("deptName", departmentService.findById(travelActivity.getDeptId()).getName());
			model.addAttribute("deptMap", deptMap);
		}else{
			model.addAttribute("deptMap", CollectionUtils.isNotEmpty(deptList) ? deptMap : "");
		}
		model.addAttribute("deptId",UserUtils.getUser().getCompany().getId());

		model.addAttribute("visaTypes", DictUtils.getDictList(Context.DICT_TYPE_VISATYPE));
		model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("flightInfo", DictUtils.getValueAndLabelMap("flight_info",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("trafficModes", DictUtils.getValueAndLabelMap(Context.TRAFFIC_MODE,StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("trafficNames", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"flight"));
		model.addAttribute("relevanceFlagId", DictUtils.getRelevanceFlag(StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("relevanceLabel", DictUtils.getRelevanceFlagName(StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("fromAreas", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"fromarea"));
		model.addAttribute("outAreas", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"outarea"));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));
		model.addAttribute("countryList", countryList);
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("groupCodeRule",travelActivityService.findGroupCodeRule());
		model.addAttribute("users",systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		//是否显示关联机票产品
		String kind = request.getParameter("kind");
		boolean trafficFalg = systemService.findTraffic(kind);
		model.addAttribute("trafficFalg", trafficFalg);

		if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
			//团号标识下拉框
			Map<String,String> groupNoMarks = new HashMap<String,String>();
			groupNoMarks.put("LX", "连线产品");
			groupNoMarks.put("JPZ", "柬埔寨产品");
			groupNoMarks.put("YN", "越南产品");
			groupNoMarks.put("MD", "缅甸产品");
			groupNoMarks.put("LW", "老挝产品");
			groupNoMarks.put("LK", "斯里兰卡产品");
			groupNoMarks.put("TG", "泰国产品");
			//0266-增加团号标识的内容-djw-start
			//单团 kind=1,散拼kind=2;大客户kind=4
			if ("1".equals(kind) || "2".equals(kind) || "4".equals(kind)) {
				groupNoMarks.put("BYZ", "芽庄包机（北京出发）");
				groupNoMarks.put("TYZ", "芽庄包机（天津出发）");
				groupNoMarks.put("BXG", "岘港包机（北京出发）");
				groupNoMarks.put("TXG", "岘港包机（天津出发）");
			}
			//0266-增加团号标识的内容-djw-end
			model.addAttribute("groupNoMarks", groupNoMarks);
		}

		// 对应需求号  233  游轮产品团控配置
		String isneedCruiseGroupControl = UserUtils.getUser().getCompany().getIsNeedCruiseshipControll().toString();
		model.addAttribute("isneedCruiseGroupControl", isneedCruiseGroupControl);
		//-----223查询库存游轮名称,uuid并放入model中-s-----//
		List<Map<String,Object>> cruiseshipStockList=cruiseshipStockService.getCruiseshipNamesUuids();
		model.addAttribute("cruiseshipNamesUuids", cruiseshipStockList);
		//-----223查询库存游轮名称,uuid并放入model中-e------//

		//   对应需求  c460
		String groupCodeRuleDT = UserUtils.getUser().getCompany().getGroupCodeRuleDT().toString();
		model.addAttribute("groupCodeRuleDT", groupCodeRuleDT);
		//t1t2增加供应价服务费率
		BigDecimal chargeRate = UserUtils.getUser().getCompany().getChargeRate();
		//默认值0.01
		if(null == chargeRate)
			chargeRate = new BigDecimal(0.01);
		model.addAttribute("chargeRate", chargeRate);
		// 直客价必填权限 王洋 2017.1.6
		boolean requiredStraightPrice = SecurityUtils.getSubject().isPermitted("looseProduct:operation:requiredStraightPrice");
		model.addAttribute("requiredStraightPrice", requiredStraightPrice);
		return "modules/activity/activityForm";
	}
	//TODO
//	@RequiresPermissions("product:manager:delete")
	@RequestMapping(value="del/{proId}/{type}/{activityKind}")
	public String del(@ModelAttribute TravelActivity travelActivity, @PathVariable String proId, @PathVariable String type,
					  @PathVariable String activityKind,
					  HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

		List<Long> idlist = new ArrayList<Long>();
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");

		if(StringUtils.isNotBlank(proId)){
			idlist.add(Long.parseLong(proId));
			travelActivityService.batchDelActivity(idlist);
			/*--0331_forbug 通过activity_id来删除cruiseship_stock_group_rel关联 --djw--*/
			travelActivityService.delCruiseshipStockGrooupRelByActivityIds(idlist);
			/*--0331_forbug 通过activity_id来删除cruiseship_stock_group_rel关联 --djw--*/
			//上下架状态改变后，同步更新缓存
			T1Utils.updateT1HomeCache();
		}

		model.addAttribute("travelActivity", travelActivity);
		model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
		model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
		if(StringUtils.isNotBlank(proId) && Long.parseLong(type) == 1) {
			return "redirect:"+Global.getAdminPath()+"/activity/manager/list/" + travelActivity.getActivityStatus()+"/" + activityKind +"?repage";
		} else{
			return "forward:"+Global.getAdminPath()+"/activity/manager/list?repage";
		}
	}

	/**
	 * 批量删除产品
	 * 创建人：liangjingming
	 * 创建时间：2014-3-3 下午2:30:10
	 * @throws Exception
	 */
//	@RequiresPermissions("product:manager:delete")
	@RequestMapping(value="batchdel/{activityIds}/{activityKind}")
	public String batchdel(@PathVariable String activityIds, @PathVariable String activityKind, @ModelAttribute TravelActivity travelActivity,
						   HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{

		List<Long> idlist = new ArrayList<Long>();
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");

		if(StringUtils.isNotBlank(activityIds)){
			String ids[] = activityIds.split(",");
			for(String id : ids){
				if(StringUtils.isNotBlank(id))
					idlist.add(Long.parseLong(id));
			}
			if(idlist.size()!=0)
				travelActivityService.batchDelActivity(idlist);
			    /*--o331_forbug 通过activity_id来删除cruiseship_stock_group_rel关联 --djw--*/
			travelActivityService.delCruiseshipStockGrooupRelByActivityIds(idlist);
			    /*--o331_forbug 通过activity_id来删除cruiseship_stock_group_rel关联 --djw--*/
		}

		model.addAttribute("travelActivity", travelActivity);
		model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
		model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
		return "redirect:"+Global.getAdminPath()+"/activity/manager/list/"+travelActivity.getActivityStatus()+"/" + activityKind + "?repage";
	}

	/**
	 * 批量下架产品
	 * 创建人：liangjingming
	 * 创建时间：2014-3-3 下午2:30:10
	 * @throws Exception
	 */
	@RequestMapping(value="batchoff/{activityIds}/{activityKind}")
	public String batchoff(@PathVariable String activityIds,@PathVariable String activityKind, @ModelAttribute TravelActivity travelActivity,
						   HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {

		List<Long> idlist = new ArrayList<Long>();
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		if(StringUtils.isNotBlank(activityIds)){
			String ids[] = activityIds.split(",");
			for(String id : ids){
				if(StringUtils.isNotBlank(id))
					idlist.add(Long.parseLong(id));
			}
			if(idlist.size()!=0)
				travelActivityService.batchOnOrOffActivity(idlist, Integer.parseInt(Context.PRODUCT_OFFLINE_STATUS));
		}
		//上下架状态改变后，同步更新缓存
		T1Utils.updateT1HomeCache();
		model.addAttribute("travelActivity", travelActivity);
		model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
		model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
		//---c460--下架取消关联状态-s-qyl//
		travelActivityService.updateCruiseshipRelStatusByActivityId(idlist);
		//---c460--下架取消关联状态-e-qyl//
		return "redirect:"+Global.getAdminPath()+"/activity/manager/list/"+travelActivity.getActivityStatus()+"/" + activityKind + "?repage";
	}

	@RequestMapping(value="offLineOne/{activityid}")
	public String offLineOne(@PathVariable String activityid,@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		List<Long> idlist = new ArrayList<Long>();
		idlist.add(Long.parseLong(activityid));
		travelActivityService.batchOnOrOffActivity(idlist, Integer.parseInt(Context.PRODUCT_OFFLINE_STATUS));
		return "redirect:"+Global.getAdminPath()+"/activity/manager/detail/"+activityid;
	}

	@RequestMapping(value="onLineOne/{activityid}")
	public String onLineOne(@PathVariable String activityid,@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		List<Long> idlist = new ArrayList<Long>();
		idlist.add(Long.parseLong(activityid));
		travelActivityService.batchOnOrOffActivity(idlist, Integer.parseInt(Context.PRODUCT_ONLINE_STATUS));
		return "redirect:"+Global.getAdminPath()+"/activity/manager/detail/"+activityid;
	}


	/**
	 * 批量上架产品
	 * 创建人：liangjingming
	 * 创建时间：2014-3-3 下午2:30:10
	 * @throws Exception
	 */
	@RequestMapping(value="batchrelease/{activityIds}/{activityKind}")
	public String batchrelease(@PathVariable String activityIds,@PathVariable String activityKind, @ModelAttribute TravelActivity travelActivity,
							   HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{

		List<Long> idlist = new ArrayList<Long>();
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");

		if(StringUtils.isNotBlank(activityIds)){
			String ids[] = activityIds.split(",");
			for(String id : ids){
				if(StringUtils.isNotBlank(id))
					idlist.add(Long.parseLong(id));
			}
			if(idlist.size()!=0)
				travelActivityService.batchOnOrOffActivity(idlist, Integer.parseInt(Context.PRODUCT_ONLINE_STATUS));
		}
		//上下架状态改变后，同步更新缓存
		T1Utils.updateT1HomeCache();
		model.addAttribute("travelActivity", travelActivity);
		model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
		model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
		return "redirect:"+Global.getAdminPath()+"/activity/manager/list/"+travelActivity.getActivityStatus()+"/" + activityKind + "?repage";
	}
	/**
	 * 批量上架产品
	 * 创建人：liangjingming
	 * 创建时间：2014-3-3 下午2:30:10
	 * @throws Exception
	 */
	@RequestMapping(value="batchreleaseTmp/{activityIds}/{activityKind}")
	public String batchreleaseTmp(@PathVariable String activityIds,@PathVariable String activityKind, @ModelAttribute TravelActivity travelActivity,
								  HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{

		List<Long> idlist = new ArrayList<Long>();
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");

		if(StringUtils.isNotBlank(activityIds)){
			String ids[] = activityIds.split(",");
			for(String id : ids){
				if(StringUtils.isNotBlank(id))
					idlist.add(Long.parseLong(id));
			}
			if(idlist.size()!=0)
				travelActivityService.batchOnActivityTmp(idlist);
		}

		model.addAttribute("travelActivity", travelActivity);
		model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
		model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
		return "redirect:"+Global.getAdminPath()+"/activity/manager/list/"+travelActivity.getActivityStatus()+"/" + activityKind + "?repage";
	}
	/**
	 * 批量发布产品
	 * 创建人：Administrator
	 * 创建时间：2014-3-3 下午5:58:37
	 * @throws Exception
	 *
	 */
	@RequestMapping(value="release/{proId}/{activityKind}")
	public String release(@PathVariable String proId,@PathVariable String activityKind, @ModelAttribute TravelActivity travelActivity,
						  HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{

		List<Long> idlist = new ArrayList<Long>();
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");

		if(StringUtils.isNotBlank(proId)){
			idlist.add(Long.parseLong(proId));
			travelActivityService.batchOnOrOffActivity(idlist, Integer.parseInt(Context.PRODUCT_ONLINE_STATUS));
		}

		model.addAttribute("travelActivity", travelActivity);
		model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
		model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
		return "redirect:"+Global.getAdminPath()+"/activity/manager/list/"+travelActivity.getActivityStatus()+"/" + activityKind + "?repage";
	}

	//	@RequiresPermissions("product:manager:edit")
	@RequestMapping(value="mod/{proId}/{isToT1}")
	public String modify(@PathVariable String proId,@PathVariable String isToT1,HttpServletRequest request, HttpServletResponse response,Model model){
		//TODO
		List<Map<String,String>> pricingStrategyList = Lists.newArrayList();
		List<Long> ids = new ArrayList<Long>();
		TravelActivity activity = new TravelActivity();
		if(StringUtils.isNotBlank(proId)){
			ids.add(Long.parseLong(proId));
			activity = travelActivityService.findById(Long.parseLong(proId));
			// 525 线路玩法对象
			model.addAttribute("touristLineId", activity.getTouristLineId());
			if (activity.getTouristLineId() != null){
				TouristLine touristLine = touristLineService.getById(activity.getTouristLineId());
				if(touristLine != null) {
					Map<String,String> touristLineMap = new HashMap<String,String>();
					touristLineMap.put(touristLine.getId().toString(),touristLine.getLineName());
					model.addAttribute("touristLineMap", touristLineMap);
//				model.addAttribute("touristLineId", touristLine.getId());
				}
			}
			//去掉activity中的重复文件信息，开始
			Set<ActivityFile> activityFiles = activity.getActivityFiles();
			if(null != activityFiles && activityFiles.size() > 0){
				List<String> fileIds = new ArrayList<String>();
				Set<ActivityFile> noRepeatActivityFiles = new HashSet<ActivityFile>();
				for(ActivityFile file:activityFiles){
					if(file.getDocInfo()!=null){
						String id = file.getDocInfo().getId().toString();
						if(!fileIds.contains(id.toString())){
							fileIds.add(id);
							noRepeatActivityFiles.add(file);
						}
					}
				}
				//给activity的ActivityFiles重新赋值，该值无重复文件项
				activity.setActivityFiles(noRepeatActivityFiles);
			}
			//去掉activity中的重复文件信息，结束

			//查找个团期是否存在策略
			//model.addAttribute("pricingStrategy_flag", findIsHasPS(activity));

			model.addAttribute("travelActivity", activity);

		}
		Map<Long, List<Map<String, List<DocInfo>>>> visaMap = new HashMap<Long, List<Map<String, List<DocInfo>>>>();
		findFileListByIds(ids, visaMap);
		//产品中各个团期的币种
		if(!activity.getActivityGroups().isEmpty()) {
			Set<ActivityGroup> ActivityGroups = activity.getActivityGroups();
			String[] groupCurrencyArr = new String[activity.getActivityGroups().size()];
			Iterator<ActivityGroup> iterator = activity.getActivityGroups().iterator();
			Long companyId = UserUtils.getUser().getCompany().getId();
			//批发商的币种列表
			List<Currency> currencyList = currencyService.findCurrencyList(companyId);

			//将币种的id和标志装成map，方便使用
			Map<String, String> currencyMap = new HashMap<String, String>();

			for(Currency c : currencyList) {
				currencyMap.put(c.getId().toString(), c.getCurrencyMark());
			}
			int arrIndex = 0;
			while(iterator.hasNext()) {

				//截取团期价格的币种
				String currencyType = iterator.next().getCurrencyType();

				//用币种标志拼装的字符串
				StringBuffer currencyMarkBuf = new StringBuffer();
				if(StringUtils.isNotBlank(currencyType)) {
					for(String currencyId : currencyType.split(",")) {
						if(StringUtils.isNotBlank(currencyId)) {
							currencyMarkBuf.append(currencyMap.get(currencyId)==null ? "":currencyMap.get(currencyId) + ",");
						}
					}
				}
				groupCurrencyArr[arrIndex] = currencyMarkBuf.toString();
				arrIndex++;
			}

			model.addAttribute("currencyTypeArr", groupCurrencyArr);
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < groupCurrencyArr.length; i++){
				sb.append(groupCurrencyArr[i]).append("###");
			}
			String newStr = sb.toString();
			model.addAttribute("currencyTypeArrStr", newStr);
			//t1t2-v2获取对应的定价策略
			Map<String,String> pricingStrategy = new HashMap<String,String>();
			Iterator<ActivityGroup> iterators = ActivityGroups.iterator();
			StringBuffer groupCodeBuffer = new StringBuffer();
			StringBuffer groupIdsBuffer = new StringBuffer();
			Integer isT1_flag = 0;
			while(iterators.hasNext()) {
				//获取上架T1平台的团期团号
				ActivityGroup activityGroup = iterators.next();
				activity.getActivityGroupList().add(activityGroup);
				Integer isT1 = activityGroup.getIsT1();
				if(isT1 == Context.QUAUQ_T1_ON) {
					isT1_flag = 1;
					if(groupCodeBuffer.length() > 0) {
						groupCodeBuffer.append("、").append(activityGroup.getGroupCode());
						groupIdsBuffer.append(",").append(activityGroup.getId());
					}else {
						groupCodeBuffer.append(activityGroup.getGroupCode());
						groupIdsBuffer.append(activityGroup.getId());
					}

				}

				//t1t2-v4 518是否有quauq策略
				String quauqAdultPrice = (activityGroup.getQuauqAdultPrice()==null?"-":activityGroup.getQuauqAdultPrice().toString());
				String quauqChildPrice = (activityGroup.getQuauqChildPrice()==null?"-":activityGroup.getQuauqChildPrice().toString());
				String quauqSpecialPrice = (activityGroup.getQuauqSpecialPrice()==null?"-":activityGroup.getQuauqSpecialPrice().toString());
				activityGroup.setPricingStrategy(new HashMap<String, String>());
				if(quauqAdultPrice != "-"||quauqChildPrice != "-"||quauqSpecialPrice != "-"){
					activityGroup.getPricingStrategy().put("alertFlag", "true");
				}else{
					activityGroup.getPricingStrategy().put("alertFlag", "false");
				}
				//获取儿童 、 特殊人群占位人数
				//获取团期剩余儿童人数、剩余特殊人数
				Map<String, Object>  counts = activityGroupService.countOrderChildAndSpecialNum(activityGroup.getId(),null);
				activityGroup.setNopayPeoplePosition(counts.get("orderPersonNumSpecial")==null?0:new Integer(counts.get("orderPersonNumSpecial").toString()));
				activityGroup.setNopayChildrenPosition(counts.get("orderPersonNumChild")==null?0:new Integer(counts.get("orderPersonNumChild").toString()));
				//获取团期已预订的儿童数和特殊人群人数
				pricingStrategy = activityPricingStrategyService.getPricingStrategy(proId,activityGroup.getId().toString());
				activityGroup.setPricingStrategy(pricingStrategy);
				pricingStrategyList.add(pricingStrategy);
			}

			//需求533 起航假期团期降序排列
			if("5c05dfc65cd24c239cd1528e03965021".equals(UserUtils.getUser().getCompany().getUuid())){
				descSort(activity);
			}

			model.addAttribute("isT1_flag", isT1_flag);
			//查找个团期是否存在策略
			model.addAttribute("pricingStrategy_flag", findIsHasPS(activity));
			model.addAttribute("groupCodeBuffer", groupCodeBuffer.toString());
			model.addAttribute("groupIdsBuffer", groupIdsBuffer.toString());
		}
		model.addAttribute("pricingStrategyList", pricingStrategyList);
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaTypes", DictUtils.getDictList(Context.DICT_TYPE_VISATYPE));
		model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("groupCodeRule",travelActivityService.findGroupCodeRule());
		model.addAttribute("trafficModes", DictUtils.getValueAndLabelMap(Context.TRAFFIC_MODE,StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("relevanceFlagId", DictUtils.getRelevanceFlag(StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("trafficNames", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"flight"));
		model.addAttribute("fromAreas", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"fromarea"));
		model.addAttribute("outAreas", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"outarea"));
		List<IntermodalStrategy> intermodalStrategies = intermodalStrategyService.getActivityIntermodalStrategies(Long.valueOf(proId));
		if(intermodalStrategies.size() == 1 && intermodalStrategies.get(0).getType() == 1){
			model.addAttribute("intermodalType", 1);
		} else if (intermodalStrategies.size() > 0 && intermodalStrategies.get(0).getType() == 2){
			model.addAttribute("intermodalType", 2);
		}
		model.addAttribute("intermodalStrategies", intermodalStrategies);
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));
		model.addAttribute("countryList", countryList);
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("visaMap", visaMap);
		model.addAttribute("users",systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));

		if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
			//团号标识下拉框
			Map<String,String> groupNoMarks = new HashMap<String,String>();
			groupNoMarks.put("LX", "连线产品");
			groupNoMarks.put("JPZ", "柬埔寨产品");
			groupNoMarks.put("YN", "越南产品");
			groupNoMarks.put("MD", "缅甸产品");
			groupNoMarks.put("LW", "老挝产品");
			groupNoMarks.put("LK", "斯里兰卡产品");
			groupNoMarks.put("TG", "泰国产品");
			//0266-增加团号标识的内容-djw-start
			if (activity.getActivityKind()==1 || activity.getActivityKind()==2 || activity.getActivityKind()==4) {
				groupNoMarks.put("BYZ", "芽庄包机（北京出发）");
				groupNoMarks.put("TYZ", "芽庄包机（天津出发）");
				groupNoMarks.put("BXG", "岘港包机（北京出发）");
				groupNoMarks.put("TXG", "岘港包机（天津出发）");

			}
			//0266-增加团号标识的内容-djw-end
			model.addAttribute("groupNoMarks", groupNoMarks);
		}

		// c451,c453  显示产品团期 的 修改信息
		Map<String, List<GroupcodeModifiedRecord>>  groupcodeModifiedRecordmap =  iGroupcodeModifiedRecordService.getGroupCodeByProductId(Long.parseLong(proId));
		model.addAttribute("groupcodeModifiedRecordmap", groupcodeModifiedRecordmap);

		//部门
		String   airTicketFlg  =   systemService.getProductAirTicketId(activity.getId());
		String deptName = departmentService.findById(activity.getDeptId()).getName();
		model.addAttribute("deptName",deptName);
		/*选择交通方式时关联机票 可配置功能未上线，这里屏蔽处理*/
		//boolean trafficFalg = systemService.findTraffic(activity.getActivityKind().toString());
		//model.addAttribute("trafficFalg", trafficFalg);   // 是否有关联机票产品权限
		model.addAttribute("trafficFalg", "true");//默认有关联机票产品权限
		model.addAttribute("airTicketFlg",airTicketFlg);  //未关联机票产品
		//获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-start-------------------------//
		model.addAttribute("is_need_groupCode", UserUtils.getUser().getCompany().getIsNeedGroupCode());
		//获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-end---------------------------//

		// 对应需求号  233  游轮产品团控配置
		String isneedCruiseGroupControl = UserUtils.getUser().getCompany().getIsNeedCruiseshipControll().toString();
		model.addAttribute("isneedCruiseGroupControl", isneedCruiseGroupControl);


		// 对应需求号  c460
		String groupCodeRuleDT = UserUtils.getUser().getCompany().getGroupCodeRuleDT().toString();
		model.addAttribute("groupCodeRuleDT", groupCodeRuleDT);
		//t1t2增加供应价服务费率
		BigDecimal chargeRate = UserUtils.getUser().getCompany().getChargeRate();
		//默认值0.01
		if(null == chargeRate)
			chargeRate = new BigDecimal(0.01);
		model.addAttribute("chargeRate", chargeRate);
		if(StringUtils.isNotBlank(isToT1)) {
			model.addAttribute("isToT1", isToT1);
		}
		//t1t2-v4，0518需求,修改同行价或直客价，设置定价策略权限
//		model.addAttribute("hasPricingStrategyPermission",UserUtils.getUser().getHasPricingStrategyPermission());
		//t1t2-v4，0518需求，当前批发商上架权限
		model.addAttribute("shelfRightsStatus",UserUtils.getUser().getCompany().getShelfRightsStatus());
		model.addAttribute("targetAreaId", activity.getTargetAreaIds());
//		return "modules/activity/activityMod";
		//t1t2-v4，0518需求
		// 直客价必填权限 王洋 2017.1.6
		boolean requiredStraightPrice = SecurityUtils.getSubject().isPermitted("looseProduct:operation:requiredStraightPrice");
		model.addAttribute("requiredStraightPrice", requiredStraightPrice);
		if(activity.getActivityKind().equals(Context.ProductType.PRODUCT_LOOSE)) {
			return "modules/activity/activityModNew";
		}else {
			return "modules/activity/activityMod";
		}
	}

	@RequestMapping(value="detail/{proId}")
	public String detail(@PathVariable Long proId, HttpServletRequest request, Model model) {

		// 查询产品
		TravelActivity activity = travelActivityService.findById(proId);

		//查找个团期是否存在策略
		model.addAttribute("pricingStrategy_flag", findIsHasPS(activity));
		Set<ActivityGroup> activityGroups = activity.getActivityGroups();
		Integer isT1 = 0;
		for (ActivityGroup activityGroup : activityGroups) {
			activity.getActivityGroupList().add(activityGroup);
			if(activityGroup.getIsT1() == Context.QUAUQ_T1_ON){
				isT1 = 1;
			}
		}

		//需求533 起航假期团期降序排列
		if("5c05dfc65cd24c239cd1528e03965021".equals(UserUtils.getUser().getCompany().getUuid())){
			descSort(activity);
		}

		model.addAttribute("isT1", isT1);

		// 查找各团期价格策略
		findGroupPricingStrategy(activity);

		// 去掉产品重复文件
		removeDuplicateFile(activity);

		// 查询签证产品
		List<Long> ids = Lists.newArrayList();
		ids.add(proId);
		Map<Long, List<Map<String, List<DocInfo>>>> visaMap = Maps.newHashMap();
		findFileListByIds(ids, visaMap);

		// 值传递
		setPara(proId, activity, model, visaMap, request);

		return "modules/activity/activityDetail";
	}

	/**
	 * 产品详情页-报名模块団期列表用
	 * 只显示对应団期信息
	 * @param proId
	 * @param groupId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="detail4Groups/{proId}/{groupId}")
	public String detail4Groups(@PathVariable Long proId, @PathVariable Long groupId, HttpServletRequest request, Model model){

		// 查询产品
		TravelActivity activity = travelActivityService.findById(proId);

		//查找个团期是否存在策略
		Set<ActivityGroup> activityGroups = activity.getActivityGroups();
		Integer isT1 = 0;
		for (ActivityGroup activityGroup : activityGroups) {
			if(activityGroup.getId().toString().equals(groupId == null ? "" : groupId.toString())){
				if(activityGroup.getIsT1() == Context.QUAUQ_T1_ON){
					isT1 = 1;
				}
			}
		}

		model.addAttribute("isT1", isT1);

		// 去掉产品重复文件
		removeDuplicateFile(activity);

		// 过滤团期
		filterGroup(activity, groupId);

		// 查询签证产品
		List<Long> ids = Lists.newArrayList();
		ids.add(proId);
		Map<Long, List<Map<String, List<DocInfo>>>> visaMap = Maps.newHashMap();
		findFileListByIds(ids, visaMap);

		// 值传递
		setPara(proId, activity, model, visaMap, request);
		
		return "modules/activity/activityDetail";
	}

	/**
	 * 查找各团期价格策略
	 * @author yakun.bai
	 * @Date 2016-7-28
	 */
	private void findGroupPricingStrategy(TravelActivity activity) {
		//查找各团期的quauq策略
		for (ActivityGroup group : activity.getActivityGroups()) {
			Long groupId = group.getId();
			Long activityId = activity.getId();
			Map<String, String> pricingStrategy = activityPricingStrategyService.getPricingStrategy(activityId.toString(), groupId.toString());
			group.setPricingStrategy(pricingStrategy);
		}
	}

	/**
	 * 去掉产品重复文件
	 * @author yakun.bai
	 * @Date 2016-7-28
	 */
	private void removeDuplicateFile(TravelActivity activity) {
		Set<ActivityFile> activityFiles = activity.getActivityFiles();
		if(null != activityFiles && activityFiles.size() > 0){
			List<ActivityFile> fileIds = new ArrayList<ActivityFile>();
			for(ActivityFile file:activityFiles){
				if(!fileIds.contains(file)){
					fileIds.add(file);
				}
			}
			activityFiles.clear();
			for(ActivityFile activityFile : fileIds){
				activityFiles.add(activityFile);
			}
		}
	}

	/**
	 * 过滤団期
	 * @author yakun.bai
	 * @Date 2016-7-28
	 */
	private void filterGroup(TravelActivity activity, Long groupId) {
		List<ActivityGroup> activityGroup = Lists.newArrayList();
		Set<ActivityGroup> activityGroups = activity.getActivityGroups();
		for(ActivityGroup group : activityGroups){
			if(group.getId() == groupId.longValue()){
				activityGroup.add(group);
			}
		}
		activity.setActivityGroupList(activityGroup);
	}

	/**
	 * 值传递（产品详情） 
	 * @author yakun.bai
	 * @Date 2016-7-28
	 */
	private void setPara(Long proId, TravelActivity activity, Model model,
						 Map<Long, List<Map<String, List<DocInfo>>>> visaMap, HttpServletRequest request) {
		model.addAttribute("travelActivity", activity);
		List<IntermodalStrategy> intermodalStrategies = intermodalStrategyService.getActivityIntermodalStrategies(proId);
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaMap",visaMap);
		model.addAttribute("visaTypes", DictUtils.getDictList(Context.DICT_TYPE_VISATYPE));
		model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("trafficModes", DictUtils.getSysDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("relevanceFlagId", DictUtils.getRelevanceFlag(StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("trafficNames", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"flight"));
		model.addAttribute("fromAreas", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"fromarea"));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));
		model.addAttribute("countryList", countryList);
		model.addAttribute("intermodalStrategies", intermodalStrategies);

		// c451,c453  显示产品团期 的 修改信息
		Map<String, List<GroupcodeModifiedRecord>>  groupcodeModifiedRecordmap =  iGroupcodeModifiedRecordService.getGroupCodeByProductId(proId);
		model.addAttribute("groupcodeModifiedRecordmap", groupcodeModifiedRecordmap);

		// 525 线路玩法对象 add by: yudong.xu 2016.10.14
		if (activity.getTouristLineId() != null){
			TouristLine touristLine;
			if (activity.getTouristLineId() == 0){ // 旅游线路id为0表示 "其他",定值常量,不进行查询
				touristLine = new TouristLine();
				touristLine.setId(0L);
				touristLine.setLineName("其他");
			}else {
				touristLine = touristLineService.getById(activity.getTouristLineId());
			}
			activity.setTouristLine(touristLine);
		}

		//部门
		String deptName = departmentService.findById(activity.getDeptId()).getName();
		model.addAttribute("deptName",deptName);
		//获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-start-------------------------//
		model.addAttribute("is_need_groupCode", UserUtils.getUser().getCompany().getIsNeedGroupCode());
		//获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-end---------------------------//

		// 是否可查看quauq价格策略
		String isOp = request.getParameter("isOp");
		model.addAttribute("isOp", isOp);
	}

	/**
	 * 根据产品id   List查询签证信息
	 * @author jiachen
	 * @DateTime 2015年1月6日 下午8:54:04
	 * @return Map<Long,List<Map<String,List<DocInfo>>>>
	 */
	private Map<Long, List<Map<String, List<DocInfo>>>> findFileListByIds(List<Long> ids,
																		  Map<Long, List<Map<String, List<DocInfo>>>> visaMap) {


		if(ids.size()!=0){
			List<Object[]> visas = visaService.findVisas(ids);
			List<Map<String, List<DocInfo>>> visalist = new ArrayList<Map<String,List<DocInfo>>>();
			Map<String, List<DocInfo>> tmp;
			if(visas != null){
				for(Object[] objs:visas){
					String srcActivityId = objs[0].toString();
					String visaType = objs[1].toString();
//					String countryName = objs[2].toString();
					String countryId = objs[3].toString();
					List<DocInfo> fileList = visaService.findVisaFiles(srcActivityId, visaType, countryId);
					tmp = new HashMap<String, List<DocInfo>>();
					tmp.put(countryId+"/"+DictUtils.getSysDicMap(Context.DICT_TYPE_VISATYPE).get(visaType), fileList);
					visalist.add(tmp);
					visaMap.put(Long.parseLong(srcActivityId), visalist);
				}
			}
		}
		return visaMap;
	}

	@ResponseBody
	@RequestMapping(value="loadvisas/{proId}")
	public Object findProVisas(@PathVariable String proId){

		@SuppressWarnings("rawtypes")
		Map<String, List> results = new HashMap<String, List>();
		List<DocInfo> docs = new ArrayList<DocInfo>();
		List<Long> srcDocIds = new ArrayList<Long>();
		List<Activityvisafile> datas = visaService.findVisaFileByProid(Long.parseLong(proId));
		if(datas != null && datas.size() != 0){
			for(Activityvisafile visa : datas){
				if(visa.getSrcDocId()!=null)
					srcDocIds.add(visa.getSrcDocId());
			}
		}
		if(srcDocIds.size() != 0)
			docs = docInfoService.getDocInfoByIds(srcDocIds);
		results.put("visas", datas);
		results.put("docs", docs);
		return results;
	}
	/**
	 * 团期类产品新增     判断团号是否重复
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="groupsplit",method=RequestMethod.POST)
	public synchronized Object groupCodeSplit(HttpServletRequest request, HttpServletResponse response){

		JSONArray results = new JSONArray();
		String jsonRes = request.getParameter("jsonresult");
		if(StringUtils.isNotBlank(jsonRes)){
			JSONArray array = JSONArray.fromObject(jsonRes);
			int len = array.size();
			Set<String> set = new HashSet<>();
			for(int i=0;i<len;i++)
			{

				JSONObject obj = array.getJSONObject(i);
				String code = obj.getString("value");
				//resobj.put("id", id);
				set.add(code);
			}
			if(len != set.size())
			{
				JSONObject resobj = new JSONObject();
				resobj.put("flag", "true");
				results.add(resobj);
				return results;
			}
			for(int i=0;i<len;i++){
				JSONObject resobj = new JSONObject();
				JSONObject obj = array.getJSONObject(i);
				String id = obj.getString("id");
				resobj.put("id", id);
				String value = obj.getString("value");
				resobj.put("value", value);
				if(groupService.groupCodeValidator(value, null))
					resobj.put("flag", "false");
				else
					resobj.put("flag", "true");
				results.add(resobj);
			}
		}
		return results;
	}

	/**
	 * 判断团号是否重复  团期类产品修改 团号重复判断方法
	 * 游轮产品新增舱型时团号可重复
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({"unchecked" })
	@ResponseBody
	@RequestMapping(value="groupCodeUpdateValidate",method=RequestMethod.POST)
	public synchronized Object groupCodeUpdateValidate(HttpServletRequest request, HttpServletResponse response){
		JSONArray results = new JSONArray();
		String jsonRes = request.getParameter("jsonresult");
		JSONArray arrays = JSONArray.fromObject(jsonRes);
		JSONArray newAdd=JSONArray.fromObject(arrays.get(0));
		JSONArray berforeEdit=JSONArray.fromObject(arrays.get(1));
		JSONArray afterEdit=JSONArray.fromObject(arrays.get(2));
		JSONArray productProperty=JSONArray.fromObject(arrays.get(3));
		//新增数据
		List<TravelActivityProperty> newAddList=(List<TravelActivityProperty>)JSONArray.toCollection(newAdd, TravelActivityProperty.class);
		//修改前数据
		List<TravelActivityProperty> berforeEditList=(List<TravelActivityProperty>)JSONArray.toCollection(berforeEdit, TravelActivityProperty.class);
		//修改后数据
		List<TravelActivityProperty> afterEditList=(List<TravelActivityProperty>)JSONArray.toCollection(afterEdit, TravelActivityProperty.class);
		//产品属性
		List<TravelActivityProperty> productPropertyClass=(List<TravelActivityProperty>)JSONArray.toCollection(productProperty, TravelActivityProperty.class);

		//产品类型
		String productType = productPropertyClass.get(0).getProductType();

		//对大洋的新增修改功能需要对传递过来的所有数据都做校验
//		 if("7a81a03577a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())|| 
//				 "7a81a03577a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid()))
//			{
		Set<String> set =validateChange(newAddList, afterEditList);
		// 如果相等说明传递过来的团号没有重复。游轮产品新增舱型时团号可重复,只需在下一个if校验是否和已存在団期的团号重复。
		if(set.size() ==newAddList.size()+afterEditList.size() || "10".equals(productType))
		{
			JSONObject resobj1 = new JSONObject();
			int count= 0;
			if(set.size() >0)
				count= groupService.getGroupSize(set,productPropertyClass.get(0).getProductId());
			if(count >0)// 有重复
			{
				resobj1.put("flag", "false");
				resobj1.put("id", 0);
				results.add(resobj1);
				return results;
			}
			else // 无重复团号
			{
				resobj1.put("flag", "true");
				resobj1.put("id", 0);
				results.add(resobj1);
				Set<String> sets = validateChange(berforeEditList, afterEditList);
				//团号有变更！
				if(sets.size() != (berforeEditList.size()+afterEditList.size())/2 )
				{

					for(TravelActivityProperty t:berforeEditList)
					{
						for(TravelActivityProperty t1:afterEditList)
						{
							if(t.getId()!=null && t.getId().equals(t1.getId())&&!t.getValue().equals(t1.getValue()))
							{
								GroupcodeModifiedRecord  groupcodeModifiedRecord = new GroupcodeModifiedRecord();
								groupcodeModifiedRecord.setCreateBy(UserUtils.getUser().getId().intValue());
								groupcodeModifiedRecord.setGroupcodeNew(t1.getValue());
								groupcodeModifiedRecord.setGroupcodeOld(t.getValue());
								groupcodeModifiedRecord.setActivityGroupId(Integer.valueOf(t.getId()));
								groupcodeModifiedRecord.setProductId(Integer.valueOf(t.getProductId()));
								groupcodeModifiedRecord.setProductType(1);
								groupcodeModifiedRecord.setUpdateByName(UserUtils.getUser().getName());
								groupcodeModifiedRecord.setProductType(Integer.valueOf(productPropertyClass.get(0).getProductType()));
								//如果是大洋/非常国际/优加/起航假期 需要保存到团号库   c460 ，添加 0 == UserUtils.getUser().getCompany().getGroupCodeRuleDT()
								if("7a81a03577a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())//大洋
										|| "1d4462b514a84ee2893c551a355a82d2".equals(UserUtils.getUser().getCompany().getUuid())//非常国际
										|| "7a81c5d777a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())//优加国际
										|| "5c05dfc65cd24c239cd1528e03965021".equals(UserUtils.getUser().getCompany().getUuid())//起航假期
										|| "7a81a26b77a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid()) //拉美途
										|| 0 == UserUtils.getUser().getCompany().getGroupCodeRuleDT())  // 对应需求号   c460
									groupcodeModifiedRecordDao.saveObj(groupcodeModifiedRecord);
							}
						}

					}

				}

				return results;
			}
		}
		else //有重复团号
		{
			JSONObject resobj = new JSONObject();
			resobj.put("flag", "false");
			resobj.put("id", 0);
			results.add(resobj);
			return results;
		}
		//}
		//对非大洋的走之前的旧的逻辑
//		 else if (!"[]".equals(arrays.get(0).toString()) && !"7a81a03577a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid()))
//		 {
//				JSONArray array = JSONArray.fromObject(arrays.get(0));
//				int len = array.size();
//				for(int i=0;i<len;i++){
//					JSONObject resobj = new JSONObject();
//					JSONObject obj = array.getJSONObject(i);
//					String id = obj.getString("id");
//					resobj.put("id", id);
//					String value = obj.getString("value");
//					resobj.put("value", value);
//					if(groupService.groupCodeValidator(value, null))
//						resobj.put("flag", "false");
//					else
//						resobj.put("flag", "true");
//					results.add(resobj);
//				}
//			}

	}
	/**
	 * 判断久团号是不是被修改
	 * @param berforeEditList
	 * @param afterEditList
	 * @return
	 */
	private Set<String> validateChange(List<TravelActivityProperty> berforeEditList,
									   List<TravelActivityProperty> afterEditList) {
		Set<String> set = new HashSet<>();
		for(TravelActivityProperty element : berforeEditList)
		{
			set.add(element.getValue());
		}
		for(TravelActivityProperty element : afterEditList)
		{
			set.add(element.getValue());
		}

		return set;
	}
	/**
	 * 产品添加
	 * @param travelActivity
	 * @param groupOpenDateBegin
	 * @param groupCloseDateEnd
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="save")
	public synchronized String save(TravelActivity travelActivity,String groupOpenDateBegin,String groupCloseDateEnd, HttpServletRequest request, HttpServletResponse response,
									Model model, RedirectAttributes redirectAttributes) {
		return travelActivityService.save(travelActivity, groupOpenDateBegin, groupCloseDateEnd, request, response, model, redirectAttributes, false);
	}

	/**
	 * 产品修改
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="modsave")
	public synchronized String modsave(@RequestBody String groupdata,TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
		try {
			return travelActivityService.modSave(groupdata, travelActivity, request, response, model, redirectAttributes);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "redirect:" + Global.getAdminPath()+"/activity/manager" + "/" + request.getParameter("kind");
		}
	}

	/**
	 * 验证产品编号重复
	 * @param activitySerNum
	 * @param proId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("serNumRepeat")
	public String serNumRepeat(@RequestParam(value="activitySerNum",required=false)String activitySerNum,@RequestParam(value="proId",required=false)String proId) {
		List<TravelActivity> list = travelActivityService.findActivity(activitySerNum,StringUtils.toLong(proId));
		if(!list.isEmpty())
			return "false";
		return "true";
	}

	/**
	 * 获取机票产品(未完成)
	 * @param actNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getTrafficAct")
	public String getTrafficAct(@RequestParam(value="actNum",required=true) String actNum) {

		return "";
	}

	/**
	 * 多文件上传
	 */
	@RequestMapping("uploadFilesPage")
	public String getUploadFilesPage() {
		return "modules/activity/mulUploadFile";
	}

	/**
	 * 单文件上传
	 */
	@RequestMapping("uploadSingleFilePage")
	public String getUploadSingleFilePage() {
		return "modules/activity/mulUploadFileSingle";
	}
	
	/**
	 * 单文件上传追加两个参数（允许上传的图片类型，允许上传的图片大小的最大值）
	 * QU-SDP-微信分销模块start 追加上传单张图片接口添加图片类型，图片大小两个参数 
	 * @author yang.gao
	 * @date   2017-01-06
	 */
	@RequestMapping("uploadSingleFilePageByParam/{type}/{maxSize}")
	public String uploadSingleFilePageByParam(@PathVariable String type, @PathVariable String maxSize, Model model) {
		model.addAttribute("type", type); // 图片类型如 *.jpg;*.jpeg;*.png;*.gif
		model.addAttribute("maxSize", maxSize); // 上传文件的大小限制 ，如果为整数型则表示以KB为单位的大小，如果是字符串，则可以使用(B, KB, MB, or GB)为单位，比如’2MB’；如果设置为0则表示无限制
		return "modules/activity/mulUploadFileSingle";
	}
	
	/**
	 * 通过base64解密获取图片并上传到服务器保存到附件表中
	 * @author yang.gao
	 * @date   2017-01-11
	 */
	@ResponseBody
	@RequestMapping("uploadImgByBaseCode")
	public Map<String, Object> uploadImgByBaseCode(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String image = request.getParameter("image");
        String header = "data:image/png;base64,";
        if(image.indexOf(header) != 0) {
        	map.put("msg", "false");
        	return map;
        }
        image = image.substring(header.length());
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] decodedBytes = decoder.decodeBuffer(image);
            // 1M=1024k=1048576字节
            if (decodedBytes.length > 2*1048576) {
            	// 图片大小不能大于2M
            	map.put("msg", "false-2");
            	return map;
            }
            String pfix = "png";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileNameFull = "微信分销广告图片_" + sdf.format(new Date()) + ".png"; // 图片全名包括图片后缀
	        // 使用唯一标识码生成文件名
	        String newName = UUID.randomUUID().toString() + "." + pfix;
            File uploadFile = null;
			StringBuilder sb = new StringBuilder();
			sb.append(FileUtils.getUploadFilePath().get(1));
			uploadFile = new File(sb.toString());
			if (!uploadFile.exists()) {
				uploadFile.mkdirs();
			}
			uploadFile = new File(sb.toString(), newName);
			FileCopyUtils.copy(decodedBytes, uploadFile);
	        // 保存到DocInfo
	        String docPath = FileUtils.getUploadFilePath().get(0) + newName;
	        DocInfo doc = new DocInfo();
	        doc.setDocName(fileNameFull);
	        doc.setDocPath(docPath);
	        Long docId = docInfoService.saveDocInfo(doc).getId();
            map.put("msg", "success");
            map.put("data", docId.toString());
        } catch(Exception e) {
            map.put("msg", "false");
            e.printStackTrace();
        }
        
        return map;
	}

	/**
	 * 根据机票产品编号查询机票产品
	 */
	@SuppressWarnings({  "rawtypes" })
	@ResponseBody
	@RequestMapping("getAirticketByProCode")
	public List getAirticketByProCode(@RequestParam(value="productCode") String productCode, Model model) {
		return iActivityAirTicketService.findActivityByPorCode(productCode);
	}


	/**
	 *----------------------------------- 填充团号
	 * @param deptId
	 * @param groupOpenDate
	 * @return
	 */

	@ResponseBody
	@RequestMapping("getGroupNum")
	public synchronized String getGroupNum(@RequestParam(value="deptId", required=false)String deptId,
										   @RequestParam(value="groupOpenDate", required=false)String groupOpenDate) {
		//如果批发商是环球行或者低空游轮
		if(68 == UserUtils.getUser().getCompany().getId() || 83 == UserUtils.getUser().getCompany().getId()) {
			return groupService.getGroupNumForTTS(deptId, groupOpenDate);
			//青岛凯撒单团和散拼境外游团号生成，境内游返回空。
		}else if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")){
			return groupService.getGroupNumForCRTS(deptId, groupOpenDate);
			//北京名扬国际旅行社团号规则
		}else if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
			return groupService.getGroupNumForMYGJ(deptId, groupOpenDate);
		}else{
			return sysIncreaseService.updateSysIncrease(UserUtils.getUser().getCompany().getName(),UserUtils.getUser().getCompany().getId(),null,Context.GROUP_NUM_TYPE);
		}
	}



	@ResponseBody
	@RequestMapping("getCurrentDateMaxGroupCode")
	public synchronized String getCurrentDateMaxGroupCode(@RequestParam(value="groupOpenDate", required=false) String groupOpenDate) {
		return groupService.getCurrentDateMaxGroupCode(groupOpenDate);
	}

	/**
	 * 223-tgy:通过库存游轮的uuid获得该游轮的所有船期
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getShipDateByCruiseshipUuid")
	public Map<String, Object> getShipDateByCruiseshipUuid(HttpServletRequest request) throws Exception {
		Map<String, Object> returnMap=new HashMap<String,Object>();
		String cruiseshipUUid=request.getParameter("uuid");
		//--223:根据游轮的uuid查询表cruiseship_stock获得该游轮所拥有的库存舱期信息-s--//
		if("-1".equals(cruiseshipUUid)){ //处理游轮名称为默认请选择的情况
			returnMap.put("shipdateList", "none");
			returnMap.put("shipStockDetailInfo", "none");
		}else{  //处理选择的游轮的情况
			List<Map<String,Object>>  shipDatesOfCruiseship=cruiseshipStockService.getShipDateByCruiseUuid(cruiseshipUUid);
			//获取默认的第一个船期//
			String shipdate=shipDatesOfCruiseship.get(0).get("ship_date").toString();
			//查询cruiseship_stock_detail表中的库存信息ByUUid和船期
			List<Map<String, Object>> shipStockDetailInfo=cruiseshipStockDetailService.getShipStockDetailByUuidAndShipdate(cruiseshipUUid,shipdate);
			returnMap.put("shipdateList", shipDatesOfCruiseship);
			returnMap.put("shipStockDetailInfo", shipStockDetailInfo);
			//--同时将选中的游轮的uuid放入model中--//
		}
		returnMap.put("cruiseshipUUid", cruiseshipUUid);
		return returnMap;
		//--223:根据游轮的uuid查询表cruiseship_stock获得该游轮所拥有的库存舱期信息-e--//
	}
	/**
	 * 根据游轮的uuid和船期查询对应的船期的具体信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getInfoByUuidDate")
	public Map<String, Object> getInfoByUuidDate(HttpServletRequest request) throws Exception {
		Map<String, Object> returnMap=new HashMap<String,Object>();
		String cruiseshipUUid=request.getParameter("uuid");
		String shipdate=request.getParameter("shipdate");
		List<Map<String, Object>> detailInfo=cruiseshipStockDetailService.getShipStockDetailByUuidAndShipdate(cruiseshipUUid,shipdate);
		if(detailInfo!=null){
			returnMap.put("detailInfo", detailInfo);
		}
		return  returnMap;
	}
	/**
	 * 223:tgy
	 * 根据csd表的主键id查询船期信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getDetailsById")
	public Map<String, Object> getDetailsById(HttpServletRequest request) throws Exception {
		Map<String,Object>returnMap=new HashMap<String,Object>();
		String keyId=request.getParameter("keyId");//cruiseship_stock_detail表的主键id
		String agId=request.getParameter("agId"); //如果是已有团期的查看,则这里取到值为团期的id,否则不是
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//指定展示的日期的样式
		if(!("".contains(keyId))){ //keyId有值的时候,才查询
			List<Map<String, Object>>details=cruiseshipStockDetailService.doGetDetailsById(keyId);
			List<Map<String, Object>> tempList=cruiseshipStockGroupRelService.getRelInfo(agId);
			if(details!=null){
				returnMap.put("details", details);
				for (Map<String, Object> map : details) {//处理船期和创建日期的样式
					if(map.get("ship_date")!=null){
						map.put("ship_date", sdf.format(map.get("ship_date")).toString());
					}
				/*if(map.get("create_date")!=null){
				map.put("create_date", sdf.format(map.get("create_date")).toString());
			}*/
					//发布产品时,关联团控日期取当前系统日期,查询中的create_date字段只是为了占位的作用
					//--------加上团期是否有团控关联的条件限制-s-----//
					String relFlag="1";//将关联的默认设置为1,未关联
					String relDate=null;
					String rel_detailId=null;
					if(!(tempList.isEmpty())){
						for (Map<String, Object> m : tempList) {
							relFlag=m.get("rel_status").toString();
							relDate=sdf.format(m.get("rel_date")).toString();
							rel_detailId=m.get("csd_id").toString();
						}
					}
					if(("0".equals(relFlag))&&(keyId.equals(rel_detailId))){ //为关联状态
						map.put("create_date", relDate);
					}else{//未关联状态,关联日期取系统当前日期
						map.put("create_date", sdf.format(new Date()));
					}
					//--------加上团期是否有团控关联的条件限制-e-----//
				}
			}
		}else{
			returnMap.put("details","none");
		}
		return returnMap;
	}

	@ResponseBody
	@RequestMapping("info4CruiseshipNamesUuids")
	public Map<String, Object> info4CruiseshipNamesUuids() throws Exception {
		Map<String, Object> returnMap=new HashMap<String,Object>();
		List<Map<String,Object>> cruiseshipStockList=cruiseshipStockService.getCruiseshipNamesUuids();
		if(cruiseshipStockList!=null){
			returnMap.put("cruiseshipNamesUuids", cruiseshipStockList);
		}else{
			returnMap.put("cruiseshipNamesUuids", "none");
		}
		return returnMap;
	}

	@ResponseBody
	@RequestMapping("infosOfCruiseshipStockDetail")
	public Map<String, Object> infosOfCruiseshipStockDetail(HttpServletRequest request) throws Exception {
		//String keyId=request.getParameter("keyId");
		//由于库存id可能存在重复,所以keyId不能按上述取法,需要关联activitygroup表和cruiseship_group_rel表
		String agId=request.getParameter("agId");//activitygroup表的id
		String csdIdTemp=request.getParameter("csdIdTemp");
		//根据团期id查询表cruiseship_group_rel和activitygroup表,获得关联状态,cruiseship_stock_detail表的id,关联日期和操作人
		List<Map<String,Object>>mapInfos=cruiseshipStockGroupRelService.getRelInfo(agId);//最多只能查询到一条记录,一个团期对应一个库存
		String rel_flag="1";//设置关联标志默认为不关联
		String keyId=null;
		String rel_date=null;
		String operator=null;
		String shipUuid=null;
		String shipDate=null;
		//格式化关联日期格式使用
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		//库存所有游轮名称
		Map<String, Object> returnMap=new HashMap<String,Object>();
		if((!(mapInfos.isEmpty()))&&(!("".contains(csdIdTemp)))){ //关联信息不为空和未点击取消状态(点击了取消状态,csdIdTemp值会为空)

			for (Map<String, Object> mapTemp : mapInfos) {
				rel_flag=mapTemp.get("rel_status").toString();
				keyId=mapTemp.get("csd_id").toString();
				rel_date=sdf.format(mapTemp.get("rel_date")).toString();
				operator=mapTemp.get("operator").toString();
			}
			//----处理多次点击编辑关联团控带出上一次的选择结果---s//
			if(keyId.equals(csdIdTemp)){ //多次点击编辑关联团控,但未改变csdId的值时
				//团控关联时(rel_flag值为0),才进行关联团控信息的展示
				if("0".equals(rel_flag)){
					if(null!=keyId){  //不为空时才进行查询
						List<Map<String,Object>> infoList=cruiseshipStockDetailService.doGetDetailsById(keyId);
						returnMap.put("infoList", infoList);
						if(null!=rel_date){
							returnMap.put("rel_date",rel_date ) ;
						}
						if(null!=operator){
							returnMap.put("operator",operator) ;
						}
						//获取该游轮的所有船期
						for (Map<String, Object> map : infoList) {
							shipUuid=map.get("ship_uuid").toString();
							shipDate=map.get("ship_date").toString();
						}
						if(null!=shipUuid){
							List<Map<String, Object>> shipdatesList= cruiseshipStockService.getShipDateByCruiseUuid(shipUuid);
							returnMap.put("shipdatesList", shipdatesList);
							//获取该游轮该船期的具体船期信息(通过游轮的uuid和船期)
							if(null!=shipDate){
								List<Map<String, Object>> detailsInfo=cruiseshipStockDetailService.getShipStockDetailByUuidAndShipdate(shipUuid,shipDate);
								returnMap.put("detailsInfo", detailsInfo);
								returnMap.put("selShipDate", shipDate);
							}
						}

					}
				}
			}else{  //多次点击编辑关联团控时,改变了csdId值时,带出上一次选择的记录
				//根据csdIdTemp获得与之对应的在cruiseship_stock_detail表中的记录信息
				List<Map<String, Object>>tempList= cruiseshipStockDetailService.doGetDetailsById(csdIdTemp);//查询到的只有一条记录
				String tempShipuuid=null;
				String tempShipdate=null;
				for (Map<String, Object> map : tempList) {
					tempShipuuid=map.get("ship_uuid").toString();
					tempShipdate=map.get("ship_date").toString();
				}
				//获得该库存游轮的所有船期
				List<Map<String, Object>>tempShipdateList=cruiseshipStockService.getShipDateByCruiseUuid(tempShipuuid);
				//获得当前游轮当前船期的库存信息
				List<Map<String,Object>>tempDetailsInfo=cruiseshipStockDetailService.getShipStockDetailByUuidAndShipdate(tempShipuuid, tempShipdate);
				returnMap.put("shipdatesList",tempShipdateList);
				returnMap.put("detailsInfo",tempDetailsInfo);
				returnMap.put("rel_date","" ) ;
				returnMap.put("operator","");
				returnMap.put("selShipDate",tempShipdate);//选中的船期
			}

			//----处理多次点击编辑关联团控带出上一次的选择结果---e//
		}else{
			//returnMap.put("infoList","none");
			returnMap.put("shipdatesList", "none");
			returnMap.put("detailsInfo","none");
			returnMap.put("rel_date","" ) ;
			returnMap.put("operator","");
			returnMap.put("selShipDate","");//选中的船期
		}

		//查询库存所有游轮的名称--s//
		List<Map<String, Object>> shipNamesUuids=cruiseshipStockService.getCruiseshipNamesUuids();
		if(!(shipNamesUuids.isEmpty())){
			returnMap.put("shipNamesUuids", shipNamesUuids);
		}else{
			returnMap.put("shipNamesUuids", "none");
		}
		//String keyId=null;
		/*//格式化关联日期格式使用
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		//库存所有游轮名称
		Map<String, Object> returnMap=new HashMap<String,Object>();*/
		/*if("".contains(keyId)){ //处理keyId值为空的情况
			returnMap.put("infoList","none");
			returnMap.put("shipdatesList", "none"); 
			returnMap.put("detailsInfo","none");
			returnMap.put("shipNamesUuids", "none");
		}else{
		 //------------注释掉----s------//	
			//根据id查询某游轮某船期的具体信息,查询到的记录最多只有一条(根据cruiseship_stock_detail表的主键id)
		  List<Map<String,Object>> infoList=cruiseshipStockDetailService.doGetDetailsById(keyId);
		  if(!(infoList.isEmpty())){ 
			  returnMap.put("infoList", infoList);
			//查询当前infoList中查询到的游轮的所有船期
			  for (Map<String, Object> map: infoList) {
				//System.out.println("----"+map.get("ship_uuid"));
			   //关联日期	  
			  if(map.get("rel_date")!=null){
				  map.put("rel_date", sdf.format(map.get("rel_date")));
			  }else{
				  map.put("rel_date","");//处理关联日期为null值的情况
			  }
			  //操作人
			  if(map.get("operator")!=null){
				  map.put("operator",map.get("operator").toString());
			  }else{
				  map.put("operator","");//处理操作人为null值的情况
			  }
			  if(map.get("ship_uuid")!=null){
			  List<Map<String, Object>> shipdatesList= cruiseshipStockService.getShipDateByCruiseUuid(map.get("ship_uuid").toString());
			  List<Map<String, Object>> detailsInfo=cruiseshipStockDetailService.getShipStockDetailByUuidAndShipdate(map.get("ship_uuid").toString(), map.get("ship_date").toString());
			    returnMap.put("shipdatesList", shipdatesList);
			    returnMap.put("detailsInfo",detailsInfo);
			  }else{
				returnMap.put("shipdatesList", "none"); 
				returnMap.put("detailsInfo","none");
			  }
			}
		  }
		//------------注释掉----e------//	 
		}
		
		//查询库存所有游轮的名称--s//
		List<Map<String, Object>> shipNamesUuids=cruiseshipStockService.getCruiseshipNamesUuids();
		if(!(shipNamesUuids.isEmpty())){
			returnMap.put("shipNamesUuids", shipNamesUuids);
		}else{
			returnMap.put("shipNamesUuids", "none");
		}
		//查询库存所有游轮的名称--e//
*/		return returnMap;
	}

	//-----------t1t2需求-----------s--//
	/**
	 * 首页
	 * @param request
	 * @param response
	 * @param model
	 * @param travelActivity
	 * @return
	 */
	@RequestMapping(value="homepagelist")
	public String homePage(HttpServletRequest request,HttpServletResponse response,Model model,@ModelAttribute TravelActivity travelActivity){

		//搜索条件
		String keyword = request.getParameter("keyword");//搜索关键字
		String keywordHidden = request.getParameter("keywordHidden");//隐藏搜索关键字
//        String fromArea=request.getParameter("fromArea");//出发城市
		String startCityPara = request.getParameter("startCityPara");//出发城市
//        String targetAreaIdList=request.getParameter("targetAreaIdList");//目的地
		String countryPara = request.getParameter("countryPara");//国家
		String endCityPara = request.getParameter("endCityPara");//目的地
//        String supplier=request.getParameter("supplier");//供应商
		String supplierPara = request.getParameter("supplierPara");//供应商
		String groupOpenDateBegin = request.getParameter("groupDatePara");//出团日期开始日期
		String groupOpenDateEnd = request.getParameter("groupOpenDateEnd");//出团日期结束日期--已不用
		String activityDurationFrom = request.getParameter("dayPara");//行程天数开始天数
		String activityDurationTo = request.getParameter("activityDurationTo");//行程天数结束天数--已不用
		String dayPara = request.getParameter("dayPara");//行程天数
		String pricePara = request.getParameter("pricePara");//价格
		String freePositionFrom = request.getParameter("freePara");//开始余位
		String freePostionTo = request.getParameter("freePostionTo");//结束余位--已不用
		String pageNo = request.getParameter("pageNo");//当前页码
		String pageSize = request.getParameter("pageSize");//每页显示记录数
		String orderBy = request.getParameter("orderBy");//排序
		String type = request.getParameter("type");//出境游、国内游
		if (StringUtils.isBlank(type)) {
			type = "100000";
		}
		model.addAttribute("type", type);

		if(("undefined,").equals(startCityPara)) {
			startCityPara = "";
		}
		if(("undefined,").equals(countryPara)) {
			countryPara = "";
		}
		if(("undefined,").equals(endCityPara)) {
			endCityPara = "";
		}
		if(("undefined,").equals(supplierPara)) {
			supplierPara = "";
		}

		String startCityPara4Show = request.getParameter("startCityPara4Show");//出发城市
		String countryPara4Show = request.getParameter("countryPara4Show");//国家
		String endCityPara4Show = request.getParameter("endCityPara4Show");//目的地
		String supplierPara4Show = request.getParameter("supplierPara4Show");//供应商
		String groupDatePara4Show = request.getParameter("groupDatePara4Show");//出团日期开始日期
		String dayPara4Show = request.getParameter("dayPara4Show");//行程天数
		String pricePara4Show = request.getParameter("pricePara4Show");//价格
		String freePara4Show = request.getParameter("freePara4Show");//开始余位

		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		//出发城市
		if (StringUtils.isNotBlank(startCityPara4Show)) {
			JSONArray startArray = new JSONArray();
			String[] startCityObjs = startCityPara4Show.split(",");
			for (int i = 0; i < startCityObjs.length; i++) {
				String[] startCityObj = startCityObjs[i].split(":");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", startCityObj[0]);
				jsonObject.put("name", startCityObj[1]);
				startArray.add(jsonObject);
			}
			JSONObject start = new JSONObject();
			start.element("出发城市", startArray);
			jsonList.add(start);
		}
		//国家--改为目的地
		if (StringUtils.isNotBlank(countryPara4Show)) {
			JSONArray countryArray = new JSONArray();
			String[] countryObjs = countryPara4Show.split(",");
			for (int i = 0; i < countryObjs.length; i++) {
				String[] countryObj = countryObjs[i].split(":");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", countryObj[0]);
				jsonObject.put("name", countryObj[1]);
				countryArray.add(jsonObject);
			}
			JSONObject country = new JSONObject();
			country.element("目的地", countryArray);
			jsonList.add(country);
		}
		//目的地--改为抵达城市
		if (StringUtils.isNotBlank(endCityPara4Show)) {
			JSONArray endArray = new JSONArray();
			String[] endCityObjs = endCityPara4Show.split(",");
			for (int i = 0; i < endCityObjs.length; i++) {
				String[] endCityObj = endCityObjs[i].split(":");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", endCityObj[0]);
				jsonObject.put("name", endCityObj[1]);
				endArray.add(jsonObject);
			}
			JSONObject end = new JSONObject();
			end.element("抵达城市", endArray);
			jsonList.add(end);
		}
		//供应商
		JSONArray supplierArray = new JSONArray();
		if (StringUtils.isNotBlank(supplierPara4Show)) {
			String[] supplierObjs = supplierPara4Show.split(",");
			for (int i = 0; i < supplierObjs.length; i++) {
				String[] supplierObj = supplierObjs[i].split(":");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", supplierObj[0]);
				jsonObject.put("name", supplierObj[1]);
				supplierArray.add(jsonObject);
			}
			JSONObject supplier = new JSONObject();
			supplier.element("供应商", supplierArray);
			jsonList.add(supplier);
		}
		//出团日期
		if (StringUtils.isNotBlank(groupDatePara4Show)) {
			JSONArray groupDateArray = new JSONArray();
			String[] groupDateObjs = groupDatePara4Show.split(",");
			for (int i = 0; i < groupDateObjs.length; i++) {
				String[] groupDateObj = groupDateObjs[i].split(":");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", groupDateObj[0]);
				jsonObject.put("name", groupDateObj[0]);
				groupDateArray.add(jsonObject);
			}
			JSONObject groupDate = new JSONObject();
			groupDate.element("出团日期", groupDateArray);
			jsonList.add(groupDate);
		}
		//行程天数
		if (StringUtils.isNotBlank(dayPara4Show)) {
			JSONArray dayArray = new JSONArray();
			if(dayPara4Show.contains(",") || dayPara4Show.contains(":")) {
				String[] dayObjs = dayPara4Show.split(",");
				for (int i = 0; i < dayObjs.length; i++) {
					String[] dayObj = dayObjs[i].split(":");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", dayObj[0]);
					jsonObject.put("name", dayObj[1]);
					dayArray.add(jsonObject);
				}
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", dayPara4Show);
				jsonObject.put("name", dayPara4Show);
				dayArray.add(jsonObject);
			}
			JSONObject day = new JSONObject();
			day.element("行程天数", dayArray);
			jsonList.add(day);
		}
		//价格
		if (StringUtils.isNotBlank(pricePara4Show)) {
			JSONArray priceArray = new JSONArray();
			if(pricePara4Show.contains(",") || pricePara4Show.contains(":")) {
				String[] priceObjs = pricePara4Show.split(",");
				for (int i = 0; i < priceObjs.length; i++) {
					String[] priceObj = priceObjs[i].split(":");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", priceObj[0]);
					jsonObject.put("name", priceObj[1]);
					priceArray.add(jsonObject);
				}
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", pricePara4Show);
				jsonObject.put("name", pricePara4Show);
				priceArray.add(jsonObject);
			}
			JSONObject price = new JSONObject();
			price.element("价格区间", priceArray);
			jsonList.add(price);
		}
		//余位
		if (StringUtils.isNotBlank(freePara4Show)) {
			JSONArray freeArray = new JSONArray();
			if(freePara4Show.contains(",") || freePara4Show.contains(":")) {
				String[] freeObjs = freePara4Show.split(",");
				for (int i = 0; i < freeObjs.length; i++) {
					String[] freeObj = freeObjs[i].split(":");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", freeObj[0]);
					jsonObject.put("name", freeObj[1]);
					freeArray.add(jsonObject);
				}
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", freePara4Show);
				jsonObject.put("name", freePara4Show);
				freeArray.add(jsonObject);
			}
			JSONObject free = new JSONObject();
			free.element("余位", freeArray);
			jsonList.add(free);
		}

		JSONArray json4Search = JSONArray.fromObject(jsonList);
		model.addAttribute("json4Search", json4Search.toString());

		//-----分页查询-s------------------------------------------------------------------------------------//
		try {
			Page<TravelActivity> pageForProductAbove = new Page<TravelActivity>(request, response);
			pageForProductAbove.setToStringFlag("t1ForProductAbove");
			Page<TravelActivity> page = (Page<TravelActivity>) travelActivityService.findActivityGroupInfos(pageForProductAbove, travelActivity,
					keyword,startCityPara,endCityPara,supplierPara,groupOpenDateBegin,groupOpenDateEnd,dayPara,activityDurationTo,pricePara,freePositionFrom,freePostionTo,countryPara,pageNo,pageSize,orderBy,type);
			model.addAttribute("page", page);

			Page<TravelActivity> page2 = new Page<TravelActivity>(request, response);
			BeanUtils.copyProperties(page, page2);
			page2.setToStringFlag("t1ForProductBottom");
			model.addAttribute("page2", page2);
			//=========查询日志 add by chao.zhang=======//
			Integer userId = UserUtils.getUser().getId().intValue();
			Date date = new Date();
			String productType =  "2";
			if(StringUtils.isNotBlank(keyword)){
				SearchLog searchLog = new SearchLog();
				searchLog.setBugetType(type);
				searchLog.setCount(page.getCount());
				searchLog.setCreateBy(userId);
				searchLog.setCreateDate(date);
				searchLog.setMessage(keyword);
				//散拼
				searchLog.setProductType(productType);
				searchLog.setSearchType(SearchLog.INPUT_TYPE);
				searchLogService.log(searchLog);
			}
			//出发城市
			if (StringUtils.isNotBlank(startCityPara4Show)) {
				String[] startCityObjs = startCityPara4Show.split(",");
				for (int i = 0; i < startCityObjs.length; i++) {
					String[] startCityObj = startCityObjs[i].split(":");
					SearchLog searchLog = new SearchLog();
					searchLog.setBugetType(type);
					searchLog.setCount(page.getCount());
					searchLog.setCreateBy(userId);
					searchLog.setCreateDate(date);
					searchLog.setMessage(startCityObj[1]);
					//散拼
					searchLog.setProductType(productType);
					searchLog.setSearchType(SearchLog.FROM_TYPE);
					searchLogService.log(searchLog);
				}
			}
			//国家--改为目的地
			if (StringUtils.isNotBlank(countryPara4Show)) {
				String[] countryObjs = countryPara4Show.split(",");
				for (int i = 0; i < countryObjs.length; i++) {
					String[] countryObj = countryObjs[i].split(":");
					SearchLog searchLog = new SearchLog();
					searchLog.setBugetType(type);
					searchLog.setCount(page.getCount());
					searchLog.setCreateBy(userId);
					searchLog.setCreateDate(date);
					searchLog.setMessage(countryObj[1]);
					//散拼
					searchLog.setProductType(productType);
					searchLog.setSearchType(SearchLog.TARGET_TYPE);
					searchLogService.log(searchLog);
				}
			}
			//目的地--改为抵达城市
			if (StringUtils.isNotBlank(endCityPara4Show)) {
				String[] endCityObjs = endCityPara4Show.split(",");
				for (int i = 0; i < endCityObjs.length; i++) {
					String[] endCityObj = endCityObjs[i].split(":");
					SearchLog searchLog = new SearchLog();
					searchLog.setBugetType(type);
					searchLog.setCount(page.getCount());
					searchLog.setCreateBy(userId);
					searchLog.setCreateDate(date);
					searchLog.setMessage(endCityObj[1]);
					//散拼
					searchLog.setProductType(productType);
					searchLog.setSearchType(SearchLog.ARRIVAL_TYPE);
					searchLogService.log(searchLog);
				}
			}
			//供应商
			if (StringUtils.isNotBlank(supplierPara4Show)) {
				String[] supplierObjs = supplierPara4Show.split(",");
				for (int i = 0; i < supplierObjs.length; i++) {
					String[] supplierObj = supplierObjs[i].split(":");
					SearchLog searchLog = new SearchLog();
					searchLog.setBugetType(type);
					searchLog.setCount(page.getCount());
					searchLog.setCreateBy(userId);
					searchLog.setCreateDate(date);
					searchLog.setMessage(supplierObj[1]);
					//散拼
					searchLog.setProductType(productType);
					searchLog.setSearchType(SearchLog.SUPPLY_TYPE);
					searchLogService.log(searchLog);
				}
			}
			//出团日期
			if (StringUtils.isNotBlank(groupDatePara4Show)) {
				String[] groupDateObjs = groupDatePara4Show.split(",");
				for (int i = 0; i < groupDateObjs.length; i++) {
					String[] groupDateObj = groupDateObjs[i].split(":");
					SearchLog searchLog = new SearchLog();
					searchLog.setBugetType(type);
					searchLog.setCount(page.getCount());
					searchLog.setCreateBy(userId);
					searchLog.setCreateDate(date);
					String groupDates = groupDateObj[0];
					String groupDate = "";
					if(groupDates.charAt(groupOpenDateBegin.length() - 1) == '-'){
						String str = groupDates.substring(0,groupDates.length()-1);
						groupDate = str+"~";
					}else if(groupDates.charAt(0) == '-'){
						groupDates = groupDates.substring(1);
						groupDate = "~" + groupDates;
					}else{
						String str1 = groupDateObj[0].substring(0, 10);
						String str2 = groupDateObj[0].substring(11);
						groupDate = str1+"~"+str2;
					}
					searchLog.setMessage(groupDate);
					//散拼
					searchLog.setProductType(productType);
					searchLog.setSearchType(SearchLog.OPENDATE_TYPE);
					searchLogService.log(searchLog);
				}
			}
			//行程天数
			if (StringUtils.isNotBlank(dayPara4Show)) {
				if(dayPara4Show.contains(",") || dayPara4Show.contains(":")) {
					String[] dayObjs = dayPara4Show.split(",");
					for (int i = 0; i < dayObjs.length; i++) {
						String[] dayObj = dayObjs[i].split(":");
						SearchLog searchLog = new SearchLog();
						searchLog.setBugetType(type);
						searchLog.setCount(page.getCount());
						searchLog.setCreateBy(userId);
						searchLog.setCreateDate(date);
						searchLog.setMessage(dayObj[1]);
						//散拼
						searchLog.setProductType(productType);
						searchLog.setSearchType(SearchLog.DAYCOUNT_TYPE);
						searchLogService.log(searchLog);
					}
				} else {
					SearchLog searchLog = new SearchLog();
					searchLog.setBugetType(type);
					searchLog.setCount(page.getCount());
					searchLog.setCreateBy(userId);
					searchLog.setCreateDate(date);
					searchLog.setMessage(dayPara4Show);
					//散拼
					searchLog.setProductType(productType);
					searchLog.setSearchType(SearchLog.DAYCOUNT_TYPE);
					searchLogService.log(searchLog);
				}
			}
			//价格
			if (StringUtils.isNotBlank(pricePara4Show)) {
				if(pricePara4Show.contains(",") || pricePara4Show.contains(":")) {
					String[] priceObjs = pricePara4Show.split(",");
					for (int i = 0; i < priceObjs.length; i++) {
						String[] priceObj = priceObjs[i].split(":");
						SearchLog searchLog = new SearchLog();
						searchLog.setBugetType(type);
						searchLog.setCount(page.getCount());
						searchLog.setCreateBy(userId);
						searchLog.setCreateDate(date);
						searchLog.setMessage(priceObj[1]);
						//散拼
						searchLog.setProductType(productType);
						searchLog.setSearchType(SearchLog.PRICE_TYPE);
						searchLogService.log(searchLog);
					}
				} else {
					SearchLog searchLog = new SearchLog();
					searchLog.setBugetType(type);
					searchLog.setCount(page.getCount());
					searchLog.setCreateBy(userId);
					searchLog.setCreateDate(date);
					searchLog.setMessage(pricePara4Show);
					//散拼
					searchLog.setProductType(productType);
					searchLog.setSearchType(SearchLog.PRICE_TYPE);
					searchLogService.log(searchLog);
				}
			}
			//余位
			if (StringUtils.isNotBlank(freePara4Show)) {
				if(freePara4Show.contains(",") || freePara4Show.contains(":")) {
					String[] freeObjs = freePara4Show.split(",");
					for (int i = 0; i < freeObjs.length; i++) {
						String[] freeObj = freeObjs[i].split(":");
						SearchLog searchLog = new SearchLog();
						searchLog.setBugetType(type);
						searchLog.setCount(page.getCount());
						searchLog.setCreateBy(userId);
						searchLog.setCreateDate(date);
						searchLog.setMessage(freeObj[1]);
						//散拼
						searchLog.setProductType(productType);
						searchLog.setSearchType(SearchLog.SEAT_TYPE);
						searchLogService.log(searchLog);
					}
				} else {
					SearchLog searchLog = new SearchLog();
					searchLog.setBugetType(type);
					searchLog.setCount(page.getCount());
					searchLog.setCreateBy(userId);
					searchLog.setCreateDate(date);
					searchLog.setMessage(freePara4Show);
					//散拼
					searchLog.setProductType(productType);
					searchLog.setSearchType(SearchLog.SEAT_TYPE);
					searchLogService.log(searchLog);
				}
			}
			//============搜索日志结束=============//
		} catch (Exception e) {
			e.printStackTrace();
		}

		//-----分页查询-e-----------------------------------------------------------------------------------//
		//查询数据取T2后台基础信息维护中的所有目的地-0426T1T2,根据type和delFlag
		List<Dict> fromAreas = DictUtils.getDictByType("from_area");
		model.addAttribute("fromAreas", fromAreas);

		List<Dict> fromAreaList = DictUtils.getFromArea4T1(type);
		model.addAttribute("fromAreaList", fromAreaList);

		model.addAttribute("supplierInfos", officeService.getOffice4T1(type));

		//国家
		List<Map<String, Object>> countrys = areaService.getCountry4T1();
		model.addAttribute("countrys", countrys);

		//城市
//		model.addAttribute("targetAreas", filterTreeData4T1(null, 2, null));
		List<Map<String, Object>> targetAreas = areaService.getTargetArea4T1(type);
		model.addAttribute("targetAreas", targetAreas);

		model.addAttribute("groupOpenDateBegin", groupOpenDateBegin);
		model.addAttribute("groupOpenDateEnd",groupOpenDateEnd);
		model.addAttribute("activityDurationFrom", activityDurationFrom);
		model.addAttribute("activityDurationTo", activityDurationTo);
		model.addAttribute("freePositionFrom", freePositionFrom);
		model.addAttribute("freePostionTo", freePostionTo);
		model.addAttribute("travelActivity",travelActivity);
		model.addAttribute("keyword",keyword);
		model.addAttribute("keywordHidden", keywordHidden);
		//----------放入查询的条件的值于model中,作为搜索条件的默认值-----//

		return "modules/homepage/homePageList";
	}

//	@ResponseBody
//	@RequestMapping(value="homepagelist4Ajax")
//	public Map<String, Object> homePage4Ajax(HttpServletRequest request,HttpServletResponse response){
//		//搜索条件
//		String keyword = request.getParameter("keyword");//搜索关键字
////        String fromArea=request.getParameter("fromArea");//出发城市
//		String startCityPara = request.getParameter("startCityPara");//出发城市
////        String targetAreaIdList=request.getParameter("targetAreaIdList");//目的地
//		String endCityPara = request.getParameter("endCityPara");//目的地
////        String supplier=request.getParameter("supplier");//供应商
//		String supplierPara = request.getParameter("supplierPara");//供应商
//		String groupOpenDateBegin = request.getParameter("groupDatePara");//出团日期开始日期
//		String groupOpenDateEnd = request.getParameter("groupOpenDateEnd");//出团日期结束日期--已不用
//		String activityDurationFrom = request.getParameter("dayPara");//行程天数开始天数
//		String activityDurationTo = request.getParameter("activityDurationTo");//行程天数结束天数--已不用
//		String dayPara = request.getParameter("dayPara");//行程天数
//		String pricePara = request.getParameter("pricePara");//价格
//		String freePositionFrom = request.getParameter("freePara");//开始余位
//		String freePostionTo = request.getParameter("freePostionTo");//结束余位--已不用
//		String pageNo = request.getParameter("pageNo");//当前页码
//		String pageSize = request.getParameter("pageSize");//每页显示记录数
//		String orderBy = request.getParameter("orderBy");//排序
////        User user = UserUtils.getUser();
////        Long companyId = user.getCompany().getId();
//
//		//-----分页查询-s------------------------------------------------------------------------------------//
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			Page4T1<TravelActivity> page = (Page4T1<TravelActivity>) travelActivityService.findActivityGroupInfos(new Page4T1<TravelActivity>(request, response), null,
//					keyword,startCityPara,endCityPara,supplierPara,groupOpenDateBegin,groupOpenDateEnd,dayPara,activityDurationTo,pricePara,freePositionFrom,freePostionTo,pageNo,pageSize,orderBy);
////			model.addAttribute("page", page);
//			map.put("page", page);
//
//			Page4T1_2<TravelActivity> page2 = (Page4T1_2<TravelActivity>) travelActivityService.findActivityGroupInfos(new Page4T1_2<TravelActivity>(request, response), null,
//					keyword,startCityPara,endCityPara,supplierPara,groupOpenDateBegin,groupOpenDateEnd,dayPara,activityDurationTo,pricePara,freePositionFrom,freePostionTo,pageNo,pageSize,orderBy);
////			model.addAttribute("page2", page2);
//			map.put("page2", page2);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return map;
//	}

	/**
	 * 通过groupCode查看详情
	 * @param groupCode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="viewDetail/{activityId}/{groupCode}")
	public String viewDetail(@PathVariable Long activityId, @PathVariable String groupCode, Model model) throws UnsupportedEncodingException {
		Map<String, Object> detail = travelActivityService.getDetail(activityId, groupCode);
		model.addAttribute("detail", detail);
		//出发城市
		List<Dict> fromAreasList=DictUtils.getDictByType("from_area");
		model.addAttribute("fromAreas", fromAreasList);
		//文件id
		TravelActivity travelActivity = travelActivityService.findById(activityId);
		Set<ActivityFile> activityFiles = travelActivity.getActivityFiles();
		model.addAttribute("activityFiles", activityFiles);
		Office office = officeService.get(travelActivity.getProCompany());
		model.addAttribute("office", office);
		model.addAttribute("docInfoId", office.getLogo());
		String docIds = "";
		for (Iterator<ActivityFile> iterator = activityFiles.iterator(); iterator.hasNext(); ) {
			ActivityFile file = iterator.next();
			docIds += file.getSrcDocId() + ",";
		}
		model.addAttribute("docIds", docIds);

		// 资质证书
		List<DocInfo> businessCertificate = docInfoService.getDocInfoByStringIds(";",office.getBusinessCertificate());
		model.addAttribute("businessCertificate", businessCertificate);
		// 营业执照
		List<DocInfo> businessLicense = docInfoService.getDocInfoByStringIds(";",office.getBusinessLicense());
		model.addAttribute("businessLicense", businessLicense);
		// 合作协议
		List<DocInfo> cooperationProtocol = docInfoService.getDocInfoByStringIds(";",office.getCooperationProtocol());
		model.addAttribute("cooperationProtocol", cooperationProtocol);

		// 交通方式
		Long companyId = office.getId();
		List<SysDefineDict> trafficModeList = DictUtils.getDefineDictByCompanyIdAndType(Context.TRAFFIC_MODE, companyId);
		model.addAttribute("trafficModeList", trafficModeList);
		// 销售
		List<Map<String, Object>> salers = UserUtils.getSalerInfoList(companyId);
		model.addAttribute("salers", salers);
		Subject currentUser = SecurityUtils.getSubject();
		model.addAttribute("t1SalerRebate", currentUser.isPermitted("t1:saler:rebate"));
		return "modules/homepage/detail";
	}
	/**
	 * T1首页的详情查看
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="viewDetails")
	public Map<String,Object> viewDetails(HttpServletRequest request){
		String activitygroupId=request.getParameter("activitygroupId");
		String travelactivityId=request.getParameter("travelactivityId");
		//List<Map<String, Object>> infoList=new ArrayList<>();
		Map<String,Object> infoMap=new HashMap<>();
		//根据产品id查询到产品信息-----------这段代码是拷贝自产品->散拼->详情的代码--------------------------s//
		TravelActivity activity = null;
		ActivityGroup activityGroup=null;
		if(StringUtils.isNotBlank(travelactivityId)){
			activity = travelActivityService.findById(Long.parseLong(travelactivityId));//这样查到的记录应该只有一条

			//去掉activity中的重复文件信息，开始
			Set<ActivityFile> activityFiles = activity.getActivityFiles();
			if(null != activityFiles && activityFiles.size() > 0){
				List<ActivityFile> fileIds = new ArrayList<ActivityFile>();
				for(ActivityFile file:activityFiles){
					if(!fileIds.contains(file)){
						fileIds.add(file);
					}
				}

				activityFiles.clear();
				for(ActivityFile activityFile : fileIds){
					activityFiles.add(activityFile);
				}
			}
			//去掉activity中的重复文件信息，结束
			//将详情需要的信息放入map中
			infoMap.put("acitivityName", (activity.getAcitivityName())==null?"":activity.getAcitivityName());//产品名称
			infoMap.put("activityDuration",(activity.getActivityDuration())==null?"":activity.getActivityDuration());//行程天数
			infoMap.put("trafficModeName",(activity.getTrafficModeName())==null?"":activity.getTrafficModeName());//交通方式
			infoMap.put("fromAreaName",(activity.getFromAreaName())==null?"":activity.getFromAreaName());//出发城市
			infoMap.put("targetAreaNames",(activity.getTargetAreaNames())==null?"":activity.getTargetAreaNames());//目的地,名称以逗号分隔
			//获得产品行程的文件id
			Set<ActivityFile> files= activity.getActivityFiles();
			if(!files.isEmpty()){ //产品行程文件不为空的时候,多文件下载
				List<String>docIdsList=new ArrayList<>();//存放产品行程介绍文件的id的list
				for (ActivityFile af : files) { //
					docIdsList.add(af.getSrcDocId().toString());
				}
				//将文件的ids逗号分隔拼接放入infoMap中
				infoMap.put("docIds", StringUtils.join(docIdsList, ","));
			}
		}
		//根据产品id查询到产品信息----------------------------------------------------------------e//
		//根据团期id,查询到详情所要的--------------------------------------------------------------s//
		if(StringUtils.isNotBlank(activitygroupId)){ //团期id不为空
			activityGroup= groupService.findById(Long.valueOf(activitygroupId));//根据id只能查询到一条记录
			if(null!=activityGroup){
				infoMap.put("groupCode",activityGroup.getGroupCode());
				infoMap.put("groupOpenDate",DateUtils.date2String(activityGroup.getGroupOpenDate(), "yyyy-MM-dd"));//出团日期
				infoMap.put("groupCloseDate",DateUtils.date2String(activityGroup.getGroupCloseDate(), "yyyy-MM-dd"));//截团日期
				infoMap.put("freePosition",activityGroup.getFreePosition());//余位
				infoMap.put("currencyType",activityGroup.getCurrencyType());//币种符号id,逗号分隔
				//quauq价
				infoMap.put("quauqAdultPriceMark",(CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 0, "mark")==null)?"":CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 6, "mark"));//quauq成人价币种符号
				infoMap.put("quauqAdultPriceName",CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 6, "name"));//quauq成人价币种名称
				infoMap.put("quauqAdultPrice",(activityGroup.getQuauqAdultPrice()==null)?"":activityGroup.getQuauqAdultPrice().setScale(2,RoundingMode.HALF_UP).toString());//quauq成人价数额
				infoMap.put("quauqChildPriceMark",(CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 1, "mark"))==null?"":CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 7, "mark"));
				infoMap.put("quauqChildPriceName",CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 7, "name"));
				infoMap.put("quauqChildPrice",(activityGroup.getQuauqChildPrice())==null?"":activityGroup.getQuauqChildPrice().setScale(2,RoundingMode.HALF_UP).toString());//quauq儿童价数额
				infoMap.put("quauqSpecialPriceMark",(CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(),2, "mark")==null)?"":CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(),8, "mark"));
				infoMap.put("quauqSpecialPriceName",CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(),8, "name"));
				infoMap.put("quauqSpecialPrice",(activityGroup.getQuauqSpecialPrice())==null?"":activityGroup.getQuauqSpecialPrice().setScale(2,RoundingMode.HALF_UP).toString());//quauq特殊人群价
				//建议直客价
				infoMap.put("suggestAdultPriceMark",(CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 3, "mark"))==null?"":CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 3, "mark"));
				infoMap.put("suggestAdultPriceName",CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 3, "name"));
				infoMap.put("suggestAdultPrice",(activityGroup.getSuggestAdultPrice())==null?"":activityGroup.getSuggestAdultPrice().setScale(2,RoundingMode.HALF_UP).toString());//建议直客成人价
				infoMap.put("suggestChildPriceMark",(CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 4, "mark"))==null?"":CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 4, "mark"));
				infoMap.put("suggestChildPriceName",CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(), 4, "name"));
				infoMap.put("suggestChildPrice",(activityGroup.getSuggestChildPrice())==null?"":activityGroup.getSuggestChildPrice().setScale(2,RoundingMode.HALF_UP).toString());//建议直客儿童价
				infoMap.put("suggestSpecialPriceMark",(CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(),5, "mark"))==null?"":CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(),5, "mark"));
				infoMap.put("suggestSpecialPriceName",CurrencyUtils.getCurrencyInfo(activityGroup.getCurrencyType(),5, "name"));
				infoMap.put("suggestSpecialPrice",(activityGroup.getSuggestSpecialPrice())==null?"":activityGroup.getSuggestSpecialPrice().setScale(2,RoundingMode.HALF_UP).toString());//建议直客特殊人群价
				//服务费:取值规则,对应QUAUQ价*1%;所以其币种符号和名称与quauq价相同
				BigDecimal per=new BigDecimal("0.01");//计算比例
				if(null!=activityGroup.getQuauqAdultPrice()){
					infoMap.put("quauqAdultPriceProfit",activityGroup.getQuauqAdultPrice().multiply(per).setScale(2,RoundingMode.HALF_UP).toString());//服务费成人价
				}else{
					infoMap.put("quauqAdultPriceProfit","");
				}
				if(null!=activityGroup.getQuauqChildPrice()){
					infoMap.put("quauqChildPriceProfit",activityGroup.getQuauqChildPrice().multiply(per).setScale(2,RoundingMode.HALF_UP).toString());//服务费儿童价
				}else{
					infoMap.put("quauqChildPriceProfit","");
				}
				if(null!=activityGroup.getQuauqSpecialPrice()){
					infoMap.put("quauqSpecialPriceProfit",activityGroup.getQuauqSpecialPrice().multiply(per).setScale(2,RoundingMode.HALF_UP).toString());//服务费特殊价
				}else{
					infoMap.put("quauqSpecialPriceProfit","");
				}
			}
		}
		//根据团期id,查询到详情所要的--------------------------------------------------------------e//
		//查询sys_user表中具有账号管理->修改->QUAUQ权限 quauqBookOrderPermission=1的用户的姓名和联系方式(0:未勾选;1:勾选)--s//
		StringBuffer tempSql=new StringBuffer();
		tempSql.append(" SELECT ")
				.append(" su.name AS 'contactName', ")
				.append(" su.phone AS 'contactPhone' ")
				.append(" FROM sys_user su ")
				.append(" WHERE su.quauqBookOrderPermission = ? AND su.delFlag=?");
		List<Map<String,Object>> userList=userDao.findBySql(tempSql.toString(), Map.class,1,0);
		if(!(userList.isEmpty())){//用户不为空
			for (Map<String, Object> m : userList) {
				if(!(StringUtils.isNotBlank(m.get("contactPhone").toString()))){//处理电话为空值的情况
					m.put("contactPhone", "");
				}
				if(!(StringUtils.isNotBlank(m.get("contactName").toString()))){//处理姓名为空值的情况
					m.put("contactName", "");
				}
			}
			infoMap.put("userList", userList);
		}else{ //用户为空的情况
			Map<String,Object> tempMap=new HashMap<>();
			tempMap.put("contactName","");
			tempMap.put("contactPhone", "");
			userList.add(tempMap);
			infoMap.put("userList", userList);
		}
		//查询sys_user表中具有账号管理->修改->QUAUQ权限 quauqBookOrderPermission=1的用户的姓名和联系方式(0:未勾选;1:勾选)--e//
		infoMap.put("differenceRights", UserUtils.getUser().getDifferenceRights());
		return infoMap;
	}

	/**
	 * 首页搜索条件,目的地的查询,针对T1需求
	 * 散拼产品(kind=2),以及目的地全查(勾选启用+未勾选启用)
	 * 该方法源自方法filterTreeData,只是查询的时候,放开勾选的启用条件限制
	 * @param extId
	 * @param kind
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "filterTreeData4T1")
	public List<Map<String, Object>> filterTreeData4T1(@RequestParam(required=false) Long extId,@RequestParam(required=false) Integer kind, HttpServletResponse response) {
//			response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = null;
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList();
		//批发商
		Office company = UserUtils.getUser().getCompany();
		Long companyId = company.getId();

		if(Context.SUPPLIER_UUID_YJXZ.equals(company.getUuid()) && kind != null && (kind == 1 || kind == 2 || kind == 4 || kind == 5)){
			targetAreaIds = travelActivityService.findAreaIdsEndCountry4T1(companyId);
		}else{
			targetAreaIds = travelActivityService.findAreaIds4T1(companyId);
		}
		if(targetAreaIds != null && targetAreaIds.size() != 0){
			for(Map<String, Object> map:targetAreaIds){
				childAreaIds.add(Long.parseLong(String.valueOf(map.get("id"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds,areaIds,targetAreas);
		//判断是否有自定义目标区域
		if(mapList == null || mapList.size() == 0){
			mapList = Lists.newArrayList();
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
		}
		return mapList;
	}
	//-----------t1t2需求-----------e--//

	/**
	 * 根据产品发布时的基础信息：
	 * 获取计算出的quauq价
	 * @author xinwei.wang@quauq.com
	 * @param request
	 * @param response
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping("checkQuauqPrice")
//	public Map<String, BigDecimal> checkQuauqPrice(HttpServletRequest request, HttpServletResponse response) {

//		Map<String, Object> map = new HashMap<String, Object>();

	//获取策略匹配参数
//		String fromArea = request.getParameter("fromArea"); //出发城市
//		String targetAreaId = request.getParameter("targetAreaId"); //目的地
//		String travelTypeId = request.getParameter("travelTypeId"); //旅游类型
//		String activityTypeId = request.getParameter("activityTypeId"); //产品类型
//		String activityLevelId = request.getParameter("activityLevelId"); //产品系列
	//t1t2団期id，获取団期数据
//		String groupid = request.getParameter("groupid");
//		String activityId = request.getParameter("activityId");
//		if(StringUtils.isNotBlank(groupid)) { //团期id不为空
//			ActivityGroup activityGroup = groupService.findById(Long.valueOf(groupid));//根据id只能查询到一条记录
//			//quauq成人价
//			map.put("quauqAdultPrice",(activityGroup.getQuauqAdultPrice()==null)?"":activityGroup.getQuauqAdultPrice().setScale(2,RoundingMode.HALF_UP).toString());
//			//quauq儿童价
//			map.put("quauqChildPrice",(activityGroup.getQuauqChildPrice())==null?"":activityGroup.getQuauqChildPrice().setScale(2,RoundingMode.HALF_UP).toString());
//			//quauq特殊人群价
//			map.put("quauqSpecialPrice",(activityGroup.getQuauqSpecialPrice())==null?"":activityGroup.getQuauqSpecialPrice().setScale(2,RoundingMode.HALF_UP).toString());
//		}
	//   uauqPrice4Adult##2:15,3:6#2:15,3:6
	//#Q#quauqPrice4Child##2:20,3:10#2:20,3:10
	//#Q#quauqPrice4SpicalPerson##
	//String strategyResult = travelActivityService.checkActivityPriceStrategy(fromArea, targetAreaId, travelTypeId, activityTypeId, activityLevelId);
//		Map<String,BigDecimal> map = activityPricingStrategyService.getQuauqPrice(groupid, activityId);
	//String[] strategyResults = strategyResult.split("#Q#");
//		if ("0".equals(strategyResult)) {
//			map.put("result", "0");
//		}else{
//			map.put("result", "1");
//			for (int i = 0; i < strategyResults.length; i++) {
//			    //处理某项策略为空的情况
//				String[] trategyResultsArray = strategyResults[i].split("##");
//				String strategyResultname = "";
//				String strategy = "";
//				if (trategyResultsArray.length>1) {
//					strategyResultname = strategyResults[i].split("##")[0];
//					strategy = strategyResults[i].split("##")[1];
//				}else{
//					strategyResultname = strategyResults[i].split("##")[0];
//				}
//				
//				if ("quauqPrice4Adult".equals(strategyResultname)) {
//					map.put("quauqPrice4Adult", strategy);
//				}else if ("quauqPrice4Child".equals(strategyResultname)) {
//					map.put("quauqPrice4Child", strategy);
//				}else if ("quauqPrice4SpicalPerson".equals(strategyResultname)) {
//					map.put("quauqPrice4SpicalPerson", strategy);
//				}
//			}
//		}
//		return map;
//	}



	/**
	 * 根据groupid activityId inputName srcPrice(输入的同行价)来计算quauq价
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getQuauqPrice")
	public String getQuauqPrice(HttpServletRequest request, HttpServletResponse response){
		//获取参数
		String groupid = request.getParameter("groupid");
		String activityId = request.getParameter("activityId");
		String inputName = request.getParameter("inputName");
		BigDecimal srcPrice = new BigDecimal(request.getParameter("srcPrice"));
		String quauqPrice = "";
		//增加不带団期id的查询策略，这样只是查询某个产品的策略
		if(StringUtils.isBlank(groupid) || "".equals(groupid)) {
			quauqPrice = activityPricingStrategyService.getQuauqPrice(activityId, inputName, srcPrice);
		}else {
			//计算quauq价
			quauqPrice = activityPricingStrategyService.getQuauqPrice(groupid, activityId, inputName, srcPrice);
		}


		return quauqPrice;
	}

	/**
	 * 团期从T1下架
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="confimIsT1Off")
	public void confimIsT1Off(HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		//默认操作执行成功
		json.setFlag(true);
		String groupIdsBuffer = request.getParameter("groupIdsBuffer");
		try {
			if (StringUtils.isNotBlank(groupIdsBuffer)) {
				//单个团期下架
				if (groupIdsBuffer.indexOf(",") == -1) {
					activityGroupService.updateActivityGroup(groupIdsBuffer,Context.QUAUQ_T1_OFF);
					activityGroupService.updatePSStatusById(Long.parseLong(groupIdsBuffer), Context.PRICING_NO_NEED_RESET_STATUS);
					//上下架状态改变后，同步更新缓存
					T1Utils.updateT1HomeCache();
				} else {
					//批量团期下架
					String[] groupIds = groupIdsBuffer.split(",");
					for (int i = 0 ; i < groupIds.length; i++) {
						activityGroupService.updateActivityGroup(groupIds[i],Context.QUAUQ_T1_OFF);
						activityGroupService.updatePSStatusById(Long.parseLong(groupIds[i]), Context.PRICING_NO_NEED_RESET_STATUS);
						//上下架状态改变后，同步更新缓存
						T1Utils.updateT1HomeCache();
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		}
		String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}

	/**
	 * t1t2-v2平台上架功能，根据团期价格策略状态的值判断是否修改了同行价和直客价
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="checkPriceStatus")
	public void checkPriceStatus(HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		String groupId = request.getParameter("groupId");
		try {
			if(StringUtils.isNotBlank(groupId)) {
				ActivityGroup group = activityGroupService.findById(Long.parseLong(groupId));
				if(null != group){
					if (Context.PRICING_NEED_RESET_STATUS.equals(group.getPricingStrategyStatus())) {
						json.setFlag(true);
					}else {
						json.setFlag(false);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		}
		String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}


	/**
	 * t1t2-v2平台上架功能，查找团期修改价格的记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="checkPriceRecord")
	public void checkPriceRecord(HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		String groupId = request.getParameter("groupId");
		try {
			if(StringUtils.isNotBlank(groupId)) {
				List<Map<String, Object>> LogProductList = activityGroupService.findLogProductList(Long.parseLong(groupId));
				if (null != LogProductList && LogProductList.size() > 0) {
					json.setFlag(true);
				}else {
					json.setFlag(false);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		}
		String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}

	/**
	 * t1t2-v2平台上架功能，批量查找团期修改价格的记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="batchCheckPriceRecord")
	public void batchCheckPriceRecord(HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		//获取选中的所有团期的id
		String groupIds = request.getParameter("groupIds");
		String[] ids = groupIds.substring(0,groupIds.length()-1).split(",");
		String groupCodes = "";
		String groupId = "";
		String title = "";
		int flag = 0;  //设置存在记录的标记
		for (int i = 0; i < ids.length; i++){
			groupId = ids[i];
			try {
				if(StringUtils.isNotBlank(groupId)) {
					//查询团期是否存在修改同行价、直客价
					//List<Map<String, Object>> LogProductList = activityGroupService.findLogProductList(Long.parseLong(groupId));
					//if (null != LogProductList && LogProductList.size() > 0) { //如果存在记录，获取该团期的团号，并拼接提示语句
					//json.setFlag(true);
					ActivityGroup group = activityGroupService.findById(Long.parseLong(groupId));
					if (Context.PRICING_NEED_RESET_STATUS.equals(group.getPricingStrategyStatus())) {
						groupCodes = groupCodes + group.getGroupCode() + "团、";
						flag++;
					}
					//}
				}
			}catch (Exception e) {
				e.printStackTrace();
				//json.setFlag(false);
				//json.setMsg(e.getMessage());
			}
		}
		if (flag > 0){
			json.setFlag(true);
			title = groupCodes.substring(0,groupCodes.length()-1) + "因修改同行价或直客价，无法上架，请重新选择！";
			json.setMsg(title);
		}else {
			json.setFlag(false);
		}
		String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}


	/**
	 * 团期从T1上架
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="confimIsT1On")
	public void confimIsT1On(HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		//默认操作执行成功
		json.setFlag(true);
		String groupId = request.getParameter("groupId");
		try {
			if (StringUtils.isNotBlank(groupId)) {
				//团期从t1上架
				activityGroupService.updateActivityGroup(groupId,Context.QUAUQ_T1_ON);
				//将团期的定价策略状态设为不需要重新设置策略状态
				activityGroupService.updatePSStatusById(Long.parseLong(groupId), Context.PRICING_NO_NEED_RESET_STATUS);
				//上下架状态改变后，同步更新缓存
				T1Utils.updateT1HomeCache();
			}
		}catch (Exception e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		}
		String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}

	/**
	 * 查找是否有quauq策略
	 * addby:djw
	 * @return
	 */
	public int findIsHasPS(TravelActivity activity){
		int pricingStrategy_flag = 0;
		Set<ActivityGroup> groupTemp = activity.getActivityGroups();
		for(ActivityGroup group : groupTemp){
			Map<String, String> pricingStrategy = activityPricingStrategyService.getPricingStrategy(activity.getId().toString(), group.getId().toString());
			for(String key : pricingStrategy.keySet()){
				if(pricingStrategy.get(key) != null && pricingStrategy.get(key) != ""){
					pricingStrategy_flag = 1;
				}
			}
		}
		return pricingStrategy_flag;
	}

	/**
	 * 异步请求，通过用户所选择的目的地，匹配合适的线路玩法。
	 * @param request
	 * @return
	 * @author yudong.xu 2016.10.13
	 */
	@ResponseBody
	@RequestMapping(value="getMatchLine")
	public List<MatchLine> getMatchLineFromArea(HttpServletRequest request){

		List<MatchLine> matchLineList = new ArrayList<>(); // 用于返回的结果list

		String areaIds = request.getParameter("areaIds");
		List<String> areaIdList  = com.alibaba.fastjson.JSONArray.parseArray(areaIds,String.class);
		if (CollectionUtils.isEmpty(areaIdList)){
			return matchLineList;
		}
		String[] areaIdArr = areaIdList.toArray(new String[areaIdList.size()]);

		List<TouristLine> touristLineList = touristLineService.getAllTouristLine();

		for (TouristLine touristLine : touristLineList) {
			String lineAreaIds = touristLine.getDestinationIds();
			if (StringUtils.isBlank(lineAreaIds)){
				continue;
			}
			String[] lineAreaIdArr = lineAreaIds.split(",");
			int hitNum = getHits(areaIdArr,lineAreaIdArr);
			if (hitNum > 0){ // 有命中的目的地的线路才放入结果
				MatchLine matchLine = new MatchLine(touristLine.getId(),touristLine.getLineName(),hitNum);
				matchLineList.add(matchLine);
			}
		}

		// 根据命中得分进行重新排序
		Collections.sort(matchLineList, new Comparator<MatchLine>() {
			@Override
			public int compare(MatchLine o1, MatchLine o2) {
				return o2.getHitScore().compareTo(o1.getHitScore());
			}
		});

		return matchLineList;
	}

	/**
	 * 遍历线路目的地的id和目标目的地的id,返回线路目的地中标的个数。
	 * @param targetAreaIdArr
	 * @param lineAreaIdArr
	 * @return
	 */
	private int getHits(String[] targetAreaIdArr,String[] lineAreaIdArr){
		int hitNum = 0;
		outer: for (int i = 0; i < lineAreaIdArr.length; i++) {
			inner: for (int j = 0; j < targetAreaIdArr.length; j++) {
				if (targetAreaIdArr[j].equals(lineAreaIdArr[i])){
					hitNum ++;
					continue outer;
				}
			}
		}
		return hitNum;
	}

	/**
	 * 对散拼产品的游玩线路进行初始化，后期删除,请求地址是随意起的。
	 * @param response
	 * @author yudong.xu 2016.10.20
	 */
	@RequestMapping(value="breaking_bad")
	public void initialTouristLine(HttpServletResponse response){
		StringBuilder log = new StringBuilder("-----初始化散拼产品的游玩线路-----\n"); // 记录日志

		List<Map<String,Object>> activityList = travelActivityService.getAllActivityAreaIdsByType(2); // 散拼
		List<TouristLine> touristLineList = touristLineService.getAllTouristLine();

		for (Map<String, Object> map : activityList) {

			// 获取该产品的目的地ids
			String areaIds = (String)map.get("areaIds");
			if (StringUtils.isBlank(areaIds)){
				continue;
			}
			String[] targetAreaIdArr = areaIds.split(",");

			// 日志记录字段
			String bestLineName = "其他"; // 最佳线路的名称，日志使用
			String  bestLineAreaIds = ""; // 最佳线路包含的地区id，日志使用

			//匹配该产品的最佳线路
			Long bestLineId = 0L; // 最匹配的游玩线路的id,0表示其他
			Integer maxHitNum = 0; // 最大命中数
			for (TouristLine touristLine : touristLineList) {
				String lineAreaIds = touristLine.getDestinationIds();
				if (StringUtils.isBlank(lineAreaIds)){
					continue;
				}
				String[] lineAreaIdArr = lineAreaIds.split(",");
				int hitNum = getHits(targetAreaIdArr,lineAreaIdArr);
				if (hitNum > maxHitNum){ // 如果当前线路的命中数比最佳匹配的命中数还大,则当前线路变为最佳线路
					bestLineId = touristLine.getId();
					maxHitNum = hitNum; // 更新一下最大命中数
					// 日志记录用
					bestLineName = touristLine.getLineName();
					bestLineAreaIds = lineAreaIds;
				}
			}

			// 更新该产品的最佳线路
			Integer activityId = (Integer)map.get("activityId");
			travelActivityService.updateTouristLine(bestLineId,activityId.longValue());

			log.append("产品名称:").append(map.get("activityName")).append(" --产品id:").append(activityId)
					.append(" --产品目的地区域ids:").append(areaIds)
					.append(" --匹配线路名称:").append(bestLineName).append(" --匹配线路id:").append(bestLineId)
					.append(" --命中个数:").append(maxHitNum)
					.append(" --最佳匹配线路的区域:").append(bestLineAreaIds).append("\n\n");
		}
		log.append("-----Successful,恭喜您,").append(activityList.size())
				.append("条散拼产品的游玩线路初始化成功,you are great!!-----");
		ServletUtil.print(response,log.toString());
	}



}
	

