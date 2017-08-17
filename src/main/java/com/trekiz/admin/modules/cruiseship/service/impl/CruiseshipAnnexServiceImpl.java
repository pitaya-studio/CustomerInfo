/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.exception.common.ExceptionConstants;
import com.trekiz.admin.common.exception.util.LogMessageUtil;
import com.trekiz.admin.common.exception.util.QuauqExceptionFactory;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipAnnexDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipAnnexInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipAnnexQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipAnnexService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CruiseshipAnnexServiceImpl  extends BaseService implements CruiseshipAnnexService{
	@Autowired
	private CruiseshipAnnexDao cruiseshipAnnexDao;
	/**
	 * 保存接口
	 * @author zhangchao
	 * 2016-2-2
	 */
	public void save (CruiseshipAnnex cruiseshipAnnex) throws BaseException4Quauq{
		try {
			super.setOptInfo(cruiseshipAnnex, BaseService.OPERATION_ADD);
			cruiseshipAnnexDao.saveObj(cruiseshipAnnex);
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
	}
	
	public void save (CruiseshipAnnexInput cruiseshipAnnexInput){
		CruiseshipAnnex cruiseshipAnnex = cruiseshipAnnexInput.getCruiseshipAnnex();
		super.setOptInfo(cruiseshipAnnex, BaseService.OPERATION_ADD);
		cruiseshipAnnexDao.saveObj(cruiseshipAnnex);
	}
	
	public void update (CruiseshipAnnex cruiseshipAnnex){
		super.setOptInfo(cruiseshipAnnex, BaseService.OPERATION_UPDATE);
		cruiseshipAnnexDao.updateObj(cruiseshipAnnex);
	}
	
	public CruiseshipAnnex getById(java.lang.Integer value) {
		return cruiseshipAnnexDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		CruiseshipAnnex obj = cruiseshipAnnexDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<CruiseshipAnnex> find(Page<CruiseshipAnnex> page, CruiseshipAnnexQuery cruiseshipAnnexQuery) {
		DetachedCriteria dc = cruiseshipAnnexDao.createDetachedCriteria();
		
	   	if(cruiseshipAnnexQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipAnnexQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipAnnexQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getMainUuid())){
			dc.add(Restrictions.eq("mainUuid", cruiseshipAnnexQuery.getMainUuid()));
		}
	   	if(cruiseshipAnnexQuery.getDocId()!=null){
	   		dc.add(Restrictions.eq("docId", cruiseshipAnnexQuery.getDocId()));
	   	}
	   	if(cruiseshipAnnexQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", cruiseshipAnnexQuery.getType()));
	   	}
	   	if(cruiseshipAnnexQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipAnnexQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getDocName())){
			dc.add(Restrictions.eq("docName", cruiseshipAnnexQuery.getDocName()));
		}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getDocPath())){
			dc.add(Restrictions.eq("docPath", cruiseshipAnnexQuery.getDocPath()));
		}
	   	if(cruiseshipAnnexQuery.getDocType()!=null){
	   		dc.add(Restrictions.eq("docType", cruiseshipAnnexQuery.getDocType()));
	   	}
	   	if(cruiseshipAnnexQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipAnnexQuery.getCreateBy()));
	   	}
		if(cruiseshipAnnexQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipAnnexQuery.getCreateDate()));
		}
	   	if(cruiseshipAnnexQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipAnnexQuery.getUpdateBy()));
	   	}
		if(cruiseshipAnnexQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipAnnexQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipAnnexQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipAnnexDao.find(page, dc);
	}
	
	public List<CruiseshipAnnex> find( CruiseshipAnnexQuery cruiseshipAnnexQuery) {
		DetachedCriteria dc = cruiseshipAnnexDao.createDetachedCriteria();
		
	   	if(cruiseshipAnnexQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipAnnexQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipAnnexQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getMainUuid())){
			dc.add(Restrictions.eq("mainUuid", cruiseshipAnnexQuery.getMainUuid()));
		}
	   	if(cruiseshipAnnexQuery.getDocId()!=null){
	   		dc.add(Restrictions.eq("docId", cruiseshipAnnexQuery.getDocId()));
	   	}
	   	if(cruiseshipAnnexQuery.getType()!=null){
	   		dc.add(Restrictions.eq("type", cruiseshipAnnexQuery.getType()));
	   	}
	   	if(cruiseshipAnnexQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipAnnexQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getDocName())){
			dc.add(Restrictions.eq("docName", cruiseshipAnnexQuery.getDocName()));
		}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getDocPath())){
			dc.add(Restrictions.eq("docPath", cruiseshipAnnexQuery.getDocPath()));
		}
	   	if(cruiseshipAnnexQuery.getDocType()!=null){
	   		dc.add(Restrictions.eq("docType", cruiseshipAnnexQuery.getDocType()));
	   	}
	   	if(cruiseshipAnnexQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipAnnexQuery.getCreateBy()));
	   	}
		if(cruiseshipAnnexQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipAnnexQuery.getCreateDate()));
		}
	   	if(cruiseshipAnnexQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipAnnexQuery.getUpdateBy()));
	   	}
		if(cruiseshipAnnexQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipAnnexQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipAnnexQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipAnnexQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipAnnexDao.find(dc);
	}
	
	public CruiseshipAnnex getByUuid(String uuid) {
		return cruiseshipAnnexDao.getByUuid(uuid);
	}
	
	public List<CruiseshipAnnex> getByStockUuid(String stockUuid){
		return  cruiseshipAnnexDao.getByStockUuid(stockUuid);
	}
	
	public void removeByUuid(String uuid) {
		CruiseshipAnnex cruiseshipAnnex = getByUuid(uuid);
		cruiseshipAnnex.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(cruiseshipAnnex);
	}
	
	public boolean batchDelete(String[] uuids) {
		return cruiseshipAnnexDao.batchDelete(uuids);
	}
	
	/**
	 *通过mainUuid 获得CruiseshipAnnex   type:1游轮 2游轮库存
	 *
	 */
	@Override
	public List<CruiseshipAnnex> getCruiseshipAnnexByMainUuid(String uuid,Integer type) throws BaseException4Quauq{
		List<CruiseshipAnnex> list =null;
		try {
			StringBuffer sbf=new StringBuffer();
			sbf.append("SELECT id,uuid,main_uuid,doc_id,type,wholesaler_id,doc_name,")
				 .append("doc_path ,doc_type ,createBy,createDate,updateBy,updateDate,delFlag ")
				 .append("FROM cruiseship_annex WHERE  main_uuid=? and type=? and delFlag=0");
			list = cruiseshipAnnexDao.findBySql(sbf.toString(), CruiseshipAnnex.class,uuid,type);
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
		return list;
	}
	
	//获取FileList集合
	@Override
	public List<CruiseshipAnnex> getFileList(HttpServletRequest request) {
		List<CruiseshipAnnex> list =null;
		if(ArrayUtils.isNotEmpty(request.getParameterValues("hotelAnnexDocId"))){
			String[] hotelAnnexDocId = request.getParameterValues("hotelAnnexDocId");
			String[] docOriName = request.getParameterValues("docOriName");
			String[] docPath = request.getParameterValues("docPath");
			list = new ArrayList<CruiseshipAnnex>();
			for(int i=0;i<hotelAnnexDocId.length;i++){
				if(StringUtils.isNotBlank(hotelAnnexDocId[i])){
					CruiseshipAnnex ha = new CruiseshipAnnex();
					ha.setDocId(Integer.parseInt(hotelAnnexDocId[i]));
					ha.setDocPath(docPath[i]);
					ha.setDocName(docOriName[i]);
					list.add(ha);
				}
			}
		}
		return list;
	}
}
