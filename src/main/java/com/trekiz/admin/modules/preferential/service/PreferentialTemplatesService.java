/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;

import com.trekiz.admin.modules.preferential.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface PreferentialTemplatesService{
	
	public void save (PreferentialTemplates preferentialTemplates);
	
	public void update (PreferentialTemplates preferentialTemplates);
	
	public PreferentialTemplates getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PreferentialTemplates> find(Page<PreferentialTemplates> page, PreferentialTemplates preferentialTemplates);
	
	public List<PreferentialTemplates> find( PreferentialTemplates preferentialTemplates);
	
	public PreferentialTemplates getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 保存模板表数据,并保存字典单位关联和模板关联表
	 * @param preferentialTemplates 模板表数据
	 * @param tempsRels 关联表数据
	 * @author majiancheng
	 */
	public void saveTemplatesAndRel(PreferentialTemplates preferentialTemplates, Map<String, Object> datas);
	
	/**
	 * 修改模板表数据,并保存字典单位关联和模板关联表
	 * @param preferentialTemplates 模板表数据
	 * @param tempsRels 关联表数据
	 * @author majiancheng
	 */
	public void updateTemplatesAndRel(PreferentialTemplates preferentialTemplates, Map<String, Object> datas);
	
	/**
	 * 根据模板名称和因果类型查询模板列表
	 * @param tempName 模板名称
	 * @return
	 */
	public Page<Map<String, String>> findTemplatesInfos(Page<Map<String, String>> page, String tempName);
	
	/**
	 * 根据uuid查询出模板关联的详细信息
	 * @param uuid
	 * @return
	 */
	public List<Object[]> findTempsInfoByUuid(String uuid);
	
	/**
	* 根据模板uuid查询出模板关联的详细信息
	* @param tempUuid 模板uuid
	* @return
	* @author majiancheng
	* @Time 2015-5-25
	*/
	public List<Object[]> findRelInfoByTempUuid(String tempUuid);
}
