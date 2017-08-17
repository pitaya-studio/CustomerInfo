/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.geography.web;

import java.util.ArrayList;
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
import com.trekiz.admin.common.mapper.JsonMapper;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/geography")
public class SysGeographyController extends BaseController {

	// forward paths
	protected static final String LIST_PAGE = "modules/geography/geographyList";
	protected static final String RE_LIST_PAGE = "redirect:"
			+ Global.getAdminPath() + "/geography/list";
	protected static final String FORM_PAGE = "modules/geography/form";
	protected static final String SHOW_PAGE = "modules/geography/show";

	@Autowired
	private SysGeographyService sysGeographyService;

	@RequestMapping(value = "list")
	public String list(SysGeography sysGeography, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String lable = request.getParameter("lable");
		if (lable == null||lable=="") {
			lable = "guonei";
		}
		// Page<SysGeography> page = sysGeographyService.find(new
		// Page<SysGeography>(request, response), sysGeography);
		Map<String, String> conditon = new HashMap<String, String>();
		conditon.put("lable", lable);
		List<SysGeography> list = sysGeographyService.getAllByContion(conditon);
		model.addAttribute("list", list);
		model.addAttribute("lable", lable);
		// model.addAttribute("page", page);

		return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(SysGeography sysGeography, Model model) {
		model.addAttribute("sysGeography", sysGeography);
		return FORM_PAGE;
	}

	@RequestMapping(value = "save")
	public String save(SysGeography sysGeography, Model model,
			RedirectAttributes redirectAttributes) {

		sysGeographyService.save(sysGeography);
		return RE_LIST_PAGE;
	}

	@RequestMapping(value = "show/{id}")
	public String show(@PathVariable String id, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if (StringUtils.isEmpty(id)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("sysGeography",
				sysGeographyService.getById(Integer.parseInt(id)));
		return SHOW_PAGE;
	}

	@RequestMapping(value = "edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		if (StringUtils.isEmpty(id)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("sysGeography",
				sysGeographyService.getById(Integer.parseInt(id)));
		return FORM_PAGE;
	}

	@RequestMapping(value = "update")
	public String update(SysGeography sysGeography, Model model,
			RedirectAttributes redirectAttributes) {

		sysGeographyService.update(sysGeography);
		return RE_LIST_PAGE;
	}

	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String ids) {

		Map<String, Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if (StringUtils.isNotBlank(ids)) {
				String[] idsArray = ids.split(",");
				if (idsArray != null && idsArray.length > 0) {
					for (String id : idsArray) {
						if (StringUtils.isNotBlank(id)) {
							sysGeographyService
									.removeById(Integer.parseInt(id));
						}
					}
				}
			} else {
				b = false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "error");
		}
		if (b) {
			datas.put("result", "1");
			datas.put("message", "success");
		} else {
			datas.put("result", "0");
		}
		return datas;
	}

	@RequestMapping(value = "search")
	public String search(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String searchCondtion = request.getParameter("wholeSalerKey");
		String lable = request.getParameter("lable");
		if(StringUtils.isBlank(searchCondtion)){
			return "redirect:"+Global.getAdminPath() +"/geography/list?lable="+lable;
		}
		Map<String, String> conditon = new HashMap<String, String>();
		conditon.put("search", searchCondtion);
		conditon.put("lable", lable);
		List<SysGeography> list = sysGeographyService.getSearchList(conditon);
		model.addAttribute("lable", lable);
		model.addAttribute("list", list);
		model.addAttribute("ifSearch", "true");
		model.addAttribute("wholeSalerKey", searchCondtion);
		return LIST_PAGE;
	}

	@RequestMapping(value = "searchChild")
	@ResponseBody
	public List<Map<String, Object>> searchChild(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		Map<String, String> conditon = new HashMap<String, String>();
		conditon.put("id", id);
		List<Map<String, Object>> list = sysGeographyService
				.getChildList(conditon);
		return list;
	}

	// 跳转到添加子类区域界面，传递过去上级区域的名称信息
	@RequestMapping(value = "saveList")
	public String saveList(Model model, HttpServletRequest request) {
		String id = request.getParameter("parentId");
		String level = request.getParameter("level");//先去得界面传过来的值。
		level =String.valueOf(Integer.parseInt(level)+1);//添加子类，其level值加一
		String lable = request.getParameter("lable");
		Map<String, String> conditon = new HashMap<String, String>();
		conditon.put("id", id);
		conditon.put("lable", lable);
		String parentName = sysGeographyService.getParentName(conditon);
		List<Object[]> list = sysGeographyService.getGeographyList(lable,"");
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< list.size();i++){   
			 sb.append("{ id:"+list.get(i)[0]+", pId:"+list.get(i)[2]+", name:"+"'"+list.get(i)[1]+"'"+",isParent:"+(list.get(i)[3]!=null?true:false)+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		model.addAttribute("treeData",treeData);
		model.addAttribute("parentId", id);
		model.addAttribute("level", level);
		model.addAttribute("parentName", parentName);
		model.addAttribute("kind", "save");
		model.addAttribute("lable", lable);
		return "modules/geography/saveList";
	}
	@ResponseBody
	@RequestMapping(value="saveGeography")
	public String saveGeography(SysGeography sysGeography,HttpServletRequest request){
		String msg = null;
		int position = 1;
		String parentId = request.getParameter("parentId");// 修改现有信息时会用到
		String kind = request.getParameter("kind");// 保存或修改都会用到的参数
		String lable = request.getParameter("lable");// 保存或修改都会用到的参数
		String menuIds = request.getParameter("crossSection");
		if("guoji".equals(lable)){
			position=2;
		}
		try{
			if (("save".equals(kind))) {  
				SysGeography parent = sysGeographyService.getById(Integer.parseInt(parentId));
				sysGeography.setUuid(UUID.randomUUID().toString().replace("-", ""));
				sysGeography.setParentUuid(parent.getUuid());
				sysGeography.setCreateBy(UserUtils.getUser().getId().intValue());
				sysGeography.setCreateDate(new Date());
				sysGeography.setPosition(position);
				sysGeography.setCrossSection(menuIds);
				//添加父级uuids
				if(parent.getParentUuids()!=null){
				    sysGeography.setParentUuids(parent.getParentUuids()+","+parent.getUuid());
				}else{
					sysGeography.setParentUuids(parent.getUuid());	
				}
				
				sysGeographyService.save(sysGeography);
			} else {
				SysGeography bean = sysGeographyService.getById(sysGeography.getId());
				bean.setUpdateBy(UserUtils.getUser().getId().intValue());
				bean.setUpdateDate(new Date());
				bean.setCrossSection(sysGeography.getCrossSection());
				bean.setDescription(sysGeography.getDescription());
				bean.setNameCn(sysGeography.getNameCn());
				bean.setNameEn(sysGeography.getNameEn());
				bean.setNamePinyin(sysGeography.getNamePinyin());
				bean.setNameShortCn(sysGeography.getNameShortCn());
				bean.setNameShortEn(sysGeography.getNameShortEn());
				bean.setNameShortPinyin(sysGeography.getNameShortPinyin());
				bean.setSort(sysGeography.getSort());
				bean.setPosition(position);
				bean.setCrossSection(sysGeography.getCrossSection());
				sysGeographyService.update(bean);
			}
		}catch(ServiceException e){
			msg = e.getMessage();
		}catch(Exception e){
			e.printStackTrace();
			msg = "保存失败";
		}
		return msg;
	}
	

	// 删除一条区域信息 ()   
	@RequestMapping(value="deleteGeography")
	@ResponseBody
	public String deleteGeography(Long id) {
		String msg = null;
		try{
			Map<String, String> map = sysGeographyService.delGeography(id);
			msg = map.get("msg");
		}catch(Exception e){
			e.printStackTrace();
			msg= e.getMessage();
		}
		return msg;
	}

	// 取得待修改的区域信息
	@RequestMapping(value = "modifyGeography")
	public String modifyGeography(Model model, HttpServletRequest request) {
		String id = request.getParameter("id");
		String level = request.getParameter("level");
		String parentId = request.getParameter("parentId");
		String lable = request.getParameter("lable");
		Map<String, String> conditon = new HashMap<String, String>();
		conditon.put("id", id);
		SysGeography geography = sysGeographyService.getById(Integer.parseInt(id));
		String parentName = sysGeographyService.getParentName(conditon);
		String parentUuids = sysGeographyService.getParentUuids(id);
		List<Object[]> list = sysGeographyService.getGeographyList(lable,parentUuids);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< list.size();i++){   
			 sb.append("{ id:"+list.get(i)[0]+", pId:"+list.get(i)[2]+", name:"+"'"+list.get(i)[1]+"'"+",isParent:"+(list.get(i)[3]!=null?true:false)+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		model.addAttribute("treeData",treeData);
		model.addAttribute("geography", geography);
		model.addAttribute("id", id);
		model.addAttribute("level", level);
		model.addAttribute("parentId", parentId);
		model.addAttribute("parentName", parentName);
		model.addAttribute("kind", "update");
		model.addAttribute("lable", lable);
		return "modules/geography/saveList";
	}
	//跳转到增加顶级区域界面
	@RequestMapping(value = "addTopList")
	public String addTopList(HttpServletRequest request,Model model) {
		String lable = request.getParameter("lable");
		List<Object[]> list = sysGeographyService.getGeographyList(lable,"");
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< list.size();i++){   
			 sb.append("{ id:"+list.get(i)[0]+", pId:"+list.get(i)[2]+", name:"+"'"+list.get(i)[1]+"'"+",isParent:"+(list.get(i)[3]!=null?true:false)+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		model.addAttribute("treeData",treeData);
		model.addAttribute("lable",lable);
		return "modules/geography/saveTopList";
	}
	@ResponseBody
	@RequestMapping(value="saveTopList")
	public Object saveTopList(SysGeography sysGeography,HttpServletRequest request,HttpServletResponse respose) throws Exception{
		String lable = request.getParameter("lable");
		String menuIds = request.getParameter("crossSection");
		// Map<String, Object> mesg = new HashMap<String, Object>();
		String mesg = "";
		try{
			if("guoji".equals(lable)){
				sysGeography.setUuid(UUID.randomUUID().toString().replace("-", ""));
				//sysGeography.setParentUuid(UUID.randomUUID().toString().replace("-", ""));
				sysGeography.setParentUuid("0");
				sysGeography.setParentId(0);
				//sysGeography.setParentUuids("");国际的父级uuids为空
				sysGeography.setCreateBy(UserUtils.getUser().getId().intValue());
				sysGeography.setCreateDate(new Date());
				sysGeography.setLevel(0);
				sysGeography.setPosition(2);
				sysGeography.setCrossSection(menuIds);
				sysGeographyService.save(sysGeography);}
			else{
				sysGeography.setUuid(UUID.randomUUID().toString().replace("-", ""));
				//sysGeography.setParentUuid(UUID.randomUUID().toString().replace("-", ""));
				//默认为中国的id，uuid
				sysGeography.setParentUuid("c89e0a6661b64d1e809d8873cf85bc80");
				sysGeography.setParentId(2);
				sysGeography.setCreateBy(UserUtils.getUser().getId().intValue());
				sysGeography.setCreateDate(new Date());
				sysGeography.setLevel(2);
				sysGeography.setPosition(1);
				sysGeography.setCrossSection(menuIds);
				//父级uuids
				sysGeography.setParentUuids("457af2555f7341d4a1dd2bcfceffaf4f,c89e0a6661b64d1e809d8873cf85bc80");
				sysGeographyService.save(sysGeography);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			//mesg.put("result", "操作失败");
			mesg="操作失败";
		}
		return mesg;
	}
    @ResponseBody
	@RequestMapping(value="saveSort")
	public String saveSort(String[] hiddenSort,String[] hiddenId,String[] sort,SysGeography sysGeography){
		String[] oldSort=hiddenSort;
		String[] newSort=sort;
//		String[] id=hiddenId;
		String[] updateSort=new String [oldSort.length];//存放需要更新的sort值
		String[] updateId=new String [oldSort.length];//存放需要更新记录的id值
		int j=0;
		for(int i=0;i<newSort.length;i++){
			if(!oldSort[i].equals(newSort[i])){
				updateId[j]=hiddenId[i];
				updateSort[j]=newSort[i];
				j++;
			}
		}
	
		String msg = null;
		try{
			
			sysGeographyService.saveSort(updateId,updateSort,sysGeography);
			
		}catch(Exception e){
			e.printStackTrace();
			msg = "操作失败";
		}
		
		return msg;
	}
	@ResponseBody
	@RequestMapping(value = "getGeoListAjax")
	public String getGeoListAjax(String type,String parentId) {
		
		
		SysGeography sysGeography = new SysGeography();
		
        List<SysGeography> list =new ArrayList<SysGeography>();
        
        if(StringUtils.isNotBlank(type)&&type.equals("1")){//境内
			
			sysGeography.setDelFlag("0");
			sysGeography.setNameCn("中国");
			sysGeography.setLevel(1);
			list = sysGeographyService.find(sysGeography);
			
		}else if(StringUtils.isNotBlank(type)&&type.equals("2")){//境外
			sysGeography.setDelFlag("0");
			sysGeography.setLevel(1);
			list = removeChinaList(sysGeographyService.find(sysGeography));
		}else if(StringUtils.isNotBlank(parentId)){//查询子节点集合
			sysGeography.setDelFlag("0");
			sysGeography.setParentUuid(parentId);
			list = sysGeographyService.find(sysGeography);
		}
		
		return JsonMapper.getInstance().toJson(list);
	}
	
	
	/***
	 * 获取全部国家
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAllConListAjax")
	public String getGeoListAjax() {
		SysGeography sysGeography = new SysGeography();
        List<SysGeography> list =new ArrayList<SysGeography>();
			sysGeography.setDelFlag("0");
			sysGeography.setLevel(1);
			list = sysGeographyService.find(sysGeography);
		return JsonMapper.getInstance().toJson(list);
	}
	
	
	/**
     * 境外国家剔除 中国
     * @param list
     * @return
     */
    private List<SysGeography> removeChinaList(List<SysGeography> list){
    	List<SysGeography> nlist = new ArrayList<SysGeography>();
    	for(SysGeography sg:list){
    		if(StringUtils.isNotBlank(sg.getNameCn())&&sg.getNameCn().equals("中国")){
    			
    		}else{
    			nlist.add(sg);
    		}
    	}
    	return nlist;
    }
    //guonei 
    @ResponseBody
    @RequestMapping(value = "secondList")
	public String secondList(HttpServletRequest request,Model model) {
		int  id =  Integer.parseInt( request.getParameter("id"));
		String  lable = request.getParameter("lable");
		List<Object[]> list = new ArrayList<Object[]>();
		if("guoji".equals(lable)){
			 list = sysGeographyService.getSecondList1(id);
		}else{
			 list = sysGeographyService.getSecondList(id);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		 for(int i = 0 ; i< list.size();i++){   
			 sb.append("{ id:"+list.get(i)[0]+", pId:"+list.get(i)[2]+", name:"+"'"+list.get(i)[1]+"'"+",isParent:"+(list.get(i)[3]!=null?true:false)+"}," );
	        }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		//model.addAttribute("treeData",treeData);
		//model.addAttribute("lable",lable);
		return treeData;
	}
    //guoji
    @ResponseBody
    @RequestMapping(value = "secondList1")
	public String secondList1(HttpServletRequest request,Model model) {
		int  id =  Integer.parseInt( request.getParameter("id"));
//		String checked = request.getParameter("checked");
		
		List<Object[]> list = sysGeographyService.getSecondList1(id);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		//
		 for(int i = 0 ; i< list.size();i++){   
			 sb.append("{ id:"+list.get(i)[0]+", pId:"+list.get(i)[2]+", name:"+"'"+list.get(i)[1]+"'"+",isParent:"+(list.get(i)[3]!=null?true:false)+"}," );
	     }   
		 String treeData=sb.toString();
		 treeData=treeData.substring(0, treeData.lastIndexOf(","));
		 treeData=treeData+"]";
		return treeData;
	}
    
    @RequestMapping(value = "initGeographyData")
    public String convertNameToPinyin(HttpServletRequest request,Model model) {
    	List<SysGeography> list = sysGeographyService.find(new SysGeography());
    	List<SysGeography> cacheList = new ArrayList<SysGeography>();
    	if(list != null && !list.isEmpty()) {
    		for(SysGeography sysGeography : list) {
    			sysGeography.setNamePinyin(ChineseToEnglish.getPingYin(sysGeography.getNameCn()).toUpperCase());
    			sysGeography.setNameShortPinyin(ChineseToEnglish.getPinYinHeadChar(sysGeography.getNameCn()).toUpperCase());
    			
    			cacheList.add(sysGeography);
    			if(cacheList.size() == 5000) {
    				sysGeographyService.updateGeographys(cacheList);
    				cacheList.clear();
    			}
    		}
    	}
    	
    	sysGeographyService.updateGeographys(cacheList);
		cacheList.clear();
		
		return LIST_PAGE;
    }
}

