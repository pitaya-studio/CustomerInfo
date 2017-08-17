package com.trekiz.admin.agentToOffice.personnalInfo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.agentToOffice.agentInfo.dao.AgentInfoDao;
import com.trekiz.admin.agentToOffice.personnalInfo.dao.PersonInfoDao;
import com.trekiz.admin.agentToOffice.personnalInfo.exception.AgentException;
import com.trekiz.admin.agentToOffice.personnalInfo.service.PersonInfoService;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.utils.UserUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonInfoServiceImpl extends BaseService implements PersonInfoService{

	private static final Logger log = Logger.getLogger(PersonInfoServiceImpl.class);

	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Autowired
	private AgentInfoDao agentInfoDao;

	/**
	 * 获得渠道
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public Agentinfo getAgentInfoById(Long agentId) {
		if(agentId!=null){
			return personInfoDao.getAgentInfoById(agentId);
		}else{
			return personInfoDao.getAgentInfoById();
		}
	}
	
	/**
	 * 通过渠道id获得联系人
	 * @param agentId
	 * @param type
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<SupplyContacts> getSupplyContactsByAgentId(Integer agentId, Integer type) {
		List<SupplyContacts> list = personInfoDao.getSupplyContactsByAgentId(agentId, type);
		return list;
	}
	
	/**
	 * 修改基本信息中渠道的信息
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void updateInfoById(Agentinfo agentInfo) {
		Agentinfo info = personInfoDao.getAgentInfoById(agentInfo.getId());
		if(StringUtils.isNotEmpty(agentInfo.getAgentNameEn())){
			info.setAgentNameEn(agentInfo.getAgentNameEn());
		}
		if(agentInfo.getBelongsArea()!=null && agentInfo.getBelongsArea()!=-1){
			info.setBelongsArea(agentInfo.getBelongsArea());
		}
		if(agentInfo.getBelongsAreaProvince()!=null){
			info.setBelongsAreaProvince(agentInfo.getBelongsAreaProvince());
		}
		if(agentInfo.getBelongsAreaCity()==null){
			info.setBelongsAreaCity(agentInfo.getBelongsAreaCity());
		}
		if(StringUtils.isNotEmpty(agentInfo.getAgentAddress()) && !agentInfo.getAgentAddress().equals("-1")){
			info.setAgentAddress(agentInfo.getAgentAddress());
		}
		if(agentInfo.getAgentAddressProvince()!=null){
			info.setAgentAddressProvince(agentInfo.getAgentAddressProvince());
		}
		if(agentInfo.getAgentAddressCity()!=null){
			info.setAgentAddressCity(agentInfo.getAgentAddressCity());
		}
		if(agentInfo.getAgentAddressStreet()!=null){
			info.setAgentAddressStreet(agentInfo.getAgentAddressStreet());
		}
		if(StringUtils.isNotEmpty(agentInfo.getAgentTelAreaCode())){
			info.setAgentTelAreaCode(agentInfo.getAgentTelAreaCode());
		}
		if(StringUtils.isNotEmpty(agentInfo.getAgentTel())){
			info.setAgentTel(agentInfo.getAgentTel());
		}
		if(StringUtils.isNotEmpty(agentInfo.getAgentFaxAreaCode())){
			info.setAgentFaxAreaCode(agentInfo.getAgentFaxAreaCode());
		}
		if(StringUtils.isNotEmpty(agentInfo.getAgentFax())){
			info.setAgentFax(agentInfo.getAgentFax());
		}
		personInfoDao.updateObj(info);
		
	}
	
	@Override
	public Long saveOrUpdateContacts(String contactsStr) {
		JSONObject jsonObject = JSON.parseObject(contactsStr);
		Long contactsId = jsonObject.getLong("id");
		if(null != contactsId){
			SupplyContacts contacts=(SupplyContacts) personInfoDao.getSupplyContactsById(contactsId);
			contacts.setContactName(jsonObject.get("contactName").toString());
			contacts.setContactMobile(jsonObject.get("contactMobile").toString());
			contacts.setContactPhone(jsonObject.get("contactPhone").toString());
			contacts.setContactFax(jsonObject.get("contactFax").toString());
			contacts.setContactEmail(jsonObject.get("contactEmail").toString());
			contacts.setContactQQ(jsonObject.get("contactQQ").toString());
			contacts.setWechatCode(jsonObject.get("wechatCode").toString());
			contacts.setRemarks(jsonObject.get("remarks").toString());
			personInfoDao.updateObj(contacts);
		}else{
			SupplyContacts contacts = new SupplyContacts();
			contacts.setContactName(jsonObject.get("contactName").toString());
			contacts.setContactMobile(jsonObject.get("contactMobile").toString());
			contacts.setContactPhone(jsonObject.get("contactPhone").toString());
			contacts.setContactFax(jsonObject.get("contactFax").toString());
			contacts.setContactEmail(jsonObject.get("contactEmail").toString());
			contacts.setContactQQ(jsonObject.get("contactQQ").toString());
			contacts.setWechatCode(jsonObject.get("wechatCode").toString());
			contacts.setRemarks(jsonObject.get("remarks").toString());
			long agentId = UserUtils.getUser().getAgentId();
			contacts.setSupplierId(agentId);
			contacts.setType("0");
			super.setOptInfo(contacts, BaseService.OPERATION_ADD);
			personInfoDao.saveObj(contacts);
			contactsId = contacts.getId();
		}
		return contactsId;
	}
	
	/**
	 * 通过渠道id获得所有的银行帐号
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<PlatBankInfo> getPlatBankInfoByAgentId(Long agentId) {
		return personInfoDao.getPlatBankInfoByAgentId(agentId.intValue());
	}
	
	@Override
	public PlatBankInfo saveOrUpdateAgentBank(String bankJson) {
		JSONObject jsonObject = JSON.parseObject(bankJson);
		Long bankId = jsonObject.getLong("id");
		PlatBankInfo info = null;
		if(null != bankId){
			info = personInfoDao.getPlatBankInfoById(bankId);
			if(jsonObject.get("accountName") != null){
				info.setAccountName(jsonObject.get("accountName").toString());
			}
			if(jsonObject.get("bankName") != null){
				info.setBankName(jsonObject.get("bankName").toString());
			}
			if(jsonObject.get("bankAddr") != null){
				info.setBankAddr(jsonObject.get("bankAddr").toString());
			}
			if(jsonObject.get("bankAccountCode") != null){
				info.setBankAccountCode(jsonObject.get("bankAccountCode").toString());
			}
			if(jsonObject.get("remarks") != null){
				info.setRemarks(jsonObject.get("remarks").toString());
			}
			if(jsonObject.get("defaultFlag") != null){
				info.setDefaultFlag(jsonObject.get("defaultFlag").toString());
			}	
			info.setPlatType(2);
			if(jsonObject.get("accountPayType") != null){
				info.setAccountPayType(jsonObject.getInteger("accountPayType"));
			}	
			personInfoDao.updateObj(info);
		}else{
			info = new PlatBankInfo();
			if(jsonObject.get("accountName") != null){
				info.setAccountName(jsonObject.get("accountName").toString());
			}
			if(jsonObject.get("bankName") != null){
				info.setBankName(jsonObject.get("bankName").toString());
			}else{
				info.setBankName("");
			}
			if(jsonObject.get("bankAddr") != null){
				info.setBankAddr(jsonObject.get("bankAddr").toString());
			}
			if(jsonObject.get("bankAccountCode") != null){
				info.setBankAccountCode(jsonObject.get("bankAccountCode").toString());
			}
			if(jsonObject.get("remarks") != null){
				info.setRemarks(jsonObject.get("remarks").toString());
			}
			info.setDefaultFlag("1");			//新增时，非默认账户
			Long agentId = UserUtils.getUser().getAgentId();
			info.setBeLongPlatId(agentId);
			info.setPlatType(2);
			if(jsonObject.get("accountPayType") != null){
				info.setAccountPayType(jsonObject.getInteger("accountPayType"));
			}	
			personInfoDao.saveObj(info);
		}
		return info;
	}
	
	@Override
	public void saveOrUpdateQualication(String type, Long docId) {
		Agentinfo ag = personInfoDao.getAgentInfoById(UserUtils.getUser().getAgentId());
		if("A".equals(type)){
			ag.setBusinessLicense(docId);
		}else if("B".equals(type)){
			ag.setLicense(docId);
		}else if("C".equals(type)){
			ag.setTaxCertificate(docId);
		}else if("D".equals(type)){
			ag.setOrganizeCertificate(docId);
		}else if("E".equals(type)){
			ag.setIdCard(docId);
		}else if("F".equals(type)){
			ag.setBankOpenLicense(docId);
		}else if("G".equals(type)){
			ag.setTravelAptitudes(docId);
		}else if("H".equals(type)){
			ag.setElseFile(docId + "");
		}else {
			ag.setElseFile(docId + "");
		}
		personInfoDao.updateObj(ag);
	}

	@Override
	public void deleteQualication(String type, Long docId) {
		Agentinfo ag = personInfoDao.getAgentInfoById(UserUtils.getUser().getAgentId());
		if("A".equals(type)){
			ag.setBusinessLicense(null);
		}else if("B".equals(type)){
			ag.setLicense(null);
		}else if("C".equals(type)){
			ag.setTaxCertificate(null);
		}else if("D".equals(type)){
			ag.setOrganizeCertificate(null);
		}else if("E".equals(type)){
			ag.setIdCard(null);
		}else if("F".equals(type)){
			ag.setBankOpenLicense(null);
		}else if("G".equals(type)){
			ag.setTravelAptitudes(null);
		}else if("H".equals(type)){
			ag.setElseFile(null);
		}else {
			ag.setElseFile(null);
		}
		personInfoDao.updateObj(ag);
		DocInfo doc=(DocInfo)personInfoDao.getDocById(docId);
		doc.setDelFlag("1");
		personInfoDao.updateObj(doc);
	}

	/**
	 * 删除资质
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void deleteFiles(Long id) {
		DocInfo doc=(DocInfo)personInfoDao.getDocById(id);
		doc.setDelFlag("1");
		personInfoDao.updateObj(doc);
	}

	@Override
	public Area getCountryById(Long id) {
		return personInfoDao.getCountryById(id);
	}

	@Override
	public void deleteContacts(Long id) {
		try {
			SupplyContacts contacts = personInfoDao.getSupplyContactsById(id);
			contacts.setDelFlag("1");
			personInfoDao.updateObj(contacts);
		}catch (Exception e){
			log.error("删除联系人信息失败", e);
			throw new AgentException("删除联系人信息失败");
		}
	}
	
	/**
	 * 通过id获得资质
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public DocInfo getDocInfoById(Long id) {
		DocInfo docById = personInfoDao.getDocById(id);
		return docById;
	}

	@Override
	public void deleteAgentBank(Long id) {
		PlatBankInfo info = personInfoDao.getPlatBankInfoById(id);
		info.setDelFlag("1");
		personInfoDao.updateObj(info);
	}

	@Override
	public void setDefaultAgentBank(Long id) {
		PlatBankInfo info = personInfoDao.getPlatBankInfoById(id);
		if(null == info){
			return;
		}
		List<PlatBankInfo> agentBanks = personInfoDao.getPlatBankInfoByAgentId(info.getBeLongPlatId().intValue());
		//该渠道下的所有银行账号都设置为非默认账户
		for (PlatBankInfo bank : agentBanks){
			bank.setDefaultFlag("1");
			personInfoDao.updateObj(bank);
		}
		//当前账户设置为默认账户
		info.setDefaultFlag("0");
		personInfoDao.updateObj(info);
	}

	@Override
	public List<PlatBankInfo> getPlatBankInfoByAgentIdForT1T2(
			Integer beLongPlatId) {
		List<PlatBankInfo> list = personInfoDao.getPlatBankInfoByAgentIdForT1T2(beLongPlatId);
		return list;
	}
	
	
}
