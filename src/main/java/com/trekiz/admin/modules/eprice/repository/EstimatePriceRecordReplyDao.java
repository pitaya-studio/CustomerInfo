package com.trekiz.admin.modules.eprice.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecordReply;

@Component("estimatePriceRecordReplyDao")
public class EstimatePriceRecordReplyDao extends BaseDaoImpl<EstimatePriceRecordReply>{
	
	/**
	 * 通过当前登录的计调用户ID和询价记录ID，进行查询(单团（地接）计调专用)
	 * @param operatorUserId
	 * @param pid
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findByOpeId(Long operatorUserId,Long recordId){
		Session session = getSession();
		String sql = new String();
		List<Object[]> replist = new ArrayList<Object[]>();
		try{
			sql = "select rep.`pid`,  rep.`rid`,  rep.`id`,  rep.`status`,  rep.`operator_user_id`,  admit.`create_time`,  rep.`operator_price_time`, "
					+ " admit.`dgroup_out_date`,  admit.`outside_day_sum`,  admit.`outside_night_sum`,  admit.`travel_country`,"
					+ "  admit.`travel_team_type`,  rep.`operator_total_price` , admit.id as admitId,rep.price_detail " 
					+ " FROM `estimate_price_project` epp, `estimate_price_record` rec,  `estimate_pricer_reply` rep, "
					+ " `estimate_price_admit_requirements` admit "
					+ " WHERE   epp.`id` = "+recordId+" AND rec.`pid` = epp.`id` AND rec.`id` = rep.`rid`"
					+ " AND admit.`id` = rec.`admit_requirements_id`  AND rec.status>0  AND rep.`status`>0"
					+ "  AND rep.`operator_user_id`='"+operatorUserId+"'  "
					+ " AND rep.`type` in (1,3,4,5)  "  
					+  " order by  rec.id desc ";
			Query queryObject = session.createSQLQuery(sql);
			replist = queryObject.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return replist;
	}
	
	public Integer findByOpeIdNum(Long operatorUserId,Long recordId){
		Session session = getSession();
		String sql = new String();
		Integer repNum = new Integer(0);
		try{
			sql = "select rep.`pid`,  rep.`rid`,  rep.`id`,  rep.`status`,  rep.`operator_user_id`,  admit.`create_time`,  rep.`operator_price_time`, "
					+ " admit.`dgroup_out_date`,  admit.`outside_day_sum`,  admit.`outside_night_sum`,  admit.`travel_country`,"
					+ "  admit.`travel_team_type`,  rep.`operator_total_price`"
					+ " FROM `estimate_price_project` epp, `estimate_price_record` rec,  `estimate_pricer_reply` rep, "
					+ " `estimate_price_admit_requirements` admit "
					+ " WHERE   epp.`id` = "+recordId+" AND rec.`pid` = epp.`id` AND rec.`id` = rep.`rid`"
					+ " AND admit.`pid` = rec.`pid`  AND rec.status>0  AND rep.`status`>0"
					+ "  AND rep.`operator_user_id`='"+operatorUserId+"'  "
					+ " AND rep.`type` = 1";
			Query queryObject = session.createSQLQuery(sql);
			if(queryObject.list()!=null && queryObject.list().size()>0){
				repNum = queryObject.list().size();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return repNum;
	}
	
	/**
	 * 通过当前登录的计调用户ID和询价记录ID，进行查询(机票计调专用)
	 * @param operatorUserId
	 * @param pid
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findTrafficByOpeId(Long operatorUserId,Long recordId){
		Session session = getSession();
		String sql = new String();
		List<Object[]> replist = new ArrayList<Object[]>();
		try{
			sql = "SELECT  rep.`pid`,  rep.`rid`,  rep.`id`,  rep.`status`,  rep.`operator_user_id`"
					+ ",  tra.`create_time`,  rep.`operator_price_time`,  rep.`operator_total_price`"
					+" , rec.start_city, rec.end_city,rep.price_detail "
					//+ ",  rec.`type`,  rep.`type`"
					+ " FROM "
					+ " `estimate_price_record` rec,"
					+ "  `estimate_pricer_reply` rep,"
					+ "  `estimate_price_traffic_requirements` tra "
					+ " WHERE "
					+ "  rec.`id` = rep.`rid`"
					+ "  AND tra.`id` = rec.`traffic_requirements_id`"
					+ "  AND rec.status>0"
					+ "  AND rep.`status`>0"
					+ "  AND rec.pid='"+recordId+"'"
					+ "  AND rep.`operator_user_id`='"+operatorUserId+"'"
				//	+ "  AND rec.`type`=1 "
					+ "  AND rep.`type` =7 " 
					+ "  order by rec.id desc ";
			Query queryObject = session.createSQLQuery(sql);
			if(queryObject.list()!=null && queryObject.list().size()>0){
				replist = queryObject.list();
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}
		return replist;
	}
	
	public Integer findTrafficByOpeIdNum(Long operatorUserId,Long recordId){
		Session session = getSession();
		String sql = new String();
		Integer repNum = new Integer(0);
		try{
			sql = "SELECT  rep.`pid`,  rep.`rid`,  rep.`id`,  rep.`status`,  rep.`operator_user_id`"
					+ ",  tra.`create_time`,  rep.`operator_price_time`,  rep.`operator_total_price`"
					//+ ",  rec.`type`,  rep.`type`"
					+ " FROM "
					+ " `estimate_price_record` rec,"
					+ "  `estimate_pricer_reply` rep,"
					+ "  `estimate_price_traffic_requirements` tra "
					+ " WHERE "
					+ "  rec.`id` = rep.`rid`"
					+ "  AND tra.`pid` = rec.`pid`"
					+ "  AND rec.status>0"
					+ "  AND rep.`status`>0"
					+ "  AND rec.pid='"+recordId+"'"
					+ "  AND rep.`operator_user_id`='"+operatorUserId+"'"
				//	+ "  AND rec.`type`=1 "
					+ "  AND rep.`type` =2";
			Query queryObject = session.createSQLQuery(sql);
			if(queryObject.list()!=null && queryObject.list().size()>0){
				repNum = queryObject.list().size();
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}
		return repNum;
	}
	/**
	 * 通过询价记录ID，获取多段航线
	 * @param recId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public String findStartToEnd(Long recId){
		Session session = getSession();
		String sql = new String();
		List<Object[]> replist = new ArrayList<Object[]>();
		String back = null; // 出发地-抵达地
		try{
			sql = "SELECT rec.`id` as recId,line.`id` as lineId,line.`start_city_id`,"
					+ "line.`start_city_name`,line.`end_city_id`" 
					+ ",line.`end_city_name` "
					+ "FROM  `estimate_price_record` rec , "
					+ " `estimate_price_traffic_requirements` tra, "
					+ " `estimate_price_traffic_line` line "
					+ "WHERE "
					+ " rec.`traffic_requirements_id` = tra.`id` "
					+ " AND line.`pfid` = tra.`id` "
					+ " AND rec.id='"+recId+"'";
			Query queryObject = session.createSQLQuery(sql);
			if(queryObject.list()!=null && queryObject.list().size()>0){
				replist = queryObject.list();
			}
			
			// 多段航线：包装成只有第一段航线出发地，和最后一段航线出发地的Map
			// 单段航线：直接包装出发抵达地
			String startCity = null;
			String endCity = null;
			String endCityother = null;
			
			if(replist!=null && replist.size()>0){
				Iterator<Object[]> iter = replist.iterator();
				if(replist.size()==1){	// 单段航线
					while(iter.hasNext()){
						Object[] obj = iter.next();
						startCity = (String)obj[3];
						endCity = (String)obj[5];
					}
					back = startCity+"-"+endCity;
				}else{	// 多段航线
					Object[] objs = iter.next();
					startCity = (String)objs[3];
					endCity = (String)objs[5];
					while(iter.hasNext()){
						Object[] obj = iter.next();
						endCityother = (String)obj[3];
						endCity = (String)obj[5];
					}
					if(startCity!=null && endCity!=null && startCity.equals(endCity)){
						back = startCity+"-"+endCity;
					}else{
						back = startCity+"-"+endCityother;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return back;
	}
}
