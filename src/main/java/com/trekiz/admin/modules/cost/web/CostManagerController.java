package com.trekiz.admin.modules.cost.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quauq.review.core.engine.ReviewProcessService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewProcess;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.mail.SendMailUtil;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.ZipUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.common.utils.word.WordDownLoadUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TargetArea;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.IActivityGroupViewService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.service.sync.ActivityGroupSyncService;
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
import com.trekiz.admin.modules.cost.entity.CostRecordVO;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.cost.service.CostIslandService;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.finance.FinanceUtils;
import com.trekiz.admin.modules.finance.entity.ForeCast;
import com.trekiz.admin.modules.finance.entity.Settle;
import com.trekiz.admin.modules.finance.service.IForeCastService;
import com.trekiz.admin.modules.finance.service.ISettleService;
import com.trekiz.admin.modules.hotel.dao.ActivityHotelGroupDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.supplier.entity.Bank;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.entity.SupplierName;
import com.trekiz.admin.modules.supplier.repository.BankDao;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AirportInfoService;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.CommonUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.review.configuration.entity.ReviewCostPaymentConfiguration;
import com.trekiz.admin.review.configuration.service.ReviewCostPaymentConfigurationService;

import freemarker.template.TemplateException;

@Controller
@RequestMapping(value = "${adminPath}/cost/manager")
public class CostManagerController extends BaseController {
		
	private static final Log LOG = LogFactory.getLog(CostManagerController.class);
	
	private static int COSTREVIEW_FLOWTYPE=15; //成本审核的 flowType
	private static int PAYREVIEW_FLOWTYPE=18; //付款审核的 flowType
	
	private static final String AGENT_NAME_OLD = "非签约渠道";
	private static final String AGENT_NAME_NEW = "未签";
	private static final String AGENT_NAME_ZHIKE = "直客";
	
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;	
	
	@Autowired
	@Qualifier("activityGroupViewService")
	private IActivityGroupViewService activityGroupViewService;

	@Autowired	
	private ActivityGroupSyncService activityGroupService;
	
	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private ReviewService reviewService;
		
	@Autowired
	private OrderCommonService orderService;
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private VisaOrderService visaOrderService;

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
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private VisaProductsDao visaProductsDao;
	
	@Autowired
	private ActivityGroupDao activityGroupDao;
	
	@Autowired
	private IStockDao stockDao;
	
	@Autowired
	private RefundService refundService;	
	
	@Autowired
	private CostRecordDao costRecordDao;
	
	@Autowired
	private DocInfoService docInfoService;

	@Autowired
	private BankDao bankDao;
	@Autowired
	private PlatBankInfoService platBankInfoService;
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private ReviewCompanyDao reviewCompanyDao;
	
	@Autowired
	private CostRecordHotelDao costRecordHotelDao;
	
	@Autowired
	private CostRecordIslandDao costRecordIslandDao;
	
	@Autowired
	private ActivityHotelGroupDao activityHotelGroupDao;
	
	@Autowired
	private ActivityIslandGroupDao activityIslandGroupDao;
	
	@Autowired
	private CostIslandService costIslandService;
	
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	
	@Autowired
	private ReviewCostPaymentConfigurationService reviewCostPaymentConfigurationService;
	
	@Autowired
	private ReviewProcessService reviewProcessService;

	@Autowired
	private IForeCastService foreCastService;

	@Autowired
	private ISettleService settleService;
	
	
	@ModelAttribute
	public TravelActivity get(@RequestParam(required=false) Long id) {
		if(id!=null){
			return travelActivityService.findById(id);
		}else
			return new TravelActivity();
	}
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 148;
	}
	
	
	/**
	 * 财务需要录入成本的列表页
	 * @return
	 */   
   @RequestMapping(value="list/{typeId}")
   public String list(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,
		  Model model,@PathVariable(value="typeId") Integer typeId){
	   User user = UserUtils.getUser();
	   Office company = user.getCompany();
	   Subject currentUser = SecurityUtils.getSubject();
	   
	   //当用户只有机票或签证成本录入权限时，需要跳转到对应页面
	   if (currentUser.isPermitted("singleCost:manager:view")||
			   currentUser.isPermitted("looseCost:manager:view")||currentUser.isPermitted("studyCost:manager:view")||
			   currentUser.isPermitted("freeCost:manager:view")||currentUser.isPermitted("bigCustomerCost:manager:view")){
	   }else if(currentUser.isPermitted("airTicketCost:manager:view")){	
		 return "redirect:"+Global.getAdminPath()+"/cost/manager/airTicketList";			 	  
	   }else if(currentUser.isPermitted("visaCost:manager:view")){	
		 return "redirect:"+Global.getAdminPath()+"/cost/visa/list";			 	  
	   }else if(currentUser.isPermitted("island:manager:view")){	
		 return "redirect:"+Global.getAdminPath()+"/cost/island/list";			 	  
	   }else if(currentUser.isPermitted("hotel:manager:view")){	
		 return "redirect:"+Global.getAdminPath()+"/cost/island/hotel";			 	  
	   }
		//按部门展示
	   DepartmentCommon common = departmentService.setDepartmentPara("cost", model);

	   Map<String, Object> params = new HashMap<String, Object>();
	   String groupCode=request.getParameter("groupCode");
	   String commitType=request.getParameter("commitType");
	   String operator = request.getParameter("operator");
	   String supplierId = request.getParameter("supplierId");
	   String isReject = request.getParameter("isReject");
	   params.put("groupCode", groupCode);
	   params.put("commitType", commitType);
	   params.put("operator", operator);
	   params.put("supplierId", supplierId);
	   params.put("orderType", typeId);
	   params.put("isReject", isReject);
	   Page<Map<String, Object>> page = activityGroupViewService.findActivityGroupCostView(new Page<Map<String, Object>>(request, response), travelActivity,
				common, params);

	   //天马运通显示预计总成本、实际总成本
	   if(Context.SUPPLIER_UUID_TMYT.equals(company.getUuid())) {
		   model.addAttribute("TMYT", true);
		   for (Map<String, Object> map : page.getList()) {
			   String groupId = map.get("id").toString();
			   List<Map<String, Object>> budgetCost=costManageService.getCost(Long.parseLong(groupId),typeId,0);
			   List<Map<String, Object>> actualCost=costManageService.getCost(Long.parseLong(groupId),typeId,1);
			   BigDecimal budgetTotal = OrderCommonUtil.getSum(budgetCost, "cost");
			   map.put("budgetTotal", budgetTotal);
			   BigDecimal actualTotal = OrderCommonUtil.getSum(actualCost, "cost");
			   map.put("actualTotal", actualTotal);
		   }
	   }
	   
	   //--- 随机测试bug16652 添加悬浮显示  modify by wangyang 2016.10.31---S
	   List<Map<String, Object>> list = page.getList();
	   String[] targetAreaNames = new String[list.size()];
	   for (int i = 0; i < list.size(); i++) {
		   Integer srcActivityId = (Integer) list.get(i).get("srcActivityId");
		   // 获取团期id下的目的地集合
		   List<TargetArea> targetAreas = AreaUtil.findTargetAreaById(srcActivityId.longValue());
		   if (targetAreas.size() > 0) {
			   StringBuilder sb = new StringBuilder();
			   for (TargetArea ta : targetAreas) {
				   sb.append(ta.getName()).append(",");
			   }
			   // 拼接目的地字符串，并去掉末尾的","
			   targetAreaNames[i] = sb.substring(0, sb.length() - 1);
		   } else {
			   targetAreaNames[i] = "";
		   }
	   }
	   model.addAttribute("areaTitle", targetAreaNames);
	   //--- 随机测试bug16652 添加悬浮显示  modify by wangyang 2016.10.31---E
	   
	   // 鼎鸿假期添加筛选条件  for 0416 by jinxin.gao
	   boolean DHJQ = false;
	   if(Context.SUPPLIER_UUID_DHJQ.equals(company.getUuid())){
		   DHJQ = true;
	   }

	   //热点好运通添加筛选条件 0469  shijun.liu  2016.06.27
	   boolean TMYT = false;
	   if(Context.SUPPLIER_UUID_TMYT.equals(company.getUuid())){
		   TMYT = true;
	   }
	   model.addAttribute("params", params);
	   model.addAttribute("DHJQ", DHJQ);
	   model.addAttribute("TMYT", TMYT);
	   model.addAttribute("supplierList", UserUtils.getSupplierInfoList(company.getId(), ""));
	   model.addAttribute("travelActivity", travelActivity);
       model.addAttribute("page", page);
       return "modules/cost/costList";
	}	 

	/*环球行，成本录入表单*/
	@RequestMapping(value = "addCostHQX/{activityId}/{groupId}/{budgetType}/{typeId}/{deptId}")
	public String addCostHQX(@PathVariable(value="activityId") Integer activityId,
							 @PathVariable(value="groupId") Integer groupId,
							 @PathVariable(value="budgetType") Integer budgetType,
							 @PathVariable(value="typeId") Integer typeId,
							 @PathVariable(value="deptId") Long deptId,Model model) {

		Office company = UserUtils.getUser().getCompany();
		Long companyId= company.getId();
		String companyName = company.getCompanyName();
		String companyUuid = company.getUuid();

		model.addAttribute("deptId", deptId);
		
		List<Currency> currencylist = currencyService.findCurrencyList(companyId);
		model.addAttribute("groupId", groupId);
		model.addAttribute("activityId", activityId);
		model.addAttribute("typeId", typeId);
		model.addAttribute("budgetType", budgetType);
		model.addAttribute("curlist", currencylist);
		model.addAttribute("companyId",companyId);
		model.addAttribute("companyName",companyName);
		model.addAttribute("companyUuid",companyUuid);
		List<String> supplyTypeList = new ArrayList<String>();
		if(companyId==68){
			supplyTypeList.add("1");
			supplyTypeList.add("5");
			supplyTypeList.add("8");
			List<SysCompanyDictView> supplytypelist = costManageService.supplierList(companyId);
			model.addAttribute("supplytypelist", supplytypelist);
		} else {
			supplyTypeList.add("11");
			List<SysCompanyDictView> supplytypelist = costManageService.supplierList(companyId);
			model.addAttribute("supplytypelist", supplytypelist);
		}
		List<Map<String, Object>> agentinfo = agentinfoService.findAllAgentinfo(companyId);
		model.addAttribute("agentinfo", agentinfo);
		if(companyName.equals("拉美途") && agentinfo != null) {
			for (Map<String, Object> agent : agentinfo) {
				if(agent.get("agentName").toString().equals("拉美途")) {
					model.addAttribute("agentId", agent.get("id"));
					List<Bank> bankList = supplierInfoService.findBank(2,Integer.parseInt(agent.get("id").toString()));
					if(bankList != null) {
						for(Bank bank : bankList) {
							if(bank.getBankName().equals("招商银行北京分行亦庄支行")) {
								model.addAttribute("bank", bank);
							}
						}
					}
				}
			}
		}
		
		//获取同一团期或者产品下的所有订单
		String permission = OrderCommonUtil.getStringOrderType(typeId) + ":order:view";
		model.addAttribute("permission", permission);
		if (SecurityUtils.getSubject().isPermitted(permission)) {
			if (typeId <= 5 || typeId == 10) { // 单团类
				List<Map<String, Object>> orders = orderService.findOrderIdAndNoByGroupId(Long.valueOf(groupId));
				model.addAttribute("orders", orders);
			} else if (typeId == 7) { // 机票类
				List<Map<String, Object>> orders = airTicketOrderService.findOrderIdAndNoByActivityId(Long.valueOf(groupId));
				model.addAttribute("orders", orders);
			} else if (typeId == 6) { // 签证类
				List<Map<String, Object>> orders = visaOrderService.findOrderIdAndNoByProductId(groupId.toString());
				model.addAttribute("orders", orders);
			}
		}
		
		//天马运通Uuid
		model.addAttribute("TMYT", Context.SUPPLIER_UUID_TMYT);
		//越柬行踪uuid
		model.addAttribute("YJXZ", Context.SUPPLIER_UUID_YJXZ);
		//鼎鸿假期uuid
		model.addAttribute("DHJQ", Context.SUPPLIER_UUID_DHJQ);
		
		if (budgetType == 2) {
			return "modules/cost/addOtherCostHQX";
		} else {
			return "review/cost/addCostHQX";
		}
	}
	
    /*环球行，批量提交审核*/
 	@ResponseBody
  	@RequestMapping(value="saveCostList")
  	public String saveCostList(@RequestParam(required=true) String costList,@RequestParam String visaIds,
  			@RequestParam(value="groupId") Long groupId,@RequestParam(value="orderType") Integer orderType,HttpServletRequest request){
 		String tmpList[]=costList.split(",");
 		String visaTempList[]=visaIds.split(",");
 		Long companyId= UserUtils.getUser().getCompany().getId();
 		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
 		Integer budgetCostAutoPass=UserUtils.getUser().getCompany().getBudgetCostAutoPass();
 		for(int i=0;i<tmpList.length;i++){ 
 			String costId = tmpList[i];
 			String visaId = "";
 			if(visaTempList != null && visaTempList.length > 1) {
 				visaId = visaTempList[i];
 			}
 			if(costId.length()>0 && !costId.equals("0") && visaId.length()==0){ 				
 				 CostRecord costRecord=costRecordDao.findOne(Long.valueOf(costId));
 				
 				if((costRecord.getBudgetType() == 0 && budgetCostAutoPass == 1) || (costRecord.getBudgetType() == 1 && costAutoPass==1)){
 					 costRecord.setReview(2);
 				}else{
 					 costRecord.setReview(1);
 				}
 				costRecord.setNowLevel(1); 				
 				if(costRecord.getBudgetType()==0 && budgetCostAutoPass==1){
 					CostRecord cr=costManageService.copyCost(costRecord);
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
 					costManageService.saveCostRecord(cr); 
 					
 				}
 				 costRecordDao.save(costRecord);
 			}else if(costId.length()>0 && !costId.equals("0") && visaId.length()>0 && !visaId.equals("0")) { //拉美途
 				List<CostRecord> visaList = costManageService.getVisaCost(groupId);
				for (CostRecord costRecord : visaList) {
					if(Long.parseLong(visaId) == costRecord.getId()) {
						CostRecord cr = costManageService.copyCost(costRecord);
						cr.setActivityId(groupId);
						cr.setOrderType(orderType);
						cr.setDelFlag("0");
						cr.setReview(2);
						cr.setBudgetType(0);
						Date date = new Date();
						User user = UserUtils.getUser();
						cr.setCreateBy(user);
						cr.setCreateDate(date);
						cr.setUpdateBy(user.getId());
						cr.setUpdateDate(date);
						cr.setVisaId(Long.parseLong(visaId));
						cr.setOverseas(0);
						String bankName = "";
						String bankAccountCode = "";
//						String sql = "select bank.bankName, bank.bankAccountCode from plat_bank_info bank, agentinfo agent " +
//								"where bank.beLongPlatId = agent.id and bank.platType = 2 and bank.delFlag = 0 and agent.agentName = '拉美途'";
//						List<Map<String, String>> bankList = costRecordDao.findBySql(sql, Map.class);
						//将上面的sql移到costManageService
						List<Map<String,String>> bankList = costManageService.getBankList();
						if(bankList != null && bankList.size() > 0) {
							bankName = bankList.get(0).get("bankName");
							bankAccountCode = bankList.get(0).get("bankAccountCode");
						}
						cr.setBankName(bankName);
						cr.setBankAccount(bankAccountCode);
						Long reviewCompanyId=(long)0;
				 		Long payReviewCompanyId=(long)0;
						String deptIdStr = request.getParameter("deptId");
				 		Long userId = UserUtils.getUser().getId();
				 		List<Long> deptList=  costManageService.getDeptList(userId);
				 		Long deptId = 0L;
				 		if(StringUtils.isNotEmpty(deptIdStr)) {
				 			deptId = Long.parseLong(deptIdStr);
				 		}
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
						cr.setReviewCompanyId(reviewCompanyId);
						cr.setPayReviewCompanyId(payReviewCompanyId);
						cr.setPayReview(4);
						cr.setPayUpdateBy(Integer.parseInt(userId.toString()));
						cr.setPayUpdateDate(date);
						costManageService.saveCostRecord(cr);
						
//						if(costAutoPass!=null && costAutoPass==1 && costRecord.getBudgetType()==0){
							CostRecord c = costManageService.copyCost(costRecord);
							c.setActivityId(groupId);
							c.setOrderType(orderType);
							c.setReview(2);
							c.setBudgetType(1);
							c.setCreateBy(UserUtils.getUser());
							c.setCreateDate(date);
							c.setOverseas(0);
							c.setSupplyType(1);
							c.setUpdateBy(UserUtils.getUser().getId());
							c.setUpdateDate(date);
//							c.setVisaId(Long.parseLong(visaId));
							c.setBankName(bankName);
							c.setBankAccount(bankAccountCode);
							c.setPayStatus(0);
							c.setPayReview(4);
							c.setPayNowLevel(1);
							c.setPayUpdateBy(Integer.parseInt(user.getId().toString()));
							c.setPayUpdateDate(date);
							c.setReviewCompanyId(reviewCompanyId);
							c.setPayReviewCompanyId(payReviewCompanyId);
							c.setPayReview(4);
							c.setPayUpdateBy(Integer.parseInt(userId.toString()));
							c.setPayUpdateDate(date);
							costManageService.saveCostRecord(c);
//						}
					}
				}
 			}
 		}
  		return "";
  	} 
 
    /*环球行，批量提交 付款审核*/ 
 	@ResponseBody
  	@RequestMapping(value="savePayList")
 	@Deprecated
  	public String savePayList(@RequestParam(required=true) String costList){
 		String tmpList[]=costList.split(",");
 		for(String costId:tmpList){ 
 			if(costId.length()>0 && !"0".equals(costId)){
 				 CostRecord costRecord=costRecordDao.findOne(Long.valueOf(costId));
 				 Date date = new Date();
 				 costRecord.setPayReview(1);
 				 costRecord.setPayNowLevel(1);
 				 costRecord.setUpdateDate(date);
 				 costRecord.setPayApplyDate(date);
 				 costRecordDao.save(costRecord);
 			}
 		}
  		return "";
  	} 
 	
      /*环球行，保存成本录入*/
 	@RequestMapping(value="saveHQX")
 	public String saveHQX(@RequestParam(required=true) String itemname, @RequestParam(required=false) BigDecimal price,@RequestParam(required=false) String comment,
     @RequestParam(required=true) Long activityId,  @RequestParam(required=false) Long groupId,@RequestParam(required=false) Integer typeId,@RequestParam(required=false) Integer quantity,@RequestParam(required=false) Integer currencyId,    
     @RequestParam(required=false) Integer overseas,@RequestParam(required=false) Integer supplyType,@RequestParam(required=false) Integer agentId,        
     @RequestParam(required=false) Integer first,@RequestParam(required=false) Integer supplier,@RequestParam(required=false) Integer budgetType,@RequestParam(required=true) Long deptId,
     @RequestParam(required=false) Long bank,@RequestParam(required=false) String bankname,@RequestParam(required=false) String account,@RequestParam(required=false) Integer review,
     @RequestParam(required=false)  BigDecimal rate,@RequestParam(required=false) Integer currencyAfter, @RequestParam(required=false)  BigDecimal priceAfter){	
 		Long companyId= UserUtils.getUser().getCompany().getId();
 		long userId = UserUtils.getUser().getId(); 		 
 		CostRecord costRecord= new CostRecord();
 		costRecord.setUuid(UuidUtils.generUuid());
 		costRecord.setPayUpdateBy(Integer.parseInt(userId+""));
 		costRecord.setPayUpdateDate(new Date());
 		costRecord.setName(itemname);		
 		costRecord.setPrice(price);
 		costRecord.setQuantity(quantity); 	
 		costRecord.setDelFlag("0");
 		costRecord.setCreateBy(UserUtils.getUser());
 		costRecord.setUpdateBy(UserUtils.getUser().getId());
 		costRecord.setCreateDate(new Date());
 		costRecord.setUpdateDate(new Date());
 		costRecord.setPayApplyDate(new Date());
 		if (bank != null) {
			if (bank == (long) -1) { /* 没有默认银行账号 */
				costRecord.setBankName(bankname);
				costRecord.setBankAccount(account);
			} else {
				Bank mybank = bankDao.findOne(bank);
				costRecord.setBankName(mybank.getBankName());
				costRecord.setBankAccount(mybank.getBankAccountCode());
			}
		}
 		costRecord.setCurrencyAfter(currencyAfter);
 		costRecord.setRate(rate);
 		costRecord.setPriceAfter(priceAfter);
 		costRecord.setSupplierType(first);
 		costRecord.setNowLevel(1);
 		costRecord.setBudgetType(budgetType);
 		//costRecord.setDeptId(deptId); 
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
 		costRecord.setReviewCompanyId(reviewCompanyId);
 		costRecord.setPayReviewCompanyId(payReviewCompanyId);
 		if(budgetType==1){ 	 			
 			costRecord.setPayReview(4);
 			costRecord.setPayNowLevel(1); 			
 		} 		
 		costRecord.setSupplyType(supplyType);
 		costRecord.setOrderType(typeId); 		
 		costRecord.setPayStatus(0);
 		if(supplyType==1){
 			 costRecord.setSupplyId(agentId);
 			Agentinfo agentinfo= new Agentinfo();
 			agentinfo=agentinfoService.findOne((long)agentId);
 			costRecord.setSupplyName(agentinfo.getAgentName());
 		}else {
 			costRecord.setSupplyId(supplier);
 			SupplierInfo supplierInfo = new SupplierInfo();
 			supplierInfo= supplierInfoService.findSupplierInfoById((long)supplier);
 			costRecord.setSupplyName(supplierInfo.getSupplierName());
 		}
 		costRecord.setActivityId(groupId); 	 		
 		if(overseas == null || "".equals(overseas)) {
 			costRecord.setOverseas(0);
 		}else{
 			costRecord.setOverseas(overseas);
 		}		
 		costRecord.setCurrencyId(currencyId);
 		costRecord.setReview(review);
 		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();//实际成本配置
 		Integer budgetCostAutoPass = UserUtils.getUser().getCompany().getBudgetCostAutoPass();//预算成本配置
 		//if((companyId==68|| companyId==83 || companyId==75|| companyId==72) && review==1){
// 		if(costAutoPass!=null && costAutoPass==1 && review==1){
// 			costRecord.setReview(2);
// 		} 
 		
 		costRecord.setReviewType(0); 		
 		if(StringUtils.isNotEmpty(comment)){
 			costRecord.setComment(comment);
 		} 		
 		if(budgetType == 0 && budgetCostAutoPass == 1 && review==1) {//预算成本自动审核通过
 			costRecord.setReview(2);
 			
 			//添加实际成本
 			CostRecord cr=costManageService.copyCost(costRecord);
			cr.setUuid(UuidUtils.generUuid());
	 		cr.setReviewCompanyId(reviewCompanyId); 
	 	 	cr.setPayReviewCompanyId(payReviewCompanyId); 
	 	 	cr.setPayNowLevel(1);
	 	 	cr.setPayReview(4);
	 	 	cr.setBudgetType(1);
	 	 	
 			if (costAutoPass == 1){
 	 			cr.setNowLevel(4);
 	 			cr.setReview(2);
 	 		} else if (costAutoPass == 0) {
 	 			cr.setNowLevel(1);
 		 	 	cr.setReview(4);
 	 		}
 			costManageService.saveCostRecord(cr);//保存实际成本
 		}
 		if(budgetType == 1 && costAutoPass == 1 && review==1) {//实际成本自动审核通过
 			costRecord.setReview(2);
 		}
 		costManageService.saveCostRecord(costRecord);
 		
 		if(typeId<6 ||typeId==10)
 		  return "redirect:"+Global.getAdminPath()+"/cost/manager/flow/start/"+activityId+"/"+groupId +"/"+typeId;
 		else if(typeId==6)
 		  return "redirect:"+Global.getAdminPath()+"/cost/visa/flow/"+activityId;
 		else 	 	
 		  return "redirect:"+Global.getAdminPath()+"/cost/manager/airTicketPreRecord/"+activityId;
			
 	}
 	
 	
	/*环球行，成本录入表单*/
	@RequestMapping(value = "updateCostHQX/{activityId}/{groupId}/{costId}/{typeId}/{deptId}")
	public String updateCostHQX(@PathVariable(value="activityId") Integer activityId,@PathVariable(value="groupId") Integer groupId,@PathVariable(value="costId") Long costId, 
			@PathVariable(value="typeId") Integer typeId,@PathVariable(value="deptId") Long deptId,Model model) {
		Long companyId= UserUtils.getUser().getCompany().getId();
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		List<Currency> currencylist = currencyService.findCurrencyList(companyId);
		CostRecord costRecord= new CostRecord();
		costRecord=costRecordDao.findOne(costId);
		model.addAttribute("companyId", companyId);
		model.addAttribute("companyName", companyName);
		model.addAttribute("companyUuid", companyUuid);
		model.addAttribute("groupId", groupId);
		model.addAttribute("deptId", deptId);
		model.addAttribute("activityId", activityId);
		model.addAttribute("payReview",costRecord.getPayReview());
		model.addAttribute("review",costRecord.getReview());
		model.addAttribute("budgetType",costRecord.getBudgetType());
		model.addAttribute("supplyType",costRecord.getSupplyType());
		model.addAttribute("curlist", currencylist);
		model.addAttribute("typeId", typeId);
		model.addAttribute("costRecord", costRecord);
		model.addAttribute("userId", UserUtils.getUser().getId());
		
		List<SupplierName> supplierTypeList = new ArrayList<SupplierName>();
		supplierTypeList = supplierInfoService.findSupplierName(costRecord.getSupplierType(), companyId);
		model.addAttribute("supplierTypeList", supplierTypeList);
//		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		Integer budgetCostAutoPass = 0;
		ReviewProcess rp1 = reviewProcessService.obtainReviewProcess(companyUuid, deptId.toString(), typeId.toString(), Context.REVIEW_FLOWTYPE_STOCK.toString());
		if(rp1 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(rp1.getProcessKey())) {
			budgetCostAutoPass = 1;
		}
		model.addAttribute("budgetCostAutoPass",budgetCostAutoPass);
		Integer costAutoPass = 0;
		ReviewProcess rp2 = reviewProcessService.obtainReviewProcess(companyUuid, deptId.toString(), typeId.toString(), Context.REVIEW_FLOWTYPE_ACTUAL_COST.toString());
		if(rp2 != null && ReviewConstant.REVIEW_PROCESS_KEY_NOTHING.equals(rp2.getProcessKey())) {
			costAutoPass = 1;
		}
		model.addAttribute("costAutoPass",costAutoPass);
		
//		if( costAutoPass!=null){
//				model.addAttribute("costAutoPass",costAutoPass);
//			}else{
//				model.addAttribute("costAutoPass","0");
//			}
		List<String> supplyTypeList = new ArrayList<String>();
		if (companyId == 68) {
			supplyTypeList.add("1");
			supplyTypeList.add("5");
			supplyTypeList.add("8");
			List<SysCompanyDictView> supplytypelist=costManageService.supplierList(companyId);
			model.addAttribute("supplytypelist", supplytypelist);
		} else {
			supplyTypeList.add("11");
			List<SysCompanyDictView> supplytypelist=costManageService.supplierList(companyId);
			model.addAttribute("supplytypelist", supplytypelist);
		}
		
		List<SupplierName> listMap = new ArrayList<SupplierName>();
		if( costRecord.getSupplierType()==null){
			model.addAttribute("supplierType", 0);
			listMap = supplierInfoService.findSupplierName(0, companyId);
		}else{
			model.addAttribute("supplierType", costRecord.getSupplierType());
			listMap = supplierInfoService.findSupplierName(costRecord.getSupplierType(), companyId);
		}
		model.addAttribute("supplylist", listMap);
		
		List<Map<String, Object>> agentinfo = agentinfoService.findAllAgentinfo(companyId);
		model.addAttribute("agentinfo", agentinfo);

		List<Bank> bankList = supplierInfoService.findBank(costRecord.getSupplyType() + 1, costRecord.getSupplyId());
		model.addAttribute("bankList", bankList);
		
		Bank oneBank= supplierInfoService.findBankByNameAccount(costRecord.getBankName(), costRecord.getBankAccount(), Integer.parseInt(companyId.toString()));
		model.addAttribute("oneBank", oneBank);
		
		ReviewCostPaymentConfiguration configuration = reviewCostPaymentConfigurationService.getConfiguration(deptId.toString(), costRecord.getOrderType().toString(), Context.REVIEW_FLOWTYPE_PAYMENT.toString());
		if(configuration != null) {
			model.addAttribute("conf", configuration.getIsPaymentEqualsCost());
		}

		//天马运通Uuid
		model.addAttribute("TMYT", Context.SUPPLIER_UUID_TMYT);
		//越柬行踪uuid
		model.addAttribute("YJXZ", Context.SUPPLIER_UUID_YJXZ);
		//鼎鸿假期uuid
		model.addAttribute("DHJQ", Context.SUPPLIER_UUID_DHJQ);
		
		//获取实际成本附件
		if(StringUtils.isNotBlank(costRecord.getCostVoucher())) {
			List<DocInfo> docList = new ArrayList<DocInfo>();
			String docIds = costRecord.getCostVoucher();
			docList = docInfoService.getDocInfoBydocids(docIds);
			model.addAttribute("docList", docList);
		}
		
		if (costRecord.getBudgetType() == 2) {
			return "modules/cost/updateOtherCostHQX";
		} else {
			return "review/cost/updateCostHQX";
		}
	}
		
    /*环球行，保存成本录入*/
	@RequestMapping(value="updateHQX")
	public String updateHQX(@RequestParam(required=true) Long costId,@RequestParam(required=true) String itemname, @RequestParam(required=false) BigDecimal price,@RequestParam(required=false) String comment,
   @RequestParam(required=true) Long activityId,  @RequestParam(required=false) Long groupId,@RequestParam(required=false) Integer typeId,@RequestParam(required=false) Integer quantity,@RequestParam(required=false) Integer currencyId,    
   @RequestParam(required=false) Integer overseas, @RequestParam(required=false) Integer first,@RequestParam(required=false) String supplyName,@RequestParam(required=false) Integer budgetType,@RequestParam(required=true) Long deptId,
   @RequestParam(required=false) String bankname,@RequestParam(required=false) String account,@RequestParam(required=false) Integer review,@RequestParam(required=false) Integer payReview,
   @RequestParam(required=false)  BigDecimal rate,@RequestParam(required=false) Integer currencyAfter, @RequestParam(required=false)  BigDecimal priceAfter, @RequestParam(required = false) Integer supplyType, 
   @RequestParam(required = false) Long bank, @RequestParam(required = false) Integer agentId, @RequestParam(required = false) Integer supplier){	
		//Long companyId= UserUtils.getUser().getCompany().getId(); 
		Long companyId= UserUtils.getUser().getCompany().getId();	
		CostRecord costRecord=costRecordDao.findOne(costId);
		costRecord.setName(itemname);		
		costRecord.setPrice(price);
		costRecord.setQuantity(quantity);
	    costRecord.setBankName(bankname);
	 	costRecord.setBankAccount(account);	
		
		costRecord.setCurrencyAfter(currencyAfter);
		costRecord.setRate(rate);
		costRecord.setPriceAfter(priceAfter);
		
		costRecord.setNowLevel(1);
		//costRecord.setReviewCompanyId((long)1);
		
		if (bank != null) {
			if (bank == (long) -1) { /* 没有默认银行账号 */
				costRecord.setBankName(bankname);
				costRecord.setBankAccount(account);
			} else {
				Bank mybank = bankDao.findOne(bank);
				costRecord.setBankName(mybank.getBankName());
				costRecord.setBankAccount(mybank.getBankAccountCode());
			}
		}
		
		costRecord.setOrderType(typeId);
		costRecord.setReview(review);
		costRecord.setPayReview(payReview);		
		costRecord.setSupplierType(first);
		if(supplyType!=null){
		  costRecord.setSupplyType(supplyType);
		}
		
		// 预算成本和实际成本中没有客户类别，而其它收入有客户类别，所以需要分别判断
		if(costRecord.getBudgetType() == 2) {
			costRecord.setSupplyType(supplyType);
			if(supplyType==1){
				costRecord.setSupplyId(agentId);
				Agentinfo agentinfo = new Agentinfo();
				agentinfo = agentinfoService.findOne((long) agentId);
				costRecord.setSupplyName(agentinfo.getAgentName());
			} else {
				costRecord.setSupplyId(supplier);
				SupplierInfo supplierInfo = new SupplierInfo();
				supplierInfo = supplierInfoService
						.findSupplierInfoById((long) supplier);
				costRecord.setSupplyName(supplierInfo.getSupplierName());
			}
		} else {
			if(costRecord.getSupplyType()!=null && costRecord.getSupplyType()==1){
				costRecord.setSupplyId(agentId);
				Agentinfo agentinfo = new Agentinfo();
				agentinfo = agentinfoService.findOne((long) agentId);
				costRecord.setSupplyName(agentinfo.getAgentName());
			} else {
				costRecord.setSupplyId(supplier);
				SupplierInfo supplierInfo = new SupplierInfo();
				supplierInfo = supplierInfoService
						.findSupplierInfoById((long) supplier);
				costRecord.setSupplyName(supplierInfo.getSupplierName());
			}
		}
		
		//撤销审核时，设置 nowLevel=1
		if(review==4){
			costRecord.setNowLevel(1);
		}
		costRecord.setPayStatus(0);			
		if(overseas == null || "".equals(overseas)) {
 			costRecord.setOverseas(0);
 		}else{
 			costRecord.setOverseas(overseas);
 		}	
		costRecord.setCurrencyId(currencyId);		
		if(StringUtils.isNotEmpty(comment)){
			costRecord.setComment(comment);
		}		
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		Integer budgetCostAutoPass=UserUtils.getUser().getCompany().getBudgetCostAutoPass();
		if(costRecord.getBudgetType()==0 && budgetCostAutoPass==1 && review==1){
			CostRecord cr=costManageService.copyCost(costRecord);
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
	
	 		costRecord.setReview(2);
	 		cr.setReviewCompanyId(reviewCompanyId);
	 		cr.setPayReviewCompanyId(payReviewCompanyId);
	 		cr.setPayNowLevel(1);
	 		cr.setPayReview(4);
	 		cr.setBudgetType(1);
	 		if(costAutoPass == 1) {
	 			cr.setNowLevel(4);
				cr.setReview(2);
	 		}else if(costAutoPass == 1) {
	 			cr.setNowLevel(1);
 		 	 	cr.setReview(4);
	 		}
			
			costManageService.saveCostRecord(cr);
			List<String> email = reviewService.getNextPayReviewEmail(cr.getId());
 			String productTypeName = DictUtils.getDictLabel(cr.getOrderType() + "", "order_type", "");//产品类型
 			String[] emails = new String[email.size()];
 			int i = 0;
 			for(String tmp : email){
 				emails[i] = tmp;
 				i++;
 			}
 			SendMailUtil.sendSimpleMail(emails, "付款审核提醒","您好，有用户发起" + productTypeName + "的" + "付款审核申请, 请及时处理");
 
		}
		
		costRecordDao.save(costRecord);
		
		//return "[{\"result\":\"ok\"}]";
		if(typeId<6 || typeId==10)
 	 	  return "redirect:"+Global.getAdminPath()+"/cost/manager/flow/start/"+activityId+"/"+groupId +"/"+typeId;
 	 	else if(typeId==6)
 	 	  return "redirect:"+Global.getAdminPath()+"/cost/visa/flow/"+activityId;
 	 	else 	 	 	
		  return "redirect:"+Global.getAdminPath()+"/cost/manager/airTicketPreRecord/"+activityId;
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
	@RequestMapping(value="delete", method=RequestMethod.POST)
	public String delete(@RequestParam(value="id") Long id, @RequestParam(value="type") String type, 
			@RequestParam(value="groupId") Long groupId, @RequestParam(value="orderType") Integer orderType,
			@RequestParam(value="visaId") Long visaId,HttpServletRequest request) {
		User user = UserUtils.getUser();
		Date date = new Date();
		try {
			if (visaId != null && visaId == -1L) {
				List<CostRecord> visaList = costManageService.getVisaCost(groupId);
				for (CostRecord costRecord : visaList) {
					if(id.equals(costRecord.getId())) {
						CostRecord cr = costManageService.copyCost(costRecord);
						cr.setUuid(UuidUtils.generUuid());
						cr.setActivityId(groupId);
						cr.setOrderType(orderType);
						cr.setDelFlag("1");
						cr.setCreateBy(user);
						cr.setCreateDate(date);
						cr.setUpdateBy(user.getId());
						cr.setUpdateDate(date);
						cr.setVisaId(id);
						cr.setOverseas(0);
						Long reviewCompanyId=(long)0;
				 		Long payReviewCompanyId=(long)0;
				 		String deptIdStr = request.getParameter("deptId");
				 		Long companyId = user.getCompany().getId();
				 		Long userId = user.getId();
				 		List<Long> deptList=  costManageService.getDeptList(userId);
				 		Long deptId = 0L;
				 		if(StringUtils.isNotEmpty(deptIdStr)) {
				 			deptId = Long.parseLong(deptIdStr);
				 		}
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
				 		cr.setReviewCompanyId(reviewCompanyId);
				 		cr.setPayReviewCompanyId(payReviewCompanyId);
				 		cr.setPayReview(4);
				 		cr.setPayUpdateBy(Integer.parseInt(userId.toString()));
				 		cr.setPayUpdateDate(date);
						costManageService.saveCostRecord(cr);
					}
				}
			} else {
				costManageService.deleteCostRecord(id);
			}
	        
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}
	
	@ResponseBody
	@RequestMapping(value="cancel", method=RequestMethod.POST)
	public String cancel(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costManageService.cancelCostRecord(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
	}
	
	@ResponseBody
	@RequestMapping(value="cancelPay", method=RequestMethod.POST)
	@Deprecated
	public String cancelPay(@RequestParam(value="id") Long id, @RequestParam(value="type") String type){
			try {
	        costManageService.cancelPayCostRecord(id);
	        return "true";
        } catch (Exception e) {
        	LOG.error("error on delete cost", e);
        	return "false";
        }
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
	@RequestMapping(value="flow/start/{activityId}/{groupId}/{typeId}", method=RequestMethod.GET)
	public String showCurrent(@PathVariable(value="activityId") Long activityId, 
			@PathVariable(value="groupId") Long groupId,
			@PathVariable(value="typeId") Integer typeId,Model model,
			HttpServletRequest request, HttpServletResponse response){
		String from = request.getParameter("from");
		//获取产品信息
		TravelActivity travelActivity = travelActivityService.findById(activityId);
		Long deptId=travelActivity.getDeptId();
		if (deptId==null){
			deptId=(long)0;
		}
		//获取团期信息
		ActivityGroup activityGroup = activityGroupService.findById(groupId);
		String costSerial= activityGroup.getCost();   //团期的预计总成本
		String incomeSerial= activityGroup.getIncome();	//	团期预计总收入
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));//预计总毛利
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));  //预计总成本
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));//预计总收入
		Object budgetrefund = costManageService.getRefundSum(groupId, 0, typeId).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(groupId, 0, typeId).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costManageService.getRefundSum(groupId, 1, typeId).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(groupId, 1, typeId).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);
		model.addAttribute("deptId",deptId); 
		model.addAttribute("companyId",companyId);
		//是否计调职务
		model.addAttribute("isOperator",reviewService.checkJobType(3,4));
		//是否操作职务
		model.addAttribute("isOpt",reviewService.checkJobType(5));
		model.addAttribute("costLog",stockDao.findCostRecordLog(groupId, typeId));

		Dict dict=dictService.findByValueAndType( travelActivity.getActivityKind().toString(), "order_type");
		model.addAttribute("typename", dict.getLabel());
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
				model.addAttribute("costAutoPass",costAutoPass);
			}else{
				model.addAttribute("costAutoPass","0");
			}
		if(activityGroup != null && travelActivity != null && 
			activityGroup.getTravelActivity() != null && 
			activityGroup.getTravelActivity().getId().equals(travelActivity.getId())){
			
			Page<Map<Object, Object>> orderList = new Page<Map<Object, Object>>(request, response);
			orderList.setPageNo(1);
			orderList.setPageSize(Integer.MAX_VALUE);
			orderList = orderService.findOrderListByPayType(orderList, groupId); 
			
			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
			model.addAttribute("travelActivity", travelActivity);
			model.addAttribute("activityGroup", activityGroup);
			model.addAttribute("orderList", orderList.getList());
			model.addAttribute("type", from);
			model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
			List<CostRecord> budgetOutList = new ArrayList<CostRecord>();
			List<CostRecord> budgetInList = new ArrayList<CostRecord>();
			List<CostRecord> actualOutList = new ArrayList<CostRecord>();
			List<CostRecord> actualInList = new ArrayList<CostRecord>();
			List<CostRecord> otherCostList = new ArrayList<CostRecord>();
			
			List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);			
						
			model.addAttribute("typename", dict.getLabel());
			
//			int totalPersonNum = 0;
//			for(int i = 0; i < orderList.getCount(); i++) {
//				totalPersonNum += Integer.parseInt(orderList.getList().get(i).get("orderPersonNum").toString());
//			}
//			model.addAttribute("totalPersonNum", totalPersonNum);
			
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
			
			 List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(groupId,typeId);
			 List<Map<String, Object>> budgetCost=costManageService.getCost(groupId,typeId,0);
			 List<Map<String, Object>> actualCost=costManageService.getCost(groupId,typeId,1);
			 model.addAttribute("incomeList", incomeList);
			 model.addAttribute("budgetCost",budgetCost);
			 model.addAttribute("actualCost",actualCost);		 
			
			//境内成本预算			
			 budgetInList = costManageService.findCostRecordList(groupId, 0,0,typeId);//typeId 订单类型 2:散拼;6签证;7机票	
			 //境外成本预算
			budgetOutList = costManageService.findCostRecordList(groupId, 0,1,typeId);
			 
			//境内实际成本	
			actualInList = costManageService.findCostRecordList(groupId, 1,0,typeId);
			//境外实际成本	
			actualOutList = costManageService.findCostRecordList(groupId, 1,1,typeId);		
			
			// 其它
			otherCostList = costManageService.findCostRecordList(groupId, 2, 0, typeId);	// 第三个参数overseas默认设置为0
			
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())) {
				budgetInList = costManageService.getBudgetInList(budgetInList, groupId, typeId);
			}
			model.addAttribute("orderType", typeId);
			
			model.addAttribute("budgetInList", budgetInList);
			model.addAttribute("budgetOutList", budgetOutList);
				
			model.addAttribute("actualInList", actualInList);
			model.addAttribute("actualOutList", actualOutList);	
			
			DecimalFormat df = new DecimalFormat("#####0.00");
			for (int i = 0; i < otherCostList.size(); i++) {
				Object obj1 = costManageService.getPayedMoney(otherCostList.get(i).getId());
				String s1 = df.format(Double.parseDouble(obj1==null?"0":obj1.toString()));
				otherCostList.get(i).setPayedMoney(s1);
				Object obj2 = costManageService.getConfirmMoney(otherCostList.get(i).getId());
				String s2 = df.format(Double.parseDouble(obj2==null?"0":obj2.toString()));
				otherCostList.get(i).setConfirmMoney(s2);
			}
			model.addAttribute("otherCostList", otherCostList);
		}else{
			throw new RuntimeException("产品和团期不匹配");
		}
		return "modules/cost/start";
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
	 * @param groupId
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


	@RequestMapping(value="airTicketList")	
	public String airTicketPreRecordList(@ModelAttribute ActivityAirTicket activityAirTicket, 
			HttpServletRequest request, HttpServletResponse response, Model model){
		
		User user = UserUtils.getUser();
		Map<String, Object> params = new HashMap<String, Object>();
		
		String departureCity = request.getParameter("departureCity");
		String arrivedCity = request.getParameter("arrivedCity");
		String groupOpenDate=request.getParameter("groupOpenDate");
		String groupCloseDate=request.getParameter("groupCloseDate");
		String airType = request.getParameter("airType");
		String commitType = request.getParameter("commitType");
		String operator = request.getParameter("operator");
		String isReject = request.getParameter("isReject");	// 540 运控机票页面添加驳回标识 王洋 2017.3.22
		
		params.put("departureCity", departureCity);
		params.put("arrivedCity", arrivedCity);
		params.put("groupOpenDate", groupOpenDate);
		params.put("groupCloseDate", groupCloseDate);
		params.put("airType", airType);
		params.put("commitType", commitType);
		params.put("operator", operator);
		params.put("isReject", isReject);	// 540 运控机票页面添加驳回标识 王洋 2017.3.22
       
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		String departureCityPara = null;
		List<Dict> fromAreas = DictUtils.getDictList("from_area");
		if(StringUtils.isNotBlank(departureCity)){
			for(Dict dict:fromAreas){
				if(departureCity.equals(dict.getLabel())){
					departureCityPara=dict.getValue().toString();
				}
			}
		}
		
		String arrivedCityPara=null;
		if(StringUtils.isNotBlank(arrivedCity)){
			List<Area> areas = areaService.findAreaByName(arrivedCity);
			if(areas.size()>0) {
				arrivedCityPara = areas.get(0).getId().toString();
			}else{
				arrivedCityPara = "-1";
			}
		}
		params.put("departureCityPara", departureCityPara);
		params.put("arrivedCityPara", arrivedCityPara);
		Long companyId = user.getCompany().getId();
        Page<Map<String, Object>> page = activityAirTicketService.findActivityAirTicketReviewPage(new Page<Map<String, Object>>(request, response),
                activityAirTicket, params);
		//批发商uuid
		String companyUuid = user.getCompany().getUuid();
		//天马运通显示预计总成本、实际总成本
		if(Context.SUPPLIER_UUID_TMYT.equals(companyUuid)) {
			model.addAttribute("TMYT", true);
			for (Map<String, Object> airTicket : page.getList()) {
				Long id = Long.valueOf(airTicket.get("id").toString());
				List<Map<String, Object>> budgetCost=costManageService.getCost(id,7,0);
				List<Map<String, Object>> actualCost=costManageService.getCost(id,7,1);
				BigDecimal budgetTotal = OrderCommonUtil.getSum(budgetCost, "cost");
				BigDecimal actualTotal = OrderCommonUtil.getSum(actualCost, "cost");
				airTicket.put("budgetTotal",budgetTotal);
				airTicket.put("actualTotal",actualTotal);
			}
		}
		 // 鼎鸿假期添加筛选条件  for 0416 by jinxin.gao
	    if(Context.SUPPLIER_UUID_DHJQ.equals(companyUuid)){
			model.addAttribute("DHJQ", true);
	    }
		model.addAttribute("params", params);
        model.addAttribute("activityAirTicket", activityAirTicket);
        model.addAttribute("page", page);
		model.addAttribute("airportlist", airportService.queryAirport(companyId));
		model.addAttribute("traffic_namelist", DictUtils.getDict2List("traffic_name"));// 航空公司
		model.addAttribute("airspacelist", DictUtils.getDict2List("airspace_Type"));// 舱位
		return "modules/cost/airTicketRecordList";
	}
	
	@Autowired
	IAirTicketOrderService  iAirTicketOrderService;
	@Autowired
	private OfficeService officeService;
	
	@RequestMapping(value="airTicketPreRecord/{airTicketId}")
	public String preRecord(Model model,@PathVariable Long airTicketId){
		model.addAttribute("airTypes", DictUtils.getSysDicMap("air_Type"));// 机票类型
		model.addAttribute("traffic_namelist", DictUtils.getDictList("traffic_name"));// 航空公司
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		
		model.addAttribute("from_area", DictUtils.getDictList("from_area"));// 出发城市		
		model.addAttribute("areas", areaService.findAirportCityList(""));//到达城市
		ActivityAirTicket airTicket=activityAirTicketService.getActivityAirTicketById(airTicketId);		
		Long deptId=airTicket.getDeptId();
		if (deptId==null){
			deptId=(long)0;
		}
        model.addAttribute("deptId",deptId);
		String costSerial= airTicket.getCost();
		String incomeSerial= airTicket.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(airTicketId,7);
		
		List<Map<String, Object>> budgetCost=costManageService.getCost(airTicketId,7,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(airTicketId,7,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		Long companyId= UserUtils.getUser().getCompany().getId();
		model.addAttribute("companyId",companyId);
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		//是否计调职务
		model.addAttribute("isOperator",reviewService.checkJobType(3,4));
		//是否操作职务
        model.addAttribute("isOpt",reviewService.checkJobType(5));
		Integer costAutoPass=UserUtils.getUser().getCompany().getCostAutoPass();
		if( costAutoPass!=null){
				model.addAttribute("costAutoPass",costAutoPass);
		}else{
				model.addAttribute("costAutoPass","0");
		}
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
		
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));
		
		Object budgetrefund = costManageService.getRefundSum(airTicketId, 0, 7).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(airTicketId, 0, 7).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costManageService.getRefundSum(airTicketId, 1, 7).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(airTicketId, 1, 7).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);
		
		model.addAttribute("activityAirTicket",airTicket);
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
        
        model.addAttribute("airportlist", airportService.queryAirport(companyId));
		
        List<Map<String,Object>> airticketOrders =iAirTicketOrderService.queryAirticketOrdersByProductId(airTicketId.toString());
		
        //计算机票剩余时间
		for(Map<String, Object> temp : airticketOrders){
			com.trekiz.admin.modules.airticketorder.web.AirTicketOrderController.getOrderLeftTime(temp);			
		} 
		
        model.addAttribute("airTicketOrderList",airticketOrders );
		List<Office> officeList = officeService.findSyncOffice();
		model.addAttribute("companyList", officeList);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		
//		int totalPersonNum = 0;
//		for(int i = 0; i < airticketOrders.size(); i++) {
//			totalPersonNum += Integer.parseInt(airticketOrders.get(i).get("personNum").toString());
//		}
//		model.addAttribute("totalPersonNum", totalPersonNum);
		
		List<CostRecord> budgetOutList = new ArrayList<CostRecord>();
		List<CostRecord> budgetInList = new ArrayList<CostRecord>();
		List<CostRecord> actualOutList = new ArrayList<CostRecord>();
		List<CostRecord> actualInList = new ArrayList<CostRecord>();
		List<CostRecord> otherCostList = new ArrayList<CostRecord>();


        budgetInList = costManageService.findCostRecordList(airTicketId, 0,0,Context.ORDER_TYPE_JP);
		budgetOutList = costManageService.findCostRecordList(airTicketId, 0,1,Context.ORDER_TYPE_JP);
		actualInList = costManageService.findCostRecordList(airTicketId, 1,0,Context.ORDER_TYPE_JP);
		actualOutList = costManageService.findCostRecordList(airTicketId, 1,1,Context.ORDER_TYPE_JP);
		otherCostList = costManageService.findCostRecordList(airTicketId, 2, 0, Context.ORDER_TYPE_JP);	// 第三个参数overseas默认设置为0
		
		model.addAttribute("budgetInList", budgetInList);
		model.addAttribute("budgetOutList", budgetOutList);
		
		model.addAttribute("actualInList", actualInList);
		model.addAttribute("actualOutList", actualOutList);
		
		DecimalFormat df = new DecimalFormat("#####0.00");
		for (int i = 0; i < otherCostList.size(); i++) {
			Object obj1 = costManageService.getPayedMoney(otherCostList.get(i).getId());
			String s1 = df.format(Double.parseDouble(obj1==null?"0":obj1.toString()));
			otherCostList.get(i).setPayedMoney(s1);
			Object obj2 = costManageService.getConfirmMoney(otherCostList.get(i).getId());
			String s2 = df.format(Double.parseDouble(obj2==null?"0":obj2.toString()));
			otherCostList.get(i).setConfirmMoney(s2);
		}
		model.addAttribute("otherCostList", otherCostList);
		
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		model.addAttribute("costLog",stockDao.findCostRecordLog(airTicketId, 7));
		
		return "modules/cost/airTicketPreRecord";
	}
	
	//提交成本录入
	@ResponseBody
	@RequestMapping(value="submitair", method=RequestMethod.POST)
	public String submitair(@RequestParam Long id){
		costRecordDao.submitCostRecord(id,7,4);
		return "[{\"result\":\"ok\"}]";
	}
	
	
    //保存机票录入
	@ResponseBody
	@RequestMapping(value="saveticket", method=RequestMethod.POST)
	public String saveticket(@RequestParam String name, @RequestParam BigDecimal price,@RequestParam String comment,
    @RequestParam Long activityId, @RequestParam Integer quantity,@RequestParam Integer currencyId,    
    @RequestParam Integer overseas,@RequestParam Integer supplyType,@RequestParam Integer supplyId,        
    @RequestParam String supplyName,@RequestParam Integer budgetType){	
	    CostRecord costRecord= new CostRecord();
		costRecord.setName(name);		
		costRecord.setPrice(price);
		costRecord.setQuantity(quantity);
		costRecord.setSupplyId(supplyId);
		costRecord.setSupplyType(supplyType);
		costRecord.setOrderType(Context.ORDER_TYPE_JP);//订单类型 2:散拼;6签证;7机票
		costRecord.setPayStatus(0);
		costRecord.setReview(Context.REVIEW_COST_WAIT);
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
	
	/**
	 * 查看对应产品或者团期的预报单数据信息
	 * @param activityIdOrUUID     产品或者团期ID或者UUID
	 * @param orderType            订单类型
	 * @author shijun.liu
	 */
	@RequestMapping(value="forcastList/{activityIdOrUUID}/{orderType}", method=RequestMethod.GET)
	public String forcastList(@PathVariable(value="activityIdOrUUID") String activityIdOrUUID, 
			@PathVariable(value="orderType") Integer orderType, Model model){
		Long activityId = null;
		String groupUUID = null;
		String returnURL = "modules/cost/newforcastlist";
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(orderType < 11){
			activityId = Long.valueOf(activityIdOrUUID);
			model.addAttribute("activityIdOrUUID", activityId);
		}else{//海岛游、酒店使用的是UUID
			groupUUID = activityIdOrUUID;
			model.addAttribute("activityIdOrUUID", groupUUID);
		}
		CostRecordVO cRecordVO = null;
		try{
			cRecordVO = costManageService.getForecastDataInfo(activityId, orderType, groupUUID);
		}catch(Exception e){
			e.printStackTrace();
		}
		//拉美途境内预算成本还要添加签证费信息，对应需求C310
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)
				&& Context.ORDER_TYPE_YX != orderType){
			handleActualInForLMT(cRecordVO,activityId,orderType); //把原有代码，提取为一个方法了。yudong.xu
			returnURL = "modules/cost/newforcastlist_for_lameitour";
		}
		//拉美图的团期产品预算单，改动较大，返回新的视图。对应需求 0396
		if (ActivityGroupService.isLMTTuanQi(orderType)){
			returnURL = "modules/cost/newforcastlist_for_lameitour_tuanqi";
		}
		Map<String, Object> resultMap = null;
		Object lockStatus = cRecordVO.getBaseinfo().get("forcastStatus");
		if(null == lockStatus || "".equals(lockStatus)){
			lockStatus = "00";
		}
		
		//获取预报单的结果map。 update by yudong.xu 2016.5.13
		try {
			resultMap = getForecastMap(lockStatus.toString(),orderType,activityId,groupUUID,cRecordVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 骡子假期
		if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)) {
			if ("10".equals(lockStatus.toString()) && "1".equals(resultMap.get("history").toString())) {
				returnURL = "modules/cost/newforcastlist";
			} else {
				returnURL = "modules/cost/newforcastlist_for_lzjq";
			}
		}
		// 0581 需求 将拉美图的操作人写死为陈卡卡   add by gaoyang 20170321
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())) {
			if (resultMap != null) {
				resultMap.put("createByLeader", "陈卡卡");
				resultMap.put("createByLeaderFull", "陈卡卡");
			}
		}
		model.addAttribute("vo", resultMap);
		model.addAttribute("forcastStatus", lockStatus);
		model.addAttribute("orderType", orderType);
		model.addAttribute("companyUuid",companyUuid);
		return returnURL;
	}

	/**
	 * 针对拉美图的境内付款进行处理，提取为一个方法。供预算单和预算单上锁进行方便调用。
	 * 拉美途境内预算成本还要添加签证费信息,对应需求C310
	 * @author yudong.xu --2016/4/29--11:56
	 */
	private void handleActualInForLMT(CostRecordVO cRecordVO,Long activityId,Integer orderType){
		List<CostRecord> actualInList = cRecordVO.getActualInList();
		//删除cost_record表中已经存在的签证子订单数据
		if(null != actualInList){
			List<CostRecord> needDel = new ArrayList<CostRecord>();
			for (CostRecord costRecord:actualInList) {
				if(null != costRecord.getVisaId()){
					needDel.add(costRecord);
				}
			}
			actualInList.removeAll(needDel);
		}
		costManageService.getBudgetInList(actualInList, activityId, orderType);
		cRecordVO.setActualInList(actualInList);
	}
	
	/**
	 * 返佣付款-打印（请款单）
	 * @param rid
	 * @param prdType
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="returnMoneyList/{rid}/{prdType}")
	public String returnMoneyList(@PathVariable(value = "rid") Long rid,
			@PathVariable(value = "prdType") Integer prdType, Model model,
			HttpServletRequest request, HttpServletResponse response){
		if(Context.ORDER_TYPE_HOTEL == prdType || Context.ORDER_TYPE_ISLAND == prdType) {
			Map<String, Object> data = costManageService.getRebates4HotelIsland(rid.intValue(), prdType);
			model.addAttribute("data", data);
			return "modules/cost/returnMoneyList";
		}else{
			return "";
		}
			
	}
	/**
	 * 借款付款-打印（请款单）
	 * @param rid
	 * @param prdType
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="returnMoneyList4JK/{rid}/{prdType}")
	public String returnMoneyList4JK(@PathVariable(value = "rid") Long rid,
			@PathVariable(value = "prdType") Integer prdType, Model model,
			HttpServletRequest request, HttpServletResponse response){
		if(Context.ORDER_TYPE_HOTEL == prdType || Context.ORDER_TYPE_ISLAND == prdType) {
			Map<String, Object> data = costManageService.getJKRebates4HotelORIsland(rid.intValue(), prdType);
			model.addAttribute("data", data);
			return "modules/cost/returnMoneyList";
		}else{
			return "";
		}
	}

	/**
	 * 查看对应产品或者团期的结算单数据信息
	 * @param activityIdOrUUID     产品或者团期ID或者UUID
	 * @param orderType            订单类型
	 * @author shijun.liu
	 */
	@RequestMapping(value="settleList/{activityIdOrUUID}/{orderType}", method=RequestMethod.GET)
	public String settleList(@PathVariable(value="activityIdOrUUID") String activityIdOrUUID, 
			@PathVariable(value="orderType") Integer orderType, Model model){
		Long activityId = null;
		String groupUUID = null;
		String returnURL = "modules/cost/newsettleList";
		if(orderType < 11){
			activityId = Long.valueOf(activityIdOrUUID);
			model.addAttribute("activityIdOrUUID", activityId);
		}else{//海岛游、酒店使用的是UUID
			groupUUID = activityIdOrUUID;
			model.addAttribute("activityIdOrUUID", groupUUID);
		}
		Map<String, Object> resultMap = null;
		resultMap = settleService.getSettleMap(orderType, activityId, groupUUID);
		// 0581 需求 将拉美图的操作人写死为陈卡卡       add by gaoyang 20170321
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())) {
			if (resultMap != null) {
				resultMap.put("createByLeader", "陈卡卡");
				resultMap.put("createByLeaderFull", "陈卡卡");
			}
		}
		model.addAttribute("vo", resultMap);
		model.addAttribute("lockStatus", resultMap.get("lockStatus"));
		model.addAttribute("orderType", orderType);

		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		model.addAttribute("isLaMeiTu",Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid));
		model.addAttribute("isYYJQ", Context.SUPPLIER_UUID_YYJQ.equals(companyUuid));
		model.addAttribute("isYJXZ", Context.SUPPLIER_UUID_YJXZ.equals(companyUuid));
		model.addAttribute("isHQX", Context.SUPPLIER_UUID_HQX.equals(companyUuid));
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && Context.ORDER_TYPE_YX != orderType){
			returnURL = "modules/cost/newsettleList_for_lameitour";
		}else if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)){
			Integer version = (Integer) resultMap.get("version");
			if (version == null || version == 2){
				// 为null表示查询出来的，不为null表示从表中查出来的，为2表示是改动后才锁定的。目的就是过滤掉改动前已经锁定的数据。
				returnURL = "modules/cost/newsettleList_for_lzjq";
			}
		}
		return returnURL;
	}
	
   /**
	* 
	* @Title: locker
	* @Description:(锁定/解锁结算单)
	* @param @param request
	* @return Object    返回类型
	* @throws
	*/
	@RequestMapping(value ="locker")
	@ResponseBody
	public void locker(HttpServletRequest request, HttpServletResponse response){
		String json ="{\"flag\":\"success\"}";
		boolean isSuccess = true;//操作成功标志位
		String activityIdOrUUID = request.getParameter("activityIdOrUUID");
		String orderType = request.getParameter("orderType");
		String lockStatus = request.getParameter("lockStatus");
		//0表示预报单，1表示结算单
		String budgetType = request.getParameter("budgetType");
		Long activityId = null;
		String groupUUID = null;
		Integer type = Integer.parseInt(orderType);
		Integer budgetTypeVal = Integer.parseInt(budgetType);
		if(StringUtils.isNotBlank(orderType) && StringUtils.isNotBlank(budgetType)){
			if(type < 11){
				activityId = Long.valueOf(activityIdOrUUID);
			}else{//海岛游、酒店使用的是UUID
				groupUUID = activityIdOrUUID;
			}
			try{
			    costManageService.lock(budgetTypeVal, activityId, type, lockStatus, groupUUID);
			} catch (Exception e) {
				json ="{\"flag\":\"fail\",\"msg\":\""+e.getMessage()+"\"}";
				isSuccess = false;
				e.printStackTrace();
			}
		}else{
			json ="{\"flag\":\"fail\",\"msg\":\"订单类型和成本类型不能为空\"}";
			isSuccess = false;
		}
		//对锁定成功的状态，保存查询数据到表中。yudong.xu
		if (isSuccess){
			try {
				handleLocker(budgetTypeVal,lockStatus,type,activityId,groupUUID);
			} catch (Exception e) {
				json ="{\"flag\":\"fail\",\"msg\":\"快照数据保存失败！\"}";
				e.printStackTrace();
			}
		}
		ServletUtil.print(response, json);
	}
	
	@RequestMapping(value ="canLock")
	public void canLock(HttpServletRequest request, HttpServletResponse response){
		String json ="{\"flag\":\"success\"}";
		String activityIdOrUUID = request.getParameter("activityIdOrUUID");
	    String orderType = request.getParameter("orderType");
	    Long activityId = null;
		String groupUUID = null;
	    if(StringUtils.isNotBlank(orderType) && StringUtils.isNotBlank(activityIdOrUUID)){
	    	Integer type = Integer.parseInt(orderType);
			if(type < 11){
				activityId = Long.valueOf(activityIdOrUUID);
			}else{//海岛游、酒店使用的是UUID
				groupUUID = activityIdOrUUID;
			}
	    	boolean b = costManageService.isAuditing(type, activityId, groupUUID);
	    	if(b){
	    		json = "{\"flag\":\"fail\",\"msg\":\"现有正处于审批中的数据，确定要锁定吗？\"}";
	    	}
	    }
		ServletUtil.print(response, json);
	}
	
	/*
	 * 保存备注内容
	 */
	@RequestMapping(value="saveRemark")
	@ResponseBody
	public void saveRemark(HttpServletRequest request, HttpServletResponse response) {
		String json = "{\"flag\":\"success\"}";
		
		String activityIdOrUUID = request.getParameter("activityIdOrUUID");
	    String orderType = request.getParameter("orderType");
	    String remark = request.getParameter("remark");
	    String option = request.getParameter("option");

		Integer type = Integer.parseInt(orderType);		
		//判断预算表或者结算表中是否存在记录，若存在则更新备注，若不存在，则插入备注
		if("forecast".equals(option)) {
			ForeCast simpleForeCast = foreCastService.getSimpleForeCast(type, activityIdOrUUID);
			if(null != simpleForeCast) {		//更新
				logger.debug("update forecast remark begin");
				simpleForeCast.setRemark(remark);
				foreCastService.updateForeCast(simpleForeCast);
				logger.debug("update forecast remark end");
			}else {		//保存
				logger.debug("save forecast remark begin");
				ForeCast foreCast = new ForeCast();
				foreCast.setUuid(UuidUtils.generUuid());
				foreCast.setGroupIdUuid(activityIdOrUUID);
				foreCast.setOrderType(type);
				foreCast.setRemark(remark);
				foreCastService.saveForeCast(foreCast);
				logger.debug("save forecast remark end");
			}
		}else if("settle".equals(option)) {
			Settle simpleSettle = settleService.getSimpleSettleObj(type, activityIdOrUUID);
			if(null != simpleSettle) {		//更新
				logger.debug("update settle remark begin");
				simpleSettle.setRemark(remark);
				settleService.updateSettle(simpleSettle);
				logger.debug("update settle remark end");
			}else {	//保存
				logger.debug("save settle remark begin");
				Settle settle = new Settle();
				settle.setUuid(UuidUtils.generUuid());
				settle.setOrderType(type);
				settle.setGroupIdUuid(activityIdOrUUID);
				settle.setRemark(remark);
				settleService.saveSettle(settle);
				logger.debug("save settle remark end");
			}
		}
		
		ServletUtil.print(response, json);
	}
	
	/**
	 * 预报单下载 
	 * @author shijun.liu
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="downLoadForcastList" , method=RequestMethod.GET)
	public void downLoadForecast(HttpServletRequest request, HttpServletResponse response, Model model){
		String activityIdOrUUID = request.getParameter("activityId");
		String orderTypeStr = request.getParameter("orderType");
		Integer orderType = null;
		Long activityId = null;
		String groupUUID = null;
		if(null != orderTypeStr){
			orderType = Integer.parseInt(orderTypeStr);
		}
		if(orderType < 11){
			activityId = Long.valueOf(activityIdOrUUID);
		}else{//海岛游、酒店使用的是UUID
			groupUUID = activityIdOrUUID;
		}

		CostRecordVO costRecordVO = costManageService.getForecastDataInfo(activityId, orderType, groupUUID);
		//拉美途境内预算成本还要添加签证费信息，对应需求C310
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())
				&& Context.ORDER_TYPE_YX != orderType){
			List<CostRecord> actualInList = costRecordVO.getActualInList();
			//删除cost_record表中已经存在的签证子订单数据
			if(null != actualInList){
				List<CostRecord> needDel = new ArrayList<CostRecord>();
				for (CostRecord costRecord:actualInList) {
					if(null != costRecord.getVisaId()){
						needDel.add(costRecord);
					}
				}
				actualInList.removeAll(needDel);
			}
			costManageService.getBudgetInList(actualInList, activityId, orderType);
			costRecordVO.setActualInList(actualInList);
		}
		Map<String, Object> resultMap = null;
		Object lockStatus = costRecordVO.getBaseinfo().get("forcastStatus");
		if(null == lockStatus || "".equals(lockStatus)){
			lockStatus = "00";
		}
		//获取结果集。 yudong.xu 2016.5.13
		try {
			resultMap = getForecastMap(lockStatus.toString(),orderType,activityId,groupUUID,costRecordVO);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		File file = null;
		String ftlName = "yubaodan.ftl";
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())) {
			ftlName = "yubaodan_LMT.ftl";
		}		
		if (ActivityGroupService.isLMTTuanQi(orderType)){
			ftlName = "yubaodan_LMT_TuanQi.ftl";
		}	
		if (Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())) {
			ftlName = "yubaodan_lzjq.ftl";
		}
		
		try {
			file = FreeMarkerUtil.generateFile(ftlName, "forcastList.doc",resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	    String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
	    String fileName = new StringBuilder().append("预报单").append(nowDate).append(".doc").toString();
	    WordDownLoadUtils.downLoadWordByAttachment(file, fileName, response);
	}
	
	/**
	 * 结算单下载 
	 * @author shijun.liu
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="downLoadSettletList" , method=RequestMethod.GET)
	public void downLoadSettletList(HttpServletRequest request, HttpServletResponse response, Model model){
		String activityIdOrUUID = request.getParameter("activityId");
		String orderTypeStr = request.getParameter("orderType");
		Integer orderType = null;
		Long activityId = null;
		String groupUUID = null;
		if(null != orderTypeStr){
			orderType = Integer.parseInt(orderTypeStr);
		}
		if(orderType < 11){
			activityId = Long.valueOf(activityIdOrUUID);
		}else{//海岛游、酒店使用的是UUID
			groupUUID = activityIdOrUUID;
		}
		CostRecordVO costRecordVO = costManageService.getSettleDataInfo(activityId, orderType, groupUUID);

		Object lockStatus = costRecordVO.getBaseinfo().get("lockStatus");
		if(null == lockStatus || "".equals(lockStatus)){
			lockStatus = 0;
		}

		String ftlName = null;
		String companyUuid = UserUtils.getCompanyUuid();
		if (Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)){
			ftlName = "jiesuandannew.ftl";
		} else {
			ftlName = "jiesuandan.ftl";
		}

		Map<String, Object> resultMap = null;
		File file = null;
		try {
			//获取结算单结果map。 yudong.xu 2016.5.13
			resultMap = getSettleMap(lockStatus.toString(),orderType,activityId,groupUUID,costRecordVO);
			if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)){
				Integer version = (Integer)resultMap.get("version");
				if (version == null || version == 2){
					ftlName = "jiesuandan_LZJQ.ftl";
				}
			}
			file = FreeMarkerUtil.generateFile(ftlName, "settleList.doc",resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

	    String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
	    String fileName = new StringBuilder().append("结算单").append(nowDate).append(".doc").toString();
	    WordDownLoadUtils.downLoadWordByAttachment(file, fileName, response);
	}
	/**
	 * 结算单批量下载 
	 * @author shijun.liu
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="batchDownLoadSettletList" , method=RequestMethod.GET)
	public void batchDownLoadSettletList(HttpServletRequest request, HttpServletResponse response, Model model){
		String activityIds = request.getParameter("activityIds");
		String orderTypes = request.getParameter("orderTypes");
		String groupCodes = (null==request.getParameter("groupCodes")) ? "" : request.getParameter("groupCodes");
		try {
			groupCodes = new String(groupCodes.getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//获取产品id
		if (StringUtils.isNotBlank(activityIds) && StringUtils.isNotBlank(orderTypes)) {
			String[] aIds = activityIds.substring(0,activityIds.length()-1).split(",");
			String[] gcs = groupCodes.substring(0,groupCodes.length()-1).split(",");
			//获取订单类型
		
			String[] ots = orderTypes.substring(0,orderTypes.length()-1).split(",");
			
			if(aIds.length != 1){
				File zipFile = null;
				InputStream fis = null;
				OutputStream out = null;
				File file = null;
				List<File> fileList = new ArrayList<File>();
				try {
					for (int i = 0; i < aIds.length; i++) {
						/*orderType = Integer.parseInt(ots[i]);
						
						if(orderType < 11){
							activityId = Long.valueOf(aIds[i]);
						}else{//海岛游、酒店使用的是UUID
							groupUUID = aIds[i];
						}
						CostRecordVO costRecordVO = costManageService.getSettleDataInfo(activityId, orderType, groupUUID);
						
						Object lockStatus = costRecordVO.getBaseinfo().get("lockStatus");
						if(null == lockStatus || "".equals(lockStatus)){
							lockStatus = 0;
						}
						String ftlName = "jiesuandannew.ftl";
						
						Map<String, Object> resultMap = null;
						File file = null;
			
						//获取结算单结果map。 yudong.xu 2016.5.13
						resultMap = getSettleMap(lockStatus.toString(),orderType,activityId,groupUUID,costRecordVO);
						groupCode = gcs[i];
						String docName = "计算单" + groupCode + ".doc";
						file = FreeMarkerUtil.generateFile(ftlName, docName,resultMap);*/
						file = getFile(aIds[i], ots[i], gcs[i]);
						fileList.add(file);
						
					}
					HttpSession session = request.getSession();      
		        	ServletContext application = session.getServletContext();    
		        	String serverRealPath = application.getRealPath("/") ;
		        	String nowDate = DateUtils.formatCustomDate(new Date(), "yyyyMMdd");
		        	String path = serverRealPath + "结算单" + nowDate  + ".zip";
		        	zipFile = new File(path);
		        	ZipUtils.zipFileList(fileList, zipFile);
	        	
		            if (!zipFile.exists()){   
		            	zipFile.createNewFile();   
		            }
	           
	                // 以流的形式下载文件。
	                fis = new BufferedInputStream(new FileInputStream(zipFile.getPath()));
	                byte[] buffer = new byte[fis.available()];
	                fis.read(buffer);
	                
	                // 清空response
	                response.reset();

	                out = new BufferedOutputStream(response.getOutputStream());
	                response.setContentType("application/octet-stream");
	                response.setHeader("Content-Disposition", "attachment;filename=" +new String(zipFile.getName().getBytes("gb2312"), "ISO-8859-1") );
	                out.write(buffer);
	                out.flush();
	                out.close();
	                } catch (Exception e) {
	                	
	                	e.printStackTrace();
	                	
	                }finally{
	                     try {
	                    	 if(fis != null){
	                    		 fis.close();
	                    	 }
	                    	 if(out != null){
	                    		 out.close();
	                    	 }
	                            File f = new File(zipFile.getPath());
	                            f.delete();
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                }
	                //return response;
				
			}else{
				
				File file = null;
				try {
					//获取需要下载的结算单
					file = getFile(aIds[0], ots[0], gcs[0]);
					
					String fileName = new StringBuilder().append("结算单").append(gcs[0]).append(".doc").toString();
					WordDownLoadUtils.downLoadWordByAttachment(file, fileName, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 查询并封装信息到file
	 * @param activityIdStr
	 * @param orderTypeStr
	 * @param groupCode
	 * @return file
	 * @throws Exception
	 */
	private File getFile(String activityIdStr, String orderTypeStr, String groupCode) throws Exception{
		
		Integer orderType = Integer.parseInt(orderTypeStr);
		Long activityId = null;
		String groupUUID = null;
		if(orderType < 11){
			activityId = Long.valueOf(activityIdStr);
		}
		CostRecordVO costRecordVO = costManageService.getSettleDataInfo(activityId, orderType, groupUUID);
		
		Object lockStatus = costRecordVO.getBaseinfo().get("lockStatus");
		if(null == lockStatus || "".equals(lockStatus)){
			lockStatus = 0;
		}
		String ftlName = "jiesuandannew.ftl";
		
		Map<String, Object> resultMap = null;
		File file = null;

		//获取结算单结果map。 yudong.xu 2016.5.13
		resultMap = getSettleMap(lockStatus.toString(),orderType,activityId,groupUUID,costRecordVO);
		
		String docName = "结算单" + groupCode + ".doc";
		file = FreeMarkerUtil.generateFile(ftlName, docName,resultMap);
		return file;
	}
	
	/**
	 * 将CostRecordVO转换为Map对象
	 * @author shijun.liu
	 * @param costRecordVO CostRecordVO对象
	 * @param orderType  订单类型
	 * @param type       0:预报单，1：结算单
	 * @return
	 */
	private Map<String, Object> castCostRecordVO2Map(CostRecordVO costRecordVO, Integer orderType, Integer type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		//产品信息
		getProductData(map, costRecordVO, orderType);
		//预报单-->预计收款;结算单-->收款明细
		payDetailData(map, costRecordVO, orderType);
		//预计收款-合计
		map.put("totalMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("totalSum").toString(), 2));
		//预计退款-合计
		map.put("backMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("backSum").toString(), 2));
		//实际收款-合计
		map.put("accountedMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("accountedSum").toString(),2));
		//未收款项-合计
		map.put("notAccountedMoneySum", MoneyNumberFormat.getThousandsByRegex(map.get("notAcccountedSum").toString(),2));
		
		//预计境内付款/境内支出
		BigDecimal inMoneySum = actualInData(map, costRecordVO, orderType);
		// 骡子假期 未加入返佣的支出合计  wangyang 2016.11.15
		BigDecimal inMoneySumWithoutFY = new BigDecimal(inMoneySum.toString());
		//返佣数据处理
		if (type == 0 && ActivityGroupService.isLMTTuanQi(orderType)){
			//如果是拉美图的预报单，不需要进行返佣处理。针对拉美图0396需求。yudong.xu
		}else {
			inMoneySum = commissionData(map, costRecordVO, inMoneySum);
		}
		//预计境内付款/境内支出 --> 合计
		map.put("expectedInMoneySum",MoneyNumberFormat.getThousandsByRegex(inMoneySum.toString(), 2));
		//预计境外付款/境外支出
		BigDecimal outMoneySum = actualOutData(map, costRecordVO);
		//预计境外付款/境外支出 --> 合计
		//0546 骡子假期 预算支出-应付合计  wangyang 2016.11.15
		BigDecimal totalExpenditureMoneySum = outMoneySum.add(inMoneySumWithoutFY);
		map.put("totalExpenditureMoneySum", MoneyNumberFormat.getThousandsByRegex(totalExpenditureMoneySum.toString(), 2));
		map.put("expectedOutMoneySum", MoneyNumberFormat.getThousandsByRegex(outMoneySum.toString(), 2));
		//骡子假期 团队退款总额
		if (Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())) {
			if(costRecordVO.getGroupRefundSum() !=  null && costRecordVO.getGroupRefundSum() != new BigDecimal("0.00")){
				map.put("groupRefundSum", "团队退款：￥"+costRecordVO.getGroupRefundSum() );
			}else{
				map.put("groupRefundSum", "");
			}
			//应收合计中减去退款总额
			if(map.get("backSum")!=null){
				BigDecimal totalMoneySum = new BigDecimal(map.get("totalSum").toString()).subtract(new BigDecimal(map.get("backSum").toString()));
				map.put("totalMoneySum", MoneyNumberFormat.getThousandsByRegex(totalMoneySum.toString(), 2));
			}
			map.put("otherRecordList", costRecordVO.getOtherRecordList());
		}
		//支出合计
		BigDecimal outSumMoney = outMoneySum.add(inMoneySum);
		//预计实际收入
		BigDecimal realMoney = new BigDecimal(map.get("totalSum").toString())
								.subtract(new BigDecimal(map.get("backSum").toString()));
		if (Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())) {
			realMoney = realMoney.add(new BigDecimal(map.get("otherSum").toString()));
		}
		if (type == 0 && ActivityGroupService.isLMTTuanQi(orderType)){
			//如果是拉美图的团期。
			String adultPriceOfLMTStr = map.get("adultPrice").toString().replace(",","");
			String preIncomeOfLMTStr = map.get("receiveMoney").toString().replace(",","");//预计收入
			String planPositionStr = map.get("planPosition").toString();//预收人数
			map.put("price", MoneyNumberFormat.getThousandsByRegex(adultPriceOfLMTStr, 2));//拉美图单价
			map.put("orderPersonNum", planPositionStr);//拉美图人数取预收人数
			//拉美图团期：预计收款=预计收入合计=(单价*预收人数)
			realMoney = new BigDecimal(preIncomeOfLMTStr);
		}
		//预计实际收入或者预计收款
		map.put("realMoneySum", MoneyNumberFormat.getThousandsByRegex(realMoney.toString(), 2));
		//0258 懿洋假期 税款=实际收入X发票税  保留两位小数  王洋   2016.3.31
		if(Context.SUPPLIER_UUID_YYJQ.equals(UserUtils.getUser().getCompany().getUuid())){
			
			BigDecimal taxRate = new BigDecimal(map.get("invoiceTax").toString()).divide(new BigDecimal(100));
			BigDecimal invoiceMoney = new BigDecimal(MoneyNumberFormat.getRoundMoney(
					realMoney.multiply(taxRate), 2, BigDecimal.ROUND_HALF_UP));
			
			map.put("invoiceMoney", MoneyNumberFormat.getThousandsByRegex(invoiceMoney.toString(), 2));

			BigDecimal profitAfterTax = realMoney.subtract(outSumMoney).subtract(
					new BigDecimal(MoneyNumberFormat.getRoundMoney(invoiceMoney, 2, BigDecimal.ROUND_HALF_UP)));
			map.put("profitAfterTax", MoneyNumberFormat.getThousandsByRegex(profitAfterTax.toString(), 2));
		}
		
		 //预计毛利
		BigDecimal profitSum = realMoney.subtract(outSumMoney);
		//预计支出合计
		map.put("outMoneySum", MoneyNumberFormat.getThousandsByRegex(outSumMoney.toString(), 2));
		//预计毛利合计
		map.put("profitSum", MoneyNumberFormat.getThousandsByRegex(profitSum.toString(), 2));
		
		/**
		 * 拉美图，结算单需求，需求号：256 by jinxin.gao
		 * 	删除收入合计与退款合计两项；
		 * 		实际收入：原取值为实际收入=收入合计—退款合计，
		 * 	                                 现改为：实际收入＝实际收款；
		 * 		支出合计=所有支出+退款合计
		 * 		毛利：原取值为毛利=实际收入—支出合计；现改为，毛利＝实际收入-支出合计（包含退款合计）
		 */
		if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())  && type == 1){
			//实际收入
			realMoney = new BigDecimal(map.get("accountedSum").toString());
			map.put("realMoneySum", MoneyNumberFormat.getThousandsByRegex(realMoney.toString(), 2));
			//支出合计
			outSumMoney = outMoneySum.add(inMoneySum).add(new BigDecimal(map.get("backSum").toString()));
			map.put("outMoneySum", MoneyNumberFormat.getThousandsByRegex(outSumMoney.toString(), 2));
			//毛利
			profitSum = realMoney.subtract(outSumMoney);
			map.put("profitSum", MoneyNumberFormat.getThousandsByRegex(profitSum.toString(), 2));
		}
		BigDecimal profitRate = new BigDecimal("0");
		if(Double.valueOf(realMoney.toString()) != 0){
			BigDecimal tempValue = new BigDecimal(MoneyNumberFormat.getRoundMoney(Double.valueOf(realMoney.toString()),
					MoneyNumberFormat.POINT_TWO));
			profitRate = profitSum.divide(tempValue, 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));//毛利率
		}
		String rate  = profitRate.toString();
		if(rate.toString().equals("0")){
			rate = "";
		}
		if(rate.startsWith(".")){
			rate += "0";
		}
		if(!"".equals(rate)){
			rate = MoneyNumberFormat.getFormatRate(Double.valueOf(rate.toString()),
					MoneyNumberFormat.POINT_THREE);
			rate = rate + "%";
		}
		map.put("profitRate", rate);//预计毛利率
		
		return map;
	}
	
	/**
	 * 处理产品信息
	 * @param map
	 * @param costRecordVO
	 * @param orderType
	 * @author shijun.liu
	 * @throws ParseException 
	 */
	private void getProductData(Map<String, Object> map, CostRecordVO costRecordVO, Integer orderType) throws ParseException{
		Map<String, Object> baseData = costRecordVO.getBaseinfo();
		String createBy = baseData.get("createBy")== null?"":baseData.get("createBy").toString();//操作
		String productName = baseData.get("productName") == null?"":baseData.get("productName").toString();//线路
		String groupCode = baseData.get("groupCode")==null?"":baseData.get("groupCode").toString();//团期
		String orderPersonNumSum = baseData.get("orderPersonNumSum")==null?"0":baseData.get("orderPersonNumSum").toString();//人数
		String groupOpenDate = baseData.get("groupOpenDate")==null?"":baseData.get("groupOpenDate").toString();//日期
		String groupCloseDate = baseData.get("groupCloseDate")==null?"":baseData.get("groupCloseDate").toString();//日期
		String activityDuration = baseData.get("activityDuration")==null?"":baseData.get("activityDuration").toString();//天数
		String grouplead = baseData.get("grouplead")==null?"":baseData.get("grouplead").toString();//领队
		String settlementAdultPrice = baseData.get("settlementAdultPrice")==null?"":baseData.get("settlementAdultPrice").toString();//同行价
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
			String groupEndDate = baseData.get("groupEndDate") == null ? "" : baseData.get("groupEndDate").toString();//截团日期
			if(StringUtils.isNotBlank(groupEndDate)){
				groupEndDate = sdf.format(sdf.parse(groupEndDate));
			}
			map.put("groupCloseDate", groupEndDate);
		}
		if(Context.SUPPLIER_UUID_YYJQ.equals(UserUtils.getUser().getCompany().getUuid())){
			String invoiceTax = baseData.get("invoiceTax")==null ? "0":baseData.get("invoiceTax").toString();//发票税
			map.put("invoiceTax", invoiceTax);
		}
		//针对拉美图团期进行操作，获取同行成人价作为单价，和预计收款。需求3690. yudong.xu
		if (ActivityGroupService.isLMTTuanQi(orderType)){
			String adultPrice = baseData.get("adultPrice")== null?"":baseData.get("adultPrice").toString();//单价(成人价)
			String preIncomeOfLMT = baseData.get("preIncome")== null?"":baseData.get("preIncome").toString();//预计收款
			String planPositionOfLMT = baseData.get("planPosition")== null?"":baseData.get("planPosition").toString();//预收人数
			map.put("adultPrice", adultPrice);
			map.put("receiveMoney", preIncomeOfLMT);
			map.put("planPosition", planPositionOfLMT);
		}
		map.put("createBy", FreeMarkerUtil.StringFilter(createBy));
		map.put("createByLeader", baseData.get("createByLeader"));
		map.put("createByLeaderFull", baseData.get("createByLeaderFull"));
		map.put("productName", FreeMarkerUtil.StringFilter(productName));
		map.put("groupCode", FreeMarkerUtil.StringFilter(groupCode));
		map.put("orderPersonNumSum", Integer.parseInt(orderPersonNumSum));
		if(orderType == Context.ORDER_TYPE_QZ){
			map.put("groupDate", "");
		}else if(orderType == Context.ORDER_TYPE_JP 
				|| orderType == Context.ORDER_TYPE_HOTEL
				|| orderType == Context.ORDER_TYPE_ISLAND){
			map.put("groupDate", DateUtils.date2String(DateUtils.string2Date(groupOpenDate)));
		}else{
			map.put("groupDate", DateUtils.date2String(DateUtils.string2Date(groupOpenDate)) + " - " 
					+ DateUtils.date2String(DateUtils.string2Date(groupCloseDate)));
		}
		String days = FinanceUtils.getDays(orderType,groupCloseDate,groupOpenDate,activityDuration);
		map.put("activityDuration", days);
		map.put("grouplead", FreeMarkerUtil.StringFilter(grouplead));
		
		map.put("groupOpenDate", sdf.format(sdf.parse(groupOpenDate)));
		map.put("orderType", CommonUtils.getOrderTypeName(orderType));
		
		// bug16973 wangyang 2016.11.16
		if (Context.ORDER_STATUS_VISA.equals(orderType.toString())) {
			map.put("groupOpenDate", "");
			map.put("groupCloseDate", "");
		}
		if((null != orderPersonNumSum && !"".equals(orderPersonNumSum)) ||
				(null != activityDuration && !"".equals(activityDuration))){
			map.put("personDay", "");//人天数
		}else{
			map.put("personDay", "");//人天数
		}
		map.put("settlementAdultPrice", MoneyNumberFormat.getThousandsByRegex(settlementAdultPrice, 2));
	}
	
	/**
	 * 处理付款明细数据
	 * @param map
	 * @param costRecordVO
	 * @param orderType
	 * @author shijun.liu
	 */
	private void payDetailData(Map<String, Object> map, CostRecordVO costRecordVO, Integer orderType){
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();//用户uuid modify by wangyang 2016.11.9
		StringBuilder salers = new StringBuilder();//销售
		BigDecimal totalSum = new BigDecimal("0");//预计收款-预计收款-合计
		BigDecimal backSum = new BigDecimal("0");//预计收款-预计退款-合计
		BigDecimal accountedSum = new BigDecimal("0");//预计收款-实际收款-合计
		BigDecimal notAcccountedSum = new BigDecimal("0");//预计收款-未收款项-合计
//		int orderPersonNumSum = 0;
		//预计收款、收款明细
		List<Map<String, Object>> refundInfoList = costRecordVO.getRefundInfo();
		for (int i = 0; i < refundInfoList.size() - 1; i++) {
			Map<String, Object> refund = refundInfoList.get(i);
			String totalMoneyStr = refund.get("totalMoney")==null?"0":refund.get("totalMoney").toString();
			String refundPriceStr = refund.get("refundprice")==null?"0":refund.get("refundprice").toString();
			String accountedMoneyStr = refund.get("accountedMoney")==null?"0":refund.get("accountedMoney").toString();
			String orderPersonNum = refund.get("orderPersonNum")==null?"0":refund.get("orderPersonNum").toString();
			BigDecimal totalMoney = new BigDecimal(totalMoneyStr);
			BigDecimal backMoney = new BigDecimal(refundPriceStr);
			BigDecimal accountedMoney = new BigDecimal(accountedMoneyStr);
//			orderPersonNumSum += Integer.parseInt(orderPersonNum);
			refund.put("orderPersonNum", Integer.parseInt(orderPersonNum));
			refund.put("totalMoney", MoneyNumberFormat.getThousandsByRegex(totalMoneyStr, 2));
            refund.put("refundprice", MoneyNumberFormat.getThousandsByRegex(refundPriceStr, 2));
            refund.put("accountedMoney", MoneyNumberFormat.getThousandsByRegex(accountedMoneyStr, 2));
            //实际收款 = 收款 - 退款  只适用于辉煌齐鲁 2016.03.17 by jinxin.gao
            BigDecimal notAccountedMoney = new BigDecimal("0");
            BigDecimal newAccountedMoney = new BigDecimal("0");
			// 需求 457 预报单   未收款项 = 预计收款 - 实际收款 + 预计退款
			//		   结算单	   未收团款 = 收款 - 实际收款 + 退款
			notAccountedMoney = totalMoney.subtract(accountedMoney).add(backMoney);
            /**if(Context.SUPPLIER_UUID_QDKS.equals(UserUtils.getUser().getCompany().getUuid())){
                newAccountedMoney = totalMoney.subtract(backMoney);
                refund.put("accountedMoney", MoneyNumberFormat.getThousandsByRegex(newAccountedMoney.toString(), 2));
                //未收款项 = 预计收款- 实际收款  2015.05.19
                notAccountedMoney = totalMoney.subtract(newAccountedMoney);
            }else{
                newAccountedMoney = totalMoney.subtract(backMoney);
                refund.put("accountedMoney", MoneyNumberFormat.getThousandsByRegex(accountedMoney.toString(), 2));
                //未收款项 = 预计收款- 实际收款  2015.05.19
                notAccountedMoney = totalMoney.subtract(accountedMoney);
            }*/

			//当未收款小于0时，未收款值置0 (只适用于拉美图) modify by 王洋 2016.3.17
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)){
				notAccountedMoney = notAccountedMoney.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : notAccountedMoney;
			}
			/**
			 * 	环球行用户不适用于 457需求，即保持原需求不变
			 * 	预报单   未收款项 = 预计收款 - 实际收款
			 * 	结算单	未收团款 = 收款 - 实际收款
			 */
			if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
				notAccountedMoney = totalMoney.subtract(accountedMoney);
			}
			refund.put("notAccountedMoney", MoneyNumberFormat.getThousandsByRegex(notAccountedMoney.toString(), 2));
			totalSum = totalSum.add(totalMoney);
			backSum = backSum.add(backMoney);
			if(Context.SUPPLIER_UUID_QDKS.equals(UserUtils.getUser().getCompany().getUuid())){
				accountedSum = accountedSum.add(newAccountedMoney);
			}else{
				accountedSum = accountedSum.add(accountedMoney);
			}
			notAcccountedSum = notAcccountedSum.add(notAccountedMoney);
			if(null != refund.get("saler") && !"其他".equals(refund.get("agentName"))){
				if(salers.indexOf(refund.get("saler").toString()) == -1){
					salers.append(refund.get("saler")).append(",");
				}
			}
			if(Context.SUPPLIER_UUID_DYGL.equals(UserUtils.getUser().getCompany().getUuid()) 
					&& AGENT_NAME_OLD.equals(refund.get("agentName"))){
				refund.put("agentName", AGENT_NAME_NEW);
			}
		}
		//其他收入收款
		List<Map<String,Object>> otherRecordList = costRecordVO.getOtherRecordList();
		BigDecimal otherSum = new BigDecimal("0");
		if(UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_LZJQ)){
			if(otherRecordList.size() > 0){
				for(Map<String,Object> otherMap : otherRecordList){
					otherSum = otherSum.add(new BigDecimal(otherMap.get("totalMoney").toString()));
				}
			}
		}
//		map.put("orderPersonNumSum", orderPersonNumSum);
		map.put("otherSum", otherSum);
		map.put("totalSum", totalSum);
		map.put("backSum", backSum);
		map.put("accountedSum", accountedSum);
		map.put("notAcccountedSum", notAcccountedSum);
		if(salers.length()>0){
			salers.delete(salers.length()-1, salers.length());
		}
		if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)) {
			map.put("expectIncome", refundInfoList.get(refundInfoList.size()-1).get("exceptIncome4LZJQ"));
		} else {
			map.put("expectIncome", refundInfoList.subList(0, refundInfoList.size() - 1));//预计收款
		}
		if(Context.ORDER_TYPE_SP == orderType || Context.ORDER_TYPE_JP == orderType 
				|| orderType == Context.ORDER_TYPE_QZ){//散拼,机票，签证销售为空
			map.put("salers", "");//销售 
		}else{
			map.put("salers", FreeMarkerUtil.StringFilter(salers.toString()));//销售
		}
	}
	
	/**
	 * 预报单->预计境内付款/结算单->境内支出
	 * @param map
	 * @param costRecordVO
	 * @return
	 * @author shijun.liu
	 */
	private BigDecimal actualInData(Map<String, Object> map, CostRecordVO costRecordVO, Integer orderType){
		List<CostRecord> actualInList = costRecordVO.getActualInList();
//		Map<String,List<CostRecord>> groups = new LinkedHashMap<>();//创建一个map，保存每个地接社下的成本记录。
		BigDecimal inMoneySum = new BigDecimal("0");
		for (CostRecord record:actualInList) {
			BigDecimal unitPrice = record.getPrice() == null ? new BigDecimal("0") : record.getPrice();
			BigDecimal priceAfter = record.getPriceAfter()==null ? new BigDecimal("0") : record.getPriceAfter();
			BigDecimal rate = record.getRate() == null ? new BigDecimal("1") : record.getRate();
			BigDecimal tempUnitPrice = unitPrice.multiply(rate);
			String formatUnitPrice = MoneyNumberFormat.getThousandsByRegex(tempUnitPrice.toString(), 2);
			//单价
			record.setFormatPrice(formatUnitPrice);
			//金额
			record.setFormatPriceAfter(MoneyNumberFormat.getThousandsByRegex(priceAfter.toString(),2));
			//地接社
			String supplyName = record.getSupplyName();
			if (null == supplyName) {
				supplyName = "";
			}

			//大洋国旅，将非签约渠道改成未签，2015.11.25 C413
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			if(Context.SUPPLIER_UUID_DYGL.equals(companyUuid) && AGENT_NAME_OLD.equals(supplyName)){
				supplyName = AGENT_NAME_NEW;
			}
			if (Context.SUPPLIER_UUID_YJXZ.equals(companyUuid) && AGENT_NAME_OLD.equals(supplyName)){
				supplyName = AGENT_NAME_ZHIKE; //越谏行踪，将非签约渠道改成直客。 add by yudong.xu 2016.5.30
			}
			record.setSupplyName(FreeMarkerUtil.StringFilter(supplyName));
			inMoneySum = inMoneySum.add(priceAfter);
		}
		//骡子假期返佣
		if(Context.SUPPLIER_UUID_LZJQ.equals(UserUtils.getUser().getCompany().getUuid())){
			List<Map<String, Object>> fyList = costRecordVO.getfYlistList();
			if(fyList != null){
				for(Map<String,Object> fyMap : fyList){
					inMoneySum = inMoneySum.add(new BigDecimal(fyMap.get("price").toString()));
				}
			}
		}
		map.put("actualInList", actualInList);//预计境内付款
		return inMoneySum;
	}
	
	/**
	 * 处理返佣数据
	 * @param map
	 * @param costRecordVO
	 * @param inMoneySum
	 * @author shijun.liu
	 */
	@SuppressWarnings("unchecked")
	private BigDecimal commissionData(Map<String, Object> map, CostRecordVO costRecordVO, BigDecimal inMoneySum){
		List<Map<String, Object>> commission = costRecordVO.getfYlistList();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(null != commission){
			for (Map<String, Object> fyMap:commission) {
				CostRecord record = new CostRecord();
				record.setName("其他");
				if(null == fyMap.get("supplyName")){
					record.setSupplyName(" ");
				}else{
					record.setSupplyName(FreeMarkerUtil.StringFilter(fyMap.get("supplyName").toString()));
				}
				record.setQuantity(Integer.parseInt(fyMap.get("count").toString()));
				String formatAfterPrice = MoneyNumberFormat.getThousandsByRegex(fyMap.get("price").toString(), 2);
				//单价
				record.setFormatPrice("-");
				//金额
				record.setFormatPriceAfter(formatAfterPrice);
				if (! Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)) {
					inMoneySum = inMoneySum.add(new BigDecimal(fyMap.get("price").toString()));
				}	
				if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)) {
					// 骡子假期 返佣不计入支出数据中   modify by wangyang 2016.11.15
				} else {
					if(null != map.get("actualInList")){
						ArrayList<CostRecord> actualIn = (ArrayList<CostRecord>)map.get("actualInList");
						actualIn.add(actualIn.size(), record);
					}else{
						List<CostRecord> tempActualInList = new ArrayList<CostRecord>();
						tempActualInList.add(record);
						map.put("actualInList", tempActualInList);//预计境内付款
					}
				}
			}
		}
		return inMoneySum;
	}
	
	/**
	 * 预报单->预计境外付款/结算单->境外支出
	 * @param map
	 * @param costRecordVO
	 * @return
	 * @author shijun.liu
	 */
	private BigDecimal actualOutData(Map<String, Object> map, CostRecordVO costRecordVO){
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		List<CostRecord> actualOutList = costRecordVO.getActualoutList();
//		Map<String,List<CostRecord>> groups = new LinkedHashMap<>();
		BigDecimal outMoneySum = new BigDecimal("0");
		for (CostRecord record:actualOutList) {
			Currency currency = currencyService.findCurrency(Long.valueOf(record.getCurrencyId()));
			record.setRate(record.getRate()==null ? new BigDecimal("1") : record.getRate());

			if (Context.SUPPLIER_UUID_LZJQ.equals(companyUuid)) {
				
				BigDecimal unitPrice = record.getPrice().multiply(record.getRate());
				record.setFormatPrice(MoneyNumberFormat.getThousandsByRegex(unitPrice.toString(), 2));
				
			} else {
				//币种，境外付款使用CostRecord对象里面的name属性作为币种名称属性
				if (null != currency) {
					record.setName(currency.getCurrencyName());
				} else {
					record.setName("");
				}
				
				//外币
				BigDecimal unitPrice = record.getPrice() == null ? new BigDecimal("0") : record.getPrice();
				Integer count = record.getQuantity() == null ? 0 : record.getQuantity();
				BigDecimal tempUnitPrice = unitPrice.multiply(new BigDecimal(count));
				String formatUnitPrice = currency.getCurrencyMark()+MoneyNumberFormat.getThousandsByRegex(tempUnitPrice.toString(), 2);
				record.setFormatPrice(formatUnitPrice);
			}
			//汇率
			record.setFormatRate(MoneyNumberFormat.getFormatRate(record.getRate().doubleValue(),MoneyNumberFormat.POINT_THREE));

			//金额
			BigDecimal priceAfter = record.getPriceAfter()==null ? new BigDecimal("0") : record.getPriceAfter();
			record.setFormatPriceAfter(MoneyNumberFormat.getThousandsByRegex(priceAfter.toString(),2));

			//地接社
			String supplyName = record.getSupplyName();
			if(null == supplyName){
				supplyName = "";
			}
			if(Context.SUPPLIER_UUID_DYGL.equals(companyUuid) && AGENT_NAME_OLD.equals(supplyName)){
				supplyName = AGENT_NAME_NEW;
			}
			if (Context.SUPPLIER_UUID_YJXZ.equals(companyUuid) && AGENT_NAME_OLD.equals(supplyName)){
				supplyName = AGENT_NAME_ZHIKE;//直客，越谏行踪，将非签约渠道改为直客。 add by yudong.xu 2016.5.30
			}
			record.setSupplyName(FreeMarkerUtil.StringFilter(supplyName));

			outMoneySum = outMoneySum.add(priceAfter);
		}
		//骡子假期 返佣
		if(UserUtils.getUser().getCompany().getUuid().equals(Context.SUPPLIER_UUID_LZJQ)){
			List<Map<String, Object>> fyList = costRecordVO.getfYlistList();
			List<CostRecord> list = new ArrayList<CostRecord>();
			for(Map<String,Object> fyMap : fyList){
				CostRecord  record = new CostRecord();
				record.setName("其他");
				record.setFormatPriceAfter(fyMap.get("price").toString());
				record.setSupplyName(fyMap.get("supplyName").toString());
				list.add(record);
			}
			actualOutList.addAll(list);
		}
		map.put("actualOutList", actualOutList);//预计境外付款/境外支出
		return outMoneySum;
	}
	
	/**
	 * 打印查询接口
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
	@RequestMapping(value="payment/{id}/{orderType}/{isNew}", method=RequestMethod.GET)
	public String payment(@PathVariable(value="id") Long id, @PathVariable(value="orderType") Integer orderType, 
			@PathVariable(value="isNew") Integer isNew, Model model,
			HttpServletRequest request, HttpServletResponse response){
		if(null == id){
			return null;
		}
		CostRecord costRecord = costManageService.findOne(id);
		if(null == costRecord){
			return null;
		}
		if(null == costRecord.getPrintTime()) {
			model.addAttribute("firstPrintTime", new Date());
		}else{
			model.addAttribute("firstPrintTime", costRecord.getPrintTime());
		}
		
		String option = request.getParameter("option");
		String payId = request.getParameter("payId");
		if("pay".equals(option))   {	//点击支付记录的打印按钮
			printbyOptionPay(model, payId, costRecord.getOrderType().toString(), costRecord.getRate());
			//获取银行账户信息
			if(-1 == costRecord.getSupplyId()) {   //非签约渠道
				model.addAttribute("accountName", "");
			}else {
				String accountName = platBankInfoService.getAccountName(Long.valueOf(costRecord.getSupplyId()), costRecord.getSupplyType()+1, 
						costRecord.getBankName(), costRecord.getBankAccount(),"");
				model.addAttribute("accountName", accountName);
			}	
		}else if("order".equals(option)) {	//点击原有成本列表记录的打印按钮
			printbyOptionOrder(model, costRecord);
			model.addAttribute("accountName", "");
		}
		
		if(Context.ORDER_TYPE_JP == orderType) {
			ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(costRecord.getActivityId());
			model.addAttribute("groupCode", activityAirTicket.getGroupCode());
		}else if(Context.ORDER_TYPE_QZ == orderType) {
			VisaProducts visaPorduct = visaProductsDao.findOne(costRecord.getActivityId());
			model.addAttribute("groupCode", visaPorduct.getGroupCode());
		}else {
			ActivityGroup activityGroup = activityGroupDao.findOne(costRecord.getActivityId());
			model.addAttribute("groupCode", activityGroup.getGroupCode());
		}
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		//新审核数据单据人员信息抓取20151205
		String deptmanager = ""; //部门主管
		String manager = "";	//总经理
		String finance = "";	//财务主管
		String cashier = "";	//出纳
		String reviewer = "" ;	//审核
		if(isNew == 2) {
			String reviewId = costRecord.getPayReviewUuid();
			//获取单据审批人员Map
			MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUuid, ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, reviewId);
			List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);//主管
			List<User> managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);//总经理
			List<User> finances = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);//财务主管
			List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);//出纳
			List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);//审核
			deptmanager = UserUtils.getUserNames(executives);//主管
			manager = UserUtils.getUserNames(managers);//总经理
			finance = UserUtils.getUserNames(finances);//财务主管
			cashier = UserUtils.getUserNames(cashiers);//出纳
			reviewer = UserUtils.getUserNames(reviewers);//审核
		}else {
			//支出凭单获取人员信息20150505，根据付款审核流程
			reviewer = reviewService.getPayReviewPerson(costRecord.getId(), 9) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 9);
			manager = reviewService.getPayReviewPerson(costRecord.getId(), 10) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 10);
			deptmanager = reviewService.getPayReviewPerson(costRecord.getId(), 4) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 4);
			//青岛凯撒需要在单据上显示出纳，20150710
			if(Context.SUPPLIER_UUID_QDKS.equals(companyUuid)) {
				cashier = reviewService.getPayReviewPerson(costRecord.getId(), 6);
			}
		}	

		model.addAttribute("cashier", cashier);
		model.addAttribute("deptmanager", deptmanager);
		model.addAttribute("reviewer", reviewer);
		model.addAttribute("manager", manager);
		model.addAttribute("finance", finance);
		model.addAttribute("id",id);
		model.addAttribute("orderType",orderType);
		model.addAttribute("option", option);
		model.addAttribute("payId", payId);
		model.addAttribute("money",costRecord.getName());	
		model.addAttribute("isNew",isNew);
		model.addAttribute("person",costRecord.getCreateBy().getName());
		model.addAttribute("currencyId", costRecord.getCurrencyId());
		model.addAttribute("price", costRecord.getPrice());
		model.addAttribute("quantity", costRecord.getQuantity());
		model.addAttribute("createDate", costRecord.getCreateDate());
		model.addAttribute("supplyName", costRecord.getSupplyName());
		model.addAttribute("conDate",costRecord.getUpdateDate());
		model.addAttribute("bankName", costRecord.getBankName());
		model.addAttribute("bankAccount", costRecord.getBankAccount());
		//20150720修改支出凭单摘要
		model.addAttribute("remark",costRecord.getComment());

		//当前批发商的美元、加元汇率（目前环球行）
		List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("curlist", currencylist);
		//20151012增加付款状态，判断财务撤销确认付款后不显示确认付款时间
		model.addAttribute("payStatus", costRecord.getPayStatus());
		//20151016环球行、拉美途客户确认到账时间为空
		if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
			model.addAttribute("isHQX", true);//环球行
		}else if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)){
			model.addAttribute("isLMT", true);//拉美途
		}
		//20151023汇率取值规则更改为成本录入时的汇率值
		model.addAttribute("rate", MoneyNumberFormat.fmtMicrometer(costRecord.getRate().toString(), "#,##0.0000"));
			
		return "modules/cost/payment";
	}
	
	//点击支付记录里面的打印按钮
	private void printbyOptionPay(Model model, String payId, String orderType, BigDecimal rate) {		
		PayInfoDetail payInfoDetail = null;
		if(StringUtils.isNotBlank(payId)) {
			payInfoDetail = refundService.getPayInfoByPayId(payId, orderType);
		}
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(null != payInfoDetail) {
			model.addAttribute("pay", payInfoDetail);
			//越柬行踪，支票和现金付款时，实际领款人，取收款单位名称 0419  update by shijun.liu 2016.04.25
			if(Context.SUPPLIER_UUID_YJXZ.equals(companyUuid)){
				Integer payType = payInfoDetail.getPayType();
				if(1 == payType || 3 == payType){
					model.addAttribute("payee", payInfoDetail.getPayerName());
				}
			}
		}
		BigDecimal currentAmount = new BigDecimal(0);
		if(payInfoDetail != null) {
			String moneyDispStyle = payInfoDetail.getMoneyDispStyle();
			List<Object[]> moneys = MoneyNumberFormat.getMoneyFromString(moneyDispStyle, "\\+");
			if(CollectionUtils.isNotEmpty(moneys)) {
				currentAmount  = new BigDecimal(Double.valueOf(moneys.get(0)[1].toString()));
				BigDecimal RMBPrice = currentAmount.multiply(rate);
				model.addAttribute("amount", RMBPrice);
			}else {
				model.addAttribute("amount", new BigDecimal(0));
			}
		}else {
			model.addAttribute("amount", new BigDecimal(0));
		}
		model.addAttribute("currencyMoney", currentAmount);
	}
	
	//点击成本付款列表的打印按钮
	private void printbyOptionOrder(Model model, CostRecord costRecord) {
		PayInfoDetail payInfoDetail = refundService.getPayInfoByPayId(costRecord.getSerialNum(),costRecord.getOrderType()+"");
		if(payInfoDetail != null) {
			model.addAttribute("pay", payInfoDetail);
		}
		model.addAttribute("payee", "");
		//取实际付款金额（单价*数量）20150420
		BigDecimal price = costRecord.getPrice();
		BigDecimal quantity = BigDecimal.valueOf(costRecord.getQuantity());
		BigDecimal currentAmount = price.multiply(quantity);
		
		model.addAttribute("amount", costRecord.getPriceAfter());
		model.addAttribute("currencyMoney", currentAmount);
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
				costRecord.setUpdateDate(nowDate);
				costRecord.setUpdateBy(UserUtils.getUser().getId());
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
			@RequestParam String isNew, @RequestParam String payId, @RequestParam String option, HttpServletRequest request, HttpServletResponse response) 
					throws IOException, TemplateException{
		if(StringUtils.isBlank(id) || StringUtils.isBlank(orderType) || StringUtils.isBlank(isNew)){
			return null;
		}
		
		File file = null;
		if("pay".equals(option)) {
			file = costManageService.createPaymentFile(Long.parseLong(id), Integer.parseInt(orderType), Integer.parseInt(isNew), payId);
		}else if("order".equals(option)) {
			file = costManageService.createPaymentFile(Long.parseLong(id), Integer.parseInt(orderType), Integer.parseInt(isNew));
		}
		
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
	@RequestMapping(value="paymentConfirm/{id}/{orderType}", method=RequestMethod.GET)
	public String paymentConfirm(@PathVariable(value="id") Long id,@PathVariable(value="orderType") String orderType,HttpServletRequest request,HttpServletResponse response){
		String payType = request.getParameter("payType");
		String groupId = request.getParameter("groupId");
//		String flag = request.getParameter("flag");
		OrderPayInput orderPayInput = null;
		if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_HOTEL==Integer.valueOf(orderType)){
			orderPayInput=costManageService.payForHotel(id+"");
		}else if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ISLAND==Integer.valueOf(orderType)){
			orderPayInput = costManageService.payforisland(id + "");
		}else{
			if (payType == null || "".equals(payType)) {
				orderPayInput = costManageService.pay(id + "");
			} else {
				orderPayInput = costManageService.pay(id + "", payType, groupId, orderType);
			}
		}
		if(SecurityUtils.getSubject().isPermitted("pay:cost")) {
			orderPayInput.setPaymentListUrl("orderCommon/manage/showOrderList/199/1.htm?option=pay");
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
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (StringUtils.isNotBlank(id)) {
			list = costManageService.findPayedRecordById(id);
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
		
		if(Context.ORDER_TYPE_ISLAND == prdType || Context.ORDER_TYPE_HOTEL == prdType){
			Map<String, Object> data = costManageService.getAccountDataHotelIsland(
					id.intValue(), prdType);
			model.addAttribute("data", data);
			return "modules/cost/payInfo";
		}else{
			Map<String, Object> data = costManageService.getAccountData(
					id.intValue(), prdType);
			model.addAttribute("data", data);
			return "modules/cost/payInfo";
		}
	}

	/**
	 * 批量打印其他收入收款单的方法
	 * @param request
	 * @param model
     * @return
	 * @author yudong.xu 2016.10.27
     */
	@RequestMapping(value = "accountBatchPrint")
	public String accountBatchPrint(HttpServletRequest request,Model model) {
		String printInfo = request.getParameter("printInfo");

		JSONArray printArr = JSONArray.parseArray(printInfo);
		if (printArr == null || printArr.size() == 0){
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "未找到记录");
			return Context.ERROR_PAGE;
		}

		List<Map<String,Object>> printList = new ArrayList<>();
		for (int i = 0; i < printArr.size(); i++) {
			JSONObject printObj = printArr.getJSONObject(i);
			Integer payId = printObj.getInteger("payId");
			Integer prdType = printObj.getInteger("prdType");

			Map<String, Object> data;
			if(Context.ORDER_TYPE_ISLAND == prdType || Context.ORDER_TYPE_HOTEL == prdType){
				data = costManageService.getAccountDataHotelIsland(payId, prdType);
			} else {
				data = costManageService.getAccountData(payId, prdType);
			}
			printList.add(data);
		}
		model.addAttribute("printList",printList);

		return "modules/cost/otherIncomeBatchPrint";
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
		File file = costManageService.createReceiveFile(Integer.parseInt(id), Integer.parseInt(orderType));
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName = "收款单" + nowDate + ".doc";
		ServletUtil.downLoadFile(response, fileName, file.getPath());
		return null;
	}
	
	/**
	 * @Title: fankuandanDownload
	 * 下载返款单(请款单)
	 * @param id
	 * @param orderType
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@RequestMapping(value = "fankuandanDownload/{id}/{orderType}/{type}")
	public ResponseEntity<byte[]> fankuandanDownload(@PathVariable String id,
			@PathVariable String orderType,@PathVariable String type, HttpServletRequest request,
			HttpServletResponse response) throws IOException, TemplateException {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(orderType)) {
			return null;
		}
		File file = costManageService.createfankuanFile(Integer.parseInt(id),
				Integer.parseInt(orderType),Integer.parseInt(type));
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName = "请款单" + nowDate + ".doc";
		ServletUtil.downLoadFile(response, fileName, file.getPath());
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
		if (StringUtils.isBlank(payId)) {
			return "错误的数据payId。请确认当前页面的数据的准确性";
		}
		return costManageService.updatePrintFlag(payId, prdtype);
	}

	/**
	 * 批量更新打印标志
	 *
	 * @param request
	 * @return
	 * @author xuydong.xu 2016.10.27
	 */
	@RequestMapping(value = "batchUpdatePrint")
	@ResponseBody
	public String batchUpdatePrintFlag(HttpServletRequest request) {
		String printInfo = request.getParameter("updateInfo");
		JSONArray printArr = JSONArray.parseArray(printInfo);
		if (printArr == null){
			return "错误的业务数据payId。请确认收款的数据的准确性";
		}

		for (int i = 0; i < printArr.size(); i++) {
			String printItem = printArr.getString(i);
			String[] itemArr = printItem.split("_");
			String payId = itemArr[0];
			String prdType = itemArr[1];
			String result = costManageService.updatePrintFlag(payId,prdType);
			if (!"success".equals(result)){
				return result;
			}
		}
		return "success";
	}

	/**
	 * 更新打印标志
	 * 返款单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updatePrint4fkd")
	@ResponseBody
	public String updatePrintFlag4fkd(HttpServletRequest request,
			HttpServletResponse response) {
		String payId = request.getParameter("payId");
		String prdtype = request.getParameter("prdType");
		String rid = request.getParameter("reviewId");
		/*if (payId == null || "".equals(payId)) {
			return "错误的数据payId。请确认当前页面的数据的准确性";
		}*/
		return costManageService.updatePrintFlag4fkd(payId, prdtype,rid);
	}
	
	
	
    //---------------------wxw added酒店（11） 和  海岛游（12） 打印开始-----------------------
	//---------------------wxw added酒店（11） 和  海岛游（12） 打印开始-----------------------
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
	@RequestMapping(value="paymentForHotelAndIslandPrint/{id}/{orderType}", method=RequestMethod.GET)
	public String paymentForHotelAndIslandPrint(@PathVariable(value="id") Long id, 
			@PathVariable(value="orderType") Integer orderType, Model model,
			HttpServletRequest request, HttpServletResponse response){
		
		//test url     http://localhost:8080/trekiz_wholesaler_tts/a/cost/manager/paymentForHotelAndIslandPrint/1/12
		
		if(id == null){
			return null;
		}

		CostRecordHotel costRecordHotel = null;
		CostRecordIsland costRecordIsland = null;
		//CostRecord costRecord  = costManageService.findOne(id);
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			costRecordHotel = costRecordHotelDao.findOne(id);
			if (null==costRecordHotel) {
				return null;
			}else {
				if(costRecordHotel.getPrintTime() == null) {
					model.addAttribute("firstPrintTime", new Date());
				}else{
					model.addAttribute("firstPrintTime", costRecordHotel.getPrintTime());
				}
			}
			
		}else if (orderType == Context.ORDER_TYPE_ISLAND) {
			costRecordIsland = costRecordIslandDao.findOne(id);
			if (null==costRecordIsland) {
				return null;
			}else {
				if(costRecordIsland.getPrintTime() == null) {
					model.addAttribute("firstPrintTime", new Date());
				}else{
					model.addAttribute("firstPrintTime", costRecordIsland.getPrintTime());
				}
			}
		}
		
		
		//---------?????------------
		PayInfoDetail payInfoDetail = null;
		//payInfoDetail = refundService.getPayInfoByPayId(costRecord.getSerialNum());
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			payInfoDetail = refundService.getPayInfoByPayId(costRecordHotel.getSerialNum(),String.valueOf(orderType));
		}else if (orderType == Context.ORDER_TYPE_ISLAND) {
			payInfoDetail = refundService.getPayInfoByPayId(costRecordIsland.getSerialNum(),String.valueOf(orderType));
		}
		model.addAttribute("pay", payInfoDetail);
		//获取一次或多次付款金额20150409
//		List<Object[]> paydmoney = moneyAmountService.getRefundPaydMoneyList(id + "");
//		String currencyId = "" , currencyMoney = "" ;
//		if(paydmoney != null && paydmoney.size() > 0) {
//			currencyId = paydmoney.get(0)[0].toString();
//			currencyMoney = paydmoney.get(0)[2].toString();
//		}
		//取实际付款金额（单价*数量）20150420
		BigDecimal price = null;
		BigDecimal quantity=null;
		BigDecimal currentAmount = null;
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			price = costRecordHotel.getPrice();
			quantity = BigDecimal.valueOf(costRecordHotel.getQuantity());
			currentAmount = price.multiply(quantity);
			ActivityHotelGroup activityHotelGroup = activityHotelGroupDao.getByUuid(costRecordHotel.getActivityUuid());
			model.addAttribute("groupCode", activityHotelGroup.getGroupCode());
		}else if (orderType == Context.ORDER_TYPE_ISLAND) {
			price = costRecordIsland.getPrice();
			quantity = BigDecimal.valueOf(costRecordIsland.getQuantity());
			currentAmount = price.multiply(quantity);
			ActivityIslandGroup activityIslandGroup = activityIslandGroupDao.getByUuid(costRecordIsland.getActivityUuid());
			model.addAttribute("groupCode", activityIslandGroup.getGroupCode());
		}
	
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			model.addAttribute("money",costRecordHotel.getName());
			if (null!=costRecordHotel.getCreateBy()) {
				model.addAttribute("person",costRecordHotel.getCreateBy().getName());
			}else {
				model.addAttribute("person","");
			}
			
			model.addAttribute("currencyId", costRecordHotel.getCurrencyId());
			model.addAttribute("price", costRecordHotel.getPrice());
			model.addAttribute("quantity", costRecordHotel.getQuantity());
			model.addAttribute("createDate", costRecordHotel.getCreateDate());
			model.addAttribute("supplyName", costRecordHotel.getSupplyName());
			model.addAttribute("bankName", costRecordHotel.getBankName());
			model.addAttribute("bankAccount", costRecordHotel.getBankAccount());
			model.addAttribute("rate", costRecordHotel.getRate());
			
			
			//Currency currency = currencyService.findCurrency(Long.parseLong(costRecordHotel.getCurrencyId()+""));
			
			String totalRMB = costRecordHotel.getPriceAfter().toString();//.multiply(costRecordHotel.getRate()).toString();
			//加元  或 美元
			model.addAttribute("originMoney",costRecordHotel.getPrice().multiply(new BigDecimal(costRecordHotel.getQuantity())));
			
			model.addAttribute("totalRMB", MoneyNumberFormat.fmtMicrometer(totalRMB, MoneyNumberFormat.THOUSANDST_POINT_TWO));
			model.addAttribute("totalRMBDX", MoneyNumberFormat.digitUppercase(Double.parseDouble(totalRMB)));
		}else if (orderType == Context.ORDER_TYPE_ISLAND) {
			model.addAttribute("money",costRecordIsland.getName());
			if (null!=costRecordIsland.getCreateBy()) {
				model.addAttribute("person",costRecordIsland.getCreateBy().getName());
			}else {
				model.addAttribute("person","");
			}
		
			model.addAttribute("currencyId", costRecordIsland.getCurrencyId());
			model.addAttribute("price", costRecordIsland.getPrice());
			model.addAttribute("quantity", costRecordIsland.getQuantity());
			model.addAttribute("createDate", costRecordIsland.getCreateDate());
			model.addAttribute("supplyName", costRecordIsland.getSupplyName());
			model.addAttribute("bankName", costRecordIsland.getBankName());
			model.addAttribute("bankAccount", costRecordIsland.getBankAccount());
			model.addAttribute("rate", costRecordIsland.getRate());
			
//			Currency currency = currencyService.findCurrency(Long.parseLong(costRecordIsland.getCurrencyId()+""));
			
			String totalRMB = costRecordIsland.getPriceAfter().toString();//.multiply(currency.getConvertLowest()).toString();//加元  或 美元
			model.addAttribute("originMoney",costRecordIsland.getPrice().multiply(new BigDecimal(costRecordIsland.getQuantity())));
			
			model.addAttribute("totalRMB", MoneyNumberFormat.fmtMicrometer(totalRMB, MoneyNumberFormat.THOUSANDST_POINT_TWO));
			model.addAttribute("totalRMBDX", MoneyNumberFormat.digitUppercase(Double.parseDouble(totalRMB)));
			
		}
		
		model.addAttribute("conDate",payInfoDetail.getCreateDate());
		model.addAttribute("currencyMoney", currentAmount);
		model.addAttribute("id",id);
		model.addAttribute("orderType",orderType);
		//根据地接社或者渠道商确定银行帐号
//		Integer supplyType = costRecord.getSupplyType();
//		if(supplyType != null) {
//			
//		}
		//当前批发商的美元、加元汇率（目前环球行）
		List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("curlist", currencylist);
		return "modules/cost/paymentforhotelandislandprint";
	}
	
	
	/**
	 * 打印时修改打印状态
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="updatePrintedForHotelAndIsland", method=RequestMethod.GET)
	public void updatePrintedForHotelAndIsland(HttpServletRequest request, HttpServletResponse response, Model model){
		String json = "";
		Long id = null;
		Integer orderType = null;
		String idValue = request.getParameter("id");
		String orderTypeValue = request.getParameter("orderType");
		if(StringUtils.isNotBlank(idValue)&&StringUtils.isNotBlank(orderTypeValue)){
			id = Long.valueOf(idValue);
			orderType = Integer.valueOf(orderTypeValue);
			Date date = updateOrderPayPrintedForHotelAndIsland(id,orderType);
			if(null != date){
				String dateStr = DateUtils.date2String(new Date(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
				json = "{\"flag\":\"success\",\"msg\":\""+dateStr+"\"}";
			}else {
				json = "{\"flag\":\"success\",\"msg\":\""+null+"\"}";
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
	
	private Date updateOrderPayPrintedForHotelAndIsland(Long id,Integer orderType){
		Date nowDate = null;
		
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			CostRecordHotel costRecordHotel = costRecordHotelDao.findOne(id);
			if(costRecordHotel != null){
				Integer printFlag = costRecordHotel.getPrintFlag();
				if (printFlag == null || printFlag == 0){
					nowDate = new Date();
					costRecordHotel.setPrintTime(nowDate);
					costRecordHotel.setPrintFlag(1);
					costRecordHotel.setUpdateBy(UserUtils.getUser().getId());
					costRecordHotel.setUpdateDate(nowDate);
					costIslandService.saveCostRecordHotel(costRecordHotel);
				}
			}
			
		}else if (orderType == Context.ORDER_TYPE_ISLAND) {
			CostRecordIsland costRecordIlaIsland = costRecordIslandDao.findOne(id);
			if(costRecordIlaIsland != null){
				Integer printFlag = costRecordIlaIsland.getPrintFlag();
				if (printFlag == null || printFlag == 0){
					nowDate = new Date();
					costRecordIlaIsland.setPrintTime(nowDate);
					costRecordIlaIsland.setPrintFlag(1);
					costRecordIlaIsland.setUpdateBy(UserUtils.getUser().getId());
					costRecordIlaIsland.setUpdateDate(nowDate);
					costIslandService.saveCostRecordIsland(costRecordIlaIsland);
				}
			}
		}
		return nowDate;
	}
	
	
	/**
	 * 
	* @Title: paymentList
	* @Description: 下载付款支付凭证
	* @param @param id
	* @param @param orderType
	* @param @param request
	* @param @param response
	* @param @return
	* @param @throws IOException
	* @return ResponseEntity<byte[]>    返回类型
	* @throws
	 */
	@RequestMapping(value="paymentListForHotelAndIsland")
	public ResponseEntity<byte[]> paymentListForHotelAndIsland(@RequestParam String id,@RequestParam String orderType,
			HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{
		if(StringUtils.isBlank(id) || StringUtils.isBlank(orderType)){
			return null;
		}
		File file = costManageService.createPaymentFileForHotelAndIsland(Long.parseLong(id), Integer.parseInt(orderType));
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName = new StringBuilder().append("支付凭单").append(nowDate).append(".doc").toString();
		WordDownLoadUtils.downLoadWordByAttachment(file, fileName, response);
		return null;
	}
	
	/**
	 * 该方法用于预报单，结算单上锁时，查询数据库数据，并按照现有逻辑进行处理后，返回一个Map类型的预报单结算单数据。
	 * type : 0 预报单，1 结算单
	 * orderType : 订单类型。根据订单类型，来判断使用哪一个id。
	 * activityId : 团期id或者产品id
	 * groupUuid : 海岛，酒店使用的uuid。
	 * @author yudong.xu --2016/4/29--12:24
	 */
	private Map<String,Object> getResultBySearch(Integer type,Integer orderType,Long activityId,String groupUUID) throws Exception {
		CostRecordVO cRecordVO = null;
		Map<String, Object> resultMap = null;
		if (type == 0 ){
			cRecordVO = costManageService.getForecastDataInfo(activityId, orderType, groupUUID);

			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && Context.ORDER_TYPE_YX != orderType){
				handleActualInForLMT(cRecordVO,activityId,orderType); //把原有代码，提取为一个方法了。yudong.xu
			}

			resultMap = castCostRecordVO2Map(cRecordVO, orderType, 0);
		}else if (type == 1){
			cRecordVO = costManageService.getSettleDataInfo(activityId, orderType, groupUUID);
			resultMap = settleService.castCostRecordVO2Map(cRecordVO, orderType, 1);
		}
		return resultMap;
	}


	/**
	 * 当预报单结算单进行锁定的时候，重新查询一遍预报单结算单的数据，把查询结果保存到相应表中。 yudong.xu 2016.5.13
	 * @param type			0是预报单，1是结算单
	 * @param lockStatus	锁定状态
	 * @param orderType		订单类型
	 * @param activityId	团期id，或者产品id
	 * @param groupUuid		海岛酒店的uuid
     * @throws Exception
     */
	private void handleLocker(Integer type,String lockStatus,Integer orderType,Long activityId,String groupUuid) throws Exception {
		if (type == 0){
			if ("10".equals(lockStatus)){
				Map<String,Object> resultMap = getResultBySearch(0,orderType,activityId,groupUuid);
				foreCastService.saveForeCastByMap(orderType,activityId,groupUuid,resultMap);
			}
		}else {
			if ("1".equals(lockStatus)){
				Map<String,Object> resultMap = getResultBySearch(1,orderType,activityId,groupUuid);
				settleService.saveSettleByMap(orderType,activityId,groupUuid,resultMap);
			}
		}
	}

	/**
	 * 根据锁定状态的不同，获取预报单数据。
	 * 如果锁定，则从预报单表中获取，如果表中并未有对应记录，则把cRecordVO数据转换一下放入预报单表中。
	 * 如果未锁定，则按现有业务进行操作。yudong.xu 2016.5.13
	 * @param lockStatus
	 * @param orderType
	 * @param activityId
	 * @param groupUUID
	 * @param cRecordVO
	 * @return
     * @throws Exception
     */
	private Map<String,Object> getForecastMap(String lockStatus,Integer orderType,Long activityId,
											  String groupUUID,CostRecordVO cRecordVO)throws Exception {
		Map<String,Object> resultMap = null;
		if ("10".equals(lockStatus)){
			resultMap = foreCastService.getForeCast(orderType,activityId,groupUUID);
			//0581 需求 将拉美图的操作人写死为陈卡卡       add by gaoyang 20170321
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())){
				resultMap.put("createByLeader", "陈卡卡");
				resultMap.put("createByLeaderFull", "陈卡卡");
			}
			if (null == resultMap){
				resultMap = castCostRecordVO2Map(cRecordVO, orderType, 0);
				resultMap.put("remark", "");
				foreCastService.saveForeCastByMap(orderType,activityId,groupUUID,resultMap);
			}
		}else {
			resultMap = castCostRecordVO2Map(cRecordVO, orderType, 0);
			//获取备注,仅针对拉美途
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())) {
				ForeCast simpleForeCast = foreCastService.getSimpleForeCast(orderType, activityId, groupUUID);
				if(null != simpleForeCast) {
					resultMap.put("remark", StringUtils.isNotBlank(simpleForeCast.getRemark())? simpleForeCast.getRemark():"");		
				}else {
					resultMap.put("remark", "");
				}
			}			
		}
		return resultMap;
	}

	//获取结算单的数据，方式同上
	private Map<String,Object> getSettleMap(String lockStatus,Integer orderType,Long activityId,
											  String groupUUID,CostRecordVO cRecordVO)throws Exception {
		Map<String,Object> resultMap = null;
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if ("1".equals(lockStatus)){
			resultMap = settleService.getSettle(orderType,activityId,groupUUID);
			//0581 需求 将拉美图的操作人写死为陈卡卡       add by gaoyang 20170321
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(UserUtils.getUser().getCompany().getUuid())){
				resultMap.put("createByLeader", "陈卡卡");
				resultMap.put("createByLeaderFull", "陈卡卡");
			}
			if (null == resultMap){
				resultMap = settleService.castCostRecordVO2Map(cRecordVO, orderType, 1);
				resultMap.put("remark", "");
				settleService.saveSettleByMap(orderType,activityId,groupUUID,resultMap);
			}
		}else {
			resultMap = settleService.castCostRecordVO2Map(cRecordVO, orderType, 1);
			//获取备注,仅针对拉美途
			if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				Settle simpleSettle = settleService.getSimpleSettleObj(orderType, activityId, groupUUID);
				if(null != simpleSettle) {
					resultMap.put("remark", StringUtils.blankReturnEmpty(simpleSettle.getRemark()));
				}else {
					resultMap.put("remark", "");
				}				
			}
		}
		resultMap.put("isHQX", Context.SUPPLIER_UUID_HQX.equals(companyUuid));
		resultMap.put("isYYJQ", Context.SUPPLIER_UUID_YYJQ.equals(companyUuid));
		return resultMap;
	}
	/**
	 * 批量打印查询
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
	@RequestMapping(value="batchPayment")
	public String batchpayment(Model model,HttpServletRequest request, HttpServletResponse response){
		String params = request.getParameter("params");
		JSONArray array = JSONArray.parseArray(params);
		List<Map<String,Object>> ids = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0 ; i < array.size() ; i ++){
			Map<String,Object> orderIds = new HashMap<String, Object>();
			Map<String,Object> resultData = new HashMap<String, Object>();
			JSONObject object = array.getJSONObject(i);
			Long id = object.getLong("id");
			orderIds.put("id", id);
			Integer orderType = object.getInteger("orderType");
			orderIds.put("orderType", orderType);
			ids.add(orderIds);
			if(null == id){
				return null;
			}
			if(orderType != 11 && orderType != 12){
				resultData.put("group", true);
				Integer isNew = object.getInteger("isNew");
				CostRecord costRecord = costManageService.findOne(id);
				if(null == costRecord){
					return null;
				}
				if(null == costRecord.getPrintTime()) {
					resultData.put("firstPrintTime", new Date());
				}else{
					resultData.put("firstPrintTime", costRecord.getPrintTime());
				}

				String payId = request.getParameter("payId");
				//点击原有成本列表记录的打印按钮
				printbyOptionOrder(model, costRecord);
				//将attribute中的值转成map
				Map<String, Object> map = model.asMap();
				Set<String> set = map.keySet();
				for(String key:set){
					resultData.put(key, map.get(key));
				}
				resultData.put("accountName", "");

				if(Context.ORDER_TYPE_JP == orderType) {
					ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(costRecord.getActivityId());
					resultData.put("groupCode", activityAirTicket.getGroupCode());
				}else if(Context.ORDER_TYPE_QZ == orderType) {
					VisaProducts visaPorduct = visaProductsDao.findOne(costRecord.getActivityId());
					resultData.put("groupCode", visaPorduct.getGroupCode());
				}else {
					ActivityGroup activityGroup = activityGroupDao.findOne(costRecord.getActivityId());
					resultData.put("groupCode", activityGroup.getGroupCode());
				}

				String companyUuid = UserUtils.getUser().getCompany().getUuid();
				//新审核数据单据人员信息抓取20151205
				String deptmanager = ""; //部门主管
				String manager = "";	//总经理
				String finance = "";	//财务主管
				String cashier = "";	//出纳
				String reviewer = "" ;	//审核
				if(isNew == 2) {
					String reviewId = costRecord.getPayReviewUuid();
					//获取单据审批人员Map
					MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUuid, ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, reviewId);
					List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);//主管
					List<User> managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);//总经理
					List<User> finances = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);//财务主管
					List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);//出纳
					List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);//审核
					deptmanager = UserUtils.getUserNames(executives);//主管
					manager = UserUtils.getUserNames(managers);//总经理
					finance = UserUtils.getUserNames(finances);//财务主管
					cashier = UserUtils.getUserNames(cashiers);//出纳
					reviewer = UserUtils.getUserNames(reviewers);//审核
				}else {
					//支出凭单获取人员信息20150505，根据付款审核流程
					reviewer = reviewService.getPayReviewPerson(costRecord.getId(), 9) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 9);
					manager = reviewService.getPayReviewPerson(costRecord.getId(), 10) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 10);
					deptmanager = reviewService.getPayReviewPerson(costRecord.getId(), 4) == null ? "" : reviewService.getPayReviewPerson(costRecord.getId(), 4);
					//青岛凯撒需要在单据上显示出纳，20150710
					if(Context.SUPPLIER_UUID_QDKS.equals(companyUuid)) {
						cashier = reviewService.getPayReviewPerson(costRecord.getId(), 6);
					}
				}
				resultData.put("cashier", cashier);
				resultData.put("deptmanager", deptmanager);
				resultData.put("reviewer", reviewer);
				resultData.put("manager", manager);
				resultData.put("finance", finance);
				resultData.put("id",id);
				resultData.put("orderType",orderType);
				resultData.put("option", "order");
				resultData.put("payId", payId);
				resultData.put("money",costRecord.getName());
				resultData.put("isNew",isNew);
				resultData.put("person",costRecord.getCreateBy().getName());
				resultData.put("currencyId", costRecord.getCurrencyId());
				resultData.put("price", costRecord.getPrice());
				resultData.put("quantity", costRecord.getQuantity());
				resultData.put("createDate", costRecord.getCreateDate());
				resultData.put("supplyName", costRecord.getSupplyName());
				resultData.put("conDate",costRecord.getUpdateDate());
				resultData.put("bankName", costRecord.getBankName());
				resultData.put("bankAccount", costRecord.getBankAccount());
				//20150720修改支出凭单摘要
				resultData.put("remark",costRecord.getComment());

				//当前批发商的美元、加元汇率（目前环球行）
				List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
				resultData.put("curlist", currencylist);
				//20151012增加付款状态，判断财务撤销确认付款后不显示确认付款时间
				resultData.put("payStatus", costRecord.getPayStatus());
				//20151016环球行、拉美途客户确认到账时间为空
				if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
					resultData.put("isHQX", true);//环球行
				}else if(Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)){
					resultData.put("isLMT", true);//拉美途
				}
				//20151023汇率取值规则更改为成本录入时的汇率值
				resultData.put("rate", MoneyNumberFormat.fmtMicrometer(costRecord.getRate().toString(), "#,##0.0000"));
			}else{
				resultData.put("group", false);
				CostRecordHotel costRecordHotel = null;
				CostRecordIsland costRecordIsland = null;
				if (orderType == Context.ORDER_TYPE_HOTEL) {
					costRecordHotel = costRecordHotelDao.findOne(id);
					if (null==costRecordHotel) {
						return null;
					}else {
						if(costRecordHotel.getPrintTime() == null) {
							resultData.put("firstPrintTime", new Date());
						}else{
							resultData.put("firstPrintTime", costRecordHotel.getPrintTime());
						}
					}

				}else if (orderType == Context.ORDER_TYPE_ISLAND) {
					costRecordIsland = costRecordIslandDao.findOne(id);
					if (null==costRecordIsland) {
						return null;
					}else {
						if(costRecordIsland.getPrintTime() == null) {
							resultData.put("firstPrintTime", new Date());
						}else{
							resultData.put("firstPrintTime", costRecordIsland.getPrintTime());
						}
					}
				}


				//---------?????------------
				PayInfoDetail payInfoDetail = null;
				if (orderType == Context.ORDER_TYPE_HOTEL) {
					payInfoDetail = refundService.getPayInfoByPayId(costRecordHotel.getSerialNum(),String.valueOf(orderType));
				}else if (orderType == Context.ORDER_TYPE_ISLAND) {
					payInfoDetail = refundService.getPayInfoByPayId(costRecordIsland.getSerialNum(),String.valueOf(orderType));
				}
				resultData.put("pay", payInfoDetail);
				//取实际付款金额（单价*数量）20150420
				BigDecimal price = null;
				BigDecimal quantity=null;
				BigDecimal currentAmount = null;
				if (orderType == Context.ORDER_TYPE_HOTEL) {
					price = costRecordHotel.getPrice();
					quantity = BigDecimal.valueOf(costRecordHotel.getQuantity());
					currentAmount = price.multiply(quantity);
					ActivityHotelGroup activityHotelGroup = activityHotelGroupDao.getByUuid(costRecordHotel.getActivityUuid());
					resultData.put("groupCode", activityHotelGroup.getGroupCode());
				}else if (orderType == Context.ORDER_TYPE_ISLAND) {
					price = costRecordIsland.getPrice();
					quantity = BigDecimal.valueOf(costRecordIsland.getQuantity());
					currentAmount = price.multiply(quantity);
					ActivityIslandGroup activityIslandGroup = activityIslandGroupDao.getByUuid(costRecordIsland.getActivityUuid());
					resultData.put("groupCode", activityIslandGroup.getGroupCode());
				}

				if (orderType == Context.ORDER_TYPE_HOTEL) {
					resultData.put("money",costRecordHotel.getName());
					if (null!=costRecordHotel.getCreateBy()) {
						resultData.put("person",costRecordHotel.getCreateBy().getName());
					}else {
						resultData.put("person","");
					}

					resultData.put("currencyId", costRecordHotel.getCurrencyId());
					resultData.put("price", costRecordHotel.getPrice());
					resultData.put("quantity", costRecordHotel.getQuantity());
					resultData.put("createDate", costRecordHotel.getCreateDate());
					resultData.put("supplyName", costRecordHotel.getSupplyName());
					resultData.put("bankName", costRecordHotel.getBankName());
					resultData.put("bankAccount", costRecordHotel.getBankAccount());
					resultData.put("rate", costRecordHotel.getRate());



					String totalRMB = costRecordHotel.getPriceAfter().toString();//.multiply(costRecordHotel.getRate()).toString();
					//加元  或 美元
					resultData.put("originMoney",costRecordHotel.getPrice().multiply(new BigDecimal(costRecordHotel.getQuantity())));

					resultData.put("totalRMB", MoneyNumberFormat.fmtMicrometer(totalRMB, MoneyNumberFormat.THOUSANDST_POINT_TWO));
					resultData.put("totalRMBDX", MoneyNumberFormat.digitUppercase(Double.parseDouble(totalRMB)));
				}else if (orderType == Context.ORDER_TYPE_ISLAND) {
					resultData.put("money",costRecordIsland.getName());
					if (null!=costRecordIsland.getCreateBy()) {
						resultData.put("person",costRecordIsland.getCreateBy().getName());
					}else {
						resultData.put("person","");
					}

					resultData.put("currencyId", costRecordIsland.getCurrencyId());
					resultData.put("price", costRecordIsland.getPrice());
					resultData.put("quantity", costRecordIsland.getQuantity());
					resultData.put("createDate", costRecordIsland.getCreateDate());
					resultData.put("supplyName", costRecordIsland.getSupplyName());
					resultData.put("bankName", costRecordIsland.getBankName());
					resultData.put("bankAccount", costRecordIsland.getBankAccount());
					resultData.put("rate", costRecordIsland.getRate());

//					Currency currency = currencyService.findCurrency(Long.parseLong(costRecordIsland.getCurrencyId()+""));

					String totalRMB = costRecordIsland.getPriceAfter().toString();//.multiply(currency.getConvertLowest()).toString();//加元  或 美元
					resultData.put("originMoney",costRecordIsland.getPrice().multiply(new BigDecimal(costRecordIsland.getQuantity())));

					resultData.put("totalRMB", MoneyNumberFormat.fmtMicrometer(totalRMB, MoneyNumberFormat.THOUSANDST_POINT_TWO));
					resultData.put("totalRMBDX", MoneyNumberFormat.digitUppercase(Double.parseDouble(totalRMB)));

				}

				resultData.put("conDate",payInfoDetail.getCreateDate());
				resultData.put("currencyMoney", currentAmount);
				resultData.put("id",id);
				resultData.put("orderType",orderType);
				//当前批发商的美元、加元汇率（目前环球行）
				List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
				resultData.put("curlist", currencylist);

			}
			list.add(resultData);
		}
		model.addAttribute("list", list);
		model.addAttribute("ids",JSON.toJSONString(ids));
		return "modules/cost/batchPayment";
	}

	/**
	 * 批量更新打印状态以及打印时间，如果是已打印状态则不进行更新操作
	 * @param request
	 * @param response
	 * @param model
	 */
	@ResponseBody
	@RequestMapping(value="batchUpdatePrinted")
	public void batchUpdatePrinted(HttpServletRequest request, HttpServletResponse response, Model model){
		String json = "";
		Long id = null;
		String idValue = request.getParameter("ids");
		JSONArray array = JSONArray.parseArray(idValue);
		for(int i = 0 ; i <array.size() ; i ++){
			JSONObject object = array.getJSONObject(i);
			id = object.getLong("id");
			Integer orderType = object.getInteger("orderType");
			if(orderType != 11 && orderType != 12){
				if(id != null){
					//id = Long.valueOf(idValue);
					Date date = updateOrderPayPrinted(id);
					if(null != date){
						String dateStr = DateUtils.date2String(new Date(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
						json = "{\"flag\":\"success\",\"msg\":\""+dateStr+"\"}";
					}
				}else{
					json = "{\"flag\":\"fail\",\"msg\":\"id的值应该是数值，请检查\"}";
				}
			}else{
				if(id != null && orderType != null){
					Date date = updateOrderPayPrintedForHotelAndIsland(id,orderType);
					if(null != date){
						String dateStr = DateUtils.date2String(new Date(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
						json = "{\"flag\":\"success\",\"msg\":\""+dateStr+"\"}";
					}else {
						json = "{\"flag\":\"success\",\"msg\":\""+null+"\"}";
					}

				}else{
					json = "{\"flag\":\"fail\",\"msg\":\"id的值应该是数值，请检查\"}";
				}
			}
		}
		ServletUtil.print(response, json);
	}
}
