package com.trekiz.admin.modules.sys.repository;


import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.UserDept;


interface UserDeptDaoCustom extends BaseDao<UserDept> {
}

public interface UserDeptDao extends UserDeptDaoCustom, CrudRepository<UserDept, Long> {
	   
	  
	   
}

class UserDeptDaoImpl extends BaseDaoImpl<UserDept> implements UserDeptDaoCustom {
	
	
	
}