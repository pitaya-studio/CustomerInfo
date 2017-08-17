<%@ page contentType="text/html;charset=UTF-8"%>
<div class="ydbz_tit">转出订单详情</div>
               <div class="orderdetails1">
               			<table border="0" style="margin-left: 25px" width="98%">
						<tbody>
							<tr>
                            	<td class="mod_details2_d1">订单编号：</td>
								<td class="mod_details2_d2">${oldBean.productOrderCommon.orderNum}</td>
                                <td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2"> ${oldBean.activitygroup.groupCode}</td>
                                <td class="mod_details2_d1"><em class="tdred"></em>团队类型：</td>
						        <td class="mod_details2_d2"> 
							        <c:choose>
										<c:when test="${oldBean.productOrderCommon.orderStatus == 1}">单团</c:when>
										<c:when test="${oldBean.productOrderCommon.orderStatus == 2}">散拼</c:when>
										<c:when test="${oldBean.productOrderCommon.orderStatus == 3}">游学</c:when>
										<c:when test="${oldBean.productOrderCommon.orderStatus == 4}">大客户</c:when>
										<c:when test="${oldBean.productOrderCommon.orderStatus == 5}">自由行</c:when>
									</c:choose>
						        </td>	
                                <td class="mod_details2_d1"><em class="tdred"></em>销售：</td>
						        <td class="mod_details2_d2">${oldBean.productOrderCommon.salerName}</td>
                                       
							</tr>
							<tr> 
                                <td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">${oldBean.productOrderCommon.createBy.name}</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${oldBean.productOrderCommon.createDate}" type="both" /></td> 
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${oldBean.orderTotalMoney}</em></td>
                                <td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">									
								${fns:getDictLabel(oldBean.productOrderCommon.payStatus, "order_pay_status", "")}
								 </td>	 
                                </tr>
							<tr>
								<td class="mod_details2_d1">操作人：</td>
						        <td class="mod_details2_d2">${oldBean.activitygroup.createBy.name}</td>
							</tr>
						</tbody>
					</table>
               </div>
			   <div class="ydbz_tit">转入订单详情</div>
               <div class="orderdetails1">
               			<table border="0" style="margin-left: 25px" width="98%">
						<tbody>
							<tr>
                            	<td class="mod_details2_d1">订单编号：</td>
								<td class="mod_details2_d2">${newBean.productOrderCommon.orderNum}</td>
                                <td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2"> ${newBean.activitygroup.groupCode}</td>
                                <td class="mod_details2_d1"><em class="tdred"></em>团队类型：</td>
						        <td class="mod_details2_d2"> 
						        	<c:choose>
											<c:when test="${newBean.productOrderCommon.orderStatus == 1}">单团</c:when>
											<c:when test="${newBean.productOrderCommon.orderStatus == 2}">散拼</c:when>
											<c:when test="${newBean.productOrderCommon.orderStatus == 3}">游学</c:when>
											<c:when test="${newBean.productOrderCommon.orderStatus == 4}">大客户</c:when>
											<c:when test="${newBean.productOrderCommon.orderStatus == 5}">自由行</c:when>
								</c:choose>
						        </td>	
                                <td class="mod_details2_d1"><em class="tdred"></em>销售：</td>
						        <td class="mod_details2_d2">${newBean.productOrderCommon.salerName}</td>
                                       
							</tr>
							<tr> 
                                <td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">${newBean.productOrderCommon.createBy.name}</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${newBean.productOrderCommon.createDate}" type="both" /></td> 
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${newBean.orderTotalMoney}</em></td>
                                <td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">
								${fns:getDictLabel(newBean.productOrderCommon.payStatus, "order_pay_status", "")}
								 </td>	 
                                </tr>
							<tr>
								<td class="mod_details2_d1">操作人：</td>
						        <td class="mod_details2_d2">${newBean.activitygroup.createBy.name}</td>
							</tr>
						</tbody>
					</table>
               </div>
                <div class="ydbz_tit">游客转款</div>
				<table id="" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            <th width="10%">游客</th>
                            <th width="15%">转入订单编号</th>
                            <th width="15%">转出团结算价</th>
                            <th width="15%">转入团结算价</th>
                            <th width="15%">转款金额</th>
                            <th width="15%">备注</th>
                        </tr>
                    </thead>
                    <tbody>
						<tr>
							<td class="tc" align="center">${traveler.traveler.name} </td>
							<td class="tc" align="center">${traveler.newOrder.orderNum}</td>
							<td class="tr"><span class="fbold tdgreen">${traveler.oldPayPriceMoney}</span></td>
							<td class="tr"><span class="fbold tdgreen">${traveler.newPayPriceMoney}</span></td>
							<td class="tr"><span class="fbold tdorange">${traveler.transferMoney}</span></td>		
							<td align="center"><c:if test="${empty traveler.remark }">——</c:if>${traveler.remark }</td>
						</tr>
                    </tbody>
                </table>