package com.trekiz.admin.modules.eprice.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceFile;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecordReply;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficRequirements;
import com.trekiz.admin.modules.eprice.entity.EstimatePricerReply;
import com.trekiz.admin.modules.eprice.entity.Page;
import com.trekiz.admin.modules.eprice.form.ListSearchForm;
import com.trekiz.admin.modules.eprice.form.ProjectFirstForm;
import com.trekiz.admin.modules.eprice.form.ProjectSecondForManagerForm;
import com.trekiz.admin.modules.eprice.form.ProjectSecondForm;
import com.trekiz.admin.modules.eprice.form.ProjectThirdForManagerForm;
import com.trekiz.admin.modules.eprice.form.ProjectThirdForm;
import com.trekiz.admin.modules.eprice.form.RecordPriceForm;
import com.trekiz.admin.modules.eprice.form.ReplyEPrice4TrafficForm;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceProjectDao;
import com.trekiz.admin.modules.eprice.repository.EstimatePricerReplyDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceFileService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceProjectService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordReplyService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordService;
import com.trekiz.admin.modules.eprice.service.EstimatePriceTrafficRequirementsService;
import com.trekiz.admin.modules.eprice.service.EstimatePricerReplyService;
import com.trekiz.admin.modules.eprice.utils.EstimatePriceUtils;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/eprice/manager/ajax/project")
public class EstimatePriceProjectAjaxController {
	private static final Log log = LogFactory.getLog(EstimatePriceProjectAjaxController.class);
	
	@Resource
	private EstimatePriceProjectService estimatePriceProjectService;
	
	@Resource
	private EstimatePriceRecordService estimatePriceRecordService;
	
	@Resource
	private EstimatePriceFileService estimatePriceFileService;
	
	@Resource
    private DepartmentService departmentService;
	
	@Resource
	private EstimatePricerReplyService estimatePricerReplyService;
	
	@Resource
	private EstimatePriceRecordReplyService estimatePriceRecordReplyService;
	
	@Resource
	private EstimatePriceTrafficRequirementsService estimatePriceTrafficRequirementsService;
	
	@Autowired
	private EstimatePricerReplyDao estimatePricerReplyDao;
	
	@Autowired
	private EstimatePriceProjectDao estimatePriceProjectDao;
	
	@Autowired
	protected LogOperateService logOpeService;
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 210;
	}
	@ResponseBody
	@RequestMapping(value="trafficOneSave", method=RequestMethod.POST)
	public Map<String,Object> trafficOneSave(@Valid ProjectFirstForm pff,BindingResult result){ 
		Map<String,Object> map = new HashMap<String,Object>();
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		}else{//检查是否有基础数据业务错误
			String msg =  pff.check();
			if(!StringUtils.isBlank(msg)){
				map.put("res", msg);
				return map;		
			}
		}
		
		map = estimatePriceProjectService.addProjectFirst(pff, UserUtils.getUser());
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="trafficTwoSave", method=RequestMethod.POST)
	public Map<String,Object> trafficTwoSave(@Valid ProjectThirdForm ptf,BindingResult result){
		Map<String,Object> map = new HashMap<String,Object>();
		
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			log.info("valid:data_error");
			return map;
		}else {
			String msg = ptf.check();
			if(!StringUtils.isBlank(msg)){//检查是否有基础数据业务错误
				map.put("res", msg);
				log.info("数据校验:"+msg);
				return map;			
			}
		}
		
		map = estimatePriceProjectService.addProjectThird(ptf, UserUtils.getUser());
		log.info(String.valueOf(map.get("res")));
		return map;
	}
	/**
	 * add 20150906 新增计调主管分配计调
	 * @param ptf
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="trafficTwoForManagerSave", method=RequestMethod.POST)
	public Map<String,Object> trafficTwoForManagerSave(@Valid ProjectThirdForManagerForm ptf,BindingResult result){
		Map<String,Object> map = new HashMap<String,Object>();
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			log.info("valid:data_error");
			return map;
		}else {
			String msg = ptf.check();
			if(!StringUtils.isBlank(msg)){//检查是否有基础数据业务错误
				map.put("res", msg);
				log.info("数据校验:"+msg);
				return map;			
			}
		}
		
		map = estimatePriceProjectService.addProjectForManagerThird(ptf, UserUtils.getUser());
		log.info(String.valueOf(map.get("res")));
		return map;
	}
	
	/**
	 * 异步提交创建询价，提交询价基本信息表单数据请求方法
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 */
	//@RequiresPermissions("eprice:project:save")
	@ResponseBody
	@RequestMapping(value="onesave", method=RequestMethod.POST)
	public Map<String,Object> onesave(@Valid ProjectFirstForm pff,BindingResult result){
		Map<String,Object> map = new HashMap<String,Object>();
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		}else{
			String msg = pff.check();
			if(!StringUtils.isBlank(msg)){
				map.put("res", msg);
				return map;			
			}
		}
		
		map = estimatePriceProjectService.addProjectFirst(pff, UserUtils.getUser());
		return map;
	}
	/**
	 * 异步提交创建询价，提交询价基本信息表单数据请求方法
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 */
	//@RequiresPermissions("eprice:project:save")
	@ResponseBody
	@RequestMapping(value="twosave", method=RequestMethod.POST)
	public Map<String,Object> twosave(@Valid ProjectSecondForm psf,BindingResult result){
		Map<String,Object> map = new HashMap<String,Object>();
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		}
		String msg = psf.check();
		if(!StringUtils.isBlank(msg)){//检查是否有基础数据业务错误
			map.put("res", msg);
			return map;			
		}
		
		
		map = estimatePriceProjectService.addProjectSecond(psf, UserUtils.getUser());
		return map;
	}
	/**
	 * 异步提交创建询价，提交询价基本信息表单数据请求方法
	 * add 20150906 增加计调主管分配计调
	 * @param psf
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="twosaveformanager", method=RequestMethod.POST)
	public Map<String,Object> twosaveformanager(@Valid ProjectSecondForManagerForm psf,BindingResult result){
		Map<String,Object> map = new HashMap<String,Object>();
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		}
		String msg = psf.check();
		if(!StringUtils.isBlank(msg)){//检查是否有基础数据业务错误
			map.put("res", msg);
			return map;			
		}
		map = estimatePriceProjectService.addProjectForManagerSecond(psf, UserUtils.getUser());
		return map;
	}
	
	
	/**
	 * 异步提交创建询价，提交询价基本信息表单数据请求方法
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 */
	//@RequiresPermissions("eprice:project:save")
	@ResponseBody
	@RequestMapping(value="treesave", method=RequestMethod.POST)
	public Map<String,Object> treesave(@Valid ProjectThirdForm ptf,BindingResult result){
		Map<String,Object> map = new HashMap<String,Object>();
		//检查@Valid的验证结果：是否存在验证不通过的项
//		if(result.hasErrors()){
//			map.put("res", "data_error");
//			map.put("mes", result.getAllErrors());
//			return map;
//		}else 
		String msg = ptf.check();
		if(!StringUtils.isBlank(msg)){//检查是否有基础数据业务错误
			map.put("res", msg);
			return map;			
		}
		
		map = estimatePriceProjectService.addProjectThird(ptf, UserUtils.getUser());
		/*if("success".equals(map.get("res"))){
			estimatePriceProjectService.update((EstimatePriceProject)map.get("model"));
		}*/
		
		return map;
	}
	
	/**
	 * 异步提交创建询价，提交询价基本信息表单数据请求方法
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 */
	//@RequiresPermissions("eprice:project:save")
	@ResponseBody
	@RequestMapping(value="treeformanagersave", method=RequestMethod.POST)
	public Map<String,Object> treeformanagersave(@Valid ProjectThirdForManagerForm ptf,BindingResult result){
		Map<String,Object> map = new HashMap<String,Object>();
		
		String msg = ptf.check();
		if(!StringUtils.isBlank(msg)){//检查是否有基础数据业务错误
			map.put("res", msg);
			return map;			
		}
		
		map = estimatePriceProjectService.addProjectForManagerThird(ptf, UserUtils.getUser());
		return map;
	}
	
	/**
	 * 异步提交查询询价项目列表，多条件可排序多条件查询分页询价项目列表
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="plist", method=RequestMethod.POST)
	public Map<String,Object> projectList(@Valid ListSearchForm lsf,BindingResult result,HttpServletRequest req,Model model){
		//System.out.println("------ajax---projectList---");
		//System.out.println("begin:"+lsf.getEpriceStartDate()+"----=-----=---end:"+lsf.getEpriceEndDate());
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("inquiry", model);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		}else if(!lsf.check()){//检查是否有基础数据业务错误
			map.put("res", "data_error");
			return map;			
		}
		
		
		String pnstr = req.getParameter("pageNo");
		String psstr = req.getParameter("pageSize");
		Integer pageNo = pnstr!=null?Integer.valueOf(pnstr):1;
		Integer pageSize = psstr!=null?Integer.valueOf(psstr):20;
//		Long salerId = UserUtils.getUser().getId();
//		lsf.setSalerId(salerId);
//		
		// 查询全部单团项目询价
		Page<EstimatePriceProject> page = estimatePriceProjectService.findListByPage(lsf, pageSize, pageNo, common);
		map.put("page", page);
		
		return map;
	}
	
	/**
	 * 计调主管询价项目
	 * @param lsf
	 * @param result
	 * @param req
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="plist4manager", method=RequestMethod.POST)
	public Map<String,Object> plist4manager(@Valid ListSearchForm lsf,BindingResult result,HttpServletRequest req,Model model) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		//检查@Valid的验证结果：是否存在验证不通过的项
		if (result.hasErrors()) {
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		} else if (!lsf.check()) {//检查是否有基础数据业务错误
			map.put("res", "data_error");
			return map;			
		}
		
		
		String pnstr = req.getParameter("pageNo");
		String psstr = req.getParameter("pageSize");
		Integer pageNo = pnstr!=null?Integer.valueOf(pnstr):1;
		Integer pageSize = psstr!=null?Integer.valueOf(psstr):20;
		// 查询计调主管全部单团项目询价
		Page<EstimatePriceProject> page = estimatePriceProjectService.findListByPageForManager(lsf, pageSize, pageNo);
		//Page<EstimatePriceProject> page = estimatePriceProjectService.findListByPage(lsf, pageSize, pageNo, common);
		map.put("page", page);
		
		return map;
	}
	
	/**
	 * 异步获取计调报价记录列表
	 * @author jianning.gao
	 * @时间 2014年12月17日
	 * @param pff
	 * @param result
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value="rlist/{pid}")
	public Map<String,Object> eprecordList(@PathVariable("pid") Long pid,HttpServletRequest req){
    	//System.out.println("------ajaxcontroller-0---=------eprecordList");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		String[] orderColumn = req.getParameterValues("orderColumn");
		
		if(orderColumn!=null && orderColumn.length!=0){
			
			String[] t;
			
			for(int i=0,len=orderColumn.length;i<len;i++){
				try {
					t = orderColumn[i].split("-");
					map.put(t[0], Integer.valueOf(t[1]));
				} catch (NumberFormatException e) {
					continue;
				}
				
			}
		}
		// 查询全部询价项
		List<EstimatePriceRecordReply> recRepList =  estimatePriceRecordReplyService.findByOpeId(UserUtils.getUser().getId(), pid);
		// 该处分页未完成，因计调询价项暂时无分页，日后若增加分页，这里需要补全
		Page<EstimatePriceRecordReply> page = new Page<EstimatePriceRecordReply>(1,100,100);
		page.setResult(recRepList);
		EstimatePriceProject project = estimatePriceProjectDao.findById(pid);
		map.put("project", project);
		map.put("page", page);
		return map;
	}
	/**
	 * 异步获取询价记录列表
	 * @param pid
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="recordlist/{pid}")
	public Map<String,Object> epprecordList(@PathVariable("pid") Long pid,HttpServletRequest req){
		//System.out.println("------ajax---epprecordList---");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		String pnstr = req.getParameter("pageNo");
		String psstr = req.getParameter("pageSize");
		Integer pageNo = pnstr!=null?Integer.valueOf(pnstr):1;
		Integer pageSize = psstr!=null?Integer.valueOf(psstr):10;
		
		String[] orderColumn = req.getParameterValues("orderColumn");
		
		Map<String,Integer> sort = new HashMap<String,Integer>();
		if(orderColumn!=null && orderColumn.length!=0){
			
			String[] t;
			
			for(int i=0,len=orderColumn.length;i<len;i++){
				try {
					t = orderColumn[i].split("-");
					sort.put(t[0], Integer.valueOf(t[1]));
				} catch (NumberFormatException e) {
					continue;
				}
				
			}
		}
		EstimatePriceProject project = 	estimatePriceProjectDao.findById(pid);
		// 查询全部单团询价项
		Page<EstimatePriceRecord> page =  estimatePriceRecordService.findByPid(pid, 0, pageSize, pageNo, sort, false);
		//行程附件
		List<String> xcFilesIds = estimatePriceRecordService.getXcFilesIdsByResultPage(page);
		//报价附件
		List<String> bjFilesIds = estimatePriceRecordService.getbjFilesIdsByResultPage(page);
		map.put("page", page);
		map.put("xcFilesIds", xcFilesIds);
		map.put("bjFilesIds", bjFilesIds);
		map.put("project", project);
		
		// 判断当前用户是不是创建询价的用户，如果不是，仅允许其查看详情
		User user  = UserUtils.getUser();
		Long loadingUserId = user.getId();
		Long createUserId = project.getUserId();
		if(createUserId.equals(loadingUserId)){
			map.put("createUser", "1");
		}else{
			map.put("createUser", "");
		}
		return map;
	}
	
	
	/**
	 * 异步获取询价记录列表（计调主管）
	 * @param pid
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="recordlistForManager/{pid}")
	public Map<String,Object> recordlistForManager(@PathVariable("pid") Long pid,HttpServletRequest req){
		//System.out.println("------ajax---recordlistForManager---");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		String pnstr = req.getParameter("pageNo");
		String psstr = req.getParameter("pageSize");
		Integer pageNo = pnstr!=null?Integer.valueOf(pnstr):1;
		Integer pageSize = psstr!=null?Integer.valueOf(psstr):10;
		
		String[] orderColumn = req.getParameterValues("orderColumn");
		
		Map<String,Integer> sort = new HashMap<String,Integer>();
		if(orderColumn!=null && orderColumn.length!=0){
			
			String[] t;
			
			for(int i=0,len=orderColumn.length;i<len;i++){
				try {
					t = orderColumn[i].split("-");
					sort.put(t[0], Integer.valueOf(t[1]));
				} catch (NumberFormatException e) {
					continue;
				}
				
			}
		}
		EstimatePriceProject project = 	estimatePriceProjectDao.findById(pid);
		// 查询全部单团询价项
		Page<EstimatePriceRecord> page =  estimatePriceRecordService.findByPid(pid,0, pageSize, pageNo, sort, true);
		//行程附件
		List<String> xcFilesIds = estimatePriceRecordService.getXcFilesIdsByResultPage(page);
		//报价附件
		List<String> bjFilesIds = estimatePriceRecordService.getbjFilesIdsByResultPage(page);
		map.put("page", page);
		map.put("xcFilesIds", xcFilesIds);
		map.put("bjFilesIds", bjFilesIds);
		map.put("project", project);
		
		// 判断当前用户是不是创建询价的用户，如果不是，仅允许其查看详情
		User user  = UserUtils.getUser();
		Long loadingUserId = user.getId();
		Long createUserId = project.getUserId();
		if(createUserId.equals(loadingUserId)){
			map.put("createUser", "1");
		}else{
			map.put("createUser", "");
		}
		return map;
	}
	
	
	/**
	 * 异步提交查询询价项目列表，多条件可排序多条件查询分页询价项目列表
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="plistback", method=RequestMethod.POST)
	public Map<String,Object> projectListback(@Valid ListSearchForm lsf,BindingResult result,HttpServletRequest req,Model model){
		//按部门展示
		//DepartmentCommon common = departmentService.setDepartmentPara(lsf.getType().toString(), model, user);
		DepartmentCommon common = departmentService.setDepartmentPara("inquiry", model);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		}else if(!lsf.check()){//检查是否有基础数据业务错误
			map.put("res", "data_error");
			return map;			
		}
		
		
		String pnstr = req.getParameter("pageNo");
		String psstr = req.getParameter("pageSize");
		Integer pageNo = pnstr!=null?Integer.valueOf(pnstr):1;
		Integer pageSize = psstr!=null?Integer.valueOf(psstr):20;
		Long operatorId = UserUtils.getUser().getId();
		lsf.setOperatorUid(operatorId);
		
		Page<EstimatePriceProject> page = estimatePriceProjectService.findByPage(lsf, pageSize, pageNo, common);
		map.put("page", page);
		
		return map;
	}
	
	
	@ResponseBody
	@RequestMapping(value="plisttrafficback", method=RequestMethod.POST)
	public Map<String,Object> projectListTrafficback(@Valid ListSearchForm lsf,BindingResult result,HttpServletRequest req,Model model){
		//System.out.println("------ajax---projectListTrafficback---");
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara(lsf.getType().toString(), model);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		}else if(!lsf.check()){//检查是否有基础数据业务错误
			map.put("res", "data_error");
			return map;			
		}
		
		
		String pnstr = req.getParameter("pageNo");
		String psstr = req.getParameter("pageSize");
		Integer pageNo = pnstr!=null?Integer.valueOf(pnstr):1;
		Integer pageSize = psstr!=null?Integer.valueOf(psstr):20;
		Long operatorId = UserUtils.getUser().getId();
		lsf.setOperatorUid(operatorId);
		//lsf.setType(2);// 改为机票询价
		Page<EstimatePriceProject> page = estimatePriceProjectService.findByPage(lsf, pageSize, pageNo, common);
		map.put("page", page);
		map.put("page", page);
		
		return map;
	}
	
	/**
	 * 异步提交获取指定询价项目下属的机票计调询价记录
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value="rtrafficlist/{pid}")
	public Map<String,Object> eprecordTrafficList(@PathVariable("pid") Long pid,HttpServletRequest req){
    	Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		
		
		String[] orderColumn = req.getParameterValues("orderColumn");
		
		if(orderColumn!=null && orderColumn.length!=0){
			
			String[] t;
			
			for(int i=0,len=orderColumn.length;i<len;i++){
				try {
					t = orderColumn[i].split("-");
					map.put(t[0], Integer.valueOf(t[1]));
				} catch (NumberFormatException e) {
					continue;
				}
				
			}
		}
		// 查询全部机票询价项（包含单团中的机票）
		//Page<EstimatePriceRecord> page =  estimatePriceRecordService.findByPid(pid,EstimatePriceRecord.TYPE_FLIGHT, pageSize, pageNo, sort);
		List<EstimatePriceRecordReply> recRepList =  estimatePriceRecordReplyService.findTrafficByOpeId(UserUtils.getUser().getId(), pid);
		// 该处分页未完成，因计调询价项暂时无分页，日后若增加分页，这里需要补全
		Page<EstimatePriceRecordReply> page = new Page<EstimatePriceRecordReply>(1,100,100);
		EstimatePriceProject project = estimatePriceProjectDao.findById(pid);
		page.setResult(recRepList);
		map.put("page", page);
		map.put("project", project);
		return map;
	}
	
	/**
	 * 接待回复提交 （出现多币种报价，于 20150511 废弃）
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 
	//@RequiresPermissions("eprice:reply:admit")
	@ResponseBody
	@RequestMapping(value="reply4admit", method=RequestMethod.POST)
	public Map<String,Object> reply4admit(@Valid ReplyEPrice4AdmitForm reaf,BindingResult result,HttpServletRequest req){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		}else if(!reaf.check()){//检查是否有基础数据业务错误
			map.put("res", "data_error");
			return map;			
		}
		
		map = estimatePriceRecordService.reply4admit(reaf , UserUtils.getUser());
		
		return map;
	}*/
	
	
	/**
	 * 异步提交创建询价，提交询价基本信息表单数据请求方法
	 * @author lihua.xu
	 * @时间 2014年9月18日
	 * @param pff
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/file/upload", method=RequestMethod.POST)
	public Map<String,Object> fileSave(@RequestParam(value = "file") MultipartFile file,HttpServletRequest req){
		Map<String,Object> map = new HashMap<String,Object>();
		String idstr = req.getParameter("salerTripFileId");
		Long id = null;
		if(idstr != null){
			id = Long.valueOf(idstr);	
		}
		map = estimatePriceFileService.saveTemp(file, id,UserUtils.getUser());
		return map;
		
	}
	
	@RequestMapping("uploadFilesPage")
	public String getUploadFilesPage() {
		return "/include/mulUploadFile";
//		System.out.println("here");
//		return "/modules/eprice/mulUploadFile";
	}
	
	/**
	 * 异步下载指定文件。
	 * @param docid
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value="/file/download/{docid}")
	public Map<String,Object> fileDownload(@PathVariable("docid") Long docid,HttpServletResponse response) throws IOException{
		File downFile;
		OutputStream out = null;
		if(docid!=null && docid>0){
			
		}else{
			return null;
		}
		try{
			EstimatePriceFile estPrice = estimatePriceFileService.findById(docid);
			if(estPrice!=null){
				downFile = new File(Global.getBasePath()+File.separator+estPrice.getPath());
				if(downFile.exists()){
					response.reset();
    				response.setHeader("Content-Disposition", "attachment; filename="+new String(estPrice.getFileName().getBytes("gb2312"), "ISO-8859-1"));
    				response.setContentType("application/octet-stream; charset=utf-8");
    				out = response.getOutputStream();
    				out.write(FileUtils.readFileToByteArray(downFile));
    				out.flush();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 异步提交确定计调报价
	 * @param form
	 * @param result
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="replyPrice", method=RequestMethod.POST)
	public Map<String,Object> replyPrice(@Valid RecordPriceForm form,BindingResult result,HttpServletRequest req){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		//检查@Valid的验证结果：是否存在验证不通过的项
		String msg = form.check();
		if(!StringUtils.isBlank(msg)){
			map.put("res", msg);
			return map;	
		}else if(result.hasErrors()){
			map.put("res", "data_error");
			map.put("mes", result.getAllErrors());
			return map;
		} 
		EstimatePriceRecord record = new EstimatePriceRecord();
		record = estimatePriceRecordService.findById(form.getPrid());
		if(record!=null){ 
			record.setEstimateStatus(form.getStatus());
			//record.setOperatorPrice(form.getOperatorPrice());
			record.setOutPrice(form.getOutPrice());
			record.setOutPriceTime(new Date());
			if(form.getAcceptAopId()!=null){
				EstimatePricerReply acceptAoperatorReply = estimatePricerReplyDao.findById((long)form.getAcceptAopId());
				if(acceptAoperatorReply!=null){
					acceptAoperatorReply.setStatus(EstimatePricerReply.STATUS_ADOPTED);
					estimatePricerReplyDao.getSession().update(acceptAoperatorReply);				
				}
				record.setAcceptAoperatorReply(acceptAoperatorReply);
			}
			if(form.getAcceptTopId()!=null){
				EstimatePricerReply acceptToperatorReply = estimatePricerReplyDao.findById((long)form.getAcceptTopId());
				if(acceptToperatorReply!=null){
					acceptToperatorReply.setStatus(EstimatePricerReply.STATUS_ADOPTED);
					estimatePricerReplyDao.getSession().update(acceptToperatorReply);				
				}
				record.setAcceptToperatorReply(acceptToperatorReply);
			}
			
			estimatePriceRecordService.save(record);
		}
		// 将产品项目设为"已确定报价"
		if(record!=null && record.getPid()!=null){
			EstimatePriceProject pro = estimatePriceProjectService.findById(record.getPid());
			if(pro!=null&&EstimatePriceProject.ESTIMATE_STATUS_SURE>pro.getEstimateStatus()){
				
				pro.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_SURE);
				estimatePriceProjectService.save(pro);
			}
		}
		return map;
	}
	/**
	 * 机票计调回复询价
	 * @param form
	 * @param result
	 * @param req
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value="replytrafficprice", method=RequestMethod.POST)
	public Map<String,Object> replyTrafficPrice(@Valid ReplyEPrice4TrafficForm form,BindingResult result,HttpServletRequest req){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("res", "success");
		
		//检查@Valid的验证结果：是否存在验证不通过的项
		if(result.hasErrors()){
			map.put("res", "data_error");
			if(StringUtils.isNotBlank(form.getRemark()) && form.getRemark().length()>500){
				map.put("mes", "备注长度不要超过500字");
			}else{
				map.put("mes", "请检查数据");
			}
			return map;
		}
//		else if(!form.check()){//检查是否有基础数据业务错误
//			map.put("res", "data_error");
//			map.put("mes", result.getAllErrors());
//			return map;			
//		}
		
		// 获取对应的计调回复项
		EstimatePricerReply epr = estimatePricerReplyService.findById(form.getRpid());
		
		if(epr!=null){ 
			// 获得询价记录类
			EstimatePriceRecord eprecord = estimatePriceRecordService.findById(epr.getRid());
			if(eprecord==null){
				map.put("res", "EstimatePriceRecord_is_null");
				map.put("mes", result.getAllErrors());
				return map;
			}
			// 获得机票询价详情类
			EstimatePriceTrafficRequirements require = eprecord.getTrafficRequirements();
			// 获得询价项目类
			EstimatePriceProject epp =  estimatePriceProjectService.findById(epr.getPid());
			
			if(require==null){
				map.put("res", "EstimatePriceTrafficRequirements_is_null");
				map.put("mes", result.getAllErrors());
				return map;
			}
			if(epp==null){
				map.put("res", "EstimatePriceProject_is_null");
				map.put("mes", result.getAllErrors());
				return map;
			}
			epr.setAdultPrice(form.getAdultPrice());
			epr.setChildPrice(form.getChildPrice());
			epr.setSpecialPersonPrice(form.getSpecialPersonPrice());
			epr.setAdultSum(require.getAdultSum());
			epr.setChildSum(require.getChildSum());
			epr.setSpecialPersonSum(require.getSpecialPersonSum());
			epr.setRemark(form.getRemark());
			JSONObject json = new JSONObject();
			JSONObject jsonAdult = new JSONObject();
			jsonAdult.put("price", form.getAdultPrice());
			jsonAdult.put("sum", form.getAdultSum());
			json.put("adult", jsonAdult);
			JSONObject jsonChild = new JSONObject();
			jsonChild.put("price", form.getChildPrice());
			jsonChild.put("sum", form.getChildSum());
			json.put("child", jsonChild);
			JSONObject jsonSpecial = new JSONObject();
			jsonSpecial.put("price", form.getSpecialPersonPrice());
			jsonSpecial.put("sum", form.getSpecialPersonSum());
			json.put("specialPerson", jsonSpecial);
			
			epr.setPriceDetail(json.toString());
			// 计算总值
			BigDecimal allCount = epr.getAdultPrice().multiply(new BigDecimal(require.getAdultSum()));
			allCount = allCount.add(epr.getChildPrice().multiply(new BigDecimal(require.getChildSum())));
			allCount = allCount.add(epr.getSpecialPersonPrice().multiply(new BigDecimal(require.getSpecialPersonSum())));
			
			
			
			epr.setOperatorTotalPrice(allCount);
			epr.setOperatorPriceTime(new Date());
			epr.setStatus(EstimatePricerReply.STATUS_REPLYED);
			estimatePricerReplyService.save(epr);
			// 询价记录改为已报价
			eprecord.setEstimateStatus(EstimatePriceRecord.ESTIMATE_STATUS_GIVEN);
			eprecord.setLastToperatorPriceTime(new Date());
			estimatePriceRecordService.save(eprecord);
			// 询价项目改为已报价
			
			
			if(EstimatePriceProject.ESTIMATE_STATUS_WAITING==epp.getEstimateStatus()
					&& EstimatePriceProject.TYPE_FLIGHT!=epp.getType()){
				List<EstimatePricerReply> list = estimatePricerReplyDao.find(" from EstimatePricerReply where pid = '"+epp.getId()+
						"' and rid = "+eprecord.getId()+" and type !='"+EstimatePricerReply.TYPE_FLIGHT+"' and status >1");
				if(list!=null&&list.size()>0){
					if(EstimatePriceProject.ESTIMATE_STATUS_GIVEN>epp.getEstimateStatus()){
						epp.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_GIVEN);// '询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品',
					}
				}
			}else{
				if(EstimatePriceProject.ESTIMATE_STATUS_GIVEN>epp.getEstimateStatus()){
					epp.setEstimateStatus(EstimatePriceProject.ESTIMATE_STATUS_GIVEN);// '询价项目状态:0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品',
				}
			}
			epp.setLastOperatorGivenTime(new Date());
			estimatePriceProjectService.save(epp);

			//询价记录日志
			StringBuffer content =new StringBuffer("机票计调:(ID "+UserUtils.getUser().getId()+")"+UserUtils.getUser().getName());
			content.append(", 回复销售员：(ID "+epp.getUserId()+")"+epp.getUserName());
			//content.append("项目编号(estimate_eprice_record.id)：" +record.getId());
			content.append(", 询价客户为 "+epp.getLastBaseInfo().getCustomerName()+" 的"+EstimatePriceUtils.backType(epp.getType())+"询价。");
			content.append(" 客户预算："+EstimatePriceUtils.backBudgetType(epp.getLastBaseInfo().getBudgetType())+", ");
			content.append("预算金额："+epp.getLastBaseInfo().getBudget()+"， ");
			content.append("预算币种：人民币， ");
			content.append("备注："+epp.getLastBaseInfo().getBudgetRemark()+"， ");
			content.append("申请总人数："+epp.getLastBaseInfo().getAllPersonSum()+"， ");
			content.append("申请成人数："+epp.getLastBaseInfo().getAdultSum()+"， ");
			content.append("申请儿童数："+epp.getLastBaseInfo().getChildSum()+"， ");
			content.append("申请特殊人群数："+epp.getLastBaseInfo().getSpecialPersonSum()+"， ");
			content.append("特殊人群备注："+epp.getLastBaseInfo().getSpecialRemark()+"。 ");		
			content.append(" 操作人："+UserUtils.getUser().getName());
			content.append(", 操作时间："+ new Date());
			
			logOpeService.saveLogOperate(Context.log_type_price, Context.log_type_price_name,
					content.toString(), Context.log_state_add, null, null);
		}
		
		return map;
	}
	
	/**
	 * 异步提交修改外报价
	 * @author yue.wang
	 * @时间 2014年12月12日
	 * @param result
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @ResponseBody
	@RequestMapping(value="changeEpriceOutPrice", method=RequestMethod.POST)
	public Map<String,Object> projectList(HttpServletRequest req,Model model){
		Map resultMap = new HashMap();
		Map conditionMap = new HashMap();
		String recordId = req.getParameter("recordId");
		String outPrice = req.getParameter("outPrice");
		if(StringUtils.isBlank(recordId)){
			resultMap.put("res", "找不到recorId");
			return resultMap;
		}
		try{
			if(!StringUtils.isBlank(outPrice)){
				BigDecimal out = new BigDecimal(outPrice);
				if(out.compareTo(new BigDecimal(10000000))>=0){
					resultMap.put("res", "值过大,请重新输入");
					return resultMap;
				}
			}
		}catch(Exception e){
			resultMap.put("res", "金额格式不符，请重新填写");
			return resultMap;
		}
		
		conditionMap.put("recordId", recordId);
		conditionMap.put("outPrice", outPrice);
		resultMap = estimatePriceRecordService.updateEpriceOutPrice(conditionMap);
		return resultMap;
	}
}
