package com.trekiz.admin.agentToOffice.personnalInfo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.personnalInfo.dao.PersonInfoDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
public class PersonInfoDaoImpl extends BaseDaoImpl implements PersonInfoDao{
	
	/**
	 * 获得渠道
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public Agentinfo getAgentInfoById() {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM agentinfo WHERE delFlag=? AND id=?");
		List<Agentinfo> list=this.findBySql(sbf.toString(), Agentinfo.class,0,UserUtils.getUser().getAgentId());
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	public Agentinfo getAgentInfoById(Long id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM agentinfo WHERE delFlag=? AND id=?");
		List<Agentinfo> list=this.findBySql(sbf.toString(), Agentinfo.class,0,id);
		return list.get(0);
	}
	/**
	 * 通过渠道id获得联系人
	 * @param agentId
	 * @param type
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<SupplyContacts> getSupplyContactsByAgentId(Integer agentId,
			Integer type) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM supplier_contacts ");
		sbf.append("WHERE supplierId=? AND type=? AND delFlag=? order by id");
		List<SupplyContacts> list=this.findBySql(sbf.toString(),SupplyContacts.class, agentId,type,0);
		return list;
	}

	@Override
	public List<PlatBankInfo> getPlatBankInfoByAgentId(Integer beLongPlatId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM plat_bank_info WHERE beLongPlatId=? AND delFlag=? ");
		//sbf.append("AND accountPayType IS NOT NULL ");
		List<PlatBankInfo> list=this.findBySql(sbf.toString(),PlatBankInfo.class, beLongPlatId,0);
		return list;
	}
	@Override
	public SupplyContacts getSupplyContactsById(Long id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM supplier_contacts WHERE id=? AND delFlag=? ");
		List<SupplyContacts> list=this.findBySql(sbf.toString(),SupplyContacts.class, id,0);
		return list.get(0);
	}
	@Override
	public PlatBankInfo getPlatBankInfoById(Long id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM plat_bank_info WHERE id=? AND delFlag=? ");
		List<PlatBankInfo> list=this.findBySql(sbf.toString(),PlatBankInfo.class, id,0);
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	@Override
	public DocInfo getDocById(Long id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM docinfo WHERE id=? AND delFlag=? ");
		List<DocInfo> list=this.findBySql(sbf.toString(),DocInfo.class, id,0);
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	@Override
	public Area getCountryById(Long id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM sys_area WHERE id=? AND delFlag=?");
		List<Area> list=this.findBySql(sbf.toString(),Area.class, id,0);
		return list.get(0);
	}
	
	/**
	 * 查询该渠道下的银行信息（包括已删除的，用于t1的订单详情页）
	 * @param beLongPlatId
	 * @return
	 */
	@Override
	public List<PlatBankInfo> getPlatBankInfoByAgentIdForT1T2(Integer beLongPlatId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM plat_bank_info WHERE beLongPlatId=? ");
		List<PlatBankInfo> list=this.findBySql(sbf.toString(),PlatBankInfo.class, beLongPlatId);
		return list;
	}
	
}
