package com.trekiz.admin.modules.reviewreceipt.common;


import java.util.HashMap;
import java.util.Map;

/**
 * 审批相关的单据
 * @author yanzhenxing
 * @date 2015/11/30
 */
public abstract class ReviewReceiptContext {

    /**
     *单据类型 payment：支出凭单
     */
    public static  final String RECEIPT_TYPE_PAYMENT="payment";
    /**
     *单据类型 refund：退款单
     */
    public static  final String RECEIPT_TYPE_REFUND="refund";
    /**
     *单据类型 borrow_money：签证费借款单
     */
    public static  final String RECEIPT_TYPE_BORROW_MONEY="borrow_money";
    /**
     *单据类型 visa_return_money：签证费还款单
     */
    public static  final String RECEIPT_TYPE_VISA_RETURN_MONEY="visa_return_money";

    /**
     *单据类型描述：支出凭单
     */
    public static  final String RECEIPT_TYPE_PAYMENT_DES="支出凭单";
    /**
     *单据类型描述：退款单
     */
    public static  final String RECEIPT_TYPE_REFUND_DES="退款单";
    /**
     *单据类型描述：借款单
     */
    public static  final String RECEIPT_TYPE_BORROW_MONEY_DES="借款单";
    /**
     *单据类型描述：签证费还款单
     */
    public static  final String RECEIPT_TYPE_VISA_RETURN_MONEY_DES="签证费还款单";

    /**
     *单据类型-描述Map
     */
    public static final Map<String,String> RECEIPT_TYPE_MAP=new HashMap<String,String>(){
        {
            put(RECEIPT_TYPE_PAYMENT,RECEIPT_TYPE_PAYMENT_DES);
            put(RECEIPT_TYPE_REFUND,RECEIPT_TYPE_REFUND_DES);
            put(RECEIPT_TYPE_BORROW_MONEY,RECEIPT_TYPE_BORROW_MONEY_DES);
            put(RECEIPT_TYPE_VISA_RETURN_MONEY,RECEIPT_TYPE_VISA_RETURN_MONEY_DES);
        }
    };

    /**
     * 支出凭单审批元素类
     */
    public static class PaymentReviewElement{

        /**
         * 主管
         */
        public static final Integer EXECUTIVE=1;

        /**
         * 总经理
         */
        public static final Integer GENERAL_MANAGER=2;

        /**
         * 财务主管
         */
        public static final Integer FINANCIAL_EXECUTIVE=3;

        /**
         * 出纳
         */
        public static final Integer CASHIER=4;

        /**
         * 审核
         */
        public static final Integer REVIEWER=5;
    }

    /**
     * 退款单审批元素类
     */
    public static class RefundReviewElement{

        /**
         * 财务
         */
        public static final Integer FINANCIAL=1;

        /**
         * 审批
         */
        public static final Integer REVIEWER=2;

        /**
         * 经理
         */
        public static final Integer MANAGER=3;
    }

    /**
     * 借款单审批元素
     */
    public static class BorrowMoneyReviewElement {

        /**
         * 财务
         */
        public static final Integer FINANCIAL=1;

        /**
         * 总经理
         */
        public static final Integer GENERAL_MANAGER=2;

        /**
         * 财务主管
         */
        public static final Integer FINANCIAL_EXECUTIVE=3;

        /**
         * 出纳
         */
        public static final Integer CASHIER=4;

        /**
         * 审核
         */
        public static final Integer REVIEWER=5;
    }

    /**
     * 签证费还款单审批元素类
     */
    public static class VisaReturnMoneyReviewElement{
        /**
         * 财务
         */
        public static final Integer FINANCIAL=1;

        /**
         * 总经理
         */
        public static final Integer GENERAL_MANAGER=2;

        /**
         * 财务主管
         */
        public static final Integer FINANCIAL_EXECUTIVE=3;

        /**
         * 出纳
         */
        public static final Integer CASHIER=4;

        /**
         * 审核
         */
        public static final Integer REVIEWER=5;
    }
}
