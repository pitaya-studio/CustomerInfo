package com.trekiz.admin.modules.order.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;

@Service
@Transactional(readOnly = true)
public class CruiseOrderService  extends BaseService {
	
	@Autowired
    private ProductOrderCommonDao productorderDao;
	@Autowired
    private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
	
	@Autowired
    private OrderStatisticsService orderStatusticsService;
	
	/**
	 * 游轮订单取消
	 * @param productOrder
	 * @param description
	 * @param request
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException
	 * @throws Exception
	 */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void cancelOrder(ProductOrderCommon productOrder, String description, HttpServletRequest request)
    				throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
    	
        //判断是否占位
        Map<String,String> rMap = orderStatusticsService.getPlaceHolderInfo(productOrder.getId(), productOrder.getOrderStatus().toString());
        if (null != rMap && null != rMap.get(Context.RESULT)) {
        	String resultP = rMap.get(Context.RESULT);
        	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
        		//当订单占位时归还余位
        		Map<String,String> pMap = orderStatusticsService.saveActivityGroupPlaceHolderChange(productOrder.getId(), 
        				productOrder.getOrderStatus().toString(), productOrder.getRoomNumber()
        				, request, 8);
        		//余位处理失败
        		if (null != pMap && Context.ORDER_PLACEHOLDER_ERROR.toString().equals( pMap.get(Context.RESULT))) {
        			throw new Exception(Context.MESSAGE);
        		}
        		//余位处理成功
        		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))) {
        			
        		} else {
        			throw new Exception("归还余位失败！");
        		}
        	} else if (Context.ORDER_PLACEHOLDER_ERROR.toString().equals(resultP)) {
        		throw new Exception(rMap.get(Context.MESSAGE));
        	}

        } else {
        	//添加操作日志
	        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
	        		"订单"+ productOrder.getOrderNum()+"取消失败", "2", productOrder.getOrderStatus(), productOrder.getId());
        	throw new Exception("取消订单失败！");
        }
        productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));
        productOrder.setCancelDescription(description);
        productorderDao.save(productOrder);
        
      //-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为99（取消）---开始---
        orderDateSaveOrUpdateDao.updateOrderStatus(productOrder.getId(), productOrder.getOrderStatus().toString());
        
      //-------by------junhao.zhao-----2017-01-06-----表order_data_statistics中order_status改为99（取消）---结束--- 
        
        //添加操作日志
        this.saveLogOperate(Context.log_type_orderform,
        		Context.log_type_orderform_name, "订单"+ productOrder.getOrderNum()+"取消成", "2", productOrder.getOrderStatus(), productOrder.getId());

    }
    
    /**
     * 已撤销占位
     * @Description 
     * @author yakun.bai
     * @Date 2015-11-16
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public void revokeOrder(ProductOrderCommon productOrder, HttpServletRequest request)
    				throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
    	
        //判断是否占位
        Map<String,String> rMap = orderStatusticsService.getPlaceHolderInfo(productOrder.getId(), productOrder.getOrderStatus().toString());
        if (null != rMap && null != rMap.get(Context.RESULT)) {
        	String resultP = rMap.get(Context.RESULT);
        	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
        		//当订单占位时归还余位
        		Map<String,String> pMap = orderStatusticsService.saveActivityGroupPlaceHolderChange(productOrder.getId(), productOrder.getOrderStatus().toString(), productOrder.getRoomNumber(), request, 0);
        		//余位处理失败
        		if (null != pMap && Context.ORDER_PLACEHOLDER_ERROR.toString().equals( pMap.get(Context.RESULT))) {
        			throw new Exception(Context.MESSAGE);
        		}
        		//余位处理成功
        		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))) {
        			
        		} else {
        			throw new Exception("归还余位失败！");
        		}
        	} else if (Context.ORDER_PLACEHOLDER_ERROR.toString().equals(resultP)) {
        		throw new Exception(rMap.get(Context.MESSAGE));
        	}

        } else {
        	//添加操作日志
	        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, 
	        		"订单" + productOrder.getOrderNum() + "撤销占位失败", "2", productOrder.getOrderStatus(), productOrder.getId());
        	throw new Exception("取消订单失败！");
        }
        productOrder.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_CW_CX));
        productorderDao.save(productOrder);
        //添加操作日志
        this.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
        		"订单" + productOrder.getOrderNum() + "撤销占位成功", "2", productOrder.getOrderStatus(), productOrder.getId());
    }
    
}