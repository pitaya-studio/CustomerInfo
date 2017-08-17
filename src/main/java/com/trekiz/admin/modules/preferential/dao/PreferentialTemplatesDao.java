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





public interface PreferentialTemplatesDao  extends BaseDao<PreferentialTemplates> {
	
	public PreferentialTemplates getByUuid(String uuid);
	
	/**
	 * 获取所有未删除的优惠模板
	*<p>Title: getAllTemplates</p>
	* @return List<PreferentialTemplates> 返回类型
	* @author majiancheng
	* @date 2015-7-9 下午8:41:47
	* @throws
	 */
	public List<PreferentialTemplates> getAllTemplates();
}
