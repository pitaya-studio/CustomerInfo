/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cruiseship.dao.CruiseshipStockDetailDao;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockDetail;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockDetailInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockDetailQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockDetailService;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockLogService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class CruiseshipStockDetailServiceImpl  extends BaseService implements CruiseshipStockDetailService{
	@Autowired
	private CruiseshipStockDetailDao cruiseshipStockDetailDao;
	
	@Autowired
	private CruiseshipStockLogService cruiseshipStockLogService;

	public void save (CruiseshipStockDetail cruiseshipStockDetail){
		super.setOptInfo(cruiseshipStockDetail, BaseService.OPERATION_ADD);
		cruiseshipStockDetailDao.saveObj(cruiseshipStockDetail);
	}
	
	public void save (CruiseshipStockDetailInput cruiseshipStockDetailInput){
		CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailInput.getCruiseshipStockDetail();
		super.setOptInfo(cruiseshipStockDetail, BaseService.OPERATION_ADD);
		cruiseshipStockDetailDao.saveObj(cruiseshipStockDetail);
	}
	
	public void update (CruiseshipStockDetail cruiseshipStockDetail){
		super.setOptInfo(cruiseshipStockDetail, BaseService.OPERATION_UPDATE);
		cruiseshipStockDetailDao.updateObj(cruiseshipStockDetail);
	}
	
	public CruiseshipStockDetail getById(java.lang.Integer value) {
		return cruiseshipStockDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		CruiseshipStockDetail obj = cruiseshipStockDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	public CruiseshipStockDetail getByStockUuid(String stockUuid){
		
		return cruiseshipStockDetailDao.getByStockUuid(stockUuid);
	}
	
	public Page<CruiseshipStockDetail> find(Page<CruiseshipStockDetail> page, CruiseshipStockDetailQuery cruiseshipStockDetailQuery) {
		DetachedCriteria dc = cruiseshipStockDetailDao.createDetachedCriteria();
		
	   	if(cruiseshipStockDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getCruiseshipStockUuid())){
			dc.add(Restrictions.eq("cruiseshipStockUuid", cruiseshipStockDetailQuery.getCruiseshipStockUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getCruiseshipInfoUuid())){
			dc.add(Restrictions.eq("cruiseshipInfoUuid", cruiseshipStockDetailQuery.getCruiseshipInfoUuid()));
		}
		if(cruiseshipStockDetailQuery.getShipDate()!=null){
			dc.add(Restrictions.eq("shipDate", cruiseshipStockDetailQuery.getShipDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getCruiseshipCabinUuid())){
			dc.add(Restrictions.eq("cruiseshipCabinUuid", cruiseshipStockDetailQuery.getCruiseshipCabinUuid()));
		}
	   	if(cruiseshipStockDetailQuery.getStockAmount()!=null){
	   		dc.add(Restrictions.eq("stockAmount", cruiseshipStockDetailQuery.getStockAmount()));
	   	}
	   	if(cruiseshipStockDetailQuery.getFreePosition()!=null){
	   		dc.add(Restrictions.eq("freePosition", cruiseshipStockDetailQuery.getFreePosition()));
	   	}
	   	if(cruiseshipStockDetailQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockDetailQuery.getWholesalerId()));
	   	}
	   	if(cruiseshipStockDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockDetailQuery.getCreateBy()));
	   	}
		if(cruiseshipStockDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockDetailQuery.getCreateDate()));
		}
	   	if(cruiseshipStockDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockDetailQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockDetailDao.find(page, dc);
	}
	
	public List<CruiseshipStockDetail> find( CruiseshipStockDetailQuery cruiseshipStockDetailQuery) {
		DetachedCriteria dc = cruiseshipStockDetailDao.createDetachedCriteria();
		
	   	if(cruiseshipStockDetailQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", cruiseshipStockDetailQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", cruiseshipStockDetailQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getCruiseshipStockUuid())){
			dc.add(Restrictions.eq("cruiseshipStockUuid", cruiseshipStockDetailQuery.getCruiseshipStockUuid()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getCruiseshipInfoUuid())){
			dc.add(Restrictions.eq("cruiseshipInfoUuid", cruiseshipStockDetailQuery.getCruiseshipInfoUuid()));
		}
		if(cruiseshipStockDetailQuery.getShipDate()!=null){
			dc.add(Restrictions.eq("shipDate", cruiseshipStockDetailQuery.getShipDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getCruiseshipCabinUuid())){
			dc.add(Restrictions.eq("cruiseshipCabinUuid", cruiseshipStockDetailQuery.getCruiseshipCabinUuid()));
		}
	   	if(cruiseshipStockDetailQuery.getStockAmount()!=null){
	   		dc.add(Restrictions.eq("stockAmount", cruiseshipStockDetailQuery.getStockAmount()));
	   	}
	   	if(cruiseshipStockDetailQuery.getFreePosition()!=null){
	   		dc.add(Restrictions.eq("freePosition", cruiseshipStockDetailQuery.getFreePosition()));
	   	}
	   	if(cruiseshipStockDetailQuery.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", cruiseshipStockDetailQuery.getWholesalerId()));
	   	}
	   	if(cruiseshipStockDetailQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", cruiseshipStockDetailQuery.getCreateBy()));
	   	}
		if(cruiseshipStockDetailQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", cruiseshipStockDetailQuery.getCreateDate()));
		}
	   	if(cruiseshipStockDetailQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", cruiseshipStockDetailQuery.getUpdateBy()));
	   	}
		if(cruiseshipStockDetailQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", cruiseshipStockDetailQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(cruiseshipStockDetailQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", cruiseshipStockDetailQuery.getDelFlag()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return cruiseshipStockDetailDao.find(dc);
	}
	
	public CruiseshipStockDetail getByUuid(String uuid) {
		return cruiseshipStockDetailDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		CruiseshipStockDetail cruiseshipStockDetail = getByUuid(uuid);
		cruiseshipStockDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(cruiseshipStockDetail);
	}
	
	public boolean batchDelete(String[] uuids) {
		return cruiseshipStockDetailDao.batchDelete(uuids);
	}
    /**
     * 223-tgy:
     *  获得cruiseship_stock_detail表中的库存信息ByUUid和船期
     */
	@Override
	public List<Map<String, Object>>getShipStockDetailByUuidAndShipdate(String cruiseshipUUid,String shipdate) {
		return cruiseshipStockDetailDao.getShipStockDetailByUuidAndShipdate(cruiseshipUUid,shipdate);
	}
    /**
     * 223:tgy
     * 根据csd表的主键id查询船期信息
     */
	@Override
	public List<Map<String, Object>> doGetDetailsById(String keyId) {
		return cruiseshipStockDetailDao.doGetDetailsById(keyId);
	}

	@Override
	public CruiseshipStockDetail getByUuidAndCreateBy(String uuid) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT detail.* FROM cruiseship_stock_detail detail left join cruiseship_stock_order stock on stock.cruiseship_stock_detail_uuid=detail.id ");
		sbf.append("where stock.createBy=? and detail.uuid=? and detail.wholesaler_id=? and detail.delFlag=? ");
		List<CruiseshipStockDetail> list = cruiseshipStockDetailDao.findBySql(sbf.toString(), CruiseshipStockDetail.class ,UserUtils.getUser().getId().intValue(),uuid,UserUtils.getUser().getCompany().getId().intValue(),0);
		CruiseshipStockDetail cruiseshipStockDetail=new CruiseshipStockDetail();
		for(CruiseshipStockDetail cruiseship:list){
			cruiseshipStockDetail=cruiseship;
		}
		return cruiseshipStockDetail;
	}
   

	/**已废弃 已废弃 已废弃 已废弃
	 * 游轮库存、余位扣减接口 by chy 2016年2月3日10:28:04  已废弃
	 * uuid 库存明细UUID
	 * type 扣减类型 库存 1 余位 2
	 * operate 操作类型  1 增加 2 减少
	 * num 增加/减少的数量
	 */
//	@Override
//	public void stockNumManage(String uuid, String type, String operate,
//			String num) {
//		try{
//			//校验参数
//			CruiseshipStockDetail cruiseshipStockDetail = cruiseshipStockDetailDao.getByUuid(uuid);
//			if(cruiseshipStockDetail == null){
//				throw new RuntimeException("查询不到对应的库存明细信息");
//			}
//			if(type == null || "".equals(type) || (!"1".equals(type) && !"2".equals(type))){
//				throw new RuntimeException("错误的扣减类型");
//			}
//			if(operate == null || "".equals(operate) || (!"1".equals(operate) && !"2".equals(operate))){
//				throw new RuntimeException("错误的操作类型");
//			}
//			if (!NumberUtils.isNumber(num)) {
//				throw new RuntimeException("错误的扣减数量");
//			}
//			//扣减库存余位
//			//1 取出所需数据
//			//原始库存
//			Integer stockAmount = cruiseshipStockDetail.getStockAmount();
//			//原始余位
//			Integer freePosition = cruiseshipStockDetail.getFreePosition();
//			//改后原始库存初始化
//			Integer newStockAmount = stockAmount;
//			//改后原始余位初始化
//			Integer newFreePosition = freePosition;
//			if("1".equals(type)){
//				if("1".equals(operate)){
//					newStockAmount += Integer.parseInt(num);
//				} else {
//					newStockAmount -= Integer.parseInt(num);
//				}
//			} else {
//				if("1".equals(operate)){
//					newFreePosition += Integer.parseInt(num);
//				} else {
//					newFreePosition -= Integer.parseInt(num);
//				}
//			}
//			cruiseshipStockDetail.setStockAmount(newStockAmount);
//			cruiseshipStockDetail.setFreePosition(newFreePosition);
//			cruiseshipStockDetail.setUpdateBy(UserUtils.getUser().getId().intValue());
//			cruiseshipStockDetail.setUpdateDate(new java.util.Date());
//			cruiseshipStockDetailDao.saveObj(cruiseshipStockDetail);
//			// 保存操作日志
//			CruiseshipStockLog cruiseshipStockLog = new CruiseshipStockLog();
//			cruiseshipStockLog.setUuid(UuidUtils.generUuid());//UUID
//			cruiseshipStockLog.setCruiseshipStockUuid(cruiseshipStockDetail.getCruiseshipStockUuid());//库存UUID
//			cruiseshipStockLog.setCruiseshipInfoUuid(cruiseshipStockDetail.getCruiseshipInfoUuid());//游轮UUID
//			cruiseshipStockLog.setShipDate(cruiseshipStockDetail.getShipDate());//船期
//			cruiseshipStockLog.setCruiseshipCabinUuid(cruiseshipStockDetail.getCruiseshipCabinUuid());//舱型
//			cruiseshipStockLog.setStockAmount(stockAmount);//修改前库存
//			cruiseshipStockLog.setFreePosition(freePosition);//修改前余位
//			cruiseshipStockLog.setWholesalerId(cruiseshipStockDetail.getWholesalerId());//批发商id
//			cruiseshipStockLog.setOperateSource(Integer.parseInt(type));//操作源 1：库存；2：余位；
//			cruiseshipStockLog.setOperateType(Integer.parseInt(operate));//操作类型1：增加；2：减少；
//			cruiseshipStockLog.setOperateNum(Integer.parseInt(num));//操作数量
//			cruiseshipStockLog.setStockAmountAfter(newStockAmount);//修改后库存
//			cruiseshipStockLog.setFreePositionAfter(newFreePosition);//修改后余位
//			cruiseshipStockLog.setCreateBy(UserUtils.getUser().getId().intValue());//创建人
//			cruiseshipStockLog.setCreateDate(new java.util.Date());//创建时间
//			cruiseshipStockLog.setUpdateBy(UserUtils.getUser().getId().intValue());//更新人
//			cruiseshipStockLog.setUpdateDate(new java.util.Date());//更新时间
//			cruiseshipStockLog.setDelFlag("0");//删除标记0 正常
//			cruiseshipStockLogService.save(cruiseshipStockLog);
//		} catch (Exception e) {
//			//记录错误日志 
////			String message = LogMessageUtil.getInstance().getLogMessage(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_);
////			logger.error(message);
//			//并抛出异常
////			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_YOULUN, ExceptionConstants.MODULE_BAOMING, message);
//		}
//		
//	}
	
}
