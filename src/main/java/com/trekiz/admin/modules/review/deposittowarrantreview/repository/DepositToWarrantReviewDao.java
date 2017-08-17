package com.trekiz.admin.modules.review.deposittowarrantreview.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewflow.entity.Review;

public interface DepositToWarrantReviewDao extends DepositToWarrantReviewDaoCustom,CrudRepository<Review, Long> {

}

interface DepositToWarrantReviewDaoCustom extends BaseDao<Review>{
	
}

@Repository
class DepositToWarrantReviewDaoImpl extends BaseDaoImpl<Review> implements DepositToWarrantReviewDaoCustom {

}