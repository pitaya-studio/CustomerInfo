package com.trekiz.admin.modules.sys.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.LogOperate;

public interface LogOperateDao extends LogOperateDaoCustom,CrudRepository<LogOperate, String>{
	
	@Query(value="from LogOperate where id = ?1")
	public LogOperate queryLogOperateById(String id);

}

/**
 * 自定义DAO接口
 * @author wangXK
 *
 */
interface LogOperateDaoCustom extends BaseDao<LogOperate> {
	public Page<Map<String,Object>> queryLogOperateList(HttpServletRequest request, HttpServletResponse response, Map<String,Object> condMap, 
			LogOperate logOperate, String beginDate, String endDate, String opecomid, Integer bussinessType);
	
	public List<Object[]> queryByParas(Integer bussinessType, Long bussinessId, String opeType);
	
	/**
	 * 获取订单结算价操作日志
	 * @param companyId
	 * @param orderType
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getPayPriceLog(Long companyId, String orderType, String orderId);
}


/**
 * 自定义DAO接口实现
 * @author wangXK
 *
 */
@Repository
class LogOperateDaoImpl extends BaseDaoImpl<LogOperate> implements LogOperateDaoCustom {
	private static final Logger log = Logger.getLogger(LogOperateDaoImpl.class);
	
	public Page<Map<String,Object>> queryLogOperateList(HttpServletRequest request, HttpServletResponse response,
			Map<String,Object> condMap, LogOperate logOperate, String beginDate, String endDate, String opecomid, Integer bussinessType){
		String basesql = "select id,modular_id,modular_name,ope_loginname,ope_id,ope_name,create_date,ope_type,content,ope_comid,ope_comname from log_operate where 1=1 ";
		StringBuffer condsql = new StringBuffer();
		if (logOperate.getModular_id() != null && !"".equals(logOperate.getModular_id())) {
			condsql = condsql.append(" and modular_id = " + logOperate.getModular_id());
		}
		if (logOperate.getOpe_type() != null && !"".equals(logOperate.getOpe_type())) {
			condsql = condsql.append(" and ope_type = " + logOperate.getOpe_type());
		}
		if (logOperate.getOpe_loginname() != null && !"".equals(logOperate.getOpe_loginname())) {
			condsql = condsql.append(" and ope_loginname = '" + logOperate.getOpe_loginname() + "'");
		}
		if (beginDate != null && !"".equals(beginDate)) {
			condsql = condsql.append(" and create_date >= " + beginDate);
		}
		if (endDate != null && !"".equals(endDate)) {
			condsql = condsql.append(" and create_date < " + endDate);
		}
		if (opecomid != null && !"".equals(opecomid)) {
			condsql = condsql.append(" and ope_comid = " + opecomid);
		}
		if (bussinessType != null) {
			condsql = condsql.append(" and bussiness_type = " + bussinessType);
		}
		
		
		//排序条件
		String orderbysql = " order by 1 = 1 ";
		
		if (condMap.get("orderModelarIdSort") != null && !"".equals(condMap.get("orderModelarIdSort"))) {
			 orderbysql += ", modular_id  " + condMap.get("orderModelarIdSort").toString();
		}
		if (condMap.get("orderOpeLoginameSort")!=null && !"".equals(condMap.get("orderOpeLoginameSort"))) {
			orderbysql += ", ope_loginname " + condMap.get("orderOpeLoginameSort").toString();
		}
		if (condMap.get("orderCreateDateSort")!=null &&!"".equals(condMap.get("orderCreateDateSort"))) {
			 orderbysql += ", create_date  " + condMap.get("orderCreateDateSort").toString();
		}
		if (condMap.get("orderOpeComIdSort")!=null && !"".equals(condMap.get("orderOpeComIdSort"))) {
			orderbysql += ", ope_comid "+condMap.get("orderOpeComIdSort").toString();
		}
		
		String sqlString = basesql + condsql.toString() + orderbysql;
		log.info("日志查询的sql语句： " + sqlString);
		return findBySql(new Page<Map<String,Object>>(request, response),sqlString,Map.class);
	}
	
	public List<Object[]> queryByParas(Integer bussinessType, Long bussinessId, String opeType) {
		String sql = "select create_date, ope_name, content " +
				"from log_operate where bussiness_type = " + bussinessType + " and bussiness_id = " + bussinessId + " and ope_type = '" + opeType + "'";
		return findBySql(sql);
	}

	@Override
	public List<Map<String, Object>> getPayPriceLog(Long companyId, String orderType, String orderId) {
		
		List<Map<String, Object>> list = new ArrayList<>();
		// 日志自定义类型标识（区分quauq订单结算价操作日志）
		String logTypeSign = "QUAUQ订单###%";
		if(companyId != null && StringUtils.isNotBlank(orderType) && StringUtils.isNotBlank(orderId)){
			StringBuffer str = new StringBuffer();
			str.append("SELECT ");
			str.append(" lo.id AS logId, ");
			str.append(" lo.ope_id AS operatorId, ");
			str.append(" lo.ope_name AS operatorName, ");
			str.append(" lo.create_date AS operationTime, ");
			str.append(" lo.content AS content, ");
			str.append(" lo.bussiness_id ");
			str.append("FROM ");
			str.append(" `log_operate` AS lo ");
			str.append("WHERE ");
			str.append(" lo.ope_comid = " + companyId + " ");
			str.append(" AND lo.content LIKE '" + logTypeSign + "' ");
			str.append(" AND lo.bussiness_type = " + orderType + " ");
			str.append(" AND lo.bussiness_id = " + orderId + " ");
			str.append("ORDER BY lo.create_date ASC ");
			
			list = findBySql(str.toString(), Map.class);
		}
		// 拆分context，获取需要的信息，并修改list的组织内容
		for (Map<String, Object> map : list) {
			String content = map.get("content").toString();
			String[] contentSplitArray = content.split("###");
			// TODO 拆分异常，待处理 （或者直接组织一个足够长度的array，填充好异常信息）
			// 如果正常
			map.put("operation", contentSplitArray[1]);
			map.put("mainContext", contentSplitArray[2]);
		}
		return list;
	}
}