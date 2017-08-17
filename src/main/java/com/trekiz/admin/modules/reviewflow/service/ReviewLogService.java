package com.trekiz.admin.modules.reviewflow.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.entity.ReviewNewExtend;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewNewExtendDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
@Service
@Transactional(readOnly = true)
public class ReviewLogService extends BaseService {

	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
	private ReviewNewExtendDao reviewNewExtendDao;
	/**
	 * 保存审核日志对象
	 * @param log
	 */
	public void saveReviewLog(ReviewLog log){
		if(log!=null && log.getReviewId()!=null){
			log.setCreateBy(UserUtils.getUser().getId());
			log.setCreateDate(new Date());
			reviewLogDao.save(log);
		}
		
	}
	
	/**
	 * 根据reviewId查询新审批扩展
	 * */
	public List<ReviewNewExtend> findByReviewId(String reviewId) {
		return reviewNewExtendDao.findByReviewId(reviewId);
	}

	/**
	 * 修改结算管理付款审批备注为不显示状态
	 * */
	public void removeIsShowRemark(String reviewId) {
		reviewNewExtendDao.removeIsShowRemark(reviewId);
	}
	
	public void save(ReviewNewExtend rne) {
		reviewNewExtendDao.save(rne);
	}
}
