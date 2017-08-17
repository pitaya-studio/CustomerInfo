/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.island.dao.IslandMoneyAmountDao;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.pay.dao.PayIslandOrderDao;
import com.trekiz.admin.modules.pay.entity.PayIslandOrder;
import com.trekiz.admin.modules.pay.input.PayIslandOrderInput;
import com.trekiz.admin.modules.pay.query.PayIslandOrderQuery;
import com.trekiz.admin.modules.pay.service.PayIslandOrderService;
import com.trekiz.admin.modules.pay.utils.PayUtils;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class PayIslandOrderServiceImpl extends BaseService implements PayIslandOrderService{
	@Autowired
	private PayIslandOrderDao payIslandOrderDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private IslandOrderDao islandOrderDao;
	@Autowired
	private IslandMoneyAmountDao islandMoneyAmountDao;
	public void save (PayIslandOrder payIslandOrder){
		super.setOptInfo(payIslandOrder, BaseService.OPERATION_ADD);
		payIslandOrderDao.saveObj(payIslandOrder);
	}
	
	public void save (PayIslandOrderInput payIslandOrderInput){
		PayIslandOrder payIslandOrder = payIslandOrderInput.getPayIslandOrder();
		super.setOptInfo(payIslandOrder, BaseService.OPERATION_ADD);
		payIslandOrderDao.saveObj(payIslandOrder);
	}
	
	public void update (PayIslandOrder payIslandOrder){
		super.setOptInfo(payIslandOrder, BaseService.OPERATION_UPDATE);
		payIslandOrderDao.updateObj(payIslandOrder);
	}
	
	public PayIslandOrder getById(java.lang.Integer value) {
		return payIslandOrderDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PayIslandOrder obj = payIslandOrderDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	

	public Page<PayIslandOrder> find(Page<PayIslandOrder> page, PayIslandOrderQuery payIslandOrderQuery) {
		DetachedCriteria dc = payIslandOrderDao.createDetachedCriteria();
		
	   	if(payIslandOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payIslandOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payIslandOrderQuery.getUuid()));
		}
	   	if(payIslandOrderQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", payIslandOrderQuery.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayTypeName())){
			dc.add(Restrictions.eq("payTypeName", payIslandOrderQuery.getPayTypeName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayerName())){
			dc.add(Restrictions.eq("payerName", payIslandOrderQuery.getPayerName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getCheckNumber())){
			dc.add(Restrictions.eq("checkNumber", payIslandOrderQuery.getCheckNumber()));
		}
		if(payIslandOrderQuery.getInvoiceDate()!=null){
			dc.add(Restrictions.eq("invoiceDate", payIslandOrderQuery.getInvoiceDate()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayVoucher())){
			dc.add(Restrictions.eq("payVoucher", payIslandOrderQuery.getPayVoucher()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getRemarks())){
			dc.add(Restrictions.eq("remarks", payIslandOrderQuery.getRemarks()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPosNo())){
			dc.add(Restrictions.eq("posNo", payIslandOrderQuery.getPosNo()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPosTagEendNo())){
			dc.add(Restrictions.eq("posTagEendNo", payIslandOrderQuery.getPosTagEendNo()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPosBank())){
			dc.add(Restrictions.eq("posBank", payIslandOrderQuery.getPosBank()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getBankName())){
			dc.add(Restrictions.eq("bankName", payIslandOrderQuery.getBankName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getToBankNname())){
			dc.add(Restrictions.eq("toBankNname", payIslandOrderQuery.getToBankNname()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getBankAccount())){
			dc.add(Restrictions.eq("bankAccount", payIslandOrderQuery.getBankAccount()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getToBankAccount())){
			dc.add(Restrictions.eq("toBankAccount", payIslandOrderQuery.getToBankAccount()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getFromCompanyName())){
			dc.add(Restrictions.eq("fromCompanyName", payIslandOrderQuery.getFromCompanyName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getFromBankName())){
			dc.add(Restrictions.eq("fromBankName", payIslandOrderQuery.getFromBankName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getFromAccount())){
			dc.add(Restrictions.eq("fromAccount", payIslandOrderQuery.getFromAccount()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payIslandOrderQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payIslandOrderQuery.getReceiveAccount()));
		}
		if(payIslandOrderQuery.getDraftAccountedDate()!=null){
			dc.add(Restrictions.eq("draftAccountedDate", payIslandOrderQuery.getDraftAccountedDate()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayPrice())){
			dc.add(Restrictions.eq("payPrice", payIslandOrderQuery.getPayPrice()));
		}
	   	if(payIslandOrderQuery.getPayPriceType()!=null){
	   		dc.add(Restrictions.eq("payPriceType", payIslandOrderQuery.getPayPriceType()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", payIslandOrderQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayPriceBack())){
			dc.add(Restrictions.eq("payPriceBack", payIslandOrderQuery.getPayPriceBack()));
		}
	   	if(payIslandOrderQuery.getIsAsAccount()!=null){
	   		dc.add(Restrictions.eq("isAsAccount", payIslandOrderQuery.getIsAsAccount()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getOldPayPrice())){
			dc.add(Restrictions.eq("oldPayPrice", payIslandOrderQuery.getOldPayPrice()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getFastPayType())){
			dc.add(Restrictions.eq("fastPayType", payIslandOrderQuery.getFastPayType()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getOrderNum())){
			dc.add(Restrictions.eq("orderNum", payIslandOrderQuery.getOrderNum()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getMoneySerialNum())){
			dc.add(Restrictions.eq("moneySerialNum", payIslandOrderQuery.getMoneySerialNum()));
		}
		if(payIslandOrderQuery.getPrintTime()!=null){
			dc.add(Restrictions.eq("printTime", payIslandOrderQuery.getPrintTime()));
		}
	   	if(payIslandOrderQuery.getPrintFlag()!=null){
	   		dc.add(Restrictions.eq("printFlag", payIslandOrderQuery.getPrintFlag()));
	   	}
	   	if(payIslandOrderQuery.getPaymentStatus()!=null){
	   		dc.add(Restrictions.eq("paymentStatus", payIslandOrderQuery.getPaymentStatus()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getTravelerUuid())){
			dc.add(Restrictions.eq("travelerUuid", payIslandOrderQuery.getTravelerUuid()));
		}
	   	if(payIslandOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payIslandOrderQuery.getCreateBy()));
	   	}
		if(payIslandOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payIslandOrderQuery.getCreateDate()));
		}
	   	if(payIslandOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payIslandOrderQuery.getUpdateBy()));
	   	}
		if(payIslandOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payIslandOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payIslandOrderQuery.getDelFlag()));
		}
		if(payIslandOrderQuery.getAccountDate()!=null){
			dc.add(Restrictions.eq("accountDate", payIslandOrderQuery.getAccountDate()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payIslandOrderDao.find(page, dc);
	}
	
	public List<PayIslandOrder> find( PayIslandOrderQuery payIslandOrderQuery) {
		DetachedCriteria dc = payIslandOrderDao.createDetachedCriteria();
		
	   	if(payIslandOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payIslandOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payIslandOrderQuery.getUuid()));
		}
	   	if(payIslandOrderQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", payIslandOrderQuery.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayTypeName())){
			dc.add(Restrictions.eq("payTypeName", payIslandOrderQuery.getPayTypeName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayerName())){
			dc.add(Restrictions.eq("payerName", payIslandOrderQuery.getPayerName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getCheckNumber())){
			dc.add(Restrictions.eq("checkNumber", payIslandOrderQuery.getCheckNumber()));
		}
		if(payIslandOrderQuery.getInvoiceDate()!=null){
			dc.add(Restrictions.eq("invoiceDate", payIslandOrderQuery.getInvoiceDate()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayVoucher())){
			dc.add(Restrictions.eq("payVoucher", payIslandOrderQuery.getPayVoucher()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getRemarks())){
			dc.add(Restrictions.eq("remarks", payIslandOrderQuery.getRemarks()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPosNo())){
			dc.add(Restrictions.eq("posNo", payIslandOrderQuery.getPosNo()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPosTagEendNo())){
			dc.add(Restrictions.eq("posTagEendNo", payIslandOrderQuery.getPosTagEendNo()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPosBank())){
			dc.add(Restrictions.eq("posBank", payIslandOrderQuery.getPosBank()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getBankName())){
			dc.add(Restrictions.eq("bankName", payIslandOrderQuery.getBankName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getToBankNname())){
			dc.add(Restrictions.eq("toBankNname", payIslandOrderQuery.getToBankNname()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getBankAccount())){
			dc.add(Restrictions.eq("bankAccount", payIslandOrderQuery.getBankAccount()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getToBankAccount())){
			dc.add(Restrictions.eq("toBankAccount", payIslandOrderQuery.getToBankAccount()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getFromCompanyName())){
			dc.add(Restrictions.eq("fromCompanyName", payIslandOrderQuery.getFromCompanyName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getFromBankName())){
			dc.add(Restrictions.eq("fromBankName", payIslandOrderQuery.getFromBankName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getFromAccount())){
			dc.add(Restrictions.eq("fromAccount", payIslandOrderQuery.getFromAccount()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payIslandOrderQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payIslandOrderQuery.getReceiveAccount()));
		}
		if(payIslandOrderQuery.getDraftAccountedDate()!=null){
			dc.add(Restrictions.eq("draftAccountedDate", payIslandOrderQuery.getDraftAccountedDate()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayPrice())){
			dc.add(Restrictions.eq("payPrice", payIslandOrderQuery.getPayPrice()));
		}
	   	if(payIslandOrderQuery.getPayPriceType()!=null){
	   		dc.add(Restrictions.eq("payPriceType", payIslandOrderQuery.getPayPriceType()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", payIslandOrderQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getPayPriceBack())){
			dc.add(Restrictions.eq("payPriceBack", payIslandOrderQuery.getPayPriceBack()));
		}
	   	if(payIslandOrderQuery.getIsAsAccount()!=null){
	   		dc.add(Restrictions.eq("isAsAccount", payIslandOrderQuery.getIsAsAccount()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getOldPayPrice())){
			dc.add(Restrictions.eq("oldPayPrice", payIslandOrderQuery.getOldPayPrice()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getFastPayType())){
			dc.add(Restrictions.eq("fastPayType", payIslandOrderQuery.getFastPayType()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getOrderNum())){
			dc.add(Restrictions.eq("orderNum", payIslandOrderQuery.getOrderNum()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getMoneySerialNum())){
			dc.add(Restrictions.eq("moneySerialNum", payIslandOrderQuery.getMoneySerialNum()));
		}
		if(payIslandOrderQuery.getPrintTime()!=null){
			dc.add(Restrictions.eq("printTime", payIslandOrderQuery.getPrintTime()));
		}
	   	if(payIslandOrderQuery.getPrintFlag()!=null){
	   		dc.add(Restrictions.eq("printFlag", payIslandOrderQuery.getPrintFlag()));
	   	}
	   	if(payIslandOrderQuery.getPaymentStatus()!=null){
	   		dc.add(Restrictions.eq("paymentStatus", payIslandOrderQuery.getPaymentStatus()));
	   	}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getTravelerUuid())){
			dc.add(Restrictions.eq("travelerUuid", payIslandOrderQuery.getTravelerUuid()));
		}
	   	if(payIslandOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payIslandOrderQuery.getCreateBy()));
	   	}
		if(payIslandOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payIslandOrderQuery.getCreateDate()));
		}
	   	if(payIslandOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payIslandOrderQuery.getUpdateBy()));
	   	}
		if(payIslandOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payIslandOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payIslandOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payIslandOrderQuery.getDelFlag()));
		}
		if(payIslandOrderQuery.getAccountDate()!=null){
			dc.add(Restrictions.eq("accountDate", payIslandOrderQuery.getAccountDate()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payIslandOrderDao.find(dc);
	}
	
	public PayIslandOrder getByUuid(String uuid) {
		return payIslandOrderDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PayIslandOrder payIslandOrder = getByUuid(uuid);
		payIslandOrder.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(payIslandOrder);
	}
	
	/**
     * 根据订单ids查询支付订单
     * @param orderIds
     * @return
     */
    public List<PayIslandOrder> findOrderPayByOrderUuids(List<String> uuids) {
    	DetachedCriteria dc = payIslandOrderDao.createDetachedCriteria();
    	dc.add(Restrictions.eq("delFlag", Context.DEL_FLAG_NORMAL));
    	dc.add(Restrictions.in("orderUuid", uuids));
    	return payIslandOrderDao.find(dc);
     }
    
    public List<PayIslandOrder> findLastDateOrderPay(String orderPayId) {
    	DetachedCriteria dc = payIslandOrderDao.createDetachedCriteria();
    	dc.add(Restrictions.eq("orderUuid", orderPayId));
    	dc.add(Restrictions.eq("delFlag", Context.DEL_FLAG_NORMAL));
    	dc.add(Restrictions.isNotNull("isAsAccount"));
    	dc.addOrder(Order.desc("updateDate"));
    	return payIslandOrderDao.find(dc);
    }
    
    public boolean saveByOrderPayForm(OrderPayForm orderPayForm) throws Exception {
    	OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();

		for (int i = 0; i < orderPayDetailList.size(); i++) {

			OrderPayDetail orderPayDetail = orderPayDetailList.get(i);

			PayIslandOrder payIslandOrder = new PayIslandOrder();
			// 支付方式
			payIslandOrder.setPayType(orderPayForm.getPayType());
			Integer paymentStatus = orderPayForm.getPaymentStatus();
			if (paymentStatus == null) {
				paymentStatus = 1;
			}
			payIslandOrder.setPaymentStatus(paymentStatus);
			payIslandOrder.setPayTypeName(PayUtils.getPayTypeName(paymentStatus, orderPayForm.getPayType()));

			// 即时支付场合
			if (paymentStatus == 1) {
				// 来款单位
				if (StringUtils.isNotEmpty(orderPayForm.getPayerName())) {
					payIslandOrder.setPayerName(orderPayForm.getPayerName());
				}

				switch (orderPayForm.getPayType()) {
				case 1:// 支票支付
						// 支票号
					if (StringUtils.isNotEmpty(orderPayForm.getCheckNumber())) {
						payIslandOrder.setCheckNumber(orderPayForm.getCheckNumber());
					}
					// 开票日期
					if (orderPayForm.getInvoiceDate() != null && StringUtils.isNotEmpty(orderPayForm.getInvoiceDate().toString())) {
						payIslandOrder.setInvoiceDate(orderPayForm.getInvoiceDate());
					}
					break;
				case 3:// 现金支付
					break;
				case 4:// 汇款
						// 开户行名称
					if (StringUtils.isNotEmpty(orderPayForm.getBankName())) {
						payIslandOrder.setBankName(orderPayForm.getBankName());
					}
					// 开户行账户
					if (StringUtils.isNotEmpty(orderPayForm.getBankAccount())) {
						payIslandOrder.setBankAccount(orderPayForm.getBankAccount());
					}
					// 转入行名称
					if (StringUtils.isNotEmpty(orderPayForm.getToBankNname())) {
						payIslandOrder.setToBankNname(orderPayForm.getToBankNname());
					}
					// 转入行账号
					if (StringUtils.isNotEmpty(orderPayForm.getToBankAccount())) {
						payIslandOrder.setToBankAccount(orderPayForm.getToBankAccount());
					}
					break;
				case 5:// 快速支付
						// 支付方式
					if (StringUtils.isNotEmpty(orderPayForm.getFastPayType())) {
						payIslandOrder.setFastPayType(orderPayForm.getFastPayType());
					}
					break;
				}

				//多个支付方式设置值--------
				//付款单位
				if(StringUtils.isNotEmpty(orderPayForm.getPayerName())) {
					payIslandOrder.setPayerName(orderPayForm.getPayerName());
				}
				// 开户行名称
				if (StringUtils.isNotEmpty(orderPayForm.getBankName())) {
					payIslandOrder.setBankName(orderPayForm.getBankName());
				}
				// 开户行账户
				if (StringUtils.isNotEmpty(orderPayForm.getBankAccount())) {
					payIslandOrder.setBankAccount(orderPayForm.getBankAccount());
				}
				// 转入行名称
				if (StringUtils.isNotEmpty(orderPayForm.getToBankNname())) {
					payIslandOrder.setToBankNname(orderPayForm.getToBankNname());
				}
				// 转入行账号
				if (StringUtils.isNotEmpty(orderPayForm.getToBankAccount())) {
					payIslandOrder.setToBankAccount(orderPayForm.getToBankAccount());
				}
				//汇票到账日期
				if(orderPayForm.getDraftAccountedDate() != null) {
					payIslandOrder.setDraftAccountedDate(orderPayForm.getDraftAccountedDate());
				}
				//多个支付方式设置值--------
				
				// 保存支付订单金额
				String moneyUuid = UuidUtils.generUuid().toString();
				payIslandOrder.setMoneySerialNum(moneyUuid);
				orderPayDetail.setMoneySerialNum(moneyUuid);

				// 备注信息
				if (StringUtils.isNotEmpty(orderPayForm.getRemarks())) {
					payIslandOrder.setRemarks(orderPayForm.getRemarks());
				}

				/*
				 * 其他和支付无直接关系，各模块相关的业务字段
				 */
				// 订单id
				payIslandOrder.setOrderUuid(orderPayDetail.getOrderUuid());
				// 游客id
				payIslandOrder.setTravelerUuid(orderPayDetail.getTravelerUuid());
				// 订单号
				payIslandOrder.setOrderNum(orderPayDetail.getOrderNum());
				// 支付款类型（全款、定金、尾款）
				payIslandOrder.setPayPriceType(orderPayDetail.getPayPriceType());
			}

			// 即时支付场合
			if (paymentStatus == 1) {
				//保存订单支付信息
				this.save(payIslandOrder);
				
				// 保存支付凭证ids
				String payVoucher = "";
				Long[] docInfoIds = orderPayForm.getDocInfoIds();
				if (i == 0) {
					if (docInfoIds != null && docInfoIds.length > 0) {
						for (Long docInfoId : docInfoIds) {
							payVoucher += docInfoId + ",";
						}
					}
				} else {
					if (docInfoIds != null && docInfoIds.length > 0) {
						for (int k = 0; k < docInfoIds.length; k++) {
							Long docInfoId = docInfoIds[k];
							Long docId = PayUtils.copySaveUploadInfo(docInfoId);
							payVoucher += docId + ",";
							docInfoIds[k] = docId;
						}
					}
				}

				payIslandOrder.setPayVoucher(payVoucher);
				orderPayDetail.setPayVoucher(payVoucher);

				// 为上传资料设置payorderid属性
				this.saveDocInfoByPayUUID(payIslandOrder.getMoneySerialNum(), docInfoIds);

				if (orderPayDetailList.size() > 1) {
					this.saveMoneyAmount(payIslandOrder.getMoneySerialNum(), orderPayDetail.getPayCurrencyId().split(","), orderPayDetail.getPayCurrencyPrice().split(","), orderPayDetail);
				} else {
					this.saveMoneyAmount(payIslandOrder.getMoneySerialNum(), orderPayForm.getCurrencyIdPrice(), orderPayForm.getDqzfprice(), orderPayDetail);
				}
			}

			orderPayDetail.setPayProductOrderUuid(payIslandOrder.getUuid());
			orderPayDetail.setPayTypeName(PayUtils.getPayTypeName(paymentStatus, orderPayForm.getPayType()));
		}
    	
    	return true;
    }
    
    /**
	 * 保存金额
	 * 
	 * @param serialNum
	 * @param payCurrencyIds
	 * @param payCurrencyPrice
	 */
	public void saveMoneyAmount(String serialNum, String[] payCurrencyIds, String[] payCurrencyPrice, OrderPayDetail orderPayDetail) throws Exception {

		List<IslandMoneyAmount> moneyAmountList = new ArrayList<IslandMoneyAmount>();

		Integer busindessType = null;

		for (int i = 0; i < payCurrencyIds.length; i++) {
			IslandMoneyAmount moneyAmount = new IslandMoneyAmount();
			// 流水号UUID
			moneyAmount.setSerialNum(serialNum);
			// 币种ID
			moneyAmount.setCurrencyId(Integer.parseInt(payCurrencyIds[i]));
			// 金额
			moneyAmount.setAmount(new Double(payCurrencyPrice[i]));
			// 订单ID、游客ID或团期ID
			busindessType = orderPayDetail.getBusindessType();
			if (busindessType == null || busindessType.intValue() == 1) {
				// 保存订单ID
				moneyAmount.setBusinessUuid(orderPayDetail.getOrderUuid());
			} else if (busindessType.intValue() == 2) {
				// 保存游客ID
				moneyAmount.setBusinessUuid(orderPayDetail.getTravelerUuid());
			} else if (busindessType.intValue() == 4) {
				// 保存团期ID
				Integer groupId = orderPayDetail.getGroupId();
				moneyAmount.setBusinessUuid(groupId == null ? null:groupId.toString());
			}

			// 款项类型
			moneyAmount.setMoneyType(orderPayDetail.getPayPriceType());
			// 业务类型(1表示订单，2表示游客,3表示询价报价,4表示团期)
			moneyAmount.setBusinessType(busindessType);

			moneyAmountList.add(moneyAmount);
		}
		islandMoneyAmountService.saveOrUpdateMoneyAmounts(serialNum, moneyAmountList);
	}
    
    /**
	 * 根据uuid找到订单支付表的主键id,保存到上传资料表中 uuid 订单支付表的付款uuid docIds 资料表的id数组
	 * 
	 * */
	public void saveDocInfoByPayUUID(String uuid, Long[] docIds) {
		if (null == docIds || docIds.length == 0 || null == uuid
				|| "".equals(uuid)) {
			return;
		}
		String paySQL = "select id from pay_island_order where moneySerialNum = ? ";
		String docinfoSQL = "update docinfo set payOrderId =? where id = ? ";
		List<Object> par = new ArrayList<Object>();
		par.add(uuid);
		List<Map<String, Object>> resultList = travelerDao.findBySql(paySQL, Map.class, par.toArray());

		for (Map<String, Object> listin : resultList) {
			if (null != listin.get("id") && !"".equals(listin.get("id"))) {
				for (int i = 0; i < docIds.length; i++) {
					List<Object> parameter = new ArrayList<Object>();
					parameter.add(listin.get("id"));
					parameter.add(docIds[i]);
					docInfoDao.updateBySql(docinfoSQL, parameter.toArray());
				}
			}
		}
	}
    
    /**
	 * 更新支付表（海岛游）的达帐状态
	*<p>Title: updateAccountStatus</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-17 上午11:26:30
	* @throws
	 */
	public boolean updateAccountStatus(String payUuid, int accountStatus) {
		PayIslandOrder payIslandOrder = this.getByUuid(payUuid);
		payIslandOrder.setIsAsAccount(accountStatus);
		this.update(payIslandOrder);
		return true;
	}

	  /**
		 * 更新支付表（海岛游）的达帐状态
		*<p>Title: updateAccountStatus</p>
		* @return boolean 返回类型
		* @author haiming.zhao
		* @param  id 主键Id	
		* @param  accountStatus
		* @date 2015-6-25 上午11:26:30
		* @throws
		 */
	@Override
	public boolean updateAccountStatus(int id, int accountStatus) {
		PayIslandOrder payIslandOrder = payIslandOrderDao.getById(id);
		payIslandOrder.setIsAsAccount(accountStatus);
		this.update(payIslandOrder);
		return true;
	}

	@Override
	public boolean rejectOrder(String productOrderId, String payId,
			String rejectRadio) {
		boolean result = false;
		try{
			PayIslandOrder orderpay = this.getById(Integer.valueOf(payId));
			IslandOrder islandOrder = islandOrderDao.getById(Integer.valueOf(productOrderId));
			if(orderpay != null && islandOrder != null){
				if(rejectRadio.equals(Context.REJECT_NO_PLACEHOLDER)){ 
					//保持占位
					updateAccountStatus(Integer.valueOf(payId), Context.ORDERPAY_ACCOUNT_STATUS_YBH);
					//已付金额减去驳回金额
					List<IslandMoneyAmount> list = islandMoneyAmountService.getMoneyAmonutBySerialNum(orderpay.getMoneySerialNum());
					if (CollectionUtils.isNotEmpty(list)) {
						for (int i = 0; i < list.size(); i++) {
							saveOrUpdateMoneyAmount(list.get(i), islandOrder.getPayedMoney(), "subtract", list.get(i).getMoneyType());
						}
					}
				}else{
	//				//退回占位
	
					
				}
			}
			result=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
    
	public void saveOrUpdateMoneyAmount(IslandMoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType) {
		IslandMoneyAmount islandMoneyAmount;

		List<IslandMoneyAmount> list = islandMoneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, moneyAmount.getCurrencyId());
		if (list != null && list.size() > 0) {
			islandMoneyAmount = list.get(0);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				islandMoneyAmountDao.updateAmount(islandMoneyAmount.getUuid(), new BigDecimal(islandMoneyAmount.getAmount()).add(new BigDecimal(moneyAmount.getAmount())));
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				islandMoneyAmountDao.updateAmount(islandMoneyAmount.getUuid(), new BigDecimal(islandMoneyAmount.getAmount()).subtract(new BigDecimal(moneyAmount.getAmount())));
			}
		} 
	}
}
