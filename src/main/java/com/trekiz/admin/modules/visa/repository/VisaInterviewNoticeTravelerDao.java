/**
 *
 */
package com.trekiz.admin.modules.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNoticeTraveler;


public interface VisaInterviewNoticeTravelerDao extends VisaInterviewNoticeTravelerDaoCustom, CrudRepository<VisaInterviewNoticeTraveler, Long> {
	
	@Query("from VisaInterviewNoticeTraveler where interviewId=?1")
	public List<VisaInterviewNoticeTraveler> list(Long interviewId);
	
	@Modifying
	@Query("delete from VisaInterviewNoticeTraveler where interviewId=?1")
	public int deleteBySubId(Long interviewId);
	
}


interface VisaInterviewNoticeTravelerDaoCustom extends BaseDao<VisaInterviewNoticeTraveler> {

}

@Repository
class VisaInterviewNoticeTravelerDaoImpl extends BaseDaoImpl<VisaInterviewNoticeTraveler> implements VisaInterviewNoticeTravelerDaoCustom {

}
