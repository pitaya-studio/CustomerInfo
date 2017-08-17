package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.UserJob;

public interface UserJobDao extends UserJobDaoCustom, CrudRepository<UserJob, Long> {
	

	

	@Query("select jobId from UserJob where userId =?1 and jobType=3 ")
	public List<Long> getUserOperList(Long userId);
	
	@Query("select jobId from UserJob where userId =?1 and jobType=?2 ")
	public List<Integer> checkJobType(Long userId,Integer jobType);
	
	@Query("select jobId from UserJob where userId =?1 and (jobType=?2 or jobType=?3) ")
	public List<Integer> checkJobType(Long userId,Integer jobOperator,Integer jobTypeOperatorMgr);
	
 	@Query("from UserJob where userId =?1 order by id")
	public List<UserJob> getUserJobList(Long userId);
	
	@Query("select jobId from UserJob where userId =?1 and orderType=?2  and ((deptLevel=2 and deptId=?3) or (deptLevel=1 and deptId=?4)) ")
	public List<Long> getUserJobDeptList(Long userId,Integer orderType,Long deptId, Long parentId);
	

}


interface UserJobDaoCustom extends BaseDao<UserJob> {

}


@Repository
class UserJobDaoImpl extends BaseDaoImpl<UserJob> implements UserJobDaoCustom {

}