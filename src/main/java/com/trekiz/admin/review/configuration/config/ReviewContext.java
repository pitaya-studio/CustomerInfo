package com.trekiz.admin.review.configuration.config;

import com.trekiz.admin.common.config.Context;

import java.util.Map;
import java.util.TreeMap;

/**
 * Copyright   2015  QUAUQ Technology Co. Ltd.
 *
 * @author zhenxing.yan
 * @date 2015年11月19日
 */
public abstract class ReviewContext {
	// 产品类型map
		public static Map<Integer, String> productTypeMap = new TreeMap<Integer, String>();
		// 审批流程类型
		public static Map<Integer, String> reviewFlowTypeMap = new TreeMap<Integer, String>();
		/**
		 * 添加产品类型map TODO 根据不同的批发商过滤显示
		 */
		static {
			productTypeMap.put(Context.ProductType.PRODUCT_SINGLE, "单团");
			productTypeMap.put(Context.ProductType.PRODUCT_LOOSE, "散拼");
			productTypeMap.put(Context.ProductType.PRODUCT_STUDY, "游学");
			productTypeMap.put(Context.ProductType.PRODUCT_BIG_CUSTOMER, "大客户");
			productTypeMap.put(Context.ProductType.PRODUCT_FREE, "自由行");
			productTypeMap.put(Context.ProductType.PRODUCT_VISA, "签证");
			productTypeMap.put(Context.ProductType.PRODUCT_AIR_TICKET, "机票");
			productTypeMap.put(Context.ProductType.PRODUCT_CRUISE, "游轮");
			productTypeMap.put(Context.ProductType.PRODUCT_HOTEL, "酒店");
			productTypeMap.put(Context.ProductType.PRODUCT_ISLAND, "海岛游");
		}

		/**
		 * 添加审批流程信息 TODO 根据批发商不同进行过滤
		 */
		static {
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_REFUND, "退款审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_INVOICE, "发票申请");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN, "退票审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT, "还签证收据");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY, "签证借款审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT, "签证押金转担保");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND, "退签证押金");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_EXIT_GROUP, "退团审批");
			reviewFlowTypeMap.put(Context.REBATES_FLOW_TYPE, "返佣审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_CHANGE_PRICE, "改价审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, "转团审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY, "转款审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT, "还签证押金收据");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_VISA_CHANGE, "机票改签审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_STOCK, "预算成本审批");
//			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_OPER_REFUND, "新行者计调退款");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_ACTUAL_COST, "实际成本审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_PAYMENT, "成本付款审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE, "散拼优惠审批");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_BORROWMONEY, "借款审批");
//			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY, "新行者签证借款");
			reviewFlowTypeMap.put(Context.REVIEW_FLOWTYPE_GUARANTEE, "担保变更审批");
		}
		
		/**
		 * 特殊需求配置代码 0 --无需审批
		 */
		public static final Integer SPECIAL_NEED_CODE_NEED_NO_REVIEW=0;
		/**
		 * 特殊需求配置代码 1 --可选起点
		 */
		public static final Integer SPECIAL_NEED_CODE_JUMP_TASK=1;
		/**
		 * 特殊需求配置代码 2 --多次申请
		 */
		public static final Integer SPECIAL_NEED_CODE_MULTI_APPLY=2;
		/**
		 *  特殊需求配置代码 3 --付款金额等于实际成本金额
		 */
		public static final Integer SPECIAL_NEED_CODE_PAYMENT_EQUALS_COST=3;

		/**
		 * 特殊需求默认值--越级审批  默认值：不允许
		 */
		public static final Integer SPECIAL_NEED_DEFAULT_VALUE_JUMP_TASK= 0;

		/**
		 * 特殊需求默认值--自动审批通过  默认值：允许
		 */
		public static final Integer SPECIAL_NEED_DEFAULT_VALUE_AUTO_APPROVE=1;
		
		/**
		 * 特殊需求默认值--发起多次审批  默认值：允许
		 */		
		public static final Integer SPECIAL_NEED_DEFAULT_VALUE_MULTI_APPLY=1;
		
		/**
		 * 无需审批的流程模型id，全系统采用默认的无需审批的流程配置
		 */
		public static final String PROCESS_MODEL_ID_NEED_NO_REVIEW="nothing";

		/**
		 * 默认的条件审批职务id ："0"
		 */
		public static final String DEFAULT_CONDITION_JOB_ID="0";

		/**
		 * 默认的条件审批职务id ："0"
		 */
		public static final String DEFAULT_CONDITION_JOB_NAME="计调";

		/**
		 * 审批相关对象类型：返佣渠道
		 */
		public static final Integer REVIEW_RELATED_OBJECT_TYPE_REBATE_AGENT=1;

		/**
		 * 审批相关对象类型：返佣供应商
		 */
		public static final Integer REVIEW_RELATED_OBJECT_TYPE_REBATE_SUPPLIER=2;
}
