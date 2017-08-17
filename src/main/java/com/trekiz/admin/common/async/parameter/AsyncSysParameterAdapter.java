/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.common.async.parameter;

import javax.persistence.Transient;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-27
 * 
 * IAsyncSysParameter的Adapter类
 */
public abstract class AsyncSysParameterAdapter  extends BaseEntity implements IAsyncSysParameter{
	private static final long serialVersionUID = 1L;
	private long AsyncSysParameterUserID;
	private String AsyncSysParameterTenantID;
	
	@Transient
	public long getAsyncSysParameterUserID() {
		return AsyncSysParameterUserID;
	}
	public void setAsyncSysParameterUserID(long asyncSysParameterUserID) {
		AsyncSysParameterUserID = asyncSysParameterUserID;
	}
	
	@Transient
	public String getAsyncSysParameterTenantID() {
		return AsyncSysParameterTenantID;
	}
	public void setAsyncSysParameterTenantID(String asyncSysParameterTenantID) {
		AsyncSysParameterTenantID = asyncSysParameterTenantID;
	}
	
	
}
