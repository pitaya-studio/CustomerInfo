package com.trekiz.admin.agentToOffice.line.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.agentToOffice.PricingStrategy.entity.PriceStrategy;
import com.trekiz.admin.agentToOffice.PricingStrategy.service.PricingStrategyService;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/priceStrategy/line")
public class TouristLineController {
	
	@Autowired
	private TouristLineService touristLineService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private PricingStrategyService pricestrategyService;
	/**
	 * 查询所有的线路
	 * @param model
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@RequestMapping(value="getAllLines")
	public String getAllLines(Model model){
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<TouristLine> tls = touristLineService.getTouristLineByCompanyId(companyId);
		
		List<Map<String, Object>> list = new ArrayList<>();
		for(TouristLine tl : tls){
			Map<String, Object> map = new HashMap<>();
			StringBuffer areaName = new StringBuffer("");
			//TODO -修改为区域
			String[] areas = tl.getDestinationIds().split(",");
			for(String areaId : areas){
				if(StringUtils.isNotEmpty(areaId)){
					Area area = areaService.findById(Long.parseLong(areaId));
					areaName.append(area.getName()).append(",");
				}
			}
			String areaNames = areaName.toString().substring(0, areaName.length() - 1);
			map.put("areaName", areaNames);
			map.put("line", tl);
			list.add(map);
		}
		model.addAttribute("list",list);
		return "/agentToOffice/line/lineList";
	}
	
	/**
	 * 删除线路
	 * @param id
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="deleteLine")
	public boolean deleteLine(Long id){
		try {
			touristLineService.deleteTouristLine(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 添加线路
	 * @param touristLine
	 * @return
	 * @author yang.wang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="saveLine")
	public boolean saveLine(Long id, String destinationIds, String lineName){
		
		Long userId = UserUtils.getUser().getId();
		
		TouristLine tl = null;
		if (id == null) { // 新建
			Long companyId = UserUtils.getUser().getCompany().getId();
			tl = new TouristLine();
			try {
				tl.setCompanyId(companyId.intValue());
				tl.setUuid(UUID.randomUUID().toString());
				tl.setCreateBy(userId);
				tl.setCreateDate(new Date());
				tl.setUpdateBy(userId);
				tl.setUpdateDate(new Date());
				tl.setDestinationIds(destinationIds);
				tl.setLineName(lineName);
				//TODO - 添加区域反算
				touristLineService.saveTouristLine(tl);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else { // 修改
			tl = touristLineService.getById(id);
			try {
				tl.setDestinationIds(destinationIds);
				tl.setLineName(lineName);
				tl.setUpdateBy(userId);
				tl.setUpdateDate(new Date());
				//TODO - 添加区域反算
				touristLineService.updateTouristLine(tl);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
	}
	
	/**
	 * 跳转到添加页面
	 * @param model
	 * @return
	 * @author yang.wang@quauq.com
	 * @date 2016.10.12
	 */
	@RequestMapping(value="form")
	public String form(Model model, 
			@RequestParam(required = false) String destinationIds, 
			@RequestParam(required = false) Long id) {
		
		Set<Long> idSet = null;
		if (StringUtils.isNotBlank(destinationIds)) {
			idSet = new HashSet<>();
			String[] dIds = destinationIds.split(",");
			for (String destinationId : dIds) {
				Area area = areaService.get(Long.parseLong(destinationId));
				String[] parentIds = area.getParentIds().split(",");
				for (String parentId : parentIds) {
					idSet.add(Long.parseLong(parentId));
				}
				idSet.add(Long.parseLong(destinationId));
			}
		}
		
		TouristLine tl = new TouristLine();
		model.addAttribute("isModify", false);
		if (id != null) {
			tl = touristLineService.getById(id);
			model.addAttribute("isModify", true);
		}
		
		Area root = areaService.findById(1L);
		Map<String, Object> areaMap = buildAreasTree(root, idSet);
		
		Object object = JSON.toJSON(areaMap);
		model.addAttribute("areaMap", object);
		model.addAttribute("lineName", tl.getLineName());
		model.addAttribute("destinationIds", tl.getDestinationIds());
		model.addAttribute("id", id);
		return "/agentToOffice/line/lineForm";
	}
	
	/**
	 * 递归实现遍历并读取Area树形结构
	 * @author yang.wang
	 * @date 2016.10.12
	 * */
	private Map<String, Object> buildAreasTree(Area area, Set<Long> checkedSet) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("name", area.getName());
		map.put("code", area.getId());
		
		if (CollectionUtils.isNotEmpty(checkedSet)) {
			for (Long dId : checkedSet) {
				if (area.getId().longValue() == dId.longValue()) {
					map.put("checked", true);
					map.put("open", true);
				}
			}
		}
		
		
		if (area.getType().equals("4")) {
			// 树形结构叶子节点，无子树。
		} else {
			// 树形结构非叶子节点，递归遍历该节点的子树
			List<Map<String,Object>> childrenlist = new ArrayList<>();
			List<Area> children = areaService.findByParentId(area.getId().intValue());
			for (Area child : children) {
				Map<String, Object> map2 = buildAreasTree(child, checkedSet);
				childrenlist.add(map2);
			}
			
			map.put("children", childrenlist);
		}
		
		return map;
	}
	
	/**
	 * 查看线路名称是否重复
	 * @param lineName
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="checkLineName")
	public boolean checkName(String lineName, @RequestParam(required = false) Long id) {
		if (id == null) {
			id = new Long(-1);
		}
		boolean b = touristLineService.checkLineName(lineName, id);
		return b;
	}
	
	/**
	 * 查看所含区域是否重复
	 * @param areas
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="checkAreas")
	public boolean checkAreas(String destinationIds, @RequestParam(required = false) Long id) {
		if (id == null) {
			id = new Long(-1);
		}
		boolean b=touristLineService.checkAreas(destinationIds, id);
		return b;
	}
	
	/**
	 * 检查线路是否被使用
	 */
	@ResponseBody
	@RequestMapping(value="checkLine")
	public boolean checkLine(Long id){
		List<PriceStrategy> list = pricestrategyService.findAllStrategyByCompanyId(UserUtils.getUser().getCompany().getId());
		boolean b=false;
		for(PriceStrategy pricestrategy:list){
			String[] split = pricestrategy.getTargetAreaIds().split(",");
			for(String str:split){
				if(str.equals(id+"")){
					b=true;
				}
			}
		}
		return b;
	}
}
