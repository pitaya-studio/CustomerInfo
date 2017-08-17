package com.trekiz.admin.modules.visa.web;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.GroupcodeModifiedRecord;
import com.trekiz.admin.modules.activity.repository.GroupcodeModifiedRecordDao;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProductFile;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.IVisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaProductsFileService;

@Controller
@RequestMapping(value = "${adminPath}/visa/visaProducts")
public class VisaProductsController extends BaseController {

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private IVisaProductsService visaProductsService;
	
	@Autowired
	private VisaProductsFileService visaProductsFileService;
	
	@Autowired
	private SysIncreaseService sysIncreaseService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private GroupcodeModifiedRecordDao  groupcodeModifiedRecordDao;
	
	@ModelAttribute("menuId")
	protected Integer getMenuId() {
		return 70;
	}

	@RequestMapping(value = "list/{status}")
	public String list(@PathVariable(value="status")String status, 
			@ModelAttribute VisaProducts visaProducts,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		String productName = request.getParameter("productName");//
		String collarZoning = request.getParameter("collarZoning");//
		String sysCountryId = request.getParameter("sysCountryId");//
		String visaProductIds = request.getParameter("visaProductIds");//
		String visaType = request.getParameter("visaType");//
		String orderBy = request.getParameter("orderBy");//
		
		DepartmentCommon common = departmentService.setDepartmentPara("activity", model);

		// 排序方式默认为逆序创建时间
		if (StringUtils.isBlank(orderBy)) {
			orderBy = "id DESC";
		}
		if(StringUtils.isBlank(status) || "2".equals(status)) {
			visaProducts.setProductStatus(new Integer(Context.PRODUCT_ONLINE_STATUS));
		}else{
			visaProducts.setProductStatus(new Integer(Context.PRODUCT_OFFLINE_STATUS));
		}

		Page<VisaProducts> page = this.visaProductsService
				.findVisaProductsPage(
						new Page<VisaProducts>(request, response),
						visaProducts, productName, collarZoning, sysCountryId,
						visaType, orderBy, common);

		//查询可以办理签证产品的国家信息，包含国家表主键和中文名称
		List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
		model.addAttribute("countryInfoList", countryInfoList);
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));

		model.addAttribute("page", page);
		model.addAttribute("productName", productName);
		model.addAttribute("sysCountryId", sysCountryId);
		model.addAttribute("collarZoning", collarZoning);
		if(StringUtils.isNotBlank(sysCountryId)) {
			model.addAttribute("collarZoningList", getArea(sysCountryId));
		}
		model.addAttribute("visaType", visaType);
		model.addAttribute("visaProductIds", visaProductIds);
		model.addAttribute("orderBy", orderBy);

		return "modules/visaProducts/visaProductList";
	}

	/**
	 * 进入添加签证产品基础信息添加
	 * 
	 * @param visaProducts
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "addVisaInformation")
	public String addVisaInformation(@ModelAttribute VisaProducts visaProduct,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
		//查询可以办理签证产品的国家信息，包含国家表主键和中文名称
		List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
		model.addAttribute("countryInfoList", countryInfoList);
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
		
		//获取审核业务 部门列表
		List<Long> deptList = UserUtils.getDepartmentByJobNew();
		Map<String,String> deptMap = new HashMap<String,String>();
		if(CollectionUtils.isNotEmpty(deptList)){
			deptMap.put("dept_id", deptList.get(0).toString());//默认显示第一个部门
			deptMap.put("deptName", departmentService.findById(deptList.get(0)).getName());//获取部门名称
		}
		if(visaProduct.getDeptId()!=null && visaProduct.getDeptId()!=0){
			deptMap.put("dept_id", visaProduct.getDeptId().toString());
			deptMap.put("deptName", departmentService.findById(visaProduct.getDeptId()).getName());
			model.addAttribute("deptMap", deptMap);
		}else{
			model.addAttribute("deptMap", CollectionUtils.isNotEmpty(deptList) ? deptMap : "");
		}
		model.addAttribute("deptId",UserUtils.getUser().getCompany().getId());
		
		try {
			String va = request.getParameter("valueString");
			String payList = request.getParameter("payList");
			
			String sysCountryCheck = "";
			String countryVisaAreaCheck = "";
			String visaTypeCheck = "";
			String payModeTextCheck = "";
			if(va != null && va.length() > 0){
				String valueString = new String((request.getParameter("valueString")).getBytes("ISO-8859-1"),"UTF-8");
				String value[] = valueString.split(";");
				sysCountryCheck = value[0];
				countryVisaAreaCheck = value[1];
				visaTypeCheck = value[2];
				payModeTextCheck = value[3];
			}
			model.addAttribute("sysCountryCheck", sysCountryCheck);
			model.addAttribute("countryVisaAreaCheck", countryVisaAreaCheck);
			model.addAttribute("visaTypeCheck",visaTypeCheck);
			model.addAttribute("payModeTextCheck",payModeTextCheck);
			model.addAttribute("groupCodeRule",travelActivityService.findGroupCodeRule());
			
			if(payList != null && payList.length() > 0){
				String[] payLis = payList.split(",");
				if(payLis.length >= 4){
					model.addAttribute("selectCurrency", payLis[0]);
					model.addAttribute("visaPrice", payLis[1]);
					model.addAttribute("visaPay", payLis[2]);
					if(!payList.substring(payList.length() - 1).equals(",")){
						model.addAttribute("otherCost", payLis[3]);
					}
				}	
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "modules/visaProducts/visaBasicInformation";
	}

	/**
	 * 进入添加签证产品价格添加
	 * 
	 * @param visaProducts
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "editVisaPrice")
	public String editVisaPrice(@ModelAttribute VisaProducts visaProduct,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
		model.addAttribute("sysCountry", CountryUtils.getCountry(visaProduct.getSysCountryId().longValue()).getCountryName_cn());
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		model.addAttribute("visaProduct", visaProduct);
		List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("curlist", currencylist);
		model.addAttribute("deptName",departmentService.findById(visaProduct.getDeptId()).getName());
		model.addAttribute("groupCode",visaProduct.getGroupCode());
//		String payOld = request.getParameter("pay");
//		String payList = request.getParameter("payList");
//		if(payOld != null && payOld.length() > 0 ){
//			String[] pay = payOld.split(";");
//			model.addAttribute("currencyId", pay[0]);
//			model.addAttribute("visaPrice", pay[1]);
//			model.addAttribute("visaPay", pay[2]);
//			if(!payOld.substring(payOld.length() - 1).equals(";")){
//				model.addAttribute("otherCost", pay[3]);
//			}
//		}	
		
//		if(payList != null && payList.length() > 0 ){
//			String[] payLis = payList.split(";");
//			if(payLis.length >= 4){
//				model.addAttribute("currencyIdChk", payLis[0]);
//				model.addAttribute("visaPriceChk", payLis[1]);
//				model.addAttribute("visaPayChk", payLis[2]);
//				if(!payList.substring(payList.length() - 1).equals(";")){
//					model.addAttribute("otherCostChk", payLis[3]);
//				}
//			}
//		}
		
		
		return "modules/visaProducts/visaProductEditPrice";
	}

	/**
	 * 进入添加签证产品附件添加
	 * 
	 * @param visaProducts
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "toVisaPriceUploadFile")
	public String toVisaPriceUploadFile(
			@ModelAttribute VisaProducts visaProduct,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		model.addAttribute("sysCountry", CountryUtils.getCountry(visaProduct.getSysCountryId().longValue()).getCountryName_cn());
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		model.addAttribute("visaProduct", visaProduct);
		Currency currency = currencyService.findCurrency(visaProduct.getCurrencyId().longValue());
		model.addAttribute("currency", currency);
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("deptName",departmentService.findById(visaProduct.getDeptId()).getName());
		model.addAttribute("groupCode",visaProduct.getGroupCode());

		return "modules/visaProducts/visaProductUpload";
	}

	/**
	 * 保存签证信息
	 * 
	 * @param request
	 * @param model
	 * @return
	 * 
	 */
	@RequestMapping(value = "save")
	public synchronized String save(@ModelAttribute VisaProducts visaProduct, MultipartHttpServletRequest request, Model model) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		//如果页面指定产品状态时失败，默认为上架状态
		if (visaProduct.getProductStatus() == null) {
			visaProduct.setProductStatus(new Integer(Context.PRODUCT_ONLINE_STATUS));
		}
		
		//已上传的文件id
		String[] docIds = request.getParameterValues("otherfile");
		
		visaProduct.setProCompanyId(companyId);
		//获取一个产品编号
		String productCode = sysIncreaseService.updateSysIncrease(UserUtils.getUser().getCompany().getName(), 
				UserUtils.getUser().getCompany().getId(), null, Context.PRODUCT_NUM_TYPE);
		if(StringUtils.isNotBlank(productCode)){
			visaProduct.setProductCode(productCode);
		}else{
			new RuntimeException("产品编号获取失败");
		}
		//应付账期
		String payableDateStr = request.getParameter("payableDate");
		if(StringUtils.isNotBlank(payableDateStr)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = sdf.parse(payableDateStr);
				visaProduct.setPayableDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		/*C460 可配置手动输入团号*/
		//0:手动输入;1:自动生成
		if(UserUtils.getUser().getCompany().getGroupCodeRuleQZ()==0){
			visaProduct.setGroupCode(request.getParameter("groupCode"));
		}else{//系统原有规则:
			//获取虚拟团号
			String groupCode = "";
			//环球行
			if(68 == companyId) {
				groupCode = activityGroupService.getGroupNumForTTS(visaProduct.getDeptId()+"",null);
			//新行者
			}else if(71 == companyId){
					
			}else if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
				String groupDate = df.format(new Date());
				groupCode = activityGroupService.getGroupNumForMYGJ("DX", groupDate);
			}else{
				groupCode = sysIncreaseService.updateSysIncrease(UserUtils.getUser().getCompany().getName(), 
						companyId, null, Context.GROUP_NUM_TYPE);
			}
			if(StringUtils.isNotBlank(groupCode)) {
				visaProduct.setGroupCode(groupCode);
			}else{
				new RuntimeException("虚拟团号获取失败");
			}
		}
		
		
		visaProduct.setReview(Context.REVIEW_COST_NEW);
		visaProduct.setLockStatus(0);
		if(UserUtils.getUser().getCompany().getUuid().contains("7a81b21a77a811e5bc1e000c29cf2586")){
			visaProduct.setGroupCode(activityGroupService.groupCodeCheck(visaProduct.getGroupCode()));
		}
		Long productId = visaProductsService.save(visaProduct).getId();
		
		//保存产品文件
		if(null != docIds && docIds.length != 0) {
			for(String s : docIds) {
				VisaProductFile visaProductFile = new VisaProductFile();
				visaProductFile.setSrcVisaProductId(productId);
				visaProductFile.setdocInfo(StringUtils.toLong(s));
				visaProductsFileService.saveVisaProductFile(visaProductFile);
			}
		}
		
		return "redirect:" + Global.getAdminPath() + "/visa/visaProducts/list/2";
	}
	
	/**
	 * 显示签证详细信息
	 * 
	 * @param visaProductId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "visaProductsDetail/{visaProductId}")
	public String VisaProductsDetail(@PathVariable String visaProductId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {


		VisaProducts visaProduct = this.visaProductsService.findByVisaProductsId(new Long(visaProductId));
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
		//部门
		String deptName = departmentService.findById(visaProduct.getDeptId()).getName();
		model.addAttribute("deptName",deptName);
		//C460 团号
		model.addAttribute("groupCode",visaProduct.getGroupCode());
		
		//C460签证团号修改记录   
		List<GroupcodeModifiedRecord> groupcodeModifiedRecords  = null;
		groupcodeModifiedRecords = groupcodeModifiedRecordDao.findGroupcodeModifiedRecordByProductIdAndType(Integer.parseInt(visaProductId), new Integer(6));
		model.addAttribute("groupcodeModifiedRecords", groupcodeModifiedRecords);
		//获得该批发商是否配置了团号库功能:1:是,0:否.团号库:包含展示团号的修改记录功能.
		model.addAttribute("is_need_groupCode", UserUtils.getUser().getCompany().getIsNeedGroupCode());
		
		return "modules/visaProducts/visaProductsDetail";
	}

	/**
	 * 删除签证
	 * 
	 * @param visaProducts
	 * @param vpId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */

	// @RequiresPermissions("product_visaProducts:manager:delete")
	@RequestMapping(value = "deleteVisaProducts/{proId}")
	public String deleteVisaProducts(@ModelAttribute VisaProducts visaProducts,
			@PathVariable String proId, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {

		List<Long> idlist = new ArrayList<Long>();

		if (StringUtils.isNotBlank(proId)) {
			idlist.add(Long.parseLong(proId));
			this.visaProductsService.batchDelVisaProducts(idlist);
		}

		model.addAttribute("visaProducts", visaProducts);
		return "redirect:" + Global.getAdminPath() + "/visa/visaProducts/list/2";
	}

	/**
	 * 渠道商登陆只显示所属批发商录过的目标区域 批发商登陆只显示自己录过的目标区域 下午3:11:19
	 * 
	 */

	/**
	 * 批量删除产品
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "batchdelVisaProducts/{visaProductsIds}")
	public String batchdel(@PathVariable String visaProductsIds,
			@ModelAttribute VisaProducts visaProducts,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {

		List<Long> idlist = new ArrayList<Long>();

		if (StringUtils.isNotBlank(visaProductsIds)) {
			String ids[] = visaProductsIds.split(",");
			for (String id : ids) {
				if (StringUtils.isNotBlank(id)) {
					idlist.add(Long.parseLong(id));
				}
			}
			if (idlist.size() != 0) {
				this.visaProductsService.batchDelVisaProducts(idlist);
			}
		}

		model.addAttribute("visaProducts", visaProducts);
		return "redirect:" + Global.getAdminPath() + "/visa/visaProducts/list/2";

	}

	/**
	 * 批量上下架产品
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "batchOnOrOffVisaProducts/{nowStatus}/{visaProductsIds}")
	public String batchOff(@PathVariable String visaProductsIds,
			@PathVariable String nowStatus, 
			@ModelAttribute VisaProducts visaProducts,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {

		List<Long> idlist = new ArrayList<Long>();

		if (StringUtils.isNotBlank(visaProductsIds)) {
			String ids[] = visaProductsIds.split(",");
			for (String id : ids) {
				if (StringUtils.isNotBlank(id))
					idlist.add(Long.parseLong(id));
			}
			if (idlist.size() != 0) {
				
				if("2".equals(nowStatus)) {
					visaProductsService.batchOnOrOffVisaProducts(idlist,
							new Integer(Context.PRODUCT_OFFLINE_STATUS));
				}else if("3".equals(nowStatus)) {
					visaProductsService.batchOnOrOffVisaProducts(idlist,
							new Integer(Context.PRODUCT_ONLINE_STATUS));
				}
				
			}
		}

		model.addAttribute("visaProducts", visaProducts);
		return "redirect:" + Global.getAdminPath() + "/visa/visaProducts/list/" + nowStatus;

	}

	/**
	 * 修改签证信息
	 * @author jiachen
	 * @DateTime 2014-12-4 上午11:29:46
	 * @return String
	 */
	@RequestMapping(value = "mod/{proId}")
	public String modify(@PathVariable String proId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(new Long(proId));
		
		//国家
		List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
		//领区
		List<Object[]> collarZoningList = getArea(visaProducts.getSysCountryId().toString());
		
		model.addAttribute("collarZoningList", collarZoningList);
		model.addAttribute("countryInfoList", countryInfoList);
		//签证类型
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
		model.addAttribute("visaProduct", visaProducts);
		model.addAttribute("deptName",departmentService.findById(visaProducts.getDeptId()).getName());
		
		return "modules/visaProducts/visaProductsMod";
	}

	/**
	 * 修改添加签证产品价格添加
	 * 
	 * @param visaProducts
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "editModVisaPrice")
	public String editmodVisaPrice(@ModelAttribute VisaProducts visaProduct,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		VisaProducts product = visaProductsService.findByVisaProductsId(visaProduct.getId());
		visaProduct.setPayableDate(product.getPayableDate());
		model.addAttribute("sysCountry", CountryUtils.getCountry(visaProduct.getSysCountryId().longValue()).getCountryName_cn());
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("curlist", currencylist);
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("deptName",departmentService.findById(visaProduct.getDeptId()).getName());
		return "modules/visaProducts/visaProductModEditPrice";
	}

	/**
	 * 修改添加签证产品附件添加
	 * @param visaProducts
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "toModVisaPriceUploadFile")
	public String toModVisaPriceUploadFile(
			@ModelAttribute VisaProducts visaProduct,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Date date = visaProduct.getPayableDate();
		if(date != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = sdf.format(date);
			model.addAttribute("payableDate",dateStr);
		}
		model.addAttribute("sysCountry", CountryUtils.getCountry(visaProduct.getSysCountryId().longValue()).getCountryName_cn());
		model.addAttribute("visaType", DictUtils.getSysDicMap("new_visa_type").get(visaProduct.getVisaType().toString()));
		Currency currency = currencyService.findCurrency(visaProduct.getCurrencyId().longValue());
		model.addAttribute("currency", currency);
		model.addAttribute("visaProduct", visaProduct);
		List<Object> docInfoList = visaProductsFileService.findFileListByProId(visaProduct.getId(), false);
		//签证文件
		model.addAttribute("docInfoList", docInfoList);
		model.addAttribute("deptName",departmentService.findById(visaProduct.getDeptId()).getName());
		//C460 团号
		model.addAttribute("groupCode",request.getParameter("groupCode"));
		//C460签证团号修改记录   
		List<GroupcodeModifiedRecord> groupcodeModifiedRecords  = null;
		groupcodeModifiedRecords = groupcodeModifiedRecordDao.findGroupcodeModifiedRecordByProductIdAndType(visaProduct.getId().intValue(), new Integer(6));
		model.addAttribute("groupcodeModifiedRecords", groupcodeModifiedRecords);
		//获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-start-------------------------//
		model.addAttribute("is_need_groupCode", UserUtils.getUser().getCompany().getIsNeedGroupCode());
        //获得该批发商是否具有配置团号库的权限:1:是,0:否.团号库:指的是是否展示修改团号的记录功能.-end---------------------------//
		
		return "modules/visaProducts/visaProductModUpload";
	}

	/**
	 * 更新签证信息
	 * 
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * 
	 */
	@RequestMapping(value = "update")
	public String update(@ModelAttribute VisaProducts visaProduct,
			MultipartHttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		
		VisaProducts onevisaProduct = null;
		if (visaProduct.getId() != null) {
			onevisaProduct = this.visaProductsService
					.findByVisaProductsId(visaProduct.getId());
		}
		if (onevisaProduct != null) {
			//应付账期 add by 2015-11-30
			String dateStr = request.getParameter("payableDate");
			if(StringUtils.isNotBlank(dateStr)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = sdf.parse(dateStr);
					onevisaProduct.setPayableDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
				
			if(StringUtils.isNotBlank(visaProduct.getProductName())) {
				onevisaProduct.setProductName(visaProduct.getProductName());
			}
			if(StringUtils.isNotBlank(visaProduct.getContactPerson())) {
				onevisaProduct.setContactPerson(visaProduct.getContactPerson());
			}
			if (StringUtils.isNotBlank(visaProduct.getCollarZoning())) {
				onevisaProduct.setCollarZoning(visaProduct.getCollarZoning());
			}
	
			if (visaProduct.getVisaType() != null) {
				onevisaProduct.setVisaType(visaProduct.getVisaType());
			}
			if (visaProduct.getReserveMethod() != null) {
				onevisaProduct.setReserveMethod(visaProduct.getReserveMethod());
			}
			if (visaProduct.getSysCountryId() != null) {
				onevisaProduct.setSysCountryId(visaProduct.getSysCountryId());
			}
			if (visaProduct.getStayTime() != null) {
				onevisaProduct.setStayTime(visaProduct.getStayTime());
			}
			if (visaProduct.getCurrencyId() != null) {
				onevisaProduct.setCurrencyId(visaProduct.getCurrencyId());
			}
			if (StringUtils.isNotBlank(visaProduct.getRemark())) {
				onevisaProduct.setRemark(visaProduct.getRemark());
			}
			
			onevisaProduct.setVisaPrice(visaProduct.getVisaPrice());
			onevisaProduct.setVisaPay(visaProduct.getVisaPay());
			/*0258-qyl-限定为懿洋假期-发票税*/
			onevisaProduct.setInvoiceQZ(visaProduct.getInvoiceQZ());
			onevisaProduct.setOtherCost(visaProduct.getOtherCost());
			
			onevisaProduct.setCopy_Project_Name(visaProduct.getCopy_Project_Name());
			onevisaProduct.setCopy_Project_Type(visaProduct.getCopy_Project_Type());
			onevisaProduct.setOriginal_Project_Name(visaProduct.getOriginal_Project_Name());
			onevisaProduct.setOriginal_Project_Type(visaProduct.getOriginal_Project_Type());
	
			onevisaProduct.setUpdateDate(new java.util.Date());
			onevisaProduct.setUpdateBy(UserUtils.getUser());
			//C460 获取修改前的团号
			String groupCodeOld = onevisaProduct.getGroupCode();
			
			//C460 团号手动输入 可配置
			if(UserUtils.getUser().getCompany().getGroupCodeRuleQZ()==0){
				onevisaProduct.setGroupCode(visaProduct.getGroupCode());
			}
			this.visaProductsService.save(onevisaProduct);
			//C460 团号修改记录
			if(UserUtils.getUser().getCompany().getGroupCodeRuleQZ()==0 && !groupCodeOld.equals(visaProduct.getGroupCode())){
				GroupcodeModifiedRecord  groupcodeModifiedRecord = new GroupcodeModifiedRecord();
				groupcodeModifiedRecord.setCreateBy(UserUtils.getUser().getId().intValue());
				groupcodeModifiedRecord.setGroupcodeNew(visaProduct.getGroupCode());
				groupcodeModifiedRecord.setGroupcodeOld(groupCodeOld);
				groupcodeModifiedRecord.setProductId(onevisaProduct.getId().intValue());
				groupcodeModifiedRecord.setProductType(6);//签证
				groupcodeModifiedRecord.setUpdateByName(UserUtils.getUser().getName());
				groupcodeModifiedRecordDao.saveObj(groupcodeModifiedRecord);
			}
	
			//新文件列表
			String[] docInfoIdArr = request.getParameterValues("otherfile");
			List<String> docInfoIdList = new ArrayList<String>();
			if(null != docInfoIdArr && docInfoIdArr.length != 0) {
				docInfoIdList = Arrays.asList(docInfoIdArr);
			}
			//旧文件列表
			List<Object> oldDocInfoList = visaProductsFileService.findFileListByProId(visaProduct.getId(), true);
			String[] oldDocInfoIdArr = null;
			List<String> oldDocInfoIdList = new ArrayList<String>();
			if(!oldDocInfoList.isEmpty()) {
				oldDocInfoIdArr = oldDocInfoList.get(0).toString().split(",");
				oldDocInfoIdList = Arrays.asList(oldDocInfoIdArr);
			}
			
			//如果新组中有不包含旧组的值，说明是删除的
			for(String oldDocInfoId : oldDocInfoIdList) {
				if(!docInfoIdList.contains(oldDocInfoId)) {
					VisaProductFile file = visaProductsFileService.findFile(onevisaProduct.getId().toString(), oldDocInfoId);
					file.setDelFlag(Context.DEL_FLAG_DELETE);
					visaProductsFileService.saveVisaProductFile(file);
				}
			}
			//如果旧组中有不包含新组的值，说明是新增的
			for(String docInfoId : docInfoIdList) {
				if(!oldDocInfoIdList.contains(docInfoId)) {
					VisaProductFile file = new VisaProductFile();
					file.setSrcVisaProductId(onevisaProduct.getId());
					file.setdocInfo(StringUtils.toLong(docInfoId));
					visaProductsFileService.saveVisaProductFile(file);
				}
			}
		}
		return "redirect:" + Global.getAdminPath() + "/visa/visaProducts/list/2";

	}     
	
	/**
	 * 查询签证国家的领区
	 * @author jiachen
	 * @DateTime 2014-12-3 下午01:59:39
	 * @return List<Object[]>
	 */
	@ResponseBody
	@RequestMapping(value = "getArea")
	public List<Object[]> getArea(@RequestParam(value="countryId", required=true) String countryId) {
		return visaProductsService.findVisaCountryArea(StringUtils.toInteger(countryId));
	}
	
	/**
	 * remote校验选择的产品类型是否可用
	 * @author jiachen
	 * @DateTime 2014-12-5 上午11:08:13
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "findMoreProduct")
	public String findMoreProduct(@RequestParam(value="sysCountry", required=true) String sysCountry,
			@RequestParam(value="collarZoning", required=true) String collarZoning,
			@RequestParam(value="visaType", required=true) String visaType,
			@RequestParam(value="proId", required=false) String proId,
			@RequestParam(value="deptId", required=false) String deptId,
			@RequestParam(value="groupCode", required=false) String groupCode) {
		
		
		return visaProductsService.findMoreProduct(StringUtils.toInteger(sysCountry), 
				collarZoning, 
				StringUtils.toInteger(visaType), 
				StringUtils.isNotBlank(proId)?StringUtils.toLong(proId):-1L,
						deptId,groupCode);
	}
	
	/**
	 * 自动匹配测试方法
	 * @author wuqiang
	 * @throws Exception 
	 */
	@RequestMapping(value = "autoCompleteTest")
	public void autoComplete(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
		
//		/*设置编码方式，这个一定要设置，不然传输中文时会出现乱码，编码方式根据环境而变化设置*/
//		response.setCharacterEncoding("UTF-8"); 
//		
//		String keyword = request.getParameter("keyword");//接收input输入框的值
//		System.out.println("keyword = " + keyword);
//		
//		/*数据*/
//		List<Dict> list = DictUtils.getDictList(keyword);
//		
//		PrintWriter out = response.getWriter();
//		
//		/*组装规定格式JSON形式字符串*/
//		StringBuffer data = new StringBuffer(); 
//		
//		/*{"data":[{"title":"ff","result":"gg"},{"title":"mm","result":"hh"}]},返回json形式*/
//		
//		if(null != list && list.size() > 0 ){
//			int num = 5;//如果list集合数据超过5个，则设置取前5个数据
//			if(list.size() <= num){
//				num = list.size();
//			}
//			
//			/*开始组装JSON形式字符串*/
//			data.append("{").append("data").append(":").append("["); 
//			for(int i = 0;i <num;i++){
//				data.append("{").append('"').append("title").append('"').append(':').append('"').append(list.get(i).getLabel()).append('"').append("},");
//			}	
//			JSONObject obj = JSONObject.fromObject(data.substring(0, data.length() - 1) + "]}");
//			/*组装JSON形式字符串结束*/
//			out.print(obj);
//			out.flush();
//		}
		
		/*设置编码方式，这个一定要设置，不然传输中文时会出现乱码，编码方式根据环境而变化设置*/
		response.setContentType("text/html;charset=utf-8");
		
		//接收input输入框的值
		String q = new String(request.getParameter("q").toString().getBytes("ISO-8859-1"), "UTF-8");
		
//		System.out.println("q = " + q);
		
		/**
		 * 根据类型type,从sys_dict表中取值
		 */
		List<Dict> list = DictUtils.getDictList(q);
		
		//组装规定格式JSON对象开始
		/*返回页面的JSON对象*/
		JSONObject obj = new JSONObject();
		/*返回页面JSON对象列表*/
		JSONArray json = new JSONArray();
		
		int jump = 0;
		for(Dict dt : list){
			obj.put("context", dt.getLabel());
			obj.put("ww", dt.getId());
			json.add(obj);
			jump++;
			if(jump == 6){
				break;/*只取6个数*/
			}
		}
  
//		for(int i = 0;i < 6;i++){
//			obj.put("context", i + "你好");
//			obj.put("to", "to" + i);
//			json.add(obj);
//		}
		
//		System.out.println(json.toString());
		
		PrintWriter out = response.getWriter();
		//组装规定格式JSON对象结束
		out.print(json);
		out.flush();
		out.close();
	}
	
}
