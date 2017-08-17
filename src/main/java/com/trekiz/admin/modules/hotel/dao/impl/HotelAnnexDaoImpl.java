/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.hotel.dao.HotelAnnexDao;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelAnnexDaoImpl extends BaseDaoImpl<HotelAnnex>  implements HotelAnnexDao{
	@Autowired
	private DocInfoDao docInfoDao;
	
	public void delByMainUuid(String uuid){
//		String sql = "UPDATE hotel_annex SET delFlag = '1' WHERE main_uuid = ?";
//		String sql = "DELETE hotel_contact  WHERE hotel_uuid = ?";
//		int result = super.updateBySql(sql, uuid);
		
	}
	
	@SuppressWarnings("unchecked")
	public void synDocInfo(String mainUuid ,int type,int wholesalerId, List<HotelAnnex> axList){
		List<HotelAnnex> axoldlist = super.createQuery("from HotelAnnex hotelAnnex where hotelAnnex.mainUuid=? and hotelAnnex.type=? and hotelAnnex.delFlag='0'", mainUuid,type).list();
		
		if(CollectionUtils.isNotEmpty(axList)){
			for(HotelAnnex ha:axList){
				if(!tranferList2Map(axoldlist).containsKey(ha.getDocId().toString())){//新的数据不在数据库中 是 新增
					ha.setCreateBy(Integer.parseInt(UserUtils.getUser().getId().toString()));
					ha.setCreateDate(new Date());
					ha.setUpdateBy(Integer.parseInt(UserUtils.getUser().getId().toString()));
					ha.setUpdateDate(new Date());
					ha.setDelFlag("0");
					ha.setUuid(UuidUtils.generUuid());
					ha.setMainUuid(mainUuid);
					ha.setType(type);
					ha.setWholesalerId(wholesalerId);
					super.saveObj(ha);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(axoldlist)){
			for(HotelAnnex ha:axoldlist){
				if(!tranferList2Map(axList).containsKey(ha.getDocId().toString())){//数据库中的数据不在新的数据中 是删除
					ha.setDelFlag("1");
					super.updateObj(ha);
					docInfoDao.delDocInfoById(Long.parseLong(ha.getDocId().toString()));
				}
			}
		}
		
	}
	private Map<String,HotelAnnex> tranferList2Map(List<HotelAnnex> list){
		Map<String,HotelAnnex> map = new HashMap<String,HotelAnnex>();
		if(list!=null){
			for(HotelAnnex ha:list){
				map.put(ha.getDocId().toString(), ha);
			}
		}
		return map;
	}
	
	public void delByUuid(String uuid){
		String sql = "UPDATE hotel_annex SET delFlag = '1' WHERE uuid = ?";
		super.updateBySql(sql, uuid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HotelAnnex> getByMainUuid(String uuid) {
		
		List<HotelAnnex> axoldlist = super.createQuery("from HotelAnnex hotelAnnex where hotelAnnex.mainUuid=? and hotelAnnex.delFlag='0'", uuid).list();
		if(axoldlist!=null){
			return axoldlist;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HotelAnnex> getAnnexListByMainUuid(String uuid) {
		List<HotelAnnex> annexList=super.createQuery("from HotelAnnex hotelAnnex where hotelAnnex.mainUuid=? and hotelAnnex.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).list();
		return annexList;
	}
	
}
