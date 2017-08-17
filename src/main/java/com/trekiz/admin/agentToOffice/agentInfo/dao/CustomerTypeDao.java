package com.trekiz.admin.agentToOffice.agentInfo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.agent.entity.CustomerType;

/**
 * @author wangyang
 * @date 2016-08-09
 * */
public interface CustomerTypeDao extends CustomerTypeDaoCustom, CrudRepository<CustomerType, Long> {

	
	@Modifying
	@Query("update CustomerType c set c.delFlag = '" + CustomerType.DEL_FLAG_DELETE + "' where c.id = ?1")
	public void delCustomerType(Long id);
	
	@Query("from CustomerType c where c.delFlag = '" + CustomerType.DEL_FLAG_NORMAL + "'")
	public List<CustomerType> findAllCustomerType();
		
//	@Query("select count(c) from CustomerType c where c.delFlag = '" + CustomerType.DEL_FLAG_NORMAL + "'")
//	public Integer getCustomerTypes();
	
	@Query("select count(c) from CustomerType c where c.delFlag = '" + CustomerType.DEL_FLAG_DELETE + "' and c.name = ?1")
	public Long checkRepeat(String name);
}

interface CustomerTypeDaoCustom extends BaseDao<CustomerType> {
	
}

@Repository
class CustomerTypeDaoImpl extends BaseDaoImpl<CustomerType> implements CustomerTypeDaoCustom{
	
}