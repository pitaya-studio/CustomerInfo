package com.trekiz.admin.modules.pay.service;

import com.trekiz.admin.modules.order.formBean.OrderPayForm;

public interface PayProductOrderService {
	
	 /**
     * 通过订单支付表单保存支付表（产品订单）信息
    *<p>Title: saveByOrderPayForm</p>
    * @return void 返回类型
    * @author majiancheng
    * @date 2015-6-16 上午10:11:41
    * @throws
     */
	public boolean saveByOrderPayForm(OrderPayForm orderPayForm, int orderType) throws Exception ;
	
	
	/**
	 * 更新支付表（产品）的达帐状态
	*<p>Title: updateAccountStatus</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 上午11:26:30
	* @throws
	 */
	public boolean updateAccountStatus(int orderType, String payUuid, int accountStatus);

}
