package com.trekiz.admin.modules.visa.form;

/***
 * 签务和销售身份管理订单功能用到的所有的SQL
 * wenjianye
 * 2014-12-03
 *   
 * */
public class SqlConstant {
	
	//根据游客id和订单id找到是否有驳回记录
	public static String SEARCH_BOHUI = "select id from orderpay where traveler_id = ? and orderId = ? and isAsAccount = 2 ";
	//根据签证订单表的id主键和游客表的id主键,查询面签通知需要的信息
	public static String JISUAN_CHAJIA = " select "
	//币种id
	+" a.currencyId, "
	//应收金额
	+" a.amount as big, "
	//计算后的差额()
	+" a.amount - b.amount as result "
	+" from " 
	+"(SELECT  m1.amount,currencyId from money_amount m1  where m1.serialNum= ? ) as a " 
	+"LEFT JOIN (SELECT  m1.amount,currencyId from money_amount m1  where m1.serialNum= ? ) as b " 
	+"on a.currencyId=b.currencyId";
	
	//根据签证订单表的id主键和游客表的id主键,查询面签通知需要的信息
	public static String SEARCH_MIANQIAN = " select "
								/*领区'*/
								+ " vin.area as lingqu, "
								/*约签时间'*/
								+ " vin.interview_time as yueqian, "
								/*'说明会时间',*/
								+ " vin.explanation_time as shuominghui, "
								/*'联系人',*/
								+ " vin.contact_man as lianxiren, "
								/*'联系方式',*/
								+ " vin.contact_way as lianxifangshi, " 
								/*'姓名',*/
								+ " vt.travaler_name as name "
								+ " from visa_interview_notice vin "
								+ " LEFT JOIN visa_interview_notice_traveler vt on vt.interview_id = vin.id where vin.order_id = ? and vin.id=(select v.id from visa_interview_notice v where v.order_id = ? ORDER BY v.id desc LIMIT 1) ";
	    
	//根据团期编号,找到所有签证的子订单的游客个数
	//public static String SEARCH_VISAORDER_BY_MAINORDER = " select vo.id,vo.travel_num from   activitygroup a LEFT JOIN  productorder po on a.id = po.productGroupId  LEFT  JOIN visa_order vo on vo.main_order_code = po.id where a.groupCode = ? ";
	public static String SEARCH_VISAORDER_BY_MAINORDER = "SELECT t.orderId as id FROM activitygroup a LEFT JOIN productorder po ON a.id = po.productGroupId LEFT JOIN traveler t ON t.main_order_id = po.id WHERE a.groupCode = ? AND t.main_order_id IS NOT NULL ";
	//根据流程id找到借款的金额和币种信息
	public static String  SEARCH_JIEKUAN=" select rd.myKey,rd.myValue,su.name from review r LEFT JOIN review_detail rd on r.id = rd.review_id LEFT JOIN sys_user su on r.createBy = su.id where r.orderId = ? and r.travelerId = ? and r.flowType = 5 ";
	//根据visaid更新visa
	public static String  UPDATE_VISA_BY_ID="update visa set sign_original_project_type = ? ,sign_copy_project_type =?  where  id = ? ";
	//根据UUid更新金额
	public static String  UPDATE_MONEY_AMOUNT_BY_UUID="update money_amount set amount = ? where  serialNum = (select total_deposit from visa where id = ?) ";
	//根据UUid查询货币名称和金额
	//设置签证担保押金 (从押金状态变更成别的状态,要删除以前的应收价格)
	public static String  SEARCH_MONEY_AMOUNT_BY_UUID="select c.currency_name , ma.amount,c.currency_mark from  visa v  LEFT JOIN money_amount ma on v.total_deposit = ma.serialNum LEFT JOIN currency c on c.currency_id =ma.currencyId where v.id = ? ";
	public static String  UPDATE_VISA_TOTAL_DEPOSIT="update visa set total_deposit = ?, guarantee_status = 3 where id = ? ";
	//更新游客信息 - 护照状态
	public static String  UPDATE_PASSPORTSTATUS="update traveler  set passportStatus =  ? where id = ? ";

	//更新游客信息 - AA码,护照状态,实际出团时间,实际约签时间,签证状态,担保状态,实际送签时间
	public static String  UPDATE_VISA="update visa  set start_out =  ? ,contract =  ?,visa_stauts =  ? ,guarantee_status =  ? ,actual_delivery_time= ?  where id = ? ";
	public static String  UPDATE_VISA_ONE="update visa  set start_out =  ? ,contract =  ?,visa_stauts =  ? ,guarantee_status =  ?  where id = ? ";
	//查询是否有正在审核的押金转担保的申请
	public static String SEARCH_REVIEW ="select status from review where orderId = ? and flowType = 6  and travelerId = ? and status = 1 and active=1 and companyId = ?";
	
	//销售身份管理订单,参团列表中展示出单办签订单的数据
	public static String XS_SEARCH_CANTUAN_BY_VISAORDER = "select "
			+" vo.id as visaOrderId, "
			+" vo.group_code as dingdantuanhao, "
			+" su2.name as createuser, "
			+" vo.travel_num as renshu, "
			+" vo.visa_product_id as proId"
			+" from "
			+" visa_order vo LEFT JOIN visa_products vp  "
			+" on vo.visa_product_id = vp.id  "
			+" LEFT JOIN sys_user su2 on su2.id = vp.createBy  "
			+" where vo.order_no = ? ";

	//销售身份管理订单,根据参团团号查询参团列表的其他数据
	public static String XS_SEARCH_CANTUAN_BY_CANTUANTUANHAO = " select "
				//参团团号
				//+" a.groupCode as cantuantuanhao,"
				//订单团号
				+" vo.group_code as dingdantuanhao ,"
				//参团类型
				//+" po.orderStatus as cantuanleixing,"
				//计调
				+" su2.name as createuser,"
				//visa表的id
				+" vo.id ,"
				//签证订单人数
				+" vo.travel_num as renshu,"
				//出团日期
				//+" a.groupOpenDate as chutuanDate ,"
				//截团日期
				//+" a.groupCloseDate as jietuanDate,"
				//主订单编号
				//+" po.orderNum as orderNum, "
				//产品id
				//+" po.productId as proId, "
				//visa_order表的id
				+" vo.id as visaId, "
				//visa_order表的订单编号
				+" vo.order_no as orderCode, "
				//渠道id
				+" vo.agentinfo_id as agentId "
//				+" "
//				+" "
				
				
				+" from "
				+" visa_order vo "
				//+" LEFT JOIN "
				//+" productorder po"
				//+" on vo.main_order_code = po.id "
				//+" LEFT JOIN  activitygroup a "
				//+" on a.id =  po.productGroupId "
				+" LEFT JOIN visa_products vp "
				+" on vo.visa_product_id = vp.id "
				+" LEFT JOIN sys_user su2 on su2.id = vp.createBy "
				+" where a.groupCode= ? ";
	  
	//销售身份管理订单,查询参团列表的总数
//	public  String XS_SEARCH_CANTUAN_TOTAL = " select "
//									+" act.groupCode  "
//									+" from  visa_order vo  LEFT JOIN productorder po "
//									+" on po.id = vo.main_order_code  LEFT JOIN activitygroup act  on act.id = po.productGroupId "
//									+" WHERE vo.visa_order_status != 100 and act.groupCode is not null ";
	public static String XS_SEARCH_CANTUAN_TOTAL = " SELECT DISTINCT "
												+ " ifnull(act.groupCode, vo.order_no) AS groupCode "
												+ " FROM "
												+ " traveler t "
												+ " LEFT JOIN productorder po ON po.id = t.main_order_id "
												+ " LEFT JOIN activitygroup act ON act.id = po.productGroupId "
												+ " LEFT JOIN visa_order vo ON vo.id = t.orderid "
												+ " WHERE "
												+ " vo.visa_order_status != 100 ";
	
	//签务身份管理订单,根据订单查询游客信息
	public static String  SEARCH_TRAVELER = " SELECT "
							//0游客名称
							+" t.name, "
							//1护照编号
							+" t.passportCode,  "
							//2AA码
							+" v.AA_code,  "
							//3签证类型
							+" vp.visaType,  "
							//4签证国家
							+" sc.countryName_cn, " 
							//5预定出团时间
							+" v.forecast_start_out, "
							//6预计签约时间
							+" v.forecast_contract, "
							//7实际出团时间
							+" v.start_out, "
							//8实际签约时间
							+" v.contract, "
							//9签证状态
							+" v.visa_stauts, "
							//10护照状态
							+" t.passportStatus, "
							//11担保类型
							+" v.guarantee_status, "
							//12应收押金UUID
							+" v.total_deposit, "
							//13达账押金UUID
							+" v.accounted_deposit, "
							//14游客主键id
							+" t.id,  "
							//15 visa表的id
							+" v.id as visaId, "
							//16 签证产品id
							+" vp.id  as visaProductId, "
							//17 visa_order表id
							+" vo.id  as visaorderId, "
							//18 visa_order表的订单编号
							+" vo.order_no  as visaorderNo, "
							//19 visa_order表agentId
							+" vo.agentinfo_id  as agentinfo_id, "
							//20  已付押金UUID
							+" v.payed_deposit, "
							//21下单人
							+" su.name as create_by, "
							//22下单时间
							+" vo.create_date as create_date, "
							//23参团类型
							+" po.orderStatus as group_type, "
							//24订单锁死状态
							+" vo.lockStatus as lockStatus,  "
							//25游客的应收uuid
							+" t.payPriceSerialNum,  "
							//26游客的已付uuid
							+" t.payed_moneySerialNum,  "
							//27游客的主订单id
							+" t.main_order_id,  "
							//28游客的主订单编号
							+" po.orderNum,  "
							//29游客的参团团号
							+" a.groupCode,  "
							//30游客的达账uuid
							+" t.accounted_money,  "
							//31游客的结算方式
							+" t.payment_type, "
							//32 实际送签时间
							+" v.actual_delivery_time as deliveryTime, "
							//33 visa_products锁死状态
							+" vp.lockStatus as activityLockStatus,"
							//34 领区
							+" vp.collarZoning, "
							//35更新时间
							+" vo.update_date, "
							//36review状态
							+" re.status, "
							//37预计返佣UUID
							+" t.rebates_moneySerialNum "
							+" FROM traveler t"
							+" LEFT JOIN visa v on t.id = v.traveler_id  "
							+" LEFT JOIN visa_order vo on vo.id = t.orderId  "
							+" LEFT JOIN visa_products vp on vp.id = vo.visa_product_id "
							+" LEFT JOIN sys_country sc on sc.id = vp.sysCountryId "
						  	+" LEFT JOIN sys_user su on su.id = vo.create_by "
							+" LEFT JOIN productorder po " 
							+" on po.id = t.main_order_id "
							+" LEFT JOIN activitygroup a " 
							+" on a.id = po.productGroupId "
							+" LEFT JOIN  (SELECT rr.travelerId,rr.createDate,rr.status FROM review rr RIGHT JOIN (select max(re1.id) id from review re1 WHERE (re1.flowType=5 or re1.flowType=20) and re1.productType = 6 GROUP BY re1.travelerId) re on rr.id=re.id WHERE (rr.flowType=5 or rr.flowType=20) and rr.productType = 6 GROUP BY rr.travelerId ) re ON t.id = re.travelerId "
							//+" LEFT JOIN batch_record br on  t.borrow_passport_batch_no=br.batch_no "
//							updateBy jiachen 
//							+" LEFT JOIN review re " 
//							+" on t.id = re.travelerId "

							+" where  t.order_type = 6 and t.delFlag != 1 ";

	
	//签务身份-根据条件查询订单信息
	public static String QW_SEARCH_ORDER = " select "
								/*******销售身份订单管理添加的 开始********/
								//出团时间
								//+" act.groupOpenDate as groupOpenDate,"
								//截团时间
								//+" act.groupCloseDate as groupCloseDate,"
								//计调员名称
								+" su2.name as productCreateUser,"
								//参团团号
								//+" act.groupCode as cantuanTuanhao,"
								//主订单编号
								//+" po.orderNum as orderNum,"
								//订单团号
								+" vo.group_code as orderTuanhao, "
								//领区联系人
								+" vp.contactPerson as contactPerson, "
								/*******销售身份订单管理添加的 结束********/
								//渠道id主键
								+" a.id as agentId, "
								//主订单id
								//+" t.main_order_id as mainOrderCode, "
								//visa_order表的主键id
								+" vo.id as orderId, "
								//预定渠道
								+" a.agentName as agentName, "
								//订单编号
								+" vo.order_no as order_no, "
								//产品编号
								+" vp.productCode as visa_product_id, "
								//产品锁定状态
								+" vp.lockStatus as activityLockStatus, "
								//参团类型
								//+" po.orderStatus as group_type, "
								//下单人
								+" su.name as create_by, "
								//下单时间
								+" vo.create_date as create_date, "
								//人数
								+" vo.travel_num as travel_num, "
								//应收金额
								+" vo.total_money as visaPay, "
								//已付总金额
								+" vo.payed_money as payed_money, "
								//到账总金额
								+" vo.accounted_money as accounted_money, "
								//id主键
								+" vo.id as id,  "
								//签证订单状态
								+" vo.visa_order_status as visa_order_status,  "
								//签证订单删除标示位
								+" vo.del_flag as del_flag,  "
								//订单锁死状态
								+" vo.lockStatus as lockStatus,  "
								//支付状态
								+"vo.payStatus as payStatus, "
								//签证产品id
								+" vp.id as productId,  "
								+" vp.productName as productName, "
								+" re.status ,"
								+ "vo.isRead, "
								//发票状态
								+ "oin.verifyStatus invoiceStatus, "
								//收据状态
								+ "ore.verifyStatus receiptStatus, "
								//预计团队返佣
								+ "vo.groupRebate groupRebate, "
								//销售名称
								+ "vo.salerName salerName "
								
								+" from "
								+" visa_order vo " 
								+" LEFT JOIN"
								+" visa_products vp " 
								+" on vo.visa_product_id = vp.id "
//								+" LEFT JOIN"
//								+" traveler t" 
//								+" on t.orderId = vo.order_no and order_type = '6' "
//								+" LEFT JOIN visa v " 
//								+" on  t.id = v.traveler_id " 
								+" LEFT JOIN agentinfo a " 
								+" on vo.agentinfo_id = a.id "
								//+" LEFT JOIN productorder po " 
								//+" on po.id = vo.main_order_code "
								+" LEFT JOIN sys_user su " 
								+" on su.id = vo.create_by "
								//+" LEFT JOIN activitygroup act " 
								//+" on act.id = po.productGroupId  "
								+" LEFT JOIN sys_user su2 " 
								+" on su2.id = vp.createBy  "
								+" Left JOIN (SELECT DISTINCT rr.orderId,rr.status FROM review rr RIGHT JOIN (SELECT max(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id WHERE rr.productType = 6 AND (rr.flowType = 5 OR rr.flowType = 20 ) ) re on re.orderId = vo.id "
								+" LEFT JOIN orderinvoice oin ON oin.orderType='6' and oin.orderNum=vo.order_no and oin.verifyStatus != 2 "
								+" LEFT JOIN orderreceipt ore ON ore.orderType='6' and ore.orderNum=vo.order_no and ore.verifyStatus != 2 "
								+" WHERE vo.visa_order_status != 100 and vo.del_flag = 0 ";
}
