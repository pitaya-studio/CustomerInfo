package com.trekiz.admin.common.query.service;

import com.trekiz.admin.common.query.entity.SelectJson;
import com.trekiz.admin.common.query.entity.SelectOption;
import com.trekiz.admin.modules.order.entity.ZhifubaoInfo;

import java.util.List;
import java.util.Map;


public interface ISelectService {

	/**
	 * 读取部门
	 * @author wangyang
	 * */
	public List<Map<String,Object>> loadDepartment(Long companyId);


	public List<SelectOption> findAllAgentinfo();


	/**
	 * 获取计调和产品发布人
	 * @author chao.zhang@quauq.com
	 * @return
	 */
	public SelectJson loadGetOperators(Long companyId);
	/*
	 * 根据公司id获取sys_user下单人列表
	 * @param companyId 公司id
	 * @return 
	 * @author xianglei.dong
	 */
	public SelectJson loadSysUserPlaceOrderPersons();
	
	/*
	 * 根据公司id获取多表联合下单人列表
	 * @param companyId 公司id
	 * @return 
	 * @author xianglei.dong
	 */
	public SelectJson loadUnionPlaceOrderPersons();



	/**
	 * 根据公司获取所有的可发布信息的人
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public SelectJson loadAllUsers();



	/**
     * 查询所有渠道商
     * @return
     * @author jinxin.gao
     * @date   2016.04.08
     */
	public SelectJson loadFindAllAgentinfo();

    /**
     * 查询审批发起人
     * @return
     * @author shijun.liu
     * @date   2016.04.08
     */
    public SelectJson loadReviewer();
	/**
	 * 根据供应商的id，查询该供应商下的所有渠道商
	 * @return
	 * @author yudong.xu
	 */
	public SelectJson loadAgentInfoBySupplyId();

	/**
	 * 根据当前渠道跟进销售人员，查询该该销售人员下的渠道商
	 * @return
	 * @author yudong.xu
	 */
	public SelectJson loadAgentInfoBySalerId();
	
	/**
	 * 根据批发商id获得计调和计调主管
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public  SelectJson loadGetInnerOperator();
	
	/*
	 * 获取地接社
	 * @author xianglei.dong
	 */
	public SelectJson loadSupplier();

	/**
	 * 获取切位渠道商
	 * @author yudong.xu
	 */
	public  SelectJson loadStockAgentinfo();

	/**
	 * 根据登陆人员的不同，普通销售只返回自己的渠道商。财务，主管，经理返回公司所有的渠道商
	 * @author yudong.xu --2016/4/11--18:05
	 */
	public SelectJson loadAgents();
	
	/**
	 * 仅查询银行名称及对应id
	 * [0]为银行id，[1]为银行名称
	 * @author yang.wang
	 * @date 2016.4.27
	 * */
	@Deprecated
	public List<String[]> getOfficePlatBankInfoForSelect();
	
	/**
	 * 仅查询银行名称及对应id
	 * 返回类型为SelectJson类型
	 * @author yang.wang
	 * @date 2016.4.27
	 * */
	public SelectJson getOfficePlatBankInfoForSelectJson();
	
	public List<Map<String, String>> getFromBanks(Integer companyId);

	/**
	 * 获取某个批发商的支付宝账户
	 * @param compnayId
	 * @return
	 * @author shijun.liu
	 * @date 2016.07.19
     */
	public List<ZhifubaoInfo> getAlipay(Long compnayId);

	/**
	 * 获取某个批发商的支付宝名称，去除重复的名称
	 * @param compnayId
	 * @return
	 * @author shijun.liu
	 * @date 2016.07.28
	 */
	public List<ZhifubaoInfo> getDistinctAlipay(Long compnayId);

	/**
	 * 获取某个批发商的支付宝账户
	 * @param id
	 * @return
	 * @author chao.zhang
	 * @date 2016.07.19
     */
	public ZhifubaoInfo getAlipayById(Long id);
	
	/**
	 * 获取某个批发商的支付宝账户
	 * @param name
	 * @return
	 * @author chao.zhang
	 * @date 2016.07.19
     */
	public List<ZhifubaoInfo>  getAlipayByName(String name);
	/**
	 * 查询所有T1渠道
	 * @return
	 */
	public List<Map<String,Object>> getT1Agentinfos();
	
	/**
	 * 所有T1用户
	 * @return
	 */
	public List<Map<String,Object>> getT1User();
}
