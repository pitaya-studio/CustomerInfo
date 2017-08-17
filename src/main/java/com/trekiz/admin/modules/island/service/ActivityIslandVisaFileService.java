/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandVisaFile;
import com.trekiz.admin.modules.island.input.ActivityIslandVisaFileInput;
import com.trekiz.admin.modules.island.query.ActivityIslandVisaFileQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandVisaFileService{
	
	public void save (ActivityIslandVisaFile activityIslandVisaFile);
	
	public void save (ActivityIslandVisaFileInput activityIslandVisaFileInput);
	
	public void update (ActivityIslandVisaFile activityIslandVisaFile);
	
	public ActivityIslandVisaFile getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandVisaFile> find(Page<ActivityIslandVisaFile> page, ActivityIslandVisaFileQuery activityIslandVisaFileQuery);
	
	public List<ActivityIslandVisaFile> find( ActivityIslandVisaFileQuery activityIslandVisaFileQuery);
	
	public List<ActivityIslandVisaFile> find( ActivityIslandVisaFile activityIslandVisaFile);
	
	public ActivityIslandVisaFile getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
