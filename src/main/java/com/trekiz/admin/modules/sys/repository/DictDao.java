/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

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
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;

/**
 * 字典DAO接口
 * @author zj
 * @version 2013-11-19
 */
public interface DictDao extends DictDaoCustom, CrudRepository<Dict, Long> {
	
	@Modifying
	@Query("update Dict set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);

	@Query("from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and type=?1 order by sort")
	public List<Dict> findAllListByType(String type);

	@Query("select type from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' group by type")
	public List<String> findTypeList();
	
	@Query("from Dict where type = ?1 and delFlag='0' order by sort")
	public List<Dict> findByType(String type);

	@Query("from Dict where type = ?1 and delFlag='0' and  value not in (?2) order by sort")
	public List<Dict> findByType(String type,List<String> values);
	
	@Query("from Dict where id in ?1 and delFlag='0' ")
	public List<Dict> findByIdlist(List<Long> ids);
	
	@Query("from Dict where id = ?1 and delFlag='0' ")
	public Dict findByIdQuery(Long id);
	
	@Query("from Dict where value = ?1 and type = ?2 and  delFlag='0' ")
	public Dict findByValueAndType(String value, String type);
	
	@Query("from Dict where uuid=?1 and  delFlag='0' ")
	public Dict findLabelByUuid(String uuid);
	
	//add by jiangyang
	@Query("from SysCompanyDictView where delFlag='0' and type=?1 order by sort")
	public List<SysCompanyDictView> findAllDictViewListByType(String type);
	
	@Query("from Dict where delFlag='0' and uuid=? order by sort")
	public Dict findByUuid(String uuid);
	
	@Query("from  Dict  WHERE description like '%支付方式%' and label !='快速支付'  and label !='POS机付款'")
	public List<Dict> findDictByPayType();
	@Query("from SysDefineDict where delFlag='" + 0 + "' and type=?1 order by sort")
	public List<SysDefineDict> findSysDefindDict(String type);
}

/**
 * DAO自定义接口
 * @author zj
 */
interface DictDaoCustom extends BaseDao<Dict> {
	List<Dict> getFromArea4T1(String type);
}

/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class DictDaoImpl extends BaseDaoImpl<Dict> implements DictDaoCustom {

	/**
	 * 出发城市
	 * 数据取T2后台基础信息维护中的所有出发城市；选项的展示顺序为：由T1端展示的产品的出发城市由多的到少的顺序；
	 * @return
	 */
	@Override
	public List<Dict> getFromArea4T1(String type) {
		DateTime dt = new DateTime();
		StringBuffer sb = new StringBuffer();
//		sb.append("SELECT " +
//				" * " +
//				"FROM " +
//				"sys_dict d " +
//				"LEFT JOIN ( " +
//				"SELECT " +
//				" t.fromArea, " +
//				" ata.parentIds, " +
//				" COUNT(*) count " +
//				"FROM " +
//				" activitygroup a " +
//				"LEFT JOIN travelactivity t ON a.srcActivityId = t.id " +
//				"LEFT JOIN ( " +
//				" SELECT " +
//				"  su.id AS 'createBy', " +
//				"  so.id AS 'sys_office_id', " +
//				"  so. NAME AS 'supplierName', " +
//				"  so.shelfRightsStatus AS 'shelfRightsStatus', " +
//				"  su.quauqBookOrderPermission AS 'quauqBookOrderPermission', " +
//				"  so.charge_rate " +
//				" FROM " +
//				"  sys_user su " +
//				" LEFT JOIN sys_office so ON su.companyId = so.id " +
//				" WHERE " +
//				"  so.delFlag = 0 " +
//				" ) sup ON sup.createBy = a.createBy " +
//				"LEFT JOIN ( " +
//				" SELECT " +
//				"  a.srcActivityId AS 'travelactivity_id', " +
//				"  GROUP_CONCAT(a.targetAreaId) AS 'sys_area_id', " +
//				" GROUP_CONCAT(sa.parentIds) parentIds " +
//				" FROM " +
//				"  activitytargetarea a " +
//				" LEFT JOIN sys_area sa on a.targetAreaId = sa.id " +
//				" GROUP BY " +
//				"  a.srcActivityId " +
//				" ) ata ON t.id = ata.travelactivity_id " +
//				"WHERE " +
//				" t.activity_kind = '2' " +
//				" AND t.delFlag = 0 AND t.activityStatus = 2 " +
//				"AND a.delFlag = 0 " +
//				"AND ( " +
//				" sup.shelfRightsStatus = 0 " +
////				" AND sup.quauqBookOrderPermission = 1 " +
//				" ) ");
//		if (StringUtils.isBlank(type) || "100000".equals(type)) {
//			sb.append(" and FIND_IN_SET(100000, parentIds) ");
//		} else {
//			sb.append(" and FIND_IN_SET(200000, parentIds) ");
//		}
//				sb.append(" and (a.quauqAdultPrice is not null or a.quauqChildPrice is not null or a.quauqSpecialPrice is not null) " +
//				"AND a.groupOpenDate >= '" + dt.toString("yyyy-MM-dd") + "' " +
//				"GROUP BY " +
//				" fromArea " +
//				") c ON d. " +
//				"VALUE " +
//				" = c.fromArea " +
//				"WHERE " +
//				"d.value = c.fromArea and d.type = 'from_area' " +
//				"and d.delFlag = 0 ");
//		if (StringUtils.isBlank(type) || "100000".equals(type)) {
//			sb.append(" and FIND_IN_SET(100000, c.parentIds) ");
//		} else {
//			sb.append(" and FIND_IN_SET(200000, c.parentIds) ");
//		}
//				sb.append(" ORDER BY c.count DESC");
		sb.append("SELECT ")
				.append(" d.* ")
				.append("FROM ")
				.append(" sys_dict d, ")
				.append(" ( ")
				.append("  SELECT ")
				.append("   t.fromArea, ")
				.append("   count(*) count ")
				.append("  FROM ")
				.append("   activitygroup a ")
				.append("  LEFT JOIN travelactivity t ON a.srcActivityId = t.id ")
				.append("  LEFT JOIN ( ")
				.append("   SELECT ")
				.append("    su.id AS 'createBy', ")
				.append("    so.id AS 'sys_office_id', ")
				.append("    so. NAME AS 'supplierName', ")
				.append("    so.shelfRightsStatus AS 'shelfRightsStatus', ")
				.append("    su.quauqBookOrderPermission AS 'quauqBookOrderPermission', ")
				.append("    so.charge_rate ")
				.append("   FROM ")
				.append("    sys_user su ")
				.append("   LEFT JOIN sys_office so ON su.companyId = so.id ")
				.append("   WHERE ")
				.append("    so.delFlag = 0 ")
				.append("  ) sup ON sup.createBy = a.createBy ")
				.append("  LEFT JOIN ( ")
				.append(AreaUtil.getLooseActivityTargetAreaSql())
				.append("  ) ata ON t.id = ata.travelactivity_id ")
				.append("  WHERE ")
				.append("   t.activity_kind = '2' ")
				.append("  AND t.delFlag = 0 ")
				.append("  AND t.activityStatus = 2 ")
				.append("  AND a.delFlag = 0 ");
//				.append("  AND FIND_IN_SET(100000, ata.parentIds) ")
		if (Context.FREE_TRAVEL_FOREIGN.equals(type)) {
			sb.append(" and FIND_IN_SET(100000, ata.parentIds) ");
		} else if (Context.FREE_TRAVEL_INLAND.equals(type)){
			sb.append(" and FIND_IN_SET(200000, ata.parentIds) ");
		}
 		sb.append("  AND sup.shelfRightsStatus = 0 ")
				.append("  AND a.is_t1 = 1 ")
				.append("  AND a.groupOpenDate >= '" + dt.toString("yyyy-MM-dd") + "' ")
				.append("  GROUP BY ")
				.append("   t.fromArea ")
				.append(" ) t ")
				.append("WHERE ")
				.append(" t.fromArea = d.value ")
				.append("AND d.type = 'from_area' ")
				.append("ORDER BY ")
				.append(" count DESC");
		return findBySql(sb.toString(), Dict.class);
	}
	
}
