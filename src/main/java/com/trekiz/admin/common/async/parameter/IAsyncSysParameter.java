/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.common.async.parameter;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-28
 * 
 * 系统参数接口，供IMessageHander实现类切换租户
 */
public interface IAsyncSysParameter {
	public String getAsyncSysParameterTenantID() ;
}
