/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.OrderContacts;


 /**
 *  文件名: ContactsDao.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 下午6:32:33
 *  @version 1.0
 */
public interface OrderContactsDao extends OrderContactsDaoCustom, CrudRepository<OrderContacts, Long> {
	/**
     * 查询订单联系人
     * @param orderId
     * @return
     */
    @Query("from OrderContacts where orderId = ?1")
    public List<OrderContacts> findOrderContactsByOrderId(Long orderId);
    
    /**
     * 删除订单联系人
     * @param orderId
     */
    @Modifying
    @Query("delete OrderContacts where orderId = ?1 and orderType = ?2")
    public void deleteOrderContactsByOrderId(Long orderId,int orderType);
    
    /**
     * 查询订单根据订单ID和订单类型
     * @param orderId 订单ID
     * @param orderType 订单类型
     * @return
     */
    @Query("from OrderContacts where orderId = ?1 and orderType = ?2")
    public List<OrderContacts> findOrderContactsByOrderIdAndOrderType(Long orderId, int orderType);

    /**
     * 删除订单根据订单ID和订单类型
     * @param orderId 订单ID
     * @param orderType 订单类型
     */
    @Modifying
    @Query("delete OrderContacts where orderId = ?1 and orderType = ?2")
    public void deleteOrderContactsByOrderIdAndOrderType(Long orderId, int orderType);
    
    /**
     * 
     * @param orderId
     * @param applyOrderId
     */
    @Modifying
    @Query("update OrderContacts set orderId = ?1,orderType = 2 where orderId = ?2 and orderType = 0")
    public void updateOrderContactsByApplyOrderId(Long orderId, Long applyOrderId);
    
    /**
     * 删除订单联系人
     * @param orderId
     */
    @Modifying
    @Query("delete OrderContacts where id = ?1")
    public void deleteOrderContactsById(Long id);
    
    /**
     * 根据agentId查询 渠道联系人 (海岛游修改)
     * @param id
     * @return
     */
	@Query(value="from OrderContacts where agentId = ?1 and orderType = '12'")
	public List<OrderContacts> findContactsByAgentInfoId(Long id);
}


 /**
 *  文件名: OrderContacts.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author jianghaili
 *  @DateTime 2014-10-29 
 *  @version 1.0
 */
interface OrderContactsDaoCustom extends BaseDao<OrderContacts> {

}

 /**
 *  文件名: OrderContacts.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author jianghaili
 *  @DateTime 2014-10-29 
 *  @version 1.0
 */
@Repository
class OrderContactsDaoImpl extends BaseDaoImpl<OrderContacts> implements OrderContactsDaoCustom {

}
