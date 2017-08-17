package com.trekiz.admin.modules.activity.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.ProductBrowseNum;


public interface ProductBrowseNumDao extends ProductBrowseNumDaoCustom,CrudRepository<ProductBrowseNum, Long>{
    @Modifying
	@Query("update ProductBrowseNum set browseNumber = ?1 , updateTime =?2  where productId = ?3")
	public int updateBrowseNum(Long browseNum, Date newTime,Long productId);
}
interface ProductBrowseNumDaoCustom extends BaseDao<ProductBrowseNum>{
	
	public List<ProductBrowseNum> findBrowseNumByProductId(Long productId);
}
@Repository
class ProductBrowseNumDaoImpl extends BaseDaoImpl<ProductBrowseNum> implements ProductBrowseNumDaoCustom{

	@Override
	public List<ProductBrowseNum> findBrowseNumByProductId(Long productId) {
		// TODO Auto-generated method stub
		String sql = "select * from product_browse_number where productId = "+productId;
		List<ProductBrowseNum> findBySql = findBySql(sql,ProductBrowseNum.class);
		return findBySql;
	}
	
}