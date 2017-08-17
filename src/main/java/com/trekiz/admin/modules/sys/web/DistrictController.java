/**
 *
 */
package com.trekiz.admin.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.District;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DistrictService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.t1.utils.T1Utils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 区域Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/district")
public class DistrictController extends BaseController {
	@Autowired
	private DistrictService districtService;
	@Autowired
	private AreaService areaService;

	/**
	 * 列表，不需要分页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "")
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
		String name = request.getParameter("name");
		String inout = request.getParameter("inout");
		List<District> list = districtService.getAllDistrict(new Page<District>(request, response), name, inout);
		handleData(list);
		model.addAttribute("list", list);
		model.addAttribute("name", name);
		model.addAttribute("inout", inout);
		return "/modules/sys/district/districtList";
	}

	/**
	 * 添加目的地属性
	 * @param list
	 */
	private void handleData(List<District> list) {
		for (int i = 0; i < list.size(); i++) {
			District district =  list.get(i);
			district.setDestinationIds(areaService.getIdsByDistrictId(district.getId()));
		}
	}

	/**
	 * 跳转到添加页面
	 */
	@RequestMapping(value="form")
	public String form(Model model, @RequestParam(required = false) String destinationIds, @RequestParam(required = false) Long id) {
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

		District district = new District();
		model.addAttribute("isModify", false);
		if (id != null) {
			district = districtService.getDistrictById(id);
			model.addAttribute("isModify", true);
		}

		Area root = areaService.findById(1L);
		Map<String, Object> areaMap = buildAreasTree(root, idSet);

		Object object = JSON.toJSON(areaMap);
		model.addAttribute("areaMap", object);
		model.addAttribute("name", district.getName());
		model.addAttribute("tourInOut", district.getTourInOut());
		String ids = areaService.getIdsByDistrictId(district.getId());
		model.addAttribute("destinationIds", ids);
		model.addAttribute("id", id);
		return "/modules/sys/district/districtForm";
	}

	/**
	 * 递归实现遍历并读取Area树形结构
	 * @param area
	 * @param checkedSet
	 * @return
	 */
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
	 * 添加/修改
	 * @param id
	 * @param destinationIds
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveOrUpdate")
	public boolean saveOrUpdate(Long id, String destinationIds, String name, Integer tourInOut){
		User user = UserUtils.getUser();

		District district = null;
		if (id == null) { // 添加
			district = new District();
		} else { // 修改
			district = districtService.getDistrictById(id);
		}
		try {
			district.setName(name);
			district.setTourInOut(tourInOut);
			districtService.saveOrUpdate(district);
			String[] areaIdArr = destinationIds.split(",");
			for (int i = 0; i < areaIdArr.length; i++) {
				String areaId = areaIdArr[i];
				areaService.updateDistrictById(district.getId(), Long.parseLong(areaId));

			}
			// 更新T1首页缓存
			T1Utils.updateT1HomeCache();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteDistrict")
	public boolean deleteLine(Long id){
		try {
			districtService.deleteDistrict(id);
			// 删除区域同步更新sys_area表数据
			areaService.deleteDistrictId(id);
			// 更新T1首页缓存
			T1Utils.updateT1HomeCache();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param name
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkDistrictName")
	public boolean checkDistrictName(String name, @RequestParam(required = false) Long id) {
		if (id == null) {
			id = new Long(-1);
		}
		boolean b = districtService.checkDistrictName(name, id);
		return b;
	}

	/**
	 * 检查区域是否被使用
	 */
	@ResponseBody
	@RequestMapping(value="checkDistrict")
	public Map<String, Object> checkLine(Long id) {

		Map<String, Object> map = new HashMap<>();
		boolean isUsed = districtService.checkDistrict(id);

		map.put("isUsed", isUsed);

		return map;
	}

}
