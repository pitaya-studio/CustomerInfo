/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.dao.impl;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.entity.HotelRoom;
import com.trekiz.admin.modules.hotelQuotePreferential.dao.HotelQuotePreferentialRequireDao;
import com.trekiz.admin.modules.hotelQuotePreferential.entity.HotelQuotePreferentialRequire;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelQuotePreferentialRequireDaoImpl extends BaseDaoImpl<HotelQuotePreferentialRequire>  implements HotelQuotePreferentialRequireDao{
	@Override
	public HotelQuotePreferentialRequire getByUuid(String uuid) {
		Object entity = super.createQuery("from HotelQuotePreferentialRequire hotelQuotePreferentialRequire where hotelQuotePreferentialRequire.uuid=? and hotelQuotePreferentialRequire.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (HotelQuotePreferentialRequire)entity;
		}
		return null;
	}
	
	public HotelQuotePreferentialRequire findRequireByPreferentialUuid(String preferentialUuid) {
		Object entity = super.createQuery("from HotelQuotePreferentialRequire hotelQuotePreferentialRequire where hotelQuotePreferentialRequire.hotelQuotePreferentialUuid=? and hotelQuotePreferentialRequire.delFlag=?", preferentialUuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			HotelQuotePreferentialRequire require = (HotelQuotePreferentialRequire)entity;
			require.setNotApplicableRoomName("");
			if(StringUtils.isNotBlank(require.getNotApplicableRoom())){
				String[] narray = require.getNotApplicableRoom().split(";");
				StringBuffer sb = new StringBuffer("");
				for(String uuid:narray){
					if(StringUtils.isNotBlank(uuid)){
						List<HotelRoom> list = (List<HotelRoom>) super.findCustomObjBySql("select room_name from hotel_room where uuid=?", HotelRoom.class, uuid);
						if(CollectionUtils.isNotEmpty(list)){
							sb.append(list.get(0).getRoomName());
							sb.append(",");
							
						}
					}
				}
				if(sb.length()>0){
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				require.setNotApplicableRoomName(sb.toString());
			}
			
			return require;
		}
		return null;
	}
}
