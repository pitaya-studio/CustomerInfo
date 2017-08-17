package com.trekiz.admin.modules.stock.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrder;
import com.trekiz.admin.modules.activity.service.ActivityReserveOrderService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.AirTicketReserveOrder;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticket.service.IAirTicketReserveOrderService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.entity.AirticketReserveFile;
import com.trekiz.admin.modules.stock.service.AirticketStockService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 机票库存切位控制器
 * Created by ZhengZiyu on 2014/10/30.
 */
@Controller
@RequestMapping(value="${adminPath}/stock/manager/airticket")
public class AirticketController extends BaseController {

    private static final Log logger = LogFactory.getLog(AirticketController.class);

    @Autowired
    private IActivityAirTicketService iActivityAirTicketService;
    @Autowired
    private ActivityReserveOrderService activityReserveOrderService;
    @Autowired
    private AgentinfoService agentinfoService;
	@Autowired
	private CurrencyService currencyService;
    @Autowired
    private DepartmentService departmentService;  
    
	@Autowired
	private SysIncreaseService sysIncreaseService;

    @Autowired
    private AirticketStockService airticketStockService;
    
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private AirportService airportService;
	@Autowired
	IAirTicketOrderService  iAirTicketOrderService;	
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
    
	@Autowired
    private IAirTicketReserveOrderService airTicketReserveOrderService;
    /**
     * 机票切位列表展示
     * @param activityAirTicket
     * @param request
     * @param response
     * @param model
     * @return
     */  
    @RequestMapping(value={"list",""})
	public String list(@ModelAttribute ActivityAirTicket activityAirTicket, 
			HttpServletRequest request, HttpServletResponse response, Model model){
		//20151013增加团号查询
		String groupCode = request.getParameter("groupCode");
		String departureCity = request.getParameter("departureCity");
		String arrivedCity = request.getParameter("arrivedCity");
		if(groupCode != null) {
			groupCode =groupCode.trim();
			activityAirTicket.setGroupCode(groupCode);
		}
		if(departureCity!= null) {
			departureCity =departureCity.trim();
		}
		
		if(arrivedCity!= null) {
			arrivedCity =arrivedCity.trim();
		}
		//User user = UserUtils.getUser();
        //按部门展示
        //DepartmentCommon common = departmentService.setDepartmentPara("activity", model, user);
        String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
        String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
        String groupOpenDate =request.getParameter("groupOpenDate");
        String groupCloseDate =request.getParameter("groupCloseDate");
        //String wholeSalerKey = request.getParameter("wholeSalerKey");
        Long agentId = null;
        Long departmentId=null;
        if(StringUtils.isNotBlank(request.getParameter("agentId"))){
            try {
                agentId = StringUtils.toLong(request.getParameter("agentId"));
            } catch (NumberFormatException e) {
            }
        }
        if(StringUtils.isNotBlank(request.getParameter("department"))){
            try {
            	departmentId = StringUtils.toLong(request.getParameter("department"));
            } catch (NumberFormatException e) {
            }
        }
        
        
    	String departureCityPara=null;
		List<Dict> from_areas=DictUtils.getDictList("from_area");
		if(StringUtils.isNotEmpty(departureCity)){
			departureCity=departureCity.trim();
			for(Dict dict:from_areas){
				if(departureCity.equals(dict.getLabel()))
					departureCityPara=dict.getValue().toString();
			}
			if(StringUtils.isEmpty(departureCityPara)) departureCityPara="-1";
		}
		
		String arrivedCityPara=null;
		if(StringUtils.isNotEmpty(arrivedCity)){
			List<Area> areas=areaService.findAreaByName(arrivedCity.trim());
			if(areas.size()>0) arrivedCityPara=areas.get(0).getId().toString();
			else arrivedCityPara = "-1";			
		}
		
        //把查询条件中的出发城市名称 转换为出发城市代码
        /*
           String departureCityName=activityAirTicket.getDepartureCity();    
           if(departureCityName!=null){
        	    //System.out.println(departureCityName);
              departureCity=DictUtils.getDictValue(String.valueOf(departureCityName.trim()), "from_area", "");
             if(departureCity.length() > 0){
            	 // System.out.println(departureCity);
              activityAirTicket.setDepartureCity(departureCity); 
             } else{
            	 activityAirTicket.setDepartureCity(departureCityName); 
             }             
           } */
           
		Long companyId = UserUtils.getUser().getCompany().getId();  
        Page<ActivityAirTicket> page = activityAirTicketService.findAirTicketStock(new Page<ActivityAirTicket>(request, response),
                activityAirTicket, departureCityPara, arrivedCityPara,
                StringUtils.isEmpty(settlementAdultPriceStart) ? null : new BigDecimal(settlementAdultPriceStart),
                StringUtils.isEmpty(settlementAdultPriceEnd) ? null : new BigDecimal(settlementAdultPriceEnd),
                StringUtils.isNotBlank(request.getParameter("airType")) ? request.getParameter("airType") : null,
                StringUtils.isNotBlank(request.getParameter("groupOpenDate")) ? request.getParameter("groupOpenDate") : null,
                StringUtils.isNotBlank(request.getParameter("groupCloseDate")) ? request.getParameter("groupCloseDate") : null, 
                companyId,agentId,
                request.getParameter("orderBy"));
        
        model.addAttribute("activityAirTicket", activityAirTicket);
        model.addAttribute("page", page);
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        
        model.addAttribute("startingDate", groupOpenDate);
        model.addAttribute("returnDate", groupCloseDate);
        
        model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
        model.addAttribute("departmentList", departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId()));
        
        List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
        //出发城市
        model.addAttribute("departureCity", departureCity);
		//到达城市
        model.addAttribute("arrivedCity", arrivedCity);
        // 机票类型
        model.addAttribute("airTypes", DictUtils.getSysDicMap("air_Type"));		
		// 航空公司
		model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));
		// 舱位
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));
		// 舱位等级
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));		
				
		model.addAttribute("areas", areaService.findAirportCityList(""));//到达城市
		model.addAttribute("from_area", DictUtils.getDictList("from_area"));// 出发城市
		
        model.addAttribute("airportlist", airportService.queryAirport(companyId));
       
		
		model.addAttribute("agentId", agentId);
		model.addAttribute("departmentId", departmentId);
		model.addAttribute("groupCode", groupCode);
		return "modules/stock/airticket/airTicketRecordList";
	}

    /**
     * 机票切位列表展示
     * @param activityAirTicket
     * @param request
     * @param response
     * @param model
     * @author WangXK
     * @return
     */  
    @RequestMapping(value={"getActivityAirTicketList"})
	public String getActivityAirTicketList(@ModelAttribute ActivityAirTicket activityAirTicket, 
			HttpServletRequest request, HttpServletResponse response, Model model){
    	//价格范围
        String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
        String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
        
        //时间区间
        String groupOpenDate =request.getParameter("groupOpenDate");
        String groupCloseDate =request.getParameter("groupCloseDate");

        //切位渠道
    	String agentIdParam = request.getParameter("agentId");
        Long agentId = null;
        if(StringUtils.isNotBlank(agentIdParam)){
            agentId = StringUtils.toLong(agentIdParam);
        }
        
        //余位
        String freePositionStarts=request.getParameter("freePositionStart");
        String freePositionEnds=request.getParameter("freePositionEnd");
        Integer freePositionStart=null;
        if(StringUtils.isNotBlank(freePositionStarts)){
        	freePositionStart=Integer.parseInt(freePositionStarts);
        }
        Integer freePositionEnd=null;
        if(StringUtils.isNotBlank(freePositionEnds)){
        	freePositionEnd=Integer.parseInt(freePositionEnds);
        }
        //出发城市
    	String departureCityPara=null;
		List<Dict> fromAreas=DictUtils.getDictList("from_area");
		String departureCity = activityAirTicket.getDepartureCity();
		if(StringUtils.isNotEmpty(departureCity)){
			departureCity=departureCity.trim();
			for(Dict dict:fromAreas){
				if(departureCity.equals(dict.getLabel()))
					departureCityPara=dict.getValue().toString();
			}
			if(StringUtils.isEmpty(departureCityPara)) departureCityPara="-1";
		}
		
		//到达城市
		String arrivedCityPara=null;
		if(StringUtils.isNotEmpty(activityAirTicket.getArrivedCity())){
			List<Area> areas=areaService.findAreaByName(activityAirTicket.getArrivedCity().trim());
			if(areas.size()>0) arrivedCityPara=areas.get(0).getId().toString();
			else arrivedCityPara = "-1";			
		}

		//切位和归还切位状态标识
		String source = request.getParameter("source");
		model.addAttribute("source", source);
		
		Long companyId = UserUtils.getUser().getCompany().getId();  
		
        Page<ActivityAirTicket> page = activityAirTicketService.findAirTicketStock(new Page<ActivityAirTicket>(request, response,-1),
                activityAirTicket, departureCityPara, arrivedCityPara,
                StringUtils.isEmpty(settlementAdultPriceStart) ? null : new BigDecimal(settlementAdultPriceStart),
                StringUtils.isEmpty(settlementAdultPriceEnd) ? null : new BigDecimal(settlementAdultPriceEnd),
                StringUtils.isNotBlank(activityAirTicket.getAirType()) ? activityAirTicket.getAirType() : null,
                StringUtils.isNotBlank(groupOpenDate) ? groupOpenDate : null,
                StringUtils.isNotBlank(groupCloseDate) ? groupCloseDate : null, 
                companyId,agentId,
                StringUtils.isNotBlank(freePositionStarts)?freePositionStart:null,
                StringUtils.isNotBlank(freePositionEnds)?freePositionEnd:null,		
                request.getParameter("orderBy"), source);
        
        //如果是返还切位则根据渠道商过滤查询结果
		List<Long> airTicketIds = new ArrayList<Long>();
		if("isReturn".equals(source) && CollectionUtils.isNotEmpty(page.getList())) {
			for(ActivityAirTicket airTicket : page.getList()) {
				airTicketIds.add(airTicket.getId());
			}
			
			//返回列表中所有的团期切位集合信息
			List<AirticketActivityReserve> reserves = airticketStockService.getReservesByAirTicketIds(airTicketIds, agentId);
			Map<String, AirticketActivityReserve> reserveMap = new HashMap<String, AirticketActivityReserve>();
			if(CollectionUtils.isNotEmpty(reserves)) {
				for(AirticketActivityReserve reserve : reserves) {
					reserveMap.put(String.valueOf(reserve.getActivityId()), reserve);
				}
			}
			//修改当前团期的切位数和已售切位数
			Iterator<ActivityAirTicket> airTicketIter = page.getList().iterator();
			while(airTicketIter.hasNext()) {
				ActivityAirTicket airTicket = airTicketIter.next();
				
				AirticketActivityReserve currReserve = reserveMap.get(String.valueOf(airTicket.getId()));
				if(currReserve != null) {
					if(currReserve.getPayReservePosition()-currReserve.getSoldPayPosition() > 0) {
						airTicket.setSoldPayPosition(currReserve.getSoldPayPosition());
						airTicket.setPayReservePosition(currReserve.getPayReservePosition());
					} else {
						airTicketIter.remove();
					}
				} else {
					airTicketIter.remove();
				}
			}
		}
        
        model.addAttribute("activityAirTicket", activityAirTicket);
        
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        
        model.addAttribute("groupOpenDate", groupOpenDate);
        model.addAttribute("groupCloseDate", groupCloseDate);
        model.addAttribute("freePositionStart", freePositionStart);
        model.addAttribute("freePositionEnd", freePositionEnd);

        //币种信息
        List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
        model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		// 航空公司
		model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));
				
		model.addAttribute("areas", areaService.findAirportCityList(""));//到达城市
		model.addAttribute("from_area", DictUtils.getDictList("from_area"));// 出发城市
		
        //机场列表
        model.addAttribute("airportlist", airportService.queryAirport(companyId));
		
		model.addAttribute("agentId", agentId);
		
		model.addAttribute("page", page);
		
		String selectedProducts = request.getParameter("selectedProducts");
		model.addAttribute("selectedProducts", selectedProducts);
		
		if(CollectionUtils.isNotEmpty(page.getList())) {
			model.addAttribute("pageCount", page.getList().size());
		} else {
			model.addAttribute("pageCount", 0);
		}
		
		return "modules/stock/airticket/productGroup";
	}
    /**
     * 详情页
     * @param id
     * @param activityAirTicket
     * @param agentId
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("stock:airticket:view")
    @RequestMapping(value="detail/{id}")
    public String detail(@PathVariable String id, @ModelAttribute ActivityAirTicket activityAirTicket,@RequestParam(required=false) Long agentId,HttpServletRequest request, HttpServletResponse response, Model model){
        if(agentId != null){
            String agentName = AgentInfoUtils.getAgentName(agentId);
            if(StringUtils.isNotEmpty(agentName))model.addAttribute("agentName", agentName);
        }
        if(StringUtils.isNotBlank(id)){
            activityAirTicket = iActivityAirTicketService.getActivityAirTicketById(Long.valueOf(id));
        }
        List<AirticketActivityReserve> ticketreserve = airticketStockService.findReserve(Long.valueOf(id));
        model.addAttribute("airticketId", id);
        model.addAttribute("activityAirTicket", activityAirTicket);
        model.addAttribute("airticketReserve", ticketreserve);       
        //model.addAttribute("arrivedcitylist", areaService.findByCityList()); // 到达城市
        model.addAttribute("arrivedcitylist",areaService.findAll());//到达城市
        //机场列表
        Long companyId = UserUtils.getUser().getCompany().getId();
        model.addAttribute("airportlist", airportService.queryAirport(companyId));
        
        String showReserve = request.getParameter("showReserve");
		model.addAttribute("showReserve", showReserve);
        return "modules/stock/airticket/detail";
    }
    
    

    /**
     * 订单详情
     * @param request
     * @param response
     * @param productGroupId
     * @param status
     * @param model
     * @return
     */
    @RequestMapping(value="airticketDetail")
    @ResponseBody
    public Object airticketDetail(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam(required=true)Long productGroupId,@RequestParam(required=true)String status,Model model) {
        return airticketStockService.findProductGroupOrders(request, response, productGroupId, status);
    }

    /**
     * 订单售出切位详情
     * @author liangjingming
     */
    @ResponseBody
    @RequestMapping(value="soldNopayPosition/{productId}")
    public Object soldNopayPosition(HttpServletRequest request, HttpServletResponse response,@PathVariable String productId){

    	List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> soldNopanList = Lists.newArrayList();
		soldNopanList= airticketStockService.findAirSoldNopayPosition(Long.parseLong(productId));
		   if(soldNopanList!=null && soldNopanList.size()!=0){
	            for(Map<String, Object> objs:soldNopanList){
	                Map<String, Object> data = new HashMap<String, Object>();
	                data.putAll(objs);
	                Object payStatus = null;
	               
	                if(objs.get("payMoney")==null) data.put("payMoney", " ");              
	                
	                if(objs.get("pay")!=null){
	                   for(Dict dict:DictUtils.getDictList("order_pay_status")){
	                     if( (objs.get("pay").toString()).equals(dict.getValue()))
	                        payStatus = dict.getLabel();	                   
	                   }
	                }else {
	                   payStatus= " ";
	                }
	                data.put("payStatus",payStatus);	                
	                datas.add(data);
	            }
	        }
	        return datas;
    	//return airticketStockService.findSoldNopayPosition(request, response, productId);
        
        /*
        List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> soldNopanList = Lists.newArrayList();
        
        soldNopanList = iAirTicketOrderService.queryAirticketOrdersByProductId(productId);
        
        soldNopanList = airticketStockService.findSoldNopayPosition(Long.parseLong(productId));
        if(soldNopanList!=null && soldNopanList.size()!=0){
            for(Map<String, Object> objs:soldNopanList){
                Map<String, Object> data = new HashMap<String, Object>();
                data.putAll(objs);
                Object payStatus = null;
                for(Dict dict:DictUtils.getDictList("order_pay_status")){
                    if((objs.get("payStatus").toString()).equals(dict.getValue()))
                        payStatus = dict.getLabel();
                }
                data.put("payStatus",payStatus);
                datas.add(data);
            }
        }
        return datas;*/
    }
    
    
    /**
     * 机票切位订单列表
     * @author haiming.zhao
     * @param activityAirTicket
     * @param request   
     * @param response
     * @param model
     * @return
     * */
    @RequestMapping(value="getReserveOrderList")
    public String  getReserveOrderList(@ModelAttribute ActivityAirTicket airTicket ,
													    		HttpServletRequest request,
													    		HttpServletResponse response,
													    		Model model){

    	  
    		String departureCity = request.getParameter("departureCity");  //出发地
    		String arrivedCity = request.getParameter("arrivedCity");//目的地	
    		String departureCityd = request.getParameter("departureCity");  //出发地
    		String arrivedCityd = request.getParameter("arrivedCity");//目的地	
    		String paymentType = request.getParameter("paymentType");//渠道结算方式
    		if(departureCity!= null) {
    			departureCity =departureCity.trim();
    		}	
    		if(arrivedCity!= null) {
    			arrivedCity =arrivedCity.trim();
    		}
    		String orderby = request.getParameter("ob");
            //按部门展示
            DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
            String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");  
            String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
//            String wholeSalerKey = request.getParameter("wholeSalerKey");
            String agentId = request.getParameter("agentId");
            
    		List<Dict> from_areas=DictUtils.getDictList("from_area");
    		if(StringUtils.isNotEmpty(departureCity)){
    			for(Dict dict:from_areas){
    				if(departureCity.equals(dict.getLabel())){
    					departureCity=dict.getValue();
    					break;
    				}
    			}
    		}
    		
    		if(StringUtils.isNotEmpty(arrivedCity)){
    			List<Area> areas=areaService.findAreaByName(arrivedCity);
    			if(areas.size()>0)
    				arrivedCity=areas.get(0).getId().toString();
    			}
    		
	    	Page<AirTicketReserveOrder> page = airTicketReserveOrderService.findAirTicketReserveOrder(new Page<AirTicketReserveOrder>(request, response),
	    			airTicket, departureCity, arrivedCity,agentId,
			               settlementAdultPriceStart,
	                        settlementAdultPriceEnd,
	                        request.getParameter("airType"),
	                      request.getParameter("startingDate"),
	                      request.getParameter("returnDate"), paymentType,
	                        orderby, common);
	    	
	    	model.addAttribute("activityAirTicket", airTicket);
	        model.addAttribute("page", page);
	    	model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
	    	//出发城市
	    	model.addAttribute("departureCity", departureCity);
	    	//到达城市
	    	model.addAttribute("arrivedCity", arrivedCity);
	    	//出发城市
	    	model.addAttribute("departureCityd", departureCityd);
	    	//到达城市
	    	model.addAttribute("arrivedCityd", arrivedCityd);
	    	//渠道结算方式
	    	model.addAttribute("paymentType", paymentType);
	    	//机场列表
	    	Long companyId = UserUtils.getUser().getCompany().getId(); 
	    	 model.addAttribute("airportlist", airportService.queryAirport(companyId));
	    	// 机票类型
	        model.addAttribute("airTypes", DictUtils.getSysDicMap("air_Type"));	
	    	// 航空公司
//			model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));
	        model.addAttribute("traffic_namelist", activityAirTicketService.findAirlineByComid(companyId));
			model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
			model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
	       return "modules/stock/airticket/reserveOrderList";
			//return "";
	    }
    
    
    /**
     *
     *  功能:产品切位
     *
     *  @author zj
     *  @DateTime 2014-2-11 上午11:52:40
     *  @param travelActivity
     *  @param request
     *  @param response
     *  @param model
     *  @return
     */
    @RequestMapping(value="reserve")
    public String reserve(@ModelAttribute ActivityAirTicket activityAirTicket,@RequestParam(required=false) Long id,@RequestParam(required=false) Long agentId,HttpServletRequest request, HttpServletResponse response,Model model){
       
    	/*
        Map<Long, List<AirticketReserveFile>> fileMap = new HashMap<Long, List<AirticketReserveFile>>();
        List<AirticketActivityReserve> reserveList = null;
        if( agentId != null && id != null){
            reserveList = airticketStockService.findReserve(agentId,id);
            List<AirticketReserveFile> activityReserveFileList = airticketStockService.findByAgentIdAndSrcActivityId(agentId, id);
            if(reserveList != null || activityReserveFileList != null){
                for(AirticketReserveFile file:activityReserveFileList){
                    List<AirticketReserveFile> filelist = fileMap.get(file.getAirticketActivityId());
                    if(filelist==null){
                        filelist = new ArrayList<AirticketReserveFile>();
                        fileMap.put(file.getAirticketActivityId(),filelist);
                    }
                    filelist.add(file);
                }

            }
        } */

        if(agentId != null){
            String agentName = AgentInfoUtils.getAgentName(agentId);
            if(StringUtils.isNotEmpty(agentName))model.addAttribute("agentName", agentName);
        }
        if(id != null && StringUtils.isNotBlank(String.valueOf(id))){
            activityAirTicket = iActivityAirTicketService.getActivityAirTicketById(id);
        }
      
        List<AirticketActivityReserve> airticketReserve=null;        
        airticketReserve=airticketStockService.findReserve(agentId,id);
       
        model.addAttribute("airticketId", id);
        model.addAttribute("activityAirTicket", activityAirTicket);       
        if(airticketReserve.size()==1){
        	model.addAttribute("airticketReserve", airticketReserve.get(0));    
        } 
        
        model.addAttribute("airticketReserveList",  airticketStockService.findAirReserveOrder(id,agentId));

        model.addAttribute("isRequested",agentId==null?true:false);
        Long companyId = UserUtils.getUser().getCompany().getId();  
        model.addAttribute("airportlist", airportService.queryAirport(companyId));
        model.addAttribute("agentId",agentId);
		model.addAttribute("areas", areaService.findAirportCityList(""));//到达城市
		model.addAttribute("from_area", areaService.findFromCityList(""));// 出发城市
        model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
        return "modules/stock/airticket/reserve";       
     }
    
    /**
     *  机票切位订单详细信息
     *  @author haiming.zhao
     *  @DateTime 2014-12-29 17:00:00
     *  @param id  机票Id
     *  @param orderNum  订单编号
     *  @param request  HttpServletRequest
     *  @param response  HttpServletResponse
     *  @param model   Model
     * */ 
    @RequestMapping(value="airTicketReserveOrderInfo")
	public String airTicketReserveOrderInfo( @RequestParam("id") Long id ,@RequestParam("orderNum") String orderNum,HttpServletRequest request, HttpServletResponse response,Model model){
    	String payVoucherIds="";
    	
    	ActivityAirTicket activityAirTicket = iActivityAirTicketService.getActivityAirTicketById(id);
    	List<ActivityReserveOrder> list = activityReserveOrderService.findActivityReserveOrderByOrderNum(orderNum);
    //	List<AirticketReserveFile> filelist = airticketStockService.findByAgentIdAndSrcActivityId(list.get(0).getAgentId(), list.get(0).getSrcActivityId());
    	List<AirticketReserveFile> filelist = airticketStockService.findFilesByAgentIdAndReserveOrderId(list.get(0).getAgentId(), list.get(0).getId());
    	if(filelist.size()>0){
    		payVoucherIds = Collections3.extractToString(filelist,"srcDocId",",");
	    }
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
    	model.addAttribute("payVoucherIds", payVoucherIds);
        Long companyId = UserUtils.getUser().getCompany().getId();  
        model.addAttribute("airportlist", airportService.queryAirport(companyId));
    	model.addAttribute("activityAirTicket", activityAirTicket);
    	model.addAttribute("list", list);
		return "modules/stock/airticket/airTicketReserveOrderInfo";
	}
    /**
     *
     *  功能:产品切位
     *
     *  @author zj
     *  @DateTime 2014-2-11 上午11:52:40
     *  @param travelActivity
     *  @param request
     *  @param response
     *  @param model
     *  @return
     */
    @RequestMapping(value="doreserve")
    public String doreserve(@RequestParam(required=false) Long id,@RequestParam(required=false) Long reserveid,@RequestParam(required=false) Long agentId,HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
//        String[] reserveids = request.getParameterValues("reserveid");
//        String[] activityGroupIds = request.getParameterValues("activityGroupId");
        String[] payReservePositions = request.getParameterValues("payReservePosition");
        String[] frontMoneys = request.getParameterValues("frontMoney");
        String[] remarks = request.getParameterValues("remark");
        String[] payType = request.getParameterValues("payType");
        String[] reservation = request.getParameterValues("reservation");
        Long reserveOrderId= null;
        if(id!=null&&agentId!=null){
            List<AirticketActivityReserve> airticketReserveList = new ArrayList<AirticketActivityReserve>();
            List<ActivityReserveOrder> activityReserveOrderList = new ArrayList<ActivityReserveOrder>();
			int payReservePosition = StringUtils.toInteger(payReservePositions[0]);
			
                if(payReservePosition>0){
                    AirticketActivityReserve ticketreserve = new AirticketActivityReserve();
                    if(reserveid != null)  ticketreserve.setId(reserveid);                     
                   
                    ticketreserve.setActivityId(id);
                    ticketreserve.setAgentId(agentId);
                    ticketreserve.setReserveType(0);
                    ticketreserve.setPayReservePosition(payReservePosition);
                    //填写价格信息列表中的订金列设为非必填项，如果没填入数据则为0，20150910
					if(StringUtils.isNotBlank(frontMoneys[0])) {
						ticketreserve.setFrontMoney(new BigDecimal(frontMoneys[0]));
					}else {
						ticketreserve.setFrontMoney(new BigDecimal(0));
					}
                    ticketreserve.setRemark(remarks[0]);
                    ticketreserve.setSoldPayPosition(0);
                    ticketreserve.setPayType(StringUtils.toInteger(payType[0]));
                    ticketreserve.setReservation(reservation[0]);
                    airticketReserveList.add(ticketreserve);                   
                    
                    ActivityReserveOrder activityReserveOrder  = new ActivityReserveOrder();					
					activityReserveOrder.setSrcActivityId(id);
					activityReserveOrder.setActivityGroupId(id);
					activityReserveOrder.setPayType(StringUtils.toInteger(payType[0]));
					activityReserveOrder.setReservation(reservation[0]);
					activityReserveOrder.setCreateBy(UserUtils.getUser().getId());
					
					String companyName=UserUtils.getUser().getCompany().getName();
					Long companyId=UserUtils.getUser().getCompany().getId();
					String orderNum = sysIncreaseService.updateSysIncrease(companyName
							.length() > 3 ? companyName.substring(0, 3) : companyName,
									companyId, null, Context.ORDER_NUM_TYPE);
					activityReserveOrder.setOrderNum(orderNum);
					activityReserveOrder.setAgentId(agentId);
					Date createDate= new Date();
					activityReserveOrder.setCreateDate(createDate);
					activityReserveOrder.setUpdateDate(createDate);
					activityReserveOrder.setSaleId(UserUtils.getUser().getId());
					
					activityReserveOrder.setOrderStatus(1); //订单状态(0:未付定金,1:已付定金)
					activityReserveOrder.setConfirm(0); //收款确认(0:未确认,1:已确认)
					activityReserveOrder.setReserveType(1); //散拼0,机票1
					activityReserveOrder.setMoneyType(1); //人民币
					activityReserveOrder.setRemark(remarks[0]);
					//填写价格信息列表中的订金列设为非必填项，如果没填入数据则为0，20150910
					BigDecimal money = null;
					if(StringUtils.isNotBlank(frontMoneys[0])) {
						money = new BigDecimal(frontMoneys[0]);
					}else {
						money = new BigDecimal("0");
					}
					activityReserveOrder.setOrderMoney(money);
					activityReserveOrder.setPayMoney(money);
					activityReserveOrder.setPayReservePosition(payReservePosition);
					activityReserveOrderList.add(activityReserveOrder);					
                }
                reserveOrderId = airticketStockService.saveAirticketReserveList(airticketReserveList,activityReserveOrderList, request);
                getFileList(request,reserveOrderId,id);
        }
        
        //model.addAttribute("srcActivityId", id);
        //model.addAttribute("agentId", agentId);
        //model.addAttribute("reserveOrderId", reserveOrderId);        
//        return "redirect:"+ Global.getAdminPath()+"/stock/manager/airticket/uploadme/"+id+"/"+agentId+"/"+reserveOrderId;        
       
        
        //sy
        return "redirect:"+ Global.getAdminPath()+"/stock/manager/airticket/detail/"+ id;
    }
    
	/**
	 * 切位附件上传
	 * by songyang 二〇一五年十二月二日 21:13:23
	 * @param request
	 * @param reserveOrderId
	 * @param srcActivityId
	 */
	
	public void getFileList(HttpServletRequest request,Long reserveOrderId,Long srcActivityId){
		
        String activityGroupId = request.getParameter("activityGroupId");
		String agentId = request.getParameter("agentId");
//		String reserveOrderId = request.getParameter("reserveOrderId");
		List<DocInfo> list =null;
		if(ArrayUtils.isNotEmpty(request.getParameterValues("docId"))){
			String[] docId = request.getParameterValues("docId");
			String[] docName = request.getParameterValues("docName");
			String[] docPath = request.getParameterValues("docPath");
			list = new ArrayList<DocInfo>();
			for(int i=0;i<docId.length;i++){
				if(StringUtils.isNotBlank(docId[i])){
					//保存附件表数据
                    AirticketReserveFile actReserve = new AirticketReserveFile();
                    actReserve.setAirticketActivityId(srcActivityId);
                    actReserve.setAgentId(Long.parseLong(agentId));
                    actReserve.setReserveOrderId(reserveOrderId);
                    actReserve.setFileName(docName[i]);
                    actReserve.setSrcDocId(Long.parseLong(docId[i]));
                    airticketStockService.saveairticketReserveFile(actReserve);
				}
			}
		}
	}
       
    @RequestMapping(value="uploadme/{srcActivityId}/{agentId}/{reserveOrderId}")
    public String uploadme(@PathVariable Long srcActivityId,@PathVariable Long agentId, @PathVariable Long reserveOrderId,Model model) {
        model.addAttribute("srcActivityId", srcActivityId);
        model.addAttribute("agentId", agentId);
        model.addAttribute("reserveOrderId", reserveOrderId);
        return "modules/stock/airticket/uploadme";
    } 
    

    @RequestMapping(value="uploadform")
    public String uploadform(@RequestParam("srcActivityId") Long srcActivityId,@RequestParam("agentId") Long agentId, Model model) {
        List<AirticketReserveFile> activityReserveFileList = airticketStockService.findFilesByAgentIdAndActivityId(agentId, srcActivityId);
        model.addAttribute("activityReserveFileList", activityReserveFileList);
        model.addAttribute("srcActivityId", srcActivityId);
        model.addAttribute("agentId", agentId);

        return "modules/stock/airticket/uploadform";
    } 

    /**
     * 上传支付凭证
     */
    @RequestMapping(value="upload")
    public String upload(@RequestParam("srcActivityId") Long srcActivityId,@RequestParam("agentId") Long agentId,@RequestParam("reserveOrderId") Long reserveOrderId,
                         @RequestParam(value = "payVoucher", required = false) List<MultipartFile> files, Model model, RedirectAttributes redirectAttributes) {
        DocInfo docInfo =null;
        if(files!=null){
            try {
                int count = 0;
                for(MultipartFile file:files){
                    String fileName = file.getOriginalFilename();
                    if(StringUtils.isNotBlank(fileName)){
                        ++count;
                        docInfo = new DocInfo();
                        //	        //保存
                        String path = FileUtils.uploadFile(file.getInputStream(), fileName);
                        docInfo.setDocName(fileName);
                        docInfo.setDocPath(path);
                        //	            //保存附件表数据
                        AirticketReserveFile actReserve = new AirticketReserveFile();
                        actReserve.setAirticketActivityId(srcActivityId);
                        actReserve.setAgentId(agentId);
                        actReserve.setReserveOrderId(reserveOrderId);
                        actReserve.setFileName(fileName);
                        airticketStockService.saveOrderPay(docInfo,actReserve);
                    }
                }
                if(count>0){
                    addMessage(redirectAttributes, "已成功上传 "+count+" 个文件!");
                }
            } catch (Exception e) {
                logger.error("error:",e);
                addMessage(redirectAttributes, "上传文件失败!");
            }
        }
        //return "redirect:"+Global.getAdminPath()+"/stock/manager/airticket/uploadform?srcActivityId="+srcActivityId+"&agentId="+agentId;
        return "redirect:"+ Global.getAdminPath()+"/stock/manager/airticket/detail/"+ srcActivityId;
    }


    /**
     * 获取切位
     * @param activityGroupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value="getReserveByGroupId")
    public List<AirticketActivityReserve> getReserveByGroupId(@RequestParam(value = "airticketId") Long airticketId){
        List<AirticketActivityReserve> activityGroupReserveList=airticketStockService.findReserve(airticketId);
        List<AirticketActivityReserve> activityGroupReserveListReturn=new LinkedList<AirticketActivityReserve>();
        for(AirticketActivityReserve activityGroupReserve:activityGroupReserveList){
            if(activityGroupReserve.getLeftpayReservePosition()>0){
                activityGroupReserveListReturn.add(activityGroupReserve);
            }
        }       
        return activityGroupReserveListReturn;
    }

    /**
     * 归还切位
     * @param activityGroupId
     * @param agentId
     * @param @param agentId
     * @param agentId
     * @param fontMoneyBackAmount
     * @return
     */
    @ResponseBody
    @RequestMapping(value="returnReserve")
    public String returnReserve(Model model, HttpServletRequest request,
                                @RequestParam Long airticketId,@RequestParam Long agentId,@RequestParam Integer reserveBackAmount,
                                @RequestParam Integer fontMoneyBackAmount) {
        //Long activityGroupId = Long.parseLong(request.getParameter("activityGroupId"));
        // reserveBackAmount=Integer.parseInt(request.getParameter("reserveBackAmount"));
    	String returnRemark=request.getParameter("returnRemark");
		if (returnRemark==null) returnRemark="";
//		System.out.println("CC"+returnRemark);
        model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
        try {
            airticketStockService.returnReserve(airticketId,reserveBackAmount,fontMoneyBackAmount,agentId,request,returnRemark);
            return "success";
        } catch (Exception e) {
            logger.error("归还余位失败，团期ID：" + airticketId);
            return "fail";
        }
    }
    
    /**
     * 跳转到机票批量切位页面
     * @Description: 
     * @param @param model
     * @param @param request
     * @param @param response
     * @param @return   
     * @return String  
     * @throws
     * @author majiancheng
     * @date 2015-12-1 下午8:21:56
     */
    @RequiresPermissions("stock:airticket:batchreserve")
    @RequestMapping(value = "batchReceiveInfo")
	public String batchReceiveInfo(Model model,HttpServletRequest request,HttpServletResponse response){
    	String channels = request.getParameter("channels");
		if(StringUtils.isNotEmpty(channels)) {
			List<String> agentInfoIdList = Arrays.asList(channels.split(","));
			List<Agentinfo> agentinfos = agentinfoService.findAgentByIdsWithSaler(agentInfoIdList);
			model.addAttribute("agentinfos", agentinfos);
			
			//初始化页面所需的渠道json信息{channelName:"西安国旅 凯德广场门市部",channelCode:319,salerName:"张三"}
			model.addAttribute("agentJsonInfos", agentinfoService.getAgentinfoJsonBean(agentinfos));
		}
		
		model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
		
		List<Dict> payTypes = DictUtils.getDictList(Context.ORDER_PAYTYPE);
		model.addAttribute("payTypes", payTypes);
		model.addAttribute("currDate", new Date());
		
    	return "modules/stock/airticket/batchReceiveInfo";
    }
    
    /**
     * 机票批量切位操作
     * @Description: 
     * @param @param model
     * @param @param request
     * @param @param response
     * @param @return   
     * @return String  
     * @throws
     * @author majiancheng
     * @date 2015-12-1 下午9:24:30
     */
    @ResponseBody
    @RequestMapping(value = "batchReceive")
	public String batchReceive(Model model,HttpServletRequest request,HttpServletResponse response) {
		String reserveJsonData = request.getParameter("reserveJsonData");
		String uploadJsonData = request.getParameter("uploadJsonData");
		Map<String, String> data = new HashMap<String, String>();
		
		try{
			data = airticketStockService.batchReceive(reserveJsonData, uploadJsonData);
		} catch(Exception e) {
			if((e.getMessage().endsWith("此团期没有足够的余位")) || (e.getMessage().endsWith("有产品团期不存在"))) {
				data.put("result", "2");
				data.put("message", e.getMessage());
			} else {
				data.put("result", "3");
				data.put("message", "系统出现异常，请重试！");
			}
			
			e.printStackTrace();
		}
		
		return JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd");
	}
	
	/**
	 * 批量归还切位页面信息
	 * @Description: 
	 * @param @param model
	 * @param @param request
	 * @param @param response
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-1 下午9:24:42
	 */
    @RequiresPermissions("stock:airticket:batchreserve")
	@RequestMapping(value = "batchReturnReceiveInfo")
	public String batchReturnReceiveInfo(Model model,HttpServletRequest request,HttpServletResponse response) {
		String channels = request.getParameter("channels");
		if(StringUtils.isNotEmpty(channels)) {
			List<String> agentInfoIdList = Arrays.asList(channels.split(","));
			List<Agentinfo> agentinfos = agentinfoService.findAgentByIdsWithSaler(agentInfoIdList);
			model.addAttribute("agentinfos", agentinfos);
			
			//初始化页面所需的渠道json信息{channelName:"西安国旅 凯德广场门市部",channelCode:319,salerName:"张三"}
			model.addAttribute("agentJsonInfos", agentinfoService.getAgentinfoJsonBean(agentinfos));
		}
		
		model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
		
		return "modules/stock/airticket/batchReturnReceiveInfo";
	}
	
	/**
	 * 切位时弹出添加团期弹出框
	 * @Description: 
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-1 下午9:24:58
	 */
	@RequestMapping(value = "getProductGroupPage")
	public String getProductGroupPage(Model model,HttpServletRequest request,HttpServletResponse response) {
		String selectedProducts = request.getParameter("selectedProducts");
		model.addAttribute("selectedProducts", selectedProducts);
		
		//切位和归还切位状态标识
		String source = request.getParameter("source");
		model.addAttribute("source", source);
		
		// 航空公司
		model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));
		
		//渠道商id
		String agentId = request.getParameter("agentId");
		model.addAttribute("agentId", agentId);
		
		return "modules/stock/airticket/productGroup";
	}
}
