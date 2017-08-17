package com.trekiz.admin.modules.cost.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.cost.entity.CostRecordIsland;
import com.trekiz.admin.modules.sys.entity.Dict;

public interface CostRecordIslandDao extends CostRecordIslandDaoCustom,CrudRepository<CostRecordIsland, Long>{
	//and (reviewStatus=null or reviewStatus<>'已取消' and reviewStatus<>'已驳回')   预算成本显示所有的数据（包括审核通过的，未通过的，已驳回，已取消的成本审核数据、退款返佣数据）
	//@Query("from CostRecordIsland where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and  (reviewType=0 or reviewStatus<>'已取消') and delFlag='0'")
	@Query("from CostRecordIsland where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and (reviewType=0 or (reviewType<>0 and name<>'退款')) and delFlag='0'")
	public List<CostRecordIsland> findCostRecordIslandList(Long activityUuid,Integer budgetType,Integer overseas,Integer orderType);

    @Query("from CostRecordIsland where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and reviewType<>1 and reviewType<>2 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecordIsland> findCostRecordIslandListpass(String activityUuid,Integer budgetType,Integer overseas,Integer orderType);
    
    @Query("from CostRecordIsland where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and reviewType<>1 and reviewType<>2 and review not in(4,5) and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecordIsland> findReviewCostRecordIslandListpass(String activityUuid,Integer budgetType,Integer overseas,Integer orderType);

	@Query("from CostRecordIsland where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and review=2 and isJoin=0 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecordIsland> findOtherCostRecordIslandList(Long activityUuid,Integer budgetType,Integer overseas,Integer orderType);
	
	@Modifying
	@Query("update CostRecordIsland set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("update CostRecordIsland set review=5,nowLevel=1  where id = ?1 and delFlag='0'")
	public int cancelById(Long id);
	
	@Modifying
	@Query("update CostRecordIsland set payReview=5,nowLevel=1  where id = ?1 and delFlag='0'")
	public int cancelPayById(Long id);
	
	@Modifying
	@Query("update CostRecordIsland set review=?2  where id = ?1")
	public int updateCostRecordIsland(Long id,Integer review);
	
	@Modifying
	@Query("update CostRecordIsland set payStatus=?1, updateBy=?2, updateDate=?3 where id=?4")
	public int confirmOrCancelPay(Integer payFlag, Long userId, Date nowDate, Long id);
	
	@Modifying
	@Query("update CostRecordIsland set payStatus=?1, updateBy=?2, updateDate=?3, rate=?5 , priceAfter=?6 where id=?4")
	public int confirmOrCancelPay(Integer payFlag, Long userId, Date nowDate, Long id,BigDecimal rate,BigDecimal afterPrice);
	
	@Modifying
	@Query("update CostRecordIsland set review=?3,reviewStatus=?2  where id = ?1")
	public int updateCostRecordIsland(Long id, String revStatus, Integer review);
	
	/*批量提交 预算成本审核*/
	@Modifying
	@Query("update CostRecordIsland set review=1  where activityUuid = ?1 and budgetType=0  and orderType=?2 and review=?3")
	public int submitCostRecordIsland(Long costId,Integer orderType,Integer review);
	
	@Modifying
	@Query("update CostRecordIsland set review=?3  where activityUuid = ?1 and orderType=?2")
	public int updateCostRecordIsland(Long activityUuid,Integer orderType,Integer review);
	/**
	 * 更新支付状态
	 * */
	@Modifying
	@Query("update CostRecordIsland set payStatus=?2 where id=?1")
	public int updatePayStatus(Long id,Integer payStatus);
	@Modifying
	@Query("delete from CostRecordIsland where activityUuid=?1 and orderType=?3 and budgetType=?2")
	public void delete(Long activityUuid,Integer budgetType,Integer orderType);
	
	/**
	 * add by chy 2015年4月14日16:50:20
	 * 根据审核id查询对应的记录
	 * @param reviewId
	 * @return
	 */
	@Query("from CostRecordIsland where reviewId=?1 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecordIsland> findCostRecordIslandList(Long reviewId);
	
	@Query("from CostRecordIsland where reviewId=?1 and budgetType=?2 and delFlag = " + Dict.DEL_FLAG_NORMAL)
	public List<CostRecordIsland> findCostRecordIslandList(Long reviewId, Integer budgetType);

	@Query("from CostRecordIsland where activityUuid=?1 and budgetType=?2 and overseas=?3  and (reviewType=0 or (reviewType<>0 and name<>'退款')) and delFlag='0'")
	public List<CostRecordIsland> findCostIslandList(String activityUuid,Integer budgetType,Integer overseas);

	@Modifying
	@Query("update CostRecordIsland set updateBy=?1, updateDate=?2 where id=?3")
	public int updateOptionCostRecord(Long userId, Date nowDate, Long id);
	
	/**
	 * 根据uuid查询成本记录
	 * @param uuid
	 * @return
	 * @author 	shijun.liu
	 */
	@Query("from CostRecordIsland where uuid=?1 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public CostRecordIsland findByUUID(String uuid);
}

interface CostRecordIslandDaoCustom extends BaseDao<CostRecordIsland> {
	public List<Map<String, Object>> findCostRecordIslandBySql(String sql);
}

@Repository
class CostRecordIslandDaoImpl extends BaseDaoImpl<CostRecordIsland> implements CostRecordIslandDaoCustom{

	@Override
	public List<Map<String, Object>> findCostRecordIslandBySql(String sql) {
		return this.findBySql(sql, Map.class);
		
	}
	
	
}