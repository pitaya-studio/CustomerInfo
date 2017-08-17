package com.trekiz.admin.modules.eprice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceStatistics;
import com.trekiz.admin.modules.eprice.form.EstimatePriceStatisticsForm;

public interface EsimatePriceStatisticsService {

	public List<EstimatePriceStatistics> findList(ArrayList<Map<String,Object>> salerList,EstimatePriceStatisticsForm epsForm);
	
	public List<Map<String, Object>> filterTreeData(Long extId);
}
