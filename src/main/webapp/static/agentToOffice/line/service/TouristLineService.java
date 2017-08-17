package com.trekiz.admin.agentToOffice.line.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.agentToOffice.line.entity.TouristLine;

public interface TouristLineService {
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
	 * 添加线路
	 * @param touristLine
	 * @author chao.zhang@quauq.com
	 */
	public void saveTouristLine(TouristLine touristLine);
	
	/**
	 * 修改线路
	 * @param touristLine
	 * @author yang.wang@quauq.com
	 * */
	public void updateTouristLine(TouristLine touristLine);
	
	/**
	 * 验证线路名称是否重复
	 * @param lineName
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public boolean checkLineName(String lineName, Long id);
	
	/**
	 * 验证所含区域是否重复
	 * @param areas
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public boolean checkAreas(String areas, Long id);
	
	/**
	 * 通过id查询线路
	 * @param id
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public TouristLine getById(Long id);

	public boolean isExist(List<String> targetAreaArray);
}
