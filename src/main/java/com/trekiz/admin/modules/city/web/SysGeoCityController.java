/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.city.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.city.entity.SysGeoCity;
import com.trekiz.admin.modules.city.service.SysGeoCityService;
import com.trekiz.admin.modules.geography.service.SysGeographyService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/sysGeoCity")
public class SysGeoCityController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/city/cityList";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/list";
	protected static final String FORM_PAGE = "modules/city/form";
	protected static final String SHOW_PAGE = "modules/city/show";
	
	@Autowired
	private SysGeoCityService sysGeoCityService;
	@Autowired
	private SysGeographyService sysGeographyService;
	
	
	@RequestMapping(value = "cityList")
	public String cityList(SysGeoCity sysGeoCity, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Map<String,Object>> fromArea =sysGeoCityService.getGeoCityList("from_area");
		List<Map<String,Object>> arrivalsArea =sysGeoCityService.getGeoCityList("arrivals_area");
		List<Map<String,Object>> outArea =sysGeoCityService.getGeoCityList("out_area");
		List<Map<String,Object>> IntermodalArea =sysGeoCityService.getGeoCityList("Intermodal_area");
        model.addAttribute("fromArea", fromArea);
        model.addAttribute("outArea", outArea);
        model.addAttribute("arrivalsArea", arrivalsArea);
        model.addAttribute("IntermodalArea", IntermodalArea);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(SysGeoCity sysGeoCity, Model model) {
		model.addAttribute("sysGeoCity", sysGeoCity);
		return FORM_PAGE;
	}

	@RequestMapping(value = "save")
	public String save(SysGeoCity sysGeoCity, Model model, RedirectAttributes redirectAttributes) {
		
		sysGeoCityService.save(sysGeoCity);
		return RE_LIST_PAGE;
	}
	
	@RequestMapping(value = "show/{id}")
	public String show(@PathVariable String id, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(id)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("sysGeoCity", sysGeoCityService.getById(Integer.parseInt(id)));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		if(StringUtils.isEmpty(id)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("sysGeoCity", sysGeoCityService.getById(Integer.parseInt(id)));
		return FORM_PAGE;
	}
	
	@RequestMapping(value = "update")
	public String update(SysGeoCity sysGeoCity, Model model, RedirectAttributes redirectAttributes) {
		
		sysGeoCityService.update(sysGeoCity);
		return RE_LIST_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String ids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(ids)){
				String[] idsArray = ids.split(",");
				if(idsArray!=null && idsArray.length>0){
					for(String id : idsArray){
						if(StringUtils.isNotBlank(id)){
							sysGeoCityService.removeById(Integer.parseInt(id));
						}
					}
				}
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "error");
		}
		if(b){
			datas.put("result", "1");
			datas.put("message", "success");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	//添加出发城市，到达城市，离境城市等信息使用
	@RequestMapping(value = "addcity")
	public String addcity(HttpServletRequest request, HttpServletResponse response, Model model){
		//传递类似“startCityShow”，以便选取父菜单中的值，设置默认选中效果
		String searchId= request.getParameter("searchId");
		List<Object[]> list = sysGeographyService.getAllGeographyList();
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< list.size();i++){   
			 sb.append("{ id:"+list.get(i)[0]+", pId:"+list.get(i)[2]+", name:"+"'"+list.get(i)[1]+"'"+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		model.addAttribute("treeData",treeData);
		model.addAttribute("searchId",searchId);
		return "modules/city/city";
	}
	
	@ResponseBody
	@RequestMapping(value = "updateAreas")
	public Object updateAreas(Integer[] startCityId,Integer[] transCityId,Integer[] leaveCityId,Integer[] interCityId ) {
		String msg = "";
		try{
			sysGeoCityService.updateAreas(startCityId,transCityId,leaveCityId,interCityId);
		}catch(Exception e){
			e.printStackTrace();
			msg=e.getMessage();
		}
		return msg;
	}
	//目的地信息维护
	@RequestMapping(value = "destinationList")
	public String destinationList( HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Map<String,Object>> list =sysGeoCityService.getDestinationList();
		List<Map<String,Object>> selectList =sysGeoCityService.getDestSelectList();
        model.addAttribute("list", list);
		model.addAttribute("selectList", selectList);
        return "modules/city/destinationList";
	}
    //
	@ResponseBody
	@RequestMapping(value = "addDest")
	public Object addDest(HttpServletRequest request ) {
		String id =request.getParameter("id");
		String msg = "";
		try{
			sysGeoCityService.addDest(id);
		}catch(Exception e){
			e.printStackTrace();
			msg=e.getMessage();
		}
		return msg;
	}
	//
	@ResponseBody
	@RequestMapping(value = "delDest")
	public Object delDest(HttpServletRequest request ) {
		String id =request.getParameter("id");
		String msg = "";
		try{
			sysGeoCityService.delDest(id);
		}catch(Exception e){
			e.printStackTrace();
			msg=e.getMessage();
		}
		return msg;
	}
}
