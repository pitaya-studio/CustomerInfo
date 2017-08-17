package com.trekiz.admin.modules.airticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;


/**
 * DAO接口实现

 * @Description:TODO

 * @author:midas

 * @time:2014-9-19 上午10:21:44
 */

public interface FlightInfoDao extends FlightInfoDaoCustom,
		CrudRepository<FlightInfo, Long> {
	
	
	  
    @Query("from FlightInfo where  airticketId =?1")
    public List<FlightInfo> findByFlightInfoByAirTicketId(Long airticketId);


	@Modifying
	@Query("delete from FlightInfo where id = ?1")
	public void delFlightInfoById(Long id);
	
	
	

}


