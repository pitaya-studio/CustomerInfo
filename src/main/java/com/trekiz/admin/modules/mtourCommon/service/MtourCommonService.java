/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.modules.mtourCommon.jsonbean.BankInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.CityInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SupplierInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SysCompanyDictViewJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.UserInfoJsonBean;


/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface MtourCommonService{
	/**
	 * 
	 * @author gao
	 * 2015年10月14日
	 * @param tourOperatorTypeUuid 地接社类型uuid
	 * @param tourOperatorName 地接社类型名称
	 * @return
	 */
	public List<SupplierInfoJsonBean> getSupplierList(String tourOperatorTypeUuid,String tourOperatorName,int count);
	
	/**
	 * 根据字典表类型获取字典表信息
	     * <p>@Description TODO</p>
		 * @Title: getSysCompanyDictListByType
	     * @return List<SysCompanyDictJsonBean>
	     * @author majiancheng       
	     * @date 2015-10-14 下午2:37:00
	 */
	public List<SysCompanyDictViewJsonBean> getDictListByType(String type);
	

	public List<UserInfoJsonBean> getUserByRoleType(String roleType,String userName, String count);
	
	/**
	 * 获取渠道信息 
	 * @author ang.gao
	 * @param channelTypeCode
	 * @param channelName
	 * @param count
	 * @return
	 */
	public List<Map<String,Object>> getAgentinfoByTypeCode(String channelTypeCode,String channelName,String count);
	
	/**
	 * 获取银行信息
	 * @author zhaohaiming	
	 * @param id 渠道、地接社、供应商ID
	 * @param type 0表示批发商,1表示地接社,2表示渠道
	 * @return list
	 * */
    public List<BankInfoJsonBean> getBankInfo(Long id,String Type);

    /**
	 * 模糊查询国家
	 * @author hhx
	 * @param input
	 * @return List<Map<String,String>>
     * @throws UnsupportedEncodingException 
     * @throws Exception 
	 */
	public List<Map<String,String>> getCountryVaguely(BaseInput4MT input) throws Exception;
	
	/**
	 * 模糊查询航空公司
	 * @author hhx
	 * @param input
	 * @return List<Map<String,String>>
	 * @throws Exception 
	 */
	public List<Map<String,String>> getAirlineVaguely(BaseInput4MT input) throws Exception;
	
	/**
	 * 模糊查询机场信息
	 * @author hhx
	 * @param input
	 * @return List<Map<String,String>>
	 * @throws Exception 
	 */
	public List<Map<String,String>> getAirportVaguely(BaseInput4MT input) throws Exception;
	
	/**
	 * 查询币种
	 * @author hhx
	 * @param input
	 * @return List<Map<String,String>>
	 */
	public List<Map<String,String>> getCurrency(BaseInput4MT input);
	
	/**
	 * 查询城市
	 * @author gao
	 * 2015年10月20日
	 * @param input
	 * @return
	 */
	public List<CityInfoJsonBean> getCityInfo(String cityKey,int count);
	
	/**
	 * 查询当前用户的menu
	 * @author ning.zhang@quauq.com
	 * @date 2015年10月22日17:32:53
	 * @return Never be null
	 */
	public Object[] getMenuInfoByCurrentUser();
	
	/**
	 * 根据查询类型,输入文本统计全部数量
	 * @author hhx
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> countAllInfo(BaseInput4MT input);
	
	/**
	 * 根据查询类型,输入文本统计订单数量
	 * @author hhx
	 * @param input
	 * @return
	 */
	public Map<String, Object> countOrders(BaseInput4MT input);

	/**
	 * 全站统计信息--付款信息数量
	 * @author zhankui.zong
	 * @param searchType
	 * @param searchKey
	 * @return
	 */
	public Map<String, Object> getPaymentCount(Integer searchType,
			String searchKey);

	/**
	 * 全站统计信息--收款信息数量
	 * @author zhankui.zong
	 * @param searchType
	 * @param searchKey
	 * @return
	 */
	public Map<String, Object> getReceiptCount(Integer searchType,
			String searchKey);
	
	/**
	 * 获取当前用户的json数据
		 * @Title: getCurrentUserInfo
	     * @return Map<String,Object>
	     * @author majiancheng       
	     * @date 2015-10-26 下午2:09:43
	 */
	public UserInfoJsonBean getCurrentUserInfo();
	/**
	 * 根据字典表类型获取字典表信息
	     * <p>@Description TODO</p>
		 * @Title: getSysCompanyDictListByType
	     * @return List<SysCompanyDictJsonBean>
	     * @author WangXK       
	     * @date 2015-11-15 下午2:37:00
	 */
	List<SysCompanyDictViewJsonBean> getDictListByTypeAndCompanyId(Long companyId,String dictType);

	public Map<String, Object> orderStatisticsInfo(Long currentUserCompanyId);
	
	/**
	 * 获得欧洲
	 * @param input
	 * @return
	 * @throws Exception
	 * @author chao.zhang
	 */
	public List<Map<String,String>> getEU(BaseInput4MT input) throws Exception;
}
