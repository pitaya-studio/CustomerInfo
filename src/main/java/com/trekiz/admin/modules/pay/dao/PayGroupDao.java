/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.dao;
import java.util.Date;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.pay.entity.PayGroup;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface PayGroupDao  extends BaseDao<PayGroup> {
	
	public PayGroup getByUuid(String uuid);
	
	public void save(PayGroup payGroup);

	public void update(PayGroup payGroup);

	public PayGroup getById(Integer id);
	
	public List<PayGroup> findById(Integer id);
	
	/**
	 * 根据订单类型和团期id获取团期支付信息
	 * @Title: findByOrderTypeAndGroupId
	 * @return List<PayGroup>
	 * @author majiancheng
	 * @date 2015-11-1 下午2:40:06
	 */
	public List<PayGroup> findByOrderTypeAndGroupId(int orderType, int groupId);

	/**
	 * 根据订单类型和成本记录ID查询其他收入收款记录
	 * @Title: findByOrderTypeAndCostRecordId
	 * @return PayGroup
	 * @author shijun.liu
	 * @date 2016.03.11
	 */
	public PayGroup findByOrderTypeAndCostRecordId(int orderType, int costRecordId);
	
	public void updateAccountStatus(Integer status,Integer updateBy,Date updateDate,Integer id);
}
