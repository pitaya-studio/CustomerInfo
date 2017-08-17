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
import com.trekiz.admin.modules.pay.dao.PayPosDao;
import com.trekiz.admin.modules.pay.entity.PayPos;
import com.trekiz.admin.modules.pay.input.PayPosInput;
import com.trekiz.admin.modules.pay.query.PayPosQuery;
import com.trekiz.admin.modules.pay.service.PayPosService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PayPosServiceImpl  extends BaseService implements PayPosService{
	@Autowired
	private PayPosDao payPosDao;

	public void save (PayPos payPos){
		super.setOptInfo(payPos, BaseService.OPERATION_ADD);
		payPosDao.saveObj(payPos);
	}
	
	public void save (PayPosInput payPosInput){
		PayPos payPos = payPosInput.getPayPos();
		super.setOptInfo(payPos, BaseService.OPERATION_ADD);
		payPosDao.saveObj(payPos);
	}
	
	public void update (PayPos payPos){
		super.setOptInfo(payPos, BaseService.OPERATION_UPDATE);
		payPosDao.updateObj(payPos);
	}
	
	public PayPos getById(java.lang.Integer value) {
		return payPosDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PayPos obj = payPosDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PayPos> find(Page<PayPos> page, PayPosQuery payPosQuery) {
		DetachedCriteria dc = payPosDao.createDetachedCriteria();
		
	   	if(payPosQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payPosQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payPosQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payPosQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(payPosQuery.getReceiveCompanyName())){
			dc.add(Restrictions.eq("receiveCompanyName", payPosQuery.getReceiveCompanyName()));
		}
		if (StringUtils.isNotEmpty(payPosQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payPosQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payPosQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payPosQuery.getReceiveAccount()));
		}
	   	if(payPosQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payPosQuery.getCreateBy()));
	   	}
		if(payPosQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payPosQuery.getCreateDate()));
		}
	   	if(payPosQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payPosQuery.getUpdateBy()));
	   	}
		if(payPosQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payPosQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payPosQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payPosQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payPosDao.find(page, dc);
	}
	
	public List<PayPos> find( PayPosQuery payPosQuery) {
		DetachedCriteria dc = payPosDao.createDetachedCriteria();
		
	   	if(payPosQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payPosQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payPosQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payPosQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(payPosQuery.getReceiveCompanyName())){
			dc.add(Restrictions.eq("receiveCompanyName", payPosQuery.getReceiveCompanyName()));
		}
		if (StringUtils.isNotEmpty(payPosQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payPosQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payPosQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payPosQuery.getReceiveAccount()));
		}
	   	if(payPosQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payPosQuery.getCreateBy()));
	   	}
		if(payPosQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payPosQuery.getCreateDate()));
		}
	   	if(payPosQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payPosQuery.getUpdateBy()));
	   	}
		if(payPosQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payPosQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payPosQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payPosQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payPosDao.find(dc);
	}
	
	public PayPos getByUuid(String uuid) {
		return payPosDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PayPos payPos = getByUuid(uuid);
		payPos.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(payPos);
	}
}
