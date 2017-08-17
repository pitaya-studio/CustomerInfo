package com.trekiz.admin.modules.eprice.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficRequirements;

/**
 * @see EstimatePriceTrafficRequirements
 * @author lihua.xu
 */
@Component("estimatePriceTrafficRequirementsDao")
public class EstimatePriceTrafficRequirementsDao extends BaseDaoImpl<EstimatePriceTrafficRequirements>  {
	private static final Logger log = LoggerFactory.getLogger(EstimatePriceTrafficRequirementsDao.class);

	public void save(EstimatePriceTrafficRequirements transientInstance) {
		log.debug("saving EstimatePriceTrafficRequirements instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public EstimatePriceTrafficRequirements findById(java.lang.Long id) {
		log.debug("getting EstimatePriceTrafficRequirements instance with id: "
				+ id);
		try {
			EstimatePriceTrafficRequirements instance = (EstimatePriceTrafficRequirements) getSession()
					.get("com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficRequirements",
							id);
			if(instance==null){
				return null;
			}
			if(instance.getStatus().equals(EstimatePriceTrafficRequirements.STATUS_DEL)){
				return null;
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}