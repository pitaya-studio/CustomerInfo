/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.hotel.dao.SysGuestTypeDao;
import com.trekiz.admin.modules.hotel.entity.SysGuestType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class SysGuestTypeDaoImpl extends BaseDaoImpl<SysGuestType>  implements SysGuestTypeDao{
	@Override
	public SysGuestType getByUuid(String uuid) {
		Object entity = super.createQuery("from SysGuestType sysGuestType where sysGuestType.uuid=? and sysGuestType.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (SysGuestType)entity;
		}
		return null;
	}
	
	/**
	 * 查询批发商下所有的住客类型集合 add by zhanghao
	 * 自动报价模块使用
	 * @param wholesalerId
	 * @return List<SysGuestType> 集合对象中的uuid是hotel_guest_type表中的uuid，其余属性来源于sys_guest_type表
	 * 			
	 */
	@SuppressWarnings("unchecked")
	public List<SysGuestType> findAllListByCompanyIdAndHotelUuid(int wholesalerId ,String hotelUuid){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT sg.uuid, hgr.hotel_room_uuid as hotelRoomUuid, sg.name, sg.value, sg.type, sg.person_type, sg.description,hg.uuid as hotelGuestTypeUuid FROM hotel_guest_type_relation hgr ");
		sb.append("LEFT JOIN hotel_guest_type hg ON hgr.hotel_guest_type_uuid=hg.uuid ");
		sb.append("LEFT JOIN sys_guest_type sg ON hg.sys_guest_type=sg.uuid ");
		sb.append("WHERE hgr.delFlag=? AND hg.delFlag=? AND hg.wholesaler_id=? AND hgr.hotel_uuid=? order BY hg.sort ");
		List<? extends Serializable> list = super.findCustomObjBySql(sb.toString(), SysGuestType.class,  BaseEntity.DEL_FLAG_NORMAL,  BaseEntity.DEL_FLAG_NORMAL ,wholesalerId,hotelUuid);

		return (List<SysGuestType>) list;
	}
}
