/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.traveler.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.SysTravelerType;
import com.trekiz.admin.modules.hotel.query.SysTravelerTypeQuery;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.hotel.service.SysTravelerTypeService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/travelerType")
public class TravelerTypeController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/traveler/travelertype/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/travelerType/list";
	protected static final String FORM_PAGE = "modules/traveler/travelertype/form";
	protected static final String SHOW_PAGE = "modules/traveler/travelertype/show";
	
	@Autowired
	private TravelerTypeService travelerTypeService;
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private SysTravelerTypeService sysTravelerTypeService;
	
	@RequestMapping(value = "list")
	public String list(TravelerType travelerType, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		travelerType.setDelFlag("0");
		travelerType.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
		List<TravelerType> travelerTypes = travelerTypeService.find(travelerType);
		if(travelerTypes!=null && travelerTypes.size() > 0){
			for(int i=0;i<travelerTypes.size();i++){
				TravelerType travelType = travelerTypes.get(i);
				travelType.setApplyProductName(travelType.getApplyProductName());;
			}
		}
        model.addAttribute("travelerTypes", travelerTypes);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(TravelerType travelerType, Model model) {
		getSysTravelerTypes(model);
		model.addAttribute("travelerType", travelerType);
		return FORM_PAGE;
	}

	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(TravelerType travelerType, Model model) {
		Map<String,Object> datas = new HashMap<String, Object>();
		//添加成人唯一验证
		if(!this.validateTravelerType(null, travelerType.getSysTravelerType())) {
			datas.put("message","3");
			datas.put("error", "系统中已经存在该游客类型，请重新输入!");
			
			return datas;
		}
		
		if(travelerType != null && UserUtils.getCompanyIdForData() != null) {
			travelerType.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
			
			travelerTypeService.save(travelerType);
			datas.put("message","1");
			
			//保存成功后，将该批发商下游客类型的排序做累加去重操作
			Long companyId = UserUtils.getUser().getCompany().getId();
			travelerTypeService.cumulationSortByWholesalerId(companyId.intValue());
			
		} else {
			datas.put("message","3");
			datas.put("error", "系统异常，请重新操作!");
		}
		
		return datas;
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		TravelerType travelerType = travelerTypeService.getByUuid(uuid);
		if(travelerType == null || travelerType.getWholesalerId() != UserUtils.getCompanyIdForData().intValue()) {
			return SHOW_PAGE;
		}
		model.addAttribute("travelerType", travelerType);
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		
		TravelerType travelerType = travelerTypeService.getByUuid(uuid);
		if(travelerType == null || travelerType.getWholesalerId() != UserUtils.getCompanyIdForData().intValue()) {
			return FORM_PAGE;
		}
		
		model.addAttribute("travelerType", travelerType);
		if(StringUtils.isNotEmpty(travelerType.getApplyProduct())) {
			String[] applyProductArr = travelerType.getApplyProduct().split(",");
			model.addAttribute("applyProductArr", applyProductArr);
		}
		getSysTravelerTypes(model);//系统游客类型数据
		return FORM_PAGE;
	}
	
	@ResponseBody
	@RequestMapping(value = "update")
	public Object update(TravelerType travelerType, Model model, HttpServletRequest request) {
		Map<String,Object> datas = new HashMap<String, Object>();
		
		TravelerType entity = null;
		if(travelerType.getUuid() != null) {
			entity = travelerTypeService.getByUuid(travelerType.getUuid());
		} else {
			datas.put("message", "3");
			datas.put("error", "系统异常，请重新操作!");
			return datas;
		}
		
		//添加成人唯一验证
		if(!this.validateTravelerType(travelerType.getUuid(), travelerType.getSysTravelerType())) {
			datas.put("message","3");
			datas.put("error", "系统中已经存在该游客类型，请重新输入!");
			
			return datas;
		}
		
		entity.setName(travelerType.getName());
		entity.setRangeFrom(travelerType.getRangeFrom());
		entity.setRangeTo(travelerType.getRangeTo());
		entity.setSysTravelerType(travelerType.getSysTravelerType());
		entity.setDescription(travelerType.getDescription());
		entity.setStatus(travelerType.getStatus());
		entity.setSort(travelerType.getSort());
		entity.setShortName(travelerType.getShortName());
		entity.setApplyProduct(travelerType.getApplyProduct());
		entity.setPersonType(travelerType.getPersonType());
		
		travelerTypeService.update(entity);
		datas.put("message", "2");
		
		//保存成功后，将该批发商下游客类型的排序做累加去重操作
		Long companyId = UserUtils.getUser().getCompany().getId();
		travelerTypeService.cumulationSortByWholesalerId(companyId.intValue());
		
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		//删除前进行校验
		TravelerType  travelerType = travelerTypeService.getByUuid(uuid);
		if(TravelerType.ALIAS_ADULT_UUID.equals(travelerType.getSysTravelerType())) {
			datas.put("result", "0");
			datas.put("message", "禁止删除对应成人的游客类型！！！");
			return datas;
		}
		
		boolean b = true;
		try {
			
			if(StringUtils.isNotBlank(uuid)){
				travelerTypeService.removeByUuid(uuid);
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "系统发生异常，请重新操作!");
		}
		if(b){
			datas.put("result", "1");
			datas.put("message", "success");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "updateOrder")
	public Object updateOrder(String uuidAndSortsStr) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		Map<String, String> uuidAndSortMap = new HashMap<String, String>();
		boolean b = true;
		try {
			if(StringUtils.isEmpty(uuidAndSortsStr)) {
				
			} else {
				String[] records = uuidAndSortsStr.split(",");
				
				//组装数据
				if(records != null && records.length != 0) {
					for(String record : records) {
						String[] uuidAndSorts = record.split("-");
						uuidAndSortMap.put(uuidAndSorts[0], uuidAndSorts[1]);
					}
				}
				
				//更新酒店星级排序
				Set<String> uuids = uuidAndSortMap.keySet();
				if(uuids != null && !uuids.isEmpty()) {
					for(String uuid : uuids) {
						TravelerType travelerType = travelerTypeService.getByUuid(uuid);
						travelerType.setSort(Integer.parseInt(uuidAndSortMap.get(uuid)));
						travelerTypeService.update(travelerType);
					}
				}
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "系统发生异常,请重新操作!");
			e.printStackTrace();
		}
		
		if(b){
			datas.put("result", "1");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "check")
	public String check(String uuid, HttpServletRequest request) throws IOException {
		String name = request.getParameter("name");
		if(StringUtils.isEmpty(uuid))
		{
			uuid = "0";
		}

		if(travelerTypeService.findIsExist(uuid, name, UserUtils.getCompanyIdForData())) {
			return "false";
		}
		return "true";
	}
	
	@ResponseBody
	@RequestMapping(value = "validateTravelerType")
	public String validateTravelerType(@RequestParam String uuid, HttpServletRequest request) {
		String sysTravelerType = request.getParameter("sysTravelerType");
		return String.valueOf(this.validateTravelerType(uuid, sysTravelerType));
	}
	
	/**
	 * 验证当前批发商能否添加该游客类型(每个批发商只能指定一个相对应的游客类型类型：在酒店加单模块存在限制)
	*<p>Title: validateTravelerType</p>
	* @param sysTravelerType 系统游客类型
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-2 上午10:46:57
	* @throws
	 */
	public boolean validateTravelerType(String uuid, String sysTravelerType) {
		//3b23624f1db94deaa32861d642f56f79是系统成人游客类型 UUid 数据字典表 sys_company_dict_view  type='traveler_type'
		if(StringUtils.isEmpty(uuid)) {
			uuid = "-1";
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		return !travelerTypeService.findIsExistBySysTravelerType(uuid, sysTravelerType, companyId.intValue());
	}
	
	private void getSysTravelerTypes(Model model) {
		SysTravelerTypeQuery sysTravelerType = new SysTravelerTypeQuery();
		sysTravelerType.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		List<SysTravelerType> sysTravelerTypeLists = sysTravelerTypeService.find(sysTravelerType);
		model.addAttribute("sysTravelerTypeLists", sysTravelerTypeLists);
	}
}
