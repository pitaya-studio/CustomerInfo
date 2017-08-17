package com.trekiz.admin.agentToOffice.line.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.common.persistence.BaseDao;

public interface TouristLineDao extends BaseDao<TouristLine>{
	/**
	 * 跟据批发商id查询该批发商下所有的线路
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<TouristLine> getAllTouristLine();
	
	/**
	 * 删除线路
	 * @param id
	 * @author chao.zhang@quauq.com
	 */
	public void deleteTouristLine(Long id);
	
	/**
	 * 根据lineName查询线路
	 * @param lineName
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<TouristLine> getByLineName(String lineName, Long id);
	
	/**
	 * 根据所含区域查询线路
	 * @param areas
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<TouristLine> getByAreas(String areas, Long id);
	
	/**
	 * 通过id查询线路
	 * @param id
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public TouristLine getById(Long id);
	
	/**
	 * 根据游玩线路名称
	 * @param paramMap 参数集合
	 * @author yang.wang@quauq.com
	 * @date 2016.10.14
	 * */
	public List<TouristLine> filterTouristLines(Map<String, String> paramMap);
	
	/**
	 * 根据区域id获取区域名称
	 * @param districtIds 区域ids
	 * @author yang.wang@quauq.com
	 * @date 2016.10.17
	 * */
	public List<String> getDistrictNameById(String[] districtIds);
	
	/**
	 * 获取区域下拉列表数据
	 * @author yang.wang@quauq.com	
	 * @date 2016.10.17
	 * */
	public List<Map<String, Object>> getAllAreas();
}
