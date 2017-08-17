package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Department;

/**
 * 
 * @description 部门DAO
 *
 * @author baiyakun
 *
 * @create_time 2014-9-15
 */
public interface DepartmentDao extends DepartmentDaoCustom, CrudRepository<Department, Long> {
	
	@Modifying
	@Query("update Department set delFlag='1' where id = ?1 or parentIds like ?2")
	public int deleteById(Long id, String likeParentIds);
	
	public List<Department> findByParentIdsLike(String parentIds);
	
	@Query("from Department where office.id = ?1 and parent.id = '0' and delFlag = '0'")
	public Department findParent(Long officeId);
	
	@Query("from Department where office.id = ?1 and code = ?2  and delFlag = '0'")
	public List<Department> findByCode(Long officeId, String code);
	
	@Query("from Department where office.id = ?1 and delFlag = '0' order by sort")
	public List<Department> findByOfficeId(Long officeId);
	
	@Query("from Department where office.id = ?1 and delFlag = '0' order by level asc")
	public List<Department> findByOfficeIdOrderByLevel(Long officeId);
	
	@Query(value="select selectMenu.menu_id from dept_select_menu selectMenu where dept_id = ?1",nativeQuery=true)
	public List<Integer> findSelectIdsByDeptId(Long roleId);
	
	@Query("select id from Department where parent_id = ?1")
	public List<Long> findSubidsByParentId(Long parentId);
	
	@Query("select parentId from Department where id = ?1 and delFlag = '0'")
	public List<Long> findParentId(Long id);
	
	@Query(value="select srcActivityId from activity_op_user where opUserId = ?1", nativeQuery=true)
	public List<Integer> findActivityIdsByOpUserId(Long userId);
	
	@Query(value="select srcActivityId from activity_op_user", nativeQuery=true)
	public List<Integer> findActivityIds();
	
	@Query("from Department where parentId = ?1 and delFlag = '0' order by sort asc")
	public List<Department> findByParentId(Long parentId);
	
	@Query("from Department where name = ?1 and office.id= ?2 and delFlag = '0'")
	public Department findByName(String name,Long companyId);
	
	@Query("from Department where id = ?1 and office.id= ?2 and delFlag = '0'")
	public Department getDeptNameById(Long id,Long companyId);
}

interface DepartmentDaoCustom extends BaseDao<Department> {

}

@Repository
class DepartmentDaoImpl extends BaseDaoImpl<Department> implements DepartmentDaoCustom {

}
