package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysUserReviewCommonPermission;

public interface UserPermissionDao extends UserPermissionDaoCustom, CrudRepository<SysUserReviewCommonPermission, Long> {

    /**
     * 查找用户审批权限
     * @param userId
     * @return
     */
    @Query("from SysUserReviewCommonPermission where user_id=?1  and del_flag=0")
    List<SysUserReviewCommonPermission> findByUserId(Long userId);
}

interface UserPermissionDaoCustom extends BaseDao<SysUserReviewCommonPermission> {
	
}

@Repository
class UserPermissionDaoImpl extends BaseDaoImpl<SysUserReviewCommonPermission> implements UserPermissionDaoCustom {

}

