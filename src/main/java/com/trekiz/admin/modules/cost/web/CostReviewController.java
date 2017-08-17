package com.trekiz.admin.modules.cost.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.excel.CommonExcelUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.ActivityGroupCostView;
import com.trekiz.admin.modules.activity.entity.HotelGroupCostView;
import com.trekiz.admin.modules.activity.entity.IslandGroupCostView;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.IActivityGroupCostViewService;
import com.trekiz.admin.modules.activity.service.IActivityGroupViewService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.service.sync.ActivityGroupSyncService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicketCost;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.AbstractSpecificCost;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.entity.CostRecordHotel;
import com.trekiz.admin.modules.cost.entity.CostRecordIsland;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import com.trekiz.admin.modules.cost.entity.GroupManagerEntity;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.cost.repository.CostRecordLogDao;
import com.trekiz.admin.modules.cost.service.CostIslandService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.cost.service.IReceivePayService;
import com.trekiz.admin.modules.finance.entity.Settle;
import com.trekiz.admin.modules.finance.service.ISettleService;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.reviewflow.entity.ReviewCompany;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.IReviewDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AirportInfoService;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.ReviewCompanyService;
import com.trekiz.admin.modules.sys.utils.CommonUtils;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.entity.VisaProductsCostView;
import com.trekiz.admin.modules.visa.entity.VisaPublicBulletin;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaPublicBulletinService;

@Controller
@RequestMapping(value = "${adminPath}/cost/review")
public class CostReviewController extends BaseController {
		
	private static final Log LOG = LogFactory.getLog(CostManagerController.class);
		
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;	
	@Autowired
	@Qualifier("activityGroupViewService")
	private IActivityGroupViewService activityGroupViewService;
	@Autowired
	@Qualifier("activityGroupCostViewService")
	private IActivityGroupCostViewService activityGroupCostViewService;	
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired	
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired	
	private ActivityHotelService activityHotelService;
	@Autowired	
	private HotelService hotelService;
	@Autowired	
	private IslandService islandService;
	@Autowired	
	private HotelOrderService hotelOrderService;
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;
	@Autowired
	private SysGeographyService sysGeographyService;
	@Autowired
	private ReviewCompanyService reviewCompanyService;
	@Autowired	
	private ActivityGroupSyncService activityGroupService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private CostIslandService costIslandService;
	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private DictService dictService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private IStockDao stockDao;
	@Autowired
	private UserJobDao userJobDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private CostRecordIslandDao costRecordIslandDao;
	@Autowired
	private CostRecordHotelDao costRecordHotelDao;
	@Autowired
	private CostRecordLogDao costRecordLogDao;
	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	@Autowired
	private IReviewDao reviewSqlDao;
	@Autowired
	private IReceivePayService receivePayService;
	@Autowired
	private ISettleService settleService;
	
	private static int COSTREVIEW_FLOWTYPE=15;	
	
	@ModelAttribute
	public TravelActivity get(@RequestParam(required=false) Long id) {
		if(id!=null){
			return travelActivityService.findById(id);
		}else {
			return new TravelActivity();
		}
	}
	
	

	/**
	 * 财务需要录入成本的列表页
	 * @return
	 * typeId:产品类型:散拼，机票..
	 * reviewLevel: 审核层级 
	 * **/    
   @RequestMapping(value="list/{typeId}/{reviewLevel}")
   public String list(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,
		  Model model,@PathVariable(value="typeId") Integer typeId,
		  @PathVariable(value="reviewLevel") Integer reviewLevel){		
	   String review = request.getParameter("review");
	 
	   String userJobId = request.getParameter("userJobId");
	   String createByName=request.getParameter("createByName");

	   User user = UserUtils.getUser();
	   Long companyId = user.getCompany().getId();
	   
		
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
		
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		//String wholeSalerKey = request.getParameter("wholeSalerKey");
		//travelActivity.setAcitivityName(wholeSalerKey);
		Long supplierId = null;
		if(StringUtils.isNotBlank(request.getParameter("supplierId"))){
			try {
				supplierId = StringUtils.toLong(request.getParameter("supplierId"));
			} catch (NumberFormatException e) {
			}
		}	
		Long agentId = null;
		if(StringUtils.isNotBlank(request.getParameter("agentId"))){
			try {
				agentId = StringUtils.toLong(request.getParameter("agentId"));
			} catch (NumberFormatException e) {
			}
		}	
		String groupCode=request.getParameter("groupCode");
		
		model.addAttribute("supplierList",supplierInfoService.findSupplierInfoByCompanyId(companyId));
 		model.addAttribute("agentinfo", agentinfoService.findAllAgentinfo(companyId)); 
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("agentId", agentId); 
		
 		
 		long reviewCompanyId=(long)0; 	 		
 		List<UserJob> userJobs = costManageService.getReviewByFlowType(COSTREVIEW_FLOWTYPE,typeId);
 		List<Map<String, Object>> jobList= new ArrayList<Map<String, Object>>();
 		List<Long> reviewCompanyList = new ArrayList<Long>();
		if(userJobId == null ||userJobId.equals("")){
			if(userJobs.size()>0) {	
				 //首次进入页面，没有选择任何部门职务，取第1个部门职务
				  userJobId=userJobs.get(0).getId().toString();
				  Long deptId=(long)0;
				  if(userJobs.get(0).getDeptLevel()==1){
					  deptId=userJobs.get(0).getDeptId();
				  }else if(userJobs.get(0).getDeptLevel()==2){
					  deptId=userJobs.get(0).getParentDept();
				  } 
				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, COSTREVIEW_FLOWTYPE, deptId);
				if( reviewCompanyList.size() > 0) {					
				  reviewCompanyId = reviewCompanyList.get(0);
				  List<Integer> reviewList;	

				  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,typeId);
			      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJobs.get(0).getJobId(),reviewCompanyId);
			      if (reviewList.size()>0) {
			    	reviewLevel=reviewList.get(0);				
			      }
				}
			 }
			 else{
				reviewCompanyId=(long)-1;
			 }
		}else{			
			for(UserJob userJob : userJobs){
				     if(userJob.getId() == Long.parseLong(userJobId)){
					  Long deptId=(long)0;
					  if(userJob.getDeptLevel()==1){
						  deptId=userJob.getDeptId();
					  }else if(userJob.getDeptLevel()==2){
						  deptId=userJob.getParentDept();
					  } 
					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, COSTREVIEW_FLOWTYPE, deptId);
					if( reviewCompanyList.size() > 0) {						
					  reviewCompanyId = reviewCompanyList.get(0);
					  List<Integer> reviewList;	

					  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,typeId);
				      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJob.getJobId(),reviewCompanyId);
				      if (reviewList.size()>0) {
				    	 reviewLevel=reviewList.get(0);				
				      }
					}
				}
			}
		}	

         model.addAttribute("jobList", jobList);//审核职位列表
		 model.addAttribute("reviewLevel", reviewLevel);
		 model.addAttribute("companyId", companyId);
		 ReviewCompany reviewCompany = reviewCompanyDao.getById(reviewCompanyId);
		 model.addAttribute("reviewCompany", reviewCompany);
		 Integer flowType=15;
		 Page<ActivityGroupCostView> page = activityGroupCostViewService.findGroupCostReview(new Page<ActivityGroupCostView>(request, response), travelActivity,  
				groupCode, supplierId,agentId,typeId,review,reviewLevel, companyId,reviewCompanyId,flowType,common,createByName);	
		model.addAttribute("page", page);
		model.addAttribute("userJobs", userJobs);//当前用户的职位
	    model.addAttribute("userJobId", userJobId);
	    model.addAttribute("reviewCompanyId", reviewCompanyId);
		model.addAttribute("reviewLevel", reviewLevel);		 
		model.addAttribute("companyId", companyId);
	    model.addAttribute("travelActivity", travelActivity);       
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        model.addAttribute("typeId", typeId);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));		
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		model.addAttribute("targetAreaList", activityGroupViewService.findTargetArea());
		model.addAttribute("review", review);
		return "modules/costreview/costReviewList";
	}	 
   /**
    * 计算提成状态更新
    * @param request
    * @param response
    * @return String
    * @author zhaohaiming
    * */
   @ResponseBody
   @RequestMapping(value="updateIscommissionStatus",method=RequestMethod.POST)
   public String updateIscommissionStatus(HttpServletRequest request,HttpServletResponse response){
	   String msg = "{\"flag\":\"ok\"}";
	   try{
		   activityGroupCostViewService.updateIscommissionStatus(request, response);
	   }catch(Exception e){
		   msg="{\"flag\":\"false\"}";
		   e.printStackTrace();
	   }
	   return msg;
   }
   /**成本审核撤销操作
    * @author haiming.zhao
  	*/
   @ResponseBody
   @RequestMapping(value="cancelOp", method=RequestMethod.POST)
   public String cancelOp(@RequestParam(value="costRecordId") Long costRecordId, @RequestParam(value="orderType") String orderType, @RequestParam(value="nowLevel") Integer nowLevel){
	   String msg = "{\"flag\":\"success\"}";
	   try{
		  costManageService.cancelOp(costRecordId, orderType, nowLevel);
	   }catch(Exception e){
		   msg="{\"flag\":\"fail\"}";
		   e.printStackTrace();
	   }
	   return msg;
	   //ServletUtil.print(response, msg);
   }
   
	/**
	 * 财务需要录入成本的列表页
	 * @return
	 * typeId:产品类型:散拼，机票..
	 * reviewLevel: 审核层级 
	 * **/    
   @RequestMapping(value="island/{reviewLevel}")
   public String islandList(@ModelAttribute ActivityIslandGroup activityIslandGroup,HttpServletRequest request, HttpServletResponse response,
 		  Model model, @PathVariable(value="reviewLevel") Integer reviewLevel){		
 	   String review = request.getParameter("review");
 	   String userJobId = request.getParameter("userJobId");
 	   String createByName=request.getParameter("createByName"); 	  
       Integer typeId = 12;
 	   User user = UserUtils.getUser();
 	   Long companyId = user.getCompany().getId();
 	  
 		
 		//按部门展示
 		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
 		Long supplierId = null;
 		if(StringUtils.isNotBlank(request.getParameter("supplierId"))){
 			try {
 				supplierId = StringUtils.toLong(request.getParameter("supplierId"));
 			} catch (NumberFormatException e) {
 			}
 		}	
 		Long agentId = null;
 		if(StringUtils.isNotBlank(request.getParameter("agentId"))){
 			try {
 				agentId = StringUtils.toLong(request.getParameter("agentId"));
 			} catch (NumberFormatException e) {
 			}
 		}	
 		String groupCode=request.getParameter("groupCode"); 		
 		model.addAttribute("supplierList",supplierInfoService.findSupplierInfoByCompanyId(companyId));
 		model.addAttribute("agentinfo", agentinfoService.findAllAgentinfo(companyId)); 
 		model.addAttribute("groupCode", groupCode);
 		model.addAttribute("agentId", agentId); 		
 		
 		long reviewCompanyId=(long)0; 	 		
 		List<UserJob> userJobs = costManageService.getReviewByFlowType(COSTREVIEW_FLOWTYPE,typeId);
 		List<Map<String, Object>> jobList= new ArrayList<Map<String, Object>>();
 		List<Long> reviewCompanyList = new ArrayList<Long>();
 		if(userJobId == null ||userJobId.equals("")){
 			if(userJobs.size()>0) {	
 				 //首次进入页面，没有选择任何部门职务，取第1个部门职务
 				  userJobId=userJobs.get(0).getId().toString();
 				  Long deptId=(long)0;
 				  if(userJobs.get(0).getDeptLevel()==1){
 					  deptId=userJobs.get(0).getDeptId();
 				  }else if(userJobs.get(0).getDeptLevel()==2){
 					  deptId=userJobs.get(0).getParentDept();
 				  } 
 				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, COSTREVIEW_FLOWTYPE, deptId);
 				if( reviewCompanyList.size() > 0) {					
 				  reviewCompanyId = reviewCompanyList.get(0);
 				  List<Integer> reviewList;	

 				  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,typeId);
 			      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJobs.get(0).getJobId(),reviewCompanyId);
 			      if (reviewList.size()>0) {
 			    	reviewLevel=reviewList.get(0);				
 			      }
 				}
 			 }
 			 else{
 				reviewCompanyId=(long)-1;
 			 }
 		}else{			
 			for(UserJob userJob : userJobs){
 				     if(userJob.getId() == Long.parseLong(userJobId)){
 					  Long deptId=(long)0;
 					  if(userJob.getDeptLevel()==1){
 						  deptId=userJob.getDeptId();
 					  }else if(userJob.getDeptLevel()==2){
 						  deptId=userJob.getParentDept();
 					  } 
 					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, COSTREVIEW_FLOWTYPE, deptId);
 					if( reviewCompanyList.size() > 0) {						
 					  reviewCompanyId = reviewCompanyList.get(0);
 					  List<Integer> reviewList;	

 					  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,typeId);
 				      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJob.getJobId(),reviewCompanyId);
 				      if (reviewList.size()>0) {
 				    	 reviewLevel=reviewList.get(0);				
 				      }
 					}
 				}
 			}
 		}	

         model.addAttribute("jobList", jobList);//审核职位列表
 		 model.addAttribute("reviewLevel", reviewLevel);
 		 model.addAttribute("companyId", companyId);
 		 Integer flowType=15;
 		Page<IslandGroupCostView> page = activityGroupCostViewService.findIslandCostReview(new Page<IslandGroupCostView>(request, response), activityIslandGroup,  
 				 supplierId,agentId,review,reviewLevel, companyId,reviewCompanyId,flowType,common,createByName);	
 		model.addAttribute("page", page); 
 		model.addAttribute("userJobs", userJobs);//当前用户的职位
 	    model.addAttribute("userJobId", userJobId); 	    
 	    model.addAttribute("reviewCompanyId", reviewCompanyId);
 		model.addAttribute("reviewLevel", reviewLevel);		 
 		model.addAttribute("companyId", companyId);
 	    model.addAttribute("activityIslandGroup", activityIslandGroup);       

        model.addAttribute("typeId", typeId);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
 		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
 		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
 		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));		
 		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
 		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
 		model.addAttribute("review", review);
 		return "modules/costreview/IslandList";
 	}	
   
   /**
	 * 获得当前步骤加载页面
	 * @param reviewLevel
	 * @param activityUuid
	 * @param activityIslandGroupUuid
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="islandDetail/{activityUuid}/{activityIslandGroupUuid}/{reviewLevel}/{reviewCompanyId}", method=RequestMethod.GET)
	public String islandDetail(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="activityUuid") String activityUuid, 
			@PathVariable(value="reviewCompanyId") Long reviewCompanyId,
			@PathVariable(value="activityIslandGroupUuid") String activityIslandGroupUuid, Model model,
			HttpServletRequest request, HttpServletResponse response){
		String from = request.getParameter("from");			
		ActivityIslandGroup activityGroup = activityIslandGroupService.getByUuid(activityIslandGroupUuid);
		ActivityIsland activityIsland = activityIslandService.getByUuid(activityUuid);	
		// 国家
		String countryName= sysGeographyService.getNameCnByUuid(activityIsland.getCountry());
		model.addAttribute("countryName", countryName);
		// 岛屿
		Island island = islandService.getByUuid(activityIsland.getIslandUuid());
		model.addAttribute("island", island);
		// 酒店
		Hotel hotel = hotelService.getByUuid(activityIsland.getHotelUuid());
		model.addAttribute("hotel", hotel);		
		
		Integer star=hotelService.getHotelStarValByHotelUuid(activityIsland.getHotelUuid());
		model.addAttribute("star",star);
		
		// 房型
//		List<ActivityIslandGroupRoom> rooms = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityGroup.getUuid());
//		model.addAttribute("activityIslandGroupRooms", activityIslandGroupRooms);
		// 餐型
//		List<ActivityIslandGroupMeal> meals = activityIslandGroupMealService.getByactivityIslandGroupUuid(activityGroup.getUuid());
//		model.addAttribute("activityIslandGroupMeals", activityIslandGroupMeals);
		List<Object> rms = costIslandService.getRoomAndMealForIsland(activityIslandGroupUuid);
		model.addAttribute("rms", rms);

		// 参考航班
		ActivityIslandGroupAirline activityIslandGroupAirline = activityIslandGroupAirlineService.getByactivityIslandGroup(activityGroup.getUuid()).get(0);
		model.addAttribute("activityIslandGroupAirline", activityIslandGroupAirline);
	
		/*
		String costSerial= activityIslandGroup.getCost();
		String incomeSerial= activityIslandGroup.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
		
		Integer typeId=12;
		model.addAttribute("typeId",typeId);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));	

		List<Map<String, Object>> incomeList=costIslandService.getIslandForCastList(activityIslandGroupUuid);
		model.addAttribute("incomeList", incomeList);
		List<Map<String, Object>> budgetCost =costManageService.getIslandCost(activityGroup.getUuid(),0);
		List<Map<String, Object>> actualCost=costManageService.getIslandCost(activityGroup.getUuid(),1); 
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost); 
		
		ReviewCompany reviewCompany= new ReviewCompany();
		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
		long userId = UserUtils.getUser().getId();
		List<UserJob> userJobList = new ArrayList<UserJob>();
		userJobList=userJobDao.getUserJobList(userId);	
		List<Long> jobIds = new ArrayList<Long>();
		for (UserJob userjob : userJobList) { 
			  if(userjob.getDeptLevel()==1 && reviewCompany.getDeptId().equals(userjob.getDeptId()) && userjob.getOrderType()==typeId)
					jobIds.add(userjob.getJobId());				 
			 else if(userjob.getDeptLevel()==2 && reviewCompany.getDeptId().equals(userjob.getParentDept()) && userjob.getOrderType()==typeId)
				    jobIds.add(userjob.getJobId());	
		}
		
		List<Integer> levelList = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,jobIds);
		model.addAttribute("levelList", levelList);	
		model.addAttribute("reviewCompanyId", reviewCompanyId);	
		
		model.addAttribute("reviewLevel", reviewLevel);	
		
		if(activityGroup != null &&activityIsland != null ){
			
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			orderList = costIslandService.getIslandOrderInfos(orderList, activityIslandGroupUuid); 
			model.addAttribute("orderList", orderList.getList());
			
			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
			model.addAttribute("activityIsland", activityIsland);
			model.addAttribute("activityGroup", activityGroup);			
			model.addAttribute("type", from);			
			List<CostRecordIsland> budgetOutList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> budgetInList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> actualOutList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> actualInList = new ArrayList<CostRecordIsland>();
			
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);			
			model.addAttribute("curlist", currencylist);				
			//model.addAttribute("costLog",stockDao.findCostRecordLog(activityIslandGroupUuid, typeId));			
			List<Dict> supplytypelist=dictService.findByType("supplier_type");
			model.addAttribute("supplytypelist", supplytypelist);
			//境内成本预算			
			budgetInList = costIslandService.findCostIslandList(activityIslandGroupUuid, 0,0);	
			 //境外成本预算
			budgetOutList = costIslandService.findCostIslandList(activityIslandGroupUuid, 0,1);
			 
			//境内实际成本	
			actualInList = costIslandService.findCostIslandList(activityIslandGroupUuid, 1,0);
			//境外实际成本	
			actualOutList = costIslandService.findCostIslandList(activityIslandGroupUuid, 1,1);		
			
					//判断在成本录入审核时如果有一条审核被拒绝了，到页面上把审核通过的按钮禁用
              List<List<CostRecordIsland>> list = new ArrayList<List<CostRecordIsland>>();
              list.add(budgetInList);
              list.add(budgetOutList);
              list.add(actualInList);
              list.add(budgetOutList);
              boolean isShow =true;
              for(int i=0;i<list.size();i++){
              	if(list.get(i).size()>0){
              		int num = list.get(i).size();
              		for(int a=0;a<num;a++){
              			if(list.get(i).get(a).getReview()==0){
              				isShow=false;
              			}
              			a=num;
              		}
              	}
              	i=list.size();
              }
              model.addAttribute("isShow",isShow);              
			  model.addAttribute("budgetInList", budgetInList);
			  model.addAttribute("budgetOutList", budgetOutList);				
			  model.addAttribute("actualInList", actualInList);
			  model.addAttribute("actualOutList", actualOutList);
			  Object budgetrefund = costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund") == null ? "0"
						: costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund");
				model.addAttribute("budgetrefund", budgetrefund);
			 Object actualrefund = costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund") == null ? "0"
						: costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund");
				model.addAttribute("actualrefund", actualrefund);

		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		return "modules/costreview/islandDetail";
	}
	
	/**
	 * 获得当前步骤加载页面
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="islandRead/{activityUuid}/{activityIslandGroupUuid}/{reviewLevel}", method=RequestMethod.GET)
	public String islandRead(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="activityUuid") String activityUuid, 
			@PathVariable(value="activityIslandGroupUuid") String activityIslandGroupUuid, Model model,
			HttpServletRequest request, HttpServletResponse response){
		String from = request.getParameter("from");
		String isEFX = request.getParameter("isEFX");//是否是俄风行
		String flag = request.getParameter("flag");
		model.addAttribute("flag", flag);

		String sitemap = request.getParameter("sitemap");
		model.addAttribute("sitemap", sitemap);

		String menuid = request.getParameter("menuid");
		if("3".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 返佣付款 > 海岛游详情页");
			model.addAttribute("head", "海岛游返佣付款详情页");
		}else if("2".equals(menuid)) {
//			model.addAttribute("title", "财务 > 结算管理 > 退款付款 > 海岛游详情页");
			model.addAttribute("title", "");//C266需求 去掉海岛游详情页的导航  modify by chy 2015年8月31日16:22:26
			model.addAttribute("head", "海岛游退款付款详情页");
		} else if("4".equals(menuid)) {
		   model.addAttribute("title", "");//C266需求 去掉海岛游详情页的导航  modify by shijun.liu
		   model.addAttribute("head", "海岛游借款付款详情"); 
	    } else if("1".equals(menuid)) {
	    	if(StringUtils.isNotBlank(isEFX) && isEFX.equalsIgnoreCase("true")){
	    	  model.addAttribute("title", "海岛游产品");
	  		  model.addAttribute("head", "海岛游成本付款详情");
	    	}else{
			  model.addAttribute("title", "财务 > 结算管理 > 成本付款 > 海岛游详情页");
			  model.addAttribute("head", "海岛游成本付款详情");
	    	}
        } else{
	      model.addAttribute("title","");
	      model.addAttribute("head", "海岛游成本详情页"); 
	    }

		ActivityIslandGroup activityGroup = activityIslandGroupService.getByUuid(activityIslandGroupUuid);
		ActivityIsland activityIsland = activityIslandService.getByUuid(activityUuid);	
		
		// 国家
		String countryName= sysGeographyService.getNameCnByUuid(activityIsland.getCountry());
		model.addAttribute("countryName", countryName);
		// 岛屿
		Island island = islandService.getByUuid(activityIsland.getIslandUuid());
		model.addAttribute("island", island);
		// 酒店
		Hotel hotel = hotelService.getByUuid(activityIsland.getHotelUuid());
		model.addAttribute("hotel", hotel);
		
		Integer star=hotelService.getHotelStarValByHotelUuid(activityIsland.getHotelUuid());
		model.addAttribute("star",star);
		
		//下单人
		List<IslandOrder> islandOrders = islandOrderService.getByIslandUuid(activityUuid);
		model.addAttribute("islandOrders", islandOrders);
		// 预报名
		Integer bookingPersonNum = islandOrderService.getBookingPersonNum(activityIslandGroupUuid);
		model.addAttribute("bookingPersonNum", bookingPersonNum == null ? 0 : bookingPersonNum);
		
		// 房型
//		List<ActivityIslandGroupRoom> rooms = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityGroup.getUuid());
//		model.addAttribute("activityIslandGroupRooms", activityIslandGroupRooms);
		// 餐型
//		List<ActivityIslandGroupMeal> meals = activityIslandGroupMealService.getByactivityIslandGroupUuid(activityGroup.getUuid());
//		model.addAttribute("activityIslandGroupMeals", activityIslandGroupMeals);
		List<Object> rms = costIslandService.getRoomAndMealForIsland(activityIslandGroupUuid);
		model.addAttribute("rms", rms);
		// 参考航班
		List<ActivityIslandGroupAirline> airlines = activityIslandGroupAirlineService.getByactivityIslandGroup(activityGroup.getUuid());
		if (airlines != null && airlines.size() > 0) {
			ActivityIslandGroupAirline activityIslandGroupAirline = airlines.get(0);
			model.addAttribute("activityIslandGroupAirline", activityIslandGroupAirline);
		}
		
		/*
		String costSerial= activityGroup.getCost();
		String incomeSerial= activityGroup.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
		
		Integer typeId= 12 ;
		model.addAttribute("typeId",typeId);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		if(activityGroup != null && activityIsland != null){
			
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			orderList = costIslandService.getIslandOrderInfos(orderList, activityIslandGroupUuid); 
			model.addAttribute("orderList", orderList.getList());
			
			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
			model.addAttribute("activityIsland", activityIsland);
			model.addAttribute("activityGroup", activityGroup);			
			model.addAttribute("type", from);
			
			List<CostRecordIsland> budgetOutList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> budgetInList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> actualOutList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> actualInList = new ArrayList<CostRecordIsland>();
			List<CostRecordIsland> otherCostList = new ArrayList<CostRecordIsland>();
			
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
			
			model.addAttribute("curlist", currencylist);
			//model.addAttribute("costLog",stockDao.findCostRecordLog(activityIslandGroupUuid, typeId));
			
			List<Dict> supplytypelist=dictService.findByType("supplier_type");
			model.addAttribute("supplytypelist", supplytypelist);
			budgetInList =  costIslandService.findCostIslandList(activityIslandGroupUuid, 0,0);
			budgetOutList =  costIslandService.findCostIslandList(activityIslandGroupUuid, 0,1);
				
			actualInList =  costIslandService.findCostIslandList(activityIslandGroupUuid, 1,0);
			actualOutList =  costIslandService.findCostIslandList(activityIslandGroupUuid, 1,1);
				
			otherCostList =  costIslandService.findCostIslandList(activityIslandGroupUuid, 2, 0);
				 
			for (int i = 0; i < otherCostList.size(); i++) {
				Object obj1 = costIslandService.getPayedMoneyForIsland(otherCostList.get(i).getId());
				otherCostList.get(i).setPayedMoney(obj1 == null ? "0" : obj1.toString());
				Object obj2 = costIslandService.getConfirmMoneyForIsland(otherCostList.get(i).getId());
				otherCostList.get(i).setConfirmMoney(obj2 == null ? "0" : obj2.toString());
			}
				
				model.addAttribute("budgetInList", budgetInList);
				model.addAttribute("budgetOutList", budgetOutList);				
				model.addAttribute("actualInList", actualInList);
				model.addAttribute("actualOutList", actualOutList);	
				model.addAttribute("otherCostList", otherCostList);
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		
		List<Map<String, Object>> incomeList=costIslandService.getIslandForCastList(activityIslandGroupUuid);
		model.addAttribute("incomeList",incomeList); 
		List<Map<String, Object>> budgetCost =costManageService.getIslandCost(activityGroup.getUuid(),0);
		List<Map<String, Object>> actualCost=costManageService.getIslandCost(activityGroup.getUuid(),1); 
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost); 
		
		Object budgetrefund = costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund") == null ? "0"
				: costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 0, typeId).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund") == null ? "0"
				: costIslandService.getRefundSumForIsland(activityIslandGroupUuid, 1, typeId).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund); 
		return "modules/costreview/islandRead";
	}
	
	@RequestMapping(value = "hotelRead/{activityHotelUuid}/{activityHotelGroupUuid}/{reviewLevel}", method = RequestMethod.GET)
	public String hotelRead(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value = "activityHotelUuid") String activityHotelUuid,
			@PathVariable(value = "activityHotelGroupUuid") String activityHotelGroupUuid,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		// String from = request.getParameter("from");
		String flag = request.getParameter("flag");
		model.addAttribute("flag", flag);

		String sitemap = request.getParameter("sitemap");
		model.addAttribute("sitemap", sitemap);
        String isEFX = request.getParameter("isEFX");
		String menuid = request.getParameter("menuid");
		if("3".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 返佣付款 > 酒店详情页");
			model.addAttribute("head", "酒店返佣付款详情页");
		}else if("2".equals(menuid)) {
//			model.addAttribute("title", "财务 > 结算管理 > 退款付款 > 酒店详情页");
			model.addAttribute("title", "");//C266需求 退款付款进入酒店详情页时 不显示导航菜单 modify by chy 2015年8月31日16:20:57
			model.addAttribute("head", "酒店退款付款详情页");
		} else if("4".equals(menuid)) {
		   model.addAttribute("title", "");//C266需求 去掉酒店详情页的导航  modify by shijun.liu
		   model.addAttribute("head", "酒店借款付款详情"); 
	    } else if("1".equals(menuid)) {
	    	if(isEFX.equalsIgnoreCase("true")){
	    		model.addAttribute("title", "酒店产品");
				model.addAttribute("head", "酒店成本付款 详情"); 
	    	}else{
			  model.addAttribute("title", "财务 > 结算管理 > 成本付款 > 酒店详情页");
			  model.addAttribute("head", "酒店成本付款 详情"); 
	    	}
        } else{
	      model.addAttribute("title","");
	      model.addAttribute("head", "酒店成本详情页"); 
	    }

		Long companyId = UserUtils.getUser().getCompany().getId();
		ActivityHotel activityHotel = activityHotelService.getByUuid(activityHotelUuid);
		Integer star=hotelService.getHotelStarValByHotelUuid(activityHotel.getHotelUuid());
		model.addAttribute("star",star);
		
		Integer deptId = activityHotel.getDeptId();
		if (deptId == null) {
			deptId = 0;
		}
		// 获取团期信息
		ActivityHotelGroup activityGroup = activityHotelGroupService.getByUuid(activityHotelGroupUuid);
		Hotel hotel = hotelService.getByUuid(activityHotel.getHotelUuid());

		Island island = islandService.getByUuid(activityHotel.getIslandUuid());
		model.addAttribute("islandName", island.getIslandName());
//		model.addAttribute("hotelName", hotel.getNameCn());
		model.addAttribute("deptId", deptId);
		model.addAttribute("companyId", companyId);
		model.addAttribute("hotel", hotel);
		// 是否计调职务
		model.addAttribute("isOperator", reviewService.checkJobType(3, 4));
		// 是否操作职务
		model.addAttribute("isOpt", reviewService.checkJobType(5));
		// model.addAttribute("costLog",stockDao.findCostRecordLog(activityHotelGroupUuid,
		// typeId));
		Integer typeId=11 ;
		model.addAttribute("typeId",typeId);
		
		// 国家
		String countryName= sysGeographyService.getNameCnByUuid(activityHotel.getCountry());
		model.addAttribute("countryName", countryName);
		// 酒店
//		HotelStar hotelStar = hotelStarService.getByUuid(hotel.getStar());
//		model.addAttribute("hotelStar", hotelStar);
		// 预报名
		Integer forecaseReportNum = hotelOrderService.getForecaseReportNum(activityHotelGroupUuid);
		model.addAttribute("forecaseReportNum", forecaseReportNum == null ? 0 : forecaseReportNum);
		// 下单人
//		HotelOrderQuery hotelOrderQuery = new HotelOrderQuery();
//		hotelOrderQuery.setActivityHotelUuid(activityHotelUuid);
		List<HotelOrder> hotelOrders = hotelOrderService.getByHotelUuid(activityHotelUuid);
		model.addAttribute("hotelOrders", hotelOrders);
		// 房型餐型
		List<Object> rms = costIslandService.getRoomAndMealForHotel(activityHotelGroupUuid);
		model.addAttribute("rms", rms);

		if (activityGroup != null && activityHotel != null) {
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			 orderList = costIslandService.getHotelOrderInfos(orderList, activityHotelGroupUuid);

			model.addAttribute("agentinfoList",	agentinfoService.findAllAgentinfo());
			model.addAttribute("activityHotel", activityHotel);
			model.addAttribute("activityGroup", activityGroup);
			model.addAttribute("orderList", orderList.getList());
			// model.addAttribute("type", from);
			model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
			List<CostRecordHotel> budgetOutList = new ArrayList<CostRecordHotel>();
			List<CostRecordHotel> budgetInList = new ArrayList<CostRecordHotel>();
			List<CostRecordHotel> actualOutList = new ArrayList<CostRecordHotel>();
			List<CostRecordHotel> actualInList = new ArrayList<CostRecordHotel>();
			List<CostRecordHotel> otherCostList = new ArrayList<CostRecordHotel>();
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
			// model.addAttribute("typename", dict.getLabel());
			model.addAttribute("curlist", currencylist);
			List<String> supplyTypeList = new ArrayList<String>();
			if (companyId == 68) {
				supplyTypeList.add("1");
				supplyTypeList.add("5");
				supplyTypeList.add("8");
				List<Dict> supplytypelist = dictService.findByType("supplier_type", supplyTypeList);
				model.addAttribute("supplytypelist", supplytypelist);
			} else {
				supplyTypeList.add("11");
				List<Dict> supplytypelist = dictService.findByType("supplier_type", supplyTypeList);
				model.addAttribute("supplytypelist", supplytypelist);
			}
			
			 List<Map<String, Object>> incomeList = costIslandService.getHotelForCastList(activityHotelGroupUuid);
			 List<Map<String, Object>> budgetCost =costManageService.getHotelCost(activityHotelGroupUuid,0);
			 List<Map<String, Object>> actualCost=costManageService.getHotelCost(activityHotelGroupUuid ,1); 
			 model.addAttribute("incomeList", incomeList);
			 model.addAttribute("budgetCost",budgetCost);
			 model.addAttribute("actualCost",actualCost);
			 

			// 境内成本预算
			budgetInList = costIslandService.findCostHotelList(activityHotelGroupUuid, 0, 0);
			// 境外成本预算
			budgetOutList = costIslandService.findCostHotelList(activityHotelGroupUuid, 0, 1);
			// 境内实际成本
			actualInList = costIslandService.findCostHotelList(activityHotelGroupUuid, 1, 0);
			// 境外实际成本
			actualOutList = costIslandService.findCostHotelList(activityHotelGroupUuid, 1, 1);
			// 其它
			otherCostList = costIslandService.findCostHotelList(activityHotelGroupUuid, 2, 0); // 第三个参数overseas默认设置为0

			model.addAttribute("budgetInList", budgetInList);
			model.addAttribute("budgetOutList", budgetOutList);

			model.addAttribute("actualInList", actualInList);
			model.addAttribute("actualOutList", actualOutList);

			for (int i = 0; i < otherCostList.size(); i++) {
				Object obj1 = costIslandService.getPayedMoneyForHotel(otherCostList.get(i).getId());
				otherCostList.get(i).setPayedMoney(obj1 == null ? "0" : obj1.toString());
				Object obj2 = costIslandService.getConfirmMoneyForHotel(otherCostList.get(i).getId());
				otherCostList.get(i).setConfirmMoney(obj2 == null ? "0" : obj2.toString());
			}
			model.addAttribute("otherCostList", otherCostList);
		} else {
			throw new RuntimeException("产品和团期不匹配");
		}

		Object budgetrefund = costIslandService
				.getRefundSumForHotel(activityHotelGroupUuid, 0, typeId).get(0)
				.get("totalRefund") == null ? "0" : costIslandService
				.getRefundSumForHotel(activityHotelGroupUuid, 0, typeId).get(0)
				.get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costIslandService
				.getRefundSumForHotel(activityHotelGroupUuid, 1, typeId).get(0)
				.get("totalRefund") == null ? "0" : costIslandService
				.getRefundSumForHotel(activityHotelGroupUuid, 1, typeId).get(0)
				.get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);

		return "modules/costreview/hotelRead";
	}
	
	 /**
		 * 获得当前步骤加载页面
		 * @param activityUuid
		 * @param activityHotelGroupUuid
		 * @param model
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="hotelDetail/{activityUuid}/{activityHotelGroupUuid}/{reviewLevel}/{reviewCompanyId}", method=RequestMethod.GET)
		public String hotelDetail(@PathVariable(value="reviewLevel") Integer reviewLevel,
				@PathVariable(value="activityUuid") String activityUuid, 
				@PathVariable(value="reviewCompanyId") Long reviewCompanyId,
				@PathVariable(value="activityHotelGroupUuid") String activityHotelGroupUuid, Model model,
				HttpServletRequest request, HttpServletResponse response){
			String from = request.getParameter("from");			
			ActivityHotelGroup activityGroup = activityHotelGroupService.getByUuid(activityHotelGroupUuid);
			ActivityHotel activityHotel = activityHotelService.getByUuid(activityUuid);	
			Integer star=hotelService.getHotelStarValByHotelUuid(activityHotel.getHotelUuid());
			model.addAttribute("star",star);
			Hotel hotel = hotelService.getByUuid(activityHotel.getHotelUuid());
			Island island = islandService.getByUuid(activityHotel.getIslandUuid());
			model.addAttribute("islandName", island.getIslandName());
			model.addAttribute("hotelName", hotel.getNameCn());
			// 国家
			String countryName= sysGeographyService.getNameCnByUuid(activityHotel.getCountry());
			model.addAttribute("countryName", countryName);
			// 预报名
			Integer forecaseReportNum = hotelOrderService.getForecaseReportNum(activityHotelGroupUuid);
			model.addAttribute("forecaseReportNum", forecaseReportNum == null ? 0 : forecaseReportNum);
			// 下单人
			List<HotelOrder> hotelOrders = hotelOrderService.getByHotelUuid(activityHotel.getUuid());
			model.addAttribute("hotelOrders", hotelOrders);
			List<Object> rms = costIslandService.getRoomAndMealForHotel(activityHotelGroupUuid);
			model.addAttribute("rms", rms);
			/*
			String costSerial= activityHotelGroup.getCost();
			String incomeSerial= activityHotelGroup.getIncome();		
			if(costSerial==null) costSerial="";
			if(incomeSerial==null) incomeSerial="";
			Long companyId= UserUtils.getUser().getCompany().getId();	
			model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
			model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
			model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
			
			Integer typeId=11;
			model.addAttribute("typeId",typeId);
			model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));	
			
			List<Map<String, Object>> incomeList=costIslandService.getHotelForCastList(activityHotelGroupUuid);
			List<Map<String, Object>> budgetCost =costManageService.getHotelCost(activityHotelGroupUuid,0);
			List<Map<String, Object>> actualCost=costManageService.getHotelCost(activityHotelGroupUuid ,1); 
			
			model.addAttribute("incomeList", incomeList);
			model.addAttribute("budgetCost",budgetCost);
			model.addAttribute("actualCost",actualCost); 
		
			ReviewCompany reviewCompany= new ReviewCompany();
			reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
			long userId = UserUtils.getUser().getId();
			List<UserJob> userJobList = new ArrayList<UserJob>();
			userJobList=userJobDao.getUserJobList(userId);	
			List<Long> jobIds = new ArrayList<Long>();
			for (UserJob userjob : userJobList) { 
				  if(userjob.getDeptLevel()==1 && reviewCompany.getDeptId().equals(userjob.getDeptId()) && userjob.getOrderType()==typeId)
						jobIds.add(userjob.getJobId());				 
				 else if(userjob.getDeptLevel()==2 && reviewCompany.getDeptId().equals(userjob.getParentDept()) && userjob.getOrderType()==typeId)
					    jobIds.add(userjob.getJobId());	
			}
			
			List<Integer> levelList = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,jobIds);
			model.addAttribute("levelList", levelList);	
			model.addAttribute("reviewCompanyId", reviewCompanyId);	
			
			model.addAttribute("reviewLevel", reviewLevel);	
			
			if(activityGroup != null &&activityHotel != null ){
				
				Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
				orderList.setPageNo(1);
				orderList.setPageSize(Integer.MAX_VALUE);
				orderList = costIslandService.getHotelOrderInfos(orderList, activityHotelGroupUuid);
				 
				model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
				model.addAttribute("activityHotel", activityHotel);
				model.addAttribute("activityGroup", activityGroup);			
				model.addAttribute("type", from);			
				List<CostRecordHotel> budgetOutList = new ArrayList<CostRecordHotel>();
				List<CostRecordHotel> budgetInList = new ArrayList<CostRecordHotel>();
				List<CostRecordHotel> actualOutList = new ArrayList<CostRecordHotel>();
				List<CostRecordHotel> actualInList = new ArrayList<CostRecordHotel>();
				
				List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);			
				model.addAttribute("curlist", currencylist);				
				//model.addAttribute("costLog",stockDao.findCostRecordLog(activityHotelGroupUuid, typeId));			
				List<Dict> supplytypelist=dictService.findByType("supplier_type");
				model.addAttribute("supplytypelist", supplytypelist);
				//境内成本预算			
				budgetInList = costIslandService.findCostHotelList(activityHotelGroupUuid, 0,0);	
				 //境外成本预算
				budgetOutList = costIslandService.findCostHotelList(activityHotelGroupUuid, 0,1);
				 
				//境内实际成本	
				actualInList = costIslandService.findCostHotelList(activityHotelGroupUuid, 1,0);
				//境外实际成本	
				actualOutList = costIslandService.findCostHotelList(activityHotelGroupUuid, 1,1);		
				
						//判断在成本录入审核时如果有一条审核被拒绝了，到页面上把审核通过的按钮禁用
	              List<List<CostRecordHotel>> list = new ArrayList<List<CostRecordHotel>>();
	              list.add(budgetInList);
	              list.add(budgetOutList);
	              list.add(actualInList);
	              list.add(budgetOutList);
	              boolean isShow =true;
	              for(int i=0;i<list.size();i++){
	              	if(list.get(i).size()>0){
	              		int num = list.get(i).size();
	              		for(int a=0;a<num;a++){
	              			if(list.get(i).get(a).getReview()==0){
	              				isShow=false;
	              			}
	              			a=num;
	              		}
	              	}
	              	i=list.size();
	              }
	              model.addAttribute("isShow",isShow);              
				  model.addAttribute("budgetInList", budgetInList);
				  model.addAttribute("budgetOutList", budgetOutList);				
				  model.addAttribute("actualInList", actualInList);
				  model.addAttribute("actualOutList", actualOutList);	
				  Object budgetrefund = costIslandService
							.getRefundSumForHotel(activityHotelGroupUuid, 0, typeId).get(0)
							.get("totalRefund") == null ? "0" : costIslandService
							.getRefundSumForHotel(activityHotelGroupUuid, 0, typeId).get(0)
							.get("totalRefund");
				model.addAttribute("budgetrefund", budgetrefund);
				Object actualrefund = costIslandService
							.getRefundSumForHotel(activityHotelGroupUuid, 1, typeId).get(0)
							.get("totalRefund") == null ? "0" : costIslandService
							.getRefundSumForHotel(activityHotelGroupUuid, 1, typeId).get(0)
							.get("totalRefund");
				model.addAttribute("actualrefund", actualrefund);
			}else{
				throw new RuntimeException("产品和团期不匹配");
			}
			return "modules/costreview/hotelDetail";
		}
   
   /**
	 * 财务需要录入成本的列表页
	 * @return
	 * typeId:产品类型:散拼，机票..
	 * reviewLevel: 审核层级 
	 * **/    
  @RequestMapping(value="hotel/{reviewLevel}")
  public String hotelList(@ModelAttribute ActivityHotelGroup activityHotelGroup, HttpServletRequest request, HttpServletResponse response,
		  Model model, @PathVariable(value="reviewLevel") Integer reviewLevel){		
	   String review = request.getParameter("review");
	   String userJobId = request.getParameter("userJobId");
	   String createByName=request.getParameter("createByName");
      Integer typeId = 11;
	   User user = UserUtils.getUser();
	   Long companyId = user.getCompany().getId();
	  
		
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
		//String wholeSalerKey = request.getParameter("wholeSalerKey");
		//activityIsland.setAcitivityName(wholeSalerKey);
		Long supplierId = null;
		if(StringUtils.isNotBlank(request.getParameter("supplierId"))){
			try {
				supplierId = StringUtils.toLong(request.getParameter("supplierId"));
			} catch (NumberFormatException e) {
			}
		}	
		Long agentId = null;
		if(StringUtils.isNotBlank(request.getParameter("agentId"))){
			try {
				agentId = StringUtils.toLong(request.getParameter("agentId"));
			} catch (NumberFormatException e) {
			}
		}	
		String groupCode=request.getParameter("groupCode"); 		
		model.addAttribute("supplierList",supplierInfoService.findSupplierInfoByCompanyId(companyId));
		model.addAttribute("agentinfo", agentinfoService.findAllAgentinfo(companyId)); 
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("agentId", agentId); 		
		
		long reviewCompanyId=(long)0; 	 		
		List<UserJob> userJobs = costManageService.getReviewByFlowType(COSTREVIEW_FLOWTYPE,typeId);
		List<Map<String, Object>> jobList= new ArrayList<Map<String, Object>>();
		List<Long> reviewCompanyList = new ArrayList<Long>();
		if(userJobId == null ||userJobId.equals("")){
			if(userJobs.size()>0) {	
				 //首次进入页面，没有选择任何部门职务，取第1个部门职务
				  userJobId=userJobs.get(0).getId().toString();
				  Long deptId=(long)0;
				  if(userJobs.get(0).getDeptLevel()==1){
					  deptId=userJobs.get(0).getDeptId();
				  }else if(userJobs.get(0).getDeptLevel()==2){
					  deptId=userJobs.get(0).getParentDept();
				  } 
				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, COSTREVIEW_FLOWTYPE, deptId);
				if( reviewCompanyList.size() > 0) {					
				  reviewCompanyId = reviewCompanyList.get(0);
				  List<Integer> reviewList;	

				  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,typeId);
			      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJobs.get(0).getJobId(),reviewCompanyId);
			      if (reviewList.size()>0) {
			    	reviewLevel=reviewList.get(0);				
			      }
				}
			 }
			 else{
				reviewCompanyId=(long)-1;
			 }
		}else{			
			for(UserJob userJob : userJobs){
				     if(userJob.getId() == Long.parseLong(userJobId)){
					  Long deptId=(long)0;
					  if(userJob.getDeptLevel()==1){
						  deptId=userJob.getDeptId();
					  }else if(userJob.getDeptLevel()==2){
						  deptId=userJob.getParentDept();
					  } 
					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, COSTREVIEW_FLOWTYPE, deptId);
					if( reviewCompanyList.size() > 0) {						
					  reviewCompanyId = reviewCompanyList.get(0);
					  List<Integer> reviewList;	

					  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,typeId);
				      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJob.getJobId(),reviewCompanyId);
				      if (reviewList.size()>0) {
				    	 reviewLevel=reviewList.get(0);				
				      }
					}
				}
			}
		}	

        model.addAttribute("jobList", jobList);//审核职位列表
		 model.addAttribute("reviewLevel", reviewLevel);
		 model.addAttribute("companyId", companyId);
		 Integer flowType=15;
		Page<HotelGroupCostView> page = activityGroupCostViewService.findHotelCostReview(new Page<HotelGroupCostView>(request, response), activityHotelGroup,  
				 supplierId,agentId,review,reviewLevel, companyId,reviewCompanyId,flowType,common,createByName);	
		model.addAttribute("page", page); 
		model.addAttribute("userJobs", userJobs);//当前用户的职位
	    model.addAttribute("userJobId", userJobId);
	    model.addAttribute("reviewCompanyId", reviewCompanyId);
		model.addAttribute("reviewLevel", reviewLevel);		 
		model.addAttribute("companyId", companyId);
	    model.addAttribute("activityHotelGroup", activityHotelGroup);       
	   
        model.addAttribute("typeId", typeId);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));		
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		model.addAttribute("review", review);
		return "modules/costreview/hotelList";
	}	 
 
  
	/**
	 * 财务需要录入成本的列表页
	 * @return
	 * typeId:产品类型:散拼，机票..
	 * reviewLevel: 审核层级 
	 * **/ 
   /*
  @RequestMapping(value="list/{typeId}/{reviewLevel}")
  public String list(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,
		  Model model,@PathVariable(value="typeId") Integer typeId,
		  @PathVariable(value="reviewLevel") Integer reviewLevel){		
	   String review = request.getParameter("review");
	   if(!StringUtils.isNotBlank(review)){
		   review="1";
	   }
	   User user = UserUtils.getUser();
	   
	   
	   Long companyId = user.getCompany().getId();
	   
	
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model, user);
		
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");		
		Long supplierId = null;
		if(StringUtils.isNotBlank(request.getParameter("supplierId"))){
			try {
				supplierId = StringUtils.toLong(request.getParameter("supplierId"));
			} catch (NumberFormatException e) {
			}
		}	
		String groupCode=request.getParameter("groupCode");		
		
		model.addAttribute("supplierList",supplierInfoService.findSupplierInfoByCompanyId(companyId));
		model.addAttribute("groupCode", groupCode);
		
		if( companyId == 71 || companyId==68){
			boolean findJob=false;			
			List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(REVIEW_FLOWTYPE_COST);
			for (UserJob userjob : userJobs) { 
				   if (userjob.getOrderType()==typeId){					     
						 List<Long> reviewCompanyId = new ArrayList<Long>();
						 Long deptId=(long)0;
						 if(userjob.getDeptLevel()==1){
							  deptId=userjob.getDeptId();
						   }else if(userjob.getDeptLevel()==2){
							  deptId=userjob.getParentDept();
						   }
						   reviewCompanyId = reviewCompanyDao.findReviewCompanyList(companyId, REVIEW_FLOWTYPE_COST,deptId);
						   if(reviewCompanyId.size()>0){
						       List<Integer> reviewList;						      
						       reviewList =reviewRoleLevelDao.findReviewJobLevel(userjob.getJobId(),(long)reviewCompanyId.get(0));
						       findJob=true;
						       if(reviewLevel==1 && reviewList.get(0)!=1){
						    	  reviewLevel= -1;
						       } else {
						         reviewLevel=reviewList.get(0);						       
						       }						      
						   }					 
					  break;
				  }
			  }	
			//新行者没有配置职位，设置 reviwLevel=-1,列表页返回空记录
			 if(findJob==false) reviewLevel= -1;		 
		}	
		 model.addAttribute("reviewLevel", reviewLevel);
		 model.addAttribute("companyId", companyId);
		 Page<ActivityGroupView> page = activityGroupViewService.findGroupReview(new Page<ActivityGroupView>(request, response), travelActivity,  
				settlementAdultPriceStart, settlementAdultPriceEnd, groupCode,supplierId,typeId,review,reviewLevel, companyId,common);	
		model.addAttribute("page", page);
	    model.addAttribute("travelActivity", travelActivity);       
       model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
       model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
       model.addAttribute("typeId", typeId);
       model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getCompanyIdForData())));
		model.addAttribute("payTypes", DictUtils.getKeyIntMap(Context.PAY_TYPE));		
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		model.addAttribute("targetAreaList", activityGroupViewService.findTargetArea());
		model.addAttribute("review", review);
		return "modules/costreview/costReviewList";
	} */	 


	
	@ResponseBody
	@RequestMapping(value="save", method=RequestMethod.POST)
	public String save(@RequestParam String name, @RequestParam BigDecimal price,@RequestParam String comment,
    @RequestParam Long activityId, @RequestParam Integer quantity,@RequestParam Integer currencyId,    
    @RequestParam Integer overseas,@RequestParam Integer supplyType,@RequestParam Integer supplyId,        
    @RequestParam String supplyName,@RequestParam Integer budgetType){	
		
		CostRecord costRecord= new CostRecord();
		costRecord.setName(name);		
		costRecord.setPrice(price);
		costRecord.setQuantity(quantity);
		costRecord.setSupplyId(supplyId);
		costRecord.setSupplyType(supplyType);
		costRecord.setOrderType(Context.ORDER_TYPE_SP);
		
		if(supplyType==1){
			Agentinfo agentinfo= new Agentinfo();
			agentinfo=agentinfoService.findOne((long)supplyId);
			costRecord.setSupplyName(agentinfo.getAgentName());
		}else {
		    costRecord.setSupplyName(supplyName);
		}
		costRecord.setActivityId(activityId);
	
		costRecord.setBudgetType(budgetType);
		costRecord.setOverseas(overseas);
		
		costRecord.setCurrencyId(currencyId);
		
		if(StringUtils.isNotEmpty(comment)){
			costRecord.setComment(comment);
		}
		costManageService.saveCostRecord(costRecord);	
		return "[{\"result\":\"ok\"}]";
	}
	
	/*
	@ResponseBody
	@RequestMapping(value="modify", method=RequestMethod.POST)
	public String modify(@RequestParam Long id,@RequestParam Integer review){	
		 
        costManageService.updateReview(id, review);		
		return "[{\"result\":\"ok\"}]";
	} */
	
	@ResponseBody
	@RequestMapping(value="delete", method=RequestMethod.POST)
	public String delete(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costManageService.deleteCostRecord(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}	
	
	
	
	//驳回团期成本
	@ResponseBody
	@RequestMapping(value="denyactivitycost", method=RequestMethod.POST)
	public String denyActivityCost(@RequestParam(required=true) Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Integer typeId,@RequestParam String remark){
		ActivityGroup activityGroup= activityGroupService.findById(id);	
		
		Long companyId = UserUtils.getUser().getCompany().getId();	
		int topLevel;
		if(companyId==(long)71) topLevel=5; //新行者审核流程：部门经理审核-团队会计审核-财务副经理审核-关总（董事长）审核-出纳
		else topLevel=5;
		
		Integer nowLevel=activityGroup.getNowLevel();
		if (nowLevel==null) {
			nowLevel=1;
		}
		if(nowLevel != reviewLevel || reviewLevel > topLevel) {
			return "[{\"result\":\"ok\"}]";
		}
		int review=Context.REVIEW_COST_FAIL;
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setRid(id);
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(0); //审核驳回
		costRecordLog.setOrderType(typeId); //1 团期,6,签证,7机票
		if (remark==null) remark="";
		costRecordLog.setRemark(remark);
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());		
		activityGroupService.updateReview(id, review, reviewLevel,costRecordLog);
		return "[{\"result\":\"ok\"}]";
	}
	
	//通过团期成本审核
	@ResponseBody
	@RequestMapping(value="passactivitycost", method=RequestMethod.POST)
	public String passActivityCost(@RequestParam (required=true) Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Integer typeId){		
		ActivityGroup activityGroup= activityGroupService.findById(id);		 
		Integer nowLevel=activityGroup.getNowLevel();

		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();	   
		
		int topLevel;
		if(companyId==(long)71){
			topLevel=5; //新行者审核流程：部门经理审核-团队会计审核-财务副经理审核-关总（董事长）审核-出纳
		}
		else{
			topLevel=5;	
		}

		if (nowLevel==null) {
			nowLevel=1;
		}
		if(nowLevel != reviewLevel) {
			return "[{\"result\":\"ok\"}]";
		}		
		String remark="";		
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setRid(id);
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(1);//审核通过
		costRecordLog.setOrderType(typeId); //1 团期,6,签证,7机票	
		costRecordLog.setRemark(remark);
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());
		
		if (nowLevel<topLevel) {
			nowLevel++;
			activityGroupService.updateReview(id, Context.REVIEW_COST_WAIT,nowLevel,costRecordLog);
			costManageService.updateCostRecord(id, typeId, Context.REVIEW_COST_WAIT);
		}else if (nowLevel == topLevel) {
			
			nowLevel++;
		    activityGroupService.updateReview(id, Context.REVIEW_COST_PASS,nowLevel,costRecordLog);
		  //审核流程走完，更新cost_record 记录
		    costManageService.updateCostRecord(id, typeId, Context.REVIEW_COST_PASS);
		} 		 
		return "[{\"result\":\"ok\"}]";
	}
	
	
	//驳回机票成本
	@ResponseBody
	@RequestMapping(value="denyaircost", method=RequestMethod.POST)
	public String denyAirCost(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam String remark){
		int review=Context.REVIEW_COST_FAIL;	
		
		Long companyId = UserUtils.getUser().getCompany().getId();	
		int topLevel;
		if(companyId==(long)71) topLevel=5; //新行者审核流程：部门经理审核-团队会计审核-财务副经理审核-关总（董事长）审核-出纳
		else topLevel=5;
		
		ActivityAirTicket activityAirTicket=activityAirTicketService.findById(id);
		
		Integer nowLevel=activityAirTicket.getNowLevel();
		if (nowLevel==null) {
			nowLevel=1;
		}
		if(nowLevel != reviewLevel || reviewLevel > topLevel) {
			return "[{\"result\":\"ok\"}]";
		}
		
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setRid(id);		
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(0); //审核驳回
		costRecordLog.setOrderType(7); //1 团期,6,签证,7机票
		if (remark==null) remark="";
		costRecordLog.setRemark(remark);
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());			
		activityAirTicketService.updateReview(id, review, nowLevel,costRecordLog);
		return "[{\"result\":\"ok\"}]";
	}
	
	//通过机票成本审核 
	@ResponseBody
	@RequestMapping(value="passaircost", method=RequestMethod.POST)
	public String passAirCost(@RequestParam(required=true)  Long id,@RequestParam (required=true) Integer reviewLevel){
		ActivityAirTicket  ativityAirTicket=new  ActivityAirTicket();
		ativityAirTicket= activityAirTicketService.getActivityAirTicketById(id);		 
		Integer nowLevel=ativityAirTicket.getNowLevel();
		
		Long companyId = UserUtils.getUser().getCompany().getId();	
		int topLevel;
		if(companyId==(long)71) topLevel=5; //新行者审核流程：部门经理审核-团队会计审核-财务副经理审核-关总（董事长）审核-出纳
		else topLevel=5;
		
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setRid(id);
		if (nowLevel==null) {
			nowLevel=1;
		}	
		if(nowLevel != reviewLevel) {
			return "[{\"result\":\"ok\"}]";
		}
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(1);//审核通过
		costRecordLog.setOrderType(7); //1 团期,6,签证,7机票	
		costRecordLog.setRemark("");
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());		
	   
		if (nowLevel<topLevel) {
			nowLevel++;
			activityAirTicketService.updateReview(id, Context.REVIEW_COST_WAIT,nowLevel,costRecordLog);
			costManageService.updateCostRecord(id, 7, Context.REVIEW_COST_WAIT);
			
		}else if (nowLevel == topLevel) {
			nowLevel++;
			activityAirTicketService.updateReview(id, Context.REVIEW_COST_PASS,nowLevel,costRecordLog);
			costManageService.updateCostRecord(id, 7, Context.REVIEW_COST_PASS);
		}
		 
		return "[{\"result\":\"ok\"}]";
	}
	
	//驳回签证成本
	@ResponseBody
	@RequestMapping(value="denyvisacost", method=RequestMethod.POST)
	public String denyVisaCost(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam String remark){
		int review=Context.REVIEW_COST_FAIL;		
		VisaProducts visaProducts= visaProductsService.findByVisaProductsId(id);
		
		Long companyId = UserUtils.getUser().getCompany().getId();	
		int topLevel;
		if(companyId==(long)71) topLevel=5; //新行者审核流程：部门经理审核-团队会计审核-财务副经理审核-关总（董事长）审核-出纳
		else topLevel=5;
		
		Integer nowLevel=visaProducts.getNowLevel();
		if(nowLevel != reviewLevel || reviewLevel > topLevel) {
			return "[{\"result\":\"ok\"}]";
		}
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setRid(id);
		
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(0); //审核驳回
		costRecordLog.setOrderType(6); //1 团期,6,签证,7机票
		if (remark==null) remark="";
		costRecordLog.setRemark(remark);
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());			
		visaProductsService.updateReview(id, review, nowLevel,costRecordLog);
		return "[{\"result\":\"ok\"}]";
	}
	
	//驳回单条成本
	@ResponseBody
	@RequestMapping(value="deny", method=RequestMethod.POST)
	public String deny(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){

//		int review=Context.REVIEW_COST_FAIL;	
		CostRecord costRecord=new CostRecord();
		costRecord=costRecordDao.findOne(id);
		costRecord.setReview(Context.REVIEW_COST_FAIL);
		costRecordDao.save(costRecord);			
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setRid(costRecord.getActivityId());
		costRecordLog.setCostId(id);
		costRecordLog.setCostName(costRecord.getName());
		costRecordLog.setNowLevel(reviewLevel);
		costRecordLog.setResult(0);//审核失败		
		costRecordLog.setRemark("");
		costRecordLog.setOrderType(orderType); 	
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());	
		costRecordLog.setLogType(0);
		costRecordLogDao.save(costRecordLog);	
		//costManageService.updateCostRecord(id, review);
		return "[{\"result\":\"ok\"}]";
	}
	
	/**
	 * 成本审核批量审批--驳回
	 * @author zzk
	 * @param code
	 * @param reviewComment
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="batchDeny", method=RequestMethod.POST)
	public String batchDeny(@RequestParam String code,@RequestParam String reviewComment) {
		String[] arr = code.split(",");
		for (int i = 0; i < arr.length; i++) {
			if ("0".equals(arr[i])) {
				continue;
			} else {
				String[] str = arr[i].split("_");
				Long id = Long.parseLong(str[0]);
				Integer reviewLevel = Integer.parseInt(str[1]);
//				Long reviewCompanyId = Long.parseLong(str[2]);
				int orderType = Integer.parseInt(str[3]);
//				ReviewCompany reviewCompany= new ReviewCompany();
//				reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
				
				CostRecord costRecord=new CostRecord();
				costRecord=costRecordDao.findOne(id);
				costRecord.setReview(Context.REVIEW_COST_FAIL);
				costRecord.setReviewComment(reviewComment);
				costRecordDao.save(costRecord);	
				
				CostRecordLog costRecordLog =new CostRecordLog();
				costRecordLog.setRid(costRecord.getActivityId());
				costRecordLog.setCostId(id);
				costRecordLog.setCostName(costRecord.getName());
				costRecordLog.setNowLevel(reviewLevel);
				costRecordLog.setResult(0);//审核失败		
				costRecordLog.setRemark(reviewComment);
				costRecordLog.setOrderType(orderType); 	
				costRecordLog.setCreateDate(new Date());
				costRecordLog.setCreateBy(UserUtils.getUser().getId());		
				costRecordLog.setLogType(0);
				costRecordLogDao.save(costRecordLog);
			}
		}
		return "[{\"result\":\"ok\"}]";
	}
	
	//驳回单条成本
	@ResponseBody
	@RequestMapping(value="denyIsland", method=RequestMethod.POST)
	public String denyIsland(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){

//		int review=Context.REVIEW_COST_FAIL;	
		CostRecordIsland costRecord=new CostRecordIsland();
		costRecord=costRecordIslandDao.findOne(id);
		costRecord.setReview(Context.REVIEW_COST_FAIL);
		costRecordIslandDao.save(costRecord);			
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setActivityUuid(costRecord.getActivityUuid());
		costRecordLog.setCostId(id);
		costRecordLog.setCostName(costRecord.getName());
		costRecordLog.setNowLevel(reviewLevel);
		costRecordLog.setResult(0);//审核失败		
		costRecordLog.setRemark("");
		costRecordLog.setOrderType(orderType); 	
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());		
		costRecordLogDao.save(costRecordLog);	
		//costManageService.updateCostRecord(id, review);
		return "[{\"result\":\"ok\"}]";
	}
	
	//驳回单条成本
		@ResponseBody
		@RequestMapping(value="denyHotel", method=RequestMethod.POST)
		public String denyHotel(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
				@RequestParam (required=true) Integer orderType){	
			CostRecordHotel costRecord=new CostRecordHotel();
			costRecord=costRecordHotelDao.findOne(id);
			costRecord.setReview(Context.REVIEW_COST_FAIL);
			costRecordHotelDao.save(costRecord);			
			CostRecordLog costRecordLog =new CostRecordLog();
			costRecordLog.setActivityUuid(costRecord.getActivityUuid());
			costRecordLog.setCostId(id);
			costRecordLog.setCostName(costRecord.getName());
			costRecordLog.setNowLevel(reviewLevel);
			costRecordLog.setResult(0);//审核失败		
			costRecordLog.setRemark("");
			costRecordLog.setOrderType(orderType); 	
			costRecordLog.setCreateDate(new Date());
			costRecordLog.setCreateBy(UserUtils.getUser().getId());		
			costRecordLogDao.save(costRecordLog);	
			//costManageService.updateCostRecord(id, review);
			return "[{\"result\":\"ok\"}]";
		}
	
	//审核通过单条成本
	@ResponseBody
	@RequestMapping(value="pass", method=RequestMethod.POST)
	public String pass(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){
//		long companyId = UserUtils.getUser().getCompany().getId();
		ReviewCompany reviewCompany= new ReviewCompany();
		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
//		int isEnd =0;
		
		int topLevel=reviewCompany.getTopLevel();
		CostRecord costRecord=new CostRecord();
		costRecord=costRecordDao.findOne(id);
		Integer nowLevel=costRecord.getNowLevel();
		if (nowLevel==null) {
			nowLevel=1;
		}
		if(! nowLevel.equals( reviewLevel)) {
			return "[{\"result\":\"ok\"}]";
		}		
		List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
		
		if (nowLevel<topLevel && isEndReviewList.size()==0) {
			costRecord.setNowLevel(reviewLevel+1);		
		}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
			costRecord.setReview(Context.REVIEW_COST_PASS);
			costRecord.setNowLevel(topLevel+1);	
		}else if (nowLevel == topLevel) {			
			costRecord.setReview(Context.REVIEW_COST_PASS);
			costRecord.setNowLevel(reviewLevel+1);
		}		
		costRecord.setUpdateBy(UserUtils.getUser().getId());
		costRecord.setUpdateDate(new Date());
		
		costRecordDao.save(costRecord);	
		
		if ((nowLevel == topLevel || (nowLevel<topLevel && isEndReviewList.size()>0)) && costRecord.getBudgetType()==0){
			//环球行：预算成本审核通过后才计入实际成本
			CostRecord cost=new CostRecord();
			cost.setBudgetType(1);
			cost.setActivityId(costRecord.getActivityId());
			cost.setName(costRecord.getName());
			cost.setCurrencyId(costRecord.getCurrencyId());
			cost.setOverseas(costRecord.getOverseas());
			cost.setQuantity(costRecord.getQuantity());
			cost.setPrice(costRecord.getPrice());
			if(costRecord.getRate()!=null) cost.setRate(costRecord.getRate());
			cost.setReview(4); //实际成本需要点按键从新提交走付款流程
			cost.setNowLevel(1);
			cost.setReviewCompanyId(costRecord.getReviewCompanyId());
			cost.setOrderType(costRecord.getOrderType());
			cost.setSupplyType(costRecord.getSupplyType());
			cost.setSupplyId(costRecord.getSupplyId());
			cost.setSupplyName(costRecord.getSupplyName());
			cost.setBankAccount(costRecord.getBankAccount());
			cost.setBankName(costRecord.getBankName());	
			cost.setCurrencyAfter(costRecord.getCurrencyAfter());
			cost.setCreateBy(costRecord.getCreateBy());
			if(StringUtils.isNotEmpty(costRecord.getComment())){
				cost.setComment(costRecord.getComment());
			}
			cost.setPriceAfter(costRecord.getPriceAfter());
			cost.setReviewType(0);
			cost.setCreateDate(new Date());
			costRecordDao.save(cost);
		}
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setRid(costRecord.getActivityId());
		costRecordLog.setCostId(id);
		costRecordLog.setCostName(costRecord.getName()+"_成本审核");
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(1);//审核通过
		costRecordLog.setOrderType(orderType);
		costRecordLog.setRemark("");
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());
		costRecordLog.setLogType(0);
		costRecordLogDao.save(costRecordLog);
		return "[{\"result\":\"ok\"}]";
	}
	
	/**
	 * 成本审核批量审批--通过
	 * @author zzk
	 * @param code
	 * @param reviewComment
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="batchPass", method=RequestMethod.POST)
	public String batchPass(@RequestParam String code,@RequestParam String reviewComment) {
		String[] arr = code.split(",");
		for (int i = 0; i < arr.length; i++) {
			if ("0".equals(arr[i])) {
				continue;
			} else {
				String[] str = arr[i].split("_");
				Long id = Long.parseLong(str[0]);
				Integer reviewLevel = Integer.parseInt(str[1]);
				Long reviewCompanyId = Long.parseLong(str[2]);
				int orderType = Integer.parseInt(str[3]);
				ReviewCompany reviewCompany= new ReviewCompany();
				reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
				int topLevel=reviewCompany.getTopLevel();
				CostRecord costRecord=new CostRecord();
				costRecord=costRecordDao.findOne(id);
				Integer nowLevel=costRecord.getNowLevel();
				if (nowLevel==null) {
					nowLevel=1;
				}
				if(! nowLevel.equals( reviewLevel)) {
					return "[{\"result\":\"ok\"}]";
				}		
				List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
				
				if (nowLevel<topLevel && isEndReviewList.size()==0) {
					costRecord.setNowLevel(reviewLevel+1);		
				}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
					costRecord.setReview(Context.REVIEW_COST_PASS);
					costRecord.setNowLevel(topLevel+1);	
				}else if (nowLevel == topLevel) {			
					costRecord.setReview(Context.REVIEW_COST_PASS);
					costRecord.setNowLevel(reviewLevel+1);
				}		
				costRecord.setUpdateBy(UserUtils.getUser().getId());
				costRecord.setUpdateDate(new Date());
				costRecord.setReviewComment(reviewComment);
				costRecordDao.save(costRecord);
				
				if ((nowLevel == topLevel || (nowLevel<topLevel && isEndReviewList.size()>0)) && costRecord.getBudgetType()==0){
					//环球行：预算成本审核通过后才计入实际成本
					CostRecord cost=new CostRecord();
					cost.setBudgetType(1);
					cost.setActivityId(costRecord.getActivityId());
					cost.setName(costRecord.getName());
					cost.setCurrencyId(costRecord.getCurrencyId());
					cost.setOverseas(costRecord.getOverseas());
					cost.setQuantity(costRecord.getQuantity());
					cost.setPrice(costRecord.getPrice());
					if(costRecord.getRate()!=null) cost.setRate(costRecord.getRate());
					cost.setReview(4); //实际成本需要点按键从新提交走付款流程
					cost.setNowLevel(1);
					cost.setReviewCompanyId(costRecord.getReviewCompanyId());
					cost.setOrderType(costRecord.getOrderType());
					cost.setSupplyType(costRecord.getSupplyType());
					cost.setSupplyId(costRecord.getSupplyId());
					cost.setSupplyName(costRecord.getSupplyName());
					cost.setBankAccount(costRecord.getBankAccount());
					cost.setBankName(costRecord.getBankName());	
					cost.setCurrencyAfter(costRecord.getCurrencyAfter());
					cost.setCreateBy(costRecord.getCreateBy());
					if(StringUtils.isNotEmpty(costRecord.getComment())){
						cost.setComment(costRecord.getComment());
					}
					cost.setPriceAfter(costRecord.getPriceAfter());
					cost.setReviewType(0);
					cost.setCreateDate(new Date());
					costRecordDao.save(cost);
				}
				
				CostRecordLog costRecordLog =new CostRecordLog();
				costRecordLog.setRid(costRecord.getActivityId());
				costRecordLog.setCostId(id);
				costRecordLog.setCostName(costRecord.getName()+"_成本审核");
				costRecordLog.setNowLevel(nowLevel);
				costRecordLog.setResult(1);//审核通过
				costRecordLog.setOrderType(orderType);
				costRecordLog.setRemark("");
				costRecordLog.setCreateDate(new Date());
				costRecordLog.setCreateBy(UserUtils.getUser().getId());
				costRecordLog.setLogType(0);
				costRecordLogDao.save(costRecordLog);
			}
		}
		return "[{\"result\":\"ok\"}]";
	}
	
	//审核通过单条成本
	@ResponseBody
	@RequestMapping(value="passIsland", method=RequestMethod.POST)
	public String passIsland(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){
//		long companyId = UserUtils.getUser().getCompany().getId();
		ReviewCompany reviewCompany= new ReviewCompany();
		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
//		int isEnd =0;
		
		int topLevel=reviewCompany.getTopLevel();
		CostRecordIsland costRecord=new CostRecordIsland();
		costRecord=costRecordIslandDao.findOne(id);
		Integer nowLevel=costRecord.getNowLevel();
		if (nowLevel==null) {
			nowLevel=1;
		}
		if(! nowLevel.equals( reviewLevel)) {
			return "[{\"result\":\"ok\"}]";
		}
		
		 List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
		
		if (nowLevel<topLevel && isEndReviewList.size()==0) {
			costRecord.setNowLevel(nowLevel+1);		
		}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
			costRecord.setReview(Context.REVIEW_COST_PASS);
			costRecord.setNowLevel(topLevel+1);	
		}else if (nowLevel == topLevel) {			
			costRecord.setReview(Context.REVIEW_COST_PASS);
			costRecord.setNowLevel(nowLevel+1);
		}			
		costRecordIslandDao.save(costRecord);	
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setActivityUuid(costRecord.getActivityUuid());
		costRecordLog.setCostId(id);
		costRecordLog.setCostName(costRecord.getName());
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(1);//审核通过
		costRecordLog.setOrderType(orderType); 	
		costRecordLog.setRemark("");
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());		
		costRecordLogDao.save(costRecordLog);	
		return "[{\"result\":\"ok\"}]";
	}
	
	//酒店审核通过单条成本
	@ResponseBody
	@RequestMapping(value="passHotel", method=RequestMethod.POST)
	public String passHotel(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){
//		long companyId = UserUtils.getUser().getCompany().getId();
		ReviewCompany reviewCompany= new ReviewCompany();
		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
//		int isEnd =0;
		
		int topLevel=reviewCompany.getTopLevel();
		CostRecordHotel costRecord=new CostRecordHotel();
		costRecord=costRecordHotelDao.findOne(id);
		Integer nowLevel=costRecord.getNowLevel();
		if (nowLevel==null) {
			nowLevel=1;
		}
		if(! nowLevel.equals( reviewLevel)) {
			return "[{\"result\":\"ok\"}]";
		}		
		List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
		if (nowLevel<topLevel && isEndReviewList.size()==0) {
			costRecord.setNowLevel(nowLevel+1);		
		}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
			costRecord.setReview(Context.REVIEW_COST_PASS);
			costRecord.setNowLevel(topLevel+1);	
		}else if (nowLevel == topLevel) {			
			costRecord.setReview(Context.REVIEW_COST_PASS);
			costRecord.setNowLevel(nowLevel+1);
		}			
		costRecordHotelDao.save(costRecord);	
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setActivityUuid(costRecord.getActivityUuid());
		costRecordLog.setCostId(id);
		costRecordLog.setCostName(costRecord.getName());
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(1);//审核通过
		costRecordLog.setOrderType(orderType); 	
		costRecordLog.setRemark("");
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());		
		costRecordLogDao.save(costRecordLog);	
		return "[{\"result\":\"ok\"}]";
	}
	
    /*成本批量审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="saveCostList")
  	public String saveCostList(@RequestParam(required=true) String costList,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){
 		ReviewCompany reviewCompany= new ReviewCompany();
		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
		int topLevel=reviewCompany.getTopLevel();		
		
 		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
 		long userId = UserUtils.getUser().getId();
 		List<UserJob> userJobList = new ArrayList<UserJob>();
 		userJobList=userJobDao.getUserJobList(userId);	
 		List<Long> jobIds = new ArrayList<Long>();
 		for (UserJob userjob : userJobList) { 
 			  if(userjob.getDeptLevel()==1 && reviewCompany.getDeptId().equals(userjob.getDeptId()) && userjob.getOrderType()==orderType)
 					jobIds.add(userjob.getJobId());				 
 			 else if(userjob.getDeptLevel()==2 && reviewCompany.getDeptId().equals(userjob.getParentDept()) && userjob.getOrderType()==orderType)
 				    jobIds.add(userjob.getJobId());	
 		} 		
 		List<Integer> levelList = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,jobIds);
 	    int reviewLevel=levelList.get(0);
		
 		String tmpList[]=costList.split(","); 		
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && ! costId.equals("0")){ 				
 				CostRecord costRecord=new CostRecord();
 				costRecord=costRecordDao.findOne(Long.valueOf(costId));
 				if(costRecord.getReview()!= 1) continue;
 				Integer nowLevel=costRecord.getNowLevel();
 				if (nowLevel==null) {
 					nowLevel=1;
 				} 
 				if(nowLevel != reviewLevel) continue;
 				
 				List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
 				 
 				if (nowLevel<topLevel && isEndReviewList.size()==0) {
 					costRecord.setNowLevel(reviewLevel+1);		
 				}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
 					costRecord.setReview(Context.REVIEW_COST_PASS);
 					costRecord.setNowLevel(topLevel+1);	
 				}else if (nowLevel == topLevel) {			
 					costRecord.setReview(Context.REVIEW_COST_PASS);
 					costRecord.setNowLevel(reviewLevel+1);
 				}		
 				costRecord.setUpdateBy(userId);
 				costRecord.setUpdateDate(new Date());
 				costRecordDao.save(costRecord);	 				
 				/*
 				if (companyId==68 && nowLevel == topLevel && costRecord.getBudgetType()==0){
 					//环球行：预算成本审核通过后才计入实际成本
 					CostRecord cost=new CostRecord();
 					cost.setBudgetType(1);
 					cost.setActivityId(costRecord.getActivityId());
 					cost.setName(costRecord.getName());
 					cost.setCurrencyId(costRecord.getCurrencyId());
 					cost.setOverseas(costRecord.getOverseas());
 				    cost.setQuantity(costRecord.getQuantity());
 				    cost.setPrice(costRecord.getPrice());
 				    if(costRecord.getRate()!=null) cost.setRate(costRecord.getRate());
 				    cost.setReview(4);//实际成本需要点按键从新提交走付款流程		 
 				    cost.setNowLevel(1);
 				    cost.setReviewCompanyId(costRecord.getReviewCompanyId());
 				    cost.setOrderType(costRecord.getOrderType());
 				    cost.setSupplyType(costRecord.getSupplyType());
 				    cost.setSupplyId(costRecord.getSupplyId());		    
 				    cost.setSupplyName(costRecord.getSupplyName());
 				    cost.setBankAccount(costRecord.getBankAccount());
 				    cost.setBankName(costRecord.getBankName());
 				    if(StringUtils.isNotEmpty(costRecord.getComment())){
 				      cost.setComment(costRecord.getComment());
 				    }
 				    cost.setPriceAfter(costRecord.getPriceAfter());
 				    cost.setReviewType(0);
 				    cost.setCurrencyAfter(costRecord.getCurrencyAfter());
 				    cost.setCreateBy(costRecord.getCreateBy());
 				    cost.setCreateDate(new Date());
 				    costRecordDao.save(cost);
 				}*/
 				CostRecordLog costRecordLog =new CostRecordLog();
 				costRecordLog.setRid(costRecord.getActivityId());
 				costRecordLog.setCostId(Long.valueOf(costId));
 				costRecordLog.setCostName(costRecord.getName()+"_成本审核");
 				costRecordLog.setNowLevel(nowLevel);
 				costRecordLog.setResult(1);//审核通过
 				costRecordLog.setOrderType(orderType); 	
 				costRecordLog.setRemark("");
 				costRecordLog.setCreateDate(new Date());
 				costRecordLog.setCreateBy(UserUtils.getUser().getId());		
 				costRecordLogDao.save(costRecordLog);							
 				
 			}
 		}
  		return "";
  	} 
 	
	
    /*海岛成本批量审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="saveCostIslandList")
  	public String saveCostIslandList(@RequestParam(required=true) String costList,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){
 		long companyId = UserUtils.getUser().getCompany().getId();
 		ReviewCompany reviewCompany= new ReviewCompany();
		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
		int topLevel=reviewCompany.getTopLevel();		
 		String tmpList[]=costList.split(","); 		
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && ! costId.equals("0")){ 				
 				CostRecordIsland costRecord=new CostRecordIsland();
 				costRecord=costRecordIslandDao.findOne(Long.valueOf(costId));
 				if(costRecord.getReview()!= 1) continue;
 				Integer nowLevel=costRecord.getNowLevel();
 				if (nowLevel==null) {
 					nowLevel=1;
 				} 
 				
 				 List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
 				 
 				if (nowLevel<topLevel && isEndReviewList.size()==0) {
 					costRecord.setNowLevel(nowLevel+1);		
 				}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
 					costRecord.setReview(Context.REVIEW_COST_PASS);
 					costRecord.setNowLevel(topLevel+1);	
 				}else if (nowLevel == topLevel) {			
 					costRecord.setReview(Context.REVIEW_COST_PASS);
 					costRecord.setNowLevel(nowLevel+1);
 				}		
 				
 				costRecordIslandDao.save(costRecord);	 				
 				if (companyId==68 && nowLevel == topLevel && costRecord.getBudgetType()==0){
 					//环球行：预算成本审核通过后才计入实际成本
 					CostRecordIsland cost=new CostRecordIsland();
 					cost.setBudgetType(1);
 					cost.setActivityUuid(costRecord.getActivityUuid()); 					
 					cost.setName(costRecord.getName());
 					cost.setCurrencyId(costRecord.getCurrencyId());
 					cost.setOverseas(costRecord.getOverseas());
 				    cost.setQuantity(costRecord.getQuantity());
 				    cost.setPrice(costRecord.getPrice());
 				    if(costRecord.getRate()!=null) cost.setRate(costRecord.getRate());
 				    cost.setReview(4);//实际成本需要点按键从新提交走付款流程		 
 				    cost.setNowLevel(1);
 				    cost.setReviewCompanyId(costRecord.getReviewCompanyId());
 				    cost.setOrderType(costRecord.getOrderType());
 				    cost.setSupplyType(costRecord.getSupplyType());
 				    cost.setSupplyId(costRecord.getSupplyId());		    
 				    cost.setSupplyName(costRecord.getSupplyName());
 				    cost.setBankAccount(costRecord.getBankAccount());
 				    cost.setBankName(costRecord.getBankName());
 				    if(StringUtils.isNotEmpty(costRecord.getComment())){
 				      cost.setComment(costRecord.getComment());
 				    }
 				    cost.setPriceAfter(costRecord.getPriceAfter());
 				    cost.setReviewType(0);
 				    cost.setCurrencyAfter(costRecord.getCurrencyAfter());
 				    cost.setCreateBy(costRecord.getCreateBy());
 				    cost.setCreateDate(new Date());
 				    costRecordIslandDao.save(cost);
 				}
 				CostRecordLog costRecordLog =new CostRecordLog();
 				costRecordLog.setActivityUuid(costRecord.getActivityUuid());
 				costRecordLog.setCostId(Long.valueOf(costId));
 				costRecordLog.setCostName(costRecord.getName());
 				costRecordLog.setNowLevel(nowLevel);
 				costRecordLog.setResult(1);//审核通过
 				costRecordLog.setOrderType(orderType); 	
 				costRecordLog.setRemark("");
 				costRecordLog.setCreateDate(new Date());
 				costRecordLog.setCreateBy(UserUtils.getUser().getId());		
 				costRecordLogDao.save(costRecordLog);					
 			}
 		}
  		return "";
  	} 
 	
 	 /*酒店成本批量审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="saveCostHotelList")
  	public String saveCostHotelList(@RequestParam(required=true) String costList,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){
 		long companyId = UserUtils.getUser().getCompany().getId();
 		ReviewCompany reviewCompany= new ReviewCompany();
		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
		int topLevel=reviewCompany.getTopLevel();		
 		String tmpList[]=costList.split(","); 		
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && ! costId.equals("0")){ 				
 				CostRecordHotel costRecord=new CostRecordHotel();
 				costRecord=costRecordHotelDao.findOne(Long.valueOf(costId));
 				if(costRecord.getReview()!= 1) continue;
 				Integer nowLevel=costRecord.getNowLevel();
 				if (nowLevel==null) {
 					nowLevel=1;
 				} 
 				
 				 List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
 				 
 				if (nowLevel<topLevel && isEndReviewList.size()==0) {
 					costRecord.setNowLevel(nowLevel+1);		
 				}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
 					costRecord.setReview(Context.REVIEW_COST_PASS);
 					costRecord.setNowLevel(topLevel+1);	
 				}else if (nowLevel == topLevel) {			
 					costRecord.setReview(Context.REVIEW_COST_PASS);
 					costRecord.setNowLevel(nowLevel+1);
 				}		
 				
 				costRecordHotelDao.save(costRecord);	 				
 				if (companyId==68 && nowLevel == topLevel && costRecord.getBudgetType()==0){
 					//环球行：预算成本审核通过后才计入实际成本
 					CostRecordHotel cost=new CostRecordHotel();
 					cost.setBudgetType(1);
 					cost.setActivityUuid(costRecord.getActivityUuid()); 					
 					cost.setName(costRecord.getName());
 					cost.setCurrencyId(costRecord.getCurrencyId());
 					cost.setOverseas(costRecord.getOverseas());
 				    cost.setQuantity(costRecord.getQuantity());
 				    cost.setPrice(costRecord.getPrice());
 				    if(costRecord.getRate()!=null) cost.setRate(costRecord.getRate());
 				    cost.setReview(4);//实际成本需要点按键从新提交走付款流程		 
 				    cost.setNowLevel(1);
 				    cost.setReviewCompanyId(costRecord.getReviewCompanyId());
 				    cost.setOrderType(costRecord.getOrderType());
 				    cost.setSupplyType(costRecord.getSupplyType());
 				    cost.setSupplyId(costRecord.getSupplyId());		    
 				    cost.setSupplyName(costRecord.getSupplyName());
 				    cost.setBankAccount(costRecord.getBankAccount());
 				    cost.setBankName(costRecord.getBankName());
 				    if(StringUtils.isNotEmpty(costRecord.getComment())){
 				      cost.setComment(costRecord.getComment());
 				    }
 				    cost.setPriceAfter(costRecord.getPriceAfter());
 				    cost.setReviewType(0);
 				    cost.setCurrencyAfter(costRecord.getCurrencyAfter());
 				    cost.setCreateBy(costRecord.getCreateBy());
 				    cost.setCreateDate(new Date());
 				    costRecordHotelDao.save(cost);
 				}
 				CostRecordLog costRecordLog =new CostRecordLog();
 				costRecordLog.setActivityUuid(costRecord.getActivityUuid());
 				costRecordLog.setCostId(Long.valueOf(costId));
 				costRecordLog.setCostName(costRecord.getName());
 				costRecordLog.setNowLevel(nowLevel);
 				costRecordLog.setResult(1);//审核通过
 				costRecordLog.setOrderType(orderType); 	
 				costRecordLog.setRemark("");
 				costRecordLog.setCreateDate(new Date());
 				costRecordLog.setCreateBy(UserUtils.getUser().getId());		
 				costRecordLogDao.save(costRecordLog);					
 			}
 		}
  		return "";
  	} 
 	
 	
	//通过签证成本审核
	@ResponseBody
	@RequestMapping(value="passvisacost", method=RequestMethod.POST)
	public String passVisaCost(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel){
		VisaProducts visaProducts=visaProductsService.findByVisaProductsId(id);		 
		Integer nowLevel=visaProducts.getNowLevel();
		
		Long companyId = UserUtils.getUser().getCompany().getId();	
		int topLevel;
		if(companyId==(long)71) topLevel=5; //新行者审核流程：部门经理审核-团队会计审核-财务副经理审核-关总（董事长）审核-出纳
		else topLevel=5;
		
		if (nowLevel==null) {
			nowLevel=1;
		}
		if(nowLevel != reviewLevel) {
			return "[{\"result\":\"ok\"}]";
		}
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setRid(id);

		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(1);//审核通过
		costRecordLog.setOrderType(6); //1 团期,6,签证,7机票	
		costRecordLog.setRemark("");
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());		
		
			
		if (nowLevel<topLevel) {
			nowLevel++;
			visaProductsService.updateReview(id, Context.REVIEW_COST_WAIT,nowLevel,costRecordLog);
			costManageService.updateCostRecord(id, 6, Context.REVIEW_COST_WAIT);
		}else if (nowLevel == topLevel) {
			nowLevel++;
			visaProductsService.updateReview(id, Context.REVIEW_COST_PASS,nowLevel,costRecordLog);
			costManageService.updateCostRecord(id, 6, Context.REVIEW_COST_PASS);
		}		 
		return "[{\"result\":\"ok\"}]";
	}
	
	/**
	 * 获得当前步骤加载页面
	 * @param activityId
	 * @param groupId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="start/{activityId}/{groupId}/{reviewLevel}/{reviewCompanyId}", method=RequestMethod.GET)
	public String showCurrent(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="activityId") Long activityId, 
			@PathVariable(value="reviewCompanyId") Long reviewCompanyId,
			@PathVariable(value="groupId") Long groupId, Model model,
			HttpServletRequest request, HttpServletResponse response){
		String from = request.getParameter("from");
		TravelActivity travelActivity = travelActivityService.findById(activityId);		
		ActivityGroup activityGroup = activityGroupService.findById(groupId);
		/*
		String costSerial= activityGroup.getCost();
		String incomeSerial= activityGroup.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
		
		Dict dict=dictService.findByValueAndType(travelActivity.getActivityKind().toString(), "order_type");
		model.addAttribute("typename", dict.getLabel());		
		Integer typeId=travelActivity.getActivityKind();
		model.addAttribute("typeId",typeId);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));	
		
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
		List<Map<String, Object>> budgetCost=costManageService.getCost(groupId,typeId,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(groupId,typeId,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		
 		ReviewCompany reviewCompany= new ReviewCompany();
 		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
 		long userId = UserUtils.getUser().getId();
 		List<UserJob> userJobList = new ArrayList<UserJob>();
 		userJobList=userJobDao.getUserJobList(userId);	
 		List<Long> jobIds = new ArrayList<Long>();
 		for (UserJob userjob : userJobList) { 
 			  if(userjob.getDeptLevel()==1 && reviewCompany.getDeptId().equals(userjob.getDeptId()) && userjob.getOrderType()==typeId)
 					jobIds.add(userjob.getJobId());				 
 			 else if(userjob.getDeptLevel()==2 && reviewCompany.getDeptId().equals(userjob.getParentDept()) && userjob.getOrderType()==typeId)
 				    jobIds.add(userjob.getJobId());	
 		}
 		
 		List<Integer> levelList = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,jobIds);
 		model.addAttribute("levelList", levelList);	
		model.addAttribute("reviewCompanyId", reviewCompanyId);	
		
		model.addAttribute("reviewLevel", reviewLevel);	
		
		if(activityGroup != null && travelActivity != null && 
				activityGroup.getTravelActivity() != null && 
				activityGroup.getTravelActivity().getId().equals(travelActivity.getId())){
			
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			orderList = orderService.findOrderListByPayType(orderList, groupId); 			
			model.addAttribute("orderList", orderList.getList());			
			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
			model.addAttribute("travelActivity", travelActivity);
			model.addAttribute("activityGroup", activityGroup);			
			model.addAttribute("type", from);			
			List<CostRecord> budgetOutList = new ArrayList<CostRecord>();
			List<CostRecord> budgetInList = new ArrayList<CostRecord>();
			List<CostRecord> actualOutList = new ArrayList<CostRecord>();
			List<CostRecord> actualInList = new ArrayList<CostRecord>();
			
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);			
			model.addAttribute("curlist", currencylist);				
			model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));			
			List<Dict> supplytypelist=dictService.findByType("supplier_type");
			model.addAttribute("supplytypelist", supplytypelist);
			    budgetInList = costManageService.findCostRecordList(groupId, 0,0,typeId);
				budgetOutList = costManageService.findCostRecordList(groupId, 0,1,typeId);
				
				actualInList = costManageService.findCostRecordList(groupId, 1,0,typeId);
				actualOutList = costManageService.findCostRecordList(groupId, 1,1,typeId);
				
				//判断在成本录入审核时如果有一条审核被拒绝了，到页面上把审核通过的按钮禁用
                List<List<CostRecord>> list = new ArrayList<List<CostRecord>>();
                list.add(budgetInList);
                list.add(budgetOutList);
                list.add(actualInList);
                list.add(budgetOutList);
                boolean isShow =true;
                for(int i=0;i<list.size();i++){
                	if(list.get(i).size()>0){
                		int num = list.get(i).size();
                		for(int a=0;a<num;a++){
                			if(list.get(i).get(a).getReview()==0){
                				isShow=false;
                			}
                			a=num;
                		}
                	}
                	i=list.size();
                }
                model.addAttribute("isShow",isShow);              
				model.addAttribute("budgetInList", budgetInList);
				model.addAttribute("budgetOutList", budgetOutList);				
				model.addAttribute("actualInList", actualInList);
				model.addAttribute("actualOutList", actualOutList);	
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		return "modules/costreview/start";
	}
	

	
	/**
	 * 获得当前步骤加载页面
	 * @param activityId
	 * @param groupId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="read/{activityId}/{groupId}/{reviewLevel}", method=RequestMethod.GET)
	public String readCurrent(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="activityId") Long activityId, 
			@PathVariable(value="groupId") Long groupId, Model model,
			HttpServletRequest request, HttpServletResponse response){
		String from = request.getParameter("from");
		String flag = request.getParameter("flag");
		model.addAttribute("flag", flag);

		String sitemap = request.getParameter("sitemap");
		model.addAttribute("sitemap", sitemap);

		TravelActivity travelActivity = travelActivityService.findById(activityId);		
		ActivityGroup activityGroup = activityGroupService.findById(groupId);
		/*
		String costSerial= activityGroup.getCost();
		String incomeSerial= activityGroup.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
		
		Dict dict=dictService.findByValueAndType(travelActivity.getActivityKind().toString(), "order_type");
		model.addAttribute("typename", dict.getLabel());		
		Integer typeId=travelActivity.getActivityKind();
		model.addAttribute("typeId",typeId);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		String menuid = request.getParameter("menuid");
		if("3".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 返佣付款 > " + dict.getLabel() + "详情页");
			model.addAttribute("head", dict.getLabel()+"返佣付款详情页");
		}else if("2".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 退款付款 > " + dict.getLabel() + "详情页");
			model.addAttribute("head", dict.getLabel() + "退款付款详情页");
		}else if("4".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 借款付款 > " + dict.getLabel() + "详情页");
			model.addAttribute("head", dict.getLabel() + "借款付款详情");
		} else if("1".equals(menuid)) {
	        model.addAttribute("title", "财务 > 结算管理 > 成本付款 > " + dict.getLabel() + "详情页");
	        model.addAttribute("head", dict.getLabel() + "成本付款详情"); 
		} else{
	        model.addAttribute("title","");
	        model.addAttribute("head", "付款审核"); 
	     }
		if(activityGroup != null && travelActivity != null && 
				activityGroup.getTravelActivity() != null && 
				activityGroup.getTravelActivity().getId().equals(travelActivity.getId())){
			
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			orderList = orderService.findOrderListByPayType(orderList, groupId); 
			model.addAttribute("orderList", orderList.getList());
			
			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
			model.addAttribute("travelActivity", travelActivity);
			model.addAttribute("activityGroup", activityGroup);			
			model.addAttribute("type", from);
			
			List<CostRecord> budgetOutList = new ArrayList<CostRecord>();
			List<CostRecord> budgetInList = new ArrayList<CostRecord>();
			List<CostRecord> actualOutList = new ArrayList<CostRecord>();
			List<CostRecord> actualInList = new ArrayList<CostRecord>();
			List<CostRecord> otherCostList = new ArrayList<CostRecord>();
			
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
			
			model.addAttribute("curlist", currencylist);
			model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));
			
			List<Dict> supplytypelist=dictService.findByType("supplier_type");
			model.addAttribute("supplytypelist", supplytypelist);
			    budgetInList = costManageService.findCostRecordList(groupId, 0,0,typeId);
				budgetOutList = costManageService.findCostRecordList(groupId, 0,1,typeId);
				
				actualInList = costManageService.findCostRecordList(groupId, 1,0,typeId);
				actualOutList = costManageService.findCostRecordList(groupId, 1,1,typeId);
				
				otherCostList = costManageService.findCostRecordList(groupId, 2, 0, typeId);
				
				DecimalFormat df = new DecimalFormat("#####0.00");
				for (int i = 0; i < otherCostList.size(); i++) {
					Object obj1 = costManageService.getPayedMoney(otherCostList.get(i).getId());
					String s1 = df.format(Double.parseDouble(obj1==null?"0":obj1.toString()));
					otherCostList.get(i).setPayedMoney(s1);
					Object obj2 = costManageService.getConfirmMoney(otherCostList.get(i).getId());
					String s2 = df.format(Double.parseDouble(obj2==null?"0":obj2.toString()));
					otherCostList.get(i).setConfirmMoney(s2);
				}
				
				model.addAttribute("budgetInList", budgetInList);
				model.addAttribute("budgetOutList", budgetOutList);				
				model.addAttribute("actualInList", actualInList);
				model.addAttribute("actualOutList", actualOutList);	
				model.addAttribute("otherCostList", otherCostList);
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
		List<Map<String, Object>> budgetCost=costManageService.getCost(groupId,typeId,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(groupId,typeId,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		
		Object budgetrefund = costManageService.getRefundSum(groupId, 0, typeId).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(groupId, 0, typeId).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costManageService.getRefundSum(groupId, 1, typeId).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(groupId, 1, typeId).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);
		
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
			model.addAttribute("costAutoPass",costAutoPass);
		}else{
			model.addAttribute("costAutoPass","0");
		}
		
		return "modules/costreview/read";
	}
	
	
	@ResponseBody
	@RequestMapping(value="getReserveByGroupId")
	//public List<Agentinfo> getReserveByGroupId(@RequestParam Long activityGroupId){
		
	//}
	
	//////////////////////////////////////////////////////////
	//列表与查看
	//////////////////////////////////////////////////////////
	
	
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
		    /*if(cost.getStatus().equals(StateMachineContext.getStateValue("start")) || 
					cost.getStatus().equals(StateMachineContext.getStateValue("financeAccepted")) ||
					cost.getStatus().equals(StateMachineContext.getStateValue("operatorSaved")) || 
					cost.getStatus().equals(StateMachineContext.getStateValue("financeSaved"))){
				// do nothing.
			}else{
				json.append(",\"directorRefused\":").append("\"").append("0").append("\"");
			} */
			
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
		
	@Autowired
	private AreaService areaService;

	@Autowired
	AirlineInfoService airlineInfoService;
	
	@Autowired
	AirportInfoService airportInfoService; 
	
	@Autowired
	private AirportService airportService;

	@RequestMapping(value="airTicketList/{reviewLevel}")	
	public String airTicketPreRecordList(@ModelAttribute ActivityAirTicket activityAirTicket, 
			HttpServletRequest request, HttpServletResponse response, Model model,
			@PathVariable(value="reviewLevel") Integer reviewLevel){
		String review = request.getParameter("review");		
		String departureCity = request.getParameter("departureCity");
		String arrivedCity = request.getParameter("arrivedCity");
		String groupOpenDate=request.getParameter("groupOpenDate");
		String groupCloseDate=request.getParameter("groupCloseDate");
		String userJobId = request.getParameter("userJobId");//
		String createByName = request.getParameter("createByName");
		if(departureCity!= null) {
			departureCity =departureCity.trim();
		}
		
		if(arrivedCity!= null) {
			arrivedCity =arrivedCity.trim();
		}		
	
        String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
        String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
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
			for(Dict dict:from_areas){
				if(departureCity.equals(dict.getLabel()))
					departureCityPara=dict.getValue().toString();
			}
			if(StringUtils.isEmpty(departureCityPara)) departureCityPara="-1";
		}
		
		String arrivedCityPara=null;
		if(StringUtils.isNotEmpty(arrivedCity)){
			List<Area> areas=areaService.findAreaByName(arrivedCity);
			if(areas.size()>0) arrivedCityPara=areas.get(0).getId().toString();	
			else arrivedCityPara = "-1";
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		//visaProducts.setProductStatus(new Integer(Context.PRODUCT_ONLINE_STATUS));// 
 		
 		long reviewCompanyId=(long)0; 	 		
 		List<UserJob> userJobs = costManageService.getReviewByFlowType(COSTREVIEW_FLOWTYPE,7); 		
 		List<Map<String, Object>> jobList= new ArrayList<Map<String, Object>>();
 		List<Long> reviewCompanyList = new ArrayList<Long>();
		if(userJobId == null ||userJobId.equals("")){
			if(userJobs.size()>0) {	
				  //首次进入页面，没有选择任何部门职务，取第1个部门职务
				  userJobId=userJobs.get(0).getId().toString();
				  Long deptId=(long)0;
				  if(userJobs.get(0).getDeptLevel()==1){
					  deptId=userJobs.get(0).getDeptId();
				  }else if(userJobs.get(0).getDeptLevel()==2){
					  deptId=userJobs.get(0).getParentDept();
				  } 
				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, COSTREVIEW_FLOWTYPE, deptId);
				if( reviewCompanyList.size() > 0) {					
				  reviewCompanyId = reviewCompanyList.get(0);
				  List<Integer> reviewList;	
				  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,7);
			      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJobs.get(0).getJobId(),reviewCompanyId);
			      if (reviewList.size()>0) {
			    	reviewLevel=reviewList.get(0);				
			      }
				}
			 }
			 else{
				reviewCompanyId=(long)-1;
			 }
		}else{			
			for(UserJob userJob : userJobs){
				     if(userJob.getId() == Long.parseLong(userJobId)){
					  Long deptId=(long)0;
					  if(userJob.getDeptLevel()==1){
						  deptId=userJob.getDeptId();
					  }else if(userJob.getDeptLevel()==2){
						  deptId=userJob.getParentDept();
					  } 
					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId,COSTREVIEW_FLOWTYPE, deptId);
					if( reviewCompanyList.size() > 0) {						
					  reviewCompanyId = reviewCompanyList.get(0);
					  List<Integer> reviewList;	
					  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,7);
				      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJob.getJobId(),reviewCompanyId);
				      if (reviewList.size()>0) {
				    	 reviewLevel=reviewList.get(0);				
				      }
					}
				}
			}
		}	
		// System.out.println("joblist is "+jobList.size());
       // System.out.println("reviewcompanyId is "+reviewCompanyId);
        //System.out.println("level is "+reviewLevel);
		model.addAttribute("reviewCompanyId", reviewCompanyId);        
        model.addAttribute("jobList", jobList);//审核职位列表
        model.addAttribute("userJobs", userJobs);//当前用户的职位
        model.addAttribute("userJobId", userJobId);	
		model.addAttribute("reviewLevel", reviewLevel);	
		model.addAttribute("areas", areaService.findAirportCityList(""));
		ReviewCompany reviewCompany = reviewCompanyDao.getById(reviewCompanyId);
		model.addAttribute("reviewCompany", reviewCompany);
		Page<ActivityAirTicketCost> page = activityAirTicketService.findAirCostReviewPage(new Page<ActivityAirTicketCost>(request, response),
                activityAirTicket, departureCityPara, arrivedCityPara,
                StringUtils.isEmpty(settlementAdultPriceStart) ? null : new BigDecimal(settlementAdultPriceStart),
                StringUtils.isEmpty(settlementAdultPriceEnd) ? null : new BigDecimal(settlementAdultPriceEnd),
                StringUtils.isNotBlank(request.getParameter("airType")) ? request.getParameter("airType") : null,
                groupOpenDate,groupCloseDate,review,reviewLevel,companyId,reviewCompanyId,COSTREVIEW_FLOWTYPE,request.getParameter("orderBy"),createByName);
        
        model.addAttribute("activityAirTicket", activityAirTicket);
        model.addAttribute("page", page);
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        
        model.addAttribute("startingDate", groupOpenDate);
        model.addAttribute("returnDate", groupCloseDate);
        
        model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
        model.addAttribute("departmentList", departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId()));
        //出发城市
        model.addAttribute("departureCity", departureCity);
		//到达城市
        model.addAttribute("arrivedCity", arrivedCity);
        model.addAttribute("airTypes", DictUtils.getSysDicMap("air_Type"));// 机票类型
		model.addAttribute("airportlist", airportService.queryAirportInfos());
		model.addAttribute("traffic_namelist", DictUtils
				.getDictList("traffic_name"));// 航空公司
		model.addAttribute("airspacelist", DictUtils
				.getDictList("airspace_Type"));// 舱位
		model.addAttribute("spaceGradelist", DictUtils                               
				.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("from_areaslist", DictUtils
						.getDictList("from_area"));// 出发城市
		model.addAttribute("arrivedareas", areaService.findByCityList());// 到达城市
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		model.addAttribute("agentId", agentId);
		model.addAttribute("review", review);
		model.addAttribute("departmentId", departmentId);
		return "modules/costreview/airTicketList";
	}
	
	@Autowired
	IAirTicketOrderService  iAirTicketOrderService;
	@Autowired
	private OfficeService officeService;
	
	@RequestMapping(value="airTicketPreRecord/{airTicketId}/{reviewLevel}/{reviewCompanyId}")
	public String preRecord(Model model,@PathVariable Long airTicketId,
			@PathVariable(value="reviewCompanyId") Long reviewCompanyId,
			@PathVariable(value="reviewLevel") Integer reviewLevel){
		model.addAttribute("airTypes", DictUtils.getSysDicMap("air_Type"));// 机票类型
		model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));// 航空公司
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		
		ActivityAirTicket airTicket=activityAirTicketService.getActivityAirTicketById(airTicketId);
		
		String costSerial= airTicket.getCost();
		String incomeSerial= airTicket.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
	 		
 		ReviewCompany reviewCompany= new ReviewCompany();
 		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
 		long userId = UserUtils.getUser().getId();
 		List<UserJob> userJobList = new ArrayList<UserJob>();
 		userJobList=userJobDao.getUserJobList(userId);	
 		List<Long> jobIds = new ArrayList<Long>();
 		for (UserJob userjob : userJobList) { 
 			  if(userjob.getDeptLevel()==1 && reviewCompany.getDeptId().equals(userjob.getDeptId()) && userjob.getOrderType()==7)
 					jobIds.add(userjob.getJobId());				 
 			 else if(userjob.getDeptLevel()==2 && reviewCompany.getDeptId().equals(userjob.getParentDept()) && userjob.getOrderType()==7)
 				    jobIds.add(userjob.getJobId());			
 		}
 		
 		List<Integer> levelList = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,jobIds); 		
 		model.addAttribute("levelList", levelList);	
		model.addAttribute("reviewCompanyId", reviewCompanyId);	
		
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));
		
		model.addAttribute("areas", areaService.findAirportCityList(""));
		model.addAttribute("from_area", DictUtils.getDictList("from_area"));// 出发城市
		model.addAttribute("activityAirTicket",airTicket);		
		model.addAttribute("airportlist", airportService.queryAirportInfos());
		model.addAttribute("costLog",stockDao.findCostRecordLog(airTicketId, 7));
		
		
	      List<Map<String,Object>> airticketOrders =iAirTicketOrderService.queryAirticketOrdersByProductId(airTicketId.toString());
			
	        //计算机票剩余时间
			for(Map<String, Object> temp : airticketOrders){
				com.trekiz.admin.modules.airticketorder.web.AirTicketOrderController.getOrderLeftTime(temp);			
			} 
			
		model.addAttribute("airTicketOrderList", airticketOrders);
		List<Office> officeList = officeService.findSyncOffice();
		model.addAttribute("companyList", officeList);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		
		List<Dict> supplytypelist=dictService.findByType("supplier_type");
		model.addAttribute("supplytypelist", supplytypelist);		
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		model.addAttribute("curlist", currencylist);		
		List<CostRecord> budgetOutList = new ArrayList<CostRecord>();
		List<CostRecord> budgetInList = new ArrayList<CostRecord>();
		List<CostRecord> actualOutList = new ArrayList<CostRecord>();
		List<CostRecord> actualInList = new ArrayList<CostRecord>();

        budgetInList = costManageService.findCostRecordList(airTicketId, 0,0,Context.ORDER_TYPE_JP);
		budgetOutList = costManageService.findCostRecordList(airTicketId, 0,1,Context.ORDER_TYPE_JP);
		actualInList = costManageService.findCostRecordList(airTicketId, 1,0,Context.ORDER_TYPE_JP);
		actualOutList = costManageService.findCostRecordList(airTicketId, 1,1,Context.ORDER_TYPE_JP);
		
		model.addAttribute("budgetInList", budgetInList);
		model.addAttribute("budgetOutList", budgetOutList);		
		model.addAttribute("actualInList", actualInList);
		model.addAttribute("actualOutList", actualOutList);		
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(airTicketId,7);
		List<Map<String, Object>> budgetCost=costManageService.getCost(airTicketId,7,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(airTicketId,7,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		
		return "modules/costreview/airTicketPreRecord";
	}
	
	
	@RequestMapping(value="airTicketRead/{airTicketId}/{reviewLevel}")
	public String readRecord(Model model,@PathVariable Long airTicketId,
			@PathVariable(value="reviewLevel") Integer reviewLevel, HttpServletRequest request, HttpServletResponse response){
		model.addAttribute("airTypes", DictUtils.getSysDicMap("air_Type"));// 机票类型
		model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));// 航空公司
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		
		String flag = request.getParameter("flag");
		model.addAttribute("flag", flag);

		String sitemap = request.getParameter("sitemap");
		model.addAttribute("sitemap", sitemap);

		String menuid = request.getParameter("menuid");
		if("3".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 返佣付款 > 机票详情页");
			model.addAttribute("head", "机票返佣付款详情页");
		}else if("2".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 退款付款 > 机票详情页");
			model.addAttribute("head", "机票退款付款详情页");
		} else if("4".equals(menuid)) {
		   model.addAttribute("title", "财务 > 结算管理 > 借款付款 > 机票详情页");
		   model.addAttribute("head", "机票借款付款详情"); 
	    } else if("1".equals(menuid)) {
		  model.addAttribute("title", "财务 > 结算管理 > 成本付款 > 机票详情页");
		  model.addAttribute("head", "机票成本付款 详情"); 		  
	    } else{
		  model.addAttribute("title","");
		  model.addAttribute("head", "机票成本详情页");
		}

		
		ActivityAirTicket airTicket=activityAirTicketService.getActivityAirTicketById(airTicketId);
		
		//String costSerial= airTicket.getCost();
		//String incomeSerial= airTicket.getIncome();		
		//if(costSerial==null) costSerial="";
		//if(incomeSerial==null) incomeSerial="";
//		Long companyId= UserUtils.getUser().getCompany().getId();	
		//model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		//model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		//model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));
		
		model.addAttribute("areas", areaService.findAirportCityList(""));
		model.addAttribute("from_area", DictUtils.getDictList("from_area"));// 出发城市
		model.addAttribute("activityAirTicket",airTicket);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		
		model.addAttribute("airportlist", airportService.queryAirportInfos());
		model.addAttribute("costLog",stockDao.findCostRecordLog(airTicketId, 7));
	    List<Map<String,Object>> airticketOrders =iAirTicketOrderService.queryAirticketOrdersByProductId(airTicketId.toString());
		//计算机票剩余时间
		for(Map<String, Object> temp : airticketOrders){
				com.trekiz.admin.modules.airticketorder.web.AirTicketOrderController.getOrderLeftTime(temp);			
			} 
		model.addAttribute("airTicketOrderList",airticketOrders);
		
		List<Office> officeList = officeService.findSyncOffice();
		model.addAttribute("companyList", officeList);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		
		List<Dict> supplytypelist=dictService.findByType("supplier_type");
		model.addAttribute("supplytypelist", supplytypelist);		
		
		model.addAttribute("curlist", currencylist);		
		List<CostRecord> budgetOutList = new ArrayList<CostRecord>();
		List<CostRecord> budgetInList = new ArrayList<CostRecord>();
		List<CostRecord> actualOutList = new ArrayList<CostRecord>();
		List<CostRecord> actualInList = new ArrayList<CostRecord>();
		List<CostRecord> otherCostList = new ArrayList<CostRecord>();

        budgetInList = costManageService.findCostRecordList(airTicketId, 0,0,Context.ORDER_TYPE_JP);
		budgetOutList = costManageService.findCostRecordList(airTicketId, 0,1,Context.ORDER_TYPE_JP);
		actualInList = costManageService.findCostRecordList(airTicketId, 1,0,Context.ORDER_TYPE_JP);
		actualOutList = costManageService.findCostRecordList(airTicketId, 1,1,Context.ORDER_TYPE_JP);
		otherCostList = costManageService.findCostRecordList(airTicketId, 2, 0, Context.ORDER_TYPE_JP);
		
		DecimalFormat df = new DecimalFormat("#####0.00");
		for (int i = 0; i < otherCostList.size(); i++) {
			Object obj1 = costManageService.getPayedMoney(otherCostList.get(i).getId());
			String s1 = df.format(Double.parseDouble(obj1==null?"0":obj1.toString()));
			otherCostList.get(i).setPayedMoney(s1);
			Object obj2 = costManageService.getConfirmMoney(otherCostList.get(i).getId());
			String s2 = df.format(Double.parseDouble(obj2==null?"0":obj2.toString()));
			otherCostList.get(i).setConfirmMoney(s2);
		}
		
		model.addAttribute("budgetInList", budgetInList);
		model.addAttribute("budgetOutList", budgetOutList);		
		model.addAttribute("actualInList", actualInList);
		model.addAttribute("actualOutList", actualOutList);
		model.addAttribute("otherCostList", otherCostList);
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());	
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(airTicketId,7);
		List<Map<String, Object>> budgetCost=costManageService.getCost(airTicketId,7,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(airTicketId,7,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		
		Object budgetrefund = costManageService.getRefundSum(airTicketId, 0, 7).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(airTicketId, 0, 7).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costManageService.getRefundSum(airTicketId, 1, 7).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(airTicketId, 1, 7).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);
		
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
			model.addAttribute("costAutoPass",costAutoPass);
		}else{
			model.addAttribute("costAutoPass","0");
		}
		
		return "modules/costreview/airTicketRead";
	}
	
	
	
    //保存机票录入
	@ResponseBody
	@RequestMapping(value="saveticket", method=RequestMethod.POST)
	public String saveticket(@RequestParam String name, @RequestParam BigDecimal price,@RequestParam String comment,
    @RequestParam Long activityId, @RequestParam Integer quantity,@RequestParam Integer currencyId,    
    @RequestParam Integer overseas,@RequestParam Integer supplyType,@RequestParam Integer supplyId,        
    @RequestParam String supplyName,@RequestParam Integer budgetType){			
		//CostRecordAirTicket costRecord= new CostRecordAirTicket();
		int orderType=7; //订单类型 2:散拼;6签证;7机票
		CostRecord costRecord= new CostRecord();
		costRecord.setName(name);		
		costRecord.setPrice(price);
		costRecord.setQuantity(quantity);
		costRecord.setSupplyId(supplyId);
		costRecord.setSupplyType(supplyType);
		costRecord.setOrderType(orderType);
		
		if(supplyType==1){
			Agentinfo agentinfo= new Agentinfo();
			agentinfo=agentinfoService.findOne((long)supplyId);
			costRecord.setSupplyName(agentinfo.getAgentName());
		}else {
		    costRecord.setSupplyName(supplyName);
		}
		costRecord.setActivityId(activityId);		
		costRecord.setBudgetType(budgetType);
		costRecord.setOverseas(overseas);
		
		costRecord.setCurrencyId(currencyId);
		
		if(StringUtils.isNotEmpty(comment)){
			costRecord.setComment(comment);
		}
		costManageService.saveCostRecord(costRecord);	
		return "[{\"result\":\"ok\"}]";
	}
	
	
	@RequestMapping(value="costRecordAirTicketSubmit")
	public String costRecordAirTicketSubmit(){
		return "redirect:airTicketPreRecordList";
	}
	
	
	@Autowired
	private VisaProductsService visaProductsService;
	

	
    @Autowired
    private VisaPublicBulletinService visaPublicBulletinService;
	
	/**
	 * 签证产品预定列表list
	 * @param visaProducts
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "visaList/{reviewLevel}")
	public String list(@ModelAttribute VisaProducts visaProducts,
			HttpServletRequest request, HttpServletResponse response,Model model,			
			@PathVariable(value="reviewLevel") Integer reviewLevel) {
		String review = request.getParameter("review");		
		String productName = request.getParameter("productName");//
		String userJobId = request.getParameter("userJobId");//
		String collarZoning = request.getParameter("collarZoning");//
		//String sysCountryId = request.getParameter("sysCountryId");//
		String visaProductIds = request.getParameter("visaProductIds");//
		String visaType = request.getParameter("visaType");//
		String orderBy = request.getParameter("orderBy");//	
		String createByName = request.getParameter("createByName"); // 审核发起人
		
		/**地接社ID*/
		//String supplierCompanyId = request.getParameter("supplierCompanyId");
		String sysCountryName = request.getParameter("sysCountryName");
		Country country = CountryUtils.getCountryId(sysCountryName);
		String sysCountryId = null;
		if(null != country){
			sysCountryId = "" + country.getId();
		}
		Integer supplierId = null ;
		if(StringUtils.isNotBlank(request.getParameter("supplierCompanyId"))){
			supplierId = Integer.parseInt(request.getParameter("supplierCompanyId"));
		}
		Integer agentId = null ;
		if(StringUtils.isNotBlank(request.getParameter("agentId"))){
			agentId = Integer.parseInt(request.getParameter("agentId"));
		}
		//visaProducts.setProductStatus(new Integer(Context.PRODUCT_ONLINE_STATUS));// 
 		Long companyId= UserUtils.getUser().getCompany().getId(); 		
 		long reviewCompanyId=(long)0; 	 		
 		List<UserJob> userJobs = costManageService.getReviewByFlowType(COSTREVIEW_FLOWTYPE,6);
 		List<Map<String, Object>> jobList= new ArrayList<Map<String, Object>>();
 		List<Long> reviewCompanyList = new ArrayList<Long>();
		if(userJobId == null ||userJobId.equals("")){
			if(userJobs.size()>0) {	
				  //首次进入页面，没有选择任何部门职务，取第1个部门职务
				  userJobId=userJobs.get(0).getId().toString();
				  Long deptId=(long)0;
				  if(userJobs.get(0).getDeptLevel()==1){
					  deptId=userJobs.get(0).getDeptId();
				  }else if(userJobs.get(0).getDeptLevel()==2){
					  deptId=userJobs.get(0).getParentDept();
				  } 
				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId,COSTREVIEW_FLOWTYPE, deptId);
				if( reviewCompanyList.size() > 0) {					
				  reviewCompanyId = reviewCompanyList.get(0);
				  List<Integer> reviewList;	
				  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,6);
			      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJobs.get(0).getJobId(),reviewCompanyId);
			      if (reviewList.size()>0) {
			    	reviewLevel=reviewList.get(0);				
			      }
				}
			 }
			 else{
				reviewCompanyId=(long)-1;
			 }
		}else{			
			for(UserJob userJob : userJobs){
				     if(userJob.getId() == Long.parseLong(userJobId)){
					  Long deptId=(long)0;
					  if(userJob.getDeptLevel()==1){
						  deptId=userJob.getDeptId();
					  }else if(userJob.getDeptLevel()==2){
						  deptId=userJob.getParentDept();
					  } 
					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, COSTREVIEW_FLOWTYPE, deptId);
					if( reviewCompanyList.size() > 0) {						
					  reviewCompanyId = reviewCompanyList.get(0);
					  List<Integer> reviewList;	
					  jobList= reviewCompanyService.findReviewJob(reviewCompanyId,6);
				      reviewList =reviewRoleLevelDao.findReviewJobLevel(userJob.getJobId(),reviewCompanyId);
				      if (reviewList.size()>0) {
				    	 reviewLevel=reviewList.get(0);				
				      }
					}
				}
			}
		}	
        model.addAttribute("jobList", jobList);//审核职位列表
        model.addAttribute("reviewCompanyId", reviewCompanyId);//审核职位列表
        model.addAttribute("userJobs", userJobs);//当前用户的职位
        model.addAttribute("userJobId", userJobId);	
		model.addAttribute("reviewLevel", reviewLevel);		 
		model.addAttribute("companyId", companyId);
		Page<VisaProductsCostView> page = this.visaProductsService
				.findVisaCostViewPage(
						new Page<VisaProductsCostView>(request, response),
						visaProducts, productName, collarZoning, sysCountryId,
						visaType, review,reviewLevel,companyId,reviewCompanyId,supplierId,agentId,COSTREVIEW_FLOWTYPE,orderBy,createByName);
        
		List<Country> countryList = CountryUtils.getCountrys();	
		List<Currency> currencylist = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		ReviewCompany reviewCompany = reviewCompanyDao.getById(reviewCompanyId);
		model.addAttribute("reviewCompany", reviewCompany);
		model.addAttribute("curlist", currencylist);		
		model.addAttribute("visaCountryList", countryList);
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));		
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));

		model.addAttribute("page", page);
		model.addAttribute("productName", productName);
		model.addAttribute("collarZoning", collarZoning);
		//model.addAttribute("sysCountryId", sysCountryId);
		model.addAttribute("sysCountryName", sysCountryName);
		model.addAttribute("visaTypeId", visaType);
		model.addAttribute("visaProductIds", visaProductIds);
		model.addAttribute("orderBy", orderBy);
		
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		//User user = UserUtils.getUser();
		//model.addAttribute("agentinfoList", agentinfoService.findAgentBySalerId(user.getId()));
		
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		VisaPublicBulletin  visaPublicBulletin = visaPublicBulletinService.findByVisaPublicBulletinForOne();
		model.addAttribute("visaPublicBulletin", visaPublicBulletin);
		model.addAttribute("review", review); 
		
		/**新增地接社列表*/
		model.addAttribute("supplierList",supplierInfoService.findSupplierInfoByCompanyId(companyId));
		model.addAttribute("agentInfo", agentinfoService.findAllAgentinfo(companyId));
		
		model.addAttribute("supplierCompanyId", supplierId);
		model.addAttribute("agentId", agentId);
		
		return "modules/costreview/visaList";
	}
	
	/**
	 * 获得当前步骤加载页面
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="visa/{visaProductId}/{reviewLevel}/{reviewCompanyId}", method=RequestMethod.GET)
	public String showCurrent(@PathVariable(value="visaProductId") Long visaProductId,
			@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="reviewCompanyId") Long reviewCompanyId,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
//		Long companyId= UserUtils.getUser().getCompany().getId();	
		 	 		
 		ReviewCompany reviewCompany= new ReviewCompany();
 		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
 		long userId = UserUtils.getUser().getId();
 		List<UserJob> userJobList = new ArrayList<UserJob>();
 		userJobList=userJobDao.getUserJobList(userId);	
 		List<Long> jobIds = new ArrayList<Long>();
 		for (UserJob userjob : userJobList) { 
 			  if(userjob.getDeptLevel()==1 && reviewCompany.getDeptId().equals(userjob.getDeptId()) && userjob.getOrderType()==6)
 				 jobIds.add(userjob.getJobId());			 
 			 else if(userjob.getDeptLevel()==2 && reviewCompany.getDeptId().equals(userjob.getParentDept()) && userjob.getOrderType()==6)
 			     jobIds.add(userjob.getJobId());			
 		}
 		
 		List<Integer> levelList = reviewRoleLevelDao.findReviewJobList(reviewCompanyId,jobIds); 		
 		model.addAttribute("levelList", levelList);	
		model.addAttribute("reviewCompanyId", reviewCompanyId);	
		
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		List<CostRecord> budgetOutList = new ArrayList<CostRecord>();
		List<CostRecord> budgetInList = new ArrayList<CostRecord>();
		List<CostRecord> actualOutList = new ArrayList<CostRecord>();
		List<CostRecord> actualInList = new ArrayList<CostRecord>();
		
		budgetInList = costManageService.findCostRecordList(visaProductId, 0,0,Context.ORDER_TYPE_QZ);
		budgetOutList = costManageService.findCostRecordList(visaProductId, 0,1,Context.ORDER_TYPE_QZ);
		
		actualInList = costManageService.findCostRecordList(visaProductId, 1,0,Context.ORDER_TYPE_QZ);
		actualOutList = costManageService.findCostRecordList(visaProductId, 1,1, Context.ORDER_TYPE_QZ);
		
		model.addAttribute("budgetInList", budgetInList);
		model.addAttribute("budgetOutList", budgetOutList);
		
		model.addAttribute("actualInList", actualInList);
		model.addAttribute("actualOutList", actualOutList);
	
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		
		model.addAttribute("curlist", currencylist);		
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		
		model.addAttribute("costLog",stockDao.findCostRecordLog(visaProductId, 6));
		
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaCountryList", countryList);		
		
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));
		
		List<Dict> supplytypelist=dictService.findByType("supplier_type");
		model.addAttribute("supplytypelist", supplytypelist);
		
		VisaProducts visaProduct = this.visaProductsService.findByVisaProductsId(visaProductId);
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		
		model.addAttribute("visaProduct", visaProduct);
		/*
		String costSerial= visaProduct.getCost();
		String incomeSerial= visaProduct.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId)); */
		
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();	
		orderList = visaOrderService.queryVisaOrdersByProductId(visaProductId.toString()); 
		model.addAttribute("orderList", orderList);	
		
		List<Dict> ordertype=dictService.findByType("order_type");
		model.addAttribute("ordertype", ordertype);	
		//价格币种
		model.addAttribute("currencyMark", CurrencyUtils.getCurrencyInfo(visaProduct.getCurrencyId().toString(), 0, "mark"));
				
		
		if (visaProduct.getCurrencyId() != null) {
			Currency currency = this.currencyService.findCurrency(new Long(
					visaProduct.getCurrencyId()));
			model.addAttribute("currency", currency);
		}
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(visaProductId,6);
		List<Map<String, Object>> budgetCost=costManageService.getCost(visaProductId,6,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(visaProductId,6,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		return "modules/costreview/visaDetail";	
	}
	
	@RequestMapping(value="visaRead/{visaProductId}/{reviewLevel}", method=RequestMethod.GET)
	public String readCurrent(@PathVariable(value="visaProductId") Long visaProductId,
			@PathVariable(value="reviewLevel") Integer reviewLevel,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		String flag = request.getParameter("flag");
		model.addAttribute("flag", flag);

		String sitemap = request.getParameter("sitemap");
		model.addAttribute("sitemap", sitemap);

		String menuid = request.getParameter("menuid");
		if("3".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 返佣付款 > 签证详情页");
			model.addAttribute("head", "签证返佣付款详情页");
		}else if("2".equals(menuid)) {
			model.addAttribute("title", "财务 > 结算管理 > 退款付款 > 签证详情页");
			model.addAttribute("head", "签证退款付款详情页");
		} else if("4".equals(menuid)) {
		   model.addAttribute("title", "财务 > 结算管理 > 借款付款 > 签证详情页");
		   model.addAttribute("head", "签证借款付款详情"); 
	    } else if("1".equals(menuid)) {
		  model.addAttribute("title", "财务 > 结算管理 > 成本付款 > 签证详情页");
		  model.addAttribute("head", "签证成本付款 详情"); 
		} else{
			 model.addAttribute("title","");
			 model.addAttribute("head", "签证成本详情页"); 
		}

		
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		List<CostRecord> budgetOutList = new ArrayList<CostRecord>();
		List<CostRecord> budgetInList = new ArrayList<CostRecord>();
		List<CostRecord> actualOutList = new ArrayList<CostRecord>();
		List<CostRecord> actualInList = new ArrayList<CostRecord>();
		List<CostRecord> otherCostList = new ArrayList<CostRecord>();
		
		budgetInList = costManageService.findCostRecordList(visaProductId, 0,0,Context.ORDER_TYPE_QZ);
		budgetOutList = costManageService.findCostRecordList(visaProductId, 0,1,Context.ORDER_TYPE_QZ);
		
		actualInList = costManageService.findCostRecordList(visaProductId, 1,0,Context.ORDER_TYPE_QZ);
		actualOutList = costManageService.findCostRecordList(visaProductId, 1,1, Context.ORDER_TYPE_QZ);
		
		otherCostList = costManageService.findCostRecordList(visaProductId, 2, 0, Context.ORDER_TYPE_QZ);
		
		DecimalFormat df = new DecimalFormat("#####0.00");
		for (int i = 0; i < otherCostList.size(); i++) {
			Object obj1 = costManageService.getPayedMoney(otherCostList.get(i).getId());
			String s1 = df.format(Double.parseDouble(obj1==null?"0":obj1.toString()));
			otherCostList.get(i).setPayedMoney(s1);
			Object obj2 = costManageService.getConfirmMoney(otherCostList.get(i).getId());
			String s2 = df.format(Double.parseDouble(obj2==null?"0":obj2.toString()));
			otherCostList.get(i).setConfirmMoney(s2);
		}
		
		model.addAttribute("budgetInList", budgetInList);
		model.addAttribute("budgetOutList", budgetOutList);
		
		model.addAttribute("actualInList", actualInList);
		model.addAttribute("actualOutList", actualOutList);
		
		model.addAttribute("otherCostList", otherCostList);
	
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		
		model.addAttribute("curlist", currencylist);		
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		
		
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaCountryList", countryList);
		model.addAttribute("costLog",stockDao.findCostRecordLog(visaProductId, 6));
		
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));	
		
		List<Dict> supplytypelist=dictService.findByType("supplier_type");
		model.addAttribute("supplytypelist", supplytypelist);
		
		VisaProducts visaProduct = this.visaProductsService.findByVisaProductsId(visaProductId);
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		
		model.addAttribute("visaProduct", visaProduct);
		/*
		String costSerial= visaProduct.getCost();
		String incomeSerial= visaProduct.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId)); */
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();	
		orderList = visaOrderService.queryVisaOrdersByProductId(visaProductId.toString()); 
		model.addAttribute("orderList", orderList);	
		
		List<Dict> ordertype=dictService.findByType("order_type");
		model.addAttribute("ordertype", ordertype);
		//价格币种
		model.addAttribute("currencyMark", CurrencyUtils.getCurrencyInfo(visaProduct.getCurrencyId().toString(), 0, "mark"));
		
		if (visaProduct.getCurrencyId() != null) {
			Currency currency = this.currencyService.findCurrency(new Long(
					visaProduct.getCurrencyId()));
			model.addAttribute("currency", currency);
		}
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(visaProductId,6);
		List<Map<String, Object>> budgetCost=costManageService.getCost(visaProductId,6,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(visaProductId, 6, 1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		
		Object budgetrefund = costManageService.getRefundSum(visaProductId, 0, 6).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(visaProductId, 0, 6).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costManageService.getRefundSum(visaProductId, 1, 6).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(visaProductId, 1, 6).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);
		
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
			model.addAttribute("costAutoPass",costAutoPass);
		}else{
			model.addAttribute("costAutoPass","0");
		}
		
		return "modules/costreview/visaRead";	
	}
	
	/**
	 * 团队管理
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @param orderType   订单类型
	 * @param reviewLevel 当前级别
	 * @return
	 */
	@RequestMapping(value="listGroup/{orderType}/{reviewLevel}")
	public String listGroup(@ModelAttribute TravelActivity travelActivity, HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable(value="orderType") Integer orderType, 
			@PathVariable(value="reviewLevel") Integer reviewLevel){

		Office company = UserUtils.getUser().getCompany();
		Long companyId = company.getId();

		String iscommission = request.getParameter("iscommission");//提成状态
		String orderS = request.getParameter("orderS");
		if(StringUtils.isBlank(orderS)){
			orderS = orderType.toString();
		}
		// 校验参数并封装团队管理实体类
		GroupManagerEntity entity = getGroupManagerEntity(request, iscommission);
		
		Page<Map<String,Object>> page = activityGroupCostViewService.findGroup(new Page<Map<String,Object>>(request, response),
				entity, Integer.valueOf(orderS), companyId);
	 	
		List<Map<String,Object>> dataList = page.getList();
		List<GroupManagerEntity> entityList = castListMap2Entity(dataList, Integer.valueOf(orderS));
		/*获取批发商下所有拥有计调角色人员的名单*/
		model.addAttribute("agentJd", agentinfoService.findAllUsers(companyId));
		//销售
		model.addAttribute("agentSaler", agentinfoService.findSalers(companyId));
	 	//部门
	 	model.addAttribute("departmentList", departmentService.findByOfficeId(companyId));
	 	model.addAttribute("page", page);
	 	model.addAttribute("entityList", entityList);
	 	model.addAttribute("groupCode", entity.getGroupCode());
	 	model.addAttribute("productName", entity.getProductName());
	 	model.addAttribute("operatorId", entity.getOperatorId());
	 	model.addAttribute("salerId", entity.getSalerId());
	 	model.addAttribute("orderType", orderType);
	 	model.addAttribute("orderS",orderS);
	 	model.addAttribute("companyId",companyId);
	 	model.addAttribute("isLMT", Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getCompanyUuid()));
	 	model.addAttribute("iscommission", iscommission);
	 	model.addAttribute("departmentId", entity.getDeptId());
		model.addAttribute("groupOpenDate", entity.getGroupOpenDate());
		model.addAttribute("groupCloseDate", entity.getGroupCloseDate());

	 	//团队类型
		model.addAttribute("orderTypes", DictUtils.getDict2List("order_type"));
		return "modules/costreview/newCostReviewList";
	}

	/**
	 * 团队管理 -- 应收、应付、实收、实付、利润总额、实际总利润、人数统计
	 * */
	@RequestMapping(value = "statistics/{orderType}")
	public String getStatistics(@PathVariable(value="orderType") Integer orderType, HttpServletRequest request, 
			HttpServletResponse response, Model model) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		//获取参数
		String iscommission = request.getParameter("iscommission");//提成状态
		String orderS = request.getParameter("orderS");//团队类型
		if (StringUtils.isBlank(orderS)) {
			orderS = orderType.toString();
		}
		// 校验参数并封装团队管理实体类
		GroupManagerEntity entity = getGroupManagerEntity(request, iscommission);
		//获取应收合计
		List<Map<String,Object>> totalMoneyList = activityGroupCostViewService.getMoneySum(Context.MONEY_TYPE_YSH, entity, Integer.valueOf(orderS), companyId);
		String totalMoneyStr = activityGroupCostViewService.getMoneySumStr(totalMoneyList);
		//应付合计
		List<Map<String,Object>> costTotalList = activityGroupCostViewService.getCostTotal(entity, Integer.valueOf(orderS), companyId);
		String costTotalStr = activityGroupCostViewService.getMoneySumStr(costTotalList);
		//利润合计 LMT:应收-应付 			其他：达帐-应付  
		String profitTotal = "";
		//判断是否是拉美图用户
		String uuid = UserUtils.getUser().getCompany().getUuid();
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(uuid)){
			profitTotal = activityGroupCostViewService.getProfitTotal(totalMoneyList, costTotalList);
		}else {
			profitTotal = activityGroupCostViewService.getProfitTotal(Context.MONEY_TYPE_DZ, entity, Integer.valueOf(orderS), companyId);
		}
		
		model.addAttribute("totalMoney", totalMoneyStr);
		model.addAttribute("costTotalStr", costTotalStr);
		model.addAttribute("profitTotal", profitTotal);

		//add by shijun.liu 2015.12.24
		//实收总额 --> 订单收款，切位收款，签证订单收款，其他收入收款的达帐金额总和
		Map<String, BigDecimal> realReceiveSumMoney = null;
		Map<String, BigDecimal> realPayedSumMoney = null;
		String realReceiveMoney = "";
		String realPayedMoney = "";
		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(uuid)) {
			List<Map<String,Object>> list = activityGroupCostViewService.getAllInfoMap(entity, Integer.valueOf(orderS), companyId);
			List<GroupManagerEntity> entityList = castListMap2Entity(list,Integer.parseInt(orderS));
			BigDecimal big = new BigDecimal("0.00");
			BigDecimal realPayed = new BigDecimal("0.00");
			Map<String,BigDecimal> real = new HashMap<String,BigDecimal>();
			Map<String,BigDecimal> realPay = new HashMap<String,BigDecimal>();
			for(GroupManagerEntity e : entityList){
				big = big.add(new BigDecimal(e.getRealMoney().replace(",","")));
				realPayed = realPayed.add(new BigDecimal(e.getRealPayMoney().replace(",","")));
			}

			real.put("amount", big);
			realPay.put("amount",realPayed);
			realReceiveMoney = MoneyAmountUtils.translateMultplyCurrencyToString(real, false);
			realPayedMoney = MoneyAmountUtils.translateMultplyCurrencyToString(realPay, false);
			model.addAttribute("realReceiveMoney",realReceiveMoney.replace("amount", Context.CURRENCY_MARK_RMB));
			model.addAttribute("realPayedMoney", realPayedMoney.replace("amount", Context.CURRENCY_MARK_RMB));
		}else{
			realReceiveSumMoney = receivePayService.getRealReceiveSumMoney(entity, Integer.parseInt(orderS));
			realReceiveMoney = MoneyAmountUtils.translateMultplyCurrencyToString(realReceiveSumMoney, false);
			model.addAttribute("realReceiveMoney", realReceiveMoney);
			//实付总额 --> 成本付款，反佣付款，退款付款（不包括退签证押金）的总额(不考虑是否确认付款的状态)
			realPayedSumMoney = receivePayService.getRealPayedSumMoney(entity, Integer.parseInt(orderS));
			realPayedMoney = MoneyAmountUtils.translateMultplyCurrencyToString(realPayedSumMoney, false);
			model.addAttribute("realPayedMoney", realPayedMoney);
		}

		//实际总利润 = 实收总额 - 实付总额
		String realProfit = "";
		String realReceive = realReceiveMoney.replaceAll(",", "").replaceAll("¥", "").replace("amount", "");
		String realPayed = realPayedMoney.replaceAll(",", "").replaceAll("¥", "").replace("amount", "");
		if(StringUtils.isBlank(realReceive)){
			realReceive = "0";
		}
		if(StringUtils.isBlank(realPayed)){
			realPayed = "0";
		}
		BigDecimal realProfitAmount = new BigDecimal(realReceive).subtract(new BigDecimal(realPayed));
		if(realProfitAmount.compareTo(new BigDecimal(0)) != 0){
			realProfit = "¥" + MoneyNumberFormat.getThousandsByRegex(realProfitAmount.toString(), 2);
		}
		model.addAttribute("realProfit", realProfit);
		//add end by shijun.liu 2015.12.24
		//总人数
		Integer totalPerson = activityGroupCostViewService.getTotalPerson(entity, orderS, companyId);
		model.addAttribute("totalPerson", totalPerson);
		return "modules/costreview/statistics";
	}
	
	/**
	 * 团队管理 -- 导出Excel
	 * */
	@RequestMapping(value="exportExcel/{orderType}")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(value="orderType") Integer orderType) throws IOException {
		Page<Map<String, Object>> pageForExcel = null;
		List<GroupManagerEntity> listForExcel = null;
		Long companyId = UserUtils.getUser().getCompany().getId();
		//获取参数
		String iscommission = request.getParameter("iscommission");//提成状态
		String orderS = request.getParameter("orderS");
		if(StringUtils.isBlank(orderS)){
			orderS = orderType.toString();
		}
		// 校验参数并封装团队管理实体类
		GroupManagerEntity entity = getGroupManagerEntity(request, iscommission);

		Page<Map<String, Object>> pageExcel = new Page<Map<String,Object>>(request, response);
		pageExcel.setMaxSize(Integer.MAX_VALUE);
		pageForExcel = activityGroupCostViewService.findGroup(pageExcel, entity, Integer.valueOf(orderS), companyId);
		listForExcel = castListMap2Entity(pageForExcel.getList(), Integer.valueOf(orderS));

		// 将原私有方法转移至excel工具类中  modify by wangyang 2016.11.29
		response = CommonExcelUtils.setFileDownloadHeader(request, response, "团队管理.xls");
		// 抽取私有方法，代码业务分离
		HSSFWorkbook workbook = getWorkbook(listForExcel);
		
		OutputStream os = response.getOutputStream();
		workbook.write(os);
		os.close();
	}
	
	/**
	 * 将查询参数封装为团队管理实体类<br>
	 * 校验次数较多，代码复用，避免重复代码
	 * */
	private GroupManagerEntity getGroupManagerEntity(HttpServletRequest request, String iscommission) {
		// 获取参数
		String operatorId = request.getParameter("operatorId");
		String salerId = request.getParameter("salerId");
		String departmentId = request.getParameter("departmentId");
		String groupOpenDate = request.getParameter("groupOpenDate");
		String groupCloseDate = request.getParameter("groupCloseDate");
		String groupCode = request.getParameter("groupCode");
		if (StringUtils.isNotBlank(groupCode)) {
			try { // URL转码
				groupCode = URLDecoder.decode(request.getParameter("groupCode"), "utf-8");
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}
		}
		String productName = request.getParameter("productName");
		if (StringUtils.isNotBlank(productName)) {
			try { // URL转码
				productName = URLDecoder.decode(request.getParameter("productName"), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		
		// 校验并封装实体
		GroupManagerEntity entity = new GroupManagerEntity();
		if(StringUtils.isNotBlank(iscommission)){
			if("N".equals(iscommission)){
				entity.setIscommission("0");
			}else if("Y".equals(iscommission)){
				entity.setIscommission("1");
			}
		}
		entity.setGroupCode(groupCode);
		entity.setProductName(productName);
		if (StringUtils.isNotBlank(operatorId)) {
			entity.setOperatorId(Integer.parseInt(operatorId));
		}
		if (StringUtils.isNotBlank(salerId)) {
			entity.setSalerId(Integer.parseInt(salerId));
		}
		if (StringUtils.isNotBlank(departmentId)) {
			entity.setDeptId(Integer.parseInt(departmentId));
		}
		entity.setGroupOpenDate(groupOpenDate);
		entity.setGroupCloseDate(groupCloseDate);
		return entity;
	}
	
	
	/**
	 * 团队管理 -- 导出excel -- 生成HSSFWorkbook对象
	 * */
	private HSSFWorkbook getWorkbook(List<GroupManagerEntity> list) {
		
		String title = "团队管理";
		String[] headers;
		Subject currentUser = SecurityUtils.getSubject();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(currentUser.isPermitted("listGroup:statustc")) {
			headers = new String[]{"序号", "团号", "团队类型", "产品名称", "出团日期", "人数", "部门", "计调", "销售",
					"应收金额", "应付金额", "利润", "毛利率", "预报单状态", "结算单状态", "提成状态"};
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)){
				headers = new String[]{"序号", "团号", "团队类型", "产品名称", "出团日期", "人数", "部门", "计调", "销售",
						"实收金额", "实付金额", "实际利润", "实际毛利率", "预报单状态", "结算单状态", "提成状态"};
			}
		}else{
			headers = new String[]{"序号", "团号", "团队类型", "产品名称", "出团日期", "人数", "部门", "计调", "销售", "应收金额",
					"应付金额", "利润", "毛利率", "预报单状态", "结算单状态"};
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)){
				headers = new String[]{"序号", "团号", "团队类型", "产品名称", "出团日期", "人数", "部门", "计调", "销售",
						"实收金额", "实付金额", "实际利润", "实际毛利率", "预报单状态", "结算单状态"};
			}
		}
		
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		
		for(int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(i + 1);//序号
			cell.setCellStyle(style);
			cell = row.createCell(1);
			if(list.get(i).getGroupCode() != null) {
				cell.setCellValue(list.get(i).getGroupCode().toString());//团号
			}
			cell.setCellStyle(style);
			cell = row.createCell(2);
			if(list.get(i).getOrderType() != null) {
				String orderTypeName = DictUtils.getDictLabel(list.get(i).getOrderType().toString(), "order_type", "");
				cell.setCellValue(orderTypeName);//团队类型
			}
			cell.setCellStyle(style);
			cell = row.createCell(3);
			if(list.get(i).getProductName() != null) {
				cell.setCellValue(list.get(i).getProductName().toString());//产品名称
			}
			cell.setCellStyle(style);
			cell = row.createCell(4);
			if(list.get(i).getGroupOpenDate() != null) {
				cell.setCellValue(list.get(i).getGroupOpenDate().toString());//日期
			}
			cell.setCellStyle(style);
			cell = row.createCell(5);
			if(list.get(i).getPersonNum() != null) {
				cell.setCellValue(list.get(i).getPersonNum().toString());//人数
			}
			cell.setCellStyle(style);
			cell = row.createCell(6);
			if(list.get(i).getDeptId() != null) {
				String deptName = CommonUtils.getDeptNameById(Long.parseLong(list.get(i).getDeptId().toString()));
				cell.setCellValue(deptName);//部门
			}
			cell.setCellStyle(style);
			cell = row.createCell(7);
			if(list.get(i).getOperator() != null) {
				cell.setCellValue(list.get(i).getOperator().toString());//计调
			}
			cell.setCellStyle(style);
			cell = row.createCell(8);
			if(list.get(i).getSaler() != null) {
				cell.setCellValue(list.get(i).getSaler().toString());//销售
			}
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)){
				cell.setCellStyle(style);
				cell = row.createCell(9);
				if(null != list.get(i).getRealMoney()) {
					cell.setCellValue(Context.CURRENCY_MARK_RMB + list.get(i).getRealMoney());//实收金额
				}else{
					cell.setCellValue("¥0.00");
				}
				cell.setCellStyle(style);
				cell = row.createCell(10);
				if(null != list.get(i).getRealPayMoney()) {
					cell.setCellValue(Context.CURRENCY_MARK_RMB + list.get(i).getRealPayMoney());//实付金额
				}else{
					cell.setCellValue("¥0.00");
				}
				cell.setCellStyle(style);
				cell = row.createCell(11);
				if(null != list.get(i).getRealProfit()) {
					cell.setCellValue(Context.CURRENCY_MARK_RMB + list.get(i).getRealProfit());//实际利润
				}else{
					cell.setCellValue("¥0.00");
				}
				cell.setCellStyle(style);
				cell = row.createCell(12);
				if(null != list.get(i).getRealProfitRate()) {
					cell.setCellValue(list.get(i).getRealProfitRate().toString());//实际毛利率
				}else{
					cell.setCellValue("0.000%");
				}
			}else{
				cell.setCellStyle(style);
				cell = row.createCell(9);
				if(list.get(i).getTotalMoney() != null) {
					cell.setCellValue(Context.CURRENCY_MARK_RMB + list.get(i).getTotalMoney());//应收金额
				}else{
					cell.setCellValue("¥0.00");
				}
				cell.setCellStyle(style);
				cell = row.createCell(10);
				if(list.get(i).getActualMoney() != null) {
					cell.setCellValue(Context.CURRENCY_MARK_RMB + list.get(i).getActualMoney());//应付金额
				}else{
					cell.setCellValue("¥0.00");
				}
				cell.setCellStyle(style);
				cell = row.createCell(11);
				if(list.get(i).getProfit() != null) {
					cell.setCellValue(Context.CURRENCY_MARK_RMB + list.get(i).getProfit());//利润
				}else{
					cell.setCellValue("¥0.00");
				}
				cell.setCellStyle(style);
				cell = row.createCell(12);
				if(list.get(i).getProfitRate() != null) {
					cell.setCellValue(list.get(i).getProfitRate().toString());//毛利率
				}else{
					cell.setCellValue("0.000%");
				}
			}

			cell.setCellStyle(style);
			cell = row.createCell(13);
			String forcastStatus = list.get(i).getForcastStatus();
			if(forcastStatus == null || "00".equals(forcastStatus.toString())) {
				cell.setCellValue("未锁定");//预报单状态
			}else{
				cell.setCellValue("锁定");
			}
			cell.setCellStyle(style);
			cell = row.createCell(14);
			String lockStatus = list.get(i).getLockStatus();
			if(lockStatus == null || "0".equals(lockStatus.toString())) {
				cell.setCellValue("未锁定");//结算单状态
			}else{
				cell.setCellValue("锁定");
			}
			cell.setCellStyle(style);
			if(currentUser.isPermitted("listGroup:statustc")) {
				cell = row.createCell(15);
				String iscommissionStr = list.get(i).getIscommission();
				if(iscommissionStr == null || (StringUtils.isNotBlank(iscommissionStr.toString()) && "0".equals(iscommissionStr.toString()))) {
					cell.setCellValue("未计算提成");//提成状态
				}else{
					cell.setCellValue("已计算提成");
				}
				cell.setCellStyle(style);
			}
			
		}
		return workbook;
	}
	
	
	/**
	 * Map类型转换为团队管理实体类
	 * */ 
	private List<GroupManagerEntity> castListMap2Entity(List<Map<String,Object>> dataList, Integer orderType){
		List<GroupManagerEntity> list = new ArrayList<GroupManagerEntity>();
		for (Map<String, Object> map:dataList) {
			GroupManagerEntity entity = new GroupManagerEntity();
			entity.setIscommission(map.get("iscommission") == null ? "" : map.get("iscommission").toString());
			entity.setGroupCode(map.get("groupCode") == null ? "" : String.valueOf(map.get("groupCode")));
			entity.setProductName(map.get("productName") == null ? "" : String.valueOf(map.get("productName")));
			entity.setOperator(map.get("operatorName") == null ? "" : String.valueOf(map.get("operatorName")));
			entity.setSaler(map.get("saler") == null ? "" : String.valueOf(map.get("saler")));
			entity.setGroupId(map.get("groupId") == null ? -1 : Long.valueOf(map.get("groupId").toString()));
			entity.setProductId(map.get("productId") == null ? -1 : Long.valueOf(map.get("productId").toString()));
			entity.setOrderType(String.valueOf(map.get("orderType")));
			entity.setGroupOpenDate(map.get("groupOpenDate") == null ? "" : map.get("groupOpenDate").toString());
			entity.setPersonNum(map.get("personNum") == null ? 0 : Integer.parseInt(map.get("personNum").toString()));
			entity.setDeptId(map.get("deptId") == null ? 0 : Integer.parseInt(map.get("deptId").toString()));
			entity.setLockStatus(map.get("lockStatus") == null ? "0" : map.get("lockStatus").toString());
			entity.setForcastStatus(map.get("forcastStatus") == null ? "00" : map.get("forcastStatus").toString());
			entity.setProductUuid(map.get("productUuid") == null ? "" : map.get("productUuid").toString());
			entity.setActivityUuid(map.get("activityUuid") == null ? "" : map.get("activityUuid").toString());

			//yudong.xu 2016.4.6
			//填充数据
			Integer orderTypeOld = orderType;
			if (orderType == 0){
				orderType = Integer.parseInt(map.get("orderType").toString());
			}
			boolean isLMT = Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getCompanyUuid());
			if (isLMT){
				setParamValueForLMT(orderType, entity, map);
			}else {
				setParamValue1(orderType, entity, map);
			}
			orderType = orderTypeOld;
			list.add(entity);
		}
		return list ;
	 }

	 /**
	  * 说明：根据订单类型的不同，来设置不同的activityId。机票，签证是产品的id；酒店，海岛是uuid；散拼等都是团期id。<br>
	  * 调用receivePayService中的方法，都会根据orderType的不同，来执行不同的sql，进行区别对待。<br>
	  * 这里所做的就是根据orderType的不同，来放入不同的查询条件。
	  * */
	 private void setParamValue1(Integer orderType, GroupManagerEntity entity, Map<String,Object> map){
		 String activityId = null;
		 if (orderType == Context.ORDER_TYPE_JP || orderType == Context.ORDER_TYPE_QZ){
			 activityId = entity.getProductId().toString();
		 }else if(orderType == Context.ORDER_TYPE_HOTEL || orderType == Context.ORDER_TYPE_ISLAND){
			 //酒店hotel和海岛游island
			 entity.setActivityUuid(map.get("activityUuid").toString());
			 entity.setProductUuid(map.get("productUuid").toString());
			 activityId = map.get("activityUuid").toString();
		 }else {//散拼等
			 activityId = entity.getGroupId().toString();
		 }

		 //设置应收金额(订单总额加上其他通过审批的其他收入)
		 BigDecimal totalMoney = receivePayService.getReceiveSumMoneyCNY(orderType,activityId);
		 totalMoney = totalMoney.add(receivePayService.getOtherIncomeSumMoneyCNY(orderType,activityId,false));
		 entity.setTotalMoney(MoneyNumberFormat.getRoundMoney(Double.valueOf(totalMoney.toString()), MoneyNumberFormat.POINT_TWO));
		 //设置应付金额,已通过审批，但是没有确认付款(成本，返佣，退款)
		 BigDecimal willPay = receivePayService.getCostSumMoneyCNY(orderType,activityId,false);
		 willPay = willPay.add(receivePayService.getRefundSumMoneyCNY(orderType,activityId,false));
		 willPay = willPay.add(receivePayService.getRebateSumMoneyCNY(orderType,activityId,false));
		 entity.setActualMoney(MoneyNumberFormat.getRoundMoney(Double.valueOf(willPay.toString()), MoneyNumberFormat.POINT_TWO));
		 //设置预计利润
		 BigDecimal proProfit = totalMoney.subtract(willPay);
		 entity.setProfit(MoneyNumberFormat.getRoundMoney(Double.valueOf(proProfit.toString()), MoneyNumberFormat.POINT_TWO));
		 //设置预计毛利率
		 if(Double.valueOf(entity.getTotalMoney().replaceAll(",", "")) > 0){
			 BigDecimal profitRate = proProfit.divide(new BigDecimal(entity.getTotalMoney().replaceAll(",", "")),
					 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			 entity.setProfitRate(MoneyNumberFormat.getFormatRate(Double.valueOf(profitRate.toString()), MoneyNumberFormat.POINT_THREE)+"%");
		 }else{
			 entity.setProfitRate("0.000%");
		 }
		 //设置实收金额
		 BigDecimal realMoney = receivePayService.getRealReceiveSumMoneyCNY(orderType,activityId);
		 realMoney = realMoney.add(receivePayService.getOtherIncomeSumMoneyCNY(orderType,activityId,true));
		 entity.setRealMoney(MoneyNumberFormat.getRoundMoney(Double.valueOf(realMoney.toString()), MoneyNumberFormat.POINT_TWO));
		 //设置实付金额,为成本，退款，返佣之和,已确认付款
		 BigDecimal realPay = receivePayService.getCostSumMoneyCNY(orderType,activityId,true);
		 realPay = realPay.add(receivePayService.getRefundSumMoneyCNY(orderType,activityId,true));
		 realPay = realPay.add(receivePayService.getRebateSumMoneyCNY(orderType,activityId,true));
		 entity.setRealPayMoney(MoneyNumberFormat.getRoundMoney(Double.valueOf(realPay.toString()), MoneyNumberFormat.POINT_TWO));
		 //设置实际利润
		 BigDecimal realProfit = realMoney.subtract(realPay);//subtract减去实际成本
		 entity.setRealProfit(MoneyNumberFormat.getRoundMoney(Double.valueOf(realProfit.toString()), MoneyNumberFormat.POINT_TWO));
		 //实际毛利率
		 if(Double.valueOf(entity.getRealMoney().replaceAll(",", "")) > 0) {
			 BigDecimal realProfitRate = realProfit.divide(new BigDecimal(entity.getRealMoney().replaceAll(",", "")),
					 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			 entity.setRealProfitRate(MoneyNumberFormat.getFormatRate(Double.valueOf(realProfitRate.toString()), MoneyNumberFormat.POINT_THREE) + "%");
		 }else {
			 entity.setRealProfitRate("0.000%");
		 }
	 }

	/**
	 * 针对拉美图进行实收金额，实付金额，利润，毛利率等进行数据填充。
	 * @author yudong.xu --2016/4/14--18:22
	 */
	 private void setParamValueForLMT(Integer orderType, GroupManagerEntity entity, Map<String,Object> map){
		String activityId = null;
		if (orderType == Context.ORDER_TYPE_JP || orderType == Context.ORDER_TYPE_QZ){
			activityId = entity.getProductId().toString();
		}else if(orderType == Context.ORDER_TYPE_HOTEL || orderType == Context.ORDER_TYPE_ISLAND) {
			//酒店hotel 和  海岛island
			entity.setActivityUuid(map.get("activityUuid").toString());
			entity.setProductUuid(map.get("productUuid").toString());
			activityId = map.get("activityUuid").toString();
		}else {//散拼等
			activityId = entity.getGroupId().toString();
		}

		//如果是锁定状态就从结算单表中获取数据。如果没有数据，表明还没有存入，则按原有规则获取。yudong.xu 2016.5.17
		if ("1".equals(entity.getLockStatus())){
			Settle settle = settleService.getSimpleSettleObj(orderType,activityId);
			if (null != settle){
				entity.setRealMoney(settle.getRealMoneySum());
				entity.setRealPayMoney(settle.getOutMoneySum());
				entity.setRealProfit(settle.getProfitSum());
				entity.setRealProfitRate(settle.getProfitRate());
				return;
			}
		}

		//实收金额
		BigDecimal realMoney = receivePayService.getRealReceiveMoneyForLMT(orderType,activityId);
		realMoney = realMoney.add(receivePayService.getOtherIncomeForLMT(orderType,activityId));
		entity.setRealMoney(MoneyNumberFormat.getThousandsByRegex(realMoney.toString(), 2));

		//实付金额
		BigDecimal realPay = receivePayService.getRealPayForLMT(orderType,activityId);//境内境外实付。
		realPay = realPay.add(receivePayService.getRealRebatesForLMT(orderType,activityId));//返佣
		realPay = realPay.add(receivePayService.getRefundForLMT(orderType,activityId));//加退款，对应需求256，支出公式改变
		entity.setRealPayMoney(MoneyNumberFormat.getThousandsByRegex(realPay.toString(), 2));

		//毛利= 实收金额-实付金额
		BigDecimal profitMoeny = realMoney.subtract(realPay);
		entity.setRealProfit(MoneyNumberFormat.getThousandsByRegex(profitMoeny.toString(), 2));

		//毛利率
		BigDecimal profitRate = new BigDecimal("0");
		if(Double.valueOf(realMoney.toString()) != 0){
			BigDecimal tempValue = new BigDecimal(MoneyNumberFormat.getRoundMoney(Double.valueOf(realMoney.toString()),
					MoneyNumberFormat.POINT_TWO));
			profitRate = profitMoeny.divide(tempValue, 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//毛利率
		}
		String rate  = profitRate.toString();
		if(rate.toString().equals("0")){
			rate = "";
		}
		if(rate.startsWith(".")){
			rate += "0";
		}
		if(!"".equals(rate)){
			rate = MoneyNumberFormat.getFormatRate(Double.valueOf(rate.toString()),MoneyNumberFormat.POINT_THREE);
			rate = rate + "%";
		}
		entity.setRealProfitRate(rate);
	 }
	 
	 
	 /**
	  * 团队管理 -- 操作 -- 展开
	  * @param request
	  * @param response
	  * @param model
	  * @return
	  */
	@ResponseBody
	@RequestMapping(value="listGroupDetail")
	public List<GroupManagerEntity> listGroupDetail(HttpServletRequest request, 
			HttpServletResponse response, Model model){
		Long companyId = UserUtils.getUser().getCompany().getId();
		String groupCodeValue = request.getParameter("groupCode");
		String orderType = request.getParameter("orderType");
		String activityId = request.getParameter("activityId");
		 
		GroupManagerEntity entity = new GroupManagerEntity();
		entity.setGroupCode(groupCodeValue);
		// 酒店或海岛游
		if (Context.ORDER_TYPE_HOTEL.toString().equals(orderType) 
				|| Context.ORDER_TYPE_ISLAND.toString().equals(orderType)) {
			entity.setActivityUuid(activityId);
		} else {
			entity.setProductId(Long.valueOf(activityId));
		}
		
		Page<Map<String,Object>> newPage = new Page<Map<String,Object>>(request, response);
		newPage.setPageSize(10000); // 设置页面大小 最大为500（10000无实际意义，仅为大于500的数字）
	 	Page<Map<String,Object>> page = activityGroupCostViewService.findGroupDetail(newPage, 
	 			entity, Integer.valueOf(orderType), Long.valueOf(companyId));
	 	List<GroupManagerEntity> entityListDetail = castListMap2EntityDetail(page.getList());
	 	return entityListDetail;
	 }
	 
	 private List<GroupManagerEntity> castListMap2EntityDetail(List<Map<String,Object>> dataList){
		 List<GroupManagerEntity> list = new ArrayList<GroupManagerEntity>();
		 for (Map<String, Object> map : dataList) {
			 GroupManagerEntity entity = new GroupManagerEntity();
			 entity.setGroupCode(map.get("groupCode") == null?"":String.valueOf(map.get("groupCode")));
			 entity.setSupplyName(map.get("supplyName") == null?"":String.valueOf(map.get("supplyName")));
			 entity.setCostName(map.get("NAME") == null?"":String.valueOf(map.get("NAME")));
			 entity.setQuantity(map.get("quantity") == null?0:Integer.parseInt(map.get("quantity").toString()));
			 entity.setCurrencyName(map.get("currencyName")==null?"":map.get("currencyName").toString());
			 entity.setComment(map.get("comment")==null?"":map.get("comment").toString());
			 String currencyMark = map.get("currencyMark")==null?"":map.get("currencyMark").toString();
			 String money = map.get("money")==null?"":map.get("money").toString();
			 entity.setDetailPayedMoney(currencyMark + money);
			 list.add(entity);
		}
		return list;
	 }
	 
}
