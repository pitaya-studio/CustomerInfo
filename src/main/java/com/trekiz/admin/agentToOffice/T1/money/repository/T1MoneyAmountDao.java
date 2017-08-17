package com.trekiz.admin.agentToOffice.T1.money.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.agentToOffice.T1.money.entity.T1MoneyAmount;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;


public interface T1MoneyAmountDao extends T1MoneyAmountDaoCustom,
		CrudRepository<T1MoneyAmount, Long> {
	@Query("from T1MoneyAmount where id = ?1")
	public List<T1MoneyAmount> findMoneyAmount(Long id);

	@Query("from T1MoneyAmount where serialNum = ?1 order by currencyId")
	public List<T1MoneyAmount> findAmountBySerialNum(String serialNum);
	
	@Query("from T1MoneyAmount where serialNum = ?1 and currencyId = ?2")
	public List<T1MoneyAmount> findAmountBySerialNumAndCurrencyId(String serialNum, Integer currencyId);
	
	@Query("from T1MoneyAmount where serialNum = ?1")
	public List<T1MoneyAmount> findAmountListBySerialNum(String serialNum);
}

interface T1MoneyAmountDaoCustom extends BaseDao<T1MoneyAmount> {
}

@Repository
class T1MoneyAmountDaoImpl extends BaseDaoImpl<T1MoneyAmount> implements
		T1MoneyAmountDaoCustom {
}
