package com.trekiz.admin.modules.pay.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.pay.service.PayGroupService;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value="${adminPath}/payGroup")
public class PayGroupController {

	@Autowired
	private PayGroupService payGroupService;
	
	@Autowired
	private DocInfoService docInfoService;
	
	/**
	 * 其他收入收款
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 * @return
	 */
	@RequestMapping(value="receiptsMoney", method = RequestMethod.GET)
	public String receiptsMoney(HttpServletRequest request,HttpServletResponse response,Model model){
		String payGroupIdStr = request.getParameter("payGroupId");
		Integer payGroupId = null;
		if(StringUtils.isNotBlank(payGroupIdStr)){
			payGroupId = Integer.parseInt(payGroupIdStr.trim());
		}
		PayGroup payGroup = payGroupService.getById(payGroupId);
		String accountDate = payGroup.getAccountDateString();
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		if(companyName.contains("环球行")) {
			accountDate = payGroup.getCreateDate() == null ? "" : payGroup.getCreateDate().toString();
			if(StringUtils.isBlank(accountDate)){
				payGroup.setAccountDate(new Date());
			}else {
				Date groupDate = DateUtils.dateFormat(accountDate);
				accountDate = DateUtils
						.formatCustomDate(groupDate, "yyyy-MM-dd");
				payGroup.setAccountDate(groupDate);
			}
		}else {
			if(StringUtils.isBlank(accountDate)){
				payGroup.setAccountDate(new Date());
			}
		}
		List<DocInfo> voucherList =null ;
		//获取订单的支付凭证信息
	    if(StringUtils.isNotBlank(payGroup.getPayVoucher())){
	    	String[] docIdArray = payGroup.getPayVoucher().split(",");
	    	List<Long> docIdList = new ArrayList<Long>();
	    	for(String docId : docIdArray){
	    		docIdList.add(Long.parseLong(docId));
	    	}
	    	voucherList = docInfoService.getDocInfoByIds(docIdList);
	    }
	    model.addAttribute("voucherList", voucherList);
		model.addAttribute("payGroup", payGroup);
		return "modules/order/list/groupReceiptsConfirm";
	}
	
	/**
	 * 确认收款
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping(value="confirmRecepitsMoney", method = RequestMethod.POST)
	public void confirmRecepitsMoney(HttpServletRequest request,HttpServletResponse response,Model model){
		String json = "{\"flag\":\"success\"}";
		String payGroupId = request.getParameter("payGroupId");   //Pay_group 表ID
		String payerName = request.getParameter("payerName");     //付款单位
		String bankName = request.getParameter("bankName");       //来款行名称
		String bankAccount = request.getParameter("bankAccount"); //来款账户
		String checkNumber = request.getParameter("checkNumber"); //支票号
		String accountDate = request.getParameter("accountDate"); //银行到账日期
		String remarks = request.getParameter("remarks");         //备注
		String fromAlipayName = request.getParameter("fromAlipayName");	//支付宝名称（来款）
		String fromAlipayAccount = request.getParameter("fromAlipayAccount");	//支付宝账号（来款）
		String toAlipayName = request.getParameter("toAlipayName");	//支付宝名称（收款）
		String toAlipayAccount = request.getParameter("toAlipayAccount");	//支付宝账号（收款）
		String comeOfficeName = request.getParameter("comeOfficeName");	//收款单位
		
		if(StringUtils.isNotBlank(payGroupId)){
			PayGroup payGroup = payGroupService.getById(Integer.parseInt(payGroupId));
			if(StringUtils.isNotBlank(payerName)){
				payGroup.setPayerName(payerName.trim());
			}
			if(StringUtils.isNotBlank(bankName)){
				payGroup.setBankName(bankName.trim());
			}
			if(StringUtils.isNotBlank(bankAccount)){
				payGroup.setBankAccount(bankAccount.trim());
			}
			if(StringUtils.isNotBlank(checkNumber)){
				payGroup.setCheckNumber(checkNumber.trim());
			}
			
			if(StringUtils.isNotBlank(accountDate)){
				Date date = DateUtils.dateFormat(accountDate, DateUtils.DATE_PATTERN_YYYY_MM_DD);
				if(null == date){
					payGroup.setAccountDate(new Date());
				}else{
					payGroup.setAccountDate(date);
				}
			}
			if(StringUtils.isNotBlank(remarks)){
				payGroup.setRemarks(remarks);
			}
			
			//224 因公支付宝 modify by yang.wang 2016.7.18
			if(StringUtils.isNotBlank(comeOfficeName)) {
				payGroup.setComeOfficeName(comeOfficeName);
			}
			if(StringUtils.isNotBlank(toAlipayAccount)) {
				payGroup.setToAlipayAccount(toAlipayAccount);
			}
			if(StringUtils.isNotBlank(toAlipayName)) {
				payGroup.setToAlipayName(toAlipayName);
			}
			if(StringUtils.isNotBlank(fromAlipayAccount)) {
				payGroup.setFromAlipayAccount(fromAlipayAccount);
			}
			if(StringUtils.isNotBlank(fromAlipayName)) {
				payGroup.setFromAlipayName(fromAlipayName);
			}
			
			//0405	收款确认日期  update by shijun.liu
			payGroup.setReceiptConfirmationDate(new Date());
			try {
				payGroupService.confirmRecepitsMoney(payGroup);
			} catch (RuntimeException e) {
				json = "{\"flag\":\"fail\",\"msg\":\"确认收款失败\"}";
			}
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 撤销收款
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping(value="cancelRecepitsMoney", method = RequestMethod.POST)
	public void cancelRecepitsMoney(HttpServletRequest request,HttpServletResponse response,Model model){
		String json = "{\"flag\":\"success\"}";
		String payGroupId = request.getParameter("payGroupId");   //Pay_group 表ID
		if(StringUtils.isNotBlank(payGroupId)){
			PayGroup payGroup = payGroupService.getById(Integer.parseInt(payGroupId));
			try {
				payGroupService.cancelRecepitsMoney(payGroup);
			} catch (RuntimeException e) {
				json = "{\"flag\":\"fail\",\"msg\":\"撤销收款失败\"}";
			}
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 驳回收款
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping(value="rejectRecepitsMoney", method = RequestMethod.POST)
	public void rejectRecepitsMoney(HttpServletRequest request,HttpServletResponse response,Model model){
		String json = "{\"flag\":\"success\"}";
		String payGroupId = request.getParameter("payGroupId");   //Pay_group 表ID
		String rejectReason = request.getParameter("reason");   //Pay_group 表 驳回备注
		if(rejectReason != null) {
			rejectReason = rejectReason.replaceAll(" ", "");
		}
		if(StringUtils.isNotBlank(payGroupId)){
			PayGroup payGroup = payGroupService.getById(Integer.parseInt(payGroupId));
			try {
				payGroupService.rejectRecepitsMoney(payGroup, rejectReason);
			} catch (RuntimeException e) {
				json = "{\"flag\":\"fail\",\"msg\":\"驳回收款失败\"}";
			}
		}
		ServletUtil.print(response, json);
	}
}
