package com.trekiz.admin.modules.activity.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Activityvisafile;
import com.trekiz.admin.modules.visa.service.VisaService;



@Controller
@RequestMapping(value="${adminPath}/activity/managerforCustomizedOrder")
public class TravelActivityControllerForCustomizedOrder extends BaseController {
	
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
    private AgentinfoService agentinfoService;	

	@Resource
	private DocInfoService docInfoService;
	@Resource
	private VisaService visaService;	
	@Autowired
	private AreaService areaService;
	
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 156;
	}
	
	@RequestMapping(value={"list/{showType}"})
	public String list(@PathVariable String showType,@ModelAttribute TravelActivity travelActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("bookOrder", model);
        
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");//起始同行价格
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");//结束同行价格
		String wholeSalerKey = request.getParameter("wholeSalerKey");//产品名称
		String remainDays = request.getParameter("remainDays");//保留天数
		
        String orderShowType = request.getParameter("orderShowType");
        if(StringUtils.isBlank(orderShowType)){
        	orderShowType="1";
		}
        if(StringUtils.isNotBlank(showType)){
        	orderShowType=showType;
		}
		if(StringUtils.isNotBlank(remainDays))
			travelActivity.setRemainDays(Integer.parseInt(remainDays));
		else
			travelActivity.setRemainDays(null);
        travelActivity.setAcitivityName(wholeSalerKey);
		String agentId = request.getParameter("agentId");//获取渠道id
		travelActivity.setActivityStatus(2);//只查询上架产品
		Page<TravelActivity> page = travelActivityService.findTravelActivity(new Page<TravelActivity>(request, response), travelActivity, 
				settlementAdultPriceStart, settlementAdultPriceEnd, common);

		model.addAttribute("agentId", agentId);
		model.addAttribute("travelActivity", travelActivity);
        model.addAttribute("page", page);
        model.addAttribute("remainDays", remainDays);
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",companyId));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",companyId));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",companyId));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.findUserDict(companyId, "flight"));
		model.addAttribute("fromAreas", DictUtils.findUserDict(companyId ,"fromarea"));
		model.addAttribute("payTypes", DictUtils.getSysDicMap(Context.PAY_TYPE));
		model.addAttribute("activityDuration", travelActivity.getActivityDuration());
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		
        model.addAttribute("showType",orderShowType);

		//出发城市
		return "modules/activity/reservation";
	}
	
	
	/**
	 * 渠道商登陆只显示所属批发商录过的目标区域
	 * 批发商登陆只显示自己录过的目标区域   
	 * 创建人：liangjingming   
	 * 创建时间：2014-2-10 下午3:11:19      
	 *
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "filterTreeData")
	public List<Map<String, Object>> filterTreeData(@RequestParam(required=false) Long extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList(); 
		//批发商ID
		Long companyId = UserUtils.getUser().getCompany().getId();
		targetAreaIds = travelActivityService.findAreaIds(companyId);
		if(targetAreaIds!=null && targetAreaIds.size()!=0){
			for(Map<String, Object> map:targetAreaIds){			
				childAreaIds.add(Long.parseLong(String.valueOf(map.get("targetAreaId"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds,areaIds,targetAreas);
		//目的地
		Map<String, Object> map = null;
		for (int i=0; i<targetAreas.size(); i++){
			Area e = targetAreas.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin()&&e.getId().equals(user.getArea().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	@RequestMapping(value="loadcountry",method=RequestMethod.GET)
	@ResponseBody
	public Object loadcountry(){
		
		Map<Long, Country> cMap = CountryUtils.getCountryList();
		int count = cMap.size();
		Iterator<Entry<Long, Country>> ite = cMap.entrySet().iterator();
		JSONObject datas = new JSONObject();
		JSONArray cs = new JSONArray();
		while(ite.hasNext()){
			JSONObject obj = new JSONObject();
			Entry<Long, Country> entry = ite.next();
			obj.put("id",entry.getKey());
			obj.put("name", ((Country)entry.getValue()).getCountryName_cn());
			cs.add(obj);
		}
		datas.put("results", cs);
		datas.put("total", count);
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value="loadvisas/{proId}")
	public Object findProVisas(@PathVariable String proId){
		
		Map<String, List<?>> results = new HashMap<String, List<?>>();
		List<DocInfo> docs = new ArrayList<DocInfo>();
		List<Long> ids = new ArrayList<Long>();
		List<Activityvisafile> datas = visaService.findVisaFileByProid(Long.parseLong(proId));
		if(datas!=null && datas.size()!=0){
			for(Activityvisafile visa:datas){
				if(visa.getSrcDocId()!=null)
					ids.add(visa.getSrcDocId());
			}
		}
		if(ids.size()!=0)
			docs = docInfoService.getDocInfoByIds(ids);
		results.put("visas", datas);
		results.put("docs", docs);
		return results;
	}


	
}
