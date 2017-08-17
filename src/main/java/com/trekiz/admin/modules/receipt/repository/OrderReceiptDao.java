package com.trekiz.admin.modules.receipt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.receipt.entity.OrderReceipt;

@Repository
public interface OrderReceiptDao extends OrderReceiptDaoCustom,CrudRepository<OrderReceipt, Long> {

	/**
	 * 查询发票信息表中申请的开发票信息
	 *     
	 * 创建人：chy   
	 * 创建时间：2015-7-6 15:36:45  
	 * @version    
	 *
	 */
	@Query("from OrderReceipt where orderId = ?1 and verifyStatus = ?2")
	public List<OrderReceipt> findApplyOrderReceipt(Integer orderid,Integer audited);
	
	@Query("from OrderReceipt where invoiceNum = ?1 and invoiceCompany  = ?2")
	public List<OrderReceipt> findByInvoiceNumAndInvoiceCompany(String inoviceNum,Long invoiceCompany);
	
	@Query("from OrderReceipt where uuid = ?1 and invoiceCompany  = ?2")
	public List<OrderReceipt> findByUuidAndInvoiceCompany(String uuid,Long invoiceCompany);
	//根据订单ID及产品类型查询未驳回收据记录
	@Query("from OrderReceipt where orderId = ?1 and orderType = ?2 and verifyStatus!=2")
	public List<OrderReceipt> findOrderReceiptByOrderId(Integer orderId,Integer orderType);
	
	/**
	 * 根据订单id 和订单类型查询单个订单所有收据
	 * @author yang.jiang 2016-2-25 20:53:11
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	@Query("from OrderReceipt receipt where orderId = ?1 and orderType=?2 and createStatus = 1")
	public List<OrderReceipt> findCreatedReceiptByOrder(Integer orderId,Integer orderType);
}

/**
 * 自定义接口
 * @author liangjingming
 *
 */
interface OrderReceiptDaoCustom extends BaseDao<OrderReceipt>{
	
}

/**
 * 自定义接口实现
 * @author liangjingming
 *
 */
@Repository
class OrderReceiptDaoImpl extends BaseDaoImpl<OrderReceipt> implements OrderReceiptDaoCustom{
	
}