package com.trekiz.admin.modules.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.PreProductOrderCommon;
import com.trekiz.admin.modules.sys.entity.Area;


 /**
  * 
  * @description 预报名订单DAO
  *
  */
public interface PreProductOrderCommonDao extends PreProductOrderCommonDaoCustom, CrudRepository<PreProductOrderCommon, Long> {

	@Query("from PreProductOrderCommon where orderNum=?1 and createBy.company.uuid = ?2 and delFlag='" + Area.DEL_FLAG_NORMAL + "' ")
	public List<PreProductOrderCommon> findByOrderNum(String orderNum, String companyUuid);
	
	@Modifying
	@Query("update PreProductOrderCommon set orderType = ?1 where id = ?2")
	public void updateOrderTypeById(Integer orderType,Long id);
}


interface PreProductOrderCommonDaoCustom extends BaseDao<PreProductOrderCommon> {

}

@Repository
class PreProductOrderCommonDaoImpl extends BaseDaoImpl<PreProductOrderCommon> implements PreProductOrderCommonDaoCustom {
    
}
