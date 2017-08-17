package com.trekiz.admin.modules.visa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaOrderFile;

/**
 * 签证相关文件dao
 */
public interface VisaOrderFileDao extends VisaBasicsDaoCustom, CrudRepository<VisaOrderFile, Long> {
		
}


interface VisaOrderFileDaoCustom extends BaseDao<VisaOrderFile> {

}

@Repository
class VisaOrderFileDaoImpl extends BaseDaoImpl<VisaOrderFile> implements VisaOrderFileDaoCustom{
	
}