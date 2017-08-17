package com.trekiz.admin.modules.eprice.repository;

import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficLine;

/**
 * 
 * @see EstimatePriceTrafficLine
 * @author lihua.xu
 */
@Component("estimatePriceTrafficLineDao")
public class EstimatePriceTrafficLineDao extends BaseDaoImpl<EstimatePriceTrafficLine>  {
	private static final Logger log = LoggerFactory.getLogger(EstimatePriceTrafficLineDao.class);

	public void save(EstimatePriceTrafficLine transientInstance) {
		log.debug("saving EstimatePriceTrafficLine instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public EstimatePriceTrafficLine findById(java.lang.Long id) {
		log.debug("getting EstimatePriceTrafficLine instance with id: " + id);
		try {
			EstimatePriceTrafficLine instance = (EstimatePriceTrafficLine) getSession()
					.get("com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficLine", id);
			if(instance.getStatus().equals(EstimatePriceTrafficLine.STATUS_DEL)){
				return null;
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/**
	 * 通过机票询价内容id获取有效的交通线路列表
	 * @author lihua.xu
	 * @时间 2014年10月8日
	 * @param pfid 机票询价内容id
	 * @return List<EstimatePriceTrafficLine>
	 */
	@SuppressWarnings("unchecked")
    public List<EstimatePriceTrafficLine> findByPfid(Long pfid){
		String hql = "from EstimatePriceTrafficLine p  where p.pfid="+pfid+"  ";
		Query q = this.getSession().createQuery(hql);
		
		return q.list();
	}
}