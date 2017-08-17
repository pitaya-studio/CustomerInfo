package com.trekiz.admin.modules.cost.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.trekiz.admin.common.mail.SendMailUtil;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
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
import com.trekiz.admin.modules.airticket.service.IFlightInfoService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.AbstractSpecificCost;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.entity.CostRecordHotel;
import com.trekiz.admin.modules.cost.entity.CostRecordIsland;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.cost.repository.CostRecordLogDao;
import com.trekiz.admin.modules.cost.service.CostIslandService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupRoom;
import com.trekiz.admin.modules.hotel.entity.Hotel;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupRoomService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupRoom;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupRoomService;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.ReviewCommonUtil;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.ReviewCompany;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewRoleLevelDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.stock.service.AirticketStockService;
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
import com.trekiz.admin.modules.sys.repository.UserDao;
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
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.entity.VisaProductsCostView;
import com.trekiz.admin.modules.visa.entity.VisaPublicBulletin;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaPublicBulletinService;

//财务审核下的付款审核
@Controller
@RequestMapping(value = "${adminPath}/payment/review")
public class PaymentReviewController extends BaseController {
		
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
	private ActivityIslandGroupService activityIslandGroupService;
	
	@Autowired	
	private ActivityHotelGroupService activityHotelGroupService;
	
	@Autowired
	private ActivityIslandService activityIslandService;
	
	@Autowired	
	private ActivityHotelService activityHotelService;
	@Autowired
	private IslandOrderService islandOrderService;	
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ReviewCompanyService reviewCompanyService;
	
	@Autowired	
	private ActivityGroupSyncService activityGroupService;
	
	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;

	@Autowired
	private ReviewService reviewService;
	@Autowired	
	private HotelService hotelService;
	@Autowired	
	private HotelStarService hotelStarService;
	@Autowired	
	private IslandService islandService;
	@Autowired
	private CostIslandService costIslandService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private CostRecordIslandDao costRecordIslandDao;
	@Autowired
	private CostRecordHotelDao costRecordHotelDao;

	@Autowired
	private SupplierInfoService supplierInfoService;
	
	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;
	
	@Autowired
	private SysGeographyService sysGeographyService;

	@Autowired
	private DictService dictService;
	@Autowired
	private ActivityHotelGroupRoomService activityHotelGroupRoomService;	
	@Autowired
	private ActivityIslandGroupRoomService activityIslandGroupRoomService;	
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
	private CostRecordLogDao costRecordLogDao;
	@Autowired
	private ReviewRoleLevelDao reviewRoleLevelDao;
	@Autowired
	private IReviewDao reviewSqlDao;	

	@ModelAttribute
	public TravelActivity get(@RequestParam(required=false) Long id) {
		if(id!=null){
			return travelActivityService.findById(id);
		}else
			return new TravelActivity();
	}
	//付款审核 ID=18
	private static int PAYREVIEW_FLOWTYPE=18;	
	/**
	 * 财务需要录入成本的列表页
	 * @return
	 * typeId:产品类型:散拼，机票..
	 * reviewLevel: 审核层级 
	 * **/    
   @RequestMapping(value="list/{typeId}/{reviewLevel}")
   public String list(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, 
		   HttpServletResponse response, Model model,@PathVariable(value="typeId") Integer typeId,
		  @PathVariable(value="reviewLevel") Integer reviewLevel){
	   String groupCode=request.getParameter("groupCode");
	   String review = request.getParameter("review");
	   String userJobId = request.getParameter("userJobId");
	   String createByName = request.getParameter("createByName"); // 审核发起人
	   String supplierIdStr = request.getParameter("supplierId");
	   Long supplierId = null;
	   if(StringUtils.isNotBlank(supplierIdStr)){
		   try {
			   supplierId = Long.parseLong(supplierIdStr);
		   }catch (NumberFormatException e) {
			   LOG.error("数据类型转换错误", e);
			   return "地接社传入值为:" + supplierIdStr;
		   }
	   }	
	   User user = UserUtils.getUser();
	   Long companyId = user.getCompany().getId();
	   
	   //按部门展示
	   DepartmentCommon common = departmentService.setDepartmentPara("activity", model);
	   long reviewCompanyId = 0L;
	   List<UserJob> userJobs = costManageService.getReviewByFlowType(PAYREVIEW_FLOWTYPE,typeId);
	   List<Map<String, Object>> jobList = new ArrayList<Map<String, Object>>();
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
			   reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, PAYREVIEW_FLOWTYPE, deptId);
			   if(reviewCompanyList.size() > 0) {					
				   reviewCompanyId = reviewCompanyList.get(0);
				   List<Integer> reviewList = null;	
				   jobList= reviewCompanyService.findReviewJob(reviewCompanyId,typeId);
				   reviewList =reviewRoleLevelDao.findReviewJobLevel(userJobs.get(0).getJobId(),reviewCompanyId);
				   if (reviewList.size()>0) {
					   reviewLevel=reviewList.get(0);				
				   }
			   }
		   }else{
			   reviewCompanyId = -1L;
		   }
	   }else{			
		   for(UserJob userJob : userJobs){
			   if(userJob.getId() == Long.parseLong(userJobId)){
				   Long deptId = null;
				   if(userJob.getDeptLevel()==1){
					   deptId=userJob.getDeptId();
				   }else if(userJob.getDeptLevel()==2){
					   deptId=userJob.getParentDept();
				   } 
				   reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, PAYREVIEW_FLOWTYPE, deptId);
				   if(reviewCompanyList.size() > 0) {						
					   reviewCompanyId = reviewCompanyList.get(0);
					   List<Integer> reviewList = null;	
					   jobList= reviewCompanyService.findReviewJob(reviewCompanyId,typeId);
					   reviewList =reviewRoleLevelDao.findReviewJobLevel(userJob.getJobId(),reviewCompanyId);
					   if(reviewList.size()>0){
						   reviewLevel=reviewList.get(0);				
					   }
				   }
			   }
		   }
	   }	

	   Page<ActivityGroupCostView> page = activityGroupCostViewService.findGroupCostReview(
			   new Page<ActivityGroupCostView>(request, response), travelActivity,  
			groupCode, supplierId, null, typeId, review, reviewLevel, companyId,reviewCompanyId,
			PAYREVIEW_FLOWTYPE, common, createByName);
	   ReviewCompany reviewCompany = reviewCompanyDao.getById(reviewCompanyId);
	   model.addAttribute("jobList", jobList);//审核职位列表
	   model.addAttribute("reviewLevel", reviewLevel);
	   model.addAttribute("companyId", companyId);
	   model.addAttribute("reviewCompany", reviewCompany);
	   model.addAttribute("page", page);
	   model.addAttribute("userJobs", userJobs);//当前用户的职位
	   model.addAttribute("userJobId", userJobId);
	   model.addAttribute("reviewCompanyId", reviewCompanyId);
	   model.addAttribute("travelActivity", travelActivity);
	   model.addAttribute("typeId", typeId);
	   model.addAttribute("review", review);
	   model.addAttribute("supplierList",supplierInfoService.findSupplierInfoByCompanyId(companyId));
	   model.addAttribute("groupCode", groupCode);
	   model.addAttribute("createByName", createByName);
	   return "modules/paymentreview/costReviewList";
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
	   String hotel = request.getParameter("hotel");
	   String island = request.getParameter("island");
	   String currencyId = request.getParameter("currencyId");
	   String startCurrency = request.getParameter("startCurrency");
	   String endCurrency = request.getParameter("endCurrency");
	   String activityName = request.getParameter("activityName");
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
		List<UserJob> userJobs = costManageService.getReviewByFlowType(PAYREVIEW_FLOWTYPE,typeId);
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
				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, PAYREVIEW_FLOWTYPE, deptId);
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
					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, PAYREVIEW_FLOWTYPE, deptId);
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
		
		ReviewCompany reviewCompany = reviewCompanyDao.getById(reviewCompanyId);
		model.addAttribute("reviewCompany", reviewCompany);
        model.addAttribute("jobList", jobList);//审核职位列表
		model.addAttribute("reviewLevel", reviewLevel);
		model.addAttribute("companyId", companyId);		 
		Page<IslandGroupCostView> page = activityGroupCostViewService.findIslandPayReview(new Page<IslandGroupCostView>(request, response), activityIslandGroup,  
				activityName,hotel, island, currencyId, startCurrency,endCurrency,supplierId,agentId,review,reviewLevel, companyId,reviewCompanyId,PAYREVIEW_FLOWTYPE,common,createByName);	
		model.addAttribute("page", page);
		
		List<Island> islandList= islandService.findListByCompanyId(companyId.intValue());
		model.addAttribute("islandList", islandList);
		Hotel myhotel=new Hotel();
	    myhotel.setWholesalerId(companyId.intValue());
		List<Hotel> hotelList= hotelService.find(myhotel);
		model.addAttribute("hotelList", hotelList);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);	
		model.addAttribute("currencyList", currencyList);
		
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
		return "modules/paymentreview/islandList";
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
	   String hotel = request.getParameter("hotel");
	   String island = request.getParameter("island");
	   String currencyId = request.getParameter("currencyId");
	   String startCurrency = request.getParameter("startCurrency");
	   String endCurrency = request.getParameter("endCurrency");
	   String activityName = request.getParameter("activityName");
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
		List<UserJob> userJobs = costManageService.getReviewByFlowType(PAYREVIEW_FLOWTYPE,typeId);
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
				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, PAYREVIEW_FLOWTYPE, deptId);
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
					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, PAYREVIEW_FLOWTYPE, deptId);
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

		ReviewCompany reviewCompany = reviewCompanyDao.getById(reviewCompanyId);
		model.addAttribute("reviewCompany", reviewCompany);
        model.addAttribute("jobList", jobList);//审核职位列表
		model.addAttribute("reviewLevel", reviewLevel);
		model.addAttribute("companyId", companyId);
		Page<HotelGroupCostView> page = activityGroupCostViewService.findHotelPayReview(new Page<HotelGroupCostView>(request, response), activityHotelGroup,  
				activityName,hotel, island, currencyId, startCurrency,endCurrency,supplierId,agentId,review,reviewLevel, companyId,reviewCompanyId,PAYREVIEW_FLOWTYPE,common,createByName);	
		model.addAttribute("page", page);
		
		List<Island> islandList= islandService.findListByCompanyId(companyId.intValue());
		model.addAttribute("islandList", islandList);
		Hotel myhotel=new Hotel();
	    myhotel.setWholesalerId(companyId.intValue());
		List<Hotel> hotelList= hotelService.find(myhotel);
		model.addAttribute("hotelList", hotelList);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);	
		model.addAttribute("currencyList", currencyList);
		
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
		return "modules/paymentreview/hotelList";
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
		return "modules/paymentreview/costReviewList";
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
	
	/**
	 * 付款审核 批量驳回或者单个驳回功能
	 * 数据格式如下：costRecordId,reviewLevel,orderType
	 *				&&costRecordId,reviewLevel,orderType
	 * 例如：203,3,4&&203,3,2&&203,3,4
	 * @param items
	 * @param comment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author shijun.liu
	 */
	@ResponseBody
	@RequestMapping(value="batchDeny", method=RequestMethod.POST)
	public String batchDeny(@RequestParam("items") String items,@RequestParam("comment") String comment,
			HttpServletRequest request, HttpServletResponse response, Model model){
		String[] arrays = items.split("&&");
		StringBuffer result = new StringBuffer();
		String json = null;
		for (String item:arrays) {
			String[] elements = item.split(",");
			Long costRecordId = -1L;
			Integer reviewLevel = -1;
			Integer orderType = -1;
			if(StringUtils.isNotBlank(elements[0])){
				costRecordId = Long.valueOf(elements[0]);
			}
			if(StringUtils.isNotBlank(elements[1])){
				reviewLevel = Integer.parseInt(elements[1]);
			}
			if(StringUtils.isNotBlank(elements[2])){
				orderType = Integer.parseInt(elements[2]);
			}
			CostRecord costRecord = costRecordDao.findOne(costRecordId);
			if(null == costRecord){
				result.append(costRecordId).append("值不正确，");
				continue;
			}
			costRecord.setPayReview(Context.REVIEW_COST_FAIL);
			costRecord.setPayUpdateBy(Integer.parseInt(UserUtils.getUser().getId()+""));
			costRecord.setPayUpdateDate(new Date());
			if(StringUtils.isNotBlank(comment)){
				costRecord.setPayReviewComment(comment);
			}
			costRecordDao.save(costRecord);
			CostRecordLog costRecordLog =new CostRecordLog();
			costRecordLog.setRid(costRecord.getActivityId());
			costRecordLog.setCostId(costRecordId);
			costRecordLog.setCostName(costRecord.getName()+"_付款审核");
			costRecordLog.setNowLevel(reviewLevel);
			costRecordLog.setResult(0);//审核失败
			costRecordLog.setRemark("");
			costRecordLog.setOrderType(orderType);
			costRecordLog.setCreateDate(new Date());
			costRecordLog.setCreateBy(UserUtils.getUser().getId());
			costRecordLogDao.save(costRecordLog);
		}
		if(result.toString().length() == 0){
			json = "{\"flag\":true,\"msg\":\""+result.toString()+"\"}";
		}else{
			json = "{\"flag\":false,\"msg\":\""+result.toString()+"\"}";
		}
		return json;
	}
	
	//驳回单条成本
		@ResponseBody
		@RequestMapping(value="denyIsland", method=RequestMethod.POST)
		public String denyIsland(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
				@RequestParam (required=true) Integer orderType){

			CostRecordIsland costRecord=new CostRecordIsland();
			costRecord=costRecordIslandDao.findOne(id);
			costRecord.setPayReview(Context.REVIEW_COST_FAIL);
			costRecordIslandDao.save(costRecord);		
			String result=ReviewCommonUtil.getNextPayIslandReview(id);
			CostRecordLog costRecordLog =new CostRecordLog();
			costRecordLog.setActivityUuid(costRecord.getActivityUuid());
			costRecordLog.setCostId(id);
			costRecordLog.setCostName(costRecord.getName()+"_付款审核");
			costRecordLog.setNowLevel(reviewLevel);
			costRecordLog.setResult(0);//审核失败		
			costRecordLog.setRemark("");
			costRecordLog.setOrderType(orderType); 	
			costRecordLog.setCreateDate(new Date());
			costRecordLog.setCreateBy(UserUtils.getUser().getId());		
			costRecordLogDao.save(costRecordLog);				
			return "[{\"result\":\""+result+"\"}]";
		}
		
		//驳回单条成本
	@ResponseBody
	@RequestMapping(value="denyHotel", method=RequestMethod.POST)
	public String denyHotel(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
						@RequestParam (required=true) Integer orderType){
	
					CostRecordHotel costRecord=new CostRecordHotel();
					costRecord=costRecordHotelDao.findOne(id);
					costRecord.setPayReview(Context.REVIEW_COST_FAIL);
					costRecordHotelDao.save(costRecord);		
					String result=ReviewCommonUtil.getNextPayHotelReview(id);
					CostRecordLog costRecordLog =new CostRecordLog();
					costRecordLog.setActivityUuid(costRecord.getActivityUuid());
					costRecordLog.setCostId(id);
					costRecordLog.setCostName(costRecord.getName()+"_付款审核");
					costRecordLog.setNowLevel(reviewLevel);
					costRecordLog.setResult(0);//审核失败		
					costRecordLog.setRemark("");
					costRecordLog.setOrderType(orderType); 	
					costRecordLog.setCreateDate(new Date());
					costRecordLog.setCreateBy(UserUtils.getUser().getId());		
					costRecordLogDao.save(costRecordLog);				
					return "[{\"result\":\""+result+"\"}]";
	}	
	
	/**
	 * 批量审核或者单个审核
	 * 数据格式如下：costRecordId,reviewLevel,reviewCompanyId,orderType
	 *				&&costRecordId,reviewLevel,reviewCompanyId,orderType
	 * 例如：203,3,124,4&&203,3,124,2&&203,3,124,4
	 * @param items		参数列表
	 * @param comment	审核通过备注信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author shijun.liu
	 */
	@ResponseBody
	@RequestMapping(value="batchPass", method=RequestMethod.POST)
	public String batchPass(@RequestParam("items") String items, @RequestParam("comment") String comment, 
			HttpServletRequest request, HttpServletResponse response, Model model){
		String[] arrays = items.split("&&");
		StringBuffer result = new StringBuffer();
		String json = null;
		for (String item : arrays) {
			Long costRecordId = -1L;
			Integer reviewLevel = -1;
			Long reviewCompanyId = -1L;
			Integer orderType = -1;
			String[] elements = item.split(",");
			if(StringUtils.isNotBlank(elements[0])){
				costRecordId = Long.valueOf(elements[0]);
			}
			if(StringUtils.isNotBlank(elements[1])){
				reviewLevel = Integer.parseInt(elements[1]);
			}
			if(StringUtils.isNotBlank(elements[2])){
				reviewCompanyId = Long.valueOf(elements[2]);
			}
			if(StringUtils.isNotBlank(elements[3])){
				orderType = Integer.parseInt(elements[3]);
			}
			ReviewCompany reviewCompany = reviewCompanyDao.findOne(reviewCompanyId);
			CostRecord costRecord = costRecordDao.findOne(costRecordId);
			if(null == reviewCompany || null == costRecord){
				result.append(reviewCompanyId).append("值不正确，");
				continue;
			}
			Integer nowLevel = costRecord.getPayNowLevel();
			int topLevel = reviewCompany.getTopLevel();
			if (null == nowLevel) {
				nowLevel = 1;
			}
			//检查是否配置某层结束审核标志
			List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
			if (nowLevel<topLevel && isEndReviewList.size()==0) {
				costRecord.setPayNowLevel(reviewLevel+1);
			}else if (nowLevel<topLevel && isEndReviewList.size()>0){
				costRecord.setPayReview(Context.REVIEW_COST_PASS);
				costRecord.setPayNowLevel(topLevel+1);
			}else if (nowLevel == topLevel) {//审核到最后
				costRecord.setPayReview(Context.REVIEW_COST_PASS);
				costRecord.setPayNowLevel(reviewLevel+1);
			}
			costRecord.setPayUpdateBy(Integer.parseInt(UserUtils.getUser().getId()+""));
			costRecord.setPayUpdateDate(new Date());
			if(StringUtils.isNotBlank(comment)){
				costRecord.setPayReviewComment(comment);
			}
			costRecordDao.save(costRecord);
			CostRecordLog costRecordLog = new CostRecordLog();
			costRecordLog.setRid(costRecord.getActivityId());
			costRecordLog.setCostId(costRecordId);
			costRecordLog.setCostName(costRecord.getName()+"_付款审核");
			costRecordLog.setNowLevel(nowLevel);
			costRecordLog.setResult(1);//审核通过
			costRecordLog.setOrderType(orderType);
			costRecordLog.setRemark("");
			costRecordLog.setCreateDate(new Date());
			costRecordLog.setCreateBy(UserUtils.getUser().getId());
			costRecordLog.setLogType(1);
			costRecordLogDao.save(costRecordLog);
		}
		if(result.toString().length() == 0){
			json = "{\"flag\":true,\"msg\":\""+result.toString()+"\"}";
		}else{
			json = "{\"flag\":false,\"msg\":\""+result.toString()+"\"}";
		}
		return json;
	}

	//审核通过单条成本
	@ResponseBody
	@RequestMapping(value="passIsland", method=RequestMethod.POST)
	public String passIsland(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){		
		ReviewCompany reviewCompany= new ReviewCompany();
		reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
		int topLevel=reviewCompany.getTopLevel();
		CostRecordIsland costRecord=new CostRecordIsland();
		costRecord=costRecordIslandDao.findOne(id);
		Integer nowLevel=costRecord.getPayNowLevel();
		
		if (nowLevel==null) {
			nowLevel=1;
		}
		if(! nowLevel.equals( reviewLevel)) {
			return "[{\"result\":\"ok\"}]";
		}
		
		List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
		if (nowLevel<topLevel && isEndReviewList.size()==0) {
			costRecord.setPayNowLevel(reviewLevel+1);		
		}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
			costRecord.setPayReview(Context.REVIEW_COST_PASS);
			costRecord.setPayNowLevel(topLevel+1);	
		}else if (nowLevel == topLevel) {			
			costRecord.setPayReview(Context.REVIEW_COST_PASS);
			costRecord.setPayNowLevel(reviewLevel+1);
		}	
		costRecordIslandDao.save(costRecord);
		String result=ReviewCommonUtil.getNextPayIslandReview(id);
		if (nowLevel<topLevel && isEndReviewList.size()==0){
			  List<String> email = reviewService.getNextPayReviewEmail(costRecord.getId());
			  String productTypeName = DictUtils.getDictLabel(costRecord.getOrderType() + "", "order_type", "");//产品类型
			  String[] emails = new String[email.size()];
			  int i = 0;
			  for(String tmp : email){
				emails[i] = tmp;
				i++;
			  }			  
			  SendMailUtil.sendSimpleMail(emails, "付款审核提醒","您好，有用户发起" + productTypeName + "的" + "付款审核申请, 请及时处理。");
		}	
		
		CostRecordLog costRecordLog =new CostRecordLog();
		costRecordLog.setActivityUuid(costRecord.getActivityUuid());
		costRecordLog.setCostId(id);
		costRecordLog.setCostName(costRecord.getName()+"_付款审核");
		costRecordLog.setNowLevel(nowLevel);
		costRecordLog.setResult(1);//审核通过
		costRecordLog.setOrderType(orderType); 	
		costRecordLog.setRemark("");
		costRecordLog.setCreateDate(new Date());
		costRecordLog.setCreateBy(UserUtils.getUser().getId());		
		costRecordLogDao.save(costRecordLog);		
		return "[{\"result\":\""+result+"\"}]";
	}
	
	//审核通过单条成本
		@ResponseBody
		@RequestMapping(value="passHotel", method=RequestMethod.POST)
		public String passHotel(@RequestParam Long id,@RequestParam (required=true) Integer reviewLevel,@RequestParam (required=true) Long reviewCompanyId,
				@RequestParam (required=true) Integer orderType){
			ReviewCompany reviewCompany= new ReviewCompany();
			reviewCompany=reviewCompanyDao.findOne(reviewCompanyId);
			int topLevel=reviewCompany.getTopLevel();
			CostRecordHotel costRecord=new CostRecordHotel();
			costRecord=costRecordHotelDao.findOne(id);
			Integer nowLevel=costRecord.getPayNowLevel();
			
			if (nowLevel==null) {
				nowLevel=1;
			}
			if(! nowLevel.equals( reviewLevel)) {
				return "[{\"result\":\"ok\"}]";
			}
			
			List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
			if (nowLevel<topLevel && isEndReviewList.size()==0) {
				costRecord.setPayNowLevel(reviewLevel+1);		
			}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
				costRecord.setPayReview(Context.REVIEW_COST_PASS);
				costRecord.setPayNowLevel(topLevel+1);	
			}else if (nowLevel == topLevel) {			
				costRecord.setPayReview(Context.REVIEW_COST_PASS);
				costRecord.setPayNowLevel(reviewLevel+1);
			}	
			costRecordHotelDao.save(costRecord);
	 		String result=ReviewCommonUtil.getNextPayHotelReview(id);
			if (nowLevel<topLevel && isEndReviewList.size()==0){
				  List<String> email = reviewService.getNextPayReviewEmail(costRecord.getId());
				  String productTypeName = DictUtils.getDictLabel(costRecord.getOrderType() + "", "order_type", "");//产品类型
				  String[] emails = new String[email.size()];
				  int i = 0;
				  for(String tmp : email){
					emails[i] = tmp;
					i++;
				  }			  
				  SendMailUtil.sendSimpleMail(emails, "付款审核提醒","您好，有用户发起" + productTypeName + "的" + "付款审核申请, 请及时处理。");
			}	
			
			CostRecordLog costRecordLog =new CostRecordLog();
			costRecordLog.setActivityUuid(costRecord.getActivityUuid());
			costRecordLog.setCostId(id);
			costRecordLog.setCostName(costRecord.getName()+"_付款审核");
			costRecordLog.setNowLevel(nowLevel);
			costRecordLog.setResult(1);//审核通过
			costRecordLog.setOrderType(orderType); 	
			costRecordLog.setRemark("");
			costRecordLog.setCreateDate(new Date());
			costRecordLog.setCreateBy(UserUtils.getUser().getId());		
			costRecordLogDao.save(costRecordLog);		
			return "[{\"result\":\""+result+"\"}]";
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
 		StringBuffer json = new StringBuffer();
		json.append("[");
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && ! costId.equals("0")){ 				
 				CostRecord costRecord=new CostRecord();
 				costRecord=costRecordDao.findOne(Long.valueOf(costId));
 				if(costRecord.getPayReview()!= 1) continue;
 				Integer nowLevel=costRecord.getPayNowLevel();
 				if (nowLevel==null) {
 					nowLevel=1;
 				} 	
 				if(nowLevel != reviewLevel) continue;
 				
 				List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
 				if (nowLevel<topLevel && isEndReviewList.size()==0) {
 					costRecord.setPayNowLevel(reviewLevel+1);		
 				}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
 					costRecord.setPayReview(Context.REVIEW_COST_PASS);
 					costRecord.setPayNowLevel(topLevel+1);	
 				}else if (nowLevel == topLevel) {			
 					costRecord.setPayReview(Context.REVIEW_COST_PASS);
 					costRecord.setPayNowLevel(reviewLevel+1);
 				}		
 				costRecord.setPayUpdateBy(Integer.parseInt(UserUtils.getUser().getId()+""));
 				costRecord.setPayUpdateDate(new Date());
 				costRecordDao.save(costRecord);

 				json.append("{");
 				json.append("\"costid\":").append("\"").append(costId).append("\", ");
 				json.append("\"result\":").append("\"").append(ReviewCommonUtil.getNextPayReview(Long.parseLong(costId))).append("\"");
 				json.append("},");
 				
 				if (nowLevel<topLevel && isEndReviewList.size()==0){
 				  List<String> email = reviewService.getNextPayReviewEmail(costRecord.getId());
 	 			  String productTypeName = DictUtils.getDictLabel(costRecord.getOrderType() + "", "order_type", "");//产品类型
 	 			  String[] emails = new String[email.size()];
 	 			  int i = 0;
 	 			  for(String tmp : email){
 	 				emails[i] = tmp;
 	 				i++;
 	 			  }
 	 			  SendMailUtil.sendSimpleMail(emails, "付款审核提醒","您好，<br><br>有用户发起" + productTypeName + "的" + "付款审核申请, 请及时处理。");
 				}			
 		
 	 			
 				CostRecordLog costRecordLog =new CostRecordLog();
 				costRecordLog.setRid(costRecord.getActivityId());
 				costRecordLog.setCostId(Long.valueOf(costId));
 				costRecordLog.setCostName(costRecord.getName()+"_付款审核");
 				costRecordLog.setNowLevel(nowLevel);
 				costRecordLog.setResult(1);//审核通过
 				costRecordLog.setOrderType(orderType); 	
 				costRecordLog.setRemark("");
 				costRecordLog.setCreateDate(new Date());
 				costRecordLog.setCreateBy(UserUtils.getUser().getId());		
 				costRecordLogDao.save(costRecordLog);					
 			}
 		}
 		
 		if(json.lastIndexOf(",") > 0){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		json.append("]");		
		return json.toString();
  	} 
 	
    /*成本批量审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="saveCostIslandList")
  	public String saveCostIslandList(@RequestParam(required=true) String costList,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){ 		
 		ReviewCompany reviewCompany= new ReviewCompany();
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
 	    
		int topLevel=reviewCompany.getTopLevel();		
 		String tmpList[]=costList.split(",");
 		
 		StringBuffer json = new StringBuffer();
		json.append("[");
		
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && ! costId.equals("0")){ 				
 				CostRecordIsland costRecord=new CostRecordIsland();
 				costRecord=costRecordIslandDao.findOne(Long.valueOf(costId));
 				if(costRecord.getPayReview()!= 1) continue;
 				Integer nowLevel=costRecord.getPayNowLevel();
 				if (nowLevel==null) {
 					nowLevel=1;
 				} 	 
 				if(nowLevel != reviewLevel) continue;
 				List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
 				if (nowLevel<topLevel && isEndReviewList.size()==0) {
 					costRecord.setPayNowLevel(reviewLevel+1);		
 				}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
 					costRecord.setPayReview(Context.REVIEW_COST_PASS);
 					costRecord.setPayNowLevel(topLevel+1);	
 				}else if (nowLevel == topLevel) {			
 					costRecord.setPayReview(Context.REVIEW_COST_PASS);
 					costRecord.setPayNowLevel(reviewLevel+1);
 				}		
 				
 				costRecordIslandDao.save(costRecord);
 				json.append("{");
 				json.append("\"costid\":").append("\"").append(costId).append("\", ");
 				json.append("\"result\":").append("\"").append(ReviewCommonUtil.getNextPayIslandReview(Long.parseLong(costId))).append("\"");
 				json.append("},");
 				
 				if (nowLevel<topLevel && isEndReviewList.size()==0){
 				  List<String> email = reviewService.getNextPayReviewEmail(costRecord.getId());
 	 			  String productTypeName = DictUtils.getDictLabel(costRecord.getOrderType() + "", "order_type", "");//产品类型
 	 			  String[] emails = new String[email.size()];
 	 			  int i = 0;
 	 			  for(String tmp : email){
 	 				emails[i] = tmp;
 	 				i++;
 	 			  }
 	 			  SendMailUtil.sendSimpleMail(emails, "付款审核提醒","您好，<br><br>有用户发起" + productTypeName + "的" + "付款审核申请, 请及时处理。");
 				}	
 		        CostRecordLog costRecordLog =new CostRecordLog();
 				costRecordLog.setActivityUuid(costRecord.getActivityUuid());
 				costRecordLog.setCostId(Long.valueOf(costId));
 				costRecordLog.setCostName(costRecord.getName()+"_付款审核");
 				costRecordLog.setNowLevel(nowLevel);
 				costRecordLog.setResult(1);//审核通过
 				costRecordLog.setOrderType(orderType); 	
 				costRecordLog.setRemark("");
 				costRecordLog.setCreateDate(new Date());
 				costRecordLog.setCreateBy(UserUtils.getUser().getId());		
 				costRecordLogDao.save(costRecordLog);						
 			}
 		}
 		if(json.lastIndexOf(",") > 0){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		json.append("]");		
		return json.toString();
  	} 
 	
    /*成本批量审核*/ 	
 	@ResponseBody
  	@RequestMapping(value="saveCostHotelList")
  	public String saveCostHotelList(@RequestParam(required=true) String costList,@RequestParam (required=true) Long reviewCompanyId,
			@RequestParam (required=true) Integer orderType){ 		
 		ReviewCompany reviewCompany= new ReviewCompany();
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
 	    
		int topLevel=reviewCompany.getTopLevel();		
 		String tmpList[]=costList.split(","); 
 		StringBuffer json = new StringBuffer();
		json.append("[");
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && ! costId.equals("0")){ 				
 				CostRecordHotel costRecord=new CostRecordHotel();
 				costRecord=costRecordHotelDao.findOne(Long.valueOf(costId));
 				if(costRecord.getPayReview()!= 1) continue;
 				Integer nowLevel=costRecord.getPayNowLevel();
 				if (nowLevel==null) {
 					nowLevel=1;
 				} 	 
 				if(nowLevel != reviewLevel) continue;
 				List<Map<String, Object>> isEndReviewList = reviewSqlDao.getEndReviewLevel(reviewCompanyId,nowLevel,orderType);
 				if (nowLevel<topLevel && isEndReviewList.size()==0) {
 					costRecord.setPayNowLevel(reviewLevel+1);		
 				}else if (nowLevel<topLevel && isEndReviewList.size()>0) {
 					costRecord.setPayReview(Context.REVIEW_COST_PASS);
 					costRecord.setPayNowLevel(topLevel+1);	
 				}else if (nowLevel == topLevel) {			
 					costRecord.setPayReview(Context.REVIEW_COST_PASS);
 					costRecord.setPayNowLevel(reviewLevel+1);
 				}
 				
 				costRecordHotelDao.save(costRecord);
 				
 				json.append("{");
 				json.append("\"costid\":").append("\"").append(costId).append("\", ");
 				json.append("\"result\":").append("\"").append(ReviewCommonUtil.getNextPayHotelReview(Long.parseLong(costId))).append("\"");
 				json.append("},");
 				
 				if (nowLevel<topLevel && isEndReviewList.size()==0){
 				  List<String> email = reviewService.getNextPayReviewEmail(costRecord.getId());
 	 			  String productTypeName = DictUtils.getDictLabel(costRecord.getOrderType() + "", "order_type", "");//产品类型
 	 			  String[] emails = new String[email.size()];
 	 			  int i = 0;
 	 			  for(String tmp : email){
 	 				emails[i] = tmp;
 	 				i++;
 	 			  }
 	 			  SendMailUtil.sendSimpleMail(emails, "付款审核提醒","您好，<br><br>有用户发起" + productTypeName + "的" + "付款审核申请, 请及时处理。");
 				}	
 		        CostRecordLog costRecordLog =new CostRecordLog();
 				costRecordLog.setActivityUuid(costRecord.getActivityUuid());
 				costRecordLog.setCostId(Long.valueOf(costId));
 				costRecordLog.setCostName(costRecord.getName()+"_付款审核");
 				costRecordLog.setNowLevel(nowLevel);
 				costRecordLog.setResult(1);//审核通过
 				costRecordLog.setOrderType(orderType); 	
 				costRecordLog.setRemark("");
 				costRecordLog.setCreateDate(new Date());
 				costRecordLog.setCreateBy(UserUtils.getUser().getId());		
 				costRecordLogDao.save(costRecordLog);						
 			}
 		} 		
 		
 		if(json.lastIndexOf(",") > 0){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		json.append("]");		
		return json.toString();
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
	 * @param current
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
		return "modules/paymentreview/start";
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
		
		// 预报名
		Integer bookingPersonNum = islandOrderService.getBookingPersonNum(activityIslandGroupUuid);
		model.addAttribute("bookingPersonNum", bookingPersonNum == null ? 0 : bookingPersonNum);
		//加载团期下所有的房型数据
		List<ActivityIslandGroupRoom> groupRooms = activityIslandGroupRoomService.getByactivityIslandGroupUuid(activityIslandGroupUuid);
	    model.addAttribute("groupRooms", groupRooms);
    	//加载团期下所有的房型数据
		
		// 餐型
//		List<ActivityIslandGroupMeal> meals = activityIslandGroupMealService.getByactivityIslandGroupUuid(activityGroup.getUuid());
//		model.addAttribute("activityIslandGroupMeals", activityIslandGroupMeals);
		List<Object> rms = costIslandService.getRoomAndMealForIsland(activityUuid);
		model.addAttribute("rms", rms);

		HotelStar hotelStar=hotelStarService.getByUuid(hotel.getStar());
		model.addAttribute("hotelStar", hotelStar);
		User user= userDao.findById((long)activityIsland.getCreateBy());
		model.addAttribute("createByName", user.getName());		

		// 参考航班
		ActivityIslandGroupAirline activityIslandGroupAirline = activityIslandGroupAirlineService.getByactivityIslandGroup(activityGroup.getUuid()).get(0);
		model.addAttribute("activityIslandGroupAirline", activityIslandGroupAirline);

			
		/*
		String costSerial= activityGroup.getCost();
		String incomeSerial= activityGroup.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
		
		Integer typeId=12;
		model.addAttribute("typeId",typeId);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));	
		/*
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
		List<Map<String, Object>> budgetCost=costManageService.getCost(groupId,typeId,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(groupId,typeId,1);
		model.addAttribute("incomeList", incomeList); */
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
			//model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));			
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
           /*     List<List<CostRecord>> list = new ArrayList<List<CostRecord>>();
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
                model.addAttribute("isShow",isShow); */             
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
		return "modules/paymentreview/islandDetail";
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
	@RequestMapping(value="hotelDetail/{activityUuid}/{activityHotelGroupUuid}/{reviewLevel}/{reviewCompanyId}", method=RequestMethod.GET)
	public String hotelDetail(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="activityUuid") String activityUuid, 
			@PathVariable(value="reviewCompanyId") Long reviewCompanyId,
			@PathVariable(value="activityHotelGroupUuid") String activityHotelGroupUuid, Model model,
			HttpServletRequest request, HttpServletResponse response){
		String from = request.getParameter("from");
		ActivityHotelGroup activityGroup = activityHotelGroupService.getByUuid(activityHotelGroupUuid);
		ActivityHotel activityHotel = activityHotelService.getByUuid(activityUuid);	
		Hotel hotel = hotelService.getByUuid(activityHotel.getHotelUuid());
		Island island = islandService.getByUuid(activityHotel.getIslandUuid());
		model.addAttribute("islandName", island.getIslandName());
		model.addAttribute("hotelName", hotel.getNameCn());
		model.addAttribute("hotel", hotel);

		Integer star=hotelService.getHotelStarValByHotelUuid(activityHotel.getHotelUuid());
		model.addAttribute("star",star);
		
		User user= userDao.findById((long)activityHotel.getCreateBy());
		model.addAttribute("createByName", user.getName());
		//加载团期下所有的房型数据		
		List<ActivityHotelGroupRoom> groupRooms = activityHotelGroupRoomService.getRoomListByGroupUuid(activityHotelGroupUuid);
		model.addAttribute("groupRooms", groupRooms);		
		List<Object> rms = costIslandService.getRoomAndMealForHotel(activityUuid);
		model.addAttribute("rms", rms);
		//加载团期下所有的房型数据
//		List<ActivityHotelGroupRoom> groupRooms = activityHotelGroupRoomService.getRoomListByGroupUuid(activityHotelGroupUuid);
				
		/*
		String costSerial= activityGroup.getCost();
		String incomeSerial= activityGroup.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
		
		Integer typeId=11;
		model.addAttribute("typeId",typeId);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));	
		/*
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
		List<Map<String, Object>> budgetCost=costManageService.getCost(groupId,typeId,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(groupId,typeId,1);
		model.addAttribute("incomeList", incomeList);*/
		List<Map<String, Object>> budgetCost =costManageService.getHotelCost(activityGroup.getUuid(),0);
		List<Map<String, Object>> actualCost=costManageService.getHotelCost(activityGroup.getUuid(),1); 
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
			//model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));			
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
           /*     List<List<CostRecord>> list = new ArrayList<List<CostRecord>>();
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
                model.addAttribute("isShow",isShow); */             
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
		return "modules/paymentreview/hotelDetail";
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
	@RequestMapping(value="islandRead/{activityUuid}/{activityIslandGroupUuid}/{reviewLevel}", method=RequestMethod.GET)
	public String islandRead(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="activityUuid") String activityUuid, 
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
		// 预报名
		Integer bookingPersonNum = islandOrderService.getBookingPersonNum(activityIslandGroupUuid);
		model.addAttribute("bookingPersonNum", bookingPersonNum == null ? 0 : bookingPersonNum);
		
		//加载团期下所有的房型数据	
	    List<ActivityIslandGroupRoom> groupRooms = activityIslandGroupRoomService.getRoomListByGroupUuid(activityIslandGroupUuid);
        model.addAttribute("groupRooms", groupRooms);
		// 餐型
//		List<ActivityIslandGroupMeal> meals = activityIslandGroupMealService.getByactivityIslandGroupUuid(activityGroup.getUuid());
//		model.addAttribute("activityIslandGroupMeals", activityIslandGroupMeals);
		List<Object> rms = costIslandService.getRoomAndMealForIsland(activityUuid);
		model.addAttribute("rms", rms);

		HotelStar hotelStar=hotelStarService.getByUuid(hotel.getStar());
		model.addAttribute("hotelStar", hotelStar);
		User user= userDao.findById((long)activityIsland.getCreateBy());
		model.addAttribute("createByName", user.getName());		

		// 参考航班
		ActivityIslandGroupAirline activityIslandGroupAirline = activityIslandGroupAirlineService.getByactivityIslandGroup(activityGroup.getUuid()).get(0);
		model.addAttribute("activityIslandGroupAirline", activityIslandGroupAirline);
		/*
		String costSerial= activityGroup.getCost();
		String incomeSerial= activityGroup.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
		
		Integer typeId=12;
		model.addAttribute("typeId",typeId);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		
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
			//model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));
			
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
		/*List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
		model.addAttribute("incomeList", incomeList);*/
		List<Map<String, Object>> budgetCost =costManageService.getIslandCost(activityGroup.getUuid(),0);
		List<Map<String, Object>> actualCost=costManageService.getIslandCost(activityGroup.getUuid(),1); 
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost); 
		
		return "modules/paymentreview/islandRead";
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
	@RequestMapping(value="hotelRead/{activityUuid}/{activityHotelGroupUuid}/{reviewLevel}", method=RequestMethod.GET)
	public String hotelRead(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="activityUuid") String activityUuid, 
			@PathVariable(value="activityHotelGroupUuid") String activityHotelGroupUuid, Model model,
				HttpServletRequest request, HttpServletResponse response){
		String from = request.getParameter("from");
		ActivityHotelGroup activityGroup = activityHotelGroupService.getByUuid(activityHotelGroupUuid);
		ActivityHotel activityHotel = activityHotelService.getByUuid(activityUuid);	
		Hotel hotel = hotelService.getByUuid(activityHotel.getHotelUuid());
		Island island = islandService.getByUuid(activityHotel.getIslandUuid());
        model.addAttribute("islandName", island.getIslandName());
		model.addAttribute("hotel", hotel);
		
		Integer star=hotelService.getHotelStarValByHotelUuid(activityHotel.getHotelUuid());
		model.addAttribute("star",star);
		
		User user= userDao.findById((long)activityHotel.getCreateBy());
		model.addAttribute("createByName", user.getName());
		/*
		String costSerial= activityGroup.getCost();
		String incomeSerial= activityGroup.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));*/
		List<Map<String, Object>> budgetCost =costManageService.getHotelCost(activityGroup.getUuid(),0);
		List<Map<String, Object>> actualCost=costManageService.getHotelCost(activityGroup.getUuid(),1); 
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost); 
		
		//加载团期下所有的房型数据		
		List<ActivityHotelGroupRoom> groupRooms = activityHotelGroupRoomService.getRoomListByGroupUuid(activityHotelGroupUuid);
		model.addAttribute("groupRooms", groupRooms);	
		List<Object> rms = costIslandService.getRoomAndMealForHotel(activityUuid);
		model.addAttribute("rms", rms);
		Integer typeId=11;
		model.addAttribute("typeId",typeId);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
		
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
			//model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));
			
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
		/*List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
		List<Map<String, Object>> budgetCost=costManageService.getCost(groupId,typeId,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(groupId,typeId,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost); */
		return "modules/paymentreview/hotelRead";
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
	@RequestMapping(value="read/{activityId}/{groupId}/{reviewLevel}", method=RequestMethod.GET)
	public String readCurrent(@PathVariable(value="reviewLevel") Integer reviewLevel,
			@PathVariable(value="activityId") Long activityId, 
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
				
				model.addAttribute("budgetInList", budgetInList);
				model.addAttribute("budgetOutList", budgetOutList);				
				model.addAttribute("actualInList", actualInList);
				model.addAttribute("actualOutList", actualOutList);	
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
		List<Map<String, Object>> budgetCost=costManageService.getCost(groupId,typeId,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(groupId,typeId,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		return "modules/paymentreview/read";
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
		
	@Autowired
	private AreaService areaService;


	@Autowired
	private IFlightInfoService flightInfoService;

	@Autowired
	AirlineInfoService airlineInfoService;
	
	@Autowired
	AirportInfoService airportInfoService; 
	
	@Autowired
	private AirportService airportService;

    @Autowired
    private AirticketStockService airticketStockService;
    
    
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
		String createByName = request.getParameter("createByName"); // 审核发起人
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
 		List<UserJob> userJobs = costManageService.getReviewByFlowType(PAYREVIEW_FLOWTYPE,7);
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
				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, PAYREVIEW_FLOWTYPE, deptId);
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
					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId,PAYREVIEW_FLOWTYPE, deptId);
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
		
		ReviewCompany reviewCompany = reviewCompanyDao.getById(reviewCompanyId);
		model.addAttribute("reviewCompany", reviewCompany);
		model.addAttribute("createByName", createByName);
		model.addAttribute("reviewCompanyId", reviewCompanyId);        
        model.addAttribute("jobList", jobList);//审核职位列表
        model.addAttribute("userJobs", userJobs);//当前用户的职位
        model.addAttribute("userJobId", userJobId);	
		model.addAttribute("reviewLevel", reviewLevel);	
		model.addAttribute("areas", areaService.findAirportCityList(""));
        Page<ActivityAirTicketCost> page = activityAirTicketService.findAirCostReviewPage(new Page<ActivityAirTicketCost>(request, response),
                activityAirTicket, departureCityPara, arrivedCityPara,
                StringUtils.isEmpty(settlementAdultPriceStart) ? null : new BigDecimal(settlementAdultPriceStart),
                StringUtils.isEmpty(settlementAdultPriceEnd) ? null : new BigDecimal(settlementAdultPriceEnd),
                StringUtils.isNotBlank(request.getParameter("airType")) ? request.getParameter("airType") : null,
                groupOpenDate,groupCloseDate,review,reviewLevel,companyId,reviewCompanyId,PAYREVIEW_FLOWTYPE,request.getParameter("orderBy"),createByName);
        
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
		return "modules/paymentreview/airTicketList";
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
		
		return "modules/paymentreview/airTicketPreRecord";
	}
	
	
	@RequestMapping(value="airTicketRead/{airTicketId}/{reviewLevel}")
	public String readRecord(Model model,@PathVariable Long airTicketId,
			@PathVariable(value="reviewLevel") Integer reviewLevel){
		model.addAttribute("airTypes", DictUtils.getSysDicMap("air_Type"));// 机票类型
		model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));// 航空公司
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		
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
		return "modules/paymentreview/airTicketRead";
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
		//visaProducts.setProductStatus(new Integer(Context.PRODUCT_ONLINE_STATUS));// 
 		Long companyId= UserUtils.getUser().getCompany().getId(); 		
 		long reviewCompanyId=(long)0; 	 		
 		List<UserJob> userJobs = costManageService.getReviewByFlowType(PAYREVIEW_FLOWTYPE,6);
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
				reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId,PAYREVIEW_FLOWTYPE, deptId);
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
					reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, PAYREVIEW_FLOWTYPE, deptId);
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
		
		ReviewCompany reviewCompany = reviewCompanyDao.getById(reviewCompanyId);
		model.addAttribute("reviewCompany", reviewCompany);
        model.addAttribute("jobList", jobList);//审核职位列表
        model.addAttribute("reviewCompanyId", reviewCompanyId);//审核职位列表
        model.addAttribute("userJobs", userJobs);//当前用户的职位
        model.addAttribute("userJobId", userJobId);	
		model.addAttribute("reviewLevel", reviewLevel);		 
		model.addAttribute("companyId", companyId);
		model.addAttribute("createByName", createByName);
		Integer agentId = null ;
		if(StringUtils.isNotBlank(request.getParameter("agentId"))){
			agentId = Integer.parseInt(request.getParameter("agentId"));
		}
		Page<VisaProductsCostView> page = this.visaProductsService
				.findVisaCostViewPage(
						new Page<VisaProductsCostView>(request, response),
						visaProducts, productName, collarZoning, sysCountryId,
						visaType, review,reviewLevel,companyId,reviewCompanyId,supplierId,agentId,PAYREVIEW_FLOWTYPE,orderBy,createByName);
        
		List<Country> countryList = CountryUtils.getCountrys();	
		List<Currency> currencylist = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		
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
		
		model.addAttribute("supplierCompanyId", supplierId);
		
		return "modules/paymentreview/visaList";
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
		return "modules/paymentreview/visaDetail";
	}
	
	@RequestMapping(value="visaRead/{visaProductId}/{reviewLevel}", method=RequestMethod.GET)
	public String readCurrent(@PathVariable(value="visaProductId") Long visaProductId,
			@PathVariable(value="reviewLevel") Integer reviewLevel,
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		
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
		List<Map<String, Object>> actualCost=costManageService.getCost(visaProductId,6,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		return "modules/paymentreview/visaRead";	
	}
	
	

}
