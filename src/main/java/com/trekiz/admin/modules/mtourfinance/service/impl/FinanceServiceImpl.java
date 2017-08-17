package com.trekiz.admin.modules.mtourfinance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.*;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.repository.FlightInfoDao;
import com.trekiz.admin.modules.airticket.service.ActivityAirTicketServiceImpl;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.mtourCommon.service.SerialNumberService;
import com.trekiz.admin.modules.mtourOrder.dao.*;
import com.trekiz.admin.modules.mtourOrder.entity.*;
import com.trekiz.admin.modules.mtourOrder.jsonbean.MtourOrderJsonBean;
import com.trekiz.admin.modules.mtourOrder.jsonbean.MtourOrderSearchJsonBean;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderMoneyAmountService;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderPnrAirlinePriceService;
import com.trekiz.admin.modules.mtourOrder.service.AirticketOrderPnrService;
import com.trekiz.admin.modules.mtourOrder.service.MtourOrderService;
import com.trekiz.admin.modules.mtourfinance.entity.PriceAmount;
import com.trekiz.admin.modules.mtourfinance.json.*;
import com.trekiz.admin.modules.mtourfinance.pojo.*;
import com.trekiz.admin.modules.mtourfinance.repository.FinanceDao;
import com.trekiz.admin.modules.mtourfinance.service.FinanceService;
import com.trekiz.admin.modules.mtourfinance.transfer.DocInfoTransfer;
import com.trekiz.admin.modules.mtourfinance.transfer.OrderpayTransfer;
import com.trekiz.admin.modules.mtourfinance.util.CommonUtils;
import com.trekiz.admin.modules.mtourfinance.util.ComparatorDate;
import com.trekiz.admin.modules.mtourfinance.util.ExcelUtils;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.entity.*;
import com.trekiz.admin.modules.order.repository.*;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.pay.dao.PayGroupDao;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.pay.service.PayGroupService;
import com.trekiz.admin.modules.supplier.repository.SupplierInfoDao;
import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.repository.*;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author trekiz
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class FinanceServiceImpl extends BaseService implements FinanceService {
	@Autowired
	FinanceDao financeDao;
	@Autowired
	private OrderPayService orderPayService;
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	OfficeDao officeDao;
	@Autowired
	PlatBankInfoDao platBankInfoDao;
	@Autowired
	private PayGroupDao payGroupDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private ActivityAirTicketServiceImpl activityAirTicketServiceImpl;
	@Autowired
	private OrderpayDao orderpayDao;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private AirticketOrderPnrDao airticketOrderPnrDao;
	@Autowired
	private AirticketOrderPnrAirlineDao airticketOrderPnrAirlineDao;
	@Autowired
	private AirticketOrderPnrAirlinePriceDao airticketOrderPnrAirlinePriceDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private RefundDao refundDao;
	@Autowired
	private PayCheckDao payCheckDao;
	@Autowired
	private PayRemittanceDao payRemittanceDao;
	@Autowired
	private AirticketOrderMoneyAmountDao airticketOrderMoneyAmountDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private AirticketPreOrderDao airticketPreOrderDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private AirticketOrderMoneyAmountService airticketOrderMoneyAmountService;
	@Autowired
	private RefundService refundService;

	@Autowired
	private CostManageService costManageService;

	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private AgentinfoDao agentinfoDao;
	@Autowired
	private AirticketOrderChangePriceDao airticketOrderChangePriceDao;

	@Autowired
	private MtourOrderService mtourOrderService;
	@Autowired
	private PayGroupService payGroupService;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private SupplierInfoDao supplierInfoDao;
	@Autowired
	private CurrencyService currencyService;
    @Autowired
    FlightInfoDao flightInfoDao;
    @Autowired
    AirportInfoDao airportInfoDao;
    @Autowired
    private AirticketOrderMtourDao airticketOrderMtourDao;
    
    @Autowired
	private AirticketOrderPnrService airticketOrderPnrService;
	@Autowired
	private AirticketOrderPnrAirlinePriceService airticketOrderPnrAirlinePriceService;
    
    private static PropertiesLoader propertiesLoader = new PropertiesLoader("application.properties");
    
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public OrderpayJsonBean getPayInfo(BaseInput4MT input) throws Exception {
		//收款id；款项类型为2是其他收入收款、1为订单收款
		String receiveId = input.getParamValue("receiveUuid");
		String orderUuid = input.getParamValue("orderUuid");
		//款项类型 
		String fundsType = input.getParamValue("fundsType");

		if (StringUtils.isEmpty(receiveId)) {
			return null;
		}

		try {
			
			//附件id字符串，格式：12,123,1234,
			String payVoucher = null;
			String moneySerialNum = null;

			OrderpayJsonBean orderpayJsonBean = new OrderpayJsonBean();
			
			//其他收入收款详情
			if("2".equals(fundsType)) {
				PayGroup payGroup = payGroupDao.getById(Integer.parseInt(receiveId));
				payVoucher = payGroup.getPayVoucher();
				
				List<DocInfo> docInfos = null;
				// 获取订单的支付凭证信息
				if (StringUtils.isNotBlank(payVoucher)) {
					String[] docId = payVoucher.split(",");
					List<Long> docIdList = new ArrayList<Long>();
					for (int index = 0; index < docId.length; index++) {
						docIdList.add(Long.parseLong(docId[index]));
					}
					docInfos = docInfoService.getDocInfoByIds(docIdList);
				}
				
				orderpayJsonBean = OrderpayTransfer.transferPayInfo2JsonBean(payGroup, docInfos);
				CostRecord costRecord = costRecordDao.getById(payGroup.getCostRecordId().longValue());
				
				//地接社或者渠道商值->	前端需要数据（地接社：1;渠道商	2） 美途国际数据库中：2: 供应商, 1:渠道商
				orderpayJsonBean.setTourOperatorChannelCategoryCode(costRecord.getSupplyType());
				
				//地接社/渠道商的类型Code
				orderpayJsonBean.setTourOperatorOrChannelTypeCode(costRecord.getSupplierType());
				//地接社/渠道商 的名称
				orderpayJsonBean.setTourOperatorOrChannelName(costRecord.getSupplyName());
				//地接社/渠道商 的Uuid
				orderpayJsonBean.setTourOperatorOrChannelUuid(String.valueOf(costRecord.getSupplyId()));
				
				moneySerialNum = payGroup.getPayPrice();
			} else {
				// 根据订单支付ID查找订单支付信息
				Orderpay orderpay = orderPayService.findOrderpayById(Long.valueOf(receiveId));

				payVoucher = orderpay.getPayVoucher();
				
				List<DocInfo> docInfos = null;
				// 获取订单的支付凭证信息
				if (StringUtils.isNotBlank(payVoucher)) {
					String[] docId = payVoucher.split(",");
					List<Long> docIdList = new ArrayList<Long>();
					for (int index = 0; index < docId.length; index++) {
						docIdList.add(Long.parseLong(docId[index]));
					}
					docInfos = docInfoService.getDocInfoByIds(docIdList);
				}
				
				orderpayJsonBean = OrderpayTransfer.transferPayInfo2JsonBean(orderpay, docInfos);
				
				moneySerialNum = orderpay.getMoneySerialNum();
			}
			
			orderpayJsonBean.setOrderUuid(orderUuid);
			// 获取并设置金额信息
			
			MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(moneySerialNum);
			if (moneyAmount != null) {
				orderpayJsonBean.setCurrencyUuid(moneyAmount.getCurrencyId().toString());
				if(moneyAmount.getExchangerate() != null) {
					orderpayJsonBean.setExchangeRate(moneyAmount.getExchangerate().doubleValue());
				}
				orderpayJsonBean.setReceiveAmount(moneyAmount.getAmount().doubleValue());
			}

			return orderpayJsonBean;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public RefundJsonBean getRefundInfo(BaseInput4MT input) throws Exception {

		RefundJsonBean refundJsonBean = new RefundJsonBean();

		String orderUuid = input.getParamValue("orderUuid");
		String paymentUuid = input.getParamValue("paymentUuid");
		String paymentDetailUuid = input.getParamValue("paymentDetailUuid");
		Refund refund = refundDao.findPayInfoByPayId(paymentDetailUuid);

		// 为付款记录详情赋值
		refundJsonBean.setOrderUuid(orderUuid);
		refundJsonBean.setPaymentUuid(paymentUuid);
		refundJsonBean.setPaymentDetailUuid(paymentDetailUuid);
		
		refundJsonBean.setFundsType(String.valueOf(refund.getMoneyType()));//'款项类型'
		refundJsonBean.setFundsName(RefundService.getMoneyTypeNameByMoneyType(refund.getMoneyType()));//'款项名称'
		refundJsonBean.setReceiveCompany(refund.getPayee());
		refundJsonBean.setMemo(refund.getRemarks());
		refundJsonBean.setPaymentDate(refund.getCreateDate());
		
		//获取付款金额
		//(currencyId,currency_name,currency_mark,sum(amount),exchangerate)集合
		List<Object[]> amountInfos = airticketOrderMoneyAmountService.getMoneyAmountsByRefund(refund);
		if(CollectionUtils.isNotEmpty(amountInfos)) {
			BigDecimal countAmount = new BigDecimal(0);
			Integer currencyId = null;
			for(Object[] objArr : amountInfos) {
				countAmount = countAmount.add(new BigDecimal(objArr[3].toString()));
				currencyId = Integer.parseInt(objArr[0].toString());
				//设置付款详情汇率
				if(objArr[4] != null) {
					refundJsonBean.setExchangeRate(new BigDecimal(objArr[4].toString()).doubleValue());
				}
			}
			refundJsonBean.setPaymentAmount(countAmount.doubleValue());
			refundJsonBean.setCurrencyUuid(currencyId);
		}

		int payType = refund.getPayType();
		refundJsonBean.setPaymentMethodCode(String.valueOf(payType));//'付款方式'
		String payTypeUuid = refund.getPayTypeId();

		switch (payType) {
		case 1:// 支票支付
			PayCheck payCheck = payCheckDao.findPayCheckInfoById(payTypeUuid);
			refundJsonBean.setCheckNo(payCheck.getCheckNumber());
			refundJsonBean.setCheckIssueDate(payCheck.getInvoiceDate());
			break;
		case 4:// 汇款
			PayRemittance payRemittance = payRemittanceDao.findPayRemittanceInfoById(payTypeUuid);
			refundJsonBean.setPaymentBank(payRemittance.getBankName());
			refundJsonBean.setPaymentAccount(payRemittance.getBankAccount());
			refundJsonBean.setReceiveBank(payRemittance.getTobankName());
			refundJsonBean.setReceiveAccount(payRemittance.getTobankAccount());
			break;
		case 6:// 银行转账
			/*
			 * PayBanktransfer payBanktransfer =
			 * payBanktransferDao.getByUuid(payTypeUuid);
			 */
			break;
		case 7:// 汇票
			/* PayDraft payDraft = payDraftDao.getByUuid(payTypeUuid); */
			break;
		case 8:// POS机刷卡
			/* PayPos payPost = payPosDao.getByUuid(payTypeUuid); */
			break;
		}

		List<DocInfo> docInfos = null;
		// 获取订单的支付凭证信息
		if (StringUtils.isNotBlank(refund.getPayVoucher())) {
			String[] docId = refund.getPayVoucher().split(",");
			List<Long> docIdList = new ArrayList<Long>();
			for (int index = 0; index < docId.length; index++) {
				docIdList.add(Long.parseLong(docId[index]));
			}
			docInfos = docInfoService.getDocInfoByIds(docIdList);
		}

		refundJsonBean.setAttachments(DocInfoTransfer.transferDocInfos2JsonBeans(docInfos));

		return refundJsonBean;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean undoRefundPayInfo(BaseInput4MT input) throws Exception {
		//付款记录id
		String refundId = input.getParamValue("paymentDetailUuid");
		// 付款撤销操作
		Refund refund = refundDao.findPayInfoByPayId(refundId);
		
		//-----------------------初始化订单付款状态 add by majiancheng
		/**
		Long orderId = null;
		if(refund.getMoneyType() == 1) {
			CostRecord costRecord = costRecordDao.getById(refund.getRecordId());
			orderId = costRecord.getOrderId();
		} else {
			AirticketOrderMoneyAmount moneyAmount = airticketOrderMoneyAmountDao.getById(refund.getRecordId().intValue());
			orderId = moneyAmount.getAirticketOrderId().longValue();
		}*/
		return this.undoRefundPayInfo(refund);
	}

	@Override
	public ConfirmSheet getConfirmSheetData(BaseInput4MT input,HttpServletRequest request) {
		String orderId= input.getParamValue("orderUuid");
		if(StringUtils.isBlank(orderId) && request != null){
			orderId= request.getParameter("orderUuid");
		}
		String id = input.getParamValue("supplyId");
		if(StringUtils.isBlank(id)){
			id=UserUtils.getUser().getCompany().getId().toString();
		}
		// TODO Auto-generated method stub
		ConfirmSheet cs = new ConfirmSheet();
		// 获取批发商信息
		Office o = officeDao.findById(Long.valueOf(id));
		if (o != null) {
			cs.setCompanyName(o.getName());
			cs.setCompanyName_EN(o.getEnname());
			cs.setTel(o.getPhone());
			cs.setTax(o.getFax());
			cs.setAddressee(o.getAddress());
			cs.setAddressee_EN(o.getEnAddress());
		}
		// 获取批发商默认的境外账户信息
		PlatBankInfo outbank = platBankInfoDao.findBankInfo(Long.valueOf(id),0, 2L,"4");
		OverseasAccount oa = new OverseasAccount();
		if (outbank != null) {
			
			oa.setAccountName(outbank.getAccountName()!=null?outbank.getAccountName():"");
			oa.setRouting(outbank.getRounting()!=null?outbank.getRounting():"");
			oa.setAccount(outbank.getBankAccountCode()!=null?outbank.getBankAccountCode():"");
			oa.setSwiftNumber(outbank.getSwiftNum()!=null?outbank.getSwiftNum():"");
			oa.setAccountBank(outbank.getBankName()!=null?outbank.getBankName():"");
			oa.setAccountBankAddress(outbank.getBankAddr()!=null?outbank.getBankAddr():"");
			oa.setAccountPhone(outbank.getPhoneNum()!=null?outbank.getPhoneNum():"");
			
			
		}
		cs.setOverseasAccount(oa);
		// 获取批发商默认的境内账户信息
		PlatBankInfo inbank = platBankInfoDao.findBankInfo(Long.valueOf(id), 0,1L,"0");
		Account a = new Account();
		if (inbank != null) {
			
			a.setAccountName(inbank.getAccountName()!=null?inbank.getAccountName():"");
			a.setAccountBank(inbank.getBankName()!=null?inbank.getBankName():"");
			a.setAccount(inbank.getBankAccountCode()!=null?inbank.getBankAccountCode():"");

		}
		cs.setAccount(a);
		List<Map<String, Object>> list = financeDao.fetchinfo(Integer
				.valueOf(orderId));
		if (CollectionUtils.isNotEmpty(list)) {
			Map<String, Object> map = list.get(0);
			cs.setChannelName(map.get("agentName") != null ? map.get("agentName")
					.toString() : "");
			cs.setChannelTel(map.get("agentTel") != null ? map.get(
					"agentTel").toString() :"");
			cs.setOrderer(map.get("NAME") != null ? map.get("NAME").toString()
					: "");
			cs.setOrdererTel(map.get("mobile") != null ? map.get("mobile")
					.toString() : (map.get("phone") != null ? map.get("phone")
					.toString() : ""));
			cs.setGroupNo(map.get("group_code") != null ? map.get(
					"group_code").toString() : "");
			cs.setOrderDate(map.get("create_date") != null ? DateUtils
					.formatCustomDate((Date) map.get("create_date"),
							"yyyy-MM-dd") : "");
			String country = "";
			String fromc = "";
			String toc = "";
			if (map.get("country") != null) {
				country = map.get("country").toString();
			}
			if (map.get("fa") != null) {
				fromc = map.get("fa").toString();
			}
			if (map.get("aa") != null) {
				toc = map.get("aa").toString();
			}
			StringBuffer sb = new StringBuffer(country);
			sb.append("  ").append(map.get("journey") !=null?map.get("journey"):"");
//			if (StringUtils.isNotBlank(fromc) && StringUtils.isNotBlank(toc)) {
//				sb.append(" ").append(fromc).append("-").append(toc);
//			} else {
//				sb.append(fromc).append(toc);
//			}
			cs.setRouterInfo(sb.toString());
			cs.setProductName(map.get("productName") != null ? map.get(
					"productName").toString() : "");
			cs.setMemo(map.get("ordercomments") != null ? map.get(
					"ordercomments").toString() : "");
			cs.setAirticketRemark(map.get("remark") != null ? map.get("remark")
					.toString() : "");
		}
        //获取机票订单信息
		AirticketOrder ao = airticketOrderDao.getAirticketOrderById(Long.valueOf(orderId));
		//航班信息
		List<FlightInfo> fi= flightInfoDao.findByFlightInfoByAirTicketId(ao.getAirticketId());
		List<Flights> flights = copy2Flights(fi);
		cs.setFlights(flights);
		// 获取pnr航段价格信息
		List<AirticketOrderPnrAirlinePrice> priceList = getByAirticketOrderId(Integer.valueOf(orderId));
		// 获取pnr航段改价信息
		List<AirticketOrderChangePrice> changePriceList = getByAirticketOrderId(orderId);
		StringBuffer total = new StringBuffer();
		if (CollectionUtils.isNotEmpty(priceList)) {
			for (int i = 0; i < priceList.size(); i++) {
				Integer currecncyId = priceList.get(i).getCurrencyId();
				Double price = priceList.get(i).getPrice();
				Integer personNum = priceList.get(i).getPersonNum();
				Currency c = currencyDao.getById(Long.valueOf(currecncyId
						.toString()));
				if (i == priceList.size() - 1) {
					total.append(c != null ? c.getCurrencyMark() : "")
							.append(price.toString()).append("*")
							.append(personNum);
				} else {
					total.append(c != null ? c.getCurrencyMark() : "")
							.append(price.toString()).append("*")
							.append(personNum).append("+");
				}
			}
		}
		if (CollectionUtils.isNotEmpty(changePriceList)) {
			for (int h = 0; h < changePriceList.size(); h++) {
				Integer computeType = changePriceList.get(h).getComputeType();
				Double price = changePriceList.get(h).getPrice();
				Currency c = currencyDao.getById(Long.valueOf(changePriceList
						.get(h).getCurrencyId().toString()));
				
				if (0 == computeType) {
					if(price != null){
						total.append("+")
								.append(c != null ? c.getCurrencyMark() : "")
								.append(price.toString());
					}
				} else {
					if(price != null){
						total.append("-")
								.append(c != null ? c.getCurrencyMark() : "")
								.append(price.toString());
					}
				}

			}
		}
		cs.setTotal(total.toString());
		// pnr航段价和订单改价总和
		List<Map<String, Object>> pnr = airticketOrderPnrAirlinePriceDao
				.getTotalMoneyByOrderId(Long.valueOf(orderId));
		StringBuffer totalResult = new StringBuffer();
		if (CollectionUtils.isNotEmpty(pnr)) {
			for (int i = 0; i < pnr.size(); i++) {
				Map<String, Object> map = pnr.get(i);
				if (i == pnr.size() - 1) {
					Currency c = currencyDao.getById(Long.valueOf(map.get(
							"currency_id").toString()));
					if(map.get("money") != null){
						totalResult.append(c.getCurrencyMark()).append(
								map.get("money"));
					}
				} else {
					Currency c = currencyDao.getById(Long.valueOf(map.get(
							"currency_id").toString()));
					if(map.get("money") != null){
					totalResult.append(c.getCurrencyMark())
							.append(map.get("money")).append("+");
					}
				}
			}
		}
		cs.setTotalResult(totalResult.toString());
		return cs;
	}

	public List<Flights> copy2Flights(List<FlightInfo> l){
		List<Flights> list = new ArrayList<Flights>();
		if(CollectionUtils.isNotEmpty(l)){
			int num = l.size();
			for(int i = 0;i<num;i++){
				FlightInfo info = l.get(i);
				String flightNum = info.getFlightNumber();//航班号
				Date startTime = info.getStartTime();//出发时间
				Date arrivalTime =info.getArrivalTime();//到达时间
				String la = info.getLeaveAirport();//出发机场
				String da = info.getDestinationAirpost();//到达机场
				if(StringUtils.isNotBlank(flightNum) && StringUtils.isNotBlank(la) && StringUtils.isNotBlank(da) && startTime !=null && arrivalTime !=null ){
					//只有上述的5个条件同时满足时放到list里
					Flights f = new Flights();
					AirportInfo leav = airportInfoDao.findOne(Long.valueOf(la));
					AirportInfo arr = airportInfoDao.findOne(Long.valueOf(da));
					f.setArrivalAirport(arr!=null?arr.getAirportCode():da);
					f.setArrivalTime(DateUtils.formatCustomDate(arrivalTime, "yyyy-MM-dd HH:mm:ss"));
					f.setDepartureAirport(leav != null?leav.getAirportCode():la);
					f.setDepartureTime(DateUtils.formatCustomDate(startTime, "yyyy-MM-dd HH:mm:ss"));
					f.setFlightNo(flightNum);
					f.setMemo(info.getRemark());
					list.add(f);
				}
			}
		}
		return list;
	}
	
	private String genStr(List<Flights> list){
		StringBuffer str = new StringBuffer();
		if(CollectionUtils.isNotEmpty(list)){
			int size = list.size();
			for(int i=0;i<size;i++){
				str.append("1、").append(list.get(i).getFlightNo()).append("  ").append(list.get(i).getDepartureAirport()).append("-").append(list.get(i).getArrivalAirport())
				.append("  ").append(list.get(i).getDepartureTime()).append("  ").append(list.get(i).getArrivalTime()).append("\n       ").append(list.get(i).getMemo()).append("\n");
			}
		}
		return str.toString();
	}
	@SuppressWarnings("deprecation")
	public void genExcl(HttpServletRequest request,
			HttpServletResponse response, ConfirmSheet cs) {

		// 创建新的Excel 工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		Map<String, CellStyle> styleMap = ExcelUtils.createStyles(workbook);
		XSSFSheet sheet = workbook.createSheet("new sheet");
		try {
			sheet.setDefaultColumnWidth(12);// 设置单元格宽度
			sheet.setDefaultRowHeightInPoints(20);// 设置行高度

			CellRangeAddress c1 = new CellRangeAddress(0, 0, 0, 5);
			CellRangeAddress c2 = new CellRangeAddress(1, 1, 0, 5);
			CellRangeAddress c3 = new CellRangeAddress(2, 2, 0, 5);
			CellRangeAddress c4 = new CellRangeAddress(3, 3, 0, 5);
			CellRangeAddress c5 = new CellRangeAddress(4, 4, 1, 2);
			CellRangeAddress c6 = new CellRangeAddress(4, 4, 4, 5);
			CellRangeAddress c7 = new CellRangeAddress(5, 5, 1, 2);
			CellRangeAddress c8 = new CellRangeAddress(5, 5, 4, 5);
			CellRangeAddress c9 = new CellRangeAddress(6, 6, 1, 2);
			CellRangeAddress c10 = new CellRangeAddress(6, 6, 4, 5);
			CellRangeAddress c11 = new CellRangeAddress(7, 7, 0, 5);
			CellRangeAddress c12 = new CellRangeAddress(8, 8, 0, 5);
			CellRangeAddress c13 = new CellRangeAddress(9, 9, 1, 2);
			CellRangeAddress c14 = new CellRangeAddress(9, 9, 4, 5);
			CellRangeAddress c15 = new CellRangeAddress(10, 10, 1, 5);
			CellRangeAddress c17 = new CellRangeAddress(11, 20, 0, 0);
			CellRangeAddress c180 = new CellRangeAddress(11, 11, 1, 5);
			CellRangeAddress c18 = new CellRangeAddress(12, 15, 1, 5);
			CellRangeAddress c190 = new CellRangeAddress(16, 16, 1, 5);
			CellRangeAddress c19 = new CellRangeAddress(17, 20, 1, 5);
			CellRangeAddress c20 = new CellRangeAddress(21, 21, 1, 3);
			CellRangeAddress c21 = new CellRangeAddress(22, 22, 0, 5);
			CellRangeAddress c23 = new CellRangeAddress(23, 29, 0, 2);
			CellRangeAddress c24 = new CellRangeAddress(23, 29, 3, 5);
			CellRangeAddress c25 = new CellRangeAddress(30, 30, 0, 2);
			CellRangeAddress c26 = new CellRangeAddress(30, 30, 3, 5);
			CellRangeAddress c27 = new CellRangeAddress(31, 31, 1, 2);
			CellRangeAddress c28 = new CellRangeAddress(31, 31, 4, 5);
			CellRangeAddress c29 = new CellRangeAddress(32, 32, 1, 2);
			CellRangeAddress c30 = new CellRangeAddress(32, 32, 4, 5);
			CellRangeAddress c31 = new CellRangeAddress(33, 33, 1, 2);
			CellRangeAddress c32 = new CellRangeAddress(33, 33, 4, 5);
			CellRangeAddress c33 = new CellRangeAddress(21, 21, 4, 5);

			// 合并单元格
			sheet.addMergedRegion(c1);
			sheet.addMergedRegion(c2);
			sheet.addMergedRegion(c3);
			sheet.addMergedRegion(c4);
			sheet.addMergedRegion(c5);
			sheet.addMergedRegion(c6);
			sheet.addMergedRegion(c7);
			sheet.addMergedRegion(c8);
			sheet.addMergedRegion(c9);
			sheet.addMergedRegion(c10);
			sheet.addMergedRegion(c11);
			sheet.addMergedRegion(c12);
			sheet.addMergedRegion(c13);
			sheet.addMergedRegion(c14);
			sheet.addMergedRegion(c15);

			sheet.addMergedRegion(c17);
			sheet.addMergedRegion(c180);
			sheet.addMergedRegion(c18);
			sheet.addMergedRegion(c190);
			sheet.addMergedRegion(c19);
			sheet.addMergedRegion(c20);
			sheet.addMergedRegion(c21);
			sheet.addMergedRegion(c23);
			sheet.addMergedRegion(c24);
			sheet.addMergedRegion(c25);
			sheet.addMergedRegion(c26);
			sheet.addMergedRegion(c27);
			sheet.addMergedRegion(c28);
			sheet.addMergedRegion(c29);
			sheet.addMergedRegion(c30);
			sheet.addMergedRegion(c31);
			sheet.addMergedRegion(c32);
			sheet.addMergedRegion(c33);

			Row row1 = sheet.createRow(0);
			Cell cell1 = row1.createCell(0);
			Font f = workbook.createFont();
			f.setFontHeightInPoints((short) 16);
			f.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			f.setFontName("宋体");
			// 设置单元格样式
			CellStyle style = workbook.createCellStyle();
			style.setFont(f);// 单元格字体样式
			style.setAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);// 单元格字体居右
			cell1.setCellStyle(style);
			cell1.setCellValue(cs.getCompanyName());

			// 第二行
			Row row2 = sheet.createRow(1);
			Font f2 = workbook.createFont();
			f2.setFontHeightInPoints((short) 13);
			f2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// 设置单元格样式
			CellStyle style2 = workbook.createCellStyle();
			style2.setFont(f2);// 单元格字体样式
			style2.setAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style2.setAlignment(XSSFCellStyle.ALIGN_RIGHT);// 单元格字体居右
			Cell cell20 = row2.createCell(0);
			cell20.setCellStyle(style2);
			cell20.setCellValue(cs.getCompanyName_EN());

			// 第三行
			Row row3 = sheet.createRow(2);
			Font f3 = workbook.createFont();
			f3.setFontHeightInPoints((short) 11);
			f3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			CellStyle style3 = workbook.createCellStyle();
			style3.setFont(f3);// 单元格字体样式
			style3.setAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style3.setAlignment(XSSFCellStyle.ALIGN_RIGHT);// 单元格字体居右
			Cell cell30 = row3.createCell(0);
			cell30.setCellValue(cs.getAddressee()+"TEL:"+cs.getTel()+"  FAX:"+cs.getTax());
			cell30.setCellStyle(style3);

			// 第四行
			Row row4 = sheet.createRow(3);
			Cell cell40 = row4.createCell(0);
			Font f4 = workbook.createFont();
			f4.setFontHeightInPoints((short) 11);
			f4.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			CellStyle style4 = workbook.createCellStyle();
			style4.setFont(f4);// 单元格字体样式
			style4.setAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			style4.setAlignment(XSSFCellStyle.ALIGN_RIGHT);// 单元格字体居右
			cell40.setCellStyle(style4);
			cell40.setCellValue(cs.getAddressee_EN());

			// 第五行
			Row row5 = sheet.createRow(4);
			// 5行1列
			Cell cell50 = row5.createCell(0);
			cell50.setCellStyle(styleMap.get("titleStyle"));
			cell50.setCellValue("收件人");
			// 第五行第2列
			Cell cell51 = row5.createCell(1);
			cell51.setCellStyle(styleMap.get("contentStyle"));
			cell51.setCellValue(cs.getChannelName());

			// 第5行第3列
			Cell cell54 = row5.createCell(3);
			cell54.setCellStyle(styleMap.get("titleStyle"));
			cell54.setCellValue("发件人");
			// 第5行第4列
			Cell cell55 = row5.createCell(4);
			cell55.setCellStyle(styleMap.get("contentStyle"));
			cell55.setCellValue(cs.getOrderer());

			// 第6hang
			Row row6 = sheet.createRow(5);
			// 6行1列
			Cell cell60 = row6.createCell(0);
			cell60.setCellStyle(styleMap.get("titleStyle"));
			cell60.setCellValue("电话");
			// 6行2列
			Cell cell61 = row6.createCell(1);
			cell61.setCellStyle(styleMap.get("contentStyle"));
			cell61.setCellValue(cs.getChannelTel());
			// 6行三列
			Cell cell64 = row6.createCell(3);
			cell64.setCellStyle(styleMap.get("titleStyle"));
			cell64.setCellValue("电话");
			// 6行4列
			Cell cell65 = row6.createCell(4);
			cell65.setCellStyle(styleMap.get("contentStyle"));
			cell65.setCellValue(cs.getOrdererTel());

			// 第7hang
			Row row7 = sheet.createRow(6);
			// 6行1列
			Cell cell70 = row7.createCell(0);
			cell70.setCellStyle(styleMap.get("titleStyle"));
			cell70.setCellValue("页数");
			// 6行2列
			Cell cell71 = row7.createCell(1);
			cell71.setCellStyle(styleMap.get("contentStyle"));
			cell71.setCellValue("");
			// 6行三列
			Cell cell74 = row7.createCell(3);
			cell74.setCellStyle(styleMap.get("titleStyle"));
			cell74.setCellValue("日期");
			// 6行4列
			Cell cell75 = row7.createCell(4);
			cell75.setCellStyle(styleMap.get("contentStyle"));
			cell75.setCellValue(cs.getOrderDate());

			// 第8hang
			Row row8 = sheet.createRow(7);
			Cell cell80 = row8.createCell(0);
			cell80.setCellStyle(styleMap.get("titleStyle"));
			cell80.setCellValue("收款通知书");

			// 第9hang
			Row row9 = sheet.createRow(8);
			Cell cell90 = row9.createCell(0);
			cell90.setCellStyle(styleMap.get("titleStyle"));
			cell90.setCellValue("您好！承蒙大力支持，团队具体费用通知如下：");

			// 第10行
			Row row10 = sheet.createRow(9);
			Cell cell100 = row10.createCell(0);
			cell100.setCellStyle(styleMap.get("titleStyle"));
			cell100.setCellValue("团号");
			Cell cell101 = row10.createCell(1);
			cell101.setCellStyle(styleMap.get("contentStyle"));
			cell101.setCellValue(cs.getGroupNo());

			Cell cell102 = row10.createCell(3);
			cell102.setCellStyle(styleMap.get("titleStyle"));
			cell102.setCellValue("路线");

			Cell cell104 = row10.createCell(4);
			cell104.setCellStyle(styleMap.get("contentStyle"));
			cell104.setCellValue(cs.getRouterInfo());

			// 第11行
			Row row11 = sheet.createRow(10);
			Cell cell110 = row11.createCell(0);
			cell110.setCellStyle(styleMap.get("titleStyle"));
			cell110.setCellValue("名称");

			Cell cell111 = row11.createCell(1);
			cell111.setCellStyle(styleMap.get("titleStyle"));
			cell111.setCellValue("明细");

//			// 第13行
//			Row row110 = sheet.createRow(13);
//			Cell cell1100 = row110.createCell(0);
//			cell1100.setCellStyle(styleMap.get("contentStyle"));
//			cell1100.setCellValue(cs.getProductName());
//			Cell cell1110 = row110.createCell(1);
//			cell1110.setCellStyle(styleMap.get("bankInfoFont"));
//			cell1110.setCellValue(cs.getAirticketRemark());
			// 第13行
			Row row12 = sheet.createRow(11);
			Cell cell120 = row12.createCell(0);
			cell120.setCellStyle(styleMap.get("contentStyle"));
			cell120.setCellValue(cs.getProductName());
			Cell cell121 = row12.createCell(1);
			cell121.setCellStyle(styleMap.get("tipStyle"));
			cell121.setCellValue("烦请参考价格及航班见下：");
			//
			Row row13 = sheet.createRow(12);
			Cell cell131 = row13.createCell(1);
			cell131.setCellStyle(styleMap.get("bankInfoFont"));
			cell131.setCellValue(new XSSFRichTextString(genStr(cs.getFlights())));

			Row row16 = sheet.createRow(16);
			Cell cell161 = row16.createCell(1);
			cell161.setCellStyle(styleMap.get("tipStyle"));
			cell161.setCellValue("备注：");
			
			Row row17 = sheet.createRow(17);
			Cell cell171 = row17.createCell(1);
			cell171.setCellStyle(styleMap.get("bankInfoFont"));
			cell171.setCellValue(cs.getMemo());

			Row row22 = sheet.createRow(21);
			Cell cell220 = row22.createCell(0);
			cell220.setCellStyle(styleMap.get("titleStyle"));
			cell220.setCellValue("总计");

			Cell cell221 = row22.createCell(1);
			cell221.setCellStyle(styleMap.get("contentStyle"));
			cell221.setCellValue(cs.getTotal());

			Cell cell222 = row22.createCell(4);
			cell222.setCellStyle(styleMap.get("contentStyle"));
			cell222.setCellValue(cs.getTotalResult());

			Row row23 = sheet.createRow(22);
			Cell cell230 = row23.createCell(0);
			CellStyle style23 = workbook.createCellStyle();
			style23.setAlignment(XSSFCellStyle.ALIGN_LEFT);
			style23.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Font f23 = workbook.createFont();
			f23.setFontHeightInPoints((short) 10);
			f23.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			style23.setFont(f23);
			cell230.setCellStyle(style23);
			cell230.setCellValue("请将团款汇入以下账号或支票支付：");

			Row row24 = sheet.createRow(23);
			Cell cell240 = row24.createCell(0);
			cell240.setCellStyle(styleMap.get("bankInfoFont"));
			cell240.setCellValue(new XSSFRichTextString("开户名:"
					+ (cs.getOverseasAccount()== null ? "" : cs.getOverseasAccount().getAccountName()!=null?cs.getOverseasAccount().getAccountName():"") + " \n Routing:"
					+ (cs.getOverseasAccount()== null ? "" : cs.getOverseasAccount().getRouting()!=null?cs.getOverseasAccount().getRouting():""))
					+ " \n 账号:"
					+ (cs.getOverseasAccount()== null ? "" :cs.getOverseasAccount().getAccount()!=null?cs.getOverseasAccount().getAccount():"")
					+ " \n Swift Number:"
					+ (cs.getOverseasAccount()== null ? "" : cs.getOverseasAccount().getSwiftNumber()!=null?cs.getOverseasAccount().getSwiftNumber():"")
					+ " \n 开户行:"
					+(cs.getOverseasAccount() == null ? "":cs.getOverseasAccount().getAccountBank()!=null?cs.getOverseasAccount().getAccountBank():"")
					+ " \n Bank adress:"
					+ (cs.getOverseasAccount() == null ? "" : cs.getOverseasAccount().getAccountBankAddress()!=null?cs.getOverseasAccount().getAccountBankAddress():""));

			Cell cell241 = row24.createCell(3);
			cell241.setCellStyle(styleMap.get("bankInfoFont"));
			cell241.setCellValue("公司名称:"
					+ (cs.getCompanyName() == null ? "" : cs.getCompanyName())
					+ " \n "
					+ "开户行:"
					+ (cs.getAccount() == null ? "" : cs.getAccount().getAccountBank()!=null?cs.getAccount().getAccountBank():"")
					+ " \n "
					+ "账号:"
					+ (cs.getAccount()== null ? "" : cs.getAccount().getAccount()!=null?cs.getAccount().getAccount():""));

			Row row25 = sheet.createRow(24);
			Cell cell250 = row25.createCell(0);
			cell250.setCellValue("Routing");

			Row row31 = sheet.createRow(30);
			Cell cell310 = row31.createCell(0);
			cell310.setCellStyle(styleMap.get("titleStyle"));
			cell310.setCellValue("销售方签章");

			Cell cell311 = row31.createCell(3);
			cell311.setCellStyle(styleMap.get("titleStyle"));
			cell311.setCellValue("客户方签章");

			Row row32 = sheet.createRow(31);
			Cell cell320 = row32.createCell(0);
			cell320.setCellStyle(styleMap.get("titleStyle"));
			cell320.setCellValue("公司名称");
			Cell cell321 = row32.createCell(1);
			cell321.setCellStyle(styleMap.get("contentStyle"));
			cell321.setCellValue(cs.getCompanyName());
			Cell cell323 = row32.createCell(3);
			cell323.setCellStyle(styleMap.get("titleStyle"));
			cell323.setCellValue("公司名称");
			Cell cell324 = row32.createCell(4);
			cell324.setCellStyle(styleMap.get("contentStyle"));
			cell324.setCellValue(cs.getChannelName());

			Row row33 = sheet.createRow(32);
			Cell cell330 = row33.createCell(0);
			cell330.setCellStyle(styleMap.get("titleStyle"));
			cell330.setCellValue("签字");
			Cell cell331 = row33.createCell(1);
			cell331.setCellStyle(styleMap.get("contentStyle"));
			cell331.setCellValue("");
			Cell cell333 = row33.createCell(3);
			cell333.setCellStyle(styleMap.get("titleStyle"));
			cell333.setCellValue("签字");
			Cell cell334 = row33.createCell(4);
			cell334.setCellStyle(styleMap.get("contentStyle"));
			cell334.setCellValue("");

			Row row34 = sheet.createRow(33);
			Cell cell340 = row34.createCell(0);
			cell340.setCellStyle(styleMap.get("titleStyle"));
			cell340.setCellValue("盖章");
			Cell cell341 = row34.createCell(2);
			cell341.setCellStyle(styleMap.get("contentStyle"));
			cell341.setCellValue("");
			Cell cell343 = row34.createCell(3);
			cell343.setCellStyle(styleMap.get("titleStyle"));
			cell343.setCellValue("盖章");
			Cell cell344 = row34.createCell(4);
			cell344.setCellStyle(styleMap.get("contentStyle"));
			cell344.setCellValue("");

			ExcelUtils.SetMergedCellBorder(CellStyle.BORDER_NONE,1,CellStyle.BORDER_NONE,CellStyle.BORDER_NONE, c4, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c5, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c6, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c7, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c8, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c9, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c10, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c11, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c12, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c13, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c14, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c15, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c17, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1,CellStyle.BORDER_NONE,1,1, c180, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(CellStyle.BORDER_NONE,1,1,1, c18, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1,CellStyle.BORDER_NONE,1,1, c190, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(CellStyle.BORDER_NONE,1,1,1, c19, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c20, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c21, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c23, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c24, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c25, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c26, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c27, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c28, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c29, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c30, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c31, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c32, sheet, workbook);
			ExcelUtils.SetMergedCellBorder(1, c33, sheet, workbook);
			ServletUtil.downLoadExcel(response, "确认单.xlsx", workbook);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Map<String, Object> getIncomeInfoByInput(String fundsType, String receiveUuid) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("receiveUuid", receiveUuid);//'收款Uuid'
		
		String payPriceSerialNum = "";
		
		//达帐状态
		Integer isAsAccount = null;
		
		//收款类型
		int fundsTypeNum = Integer.parseInt(fundsType);
		
		// 根据订单收款（orderpay表）获取收入单信息
		if (fundsTypeNum<5 && fundsTypeNum>0) {
			
			Orderpay orderpay = orderpayDao.getById(Long.parseLong(receiveUuid));
			isAsAccount = orderpay.getIsAsAccount();
			
			payPriceSerialNum = orderpay.getMoneySerialNum();

			if (Context.ORDER_TYPE_JP.intValue() == orderpay.getOrderType()) {
				
				Map<String, Object> airticketInfo = airticketOrderDao.findMeituIncomeInfoByOrderId(orderpay.getOrderId());
				data.put("groupNo", airticketInfo.get("airGroupCode"));// 团号
				
				String agentName = "";
				if((airticketInfo.get("agentinfoId") == null) || "-1".equals(airticketInfo.get("agentinfoId").toString())) {
					agentName = airticketInfo.get("nagentName").toString();
				} else {
					agentName = agentinfoDao.getAgentNameById(Long.parseLong(airticketInfo.get("agentinfoId").toString()));//兹由
				}
				data.put("fromInfo", agentName);//兹由:订单收款的收入单抓取该订单的渠道（渠道名称）
				if(UserUtils.isMtourUser()){
					// 0430 交来组成规则-> 收款类别 1全款，2尾款，3定金，4追散 （1-4是订单收款类别）5成本其他收入收款
					if(fundsTypeNum==1){
						data.put("toInfo", "定金");//交来信息
					}else if(fundsTypeNum ==2){
						data.put("toInfo", "尾款");//交来信息
					}else if(fundsTypeNum == 3){
						data.put("toInfo", "全款");//交来信息
					}else if(fundsTypeNum ==4){
						data.put("toInfo", "追散");//交来信息
					}else{
						data.put("toInfo", "");//交来信息
					}
				}else{
					//交来组成规则->出团日期、行程、人数构成，如：2015-11-25（can-lax-sey）19
					String dateStr = DateUtils.formatCustomDate((Date)airticketInfo.get("startingDate"), "yyyy-MM-dd");
					StringBuffer travelInfo = new StringBuffer();//交来信息
					travelInfo.append(dateStr);
					travelInfo.append("（");
					travelInfo.append(airticketInfo.get("journey"));
					travelInfo.append("）");
					travelInfo.append(airticketInfo.get("personNum"));
					data.put("toInfo", FreeMarkerUtil.StringFilter(travelInfo.toString()));//交来信息
				}
			}
			
			//制单人
			data.put("incomeAloner", orderpay.getCreateBy().getName());
			data.put("applicantDate", DateUtils.date2String(orderpay.getCreateDate(), DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY));//收款日期
			data.put("printNo", serialNumberService.getSerialNumber(SerialNumberService.TABLENAME_ORDERPAY, orderpay.getId().intValue()).getCode());//流水号

			// 根据其他收入收款（pay_group表）获取收入单信息
		} else if (fundsTypeNum == 5) {
			PayGroup payGroup = payGroupDao.getById(Integer.parseInt(receiveUuid));
			isAsAccount = payGroup.getIsAsAccount();
			
			payPriceSerialNum = payGroup.getPayPrice();
			
			if (Context.ORDER_TYPE_JP.intValue() == payGroup.getOrderType()) {
				Map<String, Object> airticketInfo = activityAirTicketService.getMeituIncomeInfoByAirTicketId(payGroup.getGroupId().longValue());
				
				data.put("groupNo", airticketInfo.get("airGroupCode"));// 团号
				CostRecord costRecord = costRecordDao.getById(payGroup.getCostRecordId().longValue());
				data.put("fromInfo", costRecord.getSupplyName());//兹由：其他收入收款的收入单抓该笔收款时选择的地接社/渠道商名称

				if(UserUtils.isMtourUser()){
					// 0430 交来组成规则-> 收款类别 1全款，2尾款，3定金，4追散 （1-4是订单收款类别）5成本其他收入收款
					data.put("toInfo", "其他收入");//交来信息
				}else{
					//交来组成规则->出团日期、行程、人数构成，如：2015-11-25（can-lax-sey）19
					String dateStr = DateUtils.formatCustomDate((Date)airticketInfo.get("startingDate"), "yyyy-MM-dd");
					StringBuffer travelInfo = new StringBuffer();//交来信息
					travelInfo.append(dateStr);
					travelInfo.append("（");
					travelInfo.append(airticketInfo.get("journey"));
					travelInfo.append("）");
					travelInfo.append(airticketInfo.get("personNum"));
					data.put("toInfo", travelInfo.toString());//交来信息
				}
				
			}
			
			//制单人
			data.put("incomeAloner", userDao.getById(payGroup.getCreateBy().longValue()).getName());
			//收款日期
			data.put("applicantDate", DateUtils.date2String(payGroup.getCreateDate(), DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY));
			//流水号
			data.put("printNo", serialNumberService.getSerialNumber(SerialNumberService.TABLENAME_PAY_GROUP, payGroup.getId()).getCode());
		}
		
		//计算金额信息
		/*totalRMB:'计人民币',（人民币币种符号+阿拉伯数字，如￥190.00）
        totalRMB_CN:'计人民币(大写)'（该笔收款金额转换为人民币的中文大写，如壹佰玖拾元整；）
        totalOther:'外币',（外币小写；如：$100）
        totalOther_CN:'外币(大写)'（外币大写；如：$100的大写为壹佰美元整）*/
		//moneyAmounts参数为： currencyId,currency_name,currency_mark,m.amount,currency_exchangerate,m.moneyType
		//当付款状态为默认或达帐时才会设置币种信息
		if(isAsAccount == null || isAsAccount == 99 || isAsAccount == 1) {
			List<Object[]> moneyAmounts = moneyAmountService.getMoneyAmonut(payPriceSerialNum);//获取该流水号币种信息集合
			if(CollectionUtils.isEmpty(moneyAmounts)) {
				
	    	}
	    	String currencyName = null;
	    	String currencyMark = null;
	    	BigDecimal totalMoney = new BigDecimal(0);
	    	for(Object[] obj : moneyAmounts) {
	    		if(StringUtils.isEmpty(currencyName)) {
	    			currencyName = obj[1].toString();
	    			currencyMark = obj[2].toString();
	    		}
				//102为驳回状态,当收款状态为驳回时则过滤该收款
				if((obj[5] != null) && (Integer.parseInt(obj[5].toString()) != 102)) {
					totalMoney = totalMoney.add((BigDecimal)obj[3]);
				}
	    	}

	    	DecimalFormat d = new DecimalFormat(",##0.00");// 人民币显示格式
			if("人民币".equals(currencyName)) {
				data.put("totalRMB", "¥"+d.format(totalMoney.doubleValue()));// '计人民币'
				String totalRMB_CN = "";
				if(UserUtils.isMtourUser()) {
					totalRMB_CN = MoneyAmountUtils.generUppercase(currencyName, totalMoney.doubleValue());
				} else {
					totalRMB_CN = MoneyAmountUtils.digitUppercase(totalMoney.doubleValue());
				}
				data.put("totalRMB_CN", totalRMB_CN);//'计人民币(大写)'
				data.put("totalOther", "");//'外币'
				data.put("totalOther_CN", "");//外币（大写）
			} else {
				data.put("totalRMB", "");// '计人民币'
				data.put("totalRMB_CN", "");//'计人民币(大写)'
				
				data.put("totalOther", currencyMark + d.format(totalMoney.doubleValue()));//'外币'
				String totalOther_CN = "";
				if(UserUtils.isMtourUser()) {
					totalOther_CN = MoneyAmountUtils.generUppercase(currencyName, totalMoney.doubleValue());
				} else {
					totalOther_CN = MoneyAmountUtils.digitUppercase(currencyName, totalMoney);
				}
				data.put("totalOther_CN", totalOther_CN);//外币（大写）
			}
		} else {
			data.put("totalRMB", "");// '计人民币'
			data.put("totalRMB_CN", "");//'计人民币(大写)'
			
			data.put("totalOther", "");//'外币'
			data.put("totalOther_CN", "");//外币（大写）
		}

		//审核人
		data.put("approver", UserUtils.getUser().getName());
		return data;
	}

	public File createIncomeSheetDownloadFile(Map<String, Object> data)
			throws Exception {
		/*
		 * 收入单下划线总长度 创建日期: 团号：31 PNR号：30 计人民币（大写）：20 （小写）：￥26 开具日期： 26
		 */
		Map<String, String> root = new HashMap<String, String>();
		Date applicantDate = DateUtils.string2Date(data.get("applicantDate").toString(), DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY);

		String applicantYearStr = DateUtils.formatCustomDate(applicantDate, "yyyy");
		String applicantMonthStr = DateUtils.formatCustomDate(applicantDate, "MM");
		String applicantDayStr = DateUtils.formatCustomDate(applicantDate, "dd");

		root.put("applicantYear", applicantYearStr);//年
		root.put("applicantMonth", applicantMonthStr);//月
		root.put("applicantDay", applicantDayStr);//日
		
		root.put("applicantDate", data.get("applicantDate").toString());//日期
		root.put("groupNo", data.get("groupNo").toString());// 团号
		root.put("fromInfo", data.get("fromInfo").toString());//'兹由'
		root.put("toInfo", data.get("toInfo").toString());//'交来'
		root.put("totalRMB", data.get("totalRMB").toString());// 人民币小写
		root.put("totalRMB_CN", data.get("totalRMB_CN").toString());// 人民币大写
		root.put("totalOther", data.get("totalOther").toString());//'外币'
		root.put("totalOther_CN", data.get("totalOther_CN").toString());//'外币(大写)'
		root.put("incomeAloner", data.get("incomeAloner").toString());//'制单人'
		root.put("approver", data.get("approver").toString());//'审核人'
		root.put("printNo", data.get("printNo").toString());//打印单编号
		
		if(UserUtils.isMtourUser()) {
			return FreeMarkerUtil.generateFile("incomeInvoice.ftl", "incomeInvoice.doc", root);
		} else {
			return FreeMarkerUtil.generateFile("incomeInvoiceOther.ftl", "incomeInvoice.doc", root);
		}
	}

	/**
	 * 获取带下划线的字符串
	 * <p>
	 * @Description TODO
	 * </p>
	 * 
	 * @Title: getWithSubString
	 * @return String
	 * @author majiancheng
	 * @date 2015-10-20 下午6:01:51
	 */
	private String getWithSubString(String str, int maxLength) {
		
		if (null==str) {
			str = "";
		}
		int strLength = str.length();
		if (strLength >= maxLength) {
			return str;
		} else {
			int startSubCount = 0;// 开始下划线总数
			int endSubCount = 0;// 结束下划线总数
			int count = maxLength - strLength;
			StringBuffer sb = new StringBuffer();
			if (count % 2 == 0) {
				startSubCount = count / 2;
				endSubCount = count / 2;
			} else {
				startSubCount = count / 2 + 1;
				endSubCount = count / 2 - 1;
			}

			for (int i = 0; i < startSubCount; i++) {
				sb.append("_");
			}

			sb.append(str);

			for (int i = 0; i < endSubCount; i++) {
				sb.append("_");
			}
			return sb.toString();
		}
	}

	@Override
	public List<RefundRecord> getRefundRecord(BaseInput4MT input) {
		List<RefundRecord> recordList = new ArrayList<RefundRecord>();
		String fundsType = input.getParamValue("fundsType");
		if(StringUtils.isNotBlank(fundsType)){
			if("3".equals(fundsType)){
				//美途fundsType =3 表示的是追加成本，reund表6表示追加成本，需转一下
				fundsType ="6";
			}else if("1".equals(fundsType)){
				//美途fundsType =3 表示的是借款，reund表4表示借款，需转一下
				fundsType="4";
			}else if("2".equals(fundsType)){
				
				fundsType="2";
			}else if("4".equals(fundsType)){
				//美途fundsType =4 表示的是成本付款，reund表1表示成本付款，需转一下
				fundsType="1";
			}
		}
		String paymentUuid = input.getParamValue("paymentUuid");
		if(StringUtils.isNotBlank(fundsType) && StringUtils.isNotBlank(paymentUuid)){
			List<Refund> list = refundDao.findByRecordIdWithOrder(
					Long.valueOf(paymentUuid), Integer.valueOf(fundsType));
			if (CollectionUtils.isNotEmpty(list)) {
				for (Refund r : list) {
					RefundRecord record = new RefundRecord();
					record.setPaymentUuid(r.getRecordId().toString());
					//修改付款状态显示字段edit by majiancheng(2015-11-6)
					//refund中0表示已撤销、null表示已付款
					
					//前端接口
					//{financePaymentRecordStatusCode: '0', financePaymentRecordStatusName: '已付款'},
                    //{financePaymentRecordStatusCode: '1', financePaymentRecordStatusName: '已撤销'}
					//已撤销
					if("0".equals(r.getStatus())) {
						record.setFinancePaymentRecordStatusCode("1");
					//已付款
					} else {
						record.setFinancePaymentRecordStatusCode("0");
					}
					
					record.setPaymentDetailUuid(r.getId());
					record.setPaymentMethod(r.getPayType().toString());
					List<AirticketOrderMoneyAmount> moneyList = airticketOrderMoneyAmountDao
							.getBySerialNum(r.getMoneySerialNum());
					record.setMemo(r.getRemarks());
					if (CollectionUtils.isNotEmpty(moneyList)) {
						// 付款时只能是单币种，所有list只有一条记录，直接get(0)
						record.setPaymentAmount(moneyList.get(0).getAmount());
						record.setCurrencyUuid(Integer.valueOf(moneyList.get(0).getCurrencyId().toString()));
						record.setPaymentDate(moneyList.get(0)
								.getCreateDateString());
						record.setExchangeRate(moneyList.get(0).getExchangerate());
					}
					String dict = r.getPayVoucher(); // 支付凭证Id
					List<Dict> dictList = new ArrayList<Dict>();
					// 拼装付款凭证
					if (StringUtils.isNotBlank(dict)) {
						List<DocInfo> docInfoList = docInfoService
								.getDocInfoBydocids(dict);
						if (CollectionUtils.isNotEmpty(docInfoList)) {
							
							for (DocInfo d : docInfoList) {
								Dict dt = new Dict();
								dt.setFileName(d.getDocName());
								dt.setAttachmentUrl(d.getDocPath());
								dt.setAttachmentUuid(d.getId().toString());
								if(d.getCreateBy() != null){
									dt.setUploadUserName(d.getCreateBy().getName());
								}
								dictList.add(dt);
							}
							record.setAttachments(dictList);
						}
					}else{
						record.setAttachments(dictList);
					}
					recordList.add(record);
				}
			}
		}
		return recordList;
	}

	private String assembleSql(String[] array) {
		StringBuffer s = new StringBuffer("(");
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				s.append("'" + array[i] + "',");
			}
		}
		String sql = s.toString();
		sql = sql.substring(0, sql.lastIndexOf(","));
		sql = sql + ")";
		return sql;
	}

	private String packageSql(String[] departureDateArray) {
		String tSql = "(";
		for (int i = 0; i < departureDateArray.length; i++) {
			String[] datesArray = departureDateArray[i].split("~");
			String s = "(";
			if (datesArray[0] != null) {
				s = "aa.groupOpenDate >'" + datesArray[0] + "'";
			}
			if (datesArray[1] != null) {
				s += " AND aa.groupOpenDate <'" + datesArray[0] + "'";
			}
			s += ")";
			tSql += s + "   OR  ";
		}
		return tSql += " FALSE )";
	}

	@Override
	public List<Map<String, Object>> getReceiptRecordList(BaseInput4MT input) {
		StringBuffer listSql = new StringBuffer();
		listSql.append(" SELECT o.id AS receiveUuid,o.orderId AS orderUuid,o.createDate AS receiveDate,o.accountDate AS arrivalBankDate,o.orderNum AS orderNum,");
		listSql.append("  o.remarks AS fundsName,aa.activity_airticket_name AS productName,aa.group_code AS groupNo,aa.groupOpenDate AS departureDate,o.payPriceType AS receiveType,");
		listSql.append(" aa.proCompany AS tourOperatorId,'' AS tourOperatorName,o.payerName AS paymentCompany,o.createBy AS receiverId,su.name AS receiver,");
		listSql.append(" ao.total_money AS totalMoney,ao.accounted_money AS accountedMoney,ao.payed_money AS payedMoney,o.isAsAccount AS accountType,");
		listSql.append(" CASE o.isAsAccount WHEN 1 THEN o.moneySerialNum ELSE '' END AS moneyAccountde");
		StringBuffer sql = new StringBuffer();
		sql.append(" FROM   orderpay o LEFT JOIN airticket_order ao ON o.orderId=ao.id LEFT JOIN activity_airticket aa ON ao.airticket_id=aa.id LEFT JOIN sys_user su ON o.createBy = su.id");
		sql.append(" LEFT JOIN airticket_order_pnr_airline aopa ON ao.id=aopa.airticket_order_id LEFT JOIN sys_user su1 ON ao.create_by=su1.id");
		sql.append(" LEFT JOIN airticket_order_pnr aop ON ao.id=aop.airticket_order_id LEFT JOIN sys_airline_info sai ON aop.airline=sai.airline_code");
		sql.append(" LEFT JOIN agentinfo a ON  a.id=ao.agentinfo_id");
		sql.append(" WHERE o.delFlag = 0 AND o.orderType =7 AND aa.proCompany ="
				+ UserUtils.getUser().getCompany().getId());
		// 处理查询条件
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		JSONObject searchParam = jsonObj.getJSONObject("searchParam");
		if (StringUtils.isNotBlank(searchParam.getString("searchKey"))) {
			String searchKey = searchParam.getString("searchKey");
			switch (searchParam.getInteger("searchType")) {
			case 1:// 团号
				sql.append(" AND aa.group_code like '%" + searchKey + "%'");
				break;
			case 2:// 订单号
				sql.append(" AND o.orderId =" + searchKey);
				break;
			case 3:// 产品名称
				sql.append(" AND aa.activity_airticket_name like '%"
						+ searchKey + "%'");
				break;
			case 4:// 渠道名称
				sql.append(" AND aa.activity_airticket_name like '%"
						+ searchKey + "%'");
				break;
			case 5:// 下单人
				sql.append(" AND su1.name like '%" + searchKey + "%'");
				break;
			case 6:// PNR
				sql.append(" AND aop.flight_pnr like '%" + searchKey + "%'");
				break;
			case 7:// 航空公司
				sql.append(" AND sai.airline_name like '%" + searchKey + "%'");
				break;
			case 8:// 航段名称
				sql.append(" AND aopa.airline_name like '%" + searchKey + "%'");
				break;
			}
		}
		JSONObject filterParam = jsonObj.getJSONObject("filterParam");
		String receiveTypeCode = filterParam.getString("receiveTypeCode");
		if (StringUtils.isNotBlank(receiveTypeCode)) {
			String[] receiveTypeCodeArray = receiveTypeCode.split(",");
			String receiveTypeCodeSql = this.assembleSql(receiveTypeCodeArray);
			sql.append(" AND o.payPriceType in " + receiveTypeCodeSql);
		}
		JSONObject tourOperatorOrChannel = filterParam
				.getJSONObject("tourOperatorOrChannel");// 集合 TODO

		String receiveStatusCode = filterParam.getString("receiveStatusCode");
		if (StringUtils.isNotBlank(receiveStatusCode)) {
			sql.append(" AND o.printFlag in (" + receiveStatusCode + ")");
		}
		String departureDate = filterParam.getString("departureDate");
		if (StringUtils.isNotBlank(departureDate)) {
			String[] departureDateArray = departureDate.split(",");
			String tSql = this.packageSql(departureDateArray);
			sql.append(" AND " + tSql);
		}
		String receiveDate = filterParam.getString("receiveDate");
		if (StringUtils.isNotBlank(receiveDate)) {
			String[] receiveDateArray = receiveDate.split(",");
			String tSql = this.packageSql(receiveDateArray);
			sql.append(" AND " + tSql);
		}
		String arrivalBankDate = filterParam.getString("arrivalBankDate");
		if (StringUtils.isNotBlank(arrivalBankDate)) {
			String[] arrivalBankDateArray = arrivalBankDate.split(",");
			String tSql = this.packageSql(arrivalBankDateArray);
			sql.append(" AND " + tSql);
		}
		// receivedAmount 已收金额
		String receivedAmount = filterParam.getString("receivedAmount");
		if (StringUtils.isNotBlank(receivedAmount)) {
			sql.append("  AND  EXISTS (SELECT 1 FROM money_amount ma WHERE ma.serialNum=ao.payed_money  GROUP BY serialNum HAVING (SUM(amount*exchangerate)IN ("
					+ receivedAmount + ")))");
		}
		String printStatusCode = filterParam.getString("printStatusCode");
		if (StringUtils.isNotBlank(printStatusCode)) {
			sql.append(" AND o.printFlag in (" + printStatusCode + ")");
		}
		String payer = filterParam.getString("payer");
		if (StringUtils.isNotBlank(payer)) {
			String[] payerArray = payer.split(",");
			String payerArraySql = this.assembleSql(payerArray);
			sql.append(" AND o.printFlag in " + payerArraySql);
		}
		String receiver = filterParam.getString("receiver");
		if (StringUtils.isNotBlank(receiver)) {
			sql.append(" AND o.createBy in (" + receiver + ")");
		}
		long totalRowCount = currencyDao.getCountBySQL(" SELECT COUNT(*) "
				+ sql.toString());// 总记录数
		JSONObject pageParam = jsonObj.getJSONObject("pageParam");
		int currentIndex = pageParam.getIntValue("currentIndex");// 当前页码
		int rowCount = pageParam.getIntValue("rowCount") == 0 ? 10 : pageParam
				.getIntValue("rowCount");// 每页总行数
		sql.append(" LIMIT " + currentIndex * rowCount + "," + rowCount);
		List<Map<String, Object>> resultList = currencyDao.findBySql(
				listSql.toString() + sql.toString(), Map.class);
		if (CollectionUtils.isNotEmpty(resultList)) {
			resultList.get(0).put("totalRowCount", totalRowCount);
		}
		return resultList;
	}

	@Override
	public List<CostRecordJsonBean> getCostRecordList(Integer orderId) {
		return financeDao.getCostRecordList(orderId);
	}

	@Override
	public List<CostRecordJsonBean> getOtherCostRecordList(Integer orderId) {
		return financeDao.getOtherCostRecordList(orderId);
	}

	@Override
	public List<Map<String, Object>> getReceiptRecordDetail(String orderPayId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT o.orderId AS orderUuid,o.id AS receiveUuid,o.payPriceType AS receiveType,o.payTypeName AS receiveMethod,o.payVoucher AS docId,");
		sql.append(" (SELECT SUM(ma.amount*ma.exchangerate)  FROM  money_amount ma WHERE ma.serialNum=o.moneySerialNum) AS receiveAmount,");
		sql.append(" '' AS currencyUuid,'人民币' AS currencyName,o.payerName AS payer,o.checkNumber AS checkNo,o.invoiceDate AS checkIssueDate,");
		sql.append(" o.from_bank_name AS paymentBank,o.from_account AS paymentAccount,o.receive_bank_name AS receiveBank,o.receive_account AS receiveAccount,o.remarks AS memo");
		sql.append(" FROM  orderpay o WHERE o.id= ? ");
		return currencyDao.findBySql(sql.toString(), Map.class, orderPayId);
	}

	@Override
	public List<Map<String, String>> getReceiptDocDetail(String docId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT d.id AS attachmentUuid,d.docName AS fileName,d.docPath AS attachmentUrl");
		sql.append(" FROM  docinfo d WHERE d.id IN (" + docId + ")");
		return currencyDao.findBySql(sql.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getMoneyInfo(String serialNum) {
		String sql = "SELECT ma.serialNum,ma.currencyId,ma.amount,ma.exchangerate,c.currency_name AS currencyName  FROM money_amount ma LEFT JOIN currency c ON ma.currencyId=c.currency_id WHERE ma.serialNum ='"
				+ serialNum + "'";
		return currencyDao.findBySql(sql.toString(), Map.class);
	}

	@Override
	public Workbook createSettlement(Long orderId) {
		SettlementJsonBean settlement = financeDao.getSettlementInfo(orderId);
		// 计算收入项，成本项总计以及折合人民币总计，毛利，以及毛利率(注意以下三行调用顺序不能变)
		calculateSettlementInCome(settlement);
		calculateSettlementCost(settlement);
		calculateSettlementRefundAndProfit(settlement);
		settlement.setLister(UserUtils.getUser().getName());
		settlement.setListerDate(DateUtils.formatCustomDate(new Date(), DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY));
		Workbook workbook = ExportExcel.createSettlementExcel(settlement);
		return workbook;
	}

	@Override
	public File batchZipSettlement(String orderIds) {
		File zipFile = null;
		if(StringUtils.isNotBlank(orderIds)){
			String[] orderArray = orderIds.split(",");
			final List<File> list = new ArrayList<File>();
			String prefix = FileUtils.getTempDir();
			for (String order : orderArray){
				SettlementJsonBean settlement = financeDao.getSettlementInfo(Long.valueOf(order));
				// 计算收入项，成本项总计以及折合人民币总计，毛利，以及毛利率(注意以下三行调用顺序不能变)
				calculateSettlementInCome(settlement);
				calculateSettlementCost(settlement);
				calculateSettlementRefundAndProfit(settlement);
				settlement.setLister(UserUtils.getUser().getName());
				settlement.setListerDate(DateUtils.formatCustomDate(new Date(), DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY));
				Workbook workbook = ExportExcel.createSettlementExcel(settlement);
				// bug 14773
				String excelName = settlement.getGroupNo() + "结算单.xls";
				File excelFile = new File(prefix, excelName);
				OutputStream out = null;
				try {
					out = new BufferedOutputStream(new FileOutputStream(excelFile));
					workbook.write(out);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					FileUtils.closeStream(null, out);
				}
				list.add(excelFile);
			}
			String nowDate = DateUtils.formatCustomDate(new Date(), "yyyyMMddHHmmss");
			String zipName = "结算单"+nowDate+".zip";
			zipFile = new File(prefix, zipName);
			File deleteZipFile = zipFile;
			ZipUtils.zipFileList(list, zipFile);
			FileUtils.timingDeleteFile(60 * 10, deleteZipFile);
			FileUtils.timingDeleteFile(60 * 10, list);
		}
		return zipFile;
	}

	/**
	 * 结算单收入项数据计算，主要是计算折合人民币总计.
	 * 
	 * @param settlement
	 * @author shijun.liu
	 */
	private void calculateSettlementInCome(SettlementJsonBean settlement) {
		List<InComeJsonBean> incomes = settlement.getIncomes();
		if (null != incomes) {
			BigDecimal cnySum = new BigDecimal("0");// 折合人民币总计
			for (InComeJsonBean income : incomes) {
				String currencyMark = income.getCurrencyMark();
				String totalAmount = income.getTotalAmount();
				String rmb = income.getRmb();
				if (StringUtils.isBlank(rmb)) {
					rmb = "0";
				}
				if (StringUtils.isBlank(totalAmount)) {
					totalAmount = "0";
				}
				cnySum = cnySum.add(new BigDecimal(rmb));	
				totalAmount = MoneyNumberFormat.getThousandsByRegex(totalAmount, 2);
				income.setTotalAmount(currencyMark + totalAmount);
				rmb = MoneyNumberFormat.getThousandsByRegex(rmb, 2);
				income.setRmb("¥" + rmb);
			}
			String incomeSumRMB = MoneyNumberFormat.getThousandsByRegex(cnySum.toString(), 2);
			settlement.setInComeSumRMB("¥" + incomeSumRMB);
		}
	}

	/**
	 * 结算单成本项数据计算，成本合计包括，追加成本+成本，
	 * 
	 * @param settlement
	 * @author shijun.liu
	 */
	private void calculateSettlementCost(SettlementJsonBean settlement) {
		List<CostJsonBean> costs = settlement.getCosts();// 成本项数据
		List<AdditionCostRefundJsonBean> additionalCosts = settlement.getAdditionalCosts();// 追加成本项数据
		BigDecimal additionalSumRMB = new BigDecimal("0");// 追加成本折合人民币合计
		BigDecimal costSumRMB = new BigDecimal("0");// 成本折合人民币合计
		// 追加成本求和
		if (null != additionalCosts) {
			for (AdditionCostRefundJsonBean additionalCost : additionalCosts) {
				String amount = additionalCost.getAmount();
				String rmb = additionalCost.getRmb();
				if (StringUtils.isBlank(rmb)) {
					rmb = "0";
				}
				if (StringUtils.isBlank(amount)) {
					amount = "0";
				}
				additionalSumRMB = additionalSumRMB.add(new BigDecimal(rmb));// 追加成本折合人民币求和
			}
			String additionalCostSumRMB = MoneyNumberFormat.getThousandsByRegex(additionalSumRMB.toString(), 2);// 追加成本折合人民币转换成千分位
			settlement.setAdditionalCostSumRMB("¥" + additionalCostSumRMB);
		}
		// 成本求和,每一项还要添加追加成本的数据
		if (null != costs) {
			for (CostJsonBean cost : costs) {
				String currencyMark = cost.getCurrencyMark();
				String totalAmount = cost.getTotalAmount();
				String rmb = cost.getRmb();
				if (StringUtils.isBlank(rmb)) {
					rmb = "0";
				}
				if (StringUtils.isBlank(totalAmount)) {
					totalAmount = "0";
				}
				costSumRMB = costSumRMB.add(new BigDecimal(rmb));// 成本折合人民币求和
				totalAmount = MoneyNumberFormat.getThousandsByRegex(totalAmount, 2);
				cost.setTotalAmount(currencyMark + totalAmount);
				rmb = MoneyNumberFormat.getThousandsByRegex(rmb, 2);
				cost.setRmb("¥" + rmb);
			}
		}
		costSumRMB = costSumRMB.add(additionalSumRMB);// 成本 + 追加成本折合人民币
		String costSumRMBStr = MoneyNumberFormat.getThousandsByRegex(costSumRMB.toString(), 2);// 折合人民币转换成千分位
		settlement.setCostSumRMB("¥" + costSumRMBStr);
	}

	/**
	 * 结算单退款数据计算，主要是计算总计，折合人民币总计，毛利，毛利率
	 * 
	 * @param settlement
	 * @author shijun.liu
	 */
	private void calculateSettlementRefundAndProfit(
			SettlementJsonBean settlement) {
		List<AdditionCostRefundJsonBean> refunds = settlement.getRefunds();
		if (null != refunds) {
			BigDecimal cnySumRMB = new BigDecimal("0");// 退款折合人民币总计
			for (AdditionCostRefundJsonBean refund : refunds) {
				String amount = refund.getAmount();
				String rmb = refund.getRmb();
				if (StringUtils.isBlank(rmb)) {
					rmb = "0";
				}
				if (StringUtils.isBlank(amount)) {
					amount = "0";
				}
				cnySumRMB = cnySumRMB.add(new BigDecimal(rmb));// 退款数据求和
			}
			String refundSumRMB = MoneyNumberFormat.getThousandsByRegex(cnySumRMB.toString(), 2);// 退款数据折合人民币转换成千分位
			settlement.setRefundSumRMB("¥" + refundSumRMB);
		}
		// 计算毛利，毛利率。 毛利 = 收入合计-成本合计-退款; 毛利率 = 毛利/收入合计
		String incomeSumRMB = "0";
		String costSumRMB = "0";
		String refundSumRMB = "0";
		if (null != settlement.getInComeSumRMB()) {
			incomeSumRMB = settlement.getInComeSumRMB().replaceAll("¥", "")
					.replaceAll(",", "");
		}
		if (null != settlement.getCostSumRMB()) {
			costSumRMB = settlement.getCostSumRMB().replaceAll("¥", "")
					.replaceAll(",", "");
		}
		if (null != settlement.getRefundSumRMB()) {
			refundSumRMB = settlement.getRefundSumRMB().replaceAll("¥", "")
					.replaceAll(",", "");
		}
		BigDecimal profit = new BigDecimal(incomeSumRMB).subtract(
				new BigDecimal(costSumRMB)).subtract(
				new BigDecimal(refundSumRMB));
		String profitStr = MoneyNumberFormat.getThousandsMoney(
				profit.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO);
		settlement.setGrossProfit("¥" + profitStr);
		String grossProfitRate = "0.000%";
		if (new BigDecimal(incomeSumRMB).compareTo(new BigDecimal("0")) != 0) {
			BigDecimal temp = profit.divide(new BigDecimal(incomeSumRMB),5,BigDecimal.ROUND_HALF_UP);
			grossProfitRate = MoneyNumberFormat.getFormatRate(
					temp.multiply(new BigDecimal(100)).doubleValue(), MoneyNumberFormat.POINT_THREE);
			grossProfitRate = grossProfitRate + "%";
		}
		settlement.setGrossProfitRate(grossProfitRate);
	}

	@Override
	public SettlementJsonBean getSettlementJson(Long orderId) {
		SettlementJsonBean settlement = financeDao.getSettlementInfo(orderId);
		// 计算收入项，成本项总计以及折合人民币总计，毛利，以及毛利率(注意以下三行调用顺序不能变)
		calculateSettlementInCome(settlement);
		calculateSettlementCost(settlement);
		calculateSettlementRefundAndProfit(settlement);
		settlement.setLister(UserUtils.getUser().getName());
		settlement.setListerDate(DateUtils.formatCustomDate(new Date(), DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY));
		return settlement;
	}

	@Override
	public void submitCostRecord(Integer id) {
		financeDao.submitCostRecord(id);

		//----------更新订单付款状态 add by majiancheng
		CostRecord costRecord = costRecordDao.getById(id.longValue());
		Integer budgetType = costRecord.getBudgetType();
		//其他收入收款不更新订单的付款状态。update by shijun.liu 2016.03.14
		if(2 != budgetType.intValue()){
			updateOrderRefundFlag(costRecord.getOrderId());
		}
		//----------更新订单付款状态
	}

	@Override
	public void cancelCostRecord(Integer id) {
		financeDao.cancelCostRecord(id);
		
		//----------更新订单付款状态 add by majiancheng
		CostRecord costRecord = costRecordDao.getById(id.longValue());
		Integer budgetType = costRecord.getBudgetType();
		//其他收入收款不更新订单的付款状态。update by shijun.liu 2016.03.14
		if(2 != budgetType.intValue()){
			updateOrderRefundFlag(costRecord.getOrderId());
		}
		//----------更新订单付款状态
	}

	@Override
	public void saveOrUpdateCostRecord(CostRecord costRecord) {
		if (costRecord.getId() == null) {
			CostRecord cr = costRecordDao.save(costRecord);
			serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, cr.getId().intValue());
		} else {
			costRecordDao.updateObj(costRecord);
		}
	}

	@Override
	public void saveOrUpdateOtherCostRecord(CostRecord costRecord,
			PayGroup payGroup) {
		if (costRecord.getId() == null) {
			CostRecord cr = costRecordDao.save(costRecord);
			Long costId = cr.getId();
			payGroup.setCostRecordId(Integer.parseInt(costId.toString()));
			payGroupDao.save(payGroup);
			serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_PAY_GROUP, payGroup.getId().intValue());
			// for(DocInfo doc : docList) {
			// docInfoDao.save(doc);
			// }
		} else {
			costRecordDao.updateObj(costRecord);
			if (payGroup.getId() != null) {
				payGroupDao.updateObj(payGroup);
			} else {
				payGroupDao.save(payGroup);
			}
		}
	}

	private String getPayListWhereSql(BaseInput4MT input){
		StringBuffer sb = new StringBuffer();
		JSONObject object  =JSON.parseObject(input.getParam());
		Map<String,String> map = (Map)object.get("searchParam");
		String searchType = map.get("searchType");
		String searchKey = map.get("searchKey");
		if(StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchKey)){
			if("1".equals(searchType)){//团号
				sb.append(" AND paylist.groupNo  like '%").append(searchKey).append("%'");
			}else if("2".equals(searchType)){//订单编号
				sb.append(" AND paylist.orderNum like '%").append(searchKey).append("%'");
			}else if("3".equals(searchType)){//产品名称
				sb.append(" AND paylist.productName like '%").append(searchKey).append("%'");
			}else if("4".equals(searchType)){//渠道 2表示渠道，1表示地接社
				sb.append(" AND paylist.supplyOrAgentType = 2 AND paylist.tourOperatorOrChannelName like '%").append(searchKey).append("%'");
			}
		}
		
		Map<String,Object> filterParam = (Map<String, Object>) object.get("filterParam");
		Object paymentStatusCode = filterParam.get("paymentStatusCode");
		if(paymentStatusCode != null && StringUtils.isNotBlank(paymentStatusCode.toString())){
			if("0".equals(paymentStatusCode.toString())){
				sb.append(" AND paylist.paymentStatus in (").append(paymentStatusCode).append(",3)");
			}else{
			sb.append(" AND paylist.paymentStatus = ").append(paymentStatusCode);
			}
		}
		//出团日期  
		if(filterParam.get("departureDate")!= null && StringUtils.isNotBlank(filterParam.get("departureDate").toString())){
			String departureDate = filterParam.get("departureDate").toString();
			int index = departureDate.indexOf("~");
			String[] str = departureDate.split("~");
			if(index<0){
				
			}else if(index==0){ //截止日期  
				if(StringUtils.isNotBlank(str[1])){
					sb.append(" AND paylist.departureDate <='").append(str[1]).append(" 23:59:59'");
				}
			}else if(index>0){
				if(str.length>1){
                    if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND paylist.departureDate >='").append(str[0]).append(" 00:00:00'");
					}
					if(StringUtils.isNotBlank(str[1])){
						sb.append(" AND paylist.departureDate <='").append(str[1]).append(" 23:59:59'");
					}
				}else{
					if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND paylist.departureDate >='").append(str[0]).append(" 00:00:00'");
					}
				}
			}			
		}
		//应付金额
		if(filterParam.get("payableAmount")!= null && StringUtils.isNotBlank(filterParam.get("payableAmount").toString())){
			String departureDate = filterParam.get("payableAmount").toString();
			int index = departureDate.indexOf("~");
			String[] str = departureDate.split("~");
			if(index<0){
				
			}else if(index==0){ //截止日期  
				if(StringUtils.isNotBlank(str[1])){
					sb.append(" AND paylist.amount <=").append(str[1]);
				}
			}else if(index>0){
				if(str.length>1){
                    if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND paylist.amount >=").append(str[0]);
					}
					if(StringUtils.isNotBlank(str[1])){
						sb.append(" AND paylist.amount <=").append(str[1]);
					}
				}else{
					if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND paylist.amount >=").append(str[0]);
					}
				}
			}			
		}
		//批报日期
		if(filterParam.get("approvalDate")!= null && StringUtils.isNotBlank(filterParam.get("approvalDate").toString())){
			String approvalDate = filterParam.get("approvalDate").toString();
			int index = approvalDate.indexOf("~");
			String[] str = approvalDate.split("~");
			if(index<0){
				
			}else if(index==0){ //截止日期  
				if(StringUtils.isNotBlank(str[1])){
					sb.append(" AND paylist.approvalDate <='").append(str[1]).append(" 23:59:59'");
				}
			}else if(index>0){
				if(str.length>1){
                    if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND paylist.approvalDate >='").append(str[0]).append(" 00:00:00'");
					}
					if(StringUtils.isNotBlank(str[1])){
						sb.append(" AND paylist.approvalDate <='").append(str[1]).append(" 23:59:59'");
					}
				}else{
					if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND paylist.approvalDate >='").append(str[0]).append(" 00:00:00'");
					}
				}
			}			
		}
		//申请人applicantId
		if(filterParam.get("applicantId") != null && StringUtils.isNotBlank(filterParam.get("applicantId").toString()))
		{
			sb.append(" AND paylist.applicant in (").append(filterParam.get("applicantId")).append(")");
		}
		
		//渠道或地接社
		JSONArray array = (JSONArray) filterParam.get("tourOperatorOrChannel");
	    if(array != null && array.size()>0){
	    	StringBuffer id = new StringBuffer();
	    	String type = "";
	    	int num = array.size();
	    	for(int i =0;i<num;i++){
	    		JSONObject o =  array.getJSONObject(i);
	    		type=(String) o.get("tourOperatorChannelCategoryCode");
	    		if(i==num-1){
	    		  id.append(o.get("tourOperatorOrChannelUuid"));
	    		}else{
	    		  id.append(o.get("tourOperatorOrChannelUuid")).append(",");
	    		}
	    		
	    	}
	    	if(StringUtils.isNotBlank(type) && StringUtils.isNotBlank(id.toString())){
	    		if("2".equals(type)){
	    			sb.append(" AND paylist.supplyOrAgentType=").append(type).append(" AND paylist.tourOperatorOrChannelId in (").append(id).append(") ");
	    		}else{
	    			sb.append(" AND paylist.supplyOrAgentType=").append(type).append(" AND paylist.tourOperatorOrChannelId in (").append(id).append(") ");
	    		}
	    	}
	    }
		
		//款项类型
		if(filterParam.get("fundsType") != null && StringUtils.isNotBlank(filterParam.get("fundsType").toString()))
		{
			sb.append(" AND paylist.fundsType in(").append(filterParam.get("fundsType")).append(") ");
		}

		Object pnr = filterParam.get("pnrValue");
		if(null != pnr && StringUtils.isNotEmpty(pnr.toString())){
			String[] pnrArray = pnr.toString().split(",");
			sb.append(" AND paylist.invoiceOriginalTypeCode = 0 AND (");
			for (String item : pnrArray){
				sb.append(" paylist.PNR LIKE '%").append(item).append("%' OR");
			}
			sb.delete(sb.length()-2, sb.length()).append(") ");
		}
		//成本单价
		List<Map<String,Object>> costarray = (List<Map<String,Object>>)filterParam.get("payAmount");
		if(null != costarray && costarray.size()>0){
			StringBuffer sbf = new StringBuffer();
			sbf.append(" AND paylist.fundsType=4 AND ( ");
			for(Map<String, Object> cost : costarray){
				int index = -1;
				Object currencyId = cost.get("id");
				Object minAmount = cost.get("minAmount");
				Object maxAmount = cost.get("maxAmount");
				if(null != currencyId && StringUtils.isNotEmpty(currencyId.toString())){
					index++;
					sbf.append(" ( paylist.currencyUuid = ").append(Integer.parseInt(currencyId.toString()));
				}
				if(null != minAmount && StringUtils.isNotEmpty(minAmount.toString())){
					if(-1 == index){
						index++;
						sbf.append(" ( paylist.costPrice >= ").append(Long.valueOf(minAmount.toString()));
					}else {
						sbf.append(" AND paylist.costPrice >= ").append(Long.valueOf(minAmount.toString()));
					}
				}
				if(null != maxAmount && StringUtils.isNotEmpty(maxAmount.toString())){
					if(-1 == index){
						sbf.append(" ( paylist.costPrice <= ").append(Long.valueOf(maxAmount.toString()));
					}else{
						sbf.append(" AND paylist.costPrice <= ").append(Long.valueOf(maxAmount.toString()));
					}
				}
				sbf.append(") OR");
			}
			sbf.delete(sbf.length()-2, sbf.length()).append(" ) ");
			sb.append(sbf);
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page getPayList(BaseInput4MT input) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String orderby = genOrderBy(input);
		JSONObject jo = JSONObject.parseObject(input.getParam());
		String wheresql = getPayListWhereSql(input);
		Page page = new Page();
		try{
			StringBuffer str = new StringBuffer();
			str.append(" SELECT invoiceOriginalTypeCode, orderNum, paymentUuid, approvalDate, orderUuid, productNo,")
			   .append(" PNR, groupNo, productName, departureDate, fundsName, tourOperatorOrChannelId, tourOperatorOrChannelName,")
			   .append(" supplyOrAgentType, applicant, applicantName, paymentStatus, currencyUuid, amount,")
			   .append(" updateDate as modifiedDateTime, fundsType, create_by, operator, costPrice from ");//432需求增加成本单价查询costPrice
			StringBuffer str1 = new StringBuffer();
			str1.append(" (SELECT c.isJoin invoiceOriginalTypeCode, ao.order_no orderNum, ")
			    .append(" c.id paymentUuid, c.createDate approvalDate, pro.id productNo, ao.id orderUuid,")
			    .append(" c.bigCode PNR, pro.group_code groupNo, pro.activity_airticket_name productName, ")
			    .append(" pro.startingDate departureDate, c.NAME fundsName, c.supplyId tourOperatorOrChannelId, ")
			    .append(" c.supplyName tourOperatorOrChannelName, c.supplyType supplyOrAgentType, ")
			    .append(" c.createBy applicant, (select name from sys_user su where su.id=c.createBy) applicantName, ")
			    .append(" c.payStatus paymentStatus, c.currencyId currencyUuid, c.price * c.quantity amount, c.price AS costPrice,")//432需求增加成本单价查询
			    .append(" c.updateDate, 4 fundsType, ao.create_by, (SELECT su.NAME FROM sys_user su WHERE su.id = ao.create_by) ")
			    .append(" AS operator FROM cost_record c, airticket_order ao ,activity_airticket pro ")
				.append(" WHERE c.orderId = ao.id AND ao.airticket_id = pro.id AND pro.id = c.activityId AND ")
				//edit by majiancheng(2015-12-01)成本付款状态添加待付款条件
				.append(" c.delFlag = 0 AND c.budgetType = 1 AND c.payStatus in (3,1,0) AND ")
				.append(" pro.proCompany = ").append(companyId)
				.append(" UNION ")
				.append("SELECT null invoiceOriginalTypeCode, ao.order_no orderNum,")
				.append(" money.id paymentUuid, money.createDate approvalDate, ao.airticket_id productNo,")
				.append(" ao.id orderUuid, null PNR, p.group_code groupNo, p.activity_airticket_name productName,")
				.append(" p.startingDate departureDate, money.funds_name fundsName, ao.agentinfo_id tourOperatorOrChannelId,")
				.append(" (SELECT agent.agentName FROM agentinfo agent WHERE agent.id = ao.agentinfo_id) tourOperatorOrChannelName, ")
				.append(" 2 as supplyOrAgentType, money.createBy applicant,")
				.append(" (select name from sys_user su where su.id=money.createBy) applicantName, ")
				.append(" money.paystatus paymentStatus, money.currency_id currencyUuid, money.amount, '' AS costPrice, money.updateDate, ")
				.append(" money.moneyType fundsType, ao.create_by,(SELECT su. NAME FROM sys_user su WHERE su.id = ao.create_by ) ")
				.append(" AS operator FROM airticket_order_moneyAmount money, airticket_order ao, ")
				.append(" activity_airticket p WHERE money.airticket_order_id = ao.id ")
				.append(" AND ao.airticket_id = p.id AND money.airticket_order_id IS NOT NULL AND money.status<>0 ")
				.append(" AND p.proCompany = ").append(companyId);
			StringBuffer str2 = new StringBuffer(" ) paylist where 1=?");
			Query query = airticketOrderMoneyAmountDao.createSqlQuery(str.append(str1).append(str2).append(wheresql)
																	 .append(" order by ").append(orderby).toString(), "1")
																	 .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			StringBuffer count = new StringBuffer(" select count(1) from ");	
			Map pagemap = (Map) jo.get("pageParam");
			Object p = pagemap.get("currentIndex");//页码
			Object pagesize = pagemap.get("rowCount");//每页条数
			if(p != null && pagesize !=null){
				Integer ps = Integer.valueOf(pagesize.toString()); //每页条数
				Integer num = Integer.valueOf(p.toString()); //页码
				BigDecimal bd = new BigDecimal(ps).multiply(new BigDecimal(num).subtract(new BigDecimal(1)));//每页起始条数
				// 设置每页显示多少个，设置多大结果。
				query.setMaxResults(ps);
				// 设置起点
				query.setFirstResult(Integer.valueOf(bd.toString()));
			}else{
				// 设置每页显示多少个，设置多大结果。
				query.setMaxResults(10);
				// 设置起点
				query.setFirstResult(0);
			}
			List<Map<String, Object>> list = query.list();
			// 查询总条数
			Long totalNum = airticketOrderMoneyAmountDao.getCountBySQL(count
					.append(str1).append(" ) paylist where 1=1 ").append(wheresql).toString());
			page.setResults(copyParam2Paylist(list));
			PageBean pageBean = new PageBean();
			pageBean.setTotalRowCount(totalNum.toString());
			pageBean.setCurrentIndex(p.toString());
			pageBean.setRowCount(pagesize.toString());
			page.setPage(pageBean);
		}catch(Exception e){
			e.printStackTrace();
		}
		return page;
	}
    /**
     * 付款列表、收款列表排序
     * @author zhaohaiming
     * */
	private String genOrderBy(BaseInput4MT input){
		String orderby = "modifiedDateTime desc";
		JSONObject jo = JSONObject.parseObject(input.getParam());
		Map<String,Object> map = (Map)jo.get("sortInfo");
		Object sortKey = map.get("sortKey");
		boolean desc = (boolean) map.get("dec");
		if(desc){
			if(sortKey != null && StringUtils.isNotBlank(sortKey.toString())){
				orderby = sortKey+" desc";
			}
		}else{
			if(sortKey != null && StringUtils.isNotBlank(sortKey.toString())){
				orderby = sortKey+" asc";
			}
		}
		return orderby;
	}
	/**
	 * 组织付款列表参数
	 * @author zhaohaiming
	 * */
	private List<PayList> copyParam2Paylist(List list){
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		List<PayList> payList = new ArrayList<PayList>();
		if(CollectionUtils.isNotEmpty(list)){
			int size = list.size();
			for(int n=0;n<size;n++){
				 Map<String,Object> map = (Map<String, Object>) list.get(n);
				 PayList pl = new PayList();
				 pl.setOrderUuid(map.get("orderUuid")!=null?map.get("orderUuid").toString():""); //订单uuid
				 pl.setPaymentUuid(map.get("paymentUuid")!=null?map.get("paymentUuid").toString():""); //付款uuid
				 if(map.get("approvalDate") != null){
					 Date date = (Date) map.get("approvalDate");
					 String d = DateUtils.formatCustomDate(date, "yyyy-MM-dd HH:mm:ss");
					 pl.setApprovalDate(d); //批报日期
				 }
				 pl.setProductNo(map.get("productNo")!=null?map.get("productNo").toString():""); //产品号
				 pl.setInvoiceOriginalTypeCode("");//大编号
				 pl.setPNR(map.get("PNR")!=null?map.get("PNR").toString():"");//PNR
				 pl.setGroupNo(map.get("groupNo")!=null?map.get("groupNo").toString():"");//团号
				 pl.setProductName(map.get("productName")!=null?map.get("productName").toString():"");//产品名称
				 pl.setDepartureDate(map.get("departureDate")!=null?map.get("departureDate").toString():"");//出团日期
				 pl.setFundsName(map.get("fundsName")!=null?map.get("fundsName").toString():"");//款项名称
				 pl.setFundsType(map.get("fundsType")!=null?map.get("fundsType").toString():"");//款项类型
				 pl.setTourOperatorOrChannelName(map.get("tourOperatorOrChannelName")!=null?map.get("tourOperatorOrChannelName").toString():"");
				 pl.setApplicant(map.get("applicantName")!=null?map.get("applicantName").toString():"");//申请人姓名
				 Object obj = map.get("paymentStatus");
				 if(obj != null){
					 String status = obj.toString();
					 if("3".equals(status)){
						 pl.setPaymentStatus("0");
					 }else{
						 pl.setPaymentStatus(obj.toString());
					 }
				 }else{
					 pl.setPaymentStatus(map.get("paymentStatus")!=null?map.get("paymentStatus").toString():"");
				 }
				 //渠道信息
				 ChannelType channelType = new ChannelType();
				 if(map.get("supplyOrAgentType")!=null && "2".equals(map.get("supplyOrAgentType").toString())){
					 //map.get("supplyOrAgentType") ==2 表示是渠道
					 if(map.get("tourOperatorOrChannelId") != null && "-1".equals(map.get("tourOperatorOrChannelId").toString())){
						 channelType.setChannelTypeCode("2");
						 channelType.setChannelTypeName("非签约渠道");
					 }else{
						 channelType.setChannelTypeCode("1");
						 channelType.setChannelTypeName("签约渠道");
					 }
				 }
				 pl.setChannelType(channelType);
				 pl.setModifiedDateTime(map.get("updateDate")!=null?map.get("updateDate").toString():"");
				 pl.setInvoiceOriginalTypeCode(map.get("invoiceOriginalTypeCode")!=null?map.get("invoiceOriginalTypeCode").toString():"");
				 pl.setOrderNum(map.get("orderNum")!=null?map.get("orderNum").toString():"");
				 //成本单价
				 List<MoneyAmountVO> payAmount=new ArrayList<MoneyAmountVO>();
				 MoneyAmountVO vb=new MoneyAmountVO();
				 if(map.get("currencyUuid")!=null){
						vb.setCurrencyUuid(Integer.valueOf(map.get("currencyUuid").toString()));
				}
				 if(map.get("costPrice")!=null && StringUtils.isNotEmpty(String.valueOf(map.get("costPrice")))){
					 vb.setAmount(Double.valueOf(map.get("costPrice").toString()));
				 }
				 payAmount.add(vb);
				 pl.setPayAmount(payAmount);
				 //应付金额
				 List<MoneyAmountVO> payableAmount = new ArrayList<MoneyAmountVO>();
				 MoneyAmountVO vo = new MoneyAmountVO();
				 if(map.get("currencyUuid")!=null){
					vo.setCurrencyUuid(Integer.valueOf(map.get("currencyUuid").toString()));
				 }
				 if(map.get("amount")!=null){
					 vo.setAmount(Double.valueOf(map.get("amount").toString()));
				 }
				 //已付金额
				 List<MoneyAmountVO> paidAmount = getRefundPaydMoney(pl.getPaymentUuid(),pl.getFundsType(),companyUuid);
				 pl.setPaidAmount(paidAmount);
				 payableAmount.add(vo);
				 pl.setPayableAmount(payableAmount);
				 payList.add(pl);
			 }
		 }
		return payList;
	}
	
	
	/**
	 * 获取付款已付金额
	 * @param recordid 成本id,退款，返佣id...
	 * @param moneyType 1借款，2退款，3追加陈本，4成本
	 * 
	 * 
	 * */
	public List<MoneyAmountVO> getRefundPaydMoney(String recordid, String moneyType,String companyUuid) {
		Integer refundMoneyType=1;
		if(StringUtils.isNotBlank(moneyType)){
			if("4".equals(moneyType)){
				refundMoneyType = 1;
			}else if("3".equals(moneyType)){
				refundMoneyType = 6;
			}else if("2".equals(moneyType)){
				refundMoneyType = 2;
			}else if("1".equals(moneyType)){
				refundMoneyType = 4;
			}
		}
		//获取付款记录
		List<Refund> refundList = refundDao.findRefund(Long.valueOf(recordid), refundMoneyType, companyUuid);
		List<MoneyAmountVO> returnList = new ArrayList<MoneyAmountVO>();														
		List<Map<String,Object>> paymoney = new ArrayList<Map<String,Object>>();
		StringBuffer serialNum = new StringBuffer();
		//将付款记录的serialNum拼装起来
		if (CollectionUtils.isNotEmpty(refundList)) {
			int num = refundList.size();
		    for (int i = 0; i < num; i++) {
				if(i==num-1){
					serialNum.append("'").append(refundList.get(i).getMoneySerialNum()).append("'");
				}else{
					serialNum.append("'").append(refundList.get(i).getMoneySerialNum()).append("',");
				}
			}
		}
		String serialNumStr = serialNum.toString();
		
		if (StringUtils.isNotBlank(serialNumStr)) {
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT c.currency_id id, c.currency_mark, sum(m.amount) amount, ")
			   .append(" ifnull(m.exchangerate,ifnull(c.convert_lowest,1)) as exchangeRage")
			   .append(" from airticket_order_moneyAmount m,currency c where m.currency_id=c.currency_id ")
			   .append(" and m.serialNum in (").append(serialNumStr).append(")")
			   .append(" GROUP BY c.currency_id ORDER BY c.currency_id ");
			paymoney = airticketOrderMoneyAmountDao.findBySql(sql.toString(), Map.class);
		}
		if(CollectionUtils.isNotEmpty(paymoney)){
			int num = paymoney.size();
			for(int i=0;i<num;i++){
				MoneyAmountVO vo = new MoneyAmountVO();
				BigDecimal amount = (BigDecimal)paymoney.get(i).get("amount");
				Object obj = paymoney.get(i).get("id");
				BigDecimal exchangeRage = (BigDecimal)paymoney.get(i).get("exchangeRage");
				if(amount != null){
				   vo.setAmount(amount.doubleValue());
				}
				if(obj != null){
					vo.setCurrencyUuid(Integer.valueOf(obj.toString()));
				}
				if(exchangeRage != null) {
					vo.setExchangeRate(exchangeRage.doubleValue());
				}
				returnList.add(vo);
			}
		}
		return returnList;
	}
	
	@Override
	public List<BigCode> getBigCodeList(Integer orderId) {
		return financeDao.getBigCodeList(orderId);
	}

	/**
	 * 成本记录详情
	 * 
	 * @author hhx
	 * @date 2015-10-21
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> getCostDetail(BaseInput4MT input) {
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		String sql = "SELECT "
				+ "	cr.orderId AS orderUuid, "
				+ "	cr.id AS costUuid, ";
		//if(UserUtils.isMtourUser()){
		//	sql += "	cr.supplyName AS fundsName, ";
		//}else{
			sql += "	cr.name AS fundsName, ";
	//	}
				//+ " case when cr.bigCode is null or cr.bigCode='' then 1 else 0 end AS invoiceOriginalTypeCode, "
			sql += " cr.isJoin as invoiceOriginalTypeCode,"
				+ "	cr.bigCode AS PNR,cr.supplyId AS tourOperatorUuid, "
				+ " cr.supplyName AS tourOperatorName,cr.quantity AS peopleCount, "
				+ "	cr.currencyId AS currencyUuid,cr.price AS price, "
				+ " cr.rate AS exchangeRate,cr.currencyAfter AS convertedCurrencyUuid, "
				+ "	cr.priceAfter AS convertedAmount," 
				+ "	concat(cr.supplyType,'') AS tourOperatorChannelCategoryCode," 
				+ " cr.supplyName AS tourOperatorOrChannelName,cr.supplyId AS tourOperatorOrChannelUuid, "
				+ "	cr.bankName AS paymentBank,cr.bankAccount AS paymentAccount,"
				+ " concat(cr.supplierType,'') as tourOperatorOrChannelTypeCode, "
				+ " cr.cost_total_deposit as costTotalDeposit, "
				+ " pnrAirline.airline_name as airlineName, "
				+ " cr.comment as memo,cr.pnr_uuid AS invoiceOriginalUuid FROM cost_record cr "
				+ " left join airticket_order_pnr_airline pnrAirline on cr.airline_uuid = pnrAirline.uuid"
				+ " WHERE cr.id = ? ";
		return currencyDao.findBySql(sql, Map.class,
				jsonObj.getLongValue("costUuid"));
	}

	/**
	 * 20151022 added
	 * 
	 * @author wangxinwei
	 *  paymentUuid
	 *            :订单ID cost_record, 或 airticket_order_moneyAmount 表id
	 *  fundsType
	 *            : 当 fundsType为:借款：1，退款：2，追加成本：3，这三种类型时 取
	 *            airticket_order_moneyAmount 当 fundsType为 :1,2,3以外的其它类型
	 *            取cost_record表数据
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */

	@Override
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public boolean confirmRejectOper(BaseInput4MT input) {
		Long userId = UserUtils.getUser().getId();
		String orderId = input.getParamValue("orderUuid");
		String receiveId = input.getParamValue("receiveUuid");
		String fundsType = input.getParamValue("fundsType");
		boolean flag = true;
		try {
			if(StringUtils.isNotBlank(fundsType) && "2".equals(fundsType)){
				//2表示其他收入驳回
				PayGroup pg = payGroupDao.getById(Integer.valueOf(receiveId));
				pg.setIsAsAccount(2);
				pg.setCreateBy(Integer.valueOf(userId.toString()));
				pg.setUpdateDate(new Date());
				payGroupDao.save(pg);
				costRecordDao.confirmOrCancelPay(CostRecord.PAY_STATUS_CANCEL, userId, new Date(),Long.valueOf(pg.getCostRecordId().toString()));
			}else{
				//1表示订单收款驳回
				AirticketOrder order = airticketOrderDao.getAirticketOrderById(Long
						.valueOf(orderId));
				Orderpay orderpay = orderpayDao.getOrderPayById(Long
						.valueOf(receiveId));
				List<MoneyAmount> payedMoney = moneyAmountDao
						.findAmountBySerialNum(orderpay.getMoneySerialNum());
				List<MoneyAmount> orderpayed = moneyAmountDao
						.findAmountBySerialNum(order.getPayedMoney());
				/** 更新机票订单已付  update by shijun.liu 2016.06.22 bug 14232 财务中心-收款-驳回
				 *  相减了两遍，下面代码注释，仅仅相减一遍即可
				 */
				/**for (int i = 0; i < payedMoney.size(); i++) {
					for (int j = 0; j < orderpayed.size(); j++) {
						if (payedMoney.get(i).getCurrencyId() == orderpayed.get(j)
								.getCurrencyId().intValue()) {
							BigDecimal om = orderpayed.get(j).getAmount();
							BigDecimal pm = payedMoney.get(i).getAmount();
							BigDecimal val = om.subtract(pm);
							orderpayed.get(j).setAmount(val);
							moneyAmountDao.updateOrderForAmount(orderpayed.get(j)
									.getId(), val);
						}
					}
				} */
				if(UserUtils.isMtourUser()){
					flag = moneyAmountService.subtractMoneyAmountForMtour(order.getPayedMoney(),
							orderpay.getMoneySerialNum(), null);
				}else{
					flag = moneyAmountService.subtractMoneyAmount(order.getPayedMoney(),
							orderpay.getMoneySerialNum(), null);
				}

				// 更新orderpay表状态
				orderpay.setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
				orderpay.setUpdateDate(new Date());
				orderpayDao.save(orderpay);
				// 更新moneyamount表状态
				moneyAmountDao.updateMoneyType(orderpay.getMoneySerialNum(),
						Context.MONEY_TYPE_REJECT);	
			} 
		}catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean payedConfirm(BaseInput4MT input) {
		// TODO Auto-generated method stub
		boolean flag = false;
		String receiveFundsTypeCode = input.getParamValue("receiveFundsTypeCode");//2表示其他收入收款， 1订单收款
		if(StringUtils.isNotBlank(receiveFundsTypeCode)){
			if("1".equals(receiveFundsTypeCode)){
				flag = payedConfirmForDZ(input);
			}else if("2".equals(receiveFundsTypeCode)){
				flag = payedConfirmForCost(input);
			}
		}
		return flag;
	}
	public boolean payedConfirmForCost(BaseInput4MT input){
		boolean flag = true;
		String orderId = input.getParamValue("orderUuid");
		String orderpayId = input.getParamValue("receiveUuid");
		String payer = input.getParamValue("payer");
		String accountDate = input.getParamValue("arrivalBankDate");
		String bankName = input.getParamValue("paymentBank");
		String paymentAccount = input.getParamValue("paymentAccount");
		String receiveBank = input.getParamValue("receiveBank");
		String receiveAccount = input.getParamValue("receiveAccount");
		String checkNo = input.getParamValue("checkNo");
		String checkIssueDate = input.getParamValue("checkIssueDate");
		String rate = input.getParamValue("exchange");
		String totalPrice = input.getParamValue("totalPrice");
		try{
			//汇率不能为空
			if(StringUtils.isBlank(rate)){
				flag = false;
			}
			PayGroup paygroup =	payGroupDao.getById(Integer.valueOf(orderpayId));
			if(StringUtils.isNotBlank(payer)){
			    paygroup.setPayerName(payer);
			}
			if(StringUtils.isNotBlank(accountDate)){
			   paygroup.setAccountDate(DateUtils.parseDate(accountDate));
			}
			if(StringUtils.isNotBlank(bankName)){
			   paygroup.setBankName(bankName);
			}
			if(StringUtils.isNotBlank(paymentAccount)){
			   paygroup.setBankAccount(paymentAccount);
			}
			if(StringUtils.isNotBlank(receiveBank)){
			   paygroup.setReceiveBankName(receiveBank);
			}
			if(StringUtils.isNotBlank(receiveAccount)){
			   paygroup.setReceiveAccount(receiveAccount);
			}
			if(StringUtils.isNotBlank(checkNo)){
			   paygroup.setCheckNumber(checkNo);
			}
			if(StringUtils.isNotBlank(checkIssueDate)){
			   paygroup.setInvoiceDate(DateUtils.parseDate(checkIssueDate));
			}
			Long userId = UserUtils.getUser().getId();
			paygroup.setUpdateBy(Integer.valueOf(String.valueOf(userId)));
			paygroup.setUpdateDate(new Date());
			paygroup.setIsAsAccount(1);

			String payPrice = paygroup.getPayPrice();
			MoneyAmount ma = moneyAmountDao.findOneAmountBySerialNum(payPrice);
			ma.setExchangerate(new BigDecimal(rate));
			// ma.setAmount(new BigDecimal(totalPrice));
		   // payGroupDao.save(paygroup);
			//更新cost_record   modify by hhx
			CostRecord cr = (CostRecord) orderpayDao.getSession().load(CostRecord.class, paygroup.getCostRecordId().longValue());
			cr.setRate(new BigDecimal(rate));
			cr.setPayStatus(1);
			orderpayDao.getSession().update(cr);
		}catch(Exception e){
			flag =false;
			e.printStackTrace();
		}
		return flag;
	}
   /**
    * 订单收款确认
    * @zhaohaiming
    * */
    @Transactional(readOnly = false, rollbackFor = {Exception.class })
	public boolean payedConfirmForDZ(BaseInput4MT input){

		// TODO Auto-generated method stub
		boolean flag = true;
		String orderId = input.getParamValue("orderUuid");
		String orderpayId = input.getParamValue("receiveUuid");
		String payer = input.getParamValue("payer");
		String accountDate = input.getParamValue("arrivalBankDate");
		String bankName = input.getParamValue("paymentBank");
		String paymentAccount = input.getParamValue("paymentAccount");
		String receiveBank = input.getParamValue("receiveBank");
		String receiveAccount = input.getParamValue("receiveAccount");
		//String receiveFundsTypeCode = input.getParamValue("receiveFundsTypeCode");//2表示其他收入收款， 1订单收款
		String rate = input.getParamValue("exchange");
		String totalPrice = input.getParamValue("totalPrice");

		try {
			if(StringUtils.isBlank(rate)){
				flag = false;
			}
			// 更新orderpay表信息
			Orderpay orderpay = orderpayDao.getOrderPayById(Long
					.valueOf(orderpayId));
			orderpay.setPayerName(payer);
			orderpay.setAccountDate(DateUtils.parseDate(accountDate));
			// 达帐是1 未达帐为0
			orderpay.setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
            orderpay.setBankName(bankName);
            orderpay.setBankAccount(paymentAccount);
            orderpay.setToBankNname(receiveBank);
            orderpay.setToBankAccount(receiveAccount);
			// 更新机票订单达账金额
			// 1、获取订单的达账金额和已付金额
			AirticketOrder airticketOrder = airticketPreOrderDao.findOne(Long
					.valueOf(orderId));
			String accountedMoney = airticketOrder.getAccountedMoney();
			// 2、如果是第一次进行收款确认操作，达账金额的序列号为空，需生成，如果不是第一次进行收款确认操作，跳过if判断
			if (StringUtils.isBlank(accountedMoney)) {
				accountedMoney = UUID.randomUUID().toString();
				airticketOrder.setAccountedMoney(accountedMoney);
//				airticketOrderDao.getSession().update(airticketOrder);
			}
			
			// 3、根据orderpay serialnum 获取本次金额
			List<MoneyAmount> payedMoneyList = moneyAmountDao
					.findAmountBySerialNum(orderpay.getMoneySerialNum());
			//
			if (CollectionUtils.isNotEmpty(payedMoneyList)) {
				MoneyAmount payed = payedMoneyList.get(0);
				// edit by majiancheng(2015-11-5)
				List<MoneyAmount> result = moneyAmountDao
						.findAmountBySerialNumAndCurrencyId(accountedMoney,
								payed.getCurrencyId());
				//美途国际收款时，汇率可以改变，所以每次保存都是重新创建MoneyAmount对象
				if (CollectionUtils.isNotEmpty(result) && !UserUtils.isMtourUser()) {
					result.get(0).setAmount(result.get(0).getAmount().add(payed.getAmount()));
				} else {
					MoneyAmount ma = new MoneyAmount();
					ma.setSerialNum(accountedMoney);
					ma.setMoneyType(Context.MONEY_TYPE_DZ);
					ma.setExchangerate(new BigDecimal(rate));
					ma.setAmount(payed.getAmount());
					ma.setBusindessType(payed.getBusindessType());
					ma.setCreateTime(new Date());
					ma.setCreatedBy(UserUtils.getUser().getId());
					ma.setCurrencyId(payed.getCurrencyId());
					ma.setOrderType(payed.getOrderType());
					ma.setUid(payed.getUid());
					ma.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
					ma.setPayedAccountedUUID(orderpay.getMoneySerialNum());
					moneyAmountService.saveOrUpdateMoneyAmount(ma);
					//修改订单收款记录的汇率 0094 update by shijun.liu
					payed.setExchangerate(new BigDecimal(rate));
					moneyAmountService.saveOrUpdateMoneyAmount(payed);
					//修改订单中已收金额的汇率 0094 update by jinxin.gao(已收的和取消的汇率一起修改)
					//MoneyAmount payedMoney = moneyAmountDao.findOneAmountByUUID(orderpay.getMoneySerialNum());
					List<MoneyAmount> payedMoneys = moneyAmountDao.findAmountListByUUID(orderpay.getMoneySerialNum());
					for (MoneyAmount payedMoney : payedMoneys) {
						if(null != payedMoney){
//							payedMoney.setExchangerate(new BigDecimal(rate));
//							moneyAmountService.saveOrUpdateMoneyAmount(payedMoney);
							String sql = "UPDATE money_amount m set m.exchangerate = ? where id = ?";
							orderpayDao.updateBySql(sql, rate, payedMoney.getId());
						}
					}
				}
			}
			//刷新数据(勿删除下面一句)
			orderpayDao.flush();
			//订单收款时更新收款状态
			mtourOrderService.updateAirticketOrderPaymentStatus(orderId);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	
	}
	@Override
	public File createMtourPaySheetDownLoadFile(String paymentUuid,
			String fundsType) throws IOException, TemplateException {

		//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
		Long recordId = null;
		Integer moneyType = null;
		
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		
		
		
		
		
		if(UserUtils.isMtourUser()){ //美途用户


			/**
			 * 当 fundsType为:借款：1，退款：2，追加成本：3，这三种类型时 取 airticket_order_moneyAmount 当
			 * fundsType为 :1,2,3以外的其它类型 取cost_record表数据
			 */
			try {
				if ("1".equals(fundsType) || "2".equals(fundsType)|| "3".equals(fundsType)) {
					
					AirticketOrderMoneyAmount aoma = airticketOrderMoneyAmountService.getById(Integer.parseInt(paymentUuid));
					
					AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(aoma.getAirticketOrderId() + ""));
					// 日期：
					
					//add by songyang  2016年2月18日18:22:48  bug 12453
					root.put("applyDate", new SimpleDateFormat(DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY).format(aoma.getCreateDate()));
//					root.put("applyDate", aoma.getCreateDateString());
					// 团号：
					ActivityAirTicket activityAirTicket =  activityAirTicketServiceImpl.getActivityAirTicketById(airticketOrder.getAirticketId());
					root.put("groupCode",activityAirTicket.getGroupCode());
					
					// PNR号：
					root.put("pnr","");
					
					// 支付对象：
					/**
					 * 如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
					 * 借款：1，退款：2，追加成本：3,成本付款：4
					 */
					if ("1".equals(fundsType)) {
						root.put("payObject", "");//'支付对象名称'
					}else if ("2".equals(fundsType)||"3".equals(fundsType)) {
						Agentinfo agentinfo = agentinfoService.findOne(airticketOrder.getAgentinfoId());
						if (null!=agentinfo) {
							root.put("payObject", agentinfo.getAgentName());//'支付对象名称'
						}else {
							root.put("payObject", "非签渠道");//'支付对象名称'
						}
					}else{
						root.put("payObject", "非签渠道");//'支付对象名称'
					}
					
					
					// 用途：
					/*if ("1".equals(fundsType)) {// 追加成本
						root.put("uses",getWithSubString(FinanceController.YFK, 30));// '用途'
					} else if ("2".equals(fundsType)) {// 预付款
						root.put("uses", getWithSubString(FinanceController.TK, 30));// '用途'
					} else if ("3".equals(fundsType)) {
						root.put("uses",getWithSubString(FinanceController.ZJCB, 30));// '用途'
					}*/
					
					//wangxinwei 20151111 added 用途有变化，改为取输入的款项名称:
					String fundsName = aoma.getFundsName();
					root.put("uses",(fundsName==null)?"":fundsName);
					
					//流水号
					String serialNum = getSerialNumByTableNameAndRecordId("airticket_order_moneyAmount", aoma.getId());
					root.put("serialNum", null==serialNum?"":serialNum);
					
					//处理币种
					Currency currency = currencyDao.findById(aoma.getCurrencyId().longValue());
					Double amount = aoma.getAmount();
					amount = (null==amount)?0:amount;
					
					if (null!=currency) {
						if ("¥".equals(currency.getCurrencyMark())) {
							// 计人民币小写：
							root.put("rnbAmount",currency.getCurrencyMark()+fmtMicrometer(amount.toString()));
							// 计人民币大写：
							//0044需求修改 add by majiancheng 2016-01-11
							root.put("rnbAmountCN",MoneyAmountUtils.generUppercase(currency.getCurrencyName() ,amount));
							//root.put("rnbAmountCN",digitUppercase(amount));
							// 外币小写：
							root.put("totalOther","");
							// 外币大写：
							root.put("totalOtherCN","");
						}else{
							// 计人民币小写：
							root.put("rnbAmount","");
							// 计人民币大写：
							root.put("rnbAmountCN","");
							// 外币小写：
							root.put("totalOther",currency.getCurrencyMark()+fmtMicrometer(amount.toString()));
							// 外币大写：
							String totalother_cn = digitUppercase(amount);
							//totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
							//0044需求修改 add by majiancheng 2016-01-11
							root.put("totalOtherCN",MoneyAmountUtils.generUppercase(currency.getCurrencyName() ,amount));
							//root.put("totalOtherCN",currency.getCurrencyName()+totalother_cn);
						}
					}else{
						
							// 计人民币小写：
							root.put("rnbAmount","");
							// 计人民币大写：
							root.put("rnbAmountCN","");
							// 外币小写：
							root.put("totalOther","");
							// 外币大写：
							root.put("totalOtherCN","");
						
					}
					
					
					
					
					// 申请人：
					root.put("applyPersonName",UserUtils.getUser(Long.parseLong(aoma.getCreateBy() + "")).getName());
					
					//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
					recordId = aoma.getId().longValue();
					if(aoma.getMoneyType() == 1) {
						moneyType = Refund.MONEY_TYPE_BORROW;
					} else if(aoma.getMoneyType() == 2) {
						moneyType = Refund.MONEY_TYPE_RETURNMONEY;
					} else if(aoma.getMoneyType() == 3) {
						moneyType = Refund.MONEY_TYPE_ADDCOST;
					}
					
				} else {
					CostRecord costRecord = costManageService.findOne(Long.parseLong(paymentUuid));
					AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(costRecord.getOrderId());

					// 日期：
					root.put("applyDate", DateUtils.formatDate(costRecord.getCreateDate(), "yyyy年MM月dd日"));
					// 团号：
					ActivityAirTicket activityAirTicket =  activityAirTicketServiceImpl.getActivityAirTicketById(airticketOrder.getAirticketId());
					root.put("groupCode",activityAirTicket.getGroupCode());
					// PNR号：如果是成本付款，则取成本付款中的大编号；如果是退款、追加成本、借款，则为空
					if (null != costRecord.getBigCode()) {
						root.put("pnr",costRecord.getBigCode());
					} else {
						root.put("pnr","");
					}
					// 支付对象：如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
					if (null!=costRecord.getSupplyName()) {
						root.put("payObject",costRecord.getSupplyName());
					}else{
						root.put("payObject","");
					}
					
					// 用途：如果是成本付款，则为成本支付；如果是借款，则为预付款；如果是追加成本，则为追加成本；如果是退款，则为退款
					//root.put("uses", getWithSubString(FinanceController.CBZF, 30));// '用途'
					root.put("uses", costRecord.getName());// '用途'
					
					String serialNum = getSerialNumByTableNameAndRecordId("cost_record", costRecord.getId().intValue());
					root.put("serialNum", null==serialNum?"":serialNum);
					
					
					
					//处理币种
					Currency currency = currencyDao.findById(costRecord.getCurrencyId().longValue());
					
					if(null!=currency){//处理币种为空的问题
						
						if ("¥".equals(currency.getCurrencyMark())) {
							// 计人民币小写：
							root.put("rnbAmount",(currency.getCurrencyMark()+fmtMicrometer(costRecord.getPriceAfter() + "")));
							// 计人民币大写：
							//root.put("rnbAmountCN",digitUppercase(Double.parseDouble(costRecord.getPriceAfter() + "")));
							root.put("rnbAmountCN",MoneyAmountUtils.generUppercase(currency.getCurrencyName() ,Double.parseDouble(costRecord.getPriceAfter() + "")));
							// 外币小写：
							root.put("totalOther","");
							// 外币大写：
							root.put("totalOtherCN","");
						}else{
							// 计人民币小写：
							root.put("rnbAmount","");
							// 计人民币大写：
							root.put("rnbAmountCN","");
							
							//---------20151230  wxw modified -----
							// 外币小写：
							Double amount = costRecord.getPrice().doubleValue()*costRecord.getQuantity();
							root.put("totalOther",(currency.getCurrencyMark()+fmtMicrometer(amount.toString())));
							// 外币大写：
							//String totalother_cn = digitUppercase(amount);
							//totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
							//root.put("totalOtherCN",currency.getCurrencyName()+totalother_cn);
							root.put("totalOtherCN",MoneyAmountUtils.generUppercase(currency.getCurrencyName() ,amount));
							
						}
						
					}else{
						
						// 计人民币小写：
						root.put("rnbAmount","");
						// 计人民币大写：
						root.put("rnbAmountCN","");
						// 外币小写：
						root.put("totalOther","");
						// 外币大写：
						root.put("totalOtherCN","");
						
					}
					
					
					
					// 申请人：
					root.put("applyPersonName", costRecord.getCreateBy().getName());

					//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
					recordId = costRecord.getId();
					moneyType = Refund.MONEY_TYPE_COST;
				}
				

				//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
				List<Refund> refunds = refundService.getRefundsByRecordId(recordId, moneyType);
				StringBuffer sb = new StringBuffer();
				List<String> userIds = new ArrayList<String>();
				if(CollectionUtils.isNotEmpty(refunds)) {
					for(Refund refund : refunds) {
						if(!userIds.contains(refund.getCreateBy().getId().toString())) {
							sb.append(refund.getCreateBy().getName());
							sb.append(",");
							userIds.add(refund.getCreateBy().getId().toString());
						}
					}
					sb.deleteCharAt(sb.length()-1);
				}
				
				if(sb.length() > 12) {
					sb = new StringBuffer(sb.substring(0, 12));
					//sb.append("...");
				}
				
				root.put("paymentPeople", sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return FreeMarkerUtil.generateFile("mtourpaysheet.ftl","mtourpaysheet.doc", root);
			
			
		}else{ //非美途用户
			
			/**
			 * 当 fundsType为:借款：1，退款：2，追加成本：3，这三种类型时 取 airticket_order_moneyAmount 当
			 * fundsType为 :1,2,3以外的其它类型 取cost_record表数据
			 */
			try {
				if ("1".equals(fundsType) || "2".equals(fundsType)|| "3".equals(fundsType)) {
					
					AirticketOrderMoneyAmount aoma = airticketOrderMoneyAmountService.getById(Integer.parseInt(paymentUuid));
					
					AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(aoma.getAirticketOrderId() + ""));
					// 日期：
					//add by songyang  2016年2月18日18:22:48  bug 12453
					root.put("applyDate", new SimpleDateFormat(DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY).format(aoma.getCreateDate()));
//					root.put("applyDate", aoma.getCreateDateString());
					// 团号：
					ActivityAirTicket activityAirTicket =  activityAirTicketServiceImpl.getActivityAirTicketById(airticketOrder.getAirticketId());
					root.put("groupCode",getWithSubString(activityAirTicket.getGroupCode(), 30));
					
					// PNR号：
					//root.put("pnr", getWithSubString("", 28));
					
					// 支付对象：
					/**
					 * 如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
					 * 借款：1，退款：2，追加成本：3,成本付款：4
					 */
					if ("1".equals(fundsType)) {
						root.put("payObject", getWithSubString("", 28));//'支付对象名称'
					}else if ("2".equals(fundsType)||"3".equals(fundsType)) {
						Agentinfo agentinfo = agentinfoService.findOne(airticketOrder.getAgentinfoId());
						if (null!=agentinfo) {
							root.put("payObject", getWithSubString(agentinfo.getAgentName(), 28));//'支付对象名称'
						}else {
							root.put("payObject", getWithSubString("非签渠道", 28));//'支付对象名称'
						}
					}else{
						root.put("payObject", getWithSubString("非签渠道", 28));//'支付对象名称'
					}
					
					
					// 用途：
					/*if ("1".equals(fundsType)) {// 追加成本
						root.put("uses",getWithSubString(FinanceController.YFK, 30));// '用途'
					} else if ("2".equals(fundsType)) {// 预付款
						root.put("uses", getWithSubString(FinanceController.TK, 30));// '用途'
					} else if ("3".equals(fundsType)) {
						root.put("uses",getWithSubString(FinanceController.ZJCB, 30));// '用途'
					}*/
					
					//wangxinwei 20151111 added 用途有变化，改为取输入的款项名称:
					String fundsName = aoma.getFundsName();
					root.put("uses",getWithSubString((fundsName==null)?"":fundsName, 26));
					
					//流水号
					String serialNum = getSerialNumByTableNameAndRecordId("airticket_order_moneyAmount", aoma.getId());
					root.put("serialNum", null==serialNum?"":serialNum);
					
					//处理币种
					Currency currency = currencyDao.findById(aoma.getCurrencyId().longValue());
					Double amount = aoma.getAmount();
					amount = (null==amount)?0:amount;
					if ("¥".equals(currency.getCurrencyMark())) {
						// 计人民币小写：
						root.put("rnbAmount",getWithSubString(currency.getCurrencyMark()+fmtMicrometer(amount.toString()), 18));
						// 计人民币大写：
						root.put("rnbAmountCN",getWithSubString(digitUppercase(amount), 22));
						// 外币小写：
						root.put("totalOther",getWithSubString("", 18));
						// 外币大写：
						root.put("totalOtherCN",getWithSubString("", 22));
					}else{
						// 计人民币小写：
						root.put("rnbAmount",getWithSubString("", 18));
						// 计人民币大写：
						root.put("rnbAmountCN",getWithSubString("", 22));
						// 外币小写：
						root.put("totalOther",getWithSubString(currency.getCurrencyMark()+fmtMicrometer(amount.toString()), 18));
						// 外币大写：
						String totalother_cn = digitUppercase(amount);
						totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
						root.put("totalOtherCN",getWithSubString(totalother_cn, 22));
					}
					
					// 申请人：
					root.put("applyPersonName",UserUtils.getUser(Long.parseLong(aoma.getCreateBy() + "")).getName());
					
				} else {
					CostRecord costRecord = costManageService.findOne(Long.parseLong(paymentUuid));
					AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(costRecord.getOrderId());

					// 日期：
					root.put("applyDate", DateUtils.formatDate(costRecord.getCreateDate(), "yyyy年MM月dd日"));
					// 团号：
					ActivityAirTicket activityAirTicket =  activityAirTicketServiceImpl.getActivityAirTicketById(airticketOrder.getAirticketId());
					root.put("groupCode",getWithSubString(activityAirTicket.getGroupCode(), 30));
					// PNR号：如果是成本付款，则取成本付款中的大编号；如果是退款、追加成本、借款，则为空
					if (null != costRecord.getBigCode()) {
						root.put("pnr",getWithSubString(costRecord.getBigCode(), 28));
					} else {
						root.put("pnr", getWithSubString("", 28));
					}
					// 支付对象：如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
					if (null!=costRecord.getSupplyName()) {
						root.put("payObject",getWithSubString(costRecord.getSupplyName(), 28));
					}else{
						root.put("payObject",getWithSubString("", 28));
					}
					
					// 用途：如果是成本付款，则为成本支付；如果是借款，则为预付款；如果是追加成本，则为追加成本；如果是退款，则为退款
					//root.put("uses", getWithSubString(FinanceController.CBZF, 30));// '用途'
					root.put("uses", getWithSubString(costRecord.getName(), 26));// '用途'
					
					String serialNum = getSerialNumByTableNameAndRecordId("cost_record", costRecord.getId().intValue());
					root.put("serialNum", null==serialNum?"":serialNum);
					
					//处理币种
					Currency currency = currencyDao.findById(costRecord.getCurrencyId().longValue());
					if ("¥".equals(currency.getCurrencyMark())) {
						// 计人民币小写：
						root.put("rnbAmount",getWithSubString((currency.getCurrencyMark()+fmtMicrometer(costRecord.getPriceAfter() + "")),18));
						// 计人民币大写：
						root.put("rnbAmountCN",getWithSubString(digitUppercase(Double.parseDouble(costRecord.getPriceAfter() + "")), 22));
						// 外币小写：
						root.put("totalOther",getWithSubString("", 18));
						// 外币大写：
						root.put("totalOtherCN",getWithSubString("", 22));
					}else{
						// 计人民币小写：
						root.put("rnbAmount",getWithSubString("", 18));
						// 计人民币大写：
						root.put("rnbAmountCN",getWithSubString("",22));
						// 外币小写：
						Double amount = costRecord.getPrice().doubleValue()*costRecord.getQuantity();
						root.put("totalOther",getWithSubString((currency.getCurrencyMark()+fmtMicrometer(amount.toString())), 18));
						// 外币大写：
						String totalother_cn = digitUppercase(amount);
						totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
						root.put("totalOtherCN",getWithSubString(totalother_cn, 22));
					}
					
					// 申请人：
					root.put("applyPersonName", costRecord.getCreateBy().getName());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return FreeMarkerUtil.generateFile("mtourpaysheet4other.ftl","mtourpaysheet.doc", root);
		}
		

	}

	@Override
	public PayedDetail getPayedDetail(BaseInput4MT input) {
		String orderId = input.getParamValue("orderUuid");
		String orderpayId = input.getParamValue("receiveUuid");
		PayedDetail pd = new PayedDetail();
		pd.setOrderUuid(orderId);
		try {
			if (StringUtils.isNotBlank(orderpayId)) {
				List<Map<String,Object>> list = orderpayDao.getPayedDetail(Integer.valueOf(orderpayId));
				pd.setReceiveUuid(orderpayId);
				pd.setReceiveType(list.get(0).get("receiveType")!=null?list.get(0).get("receiveType").toString():"");
				pd.setReceiveMethod(list.get(0).get("payType")!=null?list.get(0).get("payType").toString():"");
				pd.setPayer(list.get(0).get("payerName")!=null?list.get(0).get("payerName").toString():"");
				pd.setCheckNo(list.get(0).get("checkNumber")!=null?list.get(0).get("checkNumber").toString():"");
				pd.setCheckIssueDate(list.get(0).get("invoiceDate")!=null?DateUtils.formatCustomDate((Date)list.get(0).get("invoiceDate"), "yyyy-MM-dd"):"");
				pd.setPaymentBank(list.get(0).get("bankName")!=null?list.get(0).get("bankName").toString():"");
				pd.setPaymentAccount(list.get(0).get("bankAccount")!=null?list.get(0).get("bankAccount").toString():"");
				pd.setReceiveBank(list.get(0).get("toBankNname")!=null?list.get(0).get("toBankNname").toString():"");
				pd.setReceiveAccount(list.get(0).get("toBankAccount")!=null?list.get(0).get("toBankAccount").toString():"");
				pd.setMemo(list.get(0).get("remarks")!=null?list.get(0).get("remarks").toString():"");
                pd.setReceivePeopleCount(list.get(0).get("receivePeopleCount")!=null?(Integer)list.get(0).get("receivePeopleCount"):0);
				pd.setReceiveAmount(list.get(0).get("amount")!=null?list.get(0).get("amount").toString():"");
				pd.setCurrencyUuid(list.get(0).get("currencyId")!=null?list.get(0).get("currencyId").toString():"");
				pd.setExchangeRate(list.get(0).get("exchangerate")!=null?Double.valueOf(list.get(0).get("exchangerate").toString()):1);
				String payVoucher = list.get(0).get("payVoucher")!=null?list.get(0).get("payVoucher").toString():"";
				// 获取订单的支付凭证信息
				if (StringUtils.isNotBlank(payVoucher)) {
					String[] docId = payVoucher.split(",");
					List<Long> docIdList = new ArrayList<Long>();
					for (int index = 0; index < docId.length; index++) {
						docIdList.add(Long.parseLong(docId[index]));
					}
					List<DocInfo> list1 = docInfoDao.findDocInfoByIds(docIdList);
					List<Dict> dictList = new ArrayList<Dict>();
					if (CollectionUtils.isNotEmpty(list1)) {
						for (DocInfo d : list1) {
							Dict dict = new Dict();
							dict.setAttachmentUrl(d.getDocPath());
							dict.setFileName(d.getDocName());
							dict.setAttachmentUuid(d.getId().toString());
							dictList.add(dict);
						}
					}
					pd.setAttachments(dictList);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pd;
	}

	/**
	 * 将金额转换为 大写汉字
	 * 
	 * @param n
	 *            :要转换的金额
	 * @return
	 */
	private static String digitUppercase(double n) {
		String fraction[] = { "角", "分" };
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };
		String head = n < 0 ? "负" : ""; // //负 -》红字
		n = Math.abs(n);
		String s = "";
		for (int i = 0; i < fraction.length; i++) {
			s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i])
					.replaceAll("(零.)+", "");
		}
		if (s.length() < 1) {
			s = "整";
		}
		int integerPart = (int) Math.floor(n);

		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
			String p = "";
			for (int j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[integerPart % 10] + unit[1][j] + p;
				integerPart = integerPart / 10;
			}
			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]
					+ s;
		}
		return head
				+ s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
						.replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}

	/**
	 * 数字格式化
	 * 
	 * @param text
	 * @return
	 */
	private String fmtMicrometer(String text) {
		DecimalFormat df = null;
		/*
		 * if (text.indexOf(".") > 0) { if (text.length() - text.indexOf(".") -
		 * 1 == 0) { df = new DecimalFormat("###,##0."); } else if
		 * (text.length() - text.indexOf(".") - 1 == 1){ df = new
		 * DecimalFormat("###,##0.0"); } else{ df = new
		 * DecimalFormat("###,##0.00"); }
		 * 
		 * } else{ df = new DecimalFormat("###,##0"); }
		 */
		df = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	/**
	 * 其他收入记录详情
	 * 
	 * @author hhx
	 * @date 2015-10-22
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> getOtherPayDetail(BaseInput4MT input) {
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		String sql = "select cr.bigCode AS PNR, cr.NAME AS fundsName,cr.price AS price,cr.quantity AS peopleCount, "
				+ "cr.currencyId AS convertedCurrencyUuid,cr.priceAfter AS convertedAmount,cr.supplyType AS tourOperatorChannelCategoryCode, "
				+ "cr.supplyName AS tourOperatorOrChannelName,op.orderId AS orderUuid,op.paymentStatus AS speedyClearance, "
				+ "op.checkNumber AS checkNo,op.invoiceDate AS checkIssueDate,op.payType AS paymentMethodCode, "
				+ "op.payTypeName AS payTypeName, "
				+ "op.from_bank_name AS paymentBank,op.from_account AS paymentAccount,op.receive_bank_name AS receiveBank, "
				+ "op.receive_account AS receiveAccount,op.remarks AS memo,payVoucher as docId "
				+ "from orderpay op join cost_record cr on op.orderId = cr.id where op.id = ? ";
		return currencyDao.findBySql(sql, Map.class,
				jsonObj.getLongValue("otherRevenueUuid"));
	}

	/**
	 * 查询应付金额
	 * 
	 * @param orderId
	 *            , fundsType
	 * @author gaoang
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryPayableAmount(String orderId,
			String fundsType) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT aom.currency_id AS currencyUuid,aom.amount AS amount FROM airticket_order_moneyAmount aom ");
		sql.append(" WHERE aom.delFlag='0' AND aom.status=1 ");// status:默认是1，撤销为0
		sql.append(" AND aom.airticket_order_id=? AND aom.moneyType=? ");

		return financeDao.findBySql(sql.toString(), Map.class, orderId,
				fundsType);
	}

	@Override
	public List<OrderDetailJsonBean> getOrderDetail(String channelId) {

		List<OrderDetailJsonBean> beanlist = financeDao.getOrderDetail(channelId);
		if (beanlist != null && !beanlist.isEmpty()) {
			for (OrderDetailJsonBean bean : beanlist) {
				if (bean != null) {
					if (StringUtils.isNotBlank(bean.getArrivedAmountUuid())) {
						List<PriceAmount> arrivedAmount = changeMtourOrder(moneyAmountDao.mergeAmountBySerialNum(bean.getArrivedAmountUuid()), new ArrayList<PriceAmount>());// 到账金额
						bean.setArrivedAmount(arrivedAmount);
					}
					if (StringUtils.isNotBlank(bean.getOrderAmountUuid())) {
						List<PriceAmount> totalsalerPrice = changeMtourOrder(moneyAmountDao.mergeAmountBySerialNum(bean.getOrderAmountUuid()), new ArrayList<PriceAmount>());// 外报总额
						bean.setOrderAmount(totalsalerPrice);
					}
					if (StringUtils.isNotBlank(bean.getReceivedAmountUuid())) {
						List<PriceAmount> receivedAmount = changeMtourOrder(moneyAmountDao.mergeAmountBySerialNum(bean.getReceivedAmountUuid()), new ArrayList<PriceAmount>());// 已收金额
						bean.setReceivedAmount(receivedAmount);
					}
				}
			}
		}
		return beanlist;
	}

	/**
	 * 将List<MoneyAmount> 转换为 List<PriceAmount>
	 * 
	 * @author gao 2015年10月22日
	 * @param list
	 */
	private List<PriceAmount> changeMtourOrder(List<MoneyAmount> list,
			List<PriceAmount> price) {
		if (list != null && !list.isEmpty()) {
			for (MoneyAmount amount : list) {
				PriceAmount money = new PriceAmount();
				money.setCurrencyUuid(amount.getCurrencyId());
				money.setAmount(amount.getAmount());
				price.add(money);
			}
		}
		return price;
	}

	@Override
	public List<Map<String, Object>> getPaymentCost(Integer costId) {
		return financeDao.getPaymentCost(costId);
	}
	
    public PayedMoneyPojo getCostNopayMoney(String costId,String fundsType,String companyUuid){
    	PayedMoneyPojo pmp = new PayedMoneyPojo();
    	if(StringUtils.isNotBlank(costId) && StringUtils.isNotBlank(fundsType)){
    		//获取成本表数据
    		CostRecord cr = costRecordDao.findOne(Long.valueOf(costId));
    		//获取成本付款的已付金额
    		List<MoneyAmountVO> list = getRefundPaydMoney(costId, fundsType, companyUuid);
    		List<MoneyAmountVO> moneyList = new ArrayList<MoneyAmountVO>();// 应付金额列表
    		List<MoneyAmountVO> nopayList = new ArrayList<MoneyAmountVO>();// 未付金额列表
    		if(cr != null){
    			//成本付款的应付金额-成本的已付金额
				Integer currencyId = cr.getCurrencyId();//
				BigDecimal money = cr.getPrice().multiply(new BigDecimal(cr.getQuantity()));//成本付款应付金额
				//拼装应付金额集合
				MoneyAmountVO mvo = new MoneyAmountVO();
				mvo.setCurrencyUuid(currencyId);
				if(null != cr.getRate()){
					mvo.setExchangeRate(Double.valueOf(cr.getRate().toString()));
				}else{
					Currency c = currencyDao.findById(Long.valueOf(cr.getCurrencyId().toString()));
					if(c.getConvertLowest()!= null){
						if("0".equals(c.getConvertLowest().toString())){
							mvo.setExchangeRate(1d);
						}else{
							mvo.setExchangeRate(Double.valueOf(c.getConvertLowest().toString()));
						}
					}
				}
				mvo.setAmount(Double.valueOf(money.toString()));
				moneyList.add(mvo);
				pmp.setPayableAmount(moneyList);
				//已付金额不为空的时候，未付金额=成本金额-已付金额
    			if(CollectionUtils.isNotEmpty(list)){
    				MoneyAmountVO notpay = new MoneyAmountVO();
					MoneyAmountVO vo = list.get(0);
					notpay.setAmount(Double.valueOf(money.subtract(new BigDecimal(vo.getAmount().toString())).toString()));
					notpay.setExchangeRate(vo.getExchangeRate());
					notpay.setCurrencyUuid(currencyId);
					notpay.setChangeable(false);
					nopayList.add(notpay);
    				pmp.setPayingAmount(nopayList);
    			}else{
    				//已付金额为空的话，未付的金额=成本的应付金额
    				List<MoneyAmountVO> notPayedMoneyList = new ArrayList<MoneyAmountVO>();
    				MoneyAmountVO notPayedMoney = moneyList.get(0);
    				MoneyAmountVO newNotPayedMoney = notPayedMoney.clone();
    				newNotPayedMoney.setChangeable(true);
    				notPayedMoneyList.add(newNotPayedMoney);
    				pmp.setPayingAmount(notPayedMoneyList);
    			}
    			pmp.setReceiveAccount(cr.getBankAccount()==null?"":cr.getBankAccount());
    			pmp.setReceiveBank(cr.getBankName()==null?"":cr.getBankName());
    			pmp.setReceiveCompanyUuid(cr.getSupplyId()==null?"":cr.getSupplyId().toString());
    			pmp.setReceiveCompany(cr.getSupplyName()==null?"":cr.getSupplyName());
    			pmp.setTourOperatorChannelCategoryCode(cr.getSupplyType().toString());
    		}
    	}
    	return pmp;
    }
    
    
    public PayedMoneyPojo getNopayMoney(String costId,String fundsType,String companyUuid){
    	PayedMoneyPojo pmp = new PayedMoneyPojo();
    	
    	if(StringUtils.isNotBlank(costId) && StringUtils.isNotBlank(fundsType)){
    		//获取退款，返佣，借款数据
    		AirticketOrderMoneyAmount  cr = airticketOrderMoneyAmountDao.getById(Integer.valueOf(costId));
    		//获取成本表数据
    		AirticketOrder ao = airticketOrderDao.getAirticketOrderById(cr.getAirticketOrderId().longValue());
    		//获取成本付款的已付金额
    		List<MoneyAmountVO> list = getRefundPaydMoney(costId, fundsType, companyUuid);
    		List<MoneyAmountVO> moneyList = new ArrayList<MoneyAmountVO>();// 应付金额列表
    		List<MoneyAmountVO> nopayList = new ArrayList<MoneyAmountVO>();// 未付金额列表
    		if(cr != null){
    			//成本付款的应付金额-成本的已付金额
				Integer currencyId = cr.getCurrencyId();//
				BigDecimal money = new BigDecimal(cr.getAmount().toString());//成本付款应付金额
				//拼装应付金额集合
				MoneyAmountVO mvo = new MoneyAmountVO();
				mvo.setCurrencyUuid(currencyId);
				if(null != cr.getExchangerate()){
					mvo.setExchangeRate(cr.getExchangerate());
				}else {
					Currency c = currencyDao.findById(Long.valueOf(cr.getCurrencyId().toString()));
					if(c.getConvertLowest()!= null){
						if("0".equals(c.getConvertLowest().toString())){
							mvo.setExchangeRate(1d);
						}else{
						    mvo.setExchangeRate(Double.valueOf(c.getConvertLowest().toString()));
						}
					}
				}
				mvo.setAmount(Double.valueOf(money.toString()));
				moneyList.add(mvo);
				pmp.setPayableAmount(moneyList);
				//已付金额不为空，未付金额=应付金额-已付金额
    			if(CollectionUtils.isNotEmpty(list)){
    				MoneyAmountVO notpay = new MoneyAmountVO();
					MoneyAmountVO vo = list.get(0);
    				BigDecimal am = money.subtract(new BigDecimal(vo.getAmount().toString()));
					notpay.setAmount(Double.valueOf(am.toString()));
					notpay.setExchangeRate(vo.getExchangeRate());
					notpay.setCurrencyUuid(currencyId);
					notpay.setChangeable(false);
					nopayList.add(notpay);
    				pmp.setPayingAmount(nopayList);
    			}else{
    				//已付金额为空的话，未付的金额=成本的应付金额
    				List<MoneyAmountVO> notPayedMoneyList = new ArrayList<>();
    				MoneyAmountVO notPayedMoneyVo = pmp.getPayableAmount().get(0);
    				MoneyAmountVO newNotPayedMoneyVo = notPayedMoneyVo.clone();
    				newNotPayedMoneyVo.setChangeable(true);
    				notPayedMoneyList.add(newNotPayedMoneyVo);
    				pmp.setPayingAmount(notPayedMoneyList);
    			}
    			pmp.setReceiveCompany(ao.getNagentName()==null?"":ao.getNagentName());
    			pmp.setReceiveCompanyUuid(ao.getAgentinfoId() == null ? "" : ao.getAgentinfoId().toString());
    		}
    	}
    	return pmp;
    }
    
    @Override
	public PayedMoneyPojo getPayedMoneyInfoForPay(BaseInput4MT input){
		PayedMoneyPojo pmp = new PayedMoneyPojo();
		//付款款项类型  fundsType 4成本付款 3追加成本 2退款 1借款
		String fundsType = input.getParamValue("fundsType");
		String paymentUuid = input.getParamValue("paymentUuid");
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(StringUtils.isNotBlank(fundsType) && StringUtils.isNotBlank(paymentUuid)){
			if("4".equals(fundsType)){//成本de
				pmp = getCostNopayMoney(paymentUuid, fundsType, companyUuid);
			}else{//退款，借款，追加成本
				pmp = getNopayMoney(paymentUuid, fundsType, companyUuid);
			}
	    }
		return pmp;
	}
    
	@Override
	public PayedMoneyPojo getPayedMoneyInfo(BaseInput4MT input) {
		// TODO Auto-generated method stub
		PayedMoneyPojo pmp = new PayedMoneyPojo();
		String orderId = input.getParamValue("orderUuid");
		// 根据订单Id 获取订单的已付和应付 SerialNum
		AirticketOrder ao = airticketOrderDao.getAirticketOrderById(Long
				.valueOf(orderId));
		// 根据上面查出的SerialNum 获取具体的金额信息
		List<MoneyAmount> tm = moneyAmountDao.mergeAmountBySerialNum(ao
				.getTotalMoney());// 应付金额
		List<MoneyAmount> pm = moneyAmountDao.mergeAmountBySerialNum(ao
				.getPayedMoney());// 已付金额
		List<MoneyAmountVO> tmvoList = new ArrayList<MoneyAmountVO>();//
		List<MoneyAmountVO> pmvoList = new ArrayList<MoneyAmountVO>();// 未付金额列表

		if (CollectionUtils.isNotEmpty(tm)) {
			for (MoneyAmount m : tm) {
				// 应付金额
				MoneyAmountVO tmvo = new MoneyAmountVO();
				tmvo.setAmount(m.getAmount().doubleValue());
				tmvo.setCurrencyUuid(m.getCurrencyId());
				tmvoList.add(tmvo);
				if (CollectionUtils.isNotEmpty(pm)) {
					for (MoneyAmount ma : pm) {
						// 未付金额
						MoneyAmountVO pmvo = new MoneyAmountVO();
						if (m.getCurrencyId() == ma.getCurrencyId()) {
							pmvo.setCurrencyUuid(ma.getCurrencyId());
							pmvo.setAmount((m.getAmount().subtract(
									ma.getAmount()).doubleValue()));
						}
						pmvoList.add(pmvo);
					}
				}
			}
			pmp.setPayableAmount(tmvoList);
			pmp.setPayingAmount(pmvoList);
		}

		return pmp;
	}

	/**
	 * 付款记录保存接口
	 * 
	 * @Title: saveRefundInfo
	 * @return boolean
	 * @author majiancheng
	 * @date 2015-10-24 上午10:33:27
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean saveRefundInfo(BaseInput4MT input) {
		boolean flag = false;

		try {
			RefundJsonBean refundJsonBean = input.getParamObj(RefundJsonBean.class);
			//获取订单生成时的币种,因0094需要修改修改汇率，所以美途国际账号取页面传递过来的值 update by shijun.liu 2016.03.03
			boolean b = UserUtils.isMtourUser();
			BigDecimal exchangeRate = null;
			if(b){
				exchangeRate = new BigDecimal(refundJsonBean.getExchangeRate());
			}else{
				Currency currency = mtourOrderService.getOriginalCurrency(Long.parseLong(refundJsonBean.getOrderUuid()), 
						refundJsonBean.getCurrencyUuid().toString());
				exchangeRate = currency.getConvertLowest();
			}
			// 保存付款信息
			flag = refundService.saveRefundByRefundJsonBean(refundJsonBean, exchangeRate);

			//计算并更新付款状态-------------------------------------------------
			//airticketOrderMoneyAmount的款项类型（1、借款；2、退款；3、追加成本；）
			//refund的款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款）
			Integer fundsType = Integer.parseInt(refundJsonBean.getFundsType());
			Integer moneyType = null;
			if(fundsType != null) {
				switch(fundsType){
					case 1:moneyType = 4;break;//借款
					case 2:moneyType = 2;break;//退款
					case 3:moneyType = 6;break;//追加成本
					case 4:moneyType = 1;break;//成本
				}
				
				Integer payStatus = null;
				//根据cost_record表获取付款状态
				if(moneyType == 1) {
					/*//直接设置为已付款状态
					costRecordDao.updatePayStatus(Long.parseLong(refundJsonBean.getPaymentUuid()), CostRecord.PAY_STATUS_ALREADY);*/
					payStatus = buildPaystatusByCostRecordId(moneyType, Integer.parseInt(refundJsonBean.getPaymentUuid()));
					costRecordDao.updatePayStatus(Long.parseLong(refundJsonBean.getPaymentUuid()), payStatus);
					if(b){
						//美途国际付款时，如果修改过汇率则对应成本汇率修改
						CostRecord costRecord = costRecordDao.findOne(Long.parseLong(refundJsonBean.getPaymentUuid()));
						costRecord.setRate(exchangeRate);
						costRecord.setPriceAfter(exchangeRate.multiply(costRecord.getPrice())
								.multiply(new BigDecimal(costRecord.getQuantity())));
						costRecord.setPayStatus(payStatus);
						String uuid = costRecord.getUuid();
						AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice = airticketOrderPnrAirlinePriceService.getByCostRecordUuid(uuid);
						if(airticketOrderPnrAirlinePrice != null) {
							airticketOrderPnrAirlinePrice.setExchangerate(exchangeRate.doubleValue());
							airticketOrderPnrAirlinePriceService.update(airticketOrderPnrAirlinePrice);
						}
//						costRecordDao.saveObj(costRecord);
						costRecordDao.updateObj(costRecord);
					}
				} else {
					//根据airticket_order_moneyAmount表获取付款状态
					payStatus = buildPaystatusByMoneyAmountId(moneyType, Integer.parseInt(refundJsonBean.getPaymentUuid()));
					airticketOrderMoneyAmountDao.updatePaystatusByMoneyAmountId(payStatus, Integer.parseInt(refundJsonBean.getPaymentUuid()));
					if(b){
						//美途国际付款时，如果修改过汇率则对应借款，退款，追加成本的汇率也要修改
						AirticketOrderMoneyAmount aoma = airticketOrderMoneyAmountDao.getById(Integer.parseInt(refundJsonBean.getPaymentUuid()));
						aoma.setExchangerate(Double.valueOf(exchangeRate.toString()));
						aoma.setPaystatus(payStatus);
						airticketOrderMoneyAmountDao.saveObj(aoma);
					}
				}
			}
			
			//计算并更新付款状态-------------------------------------------------
			//----------更新订单的支付状态 add by majiancheng
			updateOrderRefundFlag(Long.parseLong(refundJsonBean.getOrderUuid()));
			//----------更新订单的支付状态
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return flag;
	}

	/**
	 * 根据订单金额表uuid和款项类型获取相应的款项明细；其中（moneyType为款项类型（借款：1，退款：2，追加成本：3））
	 * 
	 * @Title: getRefundTypeDetailInfo
	 * @return RefundTypeDetailJsonBean
	 * @author majiancheng
	 * @date 2015-10-24 下午4:00:42
	 */
	public RefundTypeDetailJsonBean getRefundTypeDetailInfo(String moneyAmountUuid, Integer moneyType) {
		RefundTypeDetailJsonBean jsonBean = new RefundTypeDetailJsonBean();

		AirticketOrderMoneyAmount moneyAmount = airticketOrderMoneyAmountDao.getById(Integer.parseInt(moneyAmountUuid));
		jsonBean.setFundsName(moneyAmount.getFundsName());
		jsonBean.setCurrencyUuid(moneyAmount.getCurrencyId());
		jsonBean.setAmount(moneyAmount.getAmount());

		AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(moneyAmount.getAirticketOrderId().longValue());
		// 签约渠道
		if (airticketOrder.getAgentinfoId() != null && airticketOrder.getAgentinfoId() != -1) {
			jsonBean.setChannelName(agentinfoDao.getAgentNameById(airticketOrder.getAgentinfoId()));
			// 非签约渠道
		} else {
			jsonBean.setChannelName(airticketOrder.getNagentName());
		}
		jsonBean.setApplicant(UserUtils.getUserNameById(moneyAmount.getCreateBy().longValue()));
		jsonBean.setMemo(moneyAmount.getMemo());

		List<AmountJsonBean> totalAmounts = new ArrayList<AmountJsonBean>();
		
		// 获取该订单下款项类型为：moneyType的金额信息
		List<AirticketOrderMoneyAmount> moneyTypeAmounts = airticketOrderMoneyAmountDao.getByOrderIdStatusAndMoneyType(moneyAmount.getAirticketOrderId(),
				AirticketOrderMoneyAmount.STATUS_CONFIRM, moneyType);
		if (CollectionUtils.isNotEmpty(moneyTypeAmounts)) {
			AmountJsonBean amount = new AmountJsonBean();
			BigDecimal RMBAmount = new BigDecimal(0);
			for (AirticketOrderMoneyAmount entity : moneyTypeAmounts) {
				Currency currency = currencyDao.findById(entity.getCurrencyId().longValue());
				if("人民币".equals(currency.getCurrencyName())) {
					amount.setCurrencyUuid(entity.getCurrencyId().toString());
				}
				//汇率转换
				RMBAmount = RMBAmount.add(new BigDecimal(entity.getAmount() * entity.getExchangerate()));
			}

			if(StringUtils.isEmpty(amount.getCurrencyUuid())) {
				List<Currency> currencys = currencyDao.getRMBCurrencyId(UserUtils.getUser().getCompany().getId());
				if(CollectionUtils.isNotEmpty(currencys)) {
					for (Currency currency : currencys){
						if("人民币".equals(currency.getCurrencyName())) {
							amount.setCurrencyUuid(currency.getId().toString());
						}
					}
				}
			}
			if(StringUtils.isEmpty(amount.getCurrencyUuid())) {
				//无人民币币种，应添加
				amount.setCurrencyUuid("-1");
			}
			amount.setAmount(RMBAmount.doubleValue());
			totalAmounts.add(amount);
		}
		
		jsonBean.setTotalAmounts(totalAmounts);
		return jsonBean;
	}

	@Override
	public Page getAccountAgeList(AccountAgeParam accountAgeParam) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		Map<String, Object> map = null;
		if(UserUtils.isMtourUser()){
			map = financeDao.getAccountAgeList(accountAgeParam, companyId);
		}else{
			map = financeDao.getAccountAgeListNotMtour(accountAgeParam, companyId);
		}
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("data");
		List<AccountAgeJsonResult> dataList = new ArrayList<AccountAgeJsonResult>();
		if(null != list && list.size() != 0){
			for (Map<String, Object> result: list) {
				AccountAgeJsonResult accountAge = new AccountAgeJsonResult();
				//渠道ID
				String channelUuid = String.valueOf(result.get("channelUuid")==null?"":result.get("channelUuid"));
				//渠道名称
				String channelName = String.valueOf(result.get("channelName")==null?"":result.get("channelName"));
				//跟进销售ID
				String saleId = String.valueOf(result.get("saleId")==null?"":result.get("saleId"));
				//跟进销售名称
				String salesName = String.valueOf(result.get("salesName")==null?"":result.get("salesName"));
				//某渠道下面生成的订单的总人数
				String travelerCount = String.valueOf(result.get("totalTravelerCount")==null?"":result.get("totalTravelerCount"));
				//币种ID
				//String currencyId = String.valueOf(result.get("currencyId")==null?"":result.get("currencyId"));
				String currencyId = "-1";	//默认-1，前端不做处理。原来是根据币种ID查询相应的币种符号，现(2015.11.20)以为全为人民币，此币种ID不需要
				//应收金额
				String totalMoney = String.valueOf(result.get("totalMoney")==null?"":result.get("totalMoney"));
				//已收金额
				String payedMoney = String.valueOf(result.get("payedMoney")==null?"":result.get("payedMoney"));
				//到帐金额
				String accountMoney = String.valueOf(result.get("accountedMoney")==null?"":result.get("accountedMoney"));
				//未收金额
				String notReceivedMoney = String.valueOf(result.get("notReceiviedMoney")==null?"":result.get("notReceiviedMoney"));
				List<MoneyAmountVO> receivableAmount = matcherCurrencyIdAndAmount(totalMoney,currencyId);
				List<MoneyAmountVO> receivedAmount = matcherCurrencyIdAndAmount(payedMoney,currencyId);
				List<MoneyAmountVO> arrivedAmount = matcherCurrencyIdAndAmount(accountMoney,currencyId);
				List<MoneyAmountVO> unreceiveAmount = matcherCurrencyIdAndAmount(notReceivedMoney,currencyId);
				accountAge.setChannelUuid(channelUuid);
				accountAge.setChannelName(channelName);
				accountAge.setSaleId(saleId);
				accountAge.setSalesName(salesName);
				accountAge.setTotalTravelerCount(travelerCount);
				accountAge.setTotalMoney(totalMoney);
				accountAge.setPayedMoney(payedMoney);
				accountAge.setAccountedMoney(accountMoney);
				accountAge.setNotReceiviedMoney(notReceivedMoney);
				accountAge.setReceivableAmount(receivableAmount);
				accountAge.setReceivedAmount(receivedAmount);
				accountAge.setArrivedAmount(arrivedAmount);
				accountAge.setUnreceiveAmount(unreceiveAmount);
				dataList.add(accountAge);
			}
		}
		Long count = (Long)map.get("count");
		Page page = new Page();
		//分页对象
		PageBean pageBean = new PageBean();
		pageBean.setTotalRowCount(count+"");
		pageBean.setCurrentIndex(accountAgeParam.getPageNow()+"");
		pageBean.setRowCount(accountAgeParam.getPageCount()+"");
		page.setResults(dataList);
		page.setPage(pageBean);
		return page;
	}

	/**
	 * 将金额和币种转换成数组对象
	 * @param money			金额
	 * @param currencyId	币种ID
	 * @return
	 * @author shijun.liu
	 */
	private List<MoneyAmountVO> matcherCurrencyIdAndAmount(String money, String currencyId) {
		List<MoneyAmountVO> list = new ArrayList<MoneyAmountVO>();
		MoneyAmountVO vo = new MoneyAmountVO();
		if(StringUtils.isBlank(money) || StringUtils.isBlank(currencyId)){
			return list;
		}
		vo.setCurrencyUuid(Integer.valueOf(currencyId));
		vo.setAmount(Double.valueOf(money));
		list.add(vo);
		return list;
	}

	public String getShowOrderListWhereSql(BaseInput4MT input){
		StringBuffer sb = new StringBuffer();
		JSONObject object  =JSON.parseObject(input.getParam());
		Map<String,String> map = (Map)object.get("searchParam");
		String searchType = map.get("searchType");
		String searchKey = map.get("searchKey");
		if(StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchKey)){
			if("1".equals(searchType)){//团号
				sb.append(" AND list.groupNo like '%").append(searchKey).append("%'");
			}else if("2".equals(searchType)){//订单编号
				sb.append(" AND list.orderNum like '%").append(searchKey).append("%'");
			}else if("3".equals(searchType)){//产品名称
				sb.append(" AND list.productName like '%").append(searchKey).append("%'");
			}else if("4".equals(searchType)){//渠道 1表示渠道，2表示批发商
				sb.append(" AND list.agentorsupplytype = 2  AND list.agentorsypplyname like '%").append(searchKey).append("%'");
			}else if("5".equals(searchType)){//下单人
				sb.append(" AND list.salerName like '%").append(searchKey).append("%'");
			}else if("6".equals(searchType)){//pnr
			}else if("7".equals(searchType)){//航空公司
				sb.append("");
			}else if("8".equals(searchType)){//航段名称
				sb.append("");
			}
			
			
		}
		Map m  =  (Map) object.get("filterParam");
		//收款状态
		if(null != m.get("receiveStatusCode") && StringUtils.isNotBlank(m.get("receiveStatusCode").toString())){
//			System.out.println(m.get("receiveStatusCode").toString());
			sb.append(" AND list.isAsAccount in (").append(m.get("receiveStatusCode").toString()).append(") ");
			/*if("99".equals(m.get("receiveStatusCode").toString())) {
				sb.append(" AND (list.isAsAccount = ").append(m.get("receiveStatusCode").toString()).append(" or list.isAsAccount is null)");
			}else if("0".equals(m.get("receiveStatusCode").toString())){
				sb.append(" AND (list.isAsAccount = ").append(m.get("receiveStatusCode").toString()).append(" or list.isAsAccount = 101)");
			}else if("2".equals(m.get("receiveStatusCode").toString())){
				sb.append(" AND (list.isAsAccount = ").append(m.get("receiveStatusCode").toString()).append(" or list.isAsAccount = 102)");
			} else {
				sb.append(" AND list.isAsAccount = ").append(m.get("receiveStatusCode").toString());
			}*/
		}
		//付款单位
		if(m.get("payer") != null && StringUtils.isNotBlank(m.get("payer").toString())){
			
			if(m.get("payer").toString().contains(",")){
				//多个付款单位查询条件，多个之间以逗号分隔
				String [] args = m.get("payer").toString().split(",");
				int n = args.length;
				StringBuffer con = new StringBuffer("(");
				for(int i=0;i<n;i++){
					if(i == n-1){
					   con.append("list.paymentCompany like '%").append(args[i]).append("%'").append(")");
					}else{
					   con.append("list.paymentCompany like '%").append(args[i]).append("%'").append(" or ");
					}
				}
				sb.append(" AND ").append(con.toString());
			}else{
			//一个付款单位查询条件
			sb.append(" AND list.paymentCompany like '%").append(m.get("payer").toString()).append("%'");
			}
		}
		//收款人
		if(StringUtils.isNotBlank(m.get("receiver").toString())){
			sb.append(" AND list.receiverId in( ").append(m.get("receiver").toString()).append(")");
		}
		//收款类别
		if(StringUtils.isNotBlank(m.get("receiveTypeCode").toString())){
			sb.append(" AND list.receiveType in (").append(m.get("receiveTypeCode").toString()).append(")");
		}
		//出团日期
		if(m.get("departureDate")!= null && StringUtils.isNotBlank(m.get("departureDate").toString())){
			String departureDate = m.get("departureDate").toString();
			int index = departureDate.indexOf("~");
			String[] str = departureDate.split("~");
			if(index<0){
				
			}else if(index==0){ //截止日期
				if(StringUtils.isNotBlank(str[1])){
					sb.append(" AND list.departureDate <='").append(str[1]).append(" 23:59:59'");
				}
			}else if(index>0){
				if(str.length>1){
                    if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND list.departureDate >='").append(str[0]).append(" 00:00:00'");
					}
					if(StringUtils.isNotBlank(str[1])){
						sb.append(" AND list.departureDate <='").append(str[1]).append(" 23:59:59'");
					}
				}else{
					if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND list.departureDate >='").append(str[0]).append(" 00:00:00'");
					}
				}
			}			
		}
		
		//收款日期
		if(m.get("receiveDate")!= null && StringUtils.isNotBlank(m.get("receiveDate").toString())){
			String departureDate = m.get("receiveDate").toString();
			int index = departureDate.indexOf("~");
			String[] str = departureDate.split("~");
			if(index<0){
				
			}else if(index==0){ //截止日期
				if(StringUtils.isNotBlank(str[1])){
					sb.append(" AND list.receiveDate <='").append(str[1]).append(" 23:59:59'");
				}
			}else if(index>0){
				if(str.length>1){
                    if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND list.receiveDate >='").append(str[0]).append(" 00:00:00'");
					}
					if(StringUtils.isNotBlank(str[1])){
						sb.append(" AND list.receiveDate <='").append(str[1]).append(" 23:59:59'");
					}
				}else{
					if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND list.receiveDate >='").append(str[0]).append(" 00:00:00'");
					}
				}
			}			
		}
		
		//到账日期
		if(m.get("arrivalBankDate")!= null && StringUtils.isNotBlank(m.get("arrivalBankDate").toString())){
			String departureDate = m.get("arrivalBankDate").toString();
			int index = departureDate.indexOf("~");
			String[] str = departureDate.split("~");
			if(index<0){
				
			}else if(index==0){ //截止日期
				if(StringUtils.isNotBlank(str[1])){
					sb.append(" AND list.arrivalBankDate <='").append(str[1]).append(" 23:59:59'");
				}
			}else if(index>0){
				if(str.length>1){
                    if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND list.arrivalBankDate >='").append(str[0]).append(" 00:00:00'");
					}
					if(StringUtils.isNotBlank(str[1])){
						sb.append(" AND list.arrivalBankDate <='").append(str[1]).append(" 23:59:59'");
					}
				}else{
					if(StringUtils.isNotBlank(str[0])){
						sb.append(" AND list.arrivalBankDate >='").append(str[0]).append(" 00:00:00'");
					}
				}
			}			
		}
		//渠道或地接社
				JSONArray array = (JSONArray) m.get("tourOperatorOrChannel");
			    if(array != null && array.size()>0){
			    	StringBuffer id = new StringBuffer();
			    	String type = "";
			    	int num = array.size();
			    	for(int i =0;i<num;i++){
			    		JSONObject o =  array.getJSONObject(i);
			    		type=(String) o.get("tourOperatorChannelCategoryCode");
			    		if(i==num-1){
			    		  id.append(o.get("tourOperatorOrChannelUuid"));
			    		}else{
			    		  id.append(o.get("tourOperatorOrChannelUuid")).append(",");
			    		}
			    		
			    	}
			    	if(StringUtils.isNotBlank(type) && StringUtils.isNotBlank(id.toString())){
			    		if("2".equals(type)){//2表示渠道，1表示地接社
			    			sb.append(" AND list.agentorsupplytype=").append(type).append(" AND list.agentorsupplyId in (").append(id).append(") ");
			    		}else{
			    			sb.append(" AND list.agentorsupplytype=").append(type).append(" AND list.agentorsupplyId in (").append(id).append(") ");
			    		}
			    	}
			    }
		
		//已收金额
		if(m.get("receivedAmount")!= null && StringUtils.isNotBlank(m.get("receivedAmount").toString())){
			String receivedAmount = m.get("receivedAmount").toString();
			String[] str = receivedAmount.split("~");
			String start = "";
			String end = "";
			if(str.length == 2){
				start = str[0];
				end = str[1];
			}else{
				start = str[0];
			}
			if(StringUtils.isNotBlank(start)){
				sb.append(" AND list.receivedAmount >=").append(start);
			}
			if(StringUtils.isNotBlank(end)){
				sb.append(" AND list.receivedAmount <=").append(end);
			}
		}
		String receiveTypeCode = map.get("receiveTypeCode");
		if(StringUtils.isNotBlank(receiveTypeCode)){
			sb.append(" AND list.receiveType in(").append(receiveTypeCode).append(") ");
		}
		return sb.toString();
	}

	@Override
	public com.trekiz.admin.modules.mtourfinance.json.Page showOrderList(
			BaseInput4MT input, Page page) {
		Long compayId = UserUtils.getUser().getCompany().getId();
		String orderby = genOrderBy(input);
		JSONObject jo = JSON.parseObject(input.getParam());
		Map<String,String> map = (Map)jo.get("searchParam");
		String searchType = map.get("searchType");
		String searchKey = map.get("searchKey");
		String wheresql = getShowOrderListWhereSql(input);

		StringBuffer start = new StringBuffer( "SELECT costrecordId, orderType, orderUuid, receiveUuid, arrivalBankDate, receiveDate, fundsName, orderNum, groupNo, ");
		             start.append("productName, departureDate, receiveType, paymentCompany, receiver,receiverId, salerId, salerName, agentorsupplytype, agentorsupplyId, ")
						.append("agentorsypplyname, amount, isAsAccount, currencyId, updateDate as modifiedDateTime, payedMoney, totalMoney, accountedmoney,receivedAmount,receiveFundsTypeCode,receiveFundsTypeName");
		StringBuffer str = new StringBuffer(" FROM ( SELECT cost.id costrecordId, p.product_type_id orderType, ao.id AS orderUuid, pay.id AS receiveUuid, pay.accountDate AS arrivalBankDate, pay.createDate AS receiveDate,");
		        str.append("cost. NAME AS fundsName, ao.order_no AS orderNum, p.group_code AS groupNo, p.activity_airticket_name AS productName, p.startingDate AS departureDate, ")
				.append("5 AS receiveType, pay.payerName AS paymentCompany, NULL salerId, NULL salerName, ( SELECT u. NAME FROM sys_user u WHERE u.id = pay.createBy ) AS receiver,")
				.append(" pay.createBy AS receiverId, cost.supplyType agentorsupplytype, cost.supplyId agentorsupplyId, cost.supplyName agentorsypplyname, ma.amount, pay.isAsAccount,")
				.append(" ma.currencyId, pay.updateDate, NULL AS payedMoney, ao.total_money AS totalMoney, ao.accounted_money AS accountedmoney, ma.amount AS receivedAmount,'2' receiveFundsTypeCode,'其他收入收款' receiveFundsTypeName FROM cost_record cost,")
				.append(" pay_group pay, money_amount ma, activity_airticket p,");  
				if("6".equals(searchType) && StringUtils.isNotBlank(searchKey)){
					str.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr pnr where a.id=pnr.airticket_order_id and pnr.code_type=0 and pnr.flight_pnr like '%").append(searchKey).append("%')) ao ");
				}else if("7".equals(searchType) && StringUtils.isNotBlank(searchKey)){
					str.append("(select * from airticket_order o  where o.id in(select a.id from airticket_order a ,airticket_order_pnr pnr ,sys_airline_info s where a.id=pnr.airticket_order_id and pnr.airline = s.airline_code and  s.airline_code like '%").append(searchKey).append("%')) ao ");
				}else if("8".equals(searchType) && StringUtils.isNotBlank(searchKey)){
					str.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr_airline aop where a.id=aop.airticket_order_id and aop.airline_name like '%").append(searchKey).append("%')) ao ");
				}else{
					str.append(" airticket_order ao ");
				}
				str.append(" WHERE pay.cost_record_id = cost.id AND pay.orderType = cost.orderType ")
				.append("AND pay.payPrice = ma.serialNum AND p.id = cost.activityId AND p.id = ao.airticket_id AND cost.delFlag = 0 AND cost.orderType = 7 AND cost.budgetType = 2 AND cost.payStatus<>2 AND p.proCompany=").append(compayId)
				.append(" UNION ")
				.append("SELECT null costrecordId,tmp.orderType, ")
				.append("tmp.id orderUuid, tb.id receiveUuid, tb.accountDate AS arrivalBankDate, tb.createDate AS receiveDate, NULL AS fundsName, tmp.orderNum, ")
		        .append("tmp.orderGroupCode groupNo, tmp.productname AS productName, tmp.startingDate AS departureDate, tb.payPriceType AS receiveType, ")
				.append("tb.payerName AS paymentCompany, tmp.create_by AS salerId, ( SELECT u. NAME FROM sys_user u WHERE u.id = tmp.create_by ) AS salerName, ")
				.append("( SELECT u. NAME FROM sys_user u WHERE u.id = tb.createBy ) AS receiver, tb.createBy AS receiverId, 2 agentorsupplytype, ")
				.append("tmp.agentinfo_id agentorsupplyId, tmp.agentName agentorsypplyname, NULL AS amount, tb.isAsAccount, tb.currencyId AS currencyId, tb.updateDate, ")
				.append("tb.moneySerialNum AS payedMoney, tmp.total_money AS totalMoney, tmp.accounted_money AS accountedmoney, tb.receivedAmount,'1' receiveFundsTypeCode,'订单收款' receiveFundsTypeName FROM ")
				.append("( SELECT 7 orderType, aa.startingDate, aa.activity_airticket_name productname, ao.accounted_money, ao.create_by, ")
				.append("ao.agentinfo_id, ao.id id, ao.order_no orderNum, aa.group_code orderGroupCode, ao.total_money, ao.nagentName agentName, aa.id airticketId FROM ");
				if("6".equals(searchType) && StringUtils.isNotBlank(searchKey)){
					str.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr pnr where a.id=pnr.airticket_order_id and pnr.code_type=0 and pnr.flight_pnr like '%").append(searchKey).append("%')) ao ");
				}else if("7".equals(searchType) && StringUtils.isNotBlank(searchKey)){
					str.append("(select * from airticket_order o  where o.id in(select a.id from airticket_order a ,airticket_order_pnr pnr ,sys_airline_info s where a.id=pnr.airticket_order_id and pnr.airline = s.airline_code and  s.airline_code like '%").append(searchKey).append("%')) ao ");
				}else if("8".equals(searchType) && StringUtils.isNotBlank(searchKey)){
					str.append("(select * from airticket_order o  where o.id in( select a.id from airticket_order a ,airticket_order_pnr_airline aop where a.id=aop.airticket_order_id and aop.airline_name like '%").append(searchKey).append("%')) ao ");
				}else{
					str.append(" airticket_order ao ");
				}
				str.append("LEFT JOIN agentinfo ai ON ao.agentinfo_id = ai.id LEFT JOIN sys_user su ON ao.create_by = su.id ")
				.append("LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id LEFT JOIN sys_user aasu ON aa.createBy = aasu.id  where ao.del_flag =0 AND aa.proCompany=")
				.append(compayId)
				.append(") tmp, ( SELECT op.accountDate, ")
				.append("op.createDate, op.updateDate, op.payerName, op.id, op.orderNum, op.ordertype, op.printTime, op.printFlag, op.isAsAccount, op.payPriceType, ")
				.append("op.createBy, moneySerialNum, ma.amount receivedAmount, ma.currencyId FROM orderpay op, money_amount ma WHERE op.moneySerialNum = ma.serialNum AND ")
				.append("op.orderNum IS NOT NULL AND op.delFlag = 0 AND op.ordertype = '7' ) tb WHERE tb.orderNum = tmp.orderNum ");

		StringBuffer end = new StringBuffer(") list where 1=?");
		end.append(wheresql).append(" order by ").append(orderby);
		Query query = airticketOrderMoneyAmountDao.createSqlQuery(
				start.append(str).append(end).toString(), "1")
		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		StringBuffer count = new StringBuffer("select count(1) ");
		//获取总条数
		Long totalNum = airticketOrderMoneyAmountDao.getCountBySQL(count.append(str).append(") list where 1=1").append(wheresql).toString());
		
		
		Map pagemap = (Map) jo.get("pageParam");
		PageBean pageBean = new PageBean();
		Object p = pagemap.get("currentIndex");//页码
		Object pagesize = pagemap.get("rowCount");//每页条数
		if(p != null && pagesize !=null){
			Integer ps = Integer.valueOf(pagesize.toString()); //每页条数
			Integer num = Integer.valueOf(p.toString()); //页码
			int pn = Integer.valueOf(totalNum.toString())/ps;//总条数/每页条数=总页数
			if(pn<num-1){
				//如果实际页码pn小于前端传过来的页码num,则取第一页的数据
				// 设置每页显示多少个，设置多大结果。
				query.setMaxResults(10);
				// 设置起点
				query.setFirstResult(0);
				pageBean.setCurrentIndex("1");
			}else{
				BigDecimal bd = new BigDecimal(ps).multiply(new BigDecimal(num).subtract(new BigDecimal(1)));//每页起始条数
				// 设置每页显示多少个，设置多大结果。
				query.setMaxResults(ps);
				// 设置起点
				query.setFirstResult(Integer.valueOf(bd.toString()));
				pageBean.setCurrentIndex(p.toString());
			}
		}else{
			// 设置每页显示多少个，设置多大结果。
			query.setMaxResults(10);
			// 设置起点
			query.setFirstResult(0);
			pageBean.setCurrentIndex("1");
		}
		List<Map<String, Object>> list = query.list();
		page.setResults(copyParam2OrderlistPojo(list));
		
		pageBean.setTotalRowCount(totalNum.toString());
		
		pageBean.setRowCount(pagesize.toString());
		page.setPage(pageBean);
		return page;
	}

	/**
	 * 组织收款列表返回参数对象
	 * @author zhaohaiming
	 * */
	private List<OrderlistPoJO> copyParam2OrderlistPojo(List list){
		List<OrderlistPoJO> orderList = new ArrayList<OrderlistPoJO>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (int index = 0; index < list.size(); index++) {
				Map<String, Object> tmp = (Map<String, Object>) list.get(index);
				OrderlistPoJO op = new OrderlistPoJO();
				op.setOrderUuid(tmp.get("orderUuid") != null ? tmp.get("orderUuid").toString() : "");
				op.setReceiveUuid(tmp.get("receiveUuid") != null ? tmp.get("receiveUuid").toString() : "");
				op.setReceiveDate(tmp.get("receiveDate") != null ? tmp.get("receiveDate").toString() : "");
				op.setArrivalBankDate(tmp.get("arrivalBankDate") != null ? tmp.get("arrivalBankDate").toString() : "");
				op.setFundsName(tmp.get("fundsName") != null ? tmp.get("fundsName").toString() : "");
				op.setOrderNum(tmp.get("orderNum") != null ? tmp.get("orderNum").toString() : "");
				op.setGroupNo(tmp.get("groupNo") != null ? tmp.get("groupNo").toString() : "");
				op.setProductName(tmp.get("productName") != null ? tmp.get("productName").toString() : "");
				op.setDepartureDate(tmp.get("departureDate") != null ? tmp.get("departureDate").toString() : "");
				op.setReceiveTypeCode(tmp.get("receiveType") != null ? tmp.get("receiveType").toString() : "");
				op.setTourOperatorOrChannelName(tmp.get("agentorsypplyname") != null ? tmp.get("agentorsypplyname").toString() : "");
				op.setPaymentCompany(tmp.get("paymentCompany") != null ? tmp.get("paymentCompany").toString() : "");
				op.setReceiver(tmp.get("receiver") != null ? tmp.get("receiver").toString() : "");
				op.setModifiedDateTime(tmp.get("updateDate")!=null?tmp.get("updateDate").toString():"");
				if(tmp.get("isAsAccount") != null && "101".equals(tmp.get("isAsAccount").toString())){
					op.setReceiveStatusCode("0");
				}else if(tmp.get("isAsAccount")==null){ //订单收款orderpay表isasaccount = null 表示待确认
					op.setReceiveStatusCode("99");
				}else if(tmp.get("isAsAccount") != null && "102".equals(tmp.get("isAsAccount").toString())){
					op.setReceiveStatusCode("2");//成本其他收入，pay_group表isasaccount=102表示已驳回
				}else{
					op.setReceiveStatusCode(tmp.get("isAsAccount") != null ? tmp.get("isAsAccount").toString() : "");
				}
				// 收款类别 1全款，2尾款，3定金，4追散 （1-4是订单收款类别）5成本其他收入收款
				String receiveType = tmp.get("receiveType") != null ? tmp.get("receiveType").toString() : "0";
				Integer rt = Integer.valueOf(receiveType);
				if(rt==1){
					op.setReceiveTypeName("定金");
				}else if(rt ==2){
					op.setReceiveTypeName("尾款");
				}else if(rt == 3){
					op.setReceiveTypeName("全款");
				}else if(rt ==4){
					op.setReceiveTypeName("追散");
				}else if(rt == 5){
					op.setReceiveTypeName("其他收入");
				}else{
					op.setReceiveTypeName("");
				}
				List<MoneyAmountVO> payMoneyList = new ArrayList<MoneyAmountVO>();
				List<MoneyAmountVO> arrivedAmount = new ArrayList<MoneyAmountVO>();
				List<MoneyAmountVO> totalMoney = new ArrayList<MoneyAmountVO>();
				//收款类型
				String receiveFundsTypeCode = tmp.get("receiveFundsTypeCode") != null ? tmp.get("receiveFundsTypeCode").toString() : "";
				String receiveFundsTypeName = tmp.get("receiveFundsTypeName") != null ? tmp.get("receiveFundsTypeName").toString() : "";
				ReceiveFundsTypeVO receiveFundsTypeVO = new ReceiveFundsTypeVO();
				receiveFundsTypeVO.setReceiveFundsTypeCode(receiveFundsTypeCode);
				receiveFundsTypeVO.setReceiveFundsTypeName(receiveFundsTypeName);
				op.setReceiveFundsType(receiveFundsTypeVO);
				// 应收金额serial
				String totalMoneySerial = tmp.get("totalMoney") != null ? tmp.get("totalMoney").toString() : "";
				// 达账金额serial
				String accountedmoneySerial = tmp.get("accountedmoney") != null ? tmp.get("accountedmoney").toString() : "";
				if (rt > 0 && rt < 5) {// 订单收款
					
					// 已收金额serial
					String payedMoneySerial = tmp.get("payedMoney") != null ? tmp.get("payedMoney").toString() : "";
					
					// 已收金额
					
					if (StringUtils.isNotBlank(payedMoneySerial)) {
						payMoneyList = getMoneyAmountVO(payedMoneySerial,false);
						op.setReceivedAmount(payMoneyList);
					}
					// 达账状态=1（已达账），已付金额=达账金额
					if (StringUtils.isNotBlank(op.getReceiveStatusCode()) && "1".equals(op.getReceiveStatusCode()) && StringUtils.isNotBlank(accountedmoneySerial)) {
//						arrivedAmount = getMoneyAmountVO(accountedmoneySerial);
						op.setArrivedAmount(payMoneyList);
					}
					
				} else {// 其他收入收款
						// 已收金额
					MoneyAmountVO ma = new MoneyAmountVO();
					if (tmp.get("amount") != null) {
						ma.setAmount(Double.valueOf(tmp.get("amount").toString()));
					}
					if (tmp.get("currencyId") != null) {
						ma.setCurrencyUuid(Integer.valueOf(tmp.get("currencyId").toString()));
					}

					payMoneyList.add(ma);
					// 达账金额
					String isAsAccount = tmp.get("isAsAccount") != null ? tmp.get("isAsAccount").toString() : "";
					if (StringUtils.isNotBlank(isAsAccount) && isAsAccount.equals("1")) {
						arrivedAmount.add(ma);
					}
					totalMoney.add(ma);
					op.setReceivedAmount(payMoneyList);
					op.setArrivedAmount(arrivedAmount);
					op.setOrderAmount(totalMoney);
//					String costId = tmp.get("costrecordId")!=null?tmp.get("costrecordId").toString():"";
//					if(StringUtils.isNotBlank(costId)){
//					  List<MoneyAmountVO> costlist = getTotalAccountMoneyForCost(costId);
//					  op.setTotalArrivedAmount(costlist);
//					} 
				}
				// 订单总额
				if (StringUtils.isNotBlank(totalMoneySerial)) {
					totalMoney = getMoneyAmountVO(totalMoneySerial,true);
					op.setOrderAmount(totalMoney);
				}
				// 累计达账金额
				if (StringUtils.isNotBlank(accountedmoneySerial)) {
					arrivedAmount = getMoneyAmountVO(accountedmoneySerial,true);
					op.setTotalArrivedAmount(arrivedAmount);
				}
				orderList.add(op);
			}
		}
		return orderList;
	}
	/**
	 * 获取成本其他收入达账总额
	 * @author zhaohaiming
	 * */
	public List<MoneyAmountVO> getTotalAccountMoneyForCost(String costrecordId){
		List<MoneyAmountVO> ma = new ArrayList<MoneyAmountVO>();
		String sql = "SELECT m.currencyId ,sum(m.amount) amount FROM pay_group pg,money_amount m where pg.payPrice=m.serialNum and pg.delFlag=0 and pg.isAsAccount=1 and pg.cost_record_id="+costrecordId +" group by currencyId";
		List<Map<String,Object>> list = moneyAmountDao.findBySql(sql, Map.class);
		if(CollectionUtils.isNotEmpty(list)){
			int num = list.size();
			for(int i=0;i<num;i++){
				Map<String,Object> map = list.get(i);
				MoneyAmountVO vo = new MoneyAmountVO();
				vo.setAmount(Double.valueOf(map.get("amount").toString()));
				vo.setCurrencyUuid(Integer.valueOf(map.get("currencyId").toString()));
				ma.add(vo);
			}
		}
		return ma;
	}
	/**
	 * 封装MoneyAmountVO信息
	 * @param serialNum 金额serialNum
	 * @param flag 是否需要转换成人民币 false是不需要,true需要
	 * 
	 * @author zhaohaiming
	 * */
	public List<MoneyAmountVO> getMoneyAmountVO(String serialNum,boolean flag) {
		List<MoneyAmountVO> maList = new ArrayList<MoneyAmountVO>();
		if (StringUtils.isNotBlank(serialNum)) {
			if(flag){
				MoneyAmountVO vo = moneyAmountDao.amountxexchangerate(serialNum);
				maList.add(vo);
			}else{
				List<MoneyAmount> list = moneyAmountDao.getAmountByUid(serialNum);
				if (CollectionUtils.isNotEmpty(list)) {
					int size = list.size();
					for (int i = 0; i < size; i++) {
						MoneyAmountVO vo = new MoneyAmountVO();
						MoneyAmount ma = list.get(i);
						vo.setAmount(ma.getAmount() != null ? Double.valueOf(ma
								.getAmount().toString()) : 0);
						vo.setCurrencyUuid(ma.getCurrencyId());
						maList.add(vo);
					}
				}
			}
		}
		return maList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryPayedAmount(String paymentUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT aom.currency_id AS currencyUuid,aom.amount AS amount FROM airticket_order_moneyAmount aom ");
		sql.append(" LEFT JOIN refund r ON aom.serialNum=r.money_serial_num ");
		sql.append(" WHERE aom.delFlag='0' AND aom.status=1 AND r.id=? ");

		return financeDao.findBySql(sql.toString(), Map.class, paymentUuid);
	}

	/**
	 * 根据机票订单Id获取航段改价
	 * 
	 * @param orderId
	 * @return list
	 * @author zhaohaiming
	 * @date 2015-10-20
	 * */
	public List<AirticketOrderChangePrice> getByAirticketOrderId(String orderId) {
		DetachedCriteria dc = airticketOrderChangePriceDao
				.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", "0"));
		dc.addOrder(Order.asc("computeType"));
		dc.add(Restrictions.eq("airticketOrderId", Integer.valueOf(orderId)));
		List<AirticketOrderChangePrice> list = airticketOrderChangePriceDao
				.find(dc);
		return list;
	}

	/**
	 * 根据机票订单Id获取航段价格
	 * 
	 * @return list
	 * @author zhaohaiming
	 * @date 2015-10-20
	 * */
	public List<AirticketOrderPnrAirlinePrice> getByAirticketOrderId(
			Integer airticketOrderId) {
		DetachedCriteria dc = airticketOrderPnrAirlinePriceDao
				.createDetachedCriteria();
		dc.add(Restrictions.eq("priceType", 1));
		dc.add(Restrictions.eq("delFlag", "0"));
		dc.add(Restrictions.eq("airticketOrderId", airticketOrderId));
		List<AirticketOrderPnrAirlinePrice> list = airticketOrderPnrAirlinePriceDao
				.find(dc);
		return list;
	}
	
	/**
	 * 根据款项类型美途机票快速订单金额表ID计算出付款状态
	 * @Title: buildPaystatusByMoneyAmountId
	 * @return int
	 * @author majiancheng
	 * @date 2015-11-2 下午9:05:09
	 */
	public int buildPaystatusByMoneyAmountId(Integer moneyType, Integer moneyAmountId) {
		Office company = UserUtils.getUser().getCompany();
		List<Refund> refunds = refundDao.findByRecordIdAndOrderType(moneyAmountId.longValue(), Context.ORDER_TYPE_JP, moneyType, company.getUuid());
		
		//1表示未全部支付、0表示全部支付、-1表示超出支付
		Integer result = null;
		//待付款
		if(CollectionUtils.isEmpty(refunds)) {
			return AirticketOrderMoneyAmount.PAYSTATUS_WAIT;
		}
		
		//(currencyId,currency_name,currency_mark,sum(amount),exchangerate)集合属性序列
		List<Object[]> moneyAmounts = airticketOrderMoneyAmountService.getMoneyAmountsByRefunds(refunds);
		
		AirticketOrderMoneyAmount airticketOrderMoneyAmount = airticketOrderMoneyAmountDao.getById(moneyAmountId);
		//所有待支付的金额信息
		BigDecimal allAmount = new BigDecimal(airticketOrderMoneyAmount.getAmount().toString());
		//已付金额信息
		BigDecimal payedAmount = new BigDecimal(0);

		//目前只考虑单币种操作
		if(CollectionUtils.isNotEmpty(moneyAmounts)) {
			for(Object[] objArr : moneyAmounts) {
				payedAmount = payedAmount.add(new BigDecimal(objArr[3].toString()));
			}
		}
		
		result = allAmount.compareTo(payedAmount);
		
		//1表示未全部支付、0表示全部支付、-1表示超出支付
		if(result == 0) {
			return AirticketOrderMoneyAmount.PAYSTATUS_CONFIRM;
		} else if(result <= -1) {
			return AirticketOrderMoneyAmount.PAYSTATUS_CONFIRM;
		} else {
			return AirticketOrderMoneyAmount.PAYSTATUS_WAIT;
		}
	}
	
	/**
	 * 根据其他收入id计算出付款状态
	 * @Title: buildPaystatusByCostRecordId
	 * @return int
	 * @author majiancheng
	 * @date 2015-11-4 下午4:26:07
	 */
	public int buildPaystatusByCostRecordId(Integer moneyType, Integer costRecordId) {
		Office company = UserUtils.getUser().getCompany();
		List<Refund> refunds = refundDao.findByRecordIdAndOrderType(costRecordId.longValue(), Context.ORDER_TYPE_JP, moneyType, company.getUuid());
		
		//1表示未全部支付、0表示全部支付、-1表示超出支付
		Integer result = null;
		//待付款
		if(CollectionUtils.isEmpty(refunds)) {
			return CostRecord.PAY_STATUS_SUBMIT;
		}
		
		//(currencyId,currency_name,currency_mark,sum(amount),exchangerate)集合属性序列
		List<Object[]> moneyAmounts = airticketOrderMoneyAmountService.getMoneyAmountsByRefunds(refunds);
		
		CostRecord costRecord = costRecordDao.getById(Long.valueOf(costRecordId.toString()));
		
		//所有待支付的金额信息
		BigDecimal allAmount = null;
		if(costRecord.getQuantity() != null && costRecord.getQuantity() != 0) {
			allAmount = costRecord.getPrice().multiply(new BigDecimal(costRecord.getQuantity()));
		} else {
			allAmount = costRecord.getPrice();
		}
		//已付金额信息
		BigDecimal payedAmount = new BigDecimal(0);

		//目前只考虑单币种操作
		if(CollectionUtils.isNotEmpty(moneyAmounts)) {
			for(Object[] objArr : moneyAmounts) {
				payedAmount = payedAmount.add(new BigDecimal(objArr[3].toString()));
			}
		}
		
		result = allAmount.compareTo(payedAmount);

		//1表示未全部支付、0表示全部支付、-1表示超出支付
		if(result == 0) {
			return CostRecord.PAY_STATUS_ALREADY;
		} else if(result <= -1) {
			return CostRecord.PAY_STATUS_ALREADY;
		} else {
			return CostRecord.PAY_STATUS_PENDING;
		}
		
	}
	
	/**
	 * 订单收款撤销接口（财务模块）
	 * @Title: cancelReceive
	 * @return boolean
	 * @author majiancheng
	 * @date 2015-11-4 下午5:27:18
	 */
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean cancelReceive(BaseInput4MT input) {
		boolean flag = true;
		
		String orderUuid = input.getParamValue("orderUuid");//orderUuid:'订单Uuid'
		String receiveUuid = input.getParamValue("receiveUuid");//receiveUuid:'收款Uuid'
		//款项类型 
		String fundsType = input.getParamValue("fundsType");
		
		//其他收入收款撤销
		if("2".equals(fundsType)) {
			PayGroup payGroup = payGroupDao.getById(Integer.parseInt(receiveUuid));
			//撤销其他收入收款操作
			payGroup.setIsAsAccount(99);
			payGroupService.update(payGroup);
			
			//成本录入（costRecord）收款撤销操作
			costRecordDao.confirmOrCancelPay(CostRecord.PAY_STATUS_SUBMIT, UserUtils.getUser().getId(), new Date(), payGroup.getCostRecordId().longValue());
			
		//订单收款撤销
		} else {
			//支付及moneyAmount撤销
			flag = orderPayService.cancelOrderReceive(Integer.parseInt(receiveUuid), Integer.parseInt(orderUuid), Context.ORDER_TYPE_JP);
			//更新机票订单状态
			mtourOrderService.updateAirticketOrderPaymentStatus(orderUuid);
		}
		
		return flag;
	}
	
	/**
	 * 其他收款详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOtherReceiptDetail(String costRecordId) {
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT cr.id AS otherRevenueUuid,cr.orderId AS orderUuid,pg.paymentStatus AS speedyClearance,cr.name AS fundsName,cr.rate AS exchangeRate,cr.price AS amount,");
		sql.append(" cr.currencyAfter AS convertedCurrencyUuid,cr.priceAfter AS convertedAmount,pg.payPriceType AS receiveTypeCode,cr.comment AS memo,pg.payVoucher AS docIds,");
		sql.append(" pg.payType AS paymentMethodCode,pg.payTypeName AS paymentMethodName,cr.currencyId AS currencyUuid,");
		sql.append("  CAST(cr.supplierType AS char) AS tourOperatorOrChannelTypeCode,CAST(cr.supplyType AS char) AS  tourOperatorChannelCategoryCode,cr.supplyId AS tourOperatorOrChannelUuid,cr.supplyName AS tourOperatorOrChannelName,");
		sql.append(" pg.payerName AS payer,pg.checkNumber AS checkNo,pg.invoiceDate AS checkIssueDate,pg.bankName AS paymentBank,pg.bankAccount AS paymentAccount,pg.toBankNname AS receiveBank,pg.toBankAccount AS receiveAccount");
		sql.append(" FROM  pay_group pg  LEFT JOIN  cost_record cr ON pg.cost_record_id = cr.id WHERE pg.id = ?");
		Map<String, Object> result = (Map<String, Object>)costRecordDao.getSession().createSQLQuery(sql.toString()).setParameter(0, costRecordId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
		return result;
	}

	public List<Map<String, String>> getOrderPayDocList(String docIds) {
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT d.id AS attachmentUuid,d.docName AS fileName,d.docPath AS attachmentUrl");
		sql.append(" FROM  docinfo d WHERE d.id IN ("+docIds+")");
		return docInfoDao.findBySql(sql.toString(), Map.class);
	}
	
	/**
	 * 根据 表明和记录id从serial_number查询流水号 
	 * @param tableName 表名
	 * @param recordid 表中记录id
	 * @author xinwei.wang@quauq.com
	 * @date 2015年11月11日13:48:55
	 * @return string ,如查询不到流水号返回 ""
	 */
	@Override
	public String getSerialNumByTableNameAndRecordId(String tableName,Integer recordid){
		StringBuffer sql = new  StringBuffer();
		sql.append("SELECT sn.code from serial_number sn WHERE sn.tableName = ");
		sql.append("'"+tableName+"'");
		sql.append(" and sn.recordId =");
		sql.append(recordid.toString());
//		System.out.println("--" + sql.toString());
		List<Map<String, Object>> serialNumList = costRecordDao.findBySql(sql.toString(), Map.class);
		String serialNum = "";
		if (null!=serialNumList&&serialNumList.size()>0) {
			serialNum =serialNumList.get(0).get("code").toString();
		}
		return serialNum;
	}

	@Override
	public List<Map<String, Object>> getAirlineNames(Integer orderId, String invoiceOriginalUuid) {
		String sql = "SELECT uuid airlineUuid, airline_name airlineName from airticket_order_pnr_airline where airticket_order_id = ? and airticket_order_pnr_uuid = ? and delFlag = 0";
		return airticketOrderPnrAirlinePriceDao.findBySql(sql, Map.class, orderId, invoiceOriginalUuid);
	}

	@Override
	public void lockSettlement(Long orderId) throws RuntimeException{
		try {
			financeDao.lockSettlement(orderId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[ " + orderId + " ]结算单锁定失败");
		}
	}
	
	@Override
	public void unlockSettlement(Long orderId) throws RuntimeException{
		try {
			financeDao.unlockSettlement(orderId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[ " + orderId + " ]结算单解锁失败");
		}
	}

	/**
	 *展开子列表的查询接口 
	 * @author chao.zhang
	 * @param orderUuid 订单id
	 * fundsType:款项类型：借款：1，退款：2，追加成本：3，成本：4
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getPayChildrenList(String orderUuid) {  
		StringBuffer sbf=new StringBuffer();
		List<Map<String,Object>> cra = new ArrayList<Map<String,Object>>();
		List<AirticketOrderMoneyAmount> list = airticketOrderMoneyAmountDao.getByOrderId(Integer.parseInt(orderUuid));
		sbf.append("SELECT cr.orderId as orderUuid,cr.supplyId as paymentObjectUuid,cr.supplyName as paymentObjectName,1 as tourOperatorChannelCategoryCode  " +
				"from cost_record cr where cr.orderId=? and cr.orderType=7 and cr.delFlag=0 and cr.payStatus!=4 and cr.payStatus!=2 AND cr.budgetType=1");
		sbf.append(" union ");	
		sbf.append("SELECT ao.id as orderUuid,case when ao.agentinfo_id=-1 then - 1 ELSE ao.agentinfo_id END as paymentObjectUuid,ao.nagentName as paymentObjectName," +
				"2 as tourOperatorChannelCategoryCode from airticket_order ao left join airticket_order_moneyAmount ma " +
				"on ao.id=ma.airticket_order_id where ao.id=? and ao.del_flag=0 and ma.amount is not null and ma.status=1");
		cra=airticketOrderDao.findBySql(sbf.toString(),Map.class,Integer.parseInt(orderUuid),Integer.parseInt(orderUuid));	
		//封装--应收金额
		List<Map<String,Object>> payableAmount=new ArrayList<Map<String,Object>>();
		StringBuffer ss=new StringBuffer();
		ss.append("SELECT -2 as supplyId,currency_id as currencyUuid,SUM(amount) AS amount,1 as objCode FROM airticket_order_moneyAmount  WHERE airticket_order_id=? and delFlag=0 and status=1 ");
		ss.append("group by currency_id");
		ss.append(" union ");
		ss.append("SELECT" +
				" supplyId,currencyId as currencyUuid,SUM(price*quantity) as amount,2 as objCode FROM cost_record where orderId=? and orderType=7 and delFlag=0 and payStatus!=2 and payStatus!=4 ")
			.append("group by currencyId,supplyId");
		payableAmount=costRecordDao.findBySql(ss.toString(),Map.class, Integer.parseInt(orderUuid),Integer.parseInt(orderUuid));
		for(Map<String,Object> m:cra){
			//List<Map<String,Object>> gt=new ArrayList<Map<String,Object>>();
			//List<Map<String,Object>> lt=new ArrayList<Map<String,Object>>();
			String amount="";
			for(Map<String,Object> pay:payableAmount){
				if(pay.get("objCode").toString().equals("2") && m.get("tourOperatorChannelCategoryCode").toString().equals("1")){
					if(pay.get("supplyId").toString().equals(m.get("paymentObjectUuid").toString())){
							Currency currency = currencyDao.findById(Long.parseLong(pay.get("currencyUuid").toString()));
							BigDecimal bg=new BigDecimal(pay.get("amount").toString());
							if("".equals(amount)){
								amount=currency.getCurrencyMark()+bg;
							}else{
								amount=amount+"+"+currency.getCurrencyMark()+bg;
							}
							m.put("payableAmount", amount);
					}
				}	
					if(pay.get("objCode").toString().equals("1") && m.get("tourOperatorChannelCategoryCode").toString().equals("2")){
						//gt.add(pay);
						Currency currency = currencyDao.findById(Long.parseLong(pay.get("currencyUuid").toString()));
						BigDecimal bg=new BigDecimal(pay.get("amount").toString());
						if("".equals(amount)){
							amount=currency.getCurrencyMark()+bg;
						}else{
							amount=amount+"+"+currency.getCurrencyMark()+bg;
						}
						m.put("payableAmount", amount);
					}
			}
		}
		//封装--已付金额
	for(Map<String,Object> m: cra){
			StringBuffer sb=new StringBuffer();
			if(m.get("tourOperatorChannelCategoryCode").toString().equals("1")){
				sb.append("select moneyAmount.currency_id AS currencyId,SUM(moneyAmount.amount) as amount,cr.supplyId From refund refund ")
					.append("LEFT JOIN cost_record cr on cr.id = refund.record_id ")
					.append("LEFT JOIN airticket_order_moneyAmount moneyAmount ON moneyAmount.serialNum=refund.money_serial_num ")
					.append("WHERE cr.orderId=? ")
					.append("AND refund.status is null AND refund.moneyType=1 AND refund.orderType=7 AND refund.del_flag=0 ")
					.append("GROUP BY moneyAmount.currency_id,supplyId ");
			}else if(m.get("tourOperatorChannelCategoryCode").toString().equals("2")){
				sb.append("SELECT moneyAmount.currency_id AS currencyId,SUM(moneyAmount.amount) AS amount,case when ao.agentinfo_id=-1 then - 1 ELSE ao.agentinfo_id END as supplyId " +
						"FROM refund refund "+
								"LEFT JOIN airticket_order_moneyAmount moneyAmount ON moneyAmount.serialNum = refund.money_serial_num "+
								"left join airticket_order_moneyAmount money on refund.record_id= money.id "+
								"left join airticket_order ao on ao.id=money.airticket_order_id "+
								"WHERE money.airticket_order_id =? "+
								"AND refund.status is null AND (refund.moneyType=2 or refund.moneyType=6 or refund.moneyType=4) AND refund.orderType=7 AND refund.del_flag=0 "+
								"GROUP BY moneyAmount.currency_id");
			}	
			List<Map<String,Object>> paidAmount = new ArrayList<Map<String,Object>>();
			paidAmount=costRecordDao.findBySql(sb.toString(), Map.class,Integer.parseInt(orderUuid));
			String pAmount="";
			for(Map<String,Object> pai:paidAmount){
				if(m.get("paymentObjectUuid").toString().equals(pai.get("supplyId").toString())){
					Currency currency = currencyDao.findById(Long.parseLong(pai.get("currencyId").toString()));
					//System.out.println(currency.getCurrencyMark());
					String am= pai.get("amount").toString();
					BigDecimal bg=new BigDecimal(am);
					if("".equals(pAmount)){
						pAmount=currency.getCurrencyMark()+bg;
					}else{
						pAmount=pAmount+"+"+currency.getCurrencyMark()+bg;
					}
				}
				
			}
			m.put("paidAmount", pAmount);
		}
	//付款确认----
/*	List<CostRecord> costRecords = costRecordDao.getByOrderIdAndOrderType(Long.parseLong(orderUuid), Context.ORDER_TYPE_JP);
	//获取待付款的借款、退款和追加成本记录
	List<AirticketOrderMoneyAmount> moneyAmounts = airticketOrderMoneyAmountDao.getByOrderIdAndStatus(Integer.parseInt(orderUuid), AirticketOrderMoneyAmount.STATUS_CONFIRM, AirticketOrderMoneyAmount.PAYSTATUS_WAIT);
	for(Map<String,Object> m:cra){
		if(m.get("tourOperatorChannelCategoryCode").toString().equals("1")){
			if(CollectionUtils.isNotEmpty(costRecords)) {
				m.put("paymentStatusCode", 0);
				m.put("paymentStatusName", "待付款");
			}else{
				m.put("paymentStatusCode", 1);
				m.put("paymentStatusName", "已付款");
			}
		}else{
			if(CollectionUtils.isNotEmpty(moneyAmounts)) {
				m.put("paymentStatusCode", 0);
				m.put("paymentStatusName", "待付款");
			}else{
					m.put("paymentStatusCode", 1);
					m.put("paymentStatusName", "已付款");
			}
		}
	}*/
	for(Map<String,Object> m:cra){
		StringBuffer str=new StringBuffer();
		List<Map<String,Object>> payStatus=new ArrayList<Map<String,Object>>();
		if(m.get("tourOperatorChannelCategoryCode").toString().equals("1")){
			str.append("SELECT payStatus from cost_record where supplyId=? and  orderType=7 and delFlag=0 and payStatus!=4 and payStatus!=2 AND budgetType=1 AND orderId=? ");
			payStatus = costRecordDao.findBySql(str.toString(), Map.class,Integer.parseInt(m.get("paymentObjectUuid").toString()),Integer.parseInt(orderUuid));
		}
		if(m.get("tourOperatorChannelCategoryCode").toString().equals("2")){
			str.append("SELECT ma.payStatus from airticket_order_moneyAmount ma left join airticket_order ao on ma.airticket_order_id= ao.id where ao.agentinfo_id=? and ao.id=?" +
					" and ao.del_flag=0 and ma.amount is not null and ma.status=1 ");
			payStatus=airticketOrderMoneyAmountDao.findBySql(str.toString(), Map.class,Integer.parseInt(m.get("paymentObjectUuid").toString()),Integer.parseInt(orderUuid));
		}
		boolean ps=true;
		for(Map<String,Object> paystatus:payStatus){
			if(!paystatus.get("payStatus").toString().equals("1")){
				ps=false;
			}
		}
		if(ps){
			m.put("paymentStatusCode", 1);
			m.put("paymentStatusName", "已付款");
		}else{
			m.put("paymentStatusCode", 0);
			m.put("paymentStatusName", "待付款");
		}
	}
		return cra;
	}

	
	/**
	 * 批量保存付款信息接口
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean batchSaveRefundInfo(BaseInput4MT input) {
		boolean flag = false;
		try {
			RefundJsonBean refundJsonBean = input.getParamObj(RefundJsonBean.class);

			// 批量保存付款信息
			flag = refundService.batchSaveRefundByRefundJsonBean(refundJsonBean);

			//计算并更新付款状态-------------------------------------------------
			if(CollectionUtils.isNotEmpty(refundJsonBean.getPayments())) {
				for(PaymentJsonBean payment : refundJsonBean.getPayments()) {
					//前端展示款项类型：1、成本；2、退款,3、借款,4、追加成本
					//refund的款项类型（1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款；5：退签证押金；6：追加成本付款;7：新审核成本录入付款；8：新审核退款付款；9：新审核返佣付款；10：新审核借款付款；11：新审核退签证押金；12：新审核追加成本付款；13：批量借款）
					//airticketOrderMoneyAmount的款项类型（1、借款；2、退款；3、追加成本；）
					Integer fundsType = Integer.parseInt(payment.getFundsType());
					Integer moneyType = null;
					if(fundsType != null) {
						switch(fundsType){
							case 1:moneyType = 4;break;//借款
							case 2:moneyType = 2;break;//退款
							case 3:moneyType = 6;break;//追加成本
							case 4:moneyType = 1;break;//成本
						}
						
						Integer payStatus = null;
						//根据cost_record表获取付款状态
						if(moneyType == 1) {
							/*//直接设置为已付款状态
							costRecordDao.updatePayStatus(Long.parseLong(refundJsonBean.getPaymentUuid()), CostRecord.PAY_STATUS_ALREADY);*/
							
							payStatus = buildPaystatusByCostRecordId(moneyType, Integer.parseInt(payment.getPaymentUuid()));
							costRecordDao.updatePayStatus(Long.parseLong(payment.getPaymentUuid()), payStatus);
							if(UserUtils.isMtourUser()){
								//美途国际付款时，如果修改过汇率则对应成本汇率修改
								CostRecord costRecord = costRecordDao.findOne(Long.parseLong(payment.getPaymentUuid()));
								costRecord.setRate(payment.getExchangeRate());
								costRecord.setPriceAfter(payment.getExchangeRate().multiply(costRecord.getPrice())
										.multiply(new BigDecimal(costRecord.getQuantity())));
								costRecord.setPayStatus(payStatus);
								String uuid = costRecord.getUuid();
//								AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice = airticketOrderPnrAirlinePriceService.getByUuid(uuid);
								AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice = airticketOrderPnrAirlinePriceService.getByCostRecordUuid(uuid);
								if(airticketOrderPnrAirlinePrice != null) {
									airticketOrderPnrAirlinePrice.setExchangerate(payment.getExchangeRate().doubleValue());
									airticketOrderPnrAirlinePriceService.update(airticketOrderPnrAirlinePrice);
								}
//								costRecordDao.saveObj(costRecord);
								costRecordDao.updateObj(costRecord);
							}
						} else {
							//根据airticket_order_moneyAmount表获取付款状态
							payStatus = buildPaystatusByMoneyAmountId(moneyType, Integer.parseInt(payment.getPaymentUuid()));
							airticketOrderMoneyAmountDao.updatePaystatusByMoneyAmountId(payStatus, Integer.parseInt(payment.getPaymentUuid()));
							if(UserUtils.isMtourUser()){
								//美途国际付款时，如果修改过汇率则对应成本汇率修改
								AirticketOrderMoneyAmount aoma = airticketOrderMoneyAmountDao.getById(Integer.parseInt(payment.getPaymentUuid()));
								aoma.setPaystatus(payStatus);
								aoma.setExchangerate(Double.valueOf(payment.getExchangeRate().toString()));
								airticketOrderMoneyAmountDao.saveObj(aoma);
							}
						} 
					}
				}
			}
			//计算并更新付款状态-------------------------------------------------
			
			//----------更新订单的支付状态 add by majiancheng
			updateOrderRefundFlag(Long.parseLong(refundJsonBean.getOrderUuid()));
			//----------更新订单的支付状态
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return flag;
	}
	
	/**
	 * 根据订单uuid和付款对象id获取
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return RefundRecordsJsonBean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-21
	 */
	public RefundRecordsJsonBean getBatchRefundInfo(BaseInput4MT input) {
		//批量付款信息
		RefundRecordsJsonBean refundRecordsJsonBean = new RefundRecordsJsonBean();
		List<RefundRecordJsonBean> refundRecordJsonBeans = new ArrayList<RefundRecordJsonBean>();
		
		String orderId = input.getParamValue("orderUuid");//'订单Uuid'
		String paymentObjectUuid = input.getParamValue("paymentObjectUuid");//'付款对象Uuid'
		String tourOperatorChannelCategoryCode = input.getParamValue("tourOperatorChannelCategoryCode");//'地接社/渠道商' 1、地接社；2、渠道商
		
		AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
		refundRecordsJsonBean.setGroupNo(airticketOrder.getGroupCode());
		
		//拼装付款总额
		Map<String, BigDecimal> paymentTotalAmount = new HashMap<String, BigDecimal>();
		
		//当为地接社时
		if("1".equals(tourOperatorChannelCategoryCode)) {
			List<CostRecord> costRecords = costRecordDao.getByOrderIdAndSupplyId(Long.parseLong(orderId), Context.ORDER_TYPE_JP, Integer.parseInt(paymentObjectUuid));
			if(CollectionUtils.isNotEmpty(costRecords)) {
				for(CostRecord costRecord : costRecords) {
					//设置付款对象id和付款对象名称
					if(refundRecordsJsonBean.getPaymentObjectUuid() == null) {
						refundRecordsJsonBean.setPaymentObjectUuid(costRecord.getSupplyId());
						refundRecordsJsonBean.setPaymentObjectName(costRecord.getSupplyName());
					}
					
					RefundRecordJsonBean refundRecord = new RefundRecordJsonBean();
					refundRecord.setPaymentUuid(costRecord.getId());
					refundRecord.setApprovalDate(costRecord.getCreateDate());
					refundRecord.setFundsType("4");
					refundRecord.setChangeabled(false);
					refundRecord.setExchangeRate(costRecord.getRate());
					refundRecord.setApplicant(costRecord.getCreateBy().getName());
					
					//应付金额
					AmountJsonBean payableAmount = new AmountJsonBean();
					payableAmount.setCurrencyUuid(String.valueOf(costRecord.getCurrencyId()));
					BigDecimal amountBig = costRecord.getPrice().multiply(new BigDecimal(costRecord.getQuantity()));
					payableAmount.setAmount(amountBig.doubleValue());
					refundRecord.setPayableAmount(payableAmount);
					
					//为应付金额做同币种想加
					if(paymentTotalAmount.containsKey(String.valueOf(costRecord.getCurrencyId()))) {
						BigDecimal currAmount = paymentTotalAmount.get(String.valueOf(costRecord.getCurrencyId()));
						paymentTotalAmount.put(String.valueOf(costRecord.getCurrencyId()), currAmount.add(amountBig));
					} else {
						paymentTotalAmount.put(String.valueOf(costRecord.getCurrencyId()), amountBig);
					}
					
					//已付金额
					List<AmountJsonBean> paidAmounts = new ArrayList<AmountJsonBean>();//已付金额
					List<Map<String, Object>> amounts = refundDao.getRefundsByRecordIdAndMoneyType(costRecord.getId(), Refund.MONEY_TYPE_COST);
					if(CollectionUtils.isNotEmpty(amounts)) {
						for(Map<String, Object> amount : amounts) {
							AmountJsonBean paidAmount = new AmountJsonBean();
							paidAmount.setCurrencyUuid(String.valueOf(amount.get("currencyId")));
							paidAmount.setAmount(((BigDecimal)amount.get("amount")).doubleValue());
							paidAmounts.add(paidAmount);
						}
						refundRecord.setPaidAmount(paidAmounts.get(0));
					}else if(UserUtils.isMtourUser()){
						//0094 如果是第一次付款 美途国际可以修改汇率 update by shijun.liu
						refundRecord.setChangeabled(true);
					}
					refundRecordJsonBeans.add(refundRecord);
				}
			}
		//当为渠道商时
		} else if("2".equals(tourOperatorChannelCategoryCode)) {
			refundRecordsJsonBean.setPaymentObjectUuid(airticketOrder.getAgentinfoId().intValue());
			refundRecordsJsonBean.setPaymentObjectName(airticketOrder.getNagentName());
			//付款记录集合
			List<AirticketOrderMoneyAmount> moneyAmountInfos = airticketOrderMoneyAmountDao.getByOrderId(Integer.parseInt(orderId));
			if(CollectionUtils.isNotEmpty(moneyAmountInfos)) {
				for(AirticketOrderMoneyAmount amount : moneyAmountInfos) {
					RefundRecordJsonBean refundRecord = new RefundRecordJsonBean();
					refundRecord.setPaymentUuid(amount.getId().longValue());
					refundRecord.setApprovalDate(amount.getCreateDate());
					//前端展示款项类型：借款:1,退款:2,追加成本:3,成本:4
					//数据库款项类型：款项类型（1、借款，2、退款，3、追加成本）
					Integer moneyType = null;//付款表refund中的款项类型
					if(1 == amount.getMoneyType()) {
						refundRecord.setFundsType("1");
						moneyType = Refund.MONEY_TYPE_BORROW;
					} else if(2 == amount.getMoneyType()) {
						refundRecord.setFundsType("2");
						moneyType = Refund.MONEY_TYPE_RETURNMONEY;
					} else if(3 == amount.getMoneyType()) {
						refundRecord.setFundsType("3");
						moneyType = Refund.MONEY_TYPE_ADDCOST;
					}
					
					refundRecord.setChangeabled(false);
					refundRecord.setExchangeRate(new BigDecimal(amount.getExchangerate()));
					refundRecord.setApplicant(userDao.getById(amount.getCreateBy().longValue()).getName());
					
					//应付金额
					AmountJsonBean payableAmount = new AmountJsonBean();
					payableAmount.setCurrencyUuid(String.valueOf(amount.getCurrencyId()));
					payableAmount.setAmount(amount.getAmount());
					refundRecord.setPayableAmount(payableAmount);

					//为应付金额做同币种想加
					BigDecimal amountBig = new BigDecimal(amount.getAmount());
					if(paymentTotalAmount.containsKey(String.valueOf(amount.getCurrencyId()))) {
						BigDecimal currAmount = paymentTotalAmount.get(String.valueOf(amount.getCurrencyId()));
						paymentTotalAmount.put(String.valueOf(amount.getCurrencyId()), currAmount.add(amountBig));
					} else {
						paymentTotalAmount.put(String.valueOf(amount.getCurrencyId()), amountBig);
					}
					
					//已付金额
					List<AmountJsonBean> paidAmounts = new ArrayList<AmountJsonBean>();//已付金额
					List<Map<String, Object>> amounts = refundDao.getRefundsByRecordIdAndMoneyType(amount.getId().longValue(), moneyType);
					if(CollectionUtils.isNotEmpty(amounts)) {
						for(Map<String, Object> amountInfo : amounts) {
							AmountJsonBean paidAmount = new AmountJsonBean();
							paidAmount.setCurrencyUuid(String.valueOf(amountInfo.get("currencyId")));
							paidAmount.setAmount(((BigDecimal)amountInfo.get("amount")).doubleValue());
							paidAmounts.add(paidAmount);
						}
						refundRecord.setPaidAmount(paidAmounts.get(0));
					}else if(UserUtils.isMtourUser()){
						//0094 如果是第一次付款 美途国际可以修改汇率 update by shijun.liu
						refundRecord.setChangeabled(true);
					}
					refundRecordJsonBeans.add(refundRecord);
				}
			}
		}
		
		//'付款总额'
		StringBuffer paymentTotalAmountStr = new StringBuffer();
		Set<String> totalAmountSet = paymentTotalAmount.keySet();
		if(CollectionUtils.isNotEmpty(totalAmountSet)) {
			for(String currencyId : totalAmountSet) {
				paymentTotalAmountStr.append(currencyDao.getById(Long.parseLong(currencyId)).getCurrencyMark());
				paymentTotalAmountStr.append(paymentTotalAmount.get(currencyId));
				paymentTotalAmountStr.append("+");
			}
			paymentTotalAmountStr.deleteCharAt(paymentTotalAmountStr.length()-1);
			
			refundRecordsJsonBean.setPaymentTotalAmount(paymentTotalAmountStr.toString());
		}
		refundRecordsJsonBean.setResults(refundRecordJsonBeans);
		return refundRecordsJsonBean;
	}
	
	/**
	 * 批量撤销付款记录
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-22
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean batchUndoRefundPayInfo(BaseInput4MT input) {
		String paymentObj_paymentUuid = input.getParamValue("paymentObj_paymentUuid");//按付款对象付款的付款Uuid
		String orderUuid = input.getParamValue("orderUuid");//订单id
		
		List<Refund> refunds = refundDao.getByBatchNumber(paymentObj_paymentUuid);
		if(CollectionUtils.isNotEmpty(refunds)) {
			for(Refund refund : refunds) {
				if(!undoRefundPayInfo(refund)) {
					return false;
				}
			}
		}
		
		//----更新订单的付款状态 add by majiancheng
		this.updateOrderRefundFlag(Long.parseLong(orderUuid));
		//----更新订单的付款状态 add by majiancheng
		
		return true;
	}
	
	/**
	 * 根据付款信息撤销付款记录
	 * @Description: 
	 * @param @param refund
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-22
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean undoRefundPayInfo(Refund refund) {
		boolean flag = false;
		Long userId = UserUtils.getUser().getId();
		refund.setStatus("0");
		refundDao.updateObj(refund);

		if (StringUtils.isNotEmpty(refund.getMoneySerialNum())) {
			List<AirticketOrderMoneyAmount> moneyAmounts = airticketOrderMoneyAmountDao.getBySerialNum(refund.getMoneySerialNum());
			if (CollectionUtils.isNotEmpty(moneyAmounts)) {
				for (AirticketOrderMoneyAmount moneyAmount : moneyAmounts) {
					// 将金额状态设置为撤销
					moneyAmount.setStatus(0);
					moneyAmount.setUpdateBy(userId.intValue());
					moneyAmount.setUpdateDate(new Date());
				}
				airticketOrderMoneyAmountDao.batchUpdate(moneyAmounts);
			}
		}
		
		//计算并更新付款状态-------------------------------------------------
		//refund的款项类型(1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款)
		//airticket_order_moneyAmount的款项类型（1：借款，2：退款，3：追加成本）
		Integer moneyType = refund.getMoneyType();
		Integer payStatus = null;
		
		//根据cost_record表获取付款状态
		if(moneyType == 1) {
			//撤消后设置已提交状态
			/*costRecordDao.updatePayStatus(refund.getRecordId(), CostRecord.PAY_STATUS_SUBMIT);*/
			
			payStatus = buildPaystatusByCostRecordId(moneyType, refund.getRecordId().intValue());
			costRecordDao.updatePayStatus(refund.getRecordId(), payStatus);
			// 以下代码无用，暂时注释掉， update by shijun.liu 2016.03.16
			/**if(UserUtils.isMtourUser()){
				//财务撤销成本的同时会同步当前订单中的最新成本记录 update by zhanghao start
				CostRecord costRecord = costRecordDao.getById(refund.getRecordId());
				AirticketOrderPnr pnr = airticketOrderPnrDao.getByUuid(costRecord.getPnrUuid());
				AirticketOrderPnrAirline airline = airticketOrderPnrAirlineDao.getByUuid(costRecord.getAirlineUuid());
				AirticketOrderPnrAirlinePrice price = airticketOrderPnrAirlinePriceDao.getByCostRecordUuid(costRecord.getUuid());
				if(costRecord!=null){
//					costRecord.setName(airline.getAirlineName()); update by zhanghao 财务撤销不对成本记录的款项名称做修改 20160112 修复bug11734，11723
					costRecord.setAirlineUuid(airline.getUuid());
					costRecord.setAirline(pnr.getAirline());
					costRecord.setAirlineName(airline.getAirlineName());
					costRecord.setQuantity(price.getPersonNum());
					costRecord.setCostTotalDeposit(new BigDecimal(price.getFrontMoney()));
					costRecord.setPrice(new BigDecimal(price.getPrice()));
					if(StringUtils.isNotBlank(pnr.getCostSupplyType())){
						costRecord.setSupplierType(Integer.parseInt(pnr.getCostSupplyType()));
					}
					costRecord.setSupplyName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
					costRecord.setSupplyId(pnr.getCostSupplyId());
					costRecord.setRate(new BigDecimal(price.getExchangerate()));
					costRecord.setCurrencyId(price.getCurrencyId());
					costRecord.setCurrencyAfter(currencyService.getRMBCurrencyId().getId().intValue());
					costRecord.setPriceAfter(new BigDecimal(price.getPersonNum()).multiply(new BigDecimal(price.getPrice())).multiply(new BigDecimal(price.getExchangerate())).setScale(2, BigDecimal.ROUND_HALF_UP));
					costRecord.setIsJoin(pnr.getCodeType());
					costRecord.setBigCode(pnr.getFlightPnr());
					costRecord.setBankName(pnr.getCostBankName());
					costRecord.setBankAccount(pnr.getCostAccountNo());
					costRecord.setPayStatus(payStatus);
					this.setOptInfo(costRecord, BaseService.OPERATION_UPDATE);
					airticketOrderMoneyAmountDao.getSession().saveOrUpdate(costRecord);
					serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, costRecord.getId().intValue());
				}
				//财务撤销成本的同时会同步当前订单中的最新成本记录 update by zhanghao end
			}*/
		} else {
			//根据airticket_order_moneyAmount表获取付款状态
			payStatus = buildPaystatusByMoneyAmountId(moneyType, refund.getRecordId().intValue());
			airticketOrderMoneyAmountDao.updatePaystatusByMoneyAmountId(payStatus, refund.getRecordId().intValue());
		} 
		//计算并更新付款状态-------------------------------------------------
		//更新订单的付款状态
		Long orderId = null;
		if(refund.getMoneyType() == 1) {
			CostRecord costRecord = costRecordDao.getById(refund.getRecordId());
			orderId = costRecord.getOrderId();
		} else {
			AirticketOrderMoneyAmount moneyAmount = airticketOrderMoneyAmountDao.getById(refund.getRecordId().intValue());
			orderId = moneyAmount.getAirticketOrderId().longValue();
		}
		this.updateOrderRefundFlag(orderId);
		flag = true;
		return flag;
	}
	
	/**
	 * 获取订单列表（付款页面）
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return OrderRecordJsonBean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	public MtourOrderJsonBean getRefundOrderPage(BaseInput4MT input) {
		MtourOrderSearchJsonBean orderSearch = input.getParamObj(MtourOrderSearchJsonBean.class);

		//美途国际订单列表
		MtourOrderJsonBean mtourOrderJsonBean = new MtourOrderJsonBean();
		com.trekiz.admin.common.persistence.Page<Map<String, Object>> mtourOrderPage = airticketOrderMtourDao.getMtourOrderList(orderSearch);
		if(CollectionUtils.isNotEmpty(mtourOrderPage.getList())) {
			List<MtourOrderDetail> results = new ArrayList<MtourOrderDetail>();
			for(Map<String, Object> data : mtourOrderPage.getList()) {
				MtourOrderDetail mtourOrderDetail = new MtourOrderDetail();
				mtourOrderDetail.setOrderUuid(String.valueOf(data.get("orderUuid")));
				mtourOrderDetail.setGroupNo(String.valueOf(data.get("groupNo")));
				mtourOrderDetail.setOrderDateTime(DateUtils.date2String((Date)data.get("orderDateTime"), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS));
				mtourOrderDetail.setOrderer(String.valueOf(data.get("orderer")));
				mtourOrderDetail.setFinancePayOrderListPayStatusCode(String.valueOf(data.get("financePayOrderListPayStatusCode")));
				if(data.get("financePayOrderListPayStatusName") != null) {
					mtourOrderDetail.setFinancePayOrderListPayStatusName(String.valueOf(data.get("financePayOrderListPayStatusName")));
				} else {
					mtourOrderDetail.setFinancePayOrderListPayStatusName("");
				}
				mtourOrderDetail.setLockStatus(String.valueOf(data.get("lockStatus")));
				mtourOrderDetail.setDepartureDate(String.valueOf(data.get("departureDate")));
				
				results.add(mtourOrderDetail);
			}
			mtourOrderJsonBean.setResults(results);
		}
		
		//初始化订单列表的应付金额和已付金额
		if(mtourOrderPage != null && CollectionUtils.isNotEmpty(mtourOrderPage.getList())) {
			for(MtourOrderDetail mtourOrderDetail : mtourOrderJsonBean.getResults()) {
				//加载应付金额
				List<MtourOrderMoney> payableAmount = airticketOrderMtourDao.getPayableAmountByOrderId(Long.parseLong(mtourOrderDetail.getOrderUuid()));
				mtourOrderDetail.setPayableAmount(payableAmount);
				
				//加载已付金额
				List<MtourOrderMoney> paidAmount = airticketOrderMtourDao.getPaidAmountByOrderId(Long.parseLong(mtourOrderDetail.getOrderUuid()));
				mtourOrderDetail.setPaidAmount(paidAmount);
			}
		}
		
		MtourOrderPage page = new MtourOrderPage();
		page.setCurrentIndex(mtourOrderPage.getPageNo());
		page.setRowCount(mtourOrderPage.getPageSize());
		page.setTotalRowCount((int)mtourOrderPage.getCount());
		mtourOrderJsonBean.setPage(page);
		
		return mtourOrderJsonBean;
	}
	
	/**
	 * 更新机票订单的付款状态
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	public boolean updateOrderRefundFlag(Long orderId) {
		return financeDao.updateOrderRefundFlag(orderId);
	}
	
	/**
	 * 初始化所有美途订单的付款状态
	 * @Description: 
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-26
	 */
	public boolean initOrderRefundFlag() {
		boolean flag = true;
		Object mtourCompanyUuids = propertiesLoader.getProperties().get("mtourCompanyUuid");
		if(mtourCompanyUuids != null) {
			String[] mtourCompanyUuidArr = String.valueOf(mtourCompanyUuids).split(",");
			List<Office> offices = officeDao.findByUuids(Arrays.asList(mtourCompanyUuidArr));
			List<Long> userIds = userDao.findUserIdsByOffices(offices);
			
			List<AirticketOrder> airticketOrders = airticketOrderDao.getAirticketOrderByCreateBys(userIds);
			if(CollectionUtils.isNotEmpty(airticketOrders)) {
				for(AirticketOrder airticketOrder : airticketOrders) {
					updateOrderRefundFlag(airticketOrder.getId());
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 获取订单列表款项明细
	 * by songyang 
	 */
	@Override
	public RefundRecordsJsonBean  getOrderPaymentInfo(BaseInput4MT input) throws Exception {
		//批量付款信息
		RefundRecordsJsonBean refundRecordsJsonBean = new RefundRecordsJsonBean();
		List<RefundRecordJsonBean> refundRecordJsonBeans = new ArrayList<RefundRecordJsonBean>();
		//订单ID
		String orderUuid = input.getParamValue("orderUuid");
//		String orderUuid = "4274";
		//1-地接社 2-渠道商
		String tourOperatorChannelCategoryCode = input.getParamValue("tourOperatorChannelCategoryCode");
//		String tourOperatorChannelCategoryCode = "1";
		//付款对象Uuid
		String paymentObjectUuid = input.getParamValue("paymentObjectUuid");
//		String paymentObjectUuid = "824";
		AirticketOrder airOrder =  airticketOrderDao.getAirticketOrderById(Long.parseLong(orderUuid));
		refundRecordsJsonBean.setGroupNo(airOrder.getGroupCode());
		Map<Object, BigDecimal> strTotalPaidAmount  = new HashMap<Object,BigDecimal>();//已付总额
//		List<Map<String,Object>> pnrList = airticketOrderPnrService.queryAirticketOrderPNCByOrderUuid(Integer.valueOf(orderUuid));
		//拼装应付总额
		Map<String, BigDecimal> payableTotalAmount = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> paymentTotalAmount = new HashMap<String, BigDecimal>();
		//拼装已付总额
		Map<String, BigDecimal> paidTotalAmount = new HashMap<String, BigDecimal>();
		//格式化金额 
		DecimalFormat dfPrice = new DecimalFormat(",##0.00");
		//地接社
		String paymentObject = input.getParamValue("paymentObject");
		
		
		
//		for(){
//			
//			
//			
//		}
//		
		if("1".equals(tourOperatorChannelCategoryCode)){
			List<CostRecord> costRecords = costRecordDao.getByOrderIdAndSupplyId(Long.parseLong(orderUuid), Context.ORDER_TYPE_JP, Integer.parseInt(paymentObjectUuid));
			if(CollectionUtils.isNotEmpty(costRecords)){
				for(CostRecord costRecord : costRecords) {
					Currency currency = currencyDao.findById(Long.parseLong(costRecord.getCurrencyId().toString()));
					//设置付款和付款对象名称
					//设置付款对象id和付款对象名称
					if(refundRecordsJsonBean.getPaymentObjectUuid() == null) {
						refundRecordsJsonBean.setPaymentObjectUuid(costRecord.getSupplyId());
						refundRecordsJsonBean.setPaymentObjectName(costRecord.getSupplyName());
					}
					
					RefundRecordJsonBean refundRecord = new RefundRecordJsonBean();
					refundRecord.setCurrencyId(costRecord.getCurrencyId());
//					refundRecord.setPNR(pnrList.get(0).get(""));
					if(costRecord.getPayStatus()==3||costRecord.getPayStatus()==0){
						refundRecord.setPaymentStatusCode("0");
					}else if(costRecord.getPayStatus()==1){
						refundRecord.setPaymentStatusCode("1");
					}
					refundRecord.setPaymentUuid(costRecord.getId().longValue());
					switch (costRecord.getPayStatus()) {
					case 0:
						refundRecord.setPaymentStatusName("待付款");
						break;
					case 1:
						refundRecord.setPaymentStatusName("已付款");
						break;
					case 3:
						refundRecord.setPaymentStatusName("待付款");
						break;
					default:
						break;
					}
					refundRecord.setApprovalDateTime(DateUtils.formatDate(costRecord.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
					refundRecord.setApprovalDateStr(DateUtils.formatDate(costRecord.getCreateDate(),"yyyy-MM-dd"));
					refundRecord.setFundsType("4");
					refundRecord.setChangeabled(false);
//					switch (costRecord.getOrderType()) {
//					case 2:
//						refundRecord.setFundsName("散拼");
//						break;
//					case 6:
//						refundRecord.setFundsName("签证");
//						break;
//					case 7:
//						refundRecord.setFundsName("机票");
//						break;
//					default:
//						break;
//					}
					refundRecord.setFundsName(costRecord.getName());
					refundRecord.setExchangeRate(costRecord.getRate());
					refundRecord.setApplicant(costRecord.getCreateBy().getName());
					if (costRecord.getIsJoin() == 1){ //地接社不显示pnr
						refundRecord.setPnr("");
					}else {
						refundRecord.setPnr(costRecord.getBigCode());
					}
					refundRecord.setTourOperatorUuid(costRecord.getSupplyId().toString());
					refundRecord.setTourOperatorName(costRecord.getSupplyName());
					refundRecord.setCostUnitPrice(currency.getCurrencyMark()+costRecord.getPrice());
					refundRecord.setExchangeRate(costRecord.getRate());
					refundRecord.setConvertedTotalAmount("¥"+costRecord.getPriceAfter());
//					refundRecord.setConvertedTotalAmount(currency.getCurrencyMark()+costRecord.getPriceAfter());
					refundRecord.setReceiveBank(costRecord.getBankName());
					refundRecord.setReceiveAccountNo(costRecord.getBankAccount());
					refundRecord.setPeopleCount(costRecord.getQuantity());
					refundRecord.setFundsTypeName("成本");
					refundRecord.setMemo(costRecord.getComment());
					//应付金额
					AmountJsonBean payableAmount = new AmountJsonBean();
					payableAmount.setCurrencyUuid(String.valueOf(costRecord.getCurrencyId()));
					BigDecimal amountBig = costRecord.getPrice().multiply(new BigDecimal(costRecord.getQuantity()));
					payableAmount.setAmount(amountBig.doubleValue());
					//旧应付金额
//					refundRecord.setPayableAmount(payableAmount);
					//新应付金额
					refundRecord.setStrPayableAmount(currency.getCurrencyMark()+dfPrice.format(amountBig.doubleValue()));
					refundRecord.setStrPayableAmount_amount(amountBig+"");
					//为应付总额金额做同币种想加
					if(payableTotalAmount.containsKey(String.valueOf(costRecord.getCurrencyId()))) {
						BigDecimal currAmount = payableTotalAmount.get(String.valueOf(costRecord.getCurrencyId()));
						payableTotalAmount.put(String.valueOf(costRecord.getCurrencyId()), currAmount.add(amountBig));
					} else {
						payableTotalAmount.put(String.valueOf(costRecord.getCurrencyId()), amountBig);
					}
					
					//已付金额
					List<AmountJsonBean> paidAmounts = new ArrayList<AmountJsonBean>();//已付金额
					List<Map<String, Object>> amounts = refundDao.getRefundsByRecordIdAndMoneyType(costRecord.getId(), Refund.MONEY_TYPE_COST);
//					List<Map<String, Object>> amounts = refundDao.getRefundsByRecordIdAndMoneyType(costRecord.getId(), Refund.MONEY_TYPE_COST, paymentObjectUuid);
					if(CollectionUtils.isNotEmpty(amounts)) {
						for(Map<String, Object> amount : amounts) {
							AmountJsonBean paidAmount = new AmountJsonBean();
							paidAmount.setCurrencyUuid(String.valueOf(amount.get("currencyId")));
							paidAmount.setAmount(((BigDecimal)amount.get("amount")).doubleValue());
							paidAmounts.add(paidAmount);
						}
						//旧已付金额
//						refundRecord.setPaidAmount(paidAmounts.get(0));
						//新已付金额字符串
						Currency currencyyf = currencyDao.findById(Long.parseLong(paidAmounts.get(0).getCurrencyUuid()));
						refundRecord.setStrPaidAmount(currencyyf.getCurrencyMark()+dfPrice.format(paidAmounts.get(0).getAmount()));
						if(strTotalPaidAmount.containsKey(currencyyf.getCurrencyMark())){
							strTotalPaidAmount.put(currencyyf.getCurrencyMark(), strTotalPaidAmount.get(currencyyf.getCurrencyMark()).add(new BigDecimal(paidAmounts.get(0).getAmount())));
						}else{
							strTotalPaidAmount.put(currencyyf.getCurrencyMark(), new BigDecimal(paidAmounts.get(0).getAmount()));
						}
					}
					
					refundRecordJsonBeans.add(refundRecord);
					
				}
			}
			
		}else if("2".equals(tourOperatorChannelCategoryCode)){
			//渠道商
			refundRecordsJsonBean.setPaymentObjectUuid(airOrder.getAgentinfoId().intValue());
			refundRecordsJsonBean.setPaymentObjectName(airOrder.getNagentName());
			//付款记录集合
			List<AirticketOrderMoneyAmount> moneyAmountInfos = airticketOrderMoneyAmountDao.getByOrderId(Integer.parseInt(orderUuid));
			if(CollectionUtils.isNotEmpty(moneyAmountInfos)) {
				for(AirticketOrderMoneyAmount amount : moneyAmountInfos) {
					RefundRecordJsonBean refundRecord = new RefundRecordJsonBean();
					refundRecord.setCurrencyId(amount.getCurrencyId());
					refundRecord.setPaymentUuid(amount.getId().longValue());
					refundRecord.setApprovalDateTime(DateUtils.formatDate(amount.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
					refundRecord.setApprovalDateStr(DateUtils.formatDate(amount.getCreateDate(),"yyyy-MM-dd"));
					refundRecord.setMemo(amount.getMemo());
					//数据库款项类型：款项类型（1、借款，2、退款，3、追加成本）
					Integer moneyType = null;//付款表refund中的款项类型
					if(1 == amount.getMoneyType()) {
						refundRecord.setFundsType("1");
						refundRecord.setFundsTypeName("借款");
						moneyType = Refund.MONEY_TYPE_BORROW;
//						moneyType = 1;
					} else if(2 == amount.getMoneyType()) {
						refundRecord.setFundsType("2");
						refundRecord.setFundsTypeName("退款");
						moneyType = Refund.MONEY_TYPE_RETURNMONEY;
					} else if(3 == amount.getMoneyType()) {
						refundRecord.setFundsType("3");
						refundRecord.setFundsTypeName("追加成本");
						moneyType = Refund.MONEY_TYPE_ADDCOST;
//						moneyType = 3;
					}
					
					refundRecord.setPaymentStatusCode(amount.getPaystatus().toString());
					switch (amount.getPaystatus()) {
					case 0:
						refundRecord.setPaymentStatusName("待付款");
						break;
					case 1:
						refundRecord.setPaymentStatusName("已付款");
						break;
					default:
						break;
					}
					
					refundRecord.setFundsName(amount.getFundsName());
					
					//本次退款金额， 本次追加金额
					if(moneyType == Refund.MONEY_TYPE_ADDCOST){
						//累计金额
						String ljA = "";
						// 获取该订单下款项类型为：moneyType的金额信息
						List<AirticketOrderMoneyAmount> moneyTypeAmounts = airticketOrderMoneyAmountDao.getByOrderIdStatusAndMoneyType(amount.getAirticketOrderId(), AirticketOrderMoneyAmount.STATUS_CONFIRM, 3);
						if (CollectionUtils.isNotEmpty(moneyTypeAmounts)) {
							Map<Object,BigDecimal> RMBAmount = new HashMap<Object,BigDecimal>();
							for (AirticketOrderMoneyAmount entity : moneyTypeAmounts) {
								Currency currency = currencyDao.findById(entity.getCurrencyId().longValue());
								if(RMBAmount.containsKey(currency.getCurrencyMark())){
									RMBAmount.put(currency.getCurrencyMark(), RMBAmount.get(currency.getCurrencyMark()).add(new BigDecimal(entity.getAmount())));
								}else{
									RMBAmount.put(currency.getCurrencyMark(), new BigDecimal(entity.getAmount()));
								}
							}
							DecimalFormat d = new DecimalFormat(",##0.00");
							for(Object key : RMBAmount.keySet()){
								ljA = ljA+ key+ d.format(RMBAmount.get(key))+"+";
							}
						}
						if(StringUtils.isNotEmpty(ljA)){
							ljA = ljA.substring(0, ljA.length()-1);
							refundRecord.setTotalAddAmount(ljA);
						}else{
							refundRecord.setTotalAddAmount("");
						}
					    Currency currency = currencyDao.findById(Long.parseLong(amount.getCurrencyId().toString()));
					    String currentAddAmount = "";
					    DecimalFormat d = new DecimalFormat(",##0.00");
					    currentAddAmount = currency.getCurrencyMark()+d.format(amount.getAmount());
					    refundRecord.setCurrentAddAmount(currentAddAmount);
					}else if(moneyType == Refund.MONEY_TYPE_RETURNMONEY){
						//累计金额
						String ljA = "";
						// 获取该订单下款项类型为：moneyType的金额信息
						List<AirticketOrderMoneyAmount> moneyTypeAmounts = airticketOrderMoneyAmountDao.getByOrderIdStatusAndMoneyType(amount.getAirticketOrderId(), AirticketOrderMoneyAmount.STATUS_CONFIRM, moneyType);
						if (CollectionUtils.isNotEmpty(moneyTypeAmounts)) {
							Map<Object,BigDecimal> RMBAmount = new HashMap<Object,BigDecimal>();
							for (AirticketOrderMoneyAmount entity : moneyTypeAmounts) {
								Currency currency = currencyDao.findById(entity.getCurrencyId().longValue());
								if(RMBAmount.containsKey(currency.getCurrencyMark())){
									RMBAmount.put(currency.getCurrencyMark(), RMBAmount.get(currency.getCurrencyMark()).add(new BigDecimal(entity.getAmount())));
								}else{
									RMBAmount.put(currency.getCurrencyMark(), new BigDecimal(entity.getAmount()));
								}
							}
							DecimalFormat d = new DecimalFormat(",##0.00");
							for(Object key : RMBAmount.keySet()){
								ljA = ljA+ key+ d.format(RMBAmount.get(key))+"+";
							}
						}
						if(StringUtils.isNotEmpty(ljA)){
							ljA = ljA.substring(0, ljA.length()-1);
							refundRecord.setTotalRefundAmount(ljA);
						}else{
							refundRecord.setTotalRefundAmount("");
						}
						Currency currency = currencyDao.findById(Long.parseLong(amount.getCurrencyId().toString()));
						String currentRefundAmount = "";
						DecimalFormat d = new DecimalFormat(",##0.00");
						currentRefundAmount = currency.getCurrencyMark()+d.format(amount.getAmount()).toString();
						refundRecord.setCurrentRefundAmount(currentRefundAmount);
					}else if(moneyType == Refund.MONEY_TYPE_BORROW){
						//累计金额
						String ljA = "";
						// 获取该订单下款项类型为：moneyType的金额信息
						List<AirticketOrderMoneyAmount> moneyTypeAmounts = airticketOrderMoneyAmountDao.getByOrderIdStatusAndMoneyType(amount.getAirticketOrderId(), AirticketOrderMoneyAmount.STATUS_CONFIRM, 1);
						if (CollectionUtils.isNotEmpty(moneyTypeAmounts)) {
							Map<Object,BigDecimal> RMBAmount = new HashMap<Object,BigDecimal>();
							for (AirticketOrderMoneyAmount entity : moneyTypeAmounts) {	
								Currency currency = currencyDao.findById(entity.getCurrencyId().longValue());
								if(RMBAmount.containsKey(currency.getCurrencyMark())){
									RMBAmount.put(currency.getCurrencyMark(), RMBAmount.get(currency.getCurrencyMark()).add(new BigDecimal(entity.getAmount())));
								}else{
									RMBAmount.put(currency.getCurrencyMark(), new BigDecimal(entity.getAmount()));
								}
							}
							DecimalFormat d = new DecimalFormat(",##0.00");
							for(Object key : RMBAmount.keySet()){
								ljA = ljA+ key+ d.format(RMBAmount.get(key))+"+";
							}
						}
						
						if(StringUtils.isNotEmpty(ljA)){
							ljA = ljA.substring(0, ljA.length()-1);
							refundRecord.setTotalBorrowAmount(ljA);
						}else{
							refundRecord.setTotalBorrowAmount("");
						}
						Currency currency = currencyDao.findById(Long.parseLong(amount.getCurrencyId().toString()));
						String currentRefundAmount = "";
						DecimalFormat d = new DecimalFormat(",##0.00");
						currentRefundAmount = currency.getCurrencyMark()+d.format(amount.getAmount());
						refundRecord.setCurrentBorrowAmount(currentRefundAmount);
					}
					
					
					refundRecord.setChangeabled(false);
					refundRecord.setExchangeRate(new BigDecimal(amount.getExchangerate()));
					refundRecord.setApplicant(userDao.getById(amount.getCreateBy().longValue()).getName());
					//应付金额
					AmountJsonBean payableAmount = new AmountJsonBean();
					payableAmount.setCurrencyUuid(String.valueOf(amount.getCurrencyId()));
					payableAmount.setAmount(amount.getAmount());
//					refundRecord.setPayableAmount(payableAmount);
					Currency currency  = currencyDao.findById(Long.parseLong(amount.getCurrencyId().toString()));
					refundRecord.setStrPayableAmount(currency.getCurrencyMark()+dfPrice.format(amount.getAmount()));
					refundRecord.setStrPayableAmount_amount(amount.getAmount()+"");
					//为应付金额做同币种想加
//					BigDecimal amountBig = new BigDecimal(amount.getAmount());
//					if(paymentTotalAmount.containsKey(String.valueOf(amount.getCurrencyId()))) {
//						BigDecimal currAmount = paymentTotalAmount.get(String.valueOf(amount.getCurrencyId()));
//						paymentTotalAmount.put(String.valueOf(amount.getCurrencyId()), currAmount.add(amountBig));
//					} else {
//						paymentTotalAmount.put(String.valueOf(amount.getCurrencyId()), amountBig);
//					}
					
					//为应付金额做同币种想加
					BigDecimal amountBig = new BigDecimal(amount.getAmount());
					if(payableTotalAmount.containsKey(String.valueOf(amount.getCurrencyId()))) {
						BigDecimal currAmount = payableTotalAmount.get(String.valueOf(amount.getCurrencyId()));
						payableTotalAmount.put(String.valueOf(amount.getCurrencyId()), currAmount.add(amountBig));
					} else {
						payableTotalAmount.put(String.valueOf(amount.getCurrencyId()), amountBig);
					}
					
					//已付金额
					List<AmountJsonBean> paidAmounts = new ArrayList<AmountJsonBean>();//已付金额
//					List<Map<String, Object>> amounts = refundDao.getRefundsByRecordIdAndMoneyType(amount.getId().longValue(), moneyType,paymentObjectUuid);
					List<Map<String, Object>> amounts = refundDao.getRefundsByRecordIdAndMoneyType(amount.getId().longValue(), moneyType);
					if(CollectionUtils.isNotEmpty(amounts)) {
						for(Map<String, Object> amountInfo : amounts) {
							AmountJsonBean paidAmount = new AmountJsonBean();
							paidAmount.setCurrencyUuid(String.valueOf(amountInfo.get("currencyId")));
//							paidAmount.setAmount((Double)amountInfo.get("amount"));
							paidAmount.setAmount(((BigDecimal)amountInfo.get("amount")).doubleValue());
							paidAmounts.add(paidAmount);
						}
//						refundRecord.setPaidAmount(paidAmounts.get(0));
						Currency currencyyf  = currencyDao.findById(Long.parseLong(amount.getCurrencyId().toString()));
						refundRecord.setStrPaidAmount(currencyyf.getCurrencyMark()+dfPrice.format(paidAmounts.get(0).getAmount()));
						if(strTotalPaidAmount.containsKey(currencyyf.getCurrencyMark())){
							strTotalPaidAmount.put(currencyyf.getCurrencyMark(), strTotalPaidAmount.get(currencyyf.getCurrencyMark()).add(new BigDecimal(paidAmounts.get(0).getAmount())));
						}else{
							strTotalPaidAmount.put(currencyyf.getCurrencyMark(), new BigDecimal(paidAmounts.get(0).getAmount()));
						}
					}
					refundRecordJsonBeans.add(refundRecord);
				}
			}
		}
		
		//已付金额
		List<AmountJsonBean> paidtotalAmounts = new ArrayList<AmountJsonBean>();//已付金额
		//已付总额
		List<Map<String, Object>> yfTotalPrice = refundDao.getMoneyAmountsByBatchNumber(paymentObjectUuid);
		if(CollectionUtils.isNotEmpty(yfTotalPrice)) {
			for(Map<String, Object> amount : yfTotalPrice) {
				AmountJsonBean paidAmount = new AmountJsonBean();
				paidAmount.setCurrencyUuid(String.valueOf(amount.get("currencyId")));
				paidAmount.setAmount(((BigDecimal)amount.get("amount")).doubleValue());
				paidtotalAmounts.add(paidAmount);
			}
			//新已付金额字符串
			Currency currencyyf = currencyDao.findById(Long.parseLong(paidtotalAmounts.get(0).getCurrencyUuid()));
//			refundRecordsJsonBean.setPaidTotalAmount(currencyyf.getCurrencyMark()+paidtotalAmounts.get(0).getAmount());
		}
		//遍历Map拼接币种
		DecimalFormat d = new DecimalFormat(",##0.00");
		String strTotalPaidAmounts = "";
		for (Object key : strTotalPaidAmount.keySet()) {
			strTotalPaidAmounts = strTotalPaidAmounts+ key+ d.format(strTotalPaidAmount.get(key))+"+";
		}
		if(StringUtils.isNotEmpty(strTotalPaidAmounts)){
			refundRecordsJsonBean.setPaidTotalAmount(strTotalPaidAmounts.substring(0, strTotalPaidAmounts.length()-1));
		}else{
			refundRecordsJsonBean.setPaidTotalAmount("");
		}
		//'应付总额'
		StringBuffer paymentTotalAmountStr = new StringBuffer();
		Set<String> totalAmountSet = payableTotalAmount.keySet();
		if(CollectionUtils.isNotEmpty(totalAmountSet)) {
			for(String currencyId : totalAmountSet) {
				paymentTotalAmountStr.append(currencyDao.getById(Long.parseLong(currencyId)).getCurrencyMark());
				paymentTotalAmountStr.append(d.format(payableTotalAmount.get(currencyId)));
				paymentTotalAmountStr.append("+");
			}
			paymentTotalAmountStr.deleteCharAt(paymentTotalAmountStr.length()-1);
			
			refundRecordsJsonBean.setPayableTotalAmount(paymentTotalAmountStr.toString());
		}
		refundRecordsJsonBean.setResults(refundRecordJsonBeans);
		return refundRecordsJsonBean;
	}
 
	/**
	 *  订单列表-二级列表-付款记录-详情
	 * @author zhangchao
	 * refund.status is null 是已付款 0的时候是已撤销
	 */
	@Override
	@SuppressWarnings("deprecation")
	public PayDetailJsonBean queryPayJDetail(BaseInput4MT input) {
		String orderId=input.getParamValue("orderUuid");//订单id
		String paymentObj_paymentUuid=input.getParamValue("paymentObj_paymentUuid");//付款对象付款的uuid，批量号
		String paymentObjectUuid=input.getParamValue("paymentObjectUuid");//付款对象的id
		PayDetailJsonBean payDetailJsonBean=new PayDetailJsonBean();
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT refund.pay_type AS paymentMethodCode,dict.label AS paymentMethodName,refund.create_date AS ")
		   .append(" paymentDate,refund.payee as receiveCompany,refund.remarks as memo")
		   .append(" FROM refund refund ")
		   .append("LEFT JOIN sys_dict dict ON dict.value=refund.pay_type ")
		   .append("WHERE refund.batch_number=? and dict.type='offlineorder_pay_type' and refund.del_flag=0");
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list = refundDao.findBySql(sql.toString(),Map.class,paymentObj_paymentUuid);
		if(list.get(0).get("paymentMethodCode")!=null){
			payDetailJsonBean.setPaymentMethodCode(list.get(0).get("paymentMethodCode").toString());
		}
		if(list.get(0).get("paymentMethodName")!=null){
			payDetailJsonBean.setPaymentMethodName(list.get(0).get("paymentMethodName").toString());
		}
		if(list.get(0).get("paymentDate")!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			Date date=null;
			try {
				date = sdf.parse(list.get(0).get("paymentDate").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			payDetailJsonBean.setPaymentDate(date);
		}
		if(list.get(0).get("receiveCompany")!=null){
			payDetailJsonBean.setReceiveCompany(list.get(0).get("receiveCompany").toString());
		}
		if(list.get(0).get("memo")!=null){
			payDetailJsonBean.setMemo(list.get(0).get("memo").toString());
		}
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT refund.batch_number AS paymentUuid,case when refund.moneyType=1 then record.createDate ELSE ");
		sbf.append(" moneyAmount.createDate END AS approvalDate,refund.moneyType,refund.record_id as id, ");
		sbf.append("case when refund.moneyType=1 then 4 else moneyAmount.moneyType END AS fundsType,sys.name AS applicant,");
		sbf.append(" money.exchangerate  AS exchangeRate,");
		sbf.append("case when refund.status is null then 0 else 1 end as paymentStatusCode,case when refund.status is null ");
		sbf.append(" then '已付款' else '已撤销' end as paymentStatusName, ");
		sbf.append("money.amount as paymentAmount,money.currency_id as currencyId, ");
		sbf.append("case when refund.moneyType=1 then record.price*quantity else moneyAmount.amount end as amount ,");
		sbf.append("case when refund.moneyType=1 then record.currencyId else moneyAmount.currency_id end as currency ");
		sbf.append("FROM refund refund ");
		sbf.append("LEFT JOIN cost_record record ON record.id=refund.record_id ");
		sbf.append("LEFT JOIN airticket_order_moneyAmount moneyAmount on refund.record_id=moneyAmount.id ");
		sbf.append("LEFT JOIN airticket_order_moneyAmount money on refund.money_serial_num=money.serialNum ");
		sbf.append("LEFT JOIN sys_user sys ON sys.id=refund.create_by ");
		sbf.append("where refund.batch_number=? and refund.del_flag=0 ");
		List<Map<String,Object>> rec=new ArrayList<Map<String,Object>>();
		rec=refundDao.findBySql(sbf.toString(), Map.class,paymentObj_paymentUuid);
		//本次收款金额---处理
		String str="";
		Double money=0.00;
		for(Map<String,Object> map:rec){
			String currencyId=map.get("currencyId").toString();
			Currency currency = currencyDao.getById(Long.parseLong(currencyId));
			String moneyAmount=currency.getCurrencyMark()+map.get("paymentAmount");
			map.put("paymentAmount", moneyAmount);
			String currencyid=map.get("currency").toString();
			Currency currency1 = currencyDao.getById(Long.parseLong(currencyid));
			String payableAmount=currency1.getCurrencyMark()+map.get("amount");
			map.put("payableAmount", payableAmount);
			List<Map<String, Object>> paid = refundDao.getRefundsByRecordIdAndMoneyType(Long.parseLong(map.get("id").toString()), Integer.parseInt(map.get("moneyType").toString()));
			String am="";
			for(Map<String,Object> paidA:paid){
				am=paidA.get("currencyMark").toString()+paidA.get("amount");
				map.put("paidAmount", am);
			}
			if(map.get("fundsType").toString().equals("1")){
				map.put("fundsTypeName", "借款");
			}else if(map.get("fundsType").toString().equals("2")){
				map.put("fundsTypeName", "退款");
			}else if(map.get("fundsType").toString().equals("3")){
				map.put("fundsTypeName", "追加成本");
			}else{
				map.put("fundsTypeName", "成本");
			}
		}
		payDetailJsonBean.setRecordList(rec);

		//付款总额
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT ");
		sb.append("case when refund.moneyType=1 then record.rate else moneyAmount.exchangerate END AS exchangeRate,");
		sb.append("sum(money.amount) as paymentAmount,sum(money.amount * money.exchangerate) AS allMoney,money.currency_id as currencyId ");
		sb.append("FROM refund refund ");
		sb.append("LEFT JOIN cost_record record ON record.id=refund.record_id ");
		sb.append("LEFT JOIN airticket_order_moneyAmount moneyAmount on refund.record_id=moneyAmount.id ");
		sb.append("LEFT JOIN airticket_order_moneyAmount money on refund.money_serial_num=money.serialNum ");
		sb.append("where refund.batch_number=? and refund.del_flag=0 group by money.currency_id");
		List<Map<String,Object>> allMoney=refundDao.findBySql(sb.toString(), Map.class,paymentObj_paymentUuid);
		for(Map<String,Object> map:allMoney){
			String currencyId=map.get("currencyId").toString();
			Currency currency = currencyDao.getById(Long.parseLong(currencyId));
			if("".equals(str)){
				str=currency.getCurrencyMark()+map.get("paymentAmount");
			}else{
				str=str+"+"+currency.getCurrencyMark()+map.get("paymentAmount");
			}
			BigDecimal b1=new BigDecimal(map.get("allMoney").toString());
			Double b=b1.doubleValue();
			money+=b;
		}
		/*List<Map<String,Object>> batchNumber = refundDao.getMoneyAmountsByBatchNumber(paymentObj_paymentUuid);
		String str="";
		Double money=0.00;
		for(Map<String,Object> map:batchNumber){
			if(str==""){
				str=map.get("currencyMark").toString()+map.get("amount");
			}else{
				str=str+"+"+map.get("currencyMark").toString()+map.get("amount");
			}
			BigDecimal b1=new BigDecimal(map.get("amount").toString());
			BigDecimal b2=new BigDecimal(map.get("exchangerate").toString());
			Double b=b1.multiply(b2).doubleValue();
			money+=b;
		}*/
		payDetailJsonBean.setPaymentTotalAmount(str);
		//转换后币种总额
		BigDecimal bg2=new BigDecimal(money);
		double value = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		payDetailJsonBean.setConvertedTotalAmount("¥"+new java.text.DecimalFormat("0.00").format(value));
		StringBuffer sbb=new StringBuffer();
		//------------------------------------------
		if(payDetailJsonBean.getPaymentMethodCode().equals("1")){
			sbb.append("SELECT ch.check_number as checkNo,ch.create_date as checkIssueDate from refund refund ");
			sbb.append("left join pay_check ch on ch.id = refund.pay_type_Id ");
			sbb.append("where refund.batch_number=? and refund.del_flag=0");
			List<Map<String,Object>> lst=new ArrayList<Map<String,Object>>();
			lst=refundDao.findBySql(sbb.toString(),Map.class,paymentObj_paymentUuid );
			payDetailJsonBean.setCheckNo(lst.get(0).get("checkNo").toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			Date iDate=null;
			try {
				iDate = sdf.parse(lst.get(0).get("checkIssueDate").toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			payDetailJsonBean.setCheckIssueDate(iDate);
		}
		if(payDetailJsonBean.getPaymentMethodCode().equals("4")){
			sbb.append("SELECT pay.bank_name as paymentBank,pay.bank_account as paymentAccount,")
				.append("pay.tobank_name as receiveBank,pay.tobank_account as receiveAccount ")
				.append("from refund refund left join pay_remittance pay on pay.id=refund.pay_type_id ")
				.append("where refund.batch_number=? and refund.del_flag=0");
			List<Map<String,Object>> lst=new ArrayList<Map<String,Object>>();
			lst=refundDao.findBySql(sbb.toString(),Map.class,paymentObj_paymentUuid );
			payDetailJsonBean.setPaymentBank(lst.get(0).get("paymentBank").toString());
			payDetailJsonBean.setPaymentAccount(lst.get(0).get("paymentAccount").toString());
			payDetailJsonBean.setReceiveBank(lst.get(0).get("receiveBank").toString());
			payDetailJsonBean.setReceiveAccount(lst.get(0).get("receiveAccount").toString());
		}
		//付款附件
		StringBuffer st=new StringBuffer();
		st.append("SELECT refund.pay_voucher as id from refund refund where refund.batch_number=? and refund.del_flag=0");
		List<Map<String,Object>> lt=new ArrayList<Map<String,Object>>();
		lt=refundDao.findBySql(st.toString(), Map.class,paymentObj_paymentUuid);
		String[] strings = lt.get(0).get("id").toString().split(",");
		for(String s:strings){
			if(!s.equals("")){
				DocInfo docInfo = docInfoDao.getById(Long.parseLong(s));
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("attachmentUuid", docInfo.getId());
				map.put("fileName", docInfo.getDocName());
				map.put("attachmentUrl", docInfo.getDocPath());
				List<Map<String,Object>> ll=new ArrayList<Map<String,Object>>();
				ll.add(map);
				payDetailJsonBean.setAttachments(ll);
			}
		}
		return payDetailJsonBean;
	} 
	
	/**
	 * 根据订单uuid和付款对象uuid获取付款记录
	 * @Description: 
	 * @param @param input
	 * @param @return   
	 * @return List<SecondRecordsJsonBean>  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-29
	 */
	@SuppressWarnings("unchecked")
	public List<SecondRefundRecordsJsonBean> getRefundRecordsInfo(BaseInput4MT input) {
		String orderUuid = input.getParamValue("orderUuid");//订单uuid
		String paymentObjectUuid = input.getParamValue("paymentObjectUuid");//付款对象uuid
		String tourOperatorChannelCategoryCode = input.getParamValue("tourOperatorChannelCategoryCode");//'地接社/渠道商' 1-地接社 2-渠道商'
		String companyUUID = UserUtils.getUser().getCompany().getUuid();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" refund.create_date AS paymentDate, ");
		sql.append(" refund.pay_type AS paymentMethodCode, ");
		sql.append(" CASE  ");
		sql.append(" WHEN refund.status IS NULL THEN '0' ");
		sql.append(" WHEN refund.status=0 THEN '1' ");
		sql.append(" END ");
		sql.append(" AS paymentStatusCode, ");
		sql.append(" CASE  ");
		sql.append(" WHEN refund.status IS NULL THEN '已付款' ");
		sql.append(" WHEN refund.status=0 THEN '已撤销' ");
		sql.append(" END ");
		sql.append(" AS paymentStatusName, ");
		sql.append(" refund.batch_number AS paymentObjPaymentUuid, ");
		sql.append(" refund.pay_voucher AS payVoucher ");
		sql.append(" FROM refund refund  ");
		sql.append(" WHERE 1=1 ");
		
		if(StringUtils.isNotEmpty(companyUUID)) {
			sql.append(" AND refund.companyUuid='").append(companyUUID).append("' ");
		}
		
		//当为地接社时
		if("1".equals(tourOperatorChannelCategoryCode)) {
			List<CostRecord> costRecords = costRecordDao.getByOrderIdAndSupplyId(Long.parseLong(orderUuid), Context.ORDER_TYPE_JP, Integer.parseInt(paymentObjectUuid));
			if(CollectionUtils.isNotEmpty(costRecords)) {
				sql.append("AND refund.moneyType = 1 AND refund.record_id IN(");
				StringBuffer costRecordSql = new StringBuffer();
				for(CostRecord costRecord : costRecords) {
					costRecordSql.append(costRecord.getId());
					costRecordSql.append(",");
				}
				costRecordSql.deleteCharAt(costRecordSql.length()-1);
				costRecordSql.append(")");
				
				sql.append(costRecordSql);
			} else {
				return null;
			}
		//当为渠道商时
		} else if("2".equals(tourOperatorChannelCategoryCode)) {
			//付款记录集合
			List<AirticketOrderMoneyAmount> moneyAmountInfos = airticketOrderMoneyAmountDao.getByOrderId(Integer.parseInt(orderUuid));
			if(CollectionUtils.isNotEmpty(moneyAmountInfos)) {
				StringBuffer amountSql = new StringBuffer(" AND (");
				for(AirticketOrderMoneyAmount amount : moneyAmountInfos) {
					amountSql.append("(refund.moneyType = ").append(amount.getRefundMoneyType())
							 .append(" AND refund.record_id = ").append(amount.getId()).append(") ");
					amountSql.append("OR");
				}
				amountSql.delete(amountSql.length()-2, amountSql.length());
				amountSql.append(") ");
				
				sql.append(amountSql);
			} else {
				return null;
			}
		}
		
		sql.append(" GROUP BY refund.batch_number ");
		sql.append(" ORDER BY create_date DESC ");
		List<SecondRefundRecordsJsonBean> refundRecords = (List<SecondRefundRecordsJsonBean>)financeDao.findCustomObjBySql(sql.toString(), SecondRefundRecordsJsonBean.class);
		if(CollectionUtils.isNotEmpty(refundRecords)) {
			for(SecondRefundRecordsJsonBean refundRecord : refundRecords) {
				refundRecord.setOrderUuid(orderUuid);
				refundRecord.setPaymentObjectUuid(paymentObjectUuid);
				refundRecord.setTourOperatorChannelCategoryCode(tourOperatorChannelCategoryCode);
				//批次号信息转换
				if(StringUtils.isNotEmpty(refundRecord.getPaymentObjPaymentUuid())) {
					refundRecord.setPaymentObj_paymentUuid(refundRecord.getPaymentObjPaymentUuid());
				}
				
				if(refundRecord.getPaymentMethodCode() == 1) {
					refundRecord.setPaymentMethodName("支票");
				} else if(refundRecord.getPaymentMethodCode() == 3) {
					refundRecord.setPaymentMethodName("现金支付");
				} else if(refundRecord.getPaymentMethodCode() == 4) {
					refundRecord.setPaymentMethodName("汇款");
				}

				// 获取订单的支付凭证信息
				List<DocInfo> docInfos = null;
				if (StringUtils.isNotBlank(refundRecord.getPayVoucher())) {
					String[] docId = refundRecord.getPayVoucher().split(",");
					List<Long> docIdList = new ArrayList<Long>();
					for (int index = 0; index < docId.length; index++) {
						docIdList.add(Long.parseLong(docId[index]));
					}
					docInfos = docInfoService.getDocInfoByIds(docIdList);
				}

				refundRecord.setAttachments(DocInfoTransfer.transferDocInfos2JsonBeans(docInfos));
				
				List<Map<String, Object>> amountInfos = refundDao.getAllMoneyAmountsByBatchNumber(refundRecord.getPaymentObj_paymentUuid());
				if(CollectionUtils.isNotEmpty(amountInfos)) {
					//'付款总额'
					Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
					BigDecimal convertedTotalAmount = new BigDecimal(0);//'转换后金额'
					// amountInfos 是按币种ID和汇率进行分组，所以要对相同币种的金额求和 (美途国际是多汇率)
					for(Map<String, Object> amountInfo : amountInfos) {
						BigDecimal amount = map.get(amountInfo.get("currencyMark").toString());
						BigDecimal amountObj = amountInfo.get("amount")== null?new BigDecimal(0):
								new BigDecimal(amountInfo.get("amount").toString());
						if(null != amount){
							amount = amount.add(amountObj);
							map.put(amountInfo.get("currencyMark").toString(), amount);
						}else{
							map.put(amountInfo.get("currencyMark").toString(), amountObj);
						}
						convertedTotalAmount = convertedTotalAmount.add(new BigDecimal(amountInfo.get("amount").toString()).multiply(new BigDecimal(amountInfo.get("exchangerate").toString())));
					}
					StringBuffer paymentTotalAmount = new StringBuffer();
					for(Map.Entry<String, BigDecimal> entry : map.entrySet()){
						String key = entry.getKey();
						BigDecimal value = entry.getValue();
						paymentTotalAmount.append(key).append(value.toString()).append("+");
					}
					if(paymentTotalAmount.toString().length() > 0){
						paymentTotalAmount.delete(paymentTotalAmount.length()-1,paymentTotalAmount.length());
					}
					refundRecord.setPaymentTotalAmount(paymentTotalAmount.toString());
					refundRecord.setConvertedTotalAmount(Context.CURRENCY_MARK_RMB+new java.text.DecimalFormat("0.00").format(convertedTotalAmount));
				}
			}
		}
		return refundRecords;
	}

	/**
	 * add by songyang
	 * 合开支出单下载
	 */
	@Override
	public File createMtourMergePaySheetDownLoadFile(String inputParam,String fileName) throws IOException, TemplateException {
		JSONObject json = JSONObject.parseObject(inputParam);
		//订单ID
		String orderId  = json.getString("orderUuid");
		//付款对象UUID
		String paymentObjectUuid  = json.getString("paymentObjectUuid");
		//付款类型数组
		JSONArray fundsTypePayList = json.getJSONArray("fundsTypePayList");
		// word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		//日期集合
		List<Date>  listApplicantDate = new ArrayList<Date>();
		String applicantDateStr = "";
		Set<Object> pnr = new  HashSet<Object>();//PNR
		String pnrStr = ""; //PNR
		double BigTotalRMB = 0; //人民币 币种相加
		double BigTotalOther = 0; //外币币种相加
		Set<Object> purpose = new  HashSet<Object>();//用途
		String purposeStr = ""; //多用途拼接
		//出纳
		Set<Object> paymentPeople = new HashSet<Object>();
		String paymentPeopleStr = "";
		//流水号List
		List<String[]> listObtainSerialNumber = new ArrayList<String[]>();
		//订单团号
		AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		root.put("groupCode",airticketOrder.getGroupCode());
		Set<Object> createBy = new HashSet<Object>();//申请人
		for (int i = 0; i < fundsTypePayList.size(); i++) {
			JSONObject obj = fundsTypePayList.getJSONObject(i);
			if(UserUtils.isMtourUser()){
				try {
					if ("1".equals(obj.get("fundsType"))||"2".equals(obj.get("fundsType"))||"3".equals(obj.get("fundsType"))) {

						AirticketOrderMoneyAmount aoma = airticketOrderMoneyAmountService.getById(Integer.parseInt(obj.getString("paymentUuid")));

						// 日期：
						listApplicantDate.add(aoma.getCreateDate());

						// 支付对象：
						/**
						 * 如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
						 * 借款：1，退款：2，追加成本：3,成本付款：4
						 */
						if ("1".equals(obj.get("fundsType"))) {
							root.put("payObject", "");//'支付对象名称'
						}else if ("2".equals(obj.get("fundsType"))||"3".equals(obj.get("fundsType"))) {
							Agentinfo agentinfo = agentinfoService.findOne(airticketOrder.getAgentinfoId());
							if (null!=agentinfo) {
								root.put("payObject", agentinfo.getAgentName());//'支付对象名称'
							}else {
								root.put("payObject", "非签渠道");//'支付对象名称'
							}
						}else{
							root.put("payObject", "非签渠道");//'支付对象名称'
						}

						//用途
//						if("1".equals(obj.get("fundsType"))){
//							purpose.add("借款");//用途
//						}else if("2".equals(obj.get("fundsType"))){
//							purpose.add("退款");//用途
//						}else if("3".equals(obj.get("fundsType"))){
//							purpose.add("追加成本");//用途
//						}

						purpose.add(aoma.getFundsName());

						//流水号
//						String[] strObtainSerialNumber = {obj.getString("paymentUuid"),obj.get("fundsType").toString()};
						String[] strObtainSerialNumber = {obj.get("fundsType").toString(),obj.getString("paymentUuid")};
						listObtainSerialNumber.add(strObtainSerialNumber);

						//处理币种
						Currency currency = currencyDao.findById(aoma.getCurrencyId().longValue());
						Double amount = aoma.getAmount();
						amount = (null==amount)?0:amount;

						if (null!=currency) {
							if ("¥".equals(currency.getCurrencyMark())) {
								BigTotalRMB =  BigTotalRMB+amount;
								// 计人民币小写：
								root.put("rnbAmount",currency.getCurrencyMark()+fmtMicrometer(BigTotalRMB+""));
								// 计人民币大写：
								root.put("rnbAmountCN",MoneyAmountUtils.generUppercase(currency.getCurrencyName() ,BigTotalRMB));
								// 外币小写：
								root.put("totalOther","");
								// 外币大写：
								root.put("totalOtherCN","");
							}else{
								BigTotalOther = BigTotalOther + amount;
								// 计人民币小写：
								root.put("rnbAmount","");
								// 计人民币大写：
								root.put("rnbAmountCN","");
								// 外币小写：
								root.put("totalOther",currency.getCurrencyMark()+fmtMicrometer(BigTotalOther+""));
								// 外币大写：
								String totalother_cn = digitUppercase(BigTotalOther);
								root.put("totalOtherCN",MoneyAmountUtils.generUppercase(currency.getCurrencyName() ,BigTotalOther));
							}
						}

						// 申请人：
						createBy.add(aoma.getCreateBy());

						//添加出纳显示信息
						Integer moneyType = null;
						if(aoma.getMoneyType() == 1) {
							moneyType = Refund.MONEY_TYPE_BORROW;
						} else if(aoma.getMoneyType() == 2) {
							moneyType = Refund.MONEY_TYPE_RETURNMONEY;
						} else if(aoma.getMoneyType() == 3) {
							moneyType = Refund.MONEY_TYPE_ADDCOST;
						}
						List<Refund> refunds = refundService.getRefundsByRecordId(aoma.getId().longValue(), moneyType);
						StringBuffer sb = new StringBuffer();
						List<String> userIds = new ArrayList<String>();
						if(CollectionUtils.isNotEmpty(refunds)) {
							for(Refund refund : refunds) {
								if(!userIds.contains(refund.getCreateBy().getId().toString())) {
									sb.append(refund.getCreateBy().getName());
									sb.append(",");
									userIds.add(refund.getCreateBy().getId().toString());
								}
							}
							sb.deleteCharAt(sb.length()-1);
						}
						paymentPeople.add(sb.toString());


					} else {
						CostRecord costRecord = costManageService.findOne(Long.parseLong(obj.getString("paymentUuid")));

						pnr.add(costRecord.getBigCode());

						// 日期：
						listApplicantDate.add(costRecord.getCreateDate());

						// 支付对象：如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
						if (null!=costRecord.getSupplyName()) {
							root.put("payObject",costRecord.getSupplyName());
						}else{
							root.put("payObject","");
						}

						// 用途：如果是成本付款，则为成本支付；如果是借款，则为预付款；如果是追加成本，则为追加成本；如果是退款，则为退款
						purpose.add(costRecord.getName());

						//流水号
//				        String[] strObtainSerialNumber = {obj.getString("paymentUuid"),obj.get("fundsType").toString()};
						String[] strObtainSerialNumber = {obj.get("fundsType").toString(),obj.getString("paymentUuid")};
						listObtainSerialNumber.add(strObtainSerialNumber);

						//处理币种
						Currency currency = currencyDao.findById(costRecord.getCurrencyId().longValue());

						if(null!=currency){//处理币种为空的问题

							if ("¥".equals(currency.getCurrencyMark())) {
								BigTotalRMB =  BigTotalRMB+Double.parseDouble(costRecord.getPriceAfter().toString());
								// 计人民币小写：
								root.put("rnbAmount",(currency.getCurrencyMark()+fmtMicrometer(BigTotalRMB + "")));
								// 计人民币大写：
								root.put("rnbAmountCN",MoneyAmountUtils.generUppercase(currency.getCurrencyName() ,BigTotalRMB));
								// 外币小写：
								root.put("totalOther","");
								// 外币大写：
								root.put("totalOtherCN","");
							}else{
								Double amount = costRecord.getPrice().doubleValue()*costRecord.getQuantity();
								BigTotalOther = BigTotalOther+amount;
								// 计人民币小写：
								root.put("rnbAmount","");
								// 计人民币大写：
								root.put("rnbAmountCN","");
								// 外币小写：
								root.put("totalOther",(currency.getCurrencyMark()+fmtMicrometer(BigTotalOther+"")));
								// 外币大写：
								root.put("totalOtherCN",MoneyAmountUtils.generUppercase(currency.getCurrencyName() ,BigTotalOther));
							}
						}

						// 申请人：
						createBy.add(costRecord.getCreateBy().getId());
						//添加出纳显示信息0039需求
						List<Refund> refunds = refundService.getRefundsByRecordId(costRecord.getId(), Refund.MONEY_TYPE_COST);
						StringBuffer sb = new StringBuffer();
						List<String> userIds = new ArrayList<String>();
						if(CollectionUtils.isNotEmpty(refunds)) {
							for(Refund refund : refunds) {
								if(!userIds.contains(refund.getCreateBy().getId().toString())) {
									sb.append(refund.getCreateBy().getName());
									sb.append(",");
									userIds.add(refund.getCreateBy().getId().toString());
								}
							}
							sb.deleteCharAt(sb.length()-1);
						}
						paymentPeople.add(sb.toString());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}else{

				/**
				 * 当 fundsType为:借款：1，退款：2，追加成本：3，这三种类型时 取 airticket_order_moneyAmount 当
				 * fundsType为 :1,2,3以外的其它类型 取cost_record表数据
				 */
				try {
					if ("1".equals(obj.get("fundsType"))||"2".equals(obj.get("fundsType"))||"3".equals(obj.get("fundsType"))) {

						AirticketOrderMoneyAmount aoma = airticketOrderMoneyAmountService.getById(Integer.parseInt(obj.getString("paymentUuid")));

						// 日期：
						listApplicantDate.add(aoma.getCreateDate());

						// 支付对象：
						/**
						 * 如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
						 * 借款：1，退款：2，追加成本：3,成本付款：4
						 */
						if ("1".equals(obj.get("fundsType"))) {
							root.put("payObject", getWithSubString("", 28));//'支付对象名称'
						}else if ("2".equals(obj.get("fundsType"))||"3".equals(obj.get("fundsType"))) {
							Agentinfo agentinfo = agentinfoService.findOne(airticketOrder.getAgentinfoId());
							if (null!=agentinfo) {
								root.put("payObject", getWithSubString(agentinfo.getAgentName(), 28));//'支付对象名称'
							}else {
								root.put("payObject", getWithSubString("非签渠道", 28));//'支付对象名称'
							}
						}else{
							root.put("payObject", getWithSubString("非签渠道", 28));//'支付对象名称'
						}

						purpose.add(aoma.getFundsName());

						//流水号
						String[] strObtainSerialNumber = {obj.get("fundsType").toString(),obj.getString("paymentUuid")};
//						String[] strObtainSerialNumber = {obj.getString("paymentUuid"),obj.get("fundsType").toString()};
						listObtainSerialNumber.add(strObtainSerialNumber);

						//处理币种
						Currency currency = currencyDao.findById(aoma.getCurrencyId().longValue());
						Double amount = aoma.getAmount();
						amount = (null==amount)?0:amount;
						if ("¥".equals(currency.getCurrencyMark())) {
							BigTotalRMB =  BigTotalRMB+amount;
							// 计人民币小写：
							root.put("rnbAmount",getWithSubString(currency.getCurrencyMark()+fmtMicrometer(BigTotalRMB+""), 18));
							// 计人民币大写：
							root.put("rnbAmountCN",getWithSubString(digitUppercase(BigTotalRMB), 22));
							// 外币小写：
							root.put("totalOther",getWithSubString("", 18));
							// 外币大写：
							root.put("totalOtherCN",getWithSubString("", 22));
						}else{
							BigTotalOther = BigTotalOther+amount;
							// 计人民币小写：
							root.put("rnbAmount",getWithSubString("", 18));
							// 计人民币大写：
							root.put("rnbAmountCN",getWithSubString("", 22));
							// 外币小写：
							root.put("totalOther",getWithSubString(currency.getCurrencyMark()+fmtMicrometer(BigTotalOther+""), 18));
							// 外币大写：
							String totalother_cn = digitUppercase(BigTotalOther);
							totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
							root.put("totalOtherCN",getWithSubString(totalother_cn, 22));
						}

						// 申请人：
						createBy.add(aoma.getCreateBy());
						//添加出纳显示信息
						Integer moneyType = null;
						if(aoma.getMoneyType() == 1) {
							moneyType = Refund.MONEY_TYPE_BORROW;
						} else if(aoma.getMoneyType() == 2) {
							moneyType = Refund.MONEY_TYPE_RETURNMONEY;
						} else if(aoma.getMoneyType() == 3) {
							moneyType = Refund.MONEY_TYPE_ADDCOST;
						}
						List<Refund> refunds = refundService.getRefundsByRecordId(aoma.getId().longValue(), moneyType);
						StringBuffer sb = new StringBuffer();
						List<String> userIds = new ArrayList<String>();
						if(CollectionUtils.isNotEmpty(refunds)) {
							for(Refund refund : refunds) {
								if(!userIds.contains(refund.getCreateBy().getId().toString())) {
									sb.append(refund.getCreateBy().getName());
									sb.append(",");
									userIds.add(refund.getCreateBy().getId().toString());
								}
							}
							sb.deleteCharAt(sb.length()-1);
						}
						paymentPeople.add(sb.toString());

					} else {
						CostRecord costRecord = costManageService.findOne(Long.parseLong(obj.getString("paymentUuid")));
						// 日期：
						listApplicantDate.add(costRecord.getCreateDate());

						pnr.add(costRecord.getBigCode());

						// 支付对象：如果是成本付款，则取成本录入时的地接社/渠道商；如果是退款或追加成本付款，取该订单的渠道商；如果是借款，则为空
						if (null!=costRecord.getSupplyName()) {
							root.put("payObject",getWithSubString(costRecord.getSupplyName(), 28));
						}else{
							root.put("payObject",getWithSubString("", 28));
						}

						// 用途：如果是成本付款，则为成本支付；如果是借款，则为预付款；如果是追加成本，则为追加成本；如果是退款，则为退款
						purpose.add(getWithSubString(costRecord.getName(), 26));
						String[] strObtainSerialNumber = {obj.get("fundsType").toString(),obj.getString("paymentUuid")};
//						String[] strObtainSerialNumber = {obj.getString("paymentUuid"),obj.get("fundsType").toString()};
						listObtainSerialNumber.add(strObtainSerialNumber);

						//处理币种
						Currency currency = currencyDao.findById(costRecord.getCurrencyId().longValue());
						if ("¥".equals(currency.getCurrencyMark())) {
							BigTotalRMB =  BigTotalRMB+Double.parseDouble(costRecord.getPriceAfter().toString());
							// 计人民币小写：
							root.put("rnbAmount",getWithSubString((currency.getCurrencyMark()+fmtMicrometer(BigTotalRMB+"")),18));
							// 计人民币大写：
							root.put("rnbAmountCN",getWithSubString(digitUppercase(BigTotalRMB), 22));
							// 外币小写：
							root.put("totalOther",getWithSubString("", 18));
							// 外币大写：
							root.put("totalOtherCN",getWithSubString("", 22));
						}else{
							Double amount = costRecord.getPrice().doubleValue()*costRecord.getQuantity();
							BigTotalOther = BigTotalOther+amount;
							// 计人民币小写：
							root.put("rnbAmount",getWithSubString("", 18));
							// 计人民币大写：
							root.put("rnbAmountCN",getWithSubString("",22));
							// 外币小写：
							root.put("totalOther",getWithSubString((currency.getCurrencyMark()+fmtMicrometer(BigTotalOther+"")), 18));
							// 外币大写：
							String totalother_cn = digitUppercase(BigTotalOther);
							totalother_cn = totalother_cn.replace("元", currency.getCurrencyName());
							root.put("totalOtherCN",getWithSubString(totalother_cn, 22));
						}
						// 申请人：
						createBy.add(costRecord.getCreateBy());
						//添加出纳显示信息0039需求 add by majiancheng 2016-01-09
						List<Refund> refunds = refundService.getRefundsByRecordId(costRecord.getId(), Refund.MONEY_TYPE_COST);
						StringBuffer sb = new StringBuffer();
						List<String> userIds = new ArrayList<String>();
						if(CollectionUtils.isNotEmpty(refunds)) {
							for(Refund refund : refunds) {
								if(!userIds.contains(refund.getCreateBy().getId().toString())) {
									sb.append(refund.getCreateBy().getName());
									sb.append(",");
									userIds.add(refund.getCreateBy().getId().toString());
								}
							}
							sb.deleteCharAt(sb.length()-1);
						}
						paymentPeople.add(sb.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//获取最大时间
		ComparatorDate c = new ComparatorDate();
		Collections.sort(listApplicantDate, c);
		for (Object date : listApplicantDate) {
//			SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sFormat = new SimpleDateFormat(DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY);
			applicantDateStr= sFormat.format(date);
		}
		//日期
		root.put("applyDate", applicantDateStr);
		//出纳
		for (Object paymentPeoples : paymentPeople) {
			paymentPeopleStr+=paymentPeoples+",";
		}
		if(StringUtils.isNotEmpty(paymentPeopleStr)){
			root.put("paymentPeople",paymentPeopleStr.substring(0, paymentPeopleStr.length()-1));
		}else{
			root.put("paymentPeople","");
		}
		//pnr
		for (Object spnr : pnr) {
			pnrStr+=spnr+",";
		}
		if(StringUtils.isNotEmpty(pnrStr)){
			root.put("pnr", pnrStr.substring(0, pnrStr.length()-1));
		}else{
			root.put("pnr", "");
		}
		//申请人
		String createStr  = "";
		for (Object create : createBy) {
			createStr += UserUtils.getUserNameById(Long.parseLong(create.toString()));
		}
		root.put("applyPersonName",createStr);
		//用途
		for (Object pstr : purpose) {
			purposeStr += pstr+",";
		}
		if(StringUtils.isNotEmpty(purposeStr)){
			root.put("uses",purposeStr.substring(0, purposeStr.length()-1));
		}else{
			root.put("uses","");
		}
		//流水号
		String obtainSerialNumber = serialNumberService.obtainSerialNumber(listObtainSerialNumber);
		if(fundsTypePayList.size()>1){
			root.put("serialNum", obtainSerialNumber+"(合)");
		}else{
			root.put("serialNum", obtainSerialNumber);
		}

		return FreeMarkerUtil.generateFile("mtourpaysheet.ftl",fileName + ".doc", root);
	}

	/**
	 * add by wangyang
	 * 
	 * */
	@Override
	public List<SecondReceiveListJsonBean> getReceiveOrderSubList(String orderUuid) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT orderUuid,receiveUuid,arrivalBankDate,receiveDate,fundsName,receiveType,paymentCompany,receiver,agentorsypplyname,amount, ")
		   .append(" isAsAccount,currencyId,payedMoney,accountedmoney,receivedAmount,receiveFundsTypeCode,receiveFundsTypeName ")
		   .append(" FROM ( SELECT ao.id as orderUuid,pay.id as receiveUuid,pay.accountDate as arrivalBankDate,pay.createDate as receiveDate, ")
		   .append(" cost.NAME AS fundsName,5 AS receiveType,pay.payerName AS paymentCompany, ")
		   .append(" ( SELECT u.NAME FROM sys_user u WHERE u.id = pay.createBy ) AS receiver,cost.supplyName agentorsypplyname, ")
		   .append(" ma.amount,pay.isAsAccount,ma.currencyId,NULL AS payedMoney,ao.accounted_money AS accountedmoney, ")
		   .append(" ma.amount AS receivedAmount,'2' receiveFundsTypeCode,'其他收入收款' receiveFundsTypeName FROM ")
		   .append(" cost_record cost,pay_group pay,money_amount ma,activity_airticket p,airticket_order ao ")
		   .append(" WHERE pay.cost_record_id = cost.id AND pay.orderType = cost.orderType AND pay.payPrice = ma.serialNum ")
		   .append(" AND p.id = cost.activityId AND p.id = ao.airticket_id AND cost.budgetType = 2 AND cost.payStatus <> 2 ")
		   .append(" AND p.proCompany = ").append(companyId)
		   .append(" UNION ")
		   .append(" SELECT tmp.id orderUuid,tb.id receiveUuid,tb.accountDate AS arrivalBankDate,tb.createDate AS receiveDate, ")
		   .append(" NULL AS fundsName,tb.payPriceType AS receiveType,tb.payerName AS paymentCompany, ")
		   .append(" ( SELECT u.NAME FROM sys_user u WHERE u.id = tb.createBy ) AS receiver,tmp.agentName agentorsypplyname, ")
		   .append(" NULL AS amount,tb.isAsAccount,tb.currencyId AS currencyId,tb.moneySerialNum AS payedMoney,tmp.accounted_money AS accountedmoney, ")
		   .append(" tb.receivedAmount,'1' receiveFundsTypeCode,'订单收款' receiveFundsTypeName FROM ")
		   .append(" ( SELECT ao.accounted_money,ao.id id,ao.order_no orderNum,ao.nagentName agentName FROM airticket_order ao ")
		   .append(" LEFT JOIN  activity_airticket aa ON ao.airticket_id = aa.id WHERE aa.proCompany = ").append(companyId)
		   .append(" ) tmp,  ")
		   .append(" ( SELECT op.accountDate,op.createDate,op.payerName,op.id,op.orderNum,op.isAsAccount,op.payPriceType, ")
		   .append(" op.createBy,moneySerialNum,ma.amount receivedAmount,ma.currencyId FROM orderpay op,money_amount ma ")
		   .append(" WHERE op.moneySerialNum = ma.serialNum AND op.orderNum IS NOT NULL ) tb ")
		   .append(" WHERE tb.orderNum = tmp.orderNum ) list WHERE list.orderUuid = ").append(orderUuid);
		
		Query query = airticketOrderMoneyAmountDao.createSqlQuery(sql.toString())
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query.list();
		return copyParam2ReceiveSubList(list);
	}

	/**
	 * 将Map类型转换为SecondReceiveListJsonBean类型
	 * @author wangyang
	 * @date 2016-3-9
	 * */
	private List<SecondReceiveListJsonBean> copyParam2ReceiveSubList(List<Map<String, Object>> list) {
		
		List<SecondReceiveListJsonBean> receiveSubList = new ArrayList<SecondReceiveListJsonBean>();
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> tmp : list){
				SecondReceiveListJsonBean op = new SecondReceiveListJsonBean();
				
				op.setOrderUuid(tmp.get("orderUuid") != null ? tmp.get("orderUuid").toString() : "");
				op.setReceiveUuid(tmp.get("receiveUuid") != null ? tmp.get("receiveUuid").toString() : "");
				op.setReceiveDate(tmp.get("receiveDate") != null ? tmp.get("receiveDate").toString() : "");
				op.setArrivalBankDate(tmp.get("arrivalBankDate") != null ? tmp.get("arrivalBankDate").toString() : "");
				op.setFundsName(tmp.get("fundsName") != null ? tmp.get("fundsName").toString() : "");
				op.setReceiveTypeCode(tmp.get("receiveType") != null ? tmp.get("receiveType").toString() : "");
				op.setTourOperatorOrChannelName(tmp.get("agentorsypplyname") != null ? tmp.get("agentorsypplyname").toString() : "");
				op.setPaymentCompany(tmp.get("paymentCompany") != null ? tmp.get("paymentCompany").toString() : "");
				op.setReceiver(tmp.get("receiver") != null ? tmp.get("receiver").toString() : "");
				//收款状态
				if(tmp.get("isAsAccount") != null && "101".equals(tmp.get("isAsAccount").toString())){
					op.setReceiveStatusCode("0");
				}else if(tmp.get("isAsAccount")==null){ //订单收款orderpay表isasaccount = null 表示待确认
					op.setReceiveStatusCode("99");
				}else if(tmp.get("isAsAccount") != null && "102".equals(tmp.get("isAsAccount").toString())){
					op.setReceiveStatusCode("2");//成本其他收入，pay_group表isasaccount=102表示已驳回
				}else{
					op.setReceiveStatusCode(tmp.get("isAsAccount") != null ? tmp.get("isAsAccount").toString() : "");
				}
				// 收款类别 1全款，2尾款，3定金，4追散 （1-4是订单收款类别）5成本其他收入收款
				String receiveType = tmp.get("receiveType") != null ? tmp.get("receiveType").toString() : "0";
				Integer rt = Integer.valueOf(receiveType);
				if(rt == 1){
					op.setReceiveTypeName("定金");
				}else if(rt == 2){
					op.setReceiveTypeName("尾款");
				}else if(rt == 3){
					op.setReceiveTypeName("全款");
				}else if(rt == 4){
					op.setReceiveTypeName("追散");
				}else if(rt == 5){
					op.setReceiveTypeName("其他收入");
				}else{
					op.setReceiveTypeName("");
				}
				
				//收款类型
//				op.setReceiveFundsTypeCode(tmp.get("receiveFundsTypeCode") != null ? tmp.get("receiveFundsTypeCode").toString() : "");
//				op.setReceiveFundsType(tmp.get("receiveFundsTypeName") != null ? tmp.get("receiveFundsTypeName").toString() : "");
				op.setReceiveFundsType(new ReceiveFundsTypeVO(
						tmp.get("receiveFundsTypeCode") != null ? tmp.get("receiveFundsTypeCode").toString() : "", 
						tmp.get("receiveFundsTypeName") != null ? tmp.get("receiveFundsTypeName").toString() : ""));
				
				List<MoneyAmountVO> payMoneyList = new ArrayList<MoneyAmountVO>();
				List<MoneyAmountVO> arrivedAmount = new ArrayList<MoneyAmountVO>();

				// 达账金额serial
				String accountedmoneySerial = tmp.get("accountedmoney") != null ? tmp.get("accountedmoney").toString() : "";
				if (rt > 0 && rt < 5) {// 订单收款
					
					// 已收金额serial
					String payedMoneySerial = tmp.get("payedMoney") != null ? tmp.get("payedMoney").toString() : "";
					
					// 已收金额
					if (StringUtils.isNotBlank(payedMoneySerial)) {
						payMoneyList = getMoneyAmountVO(payedMoneySerial,false);
						op.setReceivedAmount(payMoneyList);
					}
					
					// 达账状态=1（已达账），已付金额=达账金额
					if (StringUtils.isNotBlank(op.getReceiveStatusCode()) && "1".equals(op.getReceiveStatusCode()) && StringUtils.isNotBlank(accountedmoneySerial)) {
						op.setArrivedAmount(payMoneyList);
					}
					
				} else {// 其他收入收款
						// 已收金额
					MoneyAmountVO ma = new MoneyAmountVO();
					
					if (tmp.get("amount") != null) {
						ma.setAmount(Double.valueOf(tmp.get("amount").toString()));
					}
					if (tmp.get("currencyId") != null) {
						ma.setCurrencyUuid(Integer.valueOf(tmp.get("currencyId").toString()));
					}
					payMoneyList.add(ma);
					
					// 达账金额
					String isAsAccount = tmp.get("isAsAccount") != null ? tmp.get("isAsAccount").toString() : "";
					if (StringUtils.isNotBlank(isAsAccount) && isAsAccount.equals("1")) {
						arrivedAmount.add(ma);
					}
					
					op.setReceivedAmount(payMoneyList);
					op.setArrivedAmount(arrivedAmount);
				}
				
//				// 累计达账金额
//				if (StringUtils.isNotBlank(accountedmoneySerial)) {
//					arrivedAmount = getMoneyAmountVO(accountedmoneySerial,true);
//					op.setReceivedAmount(arrivedAmount);
//				}
				receiveSubList.add(op);
			}
		}
		return receiveSubList;
	}

	@Override
	public Page receiveOrderList(ReceiveOrderListParam param) {

		if(null == param){
			return null;
		}
		Map<String, Object> dataMap = financeDao.receiveOrderList(param);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = (List<Map<String, Object>>)dataMap.get("data");
		List<ReceiveOrderListData> list = new ArrayList<ReceiveOrderListData>();
		if(CollectionUtils.isNotEmpty(dataList)){
			for (Map<String, Object> result: dataList) {
				ReceiveOrderListData data = new ReceiveOrderListData();
				//订单ID
				String orderUuid = String.valueOf(result.get("orderUuid")==null?"":result.get("orderUuid"));
				//团号
				String groupNo = String.valueOf(result.get("groupNo")==null?"":result.get("groupNo"));
				//下单时间
				String orderDateTime = String.valueOf(result.get("orderDateTime")==null?"":result.get("orderDateTime"));
				//出团日期
				String departureDate = String.valueOf(result.get("departureDate")==null?"":result.get("departureDate"));
				//下单人
				String orderer = String.valueOf(result.get("orderer")==null?"":result.get("orderer"));
				//订单总额
				String totalMoney = String.valueOf(result.get("totalMoney")==null?"":result.get("totalMoney"));
				//累计到账金额
				String accountedMoney = String.valueOf(result.get("accountedMoney")==null?"":result.get("accountedMoney"));
				//订单收款状态
				String paymentStatus = String.valueOf(result.get("paymentStatus")==null?"":result.get("paymentStatus"));
				data.setOrderUuid(orderUuid);
				data.setGroupNo(groupNo);
				data.setOrderDateTime(orderDateTime);
				data.setDepartureDate(departureDate);
				data.setOrderer(orderer);
				if(StringUtils.isNotBlank(totalMoney)){
					String thousandTotalMoney = MoneyNumberFormat.getThousandsByRegex(totalMoney, 2);
					data.setOrderAmount(Context.CURRENCY_MARK_RMB + thousandTotalMoney);
				}
				if(StringUtils.isNotBlank(accountedMoney)){
					String thousandAccountedMoney = MoneyNumberFormat.getThousandsByRegex(accountedMoney, 2);
					data.setTotalArriveAmount(Context.CURRENCY_MARK_RMB + thousandAccountedMoney);
				}
				data.setOrderReceiveStatusCodeStatusCode(paymentStatus);
				list.add(data);
			}
		}
		Long count = (Long)dataMap.get("count");
		Page page = new Page();
		//分页对象
		PageBean pageBean = new PageBean();
		pageBean.setTotalRowCount(count+"");
		pageBean.setCurrentIndex(param.getPageNow()+"");
		pageBean.setRowCount(param.getPageCount()+"");
		page.setResults(list);
		page.setPage(pageBean);
		return page;
	}

	@Override
	public List<FrontMoneyStatJsonBean> getFrontMoneyStat(String statDateBegin, String statDateEnd) {
		
		List<FrontMoneyStatJsonBean> bean = new ArrayList<FrontMoneyStatJsonBean>();
		//获取当前年月和年月上下限
//		String year = DateUtils.getYear();
//		String month = DateUtils.getMonth();
//		Integer nextMonth = Integer.valueOf(month) + 1;
//		String nextMonthStr;
//		if(nextMonth > 0 && nextMonth <= 9){
//			nextMonthStr = "0" + nextMonth;
//		}else{
//			nextMonthStr = nextMonth.toString();
//		}
		//设置时间间隔上下限
		if(StringUtils.isBlank(statDateBegin)){
			statDateBegin = "0000-00-00";
		}
		if(StringUtils.isBlank(statDateEnd)){
			statDateEnd = "9999-12-31";
		}
		
		//获取当前用户公司id
		Long companyId = UserUtils.getUser().getCompany().getId();
		//获取销售人员
		StringBuffer sqlSaler = new StringBuffer();
		sqlSaler.append("SELECT ao.create_by AS salerId, ( SELECT u.`name` FROM sys_user u WHERE u.id = ao.create_by ) AS salerName ")
				.append(" FROM airticket_order ao LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id ")
				.append(" WHERE aa.proCompany = ").append(companyId)
				.append(" AND ao.del_flag = 0 ")
				.append(" AND ao.lockstatus = 4 ")
				.append(" AND ao.create_date >= '").append(statDateBegin).append(" 00:00:00'")
				.append(" AND ao.create_date <= '").append(statDateEnd).append(" 23:59:59'")
				.append(" GROUP BY ao.create_by ");
		List<Map<String, Object>> salerlist = airticketOrderDao.createSqlQuery(sqlSaler.toString())
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		//添加销售
		if(CollectionUtils.isNotEmpty(salerlist)){
			for(Map<String, Object> saler : salerlist){
				FrontMoneyStatJsonBean fmsjb = new FrontMoneyStatJsonBean();
				fmsjb.setSalerId(saler.get("salerId") != null ? saler.get("salerId").toString() : null);
				fmsjb.setSalerName(saler.get("salerName") != null ? saler.get("salerName").toString() : null);
				bean.add(fmsjb);
			}
		}
		//获取统计内容
		StringBuffer sqlFront = new StringBuffer();
		sqlFront.append("SELECT salerId, salerName, orderNum, totalMoney, accountedMoney, frontMoney, frontAccountedMoney ")
				.append(" FROM ( SELECT ao.create_by AS salerId, ( SELECT u.`name` FROM sys_user u WHERE u.id = ao.create_by ) AS salerName, ")
				.append(" ao.order_no AS orderNum, ")
				.append(" ( SELECT SUM(ma.amount * ma.exchangerate) FROM money_amount ma WHERE ma.serialNum = ao.total_money AND ma.delFlag = 0 ) AS totalMoney, ")
				.append(" ( SELECT SUM(ma.amount * ma.exchangerate) FROM money_amount ma WHERE ma.serialNum = ao.accounted_money AND ma.delFlag = 0 ) AS accountedMoney, ")
				.append(" ( SELECT SUM(ma.amount * ma.exchangerate) FROM money_amount ma WHERE ma.serialNum = ao.front_money AND ma.delFlag = 0 ) AS frontMoney, ")
				.append(" ( SELECT SUM(ma.amount * ma.exchangerate) AS amount FROM money_amount ma, ")
				.append(" ( SELECT ao.id, o.moneySerialNum FROM orderpay o LEFT JOIN airticket_order ao ON o.orderNum = ao.order_no ")
				.append(" LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id ")
				.append(" WHERE o.payPriceType = 1 AND o.isAsAccount = 1 AND aa.proCompany = ").append(companyId)
				.append(" ) tmp ")
				.append(" WHERE ma.serialNum = tmp.moneySerialNum AND ma.delFlag = 0 AND tmp.id = ao.id ) AS frontAccountedMoney, ")
				.append(" aa.proCompany, ao.del_flag, ao.lockstatus, ao.create_date AS date ")
				.append(" FROM airticket_order ao LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id ) tt ")
				.append(" WHERE tt.proCompany = ").append(companyId)
				.append(" AND tt.del_flag = 0 ")
				.append(" AND tt.lockstatus = 4 ")
				.append(" AND tt.date >= '").append(statDateBegin).append(" 00:00:00'")
				.append(" AND tt.date <= '").append(statDateEnd).append(" 23:59:59'");
		Query query = airticketOrderDao.createSqlQuery(sqlFront.toString())
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query.list();
		
		if(CollectionUtils.isNotEmpty(list)){
			
			for(Map<String, Object> data : list){
				
				FrontMoneyStatData fmsd = new FrontMoneyStatData();
				//销售id
				String salerId = data.get("salerId") != null ? data.get("salerId").toString() : "";
				//销售名称
				String salerName = data.get("salerName") != null ? data.get("salerName").toString() : "";
				//团号
				String orderNum = data.get("orderNum") != null ? data.get("orderNum").toString() : "";
				//全款总额
				String totalMoney = data.get("totalMoney") != null ? data.get("totalMoney").toString() : "0";
				//到账总额
				String accountedMoney = data.get("accountedMoney") != null ? data.get("accountedMoney").toString() : "0";
				//订金总额
				String frontMoney = data.get("frontMoney") != null ? data.get("frontMoney").toString() : "0";
				//到账订金总额
				String frontAccountedMoney = data.get("frontAccountedMoney") != null ? data.get("frontAccountedMoney").toString() : "0";
				
				fmsd.setSalerId(salerId);
				fmsd.setSalerName(salerName);
				fmsd.setOrderNum(orderNum);
				fmsd.setTotalMoney(MoneyNumberFormat.getThousandsMoney(Double.valueOf(totalMoney), MoneyNumberFormat.THOUSANDST_POINT_TWO));
				fmsd.setFrontMoney(MoneyNumberFormat.getThousandsMoney(Double.valueOf(frontMoney), MoneyNumberFormat.THOUSANDST_POINT_TWO));
				fmsd.setNotAccountedTotalMoney(MoneyNumberFormat.getThousandsMoney(Double.valueOf(totalMoney) - Double.valueOf(accountedMoney), 
						MoneyNumberFormat.THOUSANDST_POINT_TWO));
				fmsd.setNotAccountFrontMoney(MoneyNumberFormat.getThousandsMoney(Double.valueOf(frontMoney) - Double.valueOf(frontAccountedMoney), 
						MoneyNumberFormat.THOUSANDST_POINT_TWO));
				
				if(StringUtils.isNotEmpty(salerId)){
					for(FrontMoneyStatJsonBean fmsjb : bean){
						if(fmsjb.getSalerId() != null && fmsjb.getSalerId().equals(salerId)){
							fmsjb.getOrders().add(fmsd);
						}
					}
				}
			}
		}
		
		return bean;
	}

	/**
	 * 获取指定年月下的，该公司的所有营业统计信息。包括订单团号，销售，订单总额，订单到账金额等。
	 * 然后根据销售人员的名称进行分组。	标志位isStr,true返回的bean中的字段是经过数字保留两位小数的String类型。
	 * false则字段是BigDecimal类型的。用于Excel数据填充。 yudong.xu 2016.6.17
	 */

	public Map<Integer,List<OperatingRevenueData>> getOperatingRevenueByDate(String startDate,String endDate,Boolean isStr){
		List<Map<String ,Object>> list = searchOperatingRevenueByDate(startDate,endDate);
		if (CollectionUtils.isEmpty(list)){
			return new HashMap<>();
		}
		Map<Integer,List<OperatingRevenueData>> result = new LinkedHashMap<>();
		for (Map<String, Object> map : list) {
			String orderId = map.get("orderId").toString();
			Integer salerId = (int)map.get("salerId");
			String salerName = (String)map.get("salerName");
			String groupCode = (String)map.get("groupCode");
			if (null == map.get("totalMoney") ){
				map.put("totalMoney",new BigDecimal(0));
			}
			if (null == map.get("accountedMoney")){
				map.put("accountedMoney",new BigDecimal(0));
			}
			BigDecimal totalMoney = (BigDecimal)map.get("totalMoney");//营业收入
			BigDecimal cost = getTotalCost(orderId).add(getAdditionalCost(orderId));//营业成本
			BigDecimal profit = totalMoney.subtract(cost);//毛利：营业收入 - 营业成本
			BigDecimal accountedMoney = (BigDecimal)map.get("accountedMoney");
			BigDecimal receivable = totalMoney.subtract(accountedMoney);//应收账款：营业收入 - 订单已到账总额
			OperatingRevenueData data = new OperatingRevenueData(salerId,salerName,groupCode,totalMoney,cost,profit,receivable);
			if (isStr) {
				data.convertToMoneyNumberFormat();
			}
			if (result.containsKey(salerId)){ //根据销售人员id对订单进行分组
				result.get(salerId).add(data);
			}else {
				List<OperatingRevenueData> newList = new ArrayList<>();
				newList.add(data);
				result.put(salerId,newList);
			}
		}
		return result;
	}

	private List<Map<String ,Object>> searchOperatingRevenueByDate(String startDate,String endDate){
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o.id AS orderId,u.`name` AS salerName,u.id AS salerId,a.group_code AS groupCode, ")
				.append("(SELECT ROUND(SUM(m.amount*IFNULL(m.exchangerate,0)),2) FROM money_amount m ")
				.append(" WHERE m.serialNum=o.total_money) AS totalMoney,")
				.append("(SELECT ROUND(SUM(m.amount*IFNULL(m.exchangerate,0)),2) FROM money_amount m ")
				.append(" WHERE m.serialNum=o.accounted_money) AS accountedMoney ")
				.append(" FROM airticket_order o,activity_airticket a,sys_user u WHERE o.airticket_id=a.id")
				.append(" AND o.create_by=u.id AND a.proCompany=? ");
		if (StringUtils.isNotBlank(startDate)){
			sql.append(" AND o.create_date >='").append(startDate).append(" 00:00:00'");
		}
		if (StringUtils.isNotBlank(endDate)){
			sql.append(" AND o.create_date <='").append(endDate).append(" 23:59:59'");
		}
		return  financeDao.findBySql(sql.toString(),Map.class,companyId);
	}

	/**
	 * 获取该订单的所有成本之和。 yudong.xu 2016.6.17
	 */
	private BigDecimal getTotalCost(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(SUM(aopa.personNum*aopa.price*aopa.exchangerate),2) AS totalCost")
				.append(" FROM airticket_order_pnr aop LEFT JOIN airticket_order_pnr_airline_price aopa ")
				.append(" ON aopa.airticket_order_pnr_uuid = aop.uuid WHERE aopa.delFlag=0 AND aop.delFlag =0")
				.append(" AND aopa.price_type=0 AND aop.airticket_order_id=?");
		List<Map<String,Object>> result = financeDao.findBySql(sql.toString(),Map.class,orderId);
		if (CollectionUtils.isNotEmpty(result)){
			Object totalCost = result.get(0).get("totalCost");
			if(null != totalCost)
				return (BigDecimal)totalCost;
		}
		return new BigDecimal(0);
	}

	/**
	 * 获取该订单的所有追加成本之和(未撤销的)。 yudong.xu 2016.6.17
	 */
	private BigDecimal getAdditionalCost(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND(SUM(ma.amount*IFNULL(ma.exchangerate,0)),2) AS additionalCost ")
				.append(" FROM airticket_order_moneyAmount ma WHERE ma.airticket_order_id=? ")
				.append(" AND ma.`status`=1 AND ma.moneyType = 3 AND ma.delFlag=0 ");
		List<Map<String,Object>> result = financeDao.findBySql(sql.toString(),Map.class,orderId);
		if (CollectionUtils.isNotEmpty(result)){
			Object additionalCost = result.get(0).get("additionalCost");
			if (null != additionalCost)
				return (BigDecimal)additionalCost;
		}
		return new BigDecimal(0);
	}

	@Override
	public List<Map<String, Object>> getBatchIncomeInfoByInput(String orderId,String csIds,String oterIds) {
		StringBuffer sbf=new StringBuffer();
		//查询中包含团号，兹有，制单人
		sbf.append("SELECT c.groupNo AS groupNo,c.agentName AS fromInfo,c.createBy AS incomeAloner,GROUP_CONCAT(c.payPriceType) AS payPriceType,sum(c.amount) AS amount,c.currencyName,c.currencyMark  FROM( ");
		sbf.append("SELECT ");
		sbf.append("GROUP_CONCAT(DISTINCT su.name) AS createBy, ");
		sbf.append("ao.agentinfo_id AS agentId, ");
		sbf.append("ao.group_code AS groupNo, ");
		sbf.append("ao.nagentName AS agentName, ");
		sbf.append("GROUP_CONCAT(DISTINCT op.payPriceType) AS payPriceType, ");
		sbf.append("SUM(ma.amount) AS amount, ");
		sbf.append("cy.currency_name AS currencyName,cy.currency_mark AS currencyMark,cy.currency_id AS currencyId ");
		sbf.append("FROM airticket_order ao ");
		sbf.append("LEFT JOIN orderpay op ON ao.id=op.orderId ");
		sbf.append("LEFT JOIN money_amount ma ON ma.serialNum = op.moneySerialNum ");
		sbf.append("LEFT JOIN sys_user su ON su.id=op.createBy ");
		sbf.append("LEFT JOIN currency cy ON cy.currency_id=ma.currencyId ");
		sbf.append("WHERE ao.id=? AND op.orderType=? AND op.delFlag=? AND op.id IN(");
		if(StringUtils.isNotEmpty(csIds)){
			sbf.append(csIds);
		}else{
			sbf.append("''");
		}
		sbf.append(") AND (op.isAsAccount is NULL OR op.isAsAccount=99 OR op.isAsAccount=1) " );
		sbf.append("GROUP BY ao.group_code,ma.currencyId,ao.agentinfo_id ");
		sbf.append("UNION ");
		sbf.append("SELECT ");
		sbf.append("GROUP_CONCAT(DISTINCT su.name) AS createBy, ");
		sbf.append("cr.supplyId AS agentId, ");
		sbf.append("ao.group_code AS groupNo, ");
		sbf.append("cr.supplyName AS agentName, ");
		sbf.append("5 AS payPriceType, ");
		sbf.append("SUM(cr.price) AS amount, ");
		sbf.append("cy.currency_name AS currencyName,cy.currency_mark AS currencyMark,cy.currency_id AS currencyId ");
		sbf.append("FROM airticket_order ao ");
		sbf.append("LEFT JOIN cost_record cr ON cr.orderId=ao.id ");
		sbf.append("LEFT JOIN pay_group pg ON cr.id=pg.cost_record_id ");
		sbf.append("LEFT JOIN sys_user su ON su.id=pg.createBy ");
		sbf.append("LEFT JOIN currency cy ON cy.currency_id=cr.currencyId ");
		sbf.append("WHERE ao.id=? AND pg.orderType=? AND cr.delFlag=? AND pg.id IN (");
		if(StringUtils.isNotEmpty(oterIds)){
			sbf.append(oterIds);
		}else{
			sbf.append("''");
		}
		sbf.append(")  ");
		sbf.append("GROUP BY ao.group_code,cr.currencyId,cr.supplyId ");
		sbf.append(")c ");
		sbf.append("GROUP BY c.groupNo,c.currencyId,c.agentId ");
		List<Map<String,Object>> list=financeDao.findBySql(sbf.toString(),Map.class, Integer.parseInt(orderId),7,0,Integer.parseInt(orderId),7,0);
		for(Map<String,Object> map:list){
			String[] payPriceTypes = map.get("payPriceType").toString().split(",");
			String toInfo="";
			for(String payPriceType:payPriceTypes){
				if(payPriceType.equals("1")){
					if(toInfo.equals("")){
						toInfo="全款";
					}else{
						toInfo="全款、"+toInfo;
					}
				}else if(payPriceType.equals("2")){
					if(toInfo.equals("")){
						toInfo="尾款";
					}else{
						toInfo="尾款、"+toInfo;
					}
				}else if(payPriceType.equals("3")){
					if(toInfo.equals("")){
						toInfo="订金";
					}else{
						toInfo="订金、"+toInfo;
					}
				}else if(payPriceType.equals("4")){
					if(toInfo.equals("")){
						toInfo="追散";
					}else{
						toInfo="追散、"+toInfo;
					}
				}else if(payPriceType.equals("5")){
					if(toInfo.equals("")){
						toInfo="其他收款";
					}else{
						toInfo="其他收款、"+toInfo;
					}
				}
			}
			//交来
			map.put("toInfo", toInfo);
			String totalRMB_CN="";
			String totalOther_CN="";
			DecimalFormat d = new DecimalFormat(",##0.00");// 人民币显示格式
			if(map.get("currencyName").toString().equals("人民币")){
				map.put("totalRMB", "¥"+d.format(Double.parseDouble(map.get("amount").toString())));//计人民币
				totalRMB_CN = MoneyAmountUtils.generUppercase(map.get("currencyName").toString(),Double.parseDouble(map.get("amount").toString()));
				map.put("totalRMB_CN", totalRMB_CN);//'计人民币(大写)'
				map.put("totalOther", "");//'外币'
				map.put("totalOther_CN", "");//外币（大写）
			}else{
				map.put("totalRMB","");
				map.put("totalRMB_CN", "");//'计人民币(大写)'
				map.put("totalOther", map.get("currencyMark")+d.format(Double.parseDouble(map.get("amount").toString())));//'外币'
				totalOther_CN = MoneyAmountUtils.generUppercase(map.get("currencyName").toString(),Double.parseDouble(map.get("amount").toString()));
				map.put("totalOther_CN", totalOther_CN);//外币（大写）
			}
			//审核人
			map.put("approver", UserUtils.getUser().getName());
			//日期
			map.put("applicantDate", DateUtils.date2String(new Date(), DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY));
		}
		return list;
	}



	/**
	 * 解析前端传来的json参数，根据订单id,支付对象id,币种id这3个变量来产生bean.只有3个都相同才放入一个bean。
	 * yudong.xu 2016.7.7
	 */
	private List<PaymentParamsJsonBean> parseParamsPaySheet(String params){
		JSONArray payArray = JSONObject.parseArray(params);

		List<PaymentParamsJsonBean> result = new ArrayList<>();//存放最终的整理好的bean。
		for (int i = 0; i < payArray.size(); i++) {
			JSONObject pay = payArray.getJSONObject(i);
			Map<String,Map<String,PaymentParamsJsonBean>> payObjectGroup = new HashMap<>();//支付对象分组

			String orderUuid = pay.getString("orderUuid");
			String groupNo = pay.getString("groupNo");
			JSONArray payItems = pay.getJSONArray("paymentList");
			for (int j = 0; j < payItems.size(); j++) {
				JSONObject payItem = payItems.getJSONObject(j);
				String objectUuid = payItem.getString("paymentObjectUuid");
				String objectName = payItem.getString("paymentObjectName");
				String currencyId = payItem.getString("currencyId");

				String paymentUuid = payItem.getString("paymentUuid");
				String fundsType = payItem.getString("fundsType");
				PaymentParamsItem item = new PaymentParamsItem(paymentUuid,fundsType);
				if (payObjectGroup.containsKey(objectUuid)){//该订单已存在这个支付对象
					//获取该支付对象下的货币分组。currencyGroup
					Map<String,PaymentParamsJsonBean> currencyGroup = payObjectGroup.get(objectUuid);
					if (currencyGroup.containsKey(currencyId)){//该支付对象也存在这个货币，就获取原有的bean添加item。
						currencyGroup.get(currencyId).getFundsTypePayList().add(item);
					}else {//该对象没有这个货币，需要重新创建这个bean。并放入该支付对象的货币分组中。
						List<PaymentParamsItem> itemList = new ArrayList<>();
						itemList.add(item);
						PaymentParamsJsonBean bean = new PaymentParamsJsonBean(orderUuid,groupNo,objectUuid,objectName,itemList);
						currencyGroup.put(currencyId,bean);
						//只要创建新的bean，就放入到结果result中。创建bean是由订单id,支付对象id,货币id这3个组合决定。
						result.add(bean);
					}
				}else {
					Map<String,PaymentParamsJsonBean> currencyGroup = new HashMap<>();
					List<PaymentParamsItem> itemList = new ArrayList<>();
					itemList.add(item);
					PaymentParamsJsonBean bean = new PaymentParamsJsonBean(orderUuid,groupNo,objectUuid,objectName,itemList);
					currencyGroup.put(currencyId,bean);
					result.add(bean);
					payObjectGroup.put(objectUuid,currencyGroup);
				}
			}
		}
		return result;
	}

	/**
	 * 获取支出单文件列表。用于批量下载支出单。 yudong.xu 2016.7.7
     */
	public List<File> getPaySheetFileList(String params) throws Exception {
		if (StringUtils.isBlank(params)) return null;
		List<PaymentParamsJsonBean> result = parseParamsPaySheet(params);
		Map<String,Integer> resource = new HashMap<>();
		List<File> fileList = new ArrayList<>();
		for (PaymentParamsJsonBean bean : result) {
			String jsonStr = JSON.toJSONString(bean);
			String name = bean.getGroupNo()+bean.getPaymentObjectName(); //团号+支付对象 作为文件名称。
			name = CommonUtils.checkName(resource,name);
			File file = createMtourMergePaySheetDownLoadFile(jsonStr,name);
			fileList.add(file);
		}
		return fileList;
	}
}
