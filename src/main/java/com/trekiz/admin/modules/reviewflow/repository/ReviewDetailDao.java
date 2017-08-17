package com.trekiz.admin.modules.reviewflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;

interface ReviewDetailDaoCustom extends BaseDao<ReviewDetail> {
}

public interface ReviewDetailDao extends ReviewDetailDaoCustom,
		CrudRepository<ReviewDetail, Long> {

	@Query("from ReviewDetail where reviewId = ?1")
	public List<ReviewDetail> findReviewDetail(Long reviewId);

	@Modifying
	@Query("update ReviewDetail set value = '1' where reviewId = ?1 and key = ?2")
	public void updateReviewDetail(Long reviewId, String cashConfirm);
	
	@Query("from ReviewDetail where reviewId = ?1  and mykey = ?2")
	public List<ReviewDetail> findReviewDetailByMykey(Long reviewId,String mykey);
	@Query("from ReviewDetail where reviewId = ?1")
	public List<ReviewDetail> getReviewDetail(Long id);
}

class ReviewDetailDaoImpl extends BaseDaoImpl<ReviewDetail> implements
		ReviewDetailDaoCustom {

}