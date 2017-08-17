/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

import java.util.*;

import com.trekiz.admin.modules.pay.entity.*;
import com.trekiz.admin.modules.pay.dao.*;
import com.trekiz.admin.modules.pay.service.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PayGroupServiceImpl  extends BaseService implements PayGroupService{
	@Autowired
	private PayGroupDao payGroupDao;

	public void save (PayGroup payGroup){
		User currUser = UserUtils.getUser();
		Date currDate = new Date();

		payGroup.setUuid(UuidUtils.generUuid());
		payGroup.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		payGroup.setCreateBy(currUser.getId().intValue());
		payGroup.setUpdateBy(currUser.getId().intValue());
		payGroup.setCreateDate(currDate);
		payGroup.setUpdateDate(currDate);
		payGroupDao.save(payGroup);
	}
	
	public void update (PayGroup payGroup){
		User currUser = UserUtils.getUser();
		Date currDate = new Date();
		
		payGroup.setUpdateBy(currUser.getId().intValue());
		payGroup.setUpdateDate(currDate);
		payGroupDao.update(payGroup);
	}
	
	public PayGroup getById(java.lang.Integer value) {
		return payGroupDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PayGroup obj = payGroupDao.getById(value);
		obj.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		this.update(obj);
	}	
	
	
	public Page<PayGroup> find(Page<PayGroup> page, PayGroup payGroup) {
		DetachedCriteria dc = payGroupDao.createDetachedCriteria();
		
	   	if(payGroup.getId()!=null){
	   		dc.add(Restrictions.eq("id", payGroup.getId()));
	   	}
		if (StringUtils.isNotEmpty(payGroup.getUuid())){
			dc.add(Restrictions.eq("uuid", payGroup.getUuid()));
		}
	   	if(payGroup.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", payGroup.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(payGroup.getPayTypeName())){
			dc.add(Restrictions.eq("payTypeName", payGroup.getPayTypeName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPayerName())){
			dc.add(Restrictions.eq("payerName", payGroup.getPayerName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getCheckNumber())){
			dc.add(Restrictions.eq("checkNumber", payGroup.getCheckNumber()));
		}
		if(payGroup.getInvoiceDate()!=null){
			dc.add(Restrictions.eq("invoiceDate", payGroup.getInvoiceDate()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPayVoucher())){
			dc.add(Restrictions.eq("payVoucher", payGroup.getPayVoucher()));
		}
		if (StringUtils.isNotEmpty(payGroup.getRemarks())){
			dc.add(Restrictions.eq("remarks", payGroup.getRemarks()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPosNo())){
			dc.add(Restrictions.eq("posNo", payGroup.getPosNo()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPosTagEendNo())){
			dc.add(Restrictions.eq("posTagEendNo", payGroup.getPosTagEendNo()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPosBank())){
			dc.add(Restrictions.eq("posBank", payGroup.getPosBank()));
		}
		if (StringUtils.isNotEmpty(payGroup.getBankName())){
			dc.add(Restrictions.eq("bankName", payGroup.getBankName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getToBankNname())){
			dc.add(Restrictions.eq("toBankNname", payGroup.getToBankNname()));
		}
		if (StringUtils.isNotEmpty(payGroup.getBankAccount())){
			dc.add(Restrictions.eq("bankAccount", payGroup.getBankAccount()));
		}
		if (StringUtils.isNotEmpty(payGroup.getToBankAccount())){
			dc.add(Restrictions.eq("toBankAccount", payGroup.getToBankAccount()));
		}
		if (StringUtils.isNotEmpty(payGroup.getFromCompanyName())){
			dc.add(Restrictions.eq("fromCompanyName", payGroup.getFromCompanyName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getFromBankName())){
			dc.add(Restrictions.eq("fromBankName", payGroup.getFromBankName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getFromAccount())){
			dc.add(Restrictions.eq("fromAccount", payGroup.getFromAccount()));
		}
		if (StringUtils.isNotEmpty(payGroup.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payGroup.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payGroup.getReceiveAccount()));
		}
		if(payGroup.getDraftAccountedDate()!=null){
			dc.add(Restrictions.eq("draftAccountedDate", payGroup.getDraftAccountedDate()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPayPrice())){
			dc.add(Restrictions.eq("payPrice", payGroup.getPayPrice()));
		}
	   	if(payGroup.getPayPriceType()!=null){
	   		dc.add(Restrictions.eq("payPriceType", payGroup.getPayPriceType()));
	   	}
	   	if(payGroup.getGroupId()!=null){
	   		dc.add(Restrictions.eq("groupId", payGroup.getGroupId()));
	   	}
	   	if(payGroup.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payGroup.getCreateBy()));
	   	}
		if(payGroup.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payGroup.getCreateDate()));
		}
	   	if(payGroup.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payGroup.getUpdateBy()));
	   	}
		if(payGroup.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payGroup.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payGroup.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payGroup.getDelFlag()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPayPriceBack())){
			dc.add(Restrictions.eq("payPriceBack", payGroup.getPayPriceBack()));
		}
	   	if(payGroup.getIsAsAccount()!=null){
	   		dc.add(Restrictions.eq("isAsAccount", payGroup.getIsAsAccount()));
	   	}
		if (StringUtils.isNotEmpty(payGroup.getOldPayPrice())){
			dc.add(Restrictions.eq("oldPayPrice", payGroup.getOldPayPrice()));
		}
	   	if(payGroup.getOrderType()!=null){
	   		dc.add(Restrictions.eq("orderType", payGroup.getOrderType()));
	   	}
		if (StringUtils.isNotEmpty(payGroup.getFastPayType())){
			dc.add(Restrictions.eq("fastPayType", payGroup.getFastPayType()));
		}
		if(payGroup.getPrintTime()!=null){
			dc.add(Restrictions.eq("printTime", payGroup.getPrintTime()));
		}
	   	if(payGroup.getPrintFlag()!=null){
	   		dc.add(Restrictions.eq("printFlag", payGroup.getPrintFlag()));
	   	}
	   	if(payGroup.getPaymentStatus()!=null){
	   		dc.add(Restrictions.eq("paymentStatus", payGroup.getPaymentStatus()));
	   	}
		if(payGroup.getAccountDate()!=null){
			dc.add(Restrictions.eq("accountDate", payGroup.getAccountDate()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payGroupDao.find(page, dc);
	}
	
	public List<PayGroup> find( PayGroup payGroup) {
		DetachedCriteria dc = payGroupDao.createDetachedCriteria();
		
	   	if(payGroup.getId()!=null){
	   		dc.add(Restrictions.eq("id", payGroup.getId()));
	   	}
		if (StringUtils.isNotEmpty(payGroup.getUuid())){
			dc.add(Restrictions.eq("uuid", payGroup.getUuid()));
		}
	   	if(payGroup.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", payGroup.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(payGroup.getPayTypeName())){
			dc.add(Restrictions.eq("payTypeName", payGroup.getPayTypeName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPayerName())){
			dc.add(Restrictions.eq("payerName", payGroup.getPayerName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getCheckNumber())){
			dc.add(Restrictions.eq("checkNumber", payGroup.getCheckNumber()));
		}
		if(payGroup.getInvoiceDate()!=null){
			dc.add(Restrictions.eq("invoiceDate", payGroup.getInvoiceDate()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPayVoucher())){
			dc.add(Restrictions.eq("payVoucher", payGroup.getPayVoucher()));
		}
		if (StringUtils.isNotEmpty(payGroup.getRemarks())){
			dc.add(Restrictions.eq("remarks", payGroup.getRemarks()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPosNo())){
			dc.add(Restrictions.eq("posNo", payGroup.getPosNo()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPosTagEendNo())){
			dc.add(Restrictions.eq("posTagEendNo", payGroup.getPosTagEendNo()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPosBank())){
			dc.add(Restrictions.eq("posBank", payGroup.getPosBank()));
		}
		if (StringUtils.isNotEmpty(payGroup.getBankName())){
			dc.add(Restrictions.eq("bankName", payGroup.getBankName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getToBankNname())){
			dc.add(Restrictions.eq("toBankNname", payGroup.getToBankNname()));
		}
		if (StringUtils.isNotEmpty(payGroup.getBankAccount())){
			dc.add(Restrictions.eq("bankAccount", payGroup.getBankAccount()));
		}
		if (StringUtils.isNotEmpty(payGroup.getToBankAccount())){
			dc.add(Restrictions.eq("toBankAccount", payGroup.getToBankAccount()));
		}
		if (StringUtils.isNotEmpty(payGroup.getFromCompanyName())){
			dc.add(Restrictions.eq("fromCompanyName", payGroup.getFromCompanyName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getFromBankName())){
			dc.add(Restrictions.eq("fromBankName", payGroup.getFromBankName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getFromAccount())){
			dc.add(Restrictions.eq("fromAccount", payGroup.getFromAccount()));
		}
		if (StringUtils.isNotEmpty(payGroup.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payGroup.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payGroup.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payGroup.getReceiveAccount()));
		}
		if(payGroup.getDraftAccountedDate()!=null){
			dc.add(Restrictions.eq("draftAccountedDate", payGroup.getDraftAccountedDate()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPayPrice())){
			dc.add(Restrictions.eq("payPrice", payGroup.getPayPrice()));
		}
	   	if(payGroup.getPayPriceType()!=null){
	   		dc.add(Restrictions.eq("payPriceType", payGroup.getPayPriceType()));
	   	}
	   	if(payGroup.getGroupId()!=null){
	   		dc.add(Restrictions.eq("groupId", payGroup.getGroupId()));
	   	}
	   	if(payGroup.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payGroup.getCreateBy()));
	   	}
		if(payGroup.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payGroup.getCreateDate()));
		}
	   	if(payGroup.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payGroup.getUpdateBy()));
	   	}
		if(payGroup.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payGroup.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payGroup.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payGroup.getDelFlag()));
		}
		if (StringUtils.isNotEmpty(payGroup.getPayPriceBack())){
			dc.add(Restrictions.eq("payPriceBack", payGroup.getPayPriceBack()));
		}
	   	if(payGroup.getIsAsAccount()!=null){
	   		dc.add(Restrictions.eq("isAsAccount", payGroup.getIsAsAccount()));
	   	}
		if (StringUtils.isNotEmpty(payGroup.getOldPayPrice())){
			dc.add(Restrictions.eq("oldPayPrice", payGroup.getOldPayPrice()));
		}
	   	if(payGroup.getOrderType()!=null){
	   		dc.add(Restrictions.eq("orderType", payGroup.getOrderType()));
	   	}
		if (StringUtils.isNotEmpty(payGroup.getFastPayType())){
			dc.add(Restrictions.eq("fastPayType", payGroup.getFastPayType()));
		}
		if(payGroup.getPrintTime()!=null){
			dc.add(Restrictions.eq("printTime", payGroup.getPrintTime()));
		}
	   	if(payGroup.getPrintFlag()!=null){
	   		dc.add(Restrictions.eq("printFlag", payGroup.getPrintFlag()));
	   	}
	   	if(payGroup.getPaymentStatus()!=null){
	   		dc.add(Restrictions.eq("paymentStatus", payGroup.getPaymentStatus()));
	   	}
		if(payGroup.getAccountDate()!=null){
			dc.add(Restrictions.eq("accountDate", payGroup.getAccountDate()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payGroupDao.find(dc);
	}
	
	public PayGroup getByUuid(String uuid) {
		return payGroupDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PayGroup payGroup = getByUuid(uuid);
		payGroup.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(payGroup);
	}

	@Override
	public void confirmRecepitsMoney(PayGroup payGroup) {
		try {
			Long userId = UserUtils.getUser().getId();
			payGroup.setUpdateBy(Integer.valueOf(String.valueOf(userId)));
			payGroup.setUpdateDate(new Date());
			payGroup.setIsAsAccount(1);
			payGroupDao.save(payGroup);
		} catch (Exception e) {
			throw new RuntimeException("修改团期支付出现错误，" + e.getMessage());
		}
	}

	@Override
	public void cancelRecepitsMoney(PayGroup payGroup) {
		try {
			Long userId = UserUtils.getUser().getId();
			payGroup.setUpdateBy(Integer.valueOf(String.valueOf(userId)));
			payGroup.setUpdateDate(new Date());
			payGroup.setIsAsAccount(Context.MONEY_TYPE_CANCEL);
			payGroup.setReceiptConfirmationDate(null);
			payGroupDao.update(payGroup);
		} catch (Exception e) {
			throw new RuntimeException("撤销付款出现错误，" + e.getMessage());
		}
	}

	@Override
	public void rejectRecepitsMoney(PayGroup payGroup, String rejectReason) {
		try {
			Long userId = UserUtils.getUser().getId();
			payGroup.setUpdateBy(Integer.valueOf(String.valueOf(userId)));
			payGroup.setUpdateDate(new Date());
			payGroup.setRejectReason(rejectReason);
			payGroup.setIsAsAccount(Context.MONEY_TYPE_REJECT);
			payGroupDao.save(payGroup);
		} catch (Exception e) {
			throw new RuntimeException("驳回付款出现错误，" + e.getMessage());
		}
	}
}
