/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipCabinDao;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipInfoDao;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipInfo;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStock;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipInfoInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipInfoQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CruiseshipInfoServiceImpl  extends BaseService implements CruiseshipInfoService{
	@Autowired
	private CruiseshipInfoDao cruiseshipInfoDao;
	
	@Autowired
	private CruiseshipStockDao cruiseshipStockDao;   
	@Autowired
	private CruiseshipCabinDao cruiseshipCabinDao;   
	public void save (CruiseshipInfo cruiseshipInfo){
		super.setOptInfo(cruiseshipInfo, BaseService.OPERATION_ADD);
		cruiseshipInfoDao.saveObj(cruiseshipInfo);
	}
	
	/**
	 * 游轮新增接口
	 * @param cruiseshipInfoInput
	 * @author zhangchao
	 * @time  2016/2/2
	 */
	public CruiseshipInfo save (CruiseshipInfoInput cruiseshipInfoInput) throws BaseException4Quauq{
		CruiseshipInfo cruiseshipInfo = cruiseshipInfoInput.getCruiseshipInfo();
		try {
			super.setOptInfo(cruiseshipInfo, BaseService.OPERATION_ADD);
			cruiseshipInfoDao.saveObj(cruiseshipInfo);   
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
	return cruiseshipInfo;	
	}
	
	/**
	 * 游轮修改接口
	 * @param cruiseshipInfo 游轮基础信息
	 * @author zhangchao
	 */
	public void update (CruiseshipInfo cruiseshipInfo)throws BaseException4Quauq{
		try {
			super.setOptInfo(cruiseshipInfo, BaseService.OPERATION_UPDATE);
			cruiseshipInfoDao.updateObj(cruiseshipInfo);
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
	}
	
	/**
	 * 通过id查询(修改时和详情时的数据)
	 * @param value CruiseshipInfo的id
	 */
	public CruiseshipInfo getById(java.lang.Integer value) throws BaseException4Quauq {
		CruiseshipInfo cruiseshipInfo=null;
		try {
			cruiseshipInfo = cruiseshipInfoDao.getById(value);
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
		return cruiseshipInfo;
	}

	/**
	 * 删除接口
	 * @param value id
	 * @author zhangchao
	 * 2016-2-2
	 */
	public void removeById(java.lang.Integer value)throws BaseException4Quauq{
		try {
			CruiseshipInfo obj = cruiseshipInfoDao.getById(value);
			obj.setDelFlag("1");
			this.update(obj);
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
	}	
	
	/**
	 * 游轮分页排序查询接口
	 * @param page
	 * @param cruiseshipInfoQuery 游轮名称
	 * @author zhangchao
	 */
	Integer x=1;
	public Page<CruiseshipInfo> find(Page<CruiseshipInfo> page, CruiseshipInfoQuery cruiseshipInfoQuery,String orderBy) throws BaseException4Quauq{
	  DetachedCriteria dc=null;
	try {
		dc = cruiseshipInfoDao.createDetachedCriteria();
			
		   	if(cruiseshipInfoQuery.getId()!=null){
		   		dc.add(Restrictions.eq("id", cruiseshipInfoQuery.getId()));
		   	}
			if (StringUtils.isNotEmpty(cruiseshipInfoQuery.getUuid())){
				dc.add(Restrictions.eq("uuid", cruiseshipInfoQuery.getUuid()));
			}
		   	if(cruiseshipInfoQuery.getWholesalerId()!=null){
		   		dc.add(Restrictions.eq("wholesalerId", cruiseshipInfoQuery.getWholesalerId()));
		   	}
			if (StringUtils.isNotEmpty(cruiseshipInfoQuery.getName())){
				dc.add(Restrictions.like("name", cruiseshipInfoQuery.getName(),MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(cruiseshipInfoQuery.getMemo())){
				dc.add(Restrictions.eq("memo", cruiseshipInfoQuery.getMemo()));
			}
		   	if(cruiseshipInfoQuery.getCreateBy()!=null){
		   		dc.add(Restrictions.eq("createBy", cruiseshipInfoQuery.getCreateBy()));
		   	}
			if(cruiseshipInfoQuery.getCreateDate()!=null){
				dc.add(Restrictions.eq("createDate", cruiseshipInfoQuery.getCreateDate()));
			}
		   	if(cruiseshipInfoQuery.getUpdateBy()!=null){
		   		dc.add(Restrictions.eq("updateBy", cruiseshipInfoQuery.getUpdateBy()));
		   	}
			if(cruiseshipInfoQuery.getUpdateDate()!=null){
				dc.add(Restrictions.eq("updateDate", cruiseshipInfoQuery.getUpdateDate()));
			}
			if (StringUtils.isNotEmpty(cruiseshipInfoQuery.getDelFlag())){
				dc.add(Restrictions.eq("delFlag", cruiseshipInfoQuery.getDelFlag()));
			}
			if (StringUtils.isBlank(page.getOrderBy())) {
				dc.addOrder(Order.desc("createDate"));
			}
			//dc.addOrder(Order.desc("id"));
	} catch (Exception e) {
		String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
		throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
	}
		return cruiseshipInfoDao.find(page, dc);
	}
	
	public List<CruiseshipInfo> find( CruiseshipInfoQuery cruiseshipInfoQuery) {
		DetachedCriteria dc = cruiseshipInfoDao.createDetachedCriteria();
		
	   	if(cruiseshipInfoQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipInfoQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipInfoQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipInfoQuery.getUuid()));
		}
	   	if(cruiseshipInfoQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipInfoQuery.getWholesalerId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipInfoQuery.getName())){
			dc.add(Restrictions.eq("name", cruiseshipInfoQuery.getName()));
		}
		if (StringUtils.isNotEmpty(cruiseshipInfoQuery.getMemo())){
			dc.add(Restrictions.eq("memo", cruiseshipInfoQuery.getMemo()));
		}
	   	if(cruiseshipInfoQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipInfoQuery.getCreateBy()));
	   	}
		if(cruiseshipInfoQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipInfoQuery.getCreateDate()));
		}
	   	if(cruiseshipInfoQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipInfoQuery.getUpdateBy()));
	   	}
		if(cruiseshipInfoQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipInfoQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipInfoQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipInfoQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipInfoDao.find(dc);
	}
	
	public CruiseshipInfo getByUuid(String uuid) throws BaseException4Quauq{
		CruiseshipInfo cruiseshipInfo=null;
		try {
			cruiseshipInfo=cruiseshipInfoDao.getByUuid(uuid);
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
		return cruiseshipInfo;
	}
	
	public void removeByUuid(String uuid) throws BaseException4Quauq{
		try {
			CruiseshipInfo cruiseshipInfo = getByUuid(uuid);
			cruiseshipInfo.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
			update(cruiseshipInfo);
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
	}
	
	public boolean batchDelete(String[] uuids) throws BaseException4Quauq {
		boolean c=false;
		try {
			c=cruiseshipInfoDao.batchDelete(uuids);
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
		return c;
	}
	
	/**
	 * 验证游轮是否被关联接口
	 * @param uuid
	 * @return
	 * @author zhangchao
	 * 2016/2/2
	 */
	public boolean updateCheck(String uuid)  throws BaseException4Quauq{
		List<CruiseshipStock> cruiseshipStocks=null;
		try {
			StringBuffer sbf=new StringBuffer();
			sbf.append("SELECT id,uuid,cruiseship_info_uuid as cruiseshipInfoUuid,ship_date as shipDate,wholesaler_id as wholesalerId,memo,")
				 .append("createBy,createDate,updateBy,updateDate,delFlag ")
				 .append("From cruiseship_stock ")
				 .append("WHERE  cruiseship_info_uuid=? and delFlag=0 and wholesaler_id=?");
			cruiseshipStocks = cruiseshipStockDao.findBySql(sbf.toString(), uuid,UserUtils.getUser().getCompany().getId());
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
		if(cruiseshipStocks.isEmpty()){
			return false;//未关联
		}else{
			return true;//已关联
		}
	}
	/**
	 * 根据游轮的uuid查询仓型
	 * @author zhangchao
	 * 
	 */
	public List<CruiseshipCabin>  getCruiseshipCabinBycruiseshipInfoUuid(String uuid) throws BaseException4Quauq{
		List<CruiseshipCabin> list=new ArrayList<CruiseshipCabin>();
		try {
			StringBuffer sbf=new StringBuffer();
			sbf.append("SELECT id,uuid,cruiseship_info_uuid ,wholesaler_id ,name as name,createBy,")
				 .append("createDate,updateBy,updateDate,delFlag ")
				 .append("FROM cruiseship_cabin ")
				 .append("WHERE  cruiseship_info_uuid=? and delFlag=0 and wholesaler_id=?");
			list = cruiseshipCabinDao.findBySql(sbf.toString(),CruiseshipCabin.class,uuid,UserUtils.getUser().getCompany().getId());
		} catch (Exception e) {
			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN);
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_YOULUNKUCUN, message,e);
		}
		return list;
	}


	@Override
	public List<CruiseshipInfo> getCruiseshipInfoByName(String name) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT id,uuid,wholesaler_id ,name as name,memo,createBy,createDate,updateBy,updateDate,delFlag ")
			.append("from cruiseship_info where name=? and delFlag=0 and wholesaler_id=?");
		 List<CruiseshipInfo> list=cruiseshipInfoDao.findBySql(sbf.toString(), CruiseshipInfo.class,name,UserUtils.getUser().getCompany().getId());
		return list;
	}

	
	/**
	 * 根据批发商id获取该批发商下所有的游轮基础信息
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return List<CruiseshipInfo>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-2-23
	 */
	public List<CruiseshipInfo> findByWholesalerId(Long wholesalerId) {
		return cruiseshipInfoDao.findByWholesalerId(wholesalerId);
	}

}
