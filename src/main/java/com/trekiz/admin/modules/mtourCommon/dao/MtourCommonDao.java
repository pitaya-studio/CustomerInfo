/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.dao;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.mtourCommon.entity.MenuInfoTempBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.CityInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.UserInfoJsonBean;
import com.trekiz.admin.modules.mtourOrder.jsonbean.PNRRecordJsonBean;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public interface MtourCommonDao  extends BaseDao {
	
	/**
	 * 根据 参数返回地接社uuid和名称
	 * @author gao
	 * @param tourOperatorTypeUuid
	 * @param tourOperatorName
	 * @return
	 */
	public List<SupplierInfo> getSupplierList(String tourOperatorTypeUuid,String tourOperatorName,int count);

	public List<UserInfoJsonBean> getUserByRoleType(String roleType, String userName, String count);
	
	public List<Map<String,Object>> getAgentinfoByTypeCode(String channelTypeCode, String channelName, String count);
	/**
	 * 根据 参数，返回城市uuid和名称
	 * @author gao
	 * 2015年10月20日
	 * @param sql
	 * @param cityKey
	 * @param count
	 * @return
	 */
	public List<CityInfoJsonBean> getCityInfo(String cityKey,Integer count);
	
	public List<PNRRecordJsonBean> getPNRRecord(String orderId,String invoiceOriginalGroupUuid);
	/**
	 * 根据userid 查询其相关的menu 
	 * @param userId 用户id
	 * @author ning.zhang@quauq.com
	 * @date 2015年10月22日16:54:48
	 * @return Never be Null 
	 */
	public List<MenuInfoTempBean> getMenuInfoByUserId(long userId);

	/**
	 * 查询基础信息表中维护的币种信息
	 * @return
	 * @author shijun.liu
	 * @date 2016.03.14
	 */
	public List<Map<String,String>> getCurrencyList();
}	
