<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
	    <meta name="decorator" content="wholesaler"/>
		<title>
            <c:choose>
                <c:when test="${title eq 1}">担保变更-详情 </c:when>
                <c:otherwise>担保变更申请</c:otherwise>
            </c:choose>
        </title>
		<script src="${ctxStatic}/js/guarantee.js"></script>
		<script type="text/javascript">

			function selectGrt(obj){//选中担保时金额等置灰
				if($(obj).val()=="1"||$(obj).val()=="4"){
					$(obj).parent().next().find("select").attr("style","width:27%; display:none");
					$(obj).parent().next().find("input").attr("style","width:60%;display: none");
				}else {
					$(obj).parent().next().find("select").attr("style","width:27%;");
					$(obj).parent().next().find("input").attr("style","width:60%;");
				}
			}

			function passOrDeny(type){
				var reviewUuid = $("#tableId").find("td").first().attr("id");
				$.jBox($("#batch-verify-list").html(), {
					title: "备注：", buttons: {'取消': 0,'提交': 1}, submit: function (v, h, f) {
						if(v == 1) {
							var reason = f.reason;
							$.ajax({
								type:"POST",
								url:"${ctx}/guaranteeMod/approveOrReject",
								data:{reviewUuid:reviewUuid,reason:reason,type:type},
								success:function(data){
									window.opener.location.reload();
									window.close();
								},
								error:function(){
									alert('返回数据失败');
								}
							});
						}
					}, height: 250, width: 350
				});
				inquiryCheckBOX();
			}

		</script>
	</head>
<body>
	<!--66 start-->
	<%--161009 bug16225 将hidden换成display:none 兼容ie S--%>
	<input id="deptId" name="deptId" value="${visaProduct.deptId}" style="display:none;">
	<input id="orderId" name="orderId" value="${visaOrder.id}" style="display:none;">
	<%--161009 bug16225 将hidden换成display:none 兼容ie E--%>
	<div class="mod_nav">
		<c:choose>
			<c:when test="${flag eq 'approval'}">
                订单 > 销售签证订单 > 担保变更 > 担保变更审批
			</c:when>
            <c:when test="${flag eq 'detail'}">
                <c:choose>
                    <c:when test="${nav eq 1}">订单 > 销售签证订单 > 担保变更 > 担保变更审批详情</c:when>
                    <c:otherwise>订单 > 销售签证订单 > 担保变更申请</c:otherwise>
                </c:choose>
            </c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${review eq 1}">订单 > 担保变更 > 担保变更审批详情</c:when>
					<c:otherwise>订单 > 销售签证订单 > 担保变更申请</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="ydbzbox fs">
		<div class="ydbz_tit">订单详情</div>
		<ul class="ydbz_info ydbz_infoli25">
			<li>
				<span>销售：</span>
				${visaOrder.salerName}
			</li>
			<li>
				<span>下单时间：</span>
				<fmt:formatDate value="${visaOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</li>
			<li>
				<span>团队类型：</span>
				单办签
			</li>
			<li>
				<span>收客人：</span>
				${visaOrder.salerName}
			</li>
			<li>
				<span>订单号：</span>
				${visaOrder.orderNo}
			</li>
			<li style="width: 51%;">
				<span>订单团号：</span>
				${visaOrder.groupCode}
			</li>
			<li>
				<span>订单金额：</span>
				${fns:getMoneyAmountBySerialNum(visaOrder.totalMoney,2)}
			</li>
			<li>
				<span>操作人：</span>
				${visaOrder.updateBy.name}
			</li>
			<li>
				<span>办签人数：</span>
				${visaOrder.travelNum}
			</li>
			<li>
				<span>下单人：</span>
				${visaOrder.createBy.name}
			</li>
		</ul>
		<div class="ydbz_tit">产品信息</div>
		<div class="orderdetails2">
			<p class="ydbz_mc">${visaProduct.productName}</p>
			<ul class="ydbz_info">
				<li>
					<span>产品编号：</span>
					${visaProduct.productCode}
				</li>
				<li>
					<span>签证国家：</span>
					${country.countryName_cn}
				</li>
				<li>
					<span>签证类别：</span>
					${fns:getDictLabel(visaProduct.visaType,'new_visa_type','')}
				</li>
				<li>
					<span>领区：</span>
					${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
				</li>
				<li>
					<span>应收价格：</span>
					${fns:getCurrencyNameOrFlag(visaProduct.currencyId, '0') }${visaProduct.visaPay}
				</li>
				<li>
					<span>创建时间：</span>
					<fmt:formatDate value="${visaProduct.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</li>
			</ul>
		</div>
		<div class="ydbz_tit">游客担保变更</div>
        <p>申请时间：<span><fmt:formatDate value="${createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>

		<table id="contentTable" class="activitylist_bodyer_table" style="margin:10px;width: 99%;">
			<thead>
				<tr>
					<th width="5%">游客</th>
					<th width="5%">原担保类型</th>
					<th width="7%">原押金金额</th>
					<!-- <th width="10%">下单时间</th> -->
					<th width="10%">申请担保类型</th>
					<th width="10%">申请交押金金额</th>
					<th width="10%">备注</th>

				</tr>
			</thead>
			<tbody id="tableId">
				<c:forEach items="${travelerList}" var="traveler">
					<c:choose>
						<c:when test="${flag eq 'apply'}">
							<tr>
								<td><input id="${traveler.id}" class="onceChk" type="checkbox" name="onceChk" onclick="">${traveler.travelerName}</td>
								<td class=" tc fbold">
									<c:choose>
										<c:when test="${traveler.guaranteeStatus eq 1}">担保</c:when>
										<c:when test="${traveler.guaranteeStatus eq 2}">担保+押金</c:when>
										<c:when test="${traveler.guaranteeStatus eq 3}">押金</c:when>
										<c:when test="${traveler.guaranteeStatus eq 4}">无需担保</c:when>
									</c:choose>
								</td>
								<td class=" tc fbold"><c:if test="${traveler.guaranteeStatus eq 2 or traveler.guaranteeStatus eq 3}">${traveler.totalDeposit}</c:if></td>
								<td class="tc useLess">
									<select onchange="selectGrt(this)"class="selectGuarantee">
										<option value="1">担保</option>
										<option value="2">担保+押金</option>
										<option value="3">押金</option>
										<option value="4">无需担保</option>
									</select>
								</td>
								<td class="useLess">
									<select onchange="selectGrt(this)" style="width:27%;display: none">
										<c:forEach items="${currencyList}" var="currency">
											<option value="${currency.id}" <c:if test="${currency.currencyName eq '人民币'}">selected</c:if>>${currency.currencyName}</option>
										</c:forEach>
									</select>
									<input  style="width:60%;display: none" type="text" onafterpaste="refundInput(this)" onkeyup="refundInput(this)" class="jbox-width100" value="">
								</td>
								<td class="tc useLess">
									<input value=""type="text" maxlength="50" class="jbox-width100">

								</td>
							</tr>
						</c:when>
						<c:when test="${flag eq 'detail'}">
							<tr>
								<td class="tc">${traveler.travelerName}</td>
								<td class=" tc fbold">
									<c:choose>
										<c:when test="${traveler.guaranteeStatus eq 1}">担保</c:when>
										<c:when test="${traveler.guaranteeStatus eq 2}">担保+押金</c:when>
										<c:when test="${traveler.guaranteeStatus eq 3}">押金</c:when>
										<c:when test="${traveler.guaranteeStatus eq 4}">无需担保</c:when>
									</c:choose>
								</td>
								<td class=" tc fbold"><c:if test="${traveler.guaranteeStatus eq 2 or traveler.guaranteeStatus eq 3}">${traveler.totalDeposit}</c:if></td>
								<td class="tc">
									<c:choose>
										<c:when test="${traveler.newGuaranteeType eq 1}">担保</c:when>
										<c:when test="${traveler.newGuaranteeType eq 2}">担保+押金</c:when>
										<c:when test="${traveler.newGuaranteeType eq 3}">押金</c:when>
										<c:when test="${traveler.newGuaranteeType eq 4}">无需担保</c:when>
									</c:choose>
								</td>
								<td class="tc">
                                    <c:if test="${traveler.newGuaranteeType eq 2 or traveler.newGuaranteeType eq 3}">${fns:getCurrencyNameOrFlag(traveler.currencyId, '0') }${traveler.amount}</c:if>
								</td>
								<td class="tc">
										${traveler.remark}<c:set var="rid" value="${traveler.reviewUuid}"></c:set>
								</td>
							</tr>
						</c:when>
						<c:when test="${flag eq 'approval'}">
							<tr>
								<td class="tc" id="${traveler.reviewUuid}">${traveler.travelerName}</td>
								<td class=" tc fbold">
									<c:choose>
										<c:when test="${traveler.guaranteeStatus eq 1}">担保</c:when>
										<c:when test="${traveler.guaranteeStatus eq 2}">担保+押金</c:when>
										<c:when test="${traveler.guaranteeStatus eq 3}">押金</c:when>
										<c:when test="${traveler.guaranteeStatus eq 4}">无需担保</c:when>
									</c:choose>
								</td>
								<td class=" tc fbold"><c:if test="${traveler.guaranteeStatus eq 2 or traveler.guaranteeStatus eq 3}">${traveler.totalDeposit}</c:if></td>
								<td class="tc">
									<c:choose>
										<c:when test="${traveler.newGuaranteeType eq 1}">担保</c:when>
										<c:when test="${traveler.newGuaranteeType eq 2}">担保+押金</c:when>
										<c:when test="${traveler.newGuaranteeType eq 3}">押金</c:when>
										<c:when test="${traveler.newGuaranteeType eq 4}">无需担保</c:when>
									</c:choose>
								</td>
								<td class="tc">
										${fns:getCurrencyNameOrFlag(traveler.currencyId, '0') }${traveler.amount}
								</td>
								<td class="tc">
										${traveler.remark}<c:set var="rid" value="${traveler.reviewUuid}"></c:set>
								</td>

							</tr>
						</c:when>
					</c:choose>
				</c:forEach>
			</tbody>
		</table>

		<c:choose>
			<c:when test="${flag eq 'apply'}">
				<div class="ydbz_sxb" id="secondDiv">
					<div class="ydBtn ydBtn2">
						<a class="ydbz_x gray" href="javascript:window.close();">取消</a>
						<a class="ydbz_x" onClick="submitCheck('${ctx}');">提交申请</a>

					</div>
				</div>
			</c:when>
			<c:when test="${flag eq 'detail'}">
				<div class="mod_information">
					<div class="mod_information_d">
						<div class="ydbz_tit">审核动态</div>
					</div>
					<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
				</div>
				<div class="ydbz_sxb" id="secondDiv">
					<div class="ydBtn ydBtn2"style="width: 204px">
						<a class="ydbz_x gray" href="javascript:window.close();">关闭</a>
					</div>
				</div>
			</c:when>
			<c:when test="${flag eq 'approval'}">
				<div class="mod_information">
					<div class="mod_information_d">
						<div class="ydbz_tit">审核动态</div>
					</div>
					<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
				</div>
				<div class="ydbz_sxb" id="secondDiv">
					<div class="ydBtn ydBtn2"style="width: 204px">
						<a class="ydbz_x gray" href="javascript:window.close();">取消</a>
						<a class="ydbz_x" onClick="passOrDeny(1);">驳回</a>
						<a class="ydbz_x" onClick="passOrDeny(2);">审批通过</a>
					</div>
				</div>
			</c:when>
		</c:choose>
	</div>
	<!--66 END-->

	<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
		<table width="100%" style="padding:10px !important; border-collapse: separate;">
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> &nbsp;</td>
			</tr>
			<tr>
				<td><p>请填写您的审批备注！</p></td>
			</tr>
			<tr>
				<td><label>
					<textarea name="reason" id="reason" style="width: 290px;" maxlength="200"></textarea>
				</label></td>
			</tr>
		</table>
	</div>
</body>
</html>