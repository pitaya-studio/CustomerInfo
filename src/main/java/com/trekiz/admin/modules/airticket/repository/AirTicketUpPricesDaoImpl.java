package com.trekiz.admin.modules.airticket.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
/**
 * 飞机票 改价
 * @author HPT
 *
 */
@Repository
public class AirTicketUpPricesDaoImpl  extends BaseDaoImpl<Map<String,Object>> implements AirTicketUpPricesDao {
	/**
	 *  查询流程表里的机票改价记录
	 *  	(建议使用公用工作流Service获取)
	 * @param flowId
	 * @param orderId
	 * @return
	 */
	@Deprecated
	@Override
	public List<Map<String, Object>> queryAirTicketUpPricesList(String flowId,
			String orderId) {
		StringBuffer sb = new StringBuffer();
		List<Map<String,Object>> resultList  = new ArrayList<>();
		sb.append("select r.id,r.cpid,r.productType,r.orderId,r.flowType,r.companyId," +
				  "r.travelerId,r.createReason,r.createDate,r.updateDate,r.`status` "+
				  " from review  r where r.active = 01 and r.orderId =? and r.id = ?");
		resultList = this.findBySql(sb.toString(),Map.class, orderId,flowId);
		return resultList;
	}
	/**
	 * 查询该订单下的游客列表
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryTravelerUpPrices(String orderId) {
		StringBuffer sb  = new StringBuffer();
		sb.append("select t.id,t.orderId,t.`name`,t.payPriceSerialNum,t.original_payPriceSerialNum ");
		sb.append("from traveler t,airticket_order air_o ");
//		sb.append("where t.orderId = air_o.id and air_o.id = ?  and   t.order_Type = ? and t.delFlag = '" + Context.DEL_FLAG_NORMAL + "'");
		//isAirticketflag =0代表退票，查询游客列表过滤掉已退票游客
		sb.append("where t.orderId = air_o.id and air_o.id = ?  and t.isAirticketflag <> '0'  and   t.order_Type = ? and t.delFlag = '" + Context.DEL_FLAG_NORMAL + "'");
		return this.findBySql(sb.toString(),Map.class,orderId,7); 
	}
	/**
	 * 查询该订单下的游客币种信息
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryTravelerCurrency(String orderId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select t.id,ma.amount,ma.moneyType,c.currency_name,t.name,c.currency_id,ma.serialNum,t.original_payPriceSerialNum ");
		sb.append("from money_amount ma,currency c, traveler t ");
		sb.append("where (ma.serialNum = t.payPriceSerialNum )and ma.currencyId = c.currency_id ");
		sb.append(" and t.orderId = ? and  t.order_Type = ? and ma.moneyType <> ?");
		return this.findBySql(sb.toString(),Map.class, orderId,7,Context.MONEY_TYPE_SF); 
	}
	/**
	 * 查询订单定金
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryFrontMoney(String orderId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select m.amount,air_o.order_no,c.currency_name ,m.moneyType,c.currency_id ");
		sb.append("from airticket_order air_o ,money_amount m,currency c  ");
		sb.append("where (air_o.original_front_money =m.serialNum  or air_o.front_money = m.serialNum) ");
		sb.append("and m.currencyId = c.currency_id and air_o.id = ?  ");
		return this.findBySql(sb.toString(),Map.class, orderId); 
	}
	/**
	 * 查询原始应收价
	 * @param orderId
	 * @param serialNum
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryOriginalMoney(String orderId,
			String serialNum) {
			StringBuffer sb = new StringBuffer();
			sb.append("select  ma.amount amount ");
			sb.append("from money_amount ma,currency c, traveler t ");
			sb.append("where (ma.serialNum = t.original_payPriceSerialNum  )and ma.currencyId = c.currency_id  ");
			sb.append("and t.orderId = ? and ma.serialNum = ?  and  t.order_Type = ?  ");
		return this.findBySql(sb.toString(),Map.class,orderId,serialNum,7);
	}
	/**
	 * 查询订单的当前应收金额
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryOrderReceivable(String orderId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select m.amount,air_o.order_no,c.currency_name ,m.moneyType,c.currency_id,m.serialnum ");
		sb.append(" from airticket_order air_o ,money_amount m,currency c  ");
		sb.append("where (air_o.total_money =m.serialNum ) and m.currencyId = c.currency_id and air_o.id = ?  ");
		return findBySql(sb.toString(),Map.class, orderId);
	}
	/**
	 * 查询改价申请的流程
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public List<Map> queryReviewDetials(String orderId, String productType,
			String flowType) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("select r.id,rd.mykey,rd.myvalue from review  r  ,review_detail rd ");
		sbuffer.append("where r.flowType = ? and r.productType = ? and  r.active = 1  and rd.review_id = r.id and r.orderid = ? and r.`status`  =?");
		List<Map> templist = new ArrayList<Map>();  
		templist = findBySql(sbuffer.toString(), Map.class,flowType,productType,orderId,1);
		return templist; 
	}
	
	
}
