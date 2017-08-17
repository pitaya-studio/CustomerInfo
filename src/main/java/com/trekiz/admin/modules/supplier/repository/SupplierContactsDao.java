package com.trekiz.admin.modules.supplier.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;

interface SupplierContactsDaoCustom extends BaseDao<SupplierContacts> {
	/**
	 * 删除地接社联系人
	 */
	boolean deleteSupplierContacts(Long id);
}

public interface SupplierContactsDao extends SupplierContactsDaoCustom, CrudRepository<SupplierContacts, Long> {

	/**
	 * 查询地接社联系人
	 */
	@Query(value="from SupplierContacts where supplierId = ?1 and type = '1'")
	List<SupplierContacts> findContactsBySupplierInfoId(Long id);
	
	/**
	 * 查询渠道商联系人
	 */
	@Query(value="from SupplierContacts where supplierId = ?1 and type = '0'")
	List<SupplierContacts> findContactsByAgentInfoId(Long id);
	
	/**
	 * 查询渠道商联系人
	 */
	@Query(value="from SupplierContacts where supplierId = ?1 and delFlag = '0' and type = '0'")
	List<SupplierContacts> findNormalContactsByAgentInfoId(Long id);
}

class SupplierContactsDaoImpl extends BaseDaoImpl<SupplierContacts> implements SupplierContactsDaoCustom {

	/**
	 * 删除地接社联系人
	 */
	public boolean deleteSupplierContacts(Long id) {
		String deleteSupplierContactsSql = "DELETE FROM supplier_contacts WHERE supplierId=? AND type='1'";
		int count = updateBySql(deleteSupplierContactsSql, id);
		return count == 0?false:true;
	}
}