/**
 *
 */
package com.trekiz.admin.modules.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.PayConvert;

public interface PayConvertDao extends PayConvertCustom, CrudRepository<PayConvert, Long> {
	@Query("from PayConvert where id = ?1 and delFlag = '0'")
	public PayConvert findInfoById(Long id);
	@Query("from PayConvert where serialNum = ?1 and delFlag = '0'")
	public List<PayConvert> findInfoBySerialNum(String serialNum);
}


interface PayConvertCustom extends BaseDao<PayConvert> {

}

@Repository
class PayConvertDaoImpl extends BaseDaoImpl<PayConvert> implements PayConvertCustom {

}
