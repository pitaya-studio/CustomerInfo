/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoom;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotel.service.HotelRoomMealService;
import com.trekiz.admin.modules.hotel.service.HotelRoomService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerTypeRelationService;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.hotel.service.SysdefinedictService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMeal;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;
import com.trekiz.admin.modules.island.entity.ActivityIslandShare;
import com.trekiz.admin.modules.island.entity.ActivityIslandVisaFile;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.input.ActivityIslandInput;
import com.trekiz.admin.modules.island.input.ActivityIslandJsonBeanInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupQuery;
import com.trekiz.admin.modules.island.query.ActivityIslandQuery;
import com.trekiz.admin.modules.island.query.IslandOrderQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupLowpriceService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealRiseService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupPriceService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupRoomService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.ActivityIslandShareService;
import com.trekiz.admin.modules.island.service.ActivityIslandVisaFileService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.island.service.impl.ActivityIslandServiceImpl;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/activityIsland")
public class ActivityIslandController extends BaseController {
	public static final Logger log = Logger.getLogger(ActivityIslandController.class);
	//forward paths
	protected static final String LIST_PAGE = "modules/island/activityisland/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/activityIsland/list";
	protected static final String FORM_PAGE = "modules/island/activityisland/form";
	protected static final String SAVE_ISLANDPRODUCT = "modules/island/activityisland/toSaveActivityIslandProduct";
	protected static final String SHOW_PAGE = "modules/island/activityisland/show";

	protected static final String SHOW_ACTIVITY_ISLAND_DETAIL_PAGE = "modules/island/activityisland/showActivityIslandDetail";

	/** 海岛产品修改类型 product表示产品修改 */
	private final static String TYPE_PRODUCT = "product";
	/** 海岛产品修改类型 date表示团期修改 */
	private final static String TYPE_DATE = "date";
	
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandGroupRoomService activityIslandGroupRoomService;
	@Autowired
	private ActivityIslandGroupPriceService activityIslandGroupPriceService;
	@Autowired
	private ActivityIslandGroupMealService activityIslandGroupMealService;
	@Autowired
	private ActivityIslandGroupMealRiseService activityIslandGroupMealRiseService;
	@Autowired
	private ActivityIslandGroupLowpriceService activityIslandGroupLowpriceService;
	@Autowired
	private ActivityIslandShareService activityIslandShareService;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private AirlineInfoService airlineInfoService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private IslandService islandService;
	@Autowired
	private HotelMealService mealService;
	@Autowired
	private TravelerTypeService travelerTypeService;
	@Autowired
	private SysdefinedictService sysDefinedictService;
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private HotelStarService hotelStarService;
	@Autowired
	private HotelRoomService hotelRoomService;
	@Autowired
	private HotelMealService hotelMealService;
	@Autowired
	private HotelRoomMealService hotelRoomMealService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	
	private ActivityIsland dataObj;
	@Autowired
	private ActivityIslandVisaFileService activityIslandVisaFileService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private HotelTravelerTypeRelationService hotelTravelerTypeRelationService;

	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=activityIslandService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(ActivityIslandQuery activityIslandQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		activityIslandQuery.setDelFlag("0");
        Page<ActivityIsland> page = activityIslandService.find(new Page<ActivityIsland>(request, response), activityIslandQuery); 
        model.addAttribute("page", page);
        model.addAttribute("activityIslandQuery", activityIslandQuery);
        return LIST_PAGE;
	}
	/**
	 * 产品列表
	 * @param activityIslandQuery
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "islandProductList")
	public String islandProductList(ActivityIslandQuery activityIslandQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		String orderBy = request.getParameter("orderBy");
		String status = request.getParameter("status");
		String showType = request.getParameter("showType");// 1是团期列表，2是产品列表	
		Long companyId = UserUtils.getUser().getCompany().getId();
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,response);
		//产品发布权限--业务部门
		Set<Department> deptSet = UserUtils.getDepartmentByJob();
		model.addAttribute("canPublish", deptSet.size()>0?true:false);
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
		model.addAttribute("status", status);
		model.addAttribute("showType", showType);
		model.addAttribute("orderBy", orderBy);
		//添加币种列表
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		//添加酒店星级列表
		HotelStar hotelStar = new HotelStar();
		hotelStar.setWholesalerId(companyId.intValue());
		List<HotelStar> hotelStarList = hotelStarService.find(hotelStar);
		model.addAttribute("hotelStarList", hotelStarList);
		if ("2".equals(showType)) {// 产品列表	
			return "modules/island/activityisland/islandProductListProduct";
		} else {// 团期列表
			return "modules/island/activityisland/islandProductListGroup";
		}
	}
	
	/***
	 * 跳转到海岛游产品发布
	 * @author sy  
	 * @param activityIslandInput
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(@ModelAttribute ActivityIslandInput activityIslandInput, Model model) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		HotelMeal hotelMeal = new  HotelMeal();
		hotelMeal.setDelFlag("0");
		hotelMeal.setWholesalerId(companyId.intValue());
		List<HotelMeal>  hotelMeals  = mealService.find(hotelMeal);
		TravelerType travelerType = new TravelerType(companyId.intValue(),Context.DEL_FLAG_NORMAL);
		travelerType.setStatus("1");
		List<TravelerType> travelerTypes = travelerTypeService.find(travelerType);
		//初始化签证类型
		List<SysDict> newVisaTypeList = sysDictService.findByType("new_visa_type");
		model.addAttribute("newVisaTypeList", newVisaTypeList);
		model.addAttribute("travelerTypes", travelerTypes);
		model.addAttribute("hotelMeals", hotelMeals);
		model.addAttribute("activityIslandInput", activityIslandInput);
	  	model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
	  	
	  	//初始化航班所有json数据
	  	Map<String, Object> airlines = activityIslandService.getJsonAirlineInfo();
	  	if(airlines != null) {
		  	model.addAttribute("airlines", JSON.toJSONStringWithDateFormat(airlines, "yyyy-MM-dd"));
	  	}
		return SAVE_ISLANDPRODUCT;
	}
	
	/***
	 * 
	 * 海岛游产品发布保存
	 * @param activityIslandInput
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveActivityIsland")
	public Object saveActivityIsland(@ModelAttribute ActivityIslandInput activityIslandInput, Model model, HttpServletRequest request) {
		//获取上传的文件开始
		String[] docIdProArray = request.getParameterValues("DocIdPro");
		String[] docIdCostArray = request.getParameterValues("DocIdCost");
		String[] docIdOtherArray = request.getParameterValues("DocIdOther");
		String[] docOriNameArray = request.getParameterValues("docOriName");
		String[] docPathArray = request.getParameterValues("docPath");
		String[] countArray =request.getParameterValues("allvisafile");
		
		String[] jsontexts = request.getParameterValues("jsoninput");
	
		List<ActivityIslandGroup> islandGroupList = new ArrayList<ActivityIslandGroup>();
		for(int i=0;i<jsontexts.length;i++){
			ActivityIslandJsonBeanInput json2Bean = JSON.parseObject(jsontexts[i], ActivityIslandJsonBeanInput.class);
			ActivityIslandGroup islandGroup = json2Bean.getActivityIslandGroup();
			islandGroupList.add(islandGroup);
		}
		
		List[] eachVisaFileList = null;
		if (countArray != null && countArray.length > 0) {
			eachVisaFileList = new List[countArray.length];
			for (int k = 0; k < countArray.length; k++) {
				eachVisaFileList[k] = getVisaFileList(countArray[k]);
			}
		}
		int b = 0, c = 0;
		if (docIdProArray != null && docIdProArray.length > 0) {
			b = docIdProArray.length;
		}
		if (docIdCostArray != null && docIdCostArray.length > 0) {
			c = b + docIdCostArray.length;
		}	
		List<HotelAnnex> prodSchList =getFileList(0,docIdProArray,docOriNameArray,docPathArray);
		List<HotelAnnex> costProtocolList =getFileList(b,docIdCostArray,docOriNameArray,docPathArray);
		List<HotelAnnex> otherProtocolList =getFileList(c,docIdOtherArray,docOriNameArray,docPathArray);

		activityIslandInput.setProdSchList(prodSchList);
		activityIslandInput.setCostProtocolList(costProtocolList);
		activityIslandInput.setOtherProtocolList(otherProtocolList);
		activityIslandInput.setEachVisaFileList(eachVisaFileList);
		
		//获取上传的文件结束
		activityIslandInput.setActivityIslandGroupLists(islandGroupList);
		
		Map<String,String> result = new HashMap<String, String>();
		String status = request.getParameter("status");//1：上架；2：下架；3：草稿；4：已删除
		try {
			result = activityIslandService.save(activityIslandInput,status);
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
	
	@RequestMapping(value = "showActivityIslandDetail/{uuid}")
	public String showActivityIslandDetail(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		//展示类型：1、product表示产品详情；2、date表示团期详情
		String type = request.getParameter("type");
		if(StringUtils.isEmpty(uuid) || StringUtils.isEmpty(type)) {
			return RE_LIST_PAGE;
		}
		
		int baseMealNum = 0;//用来计算基础餐型的个数，根据房型循环统计
		int totalRemNum = 0;
		int bookingNum = 0;
		
		ActivityIslandInput activityIslandInput = new ActivityIslandInput();
		ActivityIsland activityIsland = new ActivityIsland();
		if(TYPE_PRODUCT.equals(type)) {//产品的详情，传递的是产品的UUid
			activityIsland = activityIslandService.getByUuid(uuid);//根据UUid查询海岛游产品
			if(activityIsland!=null){
				List<ActivityIslandGroup> islandGrouplist = activityIslandGroupService.getByActivityIslandUuid(uuid);//根据海岛游产品Uuid查询团期List
				if(islandGrouplist!=null && islandGrouplist.size()>0){   //二级表,可以有多条数据
					for(ActivityIslandGroup islandGroup:islandGrouplist){  //循环每一笔的二级表中的数据
						baseMealNum = 0;//用来计算基础餐型的个数，根据房型循环统计
						totalRemNum = 0;
						bookingNum = 0;
						
						String activityIslandGroupUuid = islandGroup.getUuid();//根据团期的UUid查询
						//海岛游产品团期参考航班表（按多个参考航班设计）,查询出航班的舱位等级   //暂时是一个团期只有一个航班,多个舱位等级，对应的是多条数据    三级表
						List<ActivityIslandGroupAirline> airlinelist = activityIslandGroupAirlineService.getByactivityIslandGroup(activityIslandGroupUuid);
						islandGroup.setActivityIslandGroupAirlineList(airlinelist);
						List<ActivityIslandGroupRoom> groupRoomlist  = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityIslandGroupUuid);
						
						for(int i=0;i<groupRoomlist.size();i++){//根据activity_island_group_room_uuid查询基础餐型
							ActivityIslandGroupRoom grouproom = groupRoomlist.get(i);
							//基础餐型  四级表
							List<ActivityIslandGroupMeal> groupMeallist = activityIslandGroupMealService.getByactivityIslandGroupUuid(grouproom.getUuid());
							//将基础餐型的升餐数据set进基础餐型里面  五级表
							for(ActivityIslandGroupMeal groupMeal:groupMeallist){
								String mealuuid = groupMeal.getUuid();
								List<ActivityIslandGroupMealRise> mealRiselist = activityIslandGroupMealRiseService.getbyGroupMealUuid(mealuuid);
								groupMeal.setActivityIslandGroupMealRiseList(mealRiselist);
							}
							baseMealNum = baseMealNum + groupMeallist.size();
							//将基础餐型的数据set进海岛游产品团期房型里面
							grouproom.setActivityIslandGroupMealList(groupMeallist);
						}

						islandGroup.setActivityIslandGroupRoomList(groupRoomlist);
						//海岛游产品团期价格表,海岛游产品团期参考航班表(ActivityIslandGroupAirline)uuid，是以舱位等级为单位的  
						List<ActivityIslandGroupPrice> groupPricelist  = activityIslandGroupPriceService.getPriceFilterTravel(activityIslandGroupUuid, activityIsland.getHotelUuid());
						islandGroup.setActivityIslandGroupPriceList(groupPricelist);
						if(StringUtils.isNotBlank(activityIslandGroupUuid)){
						bookingNum =  islandOrderService.getBookingPersonNum(activityIslandGroupUuid);}
						/*for(int i=0;i<airlinelist.size();i++){
							totalRemNum = totalRemNum + airlinelist.get(i).getRemNumber();
						}*/
						totalRemNum = activityIslandGroupService.getRemNumberByGroupAirlineList(airlinelist);
						
						islandGroup.setBookingNum(bookingNum);
						islandGroup.setBaseMealNum(baseMealNum==0?1:baseMealNum);
						islandGroup.setTotalRemNum(totalRemNum);
					}
				}
				activityIsland.setActivityIslandGroupList(islandGrouplist);
				activityIslandInput.initActivityIslandInput(activityIsland);
				model.addAttribute("hotelroomlist",getHouseTypeByHotelUuid(activityIsland.getHotelUuid()));//房型
				
				getAllFileList(uuid, model);
				// 海岛游产品上传签证资料
				ActivityIslandVisaFile visafile = new ActivityIslandVisaFile();
				visafile.setDelFlag("0");
				visafile.setActivityIslandUuid(uuid);
				List<ActivityIslandVisaFile> visaFileList = activityIslandVisaFileService.find(visafile);
				// 海岛游产品上传签证资料附件列表
				getVisaFileAnnex(visaFileList);	
				activityIsland.setActivityIslandVisaFile(visaFileList);
			}
		} else if(TYPE_DATE.equals(type)) {//团期的详情，如果是团期，传递的是团期的UUid
			ActivityIslandGroup group = activityIslandGroupService.getByUuid(uuid);
			activityIsland = activityIslandService.getByUuid(group.getActivityIslandUuid());
			if(activityIsland!=null){
				List<ActivityIslandGroup> islandGrouplist = new ArrayList<ActivityIslandGroup>();
				islandGrouplist.add(group);
				if(islandGrouplist!=null && islandGrouplist.size()>0){   //二级表,可以有多条数据
					for(ActivityIslandGroup islandGroup:islandGrouplist){  //循环每一笔的二级表中的数据
						baseMealNum = 0;//用来计算基础餐型的个数，根据房型循环统计
						totalRemNum = 0;
						bookingNum = 0;
						
						String activityIslandGroupUuid = islandGroup.getUuid();//根据团期的UUid查询
						//海岛游产品团期参考航班表（按多个参考航班设计）,查询出航班的舱位等级   //暂时是一个团期只有一个航班,多个舱位等级，对应的是多条数据    三级表
						List<ActivityIslandGroupAirline> airlinelist = activityIslandGroupAirlineService.getByactivityIslandGroup(activityIslandGroupUuid);
						islandGroup.setActivityIslandGroupAirlineList(airlinelist);
						List<ActivityIslandGroupRoom> groupRoomlist  = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityIslandGroupUuid);
						
						for(int i=0;i<groupRoomlist.size();i++){//根据activity_island_group_room_uuid查询基础餐型
							ActivityIslandGroupRoom grouproom = groupRoomlist.get(i);
							//基础餐型  四级表
							List<ActivityIslandGroupMeal> groupMeallist = activityIslandGroupMealService.getByactivityIslandGroupUuid(grouproom.getUuid());
							//将基础餐型的升餐数据set进基础餐型里面  五级表
							for(ActivityIslandGroupMeal groupMeal:groupMeallist){
								String mealuuid = groupMeal.getUuid();
								List<ActivityIslandGroupMealRise> mealRiselist = activityIslandGroupMealRiseService.getbyGroupMealUuid(mealuuid);
								groupMeal.setActivityIslandGroupMealRiseList(mealRiselist);
							}
							baseMealNum = baseMealNum + groupMeallist.size();
							//将基础餐型的数据set进海岛游产品团期房型里面
							grouproom.setActivityIslandGroupMealList(groupMeallist);
						}

						islandGroup.setActivityIslandGroupRoomList(groupRoomlist);
						//海岛游产品团期价格表,海岛游产品团期参考航班表(ActivityIslandGroupAirline)uuid，是以舱位等级为单位的  
						List<ActivityIslandGroupPrice> groupPricelist  = activityIslandGroupPriceService.getPriceFilterTravel(activityIslandGroupUuid,activityIsland.getHotelUuid());
						islandGroup.setActivityIslandGroupPriceList(groupPricelist);
						if(StringUtils.isNotBlank(activityIslandGroupUuid)){
							bookingNum =  islandOrderService.getBookingPersonNum(activityIslandGroupUuid);
						}
						totalRemNum = activityIslandGroupService.getRemNumberByGroupAirlineList(airlinelist);
						
						islandGroup.setBookingNum(bookingNum);
						islandGroup.setBaseMealNum(baseMealNum==0?1:baseMealNum);
						islandGroup.setTotalRemNum(totalRemNum);
					}
				}
				activityIsland.setActivityIslandGroupList(islandGrouplist);
				activityIslandInput.initActivityIslandInput(activityIsland);
				model.addAttribute("hotelroomlist",getHouseTypeByHotelUuid(activityIsland.getHotelUuid()));//房型
				
				
				getAllFileList(activityIsland.getUuid(), model);
				// 海岛游产品上传签证资料
				ActivityIslandVisaFile visafile = new ActivityIslandVisaFile();
				visafile.setDelFlag("0");
				visafile.setActivityIslandUuid(activityIsland.getUuid());
				List<ActivityIslandVisaFile> visaFileList = activityIslandVisaFileService.find(visafile);
				// 海岛游产品上传签证资料附件列表
				getVisaFileAnnex(visaFileList);	
				activityIsland.setActivityIslandVisaFile(visaFileList);
			}
		}
		
		model.addAttribute("activityIsland", activityIsland);
		//update by WangXK 20151020 添加空指针的判断
		if(activityIsland != null){
			List<User> shareUsers = activityIslandShareService.findShareUserByIsland(activityIsland.getUuid());
			Integer val=hotelService.getHotelStarValByHotelUuid(activityIsland.getHotelUuid());
			model.addAttribute("shareUsers", shareUsers);
			model.addAttribute("val", val);
		}
		
		TravelerType travelerType = new TravelerType(UserUtils.getUser().getCompany().getId().intValue(),Context.DEL_FLAG_NORMAL);
		travelerType.setStatus("1");
		List<TravelerType> travelerTypes = travelerTypeService.find(travelerType);
		model.addAttribute("travelerTypes", travelerTypes);
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("activityIslandInput", activityIslandInput);
		model.addAttribute("activityIsland", activityIsland);
		
		return SHOW_ACTIVITY_ISLAND_DETAIL_PAGE;
	}
	/**
	 * 获取签证资料列表，一个海岛游产品可以有多个签证资料
	 * @param visaFileList
	 */
	private void getVisaFileAnnex(List<ActivityIslandVisaFile> visaFileList) {
		if(CollectionUtils.isNotEmpty(visaFileList)){
			for(ActivityIslandVisaFile aivf:visaFileList){
				HotelAnnex ha = new HotelAnnex();
				ha.setMainUuid(aivf.getUuid());
				ha.setDelFlag("0");
				ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_VISAFILE);
				List<HotelAnnex> haList = hotelAnnexService.find(ha);
				aivf.setHotelAnnexList(haList);
			}
		}
	}
	/**
	 * 
	 * @param uuid
	 * @param model
	 */
	private void getAllFileList(String uuid, Model model) {
		HotelAnnex ha = new HotelAnnex();
		ha.setDelFlag("0");
		//ha.setMainUuid(activityIsland.getUuid());
		ha.setMainUuid(uuid);
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_PRODSCH);//获取产品行程介绍附件列表
		List<HotelAnnex> prodSchList = hotelAnnexService.find(ha);
		if(prodSchList!=null&&prodSchList.size()>0){
			model.addAttribute("prodSchList", prodSchList);
		}
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_COSTPROTOCOL);//获取自费补充协议附件列表
		List<HotelAnnex> costProtocolList = hotelAnnexService.find(ha);
		if(costProtocolList!=null&&costProtocolList.size()>0){
			model.addAttribute("costProtocolList", costProtocolList);
		}
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_OTHERPROTOCOL);//获取其他补充协议附件列表
		List<HotelAnnex> otherProtocolList = hotelAnnexService.find(ha);
		if(otherProtocolList!=null&&otherProtocolList.size()>0){
			model.addAttribute("otherProtocolList", otherProtocolList);
		}
	}
	/**
	 * add by wangXK
	 * @param uuid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		int baseMealNum = 0;//用来计算基础餐型的个数，根据房型循环统计
		int totalRemNum = 0;
		int bookingNum = 0;
		ActivityIslandInput activityIslandInput = new ActivityIslandInput();
		ActivityIsland activityIsland = activityIslandService.getByUuid(uuid);//根据UUid查询海岛游产品
		List<TravelerType> travelerTypes = new ArrayList<TravelerType>();
		if(StringUtils.isNotBlank(activityIsland.getHotelUuid())){
			travelerTypes = hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(activityIsland.getHotelUuid());
		}
		if(activityIsland!=null){
			List<ActivityIslandGroup> islandGrouplist = activityIslandGroupService.getByActivityIslandUuid(uuid);//根据海岛游产品Uuid查询团期List
			if(islandGrouplist!=null && islandGrouplist.size()>0){   //二级表,可以有多条数据
				for(ActivityIslandGroup islandGroup:islandGrouplist){  //循环每一笔的二级表中的数据
					baseMealNum = 0;//用来计算基础餐型的个数，根据房型循环统计
					totalRemNum = 0;
					bookingNum = 0;
					
					String activityIslandGroupUuid = islandGroup.getUuid();//根据团期的UUid查询
					//海岛游产品团期参考航班表（按多个参考航班设计）,查询出航班的舱位等级   //暂时是一个团期只有一个航班,多个舱位等级，对应的是多条数据    三级表
					List<ActivityIslandGroupAirline> airlinelist = activityIslandGroupAirlineService.getByactivityIslandGroup(activityIslandGroupUuid);
					islandGroup.setActivityIslandGroupAirlineList(airlinelist);
					List<ActivityIslandGroupRoom> groupRoomlist  = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityIslandGroupUuid);
					
					for(int i=0;i<groupRoomlist.size();i++){//根据activity_island_group_room_uuid查询基础餐型
						ActivityIslandGroupRoom grouproom = groupRoomlist.get(i);
						//基础餐型  四级表
						List<ActivityIslandGroupMeal> groupMeallist = activityIslandGroupMealService.getByactivityIslandGroupUuid(grouproom.getUuid());
						//将基础餐型的升餐数据set进基础餐型里面  五级表
						for(ActivityIslandGroupMeal groupMeal:groupMeallist){
							String mealuuid = groupMeal.getUuid();
							List<ActivityIslandGroupMealRise> mealRiselist = activityIslandGroupMealRiseService.getbyGroupMealUuid(mealuuid);
							groupMeal.setActivityIslandGroupMealRiseList(mealRiselist);
						}
						baseMealNum = baseMealNum + groupMeallist.size();
						//将基础餐型的数据set进海岛游产品团期房型里面
						grouproom.setActivityIslandGroupMealList(groupMeallist);
					}

					islandGroup.setActivityIslandGroupRoomList(groupRoomlist);
					//海岛游产品团期价格表,海岛游产品团期参考航班表(ActivityIslandGroupAirline)uuid，是以舱位等级为单位的  
					List<ActivityIslandGroupPrice> groupPricelist  = activityIslandGroupPriceService.getPriceFilterTravel(activityIslandGroupUuid,activityIsland.getHotelUuid());
					islandGroup.setActivityIslandGroupPriceList(groupPricelist);
					bookingNum =  islandOrderService.getBookingPersonNum(islandGroup.getUuid())==null?0:islandOrderService.getBookingPersonNum(islandGroup.getUuid());
					totalRemNum = activityIslandGroupService.getRemNumberByGroupAirlineList(airlinelist);
					
					islandGroup.setBookingNum(bookingNum);
					islandGroup.setBaseMealNum(baseMealNum==0?1:baseMealNum);
					islandGroup.setTotalRemNum(totalRemNum);
				}
			}
			activityIsland.setActivityIslandGroupList(islandGrouplist);
			activityIslandInput.initActivityIslandInput(activityIsland);
			model.addAttribute("hotelroomlist",getHouseTypeByHotelUuid(activityIsland.getHotelUuid()));//房型
		}
		//初始化签证类型
		List<SysDict> newVisaTypeList = sysDictService.findByType("new_visa_type");
		model.addAttribute("newVisaTypeList", newVisaTypeList);
		model.addAttribute("travelerTypes", travelerTypes);
		model.addAttribute("mealbaselist", getHotelMealBase()); //基础餐型,升级餐型
		//根据批发商查询航空公司的id及名字
		//model.addAttribute("airlines_list", airlineInfoService.getAirlineInfoList(UserUtils.getUser().getCompany().getId()));// 航空公司
		model.addAttribute("activityIslandInput", activityIslandInput);
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		//model.addAttribute("airlineInfoAll",  getAllAirLineInfo());
		model.addAttribute("islandWays",getIslandWaysByIslandUuid(activityIsland.getIslandUuid()));
		//获取附件信息
		getAllFileList(uuid, model);
		// 海岛游产品上传签证资料
		ActivityIslandVisaFile visafile = new ActivityIslandVisaFile();
		visafile.setDelFlag("0");
		visafile.setActivityIslandUuid(uuid);
		List<ActivityIslandVisaFile> visaFileList = activityIslandVisaFileService.find(visafile);
		// 海岛游产品上传签证资料附件列表
		getVisaFileAnnex(visaFileList);	
		activityIsland.setActivityIslandVisaFile(visaFileList);
		model.addAttribute("activityIsland", activityIsland);
		//产品分享
		List<ActivityIslandShare> islandShareList  = activityIslandShareService.findByActivityIslandUuid(uuid);
		String key = "";
		if(CollectionUtils.isNotEmpty(islandShareList)){
			for(ActivityIslandShare list : islandShareList){
				key+=list.getAcceptShareUser()+",";
			}
		}
		if(StringUtils.isNotEmpty(key)){
			key = key.substring(0, key.lastIndexOf(","));
			model.addAttribute("key", key);
		}
		return "modules/island/activityisland/editActivityIslandProduct";
	}
	
	/**
	 * 更新海岛游产品数据库
	 * @param activityIslandInput
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateacitivityisland")
	public Object updateAcitivityIsland(@ModelAttribute ActivityIslandInput activityIslandInput, Model model, HttpServletRequest request) {
		Map<String,String> result = new HashMap<String, String>();
		String uuid = activityIslandInput.getUuid();
		String[] jsontexts = request.getParameterValues("jsoninput");
		
		if(jsontexts!=null &&jsontexts.length > 0){
			List<ActivityIslandGroup> islandGroupList = new ArrayList<ActivityIslandGroup>(); 
			for(int i=0;i<jsontexts.length;i++){
				ActivityIslandJsonBeanInput json2Bean = JSON.parseObject(jsontexts[i], ActivityIslandJsonBeanInput.class);
				ActivityIslandGroup islandGroup = json2Bean.getActivityIslandGroup();
				islandGroupList.add(islandGroup);
			}
			activityIslandInput.setActivityIslandGroupLists(islandGroupList);
		}
		
		//获取上传的文件开始
		String[] docIdProArray = request.getParameterValues("DocIdPro");
		String[] docIdCostArray = request.getParameterValues("DocIdCost");
		String[] docIdOtherArray = request.getParameterValues("DocIdOther");
		String[] docOriNameArray = request.getParameterValues("docOriName");
		String[] docPathArray = request.getParameterValues("docPath");
		String[] countArray =request.getParameterValues("allvisafile");			
		List[] eachVisaFileList = null;
		if (countArray != null && countArray.length > 0) {
			eachVisaFileList = new List[countArray.length];
			for (int k = 0; k < countArray.length; k++) {
				eachVisaFileList[k] = getVisaFileList(countArray[k]);
			}
		}
		int b = 0, c = 0;
		if (docIdProArray != null && docIdProArray.length > 0) {
			b = docIdProArray.length;
		}
		if (docIdCostArray != null && docIdCostArray.length > 0) {
			c = b + docIdCostArray.length;
		}	
		List<HotelAnnex> prodSchList =getFileList(0,docIdProArray,docOriNameArray,docPathArray);
		List<HotelAnnex> costProtocolList =getFileList(b,docIdCostArray,docOriNameArray,docPathArray);
		List<HotelAnnex> otherProtocolList =getFileList(c,docIdOtherArray,docOriNameArray,docPathArray);

		activityIslandInput.setProdSchList(prodSchList);
		activityIslandInput.setCostProtocolList(costProtocolList);
		activityIslandInput.setOtherProtocolList(otherProtocolList);
		activityIslandInput.setEachVisaFileList(eachVisaFileList);
		//获取上传的文件开始
		
		try {
			result = activityIslandService.updateAcitivityIsland(activityIslandInput,"update");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
			return result;
		}
		
		return result;
	}
	/**
	 * 海岛游产品 添加团期,保存或者提交  add by wangXK
	 * @param activityIslandInput
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveActivityIslandGroup")
	public Object saveActivityIslandGroup(@ModelAttribute ActivityIslandInput activityIslandInput, Model model, HttpServletRequest request){
		Map<String,String> result = new HashMap<String, String>();
		String[] jsontexts = request.getParameterValues("jsoninput");
		
		List<ActivityIslandGroup> islandGroupList = new ArrayList<ActivityIslandGroup>(); 
		for(int i=0;i<jsontexts.length;i++){
			ActivityIslandJsonBeanInput json2Bean = JSON.parseObject(jsontexts[i], ActivityIslandJsonBeanInput.class);
			ActivityIslandGroup islandGroup = json2Bean.getActivityIslandGroup();
			islandGroupList.add(islandGroup);
		}
		activityIslandInput.setActivityIslandGroupLists(islandGroupList);
		
		try {
			result = activityIslandService.saveActivityIslandGroup(activityIslandInput);
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
	 * 海岛游产品 更新团期列表  add by wangXK
	 * @param activityIslandInput
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateAcitivityIslandGroup")
	public Object updateAcitivityIslandGroup(Model model, HttpServletRequest request) {
		
		Map<String,String> result = new HashMap<String, String>();
		
		String jsoninput = request.getParameter("jsoninput");
		
		ActivityIslandGroup islandGroup = new ActivityIslandGroup(); 
		
		ActivityIslandJsonBeanInput json2Bean = JSON.parseObject(jsoninput, ActivityIslandJsonBeanInput.class);
		islandGroup = json2Bean.getActivityIslandGroup();
		
		try {
			result = activityIslandService.updateActivityIslandGroup(islandGroup,"update");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
			return result;
		}
		
		return result;
	}
	
	
	public static void main(String[] args) {
		String json = "{\"no\":\"efx0827efxefx\",\"date\":\"2015-08-20\",\"houseTypes\":[{\"houseType\":{\"value\":\"00a5ed7b9fa1442e9cdb9092316e7a51\",\"text\":\"尊享套房\"},\"night\":\"12\",\"baseMealTypes\":[{\"mealType\":{\"value\":\"23aef9bc8aad43d5854c590163d607c7\",\"text\":\"BB\"},\"upMealTypes\":[{\"mealType\":{\"value\":\"23aef9bc8aad43d5854c590163d607c7\",\"text\":\"BB\"},\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\"}]}]}],\"priceDiff\":{\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\",\"unit\":{\"value\":\"1\",\"text\":\"/人\"}},\"airline\":{\"value\":\"AB\",\"text\":\"春秋航空公司\",\"flight\":{\"value\":\"AB12345678\",\"text\":\"AB12345678\",\"start\":\"09:00\",\"end\":\"12:00\",\"days\":\"0\"},\"prices\":[{\"islandprice\":[{\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\"},{\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\"}]},{\"islandprice\":[{\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\"},{\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\"}]},{\"islandprice\":[{\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\"},{\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\"}]}],\"touristType\":[\"成人\",\"婴儿\",\"特殊人群\"],\"ctrlTickets\":[\"12\",\"12\"],\"unCtrlTickets\":[\"12\",\"12\"],\"remainTickets\":[\"24\",\"24\"],\"amount\":{\"remainTicket\":\"48\",\"ticket\":\"48\"}},\"ctrlTicketPriority\":true,\"predictCount\":\"12\",\"deposit\":{\"currency\":{\"value\":\"77\",\"text\":\"$\"},\"price\":\"12\"},\"comment\":\"12\"}";	
		ActivityIslandJsonBeanInput json2Bean = JSON.parseObject(json, ActivityIslandJsonBeanInput.class);
		ActivityIslandGroup islandGroup = json2Bean.getActivityIslandGroup();
		
		islandGroup = json2Bean.getActivityIslandGroup();
		ActivityIslandServiceImpl activityIslandService = new ActivityIslandServiceImpl();
		activityIslandService.updateActivityIslandGroup(islandGroup,"update");
	}
	
	
	/**
	 * 查询航班的信息
	 * @param airlinelist
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getAllAirLineInfo(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<AirlineInfo> airInfoList = airlineInfoService.getDistinctAirline(UserUtils.getUser().getCompany().getId());//获得所有的航空公司 航空公司名字及二字码
		Map airlineMap = new HashMap();
		for(AirlineInfo airline:airInfoList){
			List<AirlineInfo> airFlightNums = airlineInfoService.getDistinctFlightNum(UserUtils.getUser().getCompany().getId(), airline.getAirlineCode());//查询航空公司对应下的航班号，起飞时间到达时间
			Map flyNos = new HashMap();
			if(null!=airFlightNums&&airFlightNums.size()>0){
				for(AirlineInfo flyNo:airFlightNums){
					Map startEnd = new HashMap();
					Date startDate = flyNo.getDeparturetime();
					if(startDate!=null){
						startEnd.put("start", sdf.format(startDate));
					}
					Date endDate = flyNo.getArrivaltime();
					if(endDate!=null){
						startEnd.put("end", sdf.format(endDate));
					}
					if(flyNo.getDayNum()!=null){
						startEnd.put("day", flyNo.getDayNum().toString());
					}
					
					if(StringUtils.isNotEmpty(flyNo.getFlightnumber())){
						flyNos.put(flyNo.getFlightnumber(), startEnd);
					}
				}
			}
			if(MapUtils.isNotEmpty(flyNos)){
				airlineMap.put(airline.getAirlineCode(), flyNos);
			}
		}
		
		return JSON.toJSONString(airlineMap);
	}
	
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "getJsonAirlineInfo")              
	public Map<String, Object> getJsonAirlineInfo(HttpServletRequest request) {
		return activityIslandService.getJsonAirlineInfo();
	}
	/**
	 * 根据酒店的uuid查询对应的房型
	 * @param hoteluuid
	 * @return
	 */
	public List<HotelRoom> getHouseTypeByHotelUuid(String hoteluuid){
		Long companyId = UserUtils.getUser().getCompany().getId();
		HotelRoom hotelRoom = new HotelRoom();
		hotelRoom.setDelFlag(Context.DEL_FLAG_NORMAL);
		hotelRoom.setHotelUuid(hoteluuid);
		hotelRoom.setWholesalerId(companyId.intValue());
		List<HotelRoom>  roomtype  = hotelRoomService.find(hotelRoom);
		return roomtype;
	}
	
	/**
	 * 根据hotelUUid 查询基础餐型,升级餐型
	 * @param hoteluuid
	 * @return
	 */
	public List<HotelMeal> getHotelMealBase(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		HotelMeal hotelMeal = new  HotelMeal(); 
        hotelMeal.setDelFlag(Context.DEL_FLAG_NORMAL);
        hotelMeal.setWholesalerId(companyId.intValue());
        List<HotelMeal>  hotelMeals  = mealService.find(hotelMeal);
        return hotelMeals;
	}
	/***
	 * 根据航空公司的二字码查询航班号
	 * ajax级联调用 add by wangXK
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFlightNumFromAirlineInfo")
	public Map<String, Object> getFlightNumFromAirlineInfo(HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		String airlindeCode = request.getParameter("airlinecode");
		Map<String, String> flightnum = airlineInfoService.findFlightNumberById(companyId, airlindeCode);
		map.put("flightnum", flightnum);
		return map;
	}
	/**
	 * 根据岛屿的UUId查询上岛方式
	 * @param uuid
	 */
	@ResponseBody
	@RequestMapping(value = "getAirlineInfoByType")
	public Map<String,Object> getAirlineInfoByType(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		String type = request.getParameter("type");
		
		if("space_level".equals(type)){//根据二次码和航班号查询出舱位等级
			String airlineCode = request.getParameter("airlineCode");
			String flightnum = request.getParameter("flightnum");
			List<AirlineInfo> resList = airlineInfoService.getDistinctSpaceLevel(UserUtils.getUser().getCompany().getId(), airlineCode, flightnum);
			map.put("space_level",resList);
		}else if("traveler_type".equals(type)){
			//改为根据酒店查找游客类型                                                               
			String hotelUuid = request.getParameter("hotelUuid");
			List<TravelerType> travelerTypes = hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(hotelUuid);
			map.put("traveler_type", travelerTypes);
		}else if("currency_list".equals(type)){
			map.put("currency_list", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		}else{
			return null;	
		}
		return map;
	}
	/***
	 * 根据航班号级联查询出起飞时间、到达时间、天数及仓位等级的变化       add by wangXK
	 * @param request
	 * @return
	 */
	public List<SysCompanyDictView> getIslandWaysByIslandUuid(String uuid) {
		List<SysCompanyDictView> listIslandWay = new ArrayList<SysCompanyDictView>();	
		if(!("").equals(uuid)){
			    SysCompanyDictView sysCompanyDictView = new SysCompanyDictView();
				
			    Island island = islandService.getByUuid(uuid);
			    String[] islandWay = null;
			    if(StringUtils.isNotEmpty(island.getIslandWay())){
			        islandWay=  island.getIslandWay().toString().split(",");
		    	    
				    List<SysCompanyDictView> sysCompanyDictViewList = null;
				    for (int i = 0; i < islandWay.length; i++) {
				    	sysCompanyDictView.setUuid(islandWay[i]);
				    	sysCompanyDictViewList = sysCompanyDictViewService.find(sysCompanyDictView);
				    	listIslandWay.add(sysCompanyDictViewList.get(0));
					}
			    }
			}
		return listIslandWay;
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
					activityIslandService.removeByUuid(uuid);
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
	 * 修改activity_island_group的delflag
	 * @param uuids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteGroup")
	public Object deleteGroup(String uuids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				for(String uuid:uuidArray){
					IslandOrderQuery query = new IslandOrderQuery();
					query.setActivityIslandGroupUuid(uuid);
					query.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
					query.setOrderStatus(null);
					List<IslandOrder> list = islandOrderService.find(query); 
					if(list.size()>0){
						ActivityIslandGroup group = activityIslandGroupService.getByUuid(uuid);
						datas.put("result", "0");
						datas.put("message", "团号: "+group.getGroupCode()+" 存在报名订单,不允许删除!");
						return datas;
					}
					activityIslandGroupService.removeByUuid(uuid);
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
	 * 批量更新海岛游产品状态
	*<p>Title: batchUpdateIslandStatus</p>
	* @param uuids 海岛游产品uuids(uuids按;号分隔)
	* @param status 想要修改为的产品状态
	* @return Object 返回类型
	* @author majiancheng
	* @date 2015-5-28 上午11:36:36
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "batchUpdateIslandStatus")
	public Object batchUpdateIslandStatus(String uuids, String status) {
		Map<String,Object> datas = new HashMap<String, Object>();
		if(StringUtils.isEmpty(uuids)) {
			datas.put("result", "fail");
			datas.put("message", "系统发生异常，请重新操作!");
			return datas;
		}
		
		try{
			List<ActivityIslandGroup> list = activityIslandGroupService.getByActivityIslandUuid(uuids);
			//对产品进行上架操作,只允许操作下架或草稿状态
			if("1".equals(status)){
				for(ActivityIslandGroup group:list){
					if(!"2".equals(group.getStatus()) || !"3".equals(group.getStatus())){
						datas.put("result", "fail");
						datas.put("message", "当前产品下包含非下架或草稿状态的团期,不允许对产品执行上架操作.");
						return datas;
					}
				}
			//对产品进行下架操作,如果有一个团期为非上架状态,则不能进行下架操作
			}else if("2".equals(status)){
				for(ActivityIslandGroup group:list){
					if(!"1".equals(group.getStatus())){
						datas.put("result", "fail");
						datas.put("message", "当前产品下包含非上架状态的团期,不允许对产品执行下架操作");
						return datas;
					}
				}
			//对产品进行删除操作,如果有一个团期为已删除状态,则不能进行删除操作
			}else if("4".equals(status)){
				for(ActivityIslandGroup group:list){
					if("4".equals(group.getStatus())){
						datas.put("result", "fail");
						datas.put("message", "当前产品下包含已删除状态的团期,不允许对产品执行删除操作.");
						return datas;
					}
				}
			//对产品进行恢复操作,如果有一个团期非已删除状态,则不能进行恢复操作
			}else if("3".equals(status)){
				for(ActivityIslandGroup group:list){
					if(!"4".equals(group.getStatus())){
						datas.put("result", "fail");
						datas.put("message", "当前产品下包含非删除状态的团期,不允许对产品执行恢复操作.");
						return datas;
					}
				}
			}
			String[] uuidArray = uuids.split(";");
			int count = activityIslandService.batchUpdateStatusByIslandUuidArr(uuidArray, "3".equals(status)?"2":status);
			datas.put("result", "success");
			datas.put("message", "更新成功!");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "fail");
			datas.put("message", "系统发生异常，请重新操作!");
		}
		
		return datas;
	}
	
	/**
	 * 批量更新海岛游团期产品状态
	*<p>Title: updateIslandStatusByUuid</p>
	* @param uuids 海岛游团期产品uuids(uuids按;号分隔)
	* @param status 想要修改为的产品状态
	* @return Object 返回类型
	* @author majiancheng
	* @date 2015-5-28 上午11:51:26
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value = "batchUpdateIslandGroupStatus")
	public Object batchUpdateIslandGroupStatus(String uuids, String status) {
		Map<String,Object> datas = new HashMap<String, Object>();
		if(StringUtils.isEmpty(uuids)) {
			datas.put("result", "fail");
			datas.put("message", "系统发生异常，请重新操作!");
			return datas;
		}
		
		try{
			String[] uuidArray = uuids.split(";");
			int count = activityIslandGroupService.batchUpdateStatusByGroupUuidArr(uuidArray, status);
			
			datas.put("result", "success");
			datas.put("message", "更新成功!");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("result", "fail");
			datas.put("message", "系统发生异常，请重新操作!");
		}
		
		return datas;
	}
	/**
	 * 选出当前海岛产品下的最小同行价
	 * @param activityIsland
	 */
	@RequestMapping(value = "chooseTheLowestPrice")
	public void chooseTheLowestPrice(ActivityIsland activityIsland) {
		Long CompanyId = UserUtils.getUser().getCompany().getId();
		double totalMoney =0,price = 0;
		int currencyId =0;
		//List<Map<Integer,Double>> list = new ArrayList<Map<Integer,Double>>();
		List<ActivityIslandGroup> groupList = activityIsland.getActivityIslandGroupList();
		if(CollectionUtils.isEmpty(groupList)){
			return;
		}
		for(ActivityIslandGroup activityIslandGroup :groupList){
		   
		   List<ActivityIslandGroupPrice> priceList = activityIslandGroup.getActivityIslandGroupPriceList();
		   for(ActivityIslandGroupPrice activityIslandGroupPrice :priceList){
			   if("".equals(activityIslandGroupPrice.getType())){
				   double  temp = currencyConverter(activityIslandGroupPrice.getPrice(),activityIslandGroupPrice.getCurrencyId(),CompanyId);
				   if(totalMoney<temp){
					   currencyId=activityIslandGroupPrice.getCurrencyId();
					   price = activityIslandGroupPrice.getPrice();
				   }
			   }
		   }
	    }
		/**
		 * add value
		 */
	}
	
	/**
	 * 其他币种转换成人民币
	 */
	public double currencyConverter(Double count,Integer currencyId,Long companyId){
		double totalMoney =0;
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT c.currency_id,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=");
		buffer.append(currencyId);
		buffer.append(" AND c.create_company_id=");
		buffer.append(companyId);
		List<Map<String, Object>> list = currencyDao.findBySql(buffer.toString(), Map.class);
		Map<String, Object>  mp =  list.get(0);
		totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*count;
		BigDecimal b = new BigDecimal(totalMoney);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 构造航空公司--航班号--舱位等级add by hhx
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "airlineAjaxCheck")
	private Map<String, Object> getDataByUuidAndType(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 需要添加的参数自己指定
		String type = request.getParameter("type");
		String airlinecode = request.getParameter("airline");
		String flightnum = request.getParameter("flightnum");

		if ("airline".equalsIgnoreCase(type)) {
			List<AirlineInfo> list = airlineInfoService
					.getDistinctAirline(companyId);
			map.put("airline", list);
		} else if ("flightnum".equalsIgnoreCase(type)) {
			List<AirlineInfo> flightList = airlineInfoService
					.getDistinctFlightNum(companyId, airlinecode);
			map.put("flightnum", flightList);
		} else if ("spacelevel".equalsIgnoreCase(type)) {
			List<AirlineInfo> spaceList = airlineInfoService
					.getDistinctSpaceLevel(companyId, airlinecode, flightnum);
			map.put("spacelevel", spaceList);
		}
		return map;
	}
	/**
	 * wangxv
	 * @param i
	 * @param tem
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	public List<HotelAnnex> getFileList(int i,String[] tem,String[] fileName,String[] filePath){
		List<HotelAnnex> fileList =new ArrayList<HotelAnnex>();
		if(tem!=null&&tem.length>0){
			for(int j=0;j<tem.length;j++,i++){
			HotelAnnex ha = new HotelAnnex();
			ha.setDocId(Integer.parseInt(tem[j]));
			ha.setDocName(fileName[i]);
			ha.setDocPath(filePath[i]);
			fileList.add(ha);
			}
		}
		return fileList;
	}
	 
	/**
	 * wangxv
	 * @param str
	 * @return
	 */
	public List<HotelAnnex> getVisaFileList(String str) {
		List<HotelAnnex> list = new ArrayList<HotelAnnex>();
		if (StringUtils.contains(str, ",")) {
			String[] fileDetail = str.split(",");
			for (int i = 0; i < fileDetail.length; i++) {
				String[] file = fileDetail[i].split("#");
				HotelAnnex ha = new HotelAnnex();
				ha.setDocId(Integer.parseInt(file[0]));
				ha.setDocPath(file[2]);
				ha.setDocName(file[1]);
				list.add(ha);
			}
			return list;
		} else if (StringUtils.contains(str, "#")) {
			String[] file = str.split("#");
			HotelAnnex ha = new HotelAnnex();
			ha.setDocId(Integer.parseInt(file[0]));
			ha.setDocPath(file[2]);
			ha.setDocName(file[1]);
			list.add(ha);
			return list;
		}
		return list;
	}
	/**
	 * 唯一性验证产品编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("checkedSerNum")
	public Object checkedSerNum(HttpServletRequest request){
		Map<String, Object> data=new HashMap<String,Object>();
		ActivityIslandQuery activityIslandQuery=new ActivityIslandQuery();
		String activitySerNum=request.getParameter("activitySerNum");
		activityIslandQuery.setActivitySerNum(activitySerNum);
		List<ActivityIsland> islandList=activityIslandService.find(activityIslandQuery);
		if(islandList.size()>0){
			data.put("message", "true");
		}
		
		return data;
	} 
	/**
	 * add by wangXK
	 * @param type  room baseMeal upMeal，islandway
	 * @param uuid 房型:hotelUUid,升级餐型： hotelUUId,基础餐型：roomUUid,上岛方式：
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getOptions")
	public Map<String,Object> getOptions(String type,String uuid,String islandUuid){
		Map<String,Object> map = new HashMap<String,Object>();
		if("room_upMeal_islandWay".equals(type)){//uuid 根据酒店的uuid 查询房型list
			//房型
			List<HotelRoom> roomList = new ArrayList<HotelRoom>();
			if(StringUtils.isNotBlank(uuid)){
				HotelRoom hotelRoom = new HotelRoom();
				hotelRoom.setHotelUuid(uuid);
				hotelRoom.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				roomList = hotelRoomService.find(hotelRoom);
			}
			map.put("room", roomList);
			//升级餐型
			List<HotelMeal> upMealList = new ArrayList<HotelMeal>();
			if(StringUtils.isNotBlank(uuid)){
				HotelMeal upMeal = new HotelMeal();
				upMeal.setHotelUuid(uuid);
				upMeal.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				upMealList = hotelMealService.find(upMeal);
			}
			map.put("upMeal", upMealList);
			//上岛方式
			List<Sysdefinedict> islandWayList = new ArrayList<Sysdefinedict>();
			if(StringUtils.isNotBlank(islandUuid)){
				Island island = islandService.getByUuid(islandUuid);
				if(island!=null && StringUtils.isNotBlank(island.getIslandWay())){
					String[] islandWayArr = island.getIslandWay().split(",");
					for(int i=0;i<islandWayArr.length;i++){
						Sysdefinedict sysdefinedict = sysDefinedictService.findByUUid(islandWayArr[i]);
						islandWayList.add(sysdefinedict);
					}
				}
			}
			map.put("islandWay", islandWayList);
		}else if("baseMeal".equals(type)){//uuid 根据酒店房型的uuid查询基础餐
			List<HotelMeal> list = hotelRoomMealService.findByHotelRoomUUid(uuid);
			map.put("baseMeal", list);
		}
		return map;
	}
	
	/**
	 * add by wangXK
	 * @param type  room baseMeal upMeal，islandway
	 * @param uuid 房型:hotelUUid,升级餐型： hotelUUId,基础餐型：roomUUid,上岛方式：
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getOptionsHotel")
	public Map<String,Object> getOptionsHotel(String type,String uuid,String islandUuid){
		Map<String,Object> map = new HashMap<String,Object>();
		if("room_upMeal_islandWay".equals(type)){//uuid 根据酒店的uuid 查询房型list
			//房型
			List<HotelRoom> roomList = new ArrayList<HotelRoom>();
			if(StringUtils.isNotBlank(uuid)){
				HotelRoom hotelRoom = new HotelRoom();
				hotelRoom.setHotelUuid(uuid);
				hotelRoom.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				roomList = hotelRoomService.find(hotelRoom);
			}
			map.put("room", roomList);
			//升级餐型
			List<HotelMeal> upMealList = new ArrayList<HotelMeal>();
			if(StringUtils.isNotBlank(uuid)){
				HotelMeal upMeal = new HotelMeal();
				upMeal.setHotelUuid(uuid);
				upMeal.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				upMealList = hotelMealService.find(upMeal);
			}
			map.put("upMeal", upMealList);
			//上岛方式
//			List<SysCompanyDictView> islandWayList = new ArrayList<SysCompanyDictView>();
			List<SysCompanyDictView> listIslandWay = new ArrayList<SysCompanyDictView>();
			if(StringUtils.isNotBlank(islandUuid)){
//				Island island = islandService.getByUuid(islandUuid);
//				if(island!=null && StringUtils.isNotBlank(island.getIslandWay())){
//					String[] islandWayArr = island.getIslandWay().split(",");
//					for(int i=0;i<islandWayArr.length;i++){
//						Sysdefinedict sysdefinedict = sysDefinedictService.findByUUid(islandWayArr[i]);
//						islandWayList.add(sysdefinedict);
//					}
//				}
				  Island island = islandService.getByUuid(islandUuid);
				    String[] islandWayUuids = null;
				    if(island != null && StringUtils.isNotEmpty(island.getIslandWay())){
				    	islandWayUuids = island.getIslandWay().toString().split(",");
				    	listIslandWay = sysCompanyDictViewService.findByUuids(islandWayUuids);
//				    	map.put("listIslandWay", listIslandWay);
//				    	islandWayList.add(listIslandWay);
				    }
				
			}
			map.put("islandWay", listIslandWay);
		}else if("baseMeal".equals(type)){//uuid 根据酒店房型的uuid查询基础餐
			List<HotelMeal> list = hotelRoomMealService.findByHotelRoomUUid(uuid);
			map.put("baseMeal", list);
		}
		return map;
	}
	

	/**
	 * 新增接口,从数据库中查询出要展现的数据，转换成JSON串返回给前台
	 */
	@ResponseBody
	@RequestMapping("getActivityIslandGroupJson")
	public String getActivityIslandGroupJson(HttpServletRequest request){
		String activityIslandGroupUuid = request.getParameter("activityIslandGroupUuid");
		String jsonStr = activityIslandService.getJsonStringByActivityIslandGroupUuid(activityIslandGroupUuid);
		return jsonStr;
	}
	
	/**
	 * 唯一性验证团期号
	 */
	@ResponseBody
	@RequestMapping("checkedGroup")
	public Object checkedGroup(HttpServletRequest request){
		Map<String, Object> date=new HashMap<String,Object>();
		ActivityIslandGroupQuery activityIslandGroupQuery=new ActivityIslandGroupQuery();
		String groupCode=request.getParameter("groupCode");
		activityIslandGroupQuery.setGroupCode(groupCode);
		activityIslandGroupQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		activityIslandGroupQuery.setStatus(String.valueOf(ActivityIslandGroup.STATUS_PUTAWAY_FLAG));
		List<ActivityIslandGroup> islandGroupList=activityIslandGroupService.find(activityIslandGroupQuery);
		if(islandGroupList.size()>0){
			date.put("message", "true");
		}
		
		return date;
	}
	
}