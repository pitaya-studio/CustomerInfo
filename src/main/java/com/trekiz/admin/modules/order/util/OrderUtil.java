package com.trekiz.admin.modules.order.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.trekiz.admin.common.config.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.traveler.entity.TravelerFile;
import com.trekiz.admin.modules.traveler.entity.TravelerFileEnum;

/**
 * 功能：订单模块公用工具类
 * @author jianghaili
 *
 */
public class OrderUtil {

	/**
	 * 组装订单联系人数据
	 * 根据JSON数组格式获得 List<OrderContacts> 格式的 数据
	 * @param orderContactsJSON
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static List<OrderContacts>  getContactsList(String orderContactsJSON) throws JSONException {
		List<OrderContacts> contactsList = new ArrayList<OrderContacts>();
    	JSONArray datatravelerArray = new JSONArray(orderContactsJSON);
        int len = datatravelerArray.length();
        if(len > 0){
        	for(int i=0;i<len;i++){
        		JSONObject ordercontacts = datatravelerArray.getJSONObject(i);
//        		JSONObject ordercontacts = obj.getJSONObject("ordercontacts");
        		Iterator<String> it = ordercontacts.keys();
        		List<String> keyList = new ArrayList<String>();
        		while(it.hasNext()) {
        			keyList.add(it.next());
        		}
        		//处理旅行者
        		OrderContacts contacts = new OrderContacts();
        		{
        			//联系人名称
        			Object contactsName = ordercontacts.get("contactsName");
        			if(contactsName!=null&&StringUtils.isNotBlank(contactsName.toString())){
        				contacts.setContactsName(contactsName.toString());
        			}
        			//联系人电话
        			Object contactsTel = ordercontacts.get("contactsTel");
        			if(contactsTel!=null&&StringUtils.isNotBlank(contactsTel.toString())){
        				contacts.setContactsTel(contactsTel.toString());
        			}
        			//联系人固定电话
        			Object contactsTixedTel = ordercontacts.get("contactsTixedTel");
        			if(contactsTixedTel!=null&&StringUtils.isNotBlank(contactsTixedTel.toString())){
        				contacts.setContactsTixedTel(contactsTixedTel.toString());
        			}
        			//联系人地址
        			Object contactsAddress = ordercontacts.get("contactsAddress");
        			if(contactsAddress!=null&&StringUtils.isNotBlank(contactsAddress.toString())){
        				contacts.setContactsAddress(contactsAddress.toString());
        			}
        			//联系人传真
        			Object contactsFax = ordercontacts.get("contactsFax");
        			if(contactsFax!=null&&StringUtils.isNotBlank(contactsFax.toString())){
        				contacts.setContactsFax(contactsFax.toString());
        			}
        			//联系人QQ
        			Object contactsQQ = ordercontacts.get("contactsQQ");
        			if(contactsQQ!=null&&StringUtils.isNotBlank(contactsQQ.toString())){
        				contacts.setContactsQQ(contactsQQ.toString());
        			}
        			//联系人邮箱
        			Object contactsEmail = ordercontacts.get("contactsEmail");
        			if(contactsEmail!=null&&StringUtils.isNotBlank(contactsEmail.toString())){
        				contacts.setContactsEmail(contactsEmail.toString());
        			}
        			//联系人邮编
        			Object contactsZipCode = ordercontacts.get("contactsZipCode");
        			if(contactsZipCode!=null&&StringUtils.isNotBlank(contactsZipCode.toString())){
        				contacts.setContactsZipCode(contactsZipCode.toString());
        			}
        			//备注
        			Object remark = ordercontacts.get("remark");
        			if(remark!=null&&StringUtils.isNotBlank(remark.toString())){
        				contacts.setRemark(remark.toString());
        			}
        		}  
        		contactsList.add(contacts);
        	}
        }
        
        return contactsList;
	}
	
	/**
	 * 附件vo对象组装
	 * @param fileType			附件类型
	 * @param srcTravelerId		游客id
	 * @param srcDocId			附件表id
	 * @param fileName			附件原名称
	 * @return
	 */
	public static TravelerFile getTravelerFile(Integer fileType,Long srcTravelerId,Long srcDocId,String fileName){
		TravelerFile travelerFile = new TravelerFile();
		travelerFile.setFileType(fileType);
		travelerFile.setSrcTravelerId(srcTravelerId);
		travelerFile.setSrcDocId(srcDocId);
		// 限制文件名称超过50个字以内
		travelerFile.setFileName(fileName.length() > 50 ? fileName.substring(0, 49) : fileName);
		return travelerFile;
		
	}
	
	/**
	 * 附件集合对象封装
	 * @param jsonObject	包含附件信息的 游客json
	 * @param srcTravelerId	游客id
	 * @return
	 */
	public static List<TravelerFile> getTravelerFileList(net.sf.json.JSONObject jsonObject, Long srcTravelerId){
		
		//护照
		String passportfile = null;
		String[] passportfiles = null;
		if(jsonObject.containsKey("passportfile")){
			passportfile = jsonObject.getString("passportfile");
		}
		if(passportfile.split(";").length > 1) {
			passportfiles = passportfile.split(";");
			passportfile = null;
		}
		//身份证正面/个人资料表
		String idcardfrontfile = null;
		String[] idcardfrontFiles = null;
		if(jsonObject.containsKey("idcardfrontfile")){
			idcardfrontfile = jsonObject.getString("idcardfrontfile");
		}
		if(idcardfrontfile.split(";").length > 1) {
			idcardfrontFiles = idcardfrontfile.split(";");
			idcardfrontfile = null;
		}
		//申请表格/担保书
		String entryformfile = null;
		String[] entryformfiles = null;
		if(jsonObject.containsKey("entryformfile")){
			entryformfile = jsonObject.getString("entryformfile");
		}
		if(entryformfile.split(";").length > 1) {
			entryformfiles = entryformfile.split(";");
			entryformfile = null;
		}
		//房产证
		String housefile = null;
		String[] housefiles = null;
		if(jsonObject.containsKey("housefile")){
			housefile = jsonObject.getString("housefile");
		}
		if(housefile != null && housefile.split(";").length > 1) {
			housefiles = housefile.split(";");
			housefile = null;
		}
		//电子照片/参团告知书
		String photofile = null;
		String[] photofiles = null;
		if(jsonObject.containsKey("photofile")){
			photofile = jsonObject.getString("photofile");
		}
		if(photofile.split(";").length > 1) {
			photofiles = photofile.split(";");
			photofile = null;
		}

		//身份证反面/健康承诺书
		String idcardbackfile = null;
		String[] idcardbackfiles = null;
		if(jsonObject.containsKey("idcardbackfile")){
			idcardbackfile = jsonObject.getString("idcardbackfile");
		}
		if(idcardbackfile.split(";").length > 1) {
			idcardbackfiles = idcardbackfile.split(";");
			idcardbackfile = null;
		}
		//户口本
		String residencefile = null;
		String[] residencefiles = null;
		if(jsonObject.containsKey("residencefile")){
			residencefile = jsonObject.getString("residencefile");
		}
		if(residencefile != null && residencefile.split(";").length > 1) {
			residencefiles = residencefile.split(";");
			residencefile = null;
		}

		//其他
		String otherfile = null;
		String[] otherfiles = null;
		if(jsonObject.containsKey("otherfile")){
			otherfile = jsonObject.getString("otherfile");
		}
		if(otherfile.split(";").length > 1) {
			otherfiles = otherfile.split(";");
			otherfile = null;
		}

		//签证附件
		String visaannexfile = "";
		String[] visaannexfiles = null;
		if(jsonObject.containsKey("visaannexfile")){
			visaannexfile = jsonObject.getString("visaannexfile");
		}
		if(visaannexfile.split(";").length > 1){
			visaannexfiles = visaannexfile.split(";");
			visaannexfile = null;
		}

		/**
		 * 组装 附件集合
		 */
		List<TravelerFile> list = new ArrayList<TravelerFile>();
		//护照
		if(StringUtils.isNotBlank(passportfile) && jsonObject.containsKey("passportdocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("passportdocID");
			//附件原名称
			String docName = jsonObject.getString("passportdocName");
			//附件path
//			String docPath = jsonObject.getString("passportdocPath");
			travelerFile = getTravelerFile(TravelerFile.PASSPORTS_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		if(passportfiles != null && passportfiles.length > 0 && jsonObject.containsKey("passportdocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("passportdocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("passportdocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.PASSPORTS_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//身份证正面/个人资料表
		if(StringUtils.isNotBlank(idcardfrontfile) && jsonObject.containsKey("idcardfrontdocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("idcardfrontdocID");
			//附件原名称
			String docName = jsonObject.getString("idcardfrontdocName");
			//附件path
//			String docPath = jsonObject.getString("idcardfrontdocPath");
			travelerFile = getTravelerFile(TravelerFile.IDCARD_FRONT_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		//身份证正面/个人资料表
		if(idcardfrontFiles != null && idcardfrontFiles.length > 0 && jsonObject.containsKey("idcardfrontdocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("idcardfrontdocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("idcardfrontdocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.IDCARD_FRONT_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//申请表格/担保书
		if(StringUtils.isNotBlank(entryformfile) && jsonObject.containsKey("entryformdocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("entryformdocID");
			//附件原名称
			String docName = jsonObject.getString("entryformdocName");
			//附件path
//			String docPath = jsonObject.getString("entryformdocPath");
			travelerFile = getTravelerFile(TravelerFile.ENTRY_FORM_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		//申请表格/担保书
		if(entryformfiles != null && entryformfiles.length > 0 && jsonObject.containsKey("entryformdocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("entryformdocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("entryformdocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.ENTRY_FORM_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//房产证
		if(StringUtils.isNotBlank(housefile) && jsonObject.containsKey("housedocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("housedocID");
			//附件原名称
			String docName = jsonObject.getString("housedocName");
			//附件path
//			String docPath = jsonObject.getString("housedocPath");
			travelerFile = getTravelerFile(TravelerFile.HOUSE_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		//房产证
		if(housefiles != null && housefiles.length > 0 && jsonObject.containsKey("housedocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("housedocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("housedocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.HOUSE_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//电子照片/参团告知书
		if(StringUtils.isNotBlank(photofile) && jsonObject.containsKey("photodocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("photodocID");
			//附件原名称
			String docName = jsonObject.getString("photodocName");
			//附件path
//			String docPath = jsonObject.getString("photodocPath");
			travelerFile = getTravelerFile(TravelerFile.PHOTO_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		//电子照片/参团告知书
		if(photofiles != null && photofiles.length > 0 && jsonObject.containsKey("photodocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("photodocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("photodocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.PHOTO_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//身份证反面/健康承诺书
		if(StringUtils.isNotBlank(idcardbackfile) && jsonObject.containsKey("idcardbackdocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("idcardbackdocID");
			//附件原名称
			String docName = jsonObject.getString("idcardbackdocName");
			//附件path
//			String docPath = jsonObject.getString("idcardbackdocPath");
			travelerFile = getTravelerFile(TravelerFile.IDCARD_BACK_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		//身份证反面/健康承诺书
		if(idcardbackfiles != null && idcardbackfiles.length > 0 && jsonObject.containsKey("idcardbackdocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("idcardbackdocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("idcardbackdocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.IDCARD_BACK_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//户口本
		if(StringUtils.isNotBlank(residencefile) && jsonObject.containsKey("residencedocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("residencedocID");
			//附件原名称
			String docName = jsonObject.getString("residencedocName");
			//附件path
//			String docPath = jsonObject.getString("residencedocPath");
			travelerFile = getTravelerFile(TravelerFile.RESIDENCE_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		//户口本
		if(residencefiles != null && residencefiles.length > 0 && jsonObject.containsKey("residencedocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("residencedocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("residencedocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.RESIDENCE_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//其他
		if(StringUtils.isNotBlank(otherfile) && jsonObject.containsKey("otherdocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("otherdocID");
			//附件原名称
			String docName = jsonObject.getString("otherdocName");
			//附件path
//			String docPath = jsonObject.getString("otherdocPath");
			travelerFile = getTravelerFile(TravelerFile.OTHER_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		//其他
		if(otherfiles != null && otherfiles.length > 0 && jsonObject.containsKey("otherdocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("otherdocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("otherdocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.OTHER_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}

		//签证附件
		if(StringUtils.isNotBlank(visaannexfile) && jsonObject.containsKey("visaannexdocID")){
			TravelerFile travelerFile = new TravelerFile();
			//附件表id
			String docID = jsonObject.getString("visaannexdocID");
			//附件原名称
			String docName = jsonObject.getString("visaannexdocName");
			//附件path
//			String docPath = jsonObject.getString("otherdocPath");
			travelerFile = getTravelerFile(TravelerFile.VISA_TYPE, srcTravelerId, Long.parseLong(docID), docName);
			list.add(travelerFile);
		}
		//签证附件
		if(visaannexfiles != null && visaannexfiles.length > 0 && jsonObject.containsKey("visaannexdocID")){
			//附件表id
			net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("visaannexdocID");
			//附件原名称
			net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("visaannexdocName");
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.VISA_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
						docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		
		return list;
	}
	
	/**
	 * 附件集合对象封装
	 * @param jsonObject	包含附件信息的 游客json
	 * @param srcTravelerId	游客id
	 * @return
	 */
	public static List<TravelerFile> getTravelerFileList4ZBQ(net.sf.json.JSONObject jsonObject, Long srcTravelerId){
		/** 组装 附件集合  */
		List<TravelerFile> list = new ArrayList<TravelerFile>();
		//护照
		if(jsonObject.containsKey("passportdocID")){
			// 判断是 object还是array
			net.sf.json.JSONArray docIdArr  = new net.sf.json.JSONArray();
			net.sf.json.JSONArray docNameArr  = new net.sf.json.JSONArray();
			//  ID
			Object tempJson = new net.sf.json.util.JSONTokener(jsonObject.get("passportdocID").toString()).nextValue(); 
			if(tempJson instanceof net.sf.json.JSONArray){
				docIdArr.addAll(jsonObject.getJSONArray("passportdocID"));
			} else {
				docIdArr.add(jsonObject.getString("passportdocID"));
			}
			// NAME
			tempJson = new net.sf.json.util.JSONTokener(jsonObject.get("passportdocName").toString()).nextValue();
			if(tempJson instanceof net.sf.json.JSONArray){
				docNameArr.addAll(jsonObject.getJSONArray("passportdocName"));
			} else {
				docNameArr.add(jsonObject.getString("passportdocName"));
			}
			//
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.FILETYPE_ZBQ_PASSPORT_HOMEPAGE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()), docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//其他
		if(jsonObject.containsKey("otherdocID")){
			net.sf.json.JSONArray docIdArr  = new net.sf.json.JSONArray();
			net.sf.json.JSONArray docNameArr  = new net.sf.json.JSONArray();
			// 
			Object tempJson = new net.sf.json.util.JSONTokener(jsonObject.get("otherdocID").toString()).nextValue(); 
			if(tempJson instanceof net.sf.json.JSONArray){
				docIdArr.addAll(jsonObject.getJSONArray("otherdocID"));
			} else {
				docIdArr.add(jsonObject.getString("otherdocID"));
			}
			//
			tempJson = new net.sf.json.util.JSONTokener(jsonObject.get("otherdocName").toString()).nextValue();
			if(tempJson instanceof net.sf.json.JSONArray){
				docNameArr.addAll(jsonObject.getJSONArray("otherdocName"));
			} else {
				docNameArr.add(jsonObject.getString("otherdocName"));
			}
			//
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.FILETYPE_ZBQ_OTHER, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()), docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		//签证附件
		if(jsonObject.containsKey("visaannexdocID")){
			net.sf.json.JSONArray docIdArr  = new net.sf.json.JSONArray();
			net.sf.json.JSONArray docNameArr  = new net.sf.json.JSONArray();
			// 
			Object tempJson = new net.sf.json.util.JSONTokener(jsonObject.get("visaannexdocID").toString()).nextValue(); 
			if(tempJson instanceof net.sf.json.JSONArray){
				docIdArr.addAll(jsonObject.getJSONArray("visaannexdocID"));
			} else {
				docIdArr.add(jsonObject.getString("visaannexdocID"));
			}
			//
			tempJson = new net.sf.json.util.JSONTokener(jsonObject.get("visaannexdocName").toString()).nextValue();
			if(tempJson instanceof net.sf.json.JSONArray){
				docNameArr.addAll(jsonObject.getJSONArray("visaannexdocName"));
			} else {
				docNameArr.add(jsonObject.getString("visaannexdocName"));
			}
			//
			for(int i = 0; i < docNameArr.size(); i++) {
				TravelerFile travelerFile = new TravelerFile();
				travelerFile = getTravelerFile(TravelerFile.FILETYPE_ZBQ_VISAPAGE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()), docNameArr.get(i).toString());
				list.add(travelerFile);
			}
		}
		
		return list;
	}

	/**
	 * 根据每种文件的id、name字符串键值，通过判断，获取json中对应的信息，并以此组织返回：游客文件集
	 * @param jsonObject
	 * @param srcTravelerId
	 * @return
	 */
	public static List<TravelerFile> getTrvlFileFromJsonKey(net.sf.json.JSONObject jsonObject, Long srcTravelerId, TravelerFileEnum tFileEnum) {
		List<TravelerFile> resultList = new ArrayList<>();
		// 判断是 object还是array
		net.sf.json.JSONArray docIdArr  = new net.sf.json.JSONArray();
		net.sf.json.JSONArray docNameArr  = new net.sf.json.JSONArray();
		//  ID
		Object tempJson = new net.sf.json.util.JSONTokener(jsonObject.get(tFileEnum.getKey() + "docID").toString()).nextValue(); 
		if(tempJson instanceof net.sf.json.JSONArray){
			docIdArr.addAll(jsonObject.getJSONArray(tFileEnum.getKey() + "docID"));
		} else {
			docIdArr.add(jsonObject.getString(tFileEnum.getKey() + "docID"));
		}
		// NAME
		tempJson = new net.sf.json.util.JSONTokener(jsonObject.get(tFileEnum.getKey() + "docName").toString()).nextValue();
		if(tempJson instanceof net.sf.json.JSONArray){
			docNameArr.addAll(jsonObject.getJSONArray(tFileEnum.getKey() + "docName"));
		} else {
			docNameArr.add(jsonObject.getString(tFileEnum.getKey() + "docName"));
		}
		//
		for(int i = 0; i < docNameArr.size(); i++) {
			TravelerFile travelerFile = new TravelerFile();
			travelerFile = getTravelerFile(tFileEnum.getIndex(), srcTravelerId, Long.parseLong(docIdArr.get(i).toString()), docNameArr.get(i).toString());
			resultList.add(travelerFile);
		}
		return resultList;
	}
	/**
	 * 附件集合对象封装
	 * @param jsonObject	包含附件信息的 游客json
	 * @param srcTravelerId	游客id
	 * @return
	 */
	public static List<TravelerFile> getTravelerFileListNew(net.sf.json.JSONObject jsonObject, Long srcTravelerId){
		List<TravelerFile> list = new ArrayList<TravelerFile>();
		
		//护照
		if(jsonObject.containsKey(TravelerFileEnum.PASSPORT.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.PASSPORT));
		}
		//身份证正面/个人资料表
		if(jsonObject.containsKey(TravelerFileEnum.IDCARD_FRONT.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.IDCARD_FRONT));
		}
		//申请表格/担保书
		if(jsonObject.containsKey(TravelerFileEnum.ENTRY_FORM.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.ENTRY_FORM));
		}
		//房产证
		if(jsonObject.containsKey(TravelerFileEnum.HOUSE.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.HOUSE));
		}
		//电子照片/参团告知书
		if(jsonObject.containsKey(TravelerFileEnum.E_PHOTO.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.E_PHOTO));
		}
		//身份证反面/健康承诺书
		if(jsonObject.containsKey(TravelerFileEnum.IDCARD_BACK.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.IDCARD_BACK));
		}
		//户口本
		if(jsonObject.containsKey(TravelerFileEnum.RESIDENCE.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.RESIDENCE));
		}
		//其他
		if(jsonObject.containsKey(TravelerFileEnum.OTHER.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.OTHER));
		}
		//签证附件
		if(jsonObject.containsKey(TravelerFileEnum.VISA.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.VISA));
		}
		//自备签--护照首页
		if(jsonObject.containsKey(TravelerFileEnum.ZBQ_PASSPORT_HOMEPAGE.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.ZBQ_PASSPORT_HOMEPAGE));
		}
		//自备签--签证页
		if(jsonObject.containsKey(TravelerFileEnum.ZBQ_VISAPAGE.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.ZBQ_VISAPAGE));
		}
		//自备签--其他
		if(jsonObject.containsKey(TravelerFileEnum.ZBQ_OTHER.getKey() + "docID")){
			list.addAll(getTrvlFileFromJsonKey(jsonObject, srcTravelerId, TravelerFileEnum.ZBQ_OTHER));
		}
		
		return list;
	}
	
	/**
	 * 附件集合对象封装 C147&C109
	 * @author yang.jiang 2016-2-20 20:58:55
	 * @param jsonObject 包含附件信息的 游客json
	 * @param srcTravelerId	游客id
	 * @return
	 */
	public static List<TravelerFile> getTravelerFileList4YoujiaMod(net.sf.json.JSONObject jsonObject, Long srcTravelerId){
		
		/**
		 * 组装 附件集合
		 */
		List<TravelerFile> list = new ArrayList<TravelerFile>();
		//护照
		if(jsonObject.containsKey("passportdocID")){
			if (jsonObject.getString("passportdocID").split(",").length == 1) {
				TravelerFile travelerFile = new TravelerFile();
				String docID = jsonObject.getString("passportdocID");
				String docName = jsonObject.getString("passportdocName");
				travelerFile = getTravelerFile(TravelerFile.PASSPORTS_TYPE, srcTravelerId, Long.parseLong(docID), docName);
				list.add(travelerFile);
			} else if (jsonObject.getString("passportdocID").split(",").length > 1) {				
				//附件表id
				net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("passportdocID");
				//附件原名称
				net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("passportdocName");
				for(int i = 0; i < docNameArr.size(); i++) {
					TravelerFile travelerFile = new TravelerFile();
					travelerFile = getTravelerFile(TravelerFile.PASSPORTS_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()), docNameArr.get(i).toString());
					list.add(travelerFile);
				}
			}
		}
		//身份证正面/个人资料表
		if(jsonObject.containsKey("idcardfrontdocID")){
			if (jsonObject.getString("idcardfrontdocID").split(",").length == 1) {
				TravelerFile travelerFile = new TravelerFile();
				String docID = jsonObject.getString("idcardfrontdocID");
				String docName = jsonObject.getString("idcardfrontdocName");
				travelerFile = getTravelerFile(TravelerFile.IDCARD_FRONT_TYPE, srcTravelerId, Long.parseLong(docID), docName);
				list.add(travelerFile);
			} else if (jsonObject.getString("idcardfrontdocID").split(",").length > 1) {				
				//附件表id
				net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("idcardfrontdocID");
				//附件原名称
				net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("idcardfrontdocName");
				for(int i = 0; i < docNameArr.size(); i++) {
					TravelerFile travelerFile = new TravelerFile();
					travelerFile = getTravelerFile(TravelerFile.IDCARD_FRONT_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()), docNameArr.get(i).toString());
					list.add(travelerFile);
				}
			}
		}
		//报名表/担保书
		if(jsonObject.containsKey("entryformdocID")){
			if (jsonObject.getString("entryformdocID").split(",").length == 1) {
				TravelerFile travelerFile = new TravelerFile();
				String docID = jsonObject.getString("entryformdocID");
				String docName = jsonObject.getString("entryformdocName");
				travelerFile = getTravelerFile(TravelerFile.ENTRY_FORM_TYPE, srcTravelerId, Long.parseLong(docID), docName);
				list.add(travelerFile);
			} else if (jsonObject.getString("entryformdocID").split(",").length > 1) {				
				//附件表id
				net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("entryformdocID");
				//附件原名称
				net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("entryformdocName");
				for(int i = 0; i < docNameArr.size(); i++) {
					TravelerFile travelerFile = new TravelerFile();
					travelerFile = getTravelerFile(TravelerFile.ENTRY_FORM_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()), docNameArr.get(i).toString());
					list.add(travelerFile);
				}
			}
		}
		//头像照片/参团告知书
		if(jsonObject.containsKey("photodocID")){
			if (jsonObject.getString("photodocID").split(",").length == 1) {
				TravelerFile travelerFile = new TravelerFile();
				String docID = jsonObject.getString("photodocID");
				String docName = jsonObject.getString("photodocName");
				travelerFile = getTravelerFile(TravelerFile.PHOTO_TYPE, srcTravelerId, Long.parseLong(docID), docName);
				list.add(travelerFile);
			} else if (jsonObject.getString("photodocID").split(",").length > 1) {				
				//附件表id
				net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("photodocID");
				//附件原名称
				net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("photodocName");
				for(int i = 0; i < docNameArr.size(); i++) {
					TravelerFile travelerFile = new TravelerFile();
					travelerFile = getTravelerFile(TravelerFile.PHOTO_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()), docNameArr.get(i).toString());
					list.add(travelerFile);
				}
			}
		}
		//身份证反面/健康承诺书
		if(jsonObject.containsKey("idcardbackdocID")){
			if (jsonObject.getString("idcardbackdocID").split(",").length == 1) {
				TravelerFile travelerFile = new TravelerFile();
				String docID = jsonObject.getString("idcardbackdocID");
				String docName = jsonObject.getString("idcardbackdocName");
				travelerFile = getTravelerFile(TravelerFile.IDCARD_BACK_TYPE, srcTravelerId, Long.parseLong(docID), docName);
				list.add(travelerFile);
			} else if (jsonObject.getString("idcardbackdocID").split(",").length > 1) {				
				//附件表id
				net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("idcardbackdocID");
				//附件原名称
				net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("idcardbackdocName");
				for(int i = 0; i < docNameArr.size(); i++) {
					TravelerFile travelerFile = new TravelerFile();
					travelerFile = getTravelerFile(TravelerFile.IDCARD_BACK_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
							docNameArr.get(i).toString());
					list.add(travelerFile);
				}
			}
		}
		//其他
		if(jsonObject.containsKey("otherdocID")){
			if (jsonObject.getString("otherdocID").split(",").length == 1) {
				TravelerFile travelerFile = new TravelerFile();
				String docID = jsonObject.getString("otherdocID");
				String docName = jsonObject.getString("otherdocName");
				travelerFile = getTravelerFile(TravelerFile.OTHER_TYPE, srcTravelerId, Long.parseLong(docID), docName);
				list.add(travelerFile);
			} else if (jsonObject.getString("otherdocID").split(",").length > 1) {				
				//附件表id
				net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("otherdocID");
				//附件原名称
				net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("otherdocName");
				for(int i = 0; i < docNameArr.size(); i++) {
					TravelerFile travelerFile = new TravelerFile();
					travelerFile = getTravelerFile(TravelerFile.OTHER_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
							docNameArr.get(i).toString());
					list.add(travelerFile);
				}
			}
		}

		//签证附件
		if(jsonObject.containsKey("visaannexdocID")){
			if (jsonObject.getString("visaannexdocID").split(",").length == 1) {
				TravelerFile travelerFile = new TravelerFile();
				String docID = jsonObject.getString("visaannexdocID");
				String docName = jsonObject.getString("visaannexdocName");
				travelerFile = getTravelerFile(TravelerFile.VISA_TYPE, srcTravelerId, Long.parseLong(docID), docName);
				list.add(travelerFile);
			} else if (jsonObject.getString("visaannexdocID").split(",").length > 1) {
				//附件表id
				net.sf.json.JSONArray docIdArr = jsonObject.getJSONArray("visaannexdocID");
				//附件原名称
				net.sf.json.JSONArray docNameArr = jsonObject.getJSONArray("visaannexdocName");
				for(int i = 0; i < docNameArr.size(); i++) {
					TravelerFile travelerFile = new TravelerFile();
					travelerFile = getTravelerFile(TravelerFile.VISA_TYPE, srcTravelerId, Long.parseLong(docIdArr.get(i).toString()),
							docNameArr.get(i).toString());
					list.add(travelerFile);
				}
			}
		}

		return list;
	}

	public static List<OrderContacts> getContactsListForLog(String orderContactsJSON) throws JSONException {
		List<OrderContacts> contactsList = new ArrayList<OrderContacts>();
		JSONArray datatravelerArray = new JSONArray(orderContactsJSON);
		int len = datatravelerArray.length();
		if(len > 0){
			for(int i=0;i<len;i++){
				JSONObject ordercontacts = datatravelerArray.getJSONObject(i);
				Iterator<String> it = ordercontacts.keys();
				List<String> keyList = new ArrayList<String>();
				while(it.hasNext()) {
					keyList.add(it.next());
				}
				//处理旅行者
				OrderContacts contacts = new OrderContacts();
				{
					//联系人名称
					Object contactsName = ordercontacts.get("contactsName");
					if(contactsName!=null&&StringUtils.isNotBlank(contactsName.toString())){
						contacts.setContactsName(contactsName.toString());
					}
					//联系人电话
					Object contactsTel = ordercontacts.get("contactsTel");
					if(contactsTel!=null&&StringUtils.isNotBlank(contactsTel.toString())){
						contacts.setContactsTel(contactsTel.toString());
					}
					//联系人固定电话
					Object contactsTixedTel = ordercontacts.get("contactsTixedTel");
					if(contactsTixedTel!=null&&StringUtils.isNotBlank(contactsTixedTel.toString())){
						contacts.setContactsTixedTel(contactsTixedTel.toString());
					}
					//联系人地址
					Object contactsAddress = ordercontacts.get("contactsAddress");
					if(contactsAddress!=null&&StringUtils.isNotBlank(contactsAddress.toString())){
						contacts.setContactsAddress(contactsAddress.toString());
					}
					//联系人传真
					Object contactsFax = ordercontacts.get("contactsFax");
					if(contactsFax!=null&&StringUtils.isNotBlank(contactsFax.toString())){
						contacts.setContactsFax(contactsFax.toString());
					}
					//联系人QQ
					Object contactsQQ = ordercontacts.get("contactsQQ");
					if(contactsQQ!=null&&StringUtils.isNotBlank(contactsQQ.toString())){
						contacts.setContactsQQ(contactsQQ.toString());
					}
					//联系人邮箱
					Object contactsEmail = ordercontacts.get("contactsEmail");
					if(contactsEmail!=null&&StringUtils.isNotBlank(contactsEmail.toString())){
						contacts.setContactsEmail(contactsEmail.toString());
					}
					//联系人邮编
					Object contactsZipCode = ordercontacts.get("contactsZipCode");
					if(contactsZipCode!=null&&StringUtils.isNotBlank(contactsZipCode.toString())){
						contacts.setContactsZipCode(contactsZipCode.toString());
					} 
					//备注
					Object remark = ordercontacts.get("remark");
					if(remark!=null&&StringUtils.isNotBlank(remark.toString())){
						contacts.setRemark(remark.toString());
					}
					//id 26需求
					if(keyList.indexOf("id") > 0){
					Object id = ordercontacts.get("id");
					if(id!=null&&StringUtils.isNotBlank(id.toString())){
						contacts.setId(Long.parseLong(id.toString()));
					}
				}
				}  
				contactsList.add(contacts);
			}
		}
		return contactsList;
	}

	public static  String getOrderTypeName(String orderType) {
		String typeName = "";
		if(StringUtils.isEmpty(orderType)) {
			return typeName;
		}

		if(Context.ORDER_STATUS_SINGLE.equals(orderType)) {
			typeName = "单团";
		} else if(Context.ORDER_STATUS_LOOSE.equals(orderType)) {
			typeName = "散拼";
		} else if(Context.ORDER_STATUS_STUDY.equals(orderType)) {
			typeName = "游学";
		} else if(Context.ORDER_STATUS_BIG_CUSTOMER.equals(orderType)) {
			typeName = "大客户";
		} else if(Context.ORDER_STATUS_FREE.equals(orderType)) {
			typeName = "自由行";
		} else if(Context.ORDER_STATUS_AIR_TICKET.equals(orderType)) {
			typeName = "机票";
		} else if(Context.ORDER_STATUS_CRUISE.equals(orderType)) {
			typeName = "游轮";
		}
		return typeName;
	}
	
	
}
