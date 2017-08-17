package com.trekiz.admin.modules.supplier.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierContactsView;
import com.trekiz.admin.modules.supplier.repository.SupplierContactsDao;

@Service("supplierContactsService")
@Transactional(readOnly = true)
public class SupplierContactsService {

	@Autowired
	private SupplierContactsDao supplierContactsDao;
	@Autowired
	private AgentinfoService agentinfoService;
	
	/**
	 * 根据联系人Id查询联系人
	 * @param supplierContacts
	 */
	public SupplierContacts findContactsById(Long id){
		SupplierContacts supplierContacts = supplierContactsDao.findOne(id);
		return supplierContacts;
	}

	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void saveContacts(SupplierContacts supplierContacts) {
		supplierContactsDao.save(supplierContacts);
	}

	/**
	 * 根据地接社查找其联系人
	 * @param ai
	 * @return
	 */
	public List<SupplierContacts> findContactsBySupplierInfo(Long id) {
		List<SupplierContacts> list = new ArrayList<SupplierContacts>();
		try {
			list = supplierContactsDao.findContactsBySupplierInfoId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据渠道商查找其联系人
	 * @param ai  
	 * @return
	 */
	public List<SupplierContacts> findContactsByAgentInfo(Long id) {
		List<SupplierContacts> list = new ArrayList<SupplierContacts>();
		try {
			list = supplierContactsDao.findNormalContactsByAgentInfoId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 删除地接社联系人
	 * @param id
	 * @return
	 */
	public boolean deleteSupplierContacts(Long id){
		return supplierContactsDao.deleteSupplierContacts(id);
	}

	/**
	 * 根据渠道商查找所有的联系人（包括第一联系人）
	 * @param parseLong
	 * @return 
	 */
	public List<SupplierContacts> findAllContactsByAgentInfo(Long id) {
		List<SupplierContacts> list = new ArrayList<SupplierContacts>();
		//获取agentInfo（渠道商）
		Agentinfo agentinfo = agentinfoService.findAgentInfoById(id);
		//组织第一联系人（它在agentInfo表中，故而虚拟一个entity）
		if (agentinfo != null) {
			// 签约渠道第一联系人保存在agentinfo中	
			if (!Context.QUAUQ_AGENT_YES.equals(agentinfo.getIsQuauqAgent())) {
				SupplierContacts firstContact = new SupplierContacts();
				firstContact.setId((long)0);  //id
				firstContact.setContactName(agentinfo.getAgentContact());  //name
				firstContact.setContactMobile(agentinfo.getAgentContactMobile());  //手机
				firstContact.setContactPhone(agentinfo.getAgentContactTel()); //电话
				firstContact.setContactFax(agentinfo.getAgentContactFax());  //传真
				firstContact.setContactEmail(agentinfo.getAgentContactEmail());   //E邮箱
				firstContact.setContactQQ(agentinfo.getAgentContactQQ());   //qq
				firstContact.setRemarks(agentinfo.getRemarks());   //备注
				//添加第一联系人
				list.add(firstContact);
			} else {
				// quauq渠道的第一联系人也是保存在supplier_contact中
			}
		}
		//添加其他联系人
		list.addAll(findContactsByAgentInfo(id));
		return list;
	}

	/**
	 * 由联系人实体集合转换为 JSON 字符串
	 * @param contacts
	 * @return
	 */
	public String contacts2Json(List<SupplierContacts> contacts) {
		org.json.JSONArray results = new org.json.JSONArray();
		org.json.JSONObject resobj = new org.json.JSONObject();
		String resultString = "[";
		for (SupplierContacts supplierContacts : contacts) {			
			try {
				resobj = objectToJson(supplierContacts);
				String everyContact = resobj.toString();
				everyContact = everyContact.replace("id", "uuid");
				everyContact = everyContact.replace("contactName", "text");
				results.put(resobj);
				
				resultString += (resultString.length() == 1 ? "" : ",") + everyContact;
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//渠道商的联系地址 (暂时未使用)
		if(CollectionUtils.isNotEmpty(contacts)) {
			String address = agentinfoService.getAddressStrById(contacts.get(0).getSupplierId());
		}
		
		return resultString + "]";
	}
	
	/**
	 * 由联系人实体集合转换为 jsonArray
	 * @param contacts
	 * @return
	 */
	public org.json.JSONArray contacts2JsonArray4View(List<SupplierContactsView> contactsView) {
		org.json.JSONArray results = new org.json.JSONArray();
		org.json.JSONObject resobj = new org.json.JSONObject();
//		String address = agentinfoService.getAddressStrById(contactsView.get(0).getSupplierId());
		for (SupplierContactsView supplierContactsView : contactsView) {
//			supplierContactsView.setAgentAddressFull(address);
			try {
				
				resobj = objectToJson(supplierContactsView);
				String everyContact = resobj.toString();
				everyContact = everyContact.replace("id", "uuid");
				everyContact = everyContact.replace("contactName", "text");
				resobj = new org.json.JSONObject(everyContact);
				
				results.put(resobj);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	/**
	 * 由联系人实体集合转换为 jsonArray
	 * @param contacts
	 * @return
	 */
	public org.json.JSONArray contacts2JsonArray(List<SupplierContacts> contacts) {
		org.json.JSONArray results = new org.json.JSONArray();
		org.json.JSONObject resobj = new org.json.JSONObject();
		for (SupplierContacts supplierContacts : contacts) {
			try {
				resobj = objectToJson(supplierContacts);
				String everyContact = resobj.toString();
				everyContact = everyContact.replace("id", "uuid");
				everyContact = everyContact.replace("contactName", "text");
				resobj = new org.json.JSONObject(everyContact);
				results.put(resobj);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	/**
     * 将实体POJO转化为JSON
     * @param obj
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static<T> JSONObject objectToJson(T obj) throws JSONException, IOException {
        ObjectMapper mapper = new ObjectMapper();  
        // Convert object to JSON string  
        String jsonStr = "";
        try {
             jsonStr =  mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw e;
        }
        return new JSONObject(jsonStr);
    }

    /**
     * 由联系人实体集合转换为 jsonList
     * @param contacts
     * @return
     */
	public List<String> contacts2JsonList(List<SupplierContacts> contacts) {
		List<String> contactList = Lists.newArrayList();
		//渠道商的联系地址 (暂时未使用)
		String address = agentinfoService.getAddressStrById(contacts.get(0).getSupplierId());
		org.json.JSONObject resobj = new org.json.JSONObject();
		for (SupplierContacts supplierContacts : contacts) {
			supplierContacts.setAgentAddressFull(address);
			try {
				resobj = objectToJson(supplierContacts);
				contactList.add(resobj.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//渠道商的联系地址 (暂时未使用)
		return contactList;
	}
	
}
