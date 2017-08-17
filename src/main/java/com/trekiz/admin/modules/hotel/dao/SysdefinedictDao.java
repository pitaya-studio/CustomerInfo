/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface SysdefinedictDao  extends BaseDao<Sysdefinedict> {
	
	
	/**
	* 查询所有的字典数据（主要用于初始化uuid数据）
	* @param List<Sysdefinedict>
	* @return
	* @exception  
	* @author majiancheng
	* @Time 2015-4-3 15:02:29
	*/
	public List<Sysdefinedict> findAll();
	
	/**
	* 更新所有的字典数据（主要初始化uuid数据）
	* @param List<Sysdefinedict>
	* @return
	* @exception  
	* @author majiancheng
	* @Time 2015-4-3 15:02:29
	*/
	public void updateSysdefinedicts(List<Sysdefinedict> sysdefinedicts);
	
	public Sysdefinedict getByUuid(String uuid) ;
	
	public List<Sysdefinedict> findByCompanyIdAndType(Long companyId,String type);
	
}
