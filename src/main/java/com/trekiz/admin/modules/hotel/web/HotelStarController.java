/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.io.IOException;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.HotelStar;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.HotelStarService;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.common.persistence.Page;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelStar")
public class HotelStarController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelstar/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelStar/list";
	protected static final String FORM_PAGE = "modules/hotel/hotelstar/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelstar/show";
	

	protected static final String RE_SYS_DICT_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/sysCompanyDictView/list";
	
	@Autowired
	private HotelStarService hotelStarService;
	
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	
	
	@RequestMapping(value = "list")
	public String list(HotelStar hotelStar, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设置显示页数大小
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		
		hotelStar.setDelFlag("0");
		hotelStar.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
		
		Page<HotelStar> page = new Page<HotelStar>(request, response);
		page.setPageSize(Integer.parseInt(pageSize));
		
        Page<HotelStar> hotelStars = hotelStarService.find(page, hotelStar);
        
        for(HotelStar entity : hotelStars.getList()) {
        	SysCompanyDictView sysCompanyDictView = sysCompanyDictViewService.findByUuid(entity.getDictUuid());
        	entity.setSysCompanyDictView(sysCompanyDictView);
        }
        
        page.setList(hotelStars.getList());
        model.addAttribute("page", page);
        model.addAttribute("wholesalerId", UserUtils.getUser().getCompany().getId());
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(HotelStar hotelStar, Model model) {
		model.addAttribute("hotelStar", hotelStar);

		//加载超级管理员维护的酒店星级字典信息
		initSysHotelStar(model);
		
		return FORM_PAGE;
	}

	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(HotelStar hotelStar, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> datas = new HashMap<String, Object>();
		
		if(hotelStar != null && UserUtils.getCompanyIdForData() != null) {
			hotelStar.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
			
			//设置酒店星级值
			if(StringUtils.isNotEmpty(hotelStar.getDictUuid())) {
				SysCompanyDictView view = sysCompanyDictViewService.getByUuId(hotelStar.getDictUuid());
				if(view != null) {
					hotelStar.setValue(Integer.parseInt(view.getValue()));
				}
			}
			
			hotelStarService.save(hotelStar);
			datas.put("message","1");
		} else {
			datas.put("message","3");
			datas.put("error", "系统异常，请重新操作!");
		}
		
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "saveAjax")
	public Object saveAjax(HotelStar hotelStar) {
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
				
				
				hotelStar.setWholesalerId(Integer.parseInt(getCompanyId()+""));
				
				User user = UserUtils.getUser();
				if(user != null) {
					hotelStar.setCreateBy(Integer.parseInt(user.getId()+""));
					hotelStar.setUpdateBy(Integer.parseInt(user.getId()+""));
				}
				hotelStar.setCreateDate(new Date());
				hotelStar.setUpdateDate(new Date());
				
				if(hotelStar.getSort() == null) {
					hotelStar.setSort(50);
				}
				hotelStar.setDelFlag("0");
				hotelStar.setUuid(UuidUtils.generUuid());
				
				//设置酒店星级值 add by majiancheng at 2015-8-13
				if(StringUtils.isNotEmpty(hotelStar.getDictUuid())) {
					SysCompanyDictView view = sysCompanyDictViewService.getByUuId(hotelStar.getDictUuid());
					if(view != null) {
						hotelStar.setValue(Integer.parseInt(view.getValue()));
					}
				}
				
				hotelStarService.save(hotelStar);
				datas.put("uuid", hotelStar.getUuid());
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
			return RE_SYS_DICT_LIST_PAGE;
		}
		HotelStar hotelStar = hotelStarService.getByUuid(uuid);
		//add by WangXK 20151020添加非空指针判断
		if(hotelStar.getWholesalerId() == null || (UserUtils.getCompanyIdForData()!=null && hotelStar.getWholesalerId() != UserUtils.getCompanyIdForData().intValue())) {
			return SHOW_PAGE;
		}
		
		if(hotelStar != null) {
			SysCompanyDictView sysCompanyDictView = sysCompanyDictViewService.findByUuid(hotelStar.getDictUuid());
			hotelStar.setSysCompanyDictView(sysCompanyDictView);
		}
		
		model.addAttribute("hotelStar", hotelStar);
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_SYS_DICT_LIST_PAGE;
		}
		//加载超级管理员维护的酒店星级字典信息
		initSysHotelStar(model);
		
		HotelStar hotelStar = hotelStarService.getByUuid(uuid);
		
		if(hotelStar.getWholesalerId() == null || hotelStar.getWholesalerId() != UserUtils.getCompanyIdForData().intValue()) {
			return FORM_PAGE;
		}
		
		model.addAttribute("hotelStar", hotelStar);
		return FORM_PAGE;
	}
	
	@ResponseBody
	@RequestMapping(value = "update")
	public Object update(HotelStar hotelStar, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> datas = new HashMap<String, Object>();
		
		HotelStar entity = null;
		if(hotelStar.getUuid() != null) {
			entity = hotelStarService.getByUuid(hotelStar.getUuid());
		} else {
			//加载超级管理员维护的酒店星级字典信息
			initSysHotelStar(model);
			
			datas.put("message", "3");
			datas.put("error", "系统异常，请重新操作!");
			return datas;
		}
		
		entity.setLabel(hotelStar.getLabel());
		entity.setDescription(hotelStar.getDescription());
		entity.setSort(hotelStar.getSort());
		entity.setDictUuid(hotelStar.getDictUuid());
		
		//设置酒店星级值
		if(StringUtils.isNotEmpty(entity.getDictUuid())) {
			SysCompanyDictView view = sysCompanyDictViewService.getByUuId(entity.getDictUuid());
			if(view != null) {
				entity.setValue(Integer.parseInt(view.getValue()));
			}
		}
		
		hotelStarService.update(entity);
		datas.put("message", "2");
		return datas;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid) {
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)){
				hotelStarService.removeByUuid(uuid);
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
				String[] records = uuidAndSortsStr.split(";");
				
				//组装数据
				if(records != null && records.length != 0) {
					for(String record : records) {
						String[] uuidAndSorts = record.split(",");
						uuidAndSortMap.put(uuidAndSorts[0], uuidAndSorts[1]);
					}
				}
				
				//更新酒店星级排序
				Set<String> uuids = uuidAndSortMap.keySet();
				if(uuids != null && !uuids.isEmpty()) {
					for(String uuid : uuids) {
						HotelStar hotelStar = hotelStarService.getByUuid(uuid);
						hotelStar.setSort(Integer.parseInt(uuidAndSortMap.get(uuid)));
						hotelStarService.update(hotelStar);
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
		String label = request.getParameter("label");
		if(StringUtils.isEmpty(uuid))
		{
			uuid = "0";
		}
		
		User user = UserUtils.getUser();
		if(user == null || user.getCompany() == null) {
			return "false";
		}
		
		if(hotelStarService.findIsExist(uuid, label, user.getCompany().getId())) {
			return "false";
		}
		return "true";
	}
	
	/**
		* 加载超级管理员维护的酒店星级字典信息
		* 
		* @param model
		* @return void
		* @author majiancheng
		* @Time 2015-4-9
	 */
	public void initSysHotelStar(Model model) {
		List<SysCompanyDictView> hotelStarDicts = sysCompanyDictViewService.findByType("hotel_star", -1l);
		model.addAttribute("hotelStarDicts", hotelStarDicts);
	}
}
