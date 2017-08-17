package com.trekiz.admin.modules.order.service;

import java.util.Map;

import com.trekiz.admin.modules.order.entity.OrderServiceCharge;

/**
 * 订单服务费的Service接口
 */
public interface IOrderServiceChargeService {

    /**
     * 保存订单服务费对象
     * @param orderServiceCharge    订单服务费对象
     * @return  持久化之后的订单服务费对象
     * @author  shijun.liu
     * @date    2016.09.01
     */
    public OrderServiceCharge save(OrderServiceCharge orderServiceCharge);
    
    /**
     * 服务费付款确认
     * @param orderId 订单id
     * @param type 服务费类型：0为quauq服务费；1为总社或集团服务费
     * @param confirmDate 出纳确认日期
     * @author yang.wang
     * @date 2016.09.01
     * */
    public Map<String, Object> paymentConfirm(Long orderId, Integer type, String confirmDate);
    
    /**
     * 撤销服务费付款确认
     * @param orderId 订单id
     * @param type 服务费类型：0为quauq服务费；1为总社或集团服务费
     * @author yang.wang
     * @date 2016.09.02
     * */
    public Map<String, Object> cancelServiceChargePaymentConfirm(Long orderId, Integer type);
    
    /**
     * 更新打印时间
     * @param orderId 订单id
     * @param type 服务费类型
     * @author yang.wang
     * @date 2016.9.2
     * */
    public Map<String, Object> updatePrintDate(Long orderId, Integer type);
    
    /**
     * 判断服务费率是否为已付款
     * @param orderId 订单id
     * @param type 服务费类型
     * @author yang.wang
     * @date 2016.9.18
     * */
    public boolean isPayedOrderServiceCharge(Long orderId, Integer type);
}
