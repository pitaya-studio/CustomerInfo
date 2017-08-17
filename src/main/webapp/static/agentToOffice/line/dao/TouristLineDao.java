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
	public List<TouristLine> getTouristLineByCompanyId(Long companyId);
	
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
}
