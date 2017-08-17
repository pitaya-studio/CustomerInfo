package com.trekiz.admin.modules.grouphandle.dao;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandleVisa;

public interface GroupHandleVisaDao extends GroupHandleVisaDaoCustom, CrudRepository<GroupHandleVisa, Long> {
    @Query("from GroupHandleVisa where groupHandleId = ?1")
    List<GroupHandleVisa> findByGroupHandleId(Integer id);

    @Query("from GroupHandleVisa where groupHandleId = ?1 and travelerId = ?2")
    List<GroupHandleVisa> findByGroupHandleIdAndTravelerId(Integer id, Integer travelerId);
    
    @Query("from GroupHandleVisa where groupHandleId = ?1 and travelerId = ?2 and delFlag = ?3")
    List<GroupHandleVisa> findByGroupHandleIdAndTravelerId(Integer id, Integer travelerId, String delFlag);

    @Query("from GroupHandleVisa where orderId = ?1 and travelerId =?2 and delFlag=0")
    List<GroupHandleVisa> findByOrderIdAndTravelerId(Integer orderId,Integer travelerId);
    
    @Query("delete GroupHandleVisa where orderId = ?1 and travelerId =?2 and delFlag=4")
    void deleteTobeAddByOrderAndTraveler(Integer orderId,Integer travelerId);
    
    @Modifying
    @Query("update Traveler set passportStatus = ?1 where id = ?2")
    public void updatePassportStatusByTravelerId(Integer passportStatus, Long travelerId);
    
    @Modifying
    @Query("delete GroupHandleVisa where travelerId = ?1")
    void deleteByTraveler(Integer travelerId);
    
    @Query("from GroupHandleVisa where id = ?1")
    GroupHandleVisa findById(Integer id);

	
}

interface GroupHandleVisaDaoCustom extends BaseDao<GroupHandleVisa> {
	/**签务团控批量操作签证状态
	 * @param visaIds
	 * @param visaStatus
	 * @return
	 */
	boolean batchUpdateVisaStatus(String visaIds, String visaStatus);
	/**
	 * 签务团控批量操作护照状态
	 * @param passportStatus
	 * @param travellerIds
	 * @return
	 */
	boolean batchUpdatePassportStatus(String passportStatus ,String travellerIds);
	/**
	 * 签务团控批量操作设置时间
	 * @param groupHandleVisa
	 * @param visaIds
	 * @return
	 */
	boolean batchUpdateTime(HttpServletRequest request,String visaIds) throws Exception;
	/***
	 * 签务团控保存
	 * @param request
	 * @return
	 */
	boolean saveGroup(String sql ,List<Object> list);
	/**
	 * 保存
	 * @param passportStatus
	 * @param travellerIds
	 * @return
	 */
	boolean savePassportStatus(Integer passportStatus,String travellerIds);
	
	List<Map<String, Object>> findListByOrderIdAndTravelerId(Integer passportStatus,Integer travellerIds);
}

@Repository
class GroupHandleVisaDaoImpl extends BaseDaoImpl<GroupHandleVisa> implements GroupHandleVisaDaoCustom{
	/**
	 * 批量更新签证状态
	 */
	public boolean batchUpdateVisaStatus(String visaIds, String status) {
		String batchUpdateVisaStatusSql = "update group_control_visa set visa_stauts="+status+" where id in ("+visaIds+")";
		int count = updateBySql(batchUpdateVisaStatusSql);
		return count==1?true:false;
	}
	
	/**
	 * 批量更新护照状态
	 */
	public boolean batchUpdatePassportStatus(String passportStatus, String travelerIds) {
		travelerIds = travelerIds.substring(0, travelerIds.length()-1);
		String Sql = "update traveler set passportStatus="+Integer.parseInt(passportStatus)+" where id in ("+travelerIds+")";
		int count = updateBySql(Sql);
		return count==1?true:false;
	}
	
	/**签务团控
	 * 保存
	 * @param passportStatus
	 * @param travelerId
	 */
	public boolean savePassportStatus(Integer passportStatus,String travelerId){
		String Sql = "update traveler set passportStatus="+passportStatus+" where id in ("+travelerId+")";
		int count = updateBySql(Sql);
		return count==1?true:false;
	}
	
	/**
	 * 批量更新时间
	 * @throws Exception 
	 */
	public boolean batchUpdateTime(HttpServletRequest request,
			String visaIds) throws Exception {
		// TODO Auto-generated method stub
		String signingTime=request.getParameter("signingTime");
		String visaDeliveryTime = request.getParameter("visaDeliveryTime");
		String visaGotTime=request.getParameter("visaGotTime");
		String supplementaryInfoTime=request.getParameter("supplementaryInfoTime");

		String batchUpdateTimeSql="update group_control_visa set signing_time = ? ,visa_delivery_time =? , visa_got_time =? , supplementaryinfo_time=?  where id in ("+visaIds+")";
		
		SimpleDateFormat df ;
		List<Object> par = new ArrayList<Object>();
		
		//实际约签时间
		if(null == signingTime || "".equals(signingTime)){
    		par.add(null);
    	}else{
    		if(signingTime.contains(":"))
    		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
    		else
    			df = new SimpleDateFormat("yyyy-MM-dd"); 
    		Date date = df.parse(signingTime);  
    		par.add(date);
    		
    	}
		
		//送签时间
		if(null == visaDeliveryTime || "".equals(visaDeliveryTime)){
			par.add(null);
		}else{
			if(visaDeliveryTime.contains(":"))
        		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        		else
        			df = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = df.parse(visaDeliveryTime);  
			par.add(date);
			
		}
		
		//出签时间
		if(null == visaGotTime || "".equals(visaGotTime)){
			par.add(null);
		}else{
			if(visaGotTime.contains(":"))
        		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        		else
        			df = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = df.parse(visaGotTime);  
			par.add(date);
			
		}
		
		//续补资料时间
		if(null == supplementaryInfoTime || "".equals(supplementaryInfoTime)){
			par.add(null);
		}else{
			if(supplementaryInfoTime.contains(":"))
        		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        		else
        			df = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = df.parse(supplementaryInfoTime);  
			par.add(date);
			
		}
		
		int count = updateBySql(batchUpdateTimeSql,par.toArray());
		return count==1?true:false ;
	}
	
	public boolean saveGroup(String sql ,List<Object> list ){
		int count = updateBySql(sql,list.toArray());
		return count==1?true:false ;
	}

	@Override
	public List<Map<String, Object>> findListByOrderIdAndTravelerId(
			Integer orderId, Integer travellerId) {
		String sql = "select id,visa_country_name,visa_consulardistric_name,visa_type_name,DATE_FORMAT(about_signing_time,'%d/%m/%Y') about_signing_time " +
				" from group_control_visa where order_id = ? and traveler_id =? and delFlag=0 ";
		List<Map<String, Object>> list = findBySql(sql, Map.class, orderId, travellerId);
		return list;
	}
}
