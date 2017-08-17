package com.trekiz.admin.modules.airticket.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.modules.sys.entity.*;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.GroupcodeModifiedRecord;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.GroupcodeModifiedRecordDao;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.AirTicketFile;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticket.service.IFlightInfoService;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordService;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AirportInfoService;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;


/**
 * 文件名: ActityAirTicketController.java 功能: 机票控制器 :
 * 
 * @author xiaojun
 * @DateTime 2014-9-15 下午2:36:17
 * @version 1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/airTicket/")
public class ActityAirTicketController extends BaseController {

	protected static final Logger logger = LoggerFactory
			.getLogger(ActityAirTicketController.class);

	@Autowired
	private AreaService areaService;
	@Autowired
	private AirportService airportService;
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private IFlightInfoService flightInfoService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private AirlineInfoService airlineInfoService;
	@Autowired
	private AirportInfoService airportInfoService;
	@Autowired
	private DocInfoService docInfoService;
	
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private GroupcodeModifiedRecordDao  groupcodeModifiedRecordDao;
	
	@ModelAttribute("menuId")
	protected Integer getMenuId() {
		return 80;
	}

	/**
	 * 
	 * @Description:分页列表查询
	 * @param actityAirTicket
	 * @param response
	 * @param model
	 * @param request
	 * @throws NumberFormatException
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException String
	 * @exception:
	 * @author: midas
	 * @time:2014-9-19 上午10:36:00
	 */
	@RequestMapping(value = "list/{productStatus}")
	public String showairTicketList(
			@PathVariable String productStatus, 
			@ModelAttribute ActivityAirTicket airTicket,
			HttpServletResponse response, Model model,
			HttpServletRequest request) throws NumberFormatException,
			OptimisticLockHandleException, PositionOutOfBoundException {
		
		
		

 		String departureCity = request.getParameter("departureCity");//
 		if(departureCity!=null){
 			departureCity = request.getParameter("departureCity").trim();
 		}
		String arrivedCity = request.getParameter("arrivedCity");//
		if(arrivedCity!=null){
			arrivedCity = request.getParameter("arrivedCity").trim();
 		}
		String wholeSalerKey = request.getParameter("wholeSalerKey");//搜索条件-团号
		String airlines = request.getParameter("airlines");//
		String minprice = request.getParameter("minprice");//
		String maxprice = request.getParameter("maxprice");//
		String airType = request.getParameter("airType");//
		String querystartTime = request.getParameter("querystartTime");//
		String quertEndTime = request.getParameter("quertEndTime");//
		//String orderBy = request.getParameter("orderBy");
		String airTicketIds = request.getParameter("airTicketIds");
		
		airTicket.setGroupCode(wholeSalerKey);
		String departureCityPara=departureCity;
		//System.out.println("01==="+System.currentTimeMillis());
		List<Dict> from_areas=areaService.findFromCityList("");
		//System.out.println("02==="+System.currentTimeMillis());
		if(StringUtils.isNotEmpty(departureCity)){
			for(Dict dict:from_areas){
				if(departureCity.equals(dict.getLabel()))
					departureCityPara=dict.getValue().toString();
			}
		}
		String arrivedCityPara=arrivedCity;
		//System.out.println("03==="+System.currentTimeMillis());
		List<Area> areas=areaService.findAirportCityList("");
		//System.out.println("04==="+System.currentTimeMillis());
		if(StringUtils.isNotEmpty(arrivedCity)){
			if(areas.size()>0)
				for(Area area:areas){
					if(arrivedCity.equals(area.getName())){
						arrivedCityPara=area.getId().toString();
					}
				}
		}
	
		BigDecimal minpayPrice = null;
		BigDecimal maxpayPrice = null;
		if (StringUtils.isNotBlank(minprice)) {
			minpayPrice = new BigDecimal(minprice);
		}
		if (StringUtils.isNotBlank(maxprice)) {
			maxpayPrice = new BigDecimal(maxprice);
		}

		if(StringUtils.isBlank(productStatus) || "2".equals(productStatus)) {
			airTicket.setProductStatus(Integer.valueOf(Context.PRODUCT_ONLINE_STATUS));// 查询上架产品
		}else if("3".equals(productStatus)) {
			airTicket.setProductStatus(Integer.valueOf(Context.PRODUCT_OFFLINE_STATUS));// 查询下架产品
		}
		
		User user = UserUtils.getUser();
		//System.out.println("05==="+System.currentTimeMillis());
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
		//System.out.println("06==="+System.currentTimeMillis());
		
		if((!"".equals(querystartTime)&&querystartTime!=null&&!"".equals(quertEndTime)&&quertEndTime!=null)){
			 querystartTime = querystartTime + " 00:00:00";
			 quertEndTime = quertEndTime + " 23:59:59";
		}
		
		Long companyId = user.getCompany().getId();
		//System.out.println("07==="+System.currentTimeMillis());
		Page<ActivityAirTicket> page = activityAirTicketService
				.findActivityAirTicketPage(new Page<ActivityAirTicket>(request,
						response), airTicket, departureCityPara, arrivedCityPara,
						minpayPrice, maxpayPrice, airType, querystartTime,
						quertEndTime, companyId, common);
		//System.out.println("08==="+System.currentTimeMillis());

		model.addAttribute("querystartTime", querystartTime);
		model.addAttribute("quertEndTime", quertEndTime);
		model.addAttribute("page", page);
		model.addAttribute("departureCity", departureCity);
		model.addAttribute("arrivedCity", arrivedCity);
		model.addAttribute("airlines", airlines);
		model.addAttribute("minprice", minprice);
		model.addAttribute("maxprice", maxprice);
		model.addAttribute("airType", airType);
		model.addAttribute("querystartTime", querystartTime);
		model.addAttribute("quertEndTime", quertEndTime);
		model.addAttribute("airTypes", DictUtils.getSysDicMap("air_Type"));// 机票类型
	//	model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));// 航空公司
		
		//System.out.println("09==="+System.currentTimeMillis());
	    model.addAttribute("airlines_list", activityAirTicketService.findAirlineByComid(companyId));// 航空公司
		//System.out.println("10==="+System.currentTimeMillis());
		
		model.addAttribute("airTicketIds", airTicketIds);
		model.addAttribute("airTicket", airTicket);
		//System.out.println("11==="+System.currentTimeMillis());
		//model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		
		//System.out.println("12==="+System.currentTimeMillis());
		//model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("spaceGradelist", DictUtils.getDictListValueAndLableByType("spaceGrade_Type"));// 舱位等级
		//System.out.println("13==="+System.currentTimeMillis());
		
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		//System.out.println("14==="+System.currentTimeMillis());
		model.addAttribute("curlist", currencylist);
		//model.addAttribute("from_areaslist", areaService.findByCityList());// 出发城市
		//System.out.println("15==="+System.currentTimeMillis());
		model.addAttribute("from_areaslist", areaService.findFromCityList(""));// 出发城市
		//System.out.println("16==="+System.currentTimeMillis());
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		
		//System.out.println("17==="+System.currentTimeMillis());
//		model.addAttribute("trafficNames",DictUtils.findUserDict(companyId, "flight"));// 航空公司
//		model.addAttribute("fromAreas",DictUtils.findUserDict(companyId, "fromarea"));// 出发城市
		//model.addAttribute("airports", airportInfoService.fromAllAirportInfo());// 到达城市
		List<Map<String, Object>> airports = airportInfoService.fromAllAirportMapInfo();
		model.addAttribute("airports", airports);// 到达城市
		//System.out.println("18==="+System.currentTimeMillis());
		
		return "modules/airticket/airticketList";
	}

	// @RequiresPermissions("product_airticket:manager:add")
	@RequestMapping(value = "form")
	public String form(@ModelAttribute ActivityAirTicket airticket,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String recordId=request.getParameter("recordId");//询价记录
		Long companyId = StringUtils.toLong(UserUtils.getUser().getCompany().getId());
		model.addAttribute("airlines_list", activityAirTicketService.findAirlineByComid(companyId));// 航空公司
		//model.addAttribute("from_areaslist", DictUtils.getDictList("from_area"));// 出发城市
		//当前版本没有引用的变量
//		List list = areaService.findAirportCityList("");
		
		if(recordId!=null){
			EstimatePriceRecord esr = ((EstimatePriceRecordService)SpringContextHolder.getBean("estimatePriceRecordService")).findById(Long.valueOf(recordId));
			model.addAttribute("airtype",esr.getTrafficRequirements().getTrafficLineType());
		}else{
			model.addAttribute("airtype","");
		}
		
		List<Dict> ccity = areaService.findFromCityList("");
		List<Dict> listc = new ArrayList<Dict>();
		for (int i = 0; i < ccity.size(); i++) {
			if(StringUtil.isNotBlank(ccity.get(i).getLabel())){
				listc.add(ccity.get(i));
			}
		}
		
		model.addAttribute("from_areaslist", listc);//出发城市
		model.addAttribute("arrivedCitys", areaService.findAirportCityList("")); //到达城市
		
		model.addAttribute("out_areaslist",areaService.findOutCityList(""));// 离境口岸
		//model.addAttribute("arrivedareas", areaService.findByCityList());// 到达城市
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		model.addAttribute("recordId", recordId);
		//新行者团号规则 addby jiachen
		model.addAttribute("groupCodeRule",travelActivityService.findGroupCodeRule());
		model.addAttribute("companyUUID",UserUtils.getUser().getCompany().getUuid());
		return "modules/airticket/airticketForm";
	}

	@ResponseBody
	@RequestMapping(value = "getspaceLevelList")
	public void getspaceLevelList(HttpServletResponse response,
			HttpServletRequest request) {
		String airlineCode = request.getParameter("airlineCode");
		Long companyId = StringUtils.toLong(UserUtils.getUser().getCompany()
				.getId());
		JSONObject data = new JSONObject();
		data.putAll(this.airlineInfoService.findAirlineInfo_spaceLevelList(companyId, airlineCode));
		try {
			response.getWriter().print(data.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping(value = "getspaceList")
	public void getspaceList(HttpServletResponse response,
			HttpServletRequest request) {
		Long companyId = StringUtils.toLong(UserUtils.getUser().getCompany()
				.getId());
		String airlineCode = request.getParameter("airlineCode");
		JSONObject data = new JSONObject();
		String spaceLevel = request.getParameter("spaceLevel");
	    data.putAll(this.airlineInfoService.findAirlineInfo_spaceList(
				companyId, airlineCode, spaceLevel));
		try {
			response.getWriter().print(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 到添加价格页面 将数据以json串形式传入下个页面
	 * @param airticket
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "secondform")
	public String second(@ModelAttribute ActivityAirTicket airticket,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws UnsupportedEncodingException {
		String airTicketId=request.getParameter("txt_ticketId");
		if(StringUtils.isNotEmpty(airTicketId)){
			//airticket.setId(Long.valueOf(airTicketId));
			airticket=activityAirTicketService.getActivityAirTicketById(Long.valueOf(airTicketId));
//			for(FlightInfo fi:airticket.getFlightInfos()){
//				activityAirTicketService.deleteAirlineInfoById(fi.getId());
//			}
		}
		String recordId=request.getParameter("recordId");//询价记录
		model.addAttribute("recordId", recordId);
		String airType = request.getParameter("airType");//机票类型
		String sectionNumStr = request.getParameter("sectionNum");//多段时，段的数量
//		String departureCity = new String(request.getParameter("departureCity").getBytes("ISO-8859-1"),"UTF-8").trim();//出发城市
//		departureCity=departureCity.split(";")[0];
		String departureCity = request.getParameter("departureCity");
//		String arrivedCity = new String(request.getParameter("arrivedCity").getBytes("ISO-8859-1"),"UTF-8").trim();
//		arrivedCity=arrivedCity.split(";")[0];
		String arrivedCity = request.getParameter("arrivedCity");
		String reservationsNum = request.getParameter("reservationsNum");//预收人数
		String payModeDeposit = request.getParameter("payMode_deposit");//定金占位
		String payModeAdvance = request.getParameter("payMode_advance");//预占位
		String payModeFull = request.getParameter("payMode_full");//全款占位
		String payModeOp = request.getParameter("payMode_op");//计调确认占位
		String payModeCw = request.getParameter("payMode_cw");//财务确认占位
		String remainDaysDeposit = request.getParameter("remainDays_deposit");//定金保留天数
		String remainDaysDepositHour = request.getParameter("remainDays_deposit_hour");//定金保留小时数
		String remainDaysDepositFen = request.getParameter("remainDays_deposit_fen");//定金保留分钟数
		String remainDaysAdvance = request.getParameter("remainDays_advance");//预占位保留天数
		String remainDaysAdvanceHour = request.getParameter("remainDays_advance_hour");//预占位保留小时数
		String remainDaysAdvanceFen = request.getParameter("remainDays_advance_fen");//预占位保留分钟数
		String intermodalType = request.getParameter("intermodalType");//联运类型
		String intermodalInfo = request.getParameter("intermodalInfo");//联运城市币种价格
		String outArea = request.getParameter("outArea");//出境口岸
		String groupCode = "";
		//青岛凯撒，大洋，诚品旅游 懿洋假期 机票产品手动输入团号
		if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")//青岛凯撒
				|| UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")//大洋
				|| UserUtils.getUser().getCompany().getUuid().contains("ed88f3507ba0422b859e6d7e62161b00")//诚品旅游
				|| UserUtils.getUser().getCompany().getUuid().contains("f5c8969ee6b845bcbeb5c2b40bac3a23")//懿洋假期
				|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")//非常国际
				|| UserUtils.getUser().getCompany().getUuid().contains("58a27feeab3944378b266aff05b627d2")//日信观光
				|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加国际
				|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
				|| UserUtils.getUser().getCompany().getUuid().contains("049984365af44db592d1cd529f3008c3")//鼎鸿假期
				|| UserUtils.getUser().getCompany().getGroupCodeRuleJP()==0//C460 批发商配置:手动可修改
				){
			groupCode = airticket.getGroupCode();
		//名扬国际（越柬行踪）
		}else if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
			groupCode = airticket.getGroupCode();
		}else{
			groupCode = request.getParameter("groupCode");
		}
		
		String[] startingDates = request.getParameterValues("startTime");
		String[] arrivalDates = request.getParameterValues("arrivalTime");
		String[] airlines = request.getParameterValues("airlines");
		String[] spaceGrade = request.getParameterValues("spaceGrade");
		String[] airSpace = request.getParameterValues("airSpace");
		String[] leaveAirports=request.getParameterValues("leaveAirport");
		String[] desAirposts=request.getParameterValues("desAirpost");
		String[] flightNumbers=request.getParameterValues("flightNumber");

		String outTicketTime=request.getParameter("outTicketTime");//出票日期
		//String validDate = request.getParameter("limitTime");//有效期

		// 机票对象赋值
		airticket.setAirType(airType);
		airticket.setDepartureCity(departureCity);
		airticket.setArrivedCity(arrivedCity);
		airticket.setOutArea(outArea);
		airticket.setGroupCode(groupCode);
		airticket.setRecordId(StringUtils.isNotEmpty(recordId)?Long.valueOf(recordId):null);
		int resNum=StringUtils.isNotEmpty(reservationsNum)?Integer.valueOf(reservationsNum):0;
		airticket.setReservationsNum(resNum);//预收人数
		airticket.setFreePosition(resNum);//余位等于预收人数
		airticket.setPayMode_deposit(StringUtils.isNotEmpty(payModeDeposit)?Integer.valueOf(payModeDeposit):0);
		airticket.setPayMode_advance(StringUtils.isNotEmpty(payModeAdvance)?Integer.valueOf(payModeAdvance):0);
		airticket.setPayMode_full(StringUtils.isNotEmpty(payModeFull)?Integer.valueOf(payModeFull):0);
		airticket.setPayMode_op(StringUtils.isNotEmpty(payModeOp)?Integer.valueOf(payModeOp):0);
		airticket.setPayMode_cw(StringUtils.isNotEmpty(payModeCw)?Integer.valueOf(payModeCw):0);
		
		
		if (StringUtils.isNotEmpty(payModeDeposit)&&StringUtils.isNotEmpty(remainDaysDeposit)) {
			airticket.setRemainDays_deposit(Integer.valueOf(remainDaysDeposit));
		}else{
			airticket.setRemainDays_deposit(null);
		}
		if (StringUtils.isNotEmpty(payModeDeposit)&&StringUtils.isNotEmpty(remainDaysDepositHour)) {
			airticket.setRemainDays_deposit_hour(Integer.valueOf(remainDaysDepositHour));
		}else{
			airticket.setRemainDays_deposit_hour(null);
		}
		if (StringUtils.isNotEmpty(payModeDeposit)&&StringUtils.isNotEmpty(remainDaysDepositFen)) {
			airticket.setRemainDays_deposit_fen(Integer.valueOf(remainDaysDepositFen));
		}else{
			airticket.setRemainDays_deposit_fen(null);
		}
		if (StringUtils.isNotEmpty(payModeAdvance)&&StringUtils.isNotEmpty(remainDaysAdvance)) {
			airticket.setRemainDays_advance(Integer.valueOf(remainDaysAdvance));
		}else{
			airticket.setRemainDays_advance(null);
		}
		if (StringUtils.isNotEmpty(payModeAdvance)&&StringUtils.isNotEmpty(remainDaysAdvanceHour)) {
			airticket.setRemainDays_advance_hour(Integer.valueOf(remainDaysAdvanceHour));
		}else{
			airticket.setRemainDays_advance_hour(null);
		}
		if (StringUtils.isNotEmpty(payModeAdvance)&&StringUtils.isNotEmpty(remainDaysAdvanceFen)) {
			airticket.setRemainDays_advance_fen(Integer.valueOf(remainDaysAdvanceFen));
		}else{
			airticket.setRemainDays_advance_fen(null);
		}
		if (StringUtils.isNotEmpty(outTicketTime)) {//出票日期
			airticket.setOutTicketTime(DateUtils.dateFormat(outTicketTime, "yyyy-MM-dd"));
		}

		//选择联运
		List<IntermodalStrategy> intermodalStrategys=null;
		if(!"0".equals(intermodalType)){//联运
			if(StringUtils.isNotEmpty(intermodalInfo)){
				String[] infos=intermodalInfo.split("#");
				if(infos.length==3){
					intermodalStrategys=new ArrayList<IntermodalStrategy>();
					IntermodalStrategy intermodalStrategy=null;
					Currency priceCurrency=null;
					String[] css=infos[0].split(",");//城市
					String[] bzs=infos[1].split(",");//币种
					String[] jes=infos[2].split(",");//金额
					for(int i=0;i<css.length;i++){
						intermodalStrategy=new IntermodalStrategy();
						priceCurrency=currencyService.findCurrency(bzs[i]!=null?Long.valueOf(bzs[i]):0l);
						intermodalStrategy.setGroupPart(css[i]);
						intermodalStrategy.setPriceCurrency(priceCurrency);
						intermodalStrategy.setPrice(new BigDecimal(StringUtils.toDouble(jes[i])));
						intermodalStrategy.setType(Integer.parseInt(intermodalType));//设置联运表的联运类型 2015年1月22日15:18:41
						intermodalStrategys.add(intermodalStrategy);
					}
					airticket.setIntermodalStrategies(intermodalStrategys);
				}
			}
		}else{
			airticket.setIntermodalStrategies(null);
		}
		airticket.setIntermodalType(Integer.valueOf(intermodalType));
		airticket.setProCompany(UserUtils.getUser().getCompany().getId());
		String flightArea = request.getParameter("flightArea");//航线类型
		List<FlightInfo> fiList = new ArrayList<FlightInfo>();
		
		int sectionNum=Integer.valueOf(sectionNumStr);
		//Map<String,String> tranameMap=DictUtils.getLabelDesMap(Context.TRAFFIC_NAME);
		List<FlightInfo> list = flightInfoService.findByFlightInfoByAirTicketId(airticket.getId());
			for (int i = 0; i < sectionNum; i++) {
				FlightInfo flightInfo = new FlightInfo();
				if(list.size()>0&&list.size()>=sectionNum){
					FlightInfo flightInfo2 =list.get(i);
					flightInfo.setTaxamt(flightInfo2.getTaxamt());
					flightInfo.setSettlementAdultPrice(flightInfo2.getSettlementAdultPrice());
					flightInfo.setSettlementcChildPrice(flightInfo2.getSettlementcChildPrice());
					flightInfo.setSettlementSpecialPrice(flightInfo2.getSettlementSpecialPrice());
					flightInfo.setRemark(flightInfo2.getRemark());
					flightInfo.setCurrency_id(flightInfo2.getCurrency_id());
				}

				flightInfo.setAirticketId(airticket.getId());
				String leaveAirport=leaveAirports[i].indexOf(Context.AIR_PREFIX)==0?leaveAirports[i].substring(Context.AIR_PREFIX.length()):null;
				String desAirpost=desAirposts[i].indexOf(Context.AIR_PREFIX)==0?desAirposts[i].substring(Context.AIR_PREFIX.length()):null;
				flightInfo.setLeaveAirport(leaveAirport);
				flightInfo.setDestinationAirpost(desAirpost);//到达机场
				flightInfo.setAirlines(airlines[i]);
				flightInfo.setSpaceGrade(spaceGrade!=null?spaceGrade[i]:null);
				flightInfo.setFlightNumber(flightNumbers!=null?flightNumbers[i]:null);
				flightInfo.setAirspace(airSpace!=null?airSpace[i]:null);
				flightInfo.setStartTime(DateUtils.parseDate(startingDates[i]));
				flightInfo.setArrivalTime(DateUtils.parseDate(arrivalDates[i]));
				flightInfo.setNumber(i + 1);

				if("1".equals(airType)){
					flightArea = request.getParameter("flightArea"+(i+1));//航线类型
				}
				flightInfo.setTicket_area_type(StringUtils.isNotEmpty(flightArea)?Integer.valueOf(flightArea):0);
				//flightInfo.setActivityAirTicket(airticket);
				//flightInfo = flightInfoService.save(flightInfo);
				fiList.add(flightInfo);

			}

		
		airticket.setFlightInfos(fiList);
		
		//airticket = this.activityAirTicketService.flushSave(airticket);
		
		/**设置显示参数*/
		Map<String,Object> paraMap;//存放参数
		Map<String,String> tranameMap=DictUtils.getLabelDesMap(Context.TRAFFIC_NAME);
		for (FlightInfo flightInfo:airticket.getFlightInfos()) {
			paraMap=new HashMap<String,Object>();//存放参数
			paraMap.put("airLineName", tranameMap.get(flightInfo.getAirlines()));
			if(StringUtils.isNotEmpty(flightInfo.getLeaveAirport())){
				AirportInfo airport=airportInfoService.getAirportInfo(Long.valueOf(flightInfo.getLeaveAirport()));
				paraMap.put("leaveAirport", airport!=null?airport.getAirportName():"");
			}
			if(StringUtils.isNotEmpty(flightInfo.getDestinationAirpost())){
				AirportInfo airport=airportInfoService.getAirportInfo(Long.valueOf(flightInfo.getDestinationAirpost()));
				paraMap.put("destinationAirpost", airport!=null?airport.getAirportName():"");
			}
			Currency currency=currencyService.findCurrency(flightInfo.getCurrency_id());
			paraMap.put("currencyName", currency.getCurrencyName());
			flightInfo.setParaMap(paraMap);
		}
		
		//参数设置币种
		Map<String,String> map=new HashMap<String,String>();
		if(StringUtils.isNotEmpty(airticket.getArrivedCity())){
			Area area=areaService.get(Long.valueOf(airticket.getArrivedCity()));
			map.put("arrivedCityName", area==null?"":area.getName());
		}
		if(StringUtils.isNotEmpty(airticket.getDepartureCity().toString())){
			Area area=areaService.get(Long.valueOf(airticket.getDepartureCity()));
			map.put("departureCityName", area==null?"":area.getName());
		}
		Currency currency=currencyService.findCurrency(airticket.getCurrency_id());
		map.put("currencyName", currency.getCurrencyName());
		if("0".equals(intermodalType)){
			map.put("intermodalType", "无联运");
		}
		else if("1".equals(intermodalType)){
			map.put("intermodalType", "全国联运");
		}else{
			map.put("intermodalType", "分区联运");
		}
		airticket.setParaMap(map);
		
		//airTicketFiles
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"airTicketFiles","createBy","updateBy"});
		JSONObject jsonObj = JSONObject.fromObject(airticket,config);
		
		model.addAttribute("traffic_names", DictUtils.getSysDicMap("traffic_name"));// 航空公司
		model.addAttribute("airspaceTypes", DictUtils.getSysDicMap("airspace_Type"));// 舱位
		model.addAttribute("spaceGradeTypes", DictUtils.getSysDicMap("spaceGrade_Type"));// 舱位等级
		model.addAttribute("intermodalStrategys", intermodalStrategys);
		model.addAttribute("from_areas", areaService.findFromCityList(""));// 出发城市
		model.addAttribute("arrivedCitys", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("out_areas", areaService.findOutCityList(""));// 离境口岸
		model.addAttribute("airticket", airticket);
		model.addAttribute("jsonObj", jsonObj);
		//List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		Long companyId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("curlist", currencyService.findCurrencyList(companyId));
		model.addAttribute("recordId", recordId);
		model.addAttribute("companyId", UserUtils.getUser().getCompany().getId());
		//获取占位人数
		Map<String, Object> counts = activityAirTicketService.findByGoupid(airticket.getId(),null);
		int orderPersonNumChild = 0;
		if(counts != null){
			orderPersonNumChild = counts.get("orderPersonNumChild")==null?0:new Integer(counts.get("orderPersonNumChild").toString());
		}
		int orderPersonNumSpecial = 0;
		if(counts != null){
			orderPersonNumSpecial = counts.get("orderPersonNumSpecial")==null?0:new Integer(counts.get("orderPersonNumSpecial").toString());
		}
		model.addAttribute("orderPersonNumChild", orderPersonNumChild);
		model.addAttribute("orderPersonNumSpecial", orderPersonNumSpecial);

		return "modules/airticket/airticketEditPrice";
	}

	/**到上面页面，将数据传入以json串传入下个页面*/
	@SuppressWarnings("static-access")
    @RequestMapping(value = "lastform")
	public String lastform(@ModelAttribute ActivityAirTicket airticket,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
//		String ticketId = request.getParameter("txt_ticketId");//机票类型
//		Long id=ticketId!=null?Long.valueOf(ticketId):0;
//		ActivityAirTicket airticket=activityAirTicketService.getActivityAirTicketById(id);
//		if(airticket==null) return "list";
		String recordId=request.getParameter("recordId");//询价记录
		model.addAttribute("recordId", recordId);
		String jsonObj=request.getParameter("txt_jsonObj");
		JSONObject obj = new JSONObject().fromObject(jsonObj);//将json字符串转换为json对象
		@SuppressWarnings("rawtypes")
        Map <String,Class> mymap = new HashMap<String,Class>();
		mymap.put("flightInfos",FlightInfo.class);
		airticket = (ActivityAirTicket)JSONObject.toBean(obj,ActivityAirTicket.class,mymap);
		
		String currency_id=request.getParameter("currency_id");
		String istax=request.getParameter("istax");
		String taxamt=request.getParameter("taxamt");//税费
		String settlementAdultPrice=request.getParameter("settlementAdultPrice");//成人成本价
		String settlementcChildPrice=request.getParameter("settlementcChildPrice");//儿童成本价
		String maxChildrenCount = request.getParameter("maxChildrenCount");
		String maxPeopleCount = request.getParameter("maxPeopleCount");
		String settlementSpecialPrice=request.getParameter("settlementSpecialPrice");//特殊人群成本价
		//*0258需求,发票税:针对懿洋假期-tgy-s*//
		String invoiceTax=request.getParameter("invoiceTax");//发票税
		//*258需求,发票税:针对懿洋假期-tgy-e*//
		String remarks=request.getParameter("airticket.remark");//备注
		String remark=request.getParameter("remark");//特殊人群备注
		String depositamt=request.getParameter("depositamt");//定金
		String depositTime=request.getParameter("depositTime");//订金时限
		String cancelTimeLimit=request.getParameter("cancelTimeLimit");//取消时限
		String payableDateStr = request.getParameter("payableDate");//应付账期
		if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")
				|| UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")
				|| UserUtils.getUser().getCompany().getUuid().contains("ed88f3507ba0422b859e6d7e62161b00")
				|| UserUtils.getUser().getCompany().getUuid().contains("f5c8969ee6b845bcbeb5c2b40bac3a23")
				|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")
				|| UserUtils.getUser().getCompany().getUuid().contains("58a27feeab3944378b266aff05b627d2")//日信观光
				|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加国际
				|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
				|| UserUtils.getUser().getCompany().getGroupCodeRuleJP()==0 //C460 批发商配置:手动可修改
				){
			String groupCode = request.getParameter("groupCode");
			airticket.setGroupCode(groupCode);
		}
		
		if(StringUtils.isNotEmpty(currency_id))
			airticket.setCurrency_id(Long.valueOf(currency_id));
		
		airticket.setIstax(StringUtils.isEmpty(istax)?0:1);
		if(StringUtils.isNotEmpty(istax))
			airticket.setTaxamt(new BigDecimal(StringUtils.toDouble(taxamt)));
		airticket.setSettlementAdultPrice(new BigDecimal(StringUtils.toDouble(settlementAdultPrice)));
		airticket.setSettlementcChildPrice(new BigDecimal(StringUtils.toDouble(settlementcChildPrice)));
		if (StringUtils.isBlank(maxChildrenCount)) {
			airticket.setMaxChildrenCount(null);
		} else {
			airticket.setMaxChildrenCount(Integer.valueOf(maxChildrenCount));
		}
		if (StringUtils.isBlank(maxPeopleCount)) {
			airticket.setMaxPeopleCount(null);
		} else {
			airticket.setMaxPeopleCount(Integer.valueOf(maxPeopleCount));
		}
		airticket.setSettlementSpecialPrice(new BigDecimal(StringUtils.toDouble(settlementSpecialPrice)));
		//*0258需求,发票税:针对懿洋假期-tgy-s*//
		airticket.setInvoiceTax(new BigDecimal(StringUtils.toDouble(invoiceTax)));
		//*258需求,发票税:针对懿洋假期-tgy-e*//
		airticket.setDepositamt(new BigDecimal(StringUtils.toDouble(depositamt)));
		airticket.setSpecialremark(remark);
		if (StringUtils.isNotBlank(depositTime)) {
			airticket.setDepositTime(DateUtils.dateFormat(depositTime, "yyyy-MM-dd"));
		}
		if (StringUtils.isNotBlank(cancelTimeLimit)) {
			airticket.setCancelTimeLimit(DateUtils.dateFormat(cancelTimeLimit, "yyyy-MM-dd"));
		}
		if(StringUtils.isNotBlank(payableDateStr)){
			airticket.setPayableDate(DateUtils.dateFormat(payableDateStr, "yyyy-MM-dd"));
		}
		//if (StringUtils.isNotBlank(remarks)) {
			airticket.setRemark(remarks);
		//}
		Boolean isSection = "1".equals(request.getParameter("flyDivInput"))?true:false;//是否分段计价
		airticket.setIsSection(isSection?1:0);
		//分段添加价格
		String[] moreCurrencyIds = request.getParameterValues("more_currency_id");
		String moreIstaxs = request.getParameter("txt_istax");
		String[] more_taxamts = request.getParameterValues("more_taxamt");
		String[] moreSettlementAdultPrices = request.getParameterValues("more_settlementAdultPrice");
		String[] moreSettlementcChildPrices = request.getParameterValues("more_settlementcChildPrice");
		String[] moreSettlementSpecialPrices = request.getParameterValues("more_settlementSpecialPrice");
		String[] moreRemarks=request.getParameterValues("more_remark");
		
		String[] moreIstaxsArray=null;
		if(isSection){
			moreIstaxsArray=moreIstaxs.split(",");
		}
		Map<String,String> tranameMap=DictUtils.getLabelDesMap(Context.TRAFFIC_NAME);//航空公司
		Map<String,Object> paraMap;
		for (int i=0;i< airticket.getFlightInfos().size();i++) {
			paraMap=new HashMap<String,Object>();//存放参数
			FlightInfo flightInfo =airticket.getFlightInfos().get(i);
			
			if(isSection){
				if(StringUtils.isNotEmpty(moreCurrencyIds[i]))
					flightInfo.setCurrency_id(Long.valueOf(moreCurrencyIds[i]));
				flightInfo.setIstax(0);
				if(StringUtils.isNotEmpty(moreIstaxsArray[i])&&!"-1".equals(moreIstaxsArray[i])){
					flightInfo.setIstax(1);
					flightInfo.setTaxamt(BigDecimal.valueOf(StringUtils.toDouble(more_taxamts[i])));
				}
				flightInfo.setSettlementAdultPrice(BigDecimal.valueOf(StringUtils.toDouble(moreSettlementAdultPrices[i])));
				flightInfo.setSettlementcChildPrice(BigDecimal.valueOf(StringUtils.toDouble(moreSettlementcChildPrices[i])));
				flightInfo.setSettlementSpecialPrice(BigDecimal.valueOf(StringUtils.toDouble(moreSettlementSpecialPrices[i])));
				flightInfo.setRemark(moreRemarks[i]);
			}
			//特殊字段解释项
			paraMap.put("airLineName", tranameMap.get(flightInfo.getAirlines()));
			if(StringUtils.isNotEmpty(flightInfo.getLeaveAirport())){
				AirportInfo airport=airportInfoService.getAirportInfo(Long.valueOf(flightInfo.getLeaveAirport()));
				paraMap.put("leaveAirport", airport!=null?airport.getAirportName():"");
			}
			if(StringUtils.isNotEmpty(flightInfo.getDestinationAirpost())){
				AirportInfo airport=airportInfoService.getAirportInfo(Long.valueOf(flightInfo.getDestinationAirpost()));
				paraMap.put("destinationAirpost", airport!=null?airport.getAirportName():"");
			}
			Currency currency=currencyService.findCurrency(flightInfo.getCurrency_id());
			paraMap.put("currencyName", currency.getCurrencyName());
			flightInfo.setParaMap(paraMap);
		}
		//
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerDefaultValueProcessor(Integer.class,
				new DefaultValueProcessor() {
					public Object getDefaultValue(Class type) {
						return null;
					}
				});
		
		JSONObject jsonObj1 = JSONObject.fromObject(airticket,jsonConfig);
		
		
//		Gson gson = new Gson();
//        String jsonObj1 = gson.toJson(airticket);
		
		
		Map<String,String> map=new HashMap<String,String>();
		if(!org.springframework.util.StringUtils.isEmpty(airticket.getArrivedCity())){
			Area area=areaService.get(Long.valueOf(airticket.getArrivedCity()));
			map.put("arrivedCityName", area.getName());
		}
		if(!org.springframework.util.StringUtils.isEmpty(airticket.getCurrency_id())){
			Currency currency=currencyService.findCurrency(airticket.getCurrency_id());
			map.put("currencyName", currency.getCurrencyName());
		}
		airticket.setParaMap(map);
		
		List<Map<String, String>> airTicketDocs=null;
		if(airticket.getId()!=null){
			airTicketDocs=activityAirTicketService.getDocsByAirTicketId(airticket.getId());
//			activityAirTicketService.updateEntity(airticket);
		}
		//List<Map<String, String>> deptList = activityAirTicketService.getDeptList(UserUtils.getUser().getId());
		
		//大洋87   如为修改 需要向前端保存   oldGroupCode    c451,c453
		List<GroupcodeModifiedRecord> groupcodeModifiedRecords  = null;
		if (null!=airticket.getId()&&0!=airticket.getId()) {
			if ("7a8177e377a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())
					|| "7a81a03577a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())
					|| "ed88f3507ba0422b859e6d7e62161b00".equals(UserUtils.getUser().getCompany().getUuid())
					|| "f5c8969ee6b845bcbeb5c2b40bac3a23".equals(UserUtils.getUser().getCompany().getUuid())
					|| "1d4462b514a84ee2893c551a355a82d2".equals(UserUtils.getUser().getCompany().getUuid())
					|| "58a27feeab3944378b266aff05b627d2".equals(UserUtils.getUser().getCompany().getUuid())
					|| "7a81c5d777a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())
					|| "5c05dfc65cd24c239cd1528e03965021".equals(UserUtils.getUser().getCompany().getUuid())
					|| UserUtils.getUser().getCompany().getGroupCodeRuleJP()==0 //C460 批发商配置:手动可修改
					) {
			    ActivityAirTicket activityAirTicketold = activityAirTicketService.findById(airticket.getId());
			    model.addAttribute("oldGroupCode", activityAirTicketold.getGroupCode());
				groupcodeModifiedRecords = groupcodeModifiedRecordDao.findGroupcodeModifiedRecordByProductIdAndType(airticket.getId().intValue(), new Integer(7));
				model.addAttribute("groupcodeModifiedRecords", groupcodeModifiedRecords);
			}
			
		}
		
		//获取审核业务 部门列表
		List<Long> deptList = UserUtils.getDepartmentByJobNew();
		Map<String,String> deptMap = new HashMap<String,String>();
		if(CollectionUtils.isNotEmpty(deptList)){
			deptMap.put("dept_id", deptList.get(0).toString());//默认显示第一个部门
			deptMap.put("deptName", departmentService.findById(deptList.get(0)).getName());//获取部门名称
		}
		//Set<Department> deptSet = UserUtils.getDepartmentByJob();//2015-12-18之前版本
		//Set<Department> deptSet = UserUtils.getUserDepartment();
		
		Date cDate = airticket.getCreateDate();
		if(cDate != null){//修改产品
			if(airticket.getDeptId()!=0){//旧数据存在部门id为0的情况
				deptMap.put("dept_id", airticket.getDeptId().toString());
				if(null != airticket.getDeptId()) {
					Department department = departmentService.findById(airticket.getDeptId());
					if(null != department) {
						deptMap.put("deptName", departmentService.findById(airticket.getDeptId()).getName());
					}
				}
			}
			model.addAttribute("deptMap", deptMap);
			model.addAttribute("ismodify","Y");
		}else{
			//model.addAttribute("deptMap", CollectionUtils.isNotEmpty(deptList) ? deptMap : "");
			model.addAttribute("deptMap", deptMap);
			model.addAttribute("ismodify","N");//发布产品
		}
		model.addAttribute("deptId",UserUtils.getUser().getCompany().getId());
		model.addAttribute("jsonObj", jsonObj1);
		model.addAttribute("airticket", airticket);
		model.addAttribute("airTicketDocs", airTicketDocs);
		model.addAttribute("from_areas", areaService.findFromCityList(""));// 出发城市
		model.addAttribute("arrivedCitys", areaService.findAirportCityList(""));// 到达城市
		//获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-start-------------------------//
		model.addAttribute("is_need_groupCode", UserUtils.getUser().getCompany().getIsNeedGroupCode());
		//获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-end---------------------------//
		return "modules/airticket/airticketUploadFile";
	}

	/**详情页*/
	@RequestMapping(value = "actityAirTickettail/{airticketId}")
	public String ActityAirTickettail(@PathVariable String airticketId,
			Model model, HttpServletRequest request) {

		ActivityAirTicket airticket = this.activityAirTicketService
				.getActivityAirTicketById(new Long(airticketId));

		List<FlightInfo> flistinfolist = this.flightInfoService
				.findByFlightInfoByAirTicketId(new Long(airticketId));
		
		Map<String,String> tranameMap=DictUtils.getLabelDesMap(Context.TRAFFIC_NAME);//航空公司
		Map<String,Object> paraMap;
		for (int i=0;i< airticket.getFlightInfos().size();i++) {
			paraMap=new HashMap<String,Object>();//存放参数
			FlightInfo flightInfo =airticket.getFlightInfos().get(i);
			//特殊字段解释项
			paraMap.put("airLineName", tranameMap.get(flightInfo.getAirlines()));
			if(StringUtils.isNotEmpty(flightInfo.getLeaveAirport())){
				AirportInfo airport=airportInfoService.getAirportInfo(Long.valueOf(flightInfo.getLeaveAirport()));
				paraMap.put("leaveAirport", airport!=null?airport.getAirportName():"");
			}
			if(StringUtils.isNotEmpty(flightInfo.getDestinationAirpost())){
				AirportInfo airport=airportInfoService.getAirportInfo(Long.valueOf(flightInfo.getDestinationAirpost()));
				paraMap.put("destinationAirpost", airport!=null?airport.getAirportName():"");
			}
			Currency currency=currencyService.findCurrency(flightInfo.getCurrency_id());
			paraMap.put("currencyName", currency.getCurrencyName());
			flightInfo.setParaMap(paraMap);
		}
		
		
		Map<String,String> map=new HashMap<String,String>();
		if(!org.springframework.util.StringUtils.isEmpty(airticket.getArrivedCity())){
			Area area=areaService.get(Long.valueOf(airticket.getArrivedCity()));
			map.put("arrivedCityName", area.getName());
		}
		if(!org.springframework.util.StringUtils.isEmpty(airticket.getCurrency_id())){
			Currency currency=currencyService.findCurrency(airticket.getCurrency_id());
			map.put("currencyName", currency.getCurrencyName());
		}
		if(airticket.getIntermodalType()==0){
			map.put("intermodalType", "无联运");
		}
		else if(airticket.getIntermodalType()==1){
			map.put("intermodalType", "全国联运");
		}else{
			map.put("intermodalType", "分区联运");
		}
		airticket.setParaMap(map);
		model.addAttribute("airticket", airticket);
		model.addAttribute("flistinfolist", flistinfolist);
		if (airticket.getCurrency_id() != null) {
			Currency currency = this.currencyService.findCurrency(new Long(
					airticket.getCurrency_id()));
			model.addAttribute("currency", currency);
		}
		model.addAttribute("airspaceTypes", DictUtils
				.getSysDicMap("airspace_Type"));// 舱位
		model.addAttribute("spaceGradeTypes", DictUtils
				.getSysDicMap("spaceGrade_Type"));// 舱位等级
		model.addAttribute("from_areas", areaService.findFromCityList(""));// 出发城市
		model.addAttribute("arrivedCitys", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("out_areas", areaService.findOutCityList(""));//离境口岸
		List<Currency> currencylist = currencyService
				.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		model.addAttribute("traffic_namelist", DictUtils
				.getDictList("traffic_name"));// 航空公司
		
		//c451,c453 大洋 机票 详情页显示团号修改过程   
		List<GroupcodeModifiedRecord> groupcodeModifiedRecords  = null;
		groupcodeModifiedRecords = groupcodeModifiedRecordDao.findGroupcodeModifiedRecordByProductIdAndType(airticket.getId().intValue(), new Integer(7));
		model.addAttribute("groupcodeModifiedRecords", groupcodeModifiedRecords);
		//获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-start-------------------------//
		model.addAttribute("is_need_groupCode", UserUtils.getUser().getCompany().getIsNeedGroupCode());
	   //获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-end---------------------------//
		
		//部门    处理 bug 12974
		String deptName = departmentService.findById(airticket.getDeptId())==null?"":departmentService.findById(airticket.getDeptId()).getName();
		model.addAttribute("deptName",deptName);
		
		
		
		if (airticket.getAirType().equals(Context.AIRTICLKET_TYPE_WANGFAN)) {
			return "modules/airticket/airticketDetail";

		} else if (airticket.getAirType().equals(
				Context.AIRTICLKET_TYPE_DUODUAN)) {
			return "modules/airticket/airticketDetailMore";

		} else { // 默认单程
			return "modules/airticket/airticketDetailone";
		}
	}

	// @RequiresPermissions("product_airticket:manager:delete")
	@RequestMapping(value = "delAirticket/{airticketId}")
	public String del(@ModelAttribute ActivityAirTicket airticket,
			@PathVariable String airticketId, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {

		List<Long> idlist = new ArrayList<Long>();

		if (StringUtils.isNotBlank(airticketId)) {
			idlist.add(Long.parseLong(airticketId));
			ActivityAirTicket ticket = activityAirTicketService.findById(Long.parseLong(airticketId));
			String content = "删除机票产品，产品编号为:"+ ticket.getProductCode();
			logOpeService.saveLogOperate(Context.log_type_product,
					Context.log_type_product_name, content, "3", Context.ProductType.PRODUCT_AIR_TICKET, Long.parseLong(airticketId));
			this.activityAirTicketService.batchDelActivityAirTicket(idlist);
		}

		model.addAttribute("airticket", airticket);
		return "forward:" + Global.getAdminPath() + "/airTicket/list/2";
	}

	/**
	 * 机场选择 机场ID必须以air为前缀拼接id，以区别到达城市
	 * @param extId
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "filterAirportInfoData")
	public List<Map<String, Object>> filterAirportInfoData(
			@RequestParam(required = false) Long extId,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		//当前版本没有引用的变量
//		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
//		List<Long> childAreaIds = Lists.newArrayList();
//		List<Area> targetAreas = Lists.newArrayList();
		
		List<AirportInfo> targetAirportInfos = Lists.newArrayList();
		// 批发商ID
//		Long companyId = UserUtils.getUser().getCompany().getId();
//		targetAreaIds = travelActivityService.findAreaIds(companyId);
//		if (targetAreaIds != null && targetAreaIds.size() != 0) {
//			for (Map<String, Object> map : targetAreaIds) {
//				childAreaIds.add(Long.parseLong(String.valueOf(map.get("id"))));
//			}
//		}
//		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		//取得机场列表
		targetAirportInfos=this.airportInfoService.queryAirport(UserUtils.getUser().getCompany().getId());
		Map<String, Object> map = null;
		for(AirportInfo airportInfo:targetAirportInfos){
				map = Maps.newHashMap();
				map.put("id", Context.AIR_PREFIX+airportInfo.getId());//机场ID前缀以air开头，以区别到达城市
				//map.put("pId", e.getId());
				map.put("name", airportInfo.getAirportName());
				mapList.add(map);
	 	 }
		//areaService.appendParentArea(childAreaIds, areaIds, targetAreas);
		// 判断是否有自定义目标区域
//		if (mapList == null || mapList.size() == 0) {
//			mapList = Lists.newArrayList();
//			// 目的地
//			Map<String, Object> map = null;
//			for (int i = 0; i < targetAreas.size(); i++) {
//				Area e = targetAreas.get(i);
//				
//				if (extId == null
//						|| (extId != null && !extId.equals(e.getId()) && e
//								.getParentIds().indexOf("," + extId + ",") == -1)) {
//					map = Maps.newHashMap();
//					map.put("id", e.getId());
//					map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
//					
//					if(e.getChildList()!=null){
//					    map.put("name", e.getName());
//					}
//					mapList.add(map);
//					
//					for(AirportInfo airportInfo:targetAirportInfos){
//						if(Long.valueOf(airportInfo.getArea()).equals(e.getId())){
//							map = Maps.newHashMap();
//							map.put("id", Context.AIR_PREFIX+airportInfo.getId());//机场ID前缀以air开头，以区别到达城市
//							map.put("pId", e.getId());
//							map.put("name", airportInfo.getAirportName());
//							mapList.add(map);
//						}
//				 	 }
//				}
//			}
//		}
		return mapList;
	}


	/**
	 * 批量删除产品
	 * @throws Exception
	 */
	// @RequiresPermissions("product_airticket:manager:delete")
	@RequestMapping(value = "batchdelAirticket/{airTicketIds}")
	public String batchdel(@PathVariable String airTicketIds,
			@ModelAttribute ActivityAirTicket airticket,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		List<Long> idlist = new ArrayList<Long>();
		if (StringUtils.isNotBlank(airTicketIds)) {
			String ids[] = airTicketIds.split(",");
			for (String id : ids) {
				if (StringUtils.isNotBlank(id))
			    airticket = 	activityAirTicketService.findById(Long.parseLong(id));
				String content = "删除机票产品，产品编号为:"+ airticket.getProductCode();
				logOpeService.saveLogOperate(Context.log_type_product,
						Context.log_type_product_name, content, "3", Context.ProductType.PRODUCT_AIR_TICKET, Long.parseLong(id));
					idlist.add(Long.parseLong(id));
			}
			if (idlist.size() != 0)
				this.activityAirTicketService.batchDelActivityAirTicket(idlist);
		}

		model.addAttribute("airticket", airticket);
		return "redirect:" + Global.getAdminPath() + "/airTicket/list/2";
	}
	
	/**
	 * 批量上下架产品
	 * @throws Exception
	 */
	@RequestMapping(value = "batchOffAirtickets/{nowStatus}/{airTicketIds}")
	public String batchOff(@PathVariable String nowStatus,
			@PathVariable String airTicketIds,
			@ModelAttribute ActivityAirTicket airticket,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		List<Long> idlist = new ArrayList<Long>();
		if (StringUtils.isNotBlank(airTicketIds)) {
			String ids[] = airTicketIds.split(",");
			for (String id : ids) {
				if (StringUtils.isNotBlank(id)) {
					idlist.add(Long.parseLong(id));
				}
			}
			if (idlist.size() != 0) {
				if("2".equals(nowStatus)) {
					this.activityAirTicketService.batchOnOrOffActivityAirTicket(idlist,Integer.valueOf(Context.PRODUCT_OFFLINE_STATUS));
				}else if("3".equals(nowStatus)) {
					this.activityAirTicketService.batchOnOrOffActivityAirTicket(idlist,Integer.valueOf(Context.PRODUCT_ONLINE_STATUS));
				}
			}
		}
		model.addAttribute("airticket", airticket);
		return "redirect:" + Global.getAdminPath() + "/airTicket/list/" + nowStatus;
	}

	/**修改产品*/
	@RequestMapping(value = "mod/{proId}")
	public String modify(@PathVariable String proId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Long companyId = StringUtils.toLong(UserUtils.getUser().getCompany().getId());
		model.addAttribute("airlines_list", activityAirTicketService.findAirlineByComid(companyId));// 航空公司
		model.addAttribute("out_areaslist", areaService.findOutCityList(""));// 离境口岸
		model.addAttribute("from_areaslist", areaService.findFromCityList(""));// 出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		
		ActivityAirTicket airticket = this.activityAirTicketService.getActivityAirTicketById(Long.valueOf(proId));
		
		Map<String,Object> paraMap;//存放参数
		Map<String,String> tranameMap=DictUtils.getLabelDesMap(Context.TRAFFIC_NAME);
		for (FlightInfo flightInfo : airticket.getFlightInfos()) {
			paraMap=new HashMap<String,Object>();//存放参数
			paraMap.put("airLineName", tranameMap.get(flightInfo.getAirlines()));
			if(StringUtils.isNotEmpty(flightInfo.getLeaveAirport())){
				AirportInfo airport=airportInfoService.getAirportInfo(Long.valueOf(flightInfo.getLeaveAirport()));
				paraMap.put("leaveAirport", airport!=null?airport.getAirportName():"");
			}
			if(StringUtils.isNotEmpty(flightInfo.getDestinationAirpost())){
				AirportInfo airport=airportInfoService.getAirportInfo(Long.valueOf(flightInfo.getDestinationAirpost()));
				paraMap.put("destinationAirpost", airport!=null?airport.getAirportName():"");
			}
			Currency currency=currencyService.findCurrency(flightInfo.getCurrency_id());
			paraMap.put("currencyName", currency.getCurrencyName());
			
			//仓位、舱位等级
			String airlineCode = flightInfo.getAirlines();
			String spaceLevel = flightInfo.getSpaceGrade();
			List<Map<String, String>> spaceLevelList=this.activityAirTicketService.findSpaceLevelList(companyId, airlineCode);
			List<Map<String, String>> spaceList=this.activityAirTicketService.findAirlSpaceList(companyId, airlineCode, spaceLevel);
		    paraMap.put("airLevel", spaceLevelList);
		    paraMap.put("airSpace", spaceList);
		    
			flightInfo.setParaMap(paraMap);
		}
		
		Map<String,String> map=new HashMap<String,String>();
		if(StringUtils.isNotEmpty(airticket.getArrivedCity())){
			Area area=areaService.get(Long.valueOf(airticket.getArrivedCity()));
			map.put("arrivedCityName", area.getName());
		}
		Currency currency=currencyService.findCurrency(airticket.getCurrency_id());
		map.put("currencyName", currency.getCurrencyName());
		airticket.setParaMap(map);
		model.addAttribute("airticket", airticket);
		//获取切位人数
		int sum = 0;
		for (int i = 0; i < airticket.getAirticketReserveList().size(); i++) {
	        sum += airticket.getAirticketReserveList().get(i).getPayReservePosition();
		}
		//获取占位人数
		model.addAttribute("airticketReserveNum", sum);
		return "modules/airticket/airticketFormEdit";
	}

	/**
	 * 产品添加保存
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
    @RequestMapping(value = "save")
	public synchronized String save(@ModelAttribute ActivityAirTicket airticket,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

//		String ticketId = request.getParameter("txt_ticketId");//机票类型
//		if(StringUtils.isEmpty(ticketId)) return "redirect:"+Global.getAdminPath()+"/activity/manager/list/2";

//		Long id=ticketId!=null?Long.valueOf(ticketId):0;
//		airticket=activityAirTicketService.getActivityAirTicketById(id);

		//手动
		String groupCode="";
		String groupCodeold= "";
		if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")
				|| UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")
				|| UserUtils.getUser().getCompany().getUuid().contains("ed88f3507ba0422b859e6d7e62161b00")
				|| UserUtils.getUser().getCompany().getUuid().contains("f5c8969ee6b845bcbeb5c2b40bac3a23")
				|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")
				|| UserUtils.getUser().getCompany().getUuid().contains("58a27feeab3944378b266aff05b627d2")//日信观光
				|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加国际
				|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
				|| UserUtils.getUser().getCompany().getUuid().contains("049984365af44db592d1cd529f3008c3")//鼎鸿假期
				|| UserUtils.getUser().getCompany().getGroupCodeRuleJP()==0 //C460 批发商配置:手动可修改
				){
			groupCode=airticket.getGroupCode();
		}

		String isDrop = request.getParameter("txt_isDrop");//是否放弃
		if("1".equals(isDrop)){
			return  "forward:" + Global.getAdminPath() + "/airTicket/list/2";
		}
		String recordId=request.getParameter("recordId");//询价记录
		model.addAttribute("recordId", recordId);
		// 取得部门ID
		String deptIds  = request.getParameter("deptId");
		Long deptId ;
		if(null != deptIds)
			deptId = Long.valueOf(deptIds);
		else
		deptId = airticket.getDeptId();

		String jsonObj=request.getParameter("txt_jsonObj");
		Map <String,Class> mymap = new HashMap<String,Class>();
		mymap.put("flightInfos",FlightInfo.class);
		mymap.put("intermodalStrategies",IntermodalStrategy.class);
		JSONObject obj = new JSONObject().fromObject(jsonObj);//将json字符串转换为json对象
		//新数据
		airticket = (ActivityAirTicket)JSONObject.toBean(obj,ActivityAirTicket.class,mymap);

		//大洋 87      取得原团号 ,c451,c453
		ActivityAirTicket  activityAirTicketOld = activityAirTicketService.getActivityAirTicketById(airticket.getId());
		if (null!=activityAirTicketOld) {
			groupCodeold = activityAirTicketOld.getGroupCode();
		}


		//修改之前的数据
	    //	ActivityAirTicket airTicket2 = activityAirTicketService.findById(airticket.getId());
		// 部门ID
		airticket.setDeptId(deptId);
		int temp=0;
		if(airticket.getId()==null||airticket.getId()==0){
			//产品编号
			String companyName = officeService.get(airticket.getProCompany()).getName();
			String productCode = sysIncreaseService.updateSysIncrease(companyName.length() > 3 ? companyName.substring(0, 3) : companyName,
					airticket.getProCompany(), null, Context.PRODUCT_NUM_TYPE);
			airticket.setProductCode(productCode);
		    airticket.setProduct_type_id(Context.ORDER_TYPE_JP);
		    airticket.setDelFlag(Context.DEL_FLAG_NORMAL);//正常状态
		    //产品增加记录日志
		    addAirticketLog(airticket);
		}else{

			ActivityAirTicket airTicket2 = activityAirTicketService.findById(airticket.getId());
			//机票修改日志
			updateAirticketLog(airticket,airTicket2);
			int freePosition = airTicket2.getFreePosition();
			int reservationsNum = airTicket2.getReservationsNum();
			temp = reservationsNum-freePosition;
			airticket.setGroupCode(airTicket2.getGroupCode());

		}

		//获取第一个航段的出发日期,用于生成团号
		String firstFlightInfoDate = null;

		//删除附件、航段、联运相关信息
		activityAirTicketService.deleteRelationByAirTicketId(airticket.getId());
		for (FlightInfo flightInfo : airticket.getFlightInfos()) {

			if(firstFlightInfoDate == null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				if(flightInfo.getStartTime() != null)
 					firstFlightInfoDate = format.format(flightInfo.getStartTime());
			}

			flightInfo.setActivityAirTicket(airticket);
		}
		for (IntermodalStrategy itermodalStrategy : airticket.getIntermodalStrategies()) {
			itermodalStrategy.setActivityAirTicket(airticket);
		}
		if(airticket.getFlightInfos().size()>0){
			FlightInfo flightInfo1=airticket.getFlightInfos().get(0);
			FlightInfo flightInfo2=airticket.getFlightInfos().get(airticket.getFlightInfos().size()-1);
			airticket.setStartingDate(flightInfo1.getStartTime());
			airticket.setReturnDate(flightInfo2.getArrivalTime());
		}
		String scope = request.getParameter("txt_scope");//作用范围
		String proStatus = request.getParameter("txt_proStatus");//状态
	    String hiddenradiovalue = request.getParameter("hiddenradiovalue");
		airticket.setTicket_area_type(hiddenradiovalue);
		//产品附件
		//产品上传文件
		//Set<AirTicketFile> activityFiles = new HashSet<AirTicketFile>();
		AirTicketFile airTicketFile;
	    String[] docSignUpIds = request.getParameterValues("airticket_attach");//报名人附件id


	    if(docSignUpIds != null) {
	    	String[] docSignUpNames = request.getParameterValues("docOriName");//附件名
	    	for(int i = 0; i < docSignUpIds.length; i++) {
	    		airTicketFile = new AirTicketFile();
	    		airTicketFile.setFileType(AirTicketFile.SIGN_UP_TYPE);
	    		airTicketFile.setFileName(docSignUpNames[i]);
	    		airTicketFile.setDocInfo(docInfoService.getDocInfo(StringUtils.toLong(docSignUpIds[i])));
	    		//airTicketFile.setAirticketId(id);
	    		airTicketFile.setActivityAirTicket(airticket);
	    		//activityFiles.add(airTicketFile);
	    		airticket.getAirTicketFiles().add(airTicketFile);
	    	}
	    }

	    airticket.setCreateBy(UserUtils.getUser());
	    if("1".equals(proStatus))
	    	airticket.setProductStatus(Integer.valueOf(Context.PRODUCT_TEMP_STATUS));//草稿
	    else if("2".equals(proStatus))
	    	airticket.setProductStatus(Integer.valueOf(Context.PRODUCT_ONLINE_STATUS));//上架
	    else
	    	airticket.setProductStatus(Integer.valueOf(Context.PRODUCT_OFFLINE_STATUS));//下架

	    if("1".equals(scope))
	    	airticket.setActivityScope(Context.ACTIVITY_SCOPE_ALL);//正常状态
	    else if("2".equals(scope))
	    	airticket.setActivityScope(Context.ACTIVITY_SCOPE_COMPANY);//内部可见
	    else
	    	airticket.setActivityScope(Context.ACTIVITY_SCOPE_AGENT);//渠道可见


	    int temp2 =  airticket.getReservationsNum();
	    airticket.setFreePosition(temp2-temp);
	    airticket.setReview(4);
	    Office company = UserUtils.getUser().getCompany();

	    //系统生成产品团号
	    //环球行
	    if(airticket.getId()==null||airticket.getId()==0){ //发布
	    	//C460 批发商配置团号手动输入
	    	if(UserUtils.getUser().getCompany().getGroupCodeRuleJP()==0){
	    		airticket.setGroupCode(groupCode);
	    	}else{//系统原有逻辑
	    		if(company.getId() == 68L) {
	  	  		    try{
	  	  		    	airticket.setGroupCode(activityGroupService.getGroupNumForTTS(String.valueOf(deptId),firstFlightInfoDate));
	  	  		    } catch(Exception e) {
	  	  		    	System.out.println("调用为批发商环球行提供特定的团号生成规则(包含签证订单的虚拟团号)出错");
	  	  		    	e.printStackTrace();
	  	  		    }
	  	  	    //新行者使用页面生成的团号
	  	  	    } else if (company.getId() == 71) {
	  	  	    	try{
	  	  		    	airticket.setGroupCode(activityGroupService.getGroupNumForTTS(String.valueOf(deptId),firstFlightInfoDate));
	  	  		    } catch(Exception e) {
	  	  		    	System.out.println("调用为批发商环球行提供特定的团号生成规则(包含签证订单的虚拟团号)出错");
	  	  		    	e.printStackTrace();
	  	  		    }
	  	  	    //青岛凯撒,大洋旅游,诚品旅游,懿洋假期,日信观光,优加国际,起航假期
	  	  	    } else if (UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")
	  	  	    		|| UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")//大洋
	  	  	    		|| UserUtils.getUser().getCompany().getUuid().contains("ed88f3507ba0422b859e6d7e62161b00")
	  	  	    		|| UserUtils.getUser().getCompany().getUuid().contains("f5c8969ee6b845bcbeb5c2b40bac3a23")
	  	  	    		|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")//非常国际
	  	  	    		|| UserUtils.getUser().getCompany().getUuid().contains("58a27feeab3944378b266aff05b627d2")//日信观光
	  	  	    		|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加国际
	  	  	    		|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
	  	  	    		) {
	  	  	    	airticket.setGroupCode(groupCode);
	  	        //名扬国际
	  	        } else if (company.getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")) {
	  		    	if(airticket.getId()==null||airticket.getId()==0){
	  		    		groupCode=activityGroupService.getGroupNumForMYGJ("DX", firstFlightInfoDate);
	  		    		airticket.setGroupCode(groupCode);
	  		    	}
	  	  	    //其他供应商使用系统默认的团号
	  	  	    } else {
	  	  	       String code = sysIncreaseService.updateSysIncrease(company.getName(),
	  	  		    		company.getId(), null, Context.GROUP_NUM_TYPE);
	  	      	   try{
	  	  		    	//airticket.setGroupCode(activityGroupService.getGroupNumForTTS(String.valueOf(deptId),firstFlightInfoDate));
	  	      		   airticket.setGroupCode(code);
	  	  		    } catch(Exception e) {
	  	  		    	System.out.println("调用为批发商环球行提供特定的团号生成规则(包含签证订单的虚拟团号)出错");
	  	  		    	e.printStackTrace();
	  	  		    }
	  	  	    }
	    	}
	    }

	    //c451 c453  大洋/非常国际/优加/起航假期 修改      
	    if (UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")//大洋
	    		|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")//非常国际
	    		|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加国际
	    		|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
	    		|| UserUtils.getUser().getCompany().getGroupCodeRuleJP()==0 //C460 批发商配置:手动可修改
	    		) {
  	    	airticket.setGroupCode(groupCode);
        }

	    airticket.setAirticketReserveList(null);

	    //青岛凯撒 和 大洋旅游，诚品旅游,日信观光
	    if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")
	    		|| UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")
	    		|| UserUtils.getUser().getCompany().getUuid().contains("ed88f3507ba0422b859e6d7e62161b00")
	    		|| UserUtils.getUser().getCompany().getUuid().contains("f5c8969ee6b845bcbeb5c2b40bac3a23")
	    		|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")
	    		|| UserUtils.getUser().getCompany().getUuid().contains("58a27feeab3944378b266aff05b627d2")//日信观光
	    		|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加国际
	    		|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
	    		|| UserUtils.getUser().getCompany().getGroupCodeRuleJP()==0 //C460 批发商配置:手动可修改
	    		){
	    	if(airticket.getId()==null || airticket.getId()==0){//新增
		    	boolean flag = activityGroupService.groupNoCheck(groupCode);//true:重复
		    	if(!flag){
		    		activityAirTicketService.save(airticket);
		    	}else{
		    		return "modules/activity/activityDuplicatedGroupcode";
		    	}
	    	}else{

	    		//大洋修改时也要进行团号校验,c451,c453
	    		if (UserUtils.getUser().getCompany().getUuid().contains("7a81a03577a811e5bc1e000c29cf2586")
	    				|| UserUtils.getUser().getCompany().getUuid().contains("1d4462b514a84ee2893c551a355a82d2")
	    				|| UserUtils.getUser().getCompany().getUuid().contains("7a81c5d777a811e5bc1e000c29cf2586")//优加
	    				|| UserUtils.getUser().getCompany().getUuid().contains("5c05dfc65cd24c239cd1528e03965021")//起航假期
	    				|| UserUtils.getUser().getCompany().getGroupCodeRuleJP()==0 //C460 批发商配置:手动可修改
	    				) {
	    			if (!groupCode.equalsIgnoreCase(groupCodeold)) {
	    				boolean flag = activityGroupService.groupNoCheck(groupCode);//true:重复
				    	if(!flag){
				    		ActivityAirTicket activityAirTicket = activityAirTicketService.save(airticket);
				    		if (null!=activityAirTicket) {
				    			GroupcodeModifiedRecord  groupcodeModifiedRecord = new GroupcodeModifiedRecord();
				    			groupcodeModifiedRecord.setCreateBy(UserUtils.getUser().getId().intValue());
				    			groupcodeModifiedRecord.setGroupcodeNew(groupCode);
				    			groupcodeModifiedRecord.setGroupcodeOld(groupCodeold);
				    			groupcodeModifiedRecord.setProductId(activityAirTicket.getId().intValue());
				    			groupcodeModifiedRecord.setProductType(7);
				    			groupcodeModifiedRecord.setUpdateByName(UserUtils.getUser().getName());
				    			groupcodeModifiedRecordDao.saveObj(groupcodeModifiedRecord);
				    			//修改团号时更新审批表review_new中的团号记录(同一批发商下的团号唯一)
				    			String sql = "update review_new set group_code=? where group_code=? and company_id='"+UserUtils.getUser().getCompany().getUuid()+"'";
				    			activityGroupDao.updateBySql(sql, groupCode,groupCodeold);
							}
				    	}else{
				    		return "modules/activity/activityDuplicatedGroupcode";
				    	}
					}else{
						activityAirTicketService.save(airticket);
					}

				}else{
					activityAirTicketService.save(airticket);
				}

	    	}
	    }else{
	    	if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
	    		if(airticket.getId()==null||airticket.getId()==0){
	    			airticket.setGroupCode(activityGroupService.groupCodeCheck(airticket.getGroupCode()));
	    		}
	    	}
	    	activityAirTicketService.save(airticket);
	    }
	    if(company.getId() == 68L)
	    {
	    	   //startingDate

	    	activityGroupDao.getMaxCountForSequenceNew(68L, Integer.parseInt(firstFlightInfoDate.substring(0, 4)));
	    }

	    airticket.getAirlines();
	    EstimatePriceRecordService es = SpringContextHolder.getBean("estimatePriceRecordService");
	    if(StringUtils.isNotBlank(recordId)){
	    	 es.releaseProduct(Long.parseLong(recordId),"7");
	    }

		long companyId = UserUtils.getUser().getCompany().getId();

		activityGroupDao.updateBySql("update   orderinvoice o LEFT JOIN sys_user su  ON o.createBy = su.id  " +
				"SET o.groupCode='"+groupCode+"' WHERE o.groupCode='"+groupCodeold+"' AND su.companyId="+companyId);
		//更新收据团号
		activityGroupDao.updateBySql("update   orderreceipt o LEFT JOIN sys_user su  ON o.createBy = su.id  " +
				"SET o.groupCode='"+groupCode+"' WHERE o.groupCode='"+groupCodeold+"' AND su.companyId="+companyId);
		//更新机票订单
		activityGroupDao.updateBySql("UPDATE airticket_order ao  set ao.group_code='"+groupCode+"' WHERE ao.airticket_id="+airticket.getId());

		//修改团号同时更新审批表review_new对应的团号记录
		activityGroupDao.updateBySql("UPDATE review_new rn SET rn.group_code='"+groupCode+"' WHERE rn.product_id="+airticket.getId()+
				" AND rn.product_type=7");




	    return "redirect:" + Global.getAdminPath() + "/airTicket/list/2";

	}
	/**
	 * 修改机票产品日志
	 * @param airTicket  修改之后数据
	 * @param airTicket2 修改之前数据
	 */
	public void updateAirticketLog(ActivityAirTicket airTicket,ActivityAirTicket airTicket2){
		String content = "机票产品编号"+airTicket2.getProductCode()+" 修改内容为：";
		if(!(airTicket.getAirType()).equals(airTicket2.getAirType())){
			content +="机票类型："+DictUtils.getDictLabel(airTicket2.getAirType() , "air_Type", "")+"->"+DictUtils.getDictLabel(airTicket.getAirType() , "air_Type", "") ;	
		}
		if(!(airTicket.getTicket_area_type()).equals(airTicket2.getTicket_area_type())){
			content +=",航线属地："+airTicket2.getTicket_area_type()+"->"+airTicket.getTicket_area_type();
		}
		
		 List<Dict> departureCity =areaService.findFromCityList("");//出发城市
		 String dbCity = "";
		 String daCity= "";
		if(!(airTicket.getDepartureCity()).equals(airTicket2.getDepartureCity())){
				for (int i = 0; i < departureCity.size(); i++) {
					if((airTicket2.getDepartureCity().toString()).equals(departureCity.get(i).getValue().toString())){
						 dbCity = departureCity.get(i).getLabel();
					}
					if((airTicket.getDepartureCity().toString()).equals(departureCity.get(i).getValue().toString())){
						daCity=departureCity.get(i).getLabel();
					}
				}
				content +=",出发城市："+dbCity+"->"+daCity;
		}
		 List<Area> arrivedCity =	 areaService.findAirportCityList("");// 到达城市
		 String abCity = "";
		 String aaCity = "";
		if(!(airTicket.getArrivedCity()).equals(airTicket2.getArrivedCity())){
			for (int i = 0; i < arrivedCity.size(); i++) {
				if((airTicket2.getArrivedCity().toString()).equals(arrivedCity.get(i).getCode().toString())){
					abCity=arrivedCity.get(i).getName();
				}
				if((airTicket.getArrivedCity().toString()).equals(arrivedCity.get(i).getCode().toString())){
					aaCity=arrivedCity.get(i).getName();
				}
			}
			content +=",到达城市："+abCity+"->"+aaCity;
		}
		
		int flightSize = airTicket.getFlightInfos().size(); //新的条数
		int flightSize2 = airTicket2.getFlightInfos().size(); //原始条数
		if(flightSize<=flightSize2){
		for (int i = 0; i < flightSize ; i++) {
			if(!(airTicket.getFlightInfos().get(i).getLeaveAirport()).equals(airTicket2.getFlightInfos().get(i).getLeaveAirport())){
				AirportInfo  f1 =	airportService.getAirportInfo(Long.parseLong(airTicket.getFlightInfos().get(i).getLeaveAirport()));
				AirportInfo  f2 =	airportService.getAirportInfo(Long.parseLong(airTicket2.getFlightInfos().get(i).getLeaveAirport()));
				content +=",出发机场："+f2.getAirportName()+"->"+f1.getAirportName();
			}
		}
		for (int i = 0; i < flightSize ; i++) {
			if(!(airTicket.getFlightInfos().get(i).getDestinationAirpost()).equals(airTicket2.getFlightInfos().get(i).getDestinationAirpost())){
				AirportInfo  f1 =	airportService.getAirportInfo(Long.parseLong(airTicket.getFlightInfos().get(i).getDestinationAirpost()));
				AirportInfo  f2 =	airportService.getAirportInfo(Long.parseLong(airTicket2.getFlightInfos().get(i).getDestinationAirpost()));
				content +=",到达机场："+f2.getAirportName()+"->"+f1.getAirportName();
			}
		}
		for (int i = 0; i < flightSize ; i++) {
			
			if (airTicket.getFlightInfos().get(i).getStartTime() != null && airTicket2.getFlightInfos().get(i).getStartTime() != null) {
				if(!(airTicket.getFlightInfos().get(i).getStartTime()).equals(airTicket2.getFlightInfos().get(i).getStartTime())){
					content +=",出发时刻："+airTicket2.getFlightInfos().get(i).getStartTime()+"->"+airTicket.getFlightInfos().get(i).getStartTime();
				}
			}
		}
		for (int i = 0; i < flightSize ; i++) {
			if (airTicket.getFlightInfos().get(i).getArrivalTime() != null && airTicket2.getFlightInfos().get(i).getArrivalTime() != null) {
				if(!(airTicket.getFlightInfos().get(i).getArrivalTime()).equals(airTicket2.getFlightInfos().get(i).getArrivalTime())){
					content +=",到达时刻："+airTicket2.getFlightInfos().get(i).getArrivalTime()+"->"+airTicket.getFlightInfos().get(i).getArrivalTime();
				}
			}
		}
		for (int i = 0; i < flightSize ; i++) {
			if(!(airTicket.getFlightInfos().get(i).getAirlines()).equals(airTicket2.getFlightInfos().get(i).getAirlines())){
				content +=",航空公司："+airTicket2.getFlightInfos().get(i).getAirlines()+"->"+airTicket.getFlightInfos().get(i).getAirlines();
			}
		}
		for (int i = 0; i < flightSize ; i++) {
			if(!(airTicket.getFlightInfos().get(i).getSpaceGrade()).equals(airTicket2.getFlightInfos().get(i).getSpaceGrade())){
				content +=",舱位等级："+airTicket2.getFlightInfos().get(i).getSpaceGrade()+"->"+airTicket.getFlightInfos().get(i).getSpaceGrade();
			}
		}
		for (int i = 0; i < flightSize ; i++) {
			if(!(airTicket.getFlightInfos().get(i).getFlightNumber()).equals(airTicket2.getFlightInfos().get(i).getFlightNumber())){
				content +=",航班号："+airTicket2.getFlightInfos().get(i).getFlightNumber()+"->"+airTicket.getFlightInfos().get(i).getFlightNumber();
			}
		}
		for (int i = 0; i < flightSize ; i++) {
			if(!(airTicket.getFlightInfos().get(i).getAirspace()).equals(airTicket2.getFlightInfos().get(i).getAirspace())){
				content +=",舱位："+airTicket2.getFlightInfos().get(i).getAirspace()+"->"+airTicket.getFlightInfos().get(i).getAirspace();
			}
		}
	}
		if (airTicket.getOutTicketTime() != null && airTicket2.getOutTicketTime() != null) {
			if(!(airTicket.getOutTicketTime()).equals(airTicket2.getOutTicketTime())){
				content +=",出票时间："+airTicket2.getOutTicketTime()+"->"+airTicket.getOutTicketTime();
			}
		}
		if (airTicket.getOutArea() != null && airTicket2.getOutArea() != null) {
			if(!(airTicket.getOutArea()).equals(airTicket2.getOutArea())){
				content +=",离境口岸："+airTicket2.getOutArea()+"->"+airTicket.getOutArea();
			}
		}
		if (airTicket.getIntermodalType() != null && airTicket2.getIntermodalType() != null) {
			if(!(airTicket.getIntermodalType()).equals(airTicket2.getIntermodalType())){
				content +=",联运类型："+airTicket2.getIntermodalType()+"->"+airTicket.getIntermodalType();
			}
		}
		if(!(airTicket.getPayMode_advance()).equals(airTicket2.getPayMode_advance())){
			content +=",付款方式："+airTicket2.getPayMode_advance()+"->"+airTicket.getPayMode_advance();
		}
		if(!(airTicket.getRemark()).equals(airTicket2.getRemark())){
			content +=",备注："+airTicket2.getRemark()+"->"+airTicket.getRemark();
		}
		if(!(airTicket.getTaxamt()).equals(airTicket2.getTaxamt())){
			content +=",税费："+airTicket2.getTaxamt()+"->"+airTicket.getTaxamt();
		}
		if(!(airTicket.getSettlementAdultPrice()).equals(airTicket2.getSettlementAdultPrice())){
			content +=",成人成本价："+airTicket2.getSettlementAdultPrice()+"->"+airTicket.getSettlementAdultPrice();
		}
		if(!(airTicket.getSettlementcChildPrice()).equals(airTicket2.getSettlementcChildPrice())){
			content +=",儿童成本价："+airTicket2.getSettlementcChildPrice()+"->"+airTicket.getSettlementcChildPrice();
		}
		if(!(airTicket.getSettlementSpecialPrice()).equals(airTicket2.getSettlementSpecialPrice())){
			content +=",特殊人群成本价："+airTicket2.getSettlementSpecialPrice()+"->"+airTicket.getSettlementSpecialPrice();
		}
		if(!(airTicket.getSpecialremark()).equals(airTicket2.getSpecialremark())){
			content +=",特殊人群备注："+airTicket2.getSpecialremark()+"->"+airTicket.getSpecialremark();
		}
		if(!(airTicket.getDepositamt()).equals(airTicket2.getDepositamt())){
			content +=",订金："+airTicket2.getDepositamt()+"->"+airTicket.getDepositamt();
		}
		/*审批重构-产品发布后不可修改部门*/
//		if(!(airTicket.getDeptId()).equals(airTicket2.getDeptId())){
//			Department deptAfter = departmentService.findById(airTicket.getDeptId()); 
//			String deptAfterName=deptAfter.getName();//修改后的部门
//			Department deptBefore = departmentService.findById(airTicket2.getDeptId()); 
//			String deptBeforeName = deptBefore.getName(); //修改之前的部门
//			content +=",所属部门："+deptBeforeName+"->"+deptAfterName;
//		}
		logOpeService.saveLogOperate(Context.log_type_product,
				Context.log_type_product_name, content, "2", Context.ProductType.PRODUCT_AIR_TICKET, airTicket.getId());
	}
	
	
	
	/**
	 * 增加机票产品发布日志
	 * 
	 */
	
	public void addAirticketLog(ActivityAirTicket airTicket){
		String content = "发布机票产品成功,产品编号为:"+airTicket.getProductCode();
		logOpeService.saveLogOperate(Context.log_type_product,
				Context.log_type_product_name, content, "1", Context.ProductType.PRODUCT_AIR_TICKET, airTicket.getId());
		
	}
	
	/**
	 * 多文件上传
	 */
	@RequestMapping("uploadFilesPage")
	public String getUploadFilesPage() {
		return "modules/airticket/mulUploadFile";
	}

}
