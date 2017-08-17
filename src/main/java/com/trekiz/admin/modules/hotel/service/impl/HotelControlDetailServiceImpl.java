/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service.impl;

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
import com.trekiz.admin.modules.hotel.dao.HotelControlDetailDao;
import com.trekiz.admin.modules.hotel.dao.HotelControlRoomDetailDao;
import com.trekiz.admin.modules.hotel.entity.HotelControlDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;
import com.trekiz.admin.modules.hotel.service.HotelControlDetailService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class HotelControlDetailServiceImpl  extends BaseService implements HotelControlDetailService{
	@Autowired
	private HotelControlDetailDao hotelControlDetailDao;
	@Autowired
	private HotelControlRoomDetailDao hotelControlRoomDetailDao;

	public void save (HotelControlDetail hotelControlDetail){
		super.setOptInfo(hotelControlDetail, BaseService.OPERATION_ADD);
		hotelControlDetailDao.saveObj(hotelControlDetail);
	}
	
	public void update (HotelControlDetail hotelControlDetail){
		super.setOptInfo(hotelControlDetail, BaseService.OPERATION_UPDATE);
		hotelControlDetailDao.updateObj(hotelControlDetail);
	}
	
	public HotelControlDetail getById(java.lang.Integer value) {
		return hotelControlDetailDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		HotelControlDetail obj = hotelControlDetailDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	
	
	public Page<HotelControlDetail> find(Page<HotelControlDetail> page, HotelControlDetail hotelControlDetail) {
		DetachedCriteria dc = hotelControlDetailDao.createDetachedCriteria();
		
	   	if(hotelControlDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelControlDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelControlDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getHotelControlUuid())){
			dc.add(Restrictions.like("hotelControlUuid", "%"+hotelControlDetail.getHotelControlUuid()+"%"));
		}
		if(hotelControlDetail.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelControlDetail.getInDate()));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getHotelMeal())){
			dc.add(Restrictions.like("hotelMeal", "%"+hotelControlDetail.getHotelMeal()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getIslandWay())){
			dc.add(Restrictions.like("islandWay", "%"+hotelControlDetail.getIslandWay()+"%"));
		}
	   	if(hotelControlDetail.getTotalPrice()!=null){
	   		dc.add(Restrictions.eq("totalPrice", hotelControlDetail.getTotalPrice()));
	   	}
	   	if(hotelControlDetail.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelControlDetail.getCurrencyId()));
	   	}
	   	if(hotelControlDetail.getStock()!=null){
	   		dc.add(Restrictions.eq("stock", hotelControlDetail.getStock()));
	   	}
	   	if(hotelControlDetail.getSellStock()!=null){
	   		dc.add(Restrictions.eq("sellStock", hotelControlDetail.getSellStock()));
	   	}
	   	if(hotelControlDetail.getPreStock()!=null){
	   		dc.add(Restrictions.eq("preStock", hotelControlDetail.getPreStock()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlDetail.getValidateFlag())){
			dc.add(Restrictions.like("validateFlag", "%"+hotelControlDetail.getValidateFlag()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getMemo())){
			dc.add(Restrictions.like("memo", "%"+hotelControlDetail.getMemo()+"%"));
		}
	   	if(hotelControlDetail.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", hotelControlDetail.getStatus()));
	   	}
	   	if(hotelControlDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelControlDetail.getWholesalerId()));
	   	}
	   	if(hotelControlDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelControlDetail.getCreateBy()));
	   	}
		if(hotelControlDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelControlDetail.getCreateDate()));
		}
	   	if(hotelControlDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelControlDetail.getUpdateBy()));
	   	}
		if(hotelControlDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelControlDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelControlDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelControlDetailDao.find(page, dc);
	}
	
	public List<HotelControlDetail> find( HotelControlDetail hotelControlDetail) {
		DetachedCriteria dc = hotelControlDetailDao.createDetachedCriteria();
		
	   	if(hotelControlDetail.getId()!=null){
	   		dc.add(Restrictions.eq("id", hotelControlDetail.getId()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlDetail.getUuid())){
			dc.add(Restrictions.like("uuid", "%"+hotelControlDetail.getUuid()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getHotelControlUuid())){
			dc.add(Restrictions.like("hotelControlUuid", "%"+hotelControlDetail.getHotelControlUuid()+"%"));
		}
		if(hotelControlDetail.getInDate()!=null){
			dc.add(Restrictions.eq("inDate", hotelControlDetail.getInDate()));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getHotelMeal())){
			dc.add(Restrictions.like("hotelMeal", "%"+hotelControlDetail.getHotelMeal()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getIslandWay())){
			dc.add(Restrictions.like("islandWay", "%"+hotelControlDetail.getIslandWay()+"%"));
		}
	   	if(hotelControlDetail.getTotalPrice()!=null){
	   		dc.add(Restrictions.eq("totalPrice", hotelControlDetail.getTotalPrice()));
	   	}
	   	if(hotelControlDetail.getCurrencyId()!=null){
	   		dc.add(Restrictions.eq("currencyId", hotelControlDetail.getCurrencyId()));
	   	}
	   	if(hotelControlDetail.getStock()!=null){
	   		dc.add(Restrictions.eq("stock", hotelControlDetail.getStock()));
	   	}
	   	if(hotelControlDetail.getSellStock()!=null){
	   		dc.add(Restrictions.eq("sellStock", hotelControlDetail.getSellStock()));
	   	}
	   	if(hotelControlDetail.getPreStock()!=null){
	   		dc.add(Restrictions.eq("preStock", hotelControlDetail.getPreStock()));
	   	}
		if (StringUtils.isNotEmpty(hotelControlDetail.getValidateFlag())){
			dc.add(Restrictions.like("validateFlag", "%"+hotelControlDetail.getValidateFlag()+"%"));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getMemo())){
			dc.add(Restrictions.like("memo", "%"+hotelControlDetail.getMemo()+"%"));
		}
	   	if(hotelControlDetail.getStatus()!=null){
	   		dc.add(Restrictions.eq("status", hotelControlDetail.getStatus()));
	   	}
	   	if(hotelControlDetail.getWholesalerId()!=null){
	   		dc.add(Restrictions.eq("wholesalerId", hotelControlDetail.getWholesalerId()));
	   	}
	   	if(hotelControlDetail.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", hotelControlDetail.getCreateBy()));
	   	}
		if(hotelControlDetail.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", hotelControlDetail.getCreateDate()));
		}
	   	if(hotelControlDetail.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", hotelControlDetail.getUpdateBy()));
	   	}
		if(hotelControlDetail.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", hotelControlDetail.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelControlDetail.getDelFlag())){
			dc.add(Restrictions.like("delFlag", "%"+hotelControlDetail.getDelFlag()+"%"));
		}
		
		//dc.addOrder(Order.desc("id"));
		return hotelControlDetailDao.find(dc);
	}
	
	public HotelControlDetail getByUuid(String uuid) {
		return hotelControlDetailDao.getByUuid(uuid);
	}
	//切换delFlag的值
	public void removeByUuid(String uuid) {
		HotelControlDetail hotelControlDetail = getByUuid(uuid);
		hotelControlDetail.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelControlDetail);
	}
	//切换status的状态值
	public void delStatusByUuid(String uuid) {
		HotelControlDetail hotelControlDetail = getByUuid(uuid);
		hotelControlDetail.setStatus(HotelControlDetail.STATUS_DEL_FLAG);//0：已提交；1：已保存草稿；2：已删除；
		update(hotelControlDetail);
	}
	
	public boolean findRuleIsExist(String uuid, String md5Code, Long companyId) {
		return this.hotelControlDetailDao.findRuleIsExist(uuid, md5Code, companyId);
	}

	/**
	 * 根据控房单明细信息生成规则字符串 add by zhanghao
	 * @param deatil
	 * @param roomList
	 * @return
	 */
	public String getHotelControlRule(HotelControlDetail deatil,List<HotelControlRoomDetail> roomList){
		return hotelControlDetailDao.getHotelControlRule(deatil, roomList);
	}
	public String getHotelControlRule(HotelControlDetail deatil){
		return getHotelControlRule(deatil, deatil.getRoomList());
	}
	public String getHotelControlRule(String detailUuid){
		HotelControlDetail deatil = hotelControlDetailDao.getByUuid(detailUuid);
		List<HotelControlRoomDetail> list = hotelControlRoomDetailDao.getListByDetailUuid(detailUuid);
		return getHotelControlRule(deatil, list);
	}
	
	/**
	 * 根据指定状态 修改 控房明细表 状态 add by zhanghao
	 * @param deatil
	 * @param status
	 */
	public int updateDetailStatus(String deatilUuid,int status){
		return hotelControlDetailDao.updateDetailStatus(deatilUuid, status);
	}
	
	/**
	 * 通过酒店控房表的id，hotelControlUuid 查询 酒店控房明细表信息
	 * @param hotelControlUuid
	 * @param delFlag
	 * @return
	 */
	public List<HotelControlDetail> getByHotelControlUuid(String hotelControlUuid){
		List<HotelControlDetail> list = this.hotelControlDetailDao.find("from HotelControlDetail where hotelControlUuid=? and delFlag=?", hotelControlUuid, BaseEntity.DEL_FLAG_NORMAL);
		return list;
	}
	
	/**
	 * 判断控房记录是否可以删除 如果有占位或者 已售就不可以删除
	 */
	
	@Override
	public int yonRemoveByUuid(String uuid) {
		
		int num =0 ;
		
		HotelControlDetail  hotelControlDetail = hotelControlDetailDao.getByUuid(uuid);
		
		if(hotelControlDetail.getSellStock()>0 || hotelControlDetail.getPreStock()>0){
			
			num++;
		}
		return num;
	}

	/**
	 * 批量删除控房数据
	 */
	@Override
	public void updateHotelControls(List<HotelControlDetail> listControlDetails) {
		
		for (int i = 0;i<=listControlDetails.size();i++) {
			HotelControlDetail	hotelControlDetail = hotelControlDetailDao.getByUuid(listControlDetails.get(i).toString());
			int num= this.yonRemoveByUuid(hotelControlDetail.getUuid());
			if(num==0){
				hotelControlDetail.setStatus(0);
				listControlDetails.add(hotelControlDetail);
			}
		}
		hotelControlDetailDao.updateHotelControls(listControlDetails);
	}
	
	
}
