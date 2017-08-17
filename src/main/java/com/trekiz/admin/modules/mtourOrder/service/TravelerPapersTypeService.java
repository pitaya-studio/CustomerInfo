/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.TravelerPapersType;
import com.trekiz.admin.modules.mtourOrder.input.TravelerPapersTypeInput;
import com.trekiz.admin.modules.mtourOrder.query.TravelerPapersTypeQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface TravelerPapersTypeService{
	
	public void save (TravelerPapersType travelerPapersType);
	
	public void save (TravelerPapersTypeInput travelerPapersTypeInput);
	
	public void update (TravelerPapersType travelerPapersType);
	
	public TravelerPapersType getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<TravelerPapersType> find(Page<TravelerPapersType> page, TravelerPapersTypeQuery travelerPapersTypeQuery);
	
	public List<TravelerPapersType> find( TravelerPapersTypeQuery travelerPapersTypeQuery);
	
	public TravelerPapersType getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public void deleteByOrderId(Long Id);
	
	/**
	 * 删除游客相关附件
	 */
	public void deleteFileByActivityId(Long Id);
}
