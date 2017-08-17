/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.pay.dao.PayFeeDao;
import com.trekiz.admin.modules.pay.entity.PayFee;
import com.trekiz.admin.modules.pay.input.PayFeeInput;
import com.trekiz.admin.modules.pay.query.PayFeeQuery;
import com.trekiz.admin.modules.pay.service.PayFeeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PayFeeServiceImpl  extends BaseService implements PayFeeService{
	@Autowired
	private PayFeeDao payFeeDao;

	public void save (PayFee payFee){
		super.setOptInfo(payFee, BaseService.OPERATION_ADD);
		payFeeDao.saveObj(payFee);
	}
	
	public void save (PayFeeInput payFeeInput){
		PayFee payFee = payFeeInput.getPayFee();
		super.setOptInfo(payFee, BaseService.OPERATION_ADD);
		payFeeDao.saveObj(payFee);
	}
	
	public void update (PayFee payFee){
		super.setOptInfo(payFee, BaseService.OPERATION_UPDATE);
		payFeeDao.updateObj(payFee);
	}
	
	public PayFee getById(java.lang.Integer value) {
		return payFeeDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PayFee obj = payFeeDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PayFee> find(Page<PayFee> page, PayFeeQuery payFeeQuery) {
		DetachedCriteria dc = payFeeDao.createDetachedCriteria();
		
	   	if(payFeeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payFeeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payFeeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payFeeQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(payFeeQuery.getRefundId())){
			dc.add(Restrictions.eq("refundId", payFeeQuery.getRefundId()));
		}
		if (StringUtils.isNotEmpty(payFeeQuery.getFeeName())){
			dc.add(Restrictions.eq("feeName", payFeeQuery.getFeeName()));
		}
	   	if(payFeeQuery.getFeeCurrencyId()!=null){
	   		dc.add(Restrictions.eq("feeCurrencyId", payFeeQuery.getFeeCurrencyId()));
	   	}
	   	if(payFeeQuery.getFeeAmount()!=null){
	   		dc.add(Restrictions.eq("feeAmount", payFeeQuery.getFeeAmount()));
	   	}
	   	if(payFeeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payFeeQuery.getCreateBy()));
	   	}
		if(payFeeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payFeeQuery.getCreateDate()));
		}
	   	if(payFeeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payFeeQuery.getUpdateBy()));
	   	}
		if(payFeeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payFeeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payFeeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payFeeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payFeeDao.find(page, dc);
	}
	
	public List<PayFee> find( PayFeeQuery payFeeQuery) {
		DetachedCriteria dc = payFeeDao.createDetachedCriteria();
		
	   	if(payFeeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payFeeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payFeeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payFeeQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(payFeeQuery.getRefundId())){
			dc.add(Restrictions.eq("refundId", payFeeQuery.getRefundId()));
		}
		if (StringUtils.isNotEmpty(payFeeQuery.getFeeName())){
			dc.add(Restrictions.eq("feeName", payFeeQuery.getFeeName()));
		}
	   	if(payFeeQuery.getFeeCurrencyId()!=null){
	   		dc.add(Restrictions.eq("feeCurrencyId", payFeeQuery.getFeeCurrencyId()));
	   	}
	   	if(payFeeQuery.getFeeAmount()!=null){
	   		dc.add(Restrictions.eq("feeAmount", payFeeQuery.getFeeAmount()));
	   	}
	   	if(payFeeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payFeeQuery.getCreateBy()));
	   	}
		if(payFeeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payFeeQuery.getCreateDate()));
		}
	   	if(payFeeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payFeeQuery.getUpdateBy()));
	   	}
		if(payFeeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payFeeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payFeeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payFeeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payFeeDao.find(dc);
	}
	
	public PayFee getByUuid(String uuid) {
		return payFeeDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PayFee payFee = getByUuid(uuid);
		payFee.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(payFee);
	}
	
	public boolean batchDelete(String[] uuids) {
		return payFeeDao.batchDelete(uuids);
	}
	
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
	public List<PayFee> findByRefundId(String refundId) {
		return payFeeDao.findByRefundId(refundId);
	}
	
}
