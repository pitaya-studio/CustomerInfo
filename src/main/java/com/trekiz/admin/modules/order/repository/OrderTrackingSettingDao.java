package com.trekiz.admin.modules.order.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.OrderTrackingSetting;

public interface OrderTrackingSettingDao extends OrderTrackingSettingDaoCustom, CrudRepository<OrderTrackingSetting, Long> {

//	@Modifying
//    @Query("update OrderTrackingSetting set delFlag='" + OrderTrackingSetting.DEL_FLAG_DELETE + "' where id = ?1")
//	public int deleteById(Long id);
//	
//	@Query("from OrderTrackingSetting where orderNum=?1 and createBy.company.uuid = ?2 and delFlag='" + Area.DEL_FLAG_NORMAL + "' ")
//	public List<OrderTrackingSetting> findByOrderNum(String orderNum, String companyUuid);
}


interface OrderTrackingSettingDaoCustom extends BaseDao<OrderTrackingSetting> {

}

@Repository
class OrderTrackingSettingDaoImpl extends BaseDaoImpl<OrderTrackingSetting> implements OrderTrackingSettingDaoCustom {}
