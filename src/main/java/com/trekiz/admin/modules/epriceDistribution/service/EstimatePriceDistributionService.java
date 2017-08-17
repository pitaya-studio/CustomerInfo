/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.epriceDistribution.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.epriceDistribution.entity.EstimatePriceDistribution;
import com.trekiz.admin.modules.epriceDistribution.input.EstimatePriceDistributionInput;
import com.trekiz.admin.modules.epriceDistribution.query.EstimatePriceDistributionQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface EstimatePriceDistributionService{
	
	public void save (EstimatePriceDistribution estimatePriceDistribution);
	
	public void save (EstimatePriceDistributionInput estimatePriceDistributionInput);
	
	public void update (EstimatePriceDistribution estimatePriceDistribution);
	
	public EstimatePriceDistribution getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<EstimatePriceDistribution> find(Page<EstimatePriceDistribution> page, EstimatePriceDistributionQuery estimatePriceDistributionQuery);
	
	public List<EstimatePriceDistribution> find( EstimatePriceDistributionQuery estimatePriceDistributionQuery);
	
	public EstimatePriceDistribution getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
