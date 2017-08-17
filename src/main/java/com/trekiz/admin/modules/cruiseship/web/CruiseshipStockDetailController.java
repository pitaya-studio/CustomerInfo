/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.web;

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
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockDetailInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockDetailQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockDetailService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/cruiseshipStockDetail")
public class CruiseshipStockDetailController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/cruiseship/cruiseshipstockdetail/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/cruiseshipStockDetail/list";
	protected static final String FORM_PAGE = "modules/cruiseship/cruiseshipstockdetail/form";
	protected static final String SHOW_PAGE = "modules/cruiseship/cruiseshipstockdetail/show";
	
	@Autowired
	private CruiseshipStockDetailService cruiseshipStockDetailService;
	
	private CruiseshipStockDetail dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=cruiseshipStockDetailService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(CruiseshipStockDetailQuery cruiseshipStockDetailQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		cruiseshipStockDetailQuery.setDelFlag("0");
        Page<CruiseshipStockDetail> page = cruiseshipStockDetailService.find(new Page<CruiseshipStockDetail>(request, response), cruiseshipStockDetailQuery); 
        model.addAttribute("page", page);
        model.addAttribute("cruiseshipStockDetailQuery", cruiseshipStockDetailQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(CruiseshipStockDetailInput cruiseshipStockDetailInput, Model model) {
		model.addAttribute("cruiseshipStockDetailInput", cruiseshipStockDetailInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(CruiseshipStockDetailInput cruiseshipStockDetailInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			cruiseshipStockDetailService.save(cruiseshipStockDetailInput);
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
		model.addAttribute("cruiseshipStockDetail", cruiseshipStockDetailService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailService.getByUuid(uuid);
		CruiseshipStockDetailInput cruiseshipStockDetailInput = new CruiseshipStockDetailInput(cruiseshipStockDetail);
		model.addAttribute("cruiseshipStockDetailInput", cruiseshipStockDetailInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(CruiseshipStockDetailInput cruiseshipStockDetailInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, cruiseshipStockDetailInput,true);
			cruiseshipStockDetailService.update(dataObj);
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
				b = cruiseshipStockDetailService.batchDelete(uuidArray);
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
	
	/**已废弃 已废弃 已废弃 已废弃 已废弃 已废弃
	 * 游轮库存、余位扣减接口 by chy 2016年2月3日10:28:04  
	 * uuid 库存明细UUID
	 * type 扣减类型 库存 1 余位 2
	 * operate 操作类型  1 增加 2 减少
	 * num 增加/减少的数量
	 */
//	@Autowired
//	@RequestMapping(value="stocknummanage/{uuid}/{type}/{operate}/{num}")
//	public String stockNumManage(@PathVariable String uuid, @PathVariable String type, @PathVariable String operate, @PathVariable String num){
//		try {
//			cruiseshipStockDetailService.stockNumManage(uuid, type, operate, num);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return "";
//	}
}
