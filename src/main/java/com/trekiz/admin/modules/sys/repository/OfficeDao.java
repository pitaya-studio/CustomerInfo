/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.modules.sys.entity.Office;

/**
 * 批发商DAO接口
 * @author zj
 * @version 2013-11-19
 */
public interface OfficeDao extends OfficeDaoCustom, CrudRepository<Office, Long> {

	@Modifying
	@Query("update Office set delFlag='" + Office.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	/**
	 * 修改sys_office表中export_time，即下载excel时间
	 */
	/*@Modifying
	@Query("update Office set exportTime = ?1" + " where id =?2") 
	public void insertExportEcelTime(Date exportTime,String id);*/
	
	public List<Office> findByParentIdsLike(String parentIds);
	@Query("from Office where delFlag='" + Office.DEL_FLAG_NORMAL + "' and (id = ?1 or parentIds like ?2)")
	public List<Office> findRelatedOffice(Long id, String parentIds);

	@Query("from Office where delFlag='" + Office.DEL_FLAG_NORMAL + "' and isValidateDoma = 1 and domainName like ?1")
	public List<Office> findDomainName(String domainName);
	
	@Query("from Office where delFlag='" + Office.DEL_FLAG_NORMAL + "' ")
	public List<Office> findSyncOffice();
	
	@Modifying
	@Query("update Office set signKey = null, frontVendorId = null where id=?")
	public void unboundOffice(Long officeId);
	
	@Modifying
	@Query(value="update sys_office set export_time = ?1 where id = ?2", nativeQuery=true)
	public void addExportTime(Date date,Long id);
	
	@Modifying
	@Query("update Office set delFlag = '"+Context.DEL_FLAG_DELETE+"' where id = ?1")
	public void delOffice(Long id);
	/**
	 * 根据ID获取批发商信息
	 * @author zhaohaiming	
	 * @date   2015-10-19
	 * @param id 批发商id
	 * @return office
	 * */
	@Query("from Office where delflag='"+Office.DEL_FLAG_NORMAL + "' and id=?1")
	public Office findById(Long id);
	
	/**
	 * 根据uuid集合获取批发商信息
	 * @Description: 
	 * @param @param uuids
	 * @param @return   
	 * @return List<Office>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	@Query("from Office where delflag='"+Office.DEL_FLAG_NORMAL + "' and uuid in (?1)")
	public List<Office> findByUuids(List<String> uuids);
	
	/**
	 * 根据uuid获取批发商信息
	 * @param uuids
	 * @return
	 */
	@Query("from Office where delflag='"+Office.DEL_FLAG_NORMAL + "' and uuid = ?1")
	public Office findByUuid(String uuid);
	
	/**
	 * 获取被启用的批发商
	 * @param shelfRightsStatus
	 * @return
	 */
	public List<Office> findByShelfRightsStatus(Integer shelfRightsStatus);
}

/**
 * DAO自定义接口
 * @author zj
 */
interface OfficeDaoCustom extends BaseDao<Office> {

	/**
	 * 查询银行账户
	 */
	List<String> getOfficePlatBankInfo(Long id);
	
	public List<String> getOfficePlatBankInfo(Long id,String bankName) ;
	
	public List<String> getOfficePlatBankInfoById(Long id);
	/**
	 * 仅查询银行名称及对应id
	 * [0]为银行id，[1]为银行名称
	 * */
	public List<String[]> getOfficePlatBankInfoForSelect(Long id);
	
	/**
	 * 删除银行账户
	 */
	boolean deleteOfficePlatBankInfo(Long id);

	/**
	 * 插入银行账户
	 */
	boolean insertOfficePlatBankInfo(String defaultFlag, String accountName, String bankName,
			String bankAddr, String bankAccountCode, String remarks, Long beLongPlatId, String belongType);
	
	/**
	 * 插入境外银行账户
	 */
	public boolean insertOfficePlatBankInfo(String defaultFlag, String accountName, String bankName, String bankAddr,
			String bankAccountCode, String remarks, long beLongPlatId, String rounting, String swiftNum, String phoneNum, String belongType);
	
	/**
	 * 根据供应商id查出其服务费率
	 */
	public BigDecimal getChargeRateByOfficeId(Long id);

	/**
	 * 数据取存在勾选QUAUQ渠道权限的账号所处的批发商；选项展示顺序为：T1上架产品最多的排在最前面，降序排列；
	 * @return
	 */
	public List<Office> getOffice4T1(String type);
	
	
}

/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class OfficeDaoImpl extends BaseDaoImpl<Office> implements OfficeDaoCustom {

	/**
	 * 根据批发商ID查询银行账户
	 */
	public List<String> getOfficePlatBankInfo(Long id) {
		String deleteOfficeContactsSql = "select id, belongType, defaultFlag,accountName,bankName,bankAddr,bankAccountCode,remarks, rounting, swiftNum, phoneNum from plat_bank_info where beLongPlatId = ? and platType = 0 order by id";
		List<String> platBankInfo = findBySql(deleteOfficeContactsSql, id);
		return platBankInfo;
	}
	/**
	 * 名称不能重复，结果集只包含bankName
	 */
	public List<String> getOfficePlatBankInfoById(Long id) {
		String deleteOfficeContactsSql = "select distinct bankName from plat_bank_info where beLongPlatId = ? and platType = 0 order by defaultFlag";
		List<String> platBankInfo = findBySql(deleteOfficeContactsSql, id);
		return platBankInfo;
	}
	
	/**
	 * 更具批发商ID和银行名称进行过滤
	 */
	public List<String> getOfficePlatBankInfo(Long id,String bankName) {
		String deleteOfficeContactsSql = "select id,defaultFlag,accountName,bankName,bankAddr,bankAccountCode,remarks from plat_bank_info where beLongPlatId = ? and bankName=? and platType = 0 order by defaultFlag";
		List<String> platBankInfo = findBySql(deleteOfficeContactsSql, new Object[]{id,bankName});
		return platBankInfo;
	}
	/**
	 * 根据批发商ID删除银行账户
	 */
	public boolean deleteOfficePlatBankInfo(Long id) {
		String deleteOfficePlatBankInfo = "DELETE FROM plat_bank_info WHERE beLongPlatId = ? and platType = 0";
		int count = updateBySql(deleteOfficePlatBankInfo, id);
		return count == 0?false:true;
	}
	
	/**
	 * 根据批发商ID插入银行账户
	 */
	public boolean insertOfficePlatBankInfo(String defaultFlag, String accountName, String bankName,
			String bankAddr, String bankAccountCode, String remarks, Long beLongPlatId, String belongType) {
		String insertOfficePlatBankInfoSql = "INSERT INTO plat_bank_info (" +
				" defaultFlag," +
				" accountName," +
				" bankName," +
				" bankAddr," +
				" bankAccountCode," +
				" remarks," +
				" platType," +
				" beLongPlatId," +
				" belongType) " +
				" VALUES (?, ?, ?, ?, ?, ?, 0, ?, ?)";
		int count = updateBySql(insertOfficePlatBankInfoSql, defaultFlag, accountName, bankName,
				bankAddr, bankAccountCode, remarks, beLongPlatId, belongType);
		return count == 0?false:true;
	}
	/**
	 * 根据批发商ID插入境外银行账户
	 */
	public boolean insertOfficePlatBankInfo(String defaultFlag, String accountName, String bankName, String bankAddr,
			String bankAccountCode, String remarks, long beLongPlatId, String rounting, String swiftNum, String phoneNum, String belongType){
		String insertOfficePlatBankInfoSql = "INSERT INTO plat_bank_info (" +
				" defaultFlag," +
				" accountName," +
				" bankName," +
				" bankAddr," +
				" bankAccountCode," +
				" remarks," +
				" platType," +
				" beLongPlatId," +
				" rounting, "+
				" swiftNum, " +
				" phoneNum, " +
				" belongType)" +
				" VALUES (?, ?, ?, ?, ?, ?, 0, ?, ?, ?, ?, ?)";
		int count = updateBySql(insertOfficePlatBankInfoSql, defaultFlag, accountName, bankName,
				bankAddr, bankAccountCode, remarks, beLongPlatId, rounting, swiftNum, phoneNum, belongType);
		return count == 0?false:true;
	}
	
	@Override
	public List<String[]> getOfficePlatBankInfoForSelect(Long id) {
		
		String deleteOfficeContactsSql = "select id, bankName from plat_bank_info where beLongPlatId = ? and platType = 0 order by id";
		List<String[]> platBankInfo = findBySql(deleteOfficeContactsSql, id);
		return platBankInfo;
	}
	@Override
	public BigDecimal getChargeRateByOfficeId(Long id) {
		String sql = "SELECT charge_rate from sys_office where id = ? and delFlag = 0";
		BigDecimal chargeRate = (BigDecimal) findBySql(sql , id ).get(0);
		return chargeRate;
	}

	/**
	 * 数据取存在勾选QUAUQ渠道权限的账号所处的批发商；选项展示顺序为：T1上架产品最多的排在最前面，降序排列；
	 * @return
	 */
	@Override
	public List<Office> getOffice4T1(String type) {
		DateTime dt = new DateTime();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ")
				.append("DISTINCT o.* ")
				.append("FROM ")
				.append("sys_office o ")
				.append("LEFT JOIN ( ")
				.append("	SELECT ")
				.append("sup.sys_office_id, ")
				.append(" GROUP_CONCAT(ata.parentIds) parentIds, ")
				.append("	COUNT(*) count ")
				.append("		FROM ")
				.append("activitygroup a ")
				.append("LEFT JOIN travelactivity t ON a.srcActivityId = t.id ")
				.append("LEFT JOIN ( ")
				.append("	SELECT ")
				.append("su.id AS 'createBy', ")
				.append("		so.id AS 'sys_office_id', ")
				.append("		so. NAME AS 'supplierName', ")
				.append("		so.shelfRightsStatus AS 'shelfRightsStatus', ")
				.append("		su.quauqBookOrderPermission AS 'quauqBookOrderPermission', ")
				.append("		so.charge_rate ")
				.append("FROM ")
				.append("sys_user su ")
				.append("LEFT JOIN sys_office so ON su.companyId = so.id ")
				.append("WHERE ")
				.append("so.delFlag = 0 ")
				.append(") sup ON sup.createBy = a.createBy ")
				.append("LEFT JOIN ( ")
				.append(AreaUtil.getLooseActivityTargetAreaSql())
				.append(") ata ON t.id = ata.travelactivity_id ")
				.append("WHERE ")
				.append("t.activity_kind = '2' ")
				.append("AND t.delFlag = 0 ")
				.append("AND t.activityStatus = 2 ")
				.append("AND a.delFlag = 0 ")
				.append("AND sup.shelfRightsStatus = 0 ")
//				.append("AND ( ")
//				.append("		a.quauqAdultPrice IS NOT NULL ")
//				.append("		OR a.quauqChildPrice IS NOT NULL ")
//				.append("		OR a.quauqSpecialPrice IS NOT NULL ")
//				.append(") ")
				.append(" AND a.is_t1 = 1 ")
				.append("AND a.groupOpenDate >= '" + dt.toString("yyyy-MM-dd") + "' ");
		if (Context.FREE_TRAVEL_FOREIGN.equals(type)) {
			sb.append(" and FIND_IN_SET(100000, ata.parentIds) ");
		} else if (Context.FREE_TRAVEL_INLAND.equals(type)) {
			sb.append(" and FIND_IN_SET(200000, ata.parentIds) ");
		}
				sb.append("GROUP BY ")
				.append("sup.sys_office_id ")
				.append(") c ON o.id = c.sys_office_id ")
//				.append("LEFT JOIN sys_user u on o.id = u.companyId ")
				.append("WHERE ");
		if (Context.FREE_TRAVEL_FOREIGN.equals(type)) {
			sb.append(" FIND_IN_SET(100000, c.parentIds) and ");
		} else if (Context.FREE_TRAVEL_INLAND.equals(type)) {
			sb.append(" FIND_IN_SET(200000, c.parentIds) and ");
		}
//				.append("u.quauqBookOrderPermission=1 ")
				sb.append(" o.shelfRightsStatus = 0 ")
				.append("ORDER BY ")
				.append("c.count DESC ");
		return findBySql(sb.toString(), Office.class);
	}
	
}
