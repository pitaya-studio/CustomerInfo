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
import com.trekiz.admin.modules.hotel.dao.HotelTravelerTypeRelationDao;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerTypeRelation;
import com.trekiz.admin.modules.hotel.input.HotelTravelerTypeRelationInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelerTypeRelationQuery;
import com.trekiz.admin.modules.hotel.service.HotelTravelerTypeRelationService;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class HotelTravelerTypeRelationServiceImpl extends BaseService implements
		HotelTravelerTypeRelationService {
	@Autowired
	private HotelTravelerTypeRelationDao hotelTravelerTypeRelationDao;

	public void save(HotelTravelerTypeRelation hotelTravelerTypeRelation) {
		super.setOptInfo(hotelTravelerTypeRelation, BaseService.OPERATION_ADD);
		hotelTravelerTypeRelationDao.saveObj(hotelTravelerTypeRelation);
	}

	public void save(
			HotelTravelerTypeRelationInput hotelTravelerTypeRelationInput) {
		HotelTravelerTypeRelation hotelTravelerTypeRelation = hotelTravelerTypeRelationInput
				.getHotelTravelerTypeRelation();
		super.setOptInfo(hotelTravelerTypeRelation, BaseService.OPERATION_ADD);
		hotelTravelerTypeRelationDao.saveObj(hotelTravelerTypeRelation);
	}

	public void update(HotelTravelerTypeRelation hotelTravelerTypeRelation) {
		super.setOptInfo(hotelTravelerTypeRelation,
				BaseService.OPERATION_UPDATE);
		hotelTravelerTypeRelationDao.updateObj(hotelTravelerTypeRelation);
	}

	public HotelTravelerTypeRelation getById(java.lang.Integer value) {
		return hotelTravelerTypeRelationDao.getById(value);
	}

	public void removeById(java.lang.Integer value) {
		HotelTravelerTypeRelation obj = hotelTravelerTypeRelationDao
				.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}

	public Page<HotelTravelerTypeRelation> find(
			Page<HotelTravelerTypeRelation> page,
			HotelTravelerTypeRelationQuery hotelTravelerTypeRelationQuery) {
		DetachedCriteria dc = hotelTravelerTypeRelationDao
				.createDetachedCriteria();

		if (hotelTravelerTypeRelationQuery.getId() != null) {
			dc.add(Restrictions.eq("id", hotelTravelerTypeRelationQuery.getId()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery.getUuid())) {
			dc.add(Restrictions.eq("uuid",
					hotelTravelerTypeRelationQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery
				.getTravelerTypeUuid())) {
			dc.add(Restrictions.eq("travelerTypeUuid",
					hotelTravelerTypeRelationQuery.getTravelerTypeUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery
				.getTravelerTypeName())) {
			dc.add(Restrictions.eq("travelerTypeName",
					hotelTravelerTypeRelationQuery.getTravelerTypeName()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery
				.getHotelUuid())) {
			dc.add(Restrictions.eq("hotelUuid",
					hotelTravelerTypeRelationQuery.getHotelUuid()));
		}
		if (hotelTravelerTypeRelationQuery.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy",
					hotelTravelerTypeRelationQuery.getCreateBy()));
		}
		if (hotelTravelerTypeRelationQuery.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate",
					hotelTravelerTypeRelationQuery.getCreateDate()));
		}
		if (hotelTravelerTypeRelationQuery.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy",
					hotelTravelerTypeRelationQuery.getUpdateBy()));
		}
		if (hotelTravelerTypeRelationQuery.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate",
					hotelTravelerTypeRelationQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag",
					hotelTravelerTypeRelationQuery.getDelFlag()));
		}

		// dc.addOrder(Order.desc("id"));
		return hotelTravelerTypeRelationDao.find(page, dc);
	}

	public List<HotelTravelerTypeRelation> find(
			HotelTravelerTypeRelationQuery hotelTravelerTypeRelationQuery) {
		DetachedCriteria dc = hotelTravelerTypeRelationDao
				.createDetachedCriteria();

		if (hotelTravelerTypeRelationQuery.getId() != null) {
			dc.add(Restrictions.eq("id", hotelTravelerTypeRelationQuery.getId()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery.getUuid())) {
			dc.add(Restrictions.eq("uuid",
					hotelTravelerTypeRelationQuery.getUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery
				.getTravelerTypeUuid())) {
			dc.add(Restrictions.eq("travelerTypeUuid",
					hotelTravelerTypeRelationQuery.getTravelerTypeUuid()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery
				.getTravelerTypeName())) {
			dc.add(Restrictions.eq("travelerTypeName",
					hotelTravelerTypeRelationQuery.getTravelerTypeName()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery
				.getHotelUuid())) {
			dc.add(Restrictions.eq("hotelUuid",
					hotelTravelerTypeRelationQuery.getHotelUuid()));
		}
		if (hotelTravelerTypeRelationQuery.getCreateBy() != null) {
			dc.add(Restrictions.eq("createBy",
					hotelTravelerTypeRelationQuery.getCreateBy()));
		}
		if (hotelTravelerTypeRelationQuery.getCreateDate() != null) {
			dc.add(Restrictions.eq("createDate",
					hotelTravelerTypeRelationQuery.getCreateDate()));
		}
		if (hotelTravelerTypeRelationQuery.getUpdateBy() != null) {
			dc.add(Restrictions.eq("updateBy",
					hotelTravelerTypeRelationQuery.getUpdateBy()));
		}
		if (hotelTravelerTypeRelationQuery.getUpdateDate() != null) {
			dc.add(Restrictions.eq("updateDate",
					hotelTravelerTypeRelationQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(hotelTravelerTypeRelationQuery.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag",
					hotelTravelerTypeRelationQuery.getDelFlag()));
		}

		// dc.addOrder(Order.desc("id"));
		return hotelTravelerTypeRelationDao.find(dc);
	}

	public HotelTravelerTypeRelation getByUuid(String uuid) {
		return hotelTravelerTypeRelationDao.getByUuid(uuid);
	}

	public void removeByUuid(String uuid) {
		HotelTravelerTypeRelation hotelTravelerTypeRelation = getByUuid(uuid);
		hotelTravelerTypeRelation.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(hotelTravelerTypeRelation);
	}

	@Override
	public void removeByHotelUuid(String hotelUuid) {
		hotelTravelerTypeRelationDao.deleteByHotelUuid(hotelUuid);
	}
	
	/**
	 * 根据酒店uuid获取游客类型
	*<p>Title: getTravelerTypesByHotelUuid</p>
	* @return List<TravelerType> 返回类型
	* @author majiancheng
	* @date 2015-8-17 上午10:09:35
	* @throws
	 */
	public List<TravelerType> getTravelerTypesByHotelUuid(String hotelUuid){
		return hotelTravelerTypeRelationDao.getTravelerTypesByHotelUuid(hotelUuid);
	}
	
}
