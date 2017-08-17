package com.trekiz.admin.modules.sys.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.UserDeptJob;


interface UserDeptJobDaoCustom extends BaseDao<UserDeptJob> {
}

public interface UserDeptJobDao extends UserDeptJobDaoCustom, CrudRepository<UserDeptJob, Long> {
	@Query("select deptId from UserDeptJob where userId=?1 and delFlag='0'")
	public List<Long> findDeptList(Long userId);
	
	@Query("select distinct userId from UserDeptJob where deptId=?1 and delFlag='0'")
	public List<Long> findUserIdList(Long deptId);
}

class UserDeptJobDaoImpl extends BaseDaoImpl<UserDeptJob> implements UserDeptJobDaoCustom {
	
	
	
}