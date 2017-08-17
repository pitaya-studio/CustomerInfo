/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupLowprice;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupPrice;
import com.trekiz.admin.modules.island.input.ActivityIslandInput;
import com.trekiz.admin.modules.island.query.ActivityIslandQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandService{
	
	public void save (ActivityIsland activityIsland);
	
	public Map<String, String> save (ActivityIslandInput activityIslandInput,String status);
	
	public void update (ActivityIsland activityIsland);
	
	public ActivityIsland getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIsland> find(Page<ActivityIsland> page, ActivityIslandQuery activityIslandQuery);
	
	public List<ActivityIsland> find( ActivityIslandQuery activityIslandQuery);
	
	public ActivityIsland getByUuid(String uuid);
	
	public Map<String,String> saveActivityIsland(ActivityIslandInput activityIslandInput);
	
	public void removeByUuid(String uuid);
	
	public Page<Map<String, Object>> getlandProductList(ActivityIslandQuery query,HttpServletRequest request,HttpServletResponse response);
	
	public List<List<Map<String, String>>> getlandProductListSubs(List<Map<String, Object>> loop);
	
	
	/**
	 * 通过"海岛游产品团期价格表"获得"海岛游产品团期起价表"
	 * 注意：必须把“产品表（ActivityIsland）”对应的所有的"团契价格表(ActivityIslandGroupPrice)"组装成一个activityIslandGroupPriceList集合传过来，不然逻辑就不对了
	 * @author LiuXueLiang
	 * @param priceList,flag
	 * @return
	 */
	public List<ActivityIslandGroupLowprice> getLowpriceByPriceList(List<ActivityIslandGroupPrice> activityIslandGroupPriceList,String flag);
	
	/**
	 * 批量更新海岛游产品状态
	*<p>Title: batchUpdateStatusByIslandUuidArr</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-5-28 上午11:42:27
	* @throws
	 */
	public int batchUpdateStatusByIslandUuidArr(String[] islandUuidArray, String status);
	
	public Map<String,String> updateAcitivityIsland(ActivityIslandInput activityIslandInput,String flag);
	public Map<String,String> saveActivityIslandGroup(ActivityIslandInput activityIslandInput);
	public Map<String,String> updateActivityIslandGroup(ActivityIslandGroup islandGroup,String flag);

	/**
	 * 获取单个产品对应的起价列表    按产品和币种分组
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> getIslandLowPriceList(String uuid);
	
	/**
	 * 获取单个产品对应的团期列表
	 * @param list
	 * @return
	 */
	public List<Map<String,Object>> getIslandGroupList(String uuid,ActivityIslandQuery query);
	
	/**
	 * 获取游客类型对应的价格    列表
	 * @param list
	 * @param showType
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> getTravelerPriceList(String uuid,String groupUuids);
	
	public String getJsonStringByActivityIslandGroupUuid(String activityIslandGroupUuid);
	
	/**
	 * 获取所在批发商下的所有的航空公司信息（只是针对于海岛游产品模块使用）
	*<p>Title: getJsonAirlineInfo</p>
	* @return Map<String,Object> 返回类型
	* @author majiancheng
	* @date 2015-8-24 下午5:58:55
	* @throws
	 */
	public Map<String, Object> getJsonAirlineInfo();
	
	/**
	 * 获取舱位等级及同行价
	 * @param uuid
	 * @param groupUuids
	 * @param hotelUuid
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> getTravelerPriceList(
			String uuid, String groupUuids,String hotelUuid);
}
