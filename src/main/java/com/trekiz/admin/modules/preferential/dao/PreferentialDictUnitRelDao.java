/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import java.util.*;

import com.trekiz.admin.modules.preferential.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface PreferentialDictUnitRelDao  extends BaseDao<PreferentialDictUnitRel> {
	
	public PreferentialDictUnitRel getByUuid(String uuid);
	
	/**
	 * 根据优惠字典单位关联表的字典uuid查询出单位集合
	 * @param uuid 字典uuid
	 * @return
	 * @author majiancheng
	 */
	public List<Map<String, String>> getUnitsByDictUuid(String uuid);
	
	/**
	 * 批量删除数据字典和单位的关联表 addbyzhanghao
	 * @param dictUuids
	 */
	public void removeByDictUuids(String[] dictUuids);
	
}
