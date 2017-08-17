package com.trekiz.admin.modules.eprice.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceStatistics;
import com.trekiz.admin.modules.eprice.form.EstimatePriceStatisticsForm;
import com.trekiz.admin.modules.eprice.repository.EstimatePriceStatisticsDao;
import com.trekiz.admin.modules.eprice.service.EsimatePriceStatisticsService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service("esimatePriceStatisticsService")
@Transactional(readOnly = true)
public class EsimatePriceStatisticsServiceImpl implements
		EsimatePriceStatisticsService {

	@Autowired
	private EstimatePriceStatisticsDao estimatePriceStatisticsDao;

	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private AreaService areaService;
	
	@Override
	public List<EstimatePriceStatistics> findList(
			ArrayList<Map<String, Object>> salerList,
			EstimatePriceStatisticsForm epsForm) {
		// TODO Auto-generated method stub
		System.out.println("---------------eprive-serveice--------epsForm"+epsForm);
		List<Map<String, Object>> list = estimatePriceStatisticsDao.find(salerList, epsForm);
		List<EstimatePriceStatistics> back = new ArrayList<EstimatePriceStatistics>();
		if(!list.isEmpty()){
			for(Map<String,Object> map : list){
				EstimatePriceStatistics stas = new EstimatePriceStatistics();
				stas = changeEstimatePriceStatistics(map,salerList);
				if(null!=stas){
					back.add(stas);
				}
			}
		}
		return back;
	}
	/**
	 * 将Map<String, Object> 转换为 EstimatePriceStatistics
	 * @param map   sql语句返回数据
	 * @param salerList		销售列表
	 * @return
	 */
	private EstimatePriceStatistics changeEstimatePriceStatistics(Map<String, Object> map,ArrayList<Map<String, Object>> salerList){
		if(map.isEmpty()){
			return null;
		}
		EstimatePriceStatistics sta = new EstimatePriceStatistics();
		sta.setCountryId(Integer.valueOf(map.get("countryId").toString()) );
		sta.setCountryName(map.get("countryName").toString());
		
		// 将分组统计值放入
		List<Integer> statisticsList = new ArrayList<Integer>();
		for(int n=0;n<salerList.size();n++){
			Integer num = Integer.valueOf(map.get("salerStatistics["+n+"]").toString());
			statisticsList.add(num);
		}
		// 将销售列表放入
		List<String> salerName = new ArrayList<String>();
		Iterator<Map<String, Object>> iter = salerList.iterator();
		while(iter.hasNext()){
			Map<String, Object> m= iter.next();
			if(StringUtils.isNotBlank((String)m.get("name"))){
				salerName.add((String)m.get("name"));
			}
		}
		sta.setSalerName(salerName);
		sta.setStatisticsList(statisticsList);
		
		return sta;
	}
	/**
	 *  渠道商登陆只显示所属批发商录过的目标区域
	 * 批发商登陆只显示自己录过的目标区域   
	 * @param extId
	 * @param response
	 * @return
	 */
//	private List<Map<String, Object>> filterTreeData(@RequestParam(required=false) Long extId, HttpServletResponse response) {
		//response.setContentType("application/json; charset=UTF-8");
	public List<Map<String, Object>> filterTreeData(Long extId) {
		List<Map<String, Object>> mapList = null;
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList(); 
		//批发商ID
		Long companyId = UserUtils.getUser().getCompany().getId();	
		targetAreaIds = travelActivityService.findAreaIds(companyId);
		if(targetAreaIds!=null && targetAreaIds.size()!=0){
			for(Map<String, Object> map:targetAreaIds){			
				childAreaIds.add(Long.parseLong(String.valueOf(map.get("id"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds,areaIds,targetAreas);
		//判断是否有自定义目标区域
		if(mapList == null || mapList.size() == 0){
			mapList = Lists.newArrayList();
			//目的地
			Map<String, Object> map = null;
			for (int i=0; i<targetAreas.size(); i++){
				Area e = targetAreas.get(i);
				if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
					// 只有国家单位，才放入mapList
					for(Long longid : childAreaIds){
						if(longid.equals(e.getId())){
							map = Maps.newHashMap();
							map.put("id", e.getId());
							map.put("pId", e.getParent()!=null?e.getParent().getId():0);
							map.put("name", e.getName());
							mapList.add(map);
							break;
						}
					}
				}
			}
		}		
		return mapList;
	}
}
