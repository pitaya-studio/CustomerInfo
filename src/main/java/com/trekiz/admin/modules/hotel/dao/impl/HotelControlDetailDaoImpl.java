/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.hotel.dao.HotelControlDetailDao;
import com.trekiz.admin.modules.hotel.entity.HotelControlDetail;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;
import com.trekiz.admin.modules.hotel.model.HotelControlDetailModel;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelControlDetailDaoImpl extends BaseDaoImpl<HotelControlDetail>  implements HotelControlDetailDao{
	@Override
	public HotelControlDetail getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelControlDetail hotelControlDetail where hotelControlDetail.uuid=? and hotelControlDetail.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelControlDetail)entity;
		}
		return null;
	}

	/**
	 * 批量更新控房记录状态
	 */
	public void updateHotelControls(List<HotelControlDetail> hotelControlDetails) {
		
		if(hotelControlDetails != null && !hotelControlDetails.isEmpty()) {
			for(HotelControlDetail hotelControlDetail : hotelControlDetails) {
				getSession().update(hotelControlDetail);
			}
			getSession().flush();
		}
	}

	@Override
	public List<HotelControlDetail> getByhotelControlUuid(
			String hotelControlUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HotelControlDetail> getByHotelControlUuid(
			String hotelControlUuid, Integer delFlag) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean findRuleIsExist(String uuid, String md5Code, Long companyId) {
		StringBuffer sb = new StringBuffer("from HotelControlDetail hotelControlDetail where hotelControlDetail.uuid != ? and hotelControlDetail.validateFlag = ? and hotelControlDetail.wholesalerId = ? and hotelControlDetail.delFlag = " + BaseEntity.DEL_FLAG_NORMAL);
		List<HotelControlDetail> hotelControlDetails = this.find(sb.toString(), uuid, md5Code, companyId.intValue());
		if(hotelControlDetails == null || hotelControlDetails.size() == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据指定状态 修改 控房明细表 状态 add by zhanghao
	 * @param deatil
	 * @param status
	 */
	public int updateDetailStatus(String deatilUuid,int status){
		org.hibernate.Query query = super.createQuery("update HotelControlDetail hotelControlDetail set hotelControlDetail.status=? where hotelControlDetail.uuid=?", status,deatilUuid );
		int result = query.executeUpdate();
		return result;
	}
	/**
	 * 根据控房单明细信息生成规则字符串 add by zhanghao
	 * @param deatil
	 * @param roomList
	 * @return
	 */
	public String getHotelControlRule(HotelControlDetail deatil,List<HotelControlRoomDetail> roomList){
		if(deatil==null || CollectionUtils.isEmpty(roomList)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(DateUtils.date2String(deatil.getInDate(),DateUtils.DATE_PATTERN_YYYYMMDD));
		sb.append("+");
		for(HotelControlRoomDetail room:roomList){
			sb.append(room.getUuid());
			sb.append("*");
			sb.append(room.getNight().toString());
			sb.append("+");
		}
		sb.deleteCharAt(sb.lastIndexOf("+"));
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<HotelControlDetailModel> getControlDetailsByHotelUuid(String hotelUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT detail.hotel_control_uuid    AS hotelControlUuid, ");
		sb.append("detail.uuid                  AS hotelControlDetailUuid, ");
		sb.append("detail.in_date               AS inDate, ");
		sb.append("detail.hotel_meal            AS hotelMeal, ");
		sb.append("hotelMeal.meal_name          AS hotelMealName, ");
		sb.append("detail.island_way            AS islandWay,");
		sb.append("dictView.label               AS islandWayName,");
		sb.append("detail.stock                 AS stock,");
		sb.append("detail.sell_stock            AS sellStock,");
		sb.append("detail.pre_stock             AS preStock,");
		sb.append("hotelControl.ground_supplier AS groundSupplier,");
		sb.append("hotelControl.purchase_type   AS purchaseType, ");
		sb.append("islandOrder.uuid			    AS islandOrderControlDetailUuid, ");
		sb.append("islandOrder.num			    AS islandOrderControlDetailNumber ");
		sb.append("FROM   hotel_control_detail detail ");
		sb.append("LEFT JOIN hotel_control hotelControl ");
		sb.append("ON detail.hotel_control_uuid = hotelControl.uuid ");
		sb.append("LEFT JOIN hotel_meal hotelMeal ");
		sb.append("ON detail.hotel_meal = hotelMeal.uuid ");
		sb.append("LEFT JOIN sys_company_dict_view dictView ");
		sb.append("ON detail.island_way = dictView.uuid ");
		sb.append("LEFT JOIN island_order_controlDetail islandOrder ON detail.uuid = islandOrder.hotel_control_detail_uuid ");
		sb.append("WHERE detail.delFlag = 0 and hotelControl.hotel_uuid = ? and hotelControl.delFlag=?");
		
		return (List<HotelControlDetailModel>) super.findCustomObjBySql(sb.toString(), HotelControlDetailModel.class, hotelUuid, BaseEntity.DEL_FLAG_NORMAL);
	}
	
}
