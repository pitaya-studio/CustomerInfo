package com.trekiz.admin.modules.mtourfinance.transfer;

import java.util.List;

import com.trekiz.admin.modules.mtourfinance.json.OrderpayJsonBean;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.sys.entity.DocInfo;

public class OrderpayTransfer {
	
	public static OrderpayJsonBean transferPayInfo2JsonBean(Orderpay orderpay, List<DocInfo> docInfos) {
		OrderpayJsonBean jsonBean = new OrderpayJsonBean();
		
		jsonBean.setReceiveUuid(orderpay.getId().toString());
		jsonBean.setPayer(orderpay.getPayerName());
		jsonBean.setArrivalBankDate(orderpay.getAccountDate());
		jsonBean.setReceivePeopleCount(orderpay.getReceivePeopleCount());
		jsonBean.setReceiveMethodCode(String.valueOf(orderpay.getPayType()));
		jsonBean.setMemo(orderpay.getRemarks());
		jsonBean.setCheckNo(orderpay.getCheckNumber());
		jsonBean.setCheckIssueDate(orderpay.getInvoiceDate());
		jsonBean.setPaymentBank(orderpay.getBankName());
		jsonBean.setPaymentAccount(orderpay.getBankAccount());
		jsonBean.setReceiveBank(orderpay.getToBankNname());
		jsonBean.setReceiveAccount(orderpay.getToBankAccount());
		jsonBean.setArrivalBankDate(orderpay.getAccountDate());
		
		jsonBean.setAttachments(DocInfoTransfer.transferDocInfos2JsonBeans(docInfos));
		
		return jsonBean;
	}
	
	public static OrderpayJsonBean transferPayInfo2JsonBean(PayGroup payGroup, List<DocInfo> docInfos) {
		OrderpayJsonBean jsonBean = new OrderpayJsonBean();
		
		jsonBean.setReceiveUuid(payGroup.getId().toString());
		jsonBean.setPayer(payGroup.getPayerName());
		jsonBean.setArrivalBankDate(payGroup.getAccountDate());
		/*jsonBean.setReceivePeopleCount(payGroup.getReceivePeopleCount());*/
		jsonBean.setReceiveMethodCode(String.valueOf(payGroup.getPayType()));
		jsonBean.setMemo(payGroup.getRemarks());
		jsonBean.setCheckNo(payGroup.getCheckNumber());
		jsonBean.setCheckIssueDate(payGroup.getInvoiceDate());
		jsonBean.setPaymentBank(payGroup.getBankName());
		jsonBean.setPaymentAccount(payGroup.getBankAccount());
		jsonBean.setReceiveBank(payGroup.getToBankNname());
		jsonBean.setReceiveAccount(payGroup.getToBankAccount());
		jsonBean.setArrivalBankDate(payGroup.getAccountDate());
		
		jsonBean.setAttachments(DocInfoTransfer.transferDocInfos2JsonBeans(docInfos));
		
		return jsonBean;
	}

}
