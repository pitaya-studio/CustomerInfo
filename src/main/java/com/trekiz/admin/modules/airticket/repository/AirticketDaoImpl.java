package com.trekiz.admin.modules.airticket.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Repository
public class AirticketDaoImpl extends BaseDaoImpl<Map<String,Object>> implements IAirticketDao {

	@Override
	public List<Map<String, String>> findAirline_spacelevel(Long companyId,String airlineCode) {

		String sql = "SELECT DISTINCT airline_code,airline_name,space_level levelCode,dsg.label levelName from sys_airline_info a"
					+" LEFT JOIN sys_dict dsg on a.space_level=dsg.value AND dsg.type='spaceGrade_Type'"
					+" where company_id=? and airline_code=?  order by space_level";
		
		List<Map<String,String>> list = findBySql(sql,Map.class, companyId,airlineCode);
		return list;
	}
	
	@Override
	public List<Map<String, String>> findAirline_spaceName(Long companyId,String airlineCode,String spacelevel) {

		String sql = "SELECT DISTINCT airline_code,airline_name,space spaceCode,dsg.label spaceName from sys_airline_info a"
					+" LEFT JOIN sys_dict dsg on a.space=dsg.value AND dsg.type='airspace_Type'"
					+" where company_id=? and airline_code=? and space_level=?";
		
		List<Map<String,String>> list = findBySql(sql,Map.class, companyId,airlineCode,spacelevel);
		return list;
	}
	
	@Override
	public List<Map<String, String>> findAirlineByComid(Long companyId) {

		String sql = "SELECT DISTINCT airline_code,airline_name from sys_airline_info a"
					+" where company_id=? and del_flag=0";
		
		List<Map<String,String>> list = findBySql(sql,Map.class, companyId);
		return list;
	}

	@Override
	public int deleteAirlineInfoById(Long id) {
		String sql = "DELETE from activity_flight_info where id=?";
		return this.updateBySql(sql,id);
	}
	
	@Override
	public int deleteAirlineInfoByAirTicketId(Long id) {
		String sql = "DELETE from activity_flight_info where airticketId=?";
		return this.updateBySql(sql,id);
	}

	@Override
	public int deleteDocInfosByAirTicketId(Long id) {
		String sql = "DELETE f from airTicketFile f where f.airticketId=?";
		return this.updateBySql(sql,id);
	}

	@Override
	public int deleteIntermodalStrategiesByAirTicketId(Long id) {
		String sql = "DELETE from intermodal_strategy where activity_id=?";
		return this.updateBySql(sql,id);
	}

	@Override
	public List<Map<String, String>> getDocsByAirTicketId(Long id) {
		String sql = "SELECT d.* from airTicketFile f,docinfo d where f.airticketId=? and f.docId=d.id";
		return findBySql(sql,Map.class, id);
	}
	
	@Override
	public List<Map<String, Object>> getInfoByAirTicketId(Long id) {
		String sql = "SELECT d.id as fid,f.createBy as createBy,d.docPath as docPath,d.docName as docName from airTicketFile f,docinfo d where f.airticketId=? and f.docId=d.id";
		return findBySql(sql,Map.class, id);
	}

	@Override
	public List<Map<String, String>> getDeptList(Long id) {

		String sql = "select d.id,d.name from department d where d.id in ("
					+" select sr.deptId from sys_role sr where sr.id in ("
					+" select sur.roleId from sys_user_role sur where sur.userId=?))";

		List<Map<String,String>> list = findBySql(sql,Map.class, id);
		return list;
	}
	
	/**
	 * 根据main_order_id获取团号
	 * @param id
	 * @return
	 */
	@Override
	public String getActivitygroupById(Long id) {
		String sql = "select groupCode from activitygroup where id in (select productGroupId from productorder where id = '"+id+"')";
	    List<String> gcode =findBySql(sql);
	    if(gcode != null && gcode.size()>0){
	    	return gcode.get(0);
	    }
	    return "";
	}
	
}
