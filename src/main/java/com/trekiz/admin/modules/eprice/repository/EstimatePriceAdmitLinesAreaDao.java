package com.trekiz.admin.modules.eprice.repository;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
/**
 * 
 * 
 * @see AdmitLinesArea
 * @author yue.wang
 */
@Component("estimatePriceAdmitLinesAreaDao")
public class EstimatePriceAdmitLinesAreaDao extends BaseDaoImpl<EstimatePriceAdmitLinesAreaDao> {
	private static final Logger log = LoggerFactory.getLogger(EstimatePriceAdmitLinesAreaDao.class);
	
	public void save(Long admitId,Long areaId) {
		log.debug("saving AdmitLinesArea instance");
		try {
			String sql = " insert into estimate_price_admit_lines_area(admit_id ,area_id ) values("+admitId+","+areaId+")";
			getSession().createSQLQuery(sql).executeUpdate();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void deleteByAdmitId(Long id) {
		// TODO Auto-generated method stub
		log.debug("delete AdmitLinesArea instance");
		try {
			String sql = " delete from estimate_price_admit_lines_area where admit_id = "+id;
			getSession().createSQLQuery(sql).executeUpdate();
			log.debug("del successful");
		} catch (RuntimeException re) {
			log.error("del failed", re);
			throw re;
		}
	}
	
	/*
	 * 获取线路国家名称（韩国,朝鲜)
	 * params admitId 地接询价表id
	 * 
	 */
	public String getLinesNames(String ammitId){
		String sql = " select group_concat(area.name)   " +
				" from   estimate_price_admit_requirements admit , estimate_price_admit_lines_area ala , sys_area area " +
				" where admit.id = ala.admit_id and area.id = ala.area_id " +
				" and admit.id = "+ammitId;
		Query queryName = getSession().createSQLQuery(sql);
		
		@SuppressWarnings("rawtypes")
        List nameList  = queryName.list();
		if(nameList!=null&&nameList.size()>0){
			return nameList.get(0)==null?"":nameList.get(0).toString();
		} 
		return "";
		
		
	}

}
