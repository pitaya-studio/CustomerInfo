/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;

import java.util.*;

import com.trekiz.admin.modules.preferential.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface PreferentialDictTemplatesRelDao  extends BaseDao<PreferentialDictTemplatesRel> {
	
	public PreferentialDictTemplatesRel getByUuid(String uuid);
	
	/**
	 * 根据因果类型查询出对应的字典单位管理表uuid和字典名称
	 * @param type 因果类型（0、因；1、果）
	 * @return
	 */
	public List<Map<String, String>> findDictUuidAndDictNameByType(Integer type);
	
	/**
	 * 根据模板名称和因果类型查询模板列表
	 * @param tempName 模板名称
	 * @return
	 */
	public Page<Map<String, String>> findTemplatesInfos(Page<Map<String, String>> page, String tempName);
	
	/**
	 * 根据模板uuid查询出模板的详细描述
	 * @param uuid
	 * @return
	 */
	public List<Object[]> findTempsInfoByTempsUuid(String uuid);
	
	/**
	* 根据模板uuid查询出模板关联信息
	* @param tempUuid
	* @return
	* @author majiancheng
	* @Time 2015-5-25
	*/
	public List<Object[]> findRelInfoByTempUuid(String tempUuid);
	
	/**
	* 根据模板uuid删除出模板关联信息
	* @param templatesUuid
	* @return
	* @author majiancheng
	* @Time 2015-5-26
	*/
	public int removeByTemplatesUuid(String templatesUuid);
}
