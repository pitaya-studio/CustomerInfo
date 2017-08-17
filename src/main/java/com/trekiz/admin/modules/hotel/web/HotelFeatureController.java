/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelFeature;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.hotel.service.HotelFeatureService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelFeature")
public class HotelFeatureController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelfeature/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelFeature/list";
	protected static final String FORM_PAGE = "modules/hotel/hotelfeature/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelfeature/show";
	
	@Autowired
	private HotelFeatureService hotelFeatureService;
	@Autowired
	private HotelAnnexService hotelAnnexService;
	private List<HotelAnnex> annexList;
	
	public void setAnnexList(List<HotelAnnex> annexList) {
		this.annexList = annexList;
	}
	
	@ModelAttribute  
    public void populateModel( Model model,HttpServletRequest request) {
		model.addAttribute("type", Context.BaseInfo.HOTEL_FEATURE);  
		
		if(ArrayUtils.isNotEmpty(request.getParameterValues("hotelFeaturesAnnexDocId"))){
			String[] hotelAnnexDocId = request.getParameterValues("hotelFeaturesAnnexDocId");
			String[] docOriName = request.getParameterValues("docOriName");
			String[] docPath = request.getParameterValues("docPath");
			
			List<HotelAnnex> list = new ArrayList<HotelAnnex>();
			for(int i=0;i<hotelAnnexDocId.length;i++){
				if(StringUtils.isNotBlank(hotelAnnexDocId[i])){
					HotelAnnex ha = new HotelAnnex();
					ha.setDocId(Integer.parseInt(hotelAnnexDocId[i]));
					ha.setDocPath(docPath[i]);
					ha.setDocName(docOriName[i]);
					list.add(ha);
				}
			}
			this.setAnnexList(list);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(HotelFeature hotelFeature, HttpServletRequest request, HttpServletResponse response, Model model) {
		hotelFeature.setDelFlag("0");
        Page<HotelFeature> page = hotelFeatureService.find(new Page<HotelFeature>(request, response), hotelFeature); 
        model.addAttribute("page", page);
        
        
        hotelFeature.setDelFlag("0");
        hotelFeature.setWholesalerId(getCompanyId());
		
        List<HotelFeature> list = hotelFeatureService.find(hotelFeature); 
        model.addAttribute("list", list);
        model.addAttribute("wholesalerId", getCompanyId());  
        
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(HotelFeature hotelFeature, Model model) {
		model.addAttribute("hotelFeature", hotelFeature);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(HotelFeature hotelFeature, Model model, RedirectAttributes redirectAttributes) {
		
		if(getCompanyId() == 0) {
			return "3";
		}
		
		if(getCompanyId()==-1) {
			hotelFeature.setWholesalerId(-1l);
		} else {
			hotelFeature.setWholesalerId(getCompanyId());
		}
		
		hotelFeatureService.save(hotelFeature,this.annexList);
		return "1";
	}
	
	@ResponseBody
	@RequestMapping(value = "saveAjax")
	public Object saveAjax(HotelFeature hotelFeature) {
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			//只有批发商用户才可以调用此处方法，屏蔽系统用户的增加权限 
			if(getCompanyId() == 0) {
				b=false;
				datas.put("message", "没有操作权限！");
			}
//			else if(getCompanyId()==-1) {
//				b=false;
//				datas.put("message", "操作权限异常！");
//			} 
			else {
				
				
				if(getCompanyId()==-1) {
					hotelFeature.setWholesalerId(-1l);
				} else {
					hotelFeature.setWholesalerId(getCompanyId());
				}

				
				hotelFeatureService.save(hotelFeature,this.annexList);
				datas.put("uuid", hotelFeature.getUuid());
			}
			
			
		} catch (Exception e) {
			b = false;
			datas.put("message", "操作处理异常！");
		}
		if(b){
			datas.put("result", "1");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("hotelFeature", hotelFeatureService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		
		
		HotelFeature hotelFeature = hotelFeatureService.getByUuid(uuid);
		
		HotelAnnex ha = new HotelAnnex();
		ha.setDelFlag("0");
		ha.setMainUuid(hotelFeature.getUuid());
		ha.setType(2);
		List<HotelAnnex> haList = hotelAnnexService.find(ha);
		model.addAttribute("haList", haList);
		
		model.addAttribute("hotelFeature", hotelFeature);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(HotelFeature hotelFeature, Model model, RedirectAttributes redirectAttributes) {
		
		hotelFeatureService.update(hotelFeature,this.annexList);
		return "2";
	}
	
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid,String wholesalerId) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)&&StringUtils.isNotBlank(wholesalerId)&&wholesalerId.equals(getCompanyId()+"")){
				
				hotelFeatureService.removeByUuid(uuid);
				
			}else{
				b=false;
				datas.put("message", "传递参数异常或不具备操作该记录权限！");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "操作处理异常！");
		}
		if(b){
			datas.put("result", "1");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "updateOrder")
	public Object updateOrder(String idAndSorts,String wholesalerId) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			
			if(StringUtils.isNotBlank(idAndSorts)){
				String[] idAndSortsArray = idAndSorts.split(";");
				if(idAndSortsArray!=null && idAndSortsArray.length>0){
					for(String idAndSort : idAndSortsArray){
						if(StringUtils.isNotBlank(idAndSort)){
							String[] idAndSortArray = idAndSort.split(",");
							if(idAndSortArray!=null && idAndSortArray.length>0){
								
								if(getCompanyId()==Long.parseLong(idAndSortArray[2])){
									HotelFeature sd = hotelFeatureService.getById(Integer.parseInt(idAndSortArray[0]));
									sd.setSort(Integer.parseInt(idAndSortArray[1]));
									hotelFeatureService.update(sd,this.annexList);
								}else{
									if(datas.containsKey("sortException")){
										datas.put("sortException", datas.get("sortException")+idAndSortArray[1]+",");
									}else{
										datas.put("sortException", idAndSortArray[1]+",");
									}
									
								}
								
							}
						}
					}
				}
			}else{
				b=false;
				datas.put("message", "传递参数异常！");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "操作处理异常！");
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
