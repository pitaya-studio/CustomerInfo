package com.trekiz.admin.agentToOffice.line.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.entity.SelectJson;

public interface TouristLineService {
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
	
	/**
	 * 通过线路玩法名称筛选
	 * @param paramMap 参数集合
	 * @return 符合筛选条件的线路玩法集合
	 * @author yang.wang@quauq.com
	 * @date 2016.10.14
	 * */
	public List<TouristLine> filterTouristLines(Map<String, String> paramMap);
	
	/**
	 * 根据目的地id获取区域id，拼接ids字符串
	 * @param destinationIds 目的地ids
	 * @author yang.wang@quauq.com
	 * @date 2016.10.17
	 * */
	public String getAreaIdsByDIds(String destinationIds);
	
	/**
	 * 根据ids获取所属区域
	 * @param areaIds 
	 * @author yang.wang@quauq.com
	 * @date 2016.10.17
	 * */
	public String getDistrictNameByIds(String areaIds);
	
	/**
	 * 获取所属旅游区域下拉列表
	 * @author yang.wang@quauq.com
	 * @date 2016.10.17
	 * */
	public SelectJson getAllAreas();
	
	/**
	 * 游玩路线分页显示
	 * @param paramMap 参数集合
	 * @author yang.wang@quauq.com
	 * @date 2016.10.18
	 * */
	public Page<TouristLine> filterTouristLinesWithPage(Page<TouristLine> page, Map<String, String> paramMap);
	
	/**
	 * 查询并判断路线是否关联产品
	 * @param id 游玩线路id
	 * @return true为已关联；false为未关联
	 * @author yang.wang@quauq.com
	 * @date 2016.10.18
	 * */
	public boolean checkLine(Long id);
	
	/**
	 * 根据线路名称查询线路
	 * @param name
	 * @return
	 */
	public TouristLine getLineByName(String name);
}
