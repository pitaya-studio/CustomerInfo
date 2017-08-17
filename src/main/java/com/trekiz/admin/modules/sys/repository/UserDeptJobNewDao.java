package com.trekiz.admin.modules.sys.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.UserDeptJobNew;

import java.util.List;

interface UserDeptJobNewDaoCustom extends BaseDao<UserDeptJobNew> {


}

public interface UserDeptJobNewDao extends UserDeptJobNewDaoCustom, CrudRepository<UserDeptJobNew, Long>{
    /**
     * 根据公司id查找所有的用户部门职务记录
     * @param companyUuid
     * @return
     */
    @Query("from UserDeptJobNew where company_uuid=?1 and delFlag=0")
    List<UserDeptJobNew> findByCompanyUuid(String companyUuid);
    
    /**
     * 根据用户id、公司uuid 查找该用户部门id记录
     * @param userId
     * @param companyUuid
     * @return
     */
    @Query("select dept_id from UserDeptJobNew where user_id=? and company_uuid=? and delFlag=0")
    List<Long> findByUserIdAndCompanyUuid(Long userId,String companyUuid);

    /**
     * 查找一个公司内的部门职务组合列表
     * @param companyUuid
     * @return
     */
    @Query(value = "SELECT DISTINCT CONCAT(CAST(dept_id  AS CHAR),',',CAST(job_id AS CHAR)) FROM sys_user_dept_job_new WHERE company_uuid =?1 AND del_flag=0",nativeQuery = true)
    List<String> findDeptJobByCompanyUuid(String companyUuid);

    /**
     * 查找指定部门职务下的用户id列表
     * @param deptId
     * @param jobId
     * @param companyUuid
     * @return
     */
    @Query(value = "SELECT CAST(user_id AS CHAR) FROM sys_user_dept_job_new WHERE dept_id=?1 AND job_id=?2 AND company_uuid =?3 AND del_flag=0",nativeQuery = true)
    List<String> findUserIdsByDeptJob(Long deptId,Long jobId,String companyUuid);

    /**
     * 查找指定部门职务下的用户id列表
     * @param deptId
     * @param jobId
     * @return
     */
    @Query(value = "SELECT CAST(user_id AS CHAR) FROM sys_user_dept_job_new WHERE dept_id=?1 AND job_id=?2  AND del_flag=0",nativeQuery = true)
    List<String> findUserIdsByDeptJob(Long deptId,Long jobId);

    /**
     * 查询指定用户的职务列表
     * @param userId
     * @return
     */
    @Query(value = "SELECT DISTINCT CAST(job_id AS CHAR) FROM sys_user_dept_job_new WHERE user_id=?1 AND del_flag=0",nativeQuery = true)
    List<String> findUserJobs(Long userId);

    /**
     * 查询用户的部门、职务以及职务类型
     * @param userId
     * @return
     */
    @Query(value = "SELECT sudjn.dept_id,sudjn.job_id,sjn.type  FROM sys_user_dept_job_new sudjn, sys_job_new sjn WHERE sudjn.job_id=sjn.id AND sudjn.user_id=?1  AND sudjn.del_flag=0",nativeQuery = true)
    List<Object[]> findUserDeptIdAndJobIdAndJobType(Long userId);

    /**
     * 查找用户的计调职务
     * @param userId
     * @return
     */
    @Query(value = "SELECT user_id,dept_id,job_id FROM sys_user_dept_job_new sudjn LEFT JOIN sys_job_new sjn ON sudjn.job_id = sjn.id WHERE user_id=?1 AND  sjn.type=0 AND sudjn.del_flag=0",nativeQuery = true)
    List<Object[]> findUserOperatorJobs(Long userId);
}

@Repository
class UserDeptJobNewDaoImpl extends BaseDaoImpl<UserDeptJobNew> implements UserDeptJobNewDaoCustom {
	
	
	
}