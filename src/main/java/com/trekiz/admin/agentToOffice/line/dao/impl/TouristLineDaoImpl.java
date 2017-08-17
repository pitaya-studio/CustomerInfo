package com.trekiz.admin.agentToOffice.line.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.line.dao.TouristLineDao;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.common.persistence.BaseDaoImpl;

@Service
public class TouristLineDaoImpl extends BaseDaoImpl<TouristLine> implements TouristLineDao{
	
	/**
	 * 跟据批发商id查询该批发商下所有的线路
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<TouristLine> getAllTouristLine() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM tourist_line WHERE delFlag=?");
		List<TouristLine> list = this.findBySql(sbf.toString(), TouristLine.class, 0);
		return list;
	}
	
	/**
	 * 删除线路
	 * @param id
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void deleteTouristLine(Long id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("UPDATE tourist_line SET delFlag=? WHERE id=? ");
		this.updateBySql(sbf.toString(),1,id);
	}

	@Override
	public List<TouristLine> getByLineName(String lineName, Long id) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM tourist_line WHERE delFlag=? AND line_name=? AND id <> ?");
		List<TouristLine> list = this.findBySql(sbf.toString(), TouristLine.class, 0, lineName, id);
		return list;
	}

	@Override
	public List<TouristLine> getByAreas(String areas, Long id) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM tourist_line WHERE delFlag=? AND destination_ids=? AND id<>?");
		List<TouristLine> list=this.findBySql(sbf.toString(),TouristLine.class, 0, areas, id);
		return list;
	}

	@Override
	public TouristLine getById(Long id) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM tourist_line WHERE id=? AND delFlag=? ");
		List<TouristLine> list=this.findBySql(sbf.toString(),TouristLine.class, id,0);
		if (null!=list&&list.size()>0) {
			return list.get(0);
		}else{
			return null;
		}
		
	}

	@Override
	public List<TouristLine> filterTouristLines(Map<String, String> paramMap) {
		
		String lineName = paramMap.get("lineName") == null ? "" : paramMap.get("lineName");
		String areaId = paramMap.get("areaId");
		
		StringBuffer sbf = new StringBuffer("");
		sbf.append("SELECT * FROM tourist_line WHERE line_name LIKE '%").append(lineName)
		   .append("%' AND delFlag = 0 ORDER BY createDate DESC ");
		List<TouristLine> temp = this.findBySql(sbf.toString(), TouristLine.class);
		
		List<TouristLine> list = null;
		if (StringUtils.isNotBlank(areaId)) { // 当筛选条件中有区域条件
			list = new ArrayList<>();
			for (TouristLine tl : temp) {
				if (StringUtils.isNotBlank(tl.getAreaIds())) {
					String[] areaIds = tl.getAreaIds().split(",");
					if (Arrays.asList(areaIds).contains(areaId)) {
						list.add(tl);
					}
				}
			}
		} else { // 无区域筛选条件，返回仅筛选游玩线路名称的数据
			list = temp;
		}
		
		return list;
	}

	@Override
	public List<String> getDistrictNameById(String[] districtIds) {
		StringBuffer sql = new StringBuffer("");
		List<String> namelist = new ArrayList<>();
		
		if (districtIds != null && districtIds.length > 0) {
			sql.append("SELECT d.`name` FROM sys_district d WHERE d.id IN( ");
			for (int i = 0; i < districtIds.length - 1; i++) {
				sql.append(districtIds[i]).append(",");
			}
			sql.append(districtIds[districtIds.length - 1]).append(") AND d.delFlag = 0 ");
			namelist = this.findBySql(sql.toString());
		}

		return namelist;
	}

	@Override
	public List<Map<String, Object>> getAllAreas() {
		String sql = "SELECT id,`name` AS text FROM sys_district WHERE delFlag = 0";
		List<Map<String, Object>> list = this.findBySql(sql, Map.class);
		return list;
	}

}
