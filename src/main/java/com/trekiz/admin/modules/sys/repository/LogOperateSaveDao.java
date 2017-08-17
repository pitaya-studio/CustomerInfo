package com.trekiz.admin.modules.sys.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.sys.entity.LogOperate;
/**
 * 
 * @author wangxk
 * @version 2015-1-28
 */

public interface LogOperateSaveDao extends BaseDao<LogOperate>{
	
	public void save(LogOperate logOperate);
}
