package com.trekiz.admin.modules.agent.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.mapper.JsonMapper;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.utils.JSONUtils;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.common.utils.PropertiesLoader;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.AgentInfoContacts;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.PlatBankInfoList;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.agent.repository.SupplyContactsDao;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.service.SupplyContactsService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.service.VisaOrderService;

/**
 * 渠道商控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="${adminPath}/agent/manager")
public class AgentinfoController extends BaseController{

	@Autowired
	private AgentinfoService agentinfoService;
		
	@Autowired
	private PlatBankInfoDao platBankInfoDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private PlatBankInfoService platBankInfoService;
	
	@Autowired
	private SupplyContactsService supplyContactsService;
	
	@Autowired
	private AgentinfoDao agentInfoDao;
	
	@Autowired
	private SupplyContactsDao supplyContactsDao;
	
	@Autowired
	private DictService dictService;

	@Autowired
	private ProductOrderService productOrderService;

	@Autowired
	private IAirTicketOrderService airticketOrderService;

	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 145;
	}
//========2014/12/25 start=====================================================================================	
	  
	private static PropertiesLoader propertiesLoader;
	
	@RequestMapping(value="firstForm",method=RequestMethod.GET)
	public String firstForm(Model model){
		// 当前登录的角色ID
		Long userId = UserUtils.getUser().getId();
		model.addAttribute("userId", userId.toString());
		//1.获取当前登录人根据当前登录人信息获取公司信息;根据公司信息获取该公司下的角色是销售的人员
		model.addAttribute("agentSalers", agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId()));
		//2.获取国家信息
		//model.addAttribute("areaMap", agentinfoService.findAreaInfo(Long.parseLong(Global.getConfig("sysAreaNationID"))));
		model.addAttribute("areaMap",agentinfoService.findCountryInfo());
		List<Object[]> list = agentinfoService.findAllPaymentType();
		//结款方式
		model.addAttribute("paymentMap",list );
		//读取项目配置文件
		propertiesLoader = new PropertiesLoader("application.properties");
		//获取mtourCompanyUuid值
		
		if(null != propertiesLoader.getProperties().get("mtourCompanyUuid")){
			String mtourCompanyUuid =propertiesLoader.getProperties().get("mtourCompanyUuid").toString();
			//简称信息显示状态（美途国际显示）
			if(mtourCompanyUuid.indexOf(UserUtils.getUser().getCompany().getUuid())!=-1){
				model.addAttribute("agentNameShortFlag", 1);
			}
		}
		return "modules/agent/firstForm";
	}
	
	/**
	 * 保存第一步
	 * @param agentInfo
	 * @param model
	 * @return
	 */
	@RequestMapping("saveFirstForm")
	public String saveFirstForm(AgentInfoContacts agentInfoContacts,Model model){
		//跟进销售人员ID暂时存入StrategyLevelName临时存储目录
		Agentinfo ai = agentInfoContacts.getAgentinfo();
		if(ai.getAgentBrand().length() > 50){
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "渠道品牌输入大于50个字符，过长！");
			return Context.ERROR_PAGE;
		}
		String salerIdString = agentinfoService.getIdStrFromSalerIdArray(agentInfoContacts.getAgentinfo().getSalerIdArray());  // 由id数组得逗号连接的字符串
		ai.setAgentSalerId(salerIdString);
		ai.setStatus("0");
		ai.setSupplyId(UserUtils.getUser().getCompany().getId());
//		String agentAddress = ai.getAgentAddress();
		Agentinfo agentInfo = agentinfoService.save(ai);
		if(agentInfoContacts.getContacts()!=null){
			List<SupplyContacts> supplyContactsList =  agentInfoContacts.getContacts().getSupplyContactses();
			for (Iterator<SupplyContacts> item = supplyContactsList.iterator(); item.hasNext();) {
				SupplyContacts supplyContacts = (SupplyContacts) item.next();
				if( (supplyContacts.getContactName()==null||"".equals(supplyContacts.getContactName()))  && (supplyContacts.getContactEmail()==null||"".equals(supplyContacts.getContactEmail())) && (supplyContacts.getContactFax()==null||"".equals(supplyContacts.getContactFax())) &&(supplyContacts.getContactMobile()==null||"".equals(supplyContacts.getContactMobile())) && (supplyContacts.getContactPhone()==null||"".equals(supplyContacts.getContactPhone())) && (supplyContacts.getContactQQ()==null||"".equals(supplyContacts.getContactQQ()))){
					item.remove();
					continue;
				}
				supplyContacts.setSupplierId(agentInfo.getId());
				supplyContacts.setType("0");
				supplyContactsService.save(supplyContacts);
			}
		}
		//保存渠道ID
		model.addAttribute("agentId", agentInfo.getId());
		return "modules/agent/secondForm";
	}
	
	/**
	 * 找出渠道名称的首字母
	 * @date 2015-12-24
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("firstLetter")
	public String firstLetter(HttpServletRequest request){
		String agentName = request.getParameter("agentName");  // 获取渠道名称,渠道名称为必填项，不应该有空数据
		return ChineseToEnglish.getFirstLetter(agentName);
	}
	
	/**
	 * 更新第一步功能
	 * @param agentInfoContacts
	 * @param model
	 * @return
	 */
	@RequestMapping("updateFirst")
	public String updateFirst(AgentInfoContacts agentInfoContacts,Model model){
		Agentinfo ai = agentInfoContacts.getAgentinfo();
		if(ai.getAgentBrand().length() > 50){
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "渠道品牌输入大于50个字符，过长！");
			return Context.ERROR_PAGE;
		}
		Agentinfo ageninfoDB = agentinfoService.findOne(ai.getId());
		ageninfoDB.setSupplyId(UserUtils.getUser().getCompany().getId());
		ageninfoDB.setSupplyName(UserUtils.getUser().getCompany().getName());
		ageninfoDB.setAgentBrand(ai.getAgentBrand());
		ageninfoDB.setAgentName(ai.getAgentName());
		ageninfoDB.setAgentFirstLetter(ai.getAgentFirstLetter());
		ageninfoDB.setAgentNameEn(ai.getAgentNameEn());
		ageninfoDB.setSalesRoom(ai.getSalesRoom());
		//渠道名称简写
		if(StringUtils.isNotEmpty(ai.getAgentNameShort())) {
			ageninfoDB.setAgentNameShort(ai.getAgentNameShort().trim());
		}
		ageninfoDB.setBelongsArea(ai.getBelongsArea());
		ageninfoDB.setBelongsAreaProvince(ai.getBelongsAreaProvince());
		ageninfoDB.setBelongsAreaCity(ai.getBelongsAreaCity());
		ageninfoDB.setAgentSalerId(agentinfoService.getIdStrFromSalerIdArray(ai.getSalerIdArray()));
		ageninfoDB.setPaymentType(ai.getPaymentType());
		ageninfoDB.setPaymentDay(ai.getPaymentDay());
		ageninfoDB.setAgentAddress(ai.getAgentAddress());
		ageninfoDB.setAgentAddressProvince(ai.getAgentAddressProvince());
		ageninfoDB.setAgentAddressCity(ai.getAgentAddressCity());
		ageninfoDB.setAgentAddressStreet(ai.getAgentAddressStreet());
		ageninfoDB.setAgentPostcode(ai.getAgentPostcode());
		ageninfoDB.setAgentTelAreaCode(ai.getAgentTelAreaCode());
		ageninfoDB.setAgentTel(ai.getAgentTel());
		ageninfoDB.setAgentFaxAreaCode(ai.getAgentFaxAreaCode());
		ageninfoDB.setAgentFax(ai.getAgentFax());
		ageninfoDB.setAgentContact(ai.getAgentContact());
		ageninfoDB.setAgentContactMobile(ai.getAgentContactMobile());
		ageninfoDB.setAgentContactTel(ai.getAgentContactTel());
		ageninfoDB.setAgentContactFax(ai.getAgentContactFax());
		ageninfoDB.setAgentContactEmail(ai.getAgentContactEmail());
		ageninfoDB.setAgentContactQQ(ai.getAgentContactQQ());
		ageninfoDB.setRemarks(ai.getRemarks());
		ageninfoDB.setLogo(ai.getLogo());
		Agentinfo agentInfo = agentInfoDao.save(ageninfoDB);
		// 根据渠道商ID删除联系人2~联系人n
		supplyContactsDao.deleteSupplierContacts(agentInfo.getId());
		// 根据渠道商ID添加联系人2~联系人n
		if(agentInfoContacts.getContacts()!=null){
			List<SupplyContacts> supplyContactsList =  agentInfoContacts.getContacts().getSupplyContactses();
			for (Iterator<SupplyContacts> ite = supplyContactsList.iterator(); ite.hasNext();) {
				SupplyContacts supplyContacts = (SupplyContacts) ite.next();
				if(supplyContacts!=null){
					if( (supplyContacts.getContactName()==null||"".equals(supplyContacts.getContactName()))  && (supplyContacts.getContactEmail()==null||"".equals(supplyContacts.getContactEmail())) && (supplyContacts.getContactFax()==null||"".equals(supplyContacts.getContactFax())) &&(supplyContacts.getContactMobile()==null||"".equals(supplyContacts.getContactMobile())) && (supplyContacts.getContactPhone()==null||"".equals(supplyContacts.getContactPhone())) && (supplyContacts.getContactQQ()==null||"".equals(supplyContacts.getContactQQ()))){
						ite.remove();
						continue;
					}
					supplyContacts.setSupplierId(agentInfo.getId());
					supplyContacts.setType("0");
					supplyContactsService.save(supplyContacts);
				}
			}
		}
		model.addAttribute("agentId", agentInfo.getId());
		List<PlatBankInfo> list = platBankInfoDao.findPlatBankInfoByBeLongPlatId(agentInfo.getId(), Context.PLAT_TYPE_QD);
		if((list!=null && list.size()>0)||(ai.getLicense()==null && ai.getBusinessLicense()==null && ai.getTaxCertificate()==null &&ai.getOrganizeCertificate()==null && ai.getIdCard()==null && ai.getBankOpenLicense()==null && ai.getTravelAptitudes()==null && ai.getElseFile()==null)){
			return "redirect:"+Global.getAdminPath()+"/agent/manager/updateSecondForm/"+agentInfo.getId();
		}else{
			return "modules/agent/secondForm";
		}
	}
	
	/**
	 * 页面数据回显(修改操作准备数据)
	 * @return
	 */
	@RequestMapping(value="/updateFirstForm/{id}",method=RequestMethod.GET)
	public String reviewFirstForm(@PathVariable("id") String id,Model model){
		//根据ID把渠道商查出来
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		ai.setSalerIdArray(agentinfoService.getIdArrayFromSalerIdStr(ai.getAgentSalerId()));
		//把渠道商的联系人查出来
		List<SupplyContacts> contactsList = supplyContactsService.findContactsByAgentInfo(Long.parseLong(id));
		
		if(ai.getBelongsArea()!=null){
			Map<String, String> belongsProvinceMap = agentinfoService.findAreaInfo(ai.getBelongsArea());//根据所属国家ID查省
			model.addAttribute("belongsProvinceMap", belongsProvinceMap);
		}
		if(ai.getBelongsAreaProvince()!=null){
			Map<String,String> belongsCityMap = agentinfoService.findAreaInfo(ai.getBelongsAreaProvince());//根据所属省查城市信息
			model.addAttribute("belongsCityMap",belongsCityMap );
		}
		
		if(StringUtils.isNotBlank(ai.getAgentAddress())){
			Map<String,String> addressProvinceMap = agentinfoService.findAreaInfo(Long.parseLong(ai.getAgentAddress()));//根据公司地址所属国家id查省
			model.addAttribute("addressProvinceMap",addressProvinceMap );
		}
		
		if(ai.getAgentAddressProvince()!=null){
			Map<String,String> addressCityMap = agentinfoService.findAreaInfo(ai.getAgentAddressProvince());//根据公司地址所属国家id查市
			model.addAttribute("addressCityMap",addressCityMap );
		}
		model.addAttribute("salerUserId", ai.getAgentSalerId());
		model.addAttribute("agentinfo",ai);
		model.addAttribute("supplyContactsList", contactsList);
		// 1.获取当前登录人根据当前登录人信息获取公司信息;根据公司信息获取该公司下的角色是销售的人员
		Map<String, String> salers = agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId());
		model.addAttribute("agentSalers", salers);
		org.json.JSONArray agentSalersJsonArray = JSONUtils.map2JsonArray(salers);
		model.addAttribute("agentSalersJsonArray", agentSalersJsonArray);
		// 2.获取国家信息
		model.addAttribute("areaMap", agentinfoService.findCountryInfo());
		// 结款方式
		model.addAttribute("paymentMap", agentinfoService.findAllPaymentType());
		model.addAttribute("agentId", id);
		//上传的logo返回到前台
		if(ai.getLogo()!=null){
			model.addAttribute("docInfo",docInfoDao.findOne(ai.getLogo()));
		}                                                                     
		
		//读取项目配置文件
		propertiesLoader = new PropertiesLoader("application.properties");
		//获取mtourCompanyUuid值
		if(null != propertiesLoader.getProperties().get("mtourCompanyUuid")){
			String mtourCompanyUuid =propertiesLoader.getProperties().get("mtourCompanyUuid").toString();
			//简称信息显示状态（美途国际显示）
			if(mtourCompanyUuid.indexOf(UserUtils.getUser().getCompany().getUuid())!=-1){
				model.addAttribute("agentNameShortFlag",1);
			}
		}
		return "modules/agent/updateFirstForm";
	}
	
	
	
	/**
	 * 获取省、市下拉框
	 * @param areaParentId
	 * @param response
	 */
	@RequestMapping(value="getAreaInfoById/{areaParentId}",method=RequestMethod.POST,produces="application/json")
	@ResponseBody
	public void getAreaInfoById(@PathVariable("areaParentId") String areaParentId,HttpServletResponse response){
		Map<String,String> map = agentinfoService.findAreaInfo(Long.parseLong(areaParentId));
		Map<String,String> jsonMap;
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			jsonMap = new HashMap<String,String>();
			String key = iterator.next();
			String value = map.get(key);
			jsonMap.put("id", key);
			jsonMap.put("name", value);
			listMap.add(jsonMap);
		}
		String jsonResult = JSONArray.fromObject(listMap).toString();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			response.getWriter().println(jsonResult);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
	}
	
	@RequestMapping(value="secondForm",method=RequestMethod.GET)
	public String secondForm(PlatBankInfoList banks,Model model){
		
		return "modules/agent/secondForm";
	}

	/**
	 * 对银行账号信息进行校验
	 * @param banks		前端传递的银行账号信息
	 * @return
	 * @author	shijun.liu
     */
	private List<PlatBankInfo> checkPlantBankInfo(PlatBankInfoList banks){
		if(null == banks){
			return null;
		}
		List<PlatBankInfo> platBankInfoList = banks.getBanks();
		if(null == platBankInfoList){
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		//验证出入的账号是否有重复
		Map<String, String> mapBankAccount = new HashMap<String, String>();

		for (Iterator<PlatBankInfo> iterator = platBankInfoList.iterator(); iterator.hasNext();) {
			PlatBankInfo bankInfo = iterator.next();
			//账户名
			String accountName = bankInfo.getAccountName();
			//开户行名称
			String bankName = bankInfo.getBankName();
			//银行账号
			String bankAccount = bankInfo.getBankAccountCode();
			if(mapBankAccount.get(bankAccount) == null || mapBankAccount.get(bankAccount) == ""){
				mapBankAccount.put(bankAccount, bankAccount);
			}else{
				//添加的银行账号重复，不进行重复保存
				iterator.remove();
				continue;
			}
			// 开户行地址
			String bankAddr = bankInfo.getBankAddr();
			//全部为空时，过滤掉
			if(StringUtils.isBlank(accountName) && StringUtils.isBlank(bankName) &&
					StringUtils.isBlank(bankAccount) && StringUtils.isBlank(bankAddr)){
				iterator.remove();
				continue;
			}
			bankInfo.setAccountName(accountName.trim());
			bankInfo.setBankName(bankName.trim());
			bankInfo.setBankAccountCode(bankAccount.trim());
			bankInfo.setBankAddr(bankAddr.trim());
			List<PlatBankInfo> dbPlantInfoList = platBankInfoService.getPlatBankInfo(bankName.trim(), bankAccount.trim());
			//数据库中已存在则不进行再次保存
			if(null != dbPlantInfoList && !dbPlantInfoList.isEmpty()){
				iterator.remove();
				continue;
			}
			String key = bankName.trim() + bankAccount.trim();
			String value = map.get(key);
			if(null == value){
				map.put(key, key);
			}else{
				//添加的银行信息重复，不进行重复保存
				iterator.remove();
				continue;
			}
		}
		return platBankInfoList;
	}
	/**
	 *保存账户信息
	 * @return
	 */
	@RequestMapping("saveBank/{id}")
	public String saveBank(@PathVariable String id,Model model,HttpServletRequest request,PlatBankInfoList banks){
		//渠道ID
		String agentId = id;
		if(banks!=null){
			List<PlatBankInfo> platBankInfoList = checkPlantBankInfo(banks);
			if(null != platBankInfoList){
				for (Iterator<PlatBankInfo> iterator = platBankInfoList.iterator(); iterator.hasNext();) {
					PlatBankInfo platBankInfo = iterator.next();
					platBankInfo.setBeLongPlatId(Long.parseLong(agentId));
					platBankInfo.setPlatType(Context.PLAT_TYPE_QD);
					platBankInfo.setDelFlag("0");
					platBankInfoService.save(platBankInfo);
				}
			}
		}
		model.addAttribute("agentId",id);
		return "modules/agent/thirdForm";
	}
	/**
	 * 账户信息回显
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/updateSecondForm/{id}",method=RequestMethod.GET)
	public String updateSecondForm(@PathVariable String id,Model model){
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		List<PlatBankInfo> banks = platBankInfoDao.findPlatBankInfoByBeLongPlatId(ai.getId(), Context.PLAT_TYPE_QD);
		if (CollectionUtils.isEmpty(banks)) {
			banks.add(new PlatBankInfo());
		}
		model.addAttribute("banks", banks);
		model.addAttribute("agentId", id);
		return "modules/agent/updateSecondForm";
	}
	/**
	 * 修改账户信息
	 * @return
	 */
	@RequestMapping("/updateSecond/{id}")
	public String updateSecond(@PathVariable String id,PlatBankInfoList banks,Model model){
		List<PlatBankInfo> platBankInfoList = checkPlantBankInfo(banks);
		for (Iterator<PlatBankInfo> iterator = platBankInfoList.iterator(); iterator.hasNext();) {
			PlatBankInfo platBankInfo = iterator.next();
			platBankInfo.setBeLongPlatId(Long.parseLong(id));
			platBankInfo.setPlatType(Context.PLAT_TYPE_QD);
			platBankInfo.setDelFlag("0");
			platBankInfoService.save(platBankInfo);
		}
		model.addAttribute("agentId", id);
		return "redirect:"+Global.getAdminPath()+"/agent/manager/updateThirdForm/"+id;
	}
	
	@RequestMapping(value="thirdForm/{id}",method=RequestMethod.GET)
	public String thirdForm(@PathVariable String id,Model model){
		model.addAttribute("agentId", id);
		return "modules/agent/thirdForm";
	}
	
	/**
	 * 保存第三步
	 * @return
	 */
	@RequestMapping("saveThirdForm/{id}")
	public String saveThirdForm(@PathVariable String id,@ModelAttribute Agentinfo agentinfo,Model model,HttpServletRequest request){
		Agentinfo ai = agentInfoDao.findAgentInfoById(Long.parseLong(id));
		ai.setBusinessLicense(agentinfo.getBusinessLicense());
		ai.setLicense(agentinfo.getLicense());
		ai.setTaxCertificate(agentinfo.getTaxCertificate());
		ai.setOrganizeCertificate(agentinfo.getOrganizeCertificate());
		ai.setIdCard(agentinfo.getIdCard());
		ai.setBankOpenLicense(agentinfo.getBankOpenLicense());
		ai.setTravelAptitudes(agentinfo.getTravelAptitudes());
		ai.setElseFile(agentinfo.getElseFile());
		ai.setStatus("1");
		agentInfoDao.save(ai);
//		Agentinfo aio = agentinfoService.findAgentInfoById(Long.parseLong(id));
//		agentinfoService.updateAgentInfo(aio);
		return "redirect:"+Global.getAdminPath()+"/agent/manager/queryList";
	}
	/**
	 * 更新资质
	 * @param id
	 * @param agentinfo
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("updateThird/{id}")
	public String updateThird(@PathVariable String id,@ModelAttribute Agentinfo agentinfo,Model model,HttpServletRequest request){
		agentinfo.setId(Long.parseLong(id));
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		ai.setBusinessLicense(agentinfo.getBusinessLicense());
		ai.setLicense(agentinfo.getLicense());
		ai.setTaxCertificate(agentinfo.getTaxCertificate());
		ai.setOrganizeCertificate(agentinfo.getOrganizeCertificate());
		ai.setIdCard(agentinfo.getIdCard());
		ai.setBankOpenLicense(agentinfo.getBankOpenLicense());
		ai.setTravelAptitudes(agentinfo.getTravelAptitudes());
		ai.setElseFile(agentinfo.getElseFile());
		ai.setStatus("1");
		agentinfoService.updateAgentInfo(ai);
		model.addAttribute("agentId", id);
		return "redirect:"+Global.getAdminPath()+"/agent/manager/queryList";
	}
	
	/**
	 * 资质回显
	 * @return
	 */
	@RequestMapping("updateThirdForm/{id}")
	public String updateThirdForm(@PathVariable String id,Model model){
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		if(ai.getLicense()!=null){
			DocInfo license = docInfoDao.findOne(ai.getLicense());//
			model.addAttribute("license",license );
		}
		if(ai.getBusinessLicense()!=null){
			DocInfo business = docInfoDao.findOne(ai.getBusinessLicense());
			model.addAttribute("business",business );
		}
		if(ai.getTaxCertificate()!=null){
			DocInfo taxCertificate = docInfoDao.findOne(ai.getTaxCertificate());
			model.addAttribute("taxCertificate",taxCertificate );
		}
		if(ai.getOrganizeCertificate()!=null){
			DocInfo organizeCertificate = docInfoDao.findOne(ai.getOrganizeCertificate());
			model.addAttribute("organizeCertificate", organizeCertificate);
		}
		if(ai.getIdCard()!=null){
			DocInfo idCard = docInfoDao.findOne(ai.getIdCard());
			model.addAttribute("idCard", idCard);
		}
		if(ai.getBankOpenLicense()!=null){
			DocInfo bankOpenLicense = docInfoDao.findOne(ai.getBankOpenLicense());
			model.addAttribute("bankOpenLicense",bankOpenLicense );
		}
		if(ai.getTravelAptitudes()!=null){
			DocInfo travelAptitudes = docInfoDao.findOne(ai.getTravelAptitudes());
			model.addAttribute("travelAptitudes",travelAptitudes );
		}
		if(StringUtils.isNotBlank(ai.getElseFile())){
			String [] elseFile = ai.getElseFile().split(";");
			List<DocInfo> elseFileList = new ArrayList<DocInfo>();
			for (int i = 0; i < elseFile.length; i++) {
				String [] arr = elseFile[i].split(",");
				String idStr = arr[0];
//				String nameStr = arr[1];
				String nameStr = "";
				if(arr.length != 1){
					nameStr = arr[1];
				}
				DocInfo di = docInfoDao.findOne(Long.parseLong(idStr));
				di.setElseFileName(nameStr);
				elseFileList.add(di);
			}
			model.addAttribute("elseFileList",elseFileList );
		}
		model.addAttribute("agentId", id);
		return "modules/agent/updateThirdForm";
	}
	
	@RequestMapping(value="/review/{id}",method=RequestMethod.GET)
	public String review(@PathVariable String id,Model model){
		// 根据ID把渠道商查出来
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		ai.setSalerIdArray(agentinfoService.getIdArrayFromSalerIdStr(ai.getAgentSalerId()));
		// 把渠道商的联系人查出来
		List<SupplyContacts> contactsList = supplyContactsService.findContactsByAgentInfo(Long.parseLong(id));
		if(ai.getBelongsArea()!=null){
			Map<String, String> belongsProvinceMap = agentinfoService.findAreaInfo(ai.getBelongsArea());//根据所属国家ID查省
			model.addAttribute("belongsProvinceMap", belongsProvinceMap);
		}
		if(ai.getBelongsAreaProvince()!=null){
			Map<String,String> belongsCityMap = agentinfoService.findAreaInfo(ai.getBelongsAreaProvince());//根据所属省查城市信息
			model.addAttribute("belongsCityMap",belongsCityMap );
		}
		try{
			if(StringUtils.isNotBlank(ai.getAgentAddress())){
				Map<String,String> addressProvinceMap = agentinfoService.findAreaInfo(Long.parseLong(ai.getAgentAddress()));//根据公司地址所属国家id查省
				model.addAttribute("addressProvinceMap",addressProvinceMap );
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(ai.getAgentAddressProvince()!=null){
			Map<String,String> addressCityMap = agentinfoService.findAreaInfo(ai.getAgentAddressProvince());//根据公司地址所属国家id查市
			model.addAttribute("addressCityMap",addressCityMap );
		}	
		model.addAttribute("salerUserId", ai.getAgentSalerId());
		model.addAttribute("agentinfo", ai);
		model.addAttribute("supplyContactsList", contactsList);
		// 1.获取当前登录人根据当前登录人信息获取公司信息;根据公司信息获取该公司下的角色是销售的人员
		Map<String, String> salers = agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId());
		model.addAttribute("agentSalers", salers);
		org.json.JSONArray agentSalersJsonArray = JSONUtils.map2JsonArray(salers);
		model.addAttribute("agentSalersJsonArray", agentSalersJsonArray);
		// 2.获取国家信息
		model.addAttribute("areaMap", agentinfoService.findCountryInfo());
		// 结款方式
		model.addAttribute("paymentMap", agentinfoService.findAllPaymentType());
		// 上传的logo返回到前台
		if(ai.getLogo()!=null){
			model.addAttribute("docInfo",docInfoDao.findOne(ai.getLogo()));
		}
		model.addAttribute("agentId", id);
		//====================================
		//=============   第二步 账户信息：
		List<PlatBankInfo> banks = platBankInfoDao.findPlatBankInfoByBeLongPlatId(ai.getId(), Context.PLAT_TYPE_QD);
		model.addAttribute("banks", banks);
		//===================================
		//=============  第三步  资质信息显示
		if(ai.getLicense()!=null){
			DocInfo license = docInfoDao.findOne(ai.getLicense());//
			model.addAttribute("license",license );
		}
		if(ai.getBusinessLicense()!=null){
			DocInfo business = docInfoDao.findOne(ai.getBusinessLicense());
			model.addAttribute("business",business );
		}
		if(ai.getTaxCertificate()!=null){
			DocInfo taxCertificate = docInfoDao.findOne(ai.getTaxCertificate());
			model.addAttribute("taxCertificate",taxCertificate );
		}
		if(ai.getOrganizeCertificate()!=null){
			DocInfo organizeCertificate = docInfoDao.findOne(ai.getOrganizeCertificate());
			model.addAttribute("organizeCertificate", organizeCertificate);
		}
		if(ai.getIdCard()!=null){
			DocInfo idCard = docInfoDao.findOne(ai.getIdCard());
			model.addAttribute("idCard", idCard);
		}
		if(ai.getBankOpenLicense()!=null){
			DocInfo bankOpenLicense = docInfoDao.findOne(ai.getBankOpenLicense());
			model.addAttribute("bankOpenLicense",bankOpenLicense );
		}
		if(ai.getTravelAptitudes()!=null){
			DocInfo travelAptitudes = docInfoDao.findOne(ai.getTravelAptitudes());
			model.addAttribute("travelAptitudes",travelAptitudes );
		}
		if(StringUtils.isNotBlank(ai.getElseFile())){
			String [] elseFile = ai.getElseFile().split(";");
			List<DocInfo> elseFileList = new ArrayList<DocInfo>();
			for (int i = 0; i < elseFile.length; i++) {
				String [] arr = elseFile[i].split(",");
				String idStr = arr[0];
				String nameStr = "";
				if(arr.length != 1){
					nameStr = arr[1];
				}
				DocInfo di = docInfoDao.findOne(Long.parseLong(idStr));
				di.setElseFileName(nameStr);
				elseFileList.add(di);
			}
			model.addAttribute("elseFileList",elseFileList );
		}
		
		//读取项目配置文件
		propertiesLoader = new PropertiesLoader("application.properties");
		//获取mtourCompanyUuid值
		if(null != propertiesLoader.getProperties().get("mtourCompanyUuid")){
			String mtourCompanyUuid =propertiesLoader.getProperties().get("mtourCompanyUuid").toString();
			//简称信息显示状态（美途国际显示）
			if(mtourCompanyUuid.indexOf(UserUtils.getUser().getCompany().getUuid())!=-1){
				model.addAttribute("agentNameShortFlag",1);
			}
		}
		return "modules/agent/review";
	}
	
	@RequestMapping("queryList")
	public String queryList(@ModelAttribute Agentinfo agentinfo, HttpServletRequest request, HttpServletResponse response, Model model){
		
		//查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        //参数处理：渠道名称、销售ID、创建开始日期、创建结束日期、订单类型、是否是签约渠道：0为签约渠道，1为非签约渠道
        String paras = "agentName,salesRoom,agentTel,salerUserId,beginCreateDate,endCreateDate,orderType,isSignChannel";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
		
		//查询渠道统计信息
		Page<Map<Object, Object>> pageAgent = agentinfoService.getAgentInfo(new Page<Map<Object, Object>>(request, response), mapRequest);
		List<Map<Object, Object>> agentList = pageAgent.getList();
		List<Map<Object, Object>> allAgent = agentinfoService.getAllAgentBySql(mapRequest.get("sql"));
		String allSumTotalMoney = "";//各渠道总的订单金额
		String allSumPayedMoney = "";//各渠道总的实收金额
		String allSumAccountedMoney = "";//各渠道总的达帐金额
		Integer allSumOrderNum = 0;//各渠道总的订单人数
		for (Map<Object, Object> agentMap : agentList) {
			//结算方式
			if (agentMap.get("paymentType") != null) {
				agentMap.put("paymentName", dictService.get(Long.parseLong(agentMap.get("paymentType").toString())).getLabel());
        	}
		}
		for (Map<Object, Object> agentMap : allAgent) {
			//应收
			if (agentMap.get("sumTotalMoney") != null) {
				allSumTotalMoney = moneyAmountService.addOrSubtract(agentMap.get("sumTotalMoney").toString(), allSumTotalMoney, true);
			}
			//实收
			if (agentMap.get("sumPayedMoney") != null) {
				allSumPayedMoney = moneyAmountService.addOrSubtract(agentMap.get("sumPayedMoney").toString(), allSumPayedMoney, true);
			}
			//达帐
			if (agentMap.get("sumAccountedMoney") != null) {
				allSumAccountedMoney = moneyAmountService.addOrSubtract(agentMap.get("sumAccountedMoney").toString(), allSumAccountedMoney, true);
			}
			//订单人数
			if (agentMap.get("sumOrderNum") != null) {
				String sumOrderNum = agentMap.get("sumOrderNum").toString();
				allSumOrderNum += Integer.parseInt(sumOrderNum);
			}
		}
		//内部销售人员的名单
		model.addAttribute("agentSalers", agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("allSumTotalMoney", allSumTotalMoney);
		model.addAttribute("allSumPayedMoney", allSumPayedMoney);
		model.addAttribute("allSumAccountedMoney", allSumAccountedMoney);
		model.addAttribute("allSumOrderNum", allSumOrderNum);
		model.addAttribute("agentinfo", agentinfo);
		model.addAttribute("page", pageAgent);
		
		//add by WangXK20151106 把空白的前3列隐藏。如果是美途的账号就隐藏,且将非签约渠道的标签也隐藏掉
		// 用当前的登陆人公司uuid="42e108f116a8464a902d43831e7b0381"判断
		
		//读取项目配置文件
		propertiesLoader = new PropertiesLoader("application.properties");
		//获取mtourCompanyUuid值
		if(null != propertiesLoader.getProperties().get("mtourCompanyUuid")){
			String mtourCompanyUuid =propertiesLoader.getProperties().get("mtourCompanyUuid").toString();
			//简称信息显示状态（美途国际显示）
			if(mtourCompanyUuid.indexOf(UserUtils.getUser().getCompany().getUuid())!=-1){
				model.addAttribute("flag","1");
			}
		}
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		return "modules/agent/queryList";
	}
	
	@RequestMapping("exportAgentList")
	public void exportExcel(Agentinfo agentinfo, HttpServletRequest request, HttpServletResponse response, Model model){
		//查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        //参数处理：渠道名称、销售ID、创建开始日期、创建结束日期、订单类型、是否是签约渠道：0为签约渠道，1为非签约渠道
        String paras = "agentName,salesRoom,agentTel,salerUserId,beginCreateDate,endCreateDate,orderType,isSignChannel";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
		
		//查询渠道统计信息 
        String[] columns = {"序号","渠道名称","渠道品牌","门市名称","地址","电话","联系人信息","营业额（应收）","营业额（实收）","营业额（达帐）","游客（人）","跟进销售员"};
		Page<Map<Object, Object>> pageAgent = agentinfoService.getAgentInfo(new Page<Map<Object, Object>>(request, response,-1), mapRequest);
		try {
			ExportExcel.createExcleOfAgentList("渠道列表", pageAgent.getList(), columns, "渠道列表", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据ID进行删除
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete/{id}")
	public String deleteAgentinfo(@PathVariable String id,Model model){

		/**
		 * 删除时校验，订单中已存在这个渠道时不可删除 （注：订单中状态为已取消99，已删除111的可以删除）
		 * @date 2016年3月22日
		 */
		long agentId = Long.parseLong(id);
		List<ProductOrderCommon> productOrderList = productOrderService.findByOrderCompany(agentId);
		List<AirticketOrder> airticketOrderList = airticketOrderService.findByAgentId(agentId);
		List<VisaOrder> visaOrderList = visaOrderService.findByAgentId(agentId);
		if(productOrderList.size() > 0 || airticketOrderList.size() > 0 || visaOrderList.size() > 0){
			return "error";
		}

		agentinfoService.delAgentinfo(agentId);
		agentinfoService.delSupplyContacts(agentId);
		Agentinfo ai = agentinfoService.findOne(agentId);
		if(ai.getLogo()!=null){
			agentinfoService.delLogo(ai.getLogo());
		}
		agentinfoService.delBankByAngentinfoId(ai.getId());
		return "success";
//		return "redirect:"+Global.getAdminPath()+"/agent/manager/queryList";
	}
	/**
	 * 根据主键ID删除联系人
	 * @param id
	 * @param model
	 * @param respose
	 */
	@RequestMapping(value="delContact/{id}")
	@ResponseBody
	public void deleteContact(@PathVariable String id,Model model,HttpServletResponse respose){
		//根据主键进行删除
		PrintWriter out = null;
		try {
			agentinfoService.deleteContactById(Long.parseLong(id));
			out = respose.getWriter();
			out.println("1");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
	}
	/**
	 * 根据主键ID进行
	 * @param id
	 * @param model
	 * @param respose
	 */
	@RequestMapping(value="delBankInfoById/{id}")
	@ResponseBody
	public void delBankInfoById(@PathVariable String id,Model model,HttpServletResponse respose){
		// 根据主键进行删除
		PrintWriter out = null;
		try {
			agentinfoService.delBankInfoById(Long.parseLong(id));
			out = respose.getWriter();
			out.println("1");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 根据主键ID进行删除"其他资质"
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="delAptitudes/{id}")
	@ResponseBody
	public void delAptitudes(@PathVariable String id,Model model ,HttpServletRequest request ,HttpServletResponse response){
		String agentInfoId = request.getParameter("agentInfoId");
		String name = request.getParameter("name");
		PrintWriter out = null;
		try {
			//1.把docinfo表中对应的资质删除
			agentinfoService.delAptitudesById(Long.parseLong(id));
			//2.修改agent_info 中的elsefile
			Agentinfo ai = agentinfoService.findOne(Long.parseLong(agentInfoId));
			String elseFileStr = ai.getElseFile();
			String fileStr = id+","+name+";";
			elseFileStr = elseFileStr.replace(fileStr,"");
			ai.setElseFile(elseFileStr);
			agentinfoService.updateAgentInfo(ai);
			out = response.getWriter();
			out.println("1");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	@ResponseBody
	@RequestMapping(value="findAllAgentinfo")
	public String findAllAgentinfo(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> agentList = agentinfoService.findAllAgentinfo(companyId);
		return JsonMapper.getInstance().toJson(agentList);
	}

	
	@ResponseBody
	@RequestMapping(value ="getAgentinfo")
	public Agentinfo getAgentinfo(String agentId) {
		if(StringUtils.isNotBlank(agentId)) {
			return agentinfoService.findOne(Long.valueOf(agentId));
		}else{
			return new Agentinfo();
		}
	}
	
	/**
	 * 根据渠道ids字符串获取渠道json数据
	 * @Description: 
	 * @param @param agentIds
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-1 下午5:58:16
	 */
	@ResponseBody
	@RequestMapping(value ="getAgentinfoJsonBean")
	public String getAgentinfoJsonBean(HttpServletRequest request) {
		String agentinfoIds = request.getParameter("agentinfoIds");
		if(StringUtils.isNotEmpty(agentinfoIds)) {
			List<Agentinfo> agentinfos = agentinfoService.findAgentByIdsWithSaler(Arrays.asList(agentinfoIds.split(",")));
			return agentinfoService.getAgentinfoJsonBean(agentinfos);
		}
		
		return "";
	}
	
	/**
	 * 获取渠道第一联系人信息
	 * @author yang.jiang
	 */
	@ResponseBody
	@RequestMapping(value="getFirstContact")
	public Map<String, Object> getFirstContact(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> returnResult = new HashMap<>();
		
		String agentId = request.getParameter("agentId");
		if (StringUtils.isBlank(agentId)) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "渠道id获取失败");
			return returnResult;
		}
		try {
			Agentinfo agentinfo = agentinfoService.findOne(Long.parseLong(agentId));
			if (agentinfo == null) {
				returnResult.put("flag", "faild");
				returnResult.put("message", "渠道获取失败");
				return returnResult;
			}
			returnResult.put("flag", "success");
			returnResult.put("message", "获取渠道第一联系人信息成功");
			Map<String, Object> contactInfoMap = new HashMap<>();
			contactInfoMap.put("contactName", agentinfo.getAgentContact());
			contactInfoMap.put("contactMobile", agentinfo.getAgentContactMobile());
			returnResult.put("data", contactInfoMap);
		} catch (Exception e) {
			e.printStackTrace();
			returnResult.put("flag", "faild");
			returnResult.put("message", "渠道id非法");
			return returnResult;
		}
		return returnResult;
	}
	
	
	/**
	 * 获取通讯录信息
	 * @author lixin
	 */
	@RequestMapping(value="getAddresslist")
	public String getAddresslist(HttpServletRequest request, HttpServletResponse response,Model model) {
		//判断用户是否上线
		
		//获取通讯录信息
		List<Map<String, String>> addresslist = agentinfoService.getAddressList();
		
		Collections.sort(addresslist,new Comparator<Map<String, String>>(){  
            public int compare(Map<String, String> arg0, Map<String, String> arg1) {  
            	Map<String, String> o1 = (Map<String, String>) arg0;
            	Map<String, String> o2 = (Map<String, String>) arg1;
            	
            	//获取str1 的 首字母
				String first =ChineseToEnglish.getFirstLetter(o1.get("agentBrand"));
				//获取str2的首字母
				String second = ChineseToEnglish.getFirstLetter(o2.get("agentBrand"));
				//比较首字母大小
				return first.compareTo(second);
            }  
        });
		
		Map<String, ArrayList<Map<String, String>>> angetbrandsMap = Maps.newLinkedHashMap();
		
		
		for(Map<String,String> item:addresslist){
			if(item.get("agentBrand").toString() != "思锐创途" && item.get("agentBrand").toString() != "思锐创途销售专用" && item.get("agentBrand").toString() != "测试专用" ){
				ArrayList<Map<String, String>> list = angetbrandsMap.get(item.get("agentBrand").toString());
				if(list == null){
					list = new ArrayList<Map<String, String>>();
					angetbrandsMap.put(item.get("agentBrand"), list);
				}
				list.add(item);
			}
		}
		model.addAttribute("addresslist",angetbrandsMap);
		return "layouts/branchOfAddresslist";
	}
	
	/**
	 * 获取通讯录渠道联系人
	 * @author lixin
	 */
	@RequestMapping(value="getContacts/{id}")
	public String getContracts(@PathVariable("id")String id,Model model) {
		//根据ID把渠道商查出来
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(id));
		
		String country = AreaUtil.findAreaNameById(ai.getAgentAddress());
		String provice = AreaUtil.findAreaNameById(ai.getAgentAddressProvince());
		String city = AreaUtil.findAreaNameById(ai.getAgentAddressCity());
		String street = ai.getAgentAddressStreet();
		if(null != provice && country.contains(provice)){
			provice = "";
		}
		if(null != city && provice.contains(city)){
			city = "";
		}
		if(null != street && city.contains(street)){
			street = "";
		}
		String agentAddressFull = country+provice+city+street;
		
		//把渠道商的联系人查出来
		List<SupplyContacts> contactsList = supplyContactsService.findContactsByAgentInfo(Long.parseLong(id));
		model.addAttribute("agentinfo",ai);
		model.addAttribute("agentAddressFull",agentAddressFull);
		model.addAttribute("supplyContactsList", contactsList);
		return "layouts/contractdetail";
	}

}
