package com.trekiz.admin.common.query.repository.impl;

import java.util.List;

import java.util.Map;

import com.trekiz.admin.common.config.Context;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.query.repository.ISelectDao;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.query.entity.SelectOption;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class SelectDaoImpl extends BaseDaoImpl<SelectOption> implements ISelectDao{

	private static final Logger log = Logger.getLogger(SelectDaoImpl.class);

	@Override
	public List<Map<String, Object>> getAgentInfo(Long companyId) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, agentName as text ")
				.append(" FROM agentinfo ")
				.append(" WHERE delFlag = '0' AND status = '1' AND supplyId =")
				.append(companyId)
				.append(" ORDER BY agentFirstLetter ");
		log.debug(sb.toString());
		List<Map<String, Object>> list = findBySql(sb.toString(), Map.class);
		return list;
	}

	@Override
	public List<SelectOption> findAgentBySalerId(Long userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, agentName as text ")
				.append(" FROM agentinfo ")
				.append(" WHERE delFlag = '0' AND status = '1' AND agentSalerId =")
				.append(userId)
				.append(" ORDER BY agentFirstLetter");
		log.debug(sb.toString());
		List<SelectOption> list = findBySql(sb.toString(), Map.class);
		return list;
	}

	/**
	 * 根据公司id 获取计调和产品发布人
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<Map<String,Object>> getOperators(Long companyId) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT a.id, a.name as text from (")
				.append(" SELECT u.id,u.name from sys_user u where u.delFlag=0 and u.id in (select userId from sys_user_role where roleId in ")
				.append("(select id from sys_role where roleType in (3, 4))) and u.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from travelactivity p, sys_user su where p.createBy=su.id and su.delFlag=0 and p.delFlag=0 and ")
				.append("su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from visa_products p, sys_user su where p.createBy=su.id and p.productStatus=2 and su.delFlag=0 and ")
				.append("p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_airticket p, sys_user su where p.createBy=su.id and p.productStatus=2 and su.delFlag=0 and ")
				.append("p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_hotel p, sys_user su where p.createBy=su.id and su.delFlag=0 and p.delFlag=0 and ")
				.append("su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_island p, sys_user su where p.createBy=su.id and su.delFlag=0 ")
				.append("and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" ) a;");
		log.debug(sb.toString());
		List<Map<String,Object>> userList = findBySql(sb.toString(), Map.class);
		return userList;
	}

	/*
	 * 根据公司id获取联合表中下单人列表
	 * @param companyId 公司id
	 * @return 
	 * @author xianglei.dong
	 */
	@Override
	public List<Map<String, Object>> getUnionPlaceOrderPersons(Long companyId) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT a.id, a.name as text from (")
				.append(" SELECT u.id,u.name from sys_user u where u.delFlag=0 and u.id in (select userId from sys_user_role where roleId in (select id from sys_role where roleType in (1, 2))) and u.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from travelactivity p, productorder o, sys_user su where p.id=o.productId and o.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from visa_products p, visa_order o, sys_user su where p.id=o.visa_product_id and p.productStatus=2 and o.create_by=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_airticket p, airticket_order o, sys_user su where p.id=o.airticket_id and p.productStatus=2 and o.create_by=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_hotel p, hotel_order o, sys_user su where p.uuid=o.activity_hotel_uuid and o.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(" UNION ")
				.append(" SELECT su.id, su.name from activity_island p, island_order o, sys_user su where p.uuid=o.activity_island_uuid and o.createBy=su.id and su.delFlag=0 and p.delFlag=0 and su.companyId=").append(companyId)
				.append(") a;");
		log.debug(sb.toString());
		List<Map<String, Object>> placePersonList = findBySql(sb.toString(), Map.class);
		
		return placePersonList;
	}

	@Override
	public List<Map<String,Object>> loadDepartment(Long companyId) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT d.id, d.name AS text ")
		   .append(" FROM department d ")
		   .append(" WHERE d.office_id = ").append(companyId)
		   .append(" AND d.delFlag = '0' order by sort");

		List<Map<String,Object>> departmentList = findBySql(sql.toString(), Map.class);
		return departmentList;
	}

	/**
	 * 根据companyid获取可发布产品的人
	 * @author chao.zhang@quauq.com
	 */

	@Override
	public List<Map<String,Object>> getAllUsers() {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("id,");
		sbf.append("name AS text ");
		sbf.append("FROM sys_user ");
		sbf.append("WHERE companyId=? AND delFlag=?");
		log.debug(sbf.toString());
		Long companyId=UserUtils.getUser().getCompany().getId();
		List<Map<String,Object>> list=findBySql(sbf.toString(),Map.class, companyId,0);
		return list;
	}


    @Override
    public List<Map<String, Object>> getReviewer(Long companyId) {
        StringBuffer sqls = new StringBuffer();
        sqls.append("SELECT r.id, r.name as text FROM sys_user r WHERE r.delFlag = ").append(Context.DEL_FLAG_NORMAL)
            .append(" AND r.companyId = ? ");
        log.debug(sqls.toString());
        List<Map<String, Object>> reviewList = findBySql(sqls.toString(), Map.class, companyId);
        return reviewList;
    }

	/**
	 * 该公司下的渠道商
	 * @param companyId
	 * @return
	 * @author yudong.xu
	 */
	@Override
	public List<Map<String, Object>> getAgentInfoBySupplyId(Long companyId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.id,a.agentName AS text FROM agentinfo a WHERE a.delFlag = 0 AND a.status = 1 AND a.supplyId = ? ")
		   .append(" ORDER BY a.agentFirstLetter");
		List<Map<String, Object>> agentList = findBySql(sql.toString(), Map.class,companyId);
		return agentList;
	}

	/**
	 * 根据渠道跟进销售人员的id，查询该该销售人员下的渠道商
	 * @param salerId
	 * @return
	 * @author yudong.xu
	 */
	@Override
	public List<Map<String, Object>> getAgentInfoBySalerId(Long salerId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.id,a.agentName AS text FROM agentinfo a WHERE a.delFlag = 0 AND a.status = 1 AND a.agentSalerId = ? ")
				.append(" ORDER BY a.agentFirstLetter");
		List<Map<String, Object>> agentList = findBySql(sql.toString(), Map.class, salerId);
		return agentList;
	}

	/**
	 * 根据批发商id或得计调和计调主管
	 * @author chao.zhang@quauq.com
	 * @return
	 * @param
	 */
	@Override
	public List<Map<String, Object>> getInnerOperator() {
		Long companyId=UserUtils.getUser().getCompany().getId();
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT u.id,u.name AS text FROM sys_user u WHERE u.delFlag=? AND u.companyId=? AND ");
		sbf.append("u.id IN (SELECT userId FROM sys_user_role WHERE roleId IN (SELECT id FROM sys_role WHERE roleType IN(3,4))) ");
		log.debug(sbf.toString());
		List<Map<String,Object>> list=findBySql(sbf.toString(),Map.class, 0,companyId);
		return list;
	}

	@Override
	public List<Map<String, Object>> getSupplier() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT su.id,su.supplierName AS text FROM supplier_info su WHERE su.delFlag=0 AND su.companyId=?");
		log.debug(sb.toString());
		
		List<Map<String,Object>> list=findBySql(sb.toString(), Map.class, companyId);
		return list;
	}


	@Override
	public List<Map<String, Object>> findInnerSales(Long companyId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append(" select u.id,u.name as text from sys_user u where u.delFlag=? and u.companyId=? and ");
		sbf.append(" u.id in (select userId from sys_user_role where roleId in (select id from sys_role)) ");
		log.debug(sbf.toString());
		List<Map<String,Object>> list=findBySql(sbf.toString(),Map.class, 0,companyId);
		return list;
	}

	/**
	 * 切位渠道商，排除非签约渠道 （ID < 0）
	 * @param companyId
	 * @return
	 * @author yudong.xu
	 */
	@Override
	public List<Map<String, Object>> getStockAgentinfo(Long companyId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.id,a.agentName AS text FROM agentinfo a WHERE a.delFlag = 0 AND a.status = 1 AND (a.supplyId = 0 or a.supplyId = ?) ")
				.append("AND a.id > 0 ORDER BY a.agentFirstLetter");
		List<Map<String, Object>> agentList = findBySql(sql.toString(), Map.class,companyId);
		return agentList;
	}

	@Override
	public List<Map<String, String>> getFromBanks(Integer companyId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT DISTINCT bankName FROM plat_bank_info WHERE beLongPlatId=? AND delFlag=?");
		List<Map<String, String>> list = this.findBySql(sbf.toString(), Map.class, companyId,0);
		return list;
	}
}
