/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotelPl.entity.HotelPl;
import com.trekiz.admin.modules.hotelPl.input.HotelPlInput;
import com.trekiz.admin.modules.hotelPl.module.bean.HotelPlRoom;
import com.trekiz.admin.modules.hotelPl.query.HotelPlQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlService{
	
	public void save (HotelPl hotelPl);
	
	public void save (HotelPlInput hotelPlInput);
	
	public void update (HotelPl hotelPl);
	
	public HotelPl getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPl> find(Page<HotelPl> page, HotelPlQuery hotelPlQuery);
	
	public List<HotelPl> find( HotelPlQuery hotelPlQuery);
	
	public HotelPl getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 批量删除价单信息
		 * @Title: batchDelete
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-23 下午3:13:59
	 */
	public boolean batchDelete(String[] uuids);
	
	/**
	 * 根据酒店价单uuid获取该价单下所有的价单数据
	*<p>Title: getAllHotelPlDataByUuid</p>
	* @return HotelPl 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午2:25:09
	* @throws
	 */
	public HotelPl getAllHotelPlDataByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取去重后的酒店价单房型
	*<p>Title: getDistinctHotelPlRoomsByUuid</p>
	* @return List<HotelPlRoom> 返回类型
	* @author majiancheng
	* @date 2015-6-28 下午5:01:02
	* @throws
	 */
	public List<HotelPlRoom> getDistinctHotelPlRoomsByUuid(String hotelPlUuid);
	
	/**
	 * 初始化保存酒店价单页面的数据
	*<p>Title: initSaveHotelPlPageData</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-6-29 下午4:12:52
	* @throws
	 */
	public void initSaveHotelPlPageData(Model model);
	
	/**
	 * 新增酒店税金信息
	*<p>Title: saveHotelPlTaxInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-14 下午4:24:53
	* @throws
	 */
	public Map<String,String> saveHotelPlTaxInfo(String hotelPlTaxPriceJsonData, String hotelPlTaxExceptionJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 修改酒店税金信息
	*<p>Title: updateHotelPlTaxInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-14 下午4:24:59
	* @throws
	 */
	public Map<String,String> updateHotelPlTaxInfo(String hotelPlTaxPriceJsonData, String hotelPlTaxExceptionJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 保存酒店价单价格信息
	*<p>Title: saveHotelPlPriceInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-3 上午11:34:34
	* @throws
	 */
	public Map<String, String> saveHotelPlPriceInfo(String hotelPlPriceJsonData, String roomMemoJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 修改酒店价单价格信息
	*<p>Title: updateHotelPlPriceInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-6 下午12:17:41
	* @throws
	 */
	public Map<String, String> updateHotelPlPriceInfo(String hotelPlPriceJsonData, String roomMemoJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 保存酒店价单交通费用信息
	*<p>Title: saveHotelPlIslandWayInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-6 上午11:39:35
	* @throws
	 */
	public Map<String, String> saveHotelPlIslandWayInfo(String hotelPlIslandWayJsonData, String islandWayMemoJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 修改酒店价单交通费用信息
	*<p>Title: updateHotelPlIslandWayInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-6 下午12:17:35
	* @throws
	 */
	public Map<String, String> updateHotelPlIslandWayInfo(String hotelPlIslandWayJsonData, String islandWayMemoJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 保存酒店价单升餐费用信息
	*<p>Title: saveHotelPlRiseMealInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-6 下午12:16:47
	* @throws
	 */
	public Map<String, String> saveHotelPlRiseMealInfo(String hotelPlRiseMealJsonData, String riseMealMemoJsonData, HotelPl hotelPl) throws Exception ;
	

	/**
	 * 修改酒店价单升餐费用信息
	*<p>Title: updateHotelRiseMealInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-6 下午12:17:12
	* @throws
	 */                        
	public Map<String, String> updateHotelPlRiseMealInfo(String hotelPlRiseMealJsonData, String riseMealMemoJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 保存强制性节日餐信息
	*<p>Title: saveHotelPlHolidayMealInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-6 下午3:04:03
	* @throws
	 */
	public Map<String, String> saveHotelPlHolidayMealInfo(String hotelPlHolidayMealJsonData, HotelPl hotelPl) throws Exception ;

	/**
	 * 修改强制性节日餐信息
	*<p>Title: updateHotelPlHolidayMealInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-6 下午3:04:28
	* @throws
	 */
	public Map<String, String> updateHotelPlHolidayMealInfo(String hotelPlHolidayMealJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 保存价单优惠信息
	*<p>Title: saveHotelPlPreferentialInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-9 上午10:49:16
	* @throws
	 */
	public Map<String, String> saveHotelPlPreferentialInfo(String hotelPlHolidayMealJsonData, HotelPl hotelPl) throws Exception ;
	
	/**
	 * 修改价单优惠信息
	*<p>Title: saveHotelPlPreferentialInfo</p>
	* @return Map<String,String> 返回类型
	* @author majiancheng
	* @date 2015-7-9 上午10:49:16
	* @throws
	 */
	public Map<String, String> updateHotelPlPreferentialInfo(String hotelPlHolidayMealJsonData, HotelPl hotelPl) throws Exception;
	
	/**
	 * 组装表单基本数据
	*<p>Title: buildBaseData</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-7-2 下午9:10:32
	* @throws
	 */
	public void buildBaseData(HotelPl hotelPl, Map<String, String> datas);
	/**
	 * 保存附件信息
	 * @param buildAnnexList
	 * @param hotelPl
	 */
	public void saveHOtelAnnex(List<HotelAnnex> buildAnnexList,
			HotelPl hotelPl);
	
	/**
	 * 根据酒店uuid获取多个酒店房型集合
	*<p>Title: getHotelRoomsInfoByHotelUuid</p>
	* @return Map<String,Object> 返回类型
	* @author majiancheng
	* @date 2015-7-8 下午3:41:14
	* @throws
	 */
	public Map<String,Object> getHotelRoomsInfoByHotelUuid(String hotelUuid);
	
	/**
	 * 修改酒店价单备注
	*<p>Title: updateHotelPlMemo</p>
	* @return Map<String,Object> 返回类型
	* @author majiancheng
	* @date 2015-7-10 下午8:57:43
	* @throws
	 */
	public Map<String,String> updateHotelPlMemo(String hotelPlUuid, String hotelPlMemo);
	
	/**
	 * 加载酒店价单数据
	*<p>Title: initHotelPlData</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-7-13 上午10:26:37
	* @throws
	 */
	public void initHotelPlData(HotelPl hotelPl, Model model);
	
	/**
	 * 根据“酒店名称”“采购类型”“地接供应商”3个条件来判断该价单是否已经存在
	*<p>Title: findIsExist</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-7-18 下午3:09:10
	* @throws
	 */
	public boolean findIsExist(String hotelUuid, int purchaseType, int supplierInfoId);
}
