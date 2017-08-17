package com.trekiz.admin.modules.activity.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;

/**
 * 产品服务接口
 * @author ZhengZiyu
 *
 */
public interface ITravelActivityService {
	
	TravelActivity save(TravelActivity travelActivity);

	TravelActivity findById(Long id);

	void delActivity(TravelActivity activity);

	/**
	 * 批量删除产品
	 * 创建人：liangjingming   
	 * 创建时间：2014-3-3 下午2:44:19     
	 * @throws Exception 
	 *
	 */
	void batchDelActivity(List<Long> ids);

	/**
	 * 批量上架或下架产品
	 * 创建人：liangjingming   
	 * 创建时间：2014-3-3 下午2:44:19     
	 * @throws Exception 
	 *
	 */
	void batchOnOrOffActivity(List<Long> ids, Integer status);

	/**
	 * 查询批发商录过的目标区域
	 *     
	 * 创建人：liangjingming   
	 * 创建时间：2014-2-10 下午2:04:19     
	 *
	 */
	List<Map<String, Object>> findAreaIds(Long companyId);
	
	//-----------t1t2需求-----------s--//
	List<Map<String, Object>> findAreaIds4T1(Long companyId);
	//-----------t1t2需求-----------e--//
	
	/**
	 * 查询批发商录过的目标区域 精确到国家
	 *     
	 * 创建人：lixin   
	 * 创建时间：2014-2-10 下午2:04:19     
	 *
	 */
	List<Map<String, Object>> findAreaIdsEndCountry(Long companyId);
	//-----------t1t2需求-----------s--//
	List<Map<String, Object>> findAreaIdsEndCountry4T1(Long companyId);
	//-----------t1t2需求-----------e--//
	
	List<Map<String, Object>> findCountryAreaIds(Long companyId);

	Page<TravelActivity> findTravelActivity(
			Page<TravelActivity> page, TravelActivity travelActivity,
			String settlementAdultPriceStart, String settlementAdultPriceEnd, DepartmentCommon common);

	Page<TravelActivity> findTravelActivity(
			Page<TravelActivity> page, TravelActivity travelActivity,
			String settlementAdultPriceStart, String settlementAdultPriceEnd, DepartmentCommon common, String groundingStatus);
	/**
	 * 522
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 */
	Page<Map<Object, Object>> searchProductAndActivityList(
			Page<Map<Object, Object>> page, HttpServletRequest request,Model model);
	
	List<Map<String, Object>> findProductAndActivityList(String officeId, HttpServletRequest request,Model model);
	/**
	 * @Description 查询产品列表
	 * @author yakun.bai
	 * @Date 2016-1-9
	 */
	Page<TravelActivity> findTravelActivity(
			Page<TravelActivity> page, TravelActivity travelActivity, DepartmentCommon common, Map<String,String> map);

	/**
	 * @Description 查询团期列表
	 * @author yakun.bai
	 * @Date 2016-1-9
	 */
	Page<ActivityGroup> findActivityGroup(
			Page<ActivityGroup> page, TravelActivity travelActivity, DepartmentCommon common, Map<String,String> map);
	
	List<ActivityGroup> findGroupsByActivityId(TravelActivity travelActivity, Map<String,String> map);

	Page<TravelActivity> findTravelActivity(
			Page<TravelActivity> page, TravelActivity travelActivity,
			String settlementAdultPriceStart, String settlementAdultPriceEnd, Long agentId, DepartmentCommon common);
	
	Page<TravelActivity> findTravelActivity(
			Page<TravelActivity> page, TravelActivity travelActivity,
			String settlementAdultPriceStart, String settlementAdultPriceEnd, List<Integer> state);
	
	Long findTravelActivitysByCode(String groupCode);

	List<TravelActivity> findActivity(String activitySerNum,
			Long proId);
	/**
	 * 根据批发商ID查询产品
	 * @param companyId 批发商ID
	 * @param lazy 是否需要懒加载
	 * @return
	 */
	List<TravelActivity> findActivityByCompany(Long companyId, boolean lazy);
	
	Page<TravelActivity> findActivityByCompany(Page<TravelActivity> page, TravelActivity travelActivity);

	List<TravelActivity> findActivityByCompanyIgnoreDeleteFlag(Long companyId);
	
	Boolean updateTravelActivityWithSyncStatus(Long activityId, Integer status);

	/**
	 * 产品修改
	 * @param introduction 产品行程介绍
	 * @param costagreement 自费补充协议
	 * @param otheragreement 其他补充协议
	 * @param otherfile 其他文件
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws Exception 
	 */
	String modSave(String groupdata, TravelActivity travelActivity,
			HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes) throws Exception;

	/**
	 * 产品添加
	 * @param introduction 产品行程介绍
	 * @param costagreement 自费补充协议
	 * @param otheragreement 其他补充协议
	 * @param otherfile 其他文件
	 * @param travelActivity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 */
	String save(TravelActivity travelActivity, String groupOpenDateBegin,
			String groupCloseDateEnd, HttpServletRequest request,
			HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes, boolean is_after_supplement);
	/**
	 * 草稿上架操作
	 */
	void batchOnActivityTmp(List<Long> ids);
	
	/**
	 * 查找团号生成规则(新行者需求)
	 * @author jiachen
	 * @DateTime 2015年3月5日 下午12:02:37
	 * @return List<Object[]>
	 */
	List<Object[]> findGroupCodeRule();
	
	public List<ActivityGroup> getYwByGroupIds(List<Long> groupIdList);

	/**
	 * 通过团期 id和产品 id 来查找当前团期下 剩余的切位人数(即除了占切位的人数)
	 * @param activityGroupId 团期id
	 * @return
	 */
	public Integer getAllLeftpayReservePosition(Long activityGroupId, Long activityId);

	void delCruiseshipStockGrooupRelByActivityIds(List<Long> idlist);
   /**
    * c460--下架取消关联状态
    * 通过团期id更新表cruiseship_stock_group_rel的关联状态
    * @param idlist
    */
	void updateCruiseshipRelStatusByActivityId(List<Long> idlist);


//-----------t1t2需求-----------s--//
	/**
	 * T1首页的列表分页查询
	 * @param page
	 * @param travelActivity
	 * @param keywordSearching     搜索关键字
	 * @param fromArea             出发地id
	 * @param targetAreaIdList     目的地id,逗号分隔
	 * @param supplier
	 * @param groupOpenDateBegin   出团日期起始时间
	 * @param groupOpenDateEnd     出团日期结束时间
	 * @param activityDurationFrom 行程天数起始时间
	 * @param activityDurationTo   行程天数结束时间
	 * @param freePositionFrom     余位开始
	 * @param freePostionTo        余位结束
	 * @param pageNo               当前页码
	 * @param pageSize             每页记录数
	 * @return
	 */
	Page<TravelActivity> findActivityGroupInfos(Page<TravelActivity> page, TravelActivity travelActivity, String keywordSearching,
			String fromArea, String targetAreaIdList, String supplier, String groupOpenDateBegin, String groupOpenDateEnd, String activityDurationFrom, String activityDurationTo,
												String price, String freePositionFrom, String freePostionTo, String countryPara, String pageNo, String pageSize, String orderBy, String type);
//-----------t1t2需求-----------e--//

	
	
	
	/**
	 * 获取产品的出发城市 按数量降序排列
	 * @param hasCompany 
	 * @return
	 */
	List<Map<String,String>> getFromAreas(String activityKind,String companyId);
	/**
	 * 获取产品的目的地城市，按数量降序排列
	 * @param b 
	 * @return
	 */
	List<Map<String,String>> getTargetAreas(String activityKind,String companyId);
	/**
	 * 获取产品的旅游类型
	 * @return
	 */
	List<Map<String,String>> getTravelTypes(String activityKind,String companyId);
	/**
	 * 获取产品的产品类型
	 * @return
	 */
	List<Map<String,String>> getActivityTypes(String activityKind,String companyId);
	/**
	 * 获取产品的产品等级
	 * @return
	 */
	List<Map<String,String>> getActivityLevels(String activityKind,String companyId);

	Page<Map<Object, Object>> searchSPActivityList(
			Page<Map<Object, Object>> page, HttpServletRequest request,Model model, String companyId,String requestType);
	
	List<Map<Object, Object>> exportData();

	/**
	 * 通过groupCode查看详情
	 * @param activityId
	 * @param groupCode
	 */
	Map<String, Object> getDetail(Long activityId, String groupCode);

	int getCount(boolean b);

	boolean getChangedCount();

	int hasChanged(String groupId, String srcId);

	List<Object> searchChangedList(String groupId, String srcId);

	Object getDestinations(String activityKind,String companyId);

	Object getActivityKind(String activityKind, String companyId);

	void setFreeLog(TravelActivity source, TravelActivity activity);

	void setFreeLog(TravelActivity activity, ActivityGroup sourceGroup, Map<String,String> map);

	/**
	 *  获取团期平台上架状态和批发商上架权限状态
	 * @param groupId
	 * @return
	 */
	List<Map<String,String>> getGroupAndOfficeT1PermissionStatus(String groupId);

	/**
	 * 根据产品类型获取所有的产品的目的地,做数据初始化用。
	 * @param orderType
	 * @return
     */
	public List<Map<String,Object>> getAllActivityAreaIdsByType(Integer orderType);

	public void updateTouristLine(Long touristLineId, Long activityId);

}