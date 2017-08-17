package com.trekiz.admin.modules.sys.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.entity.LogOperate;
import com.trekiz.admin.modules.sys.repository.LogOperateDao;

/**
 * 操作日志的保存service
 * @author wangxk
 * 如果需要分页查询,想要方便的话,就单建立一个jpa封装好的分页查询
 */
@Service
@Transactional(readOnly = true)
public class LogOperateService extends BaseService {

	@Autowired
	private LogOperateDao logOpeDao;
	
	public LogOperate queryLogOperateById(String id){
		
		return logOpeDao.queryLogOperateById(id);
	}
	
	public Page<Map<String,Object>> findLogOperate(HttpServletRequest request, HttpServletResponse response, Map<String,Object> condMap, 
			LogOperate logOperate, String beginDate, String endDate, String opecomid, Integer bussinessType){
		
		return logOpeDao.queryLogOperateList(request, response, condMap, logOperate, beginDate, endDate, opecomid, bussinessType);
	}
	
	public List<Object[]> queryByParas(Integer bussinessType, Long bussinessId, String opeType) {
		return logOpeDao.queryByParas(bussinessType, bussinessId, opeType);
	}
}
