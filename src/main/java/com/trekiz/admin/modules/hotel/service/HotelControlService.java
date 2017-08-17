/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.HotelControl;
import com.trekiz.admin.modules.hotel.input.HotelControlInput;
import com.trekiz.admin.modules.hotel.model.HotelControlDetailModel;
import com.trekiz.admin.modules.hotel.query.HotelControlQuery;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandOrderControlDetail;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelControlService{
	
	public void save (HotelControl hotelControl);
	
	public void update (HotelControl hotelControl);
	
	public HotelControl getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelControl> find(Page<HotelControl> page, HotelControl hotelControl);
	
	public List<HotelControl> find( HotelControl hotelControl);
	
	public HotelControl getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 保存控房记录 add by zhanghao
	 * @param input
	 * @return
	 */
	public Map<String,String> saveHotelControl(HotelControlInput input,String flag,List<HotelAnnex> listAnnex) throws Exception;
	/**
	 * 修改控房记录 add by zhanghao
	 * 修改控房记录前要做唯一性的验证
	 * @param input
	 * @param updateFlag 1、true表示根据酒店更新控价单；2、false表示根据日期更新控价单
	 * @return
	 */
	public Map<String,String> updateHotelControl(HotelControlInput input, boolean updateFlag,List<HotelAnnex> axList) throws Exception ;
	
	
	public Page<Map<String, Object>> hotelControlList(HttpServletRequest request, HttpServletResponse response,
			HotelControlQuery hotelControlQuery);
	
	public List<List<Map<String, String>>> getForHotelSubs(List<Map<String, Object>> loop);
	
	public List<List<Map<String, String>>> getForDateSubs(List<Map<String, Object>> loop);
	
	
	/**
	 * 扣减余位 接口定义zhanghao，实现sy
	 * 海岛游订单 转报名后 扣减余位操作接口（同时扣减机票产品余位和酒店控房的余位 。注：V1版本不关联机票库存。）
	 * 扣减余位之前要判断 当前库存是否满足需要扣除的数量。
	 * @param order 当前操作的订单实体对象
	 * @param detailList 需要扣减的控房信息结合（集合中的对象属性 hotelControlDetailUuid和num是必填项）
	 * @return Map<String,Object> key:result。value：success有剩余库存扣减成功，fail没有库存扣减失败，error参数异常。
	 * 							  key:message。value:fail和error情况下的异常信息
	 * 
	 */
	public Map<String,Object> deductRemNumber(IslandOrder order ,List<IslandOrderControlDetail> detailList );
	
	/**
	 * 根据酒店uuid，获取定制的酒店控房详情数据
	*<p>Title: getControlDetailsByControlUuid</p>
	* @return List<HotelControlDetailModel> 返回类型
	* @author majiancheng
	* @date 2015-6-11 下午10:24:48
	* @throws
	 */
	public List<HotelControlDetailModel> getControlDetailsByHotelUuid(String hotelUuid);
	
	/**
	 * 根据酒店uuid获取酒店房型和餐型的json数据
	*<p>Title: getHotelRoomMealsData</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-8-6 下午4:40:19
	* @throws
	 */
	public String getHotelRoomMealsData(String hotelUuid);
	
	/**
	 * 根据控房uuid获取房型和餐型的关联关系
	*<p>Title: getHotelDetailsData</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-8-7 下午9:11:36
	* @throws
	 */
	public String getHotelDetailsData(String hotelControlUuid);
	
}
