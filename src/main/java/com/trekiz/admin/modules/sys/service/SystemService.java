package com.trekiz.admin.modules.sys.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.mobile.entity.MobileUser;
import com.trekiz.admin.modules.mobile.repository.MobileUserDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.subject.WebSubject;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quauq.multi.tenant.manage.service.TenantUtil;
import com.quauq.multi.tenant.util.MultiTenantUtil;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.security.Base64Util;
import com.trekiz.admin.common.security.Digests;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.Encodes;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.SysJobNew;
import com.trekiz.admin.modules.sys.entity.SysUserReviewCommonPermission;
import com.trekiz.admin.modules.sys.entity.SysUserReviewProcessPermission;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserDeptJobNew;
import com.trekiz.admin.modules.sys.entity.UserReviewPermissionResultForm;
import com.trekiz.admin.modules.sys.model.MenuJsonBean;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.MenuDao;
import com.trekiz.admin.modules.sys.repository.RoleDao;
import com.trekiz.admin.modules.sys.repository.SysJobNewDao;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.repository.UserDeptJobNewDao;
import com.trekiz.admin.modules.sys.repository.UserPermissionDao;
import com.trekiz.admin.modules.sys.repository.UserProcessPermissionDao;
import com.trekiz.admin.modules.sys.security.SystemAuthorizingRealm;
import com.trekiz.admin.modules.sys.transfer.MenuTransfer;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author zj
 * @version 2013-11-19
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired 
	private UserPermissionDao userPermissionDao;
	@Autowired
	private UserProcessPermissionDao userProcessPermissionDao;
	@Autowired
	private SysJobNewDao sysJobNewDao;
	@Autowired
	private UserDeptJobNewDao userDeptJobNewDao;
	@Autowired
	private MobileUserDao mobileUserDao;

	//-- User Service --//
	
	public User getUser(Long id) {
		return userDao.findOne(id);
	}
	
	public Page<User> findUser(Page<User> page, User user, HttpServletRequest request) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		dc.createAlias("company", "company");
		if (user.getCompany()!=null && user.getCompany().getId()!=null){
			dc.add(Restrictions.eq("company.id", user.getCompany().getId()));
		}
		if (!currentUser.isAdmin()){
			// 如果不是超级管理员，则不显示超级管理员用户
			dc.add(Restrictions.ne("id", 1L)); 
			// 默认只显示同一个供应商下面的用户
			dc.add(Restrictions.eq("company.id", currentUser.getCompany().getId()));
		}
		if (StringUtils.isNotEmpty(user.getLoginName())){
			dc.add(Restrictions.like("loginName", "%" + user.getLoginName() + "%"));
		}
		if (StringUtils.isNotEmpty(user.getName())){
			dc.add(Restrictions.like("name", "%" + user.getName() + "%"));
		}
		//添加微信号模糊搜索
		if (StringUtils.isNotEmpty(user.getWeixin())){
			dc.add(Restrictions.like("weixin", "%" + user.getWeixin() + "%"));
		}
		
		dc.add(Restrictions.eq(User.DEL_FLAG, User.DEL_FLAG_NORMAL));
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.asc("company.code")).addOrder(Order.desc("id"));
		}
		
		
		// 部门查询
		String deptId = request.getParameter("department");
		if (StringUtils.isNotBlank(deptId)){
			String sql = "SELECT user_id FROM sys_user_dept_job_new WHERE dept_id = " + deptId;
			List<Integer> userIds = userDao.findBySql(sql);
			List<Long> userIdList = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(userIds)) {
				for (Integer id : userIds) {
					userIdList.add(id.longValue());
				}
				dc.add(Restrictions.in("id", userIdList));
			}
		}
		// 职务查询
		String jobId = request.getParameter("job");
		if (StringUtils.isNotBlank(jobId)){
			String sql = "SELECT user_id FROM sys_user_dept_job_new WHERE job_id = " + jobId;
			List<Integer> userIds = userDao.findBySql(sql);
			List<Long> userIdList = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(userIds)) {
				for (Integer id : userIds) {
					userIdList.add(id.longValue());
				}
				dc.add(Restrictions.in("id", userIdList));
			}
		}
		// 角色查询
		String roleId = request.getParameter("role");
		if (StringUtils.isNotBlank(roleId)){
			String sql = "SELECT userId FROM sys_user_role WHERE roleId = " + roleId;
			List<BigInteger> userIds = userDao.findBySql(sql);
			List<Long> userIdList = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(userIds)) {
				for (BigInteger id : userIds) {
					userIdList.add(id.longValue());
				}
				dc.add(Restrictions.in("id", userIdList));
			}
		}
		
		return userDao.find(page, dc);
	}
	
	/**
	 * 角色查询
	 * @param page
	 * @param role
	 * @return
	 */
	public Page<Role> findRole(Page<Role> page, Role role) {
		page.setPageSize(30);
		DetachedCriteria dc = roleDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		if (role.getCompanyId() != null) {
			dc.add(Restrictions.eq("companyId", role.getCompanyId()));
		}
		if (!currentUser.isAdmin()){
			// 默认只显示同一个供应商下面的用户
			dc.add(Restrictions.eq("companyId", currentUser.getCompany().getId()));
		}
		if (StringUtils.isNotEmpty(role.getName())){
			dc.add(Restrictions.like("name", "%" + role.getName() + "%"));
		}
		dc.add(Restrictions.eq(User.DEL_FLAG, User.DEL_FLAG_NORMAL));
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.asc("companyId")).addOrder(Order.asc("createDate"));
		}
		return roleDao.find(page, dc);
	}

	public User getUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}
	
	public List<User> getUserByGroupeSurname(String groupeSurname,Long companyId) {
		return userDao.findByGroupeSurname(groupeSurname,companyId);
	}
	
	public User getUserByNo(String no) {
		return userDao.findByNo(no);
	}

	/**
	 * 产品、报名、订单、运控等与部门挂钩的查询权限
	 * 
	 * @param sqlType 查询类型：例如产品、订单，用英文代表
	 * @param dc 查询对象：有可能为空，所以需判断为空，仅在签证查询中沈阳查看北京加拿大产品时候为空
	 * @param sqlWhere 查询条件
	 * @param common 部门公用对象
	 * @param orderType 订单类型
	 */
	public List<Criterion> getDepartmentSql(String sqlType, DetachedCriteria dc, StringBuffer sqlWhere, DepartmentCommon common, Integer orderType) {
		
		/** 要查询部门ID */
		String departmentId = common.getDepartmentId();
		/** 查询条件List：仅为了签证查询中沈阳查看北京加拿大产品而创建 */
		List<Criterion> whereList = null;
		
		if (StringUtils.isNotBlank(departmentId)) {
			
			/** 产品查询处理：产品模块、预定模块、运控模块 */
			if("activity".equals(sqlType)) {
				whereList = Lists.newArrayList();
				getActivitySql(dc, common, whereList, orderType);
			}
			
			/** 订单查询处理 */
			else if("order".equals(sqlType)) {
				getOrderSql(common, sqlWhere, orderType);
			}  
			
			/** 询价查询处理 */
			else if("inquiry".equals(sqlType)) {
				getInquirySql(common, sqlWhere, orderType);
			}
		}
		
		return whereList;
	}
	
	/**
	 * 添加查询产品条件
	 * @param dc
	 * @param common
	 * @param whereList 查询条件
	 */
	private void getActivitySql(DetachedCriteria dc, DepartmentCommon common, List<Criterion> whereList, Integer orderType) {
		
		//要查询部门ID
		String departmentId = common.getDepartmentId();
		//部门之间人员是否能相互查看，默认不能
		boolean flag = whetherSelectAllDeptDate(common);
		
		String queryId = "id";
		if ("cost".equals(common.getCheckType())) {
			queryId = "srcActivityId";
		}
		
		//如果只有计调角色
		if (common.getIsOP() && !common.getIsOPManager() && !(common.getIsSale() || common.getIsSaleManager()) && !common.getIsManager()) {
			if (flag) {
				addDeptUserSql(dc, departmentId, false, true, whereList, orderType, queryId);
			} else {
				if (dc != null) {
					if (orderType != null && (orderType < 6 || orderType == 10)) {
						List<Integer> opUserList = departmentDao.findActivityIdsByOpUserId(UserUtils.getUser().getId());
						if(opUserList.size()>0){
							dc.add(Restrictions.or(
									Restrictions.eq("createBy", UserUtils.getUser()), 
									Restrictions.in(queryId, IntegerTransformLong(opUserList))));
						}else{
						   dc.add(Restrictions.eq("createBy", UserUtils.getUser()));	
						}
						
					} else {
						dc.add(Restrictions.eq("createBy", UserUtils.getUser()));
					}
				}
				whereList.add(Restrictions.eq("createBy", UserUtils.getUser()));
			}
		} else {
			if (!common.getIsManager() || !common.getIsSelfDept()) {
				if ((common.getIsSale() || common.getIsSaleManager()) && !common.getIsOPManager()) {
					
					//单团、散拼、游学、大客户、自由行预定查询
					if ("bookOrder".equals(common.getCheckType())) {
						List<User> userList = Lists.newArrayList();
						userList.addAll(addDeptUserSql(dc, departmentId, true, false, whereList, orderType, queryId));
						addDeptUserSql(dc, userList, whereList, orderType, true, queryId);
					} 
					//签证查询
					else if ("visaBookOrder".equals(common.getCheckType())) {
						Department dept = departmentDao.findOne(Long.parseLong(departmentId));
						//如果是武汉部门下销售经理或销售，则能查看所有北京签证产品
						List<User> userList = Lists.newArrayList();
						if ((dept.getId() == 26 || (dept.getParent() != null && dept.getParentId() == 26))) {
							userList.addAll(addDeptUserSql(dc, "16", true, false, whereList, orderType, queryId));
						}
						if (dept.getLevel() > 1) {
							userList.addAll(addDeptUserSql(dc, dept.getParent().getId().toString(), true, false, whereList, orderType, queryId));
						} else {
							userList.addAll(addDeptUserSql(dc, departmentId, true, false, whereList, orderType, queryId));
						}
						addDeptUserSql(dc, userList, whereList, orderType, false, queryId);
						common.setDept(dept);
					} else {
						//既是销售又是计调情况：产品查询
						if (flag) {
							addDeptUserSql(dc, departmentId, false, true, whereList, orderType, queryId);
						} else {
							if (dc != null) {
								if (orderType != null && (orderType < 6 || orderType == 10)) {
									List<Integer> opUserList = departmentDao.findActivityIdsByOpUserId(UserUtils.getUser().getId());
									if(opUserList.size()>0){
										dc.add(Restrictions.or(
												Restrictions.eq("createBy", UserUtils.getUser()), 
												Restrictions.in(queryId, IntegerTransformLong(opUserList))));	
									}else{
										dc.add(Restrictions.eq("createBy", UserUtils.getUser()));	
									}
									
								} else {
									dc.add(Restrictions.eq("createBy", UserUtils.getUser()));
								}
							}
							whereList.add(Restrictions.eq("createBy", UserUtils.getUser()));
						}
					}
				} else {
					//单团、散拼、游学、大客户、自由行预定查询
					if ("bookOrder".equals(common.getCheckType())) {
						List<User> userList = Lists.newArrayList();
						userList.addAll(addDeptUserSql(dc, departmentId, true, false, whereList, orderType, queryId));
						addDeptUserSql(dc, userList, whereList, orderType, true, queryId);
					} else {
						addDeptUserSql(dc, departmentId, true, true, whereList, orderType, queryId);
					}
				}
			} 
		}
	}
	
	/**
	 * 获取订单查询条件
	 * @param common
	 * @param sqlWhere
	 * @param orderType
	 */
	private void getOrderSql(DepartmentCommon common, StringBuffer sqlWhere, Integer orderType) {

		/** 要查询部门ID */
		String departmentId = common.getDepartmentId();
		/** 部门之间人员是否能相互查看，默认不能*/
		boolean flag = whetherSelectAllDeptDate(common);
		/** 是否是管理员 */
		boolean isManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_MANAGER);
		/** 是否是计调经理 */
		boolean isOPManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_OP_EXECUTIVE);
		/** 是否是计调 */
		boolean isOP = common.getRoleTypeList().contains(Context.ROLE_TYPE_OP);
		/** 是否是销售经理 */
		boolean isSaleManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES_EXECUTIVE);
		/** 是否是销售 */
		boolean isSale = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES);
		
		//不是管理员（默认查看所有）处理：计调经理、计调、销售经理、销售
		if (!isManager) {	
			Set<Long> saleIds = Sets.newHashSet();
    		Set<Long> opIds = Sets.newHashSet();
			//计调经理
			if (isOPManager) {
				getCreateUserIds(departmentId, opIds, flag, Context.ROLE_TYPE_OP_EXECUTIVE);
			}
			//销售经理
			if (isSaleManager) {
				getCreateUserIds(departmentId, saleIds, flag, Context.ROLE_TYPE_SALES_EXECUTIVE);
			}
			//不是计调经理且不是销售经理
			if (!isSaleManager && !isOPManager && (isOP || isSale)) {
				getCreateUserIds(departmentId, opIds, flag, Context.ROLE_TYPE_OP);
				getCreateUserIds(departmentId, saleIds, flag, Context.ROLE_TYPE_SALES);
			}
			
			if (!isOPManager && !isOP && !isSaleManager && !isSale) {
				getCreateUserIds(departmentId, saleIds, flag, Context.ROLE_TYPE_SALES_EXECUTIVE);
				getCreateUserIds(departmentId, opIds, flag, Context.ROLE_TYPE_OP_EXECUTIVE);
			}
			
			handleSql(common.getCheckType(), saleIds, opIds, sqlWhere, orderType);
		}
	}
	
	private void getInquirySql(DepartmentCommon common, StringBuffer sqlWhere, Integer orderType) {
		
		/** 要查询部门ID */
		String departmentId = common.getDepartmentId();
		/** 部门之间人员是否能相互查看，默认不能*/
		boolean flag = whetherSelectAllDeptDate(common);
		/** 是否是管理员 */
		boolean isManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_MANAGER);
		/** 是否是销售经理 */
		boolean isSaleManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES_EXECUTIVE);
		/** 是否是销售 */
		boolean isSale = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES);

		//不是管理员（默认查看所有）处理：销售经理、销售
		if(!isManager) {	
			Set<Long> saleIds = Sets.newHashSet();
			//销售经理
			if(isSaleManager) {
				getCreateUserIds(departmentId, saleIds, flag, Context.ROLE_TYPE_SALES_EXECUTIVE);
			}
			//不是销售经理且是销售
			if(!isSaleManager && isSale) {
				getCreateUserIds(departmentId, saleIds, flag, Context.ROLE_TYPE_SALES);
			}
			
			if(!isSaleManager && !isSale) {
				getCreateUserIds(departmentId, saleIds, flag, Context.ROLE_TYPE_SALES_EXECUTIVE);
			}
			handleSql("inquiry", saleIds, null, sqlWhere, orderType);
			
		}
	}
	
	/**
	 * 判断此部门是否允许人员相互查看数据：
	 * 					获取有此权限标示的菜单；然后查看此部门可以相互查看的菜单列表；
	 * 					如果此菜单列表中包含这个菜单则表示允许相互查看
	 * @param common
	 * @return
	 */
	private boolean whetherSelectAllDeptDate(DepartmentCommon common) {
		
		/** 判断是否是部门之间人员可以相互查看请求 */
		List<Menu> menuList = menuDao.findByPermission(common.getPermission());//查询请求的菜单
		Department dept = departmentDao.findOne(Long.parseLong(common.getDepartmentId()));//查询用于查询的部门
		if (CollectionUtils.isNotEmpty(menuList)) {
    		List<Integer> list = departmentDao.findSelectIdsByDeptId(dept.getId());
    		if (CollectionUtils.isNotEmpty(list)) {
    			return list.contains(menuList.get(menuList.size() - 1).getId().intValue());
    		}
    	}
		return false;
	}
	
	/**
	 * 添加部门用户查询条件
	 * @param dc
	 * @param departmentId
	 * @param isAllDeptUser 是否查询本部门下子部门用户
	 * @param isAddSql 是否添加sql
	 * @param whereList where条件
	 * @param orderType 产品类型
	 * @return
	 */
	private List<User> addDeptUserSql(DetachedCriteria dc, String departmentId, boolean isAllDeptUser, boolean isAddSql, List<Criterion> whereList, Integer orderType, String queryId) {
		
		//部门中用户
		List<User> userList = null;
		//查询部门下所有子部门中用户和自己部门中用户
		if (isAllDeptUser) {
			userList = userDao.getAllUserByDepartment(UserUtils.getUser().getCompany().getId(), Long.parseLong(departmentId), "%," + departmentId + ",%");
		} 
		//查询当前部门下人员，不包括子部门
		else {
			userList = userDao.getUserByDepartment(UserUtils.getUser().getCompany().getId(), Long.parseLong(departmentId));
		}
		if (isAddSql) {
			addDeptUserSql(dc, userList, whereList, orderType, false, queryId);
		}
    	return userList;
	}
	
	/**
	 * 添加部门用户查询条件
	 * @param dc
	 * @param userList    用户list
	 * @param whereList   where条件
	 * @param orderType   订单类型
	 * @param isBookOrder 是否是订单预定
	 */
	private void addDeptUserSql(DetachedCriteria dc, List<User> userList, List<Criterion> whereList, Integer orderType, boolean isBookOrder, String queryId) {
		if (CollectionUtils.isNotEmpty(userList)) {
			if (dc != null) {
				if (orderType != null && (orderType < 6 || orderType == 10)) {
					if (isBookOrder) {
						List<Long> allOpUserIdList = IntegerTransformLong(departmentDao.findActivityIds());
						
						if (CollectionUtils.isNotEmpty(allOpUserIdList)) {
							List<Long> OpUserIdList = IntegerTransformLong(departmentDao.findActivityIdsByOpUserId(UserUtils.getUser().getId()));
							if (CollectionUtils.isNotEmpty(OpUserIdList)) {
								dc.add(Restrictions.or(
										Restrictions.and(
												Restrictions.in("createBy", userList), 
												Restrictions.not(
														Restrictions.in("id", allOpUserIdList))), 
												Restrictions.in(queryId, OpUserIdList), 
										Restrictions.eq("createBy", UserUtils.getUser())));
							} else {
								dc.add(Restrictions.or(
										Restrictions.and(
												Restrictions.in("createBy", userList), 
												Restrictions.not(
														Restrictions.in(queryId, allOpUserIdList))), 
										Restrictions.eq("createBy", UserUtils.getUser())));
							}
						} else {
							dc.add(Restrictions.eq("createBy", UserUtils.getUser()));
						}
					} else {
						List<Integer> opUserList = departmentDao.findActivityIdsByOpUserId(UserUtils.getUser().getId());
						if (CollectionUtils.isNotEmpty(opUserList)) {
							dc.add(Restrictions.or(
									Restrictions.in("createBy", userList), 
									Restrictions.in(queryId, IntegerTransformLong(opUserList))));
						} else {
							dc.add(Restrictions.in("createBy", userList));
						}
						
					}
				} else {
					dc.add(Restrictions.in("createBy", userList));
				}
			}
    		whereList.add(Restrictions.in("createBy", userList));
    	} else {
    		if (dc != null) {
    			dc.add(Restrictions.sqlRestriction("1=2"));
    		}
    		whereList.add(Restrictions.sqlRestriction("1=2"));
    	}
	}
	
	/**
	 * Integer的List转成Long的List
	 * @param sourceList
	 * @return
	 */
	private List<Long> IntegerTransformLong(List<Integer> sourceList) {
		List<Long> targetList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(sourceList)) {
			for (Integer sourceId : sourceList) {
				targetList.add(sourceId.longValue());
			}
		}
		return targetList;
	}
	
	
	/**
	 * 得到部门中所有要查询人员的ID
	 * @param departmentId
	 * @param createByIds
	 * @param flag
	 * @param roleType
	 * @return
	 */
	private Set<Long> getCreateUserIds(String departmentId, Set<Long> createByIds, boolean flag, String roleType) {
		List<User> userList = Lists.newArrayList();
		if (flag) {
			//如果是计调经理或销售经理，则查询本部门所有计调或销售及对应子元素
			if (Context.ROLE_TYPE_OP_EXECUTIVE.equals(roleType) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(roleType)) {
				userList = userDao.getAllUserByDepartment(UserUtils.getUser().getCompany().getId(), Long.parseLong(departmentId), "%," + departmentId + ",%");
			} else {
				userList = userDao.getUserByDepartment(UserUtils.getUser().getCompany().getId(), Long.parseLong(departmentId));
			}
			
    		for (int i=0;i<userList.size();i++) {
    			createByIds.add(userList.get(i).getId());
    		}
		} else {
			//如果是计调则只查询自己产品产生订单，如果是销售则只查询自己下的订单
			if (Context.ROLE_TYPE_OP.equals(roleType) || Context.ROLE_TYPE_SALES.equals(roleType)) {
				createByIds.add(UserUtils.getUser().getId());
			}
			//如果是计调主管或销售主管则查询所有本部门下人员再分类
			if (Context.ROLE_TYPE_OP_EXECUTIVE.equals(roleType) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(roleType)) {
				userList = userDao.getAllUserByDepartment(UserUtils.getUser().getCompany().getId(), Long.parseLong(departmentId), "%," + departmentId + ",%");
				
				//销售主管只能看销售下的单，计调主管只能看计调产品产生订单
				for (int i=0;i<userList.size();i++) {
					//如果是操作主管，则查看其下面所有计调及其自己产生订单
					
					if (Context.ROLE_TYPE_OP_EXECUTIVE.equals(roleType)) {
						for (Role role : userList.get(i).getRoleList()) {
							if (Context.ROLE_TYPE_OP.equals(role.getRoleType()) || Context.ROLE_TYPE_OP_EXECUTIVE.equals(role.getRoleType())) {
								createByIds.add(userList.get(i).getId());
								break;
							}
						}
					} else if(Context.ROLE_TYPE_SALES_EXECUTIVE.equals(roleType)) {
						for (Role role : userList.get(i).getRoleList()) {
							if (Context.ROLE_TYPE_SALES.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
								createByIds.add(userList.get(i).getId());
								break;
							}
						}
					}
	    		}
			}
		}
		return createByIds;
	}
	
	/**
	 * 处理sql语句
	 * @param saleIds 销售IDS
	 * @param opIds 计调IDS
	 */
	private void handleSql(String sqlType, Set<Long> saleIds, Set<Long> opIds, StringBuffer sqlWhere, Integer orderType) {
		String saleWhere = "";//销售sql语句
		String opWhere = "";//计调sql语句
		
		/** 如果销售ids不为空则添加销售sql语句 */
		if (CollectionUtils.isNotEmpty(saleIds)) {
			String createByIds = "";
			Iterator<Long> it = saleIds.iterator();
			int i = 0;
			while (it.hasNext()) {
				if (i == saleIds.size() - 1) {
					createByIds += it.next();
				} else {
					createByIds += it.next() + ",";
				}
				i++;
			}
			
			if (StringUtils.isNotBlank(createByIds)) {
				if ("order".equals(sqlType)) {
					if (orderType != null && (orderType < 6 || orderType == 10)) {
						saleWhere +=  " (pro.createBy in (" + createByIds  + ") or salerId = " + UserUtils.getUser().getId() + ")";
					} else {
						saleWhere +=  " pro.createBy in (" + createByIds  + ")";
					}
					
				} else if("visaOrder".equals(sqlType)) {
					saleWhere +=  " vo.create_by in (" + createByIds  + ") or vo.salerId = " + UserUtils.getUser().getId();
				} else if ("airticketOrder".equals(sqlType)) {
					saleWhere +=  " ao.create_by in (" + createByIds  + ") or ao.salerId = " + UserUtils.getUser().getId();
				} else if ("inquiry".equals(sqlType)) {
					saleWhere +=  " p.userId in (" + createByIds  + ")";
				}
			}
		}
		
		/** 如果计调ids不为空则添加计调sql语句 */
		if (CollectionUtils.isNotEmpty(opIds)) {
			String createByIds = "";
			Iterator<Long> it = opIds.iterator();
			int i = 0;
			while (it.hasNext()) {
				if (i == opIds.size() - 1) {
					createByIds += it.next();
				} else {
					createByIds += it.next() + ",";
				}
				i++;
			}
			
			if (StringUtils.isNotBlank(createByIds)) {
				if ("order".equals(sqlType)) {
					opWhere +=  " activity.createBy in (" + createByIds  + ")";
				} else if ("visaOrder".equals(sqlType)) {
					opWhere +=  " vp.createBy in (" + createByIds  + ")";
				} else if ("airticketOrder".equals(sqlType)) {
					opWhere +=  " aa.createBy in (" + createByIds  + ")";
				}
			}
		}
		
		/** 综合处理sql语句 */
		if (StringUtils.isNotBlank(saleWhere) && StringUtils.isNotBlank(opWhere)) {
			sqlWhere.append(" and (" + saleWhere + " or" + opWhere + ")");
		} else if (StringUtils.isNotBlank(saleWhere) && !StringUtils.isNotBlank(opWhere)) {
			sqlWhere.append(" and " + saleWhere);
		} else if (!StringUtils.isNotBlank(saleWhere) && StringUtils.isNotBlank(opWhere)) {
			sqlWhere.append(" and " + opWhere);
		} else {
			sqlWhere.append(" and 1=2");
		}
	}
	
	/**
	 * 查询供应商某一部门所有用户
	 * @param companyId
	 * @param departmentId
	 * @return
	 */
	public List<User> getAllUserByDepartment(Long companyId, Long departmentId, String parentIds) {
		return userDao.getAllUserByDepartment(companyId, departmentId, parentIds);
	}
	
	/**
	 * 查询供应商下用户
	 * @param office
	 * @return
	 */
	public List<User> getUserByCompany(Office office) {
		return userDao.getUserByCompany(office);
	}
	
	/**
	 * 查询供应商下用户
	 * @return
	 */
	public List<User> getUserByCompanyId(Long officeId) {
		
		String Sql = "SELECT id,companyId,loginName,two_psw,name FROM sys_user WHERE delFlag = 0 AND companyId = " + officeId;
		List<Map<String, Object>> userMaps = userDao.findBySql(Sql, Map.class);
		List<User> users = new ArrayList<>();
		for (Map<String, Object> userMap : userMaps) {
			User user  = new User();
			user.setId(Long.parseLong(userMap.get("id").toString()));
//			user.setCompany(userDao.getOfficeByCompanyId(Long.parseLong(userMap.get("companyId") == null ? null : userMap.get("companyId").toString())));
			user.setLoginName(userMap.get("loginName")== null ? null : userMap.get("loginName").toString());
			user.setTwo_psw(userMap.get("two_psw") == null ? null : userMap.get("two_psw").toString());
			user.setName(userMap.get("name") == null ? null : userMap.get("name").toString());
			users.add(user);
		}
		return users;
	}
	
	/**
	 * 查询指定供应商、渠道的用户（用于查quauq渠道账号而创建）
	 * @author yang.jiang 2016-4-28 12:09:00
	 * @return
	 */
	public List<User> getUserByCompanyAndAgent(Long officeId, Long agentId) {
		if (officeId == null || agentId == null) {
			return null;
		}
		String Sql = "SELECT id,companyId,loginName,two_psw,name FROM sys_user WHERE delFlag = 0 AND companyId = " + officeId + " AND agentId = " + agentId;
		List<Map<String, Object>> userMaps = userDao.findBySql(Sql, Map.class);
		List<User> users = new ArrayList<>();
		for (Map<String, Object> userMap : userMaps) {
			User user  = new User();
			user.setId(Long.parseLong(userMap.get("id").toString()));
			user.setCompany(userDao.getOfficeByCompanyId(officeId));
			user.setAgentId(agentId);
			user.setLoginName(userMap.get("loginName")== null ? null : userMap.get("loginName").toString());
			user.setTwo_psw(userMap.get("two_psw") == null ? null : userMap.get("two_psw").toString());
			user.setName(userMap.get("name") == null ? null : userMap.get("name").toString());
			users.add(user);
		}
		return users;
	}
	
	public List<User> getAllUser() {
		return userDao.getAllUser();
	}
	
	public List<User> getAllUserByCompanyId(Long companyId){
		return userDao.getUserByCompanyId(companyId);
	}
	
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void saveUser(User user) {
		userDao.save(user);
		systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteUser(Long id) {
		userDao.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public List<User> findByLoginName() {
		return userDao.findByLoginName();
	}
	
	@Transactional(readOnly = false)
	public void updatePasswordById(Long id, String loginName, String newPassword) {
		userDao.updatePasswordById(entryptPassword(newPassword), Base64Util.getBase64(newPassword), id);
		systemRealm.clearCachedAuthorizationInfo(loginName);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(Long id) {
		ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();
        String clientIp= StringUtils.getRemoteAddr((HttpServletRequest)request);
        userDao.updateLoginInfo(clientIp, new Date(), id);
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	//-- Role Service --//
	
	public Role getRole(Long id) {
		return roleDao.findOne(id);
	}

	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}
	
	/**
	 * 判断添加或修改角色的时候，名称是否重复：添加的时候校验此供应商内是否有此角色名；修改的时候要去除本身再判断
	 * @param id
	 * @param companyId
	 * @param name
	 * @return
	 */
	public boolean findRoleByCompanyIdAndName(Long id, Long companyId, String name) {
		Role role;
		// id不为空时为修改，为空时则是保存
		if(id != null) {
			role = roleDao.findRoleByCompanyIdAndName(id, companyId, name);
		} else {
			role = roleDao.findRoleByCompanyIdAndName(companyId, name);
		}
		if(role == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断系统管理员是否属于总部
	 * @param deptId
	 * @return
	 */
	public boolean isHQ(Long deptId) {
		if (deptId != null) {
			Department dept = departmentDao.findOne(deptId);
			if (dept != null && dept.getLevel() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 用户角色是否合法判定
	 * @param roleList
	 * @return
	 */
	public String userRolesIsLegal(List<Role> roleList) {
		if(CollectionUtils.isNotEmpty(roleList) && roleList.size() >= 2) {
			int saleRoleTypeNum = 0;
			int saleDeptNum = 0;
			int opRoleTypeNum = 0;
			int opDeptNum = 0;
			boolean saleManagerNoDept = false;
			boolean opManagerNoDept = false;
			for(Role role : roleList) {
				Department dept = role.getDepartment();
				//系统管理员不与任何角色同属于用户
				if(Context.ROLE_TYPE_MANAGER.equals(role.getRoleType()) && dept != null && dept.getLevel() == 0) {
					return "一个用户不能拥有系统管理员之外的角色";
				}
				//统计计调角色类型及其部门数量
				if(Context.ROLE_TYPE_OP.equals(role.getRoleType()) || Context.ROLE_TYPE_OP_EXECUTIVE.equals(role.getRoleType())) {
					opRoleTypeNum++;
					if(role.getDepartment() != null) {
						opDeptNum++;
					}
				}
				//统计销售角色类型及其部门数量
				if(Context.ROLE_TYPE_SALES.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
					saleRoleTypeNum++;
					if(role.getDepartment() != null) {
						saleDeptNum++;
					}
				}
				
				//判断计调经理有没有所属部门
				if(Context.ROLE_TYPE_OP_EXECUTIVE.equals(role.getRoleType()) && dept == null) {
					opManagerNoDept = true;
				}
				//判断销售经理有没有所属部门
				if(Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType()) && dept == null) {
					saleManagerNoDept = true;
				}
			}
			
			//一个用户不能有两个或两个以上销售角色
//			if(saleRoleTypeNum > 1) {
//				return "用户不能同时拥有两个或两个以上销售角色";
//			}
			//销售角色判断：不允许多个销售角色部分有部门，其余没部门
			if(saleRoleTypeNum > 1 && saleDeptNum >= 1 && saleRoleTypeNum != saleDeptNum) {
				return "销售角色不允许一个有部门，另一个没有";
			}
			//计调角色判断：不允许多个计调角色部分有部门，其余没部门
			if(opRoleTypeNum > 1 && opDeptNum >= 1 && opRoleTypeNum != opDeptNum) {
				return "计调角色不允许一个有部门，另一个没有";
			}
			//销售经理或计调经理判断：不允许没部门的销售经理或计调经理和其余有部门的角色同属于一个用户
			if((saleManagerNoDept || opManagerNoDept) && (saleDeptNum >= 1 || opDeptNum >= 1)) {
				return "没有部门的计调经理或销售经理不能和有部门的角色同属于一个用户";
			}
 		}
		return null;
	}
	
	/**
	 * 清除session中Role的缓存
	 * @param role
	 * @return
	 */
	public Role clearRole(Role role){
		roleDao.getSession().evict(role);
		return role;
	}
	
	public List<Role> findAllRole(){
		UserUtils.getUser();
		DetachedCriteria dc = roleDao.createDetachedCriteria();
		dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
		dc.add(Restrictions.eq(Role.DEL_FLAG, Role.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("id"));
		return roleDao.find(dc);
	}
	
	public List<Role> findRoleByCompanyId(Long companyId) {
		return roleDao.findRoleByCompanyId(companyId);
	}
	
	public List<Role> findOfficeRole(Long companyId) {
		return roleDao.findOfficeRole(companyId);
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
//		roleDao.clear();
		roleDao.save(role);
		systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Long id) {
		roleDao.deleteById(id);
		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, Long userId) {
		User user = userDao.findOne(userId);
		List<Long> roleIds = user.getRoleIdList();
		List<Role> roles = user.getRoleList();
		// 
		if (roleIds.contains(role.getId())) {
			roles.remove(role);
			saveUser(user);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, Long userId) {
		User user = userDao.findOne(userId);
		List<Long> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);		
		return user;
	}

	//-- Menu Service --//
	
	public Menu getMenu(Long id) {
		return menuDao.findOne(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	public List<Object[]> findMenuByRoleId(Long roleId) {
		return menuDao.findMenuByRoleId(roleId);
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		menu.setParent(this.getMenu(menu.getParent().getId()));
		String oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		menuDao.clear();
		menuDao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		menuDao.save(list);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Long id) {
		menuDao.deleteById(id, "%,"+id+",%");
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
	}
	
	public List<Menu> findMenuByName() {
		return menuDao.findByNames();
	}
	
	/**
	 * 查询菜单下所有子类菜单
	 * @param menuId
	 * @return
	 */
	public List<Menu> findByParentIdsLike(Long menuId) {
		return menuDao.findByParentIdsLike("%," + menuId + ",%");
	}
	
	/**
	 * 根据角色类型查询所有角色
	 * @param roleType
	 * @return
	 */
	public List<Role> findByRoleTypeAndCompanyId(String roleType, String roleType2, Long companyId) {
		return roleDao.findByRoleTypeAndCompanyId(roleType, roleType2, companyId);
	}
	
	/**
	 * 根据角色ID查询所有用户
	 * @param roleId
	 * @return
	 */
	public List<User> getUserByRoleId(Long roleId) {
		return userDao.getUserByRoleId(roleId);
	}
	
	/**
	 * sql更新语句执行
	 * @param updateSql
	 * @return
	 */
	public int updateBySql(String updateSql) {
		return userDao.updateBySql(updateSql);
	}
	
	/**
	 * 获取部门下的所有销售人员
	 * @param companyId
	 * @param departmentId
	 * @return
	 */
	public List<User> getUserByDepartmentAndRoleType(Long companyId, Long departmentId){
		return userDao.getUserByDepartmentAndRoleType( companyId,  departmentId);
	}
	
	/**
	 * 获取部门下的所有人员
	 * @param companyId
	 * @param departmentId
	 * @return
	 */
	public List<User> getUserByDepartment(Long companyId, Long departmentId){
		return userDao.getUserByDepartment( companyId,  departmentId);
	}
	/**
	 * add 20150906
	 * 根据角色类型，公司Id 获取指定公司下拥有该角色的用户列表
	 * @param roleType
	 * @param officeId
	 * @return
	 */
	public List<User> findRoleByOffice(String roleType, String officeId){
		List<User> userList = new ArrayList<User>();
		if(StringUtils.isNotBlank(roleType) && StringUtils.isNotBlank(officeId)){
			userList = userDao.findRoleByOffice(roleType,officeId);
		}
		return userList;
	}
	
	/**
	 * 根据菜单的父Id构建菜单json数据集合
		 * @Title: buildMenuDataByParentId
	     * @return MenuJsonBean
	     * @author majiancheng       
	     * @date 2015-10-27 下午8:02:50
	 */
	public MenuJsonBean buildMenusJsonDataByParentId(String parentIdsStr) {
		//装载和该菜单相关联的所有子菜单和父菜单
		MenuJsonBean menuData = new MenuJsonBean();
		
		//子菜单集合
		List<Menu> sonMenus = new ArrayList<Menu>();
		//父菜单集合
		List<Menu> parentMenus = new ArrayList<Menu>();

		List<Long> parentIdList = new ArrayList<Long>();
		
		String[] parentIdsArr = parentIdsStr.split(",");
		for(String parentIdStr : parentIdsArr) {
			Long parentId = Long.parseLong(parentIdStr);
			
			Menu currMenu = menuDao.findOne(parentId);
			sonMenus.add(currMenu);
			sonMenus.addAll(findByParentIdsLike(parentId));
			
			//获取当前菜单的所有父订单
			String parentIds = currMenu.getParentIds();
			String[] parentIdArr = parentIds.split(",");
			for(String id : parentIdArr) {
				if(StringUtils.isNotEmpty(id)) {
					parentIdList.add(Long.parseLong(id));
				}
			}
		}
		
		//查询所有的父菜单信息
		parentMenus = menuDao.findByIds(parentIdList);
		
		
		//为菜单对象设置parent对象
		if(CollectionUtils.isNotEmpty(parentMenus)) {
			for(Menu menu : parentMenus) {
				menu.setParentId(menuDao.findParentIdById(menu.getId()));
			}
		}
		if(CollectionUtils.isNotEmpty(sonMenus)) {
			for(Menu menu : sonMenus) {
				menu.setParentId(menuDao.findParentIdById(menu.getId()));
			}
		}
		
		menuData.setParentMenus(MenuTransfer.transfer2JsonBeansByMenus(parentMenus));
		menuData.setSonMenus(MenuTransfer.transfer2JsonBeansByMenus(sonMenus));
		return menuData;
	}
	
	public Map<String, String> saveMenusByMenuJsonData(String menuJsonData) {
		Map<String, String> datas = new HashMap<String, String>();
		//解析jsonBean
		MenuJsonBean menuData = JSON.parseObject(menuJsonData, MenuJsonBean.class);

		List<Menu> parentMenus = MenuTransfer.transfer2MenusByJsonBeans(menuData.getParentMenus());
		List<Menu> sonMenus = MenuTransfer.transfer2MenusByJsonBeans(menuData.getSonMenus());
		
		//将菜单信息进行排序，按照parentIds中的,号个数进行排序
		java.util.Collections.sort(sonMenus, new Comparator<Menu>(){
            @Override  
            public int compare(Menu m1, Menu m2) {
            	String[] parentIdM1Arr = m1.getParentIds().split(",");
            	String[] parentIdM2Arr = m2.getParentIds().split(",");
                return parentIdM1Arr.length - parentIdM2Arr.length; 
            }  
        });
		
		
		Map<String, Menu> menuMap = new HashMap<String, Menu>();
		if(CollectionUtils.isNotEmpty(parentMenus)) {
			for(Menu menu : parentMenus) {
				menuMap.put(menu.getId().toString(), menu);
			}
		}
		if(CollectionUtils.isNotEmpty(sonMenus)) {
			for(Menu menu : sonMenus) {
				menuMap.put(menu.getId().toString(), menu);
			}
		}
		
		User currUser = UserUtils.getUser();
		try{

			//组装新建的菜单信息
			if(CollectionUtils.isNotEmpty(sonMenus)) {
				for(Menu menu : sonMenus) {
					List<Menu> sonMenu = menuDao.findByNameAndHref(menu.getName(), menu.getHref());
					
					String parentIds = menu.getParentIds();
					//新的父菜单ids字符串
					StringBuffer newParentIds = new StringBuffer("0,");
					
					String[] parentIdArr = parentIds.split(",");
					for(int i=0; i<parentIdArr.length; i++) {
						String parentId = parentIdArr[i];
						
						if(StringUtils.isEmpty(parentId) || ("0".equals(parentId))) {
							continue;
						}
						
						Menu oldParentMenu = menuMap.get(parentId);
						List<Menu> newParentMenu = menuDao.findByNameAndHref(oldParentMenu.getName(), oldParentMenu.getHref());
						if(CollectionUtils.isNotEmpty(newParentMenu)) {
							newParentIds.append(newParentMenu.get(0).getId());
							newParentIds.append(",");
							if(i == parentIdArr.length-1) {
								menu.setParent(newParentMenu.get(0));
							}
						}
					}
					
					
					menu.setParentIds(newParentIds.toString());
					
					menu.setUpdateBy(currUser);
					menu.setUpdateDate(new Date());
					
					if(CollectionUtils.isEmpty(sonMenu)) {
						menu.setCreateBy(currUser);
						menu.setCreateDate(new Date());
						menu.setId(null);
						//保存菜单数据
						menuDao.save(menu);
					} else {
						//修改当前系统的菜单信息
						Menu currSonMen = sonMenu.get(0);
						currSonMen.setDelFlag(menu.getDelFlag());
						currSonMen.setHref(menu.getHref());
						currSonMen.setIcon(menu.getIcon());
						currSonMen.setIsShow(menu.getIsShow());
						currSonMen.setLevel(menu.getLevel());
						currSonMen.setName(menu.getName());
						currSonMen.setParent(menu.getParent());
						currSonMen.setParentId(menu.getParentId());
						currSonMen.setParentIds(menu.getParentIds());
						currSonMen.setPermission(menu.getPermission());
						currSonMen.setSort(menu.getSort());
						currSonMen.setTarget(menu.getTarget());
						currSonMen.setUpdateBy(currUser);
						currSonMen.setUpdateDate(menu.getUpdateDate());
						
						menuDao.updateObj(currSonMen);
					}
				}
			}
			
			datas.put("result", "1");
			datas.put("message", "导入成功！");
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请核实需要导入的json数据的正确性！");
		}
		
		return datas;
	}
	/**
	 * 账号修改时查询审批模块权限值
	 */
	public List<Map<String,Object>> findReviewLisence(Long userId,String companyUuid){
		String sql = " select is_jump_task_permit,is_applier_auto_approve from sys_user_review_common_permission where del_flag=0 and user_id=? and company_uuid=? ";
		List<Map<String,Object>> resultMap = userPermissionDao.findBySql(sql,Map.class, userId, companyUuid);
		return resultMap;
	}
	
	public void saveUserLisence(SysUserReviewCommonPermission object){
		
		userPermissionDao.saveObj(object);
	}
	
	public void saveUserProcess(SysUserReviewProcessPermission object){
		userProcessPermissionDao.saveObj(object);
		userProcessPermissionDao.clear();
	}
	
	/**
	 * 检查是否有用户权限记录
	 * @return
	 */
	public List<Object> check4UserLisence(String userId){
		String sql = " SELECT s.id  from sys_user_review_common_permission s WHERE s.del_flag=0 AND s.user_id=? AND s.company_id=? ";
		List<Object> value = userPermissionDao.findBySql(sql, userId,UserUtils.getUser().getCompany().getId());
		return value;
	}
	
	/**
	 * 查看是否有用户的部门-职务记录
	 * @param userId
	 * @param companyUuid
	 * @return
	 */
	public List<Object> check4DeptJobRelation(Long userId,String companyUuid){
		String sql = "SELECT s.id FROM sys_user_dept_job_new s WHERE s.user_id=? AND s.company_uuid=? AND s.del_flag=0 ";
		List<Object> value = userPermissionDao.findBySql(sql, userId,companyUuid);
		return value;
	}
	
	/**
	 * 保存用户的部门-职务关系
	 * @param relationValue
	 */
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void saveDeptJobRelationship(String relationValue,Long userId){
		String[] relation = relationValue.split(",");//数据格式：141-18,33-16
		List<String> strList = new ArrayList<String>();
		for(int i=0;i<relation.length;i++){//去重复选择
			if(!strList.contains(relation[i])){
				strList.add(relation[i]);
            }
		}
		User user = userDao.getById(userId);
		for(String str : strList){
			String deptId = str.split("-")[0];
			String jobId = str.split("-")[1];
			UserDeptJobNew entity = new UserDeptJobNew();
			entity.setCompany_uuid(user.getCompany().getUuid());
			entity.setCreate_by(UserUtils.getUser().getId().toString());
			entity.setCreate_date(new Date());
			entity.setDelFlag(0);
			entity.setDept_id(Long.parseLong(deptId));
			entity.setJob_id(Long.parseLong(jobId));
			entity.setUser_id(userId);
			//userDeptJobNewDao.saveObj(entity);
			userDeptJobNewDao.save(entity);
		}
	}
	
	public SysJobNew findSysJobNewById(Long id){
		return sysJobNewDao.findOne(id);
	}
	
	/**
	 * 查询审批监督弹出框选中的部门id
	 * @param userId
	 * @param companyUuid
	 * @return
	 */
	public List<Object> getSelectDeptId(Long userId,String companyUuid){
		String sql = " select distinct s.dept_id  from sys_user_review_process_permission s where s.user_id=? and s.company_uuid=? and s.del_flag=0 ";
		return userProcessPermissionDao.findBySql(sql, userId,companyUuid);
	}
	
	/**
	 * 查询审批监督弹出框选中的产品名称id
	 * @param userId
	 * @param companyUuid
	 * @return
	 */
	public List<Object> getSelectProductId(Long userId,String companyUuid){
		String sql = " select distinct s.product_type  from sys_user_review_process_permission s where s.user_id=? and s.company_uuid=? and s.del_flag=0 ";
		return userProcessPermissionDao.findBySql(sql, userId,companyUuid);
	}
	
	/**
	 * 查询审批监督弹出框选中的流程类型id
	 * @param userId
	 * @param companyUuid
	 * @return
	 */
	public List<Object> getSelectReviewFlowId(Long userId,String companyUuid){
		String sql = " select distinct s.review_flow  from sys_user_review_process_permission s where s.user_id=? and s.company_uuid=? and s.del_flag=0 ";
		return userProcessPermissionDao.findBySql(sql, userId,companyUuid);
	}
	
	/**
	 * 查询审批监督弹出框选中的部门id,产品名称id,流程类型id
	 * @param userId
	 * @param companyUuid
	 * @return
	 */
	public List<Map<String,Object>> getUserReviewProcessValue(Long userId,String companyUuid){
		String sql = "select s.dept_id as deptId,s.product_type as productTypeId,s.review_flow as reviewFlowId from sys_user_review_process_permission s where s.user_id=? and s.company_uuid=? and s.del_flag=0 ";
		
		return userProcessPermissionDao.findBySql(sql, Map.class, userId,companyUuid);
	}
	
	public List<Map<String,Object>> getUserReviewProcessValue(Long userId,String companyUuid,Integer reviewFlow){
		String sql = "select s.dept_id as deptId,s.product_type as productTypeId,s.review_flow as reviewFlowId from sys_user_review_process_permission s where s.user_id=? and s.company_uuid=? and s.review_flow=? and s.del_flag=0 ";
		
		return userProcessPermissionDao.findBySql(sql, Map.class, userId,companyUuid,reviewFlow);
	}
	
	
	
	public List<Map<String,Object>> getUserReviewProcessValue(Long userId,String companyUuid,String flowIdStr){
		String sql = "select s.dept_id as deptId,s.product_type as productTypeId,s.review_flow as reviewFlowId from sys_user_review_process_permission s where s.user_id=? and s.company_uuid=? and s.review_flow in("+flowIdStr+") and s.del_flag=0 ";
		
		return userProcessPermissionDao.findBySql(sql, Map.class, userId,companyUuid);
	}
	
	
	public List<Map<String,Object>> getByUserIdAndCompanyUuid(Long userId,String companyUuid){
		String sql = " select dept_id,job_id,company_uuid from sys_user_dept_job_new where user_id =? and company_uuid=? and del_flag=0 ";
		return userDeptJobNewDao.findBySql(sql, Map.class, userId,companyUuid);
	}
	
	public void deleteDeptJobRelationship(Long userId,String companyUuid){
		String sql = " UPDATE sys_user_dept_job_new SET del_flag=1 WHERE user_id=? AND company_uuid=? ";
		userDeptJobNewDao.updateBySql(sql, userId,companyUuid);
	}
	
	public void updateUserLisence(Long userId,String companyUuid){
		String sql = " UPDATE sys_user_review_common_permission SET del_flag=1,update_by=?,update_date=? WHERE user_id=? AND company_uuid=? AND del_flag=0 ";
		userPermissionDao.updateBySql(sql,UserUtils.getUser().getId(),new Date(), userId,companyUuid);
	}
	
	public void updateUserProcess(Long userId,String companyUuid){
		String sql = " UPDATE sys_user_review_process_permission SET del_flag=1,update_by=?,update_date=? WHERE user_id=? AND company_uuid=? AND del_flag=0 ";
		userProcessPermissionDao.updateBySql(sql,UserUtils.getUser().getId(),new Date(), userId,companyUuid);
	}
	
	public List<SysJobNew> findByCompanyUuid(String companyUuid){
		return sysJobNewDao.findByCompanyUuid(companyUuid);
	}
	
	/**
	 * 根据公司uuid查找职务map
	 * @author zhenxing.yan
	 * @param companyUuid
	 * @return
     */
	public Map<Long,SysJobNew> findSysJobMapByCompanyUuid(String companyUuid){
		Assert.hasText(companyUuid,"companyUuid should not be empty!");
		Map<Long,SysJobNew> jobMap=new HashMap<>();
		List<SysJobNew> sysJobs=sysJobNewDao.findByCompanyUuid(companyUuid);
		if (sysJobs!=null&&sysJobs.size()>0){
			for (SysJobNew sysJob:sysJobs) {
				jobMap.put(sysJob.getId(),sysJob);
			}
		}
		return jobMap;
	}

	/**
	 * 根据公司uuid查找职务map
	 * @author zhenxing.yan
	 * @param companyUuid
	 * @return Map<String,SysJobNew> key为uuid
     */
	public Map<String,SysJobNew> findSysJobUuidMapByCompanyUuid(String companyUuid){
		Assert.hasText(companyUuid,"companyUuid should not be empty!");
		Map<String,SysJobNew> jobMap=new HashMap<>();
		List<SysJobNew> sysJobs=sysJobNewDao.findByCompanyUuid(companyUuid);
		if (sysJobs!=null&&sysJobs.size()>0){
			for (SysJobNew sysJob:sysJobs) {
				jobMap.put(sysJob.getUuid(),sysJob);
			}
		}
		return jobMap;
	}

	/**
	 * 查找一个公司内的部门职务组合列表
	 * @param companyUuid
	 * @return
     */
	public List<String> findDeptJobByCompanyUuid(String companyUuid){
		Assert.hasText(companyUuid,"companyUuid should not be empty!");
		return userDeptJobNewDao.findDeptJobByCompanyUuid(companyUuid);
	}

	/**
	 * 查询指定用户的职务列表
	 * @param userId
	 * @return
     */
	public List<String> findUserJobsByUserId(String userId){
		Assert.hasText(userId,"userId should not be empty!");
		return userDeptJobNewDao.findUserJobs(Long.parseLong(userId));
	}

	/**
	 * 查找指定部门职务下的用户id列表
	 * @param deptId
	 * @param jobId
	 * @param companyUuid
     * @return
     */
	public List<String> findUserIdsByDeptJob(Long deptId,Long jobId,String companyUuid){
		Assert.hasText(companyUuid,"companyUuid should not be empty!");
		Assert.notNull(deptId,"deptId should not be null!");
		Assert.notNull(jobId,"jobId should not be null!");

		return userDeptJobNewDao.findUserIdsByDeptJob(deptId,jobId,companyUuid);
	}

	/**
	 * 查找指定部门职务下的用户id列表
	 * @param deptId
	 * @param jobId
	 * @return
	 */
	public List<String> findUserIdsByDeptJob(Long deptId,Long jobId){
		Assert.notNull(deptId,"deptId should not be null!");
		Assert.notNull(jobId,"jobId should not be null!");

		return userDeptJobNewDao.findUserIdsByDeptJob(deptId,jobId);
	}

	/**
	 * 查找公司所有的用户部门职务map
	 * @param companyUuid
	 * @return MultiValueMap
     */
	public MultiValueMap<Long,UserDeptJobNew> findUserDeptJobByCompanyUuid(String companyUuid){
		Assert.hasText(companyUuid,"companyUuid should not be empty!");
		MultiValueMap<Long,UserDeptJobNew> userDeptJobMap=new LinkedMultiValueMap<>();
		List<UserDeptJobNew> userDeptJobs=userDeptJobNewDao.findByCompanyUuid(companyUuid);
		if (userDeptJobs!=null&&userDeptJobs.size()>0){
			for (UserDeptJobNew userDeptJob:userDeptJobs){
				userDeptJobMap.add(userDeptJob.getUser_id(),userDeptJob);
			}
		}
		return userDeptJobMap;
	}

	/**
	 * 判断用户是否计调职务
	 * @param userId
	 * @return
     */
	public boolean hasOperatorJob(Long userId){
		if (userId!=null){
			List<Object[]> result=userDeptJobNewDao.findUserOperatorJobs(userId);
			return result.size()>0?true:false;
		}
		return false;
	}

	/**
	 * 通过用户Id、公司Uuid查找用户审批流程权限
	 * @param userId
	 * @param companyUuid
	 * @return
	 */
	public UserReviewPermissionResultForm findPermissionByUserIdAndCompanyUuid(Long userId,String companyUuid){
		UserReviewPermissionResultForm entity = new UserReviewPermissionResultForm();
		if(userId != null && StringUtils.isNotBlank(companyUuid)){
			List<Map<String, Object>> mapList = this.getUserReviewProcessValue(userId, companyUuid);
			Set<String> deptIdSet = new HashSet<String>();
			Set<String> productIdSet = new HashSet<String>();
			Set<String> reviewFlowIdSet = new HashSet<String>();
			for(Map<String,Object> map : mapList){
				deptIdSet.add(map.get("deptId").toString());
				productIdSet.add(map.get("productTypeId").toString());
				reviewFlowIdSet.add(map.get("reviewFlowId").toString());
			}
			
			entity.setUserId(userId);
			entity.setDeptId(deptIdSet);
			entity.setProductTypeId(productIdSet);
			entity.setReviewFlowId(reviewFlowIdSet);
		}

		return entity;
	}
	
	/**
	 * 通过用户Id、公司Uuid、流程类型Id 查找用户审批流程权限
	 * @param userId
	 * @param companyUuid
	 * @param reviewFlow
	 * @return
	 */
	public UserReviewPermissionResultForm findPermissionByUserIdAndCompanyUuidAndFlowType(Long userId,String companyUuid,Integer reviewFlow){
		UserReviewPermissionResultForm entity = new UserReviewPermissionResultForm();
		if(userId!=null && StringUtils.isNotBlank(companyUuid) && reviewFlow!=null){
			List<Map<String, Object>> mapList = this.getUserReviewProcessValue(userId, companyUuid, reviewFlow);
			Set<String> deptIdSet = new HashSet<String>();
			Set<String> productIdSet = new HashSet<String>();
			Set<String> reviewFlowIdSet = new HashSet<String>();
			for(Map<String,Object> map : mapList){
				deptIdSet.add(map.get("deptId").toString());
				productIdSet.add(map.get("productTypeId").toString());
				reviewFlowIdSet.add(map.get("reviewFlowId").toString());
			}
			
			entity.setUserId(userId);
			entity.setDeptId(deptIdSet);
			entity.setProductTypeId(productIdSet);
			entity.setReviewFlowId(reviewFlowIdSet);
		}
		
		return entity;
	}
	
	/**
	 * 通过用户Id、公司Uuid、流程类型Id集合 查找用户审批流程权限
	 * @param userId
	 * @param companyUuid
	 * @param flowList
	 * @return
	 */
	public UserReviewPermissionResultForm findPermissionByUserIdAndCompanyUuidAndFlowTypeList(Long userId,String companyUuid,List<Integer> flowList){
		UserReviewPermissionResultForm entity = new UserReviewPermissionResultForm();
		if(userId!=null && StringUtils.isNotBlank(companyUuid) && flowList.size()>0){
			String flowIdStr = "";
			for(Integer flowId : flowList){
				flowIdStr += flowId+",";
			}
			flowIdStr = flowIdStr.substring(0, flowIdStr.length()-1);
			
			List<Map<String, Object>> mapList = this.getUserReviewProcessValue(userId, companyUuid, flowIdStr);
			Set<String> deptIdSet = new HashSet<String>();
			Set<String> productIdSet = new HashSet<String>();
			Set<String> reviewFlowIdSet = new HashSet<String>();
			for(Map<String,Object> map : mapList){
				deptIdSet.add(map.get("deptId").toString());
				productIdSet.add(map.get("productTypeId").toString());
				reviewFlowIdSet.add(map.get("reviewFlowId").toString());
			}
			
			entity.setUserId(userId);
			entity.setDeptId(deptIdSet);
			entity.setProductTypeId(productIdSet);
			entity.setReviewFlowId(reviewFlowIdSet);
		}
		
		return entity;
	}

	public void saveUserProcessPermissionList(List<SysUserReviewProcessPermission> ls)
	{
		userProcessPermissionDao.batchSave(ls);
	}
/**
 * 判断当前登录用户的角色是否拥有关联机票产品的权限
 * @return
 */
	public boolean findTraffic(String kind) {
		String menuParentId ="";
		if("1".equals(kind))
			menuParentId ="594";
	    else if ("2".equals(kind))
			menuParentId ="595";
		else if ("3".equals(kind))
			menuParentId ="596";
		else if ("4".equals(kind))
			menuParentId ="597";
		else if ("5".equals(kind))
			menuParentId ="598";
		else
			menuParentId="9999999";// 不和任何菜单匹配



		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM  sys_role sr WHERE  sr.id  IN( ");
		buffer.append("SELECT srm.roleId FROM sys_role_menu srm WHERE srm.menuId IN (SELECT id FROM sys_menu sm WHERE sm.name LIKE '关联机票' AND sm.parentId=");
		buffer.append(menuParentId);
		buffer.append(")");
		buffer.append(") AND   sr.companyId =");
		buffer.append(UserUtils.getUser().getCompany().getId());
		buffer.append( " AND FIND_IN_SET(sr.name,'");
		buffer.append( UserUtils.getUser().getRoleNames());
		buffer.append("')");
		List<?> list=  sysJobNewDao.findBySql(buffer.toString());
		 if(list.size()>0)
			 return true;
		 else
			 return false;
	}


	public String getProductAirTicketId(Long id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT t.airticket_id FROM travelactivity t WHERE t.id =");
		buffer.append(id);
		List<?> list =  sysJobNewDao.findBySql(buffer.toString(),Map.class);
		if(list.size() >0)
		return "true";
		else
			return "false";
		
	}
	
	/**
	 * 获取代替下单用户待选列表（所有人）
	 * @param subLoginName 登录名
	 * @param subUserName 姓名
	 * @param subRole 角色
	 * @return
	 */
	public JSONArray getSubstituteList(String subLoginName, String subUserName, Long subRole) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 查询批发商下所有用户 。主要信息：id、登录名、姓名、角色名、角色id
		List<Map<String, Object>> subInfos = userDao.getSubstitute(companyId, subLoginName, subUserName, subRole);
		// 转化 list 为 jsonarray
		JSONArray jsonArray = new JSONArray();
		for (Map<String, Object> map : subInfos) {
			JSONObject jsonObject = JSONObject.fromObject(map);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	/**
	 * 获取用户下的代替人列表（已选）
	 * @param user
	 * @return
	 */
	public List<User> getChosedSubstitute(User user) {
		List<User> chosedSub = new ArrayList<>();
		// 
		try {
			if (StringUtils.isNotBlank(user.getSubstituteOrder())) {
				String[] chosedArray = user.getSubstituteOrder().split(",");
				if (chosedArray != null && chosedArray.length > 0) {
					for (int i = 0; i < chosedArray.length; i++) {
						chosedSub.add(userDao.findById(Long.parseLong(chosedArray[i])));
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return chosedSub;
		}
		return chosedSub;
	}
	

	/**
	 * 获取user（助理） 所有可被代替下单的人列表
	 * @param userId
	 * @return
	 */
	public List<User> findAllBeSubstitutes(Long userId) {
		List<User> beSubstitutes = new ArrayList<>();
		// 获取用户实体，如果取不到就获取当前登录用户
		User iUser = null;
		if (userId != null) {
			iUser = UserUtils.getUser(userId);
		} else {
			iUser = UserUtils.getUser();
		}
		// 获取对应的批发商
		Office office = iUser.getCompany();
		List<User> allPeople = new ArrayList<>();  // 批发商下所有的用户
		if (office != null) {
			allPeople = getAllUserByCompanyId(office.getId());
		}
		// 遍历用户，查找所有指定“我”（助理）作为下单代替人的用户
		for (User user : allPeople) {
			List<User> chosedSubs = getChosedSubstitute(user);
			if (CollectionUtils.isNotEmpty(chosedSubs)) {
				for (User chosedSub : chosedSubs) {
					if (chosedSub.getId() == iUser.getId()) {
						beSubstitutes.add(user);
						break;
					}
				}
			}
		}
		return beSubstitutes;
	}
	
	/**
	 * 保存用户信息
	 */
	public Map<String, Object> saveUserInfo(User user, Map<String, Object> userInfoParam){
		Map<String, Object> resultMap = new HashMap<>();
		if (user == null || userInfoParam == null || userInfoParam.size() == 0) {
			// 用户信息为空保存失败
			resultMap.put("flag", "faild");
			resultMap.put("message", "用户信息为空！");
			return resultMap;
		}
		// 通过id判断是否新建user
		boolean isNewUser = true;
		if (user.getId() != null) {
			isNewUser = false;
		}		
		// 从map中获取新数据充实user
		Office office = (Office)userInfoParam.get("company");
		if (office == null) {
			resultMap.put("flag", "faild");
			resultMap.put("message", "用户所属批发商为空！");
			return resultMap;
		}
		user.setCompany(office);  // 所属批发商
		Agentinfo agentInfo =  (Agentinfo)userInfoParam.get("agent");
		if (agentInfo == null || agentInfo.getId() == null) {
			resultMap.put("flag", "faild");
			resultMap.put("message", "用户所属渠道id为空！");
			return resultMap;
		}
		user.setAgentId(agentInfo.getId());  // 所属渠道
		user.setName(userInfoParam.get("name").toString());  // 姓名
		user.setLoginName(userInfoParam.get("newLoginName").toString());  // 登录名
		// 如果新密码为空，则不更换密码
		String newPassword = userInfoParam.get("newPassword").toString();
		if (StringUtils.isNotBlank(newPassword)) {
			user.setPassword(SystemService.entryptPassword(newPassword));
			user.setTwo_psw(Base64Util.getBase64(newPassword));
		}
		user.setUserType(Context.USER_TYPE_RECEPTION);
		user.setDelFlag(Context.DEL_FLAG_NORMAL);
		user.setIsQuauqAgentLoginUser(Context.QUAUQ_AGENT_LOGIN_USER_YES);
		// 538 下单权限
		Integer differenceRights = (Integer) userInfoParam.get("differenceRights");
		if(differenceRights != null ){
			user.setDifferenceRights(differenceRights);
		}else{
			user.setDifferenceRights(0);
		}
		/** 临时策略（待删除） */
		Integer lingxianwangshuai = (Integer) userInfoParam.get("lingxianwangshuai");
		if(lingxianwangshuai != null ){
			user.setLingxianwangshuai(lingxianwangshuai);
		}else{
			user.setLingxianwangshuai(0);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
//		List<Role> roleList = getQuauqAgentRole();
//		user.setRoleList(roleList);
		// 微信账号与t1账号关联
		MobileUser mobileUser = null;
		if(userInfoParam.get("mobileUserId") != null && StringUtils.isNotBlank(userInfoParam.get("mobileUserId").toString())){
			mobileUser = mobileUserDao.findOne(Long.parseLong(userInfoParam.get("mobileUserId").toString()));
			mobileUser.setDelFlag("2");// 已关联
			mobileUser.setT1User(user);
			user.setMobileUser(mobileUser);
			//账号来源，0 内部 1 微信
			user.setAccountFrom(1);
		}
		// 保存用户信息
		saveUser(user);
		//  roleList没有存入？TODO
//		for (Role role : roleList) {			
//			userDao.updateUserRole(user.getId(), role.getId());
//		}
		
		//TODO 待理解含义，先保留
		if (MultiTenantUtil.turnOnMulitTenant()) {
			// 将用户添加至用户租户库
			final String loginName = user.getLoginName();
			final String oldLoginName = userInfoParam.get("oldLoginName") == null ? null : userInfoParam.get("oldLoginName").toString() ;
			final String companyIdStr = office.getId().toString();

			if (isNewUser) {// 新增用户
				TenantUtil.addUser(loginName, companyIdStr);
			} else if (!user.getLoginName().equals(oldLoginName)) {// 修改用户名
				TenantUtil.modifyUser(loginName, oldLoginName, companyIdStr);
			}
		}
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
			UserUtils.getCacheMap().clear();
		}
		if(mobileUser != null){
			mobileUserDao.updateObj(mobileUser);
			resultMap.put("mobileUserId", mobileUser.getId());
			resultMap.put("userId", user.getId());
		}
		resultMap.put("flag", "success");
		resultMap.put("message", "用户账号保存成功！");
		return resultMap;
	}

	/**
	 * 获取批发商下对应拥有quauq报名权限的账号
	 * @param officeId 批发商Id
	 * @return
	 */
	public List<User> getQuauqBookerByCompanyId(Long officeId) {
		String Sql = "SELECT id,companyId,loginName,two_psw,name FROM sys_user WHERE delFlag = " + Context.DEL_FLAG_NORMAL + " AND companyId = " + officeId + " AND quauqBookOrderPermission = '1' ";
		List<Map<String, Object>> userMaps = userDao.findBySql(Sql, Map.class);
		List<User> users = new ArrayList<>();
		for (Map<String, Object> userMap : userMaps) {
			User user  = new User();
			user.setId(Long.parseLong(userMap.get("id").toString()));
			user.setLoginName(userMap.get("loginName")== null ? null : userMap.get("loginName").toString());
			user.setTwo_psw(userMap.get("two_psw") == null ? null : userMap.get("two_psw").toString());
			user.setName(userMap.get("name") == null ? null : userMap.get("name").toString());
			users.add(user);
		}
		return users;
	}

	public void changeUserLoginStatus() {
		userDao.updateLoginStatus(0);
	}
	
}
