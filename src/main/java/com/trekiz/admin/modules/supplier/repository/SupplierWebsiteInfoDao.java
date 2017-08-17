package com.trekiz.admin.modules.supplier.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.supplier.entity.SupplierWebsiteInfo;

interface SupplierWebsiteInfoDaoCustom extends BaseDao<SupplierWebsiteInfo> {
}

public interface SupplierWebsiteInfoDao extends SupplierWebsiteInfoDaoCustom, CrudRepository<SupplierWebsiteInfo, Long> {

	/**
	 * 获取网站信息编号
	 */
	@Query(value="from SupplierWebsiteInfo where supplierId = ?1")
	public SupplierWebsiteInfo getWebsiteInfoId(Long supplierId);
}

class SupplierWebsiteInfoDaoImpl extends BaseDaoImpl<SupplierWebsiteInfo> implements SupplierWebsiteInfoDaoCustom {

}

