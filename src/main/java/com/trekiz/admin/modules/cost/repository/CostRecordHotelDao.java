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
import com.trekiz.admin.modules.cost.entity.CostRecordHotel;
import com.trekiz.admin.modules.sys.entity.Dict;

public interface CostRecordHotelDao extends CostRecordHotelDaoCustom,CrudRepository<CostRecordHotel, Long>{
	//and (reviewStatus=null or reviewStatus<>'已取消' and reviewStatus<>'已驳回')   预算成本显示所有的数据（包括审核通过的，未通过的，已驳回，已取消的成本审核数据、退款返佣数据）
	//@Query("from CostRecordHotel where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and  (reviewType=0 or reviewStatus<>'已取消') and delFlag='0'")
	@Query("from CostRecordHotel where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and (reviewType=0 or (reviewType<>0 and name<>'退款')) and delFlag='0'")
	public List<CostRecordHotel> findCostRecordHotelList(Long activityUuid,Integer budgetType,Integer overseas,Integer orderType);

    @Query("from CostRecordHotel where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and reviewType<>1 and reviewType<>2 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecordHotel> findCostRecordHotelListpass(String activityUuid,Integer budgetType,Integer overseas,Integer orderType);
    
    @Query("from CostRecordHotel where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and reviewType<>1 and reviewType<>2 and review not in(4,5) and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecordHotel> findReviewCostRecordHotelListpass(String activityUuid,Integer budgetType,Integer overseas,Integer orderType);

	@Query("from CostRecordHotel where activityUuid=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and review=2 and isJoin=0 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecordHotel> findOtherCostRecordHotelList(Long activityUuid,Integer budgetType,Integer overseas,Integer orderType);
	
	@Modifying
	@Query("update CostRecordHotel set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("update CostRecordHotel set review=5,nowLevel=1  where id = ?1 and delFlag='0'")
	public int cancelById(Long id);
	
	@Modifying
	@Query("update CostRecordHotel set payReview=5,nowLevel=1  where id = ?1 and delFlag='0'")
	public int cancelPayById(Long id);
	
	@Modifying
	@Query("update CostRecordHotel set review=?2  where id = ?1")
	public int updateCostRecordHotel(Long id,Integer review);
	
	@Modifying
	@Query("update CostRecordHotel set payStatus=?1, updateBy=?2, updateDate=?3 where id=?4")
	public int confirmOrCancelPay(Integer payFlag, Long userId, Date nowDate, Long id);
	
	@Modifying
	@Query("update CostRecordHotel set payStatus=?1, updateBy=?2, updateDate=?3,rate=?5,priceAfter=?6 where id=?4")
	public int confirmOrCancelPay(Integer payFlag, Long userId, Date nowDate, Long id,BigDecimal rate,BigDecimal priceAfter);
	
	@Query("from CostRecordHotel where activityUuid=?1 and budgetType=?2 and overseas=?3  and (reviewType=0 or (reviewType<>0 and name<>'退款')) and delFlag='0'")
	public List<CostRecordHotel> findCostHotelList(String activityUuid,Integer budgetType,Integer overseas);


	@Modifying
	@Query("update CostRecordHotel set review=?3,reviewStatus=?2  where id = ?1")
	public int updateCostRecordHotel(Long id, String revStatus, Integer review);
	
	/*批量提交 预算成本审核*/
	@Modifying
	@Query("update CostRecordHotel set review=1  where activityUuid = ?1 and budgetType=0  and orderType=?2 and review=?3")
	public int submitCostRecordHotel(Long costId,Integer orderType,Integer review);
	
	@Modifying
	@Query("update CostRecordHotel set review=?3  where activityUuid = ?1 and orderType=?2")
	public int updateCostRecordHotel(Long activityUuid,Integer orderType,Integer review);
	/**
	 * 更新支付状态
	 * */
	@Modifying
	@Query("update CostRecordHotel set payStatus=?2 where id=?1")
	public int updatePayStatus(Long id,Integer payStatus);
	@Modifying
	@Query("delete from CostRecordHotel where activityUuid=?1 and orderType=?3 and budgetType=?2")
	public void delete(Long activityUuid,Integer budgetType,Integer orderType);
	
	/**
	 * add by chy 2015年4月14日16:50:20
	 * 根据审核id查询对应的记录
	 * @param reviewId
	 * @return
	 */
	@Query("from CostRecordHotel where reviewId=?1 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecordHotel> findCostRecordHotelList(Long reviewId);
	
	@Query("from CostRecordHotel where reviewId=?1 and budgetType=?2 and delFlag = " + Dict.DEL_FLAG_NORMAL)
	public List<CostRecordHotel> findCostRecordHotelList(Long reviewId, Integer budgetType);
	
	@Modifying
	@Query("update CostRecordHotel set updateBy=?1, updateDate=?2 where id=?3")
	public int updateOptionCostRecord(Long userId, Date nowDate, Long id);
	
	/**
	 * 根据uuid查询成本记录
	 * @param uuid
	 * @return
	 * @author 	shijun.liu
	 */
	@Query("from CostRecordHotel where uuid=?1 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public CostRecordHotel findByUUID(String uuid);
}

interface CostRecordHotelDaoCustom extends BaseDao<CostRecordHotel> {
	public List<Map<String, Object>> findCostRecordHotelBySql(String sql);
}

@Repository
class CostRecordHotelDaoImpl extends BaseDaoImpl<CostRecordHotel> implements CostRecordHotelDaoCustom{

	@Override
	public List<Map<String, Object>> findCostRecordHotelBySql(String sql) {
		return this.findBySql(sql, Map.class);
		
	}
	
	
}