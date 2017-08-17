package com.trekiz.admin.modules.supplier.repository;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.supplier.entity.*;

interface SupplierNameDaoCustom extends BaseDao<SupplierName> {
}

public interface SupplierNameDao extends SupplierNameDaoCustom, CrudRepository<SupplierName, Long> {

	/**
	 * 根据地接社类型，所属公司ID查询地接社名称
	 */
	@Query("from SupplierName where supplyType=?1 and companyId=?2")
	public  List<SupplierName> findSupplierName(Integer supplyType,Long companyId);	

}

class SupplierNameDaoImpl extends BaseDaoImpl<SupplierName> implements SupplierNameDaoCustom {

}