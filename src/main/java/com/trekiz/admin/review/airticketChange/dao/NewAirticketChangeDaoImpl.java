package com.trekiz.admin.review.airticketChange.dao;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
@Repository
public class NewAirticketChangeDaoImpl extends BaseDaoImpl<Map<String,Object>> implements INewAirticketChangeDao{

	public Map<String,Object> queryApprovalDetailTravel(HttpServletRequest request, HttpServletResponse response,String reviewId){
		
		String sql = "SELECT r.id,t.`name`,r.order_id,r.create_by,r.traveller_id,o.total_money ," +
				"(SELECT GROUP_CONCAT(CONCAT(c.currency_name) SEPARATOR '+') bz FROM money_amount ma " +
				"LEFT JOIN currency c ON ma.currencyId = c.currency_id WHERE ma.serialNum = t.payPriceSerialNum GROUP BY ma.serialNum) bz," +
				"(SELECT GROUP_CONCAT(CONCAT(ma.amount) SEPARATOR '+') je FROM money_amount ma " +
				"LEFT JOIN currency c ON ma.currencyId = c.currency_id WHERE ma.serialNum = t.payPriceSerialNum GROUP BY ma.serialNum) je," +
				"o.create_date" +
				" FROM review_new r, airticket_order o, traveler t " +
				" WHERE t.orderId = r.order_id and t.id = r.traveller_id" +
				" AND o.id=r.order_id AND r.id = '" + reviewId + "'";
		
		Page<Map<String, Object>> page = super.findBySql(new Page<Map<String,Object>>(request, response),sql,Map.class);
		return page.getList().get(0);
		
	}

}
