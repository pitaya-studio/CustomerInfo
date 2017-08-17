package com.trekiz.admin.modules.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;


/**
 * 
 *  文件名: OrderContactsService.java
 *  功能: 订单特殊需求
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-12-02 下午7:39:31
 *  @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class OrderContactsService extends BaseService{
	
	@Autowired
	private OrderContactsDao orderContactsDao;
	
	/**
	 * 根据订单ID和订单类型查询所有联系人
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public List<OrderContacts> findOrderContactsByOrderIdAndOrderType(Long orderId, int orderType){
		return orderContactsDao.findOrderContactsByOrderIdAndOrderType(orderId, orderType);
	}
	
	/**
	 * 根据ID删除所有联系人
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public void deleteOrderContactsById(Long id){
		orderContactsDao.deleteOrderContactsById(id);
	}
	
	/**
	 * 根据订单ID和类型删除所有联系人
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public void deleteOrderContactsByIdAndType(Long orderId, int orderType){
		orderContactsDao.deleteOrderContactsByOrderIdAndOrderType(orderId, orderType);
	}
	
	/**
	 * 根据agentId查询 渠道联系人 (海岛游修改)
	 * @param ai
	 * @return
	 */
	public List<OrderContacts> findContactsByAgentInfo(Long id) {
		List<OrderContacts> list = new ArrayList<OrderContacts>();
		try {
			list = orderContactsDao.findContactsByAgentInfoId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据 id 查找唯一匹配的记录 
	 * @param id
	 * @return
	 */
	public OrderContacts findOne(Long id){
		OrderContacts sup = new OrderContacts();
		try {
			sup = orderContactsDao.findOne(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sup;
	}
	
    /**
     * 批量保存
     * @param entitys
     */
    public void batchSave(List<OrderContacts> entitys){
    	orderContactsDao.batchSave(entitys);
    }
}
