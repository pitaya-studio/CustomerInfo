package com.trekiz.admin.modules.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.repository.CostchangeDao;

/**
 * 
 *  文件名: CostchangeService.java
 *  功能:费用信息
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-10-30 上午11:25:08
 *  @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class CostchangeService extends BaseService{
	
	@Autowired
	private CostchangeDao costchangeDao;
	
	public Costchange save(Costchange costchange){
		return costchangeDao.save(costchange);
	}
	
	public void delete(Long travelerId){
		costchangeDao.deleteCostchangeByTravelerId(travelerId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Costchange> findCostchangeByTravelerId(Long travelerId){
		String hql = "from Costchange where travelerId = ? and status = 2";
		return costchangeDao.getSession().createQuery(hql)
					.setParameter(0, travelerId)
					.list();
	}
	
	/**
	 * 根据orderId查询其他费用
	 * @param orderId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findCostChangeByOrderId(Long visaOrderId){
		if(visaOrderId != null){
			String sql = "SELECT count(1),c.costName,c.costSum,cu.currency_mark from costchange c ,traveler t, currency cu where c.travelerId=t.id and c.price_currency=cu.currency_id and c.status = 2 and t.orderId="
					+ visaOrderId
					+ " GROUP BY c.costName,c.price_currency,c.costSum";
			return costchangeDao.getSession().createSQLQuery(sql).list();
		}else{
			return null;
		}
	}

	/**
	 * 更新costchange表中状态
	 * @param status
	 * @param reviewUuid
	 */
	public void updateStatusByReviewUuid(Integer status, String reviewUuid) {
		costchangeDao.updateStatusByReviewUuid(status, reviewUuid);
	}

	/**
	 * 查询是否有正在审批的游客数据
	 * @param travelerId
	 * @param status
	 */
	public List<Costchange> findCostchangeBytravelerIdAndStatus(Long travelerId, Integer status) {
		return costchangeDao.findCostchangeBytravelerIdAndStatus(travelerId, status);
	}
}