package com.trekiz.admin.modules.eprice.repository;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePricerReply;

/**
 * 
 * @see com.trekiz.admin.modules.eprice.entity.EstimatePricerReply
 * @author MyEclipse Persistence Tools
 */
@Component("estimatePricerReplyDao")
public class EstimatePricerReplyDao extends BaseDaoImpl<EstimatePricerReply>  {
	private static final Logger log = LoggerFactory.getLogger(EstimatePricerReplyDao.class);

	public void save(EstimatePricerReply transientInstance) {
		log.debug("saving EstimatePricerReply instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public EstimatePricerReply findById(java.lang.Long id) {
		log.debug("getting EstimatePricerReply instance with id: " + id);
		try {
			EstimatePricerReply instance = (EstimatePricerReply) getSession()
					.get("com.trekiz.admin.modules.eprice.entity.EstimatePricerReply", id);
			if(instance.getStatus().equals(EstimatePricerReply.STATUS_DEL)){
				return null;
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public  EstimatePricerReply findReplyByRidAndOperatorUserId(Long rid,Long operatorUserId,String types){
		String hql   = "from EstimatePricerReply r where r.status!="+EstimatePricerReply.STATUS_DEL+" and r.rid="+rid+" and r.operatorUserId="+operatorUserId+" and r.type in ("+types+")";
		
		Query q = getSession().createQuery(hql);
		
		
		
		return (EstimatePricerReply)q.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
    public List<EstimatePricerReply> findReply(Long rid,String type){
		String hql = "from EstimatePricerReply r where r.status!="+EstimatePricerReply.STATUS_DEL+" and r.rid="+rid+" and r.type in ("+type+")";
		Query q = getSession().createQuery(hql);
		
		return q.list();
	}

	@SuppressWarnings("unchecked")
    public List<EstimatePricerReply> findReplyByAdmit(Long rid2) {
		// TODO Auto-generated method stub
		String hql = "from EstimatePricerReply r where r.status!="+EstimatePricerReply.STATUS_DEL+" and r.rid="+rid2+" and r.type in (1,3,4,5)";
		Query q = getSession().createQuery(hql);
		return q.list();
	}
	
}