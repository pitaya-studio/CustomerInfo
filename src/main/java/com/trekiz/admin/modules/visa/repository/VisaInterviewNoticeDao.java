/**
 *
 */
package com.trekiz.admin.modules.visa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNotice;


public interface VisaInterviewNoticeDao extends VisaInterviewNoticeDaoCustom, CrudRepository<VisaInterviewNotice, Long> {
	
	@Modifying
	@Query("update VisaInterviewNotice set delFlag='"+Dict.DEL_FLAG_DELETE+"'where id=?1")
	public int deleteById(Long id);
   
}


interface VisaInterviewNoticeDaoCustom extends BaseDao<VisaInterviewNotice> {

}

@Repository
class VisaInterviewNoticeDaoImpl extends BaseDaoImpl<VisaInterviewNotice> implements VisaInterviewNoticeDaoCustom {

}
