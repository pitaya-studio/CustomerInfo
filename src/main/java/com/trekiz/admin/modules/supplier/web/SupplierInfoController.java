package com.trekiz.admin.modules.supplier.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.entity.SupplierInfoContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierWebsiteInfo;
import com.trekiz.admin.modules.supplier.repository.SupplierContactsDao;
import com.trekiz.admin.modules.supplier.repository.SupplierInfoDao;
import com.trekiz.admin.modules.supplier.repository.SupplierWebsiteInfoDao;
import com.trekiz.admin.modules.supplier.service.SupplierContactsService;
import com.trekiz.admin.modules.supplier.service.SupplierInfoService;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 地接社列表
 * 
 * @author ljn
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/supplier/")
public class SupplierInfoController extends BaseController {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(SupplierInfoController.class);

	@Autowired
	private SupplierInfoService supplierInfoService;
	@Autowired
	private SupplierContactsService supplierContactsService;
	@Autowired
	private SupplierContactsDao supplierContactsDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private SupplierInfoDao supplierInfoDao;
	@Autowired
	private PlatBankInfoDao supplierPlatBankInfoDao;
	@Autowired
	private SupplierWebsiteInfoDao supplierWebsiteInfoDao;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;

	/**
	 * 查询地接社列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "supplierInfoList")
	public String supplierInfoList(Model model, HttpServletRequest request, HttpServletResponse response) {

		// 获取查询参数
		Map<String, Object> conditionsMap = prepareQueryCond(request);

		// 地接社列表分页查询
		Page<Map<String, String>> page = supplierInfoService.findSupplierInfo(request,response,conditionsMap,UserUtils.getUser().getCompany().getId());

		// 获取国家信息
		model.addAttribute("areaMap",supplierInfoService.findCountryInfo());
		model.addAttribute("page", page);
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("companyId", UserUtils.getUser().getCompany().getId());
		return "modules/supplier/supplierInfoList";
	}
	
	/**
	 * 地接社详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="supplierDetail")
	public String supplierDetail(@ModelAttribute SupplierInfo supplierInfo,HttpServletRequest request, HttpServletResponse response,Model model){
		String supplierId = request.getParameter("supplierId");
		if(StringUtils.isNotBlank(supplierId)){
			// 地接社类型格式转换
			supplierInfo = supplierInfoService.findSupplierInfoById(Long.parseLong(supplierId));
			if (StringUtils.isNotBlank(supplierInfo.getSupplierType())) {
				String[] supplier = supplierInfo.getSupplierType().split(",");
				StringBuffer supplierBuffer = new StringBuffer();
				for (int i = 0; i < supplier.length; i++) {

					// mod start by jiangyang
//					String supplierType = DictUtils.getDict(supplier[i].trim(), "travel_agency_type").getLabel();
					String supplierType = null;
					SysCompanyDictView dictView = DictUtils.getSysCompanyDictView(supplier[i].trim(), Context.BaseInfo.TRAVEL_AGENCY_TYPE);
					if(dictView != null){
						supplierType = dictView.getLabel();
					}
					// mod end   by jiangyang
					if (i < supplier.length - 1) {
						supplierBuffer.append(supplierType);
						supplierBuffer.append("，");
					} else {
						supplierBuffer.append(supplierType);
					}
				}
				model.addAttribute("supplierType", supplierBuffer.toString());
			}
			
			// 地接社等级 add start by jiangyang
			String supplierLevel = null;
			List<SysCompanyDictView> sysCompanyDictViewList = DictUtils.getSysCompanyDictViewList(Context.BaseInfo.TRAVEL_AGENCY_LEVEL);
			if(CollectionUtils.isNotEmpty(sysCompanyDictViewList)){
				for(SysCompanyDictView sysCompanyDictView : sysCompanyDictViewList){
					if(String.valueOf(supplierInfo.getSupplierLevel()).equals(sysCompanyDictView.getValue())){
						supplierLevel = sysCompanyDictView.getLabel();
						break;
					}
				}				
			}
			model.addAttribute("supplierLevel", supplierLevel);
			//地接社等级 add end   by jiangyang

			// 所属地区信息
			if (StringUtils.isNotBlank(supplierInfo.getBelongArea())) {

				// 多个用逗号隔开的城市ID放进数组
				String[] belongArea = supplierInfo.getBelongArea().split(",");

				//所属地区list
				List<String> belongAreaList = new ArrayList<String>();
				StringBuffer belongAreaBuf = new StringBuffer();
				for (int i = 0; i < belongArea.length; i++) {
					//根据市找到省
					Long province = supplierInfoService.findAreaParentInfo(Long.parseLong(belongArea[i]));
					//根据省找到国家
					Long country = supplierInfoService.findAreaParentInfo(province);
					Map<String,String> belongAreaProvinceMap = supplierInfoService.findAreaInfo(country);
					Map<String,String> belongAreaCityMap = supplierInfoService.findAreaInfo(province);
					//存放当前国家省市的map
					belongAreaBuf.append(supplierInfoService.findCountryInfo().get(country.toString()));
					belongAreaBuf.append("-");
					belongAreaBuf.append(belongAreaProvinceMap.get(province.toString()));
					belongAreaBuf.append("-");
					belongAreaBuf.append(belongAreaCityMap.get(belongArea[i]));
					belongAreaList.add(belongAreaBuf.toString());
					belongAreaBuf.delete(0, belongAreaBuf.length());
				}
				model.addAttribute("belongAreaList", belongAreaList);
			}

			// 公司地址信息
			StringBuffer companyAddrBuf = new StringBuffer();
			if (StringUtils.isNotBlank(supplierInfo.getCompanyAddr())) {
				Map<String,String> addressProvinceMap = supplierInfoService.findAreaInfo(Long.parseLong(supplierInfo.getCompanyAddr()));//根据公司地址所属国家id查省
				companyAddrBuf.append(supplierInfoService.findCountryInfo().get(supplierInfo.getCompanyAddr().toString()));
				if (StringUtils.isNotBlank(supplierInfo.getCompanyAddrProvince()) && !"省(直辖市)".equals(supplierInfo.getCompanyAddrProvince())) {
					Map<String,String> addressCityMap = supplierInfoService.findAreaInfo(Long.parseLong(supplierInfo.getCompanyAddrProvince()));//根据公司地址所属国家id查市
					companyAddrBuf.append("-");
					companyAddrBuf.append(addressProvinceMap.get(supplierInfo.getCompanyAddrProvince().toString()));
					if (StringUtils.isNotBlank(supplierInfo.getCompanyAddrCity()) && !"市(区)".equals(supplierInfo.getCompanyAddrCity())) {
						companyAddrBuf.append("-");
						companyAddrBuf.append(addressCityMap.get(supplierInfo.getCompanyAddrCity().toString()));
					}
				}
				companyAddrBuf.append(" ");
				companyAddrBuf.append(supplierInfo.getDetailAddr());
				model.addAttribute("companyAddr", companyAddrBuf.toString());
			}

			// 电话号码
			if (StringUtils.isNotBlank(supplierInfo.getPhoneCode()) && StringUtils.isNotBlank(supplierInfo.getPhone())) {
				StringBuffer phoneBuf = new StringBuffer();
				phoneBuf.append(supplierInfo.getPhoneCode());
				phoneBuf.append("-");
				phoneBuf.append(supplierInfo.getPhone());
				model.addAttribute("phone", phoneBuf.toString());
			}

			// 传真
			if (StringUtils.isNotBlank(supplierInfo.getFaxCode()) && StringUtils.isNotBlank(supplierInfo.getFax())) {
				StringBuffer faxBuf = new StringBuffer();
				faxBuf.append(supplierInfo.getFaxCode());
				faxBuf.append("-");
				faxBuf.append(supplierInfo.getFax());
				model.addAttribute("fax", faxBuf.toString());
			}

			// 取得地接社联系人
			List<SupplierContacts> contactsList = supplierContactsService.findContactsBySupplierInfo(Long.parseLong(supplierId));
			if (null != contactsList && contactsList.size() > 0) {
				model.addAttribute("supplierContactsList", contactsList);
			}

			//上传的logo返回到前台
			if (null != supplierInfo.getLogo()) {
				model.addAttribute("docInfo", docInfoDao.findOne(supplierInfo.getLogo()));
			}
			model.addAttribute("supplierInfo", supplierInfo);

			// 网站信息
			SupplierWebsiteInfo supplierWebsiteInfo = supplierInfoService.selectSupplierWebsiteInfo(Long.parseLong(supplierId));
			model.addAttribute("supplierWebsiteInfo", supplierWebsiteInfo);

			// 银行账号
			List<String> banks = supplierInfoService.getSupplierPlatBankInfo(Long.parseLong(supplierId));
			if (null != banks && banks.size() > 0) {
				model.addAttribute("banks", banks);
			}

			// 资质上传
			// 营业执照
			if (null != supplierInfo.getBusinessLicense()) {
				DocInfo businessLicense = docInfoDao.findOne(supplierInfo.getBusinessLicense());
				model.addAttribute("businessLicense",businessLicense);
			}
			// 经营许可证
			if (null != supplierInfo.getBusinessCertificate()) {
				DocInfo businessCertificate = docInfoDao.findOne(supplierInfo.getBusinessCertificate());
				model.addAttribute("businessCertificate",businessCertificate);
			}
			// 税收登记证
			if (null != supplierInfo.getTaxCertificate()) {
				DocInfo taxCertificate = docInfoDao.findOne(supplierInfo.getTaxCertificate());
				model.addAttribute("taxCertificate",taxCertificate);
			}
			// 组织机构代码证
			if (null != supplierInfo.getOrganizeCertificate()) {
				DocInfo organizeCertificate = docInfoDao.findOne(supplierInfo.getOrganizeCertificate());
				model.addAttribute("organizeCertificate", organizeCertificate);
			}
			// 企业法人身份证
			if (null != supplierInfo.getIdCard()) {
				DocInfo idCard = docInfoDao.findOne(supplierInfo.getIdCard());
				model.addAttribute("idCard", idCard);
			}
			// 银行开户许可证
			if (null != supplierInfo.getBankOpenLicense()) {
				DocInfo bankOpenLicense = docInfoDao.findOne(supplierInfo.getBankOpenLicense());
				model.addAttribute("bankOpenLicense",bankOpenLicense);
			}
			// 旅游业资质
			if (null != supplierInfo.getTravelAptitudes()) {
				DocInfo travelAptitudes = docInfoDao.findOne(supplierInfo.getTravelAptitudes());
				model.addAttribute("travelAptitudes",travelAptitudes);
			}
			// 其他上传文件
			if (StringUtils.isNotBlank(supplierInfo.getElseFile())) {
				String [] elseFile = supplierInfo.getElseFile().split(";");
				List<DocInfo> elseFileList = new ArrayList<DocInfo>();
				for (int i = 0; i < elseFile.length; i++) {
					String [] arr = elseFile[i].split(",");
					String idStr = arr[0];
					String nameStr = "";
					if (arr.length > 1) {
						nameStr = arr[1];
					}
					DocInfo di = docInfoDao.findOne(Long.parseLong(idStr));
					if(null != di) {
						di.setElseFileName(nameStr);
					}
					elseFileList.add(di);
				}
				model.addAttribute("elseFileList",elseFileList);
			}
		}
		return "modules/supplier/supplierDetail";
	}

	/**
	 * 删除地接社信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteSupplierInfo")
	public String deleteSupplierInfo(@RequestParam(required=false) Long id,Model model) {

		// 删除地接社
		supplierInfoService.deleteSupplierInfo(id);
		// 取得地接社联系人
		List<SupplierContacts> contactsList = supplierContactsService.findContactsBySupplierInfo(id);
		// 删除现在的联系人
		if (null != contactsList && contactsList.size() > 0) {
			supplierContactsService.deleteSupplierContacts(id);
		}
		// 删除现在地接社与地接社类型关联数据
		supplierInfoService.deleteSupplierMap(id);

		return "redirect:"+Global.getAdminPath()+"/supplier/supplierInfoList/";
	}

	/**
	 * 新增修改地接社信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "supplierFirstForm")
	public String supplierFirstForm(HttpServletRequest request, Model model){
		String supplierId = request.getParameter("supplierId");
		model.addAttribute("supplierId", supplierId);
		model.addAttribute("companyId", UserUtils.getUser().getCompany().getId());
		if(StringUtils.isNotBlank(supplierId)){
			// 根据ID取得地接社信息
			SupplierInfo ai = supplierInfoService.findSupplierInfoById(Long.parseLong(supplierId));
			if (null != ai) {
				// 地接社信息
				model.addAttribute("supplierInfo",ai);
	
				// 所属地区信息
				if (StringUtils.isNotBlank(ai.getBelongArea())) {
					String[] belongArea = ai.getBelongArea().split(",");

					//所属地区list
					List<Map<String,String>> belongAreaList = new ArrayList<Map<String,String>>();
					for (int i = 0; i < belongArea.length; i++) {
						//根据市找到省
						Long province = supplierInfoService.findAreaParentInfo(Long.parseLong(belongArea[i]));
						//根据省找到国家
						Long country = supplierInfoService.findAreaParentInfo(province);
						Map<String,String> belongAreaProvinceMap = supplierInfoService.findAreaInfo(country);
						Map<String,String> belongAreaCityMap = supplierInfoService.findAreaInfo(province);
						//存放当前国家省市的map
						Map<String,String> curArea = new HashMap<String,String>();
						curArea.put("belongArea", country.toString());
						curArea.put("belongsAreaProvince", province.toString());
						curArea.put("belongsAreaCity", belongArea[i]);
						belongAreaList.add(supplierInfoService.findCountryInfo());//国家
						belongAreaList.add(belongAreaProvinceMap);//省
						belongAreaList.add(belongAreaCityMap);//市
						belongAreaList.add(curArea);
					}
					model.addAttribute("belongAreaList", belongAreaList);
				} else {
					// 获取国家信息
					model.addAttribute("areaMap",supplierInfoService.findCountryInfo());
				}

				// 公司地址信息
				if (StringUtils.isNotBlank(ai.getCompanyAddr()) && StringUtils.isNotBlank(ai.getCompanyAddrProvince()) && !"省(直辖市)".equals(ai.getCompanyAddrProvince())) {
					Map<String,String> addressProvinceMap = supplierInfoService.findAreaInfo(Long.parseLong(ai.getCompanyAddr()));//根据公司地址所属国家id查省
					Map<String,String> addressCityMap = supplierInfoService.findAreaInfo(Long.parseLong(ai.getCompanyAddrProvince()));//根据公司地址所属国家id查市
					model.addAttribute("areaMap", supplierInfoService.findCountryInfo());
					model.addAttribute("addressProvinceMap", addressProvinceMap);
					model.addAttribute("addressCityMap", addressCityMap);
				} else {
					// 获取国家信息
					model.addAttribute("areaMap",supplierInfoService.findCountryInfo());
				}

				// 取得地接社联系人
				List<SupplierContacts> contactsList = supplierContactsService.findContactsBySupplierInfo(Long.parseLong(supplierId));
				if (null != contactsList && contactsList.size() > 0) {
					if (contactsList.size() == 1) {
						contactsList.add(new SupplierContacts());
					}
					model.addAttribute("supplierContactsList", contactsList);
				}

				//上传的logo返回到前台
				if (null != ai.getLogo()) {
					model.addAttribute("docInfo", docInfoDao.findOne(ai.getLogo()));
				}
			}
		} else {
			// 获取国家信息
			model.addAttribute("areaMap",supplierInfoService.findCountryInfo());
		}
		
		//mod start by jiangyang
		SysCompanyDictView sysCompanyDictView = new SysCompanyDictView();
		if(StringUtils.isBlank(sysCompanyDictView.getType())){
			sysCompanyDictView.setType(Context.BaseInfo.TRAVEL_AGENCY_TYPE);
		}
		sysCompanyDictView.setDelFlag("0");
		sysCompanyDictView.setCompanyId(getCompanyId());
		List<SysCompanyDictView> typeList = sysCompanyDictViewService.find(sysCompanyDictView);
        sysCompanyDictView.setType(Context.BaseInfo.TRAVEL_AGENCY_LEVEL);
        List<SysCompanyDictView> levelList = sysCompanyDictViewService.find(sysCompanyDictView);

        //获取地接社类型
        model.addAttribute("supplierTypeList", typeList);
        model.addAttribute("supplierTypeName", Context.BaseInfo.TRAVEL_AGENCY_TYPE_TITLE);
        //获取地接社等级
        model.addAttribute("supplierLevelList", levelList);
        model.addAttribute("supplierLevelName", Context.BaseInfo.TRAVEL_AGENCY_LEVEL_TITLE);
		
//		//获取地接社类型
//		model.addAttribute("supplierTypeList", supplierTypeSort(DictUtils.getDictList(Context.BaseInfo.TRAVEL_AGENCY_TYPE)));
//		model.addAttribute("supplierTypeName", Context.BaseInfo.TRAVEL_AGENCY_TYPE_TITLE);
//		//获取地接社等级
//		model.addAttribute("supplierLevelList", DictUtils.getDictList(Context.BaseInfo.TRAVEL_AGENCY_LEVEL));
//		model.addAttribute("supplierLevelName", Context.BaseInfo.TRAVEL_AGENCY_LEVEL_TITLE);
        //mod end   by jiangyang
		//获取当前用户公司Id
		model.addAttribute("companyId", UserUtils.getUser().getCompany().getId());
		
		return "modules/supplier/supplierFirstForm";
	}
	
	/**
	 * 把地接社其他类型放到最后
	 * @param dictList
	 * @return
	 */
	private List<Dict> supplierTypeSort(List<Dict> dictList) {
		if (CollectionUtils.isNotEmpty(dictList)) {
			for (int i = 0; i < dictList.size() -1; i++) {
				if ("10".equals(dictList.get(i).getValue())) {
					Dict temp = dictList.get(dictList.size()-1);
					dictList.set(dictList.size()-1, dictList.get(i));
					dictList.set(i, temp);
				}
			}
			
		}
		return dictList;
	}

	/**
	 * 保存第一步
	 * @param supplierInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveFirstForm")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String saveFirstForm(SupplierInfoContacts supplierInfoContacts, Model model, HttpServletRequest request, HttpServletResponse response){
		String supplierId = request.getParameter("supplierId");
		model.addAttribute("supplierId", supplierId);

		// 地接社类型
		String supplierTypeArr [] = request.getParameterValues("supplierType");
		String supplierTypes = "";
		if (supplierTypeArr != null) {
			for (int i=0;i<supplierTypeArr.length;i++) {
				if (i == supplierTypeArr.length-1) {
					supplierTypes += supplierTypeArr[i];
				} else {
					supplierTypes += supplierTypeArr[i] + ",";
				}
			}
		}
		// 地接社等级
		String supplierLevel = request.getParameter("supplierLevel");

		// 所属地区城市
		StringBuffer belongsAreaCityBuf = new StringBuffer();
		String[] belongsAreaCity = request.getParameterValues("belongsAreaCity");
		for (int i = 0; i < belongsAreaCity.length; i++) {
			if (StringUtils.isNotBlank(belongsAreaCity[i])) {
				if (i < belongsAreaCity.length - 1) {
					belongsAreaCityBuf.append(belongsAreaCity[i]);
					belongsAreaCityBuf.append(",");
				} else {
					belongsAreaCityBuf.append(belongsAreaCity[i]);
				}
			}
		}

		// 修改
		if(StringUtils.isNotBlank(supplierId)){

			// 保存地接社基本信息及地接社联系人
			SupplierInfo ai = supplierInfoService.findSupplierInfoById(Long.parseLong(supplierId));
			ai.setSupplierType(supplierTypes);
			if(StringUtils.isNotBlank(supplierLevel)){				
				ai.setSupplierLevel(Integer.valueOf(supplierLevel));
			}
			ai.setBelongArea(belongsAreaCityBuf.toString());
			if (StringUtils.isNotBlank(request.getParameter("logo"))) {
				ai.setLogo(Long.parseLong(request.getParameter("logo")));
			}

			SupplierInfo supplierInfo = supplierInfoContacts.getSupplierInfo();
			ai.setId(Long.parseLong(supplierId));
			ai.setSupplierBrand(supplierInfo.getSupplierBrand());
			ai.setSupplierName(supplierInfo.getSupplierName());
			ai.setCompanyEnName(supplierInfo.getCompanyEnName());
			ai.setCompanyAddr(supplierInfo.getCompanyAddr());
			if (!"省(直辖市)".equals(supplierInfo.getCompanyAddrProvince())) {
				ai.setCompanyAddrProvince(supplierInfo.getCompanyAddrProvince());
			}
			if (!"市(区)".equals(supplierInfo.getCompanyAddrCity())) {
				ai.setCompanyAddrCity(supplierInfo.getCompanyAddrCity());
			}
			ai.setDetailAddr(supplierInfo.getDetailAddr());
			ai.setPhoneCode(supplierInfo.getPhoneCode());
			ai.setPhone(supplierInfo.getPhone());
			ai.setFaxCode(supplierInfo.getFaxCode());
			ai.setFax(supplierInfo.getFax());
			ai.setDescription(supplierInfo.getDescription());
			supplierInfoService.saveSupplierInfo(ai);
			// 取得地接社联系人
			List<SupplierContacts> contactsList = supplierContactsService.findContactsBySupplierInfo(Long.parseLong(supplierId));
			// 删除现在的联系人
			if (null != contactsList && contactsList.size() > 0) {
				supplierContactsService.deleteSupplierContacts(Long.parseLong(supplierId));
			}
			// 添加新联系人
			List<SupplierContacts> supplierContactsList =  supplierInfoContacts.getContacts().getSupplierContactses();
			for (Iterator<SupplierContacts> ite = supplierContactsList.iterator(); ite.hasNext();) {
				SupplierContacts supplierContacts = (SupplierContacts) ite.next();
				if (StringUtils.isNotBlank(supplierContacts.getContactName())) {
					supplierContacts.setSupplierId(Long.parseLong(supplierId));
					supplierContacts.setType("1");
					supplierContactsService.saveContacts(supplierContacts);
				}
			}

			// 删除现在地接社与地接社类型关联数据
			supplierInfoService.deleteSupplierMap(Long.parseLong(supplierId));
			// 添加新的地接社与地接社类型关联数据
			if (supplierTypeArr != null) {
				for (int i = 0; i < supplierTypeArr.length; i++) {
					supplierInfoService.insertSupplierMap(Long.parseLong(supplierTypeArr[i]), Long.parseLong(supplierId));
				}
			}

			// 保存地接社ID
			model.addAttribute("supplierId", supplierId);

			// 获得网站信息
			SupplierWebsiteInfo websiteInfo = supplierInfoService.findWebsiteInfo(Long.parseLong(supplierId));
			if (null != websiteInfo && null != websiteInfo.getId()) {
				model.addAttribute("websiteInfo", websiteInfo);
			}
			return "modules/supplier/supplierSecondForm";
		// 新增
		} else {
			// 保存地接社基本信息及地接社联系人
			SupplierInfo ai = supplierInfoContacts.getSupplierInfo();
			ai.setSupplierType(supplierTypes);
			if(StringUtils.isNotBlank(supplierLevel)){				
				ai.setSupplierLevel(Integer.valueOf(supplierLevel));
			}
			ai.setBelongArea(belongsAreaCityBuf.toString());
			if ("省(直辖市)".equals(ai.getCompanyAddrProvince())) {
				ai.setCompanyAddrProvince(null);
			}
			if ("市(区)".equals(ai.getCompanyAddrCity())) {
				ai.setCompanyAddrCity(null);
			}
			if (StringUtils.isNotBlank(request.getParameter("logo"))) {
				ai.setLogo(Long.parseLong(request.getParameter("logo")));
			}

			ai.setStatus("0");
			if (null == ai.getId()) {
				ai.setCompanyId(UserUtils.getUser().getCompany().getId());
				//添加地接社Uuid
				ai.setUuid(UuidUtils.generUuid());
			}
			SupplierInfo supplierInfo = supplierInfoService.saveSupplierInfo(ai);
			// 添加联系人
			List<SupplierContacts> supplierContactsList =  supplierInfoContacts.getContacts().getSupplierContactses();
			for (Iterator<SupplierContacts> ite = supplierContactsList.iterator(); ite.hasNext();) {
				SupplierContacts supplierContacts = (SupplierContacts) ite.next();
				if (StringUtils.isNotBlank(supplierContacts.getContactName())) {
					supplierContacts.setSupplierId(supplierInfo.getId());
					supplierContacts.setType("1");
					supplierContactsService.saveContacts(supplierContacts);
				}
			}
			// 添加地接社与地接社类型关联数据
			if (supplierTypeArr != null) {
				for (int i = 0; i < supplierTypeArr.length; i++) {
					supplierInfoService.insertSupplierMap(Long.parseLong(supplierTypeArr[i]), supplierInfo.getId());
				}
			}

			// 保存地接社ID
			model.addAttribute("supplierId", supplierInfo.getId());
			return "modules/supplier/supplierSecondForm";
		}
	}

	/**
	 * 获取省、市下拉框
	 * @param areaParentId
	 * @param response
	 */
	@RequestMapping(value="getAreaInfoById/{areaParentId}",method=RequestMethod.POST,produces="application/json")
	@ResponseBody
	public void getAreaInfoById(@PathVariable("areaParentId") String areaParentId,HttpServletResponse response){
		Map<String,String> map = supplierInfoService.findAreaInfo(Long.parseLong(areaParentId));
		Map<String,String> jsonMap;
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			jsonMap = new HashMap<String,String>();
			String key = (String) iterator.next();
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

	/**
	 * 获取地区下拉框
	 * @param areaParentId
	 * @param response
	 */
	@RequestMapping(value="getAreaById/{areaParentId}",method=RequestMethod.POST,produces="application/json")
	@ResponseBody
	public void getAreaById(@PathVariable("areaParentId") String areaParentId,HttpServletResponse response){
		Map<String,String> map = supplierInfoService.findArea(areaParentId);
		Map<String,String> jsonMap;
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			jsonMap = new HashMap<String,String>();
			String key = (String) iterator.next();
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

	/**
	 * 获取查询条件
	 * @param request
	 * @return conditionsMap
	 */
	private Map<String, Object> prepareQueryCond(HttpServletRequest request) {

		Map<String,Object> conditionsMap = new HashMap<String,Object>();

		// 按钮标识
		String search = request.getParameter("search");
		// 地接社名称
		String supplierName = request.getParameter("supplierName");
		// 地接社类型
		String supplierType = request.getParameter("supplierType");
		// 国家
		String country = request.getParameter("country");
		// 地区下拉框
		if (StringUtils.isNotBlank(country)) {
			conditionsMap.put("areaMap", supplierInfoService.findArea(country));
		}
		// 地区
		String area = request.getParameter("area");
		// 创建日期
		String minCreateDate = request.getParameter("minCreateDate");
		// 创建日期
		String maxCreateDate = request.getParameter("maxCreateDate");
		// 创建日期排序标识
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");
		// 更新日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");
		// 列表创建日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");
		// 列表更新日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");
		
		if(orderCreateDateSort == null && orderUpdateDateSort == null ){
			orderCreateDateSort = "desc";
			orderCreateDateCss = "activitylist_paixu_moren";
		}

		conditionsMap.put("supplierName", supplierName);
		conditionsMap.put("supplierType", supplierType);
		conditionsMap.put("country", country);
		conditionsMap.put("area", area);
		conditionsMap.put("minCreateDate", minCreateDate);
		conditionsMap.put("maxCreateDate", maxCreateDate);
		conditionsMap.put("orderCreateDateSort", orderCreateDateSort);
		conditionsMap.put("orderUpdateDateSort", orderUpdateDateSort);
		conditionsMap.put("orderCreateDateCss", orderCreateDateCss);
		conditionsMap.put("orderUpdateDateCss", orderUpdateDateCss);
		conditionsMap.put("search", search);

		return conditionsMap;
	}

	/**
	 * 保存第二步
	 * @param model
	 * @return
	 */
	@RequestMapping(value="saveSecondForm")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String saveSecondForm(SupplierWebsiteInfo supplierWebsiteInfo,HttpServletRequest request, Model model){
		String supplierId = request.getParameter("supplierId");
		model.addAttribute("supplierId", supplierId);
		if(StringUtils.isNotBlank(supplierId)){
			SupplierWebsiteInfo websiteInfo = supplierInfoService.findWebsiteInfo(Long.parseLong(supplierId));
			if (null != websiteInfo && null != websiteInfo.getId()) {
				supplierWebsiteInfo.setId(websiteInfo.getId());
			}

			// 网站状态
			if ("on".equals(supplierWebsiteInfo.getSiteStatus())) {
				supplierWebsiteInfo.setSiteStatus("1");
			} else {
				supplierWebsiteInfo.setSiteStatus("0");
			}

			// 登陆密码
			if (StringUtils.isBlank(supplierWebsiteInfo.getLoginPwd())) {
				supplierWebsiteInfo.setLoginPwd("111111");
			}

			supplierInfoService.saveWebsite(supplierWebsiteInfo);

			// 获取银行账户
			List<String> banks = supplierInfoService.getSupplierPlatBankInfo(Long.parseLong(supplierId));
			if (null != banks && banks.size() > 0) {
				model.addAttribute("banks", banks);
			}
		}
		return "modules/supplier/supplierThirdForm";
	}

	/**
	 * 第二步回显
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "supplierSecondForm")
	public String supplierSecondForm(HttpServletRequest request, Model model){
		String supplierId = request.getParameter("supplierId");
		model.addAttribute("supplierId", supplierId);
		if(StringUtils.isNotBlank(supplierId)){
			// 获得网站信息
			SupplierWebsiteInfo websiteInfo = supplierInfoService.findWebsiteInfo(Long.parseLong(supplierId));
			if (null != websiteInfo && null != websiteInfo.getId()) {
				model.addAttribute("websiteInfo", websiteInfo);
			}
		}
		return "modules/supplier/supplierSecondForm";
	}

	/**
	 * 保存第三步
	 * @param model
	 * @return
	 */
	@RequestMapping(value="saveThirdForm")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String saveThirdForm(HttpServletRequest request, Model model){
		String supplierId = request.getParameter("supplierId");
		model.addAttribute("supplierId", supplierId);

		if(StringUtils.isNotBlank(supplierId)){
			// 获取银行账户
			List<String> banks = supplierInfoService.getSupplierPlatBankInfo(Long.parseLong(supplierId));
			if (null != banks && banks.size() > 0) {
				// 删除现有银行账户
				supplierInfoService.deleteSupplierPlatBankInfo(Long.parseLong(supplierId));
			}

			StringBuffer accountBuf = new StringBuffer();
			String[] accountSize = request.getParameterValues("accountId");
			String defFlag = request.getParameter("defaultFlag");
			for (int i = 0; i < accountSize.length; i++) {
				String defaultFlag = "1";
				if (accountSize[i].equals(defFlag)) {
					defaultFlag = "0";
				}
				accountBuf.append("banks[");
				accountBuf.append(accountSize[i]);
				accountBuf.append("].accountName");
				String accountName = request.getParameter(accountBuf.toString());
				accountBuf.delete(0, accountBuf.length());
				accountBuf.append("banks[");
				accountBuf.append(accountSize[i]);
				accountBuf.append("].bankName");
				String bankName = request.getParameter(accountBuf.toString());
				accountBuf.delete(0, accountBuf.length());
				accountBuf.append("banks[");
				accountBuf.append(accountSize[i]);
				accountBuf.append("].bankAddr");
				String bankAddr = request.getParameter(accountBuf.toString());
				accountBuf.delete(0, accountBuf.length());
				accountBuf.append("banks[");
				accountBuf.append(accountSize[i]);
				accountBuf.append("].bankAccountCode");
				String bankAccountCode = request.getParameter(accountBuf.toString());
				accountBuf.delete(0, accountBuf.length());
				accountBuf.append("banks[");
				accountBuf.append(accountSize[i]);
				accountBuf.append("].remarks");
				String remarks = request.getParameter(accountBuf.toString());
				accountBuf.delete(0, accountBuf.length());
				if (StringUtils.isNotBlank(accountName) || StringUtils.isNotBlank(bankName)
						|| StringUtils.isNotBlank(bankAddr) || StringUtils.isNotBlank(bankAccountCode)) {
				// 插入银行账户
				supplierInfoService.insertSupplierPlatBankInfo(defaultFlag, accountName, bankName, 
						bankAddr, bankAccountCode, remarks, Long.parseLong(supplierId));
				}
			}

			SupplierInfo supplierInfo = supplierInfoService.findSupplierInfoById(Long.parseLong(supplierId));

			// 营业执照
			if (null != supplierInfo.getBusinessLicense()) {
				DocInfo businessLicense = docInfoDao.findOne(supplierInfo.getBusinessLicense());
				model.addAttribute("businessLicense",businessLicense);
			}
			// 经营许可证
			if (null != supplierInfo.getBusinessCertificate()) {
				DocInfo businessCertificate = docInfoDao.findOne(supplierInfo.getBusinessCertificate());
				model.addAttribute("businessCertificate",businessCertificate);
			}
			// 税收登记证
			if (null != supplierInfo.getTaxCertificate()) {
				DocInfo taxCertificate = docInfoDao.findOne(supplierInfo.getTaxCertificate());
				model.addAttribute("taxCertificate",taxCertificate);
			}
			// 组织机构代码证
			if (null != supplierInfo.getOrganizeCertificate()) {
				DocInfo organizeCertificate = docInfoDao.findOne(supplierInfo.getOrganizeCertificate());
				model.addAttribute("organizeCertificate", organizeCertificate);
			}
			// 企业法人身份证
			if (null != supplierInfo.getIdCard()) {
				DocInfo idCard = docInfoDao.findOne(supplierInfo.getIdCard());
				model.addAttribute("idCard", idCard);
			}
			// 银行开户许可证
			if (null != supplierInfo.getBankOpenLicense()) {
				DocInfo bankOpenLicense = docInfoDao.findOne(supplierInfo.getBankOpenLicense());
				model.addAttribute("bankOpenLicense",bankOpenLicense);
			}
			// 旅游业资质
			if (null != supplierInfo.getTravelAptitudes()) {
				DocInfo travelAptitudes = docInfoDao.findOne(supplierInfo.getTravelAptitudes());
				model.addAttribute("travelAptitudes",travelAptitudes);
			}
			// 其他上传文件
			if (StringUtils.isNotBlank(supplierInfo.getElseFile())) {
				String [] elseFile = supplierInfo.getElseFile().split(";");
				List<DocInfo> elseFileList = new ArrayList<DocInfo>();
				for (int i = 0; i < elseFile.length; i++) {
					String [] arr = elseFile[i].split(",");
					String idStr = arr[0];
					String nameStr = "";
					if (arr.length > 1) {
						nameStr = arr[1];
					}
					DocInfo di = docInfoDao.findOne(Long.parseLong(idStr));
					if (null != di) {
						di.setElseFileName(nameStr);
					}
					elseFileList.add(di);
				}
				model.addAttribute("elseFileList",elseFileList);
			}
		}
		return "modules/supplier/supplierFourthForm";
	}

	/**
	 * 第三步回显
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "supplierThirdForm")
	public String supplierThirdForm(HttpServletRequest request, Model model){
		String supplierId = request.getParameter("supplierId");
		model.addAttribute("supplierId", supplierId);
		if(StringUtils.isNotBlank(supplierId)){
			// 获取银行账户
			List<String> banks = supplierInfoService.getSupplierPlatBankInfo(Long.parseLong(supplierId));
			if (null != banks && banks.size() > 0) {
				model.addAttribute("banks", banks);
			}
		}
		return "modules/supplier/supplierThirdForm";
	}

	/**
	 * 保存第四步
	 * @return
	 */
	@RequestMapping("saveFourthForm")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String saveFourthForm(Model model,HttpServletRequest request){
		String supplierId = request.getParameter("supplierId");
		model.addAttribute("supplierId", supplierId);
		if (StringUtils.isNotBlank(supplierId)) {
			SupplierInfo supplierInfo = supplierInfoService.findSupplierInfoById(Long.parseLong(supplierId));

			// 营业执照
			if (StringUtils.isNotBlank(request.getParameter("supplierInfo.businessLicense"))) {
				supplierInfo.setBusinessLicense(Long.parseLong(request.getParameter("supplierInfo.businessLicense")));
			}
			// 经营许可证
			if (StringUtils.isNotBlank(request.getParameter("supplierInfo.businessCertificate"))) {
				supplierInfo.setBusinessCertificate(Long.parseLong(request.getParameter("supplierInfo.businessCertificate")));
			}
			// 税收登记证
			if (StringUtils.isNotBlank(request.getParameter("supplierInfo.taxCertificate"))) {
				supplierInfo.setTaxCertificate(Long.parseLong(request.getParameter("supplierInfo.taxCertificate")));
			}
			// 组织机构代码证
			if (StringUtils.isNotBlank(request.getParameter("supplierInfo.organizeCertificate"))) {
				supplierInfo.setOrganizeCertificate(Long.parseLong(request.getParameter("supplierInfo.organizeCertificate")));
			}
			// 企业法人身份证
			if (StringUtils.isNotBlank(request.getParameter("supplierInfo.idCard"))) {
				supplierInfo.setIdCard(Long.parseLong(request.getParameter("supplierInfo.idCard")));
			}
			// 银行开户许可证
			if (StringUtils.isNotBlank(request.getParameter("supplierInfo.bankOpenLicense"))) {
				supplierInfo.setBankOpenLicense(Long.parseLong(request.getParameter("supplierInfo.bankOpenLicense")));
			}
			// 旅游业资质
			if (StringUtils.isNotBlank(request.getParameter("supplierInfo.travelAptitudes"))) {
				supplierInfo.setTravelAptitudes(Long.parseLong(request.getParameter("supplierInfo.travelAptitudes")));
			}
			// 其他上传文件
			String[] elseFileSize = request.getParameterValues("elseFileSize");
			if (elseFileSize.length > 0) {
				StringBuffer elseFileBuf = new StringBuffer();
				for (int i = 0; i < elseFileSize.length; i++) {
					if (StringUtils.isNotBlank(request.getParameter("elseFileId[".concat(elseFileSize[i]).concat("]")))) {
						elseFileBuf.append(request.getParameter("elseFileId[".concat(elseFileSize[i]).concat("]")));
						elseFileBuf.append(",");
						elseFileBuf.append(request.getParameter("elseFileName[".concat(elseFileSize[i]).concat("]")));
						elseFileBuf.append(";");
					}
				}
				if (StringUtils.isNotBlank(elseFileBuf.toString())) {
					supplierInfo.setElseFile(elseFileBuf.toString());
				}
			}
			supplierInfo.setStatus("1");
			supplierInfoDao.save(supplierInfo);
		}
		return "redirect:"+Global.getAdminPath()+"/supplier/supplierInfoList/";
	}

	/**
	 * 第四步回显
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "supplierFourthForm")
	public String supplierFourthForm(HttpServletRequest request, Model model){
		String supplierId = request.getParameter("supplierId");
		model.addAttribute("supplierId", supplierId);
		if(StringUtils.isNotBlank(supplierId)){
			SupplierInfo supplierInfo = supplierInfoService.findSupplierInfoById(Long.parseLong(supplierId));
			if (null != supplierInfo) {
				// 营业执照
				if (null != supplierInfo.getBusinessLicense()) {
					DocInfo businessLicense = docInfoDao.findOne(supplierInfo.getBusinessLicense());
					model.addAttribute("businessLicense",businessLicense);
				}
				// 经营许可证
				if (null != supplierInfo.getBusinessCertificate()) {
					DocInfo businessCertificate = docInfoDao.findOne(supplierInfo.getBusinessCertificate());
					model.addAttribute("businessCertificate",businessCertificate);
				}
				// 税收登记证
				if (null != supplierInfo.getTaxCertificate()) {
					DocInfo taxCertificate = docInfoDao.findOne(supplierInfo.getTaxCertificate());
					model.addAttribute("taxCertificate",taxCertificate);
				}
				// 组织机构代码证
				if (null != supplierInfo.getOrganizeCertificate()) {
					DocInfo organizeCertificate = docInfoDao.findOne(supplierInfo.getOrganizeCertificate());
					model.addAttribute("organizeCertificate", organizeCertificate);
				}
				// 企业法人身份证
				if (null != supplierInfo.getIdCard()) {
					DocInfo idCard = docInfoDao.findOne(supplierInfo.getIdCard());
					model.addAttribute("idCard", idCard);
				}
				// 银行开户许可证
				if (null != supplierInfo.getBankOpenLicense()) {
					DocInfo bankOpenLicense = docInfoDao.findOne(supplierInfo.getBankOpenLicense());
					model.addAttribute("bankOpenLicense",bankOpenLicense);
				}
				// 旅游业资质
				if (null != supplierInfo.getTravelAptitudes()) {
					DocInfo travelAptitudes = docInfoDao.findOne(supplierInfo.getTravelAptitudes());
					model.addAttribute("travelAptitudes",travelAptitudes);
				}
				// 其他上传文件
				if (StringUtils.isNotBlank(supplierInfo.getElseFile())) {
					String [] elseFile = supplierInfo.getElseFile().split(";");
					List<DocInfo> elseFileList = new ArrayList<DocInfo>();
					for (int i = 0; i < elseFile.length; i++) {
						String [] arr = elseFile[i].split(",");
						String idStr = arr[0];
						String nameStr = "";
						if (arr.length > 1) {
							nameStr = arr[1];
						}
						DocInfo di = docInfoDao.findOne(Long.parseLong(idStr));
						if (null != di) {
							di.setElseFileName(nameStr);
						}
						elseFileList.add(di);
					}
					model.addAttribute("elseFileList",elseFileList);
				}
			}
		}
		return "modules/supplier/supplierFourthForm";
	}
}
