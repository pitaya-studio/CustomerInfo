/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.geography.service.SysGeographyService;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;
import com.trekiz.admin.modules.island.entity.Island;
import com.trekiz.admin.modules.island.service.IslandService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/island")
public class IslandController extends BaseController {
	
	//forward paths
		protected static final String LIST_PAGE = "modules/island/island/list";
		protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/island/list";
		protected static final String FORM_PAGE = "modules/island/island/form";
		protected static final String SHOW_PAGE = "modules/island/island/show";

		@Autowired
		private IslandService islandService;
		
		@Autowired
		private SysGeographyService sysGeographyService;

		@Autowired
		private HotelAnnexService hotelAnnexService;
		
		
		@ModelAttribute  
	    public void populateModel( Model model,HttpServletRequest request) {
			model.addAttribute("type", Context.BaseInfo.ISLANDS_MANAGER);  
		}
		
		@RequestMapping(value = "list")
		public String list(Island island, HttpServletRequest request, HttpServletResponse response, Model model) {
			//设置显示页数大小
			String pageSize = request.getParameter("pageSize");
			if(StringUtils.isEmpty(pageSize)) {
				pageSize = "-2";
			}
			
			island.setDelFlag("0");
			island.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
	        Page<Island> page = islandService.find(new Page<Island>(request, response, Integer.parseInt(pageSize)), island);
	        model.addAttribute("page", page);
	        if(island.getCountry() != null) {
	        	request.setAttribute("countryName", sysGeographyService.getNameCnByUuid(island.getCountry()));
	        } else {
	        	request.setAttribute("countryName", "");
	        }
	        return LIST_PAGE;
		}

		@RequestMapping(value = "form")
		public String form(Island island, Model model, HttpServletRequest request) {
			model.addAttribute("island", island);
			
			String source = request.getParameter("source");
			if(StringUtils.isNotBlank(source)&&source.equals("hotelForm")){
				model.addAttribute("source", source);
			}
			Map<String,String> positionMap = new LinkedHashMap<String,String>();
			positionMap.put("1", "境内岛屿");
			positionMap.put("2", "境外岛屿");
			model.addAttribute("positionMap", positionMap);
			return FORM_PAGE;
		}

		@ResponseBody
		@RequestMapping(value = "save")
		public Object save(Island island, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
			Map<String,Object> datas = new HashMap<String, Object>();
			String[] shortAddressArr = request.getParameterValues("shortAddress");
			
			Long companyId = UserUtils.getUser().getCompany().getId();
			if(island != null && companyId != null) {
				String position= request.getParameter("position");
				if("2".equals(position)){
	    			island.setCountry(request.getParameter("overseas_state"));
	    			island.setProvince(request.getParameter("overseas_province"));
	    			island.setCity(request.getParameter("overseas_city"));
	    			island.setShortAddress(shortAddressArr[1]);
	    		}else if("1".equals(position)){
	    			island.setShortAddress(shortAddressArr[0]);
	    		}
				island.setWholesalerId(companyId.intValue());
				islandService.saveIsland(island, buildAnnexList(request));
				
				//酒店表单中的新增岛屿功能
				String source = request.getParameter("source");
				if(StringUtils.isNotBlank(source) && source.equals("hotelForm")){
					datas.put("message","4");
					datas.put("uuid",island.getUuid());
					datas.put("islandName",island.getIslandName());
				}else{
					datas.put("message","1");
				}
				
			} else {
				datas.put("message","3");
				datas.put("error", "系统异常，请重新操作!");
			}
			
			return datas;
		}
		
		@RequestMapping(value = "show/{uuid}")
		public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
			if(StringUtils.isEmpty(uuid)) {
				return RE_LIST_PAGE;
			}
			
			Island island = islandService.getByUuid(uuid);
			if(island == null || island.getWholesalerId() != UserUtils.getUser().getCompany().getId().intValue()) {
				return SHOW_PAGE;
			}
			List<HotelAnnex> prodSchList=hotelAnnexService.getAnnexListByMainUuid(uuid);
			model.addAttribute("prodSchList", prodSchList);
			model.addAttribute("island", island);
			return SHOW_PAGE;
		}
		/**
		 * 加入 flag=1，用来判断是修改的页面
		 * @param uuid
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "edit/{uuid}")
		public String edit(@PathVariable String uuid, Model model) {
			Map<String,String> positionMap = new LinkedHashMap<String,String>();
			positionMap.put("1", "境内岛屿");
			positionMap.put("2", "境外岛屿");
			
			if(StringUtils.isEmpty(uuid)) {
				return RE_LIST_PAGE;
			}
			
			Island island = islandService.getByUuid(uuid);
			if(island == null || island.getWholesalerId() != UserUtils.getUser().getCompany().getId().intValue()) {
				return FORM_PAGE;
			}
			
			HotelAnnex hotelAnnex = new HotelAnnex();
			hotelAnnex.setDelFlag("0");
			hotelAnnex.setMainUuid(island.getUuid());
			hotelAnnex.setType(3);
			List<HotelAnnex> hotelAnnexs = hotelAnnexService.find(hotelAnnex);
			if(island.getPosition()==1){
				model.addAttribute("countrys",getGeoListFromPosition(island.getPosition().toString(),""));
				model.addAttribute("provinces", getGeoListFromPosition("",island.getCountry()));
				model.addAttribute("citys", getGeoListFromPosition("",island.getProvince()));
				model.addAttribute("districts", getGeoListFromPosition("",island.getCity()));
			}else if(island.getPosition()==2){
				model.addAttribute("countrys",getGeoListFromPosition(island.getPosition().toString(),""));
				model.addAttribute("provinces", getGeoListFromPosition("",island.getCountry()));
				model.addAttribute("citys", getGeoListFromPosition("",island.getProvince()));
			}

			model.addAttribute("hotelAnnexs", hotelAnnexs);
			model.addAttribute("island", island);
			model.addAttribute("positionMap", positionMap);
			return "modules/island/island/edit";
		}
		
		@ResponseBody
		@RequestMapping(value = "update")
		public Object update(Island island, Model model, HttpServletRequest request) {
			Map<String,Object> datas = new HashMap<String, Object>();
			String position = request.getParameter("position");
			String[] shortAddress = request.getParameterValues("shortAddress");
			
			Island entity = null;
			if(!StringUtils.isEmpty(island.getUuid())) {
				entity = islandService.getByUuid(island.getUuid());

				entity.setPosition(island.getPosition());
				entity.setType(island.getType());
				entity.setIslandName(island.getIslandName());
				entity.setShortName(island.getShortName());
				entity.setSpelling(island.getSpelling());
				entity.setShortSpelling(island.getShortSpelling());
				entity.setIslandNameEn(island.getIslandNameEn());
				entity.setShortNameEn(island.getShortNameEn());
				entity.setTopic(island.getTopic());
				entity.setIslandWay(island.getIslandWay());
				entity.setSort(island.getSort());
				if("1".equals(position)){
					entity.setCountry(island.getCountry());
					entity.setProvince(island.getProvince());
					entity.setCity(island.getCity());
					entity.setDistrict(island.getDistrict());
					entity.setShortAddress(shortAddress[0]);
				}else if("2".equals(position)){
	    			entity.setCountry(request.getParameter("overseas_state"));
	    			entity.setProvince(request.getParameter("overseas_province"));
	    			entity.setCity(request.getParameter("overseas_city"));
	    			entity.setShortAddress(shortAddress[1]); 
				}
			
				//entity.setShortAddress(island.getShortAddress());
				entity.setIslandAddress(island.getIslandAddress());
				entity.setDescription(island.getDescription());
				
			} else {
				datas.put("message", "3");
				datas.put("error", "系统异常，请重新操作!");
				return datas;
			}
			islandService.updateIsland(entity, buildAnnexList(request));
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
					islandService.removeByUuid(uuid);
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
		public String check(String uuid, HttpServletRequest request) throws IOException {
			String islandName = request.getParameter("islandName");
			if(StringUtils.isEmpty(uuid))
			{
				uuid = "0";
			}
			
			if(islandService.findIsExist(uuid, islandName, UserUtils.getUser().getCompany().getId())) {
				return "false";
			}
			return "true";
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
					String[] records = uuidAndSortsStr.split(",");
					
					//组装数据
					if(records != null && records.length != 0) {
						for(String record : records) {
							String[] uuidAndSorts = record.split("-");
							uuidAndSortMap.put(uuidAndSorts[0], uuidAndSorts[1]);
						}
					}
					
					//更新酒店餐型排序
					Set<String> uuids = uuidAndSortMap.keySet();
					if(uuids != null && !uuids.isEmpty()) {
						for(String uuid : uuids) {
							Island island = islandService.getByUuid(uuid);
							island.setSort(Integer.parseInt(uuidAndSortMap.get(uuid)));
							islandService.update(island);
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
		
		//装载上传文件信息
		private List<HotelAnnex> buildAnnexList(HttpServletRequest request) {
			List<HotelAnnex> list = new ArrayList<HotelAnnex>();
			if(ArrayUtils.isNotEmpty(request.getParameterValues("hotelAnnexDocId"))){
				String[] hotelAnnexDocId = request.getParameterValues("hotelAnnexDocId");
				String[] docOriName = request.getParameterValues("docOriName");
				String[] docPath = request.getParameterValues("docPath");
				
				for(int i=0;i<hotelAnnexDocId.length;i++){
					if(StringUtils.isNotBlank(hotelAnnexDocId[i])){
						HotelAnnex ha = new HotelAnnex();
						ha.setDocId(Integer.parseInt(hotelAnnexDocId[i]));
						ha.setDocPath(docPath[i]);
						ha.setDocName(docOriName[i]);
						list.add(ha);
					}
				}
			}
			return list;
		}
		@RequestMapping(value ="getIslandList/{showType}/{orderStatus}")
		public String getIslandList(@PathVariable String showType, @PathVariable String orderStatus,HttpServletResponse response, Model model, HttpServletRequest request) throws Exception {
			
			//modules/order/orderListForDzOrderPay
			model.addAttribute("showType",showType); 
			return "/modules/island/activityisland/islandProductList";
		}
		
		private List<SysGeography> getGeoListFromPosition(String type,String parentId) {
			List<SysGeography> allList = new ArrayList<SysGeography>();
			SysGeography sysGeography = new SysGeography();
			sysGeography.setUuid("");
	        sysGeography.setNameCn("请选择");
	        allList.add(sysGeography);
	        
			sysGeography = new SysGeography();
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
	        allList.addAll(list);
			return allList;
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
}
