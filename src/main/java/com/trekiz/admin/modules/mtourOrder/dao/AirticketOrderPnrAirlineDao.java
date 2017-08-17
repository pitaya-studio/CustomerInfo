/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirline;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface AirticketOrderPnrAirlineDao  extends BaseDao<AirticketOrderPnrAirline> {
	
	public AirticketOrderPnrAirline getByUuid(String uuid);
	
	/**
	 * @param id  airticket_order_pnr.uuid
	 * @return
	 */
	public List<AirticketOrderPnrAirline> findByOrderPpnrUuid(String uuid); 
}
