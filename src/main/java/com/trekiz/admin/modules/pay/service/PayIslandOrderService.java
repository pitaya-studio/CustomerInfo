/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.pay.entity.PayIslandOrder;
import com.trekiz.admin.modules.pay.input.PayIslandOrderInput;
import com.trekiz.admin.modules.pay.query.PayIslandOrderQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface PayIslandOrderService {
	
	public void save (PayIslandOrder payIslandOrder);
	
	public void save (PayIslandOrderInput payIslandOrderInput);
	
	public void update (PayIslandOrder payIslandOrder);
	
	public PayIslandOrder getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PayIslandOrder> find(Page<PayIslandOrder> page, PayIslandOrderQuery payIslandOrderQuery);
	
	public List<PayIslandOrder> find( PayIslandOrderQuery payIslandOrderQuery);
	
	public PayIslandOrder getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
     * 根据订单ids查询支付订单
     * @param orderIds
     * @return
     */
    public List<PayIslandOrder> findOrderPayByOrderUuids(List<String> uuids);
    
    public List<PayIslandOrder> findLastDateOrderPay(String orderUuid);
    
    /**
     * 通过订单支付表单保存支付表（海岛游）信息
    *<p>Title: saveByOrderPayForm</p>
    * @return void 返回类型
    * @author majiancheng
    * @date 2015-6-16 上午10:11:41
    * @throws
     */
    public boolean saveByOrderPayForm(OrderPayForm orderPayForm) throws Exception;
    
    /**
	 * 更新支付表（海岛游）的达帐状态
	*<p>Title: updateAccountStatus</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 上午11:26:30
	* @throws
	 */
	public boolean updateAccountStatus(String payUuid, int accountStatus);
	/**
	 * 更新支付表（海岛游）的达帐状态
	*<p>Title: updateAccountStatus</p>
	* @return boolean 返回类型
	* @author haiming.zhao
	* @param  id  主键Id
	* @param  accountStatus
	* @date 2015-6-15 上午11:26:30
	* @throws
	 */
	public boolean updateAccountStatus(int id, int accountStatus);
	
	/**
	 * 海岛游订单收款确认驳回操作
	 * @author haiming.zhao
	 * @param productOrderId 订单Id
	 * @param payId  支付记录Id
	 * @param rejectRadio 是否保持占位 0-保持占位，1-退回占位
	 * @return boolean
	 * */
	public boolean rejectOrder(String productOrderId,String payId,String rejectRadio);
}
