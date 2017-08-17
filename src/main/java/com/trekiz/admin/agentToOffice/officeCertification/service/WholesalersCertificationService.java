package com.trekiz.admin.agentToOffice.officeCertification.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.Office;

public interface WholesalersCertificationService {
	
	/**
	 * 查询所有具有上架权限的批发商和批发商上架到T1的产品团期数
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-13
	 */
	public Page<Map<String,Object>> getAllOffice(Page<Map<String,Object>> page,String groupCodeOrOfficeNameOrActivityName);
	
	/**
	 * 根据批发商id查询批发商详情
	 * @param companyId
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-14
	 */
	public Office getOfficeDetail(Long companyId);
	
	/**
	 * 根据批发商id获得名片的ids
	 * @param companyId
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-19
	 */
	public List<Map<String,String>> getDocIds(Long companyId);
}
