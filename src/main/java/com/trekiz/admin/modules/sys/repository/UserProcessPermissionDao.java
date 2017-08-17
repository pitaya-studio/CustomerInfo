package com.trekiz.admin.modules.sys.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysUserReviewProcessPermission;

public interface UserProcessPermissionDao extends UserProcessPermissionDaoCustom, CrudRepository<SysUserReviewProcessPermission, Long>{

}

interface UserProcessPermissionDaoCustom extends BaseDao<SysUserReviewProcessPermission> {
	
}

@Repository
class UserProcessPermissionDaoImpl extends BaseDaoImpl<SysUserReviewProcessPermission> implements UserProcessPermissionDaoCustom {

}