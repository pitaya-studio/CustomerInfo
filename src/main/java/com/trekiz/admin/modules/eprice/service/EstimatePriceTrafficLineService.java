package com.trekiz.admin.modules.eprice.service;

import java.util.List;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceTrafficLine;

/**
* 询价交通线路记录service业务接口
* @author lihua.xu
* @时间 2014年9月17日
*
*/
public interface EstimatePriceTrafficLineService {

	/**
	 * 询价交通线路记录存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param eptl
	 */
	public void save(EstimatePriceTrafficLine eptl);
	
	/**
	 * 询价交通线路记录存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param eptl
	 */
	public void save(List<EstimatePriceTrafficLine> eptls);
	
	/**
	 * 通过id获取询价交通线路记录
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价交通线路记录id
	 * @return EstimatePriceTrafficLine
	 */
	public EstimatePriceTrafficLine findById(Long id);
	
	/**
	 * 通过id逻辑删除询价交通线路记录对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价交通线路记录id
	 */
	public void delById(Long id);
	
	/**
	 * 通过机票询价内容id获取有效的交通线路列表
	 * @author lihua.xu
	 * @时间 2014年10月8日
	 * @param pfid 机票询价内容id
	 * @return List<EstimatePriceTrafficLine>
	 */
	public List<EstimatePriceTrafficLine> findByPfid(Long pfid);
}
