package com.trekiz.admin.modules.visa.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
/**
 * 订单-签证-申请改价
 * @author HPT
 *
 */
@Repository
public class VisaUpPricesDaoImpl  extends BaseDaoImpl<Map<String,Object>> implements VisaUpPricesDao {

	@Override
	public List<Map<String, Object>> queryVisaUpPricesList(String flowId,
			String orderId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 查询该订单下的游客列表
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryTravelerUpPrices(String orderId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select t.id,t.orderId,t.`name`,t.payPriceSerialNum,t.original_payPriceSerialNum  ");
		sb.append("from traveler t,visa_order vo ");
		sb.append("where t.orderId = vo.id and vo.id = ?  and  t.order_Type = ? and t.delFlag = '" + Context.DEL_FLAG_NORMAL + "'");
		return findBySql(sb.toString(),Map.class,orderId,6); 
	} 
	/**
	 * 查询该订单下的游客币种信息
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryTravelerCurrency(String orderId) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("select t.id,ma.amount,ma.moneyType,c.currency_name,t.name,c.currency_id,ma.serialNum,t.original_payPriceSerialNum ");
		sbuffer.append("from money_amount ma,currency c, traveler t ");
		sbuffer.append("where (ma.serialNum = t.payPriceSerialNum )and ma.currencyId = c.currency_id  ");
		sbuffer.append(" and t.orderId = ? and  t.order_Type = ?");
		
		return findBySql(sbuffer.toString(), Map.class,orderId,6); 
	}
	/**
	 *  查询团队签证费
	 * @param orderId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryVisaMoney(String orderId) {
		StringBuffer sb = new StringBuffer();
//		sb.append("select m.amount,vo.order_no,c.currency_name ,m.moneyType,c.currency_id  ");
//		sb.append("from visa_order vo ,money_amount m,currency c  ");
//		sb.append("where (vo.total_money =m.serialNum  or vo.original_total_money = m.serialNum) ");
//		sb.append("and m.currencyId = c.currency_id and vo.id = ?  ");
		sb.append("SELECT t1.id,t1.amount curtotalmoney ,t2.amount oldtotalmoney ,t1.currencyId currencyid,t1.currency_name currencyname FROM");
		sb.append("(SELECT  a.id,m.amount,m.currencyId,c.currency_name,m.moneyType from visa_order a,money_amount m ,currency c where a.total_money = m.serialNum and a.id = ?");
		sb.append("and m.currencyId = c.currency_id )t1,");
		sb.append("(SELECT  a.id,m.amount,m.currencyId,c.currency_name,m.moneyType from visa_order a,money_amount m ,currency c where a.original_total_money = m.serialNum and a.id = ?");
		sb.append("and m.currencyId = c.currency_id )t2 WHERE t1.id=t2.id and t1.currencyId =t2.currencyId and t1.currency_name = t2.currency_name ");
		return this.findBySql(sb.toString(),Map.class, orderId,orderId); 
	}

	@Override
	public List<Map<String, Object>> queryOriginalMoney(String orderId,
			String serialNum) {
		StringBuffer sb = new StringBuffer();
		sb.append("select  ma.amount amount ");
		sb.append("from money_amount ma,currency c, traveler t ");
		sb.append("where (ma.serialNum = t.original_payPriceSerialNum  )and ma.currencyId = c.currency_id  ");
		sb.append("and t.orderId = ? and ma.serialNum = ?  and  t.order_Type = ? ");
		return this.findBySql(sb.toString(),Map.class,orderId,serialNum,6);
	}

	@Override
	public List<Map<String, Object>> queryOrderReceivable(String orderId) {
		// TODO Auto-generated method stub
		return null;
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
