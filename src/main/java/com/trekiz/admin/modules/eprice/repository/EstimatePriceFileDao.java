package com.trekiz.admin.modules.eprice.repository;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceFile;

/**
 *
 * 
 * @see EstimatePriceFile
 * @author lihua.xu
 */
@Component("estimatePriceFileDao")
public class EstimatePriceFileDao extends BaseDaoImpl<EstimatePriceFile>  {
	private static final Logger log = LoggerFactory.getLogger(EstimatePriceFileDao.class);
	// property constants
	private static final String PID = "pid";

	public void save(EstimatePriceFile transientInstance) {
		log.debug("saveOrUpdateing EstimatePriceFile instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public EstimatePriceFile findById(java.lang.Long id) {
		log.debug("getting EstimatePriceFile instance with id: " + id);
		try {
			EstimatePriceFile instance = (EstimatePriceFile) getSession().get(
					"com.trekiz.admin.modules.eprice.entity.EstimatePriceFile", id);
			if(instance==null){
				return null	;
			}
			if(instance.getStatus().equals(EstimatePriceFile.STATUS_DEL)){
				return null;
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EstimatePriceFile> findByExample(EstimatePriceFile instance) {
		log.debug("finding EstimatePriceFile instance by example");
		try {
			List<EstimatePriceFile> results = (List<EstimatePriceFile>) getSession()
					.createCriteria("com.trekiz.admin.modules.eprice.entity.EstimatePriceFile")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	private List findByProperty(String propertyName, Object value) {
		log.debug("finding EstimatePriceFile instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EstimatePriceFile as model where model."
					+ propertyName + "= ? AND model.type = ?";//线上bug17315 add by chao.zhang 2017/02/08
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			queryObject.setParameter(1, 1);//线上bug17315 add by chao.zhang 2017/02/08
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EstimatePriceFile> findByPid(Object pid) {
		return findByProperty(PID, pid);
	}
	
}