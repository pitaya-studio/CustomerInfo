package com.trekiz.admin.agentToOffice.officeCertification.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.sun.star.uno.RuntimeException;
import com.trekiz.admin.agentToOffice.officeCertification.dao.WholesalersCertificationDao;
import com.trekiz.admin.agentToOffice.officeCertification.service.WholesalersCertificationService;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.Office;

@Service
public class WholesalersCertificationServiceImpl implements WholesalersCertificationService{
	@Autowired
	WholesalersCertificationDao wholesalersCertificationDao;
	/**
	 * 查询所有具有上架权限的批发商和批发商上架到T1的产品团期数
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-13
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<Map<String,Object>> getAllOffice(Page<Map<String,Object>> page,String groupCodeOrOfficeNameOrActivityName) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT o.*,COUNT(ag.id) AS count ");
		sbf.append("FROM ");
		sbf.append("sys_office o LEFT JOIN travelactivity tr ON tr.proCompany = o.id ");
		sbf.append("LEFT JOIN activitygroup ag ON ag.srcActivityId = tr.id ");
		sbf.append("WHERE ");
		sbf.append("o.delFlag = ? ");
		sbf.append("AND o.shelfRightsStatus = ? ");
		sbf.append("AND tr.delFlag = ?  ");
		sbf.append("AND tr.activityStatus = 2  ");
		sbf.append("AND ag.delFlag = ? ");
		sbf.append("AND ag.is_t1 = ? ");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		sbf.append("AND ag.groupOpenDate >= '"+format.format(new Date())+"' ");
		if(StringUtils.isNoneBlank(groupCodeOrOfficeNameOrActivityName)){
			sbf.append("AND (ag.groupCode ='").append(groupCodeOrOfficeNameOrActivityName).append("' OR ");
			sbf.append("o.name LIKE '%").append(groupCodeOrOfficeNameOrActivityName).append("%' OR ");
			sbf.append("tr.acitivityName = '").append(groupCodeOrOfficeNameOrActivityName).append("')  ");
		}
		sbf.append("GROUP BY o.id ");
		return wholesalersCertificationDao.findPageBySql(page, sbf.toString(), Map.class,0,0,0,0,1 );
	}
	
	/**
	 * 根据批发商id查询批发商详情
	 * @param companyId
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-14
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Office getOfficeDetail(Long companyId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM sys_office WHERE id= ? ");
		List<Office> list = wholesalersCertificationDao.findBySql(sbf.toString(),Office.class, companyId);
		if(list.size()>0){
			return list.get(0);
		}else{
			throw new RuntimeException("该批发商不存在");
		}
	}
	
	/**
	 * 根据批发商id获得名片的ids和fileName
	 * @param companyId
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-19
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,String>> getDocIds(Long companyId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("u.cardId ");
		sbf.append("FROM ");
		sbf.append(" sys_user u  ");
		sbf.append("WHERE  u.delFlag=0 ");
		sbf.append("AND  u.companyId= "+companyId + " " );
		sbf.append("AND  u.quauqBookOrderPermission=1");
		List<Map<String,String>> list=wholesalersCertificationDao.findBySql(sbf.toString(),Map.class);
		return list;
	}
	
}
