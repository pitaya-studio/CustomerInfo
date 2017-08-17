<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title><c:if test="${verifyStatus eq '-2'}">批发商开发票详情</c:if>
		<c:if test="${verifyStatus eq '0'}">审核发票</c:if>
		<c:if test="${verifyStatus eq 'ne0'}">已审核发票</c:if>
		<c:if test="${verifyStatus eq '1'}">开票明细</c:if>
		<c:if test="${verifyStatus eq '-1'}">开具发票</c:if></title>
<meta name="decorator" content="wholesaler" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
.sort {
	color: #0663A2;
	cursor: pointer;
}
</style>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
    
	$("#searchForm").validate({});
	
    jQuery.extend(jQuery.validator.messages, {
        required: "必填信息",
        digits:"请输入正确的数字",
        number : "请输入正确的数字价格"
    });
    
    var invoiceReasonVal = $("#invoiceReason").text();
    if(invoiceReasonVal.length > 40) {
    	var tempInvoiceReasonVal = invoiceReasonVal.substring(0, 40) + "......";
    	$("#invoiceReason").text(tempInvoiceReasonVal);
    }

});
function createInvoiceSubmit() {
	var inoviceNum = $("[name='invoiceNum']").val();
	var inoviceRemark = $("[name='inoviceRemark']").val();
	var validate = $("#searchForm").valid();
	if(validate) {
    	$.ajax({
    		type:"POST",
    		url:"${ctx}/invoice/limit/invoiceNum",
    		data:{
    			inoviceNum : inoviceNum,
    			inoviceRemark: inoviceRemark
    		},
    		success:function(msg) {
    			if(msg=="true") {
    				createInvoiceByInvoiceNum();
    			}else{
    				var submit = function (v, h, f) {
				    if (v == 'ok') {
				        createInvoiceByInvoiceNum();
				    }
				    if (v == 'no') {
				    }
				    if (v == 'cancel') {
				    	$("[name='invoiceNum']").select();
				    }
				    	return true;
					};
				
					$.jBox.confirm("该发票号已经存在，是否再次使用？", "提示", submit ,{persistent: true});
    			}
    		}
    	});
    }
}

function createInvoiceByInvoiceNum() {
	$("#createInvoiceHid").val("true");
	$("#searchForm").submit();
}
/**
 * 驳回申请
 */
function denyInvoiceSubmit(uuid) {
	var url = "${ctx}/invoice/limit/denyinvoice/" + uuid;
	$("#searchForm").attr("action",url);
	$("#searchForm").submit();
}
/**
 * 审核通过
 */
function verifyInvoiceSubmit(uuid) {
	var url = "${ctx}/invoice/limit/verifyinvoice/" + uuid;
	$("#searchForm").attr("action",url);
	$("#searchForm").submit();
}

function limitTextArea(e){
	var maxlimit = 250; 
    if (e.value.length > maxlimit){
    	e.value = e.value.substring(0, maxlimit);
    }
}

</script>
</head>
<body>
	<page:applyDecorator name="show_head">
		<page:param name="desc"><c:if test="${verifyStatus eq '-2'}">批发商开发票详情</c:if>
		<c:if test="${verifyStatus eq '0'}">审核发票</c:if>
		<c:if test="${verifyStatus eq 'ne0'}">已审核发票</c:if>
		<c:if test="${verifyStatus eq '1'}">开票明细</c:if>
		<c:if test="${verifyStatus eq '-1'}">开具发票</c:if></page:param>
	</page:applyDecorator>
	<form:form id="searchForm" action="${ctx}/invoice/limit/verifyinvoice/${uuid}" method="post" class="breadcrumb form-search"><!-- ${details[0].invoiceNum} 改为了${uuid} -->
	<input id="createInvoiceHid" name="createInvoiceHid" value="" type="hidden"/>
	<div style="margin-top: 8px;">
		<table id="contentTable" class="activitylist_bodyer_table"
			style="border-top: 1px solid #dddddd">
			<thead>
				<tr>
					<th>序号</th>
					<th>订单类型</th>
					<th>订单号</th>
					<th>团号</th>
					<th>申请人</th>
<!-- 					<th>下单时间</th> -->
					<th>人数</th>
<!-- 					<th>出/截团日期</th> -->
					<th>应收金额</th>
					<th>财务到账</th>
					<th>已退款金额</th>
					<th>已开票金额</th>
					<th>已开收据金额</th>
					<c:if test="${verifyStatus ne 'ne0'}">
						<c:choose>
							<c:when test="${verifyStatus eq '0'}">
								<th>需审核发票金额</th>
							</c:when>
							<c:otherwise>
								<th>本次开票</th>
							</c:otherwise>
						</c:choose>
					</c:if><%-- 发票详情中显示 本次开票 列 --%>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${limits}" var="lim" varStatus="s">
					<tr>
						<td>${s.count}</td>
						<td class="tc">${lim[12]}</td>
						<td class="tc">${lim[0]}</td>
						<td class="tc">${lim[1]}</td>
 						<td class="tc">${lim[3]}</td> 
						<c:set var="supplyBy" value="${lim[3]}" />
						<!--<td class="tc">${lim[4]}</td>-->
						<td class="tc">${lim[5]}</td>
						<%-- <td class="tc" style="padding: 0px;">
						<c:if test="${lim[12] ne '6' && lim[12] ne '7'}">
							<div class="out-date">
							<c:if test="${not empty lim[6] }">
								<fmt:formatDate pattern="yyyy-MM-dd" value="${lim[6]}" /></c:if>
							</div>
							<div class="close-date">
							<c:if test="${not empty lim[7] }">
								<fmt:formatDate pattern="yyyy-MM-dd" value="${lim[7]}" /></c:if>
							</div>
						</c:if>
						</td> --%>
						<td class="tr"><c:if test="${not empty lim[8]}"></c:if>${fns:getMoneyAmountBySerialNum(lim[8],2)}</td>
						<td class="tr"><c:if test="${not empty lim[13]}"></c:if>${fns:getMoneyAmountBySerialNum(lim[13],2)}</td>
						<td class="tr">¥${lim[14] }</td>
						<td class="tr"><c:if test="${not empty lim[10]}">¥</c:if> <fmt:formatNumber
								type="currency" pattern="#,##0.00" value="${lim[10]}" /></td>
						<td class="tr">¥${lim[4] }</td>
						<c:if test="${verifyStatus ne 'ne0'}">
							<td class="tr"><c:if test="${not empty lim[11]}">¥</c:if>
								<fmt:formatNumber type="currency" pattern="#,##0.00" value="${lim[11]}" />
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="sup_detail_top">
		<ul class="team_co clearFix vote-ul">
			<%--需求0444--%>
			<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
			<li><label>申请方式：</label>
			<c:if test="${details[0].applyInvoiceWay == '1'}">预开发票</c:if>
			<c:if test="${details[0].applyInvoiceWay == '0'}">正常申请</c:if></li>
			</c:if>
			<%--需求0444--%>
		<%--需求0411，越谏行踪不显示开票项目  0453起航假期也需隐藏--%>
			<c:if test="${!isYJXZ and fns:getUser().company.uuid ne '5c05dfc65cd24c239cd1528e03965021'}">
			<li><label>开票项目：</label>
					${invoiceSubjects[details[0].invoiceSubject] }</li>
			</c:if>
			 <!-- 0453需求,开票类型替换成开票项目-->
        	<c:choose>
	            <c:when test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}"> 
				<li><label>开票项目：</label>
					${invoiceSubjects[details[0].invoiceSubject] }</li>
				</c:when>
		       	<c:otherwise>
		       		<li><label>开票类型：</label>
					${invoiceTypes[details[0].invoiceType]}</li>
		       	</c:otherwise>
	        </c:choose>
	        <!-- 0453需求,开票类型替换成开票项目-->
				
			<li><label>开票方式：</label>
				${invoiceModes[details[0].invoiceMode] }</li>
			<%--需求0444--%>
			<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
			<li style="width:50%"><label>回款状态：</label>
			<c:choose>
				<c:when test="${details[0].applyInvoiceWay == '1'}">
					<c:if test="${details[0].receivedPayStatus == '0'}">未回款</c:if>
					<c:if test="${details[0].receivedPayStatus == '1'}">已回款</c:if></li>
				</c:when>
				<c:otherwise>已回款</li></c:otherwise>
			</c:choose>
			</c:if>
			<%--需求0444--%>
			<c:if test="${verifyStatus eq '1' || verifyStatus eq '-2'}">
				<li style="width:100%;"><label>发票号&nbsp;&nbsp;&nbsp;：</label>
					<c:choose>
						<c:when test="${fn:contains(details[0].invoiceNum, 'TTS')}"></c:when>
						<c:otherwise>${details[0].invoiceNum}</c:otherwise>
					</c:choose>
				</li>
			</c:if>
			<li style="width:100%;"><label>发票抬头：</label> ${details[0].invoiceHead}</li>
			<li><label>来款单位：</label> ${details[0].invoiceComingUnit}</li>
			<c:if test="${companyUUID == '049984365af44db592d1cd529f3008c3'}">
				<li><label>出票单位：</label>
					<c:if test="${details[0].invoiceComeFromCompany == '3'}">
						北京鼎鸿假日国际旅行社有限公司</li>
						</c:if>
					<c:if test="${details[0].invoiceComeFromCompany == '2'}">
						北京鼎鸿假期旅行社有限公司</li>	
					</c:if>
			</c:if>
			<li style="width:100%;"><label>开票客户：</label> ${details[0].invoiceCustomer }</li>
			<c:if test="${verifyStatus eq '0'}"><li><label>申请人：</label> ${supplyBy}</li>
				<li><label>开票金额：</label>
				 ¥ <fmt:formatNumber type="currency" pattern="#,##0.00" value="${invoiceMoney }" />
				</li>
			</c:if>
			<c:if test="${verifyStatus eq '1' || verifyStatus eq '-2'}">
				<li><label>开票日期：</label>
					<c:if test="${details[0].createStatus eq 1 }">
						<fmt:formatDate pattern="yyyy-MM-dd" value="${details[0].updateDate}" />
					</c:if>
				</li>
				<li class="li100" style="width:100%;"><label>开票备注：</label><span class="tj-hbbz-ys">${details[0].invoiceRemark}</span></li>
			</c:if>
			<c:if test="${verifyStatus eq '0' || verifyStatus eq '-1'}">
				<li style="width:100%">
					<label>开票原因：</label> 
					<span id="invoiceReason" title="${details[0].remarks }">${details[0].remarks }</span>
				</li>
				<c:if test="${verifyStatus eq '-1'}">
					<li style="width:100%">
						<label>审核备注：</label>
						<span id="review_remark" title="${details[0].reviewRemark }">${details[0].reviewRemark }</span>
					</li>
				</c:if>
			</c:if>
			<c:if test="${verifyStatus eq '0'}">
				<li style="width:100%; height:80px;"><label>审核人备注：</label>
					<textarea name="reviewRemark" style="width:600px; height:60px;resize:none;"
					onKeyDown="limitTextArea(this)" onKeyUp="limitTextArea(this)" onkeypress="limitTextArea(this)" ></textarea>
				</li>
			</c:if>
			<li class="clear"></li>
			<c:if test="${verifyStatus eq '-1'}">
			<li style="width:100%;"><label><font style="color:#ff0000; padding-right:5px;">*</font>发票号&nbsp;&nbsp;&nbsp;：</label><input type="text" name="invoiceNum" value="" maxlength="20" class="required"/></li>
			<li style="width:100%;"><label><font style="color:#ff0000; padding-right:5px;">*</font>开票日期&nbsp;&nbsp;：</label>
				<jsp:useBean id="now" class="java.util.Date"></jsp:useBean>
				<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" var="nowDate"/>
				<input class="inputTxt dateinput required" name="invoiceTime" value="${nowDate }"  onClick="WdatePicker()" readonly/>
			</li>
			<li style="width:100%; height:60px;"><label>开票备注：</label>
                <textarea name="invoiceRemark" style="width:200px; height:45px;"></textarea>
            </li>
			</c:if>
		</ul>
	</div>
	<br />
	<c:if test="${verifyStatus eq '-2' || verifyStatus eq '1'}">
		<div class="ydBtn">
		<a class="ydbz_x" href="javascript:void(0)"
			onClick="javascript:window.close();">关闭</a>
		</div>
	</c:if>
	<c:if test="${verifyStatus eq '-1'}">
	<div class="ydBtn" style="margin-top:50px;width: 286px !important;">
		<input type="button" value="返   回" onclick="history.go(-1)" class="btn btn-primary" id="btnCancel">
		<input id="createInvoice" class="btn btn-primary" type="button" value="开票" onClick="createInvoiceSubmit()" class="required"/>
	</div>
	</c:if>
	<c:if test="${verifyStatus eq '0'}">
	<div class="ydBtn" style="margin-top:50px;width: 286px !important;">
		<input type="button" value="返   回" onclick="history.go(-1)" class="btn btn-primary" id="btnCancel">
		<input id="backInvoice" class="btn btn-primary" type="button" value="驳回" onClick="denyInvoiceSubmit('${uuid}')" class="required"/>
		<input id="checkInvoice" class="btn btn-primary" type="button" value="审批通过" onClick="verifyInvoiceSubmit('${uuid}')" class="required"/>
	</div>
	</c:if>
	<div style="clear: both"></div>
	</form:form>


</body>
</html>