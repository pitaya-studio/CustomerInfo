package com.trekiz.admin.modules.sys.utils;

import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.repository.SupplierInfoDao;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.AreaDao;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.MenuDao;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.repository.UserDeptJobDao;
import com.trekiz.admin.modules.sys.repository.UserDeptJobNewDao;
import com.trekiz.admin.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.trekiz.admin.review.guarantee.service.GuaranteeService;

/**
 * 用户工具类
 *
 * @author zj
 * @version 2013-11-19
 */
public class UserUtils extends BaseService {

	private static final Log LOG = LogFactory.getLog(UserUtils.class);

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder
			.getBean(OfficeDao.class);
	private static SupplierInfoDao supplierInfoDao = SpringContextHolder.getBean(SupplierInfoDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
    private static UserDeptJobDao userDeptJobDao = SpringContextHolder.getBean(UserDeptJobDao.class);
    private static UserDeptJobNewDao userDeptJobNewDao = SpringContextHolder.getBean(UserDeptJobNewDao.class);
	private static DepartmentDao departmentDao = SpringContextHolder.getBean(DepartmentDao.class);
	private static AgentinfoService agentinfoService = SpringContextHolder.getBean(AgentinfoService.class);
	private static GuaranteeService guaranteeService = SpringContextHolder.getBean(GuaranteeService.class);
	public static final String CACHE_USER = "user";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_SUPPLIER_LIST = "supplierList";
	public static final String ONLINE_USERID = "onlineUserId";
	public static final String ONLINE_USER_LOGIN_NAME = "onlineUserLoginName";
	public static final String CACHE_USER_LIST = "userLIST";
	private static List<String> meituCompanyUuids = Lists.newArrayList
	    		("42e108f116a8464a902d43831e7b0381","7a81823b77a811e5bc1e000c29cf2586","e5bbee00327a42b28b7b15b512a1db1e","fe06d203df074cdc9a6c5bacd78a7b46");
	private static List<String> huaerCompanyUuids = Lists.newArrayList("dca73953d6204a57a3840c901cea8547", "eea674eff3a14b5aaf891e46acaacca6");
	public static User getUser() {
		User user = (User) getCache(CACHE_USER);
		if (user == null) {
			Principal principal = (Principal) SecurityUtils.getSubject()
					.getPrincipal();
			if (principal != null) {
				user = userDao.findOne(principal.getId());
				putCache(CACHE_USER, user);
			}
		}
		if (user == null) {
			user = new User();
			try {
				SecurityUtils.getSubject().logout();
			} catch (Exception e) {
				LOG.error("当前没有登录用户", e);
			}
		}
		return user;
	}

	/**
	 * 查询用户名 add by chy 2014年11月7日20:41:10
	 *
	 * @param userId
	 * @return
	 */
	public static String getUserNameById(Long userId) {
		User user = getUser(userId);
		return user.getName();
	}

	public static User getUser(boolean isRefresh) {
		if (isRefresh) {
			removeCache(CACHE_USER);
		}
		return getUser();
	}

	/**
	 * 根据用户ID查询用户对象信息
	 * @param userId
	 * @return
     */
	public static User getUser(Long userId) {
		if(null == userId){
			return new User();
		}
		User u = userDao.findOne(userId);
		return u == null ? new User() : u;
	}


	/**
	 * 查询用户名List
	 *
	 * @param userIds
	 * @return
	 */
	public static List<String> getUserNameListByIds(String userIds) {
		List<String> resultUserName = Lists.newArrayList();
		if(StringUtils.isBlank(userIds)){
			return resultUserName;
		}
		String[] userIdArray = userIds.split(",");
		for (String userId : userIdArray) {
			User user = getUser(userId);
			if(null != user){
				resultUserName.add(user.getName());
			}
		}
		return resultUserName;
	}
	
	/**
	 * 查询用户List<Map>
	 *
	 * @param userIds
	 * @return
	 */
	public static List<User> getUserListByIds(String userIds) {
		List<User> resultUserList = Lists.newArrayList();
		if(StringUtils.isBlank(userIds)){
			return resultUserList;
		}
		String[] userIdArray = userIds.split(",");
		for (String userId : userIdArray) {
			User user = getUser(userId);
			if(null != user.getId()){
				resultUserList.add(user);
			}
		}
		return resultUserList;
	}

	/**
	 * 查询用户名 add by sy 2015年11月3日10:06:10
	 *
	 * @param userIds
	 * @return
	 */
	public static String getUserNameByIds(String userIds) {
		StringBuffer userName = new StringBuffer("");
		if(StringUtils.isBlank(userIds)){
			return userName.toString();
		}
		String[] userIdArray = userIds.split(",");
		for (String userId:userIdArray) {
			User user = getUser(userId);
			if(null != user.getId()){
				userName.append(user.getName()).append(",");
			}
		}
		if(userName.toString().endsWith(",")){
			userName.delete(userName.toString().length()-1, userName.toString().length());
		}
		return userName.toString();
	}


	/**
	 * fns标签使用的根据id查找用户
	 * @author jiachen
	 * @DateTime 2014-12-9 下午07:42:30
	 * @return User
	 * update by ruyi.chen 2015-06-09 增加空值判断
	 */
	public static User getUser(String userId) {
		if(StringUtils.isNotBlank(userId)){
			User u = getUser(Long.valueOf(userId));
			return u;
		}else{
			return new User();
		}

	}

	/**
	 * 获取公司uuid
	 * @return
     */
	public static String getCompanyUuid(){
		Office office = getCurrentOffice();
		if(null == office){
			return null;
		}
		return office.getUuid();
	}
	
	/**
	 * 获取公司
	 * @return
     */
	public static Office getCurrentOffice(){
		User user = getUser();
		if(user==null){
			return null;
		}
		return user.getCompany();
	}

	public static Long getCompanyIdForData() {
		User user = getUser();
		if (user == null)
			return null;
		Long companyId = null;
		if (Context.USER_TYPE_MAINOFFICE.equalsIgnoreCase(user.getUserType())
				&& user.getAgentId() != null) {
			// 渠道商返回渠道商id
			companyId = user.getAgentId();
		} else if (Context.USER_TYPE_RECEPTION.equalsIgnoreCase(user
				.getUserType()) && user.getCompany() != null) {
			// 地接社返回本公司id
			companyId = user.getCompany().getId();
		}
		return companyId;
	}

	@SuppressWarnings("unchecked")
	public static List<Menu> getMenuList() {
		List<Menu> menuList = Lists.newArrayList();
		User user = getUser();
		if (user.isAdmin()) {
			menuList = menuDao.findAllList();
		} else {
			menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
			try {
				if(menuList == null) {
					menuList = menuDao.findByUserId(user.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		putCache(CACHE_MENU_LIST, menuList);
		return menuList;
	}

	public static List<Area> getAreaList() {
		List<Area> areaList = null;// (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null) {
			// User user = getUser();
			// if (user.isAdmin()){
			areaList = areaDao.findAllList();
			// }else{
			// areaList = areaDao.findAllChild(user.getArea().getId(),
			// "%,"+user.getArea().getId()+",%");
			// }
			// putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}

	public static List<Area> getAreaCityList() {
		List<Area> areaList = null;// (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null) {
			// User user = getUser();
			// if (user.isAdmin()){
			areaList = areaDao.findByCityList();
			// }else{
			// areaList = areaDao.findAllChild(user.getArea().getId(),
			// "%,"+user.getArea().getId()+",%");
			// }
			// putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}

	/**
	 * 渠道商登陆只显示所属批发商录过的目标区域 批发商登陆只显示自己录过的目标区域 创建人：liangjingming 创建时间：2014-2-10
	 * 下午12:14:27
	 *
	 */
	public static List<Area> findByFilter(List<Long> areaIds) {
		List<Area> areaList;
		areaList = areaDao.findByFilter(areaIds);
		return areaList;
	}

	/**
	 * 批发商查询
	 *
	 * @param orderName
	 *            排序字段
	 * @param filterName
	 *            过滤包含此词组的批发商
	 * @return
	 */
	public static List<Office> getOfficeList(Boolean isSelectAll, String orderName, String filterName) {
		List<Office> officeList = null;// (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null) {
			DetachedCriteria dc = officeDao.createDetachedCriteria();
			dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
			User user = UserUtils.getUser();
			if(!user.isAdmin() && user.getCompany() != null && !isSelectAll) {
				dc.add(Restrictions.eq("id", user.getCompany().getId()));
			}
			if (!StringUtils.isBlank(filterName)) {
				dc.add(Restrictions.sqlRestriction(" name not like '%"
						+ filterName + "%' "));
			}
			if (!StringUtils.isBlank(orderName)) {
				// dc.addOrder(Order.asc(orderName));
				dc.add(Restrictions.sqlRestriction(" 1=1 order by convert("
						+ orderName + " USING gbk) "));
			} else {
				dc.addOrder(Order.desc("id"));
			}

			officeList = officeDao.find(dc);
			// putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}
	/**
	 *
	* @Title: getSupplierInfoList
	* @Description: TODO(环球行批发商集合)
	* @param @param orderName
	* @param @param filterName
	* @return List<SupplierInfo>    返回类型
	* @throws
	 */
	public static List<SupplierInfo> getSupplierInfoList(String orderName, String filterName) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		DetachedCriteria dc = supplierInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("companyId", companyId));
		List<SupplierInfo> supplierInfoList = new ArrayList<SupplierInfo>();
		if (!StringUtils.isBlank(orderName)) {
			// dc.addOrder(Order.asc(orderName));
			dc.add(Restrictions.sqlRestriction(" 1=1 order by convert("
					+ orderName + " USING gbk) "));
		} else {
			dc.addOrder(Order.desc("id"));
		}
		supplierInfoList = supplierInfoDao.find(dc);
		return supplierInfoList;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getSupplierInfoList(Long companyId, String orderName) {
		List<Map<String, Object>> supplierInfoList = (List<Map<String, Object>>) getCache(CACHE_SUPPLIER_LIST);
		if (supplierInfoList == null) {
			StringBuffer sql = new StringBuffer("select id, supplierName from supplier_info where companyId = ? and delFlag = 0 ");
			if (StringUtils.isNotBlank(orderName)) {
				sql.append("ORDER BY CONVERT(" + orderName + " USING gbk)");
			} else {
				sql.append("ORDER BY id");
			}
			supplierInfoList = supplierInfoDao.findBySql(sql.toString(), Map.class, companyId);
			putCache(CACHE_SUPPLIER_LIST, supplierInfoList);
		}
		return supplierInfoList;
	}

	/**
	 *
	* @Title: getUserByRoleId
	* @Description: TODO(根据计调主管角色查询出该渠道计调主管的对象)
	* @param @param companyId
	* @return List<User>    返回类型
	* @throws
	 */
	public static List<User> getUserByRoleId(Long companyId) {
		//获取当前渠道计调主管的角色对象
		List<Role> roleList = roleDao.findRoleByCompanyIdAndRoleType(companyId,Context.ROLE_TYPE_OP_EXECUTIVE);
		List<User> userList = new ArrayList<User>();
		if(roleList != null) {
			for (Role role : roleList) {
				userList = userDao.getUserByRoleId(role.getId());
			}
		}
		return userList;
	}

	/**
	 * 批发商查询
	 *
	 * @param id
	 *            批发商id
	 * @return
	 */
	public static String getOfficeById(Long id) {
		if (id == null) {
			return "无此批发商数据";
		}
		Office office = officeDao.findOne(id);
		if (office == null) {
			return "无此批发商数据";
		}
		return office.getName();
	}

	/**
	 * 功能：销售 列表 add by chy 2014年11月14日15:14:10
	 */
	public static List<User> getSaleUserList(String userType) {

//		if (userType == null || "".equals(userType)) { //不再过滤userType 取出所有的用户作为查询结果 modify by chy 2015年8月25日14:57:50
//			return null;
//		}
		return userDao.findSalerListByUserType(userType);
	}

	/**
	 * 功能：签证销售/计调列表(仅签证可用)
	 */
	public static List<User> getVisaSaleUserList(String userType) {
		if (userType == null || "".equals(userType)) {
			return null;
		}
		if(UserUtils.getUser().getCompany().getUuid().equals("33ab2de5fdc842caba057296b28f5bae")){
			List<User> userListLX = new ArrayList<User>();
			userListLX.add(UserUtils.getUser());
			return userListLX;
		}
		return userDao.findVisaSalerListByUserType(userType);
	}

	/**
	 * 根据用户姓名查询当前批发商下该名称的用户(列表)
	 */
	public static List<User> getUserListByName(String userName) {
		List<User> userList = new ArrayList<User>();
		if(StringUtils.isNotBlank(userName)) {
			Long companyId = getUser().getCompany().getId();
			userList = userDao.findByName(userName, companyId);
			if(userList.isEmpty()) {
				User u = new User();
				//表示没有这个用户
				u.setId(-1L);
				u.setName(userName);
				userList.add(u);
			}
		}
		return userList;
	}

	// ============== User Cache ==============

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}

	public static Map<String, Object> getCacheMap() {
		Map<String, Object> map = Maps.newHashMap();
		try {
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal) subject.getPrincipal();
			return principal != null ? principal.getCacheMap() : map;
		} catch (UnavailableSecurityManagerException e) {
			return map;
		}
	}
	/**
	 * 更改登录状态
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static boolean changeLoginStatus(User user) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		if (userDao.updateLoginStatus(user.getId(),user.getLoginStatus())==0) {
			return false;
		}
		return true;
	}

	/**
	 * 审批重构-所有产品发布时部门树带出账号添加时选择的部门，默认显示第一个。相关表：sys_user_dept_job_new
	 * @author ang.gao
	 * @date 2015-12-18
	 * @return
	 */
	public static List<Long> getDepartmentByJobNew(){
		long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();

		return userDeptJobNewDao.findByUserIdAndCompanyUuid(userId, companyUuid);
	}


	/**
	 * 通过职务 获取当前用户可以提交审核业务 的部门列表，只有 level=2 的部门才能提交审核
	 */
	public static Set<Department> getDepartmentByJob() {
	         /*
			  long userId = UserUtils.getUser().getId();			  
			  List<UserJob> userJobList = new ArrayList<UserJob>();			 
			  userJobList=userJobDao.getUserJobList(userId); 
			  Set<Department> departmentSet = new HashSet<Department>();
			  for (UserJob userjob : userJobList) { 		  
				  //只有分公司下的某个部门(deptLevel=2)才能提交审核
				  if(userjob.getDeptLevel()==2){
					  Department dept=departmentDao.findOne(userjob.getDeptId());					 
					  departmentSet.add(dept);
				  }		  
			  }	*/
		  long userId = UserUtils.getUser().getId();
		  List<Long> userJobList = new ArrayList<Long>();
		  userJobList=userDeptJobDao.findDeptList(userId);

		  Set<Long> deptSet = new HashSet<Long>();
		  for (Long userDept : userJobList) {
			  deptSet.add(userDept);
		  }
		  Set<Department> departmentSet = new HashSet<Department>();
		  for (Long userDept : deptSet) {
			  Department dept=departmentDao.findOne(userDept);
			  if (dept.getLevel()==2){
			   departmentSet.add(dept);
			  }
		  }
		  return departmentSet;
}

	/**
	 * 获取青岛凯撒出境产品事业部部门列表
	 *
	*/
	public static List<Department> getCrtsdeptCodeSet() {
		Department department = departmentDao.findByName("出境产品事业部", 72L);
		List<Department> departmentSet = null;
		if(department != null){
			departmentSet = departmentDao.findByParentId(department.getId());
		}
		return departmentSet;
	}


	/**
	 * 获得当前用户部门列表（审核用）
	 *
	*/
	public static List<Department> getUserDept() {
		  long userId = UserUtils.getUser().getId();
		  List<Long> userJobList = new ArrayList<Long>();
		  userJobList=userDeptJobDao.findDeptList(userId);

		  Set<Long> deptSet = new HashSet<Long>();
		  for (Long userDept : userJobList) {
			  deptSet.add(userDept);
		  }

		  List<Department> departmentSet = new ArrayList<Department>();
		  for (Long userDept : deptSet) {
			  Department dept=departmentDao.findOne(userDept);
			  departmentSet.add(dept);
		  }
		  return departmentSet;
}
	
	/**
	 * 通过角色 获取当前用户的部门列表
	 * @author jiachen
	 * @DateTime 2015年2月4日 下午12:09:16
	 * @return Set<Department>
	 */
	public static Set<Department> getUserDepartment() {
		//获取当前用户的角色列表
		List<Role> roleList = getUser().getRoleList();
		//通过用户角色获取部门列表
		if(!roleList.isEmpty()) {
			Set<Department> departmentSet = new HashSet<Department>();
			for(Role role : roleList) {
				Department dept = role.getDepartment();
				if(null != dept) {
					departmentSet.add(dept);
				}
			}
			return departmentSet;
		}else{
			return new TreeSet<Department>();
		}
	}

	/**
	 * 通过角色获取当前用户的部门列表用户发布产品(只获取部门等级为2的)
	 * @author jiachen
	 * @DateTime 2015年2月4日 下午12:09:16
	 * @return Set<Department>
	 */
	public static Set<Department> getUserDepartmentToFoundActivity() {
		//获取当前用户的角色列表
		List<Role> roleList = getUser().getRoleList();
		//通过用户角色获取部门列表
		if(!roleList.isEmpty()) {
			Set<Department> departmentSet = new HashSet<Department>();
			for(Role role : roleList) {
				Department dept = role.getDepartment();
				//只有部门等级为2的才能发布产品
				if(null != dept && 2 == dept.getLevel()) {
					departmentSet.add(dept);
				}
			}
			return departmentSet;
		}else{
			return new TreeSet<Department>();
		}
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-05-18
	 * describe 获取当前用户所在供应商部门以及部门人员列表树
	 * @return
	 */
	public static String getAllDepartmentUsersByCompanyId(){
		JSONArray json = new JSONArray();
		Office office =UserUtils.getUser().getCompany();
		List<User> userList = userDao.getUserByCompany(office);
		List<Department> deptList = departmentDao.findByOfficeId(office.getId());
		if(null != deptList && 0 < deptList.size()){
			for(Department dept :deptList){
				String tid = dept.getId()+"_d";
				String tpid = dept.getParentId()+"_d";
				for(User u : userList){
					boolean flag = false;
					for(Role r:u.getRoleList()){
						if(r.getDepartment().getId().longValue() == dept.getId().longValue()){
							flag = true;
						}
					}
					if(flag){
						JSONObject jo = new JSONObject();
		                jo.put("id", u.getId()+"");
		                jo.put("pId", tid);
		                jo.put("name", u.getName());
		                json.add(jo);
					}
				}
				List<User> deptUserList = userDao.getUserByDepartment(office.getId(), dept.getId());
				if(CollectionUtils.isNotEmpty(deptUserList)) {
					JSONObject jo = new JSONObject();
	                jo.put("id", tid);
	                jo.put("pId", tpid);
	                jo.put("name", dept.getName());
	                json.add(jo);
				}
			}
		}
		return json.toString();
	}
	/**
	 * 获取计调和产品发布人
	 * @author zzk
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getOperators(Long companyId) {
		List<Map<String, Object>> userList = (List<Map<String, Object>>) getCache(CACHE_USER_LIST);
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT a.id, a.name from (")
				.append(" SELECT u.id,u.name from sys_user u where u.delFlag=0 and u.id in (select userId from sys_user_role where roleId in (select id from sys_role where roleType in (3, 4))) and u.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from travelactivity p, sys_user su where p.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from visa_products p, sys_user su where p.createBy=su.id and p.productStatus=2 and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_airticket p, sys_user su where p.createBy=su.id and p.productStatus=2 and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_hotel p, sys_user su where p.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_island p, sys_user su where p.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" ) a;");
		if(userList == null) {
			userList = userDao.findBySql(sb.toString(), Map.class);
			putCache(CACHE_USER_LIST, userList);
		}
		return userList;
	}

	/**
	 * 获取销售人员和下单人
	 * @author zzk
	 * @param companyId
	 * @return
	 */
	public static List<Map<String, Object>> getSalers(Long companyId) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT a.id, a.name from (")
				.append(" SELECT u.id,u.name from sys_user u where u.delFlag=0 and u.id in (select userId from sys_user_role where roleId in (select id from sys_role where roleType in (1, 2))) and u.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from travelactivity p, productorder o, sys_user su where p.id=o.productId and o.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from visa_products p, visa_order o, sys_user su where p.id=o.visa_product_id and p.productStatus=2 and o.create_by=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_airticket p, airticket_order o, sys_user su where p.id=o.airticket_id and p.productStatus=2 and o.create_by=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_hotel p, hotel_order o, sys_user su where p.uuid=o.activity_hotel_uuid and o.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_island p, island_order o, sys_user su where p.uuid=o.activity_island_uuid and o.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(") a;");
		List<Map<String, Object>> userList = userDao.findBySql(sb.toString(), Map.class);
		return userList;
	}

	/**
	 * 按人员姓氏拼音排序
	 * @param String[]
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String[] getSortOfChinese(String[] a) {
        // Collator 类是用来执行区分语言环境这里使用CHINA
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        // JDK自带对数组进行排序。
        Arrays.sort(a, cmp);
        return a;
    }

	/**
	 * 判断当前用户是否拥有某个角色
	 * @param user				当前用户
	 * @param roleType			角色类型
     * @return
	 * @author shijun.liu
	 * @date   2016.06.15
     */
	public static boolean hasRole(User user, String roleType){
		boolean hasRole = false;
		List<Role> list = user.getRoleList();
		if(Collections3.isEmpty(list)){
			return hasRole;
		}
		for (Role role : list){
			if(roleType.equals(role.getRoleType())){
				hasRole = true;
				break;
			}
		}
		return hasRole;
	}
	/**
	 * 判断是否为美图公司用户：true 是；false 否
	 * @return
	 */
	public static boolean isMtourUser() {
		Office office = getUser().getCompany();
		if(meituCompanyUuids.contains(office.getUuid())){
			return true;//美图公司用户
		}else{
			return false;
		}
    }

	/**
	 * 判断是否为华尔公司用户：true 是；false 否
	 * @return
	 */
	public static boolean isHuaerUser() {
		Office office = getUser().getCompany();
		if(huaerCompanyUuids.contains(office.getUuid())){
			return true;//华尔公司用户
		}else{
			return false;
		}
	}
	
	/**
	 * 依据id字符串获取渠道的销售
	 * @param salerIdStr
	 * @return
	 */
	public static Map<String, Object> getSalersFromIdStr(String salerIdStr) {
		Map<String, Object> salerMap = new HashMap<>();
		// 声明 value
		List<User> salerList = new ArrayList<>();
		String salerNameStr = "";
		String salerNameStrWithStop = "";
		String[] salerIdArray = null;
		// value赋值
		if (StringUtils.isNotBlank(salerIdStr)) {
			salerIdArray = agentinfoService.getIdArrayFromSalerIdStr(salerIdStr);
			if (salerIdArray != null && salerIdArray.length > 0) {
				for (int i = 0; i < salerIdArray.length; i++) {
					User tempUser = userDao.findById(Long.parseLong(salerIdArray[i]));
					if (tempUser != null) {
						salerList.add(tempUser);
						if (i != 0) {
							salerNameStr += ",";
							salerNameStrWithStop += "、";
						}
						salerNameStr += tempUser.getName();
						salerNameStrWithStop += tempUser.getName();
					}
				}
			}
		}
		salerMap.put("salerList", salerList);
		salerMap.put("salerIdStr", salerIdStr);
		salerMap.put("salerIdArray", salerIdArray);
		salerMap.put("salerNameStr", salerNameStr);  // 以逗号分割的销售名字
		salerMap.put("salerNameStrWithStop", salerNameStrWithStop);  // 以顿号分割的销售名字
		
		return salerMap;
	}

	public static List<Map<String, Object>> getSalerNameAndPhone(Long companyId) {
		return userDao.getSalerNameAndPhone(companyId);
	}

	/**
	 * 获取可以quauq下单的销售信息。
	 * @param companyId
     * @return
	 * @author yudong.xu 2016.9.20
     */
	public static List<Map<String, Object>> getSalerInfoList(Long companyId) {
		return userDao.getSalerInfoList(companyId);
	}
	

	/**
	 * 判断是否是计调主管
	 * @author yakun.bai
	 * @Date 2016-6-21
	 */
	public static boolean isOpManager() {
		return hasRole(getUser(), Context.ROLE_TYPE_OP_EXECUTIVE);
	}

	/**
	 * 判断是否在担保变更审批中，是返回“yes”，否返回“no”
	 * @param travelerId
	 * @return
     */
	public static String isReviewing(Long travelerId, String orderId) {
		return guaranteeService.isReviewing(travelerId, orderId);
	}

	/**
	 * 根据用户列表返回其名称组合
	 * @param users
	 * @return
	 * @author shijun.liu
     */
	public static String getUserNames(List<User> users){
		StringBuffer str = new StringBuffer();
		if(Collections3.isEmpty(users)){
			return " ";
		}
		for(User user : users){
			str.append(user.getName()).append(",");
		}
		if(str.length() != 0){
			str.delete(str.length()-1, str.length());
		}
		return str.toString();
	}

}
