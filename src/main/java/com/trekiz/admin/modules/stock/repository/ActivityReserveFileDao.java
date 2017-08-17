package com.trekiz.admin.modules.stock.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.stock.entity.ActivityReserveFile;

/**
 * 切位信息DAO接口
 * @author zj
 *
 */
public interface ActivityReserveFileDao extends ActivityReserveFileDaoCustom,CrudRepository<ActivityReserveFile, Long>{

	public List<ActivityReserveFile> findByAgentIdAndSrcActivityId(Long agentId,Long srcActivityId);

	public List<ActivityReserveFile> findByAgentIdAndSrcActivityIdAndActivityGroupId(Long agentId,Long srcActivityId,Long activityGroupId);
    
	public List<ActivityReserveFile> findByAgentIdAndReserveOrderId(Long agentId,Long reserveOrderId);
}

/**
 * 自定义DAO接口
 * @author liangjingming
 *
 */
interface ActivityReserveFileDaoCustom extends BaseDao<ActivityReserveFile>{
	
}

/**
 * 自定义DAO接口实现
 * @author liangjingming
 *
 */
@Repository
class ActivityReserveFileDaoImpl extends BaseDaoImpl<ActivityReserveFile> implements ActivityReserveFileDaoCustom{
	
}