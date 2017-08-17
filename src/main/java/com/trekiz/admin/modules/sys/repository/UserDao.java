package com.trekiz.admin.modules.sys.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 用户DAO接口
 * 
 * @author zj
 * @version 2013-11-19
 */
public interface UserDao extends UserDaoCustom, CrudRepository<User, Long> {

	@Query("from User where loginName = ?1 and delFlag = '" + User.DEL_FLAG_NORMAL + "'")
	public User findByLoginName(String loginName);

	@Query("from User where no = ?1 and delFlag = '" + User.DEL_FLAG_NORMAL + "'")
	public User findByNo(String no);

	@Query("from User where id = ?1 and delFlag = '" + User.DEL_FLAG_NORMAL+ "'")
	public User findById(Long id);
	
	@Query("from User where loginName in ('gmyx1', 'gmyx2', 'gmyx3', 'gmyx4', 'gmrh', 'gmtw', 'gmmz', 'gmoz', 'gmdny', 'gmzdf', 'gmax', 'gmqz') and two_psw is not null")
	public List<User> findByLoginName();

	@Query("select distinct user from User user, Role role, Department dept where user.company.id = ?1 and role in elements (user.roleList) and role.department = dept and (dept.id = ?2 or dept.parentIds like ?3) and dept.delFlag = '"
			+ User.DEL_FLAG_NORMAL
			+ "' and user.delFlag = '"
			+ User.DEL_FLAG_NORMAL + "'")
	public List<User> getAllUserByDepartment(Long companyId, Long deptId, String parentIds);

	@Query("select distinct user from User user, Role role where user.company.id = ?1 and role in elements (user.roleList) and role.department.id = ?2 and user.delFlag = '"
			+ User.DEL_FLAG_NORMAL + "'")
	public List<User> getUserByDepartment(Long companyId, Long departmentId);

	@Query("select user from User user, Role role where role in elements (user.roleList) and role.id = ?1 and user.delFlag = '"
			+ User.DEL_FLAG_NORMAL + "'")
	public List<User> getUserByRoleId(Long roleId);

	@Modifying
	@Query("update User set delFlag='" + User.DEL_FLAG_DELETE
			+ "' where id = ?1")
	public int deleteById(Long id);
	
	public List<User> getUserByCompany(Office office);
	
	@Query("from User where delFlag = '" + User.DEL_FLAG_NORMAL + "'")
	public List<User> getAllUser();
	
	@Query("from User where company.id = ?1 and delFlag = '" + User.DEL_FLAG_NORMAL + "'")
	public List<User> getUserByCompanyId(Long officeId);
	
	@Query("select distinct user from User user, Role role where user.company.id = ?1 and role in elements (user.roleList) " +
			"and (role.name like '%财务%' or role.name like '%会计%' or role.name like '%出纳%') and user.delFlag = '" + User.DEL_FLAG_NORMAL + "'")
	public List<User> getCwUserByCompanyId(Long officeId);
	
	@Modifying
	@Query("update User set password=?1, two_psw=?2 where id = ?3")
	public int updatePasswordById(String newPassword, String twoPsw, Long id);

	@Modifying
	@Query("update User set loginIp=?1, loginDate=?2 where id = ?3")
	public int updateLoginInfo(String loginIp, Date loginDate, Long id);

	@Query("from User where companyId = ?2 and name = ?1 and delFlag= '"+ User.DEL_FLAG_NORMAL + "'")
	public List<User> findByName(String name, Long companyId);
	
	@Query("from User as user  where user.agentId = ?1 and user.delFlag = '"+ User.DEL_FLAG_NORMAL + "'")
	public List<User> findByAgentId(Long agentId);
	
	@Modifying
	@Query("update User set delFlag = '" + User.DEL_FLAG_DELETE + "' where agentId = ?1 and isQuauqAgentLoginUser = ?2")
	public int deleteByAgentAndQuauqflag(Long agentId, String isQuauqLoginUser);
	
	@Query("from User as user where user.agentId = ?1 and user.isQuauqAgentLoginUser = '1' and user.delFlag = '"+ User.DEL_FLAG_NORMAL + "'")
	public List<User> findByQuauqAgentId(Long agentId);
	
	@Query("from User where company.id = ?1 and quauqBookOrderPermission = '1' and delFlag = '"+ User.DEL_FLAG_NORMAL + "'")
	public List<User> findCompanyT1User(Long companyId);
	
	@Query("from User as user where user.agentId = ?1 and user.isQuauqAgentLoginUser = '1'")
	public List<User> findByQuauqAgentIdWithoutDelflag(Long agentId);
	
	@Query("from User where isQuauqAgentLoginUser = '1'")
	public List<User> findQuauqUser();
	
	@Query("select userId from UserDeptJob where deptId = ?1 and delFlag = 0")
	public List<Long> findByUserDept(Long deptId);
	
	@Query("from User where id in ?1")
	public List<User> findByIds(List<Long> userIds);
	
	@Transactional
	@Modifying
	@Query("update User set loginStatus=?2 where id=?1")
	public int updateLoginStatus(Long id, Integer loginStatus);
	
	@Transactional
	@Modifying
	@Query("update User set loginStatus=?1")
	public int updateLoginStatus(Integer loginStatus);
	
	@Query("select distinct user from User user, Role role where user.company.id = ?1 and role in elements (user.roleList) and role.department.id = ?2 and role.roleType='1' and user.delFlag = '"
			+ User.DEL_FLAG_NORMAL + "'")
	public List<User> getUserByDepartmentAndRoleType(Long companyId, Long departmentId);
	
	@Query("from User where groupeSurname = ?1 and company.id = ?2 and delFlag = '"
			+ User.DEL_FLAG_NORMAL + "'")
	public List<User> findByGroupeSurname(String groupeSurname,Long companyId);

	@Query("from Office where id = ?1")
	public Office getOfficeByCompanyId(Long companyId);
	
	@Query("from Office where uuid = ?1")
	public Office getOfficeByCompanyUuid(String companyUuid);
}

/**
 * DAO自定义接口
 * 
 * @author zj
 */
interface UserDaoCustom extends BaseDao<User> {
	List<User> findSalerListByUserType(String userType);
	List<User> findVisaSalerListByUserType(String userType); //查询签证销售/计调人员（包含发过产品但被取消角色的人员）（仅签证可用）
	// 20150906新增 查询指定office中指定角色类型的用户列表
	public List<User> findRoleByOffice(String roleType,String officeId);
	
	/**
	 * 根据批发商集合获取用户id集合
	 * @Description: 
	 * @param @param offices
	 * @param @return   
	 * @return List<String>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	public List<Long> findUserIdsByOffices(List<Office> offices);
	
	public List<Map<String, Object>> getSubstitute(Long officeId, String subLoginName, String subUserName, Long subRole);
	
	List<Map<String,Object>> getSalerNameAndPhone(Long companyId);

	/**
	 * 获取该公司下可用quauq渠道报名的销售信息，包括姓名,名片图片名称等。
	 * @param companyId
     * @return
	 * @author yudong.xu 2016.9.20
     */
	List<Map<String,Object>> getSalerInfoList(Long companyId);

	// 根据职务的名字数组，查询该公司下的是该职务的用户id。
	public List<Integer> getUserIdByJobNameArr(String companyUuid,String[] jobNameArr);
}

/**
 * DAO自定义接口实现
 * 
 * @author zj
 */
@Repository
class UserDaoImpl extends BaseDaoImpl<User> implements UserDaoCustom {
	
	@Override
	public List<User> findSalerListByUserType(String userType){
		if("1".equals(userType)){
			userType = "1,2";
		}
		if("3".equals(userType)){
			userType = "3,4";
		}
		String sql = "select * from sys_user where " +
				" delFlag = '" + User.DEL_FLAG_NORMAL + "' and companyId = " + UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> list = findBySql(sql, Map.class);
		User user;
		List<User> resultList = new ArrayList<User>();
		for(Map<String, Object> temp : list){
			user = new User();
			user.setId(Long.parseLong(temp.get("id").toString()));
			user.setName(temp.get("name").toString());
			resultList.add(user);
		}
		return resultList;
	}
	
	@Override
	//查询签证销售/计调人员（包含发过产品但被取消角色的人员）（仅签证可用）
	/**
	 * 签证 专用  获取 公司销售人员，销售主管
	 * userType，即User的roleType： 1-销售 2-销售主管 3-计调 4-计调主管 0-其他',
	 */
	public List<User> findVisaSalerListByUserType(String userType){
		if("1".equals(userType)){
			userType = "1,2";
		}
		if("3".equals(userType)){
			userType = "3,4";
		}
		String sql = "select " +
				"id,companyId,agentId,officeId,loginName," +
				"password,no,name,email,phone,mobile,userType," +
				"loginIp,loginDate,createBy,createDate,updateBy," +
				"updateDate,remarks,delFlag,two_psw,deptId,login_status," +
				"groupeSurname from sys_user where id in " +//不再过滤userType 取出所有的用户作为查询结果 modify by chy 2015年8月25日14:57:50
				"(select userid from sys_user_role u,sys_role r where u.roleid=r.id and r.roletype in ("+userType+"))" +//"+userType+" 1 2 写死了
				" and delFlag = '" + User.DEL_FLAG_NORMAL + "' and companyId = " + UserUtils.getUser().getCompany().getId();
		List<Map<String, Object>> list = findBySql(sql, Map.class);
		User user;
		List<User> resultList = new ArrayList<User>();
		for(Map<String, Object> temp : list){
			user = new User();
			user.setId(Long.parseLong(temp.get("id").toString()));
			user.setName(temp.get("name").toString());
			resultList.add(user);
		}
		return resultList;
	}
	
	@Override
	public List<User> findRoleByOffice(String roleType, String officeId) {
		List<User> userList = new ArrayList<User>();
		if(StringUtils.isNotBlank(roleType) && StringUtils.isNotBlank(officeId)){
			StringBuffer str = new StringBuffer();
			str.append("select u.id,u.name from sys_user u,sys_user_role ur,sys_role r "
					+ "where u.id=ur.userId and ur.roleId=r.id and "
					+ "r.roleType="+roleType+" and r.companyId="+officeId+" "
					+ " and r.delFlag = '" + Role.DEL_FLAG_NORMAL + "' " 
					+ " and u.delFlag = '" + User.DEL_FLAG_NORMAL + "' "  // 修正，限定user表的用户状态必须是：“正常”
					+ " group by u.id,u.name   order by u.name");
			List<Map<String, Object>> list = findBySql(str.toString(),Map.class);
			for(Map<String, Object> temp : list){
				User user = new User();
				user.setId(Long.valueOf(temp.get("id").toString()));
				user.setName(temp.get("name").toString());
				userList.add(user);
			}
		}
		return userList;
	}
	
	/**
	 * 根据批发商集合获取用户id集合
	 * @Description: 
	 * @param @param offices
	 * @param @return   
	 * @return List<String>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	@SuppressWarnings("rawtypes")
	public List<Long> findUserIdsByOffices(List<Office> offices) {
		if(CollectionUtils.isEmpty(offices)) {
			return null;
		}

		StringBuffer sql = new StringBuffer();
		sql.append("select id from sys_user where companyId in (");
		for(Office office : offices) {
			sql.append(office.getId());
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		List<Map> userIds = findBySql(sql.toString(),Map.class);
		List<Long> userIdLs = new ArrayList<Long>();
		for(Map map : userIds) {
			userIdLs.add(Long.parseLong(map.get("id").toString()));
		}
		return userIdLs;
	}

	@Override
	public List<Map<String, Object>> getSubstitute(Long officeId, String subLoginName, String subUserName, Long subRole) {
		List<Map<String, Object>> list = new ArrayList<>();
		if(officeId != null){
			StringBuffer str = new StringBuffer();
			str.append("select u.id, u.name as subUserName, u.loginName as subLoginName, GROUP_CONCAT(r.id) as roleIds, GROUP_CONCAT(r.name) as subRole from sys_user u, sys_user_role ur, sys_role r "
					+ "where u.id = ur.userId and ur.roleId = r.id "
					+ " and r.companyId = " + officeId + " "
					+ (subRole == null ? "" : " and r.id = " + subRole) + " "
					+ " and u.loginName like '%" + subLoginName + "%' "
					+ " and u.name like '%" + subUserName + "%' "
					+ " and r.delFlag = '" + Role.DEL_FLAG_NORMAL + "' "
					+ " and u.delFlag = '" + User.DEL_FLAG_NORMAL + "' "  // 修正，限定user表的用户状态必须是：“正常”
					+ " group by u.id order by u.id");
			list = findBySql(str.toString(),Map.class);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getSalerNameAndPhone(Long companyId) {
		String sql = "select su.name, su.mobile from sys_user su " +
				"where su.companyId = ? and su.quauqBookOrderPermission = 1 and su.delFlag = 0";
		List<Map<String, Object>> list = findBySql(sql, Map.class, companyId);
		return list;
	}

	@Override
	public List<Map<String,Object>> getSalerInfoList(Long companyId){
		String sql = "SELECT u.id AS userId, u.`name`, u.mobile, u.phone,u.email,u.weixin,u.photoId,u.cardId,d.docName " +
				" FROM sys_user u LEFT JOIN docinfo d ON u.cardId = d.id WHERE u.companyId = ? " +
				" AND u.quauqBookOrderPermission = 1 AND u.delFlag = '0'";
		List<Map<String, Object>> list = findBySql(sql, Map.class,companyId);
		return list;
	}

	// 根据职务的名字数组，查询该公司下的是该职务的用户id。字符串数组必须不为null，长度>=1
	@Override
	public List<Integer> getUserIdByJobNameArr(String companyUuid,String[] jobNameArr){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT m.user_id FROM sys_user_dept_job_new m, sys_job_new j WHERE ")
		   .append("m.company_uuid = j.company_uuid AND m.job_id = j.id AND j.`name` IN (");
		for (String s : jobNameArr) {
			sql.append("'").append(s).append("',");
		}
		sql.deleteCharAt(sql.length()-1); // 去除最后一个逗号
		sql.append(") AND m.company_uuid = ?");
		List<Integer> list = findBySql(sql.toString(),companyUuid);
		return list;
	}

}
