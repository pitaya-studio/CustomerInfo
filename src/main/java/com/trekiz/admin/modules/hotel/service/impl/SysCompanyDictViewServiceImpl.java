/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.SysCompanyDictViewDao;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class SysCompanyDictViewServiceImpl  extends BaseService implements SysCompanyDictViewService{
	@Autowired
	private SysCompanyDictViewDao sysCompanyDictViewDao;

	public SysCompanyDictView getByUuId(String value){
		return sysCompanyDictViewDao.getByUuId(value);
	}
	
	public Page<SysCompanyDictView> find(Page<SysCompanyDictView> page, SysCompanyDictView sysCompanyDictView) {
		DetachedCriteria dc = sysCompanyDictViewDao.createDetachedCriteria();
		
	   	if(sysCompanyDictView.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysCompanyDictView.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getLabel())){
			dc.add(Restrictions.like("label", "%"+sysCompanyDictView.getLabel()+"%"));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getValue())){
			dc.add(Restrictions.like("value", "%"+sysCompanyDictView.getValue()+"%"));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getType())){
			dc.add(Restrictions.like("type", "%"+sysCompanyDictView.getType()+"%"));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getDefaultFlag())){
			dc.add(Restrictions.like("defaultFlag", "%"+sysCompanyDictView.getDefaultFlag()+"%"));
		}
	   	if(sysCompanyDictView.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", sysCompanyDictView.getSort()));
	   	}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getDescription())){
			dc.add(Restrictions.like("description", "%"+sysCompanyDictView.getDescription()+"%"));
		}
		if (sysCompanyDictView.getCompanyId()!=null){
			dc.add(Restrictions.eq("companyId", sysCompanyDictView.getCompanyId()));
		}
	   	if(sysCompanyDictView.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysCompanyDictView.getCreateBy()));
	   	}
		if(sysCompanyDictView.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysCompanyDictView.getCreateDate()));
		}
	   	if(sysCompanyDictView.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysCompanyDictView.getUpdateBy()));
	   	}
		if(sysCompanyDictView.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysCompanyDictView.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getRemarks())){
			dc.add(Restrictions.like("remarks", "%"+sysCompanyDictView.getRemarks()+"%"));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+sysCompanyDictView.getDelFlag()+"%"));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+sysCompanyDictView.getUuid()+"%"));
		}

		dc.addOrder(Order.asc("sort"));
		return sysCompanyDictViewDao.find(page, dc);
	}
	
	public List<SysCompanyDictView> find( SysCompanyDictView sysCompanyDictView) {
		DetachedCriteria dc = sysCompanyDictViewDao.createDetachedCriteria();
		
	   	if(sysCompanyDictView.getId()!=null){
	   		dc.add(Restrictions.eq("id", sysCompanyDictView.getId()));
	   	}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getLabel())){
			dc.add(Restrictions.like("label", "%"+sysCompanyDictView.getLabel()+"%"));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getValue())){
			dc.add(Restrictions.like("value", "%"+sysCompanyDictView.getValue()+"%"));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getType())){
			dc.add(Restrictions.eq("type", sysCompanyDictView.getType()));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getDefaultFlag())){
			dc.add(Restrictions.eq("defaultFlag", sysCompanyDictView.getDefaultFlag()));
		}
	   	if(sysCompanyDictView.getSort()!=null){
	   		dc.add(Restrictions.eq("sort", sysCompanyDictView.getSort()));
	   	}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getDescription())){
			dc.add(Restrictions.like("description", "%"+sysCompanyDictView.getDescription()+"%"));
		}
		if (sysCompanyDictView.getCompanyId()!=null){
			if(sysCompanyDictView.getCompanyId().equals("-1")){
				dc.add(Restrictions.eq("companyId", sysCompanyDictView.getCompanyId()));
			}else{
				dc.add(Restrictions.in("companyId", new Long[]{-1l,sysCompanyDictView.getCompanyId()}));
			}
			
		}
	   	if(sysCompanyDictView.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", sysCompanyDictView.getCreateBy()));
	   	}
		if(sysCompanyDictView.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", sysCompanyDictView.getCreateDate()));
		}
	   	if(sysCompanyDictView.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", sysCompanyDictView.getUpdateBy()));
	   	}
		if(sysCompanyDictView.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", sysCompanyDictView.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getRemarks())){
			dc.add(Restrictions.like("remarks", "%"+sysCompanyDictView.getRemarks()+"%"));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", sysCompanyDictView.getDelFlag()));
		}
		if (StringUtils.isNotEmpty(sysCompanyDictView.getUuid())){
			dc.add(Restrictions.eq("uuid", sysCompanyDictView.getUuid()));
		}

		dc.addOrder(Order.asc("sort"));
		return sysCompanyDictViewDao.find(dc);
	}
	
	public boolean findIsExist(String type, int id, String label, Long companyId) {
		StringBuffer sb = new StringBuffer("from SysCompanyDictView view where view.type=? and view.id != ? and view.label = ? and view.delFlag = '0'");
		if("-1".equals(companyId.toString())) {
			sb.append(" and view.companyId = ?");
		} else {
			sb.append(" and (view.companyId = -1 or view.companyId = ?)");
		}
		List<SysCompanyDictView> sysCompanyDictViews = sysCompanyDictViewDao.find(sb.toString(), type, id, label, companyId);
		
		if(sysCompanyDictViews == null || sysCompanyDictViews.size() == 0) {
			return false;
		}
		return true;
	}
	
	public List<SysCompanyDictView> findByType(String type, Long companyId) {
		StringBuffer sb = new StringBuffer("from SysCompanyDictView view where view.type=? and view.delFlag = '0'");
		if(companyId == -1) {
			sb.append(" and view.companyId = ?");
		} else {
			sb.append(" and (view.companyId = '-1' or view.companyId = ?)");
		}
		List<SysCompanyDictView> sysCompanyDictViews = sysCompanyDictViewDao.find(sb.toString(), type, companyId);
		return sysCompanyDictViews;
	}
	
	public SysCompanyDictView findByUuid(String uuid) {
		StringBuffer sb = new StringBuffer("from SysCompanyDictView view where view.uuid=? and view.delFlag = "+BaseEntity.DEL_FLAG_NORMAL);

		List<SysCompanyDictView> sysCompanyDictViews = sysCompanyDictViewDao.find(sb.toString(), uuid);
		if(sysCompanyDictViews != null && sysCompanyDictViews.size() == 1) {
			return sysCompanyDictViews.get(0);
		}
		return null;
	}
	
	public List<SysCompanyDictView> findByUuids(String[] uuids) {
		return sysCompanyDictViewDao.findByUuids(uuids);
	}
}
