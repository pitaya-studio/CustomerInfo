package com.trekiz.admin.modules.wholesalerbase.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.agent.service.SupplyContactsService;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.wholesalerbase.entity.Qualifications;
import com.trekiz.admin.modules.wholesalerbase.entity.WholeArea;
import com.trekiz.admin.modules.wholesalerbase.entity.WholeOfficeType;
import com.trekiz.admin.modules.wholesalerbase.form.OfficeSelectForm;
import com.trekiz.admin.modules.wholesalerbase.form.SupplyContactsForm;
import com.trekiz.admin.modules.wholesalerbase.form.WholeOfficeForm;
import com.trekiz.admin.modules.wholesalerbase.form.WholeOfficeFourForm;
import com.trekiz.admin.modules.wholesalerbase.form.WholeOfficeThreeForm;
import com.trekiz.admin.modules.wholesalerbase.form.WholeOfficeTwoForm;
import com.trekiz.admin.modules.wholesalerbase.service.QualificationsService;
import com.trekiz.admin.modules.wholesalerbase.service.WholeAreaService;
import com.trekiz.admin.modules.wholesalerbase.service.WholeOfficeTypeService;

/**
 * 批发商管理分页列表
 * 
 * @author gao 2015年4月8日
 */
@Controller
@RequestMapping(value = "${adminPath}/manage/saler")
public class wholesalerController {

	@Autowired
	private OfficeService wholesalerService;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private WholeOfficeTypeService wholeOfficeTypeService;
	@Autowired
	private PlatBankInfoService platBankInfoService;
	@Autowired
	private QualificationsService qualificationsService;
	@Autowired
	private SupplyContactsService supplyContactsService;
	@Autowired
	private WholeAreaService wholeAreaService;
	@Autowired
	private SysGeographyService sysGeographyService;
	
	@RequestMapping(value = "salerlist")
	public String gotoWholesalerList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String conn = request.getParameter("conn");
		String companyName = request.getParameter("companyName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String frontier = request.getParameter("frontier");
		String status = request.getParameter("status");
		String typevalue = request.getParameter("typevalue");
		String levelvalue = request.getParameter("levelvalue");
		String parentName = request.getParameter("parentName");
		String orderby = request.getParameter("orderby");

		OfficeSelectForm form = new OfficeSelectForm();
		if (StringUtils.isNotBlank(conn)) {
			form.setConn(conn);
		}
		if (StringUtils.isNotBlank(companyName)) {
			form.setCompanyName(companyName);
		}
		if (StringUtils.isNotBlank(startDate)) {
			form.setStartDate(startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			form.setEndDate(endDate);
		}
		if (StringUtils.isNotBlank(frontier)) {
			form.setFrontier(Integer.valueOf(frontier));
		} else {
			form.setFrontier(1);
		}
		if (StringUtils.isNotBlank(status)) {
			form.setStatus(Integer.valueOf(status));
		} else {
			form.setStatus(1);
		}
		if (StringUtils.isNotBlank(typevalue)) {
			form.setTypevalue(typevalue);
		}
		if (StringUtils.isNotBlank(levelvalue)) {
			form.setLevelvalue(levelvalue);
		}
		if (StringUtils.isNotBlank(parentName)) {
			form.setParentName(parentName);
		}
		if (orderby != null) {
			form.setOrderby(Integer.valueOf(orderby));
		} else {
			form.setOrderby(1);
		}
		
		Page<Office> page =null;
		Integer count =0;
		if(StringUtils.isNotBlank(form.getTypevalue())){
			// 按全部条件查找的返回组
			page = wholesalerService.findOfficePage(form, new Page<Office>(request, response));
			count = wholesalerService.findOfficePageNum(form);
		}else{
			// 按部分条件查找的返回组（不带批发商类型分类）
			page = wholesalerService.findOfficePageWithoutTypePage(form, new Page<Office>(request, response));
			count = wholesalerService.findOfficePageWithoutTypeNum(form);
		}
		// 查找批发商类型
		if(page!=null && page.getList()!=null){
			List<Office> list = page.getList();
			for(Office office : list){
				StringBuffer names = new StringBuffer();
				// 找到批发商类型数组
				List<WholeOfficeType> officeTypeList= wholeOfficeTypeService.findByCompanyID(office.getId().toString());
				if(officeTypeList!=null && !officeTypeList.isEmpty()){
					for(WholeOfficeType con : officeTypeList){
						// 查找类型实体，获取类型名称
						if(con!=null && StringUtils.isNotBlank(con.getSysdefinedictUUID())){
							SysCompanyDictView  sys= sysCompanyDictViewService.findByUuid(con.getSysdefinedictUUID());
							if(sys!=null && StringUtils.isNotBlank(sys.getLabel())){
								names.append(sys.getLabel()+"  ");
							}
						}
					}
				}
				// 写入批发商对象
				office.setSupplierTypeNames(names.toString());
			}
		}
		if (form != null) {
			model.addAttribute("form", form);
		}
		
		model.addAttribute("page", page); // 返回分页
		model.addAttribute("count", count); // 返回查询总量
		return "modules/wholesalerbase/wholesalerList";
	}

	/**
	 * 跳转到新增/修改批发商第一步
	 * 
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "gotoAddWholeOfficeOne/{rid}")
	public String gotoAddWholeOfficeOne(@PathVariable("rid") Long rid,HttpServletResponse response, Model model,
			HttpServletRequest request) {
		// 获取批发商类型(此地获取系统批发商类型)
		List<SysCompanyDictView> typeList = sysCompanyDictViewService
				.findByType(Context.BaseInfo.WHOLESALER_TYPE, Long.valueOf(UserUtils.getCompanyIdForData()));
		// 获取批发商等级(此地获取系统批发商类型)
		List<SysCompanyDictView> levelList = sysCompanyDictViewService
				.findByType(Context.BaseInfo.WHOLESALER_LEVEL, Long.valueOf(UserUtils.getCompanyIdForData()));
		// 获取全部境内区域
		List<Object[]> insidelist = sysGeographyService.getGeographyLevel("guonei");
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< insidelist.size();i++){   
			 sb.append("{ id:"+insidelist.get(i)[0]+", pId:"+insidelist.get(i)[2]+", name:"+"'"+insidelist.get(i)[1]+"'"+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		model.addAttribute("treeData",treeData);

		// 获取全部境外区域
		List<Object[]> outsidelist = sysGeographyService.getGeographyLevel("guoji");
		StringBuffer sbout = new StringBuffer();
		sbout.append("[");
		 for(int i = 0 ; i< outsidelist.size();i++){   
			 sbout.append("{ id:"+outsidelist.get(i)[0]+", pId:"+outsidelist.get(i)[2]+", name:"+"'"+outsidelist.get(i)[1]+"'"+"}," );
	        }   
		 String treeOutsideData=sbout.toString();
		 treeOutsideData=treeOutsideData.substring(0, treeOutsideData.lastIndexOf(","));
		 treeOutsideData=treeOutsideData+"]";
		model.addAttribute("treeOutsideData",treeOutsideData);
		
		// 如果为修正，则需获取批发商
		// 创建批发商实体
		Office office = new Office();
		if(rid!=0){
			/** 修改批发商*/
			office = wholesalerService.findWholeOfficeById(Long.valueOf(rid));
			// 传递修改批发商标记
			model.addAttribute("newORold","1");
			// 获取已选批发商类型
			List<WholeOfficeType> officeTypeList= wholeOfficeTypeService.findByCompanyID(rid.toString());
			if(officeTypeList!=null && !officeTypeList.isEmpty()){
				model.addAttribute("officeTypeList",officeTypeList);
			}
			// 获取批发商联系人
			List<SupplyContacts> list = supplyContactsService.findSupplyContactsByIdAType(Long.valueOf(rid), SupplyContacts.WHO_TYPE);
			if(list!=null && !list.isEmpty()){
				SupplyContacts first = list.get(0);
				SupplyContacts second = list.get(1);
				model.addAttribute("firstSupply",first); // 获得第一个联系人
				model.addAttribute("secondSupply",second); // 获得第二个联系人
				list.remove(first);	// 去掉第一个联系人
				list.remove(second);	// 去掉第二个联系人
				model.addAttribute("supplyContactsList",list);
			}
		}else{
			/** 新建批发商(批发商实体草稿，只为了占位而存在)*/
			office.setParent(wholesalerService.findWholeOfficeById(Long.valueOf(1))); // 添加顶级批发商为父级
			office.setParentIds("0,1");
			office.setFrontier(1);
			office.setName("新建批发商");// 页面不会显示
			office.setDelFlag(Context.DEL_FLAG_TEMP);
			office.setUuid(UuidUtils.generUuid());
			office= wholesalerService.saveOffice(office);
			// 传递新建批发商标记
			model.addAttribute("newORold","0");
		}

		model.addAttribute("office",office);
		model.addAttribute("typeList", typeList);
		model.addAttribute("levelList", levelList);
		return "modules/wholesalerbase/addWholesalerOne";
	}
	/**
	 * 跳转到新增批发商第二步
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="gotoAddWholeOfficeTwo/{rid}/{newORold}")
	public String gotoAddWholeOfficeTwo(@PathVariable("rid") Long rid,@PathVariable("newORold") Integer newORold,HttpServletResponse response, Model model,
			HttpServletRequest request){
		if(rid!=null){
			// 找到刚刚新增的批发商
			Office office = wholesalerService.findWholeOfficeById(rid);
			if(office!=null){
				model.addAttribute("office",office);
				model.addAttribute("newORold",newORold);
				return "modules/wholesalerbase/addWholesalerTwo";
			}
		}
		return null;
	}
	
	/**
	 * 跳转到新增批发商第三步
	 * @author gao
	 * @param rid
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="gotoAddWholeOfficeThree/{rid}/{newORold}")
	public String gotoAddWholeOfficeThree(@PathVariable("rid") Long rid,@PathVariable("newORold") Integer newORold,HttpServletResponse response, Model model,
			HttpServletRequest request){
		if(rid!=null){
			// 找到刚刚新增的批发商
			Office office = wholesalerService.findWholeOfficeById(rid);
			if(office!=null){
				model.addAttribute("office",office);
				model.addAttribute("newORold",newORold);
				// 找到批发商的银行账户
				List<PlatBankInfo> platList = platBankInfoService.findBankInfoList(office.getId(), PlatBankInfo.PLAT_BANK_TYPE_WHO);
				if(platList!=null && !platList.isEmpty()){
					PlatBankInfo firstBank = platList.get(0);
					model.addAttribute("firstPlat",firstBank);
					platList.remove(firstBank);
					model.addAttribute("platList",platList);
				}
				return "modules/wholesalerbase/addWholesalerThree";
			}
		}
		return null;
	}
	
	/**
	 * 跳转到新增批发商第四步
	 * @author gao
	 * @param rid
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="gotoAddWholeOfficeFour/{rid}/{newORold}")
	public String gotoAddWholeOfficeFour(@PathVariable("rid") Long rid,@PathVariable("newORold") Integer newORold,HttpServletResponse response, Model model,
			HttpServletRequest request){
		if(rid!=null){
			// 找到刚刚新增的批发商
			Office office = wholesalerService.findWholeOfficeById(rid);
			if(office!=null){
				model.addAttribute("office",office);
				model.addAttribute("newORold",newORold);
				// 找到批发商的附件
				List<Qualifications> quallist = qualificationsService.getQualificationsByCompanyIdWithOutOther(office.getId());
				if(quallist!=null && !quallist.isEmpty()){
					for(Qualifications q :quallist){
						if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_1){ //营业执照
							model.addAttribute("qual1",q);
						}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_2){// 经营许可证
							model.addAttribute("qual2",q);
						}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_3){// 税务登记证
							model.addAttribute("qual3",q);
						}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_4){// 组织机构代码证
							model.addAttribute("qual4",q);
						}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_5){// 公司法人身份证（正反面一起）
							model.addAttribute("qual5",q);
						}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_6){// 公司银行开户许可证
							model.addAttribute("qual6",q);
						}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_7){// 旅游业资质
							model.addAttribute("qual7",q);
						}
						
					}
					model.addAttribute("qualList",qualificationsService.getQualificationsByCompanyIdOther(office.getId())); // 其他文件
				}
				return "modules/wholesalerbase/addWholesalerFour";
			}
		}
		return null;
	}
	
	/**
	 * 新增批发商 第一步（基本信息填写）
	 * 
	 * @author gao
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addWholeOfficeOne", method = RequestMethod.POST)
	public Map<String, Object> addWholeOfficeOne(@Valid WholeOfficeForm form,
			HttpServletRequest request, HttpServletResponse response,
			BindingResult result) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 检查@Valid的验证结果：是否存在验证不通过的项
		if (result.hasErrors()) {
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors().get(0).getDefaultMessage());
			return map;
		}
		if(form.getId()==null){
			map.put("res", "data_error");
			map.put("mes", "指定批发商不存在");
			return map;
		}
		Office office = wholesalerService.findWholeOfficeById(form.getId());
		if(office==null){
			map.put("res", "data_error");
			map.put("mes", "指定批发商不存在");
			return map;
		}
		/*
		// 判断，如果当前账户是超级管理员，则不可以做任何修改或增加
		if(UserUtils.getUser().getId()==1){
			map.put("res", "data_error");
			map.put("mes", "当前登录账户是系统超级管理员，不可配置批发商");
			return map;
		}*/
		if (StringUtils.isNotBlank(form.getCode())) {
			office.setCode(form.getCode());
		}
		if (StringUtils.isNotBlank(form.getName())) {
			office.setName(form.getName());
		}
		if (StringUtils.isNotBlank(form.getEnname())) {
			office.setEnname(form.getEnname());
		}
		if (StringUtils.isNotBlank(form.getSupplierBrand())) {
			office.setSupplierBrand(form.getSupplierBrand());
		}
		if (StringUtils.isNotBlank(form.getCompanyName())) {
			office.setCompanyName(form.getCompanyName());
		}
		office.setFrontier(form.getFrontier());
		if (form.getCountry() != null) {
			office.setCountryId(form.getCountry());
		}
		if (form.getProvince() != null) {
			office.setProvinceId(form.getProvince());
		}
		if (form.getCity() != null) {
			office.setCityId(form.getCity());
		}
		if (form.getDistrict() != null) {
			office.setDistrictId(form.getDistrict());
		}
		if (StringUtils.isNotBlank(form.getShortAddress())) {
			office.setAddress(form.getShortAddress());
		}
		if (StringUtils.isNotBlank(form.getDistrictCode())) {
			office.setDistrictCode(form.getDistrictCode());
		}
		if (StringUtils.isNotBlank(form.getZipCode())) {
			office.setZipCode(form.getZipCode());
		}
		if (StringUtils.isNotBlank(form.getPhone())) {
			office.setPhone(form.getPhone());
		}
		if (StringUtils.isNotBlank(form.getFax())) {
			office.setFax(form.getFax());
		}
		if (StringUtils.isNotBlank(form.getRemarks())) {
			office.setRemarks(form.getRemarks());
		}
		office.setStatus(form.getStatus());
		
		
		/* 在office表中直接存入国内外覆盖区域ID字符串，方便页面添加 */
		if(StringUtils.isNotBlank(form.getMenuIds())){
			office.setAreaInternal(form.getMenuIds()); // 国内覆盖区域
		}
		if(StringUtils.isNotBlank(form.getMenuIds2())){
			office.setAreaOverseas(form.getMenuIds2()); // 国外覆盖区域
		}
		
		/* 在wholeoffice表中拆分国内外覆盖区域字符串，按ID存入，方便日后按区域查询功能使用 */
		// 拆分字符串(国内)
		if(StringUtils.isNotBlank(form.getMenuIds())){
			String[] inter = form.getMenuIds().split(",");
			for(String in : inter){
				if(StringUtils.isNotBlank(in)){
					WholeArea back = wholeAreaService.findWholeAreaOne(in, office.getId());
					if(back==null){
						WholeArea area = new WholeArea();
						area.setAreaid(Long.valueOf(in));
						area.setCompanyID(office.getId());
						area.setFrontier(WholeArea.AREA_INTERNAL);
						wholeAreaService.save(area);
					}
				}
			}
		}
		// 拆分字符串(国际)
		if(StringUtils.isNotBlank(form.getMenuIds2())){
			String[] overseas = form.getMenuIds2().split(",");
			for(String in : overseas){
				if(StringUtils.isNotBlank(in)){
					WholeArea back = wholeAreaService.findWholeAreaOne(in, office.getId());
					if(back==null){
						WholeArea area = new WholeArea();
						area.setAreaid(Long.valueOf(in));
						area.setCompanyID(office.getId());
						area.setFrontier(WholeArea.AREA_OVERSEAS);
						wholeAreaService.save(area);
					}
				}
			}
		}
		
		
		// 新增批发商等级
		office.setLevel(sysCompanyDictViewService.findByUuid(form.getLevelUuid()));
		// 新增批发商
		wholesalerService.save(office);
		// 新增批发商后，再增加批发商等级和分类
		
		
		// 新增批发商分类
		for(String UUID : form.getTypeUuid()){
			WholeOfficeType officeType = new WholeOfficeType();
			officeType.setCompanyID(office.getId());
			officeType.setSysdefinedictUUID(UUID);
			// 检查该分类是否已经添加
			WholeOfficeType back= wholeOfficeTypeService.findBySysdefinedictUUID(UUID,office.getId());
			if(back==null){// 说明该分类没有被添加
				// 保存批发商分类
				wholeOfficeTypeService.save(officeType);
			}
		}
		
		map.put("res", "success");
		map.put("rid", office.getId());
		// 判断，是提交还是下一步
		if(form.getSaveOrNext()==1){
			finalWholesaler(office.getId());
		}
		return map;
	}
	/**
	 * 新增批发商联系人
	 * @author gao
	 * @param form
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addNewContact", method = RequestMethod.POST)
	public Map<String,Object> addNewContact(@Valid SupplyContactsForm form,
			BindingResult result){
		Map<String, Object> map = new HashMap<String, Object>();
		// 检查@Valid的验证结果：是否存在验证不通过的项
		if (result.hasErrors()) {
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors().get(0).getDefaultMessage());
			return map;
		}
		// 联系人
		// 检查该联系人是否已经添加过
		SupplyContacts sup = new SupplyContacts();
		if(form.getId()!=null){
			sup= supplyContactsService.findOne(form.getId());
		}
		sup.setSupplierId(Long.valueOf(form.getSupplierId()));
		sup.setContactName(form.getContactName());
		sup.setContactMobile(form.getContactMobile());
		sup.setContactPhone(form.getContactPhone());
		sup.setContactEmail(form.getContactEmail());
		sup.setContactFax(form.getContactFax());
		sup.setContactQQ(form.getContactQQ());
		sup.setType(SupplyContacts.WHO_TYPE);
		SupplyContacts back= supplyContactsService.save(sup);
		
		map.put("res", "success");
		map.put("supplyContacts", back);
		return map;
	}
	
	/**
	 * 删除批发商联系人
	 * @author gao
	 * @param form
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delNewContact", method = RequestMethod.POST)
	public Map<String,Object> delNewContact(@Valid SupplyContactsForm form,
			BindingResult result){
		Map<String, Object> map = new HashMap<String, Object>();
		SupplyContacts sup= supplyContactsService.findOne(form.getId());
		if(sup==null){
			map.put("res", "data_error");
			map.put("mes", "未找到该联系人");
		}
		sup.setDelFlag(Context.DEL_FLAG_DELETE);
		supplyContactsService.save(sup);
		map.put("res", "success");
		return map;
	}

	/**
	 *  新增批发商 第二步（网站信息填写）
	 * @author gao
	 * @param form
	 * @param request
	 * @param response
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addWholeOfficeTwo", method = RequestMethod.POST)
	public Map<String, Object> addWholeOfficeTwo(@Valid WholeOfficeTwoForm form,
			HttpServletRequest request, HttpServletResponse response,
			BindingResult result){
		Map<String, Object> map = new HashMap<String, Object>();
		// 检查@Valid的验证结果：是否存在验证不通过的项
		if (result.hasErrors()) {
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors().get(0).getDefaultMessage());
			return map;
		}
		Office office = wholesalerService.findWholeOfficeById(form.getCompanyId());
		if(office==null){
			map.put("res", "data_error");
			map.put("mes", "未找到指定批发商");
			return map;
		}
//		office.setLoginCode(form.getLoginCode());
//		office.setLoginPW(form.getLoginPW());
		office.setLoginMaster(form.getLoginMaster());
		office.setLoginMPhone(form.getLoginMPhone());
		office.setLoginSPhone(form.getLoginSPhone());
		office.setLoginAMail(form.getLoginAMail());
		office.setLoginSQQ(form.getLoginSQQ());
		office.setLoginStatus(form.getLoginStatus());
		office.setLoginShow(form.getLoginShow());
		office.setLoginName(form.getLoginName());
		if(StringUtils.isNotBlank(form.getSalerTripFileId())){
			office.setLogo(Long.valueOf(form.getSalerTripFileId()));
			office.setLoginLogoName(form.getSalerTripFileName());
			office.setLoginLogoPath(form.getSalerTipFilePath());
		}
		office.setLoginArr(form.getLoginArr());
		
		wholesalerService.save(office);
		map.put("res", "success");
		// 判断，是提交还是下一步
		if(form.getSaveOrNext()==1){
			finalWholesaler(office.getId());
		}
		return map;
	}
	
	
	/**
	 * 新增批发商第三步，银行账户(这里是直接提交,下一步的操作在页面上直接跳转即可)
	 * @author gao
	 * @param form
	 * @param request
	 * @param response
	 * @param result
	 * @return
	 */
	@RequestMapping(value="addWholeOfficeThree/{rid}")
	public String addWholeOfficeThree(@PathVariable("rid") Long rid,HttpServletResponse response, Model model,
			HttpServletRequest request){
		if(rid!=null){
			// 找到刚刚新增的批发商
			Office office = wholesalerService.findWholeOfficeById(rid);
			if(office!=null){
				office.setDelFlag(Context.DEL_FLAG_NORMAL);
				wholesalerService.save(office);
				return "modules/wholesalerbase/wholesalerList";
			}
		}
		return null;
	}
	
	/**
	 * 新增银行账户
	 * @author gao
	 * @param form
	 * @param request
	 * @param response
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addNewBlankCode", method = RequestMethod.POST)
	public Map<String, Object> addNewBlankCode(@Valid WholeOfficeThreeForm form,
			HttpServletRequest request, HttpServletResponse response,
			BindingResult result){
		Map<String, Object> map = new HashMap<String, Object>();
		// 检查@Valid的验证结果：是否存在验证不通过的项
		if (result.hasErrors()) {
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors().get(0).getDefaultMessage());
			return map;
		}
		// 获得批发商
		Office office = wholesalerService.findWholeOfficeById(form.getBelongParentPlatId());
		if(office==null){
			map.put("res", "data_error");
			map.put("mes", "未找到指定批发商");
			return map;
		}
		// 保存账户类
		PlatBankInfo info = new PlatBankInfo();
		// 账户ID不为空，说明该账户存在
		if(form.getId()!=null ){
			info = platBankInfoService.findBankInfoById(form.getId());
		}
		info.setAccountName(form.getAccountName());
		info.setDefaultFlag(form.getDefaultFlag());
		info.setBankName(form.getBankName());
		info.setBankAddr(form.getBankAddr());
		info.setBankAccountCode(form.getBankAccountCode());
		info.setPlatType(PlatBankInfo.PLAT_BANK_TYPE_WHO);
		info.setBeLongPlatId(office.getId());
		info.setRemarks(form.getRemarks());
		platBankInfoService.save(info);
		map.put("res", "success");
		return map;
	}
	/**
	 * 删除银行账户
	 * @author gao
	 * @param form
	 * @param request
	 * @param response
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delNewBlankCode", method = RequestMethod.POST)
	public Map<String, Object> delNewBlankCode(@Valid WholeOfficeThreeForm form,
			HttpServletRequest request, HttpServletResponse response,
			BindingResult result){
		Map<String, Object> map = new HashMap<String, Object>();
		// 检查@Valid的验证结果：是否存在验证不通过的项
		if (result.hasErrors()) {
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors().get(0).getDefaultMessage());
			return map;
		}
		// 获得批发商
		Office office = wholesalerService.findWholeOfficeById(form.getBelongParentPlatId());
		if(office==null){
			map.put("res", "data_error");
			map.put("mes", "未找到指定批发商");
			return map;
		}
		// 保存账户类
		PlatBankInfo info = new PlatBankInfo();
		// 账户ID不为空，说明该账户存在
		if(form.getId()!=null ){
			info = platBankInfoService.findBankInfoById(form.getId());
			info.setDelFlag(Context.DEL_FLAG_DELETE);
			platBankInfoService.save(info);
			map.put("res", "success");
			return map;
		}
		map.put("res", "data_error");
		map.put("mes", "未找到指定批发商");
		return map;
	}
	
	/**
	 * 新增批发商第四步 资质上传（最后一步，直接提交）update by yunpeng.zhang
	 * @author gao
	 * @param form
	 * @param request
	 * @param response
	 * @param result
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/addWholeOfficeFour", method = RequestMethod.POST)
	public Map<String, Object> addWholeOfficeFour(@Valid WholeOfficeFourForm form,
			BindingResult result,HttpServletRequest request, 
			HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		// 检查@Valid的验证结果：是否存在验证不通过的项
		if (result.hasErrors()) {
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors().get(0).getDefaultMessage());
			return map;
		}
		// 获得批发商
		Office office = wholesalerService.findWholeOfficeById(form.getCompanyId());
		if(office==null){
			map.put("res", "data_error");
			map.put("mes", "未找到指定批发商");
			return map;
		}
		Long[] salerTripFileIds = form.getSalerTripFileId();
		if(salerTripFileIds!=null && salerTripFileIds.length>0){
			for(int n=0;n<salerTripFileIds.length;n++){
				// 保存资质
				Qualifications qualifications = qualificationsService.getBySalerTripFileId(form.getSalerTripFileId()[n]);
				if(qualifications == null) {
					qualifications = new Qualifications();
					qualifications.setDocInfoId(form.getSalerTripFileId()[n]);
					qualifications.setUuid(UuidUtils.generUuid());
				}
				qualifications.setTitle(form.getTitle()[n]);
				qualifications.setTitleType(form.getTitleType()[n]);
				qualifications.setCompanyId(form.getCompanyId());
				qualifications.setDocInfoName(form.getSalerTripFileName()[n]);
				qualifications.setDocInfoPath(form.getSalerTipFilePath()[n]);
				qualificationsService.save(qualifications);
			}
			map.put("res", "success");
			map.put("rid", office.getId());
			// 提交批发商
			finalWholesaler(office.getId());
		}
		
		return map;
	}
	/**
	 * 提交批发商信息（结束全部批发商资料录入，保存批发商资料，可在第一到第四步中任何一处执行）
	 * 实际就是修改批发商状态，从删除状态改为正常状态
	 * @author gao
	 * @param rid
	 * @return
	 */
	private void finalWholesaler(Long rid){
		Office office = wholesalerService.findWholeOfficeById(rid);
		office.setDelFlag(Context.DEL_FLAG_NORMAL);
		wholesalerService.save(office);
	}
	
	/**
	 * 批发商第二步返回第一步
	 * 
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "backWholeOfficeOne/{rid}/{newORold}")
	public String backWholeOfficeOne(@PathVariable("rid") Long rid,@PathVariable("newORold") Long newORold,
			HttpServletRequest request,HttpServletResponse response, Model model) {
		// 获取批发商类型(此地获取系统批发商类型)
		List<SysCompanyDictView> typeList = sysCompanyDictViewService
				.findByType(Context.BaseInfo.WHOLESALER_TYPE, Long.valueOf(UserUtils.getCompanyIdForData()));
		// 获取批发商等级(此地获取系统批发商类型)
		List<SysCompanyDictView> levelList = sysCompanyDictViewService
				.findByType(Context.BaseInfo.WHOLESALER_LEVEL, Long.valueOf(UserUtils.getCompanyIdForData()));
		// 获取全部境内区域
		List<Object[]> insidelist = sysGeographyService.getGeographyLevel("guonei");
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< insidelist.size();i++){   
			 sb.append("{ id:"+insidelist.get(i)[0]+", pId:"+insidelist.get(i)[2]+", name:"+"'"+insidelist.get(i)[1]+"'"+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		model.addAttribute("treeData",treeData);

		// 获取全部境外区域
		List<Object[]> outsidelist = sysGeographyService.getGeographyLevel("guoji");
		StringBuffer sbout = new StringBuffer();
		sbout.append("[");
		 for(int i = 0 ; i< outsidelist.size();i++){   
			 sbout.append("{ id:"+outsidelist.get(i)[0]+", pId:"+outsidelist.get(i)[2]+", name:"+"'"+outsidelist.get(i)[1]+"'"+"}," );
	        }   
		 String treeOutsideData=sbout.toString();
		 treeOutsideData=treeOutsideData.substring(0, treeOutsideData.lastIndexOf(","));
		 treeOutsideData=treeOutsideData+"]";
		model.addAttribute("treeOutsideData",treeOutsideData);
//		String rid = request.getParameter("rid"); // 获取批发商ID
//		String newORold = request.getParameter("newORold");// 区分是新增还是修改
		
		Office office = new Office();
		List<SupplyContacts> list = new ArrayList<SupplyContacts>();
		if(rid!=null){
			office = wholesalerService.findWholeOfficeById(Long.valueOf(rid));
			model.addAttribute("office", office);
			model.addAttribute("newORold",newORold);
			// 获取批发商联系人列表
			list = supplyContactsService.findSupplyContactsByIdAType(Long.valueOf(rid),SupplyContacts.WHO_TYPE);
			if(list!=null && !list.isEmpty()){
				SupplyContacts first = list.get(0);
				SupplyContacts second = list.get(1);
				model.addAttribute("firstSupply",first); // 获得第一个联系人
				model.addAttribute("secondSupply",second); // 获得第二个联系人
				list.remove(first);	// 去掉第一个联系人
				list.remove(second);	// 去掉第二个联系人
				model.addAttribute("supplyContactsList",list);
			}
		}
		model.addAttribute("typeList", typeList);
		model.addAttribute("levelList", levelList);
		return "modules/wholesalerbase/addWholesalerOne";
	}
	/**
	 * 批发商第三步返回第二步
	 * @author gao
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="backWholeOfficeTwo/{rid}/{newORold}")
	public String backWholeOfficeTwo(@PathVariable("rid") Long rid,@PathVariable("newORold") Long newORold,HttpServletResponse response, Model model,
			HttpServletRequest request){
		if(rid!=null){
			// 找到刚刚修改的批发商
			Office office = wholesalerService.findWholeOfficeById(rid);
			if(office!=null){
				model.addAttribute("office",office);
				model.addAttribute("newORold",newORold);
				return "modules/wholesalerbase/addWholesalerTwo";
			}
		}
		return null;
	}
	
	/**
	 * 批发商第四步返回第三步
	 * @author gao
	 * @param rid
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="backWholeOfficeThree/{rid}/{newORold}")
	public String backWholeOfficeThree(@PathVariable("rid") Long rid,@PathVariable("newORold") Long newORold,HttpServletResponse response, Model model,
			HttpServletRequest request){
		if(rid!=null){
			// 找到刚刚修改的批发商
			Office office = wholesalerService.findWholeOfficeById(rid);
			if(office!=null){
				model.addAttribute("office",office);
				model.addAttribute("newORold",newORold);
				// 找到批发商的银行账户
				List<PlatBankInfo> platList = platBankInfoService.findBankInfoList(office.getId(), PlatBankInfo.PLAT_BANK_TYPE_WHO);
				if(platList!=null && !platList.isEmpty()){
					PlatBankInfo firstBank = platList.get(0);
					model.addAttribute("firstPlat",firstBank);
					platList.remove(firstBank);
					if(!platList.isEmpty()){
						model.addAttribute("platList",platList);
					}
				}
				return "modules/wholesalerbase/addWholesalerThree";
			}
		}
		return null;
	}
	
	/**
	 * 删除批发商
	 * @author gao
	 * @param rid
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delWholeOffice", method = RequestMethod.POST)
	public Map<String,Object> delWholeOffice(HttpServletResponse response,HttpServletRequest request){
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		String rid = request.getParameter("rid");
		if(rid==null){
			map.put("res", "data_error");
			map.put("mes", "未找到指定批发商");
		}
		Office office = wholesalerService.findWholeOfficeById(Long.valueOf(rid));
		if(office==null){
			map.put("res", "data_error");
			map.put("mes", "未找到指定批发商");
		}
		wholesalerService.delWholeOffice(Long.valueOf(rid));
		map.put("res", "success");
		return map;
	}
	/**
	 * 查找批发商详情
	 * @author gao
	 * @param rid
	 * @param newORold
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="wholeOfficeInfo/{rid}")
	public String wholeOfficeInfo(@PathVariable("rid") Long rid,HttpServletResponse response, Model model,
			HttpServletRequest request){
		/** 批发商基本详情*/
		Office office = wholesalerService.findWholeOfficeById(rid);
		/** 批发商类型 */
		StringBuffer names = new StringBuffer();
		List<WholeOfficeType> officeTypeList= wholeOfficeTypeService.findByCompanyID(office.getId().toString());
		if(officeTypeList!=null && !officeTypeList.isEmpty()){
			for(WholeOfficeType con : officeTypeList){
				// 查找类型实体，获取类型名称
				if(con!=null && StringUtils.isNotBlank(con.getSysdefinedictUUID())){
					SysCompanyDictView  sys= sysCompanyDictViewService.findByUuid(con.getSysdefinedictUUID());
					names.append(sys.getLabel()+"  ");
				}
			}
		}
		// 写入批发商对象 
		office.setSupplierTypeNames(names.toString());
		/** 批发商联系人 */
		List<SupplyContacts>  sup= supplyContactsService.findSupplyContactsByIdAType(office.getId(), SupplyContacts.WHO_TYPE);
		/** 批发商账户 */
		List<PlatBankInfo> banklist = platBankInfoService.findBankInfoList(office.getId(), PlatBankInfo.PLAT_BANK_TYPE_WHO);
		/** 批发商上传文件 */
		List<Qualifications> quallist = qualificationsService.getQualificationsByCompanyIdWithOutOther(office.getId());
		if(quallist!=null && !quallist.isEmpty()){
			for(Qualifications q :quallist){
				if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_1){ //营业执照
					model.addAttribute("qual1",q);
				}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_2){// 经营许可证
					model.addAttribute("qual2",q);
				}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_3){// 税务登记证
					model.addAttribute("qual3",q);
				}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_4){// 组织机构代码证
					model.addAttribute("qual4",q);
				}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_5){// 公司法人身份证（正反面一起）
					model.addAttribute("qual5",q);
				}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_6){// 公司银行开户许可证
					model.addAttribute("qual6",q);
				}else if(q!=null && q.getTitleType()==Qualifications.TITLE_TYPE_7){// 旅游业资质
					model.addAttribute("qual7",q);
				}
				
			}
			model.addAttribute("qualList",qualificationsService.getQualificationsByCompanyIdOther(office.getId())); // 其他文件
		}
		// 批发商国内覆盖范围
		if(StringUtils.isNotBlank(office.getAreaInternal())){
			String[] strId = office.getAreaInternal().split(",");
			StringBuffer areaInternals = new StringBuffer();
			for(String id : strId){
				if(StringUtils.isNotBlank(id)){
					SysGeography sys = sysGeographyService.getById(Integer.valueOf(id));
					if(sys!=null){
						areaInternals.append(sys.getNameCn());
						areaInternals.append("   ");
					}
				}
			}
			model.addAttribute("areaInternals",areaInternals);
		}
		// 批发商国际覆盖范围
		if(StringUtils.isNotBlank(office.getAreaOverseas())){
			String[] strId = office.getAreaOverseas().split(",");
			StringBuffer areaoverseas = new StringBuffer();
			for(String id : strId){
				if(StringUtils.isNotBlank(id)){
					SysGeography sys = sysGeographyService.getById(Integer.valueOf(id));
					if(sys!=null){
						areaoverseas.append(sys.getNameCn());
						areaoverseas.append("   ");
					}
				}
			}
			model.addAttribute("areaoverseas",areaoverseas);
		}
		model.addAttribute("office",office);
		model.addAttribute("banklist",banklist);
		model.addAttribute("supplyContacts",sup);
		return "modules/wholesalerbase/wholesalerInfo";
	}
	
}
