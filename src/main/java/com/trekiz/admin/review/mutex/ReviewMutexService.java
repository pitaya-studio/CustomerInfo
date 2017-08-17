package com.trekiz.admin.review.mutex;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 流程互斥服务类，为流程互斥判断提供统一接口
 * @author yanzhenxing
 * @date 2015/12/3
 */
@Service
public class ReviewMutexService {

    /**
     * 旧版审批状态：进行中
     */
    private static final Integer REVIEW_STATUS_PROCESSING=1;
    /**
     * 旧版审批状态：已通过
     */
    private static final Integer REVIEW_STATUS_PASSED=2;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TravelerService travelerService;

    @Autowired
    private ReviewDao oldReviewDao;

    /**
     * 批量游客申请时进行互斥检查
     * @param orderId 订单id
     * @param travellerIds 游客id列表
     * @param productType 产品类型
     * @param processType 流程类型
     * @return
     */
    public CommonResult check(String orderId,List<String> travellerIds,String productType,String processType){
        Assert.hasText(orderId,"orderId should not be empty!");
        Assert.notEmpty(travellerIds,"travellerIds should not be empty!");
        Assert.hasText(productType,"productType should not be empty!");
        Assert.hasText(processType,"processType should not be empty!");

        CommonResult result=new CommonResult();
        result.setSuccess(true);
        StringBuilder messageBuilder=new StringBuilder();
        boolean canCancel=true;//是否允许撤销其他互斥流程
        List<ReviewNew> processingReviews=new ArrayList<>();
        List<ReviewNew> passedReviews=new ArrayList<>();
        //旧审批记录列表
        List<Review> processingOldReviews=new ArrayList<>();
        List<Review> passedOldReviews=new ArrayList<>();
        for (String travellerId:travellerIds){
            CommonResult singleResult=check(orderId,travellerId,productType,processType,false);
            if (!singleResult.getSuccess()){
                result.setSuccess(false);
                Map<String,Object> params=singleResult.getParams();
                if(params.containsKey("canCancel")&&!(boolean)params.get("canCancel")){
                    canCancel=false;
                }
                messageBuilder.append(StringUtils.isBlank(singleResult.getMessage())?"":singleResult.getMessage());
                if (params.containsKey(ReviewMutexContext.PROCESSING_REVIEWS)){
                    processingReviews.addAll((List)params.get(ReviewMutexContext.PROCESSING_REVIEWS));
                }
                if (params.containsKey(ReviewMutexContext.PASSED_REVIEWS)){
                    passedReviews.addAll((List)params.get(ReviewMutexContext.PASSED_REVIEWS));
                }
                if (params.containsKey(ReviewMutexContext.PROCESSING_OLD_REVIEWS)){
                    processingOldReviews.addAll((List)params.get(ReviewMutexContext.PROCESSING_OLD_REVIEWS));
                }
                if (params.containsKey(ReviewMutexContext.PASSED_OLD_REVIEWS)){
                    passedOldReviews.addAll((List)params.get(ReviewMutexContext.PASSED_OLD_REVIEWS));
                }
            }
        }

        Map<String,Object> params=new HashMap<>();
        params.put("canCancel",canCancel);
        params.put(ReviewMutexContext.PROCESSING_REVIEWS,processingReviews);
        params.put(ReviewMutexContext.PASSED_REVIEWS,passedReviews);
        //旧版审批记录
        params.put(ReviewMutexContext.PROCESSING_OLD_REVIEWS,processingOldReviews);
        params.put(ReviewMutexContext.PASSED_REVIEWS,passedOldReviews);

        result.setMessage(messageBuilder.toString());
        result.setParams(params);

        return result;
    }

    /**
     * 流程申请前检查互斥
     * @param orderId 订单id
     * @param travellerId 游客id
     * @param productType 产品类型
     * @param processType 要申请的流程类型
     * @param isGroup 是否是团队
     * @return CommonResult
     */
    public CommonResult check(String orderId,String travellerId,String productType,String processType,boolean isGroup){
        Assert.hasText(orderId,"orderId should not be empty!");
        Assert.hasText(travellerId,"travellerId should not be empty!");
        Assert.hasText(productType,"productType should not be empty!");
        Assert.hasText(processType,"processType should not be empty!");
        Assert.notNull(isGroup,"isGroup should not be null!");
        CommonResult result=new CommonResult();
        result.setSuccess(true);
        //如果是团队申请，目前没有限制
        if (isGroup){
            return result;
        }

        /**
         * 判断产品类型（单团、机票、签证），选择互斥检查方式
         */

        MutexReviewList mutexReviewList;

        switch (productType){
            case "1"://单团
                mutexReviewList=checkMutext4Single(orderId,travellerId,productType,processType);
                break;
            case "2"://散拼
                mutexReviewList=checkMutext4Loose(orderId,travellerId,processType);
                break;
            case "3"://游学
                mutexReviewList=checkMutext4Single(orderId,travellerId,productType,processType);
                break;
            case "4"://大客户
                mutexReviewList=checkMutext4Single(orderId,travellerId,productType,processType);
                break;
            case "5"://自由行
                mutexReviewList=checkMutext4Single(orderId,travellerId,productType,processType);
                break;
            case "10"://游轮
                mutexReviewList=checkMutext4Single(orderId,travellerId,productType,processType);
                break;
            case "6"://签证
                mutexReviewList=checkMutext4Visa(orderId,travellerId,productType,processType);
                break;
            case "7"://机票
                mutexReviewList=checkMutext4AirTicket(orderId,travellerId,productType,processType);
                break;
            default:
                mutexReviewList=new MutexReviewList();
        }

        //如果结果有审批记录，表明存在互斥，进行参数组装
        if (mutexReviewList.getHasReviews()){
            String userId= UserUtils.getUser().getId().toString();
            Traveler traveler=travelerService.findTravelerById(Long.parseLong(travellerId));
            boolean canCancel=true;//是否允许撤销其他互斥流程
            result.setSuccess(false);
            Map<String,Object> params=new HashMap<>();
            StringBuilder stringBuilder=new StringBuilder();//通用的提示信息主体
            List<ReviewNew> processingReviews=mutexReviewList.getProcessingReviews();
            if (processingReviews!=null&&processingReviews.size()>0){
                //提取正在审批的互斥的流程类型
                Set<String> processingProcessTypes=new HashSet<>();
                for (ReviewNew review:processingReviews){
                    processingProcessTypes.add(review.getProcessType());
                    if(!review.getCreateBy().equals(userId)){
                        canCancel=false;
                    }

                    /**
                     * 组装通用提示语
                     */
                    if(traveler==null){
                        stringBuilder.append("此订单 ");
                    }else{
                        stringBuilder.append("游客 ");
                        stringBuilder.append(traveler==null?" ":traveler.getName());
                    }
                    stringBuilder.append(": ");
                    stringBuilder.append(Context.getREVIEW_FLOW().get(review.getProcessType()));
                    stringBuilder.append(" 流程审批中（申请人：");
                    stringBuilder.append(UserUtils.getUserNameById(Long.parseLong(review.getCreateBy())));
                    stringBuilder.append(" )");
                    stringBuilder.append("<br>");

                }
                //检查特殊场景
                if (!canCancelSpecialCheck(processType,processingProcessTypes)){
                    canCancel=false;
                }

                params.put(ReviewMutexContext.PROCESSING_REVIEWS,processingReviews);
                params.put(ReviewMutexContext.PROCESSING_PROCESS_TYPES,processingProcessTypes);
            }
            List<ReviewNew> passedReviews=mutexReviewList.getPassedReviews();
            if (passedReviews!=null&&passedReviews.size()>0){
                //提取审批通过的互斥的流程类型
                Set<String> passedProcessTypes=new HashSet<>();
                for (ReviewNew review:passedReviews){
                    passedProcessTypes.add(review.getProcessType());

                    /**
                     * 组装通用提示语
                     */
                    stringBuilder.append("游客 ");
                    stringBuilder.append(traveler==null?" ":traveler.getName());
                    stringBuilder.append(": ");
                    stringBuilder.append(Context.getREVIEW_FLOW().get(review.getProcessType()));
                    stringBuilder.append(" 流程已通过（申请人：");
                    stringBuilder.append(UserUtils.getUserNameById(Long.parseLong(review.getCreateBy())));
                    stringBuilder.append(" )");
                    stringBuilder.append("<br>");
                }
                //只要存在已经通过的流程，就不允许取消
                canCancel=false;

                params.put(ReviewMutexContext.PASSED_REVIEWS,passedReviews);
                params.put(ReviewMutexContext.PASSED_PROCESS_TYPES,passedProcessTypes);
            }

            //旧版审批数据处理
            List<Review> processingOldReviews=mutexReviewList.getProcessingOldReviews();
            if (processingOldReviews!=null&&processingOldReviews.size()>0){
                Set<String> processingOldProcessTypes=new HashSet<>();
                for (Review review:processingOldReviews){
                    processingOldProcessTypes.add(review.getFlowType().toString());
                    if(!review.getCreateBy().equals(userId)){
                        canCancel=false;
                    }

                    /**
                     * 组装通用提示语
                     */
                    stringBuilder.append("游客 ");
                    stringBuilder.append(traveler==null?" ":traveler.getName());
                    stringBuilder.append(": ");
                    stringBuilder.append(Context.getREVIEW_FLOW().get(review.getFlowType().toString()));
                    stringBuilder.append(" 流程审批中（申请人：");
                    stringBuilder.append(UserUtils.getUserNameById(review.getCreateBy()));
                    stringBuilder.append(" )");
                    stringBuilder.append("<br>");

                }
                //检查特殊场景
                if (!canCancelSpecialCheck(processType,processingOldProcessTypes)){
                    canCancel=false;
                }
                params.put(ReviewMutexContext.PROCESSING_OLD_REVIEWS,processingOldReviews);
            }

            List<Review> passedOldReviews=mutexReviewList.getPassedOldReviews();
            if (passedOldReviews!=null&&passedOldReviews.size()>0){
                for (Review review:passedOldReviews){
                    /**
                     * 组装通用提示语
                     */
                    stringBuilder.append("游客 ");
                    stringBuilder.append(traveler==null?" ":traveler.getName());
                    stringBuilder.append(": ");
                    stringBuilder.append(Context.getREVIEW_FLOW().get(review.getFlowType().toString()));
                    stringBuilder.append(" 流程已通过（申请人：");
                    stringBuilder.append(UserUtils.getUserNameById(review.getCreateBy()));
                    stringBuilder.append(" )");
                    stringBuilder.append("<br>");
                }
                //只要存在已经通过的流程，就不允许取消
                canCancel=false;

                params.put(ReviewMutexContext.PASSED_OLD_REVIEWS,passedOldReviews);
            }

//            params.put("canCancel",canCancel);
            params.put(ReviewMutexContext.CAN_CANCEL,canCancel);

            result.setParams(params);
            result.setMessage(stringBuilder.toString());
        }

        return result;
    }

    /**
     * 检查能否取消申请的特殊条件
     * @param currentProcessType
     * @param mutexProcessTypes
     * @return
     */
    private boolean canCancelSpecialCheck(String currentProcessType,Set<String> mutexProcessTypes){

        if (StringUtils.isBlank(currentProcessType)||mutexProcessTypes==null||mutexProcessTypes.size()==0) return true;
        boolean result=true;
        switch (currentProcessType){
            /**
             * 如果是退团申请，则原来存在退团申请的情况下不能取消原来的申请
             */
            case "8":
                if(mutexProcessTypes.contains(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString())){
                    result= false;
                }
                break;
            /**
             * 如果是转团申请，则原来存在转团和退团的情况下不能取消原来的申请
             */
            case "11":
                if (mutexProcessTypes.contains(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString())||mutexProcessTypes.contains(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString())){
                    result= false;
                }
                break;
            /**
             * 如果是退票申请，则原来存在退票申请的情况下不能取消原来的申请
             */
            case "3":
                if (mutexProcessTypes.contains(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString())){
                    result= false;
                }
                break;
            /**
             * 如果是改签申请，则原来存在退票申请或者改签申请的情况下不能取消原来的申请
             */
            case "14":
                if (mutexProcessTypes.contains(Context.REVIEW_FLOWTYPE_AIRTICKET_CHANGE)||mutexProcessTypes.contains(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString())){
                    result= false;
                }
                break;
            default: result=true;
        }
        return result;
    }

    /**
     * 查询单团类产品与新的申请流程互斥的审批记录
     * @param orderId
     * @param travellerId
     * @param productType
     * @param processType
     * @return
     */
    private MutexReviewList checkMutext4Single(String orderId,String travellerId,String productType,String processType){

        /**
         * 首先查找当前要申请的流程是否和别的流程互斥
         */
        Set<String> processingProcessTypes=ReviewMutexContext.SINGLE_TRAVELLER_PROCESSING_MUTEX.containsKey(processType)?
                ReviewMutexContext.SINGLE_TRAVELLER_PROCESSING_MUTEX.get(processType):null;
        Set<String> passedProcessTypes=ReviewMutexContext.SINGLE_TRAVELLER_PASSED_MUTEX.containsKey(processType)?
                ReviewMutexContext.SINGLE_TRAVELLER_PASSED_MUTEX.get(processType):null;

        if (processingProcessTypes==null&&passedProcessTypes==null){
            return new MutexReviewList();
        }

        return checkMutexReviews(orderId,travellerId,productType,processingProcessTypes,passedProcessTypes);
    }

    /**
     * 查询散拼类产品与新的申请流程互斥的审批记录
     * @param orderId
     * @param travellerId
     * @param processType
     * @return
     */
    private MutexReviewList checkMutext4Loose(String orderId,String travellerId,String processType){

        /**
         * 散拼类存在订单互斥
         */
        Set<String> processingOrderProcessTypes=ReviewMutexContext.SINGLE_ORDER_PROCESSING_MUTEX.containsKey(processType)?
                ReviewMutexContext.SINGLE_ORDER_PROCESSING_MUTEX.get(processType):null;
        Set<String> passedOrderProcessTypes=ReviewMutexContext.SINGLE_ORDER_PASSED_MUTEX.containsKey(processType)?
                ReviewMutexContext.SINGLE_ORDER_PASSED_MUTEX.get(processType):null;

        /**
         * 首先查找当前要申请的流程是否和别的流程互斥
         */
        Set<String> processingProcessTypes=ReviewMutexContext.SINGLE_TRAVELLER_PROCESSING_MUTEX.containsKey(processType)?
                ReviewMutexContext.SINGLE_TRAVELLER_PROCESSING_MUTEX.get(processType):null;
        Set<String> passedProcessTypes=ReviewMutexContext.SINGLE_TRAVELLER_PASSED_MUTEX.containsKey(processType)?
                ReviewMutexContext.SINGLE_TRAVELLER_PASSED_MUTEX.get(processType):null;

        if (processingOrderProcessTypes==null&&passedOrderProcessTypes==null&&processingProcessTypes==null&&passedProcessTypes==null){
            return new MutexReviewList();
        }

        MutexReviewList orderReviewList=checkMutexReviews(orderId, Context.ProductType.PRODUCT_LOOSE.toString(),processingOrderProcessTypes,passedOrderProcessTypes);
        MutexReviewList travellerReviewList=checkMutexReviews(orderId,travellerId, Context.ProductType.PRODUCT_LOOSE.toString(),processingProcessTypes,passedProcessTypes);
        return merge(orderReviewList,travellerReviewList);
    }



    /**
     * 合并两个ReviewList
     * @param reviewList1
     * @param reviewList2
     * @return
     */
    private MutexReviewList merge(MutexReviewList reviewList1,MutexReviewList reviewList2){
        if(reviewList1==null){
            return reviewList2;
        }
        if(reviewList2==null){
            return reviewList1;
        }

        MutexReviewList resultList=new MutexReviewList();

        resultList.setHasReviews(reviewList1.getHasReviews()||reviewList2.getHasReviews());

        resultList.getProcessingReviews().addAll(reviewList1.getProcessingReviews());
        resultList.getProcessingReviews().addAll(reviewList2.getProcessingReviews());

        resultList.getProcessingOldReviews().addAll(reviewList1.getProcessingOldReviews());
        resultList.getProcessingOldReviews().addAll(reviewList2.getProcessingOldReviews());

        resultList.getPassedReviews().addAll(reviewList1.getPassedReviews());
        resultList.getPassedReviews().addAll(reviewList2.getPassedReviews());

        resultList.getPassedOldReviews().addAll(reviewList1.getPassedOldReviews());
        resultList.getPassedOldReviews().addAll(reviewList2.getPassedOldReviews());

        return resultList;
    }

    /**
     * 查询机票类产品与新的申请流程互斥的审批记录
     * @param orderId
     * @param travellerId
     * @param productType
     * @param processType
     * @return
     */
    private MutexReviewList checkMutext4AirTicket(String orderId,String travellerId,String productType,String processType){

        /**
         * 首先查找当前要申请的流程是否和别的流程互斥
         */
        Set<String> processingProcessTypes=ReviewMutexContext.AIRTICKET_TRAVELLER_PROCESSING_MUTEX.containsKey(processType)?
                ReviewMutexContext.AIRTICKET_TRAVELLER_PROCESSING_MUTEX.get(processType):null;
        Set<String> passedProcessTypes=ReviewMutexContext.AIRTICKET_TRAVELLER_PASSED_MUTEX.containsKey(processType)?
                ReviewMutexContext.AIRTICKET_TRAVELLER_PASSED_MUTEX.get(processType):null;

        if (processingProcessTypes==null&&passedProcessTypes==null){
            return new MutexReviewList();
        }

        return checkMutexReviews(orderId,travellerId,productType,processingProcessTypes,passedProcessTypes);
    }

    /**
     * 查询签证类产品与新的申请流程互斥的审批记录
     * @param orderId
     * @param travellerId
     * @param productType
     * @param processType
     * @return
     */
    private MutexReviewList checkMutext4Visa(String orderId,String travellerId,String productType,String processType){

        /**
         * 首先查找当前要申请的流程是否和别的流程互斥
         */
        Set<String> processingProcessTypes=ReviewMutexContext.VISA_TRAVELLER_PROCESSING_MUTEX.containsKey(processType)?
                ReviewMutexContext.VISA_TRAVELLER_PROCESSING_MUTEX.get(processType):null;
        Set<String> passedProcessTypes=ReviewMutexContext.VISA_TRAVELLER_PASSED_MUTEX.containsKey(processType)?
                ReviewMutexContext.VISA_TRAVELLER_PASSED_MUTEX.get(processType):null;

        if (processingProcessTypes==null&&passedProcessTypes==null){
            return new MutexReviewList();
        }

        return checkMutexReviews(orderId,travellerId,productType,processingProcessTypes,passedProcessTypes);
    }

    private MutexReviewList checkMutexReviews(String orderId,String travellerId,String productType,Set<String> processingProcessTypes,Set<String> passedProcessTypes){
        MutexReviewList result=new MutexReviewList();
        //查询正在进行中的互斥的记录
        if (processingProcessTypes!=null&&processingProcessTypes.size()>0){
            List<ReviewNew> reviewNews=reviewService.findMutexProcessingReviews(orderId,travellerId,productType,processingProcessTypes);
            if (reviewNews!=null&&reviewNews.size()>0){
                result.setHasReviews(true);
                result.setProcessingReviews(reviewNews);
            }
            //查询旧版审批记录
            List<Review> oldReviews=getOldProcessingReviews(orderId, travellerId, productType, processingProcessTypes);
            if (oldReviews!=null&&oldReviews.size()>0){
                result.setHasReviews(true);
                result.setProcessingOldReviews(oldReviews);
            }
        }
        //查询已通过的互斥的记录
        if (passedProcessTypes!=null&&passedProcessTypes.size()>0){
            List<ReviewNew> reviewNews=reviewService.findMutexPassedReviews(orderId,travellerId,productType,passedProcessTypes);
            if (reviewNews!=null&&reviewNews.size()>0){
                result.setHasReviews(true);
                result.setPassedReviews(reviewNews);
            }
            //查询旧版审批记录
            List<Review> oldReviews=getOldPassedReviews(orderId, travellerId, productType, passedProcessTypes);
            if (oldReviews!=null&&oldReviews.size()>0){
                result.setHasReviews(true);
                result.setPassedOldReviews(oldReviews);
            }
        }
        return result;
    }

    /**
     * 检查互斥的审批流程列表，订单维度
     * @param orderId
     * @param productType
     * @param processingProcessTypes
     * @param passedProcessTypes
     * @return
     */
    private MutexReviewList checkMutexReviews(String orderId,String productType,Set<String> processingProcessTypes,Set<String> passedProcessTypes){
        MutexReviewList result=new MutexReviewList();
        //查询正在进行中的互斥的记录
        if (processingProcessTypes!=null&&processingProcessTypes.size()>0){
            List<ReviewNew> reviewNews=reviewService.findMutexProcessingReviews(orderId,productType,processingProcessTypes);
            if (reviewNews!=null&&reviewNews.size()>0){
                result.setHasReviews(true);
                result.setProcessingReviews(reviewNews);
            }
            //查询旧版审批记录
//            List<Review> oldReviews=getOldProcessingReviews(orderId, productType, processingProcessTypes);
//            if (oldReviews!=null&&oldReviews.size()>0){
//                result.setHasReviews(true);
//                result.setProcessingOldReviews(oldReviews);
//            }
        }
        //查询已通过的互斥的记录
        if (passedProcessTypes!=null&&passedProcessTypes.size()>0){
            List<ReviewNew> reviewNews=reviewService.findMutexPassedReviews(orderId,productType,passedProcessTypes);
            if (reviewNews!=null&&reviewNews.size()>0){
                result.setHasReviews(true);
                result.setPassedReviews(reviewNews);
            }
            //查询旧版审批记录
//            List<Review> oldReviews=getOldPassedReviews(orderId, travellerId, productType, passedProcessTypes);
//            if (oldReviews!=null&&oldReviews.size()>0){
//                result.setHasReviews(true);
//                result.setPassedOldReviews(oldReviews);
//            }
        }
        return result;
    }

    /**
     * 获取审批中的旧审批记录列表
     * @param orderId
     * @param travellerId
     * @param productType
     * @param processingProcessTypes
     * @return
     */
    private List<Review> getOldProcessingReviews(String orderId, String travellerId, String productType, Set<String> processingProcessTypes){
        String sql= buildSql4OldReviews(orderId,travellerId,productType,processingProcessTypes,REVIEW_STATUS_PROCESSING);
        return oldReviewDao.findBySql(sql,Review.class);
    }
    /**
     * 获取审批中的旧审批记录列表，订单维度
     * @param orderId
     * @param productType
     * @param processingProcessTypes
     * @return
     */
    private List<Review> getOldProcessingReviews(String orderId, String productType, Set<String> processingProcessTypes){
        String sql= buildSql4OldReviews(orderId,productType,processingProcessTypes,REVIEW_STATUS_PROCESSING);
        return oldReviewDao.findBySql(sql,Review.class);
    }
    /**
     * 获取已通过的旧审批记录列表
     * @param orderId
     * @param travellerId
     * @param productType
     * @param processingProcessTypes
     * @return
     */
    private List<Review> getOldPassedReviews(String orderId,String travellerId,String productType,Set<String> processingProcessTypes){
        String sql=buildSql4OldReviews(orderId,travellerId,productType,processingProcessTypes,REVIEW_STATUS_PASSED);
        return oldReviewDao.findBySql(sql,Review.class);
    }

    /**
     * 构建基本的查询语句
     * @param orderId
     * @param travellerId
     * @param productType
     * @param processTypes
     * @param status
     * @return
     */
    private String buildSql4OldReviews(String orderId,String travellerId,String productType,Set<String> processTypes,Integer status){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("SELECT * FROM review r WHERE ");
        stringBuilder.append(" r.orderId='");
        stringBuilder.append(orderId);
        stringBuilder.append("' AND r.travelerId=");
        stringBuilder.append(travellerId);
        stringBuilder.append(" AND r.productType=");
        stringBuilder.append(productType);
        stringBuilder.append(" AND r.status=");
        stringBuilder.append(status);
        stringBuilder.append(" AND r.flowType IN (");
        StringBuilder processTypesBuilder=new StringBuilder();
        for (String processType:processTypes){
            processTypesBuilder.append(processType).append(",");
        }
        stringBuilder.append(processTypesBuilder.substring(0,processTypesBuilder.length()-1));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * 构建基本的查询语句
     * @param orderId
     * @param productType
     * @param processTypes
     * @param status
     * @return
     */
    private String buildSql4OldReviews(String orderId,String productType,Set<String> processTypes,Integer status){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("SELECT * FROM review r WHERE ");
        stringBuilder.append(" r.orderId='");
        stringBuilder.append(orderId);
        stringBuilder.append("' AND r.productType=");
        stringBuilder.append(productType);
        stringBuilder.append(" AND r.status=");
        stringBuilder.append(status);
        stringBuilder.append(" AND r.flowType IN (");
        StringBuilder processTypesBuilder=new StringBuilder();
        for (String processType:processTypes){
            processTypesBuilder.append(processType).append(",");
        }
        stringBuilder.append(processTypesBuilder.substring(0,processTypesBuilder.length()-1));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
