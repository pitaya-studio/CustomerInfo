package com.trekiz.admin.modules.sys.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.quauq.multi.tenant.manage.service.TenantUtil;
import com.quauq.multi.tenant.util.MultiTenantUtil;
import com.trekiz.admin.common.beanvalidator.BeanValidators;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.security.Base64Util;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.utils.excel.ImportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.SysJobNew;
import com.trekiz.admin.modules.sys.entity.SysOfficeProcessType;
import com.trekiz.admin.modules.sys.entity.SysOfficeProductType;
import com.trekiz.admin.modules.sys.entity.SysUserReviewCommonPermission;
import com.trekiz.admin.modules.sys.entity.SysUserReviewProcessPermission;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysOfficeConfigurationService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.configuration.config.ReviewContext;
import com.trekiz.admin.review.configuration.model.DepartmentNode;
import com.trekiz.admin.review.configuration.util.DepartmentUtil;

/**
 * 用户Controller
 * 
 * @author zj
 * @version 2013-11-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
    private SysOfficeConfigurationService officeConfigurationService;
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private AgentinfoService agentInfoService;

	@ModelAttribute
	public User get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return systemService.getUser(id);
		} else {
			return new User();
		}
	}

	@ModelAttribute("menuId")
	protected Integer getMenuId() {
		return 29;
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = { "list", "" })
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		String roleId = request.getParameter("role");
		String deptId = request.getParameter("department");
		String jobId = request.getParameter("job");
		
		
		if (user.getCompany() == null) {
			user.setCompany(officeService.get(StringUtils.toLong(request.getParameter("companyId"))));
		}
		Page<User> page = systemService.findUser(new Page<User>(request, response), user, request);
		//查询角色下拉框值
		List<Role> allRole = new ArrayList<Role>();
		allRole = systemService.findOfficeRole(UserUtils.getUser().getCompany().getId());
		// 除了超级管理员，对于其他用户默认不显示编号1的角色
		if (UserUtils.getUser().getId() != 1 && allRole.size() > 0 && allRole.get(0).getId() == 1) {
			allRole.remove(0);
		}
		//查询职务列表
		List<SysJobNew> jobList = systemService.findByCompanyUuid(UserUtils.getUser().getCompany().getUuid());
			if(CollectionUtils.isNotEmpty(jobList)){
			model.addAttribute("jobList",jobList);
		}
		//查询部门
		List<Department> deptList=departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId());
		
		List<User> list = page.getList();
		//for(User u : list){
		for(int i =0 ;i<list.size();i++){
			User u = list.get(i);
			//查询部门-职务信息
			List<Map<String,Object>> mapList =systemService.getByUserIdAndCompanyUuid(u.getId(),u.getCompany().getUuid());
			for(Map<String,Object> map :mapList){
				map.put("deptName", departmentService.findById(Long.parseLong(map.get("dept_id").toString())).getName());
				map.put("jobName", systemService.findSysJobNewById(Long.parseLong(map.get("job_id").toString())).getName());
			}
			if(CollectionUtils.isNotEmpty(mapList)){
				u.setDeptJobRelation(mapList);
			}
		}
		page.setList(list);
		model.addAttribute("page", page);
		model.addAttribute("allRoles", allRole);
		model.addAttribute("deptList",deptList);
		model.addAttribute("roleId",Integer.parseInt(StringUtils.isBlank(roleId)?"0":roleId));
		model.addAttribute("deptId",Integer.parseInt(StringUtils.isBlank(deptId)?"0":deptId));
		model.addAttribute("jobId",Integer.parseInt(StringUtils.isBlank(jobId)?"0":jobId));
		return "modules/sys/userList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(@RequestParam(value = "id", required = false) String id, User user, Model model) {
		if (user.getCompany() == null || user.getCompany().getId() == null) {
			user.setCompany(UserUtils.getUser().getCompany());
			model.addAttribute("isNewUser","Y");//为新增用户
			// 如果是新增用户，则需要默认生成十一位随机密码（含大小写和数字）
			String chars1 = "abcdefghijklmnopqrstuvwxyz";
			String chars2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String chars3 = "0123456789";
			StringBuffer randomPwd = new StringBuffer("");
			for (int i = 0; i < 4; i++) {
				randomPwd.append(chars1.charAt((int)(Math.random() * 26)));
				randomPwd.append(chars3.charAt((int)(Math.random() * 10)));
				if (i != 3) {
					randomPwd.append(chars2.charAt((int)(Math.random() * 26)));
				}
			}
			model.addAttribute("randomPwd", randomPwd);
		}else{
			model.addAttribute("isNewUser","N");//为修改用户
		}
		List<Role> allRole = new ArrayList<Role>();
		model.addAttribute("user", user);
		if (id == null) {
			allRole = systemService.findOfficeRole(UserUtils.getUser().getCompany().getId());
		} else {
			allRole = systemService.findOfficeRole(systemService.getUser(StringUtils.toLong(id)).getCompany().getId());
		}
		// 除了超级管理员，对于其他用户默认不显示编号1的角色
		if (UserUtils.getUser().getId() != 1 && allRole.size() > 0 && allRole.get(0).getId() == 1) {
			allRole.remove(0);
		}
		
		//查询部门集合
		User user1=UserUtils.getUser();
		List<Department> departments=departmentService.findByOfficeId(user1.getCompany().getId());
		Department root = departmentService.getParent(user1.getCompany());
		if (root != null) {
			DepartmentNode departmentNode=DepartmentUtil.buildDepartmentTree(0L, root.getId(), departments);
			JSONObject jsonObject=JSONObject.fromObject(departmentNode);
			model.addAttribute("departmentJson", jsonObject);
		}
		//查询审批监督弹出框选中的部门id、产品名称id、流程id
		List<Object> deptIdList = systemService.getSelectDeptId(user.getId(), user.getCompany().getUuid());
		List<Object> productIdList = systemService.getSelectProductId(user.getId(), user.getCompany().getUuid());
		List<Object> reviewFlowIdList = systemService.getSelectReviewFlowId(user.getId(), user.getCompany().getUuid());
		String deptIdStr = "";
		for(Object o :deptIdList){
			deptIdStr += o.toString()+",";
		}
		if(CollectionUtils.isNotEmpty(deptIdList)){
			deptIdStr  = deptIdStr.substring(0, deptIdStr.length()-1);
		}
		model.addAttribute("selectIds",deptIdStr);
		model.addAttribute("productIdList",productIdList);
		model.addAttribute("reviewFlowIdList",reviewFlowIdList);
		
		//查询审批模块权限
		List<Map<String,Object>> list = systemService.findReviewLisence(user.getId(), user.getCompany().getUuid());
		if(CollectionUtils.isNotEmpty(list)){
			model.addAttribute("is_jump_task_permit",list.get(0).get("is_jump_task_permit"));
			model.addAttribute("is_applier_auto_approve",list.get(0).get("is_applier_auto_approve"));
		}
		//查询职务列表
		List<SysJobNew> jobList = systemService.findByCompanyUuid(user.getCompany().getUuid());
		if(CollectionUtils.isNotEmpty(jobList)){
			model.addAttribute("jobList",jobList);
		}
		//查询部门-职务记录
		List<Map<String,Object>> deptJobRecord = systemService.getByUserIdAndCompanyUuid(user.getId(), user.getCompany().getUuid());
		for(Map<String,Object> map : deptJobRecord){
			map.put("deptName", departmentService.findById(Long.parseLong(map.get("dept_id").toString())).getName());
			map.put("jobName", systemService.findSysJobNewById(Long.parseLong(map.get("job_id").toString())).getName());
		}
		model.addAttribute("deptJobRecord",deptJobRecord);

		// 查询名片的DocInfo
		if (StringUtils.isNotBlank(user.getCardId())){
			Long cardId = Long.parseLong(user.getCardId());
			DocInfo cardDoc = docInfoService.getDocInfo(cardId);
			user.setCardDocInfo(cardDoc);
		}

		// 查询照片的DocInfo
		if (StringUtils.isNotBlank(user.getPhotoId())){
			Long photoId = Long.parseLong(user.getPhotoId());
			DocInfo photoDoc = docInfoService.getDocInfo(photoId);
			user.setPhotoDocInfo(photoDoc);
		}

		List<SysOfficeProcessType> officeProcessTypes = officeConfigurationService.obtainOfficeProcessTypes(user.getCompany().getUuid());
        List<SysOfficeProductType> officeProductTypes = officeConfigurationService.obtainOfficeProductTypes(user.getCompany().getUuid());
		
		model.addAttribute("productTypeMap", buildProductTypeMap(officeProductTypes));
		model.addAttribute("reviewFlowTypeMap", buildProcessTypeMap(officeProcessTypes));
		model.addAttribute("allRoles", allRole);
		model.addAttribute("deptId",user1.getCompany().getId());
		model.addAttribute("chosedSubstitute", systemService.getChosedSubstitute(user));
		return "modules/sys/userForm";
	}

	@SuppressWarnings("deprecation")
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(User user, String oldLoginName, String oldNo, String newPassword, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes) {
		String saveDepartTree = request.getParameter("saveDepartTree");//弹出框选中的部门id      1,2
		String saveProductType = request.getParameter("saveProductType");//弹出框选中的产品名称id  1,2
		String saveReviewFlowType = request.getParameter("saveReviewFlowType");//弹出框选中的审批流程名称id   1,2
		String lisenceValue = request.getParameter("lisenceValue");//审批模块权限:1表示选中 "本人审批自动越过" 2表示选中"越级审批"  1,2,
		String relationValue = request.getParameter("relationValue");//用户部门-职务对应关系     141-18,33-16
		String quauqBookOrderPermission = request.getParameter("quauqBookOrderPermission");
		String hasPricingSP = request.getParameter("hasPricingStrategyPermission");
		int hasPricingStrategyPermission = 0;
		if(StringUtils.isNotBlank(hasPricingSP)){
			hasPricingStrategyPermission = Integer.parseInt(hasPricingSP);
		}


		//String isSave  = request.getParameter("isSave");//审批监督弹出框是否选中
		
		String[] saveDepartTreeArray = saveDepartTree.split(",");//[""]
		String[] saveProductTypeArray = saveProductType.split(",");
		String[] saveReviewFlowTypeArray = saveReviewFlowType.split(",");
		//数组去重复
		List<String> saveProductTypeList = StringUtils.removeRepeat4Array(saveProductTypeArray);
		List<String> saveReviewFlowTypeList = StringUtils.removeRepeat4Array(saveReviewFlowTypeArray);
		
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		if (UserUtils.getUser().getCompany().getId() != 1) {
			Office office = new Office(UserUtils.getUser().getCompany().getId());
			office.setUuid(UserUtils.getUser().getCompany().getUuid());
			user.setCompany(office);
		} else {
			Office office = officeService.findWholeOfficeById(user.getCompany().getId());
			user.setCompany(office);
		}
		user.setAgentId(StringUtils.toLong(request.getParameter("agent.id")));
		user.setQuauqBookOrderPermission(quauqBookOrderPermission);
		user.setHasPricingStrategyPermission(hasPricingStrategyPermission);
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(newPassword)) {
			user.setPassword(SystemService.entryptPassword(newPassword));
			user.setTwo_psw(Base64Util.getBase64(newPassword));
		}
		
		if (!beanValidator(model, user)) {
			return form(null, user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<Long> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()) {
			if (roleIdList.contains(r.getId())) {
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		Long companyId = user.getCompany().getId();
		String companyUuid = user.getCompany().getUuid();
		// 判断是新增用户还是修改用户
		Long userId = user.getId();
		// 可代替自己下单的人
		String substituteOrder = request.getParameter("substituteOrder");
		user.setSubstituteOrder(substituteOrder);

		// 获取用户上传的名片和个人照片的id
		String cardId = request.getParameter("cardId");
		user.setCardId(cardId);
		String photoId = request.getParameter("photoId");
		user.setPhotoId(photoId);
		
		// 保存用户信息
		systemService.saveUser(user);
		
		/*保存部门-职务关系*/
		//判断是否有记录，若有先删除再保存
		List<Object> object = systemService.check4DeptJobRelation(user.getId(),companyUuid);
		if(CollectionUtils.isEmpty(object)){//无记录
			systemService.saveDeptJobRelationship(relationValue,user.getId());
		}else{
			//先删除后再新增
			systemService.deleteDeptJobRelationship(user.getId(),companyUuid);
			systemService.saveDeptJobRelationship(relationValue,user.getId());
		}
		
		/*保存审批模块权限*/
		if(userId==null){//新增用户
			saveUserLisence(user.getId(),lisenceValue);
		}else{//修改用户
			//两种情况：老数据无权限记录的要新增、有权限记录的则修改
			List<Object> o = systemService.check4UserLisence(user.getId().toString());
			if(CollectionUtils.isEmpty(o)){//无记录
				saveUserLisence(user.getId(),lisenceValue);
			}else{
				//先删除后再新增
				systemService.updateUserLisence(user.getId(),UserUtils.getUser().getCompany().getUuid());
				saveUserLisence(user.getId(),lisenceValue);
			}
		}
		/*保存审批监督弹出框选择内容*/
		if(userId==null){//新增用户
			if(!saveDepartTreeArray[0].equals("")){//审批监督弹出框选择内容不为空
				saveUserProcess(user.getId(),saveDepartTreeArray,saveProductTypeList,saveReviewFlowTypeList);
			}
		}else{//修改用户,删除记录后再新增
			//if(StringUtils.isNotBlank(isSave)){//没有选择对应关系，历史记录不做修改
				systemService.updateUserProcess(user.getId(),UserUtils.getUser().getCompany().getUuid());
				if(!saveDepartTreeArray[0].equals("")){
					saveUserProcess(user.getId(),saveDepartTreeArray,saveProductTypeList,saveReviewFlowTypeList);
				}
			//}
		}
		
		
		if (MultiTenantUtil.turnOnMulitTenant()) {
			// 将用户添加至用户租户库
			final String loginName = user.getLoginName();
			final String companyIdStr = companyId.toString();

			if (userId == null) {// 新增用户
			// tenantService.addUser(user.getLoginName(), companyId.toString());
			// Thread thread=new Thread(new FutureTask<Boolean>(new
			// Callable<Boolean>() {
			// @Override
			// public Boolean call() throws Exception {
			// FacesContext.setCurrentTenant(DataSourceContainer.DEFAULT_DATASOURCE_NAME);
			// tenantService.addUser(loginName,companyIdStr);
			// return true;
			// }
			// }));
			// thread.start();
				TenantUtil.addUser(loginName, companyIdStr);
			} else if (!user.getLoginName().equals(oldLoginName)) {// 修改用户名
				TenantUtil.modifyUser(loginName, oldLoginName, companyIdStr);
				// tenantService.modifyUser(user.getLoginName(), oldLoginName,
				// companyId.toString());
			}
		}
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
			UserUtils.getCacheMap().clear();
		}
		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage&companyId=" + companyId;
	}
	
	
	/**
	 * 保存审批监督弹出框选择内容：部门-产品名称-流程名称，每条对应关系对应sys_user_review_process_permission表中一条记录
	 * @param userId,saveDepartTreeArray,saveProductTypeArray,saveReviewFlowTypeArray
	 */
	public void saveUserProcess(Long userId,String[] saveDepartTreeArray,List<String> saveProductTypeList,List<String> saveReviewFlowTypeList){
		List<SysUserReviewProcessPermission> list = new ArrayList<SysUserReviewProcessPermission>();
		for(String deptId :saveDepartTreeArray){
			for(String productTypeId : saveProductTypeList){
				for(String flowTypeId : saveReviewFlowTypeList){
					SysUserReviewProcessPermission entity = new SysUserReviewProcessPermission();
					entity.setCompany_id(UserUtils.getUser().getCompany().getId());
					entity.setCompany_uuid(UserUtils.getUser().getCompany().getUuid());
					entity.setCreate_by(UserUtils.getUser().getId().toString());
					entity.setCreate_date(new Date());
					entity.setDel_flag(0);
					entity.setDept_id(deptId);
					entity.setProduct_type(productTypeId);
					entity.setReview_flow(flowTypeId);
					entity.setUuid(UuidUtils.generUuid().toString().replaceAll("-", ""));
					entity.setUser_id(userId);
//					systemService.saveUserProcess(entity);
					list.add(entity);
				}
			}
		}
		if(null != list)
		systemService.saveUserProcessPermissionList(list);
		
	}
	
	/**
	 * 账号保存时，新增一条账号权限记录
	 * @return
	 */
	public void saveUserLisence(Long userId,String values){
		SysUserReviewCommonPermission entity = new SysUserReviewCommonPermission();
		entity.setCompany_id(UserUtils.getUser().getCompany().getId());
		entity.setCompany_uuid(UserUtils.getUser().getCompany().getUuid());
		entity.setCreate_by(UserUtils.getUser().getId().toString());
		entity.setCreate_date(new Date());
		entity.setDel_flag(0);
		entity.setUuid(UuidUtils.generUuid().toString().replaceAll("-", ""));
		entity.setUser_id(userId); 
		if(StringUtils.isNotBlank(values)){
			 String[] valueArray = (values+"0").split(",");
			 if(valueArray.length==2){//选中一个
				 if(valueArray[0].equals("1")){//本人审批自动越过
					 entity.setIs_applier_auto_approve(1);
					 entity.setIs_jump_task_permit(0);
					 systemService.saveUserLisence(entity);
				 }else{//越级审批
					 entity.setIs_jump_task_permit(1);
					 entity.setIs_applier_auto_approve(0);
					 systemService.saveUserLisence(entity);
				 }
				 
			 }
			 if(valueArray.length==3){//选中两个
				 entity.setIs_applier_auto_approve(1);
				 entity.setIs_jump_task_permit(1);
				 systemService.saveUserLisence(entity);
			 }
		 }else{
			 entity.setIs_applier_auto_approve(0);
			 entity.setIs_jump_task_permit(0);
			 systemService.saveUserLisence(entity);
		 }
		 
	}

	/**
	 * 请求图片上传的页面
	 * @return
     */
	@RequestMapping(value = "uploadPicturePage")
	public String uploadPicturePage(){
		return "common/uploadPicturePage";
	}
	

	@ResponseBody
	@RequestMapping(value = "validateUser")
	public String validateUser(String oldLoginName, String loginName, String oldGroupeSurname, String groupeSurname, String roleIds) {

		// 判断用户登录名是否重复
		if (!"true".equals(checkLoginName(oldLoginName, loginName))) {
			return "保存用户'" + loginName + "'失败，登录名已存在";
		}

		//青岛凯撒团号姓氏缩写必填
		if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")){
			// 判断团号姓氏缩写是否重复
			if(StringUtils.isBlank(groupeSurname)){
				return "请输入团号姓氏缩写";
			}
			if (!"true".equals(checkGroupeSurname(oldGroupeSurname, groupeSurname))) {
				return "保存团号姓氏缩写'" + groupeSurname + "'失败，团号姓氏缩写已存在";
			}
		}

		// 判断用户角色是否合法
		List<Role> roleList = Lists.newArrayList();
		if (StringUtils.isNotBlank(roleIds)) {
			for (String id : roleIds.split(",")) {
				if (StringUtils.isNotBlank(id)) {
					roleList.add(systemService.getRole(Long.parseLong(id)));
				}
			}
		}
		String returnMsg = systemService.userRolesIsLegal(roleList);
		if (StringUtils.isNotBlank(returnMsg)) {
			return returnMsg;
		}
		return "true";
	}

	@SuppressWarnings("deprecation")
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		if (UserUtils.getUser().getId().equals(id)) {
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		} else if (User.isAdmin(id)) {
			addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		} else {
			// 删除用户租户库中的用户1
			User user = systemService.getUser(id);
			
			// 如果渠道跟进销售有此用户，则不允许删除
			List<Agentinfo> agentList = agentInfoService.findAgentBySalerId(id);
			if (CollectionUtils.isNotEmpty(agentList)) {
				addMessage(redirectAttributes, "删除用户失败, 请先修改或删除此用户下渠道");
			} else {
				systemService.deleteUser(id);
				if (MultiTenantUtil.turnOnMulitTenant()) {
					// 删除用户租户库中的用户2
					// tenantService.removeUser(user.getLoginName(),
					// user.getCompany().getId().toString());
					TenantUtil.deleteUser(user.getLoginName(), user.getCompany().getId().toString());
				}
				addMessage(redirectAttributes, "删除用户成功");
			}
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(User user, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user, request);
			new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User user : list) {
				try {
					if ("true".equals(checkLoginName("", user.getLoginName()))) {
						user.setPassword(SystemService.entryptPassword("123456"));
						BeanValidators.validateWithException(validator, user);
						systemService.saveUser(user);
						successNum++;
					} else {
						failureMsg.append("<br/>登录名 " + user.getLoginName() + " 已存在; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条用户" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据导入模板.xlsx";
			List<User> list = Lists.newArrayList();
			list.add(UserUtils.getUser());
			new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
	}

	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName != null && loginName.equals(oldLoginName)) {
			return "true";
		} else {
			/**
			 * 多租户开关
			 */
			if (MultiTenantUtil.turnOnMulitTenant()) {
				if (TenantUtil.userExist(loginName)) {
					return "false";
				}
			}
			if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
				return "true";
			}
		}
		return "false";
	}
	
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping(value = "checkQuauqAgentLoginName")
	public String checkQuauqAgentLoginName(String oldLoginName, String loginName) {
		if (loginName != null && loginName.equals(oldLoginName)) {
			return "true";
		} else {
			/**
			 * 多租户开关
			 */
			if (MultiTenantUtil.turnOnMulitTenant()) {
				if (TenantUtil.userExist(loginName)) {
					return "false";
				}
			}
			if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
				return "true";
			}
		}
		return "false";
	}

	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkGroupeSurname")
	public String checkGroupeSurname(String oldGroupeSurname, String groupeSurname) {
		if(UserUtils.getUser().getCompany().getUuid().contains("7a8177e377a811e5bc1e000c29cf2586")){
			if (StringUtils.isNotBlank(groupeSurname) && groupeSurname.equals(oldGroupeSurname)) {
				return "true";
			} else {
				if (StringUtils.isNotBlank(groupeSurname)) {
					List<User> userList = systemService.getUserByGroupeSurname(groupeSurname,UserUtils.getUser().getCompany().getId());
					if(userList == null || userList.size() == 0){
						return "true";
					}
				}
			}
			return "false";
		}else{
			return "true";
		}
	}

	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkNo")
	public String checkNo(String oldNo, String no) {
		if (no != null && no.equals(oldNo)) {
			return "true";

		} else if (no != null && systemService.getUserByNo(no) == null) {
//			System.out.println("dose here");
			return "true";
		}
		return "false";
	}

	@ResponseBody
	@RequestMapping(value = "roleList")
	public String roleList(String companyId) {
		List<Role> roleList = new ArrayList<Role>();
		roleList = systemService.findOfficeRole(StringUtils.toLong(companyId));
		String roleSpan = "";
		if (roleList != null && roleList.size() != 0) {
			int i = 1;
			for (Role r : roleList) {
				String color = "";
				if (r.getCompanyId() == null && "3".equals(r.getUserType())) {
					color = "red";
				}
				roleSpan += "<span usertype=\"" + r.getUserType()
						+ "\" style=\"display: inline;\"><input type=\"checkbox\" value=\"" + r.getId()
						+ "\" name=\"roleIdList\" id=\"roleIdList" + i + "\"><label for=\"roleIdList" + i
						+ "\" style=\"color:" + color + "\">" + r.getName() + "</label></span>";
				i++;
				if (i == 11)
					roleSpan += "<br>";
			}
		} else {
			roleSpan = "<label>暂无角色信息</label>";
		}
		return roleSpan;
	}

	/**
	 * 离职账户转移跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("transfer:leave:account")
	@RequestMapping(value = "transferLeaveAccountForm")
	public String transferLeaveAccountForm(Model model) {
		model.addAttribute("users", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		return "modules/sys/transferLeaveAccountForm";
	}

	/**
	 * 离职账户转移跳转
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("transfer:leave:account")
	@RequestMapping(value = "getUserInfoById")
	public String getUserInfoById(Long id) {
		User user = systemService.getUser(id);
		return user.getRoleNames();
	}

	@ResponseBody
	@RequiresPermissions("transfer:leave:account")
	@RequestMapping(value = "transferLeaveAccount")
	public String transferLeaveAccount(String businessType, String modulesType, Long leaveUserId, Long transferUserId) {

		List<String> sqlParaList = Lists.newArrayList(); // 涉及业务模块sql的集合
		boolean isOrder = false; // 是否是订单模块
		// 产品 1；订单 2；
		if ("2".equals(modulesType)) {
			isOrder = true;
		}

		// 签证数据转移
		if (Context.ORDER_STATUS_VISA.equals(businessType)) {
			// 产品
			if (!isOrder) {
				sqlParaList.add("visafile,createBy,updateBy");
				sqlParaList.add("visa_product_file,create_by,update_by");
				sqlParaList.add("visa_products,createBy,updateBy");
			}
			// 订单
			else {
				sqlParaList.add("visa,create_by,update_by");
				sqlParaList.add("orderpay,createBy,updateBy,orderType");
				sqlParaList.add("visa_order,create_by,update_by");
			}
		}
		// 机票数据转移
		else if (Context.ORDER_STATUS_AIR_TICKET.equals(businessType)) {
			// 产品
			if (!isOrder) {
				sqlParaList.add("activity_airticket,createBy,updateBy");
			}
			// 订单
			else {
				sqlParaList.add("orderpay,createBy,updateBy,orderType");
				sqlParaList.add("airticket_order,create_by,update_by");
			}
		}
		// 单团类产品转移（单团、散拼、游学、大客户、自由行、邮轮）
		else {
			// 产品
			if (!isOrder) {
				sqlParaList.add("activitygroup,createBy,updateBy");
				sqlParaList.add("travelactivity,createBy,updateBy,activity_kind");
			}
			// 订单
			else {
				sqlParaList.add("orderpay,createBy,updateBy,orderType");
				sqlParaList.add("productorder,createBy,updateBy,orderStatus");
			}
		}

		int create_count = 0;
		if (CollectionUtils.isNotEmpty(sqlParaList)) {
			for (String sqlPara : sqlParaList) {
				if (StringUtils.isNotBlank(sqlPara)) {
					String paras[] = sqlPara.split(",");
					String tableName = paras[0];
					String column_create = paras[1];
					String column_update = paras[2];
					String type = (paras.length == 4 ? paras[3] : null);

					String batchUpdateSql_create = "update " + tableName + " set " + column_create + " = "
							+ transferUserId + " where " + column_create + " = " + leaveUserId
							+ (StringUtils.isNotBlank(type) ? " and " + type + "=" + businessType : "");

					String batchUpdateSql_update = "update " + tableName + " set " + column_update + " = "
							+ transferUserId + " where " + column_create + " = " + leaveUserId
							+ (StringUtils.isNotBlank(type) ? " and " + type + "=" + businessType : "");

					create_count = systemService.updateBySql(batchUpdateSql_create);
					systemService.updateBySql(batchUpdateSql_update);
				}
			}
		}

		return "共有" + create_count + "条数据被迁移";
	}

	@ResponseBody
	@RequestMapping(value = "deptUserList")
	public String deptUserList(String companyId) {

		return UserUtils.getAllDepartmentUsersByCompanyId();
	}
	
	@ResponseBody
	@RequestMapping(value = "getUserByDepAndRole")
	public List<User> getUserByDepAndRole(HttpServletRequest request) {
		List<User> userList = new ArrayList<User>();
		String depId = request.getParameter("departmentId");
		Long companyId = UserUtils.getUser().getCompany().getId();
		if (StringUtils.isNotEmpty(depId) && companyId != null) {
			userList = systemService.getUserByDepartmentAndRoleType(companyId,Long.parseLong(depId));
		}
		return userList;
	}
	
	@ResponseBody
	@RequestMapping(value = "getUserByDepartment")
	public List<User> getUserByDepartment(HttpServletRequest request) {
		List<User> userList = new ArrayList<User>();
		String depId = request.getParameter("departmentId");
		Long companyId = UserUtils.getUser().getCompany().getId();
		if (StringUtils.isNotEmpty(depId) && companyId != null) {
			userList = systemService.getUserByDepartment(companyId,Long.parseLong(depId));
		}
		return userList;
	}
	
	/**
     * 构建公司产品类型映射关系
     * @param officeProductTypes
     * @return
     * @created_by zhenxing.yan 2015年11月16日
     */
    private Map<Integer, String> buildProductTypeMap(List<SysOfficeProductType> officeProductTypes) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        if (officeProductTypes != null && officeProductTypes.size() > 0) {
            for (SysOfficeProductType sysOfficeProductType : officeProductTypes) {
                if (ReviewContext.productTypeMap.containsKey(sysOfficeProductType.getProductType())) {
                    result.put(sysOfficeProductType.getProductType(),
                            ReviewContext.productTypeMap.get(sysOfficeProductType.getProductType()));
                }
            }
        }
        return result;
    }
    
    /**
     * 构建公司流程类型映射关系
     * @param officeProcessTypes
     * @return
     * @created_by zhenxing.yan 2015年11月16日
     */
    private Map<Integer, String> buildProcessTypeMap(List<SysOfficeProcessType> officeProcessTypes) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        if (officeProcessTypes != null && officeProcessTypes.size() > 0) {
            for (SysOfficeProcessType sysOfficeProcessType : officeProcessTypes) {
                if (ReviewContext.reviewFlowTypeMap.containsKey(sysOfficeProcessType.getProcessType())) {
                    result.put(sysOfficeProcessType.getProcessType(),
                            ReviewContext.reviewFlowTypeMap.get(sysOfficeProcessType.getProcessType()));
                }
            }
        }
        return result;
    }
    
    @RequiresPermissions("sys:user:exportPwd")
    @RequestMapping(value="downloadPwd",method=RequestMethod.POST)
	public void downloadPwd(HttpServletRequest request, HttpServletResponse response) {
    	
    	List<User> userList;
    	Office office;
    	if (UserUtils.getUser().getId() == 1) {
			userList = systemService.getAllUser();
			downloadAll(userList, request, response);
    	} else {
    		office = UserUtils.getUser().getCompany();
    		userList = systemService.getUserByCompanyId(office.getId());
    		downloadOne(office, userList, request, response);
    	}
	}
    
    private void downloadOne(Office office, List<User> userList, HttpServletRequest request, HttpServletResponse response) {
    	// 要导出数据
    	List<Object[]> exportUserList = new ArrayList<Object[]>();
    	if (CollectionUtils.isNotEmpty(userList)) {
    		for (User user : userList) {
    			Object[] temp = new Object[4];
    			temp[0] = user.getName();
    			temp[1] = user.getLoginName();
    			temp[2] = Base64Util.getFromBase64(user.getTwo_psw());
    			temp[3] = DateUtils.date2String(new Date());
    			exportUserList.add(temp);
    		}
    		
    	}
		//文件名称
		String fileName = office.getName() + "-用户信息表";
		//Excel各行名称
		String[] cellTitle =  {"姓名","登录名","密码","日期"};
		//文件首行标题
		String firstTitle = "用户信息表";
		try {
			ExportExcel.createExcle(fileName, exportUserList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void downloadAll(List<User> userList, HttpServletRequest request, HttpServletResponse response) {
    	// 要导出数据
    	List<Object[]> exportUserList = new ArrayList<Object[]>();
    	if (CollectionUtils.isNotEmpty(userList)) {
    		for (User user : userList) {
    			if (user.getCompany() != null) {
    				Object[] temp = new Object[6];
        			temp[0] = user.getCompany().getName();
        			temp[1] = user.getName();
        			temp[2] = user.getLoginName();
        			temp[3] = user.getPassword();
        			temp[4] = Base64Util.getFromBase64(user.getTwo_psw());
        			temp[5] = DateUtils.date2String(new Date());
        			exportUserList.add(temp);
    			}
    		}
    		
    	}
		//文件名称
		String fileName = "用户信息表";
		//Excel各行名称
		String[] cellTitle =  {"公司名称","姓名","登录名","密码","明文密码","日期"};
		//文件首行标题
		String firstTitle = "用户信息表";
		try {
			ExportExcel.createExcle(fileName, exportUserList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 获取代替下单用户列表
     * @param request
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "getSubstituteList")
	public Map<String, Object> getSubstituteList(HttpServletRequest request) {
    	Map<String, Object> resultMap = new HashMap<>();
		String subLoginName = request.getParameter("subLoginName");  // 登录名
		String subUserName = request.getParameter("subUserName");  // 姓名
		String subRoleStr = request.getParameter("subRole");  // 角色
		Long subRole = null;
		if (StringUtils.isNotBlank(subRoleStr)) {
			subRole = Long.parseLong(subRoleStr);
		}
		// 获取代替下单用户列表
		JSONArray subJson = systemService.getSubstituteList(subLoginName, subUserName, subRole);
		resultMap.put("flag", "success");
		resultMap.put("data", subJson);
		return resultMap;
	}
    
    
}
