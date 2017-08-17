/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.preferential.entity.PreferentialDict;
import com.trekiz.admin.modules.preferential.entity.PreferentialDictTemplatesRel;
import com.trekiz.admin.modules.preferential.entity.PreferentialTemplates;
import com.trekiz.admin.modules.preferential.service.PreferentialDictTemplatesRelService;
import com.trekiz.admin.modules.preferential.service.PreferentialDictUnitRelService;
import com.trekiz.admin.modules.preferential.service.PreferentialTemplatesService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/preferentialTemplates")
public class PreferentialTemplatesController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/preferential/preferentialtemplates/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/preferentialTemplates/list";
	protected static final String FORM_PAGE = "modules/preferential/preferentialtemplates/form";
	protected static final String SHOW_PAGE = "modules/preferential/preferentialtemplates/show";
	
	/** 优惠模板Service */
	@Autowired
	private PreferentialTemplatesService preferentialTemplatesService;
	
	/** 优惠字典单位和优惠模板关联表Service */
	@Autowired
	private PreferentialDictTemplatesRelService preferentialDictTemplatesRelService;
	
	/** 优惠字典单位关联表Service */
	@Autowired
	private PreferentialDictUnitRelService preferentialDictUnitRelService;
	
	@RequestMapping(value = "list")
	public String list(PreferentialTemplates preferentialTemplates, HttpServletRequest request, HttpServletResponse response, Model model) {
		preferentialTemplates.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		Page<PreferentialTemplates> page = preferentialTemplatesService.find(new Page<PreferentialTemplates>(request, response), preferentialTemplates);
		
		//拼接因果描述
		if(page != null && CollectionUtils.isNotEmpty(page.getList())) {
			for(PreferentialTemplates temp : page.getList()) {
				List<Object[]> list = preferentialTemplatesService.findTempsInfoByUuid(temp.getUuid());
				if(list == null || CollectionUtils.isEmpty(list)) {
					continue;
				}
				StringBuffer description = new StringBuffer();
				boolean causeFlag = true;
				boolean effectFlag = true;
				
				for(Object[] objArray : list) {
					if(Integer.parseInt(objArray[3].toString()) == PreferentialDict.TYPE_CAUSE && causeFlag) {
						causeFlag = false;
						description.append("因：");
					} else if(Integer.parseInt(objArray[3].toString()) == PreferentialDict.TYPE_EFFECT && effectFlag) {
						effectFlag = false;
						description.append("&nbsp;&nbsp;&nbsp;&nbsp;果：");
					}
					description.append(preferentialDictUnitRelService.findDescByNamesAndDataType(String.valueOf(objArray[1]), String.valueOf(objArray[2]), Integer.parseInt(objArray[4].toString())));
					description.append("&nbsp;&nbsp;&nbsp;&nbsp;");
				}
				temp.setOutHtml(description.toString());
			}
		}
		
        model.addAttribute("page", page);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(PreferentialTemplates preferentialTemplates, Model model) {
		
		initCauseDictAndEffectDictList(model);
		model.addAttribute("preferentialTemplates", preferentialTemplates);
		
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(PreferentialTemplates preferentialTemplates, Model model, HttpServletRequest request) {
		Map<String,Object> datas = new HashMap<String, Object>();
		//设置默认值，供以后扩展使用
		preferentialTemplates.setType("0");
		
		try {
			//根据前台页面，保存模板表数据,并保存字典单位关联和模板关联表
			preferentialTemplatesService.saveTemplatesAndRel(preferentialTemplates, buildDictTemplatesRelData(request));
			
			
			datas.put("message","1");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("message","3");
			datas.put("error", "系统异常，请重新操作!");
		}
		
		return datas;
	}
	
	/**
	 * 组装前台的模板关联数据
	 * @param request
	 * @author majiancheng
	 * @Time 2015-5-26
	 */
	private Map<String,Object> buildDictTemplatesRelData(HttpServletRequest request) {
		String causeDictUuid = request.getParameter("causeDictUuid");
		String causeUnitUuid = request.getParameter("causeUnitUuid");
		String effectDictUuid = request.getParameter("effectDictUuid");
		String effectUnitUuid = request.getParameter("effectUnitUuid");
		
		Map<String, Object> datas = new HashMap<String, Object>();
		//组装模板关联数据
		List<PreferentialDictTemplatesRel> tempsRels = new ArrayList<PreferentialDictTemplatesRel>();
		PreferentialDictTemplatesRel causeRel = new PreferentialDictTemplatesRel();
		causeRel.setDictUuid(causeDictUuid);
		causeRel.setUnitUuid(causeUnitUuid);
		
		PreferentialDictTemplatesRel effectRel = new PreferentialDictTemplatesRel();
		effectRel.setDictUuid(effectDictUuid);
		effectRel.setUnitUuid(effectUnitUuid);

		tempsRels.add(causeRel);
		tempsRels.add(effectRel);
		
		datas.put("tempsRels", tempsRels);
		StringBuffer outHtml = new StringBuffer();
		outHtml.append(preferentialDictUnitRelService.getOutHtmlByDictUuidAndUnitUuid(causeDictUuid, causeUnitUuid, PreferentialDict.TYPE_CAUSE));
		outHtml.append(preferentialDictUnitRelService.getOutHtmlByDictUuidAndUnitUuid(effectDictUuid, effectUnitUuid, PreferentialDict.TYPE_EFFECT));
		datas.put("outHtml", outHtml.toString());
		return datas;
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("preferentialTemplates", preferentialTemplatesService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		//初始化因和果列表数据
		initCauseDictAndEffectDictList(model);
		
		List<Object[]> objs = preferentialTemplatesService.findRelInfoByTempUuid(uuid);
		if(objs != null && CollectionUtils.isNotEmpty(objs)) {
			for(Object[] objArray : objs) {
				if(Integer.parseInt(objArray[4].toString()) == PreferentialDict.TYPE_CAUSE) {
					model.addAttribute("causeDictDefault", objArray[2]);
					model.addAttribute("causeUnits", this.getUnitDatasByDictUuid(String.valueOf(objArray[2])));
					model.addAttribute("causeDesc", preferentialDictUnitRelService.findNamesByDictUuidAndUnitUuid(String.valueOf(objArray[2]), String.valueOf(objArray[3])));
					
					model.addAttribute("causeUnitDefault", objArray[3]);
				} else if(Integer.parseInt(objArray[4].toString()) == PreferentialDict.TYPE_EFFECT) {
					model.addAttribute("effectDictDefault", objArray[2]);
					model.addAttribute("effectUnits", this.getUnitDatasByDictUuid(String.valueOf(objArray[2])));
					model.addAttribute("effectDesc", preferentialDictUnitRelService.findNamesByDictUuidAndUnitUuid(String.valueOf(objArray[2]), String.valueOf(objArray[3])));
					
					model.addAttribute("effectUnitDefault", objArray[3]);
				}
			}
		}
		
		model.addAttribute("preferentialTemplates", preferentialTemplatesService.getByUuid(uuid));
		return FORM_PAGE;
	}
	
	@ResponseBody
	@RequestMapping(value = "update")
	public Object update(PreferentialTemplates preferentialTemplates, Model model, HttpServletRequest request) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		
		try {
			preferentialTemplatesService.updateTemplatesAndRel(preferentialTemplates, buildDictTemplatesRelData(request));
			
			datas.put("message","2");
		} catch (Exception e) {
			e.printStackTrace();
			datas.put("message","3");
			datas.put("error", "系统异常，请重新操作!");
		}
		
		return datas;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				if(uuids.indexOf(",") == -1) {
					preferentialTemplatesService.removeByUuid(uuids);
				} else {
					for(String uuid : uuids.split(",")) {
						preferentialTemplatesService.removeByUuid(uuid);
					}
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
	
	@ResponseBody
	@RequestMapping(value = "getRelDesc")
	public Object getRelDesc(String dictUuid, String unitUuid) {
		Map<String,Object> datas = new HashMap<String, Object>();
		//根据优惠字典单位关联表uuid获取拼接的描述
		datas.put("desc", preferentialDictUnitRelService.findNamesByDictUuidAndUnitUuid(dictUuid, unitUuid));
		return datas;
	}
	
	/** 加载因果下拉列表框 */
	private void initCauseDictAndEffectDictList(Model model) {
		List<Map<String, String>> causeDictList = preferentialDictTemplatesRelService.findDictUuidAndDictNameByType(PreferentialDict.TYPE_CAUSE);
		List<Map<String, String>> effectDictList = preferentialDictTemplatesRelService.findDictUuidAndDictNameByType(PreferentialDict.TYPE_EFFECT);
		model.addAttribute("causeDictList", causeDictList);
		model.addAttribute("effectDictList", effectDictList);
	}
	
	@ResponseBody
	@RequestMapping(value = "getUnitsByDictUuid")
	public Object getUnitsByDictUuid(String uuid) {
		Map<String,Object> datas = new HashMap<String, Object>();
		//根据优惠字典单位关联表uuid获取拼接的描述
		datas.put("units", this.getUnitDatasByDictUuid(uuid));
		return datas;
	}
	
	/**
	 * 根据字典uuid获取单位列表
	 * @param uuid
	 * @return
	 */
	private List<Map<String, String>> getUnitDatasByDictUuid(String uuid) {
		return preferentialDictUnitRelService.getUnitsByDictUuid(uuid);
	}
}
