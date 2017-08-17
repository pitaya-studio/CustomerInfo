package com.trekiz.admin.modules.eprice.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceStatistics;
import com.trekiz.admin.modules.eprice.form.EstimatePriceStatisticsForm;

@Component("estimatePriceStatisticsDao")
public class EstimatePriceStatisticsDao extends BaseDaoImpl<EstimatePriceStatistics> {
	
	/**
	 * 查询询价统计
	 * @param salerList  需要查询的销售相关数组(包括销售员id,销售员名字）
	 * @param epsForm 包含查询条件
	 * @return
	 */
	public List<Map<String, Object>> find(ArrayList<Map<String,Object>> salerList,EstimatePriceStatisticsForm epsForm){
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if(salerList.isEmpty()){
			return returnList;
		}
		// 组合查询sql语句
		
		// sql 子查询
		StringBuffer sonSql = new StringBuffer();
		sonSql.append("SELECT sa.id AS  'countryId',");
		sonSql.append(" sa.name AS 'countryName', ");
		sonSql.append("epr.`user_id` AS 'userName', ");
		sonSql.append(" COUNT(epr.id) AS 'allSum'  ");
		sonSql.append(" FROM ");
		sonSql.append(" `estimate_price_record` epr, `estimate_price_admit_lines_area` epala,`sys_area` sa ");
		sonSql.append(" WHERE ");
		//sonSql.append(" epr.`base_info_id`=epbi.`id` ");
		//sonSql.append(" AND epr.`admit_requirements_id` = epar.`id` ");
		sonSql.append(" epr.`admit_requirements_id`= epala.admit_id");
		sonSql.append(" AND sa.id = epala.area_id ");
		// 询单开始时间
		if(epsForm.getBeginTime()!=null){
			sonSql.append(" AND epr.`create_time` >= '"+epsForm.getBeginTime()+" 00:00:00'");
		}
		// 询单结束时间
		if(epsForm.getEndTime()!=null){
			sonSql.append(" AND epr.`create_time` <= '"+epsForm.getEndTime()+" 23:59:59'");
		}
		// 指定线路国家
		if(epsForm.getCountryId()!=null){
			sonSql.append(" AND epala.area_id IN ( ");
			sonSql.append(epsForm.getCountryId());
			sonSql.append(" ) ");
		}else{
//			List<Integer> countryId = epsForm.getCountryIdList();
//			if(!countryId.isEmpty()){
//				sonSql.append(" AND epala.area_id IN ( ");
//				Iterator<Integer> iter = countryId.iterator();
//				int num = countryId.size();
//				int num2 = 1;
//				while(iter.hasNext()){
//					int country = iter.next();
//					sonSql.append( country );
//					if(num2!=num){
//						sonSql.append(",");
//					}
//					num2++;
//				}
//				sonSql.append(" ) ");
//			}
		}
		// 指定销售ID
		if(!salerList.isEmpty()){
			sonSql.append(" AND epr.`user_id` IN ( ");
			Iterator<Map<String,Object>> iter = salerList.iterator();
			int num = salerList.size();
			int num2 = 1;
			while(iter.hasNext()){
				Map<String,Object> map = iter.next();
				sonSql.append(map.get("id"));
				if(num!=num2){
					sonSql.append(",");
				}
				num2++;
			}
			sonSql.append(" ) ");
		}

		sonSql.append(" GROUP BY sa.`id` ");
		// sql 父查询
		StringBuffer fatherSql= new StringBuffer();
		fatherSql.append("select ret.countryId,ret.countryName, ");
		int code = 0;
		List<Integer> salers = new ArrayList<Integer>();
		for(Map<String,Object> salerMap : salerList){
			fatherSql.append(" CASE ret.userName  WHEN '"+salerMap.get("id")+"' THEN ret.allSum ELSE 0 END AS 's"+code+"'");
			salers.add(Integer.valueOf((salerMap.get("id")).toString())); 
			code++;
			if(code!=salerList.size()){
				fatherSql.append(",");
			}
		}
		fatherSql.append(" FROM (");
		fatherSql.append(sonSql+") ret ");
		
		// sql 顶级查询
		StringBuffer grandpaSql = new StringBuffer();
		grandpaSql.append("Select t.countryId AS 'countryId', t.countryName AS 'countryName', ");
		int code2 = 0;
		List<Integer> salers2 = new ArrayList<Integer>();
		for(Map<String,Object> salerMap : salerList){
			grandpaSql.append(" SUM(s"+code2+") AS 'salerStatistics["+code2+"]'");
			salers2.add(Integer.valueOf((salerMap.get("id")).toString())); 
			code2++;
			if(code2!=salerList.size()){
				grandpaSql.append(",");
			}
		}
		grandpaSql.append(" FROM (");
		grandpaSql.append(fatherSql+") t ");
		grandpaSql.append(" GROUP BY t.countryName ");
		grandpaSql.append(" ORDER BY t.countryId ");
		
		 returnList = this.findBySql(grandpaSql.toString(), Map.class);
		System.out.println("dao----dao---aaaaaaaaaaaatest--------"+returnList);
		
		return returnList;
	}

}
