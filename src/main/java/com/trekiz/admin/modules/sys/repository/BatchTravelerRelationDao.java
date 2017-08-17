package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.BatchTravelerRelation;

/**
 * 游客批次关系DAO接口
 * @author ang.gao
 * @version 2015-05-25
 */
@Component("batchTravelerRelationDao")
class BatchTravelerRelationDaoImpl extends BaseDaoImpl<BatchTravelerRelation> implements BatchTravelerRelationDaoCustom {

}

/**
 * DAO自定义接口
 */
interface BatchTravelerRelationDaoCustom extends BaseDao<BatchTravelerRelation> {

}

public interface BatchTravelerRelationDao extends BatchTravelerRelationDaoCustom, CrudRepository<BatchTravelerRelation, Long> {

	@Query("from BatchTravelerRelation b where b.travelerId = ?1 and b.businessType = ?2")
	public List<BatchTravelerRelation> findBatchNo(Long travelerId,int businessType);
	
	@Query("from BatchTravelerRelation b where b.batchRecordNo = ?1 and b.businessType = ?2")
	public List<BatchTravelerRelation> findByBatchNo(String batchNo, int type);
	
	@Query("from BatchTravelerRelation b where b.batchRecordNo = ?1 and b.travelerId = ?2 and b.businessType = ?3")
	public BatchTravelerRelation findByBatchNoAndTid(String batchNo,Long tid,int type);
	
	@Query("from BatchTravelerRelation b where b.batchRecordNo=?1 and b.businessType='3'")
	public List<BatchTravelerRelation> findTravelerIdByNo(String batchNo);
	
	@Modifying
	@Query("delete from BatchTravelerRelation b where b.batchRecordNo = ?1 and b.travelerId = ?2 and b.businessType = ?3")
	public void deleteByBatchNoAndTid(String batchNo,Long tid,int type);
	
	@Modifying
	@Query("delete from BatchTravelerRelation b where b.batchRecordNo = ?1 and b.travelerId = ?2 and (b.businessType = '3' or b.businessType = '4') ")
	public void deleteTraveler(String batchNo,Long tid);
}