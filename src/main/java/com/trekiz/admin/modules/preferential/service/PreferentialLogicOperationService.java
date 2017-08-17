/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.preferential.entity.PreferentialLogicOperation;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface PreferentialLogicOperationService{
	
	public void save (PreferentialLogicOperation preferentialLogicOperation);
	
	public void update (PreferentialLogicOperation preferentialLogicOperation);
	
	public PreferentialLogicOperation getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PreferentialLogicOperation> find(Page<PreferentialLogicOperation> page, PreferentialLogicOperation preferentialLogicOperation);
	
	public List<PreferentialLogicOperation> find( PreferentialLogicOperation preferentialLogicOperation);
	
	public PreferentialLogicOperation getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
