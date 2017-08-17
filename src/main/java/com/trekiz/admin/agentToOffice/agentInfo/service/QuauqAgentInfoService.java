package com.trekiz.admin.agentToOffice.agentInfo.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.agent.entity.AgentInfoContacts;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.sys.entity.User;

public interface QuauqAgentInfoService {
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
	/**
	 * 根据标签获得DefineDict
	 * @param companyId
	 * @param label
	 * @param type
	 * @return
	 */
	public boolean getDefineDictByLabel(Integer companyId,
			String label, String type);
	/**
	 * 验证渠道等级或渠道类型是否被使用
	 * @param label
	 * @return
	 */
	public boolean checkTypeOrGrade(String id,String label);

	/**
	 * 保存quauq渠道账号前期准备
	 * @param agentInfo
	 * @param quauqCompanyId
	 * @param quauqAgentUserLoginName
	 * @return
	 */
	public Map<String, Object> handleUserInfo(User user, Agentinfo agentInfo,
			Long quauqCompanyId, AgentInfoContacts agentInfoContacts);
	
	
	/**
	 * 获取所有的quauq渠道
	 * @return
	 */
	public List<Agentinfo> getAllQuauqAgentinfos();
	
	/**
	 * 获取所有的quauq渠道的登陆账号
	 * @return
	 */
	public List<User> getAllQuauqAgentLoginUser();

	/**
	 * 依据id获取quauq渠道的登陆账号
	 * @param agentIdList
	 * @return
	 */
	public List<User> getQuauqAgentLoginUsers(List<String> agentIdList);

	/**
	 * 获取quauq渠道列表
	 * @param page
	 * @param paramMap
	 * @return
	 */
	public Page<Map<Object, Object>> findQuauqAgentList(Page<Map<Object, Object>> page, Map<String, Object> paramMap);

	/**
	 * 删除 quauq渠道
	 * @param parseLong
	 */
	public void deleteQuauqAgent(long parseLong);
	
	/**
	 * @Description ququa渠道统计
	 * @author yakun.bai
	 * @Date 2016-5-3
	 */
	public Page<Map<Object, Object>> quauqAgentStatistics(Page<Map<Object, Object>> page, Map<String, String> mapRequest);
	
	/**
	 * @Description ququa渠道订单导出
	 * @author yakun.bai
	 * @Date 2016-5-5
	 */
	public List<Map<Object, Object>> downloadAllOrder(Map<String, String> mapRequest);

	public boolean isExist(String string);
	
	/**
	 * 获取渠道类别列表（下拉框）
	 * @author yang.wang
	 * @date 2016-08-10
	 * */
	public List<Map<String, Object>> getAgentTypeList(); 
	
	/**
	 * 获取渠道商可选的上级关系列表（上下级互斥）
	 * @param agentId 该渠道商id 若新建渠道商置agentId=null
	 * @author yang.wang
	 * @date 2016-08-11
	 * */
	public List<Map<String, String>> getAgentParentList(Long agentId);

	public List<Map<Object, Object>> findAgentNotBoundList(
			Map<String, Object> paramMap);
}
