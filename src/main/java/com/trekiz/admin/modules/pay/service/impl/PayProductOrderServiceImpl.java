package com.trekiz.admin.modules.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.pay.service.PayHotelOrderService;
import com.trekiz.admin.modules.pay.service.PayIslandOrderService;
import com.trekiz.admin.modules.pay.service.PayProductOrderService;
import com.trekiz.admin.modules.pay.service.ProductMoneyAmountService;
import com.trekiz.admin.modules.sys.service.DocInfoService;


@Service
@Transactional(readOnly = true)
public class PayProductOrderServiceImpl extends BaseService implements PayProductOrderService{
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private ProductMoneyAmountService productMoneyAmountService;
	@Autowired
	private PayIslandOrderService payIslandOrderService;
	@Autowired
	private PayHotelOrderService payHotelOrderService;
	 /**
     * 通过订单支付表单保存支付表（产品订单）信息
    *<p>Title: saveByOrderPayForm</p>
    * @return void 返回类型
    * @author majiancheng
    * @date 2015-6-16 上午10:11:41
    * @throws
     */
	public boolean saveByOrderPayForm(OrderPayForm orderPayForm, int orderType) throws Exception {
		if(Context.ORDER_TYPE_ISLAND == orderType) {
			payIslandOrderService.saveByOrderPayForm(orderPayForm);
		} else if(Context.ORDER_TYPE_HOTEL == orderType) {
			
		}
		return false;
	}
	
	/**
	 * 更新支付表（产品）的达帐状态
	*<p>Title: updateAccountStatus</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 上午11:26:30
	* @throws
	 */
	public boolean updateAccountStatus(int orderType, String payUuid, int accountStatus) {
		if(Context.ORDER_TYPE_ISLAND == orderType) {
			return payIslandOrderService.updateAccountStatus(payUuid, accountStatus);
		} else if(Context.ORDER_TYPE_HOTEL == orderType) {
			return payHotelOrderService.updateAccountStatus(payUuid, accountStatus);
		}
		return false;
	}

}
