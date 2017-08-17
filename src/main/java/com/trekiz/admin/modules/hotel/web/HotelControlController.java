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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelControl;
import com.trekiz.admin.modules.hotel.entity.HotelControlDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlFlightDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoom;
import com.trekiz.admin.modules.hotel.entity.HotelRoomMeal;
import com.trekiz.admin.modules.hotel.entity.HotelRoomOccuRate;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerTypeRelation;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.input.HotelControlInput;
import com.trekiz.admin.modules.hotel.model.HotelControlDetailModel;
import com.trekiz.admin.modules.hotel.model.jsonbean.HotelMealJsonBean;
import com.trekiz.admin.modules.hotel.model.jsonbean.HotelRoomJsonBean;
import com.trekiz.admin.modules.hotel.model.jsonbean.HotelRoomMealJsonBean;
import com.trekiz.admin.modules.hotel.query.HotelControlQuery;
import com.trekiz.admin.modules.hotel.query.HotelRoomMealQuery;
import com.trekiz.admin.modules.hotel.query.HotelTravelerTypeRelationQuery;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelControlDetailService;
import com.trekiz.admin.modules.hotel.service.HotelControlFlightDetailService;
import com.trekiz.admin.modules.hotel.service.HotelControlRoomDetailService;
import com.trekiz.admin.modules.hotel.service.HotelControlService;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotel.service.HotelRoomMealService;
import com.trekiz.admin.modules.hotel.service.HotelRoomOccuRateService;
import com.trekiz.admin.modules.hotel.service.HotelRoomService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerTypeRelationService;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelControl")
public class HotelControlController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelcontrol/hotelContrlList";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelControl/hotelContrlList";
	protected static final String FORM_PAGE = "modules/hotel/hotelcontrol/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelcontrol/show";
	

	protected static final String EDIT_FOR_HOTEL_PAGE = "modules/hotel/hotelcontrol/editForHotel";
	
	@Autowired
	private HotelControlService hotelControlService;
	@Autowired
	private HotelControlDetailService hotelControlDetailService;
	@Autowired
	private HotelControlFlightDetailService hotelControlFlightDetailService;
	@Autowired
	private HotelControlRoomDetailService hotelControlRoomDetailService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private HotelRoomService roomService;
	@Autowired
	private IslandService islandService;
	@Autowired
	private HotelMealService mealService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private HotelRoomMealService hotelRoomMealService;
	@Autowired
	private HotelMealService hotelMealService;
	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private HotelTravelerTypeRelationService hotelTravelerTypeRelationService;
	@Autowired
	private HotelRoomOccuRateService hotelRoomOccuRateService;
	
	/**
	 * 控房列表方法
	 * 
	 * @param hotelControl
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author wangxv
	 */
	@RequestMapping(value = "hotelControlList")
	public String hotelControlList(HotelControlQuery hotelControlQuery,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
		String orderBy = request.getParameter("orderBy");
		String activityStatus = request.getParameter("activityStatus");// 0：已提交；1：已保存草稿；2：已删除；
		String showType = request.getParameter("showType");// 1是日期列表，2是酒店列表
		if(StringUtils.isEmpty(hotelControlQuery.getCountry())&&"GET".equals(request.getMethod())){//初始化国家选项的值为马尔代夫
			hotelControlQuery.setCountry("80415d01488c4d789494a67b638f8a37");
		}
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,
				response);
		if ("2".equals(showType)) {
			page = hotelControlService.hotelControlList(request, response,
					hotelControlQuery);
			List<List<Map<String, String>>> hotelList = hotelControlService
					.getForHotelSubs(page.getList());
			if (CollectionUtils.isNotEmpty(hotelList)) {
				model.addAttribute("hotelList", hotelList);
			}
		} else {
			page = hotelControlService.hotelControlList(request, response,
					hotelControlQuery);
			List<List<Map<String,String>>> subList = hotelControlService.getForDateSubs(page.getList());
			model.addAttribute("subList", subList);
		}

		String pageStr = page.toString();
		pageStr = pageStr.replace("<div style=\"clear:both;\"></div>", "");
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("page", page);
		model.addAttribute("count", page.getCount());
		//model.addAllAttributes(hotelControlQuery);
		model.addAttribute("activityStatus", activityStatus);
		model.addAttribute("showType", showType);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("hotelControlQuery", hotelControlQuery);


		if ("2".equals(showType)) {// hotel
			return "modules/hotel/hotelcontrol/hotelContrlListHotel";
		} else {// date
			return "modules/hotel/hotelcontrol/hotelContrlListDate";
		}
	}
	/**
	 * 新增保存控房的数据
	 * flag = 1 是控房需要 新增日期  否则是控房新增
	 * @param hotelControl
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "tosavehotelcontrol")
	public String toSaveHotelControl(HttpServletRequest request, HttpServletRequest response , Model model){
		
		String flag = request.getParameter("flag");
		if("1".equals(flag)){//flag = 1,新增控房日期        根据 uuid进行查询控房表
			String uuid = request.getParameter("uuid");
			HotelControl hotelControl = hotelControlService.getByUuid(uuid);
			model.addAttribute("hotelControl", hotelControl);
			model.addAttribute("flag", flag);
			model.addAttribute("roomList", getDataByUuidAndType(hotelControl.getHotelUuid(), "roomtype").get("roomtype"));//房型
			Map<String, Object> islandwayMap = getDataByUuidAndType(hotelControl.getIslandUuid(), "islandway");//上岛方式
			model.addAttribute("islandWayList", islandwayMap.get("listIslandWay"));
		} else {
			model.addAttribute("flag", "0");
		}
		Map<String, Object> foottypeMap = getDataByUuidAndType("foodtype","foodtype");//餐型
		model.addAttribute("hotelMeals", foottypeMap.get("hotelMeals"));

		Long companyId = UserUtils.getUser().getCompany().getId();
		
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);//币种选择（币种表，与供应商ID关联）
		model.addAttribute("currencyList", currencyList);

		//读取地接社信息集合
        model.addAttribute("supplierInfos", supplierInfoService.findSupplierInfoByCompanyId(companyId));
        
		return "modules/hotel/hotelcontrol/tosavehotelcontrol";
	}

	@ResponseBody
	@RequestMapping(value = "saveHotelControl")
	public Object saveHotelControl(@ModelAttribute HotelControlInput hotelControlInput, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request){
		
		String flag = request.getParameter("flag");
		Map<String,String> result = new HashMap<String, String>();
		try{
			List<HotelAnnex>  annexList =  hotelAnnexService.getFileList(request);
			result = hotelControlService.saveHotelControl(hotelControlInput,flag,annexList);
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
	 * 跳转到控房单酒店修改页
	 * @param uuid 控房单uuid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "editHotelControl/{editType}/{uuid}")
	public String editHotelControl(@PathVariable String editType, @PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		long companyId = UserUtils.getUser().getCompany().getId();
		HotelControl hotelControl = new HotelControl();
		List<HotelControlDetail> detailList = new ArrayList<HotelControlDetail>();
		
		//修改酒店控房所有日期
		if("editForHotel".equals(editType)) {
			hotelControl = hotelControlService.getByUuid(uuid);
			detailList = hotelControlDetailService.getByHotelControlUuid(uuid);
		//修改酒店控房单个日期
		} else if("editForDate".equals(editType)) {
			HotelControlDetail controlDetail = hotelControlDetailService.getByUuid(uuid);
			hotelControl = hotelControlService.getByUuid(controlDetail.getHotelControlUuid());
			detailList.add(controlDetail);
		}
		HotelAnnex ha = new HotelAnnex();
		ha.setDelFlag("0");
		ha.setMainUuid(hotelControl.getUuid());
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_HOTEL_CONTROL);
		List<HotelAnnex> haList = hotelAnnexService.find(ha);
		if(haList!=null&&haList.size()>0){
			model.addAttribute("haList", haList);
		}
		boolean editFlag = true;
		if(CollectionUtils.isNotEmpty(detailList)){
			for(HotelControlDetail controlDetail : detailList){
				String hotelControlDetailUuid = controlDetail.getUuid();
				List<HotelControlRoomDetail> roomList = hotelControlRoomDetailService.getByHotelControlDetailUuid(hotelControlDetailUuid);
				List<HotelControlFlightDetail> flightList = hotelControlFlightDetailService.getByHotelControlDetailUuid(hotelControlDetailUuid);
				controlDetail.setRoomList(roomList);
				controlDetail.setFlightList(flightList);
				
				if(controlDetail.getStatus() == HotelControlDetail.STATUS_SUBMIT_FLAG) {
					editFlag = false;
				}
			}
		}

		hotelControl.setDetailList(detailList);
		HotelControlInput hotelControlInput = new HotelControlInput();
		hotelControlInput.initHotelControlInput(hotelControl);
		hotelControlInput.getHotelControlDetails();
		
		//初始化控房单修改类型
		model.addAttribute("editType", editType);
		
		//配置控房单是否只读（根据控房单中是否有已提交的日期）
		model.addAttribute("editFlag", editFlag);
		//读取公司配置币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
		model.addAttribute("hotelControlInput", hotelControlInput);
		//初始化级联列表基础数据
		initCascadeListData(hotelControlInput, model);
		
		//读取地接社信息集合
        model.addAttribute("supplierInfos", supplierInfoService.findSupplierInfoByCompanyId(companyId));
        
        //读取酒店下所有房型和餐型的关联信息
        model.addAttribute("hotelRoomMeal", hotelControlService.getHotelRoomMealsData(hotelControl.getHotelUuid()));
        
        //获取控房详情中房型和餐型的对应关系
        model.addAttribute("roomMealsRel", hotelControlService.getHotelDetailsData(hotelControl.getUuid()));
		
		return EDIT_FOR_HOTEL_PAGE;
	}
	
	@ResponseBody
	@RequestMapping(value = "updateHotelControl")
	public Object updateHotelControl(@ModelAttribute HotelControlInput hotelControlInput, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		String editType = request.getParameter("editType");
	    boolean flag = true;
	    if("editForHotel".equals(editType)) {
	    	flag = true;
	    } else if("editForDate".equals(editType)) {
	    	flag = false;
	    }
		
		Map<String,String> result = new HashMap<String, String>();
		try{
		    List<HotelAnnex>  annexList =  hotelAnnexService.getFileList(request);
		    
		    
			result = hotelControlService.updateHotelControl(hotelControlInput, flag, annexList);
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
	
	
	/*@ResponseBody
	@RequestMapping(value = "updateForDate")
	public Object updateForDate(@ModelAttribute HotelControlInput hotelControlInput, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {

		Map<String,String> result = new HashMap<String, String>();
		
		try{
			List<HotelAnnex>  annexList =  hotelAnnexService.getFileList(request);
			result = hotelControlService.updateHotelControl(hotelControlInput, false,annexList);
		} catch (Exception e) {
			e.printStackTrace();
			if(!"3".equals(result.get("message"))) {
				result.put("message", "3");
				result.put("error", "系统异常，请重新操作!");
			} 
			return result;
		}
		
		return result;
	}*/
	
	/**
	 * 针对status的三种状态的操作
	 * @param uuids
	 * @return
	 * @author wangxv
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuids) {
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidsArray = uuids.split(",");
				for(String uuid :uuidsArray){
					if(StringUtils.isNotBlank(uuid)){
				      hotelControlDetailService.delStatusByUuid(uuid);
				    }
			    }
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 针对delfalg的状态进行处理(处理多个)
	 * @param uuids
	 * @author wangxv
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAllFlag")
	public Object deleteAllFlag(String uuids) {
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArr = uuids.split(",");
				for(String uuid : uuidArr) {
					hotelControlDetailService.removeByUuid(uuid);
				}
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 针对delfalg的状态进行处理
	 * @param uuid
	 * @author wangxv
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteFlag")
	public Object deleteFlag(String uuid) {
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)){
				 hotelControlDetailService.removeByUuid(uuid);
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 发布前的验证 唯一性 add by zhanghao
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "hotelControlDetailRelease/{detailUuid}")
	public Object hotelControlDetailRelease(@PathVariable String detailUuid){
		Map<String,Object> datas = new HashMap<String, Object>();
		if(StringUtils.isBlank(detailUuid)){
			datas.put("result", "error");
			datas.put("message", "参数传递错误！");
		}else{
			String md5Code = hotelControlDetailService.getHotelControlRule(detailUuid);
			Long companyId = UserUtils.getCompanyIdForData();
			boolean isExist = !hotelControlDetailService.findRuleIsExist(detailUuid, md5Code, companyId);
			if(isExist){
				try {
					hotelControlDetailService.updateDetailStatus(detailUuid,HotelControlDetail.STATUS_SUBMIT_FLAG);
					datas.put("result", "success");
				} catch (Exception e) {
					datas.put("result", "error");
					datas.put("message", "系统发生异常，请重新操作!");
				}
			}else{
				datas.put("result", "error");
				datas.put("message", "已存在发布状态的控房单明细！");
			}
		}
		return datas;
	}
	
	/**
	 * @详情
	 * @author 刘学良 
	 * @since 2015/05/19
	 */
	@RequestMapping(value = "show/{uuid}/{type}")
	public String show(@PathVariable String uuid,@PathVariable String type, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		HotelControl hotelControl =null;
		if(StringUtils.isNotBlank(type)&&type.equals("1")){//查看控房单详情
			 hotelControl = hotelControlService.getByUuid(uuid);
			List<HotelControlDetail> detailList = hotelControlDetailService.getByHotelControlUuid(uuid);
			if(CollectionUtils.isNotEmpty(detailList)){
				for(HotelControlDetail controlDetail : detailList){
					String hotelControlDetailUuid = controlDetail.getUuid();
					List<HotelControlRoomDetail> roomList = hotelControlRoomDetailService.getByHotelControlDetailUuid(hotelControlDetailUuid);
					List<HotelControlFlightDetail> flightList = hotelControlFlightDetailService.getByHotelControlDetailUuid(hotelControlDetailUuid);
					controlDetail.setRoomList(roomList);
					controlDetail.setFlightList(flightList);;
				}
			}
			hotelControl.setDetailList(detailList);
			
			model.addAttribute("hotelControl", hotelControl);
		}else{//查看控房单明细详情
			//HotelControl hotelControl = null;
			List<HotelControlDetail> detailList = new ArrayList<HotelControlDetail>();
			HotelControlDetail controlDetail = hotelControlDetailService.getByUuid(uuid);
			
			hotelControl = hotelControlService.getByUuid(controlDetail.getHotelControlUuid());

			List<HotelControlRoomDetail> roomList = hotelControlRoomDetailService.getByHotelControlDetailUuid(controlDetail.getUuid());
			List<HotelControlFlightDetail> flightList = hotelControlFlightDetailService.getByHotelControlDetailUuid(controlDetail.getUuid());

			controlDetail.setRoomList(roomList);
			controlDetail.setFlightList(flightList);
			detailList.add(controlDetail);
			
			hotelControl.setDetailList(detailList);
			model.addAttribute("hotelControl", hotelControl);
			
		}
		HotelAnnex ha = new HotelAnnex();
		ha.setDelFlag("0");
		ha.setMainUuid(hotelControl.getUuid());
		ha.setType(4);
		List<HotelAnnex> haList = hotelAnnexService.find(ha);
		if(haList!=null&&haList.size()>0){
			model.addAttribute("haList", haList);
		}
		model.addAttribute("type",type);
		return SHOW_PAGE;
	}

	/***
	 * 基础信息ajax级联调用
	 * @author  sy 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxCheck")
	public Map<String, Object> ajaxCheck(HttpServletRequest request) {
		String uuid = request.getParameter("uuid");
		String type = request.getParameter("type");
		return getDataByUuidAndType(uuid, type);
	}
	
	/***
	 * 基础信息ajax数据封装
	 * @author  majiancheng
	 * @param request
	 * @return
	 */
	private Map<String, Object> getDataByUuidAndType(String uuid, String type) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		Map<String, Object> map = new HashMap<String, Object>();
		if(type.equals("islandway")){
			if(!("").equals(uuid)){
				
				Hotel hotel = new Hotel();
				hotel.setDelFlag("0");
				hotel.setIslandUuid(uuid);
				hotel.setWholesalerId(companyId.intValue());
				
				List<Hotel> hotelList = hotelService.find(hotel);
				
			    Island island = islandService.getByUuid(uuid);
			    
			    String[] islandWayUuids = null;
			    if(island != null && StringUtils.isNotEmpty(island.getIslandWay())){
			    	islandWayUuids = island.getIslandWay().toString().split(",");
			    	List<SysCompanyDictView> listIslandWay = sysCompanyDictViewService.findByUuids(islandWayUuids);
		    	   
			    	map.put("listIslandWay", listIslandWay);
			    }
			    map.put("hotelList", hotelList);
			}
		}else if(type.equals("island")){
			if(!("").equals(uuid)){
				Island island = new Island();
		        island.setDelFlag("0");
		        island.setCountry(uuid);
		        island.setWholesalerId(companyId.intValue());
				List<Island> islandList = islandService.find(island);
				map.put("islandList", islandList);
			}
	        
		}else if(type.equals("hotel")){
			Hotel hotel = new Hotel();
			if(StringUtils.isNotEmpty(uuid)){
				hotel.setDelFlag("0");
				hotel.setIslandUuid(uuid);
				hotel.setWholesalerId(companyId.intValue());
				List<Hotel> hotelList = hotelService.find(hotel);
				map.put("hotelList", hotelList);
			}
		}else if(type.equals("roomtype")){
 			HotelRoom hotelRoom = new HotelRoom();
			if(!("").equals(uuid)){
				hotelRoom.setDelFlag("0");
				hotelRoom.setHotelUuid(uuid);
				hotelRoom.setWholesalerId(companyId.intValue());
				List<HotelRoom>  roomtype  = roomService.find(hotelRoom);
				map.put("roomtype", roomtype);
			}
			
		}else if(type.equals("foodtype")){
			
			//通过roomUuid查询出hotelMealUuid
			HotelRoomMealQuery hotelRoomMealQuery = new HotelRoomMealQuery();
			List<HotelRoomMeal> listHotelRoomMeals = null;
			if(!("").equals(uuid)){
				hotelRoomMealQuery.setDelFlag("0");
				hotelRoomMealQuery.setHotelRoomUuid(uuid);
			    listHotelRoomMeals = hotelRoomMealService.find(hotelRoomMealQuery);
			    List<String> hotelMealUuids = new ArrayList<String>();
			    if(CollectionUtils.isNotEmpty(listHotelRoomMeals)) {
			    	for(HotelRoomMeal hotelRoomMeal : listHotelRoomMeals) {
			    		hotelMealUuids.add(hotelRoomMeal.getHotelMealUuid());
			    	}
			    }
			    map.put("roomMeals", mealService.getMealListByUuids(hotelMealUuids));
			    
			}
			
		}else if(type.equals("hotelrank")){
			HotelMeal hotelMeal = new  HotelMeal(); 
			List<HotelMeal>  hotelMeals = null;
			//酒店下绑定的游客类型
			HotelTravelerTypeRelationQuery hotelTravelerTypeRelationQuery = new HotelTravelerTypeRelationQuery();
			List<HotelTravelerTypeRelation> travelerTypeRelations = null;
			if(!("").equals(uuid)){
				List<HotelControlDetailModel> delists=hotelControlService.getControlDetailsByHotelUuid(uuid);
				Integer hotelrank=hotelService.getHotelStarValByHotelUuid(uuid);
				hotelMeal.setHotelUuid(uuid);
				hotelMeal.setDelFlag("0");
				hotelMeal.setWholesalerId(companyId.intValue());
				hotelMeals  = mealService.find(hotelMeal);
				//查询酒店下绑定的游客类型
				hotelTravelerTypeRelationQuery.setHotelUuid(uuid);
				hotelTravelerTypeRelationQuery.setDelFlag("0");
				travelerTypeRelations = hotelTravelerTypeRelationService.find(hotelTravelerTypeRelationQuery);
				map.put("hotelMeals", hotelMeals);
				map.put("delists", delists);
				map.put("hotelrank", hotelrank);
				map.put("currencyList",currencyList);
				map.put("travelerTypeRelations", travelerTypeRelations);
			}
		}else if(type.equals("hotelRoom")){
			HotelRoom hotelRoom = new HotelRoom();
			if(StringUtils.isNotEmpty(uuid)){
				hotelRoom.setDelFlag("0");
				hotelRoom.setHotelUuid(uuid);
				hotelRoom.setWholesalerId(companyId.intValue());
				List<HotelRoom>  roomtype  = roomService.find(hotelRoom);
				Map<String,HotelRoomJsonBean> houseTypes = new HashMap<String,HotelRoomJsonBean>();
				setParam(houseTypes, roomtype);
				map.put("houseTypes", houseTypes);
				
				//游客类型
				List<TravelerType> list = hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(uuid);
				List<Map<String,String>> travelerTypeList = new ArrayList<Map<String,String>>();
				for(TravelerType t:list){
		        	Map<String,String> travelerMap = new HashMap<String,String>();
		        	travelerMap.put("travelerTypeText", t.getName());
		        	travelerMap.put("travelerType", t.getUuid());
		        	travelerMap.put("shortName", t.getShortName());
		        	if(t.getPersonType()!=null){
		        		travelerMap.put("personType", t.getPersonType().toString());
		        	}else{
		        		travelerMap.put("personType", "-1");
		        	}
		        	
		        	travelerTypeList.add(travelerMap);
		        }
				map.put("travelerTypes", travelerTypeList);
				
				//酒店星级
				Integer hotelrank=hotelService.getHotelStarValByHotelUuid(uuid);
				map.put("hotelrank", hotelrank);
				
				//将酒店房型转换成json数据
				if(houseTypes != null) {
					map.put("houseTypesJsonData", JSON.toJSONStringWithDateFormat(houseTypes, "yyyy-MM-dd", SerializerFeature.DisableCircularReferenceDetect));
				}
			}

		//直接根据国家级联出
		} else if("countryToHotel".equals(type)) {
			if(StringUtils.isNotEmpty(uuid)){
				List<Hotel> hotelList = hotelService.findHotelsByCompanyIdAndCountry(companyId.intValue(), uuid);
				map.put("hotelList", hotelList);
			}
		}else if("travelerTypeRelations".equals(type)){
			//酒店下绑定的游客类型
			if(StringUtils.isNotEmpty(uuid)){
				//查询酒店下绑定的游客类型
	 			List<TravelerType> travelerTypes = hotelTravelerTypeRelationService.getTravelerTypesByHotelUuid(uuid);
	 			List<HotelTravelerTypeRelation> travelerTypeRelations = new ArrayList<HotelTravelerTypeRelation>();
	 			if(CollectionUtils.isNotEmpty(travelerTypes)){
	 				for(TravelerType t : travelerTypes){
	 					HotelTravelerTypeRelation relationType = new HotelTravelerTypeRelation();
	 					relationType.setTravelerTypeUuid(t.getUuid());
	 					relationType.setTravelerTypeName(t.getName());
	 					travelerTypeRelations.add(relationType);
	 				}
	 				
	 			}
				map.put("travelerTypeRelations", travelerTypeRelations);
				map.put("currencyList",currencyList);
			}
		}
		return map;
	}
	
	/**
	 * 组装前端需要的数据结构
	 * @param houseTypes
	 * @param roomtype
	 */
	private void setParam(Map<String,HotelRoomJsonBean> houseTypes , List<HotelRoom>  roomtype){
		if(CollectionUtils.isNotEmpty(roomtype)){
			
			Map<String,HotelMealJsonBean> upMealTypes = new HashMap<String,HotelMealJsonBean>();
			List<HotelMeal> hotelMealList = hotelMealService.getMealListByUuid(roomtype.get(0).getHotelUuid());
			if(CollectionUtils.isNotEmpty(hotelMealList)){
				for(HotelMeal hm:hotelMealList){
					if(hm!=null){
						HotelMealJsonBean hmb = new HotelMealJsonBean();
						hmb.setName(hm.getMealName());
						hmb.setUuid(hm.getUuid());
						upMealTypes.put(hm.getUuid(), hmb);
					}
				}
			}
			
			for(HotelRoom room : roomtype){
				HotelRoomJsonBean hb = new HotelRoomJsonBean();
				hb.setName(room.getRoomName());
				hb.setCapacity(room.getOccupancyRate());
				
				HotelRoomOccuRate hotelRoomOccuRate = new HotelRoomOccuRate();
				hotelRoomOccuRate.setHotelRoomUuid(room.getUuid());
				hotelRoomOccuRate.setDelFlag("0");
				List<HotelRoomOccuRate> roccurateList = hotelRoomOccuRateService.find(hotelRoomOccuRate);
				if(CollectionUtils.isNotEmpty(roccurateList)){
					StringBuffer sb = new StringBuffer();
					for(HotelRoomOccuRate rocc:roccurateList){
						if(StringUtils.isNotBlank(rocc.getRemark())){
							sb.append(rocc.getRemark());
							sb.append(HotelRoomOccuRate.SPLIT_FLAG);
						}
					}
					if(sb.length()>0){
						sb.deleteCharAt(sb.lastIndexOf(HotelRoomOccuRate.SPLIT_FLAG));
					}
					hb.setCapacityNote(sb.toString());
				}
				
				Map<String,HotelRoomMealJsonBean> hrmbMap = new HashMap<String,HotelRoomMealJsonBean>();
				List<HotelMeal> rmList = hotelRoomMealService.findByHotelRoomUUid(room.getUuid());
				if(CollectionUtils.isNotEmpty(rmList)){
					for(HotelMeal hm:rmList){
						if(hm!=null){
							HotelRoomMealJsonBean hrmb = new HotelRoomMealJsonBean();
							hrmb.setName(hm.getMealName());
							hrmb.setUuid(hm.getUuid());
							hrmb.setUpMealTypes(upMealTypes);
							hrmbMap.put(hm.getUuid(), hrmb);
						}
					}
				}
				
				hb.setBaseMealTypes(hrmbMap);
				houseTypes.put(room.getUuid(), hb);
			}
		}
	}
	/**
	 * 加载级联数据
	 * @param hotelControlInput
	 * @param model
	 */
	private void initCascadeListData(HotelControlInput hotelControlInput, Model model) {
		if(hotelControlInput == null) {
			return ;
		}
		Map<String, Object> islandwayMap = getDataByUuidAndType(hotelControlInput.getIslandUuid(), "islandway");
		
		model.addAttribute("islandWayList", islandwayMap.get("listIslandWay"));//上岛方式
		model.addAttribute("hotelList", islandwayMap.get("hotelList"));//酒店名称
		model.addAttribute("islandList", getDataByUuidAndType(hotelControlInput.getCountry(), "island").get("islandList"));//岛屿名称
		model.addAttribute("roomList", getDataByUuidAndType(hotelControlInput.getHotelUuid(), "roomtype").get("roomtype"));//房型
	}
	
	
	/**
	 * 状态停用 add by hhx
	 */
	@ResponseBody
	@RequestMapping(value = "updateHotelControlDetailStatus/{detailUuid}")
	public Object updateHotelControlDetailStatus(@PathVariable String detailUuid){
		Map<String,Object> datas = new HashMap<String, Object>();
		if(StringUtils.isBlank(detailUuid)){
			datas.put("result", "error");
			datas.put("message", "参数传递错误！");
			return datas;
		}
		try{
			hotelControlDetailService.updateDetailStatus(detailUuid,HotelControlDetail.STATUS_STOP);
		}catch(Exception e){
			datas.put("result", "error");
			datas.put("message", "状态更新错误！");
			return datas;
		}
		datas.put("result", "success");
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "updateBatchStopStatus")
	public Object updateBatchStopStatus(HttpServletRequest request){
		String uuids = request.getParameter("uuids");
		String status = request.getParameter("status");
		String uuid = "";
		int intstatus = 4;
		if(status!=null && !"".equals(status)){
			intstatus = Integer.parseInt(status);
		}
		Map<String,Object> datas = new HashMap<String, Object>();
		if(StringUtils.isBlank(uuids)){
			datas.put("result", "error");
			datas.put("message", "参数传递错误！");
			return datas;
		}
		
		
		try{
			String[] ss = uuids.split(",");
			for(int i=0;i<ss.length;i++){
				uuid = ss[i];
				if(uuid!=null && !"".equals(ss[i])){
					hotelControlDetailService.updateDetailStatus(uuid,intstatus);
				}
				
			}
			
			
		}catch(Exception e){
			datas.put("result", "error");
			datas.put("message", "状态更新错误！");
			return datas;
		}
		datas.put("result", "success");
		return datas;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "getHotelRoomMealsData")
	public Map<String, Object> getHotelRoomMealsData(HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		String hotelUuid = request.getParameter("hotelUuid");
		
		data.put("hotelRoomMeal", hotelControlService.getHotelRoomMealsData(hotelUuid));
		return data;
	}
}
