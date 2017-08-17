/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderMoneyAmount;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderMoneyAmountInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderMoneyAmountQuery;
import com.trekiz.admin.modules.order.entity.Refund;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketOrderMoneyAmountService{
	
	public void save (AirticketOrderMoneyAmount airticketOrderMoneyAmount);
	
	public void save (AirticketOrderMoneyAmountInput airticketOrderMoneyAmountInput);
	
	public void update (AirticketOrderMoneyAmount airticketOrderMoneyAmount);
	
	public AirticketOrderMoneyAmount getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketOrderMoneyAmount> find(Page<AirticketOrderMoneyAmount> page, AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery);
	
	public List<AirticketOrderMoneyAmount> find( AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery);
	
	public AirticketOrderMoneyAmount getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<Map<String, Object>> queryAirticketOrderMoneyAmount(AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery);
	/**
	 * 美途机票批量保存 借款/退款/追加成本记录---撤销   退款:2,借款:3,追加成本:4
	 * @param list
	 */
	public void batchSave(List<AirticketOrderMoneyAmount> list);
	
	/**
	 * 保存美途机票快速订单金额表（目前只用于美途国际的付款）
		 * @Title: saveMoneyAmount
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-24 下午2:09:30
	 */
	public boolean saveMoneyAmount(String serialNum, String[] currencyIds, String[] amounts, String[] convertLowests);
	
	/**
	 * 删除操作
	 * @param uuid
	 */
	public void deleteByOrderId(Long id);
	
	
	/**
	 * 根据付款集合信息获取,返回字段信息集合(currencyId,currency_name,currency_mark,sum(amount),exchangerate)
		 * @Title: getRMBPriceByRefunds
	     * @return BigDecimal
	     * @author majiancheng       
	     * @date 2015-11-2 下午11:01:06
	 */
	public List<Object[]> getMoneyAmountsByRefunds(List<Refund> refunds);
	
	
	/**
	 * 根据付款信息获取(currencyId,currency_name,currency_mark,sum(amount),exchangerate)集合
		 * @Title: getRMBPriceByRefunds
	     * @return BigDecimal
	     * @author majiancheng       
	     * @date 2015-11-2 下午11:01:06
	 */
	public List<Object[]> getMoneyAmountsByRefund(Refund refund);
	/**
	 * 统计的是已提交的数据，已撤销的不再统计之列
	 * 根据订单ID和款项类型（退款、借款、追加成本）返回指定的金额总计
	 * @param orderid
	 * @param type
	 * @return
	 */
	public Double queryAirticketOrderMoneyAmountTotal(Integer orderid,Integer moneytype);
	/**
	 * 统计的是已提交的数据，已撤销的不再统计之列
	 * 根据订单ID和款项类型（退款、借款、追加成本）返回指定的币种Id 金额 和汇率
	 * @param airticketOrderMoneyAmountQuery
	 * @return
	 */
	public List<Map<String, Object>> queryAirticketOrderMoneyAmountList(AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery);
	
}
