package com.trekiz.admin.modules.log.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.hibernate.MyMultiTenantConnectionProviderImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.JDBCUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.log.entity.SearchLog;
import com.trekiz.admin.modules.log.repository.LogProductT1Dao;

@Service
public class T1SearchLogService {
	@Autowired
	private LogProductT1Dao  logProductT1Dao;
	/**
	 * t1搜索日志
	 * @param message 搜索内容
	 * @param productType 产品类型
	 * @param bugetType 境外游境内游类型
	 * @param searchType 操作项（如：产品名称，目的地，出发地）
	 * @param count 符合条件的总数
	 */
	public void log(final SearchLog searchLog){
		if(StringUtils.isBlank(searchLog.getMessage())){
			throw new RuntimeException("日志内容不能为空");
		}
		if(StringUtils.isBlank(searchLog.getProductType())){
			throw new RuntimeException("产品类型不能为空");
		}
		if(StringUtils.isBlank(searchLog.getBugetType())){
			throw new RuntimeException("境内外类型不能为空");
		}
		if(StringUtils.isBlank(searchLog.getSearchType())){
			throw new RuntimeException("搜索类型不能为空");
		}
		 final String tenantId = FacesContext.getCurrentTenant();
		 final MyMultiTenantConnectionProviderImpl mtcp = new MyMultiTenantConnectionProviderImpl();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement pstmt = null;
                try {
                    StringBuffer str = new StringBuffer();
                    str.append("INSERT INTO search_log (message, productType, bugetType, searchType, ")
                            .append("count, createBy,createDate) VALUES (?,?,?,?,?,?,?)");
                    connection = mtcp.getConnection(tenantId);
                    pstmt = connection.prepareStatement(str.toString());
                    pstmt.setString(1, searchLog.getMessage());
                    pstmt.setString(2, searchLog.getProductType());
                    pstmt.setString(3, searchLog.getBugetType());
                    pstmt.setString(4, searchLog.getSearchType());
                    pstmt.setLong(5, searchLog.getCount());
                    pstmt.setInt(6, searchLog.getCreateBy());
                    pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    JDBCUtils.close(connection,pstmt, null);
                }
            }
        }).start();
	}
	
	
	public Page<Map<String,Object>> getLogList(Page<Map<String,Object>> page){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT  ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 1 THEN a.message END) input, ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 2 THEN a.message END) target, ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 3 THEN a.message END) fromCity,  ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 4 THEN a.message END) arrival,  ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 5 THEN a.message END) supply,  ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 6 THEN a.message END) groupOpenDate, ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 7 THEN a.message END) days, ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 8 THEN a.message END) prices, ")
			 .append("GROUP_CONCAT(case a.searchType WHEN 9 THEN a.message END) seats, ")
			 .append("a.createDate, ")
			.append(" a.createBy, ")
			.append(" a.productType, ")
			.append(" a.bugetType, ")
			 .append("a.count ")
			.append("FROM ( ")
			.append("SELECT  ")
			.append("	GROUP_CONCAT(DISTINCT  s.message) message, ")
			.append("	s.createBy, ")
			.append("	s.createDate, ")
			.append("	s.searchType, ")
			.append("	s.count, ")
			.append("	s.productType, ")
			 .append(" s.bugetType ")
			.append("FROM ")
			.append("	search_log s LEFT JOIN  search_log s1 ON s.createDate = s1.createDate ")
			.append("WHERE s.createBy = s1.createBy ")
			.append("GROUP BY s.createDate,s.createBy,s.searchType) a ")
			 .append("GROUP BY a.createDate,a.createBy ")
		.append("ORDER BY a.createDate DESC ");
		page = logProductT1Dao.findPageBySql(page,sbf.toString(),Map.class);
		return page;
	}
}
