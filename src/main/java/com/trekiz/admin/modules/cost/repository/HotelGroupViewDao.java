package com.trekiz.admin.modules.cost.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.cost.entity.HotelGroupView;

public interface HotelGroupViewDao extends HotelGroupViewDaoCustom,
		CrudRepository<HotelGroupView, Long> {

	//@Modifying
	//@Query("update HotelGroupView set productStatus = ?2 where id in ?1")
	//public void batchOnOrOffHotelGroupView(List<Long> ids, Integer product_status);
	}

interface HotelGroupViewDaoCustom extends BaseDao<HotelGroupView> {

}

@Repository
class HotelGroupViewDaoImpl extends BaseDaoImpl<HotelGroupView> implements HotelGroupViewDaoCustom{
	
	
}
