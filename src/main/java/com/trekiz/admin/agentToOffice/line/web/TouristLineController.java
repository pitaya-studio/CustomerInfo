package com.trekiz.admin.agentToOffice.line.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/tourist/line")
public class TouristLineController {
	
	@Autowired
	private TouristLineService touristLineService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private DocInfoService docInfoService;
	
	/**
	 * 查询所有的线路
	 * @param model
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@RequestMapping(value="getAllLines")
	public String getAllLines(Model model, HttpServletRequest request, HttpServletResponse response){
		
		Map<String, String> paramMap = new HashMap<>();
		String params = "lineName,areaId";
		setParameters(params, paramMap, request, model);
		Page<TouristLine> page = touristLineService.filterTouristLinesWithPage(new Page<TouristLine>(request, response), paramMap);
		model.addAttribute("page", page);
		
		// 所属区域下拉框
		model.addAttribute("areas4Select", touristLineService.getAllAreas());
		return "/agentToOffice/line/lineList";
	}
	
	private void setParameters(String params, Map<String, String> paramMap,
			HttpServletRequest request, Model model) {
		
		for (String paramName : params.split(",")) {
			String parameter = request.getParameter(paramName);
			if (StringUtils.isNotBlank(parameter)) {
				paramMap.put(paramName, parameter);
				model.addAttribute(paramName, parameter);
			}
		}
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
				// 根据目的地id获取区域id
				String areaIds = touristLineService.getAreaIdsByDIds(destinationIds);
				tl.setAreaIds(areaIds);
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
				// 根据目的地id获取区域id
				String areaIds = touristLineService.getAreaIdsByDIds(destinationIds);
				tl.setAreaIds(areaIds);
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
	public Map<String, Object> checkLine(Long id) {
		
		Map<String, Object> map = new HashMap<>();
		boolean isUsed = touristLineService.checkLine(id);
		
		map.put("isUsed", isUsed);
		
		return map;
	}
	
	/**
	 * 从文件中读取线路插入到tourist_line
	 * @return
	 * @throws IOException
	 * @author chao.zhang
	 */
	@ResponseBody
	@RequestMapping(value = "readDataForLine")
	public String readDataForLine() throws IOException{
		DocInfo docInfo = docInfoService.getDocInfoByNameForLine();
		 File file = new File(Global.getBasePath() + File.separator + docInfo.getDocPath());
		 InputStream is = new FileInputStream(file);
	     XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
	     for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
	            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
	            if (xssfSheet == null) {
	                continue;
	            }
	            // 读取Sheet中数据
	            String lineName = "";
	            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
	                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
	                if (xssfRow != null) {
	                    //线路名称
	                    XSSFCell districtName = xssfRow.getCell(0);
	                    System.out.println("districtName："+districtName);
	                    if (districtName != null && StringUtils.isNotBlank(districtName.toString())) {
	                        lineName = districtName.toString();
	                        // 城市
		                    XSSFCell cityName = xssfRow.getCell(1);
		                    if (cityName != null) {
		                        List<Area> areaList = areaService.findAreaByName(cityName.toString());
		                        if (areaList == null || areaList.size() == 0) {
		                        	 TouristLine touristLine = new TouristLine();
		                        	 touristLine.setLineName(lineName);
		                        	 touristLine.setCompanyId(1);
		                             touristLine.setCreateBy(1L);
		                             Date date = new Date();
		                             touristLine.setCreateDate(date);
		                             touristLine.setUuid(UuidUtils.generUuid());
		                             touristLine.setUpdateBy(1L);
		                             touristLine.setUpdateDate(date);
		                             touristLine.setDelFlag("0");
		                             touristLineService.saveTouristLine(touristLine);
		                            System.out.println("没有这个城市" + cityName.toString());
		                        } else {
		                        	TouristLine touristLine = new TouristLine();
		                        	touristLine.setLineName(lineName);
		                        	Area area = areaList.get(0);
		                            Long areaId = areaList.get(0).getId();
		                            touristLine.setDestinationIds(areaId.toString());
		                            if(area.getSysDistrictId()!=null){
		                            	touristLine.setAreaIds(area.getSysDistrictId().toString());
		                            }else{
		                            	System.out.println(area.getName()+"没有区域id");
		                            }
		                            touristLine.setCompanyId(1);
		                            touristLine.setCreateBy(1L);
		                            Date date = new Date();
		                            touristLine.setCreateDate(date);
		                            touristLine.setUuid(UuidUtils.generUuid());
		                            touristLine.setUpdateBy(1L);
		                            touristLine.setUpdateDate(date);
		                            touristLine.setDelFlag("0");
		                            touristLineService.saveTouristLine(touristLine);
		                        }
		                    }
	                    }else{
	                    	TouristLine touristLine = touristLineService.getLineByName(lineName);
	                    	XSSFCell cityName = xssfRow.getCell(1);
		                    if (cityName != null) {
		                        List<Area> areaList = areaService.findAreaByName(cityName.toString());
		                        if (areaList == null || areaList.size() == 0) {
		                            System.out.println("没有这个城市" + cityName.toString());
		                        } else {
		                        	Area area = areaList.get(0);
		                            Long areaId = area.getId();
		                            Long sysDistrictId = area.getSysDistrictId();
		                            if(touristLine.getDestinationIds()!=null){
		                            	touristLine.setDestinationIds(touristLine.getDestinationIds()+","+areaId);
		                            }else{
		                            	touristLine.setDestinationIds(areaId.toString());
		                            }
		                        	boolean flag = true;
		                        	if(touristLine.getAreaIds()!=null){
		                        		for(String districtId : touristLine.getAreaIds().split(",") ){
			                        		if(sysDistrictId!=null && districtId.equals(sysDistrictId.toString())){
			                        			flag = false;
			                        		}
			                        	}
		                        		if(flag){
		                        			if(sysDistrictId != null){
		                        				touristLine.setAreaIds(touristLine.getAreaIds()+","+sysDistrictId);
		                        			}
			                        	}
		                        	}else{
		                        		if(sysDistrictId != null){
		                        			touristLine.setAreaIds(sysDistrictId.toString());
		                        		}
		                        	}
		                        	touristLineService.updateTouristLine(touristLine);
		                        }
		                    }
	                    }
	                }
	            }
	        }
	        return "数据关联成功";
	}
}
