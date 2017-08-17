package com.trekiz.admin.modules.cost.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.cost.entity.IslandGroupView;

public interface IslandGroupViewDao extends IslandGroupViewDaoCustom,
		CrudRepository<IslandGroupView, Long> {

	//@Modifying
	//@Query("update IslandGroupView set productStatus = ?2 where id in ?1")
	//public void batchOnOrOffIslandGroupView(List<Long> ids, Integer product_status);
	}

interface IslandGroupViewDaoCustom extends BaseDao<IslandGroupView> {

}

@Repository
class IslandGroupViewDaoImpl extends BaseDaoImpl<IslandGroupView> implements IslandGroupViewDaoCustom{
	
	
}
