/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.region.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.region.entity.SysRegion;
import com.trekiz.admin.modules.region.service.SysGeoRegionRelService;
import com.trekiz.admin.modules.region.service.SysRegionService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/region")
public class SysRegionController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/region/regionList";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/regionList";
	protected static final String FORM_PAGE = "modules/region/form";
	protected static final String SHOW_PAGE = "modules/region/regionDetail";
	
	@Autowired
	private SysRegionService sysRegionService;
	@Autowired
	private  SysGeographyService  sysGeographyService;
	@Autowired
	private  SysGeoRegionRelService sysGeoRegionRelService;
	
	//显示国际或者国内列表
	@RequestMapping(value = "regionList")
	public String regionList(SysRegion sysRegion, HttpServletRequest request, HttpServletResponse response, Model model) {
		String lable = request.getParameter("lable");
		String isHome ="1";
		if (lable == null||lable=="") {
			lable = "guonei";
		}
		if ("guoji".equals(lable)){
			isHome="2";
		}
		sysRegion.setDelFlag("0");
		sysRegion.setIsHome(isHome);
        Page page = sysRegionService.findRegionList(request, response,sysRegion); 
        model.addAttribute("page", page);
        model.addAttribute("lable", lable);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(SysRegion sysRegion, Model model) {
		model.addAttribute("sysRegion", sysRegion);
		return FORM_PAGE;
	}

	@RequestMapping(value = "save")
	public String save(SysRegion sysRegion, Model model, RedirectAttributes redirectAttributes) {
		
		sysRegionService.save(sysRegion);
		return RE_LIST_PAGE;
	}
	
	@RequestMapping(value = "showRegion")
	public String showRegion( HttpServletRequest request, HttpServletResponse response, Model model) {
		String id=request.getParameter("id");
		String lable=request.getParameter("lable");
		if(StringUtils.isEmpty(id)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("sysRegion", sysRegionService.showRegionDetail(Integer.parseInt(id)).get(0));
		model.addAttribute("lable",lable);
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		if(StringUtils.isEmpty(id)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("sysRegion", sysRegionService.getById(Integer.parseInt(id)));
		return FORM_PAGE;
	}
	
	@RequestMapping(value = "update")
	public String update(SysRegion sysRegion, Model model, RedirectAttributes redirectAttributes) {
		
		sysRegionService.update(sysRegion);
		return RE_LIST_PAGE;
	}
	@RequestMapping(value = "delete")
	@ResponseBody
	public Object delete(HttpServletRequest request) {
		String ids = request.getParameter("id");
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(ids)){
				String[] idsArray = ids.split(",");
				if(idsArray!=null && idsArray.length>0){
					for(String id : idsArray){
						if(StringUtils.isNotBlank(id)){
							sysRegionService.removeById(Integer.parseInt(id));
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
	//添加一个国内或者国际地理区域，通过lable区别，跳转用
	@RequestMapping(value = "addRegion")
	public String addRegion(HttpServletRequest request, Model model) {
		String lable=request.getParameter("lable");
		//String id=request.getParameter("id");
		model.addAttribute("lable", lable);
		List<Object[]> list = sysGeographyService.getGeographyList(lable,"");//todo
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< list.size();i++){   
			 sb.append("{ id:"+list.get(i)[0]+", pId:"+list.get(i)[2]+", name:"+"'"+list.get(i)[1]+"'"+",isParent:"+(list.get(i)[3]!=null?true:false)+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		model.addAttribute("treeData",treeData);
		model.addAttribute("kind", "save");
		return "modules/region/addregion";
	}
	//执行新增或修改的操作
	@ResponseBody
	@RequestMapping(value="saveOrUpdateRegion")
	public String saveOrUpdateRegion(SysRegion sysRegion,HttpServletRequest request,RedirectAttributes redirectAttributes){
		String msg = null;
		String isHome = "1";
		String kind = request.getParameter("kind");// 保存或修改都会用到的参数
		String lable = request.getParameter("lable");// 保存或修改都会用到的参数
		String Ids = request.getParameter("outKey");//传过来的ID
		
		if("guoji".equals(lable)){
			isHome="2";
		}
		try{					
			if (("save".equals(kind))) {  
				sysRegion.setUuid(UUID.randomUUID().toString().replace("-", ""));
				//sysRegion.setStatus("0");
				//sysRegion.setName("0");
				//sysRegion.setDomain(menuName);
				//sysRegion.setDescription("0");
				sysRegion.setDelFlag("0");
				sysRegion.setIsHome(isHome);
				sysRegion.setCreateBy((long)UserUtils.getUser().getId().intValue());
				sysRegion.setCreateDate(new Date());
				sysRegionService.save(sysRegion);
				sysRegionService.addSysRegionRel(sysRegion,Ids);
			} else {
				SysRegion bean = sysRegionService.getById(sysRegion.getId());
				bean.setUpdateBy((long)UserUtils.getUser().getId().intValue());
				bean.setUpdateDate(new Date());				
				bean.setName(sysRegion.getName());				
				//bean.setDomain(sysRegion.getDomain());				
				bean.setStatus(sysRegion.getStatus());				
				bean.setDescription(sysRegion.getDescription());				
				sysRegionService.update(bean);
				//先删除原有的对应关系
				sysGeoRegionRelService.delRel(sysRegion.getId());
				//创建新的关联关系
				sysRegionService.addSysRegionRel(bean,Ids);
			}
		}catch(ServiceException e){
			msg = e.getMessage();
		}catch(Exception e){
			e.printStackTrace();
			msg = "保存失败";
		}
				
		return msg;
	}
	//修改国内或国际地理区域的跳转传值方法
	@RequestMapping(value = "modifyRegion")
	public String modifyRegion(Model model, HttpServletRequest request) {
		String id = request.getParameter("id");
		String lable = request.getParameter("lable");
		Map<String, String> conditon = new HashMap<String, String>();
		conditon.put("id", id);
		String outKey = sysGeographyService.getGeoIds(id);
		String parentUuids = "";
		if(!"".equals(outKey)&&outKey!=null){
		  parentUuids = sysGeographyService.getParentUuidsForRegion(outKey);
		}
		SysRegion sysRegion = sysRegionService.getById(Integer.parseInt(id));
		List<Object[]> list = sysGeographyService.getGeographyList(lable,parentUuids);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< list.size();i++){   
			 sb.append("{ id:"+list.get(i)[0]+", pId:"+list.get(i)[2]+", name:"+"'"+list.get(i)[1]+"'"+", isParent:"+(list.get(i)[3]!=null?true:false)+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		model.addAttribute("treeData",treeData);
		model.addAttribute("sysRegion", sysRegion);
		model.addAttribute("id", id);
		model.addAttribute("kind", "update");
		model.addAttribute("lable", lable);
		model.addAttribute("outKey", outKey);
		return "modules/region/addregion";
	}
}
