<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">
	.mod_information_dzhan{
       	width:100%;
    }
    /* .orderdetails1 span {
	    padding-left: 0px;
	    line-height: 28px;
	} */
</style>
<div class="ydbz_tit">订单详情</div>
<div class="orderdetails1">
	<table border="0" width="90%" style="margin-left:0;">
		<tbody>
			<tr>
	             <td class="mod_details2_d1">下单人：</td>
	             <td class="mod_details2_d2">${productorder.createBy.name}</td>
	             <td class="mod_details2_d1">下单时间：</td>
	             <td class="mod_details2_d2"><fmt:formatDate value="${productorder.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	             <td class="mod_details2_d1">团队类型：</td>
	             <td class="mod_details2_d2">${groupType}</td>
	             <td class="mod_details2_d1">销售：</td>
	             <td class="mod_details2_d2">${productorder.salerName}</td>
			</tr>
	         <tr>
	             <td class="mod_details2_d1">订单编号：</td>
	             <td class="mod_details2_d2">${productorder.orderNum}</td>
	             <td class="mod_details2_d1">订单团号：</td>
	             <td class="mod_details2_d2" title="${productGroup.groupCode}"><span id="groupCodeEle">${productGroup.groupCode}</span></td>
	             <td class="mod_details2_d1">订单总额：</td>
	             <td class="mod_details2_d2">${productorder.totalMoney}</td>
	             <td class="mod_details2_d1">订单状态：</td>
	             <td class="mod_details2_d2">${fns:getDictLabel(productorder.payStatus, "order_pay_status", "")}</td>
	         </tr>
	         <c:if test="${productorder.payStatus==3}">
				<tr>
					<td class="mod_details2_d1">占位类型：</td>
					<td class="mod_details2_d2">
	              	<c:choose>
						<c:when test="${productorder.payMode=='2'}">
							预占位
						</c:when>
						<c:when test="${productorder.payMode=='4'}">
							资料占位
						</c:when>
						<c:when test="${productorder.payMode=='5'}">
							担保占位
						</c:when>
						<c:when test="${productorder.payMode=='6'}">
							确认单占位
						</c:when>
						<c:when test="${productorder.payMode=='7'}">
							计调确认占位
						</c:when>
						<c:when test="${productorder.payMode=='8'}">
							财务确认占位
						</c:when>
					</c:choose>
					</td>
				</tr>
			</c:if>
			<tr>
				<td class="mod_details2_d1">操作人：</td>
	           	<td class="mod_details2_d2">${product.createBy.name}</td>
			</tr>
		</tbody>
	</table>
	<div class="mod_information_d7" style="width:auto;margin-left:25px"></div>
	<table border="0" width="90%" style="margin-left:0;">
		<tbody>
			<tr>
				<td class="mod_details2_d1" style="padding-top:8px;">发票号：</td>
				<td class="mod_details2_d2" style="overflow:hidden;">
					<c:forEach var="invoice" items="${invoices }" varStatus="varStatus">
						<c:if test="${varStatus.count > 1 }"></br></c:if>
						<a target="_blank" href="${ctx}/orderInvoice/manage/viewInvoiceInfo/${invoice.uuid}/-2/${productorder.orderStatus}">
						<span style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden; padding-left:0px; width:90%; display:inline-block;" title="${invoice.invoiceNum }">
							${invoice.invoiceNum }
						</span></a>
					</c:forEach>
				</td>
				<td class="mod_details2_d1" style="padding-top:8px">收据号：</td>
				<td class="mod_details2_d2" style="overflow:hidden;">
					<c:forEach var="receipt" items="${receipts }" varStatus="varStatus">
						<c:if test="${varStatus.count > 1 }"></br></c:if>
						<a target="_blank" href="${ctx}/receipt/limit/supplyviewrecorddetail/${receipt.uuid}/-2/${productorder.orderStatus}">
						<span style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden; padding-left:0px; width:90%; display:inline-block;" title="${receipt.invoiceNum }">
							${receipt.invoiceNum }
						</span></a>
					</c:forEach>
				</td>
				<td class="mod_details2_d1"></td>
                <td class="mod_details2_d2"></td>
                <td class="mod_details2_d1"></td>
            	<td class="mod_details2_d2"></td>
			</tr>
		</tbody>
	</table>
</div>
