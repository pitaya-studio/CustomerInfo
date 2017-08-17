/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.pay.entity.PayBanktransfer;
import com.trekiz.admin.modules.pay.input.PayBanktransferInput;
import com.trekiz.admin.modules.pay.query.PayBanktransferQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface PayBanktransferService{
	
	public void save (PayBanktransfer payBanktransfer);
	
	public void save (PayBanktransferInput payBanktransferInput);
	
	public void update (PayBanktransfer payBanktransfer);
	
	public PayBanktransfer getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PayBanktransfer> find(Page<PayBanktransfer> page, PayBanktransferQuery payBanktransferQuery);
	
	public List<PayBanktransfer> find( PayBanktransferQuery payBanktransferQuery);
	
	public PayBanktransfer getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
