/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.pay.entity.PayPos;
import com.trekiz.admin.modules.pay.input.PayPosInput;
import com.trekiz.admin.modules.pay.query.PayPosQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface PayPosService{
	
	public void save (PayPos payPos);
	
	public void save (PayPosInput payPosInput);
	
	public void update (PayPos payPos);
	
	public PayPos getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PayPos> find(Page<PayPos> page, PayPosQuery payPosQuery);
	
	public List<PayPos> find( PayPosQuery payPosQuery);
	
	public PayPos getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
