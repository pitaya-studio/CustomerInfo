/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.preferential.entity.PreferentialDictTemplatesRel;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface PreferentialDictTemplatesRelService{
	
	public void save (PreferentialDictTemplatesRel preferentialDictTemplatesRel);
	
	public void update (PreferentialDictTemplatesRel preferentialDictTemplatesRel);
	
	public PreferentialDictTemplatesRel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PreferentialDictTemplatesRel> find(Page<PreferentialDictTemplatesRel> page, PreferentialDictTemplatesRel preferentialDictTemplatesRel);
	
	public List<PreferentialDictTemplatesRel> find( PreferentialDictTemplatesRel preferentialDictTemplatesRel);
	
	public PreferentialDictTemplatesRel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据因果类型查询出对应的字典单位管理表uuid和字典名称
	 * @param type 因果类型（0、因；1、果）
	 * @return
	 */
	public List<Map<String, String>> findDictUuidAndDictNameByType(Integer type);
}
