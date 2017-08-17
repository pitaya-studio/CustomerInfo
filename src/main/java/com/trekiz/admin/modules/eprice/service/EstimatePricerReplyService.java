package com.trekiz.admin.modules.eprice.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceProject;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.eprice.entity.EstimatePricerReply;

/**
 * 询价回复service业务接口
 * @author lihua.xu
 * @时间 2014年9月17日
 *
 */
public interface EstimatePricerReplyService {

	/**
	 * 询价回复存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epr
	 */
	public void save(EstimatePricerReply epr);
	
	/**
	 * 通过id获取询价回复对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价回复id
	 * @return EstimatePricerReply
	 */
	public EstimatePricerReply findById(Long id);
	
	/**
	 * 通过id逻辑删除询价回复对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价回复id
	 */
	public void delById(Long id);
	
	/**
	 * 发送询价给各个计调
	 * @author lihua.xu
	 * @时间 2014年9月24日
	 * @param epp 询价项目
	 * @param epr 询价记录
	 */
	public void send(EstimatePriceProject epp, EstimatePriceRecord epr, JSONArray jaa, JSONArray jat, boolean getByEntity);
	
	/**
	 * 根据询价记录id和计调用户id获取对于的询价回复数据对象
	 * @author lihua.xu
	 * @时间 2014年9月24日
	 * @param rid 询价记录id
	 * @param operatorUserId 计调用户id
	 * @return EstimatePricerReply
	 */
	public EstimatePricerReply findReplyByRidAndOperatorUserId(Long rid,Long operatorUserId,String types);
	
	/**
	 * 根据询价记录id和询价回复类型获取对于的询价回复数据对象
	 * @author lihua.xu
	 * @时间 2014年9月29日
	 * @param rid
	 * @param status
	 * @return
	 */
	public List<EstimatePricerReply> findReply(Long rid,String types);
	
	/**
	 * 根据询价记录id 地接询价记录
	 * @author lihua.xu
	 * @时间 2014年9月29日
	 * @param rid
	 * @param status
	 * @return
	 */
	public List<EstimatePricerReply> findReplyByAdmit(Long rid);
	
	
}
