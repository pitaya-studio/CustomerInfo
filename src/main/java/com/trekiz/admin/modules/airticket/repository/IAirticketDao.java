package com.trekiz.admin.modules.airticket.repository;

import java.util.List;
import java.util.Map;

public interface IAirticketDao {

	List<Map<String, String>> findAirline_spacelevel(Long companyId,
			String airlineCode);

	List<Map<String, String>> findAirline_spaceName(Long companyId,
			String airlineCode, String spacelevel);

	List<Map<String, String>> findAirlineByComid(Long companyId);

	int deleteAirlineInfoById(Long id);

	int deleteAirlineInfoByAirTicketId(Long id);
	int deleteDocInfosByAirTicketId(Long id);
	int deleteIntermodalStrategiesByAirTicketId(Long id);
	
	/**
	 *获取所有附件 
	 * @return 
	 */
	List<Map<String, String>> getDocsByAirTicketId(Long id);

	List<Map<String, String>> getDeptList(Long id);
	/**
	 * 根据main_order_id获取团号
	 * @param id
	 * @return
	 */
	String getActivitygroupById(Long id);
	
	public List<Map<String, Object>> getInfoByAirTicketId(Long id);
}
