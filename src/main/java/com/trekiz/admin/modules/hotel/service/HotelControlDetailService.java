/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelControlDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelControlDetailService{
	
	
	
	public void save (HotelControlDetail hotelControlDetail);
	
	public void update (HotelControlDetail hotelControlDetail);
	
	public HotelControlDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelControlDetail> find(Page<HotelControlDetail> page, HotelControlDetail hotelControlDetail);
	
	public List<HotelControlDetail> find( HotelControlDetail hotelControlDetail);
	
	public HotelControlDetail getByUuid(String uuid);
	
	
	public int  yonRemoveByUuid(String uuid);
	
	public void updateHotelControls(List<HotelControlDetail> listControlDetails);
	
	public void removeByUuid(String uuid);
	
	public void delStatusByUuid(String uuid);
	
	/**
	 * 根据生成后的md5判断控房规则是否存在
		* 
		* @param uuid	控房详情表uuid，修改时需要指定
		* @param md5Code  MD5字符串
		* @param companyId 公司ID
		* @return boolean
		* @author majiancheng
		* @Time 2015-5-13
	 */
	public boolean findRuleIsExist(String uuid, String md5Code, Long companyId);
	public List<HotelControlDetail> getByHotelControlUuid(String hotelControlUuid);
	
	
	/**
	 * 根据指定状态 修改 控房明细表 状态 add by zhanghao
	 * @param deatil
	 * @param status
	 */
	public int updateDetailStatus(String deatilUuid,int status);
	
	
	/**
	 * 根据控房单明细信息生成规则字符串 add by zhanghao
	 * @param deatil
	 * @param roomList
	 * @return
	 */
	public String getHotelControlRule(HotelControlDetail deatil,List<HotelControlRoomDetail> roomList);
	public String getHotelControlRule(HotelControlDetail deatil);
	public String getHotelControlRule(String detailUuid);
	
	
}
