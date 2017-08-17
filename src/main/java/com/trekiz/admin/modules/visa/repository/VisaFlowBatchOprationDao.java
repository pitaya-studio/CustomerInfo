/**
 *
 */
package com.trekiz.admin.modules.visa.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;

/**
 * 系统批次号DAO接口
 */
//@Component("visaFlowBatchOprationDao")
public interface VisaFlowBatchOprationDao extends VisaFlowBatchOprationDaoCustom, CrudRepository<VisaFlowBatchOpration, Long>{

	@Query("from VisaFlowBatchOpration vb where vb.batchNo = ?1 and vb.busynessType = ?2")
	public VisaFlowBatchOpration findByBatchNo(String batchNo,String type);
	
	/**
	 * 根据批次号查询批量操作的类型
	 * @author jiachen
	 * @DateTime 2015年7月28日 下午8:18:24
	 * @param batchNo
	 * @return
	 * @return List<VisaFlowBatchOpration>
	 */
	public List<VisaFlowBatchOpration> findByBatchNo(String batchNo);
	
	/**
	 * 根据批次号和批次类型更新批次审批状态
	 * wxw added 2015-05-28
	 * @param batchNo
	 * @param reviewStatus
	 */
	@Modifying
	@Query("update VisaFlowBatchOpration set reviewStatus=2 where batchNo = ?1 and busynessType = ?2")
	public void updateVisaFlowBatchOprationStatusByBatchNo(String batchNo,String busynessType);
	
	
	/**
	 * 根据批次号和批次类型更新批次打印状态和时间
	 * wxw added 2015-05-28
	 * @param batchNo
	 * @param reviewStatus
	 */
	@Modifying
	@Query("update VisaFlowBatchOpration set printStatus=1, printTime=?3 where batchNo=?1 and busynessType=?2")
	public void updateVisaFlowBatchPrintTimeAndPrintStatus(String batchNo,String busynessType,Date printTime);
	
	/**
	 * 打印时更新最后修改时间和最后修改人
	 * @param batchNo
	 * @param busynessType
	 * @param printTime
	 * @param userId
	 * @author shijun.liu
	 */
	@Modifying
	@Query("update VisaFlowBatchOpration set updateTime=?3, updateBy=?4 where batchNo=?1 and busynessType=?2")
	public void updateVisaFlowBatchUpdateTime(String batchNo,String busynessType,Date printTime, Long userId);
	
}

interface VisaFlowBatchOprationDaoCustom extends BaseDao<VisaFlowBatchOpration> {

	// 老审批
	public List<Map<String, Object>> getVisaFlowReviewInfo(String batchNo);
	// 新审批
	public List<Map<String, Object>> getVisaFlowReviewNewInfo(String batchNo);
}

@Repository
class VisaFlowBatchOprationDaoImpl extends BaseDaoImpl<VisaFlowBatchOpration> implements VisaFlowBatchOprationDaoCustom {

	/**
	 * 根据批量号和老审批表review及相关表进行联表查询，获取相关信息。该查询原先写在Controller里面，为了规范提取到Dao里面。
	 * @param batchNo
	 * @return
	 */
	public List<Map<String, Object>> getVisaFlowReviewInfo(String batchNo){
		String sql = "SELECT revd.myValue AS batchno, rev.createBy AS createBy, rev.id AS reviewid," +
				" rev.orderId AS orderid,rev.travelerId AS travelerid, rev.nowLevel as curentlevel,tr.`name` " +
				" AS travelername FROM review rev, review_detail revd, visa_flow_batch_opration vfbo, traveler tr" +
				" WHERE rev.id = revd.review_id AND revd.myKey = 'visaBorrowMoneyBatchNo' AND revd.myValue= vfbo.batch_no" +
				" AND vfbo.busyness_type = 2 AND rev.travelerId = tr.id AND vfbo.batch_no=? ";
		List<Map<String, Object>> result = findBySql(sql,Map.class,batchNo);
		return result;
	}

	/**
	 * 根据批量号和新审批表review_new进行联表查询，获取审批表中的保存的相关信息。该查询原先写在Controller里面，为了规范提取到Dao里面。
	 * @param batchNo
	 * @return
	 */
	public List<Map<String, Object>> getVisaFlowReviewNewInfo(String batchNo){
		String sql = "SELECT revn.batch_no AS batchno, revn.create_by AS createBy, revn.id AS reviewid," +
				" revn.order_id AS orderid,revn.traveller_id AS travelerid, revn.traveller_name AS travelername" +
				" FROM review_new revn, visa_flow_batch_opration vfbo WHERE revn.batch_no = vfbo.batch_no" +
				" AND vfbo.busyness_type = 2 AND vfbo.batch_no=?";
		List<Map<String, Object>> result = findBySql(sql,Map.class,batchNo);
		return result;
	}



}
