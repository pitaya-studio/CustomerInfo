package com.trekiz.admin.modules.hotel.web;

import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.HotelTopic;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.HotelTopicService;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;


/**
 * 酒店主题模块
 * @author majiancheng
 * @Time 2015-4-13
 */
@Controller
@RequestMapping(value = "${adminPath}/hotelTopic")
public class HotelTopicController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hoteltopic/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelTopic/list";
	protected static final String SHOW_PAGE = "modules/hotel/hoteltopic/show";
	
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private HotelTopicService hotelTopicService;
	
	@ModelAttribute  
    public void populateModel( Model model,HttpServletRequest request) {
		model.addAttribute("type", Context.BaseInfo.HOTEL_TOPIC);  
	}
	
	@RequestMapping(value = "list")
	public String list(SysCompanyDictView sysCompanyDictView, HttpServletRequest request, HttpServletResponse response, Model model) {
		String pageSize = request.getParameter("pageSize");
		if(StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		
		//获取超级管理员维护的所有的酒店主题信息
		sysCompanyDictView.setType(Context.BaseInfo.HOTEL_TOPIC);
		sysCompanyDictView.setDelFlag("0");
		sysCompanyDictView.setCompanyId(-1L);
		String topicName = request.getParameter("topicName");
		if(!StringUtils.isEmpty(topicName)) {
			topicName = topicName.trim();
			sysCompanyDictView.setLabel(topicName);
			model.addAttribute("topicName", topicName);
		}
		
		Page<SysCompanyDictView> page = sysCompanyDictViewService.find(new Page<SysCompanyDictView>(request, response, Integer.parseInt(pageSize)), sysCompanyDictView);
		
		List<SysCompanyDictView> sysCompanyDictViews = page.getList();
		for(SysCompanyDictView entity : sysCompanyDictViews) {
			HotelTopic hotelTopic = hotelTopicService.findByViewUuidAndCompany(entity.getUuid(), getCompanyId().intValue());
			entity.setHotelTopic(hotelTopic);
		}
		
		//添加排序
		Collections.sort(sysCompanyDictViews, new Comparator<SysCompanyDictView>() {
            public int compare(SysCompanyDictView arg0, SysCompanyDictView arg1) {
            	if(arg0.getHotelTopic() == null) {
            		return -1;
            	} else if(arg1.getHotelTopic() == null) {
            		return 1;
            	}
            	
                return arg0.getHotelTopic().getSort().compareTo(arg1.getHotelTopic().getSort());
     
            }
        });
		
		model.addAttribute("page", page);
        model.addAttribute("companyId", getCompanyId());
        return LIST_PAGE;
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
				
				//更新酒店主题排序
				Set<String> uuids = uuidAndSortMap.keySet();
				if(uuids != null && !uuids.isEmpty()) {
					for(String uuid : uuids) {
						HotelTopic hotelTopic = hotelTopicService.getByUuid(uuid);
						hotelTopic.setSort(Integer.parseInt(uuidAndSortMap.get(uuid)));
						hotelTopicService.update(hotelTopic);
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
	@RequestMapping(value = "updateHotelTopic")
	public Object updateHotelTopic(String checkedUuids, String cancelUuids) {
		Map<String,Object> datas = new HashMap<String, Object>();
		String[] checkedUuidArr = checkedUuids.split(",");
		String[] cancelUuidArr = cancelUuids.split(",");
		
		if((checkedUuidArr != null) && (checkedUuidArr.length != 0)) {
			for(String checkedUuid : checkedUuidArr) {
				if(StringUtils.isEmpty(checkedUuid)) {
					continue;
				}
				
				HotelTopic hotelTopic = new HotelTopic();
				hotelTopic.setSort(50);
				hotelTopic.setViewUuid(checkedUuid);
				hotelTopic.setWholesalerId(getCompanyId().intValue());
				hotelTopicService.save(hotelTopic);
			}
		}
		
		if((cancelUuidArr != null) && (cancelUuidArr.length != 0)) {
			for(String calcelUuid : cancelUuidArr) {
				if(StringUtils.isEmpty(calcelUuid)) {
					continue;
				}
				
				HotelTopic hotelTopic = hotelTopicService.findByViewUuidAndCompany(calcelUuid, getCompanyId().intValue());
				if(hotelTopic != null) {
					hotelTopic.setDelFlag("1");
					hotelTopicService.update(hotelTopic);
				}
			}
		}
		
		datas.put("result","1");
		return datas;
	}

}
