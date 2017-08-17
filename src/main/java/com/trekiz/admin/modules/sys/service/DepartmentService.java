package com.trekiz.admin.modules.sys.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.MenuDao;
import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 
 * @description 部门管理
 *
 * @author baiyakun
 *
 * @create_time 2014-9-15
 */
@Service
@Transactional(readOnly = true)
public class DepartmentService extends BaseService {
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private RoleDao roleDao;

	/**
	 * 根据ID删除对象
	 * @param id
	 */
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public Map<String, String> delDepartment(Long id) {
		
		Map<String, String> map = Maps.newHashMap();
		map.put("result", "success");
		map.put("msg", "删除部门成功");
		
		Department department = departmentDao.findOne(id);
		List<Department> departmentList = departmentDao.findByParentIdsLike("%," + id + ",%");
		if (CollectionUtils.isEmpty(departmentList)) {
			departmentList = Lists.newArrayList();
		}
		departmentList.add(department);
		for (Department dept : departmentList) {
			if (Context.DEL_FLAG_NORMAL.equals(dept.getDelFlag())) {
				List<Role> roleList = roleDao.findByDepartment(dept);
				if (CollectionUtils.isNotEmpty(roleList)) {
					for (Role role : roleList) {
						if (Context.DEL_FLAG_NORMAL.equals(role.getDelFlag())) {
							map.put("result", "error");
							map.put("msg", "本部门有角色引用，不能删除");
							break;
						}
					}
				}
			}
		}
		if (map.get("result").equals("success")) {
			departmentDao.deleteById(id, "%," + id + ",%");
		}
		return map;
	}
	
	/**
	 * 保存
	 * @param entity
	 */
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void save(Department department) {
		if(!"0,".equals(department.getParentIds())) {
			department.setParent(this.findById(department.getParent().getId()));
			String oldParentIds = department.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
			department.setParentIds(department.getParent().getParentIds() + department.getParent().getId() + ",");
			department.setLevel(department.getParentIds().split(",").length - 1);
			departmentDao.clear();
			departmentDao.save(department);
			// 更新子节点 parentIds
			List<Department> list = departmentDao.findByParentIdsLike("%," + department.getId() + ",%");
			for (Department e : list) {
				e.setParentIds(e.getParentIds().replace(oldParentIds, department.getParentIds()));
				if (e.getParentIds().split(",").length <= 0) {
					e.setLevel(0);
				} else {
					e.setLevel(e.getParentIds().split(",").length - 1);
				}
			}
			departmentDao.save(list);
		}
	}
	
	/**
	 * 部门-职务管理列表查询
	 * @author gaoang
	 * @return
	 */
	public Page<Map<String,Object>> searchJobManagementList(HttpServletRequest request,HttpServletResponse response){
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" SELECT sjn.uuid,sjn.name,sjn.type FROM sys_job_new sjn ");
		sqlBuffer.append(" WHERE sjn.del_flag=0 AND sjn.company_uuid=? ");
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		page.setOrderBy("sjn.id DESC");
		
		//用户所属批发商uuid
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		Page<Map<String,Object>> JobManagementList = departmentDao.findBySql(page, sqlBuffer.toString(), Map.class, companyUuid);
		
		return JobManagementList;
	}
	
	/**
	 * 校验职务名称是否已存在
	 * @param jobName
	 * @return
	 */
	public List<Map<String,Object>> searchJobNames(String jobName,String uuid){
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" SELECT sjn.name,sjn.type FROM sys_job_new sjn ");
		sqlBuffer.append(" WHERE sjn.del_flag=0 AND sjn.company_uuid=? AND sjn.name=? ");
		//职务修改时，校验除自己之外是否还有重名情况。
		if(StringUtils.isNotBlank(uuid)){
			sqlBuffer.append(" AND sjn.uuid<>? ");
			return departmentDao.findBySql(sqlBuffer.toString(), Map.class,companyUuid, jobName,uuid);
		}else{
			return departmentDao.findBySql(sqlBuffer.toString(), Map.class,companyUuid, jobName);
		}
		
		
		
	}
	
	/**
	 * 部门-职务管理-删除
	 * @param uuid
	 */
	public void deleteJobManagement(String uuid){
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE sys_job_new SET del_flag=1 WHERE uuid=? ");
		departmentDao.updateBySql(sql.toString(), uuid);
	}
	
	/**
	 * 部门-职务管理-修改
	 */
	public void modifyJobManagement(String type,String name, String uuid){
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE sys_job_new SET type=?,name=?,update_by=?,update_date=? WHERE uuid=? ");
		departmentDao.updateBySql(sql.toString(),type,name,UserUtils.getUser().getId(),new Date(),uuid);
	}
	
	/**
	 * 根据ID查询对象
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public Department findById(Long id) {
		return departmentDao.findOne(id);
	}
	
	/**
	 * 根据公司ID查询
	 * @param officeId
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<Department> findByOfficeId(Long officeId) {
		return departmentDao.findByOfficeId(officeId);
	}
	
	/**
	 * 查询该部门的所有子部门的id
	 * @param officeId
	 * @param parentId
	 * @return
	 */
	public List<Object> findByOfficeIdAndParentId(Long officeId,Long parentId){
		String sql = "select d.id from department d where d.office_id=? and d.parent_ids like '%,"+parentId+",%' and d.delFlag='0' ";
		
		return departmentDao.findBySql(sql, officeId);
	}

	/**
	 * 根据公司id获取部门Map
	 * @param officeId
	 * @return
     */
	public Map<Long,Department> getDepartmentMapByOfficeId(Long officeId){
		Assert.notNull(officeId,"officeId should not be null!");
		Map<Long,Department> departmentMap=new HashMap<>();

		List<Department> departments=departmentDao.findByOfficeId(officeId);
		if (departments!=null&&departments.size()>0){
			for (Department department:departments){
				departmentMap.put(department.getId(),department);
			}
		}

		return departmentMap;
	}
	
	/**
	 * 查询本公司总部（父级）
	 * @param comanyId
	 * @return
	 */
	@Transactional(readOnly = true)
	public Department getParent(Office office) {
		Department dept = null;
		dept = departmentDao.findParent(office.getId());
		return dept;
	}
	
	/**
	 * 添加供应商顶级部门
	 * @param office
	 */
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void addParent(Office office) {
		Department dept = new Department();
		Date date = new Date();
		dept.setOffice(office);
		dept.setName(office.getName());
		dept.setCode(String.valueOf(date.getTime()));
		dept.setParent(new Department(0L));
		dept.setParentIds("0,");
		dept.setAnnouncement("0");
		dept.setLowestLevel("0");
		dept.setLevel(0);
		dept.setSort(0);
		dept.setDelFlag("0");
		departmentDao.save(dept);
	}
	
	
	/**
	 * 根据ID查询对象
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean findIsExist(Long officeId, Long id, String code) {
		boolean flag = false;
		List<Department> list = departmentDao.findByCode(officeId, code);
		if(CollectionUtils.isNotEmpty(list)) {
			Iterator<Department> it = list.iterator();
			if(id != null) {
				while(it.hasNext()) {
					if(it.next().getId() != id) {
						flag = true;
						break;
					}
				}
			} else {
				flag = true;
			}
		}
		return flag;
	}
	
	
	
	/**
	 * 设置部门对象属性
	 * @param checkType 查询种类
	 * @param model
	 * @param user
	 * @return
	 */
	public DepartmentCommon setDepartmentPara(String checkType, Model model) {
		
		User user = UserUtils.getUser();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		//创建部门查询对象，并把前台传递过来部门id传给对象
		DepartmentCommon common = new DepartmentCommon();
		String departmentId = request.getParameter("departmentId");
		common.setDepartmentId(departmentId);
		if (StringUtils.isNotBlank(departmentId)) {
			common.setDept(findById(Long.parseLong(departmentId)));
		}
		//设置查询种类
		common.setCheckType(checkType);
		//根据URL查询对应权限标识符
		Menu menu = getMenuByUrl(request);
		if (menu != null) {
			common.setPermission(menu.getPermission());
		}
		
		//订单统计或询价
		if ("orderStatistics".equals(checkType) || "inquiry".equals(checkType)) {
			//获取要显示的部门
			setOrderStatisticsShowArea(user, common);
		} else {
			setShowArea(user, common);
			if (model != null) {
				model.addAttribute("operation", common.getOperation());
			} else {
				//兼容签证订单
				request.setAttribute("operation", common.getOperation());
			}
			
		}
		if (model != null) {
			model.addAttribute("departmentId", common.getDepartmentId());
			model.addAttribute("showAreaList", common.getShowAreaList());
		} else {
			//兼容签证订单
			request.setAttribute("departmentId", common.getDepartmentId());
			request.setAttribute("showAreaList", common.getShowAreaList());
		}
		
		return common;
	}
	
	/**
	 * 判断用户角色类型并选择要显示的部门并选择要查询的部门ID
	 * @param user
	 * @param inner
	 * @return
	 */
	private DepartmentCommon setShowArea(User user, DepartmentCommon common) {
		
		Set<String> userOfDeptIds = Sets.newHashSet();//用户所在部门IDSet集合
		
		//判断当前用户所拥有的角色类型
        List<Role> roleList = user.getRoleListOrderByDept();
        
    	for (Role role : roleList) {
    		Department dept = role.getDepartment();
        	String type = role.getRoleType();
        	
        	if (Context.DICT_TYPE_YES.equals(role.getIsOperational().toString())) {
        		common.setOperation(true); //是否可操作
        	}
        	if (Context.ROLE_TYPE_MANAGER.equals(type)) {
        		common.setIsManager(true); //是否是系统管理员
        	}
        	if (Context.ROLE_TYPE_SALES.equals(type)) {
        		common.setIsSale(true); //是否是销售
        	}
        	if (Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
        		common.setIsSaleManager(true); //是否是销售经理
        	}
        	if (Context.ROLE_TYPE_OP.equals(type)) {
        		common.setIsOP(true); //是否是计调
        	}
        	if (Context.ROLE_TYPE_OP_EXECUTIVE.equals(type)) {
        		common.setIsOPManager(true); //是否是计调经理
        	}
        	if (Context.ROLE_TYPE_OTHER.equals(type)) {
        		common.setIsOther(true); //是否是其他角色
        	}
        	
        	//角色所属部门不为空且没有被删除
        	if (dept != null && Context.DEL_FLAG_NORMAL.equals(dept.getDelFlag())) {
        		
        		userOfDeptIds.add(dept.getId().toString());
        		
        		//判断用户是否属于总部人员
        		if (dept.getParent() == null) {
        			common.setIsHQ(true);
        			common.setHQId(dept.getId());
        		}
        	} 
        }
    	
    	setDeptIdAndShowArea(common, user);
    	
		if(!userOfDeptIds.contains(common.getDepartmentId())) {
			common.setIsSelfDept(false);
		}
        
        getRoleTypeList(user, common);
        if(CollectionUtils.isEmpty(common.getShowAreaList())) {
        	common.setShowAreaList(getShowAreas(roleList, null));
        }
		return common;
	}
	
	/**
	 * 如果是销售则在预定模块显示所有区域产品
	 * @param common
	 * @param user
	 */
	private void setDeptIdAndShowArea(DepartmentCommon common, User user) {
		//add by jiachen
		if(user.getRoleListOrderByDept().isEmpty()) {
			return;
		}
		
		List<Role> roleList = user.getRoleListOrderByDept();
		Department dept = roleList.get(0).getDepartment();
		
		/** 如果是销售，且在预定页面，默认查看所有产品，并分组；如果为系统管理员或总部人员能查看所有分组*/
		if (((common.getIsSale() || common.getIsSaleManager()) && "bookOrder".equals(common.getCheckType())) 
				|| common.getIsManager() || common.getIsHQ()) {
			Department deptOfParent = getParent(user.getCompany());
			if (deptOfParent != null) {
				if (StringUtils.isBlank(common.getDepartmentId())) {
	    			common.setDepartmentId(deptOfParent.getId().toString());
	    			common.setDept(deptOfParent);
				}
				common.setShowAreaList(getShowAreas(roleList, deptOfParent));
			}
		} else {
			if (StringUtils.isBlank(common.getDepartmentId()) && dept != null) {
				common.setDepartmentId(dept.getId().toString());
				common.setDept(dept);
			}
			common.setShowAreaList(getShowAreas(roleList, dept));
		}
	}
	
	/**
	 * 根据用户角色查询要展示部门：如果有子部门则查询出来，没有则不显示
	 * @param roleList
	 * @param dept
	 * @return
	 */
	private Set<Department> getShowAreas(List<Role> roleList, Department dept) {
		if (dept != null) {
			//如果dept有同级别的部门，则显示所有同级别部门，否则显示dept及其子部门（当部门为顶级部门的时候不再判断）
			if (CollectionUtils.isNotEmpty(roleList) 
					&& roleList.size() >= 2 
					&& roleList.get(0).getDepartment().getLevel() == roleList.get(1).getDepartment().getLevel()
					&& dept.getParent() != null) {
				Set<Department> list = new LinkedHashSet<Department>();
				for (int i = 0; i < roleList.size()-1; i++) {
					if (roleList.get(i).getDepartment().getLevel() == roleList.get(i+1).getDepartment().getLevel()) {
						list.add(roleList.get(i).getDepartment());
						list.add(roleList.get(i+1).getDepartment());
					}
				}
				return list;
			} else {
				dept = findById(dept.getId());
				Set<Department> list = new LinkedHashSet<Department>();
				list.add(dept);
				list.addAll(dept.getChildList());
				//添加本部门：因为本部门也可以发布产品
				return list;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 获取用户所在此部门的所有角色类型
	 * @param common
	 * @return
	 */
	public DepartmentCommon getRoleTypeList(User user, DepartmentCommon common) {
		List<Role> roleList = user.getRoleList();
		List<String> roleTypeList = common.getRoleTypeList();
		if (CollectionUtils.isEmpty(roleTypeList)) {
			roleTypeList = Lists.newArrayList();
		}
		//如果用户所在部门与要显示部门相同，则把对应角色类型放入List
		if (CollectionUtils.isNotEmpty(roleList)) {
			for (Role role : roleList) {
				if (role.getDepartment() != null && role.getDepartment().getId().toString().equals(common.getDepartmentId())) {
					roleTypeList.add(role.getRoleType());
				}
			}
		}
		common.setRoleTypeList(roleTypeList);
		return common;
	}
	
	/**
	 * 根据请求url查询对应菜单
	 * @param request
	 * @return
	 */
	public Menu getMenuByUrl(HttpServletRequest request) {
		
		String url = request.getRequestURI();
		if(StringUtils.isNotBlank(url)) {
			//去除前面项目名称
			int beginIndex = url.indexOf("/a/");
	    	url = url.substring(beginIndex + 2);
	    	//去除后缀.htm
	    	int endHtlIndex = url.indexOf(".htm");
	    	if(endHtlIndex != -1) {
	    		url = url.substring(0, endHtlIndex);
	    	}
	    	//去除后缀参数
	    	int endParaIndex = url.indexOf("/?");
	    	if(endParaIndex != -1) {
	    		url = url.substring(0, endParaIndex);
	    	}
	    	//特殊处理：预报名
	    	if("/activity/managerforOrder/list/2".equals(url)) {
	    		url = "/activity/managerforOrder/list/1";
	    	}
	    	//特殊处理：询价
	    	if("/eprice/manager/ajax/project/plist".equals(url)) {
	    		url = "/eprice/manager/project/list4saler";
	    	}
	    	//特殊处理：签证销售订单
	    	if("/visa/order/searchxs".equals(url)) {
	    		url = "/visa/order/searchxs";
	    	}
	    	
	    	//add by 2016/1/22 for 113 特殊处理：签务签证订单
	    	if("/visa/order/searchqw".equals(url)) {
	    		url = "/visa/order/list";
	    	}
	    	
	    	List<Menu> menuList = menuDao.findByHref(url);
	    	if(CollectionUtils.isNotEmpty(menuList)) {
	    		return menuList.get(0);
	    	}
		}
		return null;
	}
	
	/**
	 * 获取订单统计要显示区域和要实现部门ID
	 * @param user
	 * @param common
	 * @return
	 */
	private DepartmentCommon setOrderStatisticsShowArea(User user, DepartmentCommon common) {
		List<Role> salerRoleList = Lists.newArrayList();
		List<Department> salerDeptList = Lists.newArrayList();
		//判断当前用户所拥有的角色类型
		Iterator<Role> roleList = user.getRoleList().iterator();
    	while(roleList.hasNext()) {
    		Role role = roleList.next();
        	String type = role.getRoleType();
        	
        	if(Context.ROLE_TYPE_MANAGER.equals(type)) {
        		common.setIsManager(true); //是否是系统管理员
        	}
        	if(Context.ROLE_TYPE_SALES.equals(type)) {
        		common.setIsSale(true); //是否是销售
        	}
        	if(Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
        		common.setIsSaleManager(true); //是否是销售经理
        	}
        	
        	//如果是系统管理员或销售或销售主管，则把此角色放入salerRoleList
        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
        		salerRoleList.add(role);
        		salerDeptList.add(role.getDepartment());
        	}
        }
    	
    	//获取订单统计要显示区域和要实现部门ID
    	if(CollectionUtils.isNotEmpty(salerRoleList)) {
    		Role role = salerRoleList.get(0);
    		Department dept = role.getDepartment();
    		if(dept != null) {
    			dept = departmentDao.findOne(dept.getId());
    			if(StringUtils.isBlank(common.getDepartmentId())) {
        			common.setDepartmentId(dept.getId().toString());
        			common.setDept(dept);
        		}
    			//添加本部门：因为本部门也可以发布产品
    			Set<Department> list = new LinkedHashSet<Department>();
    			if(common.getIsManager()) {
    				list.add(dept);
    				list.addAll(dept.getChildList());
    			} else if(common.getIsSaleManager() || common.getIsSale()) {
    				if(salerRoleList.size() == 1 && common.getIsSaleManager()) {
    					list.add(dept);
    					list.addAll(dept.getChildList());
    				} else if(salerRoleList.size() > 1) {
    					common.setIsParentsAndChildren(false);
    					for(Role temp : salerRoleList) {
        					list.add(temp.getDepartment());
        				}
    				}
    			}
    			common.setShowAreaList(list);
    		} 
    	}
		return common;
	}
/**
 * 使当前供应商的交通方式都能和机票产品进行关联
 */
	public void updateTrafficeType() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE sysdefinedict s SET s.defaultFlag=1 WHERE s.companyId=");
		buffer.append(UserUtils.getUser().getCompany().getId());
		buffer.append(" AND s.type='traffic_mode' AND s.delFlag=0");
		departmentDao.updateBySql(buffer.toString());
	}
	
	/**
	 * 签证专用
	 * @author yakun.bai
	 * @Date 2016-6-2
	 */
	public DepartmentCommon setDepartmentPara() {
		
		//当前登录用户信息
		User user = UserUtils.getUser();
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		//部门
		DepartmentCommon common = new DepartmentCommon();
		
		//根据URL查询对应权限标识符
		Menu menu = getMenuByUrl(request);
		if (menu != null) {
			common.setPermission(menu.getPermission());
		}
		
		//判断当前用户所拥有的角色类型
		List<Role> roleListOrderByDept = user.getRoleListOrderByDept();
		//部门ID
		if(user.getRoleListOrderByDept() != null && user.getRoleListOrderByDept().size()>0){
			Department dept = roleListOrderByDept.get(0).getDepartment();
			if (dept != null) {
				//部门ID
				common.setDepartmentId(dept.getId().toString());
			}
		}
		List<Role> roleList = user.getRoleList();
		List<String> roleTypeList = common.getRoleTypeList();
		if (CollectionUtils.isEmpty(roleTypeList)) {
			roleTypeList = new ArrayList<String>();
		}
		//如果用户所在部门与要显示部门相同，则把对应角色类型放入List
		if (CollectionUtils.isNotEmpty(roleList)) {
			for (Role role : roleList) {
				if (role.getDepartment() != null && role.getDepartment().getId().toString().equals(common.getDepartmentId())) {
					roleTypeList.add(role.getRoleType());
				}
			}
		}
		common.setRoleTypeList(roleTypeList);
		return common;
	}
	
}


