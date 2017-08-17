package com.trekiz.admin.modules.eprice.service;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficRequirements;

/**
 * 交通询价内容service业务接口
 * @author lihua.xu
 * @时间 2014年9月17日
 *
 */
public interface EstimatePriceTrafficRequirementsService {

	/**
	 * 交通询价内容存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epp
	 */
	public void save(EstimatePriceTrafficRequirements eptr);
	
	/**
	 * 通过id获取交通询价内容对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价项目id
	 * @return EstimatePriceTrafficRequirements
	 */
	public EstimatePriceTrafficRequirements findById(Long id);
	
	/**
	 * 通过id逻辑删除交通询价内容对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价项目id
	 */
	public void delById(Long id);
}
