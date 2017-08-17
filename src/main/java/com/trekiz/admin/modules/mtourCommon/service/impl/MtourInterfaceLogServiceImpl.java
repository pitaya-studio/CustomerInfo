/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.service.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.mtourCommon.dao.MtourInterfaceLogDao;
import com.trekiz.admin.modules.mtourCommon.entity.MtourInterfaceLog;
import com.trekiz.admin.modules.mtourCommon.input.MtourInterfaceLogInput;
import com.trekiz.admin.modules.mtourCommon.query.MtourInterfaceLogQuery;
import com.trekiz.admin.modules.mtourCommon.service.MtourInterfaceLogService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class MtourInterfaceLogServiceImpl  extends BaseService implements MtourInterfaceLogService{
	@Autowired
	private MtourInterfaceLogDao mtourInterfaceLogDao;

	public void save (MtourInterfaceLog mtourInterfaceLog){
		super.setOptInfo(mtourInterfaceLog, BaseService.OPERATION_ADD);
		mtourInterfaceLogDao.saveObj(mtourInterfaceLog);
	}
	
	public void save (MtourInterfaceLogInput mtourInterfaceLogInput){
		MtourInterfaceLog mtourInterfaceLog = mtourInterfaceLogInput.getMtourInterfaceLog();
		super.setOptInfo(mtourInterfaceLog, BaseService.OPERATION_ADD);
		mtourInterfaceLogDao.saveObj(mtourInterfaceLog);
	}
	
	public void update (MtourInterfaceLog mtourInterfaceLog){
		super.setOptInfo(mtourInterfaceLog, BaseService.OPERATION_UPDATE);
		mtourInterfaceLogDao.updateObj(mtourInterfaceLog);
	}
	
	public MtourInterfaceLog getById(java.lang.Long value) {
		return mtourInterfaceLogDao.getById(value);
	}	
	public void removeById(java.lang.Long value){
		MtourInterfaceLog obj = mtourInterfaceLogDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<MtourInterfaceLog> find(Page<MtourInterfaceLog> page, MtourInterfaceLogQuery mtourInterfaceLogQuery) {
		DetachedCriteria dc = mtourInterfaceLogDao.createDetachedCriteria();
		
	   	if(mtourInterfaceLogQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", mtourInterfaceLogQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", mtourInterfaceLogQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getRemoteAddr())){
			dc.add(Restrictions.eq("remoteAddr", mtourInterfaceLogQuery.getRemoteAddr()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getUserAgent())){
			dc.add(Restrictions.eq("userAgent", mtourInterfaceLogQuery.getUserAgent()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getRequestUri())){
			dc.add(Restrictions.eq("requestUri", mtourInterfaceLogQuery.getRequestUri()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getMethod())){
			dc.add(Restrictions.eq("method", mtourInterfaceLogQuery.getMethod()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getParams())){
			dc.add(Restrictions.eq("params", mtourInterfaceLogQuery.getParams()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getResponse())){
			dc.add(Restrictions.eq("response", mtourInterfaceLogQuery.getResponse()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getException())){
			dc.add(Restrictions.eq("exception", mtourInterfaceLogQuery.getException()));
		}
	   	if(mtourInterfaceLogQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", mtourInterfaceLogQuery.getCreateBy()));
	   	}
		if(mtourInterfaceLogQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", mtourInterfaceLogQuery.getCreateDate()));
		}
	   	if(mtourInterfaceLogQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", mtourInterfaceLogQuery.getUpdateBy()));
	   	}
		if(mtourInterfaceLogQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", mtourInterfaceLogQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", mtourInterfaceLogQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return mtourInterfaceLogDao.find(page, dc);
	}
	
	public List<MtourInterfaceLog> find( MtourInterfaceLogQuery mtourInterfaceLogQuery) {
		DetachedCriteria dc = mtourInterfaceLogDao.createDetachedCriteria();
		
	   	if(mtourInterfaceLogQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", mtourInterfaceLogQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", mtourInterfaceLogQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getRemoteAddr())){
			dc.add(Restrictions.eq("remoteAddr", mtourInterfaceLogQuery.getRemoteAddr()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getUserAgent())){
			dc.add(Restrictions.eq("userAgent", mtourInterfaceLogQuery.getUserAgent()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getRequestUri())){
			dc.add(Restrictions.eq("requestUri", mtourInterfaceLogQuery.getRequestUri()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getMethod())){
			dc.add(Restrictions.eq("method", mtourInterfaceLogQuery.getMethod()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getParams())){
			dc.add(Restrictions.eq("params", mtourInterfaceLogQuery.getParams()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getResponse())){
			dc.add(Restrictions.eq("response", mtourInterfaceLogQuery.getResponse()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getException())){
			dc.add(Restrictions.eq("exception", mtourInterfaceLogQuery.getException()));
		}
	   	if(mtourInterfaceLogQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", mtourInterfaceLogQuery.getCreateBy()));
	   	}
		if(mtourInterfaceLogQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", mtourInterfaceLogQuery.getCreateDate()));
		}
	   	if(mtourInterfaceLogQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", mtourInterfaceLogQuery.getUpdateBy()));
	   	}
		if(mtourInterfaceLogQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", mtourInterfaceLogQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(mtourInterfaceLogQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", mtourInterfaceLogQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return mtourInterfaceLogDao.find(dc);
	}
	
	public MtourInterfaceLog getByUuid(String uuid) {
		return mtourInterfaceLogDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		MtourInterfaceLog mtourInterfaceLog = getByUuid(uuid);
		mtourInterfaceLog.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(mtourInterfaceLog);
	}

	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.mtourCommon.service.MtourInterfaceLogService#saveAll(java.util.Queue)
	 */
	@Override
	public void batchSave(List<MtourInterfaceLog> mtourInterfaceLogs) { 
		mtourInterfaceLogDao.batchSave(mtourInterfaceLogs); 
	}
}
