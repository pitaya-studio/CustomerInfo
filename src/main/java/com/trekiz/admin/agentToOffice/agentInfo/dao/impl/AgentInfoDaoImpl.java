package com.trekiz.admin.agentToOffice.agentInfo.dao.impl;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.agent.entity.Agentinfo;
import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.agentInfo.dao.AgentInfoDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;
@Service
public class AgentInfoDaoImpl extends BaseDaoImpl implements AgentInfoDao {
	/**
	 * 根据companyId和type查询
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<Sysdefinedict> getDefineDictByCompanyIdAndType(Long companyId,
			String type) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT id,label,value,type,defaultFlag,sort,description,companyId,createBy,createDate,updateBy,updateDate,");
		sbf.append("delFlag,remarks,uuid FROM sysdefinedict WHERE companyId=? AND type=? AND delFlag=? ORDER BY sort ASC ");
		List<Sysdefinedict> list=this.findBySql(sbf.toString(),Sysdefinedict.class,companyId.intValue(),type,0);
		return list;
	}
	/**
	 * 保存sysdefinedict
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void saveSysDefineDict(Sysdefinedict sysDefineDict) {
		this.saveObj(sysDefineDict);
		
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
		StringBuffer sbf=new StringBuffer();
		sbf.append("update sysdefinedict set delFlag=? WHERE id=? ");
		this.updateBySql(sbf.toString(),SysDefineDict.DEL_FLAG_DELETE,sysDefineDictId);
	}
	@Override
	public List<Sysdefinedict> getDefineDictByLabel(Integer companyId,
			String label, String type) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT id,label,value,type,defaultFlag,sort,description,companyId,createBy,createDate,updateBy,updateDate,");
		sbf.append("delFlag,remarks,uuid FROM sysdefinedict WHERE companyId=? AND label=? AND type=? AND delFlag=? ");
		List<Sysdefinedict> list=this.findBySql(sbf.toString(),Sysdefinedict.class,companyId,label,type,0);
		return list;
	}
	
	@Override
	public Sysdefinedict getDefineDictByiD(Long id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * from sysdefinedict where delFlag = 0 and id=?");
		List<Sysdefinedict> list=this.findBySql(sbf.toString(),Sysdefinedict.class,id);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getAgentType4Select() {
		
		String sql = "SELECT d.label, d.`value` FROM sys_dict d WHERE d.delFlag = '0' AND d.type = 't1_agent_type' ";
		List<Map<String, Object>> agentTypeList = this.findBySql(sql, Map.class);
		return agentTypeList;
	}
	
	@Override
	public List<Map<String, String>> getAgentParent4Select(Long agentId) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT a.id, a.agentName FROM agentinfo a WHERE a.is_quauq_agent = 1 AND a.delFlag = 0 AND a.agent_type <> 1 ")
			  .append(" AND a.agent_type <> -1 ");
		
		if (agentId != -2) {
			buffer.append(" AND a.id <> ").append(agentId)
			      .append(" AND a.agent_parent <> ").append(agentId);
		}
		
		List<Map<String, String>> agentParentList = this.findBySql(buffer.toString(), Map.class);
		return agentParentList;
	}

	@Override
	public Agentinfo getAgentInfoById(long id) {
		String sql = "SELECT * FROM agentinfo a where a.id = " + id;
		List<Agentinfo> list = findBySql(sql, Agentinfo.class);
		if(list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
}
