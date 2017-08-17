/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.hotel.dao.HotelDao;
import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.hotel.dao.HotelOrderDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.pay.dao.PayHotelOrderDao;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;
import com.trekiz.admin.modules.pay.input.PayHotelOrderInput;
import com.trekiz.admin.modules.pay.query.PayHotelOrderQuery;
import com.trekiz.admin.modules.pay.service.PayHotelOrderService;
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
public class PayHotelOrderServiceImpl  extends BaseService implements PayHotelOrderService{
	@Autowired
	private PayHotelOrderDao payHotelOrderDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private HotelOrderDao hotelOrderDao;
	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private HotelMoneyAmountDao hotelMoneyAmountDao;
	public void save (PayHotelOrder payHotelOrder){
		super.setOptInfo(payHotelOrder, BaseService.OPERATION_ADD);
		payHotelOrderDao.saveObj(payHotelOrder);
	}
	
	public void save (PayHotelOrderInput payHotelOrderInput){
		PayHotelOrder payHotelOrder = payHotelOrderInput.getPayHotelOrder();
		super.setOptInfo(payHotelOrder, BaseService.OPERATION_ADD);
		payHotelOrderDao.saveObj(payHotelOrder);
	}
	
	public void update (PayHotelOrder payHotelOrder){
		super.setOptInfo(payHotelOrder, BaseService.OPERATION_UPDATE);
		payHotelOrderDao.updateObj(payHotelOrder);
	}
	
	public PayHotelOrder getById(java.lang.Integer value) {
		return payHotelOrderDao.getById(value);
	}	
	public void removeById(java.lang.Integer value){
		PayHotelOrder obj = payHotelOrderDao.getById(value);
		obj.setDelFlag("1");
		this.update(obj);
	}	
	

	public Page<PayHotelOrder> find(Page<PayHotelOrder> page, PayHotelOrderQuery payHotelOrderQuery) {
		DetachedCriteria dc = payHotelOrderDao.createDetachedCriteria();
		
	   	if(payHotelOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payHotelOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payHotelOrderQuery.getUuid()));
		}
	   	if(payHotelOrderQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", payHotelOrderQuery.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayTypeName())){
			dc.add(Restrictions.eq("payTypeName", payHotelOrderQuery.getPayTypeName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayerName())){
			dc.add(Restrictions.eq("payerName", payHotelOrderQuery.getPayerName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getCheckNumber())){
			dc.add(Restrictions.eq("checkNumber", payHotelOrderQuery.getCheckNumber()));
		}
		if(payHotelOrderQuery.getInvoiceDate()!=null){
			dc.add(Restrictions.eq("invoiceDate", payHotelOrderQuery.getInvoiceDate()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayVoucher())){
			dc.add(Restrictions.eq("payVoucher", payHotelOrderQuery.getPayVoucher()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getRemarks())){
			dc.add(Restrictions.eq("remarks", payHotelOrderQuery.getRemarks()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPosNo())){
			dc.add(Restrictions.eq("posNo", payHotelOrderQuery.getPosNo()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPosTagEendNo())){
			dc.add(Restrictions.eq("posTagEendNo", payHotelOrderQuery.getPosTagEendNo()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPosBank())){
			dc.add(Restrictions.eq("posBank", payHotelOrderQuery.getPosBank()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getBankName())){
			dc.add(Restrictions.eq("bankName", payHotelOrderQuery.getBankName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getToBankNname())){
			dc.add(Restrictions.eq("toBankNname", payHotelOrderQuery.getToBankNname()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getBankAccount())){
			dc.add(Restrictions.eq("bankAccount", payHotelOrderQuery.getBankAccount()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getToBankAccount())){
			dc.add(Restrictions.eq("toBankAccount", payHotelOrderQuery.getToBankAccount()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getFromCompanyName())){
			dc.add(Restrictions.eq("fromCompanyName", payHotelOrderQuery.getFromCompanyName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getFromBankName())){
			dc.add(Restrictions.eq("fromBankName", payHotelOrderQuery.getFromBankName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getFromAccount())){
			dc.add(Restrictions.eq("fromAccount", payHotelOrderQuery.getFromAccount()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payHotelOrderQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payHotelOrderQuery.getReceiveAccount()));
		}
		if(payHotelOrderQuery.getDraftAccountedDate()!=null){
			dc.add(Restrictions.eq("draftAccountedDate", payHotelOrderQuery.getDraftAccountedDate()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayPrice())){
			dc.add(Restrictions.eq("payPrice", payHotelOrderQuery.getPayPrice()));
		}
	   	if(payHotelOrderQuery.getPayPriceType()!=null){
	   		dc.add(Restrictions.eq("payPriceType", payHotelOrderQuery.getPayPriceType()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", payHotelOrderQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayPriceBack())){
			dc.add(Restrictions.eq("payPriceBack", payHotelOrderQuery.getPayPriceBack()));
		}
	   	if(payHotelOrderQuery.getIsAsAccount()!=null){
	   		dc.add(Restrictions.eq("isAsAccount", payHotelOrderQuery.getIsAsAccount()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getOldPayPrice())){
			dc.add(Restrictions.eq("oldPayPrice", payHotelOrderQuery.getOldPayPrice()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getFastPayType())){
			dc.add(Restrictions.eq("fastPayType", payHotelOrderQuery.getFastPayType()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getOrderNum())){
			dc.add(Restrictions.eq("orderNum", payHotelOrderQuery.getOrderNum()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getMoneySerialNum())){
			dc.add(Restrictions.eq("moneySerialNum", payHotelOrderQuery.getMoneySerialNum()));
		}
		if(payHotelOrderQuery.getPrintTime()!=null){
			dc.add(Restrictions.eq("printTime", payHotelOrderQuery.getPrintTime()));
		}
	   	if(payHotelOrderQuery.getPrintFlag()!=null){
	   		dc.add(Restrictions.eq("printFlag", payHotelOrderQuery.getPrintFlag()));
	   	}
	   	if(payHotelOrderQuery.getPaymentStatus()!=null){
	   		dc.add(Restrictions.eq("paymentStatus", payHotelOrderQuery.getPaymentStatus()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getTravelerUuid())){
			dc.add(Restrictions.eq("travelerUuid", payHotelOrderQuery.getTravelerUuid()));
		}
	   	if(payHotelOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payHotelOrderQuery.getCreateBy()));
	   	}
		if(payHotelOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payHotelOrderQuery.getCreateDate()));
		}
	   	if(payHotelOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payHotelOrderQuery.getUpdateBy()));
	   	}
		if(payHotelOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payHotelOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payHotelOrderQuery.getDelFlag()));
		}
		if(payHotelOrderQuery.getAccountDate()!=null){
			dc.add(Restrictions.eq("accountDate", payHotelOrderQuery.getAccountDate()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payHotelOrderDao.find(page, dc);
	}
	
	public List<PayHotelOrder> find( PayHotelOrderQuery payHotelOrderQuery) {
		DetachedCriteria dc = payHotelOrderDao.createDetachedCriteria();
		
	   	if(payHotelOrderQuery.getId()!=null){
	   		dc.add(Restrictions.eq("id", payHotelOrderQuery.getId()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getUuid())){
			dc.add(Restrictions.eq("uuid", payHotelOrderQuery.getUuid()));
		}
	   	if(payHotelOrderQuery.getPayType()!=null){
	   		dc.add(Restrictions.eq("payType", payHotelOrderQuery.getPayType()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayTypeName())){
			dc.add(Restrictions.eq("payTypeName", payHotelOrderQuery.getPayTypeName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayerName())){
			dc.add(Restrictions.eq("payerName", payHotelOrderQuery.getPayerName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getCheckNumber())){
			dc.add(Restrictions.eq("checkNumber", payHotelOrderQuery.getCheckNumber()));
		}
		if(payHotelOrderQuery.getInvoiceDate()!=null){
			dc.add(Restrictions.eq("invoiceDate", payHotelOrderQuery.getInvoiceDate()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayVoucher())){
			dc.add(Restrictions.eq("payVoucher", payHotelOrderQuery.getPayVoucher()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getRemarks())){
			dc.add(Restrictions.eq("remarks", payHotelOrderQuery.getRemarks()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPosNo())){
			dc.add(Restrictions.eq("posNo", payHotelOrderQuery.getPosNo()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPosTagEendNo())){
			dc.add(Restrictions.eq("posTagEendNo", payHotelOrderQuery.getPosTagEendNo()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPosBank())){
			dc.add(Restrictions.eq("posBank", payHotelOrderQuery.getPosBank()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getBankName())){
			dc.add(Restrictions.eq("bankName", payHotelOrderQuery.getBankName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getToBankNname())){
			dc.add(Restrictions.eq("toBankNname", payHotelOrderQuery.getToBankNname()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getBankAccount())){
			dc.add(Restrictions.eq("bankAccount", payHotelOrderQuery.getBankAccount()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getToBankAccount())){
			dc.add(Restrictions.eq("toBankAccount", payHotelOrderQuery.getToBankAccount()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getFromCompanyName())){
			dc.add(Restrictions.eq("fromCompanyName", payHotelOrderQuery.getFromCompanyName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getFromBankName())){
			dc.add(Restrictions.eq("fromBankName", payHotelOrderQuery.getFromBankName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getFromAccount())){
			dc.add(Restrictions.eq("fromAccount", payHotelOrderQuery.getFromAccount()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getReceiveBankName())){
			dc.add(Restrictions.eq("receiveBankName", payHotelOrderQuery.getReceiveBankName()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getReceiveAccount())){
			dc.add(Restrictions.eq("receiveAccount", payHotelOrderQuery.getReceiveAccount()));
		}
		if(payHotelOrderQuery.getDraftAccountedDate()!=null){
			dc.add(Restrictions.eq("draftAccountedDate", payHotelOrderQuery.getDraftAccountedDate()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayPrice())){
			dc.add(Restrictions.eq("payPrice", payHotelOrderQuery.getPayPrice()));
		}
	   	if(payHotelOrderQuery.getPayPriceType()!=null){
	   		dc.add(Restrictions.eq("payPriceType", payHotelOrderQuery.getPayPriceType()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getOrderUuid())){
			dc.add(Restrictions.eq("orderUuid", payHotelOrderQuery.getOrderUuid()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getPayPriceBack())){
			dc.add(Restrictions.eq("payPriceBack", payHotelOrderQuery.getPayPriceBack()));
		}
	   	if(payHotelOrderQuery.getIsAsAccount()!=null){
	   		dc.add(Restrictions.eq("isAsAccount", payHotelOrderQuery.getIsAsAccount()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getOldPayPrice())){
			dc.add(Restrictions.eq("oldPayPrice", payHotelOrderQuery.getOldPayPrice()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getFastPayType())){
			dc.add(Restrictions.eq("fastPayType", payHotelOrderQuery.getFastPayType()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getOrderNum())){
			dc.add(Restrictions.eq("orderNum", payHotelOrderQuery.getOrderNum()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getMoneySerialNum())){
			dc.add(Restrictions.eq("moneySerialNum", payHotelOrderQuery.getMoneySerialNum()));
		}
		if(payHotelOrderQuery.getPrintTime()!=null){
			dc.add(Restrictions.eq("printTime", payHotelOrderQuery.getPrintTime()));
		}
	   	if(payHotelOrderQuery.getPrintFlag()!=null){
	   		dc.add(Restrictions.eq("printFlag", payHotelOrderQuery.getPrintFlag()));
	   	}
	   	if(payHotelOrderQuery.getPaymentStatus()!=null){
	   		dc.add(Restrictions.eq("paymentStatus", payHotelOrderQuery.getPaymentStatus()));
	   	}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getTravelerUuid())){
			dc.add(Restrictions.eq("travelerUuid", payHotelOrderQuery.getTravelerUuid()));
		}
	   	if(payHotelOrderQuery.getCreateBy()!=null){
	   		dc.add(Restrictions.eq("createBy", payHotelOrderQuery.getCreateBy()));
	   	}
		if(payHotelOrderQuery.getCreateDate()!=null){
			dc.add(Restrictions.eq("createDate", payHotelOrderQuery.getCreateDate()));
		}
	   	if(payHotelOrderQuery.getUpdateBy()!=null){
	   		dc.add(Restrictions.eq("updateBy", payHotelOrderQuery.getUpdateBy()));
	   	}
		if(payHotelOrderQuery.getUpdateDate()!=null){
			dc.add(Restrictions.eq("updateDate", payHotelOrderQuery.getUpdateDate()));
		}
		if (StringUtils.isNotEmpty(payHotelOrderQuery.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", payHotelOrderQuery.getDelFlag()));
		}
		if(payHotelOrderQuery.getAccountDate()!=null){
			dc.add(Restrictions.eq("accountDate", payHotelOrderQuery.getAccountDate()));
		}
		
		//dc.addOrder(Order.desc("id"));
		return payHotelOrderDao.find(dc);
	}
	
	public PayHotelOrder getByUuid(String uuid) {
		return payHotelOrderDao.getByUuid(uuid);
	}
	
	public void removeByUuid(String uuid) {
		PayHotelOrder payHotelOrder = getByUuid(uuid);
		payHotelOrder.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		update(payHotelOrder);
	}
	
	/**
     * 根据订单ids查询支付订单
     * @param orderIds
     * @return
     */
    public List<PayHotelOrder> findOrderPayByOrderUuids(List<String> uuids) {
    	DetachedCriteria dc = payHotelOrderDao.createDetachedCriteria();
    	dc.add(Restrictions.eq("delFlag", Context.DEL_FLAG_NORMAL));
    	dc.add(Restrictions.in("orderUuid", uuids));
    	return payHotelOrderDao.find(dc);
     }
    
    public List<PayHotelOrder> findLastDateOrderPay(String orderPayId) {
    	DetachedCriteria dc = payHotelOrderDao.createDetachedCriteria();
    	dc.add(Restrictions.eq("orderUuid", orderPayId));
    	dc.add(Restrictions.eq("delFlag", Context.DEL_FLAG_NORMAL));
    	dc.add(Restrictions.isNotNull("isAsAccount"));
    	dc.addOrder(Order.desc("updateDate"));
    	return payHotelOrderDao.find(dc);
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
		PayHotelOrder entity = this.getByUuid(payUuid);
		entity.setIsAsAccount(accountStatus);
		this.update(entity);
		return true;
	}
	

	/**
	 * 更新支付表（海岛游）的达帐状态
	*<p>Title: updateAccountStatus</p>
	* @return boolean 返回类型
	* @author haiming.zhao
	* @param id  主键Id
	* @param accountStatus  
	* @date 2015-6-25 上午11:26:30
	* @throws
	 */
	@Override
	public boolean updateAccountStatus(int id, int accountStatus) {
		PayHotelOrder entity = payHotelOrderDao.getById(id);
		entity.setIsAsAccount(accountStatus);
		this.update(entity);
		return true;
	}
	
	
	public boolean saveByOrderPayForm(OrderPayForm orderPayForm) throws Exception {
    	OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();

		for (int i = 0; i < orderPayDetailList.size(); i++) {

			OrderPayDetail orderPayDetail = orderPayDetailList.get(i);

			PayHotelOrder payHotelOrder = new PayHotelOrder();
			// 支付方式
			payHotelOrder.setPayType(orderPayForm.getPayType());
			Integer paymentStatus = orderPayForm.getPaymentStatus();
			if (paymentStatus == null) {
				paymentStatus = 1;
			}
			payHotelOrder.setPaymentStatus(paymentStatus);
			payHotelOrder.setPayTypeName(PayUtils.getPayTypeName(paymentStatus, orderPayForm.getPayType()));

			// 即时支付场合
			if (paymentStatus == 1) {
				// 来款单位
				if (StringUtils.isNotEmpty(orderPayForm.getPayerName())) {
					payHotelOrder.setPayerName(orderPayForm.getPayerName());
				}

				switch (orderPayForm.getPayType()) {
				case 1:// 支票支付
						// 支票号
					if (StringUtils.isNotEmpty(orderPayForm.getCheckNumber())) {
						payHotelOrder.setCheckNumber(orderPayForm.getCheckNumber());
					}
					// 开票日期
					if (orderPayForm.getInvoiceDate() != null && StringUtils.isNotEmpty(orderPayForm.getInvoiceDate().toString())) {
						payHotelOrder.setInvoiceDate(orderPayForm.getInvoiceDate());
					}
					break;
				case 3:// 现金支付
					break;
				case 4:// 汇款
						// 开户行名称
					if (StringUtils.isNotEmpty(orderPayForm.getBankName())) {
						payHotelOrder.setBankName(orderPayForm.getBankName());
					}
					// 开户行账户
					if (StringUtils.isNotEmpty(orderPayForm.getBankAccount())) {
						payHotelOrder.setBankAccount(orderPayForm.getBankAccount());
					}
					// 转入行名称
					if (StringUtils.isNotEmpty(orderPayForm.getToBankNname())) {
						payHotelOrder.setToBankNname(orderPayForm.getToBankNname());
					}
					// 转入行账号
					if (StringUtils.isNotEmpty(orderPayForm.getToBankAccount())) {
						payHotelOrder.setToBankAccount(orderPayForm.getToBankAccount());
					}
					break;
				case 5:// 快速支付
						// 支付方式
					if (StringUtils.isNotEmpty(orderPayForm.getFastPayType())) {
						payHotelOrder.setFastPayType(orderPayForm.getFastPayType());
					}
					break;
				}
				
				//多个支付方式设置值--------
				//付款单位
				if(StringUtils.isNotEmpty(orderPayForm.getPayerName())) {
					payHotelOrder.setPayerName(orderPayForm.getPayerName());
				}
				// 开户行名称
				if (StringUtils.isNotEmpty(orderPayForm.getBankName())) {
					payHotelOrder.setBankName(orderPayForm.getBankName());
				}
				// 开户行账户
				if (StringUtils.isNotEmpty(orderPayForm.getBankAccount())) {
					payHotelOrder.setBankAccount(orderPayForm.getBankAccount());
				}
				// 转入行名称
				if (StringUtils.isNotEmpty(orderPayForm.getToBankNname())) {
					payHotelOrder.setToBankNname(orderPayForm.getToBankNname());
				}
				// 转入行账号
				if (StringUtils.isNotEmpty(orderPayForm.getToBankAccount())) {
					payHotelOrder.setToBankAccount(orderPayForm.getToBankAccount());
				}
				//汇票到账日期
				if(orderPayForm.getDraftAccountedDate() != null) {
					payHotelOrder.setDraftAccountedDate(orderPayForm.getDraftAccountedDate());
				}
				//多个支付方式设置值--------
				

				// 保存支付订单金额
				String moneyUuid = UuidUtils.generUuid().toString();
				payHotelOrder.setMoneySerialNum(moneyUuid);
				orderPayDetail.setMoneySerialNum(moneyUuid);

				// 备注信息
				if (StringUtils.isNotEmpty(orderPayForm.getRemarks())) {
					payHotelOrder.setRemarks(orderPayForm.getRemarks());
				}

				/*
				 * 其他和支付无直接关系，各模块相关的业务字段
				 */
				// 订单id
				payHotelOrder.setOrderUuid(orderPayDetail.getOrderUuid());
				// 游客id
				payHotelOrder.setTravelerUuid(orderPayDetail.getTravelerUuid());
				// 订单号
				payHotelOrder.setOrderNum(orderPayDetail.getOrderNum());
				// 支付款类型（全款、定金、尾款）
				payHotelOrder.setPayPriceType(orderPayDetail.getPayPriceType());
			}

			// 即时支付场合
			if (paymentStatus == 1) {
				//保存订单支付信息
				this.save(payHotelOrder);
				
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

				payHotelOrder.setPayVoucher(payVoucher);
				orderPayDetail.setPayVoucher(payVoucher);

				// 为上传资料设置payorderid属性
				this.saveDocInfoByPayUUID(payHotelOrder.getMoneySerialNum(), docInfoIds);

				if (orderPayDetailList.size() > 1) {
					this.saveMoneyAmount(payHotelOrder.getMoneySerialNum(), orderPayDetail.getPayCurrencyId().split(","), orderPayDetail.getPayCurrencyPrice().split(","), orderPayDetail);
				} else {
					this.saveMoneyAmount(payHotelOrder.getMoneySerialNum(), orderPayForm.getCurrencyIdPrice(), orderPayForm.getDqzfprice(), orderPayDetail);
				}
			}

			orderPayDetail.setPayProductOrderUuid(payHotelOrder.getUuid());
			orderPayDetail.setPayTypeName(PayUtils.getPayTypeName(paymentStatus, orderPayForm.getPayType()));
		}
    	
    	return true;
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
		String paySQL = "select id from pay_hotel_order where moneySerialNum = ? ";
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
	 * 保存金额
	 * 
	 * @param serialNum
	 * @param payCurrencyIds
	 * @param payCurrencyPrice
	 */
	public void saveMoneyAmount(String serialNum, String[] payCurrencyIds, String[] payCurrencyPrice, OrderPayDetail orderPayDetail) throws Exception {

		List<HotelMoneyAmount> moneyAmountList = new ArrayList<HotelMoneyAmount>();

		Integer busindessType = null;

		for (int i = 0; i < payCurrencyIds.length; i++) {
			HotelMoneyAmount moneyAmount = new HotelMoneyAmount();
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
		hotelMoneyAmountService.saveOrUpdateMoneyAmounts(serialNum, moneyAmountList);
	}

	
	@Override
	public boolean rejectOrder(String productOrderId, String payId,
			String rejectRadio) {
		boolean result = false;
		try{
			PayHotelOrder orderpay = this.getById(Integer.valueOf(payId));
			HotelOrder hotelOrder = hotelOrderDao.getById(Integer.valueOf(productOrderId));
			if(orderpay != null && hotelOrder != null){
				if(rejectRadio.equals(Context.REJECT_NO_PLACEHOLDER)){
					//保持占位
					updateAccountStatus(Integer.valueOf(payId), Context.ORDERPAY_ACCOUNT_STATUS_YBH);
					//已付金额减去驳回金额
					List<HotelMoneyAmount> list = hotelMoneyAmountService.getMoneyAmonutBySerialNum(orderpay.getMoneySerialNum());
					if (CollectionUtils.isNotEmpty(list)) {
						for (int i = 0; i < list.size(); i++) {
							saveOrUpdateMoneyAmount(list.get(i), hotelOrder.getPayedMoney(), "subtract", list.get(i).getMoneyType());
						}
					}
				}else{
					
				}
			}
			result = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void saveOrUpdateMoneyAmount(HotelMoneyAmount moneyAmount, String serialNum, String operate, Integer moneyType) {
		HotelMoneyAmount hotelMoneyAmount;

		List<HotelMoneyAmount> list = hotelMoneyAmountDao.findAmountBySerialNumAndCurrencyId(serialNum, moneyAmount.getCurrencyId());
		if (list != null && list.size() > 0) {
			hotelMoneyAmount = list.get(0);
			if (!StringUtils.isEmpty(operate) && "add".equals(operate)) {
				hotelMoneyAmountDao.updateAmount(hotelMoneyAmount.getUuid(), new BigDecimal(hotelMoneyAmount.getAmount()).add(new BigDecimal(moneyAmount.getAmount())));
			} else if (!StringUtils.isEmpty(operate) && "subtract".equals(operate)) {
				hotelMoneyAmountDao.updateAmount(hotelMoneyAmount.getUuid(), new BigDecimal(hotelMoneyAmount.getAmount()).subtract(new BigDecimal(moneyAmount.getAmount())));
			}
		} 
	}

}
