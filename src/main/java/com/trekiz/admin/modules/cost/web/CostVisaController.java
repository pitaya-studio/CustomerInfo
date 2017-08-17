package com.trekiz.admin.modules.cost.web;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.stock.repository.IStockDao;
import com.trekiz.admin.modules.sys.entity.*;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.entity.Visafile;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaPublicBulletinService;
import com.trekiz.admin.modules.visa.service.VisaService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
/**
  文件名: VisaProductController.java
 *  功能:
 *  签证产品预订
 *  修改记录:   
 *  
 *  @DateTime 2014-9-22 下午2:36:17
 *  @version 1.5
 */
@Controller
@RequestMapping(value = "${adminPath}/cost/visa")
public class CostVisaController extends BaseController {
	 private static final Log LOG = LogFactory.getLog(CostManagerController.class);
	 public static final Integer  VISA_ORDER_TYPE=6;
	 public static final Integer  MONY_TYPE=14; // 结算价
	 public static final Integer  BUSINDESS_TYPE_ORDER=1;//1表示订单，2表示游客
	 public static final Integer  BUSINDESS_TYPE_TRAVELER=2;//1表示订单，2表示游客

	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private CostManageService costManageService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private DictService dictService;
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private OrderCommonService orderCommonService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private ReviewService reviewService;
	 
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private VisaPublicBulletinService visaPublicBulletinService;
	@Autowired
	private IStockDao stockDao;
	@Autowired
	private CostRecordDao costRecordDao;
	
	/**
	 * 签证产品预定列表list
	 * @param visaProducts
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(@ModelAttribute VisaProducts visaProducts,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String productName = request.getParameter("productName");
		String collarZoning = request.getParameter("collarZoning");
		String sysCountryId = request.getParameter("sysCountryId");
		String visaType = request.getParameter("visaType");
		String commitType = request.getParameter("commitType");
		String operator = request.getParameter("operator");
		String isReject = request.getParameter("isReject"); 	// 540 签证页面添加驳回筛选项 王洋 2017.3.22
		
		User user = UserUtils.getUser();
		visaProducts.setProductStatus(new Integer(Context.PRODUCT_ONLINE_STATUS));//
		//批发商uuid
		String companyUuid = user.getCompany().getUuid();
		Map<String, Object> params = new HashMap<String, Object>();
		//签证产品沈阳分公司仅看到自己的成本录入
		boolean isShenYangDept= false;
		if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
		  List<Department> deptList = UserUtils.getUserDept();
		  for (Department dept : deptList) { 
			  if (39 == dept.getId() || 39 == dept.getParentId()) {
				  isShenYangDept=true;
				  break;
			  }
		  }
		}
		params.put("productName", productName);
		params.put("collarZoning", collarZoning);
		params.put("sysCountryId", sysCountryId);
		params.put("visaType", visaType);
		params.put("commitType", commitType);
		params.put("isShenYangDept", isShenYangDept);
		params.put("operator", operator);
		params.put("isReject", isReject);	// 540 签证页面添加驳回筛选项 王洋 2017.3.22
		Page<Map<String, Object>> page = visaProductsService.findVisaProductsReviewPage(
						new Page<Map<String, Object>>(request, response), visaProducts, params);

		//天马运通显示预计总成本、实际总成本
		if(Context.SUPPLIER_UUID_TMYT.equals(companyUuid)) {
			//天马运通/热典好运通uuid
			model.addAttribute("TMYT", true);
			for (Map<String, Object> map : page.getList()) {
				String id = map.get("id").toString();
				List<Map<String, Object>> budgetCost=costManageService.getCost(Long.parseLong(id),6,0);
				List<Map<String, Object>> actualCost=costManageService.getCost(Long.parseLong(id),6,1);
				BigDecimal budgetTotal = OrderCommonUtil.getSum(budgetCost, "cost");
				map.put("budgetTotal", budgetTotal);
				BigDecimal actualTotal = OrderCommonUtil.getSum(actualCost, "cost");
				map.put("actualTotal", actualTotal);
			}
		}

		List<Country> countryList = CountryUtils.getCountrys();
		List<Currency> currencylist = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("curlist", currencylist);
		model.addAttribute("visaCountryList", countryList);
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));
		 // 鼎鸿假期添加筛选条件  for 0416 by jinxin.gao
	    boolean DHJQ = false;
	    if(Context.SUPPLIER_UUID_DHJQ.equals(companyUuid)){
		   DHJQ = true;
	    }
	    model.addAttribute("DHJQ", DHJQ);
	    model.addAttribute("params", params);
		model.addAttribute("page", page);

		Menu menu = departmentService.getMenuByUrl(request);
        if (menu != null) {
        	request.setAttribute("_m", menu.getParent().getParent() != null ? menu.getParent().getParent().getId() : null);
        	request.setAttribute("_mc", menu.getParent().getId());
        }
		return "modules/cost/visaList";
	}
	
	//提交成本录入
	@ResponseBody
	@RequestMapping(value="submitvisa", method= RequestMethod.POST)
	public String submitvisa(@RequestParam Long id){
		//int review=Context.REVIEW_COST_WAIT;			
		//visaProductsService.submitReview(id, review);
		costRecordDao.submitCostRecord(id,6,4);
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
	@RequestMapping(value="flow/{visaProductId}", method=RequestMethod.GET)
	public String showCurrent(@PathVariable(value="visaProductId") Long visaProductId,			
			HttpServletRequest request, HttpServletResponse response,
			Model model){
		
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
		
		otherCostList = costManageService.findCostRecordList(visaProductId, 2, 0, Context.ORDER_TYPE_QZ);	// 第三个参数overseas默认设置为0
		
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
		List<Map<String, Object>> incomeList=costManageService.getRefunifoForCastList(visaProductId,6);
		List<Map<String, Object>> budgetCost=costManageService.getCost(visaProductId,6,0);
		List<Map<String, Object>> actualCost=costManageService.getCost(visaProductId,6,1);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("budgetCost",budgetCost);
		model.addAttribute("actualCost",actualCost);
		
		model.addAttribute("otherCostList", otherCostList);
		
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		
		model.addAttribute("curlist", currencylist);	
        model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));		
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaCountryList", countryList);	
		
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));
				
		VisaProducts visaProduct = this.visaProductsService.findByVisaProductsId(visaProductId);
		
		Long deptId=visaProduct.getDeptId();
		if (deptId==null){
			deptId=(long)0;
		}
        model.addAttribute("deptId",deptId);
        
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		
		String costSerial= visaProduct.getCost();
		String incomeSerial= visaProduct.getIncome();		
		if(costSerial==null) costSerial="";
		if(incomeSerial==null) incomeSerial="";
		Long companyId= UserUtils.getUser().getCompany().getId();	
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		model.addAttribute("myincome",costManageService.getChajia(incomeSerial,costSerial,companyId));
		model.addAttribute("cost", costManageService.getCurrenySum(costSerial,companyId));
		model.addAttribute("income", costManageService.getCurrenySum(incomeSerial,companyId));
		
		Object budgetrefund = costManageService.getRefundSum(visaProductId, 0, 6).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(visaProductId, 0, 6).get(0).get("totalRefund");
		model.addAttribute("budgetrefund", budgetrefund);
		Object actualrefund = costManageService.getRefundSum(visaProductId, 1, 6).get(0).get("totalRefund") == null ? "0"
				: costManageService.getRefundSum(visaProductId, 1, 6).get(0).get("totalRefund");
		model.addAttribute("actualrefund", actualrefund);

		model.addAttribute("companyId", companyId);
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
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();	
		orderList = visaOrderService.queryVisaOrdersByProductId(visaProductId.toString()); 
		model.addAttribute("orderList", orderList);		
		//价格币种
		model.addAttribute("currencyMark", CurrencyUtils.getCurrencyInfo(visaProduct.getCurrencyId().toString(), 0, "mark"));
		
//		int totalPersonNum = 0;
//		for(int i = 0; i < orderList.size(); i++) {
//			totalPersonNum += Integer.parseInt(orderList.get(i).get("travel_num").toString());
//		}
//		model.addAttribute("totalPersonNum", totalPersonNum);
				
		List<Dict> ordertype=dictService.findByType("order_type");
		model.addAttribute("ordertype", ordertype);	
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("costLog",stockDao.findCostRecordLog(visaProductId, 6));
		if (visaProduct.getCurrencyId() != null) {
			Currency currency = this.currencyService.findCurrency(new Long(
					visaProduct.getCurrencyId()));
			model.addAttribute("currency", currency);
		}
		return "modules/cost/visaDetail";		
	
	}
	
	
		
	/**
	 * 签证产品详情页
	 * @param visaProductId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "visaProductsDetail/{visaProductId}")
	public String visaProductsDetail(@PathVariable String visaProductId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		//办签类型
			
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("visaCountryList", countryList);
		
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));
		
		List<Currency> currencylist = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("curlist", currencylist);

		VisaProducts visaProduct = this.visaProductsService.findByVisaProductsId(new Long(visaProductId));
		
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
		
		model.addAttribute("visaProduct", visaProduct);
		
		if (visaProduct.getCurrencyId() != null) {
			Currency currency = this.currencyService.findCurrency(new Long(
					visaProduct.getCurrencyId()));
			model.addAttribute("currency", currency);
		}
		return "modules/visa/visaProductsPreOrderDetail";
	}
	
	
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
		costRecord.setOrderType(Context.ORDER_TYPE_QZ);
		costRecord.setReview(Context.REVIEW_COST_WAIT);
		costRecord.setPayStatus(0);
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
	
	/**
	 * 更换渠道
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAgentInfo",method=RequestMethod.POST)
	public JSONArray getAgentInfo(HttpServletRequest request, HttpServletResponse response){
		JSONArray results = new JSONArray();
		String jsonRes = request.getParameter("jsonresult");
		Agentinfo info = new Agentinfo();
		
		if(StringUtils.isNotBlank(jsonRes.trim())){
			JSONArray array = JSONArray.fromObject(jsonRes);
			int len = array.size();
			for(int i=0;i<len;i++){
				JSONObject resobj = new JSONObject();
				JSONObject obj = array.getJSONObject(i);
				String agentId = obj.getString("id");
				if(StringUtils.isNotBlank(agentId)){
					info=agentinfoService.findOne(Long.valueOf(agentId));
				}
				if(info!=null){
					resobj.put("agentContact",info.getAgentContact());
					resobj.put("agentTel", info.getAgentTel());
					resobj.put("agentFixedLine", info.getAgentFixedLine());
					resobj.put("agentAddress", info.getAgentAddress());
					resobj.put("agentFax", info.getAgentFax());
					resobj.put("agentEmail", info.getAgentEmail());
					resobj.put("agentQQ", info.getAgentQQ());
				}
				results.add(resobj);
			}
		}
		return results;
	}
	
	/**
	 * 该方法以由  createVisaOrder 代替
	 * 
	 * 签证预定起始页面 
	 * @param visaProduct
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
//	@RequestMapping(value="getOrder/{visaProductId}/{agentinfoId}/{sysCountryId}")
//	public String getOrder(@PathVariable String visaProductId,@PathVariable String sysCountryId,
	@RequestMapping(value="getOrder/{visaProductId}/{agentinfoId}")
	public String getOrder(@PathVariable String visaProductId,@PathVariable String agentinfoId,HttpServletRequest request, HttpServletResponse response, Model model){
		
		VisaProducts visaPro = new VisaProducts();
		Country country = new Country();
		Agentinfo agentinfo = new Agentinfo();
		
		if(visaProductId!=null && StringUtils.isNumeric(visaProductId)){
			visaPro=visaProductsService.findByVisaProductsId(Long.valueOf(visaProductId));
		}else{
			//return "该产品不存在";
			return "redirect:"+Global.getViewPath()+"/error/400.jsp";
		}
		
		if(visaPro.getSysCountryId()!=null && visaPro.getSysCountryId()>0){
			country = CountryUtils.getCountry(Long.valueOf(visaPro.getSysCountryId()));
		}else{
			//return "这个国家没有登记";
			return "redirect:"+Global.getViewPath()+"/error/400.jsp";
		}
		// 判断前台是否设定了渠道商ID(-2表示没有带来任何渠道商信息)
		if(agentinfoId!=null && StringUtils.isNumeric(agentinfoId) && Long.valueOf(agentinfoId)!=-2){
			agentinfo = agentinfoService.findOne(Long.valueOf(agentinfoId));
		}
		
		// 查询该登陆用户对应的渠道商
		ArrayList<Agentinfo> agentinfoList = new ArrayList<Agentinfo>();
		agentinfoList = (ArrayList<Agentinfo>) agentinfoService.findAllAgentinfo();
		// 获取签证类型列表
		Map<String,String> map =DictUtils.getVisaTypeMap("visa_type");
		// 币种类
		Currency currency = new Currency();
		if(visaPro.getCurrencyId()!=null && visaPro.getCurrencyId()>0){
			currency = currencyService.findCurrency(Long.valueOf(visaPro.getCurrencyId()));
		}
		
		model.addAttribute("productId", visaPro.getId());
		model.addAttribute("productName", visaPro.getProductName());
		model.addAttribute("countryName", CountryUtils.getCountryName(Long.valueOf(visaPro.getSysCountryId())));
		model.addAttribute("sysCountryId", visaPro.getSysCountryId());
		model.addAttribute("visaType", map.get(String.valueOf(visaPro.getVisaType())));
//		model.addAttribute("needSpotAudition", visaPro.getNeedSpotAudition()==1?"是":"否");
		model.addAttribute("forecastWorkingTime", visaPro.getForecastWorkingTime());
		model.addAttribute("enterNum", visaPro.getEnterNum());
		model.addAttribute("stayTime", visaPro.getStayTime());
		model.addAttribute("stayTimeUnit", visaPro.getStayTimeUnit());
		model.addAttribute("visaPrice", visaPro.getVisaPrice());
		model.addAttribute("visaPay", visaPro.getVisaPay());
		model.addAttribute("country", country);
		model.addAttribute("agentinfo", agentinfo);
		model.addAttribute("agentinfoList", agentinfoList);
		model.addAttribute("product",visaPro);
		model.addAttribute("currency",currency);
		model.addAttribute("step","1");	// 预定订单第一步
		
		return "modules/visa/visaGetOrderFirst";
	}
	

	
	/**
	 * 保存游客信息
	 * 由  saveTraveler  方法取代
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Deprecated
	@RequestMapping(value="saveTravel")
	public String saveTravel(@RequestParam(value = "fileInfo", required = false) MultipartFile[] files,HttpServletRequest request, HttpServletResponse response, Model model){
		
		// 订单ID
		String orderID = request.getParameter("orderID");
		// 游客类型
		String personTypeinner = request.getParameter("personTypeinner");
		// 游客姓名
		String travelerName = request.getParameter("travelerName");
		// 游客姓名全拼/英文
		String travelerSpell = request.getParameter("travelerSpell");
		// 游客性别
		String travelerSex = request.getParameter("travelerSex");
		// 出生日期
		String brithday = request.getParameter("brithday");
		// 联系电话
		String telephone = request.getParameter("telephone");
		// 护照号码
		String passportCode = request.getParameter("passportCode");
		// 护照有效期
		String passportValidity = request.getParameter("passportValidity");
		// 身份证号码
		String idCard = request.getParameter("idCard");
		// 预计出发时间
		String forecastStartOut = request.getParameter("forecastStartOut");
		
		// 以下为附件名称：
		// 护照首页
		/*
		String passportPhotoId = request.getParameter("passportPhotoId");
		// 身份证正面
		String identityFrontPhotoId = request.getParameter("identityFrontPhotoId");
		// 报名表
		String tablePhotoId = request.getParameter("tablePhotoId");
		// 照片
		String personPhotoId = request.getParameter("personPhotoId");
		// 身份证反面
		String identityBackPhotoId = request.getParameter("identityBackPhotoId");
		// 其它
		String otherPhotoId = request.getParameter("otherPhotoId"); */
		
		// 游客类
		Traveler traveler = new Traveler();
		// 签证类
		Visa visa = new Visa();
		
		if(StringUtils.isNumeric(personTypeinner) && StringUtils.isNotBlank(travelerName) && StringUtils.isNotBlank(travelerSpell)
				&& StringUtils.isNumeric(travelerSex) && StringUtils.isNotBlank(brithday) && StringUtils.isNumeric(telephone)
				&& StringUtils.isNotBlank(passportCode) && StringUtils.isNotBlank(passportValidity) && StringUtils.isNotBlank(idCard)
				&& StringUtils.isNumeric(orderID)){
			
			// 创建游客信息
			traveler.setOrderId(Long.valueOf(orderID));
			traveler.setName(travelerName);
			traveler.setNameSpell(travelerSpell);
			traveler.setSex(Integer.valueOf(travelerSex));
			traveler.setBirthDay(DateUtils.dateFormat(brithday));
			traveler.setTelephone(telephone);
			traveler.setPassportCode(passportCode);
			traveler.setPassportValidity(DateUtils.dateFormat(passportValidity));
			traveler.setIdCard(idCard);
			traveler.setPapersType(Context.PAPERSTYPE_1); // 锁定证件为身份证
			
			Traveler backtravel = orderCommonService.saveTraveler(traveler); // 保存游客信息
			
			// 创建签证信息
			visa.setTravelerId(backtravel.getId());
			visa.setVisaStauts(Integer.valueOf(Context.VISA_STATUTS_TO));
			visa.setForecastStartOut(DateUtils.dateFormat(forecastStartOut));
			Visa backVisa = visaService.saveVisa(visa);
			
			// 保存签证信息附件
			if(backVisa!=null && backVisa.getId()>0){
				Iterable<Visafile> iter = visaService.visaFile(files, backVisa.getId());
				// 将附件ID放入Visa类中
				Iterator<Visafile> iterator = iter.iterator();
				while(iterator.hasNext()){
					//Visafile file = iterator.next();
				//	backVisa.set
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取签证类型列表
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getVisaTypeMap",method=RequestMethod.POST)
	public JSONArray getVisaTypeMap(HttpServletRequest request, HttpServletResponse response) {
		JSONArray results = new JSONArray();
		Map<String,String> map =DictUtils.getVisaTypeMap("visa_type");
		
		JSONObject resobj = new JSONObject();
		if(map!=null){
			Iterator<Entry<String, String>> iter = map.entrySet().iterator();
			while(iter.hasNext()){
				 Entry<String, String> entry = iter.next();
				 resobj.put(entry.getKey(), entry.getValue());
				 results.add(resobj);
			}
		}
		return results;
	}
	
	/**
	 * 获取渠道信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "agentList")
	public Map<String, Object> getAgentInfoJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();// 渠道信息列表
		map.put("agentList", agentInfos);
		return map;
	}
	
	
	
	
	/**
	 * 订单的第一个页面  产品基本信息 和  联系人信息
	 * 进入创建visa订单首页  传递 visa产品id、渠道id、占位类型
	 * @param visaProductId
	 * @param agentinfoId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
//	@RequestMapping(value="getOrder/{visaProductId}/{agentinfoId}/{sysCountryId}")
//	public String getOrder(@PathVariable String visaProductId,@PathVariable String sysCountryId,
	@RequestMapping(value="createVisaOrder/{visaProductId}/{agentinfoId}/{payType}")
	public String createVisaOrder(@PathVariable String visaProductId,@PathVariable String agentinfoId,@PathVariable String payType,HttpServletRequest request, HttpServletResponse response, Model model){
		
		VisaProducts visaPro = new VisaProducts();
	    Country country = new Country();
		Agentinfo agentinfo = new Agentinfo();
		
		if(visaProductId!=null && StringUtils.isNumeric(visaProductId)){
			visaPro=visaProductsService.findByVisaProductsId(Long.valueOf(visaProductId));
		}else{
			//return "该产品不存在";
			return "redirect:"+Global.getViewPath()+"/error/400.jsp";
		}
		
		if(visaPro.getSysCountryId()!=null && visaPro.getSysCountryId()>0){
			country = CountryUtils.getCountry(Long.valueOf(visaPro.getSysCountryId()));
		}else{
			//return "这个国家没有登记";
			return "redirect:"+Global.getViewPath()+"/error/400.jsp";
		}
		// 判断前台是否设定了渠道商ID(-2表示没有带来任何渠道商信息)
		if(agentinfoId!=null && StringUtils.isNumeric(agentinfoId) && Long.valueOf(agentinfoId)!=-2){
			agentinfo = agentinfoService.findOne(Long.valueOf(agentinfoId));
		}
		
		// 查询该登陆用户对应的渠道商
		ArrayList<Agentinfo> agentinfoList = new ArrayList<Agentinfo>();
		agentinfoList = (ArrayList<Agentinfo>) agentinfoService.findAllAgentinfo();
		// 获取签证类型列表
		//Map<String,String> map =DictUtils.getVisaTypeMap("visa_type");
		// 币种类
		Currency currency = new Currency();
		if(visaPro.getCurrencyId()!=null && visaPro.getCurrencyId()>0){
			currency = currencyService.findCurrency(Long.valueOf(visaPro.getCurrencyId()));
		}
		
		//获取币种信息
		setCurrencyModel(model);
		//签证类型List
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
		
		model.addAttribute("payType",payType);
		
		model.addAttribute("visaProductId", visaProductId);//渠道ID 
		model.addAttribute("agentId", agentinfoId);//渠道ID 
		model.addAttribute("valid_period", visaPro.getValid_period());
		model.addAttribute("needSpotAudition", visaPro.isNeedSpotAudition()?"1":"0");
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));
		model.addAttribute("productId", visaPro.getId());
		model.addAttribute("productName", visaPro.getProductName());
		model.addAttribute("countryName", CountryUtils.getCountryName(Long.valueOf(visaPro.getSysCountryId())));
		model.addAttribute("sysCountryId", visaPro.getSysCountryId());
		//model.addAttribute("visaType", map.get(String.valueOf(visaPro.getVisaType())));
		model.addAttribute("visaType", visaPro.getVisaType());
		model.addAttribute("collarZoning", visaPro.getCollarZoning());
//		model.addAttribute("needSpotAudition", visaPro.getNeedSpotAudition()==1?"是":"否");
		model.addAttribute("forecastWorkingTime", visaPro.getForecastWorkingTime());
		model.addAttribute("enterNum", visaPro.getEnterNum());
		model.addAttribute("stayTime", visaPro.getStayTime());
		model.addAttribute("stayTimeUnit", visaPro.getStayTimeUnit());
		model.addAttribute("visaPrice", visaPro.getVisaPrice());
		model.addAttribute("visaPay", visaPro.getVisaPay());
		model.addAttribute("country", country);
		model.addAttribute("agentinfo", agentinfo);
		model.addAttribute("agentinfoList", agentinfoList);
		model.addAttribute("product",visaPro);
		model.addAttribute("currency",currency);
		model.addAttribute("step","1");	// 预定订单第一步
		//办签资料相关：
		model.addAttribute("originalProjectType",visaPro.getOriginal_Project_Type());
		model.addAttribute("originalProjectName",visaPro.getOriginal_Project_Name());
		model.addAttribute("copyProjectType",visaPro.getCopy_Project_Type());
		model.addAttribute("copyProjectName",visaPro.getCopy_Project_Name());
		
		//获取护照类型信息
        Map<String,String> passportTypeList = DictUtils.getVisaTypeMap(Context.PASSPORT_TYPE);
        model.addAttribute("passportTypeList", passportTypeList);
		
		/**
		 * ==================
		 * 签证子接口测试
		 * for testing   
		 */
		//VisaProducts visaProducts = visaProductsService.findVisaProductsByCountryTypeCollarZonID(41, 2, "19");
		//System.out.println(visaProducts.getProductName());
		//构造测试数据
		//ProductOrderCommon  productOrderCommon  = orderCommonService.getProductorderById(Long.parseLong("4015"));
		//Traveler traveler = travelerService.findTravelerByOrderIdAndOrderType(Long.parseLong("3964"), 2).get(0);
		
		/*passport_photo_id护照首页ID
	     *       identity_front_photo_id身份证正面ID
	     *       identity_back_photo_id身份证背面ID
	     *       table_photo_id报名表ID
	     *       person_photo_id照片ID
	     *       other_photo_id*/
		Map<String, Long> travelerDocInfoIds = new HashMap<String, Long>();
		travelerDocInfoIds.put("passport_photo_id", Long.parseLong("5370"));
		travelerDocInfoIds.put("identity_front_photo_id",  null);
		travelerDocInfoIds.put("identity_back_photo_id",  Long.parseLong("4371"));
		travelerDocInfoIds.put("table_photo_id",  null);
		//travelerDocInfoIds.put("person_photo_id",  Long.parseLong(s));
		//travelerDocInfoIds.put("other_photo_id",  Long.parseLong(s));
		
	/*	visaOrderService.subVisaOrderCreate(DateUtils.dateFormat("2014-12-08"), 
				DateUtils.dateFormat("2014-12-08"), 41, 2, "19"
				, productOrderCommon,
				traveler, travelerDocInfoIds);*/
		//-----------------以上为子订单接口测试------------------
		
		return "modules/visa/visaOrderFirst";
	}
	

	
	
	
	
    /**
     *  获取币种相关信息
	 *
	 *  @author xinwei.wang
	 *  @DateTime 2014-11-15 上午10:33:00
	 *  @param model
     */
	private void setCurrencyModel(Model model){
	    //获取币种信息
        List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
        StringBuilder sbBuilder = new StringBuilder();
        
        for(Currency curr:currencyList){
        	sbBuilder.append("<option value=\"").append(curr.getId()).append("\">").append(curr.getCurrencyName()).append("</option>");
        }
        model.addAttribute("currencyList", sbBuilder.toString());
        JsonConfig currencyconfig = new JsonConfig();
        currencyconfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				if (arg1.equals("id") || arg1.equals("currencyName")|| arg1.equals("currencyMark")) {
					return false;
				} else {
					return true;
				}
			}
		});
        net.sf.json.JSONArray currencyListJsonArray = net.sf.json.JSONArray.fromObject(currencyList, currencyconfig);
        model.addAttribute("currencyListJsonArray", currencyListJsonArray.toString());
	}

}
