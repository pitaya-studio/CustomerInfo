/**
 *
 */
package com.trekiz.admin.modules.visa.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNoticeAddress;

public interface VisaInterviewNoticeAddressDao extends VisaInterviewNoticeAddressDaoCustom, CrudRepository<VisaInterviewNoticeAddress, Long> {
	
	@Query("from VisaInterviewNoticeAddress where countryId=?1 AND area=?2 AND companyId=?3")
	public List<VisaInterviewNoticeAddress> findByCountryIdAndArea(Long countryId,String area,Long companyId);
	
	@Modifying
	@Query("delete from VisaInterviewNoticeAddress where countryId=?1 AND area=?2 AND companyId=?3")
	public int deleteByCountryIdAndArea(Long countryId,String area,Long companyId);
}


interface VisaInterviewNoticeAddressDaoCustom extends BaseDao<VisaInterviewNoticeAddress> {
	
	/**
	 * 签证国家领区List
	 */
	List<Map<Object, Object>> list(Long companyId);
	
	/**
	 * 批量删除签证国家及领区
	 */
	boolean batchDelete(String listIds);
}

@Repository
class VisaInterviewNoticeAddressDaoImpl extends BaseDaoImpl<VisaInterviewNoticeAddress> implements VisaInterviewNoticeAddressDaoCustom {
	
	/**
	 * 签证国家领区List
	 */
	public List<Map<Object, Object>> list(Long companyId) {
		String sql = "SELECT "
				+ " vina.id id, "
				+ " vina.country_id countryId, "
				+ " va.ct ct, "
				+ " vina.area area, "
				+ " vina.remark remark, "
				+ " vina.company_id companyId "
				+ " FROM visa_interview_notice_address vina"
				+ " LEFT JOIN (SELECT vi.country_id, COUNT(*) ct FROM visa_interview_notice_address vi where vi.company_id="+companyId+" GROUP BY vi.country_id) va "
				+ " ON vina.country_id = va.country_id"
				+ " WHERE company_id = "+companyId
				+ " ORDER BY vina.country_id,vina.area ";
		List<Map<Object, Object>> list = findBySql(sql, Map.class);
		return list;
	}
	
	/**
	 * 批量删除签证国家及领区
	 */
	public boolean batchDelete(String listIds) {
		String sql = "DELETE FROM visa_interview_notice_address WHERE id in ("+listIds+")";
		int count = updateBySql(sql);
		return count == 0?false:true;
	}
}
