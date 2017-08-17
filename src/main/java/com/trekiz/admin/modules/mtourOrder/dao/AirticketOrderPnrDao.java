/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.dao;

import com.trekiz.admin.common.persistence.BaseDao;

import java.util.*;

import com.trekiz.admin.modules.mtourOrder.entity.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface AirticketOrderPnrDao  extends BaseDao<AirticketOrderPnr> {
	
	public AirticketOrderPnr getByUuid(String uuid);
	
	public List<AirticketOrderPnr> getByAirticketOrderId(Integer airticketOrderId);
	
	public List<Map<String,Object>> queryAirticketOrderPNCByOrderUuid(Integer airticketOrderId);
	
	/**
	 * 通过PNR组的uuid得到该组下的pnr集合
	 */
	public List<AirticketOrderPnr> findByPNRGroupUuid(String pnrGroupUuid);
}
