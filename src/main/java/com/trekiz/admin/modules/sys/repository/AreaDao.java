/**
 *
 */
package com.trekiz.admin.modules.sys.repository;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.config.Context;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author zj
 * @version 2013-11-19
 */
public interface AreaDao extends AreaDaoCustom, CrudRepository<Area, Long> {

	@Modifying
	@Query("update Area set delFlag='" + Area.DEL_FLAG_DELETE + "' where id = ?1 or parentIds like ?2")
	public int deleteById(Long id, String likeParentIds);

	// 根据id更新T1区域字段
	@Modifying
	@Query("update Area set sysDistrictId = ?1 where id = ?2")
	public int updateDistrictById(Long districtId, Long id);
	
	public List<Area> findByParentIdsLike(String parentIds);

	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' order by code")
	public List<Area> findAllList();
	
	
	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and ((type='2' and parentIds like '%100000%') or (type='4' and parentIds not like '%100000%')) order by code")
	public List<Area> findByCityList();
	
	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and id in ?1 order by code")
	public List<Area> findByFilter(List<Long> areaIds);
	
	@Query("from Area where (id=?1 or parent.id=?1 or parentIds like ?2) and delFlag='" + Area.DEL_FLAG_NORMAL + "' order by code")
	public List<Area> findAllChild(Long parentId, String likeParentIds);
	
	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and name like ?1 order by code")
	public List<Area> findAreaLike(String term);
	
	@Query("select id from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and parentId = ?1")
	public List<Long> findAnyChild(Long parentId);
	
	@Query("select id from Area where parentIds like ?1 and delFlag='" + Area.DEL_FLAG_NORMAL + "'")
	public List<Long> findAllChild(String parentIds);
	
	@Query(value="select * from sys_area where id in (select targetAreaId from activitytargetarea where srcActivityId=?1)", nativeQuery=true)
	public List<Area> findAreasByActivity(Long activityId);
	
	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and name=?1 order by code")
	public List<Area> findAreaByName(String term);
	
	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and type='2'")
	public List<Area> findCountrys();
	
	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and type='2' and name = ?1")
	public List<Area> findCountryByName(String countryName);

	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and name = ?1")
	public List<Area> isExist(String name);

	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' and sysDistrictId = ?1")
    List<Area> checkDistrict(Long id);
}

/**
 * DAO自定义接口
 * @author zj
 */
interface AreaDaoCustom extends BaseDao<Area> {
	public List<Map<String, Object>> getTargetArea4T1(String type);

	public List<Map<String, Object>> getCountry4T1();
}

/**
 * DAO自定义接口实现
 * @author zj
 */
@Repository
class AreaDaoImpl extends BaseDaoImpl<Area> implements AreaDaoCustom {

	@Override
	public List<Map<String, Object>> getTargetArea4T1(String type) {
		DateTime dt = new DateTime();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ")
			.append("		id,	name ")
				.append("FROM ")
				.append("sys_area d ")
				.append("LEFT JOIN( ")
				.append("SELECT ")
				.append("targetAreaId, ")
				.append("COUNT( *)count ")
				.append("FROM ")
				.append("activitygroup a ")
				.append("LEFT JOIN travelactivity t ON a.srcActivityId = t.id ")
				.append("LEFT JOIN( ")
				.append("	SELECT ")
				.append("							su.id AS 'createBy', ")
				.append("so.id AS 'sys_office_id', ")
				.append("so. NAME AS 'supplierName', ")
				.append("so.shelfRightsStatus AS 'shelfRightsStatus', ")
				.append("su.quauqBookOrderPermission AS 'quauqBookOrderPermission', ")
				.append("so.charge_rate ")
				.append("FROM ")
				.append("sys_user su ")
				.append("LEFT JOIN sys_office so ON su.companyId = so.id ")
				.append("WHERE ")
				.append("		so.delFlag = 0 ")
				.append(") sup ON sup.createBy = a.createBy ")
				.append("LEFT JOIN ( ")
				.append("SELECT ")
				.append("a.srcActivityId AS ")
				.append("'travelactivity_id', ")
				.append("		a.targetAreaId ")
				.append("FROM ")
				.append("activitytargetarea a ")
//				.append("GROUP BY ")
//				.append("a.srcActivityId ")
				.append(") ata ON t.id = ata.travelactivity_id ")
				.append("WHERE ")
				.append("t.activity_kind = '2' ")
				.append("AND t.delFlag = 0 ")
				.append("AND t.activityStatus = 2 ")
				.append("AND a.delFlag = 0 ")
				.append("AND sup.shelfRightsStatus = 0 ")
//				.append("AND( ")
//				.append("		a.quauqAdultPrice IS NOT NULL ")
//				.append("		OR a.quauqChildPrice IS NOT NULL ")
//				.append("		OR a.quauqSpecialPrice IS NOT NULL ")
//				.append(") ")
				.append(" AND a.is_t1 = 1 ")
				.append("AND a.groupOpenDate >= '" + dt.toString("yyyy-MM-dd") +"' ")
				.append("GROUP BY ")
				.append("ata.targetAreaId ")
				.append(") c ON d.id = c.targetAreaId ")
				.append("WHERE ")
				.append(" d.id = c.targetAreaId and d.type IN (1, 2, 3, 4) ") // forbug 之前条件限制,导致type=1的港澳台 id=5没查出来
				.append("AND d.delFlag = 0 ");
//		if (Context.FREE_TRAVEL_FOREIGN.equals(type)) {
//			sb.append(" and FIND_IN_SET(100000, parentIds) ");
//		} else if (Context.FREE_TRAVEL_INLAND.equals(type)){
//			sb.append(" and FIND_IN_SET(200000, parentIds) ");
//		}
				sb.append("ORDER BY ")
				.append("c.count DESC");
		return findBySql(sb.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getCountry4T1() {
		DateTime dt = new DateTime();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT " +
				" id, " +
				" name, " +
				" count, " +
				" concat " +
				"FROM " +
				" ( " +
				"  SELECT " +
				"   rr.id, " +
				"   rr.name, " +
				"   count(gg.id) count, " +
				"   GROUP_CONCAT(gg.id) concat " +
				"  FROM " +
				"   sys_area rr " +
				"  LEFT JOIN ( " +
				"   SELECT " +
				"    sa.parentId, " +
				"    sa.parentIds, " +
				"    g.* " +
				"   FROM " +
				"    activitygroup g " +
				"   LEFT JOIN ( " +
				"    SELECT " +
				"     su.id, " +
				"     o.shelfRightsStatus " +
				"    FROM " +
				"     sys_user su, " +
				"     sys_office o " +
				"    WHERE " +
				"     su.companyId = o.id " +
				"   ) su ON su.id = g.createBy " +
				"   LEFT JOIN travelactivity p ON g.srcActivityId = p.id " +
				"   LEFT JOIN activitytargetarea t ON t.srcActivityId = p.id " +
				"   LEFT JOIN sys_area sa ON t.targetAreaId = sa.id " +
				"   WHERE " +
				"    g.delFlag = 0 " +
				"    AND g.is_t1 = 1 " +
//				"   AND ( " +
//				"    g.quauqAdultPrice IS NOT NULL " +
//				"    OR g.quauqChildPrice IS NOT NULL " +
//				"    OR g.quauqSpecialPrice IS NOT NULL " +
//				"   ) " +
				"   AND g.groupOpenDate >= '" + dt.toString("yyyy-MM-dd") + "' " +
				"   AND su.shelfRightsStatus = '0' " +
				"   AND p.activityStatus = 2 " +
				"   AND p.activity_kind = 2 " +
				"   AND p.delFlag = 0 " +
				"   GROUP BY " +
				"    g.id, " +
				"    sa.parentId " +
				"  ) gg ON rr.id = gg.parentId " +
				"  WHERE " +
				"   rr.id = gg.parentId " +
				"  AND type = 2 " +
				"  AND FIND_IN_SET(100000, gg.parentIds) " +
				"  GROUP BY " +
				"   gg.parentId " +
				"  UNION " +
				"   SELECT " +
				"    rr.id, " +
				"    rr.name, " +
				"    count(gg.id) count, " +
				"    GROUP_CONCAT(gg.id) concat " +
				"   FROM " +
				"    sys_area rr " +
				"   LEFT JOIN ( " +
				"    SELECT " +
				"     sa.parentId, " +
				"     sa.parentIds, " +
				"     t.targetAreaId, " +
				"     g.* " +
				"    FROM " +
				"     activitygroup g " +
				"    LEFT JOIN ( " +
				"     SELECT " +
				"      su.id, " +
				"      o.shelfRightsStatus " +
				"     FROM " +
				"      sys_user su, " +
				"      sys_office o " +
				"     WHERE " +
				"      su.companyId = o.id " +
				"    ) su ON su.id = g.createBy " +
				"    LEFT JOIN travelactivity p ON g.srcActivityId = p.id " +
				"    LEFT JOIN activitytargetarea t ON t.srcActivityId = p.id " +
				"    LEFT JOIN sys_area sa ON t.targetAreaId = sa.id " +
				"    WHERE " +
				"     g.delFlag = 0 " +
				"     AND g.is_t1 = 1 " +
//				"    AND ( " +
//				"     g.quauqAdultPrice IS NOT NULL " +
//				"     OR g.quauqChildPrice IS NOT NULL " +
//				"     OR g.quauqSpecialPrice IS NOT NULL " +
//				"    ) " +
				"    AND g.groupOpenDate >= '" + dt.toString("yyyy-MM-dd") + "' " +
				"    AND su.shelfRightsStatus = '0' " +
				"    AND p.activityStatus = 2 " +
				"    AND p.activity_kind = 2 " +
				"    AND p.delFlag = 0 " +
				"    GROUP BY " +
				"     g.id " +
				"   ) gg ON rr.id = gg.targetAreaId " +
				"   WHERE " +
				"    rr.id = gg.targetAreaId " +
				"   AND type = 2 " +
				"   AND FIND_IN_SET(100000, gg.parentIds) " +
				"   GROUP BY " +
				"    gg.targetAreaId " +
				" ) t " +
				"ORDER BY " +
				" t.count DESC");
		return findBySql(sb.toString(), Map.class);
	}
}
