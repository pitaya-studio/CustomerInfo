package com.trekiz.admin.modules.sys.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;


public interface AirlineInfoDao extends AirlineInfoDaoCustom, CrudRepository<AirlineInfo, Long>{


	@Query("from AirlineInfo where companyId = ?1 and delFlag = '0' and userMode = '1'")
	public List<AirlineInfo> findAirlineInfoList(Long companyId);
	@Query("from AirlineInfo where companyId = ?1 and airlineCode=?2 and spaceLevel=?3 and delFlag = '0' and userMode = '1'")
	public List<AirlineInfo> findAirlineInfo_spaceList(Long companyId,String airlineCode,String spaceLevel);
	@Query("from AirlineInfo where companyId = ?1 and airlineCode=?2 and delFlag = '0' and userMode = '1'")
	public List<AirlineInfo> findAirlineInfo_spaceLevelList(Long companyId,String airlineCode);
	
	@Query("from AirlineInfo where companyId = ?1 and flightnumber=?2 and delFlag = '0' and userMode = '1'")
	public List<AirlineInfo> findSpaceLevelByFlightNumber(Long companyId,String flightnumber);
	
	@Query("from AirlineInfo where companyId = ?1 and id=?2 and delFlag = '0' and userMode = '1'")
	public List<AirlineInfo> findFlightNumberById(Long companyId,Long id);
	
	@Query("select DISTINCT airlineCode,airlineName from AirlineInfo where companyId = ?1 and delFlag = '0' and userMode = '1'")
	public List<Map> getAirlineList(Long companyId);
	
	@Query("from AirlineInfo where companyId = ?1 and area = ?2 and countryId = ?3 and airlineName = ?4 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<AirlineInfo> checkSameName(long companyId, int areaId, Long countryId, String airportName);
	
	@Query("from AirlineInfo where companyId = ?1 and area = ?2 and countryId = ?3 and airlineCode = ?4 and airlineName <> ?5 and delFlag ='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<AirlineInfo> checkSameAirlineCode(long companyId, int areaId, Long countryId, String airlineCode,String airlineName);
	
	@Modifying
	@Query("update AirlineInfo set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public void deleteById(Long id);
	
	@Query("select DISTINCT airlineName from AirlineInfo where companyId = ?1 and airlineCode = ?2")
	public List<String> getAirlineNameByAirlineCode(Long companyId,String airlineCode);
	
	/**
	 * 根据供货商id,航空公司简码返回唯一的航班号列表add by hhx
	 * @param companyId
	 * @param airlineCode
	 * @return
	 */
	@Query("select distinct airlineCode,airlineName,flightnumber,departuretime,arrivaltime,dayNum from AirlineInfo where  companyId = ?1 and airlineCode = ?2 and delFlag = '0' and userMode = '1' ")
	public List<Object> getDistinctFlightNum(Long companyId,String airlineCode);
	
	/**
	 * 根据供货商id,航空公司简码,航班号返回唯一的舱位等级列表add by hhx
	 * @param companyId
	 * @param airlineCode
	 * @param flight_number
	 * @return
	 */
	@Query("select distinct airlineCode,airlineName,flightnumber,spaceLevel from AirlineInfo where  companyId = ?1 and airlineCode = ?2 and flightnumber = ?3 and delFlag = '0' and userMode = '1' ")
	public List<Object> getDistinctSpaceLevel(Long companyId,String airlineCode,String flight_number);
	
	@Query("from AirlineInfo where companyId = ?1 and airlineCode = ?2 and flightnumber = ?3 and delFlag = '0' and userMode = '1' ")
	public List<AirlineInfo> findByCompanyIdAndAirlineCodeAndFiightNumber(Long companyId, String airlineCode, String flight_number);
	/**
	 * 根据供货商id，航空公司二字码，位置判断二字码的唯一性
	 * @param id
	 * @param area
	 * @param airlineCode
	 * @return
	 */
	@Query("from AirlineInfo where companyId= ?1 and area= ?2 and airlineCode= ?3 and delFlag='"+Currency.DEL_FLAG_NORMAL+"'")
	public List<AirlineInfo> getByAirlineCode(Long id, int area,
			String airlineCode);
}

interface AirlineInfoDaoCustom extends BaseDao<AirlineInfo>{}

@Repository
class AirlineInfoDaoImpl extends BaseDaoImpl<AirlineInfo> implements AirlineInfoDaoCustom {
}