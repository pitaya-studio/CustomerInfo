package com.trekiz.admin.modules.sys.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysIncrease;

/**
 * 自增sequenceDAO接口
 * @author liangjingming
 * @version 2014-01-17
 */
public interface SysIncreaseDao extends SysIncreaseDaoCustom,CrudRepository<SysIncrease, Long>{

	
	@Query("from SysIncrease where codeName = ?1 and codeType = ?2")
	public SysIncrease findSysIncreaseByType(String codeName,Integer codeType);
	
	/**
	 * 获取产品编号
	 * @param proCompanyId 批发商ID
	 * @param codeType 编码类型
	 * @return
	 */
	@Query("from SysIncrease where proCompanyId = ?1 and codeType = ?2")
	public SysIncrease findNumByProidCode(Long proCompanyId,Integer codeType);
	
	@Query("from SysIncrease where proCompanyId = ?1 and codeType = ?2 and codeName = ?3")
	public SysIncrease findNumByProidCodeName(Long proCompanyId,Integer codeType,String codeName);
}

/**
 * DAO自定义接口
 * @author liangjingming
 */
interface SysIncreaseDaoCustom extends BaseDao<SysIncrease>{
	
}

/**
 * DAO自定义接口实现
 * @author liangjingming
 */
@Repository
class SysIncreaseDaoImpl extends BaseDaoImpl<SysIncrease> implements SysIncreaseDaoCustom{
	
}
