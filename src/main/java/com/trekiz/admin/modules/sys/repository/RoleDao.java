/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Role;

/**
 * 角色DAO接口
 * @author zj
 * @version 2013-11-19
 */
public interface RoleDao extends RoleDaoCustom, CrudRepository<Role, Long> {
	
	@Query("from Role where companyId = ?2 and name = ?3 and delFlag = '" + Role.DEL_FLAG_NORMAL + "' and id != ?1")
	public Role findRoleByCompanyIdAndName(Long id, Long companyId, String name);
	
	@Query("from Role where companyId = ?1 and name = ?2 and delFlag = '" + Role.DEL_FLAG_NORMAL + "'")
	public Role findRoleByCompanyIdAndName(Long companyId, String name);
	
	/**
	 * quauq渠道默认角色，（用户类型：渠道用户，角色类型：其他）
	 * @param companyId
	 * @param name
	 * @return
	 */
	@Query("from Role where companyId = ?1 and name = ?2 and delFlag = '" + Role.DEL_FLAG_NORMAL + "'" + " and userType = '3' and roleType = '0'")
	public List<Role> findQuauaAgentDefaultRole(Long companyId, String name);
	
	@Query("from Role where name = ?1 and delFlag = '" + Role.DEL_FLAG_NORMAL + "'")
	public Role findByName(String name);
	
	public List<Role> findByDepartment(Department dept);

	@Modifying
	@Query("update Role set delFlag='" + Role.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);

	@Query("from Role where delFlag='" + Role.DEL_FLAG_NORMAL + "' and (companyId = ?1 or 1 = ?1 or companyId is null) order by id")
	public List<Role> findRoleByCompanyId(Long companyId);
	
	@Query("from Role where delFlag='" + Role.DEL_FLAG_NORMAL + "' and companyId = ?1")
	public List<Role> findOfficeRole(Long companyId);
	
	@Query("from Role where (roleType = ?1 or roleType = ?2) and companyId = ?3 and delFlag = '" + Role.DEL_FLAG_NORMAL + "'")
	public List<Role> findByRoleTypeAndCompanyId(String roleType, String roleType2, Long companyId);
	/**
	 * 
	* @Title: findRoleByCompanyIdAndRoleType
	* @Description: TODO(根据角色所属批发商Id和角色类型查询角色)
	* @param @param companyId
	* @param @param roleType
	* @return Role    返回类型
	* @throws
	 */
	@Query("from Role where companyId = ?1 and roleType = ?2 and delFlag = '" + Role.DEL_FLAG_NORMAL + "'")
	public List<Role> findRoleByCompanyIdAndRoleType(Long companyId, String roleType);
}

/**
 * DAO自定义接口
 * @author zj
 */
interface RoleDaoCustom extends BaseDao<Role> {
	
}

/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDaoCustom {

}
