package com.trekiz.admin.modules.order.rebates.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;

/**
 * 
 *  文件名: RebatesDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-12-16 上午11:44:31 
 *  @version 1.0
 */
public interface RebatesDao extends RebatesDaoCustom, CrudRepository<Rebates, Long> {
	@Query("from Rebates where travelerId != null and orderId = ?1 and delFlag = "+Context.DEL_FLAG_NORMAL)
	public List<Rebates> findTravelerRebatesListByOrderId(Long orderId);
	
	@Query("from Rebates where orderId = ?1 and orderType = ?2  and delFlag = "+Context.DEL_FLAG_NORMAL)
	public List<Rebates> findRebatesList(Long orderId,int orderType);
	
	@Query("from Rebates  where orderId = ?1 and orderType = ?2  and delFlag = "+Context.DEL_FLAG_NORMAL+" order by createDate desc")
	public List<Rebates> findRebatesListAir(Long orderId,int orderType);
	
	@Query("from Rebates where rid in (select id from Review where status=1 and productType = ?1 and flowType=?2 and orderId=?3  and delFlag = "+Context.DEL_FLAG_NORMAL+")  and delFlag = "+Context.DEL_FLAG_NORMAL)
	public List<Rebates> findRebatesListByStatus(Integer productType, Integer flowType, String orderId);
	
	@Query("select rebates from Rebates rebates,Review review where rebates.review.id = review.id and rebates.review.status=2 and rebates.travelerId = ?1  and rebates.delFlag = "+Context.DEL_FLAG_NORMAL+" order by rebates.id desc")
	public List<Rebates> findRebatesByTravelerAndStatus(Long travelerId);
	
	@Query("select rebates from Rebates rebates,Review review where rebates.review.id = review.id and rebates.review.status=2 and rebates.travelerId = ?1 and rebates.orderId=?2 and rebates.delFlag = "+Context.DEL_FLAG_NORMAL+" order by rebates.id desc")
	public List<Rebates> findRebatesByTravelerAndStatus(Long travelerId,Long orderId);
	
	@Query("select rebates from Rebates rebates,Review review where rebates.review.id = review.id and rebates.review.status=1 and rebates.orderId = ?1  and rebates.delFlag = "+Context.DEL_FLAG_NORMAL)
	public List<Rebates> findRebatesProcessStateByOrderId(Long orderId);
	
	@Query("from Rebates where rid = ?1 and delFlag = "+Context.DEL_FLAG_NORMAL)
	public Rebates findOneByRid(Long rid);
	
	@Query("from Rebates where rid = ?1 and delFlag = "+Context.DEL_FLAG_NORMAL)
	public List<Rebates> findOneByRidList(Long rid);
	
	@Query("from Rebates where rid = ?1 and delFlag = "+Context.DEL_FLAG_NORMAL)
	public List<Rebates> findListByRid(Long rid);
	/** 通过订单ID 获取报名时填写的团队返佣参考值 */
	@Query("from Rebates where orderId = ?1 and delFlag = "+Context.DEL_FLAG_TEMP)
	public List<Rebates> findListByOrderId(Long orderId);
	
	@Modifying
	@Query("update Rebates set rate = ?1  where rid = ?2")
	public void updateRate(BigDecimal rate, Long reviewId);
}
/**
 *  @author xiaoyang.tao
 */
interface RebatesDaoCustom extends BaseDao<Rebates> {

}

/**
 *  @author xiaoyang.tao
 */
@Repository
class RebatesDaoImpl extends BaseDaoImpl<Rebates> implements RebatesDaoCustom {
   
}