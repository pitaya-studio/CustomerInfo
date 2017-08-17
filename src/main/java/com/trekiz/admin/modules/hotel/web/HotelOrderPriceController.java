/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.HotelOrderPrice;
import com.trekiz.admin.modules.hotel.input.HotelOrderPriceInput;
import com.trekiz.admin.modules.hotel.query.HotelOrderPriceQuery;
import com.trekiz.admin.modules.hotel.service.HotelOrderPriceService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelOrderPrice")
public class HotelOrderPriceController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelorderprice/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelOrderPrice/list";
	protected static final String FORM_PAGE = "modules/hotel/hotelorderprice/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelorderprice/show";
	
	@Autowired
	private HotelOrderPriceService hotelOrderPriceService;
	
	private HotelOrderPrice dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=hotelOrderPriceService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(HotelOrderPriceQuery hotelOrderPriceQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		hotelOrderPriceQuery.setDelFlag("0");
        Page<HotelOrderPrice> page = hotelOrderPriceService.find(new Page<HotelOrderPrice>(request, response), hotelOrderPriceQuery); 
        model.addAttribute("page", page);
        model.addAttribute("hotelOrderPriceQuery", hotelOrderPriceQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(HotelOrderPriceInput hotelOrderPriceInput, Model model) {
		model.addAttribute("hotelOrderPriceInput", hotelOrderPriceInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(HotelOrderPriceInput hotelOrderPriceInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			hotelOrderPriceService.save(hotelOrderPriceInput);
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
		model.addAttribute("hotelOrderPrice", hotelOrderPriceService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		HotelOrderPrice hotelOrderPrice = hotelOrderPriceService.getByUuid(uuid);
		HotelOrderPriceInput hotelOrderPriceInput = new HotelOrderPriceInput(hotelOrderPrice);
		model.addAttribute("hotelOrderPriceInput", hotelOrderPriceInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(HotelOrderPriceInput hotelOrderPriceInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, hotelOrderPriceInput,true);
			hotelOrderPriceService.update(dataObj);
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
					hotelOrderPriceService.removeByUuid(uuid);
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
}
