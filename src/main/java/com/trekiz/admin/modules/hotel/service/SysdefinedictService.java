/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface SysdefinedictService{
	
	public void save (Sysdefinedict sysdefinedict);
	
	public void save (SysCompanyDictView sysCompanyDictView, long companyId);
	
	public void update (Sysdefinedict sysdefinedict);

	public void update (SysCompanyDictView sysCompanyDictView);
	
	public Sysdefinedict getById(java.lang.Long value);
	
	public void removeById(java.lang.Long value);
	
	public void removeByUuid(String uuid);
	
	public Page<Sysdefinedict> find(Page<Sysdefinedict> page, Sysdefinedict sysdefinedict);
	
	public List<Sysdefinedict> find( Sysdefinedict sysdefinedict);
	
	public Sysdefinedict findByUUid(String uuid);
	
	/**
	* 查询所有的字典数据不加delFlag='0'（主要用于初始化uuid数据）
	* @param 
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
}
