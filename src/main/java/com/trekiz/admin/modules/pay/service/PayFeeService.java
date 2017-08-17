/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.pay.entity.PayFee;
import com.trekiz.admin.modules.pay.input.PayFeeInput;
import com.trekiz.admin.modules.pay.query.PayFeeQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface PayFeeService{
	
	public void save (PayFee payFee);
	
	public void save (PayFeeInput payFeeInput);
	
	public void update (PayFee payFee);
	
	public PayFee getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PayFee> find(Page<PayFee> page, PayFeeQuery payFeeQuery);
	
	public List<PayFee> find( PayFeeQuery payFeeQuery);
	
	public PayFee getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据支付信息id获取支付手续费信息
	 * @Description: 
	 * @param @param refundId
	 * @param @return   
	 * @return List<PayFee>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-7
	 */
	public List<PayFee> findByRefundId(String refundId);
}
