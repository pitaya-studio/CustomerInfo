package com.trekiz.admin.modules.wholesalerbase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.wholesalerbase.entity.WholeArea;

/**
 * 批发商覆盖地区
 * @author gao
 *  2015年4月22日
 */
public interface WholeAreaDao extends WholeAreaDaoCustom,CrudRepository<WholeArea, Long>{
	@Query("from WholeArea w where w.companyID = ?1")
	public List<WholeArea> findWholeAreaList(Long companyID);
	@Query("from WholeArea w where w.areaid=?1 and w.companyID = ?2 ")
	public WholeArea findWholeAreaOne(Long areaid,Long companyID);
	
}
interface WholeAreaDaoCustom extends BaseDao<WholeArea>{
	
}

@Repository
class WholeAreaDaoImpl extends BaseDaoImpl<WholeArea> implements WholeAreaDaoCustom {
	
}