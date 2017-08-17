package com.trekiz.admin.modules.agent.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;

public interface SupplyContactsDao extends SupplyContactsDaoCustom, CrudRepository<SupplyContacts, Long> {
	
	@Query(value="from SupplyContacts where supplierId = ?1 and type = '0' ")
	List<SupplyContacts> findContactsByAgentInfoId(Long id);
	@Query(value="from SupplyContacts where supplierId = ?1 and type = '0' and delFlag = '" + Context.DEL_FLAG_NORMAL + "'")
	List<SupplyContacts> findContactsByAgentInfoIdWithDelflag(Long id);
	@Query(value="from SupplyContacts where supplierId = ?1 and type = ?2 order by id asc")
	List<SupplyContacts> findSupplyContactsByIdAType(Long supplierId,String type);
}

interface SupplyContactsDaoCustom extends BaseDao<SupplyContacts> {

	/**
	 * 删除渠道联系人
	 */
	boolean deleteSupplierContacts(Long id);
}

@Repository
class SupplyContactsDaoImpl extends BaseDaoImpl<SupplyContacts> implements SupplyContactsDaoCustom{

	/**
	 * 删除渠道联系人
	 */
	public boolean deleteSupplierContacts(Long id) {
		String deleteSupplyContactsSql = "DELETE FROM supplier_contacts WHERE supplierId=? AND type='0'";
		int count = updateBySql(deleteSupplyContactsSql, id);
		return count == 0?false:true;
	}
}