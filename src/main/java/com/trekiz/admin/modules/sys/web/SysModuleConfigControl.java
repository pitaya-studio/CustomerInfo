package com.trekiz.admin.modules.sys.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.SysModuleConfig;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysModuleConfigService;
import com.trekiz.admin.modules.sys.service.SysModulePathService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
/**
 * 自由配置模块的配置及管理
 * @author wangxk
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/modules/sysmoduleconfig")
public class SysModuleConfigControl extends BaseController {
	private final static Log log = LogFactory.getLog(SysModuleConfigControl.class);
	@Autowired
	private SysModuleConfigService sysModuleService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private SysModulePathService sysModulePathService;
	
	/**
	 * 点击添加按钮之后的页面
	 * @param request      
	 * @param response   
	 * @param model     
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="toSaveSysModuleConfig")
	public String toSaveSysModuleConfig(HttpServletRequest request,HttpServletResponse response,Model model){
		
		List<Office> officelist = officeService.findAll();
		List fmodulelist = getFatherModulesListFromJson();
		List modulePathlist = sysModulePathService.queryAllSysModulePath();
		
		model.addAttribute("officelist", officelist);
		model.addAttribute("fmodulelist", fmodulelist);
		model.addAttribute("modulePathlist", modulePathlist);
		return "modules/sysmoduleconfig/sysModuleConfigSave";
	}
	/**
	 * 点击保存之后的页面，保存成功跳转到查询页面，最新保存的数据，按生成的时间的倒叙排列
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "saveSysModuleConfig")
	public String saveSysModuleConfig(HttpServletRequest request,HttpServletResponse response,Model model){
		String companyId = request.getParameter("companyId");//批发商ID
		String fmoduleId = request.getParameter("fmoduleId");//父模块Id
		String moduleId = request.getParameter("moduleId");//模块Id
		String pageName  = request.getParameter("pageName");//页面名称
		String path = request.getParameter("path");//路径
		String prepath = request.getParameter("prepath");//预览路径
		
		User user = UserUtils.getUser();
		
		Map fmodulemap = getFatherModulesMapFromJson();
		String fmodulename = (String)fmodulemap.get(fmoduleId);
		
		Map modulemap = (Map)getSonModulesMapByFid(fmoduleId);
		String modulename = (String)modulemap.get(moduleId);		
		
		SysModuleConfig sysModule = new SysModuleConfig();
		sysModule.setId(UUID.randomUUID().toString());
		if(companyId!=null&&!"".equals(companyId)){
			sysModule.setCompanyId(Long.valueOf(companyId));
		}
		sysModule.setFmoduleId(fmoduleId);
		sysModule.setFmodulename(fmodulename);
		sysModule.setModuleId(moduleId);
		sysModule.setModulename(modulename);
		sysModule.setPageName(pageName);//页面名称
		sysModule.setPath(path);
		sysModule.setPrePath(prepath);
		sysModule.setCreateBy(user.getLoginName());
		sysModule.setCreateDate(new Date());
		
		SysModuleConfig smc = sysModuleService.queryModuleByPathAndCompanyId(path, companyId);
		if(smc!=null&&smc.getId()!=null&&!"".equals(smc.getId())){
			model.addAttribute("reason", "批发商和配置的路径是唯一的，该页面已经配置，不能重复配置");
			return "modules/sysmoduleconfig/sysModuleConfigSave";
		}
		
		sysModuleService.saveSysModuleConfig(sysModule);
		log.info("公司Id： " + companyId + "  模块名： " + modulename + "配置成功");
		List fmodulelist = getFatherModulesListFromJson();//进入到查询的页面 ,默认按日期排序
		List<Office> officelist = officeService.findAll();//根据条件进行查询，默认时间倒序
		List modulelist = new ArrayList();
		if(fmoduleId!=null&&!"".equals(fmoduleId)){
			modulelist = getSonModulesListByFid(fmoduleId);
		}
		sysModule = new SysModuleConfig();
		Map conditionsMap = new HashMap<String,Object>();//根据日期进行查询
		Page<Map<String,Object>> page = sysModuleService.querySysModuleConfigList(request,response,conditionsMap,sysModule,"");
		
		model.addAttribute("page", page);
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("fmodulelist", fmodulelist);
		model.addAttribute("modulelist", modulelist);
		model.addAttribute("officelist", officelist);
		
		return "modules/sysmoduleconfig/sysModuleConfigListQuery";
	}
	/**
	 * 根据ID查询单个的明细
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "querySysModuleConfigById")
	public String querySysModuleConfigById(HttpServletRequest request,HttpServletResponse response,Model model){
		String id = request.getParameter("id");
		SysModuleConfig sysModule = sysModuleService.querySysModuleConfigById(id);
		Office office= officeService.get(sysModule.getCompanyId());
		model.addAttribute("sysModule", sysModule);
		model.addAttribute("office", office);
		return "modules/sysmoduleconfig/sysModuleConfigById";
	}
	/**
	 * 根据ID查询修改模块的内容
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "toUpdateSysModuleConfig")
	public String toUpdateSysModuleConfig(HttpServletRequest request,HttpServletResponse response,Model model){
		String id = request.getParameter("id");
		SysModuleConfig sysModule = sysModuleService.querySysModuleConfigById(id);
		//根据条件进行查询，默认时间倒序
		List<Office> officelist = officeService.findAll();
		String fmoduleId = sysModule.getFmoduleId();
		
		List fmodulelist = getFatherModulesListFromJson();
		List modulelist = new ArrayList();
		if(fmoduleId!=null&&!"".equals(fmoduleId)){
			modulelist = getSonModulesListByFid(fmoduleId);
		}
		List modulePathlist = sysModulePathService.queryAllSysModulePath();
		
		model.addAttribute("modulePathlist", modulePathlist);
		model.addAttribute("sysModule", sysModule);
		model.addAttribute("fmodulelist", fmodulelist);
		model.addAttribute("modulelist",modulelist );
		model.addAttribute("officelist",officelist );
		return "modules/sysmoduleconfig/sysModuleConfigUpdate";
	}
	/**
	 * 点击修改页面的提交按钮执行的方法
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="updateSysModuleConfig")
	public String updateSysModuleConfig(HttpServletRequest request,HttpServletResponse response,Model model){
		String id = request.getParameter("sysid");
		String companyId = request.getParameter("companyId");//批发商ID
		String moduleId = request.getParameter("moduleId");
		String fmoduleId = request.getParameter("fmoduleId");
		String pageName  = request.getParameter("pageName");
		String path = request.getParameter("path");
		String prepath = request.getParameter("prepath");
		
		Map fmodulemap = getFatherModulesMapFromJson();
		String fmodulename = (String)fmodulemap.get(fmoduleId);
		
		Map modulemap = (Map)getSonModulesMapByFid(fmoduleId);
		String modulename = (String)modulemap.get(moduleId);
		
		User user = UserUtils.getUser();
		Long lcompanyId = new Long(0);
		if(companyId!=null&&!"".equals(companyId)){
			lcompanyId = Long.valueOf(companyId);
		}
		int num = sysModuleService.updateSysModuleConfig(id,lcompanyId,fmoduleId,moduleId,pageName,path,prepath,user.getLoginName(),new Date(),fmodulename,modulename);
		log.info("sys_config_company更新的记录id: " +  id + "更新的行数： " + num);
		//根据条件进行查询，默认时间倒序
		List<Office> officelist = officeService.findAll();
		List fmodulelist = getFatherModulesListFromJson();
		
		SysModuleConfig sysModule = new SysModuleConfig();
		Map conditionsMap = new HashMap<String,Object>();//根据日期进行查询
		Page<Map<String,Object>> page = sysModuleService.querySysModuleConfigList(request,response,conditionsMap,sysModule,"");
		
		model.addAttribute("page", page);
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("fmodulelist", fmodulelist);
		model.addAttribute("officelist", officelist);
		
		return "modules/sysmoduleconfig/sysModuleConfigListQuery";
	}
	/**
	 * 根据Id删除当前的记录
	 * @param id
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="delSysModuleConfig")
	@ResponseBody
	public Object delSysModuleConfig(@RequestParam String id,HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> data = new HashMap<String, Object>();
		
		sysModuleService.delSysModuleConfig(id);
		log.info("sys_config_company表中删除成功的记录的id是: " +  id);
		data.put("delsuccess", "删除成功！");
		return  data;
	}
	/**
	 * 灵活配置模块的查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "queryModuleConfigList")
	public String queryModuleConfigList(HttpServletRequest request,HttpServletResponse response,Model model){
		String fmoduleId = request.getParameter("fmoduleId");
		String moduleId = request.getParameter("moduleId");
		String companyId = request.getParameter("companyId");
		String pageName = request.getParameter("pageName");//页面名称
		String path = request.getParameter("path");//路径
		
		List fmodulelist = getFatherModulesListFromJson();
		List modulelist = new ArrayList();
		if(fmoduleId!=null&&!"".equals(fmoduleId)){
			modulelist = getSonModulesListByFid(fmoduleId);
		}
		
		SysModuleConfig sysModule = new SysModuleConfig();
		sysModule.setFmoduleId(fmoduleId);
		sysModule.setModuleId(moduleId);
		if(companyId!=null&&!"".equals(companyId)){
			sysModule.setCompanyId(Long.valueOf(companyId));
		}
		sysModule.setPageName(pageName);
		sysModule.setPath(path);
		
		Map<String,Object> conditionsMap = new HashMap<String,Object>();//根据日期进行查询
		//根据条件进行排序
		String sysModuleIdSort = request.getParameter("sysModuleIdSort"); // 更新模块   desc or not 
		String sysModuleIdCss  = request.getParameter("sysModuleIdCss");// 
		String sysModuleSonIdSort = request.getParameter("sysModuleSonIdSort");// 更新日期排序标识
		String sysModuleSonIdCss  = request.getParameter("sysModuleSonIdCss");//订单创建日期排序标识
		String sysOfficeSort = request.getParameter("sysOfficeSort");//订单更新日期排序标识
		String sysOfficeCss  = request.getParameter("sysOfficeCss");// 创建日期排序标识
		
		conditionsMap.put("sysModuleIdSort", sysModuleIdSort);
		conditionsMap.put("sysModuleIdCss", sysModuleIdCss);
		conditionsMap.put("sysModuleSonIdSort", sysModuleSonIdSort);
		conditionsMap.put("sysModuleSonIdCss", sysModuleSonIdCss);
		conditionsMap.put("sysOfficeSort", sysOfficeSort);
		conditionsMap.put("sysOfficeCss", sysOfficeCss);
		
		Page<Map<String,Object>> page = sysModuleService.querySysModuleConfigList(request,response,conditionsMap,sysModule,companyId);
		List<Office> officelist = officeService.findAll();
		
		model.addAttribute("fmodulelist", fmodulelist);
		model.addAttribute("modulelist", modulelist);
		model.addAttribute("fmoduleId",fmoduleId);
		model.addAttribute("moduleId", moduleId);
		model.addAttribute("companyId", companyId);
		model.addAttribute("officelist", officelist);
		model.addAttribute("pageName", pageName);
		model.addAttribute("path", path);
		model.addAttribute("page", page);
		model.addAttribute("conditionsMap", conditionsMap);
		
		return "modules/sysmoduleconfig/sysModuleConfigListQuery";
	}
	
	
	/**
	 * 获取父模块的list，list中放的是<fid,fname>,<fid,fname>
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	public List getFatherModulesListFromJson(){
		List list = new ArrayList();
		list = (List)readTxtFile().get("flist");
		return list;
	}
	/**
	 * 获取父模块的Map,<key,value>=<fid,fname>
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	public Map getFatherModulesMapFromJson(){
		Map fmap = new LinkedHashMap();
		fmap = (Map)readTxtFile().get("fmap");
		return fmap;
	}
		
	/**
	 * 根据fid获取子模块的id和name的list
	 * @param fmoduleId
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	public List getSonModulesListByFid(String fmoduleId) {
		List modulelist = new ArrayList();
		Map map = (Map)readTxtFile().get("slist");
		modulelist = (List)map.get(fmoduleId);
		return modulelist;
	}
	/**
	 * 根据fid获取子模块的id和name的map
	 * @param fmoduleId
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	public Map getSonModulesMapByFid(String fmoduleId) {
		Map modulemap = new LinkedHashMap();
		Map map = (Map)readTxtFile().get("smap");
		modulemap = (Map)map.get(fmoduleId);
		return modulemap;
	}
	/**
	 * Ajax 根据父模块的id 获取对应的子模块list
	 * @param fmoduleId
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({"rawtypes"})
	@ResponseBody
	@RequestMapping(value = "getSonModulesFromJson")
	public List getSonModulesFromJson(@RequestParam(value="fmoduleId", required=true) String fmoduleId,HttpServletResponse response) throws IOException {
		List modulelist = new ArrayList();
		Map map = (Map)readTxtFile().get("slist");
		modulelist = (List)map.get(fmoduleId);
		return modulelist;
	}
	
	/**
	 * 读取文件,文件中只能存一行，否则得出的效果就不是想要的结果
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map readTxtFile() {
		String realpath = this.getClass().getClassLoader().getResource("").getPath();
		log.info("获取数据库的文件的绝对路径： " + realpath);
		
		String filename =realpath + Global.getConfig("jsontxt");//获取文件路径
		
		log.info("后台管理中自由配置的文件的路径为:  " + filename);
		BufferedReader br = null;
		InputStreamReader reader = null;
		FileInputStream fis = null;
		Map map = new HashMap();
		try {
			fis =new FileInputStream(new File(filename));
			reader = new InputStreamReader(fis,"utf-8");
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)),"utf-8"));
			String str = null;
			
			while ((str = br.readLine()) != null) {
				log.info("json字符串:   " + str);
				map = dealwithJsonArray(str);
			}
			closeInputStream(fis,reader,br);
			
		} catch (FileNotFoundException e) {
			log.info("后台管理中自由配置的文件的路径:  " + filename + "  不存在!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			closeInputStream(fis,reader,br);
		}
		return map;
		
	}
	
	/**
	 * 关闭数据流
	 * @param fis
	 * @param reader
	 * @param br
	 */
	public void closeInputStream(FileInputStream fis, InputStreamReader reader, BufferedReader br){
		if(reader!=null){
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(fis!=null){
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		if(br!=null){
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 处理json字符串,将返回一个子模块的Map，map中存放的是模块和子模块的map,子模块的map中存放的是父模块的id和list，list中存放的是子模块的id和list
	 * dealwithJsonArray,将json中的数据解析成fmap，flist ，smap和slist四部分
	 * (注意事项：这里描述这个方法适用条件 – 可选)
	 * @param jsonstr
	 * @return map  fmap 
	 * @exception
	 * @since  1.0.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map dealwithJsonArray(String jsonstr) {
		Map map = new HashMap();
		
		JSONArray jsonArray = JSONArray.fromObject(jsonstr);
		int jsonLength = jsonArray.size();
		int modulelength = 0;
		// 对json数组进行循环
		Map fmap = new LinkedHashMap(); //存放的是所有的fmap
		List flist = new ArrayList();
		Map slist = new LinkedHashMap();//对应于每个父模块下的id下的所有的子模块的每一个list放入到map中
		List stemplist = new ArrayList();//每一个父模块下的id对应下的所有子模块放入到该list中，最后都放入到slist中
		Map smap = new LinkedHashMap();//对应于父模块下的id下的所有的子模块的map放入到一个map中
		Map stempmap = new LinkedHashMap(); //每一个父模块下的id对应下的所有子模块id,name放入到该map中，最后都放入到smap中
		
		//Map templist = new HashMap();//对应于父模块下的id下的所有的子模块的list放入到一个map中
		List temp = new ArrayList();
		
		for (int i = 0; i < jsonLength; i++) {
			stemplist = new ArrayList();//对应于每个父模块下的id下的所有的子模块，所以不同模块的父id都要初始化一次
			stempmap = new LinkedHashMap();//对应于每个父模块下的id下的所有的子模块，所以不同模块的父id都要初始化一次
			temp = new ArrayList();
			
			JSONObject tempJson = JSONObject.fromObject(jsonArray.get(i));
//			System.out.println("父模块:" + tempJson);
			String fid = StringEscapeUtils.escapeSql(tempJson.getString("fid"));
			String fname = StringEscapeUtils.escapeSql(tempJson.getString("fname"));
			temp.add(fid);
			temp.add(fname);
			
			fmap.put(fid,fname);
			flist.add(temp);
			
			JSONArray modules = tempJson.getJSONArray("modules");//获取改JsonObject的字数组
			modulelength = modules.size();
			for(int j = 0;j<modulelength;j++){
				//tempmap = new HashMap();
				temp = new ArrayList();
				JSONObject sonjson = JSONObject.fromObject(modules.get(j));
				String id = StringEscapeUtils.escapeSql(sonjson.getString("id"));
				String name = StringEscapeUtils.escapeSql(sonjson.getString("name"));
				temp.add(id);
				temp.add(name);
				
				stemplist.add(temp);
				stempmap.put(id,name);
			}
			slist.put(fid,stemplist);
			smap.put(fid, stempmap);
			log.info("子模块的fid: " +fid + "对应的 map值： "  + stempmap + "对应的list值： " + stemplist);
		}
		map.put("fmap", fmap);
		map.put("flist", flist);
		map.put("smap", smap);
		map.put("slist", slist);
		
		return map;
	}
	
}
