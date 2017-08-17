package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysOfficeProcessType;
/**
 * 
 * Copyright   2015  QUAUQ Technology Co. Ltd.
 *
 * @author zhenxing.yan
 * @date 2015年11月16日
 */
public interface SysOfficeProcessTypeDao extends SysOfficeProcessTypeDaoCustom,CrudRepository<SysOfficeProcessType, String> {

	/**
	 * 根据公司id查找公司流程类型映射列表
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param companyId
	 * @param delFlag
	 * @return
	 */
	List<SysOfficeProcessType> findByCompanyIdAndDelFlag(String companyId,Integer delFlag);
	
	/**
	 * 查找指定公司中存在的类型
	 * @created_by zhenxing.yan 2015年11月16日
	 *
	 * @param companyId 公司id
	 * @param processType 要查找的类型列表
	 * @return
	 */
	@Query(value="select t.processType  from SysOfficeProcessType t where t.companyId = ?1 and t.delFlag=0 and t.processType in ?2")
	List<Integer> findExistTypes(String companyId,List<Integer> processType);

	/**
	 * 根据公司id删除流程类型
	 * @param companyId
	 */
	@Modifying
	@Query(value = "update SysOfficeProcessType set delFlag=1 where companyId=?1 and delFlag=0")
	void deleteByCompanyId(String companyId);
}

interface SysOfficeProcessTypeDaoCustom extends BaseDao<SysOfficeProcessType>{}

@Repository
class SysOfficeProcessTypeDaoImpl extends BaseDaoImpl<SysOfficeProcessType> implements SysOfficeProcessTypeDaoCustom {
	
}