/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.pay.entity.PayDraft;
import com.trekiz.admin.modules.pay.input.PayDraftInput;
import com.trekiz.admin.modules.pay.query.PayDraftQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface PayDraftService{
	
	public void save (PayDraft payDraft);
	
	public void save (PayDraftInput payDraftInput);
	
	public void update (PayDraft payDraft);
	
	public PayDraft getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PayDraft> find(Page<PayDraft> page, PayDraftQuery payDraftQuery);
	
	public List<PayDraft> find( PayDraftQuery payDraftQuery);
	
	public PayDraft getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
