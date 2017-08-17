package com.trekiz.admin.modules.reviewreceipt.service;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.support.CommonResult;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.entity.ReviewReceiptConfiguration;
import com.trekiz.admin.modules.reviewreceipt.model.ReceiptProcessModel;
import com.trekiz.admin.modules.reviewreceipt.repository.ReviewReceiptConfigurationDao;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * 单据流程服务类
 * TODO 前期各项数据固定，后期可以采用配置的方式，使用数据库存储
 * @author yanzhenxing
 * @date 2015/11/30
 */
@Service
public class ReviewReceiptService {

    @Autowired
    private ReviewReceiptConfigurationDao reviewReceiptConfigurationDao;

    @Autowired
    private ReviewService reviewService;

    private static Map<String,String> receiptProcessMap=new HashMap<>();
    private static Map<String,String> receiptDesMap=new HashMap<>();

    static {
        receiptProcessMap.put(ReviewReceiptContext.RECEIPT_TYPE_PAYMENT,"成本付款审批、返佣审批");
        receiptProcessMap.put(ReviewReceiptContext.RECEIPT_TYPE_REFUND,"退款审批");
        receiptProcessMap.put(ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY,"签证借款审批、借款审批");
        receiptProcessMap.put(ReviewReceiptContext.RECEIPT_TYPE_VISA_RETURN_MONEY,"还签证收据审批");

        receiptDesMap.put(ReviewReceiptContext.RECEIPT_TYPE_PAYMENT,ReviewReceiptContext.RECEIPT_TYPE_PAYMENT_DES);
        receiptDesMap.put(ReviewReceiptContext.RECEIPT_TYPE_REFUND,ReviewReceiptContext.RECEIPT_TYPE_REFUND_DES);
        receiptDesMap.put(ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY,ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY_DES);
        receiptDesMap.put(ReviewReceiptContext.RECEIPT_TYPE_VISA_RETURN_MONEY,ReviewReceiptContext.RECEIPT_TYPE_VISA_RETURN_MONEY_DES);
    }

    /**
     * 获取单据审批人员Map
     * @param companyUuid 公司的uuid
     * @param receiptType 单据类型
     * @param reviewId 审批记录id
     * @return MultiValueMap<Integer,User> key：单据中审批元素的key值，value：User列表
     * @see com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext
     */
    public MultiValueMap<Integer,User> obtainReviewer4Receipt(String companyUuid,String receiptType,String reviewId){
        Assert.hasText(companyUuid,"companyUuid should not be empty!");
        Assert.hasText(receiptType,"receiptType should not be empty!");
        Assert.hasText(reviewId,"reviewId should not be empty!");
        MultiValueMap<Integer,User> result=new LinkedMultiValueMap<>();

        List<ReviewLogNew> reviewLogs=reviewService.getReviewLogByReviewId(reviewId);
        if (reviewLogs!=null&&reviewLogs.size()>0){
            MultiValueMap<Integer,String> configurations=obtainReceiptConfigurationMapInternal(companyUuid,receiptType);
            if (configurations!=null&&configurations.size()>0){
                for (ReviewLogNew reviewLog:reviewLogs){
                    if (reviewLog.getActiveFlag()==1&&!reviewLog.getOperation().equals(ReviewConstant.REVIEW_OPERATION_BACK)){//必须是有效的日志（撤销的日志属于无效）
                        String reviewerKey=reviewLog.getTagkey();
                        if (StringUtils.isNotBlank(reviewerKey)){
                            String[] reviewerTags=reviewerKey.split(",");
                            for (Integer configKey:configurations.keySet()){
                                List<String> configReviewerKeys=configurations.get(configKey);
                                for (int i=0;i<reviewerTags.length;i++){
                                    if (configReviewerKeys.contains(reviewerTags[i])){
                                        result.add(configKey, UserUtils.getUser(reviewLog.getCreateBy()));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 获取系统的单据流程关系列表
     * @return
     */
    public List<ReceiptProcessModel> obtainReceiptProcesses(){
        List<ReceiptProcessModel> receiptProcessModels=new ArrayList<>();
        for (String receiptType:receiptProcessMap.keySet()) {
            ReceiptProcessModel model=new ReceiptProcessModel();
            model.setReceiptType(receiptType);
            model.setReceiptDescription(receiptDesMap.get(receiptType));
            model.setProcessNames(receiptProcessMap.get(receiptType));

            receiptProcessModels.add(model);
        }
        return receiptProcessModels;
    }

    /**
     * 获取单据的配置列表
     * @param companyId 公司id（使用uuid）
     * @param receiptType 单据类型
     * @return
     */
    public List<ReviewReceiptConfiguration> obtainReceiptConfigurations(String companyId,String receiptType){
        Assert.hasText(companyId,"companyId should not be empty!");
        Assert.hasText(receiptType,"receiptType should not be empty!");
        return reviewReceiptConfigurationDao.findConfigurations(companyId,receiptType);
    }

    /**
     * 获取单据的配置Map
     * @param companyId
     * @param receiptType
     * @return
     */
    public MultiValueMap<String,String> obtainReceiptConfigurationMap(String companyId, String receiptType){
        Assert.hasText(companyId,"companyId should not be empty!");
        Assert.hasText(receiptType,"receiptType should not be empty!");
        MultiValueMap<String,String> result=new LinkedMultiValueMap<>();
        List<ReviewReceiptConfiguration> configurations=reviewReceiptConfigurationDao.findConfigurations(companyId,receiptType);
        if (configurations!=null&&configurations.size()>0){
            for (ReviewReceiptConfiguration configuration:configurations) {
                result.add(configuration.getReviewElement().toString(),configuration.getReviewerKey());
            }
        }

        return result;
    }
    private MultiValueMap<Integer,String> obtainReceiptConfigurationMapInternal(String companyId, String receiptType){
        MultiValueMap<Integer,String> result=new LinkedMultiValueMap<>();
        List<ReviewReceiptConfiguration> configurations=reviewReceiptConfigurationDao.findConfigurations(companyId,receiptType);
        if (configurations!=null&&configurations.size()>0){
            for (ReviewReceiptConfiguration configuration:configurations) {
                result.add(configuration.getReviewElement(),configuration.getReviewerKey());
            }
        }
        return result;
    }

    /**
     * 保存审批单据配置列表
     * @param userId 当前用户id
     * @param companyId 公司id（使用uuid）
     * @param receiptType 单据类型
     * @param configurations 单据配置列表
     * @return
     */
    @Transactional
    public CommonResult saveReviewReceiptConfigurations(String userId, String companyId, String receiptType,List<ReviewReceiptConfiguration> configurations){

        Assert.hasText(userId,"userId should not be empty!");
        Assert.hasText(companyId,"companyId should not be empty!");
        Assert.hasText(receiptType,"receiptType should not be empty!");

        CommonResult result=new CommonResult();
        result.setSuccess(true);
//        /**
//         * 检验是否已经有记录，如果有的话，更新原记录
//         */
//        List<ReviewReceiptConfiguration> existConfigurations=reviewReceiptConfigurationDao.findConfigurations(companyId,receiptType);
//
//        if (existConfigurations!=null&&existConfigurations.size()>0){
//            Date currentDate=new Date();
//            for (ReviewReceiptConfiguration existConfiguration: existConfigurations) {
//                for (ReviewReceiptConfiguration savedConfiguration:configurations) {
//                    if (existConfiguration.isSameReviewElement(savedConfiguration)){
//                        existConfiguration.copyReviewElement(savedConfiguration);
//                        savedConfiguration.setUpdateBy(userId);
//                        savedConfiguration.setUpdateDate(currentDate);
//                    }
//                }
//            }
//        }
        /**
         * 先删除原有记录，再保存新记录
         */
        reviewReceiptConfigurationDao.deleteConfigurations(userId,new Date(),companyId,receiptType);
        if (configurations!=null&&configurations.size()>0){
            reviewReceiptConfigurationDao.save(configurations);
        }

        return result;
    }

}
