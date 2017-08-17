/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.dao.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderPnrAirlinePriceDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirlinePrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class AirticketOrderPnrAirlinePriceDaoImpl extends BaseDaoImpl<AirticketOrderPnrAirlinePrice>  implements AirticketOrderPnrAirlinePriceDao{
	@Override
	public AirticketOrderPnrAirlinePrice getByUuid(String uuid) {
		Object entity = super.createQuery("from AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice where airticketOrderPnrAirlinePrice.uuid=? and airticketOrderPnrAirlinePrice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (AirticketOrderPnrAirlinePrice)entity;
		}
		return null;
	}
	
	@Override
	public AirticketOrderPnrAirlinePrice getByCostRecordUuid(String costRecordUuid) {
		Object entity = super.createQuery("from AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice where airticketOrderPnrAirlinePrice.costRecordUuid=? and airticketOrderPnrAirlinePrice.delFlag=?", costRecordUuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (AirticketOrderPnrAirlinePrice)entity;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getTotalMoneyByOrderId(Long orderId) {
		String sql ="SELECT currency_id, sum(money) money FROM ( SELECT airticket_order_id orderId, currency_id, sum(total) money " +
				"FROM ( SELECT currency_id, airticket_order_id, ( CASE a.compute_type WHEN 0 THEN price WHEN 1 THEN 0 - price END ) " +
				"total FROM airticket_order_changePrice a WHERE airticket_order_id = ? AND delFlag = 0 ) a GROUP BY a.airticket_order_id, " +
				"a.currency_id UNION SELECT airticket_order_id orderId, currency_id, sum(price * personNum) money FROM " +
				"airticket_order_pnr_airline_price WHERE airticket_order_id = ?  AND price_type=1 AND delflag = 0 GROUP BY orderId, currency_id ) tn GROUP BY currency_id";
		List<Map<String,Object>> list = this.findBySql(sql, Map.class,orderId,orderId);
		return list;
	}
	
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
	public List<AirticketOrderPnrAirlinePrice> getAirlinePricesByOrdereId(Long orderId) {
		return super.find("from AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice where airticketOrderPnrAirlinePrice.airticketOrderId=? and airticketOrderPnrAirlinePrice.delFlag=?", orderId.intValue(), BaseEntity.DEL_FLAG_NORMAL);
	}

	@Override
	public List<AirticketOrderPnrAirlinePrice> getAirlinePricesByPnrAirlineUuid(String uuid, Integer priceType) {
		return super.find("from AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice where airticketOrderPnrAirlinePrice.airticketOrderPnrAirlineUuid=? and airticketOrderPnrAirlinePrice.priceType = ? and airticketOrderPnrAirlinePrice.delFlag=0", uuid, priceType);
	}

	
}
