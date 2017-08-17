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
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.sys.entity.Dict;

public interface CostRecordDao extends CostRecordDaoCustom,CrudRepository<CostRecord, Long>{
	//and (reviewStatus=null or reviewStatus<>'已取消' and reviewStatus<>'已驳回')   预算成本显示所有的数据（包括审核通过的，未通过的，已驳回，已取消的成本审核数据、退款返佣数据）
	//@Query("from CostRecord where activityId=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and  (reviewType=0 or reviewStatus<>'已取消') and delFlag='0'")
	@Query("from CostRecord where activityId=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and (reviewType=0 or (reviewType<>0 and name<>'退款')) and delFlag='0' and reviewUuid is null and isNew=1 and visaId is null")
	public List<CostRecord> findCostRecordList(Long activityId,Integer budgetType,Integer overseas,Integer orderType);
	
	@Query("from CostRecord where activityId=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and (reviewType=0 or (reviewType<>0 and name<>'退款')) and delFlag='0' and visaId is null")
	public List<CostRecord> findNewAndOldCostRecordList(Long activityId,Integer budgetType,Integer overseas,Integer orderType);
	
	@Query("from CostRecord where activityId=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and (reviewType=?5 or (reviewType<>0 and name<>'退款')) and visaId is not null")
	public List<CostRecord> findCostRecordList(Long activityId,Integer budgetType,Integer overseas,Integer orderType,Integer reviewType);

    @Query("from CostRecord where activityId=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and reviewType<>1 and reviewType<>2 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecord> findCostRecordListpass(Long activityId,Integer budgetType,Integer overseas,Integer orderType);
    
    //查询除待提交之外的成本数据，C395 2015.11.20	add by shijun.liu
    @Query("from CostRecord where activityId=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and reviewType<>1 and reviewType<>2 and review not in(4,5) and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecord> findReviewCostRecordListpass(Long activityId,Integer budgetType,Integer overseas,Integer orderType);

	@Query("from CostRecord where activityId=?1 and budgetType=?2 and overseas=?3 and orderType=?4 and review=2 and isJoin=0 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecord> findOtherCostRecordList(Long activityId,Integer budgetType,Integer overseas,Integer orderType);
	
	@Query("from CostRecord where reviewUuid=?1 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public CostRecord getByReviewUuid(String reviewUuid);

	@Query("from CostRecord where reviewUuid=?1 and delFlag= ?2")
	public List<CostRecord> getByReviewUuid(String reviewUuid, String delFlag);
	
	@Query("from CostRecord where id=?1 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public CostRecord getById(Long id);
	
	@Modifying
	@Query("update CostRecord set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("update CostRecord set review=5,nowLevel=1  where id = ?1 and delFlag='0'")
	public int cancelById(Long id);
	
	@Modifying
	@Query("update CostRecord set payReview=5,nowLevel=1  where id = ?1 and delFlag='0'")
	@Deprecated
	public int cancelPayById(Long id);
	
	@Modifying
	@Query("update CostRecord set review=?2  where id = ?1")
	public int updateCostRecord(Long id,Integer review);
	
	@Modifying
	@Query("update CostRecord set payStatus=?1, updateBy=?2, updateDate=?3,confirmCashierDate=null where id=?4")
	public int confirmOrCancelPay(Integer payFlag, Long userId, Date nowDate, Long id);
	
	@Modifying
	@Query("update CostRecord set payStatus=?1, updateBy=?2, updateDate=?3,rate=?5 , priceAfter=?6, confirmCashierDate=?7 where id=?4")
	public int confirmOrCancelPay(Integer payFlag, Long userId, Date nowDate, Long id,BigDecimal rate,BigDecimal priceAfter,Date confirmCashierDate);
	
	@Modifying
	@Query("update CostRecord set review=?3,reviewStatus=?2  where id = ?1")
	public int updateCostRecord(Long id, String revStatus, Integer review);
	
	/*批量提交 预算成本审核*/
	@Modifying
	@Query("update CostRecord set review=1  where activityId = ?1 and budgetType=0  and orderType=?2 and review=?3")
	public int submitCostRecord(Long costId,Integer orderType,Integer review);
	
	@Modifying
	@Query("update CostRecord set review=?3  where activityId = ?1 and orderType=?2")
	public int updateCostRecord(Long activityId,Integer orderType,Integer review);
	/**
	 * 更新支付状态
	 * */
	@Modifying
	@Query("update CostRecord set payStatus=?2 where id=?1")
	public int updatePayStatus(Long id,Integer payStatus);
	@Modifying
	@Query("delete from CostRecord where activityId=?1 and orderType=?3 and budgetType=?2")
	public void delete(Long activityId,Integer budgetType,Integer orderType);

	@Modifying
	@Query("update CostRecord set rate = ?1 where reviewId = ?2")
	public void updateRate(BigDecimal rate, Long reviewId);
	
	/**
	 * 
	 * @param docIds
	 * @param costId
	 */
	@Modifying
	@Query("update CostRecord set costVoucher = ?1 where id = ?2")
	public int updateCostVoucher(String docIds, Long costId);
	
	
	
	/**
	 * add by chy 2015年4月14日16:50:20
	 * 根据审核id查询对应的记录
	 * @param reviewId
	 * @return
	 */
	@Query("from CostRecord where reviewId=?1 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecord> findCostRecordList(Long reviewId);
	
	@Query("from CostRecord where reviewId=?1 and budgetType=?2 and delFlag = " + Dict.DEL_FLAG_NORMAL)
	public List<CostRecord> findCostRecordList(Long reviewId, Integer budgetType);
	
	@Query("from CostRecord where reviewUuid=?1 and budgetType=?2 and delFlag = " + Dict.DEL_FLAG_NORMAL)
	public List<CostRecord> findCostRecordList(String reviewUuid, Integer budgetType);
	
	@Modifying
	@Query("update CostRecord set updateBy=?1, updateDate=?2 where id=?3")
	public int updateOptionCostRecord(Long userId, Date nowDate, Long id);
	
	/**
	 * 根据订单类型和团期id获取成本录入集合
		 * @Title: findByOrderTypeAndActivityId
	     * @return List<CostRecord>
	     * @author majiancheng       
	     * @date 2015-11-1 下午2:32:33
	 */
	@Query("from CostRecord where orderType=?1 and activityId=?2 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecord> getByOrderTypeAndActivityId(Integer orderType,Long activityId);
	
	/**
	 * 根据uuid查询成本记录
	 * @param uuid
	 * @return
	 * @author 	shijun.liu
	 */
	@Query("from CostRecord where uuid=?1 and delFlag="+Dict.DEL_FLAG_NORMAL)
	public CostRecord findByUUID(String uuid);
	
	/**
	 * 查询新审批的成本数据(delflag=1的也许要查询)
	 * @param activityId	产品或团期ID
	 * @param orderType		订单类型
	 * @param budgetOrActualType	成本类型0，预算成本，1，实际成本
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.17
	 */
	@Query("from CostRecord where activityId=?1 and orderType=?2 and budgetType=?3 and reviewType=0 and isNew=2 ") 
	public List<CostRecord> getNewReviewCostRecord(Long activityId, Integer orderType, Integer budgetOrActualType);
	
	/**
	 * 根据订单id、订单类型和地接社id获取成本记录集合
	 * @Description: 
	 * @param @param orderId
	 * @param @param orderType
	 * @param @param supplyId
	 * @param @return   
	 * @return List<CostRecord>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	@Query("from CostRecord where orderId=?1 and orderType=?2 and supplyId=?3 and budgetType=1 and payStatus in("+CostRecord.PAY_STATUS_PENDING+","+CostRecord.PAY_STATUS_ALREADY+","+CostRecord.PAY_STATUS_SUBMIT+") and delFlag="+Dict.DEL_FLAG_NORMAL) 
	public List<CostRecord> getByOrderIdAndSupplyId(Long orderId, Integer orderType, Integer supplyId);
	
	/**
	 * 根据订单id、订单类型获取未付款或已提交的成本记录
	 * @Description: 
	 * @param @param orderId
	 * @param @param orderType
	 * @param @return   
	 * @return List<CostRecord>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	@Query("from CostRecord where orderId=?1 and orderType=?2 and budgetType=1 and reviewType=0 and payStatus in ("+CostRecord.PAY_STATUS_PENDING+","+CostRecord.PAY_STATUS_SUBMIT+") and delFlag="+Dict.DEL_FLAG_NORMAL)
	public List<CostRecord> getByOrderIdAndOrderType(Long orderId, Integer orderType);
}

interface CostRecordDaoCustom extends BaseDao<CostRecord> {
	public List<Map<String, Object>> findCostRecordBySql(String sql);
	
	//查询所有批发商和渠道
	public List<Map<String,Object>> getAgentsAndSuppliers(Integer activityId, Integer orderType, Integer budgetType);
	//根据supplyId和supplyType查询指定地接社或者渠道商的指定产品id和类型的成本列表
	public List<Map<String,Object>> getCostRecordListYJXZ(Integer activityId, Integer orderType, Integer supplyId, Integer supplyType, Integer budgetType);
}

@Repository
class CostRecordDaoImpl extends BaseDaoImpl<CostRecord> implements CostRecordDaoCustom{

	@Override
	public List<Map<String, Object>> findCostRecordBySql(String sql) {
		return this.findBySql(sql, Map.class);		
	}

	/*
	 * 根据supplyId和supplyType对成本中的地接社or渠道商进行分组
	 * @param activityId 产品或者团期Id
	 * @param orderType 产品或者订单类型
	 */
	@Override
	public List<Map<String, Object>> getAgentsAndSuppliers(Integer activityId, Integer orderType, Integer budgetType) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("cost.supplyName,cost.supplyId,cost.supplyType,cost.currencyAfter,sum(cost.priceAfter) AS priceAfter ");
		sbf.append("FROM ");
		sbf.append("cost_record cost ");
		sbf.append("WHERE activityId = ? AND orderType = ? AND budgetType = ?");
		sbf.append(" AND ( reviewType = 0 OR ( reviewType <> 0 AND NAME <> '退款' )) ");
		sbf.append(" AND delFlag = '0' AND visaId IS NULL GROUP BY supplyId,supplyType");
		List<Map<String,Object>> list=this.findBySql(sbf.toString(), Map.class, activityId, orderType, budgetType);
		return list;
	}

	/*
	 * 根据supplyId和supplyType查询指定的地接社or渠道商下的指定产品id和产品type的成本列表
	 * @param activityId 产品或者团期Id
	 * @param orderType 产品或者订单类型
	 * @param supplyId	地接社or渠道商id
	 * @param supplyType 地接社or渠道商Type
	 */
	@Override
	public List<Map<String, Object>> getCostRecordListYJXZ(Integer activityId,
			Integer orderType, Integer supplyId, Integer supplyType, Integer budgetType) {
		
		String sql = "SELECT cost.name,cost.kb,cost.quantity,cost.supplyName,cost.currencyId, "
				+ "(select currency_mark from currency where currency_id=cost.currencyId) currencyMark," 
				+ "cost.price,cost.rate,cost.currencyAfter,cost.priceAfter,cost.`comment`,"
				+ "cost.createBy,cost.reviewType,cost.id,cost.reviewId,cost.budgetType,cost.overseas,review.id reviewUuid,CONCAT(review.status, '') status,review.current_reviewer,"
				+ "cost.reviewStatus,cost.delFlag,cost.visaId,cost.uuid,cost.activityId,cost.orderType,cost.pay_review_uuid,cost.payReview,"
				+ "(select n.status from review_new n where n.id = cost.pay_review_uuid) as pay_status,"
				+ "(select n.current_reviewer from review_new n where n.id = cost.pay_review_uuid) as pay_current_reviewer,"
				+ "CONCAT(cost.is_new,'') isNew,cost.review,CONCAT(review.need_no_review_flag,'') noReview,CONCAT(r.need_no_review_flag,'') noPayReview,"
				+ "cost.costVoucher"
				+ " from cost_record cost"
				+ " LEFT JOIN review_new review"
				+ " on cost.reviewUuid = review.id"
				+ " LEFT JOIN review_new r on cost.pay_review_uuid = r.id";
		
		StringBuilder sb = new StringBuilder(sql);
		sb.append(" WHERE activityId = ").append(activityId).append(" and orderType = ").append(orderType).append(" and budgetType = ").append(budgetType)
			.append(" and (reviewType = 0 or (reviewType<>0 and name<>'退款')) and delFlag='0' and visaId is NULL ")
			.append(" and supplyId = ").append(supplyId)
			.append(" and supplyType = ").append(supplyType);

		return this.findBySql(sb.toString(), Map.class);
	}
}