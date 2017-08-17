package com.trekiz.admin.modules.eprice.repository;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceBaseInfo;


/**
 *
 * @see EstimatePriceBaseInfo
 * @author lihua.xu
 */
@Component("estimatePriceBaseInfoDao")
public class EstimatePriceBaseInfoDao extends BaseDaoImpl<EstimatePriceBaseInfo> {
	private static final Logger log = LoggerFactory.getLogger(EstimatePriceBaseInfoDao.class);
	// property constants
	private static final String PID = "pid";

	
	public EstimatePriceBaseInfo save(EstimatePriceBaseInfo transientInstance) {
		log.debug("saving EstimatePriceBaseInfo instance");
		try {
			//getSession().saveOrUpdate(transientInstance);
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
		return transientInstance;
	}
	
	public void update(EstimatePriceBaseInfo epbi) {
		log.debug("saving EstimatePriceBaseInfo epbi");
		try {
			
			getSession().update(epbi);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public EstimatePriceBaseInfo findById(java.lang.Long  id) {
		log.debug("getting EstimatePriceBaseInfo instance with id: " + id);
		try {
			EstimatePriceBaseInfo instance = (EstimatePriceBaseInfo) getSession()
					.get("com.trekiz.admin.modules.eprice.entity.EstimatePriceBaseInfo", id);
			if(instance.getStatus().equals(EstimatePriceBaseInfo.STATUS_DEL)){
				return null;
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	

	@SuppressWarnings("rawtypes")
	private List findByProperty(String propertyName, Object value) {
		log.debug("finding EstimatePriceBaseInfo instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EstimatePriceBaseInfo as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EstimatePriceBaseInfo> findByPid(Object pid) {
		return findByProperty(PID, pid);
	}
}