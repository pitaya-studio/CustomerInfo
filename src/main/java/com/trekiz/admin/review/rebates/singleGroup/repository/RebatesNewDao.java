package com.trekiz.admin.review.rebates.singleGroup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;

/**
 * 新返佣DAO
 * @author yakun.bai
 * @Date 2015-11-26
 */
public interface RebatesNewDao extends RebatesNewDaoCustom, CrudRepository<RebatesNew, Long> {
	
	@Query("from RebatesNew where travelerId != null and orderId = ?1 and delFlag = " + Context.DEL_FLAG_NORMAL)
	public List<RebatesNew> findTravelerRebatesListByOrderId(Long orderId);
	
	@Query("from RebatesNew where orderId = ?1 and orderType = ?2  and delFlag = " + Context.DEL_FLAG_NORMAL + " order by createDate desc" )
	public List<RebatesNew> findRebatesList(Long orderId,int orderType);
	
	@Query("from RebatesNew  where orderId = ?1 and orderType = ?2  and delFlag = " + Context.DEL_FLAG_NORMAL + " order by createDate desc")
	public List<RebatesNew> findRebatesListAir(Long orderId,int orderType);
	
	@Query("from RebatesNew where rid in (select id from ReviewNew where status = 1 and productType = ?1 and processType = ?2 and orderId = ?3  and delFlag = " + 
				Context.DEL_FLAG_NORMAL+")  and delFlag = " + Context.DEL_FLAG_NORMAL)
	public List<RebatesNew> findRebatesListByStatus(String productType, String flowType, String orderId);
	
	@Query("select rebates from RebatesNew rebates, ReviewNew review where rebates.review.id = review.id and rebates.review.status = 2 " +
			"and rebates.travelerId = ?1 and rebates.delFlag = " + Context.DEL_FLAG_NORMAL + " order by rebates.id desc")
	public List<RebatesNew> findRebatesByTravelerAndStatus(Long travelerId);
	
	@Query("select rebates from RebatesNew rebates where rebates.review.status = 2 " +
			"and rebates.travelerId = ?1 and rebates.orderId = ?2 and rebates.delFlag = " + Context.DEL_FLAG_NORMAL + " order by rebates.id desc")
	public List<RebatesNew> findRebatesByTravelerAndStatus(Long travelerId, Long orderId);
	
	@Query("select rebates from RebatesNew rebates,Review review where rebates.review.id = review.id and rebates.review.status = 1 " +
			"and rebates.orderId = ?1  and rebates.delFlag = " + Context.DEL_FLAG_NORMAL)
	public List<RebatesNew> findRebatesProcessStateByOrderId(Long orderId);
	
	@Query("from RebatesNew where rid = ?1 and delFlag = " + Context.DEL_FLAG_NORMAL)
	public RebatesNew findOneByRid(String rid);
	
	@Query("from RebatesNew where rid = ?1 and delFlag = " + Context.DEL_FLAG_NORMAL)
	public List<RebatesNew> findOneByRidList(String rid);
	
	@Query("from RebatesNew where rid = ?1 and delFlag = " + Context.DEL_FLAG_NORMAL)
	public List<RebatesNew> findListByRid(String rid);
	
	/** 通过订单ID 获取报名时填写的团队返佣参考值 */
	@Query("from RebatesNew where orderId = ?1 and delFlag = " + Context.DEL_FLAG_TEMP)
	public List<RebatesNew> findListByOrderId(Long orderId);
}

interface RebatesNewDaoCustom extends BaseDao<RebatesNew> {

}

@Repository
class RebatesNewDaoImpl extends BaseDaoImpl<RebatesNew> implements RebatesNewDaoCustom {
   
}