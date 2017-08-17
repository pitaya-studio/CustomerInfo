/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderOperateRecord;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderOperateRecordInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderOperateRecordQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketOrderOperateRecordService{
	
	public void save (AirticketOrderOperateRecord airticketOrderOperateRecord);
	
	public void save (AirticketOrderOperateRecordInput airticketOrderOperateRecordInput);
	
	public void update (AirticketOrderOperateRecord airticketOrderOperateRecord);
	
	public AirticketOrderOperateRecord getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketOrderOperateRecord> find(Page<AirticketOrderOperateRecord> page, AirticketOrderOperateRecordQuery airticketOrderOperateRecordQuery);
	
	public List<AirticketOrderOperateRecord> find( AirticketOrderOperateRecordQuery airticketOrderOperateRecordQuery);
	
	public AirticketOrderOperateRecord getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
