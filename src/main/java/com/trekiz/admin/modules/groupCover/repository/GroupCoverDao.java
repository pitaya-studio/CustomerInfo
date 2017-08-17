package com.trekiz.admin.modules.groupCover.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.groupCover.entity.GroupCover;

/**
 * 团期补位申请DAO
 * @author yakun.bai
 * @Date 2016-4-20
 */
public interface GroupCoverDao extends GroupCoverDaoCustom, CrudRepository<GroupCover, Long> {
	
	@Query("from GroupCover where activityGroup.id = ?1 and delFlag = " + Context.DEL_FLAG_NORMAL)
	public List<GroupCover> findGroupCoverList(Long groupId);
	
	@Query("from GroupCover where coverStatus=1 and activityGroup.id = ?1 and delFlag = " + Context.DEL_FLAG_NORMAL)
	public List<GroupCover> findGroupCoverListUndo(Long groupId);

	

//	public GroupCover findOne(String coverId);

}

interface GroupCoverDaoCustom extends BaseDao<GroupCover> {
	
	public List<Map<String, Object>> getCoverInfo(Integer coverId);

}

@Repository
class GroupCoverDaoImpl extends BaseDaoImpl<GroupCover> implements GroupCoverDaoCustom {

	@Override
	public List<Map<String, Object>> getCoverInfo(Integer coverId) {
		// TODO Auto-generated method stub
		return null;
	}
   
}