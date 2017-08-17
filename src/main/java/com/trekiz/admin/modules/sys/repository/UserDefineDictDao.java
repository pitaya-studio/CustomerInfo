package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.UserDefineDict;

/**
 * 渠道区域DAO接口
 * @author jiachen
 * @version 2014-3-21
 */

public interface UserDefineDictDao extends UserDefineDictDaoCustom, CrudRepository<UserDefineDict, Long> {
	
	//查询对应渠道目的地
	public List<UserDefineDict> findByCompanyIdAndType(Long companyId,String type);
	
	@Modifying
	@Query("delete UserDefineDict where companyId = ?1 and type = ?2 and  dictId = ?3")
	public void deleleByDictId(Long companyId, String type, Long dictId);
	
}

/**
 * DAO自定义接口
 * @author zj
 */
interface UserDefineDictDaoCustom extends BaseDao<UserDefineDict> {
	
}

/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class UserDefineDictDaoImpl extends BaseDaoImpl<UserDefineDict> implements UserDefineDictDaoCustom {
	
}

