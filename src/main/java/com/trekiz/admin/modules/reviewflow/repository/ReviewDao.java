package com.trekiz.admin.modules.reviewflow.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewflow.entity.Review;



public interface ReviewDao extends ReviewDaoCustom,
		CrudRepository<Review, Long> {
	@Query("from Review where id = ?1 and active=1")
	public List<Review> findReviewActive(Long id);
	/**
	 * 通过游客id查询该游客的所有审核记录
	 * @param id
	 * @return
	 */
	@Query("from Review where travelerId = ?1")
	public List<Review> findByTravelerId(Long id);
	
	/**
	 * 通过游客id查询该游客所有审核中的记录
	 * @param id
	 * @return
	 */
	@Query("from Review where travelerId = ?1 and active=1 and status=1 and productType=6 and flowType in(5,9,10) ")
	public List<Review> getReviewingRecordBytId(Long id);
	
	/**
	 * 通过游客id查询该游客所有审核通过的记录
	 * @param id
	 * @return
	 */
	@Query("from Review where travelerId = ?1 and active=1 and (status=2 or status=3) and productType=6 and flowType in(5,9,10) ")
	public List<Review> getSuccessedBytId(Long id);
	
	/**
	 * 查询某订单下的团队返佣通过与审核中的记录
	 * @param orderId
	 * @return
	 */
	@Query("from Review where active=1 and (status=2 or status=3 or status=1) and travelerId is null and flowType=9 and orderId=?1")
	public List<Review> getSuccessedBatchBytId(String orderId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and companyId=?3 and active=1")
	public List<Review> findReviewCompanyActive(Integer productType,
			Integer flowType, Long companyId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and companyId=?3  and deptId in (?4)  and active=1")
	public List<Review> findReviewCompanyActiveDept(Integer productType,
			Integer flowType, Long companyId,List<Long> deptId);
	
	@Query("from Review where productType <6 and flowType=?1 and companyId=?2  and deptId in (?3)  and active=1")
	public List<Review> findReviewCompanyActiveDept(Integer flowType, Long companyId,List<Long> deptId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and companyId=?3  and deptId=?4  and active=1")
	public List<Review> findReviewCompanyActiveDept(Integer productType,
			Integer flowType, Long companyId,Long deptId);

	/* 这个查询是用来处理查询所有产品类型的某种流程的审批记录 add by chy 2014年11月26日10:55:24 */
	@Query("from Review where flowType=?1 and companyId=?2 and active=1")
	public List<Review> findReviewCompanyAllPrdActive(Integer flowType,
			Long companyId);
	
	/* 这个查询是用来处理查询所有产品类型的某种流程的审批记录 add by chy 2014年11月26日10:55:24 */
	@Query("from Review where flowType=?1 and companyId=?2 and  deptId in (?3)  and active=1")
	public List<Review> findReviewCompanyAllPrdActiveDept(Integer flowType,
			Long companyId,List<Long> deptId);
	
	/* 这个查询是用来处理查询所有产品类型的某种流程的审批记录 add by chy 2014年11月26日10:55:24 */
	@Query("from Review where flowType=?1 and companyId=?2 and  deptId=?3  and active=1")
	public List<Review> findReviewCompanyAllPrdActiveDept(Integer flowType,
			Long companyId,Long deptId);
	

	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and active=1 order by createDate asc")
	public List<Review> findReviewActive(Integer productType, Integer flowType, String orderId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and deptId=?4 and active=1")
	public List<Review> findReviewActiveDept(Integer productType, Integer flowType, String orderId,Long deptId);
	
	//新行者账号申请签证退款时，会根据不同职务，生成不同类型审核流程 addBy jiachen
	@Query("from Review where productType = ?1 and flowType in ?2 and orderId=?3 and deptId=?4 and active=1")
	public List<Review> findReviewActiveDept(Integer productType, List<Integer> flowType, String orderId, Long deptId);

	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and travelerId is null and active=1")
	public List<Review> findReviewCheck(Integer productType, Integer flowType, String orderId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and deptId=?4 and travelerId is null and active=1")
	public List<Review> findReviewCheckDept(Integer productType, Integer flowType, String orderId, Long deptId);

	//新加了过滤条件 查询审核状态不是已驳回的记录 and status <> 0 by chy 2014年12月3日10:49:29
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and travelerId=?4 and active=1 and status <> 0")
	public List<Review> findReviewActive(Integer productType, Integer flowType, String orderId, Long travelerId);
	
	//新加了过滤条件 查询审核状态不是已驳回的记录 and status = 1 ---------------by wxw 2015年05月5日16:51:12
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and active=1 and status = 1")
	public List<Review> findReviewActive4XXZ(Integer productType, Integer flowType,String orderId);
	
	//环球行 and status = 1 ---------------by wxw 2015年06月26日16:51:12
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and travelerId=?4 and active=1 and status = 1")
	public List<Review> findReviewActive4HQX(Integer productType, Integer flowType,String orderId,Long travelerId);
	
	
	//新加了过滤条件 查询审核状态不是已驳回的记录 and status <> 0 by chy 2014年12月3日10:49:29
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3  and travelerId=?4 and deptId=?5 and active=1 and status <> 0")
	public List<Review> findReviewActiveDept(Integer productType, Integer flowType,
			String orderId, Long travelerId,Long deptId);
	

	@Query("from Review where id = ?1")
	public List<Review> findReview(Long id);

	@Query("from Review where productType = ?1 and flowType=?2 and companyId=?3 ")
	public List<Review> findReviewCompany(Integer productType,
			Integer flowType, Long companyId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and companyId=?3 and deptId in (?4)  ")
	public List<Review> findReviewCompanyDept(Integer productType,
			Integer flowType, Long companyId, List<Long> deptId);
	
	@Query("from Review where productType<6 and flowType=?1 and companyId=?2 and deptId in (?3)  ")
	public List<Review> findReviewCompanyDept(Integer flowType, Long companyId, List<Long> deptId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and companyId=?3 and deptId=?4  ")
	public List<Review> findReviewCompanyDept(Integer productType,
			Integer flowType, Long companyId, Long deptId);

	/* 这个查询是用来处理查询所有产品类型的某种流程的审批记录 不区分active add by chy 2014年11月26日10:55:24 */
	@Query("from Review where flowType=?1 and companyId=?2 ")
	public List<Review> findReviewCompanyAllPrd(Integer flowType, Long companyId);
	
	/* 这个查询是用来处理查询所有产品类型的某种流程的审批记录 不区分active add by chy 2014年11月26日10:55:24 */
	@Query("from Review where flowType=?1 and companyId=?2  and deptId in (?3)  ")
	public List<Review> findReviewCompanyAllPrdDept(Integer flowType, Long companyId,List<Long> deptId);
	
	/* 这个查询是用来处理查询所有产品类型的某种流程的审批记录 不区分active add by chy 2014年11月26日10:55:24 */
	@Query("from Review where flowType=?1 and companyId=?2  and deptId=?3   ")
	public List<Review> findReviewCompanyAllPrdDept(Integer flowType, Long companyId,Long deptId);

	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 order by createDate desc")
	public List<Review> findReview(Integer productType, Integer flowType, String orderId);
	
	@Query("from Review where reviewCompanyId=?1 and status=1 and productType = ?2  and nowLevel=?3 ")
	public List<Review> findReviewForPass(Long reviewCompanyId,Integer productType, Integer nowLevel);
	
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 order by createDate desc")
	public List<Review> findReviewSortByCreateDate(Integer productType, Integer flowType,
			String orderId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and status=?3 and orderId=?4 order by id desc")
	public List<Review> findReviewIdByOrderId(Integer productType, Integer flowType, Integer status, String orderId);
	
	@Query("from Review where productType = ?1 and flowType=?2  and orderId=?3 and deptId=?4")
	public List<Review> findReviewDept(Integer productType, Integer flowType, String orderId, Long deptId);
	
	//新行者账号申请签证退款时，会根据不同职务，生成不同类型审核流程 addBy jiachen
	@Query("from Review where productType = ?1 and flowType in ?2  and orderId=?3 and deptId=?4")
	public List<Review> findReviewDept(Integer productType, List<Integer> flowType,
			String orderId,Long deptId);
	
	/**
	 * 增加Review的id属性查询，查询单个改价信息
	 */
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and id=?4")
	public List<Review> findReviewById(Integer productType, Integer flowType,
			String orderId,Long id);
	
	

	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and travelerId=?4")
	public List<Review> findReview(Integer productType, Integer flowType,
			String orderId, Long travelerId);
	
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3 and travelerId=?4 and deptId=?5")
	public List<Review> findReviewDept(Integer productType, Integer flowType,
			String orderId, Long travelerId,Long deptId);

	@Modifying
	@Query("update Review set active=0  where id = ?1")
	public void removeReview(Long id);
	
	/* 取消某条审核,设置 status=4,active=0 */
	@Modifying
	@Query("update Review set status=4,active=0   where id = ?1")
	public void removeMyReview(Long id);
	
	/* 付款确认或撤销,设置 payStatus=1(已付款)0(未付款) */
	@Modifying
	@Query("update Review set payStatus=?1, updateBy=?2, updateDate=?3, pay_confirm_date=?5 where id=?4")
	public void confirmOrCancelPay(Integer payStatus, Long userId, Date nowDate, Long id, Date confirmPayDate);
		
	/* 取消某条审核,设置 status=4,active=0 */
	@Modifying
	@Query("update Review set status=4,active=0 where productType = ?1 and flowType=?2 and orderId=?3")
	public void removeReview(Integer productType, Integer flowType,
			String orderId);

	/* 取消某条审核,设置 status=4,active=0 */
	@Modifying
	@Query("update Review set status=4,active=0 where productType = ?1 and flowType=?2 and orderId=?3 and deptId=?4")
	public void removeReviewDept(Integer productType, Integer flowType,
			String orderId,Long deptId);
	
	
	/* 取消某条审核,设置 status=4,active=0 */
	@Modifying
	@Query("update Review set status=4,active=0 where productType = ?1 and flowType=?2 and orderId=?3 and travelerId=?4")
	public void removeReview(Integer productType, Integer flowType,
			String orderId, Long travelerId);

	/* 取消某条审核,设置 status=4,active=0 */
	@Modifying
	@Query("update Review set status=4,active=0 where productType = ?1 and flowType=?2 and orderId=?3 and travelerId=?4 and deptId=?5")
	public void removeReviewDept(Integer productType, Integer flowType,
			String orderId, Long travelerId,Long deptId);
	
	@Modifying
	@Query("update Review set nowLevel=?2,status=?3,updateBy=?4, updateByName=?5, updateDate=?6,denyReason=?7  where id=?1")
	public void UpdateReview(Long id, Integer nowLevel, Integer status,
			Long updateBy, String updateByName, Date UpdateDate,
			String denyReason);
	
	/**
	 * 在业务上没有后续操作：整个流程审批及后续操作完毕
	 * @param id
	 * @param status
	 */
	@Modifying
	@Query("update Review set status=?2  where id =?1")
	public void reviewOperationDone(Long id, Integer status);

	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3  and status=?4 and active=1")
	public List<Review> findReviewActive(Integer productType, Integer flowType, String orderId, Integer status);

	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3  and status=?4 and deptId=?5 and active=1")
	public List<Review> findReviewActiveStatus(Integer productType, Integer flowType, String orderId, Integer status, Long deptId);

	
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3  and status=?4 ")
	public List<Review> findReview(Integer productType, Integer flowType, String orderId, Integer status);
	
	@Query("from Review where productType = ?1 and flowType=?2 and orderId=?3  and status=?4 and deptId=?5")
	public List<Review> findReviewDept(Integer productType, Integer flowType, String orderId, Integer status,Long deptId);
	
	
	
	@Query("from Review where  flowType =?1 and productType = ?2 and orderId = ?3 and status = ?4")
	public List<Review> queryWorkFlowByStatus(Integer flowType,Integer productType,String orderId , Integer status);
	
	
	@Query("from Review where  flowType =?1 and productType = ?2 and orderId = ?3 and status = ?4 and deptId=?5")
	public List<Review> queryWorkFlowByStatusDept(Integer flowType,Integer productType,String orderId , Integer status,Long deptId);
	
	@Modifying
	@Query("update Review set printTime=?2, updateDate=?2, updateBy=?4, printFlag=?3 where id=?1")
	public void updateReviewPrintInfoById(Long id, Date printTime,Integer printFlag, Long userId);
	
	@Query("from Review where  productType =6 and status =2 and flowType =5 and travelerId = ?1")
	public List<Review> validateBorrowMoney(Long travelerId);
	@Query("from Review where  flowType =5 and travelerId = ?1")
	public List<Review> getReview(Long travleId);
	
	//查询某游客所有签证相关的流程  added by wangxinwei
	@Query("from Review where productType = ?1 and flowType in ?2  and travelerId=?3 and orderId=?4  and status=1 and active=1")
	public List<Review> findReview4WithdrawalVisa(Integer productType, List<Integer> flowType,Long travelerId,String orderId);
	
	//查询某游客所有机票相关的流程  add by chy2015年4月8日17:39:13
	@Query("from Review where productType = ?1 and flowType in ?2  and travelerId in ?3 and orderId=?4  and status=1 and active=1")
	public List<Review> findReview4Airticket(Integer productType, List<Integer> flowType,List<Long> travelerIds,String orderId);
	
	@Modifying
	@Query("update Review set updateBy=?1, updateDate=?2 where id=?3")
	public int updateOptionReview(Long userId, Date nowDate, Long id);
}
interface ReviewDaoCustom extends BaseDao<Review> {
	
}

class ReviewDaoImpl extends BaseDaoImpl<Review> implements ReviewDaoCustom {
	
}