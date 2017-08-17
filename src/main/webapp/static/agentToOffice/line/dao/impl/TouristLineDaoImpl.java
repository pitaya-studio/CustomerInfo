package com.trekiz.admin.agentToOffice.line.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.line.dao.TouristLineDao;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
public class TouristLineDaoImpl extends BaseDaoImpl<TouristLine> implements TouristLineDao{
	
	/**
	 * 跟据批发商id查询该批发商下所有的线路
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public List<TouristLine> getTouristLineByCompanyId(
			Long companyId) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM tourist_line WHERE company_id=? AND delFlag=?");
		List<TouristLine> list=this.findBySql(sbf.toString(), TouristLine.class, companyId.intValue(),0);
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
		sbf.append("SELECT * FROM tourist_line WHERE company_id=? AND delFlag=? AND line_name=? AND id <> ?");
		List<TouristLine> list = this.findBySql(sbf.toString(), TouristLine.class, 
				UserUtils.getUser().getCompany().getId().intValue(), 0, lineName, id);
		return list;
	}

	@Override
	public List<TouristLine> getByAreas(String areas, Long id) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM tourist_line WHERE company_id=? AND delFlag=? AND destination_ids=? AND id<>?");
		List<TouristLine> list=this.findBySql(sbf.toString(),TouristLine.class, UserUtils.getUser().getCompany().getId().intValue(),0,areas,id);
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

}
