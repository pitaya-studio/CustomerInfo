package com.trekiz.admin.modules.mtourfinance.transfer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.trekiz.admin.modules.mtourfinance.json.DocInfoJsonBean;
import com.trekiz.admin.modules.sys.entity.DocInfo;

public class DocInfoTransfer {
	public static DocInfoJsonBean transferDocInfo2JsonBean(DocInfo docInfo) {
		if(docInfo == null) {
			return null;
		}
		DocInfoJsonBean docInfoJsonBean = new DocInfoJsonBean();
		docInfoJsonBean.setAttachmentUuid(docInfo.getId().toString());
		docInfoJsonBean.setFileName(docInfo.getDocName());
		docInfoJsonBean.setAttachmentUrl(docInfo.getDocPath());
		docInfoJsonBean.setUploadUserName(docInfo.getCreateBy().getName());
		return docInfoJsonBean;
	}
	
	public static List<DocInfoJsonBean> transferDocInfos2JsonBeans(List<DocInfo> docInfos) {
		if(CollectionUtils.isEmpty(docInfos)) {
			return null;
		}
		List<DocInfoJsonBean> jsonBeans = new ArrayList<DocInfoJsonBean>();
		for(DocInfo docInfo : docInfos) {
			jsonBeans.add(transferDocInfo2JsonBean(docInfo));
		}
		return jsonBeans;
	}

}
