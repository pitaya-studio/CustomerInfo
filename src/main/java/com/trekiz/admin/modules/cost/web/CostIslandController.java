package com.trekiz.admin.modules.cost.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.mail.SendMailUtil;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.word.WordDownLoadUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.AbstractSpecificCost;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.entity.CostRecordHotel;
import com.trekiz.admin.modules.cost.entity.CostRecordIsland;
import com.trekiz.admin.modules.cost.entity.HotelGroupView;
import com.trekiz.admin.modules.cost.entity.IslandGroupView;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.cost.service.CostIslandService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.island.dao.IslandDao;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupMealService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupRoomService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.supplier.entity.Bank;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.entity.SupplierName;
import com.trekiz.admin.modules.supplier.repository.BankDao;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.SysCompanyDictViewDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AirportInfoService;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;

import freemarker.template.TemplateException;

@Controller
@RequestMapping(value = "${adminPath}/cost/island")
public class CostIslandController extends BaseController {
		
	private static final Log LOG = LogFactory.getLog(CostManagerController.class);
	
	private static int COSTREVIEW_FLOWTYPE=15; //成本审核的 flowType
	private static int PAYREVIEW_FLOWTYPE=18; //付款审核的 flowType
	
	@Autowired	
	private ActivityIslandService activityIslandService;
	@Autowired
	private UserDao userDao;
	@Autowired	
	private ActivityHotelService activityHotelService;
	
	@Autowired	
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;	
	@Autowired	
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired	
	private HotelService hotelService;
	@Autowired	
	private IslandService islandService;
	
	@Autowired
	private ActivityIslandGroupRoomService activityIslandGroupRoomService;
	
	@Autowired
	private ActivityIslandGroupMealService activityIslandGroupMealService;
	
	@Autowired
	private SysGeographyService sysGeographyService;

	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private CostIslandService costIslandService;
	
	@Autowired
	private ReviewService reviewService;
		
	@Autowired
	private SupplierInfoService supplierInfoService;
	
	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private IslandDao islandDao;
	
	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DictService dictService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private VisaProductsDao visaProductsDao;
	
	@Autowired
	private ActivityGroupDao activityGroupDao;
	
	@Autowired
	private RefundService refundService;	
	
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;

	@Autowired
	private CostRecordIslandDao costRecordIslandDao;
	
	@Autowired
	private CostRecordHotelDao costRecordHotelDao;
	
	@Autowired
	private DocInfoService docInfoService;

	@Autowired
	private BankDao bankDao;
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	/*
	@ModelAttribute
	public TravelActivity get(@RequestParam(required=false) Long id) {
		if(id!=null){
			return travelActivityService.findById(id);
		}else
			return new TravelActivity();
	} */
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 148;
	}
	
	
	/**
	 * 海岛成本录入列表页
	 * @return
	 */   
   @RequestMapping(value="list")
   public String list(@ModelAttribute ActivityIslandGroup activityIslandGroup,HttpServletRequest request, HttpServletResponse response,
		  Model model){		
	   String review = request.getParameter("review");	
	   String activityName = request.getParameter("activityName");	
	  	   
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
				
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		String groupCode=request.getParameter("groupCode");	
		Long supplierId = null;
		if(StringUtils.isNotBlank(request.getParameter("supplierId"))){
			try {
				supplierId = StringUtils.toLong(request.getParameter("supplierId"));
			} catch (NumberFormatException e) {
			}
		}		
		Long companyId = UserUtils.getUser().getCompany().getId();			
		
		Page<IslandGroupView> page= costIslandService.findIslandGroupView(new Page<IslandGroupView>(request, response), activityIslandGroup, 
				activityName, supplierId, companyId,  common);
		 // 鼎鸿假期添加筛选条件  for 0416 by jinxin.gao
//	    boolean DHJQ = false;
//	    if(Context.SUPPLIER_UUID_DHJQ.equals(user.getCompany().getUuid())){
//		   DHJQ = true;
//	    }
//	    model.addAttribute("DHJQ", true);
//	    model.addAttribute("commitType", commitType);		
		model.addAttribute("supplierList",supplierInfoService.findSupplierInfoByCompanyId(companyId));
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("activityIslandGroup",activityIslandGroup);
		model.addAttribute("activityName",activityName);
        model.addAttribute("page", page);
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);       
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));		
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		model.addAttribute("review", review);
		
		Menu menu = departmentService.getMenuByUrl(request);
		if (menu != null) {
			request.setAttribute("_m", menu.getParent().getParent() != null ? menu.getParent().getParent().getId() : null);
			request.setAttribute("_mc", menu.getParent().getId());
		}
		return "modules/cost/islandList";
	}	 
   
   
	/**
	 * 酒店录入成本的列表页
	 * @return
	 */   
  @RequestMapping(value="hotel")
  public String hotel(@ModelAttribute ActivityHotelGroup activityHotelGroup,HttpServletRequest request, HttpServletResponse response,
		  Model model){		
	   String review = request.getParameter("review");	
	   String activityName = request.getParameter("activityName");	
	  	   
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
				
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		String groupCode=request.getParameter("groupCode");	
		Long supplierId = null;
		if(StringUtils.isNotBlank(request.getParameter("supplierId"))){
			try {
				supplierId = StringUtils.toLong(request.getParameter("supplierId"));
			} catch (NumberFormatException e) {
			}
		}		
		Long companyId = UserUtils.getUser().getCompany().getId();			
		
		
		Page<HotelGroupView> page= costIslandService.findHotelGroupView(new Page<HotelGroupView>(request, response), activityHotelGroup, 
				activityName, supplierId, companyId,  common);
				
		model.addAttribute("supplierList",supplierInfoService.findSupplierInfoByCompanyId(companyId));
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("activityHotelGroup",activityHotelGroup);
		model.addAttribute("activityName",activityName);
       model.addAttribute("page", page);
       model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
       model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);       
       model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));		
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		model.addAttribute("review", review);
		
		Menu menu = departmentService.getMenuByUrl(request);
		if (menu != null) {
			request.setAttribute("_m", menu.getParent().getParent() != null ? menu.getParent().getParent().getId() : null);
			request.setAttribute("_mc", menu.getParent().getId());
		}
		return "modules/cost/hotelList";
	}	 
  
  
  /**
	 * 获得当前步骤加载页面
	 * @param current
	 * @param activityId
	 * @param groupId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
  @RequestMapping(value="islandDetail/{activityIslandUuid}/{activityIslandGroupUuid}/{typeId}", method=RequestMethod.GET)
	public String islandDetail(@PathVariable(value="activityIslandUuid") String activityIslandUuid, 
			@PathVariable(value="activityIslandGroupUuid") String activityIslandGroupUuid,
			@PathVariable(value="typeId") Integer typeId,Model model,
			HttpServletRequest request, HttpServletResponse response){
		//String from = request.getParameter("from");
		
		ActivityIsland activityIsland = activityIslandService.getByUuid(activityIslandUuid);
		Integer deptId=activityIsland.getDeptId();
		if (deptId==null){
			deptId=0;
		}
		//获取团期信息
		ActivityIslandGroup activityGroup = activityIslandGroupService.getByUuid(activityIslandGroupUuid);
		// 岛屿
		Island island= islandDao.getByUuid(activityIsland.getIslandUuid());
		model.addAttribute("island", island);
		// 酒店
		Hotel hotel = hotelService.getByUuid(activityIsland.getHotelUuid());
		model.addAttribute("hotel", hotel);
		// 参考航班
		ActivityIslandGroupAirline activityIslandGroupAirline = activityIslandGroupAirlineService.getByactivityIslandGroup(activityGroup.getUuid()).get(0);
		model.addAttribute("activityIslandGroupAirline", activityIslandGroupAirline);
				
		Long companyId= UserUtils.getUser().getCompany().getId();	
		/*
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";		
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));//预计总毛利
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));  //预计总成本
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));//预计总收入
		Object budgetrefund = costManageService.getRefundSum(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costManageService.getRefundSum(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund); */
		model.addAttribute("deptId",deptId); 
		model.addAttribute("companyId",companyId);
		model.addAttribute("activityIslandUuid", activityIslandUuid);
		model.addAttribute("activityIslandGroupUuid", activityIslandGroupUuid);
		
		//是否计调职务
		model.addAttribute("isOperator",reviewService.checkJobType(3,4));
		//是否操作职务
		model.addAttribute("isOpt",reviewService.checkJobType(5));
		//model.addAttribute("costLog",stockDao.findCostRecordLog(activityIslandGroupUuid, typeId));
        Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
				model.addAttribute("costAutoPass",costAutoPass);
		}else{
				model.addAttribute("costAutoPass","0");
		}
		if(activityGroup != null && activityIsland != null){			
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			orderList = costIslandService.getIslandOrderInfos(orderList, activityIslandGroupUuid); 
			
			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
			model.addAttribute("activityIsland", activityIsland);
			model.addAttribute("activityGroup", activityGroup);
			model.addAttribute("orderList", orderList.getList());
			//model.addAttribute("type", from);
			model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
			List<CostRecordIsland> budgetOutList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> budgetInList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> actualOutList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> actualInList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> otherCostList = new ArrayList<CostRecordIsland>();
			
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);			
						
			//model.addAttribute("typename", dict.getLabel());			
			
			model.addAttribute("curlist", currencylist);			
			List<String> supplyTypeList = new ArrayList<String>();
			if(companyId==68){		   
			    supplyTypeList.add("1");
			    supplyTypeList.add("5");
			    supplyTypeList.add("8");
				List<Dict> supplytypelist=dictService.findByType("supplier_type",supplyTypeList);
				model.addAttribute("supplytypelist", supplytypelist);
			} else {			   
			    supplyTypeList.add("11");
				List<Dict> supplytypelist=dictService.findByType("supplier_type",supplyTypeList);
				model.addAttribute("supplytypelist", supplytypelist);
			}	
			
			 List<Map<String, Object>> incomeList=costIslandService.getIslandForCastList(activityIslandGroupUuid);
			 model.addAttribute("incomeList", incomeList);
              
			List<Map<String, Object>> budgetCost =costManageService.getIslandCost(activityGroup.getUuid(),0);
			List<Map<String, Object>> actualCost=costManageService.getIslandCost(activityGroup.getUuid(),1); 
			model.addAttribute("budgetCost",budgetCost);
			model.addAttribute("actualCost",actualCost); 
			
			//境内成本预算			
			 budgetInList = costIslandService.findCostIslandList(activityIslandGroupUuid, 0,0);	
			 //境外成本预算
			budgetOutList = costIslandService.findCostIslandList(activityIslandGroupUuid, 0,1);
			 
			//境内实际成本	
			actualInList = costIslandService.findCostIslandList(activityIslandGroupUuid, 1,0);
			//境外实际成本	
			actualOutList = costIslandService.findCostIslandList(activityIslandGroupUuid, 1,1);		
			
			// 其它
			otherCostList = costIslandService.findCostIslandList(activityIslandGroupUuid, 2, 0);	// 第三个参数overseas默认设置为0
			
			model.addAttribute("budgetInList", budgetInList);
			model.addAttribute("budgetOutList", budgetOutList);
				
			model.addAttribute("actualInList", actualInList);
			model.addAttribute("actualOutList", actualOutList);	
			
			for (int i = 0; i < otherCostList.size(); i++) {
				Object obj1 = costIslandService.getPayedMoneyForIsland(otherCostList.get(i).getId());
				otherCostList.get(i).setPayedMoney(obj1==null?"0":obj1.toString());
				Object obj2 = costIslandService.getConfirmMoneyForIsland(otherCostList.get(i).getId());
				otherCostList.get(i).setConfirmMoney(obj2==null?"0":obj2.toString());
			}
			model.addAttribute("otherCostList", otherCostList);
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		
		Object budgetrefund = costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund") == null ? "0"
				: costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund") == null ? "0"
				: costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);
		
		return "modules/cost/islandDetail";
	}
  
//  /**
//   * 海岛游--团队管理--查看
//   * @param activityIslandUuid
//   * @param activityIslandGroupUuid
//   * @param typeId
//   * @param model
//   * @param request
//   * @param response
//   * @return
//   */
//    @RequestMapping(value="islandRead/{activityIslandUuid}/{activityIslandGroupUuid}/{typeId}", method=RequestMethod.GET)
//	public String islandRead(@PathVariable(value="activityIslandUuid") String activityIslandUuid, 
//			@PathVariable(value="activityIslandGroupUuid") String activityIslandGroupUuid,
//			@PathVariable(value="typeId") Integer typeId,Model model,
//			HttpServletRequest request, HttpServletResponse response){
//		//String from = request.getParameter("from");
//		
//		ActivityIsland activityIsland = activityIslandService.getByUuid(activityIslandUuid);
//		Integer deptId=activityIsland.getDeptId();
//		if (deptId==null){
//			deptId=0;
//		}
//		//获取团期信息
//		ActivityIslandGroup activityGroup = activityIslandGroupService.getByUuid(activityIslandGroupUuid);
//				
//		// 国家
//		String countryName= sysGeographyService.getNameCnByUuid(activityIsland.getCountry());
//		model.addAttribute("countryName", countryName);
//		// 岛屿
//		Island island = islandService.getByUuid(activityIsland.getIslandUuid());
//		model.addAttribute("island", island);
//		// 酒店
//		Hotel hotel = hotelService.getByUuid(activityIsland.getHotelUuid());
//		model.addAttribute("hotel", hotel);
//		// 房型
//		List<ActivityIslandGroupRoom> activityIslandGroupRooms = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityGroup.getUuid());
//		model.addAttribute("activityIslandGroupRooms", activityIslandGroupRooms);
//		// 餐型
//		List<ActivityIslandGroupMeal> activityIslandGroupMeals = activityIslandGroupMealService.getByactivityIslandGroupUuid(activityGroup.getUuid());
//		model.addAttribute("activityIslandGroupMeals", activityIslandGroupMeals);
//		
//		
//		Long companyId= UserUtils.getUser().getCompany().getId();	
//		/*
//		if(costSerial==null) costSerial="";
//		if(incomeSerial==null) incomeSerial="";		
//		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));//预计总毛利
//		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));  //预计总成本
//		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));//预计总收入
//		*/
//		model.addAttribute("deptId",deptId); 
//		model.addAttribute("companyId",companyId);
//		//是否计调职务
//		model.addAttribute("isOperator",reviewService.checkJobType(3,4));
//		//是否操作职务
//		model.addAttribute("isOpt",reviewService.checkJobType(5));
//		//model.addAttribute("costLog",stockDao.findCostRecordLog(activityIslandGroupUuid, typeId));
//    
//		if(activityGroup != null && activityIsland != null){			
//			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
//			orderList.setPageNo(1);
//			orderList.setPageSize(Integer.MAX_VALUE);
//			//orderList = orderService.findOrderListByPayType(orderList, activityIslandGroupUuid); 
//			
//			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
//			model.addAttribute("activityIsland", activityIsland);
//			model.addAttribute("activityGroup", activityGroup);
//			model.addAttribute("orderList", orderList.getList());
//			//model.addAttribute("type", from);
//			model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
//			List<CostRecordIsland> budgetOutList = new ArrayList<CostRecordIsland>();
//			List<CostRecordIsland> budgetInList = new ArrayList<CostRecordIsland>();
//			List<CostRecordIsland> actualOutList = new ArrayList<CostRecordIsland>();
//			List<CostRecordIsland> actualInList = new ArrayList<CostRecordIsland>();
//			List<CostRecordIsland> otherCostList = new ArrayList<CostRecordIsland>();
//			
//			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);			
//						
//			//model.addAttribute("typename", dict.getLabel());			
//			
//			model.addAttribute("curlist", currencylist);			
//			List<String> supplyTypeList = new ArrayList<String>();
//			if(companyId==68){		   
//			    supplyTypeList.add("1");
//			    supplyTypeList.add("5");
//			    supplyTypeList.add("8");
//				List<Dict> supplytypelist=dictService.findByType("supplier_type",supplyTypeList);
//				model.addAttribute("supplytypelist", supplytypelist);
//			} else {			   
//			    supplyTypeList.add("11");
//				List<Dict> supplytypelist=dictService.findByType("supplier_type",supplyTypeList);
//				model.addAttribute("supplytypelist", supplytypelist);
//			}	
//			/*
//			 List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(activityIslandGroupUuid,typeId);
//			 List<Map<String, Object>> budgetCost=costManageService.getCost(activityIslandGroupUuid,typeId,0);
//			 List<Map<String, Object>> actualCost=costManageService.getCost(activityIslandGroupUuid,typeId,1);
//			 model.addAttribute("incomeList", incomeList);
//			 model.addAttribute("budgetCost",budgetCost);
//			 model.addAttribute("actualCost",actualCost); */		 
//			
//			//境内成本预算			
//			 budgetInList = costIslandService.findCostIslandList(activityIslandGroupUuid, 0,0);	
//			 //境外成本预算
//			budgetOutList = costIslandService.findCostIslandList(activityIslandGroupUuid, 0,1);
//			 
//			//境内实际成本	
//			actualInList = costIslandService.findCostIslandList(activityIslandGroupUuid, 1,0);
//			//境外实际成本	
//			actualOutList = costIslandService.findCostIslandList(activityIslandGroupUuid, 1,1);		
//			
//			// 其它
//			otherCostList = costIslandService.findCostIslandList(activityIslandGroupUuid, 2, 0);	// 第三个参数overseas默认设置为0
//			
//			model.addAttribute("budgetInList", budgetInList);
//			model.addAttribute("budgetOutList", budgetOutList);
//				
//			model.addAttribute("actualInList", actualInList);
//			model.addAttribute("actualOutList", actualOutList);	
//			
//			for (int i = 0; i < otherCostList.size(); i++) {
//				Object obj1 = costIslandService.getPayedMoneyForIsland(otherCostList.get(i).getId());
//				otherCostList.get(i).setPayedMoney(obj1==null?"0":obj1.toString());
//				Object obj2 = costIslandService.getConfirmMoneyForIsland(otherCostList.get(i).getId());
//				otherCostList.get(i).setConfirmMoney(obj2==null?"0":obj2.toString());
//			}
//			model.addAttribute("otherCostList", otherCostList);
//		}else{
//			throw new RuntimeException("产品和团期不匹配");
//		}
//		
//		Object budgetrefund = costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund") == null ? "0"
//				: costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund");
//		model.addAttribute("budgetrefund", budgetrefund);
//		Object actualrefund = costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund") == null ? "0"
//				: costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund");
//		model.addAttribute("actualrefund", actualrefund);
//		
//		return "modules/costreview/islandRead";
//	}
	

  /**
	 * 获得当前步骤加载页面
	 * @param current
	 * @param activityId
	 * @param groupId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
@RequestMapping(value="hotelDetail/{activityHotelUuid}/{activityHotelGroupUuid}/{typeId}", method=RequestMethod.GET)
	public String hotelDetail(@PathVariable(value="activityHotelUuid") String activityHotelUuid, 
			@PathVariable(value="activityHotelGroupUuid") String activityHotelGroupUuid,
			@PathVariable(value="typeId") Integer typeId,Model model,
			HttpServletRequest request, HttpServletResponse response){
		//String from = request.getParameter("from");
	    Long companyId= UserUtils.getUser().getCompany().getId();
		ActivityHotel activityHotel = activityHotelService.getByUuid(activityHotelUuid);
		Integer deptId=activityHotel.getDeptId();
		if (deptId==null){
			deptId=0;
		}
		//获取团期信息
		ActivityHotelGroup activityGroup = activityHotelGroupService.getByUuid(activityHotelGroupUuid);
		Hotel hotel= hotelService.getByUuid(activityHotel.getHotelUuid());
		Island island=islandService.getByUuid(activityHotel.getIslandUuid());
		model.addAttribute("islandName",island.getIslandName()); 	
		model.addAttribute("hotelName",hotel.getNameCn()); 
		model.addAttribute("deptId",deptId); 
		model.addAttribute("companyId",companyId);
		model.addAttribute("activityHotelUuid", activityHotelUuid);
		model.addAttribute("activityHotelGroupUuid", activityHotelGroupUuid);
		//是否计调职务
		model.addAttribute("isOperator",reviewService.checkJobType(3,4));
		//是否操作职务
		model.addAttribute("isOpt",reviewService.checkJobType(5));
		// model.addAttribute("costLog",stockDao.findCostRecordLog(activityHotelGroupUuid, typeId));
    	Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
				model.addAttribute("costAutoPass",costAutoPass);
		}else{
				model.addAttribute("costAutoPass","0");
		}
		if(activityGroup != null && activityHotel != null){			
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			 orderList = costIslandService.getHotelOrderInfos(orderList, activityHotelGroupUuid);
			model.addAttribute("orderList", orderList.getList());
			
			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
			model.addAttribute("activityHotel", activityHotel);
			model.addAttribute("activityGroup", activityGroup);
			model.addAttribute("orderList", orderList.getList());
			//model.addAttribute("type", from);
			model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
			List<CostRecordHotel> budgetOutList = new ArrayList<CostRecordHotel>();
			List<CostRecordHotel> budgetInList = new ArrayList<CostRecordHotel>();
			List<CostRecordHotel> actualOutList = new ArrayList<CostRecordHotel>();
			List<CostRecordHotel> actualInList = new ArrayList<CostRecordHotel>();
			List<CostRecordHotel> otherCostList = new ArrayList<CostRecordHotel>();			
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);		
			//model.addAttribute("typename", dict.getLabel());				
			model.addAttribute("curlist", currencylist);			
			List<String> supplyTypeList = new ArrayList<String>();
			if(companyId==68){		   
			    supplyTypeList.add("1");
			    supplyTypeList.add("5");
			    supplyTypeList.add("8");
				List<Dict> supplytypelist=dictService.findByType("supplier_type",supplyTypeList);
				model.addAttribute("supplytypelist", supplytypelist);
			} else {			   
			    supplyTypeList.add("11");
				List<Dict> supplytypelist=dictService.findByType("supplier_type",supplyTypeList);
				model.addAttribute("supplytypelist", supplytypelist);
			}	
			
			List<Map<String, Object>> incomeList=costIslandService.getHotelForCastList(activityHotelGroupUuid);
			model.addAttribute("incomeList", incomeList);
			List<Map<String, Object>> budgetCost =costManageService.getHotelCost(activityHotelGroupUuid,0);
			List<Map<String, Object>> actualCost=costManageService.getHotelCost(activityHotelGroupUuid ,1); 
			 model.addAttribute("budgetCost",budgetCost);
			 model.addAttribute("actualCost",actualCost); 		 
			
			//境内成本预算			
			 budgetInList = costIslandService.findCostHotelList(activityHotelGroupUuid, 0,0);	
			 //境外成本预算
			budgetOutList =costIslandService.findCostHotelList(activityHotelGroupUuid, 0,1);			 
			//境内实际成本	
			actualInList = costIslandService.findCostHotelList(activityHotelGroupUuid, 1,0);
			//境外实际成本	
			actualOutList = costIslandService.findCostHotelList(activityHotelGroupUuid, 1,1);				
			// 其它
			otherCostList = costIslandService.findCostHotelList(activityHotelGroupUuid, 2, 0);	// 第三个参数overseas默认设置为0
			
			model.addAttribute("budgetInList", budgetInList);
			model.addAttribute("budgetOutList", budgetOutList);
				
			model.addAttribute("actualInList", actualInList);
			model.addAttribute("actualOutList", actualOutList);	
			
			for (int i = 0; i < otherCostList.size(); i++) {
				Object obj1 = costIslandService.getPayedMoneyForHotel(otherCostList.get(i).getId());
				otherCostList.get(i).setPayedMoney(obj1==null?"0":obj1.toString());
				Object obj2 = costIslandService.getConfirmMoneyForHotel(otherCostList.get(i).getId());
				otherCostList.get(i).setConfirmMoney(obj2==null?"0":obj2.toString());
			}
			model.addAttribute("otherCostList", otherCostList);
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		
		Object budgetrefund = costIslandService.getRefundSumForHotel(activityHotelGroupUuid, 0, typeId).get(0).get("totalRefund") == null ? "0"
				: costIslandService.getRefundSumForHotel(activityHotelGroupUuid, 0, typeId).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costIslandService.getRefundSumForHotel(activityHotelGroupUuid, 1, typeId).get(0).get("totalRefund") == null ? "0"
				: costIslandService.getRefundSumForHotel(activityHotelGroupUuid, 1, typeId).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);
		
		return "modules/cost/hotelDetail";
	}

//	/**
//	 * 酒店--团队管理--查看
//	 * @param activityHotelUuid
//	 * @param activityHotelGroupUuid
//	 * @param typeId
//	 * @param model
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "hotelRead/{activityHotelUuid}/{activityHotelGroupUuid}/{typeId}", method = RequestMethod.GET)
//	public String hotelRead(
//			@PathVariable(value = "activityHotelUuid") String activityHotelUuid,
//			@PathVariable(value = "activityHotelGroupUuid") String activityHotelGroupUuid,
//			@PathVariable(value = "typeId") Integer typeId, Model model,
//			HttpServletRequest request, HttpServletResponse response) {
//		// String from = request.getParameter("from");
//		Long companyId = UserUtils.getUser().getCompany().getId();
//		ActivityHotel activityHotel = activityHotelService.getByUuid(activityHotelUuid);
//		Integer deptId = activityHotel.getDeptId();
//		if (deptId == null) {
//			deptId = 0;
//		}
//		// 获取团期信息
//		ActivityHotelGroup activityGroup = activityHotelGroupService.getByUuid(activityHotelGroupUuid);
//		Hotel hotel = hotelService.getByUuid(activityHotel.getHotelUuid());
//		Island island = islandService.getByUuid(activityHotel.getIslandUuid());
//		model.addAttribute("islandName", island.getIslandName());
//		model.addAttribute("hotelName", hotel.getNameCn());
//		model.addAttribute("deptId", deptId);
//		model.addAttribute("companyId", companyId);
//		// 是否计调职务
//		model.addAttribute("isOperator", reviewService.checkJobType(3, 4));
//		// 是否操作职务
//		model.addAttribute("isOpt", reviewService.checkJobType(5));
//		// model.addAttribute("costLog",stockDao.findCostRecordLog(activityHotelGroupUuid,
//		// typeId));
//
//		if (activityGroup != null && activityHotel != null) {
//			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
//			orderList.setPageNo(1);
//			orderList.setPageSize(Integer.MAX_VALUE);
//			// orderList = orderService.findOrderListByPayType(orderList,
//			// activityHotelGroupUuid);
//
//			model.addAttribute("agentinfoList",	agentinfoService.findAllAgentinfo());
//			model.addAttribute("activityHotel", activityHotel);
//			model.addAttribute("activityGroup", activityGroup);
//			model.addAttribute("orderList", orderList.getList());
//			// model.addAttribute("type", from);
//			model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
//			List<CostRecordHotel> budgetOutList = new ArrayList<CostRecordHotel>();
//			List<CostRecordHotel> budgetInList = new ArrayList<CostRecordHotel>();
//			List<CostRecordHotel> actualOutList = new ArrayList<CostRecordHotel>();
//			List<CostRecordHotel> actualInList = new ArrayList<CostRecordHotel>();
//			List<CostRecordHotel> otherCostList = new ArrayList<CostRecordHotel>();
//			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
//			// model.addAttribute("typename", dict.getLabel());
//			model.addAttribute("curlist", currencylist);
//			List<String> supplyTypeList = new ArrayList<String>();
//			if (companyId == 68) {
//				supplyTypeList.add("1");
//				supplyTypeList.add("5");
//				supplyTypeList.add("8");
//				List<Dict> supplytypelist = dictService.findByType("supplier_type", supplyTypeList);
//				model.addAttribute("supplytypelist", supplytypelist);
//			} else {
//				supplyTypeList.add("11");
//				List<Dict> supplytypelist = dictService.findByType("supplier_type", supplyTypeList);
//				model.addAttribute("supplytypelist", supplytypelist);
//			}
//			/*
//			 * List<Map<String, Object>>
//			 * incomeList=costManageService.getRefunifoForCastList
//			 * (activityHotelGroupUuid,typeId); List<Map<String, Object>>
//			 * budgetCost
//			 * =costManageService.getCost(activityHotelGroupUuid,typeId,0);
//			 * List<Map<String, Object>>
//			 * actualCost=costManageService.getCost(activityHotelGroupUuid
//			 * ,typeId,1); model.addAttribute("incomeList", incomeList);
//			 * model.addAttribute("budgetCost",budgetCost);
//			 * model.addAttribute("actualCost",actualCost);
//			 */
//
//			// 境内成本预算
//			budgetInList = costIslandService.findCostHotelList(activityHotelGroupUuid, 0, 0);
//			// 境外成本预算
//			budgetOutList = costIslandService.findCostHotelList(activityHotelGroupUuid, 0, 1);
//			// 境内实际成本
//			actualInList = costIslandService.findCostHotelList(activityHotelGroupUuid, 1, 0);
//			// 境外实际成本
//			actualOutList = costIslandService.findCostHotelList(activityHotelGroupUuid, 1, 1);
//			// 其它
//			otherCostList = costIslandService.findCostHotelList(activityHotelGroupUuid, 2, 0); // 第三个参数overseas默认设置为0
//
//			model.addAttribute("budgetInList", budgetInList);
//			model.addAttribute("budgetOutList", budgetOutList);
//
//			model.addAttribute("actualInList", actualInList);
//			model.addAttribute("actualOutList", actualOutList);
//
//			for (int i = 0; i < otherCostList.size(); i++) {
//				Object obj1 = costIslandService.getPayedMoneyForHotel(otherCostList.get(i).getId());
//				otherCostList.get(i).setPayedMoney(obj1 == null ? "0" : obj1.toString());
//				Object obj2 = costIslandService.getConfirmMoneyForHotel(otherCostList.get(i).getId());
//				otherCostList.get(i).setConfirmMoney(obj2 == null ? "0" : obj2.toString());
//			}
//			model.addAttribute("otherCostList", otherCostList);
//		} else {
//			throw new RuntimeException("产品和团期不匹配");
//		}
//
//		Object budgetrefund = costIslandService
//				.getRefundSumForHotel(activityHotelGroupUuid, 0, typeId).get(0)
//				.get("totalRefund") == null ? "0" : costIslandService
//				.getRefundSumForHotel(activityHotelGroupUuid, 0, typeId).get(0)
//				.get("totalRefund");
//		model.addAttribute("budgetrefund", budgetrefund);
//		Object actualrefund = costIslandService
//				.getRefundSumForHotel(activityHotelGroupUuid, 1, typeId).get(0)
//				.get("totalRefund") == null ? "0" : costIslandService
//				.getRefundSumForHotel(activityHotelGroupUuid, 1, typeId).get(0)
//				.get("totalRefund");
//		model.addAttribute("actualrefund", actualrefund);
//
//		return "modules/costreview/hotelRead";
//	}

   /*海岛游，成本录入表单*/
 	@RequestMapping(value = "addIslandCost/{activityIslandUuid}/{activityUuid}/{budgetType}/{typeId}/{deptId}")
 	public String addIslandCost(@PathVariable(value="activityIslandUuid") String activityIslandUuid,@PathVariable(value="activityUuid") String activityUuid,@PathVariable(value="budgetType") Integer budgetType, 
 			@PathVariable(value="typeId") Integer typeId,@PathVariable(value="deptId") Long deptId,Model model) {
 		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
			if( costAutoPass==null ){
				model.addAttribute("costAutoPass", "0");
			}else{
				model.addAttribute("costAutoPass", costAutoPass.toString());
			}	
 		Long companyId= UserUtils.getUser().getCompany().getId();			
 		List<Currency> currencylist = currencyService.findCurrencyList(companyId);		
 		model.addAttribute("activityUuid", activityUuid);
 		model.addAttribute("activityIslandUuid", activityIslandUuid);
 		model.addAttribute("typeId", typeId);
 		model.addAttribute("budgetType", budgetType);
 		
 		model.addAttribute("curlist", currencylist);
 		model.addAttribute("companyId",companyId);
 		List<Long> deptList=  costManageService.getDeptList(UserUtils.getUser().getId()); 		
 		model.addAttribute("deptSize", deptList.size());
 		model.addAttribute("deptId", deptId);		
 		
 		List<String> supplyTypeList = new ArrayList<String>();
 		if(companyId==68){		   
 		    supplyTypeList.add("1");
 		    supplyTypeList.add("5");
 		    supplyTypeList.add("8");
 		   List<SysCompanyDictView> supplytypelist=sysCompanyDictViewDao.findByType("travel_agency_type",companyId,supplyTypeList);
 			model.addAttribute("supplytypelist", supplytypelist);
 		} else {			   
 		    supplyTypeList.add("11");
 		   List<SysCompanyDictView> supplytypelist=sysCompanyDictViewDao.findByType("travel_agency_type",companyId,supplyTypeList);
 			model.addAttribute("supplytypelist", supplytypelist);
 		}		
// 		List<Agentinfo> agentinfo=agentinfoService.findAllAgentinfo(companyId);
// 		model.addAttribute("agentinfo", agentinfo);
 		
 		
 		if (budgetType == 2) {
			return "modules/cost/addIslandOtherCost";
		} else {
			
			return "modules/cost/addIslandCost";
			
		}
 		}
 	
 	/*酒店，成本录入表单*/
	@RequestMapping(value = "addHotelCost/{activityHotelUuid}/{groupUuid}/{budgetType}/{typeId}/{deptId}")
	public String addHotelCost(
			@PathVariable(value = "activityHotelUuid") String activityHotelUuid,
			@PathVariable(value = "groupUuid") String groupUuid,
			@PathVariable(value = "budgetType") Integer budgetType,
			@PathVariable(value = "typeId") Integer typeId,
			@PathVariable(value = "deptId") Long deptId, Model model) {

		Long companyId = UserUtils.getUser().getCompany().getId();		
 		List<Long> deptList=  costManageService.getDeptList(UserUtils.getUser().getId());
 		model.addAttribute("deptSize", deptList.size());
 		model.addAttribute("deptId", deptId);
 		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
			if( costAutoPass==null ){
				model.addAttribute("costAutoPass", "0");
			}else{
				model.addAttribute("costAutoPass", costAutoPass.toString());
			}
 		List<Currency> currencylist = currencyService.findCurrencyList(companyId);
		model.addAttribute("groupUuid", groupUuid);
		model.addAttribute("activityHotelUuid", activityHotelUuid);
		model.addAttribute("typeId", typeId);
		model.addAttribute("budgetType", budgetType);		
		model.addAttribute("curlist", currencylist);
		model.addAttribute("companyId", companyId);
		List<String> supplyTypeList = new ArrayList<String>();
		if (companyId == 68) {
			supplyTypeList.add("1");
			supplyTypeList.add("5");
			supplyTypeList.add("8");
			List<SysCompanyDictView> supplytypelist=sysCompanyDictViewDao.findByType("travel_agency_type",companyId,supplyTypeList);
			model.addAttribute("supplytypelist", supplytypelist);
		} else {
			supplyTypeList.add("11");
			List<SysCompanyDictView> supplytypelist=sysCompanyDictViewDao.findByType("travel_agency_type",companyId,supplyTypeList);
			model.addAttribute("supplytypelist", supplytypelist);
		}
		List<Map<String, Object>> agentinfo = agentinfoService.findAllAgentinfo(companyId);
		model.addAttribute("agentinfo", agentinfo);

		if (budgetType == 2) {
			return "modules/cost/addHotelOtherCost";
		} else {
			return "modules/cost/addHotelCost";
		}
	}
 	
    /*环球行，批量提交审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="saveCostIslandList")
  	public String saveCostIslandList(@RequestParam(required=true) String costList){
 		String tmpList[]=costList.split(",");
 		Long companyId= UserUtils.getUser().getCompany().getId();
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && ! costId.equals("0")){ 				
 				 CostRecordIsland costRecord=costRecordIslandDao.findOne(Long.valueOf(costId));
 				 Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
 				 if( costAutoPass!=null && costAutoPass==1){
 					 costRecord.setReview(2);
 				 }else{
 					 costRecord.setReview(1);
 				 }
 				 costRecord.setNowLevel(1); 				
 				if( costAutoPass!=null && costAutoPass==1 && costRecord.getBudgetType()==0){
 					CostRecordIsland cr=costIslandService.copyCostRecordIsland(costRecord);
 					long userId = UserUtils.getUser().getId();
 					List<Long> deptList=  costManageService.getDeptList(userId);
 					 //成本审核流程Id=15
 					Long reviewCompanyId=costManageService.findReviewCompanyId(companyId,15,deptList);
 					costRecord.setReview(2);
 			 		cr.setReviewCompanyId(reviewCompanyId); 
 			 		
 			 		//付款审核流程Id=18
 			 		reviewCompanyId=costManageService.findReviewCompanyId(companyId,18,deptList);
 			 		cr.setPayReviewCompanyId(reviewCompanyId); 
 			 		cr.setPayNowLevel(1);
 			 		cr.setPayReview(4);
 			 		
 			 		cr.setBudgetType(1);
 					cr.setNowLevel(4);
 					cr.setReview(2);
 					costManageService.saveCostRecordIsland(cr); 
 					
 				}
 				 
 			 	costRecordIslandDao.save(costRecord); 				
 				//costRecordDao.updateCostRecord(Long.valueOf(costId),1); 			
 			}
 		}
  		return "";
  	} 
 	
    /*环球行，批量提交审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="saveCostHotelList")
  	public String saveCostHotelList(@RequestParam(required=true) String costList){
 		String tmpList[]=costList.split(",");
 		Long companyId= UserUtils.getUser().getCompany().getId();
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && ! costId.equals("0")){ 				
 				 CostRecordHotel costRecord=costRecordHotelDao.findOne(Long.valueOf(costId));
 				Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
 				if( costAutoPass!=null && costAutoPass==1){
 					 costRecord.setReview(2);
 				}else{
 					 costRecord.setReview(1);
 				}
 				costRecord.setNowLevel(1); 				
 				if( costAutoPass!=null && costAutoPass==1 && costRecord.getBudgetType()==0){
 					CostRecordHotel cr=costIslandService.copyCostRecordHotel(costRecord);
 					long userId = UserUtils.getUser().getId();
 					List<Long> deptList=  costManageService.getDeptList(userId);
 					 //成本审核流程Id=15
 					Long reviewCompanyId=costManageService.findReviewCompanyId(companyId,15,deptList);
 					costRecord.setReview(2);
 			 		cr.setReviewCompanyId(reviewCompanyId); 
 			 		
 			 		//付款审核流程Id=18
 			 		reviewCompanyId=costManageService.findReviewCompanyId(companyId,18,deptList);
 			 		cr.setPayReviewCompanyId(reviewCompanyId); 
 			 		cr.setPayNowLevel(1);
 			 		cr.setPayReview(4); 			 		
 			 		cr.setBudgetType(1);
 					cr.setNowLevel(4);
 					cr.setReview(2);
 					costManageService.saveCostRecordHotel(cr);  					
 				}
 				 
 			 	costRecordHotelDao.save(costRecord); 				
 				//costRecordDao.updateCostRecord(Long.valueOf(costId),1); 			
 			}
 		}
  		return "";
  	} 
 
    /*环球行，批量提交 付款审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="savePayIslandList")
  	public String savePayIslandList(@RequestParam(required=true) String costList){
 		String tmpList[]=costList.split(",");
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && !"0".equals(costId)){
 				 CostRecordIsland costRecord=costRecordIslandDao.findOne(Long.valueOf(costId));
 				 Date date = new Date();
 				 costRecord.setPayReview(2);
 				 costRecord.setPayNowLevel(3);
 				 costRecord.setUpdateDate(date);
 				 costRecord.setPayApplyDate(date);
 				 costRecordIslandDao.save(costRecord);
 			}
 		}
  		return "";
  	} 
 	
 	 /*环球行，批量提交 付款审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="savePayHotelList")
  	public String savePayHotelList(@RequestParam(required=true) String costList){
 		String tmpList[]=costList.split(",");
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && !"0".equals(costId)){
 				 CostRecordHotel costRecord=costRecordHotelDao.findOne(Long.valueOf(costId));
 				 Date date = new Date();
 				 costRecord.setPayReview(2);
 				 costRecord.setPayNowLevel(3);
 				 costRecord.setUpdateDate(date);
 				 costRecord.setPayApplyDate(date);
 				 costRecordHotelDao.save(costRecord);
 			}
 		}
  		return "";
  	} 
 	
 	
      /*海岛游，保存成本录入*/
 	@RequestMapping(value="saveIslandCost")
 	public String saveIslandCost(@RequestParam(required=true) String itemname, @RequestParam(required=false) BigDecimal price,@RequestParam(required=true) String comment,
     @RequestParam(required=true) String activityIslandUuid,  @RequestParam(required=false) String activityUuid,@RequestParam(required=false) Integer typeId,@RequestParam(required=false) Integer quantity,@RequestParam(required=false) Integer currencyId,    
     @RequestParam(required=false) Integer overseas,@RequestParam(required=false) Integer supplyType,@RequestParam(required=false) Integer agentId,        
     @RequestParam(required=false) Integer first,@RequestParam(required=false) Integer supplier,@RequestParam(required=false) Integer budgetType,@RequestParam(required=true) Long deptId,
     @RequestParam(required=false) Long bank,@RequestParam(required=false) String bankname,@RequestParam(required=false) String account,@RequestParam(required=false) Integer review,
     @RequestParam(required=false)  BigDecimal rate,@RequestParam(required=false) Integer currencyAfter, @RequestParam(required=false)  BigDecimal priceAfter){	
 		Long companyId= UserUtils.getUser().getCompany().getId();
 		long userId = UserUtils.getUser().getId(); 		 
 		CostRecordIsland costRecordIsland = new CostRecordIsland();
 		costRecordIsland.setUuid(UUID.randomUUID().toString().replace("-", ""));
 		costRecordIsland.setName(itemname);		
 		costRecordIsland.setPrice(price);
 		costRecordIsland.setQuantity(quantity); 	
 			
 		if (bank != null) {
			if (bank == (long) -1) { /* 没有默认银行账号 */
				costRecordIsland.setBankName(bankname);
				costRecordIsland.setBankAccount(account);
			} else {
				Bank mybank = bankDao.findOne(bank);
				costRecordIsland.setBankName(mybank.getBankName());
				costRecordIsland.setBankAccount(mybank.getBankAccountCode());
			}
		}
 		costRecordIsland.setCurrencyAfter(currencyAfter);
 		costRecordIsland.setRate(rate);
 		costRecordIsland.setPriceAfter(priceAfter);
 		costRecordIsland.setSupplierType(first);
 		costRecordIsland.setNowLevel(1);
 		costRecordIsland.setBudgetType(budgetType);
 		//costRecord.setDeptId(deptId); 
 		costRecordIsland.setDelFlag("0");
 		costRecordIsland.setCreateBy(UserUtils.getUser());
 		costRecordIsland.setUpdateBy(UserUtils.getUser().getId());
 		costRecordIsland.setCreateDate(new Date());
 		costRecordIsland.setUpdateDate(new Date());
 		costRecordIsland.setPayApplyDate(new Date());
 		List<Long> deptList=  costManageService.getDeptList(userId);
 		if(deptList.size()==0){
 			System.out.println("没有给用户配置部门");
 		}
 		
 		Long reviewCompanyId=(long)0;
 		Long payReviewCompanyId=(long)0;
 		if(deptId==0){ //部门为空
 			reviewCompanyId=costManageService.findReviewCompanyId(companyId,COSTREVIEW_FLOWTYPE,deptList);
 			payReviewCompanyId=costManageService.findReviewCompanyId(companyId,PAYREVIEW_FLOWTYPE,deptList);
 		}else{
 			Department department= departmentDao.findOne(deptId);
 			long parentDept=department.getParentId();
 			List<Long> listCompany = new ArrayList<Long>();
 			listCompany = reviewCompanyDao.findReviewCompanyId(companyId,COSTREVIEW_FLOWTYPE,parentDept);
 	 		if(listCompany.size()>0) {
 	 			reviewCompanyId= listCompany.get(0); 			
 	 		} else{
 	 		    reviewCompanyId=costManageService.findReviewCompanyId(companyId,COSTREVIEW_FLOWTYPE,deptList);
 	 		}
 	 		listCompany = reviewCompanyDao.findReviewCompanyId(companyId,PAYREVIEW_FLOWTYPE,parentDept);
 	 		if(listCompany.size()>0) {
 	 		   payReviewCompanyId= listCompany.get(0); 			
 	 		} else{
 	 		   payReviewCompanyId=costManageService.findReviewCompanyId(companyId,PAYREVIEW_FLOWTYPE,deptList);
 	 		}
 		}
 		costRecordIsland.setReviewCompanyId(reviewCompanyId);
 		costRecordIsland.setPayReviewCompanyId(payReviewCompanyId);
 		if(budgetType==1){ 	 			
 			costRecordIsland.setPayReview(4);
 			costRecordIsland.setPayNowLevel(1); 			
 		} 		
 		costRecordIsland.setSupplyType(supplyType);
 		costRecordIsland.setOrderType(typeId); 		
 		costRecordIsland.setPayStatus(0);
 		if(supplyType==1){
 			costRecordIsland.setSupplyId(agentId);
 			Agentinfo agentinfo= new Agentinfo();
 			agentinfo=agentinfoService.findOne((long)agentId);
 			costRecordIsland.setSupplyName(agentinfo.getAgentName());
 		}else {
 			costRecordIsland.setSupplyId(supplier);
 			SupplierInfo supplierInfo = new SupplierInfo();
 			supplierInfo= supplierInfoService.findSupplierInfoById((long)supplier);
 			costRecordIsland.setSupplyName(supplierInfo.getSupplierName());
 		}

 		costRecordIsland.setActivityUuid(activityUuid); 	 		
 		if(overseas == null || "".equals(overseas)) {
 			costRecordIsland.setOverseas(0);
 		}else{
 			costRecordIsland.setOverseas(overseas);
 		}		
 		costRecordIsland.setCurrencyId(currencyId);
 		costRecordIsland.setReview(review);
 		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
 		if(costAutoPass!=null && costAutoPass==1 && review==1){
 			costRecordIsland.setReview(2);
 		} 
 		costRecordIsland.setReviewType(0); 		
 		if(StringUtils.isNotEmpty(comment)){
 			costRecordIsland.setComment(comment);
 		} 		
 		costIslandService.saveCostRecordIsland(costRecordIsland); 		
 		
 		if(costAutoPass!=null && costAutoPass==1 && budgetType==0 && review==1){
 			CostRecordIsland cri = costIslandService.copyCostRecordIsland(costRecordIsland);
 			cri.setReviewCompanyId(reviewCompanyId); 
 			cri.setPayReviewCompanyId(payReviewCompanyId); 
 			cri.setPayNowLevel(1);
 			cri.setPayReview(4);
 	 		
 			cri.setBudgetType(1);
 			cri.setNowLevel(4);
 			cri.setReview(2);
 			costIslandService.saveCostRecordIsland(cri);
 			}		
 		
 		return "redirect:"+Global.getAdminPath()+"/cost/island/islandDetail/"+activityIslandUuid+"/"+activityUuid +"/"+typeId;
			
 	}
 	
	/* 酒店，保存成本录入 */
	@RequestMapping(value = "saveHotelCost")
	public String saveHotelCost(@RequestParam(required = true) String itemname,
			@RequestParam(required = false) BigDecimal price,
			@RequestParam(required = false) String comment,
			@RequestParam(required = true) String activityHotelUuid,
			@RequestParam(required = false) String groupUuid,
			@RequestParam(required = false) Integer typeId,
			@RequestParam(required = false) Integer quantity,
			@RequestParam(required = false) Integer currencyId,
			@RequestParam(required = false) Integer overseas,
			@RequestParam(required = false) Integer supplyType,
			@RequestParam(required = false) Integer agentId,
			@RequestParam(required = false) Integer first,
			@RequestParam(required = false) Integer supplier,
			@RequestParam(required = false) Integer budgetType,
			@RequestParam(required = true) Long deptId,
			@RequestParam(required = false) Long bank,
			@RequestParam(required = false) String bankname,
			@RequestParam(required = false) String account,
			@RequestParam(required = false) Integer review,
			@RequestParam(required = false) BigDecimal rate,
			@RequestParam(required = false) Integer currencyAfter,
			@RequestParam(required = false) BigDecimal priceAfter) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		long userId = UserUtils.getUser().getId();
		CostRecordHotel costRecordHotel = new CostRecordHotel();
		costRecordHotel.setUuid(UUID.randomUUID().toString().replace("-", ""));
		costRecordHotel.setName(itemname);
		costRecordHotel.setPrice(price);
		costRecordHotel.setQuantity(quantity);

		if (bank != null) {
			if (bank == (long) -1) { /* 没有默认银行账号 */
				costRecordHotel.setBankName(bankname);
				costRecordHotel.setBankAccount(account);
			} else {
				Bank mybank = bankDao.findOne(bank);
				costRecordHotel.setBankName(mybank.getBankName());
				costRecordHotel.setBankAccount(mybank.getBankAccountCode());
			}
		}
		costRecordHotel.setCurrencyAfter(currencyAfter);
		costRecordHotel.setRate(rate);
		costRecordHotel.setPriceAfter(priceAfter);
		costRecordHotel.setSupplierType(first);
		costRecordHotel.setNowLevel(1);
 		costRecordHotel.setDelFlag("0");
 		costRecordHotel.setCreateBy(UserUtils.getUser());
 		costRecordHotel.setUpdateBy(UserUtils.getUser().getId());
 		costRecordHotel.setCreateDate(new Date());
 		costRecordHotel.setUpdateDate(new Date());
		costRecordHotel.setBudgetType(budgetType);
		costRecordHotel.setPayApplyDate(new Date());
		// costRecord.setDeptId(deptId);
		List<Long> deptList = costManageService.getDeptList(userId);
		if (deptList.size() == 0) {
			System.out.println("没有给用户配置部门");
		}

		Long reviewCompanyId = (long) 0;
		Long payReviewCompanyId = (long) 0;
		if (deptId == 0) { // 部门为空
			reviewCompanyId = costManageService.findReviewCompanyId(companyId, COSTREVIEW_FLOWTYPE, deptList);
			payReviewCompanyId = costManageService.findReviewCompanyId(companyId, PAYREVIEW_FLOWTYPE, deptList);
		} else {
			Department department = departmentDao.findOne(deptId);
			long parentDept = department.getParentId();
			List<Long> listCompany = new ArrayList<Long>();
			listCompany = reviewCompanyDao.findReviewCompanyId(companyId, COSTREVIEW_FLOWTYPE, parentDept);
			if (listCompany.size() > 0) {
				reviewCompanyId = listCompany.get(0);
			} else {
				reviewCompanyId = costManageService.findReviewCompanyId(companyId, COSTREVIEW_FLOWTYPE, deptList);
			}
			listCompany = reviewCompanyDao.findReviewCompanyId(companyId, PAYREVIEW_FLOWTYPE, parentDept);
			if (listCompany.size() > 0) {
				payReviewCompanyId = listCompany.get(0);
			} else {
				payReviewCompanyId = costManageService.findReviewCompanyId(companyId, PAYREVIEW_FLOWTYPE, deptList);
			}
		}
		costRecordHotel.setReviewCompanyId(reviewCompanyId);
		costRecordHotel.setPayReviewCompanyId(payReviewCompanyId);
		if (budgetType == 1) {
			costRecordHotel.setPayReview(4);
			costRecordHotel.setPayNowLevel(1);
		}
		costRecordHotel.setSupplyType(supplyType);
		costRecordHotel.setOrderType(typeId);
		costRecordHotel.setPayStatus(0);
		if (supplyType == 1) {
			costRecordHotel.setSupplyId(agentId);
			Agentinfo agentinfo = new Agentinfo();
			agentinfo = agentinfoService.findOne((long) agentId);
			costRecordHotel.setSupplyName(agentinfo.getAgentName());
		} else {
			costRecordHotel.setSupplyId(supplier);
			SupplierInfo supplierInfo = new SupplierInfo();
			supplierInfo = supplierInfoService.findSupplierInfoById((long) supplier);
			costRecordHotel.setSupplyName(supplierInfo.getSupplierName());
		}

		costRecordHotel.setActivityUuid(groupUuid);
		if (overseas == null || "".equals(overseas)) {
			costRecordHotel.setOverseas(0);
		} else {
			costRecordHotel.setOverseas(overseas);
		}
		costRecordHotel.setCurrencyId(currencyId);
		costRecordHotel.setReview(review);
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if (costAutoPass!=null && costAutoPass==1 && review == 1) {
			costRecordHotel.setReview(2);
		}
		costRecordHotel.setReviewType(0);
		if (StringUtils.isNotEmpty(comment)) {
			costRecordHotel.setComment(comment);
		}
		costIslandService.saveCostRecordHotel(costRecordHotel);
		if (costAutoPass!=null && costAutoPass==1  && budgetType == 0 && review == 1) {
			CostRecordHotel crh = costIslandService.copyCostRecordHotel(costRecordHotel);
			crh.setReviewCompanyId(reviewCompanyId);
			crh.setPayReviewCompanyId(payReviewCompanyId);
			crh.setPayNowLevel(1);
			crh.setPayReview(4);

			crh.setBudgetType(1);
			crh.setNowLevel(4);
			crh.setReview(2);
			costIslandService.saveCostRecordHotel(crh);
		}

		return "redirect:" + Global.getAdminPath() + "/cost/island/hotelDetail/" + activityHotelUuid + "/" + groupUuid + "/" + typeId;

	}
 	
 	
 	 /*海岛游，修改成本录入表单*/
 	@RequestMapping(value = "updateIslandCost/{activityIslandUuid}/{activityUuid}/{costId}/{typeId}/{deptId}")
 	public String updateIslandCost(@PathVariable(value="activityIslandUuid") String activityIslandUuid,@PathVariable(value="activityUuid") String activityUuid,@PathVariable(value="costId") Long costId, 
 			@PathVariable(value="typeId") Integer typeId,@PathVariable(value="deptId") long deptId,Model model) { 			
 		Long companyId= UserUtils.getUser().getCompany().getId();			
 		List<Currency> currencylist = currencyService.findCurrencyList(companyId); 	
 		CostRecordIsland costRecordIsland = new CostRecordIsland();
		costRecordIsland = costRecordIslandDao.findOne(costId);
		model.addAttribute("companyId", companyId);
 		model.addAttribute("activityIslandGroupUuid", activityUuid);
 		model.addAttribute("deptId", deptId);
 		model.addAttribute("activityIslandUuid", activityIslandUuid);
 		model.addAttribute("payReview",costRecordIsland.getPayReview());
 		model.addAttribute("review",costRecordIsland.getReview());
 		model.addAttribute("budgetType",costRecordIsland.getBudgetType());
 		model.addAttribute("supplyType",costRecordIsland.getSupplyType());
 		model.addAttribute("curlist", currencylist);
 		model.addAttribute("typeId", typeId);
 		model.addAttribute("costRecordIsland", costRecordIsland);
 		
 		List<SupplierName> supplierTypeList = new ArrayList<SupplierName>();
		supplierTypeList = supplierInfoService.findSupplierName(costRecordIsland.getSupplierType(), companyId);
		model.addAttribute("supplierTypeList", supplierTypeList);
		
		List<String> supplyTypeList = new ArrayList<String>();
		if (companyId == 68) {
			supplyTypeList.add("1");
			supplyTypeList.add("5");
			supplyTypeList.add("8");
			List<SysCompanyDictView> supplytypelist=sysCompanyDictViewDao.findByType("travel_agency_type",companyId,supplyTypeList);
			model.addAttribute("supplytypelist", supplytypelist);
		} else {
			supplyTypeList.add("11");
			List<SysCompanyDictView> supplytypelist=sysCompanyDictViewDao.findByType("travel_agency_type",companyId,supplyTypeList);
			model.addAttribute("supplytypelist", supplytypelist);
		}
		
		List<SupplierName> listMap = new ArrayList<SupplierName>();
		if( costRecordIsland.getSupplierType()==null){
		    model.addAttribute("supplierType", 0);
		    listMap = supplierInfoService.findSupplierName(0, companyId);
		}else{
			model.addAttribute("supplierType", costRecordIsland.getSupplierType());
			listMap = supplierInfoService.findSupplierName(costRecordIsland.getSupplierType(), companyId);
		}		
		model.addAttribute("supplylist",  listMap);	
		
		List<Map<String, Object>> agentinfo = agentinfoService
				.findAllAgentinfo(companyId);
		model.addAttribute("agentinfo", agentinfo);
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
				model.addAttribute("costAutoPass",costAutoPass);
			}else{
				model.addAttribute("costAutoPass","0");
		} 
		List<Bank> bankList = supplierInfoService.findBank(costRecordIsland.getSupplyType() + 1, costRecordIsland.getSupplyId());
		model.addAttribute("bankList", bankList);
		
		Bank oneBank= supplierInfoService.findBankByNameAccount(costRecordIsland.getBankName(), costRecordIsland.getBankAccount(), Integer.parseInt(companyId.toString()));
		model.addAttribute("oneBank", oneBank);
 	      
 		if (costRecordIsland.getBudgetType() == 2) {
 			return "modules/cost/updateIslandOtherCost";
 		} else {
 			return "modules/cost/updateIsland";
 		}
 		}
 	
    /*海岛游，保存修改后的成本录入*/
	@RequestMapping(value="updateIsland")
	public String updateIsland(@RequestParam(required=true) Long costId,@RequestParam(required=true) String itemname, @RequestParam(required=false) BigDecimal price,@RequestParam(required=false) String comment,
   @RequestParam(required=true) String activityIslandUuid,  @RequestParam(required=false) String activityUuid,@RequestParam(required=false) Integer typeId,@RequestParam(required=false) Integer quantity,@RequestParam(required=false) Integer currencyId,    
   @RequestParam(required=false) Integer overseas, @RequestParam(required=false) Integer first,@RequestParam(required=false) String supplyName,@RequestParam(required=false) Integer budgetType,@RequestParam(required=true) Long deptId,
   @RequestParam(required=false) String bankname,@RequestParam(required=false) String account,@RequestParam(required=false) Integer review,@RequestParam(required=false) Integer payReview,
   @RequestParam(required=false)  BigDecimal rate,@RequestParam(required=false) Integer currencyAfter, @RequestParam(required=false)  BigDecimal priceAfter, @RequestParam(required = false) Integer supplyType, 
   @RequestParam(required = false) Long bank, @RequestParam(required = false) Integer agentId, @RequestParam(required = false) Integer supplier){	
		Long companyId= UserUtils.getUser().getCompany().getId();	
		CostRecordIsland costRecordIsland = costRecordIslandDao.findOne(costId);
		costRecordIsland.setName(itemname);		
		costRecordIsland.setPrice(price);
		costRecordIsland.setQuantity(quantity);
		costRecordIsland.setBankName(bankname);
		costRecordIsland.setBankAccount(account);	
		
		costRecordIsland.setCurrencyAfter(currencyAfter);
		costRecordIsland.setRate(rate);
		costRecordIsland.setPriceAfter(priceAfter);
		
		costRecordIsland.setNowLevel(1);
		//costRecord.setReviewCompanyId((long)1);
		
		if (bank != null) {
			if (bank == (long) -1) { /* 没有默认银行账号 */
				costRecordIsland.setBankName(bankname);
				costRecordIsland.setBankAccount(account);
			} else {
				Bank mybank = bankDao.findOne(bank);
				costRecordIsland.setBankName(mybank.getBankName());
				costRecordIsland.setBankAccount(mybank.getBankAccountCode());
			}
		}
		
		costRecordIsland.setOrderType(typeId);
		costRecordIsland.setReview(review);
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
 		if(costAutoPass!=null && costAutoPass==1 && review==1){
 			costRecordIsland.setReview(2);
 		} 		
		if(review==2){
		  costRecordIsland.setPayReview(2);
		}else{
          costRecordIsland.setPayReview(payReview);
        }
		costRecordIsland.setSupplierType(first);
		if(supplyType!=null){
			costRecordIsland.setSupplyType(supplyType);
		}
		
		// 预算成本和实际成本中没有客户类别，而其它收入有客户类别，所以需要分别判断
		if(costRecordIsland.getBudgetType() == 2) {
			costRecordIsland.setSupplyType(supplyType);
			if(supplyType==1){
				costRecordIsland.setSupplyId(agentId);
				Agentinfo agentinfo = new Agentinfo();
				agentinfo = agentinfoService.findOne((long) agentId);
				costRecordIsland.setSupplyName(agentinfo.getAgentName());
			} else {
				costRecordIsland.setSupplyId(supplier);
				SupplierInfo supplierInfo = new SupplierInfo();
				supplierInfo = supplierInfoService
						.findSupplierInfoById((long) supplier);
				costRecordIsland.setSupplyName(supplierInfo.getSupplierName());
			}
		} else {
			if(costRecordIsland.getSupplyType()!=null && costRecordIsland.getSupplyType()==1){
				costRecordIsland.setSupplyId(agentId);
				Agentinfo agentinfo = new Agentinfo();
				agentinfo = agentinfoService.findOne((long) agentId);
				costRecordIsland.setSupplyName(agentinfo.getAgentName());
			} else {
				costRecordIsland.setSupplyId(supplier);
				SupplierInfo supplierInfo = new SupplierInfo();
				supplierInfo = supplierInfoService
						.findSupplierInfoById((long) supplier);
				costRecordIsland.setSupplyName(supplierInfo.getSupplierName());
			}
		}
		
		//撤销审核时，设置 nowLevel=1
		if(review==4){
			costRecordIsland.setNowLevel(1);
		}
		costRecordIsland.setPayStatus(0);			
		if(overseas == null || "".equals(overseas)) {
			costRecordIsland.setOverseas(0);
 		}else{
 			costRecordIsland.setOverseas(overseas);
 		}	
		costRecordIsland.setCurrencyId(currencyId);		
		if(StringUtils.isNotEmpty(comment)){
			costRecordIsland.setComment(comment);
		}		
		
		if(costAutoPass!=null && costAutoPass==1 && review==1  && costRecordIsland.getBudgetType()==0){
			CostRecordIsland cri = costIslandService.copyCostRecordIsland(costRecordIsland);
			long userId = UserUtils.getUser().getId();
			List<Long> deptList=  costManageService.getDeptList(userId);		
			
			Long reviewCompanyId=(long)0;
	 		Long payReviewCompanyId=(long)0;
	 		if(deptId==0){ //部门为空
	 			reviewCompanyId=costManageService.findReviewCompanyId(companyId,COSTREVIEW_FLOWTYPE,deptList);
	 			payReviewCompanyId=costManageService.findReviewCompanyId(companyId,PAYREVIEW_FLOWTYPE,deptList);
	 		}else{
	 			Department department= departmentDao.findOne(deptId);
	 			long parentDept=department.getParentId();
	 			List<Long> listCompany = new ArrayList<Long>();
	 			listCompany = reviewCompanyDao.findReviewCompanyId(companyId,COSTREVIEW_FLOWTYPE,parentDept);
	 	 		if(listCompany.size()>0) {
	 	 			reviewCompanyId= listCompany.get(0); 			
	 	 		} else{
	 	 		    reviewCompanyId=costManageService.findReviewCompanyId(companyId,COSTREVIEW_FLOWTYPE,deptList);
	 	 		}
	 	 		listCompany = reviewCompanyDao.findReviewCompanyId(companyId,PAYREVIEW_FLOWTYPE,parentDept);
	 	 		if(listCompany.size()>0) {
	 	 		   payReviewCompanyId= listCompany.get(0); 			
	 	 		} else{
	 	 		   payReviewCompanyId=costManageService.findReviewCompanyId(companyId,PAYREVIEW_FLOWTYPE,deptList);
	 	 		}
	 		}	 		
	
	 		costRecordIsland.setReview(2);	 		
	 		cri.setReviewCompanyId(reviewCompanyId); 
	 		cri.setPayReviewCompanyId(payReviewCompanyId); 	 		
	 		cri.setPayNowLevel(1);
	 		cri.setPayReview(4);	 		
	 		cri.setBudgetType(1);
	 		cri.setNowLevel(4);
	 		cri.setReview(2);
	 		costIslandService.saveCostRecordIsland(cri);
			List<String> email = reviewService.getNextPayReviewEmail(cri.getId());
 			String productTypeName = DictUtils.getDictLabel(cri.getOrderType() + "", "order_type", "");//产品类型
 			String[] emails = new String[email.size()];
 			int i = 0;
 			for(String tmp : email){
 				emails[i] = tmp;
 				i++;
 			}
 			SendMailUtil.sendSimpleMail(emails, "付款审核提醒","您好，有用户发起" + productTypeName + "的" + "付款审核申请, 请及时处理");
 
		}
		
		costRecordIslandDao.save(costRecordIsland);
		
		return "redirect:"+Global.getAdminPath()+"/cost/island/islandDetail/"+activityIslandUuid+"/"+activityUuid +"/"+typeId;
	}
 	
	/* 酒店，修改成本录入表单 */
	@RequestMapping(value = "updateHotelCost/{activityHotelUuid}/{activityUuid}/{costId}/{typeId}/{deptId}")
	public String updateHotelCost(
			@PathVariable(value = "activityHotelUuid") String activityHotelUuid,
			@PathVariable(value = "activityUuid") String activityUuid,
			@PathVariable(value = "costId") Long costId,
			@PathVariable(value = "typeId") Integer typeId,
			@PathVariable(value = "deptId") long deptId, Model model) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Currency> currencylist = currencyService.findCurrencyList(companyId);
		CostRecordHotel costRecordHotel = new CostRecordHotel();
		costRecordHotel = costRecordHotelDao.findOne(costId);
		model.addAttribute("companyId", companyId);
		model.addAttribute("activityHotelUuid", activityHotelUuid);
		model.addAttribute("activityUuid", activityUuid);
		model.addAttribute("deptId", deptId);		
		model.addAttribute("payReview", costRecordHotel.getPayReview());
		model.addAttribute("review", costRecordHotel.getReview());
		model.addAttribute("budgetType", costRecordHotel.getBudgetType());
		model.addAttribute("supplyType", costRecordHotel.getSupplyType());
		model.addAttribute("curlist", currencylist);
		model.addAttribute("typeId", typeId);
		model.addAttribute("costRecordHotel", costRecordHotel);

		List<SupplierName> supplierTypeList = new ArrayList<SupplierName>();
		supplierTypeList = supplierInfoService.findSupplierName(costRecordHotel.getSupplierType(), companyId);
		model.addAttribute("supplierTypeList", supplierTypeList);

		List<String> supplyTypeList = new ArrayList<String>();
		if (companyId == 68) {
			supplyTypeList.add("1");
			supplyTypeList.add("5");
			supplyTypeList.add("8");
			List<SysCompanyDictView> supplytypelist=sysCompanyDictViewDao.findByType("travel_agency_type",companyId,supplyTypeList);
			model.addAttribute("supplytypelist", supplytypelist);
		} else {
			supplyTypeList.add("11");
			List<SysCompanyDictView> supplytypelist=sysCompanyDictViewDao.findByType("travel_agency_type",companyId,supplyTypeList);
			model.addAttribute("supplytypelist", supplytypelist);
		}

		List<SupplierName> listMap = new ArrayList<SupplierName>();
		if (costRecordHotel.getSupplierType() == null) {
			model.addAttribute("supplierType", 0);
			listMap = supplierInfoService.findSupplierName(0, companyId);
		} else {
			model.addAttribute("supplierType", costRecordHotel.getSupplierType());
			listMap = supplierInfoService.findSupplierName(costRecordHotel.getSupplierType(), companyId);
		}
		model.addAttribute("supplylist", listMap);

		List<Map<String, Object>> agentinfo = agentinfoService.findAllAgentinfo(companyId);
		model.addAttribute("agentinfo", agentinfo);

		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
				model.addAttribute("costAutoPass",costAutoPass);
			}else{
				model.addAttribute("costAutoPass","0");
			}
		List<Bank> bankList = supplierInfoService.findBank(costRecordHotel.getSupplyType() + 1,	costRecordHotel.getSupplyId());
		model.addAttribute("bankList", bankList);

		Bank oneBank = supplierInfoService.findBankByNameAccount(costRecordHotel.getBankName(),
				costRecordHotel.getBankAccount(), Integer.parseInt(companyId.toString()));
		model.addAttribute("oneBank", oneBank);

		if (costRecordHotel.getBudgetType() == 2) {
			return "modules/cost/updateHotelOtherCost";
		} else {
			return "modules/cost/updateHotel";
		}
	}

	/* 海岛游，保存修改后的成本录入 */
	@RequestMapping(value = "updateHotel")
	public String updateHotel(@RequestParam(required = true) Long costId,
			@RequestParam(required = true) String itemname,
			@RequestParam(required = false) BigDecimal price,
			@RequestParam(required = false) String comment,
			@RequestParam(required = true) String activityHotelUuid,
			@RequestParam(required = false) String activityUuid,
			@RequestParam(required = false) Integer typeId,
			@RequestParam(required = false) Integer quantity,
			@RequestParam(required = false) Integer currencyId,
			@RequestParam(required = false) Integer overseas,
			@RequestParam(required = false) Integer first,
			@RequestParam(required = false) String supplyName,
			@RequestParam(required = false) Integer budgetType,
			@RequestParam(required = true) Long deptId,
			@RequestParam(required = false) String bankname,
			@RequestParam(required = false) String account,
			@RequestParam(required = false) Integer review,
			@RequestParam(required = false) Integer payReview,
			@RequestParam(required = false) BigDecimal rate,
			@RequestParam(required = false) Integer currencyAfter,
			@RequestParam(required = false) BigDecimal priceAfter,
			@RequestParam(required = false) Integer supplyType,
			@RequestParam(required = false) Long bank,
			@RequestParam(required = false) Integer agentId,
			@RequestParam(required = false) Integer supplier) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		CostRecordHotel costRecordHotel = costRecordHotelDao.findOne(costId);
		costRecordHotel.setName(itemname);
		costRecordHotel.setPrice(price);
		costRecordHotel.setQuantity(quantity);
		costRecordHotel.setBankName(bankname);
		costRecordHotel.setBankAccount(account);

		costRecordHotel.setCurrencyAfter(currencyAfter);
		costRecordHotel.setRate(rate);
		costRecordHotel.setPriceAfter(priceAfter);

		costRecordHotel.setNowLevel(1);
		// costRecord.setReviewCompanyId((long)1);

		if (bank != null) {
			if (bank == (long) -1) { /* 没有默认银行账号 */
				costRecordHotel.setBankName(bankname);
				costRecordHotel.setBankAccount(account);
			} else {
				Bank mybank = bankDao.findOne(bank);
				costRecordHotel.setBankName(mybank.getBankName());
				costRecordHotel.setBankAccount(mybank.getBankAccountCode());
			}
		}

		costRecordHotel.setOrderType(typeId);
		costRecordHotel.setReview(review);
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
 		if(costAutoPass!=null && costAutoPass==1 && review==1){
 			costRecordHotel.setReview(2);
 		}
		if(review==2){
		   costRecordHotel.setPayReview(2);
		}else{
           costRecordHotel.setPayReview(payReview);
        }
		costRecordHotel.setSupplierType(first);
		if (supplyType != null) {
			costRecordHotel.setSupplyType(supplyType);
		}

		// 预算成本和实际成本中没有客户类别，而其它收入有客户类别，所以需要分别判断
		if (costRecordHotel.getBudgetType() == 2) {
			costRecordHotel.setSupplyType(supplyType);
			if (supplyType == 1) {
				costRecordHotel.setSupplyId(agentId);
				Agentinfo agentinfo = new Agentinfo();
				agentinfo = agentinfoService.findOne((long) agentId);
				costRecordHotel.setSupplyName(agentinfo.getAgentName());
			} else {
				costRecordHotel.setSupplyId(supplier);
				SupplierInfo supplierInfo = new SupplierInfo();
				supplierInfo = supplierInfoService.findSupplierInfoById((long) supplier);
				costRecordHotel.setSupplyName(supplierInfo.getSupplierName());
			}
		} else {
			if (costRecordHotel.getSupplyType() != null	&& costRecordHotel.getSupplyType() == 1) {
				costRecordHotel.setSupplyId(agentId);
				Agentinfo agentinfo = new Agentinfo();
				agentinfo = agentinfoService.findOne((long) agentId);
				costRecordHotel.setSupplyName(agentinfo.getAgentName());
			} else {
				costRecordHotel.setSupplyId(supplier);
				SupplierInfo supplierInfo = new SupplierInfo();
				supplierInfo = supplierInfoService.findSupplierInfoById((long) supplier);
				costRecordHotel.setSupplyName(supplierInfo.getSupplierName());
			}
		}

		// 撤销审核时，设置 nowLevel=1
		if (review == 4) {
			costRecordHotel.setNowLevel(1);
		}
		costRecordHotel.setPayStatus(0);
		if (overseas == null || "".equals(overseas)) {
			costRecordHotel.setOverseas(0);
		} else {
			costRecordHotel.setOverseas(overseas);
		}
		costRecordHotel.setCurrencyId(currencyId);
		if (StringUtils.isNotEmpty(comment)) {
			costRecordHotel.setComment(comment);
		}		
		if (costAutoPass!=null && costAutoPass==1 && review == 1 && costRecordHotel.getBudgetType() == 0) {
			CostRecordHotel crh = costIslandService.copyCostRecordHotel(costRecordHotel);
			long userId = UserUtils.getUser().getId();
			List<Long> deptList = costManageService.getDeptList(userId);

			Long reviewCompanyId = (long) 0;
			Long payReviewCompanyId = (long) 0;
			if (deptId == 0) { // 部门为空
				reviewCompanyId = costManageService.findReviewCompanyId(companyId, COSTREVIEW_FLOWTYPE, deptList);
				payReviewCompanyId = costManageService.findReviewCompanyId(companyId, PAYREVIEW_FLOWTYPE, deptList);
			} else {
				Department department = departmentDao.findOne(deptId);
				long parentDept = department.getParentId();
				List<Long> listCompany = new ArrayList<Long>();
				listCompany = reviewCompanyDao.findReviewCompanyId(companyId, COSTREVIEW_FLOWTYPE, parentDept);
				if (listCompany.size() > 0) {
					reviewCompanyId = listCompany.get(0);
				} else {
					reviewCompanyId = costManageService.findReviewCompanyId(companyId, COSTREVIEW_FLOWTYPE, deptList);
				}
				listCompany = reviewCompanyDao.findReviewCompanyId(companyId, PAYREVIEW_FLOWTYPE, parentDept);
				if (listCompany.size() > 0) {
					payReviewCompanyId = listCompany.get(0);
				} else {
					payReviewCompanyId = costManageService.findReviewCompanyId(companyId, PAYREVIEW_FLOWTYPE, deptList);
				}
			}

			costRecordHotel.setReview(2);
			crh.setReviewCompanyId(reviewCompanyId);
			crh.setPayReviewCompanyId(payReviewCompanyId);
			crh.setPayNowLevel(1);
			crh.setPayReview(4);
			crh.setBudgetType(1);
			crh.setNowLevel(4);
			crh.setReview(2);
			costIslandService.saveCostRecordHotel(crh);
			List<String> email = reviewService.getNextPayReviewEmail(crh.getId());
			String productTypeName = DictUtils.getDictLabel(crh.getOrderType() + "", "order_type", "");// 产品类型
			String[] emails = new String[email.size()];
			int i = 0;
			for (String tmp : email) {
				emails[i] = tmp;
				i++;
			}
			SendMailUtil.sendSimpleMail(emails, "付款审核提醒", "您好，有用户发起"
					+ productTypeName + "的" + "付款审核申请, 请及时处理");

		}

		costRecordHotelDao.save(costRecordHotel);

		return "redirect:" + Global.getAdminPath() + "/cost/island/hotelDetail/" + activityHotelUuid + "/" + activityUuid + "/" + typeId;
	}
	
	 	
	//散拼等提交成本录入
	@ResponseBody
	@RequestMapping(value="submitgroup", method=RequestMethod.POST)
	public String submitgroup(@RequestParam Long id,@RequestParam Integer typeId){
		//reivew=4 ：待提交
		costRecordDao.submitCostRecord(id,typeId,4);
		return "[{\"result\":\"ok\"}]";
	}
	
	@ResponseBody
	@RequestMapping(value="modify", method=RequestMethod.POST)
	public String modify(@RequestParam Long id,@RequestParam String name,
			@RequestParam BigDecimal price,@RequestParam String comment,
		    @RequestParam Integer quantity,@RequestParam Integer currencyId,    
		    @RequestParam Integer overseas,  @RequestParam Integer budgetType){	
		CostRecord costRecord= new CostRecord();
		costRecord= costManageService.findOne(id);
		costRecord.setName(name);
		costRecord.setPrice(price);
		costRecord.setQuantity(quantity);			
		costRecord.setBudgetType(budgetType);
		costRecord.setOverseas(overseas);		
		costRecord.setCurrencyId(currencyId);
		costRecord.setReview(Context.REVIEW_COST_WAIT);
		if(StringUtils.isNotEmpty(comment)){
			costRecord.setComment(comment);
		}
		costManageService.saveCostRecord(costRecord);	
		return "[{\"result\":\"ok\"}]";
	}
	
	@ResponseBody
	@RequestMapping(value="deleteIsland", method=RequestMethod.POST)
	public String deleteIsland(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costIslandService.deleteCostIsland(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}
	
	@ResponseBody
	@RequestMapping(value="deleteHotel", method=RequestMethod.POST)
	public String deleteHotel(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costIslandService.deleteCostHotel(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}
	
	@ResponseBody
	@RequestMapping(value="cancelIsland", method=RequestMethod.POST)
	public String cancelIsland(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costManageService.cancelCostIsland(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}
	
	@ResponseBody
	@RequestMapping(value="cancelHotel", method=RequestMethod.POST)
	public String cancelHotel(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costManageService.cancelCostHotel(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}
	
	@ResponseBody
	@RequestMapping(value="cancelPayIsland", method=RequestMethod.POST)
	public String cancelPayIsland(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costManageService.cancelPayCostIsland(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}
	
	@ResponseBody
	@RequestMapping(value="cancelPayHotel", method=RequestMethod.POST)
	public String cancelPayHotel(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costManageService.cancelPayCostHotel(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}	
	
	/**
	 * 对象列表转换为json字符串。
	 * @param costs
	 * @return
	 */
	private String costsJson(List<AbstractSpecificCost> costs){
		StringBuffer json = new StringBuffer();
		json.append("[");
		for(AbstractSpecificCost cost : costs){
			json.append("{");
			
			Long id = cost.getId();
			String name = cost.getName();
			BigDecimal price = cost.getPrice();
			String comment = cost.getComment();
			Long groupId = cost.getActivityId();
			
			json.append("\"id\":").append("\"").append(id).append("\", ");
			json.append("\"name\":").append("\"").append(name).append("\", ");
			json.append("\"price\":").append("\"").append(price).append("\", ");
			json.append("\"comment\":").append("\"").append(comment).append("\", ");
			json.append("\"activityGroupId\":").append("\"").append(groupId).append("\"");			
			json.append("},");
			
		}
		if(json.lastIndexOf(",") > 0){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		json.append("]");
		return json.toString();
	}
	
	
	/**
	 * 根据当前状态，类型，获取成本ajax请求
	 * @param activityGroupId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getCosts/{type}/{groupId}", method=RequestMethod.GET)
	public String getCosts(@PathVariable(value="groupId")Long groupId,
			@PathVariable(value="type")String type, HttpServletRequest request){
		String states = request.getParameter("states");
		List<AbstractSpecificCost> costs = costManageService.getCostList(groupId, type, states);
		return this.costsJson(costs);
	}
	
	/*录入成本与收入--获取金额数据*/
	@RequestMapping(value="startCurrency", method=RequestMethod.GET)
	public String startCurrency(@RequestParam(required=true) String activityId,@RequestParam(required=true) Long groupId, @RequestParam(required=true) Long typeId,  HttpServletRequest request,Model model){
		String costSerial="",incomeSerial="";
		if(typeId==7){
		  ActivityAirTicket activityAirTicket=activityAirTicketDao.findOne(groupId);
		  costSerial= activityAirTicket.getCost();
		  incomeSerial= activityAirTicket.getIncome();
		  if (costSerial==null) costSerial="";
		  if (incomeSerial==null) incomeSerial="";	  
		} else if(typeId==6){
		 VisaProducts visaProduct = this.visaProductsDao.findOne(groupId);
		  costSerial= visaProduct.getCost();
		  incomeSerial= visaProduct.getIncome();
		  if (costSerial==null) costSerial="";
		  if (incomeSerial==null) incomeSerial="";	 
		} else{
		  ActivityGroup activityGroup= activityGroupDao.findOne(groupId);
		  costSerial= activityGroup.getCost();
		  incomeSerial= activityGroup.getIncome();
		  if (costSerial==null) costSerial="";
		  if (incomeSerial==null) incomeSerial="";	 
		}
		
		Long companyId= UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> costList=costManageService.getCurrenySum(costSerial,companyId);
		List<Map<String, Object>> incomeList=costManageService.getCurrenySum(incomeSerial,companyId);
		model.addAttribute("costTotal",costList.size());
		model.addAttribute("incomeTotal",incomeList.size());
		
		model.addAttribute("costList",costList);
		model.addAttribute("incomeList", incomeList);
		
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);			
		model.addAttribute("curlist", currencylist);
		model.addAttribute("curTotal", currencylist.size());
		
		model.addAttribute("activityId", activityId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("typeId", typeId);
		
		return "modules/cost/startCurrency";
	}
	
	/*录入成本与收入--保存*/	
	@RequestMapping(value = "saveCurrency")
	public String saveCurrency(@RequestParam(required=true) String activityId,@RequestParam(required=true) Long groupId,
			@RequestParam(required=true) Long typeId, HttpServletRequest request,HttpServletResponse response, Model model) {
		
		int costTotal=Integer.parseInt(request.getParameter("addCostNum").trim());
		int costId [] =new int[costTotal];
		BigDecimal  cost [] =new BigDecimal[costTotal];	
		
		int incomeTotal=Integer.parseInt(request.getParameter("addIncomeNum").trim());
		
		int incomeId [] =new int[incomeTotal];
		BigDecimal  income [] =new BigDecimal[incomeTotal];	
		
		for (int i=1;i<=costTotal;i++){
			costId[i-1] =Integer.parseInt(request.getParameter("costid"+i).trim());
			cost[i-1] =new BigDecimal(request.getParameter("costsum"+i).trim());			
		}	
		
		for (int i=1;i<=incomeTotal;i++){
			incomeId[i-1] =Integer.parseInt(request.getParameter("incomeid"+i).trim());
			income[i-1] =new BigDecimal(request.getParameter("incomesum"+i).trim());			
		}	
		
		costManageService.insertMoney(activityId, groupId, typeId, costTotal, costId, cost);		
		costManageService.insertIncome(activityId, groupId, typeId, incomeTotal, incomeId, income);	
		
		if(typeId==7) {
			return "redirect:"+Global.getAdminPath()+"/cost/manager/airTicketPreRecord/"+ groupId;
		}else if(typeId==6) {
			return "redirect:"+Global.getAdminPath()+"/cost/visa/flow/"+ groupId;
		} else{		
		return "redirect:"+Global.getAdminPath()+"/cost/manager/flow/start/"+ activityId+"/"+ groupId+"/"+ typeId;
	    }
	}
	
	
	//获得批发商列表
	@ResponseBody
	@RequestMapping(value="supplylist/{supplyId}", method=RequestMethod.GET)
	public String supplylist(@PathVariable(value="supplyId")Integer supplyId, HttpServletRequest request){
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<SupplierName> listMap = new ArrayList<SupplierName>();		
		listMap=supplierInfoService.findSupplierName(supplyId,companyId);
		
		StringBuffer json = new StringBuffer();
		json.append("[");
		for(SupplierName supplierName: listMap){
	       json.append("{");	
	       json.append("\"supplierid\":").append("\"").append(supplierName.getSupplierId()).append("\",");
	       json.append("\"suppliername\":").append("\"").append(supplierName.getSupplierName()).append("\"");
		   json.append("},");    
		}
		if(json.lastIndexOf(",") > 0){
			json.deleteCharAt(json.lastIndexOf(","));
		}		
    json.append("]");
    return json.toString();
	}
	

	//获得批发商列表
	@ResponseBody
	@RequestMapping(value="banklist/{supplyType}/{supplyId}", method=RequestMethod.GET)
	public String banklist(@PathVariable(value="supplyType")Integer supplyType, @PathVariable(value="supplyId")Integer supplyId, HttpServletRequest request){
		//Long companyId = UserUtils.getUser().getCompany().getId();
		List<Bank> listMap = new ArrayList<Bank>();		
		listMap=supplierInfoService.findBank(supplyType,supplyId);
		StringBuffer json = new StringBuffer();
		json.append("[");
		for(Bank bank: listMap){
	       json.append("{");	
	       json.append("\"bankid\":").append("\"").append(bank.getId()).append("\",");
	       json.append("\"bankname\":").append("\"").append(bank.getBankName()).append("\",");
	       json.append("\"account\":").append("\"").append(bank.getBankAccountCode()).append("\"");
		   json.append("},");    
		}
		if(json.lastIndexOf(",") > 0){
			json.deleteCharAt(json.lastIndexOf(","));
		}		
    json.append("]");   
    return json.toString();
	}
	
	//获得汇率
		@ResponseBody
		@RequestMapping(value="changecurrency/{currencyId}", method=RequestMethod.GET)
		public String changecurrency(@PathVariable(value="currencyId")Long currencyId, HttpServletRequest request){
	 		//Long companyId= UserUtils.getUser().getCompany().getId();			
	 		Currency currency = currencyService.findCurrency(currencyId);			
		
			StringBuffer json = new StringBuffer();
			json.append("[");
			   json.append("{");	
		       json.append("\"currencyId\":").append("\"").append(currencyId).append("\",");		      
		       json.append("\"rate\":").append("\"").append(currency.getConvertLowest()).append("\"");
			   json.append("},");    
			
			if(json.lastIndexOf(",") > 0){
				json.deleteCharAt(json.lastIndexOf(","));
			}		
	    json.append("]");	   
	    return json.toString();
		}
	
	
	/*
	@RequestMapping(value="list/{type}")
	public String approveList(@ModelAttribute TravelActivity travelActivity,
			HttpServletRequest request, HttpServletResponse response,Model model, 
			@PathVariable(value="type") String type){
		
		List<Integer> states = dispatchMap.get(type);
		List<Integer> ids = new ArrayList<Integer>();
		String groupOpenDateString = request.getParameter("groupOpenDateString");
		String groupCloseDateString = request.getParameter("groupCloseDateString");
		if(StringUtils.isNotBlank(groupOpenDateString)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
	            travelActivity.setGroupOpenDate(format.parse(groupOpenDateString));
            } catch (ParseException e) {
	            LOG.error("日期转换错误", e);
            }
		}
		if(StringUtils.isNotBlank(groupCloseDateString)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
	            travelActivity.setGroupCloseDate(format.parse(groupCloseDateString));
            } catch (ParseException e) {
            	LOG.error("日期转换错误", e);
            }
		}
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		String wholeSalerKey = request.getParameter("wholeSalerKey");
		travelActivity.setAcitivityName(wholeSalerKey);
		Page<TravelActivity> page = travelActivityService.findTravelActivity(new Page<TravelActivity>(request, response), travelActivity, 
				settlementAdultPriceStart, settlementAdultPriceEnd, states);
		
		if(page.getList()!=null){
			for(TravelActivity tmp:page.getList()){
				ids.add(Integer.parseInt(tmp.getId().toString()));
			}
		}
		model.addAttribute("travelActivity", travelActivity);
        model.addAttribute("page", page);
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));
		model.addAttribute("type", type);
       //model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		if(type.startsWith("operator")){
			model.addAttribute("menuId", 147);
			return "modules/cost/preCostList";
		}
		if(type.startsWith("finance")){
			model.addAttribute("menuId", 90);
			return "modules/cost/financeCostInList";
		}
		if(type.startsWith("director")){
			model.addAttribute("menuId", 200);
			return "modules/cost/directorApproveList";
		}
		return null; 
	}  */
	
	@Autowired
	private AreaService areaService;
		
	@Autowired
	private IActivityAirTicketService activityAirTicketService;

	@Autowired
	AirlineInfoService airlineInfoService;
	
	@Autowired
	AirportInfoService airportInfoService; 
	
	@Autowired
	private AirportService airportService;
	
	@Autowired
	IAirTicketOrderService  iAirTicketOrderService;
	@Autowired
	private OfficeService officeService;
	
	//提交成本录入
	@ResponseBody
	@RequestMapping(value="submitair", method=RequestMethod.POST)
	public String submitair(@RequestParam Long id){
		costRecordDao.submitCostRecord(id,7,4);
		return "[{\"result\":\"ok\"}]";
	}
	
   /**
	* 
	* @Title: payment
	* @Description: TODO(付款凭证word下载)
	* @param @param id
	* @param @param orderType
	* @param @param model
	* @param @param request
	* @param @param response
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping(value="payment/{id}/{orderType}", method=RequestMethod.GET)
	public String payment(@PathVariable(value="id") Long id, 
			@PathVariable(value="orderType") Integer orderType, Model model,
			HttpServletRequest request, HttpServletResponse response){
		if(id == null)
			return null;
		CostRecord costRecord  = costManageService.findOne(id);
		if(costRecord == null){
			return null;
		}
		if(costRecord.getPrintTime() == null) {
			model.addAttribute("firstPrintTime", new Date());
		}else{
			model.addAttribute("firstPrintTime", costRecord.getPrintTime());
		}
		ActivityGroup activityGroup = new ActivityGroup();
		PayInfoDetail payInfoDetail = refundService.getPayInfoByPayId(costRecord.getSerialNum(),costRecord.getOrderType()+"");
		//获取一次或多次付款金额20150409
//		List<Object[]> paydmoney = moneyAmountService.getRefundPaydMoneyList(id + "");
//		String currencyId = "" , currencyMoney = "" ;
//		if(paydmoney != null && paydmoney.size() > 0) {
//			currencyId = paydmoney.get(0)[0].toString();
//			currencyMoney = paydmoney.get(0)[2].toString();
//		}
		//取实际付款金额（单价*数量）20150420
		BigDecimal price = costRecord.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecord.getQuantity());
		BigDecimal currentAmount = price.multiply(quantity);
		if(orderType == Context.ORDER_TYPE_JP) {
			ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(costRecord.getActivityId());
			model.addAttribute("groupCode", activityAirTicket.getGroupCode());
		}else if(orderType == Context.ORDER_TYPE_QZ) {
			VisaProducts visaPorduct = visaProductsDao.findOne(costRecord.getActivityId());
			model.addAttribute("groupCode", visaPorduct.getGroupCode());
		}else {
			activityGroup = activityGroupDao.findOne(costRecord.getActivityId());
			model.addAttribute("groupCode", activityGroup.getGroupCode());
		}
		if(payInfoDetail != null) {
			model.addAttribute("pay", payInfoDetail);
		}
		//支出凭单获取人员信息20150505，根据付款审核流程
		String reviewer = reviewService.getPayReviewPerson(costRecord.getId(), 9) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 9);
		String manager = reviewService.getPayReviewPerson(costRecord.getId(), 10) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 10);
		String deptmanager = reviewService.getPayReviewPerson(costRecord.getId(), 4) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 4);
		model.addAttribute("deptmanager", deptmanager);
		model.addAttribute("reviewer", reviewer);
		model.addAttribute("manager", manager);
		model.addAttribute("id",id);
		model.addAttribute("orderType",orderType);
		model.addAttribute("money",costRecord.getName());		
		model.addAttribute("person",costRecord.getCreateBy().getName());
		model.addAttribute("currencyId", costRecord.getCurrencyId());
		model.addAttribute("price", costRecord.getPrice());
		model.addAttribute("quantity", costRecord.getQuantity());
		model.addAttribute("currencyMoney", currentAmount);
		model.addAttribute("createDate", costRecord.getCreateDate());
		model.addAttribute("supplyName", costRecord.getSupplyName());
		model.addAttribute("conDate",payInfoDetail.getCreateDate());
		model.addAttribute("bankName", costRecord.getBankName());
		model.addAttribute("bankAccount", costRecord.getBankAccount());
		//根据地接社或者渠道商确定银行帐号
//		Integer supplyType = costRecord.getSupplyType();
//		if(supplyType != null) {
//			
//		}
		//当前批发商的美元、加元汇率（目前环球行）
		List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("curlist", currencylist);
		return "modules/cost/payment";
	}
	
	@RequestMapping(value="updatePrinted", method=RequestMethod.GET)
	public void updatePrinted(HttpServletRequest request, HttpServletResponse response, Model model){
		String json = "";
		Long id = null;
		String idValue = request.getParameter("id");
		if(StringUtils.isNotBlank(idValue)){
			id = Long.valueOf(idValue);
			Date date = updateOrderPayPrinted(id);
			if(null != date){
				String dateStr = DateUtils.date2String(new Date(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
				json = "{\"flag\":\"success\",\"msg\":\""+dateStr+"\"}";
			}
		}else{
			json = "{\"flag\":\"fail\",\"msg\":\"id的值应该是数值，请检查\"}";
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 更新打印状态以及打印时间，如果是已打印状态则不进行更新操作
	 * @param id
	 * @return
	 */
	private Date updateOrderPayPrinted(Long id){
		Date nowDate = null;
		CostRecord costRecord  = costManageService.findOne(id);
		if(costRecord != null){
			Integer printFlag = costRecord.getPrintFlag();
			if (printFlag == null || printFlag == 0){
				nowDate = new Date();
				costRecord.setPrintTime(nowDate);
				costRecord.setPrintFlag(1);
				costRecordDao.save(costRecord);
			}
		}
		return nowDate;
	}
	
	/**
	 * 
	* @Title: paymentList
	* @Description: TODO(下载付款支付凭证)
	* @param @param id
	* @param @param orderType
	* @param @param request
	* @param @param response
	* @param @return
	* @param @throws IOException
	* @return ResponseEntity<byte[]>    返回类型
	* @throws
	 */
	@RequestMapping(value="paymentList")
	public ResponseEntity<byte[]> paymentList(@RequestParam String id,@RequestParam String orderType,
			@RequestParam String isNew,
			HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{
		if(StringUtils.isBlank(id) || StringUtils.isBlank(orderType)){
			return null;
		}
		File file = costManageService.createPaymentFile(Long.parseLong(id), Integer.parseInt(orderType), Integer.parseInt(isNew));
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName = new StringBuilder().append("支付凭单").append(nowDate).append(".doc").toString();
		updateOrderPayPrinted(Long.parseLong(id));
		WordDownLoadUtils.downLoadWordByAttachment(file, fileName, response);
		return null;
	}

	/**
	 * 
	* @Title: paymentConfirm
	* @Description: TODO(实际成本付款给批发商或渠道商)
	* @param @param id
	* @param @param request
	* @param @param response
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping(value="paymentConfirm/{id}", method=RequestMethod.GET)
	public String paymentConfirm(@PathVariable(value="id") Long id,HttpServletRequest request,HttpServletResponse response){
		String payType = request.getParameter("payType");
		String activityUuid = request.getParameter("activityUuid");
		String orderType = request.getParameter("orderType");
		String groupId = request.getParameter("groupId");
		boolean totalCurrencyFlag = Boolean.parseBoolean(request.getParameter("totalCurrencyFlag"));
		OrderPayInput orderPayInput = null;
		if ("11".equals(orderType)) {
			orderPayInput = costIslandService.payHotel(id + "", groupId, payType, activityUuid, orderType, totalCurrencyFlag);
		} else if ("12".equals(orderType)) {
			orderPayInput = costIslandService.payIsland(id + "", groupId, payType, activityUuid, orderType, totalCurrencyFlag);
		}
		request.setAttribute("pay", orderPayInput);
		return "forward:"+Global.getAdminPath()+"/orderPayMore/pay";
	}
	
	/**
	 * 收款记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="payedRecord")
	@ResponseBody
	public List<Map<String, String>> payedRecord(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String flag = request.getParameter("flag");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (StringUtils.isNotBlank(id)) {
			list = costIslandService.findPayedRecordById(id, flag);
		}
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("createDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).get("createDate")));
			
			String tmp = (String) list.get(i).get("payVoucher");
			StringBuffer name = new StringBuffer();
			StringBuffer ids = new StringBuffer();
			String[] idArr = tmp.split(",");
			if(StringUtils.isNotBlank(tmp)){
				List<DocInfo> docInfoList = docInfoService.getDocInfoBydocids(tmp);
				int docInfoListSize = docInfoList.size();
				for(int ds=0;ds<docInfoListSize;ds++){
					if(ds==docInfoListSize-1){
					    name.append(docInfoList.get(ds).getDocName());
					    ids.append(idArr[ds]);
					}else{
						name.append(docInfoList.get(ds).getDocName()).append("|");
						ids.append(idArr[ds]).append("|");
					}
				}
			}
			list.get(i).put("payvoucher", name.toString());
			list.get(i).put("ids", ids.toString());
		}
		return list;
	}
	
	/**
	 * 组织收款单数据 跳转到收款单页面 add by chy 2015年5月25日21:21:45
	 */
	@RequestMapping(value = "accountPrint/{id}/{prdType}", method = RequestMethod.GET)
	public String accountPrint(@PathVariable(value = "id") Long id,
			@PathVariable(value = "prdType") Integer prdType, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = costManageService.getAccountData(
				id.intValue(), prdType);
		model.addAttribute("data", data);
		return "modules/cost/payInfo";
	}

	/**
	 * 
	 * @Title: recevieDownload
	 * @Description: TODO(下载收款单)
	 * @param @param id
	 * @param @param orderType
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws IOException
	 * @return ResponseEntity<byte[]> 返回类型
	 * @throws
	 */
	@RequestMapping(value = "recevieDownload/{id}/{orderType}")
	public ResponseEntity<byte[]> recevieDownload(@PathVariable String id,
			@PathVariable String orderType, HttpServletRequest request,
			HttpServletResponse response) throws IOException, TemplateException {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(orderType)) {
			return null;
		}
		File file = costManageService.createReceiveFile(Integer.parseInt(id),
				Integer.parseInt(orderType));
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName = "收款单" + nowDate + ".doc";
		OutputStream os = null;
		try {
			if (file != null && file.exists()) {
				response.reset();
				response.setHeader(
						"Content-Disposition",
						"attachment; filename="
								+ new String(fileName.getBytes("gb2312"),
										"ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
				os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
				os.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
		return null;
	}

	/**
	 * 更新打印标志
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updatePrint")
	@ResponseBody
	public String updatePrintFlag(HttpServletRequest request,
			HttpServletResponse response) {
		String payId = request.getParameter("payId");
		String prdtype = request.getParameter("prdType");
		if (payId == null || "".equals(payId)) {
			return "错误的数据payId。请确认当前页面的数据的准确性";
		}
		return costManageService.updatePrintFlag(payId, prdtype);
	}

}
