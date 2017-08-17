/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service.impl;

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
import com.trekiz.admin.modules.mtourOrder.dao.TravelerPapersTypeDao;
import com.trekiz.admin.modules.mtourOrder.entity.TravelerPapersType;
import com.trekiz.admin.modules.mtourOrder.input.TravelerPapersTypeInput;
import com.trekiz.admin.modules.mtourOrder.query.TravelerPapersTypeQuery;
import com.trekiz.admin.modules.mtourOrder.service.TravelerPapersTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class TravelerPapersTypeServiceImpl  extends BaseService implements TravelerPapersTypeService{
	@Autowired
	private TravelerPapersTypeDao travelerPapersTypeDao;

	public void save (TravelerPapersType travelerPapersType){
		super.setOptInfo(travelerPapersType, BaseService.OPERATION_ADD);
		travelerPapersTypeDao.saveObj(travelerPapersType);
	}
	
	public void save (TravelerPapersTypeInput travelerPapersTypeInput){
		TravelerPapersType travelerPapersType = travelerPapersTypeInput.getTravelerPapersType();
		super.setOptInfo(travelerPapersType, BaseService.OPERATION_ADD);
		travelerPapersTypeDao.saveObj(travelerPapersType);
	}
	
	public void update (TravelerPapersType travelerPapersType){
		super.setOptInfo(travelerPapersType, BaseService.OPERATION_UPDATE);
		travelerPapersTypeDao.updateObj(travelerPapersType);
	}
	
	public TravelerPapersType getById(java.lang.Integer value) {
		return travelerPapersTypeDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		TravelerPapersType obj = travelerPapersTypeDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<TravelerPapersType> find(Page<TravelerPapersType> page, TravelerPapersTypeQuery travelerPapersTypeQuery) {
		DetachedCriteria dc = travelerPapersTypeDao.createDetachedCriteria();
		
	   	if(travelerPapersTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", travelerPapersTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(travelerPapersTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", travelerPapersTypeQuery.getUuid()));
		}
	   	if(travelerPapersTypeQuery.getTravelerId()!=null){
	   		dc.add(Restrictions.eq("travelerId", travelerPapersTypeQuery.getTravelerId()));
	   	}
	   	if(travelerPapersTypeQuery.getOrderId()!=null){
	   		dc.add(Restrictions.eq("orderId", travelerPapersTypeQuery.getOrderId()));
	   	}
	   	if(travelerPapersTypeQuery.getPapersType()!=null){
	   		dc.add(Restrictions.eq("papersType", travelerPapersTypeQuery.getPapersType()));
	   	}
		if(travelerPapersTypeQuery.getValidityDate()!=null){
			dc.add(Restrictions.eq("validityDate", travelerPapersTypeQuery.getValidityDate()));
		}
		if (StringUtils.isNotEmpty(travelerPapersTypeQuery.getIdCard())){
			dc.add(Restrictions.eq("idCard", travelerPapersTypeQuery.getIdCard()));
		}
		if(travelerPapersTypeQuery.getIssueDate()!=null){
			dc.add(Restrictions.eq("issueDate", travelerPapersTypeQuery.getIssueDate()));
		}
		if (StringUtils.isNotEmpty(travelerPapersTypeQuery.getIssuePlace())){
			dc.add(Restrictions.eq("issuePlace", travelerPapersTypeQuery.getIssuePlace()));
		}
	   	if(travelerPapersTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", travelerPapersTypeQuery.getCreateBy()));
	   	}
		if(travelerPapersTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", travelerPapersTypeQuery.getCreateDate()));
		}
	   	if(travelerPapersTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", travelerPapersTypeQuery.getUpdateBy()));
	   	}
		if(travelerPapersTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", travelerPapersTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(travelerPapersTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", travelerPapersTypeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return travelerPapersTypeDao.find(page, dc);
	}
	
	public List<TravelerPapersType> find( TravelerPapersTypeQuery travelerPapersTypeQuery) {
		DetachedCriteria dc = travelerPapersTypeDao.createDetachedCriteria();
		
	   	if(travelerPapersTypeQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", travelerPapersTypeQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(travelerPapersTypeQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", travelerPapersTypeQuery.getUuid()));
		}
	   	if(travelerPapersTypeQuery.getTravelerId()!=null){
	   		dc.add(Restrictions.eq("travelerId", travelerPapersTypeQuery.getTravelerId()));
	   	}
	   	if(travelerPapersTypeQuery.getOrderId()!=null){
	   		dc.add(Restrictions.eq("orderId", travelerPapersTypeQuery.getOrderId()));
	   	}
	   	if(travelerPapersTypeQuery.getPapersType()!=null){
	   		dc.add(Restrictions.eq("papersType", travelerPapersTypeQuery.getPapersType()));
	   	}
		if(travelerPapersTypeQuery.getValidityDate()!=null){
			dc.add(Restrictions.eq("validityDate", travelerPapersTypeQuery.getValidityDate()));
		}
		if (StringUtils.isNotEmpty(travelerPapersTypeQuery.getIdCard())){
			dc.add(Restrictions.eq("idCard", travelerPapersTypeQuery.getIdCard()));
		}
		if(travelerPapersTypeQuery.getIssueDate()!=null){
			dc.add(Restrictions.eq("issueDate", travelerPapersTypeQuery.getIssueDate()));
		}
		if (StringUtils.isNotEmpty(travelerPapersTypeQuery.getIssuePlace())){
			dc.add(Restrictions.eq("issuePlace", travelerPapersTypeQuery.getIssuePlace()));
		}
	   	if(travelerPapersTypeQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", travelerPapersTypeQuery.getCreateBy()));
	   	}
		if(travelerPapersTypeQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", travelerPapersTypeQuery.getCreateDate()));
		}
	   	if(travelerPapersTypeQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", travelerPapersTypeQuery.getUpdateBy()));
	   	}
		if(travelerPapersTypeQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", travelerPapersTypeQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(travelerPapersTypeQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", travelerPapersTypeQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return travelerPapersTypeDao.find(dc);
	}
	
	public TravelerPapersType getByUuid(String uuid) {
		return travelerPapersTypeDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		TravelerPapersType travelerPapersType = getByUuid(uuid);
		travelerPapersType.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(travelerPapersType);
	}
	
	public void deleteByOrderId(Long Id){
		String sql = "delete from traveler_papers_type where traveler_id in (select id from traveler where orderid = "+Id+")";
		travelerPapersTypeDao.getSession().createSQLQuery(sql).executeUpdate();
	}
	/**
	 * 删除游客相关附件
	 */
	public void deleteFileByActivityId(Long Id){
		String sql = "delete from airTicketFile where airticketId = "+Id;
		travelerPapersTypeDao.getSession().createSQLQuery(sql).executeUpdate();
	}
}
