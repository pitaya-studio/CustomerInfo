/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.flight.web;

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

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.flight.entity.FlightControl;
import com.trekiz.admin.modules.flight.entity.FlightControlDetail;
import com.trekiz.admin.modules.flight.entity.FlightControlHotelDetail;
import com.trekiz.admin.modules.flight.input.FlightControlInput;
import com.trekiz.admin.modules.flight.query.FlightControlQuery;
import com.trekiz.admin.modules.flight.service.FlightControlDetailService;
import com.trekiz.admin.modules.flight.service.FlightControlHotelDetailService;
import com.trekiz.admin.modules.flight.service.FlightControlService;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelMealService;
import com.trekiz.admin.modules.hotel.service.HotelRoomService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;


/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/flightControl")
public class FlightControlController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/flight/flightcontrol/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/flightControl/list";
	protected static final String FORM_PAGE = "modules/flight/flightcontrol/form";
	protected static final String SHOW_PAGE = "modules/flight/flightcontrol/show";
	protected static final String EDIT_FOR_FLIGHT_PAGE ="modules/flight/flightcontrol/editForFlight";
	protected static final String EDIT_FOR_DATE_PAGE ="modules/flight/flightcontrol/editForDate";
	

	
	@Autowired
	private FlightControlDetailService controlDetailService;
	@Autowired
	private FlightControlService flightControlService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private FlightControlDetailService flightControlDetailService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private FlightControlHotelDetailService flightControlHotelDetailService;
	@Autowired
	private AirlineInfoService airlineInfoService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private HotelRoomService roomService;
	@Autowired
	private IslandService islandService;
	@Autowired
	private HotelMealService mealService;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	
	@RequestMapping(value = "list")
	public String list(FlightControl flightControl, HttpServletRequest request, HttpServletResponse response, Model model) {
		flightControl.setDelFlag("0");
        Page<FlightControl> page = flightControlService.find(new Page<FlightControl>(request, response), flightControl); 
        model.addAttribute("page", page);
        return LIST_PAGE;
	}
	
	
	
	/**
	 * 跳转到机票库存修改页面 by sy 
	 * @param uuid 库存单uuid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "editForFlight/{uuid}")
	public String editForFlight(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		FlightControl flightControl = flightControlService.getByUuid("de6b448458234abd8d713fb3089b164d");
		List<FlightControlDetail> DetailList = controlDetailService.getByFlightControlUuid(uuid);
		HotelAnnex ha = new HotelAnnex();
		ha.setDelFlag("0");
		ha.setMainUuid(flightControl.getUuid());
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_FLIGHT_CONTROL);
		List<HotelAnnex> haList = hotelAnnexService.find(ha);
		if(haList!=null&&haList.size()>0){
			model.addAttribute("haList", haList);
		}
		boolean editFlag = true;
		if(CollectionUtils.isNotEmpty(DetailList)){
			for(FlightControlDetail controlDetail : DetailList){
				 String flightControlDetailUuid = controlDetail.getUuid();
				 List<FlightControlHotelDetail> hotelList  = flightControlHotelDetailService.getByFlightControlDetailUuid(flightControlDetailUuid);
				 controlDetail.setFlightControlHotelDetails(hotelList) ;
				 if(controlDetail.getStatus() == FlightControlDetail.STATUS_SUBMIT_FLAG) {
						editFlag = false;
			    }
			}
		}
		flightControl.setFlightControlDetails(DetailList);
		FlightControlInput flightControlInput = new FlightControlInput();
	    flightControlInput.initFlightControlInput(flightControl);
	    flightControlInput.getFlightControlDetails();
	  	model.addAttribute("editFlag", editFlag);
	  	model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
	  	model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
	  	model.addAttribute("flightControlInput", flightControlInput);
	  	return EDIT_FOR_FLIGHT_PAGE;
	}
	
	
	@RequestMapping(value = "editForDate/{uuid}")
	public String editForDate(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		FlightControl flightControl = flightControlService.getByUuid("de6b448458234abd8d713fb3089b164d");
		List<FlightControlDetail> DetailList = controlDetailService.getByFlightControlUuid(uuid);
		HotelAnnex ha = new HotelAnnex();
		ha.setDelFlag("0");
		ha.setMainUuid(flightControl.getUuid());
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_FLIGHT_CONTROL);
		List<HotelAnnex> haList = hotelAnnexService.find(ha);
		if(haList!=null&&haList.size()>0){
			model.addAttribute("haList", haList);
		}
		boolean editFlag = true;
		if(CollectionUtils.isNotEmpty(DetailList)){
			for(FlightControlDetail controlDetail : DetailList){
				 String flightControlDetailUuid = controlDetail.getUuid();
				 List<FlightControlHotelDetail> hotelList  = flightControlHotelDetailService.getByFlightControlDetailUuid(flightControlDetailUuid);
				 controlDetail.setFlightControlHotelDetails(hotelList) ;
				 if(controlDetail.getStatus() == FlightControlDetail.STATUS_SUBMIT_FLAG) {
						editFlag = false;
			    }
			}
		}
		flightControl.setFlightControlDetails(DetailList);
		FlightControlInput flightControlInput = new FlightControlInput();
	    flightControlInput.initFlightControlInput(flightControl);
	    flightControlInput.getFlightControlDetails();
	  	model.addAttribute("editFlag", editFlag);
	  	model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
	  	model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
	  	model.addAttribute("flightControlInput", flightControlInput);
	  	return EDIT_FOR_DATE_PAGE;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "updateForFlight")
	public Object updateForFlight(FlightControl flightControl, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		Map<String,String> result = new HashMap<String, String>();
		try{
//			List<HotelAnnex>  annexList =  hotelAnnexService.getFileList(request);
			//flightControlService.updateFlightControl(flightControl, true,annexList);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
			return result;
		}
		
		return result;
	}
	@ResponseBody
	@RequestMapping(value = "updateForDate")
	public Object updateForDate(FlightControl flightControl, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		Map<String,String> result = new HashMap<String, String>();
		try{
			//List<HotelAnnex>  annexList =  hotelAnnexService.getFileList(request);
			flightControlService.update(flightControl);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
			return result;
		}
		
		return result;
	}
	

	@RequestMapping(value = "flightControlList")
	public String flightControlList(FlightControlQuery flightControlQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		String orderBy = request.getParameter("orderBy");
		String activityStatus = request.getParameter("activityStatus");
		String showType = request.getParameter("showType");// 1是日期列表，2是酒店列表		
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,
				response);
		if ("2".equals(showType)) {
			page = flightControlService.getFlightList(request, response,
					flightControlQuery);
			List<List<Map<String, String>>> flightList = flightControlService
					.getFlightListSubs(page.getList());
			if (CollectionUtils.isNotEmpty(flightList)) {
				model.addAttribute("flightList", flightList);
			}
		} else {
			page = this.flightControlDetailService.FlightControlList(flightControlQuery, request, response);
		}

		String pageStr = page.toString();
		/**使用新版的分页*/
		//String pageStr = page.toString("new style");
		pageStr = pageStr.replace("<div style=\"clear:both;\"></div>", "");
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("page", page);
		model.addAttribute("count", page.getCount());
		model.addAttribute("flightControlQuery", flightControlQuery);
		model.addAttribute("activityStatus", activityStatus);
		model.addAttribute("showType", showType);
		model.addAttribute("orderBy", orderBy);
		//add
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		model.addAttribute("traffic_namelist", activityAirTicketService.findAirlineByComid(companyId));// 航空公司
		//end
		
		if ("2".equals(showType)) {// flight
			return "modules/flight/flightcontrol/flightControlListFlight";
		} else {// date
			return "modules/flight/flightcontrol/flightContrlListDate";
		}
	}
	
	@RequestMapping(value = "form")
	public String form(FlightControl flightControl, Model model) {
		model.addAttribute("flightControl", flightControl);
		return FORM_PAGE;
	}
	
	@RequestMapping(value = "tosaveflightcontrol")
	public String toSaveFlightControl(HttpServletRequest request ,HttpServletRequest response ,Model model) {
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);//币种选择（币种表，与供应商ID关联）
		String flag = request.getParameter("flag");
		if("1".equals(flag)){
			String uuid = request.getParameter("uuid");
			FlightControl flightControl = flightControlService.getByUuid(uuid);
			String islandUuid = flightControl.getIslandUuid();
			String airlineCode = flightControl.getAirline();
			
			Map<String, String> spaceGradelist = airlineInfoService.findAirlineInfo_spaceLevelList(companyId, airlineCode);
			
			model.addAttribute("spaceGradelist", spaceGradelist);
			model.addAttribute("flag", flag);
			model.addAttribute("flightControl", flightControl);
			List<Hotel> hoteLlist = getDataByUuidAndType(islandUuid);
			model.addAttribute("hotelmap", hoteLlist);
		}
		
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("airlines_list", activityAirTicketService.findAirlineByComid(companyId));// 航空公司
		return "modules/flight/flightcontrol/tosaveflightcontrol";
	}

	
	@ResponseBody
	@RequestMapping(value = "saveflightcontrol")
	public Map<String,String> saveFlightControl(@ModelAttribute FlightControlInput flightControlInput, Model model, HttpServletRequest request) {
		Map<String,String> result = new HashMap<String, String>();
		
		String flag = request.getParameter("flag");
		try {
			if("1".equals(flag)){
				String uuid = request.getParameter("adddateuuid");
				flightControlInput.setUuid(uuid);
				result = flightControlService.saveFlightControlDate(flightControlInput);
			}else{
				result = flightControlService.saveFlightControl(flightControlInput);
			}
			
		} catch (Exception e) {
			result.put("message", "3");
			result.put("error", "系统异常，请重新操作!");
			return result;
		}
		
		return result;
	}
	
	private List<Hotel> getDataByUuidAndType(String uuid) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Hotel> hotelList = new ArrayList<Hotel>();
			Hotel hotel = new Hotel();
			if(!("").equals(uuid)){
				hotel.setDelFlag("0");
				hotel.setIslandUuid(uuid);
				hotel.setWholesalerId(companyId.intValue());
				hotelList = hotelService.find(hotel);
			}
		return hotelList;
	}
	
	/***
	 * 舱位等级ajax级联调用
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSpaceFromAirCom")
	public Map<String, Object> getSpaceFromAirCom(HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		String airlineCode = request.getParameter("airlineCode");
		Map<String, String> spaceLevel = airlineInfoService.findAirlineInfo_spaceLevelList(companyId, airlineCode);
		map.put("spaceLevel", spaceLevel);
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(FlightControl flightControl, Model model, RedirectAttributes redirectAttributes) {
		
		flightControlService.save(flightControl);
		return "1";
	}
	/**
	 * @author LiuXueLiang
	 * @param uuid
	 * @param type
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "show/{uuid}/{type}")
	public String show(@PathVariable String uuid,@PathVariable String type, HttpServletRequest request, HttpServletResponse response, Model model) {
		//初始化变量
		String fcUuid = "";//flightControl.uuid
		FlightControl fc = new FlightControl();
		List<FlightControlDetail> fcdList = new ArrayList<FlightControlDetail>();
		List<FlightControlHotelDetail> fchdList = new ArrayList<FlightControlHotelDetail>();
		//判断非空
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		//1：给fcUuid赋值  2：给fcdList赋值
		if(StringUtils.isNotBlank(type)&&type.equals("1")){//单条 flightControlDetail.uuid
			FlightControlDetail fcd = this.flightControlDetailService.getByUuid(uuid);
			fcUuid = fcd.getFlightControlUuid();//flightControl.uuid
			fcdList.add(fcd);//filghtControlDetail集合
		}else{//多条 flightControl.uuid
			fcUuid = uuid;
			fcdList = this.flightControlDetailService.getByFlightControlUuid(fcUuid);//flightControlDetail集合
		}
		//给fc赋值，并且把fcdList赋值给fc,fchdList赋值给每个fcd
		if(StringUtils.isNotBlank(fcUuid)){
			fc = flightControlService.getByUuid(fcUuid);
			for(FlightControlDetail fcd:fcdList){
				fchdList = this.flightControlHotelDetailService.findByFlightControlDetailUuid(fcd.getUuid());
				fcd.setFlightControlHotelDetails(fchdList);
			}
			fc.setFlightControlDetails(fcdList);
		}
		model.addAttribute("flightControl", fc);
		model.addAttribute("type",type);
		//下载
		HotelAnnex ha = new HotelAnnex();
		ha.setDelFlag("0");
		ha.setMainUuid(fc.getUuid());
		ha.setType(HotelAnnex.ANNEX_TYPE_FOR_HOTEL_CONTROL);
		List<HotelAnnex> haList = hotelAnnexService.find(ha);
		if(haList!=null&&haList.size()>0){
			model.addAttribute("haList", haList);
		}
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("flightControl", flightControlService.getByUuid(uuid));
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(FlightControl flightControl, Model model, RedirectAttributes redirectAttributes) {
		
		flightControlService.update(flightControl);
		
		return "2";
	}
	/*@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)){
				flightControlService.removeByUuid(uuid);
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
	}*/
	
	/**
	 * 针对status的三种状态的操作
	 * @param uuids
	 * @return
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
						flightControlDetailService.delStatusByUuid(uuid);
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
	 * 针对delfalg的状态进行处理
	 * @param uuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteFlag")
	public Object deleteFlag(String uuid) {
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)){
				flightControlDetailService.removeByUuid(uuid);
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
	 * 发布前的验证 唯一性
	 * @param request
	 * @return
	 * @throws IOException
	 */
	/*@ResponseBody
	@RequestMapping(value = "flightControlDetailRelease/{detailUuid}")
	public Object flightControlDetailRelease(@PathVariable String detailUuid){
		Map<String,Object> datas = new HashMap<String, Object>();
		if(StringUtils.isBlank(detailUuid)){
			datas.put("result", "error");
			datas.put("message", "参数传递错误！");
		}else{
			String md5Code = flightControlDetailService.getFlightControlRule(detailUuid);
			Long companyId = UserUtils.getCompanyIdForData();
			boolean isExist = !flightControlDetailService.findRuleIsExist(detailUuid, md5Code, companyId);
			if(isExist){
				try {
					flightControlDetailService.updateDetailStatus(detailUuid,FlightControlDetail.STATUS_SUBMIT_FLAG);
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
	}*/
}
