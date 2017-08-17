package com.trekiz.admin.modules.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;

public interface AirportInfoDao extends AirportInfoDaoCustom, CrudRepository<AirportInfo, Long>{

	@Query("from AirportInfo where companyId = ?1 and area = ?2 and countryId = ?3 and airportName = ?4 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<AirportInfo> checkSameName(long companyId, int areaId, Long countryId, String airportName);
	
	@Query("from AirportInfo where companyId = ?1 and airportCode = ?2 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<AirportInfo> checkSameAirportCode(long companyId, String airportCode);
	
	@Query("from AirportInfo where companyId = ?1 and airportCode = ?2 and id <> ?3 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<AirportInfo> checkSameAirportCode(long companyId, String airportCode, Long airportId);
	
	
	@Query("from AirportInfo where companyId = ?1 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<AirportInfo> queryAirport(long companyId);
	
	@Modifying
	@Query("update AirportInfo set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public void deleteById(Long id);
}

interface AirportInfoDaoCustom extends BaseDao<AirportInfo>{}

@Repository
class AirportInfoDaoImpl extends BaseDaoImpl<AirportInfo> implements AirportInfoDaoCustom {
	
}