package com.trekiz.admin.review.mutex;

import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.modules.reviewflow.entity.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * 互斥的记录列表vo
 * @author yanzhenxing
 * @date 2015/12/10
 */
public class MutexReviewList {

    /**
     * 是否有审批记录
     */
    private boolean hasReviews;
    /**
     * 正在审批中的审批记录列表
     */
    private List<ReviewNew> processingReviews;
    /**
     * 已经通过的审批记录列表
     */
    private List<ReviewNew> passedReviews;
    /**
     * 正在审批中的旧版审批记录列表
     */
    private List<Review> processingOldReviews;
    /**
     * 已经通过的旧版审批记录列表
     */
    private List<Review> passedOldReviews;

    public MutexReviewList() {
        processingReviews=new ArrayList<>();
        passedReviews=new ArrayList<>();
        processingOldReviews=new ArrayList<>();
        passedOldReviews=new ArrayList<>();
    }

    public boolean getHasReviews() {
        return hasReviews;
    }

    public void setHasReviews(boolean hasReviews) {
        this.hasReviews = hasReviews;
    }

    public List<ReviewNew> getPassedReviews() {
        return passedReviews;
    }

    public void setPassedReviews(List<ReviewNew> passedReviews) {
        this.passedReviews = passedReviews;
    }

    public List<ReviewNew> getProcessingReviews() {
        return processingReviews;
    }

    public void setProcessingReviews(List<ReviewNew> processingReviews) {
        this.processingReviews = processingReviews;
    }

    public List<Review> getProcessingOldReviews() {
        return processingOldReviews;
    }

    public void setProcessingOldReviews(List<Review> processingOldReviews) {
        this.processingOldReviews = processingOldReviews;
    }

    public List<Review> getPassedOldReviews() {
        return passedOldReviews;
    }

    public void setPassedOldReviews(List<Review> passedOldReviews) {
        this.passedOldReviews = passedOldReviews;
    }
}
