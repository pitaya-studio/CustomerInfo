package com.trekiz.admin.modules.activity.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;

public interface IGroupControlBoardService {

	/**
	 * 获取团控板首页数据集
	 * 
	 * @param nameOrCode
	 * @param groupOpenDateFrom
	 * @param groupOpenDateTo
	 * @return
	 */
	public List<Map<String, Object>> getGroupControlBoardList(
			String nameOrCode, String groupOpenDateFrom, String groupOpenDateTo);

	/**
	 * 获取团控板首页数据集
	 * 
	 * @param nameOrCode
	 * @param groupOpenDateFrom
	 * @param groupOpenDateTo
	 * @return
	 */
	public List<Map<String, Object>> getGroupControlBoardListNew(Page<ActivityGroup> page,
			String nameOrCode, String groupOpenDateFrom, String groupOpenDateTo) throws ParseException;

	/**
	 * 团控板操作页
	 * 
	 * @param groupId
	 * @param pageType
	 * @return
	 */
	public Map<String, Object> groupControlBoardOpePage(long groupId,
			String pageType);

	/**
	 * 团控板-操作记录
	 * 
	 * @return
	 */
	public Map<String, Object> groupContralBoardOpeRecord(
			HttpServletRequest request);

	/**
	 * 团控板-全部操作记录
	 * 
	 * @return
	 */
	public Map<String, Object> groupContralBoardOpeRecordAll(
			HttpServletRequest request);

	/**
	 * 插入团控板数据
	 * 
	 * @param opeType
	 *            操作项 1收客 2报名 3余位调整 4订单修改 5退团 6转团(转出团) 7转团(转入团) 8订单取消 9订单删除 10财务驳回取消占位
	 * @param amount
	 *            数量
	 * @param remarks
	 *            备注
	 * @param groupId
	 *            团期id
	 * @param createBy
	 *            操作者id(针对转团、退团操作、系统自动取消的订单传入,其他操作可传入-1)
	 * @return
	 */
	public Map<String, String> insertGroupControlBoard(Integer opeType,
			Integer amount, String remarks, long groupId, long createBy);

}
