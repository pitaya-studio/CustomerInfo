/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourCommon.entity.MtourInterfaceLog;
import com.trekiz.admin.modules.mtourCommon.input.MtourInterfaceLogInput;
import com.trekiz.admin.modules.mtourCommon.query.MtourInterfaceLogQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface MtourInterfaceLogService{
	
	public void save (MtourInterfaceLog mtourInterfaceLog);
	
	public void batchSave(List<MtourInterfaceLog> mtourInterfaceLogs);
	
	public void save (MtourInterfaceLogInput mtourInterfaceLogInput);
	
	public void update (MtourInterfaceLog mtourInterfaceLog);
	
	public MtourInterfaceLog getById(java.lang.Long value);
	
	public void removeById(java.lang.Long value);
	
	public Page<MtourInterfaceLog> find(Page<MtourInterfaceLog> page, MtourInterfaceLogQuery mtourInterfaceLogQuery);
	
	public List<MtourInterfaceLog> find( MtourInterfaceLogQuery mtourInterfaceLogQuery);
	
	public MtourInterfaceLog getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
