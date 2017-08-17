package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;

public interface SysCompanyDictViewDao extends SysCompanyDictViewDaoCustom, CrudRepository<SysCompanyDictView, Long> {
	
	//add by jiangyang
	@Query("from SysCompanyDictView where delFlag='0' and type=?1 order by sort")
//	@Query(value = "select * from sys_company_dict_view where delFlag='0' and type='travel_agency_type' order by sort" ,nativeQuery=true)
	public List<SysCompanyDictView> findAllDictViewListByType(String type);
	
	@Query("from SysCompanyDictView where type = ?1 and (companyId=-1 or companyId=?2) and delFlag='0' order by sort")
	public List<SysCompanyDictView> findAllDictViewListByType(String supplyType, Long companyId);
	
	@Query("from SysCompanyDictView where type = ?1 and (companyId=-1 or companyId=?2) and delFlag='0' and  value not in (?3) order by sort")
	public List<SysCompanyDictView> findByType(String supplyType,Long companyId,List<String> values);
}

/**
 * DAO自定义接口 
 */
interface SysCompanyDictViewDaoCustom extends BaseDao<SysCompanyDictView> {
//	public List<SysCompanyDictView> findAllDictViewListByType(String type);
}

/**
 * DAO自定义接口实现 
 */
@Repository(value="sysCompanyDictViewDaoImpl1")
class SysCompanyDictViewDaoImpl extends BaseDaoImpl<SysCompanyDictView> implements SysCompanyDictViewDaoCustom {

//	@Override
//	public List<SysCompanyDictView> findAllDictViewListByType(String type) {
//		return this.find("from SysCompanyDictView where delFlag='0' and type=? order by sort", type);
//	}
 

}