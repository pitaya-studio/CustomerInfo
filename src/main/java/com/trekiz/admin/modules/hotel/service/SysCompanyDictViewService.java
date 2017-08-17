/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;
import com.trekiz.admin.modules.hotel.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface SysCompanyDictViewService{
	
	public Page<SysCompanyDictView> find(Page<SysCompanyDictView> page, SysCompanyDictView sysCompanyDictView);
	
	public List<SysCompanyDictView> find( SysCompanyDictView sysCompanyDictView);
	
	public SysCompanyDictView getByUuId(String value);
	
	/**
	* 查询基础信息中名称是否存在(名称存在返回true，反之返回false)
	* @param type 字典类型
	* @param id 字典ID
	* @param label 字典Label值
	* @param companyId 公司ID
	* @return 
	* @author majiancheng
	* @Time 2015-4-3
	*/
	public boolean findIsExist(String type, int id, String label, Long companyId);
	
	/**
	* 根据字典类型和公司ID查询
	* @param type 字典类型
	* @param companyId 公司ID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public List<SysCompanyDictView> findByType(String type, Long companyId);
	
	/**
	* 根据uuid查询字典信息
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public SysCompanyDictView findByUuid(String uuid);
	
	/**
	 * 根据uuid集合查询字典信息
	 * @param uuids uuid集合
	 * @return
	 */
	public List<SysCompanyDictView> findByUuids(String[] uuids);
}
