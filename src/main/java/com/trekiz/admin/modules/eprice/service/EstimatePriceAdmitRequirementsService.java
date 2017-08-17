package com.trekiz.admin.modules.eprice.service;

import java.util.List;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceAdmitRequirements;



/**
 * 接待社询价内容service业务接口
 * @author lihua.xu
 * @时间 2014年9月17日
 *
 */
public interface EstimatePriceAdmitRequirementsService {
	
	/**
	 * 接待社询价内容存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epar 要存储的 接待社询价内容对象 
	 */
	public void save(EstimatePriceAdmitRequirements epar);
	
	/**
	 * 通过id查询接待社询价内容对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 接待社询价内容的id
	 * @return EstimatePriceAdmitRequirements
	 */
	public EstimatePriceAdmitRequirements findById(Long id);
	
	
	/**
	 * 通过id逻辑删除接待社询价内容对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 接待社询价内容的id
	 */
	public void delById(Long id);

	
	/**
	 * 接待社询价内容更新的基础接口方法
	 * @author yue.wang
	 * @时间 2014年12月03日
	 * @param epar 要修改的 接待社询价内容对象 
	 */
	public void update(EstimatePriceAdmitRequirements epar);
	/**
	 * 通过projectid查询有关线路国家信息
	 * @param pid
	 * @return
	 */
	public List<EstimatePriceAdmitRequirements> findByPid(Long pid);

}
