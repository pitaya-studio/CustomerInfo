package com.trekiz.admin.modules.reviewflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.reviewflow.entity.ReviewNewExtend;

public interface ReviewNewExtendDao extends ReviewNewExtendDaoCustom, CrudRepository<ReviewNewExtend, Integer>{

	@Query("from ReviewNewExtend where reviewId = ?1 and delFlag = 0")
	public List<ReviewNewExtend> findByReviewId(String reviewId);

	@Modifying
	@Query("update ReviewNewExtend set isShowRemark = 0 where reviewId = ?1 and delFlag = 0")
	public void removeIsShowRemark(String reviewId);
}

interface ReviewNewExtendDaoCustom extends BaseDao<ReviewNewExtend> {
	
}

@Repository
class ReviewNewExtendDaoImpl extends BaseDaoImpl<ReviewNewExtend> implements ReviewNewExtendDaoCustom {
	
}
