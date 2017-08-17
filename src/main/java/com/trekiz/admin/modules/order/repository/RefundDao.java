/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.sys.utils.UserUtils;

public interface RefundDao extends RefundCustom, CrudRepository<Refund, Long> {
   
	@Query("from Refund where id = ?1 and delFlag = '0'")
	public Refund findPayInfoByPayId(String payId);
	@Query("from Refund where id = ?1 and orderType = ?2 and delFlag = '0'")
	public Refund findPayInfoByPayId(String payId,Integer orderType);
	@Query("from Refund where recordId = ?1 and moneyType = ?2 and delFlag = '0' order by createDate")
	public List<Refund> findByRecordIdWithOrder(Long recordId,Integer moneyType);
	@Query("from Refund where recordId=?1 and moneyType=?2 and status is null and  delFlag='0'")
	public List<Refund> findByRecordId(Long recordId,Integer moneyType);
	@Query("from Refund where recordId=?1 and orderType=?2 and moneyType=1 and status is null and  delFlag='0'")
	public List<Refund> findRefund(Long recordId,Integer orderType);
	@Query("from Refund where recordId = ?1 and orderType = ?2 and delFlag='0'")
	public List<Refund> findByRecordIdAndOrderType(Long recordId,Integer orderType);
	@Query("from Refund where recordId = ?1 and moneyType = ?2 and orderType = ?3 and delFlag = '0' order by createDate desc")
	public List<Refund> findLastPayByRecordId(Long recordId,Integer moneyType,Integer orderType);
	@Query("from Refund where recordId = ?1 and delFlag = '0' and status is null order by createDate desc")
	public List<Refund> findLastPayByRecordIdAndStatus(Long recordId);
	@Query("from Refund where recordId =?1 and moneyType=?2 and companyUuid=?3 and delFlag='0' and status is null")
	public List<Refund> findRefund(Long recordId,Integer moneyType,String companyUuid);
	/**
	 * 根据订单类型、款项类型、批发商uuid和相应的aircketOrderMoneyAmount的id获取已支付信息
	 * @Title: findByRecordIdAndOrderType
	 * @return List<Refund>
	 * @author majiancheng
	 * @date 2015-11-2 下午4:08:00
	 */
	@Query("from Refund where recordId = ?1 and orderType = ?2 and moneyType=?3 and companyUuid = ?4 and status is null and delFlag = '0'")
	public List<Refund> findByRecordIdAndOrderType(Long recordId, Integer orderType, Integer moneyType, String companyUuid);
	
	/**
	 * 根据订单类型、批发商uuid和相应的aircketOrderMoneyAmount的id获取已支付信息
		 * @Title: findByRecordIdAndOrderType
	     * @return List<Refund>
	     * @author majiancheng       
	     * @date 2015-11-2 下午4:08:00
	 */
	@Query("from Refund where recordId = ?1 and orderType = ?2 and companyUuid = ?3 and status is null and delFlag = '0'")
	public List<Refund> findByRecordIdAndOrderType(Long recordId, Integer orderType, String companyUuid);
	
	@Query("from Refund where recordId = ?1 and moneyType = ?2 and delFlag = '0' order by createDate desc")
	public List<Refund> findLastPayByRecordId(Long recordId,Integer moneyType);
	
	@Query("from Refund where recordId = ?1 and moneyType = ?2 and status is null and delFlag = '0' order by createDate asc")
	public List<Refund> getRefundsByRecordId(Long recordId,Integer moneyType);
	
	/**
	 * 根据付款批次号获取付款信息记录
	 * @Description: 
	 * @param @param batchNumber
	 * @param @return   
	 * @return List<Refund>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-22
	 */
	@Query("from Refund where batchNumber = ?1 and status is null and delFlag = '0'")
	public List<Refund> getByBatchNumber(String batchNumber);
}

interface RefundCustom extends BaseDao<Refund> {
	public List<Map<String,String>> findByRecordIDAndOrderTypeAndMoneyType(String recordId,String orderType,String moneyType);
	
	/**
	 * 根据付款信息获取付款金额集合(仅仅支持美途国际的付款信息)->指的是全部已付金额
	 * @Description: 
	 * @param @param recordId
	 * @param @param moneyType
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	public List<Map<String, Object>> getRefundsByRecordIdAndMoneyType(Long recordId,Integer moneyType);
	
	/**
	 * 根据付款批次号获取付款金额集合(仅仅支持美途国际的付款信息)
	 * @Description: 
	 * @param @param batchNumber
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-27
	 */
	public List<Map<String, Object>> getMoneyAmountsByBatchNumber(String batchNumber);
	
	/**
	 * 根据付款批次号获取全部付款金额集合包括撤销的付款记录(仅仅支持美途国际的付款信息)
	 * @Description: 
	 * @param @param batchNumber
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-16
	 */
	public List<Map<String, Object>> getAllMoneyAmountsByBatchNumber(String batchNumber);
	
	/**
	 * 根据付款信息获取付款金额集合(仅仅支持美途国际的付款信息)->指的是本次已付金额
	 * @Description: 
	 * @param @param recordId
	 * @param @param moneyType
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	public List<Map<String, Object>> getRefundsByRecordIdAndMoneyType(Long recordId,Integer moneyType, String batchNumber);

	/**
	 * 获取对应付款类型和对应的记录id来查询refund，获取所有的付款金额的总和，返回不同币种总额相加的字符串。如$100+￥200.
	 * @param recordId
	 * @param moneyType
     * @return
	 * @author yudong.xu 2016.9.1
     */
	public String getMultiCurrencyRefundSum(Integer recordId, Integer moneyType);

}

@Repository
class RefundDaoImpl extends BaseDaoImpl<Refund> implements RefundCustom {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,String>> findByRecordIDAndOrderTypeAndMoneyType(String recordId,
			String orderType, String moneyType) {
		String sb = "select distinct payee from refund r where r.record_id in (?) and r.orderType=? and r.moneyType=?  and status is null and r.del_flag = '0'";
		List<Map<String,String>> list = createSqlQuery(sb, recordId,orderType,moneyType).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	/**
	 * 根据付款信息获取付款金额集合(仅仅支持美途国际的付款信息)->指的是全部已付金额
	 * @Description: 
	 * @param @param recordId
	 * @param @param moneyType
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRefundsByRecordIdAndMoneyType(Long recordId,Integer moneyType) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT currency.currency_mark AS currencyMark,moneyAmount.currency_id AS currencyId,moneyAmount.exchangerate as exchangerate,SUM(moneyAmount.amount) AS amount FROM refund refund ");
		sb.append(" LEFT JOIN airticket_order_moneyAmount moneyAmount ON moneyAmount.serialNum = refund.money_serial_num ");
		sb.append(" LEFT JOIN currency currency ON moneyAmount.currency_id = currency.currency_id ");
		sb.append(" WHERE refund.status is null AND refund.record_id=? AND refund.moneyType=? AND refund.orderType=? AND refund.del_flag=? ");
		if(StringUtils.isNotEmpty(UserUtils.getUser().getCompany().getUuid())) {
			sb.append(" AND refund.companyUuid='"+UserUtils.getUser().getCompany().getUuid()+"' ");
		}
		sb.append(" GROUP BY moneyAmount.currency_id");
		List<Map<String,Object>> list = createSqlQuery(sb.toString(), recordId, moneyType, Context.ORDER_TYPE_JP, BaseEntity.DEL_FLAG_NORMAL).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	/**
	 * 根据付款批次号获取付款金额集合(仅仅支持美途国际的付款信息)
	 * @Description: 
	 * @param @param batchNumber
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-27
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMoneyAmountsByBatchNumber(String batchNumber) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT currency.currency_mark AS currencyMark,moneyAmount.currency_id AS currencyId,moneyAmount.exchangerate as exchangerate, ");
		sb.append(" SUM(moneyAmount.amount) AS amount FROM refund refund ");
		sb.append(" LEFT JOIN airticket_order_moneyAmount moneyAmount ON moneyAmount.serialNum = refund.money_serial_num ");
		sb.append(" LEFT JOIN currency currency ON moneyAmount.currency_id = currency.currency_id ");
		sb.append(" WHERE refund.status IS null AND refund.batch_number=? AND refund.orderType=? AND refund.del_flag=? ");
		if(StringUtils.isNotEmpty(UserUtils.getUser().getCompany().getUuid())) {
			sb.append(" AND refund.companyUuid='"+UserUtils.getUser().getCompany().getUuid()+"' ");
		}
		sb.append(" GROUP BY moneyAmount.currency_id");
		List<Map<String,Object>> list = createSqlQuery(sb.toString(), batchNumber, Context.ORDER_TYPE_JP, BaseEntity.DEL_FLAG_NORMAL).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	/**
	 * 根据付款批次号获取全部付款金额集合包括撤销的付款记录(仅仅支持美途国际的付款信息)
	 * @Description: 
	 * @param @param batchNumber
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-16
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAllMoneyAmountsByBatchNumber(String batchNumber) {
		StringBuffer sb = new StringBuffer();
		String currentCompanyUUID = UserUtils.getUser().getCompany().getUuid();
		sb.append(" SELECT currency.currency_mark AS currencyMark,moneyAmount.currency_id AS currencyId, ")
		  .append(" moneyAmount.exchangerate as exchangerate,SUM(moneyAmount.amount) AS amount FROM refund refund ")
		  .append(" LEFT JOIN airticket_order_moneyAmount moneyAmount ON moneyAmount.serialNum = refund.money_serial_num ")
		  .append(" LEFT JOIN currency currency ON moneyAmount.currency_id = currency.currency_id ")
		  .append(" WHERE refund.batch_number=? AND refund.orderType=? AND refund.del_flag=? ");
		if(StringUtils.isNotEmpty(currentCompanyUUID)) {
			sb.append(" AND refund.companyUuid='").append(currentCompanyUUID).append("' ");
		}
		sb.append(" GROUP BY moneyAmount.currency_id, moneyAmount.exchangerate ");
		List<Map<String,Object>> list = createSqlQuery(sb.toString(), batchNumber, Context.ORDER_TYPE_JP, BaseEntity.DEL_FLAG_NORMAL).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	/**
	 * 根据付款信息获取付款金额集合(仅仅支持美途国际的付款信息)->指的是本次已付金额
	 * @Description: 
	 * @param @param recordId
	 * @param @param moneyType
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRefundsByRecordIdAndMoneyType(Long recordId,Integer moneyType, String batchNumber) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT currency.currency_mark AS currencyMark,moneyAmount.currency_id AS currencyId,moneyAmount.exchangerate as exchangerate, ");
		sb.append(" SUM(moneyAmount.amount) AS amount FROM refund refund ");
		sb.append(" LEFT JOIN airticket_order_moneyAmount moneyAmount ON moneyAmount.serialNum = refund.money_serial_num ");
		sb.append(" LEFT JOIN currency currency ON moneyAmount.currency_id = currency.currency_id ");
		sb.append(" WHERE refund.status is null AND refund.batch_number=? AND refund.record_id=? AND refund.moneyType=? AND refund.orderType=? AND refund.del_flag=? ");
		if(StringUtils.isNotEmpty(UserUtils.getUser().getCompany().getUuid())) {
			sb.append(" AND refund.companyUuid='"+UserUtils.getUser().getCompany().getUuid()+"' ");
		}
		sb.append(" GROUP BY moneyAmount.currency_id");
		List<Map<String,Object>> list = createSqlQuery(sb.toString(), batchNumber, recordId, moneyType, Context.ORDER_TYPE_JP, BaseEntity.DEL_FLAG_NORMAL).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	/**
	 * 获取对应付款类型和对应的记录id来查询refund，获取所有的付款金额的总和，返回不同币种总额相加的字符串。如$100+￥200.
	 * @param recordId
	 * @param moneyType
	 * @return
	 * @author yudong.xu 2016.9.1
	 */
	public String getMultiCurrencyRefundSum(Integer recordId,Integer moneyType){
		if (recordId == null || moneyType == null){
			return "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT GROUP_CONCAT(t.money SEPARATOR '+') AS money FROM (")
		.append("SELECT CONCAT(c.currency_mark,FORMAT(SUM(m.amount),2)) AS money,c.currency_id FROM refund r,")
		.append("money_amount m,currency c WHERE r.money_serial_num=m.serialNum AND m.currencyId=c.currency_id ")
		.append(" AND r.record_id=? AND r.moneyType=? AND r.`status` IS NULL GROUP BY c.currency_mark ")
		.append(" ORDER BY c.currency_id) t WHERE 1=1");
		List<String> list = findBySql(sql.toString(),recordId,moneyType);
		if (list.size() == 0 || list.get(0) == null){
			return "";
		}
		return list.get(0).toString();
	}
	
	
	
}
