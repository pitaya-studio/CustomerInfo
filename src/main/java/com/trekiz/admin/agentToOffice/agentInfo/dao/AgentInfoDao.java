package com.trekiz.admin.agentToOffice.agentInfo.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;

public interface AgentInfoDao extends BaseDao {
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
	 * @
	 */
	public void deleteSysDefineDict(Long sysDefineDictId);
	
	/**
	 * 检验是否重复
	 * @author chao.zhang@quauq.com
	 */
	public List<Sysdefinedict> getDefineDictByLabel(Integer companyId,String label,String type);
	
	public Sysdefinedict getDefineDictByiD(Long id);
	
	/**
	 * 获取字典表中渠道商类别
	 * @author yang.wang@quauq.com
	 * */
	public List<Map<String, Object>> getAgentType4Select();
	
	/**
	 * 获取可选的上级关系列表（上下级互斥）
	 * @param agentId 渠道商id 新建时为null
	 * @author yang.wang@quauq.com
	 * */
	public List<Map<String, String>> getAgentParent4Select(Long agentId);

	public Agentinfo getAgentInfoById(long id);
}
