package com.trekiz.admin.modules.supplier.repository;
import org.springframework.data.repository.CrudRepository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.supplier.entity.*;

interface BankDaoCustom extends BaseDao<Bank> {
}

public interface BankDao extends BankDaoCustom, CrudRepository<Bank, Long> {

	
	//@Query("from Bank where supplyType=?1 and companyId=?2")
	//public  List<Bank> findBank(Integer supplyType,Long companyId);	

}

class BankDaoImpl extends BaseDaoImpl<Bank> implements BankDaoCustom {

}