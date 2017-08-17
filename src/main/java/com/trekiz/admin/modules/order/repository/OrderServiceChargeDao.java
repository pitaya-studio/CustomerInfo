package com.trekiz.admin.modules.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.OrderServiceCharge;

/**
 * 订单服务费DAO
 * @author yang.wang
 * @date 2016.9.1
 * */
public interface OrderServiceChargeDao extends OrderServiceChargeDaoCustom, CrudRepository<OrderServiceCharge, Long> {

	@Query("from OrderServiceCharge where orderId = ?1 and type = ?2 and delFlag = 0")
	public List<OrderServiceCharge> findByOrderIdAndType(Long orderId, Integer type);
	
	@Query("from OrderServiceCharge where orderId = ?1 and type = ?2 and delFlag = 0")
	public List<OrderServiceCharge> findAllByOrderIdAndType(Long orderId, Integer type);
	
	@Modifying
	@Query("update OrderServiceCharge set payStatus = 0, payTime = null where orderId = ?1 and type = ?2 and delFlag = 0")
	public void updatePayStatusAndPayTime(Long orderId, Integer type);
}

interface OrderServiceChargeDaoCustom extends BaseDao<OrderServiceCharge> {
	
}

@Repository
class OrderServiceChargeDaoImpl extends BaseDaoImpl<OrderServiceCharge> implements OrderServiceChargeDaoCustom {
	
}