package com.trekiz.admin.modules.sys.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.SysModulePath;

public interface SysModulePathDao extends SysModulePathDaoCustom,CrudRepository<SysModulePath, String>{
	
}

interface SysModulePathDaoCustom extends BaseDao<SysModulePath>{
	public List<SysModulePath> queryAllSysModulePath();
	public List<SysModulePath> queryModulePathByPath(String path);
}

@Repository
class SysModulePathDaoImpl extends BaseDaoImpl<SysModulePath>implements SysModulePathDaoCustom{
	
	public List<SysModulePath> queryAllSysModulePath(){
		List<SysModulePath> list = new ArrayList<SysModulePath>();
		String sqlString = "select id,modulepath from sys_config_uri_path";
		list = findBySql(sqlString, SysModulePath.class);
		return list;
	}
	
	public List<SysModulePath> queryModulePathByPath(String modulepath){
		List<SysModulePath> list = new ArrayList<SysModulePath>();
		String sqlString = "select id,modulepath from sys_config_uri_path where 1 = 1 ";
		if(modulepath!=null&&!"".equals(modulepath)){
			sqlString += " and modulepath='" + modulepath + "'";
		}
		list = findBySql(sqlString, SysModulePath.class);
		return list;
	}
}
