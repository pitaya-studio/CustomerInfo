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
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderChangePriceDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderChangePrice;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class AirticketOrderChangePriceDaoImpl extends BaseDaoImpl<AirticketOrderChangePrice>  implements AirticketOrderChangePriceDao{
	@Override
	public AirticketOrderChangePrice getByUuid(String uuid) {
		Object entity = super.createQuery("from AirticketOrderChangePrice airticketOrderChangePrice where airticketOrderChangePrice.uuid=? and airticketOrderChangePrice.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (AirticketOrderChangePrice)entity;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getChangPriceTotal(Long orderId) {
		String sql ="SELECT currency_id, airticket_order_id, sum(total) changeMoney FROM ( SELECT currency_id, airticket_order_id, ( CASE a.compute_type WHEN 0 THEN price WHEN 1 THEN 0 - price END ) total FROM airticket_order_changePrice a WHERE airticket_order_id = ? AND delFlag = 0 ) a GROUP BY a.airticket_order_id, a.currency_id";
		List<Map<String,Object>> list = findBySql(sql, Map.class,orderId);
		return list;
	}
	
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
	public List<AirticketOrderChangePrice> getChangePricesByOrderId(Long orderId) {
		return super.find("from AirticketOrderChangePrice airticketOrderChangePrice where airticketOrderChangePrice.airticketOrderId=? and airticketOrderChangePrice.delFlag=?", orderId.intValue(), BaseEntity.DEL_FLAG_NORMAL);
	}

	
}
