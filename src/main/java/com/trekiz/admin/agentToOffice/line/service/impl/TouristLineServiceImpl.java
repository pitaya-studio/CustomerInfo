package com.trekiz.admin.agentToOffice.line.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.line.dao.TouristLineDao;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.entity.SelectJson;
import com.trekiz.admin.common.query.entity.SelectOption;
import com.trekiz.admin.common.query.utils.CommonUtils;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.sys.repository.AreaDao;
@Service
@Transactional(readOnly = true)
public class TouristLineServiceImpl extends BaseService implements TouristLineService{
	
	@Autowired
	private TouristLineDao touristLineDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private TravelActivityDao travelActivityDao;
	
	/**
	 * 根据批发商id查询此批发商下所有的线路
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<TouristLine> getAllTouristLine() {
		
		List<TouristLine> list = touristLineDao.getAllTouristLine();
		return list;
	}
	
	/**
	 * 删除线路
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void deleteTouristLine(Long id) {
		if(null==id){
			throw new RuntimeException("id不能为空！");
		}
		touristLineDao.deleteTouristLine(id);
	}
	
	/**
	 * 添加线路
	 * @param priceStrategyLine
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public void saveTouristLine(TouristLine touristLine) {
		if(touristLine==null){
			throw new RuntimeException("添加的数据不能为空");
		}
		super.setOptInfo(touristLine, BaseService.OPERATION_ADD);
		touristLineDao.saveObj(touristLine);
	}
	
	/**
	 * 查看线路名称是否重复
	 * @param lineName
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public boolean checkLineName(String lineName, Long id) {
		List<TouristLine> list = touristLineDao.getByLineName(lineName, id);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 查看所含区域是否重复
	 * @param areas
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public boolean checkAreas(String areas, Long id) {
		List<TouristLine> list = touristLineDao.getByAreas(areas, id);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public TouristLine getById(Long id) {
		TouristLine line = touristLineDao.getById(id);
		return line;
	}

	@Override
	public boolean isExist(List<String> targetAreaArray) {
		for(String str:targetAreaArray){
			if(null==touristLineDao.getById(new Long(str))){
				return false;
			}
		}
		return true;
	}

	@Override
	public void updateTouristLine(TouristLine touristLine) {
		
		if (touristLine == null) {
			throw new RuntimeException("数据为空");
		}
		touristLineDao.updateObj(touristLine);
	}

	@Override
	public List<TouristLine> filterTouristLines(Map<String, String> paramMap) {
		
		return touristLineDao.filterTouristLines(paramMap);
	}

	@Override
	public String getAreaIdsByDIds(String destinationIds) {
		
		String areaIds = null;
		if (StringUtils.isNotBlank(destinationIds)) {
			StringBuffer sb = new StringBuffer("");
			Set<Long> distictIdSet = new HashSet<>();
			
			String[] dIds = destinationIds.split(",");
			for (String dId : dIds) {
				if (StringUtils.isNotBlank(dId)) {
					Long districtId = areaDao.getById(Long.parseLong(dId)).getSysDistrictId();
					if (districtId != null) {
						distictIdSet.add(districtId); // 去除重复
					}
				}
			}
			
			for (Long districtId : distictIdSet) {
				sb.append(districtId).append(","); // 区域id拼接
			}
			areaIds = sb.substring(0, sb.length() - 1);
		}
		return areaIds;
	}

	@Override
	public String getDistrictNameByIds(String areaIds) {
		
		StringBuffer districtNameBuffer = new StringBuffer();
		if (StringUtils.isNotBlank(areaIds)) {
			String[] aIds = areaIds.split(",");
			List<String> districtNames = touristLineDao.getDistrictNameById(aIds);
			
			if (districtNames != null && districtNames.size() > 0) {
				for (String name : districtNames) {
					districtNameBuffer.append(name).append("、");
				}
				return districtNameBuffer.substring(0, districtNameBuffer.length() - 1);
			}
		}
		return null;
	}

	@Override
	public SelectJson getAllAreas() {
		
		SelectJson selectJson = new SelectJson();
		try {
			List<Map<String, Object>> list = touristLineDao.getAllAreas();
			List<SelectOption> listSelectOption = CommonUtils.toListSelectOption(list);
			selectJson.setData(listSelectOption);
		} catch (Exception e) {
			selectJson.setError("系统异常，请联系管理员");
			e.printStackTrace();
		}
		return selectJson;
	}

	@Override
	public Page<TouristLine> filterTouristLinesWithPage(Page<TouristLine> page,
			Map<String, String> paramMap) {

		List<TouristLine> touristLines = touristLineDao.filterTouristLines(paramMap);
		// 返回页面的分页
		List<TouristLine> results = new ArrayList<>();
		
		if (!page.isDisabled() && !page.isNotCount()) {
			if (touristLines != null && touristLines.size() > 0) {
				page.setCount(touristLines.size());
			}
			
			if (page.getCount() < 1) {
				return page;
			}
		}
		
		if (!page.isDisabled()) {
			int start = page.getFirstResult();
			int length = 0;
			if (start + page.getMaxResults() > touristLines.size()) {
				length = touristLines.size() - start;
			} else {
				length = page.getMaxResults();
			}
			
			for (int i = 0; i < length; i++) {
				results.add(touristLines.get(start + i));
			}
		}
		
		page.setList(results);
		return page;
	}

	@Override
	public boolean checkLine(Long id) {
		
		boolean isUsed = false;
		List<TravelActivity> list = travelActivityDao.isUsed4TouristLine(id);
		if (list != null && list.size() > 0) {
			isUsed = true;
		}
		return isUsed;
	}

	@Override
	public TouristLine getLineByName(String name) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM tourist_line WHERE line_name = '"+name+"' and delFlag = 0 " );
		List<TouristLine> list = touristLineDao.findBySql(sbf.toString(), TouristLine.class);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
