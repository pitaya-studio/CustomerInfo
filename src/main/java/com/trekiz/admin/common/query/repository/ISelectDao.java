package com.trekiz.admin.common.query.repository;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.query.entity.SelectOption;

public interface ISelectDao extends BaseDao<SelectOption> {
	/**
	 * 根据公司id获得计调和产品发布人
	 * @param companyId  公司id
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<Map<String,Object>> getOperators(Long companyId);
	/*
	 * 根据公司id获取下单人列表
	 * @param companyId 公司id
	 * @return 
	 * @author xianglei.dong
	 */
	public List<Map<String, Object>> getUnionPlaceOrderPersons(Long companyId);
	
	/**
	 * 用于下拉框获取部门信息
	 * @author wangyang
	 * */
	public List<Map<String,Object>> loadDepartment(Long companyId);

	/**
     * 根据公司ID查询渠道商
     * @param userCompanyId     公司ID
     * @return List<Map<String, Object>>
     * @author jinxin.gao
     */
	public List<Map<String, Object>> getAgentInfo(Long userCompanyId);
	/**
     * 根据用户ID查询渠道商
     * @param userId     用户ID
     * @return	List<SelectOption>
     * @author jinxin.gao
     */
	public List<SelectOption> findAgentBySalerId(Long userId);

	/**
	 * 根据公司获得产品发布人
	 * @return
	 */
	public List<Map<String,Object>> getAllUsers();

    /**
     * 根据公司ID查询审批发起人
     * @param companyId     公司ID
     * @return
     * @author shijun.liu
     * @date   2016.04.08
     */
    public List<Map<String, Object>> getReviewer(Long companyId);

	/**
	 * 根据供应商的id，查询该供应商下的所有渠道商
	 * @param companyId
	 * @return
	 * @author yudong.xu
	 */
	public List<Map<String, Object>> getAgentInfoBySupplyId(Long companyId);

	/**
	 * 根据渠道跟进销售人员的id，查询该该销售人员下的渠道商
	 * @param salerId
	 * @return
	 * @author yudong.xu
	 */
	public List<Map<String, Object>> getAgentInfoBySalerId(Long salerId);
	
	/**
	 * 获得计调和计调主管
	 * @param
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<Map<String,Object>> getInnerOperator();
	
	/*
	 * 获取地接社
	 * @author xianglei.dong
	 */
	public List<Map<String, Object>> getSupplier();
	/**
	 * 获得销售
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> findInnerSales(Long companyId);

	/**
	 * 获取切位渠道商
	 * @author yudong.xu --2016/4/11--15:37
	 */
	public List<Map<String, Object>> getStockAgentinfo(Long companyId);
	
	/**
	 * 获取来款行信息
	 * @param companyId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<Map<String, String>> getFromBanks(Integer companyId);
}
