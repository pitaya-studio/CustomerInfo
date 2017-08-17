/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelAnnexService{
	
	public void save (HotelAnnex hotelAnnex);
	
	public void update (HotelAnnex hotelAnnex);
	
	public HotelAnnex getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelAnnex> find(Page<HotelAnnex> page, HotelAnnex hotelAnnex);
	
	public List<HotelAnnex> find( HotelAnnex hotelAnnex);
	
	public List<HotelAnnex> getFileList(HttpServletRequest request);
	
	public void removeByUuid(String uuid);
	/**
	 * 根据主表uuid查找附件
	 * @param uuid
	 * @return
	 */
	public List<HotelAnnex> getAnnexListByMainUuid(String uuid);

}
