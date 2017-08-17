package com.trekiz.admin.modules.eprice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.eprice.entity.EstimatePricePartReply;
/**
 * 多币种、整体报价、细分报价
 * @author gao
 *  2015年5月8日
 */
public interface EstimatePricePartReplyDao extends EstimatePricePartReplyDaoCustom, CrudRepository<EstimatePricePartReply,Long>{

	
}
interface EstimatePricePartReplyDaoCustom extends BaseDao<EstimatePricePartReply>{
	
}
@Repository
class EstimatePricePartReplyDaoImpl extends BaseDaoImpl<EstimatePricePartReply> implements EstimatePricePartReplyDaoCustom{

	
	
}