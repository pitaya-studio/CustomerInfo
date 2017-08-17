/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service;

import com.trekiz.admin.common.persistence.Page;

import java.util.*;

import com.trekiz.admin.modules.pay.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface PayGroupService{
	
	public void save (PayGroup payGroup);
	
	public void update (PayGroup payGroup);
	
	public PayGroup getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PayGroup> find(Page<PayGroup> page, PayGroup payGroup);
	
	public List<PayGroup> find( PayGroup payGroup);
	
	public PayGroup getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public void confirmRecepitsMoney(PayGroup payGroup);
	
	public void cancelRecepitsMoney(PayGroup payGroup);
	
	public void rejectRecepitsMoney(PayGroup payGroup, String rejectReason);
}
