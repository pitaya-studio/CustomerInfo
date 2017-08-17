/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotel.dao.SysdefinedictDao;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.mtourCommon.dao.MtourCommonDao;
import com.trekiz.admin.modules.mtourCommon.entity.MenuInfoTempBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.AccountInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.BankInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.CityInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SecondLevelMenuInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SupplierInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SysCompanyDictViewJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.ThirdLevelMenuInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.TopLevelMenuInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.UserInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.service.MtourCommonService;
import com.trekiz.admin.modules.mtourCommon.transfer.MenuInfoTransfer;
import com.trekiz.admin.modules.mtourCommon.transfer.SupplierTransfer;
import com.trekiz.admin.modules.mtourCommon.transfer.SysCompanyDictViewTransfer;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderMoneyAmountDao;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class MtourCommonServiceImpl  extends BaseService implements MtourCommonService{
	@Autowired
	private MtourCommonDao mtourCommonDao;
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;
	@Autowired
	private PlatBankInfoDao platBankInfoDao;
	@Autowired
	private AirticketOrderMoneyAmountDao airticketOrderMoneyAmountDao;
	@Autowired
	private SysdefinedictDao sysdefinedictDao;

	/**
	 * 根据地接社类型uuid值、地接社名称、返回个数，输出地接社列表
	 * @author gao
	 * @param tourOperatorTypeUuid 地接社类型值
	 * @param tourOperatorName 地接社名称
	 * @param count 返回个数
	 */
	@Override
	public List<SupplierInfoJsonBean> getSupplierList(
			String tourOperatorTypeUuid, String tourOperatorName,int count) {
		
		List<SupplierInfo> infoList = mtourCommonDao.getSupplierList(tourOperatorTypeUuid,tourOperatorName,count);
		List<SupplierInfoJsonBean> list = SupplierTransfer.supplierInfo2JsonBean(infoList);//后台数据库对象转换成前端需要的对象
		return list;
	}
	public List<SysCompanyDictViewJsonBean> getDictListByType(String type) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<SysCompanyDictViewJsonBean> jsonBeanList = new ArrayList<SysCompanyDictViewJsonBean>();
		if (companyId!=null){
			if(companyId.equals("-1")){
				List<SysDict> list= sysCompanyDictViewDao.findByType(type);
				jsonBeanList = SysCompanyDictViewTransfer.dictBeans2DictJsonBeans(list);
			}else{
				List<SysDict> list= sysCompanyDictViewDao.findByType(type);
				List<SysCompanyDictViewJsonBean> jsonBeanList1 = SysCompanyDictViewTransfer.dictBeans2DictJsonBeans(list);
				
				List<Sysdefinedict> list1= sysdefinedictDao.findByCompanyIdAndType(companyId, type);
				List<SysCompanyDictViewJsonBean> jsonBeanList2 = SysCompanyDictViewTransfer.SysdefinedictBeans2DictJsonBeans(list1);
				if(jsonBeanList!=null){
					if(jsonBeanList1!=null){
						jsonBeanList.addAll(jsonBeanList1);
					}
					if(jsonBeanList2!=null){
						jsonBeanList.addAll(jsonBeanList2);
					}
				}
				
				
			}
		}
		
		return jsonBeanList;
	}
	
	@Override
	public List<UserInfoJsonBean> getUserByRoleType(String roleType, String userName, String count) {
		List<UserInfoJsonBean> infoList = mtourCommonDao.getUserByRoleType(roleType, userName, count);
		return infoList;
	}

	@Override
	public List<Map<String,Object>> getAgentinfoByTypeCode(String channelTypeCode, String channelName, String count) {
		List<Map<String,Object>> infoList = mtourCommonDao.getAgentinfoByTypeCode(channelTypeCode, channelName, count);
		
		return infoList;
	}
	

	/**
	 * 获取银行信息
	 * @author zhaohaiming	
	 * @param id 渠道、地接社、供应商ID
	 * @param type 0表示批发商,1表示地接社,2表示渠道
	 * @return list
	 * */
	@Override
	public List<BankInfoJsonBean> getBankInfo(Long id, String type) {
		//获取银行名称
		List<String> bn = platBankInfoDao.getBankName(id, Integer.valueOf(type));
		//根据银行名称获取银行账号拼装BankInfoJsonBean集合
		List<BankInfoJsonBean> list = bean2jsons(bn,id,type);
		return list;
	}
	
	public  List<BankInfoJsonBean> bean2jsons(List<String> list,Long id,String type){
		if(CollectionUtils.isNotEmpty(list)) {
			List<BankInfoJsonBean> jsonBeans = new ArrayList<BankInfoJsonBean>();
			for(String str : list) {
				jsonBeans.add(bean2json(str,id,Integer.valueOf(type)));
			}
			return jsonBeans;
		}
		return null;
	}
	
	public  BankInfoJsonBean bean2json(String obj,Long id,Integer type){
		if(obj != null) {
			BankInfoJsonBean jsonBean = new BankInfoJsonBean();
			jsonBean.setBankName(obj);
			//获取银行账号
			List<String> bc = platBankInfoDao.getBankAccount(id, type, obj);
			if(CollectionUtils.isNotEmpty(bc)){
				List<AccountInfoJsonBean> list = new ArrayList<AccountInfoJsonBean>();
				for(String str: bc){
					AccountInfoJsonBean accountInfo = new AccountInfoJsonBean();
					accountInfo.setAccountNo(str);
					list.add(accountInfo);
				}
			   jsonBean.setAccounts(list);
			}
			return jsonBean;
		}
		return null;
	}
	/**
	 * 模糊查询国家
	 * @author hhx
	 * @param input
	 * @return List<Map<String,String>>
	 * @throws Exception 
	 */
	public List<Map<String,String>> getCountryVaguely(BaseInput4MT input) throws Exception {
		StringBuffer sql = new StringBuffer("select sg.id,sg.uuid as countryUuid,sg.name_cn as countryName from sys_geography sg where sg.level=1 and sg.delflag='0' ");
		String key = input.getParamValue("countryKey");
		if(StringUtils.isNotBlank(key)){
			sql.append(" and (sg.name_cn like '%"+key+"%' " +
					   " or sg.name_short_cn like '%"+key+"%' " +
					   " or sg.name_pinyin like '%"+key+"%' " +
					   " or sg.name_short_pinyin like '%"+key+"%') ");
		}
		int count = Integer.parseInt(input.getParamValue("count"));
		if(count!=-1){
			sql.append(" limit "+Integer.valueOf(count));
		}
		return mtourCommonDao.findBySql(sql.toString(), Map.class);
	}
	
	/**
	 * 模糊查询航空公司
	 * @author hhx
	 * @param input
	 * @return List<Map<String,String>>
	 * @throws Exception 
	 */
	public List<Map<String,String>> getAirlineVaguely(BaseInput4MT input) throws Exception {
		StringBuffer sql = new StringBuffer("select distinct sai.airline_code as airlineCompanyUuid,sai.airline_name as airlineCompanyName from sys_airline_info sai where sai.del_flag='0' and user_mode='1' and company_id = ? ");
		String key = input.getParamValue("airlineCompanyKey");
		if(StringUtils.isNotBlank(key)){
//			sql.append(" and (sai.airline_name like '%"+key+"%' " +
//					   " or sai.airline_code like '%"+key+"%') ");
			sql.append(" and sai.airline_code like '%"+key+"%' ");
		}
		int count = Integer.parseInt(input.getParamValue("count"));
		if(count!=-1){
			sql.append(" limit "+Integer.valueOf(count));
		}
		return mtourCommonDao.findBySql(sql.toString(), Map.class, UserUtils.getUser().getCompany().getId());
	}
	
	/**
	 * 模糊查询机场信息
	 * @author hhx
	 * @param input
	 * @return List<Map<String,String>>
	 * @throws Exception 
	 */
	public List<Map<String,String>> getAirportVaguely(BaseInput4MT input) throws Exception {
		StringBuffer sql = new StringBuffer("select sai.id,sai.airport_code as airportUuid,sai.airport_name as airportName,sai.airport_code as airportCode from sys_airport_info sai where sai.del_flag='0' and user_mode='1' and company_id = ? ");
		String key = input.getParamValue("airportKey");
		if(StringUtils.isNotBlank(key)){
			sql.append(" and (sai.airport_name like '%"+key+"%' " +
					   " or sai.airport_code like '%"+key+"%') ");
		}
		int count = Integer.parseInt(input.getParamValue("count"));
		if(count!=-1){
			sql.append(" limit "+Integer.valueOf(count));
		}
		return mtourCommonDao.findBySql(sql.toString(), Map.class, UserUtils.getUser().getCompany().getId());
	}
	
	/**
	 * 查询币种
	 * @author hhx
	 * @param input
	 * @return List<Map<String,String>>
	 */
	public List<Map<String,String>> getCurrency(BaseInput4MT input) {
		//select c.currency_id as currencyUuid,c.currency_name as currencyName,c.currency_mark as currencyCode,c.currency_exchangerate as convertLowest from currency c where c.del_flag='0' and c.display_flag='1' and c.create_company_id = ? 修改sql update by zhanghao
		StringBuffer sql = new StringBuffer("select c.currency_id as currencyUuid,c.currency_name as currencyName,c.currency_mark as currencyCode,c.convert_lowest as convertLowest from currency c where c.del_flag='0' and c.display_flag='1' and c.create_company_id = ? ");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, UserUtils.getUser().getCompany().getId());
	}
	
	/**
	 * 查询城市
	 * @author gao
	 * 2015年10月20日
	 * @param input
	 * @return
	 */
	@Override
	public List<CityInfoJsonBean> getCityInfo(String cityKey,int count) {
		
		return mtourCommonDao.getCityInfo(cityKey,count);
	}
	
	/**
	 * 查询当前用户的menu
	 * @author ning.zhang@quauq.com
	 * @Date 2015年10月22日17:34:56
	 */
	@Override
	public Object[] getMenuInfoByCurrentUser() {
		boolean hasIsolatedMenu = false;
		StringBuffer IsolatedMenus = new StringBuffer();
		List<TopLevelMenuInfoJsonBean> topList = new LinkedList<>();
		List<MenuInfoTempBean>  infoList = mtourCommonDao.getMenuInfoByUserId(UserUtils.getUser().getId());
		TreeMap<Long ,Object> idJsonBeanMap = new TreeMap<>();
		for(MenuInfoTempBean info : infoList){
			int count = StringUtils.countMatches(info.getParentIds(), ",");
			if(count == 2 ){ //业务第一级目录
				TopLevelMenuInfoJsonBean jsonBean = MenuInfoTransfer.MenuInfo2TopLevelMenuJsonBean(info);
				topList.add(jsonBean);
				idJsonBeanMap.put(info.getId(), jsonBean);
			}else{
				continue; // 非业务第一级目录不处理
			}
		}
		
		for(MenuInfoTempBean info : infoList){
			int count = StringUtils.countMatches(info.getParentIds(), ",");
			if(count == 3){ //业务第二级目录
				long parentId = info.getParentId();
				Object o = idJsonBeanMap.get(parentId);
				if(null == o){//发现无父的节点
					hasIsolatedMenu = true;
					IsolatedMenus.append(info.getId());
					IsolatedMenus.append(" ");
					continue;
				}
				if(o instanceof TopLevelMenuInfoJsonBean){
					TopLevelMenuInfoJsonBean parentJsonBean = (TopLevelMenuInfoJsonBean)o;
					SecondLevelMenuInfoJsonBean  jsonBean = MenuInfoTransfer.MenuInfo2SecondLevelMenuJsonBean(info);
					idJsonBeanMap.put(info.getId(), jsonBean);
					parentJsonBean.addSubMenu(jsonBean);
				}else{
					throw new RuntimeException("发现有menu parents定义与parent定义不一致的情况"+ info.getId());
				}
			}else{
				continue; // 非业务第二级目录不处理
			}
		}
		
		for(MenuInfoTempBean info : infoList){
			int count = StringUtils.countMatches(info.getParentIds(), ",");
			if(count == 4){ // 业务第三级目录
				long parentId = info.getParentId();
				Object o = idJsonBeanMap.get(parentId);
				if(null == o){//发现无父的节点
					hasIsolatedMenu = true;
					IsolatedMenus.append(info.getId());
					IsolatedMenus.append(" ");
					continue;
				}
				if(o instanceof SecondLevelMenuInfoJsonBean){
					SecondLevelMenuInfoJsonBean parentJsonBean = (SecondLevelMenuInfoJsonBean)o;
					ThirdLevelMenuInfoJsonBean  jsonBean = MenuInfoTransfer.MenuInfo2ThirdLevelMenuJsonBean(info);
					parentJsonBean.addRole(jsonBean);
				}else{
					throw new RuntimeException("发现有menu parents定义与parent定义不一致的情况"+ info.getId());
				}

			}else{
				continue; // 非业务第三级目录不处理
			}
		}
		Object[] objs = new Object[3];
		objs[0] = hasIsolatedMenu;
		objs[1] = IsolatedMenus.toString();
		objs[2] = topList;
		return objs;
	}
	
	/**
	 * 根据查询类型,输入文本统计全部数量
	 * @author hhx
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> countAllInfo(BaseInput4MT input){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		JSONObject jsonObject = JSONObject.parseObject(input.getParam());
		JSONObject jsonObj = jsonObject.getJSONObject("searchParam");
		Integer type = jsonObj.getInteger("searchType");
		String key = jsonObj.getString("searchKey");
		
		int total = 0;
		list.add(countOrders(input));
		total += (Integer)countOrders(input).get("count");
		
		list.add(getReceiptCount(type,key));
		total += (Integer)getReceiptCount(type,key).get("count");
		
		list.add(getPaymentCount(type,key));
		total += (Integer)getPaymentCount(type,key).get("count");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("count", total);
		map.put("listCategoryCode", "0");
		map.put("listCategoryName", "全部");
		list.add(map);
		return list;
	}
	
	/**
	 * 根据查询类型,输入文本统计订单数量
	 * @author hhx
	 * @param input
	 * @return
	 */
	public Map<String,Object> countOrders(BaseInput4MT input) {
		JSONObject jsonObject = JSONObject.parseObject(input.getParam());
		JSONObject jsonObj = jsonObject.getJSONObject("searchParam");
		String type = jsonObj.getString("searchType");
		String key = jsonObj.getString("searchKey");
		Map<String,Object> map = new HashMap<String,Object>();
		User u = UserUtils.getUser();
		StringBuffer sql = new StringBuffer(
				"SELECT count(ao.id) FROM airticket_order ao,activity_airticket aa "
						+ "WHERE ao.del_flag = 0 AND aa.delflag = '0' AND aa.id = ao.airticket_id AND aa.proCompany = "
						+ u.getCompany().getId() + " AND (aa.operator = "
						+ u.getId() + " or aa.createBy=" + u.getId()
						+ " or ao.create_by=" + u.getId() + ")");
		if(StringUtils.isNotBlank(key)){
			switch (type) {
			case "1":
				sql.append("AND EXISTS (SELECT 1 FROM activity_airticket aa "
						+ "WHERE aa.id = ao.airticket_id "
						+ "AND aa.delflag = '0'	" + "AND aa.group_code like '%"
						+ key + "%')");
				break;
			case "2":
				sql.append("AND ao.order_no like '%" + key + "%'");
				break;
			case "3":
				sql.append("AND EXISTS (SELECT 1 FROM activity_airticket aa "
						+ "WHERE aa.id = ao.airticket_id "
						+ "AND aa.delflag = '0'	"
						+ "AND aa.activity_airticket_name like '%" + key + "%')");
				break;
			case "4":
				sql.append("AND EXISTS (SELECT 1 FROM ordercontacts o,agentinfo a "
						+ "WHERE o.agentid IS NOT NULL " + "AND o.orderid = ao.id	"
						+ "AND a.id = o.agentid AND a.agentName LIKE '%" + key + "%')");
				break;
			case "5":
				sql.append("AND exists (select 1 from sys_user su where ao.create_by = su.id and su.name like '%" + key + "%')");
				break;
			case "6":
				sql.append("AND EXISTS (SELECT 1 FROM airticket_order_pnr aop "
						+ "WHERE aop.airticket_order_id = ao.id "
						+ "AND aop.delflag = '0' " + "AND aop.flight_pnr like '%"
						+ key + "%')");
				break;
			case "7":
				/*sql.append("AND EXISTS (SELECT 1 FROM airticket_order_pnr aop,sys_airline_info sai "
						+ "WHERE aop.airticket_order_id = ao.id and aop.airline = sai.airline_code and sai.del_flag = '0' "
						+ "AND aop.delflag = '0' AND sai.airline_name like '%"
						+ key + "%')");*/
				sql.append("AND EXISTS (SELECT 1 FROM airticket_order_pnr aop "
						+ "WHERE aop.delflag = '0' AND aop.airline like '%"
						+ key + "%')");
				break;
			case "8":
				sql.append("AND EXISTS (SELECT 1 FROM airticket_order_pnr_airline aopa "
						+ "WHERE aopa.airticket_order_id = ao.id "
						+ "AND aopa.delflag = '0' "
						+ "AND aopa.airline_name LIKE '%" + key + "%')");
				break;
			default:
				break;
			}
		}
		String ret = mtourCommonDao.createSqlQuery(sql.toString()).uniqueResult().toString();
		if(StringUtils.isNotBlank(ret)){
			map.put("count", Integer.parseInt(ret));
		}
		map.put("listCategoryCode", "1");
		map.put("listCategoryName", "订单");
		return map;
	}
	
	@Override
	public Map<String, Object> getPaymentCount(Integer searchType,
			String searchKey) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		Map<String,Object> map = new HashMap<String,Object>();
//		StringBuffer sql = new StringBuffer("SELECT count(*) count, pay.del_flag, pay.id, pay.orderId, pay.airticket_id, pay.group_code, pay.order_no, pay.agentinfo_id, pay.create_by from ")
//			.append("(")
//			.append("SELECT r.id, r.del_flag, orders.id orderId, orders.airticket_id, orders.group_code,orders.order_no, orders.agentinfo_id,orders.create_by ")
//			.append("from refund r, cost_record cost, airticket_order orders, activity_airticket airticket where r.record_id = cost.id and cost.orderId = orders.id and airticket.id = orders.airticket_id and r.moneyType = 1 and r.del_flag = 0 ")
//			.append(" and airticket.proCompany =  ").append(companyId + " ")
//			.append(" UNION ")
//			.append("SELECT r.id, r.del_flag, orders.id orderId, orders.airticket_id, orders.group_code,orders.order_no, orders.agentinfo_id,orders.create_by ")
//			.append("from refund r, airticket_order_moneyAmount amount, airticket_order orders, activity_airticket airticket where r.record_id = amount.id and amount.airticket_order_id = orders.id and airticket.id = orders.airticket_id and r.moneyType in (2,3,4) and r.del_flag = 0 and airticket.proCompany = ").append(companyId + " ")
//			.append(") pay where pay.del_flag = 0 ");
//		switch (searchType) {
//		case 1:
//			sql.append("and pay.group_code like '%").append(searchKey).append("%'");
//			break;
//		case 2:
//			sql.append("and pay.order_no like '%").append(searchKey).append("%'");
//			break;
//		case 3:
//			sql.append("AND EXISTS (select 1 from activity_airticket a where a.id = pay.airticket_id ")
//				.append("and a.activity_airticket_name like '%").append(searchKey).append("%')");
//			break;
//		case 4:
//			sql.append("AND EXISTS (select 1 from agentinfo agent where agent.id = pay.agentinfo_id ")
//				.append("and agent.agentName like '%").append(searchKey).append("%')");
//			break;
//		case 5:
//			sql.append("AND EXISTS (select 1 from sys_user su where su.id = pay.create_by ")
//				.append("and su.name like '%").append(searchKey).append("%')");
//			break;
//		case 6:
//			sql.append("AND EXISTS (select 1 from airticket_order_pnr pnr where pay.orderId = pnr.airticket_order_id ")
//				.append("and pnr.flight_pnr like '%").append(searchKey).append("%')");
//			break;
//		case 7:
//			sql.append("AND EXISTS (select 1 from airticket_order_pnr pnr, sys_airline_info airline ")
//				.append("where pay.orderId = pnr.airticket_order_id and pnr.airline = airline.airline_code ")
//				.append("and airline.airline_name like '%").append(searchKey).append("%')");
//			break;
//		case 8:
//			sql.append("AND EXISTS (select 1 from airticket_order_pnr_airline pnr where pnr.airticket_order_id = pay.orderId ")
//				.append("and pnr.airline_name like '%").append(searchKey).append("%')");
//			break;
//		default:
//			break;
//		}
//		
//		List<Map<String, Object>> list = mtourCommonDao.findBySql(sql.toString(), Map.class);
//		if(list != null && list.size() > 0) {
//			Map<String, Object> mapObj = list.get(0);
//			String count = mapObj.get("count").toString();
//			if(StringUtils.isNotBlank(count)){
//				map.put("count", Integer.parseInt(count));
//			}
//		}
		String wheresql = getPayListWhereSql(searchType, searchKey);
		StringBuffer str = new StringBuffer(
				"SELECT orderNum, paymentUuid, approvalDate, orderUuid, productNo, PNR, groupNo, productName, departureDate, fundsName, " +
				"tourOperatorOrChannelId, tourOperatorOrChannelName, supplyOrAgentType, applicant, paymentStatus, currencyUuid, amount, " +
				"updateDate, fundsType, create_by, operator ");
		StringBuffer str1 = new StringBuffer(" from ( SELECT ao.order_no orderNum, costrecord.id paymentUuid, costrecord.createDate approvalDate,")
				.append(" pro.id productNo, ao.id orderUuid, costrecord.bigCode PNR, pro.group_code groupNo, pro.activity_airticket_name productName,")
				.append(" pro.groupOpenDate departureDate, costrecord. NAME fundsName, costrecord.supplyId tourOperatorOrChannelId, ")
				.append("costrecord.supplyName tourOperatorOrChannelName, costrecord.supplyType supplyOrAgentType, pro.createBy applicant, ")
				.append("costrecord.payStatus paymentStatus, costrecord.currencyId currencyUuid, costrecord.price * costrecord.quantity amount, ")
				.append("costrecord.updateDate, 4 fundsType, ao.create_by, ( SELECT su. NAME FROM sys_user su WHERE su.id = ao.create_by ) AS operator ")
				.append("FROM cost_record costrecord,");
				if("6".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
					str1.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr pnr where a.id=pnr.airticket_order_id and pnr.code_type=0 and pnr.flight_pnr like '%").append(searchKey).append("%')) ao ");
				}else if("7".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
					str1.append("(select * from airticket_order o  where o.id in(select a.id from airticket_order a ,airticket_order_pnr pnr ,sys_airline_info s where a.id=pnr.airticket_order_id and pnr.airline = s.airline_code and  s.airline_code like '%").append(searchKey).append("%')) ao ");
				}else if("8".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
					str1.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr_airline aop where a.id=aop.airticket_order_id and aop.airline_name like '%").append(searchKey).append("%')) ao ");
				}else{
					str1.append(" airticket_order ao ");
				}
				str1.append(",activity_airticket pro WHERE costrecord.orderId = ao.id AND ")
					.append("ao.airticket_id = pro.id AND costrecord.delFlag = 0 AND costrecord.budgetType = 1 AND costrecord.payStatus in (1,3) AND ")
					.append("pro.proCompany = ")
					.append(companyId)
				    .append(" UNION ")
					.append("SELECT ao.order_no orderNum,")
					.append(" money.id paymentUuid, money.createDate approvalDate, ao.airticket_id productNo, ao.id orderUuid, 1 PNR, airticket.group_code groupNo, ")
					.append("airticket.activity_airticket_name productName, airticket.groupOpenDate departureDate, money.funds_name fundsName, ")
					.append("ao.agentinfo_id tourOperatorOrChannelId,")
					.append(" ( SELECT agent.agentName FROM agentinfo agent WHERE agent.id = ao.agentinfo_id ) tourOperatorOrChannelName, 2 supplyOrAgentType,")
					.append(" money.createBy applicant, money.paystatus paymentStatus, money.currency_id currencyUuid, money.amount, money.updateDate, ")
					.append("money.moneyType fundsType, ao.create_by, ( SELECT su. NAME FROM sys_user su WHERE su.id = ao.create_by ) AS operator FROM ")
					.append("airticket_order_moneyAmount money,");
				if("6".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
					str1.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr pnr where a.id=pnr.airticket_order_id and pnr.code_type=0 and pnr.flight_pnr like '%").append(searchKey).append("%')) ao ");
				}else if("7".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
					str1.append("(select * from airticket_order o  where o.id in(select a.id from airticket_order a ,airticket_order_pnr pnr ,sys_airline_info s where a.id=pnr.airticket_order_id and pnr.airline = s.airline_code and  s.airline_code like '%").append(searchKey).append("%')) ao ");
				}else if("8".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
					str1.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr_airline aop where a.id=aop.airticket_order_id and aop.airline_name like '%").append(searchKey).append("%')) ao ");
				}else{
					str1.append(" airticket_order ao ");
				}
				str1.append(",activity_airticket airticket WHERE money.airticket_order_id = ao.id ")
				.append("AND ao.airticket_id = airticket.id AND money.airticket_order_id IS NOT NULL AND money.status<>0 AND airticket.proCompany =")
				.append(companyId);

		StringBuffer str2 = new StringBuffer(") paylist where 1=?");
//		Query query = airticketOrderMoneyAmountDao.createSqlQuery(str.append(str1).append(str2).append(wheresql).toString(), "1").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		StringBuffer count = new StringBuffer("select count(*) ");
		
		// 查询总条数
		Long totalNum = airticketOrderMoneyAmountDao.getCountBySQL(count
				.append(str1).append(") paylist where 1=1 ").append(wheresql).toString());
		if(totalNum != null) {
			map.put("count", Integer.parseInt(totalNum.toString()));
		}
		map.put("listCategoryCode", "2");
		map.put("listCategoryName", "付款");
		return map;
	}
	
	private String getPayListWhereSql(Integer searchType, String searchKey){
		StringBuffer sb = new StringBuffer();
//		JSONObject object  =JSON.parseObject(input.getParam());
//		Map<String,String> map = (Map)object.get("searchParam");
//		String searchType = map.get("searchType");
//		String searchKey = map.get("searchKey");
		if(StringUtils.isNotBlank(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
			if(1==searchType){//团号
				sb.append(" AND paylist.groupNo  like '%").append(searchKey).append("%'");
			}else if(2==searchType){//订单编号
				sb.append(" AND paylist.orderNum like '%").append(searchKey).append("%'");
			}else if(3==searchType){//产品名称
				sb.append(" AND paylist.productName like '%").append(searchKey).append("%'");
			}else if(4==searchType){//渠道 1表示渠道，2表示批发商
				sb.append(" AND paylist.supplyOrAgentType = 2 AND paylist.tourOperatorOrChannelName like '%").append(searchKey).append("%'");
			}else if(5==searchType){//下单人
			//	sb.append(" AND list.salerName like '%");
			}else if(6==searchType){//pnr
				sb.append("");
			}else if(7==searchType){//航空公司
				sb.append("");
			}else if(8==searchType){//航段名称
				sb.append("");
			}
			
			
		}
		
//		Map<String,Object> filterParam = (Map<String, Object>) object.get("filterParam");
//		Object paymentStatusCode = filterParam.get("paymentStatusCode");
//		if(paymentStatusCode != null && StringUtils.isNotBlank(paymentStatusCode.toString())){
//			sb.append(" AND paylist.paymentStatus = ").append(paymentStatusCode);
//		}
//		
//		//出团日期
//		if(filterParam.get("departureDate")!= null && StringUtils.isNotBlank(filterParam.get("departureDate").toString())){
//			String departureDate = filterParam.get("departureDate").toString();
//			int index = departureDate.indexOf("~");
//			String[] str = departureDate.split("~");
//			if(index<0){
//				
//			}else if(index==0){ //截止日期  
//				if(StringUtils.isNotBlank(str[1])){
//					sb.append(" AND paylist.departureDate <='").append(str[1]).append(" 23:59:59'");
//				}
//			}else if(index>0){
//				if(str.length>1){
//                    if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND paylist.departureDate >='").append(str[0]).append(" 00:00:00'");
//					}
//					if(StringUtils.isNotBlank(str[1])){
//						sb.append(" AND paylist.departureDate <='").append(str[1]).append(" 23:59:59'");
//					}
//				}else{
//					if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND paylist.departureDate >='").append(str[0]).append(" 00:00:00'");
//					}
//				}
//			}			
//		}
//		//批报日期
//		if(filterParam.get("approvalDate")!= null && StringUtils.isNotBlank(filterParam.get("approvalDate").toString())){
//			String approvalDate = filterParam.get("approvalDate").toString();
//			int index = approvalDate.indexOf("~");
//			String[] str = approvalDate.split("~");
//			if(index<0){
//				
//			}else if(index==0){ //截止日期  
//				if(StringUtils.isNotBlank(str[1])){
//					sb.append(" AND paylist.approvalDate <='").append(str[1]).append(" 23:59:59'");
//				}
//			}else if(index>0){
//				if(str.length>1){
//                    if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND paylist.approvalDate >='").append(str[0]).append(" 00:00:00'");
//					}
//					if(StringUtils.isNotBlank(str[1])){
//						sb.append(" AND paylist.approvalDate <='").append(str[1]).append(" 23:59:59'");
//					}
//				}else{
//					if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND paylist.approvalDate >='").append(str[0]).append(" 00:00:00'");
//					}
//				}
//			}			
//		}
//		//申请人applicantId
//		if(filterParam.get("applicantId") != null && StringUtils.isNotBlank(filterParam.get("applicantId").toString()))
//		{
//			sb.append(" AND paylist.applicant = ").append(filterParam.get("applicantId"));
//		}
//		
//		JSONArray array = (JSONArray) filterParam.get("tourOperatorOrChannel");
//	    if(array != null && array.size()>0){
//	    	StringBuffer id = new StringBuffer();
//	    	String type = "";
//	    	int num = array.size();
//	    	for(int i =0;i<num;i++){
//	    		JSONObject o =  array.getJSONObject(i);
//	    		type=(String) o.get("tourOperatorChannelCategoryCode");
//	    		if(i==num-1){
//	    		  id.append(o.get("tourOperatorOrChannelUuid"));
//	    		}else{
//	    		  id.append(o.get("tourOperatorOrChannelUuid")).append(",");
//	    		}
//	    		
//	    	}
//	    	if(StringUtils.isNotBlank(type) && StringUtils.isNotBlank(id.toString())){
//	    		if("2".equals(type)){
//	    			sb.append(" AND paylist.supplyOrAgentType=").append(type).append(" AND paylist.tourOperatorOrChannelId in (").append(id).append(") ");
//	    		}
//	    	}
//	    }
//		
//		//款项类型
//		if(filterParam.get("fundsType") != null && StringUtils.isNotBlank(filterParam.get("fundsType").toString()))
//		{
//			sb.append(" AND paylist.fundsType = ").append(filterParam.get("fundsType"));
//		}
		return sb.toString();
	}
	
	@Override
	public Map<String, Object> getReceiptCount(Integer searchType,
			String searchKey) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		Map<String,Object> map = new HashMap<String,Object>();
//		StringBuffer sql = new StringBuffer("SELECT count(*) FROM orderpay pay,airticket_order orders,activity_airticket airticket WHERE pay.delFlag = 0 ")
//			.append("and orders.del_flag = 0 and airticket.delflag = 0 and airticket.id = orders.airticket_id and orders.id = pay.orderId and airticket.proCompany =  ")
//			.append(companyId + " ");
//		switch (searchType) {
//		case 1:
//			sql.append("and EXISTS (select 1 from airticket_order orders where pay.orderId = orders.id ")
//				.append("and orders.group_code like '%").append(searchKey).append("%')");
//			break;
//		case 2:
//			sql.append("and EXISTS (select 1 from airticket_order orders where pay.orderId = orders.id ")
//				.append("and orders.order_no like '%").append(searchKey).append("%')");
//			break;
//		case 3:
//			sql.append("and EXISTS (select 1 from airticket_order orders, activity_airticket a ")
//				.append("where pay.orderId = orders.id and orders.airticket_id = a.id ")
//				.append("and a.activity_airticket_name like '%").append(searchKey).append("%')");
//			break;
//		case 4:
//			sql.append("and EXISTS (select 1 from airticket_order orders, agentinfo agent ")
//				.append("where pay.orderId = orders.id and orders.agentinfo_id = agent.id ")
//				.append("and agent.agentName like '%").append(searchKey).append("%')");
//			break;
//		case 5:
//			sql.append("and EXISTS (select 1 from airticket_order orders, sys_user su ")
//				.append("where pay.orderId = orders.id and orders.create_by = su.id ")
//				.append("and su.name like '%").append(searchKey).append("%')");
//			break;
//		case 6:
//			sql.append("and EXISTS (select 1 from airticket_order orders, airticket_order_pnr pnr ")
//				.append("where pay.orderId = orders.id and orders.id = pnr.airticket_order_id ")
//				.append("and pnr.flight_pnr like '%").append(searchKey).append("%')");
//			break;
//		case 7:
//			sql.append("and EXISTS (select 1 from airticket_order orders, airticket_order_pnr pnr, sys_airline_info airline ")
//				.append("where pay.orderId = orders.id and orders.id = pnr.airticket_order_id and pnr.airline = airline.airline_code ")
//				.append("and airline.airline_name like '%").append(searchKey).append("%')");
//			break;
//		case 8:
//			sql.append("and EXISTS (select 1 from airticket_order orders, airticket_order_pnr_airline pnr ")
//				.append("where pay.orderId = orders.id and pnr.airticket_order_id = orders.id ")
//				.append("and pnr.airline_name like '%").append(searchKey).append("%')");
//			break;
//		default:
//			break;
//		}
		String wheresql = getShowOrderListWhereSql(searchType, searchKey);
		
		StringBuffer start = new StringBuffer( "SELECT costrecordId, orderType, orderUuid, receiveUuid, arrivalBankDate, receiveDate, fundsName, orderNum, groupNo, ");
        start.append("productName, departureDate, receiveType, paymentCompany, receiver,receiverId, salerId, salerName, agentorsupplytype, agentorsupplyId, ")
			.append("agentorsypplyname, amount, isAsAccount, currencyId, updateDate as modifiedDateTime, payedMoney, totalMoney, accountedmoney,receivedAmount,receiveFundsTypeCode,receiveFundsTypeName");
StringBuffer str = new StringBuffer(" FROM ( SELECT cost.id costrecordId, p.product_type_id orderType, ao.id AS orderUuid, pay.id AS receiveUuid, pay.accountDate AS arrivalBankDate, pay.createDate AS receiveDate,");
   str.append("cost. NAME AS fundsName, ao.order_no AS orderNum, p.group_code AS groupNo, p.activity_airticket_name AS productName, p.startingDate AS departureDate, ")
	.append("5 AS receiveType, pay.payerName AS paymentCompany, NULL salerId, NULL salerName, ( SELECT u. NAME FROM sys_user u WHERE u.id = pay.createBy ) AS receiver,")
	.append(" pay.createBy AS receiverId, cost.supplyType agentorsupplytype, cost.supplyId agentorsupplyId, cost.supplyName agentorsypplyname, ma.amount, pay.isAsAccount,")
	.append(" ma.currencyId, pay.updateDate, NULL AS payedMoney, ao.total_money AS totalMoney, ao.accounted_money AS accountedmoney, ma.amount AS receivedAmount,'2' receiveFundsTypeCode,'其他收入收款' receiveFundsTypeName FROM cost_record cost,")
	.append(" pay_group pay, money_amount ma, activity_airticket p,");  
	if("6".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
		str.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr pnr where a.id=pnr.airticket_order_id and pnr.code_type=0 and pnr.flight_pnr like '%").append(searchKey).append("%')) ao ");
	}else if("7".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
		str.append("(select * from airticket_order o  where o.id in(select a.id from airticket_order a ,airticket_order_pnr pnr ,sys_airline_info s where a.id=pnr.airticket_order_id and pnr.airline = s.airline_code and  s.airline_code like '%").append(searchKey).append("%')) ao ");
	}else if("8".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
		str.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr_airline aop where a.id=aop.airticket_order_id and aop.airline_name like '%").append(searchKey).append("%')) ao ");
	}else{
		str.append(" airticket_order ao ");
	}
	str.append(" WHERE pay.cost_record_id = cost.id AND pay.orderType = cost.orderType ")
	.append("AND pay.payPrice = ma.serialNum AND p.id = cost.activityId AND p.id = ao.airticket_id AND cost.budgetType = 2 AND cost.payStatus<>2 AND p.proCompany=").append(companyId)
	.append(" UNION ")
	.append("SELECT null costrecordId,tmp.orderType, ")
	.append("tmp.id orderUuid, tb.id receiveUuid, tb.accountDate AS arrivalBankDate, tb.createDate AS receiveDate, NULL AS fundsName, tmp.orderNum, ")
   .append("tmp.orderGroupCode groupNo, tmp.productname AS productName, tmp.startingDate AS departureDate, tb.payPriceType AS receiveType, ")
	.append("tb.payerName AS paymentCompany, tmp.create_by AS salerId, ( SELECT u. NAME FROM sys_user u WHERE u.id = tmp.create_by ) AS salerName, ")
	.append("( SELECT u. NAME FROM sys_user u WHERE u.id = tb.createBy ) AS receiver, tb.createBy AS receiverId, 2 agentorsupplytype, ")
	.append("tmp.agentinfo_id agentorsupplyId, tmp.agentName agentorsypplyname, NULL AS amount, tb.isAsAccount, tb.currencyId AS currencyId, tb.updateDate, ")
	.append("tb.moneySerialNum AS payedMoney, tmp.total_money AS totalMoney, tmp.accounted_money AS accountedmoney, tb.receivedAmount,'1' receiveFundsTypeCode,'订单收款' receiveFundsTypeName FROM ")
	.append("( SELECT aa.product_type_id orderType, aa.startingDate, aa.activity_airticket_name productname, ao.accounted_money, ao.create_by, ")
	.append("ao.agentinfo_id, ao.id id, ao.order_no orderNum, aa.group_code orderGroupCode, ao.total_money, ao.nagentName agentName, aa.id airticketId FROM ");
	if("6".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
		str.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr pnr where a.id=pnr.airticket_order_id and pnr.code_type=0 and pnr.flight_pnr like '%").append(searchKey).append("%')) ao ");
	}else if("7".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
		str.append("(select * from airticket_order o  where o.id in(select a.id from airticket_order a ,airticket_order_pnr pnr ,sys_airline_info s where a.id=pnr.airticket_order_id and pnr.airline = s.airline_code and  s.airline_code like '%").append(searchKey).append("%')) ao ");
	}else if("8".equals(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
		str.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr_airline aop where a.id=aop.airticket_order_id and aop.airline_name like '%").append(searchKey).append("%')) ao ");
	}else{
		str.append(" airticket_order ao ");
	}
	str.append("LEFT JOIN agentinfo ai ON ao.agentinfo_id = ai.id LEFT JOIN sys_user su ON ao.create_by = su.id ")
	.append("LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id LEFT JOIN sys_user aasu ON aa.createBy = aasu.id  where aa.proCompany=")
	.append(companyId)
	.append(") tmp, ( SELECT op.accountDate, ")
	.append("op.createDate, op.updateDate, op.payerName, op.id, op.orderNum, op.ordertype, op.printTime, op.printFlag, op.isAsAccount, op.payPriceType, ")
	.append("op.createBy, moneySerialNum, ma.amount receivedAmount, ma.currencyId FROM orderpay op, money_amount ma WHERE op.moneySerialNum = ma.serialNum AND ")
	.append("op.orderNum IS NOT NULL ) tb WHERE tb.orderNum = tmp.orderNum ");

		StringBuffer end = new StringBuffer(") list where 1=?");
		end.append(wheresql).append(" order by receiveDate desc");
//		Query query = airticketOrderMoneyAmountDao.createSqlQuery(
//				start.append(str).append(end).toString(), "1")
//				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		StringBuffer count = new StringBuffer("select count(1) ");
		//获取总条数
		Long totalNum = airticketOrderMoneyAmountDao.getCountBySQL(count.append(str).append(") list where 1=1").append(wheresql).toString());
//		String count = mtourCommonDao.createSqlQuery(sql.toString()).uniqueResult().toString();
//		if(StringUtils.isNotBlank(count)){
//			map.put("count", Integer.parseInt(count));
//		}
		if(totalNum != null){
			map.put("count", Integer.parseInt(totalNum.toString()));
		}
		map.put("listCategoryCode", "3");
		map.put("listCategoryName", "收款");
		return map;
	}
	
	public String getShowOrderListWhereSql(Integer searchType, String searchKey){
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotBlank(searchType.toString()) && StringUtils.isNotBlank(searchKey)){
			if(1 == searchType){//团号
				sb.append(" AND list.groupNo like '%").append(searchKey).append("%'");
			}else if(2 == searchType){//订单编号
				sb.append(" AND list.orderNum like '%").append(searchKey).append("%'");
			}else if(3 == searchType){//产品名称
				sb.append(" AND list.productName like '%").append(searchKey).append("%'");
			}else if(4 == searchType){//渠道 1表示渠道，2表示批发商
				sb.append(" AND agentorsupplytype = 1 AND list.agentorsypplyname like '%").append(searchKey).append("%'");
			}else if(5 == searchType){//下单人
				sb.append(" AND list.salerName like '%").append(searchKey).append("%'");
			}else if(6 == searchType){//pnr
			}else if(7==searchType){//航空公司
				sb.append("");
			}else if(8==searchType){//航段名称
				sb.append("");
			}
			
			
		}
//		Map m  =  (Map) object.get("filterParam");
		//收款状态
//		if(m.get("receiveStatusCode")!= null && StringUtils.isNotBlank(m.get("receiveStatusCode").toString())){
//			if("99".equals(m.get("receiveStatusCode").toString())) {
//				sb.append(" AND (list.isAsAccount = ").append(m.get("receiveStatusCode").toString()).append(" or list.isAsAccount is null)");
//			} else {
//				sb.append(" AND list.isAsAccount = ").append(m.get("receiveStatusCode").toString());
//			}
//		}
//		//付款单位
//		if(m.get("payer") != null && StringUtils.isNotBlank(m.get("payer").toString())){
//			sb.append(" AND list.paymentCompany = ").append(m.get("payer").toString());
//		}
//		//收款人
//		if(StringUtils.isNotBlank(map.get("receiver"))){
//			sb.append(" AND list.receiverId = ").append(m.get("receiver").toString());
//		}
//		//出团日期
//		if(m.get("departureDate")!= null && StringUtils.isNotBlank(m.get("departureDate").toString())){
//			String departureDate = m.get("departureDate").toString();
//			int index = departureDate.indexOf("~");
//			String[] str = departureDate.split("~");
//			if(index<0){
//				
//			}else if(index==0){ //截止日期
//				if(StringUtils.isNotBlank(str[1])){
//					sb.append(" AND list.departureDate <='").append(str[1]).append(" 23:59:59'");
//				}
//			}else if(index>0){
//				if(str.length>1){
//                    if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND list.departureDate >='").append(str[0]).append(" 00:00:00'");
//					}
//					if(StringUtils.isNotBlank(str[1])){
//						sb.append(" AND list.departureDate <='").append(str[1]).append(" 23:59:59'");
//					}
//				}else{
//					if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND list.departureDate >='").append(str[0]).append(" 00:00:00'");
//					}
//				}
//			}			
//		}
//		
//		//收款日期
//		if(m.get("receiveDate")!= null && StringUtils.isNotBlank(m.get("receiveDate").toString())){
//			String departureDate = m.get("receiveDate").toString();
//			int index = departureDate.indexOf("~");
//			String[] str = departureDate.split("~");
//			if(index<0){
//				
//			}else if(index==0){ //截止日期
//				if(StringUtils.isNotBlank(str[1])){
//					sb.append(" AND list.receiveDate <='").append(str[1]).append(" 23:59:59'");
//				}
//			}else if(index>0){
//				if(str.length>1){
//                    if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND list.receiveDate >='").append(str[0]).append(" 00:00:00'");
//					}
//					if(StringUtils.isNotBlank(str[1])){
//						sb.append(" AND list.receiveDate <='").append(str[1]).append(" 23:59:59'");
//					}
//				}else{
//					if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND list.receiveDate >='").append(str[0]).append(" 00:00:00'");
//					}
//				}
//			}			
//		}
//		
//		//到账日期
//		if(m.get("arrivalBankDate")!= null && StringUtils.isNotBlank(m.get("arrivalBankDate").toString())){
//			String departureDate = m.get("arrivalBankDate").toString();
//			int index = departureDate.indexOf("~");
//			String[] str = departureDate.split("~");
//			if(index<0){
//				
//			}else if(index==0){ //截止日期
//				if(StringUtils.isNotBlank(str[1])){
//					sb.append(" AND list.arrivalBankDate <='").append(str[1]).append(" 23:59:59'");
//				}
//			}else if(index>0){
//				if(str.length>1){
//                    if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND list.arrivalBankDate >='").append(str[0]).append(" 00:00:00'");
//					}
//					if(StringUtils.isNotBlank(str[1])){
//						sb.append(" AND list.arrivalBankDate <='").append(str[1]).append(" 23:59:59'");
//					}
//				}else{
//					if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND list.arrivalBankDate >='").append(str[0]).append(" 00:00:00'");
//					}
//				}
//			}			
//		}
//		
//		//已收金额
//		if(m.get("receivedAmount")!= null && StringUtils.isNotBlank(m.get("receivedAmount").toString())){
//			String departureDate = m.get("receivedAmount").toString();
//			int index = departureDate.indexOf("~");
//			String[] str = departureDate.split("~");
//			if(index<0){
//				
//			}else if(index==0){ //截止日期
//				if(StringUtils.isNotBlank(str[1])){
//					sb.append(" AND list.receivedAmount <='").append(str[1]);
//				}
//			}else if(index>0){
//				if(str.length>1){
//                    if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND list.receivedAmount >='").append(str[0]);
//					}
//					if(StringUtils.isNotBlank(str[1])){
//						sb.append(" AND list.receivedAmount <='").append(str[1]);
//					}
//				}else{
//					if(StringUtils.isNotBlank(str[0])){
//						sb.append(" AND list.receivedAmount >='").append(str[0]);
//					}
//				}
//			}			
//		}
		return sb.toString();
	}
	
	/**
	 * 获取当前用户的json数据
		 * @Title: getCurrentUserInfo
	     * @return Map<String,Object>
	     * @author majiancheng       
	     * @date 2015-10-26 下午2:09:43
	 */
	public UserInfoJsonBean getCurrentUserInfo() {
		UserInfoJsonBean userInfoJsonBean = new UserInfoJsonBean();
		
		//封装当前用户的基本信息
		User user = UserUtils.getUser();
		userInfoJsonBean.setUserId(user.getId().toString());
		userInfoJsonBean.setUserName(user.getName());
		userInfoJsonBean.setCurrentDate(new Date());
		userInfoJsonBean.setLastLoginDateTime(user.getLoginDate());
		if(UserUtils.isMtourUser()){
			userInfoJsonBean.setCompanyRoleCode("0");//美图公司用户
		}else{
			userInfoJsonBean.setCompanyRoleCode("1");//华尔远航公司用户
		}
		//封装当前批发商的银行信息
		userInfoJsonBean.setBanks(getBankInfo(user.getCompany().getId(), "0"));
		
		return userInfoJsonBean;
	}
	
	public List<SysCompanyDictViewJsonBean> getDictListByTypeAndCompanyId(Long companyId,String dictType){
		DetachedCriteria dc = sysCompanyDictViewDao.createDetachedCriteria();
		
		if (StringUtils.isNotEmpty(dictType)){
			dc.add(Restrictions.eq("type", dictType));
		}
		if (companyId!=null){
			if(companyId.equals("-1")){
				dc.add(Restrictions.eq("companyId", companyId));
			}else{
				dc.add(Restrictions.in("companyId", new Long[]{-1l,companyId}));
			}
			
		}
		dc.add(Restrictions.eq("delFlag",Context.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("sort"));
		List<SysCompanyDictView> list= sysCompanyDictViewDao.find(dc);
		List<SysCompanyDictViewJsonBean> jsonBeanList = SysCompanyDictViewTransfer.SysCompanyDictBeans2DictJsonBeans(list);
		
		return jsonBeanList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
    public Map<String, Object> orderStatisticsInfo(Long currentUserCompanyId) {
		StringBuffer sql = new StringBuffer(" SELECT COUNT(*) AS totalOrderCount,SUM(ma.amount*ma.exchangerate)AS totalReceiveAmount,");
		sql.append(" SUM(ma.amount*ma.exchangerate)-SUM(ma1.amount*ma1.exchangerate)AS totalUnreceiveAmount FROM ")
		   .append(" (SELECT ao.total_money,ao.payed_money FROM airticket_order ao WHERE ao.del_flag = 0  AND ")
		   .append("  ao.airticket_id IN (SELECT aa.id FROM activity_airticket aa WHERE aa.proCompany = "+currentUserCompanyId+")) temp ")
		   .append("  LEFT JOIN money_amount ma ON ma.serialNum=temp.total_money LEFT JOIN money_amount ma1 ON ma1.serialNum=temp.payed_money ");
		Map<String, Object> result = (Map<String, Object>)mtourCommonDao.getSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
		return result;
	}
	/**
	 * 获得欧洲
	 * @param input
	 * @return
	 * @throws Exception
	 * @author chao.zhang
	 */
	public List<Map<String,String>> getEU(BaseInput4MT input) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("sg.id, ");
		sql.append("sg.uuid AS countryUuid, ");
		sql.append("sg.name_cn AS countryName ");
		sql.append("FROM ");
		sql.append("sys_geography sg ");
		sql.append("WHERE ");
		sql.append("sg.delflag = '0' ");
		sql.append("AND sg.name_cn = '欧洲' ");
		return mtourCommonDao.findBySql(sql.toString(), Map.class);
	}
}
