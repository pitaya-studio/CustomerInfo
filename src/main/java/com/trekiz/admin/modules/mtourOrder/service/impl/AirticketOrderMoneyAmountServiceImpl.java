/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderMoneyAmountDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderMoneyAmount;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderMoneyAmountInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderMoneyAmountQuery;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderMoneyAmountService;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class AirticketOrderMoneyAmountServiceImpl  extends BaseService implements AirticketOrderMoneyAmountService{
	@Autowired
	private AirticketOrderMoneyAmountDao airticketOrderMoneyAmountDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private RefundDao refundDao;
	
	public void save (AirticketOrderMoneyAmount airticketOrderMoneyAmount){
		super.setOptInfo(airticketOrderMoneyAmount, BaseService.OPERATION_ADD);
		airticketOrderMoneyAmountDao.saveObj(airticketOrderMoneyAmount);
	}
	
	public void save (AirticketOrderMoneyAmountInput airticketOrderMoneyAmountInput){
		AirticketOrderMoneyAmount airticketOrderMoneyAmount = airticketOrderMoneyAmountInput.getAirticketOrderMoneyAmount();
		super.setOptInfo(airticketOrderMoneyAmount, BaseService.OPERATION_ADD);
		airticketOrderMoneyAmountDao.saveObj(airticketOrderMoneyAmount);
	}
	
	public void update (AirticketOrderMoneyAmount airticketOrderMoneyAmount){
		super.setOptInfo(airticketOrderMoneyAmount, BaseService.OPERATION_UPDATE);
		airticketOrderMoneyAmountDao.updateObj(airticketOrderMoneyAmount);
	}
	
	public AirticketOrderMoneyAmount getById(java.lang.Integer value) {
		return airticketOrderMoneyAmountDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		AirticketOrderMoneyAmount obj = airticketOrderMoneyAmountDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<AirticketOrderMoneyAmount> find(Page<AirticketOrderMoneyAmount> page, AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery) {
		DetachedCriteria dc = airticketOrderMoneyAmountDao.createDetachedCriteria();
		
	   	if(airticketOrderMoneyAmountQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderMoneyAmountQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderMoneyAmountQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getSerialNum())){
			dc.add(Restrictions.eq("serialNum", airticketOrderMoneyAmountQuery.getSerialNum()));
		}
	   	if(airticketOrderMoneyAmountQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", airticketOrderMoneyAmountQuery.getCurrencyId()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", airticketOrderMoneyAmountQuery.getAmount()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getExchangerate()!=null){
	   		dc.add(Restrictions.eq("exchangerate", airticketOrderMoneyAmountQuery.getExchangerate()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderMoneyAmountQuery.getAirticketOrderId()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getMoneyType()!=null){
	   		dc.add(Restrictions.eq("moneyType", airticketOrderMoneyAmountQuery.getMoneyType()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getFundsName())){
			dc.add(Restrictions.eq("fundsName", airticketOrderMoneyAmountQuery.getFundsName()));
		}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getMemo())){
			dc.add(Restrictions.eq("memo", airticketOrderMoneyAmountQuery.getMemo()));
		}
	   	if(airticketOrderMoneyAmountQuery.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", airticketOrderMoneyAmountQuery.getStatus()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderMoneyAmountQuery.getCreateBy()));
	   	}
		if(airticketOrderMoneyAmountQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderMoneyAmountQuery.getCreateDate()));
		}
	   	if(airticketOrderMoneyAmountQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderMoneyAmountQuery.getUpdateBy()));
	   	}
		if(airticketOrderMoneyAmountQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderMoneyAmountQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderMoneyAmountQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderMoneyAmountDao.find(page, dc);
	}
	
	public List<AirticketOrderMoneyAmount> find( AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery) {
		DetachedCriteria dc = airticketOrderMoneyAmountDao.createDetachedCriteria();
		
	   	if(airticketOrderMoneyAmountQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", airticketOrderMoneyAmountQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", airticketOrderMoneyAmountQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getSerialNum())){
			dc.add(Restrictions.eq("serialNum", airticketOrderMoneyAmountQuery.getSerialNum()));
		}
	   	if(airticketOrderMoneyAmountQuery.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", airticketOrderMoneyAmountQuery.getCurrencyId()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getAmount()!=null){
	   		dc.add(Restrictions.eq("amount", airticketOrderMoneyAmountQuery.getAmount()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getExchangerate()!=null){
	   		dc.add(Restrictions.eq("exchangerate", airticketOrderMoneyAmountQuery.getExchangerate()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getAirticketOrderId()!=null){
	   		dc.add(Restrictions.eq("airticketOrderId", airticketOrderMoneyAmountQuery.getAirticketOrderId()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getMoneyType()!=null){
	   		dc.add(Restrictions.eq("moneyType", airticketOrderMoneyAmountQuery.getMoneyType()));
	   	}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getFundsName())){
			dc.add(Restrictions.eq("fundsName", airticketOrderMoneyAmountQuery.getFundsName()));
		}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getMemo())){
			dc.add(Restrictions.eq("memo", airticketOrderMoneyAmountQuery.getMemo()));
		}
	   	if(airticketOrderMoneyAmountQuery.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", airticketOrderMoneyAmountQuery.getStatus()));
	   	}
	   	if(airticketOrderMoneyAmountQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", airticketOrderMoneyAmountQuery.getCreateBy()));
	   	}
		if(airticketOrderMoneyAmountQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", airticketOrderMoneyAmountQuery.getCreateDate()));
		}
	   	if(airticketOrderMoneyAmountQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", airticketOrderMoneyAmountQuery.getUpdateBy()));
	   	}
		if(airticketOrderMoneyAmountQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", airticketOrderMoneyAmountQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(airticketOrderMoneyAmountQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", airticketOrderMoneyAmountQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return airticketOrderMoneyAmountDao.find(dc);
	}
	
	public AirticketOrderMoneyAmount getByUuid(String uuid) {
		return airticketOrderMoneyAmountDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		AirticketOrderMoneyAmount airticketOrderMoneyAmount = getByUuid(uuid);
		airticketOrderMoneyAmount.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(airticketOrderMoneyAmount);
	}
	public List<Map<String,Object>> queryAirticketOrderMoneyAmount(AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery){
		String sql = "select a.id, a.airticket_order_id as orderUuid,a.moneyType as fundsType,a.funds_name as fundsName,a.uuid,date_format(a.createDate,'%Y-%m-%d') as applicationDate,a.currency_id as currencyUuid,a.exchangeRate as exchangeRate,a.amount as amount,u.name as applicant,a.status as stateCode,a.memo as memo,CASE WHEN a.status = 1 then '已提交' ELSE '已撤销' end AS state from airticket_order_moneyAmount a ,sys_user u where 1 = 1 and a.createBy = u.id and a.airticket_order_id = ? and a.moneyType = ?  order by a.createDate desc";
		List<Map<String,Object>> list = airticketOrderMoneyAmountDao.findBySql(sql, Map.class, airticketOrderMoneyAmountQuery.getAirticketOrderId(),airticketOrderMoneyAmountQuery.getMoneyType());
		/**
		 * update by wangXK 增加状态： 已付款 如果在refund中已经有付款
		 * refund moneyType :'款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款；5：退签证押金；6：追加成本付款）',
		 */
		String companyId = UserUtils.getUser().getCompany().getUuid();
		if(CollectionUtils.isNotEmpty(list)&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> tempMap = list.get(i);
				//Object stateCode = tempMap.get("stateCode");
				Object moneyType = tempMap.get("fundsType");
				//Object state = tempMap.get("stateCode");
				Integer refundMoneyType=0;
				if(StringUtils.isNotBlank(moneyType.toString())){
					if("3".equals(moneyType.toString())){
						refundMoneyType = 6;
					}else if("2".equals(moneyType.toString())){
						refundMoneyType = 2;
					}else if("1".equals(moneyType.toString())){
						refundMoneyType = 4;
					}
				}
				
				//对于美途机票快速订单金额表每一笔支付记录，都查询refund中是否有 有效的支付记录，有，则是已付款，状态码为2
				List<Refund> refundList = refundDao.findRefund(Long.valueOf(tempMap.get("id").toString()), refundMoneyType,companyId);
				if(CollectionUtils.isNotEmpty(refundList) && refundList.size()>0){
					tempMap.put("stateCode", "2");
					tempMap.put("state", "已付款");
				}
			}
		}
		return list;
	}
	
	public void batchSave(List<AirticketOrderMoneyAmount> list){
		airticketOrderMoneyAmountDao.batchUpdate(list);
	}
	
	/**
	 * 保存美途机票快速订单金额表（目前只用于美途国际的付款）
	 * @Title: saveMoneyAmount
	 * @return boolean
	 * @author majiancheng
	 * @date 2015-10-24 下午2:09:30
	 */
	public boolean saveMoneyAmount(String serialNum, String[] currencyIds, String[] amounts, String[] convertLowests) {
		boolean flag = false;
		if(currencyIds == null || amounts == null || (currencyIds.length != amounts.length)) {
			return false;
		}
		
		try{
			List<AirticketOrderMoneyAmount> moneyAmounts = new ArrayList<AirticketOrderMoneyAmount>();
			for(int i=0; i<currencyIds.length; i++) {
				AirticketOrderMoneyAmount moneyAmount = new AirticketOrderMoneyAmount();
				moneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
				moneyAmount.setAmount(Double.parseDouble(amounts[i]));
				moneyAmount.setSerialNum(serialNum);
				//设置默认状态
				moneyAmount.setStatus(1);
				//读取当前币种的汇率  (edit by majiancheng 2015-11-20该币种现在取订单中的币种信息)
				/*BigDecimal exchangerate = currencyDao.findExchangerageById(Long.parseLong(currencyIds[i]));*/
				moneyAmount.setExchangerate(Double.parseDouble(convertLowests[i]));
				super.setOptInfo(moneyAmount, OPERATION_ADD);
				moneyAmounts.add(moneyAmount);
			}
			
			airticketOrderMoneyAmountDao.batchSave(moneyAmounts);
			flag = true;
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return flag;
	}
	
	/**
	 * 删除操作
	 * @param uuid
	 */
	public void deleteByOrderId(Long id){
		String sql = "delete from airticket_order_moneyAmount where airticket_order_id = "+id;
		airticketOrderMoneyAmountDao.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	
	/**
	 * 根据付款集合信息获取(currencyId,currency_name,currency_mark,sum(amount),exchangerate)集合
	 * @Title: getRMBPriceByRefunds
	 * @return BigDecimal
	 * @author majiancheng
	 * @date 2015-11-2 下午11:01:06
	 */
	public List<Object[]> getMoneyAmountsByRefunds(List<Refund> refunds) {
		if(CollectionUtils.isEmpty(refunds)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(refunds)) {
			for(Refund refund : refunds) {
				sb.append("'");
				sb.append(refund.getMoneySerialNum());
				sb.append("'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		StringBuffer str = new StringBuffer();
		str.append(" SELECT m.currency_id,c.currency_name,c.currency_mark,sum(m.amount),m.exchangerate ")
		   .append(" from airticket_order_moneyAmount m,currency c where m.currency_id=c.currency_id ")
		   .append(" and m.serialNum in (").append(sb.toString()).append(") GROUP BY m.currency_id ")
		   .append(" ORDER BY m.currency_id ");
		List<Object[]> moneyAmountInfos = airticketOrderMoneyAmountDao.findBySql(str.toString());
		return moneyAmountInfos;
	}
	
	/**
	 * 根据付款信息获取(currencyId,currency_name,currency_mark,sum(amount),exchangerate)集合
	 * @Title: getRMBPriceByRefunds
	 * @return BigDecimal
	 * @author majiancheng
	 * @date 2015-11-2 下午11:01:06
	 */
	public List<Object[]> getMoneyAmountsByRefund(Refund refund) {
		List<Refund> refunds = new ArrayList<Refund>();
		refunds.add(refund);
		return getMoneyAmountsByRefunds(refunds);
	}
	
	public Double queryAirticketOrderMoneyAmountTotal(Integer orderid,Integer moneytype){
		String sql = "select airticket_order_id,moneyType,funds_name,uuid,currency_id as currencyUuid,exchangeRate,amount,status,memo from airticket_order_moneyAmount where 1 = 1 and status = 1 and airticket_order_id = ? and moneyType = ?";
		List<Map<String,Object>> list = airticketOrderMoneyAmountDao.findBySql(sql, Map.class, orderid,moneytype);
		BigDecimal total = new BigDecimal(0);
		if(CollectionUtils.isNotEmpty(list)&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> tempMap = list.get(i);
				Object amount = tempMap.get("amount");
				Object exchangeRate = tempMap.get("exchangeRate");
				if(amount != null && exchangeRate != null){
					total = total.add(BigDecimal.valueOf(Double.valueOf(amount.toString())).multiply(BigDecimal.valueOf(Double.valueOf(exchangeRate.toString()))));
				}
			}
		}
		return Double.valueOf(total.toString());
	}
	public List<Map<String, Object>> queryAirticketOrderMoneyAmountList(AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery){
		String sql = "select currency_id as currencyUuid,exchangeRate,amount from airticket_order_moneyAmount where 1 = 1 and status = 1 and airticket_order_id = ? and moneyType = ?";
		List<Map<String,Object>> list = airticketOrderMoneyAmountDao.findBySql(sql, Map.class, airticketOrderMoneyAmountQuery.getAirticketOrderId(),airticketOrderMoneyAmountQuery.getMoneyType());
		return list;
	}
}
