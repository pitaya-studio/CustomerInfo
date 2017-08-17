package com.trekiz.admin.modules.sys.service;

import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.LogOperate;

public interface ILogOperateService {
	
	public LogOperate get(String id);
	
	public LogOperate saveLogOperate(LogOperate logOperate);
	
	public Page<LogOperate> find(Page<LogOperate> page,Map<String,Object> paramMap);
}
