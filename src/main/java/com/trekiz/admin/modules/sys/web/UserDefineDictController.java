package com.trekiz.admin.modules.sys.web;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.UserDefineDict;
import com.trekiz.admin.modules.sys.service.UserDefineDictService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 渠道字典Controller
 * @author jiachen
 * @version 2014-3-21
 */

@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class UserDefineDictController extends BaseController {
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 163;
	}
	
	@Autowired
	private UserDefineDictService userDefineDictService;
	
//	@Autowired
//	private DictService dictService;
//	
//	private Hanyu hanyu;
	
	//目的地区域列表(不允许分页)
	@RequiresPermissions("common:mtour:menu")
	@RequestMapping(value = "normalList")
	public String normalList(@RequestParam(required=false) String type,Model model) {
		Long companyId = StringUtils.toLong(UserUtils.getUser().getCompany().getId());
		//根据type获取列表
		List<Area> holderList = new ArrayList<Area>();
		List<Area> arealist = userDefineDictService.findAll();
		List<UserDefineDict> areaDefineDictList = userDefineDictService.findDefineDict(companyId,type);
		Set<Long> set = new HashSet<Long>();
		if(areaDefineDictList.size()!=0) {
			for(UserDefineDict userDefineDict:areaDefineDictList) {
				set.add(StringUtils.toLong(userDefineDict.getDictId()));
			}
			if("area".equals(type))
					arealist.remove(0);
			
			for(Area area:arealist) {
				if(set.contains(area.getId())) {
					area.setSelected("1");
				}
			}
		}
		//目的地区域排序
		Area.sortList(holderList, arealist, 1L);
		model.addAttribute("dataList",holderList);
		return "modules/sys/"+type+"DictList";
	}
	//分页查询列表
	@RequestMapping(value = "pagingList")
	public String pagingList(@RequestParam(required=false)String type,@RequestParam(value="term1",required=false)String term1,HttpServletRequest request, HttpServletResponse response, Model model) {
		Long companyId = StringUtils.toLong(UserUtils.getUser().getCompany().getId());
		userDefineDictService.findPagind(companyId,type);
		Page<Dict> page = userDefineDictService.searchDictPage(new Page<Dict>(request, response),type,term1);
		model.addAttribute("page", page);
		model.addAttribute("seek", term1);
		return "modules/sys/"+type+"DictList";
	}
	//修改
	@RequestMapping(value="descUpdate")
	public String update(String checkedIds,String cancelIds,String type,Model model, RedirectAttributes redirectAttributes) {
		if(checkedIds!=null||cancelIds!=null&&type!=null){
			Long companyId = UserUtils.getUser().getCompany().getId();
			String[] checkedIdArr = checkedIds.split(",");
			String[] cancelIdArr = cancelIds.split(",");
			userDefineDictService.update(checkedIdArr,cancelIdArr,type,companyId);
			addMessage(redirectAttributes, "1");
		}
		String kind = "";
		if("area".equals(type)){
			kind = "normalList";
		}else{
			kind = "pagingList";
		}
		
		return "redirect:"+Global.getAdminPath()+"/sys/dict/"+kind+"?type="+type;
	}
	//搜索
	@RequestMapping(value="descSearch")
	public String descSearch(String upLoadTypeIds,String term,String type,HttpServletRequest request, HttpServletResponse response,Model model) {
		//目前只能满足(sys_dict,sys_area)两张表
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(term != null && !"".equals(term)){
			Set<String> set = new HashSet<String>();
			List<UserDefineDict> selectedList = userDefineDictService.findDefineDict(companyId, type);
			for(UserDefineDict dict:selectedList) {
				set.add(dict.getDictId().toString());
			}
			//勾选的记录做标记
			if("area".equals(type)){
				List<Area> searchList = userDefineDictService.areaSearch(term);
				List<Area> pageSearchList = new ArrayList<Area>();
				for(Area area:searchList){
					if(set.contains(area.getId().toString())){
						area.setSelected("1");
					}
				}
				Area.sortList(pageSearchList, searchList, 1L);
				model.addAttribute("dataList", searchList);
			}else{
				Page<Dict> page = userDefineDictService.searchDictPage(new Page<Dict>(request, response), type, term);
//				List<Dict> searchList = userDefineDictService.dictSearch(term,dictType);
				for(Dict dict:page.getList()){
					if(set.contains(dict.getId().toString())){
						dict.setSelected("1");
					}
				}
				model.addAttribute("page", page);
			}
			model.addAttribute("seek", term);
			return "modules/sys/"+type+"DictList";
//			return "redirect:"+Global.getAdminPath()+"/stock/manager/pagingList?type="+type;
		}
		//如果搜索条件为空，则刷新当前页
		String kind = "";
		if("area".equals(type)){
			kind = "normal";
		}else{
			kind = "paging";
		}
		return "redirect:"+Global.getAdminPath()+"/sys/dict/"+kind+"List?type="+type;
	}
//	//修改出发地首字母排序
//	@RequestMapping("setSort")
//	public void setSort() {
//		hanyu = new Hanyu();
//		List<Dict> fromAreaList = dictService.findByType(Context.FROM_AREA);
//		for(Dict dict:fromAreaList) {
//			String label = dict.getLabel();
//			String firstWords = hanyu.getCharacterPinYin(label.substring(0, 1).charAt(0)).toUpperCase();
//			System.out.println(label+"---"+firstWords);
//			byte firstWordsAscii = (byte)firstWords.charAt(0);
//			dict.setSort((int)firstWordsAscii);
//			dictService.save(dict);
//		}
//	}
}
