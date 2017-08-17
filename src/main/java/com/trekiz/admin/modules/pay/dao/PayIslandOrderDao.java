/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.pay.entity.PayIslandOrder;


public interface PayIslandOrderDao  extends BaseDao<PayIslandOrder> {
	
	public PayIslandOrder getByUuid(String uuid);
	
	public PayIslandOrder getByPid(Integer pid);
}
