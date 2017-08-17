package com.trekiz.admin.agentToOffice.agentInfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.agentInfo.dao.AgentInfoDao;
import com.trekiz.admin.agentToOffice.agentInfo.service.AgentInfoService;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
@Service
@Transactional(readOnly = true)
public class AgentInfoServiceImpl extends BaseService implements AgentInfoService{
	@Autowired
	private AgentInfoDao agentInfoDao;
	
	/**
	 * 根据字典类型和批发商id查询
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<Sysdefinedict> getDefineDictByCompanyIdAndType(Long companyId,
			String type) {
		List<Sysdefinedict> list = agentInfoDao.getDefineDictByCompanyIdAndType(companyId, type);
		return list;
	}
	
	/**
	 * 保存
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void saveSysDefineDict(Sysdefinedict sysDefineDict) {
		super.setOptInfo(sysDefineDict, BaseService.OPERATION_ADD);
		agentInfoDao.saveSysDefineDict(sysDefineDict);
		
	}

	@Override
	public void updateSysDefineDict(Sysdefinedict sysDefineDict) {
		
		
	}
	
	/**
	 * 删除
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void deleteSysDefineDict(Long sysDefineDictId) {
		agentInfoDao.deleteSysDefineDict(sysDefineDictId);
		
	}

	@Override
	public boolean getDefineDictByLabel(Integer companyId,
			String label, String type) {
		List<Sysdefinedict> list = agentInfoDao.getDefineDictByLabel(companyId, label, type);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}

}
