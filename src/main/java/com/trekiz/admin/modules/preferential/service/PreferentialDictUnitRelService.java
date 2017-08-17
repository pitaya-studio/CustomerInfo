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

public interface PreferentialDictUnitRelService{
	
	public void save (PreferentialDictUnitRel preferentialDictUnitRel);
	
	public void update (PreferentialDictUnitRel preferentialDictUnitRel);
	
	public PreferentialDictUnitRel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<PreferentialDictUnitRel> find(Page<PreferentialDictUnitRel> page, PreferentialDictUnitRel preferentialDictUnitRel);
	
	public List<PreferentialDictUnitRel> find( PreferentialDictUnitRel preferentialDictUnitRel);
	
	public PreferentialDictUnitRel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据优惠字典单位关联表的字典uuid查询出单位集合
	 * @param uuid 字典uuid
	 * @return
	 * @author majiancheng
	 */
	public List<Map<String, String>> getUnitsByDictUuid(String uuid);
	
	/**
	* 根据字典和单位uuid获取描述
	* @param dictUuid 字典uuid
	* @param unitUuid 单位uuid
	* @return
	* @author majiancheng
	* @Time 2015-5-26
	*/
	public String findNamesByDictUuidAndUnitUuid(String dictUuid, String unitUuid);
	
	/**
	 * 根据字典名称、单位名称和数据类型获取描述
	*<p>Title: findDescByNamesAndDataType</p>
	* @param dictName 字典名称
	* @param unitName 单位名称
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-2 上午10:21:22
	* @throws
	 */
	public String findDescByNamesAndDataType(String dictName, String unitName, int dataType);
	
	/**
	 * 根据字典uuid和单位uuid,获取模板的html代码(type表示因果类型)
	*<p>Title: getOutHtmlByDictUuidAndUnitUuid</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午10:23:09
	* @throws
	 */
	public String getOutHtmlByDictUuidAndUnitUuid(String dictUuid, String unitUuid, int type);
	
	/**
	 * 根据字典名称和单位名称,获取模板的html代码(type表示因果类型)
	*<p>Title: getOutDataByNamesAndDataType</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午10:22:19
	* @throws
	 */
	public String getOutHtmlByNamesAndDataType(String dictName, String unitName, int dataType, int type);
	
}
