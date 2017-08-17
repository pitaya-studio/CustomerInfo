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
import com.trekiz.admin.modules.hotel.entity.SysDict;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface SysDictService{
	
	public void save (SysDict sysDict);
	
	public void save (SysCompanyDictView sysCompanyDictView);
	
	public void update (SysDict sysDict);
	
	public void update (SysCompanyDictView sysCompanyDictView);
	
	public SysDict getById(java.lang.Long value);
	
	public SysDict getByUuId(String uuid);
	
	public void removeById(java.lang.Long value);
	
	public void removeByUuid(String uuid);
	
	public Page<SysDict> find(Page<SysDict> page, SysDict sysDict);
	
	public List<SysDict> find(SysDict sysDict);
	
	/**
	* 查询所有的字典数据不加delFlag='0'（主要用于初始化uuid数据）
	* @return
	* @exception  
	* @author majiancheng
	* @Time 2015-4-3 15:02:29
	*/
	public List<SysDict> findAll();
	
	/**
	* 更新所有的字典数据（主要初始化uuid数据）
	* @param List<SysDict>
	* @return
	* @exception  
	* @author majiancheng
	* @Time 2015-4-3 15:02:29
	*/
	public void updateSysDicts(List<SysDict> sysDicts);
	
	/**
	 * 根据类型查询字典集合
	*<p>Title: findByType</p>
	* @param type 类型
	* @return List<SysDict> 返回类型
	* @author majiancheng
	* @date 2015-6-3 下午8:14:40
	* @throws
	 */
	public List<SysDict> findByType(String type);
	
}
