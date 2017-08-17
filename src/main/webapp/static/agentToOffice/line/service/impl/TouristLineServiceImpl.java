package com.trekiz.admin.agentToOffice.line.service.impl;

import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.line.dao.TouristLineDao;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.agentToOffice.line.service.TouristLineService;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
@Service
@Transactional(readOnly = true)
public class TouristLineServiceImpl extends BaseService implements TouristLineService{
	
	@Autowired
	private TouristLineDao touristLineDao;
	
	/**
	 * 根据批发商id查询此批发商下所有的线路
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<TouristLine> getTouristLineByCompanyId(
			Long companyId) {
		if(companyId == null){
			throw new RuntimeException("批发商id为空！");
		}
		List<TouristLine> list = touristLineDao.getTouristLineByCompanyId(companyId);
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
	
}
