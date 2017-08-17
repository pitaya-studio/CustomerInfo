package com.trekiz.admin.modules.sys.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.entity.SysModulePath;
import com.trekiz.admin.modules.sys.repository.SysModulePathDao;

/**
 * 自由配置路径的处理service
 * @author Administrator
 *
 */
@Service
public class SysModulePathService extends BaseService{
	private static final Log log = LogFactory.getLog(SysModulePathService.class);
	
	@Autowired
	private SysModulePathDao sysModulePathDao;
	
	/**
	 * 保存
	 * @param sysModulePath 已经处理过的modulepath
	 * @return
	 */
	public void save(String modulepath){
		List<SysModulePath> list = sysModulePathDao.queryModulePathByPath(modulepath);
		SysModulePath sysModulePath = new SysModulePath();
		if(list!=null&&list.size()<1){//没有记录则保存
			sysModulePath.setId(UUID.randomUUID().toString());
			sysModulePath.setModulepath(modulepath);
			sysModulePathDao.save(sysModulePath);
		}else{
			log.info("自由配置的路径：  " + list.get(0).getModulepath() + " 已经存在");
		}
	}
	/**
	 * 根据id进行查询
	 * @param id
	 * @return
	 */
	public SysModulePath queryModulePathById(String id){
		
		return sysModulePathDao.findOne(id);
	}
	
	public List<SysModulePath> queryModulePathByPath(String modulepath){
		return sysModulePathDao.queryModulePathByPath(modulepath);
	}
	/**
	 * 查询出所有的已经配置的路径
	 * @return
	 */
	public List<SysModulePath> queryAllSysModulePath(){
		return sysModulePathDao.queryAllSysModulePath();
	}
	
}
