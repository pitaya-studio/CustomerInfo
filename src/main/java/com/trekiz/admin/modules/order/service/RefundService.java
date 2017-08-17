package com.trekiz.admin.modules.order.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.repository.CostRecordHotelDao;
import com.trekiz.admin.modules.cost.repository.CostRecordIslandDao;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderMoneyAmountService;
import com.trekiz.admin.modules.mtourfinance.json.DocInfoJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.PaymentJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.RefundJsonBean;
import com.trekiz.admin.modules.order.entity.PayCheck;
import com.trekiz.admin.modules.order.entity.PayRemittance;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.pay.dao.PayAlipayDao;
import com.trekiz.admin.modules.pay.dao.PayBanktransferDao;
import com.trekiz.admin.modules.pay.dao.PayDraftDao;
import com.trekiz.admin.modules.pay.dao.PayPosDao;
import com.trekiz.admin.modules.pay.entity.PayAlipay;
import com.trekiz.admin.modules.pay.entity.PayBanktransfer;
import com.trekiz.admin.modules.pay.entity.PayDraft;
import com.trekiz.admin.modules.pay.entity.PayFee;
import com.trekiz.admin.modules.pay.entity.PayPos;
import com.trekiz.admin.modules.pay.service.PayFeeService;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaFlowBatchOpration;
import com.trekiz.admin.modules.visa.repository.VisaFlowBatchOprationDao;

/**
 * 付款信息Service 
 */
@Service
@Transactional(readOnly = true)
public class RefundService {
	@Autowired
	private RefundDao refundDao;
	@Autowired
	private PayCheckService payCheckService;
	@Autowired
	private PayRemittanceService payRemittanceService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
    private MoneyAmountDao moneyAmountDao;
    @Autowired
    private IslandMoneyAmountService islandMoneyAmountService;
    @Autowired
    private HotelMoneyAmountService hotelMoneyAmountService;
    @Autowired
    private PayBanktransferDao payBanktransferDao;
    @Autowired
    private PayPosDao payPosDao;
    @Autowired
    private PayDraftDao payDraftDao;
	@Autowired
	private PayAlipayDao payAlipayDao;
    @Autowired
    private CostRecordDao costRecordDao;
    @Autowired
    private CostRecordHotelDao costRecordHotelDao;
    @Autowired
    private CostRecordIslandDao costRecordIslandDao;
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private AirticketOrderMoneyAmountService airticketOrderMoneyAmountService;
    @Autowired
    private PayTypeService payTypeService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private VisaFlowBatchOprationDao visaFlowBatchOprationDao;
    @Autowired
    private PayFeeService payFeeService;
    
	/**
	 * 取得付款的全部信息
	 * 
	 * @param payId
	 * @return
	 */
	public PayInfoDetail getPayInfoByPayId(String payId,String orderType) {

		PayInfoDetail payInfoDetail = new PayInfoDetail();

		/**
		 * 取得支付的基本信息
		 */
		Refund refund = refundDao.findPayInfoByPayId(payId,Integer.valueOf(orderType));
		if (refund == null) {
			return payInfoDetail;
		}
		copyRefundInfo(refund, payInfoDetail);

		Integer payType = refund.getPayType();
		String payTypeId = refund.getPayTypeId();
		switch (payType) {
		// 支票
		case 1:
			PayCheck payCheck = payCheckService.findPayCheckInfoById(payTypeId);
			copyPayCheckInfo(payCheck, payInfoDetail);
			break;
		// 现金
		case 3:
			break;
		// 汇款
		case 4:
			PayRemittance payRemittance = payRemittanceService
					.findPayRemittanceInfoById(payTypeId);
			copyPayRemittance(payRemittance, payInfoDetail);
			break;
		//转帐
		case 6:
			PayBanktransfer payBanktransfer = payBanktransferDao.getByUuid(payTypeId);
			copyPayBankTransfer(payBanktransfer, payInfoDetail);
			break;
		case 7://汇票
			PayDraft payDraft = payDraftDao.getByUuid(payTypeId);
			copyPayDraft(payDraft,payInfoDetail);
		case 8://POS机刷卡
			PayPos payPos = payPosDao.getByUuid(payTypeId);
			copyPayPos(payPos,payInfoDetail);
			break;
		case 9: // 支付宝
			PayAlipay alipay = payAlipayDao.getByUuid(payTypeId);
			copyPayAlipay(alipay,payInfoDetail);
			break;
		default:
			// do nothing.
		}

		/**
		 * 取得金额信息
		 */
		String mergePayFlag = refund.getMergePayFlag();
		payInfoDetail.setMergePayFlag(mergePayFlag);
		String moneyDispStyle = "";
		List<Object[]> moneyAmounts = null;
		// 合并后支付
		if ("1".equals(mergePayFlag)) {
			if(Context.ORDER_TYPE_ISLAND==refund.getOrderType()){
				moneyDispStyle=islandMoneyAmountService.getMoneyStr(refund.getMergeMoneySerialNum(), true);
        	    //付款金额集合
				moneyAmounts = islandMoneyAmountService.getMoneyAmonut(refund.getMergeMoneySerialNum());
			}else if(Context.ORDER_TYPE_HOTEL==refund.getOrderType()){
				moneyDispStyle=hotelMoneyAmountService.getMoneyStr(refund.getMergeMoneySerialNum(), true);
        	    //付款金额集合
				moneyAmounts = hotelMoneyAmountService.getMoneyAmonut(refund.getMergeMoneySerialNum());
			}else{
				moneyDispStyle = moneyAmountService.getMergeMoney(refund.getMergeMoneySerialNum());
				//付款金额集合
				moneyAmounts = moneyAmountService.getMoneyAmonut(refund.getMergeMoneySerialNum());
			}
		} else {

           	if(Context.ORDER_TYPE_ISLAND==refund.getOrderType()){
        	   moneyDispStyle=islandMoneyAmountService.getMoneyStr(refund.getMoneySerialNum(), true);
        	   //付款金额集合
				moneyAmounts = islandMoneyAmountService.getMoneyAmonut(refund.getMoneySerialNum());
		   	}else if(Context.ORDER_TYPE_HOTEL==refund.getOrderType()){
				moneyDispStyle=hotelMoneyAmountService.getMoneyStr(refund.getMoneySerialNum(), true);
				
        	   //付款金额集合
				moneyAmounts = hotelMoneyAmountService.getMoneyAmonut(refund.getMoneySerialNum());
		   	}else{
				moneyDispStyle = moneyAmountService.getMoney(refund.getMoneySerialNum());
				//付款金额集合
				moneyAmounts = moneyAmountService.getMoneyAmonut(refund.getMoneySerialNum());
		   	}
		}
		payInfoDetail.setMoneyDispStyle(moneyDispStyle);

		//获取付款金额(付款金额减去手续费金额)
		Map<String, BigDecimal> refundMoneyMap = new HashMap<String, BigDecimal>();
		Map<String, String> currencyMarkMap = new HashMap<String, String>();
		
		BigDecimal RMBPrice = new BigDecimal(0);
		if(CollectionUtils.isNotEmpty(moneyAmounts)) {
			for(Object[] objArr : moneyAmounts) {
				refundMoneyMap.put(String.valueOf(objArr[0]), new BigDecimal(objArr[3].toString()));
				currencyMarkMap.put(String.valueOf(objArr[0]), String.valueOf(objArr[2]));
				RMBPrice = RMBPrice.add(new BigDecimal(objArr[3].toString()).multiply(new BigDecimal(objArr[4].toString())));
			}			
		}
		DecimalFormat dt = new DecimalFormat("#,##0.00");
		payInfoDetail.setRefundRMBDispStyle(dt.format(RMBPrice));
		//手续费集合
		List<PayFee> payFees = payFeeService.findByRefundId(payId);
		payInfoDetail.setPayFees(payFees);
		if(CollectionUtils.isNotEmpty(payFees)) {
			for(PayFee payFee : payFees) {
				BigDecimal money = refundMoneyMap.get(String.valueOf(payFee.getFeeCurrencyId()));
				if(money != null) {
					money = money.subtract(new BigDecimal(payFee.getFeeAmount().toString()));
				}
				refundMoneyMap.put(String.valueOf(payFee.getFeeCurrencyId()), money);
			}
		}
		StringBuffer sb = new StringBuffer();
		Set<String> currencyIdSet = refundMoneyMap.keySet();
		if(CollectionUtils.isNotEmpty(currencyIdSet)) {
			for(String currencyId : currencyIdSet) {
				sb.append(currencyMarkMap.get(currencyId));
				sb.append(refundMoneyMap.get(currencyId));
				sb.append("+");
			}
			sb.deleteCharAt(sb.length()-1);
			payInfoDetail.setRefundDispStyle(sb.toString());
		}
		//获取付款金额(付款金额减去手续费金额)
		
		/**
		 * 取得凭证信息
		 */
		String payVoucher = refund.getPayVoucher();
		List<DocInfo> docInfoList = docInfoService.getDocInfoBydocids(payVoucher);
		payInfoDetail.setDocInfoList(docInfoList);

		return payInfoDetail;
	}

	/**
	 * 将PayDraft对象中的值复制到PayInfoDetail对象中
	 * 
	 * @param PayDraft
	 * @param payInfoDetail
	 */
	private void copyPayDraft(PayDraft payDraft,PayInfoDetail payInfoDetail){
		
		if(payDraft != null){
			payInfoDetail.setDrawerName(payDraft.getDrawerName());
			payInfoDetail.setDraftAccountedDate(payDraft.getDraftAccountedDateString());
			payInfoDetail.setDrawerAccount(payDraft.getDrawerAccount());
			payInfoDetail.setPayBankName(payDraft.getPayBankName());
			payInfoDetail.setTobankAccount(payDraft.getReceiveAccount());
			if(StringUtils.isEmpty(payInfoDetail.getPayerName())) {
				payInfoDetail.setPayerName(payDraft.getReceiveCompanyName());
			}
			payInfoDetail.setTobankName(payDraft.getReceiveBankName());
		}
	}
	/**
	 * 将Refund对象中的值复制到PayInfoDetail对象中
	 * 
	 * @param refund
	 * @param payInfoDetail
	 */
	private void copyRefundInfo(Refund refund, PayInfoDetail payInfoDetail) {
		if (refund != null) {
			payInfoDetail.setId(refund.getId());
			payInfoDetail.setPayType(refund.getPayType());
			if (refund.getPayType() == 9){
				payInfoDetail.setPayTypeName("因公支付宝");
			} else {
				payInfoDetail.setPayTypeName(DictUtils.getDictLabel(refund
						.getPayType().toString(), Context.ORDER_PAYTYPE, ""));
			}
			payInfoDetail.setPayTypeId(refund.getPayTypeId());
			payInfoDetail.setMoneySerialNum(refund.getMoneySerialNum());
			payInfoDetail.setPayVoucher(refund.getPayVoucher());
			payInfoDetail.setMergePayFlag(refund.getMergePayFlag());
			payInfoDetail.setMergeMoneySerialNum(refund
					.getMergeMoneySerialNum());
			payInfoDetail.setMoneyType(refund.getMoneyType());
			payInfoDetail.setStatus(refund.getStatus());
			payInfoDetail.setRecordId(refund.getRecordId());
			payInfoDetail.setRemarks(refund.getRemarks());
			payInfoDetail.setCreateDate(refund.getCreateDate());
			payInfoDetail.setPayerName(refund.getPayee());
			//20151016付款确认时间
			payInfoDetail.setUpdateDate(refund.getUpdateDate());
		}
	}

	/**
	 * 将PayCheck对象中的值复制到PayInfoDetail对象中
	 * 
	 * @param payCheck
	 * @param payInfoDetail
	 */
	private void copyPayCheckInfo(PayCheck payCheck, PayInfoDetail payInfoDetail) {
		if (payCheck != null) {
			//付款和收款单位取值问题
			if(StringUtils.isEmpty(payInfoDetail.getPayerName())) {
				payInfoDetail.setPayerName(payCheck.getPayerName());
			}
			payInfoDetail.setCheckNumber(payCheck.getCheckNumber());
			payInfoDetail.setInvoiceDate(payCheck.getInvoiceDate());
		}
	}

	/**
	 * 将PayRemittance对象中的值复制到PayInfoDetail对象中
	 * 
	 * @param payRemittance
	 * @param payInfoDetail
	 */
	private void copyPayRemittance(PayRemittance payRemittance,
			PayInfoDetail payInfoDetail) {
		if (payRemittance != null) {
			payInfoDetail.setBankName(payRemittance.getBankName());
			payInfoDetail.setBankAccount(payRemittance.getBankAccount());
			payInfoDetail.setTobankName(payRemittance.getTobankName());
			payInfoDetail.setTobankAccount(payRemittance.getTobankAccount());
		}
	}
	/**
	 * 将PayBanktransfer对象中的值复制到PayInfoDetail对象中
	 * 
	 * @param payBanktransfer
	 * @param payInfoDetail
	 */
	private void copyPayBankTransfer(PayBanktransfer payBanktransfer,PayInfoDetail payInfoDetail){
		if(payBanktransfer != null){
			payInfoDetail.setBankName(payBanktransfer.getPayBankName());
			payInfoDetail.setBankAccount(payBanktransfer.getPayAccount());
			payInfoDetail.setTobankAccount(payBanktransfer.getReceiveAccount());
			payInfoDetail.setTobankName(payBanktransfer.getReceiveBankName());
			if(StringUtils.isEmpty(payInfoDetail.getPayerName())) {
				payInfoDetail.setPayerName(payBanktransfer.getReceiveCompanyName());
			}
		}
	}
	
	/**
	 * 将PayBanktransfer对象中的值复制到PayInfoDetail对象中
	 * 
	 * @param payPos
	 * @param payInfoDetail
	 */
	private void copyPayPos(PayPos payPos,PayInfoDetail payInfoDetail){
		if(payPos != null){
			payInfoDetail.setTobankAccount(payPos.getReceiveAccount());
			payInfoDetail.setTobankName(payPos.getReceiveBankName());
			if(StringUtils.isEmpty(payInfoDetail.getPayerName())) {
				payInfoDetail.setPayerName(payPos.getReceiveCompanyName());
			}
		}
	}

	private void copyPayAlipay(PayAlipay payAlipay,PayInfoDetail payInfoDetail){
		if (payAlipay != null){
			payInfoDetail.setFromAlipayName(payAlipay.getFromAlipayName());
			payInfoDetail.setFromAlipayAccount(payAlipay.getFromAlipayAccount());
			payInfoDetail.setToAlipayName(payAlipay.getToAlipayName());
			payInfoDetail.setToAlipayAccount(payAlipay.getToAlipayAccount());
			if (StringUtils.isEmpty(payInfoDetail.getReceiveCompanyName())){
				payInfoDetail.setReceiveCompanyName(payAlipay.getReceiveCompanyName());
			}
		}
	}
	/**
	 * 根据recordId获取付款记录
	 * @param id  
	 * @param type 1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款 ；5：退签证押金付款
	 * @return 
	 * */
	public List<Map<Object, Object>> findbyRecordId(String id,String type,String orderType){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT sysdict.label,refund.id,refund.orderType,")
		   .append(" refund.createdate,refund.status status, refund.payvoucher,")
		   .append(" (case refund.`status` when '0' then '已撤销' else '已支付' end) opstatus, ")
		   .append(" (CASE refund.moneyType WHEN '1' THEN '成本付款' WHEN '2' THEN '退款付款' ")
		   .append(" WHEN '3' THEN '返佣付款' WHEN '4' THEN '借款付款' WHEN '5' THEN '退签证押金付款' ")
		   .append(" WHEN '6' THEN '追加成本付款' WHEN '7' THEN '成本付款' WHEN '8' THEN '退款付款' ")
		   .append(" WHEN '9' THEN '返佣付款' WHEN '10' THEN '借款付款' WHEN '11' THEN '退签证押金付款' ")
		   .append(" WHEN '12' THEN '追加成本付款' WHEN '14' THEN '交易服务费付款' WHEN '15' THEN '交易服务费付款'")
		   .append(" ELSE '' END ) a, ")
		   .append(" (CASE refund.mergePayFlag WHEN 0 THEN (SELECT group_concat(concat(c.currency_mark,m.amount) SEPARATOR '+') " )
		   .append(" FROM " );
		if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_HOTEL.equals(orderType)){//酒店
			sql.append(" hotel_money_amount m ");
		}else if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ISLAND.equals(orderType)){//海岛游
			sql.append(" island_money_amount m ");
		}else{
			sql.append(" money_amount m " );
		}
		sql.append(",currency c WHERE m.serialNum=refund.money_serial_num and m.currencyId=c.currency_id) " )
		   .append(" WHEN 1 THEN (SELECT concat('¥',m.amount) ");
		if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_HOTEL.equals(orderType)){//酒店
			sql.append(" FROM hotel_money_amount m ");
		}else if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ISLAND.equals(orderType)){//海岛游
			sql.append(" FROM island_money_amount m ");
		}else{
			sql.append(" FROM money_amount m " );
	 	}
	    sql.append(" WHERE m.serialNum=refund.merge_money_serial_num ) ELSE '' END) amount ")
		   .append(" FROM (SELECT r.id,r.pay_type payType, r.create_date createdate, " )
		   .append(" r.pay_voucher payvoucher,r.status, r.moneyType, r.mergePayFlag, ")
		   .append(" r.money_serial_num,r.orderType, r.merge_money_serial_num FROM refund r WHERE ");
		if(StringUtils.isNotBlank(type)){
			sql.append(" r.moneyType = ").append(type).append(" and ");
		}
		sql.append(" r.orderType=").append(orderType).append(" and ").append(" r.record_id = ")
		   .append(id).append(" ) refund LEFT JOIN (SELECT VALUE ,label FROM sys_dict WHERE " )
		   .append(" type = 'offlineorder_pay_type' AND delFlag = '0') sysdict ")
		   .append(" ON refund.payType = sysdict.`VALUE` order by createDate DESC ");
       return refundDao.findBySql(sql.toString(), Map.class);
    }
	
	/**
	 * 成本付款撤销操作
	 * @param refundid  refund主键
	 * @param recordId  cost_record主键
	 * @return 
	 * */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public boolean undoOp(String refundid,String recordId){
		boolean succ = false;
		try{
			Long userId = UserUtils.getUser().getId();
			Refund refund = refundDao.findPayInfoByPayId(refundid);
			refund.setStatus("0");
			refundDao.save(refund);
			moneyAmountDao.updateMoneyType(refund.getMoneySerialNum(),
					Context.MONEY_TYPE_CANCEL);
			//add by shijun.liu 修改costrecord表的更新时间，调整成本付款排序，C162需求
			Integer orderType = refund.getOrderType();
			Integer moneyType = refund.getMoneyType();
			if(moneyType == Refund.MONEY_TYPE_COST || moneyType == Refund.MONEY_TYPE_NEWCOST){
				if(null != orderType && StringUtils.isNotBlank(recordId)){
					if(Context.ORDER_TYPE_HOTEL == orderType){
						costRecordHotelDao.updateOptionCostRecord(userId, new Date(), Long.valueOf(recordId));
					}else if(Context.ORDER_TYPE_ISLAND == orderType){
						costRecordIslandDao.updateOptionCostRecord(userId, new Date(), Long.valueOf(recordId));
					}else {
						costRecordDao.updateOptionCostRecord(userId, new Date(), Long.valueOf(recordId));
					}
				}
			}else{
				if(StringUtils.isNotBlank(recordId)){
					//2 退款付款，3，返佣付款，4，借款付款，5，退签证押金
					if(moneyType == Refund.MONEY_TYPE_RETURNMONEY || moneyType == Refund.MONEY_TYPE_PAYBACK 
							|| moneyType == Refund.MONEY_TYPE_BORROW || moneyType == Refund.MONEY_TYPE_RETURNVISADEPOSIT){
						reviewDao.updateOptionReview(userId, new Date(), Long.valueOf(recordId));
					}
					//8 退款付款，9，返佣付款，10，借款付款，11，退签证押金 
					if(moneyType == Refund.MONEY_TYPE_NEWRETURNMONEY ||
							moneyType == Refund.MONEY_TYPE_NEWPAYBACK || moneyType == Refund.MONEY_TYPE_NEWBORROW 
							|| moneyType == Refund.MONEY_TYPE_NEWRETURNVISADEPOSIT ){
						reviewService.update(Long.valueOf(recordId), String.valueOf(userId), new Date());
					}
					//环球行签证借款付款  C162需求
					if(moneyType == Refund.MONEY_TYPE_BATCHBORROW){
						VisaFlowBatchOpration visaBatch = visaFlowBatchOprationDao.findOne(Long.valueOf(recordId));
						if(null != visaBatch){
							if(2 == visaBatch.getIsNewReview()){
								reviewService.batchUpdate(visaBatch.getBatchNo(), String.valueOf(userId), new Date());
							}else{
								StringBuffer str = new StringBuffer();
								str.append(" update review r set r.updateBy = ?, r.updateDate = ? where ")
								   .append(" r.id in (select d.review_id from review_detail d where d.myKey = 'visaBorrowMoneyBatchNo' ")
								   .append(" and d.myValue = ? )");
								reviewDao.updateBySql(str.toString(), userId, new Date(), visaBatch.getBatchNo());
							}
						}
					}
				}
			}
			succ=true;
		}catch(Exception e){
			succ=false;
			e.printStackTrace();
		}
		return succ;
	}
	
	public List<PayInfoDetail> findByRecordIdAndOrderType(Long recordId,
			Integer orderType) {

		List<PayInfoDetail> pidList = new ArrayList<PayInfoDetail>();

		List<Refund> refundList = refundDao.findByRecordIdAndOrderType(
				recordId, orderType);
		for (Refund refund : refundList) {
			PayInfoDetail pid = this.getPayInfoByPayId(refund.getId(),String.valueOf(orderType));
			pidList.add(pid);
		}

		return pidList;
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean saveRefundByRefundJsonBean(RefundJsonBean refundJsonBean, BigDecimal convertLowest) {
		boolean flag = false;
		try{
			
			Refund refund = new Refund();
			refund.setId(UuidUtils.generUuid());
			refund.setPayType(Integer.parseInt(refundJsonBean.getPaymentMethodCode()));
			//保存付款支付方式
			payTypeService.savePayTypeInfoByRefundJsonBean(refundJsonBean);
			refund.setPayTypeId(refundJsonBean.getPayTypeId());
			
			//保存付款moneyAmount信息
			String moneySerialNum = UuidUtils.generUuid();
			refund.setMoneySerialNum(moneySerialNum);
			
			//保存美途国际moneyAmount金额表信息
			airticketOrderMoneyAmountService.saveMoneyAmount(moneySerialNum, new String[]{refundJsonBean.getCurrencyUuid().toString()}, new String[]{refundJsonBean.getPaymentAmount().toString()}, new String[]{convertLowest.toString()});
			
			StringBuffer payVoucherSb = new StringBuffer();
			if(CollectionUtils.isNotEmpty(refundJsonBean.getAttachments())) {
				for(DocInfoJsonBean jsonBean : refundJsonBean.getAttachments()) {
					payVoucherSb.append(jsonBean.getAttachmentUuid());
					payVoucherSb.append(",");
				}
				payVoucherSb.deleteCharAt(payVoucherSb.length()-1);
			}
			refund.setPayVoucher(payVoucherSb.toString());
			refund.setRemarks(refundJsonBean.getMemo());
			if(StringUtils.isEmpty(refundJsonBean.getMergePayFlag())) {
				refund.setMergePayFlag("0");
			} else {
				//合并支付情况处理，现只设置合并支付状态，不进行其他合并支付操作（美途国际不使用合并支付）
				refund.setMergePayFlag(refundJsonBean.getMergePayFlag());
			}
			//airticketOrderMoneyAmount的款项类型（1、借款；2、退款；3、追加成本；4、成本；）
			//refund的款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款）
			Integer fundsType = Integer.parseInt(refundJsonBean.getFundsType());
			Integer moneyType = null;
			if(fundsType != null) {
				switch(fundsType) {
				case 1:moneyType=4;break;//借款
				case 2:moneyType=2;break;//退款
				case 3:moneyType=6;break;//追加成本
				case 4:moneyType=1;break;//成本
				}
			}
			
			refund.setMoneyType(moneyType);
			
			//需要前端传递属性，款项id
			/*List<AirticketOrderMoneyAmount> airticketOrderMoneyAmounts =  airticketOrderMoneyAmountDao.getByOrderIdAndMoneyType(Integer.parseInt(refundJsonBean.getOrderUuid()), refund.getMoneyType());
			if(CollectionUtils.isNotEmpty(airticketOrderMoneyAmounts) && airticketOrderMoneyAmounts.size() == 1) {
				refund.setRecordId(airticketOrderMoneyAmounts.get(0).getId().longValue());
			}*/
			
			// '业务表ID、其中包括(review表id、cost_record表id、airticket_order_moneyAmount表id)'
			refund.setRecordId(Long.parseLong(refundJsonBean.getPaymentUuid()));
			// 收款单位
			refund.setPayee(refundJsonBean.getReceiveCompany());
			//订单类型
			refund.setOrderType(Context.ORDER_TYPE_JP);
			//批发商uuid
			refund.setCompanyUuid(UserUtils.getUser().getCompany().getUuid());
			
			//生成付款的批次号信息
			refund.setBatchNumber(UuidUtils.generUuid());
			refundDao.save(refund);
			
			flag = true;
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return flag;
	}
	
	/**
	 * 根据美途接口传递json，批量保存付款记录信息
	 * @Description: 
	 * @param @param refundJsonBean
	 * @param @param convertLowest
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean batchSaveRefundByRefundJsonBean(RefundJsonBean refundJsonBean) {
		boolean flag = false;
		try{

			//待付款记录
			List<Refund> toSaveRefunds = new ArrayList<Refund>();
			//付款信息集合
			List<PaymentJsonBean> payments = refundJsonBean.getPayments();
			
			//生成批次号
			String batchNumber = UuidUtils.generUuid(); 
			//保存付款支付方式
			payTypeService.savePayTypeInfoByRefundJsonBean(refundJsonBean);
			
			//付款的附件凭证信息
			StringBuffer payVoucherSb = new StringBuffer();
			if(CollectionUtils.isNotEmpty(refundJsonBean.getAttachments())) {
				for(DocInfoJsonBean jsonBean : refundJsonBean.getAttachments()) {
					payVoucherSb.append(jsonBean.getAttachmentUuid());
					payVoucherSb.append(",");
				}
				payVoucherSb.deleteCharAt(payVoucherSb.length()-1);
			}
			
			if(CollectionUtils.isNotEmpty(payments)) {
				for(PaymentJsonBean payment : payments) {
					//配置金额可以设置为空，如果为空则直接退出本次循环
					if(payment.getPaymentAmount() == null) {
						continue;
					}
					
					Refund refund = new Refund();
					refund.setBatchNumber(batchNumber);
					refund.setId(UuidUtils.generUuid());
					refund.setPayType(Integer.parseInt(refundJsonBean.getPaymentMethodCode()));
					//保存付款支付方式
					refund.setPayTypeId(refundJsonBean.getPayTypeId());

					//保存付款moneyAmount信息
					String moneySerialNum = UuidUtils.generUuid();
					refund.setMoneySerialNum(moneySerialNum);
					//保存美途国际moneyAmount金额表信息
					airticketOrderMoneyAmountService.saveMoneyAmount(moneySerialNum, new String[]{payment.getCurrencyUuid().toString()}, new String[]{payment.getPaymentAmount().toString()}, new String[]{payment.getExchangeRate().toString()});
					refund.setPayVoucher(payVoucherSb.toString());
					refund.setRemarks(refundJsonBean.getMemo());
					if(StringUtils.isEmpty(refundJsonBean.getMergePayFlag())) {
						refund.setMergePayFlag("0");
					} else {
						//合并支付情况处理，现只设置合并支付状态，不进行其他合并支付操作（美途国际不使用合并支付）
						refund.setMergePayFlag(refundJsonBean.getMergePayFlag());
					}
					//airticketOrderMoneyAmount的款项类型（1、借款；2、退款；3、追加成本；4、成本；）
					//refund的款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款）
					Integer fundsType = Integer.parseInt(payment.getFundsType());
					Integer moneyType = null;
					if(fundsType != null) {
						switch(fundsType) {
							case 1:moneyType=4;break;//借款
							case 2:moneyType=2;break;//退款
							case 3:moneyType=6;break;//追加成本
							case 4:moneyType=1;break;//成本
						}
					}
					refund.setMoneyType(moneyType);
					
					// '业务表ID、其中包括(review表id、cost_record表id、airticket_order_moneyAmount表id)'
					refund.setRecordId(Long.parseLong(payment.getPaymentUuid()));
					
					// 收款单位
					refund.setPayee(refundJsonBean.getReceiveCompany());
					//订单类型
					refund.setOrderType(Context.ORDER_TYPE_JP);
					//批发商uuid
					refund.setCompanyUuid(UserUtils.getUser().getCompany().getUuid());
					
					toSaveRefunds.add(refund);
				}
			}
			
			//批量保存付款信息
			refundDao.batchSave(toSaveRefunds);
			
			flag = true;
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return flag;
	}
	
	public static String getMoneyTypeNameByMoneyType(Integer moneyType) {
		if(moneyType == null) {
			return "";
		}
		String moneyTypeName = "";
		switch(moneyType){
			//款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款）
			case 1: moneyTypeName = "成本录入付款";break;
			case 2: moneyTypeName = "退款付款";break;
			case 3: moneyTypeName = "返佣付款";break;
			case 4: moneyTypeName = "借款付款";break;
		}
		
		return moneyTypeName;
	}

	/**
	 * 根据成本记录id或追加成本记录id获取未撤销的付款信息
	 * @Description: 
	 * @param @param recordId
	 * @param @return   
	 * @return List<Refund>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-9
	 */
	public List<Refund> getRefundsByRecordId(Long recordId, Integer moneyType) {
		return refundDao.getRefundsByRecordId(recordId, moneyType);
	}
	
	public List<Refund> findLastPayByRecordIdAndStatus(Long recordId) {
		return refundDao.findLastPayByRecordIdAndStatus(recordId);
	}

	/**
	 * 根据成本记录id和成本类型
	 * @Description:
	 * @param @param recordId
	 * @param @return
	 * @return List<Refund>
	 * @author shijun.liu
	 * @date 2016-04-25
	 */
	public List<Refund> getRefunds(Long recordId, Integer moneyType) {
		return refundDao.findLastPayByRecordId(recordId, moneyType);
	}

	public List<Refund> findLastPayByRecordId(long recordId, int moneyType) {
		// TODO Auto-generated method stub
		return refundDao.findLastPayByRecordId(recordId, moneyType);
	}
}
