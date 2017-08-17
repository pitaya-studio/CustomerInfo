package com.trekiz.admin.modules.activity.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.springframework.web.multipart.MultipartFile;

import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.sys.entity.Remind;

/**
 * 团期服务接口
 * @author ZhengZiyu
 *
 */
public interface IActivityGroupService {

	public abstract void save(Set<ActivityGroup> activityGroups);
	
	/**
	 * 根据团期ID查询单团类的部分信息，目前只适用于预报单
	 * @param groupId      团期ID
	 * @author shijun.liu
	 * @return
	 * @date 2015.12.25
	 */
	public List<Map<String, Object>> getProductInfoForForecast(Long groupId);
	
	/**
	 * 根据团期ID查询单团类的部分信息，目前只适用于结算单
	 * @param groupId      团期ID
	 * @author shijun.liu
	 * @return
	 * @date 2015.12.25
	 */
	public List<Map<String, Object>> getProductInfoForSettle(Long groupId);
	
	/**
	 * 单团类产品--预报单预计收款和退款数据
	 * @param productId           产品ID
	 * @param orderType           订单类型
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, Object>> getOrderAndRefundInfoForcast(Long productId,Integer orderType);
	
	/**
	 * 单团类产品--结算单收款明细和退款数据
	 * @param productId           产品ID
	 * @param orderType           订单类型
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, Object>> getOrderAndRefundInfoSettle(Long productId,Integer orderType);
	
	/**
	 * 保存单个activityGroup对象
	 * @author lihua.xu
	 * @param activityGroup
	 */
	public abstract void save(ActivityGroup activityGroup);

	public abstract List<ActivityGroup> getGroupListByActivityId(
			Integer srcActivityId);

	public abstract ActivityGroup findById(Long id);
	
	public abstract ActivityGroup findByGroupCode(String groupCode);

	public abstract void delGroup(ActivityGroup group) throws Exception;

	public abstract void saveGroups(List<ActivityGroup> groups);

	public abstract void delGroupsByIds(List<Long> ids) throws Exception;

	public abstract void execTravelActivityTask();

	public abstract void execOffLineTask();

	public abstract void batchOffLineActivity(String updateSql,
			List<String> ids, String activityStatus);

	public abstract void batchUpdateActivity(String updateSql,
			List<Object[]> datas);

	public abstract List<ActivityGroup> groupOpenDateRepeat(String activityId,
			String groupOpenDate, String groupid);
	
	public abstract boolean groupCodeValidator(String groupCode, String groupid);
	
	public abstract String groupCodeCheck(String groupCode);
	
	public abstract boolean groupNoCheck(String groupCode);

	public abstract String uploadGroupFile(MultipartFile file);
	/**
	 * ActivityGroup更新的字段：	
	 *  freePosition 
	 *  groupCloseDate
	 *  groupOpenDate
	 *  nopayReservePosition
	 *  payDeposit
	 *  payReservePosition
	 *  settlementAdultPrice
	 *  settlementcChildPrice
	 *  singleDiff
	 *  soldNopayPosition
	 *  soldPayPosition
	 *  suggestAdultPrice
	 *  suggestChildPrice
	 *  visaCountry
	 *  visaDate
	 *  <br/>
	 *  位置与团期信息一起更新。需要在使用方法前调用ActivityGroup的setPlus{field} 方法，以保证在并发情况下可以继续尝试执行三次更新操作而保证数据的正确性。
	 * @param activityGroup
	 * @param versionString
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException 
	 */
	public abstract void updateByOptLock(ActivityGroup activityGroup,
			String versionString) throws Exception;

	/**
	 * 只更新与位置有关的6个字段：
	 * <p>FreePosition
	 * <p>NopayReservePosition
	 * <p>PayReservePosition
	 * <p>PlanPosition
	 * <p>SoldNopayPosition
	 * <p>SoldPayPosition
	 * <br />
	 * 需要在使用方法前设置ActivityGroup属性为以上字段的setPlus{field} 方法，以保证在并发情况下可以继续尝试执行三次更新操作而保证数据的正确性。
	 * @param activityGroup
	 * @param versionString
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException 
	 */
	public abstract void updatePositionNumByOptLock(
			ActivityGroup activityGroup, String versionString)
			throws Exception;
	
	/**
	 * 通过ActivityGroup id更新ActivityGroup对应数据的costStatus属性
	 * @author lihua.xu
	 * @param groupId ActivityGroup id
	 * @param costStatus costStatus值
	 */
	public  void resetCostStatus(Long groupId,Integer costStatus);
	
	/**
	 * 
	 *  功能:获取当前用户所属批发商产品团期中在当前系统时间中的最大的团号+1
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-11-7 下午3:39:35
	 *  @return
	 */
	public abstract String getCurrentDateMaxGroupCode(String groupOpenDate);
	
	/**
	 * 为批发商环球行提供特定的团号生成规则
	 * @author jiachen
	 * @DateTime 2015年3月3日 下午4:03:21
	 * @return String
	 * 
	 * 规则：“  TTS-BJ-14TY-0001-0925“ 
	 * 			“  公司简称 + 地域+年份（后两位）+部门简称+序号+出团日期”
	 */
	public String getGroupNumForTTS(String deptId, String groupOpenDate);

	/**
	 * 获取指定格式团号的最大累加值
	 * @author jiachen
	 * @DateTime 2015年3月3日 下午5:16:56
	 * @return String
	 */
	public String getMaxCountIngroupNumForComp(String currentCode, String defaultCount);
	
	/**
	 * 将传入的数值处理成带有0补位的字符串  maxNum:指定数值 countLength:补位数(3、4)
	 * @author jiachen
	 * @DateTime 2015年3月4日 下午2:55:29
	 * @return String
	 */
	public String getZeroCode(String maxNum, int countLength);
	
	/**
	 * 获取当前供应商团号累加最大值
	 * @author jiachen
	 * @DateTime 2015年6月9日 上午11:46:12
	 * @param companyId
	 * @return Long
	 */
	public Long getMaxCountForSequence(int date);

	/**
	 * 依据产品类型获取所有产品信息
	 * @author yang.jiang
	 * @param paramMap 产品类型
	 * @return
	 */
	public JSONArray getAllActivityByType(Map<String, Object> paramMap);

	public JSONArray getAllActivityByType(Integer activityType);

	/**
	 * 获取所有的团期创建者
	 * @return
	 */
	public abstract List<Map<String, Object>> getAllCreators(Integer activityType);

	/**
	 * 获取remind对应的产品团期信息
	 * @author yang.jiang 2016-4-11 21:41:48
	 * @param remind　提醒生成规则
	 * @return
	 */
	public abstract JSONArray getActivityInfoByRemind(Remind remind);
	
	public void updateObj(ActivityGroup activityGroup);
	
	//containSelf:orderid
	Map<String, Object> countOrderChildAndSpecialNum(Long productGroupId,String containSelf);

	public abstract void updatePSStatusById(Long groupId, Integer status);
}