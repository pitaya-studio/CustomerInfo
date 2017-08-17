/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.sys.utils.DictUtils;


 /**
 *  文件名: Orderpay.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:33
 *  @version 1.0
 */
public interface OrderpayDao extends OrderpayCustom, CrudRepository<Orderpay, Long> {
    
    @Modifying
    @Query("update Orderpay set delFlag='" + Orderpay.DEL_FLAG_DELETE + "' where id = ?1")
    public int deleteById(Long id);
    
    @Modifying
    @Query("update Orderpay set delFlag='" + Orderpay.DEL_FLAG_DELETE + "' where orderId = ?1")
    public int deleteByOrderId(Long orderId);
    
    @Modifying
    @Query("update Orderpay set delFlag='" + Orderpay.DEL_FLAG_DELETE + "' where orderId = ?1")
    public int updateByOrderId(Long orderId);
    
    @Modifying
    @Query("update Orderpay set isAsAccount=1 where orderId = ?1 and payPriceType = ?2 and orderType=?3")
    public int updateIsAsAccount(Long orderId,Integer moneyType,Integer orderType);
    
    @Modifying
    @Query("update Orderpay set isAsAccount=1 where orderId = ?1 and payPriceType = ?2 and orderType=?3 and moneySerialNum=?4")
    public int updateIsAsAccount(Long orderId,Integer moneyType,Integer orderType,String moneySerialNum);
    
    @Modifying
    @Query("update Orderpay set isAsAccount=1 where moneySerialNum=?1")
    public int updateIsAsAccount(String moneySerialNum);
    
    @Query("from Orderpay where orderId = ?1 and delFlag = '" + Orderpay.DEL_FLAG_NORMAL + "'")
    public List<Orderpay> findOrderpayByOrderId(Long orderId);
    
    @Query("from Orderpay where orderId = ?1 and orderType=?2 and delFlag = '" + Orderpay.DEL_FLAG_NORMAL + "'  and orderPaySerialNum is not null group by orderPaySerialNum order by id desc")
    public List<Orderpay> findOrderpayByOrderId(Long orderId, Integer orderType);
    
    @Query("from Orderpay where orderId = ?1 and orderType=?2 and isAsAccount is not null and isAsAccount != 99 " +
    		"and delFlag = '" + Orderpay.DEL_FLAG_NORMAL + "' order by updateDate desc")
    public List<Orderpay> findLastDateOrderpay(Long orderId, Integer orderType);
    
    @Query("from Orderpay where orderId in(?1) and orderType = ?2 and delFlag = '" + Orderpay.DEL_FLAG_NORMAL + "'")
    public List<Orderpay> findOrderpayByOrderIds(List<Long> orderIds, Integer orderType);
    
    @Query("from Orderpay where orderId =?1 and orderType = ?2 and (isAsAccount != 2 or isAsAccount is null) order by id asc")
    public List<Orderpay> findOrderPayOrderById(Long orderId, Integer orderType);
    
    @Query("from Orderpay where orderId =?1 and orderType = ?2 and isAsAccount = 1 order by updateDate desc")
    public List<Orderpay> findOrderPayOrderByUpdate(Long orderId, Integer orderType);
    
    @Query("from Orderpay where orderNum =?1 and delFlag = '" + Orderpay.DEL_FLAG_NORMAL+"'")
    public List<Orderpay> findOrderpay(String orderNum);
    
    @Query("from Orderpay where id = ?1")
    public Orderpay getOrderPayById(Long id);
    
    @Modifying
    @Query("update Orderpay set printTime=?1 ,printFlag=1 where id = ?2")
    public int updateOrderPayById(Date printTime,Long id);
    
    
    @Modifying
    @Query("update Orderpay set isAsAccount=2 where id = ?1")
    public int updateOrderPayByOrderId(Long id);
    
    //----- wxw added -------
    @Query("from Orderpay where orderPaySerialNum =?1  and delFlag = '" + Orderpay.DEL_FLAG_NORMAL+"'")
    public List<Orderpay> findOrderpaByOrderPaySerialNum(String orderPaySerialNum);
    
    
    
    @Query("from Orderpay where orderPaySerialNum =?1 and orderNum =?2 and delFlag = '" + Orderpay.DEL_FLAG_NORMAL+"'")
    public List<Orderpay> findOrderpaByOrderPaySerialNumNew(String orderPaySerialNum,String orderCode);
    
    
    @Query("from Orderpay where orderNum=?1 and orderPaySerialNum=?2 and delFlag='"+Orderpay.DEL_FLAG_NORMAL+"'")
    public List<Orderpay> findOrderpay(String orderNum,String orderPaySerialNum);
    @Modifying
    @Query("update Orderpay set printTime=?1 ,printFlag=1 where orderPaySerialNum = ?2")
    public int updateOrderPayByOrderPaySerialNum(Date printTime,String orderPaySerialNum);
    
    /**
     * 根据支付表id更新达帐状态
    	 * @Title: updateIsAsAccountById
         * @return int
         * @author majiancheng       
         * @date 2015-11-4 下午6:22:57
     */
    @Modifying
    @Query("update Orderpay set isAsAccount=?1 where id = ?2")
    public int updateIsAsAccountById(Integer isAsAccount, Long id);

}


 /**
 *  文件名: Orderpay.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:25
 *  @version 1.0
 */
interface OrderpayCustom extends BaseDao<Orderpay> {
	/**
	 * 根据支付方式和支付类型获取支付类型名称
     * @Title: getPayTypeName
     * @return String
     * @author majiancheng
     * @date 2015-10-23 下午5:38:19
	 */
	public String getPayTypeName(Integer paymentStatus, Integer payType);
	
	public List<Map<String,Object>> getPayedDetail(Integer orderPayId);

    String getOrderPayTravelerNames(String orderPaySerialNum);
}

 /**
 *  文件名: Orderpay.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:15
 *  @version 1.0
 */
@Repository
class OrderpayDaoImpl extends BaseDaoImpl<Orderpay> implements OrderpayCustom {
	
	/**
	 * 根据支付方式和支付类型获取支付类型名称
		 * @Title: getPayTypeName
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-23 下午5:38:19
	 */
	public String getPayTypeName(Integer paymentStatus, Integer payType){
		// 即时支付
		if (paymentStatus == null || paymentStatus == 1) {
			return DictUtils.getDictLabel(payType.toString(), Context.ORDER_PAYTYPE, "");
		}

		// 其他支付方式
		return DictUtils.getDictLabel(paymentStatus.toString(), Context.PAY_MENT_TYPE, "");
	}

	@Override
	public List<Map<String, Object>> getPayedDetail(Integer orderPayId) {
		String sql = "SELECT op.payVoucher,op.payPriceType receiveType, op.payType, op.payerName, op.checkNumber, op.invoiceDate, " +
				"op.toBankNname, op.toBankAccount, op.bankName, op.bankAccount, op.remarks, " +
				"op.receivePeopleCount, ma.amount, IFNULL( ma.exchangerate, IFNULL(c.convert_lowest, 1)) exchangerate,ma.currencyId " +
				"FROM orderpay op, money_amount ma, currency c WHERE op.moneySerialNum = ma.serialNum AND ma.currencyId = " +
				"c.currency_id AND op.id = ?";
		
		return findBySql(sql, Map.class,orderPayId);
	}

     /**
      * 根据订单付款序列号查询所有游客姓名。目前用于签证详情和修改页面的付款信息栏显示所有的付款游客名称。
      * @param orderPaySerialNum
      * @return
      * @author yudong.xu 2016.10.11
      */
     @Override
     public String getOrderPayTravelerNames(String orderPaySerialNum) {
         String sql = "SELECT GROUP_CONCAT(t.`name`) AS travelers FROM orderpay p LEFT JOIN traveler t " +
                 " ON p.traveler_id = t.id WHERE p.orderPaySerialNum = ?";
         List<Object> result = findBySql(sql,orderPaySerialNum);
         if (result == null || result.size() == 0){
             return "";
         }
         return result.get(0).toString();
     }

 }
