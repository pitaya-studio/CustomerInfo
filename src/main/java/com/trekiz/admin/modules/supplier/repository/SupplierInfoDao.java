package com.trekiz.admin.modules.supplier.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.supplier.entity.Bank;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.entity.SupplierWebsiteInfo;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.utils.UserUtils;

interface SupplierInfoDaoCustom extends BaseDao<SupplierInfo> {

	/**
	 * 地接社列表
	 */
	Page<Map<String,String>> querySupplierOrderListByCond(HttpServletRequest request, HttpServletResponse response, Map<String,Object> condMap, Long companyId);

	/**
	 * 删除地接社信息
	 */
	boolean deleteSupplierInfo(Long id);

	/**
	 * 插入地接社信息
	 */
	boolean insertSupplierMap(Long type, Long id);

	/**
	 * 删除地接社信息
	 */
	boolean deleteSupplierMap(Long id);

	/**
	 * 删除地接社联系人
	 */
	boolean deleteSupplierContacts(Long id);

	/**
	 * 根据ID和类型查询银行账户
	 */
	List<String> getPlatBankInfo(String id, String type);

	/**
	 * 查询银行账户
	 */
	List<String> getSupplierPlatBankInfo(Long id);

	/**
	 * 删除银行账户
	 */
	boolean deleteSupplierPlatBankInfo(Long id);

	/**
	 * 插入银行账户
	 */
	boolean insertSupplierPlatBankInfo(String defaultFlag, String accountName, String bankName,
			String bankAddr, String bankAccountCode, String remarks, Long beLongPlatId);
}

public interface SupplierInfoDao extends SupplierInfoDaoCustom, CrudRepository<SupplierInfo, Long> {

	/**
	 * 获取省、市下拉框
	 */
	@Query(value="select id,name from Area where parent.id = ?1")
	public List<Object[]> getAreaInfo(Long areaParentId);

	/**
	 * 获取地区下拉框
	 */
	@Query(value="select id,name from Area where delFlag='0' and parentIds like ?1")
	public List<Object[]> getArea(String areaParentId);

	@Query(value="select id,name from Area where type = 2 and delFlag = 0")
	public List<Object[]> getCountryInfo();

	/**
	 * 获取父级编号
	 */
	@Query(value="select parent.id from Area where id = ?1")
	public Long getAreaParentInfo(Long areaId);

	@Query(value="from SupplierContacts where supplierId = ?1 and type = '1'")
	List<SupplierContacts> findContactsBySupplierInfoId(Long id);	
	
	@Query(value="from SupplierInfo where companyId = ?1 and delFlag=0")
	List<SupplierInfo> findSupplierInfoByCompanyId(Long companyId);

	@Query("from SupplierWebsiteInfo where supplierId = ?1")
	public SupplierWebsiteInfo selectSupplierWebsiteInfo(Long supplierId);
	
	@Query(value="from Bank where platType = ?1 and belongPlatId=?2 and delFlag = '0' order by defaultFlag")
	List<Bank> findBank(Integer platType,Integer belongPlatId);
	
	@Query(value="from Bank where platType = ?1 and belongPlatId=?2 and bankName=?3 and delFlag = '0' order by defaultFlag")
	List<Bank> findBank(Integer platType,Integer belongPlatId,String bankName);
	
	@Query(value="from Bank where bankName = ?1 and bankAccountCode = ?2 and belongPlatId = ?3 and delFlag = '0' order by defaultFlag")
	Bank findBankByNameAccount(String bankName, String bankAccount, Integer belongPlatId);

	public List<Dict> getSupplierList4Type();
	public List<SupplierInfo> getSupplierList(String tourOperatorTypeUuid,
			String tourOperatorName,int count) ;
}

class SupplierInfoDaoImpl extends BaseDaoImpl<SupplierInfo> implements SupplierInfoDaoCustom {

	/**
	 * 查询地接社列表
	 */
	@Override
	public Page<Map<String,String>> querySupplierOrderListByCond(HttpServletRequest request, HttpServletResponse response, Map<String,Object> condMap, Long companyId) {
		String baseSql = "SELECT"
							+" SI.id id,"
							+" SI.supplierType supplierType,"
							+" SI.supplierOtherType supplierOtherType,"
							+" SI.supplierName supplierName,"
							+" SI.bussinessUUID bussinessUUID"
						+" FROM"
							+" supplier_info SI"
						+" WHERE"
							+" SI.delFlag = '0' AND SI.companyId = " + companyId;

		// 动态查询条件
		String condSql = "";
		if(condMap.get("supplierName")!=null && condMap.get("supplierName")!=""){
			condSql+=" AND SI.supplierName LIKE '%"+condMap.get("supplierName").toString()+"%'";
		}
		if(condMap.get("minCreateDate")!=null && condMap.get("minCreateDate")!=""){
			condSql+=" AND SI.createDate >= '"+condMap.get("minCreateDate").toString()+" 00:00:00'";
		}
		if(condMap.get("maxCreateDate")!=null && condMap.get("maxCreateDate")!=""){
			condSql+=" AND SI.createDate <= '"+condMap.get("maxCreateDate").toString()+" 23:59:59'";
		}
		if(condMap.get("supplierType")!=null && condMap.get("supplierType")!=""){
			condSql+= " AND FIND_IN_SET('"+condMap.get("supplierType").toString()+"', SI.supplierType)";
		}
		if(condMap.get("country")!=null && condMap.get("country")!=""){
			condSql+=" AND SI.companyAddr = '"+condMap.get("country").toString()+"'";
		}
		if(condMap.get("area")!=null && condMap.get("area")!=""){
			condSql+=" AND (SI.companyAddrProvince = '"+condMap.get("area").toString()+"'"
					+ " OR SI.companyAddrCity = '"+condMap.get("area").toString()+"')";
		}

		// 排序条件
		String orderBySql = " ORDER BY 1=1";
		if(condMap.get("orderCreateDateSort")!=null && condMap.get("orderCreateDateSort")!=""){
			orderBySql+=", SI.createDate "+condMap.get("orderCreateDateSort").toString();
		}
		if(condMap.get("orderUpdateDateSort")!=null && condMap.get("orderUpdateDateSort")!=""){
			orderBySql+=", SI.updateDate "+condMap.get("orderUpdateDateSort").toString();
		}

		// 组织完整的sql
		String sqlString = baseSql+condSql+orderBySql;

		return findPageBySql(new Page<Map<String,String>>(request, response),sqlString,Map.class);
	}

	/**
	 * 根据地接社ID删除地接社信息
	 */
	public boolean deleteSupplierInfo(Long id) {
		String deleteSupplierInfoSql = "UPDATE supplier_info SI SET SI.delFlag = '1' WHERE SI.id = ?";
		int count = updateBySql(deleteSupplierInfoSql, id);
		return count==1?true:false;
	}

	/**
	 * 根据地接社ID插入地接社与地接社类型关联数据
	 */
	public boolean insertSupplierMap(Long supplierType, Long supplierId) {
		String insertSupplierMapSql = "INSERT INTO supplier_map(supplyType, supplierId) VALUES (?, ?)";
		int count = updateBySql(insertSupplierMapSql, supplierType, supplierId);
		return count == 0?false:true;
	}

	/**
	 * 根据地接社ID删除地接社与地接社类型关联数据
	 */
	public boolean deleteSupplierMap(Long supplierId) {
		String deleteSupplierMapSql = "DELETE FROM supplier_map WHERE supplierId = ?";
		int count = updateBySql(deleteSupplierMapSql, supplierId);
		return count == 0?false:true;
	}

	/**
	 * 根据地接社ID删除地接社联系人
	 */
	public boolean deleteSupplierContacts(Long id) {
		String deleteSupplierContactsSql = "DELETE FROM supplier_contacts WHERE supplierId=? AND type='1'";
		int count = updateBySql(deleteSupplierContactsSql, id);
		return count == 0?false:true;
	}

	/**
	 * 根据ID和类型查询银行账户
	 */
	public List<String> getPlatBankInfo(String id, String type) {
		String selectPlatBankInfo = "select id,bankName,bankAccountCode from plat_bank_info where beLongPlatId = ? and platType = ? order by defaultFlag";
		List<String> platBankInfo = findBySql(selectPlatBankInfo, id);
		return platBankInfo;
	}

	/**
	 * 根据地接社ID查询银行账户
	 */
	public List<String> getSupplierPlatBankInfo(Long id) {
		String selectSupplierPlatBankInfoSql = "select id,defaultFlag,accountName,bankName,bankAddr,bankAccountCode,remarks from plat_bank_info where beLongPlatId = ? and platType = 1 order by defaultFlag";
		List<String> platBankInfo = findBySql(selectSupplierPlatBankInfoSql, id);
		return platBankInfo;
	}

	/**
	 * 根据地接社ID删除银行账户
	 */
	public boolean deleteSupplierPlatBankInfo(Long id) {
		String deleteSupplierPlatBankInfo = "DELETE FROM plat_bank_info WHERE beLongPlatId = ? and platType = 1";
		int count = updateBySql(deleteSupplierPlatBankInfo, id);
		return count == 0?false:true;
	}

	/**
	 * 根据地接社ID插入银行账户
	 */
	public boolean insertSupplierPlatBankInfo(String defaultFlag, String accountName, String bankName,
			String bankAddr, String bankAccountCode, String remarks, Long beLongPlatId) {
		String insertSupplierPlatBankInfoSql = "INSERT INTO plat_bank_info (" +
				" defaultFlag," +
				" accountName," +
				" bankName," +
				" bankAddr," +
				" bankAccountCode," +
				" remarks," +
				" platType," +
				" beLongPlatId)" +
				" VALUES (?, ?, ?, ?, ?, ?, 1, ?)";
		int count = updateBySql(insertSupplierPlatBankInfoSql, defaultFlag, accountName, bankName,
				bankAddr, bankAccountCode, remarks, beLongPlatId);
		return count == 0?false:true;
	}
	/**
	 * 返回地接社类型列表
	 * @author gao
	 * 2015年10月14日
	 * @return
	 */
	public List<Dict> getSupplierList4Type(){
		
		String strsql = "from Dict info where info.delFlag=? and info.type= ?";
		return super.find(strsql,DataEntity.DEL_FLAG_NORMAL,Context.BaseInfo.TRAVEL_AGENCY_TYPE);
	}
	/**
	 * 返回地接社列表
	 * @author gao
	 * 2015年10月14日
	 * @param tourOperatorTypeUuid
	 * @param tourOperatorName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SupplierInfo> getSupplierList(String tourOperatorTypeUuid,
			String tourOperatorName,int count) {
		
		Session session = getSession();
		List<SupplierInfo> suppList = new ArrayList<SupplierInfo>();
		
		StringBuffer str = new StringBuffer("select id,supplierName from supplier_info where delFlag="+DataEntity.DEL_FLAG_NORMAL);
		if(StringUtils.isNotBlank(tourOperatorTypeUuid)){
			str.append(" and find_in_set("+tourOperatorTypeUuid+",supplierType) ");
		}
		if(StringUtils.isNotBlank(tourOperatorName)){
			str.append(" and supplierName like ('"+"%"+tourOperatorName+"%"+"') ");
		}
		// 地接社查询接口增加批发商的默认查询条件。当前登录人所在的批发商。
		Long myCompanyID = UserUtils.getUser().getCompany().getId();
		str.append(" and companyId = "+myCompanyID);
		
		if(count>1){
			str.append(" limit 0,"+(count));
		}else if(count==1){
			str.append(" limit 0,1 ");
		}
		
		List<Object[]> res = session.createSQLQuery(str.toString()).list();
		for(Object[] obj : res){
			SupplierInfo info = new SupplierInfo();
			info.setId(Long.valueOf(obj[0].toString()));
			info.setSupplierName(obj[1].toString());
			suppList.add(info);
		}
		
		return suppList;
	}
}