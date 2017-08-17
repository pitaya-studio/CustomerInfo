/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.service.HotelAnnexService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelAnnexServiceImpl  extends BaseService implements HotelAnnexService{
	@Autowired
	private HotelAnnexDao hotelAnnexDao;

	public void save (HotelAnnex hotelAnnex){
		hotelAnnexDao.saveObj(hotelAnnex);
	}
	
	public void update (HotelAnnex hotelAnnex){
		hotelAnnexDao.updateObj(hotelAnnex);
	}
	
	public HotelAnnex getById(java.lang.Integer value) {
		return hotelAnnexDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelAnnex obj = hotelAnnexDao.getById(value);
		obj.setDelFlag("1");
		hotelAnnexDao.updateObj(obj);
	}	
	
	
	public Page<HotelAnnex> find(Page<HotelAnnex> page, HotelAnnex hotelAnnex) {
		DetachedCriteria dc = hotelAnnexDao.createDetachedCriteria();
		
	   	if(hotelAnnex.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelAnnex.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelAnnex.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelAnnex.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelAnnex.getMainUuid())){
			dc.add(Restrictions.like("mainUuid", "%"+hotelAnnex.getMainUuid()+"%"));
		}
	   	if(hotelAnnex.getDocId()!=null){
	   		dc.add(Restrictions.eq("docId", hotelAnnex.getDocId()));
	   	}
	   	if(hotelAnnex.getType()!=null){
	   		dc.add(Restrictions.eq("type", hotelAnnex.getType()));
	   	}
	   	if(hotelAnnex.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelAnnex.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotelAnnex.getDocName())){
			dc.add(Restrictions.like("docName", "%"+hotelAnnex.getDocName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelAnnex.getDocPath())){
			dc.add(Restrictions.like("docPath", "%"+hotelAnnex.getDocPath()+"%"));
		}
	   	if(hotelAnnex.getDocType()!=null){
	   		dc.add(Restrictions.eq("docType", hotelAnnex.getDocType()));
	   	}
	   	if(hotelAnnex.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelAnnex.getCreateBy()));
	   	}
		if(hotelAnnex.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelAnnex.getCreateDate()));
		}
	   	if(hotelAnnex.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelAnnex.getUpdateBy()));
	   	}
		if(hotelAnnex.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelAnnex.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelAnnex.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelAnnex.getDelFlag()+"%"));
		}

		return hotelAnnexDao.find(page, dc);
	}
	
	public List<HotelAnnex> find( HotelAnnex hotelAnnex) {
		DetachedCriteria dc = hotelAnnexDao.createDetachedCriteria();
		
	   	if(hotelAnnex.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelAnnex.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelAnnex.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelAnnex.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelAnnex.getMainUuid())){
			dc.add(Restrictions.like("mainUuid", "%"+hotelAnnex.getMainUuid()+"%"));
		}
	   	if(hotelAnnex.getDocId()!=null){
	   		dc.add(Restrictions.eq("docId", hotelAnnex.getDocId()));
	   	}
	   	if(hotelAnnex.getType()!=null){
	   		dc.add(Restrictions.eq("type", hotelAnnex.getType()));
	   	}
	   	if(hotelAnnex.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelAnnex.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(hotelAnnex.getDocName())){
			dc.add(Restrictions.like("docName", "%"+hotelAnnex.getDocName()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelAnnex.getDocPath())){
			dc.add(Restrictions.like("docPath", "%"+hotelAnnex.getDocPath()+"%"));
		}
	   	if(hotelAnnex.getDocType()!=null){
	   		dc.add(Restrictions.eq("docType", hotelAnnex.getDocType()));
	   	}
	   	if(hotelAnnex.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelAnnex.getCreateBy()));
	   	}
		if(hotelAnnex.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelAnnex.getCreateDate()));
		}
	   	if(hotelAnnex.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelAnnex.getUpdateBy()));
	   	}
		if(hotelAnnex.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelAnnex.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelAnnex.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelAnnex.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelAnnexDao.find(dc);
	}
	
	//获取FileList集合
	@Override
	public List<HotelAnnex> getFileList(HttpServletRequest request) {
		List<HotelAnnex> list =null;
		if(ArrayUtils.isNotEmpty(request.getParameterValues("hotelAnnexDocId"))){
			String[] hotelAnnexDocId = request.getParameterValues("hotelAnnexDocId");
			String[] docOriName = request.getParameterValues("docOriName");
			String[] docPath = request.getParameterValues("docPath");
			list = new ArrayList<HotelAnnex>();
			for(int i=0;i<hotelAnnexDocId.length;i++){
				if(StringUtils.isNotBlank(hotelAnnexDocId[i])){
					HotelAnnex ha = new HotelAnnex();
					ha.setDocId(Integer.parseInt(hotelAnnexDocId[i]));
					ha.setDocPath(docPath[i]);
					ha.setDocName(docOriName[i]);
					list.add(ha);
				}
			}
		}
		return list;
	}
	
	public void removeByUuid(String uuid){
		hotelAnnexDao.delByUuid(uuid);
	}
/**
 * 根据附件表的主表uuid查找附件
 */
	@Override
	public List<HotelAnnex> getAnnexListByMainUuid(String uuid) {
		
		return hotelAnnexDao.getAnnexListByMainUuid(uuid);
	}	
	
}
