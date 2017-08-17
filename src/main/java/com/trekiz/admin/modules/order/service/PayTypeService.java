package com.trekiz.admin.modules.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.mtourfinance.json.RefundJsonBean;
import com.trekiz.admin.modules.order.entity.PayCheck;
import com.trekiz.admin.modules.order.entity.PayRemittance;
import com.trekiz.admin.modules.order.repository.PayCheckDao;
import com.trekiz.admin.modules.order.repository.PayRemittanceDao;
import com.trekiz.admin.modules.pay.dao.PayBanktransferDao;
import com.trekiz.admin.modules.pay.dao.PayDraftDao;
import com.trekiz.admin.modules.pay.dao.PayPosDao;

/**
 * 此类是支付平台所有的支付方式操作类
     * Title: PayTypeService.java    
     * Description: 
     * @author majiancheng       
     * @created 2015-10-24 下午12:21:33
 */
@Service
@Transactional(readOnly = true) 
class PayTypeService extends BaseService {

	/**
	 *  汇款
	 */
	@Autowired
	private PayRemittanceDao payRemittanceDao;
	/**
	 *  银行转账
	 */
	@Autowired
	private PayBanktransferDao payBanktransferDao;
	/**
	 *  汇票
	 */
	@Autowired
	private PayDraftDao payDraftDao;
	/**
	 *  POS机刷卡
	 */
	@Autowired
	private PayPosDao payPosDao;
	/**
	 *  支票支付
	 */
	@Autowired
	private PayCheckDao payCheckDao;
	
	/**
	 * 根据付款json对象保存支付类型信息
		 * @Title: savePayTypeInfoByRefundJsonBean
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-10-24 下午1:49:10
	 */
	boolean savePayTypeInfoByRefundJsonBean(RefundJsonBean refundJsonBean) {
		boolean flag = false;
		
		if(refundJsonBean == null) {
			return false;
		}
		
		try{
			int payType = Integer.parseInt(refundJsonBean.getPaymentMethodCode());
			String payTypeId = UuidUtils.generUuid();
			refundJsonBean.setPayTypeId(payTypeId);
			switch(payType) {
			
			case 1:// 支票支付
				PayCheck payCheck = new PayCheck();
				payCheck.setId(payTypeId);
				payCheck.setPayerName(refundJsonBean.getReceiveCompany());
				payCheck.setCheckNumber(refundJsonBean.getCheckNo());
				payCheck.setInvoiceDate(refundJsonBean.getCheckIssueDate());
				payCheckDao.save(payCheck);

				break;
			case 3:// 现金支付
				break;
			case 4:// 汇款
				PayRemittance payRemittance = new PayRemittance();
				payRemittance.setId(payTypeId);
				payRemittance.setBankName(refundJsonBean.getPaymentBank());
				payRemittance.setBankAccount(refundJsonBean.getPaymentAccount());
				payRemittance.setTobankName(refundJsonBean.getReceiveBank());
				payRemittance.setTobankAccount(refundJsonBean.getReceiveAccount());
				payRemittanceDao.save(payRemittance);

				break;
			case 5:// 快速支付
				break;
			case 6://银行转账
				/*PayBanktransfer payBanktransfer = new PayBanktransfer();
				payBanktransfer.setPayBankName(orderPayForm.getPayBankName());
				payBanktransfer.setPayAccount(orderPayForm.getPayAccount());
				payBanktransfer.setReceiveCompanyName(orderPayForm.getReceiveCompanyName());
				payBanktransfer.setReceiveBankName(orderPayForm.getReceiveBankName());
				payBanktransfer.setReceiveAccount(orderPayForm.getReceiveAccount());
				super.setOptInfo(payBanktransfer, BaseService.OPERATION_ADD);
				payBanktransfer.setUuid(payTypeId);
				
				payBanktransferDao.saveObj(payBanktransfer);*/
				break;
			case 7://汇票
				/*PayDraft payDraft = new PayDraft();
				payDraft.setDrawerName(orderPayForm.getDrawerName());
				payDraft.setDrawerAccount(orderPayForm.getDrawerAccount());
				payDraft.setPayBankName(orderPayForm.getPayBankName());
				payDraft.setDraftAccountedDate(orderPayForm.getDraftAccountedDate());
				payDraft.setReceiveCompanyName(orderPayForm.getReceiveCompanyName());
				payDraft.setReceiveBankName(orderPayForm.getReceiveBankName());
				payDraft.setReceiveAccount(orderPayForm.getReceiveAccount());
				super.setOptInfo(payDraft, BaseService.OPERATION_ADD);
				payDraft.setUuid(payTypeId);
				
				payDraftDao.saveObj(payDraft);*/
				break;
			case 8://POS机刷卡
				/*PayPos payPos = new PayPos();
				payPos.setReceiveCompanyName(orderPayForm.getReceiveCompanyName());
				payPos.setReceiveBankName(orderPayForm.getReceiveBankName());
				payPos.setReceiveAccount(orderPayForm.getReceiveAccount());
				super.setOptInfo(payPos, BaseService.OPERATION_ADD);
				payPos.setUuid(payTypeId);
				
				payPosDao.saveObj(payPos);*/
				break;
			}
			flag = true;
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return flag;
	}

}
