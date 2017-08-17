package com.trekiz.admin.modules.visa.utils;

import java.util.List;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.visa.service.VisaProductsFileService;

/**
 * 签证产品文件工具类
 */
public class VisaProductFileUtils {
	
	private static VisaProductsFileService visaProductService = SpringContextHolder.getBean(VisaProductsFileService.class);
	
	/**
	 * 查询单个产品相关文件的id串，用于打包下载
	 * @author jiachen
	 * @DateTime 2014-12-4 下午06:27:07
	 * @return List<Object>
	 */
	public static String findFileIds(Long visaProdectsId) {
		
		List<Object> list = visaProductService.findFileListByProId(visaProdectsId, true);
		if(list.isEmpty()) {
			return "";
		}else{
			return visaProductService.findFileListByProId(visaProdectsId, true).get(0).toString();
		}
	}
}
