package com.trekiz.admin.modules.activity.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.ActivityGroupCostView;
import com.trekiz.admin.modules.activity.entity.HotelGroupCostView;
import com.trekiz.admin.modules.activity.entity.IslandGroupCostView;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.cost.entity.GroupManagerEntity;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;


/**
 * 旅游产品信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public interface IActivityGroupCostViewService {

	Page<ActivityGroupCostView> findGroupCostReview(Page<ActivityGroupCostView> page,TravelActivity travelActivity,
			String groupCode,Long supplierId,Long agentId,Integer  activityKind,String review,Integer nowLevel, 
			Long companyId,Long reviewCompanyId,Integer flowType,DepartmentCommon common,String createByName);
	
	/**
	 * 查询团队管理数据
	 * @param entity           参数对象
	 * @param orderType        产品类型
	 * @param companyId        公司ID
	 * @author shijun.liu
	 * @return
	 */
	public Page<Map<String, Object>> findGroup(Page<Map<String, Object>> page, GroupManagerEntity entity, Integer orderType, Long companyId);


	/**
	 * 查询团队管理明细数据
	 * @param entity           参数对象
	 * @param orderType        产品类型
	 * @param companyId        公司ID
	 * @author shijun.liu
	 * @return
	 */
	public Page<Map<String, Object>> findGroupDetail(Page<Map<String, Object>> page, GroupManagerEntity entity, Integer orderType, Long companyId);

	public Page<IslandGroupCostView> findIslandCostReview(
			Page<IslandGroupCostView> page, ActivityIslandGroup activityIslandGroup,
			Long supplierId, Long agentId, String review,
			Integer nowLevel, Long companyId, Long reviewCompanyId,
			Integer flowType, DepartmentCommon common, String createByName);

	public Page<HotelGroupCostView> findHotelCostReview(Page<HotelGroupCostView> page,
			ActivityHotelGroup activityHotelGroup,  Long supplierId,
			Long agentId, String review, Integer nowLevel, Long companyId,
			Long reviewCompanyId, Integer flowType, DepartmentCommon common,
			String createByName);

	public Page<IslandGroupCostView> findIslandPayReview(
			Page<IslandGroupCostView> page,
			ActivityIslandGroup activityIslandGroup, String activityName,
			String hotel, String island, String currencyId,
			String startCurrency, String endCurrency, Long supplierId,
			Long agentId, String review, Integer nowLevel, Long companyId,
			Long reviewCompanyId, Integer flowType, DepartmentCommon common,
			String createByName);

	public Page<HotelGroupCostView> findHotelPayReview(Page<HotelGroupCostView> page,
			ActivityHotelGroup activityHotelGroup, String activityName,
			String hotel, String island, String currencyId,
			String startCurrency, String endCurrency, Long supplierId,
			Long agentId, String review, Integer nowLevel, Long companyId,
			Long reviewCompanyId, Integer flowType, DepartmentCommon common,
			String createByName);

	
	/**
	 * 团队管理-应收，已收，达账金额合计查询
	 * @param moneyType          金额类型，13：totlaMoney ,5： payedMoney ,4：accountedMoney
	 * @param entity
	 * @param orderType	团队类型
	 * @param companId	供应商ID
	 * @return	list
	 * @author zhaohaiming
	 * **/
    public List<Map<String,Object>> getMoneySum(int moneyType,GroupManagerEntity entity,Integer orderType,Long companyId);
    
    /**
     *金额转换，
     * @param List<Map<String,Object>> list
	 * @return	String
	 * @author zhaohaiming
	 * **/
    public String getMoneySumStr(List<Map<String,Object>> list);
    /**
     * 团队管理-应付总额
     * @param entity
     * @param orderType 
     * @param companyId
     * @return list
     * @author zhaohaiming
     * */
    public List<Map<String,Object>> getCostTotal(GroupManagerEntity entity,Integer orderType, Long companyId);
    /**
     * 团队管理-利润总额(达账总额-应付总额)
     * @param moneyType          金额类型，13：totlaMoney ,5： payedMoney ,4：accountedMoney
	 * @param entity
	 * @param orderType	团队类型
	 * @param companId	供应商ID
	 * @return	String
	 * @author zhaohaiming
	 * **/
    public String getProfitTotal(int moneyType,GroupManagerEntity entity,Integer orderType,Long companyId);
    
    /**
     * 团队管理-利润总额(应收总额-应付总额)
     * @param totalMoneyList    应收总额币种和金额列表
	 * @param costTotalList		应付总和币种和金额列表
	 * @return	String
	 * @author xianglei.dong
	 * **/
    public String getProfitTotal(List<Map<String,Object>> totalMoneyList, List<Map<String,Object>> costTotalList);
    
    /**
     * 计算提成状态更新
     * @author zhaohaiming
     * */
    public boolean updateIscommissionStatus(HttpServletRequest request,HttpServletResponse response);

    /**
     * 团队管理--总人数
     * @param entity
     * @param orderS
     * @param companyId
     * @return
     */
	Integer getTotalPerson(GroupManagerEntity entity, String orderS,
			Long companyId);
	
	/**
	 * 团队管理查询所有
	 */
	public List<Map<String,Object>> getAllInfoMap(GroupManagerEntity entity,
			Integer orderType, Long companyId);
}
