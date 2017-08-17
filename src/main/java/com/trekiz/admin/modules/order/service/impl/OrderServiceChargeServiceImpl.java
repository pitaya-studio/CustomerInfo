package com.trekiz.admin.modules.order.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.order.entity.OrderServiceCharge;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.repository.OrderServiceChargeDao;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.order.service.IOrderServiceChargeService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 订单服务费信息操作服务实现类
 * @author  shijun.liu
 * @date    2016.09.01
 */
@Service
public class OrderServiceChargeServiceImpl implements IOrderServiceChargeService{

    @Autowired
    private OrderServiceChargeDao orderServiceChargeDao;
    @Autowired
    private RefundDao refundDao;

    @Override
    public OrderServiceCharge save(OrderServiceCharge orderServiceCharge){
        if(null == orderServiceCharge){
            throw new RuntimeException("订单服务费对象不能为空");
        }
        if (orderServiceCharge.getId() == null) {
        	orderServiceCharge.setCreateBy(UserUtils.getUser().getId());
            orderServiceCharge.setCreateDate(new Date());
        }
        orderServiceCharge.setUpdateBy(UserUtils.getUser().getId());
        orderServiceCharge.setUpdateDate(new Date());
        return orderServiceChargeDao.save(orderServiceCharge);
    }

	@Override
	public Map<String, Object> paymentConfirm(Long orderId, Integer type, String confirmDate) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<OrderServiceCharge> orderServiceChargeList = orderServiceChargeDao.findByOrderIdAndType(orderId, type);
		if (orderServiceChargeList == null || orderServiceChargeList.size() == 0) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		
		try {
			OrderServiceCharge orderServiceCharge = orderServiceChargeList.get(0);//获取服务费付款对象
			orderServiceCharge.setPayStatus(1);//修改付款状态
			orderServiceCharge.setPayTime(new SimpleDateFormat("yyyy-MM-dd").parse(confirmDate));//修改付款时间
			orderServiceCharge.setUpdateBy(UserUtils.getUser().getId());//修改人
			orderServiceCharge.setUpdateDate(new Date());//修改时间
			orderServiceChargeDao.save(orderServiceCharge);
		} catch (ParseException e) {
			map.put("flag", false);
			map.put("msg", "出纳确认时间格式错误！");
			e.printStackTrace();
			return map;
		} catch (Exception e) {
			map.put("flag", false);
			map.put("msg", "付款确认失败！");
			e.printStackTrace();
			return map;
		}
		
		map.put("flag", true);
		return map;
	}

	@Override
	public Map<String, Object> cancelServiceChargePaymentConfirm(Long orderId, Integer type) {
		
		Map<String, Object> map = new HashMap<>();
		
		if (orderId <= 0 || (type != 0 && type != 1)) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		List<OrderServiceCharge> list = orderServiceChargeDao.findByOrderIdAndType(orderId, type);
		if (list == null || list.size() == 0) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		
		try {
			OrderServiceCharge orderServiceCharge = list.get(0);
			orderServiceCharge.setPayStatus(0);//付款状态
			orderServiceCharge.setPayTime(null);//出纳确认时间
			orderServiceCharge.setUpdateBy(UserUtils.getUser().getId());//修改人
			orderServiceCharge.setUpdateDate(new Date());//修改时间
			orderServiceChargeDao.save(orderServiceCharge);
		} catch (Exception e) {
			map.put("flag", false);
			map.put("msg", "撤销失败！");
			e.printStackTrace();
			return map;
		}
		
		map.put("flag", true);
		return map;
	}

	@Override
	public Map<String, Object> updatePrintDate(Long orderId, Integer type) {

		Map<String, Object> map = new HashMap<>();
		if (orderId <= 0 || (type != 0 && type != 1)) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		
		List<OrderServiceCharge> list = orderServiceChargeDao.findByOrderIdAndType(orderId, type);
		if (list == null || list.size() == 0) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		
		try {
			OrderServiceCharge orderServiceCharge = list.get(0);
			if (orderServiceCharge.getPrintStatus() == 0) {
				orderServiceCharge.setPrintStatus(1);//打印状态
				orderServiceCharge.setPrintTime(new Date());//首次打印时间
				orderServiceCharge.setUpdateBy(UserUtils.getUser().getId());//修改人
				orderServiceCharge.setUpdateDate(new Date());//修改时间
				orderServiceChargeDao.save(orderServiceCharge);
			}
		} catch (Exception e) {
			map.put("flag", false);
			map.put("msg", "更新失败！");
			e.printStackTrace();
			return map;
		}
		
		map.put("flag", true);
		return map;
	}

	@Override
	public boolean isPayedOrderServiceCharge(Long orderId, Integer type) {

		List<OrderServiceCharge> osclist = orderServiceChargeDao.findByOrderIdAndType(orderId, type);
		if (osclist != null && osclist.size() > 0) {
			OrderServiceCharge osc = osclist.get(0);
			List<Refund> refundList = refundDao.findByRecordId(osc.getId(), osc.getType() + 14);
			
			if (refundList != null && refundList.size() > 0) {
				return true;
			}
		}
		return false;
	}
}
