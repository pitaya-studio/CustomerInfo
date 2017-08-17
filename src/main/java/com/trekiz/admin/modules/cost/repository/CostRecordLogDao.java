package com.trekiz.admin.modules.cost.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.cost.entity.CostRecordLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CostRecordLogDao extends CostRecordLogDaoCustom,CrudRepository<CostRecordLog, Long>{
	
	@Query("from CostRecordLog where rid=?1 and orderType=?2")
	public List<CostRecordLog> findCostRecordLogList(Long rid,Integer orderType);
	@Query("from CostRecordLog where costId=?1 and orderType=?2 and nowLevel=?3 and result=1")
	public List<CostRecordLog> findCostRecordLog(Long costId,Integer orderType,Integer nowLevel);
	
	/*
	@Modifying
	@Query("update CostRecordLog set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("update CostRecordLog set review=?2  where id = ?1")
	public int updateReview(Long id,Integer review); */
	
}

interface CostRecordLogDaoCustom extends BaseDao<CostRecordLog> {
	
}

@Repository
class CostRecordLogDaoImpl extends BaseDaoImpl<CostRecordLog> implements CostRecordLogDaoCustom{
	
	
}