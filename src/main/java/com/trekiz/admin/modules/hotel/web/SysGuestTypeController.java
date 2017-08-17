/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.SysGuestTravelerTypeRel;
import com.trekiz.admin.modules.hotel.entity.SysGuestType;
import com.trekiz.admin.modules.hotel.entity.SysTravelerType;
import com.trekiz.admin.modules.hotel.input.SysGuestTypeInput;
import com.trekiz.admin.modules.hotel.query.SysGuestTravelerTypeRelQuery;
import com.trekiz.admin.modules.hotel.query.SysGuestTypeQuery;
import com.trekiz.admin.modules.hotel.query.SysTravelerTypeQuery;
import com.trekiz.admin.modules.hotel.service.SysGuestTravelerTypeRelService;
import com.trekiz.admin.modules.hotel.service.SysGuestTypeService;
import com.trekiz.admin.modules.hotel.service.SysTravelerTypeService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/sysGuestType")
public class SysGuestTypeController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/sysguesttype/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/sysGuestType/list";
	protected static final String FORM_PAGE = "modules/hotel/sysguesttype/form";
	protected static final String SHOW_PAGE = "modules/hotel/sysguesttype/show";
	
	@Autowired
	private SysGuestTypeService sysGuestTypeService;
	@Autowired
	private SysTravelerTypeService sysTravelerTypeService;
	@Autowired
	private SysGuestTravelerTypeRelService sysGuestTravelerTypeRelService;
	private SysGuestType dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=sysGuestTypeService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(SysGuestTypeQuery sysGuestTypeQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		sysGuestTypeQuery.setDelFlag("0");
		//系统维护的住客类型，不需要增加批发商的默认查询条件---update by zhanghao 20150824
		//sysGuestTypeQuery.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
        Page<SysGuestType> page = sysGuestTypeService.find(new Page<SysGuestType>(request, response), sysGuestTypeQuery); 
        List<SysGuestType> sysGuestlist = page.getList();
        if(sysGuestlist.size()>0){
        	List<SysGuestTravelerTypeRel> syslist = new ArrayList<SysGuestTravelerTypeRel>();
        	SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery = new SysGuestTravelerTypeRelQuery();
        	for(SysGuestType sysGuestType:sysGuestlist){
        		sysGuestTravelerTypeRelQuery = new SysGuestTravelerTypeRelQuery();
        		sysGuestTravelerTypeRelQuery.setSysGuestTypeUuid(sysGuestType.getUuid());
        		sysGuestTravelerTypeRelQuery.setDelFlag(Context.DEL_FLAG_NORMAL);
        		syslist = sysGuestTravelerTypeRelService.find(sysGuestTravelerTypeRelQuery);
        		sysGuestType.setTravelerTypeRelList(syslist);
            }
        }
        //page.setList(sysGuestlist);
        model.addAttribute("page", page);
        model.addAttribute("sysGuestTypeQuery", sysGuestTypeQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(SysGuestTypeInput sysGuestTypeInput, Model model) {
		model.addAttribute("sysGuestTypeInput", sysGuestTypeInput);
		model.addAttribute("sysTravelerTypeList", getSysTravelerTypeList());
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(SysGuestTypeInput sysGuestTypeInput, Model model, HttpServletRequest request) {
		String result="";
		String sysTravelerType = request.getParameter("sysTravelerType");//测试使用
		String[] sysTravelerTypeUuids = request.getParameter("sysTravelerTypeUuids").trim().split(",");
		try {
			sysGuestTypeInput.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
			SysGuestType sysGuestType = sysGuestTypeInput.getSysGuestType();
			sysGuestTypeService.save(sysGuestType);
    		
			//保存 travelerType表的UUid 保存到hotelTravelerTypeRelationService
			for(int i=0;i<sysTravelerTypeUuids.length;i++){
				String s = sysTravelerTypeUuids[i];
				if(s!=null&&!"".equals(s)){
					SysGuestTravelerTypeRel typeRelation = new SysGuestTravelerTypeRel();
					typeRelation.setSysTravelerTypeUuid(sysTravelerTypeUuids[i]);
					typeRelation.setSysGuestTypeUuid(sysGuestType.getUuid());
					sysGuestTravelerTypeRelService.save(typeRelation);
				}
			}
			
			result="1";
		} catch (Exception e) {
			result="0";
		}
		return result;
		
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		
		SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery = new SysGuestTravelerTypeRelQuery();
		sysGuestTravelerTypeRelQuery.setSysGuestTypeUuid(uuid);
		sysGuestTravelerTypeRelQuery.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<SysGuestTravelerTypeRel> list = sysGuestTravelerTypeRelService.find(sysGuestTravelerTypeRelQuery);
		model.addAttribute("list", list);
		model.addAttribute("sysGuestType", sysGuestTypeService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		SysGuestType sysGuestType = sysGuestTypeService.getByUuid(uuid);
		SysGuestTypeInput sysGuestTypeInput = new SysGuestTypeInput(sysGuestType);
		
		//add by wangXK 20180826 
		
		SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery = new SysGuestTravelerTypeRelQuery();
		sysGuestTravelerTypeRelQuery.setSysGuestTypeUuid(uuid);
		sysGuestTravelerTypeRelQuery.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<SysGuestTravelerTypeRel> list = sysGuestTravelerTypeRelService.find(sysGuestTravelerTypeRelQuery);
		String sysTravelerTypeString = "";
		if(list != null && list.size()>0){
			for(SysGuestTravelerTypeRel sysGuestTravelerTypeRel: list){
				sysTravelerTypeString = sysTravelerTypeString + sysGuestTravelerTypeRel.getSysTravelerTypeUuid()+ ",";
			}
		}
		
		model.addAttribute("sysTravelerTypeString", sysTravelerTypeString);
		model.addAttribute("sysTravelerTypeList", getSysTravelerTypeList());
		model.addAttribute("sysGuestTypeInput", sysGuestTypeInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(SysGuestTypeInput sysGuestTypeInput, Model model, HttpServletRequest request) {
		String result="";
		String[] sysTravelerTypeUuids = request.getParameter("sysTravelerTypeUuids").trim().split(",");
		try {
			BeanUtil.copySimpleProperties(dataObj, sysGuestTypeInput,true);
			sysGuestTypeService.update(dataObj);
			//删除掉之前的系统游客类型，重新保存
			if(sysGuestTypeInput.getUuid()!=null && !"".equals(sysGuestTypeInput.getUuid().trim())){
				sysGuestTravelerTypeRelService.removeBySysGuestTypeUuid(sysGuestTypeInput.getUuid());
			}
			sysGuestTypeInput.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
    		
			//保存 travelerType表的UUid 保存到hotelTravelerTypeRelationService
			for(int i=0;i<sysTravelerTypeUuids.length;i++){
				String s = sysTravelerTypeUuids[i];
				if(s!=null&&!"".equals(s)){
					SysGuestTravelerTypeRel typeRelation = new SysGuestTravelerTypeRel();
					typeRelation.setSysTravelerTypeUuid(sysTravelerTypeUuids[i]);
					typeRelation.setSysGuestTypeUuid(sysGuestTypeInput.getUuid());
					sysGuestTravelerTypeRelService.save(typeRelation);
				}
			}
			result="2";
		} catch (Exception e) {
			result="0";
		}
		return result;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				for(String uuid:uuidArray){
					sysGuestTypeService.removeByUuid(uuid);
				}
				
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
	@RequestMapping(value = "check")
	public Object check(HttpServletRequest request) throws IOException {
		String flag = "false";
		Map<String, Object> data=new HashMap<String,Object>();
		String name = request.getParameter("name");
		SysGuestTypeQuery sysGuestTypeQuery = new SysGuestTypeQuery();
		sysGuestTypeQuery.setName(name.trim());
		
		List<SysGuestType> list = sysGuestTypeService.find(sysGuestTypeQuery);
		if(list!=null && list.size() >0){
			flag = "true";
			data.put("result", list.get(0).getName());
		}
		data.put("message", flag);
		return data;
	}
	
	public List<SysTravelerType> getSysTravelerTypeList(){
		SysTravelerTypeQuery sysTravelerTypeQuery = new SysTravelerTypeQuery();
		sysTravelerTypeQuery.setDelFlag(Context.DEL_FLAG_NORMAL);
		return sysTravelerTypeService.find(sysTravelerTypeQuery);
	}
	public List<SysGuestTravelerTypeRel> getSysGuestTravelerTypeRelList(){
		SysGuestTravelerTypeRelQuery query = new SysGuestTravelerTypeRelQuery();
		query.setDelFlag(Context.DEL_FLAG_NORMAL);
		return sysGuestTravelerTypeRelService.find(query);
	}
	@ResponseBody
	@RequestMapping(value = "checkSysTravelerType")
	public Object checkSysTravelerType(HttpServletRequest request) throws IOException {
		String flag = "false";
		String sysTravelerTypeUuids = request.getParameter("sysTravelerTypeUuids");
		List<SysGuestTravelerTypeRel> list = getSysGuestTravelerTypeRelList();
		Map<String, Object> data=new HashMap<String,Object>();
		String result = "";
		if(list!=null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				if(sysTravelerTypeUuids!=null && !"".equals(sysTravelerTypeUuids)){
					if(sysTravelerTypeUuids.contains(list.get(i).getSysTravelerTypeUuid())){
						SysTravelerType s = sysTravelerTypeService.getByUuid(list.get(i).getSysTravelerTypeUuid());
						result += s.getName() + ",";
						flag = "true";
					}
				}
			}
			
			result = result.substring(0,result.length()-1);
		}
		data.put("message", flag);
		data.put("result", result);
		return data;
	}
	@ResponseBody
	@RequestMapping(value = "checkRepetition")
	public Object checkRepetition(HttpServletRequest request) throws IOException {
		String[] selectedTravelerTypeUuids = request.getParameter("sysTravelerTypeUuids").split(",");
		String sysGuestTypeUuid = request.getParameter("sysGuestTypeUuid")==null?"0":request.getParameter("sysGuestTypeUuid");
		String value = request.getParameter("value");
		String type = request.getParameter("type");
		List<String> existTravelerTypeUuids = sysTravelerTypeService.findExistTravelerUuids(sysGuestTypeUuid, value, type);
		Map<String,String> data=new HashMap<String,String>();
		String conflictNames = "";
		if(CollectionUtils.isNotEmpty(existTravelerTypeUuids)){
			for(int i=0;i<selectedTravelerTypeUuids.length;i++){
				if(existTravelerTypeUuids.contains(selectedTravelerTypeUuids[i])){
					SysTravelerType s = sysTravelerTypeService.getByUuid(selectedTravelerTypeUuids[i]);
					conflictNames += s.getName() + ",";
				}
			}
		}
		if(StringUtils.isNotEmpty(conflictNames)){
			data.put("result", "1");
			data.put("mess", conflictNames.substring(0,conflictNames.length()-1));
		}else{
			data.put("result", "0");
		}
		return data;
	}
}
