/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.modules.mtourCommon.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.mtourCommon.utils.ThreadVariable;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-27
 * 
 * 美途项目-Controller支持类
 */
public abstract class MtourBaseController extends BaseController {
	
	/**
	 * 将Object按json编码，并将其缓存到线程中。
	 * 注意：该方法仅仅供美途项目-对应的Ajax请求处理controller使用。
	 * @param obj
	 * @return
	 */
	protected String toAndCacheJSONString(Object obj){
		String json = JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
		ThreadVariable.setMtourAjaxResponse(json);
		return json;
	}
	protected String toAndCacheJSONString(Object obj,String dateFormat){
		String json = JSON.toJSONStringWithDateFormat(obj, dateFormat, SerializerFeature.DisableCircularReferenceDetect);
		ThreadVariable.setMtourAjaxResponse(json);
		return json;
	}
}
