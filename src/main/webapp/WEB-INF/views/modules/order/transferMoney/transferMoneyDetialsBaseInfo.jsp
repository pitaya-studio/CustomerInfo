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
								<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${oldBean.productOrderCommon.createDate}" /></td> 
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${oldBean.orderTotalMoney}</em></td>
                                <td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">
										
								${fns:getDictLabel(oldBean.productOrderCommon.payStatus, "order_pay_status", "")}
								<!-- 
												<c:if test="${oldBean.productOrderCommon.orderType==0}">全部</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==1}">全款未支付</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==2}">订金未支付</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==3}">已占位</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==4}">订金已经支付</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==5}">已经支付</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==99}">已经取消订单</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==100}">可操作状态      正向平台同步</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==101}">查看状态      正向平台同步</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==111}">已经删除订单</c:if>
												<c:if test="${oldBean.productOrderCommon.orderType==199}">财务订单</c:if> -->
								 </td>	 
                                </tr>
							<tr>
								<td class="mod_details2_d1">操作人：</td>
						        <td class="mod_details2_d2">${oldBean.productOrderCommon.createBy.updateBy.name}</td>
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
								<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${newBean.productOrderCommon.createDate}" /></td> 
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${newBean.orderTotalMoney}</em></td>
                                <td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">
										
								${fns:getDictLabel(newBean.productOrderCommon.payStatus, "order_pay_status", "")}
								<!-- 
												<c:if test="${newBean.productOrderCommon.orderType==0}">全部</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==1}">全款未支付</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==2}">订金未支付</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==3}">已占位</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==4}">订金已经支付</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==5}">已经支付</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==99}">已经取消订单</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==100}">可操作状态      正向平台同步</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==101}">查看状态      正向平台同步</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==111}">已经删除订单</c:if>
												<c:if test="${newBean.productOrderCommon.orderType==199}">财务订单</c:if> -->
								 </td>	 
                                </tr>
							<tr>
								<td class="mod_details2_d1">操作人：</td>
						        <td class="mod_details2_d2">${newBean.productOrderCommon.createBy.updateBy.name}</td>
							</tr>
						</tbody>
					</table>
               </div>
               	<div class="ydbz_tit">游客转款</div>
				<table id="" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            <th width="10%">游客</th>
                            <th width="15%">转入团号</th>
                             <th width="15%">备注</th>
                            <th width="15%">应收金额</th>
                            <th width="15%">转款金额</th>
                        </tr>
                    </thead>
                    <tbody>
						<tr>
							<td align="center">${traveler.traveler.name} </td>
							<td  align="center">${traveler.groupNo}</td>
							 <td  align="center">转团转款</td>
							<td class="tr">
									${traveler.orderMoney}
							 </td >
							 <td class="tr">	
							    	${traveler.transferMoney}
							 </td>		
						</tr>
                    </tbody>
                </table>