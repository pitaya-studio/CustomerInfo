package com.trekiz.admin.modules.grouphandle.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandle;

public interface GroupHandleDao extends GroupHandleDaoCustom, CrudRepository<GroupHandle, Long> {
	
	@Query("from GroupHandle where orderId = ?1 and delFlag=0")
	GroupHandle findByOrderId(Integer orderId);
	
}

interface GroupHandleDaoCustom extends BaseDao<GroupHandle> {
}

@Repository
class GroupHandleDaoImpl extends BaseDaoImpl<GroupHandle> implements GroupHandleDaoCustom{
}
