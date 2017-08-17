package com.trekiz.admin.modules.agent.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.entity.SupplyContacts;
import com.trekiz.admin.modules.agent.repository.SupplyContactsDao;

@Service("supplyContactsService")
@Transactional(readOnly = true)
public class SupplyContactsService {
	
	@Autowired
	private SupplyContactsDao supplyContactsDao;
	
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public SupplyContacts save(SupplyContacts supplyContacts) {
		SupplyContacts sup = supplyContactsDao.save(supplyContacts);
		return sup;
	}
	/**
	 * 根据渠道商查找其联系人
	 * @param ai
	 * @return
	 */
	public List<SupplyContacts> findContactsByAgentInfo(Long id) {
		List<SupplyContacts> list = new ArrayList<SupplyContacts>();
		try {
			list = supplyContactsDao.findContactsByAgentInfoIdWithDelflag(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 根据联系人ID查找
	 * @author gao
	 * @param id
	 * @return
	 */
	public SupplyContacts findOne(Long id){
		SupplyContacts sup = new SupplyContacts();
		try {
			sup = supplyContactsDao.findOne(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sup;
	}
	/**
	 * 添加/修改 联系人
	 * @author gao
	 * @param sup
	 * @return
	 */
	public SupplyContacts update(SupplyContacts sup){
		SupplyContacts back = new SupplyContacts();
		back =supplyContactsDao.save(sup);
		return back;
	}
	/**
	 * 根据平台商人ID和平台类型查询
	 * @author gao
	 * @return
	 */
	public List<SupplyContacts> findSupplyContactsByIdAType(Long supplierId,String type){
		List<SupplyContacts> list = new ArrayList<SupplyContacts>();
		try {
			list = supplyContactsDao.findSupplyContactsByIdAType(supplierId,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 智能更新联系人（自动判断并新增、删除、更新）
	 * @param agentInfo 渠道
	 * @param contacts 新联系人信息
	 */
	public void updateAgentContactAI(Agentinfo agentInfo, List<SupplyContacts> contacts) {
		List<SupplyContacts> listDB = new ArrayList<SupplyContacts>();
		listDB = supplyContactsDao.findContactsByAgentInfoIdWithDelflag(agentInfo.getId());  // 渠道原有联系人
		// 待新增的联系人
		List<SupplyContacts> tobeInsert = new ArrayList<>();
		// 待更新的联系人原信息
		List<SupplyContacts> tobeUpdate = new ArrayList<>();
		// 待删除的联系人
		List<SupplyContacts> tobeDelete = new ArrayList<>();
		
		// 比较得出对应联系人ID
		if (CollectionUtils.isEmpty(listDB) && CollectionUtils.isNotEmpty(contacts)) {  // 无旧联系人，全部新增
			tobeInsert.addAll(contacts);
		} else if (CollectionUtils.isNotEmpty(listDB) && CollectionUtils.isNotEmpty(contacts)) {  // 新旧联系人都存在 			
			// 待更新联系人id 集合
			List<String> tobeUpdateIDs = new ArrayList<>();
			// 待更新联系人的新信息		
			List<SupplyContacts> info4Update = new ArrayList<>();
			for (SupplyContacts newContacts : contacts) {
				if (newContacts.getId() != null) {
					tobeUpdateIDs.add(newContacts.getId().toString());
					info4Update.add(newContacts);
				} else {
					newContacts.setSupplierId(agentInfo.getId());
					newContacts.setType("0");
					tobeInsert.add(newContacts);
				}
			}
			// 待更新联系人旧信息
			for (String updateID : tobeUpdateIDs) {
				for (SupplyContacts dbContact : listDB) {
					if (dbContact.getId().toString().equals(updateID)) {
						tobeUpdate.add(dbContact);
						continue;
					}
				}
			}
			// 待删除联系人取余集
			listDB.removeAll(tobeUpdate);
			tobeDelete.addAll(listDB);
			listDB.addAll(tobeUpdate);
			// 填充待更新联系人信息
			for (SupplyContacts baseObj : tobeUpdate) {
				for (SupplyContacts infoObj : info4Update) {
					if (baseObj.getId().compareTo(infoObj.getId()) == 0) {
						baseObj.setContactName(infoObj.getContactName());  		// 联系人名字
						baseObj.setContactMobile(infoObj.getContactMobile());  	// 联系人电话
						baseObj.setContactPhone(infoObj.getContactPhone());		// 联系人固定电话
						baseObj.setContactFax(infoObj.getContactFax());			// 联系人传真
						baseObj.setContactEmail(infoObj.getContactEmail());		// 联系人电子邮箱
						baseObj.setContactQQ(infoObj.getContactQQ());			// 联系人QQ
						baseObj.setWechatCode(infoObj.getWechatCode());			// 联系人微信
						baseObj.setRemarks(infoObj.getRemarks());				// 备注
						continue;
					}
				}
			}
		} else {  // 无新联系人，不做操作
			return;
		}
		// 批量执行
		if (CollectionUtils.isNotEmpty(tobeInsert)) {			
			supplyContactsDao.batchSave(tobeInsert);
		}
		if (CollectionUtils.isNotEmpty(tobeDelete)) {			
			supplyContactsDao.batchDelete(tobeDelete);
		}
		if (CollectionUtils.isNotEmpty(tobeUpdate)) {			
			supplyContactsDao.batchUpdate(tobeUpdate);
		}
	}
}
