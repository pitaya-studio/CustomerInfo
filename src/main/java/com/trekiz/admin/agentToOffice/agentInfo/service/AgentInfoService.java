package com.trekiz.admin.agentToOffice.agentInfo.service;

import java.util.List;

import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;

public interface AgentInfoService {
	/**
	 * 通过批发商id和类型查找字典数据
	 * @param companyId
	 * @param type
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<Sysdefinedict> getDefineDictByCompanyIdAndType(Long companyId,String type);
	
	/**
	 *保存自定义字典表 
	 * @param sysDefineDict
	 * @author chao.zhang@quauq.com
	 */
	public  void saveSysDefineDict(Sysdefinedict sysDefineDict);
	
	/**
	 * 修改自定义字典表
	 * @param sysDefineDict
	 * @author chao.zhang@quauq.com
	 */
	public void updateSysDefineDict(Sysdefinedict sysDefineDict);
	
	/**
	 * 删除自定义字典表
	 * @param sysDefineDict
	 * @author chao.zhang@quauq.com
	 */
	public void deleteSysDefineDict(Long sysDefineDictId);
	
	public boolean getDefineDictByLabel(Integer companyId,
			String label, String type);
}
