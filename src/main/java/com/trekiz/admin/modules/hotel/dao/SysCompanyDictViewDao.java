/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.SysDict;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface SysCompanyDictViewDao  extends BaseDao<SysCompanyDictView> {
	
	
	public int findMaxValueByCompanyIdAndType(String type);
	
	public SysCompanyDictView getByUuId(String value);
	
	/**
	 * 根据uuids集合获取多个字典信息
	*<p>Title: findByUuids</p>
	* @return List<SysCompanyDictView> 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午3:21:31
	* @throws
	 */
	public List<SysCompanyDictView> findByUuids(String[] uuids);
	
	/**
	 * 根据类型获取所有的字典信息
	     * <p>@Description TODO</p>
		 * @Title: findByType
	     * @return List<SysCompanyDictView>
	     * @author majiancheng       
	     * @date 2015-10-14 下午2:27:06
	 */
	public List<SysDict> findByType(String type);
}
