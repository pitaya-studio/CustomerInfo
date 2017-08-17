package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysOfficeProductType;
/**
 * 
 * Copyright   2015  QUAUQ Technology Co. Ltd.
 *
 * @author zhenxing.yan
 * @date 2015年11月16日
 */
public interface SysOfficeProductTypeDao extends SysOfficeProductTypeDaoCustom,CrudRepository<SysOfficeProductType, String> {

	/**
	 * 根据公司id查找公司产品类型映射列表
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param companyId
	 * @param delFlag
	 * @return
	 */
	List<SysOfficeProductType> findByCompanyIdAndDelFlag(String companyId,Integer delFlag);
	
	/**
	 * 查找指定公司中存在的类型
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param companyId 公司id
	 * @param productTypes 要查找的类型列表
	 * @return
	 */
	@Query(value="select t.productType  from SysOfficeProductType t where t.companyId = ?1 and t.delFlag=0 and t.productType in ?2")
	List<Integer> findExistTypes(String companyId,List<Integer> productTypes);

	/**
	 * 根据公司id删除公司对应的产品记录
	 * @param companyId
	 */
	@Modifying
	@Query(value = "update SysOfficeProductType set delFlag=1 where companyId=?1 and delFlag=0")
	void deleteByCompanyId(String companyId);
}

interface SysOfficeProductTypeDaoCustom extends BaseDao<SysOfficeProductType>{}

@Repository
class SysOfficeProductTypeDaoImpl extends BaseDaoImpl<SysOfficeProductType> implements SysOfficeProductTypeDaoCustom {
	
}