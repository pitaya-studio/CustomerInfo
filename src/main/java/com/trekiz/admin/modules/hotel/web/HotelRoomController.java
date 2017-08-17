/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotel.entity.HotelGuestTypeRelation;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoom;
import com.trekiz.admin.modules.hotel.entity.HotelRoomMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoomOccuRate;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.query.HotelGuestTypeRelationQuery;
import com.trekiz.admin.modules.hotel.service.HotelGuestTypeRelationService;
import com.trekiz.admin.modules.hotel.service.HotelGuestTypeService;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotel.service.HotelRoomMealService;
import com.trekiz.admin.modules.hotel.service.HotelRoomOccuRateDetailService;
import com.trekiz.admin.modules.hotel.service.HotelRoomOccuRateService;
import com.trekiz.admin.modules.hotel.service.HotelRoomService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerTypeRelationService;
import com.trekiz.admin.modules.hotel.service.SysdefinedictService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelRoom")
public class HotelRoomController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelroom/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelRoom/list";
	protected static final String FORM_PAGE = "modules/hotel/hotelroom/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelroom/show";
	
	@Autowired
	private HotelRoomService hotelRoomService;
	
	@Autowired
	private SysdefinedictService sysdefinedictService;

	@Autowired
	private HotelRoomOccuRateDetailService hotelRoomOccuRateDetailService;

	@Autowired
	private HotelRoomOccuRateService hotelRoomOccuRateService;
	
	@Autowired
	private TravelerTypeService travelerTypeService;
	@Autowired
	private HotelMealService hotelMealService;
	@Autowired
	private HotelRoomMealService hotelRoomMealService;
	@Autowired
	private HotelGuestTypeService hotelGuestTypeService;
	@Autowired
	private HotelGuestTypeRelationService hgtRealtionService;
	@Autowired
	private HotelTravelerTypeRelationService hotelTravelerTypeRelationService;
	
	@RequestMapping(value = "list/{hotelUuid}")
	public String list(@PathVariable String hotelUuid,HotelRoom hotelRoom, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isNotBlank(hotelUuid)&&UserUtils.getCompanyIdForData()!=null){
			hotelRoom.setDelFlag("0");
			hotelRoom.setHotelUuid(hotelUuid);
			hotelRoom.setWholesalerId(Integer.parseInt(UserUtils.getCompanyIdForData().toString()));
	        List<HotelRoom> hotelRooms = hotelRoomService.find(hotelRoom);
	        model.addAttribute("hotelRooms", hotelRooms);
	        model.addAttribute("hotelUuid", hotelUuid);
		}
        return LIST_PAGE;
	}

	@RequestMapping(value = "form/{hotelUuid}")
	public String form(HotelRoom hotelRoom, Model model,@PathVariable String hotelUuid) {
		hotelRoom.setHotelUuid(hotelUuid);
		List<HotelMeal> mealList=hotelMealService.getMealListByUuid(hotelUuid);
		model.addAttribute("mealList",mealList);
		model.addAttribute("hotelRoom", hotelRoom);
		//加载游客类型
		if(StringUtils.isNotBlank(hotelUuid)){
			model.addAttribute("travelerTypes", hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(hotelRoom.getHotelUuid()));
		}
		//添加住客类型
		model.addAttribute("hotelGuestTypes",getHotelGuestTypes());
		
		return FORM_PAGE;
	}

	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(HotelRoom hotelRoom, Model model, HttpServletRequest request) {
		Map<String,Object> datas = new HashMap<String, Object>();
		String[] mealtype = request.getParameter("mealtype").trim().split(",");
		String[] hotelGuestTypeUuids = request.getParameter("hotelGuestTypeUuids").trim().split(",");
		String[] hotelGuestTypeNames = request.getParameter("hotelGuestTypeNames").trim().split(",");
		
		if(hotelRoom != null && UserUtils.getCompanyIdForData() != null) {
			//获取容住率字符串
			String occupancyRates = request.getParameter("occupancyRates");
			if(hotelRoom.getExtraBedCost() == null) {
				hotelRoom.setExtraBedCost(0D);
			}
			hotelRoom.setWholesalerId(Integer.parseInt(UserUtils.getCompanyIdForData().toString()));
			hotelRoomService.saveHotelRoom(hotelRoom, occupancyRates);
			
			//保存中间表
			for(int i=0;i<mealtype.length;i++){
				String s = mealtype[i];
				if(s!=null&&!"".equals(s)){
					HotelRoomMeal hrm = new HotelRoomMeal();
					hrm.setHotelUuid(hotelRoom.getHotelUuid());
					hrm.setHotelMealUuid(mealtype[i]);
					hrm.setHotelRoomUuid(hotelRoom.getUuid());
					hotelRoomMealService.save(hrm);
				}
			}
			//保存 hostGuestType表的UUid 保存到HostGuestTypeRelation
			for(int i=0;i<hotelGuestTypeUuids.length;i++){
				String s = hotelGuestTypeUuids[i];
				if(s!=null&&!"".equals(s)){
					HotelGuestTypeRelation typeRelation = new HotelGuestTypeRelation();
					typeRelation.setHotelGuestTypeUuid(hotelGuestTypeUuids[i]);
					typeRelation.setHotelGuestTypeName(hotelGuestTypeNames[i]);
					typeRelation.setHotelRoomUuid(hotelRoom.getUuid());
					typeRelation.setHotelUuid(hotelRoom.getHotelUuid());
					hgtRealtionService.save(typeRelation);
				}
			}
			
			datas.put("message","1");
		} else {
			//加载游客类型
			model.addAttribute("travelerTypes", loadTravelerTypes());
			model.addAttribute("hotelGuestTypes",getHotelGuestTypes());
			datas.put("message","3");
			datas.put("error", "系统异常，请重新操作!");
		}
		return datas;
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		HotelRoom hotelRoom = hotelRoomService.getByUuid(uuid);
		if(hotelRoom.getWholesalerId() != UserUtils.getCompanyIdForData().intValue()) {
			return SHOW_PAGE;
		}
		
		List<HotelRoomMeal> meal=hotelMealService.getMealListByRoomUuid(uuid);
		List<HotelMeal> mealList=new ArrayList<HotelMeal>();
		for(HotelRoomMeal hrm:meal){
			HotelMeal hotelMeal=hotelMealService.getByUuid(hrm.getHotelMealUuid());
			mealList.add(hotelMeal);
		}
		//add by WangXK 20150812
		HotelGuestTypeRelationQuery query = new HotelGuestTypeRelationQuery();
		query.setHotelRoomUuid(uuid);
		query.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<HotelGuestTypeRelation> relationlist =  hgtRealtionService.find(query);
		
		/*String relationString = "";
		if(list != null && list.size()>0){
			for(HotelGuestTypeRelation hgtrealtion: list){
				relationString = relationString + hgtrealtion.getHotelGuestTypeUuid() + ",";
			}
			System.out.println("relationString: " + relationString);
		}
		model.addAttribute("relationlist",relationlist);
		*/
		
		//add by sy 20150917
		HotelRoomOccuRate hotelRoomOccuRate =new HotelRoomOccuRate();
		hotelRoomOccuRate.setDelFlag("0");
		hotelRoomOccuRate.setHotelRoomUuid(uuid);
		List<HotelRoomOccuRate> hotelRoomOccuRates = hotelRoomOccuRateService.find(hotelRoomOccuRate);
		int roomNum = 0;
		int hotelRoomSize = hotelRoomOccuRates.size();
		for (int i = 0; i <hotelRoomSize; i++) {
			roomNum += hotelRoomOccuRates.get(i).getOccupancy();
		}
		model.addAttribute("roomNum",roomNum);
		model.addAttribute("hotelRoomOccuRates",hotelRoomOccuRates);
		model.addAttribute("relationlist",relationlist);
		model.addAttribute("mealList",mealList);
		model.addAttribute("hotelRoom", hotelRoom);
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		
		HotelRoom hotelRoom = hotelRoomService.getByUuid(uuid);
		if(hotelRoom.getWholesalerId() != UserUtils.getCompanyIdForData().intValue()) {
			return FORM_PAGE;
		}
		//加载游客类型
		if(StringUtils.isNotBlank(hotelRoom.getHotelUuid())){
			model.addAttribute("travelerTypes", hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(hotelRoom.getHotelUuid()));
		}
		model.addAttribute("hotelRoom", hotelRoom);
		
		//加载容住率信息
		List<HotelRoomOccuRate> hotelRoomOccuRates = hotelRoomOccuRateService.findRoomOccuRateByHotelRoom(hotelRoom.getUuid());
		String rateuuid = "";
		//update by WangXK 20151020 添加空指针的判断
		for(HotelRoomOccuRate hotelRoomOccuRate : hotelRoomOccuRates) {
			if(hotelRoomOccuRate!=null){
				rateuuid = hotelRoomOccuRate.getUuid();
				hotelRoomOccuRate.setHotelRoomOccuRateDetails(hotelRoomOccuRateDetailService.findOccuRateDetailByRoomOccuRate(rateuuid));
			}
		}
		if(hotelRoomOccuRates != null && hotelRoomOccuRates.size() > 0) {
			model.addAttribute("hotelRoomOccuRates", hotelRoomOccuRates);
		}
		List<HotelRoomMeal> roomMealList=hotelMealService.getMealListByRoomUuid(uuid);
		if(StringUtils.isNotEmpty(hotelRoom.getHotelUuid())){
			List<HotelMeal> mealList=hotelMealService.getMealListByUuid(hotelRoom.getHotelUuid());
			model.addAttribute("mealList",mealList);
		}
		String mealString = "";
		for(HotelRoomMeal hrm:roomMealList){
			HotelMeal meal = hotelMealService.getByUuid(hrm.getHotelMealUuid());
			if(meal!=null){
				mealString = mealString+meal.getUuid();
			}
		}
		//add by WangXK 20150812
		HotelGuestTypeRelationQuery query = new HotelGuestTypeRelationQuery();
		query.setHotelRoomUuid(uuid);
		query.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<HotelGuestTypeRelation> list =  hgtRealtionService.find(query);
		String relationString = "";
		if(list != null && list.size()>0){
			for(HotelGuestTypeRelation hgtrealtion: list){
				relationString = relationString + hgtrealtion.getHotelGuestTypeUuid() + ",";
			}
//			System.out.println("relationString: " + relationString);
		}
		model.addAttribute("hotelGuestTypes",getHotelGuestTypes());
		model.addAttribute("relationString", relationString);
		model.addAttribute("mealString",mealString);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public Object update(HotelRoom hotelRoom, Model model, HttpServletRequest request) {
		//获取容住率uuids
		String roomOccuRateUuids = request.getParameter("roomOccuRateUuids");
		//获取容住率字符串
		String occupancyRates = request.getParameter("occupancyRates");
		String[] mealtype = request.getParameter("mealtype").trim().split(",");
		String[] hotelGuestTypeUuids = request.getParameter("hotelGuestTypeUuids").trim().split(",");
		String[] hotelGuestTypeNames = request.getParameter("hotelGuestTypeNames").trim().split(",");
		
		Map<String,Object> datas = new HashMap<String, Object>();
		HotelRoom entity = null;
		//update by WangXK 20151020 添加空指针的判断
		if(!StringUtils.isEmpty(hotelRoom.getUuid())) {
			entity = hotelRoomService.getByUuid(hotelRoom.getUuid());

			entity.setRoomName(hotelRoom.getRoomName());
			entity.setShowName(hotelRoom.getShowName());
			entity.setRoomNumb(hotelRoom.getRoomNumb());
			entity.setBed(hotelRoom.getBed());
			entity.setFloor(hotelRoom.getFloor());
			entity.setExtraBedNum(hotelRoom.getExtraBedNum());
			entity.setExtraBedCost(hotelRoom.getExtraBedCost());
			entity.setExtraBedCustomer(hotelRoom.getExtraBedCustomer());
			entity.setRoomArea(hotelRoom.getRoomArea());
			entity.setSort(hotelRoom.getSort());
			entity.setRoomFeatures(hotelRoom.getRoomFeatures());
			entity.setInDate(hotelRoom.getInDate());
			entity.setOutDate(hotelRoom.getOutDate());
			entity.setRemark(hotelRoom.getRemark());
			hotelRoomService.update(entity);
		} else {
			//加载游客类型
			model.addAttribute("travelerTypes", loadTravelerTypes());
			datas.put("message","3");
			datas.put("error", "系统异常，请重新操作!");
		}
		
		List<HotelRoomMeal> roomMealList=hotelMealService.getMealListByRoomUuid(hotelRoom.getUuid());
		for(HotelRoomMeal hrm:roomMealList){
			hrm.setDelFlag("1");
		}
		//add by WangXK 20150812
		HotelGuestTypeRelationQuery query = new HotelGuestTypeRelationQuery();
		query.setHotelRoomUuid(hotelRoom.getUuid());
		query.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<HotelGuestTypeRelation> relationList =  hgtRealtionService.find(query);
		for(HotelGuestTypeRelation hgtr:relationList){
			hgtr.setDelFlag("1");
		}
		//保存中间表
		for(int i=0;i<mealtype.length;i++){
			String s = mealtype[i];
			if(s!=null&&!"".equals(s)){
				HotelRoomMeal hrm = new HotelRoomMeal();
				hrm.setHotelUuid(hotelRoom.getHotelUuid());
				hrm.setHotelMealUuid(mealtype[i]);
				hrm.setHotelRoomUuid(hotelRoom.getUuid());
				hotelRoomMealService.save(hrm);
			}
		}
		//保存 hostGuestType表的UUid 保存到HostGuestTypeRelation
		for(int i=0;i<hotelGuestTypeUuids.length;i++){
			String s = hotelGuestTypeUuids[i];
			if(s!=null&&!"".equals(s)){
				HotelGuestTypeRelation typeRelation = new HotelGuestTypeRelation();
				typeRelation.setHotelGuestTypeUuid(hotelGuestTypeUuids[i]);
				typeRelation.setHotelGuestTypeName(hotelGuestTypeNames[i]);
				typeRelation.setHotelRoomUuid(hotelRoom.getUuid());
				typeRelation.setHotelUuid(hotelRoom.getHotelUuid());
				hgtRealtionService.save(typeRelation);
			}
		}
		
		hotelRoomService.updateHotelRoom(entity, occupancyRates, roomOccuRateUuids);
		datas.put("message","2");
		return datas;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)){
				hotelRoomService.removeByUuid(uuid);
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
	
	@ResponseBody
	@RequestMapping(value = "updateOrder")
	public Object updateOrder(String uuidAndSortsStr) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		Map<String, String> uuidAndSortMap = new HashMap<String, String>();
		boolean b = true;
		try {
			if(StringUtils.isEmpty(uuidAndSortsStr)) {
				
			} else {
				String[] records = uuidAndSortsStr.split(";");
				
				//组装数据
				if(records != null && records.length != 0) {
					for(String record : records) {
						String[] uuidAndSorts = record.split(",");
						uuidAndSortMap.put(uuidAndSorts[0], uuidAndSorts[1]);
					}
				}
				
				//更新酒店房型排序
				Set<String> uuids = uuidAndSortMap.keySet();
				if(uuids != null && !uuids.isEmpty()) {
					for(String uuid : uuids) {
						HotelRoom hotelRoom = hotelRoomService.getByUuid(uuid);
						hotelRoom.setSort(Integer.parseInt(uuidAndSortMap.get(uuid)));
						hotelRoomService.update(hotelRoom);
					}
				}
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "系统发生异常,请重新操作!");
			e.printStackTrace();
		}
		
		if(b){
			datas.put("result", "1");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "check")
	public String check(String hotelUuid, String uuid, HttpServletRequest request) throws IOException {
		String roomName = request.getParameter("roomName");
		if(StringUtils.isEmpty(uuid))
		{
			uuid = "0";
		}
		
		if(hotelRoomService.findIsExist(hotelUuid, uuid, roomName, UserUtils.getCompanyIdForData())) {
			return "false";
		}
		return "true";
	}
	
	@ResponseBody
	@RequestMapping(value = "saveRoomFeature")
	public Object saveRoomFeature(SysCompanyDictView sysCompanyDictView, HttpServletRequest request) {
		Map<String, Object> datas = new HashMap<String, Object>();
		try{
			sysdefinedictService.save(sysCompanyDictView, UserUtils.getCompanyIdForData());
			datas.put("result", "1");
			datas.put("uuid", sysCompanyDictView.getUuid());
			
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "0");
			datas.put("message", "系统发生异常,请重新操作!");
		}
		return datas;
	}
	
	/**
	 * 加载本公司所有可用游客信息
		* 
		* @param 
		* @return List<TravelerType>
		* @author majiancheng
		* @Time 2015-4-22
	 */
	private List<TravelerType> loadTravelerTypes() {
		TravelerType travelerType = new TravelerType();
		travelerType.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		travelerType.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
		travelerType.setStatus("1");
		List<TravelerType> travelerTypes = travelerTypeService.find(travelerType);
		return travelerTypes;
	}
	
	private List<HotelGuestType> getHotelGuestTypes(){
		HotelGuestType hotelGuestType = new HotelGuestType();
		hotelGuestType.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
		hotelGuestType.setStatus("1");
		hotelGuestType.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<HotelGuestType> list = hotelGuestTypeService.find(hotelGuestType);
		return list;
	}
	
}
