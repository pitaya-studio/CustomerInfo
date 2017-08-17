/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.HotelControlDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;
import com.trekiz.admin.modules.hotel.model.HotelControlDetailModel;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelControlDetailDao  extends BaseDao<HotelControlDetail> {
	
	public HotelControlDetail getByUuid(String uuid);

	public void updateHotelControls(List<HotelControlDetail> hotelControlDetails);
	
	public List<HotelControlDetail> getByhotelControlUuid(String hotelControlUuid);
	
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
	
	public List<HotelControlDetail> getByHotelControlUuid(String hotelControlUuid,Integer delFlag);
	/**
	 * 根据指定状态 修改 控房明细表 状态 add by zhanghao
	 * @param deatil
	 * @param status
	 */
	public int updateDetailStatus(String deatilUuid,int status);
	
	public String getHotelControlRule(HotelControlDetail deatil,List<HotelControlRoomDetail> roomList);
	
	/**
	 * 根据酒店uuid，获取定制的酒店控房详情数据
	*<p>Title: getControlDetailsByControlUuid</p>
	* @return List<HotelControlDetailModel> 返回类型
	* @author majiancheng
	* @date 2015-6-11 下午10:24:48
	* @throws
	 */
	public List<HotelControlDetailModel> getControlDetailsByHotelUuid(String hotelUuid);
	
}
