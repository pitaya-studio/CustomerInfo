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
import com.trekiz.admin.modules.pay.dao.PayBanktransferDao;
import com.trekiz.admin.modules.pay.entity.PayBanktransfer;
import com.trekiz.admin.modules.pay.input.PayBanktransferInput;
import com.trekiz.admin.modules.pay.query.PayBanktransferQuery;
import com.trekiz.admin.modules.pay.service.PayBanktransferService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PayBanktransferServiceImpl  extends BaseService implements PayBanktransferService{
	@Autowired
	private PayBanktransferDao payBanktransferDao;

	public void save (PayBanktransfer payBanktransfer){
		super.setOptInfo(payBanktransfer, BaseService.OPERATION_ADD);
		payBanktransferDao.saveObj(payBanktransfer);
	}
	
	public void save (PayBanktransferInput payBanktransferInput){
		PayBanktransfer payBanktransfer = payBanktransferInput.getPayBanktransfer();
		super.setOptInfo(payBanktransfer, BaseService.OPERATION_ADD);
		payBanktransferDao.saveObj(payBanktransfer);
	}
	
	public void update (PayBanktransfer payBanktransfer){
		super.setOptInfo(payBanktransfer, BaseService.OPERATION_UPDATE);
		payBanktransferDao.updateObj(payBanktransfer);
	}
	
	public PayBanktransfer getById(java.lang.Integer value) {
		return payBanktransferDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PayBanktransfer obj = payBanktransferDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PayBanktransfer> find(Page<PayBanktransfer> page, PayBanktransferQuery payBanktransferQuery) {
		DetachedCriteria dc = payBanktransferDao.createDetachedCriteria();
		
	   	if(payBanktransferQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payBanktransferQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payBanktransferQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getPayBankName())){
			dc.add(Restrictions.eq("payBankName", payBanktransferQuery.getPayBankName()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getPayAccount())){
			dc.add(Restrictions.eq("payAccount", payBanktransferQuery.getPayAccount()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getReceiveCompanyName())){
			dc.add(Restrictions.eq("receiveCompanyName", payBanktransferQuery.getReceiveCompanyName()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payBanktransferQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payBanktransferQuery.getReceiveAccount()));
		}
	   	if(payBanktransferQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payBanktransferQuery.getCreateBy()));
	   	}
		if(payBanktransferQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payBanktransferQuery.getCreateDate()));
		}
	   	if(payBanktransferQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payBanktransferQuery.getUpdateBy()));
	   	}
		if(payBanktransferQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payBanktransferQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payBanktransferQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payBanktransferDao.find(page, dc);
	}
	
	public List<PayBanktransfer> find( PayBanktransferQuery payBanktransferQuery) {
		DetachedCriteria dc = payBanktransferDao.createDetachedCriteria();
		
	   	if(payBanktransferQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payBanktransferQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payBanktransferQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getPayBankName())){
			dc.add(Restrictions.eq("payBankName", payBanktransferQuery.getPayBankName()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getPayAccount())){
			dc.add(Restrictions.eq("payAccount", payBanktransferQuery.getPayAccount()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getReceiveCompanyName())){
			dc.add(Restrictions.eq("receiveCompanyName", payBanktransferQuery.getReceiveCompanyName()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payBanktransferQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payBanktransferQuery.getReceiveAccount()));
		}
	   	if(payBanktransferQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payBanktransferQuery.getCreateBy()));
	   	}
		if(payBanktransferQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payBanktransferQuery.getCreateDate()));
		}
	   	if(payBanktransferQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payBanktransferQuery.getUpdateBy()));
	   	}
		if(payBanktransferQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payBanktransferQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payBanktransferQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payBanktransferQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payBanktransferDao.find(dc);
	}
	
	public PayBanktransfer getByUuid(String uuid) {
		return payBanktransferDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PayBanktransfer payBanktransfer = getByUuid(uuid);
		payBanktransfer.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(payBanktransfer);
	}
}
