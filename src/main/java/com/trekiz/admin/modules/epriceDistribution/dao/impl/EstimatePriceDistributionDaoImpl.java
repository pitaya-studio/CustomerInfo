/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.epriceDistribution.dao.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.epriceDistribution.dao.EstimatePriceDistributionDao;
import com.trekiz.admin.modules.epriceDistribution.entity.EstimatePriceDistribution;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Service
@Transactional(readOnly = true)
public class EstimatePriceDistributionDaoImpl extends BaseDaoImpl<EstimatePriceDistribution>  implements EstimatePriceDistributionDao{
	@Override
	public EstimatePriceDistribution getByUuid(String uuid) {
		Object entity = super.createQuery("from EstimatePriceDistribution estimatePriceDistribution where estimatePriceDistribution.uuid=? and estimatePriceDistribution.delFlag=?", uuid, BaseEntity.DEL_FLAG_NORMAL).uniqueResult();
		if(entity != null) {
			return (EstimatePriceDistribution)entity;
		}
		return null;
	}
	
}
