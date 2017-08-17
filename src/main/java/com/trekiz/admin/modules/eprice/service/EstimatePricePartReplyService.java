package com.trekiz.admin.modules.eprice.service;

import com.trekiz.admin.modules.eprice.entity.EstimatePricePartReply;
/**
 * 多币种、整体报价、细分报价
 * @author gao
 *  2015年5月11日
 */
public interface EstimatePricePartReplyService {

	/**
	 * 保存 多币种报价
	 * @author gao
	 * @param reply
	 * @return
	 */
	public EstimatePricePartReply save(EstimatePricePartReply reply);
	
	
}
