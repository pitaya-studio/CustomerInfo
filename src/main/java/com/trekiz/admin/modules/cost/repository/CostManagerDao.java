package com.trekiz.admin.modules.cost.repository;

import com.trekiz.admin.modules.cost.entity.AbstractSpecificCost;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CostManagerDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<AbstractSpecificCost> getSpecificCost(Long groupId,
			Class<?> type, List<Integer> states) {
		List<AbstractSpecificCost> list = new ArrayList<AbstractSpecificCost>();
		String statesArray = "";
		Query query;
		if (states != null && !states.isEmpty()) {
			for (Integer state : states) {
				statesArray += (state + ",");
			}
			statesArray = statesArray.substring(0, statesArray.length() - 1);
			query = entityManager.createQuery("from " + type.getName()
					+ " where activityGroupId=?1 and status in (" + statesArray
					+ ") order by id asc");
		} else {
			query = entityManager.createQuery("from " + type.getName()
					+ " where activityGroupId=?1 order by id asc");
		}
		query.setParameter(1, groupId);
		list = query.getResultList();
		return list;
	}

	public AbstractSpecificCost save(AbstractSpecificCost cost) {
		if (cost.getId() == null) {
			entityManager.persist(cost);
			return cost;
		} else {
			return entityManager.merge(cost);
		}
	}

	public void delete(Long id, Class<?> type) {
		Query query = entityManager.createQuery("delete from " + type.getName()
				+ " where id=?1");
		query.setParameter(1, id);
		query.executeUpdate();
	}

	/**
	 * 通过activityGroup的id删除下属的type对应的所有成本录入数据
	 * 
	 * @author lihua.xu
	 * @param gid
	 *            activityGroupId，团期id
	 * @param type
	 *            被删除数据对应的model类的class对象
	 */
	public void deleteByGroupId(Long gid, Class<?> type) {
		Query query = entityManager.createQuery("delete from " + type.getName()
				+ " where a=?1");
		query.setParameter(1, gid);
		query.executeUpdate();
	}

	public AbstractSpecificCost find(Long id, Class<?> type) {
		Query query = entityManager.createQuery("from " + type.getName()
				+ " where id=?1");
		query.setParameter(1, id);
		return (AbstractSpecificCost) query.getSingleResult();
	}
}
