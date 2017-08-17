/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.dao.impl;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.pay.dao.PayGroupDao;
import com.trekiz.admin.modules.pay.entity.PayGroup;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class PayGroupDaoImpl extends BaseDaoImpl<PayGroup>  implements PayGroupDao{
	@Override
	public PayGroup getByUuid(String uuid) {
		Object entity = super.createQuery("from PayGroup payGroup where payGroup.uuid=? and payGroup.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (PayGroup)entity;
		}
		return null;
	}
	
	public void save(PayGroup payGroup) {
		super.getSession().save(payGroup);
		super.getSession().flush();
	}

	public void update(PayGroup payGroup) {
		super.getSession().update(payGroup);
		super.getSession().flush();
	}

	public PayGroup getById(Integer id) {
		Object entity = super.createQuery("from PayGroup payGroup where payGroup.id=? and payGroup.delFlag=?", id, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (PayGroup)entity;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PayGroup> findById(Integer id) {
		List<PayGroup> list = super.createQuery("from PayGroup payGroup where payGroup.costRecordId=? and payGroup.delFlag=?", id, BaseEntity.DEL_FLAG_NORMAL).list();
		if(list != null) {
			return list;
		}
		return null;
	}
	
	/**
	 * 根据订单类型和团期id获取团期支付信息
	 * @Title: findByOrderTypeAndGroupId
	 * @return List<PayGroup>
	 * @author majiancheng
	 * @date 2015-11-1 下午2:40:06
	 */
	public List<PayGroup> findByOrderTypeAndGroupId(int orderType, int groupId) {
		return super.find("from PayGroup payGroup where payGroup.orderType=? and payGroup.groupId=? and payGroup.delFlag=?", orderType, groupId, BaseEntity.DEL_FLAG_NORMAL);
	}

	/**
	 * 根据订单类型和成本记录ID查询其他收入收款记录
	 * @Title: findByOrderTypeAndCostRecordId
	 * @return PayGroup
	 * @author shijun.liu
	 * @date 2016.03.11
	 */
	public PayGroup findByOrderTypeAndCostRecordId(int orderType, int costRecordId) {
		PayGroup payGroup = null;
		List<PayGroup> list = super.find("from PayGroup payGroup where payGroup.orderType=? and payGroup.costRecordId=? and payGroup.delFlag=?",
				orderType, costRecordId, BaseEntity.DEL_FLAG_NORMAL);
		if(CollectionUtils.isNotEmpty(list)){
			payGroup = list.get(0);
		}
		return payGroup;
	}

	@Override
	public void updateAccountStatus(Integer status, Integer updateBy,Date updateDate, Integer id) {
		PayGroup pg = super.getById(id);
		pg.setIsAsAccount(status);
		pg.setUpdateBy(updateBy);
		pg.setUpdateDate(updateDate);
		super.updateObj(pg);
	}
}
