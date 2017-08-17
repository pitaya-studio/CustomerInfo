/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelAnnexDao  extends BaseDao<HotelAnnex> {
	
	public void delByMainUuid(String uuid);
	
	/**
	 * 酒店相关的附件关联表（hotel_annex）  和 系统内的附件表(docinfo) 同步 add by zhanghao
	 * 
	 * 
	 * 如果是新增则新增 hotel_annex 
	 * 如果是删除则删除 hotel_annex docinfo
	 * 
	 * @param mainUuid 关联的主表ID
	 * @param type 关联的表类型 目前 类型。1：酒店；2：酒店特色
	 * @param wholesalerId 操作的批发商ID
	 * @param axList 当前要操作的 附件信息集合
	 * 
	 * 
	 */
	public void synDocInfo(String mainUuid ,int type,int wholesalerId, List<HotelAnnex> axList);
	
	
	public void delByUuid(String uuid);

	public List<HotelAnnex> getByMainUuid(String uuid);

	public List<HotelAnnex> getAnnexListByMainUuid(String uuid);

}
