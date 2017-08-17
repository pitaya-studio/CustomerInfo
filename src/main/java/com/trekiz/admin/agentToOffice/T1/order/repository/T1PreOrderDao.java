package com.trekiz.admin.agentToOffice.T1.order.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.agentToOffice.T1.order.entity.T1PreOrder;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;


public interface T1PreOrderDao extends T1PreOrderDaoCustom,
		CrudRepository<T1PreOrder, Long> {
	
	@Query(value="SELECT orderNum FROM t1_pre_order pro, travelactivity activity WHERE pro.productId = activity.id AND pro.createDate >= CURDATE() ORDER BY pro.id DESC LIMIT 1", nativeQuery = true)
	public String findLastPreOrder();
}

interface T1PreOrderDaoCustom extends BaseDao<T1PreOrder> {
}

@Repository
class T1PreOrderDaoImpl extends BaseDaoImpl<T1PreOrder> implements T1PreOrderDaoCustom {
}
