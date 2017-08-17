package com.trekiz.admin.modules.eprice.repository;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceAdmitRequirements;



/**
 * 
 * 
 * @see EstimatePriceAdmitRequirements
 * @author lihua.xu
 */
@Component("estimatePriceAdmitRequirementsDao")
public class EstimatePriceAdmitRequirementsDao extends BaseDaoImpl<EstimatePriceAdmitRequirementsDao> {
	private static final Logger log = LoggerFactory.getLogger(EstimatePriceAdmitRequirementsDao.class);
	// property constants
	private static final String PID = "pid";

	public void save(EstimatePriceAdmitRequirements transientInstance) {
		log.debug("saving EstimatePriceAdmitRequirements instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public EstimatePriceAdmitRequirements findById(java.lang.Long id) {
		log.debug("getting EstimatePriceAdmitRequirements instance with id: "
				+ id);
		try {
			EstimatePriceAdmitRequirements instance = (EstimatePriceAdmitRequirements) getSession()
					.get("com.trekiz.admin.modules.eprice.entity.EstimatePriceAdmitRequirements", id);
			if(instance==null) {
				return null;
			}
			if(instance.getStatus().equals(EstimatePriceAdmitRequirements.STATUS_DEL)){
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
		log.debug("finding EstimatePriceAdmitRequirements instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EstimatePriceAdmitRequirements as model where model."
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
	public List<EstimatePriceAdmitRequirements> findByPid(Object pid) {
		return findByProperty(PID, pid);
	}
}