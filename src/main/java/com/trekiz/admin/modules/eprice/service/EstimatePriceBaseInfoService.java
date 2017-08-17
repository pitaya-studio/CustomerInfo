package com.trekiz.admin.modules.eprice.service;

import java.util.List;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceBaseInfo;

/**
 * 询价基本信息service业务接口
 * @author lihua.xu
 * @时间 2014年9月17日
 *
 */
public interface EstimatePriceBaseInfoService {

	/**
	 * 询价基本信息存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epbi
	 * @return 
	 */
	public EstimatePriceBaseInfo save(EstimatePriceBaseInfo epbi);
	
	/**
	 * 询价基本信息更新的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epbi
	 */
	public void update(EstimatePriceBaseInfo epbi);
	
	/**
	 * 通过id查询询价基本信息对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epbi
	 * @return EstimatePriceBaseInfo
	 */
	public EstimatePriceBaseInfo findById(Long id);
	
	
	/**
	 * 通过id逻辑删除询价基本信息数据
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epbi
	 */
	public void delById(Long id);
	
	/**
	 * 通过projectId查询baseinfo的信息
	 * @param pid
	 * @return
	 */
	public List<EstimatePriceBaseInfo> findByPid(Long pid);
}
