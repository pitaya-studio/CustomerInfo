package com.trekiz.admin.modules.eprice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimateMoneyView;

public interface EstimateMoneyViewDao extends EstimateMoneyViewDaoCustom,CrudRepository<EstimateMoneyView,Long>{

	@Query("from EstimateMoneyView  where estimatePriceReplyId = ?1 and moneyType = '30' and  businessType='3'")
	public List<EstimateMoneyView> findByReplyId(Long id);
	
	@Query("from EstimateMoneyView  where estimatePriceReplyId = ?1 and moneyType = '30' and  businessType='4'")
	public List<EstimateMoneyView> findByTopReplyId(Long id);
	
}
interface EstimateMoneyViewDaoCustom extends BaseDao<EstimateMoneyView>{
	
}
@Repository
class EstimateMoneyViewDaoImpl extends BaseDaoImpl<EstimateMoneyView> implements EstimateMoneyViewDaoCustom{
	
}