/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.dao.impl;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.geography.dao.SysGeographyDao;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.mtourCommon.dao.MtourCommonDao;
import com.trekiz.admin.modules.mtourCommon.entity.MenuInfoTempBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.CityInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.UserInfoJsonBean;
import com.trekiz.admin.modules.mtourOrder.jsonbean.PNRRecordJsonBean;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.repository.SupplierInfoDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@SuppressWarnings("rawtypes")
@Service
@Transactional(readOnly = true)
public class MtourCommonDaoImpl extends BaseDaoImpl  implements MtourCommonDao{
	@Autowired
	private SupplierInfoDao supplierInfoDao;
	@Autowired
	private SysGeographyDao sysGeographyDao;

	@Override
	public List<SupplierInfo> getSupplierList(String tourOperatorTypeUuid,
			String tourOperatorName,int count) {
		
		return supplierInfoDao.getSupplierList(tourOperatorTypeUuid, tourOperatorName, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfoJsonBean> getUserByRoleType(String roleType, String userName, String count) {
		StringBuffer sql = new StringBuffer();
		/*sql.append(" SELECT  su.id as userId,su.name as userName FROM sys_role sr INNER JOIN sys_user_role sur ON sr.id = sur.roleId");
		sql.append(" INNER JOIN sys_user su ON su.id = sur.userId");
		sql.append(" WHERE su.delFlag=0 AND sr.delFlag=0 AND su.companyId="+UserUtils.getUser().getCompany().getId());
		if(StringUtils.isNotBlank(roleType)){
			sql.append(" AND sr.roleType='"+roleType+"'");
		}*/
		sql.append("SELECT  su.id as userId,su.name as userName FROM  sys_user su WHERE su.delFlag=0 AND su.companyId="+UserUtils.getUser().getCompany().getId());
		if(StringUtils.isNotBlank(userName)){
			sql.append(" AND su.name LIKE '%"+userName+"%'");
		}
		if(!("-1".equals(count))){
			sql.append(" group by su.id LIMIT "+count);
		}
		SQLQuery query = super.getSession().createSQLQuery(sql.toString());
		query.addScalar("userId", StandardBasicTypes.STRING).addScalar("userName", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(UserInfoJsonBean.class));
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getAgentinfoByTypeCode(String channelTypeCode, String channelName, String count) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT a.id AS channelUuid,a.agentName AS channelName,a.agentContact AS contactName,a.agentContactMobile AS contactPhone, ");
		sb.append(" a.agentTelAreaCode AS agentTelAreaCode,a.agentTel AS channelTel,a.agentPostcode AS channelZipCode,");
		sb.append(" a.agentAddressStreet AS channelAddress,a.agentFaxAreaCode AS agentFaxAreaCode,a.agentFax AS channelTax ");
		sb.append(" FROM agentinfo a WHERE a.delFlag = 0 AND a.supplyId = ").append(UserUtils.getUser().getCompany().getId());
		if(StringUtils.isNotBlank(channelName)){
			sb.append(" AND a.agentName LIKE '%").append(channelName).append("%' ");
		}
		if("2".equals(channelTypeCode)){
			sb.append(" AND a.is_uncontract = '1' ");//查询非签约渠道
		}else if("1".equals(channelTypeCode)){
			sb.append(" AND a.is_uncontract IS NULL ");//查询签约渠道
		}
		if(!("-1".equals(count))){
			sb.append(" GROUP BY a.id LIMIT ").append(count);
		}
		List<Map<String,Object>> resultMap = findBySql(sb.toString(), Map.class);
		return resultMap;
	}

	@Override
	public List<CityInfoJsonBean> getCityInfo(String cityKey, Integer count) {

		StringBuffer sql = new StringBuffer("select geo.uuid,geo.name_cn from sys_geography geo ");
		List<CityInfoJsonBean> cityList = new ArrayList<CityInfoJsonBean>();
		List<Object[]> list = null;

		sql.append(" where ");
		sql.append(" geo.level = 3 "); // 限定级别为“城市”
		if(StringUtils.isNotBlank(cityKey)){
			if(StringUtil.judgeChineseChar(cityKey)){
				sql.append(" and geo.name_cn like '"+'%'+cityKey+'%'+"'"); // 输入为汉字
			}else if(StringUtil.judgeEnglishChar(cityKey)){
				sql.append(" and geo.name_pinyin like '"+'%'+cityKey+'%'+"'" ); // 输入为拼音
			}// 输入其他字符，则不予查询
		}
		if(count==1){
			sql.append(" limit 0,1 ");
			list = sysGeographyDao.findBySql(sql.toString());
		}else if(count>1){
			sql.append(" limit 0,? ");
			list = sysGeographyDao.findBySql(sql.toString(),count);
		}else{
			list = sysGeographyDao.findBySql(sql.toString());
		}
		if(list!=null && !list.isEmpty()){
			for(Object[] sys : list){
				CityInfoJsonBean bean = new CityInfoJsonBean();
				if(sys[1]!=null && sys[0]!=null){
					bean.setCityName(sys[1].toString());
					bean.setCityUuid(sys[0].toString());
				}
				cityList.add(bean);
			}
		}
		return cityList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PNRRecordJsonBean> getPNRRecord(String orderId,String invoiceOriginalGroupUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT aor.createDate AS modifyDate,aor.createBy AS modifier,aor.content AS modifyContent FROM airticket_order_operate_record aor ");
		if(StringUtils.isNotBlank(orderId)){
			sql.append(" LEFT JOIN airticket_order_pnrGroup aop ON aor.target_uuid=aop.uuid WHERE aor.delFlag='0' AND aor.target_type=0 AND aop.delFlag='0' AND aop.airticket_order_id=? ");
			if(StringUtils.isNotBlank(invoiceOriginalGroupUuid)){
				sql.append(" AND aor.target_uuid=? ");
			}
		}else{
			sql.append("  WHERE aor.delFlag='0' AND aor.target_type=0 AND aor.target_uuid=? ");//target_type=0 :修改PNR组
		}
			
		SQLQuery query = super.getSession().createSQLQuery(sql.toString());
		if(StringUtils.isNotBlank(orderId)){
			query.setString(0, orderId);
			if(StringUtils.isNotBlank(invoiceOriginalGroupUuid)){
				query.setString(1, invoiceOriginalGroupUuid);
			}
		}else{
			query.setString(0, invoiceOriginalGroupUuid);
		}
		query.addScalar("modifyDate", StandardBasicTypes.STRING).addScalar("modifier", StandardBasicTypes.STRING).addScalar("modifyContent", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(PNRRecordJsonBean.class));
		List<PNRRecordJsonBean> result = query.list();
		
		return (null == result)? new LinkedList<PNRRecordJsonBean>():result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MenuInfoTempBean> getMenuInfoByUserId(long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT m.id ,m.parentId ,m.parentIds ,m.name ,m.href ,m.level ,m.permission ,m.isShow ,m.icon ,m.sort ");
		sql.append("from sys_menu m ,sys_user_role ur ,sys_role_menu rm ,sys_user u ,sys_role r ");
		sql.append("where m.delFlag=0 and  m.id = rm.menuId and rm.roleId = r.id and r.delFlag=0 and ur.roleId = r.id and ur.userId=u.id and u.delFlag=0 and u.id=? ");
		sql.append("order by m.id"); 
		SQLQuery query = super.getSession().createSQLQuery(sql.toString());
		query.setLong(0, userId);
		 
		query.addScalar("id", StandardBasicTypes.LONG)
			.addScalar("parentId", StandardBasicTypes.LONG)
			.addScalar("parentIds", StandardBasicTypes.STRING)
			.addScalar("name", StandardBasicTypes.STRING)
			.addScalar("href", StandardBasicTypes.STRING)
			.addScalar("level", StandardBasicTypes.STRING)
			.addScalar("permission", StandardBasicTypes.STRING)
			.addScalar("isShow", StandardBasicTypes.STRING)
			.addScalar("icon", StandardBasicTypes.STRING)
			.addScalar("sort", StandardBasicTypes.LONG); 
		query.setResultTransformer(Transformers.aliasToBean(MenuInfoTempBean.class));
		List<MenuInfoTempBean> result = query.list();
		return (null == result)? new LinkedList<MenuInfoTempBean>():result;
	}

	@Override
	public List<Map<String, String>> getCurrencyList() {
		StringBuffer str = new StringBuffer();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		str.append(" SELECT ")
		   .append(" 	currency_id AS currencyUuid, ")
		   .append(" 	currency_mark AS currencyCode, ")
		   .append(" 	currency_name AS currencyName, ")
		   .append(" 	convert_lowest AS exchangeRate ")
		   .append(" FROM ")
		   .append(" 	currency ")
		   .append(" WHERE ")
		   .append(" 	del_flag = ").append(Context.DEL_FLAG_NORMAL)
		   .append(" AND create_company_id = ").append(currentCompanyId);
		list = findBySql(str.toString(), Map.class);
		return list;
	}

}