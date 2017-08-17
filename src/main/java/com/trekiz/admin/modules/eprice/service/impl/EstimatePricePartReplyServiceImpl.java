package com.trekiz.admin.modules.eprice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.eprice.entity.EstimatePricePartReply;
import com.trekiz.admin.modules.eprice.repository.EstimatePricePartReplyDao;
import com.trekiz.admin.modules.eprice.service.EstimatePricePartReplyService;
/**
 * 多币种、整体报价、细分报价
 * @author gao
 *  2015年5月11日
 */
@Service("estimatePricePartReplyService")
@Transactional(readOnly = true)
public class EstimatePricePartReplyServiceImpl extends BaseService  implements EstimatePricePartReplyService {
	@Autowired
	private EstimatePricePartReplyDao estimatePricePartReplyDao;

	@Override
	public EstimatePricePartReply save(EstimatePricePartReply reply) {
		EstimatePricePartReply rep = estimatePricePartReplyDao.save(reply);
		return rep;
	}
	
	
}
