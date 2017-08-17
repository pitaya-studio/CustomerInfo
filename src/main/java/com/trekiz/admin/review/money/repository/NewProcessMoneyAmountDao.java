package com.trekiz.admin.review.money.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;

/**
 * 
 * @interfaceName: NewProcessMoneyAmountDao
 * @Description: TODO(自定义接口)
 * @author songyang
 * @date 2015年11月6日12:04:35
 *
 */
public interface NewProcessMoneyAmountDao extends NewProcessMoneyAmountDaoCustom,CrudRepository<NewProcessMoneyAmount, Long>{
	
	@Query("from NewProcessMoneyAmount where reviewId = ?1")
	public NewProcessMoneyAmount findByReviewId(String reviewId);
	
	@Query("from NewProcessMoneyAmount where reviewId = ?1")
	public List<NewProcessMoneyAmount> findListByReviewId(String reviewId);
	
	@Query("from NewProcessMoneyAmount where serialNum = ?1 order by currencyId")
	public List<NewProcessMoneyAmount> findAmountBySerialNum(String uuid);

}



/**
 * 
 * @interfaceName: NewProcessMoneyAmountDaoCustom
 * @Description: TODO(自定义接口)
 * @author songyang
 * @date 2015年11月6日12:04:17
 *
 */

interface NewProcessMoneyAmountDaoCustom extends BaseDao<NewProcessMoneyAmount>{
}


/**
 * 
 * @ClassName: NewProcessMoneyAmountDaoImpl
 * @Description: TODO(自定义接口实现类)
 * @author songyang
 * @date  2015年11月6日12:03:19
 *
 */
@Repository
class NewProcessMoneyAmountDaoImpl extends BaseDaoImpl<NewProcessMoneyAmount> implements NewProcessMoneyAmountDaoCustom{
	
	
} 