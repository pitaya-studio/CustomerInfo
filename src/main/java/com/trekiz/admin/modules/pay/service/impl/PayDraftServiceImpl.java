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
import com.trekiz.admin.modules.pay.dao.PayDraftDao;
import com.trekiz.admin.modules.pay.entity.PayDraft;
import com.trekiz.admin.modules.pay.input.PayDraftInput;
import com.trekiz.admin.modules.pay.query.PayDraftQuery;
import com.trekiz.admin.modules.pay.service.PayDraftService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PayDraftServiceImpl  extends BaseService implements PayDraftService{
	@Autowired
	private PayDraftDao payDraftDao;

	public void save (PayDraft payDraft){
		super.setOptInfo(payDraft, BaseService.OPERATION_ADD);
		payDraftDao.saveObj(payDraft);
	}
	
	public void save (PayDraftInput payDraftInput){
		PayDraft payDraft = payDraftInput.getPayDraft();
		super.setOptInfo(payDraft, BaseService.OPERATION_ADD);
		payDraftDao.saveObj(payDraft);
	}
	
	public void update (PayDraft payDraft){
		super.setOptInfo(payDraft, BaseService.OPERATION_UPDATE);
		payDraftDao.updateObj(payDraft);
	}
	
	public PayDraft getById(java.lang.Integer value) {
		return payDraftDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PayDraft obj = payDraftDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<PayDraft> find(Page<PayDraft> page, PayDraftQuery payDraftQuery) {
		DetachedCriteria dc = payDraftDao.createDetachedCriteria();
		
	   	if(payDraftQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payDraftQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payDraftQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payDraftQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getDrawerName())){
			dc.add(Restrictions.eq("drawerName", payDraftQuery.getDrawerName()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getDrawerAccount())){
			dc.add(Restrictions.eq("drawerAccount", payDraftQuery.getDrawerAccount()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getPayBankName())){
			dc.add(Restrictions.eq("payBankName", payDraftQuery.getPayBankName()));
		}
		if(payDraftQuery.getDraftAccountedDate()!=null){
			dc.add(Restrictions.eq("draftAccountedDate", payDraftQuery.getDraftAccountedDate()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getReceiveCompanyName())){
			dc.add(Restrictions.eq("receiveCompanyName", payDraftQuery.getReceiveCompanyName()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payDraftQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payDraftQuery.getReceiveAccount()));
		}
	   	if(payDraftQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payDraftQuery.getCreateBy()));
	   	}
		if(payDraftQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payDraftQuery.getCreateDate()));
		}
	   	if(payDraftQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payDraftQuery.getUpdateBy()));
	   	}
		if(payDraftQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payDraftQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payDraftQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payDraftDao.find(page, dc);
	}
	
	public List<PayDraft> find( PayDraftQuery payDraftQuery) {
		DetachedCriteria dc = payDraftDao.createDetachedCriteria();
		
	   	if(payDraftQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payDraftQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payDraftQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payDraftQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getDrawerName())){
			dc.add(Restrictions.eq("drawerName", payDraftQuery.getDrawerName()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getDrawerAccount())){
			dc.add(Restrictions.eq("drawerAccount", payDraftQuery.getDrawerAccount()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getPayBankName())){
			dc.add(Restrictions.eq("payBankName", payDraftQuery.getPayBankName()));
		}
		if(payDraftQuery.getDraftAccountedDate()!=null){
			dc.add(Restrictions.eq("draftAccountedDate", payDraftQuery.getDraftAccountedDate()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getReceiveCompanyName())){
			dc.add(Restrictions.eq("receiveCompanyName", payDraftQuery.getReceiveCompanyName()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payDraftQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payDraftQuery.getReceiveAccount()));
		}
	   	if(payDraftQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payDraftQuery.getCreateBy()));
	   	}
		if(payDraftQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payDraftQuery.getCreateDate()));
		}
	   	if(payDraftQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payDraftQuery.getUpdateBy()));
	   	}
		if(payDraftQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payDraftQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payDraftQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payDraftQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payDraftDao.find(dc);
	}
	
	public PayDraft getByUuid(String uuid) {
		return payDraftDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PayDraft payDraft = getByUuid(uuid);
		payDraft.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(payDraft);
	}
}
