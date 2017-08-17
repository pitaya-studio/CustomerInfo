/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.dao;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderChangePrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface AirticketOrderChangePriceDao  extends BaseDao<AirticketOrderChangePrice> {
	
	public AirticketOrderChangePrice getByUuid(String uuid);
	
	/**
	 * 获取改价总和
	 * @param orderId
	 * @return list
	 * @author zhaohaiming
	 * */
	public List<Map<String,Object>> getChangPriceTotal(Long orderId);
	
	/**
	 * 根据订单id获取美途机票快速订单外报价集合信息
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return List<AirticketOrderChangePrice>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-20 下午11:09:03
	 */
	public List<AirticketOrderChangePrice> getChangePricesByOrderId(Long orderId);
}
