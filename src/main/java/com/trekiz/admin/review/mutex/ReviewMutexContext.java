package com.trekiz.admin.review.mutex;

import com.trekiz.admin.common.config.Context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 流程互斥配置规则，
 * 注意：审批类型互斥Map中，key：要申请的流程，value：与该申请存在互斥关系的流程集合
 * @author yanzhenxing
 * @date 2015/12/3
 */
public class ReviewMutexContext {

    /**
     * 常量字符串：互斥的审批中的记录
     */
    public static  final String PROCESSING_REVIEWS="processingReviews";
    /**
     * 常量字符串：互斥的旧审批中的记录
     */
    public static  final String PROCESSING_OLD_REVIEWS="processingOldReviews";
    /**
     * 常量字符串：互斥的审批中的审批类型列表
     */
    public static  final String PROCESSING_PROCESS_TYPES="processingProcessTypes";
    /**
     * 常量字符串：互斥的已通过的记录
     */
    public static  final String PASSED_REVIEWS="passedReviews";
    /**
     * 常量字符串：互斥的已通过的旧审批记录
     */
    public static  final String PASSED_OLD_REVIEWS="passedOldReviews";
    /**
     * 常量字符串：互斥的已通过的审批类型列表
     */
    public static  final String PASSED_PROCESS_TYPES="passedProcessTypes";
    /**
     * 常量字符串：互斥时是否允许取消其他流程，进而发起此流程
     */
    public static final String CAN_CANCEL="canCancel";

    /**
     * 单团-团队-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> SINGLE_GROUP_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 单团-团队-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> SINGLE_GROUP_PASSED_MUTEX=new HashMap<>();

    /**
     * 单团-订单-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> SINGLE_ORDER_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 单团-订单-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> SINGLE_ORDER_PASSED_MUTEX=new HashMap<>();

    /**
     * 单团-游客-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> SINGLE_TRAVELLER_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 单团-游客-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> SINGLE_TRAVELLER_PASSED_MUTEX=new HashMap<>();


    /**
     * 机票-团队-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> AIRTICKET_GROUP_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 机票-团队-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> AIRTICKET_GROUP_PASSED_MUTEX=new HashMap<>();

    /**
     * 机票-订单-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> AIRTICKET_ORDER_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 机票-订单-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> AIRTICKET_ORDER_PASSED_MUTEX=new HashMap<>();

    /**
     * 机票-游客-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> AIRTICKET_TRAVELLER_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 机票-游客-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> AIRTICKET_TRAVELLER_PASSED_MUTEX=new HashMap<>();


    /**
     * 签证-团队-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> VISA_GROUP_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 签证-团队-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> VISA_GROUP_PASSED_MUTEX=new HashMap<>();

    /**
     * 签证-订单-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> VISA_ORDER_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 签证-订单-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> VISA_ORDER_PASSED_MUTEX=new HashMap<>();

    /**
     * 签证-游客-执行中互斥
     * key：即将要发起申请的审批类型，value：在进行中的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> VISA_TRAVELLER_PROCESSING_MUTEX=new HashMap<>();

    /**
     * 签证-游客-已通过互斥
     * key：即将要发起申请的审批类型，value：已审批通过的与key流程互斥的流程类型列表
     */
    public static Map<String,Set<String>> VISA_TRAVELLER_PASSED_MUTEX=new HashMap<>();

    /**
     * 设置互斥规则，参见互斥需求文档
     */
    static {

        /**
         * 目前团队的互斥没有限制
         */

        /**
         * 目前以订单维度进行互斥的只有单团类中（散拼）的退团、转团、改价与散拼优惠流程互斥，散拼优惠与自身互斥
         * =========================================start=================================================
         */
        /*与散拼优惠互斥的流程*/
        SINGLE_ORDER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString());
            }
        });

        /*与退团互斥的流程*/
        SINGLE_ORDER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString());
            }
        });

        /*与转团互斥的流程*/
        SINGLE_ORDER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString());
            }
        });

        /**
         * ==========================================end==================================================
         */

        /**
         * 下面针对单个游客的互斥规则进行初始化
         */

        /**
         * 单团类--申请与其他进行中的流程互斥
         */
        /*与转团申请互斥的流程*/
        SINGLE_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//改价（10）
                add(Context.REBATES_FLOW_TYPE.toString());//返佣（9）
                add(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());//借款（19）
            }
        });
        /*与转款申请互斥的流程*/
        SINGLE_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//改价（10）
                add(Context.REBATES_FLOW_TYPE.toString());//返佣（9）
                add(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());//借款（19）
            }
        });
        /*与退团申请互斥的流程*/
        SINGLE_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//改价（10）
                add(Context.REBATES_FLOW_TYPE.toString());//返佣（9）
                add(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());//借款（19）
            }
        });
        /*与改价申请互斥的流程*/
        SINGLE_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//改价（10）

                /**
                 * 改价与散拼优惠审批互斥，订单级：只要订单有优惠审批正在进行，游客无法发起改价，订单级互斥暂时不放在这里
                 */
//                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//散拼优惠审批
            }
        });
        /*与返佣申请互斥的流程*/
        SINGLE_TRAVELLER_PROCESSING_MUTEX.put(Context.REBATES_FLOW_TYPE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
            }
        });
        /*与借款申请互斥的流程*/
        SINGLE_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
            }
        });
        /**
         * 单团类--申请与其他已通过的流程互斥
         */
        /*与转团申请互斥的流程*/
        SINGLE_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
            }
        });
        /*与转款申请互斥的流程*/
        SINGLE_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
            }
        });
        /*与退团申请互斥的流程*/
        SINGLE_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
            }
        });
        /*与改价申请互斥的流程*/
        SINGLE_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
            }
        });
        /*与返佣申请互斥的流程*/
        SINGLE_TRAVELLER_PASSED_MUTEX.put(Context.REBATES_FLOW_TYPE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
            }
        });
        /*与借款申请互斥的流程*/
        SINGLE_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());//转团（11）
                add(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());//转款（12）
                add(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString());//退团（8）
            }
        });


        /**
         * 机票类--申请与其他进行中的流程互斥
         */
        /* 与退票申请互斥的流程*/
        AIRTICKET_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//改价（10）
                add(Context.REBATES_FLOW_TYPE.toString());//返佣（9）
                add(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());//借款（19）
            }
        });
        /*与改签申请互斥的流程*/
        AIRTICKET_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//改价（10）
                add(Context.REBATES_FLOW_TYPE.toString());//返佣（9）
                add(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());//借款（19）
            }
        });
        /*与改价申请互斥的流程*/
        AIRTICKET_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//改价（10）
            }
        });
        /*与返佣申请互斥的流程*/
        AIRTICKET_TRAVELLER_PROCESSING_MUTEX.put(Context.REBATES_FLOW_TYPE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
            }
        });
        /*与借款申请互斥的流程*/
        AIRTICKET_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
            }
        });

        /**
         * 机票类--申请与其他已通过的流程互斥
         */
        AIRTICKET_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
            }
        });
        /*与改签申请互斥的流程*/
        AIRTICKET_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
            }
        });
        /*与改价申请互斥的流程*/
        AIRTICKET_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
            }
        });
        /*与返佣申请互斥的流程*/
        AIRTICKET_TRAVELLER_PASSED_MUTEX.put(Context.REBATES_FLOW_TYPE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
            }
        });
        /*与借款申请互斥的流程*/
        AIRTICKET_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_BORROWMONEY.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString());//退票（3）
                add(Context.REVIEW_FLOWTYPE_VISA_CHANGE.toString());//改签（14）
            }
        });

        /**
         * 签证类--申请与其他进行中的流程互斥 TODO 退押金和还押金收据未配置
         */
        /*与改价申请互斥的流程*/
        VISA_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString());//改价（10）
            }
        });
        /*与担保变更申请互斥的流程*/
        VISA_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_GUARANTEE.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_GUARANTEE.toString());//担保变更（22）
            }
        });
        /*与签证借款申请互斥的流程*/
        VISA_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString());//签证借款（5）
                add(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString());//还签证收据（4）
            }
        });
        /*与还签证收据申请互斥的流程*/
        VISA_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString());//签证借款（5）
                add(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString());//还签证收据（4）
            }
        });
        /*与签证押金转担保申请互斥的流程*/
        VISA_TRAVELLER_PROCESSING_MUTEX.put(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT.toString());//签证押金转担保（6）
            }
        });

        /**
         * 签证类--申请与其他已通过的流程互斥 TODO 退押金和还押金收据未配置
         */
        /*与签证借款申请互斥的流程*/
        VISA_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString());//签证借款（5）
                add(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString());//还签证收据（4）
            }
        });
        /*与还签证收据申请互斥的流程*/
        VISA_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString());//还签证收据（4）
            }
        });
        /*与签证押金转担保申请互斥的流程*/
        VISA_TRAVELLER_PASSED_MUTEX.put(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT.toString(),new HashSet<String>(){
            {
                add(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT.toString());//签证押金转担保（6）
            }
        });
    }



}
