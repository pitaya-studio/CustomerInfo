package com.trekiz.admin.modules.sys.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.SysModuleConfig;
import com.trekiz.admin.modules.sys.repository.SysModuleConfigDao;
/**
 * 模块信息表的操作方法
 * @author wangxk
 *
 */
@Service
public class SysModuleConfigService {

	@Autowired
	private SysModuleConfigDao sysModuleConfigDao ;
	
	/**
	 * 保存记录到数据库中 
	 * @param sysModule
	 */
	public void saveSysModuleConfig(SysModuleConfig sysModule){
		sysModuleConfigDao.save(sysModule);
	}
	/**
	 * 根据id删除模块信息表的记录
	 * @param id
	 */
	public void delSysModuleConfig(String id){
		sysModuleConfigDao.delete(id);
	}
	/**
	 * 根据id及页面上获取的数据更新模块信息表
	 * @param id
	 * @param lcompanyId 批发商id
	 * @param fmoduleId 父模块id
	 * @param moduleId 模块id
	 * @param pageName 页面名称
	 * @param path 页面路径
	 * @param prepath 页面预览路径
	 * @param updateBy 更新人
	 * @param date 更新时间
	 * @param fmodulename 父模块名称
	 * @param modulename 模块名称
	 * @return 更新的行数
	 */
	@Transactional
	public int updateSysModuleConfig(String id,Long lcompanyId,String fmoduleId,String moduleId,String pageName,String path,String prepath,String updateBy,Date date,String fmodulename,String modulename){
		return sysModuleConfigDao.updateSysModuleConfig(id,lcompanyId,fmoduleId,moduleId,pageName,path,prepath,updateBy,date,fmodulename,modulename);
	}
	/**
	 * 查询模块信息表的信息分页显示
	 * @param request
	 * @param response
	 * @param condMap
	 * @param sysModule
	 * @param companyId
	 * @return
	 */
	public  Page<Map<String,Object>> querySysModuleConfigList(HttpServletRequest request,HttpServletResponse response,Map<String,Object> condMap,SysModuleConfig sysModule,String companyId){
		return sysModuleConfigDao.querySysModuleConfigList(request, response, condMap, sysModule,companyId);
	}
	/**
	 * 根据Id进行查询配置模块信息表的明细信息
	 * @param id
	 * @return
	 */
	public SysModuleConfig querySysModuleConfigById(String id){
		return sysModuleConfigDao.querySysModuleConfigById(id);
	}
	/**
	 * 根据公司，判断path是否在sys_module_config中存在，如果存在，则返回格式化的路径（rootpath/公司id/path）；不存在则返回path
	 * @return
	 */
	public SysModuleConfig queryModuleByPathAndCompanyId(String path,String companyid){
		return sysModuleConfigDao.queryModuleByPathAndCompanyId(path,companyid);
	}
}
