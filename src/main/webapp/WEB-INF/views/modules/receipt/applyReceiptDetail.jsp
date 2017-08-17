<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>财务-收据管理-开收据-开收据</title>
<meta name="decorator" content="wholesaler" />
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script type="text/javascript">
$(document).ready(function() {
	$("#searchForm").validate({});
    jQuery.extend(jQuery.validator.messages, {
        required: "必填信息",
        digits:"请输入正确的数字",
        number : "请输入正确的数字价格"
    });
});

function createInvoiceSubmit() {
	var inoviceNum = $("[name='invoiceNum']").val();
	var validate = $("#searchForm").valid();
	if(validate) {
    	$.ajax({
    		type:"POST",
    		url:"${ctx}/receipt/limit/invoiceNum",
    		data:{
    			inoviceNum : inoviceNum
    		},
    		success:function(msg) {
    			if(msg=="true") {
    				createInvoiceByInvoiceNum();
    			}else{
    				var submit = function (v, h, f) {
				    	if (v == 'ok') {
				        	createInvoiceByInvoiceNum();
				    	}
				   	 	
				   	 	if (v == 'cancel') {
				    		$("[name='invoiceNum']").select();
				    	}
				    	return true;
					};
				
					$.jBox.confirm("该收据号已经存在，是否再次使用？", "提示", submit ,{persistent: true});
    			}
    		}
    	});
    } else {
    	$.jBox.tip("请填写收据号！", "warning");
    }
}
function createInvoiceByInvoiceNum() {
	$("#createInvoiceHid").val("true");
	$("#searchForm").submit();
}
</script>
</head>
<body>
		<!--右侧内容部分开始-->
		<form:form id="searchForm" action="${ctx}/receipt/limit/verifyinvoice/${details[0].uuid}" method="post" class="breadcrumb form-search">
		<table class="activitylist_bodyer_table" id="contentTable">
			<thead>
				<tr>
					<th width="6%">序号</th>
					<th width="10%">订单号</th>
					<th width="10%">团号</th>
					<th width="6%">销售</th>
					<th width="10%">下单时间</th>
					<th width="8%">人数</th>
					<th width="5%">出/截团日期</th>
					<th width="10%">应收金额</th>
					<th width="10%">财务到账</th>
					<th width="10%">已开收据金额</th>
					<th width="10%">本次开收据金额</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${limits}" var="receiptInfo" varStatus="s">
		            <tr>
		            	<td class="tc">${s.count }</td>
						<td class="tc">${receiptInfo[0]}</td>
						<td class="tc">${receiptInfo[1]}</td>
						<c:set var="supplyBy" value="${receiptInfo[3]}" />
						<td class="tc">${receiptInfo[3]}</td>
						<td class="tc">${receiptInfo[4]}</td>
						<td class="tc">${receiptInfo[5]}</td>
						<td style="padding: 0px;" class="tc">
							<div class="">
								<c:if test="${not empty receiptInfo[6] }">
									<fmt:formatDate pattern="yyyy-MM-dd" value="${receiptInfo[6]}" />
								</c:if>
							</div>
							<div class="">
								<c:if test="${not empty receiptInfo[7] }">
									<fmt:parseDate pattern="yyyy-MM-dd" value="${receiptInfo[7]}" var="receiptInfo7"></fmt:parseDate>
									<fmt:formatDate pattern="yyyy-MM-dd" value="${receiptInfo7}" />
								</c:if>
							</div>
						</td>
						<td class="tr">${fns:getMoneyAmountBySerialNum(receiptInfo[8],1)}</td>
						<td class="tr">${fns:getMoneyAmountBySerialNum(receiptInfo[13],1)}</td>
						<td class="tr"><fmt:formatNumber
									type="currency" pattern="#,##0.00" value="${receiptInfo[10]}" /></td>
						<td class="tr"><fmt:formatNumber
									type="currency" pattern="#,##0.00" value="${receiptInfo[11]}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="sup_detail_top">
			<ul class="team_co clearFix vote-ul">
        		<li style="width:100%;"><label>开收据客户：</label>${details[0].invoiceCustomer }</li>
        		<li><label>开收据类型：</label>${invoiceTypes[details[0].invoiceType]}</li>
        		<li><label>开收据方式：</label>${invoiceModes[details[0].invoiceMode] }</li>
        		<li><label>开收据项目：</label>${invoiceSubjects[details[0].invoiceSubject] }</li>
        		<li><label>开收据抬头：</label>${details[0].invoiceHead}</li>
        		<li><label>申请人：</label>${supplyBy}</li>
        		<li><label>开收据金额：</label>￥${invoiceMoney }</li>
        		<li><label>开收据原因：</label>${details[0].remarks }</li>
        		<input id="createInvoiceHid" name="createInvoiceHid" value="" type="hidden"/>
        		<li class="clear"></li>
				
				<li>
					<label><span class="xing">*</span>收据号：</label>
					<input type="text" name="invoiceNum" value="" maxlength="20" class="required"/>
				</li>
				<li class="clear"></li>
			</ul>
		</div>
       	<div class="ydBtn" style="margin-top:50px;width: 280px !important;">
			<input type="button" value="返   回" onclick="javascript:window.close();" class="btn btn-primary" id="btnCancel">
			<input id="createInvoice" class="btn btn-primary" type="button" value="开收据" onClick="createInvoiceSubmit()" class="required"/>
		</div>
		</form:form>
		<!--右侧内容部分结束-->
</body>
</html>
