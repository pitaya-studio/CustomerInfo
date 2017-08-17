package com.trekiz.admin.agentToOffice.personnalInfo.dao;

import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.DocInfo;

public interface PersonInfoDao extends BaseDao {
	/**
	 * 通过id获得渠道
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public Agentinfo getAgentInfoById();
	
	/**
	 * 通过渠道id获得联系人
	 * @param agentId
	 * @param type
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<SupplyContacts> getSupplyContactsByAgentId(Integer agentId,Integer type);
	
	/**
	 * 通过渠道id获得所有的银行帐号
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<PlatBankInfo> getPlatBankInfoByAgentId(Integer beLongPlatId);
	
	public Agentinfo getAgentInfoById(Long id);
	
	public SupplyContacts getSupplyContactsById(Long id);
	
	public PlatBankInfo getPlatBankInfoById(Long id);
	
	public DocInfo getDocById(Long id);
	
	public Area getCountryById(Long id);
	
	/**
	 * 查询该渠道下的银行信息（包括已删除的，用于t1的订单详情页）
	 * @param beLongPlatId
	 * @return
	 */
	public List<PlatBankInfo> getPlatBankInfoByAgentIdForT1T2(Integer beLongPlatId);
}
