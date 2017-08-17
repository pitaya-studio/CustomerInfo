package com.trekiz.admin.modules.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaProductFile;

/**
 * 签证相关文件dao
 */
public interface VisaProductFileDao extends VisaBasicsDaoCustom, CrudRepository<VisaProductFile, Long> {
	
	@Query("from VisaProductFile where srcVisaProductId = ?1 and delFlag='" + Context.DEL_FLAG_NORMAL + "'")
	public List<VisaProductFile> findFileListByProId(Long visaProductId);
	
	public List<VisaProductFile> findByDelFlag(String delFlag);
	
	public VisaProductFile findBySrcVisaProductIdAndDocInfo(Long proId, Long docId);
	
}


interface VisaProductFileDaoCustom extends BaseDao<VisaProductFile> {

}

@Repository
class VisaProductFileDaoImpl extends BaseDaoImpl<VisaProductFile> implements VisaProductFileDaoCustom{
	
}