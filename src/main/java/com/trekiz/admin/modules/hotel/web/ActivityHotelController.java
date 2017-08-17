/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupLowprice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMeal;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupMealRise;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupPrice;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelShare;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.input.ActivityHotelInput;
import com.trekiz.admin.modules.hotel.input.ActivityHotelJsonBeanInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupQuery;
import com.trekiz.admin.modules.hotel.query.ActivityHotelQuery;
import com.trekiz.admin.modules.hotel.query.HotelOrderQuery;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupControlDetailService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupLowpriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealRiseService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupMealService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupPriceService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupRoomService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelShareService;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelControlService;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.User;
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
@RequestMapping(value = "${adminPath}/activityHotel")
public class ActivityHotelController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/activityhotel/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/activityHotel/list";
	protected static final String FORM_PAGE = "modules/hotel/activityhotel/form";
	protected static final String SHOW_PAGE = "modules/hotel/activityhotel/show";
	protected static final String SHOW_ACTIVITY_HOTEL_DETAIL_PAGE="modules/hotel/activityhotel/showActivityHotelDetail";
	protected static final String ACTIVITY_HOTEL_PRODUCT_LIST="modules/hotel/activityhotel/activityHotelProductList";
	protected static final String ACTIVITY_HOTEL_GROUP_LIST="modules/hotel/activityhotel/activityHotelGroupList";
	
	
	//酒店产品‘TYPE_PRODUCT’酒店团期‘TYPE_GROUP’
	protected static final String TYPE_PRODUCT="product";
	protected static final String TYPE_GROUP="group";
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
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
	private HotelMealService mealService;
	@Autowired
	private TravelerTypeService travelerTypeService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private HotelControlService hotelControlService;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	@Autowired
	private ActivityHotelShareService activityHotelShareService;
	@Autowired
	private ActivityHotelGroupControlDetailService activityHotelGroupControlDetailService;
	
	private ActivityHotel dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=activityHotelService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(ActivityHotelQuery activityHotelQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		activityHotelQuery.setDelFlag("0");
        Page<ActivityHotel> page = activityHotelService.find(new Page<ActivityHotel>(request, response), activityHotelQuery);
        
        model.addAttribute("page", page);
        model.addAttribute("activityHotelQuery", activityHotelQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(ActivityHotelInput activityHotelInput, Model model) {
		model.addAttribute("activityHotelInput", activityHotelInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(ActivityHotelInput activityHotelInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			activityHotelService.save(activityHotelInput, "");
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
		model.addAttribute("activityHotel", activityHotelService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	/**
	 * 跳转到酒店修改页面
	 * @param uuid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		ActivityHotel activityHotel = activityHotelService.getByUuid(uuid);
		ActivityHotelInput activityHotelInput = new ActivityHotelInput(activityHotel);
		model.addAttribute("activityHotelInput", activityHotelInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(ActivityHotelInput activityHotelInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, activityHotelInput,true);
			activityHotelService.update(dataObj);
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
					activityHotelService.removeByUuid(uuid);
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
	 * 显示酒店产品的详情及酒店产品团期的详情
	 * @param uuid
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("showActivityHotelDetail/{uuid}")
	public String showActivityHotelDetail(@PathVariable String uuid,HttpServletRequest request,Model model){
		String type=request.getParameter("type");
		if(StringUtils.isEmpty(type)||StringUtils.isEmpty(uuid)){
			return ACTIVITY_HOTEL_PRODUCT_LIST;
		}
		ActivityHotel activityHotel=null;
		List<ActivityHotelGroup> groups=new ArrayList<ActivityHotelGroup>();
		if(TYPE_PRODUCT.equals(type)){
			
			activityHotel=activityHotelService.getByUuid(uuid);
			if(activityHotel==null){
				return ACTIVITY_HOTEL_PRODUCT_LIST;
			}
			//获取所有的附件列表
			getAllFileList(uuid,model);
			groups=activityHotelGroupService.findGroupsByActivityHotelUuid(uuid);
			
		}else if(TYPE_GROUP.equals(type)){
			//当用户查看的是团期详情时展示单个团期的详情
			ActivityHotelGroup group=activityHotelGroupService.getByUuid(uuid);
			if(group!=null){
			
			getAllFileList(group.getActivityHotelUuid(), model);
			activityHotel=activityHotelService.getByUuid(group.getActivityHotelUuid());
			groups.add(group);}
		}
		//查询出团期中相关数据进行封装
		
		if(CollectionUtils.isNotEmpty(groups) && activityHotel!=null){
			for(ActivityHotelGroup group:groups){
				//团期根据所有升级餐型进行合并
				int mealRiseRowspan = 0;
				//预报名
				Integer orderNum=hotelOrderService.getForecaseReportNum(group.getUuid());
				group.setOrderNum(orderNum);
				//同行价格
				List<ActivityHotelGroupPrice> groupPriceList=activityHotelGroupPriceService.getPriceFilterTravel(group.getUuid(),activityHotel.getHotelUuid());
				group.setActivityHotelGroupPriceList(groupPriceList);
				
				//房型
				List<ActivityHotelGroupRoom> groupRoomList=activityHotelGroupRoomService.getRoomListByGroupUuid(group.getUuid());
				for(ActivityHotelGroupRoom room : groupRoomList){
					//房型下的餐型
					List<ActivityHotelGroupMeal> roomMealList = activityHotelGroupMealService.getByactivityHotelGroupRoomUuid(room.getUuid());
					room.setActivityHotelGroupMealList(roomMealList);
					//餐型下的升级餐型
					if(CollectionUtils.isNotEmpty(roomMealList)){
						for(ActivityHotelGroupMeal meal:roomMealList){
							List<ActivityHotelGroupMealRise> mealRiseList=activityHotelGroupMealRiseService.getMealRiseByMealUuid(meal.getUuid());
							meal.setActivityHotelGroupMealsRiseList(mealRiseList);
							//累加所有的升级餐型总数
						}
					}
					mealRiseRowspan += roomMealList.size();
				}
				group.setMealRiseRowspan(mealRiseRowspan==0?1:mealRiseRowspan);
				group.setActivityHotelGroupRoomList(groupRoomList);
			}	
		}
		//add by WangXK 20151020添加非空指针判断
		if(activityHotel!=null){
			List<User> shareUser=activityHotelShareService.findUserByActivityHotelUuid(activityHotel.getUuid());
			model.addAttribute("shareUser", shareUser);
			Integer val=hotelService.getHotelStarValByHotelUuid(activityHotel.getHotelUuid());
			model.addAttribute("val",val);
			activityHotel.setActivityHotelGroupList(groups);
			
		}
		model.addAttribute("activityHotel",activityHotel);
		return SHOW_ACTIVITY_HOTEL_DETAIL_PAGE;
	}

	
	@SuppressWarnings("unused")
	private void getAllFile(String uuid, Model model) {
		HotelAnnex ha=new HotelAnnex();
		ha.setUuid(uuid);
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_PRODSCH);
		List<HotelAnnex> prodschList=hotelAnnexService.find(ha);
		if(prodschList!=null&&prodschList.size()>0){
			model.addAttribute("prodschList",prodschList);//获取产品行程介绍附件列表
		}
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_COSTPROTOCOL);
		List<HotelAnnex> costProtocolchList=hotelAnnexService.find(ha);
		if(costProtocolchList!=null&&prodschList.size()>0){
			model.addAttribute("costProtocolchList",costProtocolchList);//获取产品行程介绍附件列表
		}
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_ISLAND_OTHERPROTOCOL);
		List<HotelAnnex> otherProtocolList=hotelAnnexService.find(ha);
		if(otherProtocolList!=null&&prodschList.size()>0){
			model.addAttribute("otherProtocolList",otherProtocolList);//获取产品行程介绍附件列表
		}
		
	}

	/***
	 * 跳转到酒店产品发布
	 * @author wangxv  
	 * @param ActivityHotelInput
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "activityHotelForm")
	public String activityHotelForm(@ModelAttribute ActivityHotelInput activityHotelInput, Model model) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
//		HotelMeal hotelMeal = new  HotelMeal(); 
//      hotelMeal.setDelFlag(Context.DEL_FLAG_NORMAL);
//      hotelMeal.setWholesalerId(companyId.intValue());
//      List<HotelMeal>  hotelMeals  = mealService.find(hotelMeal);
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
//		model.addAttribute("hotelMealsList", hotelMeals);
//		List<TravelerType> travelerTypes = travelerTypeService.find(new TravelerType(companyId.intValue(),"0","1"));//0代表是否删除，1代表是否启用
//		model.addAttribute("travelerTypes", travelerTypes);
		model.addAttribute("countryUuid", "80415d01488c4d789494a67b638f8a37");//默认国家是马尔代夫    
		
		return "modules/hotel/activityhotel/activityHotelForm";
	}
	
	/***
	 * 
	 * 酒店产品发布保存
	 * @author wangxv
	 * @param ActivityHotelInput
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveActivityHotel")
	public Map<String,String> saveActivityHotel(ActivityHotelInput activityHotelInput, Model model, HttpServletRequest request) {
		//附件
		String[] docIdProArray = request.getParameterValues("DocIdPro");
		String[] docIdCostArray = request.getParameterValues("DocIdCost");
		String[] docIdOtherArray = request.getParameterValues("DocIdOther");
		String[] docOriNameArray = request.getParameterValues("docOriName");
		String[] docPathArray = request.getParameterValues("docPath");
		String[] jsontext = request.getParameterValues("jsoninput");
		Map<String,String> result = new HashMap<String, String>();
		if(jsontext != null){
			List<ActivityHotelGroup> activityhotelGroupLists = new ArrayList<ActivityHotelGroup>();
			for(String jsonstr : jsontext){
				 ActivityHotelJsonBeanInput info = JSON.parseObject(jsonstr,ActivityHotelJsonBeanInput.class);
				 ActivityHotelGroup ahg = info.transfer2ActivityHotelGroup();
				 Map<String, Object> resultMap = (Map<String, Object>)checkedGroup(ahg.getGroupCode(),null);
				 if("true".equals(resultMap.get("message"))){
					 result.put("message", "2");
					 result.put("error", ahg.getGroupCode()+" 团号已经存在！");
					 return result;
				 }
				 activityhotelGroupLists.add(ahg);
			}
			 activityHotelInput.setActivityhotelGroupLists(activityhotelGroupLists);
		}
		//记录上传的各种文件的数量
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
		activityHotelInput.setProdSchList(prodSchList);
		activityHotelInput.setCostProtocolList(costProtocolList);
		activityHotelInput.setOtherProtocolList(otherProtocolList);
		
		String status = request.getParameter("status");//1：上架；2：下架；3：草稿；4：已删除
		try {
			result = activityHotelService.save(activityHotelInput,status);
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
	 * 单个增加酒店产品团期的保存方法
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveActivityHotelForGroup")
	public Map<String,String> saveActivityHotelForGroup(HttpServletRequest request) {
		
		Map<String,String> result = new HashMap<String, String>();
		String jsonstr = request.getParameter("jsonstr");
//		String activityHotelUuid = request.getParameter("activityHotelUuid");
		if(StringUtils.isEmpty(jsonstr)){
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
			return result;
		}
		 ActivityHotelJsonBeanInput info = JSON.parseObject(jsonstr,ActivityHotelJsonBeanInput.class);
		 ActivityHotelGroup ahg = info.transfer2ActivityHotelGroup();
		 String statusStr = request.getParameter("statusStr");
		 if(!"3".equals(statusStr)){
			 Map<String, Object> resultMap = (Map<String, Object>)checkedGroup(ahg.getGroupCode(),null);
			 if("true".equals(resultMap.get("message"))){
				 result.put("message", "2");
				 result.put("error", ahg.getGroupCode()+" 团号已经存在！");
				 return result;
			 }
		 }
		 String status = request.getParameter("statusStr");//1：上架；2：下架；3：草稿；4：已删除
		try {
			result = activityHotelService.saveorUpdateActivityHotel(ahg,ahg.getActivityHotelUuid(),status);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
			return result;
		}
		return result;
		
	}
	
	/**
	 * 跳转到修改酒店产品页面
	 * @param uuid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "editActivityHotel/{uuid}")
	public String editActivityHotel(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return ACTIVITY_HOTEL_PRODUCT_LIST;
		}
		ActivityHotel activityHotel = activityHotelService.getByUuid(uuid);
		Integer startlevel = hotelService.getHotelStarValByHotelUuid(activityHotel.getHotelUuid());
		List<ActivityHotelGroup> detailGroupList = activityHotelGroupService.findGroupsByActivityHotelUuid(uuid);
		//产品分享
		List<ActivityHotelShare> hotelShareList  = activityHotelShareService.findByActivityHotelUuid(uuid);
		String key = "";
		if(CollectionUtils.isNotEmpty(hotelShareList)){
			for(ActivityHotelShare list : hotelShareList){
				key+=list.getAcceptShareUser()+",";
			}
		}
		if(StringUtils.isNotEmpty(key)){
			key = key.substring(0, key.lastIndexOf(","));
			model.addAttribute("key", key);
		}
		//装载上传资料信息
		getAllFileList(uuid,model);
		//允许修改标识
		//boolean editFlag = true;
		if(CollectionUtils.isNotEmpty(detailGroupList)){
			for(ActivityHotelGroup hotelGroups : detailGroupList){
				String hotelGroupUuid = hotelGroups.getUuid();
				
				List<ActivityHotelGroupRoom> roomList = activityHotelGroupRoomService.getRoomListByGroupUuid(hotelGroupUuid);
				//test
				if(CollectionUtils.isNotEmpty(roomList)){
					for(ActivityHotelGroupRoom roomLists : roomList){
			      	    List<ActivityHotelGroupMeal> mealRiseLists = activityHotelGroupMealService.getMealListByRoomUuid(roomLists.getUuid());
			      	    roomLists.setActivityHotelGroupMealList(mealRiseLists);
					}
				}
			    //test
				List<ActivityHotelGroupMeal> mealList = activityHotelGroupMealService.getMealListByGroupUuid(hotelGroupUuid);
				/*if(CollectionUtils.isNotEmpty(mealList)){
					for(ActivityHotelGroupMeal mealLists : mealList){
			      	    List<ActivityHotelGroupMealRise> mealRiseList = activityHotelGroupMealRiseService.getMealRiseByMealUuid(mealLists.getUuid());
			      	    mealLists.setActivityHotelGroupMealsRiseList(mealRiseList);
					}
				}*/
				List<ActivityHotelGroupPrice> priceList = activityHotelGroupPriceService.getPriceFilterTravel(hotelGroupUuid,activityHotel.getHotelUuid());
				List<ActivityHotelGroupControlDetail> detailList = activityHotelGroupControlDetailService.getDetailListByGroupUuid(hotelGroupUuid);
				
				hotelGroups.setActivityHotelGroupRoomList(roomList);
				hotelGroups.setActivityHotelGroupMealList(mealList);
				hotelGroups.setActivityHotelGroupPriceList(priceList);
				hotelGroups.setActivityHotelGroupControlDetail(detailList);
				
				/*if(controlDetail.getStatus() == HotelControlDetail.STATUS_SUBMIT_FLAG) {
					editFlag = false;
				}*/
				//添加预报名数量
				Integer orderNum=hotelOrderService.getForecaseReportNum(hotelGroupUuid);
				hotelGroups.setPreApplyNum(orderNum==null?0:orderNum);
			}
		}

		activityHotel.setActivityHotelGroupList(detailGroupList);
		activityHotel.setActivityHotelShareList(hotelShareList);
		ActivityHotelInput activityHotelInput = new ActivityHotelInput();
		activityHotelInput.initActivityHotelInput(activityHotel);
		
		//model.addAttribute("editFlag", editFlag);
		//读取公司配置币种
		Long companyId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
        List<TravelerType> travelerTypes = travelerTypeService.find(new TravelerType(companyId.intValue(),"0","1"));//0代表是否删除，1代表是否启用
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
		model.addAttribute("travelerTypes", travelerTypes);
		model.addAttribute("activityHotelInput", activityHotelInput);
		//酒店星级
		model.addAttribute("startlevel", startlevel);
	
		return "modules/hotel/activityhotel/activityHotelFormEdit";
	}
	
	/**
	 * 保存酒店产品的修改
	 * @param activityHotelInput
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateActivityHotel")
	public Object updateActivityHotel(ActivityHotelInput activityHotelInput, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {

		Map<String,String> result = new HashMap<String, String>();
		try{
			//获取上传附件
			String[] docIdProArray = request.getParameterValues("DocIdPro");
			String[] docIdCostArray = request.getParameterValues("DocIdCost");
			String[] docIdOtherArray = request.getParameterValues("DocIdOther");
			String[] docOriNameArray = request.getParameterValues("docOriName");
			String[] docPathArray = request.getParameterValues("docPath");
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
			activityHotelInput.setProdSchList(prodSchList);
			activityHotelInput.setCostProtocolList(costProtocolList);
			activityHotelInput.setOtherProtocolList(otherProtocolList);
			
			//获取修改的酒店产品团期json信息
			String[] jsontext = request.getParameterValues("jsoninput");
			result = activityHotelService.updateActivityHotel(activityHotelInput,jsontext);
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
	 * 单个修改酒店产品团期的方法
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateActivityHotelForGroup")
	public Object updateActivityHotelForGroup(HttpServletRequest request) {
		Map<String,String> result = new HashMap<String, String>();
		String jsonstr = request.getParameter("jsonstr");
		//String activityHotelUuid = request.getParameter("activityHotelUuid");
		if(StringUtils.isEmpty(jsonstr)){
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
			return result;
		}
		 ActivityHotelJsonBeanInput info = JSON.parseObject(jsonstr,ActivityHotelJsonBeanInput.class);
		 ActivityHotelGroup ahg = info.transfer2ActivityHotelGroup();
		try{
			result = activityHotelService.saveorUpdateActivityHotel(ahg,ahg.getActivityHotelUuid(),null);
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
	 * 构造酒店产品列表add by hhx
	 * 
	 * @param query
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "activityHotelList")
	public ModelAndView getActivityHotelList(ActivityHotelQuery query,
			HttpServletRequest request, HttpServletResponse response ) {
		String orderBy = request.getParameter("orderBy");
		String status = request.getParameter("status");
		String showType = request.getParameter("showType");
		Long companyId = UserUtils.getUser().getCompany().getId();
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,
				response);
		ModelAndView mav = new ModelAndView();
		Set<Department> deptSet = UserUtils.getDepartmentByJob();
		//deptSet.add(new Department());
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
					submap.put("orderNum", orderNum==null?0:orderNum);
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
				map.put("orderNum", orderNum==null?0:orderNum);
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
		List<TravelerType> travelerTypes = travelerTypeService.find(new TravelerType(companyId.intValue(),"0"));
		mav.addObject("travelerTypes", travelerTypes);
		mav.addObject("hotelStarList", hotelStarList);
		mav.setViewName("2".equals(showType) ? ACTIVITY_HOTEL_PRODUCT_LIST
				: ACTIVITY_HOTEL_GROUP_LIST);
		return mav;
	}
	/**
	 * 获取上传资料信息
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
	 * 根据产品的uuid来跟新产品下团期的状态
	 * @param uuids
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateGroupStatusByActivityUuid")
	public Object updateGroupStatusByActivityUuid(String uuids, String status) {
		Map<String,Object> datas = new HashMap<String, Object>();
		if(StringUtils.isEmpty(uuids)) {
			datas.put("result", "fail");
			datas.put("message", "系统发生异常，请重新操作!");
			return datas;
		}
		try{
			List<ActivityHotelGroup> list = activityHotelGroupService.findGroupsByActivityHotelUuid(uuids);
			/*if("4".equals(status)){
				for(ActivityHotelGroup group:list){
					HotelOrderQuery query = new HotelOrderQuery();
					query.setActivityHotelGroupUuid(group.getUuid());
					query.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
					query.setOrderStatus(null);
					List<HotelOrder> orderList = hotelOrderService.find(query); 
					if(orderList.size()>0){
						datas.put("result", "0");
						datas.put("message", "本产品下,团号: "+group.getGroupCode()+" 存在报名订单,不允许对本产品执行删除操作!");
						return datas;
					}
				}
			}*/
			//对产品进行上架操作,只允许操作下架或草稿状态
			if("1".equals(status)){
				for(ActivityHotelGroup group:list){
					if(!"2".equals(group.getStatus()) && !"3".equals(group.getStatus())){
						datas.put("result", "fail");
						datas.put("message", "当前产品下包含非下架或草稿状态的团期,不允许对产品执行上架操作.");
						return datas;
					}
				}
			//对产品进行下架操作,如果有一个团期为非上架状态,则不能进行下架操作
			}else if("2".equals(status)){
				for(ActivityHotelGroup group:list){
					if(!"1".equals(group.getStatus())){
						datas.put("result", "fail");
						datas.put("message", "当前产品下包含非上架状态的团期,不允许对产品执行下架操作");
						return datas;
					}
				}
			//对产品进行删除操作,如果有一个团期为已删除状态,则不能进行删除操作
			}else if("4".equals(status)){
				for(ActivityHotelGroup group:list){
					if("4".equals(group.getStatus())){
						datas.put("result", "fail");
						datas.put("message", "当前产品下包含已删除状态的团期,不允许对产品执行删除操作.");
						return datas;
					}
				}
			//对产品进行恢复操作,如果有一个团期非已删除状态,则不能进行恢复操作
			}else if("3".equals(status)){
				for(ActivityHotelGroup group:list){
					if(!"4".equals(group.getStatus())){
						datas.put("result", "fail");
						datas.put("message", "当前产品下包含非删除状态的团期,不允许对产品执行恢复操作.");
						return datas;
					}
				}
			}
			activityHotelGroupService.updateGroupStatusByActivityUuid(uuids, "3".equals(status)?"2":status);
			datas.put("result", "success");
			datas.put("message", "更新成功!");
		} catch (Exception e) {
			//log.error("根据产品的uuid来跟新产品下团期的状态出错"+e.getMessage());
			datas.put("result", "fail");
			datas.put("message", "系统发生异常，请重新操作!");
		}
		return datas;
	}
	
	/**
	 * 根据uuid来更新团期的状态
	 * @param uuids
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateGroupStatusByUuid")
	public Object updateGroupStatusByUuid(String uuids, String status) {
		Map<String,Object> datas = new HashMap<String, Object>();
		if(StringUtils.isEmpty(uuids)) {
			datas.put("result", "fail");
			datas.put("message", "系统发生异常，请重新操作!");
			return datas;
		}
		try{
			//如果传入3,为恢复操作,状态应该改为2,已下架 
			activityHotelGroupService.updateGroupStatusByUuid(uuids, "3".equals(status)?"2":status);
			datas.put("result", "success");
			datas.put("message", "更新成功!");
		} catch (Exception e) {
			//log.error("根据uuid来更新团期的状态"+e.getMessage());
			datas.put("result", "fail");
			datas.put("message", "系统发生异常，请重新操作!");
		}
		return datas;
	}
	
	/**
	 * 修改activity_hotel_group的delflag
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
					HotelOrderQuery query = new HotelOrderQuery();
					query.setActivityHotelGroupUuid(uuid);
					query.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
					query.setOrderStatus(null);
					List<HotelOrder> list = hotelOrderService.find(query); 
					if(list.size()>0){
						ActivityHotelGroup group = activityHotelGroupService.getByUuid(uuid);
						datas.put("result", "0");
						datas.put("message", "团号: "+group.getGroupCode()+" 存在报名订单,不允许删除!");
						return datas;
					}
					activityHotelGroupService.removeByUuid(uuid);
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
	 * 获取发布酒店产品时的上传文件
	 * @param uuid
	 * @param model
	 */
	private void getAllFileList(String uuid, Model model) {
		HotelAnnex ha = new HotelAnnex();
		ha.setDelFlag("0");
		//ha.setMainUuid(activityIsland.getUuid());
		ha.setMainUuid(uuid);
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_PRODSCH);//获取产品行程介绍附件列表
		List<HotelAnnex> prodSchList = hotelAnnexService.find(ha);
		if(prodSchList!=null&&prodSchList.size()>0){
			model.addAttribute("prodSchList", prodSchList);
		}
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_COSTPROTOCOL);//获取自费补充协议附件列表
		List<HotelAnnex> costProtocolList = hotelAnnexService.find(ha);
		if(costProtocolList!=null&&costProtocolList.size()>0){
			model.addAttribute("costProtocolList", costProtocolList);
		}
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_ACTIVITY_HOTEL_OTHERPROTOCOL);//获取其他补充协议附件列表
		List<HotelAnnex> otherProtocolList = hotelAnnexService.find(ha);
		if(otherProtocolList!=null&&otherProtocolList.size()>0){
			model.addAttribute("otherProtocolList", otherProtocolList);
		}
	}
	/**
	 * 唯一性验证
	 */
	@ResponseBody
	@RequestMapping("checkedGroup")
	public Object checkedGroup(String groupCode, HttpServletRequest request){
		Map<String, Object> data=new HashMap<String,Object>();
		ActivityHotelGroupQuery activityHotelGroupQuery=new ActivityHotelGroupQuery();
		activityHotelGroupQuery.setGroupCode(groupCode);
		activityHotelGroupQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		activityHotelGroupQuery.setStatus("1");
		List<ActivityHotelGroup> islandGroupList=activityHotelGroupService.find(activityHotelGroupQuery);
		if(islandGroupList.size()>0){
			data.put("message", "true");
		} else {
			data.put("message", "false");
		}
		return data;
	}
	
	/**
	 * 由库中数据返回JSON字符
	 */
	@ResponseBody
	@RequestMapping("getActivityGroupJson")
	public String getActivityGroupJson(HttpServletRequest request){
		String activityGroupUuid = request.getParameter("activityGroupUuid");
		ActivityHotelGroup hotelGroup = activityHotelGroupService.getByUuid(activityGroupUuid);
		List<ActivityHotelGroupRoom> roomList = activityHotelGroupRoomService.getRoomListByGroupUuid(activityGroupUuid);
		List<ActivityHotelGroupPrice> priceList = activityHotelGroupPriceService.getPriceListByGroupUuid(activityGroupUuid);
		List<ActivityHotelGroupControlDetail> controlDetailList = activityHotelGroupControlDetailService.getDetailListByGroupUuid(activityGroupUuid);
		if(CollectionUtils.isNotEmpty(controlDetailList)){
		hotelGroup.setActivityHotelGroupControlDetail(controlDetailList);
		}
		if(CollectionUtils.isNotEmpty(priceList)){
		hotelGroup.setActivityHotelGroupPriceList(priceList);
		}
		if(CollectionUtils.isNotEmpty(roomList)){
		hotelGroup.setActivityHotelGroupRoomList(roomList);
		}
		ActivityHotelJsonBeanInput jsonInput = new ActivityHotelJsonBeanInput();
		jsonInput.initActivityHotelJsonInput(hotelGroup);
		String jsonStr = JSON.toJSONString(jsonInput);
		return jsonStr;
	}
}
