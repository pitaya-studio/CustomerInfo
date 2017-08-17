/**
 *
 */
package com.trekiz.admin.modules.sys.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;
import com.trekiz.admin.modules.sys.service.SysDefineDictService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 菜单Controller
 * @author zj
 * @version 2013-11-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/CompanyDict")
public class SysDefineDictController extends BaseController {

	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 163;
	}
	
	@Autowired
	private SysDefineDictService sysDefineDictService;
	//列表
	@RequestMapping(value = "CompanyDictList")
	public String travelType(String type,Model model) {
		List<SysDefineDict> list = sysDefineDictService.findType(type);
		model.addAttribute("companyDictList", list);
		if("traffic_mode".equals(type)) {
			return "modules/sys/trafficmode/list";
		}
		return "modules/sys/companyDictList";
	}
	//删除
	@RequestMapping(value = "delCompanyDict")
	public String delete(@RequestParam(required=false) Long id,Model model) {
		sysDefineDictService.deleteType(id);
		return "redirect:"+Global.getAdminPath()+"/sys/CompanyDict/CompanyDictList?type="+sysDefineDictService.findOne(id).getType();
	}
	//新增
	@RequestMapping(value = "saveCompanyDict")
	public String save(SysDefineDict sysdefinedict, Model model) {
		sysdefinedict.setCompanyId(StringUtils.toLong(UserUtils.getUser().getCompany().getId()));
		if(StringUtils.isBlank(sysdefinedict.getDefaultFlag())){
			sysdefinedict.setDefaultFlag("0");
		}
		if(sysdefinedict.getId() != null) {
			sysdefinedict.setCreateBy(sysDefineDictService.findOne(sysdefinedict.getId()).getCreateBy());
			sysdefinedict.setCreateDate(sysDefineDictService.findOne(sysdefinedict.getId()).getCreateDate());
		}
		sysDefineDictService.save(sysdefinedict);
		return "redirect:"+Global.getAdminPath()+"/sys/CompanyDict/CompanyDictList?type="+sysdefinedict.getType();
	}
	//修改时回显
	@RequestMapping(value = "companyDictForm")
	public String form(@RequestParam(required=false) Long id,HttpServletRequest request, Model model, SysDefineDict sysDefineDict) {
		if(id!=null) {
			sysDefineDict = sysDefineDictService.findOne(id);
			model.addAttribute("sysdefinedict", sysDefineDict);
		}
		
		//处理交通方式模块
		String type = request.getParameter("type");
		if("traffic_mode".equals(type)) {
			if(id == null) {
				model.addAttribute("sysdefinedict", sysDefineDict);
			}
			return "modules/sys/trafficmode/form";
		}
		
		return "modules/sys/companyDictForm";
	}
	@ResponseBody
	@RequestMapping(value = "check")
	public String check(String type, Long id, String checked, HttpServletRequest request) throws IOException {
		
		String val = request.getParameter(checked);
		if(id==null){id=0l;}
			if(sysDefineDictService.findIsExist(type,id,checked,val).size()!=0) {
				  return "false";
			}
		return "true";
	}
	
	@RequestMapping(value = "show/{id}")
	public String show(@PathVariable String id, HttpServletRequest request, HttpServletResponse response, Model model) {
		String type = request.getParameter("type");
		if(StringUtils.isEmpty(id)) {
			return "redirect:"+Global.getAdminPath()+"/sys/CompanyDict/CompanyDictList?type="+type;
		}
		model.addAttribute("sysdefinedict", sysDefineDictService.findOne(Long.parseLong(id)));
		return "modules/sys/trafficmode/show";
	}
	
	@ResponseBody
	@RequestMapping(value = "updateOrder")
	public Object updateOrder(String idAndSortsStr) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		Map<String, String> uuidAndSortMap = new HashMap<String, String>();
		boolean b = true;
		try {
			if(StringUtils.isEmpty(idAndSortsStr)) {
				
			} else {
				String[] records = idAndSortsStr.split(",");
				
				//组装数据
				if(records != null && records.length != 0) {
					for(String record : records) {
						String[] uuidAndSorts = record.split("-");
						uuidAndSortMap.put(uuidAndSorts[0], uuidAndSorts[1]);
					}
				}
				
				//更新酒店星级排序
				Set<String> ids = uuidAndSortMap.keySet();
				if(ids != null && !ids.isEmpty()) {
					for(String id : ids) {
						SysDefineDict sysDefineDict = sysDefineDictService.findOne(Long.parseLong(id));
						sysDefineDict.setSort(Integer.parseInt(uuidAndSortMap.get(id)));
						sysDefineDictService.update(sysDefineDict);
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
	
}
