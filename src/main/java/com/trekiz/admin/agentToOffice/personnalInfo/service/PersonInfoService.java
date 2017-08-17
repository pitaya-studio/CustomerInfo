package com.trekiz.admin.agentToOffice.personnalInfo.service;

import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.DocInfo;

import java.util.List;

public interface PersonInfoService {
	/**
	 * 获得渠道
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public Agentinfo getAgentInfoById(Long agentId);
	
	/**
	 * 通过渠道id获得联系人
	 * @param agentId
	 * @param type
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<SupplyContacts> getSupplyContactsByAgentId(Integer agentId,Integer type);
	
	/**
	 * 修改基本信息中渠道的信息
	 * @author chao.zhang@quauq.com
	 */
	public void updateInfoById(Agentinfo agentInfo);

	/**
	 * 保存或者新曾联系人
	 * @param contacts	联系人对象
	 * @return			新增或者修改的联系人ID
	 * @author shijun.liu
	 * @date   2016.06.18
     */
	public Long saveOrUpdateContacts(String contacts);
	
	/**
	 * 通过渠道id获得所有的银行帐号
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<PlatBankInfo> getPlatBankInfoByAgentId(Long agentId);

	/**
	 * 保存渠道银行账户信息
	 * @param bankJson    银行信息
	 * @return
	 * @author  shijun.liu
	 * @date    2016.06.18
     */
	public PlatBankInfo saveOrUpdateAgentBank(String bankJson);

	/**
	 * 保存资质
	 * @param type A:营业执照，B:经营许可证，C:税务登记证，D:组织结构代码，E：公司法人身份证，F:公司银行开户许可证，G:旅游业资质，H:其他资质
	 * @param docId
	 * @author shijun.liu
	 * @date   2016.06.20
     */
	public void saveOrUpdateQualication(String type, Long docId);

	/**
	 * 删除资质信息
	 * @param type A:营业执照，B:经营许可证，C:税务登记证，D:组织结构代码，E：公司法人身份证，F:公司银行开户许可证，G:旅游业资质，H:其他资质
	 * @param docId
	 * @author shijun.liu
	 * @date   2016.06.27
	 */
	public void deleteQualication(String type, Long docId);
	
	/**
	 * 删除资质
	 * @author chao.zhang@quauq.com
	 */
	public void deleteFiles(Long id);
	
	public Area getCountryById(Long id);
	
	public void deleteContacts(Long id);
	
	public DocInfo getDocInfoById(Long id);

	/**
	 * 删除渠道银行信息
	 * @param id
	 * @author	shijun.liu
	 * @date    2016.06.20
     */
	public void deleteAgentBank(Long id);

	/**
	 * 设置为渠道的默认账户
	 * @param id
	 * @author	shijun.liu
	 * @date 	2016.06.20
     */
	public void setDefaultAgentBank(Long id);
	
	/**
	 * 查询该渠道下的银行信息（包括已删除的，用于t1的订单详情页）
	 * @param beLongPlatId
	 * @return
	 */
	public List<PlatBankInfo> getPlatBankInfoByAgentIdForT1T2(Integer beLongPlatId);
}
