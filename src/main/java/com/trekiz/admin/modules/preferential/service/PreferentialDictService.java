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
import com.trekiz.admin.modules.preferential.entity.PreferentialDict;
import com.trekiz.admin.modules.preferential.input.PreferentialDictInput;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface PreferentialDictService{
	
	public void save (PreferentialDict preferentialDict);
	
	public void update (PreferentialDict preferentialDict);
	
	public PreferentialDict getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PreferentialDict> find(Page<PreferentialDict> page, PreferentialDict preferentialDict);
	
	public List<PreferentialDict> find( PreferentialDict preferentialDict);
	
	public PreferentialDict getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 保存字典接口
	 * 关联保存相关的关联数据
	 * add by zhanghao
	 * @param input
	 * @return
	 */
	public String saveObj(PreferentialDictInput input);
	
	/**
	 * 更新字典接口
	 * 关联保存相关的关联数据
	 * add by zhanghao
	 * @param input
	 * @return
	 */
	public String updateObj(PreferentialDictInput input);
	
}
