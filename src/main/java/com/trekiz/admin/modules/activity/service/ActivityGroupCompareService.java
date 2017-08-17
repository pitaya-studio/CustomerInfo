package com.trekiz.admin.modules.activity.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.ActivityGroupCompare;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupCompareDao;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.order.repository.PreProductOrderCommonDao;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class ActivityGroupCompareService extends BaseService implements IActivityGroupCompareService {

	@Autowired
	private ActivityGroupCompareDao activityGroupCompareDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private PreProductOrderCommonDao preproductorderDao;
	
	/*
	 * 保存产品团期对比信息（单条）
	 * 
	 * @see
	 * com.trekiz.admin.modules.activity.service.IActivityGroupCompareService
	 * #save(com.trekiz.admin.modules.activity.entity.ActivityGroupCompare)
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveActivityGroupCompare(ActivityGroupCompare activityGroupCompare) throws Exception {
		ActivityGroupCompare result = activityGroupCompareDao
				.save(activityGroupCompare);
		// 如果产品团期不存在，则向上抛出异常
		if (result == null) {
			throw new Exception("保存失败!");
		}
	}

	/*
	 * 删除单个 产品团期对比信息
	 * 
	 * @see
	 * com.trekiz.admin.modules.activity.service.IActivityGroupCompareService
	 * #delActivityGroupCompare
	 * (com.trekiz.admin.modules.activity.entity.ActivityGroupCompare)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delActivityGroupCompare(Long operatorId, Long activityGroupId) throws Exception {
		// 根据 operatorId 和 activityGroupId 查询对应的产品团期信息
		ActivityGroupCompare activityGroupCompare = activityGroupCompareDao.findByProperties(operatorId, activityGroupId);
		// 如果产品团期存在则删除，不存在则向上抛出异常
		if (activityGroupCompare != null) {
			activityGroupCompareDao.delete(activityGroupCompare);
		} else {
			throw new Exception("要删除的产品团期不存在！");
		}
	}

	/*
	 * 执行批量的保存产品团期对比信息的操作
	 * 
	 * @see
	 * com.trekiz.admin.modules.activity.service.IActivityGroupCompareService
	 * #saveAllActivityGroupCompares(java.util.List)
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveAllActivityGroupCompares(List<ActivityGroupCompare> activityGroupCompares) {
		// 执行批量的保存产品团期对比信息的操作
		activityGroupCompareDao.batchSave(activityGroupCompares);
	}

	/*
	 * 将一个产品下可以加入对比的团期全部从产品团期对比信息表删除
	 * 
	 * @see
	 * com.trekiz.admin.modules.activity.service.IActivityGroupCompareService
	 * #delAllActivityGroupCompares(java.util.List)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delAllActivityGroupCompares(List<ActivityGroupCompare> activityGroupCompares) {
		// 如果 activityGroupCompares不为空，则通过循环删除所有的 activityGroupCompare
		if (activityGroupCompares.size() > 0 && activityGroupCompares != null) {
			for (ActivityGroupCompare activityGroupCompare : activityGroupCompares) {
				activityGroupCompareDao.delete(activityGroupCompare);
			}
		}

	}

	/* 
	 * 将当前操作员下所有产品团期对比信息删除
	 * 
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupCompareService#clearAllActivityGroupCompares(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void clearAllActivityGroupCompares(Long operatorId) throws Exception {
		List<ActivityGroupCompare> activityGroupCompares = activityGroupCompareDao.findByPropertie(operatorId);
		if(activityGroupCompares.size() == 0 || activityGroupCompares == null) {
			throw new Exception("当前操作员下没有产品团期对比信息");
		} else {
			for (ActivityGroupCompare activityGroupCompare : activityGroupCompares) {
				activityGroupCompareDao.delete(activityGroupCompare);
			}
		}
	}

	/* 通过 operatorId 和 activityGroupId 查询对应的 ActivityGroupCompare并返回
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupCompareService#findByProperties(java.lang.Long, java.lang.Long)
	 */
	@Override
	public ActivityGroupCompare findByProperties(Long operatorId, Long activityGroupId) {
		ActivityGroupCompare activityGroupCompare = activityGroupCompareDao.findByProperties(operatorId, activityGroupId);
		return activityGroupCompare;
	}
	
	/**
	 * @Description 根据用户id查询对比团期
	 * @author yakun.bai
	 * @Date 2015-10-23
	 */
	@Override
	public List<ActivityGroup> getCompareGroup(Long userId) {
		List<ActivityGroup> activityGroupCompares = activityGroupCompareDao.findCompareGroup(userId);
		return activityGroupCompares;
	}

	
	
	/* 返回当前要下载的产品团期对比列表
	 * @see com.trekiz.admin.modules.activity.service.IActivityGroupCompareService#getActivityGroupCompareList(java.lang.String[])
	 */
	@Override
	@Transactional(readOnly = false)
	public List<Object[]> getActivityGroupCompareList(String[] groupCodes) {
		//要返回的产品团期对比信息列表
		List<Object[]> activityGroupCompareList = new ArrayList<>();
		//根据团号查询对应的团期信息，产品信息，机票信息
		for (String groupCode : groupCodes) {
			Object[] objArr = new Object[22];
			//根据 groupCode 获取 activityGroup 
			ActivityGroup activityGroup = activityGroupDao.findByGroupCodeAndCompany(groupCode, 
					UserUtils.getUser().getCompany().getId()).get(0);
			//为当前 activityGroup团期产品设置预报名人数
			setOrderPersonNum2ActivityGroup(activityGroup);
			
			// 根据 activityGroup（团期信息） 获取 travelActivity(产品信息)
			TravelActivity travelActivity = null;
			if(activityGroup != null) {
				travelActivity = activityGroup.getTravelActivity();
			}
			// 根据travelActivity(产品信息获取机票信息)
			ActivityAirTicket airTicket = null;
			if(travelActivity != null) {
				airTicket = travelActivity.getActivityAirTicket();
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
			if(activityGroup != null ){
				//团号
				objArr[0] = activityGroup.getGroupCode() == null ? "" : activityGroup.getGroupCode();
				//出团日期
				objArr[2] = activityGroup.getGroupOpenDate() == null ? "" : dateFormat.format(activityGroup.getGroupOpenDate());
				//截团日期
				objArr[3] = activityGroup.getGroupCloseDate() == null ? "" : dateFormat.format(activityGroup.getGroupCloseDate());
				//资料截止日期
				objArr[4] = activityGroup.getVisaDate() == null ? "" : dateFormat.format(activityGroup.getVisaDate());
				//成人同行价
				objArr[5] = activityGroup.getSettlementAdultPrice() == null ? "" : activityGroup.getSettlementAdultPrice();
				//儿童同行价
				objArr[6] = activityGroup.getSettlementcChildPrice() == null ? "" : activityGroup.getSettlementcChildPrice();
				//特殊人群同行价
				objArr[7] = activityGroup.getSettlementSpecialPrice()==null?"":activityGroup.getSettlementSpecialPrice();
				//成人直客价
				objArr[8] = activityGroup.getSuggestAdultPrice() == null ? "" : activityGroup.getSuggestAdultPrice();
				//儿童直客价
				objArr[9] = activityGroup.getSuggestChildPrice() == null ? "" : activityGroup.getSuggestChildPrice();
				//特殊人群直客价
				objArr[10] = activityGroup.getSuggestSpecialPrice() == null ? "" : activityGroup.getSuggestSpecialPrice();
				//单房差/间夜
				objArr[11] = activityGroup.getSingleDiff() == null ? "" : activityGroup.getSingleDiff();
				//预报名
				objArr[12] = activityGroup.getOrderPersonNum() == null ? "" : activityGroup.getOrderPersonNum();
				//预收
				objArr[13] = activityGroup.getPlanPosition() == null ? "" : activityGroup.getPlanPosition();
				//售出切位
				objArr[14] = activityGroup.getSoldPayPosition();
				//已切位
				objArr[15] = activityGroup.getPayReservePosition();
				//余位
				objArr[16] = activityGroup.getFreePosition() == null ? "" : activityGroup.getFreePosition();
				//签证国家
				objArr[20] = activityGroup.getVisaCountry() == null ? "" : activityGroup.getVisaCountry();
			} else {
				objArr[0] = "";
				objArr[2] = "";
				objArr[3] = "";
				objArr[4] = "";
				objArr[5] = "";
				objArr[6] = "";
				objArr[7] = "";
				objArr[8] = "";
				objArr[9] = "";
				objArr[10] = "";
				objArr[11] = "";
				objArr[12] = "";
				objArr[13] = "";
				objArr[14] = "";
				objArr[15] = "";
				objArr[16] = "";
				objArr[20] = "";
			}
			
			if(travelActivity != null) {
				//产品名称
				objArr[1] = travelActivity.getAcitivityName() == null ? "" : travelActivity.getAcitivityName();
				//产品系列
				objArr[17] = travelActivity.getActivityLevelName()  == null ? "" : travelActivity.getActivityLevelName();
				//出发地
				Map<String, String> findUserDict = DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"fromarea");
				String fromAreaName = null;
				if(travelActivity.getFromArea() != null) {
					fromAreaName = findUserDict.get(travelActivity.getFromArea().toString());
				}
				objArr[18] = fromAreaName  == null ? "" : fromAreaName;
			} else {
				objArr[1] = "";
				objArr[17] = "";
				objArr[18] = "";
			}
			
			if(airTicket != null) {
				String airlines = airTicket.getAirlines();
				if(StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
					//航空公司
					objArr[19] = airlines;
				}
			} else {
				objArr[19] = "";
			}
			
			String userName = UserUtils.getUser().getName();
			objArr[21] = userName;
			activityGroupCompareList.add(objArr);
		}
		return activityGroupCompareList;
	}
	
	
	/**
	 * 为传入的 activityGroup 计算 预报名总人数
	 * @param activityGroup
	 */
	private void setOrderPersonNum2ActivityGroup(ActivityGroup activityGroup) {
		StringBuilder sql =  new StringBuilder(" select IFNULL(sum(orderPersonNum),0),sum(payMode) from preproductorder where orderType = 0 and productGroupId = ").append(activityGroup.getId());
		Object[] orderPersonNumObj = (Object[]) preproductorderDao.findBySql(sql.toString()).get(0);
		activityGroup.setOrderPersonNum(Integer.parseInt(orderPersonNumObj[0].toString()));
	}

	/**
	 * 保存对比排序值
	 * @Description 
	 * @author yakun.bai
	 * @Date 2015-11-2
	 */
	public void saveCompareSortName(Long userId, String sortName) {
		activityGroupCompareDao.saveCompareSortName(userId, sortName);
	}
}
