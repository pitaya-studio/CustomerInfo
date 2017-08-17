package com.trekiz.admin.modules.eprice.service;

import java.util.List;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecordReply;

/**
 * 用于计调列表页面
 * 用于询价表和计调回复列表的联合查询
 * @author gao
 *
 */
public interface EstimatePriceRecordReplyService {

	/**
	 * 通过当前登录的计调用户ID和询价记录ID，进行查询
	 * @param operatorUserId
	 * @param recordId
	 * @return
	 */
	public List<EstimatePriceRecordReply> findByOpeId(Long operatorUserId,Long recordId);
	
	/**
	 * 通过当前登录的计调用户ID和询价记录ID，进行查询(机票计调专用)
	 * @param operatorUserId
	 * @param recordId
	 * @return
	 */
	public List<EstimatePriceRecordReply> findTrafficByOpeId(Long operatorUserId,Long recordId);
	/**
	 * 根据询价记录ID，查询出发城市和到达城市
	 * @param repId
	 * @return
	 */
	public String findStartToEnd(Long recId);
}
