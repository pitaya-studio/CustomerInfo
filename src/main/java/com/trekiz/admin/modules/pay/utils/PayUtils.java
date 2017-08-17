package com.trekiz.admin.modules.pay.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.LocaleResolver;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.DictUtils;

/**
 * 支付公用表
* <p>description: TODO</p>
* <p>Date: 2015-6-17 下午2:53:46</p>
* <p>modify：</p>
* @author: jangta
* @version: 3.0
* </p>Company: jangt.com</p>
 */
public class PayUtils {
	private static DocInfoService docInfoService = (DocInfoService) SpringContextHolder.getBean(DocInfoService.class);
	
	/**
	 * 取得支付类型名称
	 * 
	 * @param paymentStatus
	 * @param payType
	 * @return
	 */
	public static String getPayTypeName(Integer paymentStatus, Integer payType) {
		// 即时支付
		if (paymentStatus == null || paymentStatus == 1) {
			return DictUtils.getDictLabel(payType.toString(), Context.ORDER_PAYTYPE, "");
		}
		
		// 其他支付方式
		return DictUtils.getDictLabel(paymentStatus.toString(), Context.PAY_MENT_TYPE, "");
	}
	
	/**
	 * 复制指定ID的上传文件并保存该复制数据至表中
	 * 
	 * @param docInfoId
	 * @return
	 * @throws IOException
	 */
	public static Long copySaveUploadInfo(Long docInfoId) throws IOException {
		// 根据ID取得相应的信息
		DocInfo docInfo = docInfoService.getDocInfo(docInfoId);
		String docPath = docInfo.getDocPath();
		File srcFile = new File(Global.getBasePath() + File.separator + docPath);
		String path = FilenameUtils.getFullPath(srcFile.getAbsolutePath());
		String ext = FilenameUtils.getExtension(srcFile.getName()).toLowerCase(Locale.ENGLISH);
		// 使用唯一标识码生成文件名
		String newFilePath = path + UuidUtils.generUuid() + "." + (ext != null ? ext : "");
		// 复制并保存上传文件
		FileCopyUtils.copy(srcFile, new File(newFilePath));

		// 保存相应的上传文件信息
		DocInfo doc = new DocInfo();
		doc.setDocName(docInfo.getDocName());
		doc.setDocPath(newFilePath.replace(new File(Global.getBasePath()).getAbsolutePath() + File.separator, ""));
		Long docId = docInfoService.saveDocInfo(doc).getId();
		return docId;
	}

}
