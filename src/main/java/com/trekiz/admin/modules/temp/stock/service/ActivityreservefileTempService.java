/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.temp.stock.entity.ActivityreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.ActivityreservefileTempInput;
import com.trekiz.admin.modules.temp.stock.query.ActivityreservefileTempQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityreservefileTempService{
	
	public void save (ActivityreservefileTemp activityreservefileTemp);
	
	public void save (ActivityreservefileTempInput activityreservefileTempInput);
	
	public void update (ActivityreservefileTemp activityreservefileTemp);
	
	public ActivityreservefileTemp getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityreservefileTemp> find(Page<ActivityreservefileTemp> page, ActivityreservefileTempQuery activityreservefileTempQuery);
	
	public List<ActivityreservefileTemp> find( ActivityreservefileTempQuery activityreservefileTempQuery);
	
	public ActivityreservefileTemp getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据草稿箱uuid获取草稿箱文件集合
	 * @Description: 
	 * @param @param reserveTempUuid
	 * @param @return   
	 * @return List<ActivityreservefileTemp>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-12-23 下午9:36:02
	 */
	public List<ActivityreservefileTemp> getFilesByReserveTempUuid(String reserveTempUuid);
}
