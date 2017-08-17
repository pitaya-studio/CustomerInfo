package com.trekiz.admin.common.config;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public abstract class Context {

	/**
	 * 系统中使用的编码格式
	 */
	public static final String ENCODING_GBK = "GBK";

	/**
	 *字典表中 类型  是否
	 */
	public static final String DICT_TYPE_YESNO = "yes_no";

	/**
	 *字典表中 类型  是
	 */
	public static final String DICT_TYPE_YES = "1";

	/**
	 *字典表中 类型  否
	 */
	public static final String DICT_TYPE_NO = "0";

	/**
	 *字典表中 签证人员类型
	 */
	public static final String DICT_TYPE_VISAPERSONALTYPE = "visa_personnaltype";

	/**
	 *字典表中  签证类型
	 */
	public static final String DICT_TYPE_VISATYPE = "visa_type";

	/**
	 *字典表中  环球签证类型
	 */
	public static final String DICT_TYPE_NEW_VISATYPE = "new_visa_type";

	/** 全部订单 */
	public static final String ORDER_PAYSTATUS_ALL = "0";
	/** 全款未支付   预订后没有支付 */
	public static final String ORDER_PAYSTATUS_WZF = "1";
	/** 订金未支付   订金占位后没有支付 */
	public static final String ORDER_PAYSTATUS_DJWZF = "2";
	/** 已占位   非订金占位 */
	public static final String ORDER_PAYSTATUS_YZW = "3";
	/** 订金已经支付   */
	public static final String ORDER_PAYSTATUS_YZFDJ = "4";
	/** 已经支付 */
	public static final String ORDER_PAYSTATUS_YZF = "5";
	/** 计调占位 */
	public static final String ORDER_PAYSTATUS_OP = "7";
	/** 财务确认占位 */
	public static final String ORDER_PAYSTATUS_CW = "8";
	/** 已撤销占位 */
	public static final String ORDER_PAYSTATUS_CW_CX = "9";
	/** 已经取消订单 */
	public static final String ORDER_PAYSTATUS_YQX = "99";
	/** 查看状态-正向平台同步 */
	public static final String ORDER_PAYSTATUS_SYNC_CHECK = "101";
	/** 已经删除订单 */
	public static final String ORDER_PAYSTATUS_DEL = "111";
	/** 财务订单 */
	public static final String ORDER_PAYSTATUS_FINANCE = "199";

	public static final String ORDER_PAYTYPE = "offlineorder_pay_type";

	/** 海岛游订单状态：待确认 */
	public static final Integer ISLAND_ORDER_STATUS_DQR = 1;
	/** 海岛游订单状态：已确认 */
	public static final Integer ISLAND_ORDER_STATUS_YQR = 2;
	/** 海岛游订单状态：已取消 */
	public static final Integer ISLAND_ORDER_STATUS_YQX = 3;

	/** 酒店订单状态：待确认 */
	public static final Integer HOTEL_ORDER_STATUS_DQR = 1;
	/** 酒店订单状态：已确认 */
	public static final Integer HOTEL_ORDER_STATUS_YQR = 2;
	/** 酒店订单状态：已取消 */
	public static final Integer HOTEL_ORDER_STATUS_YQX = 3;


	/** 支付订单达账状态：已撤销   */
	public static final Integer ORDERPAY_ACCOUNT_STATUS_YCX = 0;
	/** 支付订单达账状态：已达账   */
	public static final Integer ORDERPAY_ACCOUNT_STATUS_YDZ = 1;
	/** 支付订单达账状态：已驳回   */
	public static final Integer ORDERPAY_ACCOUNT_STATUS_YBH = 2;

	/**
	 *支付款类型 --支付全款
	 */
	public static final String ORDER_ORDERTYPE_ZFQK = "1";

	/**
	 *支付款类型 --支付尾款
	 */
	public static final String ORDER_ORDERTYPE_ZFWK = "2";

	/**
	 *支付款类型 --交订金
	 */
	public static final String ORDER_ORDERTYPE_ZFDJ = "3";

	/**
	 * 出发城市
	 */
	public static final String OUT_AREA = "out_area";
	/**
	 * 离境城市
	 */
	public static final String FROM_AREA = "from_area";

	/**
	 * 旅游类型
	 */
	public static final String TRAVEL_TYPE = "travel_type";

	/**
	 * 产品等级
	 */
	public static final String PRODUCT_LEVEL = "product_level";

	/**
	 * 产品类型
	 */
	public static final String PRODUCT_TYPE = "product_type";

	/**
	 * 交通方式
	 */
	public static final String TRAFFIC_MODE = "traffic_mode";

	/**
	 * 航空公司名称
	 */
	public static final String TRAFFIC_NAME = "traffic_name";

	/**
	 * 付款方式
	 */
	public static final String PAY_TYPE = "pay_type";

	/**
	 * 总社（实际为渠道商）
	 */
	public static final String USER_TYPE_MAINOFFICE = "1";

	/**
	 * 接待社
	 */
	public static final String USER_TYPE_RECEPTION = "3";

	/**
	 * 自定义目标区域
	 */
	public static final String DEFINE_AREA = "sys_area";

	/**
	 * 开票方式
	 */
	public static final String INVOICE_MODE = "invoice_mode";

	/**
	 * 开票类型
	 */
	public static final String INVOICE_TYPE = "invoice_type";

	/**
	 * 开票项目
	 */
	public static final String INVOICE_SUBJECT = "invoice_subject";

	/**
	 * 占位类型 如果为0 表示是普通的占位
	 */
	public static final String PLACEHOLDERTYPE_ZW = "0";

	/**
	 * 占位类型 如果为1 表示是切位
	 */
	public static final String PLACEHOLDERTYPE_QW = "1";

	/**
	 * 产品编号类型
	 */
	public static final Integer PRODUCT_NUM_TYPE = 1;

	/**
	 * 订单编号规则
	 */
	public static final Integer ORDER_NUM_TYPE = 2;

	/**
	 * 团号规则
	 */
	public static final Integer GROUP_NUM_TYPE = 3;

	/**
	 * 渠道商等级编号规则
	 */
	public static final Integer PRICESTRATEGY_NUM_TYPE = 4;

	/**
	 * 产品草稿状态
	 */
	public static final String PRODUCT_TEMP_STATUS = "1";
	/**
	 * 产品上架状态
	 */
	public static final String PRODUCT_ONLINE_STATUS = "2";
	/**
	 * 产品下架状态
	 */
	public static final String PRODUCT_OFFLINE_STATUS = "3";

	/** 删除标记：正常 */
	public static final String DEL_FLAG_NORMAL = "0";
	/** 删除标记：删除 */
	public static final String DEL_FLAG_DELETE = "1";
	/** 删除标记：审核 */
	public static final String DEL_FLAG_AUDIT = "2";
	/** 删除标记：草稿 */
	public static final String DEL_FLAG_TEMP = "3";

	/** 角色类型：系统管理员 */
	public static final String ROLE_TYPE_MANAGER = "9";
	/** 角色类型：销售 */
	public static final String ROLE_TYPE_SALES = "1";
	/** 角色类型：销售主管 */
	public static final String ROLE_TYPE_SALES_EXECUTIVE = "2";
	/** 角色类型：计调 */
	public static final String ROLE_TYPE_OP = "3";
	/** 角色类型：计调主管 */
	public static final String ROLE_TYPE_OP_EXECUTIVE = "4";
	/** 角色类型：其他 */
	public static final String ROLE_TYPE_OTHER = "0";

	/** 订单类型：单团 */
	public static final String ORDER_STATUS_SINGLE = "1";
	/** 订单类型：散拼 */
	public static final String ORDER_STATUS_LOOSE = "2";
	/** 订单类型：游学 */
	public static final String ORDER_STATUS_STUDY = "3";
	/** 订单类型：大客户 */
	public static final String ORDER_STATUS_BIG_CUSTOMER = "4";
	/** 订单类型：自由行 */
	public static final String ORDER_STATUS_FREE = "5";
	/** 订单类型：签证 */
	public static final String ORDER_STATUS_VISA = "6";
	/** 订单类型：机票 */
	public static final String ORDER_STATUS_AIR_TICKET = "7";
	/** 订单类型：游轮 */
	public static final String ORDER_STATUS_CRUISE = "10";
	/** 订单类型：酒店 */
	public static final String ORDER_STATUS_HOTEL = "11";
	/** 订单类型：海岛游 */
	public static final String ORDER_STATUS_ISLAND = "12";
	/**
	 * 机票产品 类型 单程
	 */
	public static final String AIRTICLKET_TYPE_DANCHEN = "3";
	/**
	 * 机票产品 类型 往返
	 */
	public static final String AIRTICLKET_TYPE_WANGFAN = "2";
	/**
	 * 机票产品 类型 多段
	 */
	public static final String AIRTICLKET_TYPE_DUODUAN = "1";

	/**
	 * 产品分类 单团
	 */
	public static final String PRODUCT_TYPE_DAN_TUAN = "1";

	/**
	 * 产品分类 散拼
	 */
	public static final String PRODUCT_TYPE_SAN_PIN = "2";

	/**
	 * 产品分类 游学
	 */
	public static final String PRODUCT_TYPE_YOU_XUE = "3";

	/**
	 * 产品分类 大客户
	 */
	public static final String PRODUCT_TYPE_DA_KE_HU = "4";

	/**
	 * 产品分类 自由行
	 */
	public static final String PRODUCT_TYPE_ZI_YOU_XING = "5";

	/**
	 * 产品分类 签证
	 */
	public static final String PRODUCT_TYPE_QIAN_ZHENG = "6";

	/**
	 * 产品分类 机票"7"
	 */
	public static final Integer PRODUCT_TYPE_AIRTICKET = 7;


	// 产品种类 单团
	public static final String ACTIVITY_KINDS_DT = "1";

	// 产品种类 散拼
	public static final String ACTIVITY_KINDS_SP = "2";

	// 产品种类 游学
	public static final String ACTIVITY_KINDS_YX = "3";

	// 产品种类 大客户
	public static final String ACTIVITY_KINDS_DKH = "4";

	// 产品种类 自由行
	public static final String ACTIVITY_KINDS_ZYX = "5";

	// 产品种类 签证
	public static final String ACTIVITY_KINDS_QZ = "6";

	// 产品种类 机票
	public static final String ACTIVITY_KINDS_JP = "7";

	// 产品种类 游轮
	public static final String ACTIVITY_KINDS_YL = "10";

	/**
	 * 签证订单状态：订单已完成，但未支付。
	 */
	public static final String VISA_ORDER_PAYSTATUS_NOPAY = "0";
	/**
	 * 签证订单状态：订单已完成，且已经支付。
	 */
	public static final String VISA_ORDER_PAYSTATUS_PAYED = "1";
	/**
	 * 签证订单状态：订单已完成，但已被取消。
	 */
	public static final String VISA_ORDER_PAYSTATUS_CANCEL = "2";

	/**
	 * 签证订单状态：订单尚未完成，正在预订中，在完成前该订单不能使用。
	 */
	public static final String VISA_ORDER_PAYSTATUS_DOING = "100";

	/**
	 * 签证状态：未送签
	 */
	public static String VISA_STATUTS_NO_TO = "0";
	/**
	 * 签证状态：送签
	 */
	public static String VISA_STATUTS_TO = "1";
	/**
	 * 签证状态：约签
	 */
	public static String VISA_STATUTS_PROMISE = "2";
	/**
	 * 签证状态：出签
	 */
	public static String VISA_STATUTS_FINISH = "3";
	/**
	 * 签证状态：申请撤签
	 */
	// public static String VISA_STATUTS_TOCANCAL = "4";
	/**
	 * 签证状态：已撤签
	 */
	public static String VISA_STATUTS_CANCAL = "5";
	/**
	 * 签证状态：撤签失败
	 */
	// public static String VISA_STATUTS_CANCALFLUNK= "6";
	/**
	 * 签证状态：拒签
	 */
	public static String VISA_STATUTS_REFUSE = "7";

	/**
	 * 签证状态： 续补资料
	 */
	public static String VISA_STATUTS_RESOURCENEEDED = "9";

	/**
	 * 证件类型：身份证
	 */
	public static String PAPERSTYPE_1 = "1";
	/**
	 * 证件类型：护照
	 */
	public static String PAPERSTYPE_2 = "2";
	/**
	 * 证件类型：警官证
	 */
	public static String PAPERSTYPE_3 = "3";
	/**
	 * 证件类型：军官证
	 */
	public static String PAPERSTYPE_4 = "4";
	/**
	 * 证件类型：其他
	 */
	public static String PAPERSTYPE_5 = "5";

	/**
	 * 出境游code
	 */
	public static final String FREE_TRAVEL_FOREIGN = "100000";
	/**
	 * 国内游code
	 */
	public static final String FREE_TRAVEL_INLAND = "200000";

	/**
	 * 出境游中文
	 */
	public static final String FREE_TRAVEL_FOREIGN_CHINA = "出境游";

	/**
	 * 国内游中文
	 */
	public static final String FREE_TRAVEL_INLAND_CHINA = "国内游";

	public static final String PASSPORT_TYPE = "passport_type";

	/**
	 * 产品（机票、签证）可见范围 1 - 全部可见
	 */
	public static final int ACTIVITY_SCOPE_ALL = 1;

	/**
	 * 产品（机票、签证）可见范围 2 - 内部可见
	 */
	public static final int ACTIVITY_SCOPE_COMPANY = 2;

	/**
	 * 产品（机票、签证）可见范围 3 - 渠道可见
	 */
	public static final int ACTIVITY_SCOPE_AGENT = 3;

	/**
	 * 树形机场ID前缀
	 */
	public static final String AIR_PREFIX = "air";

	/**
	 * 退款状态 1 - 待审核
	 */
	public static int REFUND_STATUS_PROCESS = 1;
	/**
	 * 退款状态 2 - 已驳回
	 */
	public static int REFUND_STATUS_REJECT = 0;
	/**
	 * 退款状态 3 - 审核成功
	 */
	public static int REFUND_STATUS_PASS = 2;
	/**
	 * 退款状态 4 - 操作完成
	 */
	public static int REFUND_STATUS_DONE = 3;

	/**
	 * 团号分隔符
	 */
	public static final String GROUPCODE_SPLIT_FLAG = "-";
	/** 产品类型 */
	public final static String ORDER_TYPE = "order_type";
	/** 款项类型 */
	public final static String MONEY_TYPE = "money_type";
	/** 锁表标记 */
	public final static Integer LOCK_ORDER= 1;//锁定
	public final static Integer UNLOCK_ORDER=0;//正常
	public final static Integer CANCEL_STATUS=2;//取消标记

	/**游客退团，转团操作等状态标记     0 正常   */
	public final static Integer TRAVELER_DELFLAG_NORMAL=0;
	/**游客退团，转团操作等状态标记     1 删除   */
	public final static Integer TRAVELER_DELFLAG_DEL=1;
	/**游客退团，转团操作等状态标记     2 退团审核中  */
	public final static Integer TRAVELER_DELFLAG_EXIT=2;
	/**游客退团，转团操作等状态标记     3 已退团   */
	public final static Integer TRAVELER_DELFLAG_EXITED=3;
	/**游客退团，转团操作等状态标记     4 转团审核中   */
	public final static Integer TRAVELER_DELFLAG_TURNROUND=4;
	/**游客退团，转团操作等状态标记     5 已转团   */
	public final static Integer TRAVELER_DELFLAG_TURNROUNDED=5;
	/**游客改签，改签操作等状态标记   */
	public final static Integer TRAVELER_DELFLAG_PLANEREVIEW = 6;



	/** 付款方式类型：全款 */
	public final static Integer MONEY_TYPE_QK = 1;
	/** 付款方式类型：尾款 */
	public final static Integer MONEY_TYPE_WK = 2;
	/** 付款方式类型：订金 */
	public final static Integer MONEY_TYPE_DJ = 3;
	/** 付款方式类型：达账 */
	public final static Integer MONEY_TYPE_DZ = 4;
	/** 付款方式类型：实收 */
	public final static Integer MONEY_TYPE_YS = 5;
	/** 付款方式类型：返佣*/
	public final static Integer MONEY_TYPE_FY = 6;
	/** 付款方式类型：收押金 */
	public final static Integer MONEY_TYPE_SYJ = 7;
	/** 付款方式类型：改价 */
	public final static Integer MONEY_TYPE_GJ = 8;
	/** 付款方式类型：转款 */
	public final static Integer MONEY_TYPE_ZK = 9;
	/** 付款方式类型：担保 */
	public final static Integer MONEY_TYPE_DB = 10;
	/** 付款方式类型：退款 */
	public final static Integer MONEY_TYPE_TK = 11;
	/** 付款方式类型：借款 */
	public final static Integer MONEY_TYPE_JK = 12;
	/** 付款方式类型：应收 */
	public final static Integer MONEY_TYPE_YSH = 13;
	/** 付款方式类型：游客结算价 */
	public final static Integer MONEY_TYPE_JSJ = 14;
	/** 付款方式类型：退押金 */
	public final static Integer MONEY_TYPE_TYJ = 15;
	/** 付款方式类型：已收押金 */
	public final static Integer MONEY_TYPE_YSYJ = 16;
	/** 付款方式类型：已退押金 */
	public final static Integer MONEY_TYPE_YTYJ = 17;
	/** 付款方式类型：达账押金 */
	public final static Integer MONEY_TYPE_DZYJ = 18;
	/** 付款方式类型：原始应收 */
	public final static Integer MONEY_TYPE_YSYSH = 19;
	/** 付款方式类型：原始订金 */
	public final static Integer MONEY_TYPE_YSDJ = 20;
	/** 付款方式类型：原始（游客）结算价 */
	public final static Integer MONEY_TYPE_YSJSJ = 21;
	/** 付款方式类型：联运费 */
	public final static Integer MONEY_TYPE_LYF = 22;
	/** 付款方式类型：税费 */
	public final static Integer MONEY_TYPE_SF = 23;
	/**付款方式类型 返佣费用 */
	public final static Integer MONEY_TYPE_FYFY = 23;
	/** 款项类型  退团后应收  */
	public final static Integer MONEY_TYPE_TTHYS = 25;
	/** 款项类型  退团退款  */
	public final static Integer MONEY_TYPE_TTTK = 26;
	/** 款项类型  扣减金额（游客表字段）  */
	public final static Integer MONEY_TYPE_KJJE = 27;
	/**付款方式类型 成本价 */
	public final static Integer MONEY_TYPE_CBJ = 28;
	/** 付款方式类型 询价报价 */
	public final static Integer MONEY_TYPE_ESTIMATE = 30;
	/** 金额类型 ： 服务费(31) */
	public final static Integer MONEY_TYPE_CHARGE = 31;
	/**付款方式类型 财务撤消状态 */
	public final static Integer MONEY_TYPE_CANCEL = 101;
	/**付款方式类型 财务驳回状态 */
	public final static Integer MONEY_TYPE_REJECT = 102;

	/** 订单类型： 单团 */
	public final static Integer ORDER_TYPE_DT = 1;
	/** 订单类型： 散拼 */
	public final static Integer ORDER_TYPE_SP = 2;
	/** 订单类型： 游学 */
	public final static Integer ORDER_TYPE_YX = 3;
	/** 订单类型： 大客户 */
	public final static Integer ORDER_TYPE_DKH = 4;
	/** 订单类型： 自由行 */
	public final static Integer ORDER_TYPE_ZYX = 5;
	/** 订单类型： 签证 */
	public final static Integer ORDER_TYPE_QZ = 6;
	/** 订单类型： 机票 */
	public final static Integer ORDER_TYPE_JP = 7;
	/** 订单类型： 其他 */
	public final static Integer ORDER_TYPE_OT = 9;
	/**订单类型：全部（包含：单团，散拼，游学，大客户，自由行，签证，机票），主要是针对列表页的查询条件*/
	public final static Integer ORDER_TYPE_ALL = 0;
	/** 订单类型： 游轮 */
	public final static Integer ORDER_TYPE_CRUISE = 10;
	/** 订单类型： 酒店 */
	public final static Integer ORDER_TYPE_HOTEL = 11;
	/** 订单类型： 海岛游 */
	public final static Integer ORDER_TYPE_ISLAND = 12;

	/** 流水表业务类型： 订单 */
	public final static Integer MONEY_BUSINESSTYPE_ORDER = 1;
	/** 流水表业务类型： 游客 */
	public final static Integer MONEY_BUSINESSTYPE_TRAVELER = 2;
	/** 流水表业务类型：询价报价 */
	public final static Integer MONEY_BUSINESSTYPE_ESTIMATE = 3;
	/** 流水表业务类型：团期 */
	public final static Integer MONEY_BUSINESSTYPE_ESTIMATETOP = 4;

	/**
	 * 运控成本录入： 0: 审核失败 1: 待审核 2: 审核通过 4: 待录入 5: 已取消
	 */
	public final static Integer REVIEW_COST_FAIL = 0;
	public final static Integer REVIEW_COST_WAIT = 1;
	public final static Integer REVIEW_COST_PASS = 2;
	public final static Integer REVIEW_COST_NEW = 4;
	public final static Integer REVIEW_COST_CANCEL = 5;

	/**
	 * 'REVIEW.STATUS:审核状态 0: 已驳回 (审核失败); 1: 待审核; 2: 审核成功; 3: 操作完成
	 * (审核成功后，操作员完成退款退团等操作), 4:取消申请',
	 */
	public final static Integer REVIEW_STATUS_FAIL = 0;
	public final static Integer REVIEW_STATUS_WAIT = 1;
	public final static Integer REVIEW_STATUS_PASS = 2;
	public final static Integer REVIEW_STATUS_DONE = 3;
	public final static Integer REVIEW_STATUS_CANCEL = 4;

	/** 审核流程类型：退款1 */
	public final static Integer REVIEW_FLOWTYPE_REFUND = 1;
	/** 审核流程类型：退票3 */
	public final static Integer REVIEW_FLOWTYPE_AIRTICKET_RETURN = 3;
	/** 审核流程类型：签证押金转担保6 */
	public final static Integer REVIEW_FLOWTYPE_DEPOSITTOWARRANT = 6;
	/** 审核流程类型：退签证押金7 */
	public final static Integer REVIEW_FLOWTYPE_DEPOSITE_REFUND = 7;
	/** 审核流程类型：退团 8 */
	public final static Integer REVIEW_FLOWTYPE_EXIT_GROUP = 8;
	/** 审核流程类型：返佣 9 */
	public final static Integer REBATES_FLOW_TYPE = 9;
	/** 审核流程类型：改价10 */
	public final static Integer REVIEW_FLOWTYPE_CHANGE_PRICE = 10;
	/** 审核流程类型：转团 11 */
	public final static Integer REVIEW_FLOWTYPE_TRANSFER_GROUP = 11;
	/** 审核流程类型：转款 12 */
	public final static Integer REVIEW_FLOWTYPE_TRANSFER_MONEY = 12;
	/** 审核流程类型：还签证收据 4 */
	public final static Integer REVIEW_FLOWTYPE_VISA_RETURNRECEIPT = 4;
	/** 审核流程类型：签证借款 5 */
	public final static Integer REVIEW_FLOWTYPE_VISA_BORROWMONEY = 5;
	/** 审核流程类型：还签证押金收据 13 */
	public final static Integer REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT = 13;
	/** 发票申请 2 */
	public final static Integer REVIEW_FLOWTYPE_INVOICE = 2;
	/** 改签 14 */
	public final static Integer REVIEW_FLOWTYPE_VISA_CHANGE = 14;
	/** 改签 14 */
	public final static String  REVIEW_FLOWTYPE_AIRTICKET_CHANGE = "14";
	/** 预算成本审核 15 */
	public final static Integer REVIEW_FLOWTYPE_STOCK = 15;
	/** 审核流程类型：新行者计调退款16 */
	public final static Integer REVIEW_FLOWTYPE_OPER_REFUND = 16;

	/** 审核流程类型：实际成本审核 17 */
	public final static Integer REVIEW_FLOWTYPE_ACTUAL_COST = 17;
	/** 审核流程类型：付款审核 18 */
	public final static Integer REVIEW_FLOWTYPE_PAYMENT = 18;
	/** 审核流程类型：借款申请 19 */
	public final static Integer REVIEW_FLOWTYPE_BORROWMONEY = 19;

	/** 审核流程类型：新行者签证借款(废弃) 20 */
	public final static Integer REVIEW_FLOWTYPE_VISA_XINXINGZHEBORROWMONEY = 20;

	/** 审核流程类型：散拼优惠申请 21*/
	public final static Integer REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE = 21;

	/** 审核流程类型：担保变更审批 22*/
	public final static Integer REVIEW_FLOWTYPE_GUARANTEE = 22;

	/** 审核标记：0：无效 */
	public final static boolean REVIEW_ACTIVE_INEFFECTIVE = false;
	/** 审核标记：1：有效 */
	public final static boolean REVIEW_ACTIVE_EFFECTIVE = true;

	public static final String PAY_MENT_TYPE = "payment_type";

	/** 订单占位方式：订金占位 */
	public static final String PAY_MODE_EARNEST = "1";
	/** 订单占位方式：预占位 */
	public static final String PAY_MODE_BEFOREHAND = "2";
	/** 订单占位方式：全款占位 */
	public static final String PAY_MODE_FULL = "3";
	/** 订单占位方式：资料占位 */
	public static final String PAY_MODE_INFO = "4";
	/** 订单占位方式：担保占位 */
	public static final String PAY_MODE_GUARANTY = "5";
	/** 订单占位方式：确认单占位 */
	public static final String PAY_MODE_CODE = "6";
	/** 订单占位方式：计调确认占位 */
	public static final String PAY_MODE_OP = "7";
	/** 订单占位方式：财务确认占位  */
	public static final String PAY_MODE_CW = "8";

	/** 原始游客结算价 */
	public static final Integer MONEY_TYPE_21 = 21;
	/** 应收价 */
	public static final Integer MONEY_TYPE_13 = 13;
	/** 业务类型 订单 */
	public static final Integer BUSINESS_TYPE_ORDER = 1;
	/** 业务类型 旅客 */
	public static final Integer BUSINESS_TYPE_TRAVELER = 2;
	/** 业务类型 询价报价 */
	public static final Integer BUSINESS_TYPE_QUOTED = 3;
	/** 业务类型 团期 */
	public static final Integer BUSINESS_TYPE_GROUP = 4;
	/** 游客类型 1 成人 */
	public static final int PERSON_TYPE_ADULT = 1;
	/** 游客类型 2 儿童 */
	public static final int PERSON_TYPE_CHILD = 2;
	/** 游客类型 3 特殊 人群 */
	public static final int PERSON_TYPE_SPECIAL = 3;
	/** （公告/消息）msg 状态：保存 */
	public static final Integer MESSAGE_STATUS_SAVE = 0;
	/** （公告/消息）msg 状态：发布 */
	public static final Integer MESSAGE_STATUS_ISSUE = 1;
	/** （公告/消息）msg 状态：删除 */
	public static final Integer MESSAGE_STATUS_DEL = 2;
	/** （公告/消息）msg 状态：过期 */
	public static final Integer MESSAGE_STATUS_PAST = 3;

	/** （公告/消息）msg 分类：全站公告 */
	public static final Integer MSG_TYPE_ALL = 1;
	/** （公告/消息）msg 分类：部门公告 */
	public static final Integer MSG_TYPE_PART = 2;
	/** （公告/消息）msg 分类：渠道公告 */
	public static final Integer MSG_TYPE_AGINE = 3;
	/** （公告/消息）msg 分类：约签 */
	public static final Integer MSG_TYPE_ENGAGE = 4;
	/** （公告/消息）msg 分类：消息 */
	public static final Integer MSG_TYPE_MESSAGE = 5;
	/**  （公告/消息）msg 分类：财务消息 */
	public static final Integer  MSG_TYPE_FINANCE = 6;
	/**  （公告/消息/提醒）msg 分类：还款提醒 */
	public static final Integer  MSG_TYPE_REFUND = 7;
	/** （公告/消息）msg 消息未读 */
	public static final Integer MSG_IFREAD_NO = 0;
	/** （公告/消息）msg 消息已读 */
	public static final Integer MSG_IFREAD_YES = 1;
	/**
	 * （公告/消息）约签缓存key标记 标记结构： CACHE_NAME_companyId_userID (key标记 _ 约签发布公司ID _
	 * 约签接收人ID)
	 */
	public final static String CACHE_NAME = "cacheengage_";
	/** （公告/消息）控制公告正文缩略长度为150 */
	public final static int STR_LENGTH = 150;
	public static final String log_type_product = "1"; // 产品
	public static final String log_type_product_name = "产品"; // 产品名
	public static final String log_type_control = "2"; // 运控
	public static final String log_type_control_name = "运控"; // 运控名
	public static final String log_type_schedule = "3"; // 预定
	public static final String log_type_schedule_name = "预定"; // 预定名
	public static final String log_type_orderform = "4"; // 订单
	public static final String log_type_orderform_name = "订单"; // 订单名
	public static final String log_type_financial = "5"; // 财务
	public static final String log_type_financial_name = "财务"; // 财务名
	public static final String log_type_price = "6"; // 询价
	public static final String log_type_price_name = "询价"; // 询价名
	public static final String log_type_examine = "7"; // 审核
	public static final String log_type_examine_name = "审核"; // 审核名
	public static final String log_type_notice = "8"; // 公告
	public static final String log_type_notice_name = "公告"; // 公告名
	/** 财务消息_订单  */
	public static final String MSG_FINANCE_ORDER = "财务消息_订单";
	/** 应收到期预警_账期 */
	public static final String MSG_RECEIVE_PAY = "应收到期预警_账期";
	// 操作状态
	public static final String log_state_add = "1"; // 增加
	public static final String log_state_up = "2"; // 修改
	public static final String log_state_del = "3"; // 删除

	/** 平台类型：批发商 */
	public static final Integer PLAT_TYPE_PF = 0;
	/** 平台类型：地接社 */
	public static final Integer PLAT_TYPE_DJ = 1;
	/** 平台类型：渠道商 */
	public static final Integer PLAT_TYPE_QD = 2;
	/** 平台类型： 供应商*/
	public static final Integer PLAT_TYPE_SUP = 3;

	/** 结算方式： 即时结算 */
	public final static Integer PAYMENT_TYPE_JS = 1;
	/** 结算方式： 按月结算 */
	public final static Integer PAYMENT_TYPE_AY = 2;
	/** 结算方式： 担保结算 */
	public final static Integer PAYMENT_TYPE_DB = 3;
	/** 结算方式： 后续费 */
	public final static Integer PAYMENT_TYPE_HXF = 4;
	/** 订单占位情况： 0 未占位 */
	public final static Integer ORDER_PLACEHOLDER_NO = 0;
	/** 订单占位情况： 1 已占位 */
	public final static Integer ORDER_PLACEHOLDER_YES = 1;
	/** 订单占位情况： 2 获取占位信息错误 */
	public final static Integer ORDER_PLACEHOLDER_ERROR = 2;
	/** 操作返回结果 code */
	public final static String RESULT = "result";
	/** 操作返回结果信息描述 */
	public final static String MESSAGE = "message";

	/** 驳回操作，是否影响占位：0-保持占位 */
	public final static String REJECT_NO_PLACEHOLDER = "0";
	/** 驳回操作，是否影响占位：1-退回占位 */
	public final static String REJECT_YES_PLACEHOLDER = "1";
	/** 审核流程对应情况 */
	private static Map<String, String> REVIEW_FLOW = new HashMap<String, String>();
	/** 审核部分流程互斥处理 */
	private static Map<String, String> REVIEW_MUTEX = new HashMap<String, String>();

	/** 支付方式：支票 */
	public static final String PAYTYPE_CHECK = "1";
	/** 支付方式：现金 */
	public static final String PAYTYPE_CASH = "3";
	/** 支付方式：汇款*/
	public static final String PAYTYPE_REMIT = "4";
	/** 支付方式：银行转账 */
	public static final String PAYTYPE_YHZZ = "6";
	/** 支付方式：汇票 */
	public static final String PAYTYPE_DRAFT = "7";
	/** 支付方式：POS机刷卡 */
	public static final String PAYTYPE_POS = "8";
	/** 支付方式：因公支付宝 */
	public static final String PAYTYPE_ALIPAY = "9";

	static {
		// 退款流程互斥，与退款流程互斥的流程代号逗号分隔，
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_REFUND.toString(), "1,16");
		// 与发票申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_INVOICE.toString(), "");
		// 与退票申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString(), "");
		// 与还签证收据申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString(), "");
		// 与签证借款申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString(), "");
		// 与签证押金转担保申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_DEPOSITTOWARRANT.toString(), "");
		// 与退签证押金申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_DEPOSITE_REFUND.toString(), "");
		// 与退团申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_EXIT_GROUP.toString(), "1,16,8,9,10,11");
		// 与返佣申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REBATES_FLOW_TYPE.toString(), "8,9,11");
		// 与改价申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_CHANGE_PRICE.toString(), "");
		// 与转团申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_TRANSFER_GROUP.toString(), "8,9,11");
		// 与转款申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_TRANSFER_MONEY.toString(), "12");
		// 与还签证押金收据申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT.toString(), "");
		// 与改签申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_VISA_CHANGE.toString(), "");
		// 与运控成本审核申请流程互斥的流程代号逗号分隔
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_STOCK.toString(), "");
		// 新行者计调退款流程互斥，与退款流程互斥的流程代号逗号分隔，
		REVIEW_MUTEX.put(REVIEW_FLOWTYPE_OPER_REFUND.toString(), "1,16");
		// 添加流程对应
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_REFUND.toString(), "退款");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_INVOICE.toString(), "发票申请");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString(), "退票");
//		REVIEW_FLOW.put(REVIEW_FLOWTYPE_OPER_REFUND.toString(), "退款");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString(), "还签证收据");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString(), "签证借款");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_DEPOSITTOWARRANT.toString(), "签证押金转担保");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_DEPOSITE_REFUND.toString(), "退签证押金");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_EXIT_GROUP.toString(), "退团");
		REVIEW_FLOW.put(REBATES_FLOW_TYPE.toString(), "返佣");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_CHANGE_PRICE.toString(), "改价");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_TRANSFER_GROUP.toString(), "转团");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_TRANSFER_MONEY.toString(), "转款");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT.toString(), "还签证押金收据");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_VISA_CHANGE.toString(), "改签");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_STOCK.toString(), "预算成本审核");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_ACTUAL_COST.toString(), "实际成本审批");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_PAYMENT.toString(), "成本付款审批");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_BORROWMONEY.toString(), "借款申请");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString(), "散拼优惠审批");
		REVIEW_FLOW.put(REVIEW_FLOWTYPE_GUARANTEE.toString(), "担保变更审批");

	}

	public static Map<String, String> getREVIEW_MUTEX() {
		return REVIEW_MUTEX;
	}

	public static Map<String, String> getREVIEW_FLOW() {
		return REVIEW_FLOW;
	}

	/**
	 * */
	public final static Integer REFUNDMONEYTYPE = 1;

	/* 销售职务类型 */
	public final static Integer JOB_SALES = 1;
	/* 销售经理职务类型 */
	public final static Integer JOB_SALES_Manager = 2;
	/* 计调职务类型 */
	public final static Integer JOB_OPERATOR = 3;
	/* 计调经理职务类型 */
	public final static Integer JOB_OPEATOR_Manager = 4;
	/* 其它职务类型 */
	public final static Integer JOB_OTHER = 0;
	/** 开始时间 */
	public final static String DEFAULT_BEGIN_TIME = "1990-01-01 00:00:00";
	/** 结束时间 */
	public final static String DEFAULT_END_TIME = "9999-12-30 23:59:59";
	/** 互斥操作结果code: 0游客、团队均无互斥 1游客无互斥,团队互斥 2团队无互斥，游客有互斥 3游客、团队均互斥或者不展示 */
	public final static String MUTEX_CODE = "mutex_code";
	/** 互斥操作 订单不受互斥影响的游客结果集 */
	public final static String MUTEX_RESULT_lIST = "mutex_result_list";

	/** 基础信息模块 */
	public static class BaseInfo {
		/** 酒店设施管理 */
		public static final String HOTEL_INFO = "hotel";
		public static final String HOTEL_INFO_TITLE = "酒店管理";
		/** 酒店设施管理 */
		public static final String HOTEL_INSTALLATION = "hotel_facilities";
		public static final String HOTEL_INSTALLATION_TITLE = "酒店设施";
		/** 酒店主题管理 */
		public static final String HOTEL_TOPIC = "hotel_topic";
		public static final String HOTEL_TOPIC_TITLE = "酒店主题";
		/** 酒店星级管理 */
		public static final String HOTEL_STAR = "hotel_star";
		public static final String HOTEL_STAR_TITLE = "酒店星级";
		/** 酒店集团管理 */
		public static final String HOTEL_GROUP = "hotel_group";
		public static final String HOTEL_GROUP_TITLE = "酒店集团";

		/** 餐型类型管理 */
		public static final String HOTEL_MEAL = "hotel_meal";
		public static final String HOTEL_MEAL_TITLE = "餐型管理";
		/** 餐型类型管理 */
		public static final String HOTEL_MEAL_TYPE = "hotel_meal_type";
		public static final String HOTEL_MEAL_TYPE_TITLE = "餐型类型";
		/** 酒店楼层管理 */
		public static final String HOTEL_FLOOR = "hotel_floor";
		public static final String HOTEL_FLOOR_TITLE = "酒店楼层";
		/** 酒店特色管理 */
		public static final String HOTEL_FEATURE = "hotel_feature";
		public static final String HOTEL_FEATURE_TITLE = "酒店特色";
		/** 酒店类别管理 */
		public static final String HOTEL_TYPE = "hotel_type";
		public static final String HOTEL_TYPE_TITLE = "酒店类别";
		/** 房型特色管理 */
		public static final String HOTEL_ROOM_FEATURE = "room_feature";
		public static final String HOTEL_ROOM_FEATURE_TITLE = "房型特色";
		/** 酒店床型管理 */
		public static final String HOTEL_BED_TYPE = "hotel_bed_type";
		public static final String HOTEL_BED_TYPE_TITLE = "酒店床型";

		/** 岛屿管理 */
		public static final String ISLANDS_MANAGER = "islands_manager";
		public static final String ISLANDS_MANAGER_TITLE = "岛屿";
		/** 岛屿主题管理 */
		public static final String ISLANDS_TOPIC = "islands_topic";
		public static final String ISLANDS_TOPIC_TITLE = "岛屿主题";
		/** 岛屿类型管理 */
		public static final String ISLANDS_TYPE = "islands_type";
		public static final String ISLANDS_TYPE_TITLE = "岛屿类型";
		/** 岛屿上岛方式管理 */
		public static final String ISLANDS_LANDING_STYLE = "islands_way";
		public static final String ISLANDS_LANDING_STYLE_TITLE = "岛屿上岛方式";

		/** 批发商类型管理 */
		public static final String WHOLESALER_TYPE = "wholesaler_type";
		public static final String WHOLESALER_TYPE_TITLE = "批发商类型";
		/** 批发商等级管理 */
		public static final String WHOLESALER_LEVEL = "wholesaler_level";
		public static final String WHOLESALER_LEVEL_TITLE = "批发商等级";

		/** 地接社类型管理 */
		public static final String TRAVEL_AGENCY_TYPE = "travel_agency_type";
		public static final String TRAVEL_AGENCY_TYPE_TITLE = "地接社类型";
		/** 地接社等级管理 */
		public static final String TRAVEL_AGENCY_LEVEL = "travel_agency_level";
		public static final String TRAVEL_AGENCY_LEVEL_TITLE = "地接社等级";

		/** 游客类型管理 */
		public static final String TRAVELER_TYPE = "traveler_type";
		public static final String TRAVELER_TYPE_TITLE = "游客类型";

	}

	/** 产品类型 */
	public static class ProductType {
		/** 产品类型：全部*/
		public static final String PRODUCT_ALL_DES = "全部";
		/** 产品类型：单团 */
		public static final Integer PRODUCT_SINGLE = 1;
		/** 产品类型：散拼 */
		public static final Integer PRODUCT_LOOSE = 2;
		/** 产品类型：游学 */
		public static final Integer PRODUCT_STUDY = 3;
		/** 产品类型：大客户 */
		public static final Integer PRODUCT_BIG_CUSTOMER = 4;
		/** 产品类型：自由行 */
		public static final Integer PRODUCT_FREE = 5;
		/** 产品类型：签证 */
		public static final Integer PRODUCT_VISA = 6;
		/** 产品类型：机票 */
		public static final Integer PRODUCT_AIR_TICKET = 7;
		/** 产品类型：游轮 */
		public static final Integer PRODUCT_CRUISE = 10;
		/** 产品类型：酒店 */
		public static final Integer PRODUCT_HOTEL = 11;
		/** 产品类型：酒店 */
		public static final String PRODUCT_HOTEL_DESC = "酒店";
		/** 产品类型：海岛游 */
		public static final Integer PRODUCT_ISLAND = 12;
		/** 产品类型：海岛游 */
		public static final String PRODUCT_ISLAND_DESC = "海岛游";
	}

	public static Map<Integer, String> opMap = Maps.newHashMap();

	/** 订单状态对应库存（用于余位判断和扣减或增加）  */
	public static class StockOpType {
		/** 创建订单 */
		public static final Integer CREATE = 1;
		/** 修改订单 */
		public static final Integer MODIFY = 2;
		/** 计调确认 */
		public static final Integer JD_CONFIRM = 3;
		/** 订单激活 */
		public static final Integer INVOKE = 4;
		/** 订单支付 */
		public static final Integer PAY = 5;
		/** 财务确认收款 */
		public static final Integer CW_CONFIRM = 6;
		/** 转团 */
		public static final Integer TRANSFER_GROUP = 7;
		/** 参团 */
		public static final Integer JOIN_GROUP = 7;
	}

	static{
		opMap.put(StockOpType.CREATE, "创建订单");
		opMap.put(StockOpType.MODIFY, "修改订单");
		opMap.put(StockOpType.JD_CONFIRM, "计调确认");
		opMap.put(StockOpType.INVOKE, "订单激活");
		opMap.put(StockOpType.PAY, "订单支付");
		opMap.put(StockOpType.CW_CONFIRM, "财务确认收款");
		opMap.put(StockOpType.TRANSFER_GROUP, "转团");
		opMap.put(StockOpType.JOIN_GROUP, "参团");
	}

	/**
	 * 询价：销售直接选择计调
	 */
	public static final Integer FOR_MANAGER_NO = 0;
	/**
	 * 询价：计调主管分配计调
	 */
	public static final Integer FOR_MANAGER_YES = 1;
	/**
	 * 询价：地接计调
	 */
	public static final Integer TYPE_OP_DAN_TUAN = 0;
	/**
	 * 询价：机票计调
	 */
	public static final Integer TYPE_OP_AIR = 1;

	/**
	 * 供应商编码 拉美途
	 */
	public static final String SUPPLIER_UUID_LAMEITOUR = "7a81a26b77a811e5bc1e000c29cf2586";
	/**
	 * 越柬行踪
	 * */
	public static final String SUPPLIER_UUID_YJXZ = "7a81b21a77a811e5bc1e000c29cf2586";

	/**环球行UUID*/
	public static final String SUPPLIER_UUID_HQX =  "7a816f5077a811e5bc1e000c29cf2586";
	/**新行者UUID*/
	public static final String SUPPLIER_UUID_XXZ =  "7a8175bc77a811e5bc1e000c29cf2586";
	/**
	 * 大洋国旅
	 */
	public static final String SUPPLIER_UUID_DYGL = "7a81a03577a811e5bc1e000c29cf2586";
	/**
	 * 诚品旅游
	 */
	public static final String SUPPLIER_UUID_CPLY = "ed88f3507ba0422b859e6d7e62161b00";
	/**
	 * 日信观光
	 */
	public static final String SUPPLIER_UUID_RXGG = "58a27feeab3944378b266aff05b627d2";
	/**
	 * 天马运通
	 */
	public static final String SUPPLIER_UUID_TMYT = "dfafad3ebab448bea81ca13b2eb0673e";
	/**
	 * 拉美途
	 */
	public static final String SUPPLIER_UUID_LMT = "7a81a26b77a811e5bc1e000c29cf2586";

	/**
	 * 奢华之旅
	 */
	public static final String SUPPLIER_UUID_SHZL = "75895555346a4db9a96ba9237eae96a5";

	/**
	 * 优加
	 */
	public static final String SUPPLIER_UUID_YJ = "7a81c5d777a811e5bc1e000c29cf2586";

	/**
	 * 起航假期
	 */
	public static final String SUPPLIER_UUID_QHJQ = "5c05dfc65cd24c239cd1528e03965021";

	/**
	 * 南亚大自然
	 */
	public static final String SUPPLIER_UUID_NYDZR = "cb4390e3fed841798f1bb755257334be";

	/**
	 * 鼎鸿假期
	 */
	public static final String SUPPLIER_UUID_DHJQ = "049984365af44db592d1cd529f3008c3";

	/**
	 * 懿洋假期
	 * */
	public static final String SUPPLIER_UUID_YYJQ = "f5c8969ee6b845bcbeb5c2b40bac3a23";

	/**
	 * 青岛凯撒(辉煌齐鲁)
	 */
	public static final String SUPPLIER_UUID_QDKS = "7a8177e377a811e5bc1e000c29cf2586";

	/**
	 * 思锐创途 QUAUQ
	 * */
	public static final String SUPPLIER_UUID_QUAUQ = "b9062c873383404388c27ca3cbb78b69";

	/**
	 * 美途国际
	 * */
	public static final String SUPPLIER_UUID_MTGJ = "42e108f116a8464a902d43831e7b0381";

	/**
	 * 友创国际
	 * */
	public static final String SUPPLIER_UUID_YCGJ = "7a45838277a811e5bc1e000c29cf2586";

	/**
	 * 骡子假期
	 * */
	public static final String SUPPLIER_UUID_LZJQ = "980e4c74b7684136afd89df7f89b2bee";

	/**
	 * 美途国际-成本付款状态：已付款
	 */
	public static final Integer MTOUR_PAYSTATUS_1 = 1;
	/**
	 * 美途国际-成本付款状态：待提交
	 */
	public static final Integer MTOUR_PAYSTATUS_2 = 2;
	/**
	 * 美途国际-成本付款状态：已提交
	 */
	public static final Integer MTOUR_PAYSTATUS_3 = 3;
	/**
	 * 美途国际-成本付款状态：撤回
	 */
	public static final Integer MTOUR_PAYSTATUS_4 = 4;

	/**
	 * 美途国际搜索类型，团号
	 */
	public static final String MTOUR_SEARCHTYPE_GROUPCODE = "1";

	/**
	 * 美途国际搜索类型，订单号
	 */
	public static final String MTOUR_SEARCHTYPE_ORDERNUM = "2";

	/**
	 * 美途国际搜索类型，产品名称
	 */
	public static final String MTOUR_SEARCHTYPE_PRODUCTNAME = "3";

	/**
	 * 美途国际搜索类型，渠道名称
	 */
	public static final String MTOUR_SEARCHTYPE_AGENTNAME = "4";

	/**
	 * 美图国际--账龄查询：已结清
	 */
	public static final String MTOUR_ACCOUTAGE_1 = "1";

	/**
	 * 美图国际--账龄查询：未结清
	 */
	public static final String MTOUR_ACCOUTAGE_0 = "0";
	public static final String DA_YANG_COMPANYUUID = "7a81a03577a811e5bc1e000c29cf2586";
	public static final String YOU_JIA_COMPANYUUID = "7a81c5d777a811e5bc1e000c29cf2586";
	/**
	 * 币种符号，人民币
	 */
	public static final String CURRENCY_MARK_RMB = "¥";

	/**  报名价格类型 0：同行价 */
	public final static Integer PRICE_TYPE_THJ = 0;

	/**  报名价格类型 1：直客价 */
	public final static Integer PRICE_TYPE_ZKJ = 1;

	/**  报名价格类型 2：QUAUQ价 */
	public final static Integer PRICE_TYPE_QUJ = 2;

	/**
	 * 审核页面列表页tab定义 全部
	 */
	public static final String REVIEW_TAB_ALL = "0";

	/**
	 * 审核页面列表页tab定义 1 待本人审批
	 */
	public static final String REVIEW_TAB_TO_BE_REVIEWED = "1";

	/**
	 * 审核页面列表页tab定义 2 本人审批通过 3 非本人审批
	 */
	public static final String REVIEW_TAB_REVIEWED = "2";

	/**
	 * 审核页面列表页tab定义 3 非本人审批
	 */
	public static final String REVIEW_TAB_OTHER_REVIEWED = "3";

	/**
	 * 审核页面操作定义 0 驳回 1 通过
	 */
	public static final String REVIEW_ACTION_PASS = "1";

	public static final String REVIEW_ACTION_REJECT = "0";

	/* 数字定义 */
	public static class NumberDef {
		public static final Integer NUMER_ZERO = 0;

		public static final Integer NUMER_ONE = 1;

		public static final Integer NUMER_TWO = 2;

		public static final Integer NUMER_THREE = 3;
	}
	/*退款付款参数定义*/
	public static class RefundPayParams{
		/** 团号 */
		public static final String GROUP_CODE = "groupCode";
		/** 产品类型 */
		public static final String PRODUCT_TYPE = "prdType";
		/** 币种id */
		public static final String CURRENCY_ID = "currencyid";
		/** 钱数范围1 */
		public static final String C_AMOUNT_START = "cAmountStart";
		/** 钱数范围2 */
		public static final String C_AMOUNT_END = "cAmountEnd";
		/** 渠道id*/
		public static final String AGENT_ID = "agentId";
		/** 下单人id */
		public static final String CREATOR_ID = "creator";
		/** 销售id */
		public static final String SALER_ID = "saler";
		/** 计调id */
		public static final String JD_ID = "jdsaler";
		/** 支付状态 */
		public static final String PAY_STATUS = "payStatus";
		/** 打印状态 */
		public static final String PRINT_STATUS = "printStatus";
		/** 款项选择 */
		public static final String PAY_REFUND_TYPE = "payType2";
		/** 支付方式 */
		public static final String PAY_MODE = "payMode";
		/** 来款银行 */
		public static final String FROM_BANK = "fromBank";
		/** 出纳确认时间开始 */
		public static final String CASHIER_CONFIRM_DATE_BEGIN = "cashierConfirmDateBegin";
		/** 出纳确认时间结束 */
		public static final String CASHIER_CONFIRM_DATE_END = "cashierConfirmDateEnd";
	}

	/**
	 * 下单时间
	 */
	public static final String REVIEW_VARIABLE_KEY_ORDER_TIME = "orderTime";

	/**
	 * 订单总额
	 */
	public static final String REVIEW_VARIABLE_KEY_TOTAL_MONEY = "totalMoney";

	/**
	 * 出团日期
	 */
	public static final String REVIEW_VARIABLE_KEY_GROUP_OPEN_DATE = "groupOpenDate";

	/**
	 * 行程天数
	 */
	public static final String REVIEW_VARIABLE_KEY_ACTIVITY_DURATION = "activityDuration";

	/**
	 * 目的地
	 */
	public static final String REVIEW_VARIABLE_KEY_TARGETAREANAMES = "targetAreaNames";

	/**
	 * 退团改后应收价币种id
	 */
	public static final String REVIEW_VARIABLE_KEY_AFTER_EXIT_GROUP_CURRENCY_ID = "afterExitGroupCurrencyId";

	/**
	 * 退团改后应收价数量
	 */
	public static final String REVIEW_VARIABLE_KEY_AFTER_EXIT_GROUP_MONEY = "afterExitGroupMoney";

	/**
	 * 退团 退款str （游客id#币种id#金额）
	 */
	public static final String REVIEW_VARIABLE_KEY_AFTER_EXIT_GROUP_REFUND_MONEY = "refundMoneyStr";

	/**
	 * 游客原始应收金额
	 */
	public static final String REVIEW_VARIABLE_KEY_OLD_TOTAL_MONEY = "oldtotalmoney";

	/**
	 * 游客应收金额
	 */
	public static final String REVIEW_VARIABLE_KEY_TRAVELER_PAY_PRICE = "travelerPayPrice";

	/**
	 * 改价游客ID
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGE_TRAVELER_ID = "travelerid";
	/**
	 * 改价游客姓名
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGE_TRAVELER_NAME = "travelername";
	/**
	 * 改价币种
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGE_CURRENCY_ID = "currencyid";
	/**
	 * 改价币种名称
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGE_CURRENCY_NAME = "currencyname";
	/**
	 * 改价款项
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGED_FUND = "changedfund";
	/**
	 * 改价前金额
	 */
	public static final String REVIEW_VARIABLE_KEY_CUR_TOTAL_MONEY = "curtotalmoney";
	/**
	 * 改价后金额
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGED_TOTAL_MONEY= "changedtotalmoney";
	/**
	 * 改价差额
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGED_PRICE = "changedprice";
	/**
	 * 是否只有人民改价
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGE_IS_RMB_ONLY = "isRmbOnly";
	/**
	 * 改价差额
	 */
	public static final String REVIEW_VARIABLE_KEY_CHANGE_PRICE = "changePrice";


	/**
	 * 返佣款项
	 */
	public static final String REVIEW_VARIABLE_KEY_REBATES_COST_NAME = "costName";

	/**
	 * 转款uuid(对应review_process_money_amount的id)
	 */
	public static final String REVIEW_VARIABLE_KEY_TRANSFER_MONEY_UUID = "transferMoneyUuid";

	/**
	 * 转款备注
	 */
	public static final String REVIEW_VARIABLE_KEY_TRANSFER_MONEY_REMARK = "remark";

	/**
	 * 转团后游客结算价
	 */
	public static final String REVIEW_VARIABLE_KEY_OLD_TRAVELER_PAY_PRICE = "oldPayPriceMoney";

	/**
	 * 转入团游客结算价
	 */
	public static final String REVIEW_VARIABLE_KEY_NEW_TRAVELER_PAY_PRICE = "newPayPriceMoney";

	/**
	 * 转团前游客所在订单id
	 */
	public static final String REVIEW_VARIABLE_KEY_OLD_ORDERID = "transferOldOrderId";

	/**
	 * 转团后游客所在订单id
	 */
	public static final String REVIEW_VARIABLE_KEY_NEW_ORDERID = "transferNewOrderId";

	/**
	 * 转团后新游客id
	 */
	public static final String REVIEW_VARIABLE_KEY_NEW_TRAVELLER_ID = "transferNewTravellerId";

	/**
	 * 还签证押金收据 游客ID
	 */
	public static final String REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_TRAVELERID = "travelerID";

	/**
	 * 还签证押金收据 收据金额
	 */
	public static final String REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_AMOUNT = "depositReceiptAmount";

	/**
	 * 还签证押金收据 接收人
	 */
	public static final String REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_RECEIPTOR = "depositReceiptor";

	/**
	 * 还签证押金收据 备注
	 */
	public static final String REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_REMARK = "remark";

	/**
	 * 还签证押金收据 接收时间
	 */
	public static final String REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_TIME = "depositReceiptReturnTime";

	/**
	 * 付款自定义类型 退款付款
	 */
	public static final String PAY_REFUND = "201";
	/**
	 * 付款自定义类型 借款付款
	 */
	public static final String PAY_REBATE = "202";
	/**
	 * 付款自定义类型 借款付款
	 */
	public static final String PAY_BORROW = "203";

	/**
	 * 供应商标识 --- 俄风行
	 */
	public static final String OFFICE_EFX = "俄风行";

	/**
	 * 供应商标识 --- 九州风行
	 */
	public static final String OFFICE_JZFX = "九州风行";

	/**
	 *  自定义 字符串 代表下单人列表
	 */
	public static final String CREATOR_LIST = "creatorList";
	/**
	 *  自定义 字符串 代表计调列表
	 */
	public static final String JD_LIST = "jdList";
	/**
	 *  自定义 字符串 代表销售列表
	 */
	public static final String SALER_LIST = "salerList";

	/**
	 * 自定义字符串 支付类型
	 */
	public static final String PAY_TYPE_DESC = "payType";
	/**
	 * 自定义字符串 page
	 */
	public static final String PAGE = "page";
	/**
	 * 自定义字符串 params
	 */
	public static final String PARAMS = "params";
	/**
	 * 自定义字符串 dicts
	 */
	public static final String DICTS = "dicts";
	/**
	 * 自定义 字符串常量 0
	 */
	public static final String ZERO_DESC = "0";
	/**
	 * 自定义 字符串常量 空字符串
	 */
	public static final String BLANK_STR = "";
	/**
	 * 自定义字符串 退款付款列表路径(新审核)
	 */
	public static final String PATH_NEW_REFUND_PAY_LIST = "review/pay/refundPayList";

	/**
	 * 自定义字符串 下拉框的全部选项的value
	 */
	public static final String ALL_VALUE = "-99999";

	/**
	 * 还款日期
	 */
	public static final String REVIEW_VARIABLE_KEY_REFUND_DATE = "refundDate";

	/**
	 * 转团审批：转团模式标识, 多个游客 --- 转团 ---> 一个审批一个订单
	 */
	public static final String REVIEW_VARIABLE_TRANSGROUP_MANY2ONE_FLAG = "transGroupMany2One";
	/**
	 * 转团审批：原订单游客IDs（逗号隔开的id组成的String）
	 */
	public static final String REVIEW_TRANSGROUP_OLD_TRAVELERIDS = "transGroupTravelerIDs";

	/**
	 * 自定义：审核转团 应付金额
	 */
	public static final String REVIEW_VARIABLE_KEY_PAY_PRICE = "payPrice";

	/**
	 * 自定义：审核转团 转团后应付金额
	 */
	public static final String REVIEW_VARIABLE_KEY_SUBTRACT_PRICE = "subtractPrice";

	/**
	 * 自定义：审核转团 转入团产品ID
	 */
	public static final String REVIEW_VARIABLE_TRANSFER_GROUP_NEW_PRODUCT_ID = "newProductId";

	/**
	 * 自定义：审核转团 转入团产品类型
	 */
	public static final String REVIEW_VARIABLE_TRANSFER_GROUP_NEW_PRODUCT_TYPE = "newProductType";

	/**
	 * 自定义：审核转团 转入团ID
	 */
	public static final String REVIEW_VARIABLE_TRANSFER_GROUP_NEW_GROUP_ID = "newGroupId";

	/**
	 * 自定义：审核转团 转入团团号
	 */
	public static final String REVIEW_VARIABLE_TRANSFER_GROUP_NEW_GROUP_CODE = "newGroupCode";

	/**
	 * 自定义：审核转团 游客ID
	 */
	public static final String REVIEW_VARIABLE_TRANSFER_GROUP_TRAVELLERID = "travellerId";

	/**
	 * 自定义：审核转团 支付方式
	 */
	public static final String REVIEW_VARIABLE_KEY_PAY_TYPE = "payType";

	/**
	 * 自定义：审核转团 保留天数
	 */
	public static final String REVIEW_VARIABLE_KEY_REMAIN_DAYS = "remainDays";

	/**
	 * 自定义：审核返佣 返佣差额（币种符号与金额）
	 */
	public static final String REVIEW_VARIABLE_KEY_REBATES_MARK_TOTAL_MONEY = "markTotalMoney";

	/**
	 * 菜单名称常量--审核模块
	 */
	public static final String MENU_FLAG_REVIEW = "REVIEW";

	/**
	 * 菜单名称常量--财务审核模块
	 */
	public static final String MENU_FLAG_REVIEW4CW = "REVIEW4CW";

	public static final String QINGDAO_KAISA_COMPANYUUID = "7a816f5077a811e5bc1e000c29cf2586";



	/**
	 * =================返佣对象信息常量表 begin ===================
	 */

	/**
	 * 返佣对象id
	 */
	public static final String REBATES_OBJECT_ID="rebatesObjectId";
	/**
	 * 返佣对象类型
	 */
	public static final String REBATES_OBJECT_TYPE="rebatesObjectType";
	/**
	 * 返佣对象名称
	 */
	public static final String REBATES_OBJECT_NAME="rebatesObjectName";
	/**
	 * 返佣对象账号id
	 */
	public static final String REBATES_OBJECT_ACCOUNT_ID="rebatesObjectAccountId";
	/**
	 * 返佣对象账号类型：境内、境外
	 */
	public static final String REBATES_OBJECT_ACCOUNT_TYPE="rebatesObjectAccountType";
	/**
	 * 返佣对象账号开户行名称
	 */
	public static final String REBATES_OBJECT_ACCOUNT_BANK="rebatesObjectAccountBank";
	/**
	 * 返佣对象账户号码
	 */
	public static final String REBATES_OBJECT_ACCOUNT_CODE="rebatesObjectAccountCode";

	/**
	 * 已占位订单的确认状态  已确认 ： 1  未确认 ： 0
	 */
	public static final Integer SEIZEDCONFIRMATIONSTATUS_1 = 1;

	/**
	 * 是否是补单产品，0：否，1：是
	 */
	public static final Integer IS_AFTER_SUPPLEMENT_1 = 1;

	/**
	 * 是否是补单产品，0：否，1：是
	 */
	public static final Integer IS_AFTER_SUPPLEMENT_0 = 0;

	/**
	 * 提醒类型   1.还款提醒
	 */
	public static final Integer REMIND_TYPE_REFUND = 1;

	/** 团期补位状态  待补位 */
	public static final Integer COVER_STATUS_DBW = 1;
	/** 团期补位状态  已补位 */
	public static final Integer COVER_STATUS_YBW = 2;
	/** 团期补位状态  已驳回 */
	public static final Integer COVER_STATUS_YBH = 3;
	/** 团期补位状态  已取消 */
	public static final Integer COVER_STATUS_YQX = 4;
	/** 团期补位状态  生成订单 */
	public static final Integer COVER_STATUS_SCDD = 5;

	/** 定价策略  加 */
	public static final Integer ADD = 1;
	/** 定价策略  减 */
	public static final Integer SUBTRACT = 2;
	/** 定价策略  乘 */
	public static final Integer MULTIPLY = 3;
	/**
	 * ==========================
	 * T1 2 T2
	 * ==========================
	 */
	/**
	 * QUAUQ渠道默认登录账户的角色名称 （用户类型：渠道，角色类型：其他）
	 */
	public static final String QUAUQAGENT_ROLENAME_DEFAULT = "Quauq渠道登录账号角色";

	/**
	 * sys_user表中： 是否属quauq渠道登录用户 is_quauq_agent_login_user  0：否  1：是
	 */
	public static final String QUAUQ_AGENT_LOGIN_USER_NO = "0";
	/**
	 * sys_user表中： 是否属quauq渠道登录用户 is_quauq_agent_login_user  0：否  1：是
	 */
	public static final String QUAUQ_AGENT_LOGIN_USER_YES = "1";

	/**
	 * agentinfo表中： 是否属quauq渠道 is_quauq_agent  0：否  1：是
	 */
	public static final String QUAUQ_AGENT_NO = "0";
	/**
	 * agentinfo表中： 是否属quauq渠道is_quauq_agent  0：否  1：是
	 */
	public static final String QUAUQ_AGENT_YES = "1";

	/**
	 * agentinfo表中： 本quauq渠道是否已经启用 enable_quauq_agent  0：否  1：是
	 */
	public static final String QUAUQ_AGENT_DISABLED = "0";
	/**
	 * agentinfo表中： 本quauq渠道是否已经启用enable_quauq_agent  0：否  1：是
	 */
	public static final String QUAUQ_AGENT_ENABLED = "1";
	/**
	 * activitygroup表中： 是否上架到T1平台,0表示未上架T1,1表示已上架T1
	 */
	public static final Integer QUAUQ_T1_ON = 1;
	/**
	 * activitygroup表中： 是否上架到T1平台,0表示未上架T1,1表示已上架T1
	 */
	public static final Integer QUAUQ_T1_OFF = 0;

	/**
	 * 错误信息对应的key
	 */
	public static final String ERROR_MESSAGE_KEY = "error_message_key";

	/**
	 * 错误提示页面
	 */
	public static final String ERROR_PAGE = "error/error";

	/**
	 *设置定价策略状态 0表示未设置状态，1表示需重新设置状态，2表示不需重新设置状态
	 */
	public static final Integer NO_PRICING_STATUS = 0;

	public static final Integer PRICING_NEED_RESET_STATUS  = 1;

	public static final Integer PRICING_NO_NEED_RESET_STATUS  = 2;

	// T1首页缓存
	public static final String T1HOMEFOREIGNCACHE = "T1HOMEFOREIGNCACHE";
	public static final String T1HOMEINLANDCACHE = "T1HOMEINLANDCACHE";
	public static final String T1HOMEFOREIGNBEFORE = "T1HOMEFOREIGNBEFORE";
	public static final String T1HOMEINLANDBEFORE = "T1HOMEINLANDBEFORE";




	/** 订单跟踪-订单状态：正常订单 */
	public static final Integer ORDER_ZZ_NORMAL = 0;
	/** 订单跟踪-订单状态：T2订单删除 */
	public static final Integer ORDER_ZZ_DELETE = 1;
	/** 订单跟踪-订单状态：T2订单取消 */
	public static final Integer ORDER_ZZ_CANCEL = 2;
	/** 订单跟踪-订单状态：T2订单驳回取消（不保留位置） */
	public static final Integer ORDER_ZZ_BH_CANCEL = 3;
	/** 订单跟踪-订单状态：预报名取消 */
	public static final Integer ORDER_ZZ_YBM_CANCEL = 4;
	/** 订单跟踪-订单状态：预报名删除 */
	public static final Integer ORDER_ZZ_YBM_DELETE = 5;
	/** 订单跟踪-订单状态：T2订单驳回取消（保留位置） */
	public static final Integer ORDER_ZZ_BH2_CANCEL = 6;

	/**数据分析——今日*/
	public static final String ORDER_DATA_STATISTICS_TODAY = "1";
	/**数据分析——昨日*/
	public static final String ORDER_DATA_STATISTICS_YESTERDAY = "-1";
	/**数据分析——本周*/
	public static final String ORDER_DATA_STATISTICS_WEEK = "2";
	/**数据分析——本月*/
	public static final String ORDER_DATA_STATISTICS_MONTH = "3";
	/**数据分析——上月*/
	public static final String ORDER_DATA_STATISTICS_LAST_MONTH = "-3";
	/**数据分析——本年*/
	public static final String ORDER_DATA_STATISTICS_YEAR = "4";
	/**数据分析——去年*/
	public static final String ORDER_DATA_STATISTICS_LAST_YEAR = "-4";
	/**数据分析——全部*/
	public static final String ORDER_DATA_STATISTICS_ALL = "5";

	/**数据分析类型——订单数*/
	public static final String ORDER_DATA_STATISTICS_ORDER_NUM = "1";
	/**数据分析类型——收客人数*/
	public static final String ORDER_DATA_STATISTICS_CUSTOMER_NUM = "2";
	/**数据分析类型——订单金额*/
	public static final String ORDER_DATA_STATISTICS_ORDER_MONEY = "3";
	
	/**数据分析V2.0总览类型——订单总览*/
	public static final String ORDER_DATA_STATISTICS_ORDER_OVERVIEW  = "dd";
	/**数据分析V2.0总览类型——询单总览*/
	public static final String ORDER_DATA_STATISTICS_ENQUIRY_OVERVIEW = "xd";


	/** 微信分销图片广告-默认图片路径 */
	public static final String DEFAULT_IMG_PATH = "\\static\\images\\weixin\\默认广告图.png";
	/** 微信appid*/

    //public static final String WEIXIN_APP_ID = "wxea0928dd01ac6752";
    //public static final String WEIXIN_APP_ID = "wxaecb7b4b74590022";
	public static final String WEIXIN_APP_ID = "wx3b6fdf32c6cb785d";


	/**列表页*/
	public static final String QUAUQ_WEIXIN_INDEX_URL = "http://test.quauqsystem.com.cn/weixin/index.php?uuid=";

	/**详情页*/
	public static final String QUAUQ_WEIXIN_DETAIL_URL = "http://test.quauqsystem.com.cn/weixin/detail.php?uuid=";

	/**微信分销产品列表页*/
	public static final String QUAUQ_WEIXIN_INDEX_URL_JAVA = "http://test.quauqsystem.com.cn/weixin/oAuthAndUserInfo/userInfo?uuid=";

	/**微信分销产品详情页*/
	public static final String QUAUQ_WEIXIN_DETAIL_URL_JAVA = "http://test.quauqsystem.com.cn/weixin/oAuthAndUserInfo/goToProductDetail?uuid=";
}
