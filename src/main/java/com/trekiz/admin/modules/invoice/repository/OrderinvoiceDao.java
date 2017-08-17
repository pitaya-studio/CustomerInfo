package com.trekiz.admin.modules.invoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;

public interface OrderinvoiceDao extends OrderinvoiceDaoCustom,CrudRepository<Orderinvoice, Long> {

	/**
	 * 查询发票信息表中申请的开发票信息
	 *     
	 * 创建人：liangjingming   
	 * 创建时间：2014-1-27 下午1:44:46   
	 * 修改人：liangjingming   
	 * 修改时间：2014-1-27 下午1:44:46   
	 * @version    
	 *
	 */
	@Query("from Orderinvoice invoice where orderId = ?1 and verifyStatus = ?2")
	public List<Orderinvoice> findApplyOrderinvoice(Integer orderid,Integer audited);
	
	@Query("from Orderinvoice where uuid = ?1 and invoiceCompany  = ?2")
	public List<Orderinvoice> findByInvoiceNumAndInvoiceCompany(String uuid,Long invoiceCompany);
	
	@Query("from Orderinvoice where invoiceNum = ?1 and invoiceCompany  = ?2")
	public List<Orderinvoice> findByInvoiceNumAndInvoiceCompany2(String invoiceNum,Long invoiceCompany);
	
	@Query("from Orderinvoice invoice where orderId = ?1")
	public List<Orderinvoice> findApplyOrderinvoiceInfos(Integer orderId);
	
	@Query("from Orderinvoice invoice where invoiceNum = ?1")
	public List<Orderinvoice> findOrderinvoiceInfos(String invoiceNum);
	
	@Query("from Orderinvoice invoice where orderId = ?1 and orderType=?2 and invoiceNum = ?3")
	public Orderinvoice findOrderinvoiceByOrderIdOrderTypeInvoiceNum(Integer orderId, Integer orderType, String invoiceNum);
	
	/**
	 * 根据订单id 和订单类型查询单个订单的已开发票 记录 by chy 2015年7月14日16:46:14
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	@Query("from Orderinvoice invoice where orderId = ?1 and orderType=?2 and createStatus in (1,2)")
	public List<Orderinvoice> findOrderinvoiceByOrderIdOrderType(Integer orderId,Integer orderType);
	
	/**
	 * 根据订单id 和订单类型查询单个订单的已开发票（新）
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	@Query("from Orderinvoice invoice where orderId = ?1 and orderType=?2 and createStatus = 1")
	public List<Orderinvoice> findCreatedInvoiceByOrder(Integer orderId,Integer orderType);
	
	/**
	 * 根据订单id 和订单类型查询单个订单的待开发票 记录 by chy 2016年1月5日20:12:07
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	@Query("from Orderinvoice invoice where orderId = ?1 and orderType=?2 and createStatus <> 1 ")
	public List<Orderinvoice> findOrderinvoiceByOrderIdOrderType2(Integer orderId,Integer orderType);

	//根据订单ID及产品类型查询未驳回发票记录
	@Query("from Orderinvoice where orderId = ?1 and orderType = ?2 and verifyStatus <> 2")
	public List<Orderinvoice> findOrderInvoiceByOrderId(Integer orderId,Integer orderType);
	
	//add by chy 2016年1月22日14:48:02 根据uuid和开票状态查询记录
	@Query("from Orderinvoice where uuid = ?1 and createStatus  = ?2")
	public List<Orderinvoice> findByInvoiceNumAndCreateStatus(String uuid,Integer createStatus);
	
	/**
	 * 根据订单id 和订单类型查询单个订单的待开 和 已开 发票 记录 by chy 2016年1月5日20:12:07
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	@Query("from Orderinvoice invoice where orderId = ?1 and orderType=?2 and createStatus in (0,1) ")
	public List<Orderinvoice> findOrderinvoiceByOrderIdOrderTypeStatusINOneTwo(Integer orderId,Integer orderType);

}

/**
 * 自定义接口
 * @author liangjingming
 *
 */
interface OrderinvoiceDaoCustom extends BaseDao<Orderinvoice>{
	
	/**
	 * 根据订单id查询该订单下的所有预开发票
	 * @param orderId 订单id
	 * @return
	 * */
	public List<Orderinvoice> findOrderinvoiceByOrderIdApplyWay(Integer orderId);
	
	/**
	 * 判断在订单中是否有预开发票
	 * @param orderId
	 * @return
	 */
	public Boolean findHasPreOpeninvoiceInOrder(Integer orderId);
	
}

/**
 * 自定义接口实现
 * @author liangjingming
 *
 */
@SuppressWarnings("all")
@Repository
class OrderinvoiceDaoImpl extends BaseDaoImpl<Orderinvoice> implements OrderinvoiceDaoCustom{

	@Override
	public List<Orderinvoice> findOrderinvoiceByOrderIdApplyWay(Integer orderId) {
		String sql = "SELECT invoice.id invoiceId,invoice.invoiceNum,invoice.groupCode,'已开票' createStatus, " +
				     " FORMAT(invoice.invoiceAmount,2) invoiceAmount,user.`name` applyPeason " +
				     " FROM orderinvoice invoice, sys_user user " +
				     " WHERE invoice.createBy = user.id " +
				     " AND invoice.applyInvoiceWay = 1 " +
				     " AND invoice.createStatus = 1 " +
				     " AND invoice.orderId = " + orderId;
		List<Orderinvoice> invoiceList = this.findBySql(sql, null);
		return invoiceList;
	}

	@Override
	public Boolean findHasPreOpeninvoiceInOrder(Integer orderId) {
		String sql = "SELECT 1 FROM orderinvoice where applyInvoiceWay = 1 AND orderId =" + orderId;
		List<Integer> lists = this.findBySql(sql);
		if(lists != null && lists.size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
}