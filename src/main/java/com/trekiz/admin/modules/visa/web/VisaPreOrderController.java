package com.trekiz.admin.modules.visa.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.service.SupplyContactsService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.service.CostchangeService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.service.SupplierContactsService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.entity.VisaPublicBulletin;
import com.trekiz.admin.modules.visa.entity.Visafile;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsFileService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaPublicBulletinService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.modules.visa.utils.Items;
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
@RequestMapping(value = "${adminPath}/visa/preorder") 
public class VisaPreOrderController extends BaseController {
	
	 public static final Integer  VISA_ORDER_TYPE=6;
	 public static final Integer  BUSINDESS_TYPE_ORDER=1;//1表示订单，2表示游客
	 public static final Integer  BUSINDESS_TYPE_TRAVELER=2;//1表示订单，2表示游客
	
	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private OrderCommonService orderCommonService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private SysIncreaseService sysIncreaseService;
	
	 @Autowired
	private CostchangeService costchangeService;
	 
	@Autowired
	private MoneyAmountService moneyAmountService;
	
    @Autowired
    private TravelerService travelerService;
    
    @Autowired
    private VisaPublicBulletinService visaPublicBulletinService;
    
    @Autowired
	private VisaProductsFileService visaProductsFileService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;
	
	@Autowired
	private SupplierContactsService supplierContactsService;
	@Autowired
	private SupplyContactsService supplyContactsService;
	@Autowired
	private DocInfoService docInfoService ;
	

	/**
	 * 签证产品列表展示(根据条件筛选)
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"listBack"})
//	public String list(@RequestParam("productName") String productName,@RequestParam("collarZoning") String collarZoning,
	public String listBack(HttpServletRequest request, HttpServletResponse response, Model model){
		
		//Long companyId = UserUtils.getUser().getCompany().getId();
		
		String sysCountryId = StringUtils.isNotBlank(request.getParameter("sysCountryId"))?request.getParameter("sysCountryId"):null;
		String collarZoning = StringUtils.isNotBlank(request.getParameter("collarZoning"))?request.getParameter("collarZoning"):null;
		String visaType = StringUtils.isNotBlank(request.getParameter("visaType"))?request.getParameter("visaType"):null;
		String orderBy = StringUtils.isNotBlank(request.getParameter("orderBy"))?request.getParameter("orderBy"):null;
		// 获取签证国家列表
		
		// 获取签证类别列表
		
		// 获取领区列表
		
		VisaProducts visaProducts = new VisaProducts();
//		if(StringUtils.isNotBlank(productName)){
//			visaProducts.setProductName(productName);
//		}
		if(StringUtils.isNumeric(sysCountryId)){
			visaProducts.setCurrencyId(Integer.valueOf(sysCountryId));
		}
		if(StringUtils.isNotBlank(collarZoning)){
			visaProducts.setCollarZoning(collarZoning);
		}
		if(StringUtils.isNumeric(visaType)){
			visaProducts.setVisaType(Integer.valueOf(visaType));
		}
		if(StringUtils.isBlank(orderBy)){
			orderBy = "visa_pay";
		}
		DepartmentCommon common = departmentService.setDepartmentPara("visaBookOrder", model);
		// 获取分页数据
		Page<VisaProducts> page = visaProductsService.findVisaProductsPage(new Page<VisaProducts>(request,response), visaProducts, null, collarZoning, sysCountryId, visaType, orderBy, common);
		model.addAttribute("page",page);
		//model.addAttribute("productName",productName);
		model.addAttribute("sysCountryId",sysCountryId);
		model.addAttribute("collarZoning",collarZoning);
		model.addAttribute("visaType",visaType);
		model.addAttribute("orderBy",orderBy);
		
		return "modules/visa/visaProductList";
	}
	
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
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		String productName = request.getParameter("productName");//产品名称
		String collarZoning = request.getParameter("collarZoning");//领区
		String sysCountryId = request.getParameter("sysCountryId");//
		         sysCountryId = " ".equals(sysCountryId)?null:sysCountryId;
		String visaProductIds = request.getParameter("visaProductIds");//签证产品ID
		String visaType = request.getParameter("visaType");//签证类型
		String orderBy = request.getParameter("orderBy");//
		
		String isShowSearch = request.getParameter("showFlag");//是否显示搜索条件的关闭与收起
		
		
		/*
		 * if (StringUtils.isBlank(orderBy)) { orderBy = "pro.id DESC"; }
		 */
		visaProducts.setProductStatus(new Integer(Context.PRODUCT_ONLINE_STATUS));// 

		User user = UserUtils.getUser();
		DepartmentCommon common = departmentService.setDepartmentPara("visaBookOrder", model);
		
		Page<VisaProducts> page = this.visaProductsService
				.findVisaProductsPage4PreOrder(
						new Page<VisaProducts>(request, response),
						visaProducts, productName, collarZoning, sysCountryId,
						visaType, orderBy, common);

	   //	List<Country> countryList = CountryUtils.getCountrys();
		
		List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("curlist", currencylist);

		//model.addAttribute("visaCountryList", countryList);
		List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
		model.addAttribute("countryInfoList", countryInfoList);
		
		if(StringUtils.isNotBlank(sysCountryId)) {
			model.addAttribute("collarZoningList", getArea(sysCountryId));
		}
		
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));

		model.addAttribute("page", page);
		model.addAttribute("productName", productName);
		model.addAttribute("collarZoning", collarZoning);
		model.addAttribute("sysCountryId", sysCountryId);
		model.addAttribute("visaType", visaType);
		model.addAttribute("visaProductIds", visaProductIds);
		model.addAttribute("orderBy", orderBy);
		
		
		//User user = UserUtils.getUser();
		//model.addAttribute("agentinfoList", agentinfoService.findAgentBySalerId(user.getId()));
		
		VisaPublicBulletin  visaPublicBulletin = visaPublicBulletinService.findByVisaPublicBulletinForOne();
		model.addAttribute("visaPublicBulletin", visaPublicBulletin);
		
		List<Agentinfo> agentInfoList = agentinfoService.findAllAgentinfo();
		model.addAttribute("agentinfoList", agentInfoList);
		//model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		//处理筛选自动关闭的
		model.addAttribute("flag", isShowSearch);
		model.addAttribute("userId", user.getId());
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		Long queryCommonOrderList = UserUtils.getUser().getCompany().getQueryCommonOrderList(); //是否查看明细
		model.addAttribute("queryCommonOrderList",queryCommonOrderList);

		return "modules/visa/visaProductPreOrderList";
	}
	
	/**
	 * 查询签证国家的领区
	 * @DateTime 2014-12-3 下午01:59:39
	 * @return List<Object[]>
	 */
	@ResponseBody
	@RequestMapping(value = "getArea")
	public List<Object[]> getArea(@RequestParam(value="countryId", required=true) String countryId) {
		return visaProductsService.findVisaCountryArea(com.trekiz.admin.common.utils.StringUtils.toInteger(countryId));
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

		VisaProducts visaProduct = this.visaProductsService.findByVisaProductsId(new Long(visaProductId));
		if(visaProduct.getCopy_Project_Type() == null)
			//设置100的目的是避免修改前端页面代码，特意设置一个前端页面没有的值，跳过 freemarker 的split方法对null处理不正确
			visaProduct.setCopy_Project_Type("100");
		if(visaProduct.getOriginal_Project_Type() == null)
			visaProduct.setOriginal_Project_Type("100");
		
		List<Object> docInfoList = visaProductsFileService.findFileListByProId(visaProduct.getId(), false);
		
		//签证文件
		model.addAttribute("docInfoList", docInfoList);
		//办签国家
		model.addAttribute("sysCountry", CountryUtils.getCountry(visaProduct.getSysCountryId().longValue()).getCountryName_cn());
		//办签类型
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		//签证产品
		model.addAttribute("visaProduct", visaProduct);
		//价格币种
		model.addAttribute("currencyMark", CurrencyUtils.getCurrencyInfo(visaProduct.getCurrencyId().toString(), 0, "mark"));
		//是否显示签证成本价格
		UserUtils.removeCache("user");
		model.addAttribute("visaCostPrice",UserUtils.getUser().getCompany().getVisaCostPrice()+"1");
		return "modules/visa/visaProductsPreOrderDetail";
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
	 * 预定订单（第一步）
	 * @param visaProductId 签证产品订单ID
	 * @param visaOrderType 签证订单类别
	 * @param travelNum		签证订单人数
	 * @param agentinfoId   渠道ID
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getOrderFirst")
	public String getOrderFirst(HttpServletRequest request, HttpServletResponse response, Model model){
		
		// step 判断下单进行到第几步
		//String step = request.getParameter("step");
		// 判断渠道商ID 
		String agentinfoId = request.getParameter("agentinfoId");
		// 签证产品ID
		String visaProductId = request.getParameter("visaProductId");
		// 旅客人数
		String travelNum = request.getParameter("travelNum");
		// 特殊需求
		String remark = request.getParameter("remark");
		// 签证产品
		VisaProducts visaProduct = new VisaProducts();
		// 渠道商
		Agentinfo agentInfo = new Agentinfo();
		// 签证国家
		Country country = new Country();
		// 签证类型集合
		Map<String,String> map =DictUtils.getVisaTypeMap("visa_type");
		// 币种类
		Currency currency = new Currency();
		if(visaProduct.getCurrencyId()!=null && visaProduct.getCurrencyId()>0){
			currency = currencyService.findCurrency(Long.valueOf(visaProduct.getCurrencyId()));
		}
		
		VisaOrder order = new VisaOrder();
		if(StringUtils.isNumeric(visaProductId)  && StringUtils.isNumeric(travelNum) && StringUtils.isNumeric(agentinfoId)){
			visaProduct = visaProductsService.findByVisaProductsId(Long.valueOf(visaProductId));
			agentInfo = agentinfoService.findOne(Long.valueOf(agentinfoId));
			country = CountryUtils.getCountry(Long.valueOf(visaProduct.getSysCountryId()));
			order.setVisaProductId(Long.valueOf(visaProductId));
			order.setProductTypeID(VisaPreOrderController.VISA_ORDER_TYPE.longValue());
			order.setTravelNum(Integer.valueOf(travelNum));
			order.setAgentinfoId(Long.valueOf(agentinfoId));
			order.setRemark(remark);
			order.setVisaOrderStatus(Integer.valueOf(Context.VISA_ORDER_PAYSTATUS_DOING));// 订单状态(正在进行中)
			
		}
		if(order!=null){
			VisaOrder backOrder = visaOrderService.saveVisaOrder(order);
			if(backOrder!=null){
				model.addAttribute("step","1");	// 确定该请求由： 预定订单第一步 发出
				model.addAttribute("visaProduct",visaProduct); // 传送签证产品对象
				model.addAttribute("visaOrder",backOrder);// 传送订单对象
				model.addAttribute("agentInfo",agentInfo);// 传送批发商对象
				model.addAttribute("country",country);// 传送签证国家对象
				model.addAttribute("currency",currency);// 币种
				model.addAttribute("visaType", map.get(String.valueOf(visaProduct.getVisaType())));
//				model.addAttribute("needSpotAudition", visaProduct.getNeedSpotAudition()==1?"是":"否");// 是否面试
				return "modules/visa/visaGetOrderSecond";
			}
		}
		return "redirect:"+Global.getViewPath()+"/error/400.jsp";
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
		//String passportPhotoId = request.getParameter("passportPhotoId");
		// 身份证正面
		//String identityFrontPhotoId = request.getParameter("identityFrontPhotoId");
		// 报名表
		//String tablePhotoId = request.getParameter("tablePhotoId");
		// 照片
		//String personPhotoId = request.getParameter("personPhotoId");
		// 身份证反面
		//String identityBackPhotoId = request.getParameter("identityBackPhotoId");
		// 其它
		//String otherPhotoId = request.getParameter("otherPhotoId");
		
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
				//	Visafile file = iterator.next();
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
	
	//------------------------------------wxw added-----------------------------------------
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
	@RequestMapping(value="createVisaOrder/{visaProductId}/{agentId}/{payType}/{salerId}")
	public String createVisaOrder(@PathVariable String visaProductId,
			@PathVariable String agentId,
			@PathVariable String payType,
			@PathVariable String salerId,
			HttpServletRequest request, HttpServletResponse response, Model model){
		
		VisaProducts visaPro = new VisaProducts();
	    Country country = new Country();
		Agentinfo agentinfo = null;
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		
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
		if(agentId!=null && StringUtils.isNumeric(agentId) && Long.valueOf(agentId)!=-1){
			agentinfo = agentinfoService.findOne(Long.valueOf(agentId));
			//通过渠道的ID查询该渠道下的所有联系人--wenchao
			List<SupplierContacts> supplierContacts = supplierContactsService.findAllContactsByAgentInfo(Long.valueOf(agentId));
			if(supplierContacts.size()>0){
				List<Items> list= new ArrayList<Items>();
				for (SupplierContacts supplierContacts2 : supplierContacts) {
					Items items = new Items();
					items.setUuid(String.valueOf(supplierContacts2.getId()));
					items.setText(supplierContacts2.getContactName());
					list.add(items);
				}
				Gson gson = new Gson();
				String json = gson.toJson(list);
				model.addAttribute("supplierContacts", json);
			}
			model.addAttribute("agentName", agentinfo.getAgentName());//渠道名称 
		}else {
			if(companyUuid.equals("7a81a03577a811e5bc1e000c29cf2586")) {
				model.addAttribute("agentName", "未签");//渠道名称 
			}else{
				model.addAttribute("agentName", "非签约渠道");//渠道名称 
			}
			model.addAttribute("supplierContacts", "-1");
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
		model.addAttribute("visaProductId", visaProductId);//产品ID 
		model.addAttribute("agentId", agentId);//渠道ID 
		//wenchao add 2016014
		String agentAddressStr = agentinfoService.getAddressStrById(Long.parseLong(agentId));  //渠道地址
		Integer isAllowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();//是否允许添加渠道联系人
		Integer isAllowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();//是否允许修改联系人信息
		model.addAttribute("isAllowAddAgentInfo", isAllowAddAgentInfo);
		model.addAttribute("isAllowModifyAgentInfo", isAllowModifyAgentInfo);
		model.addAttribute("agentAddressStr", agentAddressStr);
		// 渠道商邮编
		if(agentinfo == null) {
			model.addAttribute("zipCode", "");
		}else {
			model.addAttribute("zipCode", agentinfo.getAgentPostcode() == null ? "" : agentinfo.getAgentPostcode());
		}

		model.addAttribute("payType",payType);
		//--wxw added 20150819---
		model.addAttribute("salerId", salerId);//销售ID 
		
		model.addAttribute("valid_period", visaPro.getValid_period());
		model.addAttribute("needSpotAudition", visaPro.isNeedSpotAudition()?"1":"0");
		model.addAttribute("visaDistrictList", DictUtils.getSysDicMap("from_area"));
		model.addAttribute("productId", visaPro.getId());
		model.addAttribute("productName", visaPro.getProductName());
		model.addAttribute("countryName", CountryUtils.getCountryName(Long.valueOf(visaPro.getSysCountryId())));
		model.addAttribute("sysCountryId", visaPro.getSysCountryId());
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaPro.getVisaType().toString()));
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
        //是否显示签证成本价格
        UserUtils.removeCache("user");
		model.addAttribute("visaCostPrice",UserUtils.getUser().getCompany().getVisaCostPrice()+"1");
		/**
		 * ==================
		 * 签证子接口测试，暂不删除，留以后接口变动时测试用
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
		/*Map<String, Long> travelerDocInfoIds = new HashMap<String, Long>();
		travelerDocInfoIds.put("passport_photo_id", Long.parseLong("5370"));
		travelerDocInfoIds.put("identity_front_photo_id",  null);
		travelerDocInfoIds.put("identity_back_photo_id",  Long.parseLong("4371"));
		travelerDocInfoIds.put("table_photo_id",  null);*/
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
	 * 1.保存签证订单基础数据（订单产品的基本信息）到 VisaOrder
	 * 2.保存 联系人信息 到  OrderContacts （如果有渠道联系人信息包括一个渠道联系人信息），还包括后续添加的联系人信息。
	 *   
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "saveVisaOrder")
	public Map<String, Object> saveVisaOrder(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String productId = request.getParameter("productId");
		String orderPersonNumAdult = request.getParameter("orderPersonNumAdult");
		String agentId = request.getParameter("agentId");
		String payType = request.getParameter("payType");
		String orderContactsJSON = request.getParameter("orderContactsJSON");
		String orderid = request.getParameter("orderid");
		String agentinfoName = request.getParameter("agentinfoName");
		//--wxw added 20150819---
		String salerId = request.getParameter("salerId");

		List<OrderContacts> contactsList = OrderUtil.getContactsList(orderContactsJSON);
		
        //获取产品相关信息
		VisaProducts visaPro=visaProductsService.findByVisaProductsId(Long.valueOf(productId));
		VisaOrder  order = new VisaOrder();
		if (com.trekiz.admin.common.utils.StringUtils.isNotEmpty(orderid)) {
			order.setId(Long.parseLong(orderid));
		}
		if (null!=agentinfoName) {
			order.setAgentinfoName(agentinfoName);
		}
		if(StringUtils.isNumeric(productId)  && StringUtils.isNumeric(orderPersonNumAdult)){
			String companyName = UserUtils.getUser().getCompany().getName();
			Long companyId = UserUtils.getUser().getCompany().getId();
			
			//订单编号  和  订单团号
			String orderNum = sysIncreaseService.updateSysIncrease(companyName
					.length() > 3 ? companyName.substring(0, 3) : companyName,
							companyId, null, Context.ORDER_NUM_TYPE);
			
			//新的虚拟团号算法，子订单中需要做同样的处理
			String GroupNo = null;
			if (UserUtils.getUser().getCompany().getId()==68) {
				  GroupNo=activityGroupService.getGroupNumForTTS(visaPro.getDeptId()+"",null);
			}else if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
				 GroupNo = visaPro.getGroupCode();
			}else {
				 GroupNo = sysIncreaseService.updateSysIncrease(companyName
							.length() > 3 ? companyName.substring(0, 3) : companyName,
									companyId, null, Context.GROUP_NUM_TYPE);
			}
			
			order.setOrderNo(orderNum);
			order.setGroupCode(GroupNo);
			//order.setMainOrderCode(null);
			
			//支付状态
			if (null!=payType&&StringUtils.isNumeric(payType)) {
				order.setPayStatus(Integer.parseInt(payType));
			}else {
				order.setPayStatus(1);
			}
			
			order.setVisaProductId(Long.valueOf(productId));
			
			//在签证订单中保存签证预定时相关签证产品的应收价
			order.setProOriginCurrencyId(visaPro.getCurrencyId());
			//DecimalFormat df = new DecimalFormat("#.00");
			//order.setProOriginVisaPay(new BigDecimal(Double.parseDouble(visaPro.getVisaPay().toString())));
			order.setProOriginVisaPay(visaPro.getVisaPay());
			
			order.setProductTypeID(VisaPreOrderController.VISA_ORDER_TYPE.longValue());
			order.setTravelNum(Integer.valueOf(orderPersonNumAdult));

//			Agentinfo agentinfo = new Agentinfo();
			if (StringUtils.isNotBlank(agentId)) {
//				agentinfo = agentinfoService.findAgentInfoById(Long.valueOf(agentId));

				order.setAgentinfoId(Long.valueOf(agentId));
			} else {
				order.setAgentinfoId(Long.valueOf("-1"));  // 未深究，当获取不到时是否算作非签约
			}
			User user = UserUtils.getUser(Long.parseLong(salerId));
			order.setSalerId(Integer.parseInt(salerId));
			order.setSalerName(user.getName());
			
			/**
			 * wangxinwei 2015-10-14  签证产品预定评审后修改
			 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
			 */
			order.setPayedMoney(UUID.randomUUID().toString().replace("-", ""));
			order.setAccountedMoney(UUID.randomUUID().toString().replace("-", ""));
		    //  0：未支付;1:已支付;2:已取消;100:订单创建中（创建未完成，不能使用）
			order.setVisaOrderStatus(Integer.valueOf(Context.VISA_ORDER_PAYSTATUS_DOING));// 订单状态(正在进行中)
		}
		//重新赋值createDate和createBy,否则上一步后下一步createDate和createBy数据库会清空,导致列表不显示订单相关信息
		
		
		/**
		 * wxw added 2016-01-04   对应需求号c457,c486
		 * 预定时处理产品预报单锁定情况--0：未锁定（默认），1：锁定
		 * (目前只针对拉美途)
		 */
		if ("7a81a26b77a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())) {
			if ("10".equals(visaPro.getForcastStatus())) {
				order.setForecastLockedIn('1');
			}
		}
		
		
		
		/**
		 * wxw added 2016-01-04   对应需求号c457,c486
		 * 预定时处理产品结算单锁定情况--0：未锁定（默认），1：锁定
		 * (目前只针对拉美途)
		 */
		if ("7a81a26b77a811e5bc1e000c29cf2586".equals(UserUtils.getUser().getCompany().getUuid())) {
			if ("1".equals(visaPro.getLockStatus().toString())) {
				order.setSettleLockedIn('1');
			}
		}
		
		
		
		//取得系统当前时间
		order.setCreateDate(new Date());
		//取得当前登录人
		order.setCreateBy(UserUtils.getUser());

		//1.保存订单信息 和 联系人信息
		order = visaOrderService.saveOrderInfo(order,contactsList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", order.getId());
		map.put("agentId", agentId);
		//--wxw added 20150819---
		map.put("salerId", salerId);
		map.put("visaOrderStatus",order.getVisaOrderStatus());
		return map;
	}
	
	@ResponseBody
	@Transactional
	@RequestMapping(value = "addTravelers")
	public Map<String, Object> saveTraveler(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		
			String travelerJSON = request.getParameter("travelerJSON");
			//String activityId = request.getParameter("activityId");//产品ID
			String orderid = request.getParameter("orderid");//订单ID
			String specialremark = request.getParameter("specialremark");//订单特殊需求
			//[{"id":"","name":"my","sum":"2","currency":"34"},{"id":"","name":"jnd","sum":"3","currency":"35"}]
			JSONArray costObject = JSONArray.fromObject(request.getParameter("costs"));
			                                                                                                                           
			
		//payPrice
		//1.保存游客信息  
		List<Traveler> travelerList = jsonToTravelerBean(travelerJSON);
		Traveler saveTraveler = visaOrderService.saveTravelers(Long.parseLong(orderid),travelerList,false);
		String rebatesPayPrice =request.getParameter("rebatesPayPrice");
//		saveTraveler.getId();
		

		//0820上
		visaOrderService.saveMoneyAmount(rebatesPayPrice,saveTraveler);

		
		
		//2. 创建签证信息
		Visa backVisa = saveVisa(saveTraveler.getId(),request);
		
		//3.保存签证订单特殊需求
		VisaOrder  visaOrder  = visaOrderService.findVisaOrder(Long.parseLong(orderid));
		if (null!=visaOrder) {
			visaOrder.setRemark(specialremark);
			visaOrderService.saveOrderInfo(visaOrder, null);
		}
		
		//4.保存游客其它费用信息 [{"currencyId":"33","currencyName":"人民币","price":"100"},{"currencyId":"34","currencyName":"美元","price":"4"},{"currencyId":"35","currencyName":"加拿大","price":"12"}]
		if(costObject.size() > 0){
			saveCost(costObject, saveTraveler.getId());
		}
		
		//5.保存游客结算价 和  游客成本价 ----modified wxw 2015-03-04 游客成本价 ----
		savePayPrice(saveTraveler, request);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("travelerId", saveTraveler.getId());
		map.put("visaId", backVisa.getId());
		
		return map;
	}
	
	/**
	 * 保存签证基本信息 
	 * @param travelerId
	 * @param request
	 * @return
	 */
	private Visa saveVisa(Long travelerId, HttpServletRequest request){
		
		//保存游客前需要 删除游客签证信息
		visaService.delVisaByTravelerId(travelerId);
		
		String passportdocID = request.getParameter("passportdocID");
		String idcardfrontdocID = request.getParameter("idcardfrontdocID");
		String entry_formdocID = request.getParameter("entry_formdocID");
		String photodocID = request.getParameter("photodocID");
		String idcardbackdocID = request.getParameter("idcardbackdocID");
		String otherdocID = request.getParameter("otherdocID");
		String visa_annexdocID = request.getParameter("visa_annexdocID");
		String familyRegisterdocID = request.getParameter("familyRegisterdocID");//户口本ID
		String houseEvidencedocID = request.getParameter("houseEvidencedocID");//房产证ID
		
		String forecastStartOut = request.getParameter("forecastStartOut");//预计出团时间
		/**
		 * 预计回团时间
		 * 对应需求  0211 星辉四海  添加预计回团时间
		 */
		String forecastBackDate = request.getParameter("forecastBackDate");//预计回团时间
		String forecastContract = request.getParameter("forecastContract");//预计签约时间
		String remark = request.getParameter("remark"); //签证备注信息
		
		Visa visa = new Visa();
		visa.setTravelerId(travelerId);
		//签证预订后默认状态为 请选择 -  2015-04-15--
		visa.setVisaStauts(-1);
		visa.setForecastStartOut(DateUtils.dateFormat(forecastStartOut,"yyyy-MM-dd"));
		/**
		 * 预计回团时间
		 * 对应需求  0211 星辉四海  添加预计回团时间
		 */
		if(forecastBackDate!=null){
			visa.setForecastBackDate(DateUtils.dateFormat(forecastBackDate,"yyyy-MM-dd"));
		}else{
			visa.setForecastBackDate(null);	
		}
		visa.setForecastContract(DateUtils.dateFormat(forecastContract,"yyyy-MM-dd"));

		visa.setRemark(remark);
		if (null!=passportdocID&&!"".equals(passportdocID)) {
			visa.setPassportPhotoId(Long.parseLong(passportdocID));
		}
		if (null!=idcardfrontdocID&&!"".equals(idcardfrontdocID)) {
			visa.setIdentityFrontPhotoId(Long.parseLong(idcardfrontdocID));
		}
		if (null!=entry_formdocID&&!"".equals(entry_formdocID)) {
			visa.setTablePhotoId(Long.parseLong(entry_formdocID));
		}
		if (null!=photodocID&&!"".equals(photodocID)) {
			visa.setPersonPhotoId(Long.parseLong(photodocID));
		}
		if (null!=idcardbackdocID&&!"".equals(idcardbackdocID)) {
			visa.setIdentityBackPhotoId(Long.parseLong(idcardbackdocID));
		}
		if (null!=otherdocID&&!"".equals(otherdocID)) {
			visa.setOtherPhotoId(Long.parseLong(otherdocID));
		}
		//户口本
		if (null!=familyRegisterdocID && !"".equals(familyRegisterdocID)) {
			visa.setFamilyRegisterPhotoId(Long.parseLong(familyRegisterdocID));
		}
		//房产证
		if (null!=houseEvidencedocID && !"".equals(houseEvidencedocID)) {
			visa.setHouseEvidencePhotoId(Long.parseLong(houseEvidencedocID));
		}
		//签证附件
		
		if(null!=visa_annexdocID&&!"".equals(visa_annexdocID)){
			visa.setDocIds(visa_annexdocID);
			List<DocInfo> docs = docInfoService.getDocInfoBydocids(visa_annexdocID);
			visa.setDocs(docs);
			
		}
		
		return  visaService.saveVisa(visa);
	}
	
	
    /**
     * 保存游客的其它费用
     * 
     * @param jsonCost : 其它费用的JSON数组
     * @param travelerId： 游客ID
     */
	public void saveCost(JSONArray jsonCost,Long travelerId){
		//清除费用相关信息
		costchangeService.delete(travelerId);
		for(int i = 0; i < jsonCost.size(); i++){
			JSONObject costJson = jsonCost.getJSONObject(i);
			Costchange costchange = new Costchange();
			//费用名称
	        String name = costJson.getString("name");
	        if(name != null && StringUtils.isNotBlank(name)){
	            costchange.setCostName(name.toString());
	        }
	        //费用币种
	        String curency = costJson.getString("currency");
	        if(curency != null && StringUtils.isNotBlank(curency)){
	            costchange.setPriceCurrency(currencyService.findCurrency(Long.parseLong(curency)));
	        }
	        //费用金额
	        String sum = costJson.getString("sum");
	        if(sum != null && StringUtils.isNotBlank(sum)){
	            costchange.setCostSum(StringNumFormat.getBigDecimalForTow(sum));
	        }
	        costchange.setTravelerId(travelerId);
	        costchangeService.save(costchange);
		}
	}
	
	
	/**
	 * 保存游客结算价  和  游客成本价
	 * @param travelerId 游客Id
	 * @param request
	 */
	private void savePayPrice(Traveler traveler, HttpServletRequest request){
		JSONArray payPriceObject = JSONArray.fromObject(request.getParameter("payPrice"));
		List<MoneyAmount> moneyAmountList= Lists.newArrayList();
		MoneyAmount moneyAmount = null;
		List<MoneyAmount> moneyAmountListOrigin= Lists.newArrayList();
		MoneyAmount moneyAmountOrigin = null;
		
		int orderType =VisaPreOrderController.VISA_ORDER_TYPE;
		int busindessType=VisaPreOrderController.BUSINDESS_TYPE_TRAVELER;
		
		//保存游客结算价
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		String payPriceSerialNum = UUID.randomUUID().toString().replace("-", "");
		String payPriceSerialNumOrigin = UUID.randomUUID().toString().replace("-", "");
		for(int i = 0; i < payPriceObject.size(); i++){
		       JSONObject payPrice = payPriceObject.getJSONObject(i);
		       
		        /**
		         * -------wxw added--2015-03-14----
		         * 处理某币种结算价为""是产生的问题
		         */
		        String price = payPrice.getString("price");
		        Double priceDouble=null;
		        if (null==price||"".equals(price)) {
		        	priceDouble = new Double("0");
				}else {
					priceDouble= new Double(price);
				}
		        
		        //构造游客结算价
		        /*moneyAmount = new MoneyAmount(traveler.getPayPriceSerialNum()==null?payPriceSerialNum:traveler.getPayPriceSerialNum(), //款项UUID
				payPrice.getInt("currencyId"),//币种ID
				new BigDecimal(priceDouble),//相应币种的金额
				traveler.getId(), //订单或游客ID
				Context.MONEY_TYPE_JSJ, //款项类型: 游客结算 为 14
				orderType,//订单类型
				busindessType,//
				UserUtils.getUser().getId());*/
		        
		        /**
                 * wangxinwei added 2015-10-13
                 * 签证产品预定评审后修改：重写上面一段代码，代码中方法参数个数删减
		         */
		        moneyAmount = new MoneyAmount();
		        moneyAmount.setSerialNum(traveler.getPayPriceSerialNum()==null?payPriceSerialNum:traveler.getPayPriceSerialNum());//款项UUID
		        moneyAmount.setCurrencyId(payPrice.getInt("currencyId"));//币种ID
		        moneyAmount.setAmount(new BigDecimal(priceDouble));//相应币种的金额
		        moneyAmount.setUid(traveler.getId());//订单或游客ID
		        moneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);//款项类型: 游客结算 为 14
		        moneyAmount.setOrderType(orderType);//订单类型
		        moneyAmount.setBusindessType(busindessType);//业务类型
		        moneyAmount.setCreatedBy(UserUtils.getUser().getId());//创建者
		        moneyAmountList.add(moneyAmount);
		        
		        //构造游客原始结算价
		    	/*moneyAmountOrigin = new MoneyAmount(traveler.getPayPriceSerialNum()==null?payPriceSerialNumOrigin:traveler.getOriginalPayPriceSerialNum(), //款项UUID
				payPrice.getInt("currencyId"),//币种ID
				new BigDecimal(priceDouble),//相应币种的金额
				traveler.getId(), //订单或游客ID
				Context.MONEY_TYPE_YSJSJ, //款项类型: 原始游客结算价 为21
				orderType,//订单类型
				busindessType,//
				UserUtils.getUser().getId());*/
		        /**
                 * wangxinwei added 2015-10-13
                 * 签证产品预定评审后修改：重写上面一段代码，代码中方法参数个数删减
		         */
		        moneyAmountOrigin = new MoneyAmount();
		        moneyAmountOrigin.setSerialNum(traveler.getPayPriceSerialNum()==null?payPriceSerialNumOrigin:traveler.getOriginalPayPriceSerialNum());//款项UUID
		        moneyAmountOrigin.setCurrencyId(payPrice.getInt("currencyId"));//币种ID
		        moneyAmountOrigin.setAmount(new BigDecimal(priceDouble));//相应币种的金额
		        moneyAmountOrigin.setUid(traveler.getId());//订单或游客ID
		        moneyAmountOrigin.setMoneyType(Context.MONEY_TYPE_YSJSJ);//款项类型: 原始游客结算价 为21
		        moneyAmountOrigin.setOrderType(orderType);//订单类型
		        moneyAmountOrigin.setBusindessType(busindessType);//业务类型
		        moneyAmountOrigin.setCreatedBy(UserUtils.getUser().getId());//创建者
		    	moneyAmountListOrigin.add(moneyAmountOrigin);
		}
		
		boolean flagpaysave =moneyAmountService.saveOrUpdateMoneyAmounts(traveler.getPayPriceSerialNum()==null?payPriceSerialNum:traveler.getPayPriceSerialNum(), moneyAmountList);
		boolean flagpaysaveorigin =moneyAmountService.saveOrUpdateMoneyAmounts(traveler.getPayPriceSerialNum()==null?payPriceSerialNumOrigin:traveler.getOriginalPayPriceSerialNum(), moneyAmountListOrigin);
		
		if (flagpaysave&&traveler.getPayPriceSerialNum()==null) {
			travelerService.updateSerialNumByTravelerId(payPriceSerialNum, traveler.getId());
		}
		if (flagpaysaveorigin&&traveler.getPayPriceSerialNum()==null) {
			travelerService.updateOriginalPayPriceSerialNumByTravelerId(payPriceSerialNumOrigin,traveler.getId());
		}
		
		//保存游客成本价2014-03-04
		JSONArray costPriceObject = JSONArray.fromObject(request.getParameter("costPrice"));
		List<MoneyAmount> costMoneyAmountList= Lists.newArrayList();
		MoneyAmount costMoneyAmount = null;
		
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		String costPriceSerialNum = UUID.randomUUID().toString().replace("-", "");	//成本价序列号
		JSONObject costPrice = null;	
		for(int i =0 ; i < costPriceObject.size(); i++){
			costPrice = costPriceObject.getJSONObject(i);
			int currencyId = costPrice.getInt("currencyId");
			BigDecimal price = new BigDecimal(costPrice.getDouble("price"));
			Long userId = UserUtils.getUser().getId();
			costMoneyAmount = new MoneyAmount(costPriceSerialNum, currencyId, price, traveler.getId(), Context.MONEY_TYPE_CBJ, orderType, busindessType, userId);
			costMoneyAmountList.add(costMoneyAmount);
		}
		boolean flagpaysavecost =moneyAmountService.saveOrUpdateMoneyAmounts(traveler.getCostPriceSerialNum()==null?costPriceSerialNum:traveler.getCostPriceSerialNum(), costMoneyAmountList);
		if (flagpaysavecost&&traveler.getCostPriceSerialNum()==null) {
			travelerService.updateCostSerialNumByTravelerId(costPriceSerialNum,traveler.getId());
		}
		
		
	}

	
	/**
	 * 把traveler  JSON字符串转换为  List<Traveler>
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	private List<Traveler> jsonToTravelerBean(String jsonStr)
			throws JSONException {
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		List<Traveler> result = new ArrayList<Traveler>();
        
		for(int i = 0; i < jsonArray.size(); i++){
		       JSONObject jsonTravler = jsonArray.getJSONObject(i);
	           Traveler traveler  =  new Traveler();
	           if (com.trekiz.admin.common.utils.StringUtils.isNumeric(jsonTravler.getString("id"))) {
	        	   traveler.setId(jsonTravler.getLong("id"));
			   }
	           traveler.setName(jsonTravler.getString("name"));
	           traveler.setNameSpell(jsonTravler.getString("nameSpell"));
	           traveler.setSex(jsonTravler.getInt("sex"));
	           traveler.setPassportType(jsonTravler.getString("passportType"));
	           traveler.setBirthDay(DateUtils.dateFormat(jsonTravler.getString("birthDay"),"yyyy-MM-dd"));
	           traveler.setTelephone(jsonTravler.getString("telephone"));
	           traveler.setPassportCode(jsonTravler.getString("passportCode"));
	           traveler.setIssuePlace(DateUtils.dateFormat(jsonTravler.getString("issuePlace"),"yyyy-MM-dd"));
	           traveler.setPassportValidity(DateUtils.dateFormat(jsonTravler.getString("passportValidity"),"yyyy-MM-dd"));
	           traveler.setRemark(jsonTravler.getString("remark"));
	          // traveler.setIdCard(jsonTravler.getString("idCard"));
	           traveler.setPassportStatus(jsonTravler.getInt("passportStatus"));
	           if(UserUtils.getUser().getCompany().getUuid().equals("980e4c74b7684136afd89df7f89b2bee")){
	           String issuePlace1 = jsonTravler.getString("issuePlace1");
	           if(StringUtils.isNotBlank(issuePlace1)){
	        	   traveler.setIssuePlace1(issuePlace1);
	           }
	           }
	           result.add(traveler);
		}
		//List<Traveler> result = JSONArray.toList(jsonArray, Traveler.class);
		return result;
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
        model.addAttribute("currencyList4Rebates", currencyList);
	}
	
	
	/**
	 * 
	 *  功能:订单最后一步保存  点  保存并支付按钮时触发
	 *  1. 更新visa_order 表中TotalMoney 字段
	 *  2.保存订单的特殊需求
	 *  3.保存多币种订单结算额到 MoneyAmount表
	 *  @param model
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value = "lastSave")
	public Object lastSave(Model model,HttpServletRequest request){
		Map<String, Object> result = new  HashMap<String, Object>();
		String orderid = request.getParameter("orderid");
		String specialremark = request.getParameter("specialremark");
		String groupRebatesCurrency = request.getParameter("groupRebatesCurrency");
		String groupRebatesMoney = request.getParameter("groupRebatesMoney");
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
//		String currencyId = request.getParameter("currencyId");
//		String currencyPrice = request.getParameter("currenctPrice");
//		String[] currencyIdArr = currencyId.split(",");
//		String[] currencyPrcieArr = currencyPrice.split(",");
		int orderType =VisaPreOrderController.VISA_ORDER_TYPE;
		int busindessType=VisaPreOrderController.BUSINDESS_TYPE_ORDER;
		String agentId = request.getParameter("agentId");
		String orderContactsJSON = request.getParameter("orderContactsJSON");
		List<OrderContacts> contactsList;
		try {
			contactsList = OrderUtil.getContactsList(orderContactsJSON);
			String agentinfoName = request.getParameter("agentinfoName");
			String salerId = request.getParameter("salerId");
			if(agentId == null || agentId.equals("-1") && UserUtils.getUser().getCompany().getUuid().equals("7a81a26b77a811e5bc1e000c29cf2586")){
				Long tempAgentId = agentinfoService.saveAgent(contactsList, agentinfoName,salerId);
				if(tempAgentId!=null){
					agentId = ""+tempAgentId;
				}else{
					result.put("errorMsg", "渠道商已存在");
					return result;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//通过订单id获取订单总结算价
		Map<String, String> countMap = countOrderClearMoney(orderid);
		String[] currencyIdArr = countMap.get("currencyID").split(",");
		String[] currencyPrcieArr = countMap.get("currencyPrice").split(",");
		
		//保存订单结算价
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		String serialNum = UUID.randomUUID().toString().replace("-", "");
		/*MoneyAmount moneyAmount = null;
		for(int i = 0; i < currencyIdArr.length; i++){
			moneyAmount = new MoneyAmount(serialNum, 
					Integer.parseInt(currencyIdArr[i]), 
					new BigDecimal(currencyPrcieArr[i]),
					visaOrder.getId(),
					Context.MONEY_TYPE_YSH, //订单应收
					orderType,
					busindessType,
                    UserUtils.getUser().getId());
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
		}*/
		
		/**
         * wangxinwei added 2015-10-14 代码中循环保存优化
         * 签证产品预定评审后修改：重写上面一段代码，代码中循环保存优化
         * 签证产品预定评审后修改：代码中方法参数个数删减
         */
		List<MoneyAmount> orderClearingAmounts  = new ArrayList<MoneyAmount>();
		for(int i = 0; i < currencyIdArr.length; i++){
			MoneyAmount moneyAmount = new MoneyAmount();
			moneyAmount.setSerialNum(serialNum);
			moneyAmount.setCurrencyId(Integer.parseInt(currencyIdArr[i]));
			moneyAmount.setAmount(new BigDecimal(currencyPrcieArr[i]));
			moneyAmount.setUid(visaOrder.getId());
			moneyAmount.setMoneyType(Context.MONEY_TYPE_YSH);
			moneyAmount.setOrderType(orderType);
			moneyAmount.setBusindessType(busindessType);
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			
			orderClearingAmounts.add(moneyAmount);
		}
		moneyAmountService.saveOrUpdateMoneyAmounts(serialNum,orderClearingAmounts); 
		
		//保存一份原始总价
		/**
		 * wangxinwei 2015-10-15  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		String serialNumOrigin = UUID.randomUUID().toString().replace("-", "");
		/*MoneyAmount moneyAmountOrigin = null;
		for(int i = 0; i < currencyIdArr.length; i++){
			moneyAmountOrigin = new MoneyAmount(serialNumOrigin, 
					Integer.parseInt(currencyIdArr[i]), 
					new BigDecimal(currencyPrcieArr[i]),
					visaOrder.getId(),
					Context.MONEY_TYPE_YSYSH, //订单原始应收
					orderType,
					busindessType,
                    UserUtils.getUser().getId());
			moneyAmountOrigin.setCreatedBy(UserUtils.getUser().getId());
			moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountOrigin);
		}*/
		
		/**
         * wangxinwei added 2015-10-15 代码中循环保存优化
         * 签证产品预定评审后修改：代码中循环保存优化
         * 签证产品预定评审后修改：代码中方法参数个数删减
         */
		List<MoneyAmount> orderOriginClearingAmounts  = new ArrayList<MoneyAmount>();
		for(int i = 0; i < currencyIdArr.length; i++){
			
			MoneyAmount moneyAmountOrigin = new MoneyAmount();
			moneyAmountOrigin.setSerialNum(serialNumOrigin);
			moneyAmountOrigin.setCurrencyId(Integer.parseInt(currencyIdArr[i]));
			moneyAmountOrigin.setAmount(new BigDecimal(currencyPrcieArr[i]));
			moneyAmountOrigin.setUid(visaOrder.getId());
			moneyAmountOrigin.setMoneyType(Context.MONEY_TYPE_YSYSH);
			moneyAmountOrigin.setOrderType(orderType);
			moneyAmountOrigin.setBusindessType(busindessType);
			moneyAmountOrigin.setCreatedBy(UserUtils.getUser().getId());
			
			orderOriginClearingAmounts.add(moneyAmountOrigin);
		}
		moneyAmountService.saveOrUpdateMoneyAmounts(serialNumOrigin,orderOriginClearingAmounts);
		
		visaOrder.setTotalMoney(serialNum); //设置总额的UUID
		visaOrder.setOriginalTotalMoney(serialNumOrigin);
		visaOrder.setRemark(specialremark); //设置特殊需求
		visaOrder.setVisaOrderStatus(0);//支付前设置订单的支付状态为未支付
		visaOrder.setAgentinfoId(new Long(agentId));

		
		
		//----wxw added 保存订单成本价----
		String costcurrencyId = request.getParameter("costcurrencyId");
		String costcurrencyPrice = request.getParameter("costcurrencyPrice");
		String[] costcurrencyIdArr = costcurrencyId.split(",");
		String[] costcurrencyPrcieArr = costcurrencyPrice.split(",");
		
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		String costserialNum = UUID.randomUUID().toString().replace("-", ""); //订单成本价UUID
		/*MoneyAmount costmoneyAmount = null;
		for(int i = 0; i < costcurrencyIdArr.length; i++){
			costmoneyAmount = new MoneyAmount(costserialNum, 
					Integer.parseInt(costcurrencyIdArr[i]), 
					new BigDecimal(costcurrencyPrcieArr[i]),
					visaOrder.getId(),
					Context.MONEY_TYPE_CBJ, //订单成本价
					orderType,
					busindessType,
                    UserUtils.getUser().getId());
			costmoneyAmount.setCreatedBy(UserUtils.getUser().getId());
			moneyAmountService.saveOrUpdateMoneyAmount(costmoneyAmount);
		}*/
		
		/**
         * wangxinwei added 2015-10-14 代码中循环保存优化
         * 签证产品预定评审后修改：代码中循环保存优化
         * 签证产品预定评审后修改：代码中方法参数个数删减
         */
		List<MoneyAmount> orderCostAmounts  = new ArrayList<MoneyAmount>();
		for(int i = 0; i < costcurrencyIdArr.length; i++){
	      
			MoneyAmount costmoneyAmount = new MoneyAmount();
			costmoneyAmount.setSerialNum(costserialNum);
			costmoneyAmount.setCurrencyId(Integer.parseInt(costcurrencyIdArr[i]));
			costmoneyAmount.setAmount(new BigDecimal(costcurrencyPrcieArr[i]));
			costmoneyAmount.setUid(visaOrder.getId());
			costmoneyAmount.setMoneyType(Context.MONEY_TYPE_CBJ);
			costmoneyAmount.setOrderType(orderType);
			costmoneyAmount.setBusindessType(busindessType);
			costmoneyAmount.setCreatedBy(UserUtils.getUser().getId());;
			
			orderCostAmounts.add(costmoneyAmount);
		}
		moneyAmountService.saveOrUpdateMoneyAmounts(costserialNum,orderCostAmounts);
		
		
		visaOrder.setCostTotalMoney(costserialNum);
		
		//订单团队返佣金额
		/**
		 * wangxinwei 2015-10-14  签证产品预定评审后修改
		 * uuid长度统一，兼容老数据:数据库里保留36位，程序中都用32位不带'-'的uuid
		 */
		String groupRebatesSerialNum = UUID.randomUUID().toString().replace("-", "");
		MoneyAmount amount = new MoneyAmount();
		amount.setSerialNum(groupRebatesSerialNum);
		amount.setCurrencyId(Integer.valueOf(groupRebatesCurrency));
		if (StringUtils.isNotBlank(groupRebatesMoney)) {
			amount.setAmount(new BigDecimal(groupRebatesMoney));
		}else{
			amount.setAmount(BigDecimal.ZERO);
		}
		amount.setUid(Long.parseLong(orderid));
		amount.setMoneyType(23);
		amount.setOrderType(6);
		amount.setBusindessType(1);
		amount.setCreatedBy(UserUtils.getUser().getId());
		amount.setCreateTime(new Date());
		moneyAmountService.saveOrUpdateMoneyAmount(amount);
		visaOrder.setGroupRebate(groupRebatesSerialNum);
		
		//保存订单信息
		visaOrder =	visaOrderService.saveOrderInfo(visaOrder, null);
		//-------by------junhao.zhao-----2017-01-06-----主要通过visa_order向表order_data_statistics中添加数据---开始-------------------------------------------------------------
		// 向表order_data_statistics中添加数据
		if(visaOrder!=null)	{
			orderDateSaveOrUpdateService.insertVisaOrder(visaOrder);
		//-------by------junhao.zhao-----2017-01-06-----主要通过visa_order向表order_data_statistics中添加数据---结束-----------------------------------
		
		}
		result.put("productOrder", visaOrder);
		//result.put("", value);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "deleteTraveler")
	public void deleteTraveler(HttpServletRequest request,
			HttpServletResponse response) {
		String travelerId = request.getParameter("travelerId");
		visaOrderService.deleteTraveler(Long.valueOf(travelerId));
	}
	
	/**
	 * 结算订单总结算价
	 * @param currencyIds：游客的结算价币种的IDs 
	 * @param currencyPrices：游客结算价金额的IDs
	 * @return
	 */
	private  Map<String, String> countOrderClearMoney(String visaOrderId) {
		//获取订单的所有游客列表
		List<Traveler> travelerList = travelerDao.findTravelerByOrderIdAndOrderType(Long.parseLong(visaOrderId),Context.ORDER_TYPE_QZ);
		//游客的结算币种IDs
		String[] currencyIds = new String[travelerList.size()];//{"1,2,34,5","1,3,34,7"}
		//游客结算各个币种的价钱
		String[] currencyPrices = new String[travelerList.size()];//{"120.00,50.00,40.00,70.00","100.00,50.00,40.00,50.00"}
		
		//处理每个游客的币种和钱数
		for (int i = 0; i < travelerList.size(); i++) {
			Traveler traveler = travelerList.get(i);
			List<String> serialNumList = new ArrayList<String>();
			serialNumList.add(traveler.getPayPriceSerialNum());
			List<String> moneystrList = moneyAmountService.getMoneyIdAndPrice(serialNumList);//返回的数据格式为//33 100.00
			StringBuffer sbcurrencyId = new StringBuffer("");
			StringBuffer sbcurrencyPrice = new StringBuffer("");
			for (String currencyIdAndCurrencyPrice:moneystrList) {
				String[] _temp  = currencyIdAndCurrencyPrice.split(" ");
				sbcurrencyId.append(_temp[0]).append(",");
				sbcurrencyPrice.append(_temp[1]).append(",");
			}
			String currencyIdsStr = sbcurrencyId.toString();
			currencyIdsStr = currencyIdsStr.substring(0,currencyIdsStr.length()-1);
			
			String currencyPricesStr = sbcurrencyPrice.toString();
			currencyPricesStr = currencyPricesStr.substring(0,currencyPricesStr.length()-1);
			currencyIds[i]=currencyIdsStr;
			currencyPrices[i]=currencyPricesStr;
		}
		
		//合并所有游客各个币种的钱数
		Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
		for (int i = 0; i < currencyIds.length; i++) {
			String currencyId = currencyIds[i];
			String currencyPrice = currencyPrices[i];
			String[] currIds = currencyId.split(",");
			String[] currPrices = currencyPrice.split(",");
			for (int j = 0; j < currIds.length; j++) {
				String currId = currIds[j];
				String currPrice = currPrices[j];

				if (!map.containsKey(currId)) {
					map.put(currId, new BigDecimal(currPrice));
				} else {
					map.put(currId,map.get(currId).add(new BigDecimal(currPrice)));
				}
			}
		}
		
		StringBuffer resultcurrencyIDs = new StringBuffer("");
		StringBuffer resultcurrencyPrices = new StringBuffer("");
		for (String key : map.keySet()) {
			resultcurrencyIDs.append(key).append(",");
			resultcurrencyPrices.append(map.get(key).toString()).append(",");	
		}
		Map<String, String> resultmap = new HashMap<String, String>();
		resultmap.put("currencyID", resultcurrencyIDs.toString());
		resultmap.put("currencyPrice", resultcurrencyPrices.toString());
		return resultmap;
	}
	

	
	@RequestMapping(value = "pay")
	public String pay(HttpServletRequest request) {
		String visaOrderId = request.getParameter("orderId");
		String orderDetailUrl = request.getParameter("orderDetailUrl");//订单详情URL
		OrderPayInput orderPayInput = visaOrderService.createPayParameter(visaOrderId,orderDetailUrl);
		orderPayInput.setEntryOrderUrl("/visa/order/searchxs?orderNo="+orderPayInput.getOrderPayDetailList().get(0).getOrderNum()+"&_m=417&_mc=580");
		request.setAttribute("pay", orderPayInput);
		return "forward:../../orderPayMore/pay";
	}
	
	
	
	/**
	 * 更新未查看订单数量
	 * @author xudong.he
     */
    @RequestMapping(value ="updateCount")
	@ResponseBody
	@Transactional
    public Object updateCount(HttpServletResponse response,
       	    Model model, HttpServletRequest request) throws Exception { 
    	Map<String, Object> data = new HashMap<String, Object>();
	   	visaOrderService.updateVisaOrderByCode(request.getParameter("orderCode"));
    	return data;
    }
    
    /**
     * 
     * @Description: 签证预定列表，查询产品的已有有效订单；对应需求编号C361       
     * @author xinwei.wang
     * @date 2015年11月12日下午9:23:08
     * @param batchNo
     * @param busynessType
     * @return    
     * @throws
     */
	@ResponseBody
	@RequestMapping("getProductOrderList")
	public List<Map<String, String>> getProductOrderList(String visaproductid) {
		List<Map<String, String>> visaOrderList =  new ArrayList<Map<String, String>>();
		if(StringUtils.isNotBlank(visaproductid)) {
			visaOrderService.getOrderListByVisaProductId(visaproductid, visaOrderList);
		}	
		return visaOrderList;
	}
	/**
	 * 签证报名渠道联系人下拉显示
	 * wenchao.lv
	 * @param id
	 * @param model
	 * @param response
	 * @param agentInfoId
	 */
	@RequestMapping("findOneSupplyContacts")
	public void findOneSupplyContacts(String id,Model model,HttpServletResponse response, String agentInfoId){
		SupplierContacts supplierContacts = supplierContactsService.findContactsById(Long.valueOf(id));
		model.addAttribute("Contacts", supplierContacts);		
		Gson gson = new Gson ();
		String json = gson.toJson(supplierContacts);
		try {
			PrintWriter writer = response.getWriter();
			writer.print(json);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
