package com.trekiz.admin.modules.statistics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.util.Hash;

import com.trekiz.admin.common.utils.JDBCUtils;

public class A {

	public static void main(String[] args) throws Exception{
		String sql = "SELECT office.NAME, tra.acitivityName, ( SELECT label FROM sys_dict WHERE type = 'from_area' AND VALUE = tra.fromArea ), " +
				"tra.activityDuration, agp.groupOpenDate, agp.planPosition, agp.freePosition, agp.settlementAdultPrice, " +
				"agp.settlementcChildPrice, agp.settlementSpecialPrice" +
				" FROM activitygroup agp, sys_office office, travelactivity tra WHERE agp.srcActivityId = tra.id AND office.id = tra.proCompany AND agp.groupOpenDate IS NOT NULL AND agp.groupOpenDate > NOW() AND agp.groupOpenDate <= '2016-6-30' AND office.id IN (68, 353, 122, 379, 383, 356) ORDER BY office.id";
		List<Map<String,String>> list = new ArrayList<>();
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			String companyName = rs.getString(1);
			String productName = rs.getString(2);
			String fromArea = rs.getString(3);
			String activityDuration = rs.getString(4);
			String groupOpenDate = rs.getString(5);
			String planPosition = rs.getString(6);
			String freePosition = rs.getString(7);
			String settlementAdultPrice = rs.getString(8);
			String settlementcChildPrice = rs.getString(9);
			String settlementSpecialPrice = rs.getString(10);
			String productId = rs.getString(11);
			
			Map<String, String> map = new HashMap<>();
			map.put("companyName", companyName);
			map.put("productName", productName);
			map.put("fromArea", fromArea);
			map.put("activityDuration", activityDuration);
			map.put("groupOpenDate", groupOpenDate);
			map.put("planPosition", planPosition);
			map.put("freePosition", freePosition);
			map.put("settlementAdultPrice", settlementAdultPrice);
			map.put("settlementcChildPrice", settlementcChildPrice);
			map.put("settlementSpecialPrice", settlementSpecialPrice);
			map.put("companyName", companyName);
			list.add(map);
		}
	}
}
