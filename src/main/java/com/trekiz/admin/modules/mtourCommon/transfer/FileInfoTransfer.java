package com.trekiz.admin.modules.mtourCommon.transfer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.trekiz.admin.modules.mtourCommon.jsonbean.FileInfoJsonBean;
import com.trekiz.admin.modules.sys.entity.DocInfo;

public class FileInfoTransfer {
	
	/**
	 * 由数据库文件对象转为前端文件json信息
	     * <p>@Description TODO</p>
		 * @Title: docInfo2FileInfoJsonBean
	     * @return FileInfoJsonBean
	     * @author majiancheng       
	     * @date 2015-10-14 上午10:37:20
	 */
	public static FileInfoJsonBean docInfo2FileInfoJsonBean(DocInfo docInfo) {
		if(docInfo!=null){
			FileInfoJsonBean bean = new FileInfoJsonBean();
			bean.setAttachmentUuid(docInfo.getId().toString());
			bean.setAttachmentUrl(docInfo.getDocPath());
			bean.setFileName(docInfo.getDocName());
			return bean;
		}
		return null;
	}
	
	/**
	 * 由数据库文件对象集合转为前端文件json信息集合
	     * <p>@Description TODO</p>
		 * @Title: docInfos2FileInfoJsonBeans
	     * @return List<FileInfoJsonBean>
	     * @author majiancheng       
	     * @date 2015-10-14 上午10:38:08
	 */
	public static List<FileInfoJsonBean> docInfos2FileInfoJsonBeans(List<DocInfo> docInfos) {
		List<FileInfoJsonBean> fileInfoJsonBeans = new ArrayList<FileInfoJsonBean>();
		if(CollectionUtils.isNotEmpty(docInfos)) {
			for(DocInfo docInfo : docInfos) {
				fileInfoJsonBeans.add(docInfo2FileInfoJsonBean(docInfo));
			}
		}
		return fileInfoJsonBeans;
	}

}
