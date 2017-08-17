package com.trekiz.admin.modules.wholesalerbase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.wholesalerbase.entity.WholeArea;
import com.trekiz.admin.modules.wholesalerbase.repository.WholeAreaDao;
import com.trekiz.admin.modules.wholesalerbase.service.WholeAreaService;
@Service
@Transactional(readOnly = true)
public class WholeAreaServiceImpl implements WholeAreaService {

	@Autowired
	private WholeAreaDao  wholeAreaDao;
	@Override
	public void save(WholeArea area) {
		// TODO Auto-generated method stub
		wholeAreaDao.save(area);
	}

	@Override
	public List<WholeArea> findWholeAreaList(Long companyID) {
		List<WholeArea> list = wholeAreaDao.findWholeAreaList(companyID);
		if(list!=null && !list.isEmpty()){
			return list;
		}
		return null;
	}

	@Override
	public WholeArea findWholeAreaOne(String id, Long companyID) {
		WholeArea area = new WholeArea();
		area = wholeAreaDao.findWholeAreaOne(Long.valueOf(id), companyID);
		return area;
	}
}
