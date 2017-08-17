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
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirlinePrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface AirticketOrderPnrAirlinePriceDao  extends BaseDao<AirticketOrderPnrAirlinePrice> {
	
	public AirticketOrderPnrAirlinePrice getByUuid(String uuid);
	
	public AirticketOrderPnrAirlinePrice getByCostRecordUuid(String costRecordUuid);
	
	/**
	 * 根据订单Id获取pnr航段外报价和订单改价总和
	 * @author zhaohaiming
	 * @param orderId
	 * @return list
	 * */
	public List<Map<String,Object>> getTotalMoneyByOrderId(Long orderId);
	
	/**
	 * 根据订单id获取机票航段价格信息集合
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return List<AirticketOrderPnrAirlinePrice>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-20 下午11:19:54
	 */
	public List<AirticketOrderPnrAirlinePrice> getAirlinePricesByOrdereId(Long orderId); 
	
	public List<AirticketOrderPnrAirlinePrice> getAirlinePricesByPnrAirlineUuid(String uuid, Integer priceType); 
}
