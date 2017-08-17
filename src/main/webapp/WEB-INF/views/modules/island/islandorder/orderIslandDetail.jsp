<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单详情</title>
<meta name="decorator" content="wholesaler" />
<script	src="${ctxStatic}/modules/island/islandorder/islandOrderCommon.js"	type="text/javascript"></script>
<script	src="${ctxStatic}/modules/island/islandorder/islandAsynchronousData.js"	type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
//
var orderInfo = {
    traveller: 
    [
		<c:forEach items="${groupPrices }" var="groupPrice" varStatus="status">
		{ type: "${groupPrice.uuid}", 
		  name: "<trekiz:autoId2Name4Table tableName='traveler_type' sourceColumnName='uuid' srcColumnName='name' value='${groupPrice.travelerType}'/>",
		  cost: { 
			  "${groupPrice.currencyId}":{
				  	  code:'<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId}"/>',
				  	  price:"${groupPrice.price}",
				  }
			  }
		},
		</c:forEach>
    ],
    // 总人数
    totalCount: 0,
};

</script>

</head>
<body>
	<input type="hidden" name="ctx" id="ctx" value="${ctx}" />
	<input type="hidden" name="islandOrderUuid" id="islandOrderUuid" value="${islandOrder.uuid }" />
	<input type="hidden" name="islandOrderId" id="islandOrderId" value="${islandOrder.id }" />	
	<input type="hidden" name="costMoneyUuid" id="costMoneyUuid" value="${islandOrder.costMoney }">
	<input type="hidden" name="payedMoneyUuid" id="payedMoneyUuid" value="${islandOrder.payedMoney }">
	<input type="hidden" name="totalMoneyUuid" id="totalMoneyUuid" value="${islandOrder.totalMoney }">

		<page:applyDecorator name="show_head">
		    <page:param name="desc">订单详情</page:param>
		</page:applyDecorator>
		<div class="ydbzbox fs">
			<%@ include file="/WEB-INF/views/modules/island/islandorder/orderIslandBaseinfo.jsp"%>

			<div class="ydbz_tit"><!--  <span class="ydExpand" data-target="#bookingPeopleInfo"></span>-->填写预订人信息</div>
			<div id="bookingPeopleInfo">
				<form id="orderpersonMesdtail">
					<div class="mod_information_dzhan" id="secondStepDiv">
						<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
							<div class="mod_information_d2 ">
								<label><span class="xing">*</span>渠道：</label> 
									<c:if test="${islandOrder.orderCompany == -1 }">非签约渠道</c:if> 
									<c:if test="${islandOrder.orderCompany != -1 }">签约渠道</c:if> 
							</div>
							<div class="mod_information_d2" id="signChannel" <c:if test="${islandOrder.orderCompany == -1 }">style="display: none"</c:if>>
								<label><span class="xing">*</span>渠道总社：</label> 
									<c:forEach items="${agentList}" var="agentinfo">
										<c:if test="${islandOrder.orderCompany == agentinfo.id }">${agentinfo.agentName}</c:if>
									</c:forEach>
								</select>
							</div>
							<div class="mod_information_d2" id="nonChannel" <c:if test="${islandOrder.orderCompany != -1 }">style="display: none"</c:if> >
								<label class="price_sale_house_label02"><span class="xing">*</span>非签约渠道名称：</label> 
								<input class="valid" type="text" name="orderCompanyName" id="orderCompanyName" value="${islandOrder.orderCompanyName }"/>
							</div>
						</div>
					</div>
					<p class="ydbz_qdmc" style="padding: 12px 0px;"></p>
					
					<!--签约渠道-->
					<div id="signChannelList">
						<c:forEach items="${orderContactsList }" var="orderContacts" varStatus="status">
							<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
								<li><label><span class="xing">*</span>渠道联系人：</label> 
									${orderContacts.contactsName }
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									${orderContacts.contactsTel }
									<div class="zksx boxCloseOnAdd"	onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li><label>固定电话：</label> ${orderContacts.contactsTixedTel }</li>
										<li><label>渠道地址：</label> ${orderContacts.contactsAddress }</li>
										<li><label>传真：</label> ${orderContacts.contactsFax }</li>
										<li><label>QQ：</label> ${orderContacts.contactsQQ }</li>
										<li><label>Email：</label> ${orderContacts.contactsEmail }</li>
										<li><label>渠道邮编：</label> ${orderContacts.contactsZipCode }</li>
										<li><label>其他：</label> ${orderContacts.remark }</li>
									</ul>
								</li>
							</ul>
						</c:forEach>
					</div>
				</form>
			</div>

			<div class="ydbz_tit"><!--  <span class="ydExpand" data-target="#costTable"></span>-->费用及人数</div>
			<div id="costTable">
				<table id="moneyAndPeopleTab" class="table activitylist_bodyer_table_new contentTable_preventive">
					<thead>
						<tr>
							<th width="12%">舱位等级</th>
							<th width="13%">游客类型</th>
							<th width="25%">同行价/人</th>
							<th width="25%"><span class="xing">*</span>人数</th>
							<th width="25%">小计</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${groupPrices }" var="groupPrice">
							<tr id="${groupPrice.uuid}" class="groupPrices_tr">
								<td class="tc">${fns:getDictLabel(groupPrice.spaceLevel, 'spaceGrade_Type', '-')}</td>
								<td class="tc">
									<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.travelerType}"/>
								</td>
								<td class="tc">
									<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/><fmt:formatNumber  type="currency" pattern="##0.00" value="${groupPrice.price}" /></span>
								</td>
								<td class="tc"><input type="hidden" class="price_sale_house_w100" name="orderPersonNum" id="orderPersonNum" value="${groupPrice.num }"/>${groupPrice.num }</td>
								<td class="tc"><span>${groupPrice.subTotal }</span></td>
							</tr>
						</c:forEach>
						<tr>
							<td colspan="5" class="tr">
								<span class="price_sale_houser_25"><label>合计人数：</label>
									<em> <span id="totalPeopleCount"></span> 人</em>
								</span> 
								<span class="price_sale_houser_25"><label>合计金额：</label>
									<em><i><span class="totalCost" id="totalPeopleMoney"></span></i></em>
								</span>
							</td>
						</tr>
						<tr>
							<td colspan="5" class="tl">
								<span class="price_sale_houser_25" style="margin-left:89px;"><label>单房差：</label>
									<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityIslandGroup.currencyId }"/><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activityIslandGroup.singlePrice}" />
								</span>
								<span class="price_sale_houser_25" style="margin-left:200px;"><label>需交定金：</label>
									<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityIslandGroup.frontMoneyCurrencyId }"/><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activityIslandGroup.frontMoney}" />
								</span>
							</td>
						</tr>
					</tbody>
				</table>
				
				<!-- 预报名间数 -->
				<ul class="ydbz_qd_02" style="background-color:#FBFBFB;">
					<c:set var="forecaseReportRoomNum" value="${not empty islandOrder.forecaseReportRoomNum ? islandOrder.forecaseReportRoomNum : 0}"></c:set>
				 	<c:set var="subControlNum" value="${not empty islandOrder.subControlNum ? islandOrder.subControlNum : 0}"></c:set>
				 	<c:set var="subUnControlNum" value="${not empty islandOrder.subUnControlNum ? islandOrder.subUnControlNum : 0}"></c:set>
				 	<c:set var="sumNum" value="${subControlNum + subUnControlNum}"></c:set>
					<li style="width:400px;">
						<label>预报名间数：${forecaseReportRoomNum }间</label>
						<label>酒店扣减间数：控房 ${subControlNum}间</label>
						<label>非控房 ${subUnControlNum}间</label>
					</li>
					<li>
						<label style=" margin-left:60px">合计：${sumNum}间</label>
					</li>
				 </ul>
				 
				 <!-- 预报名票数 -->
				 <ul class="ydbz_qd_02" style="background-color:#FBFBFB;">
				 	<c:set var="forecaseReportTicketNum" value="${not empty islandOrder.forecaseReportTicketNum ? islandOrder.forecaseReportTicketNum : 0}"></c:set>
				 	<c:set var="subControlTicketNum" value="${not empty islandOrder.subControlTicketNum ? islandOrder.subControlTicketNum : 0}"></c:set>
				 	<c:set var="subUnControlTicketNum" value="${not empty islandOrder.subUnControlTicketNum ? islandOrder.subUnControlTicketNum : 0}"></c:set>
				 	<c:set var="sumTicketNum" value="${subControlTicketNum + subUnControlTicketNum}"></c:set>
					<li style="width:400px;">
						<label>预报名票数：${forecaseReportTicketNum }张</label>
						<label>机票扣减票数：控票 ${subControlTicketNum}张</label>
						<label>非控票 ${subUnControlTicketNum}张</label>
					</li>
					<li>
						<label style=" margin-left:60px">合计：${sumTicketNum}张</label>
					</li>
				 </ul>
			</div>
			
			<!--费用调整开始-->
			<div class="ydbz_tit"><!-- <span class="ydExpand" data-target="#adjustCost"></span>-->费用调整</div>
			<form 	class=" form-search" id="adjustCost" novalidate="novalidate">
				<div class="mod_information_dzhan" id="dddddStepDiv">
					<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
						<table class="contentTable_preventive table_padings" id="add_other_charges_table">
							<c:forEach items="${islandOrderPriceList }" var="islandOrderPrice" varStatus="status" >
								<!-- 指定类型费用展示 -->
								<c:if test="${islandOrderPrice.priceType != 4 && islandOrderPrice.priceType != 1}">
									<tr>
										<td width="100" class="tr">
											${islandOrderPrice.priceTypeStr }金额：
											<input type="hidden" name="orderPriceUuid" id="orderPriceUuid" value="${islandOrderPrice.uuid }">
											<input type="hidden" name="orderPriceId" id="orderPriceId" value="${islandOrderPrice.id }">
											<input type="hidden" name="priceType" id="priceType" value="${islandOrderPrice.priceType }">
										</td>
										<td width="280" class="tl">
											<label> 
												<select class="w80" name="currencyId" id="currencyId" disabled="disabled">
													<c:forEach items="${currencyList }" var="currency">
														<option value="${currency.id }" <c:if test="${islandOrderPrice.currencyId == currency.id }">selected="selected"</c:if> data-currency="${currency.currencyMark}">${currency.currencyName}</option>
													</c:forEach>
												</select> 
											</label> 
											<input type="text" data-type="float" disabled="disabled"  id="orderPrice" name="orderPrice" class="price_sale_house_w93 <c:if test="${islandOrderPrice.priceType == 3 }">price</c:if>" value='<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${islandOrderPrice.price}" />'  />
										</td>
										<td width="180" class="tr">备注：</td>
										<td colspan="3">
											<span class="tl">${islandOrderPrice.remark }</span>
											<span class="padr10"></span>
										</td>
									</tr>
								</c:if>
								<!-- 其他费用展示 -->
								<c:if test="${islandOrderPrice.priceType == 4 || islandOrderPrice.priceType == '4' }">
									<tr>
										<td class="tr">金额名称：</td>
										<td>
											<input type="hidden" name="orderPriceUuid" id="orderPriceUuid" value="${islandOrderPrice.uuid }">
											<input type="hidden" name="orderPriceId" id="orderPriceId" value="${islandOrderPrice.id }">
											<input type="hidden" name="priceType" id="priceType" value="4">
											<span class="tl"> <input type="text" disabled="disabled" id="priceName" name="priceName" class="price_sale_house_w93" value="${islandOrderPrice.priceName }"  maxlength="" /></span>
										</td>
										<td class="tr tr_other_u">
											<input  type="radio" disabled="disabled" name="other_u_${status.index }" id="u138_input" class="dis_inlineblock"  value="1"  data-label="增加" <c:if test="${islandOrderPrice.price >= 0 }">checked="checked"</c:if> /><label for="u138_input">增加</label> 
											<input type="radio" disabled="disabled" name="other_u_${status.index }"  id="u138_input2" value="0" data-label="减少"  <c:if test="${islandOrderPrice.price < 0 }">checked="checked"</c:if> /><label for="u140_input">减少：</label>
										</td>
										<td class="tl">
											<label> 
												<select class="w80" name="currencyId" id="currencyId" disabled="disabled">
													<c:forEach items="${currencyList }" var="currency">
														<option value="${currency.id }" <c:if test="${islandOrderPrice.currencyId == currency.id }">selected="selected"</c:if> data-currency="${currency.currencyMark}">${currency.currencyName}</option>
													</c:forEach>
												</select>
											</label>
											<input type="text" disabled="disabled" id="orderPrice" name="orderPrice" value='<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${islandOrderPrice.price < 0 ? -islandOrderPrice.price : islandOrderPrice.price}" />'  data-type="float" class="price_sale_house_w93 price" />
										</td>
										<td class="tr">备注：</td>
										<td class="tl">
											${islandOrderPrice.remark }
											<span class="padr10"></span> 
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</table>
					</div>
				</div>
			</form>
			<!--费用调整结束-->
			
			<!--费用结算开始-->
			<div class="ydbz_tit"><!-- <span class="ydExpand" data-target="#costSettlement"></span>-->费用结算</div>
			<div id="costSettlement">
				<div class="mod_information_dzhan">
					<div class="mod_information_dzhan_d error_add1" style="overflow: hidden;">
						<div class="mod_information_d2">
							<label>订单总额：</label><span>${costMoneyStr }</span>
						</div>
						<div class="mod_information_d2 " style="width: 60%">
							<label>结算总额：</label><span class="red">${totalMoneyStr }</span>
						</div>
					</div>
				</div>
				<!-- <p class="ydbz_qdmc"></p>-->
				<p class="price_sale_houser_line"></p>
				<div class="mod_information_dzhan">
					<div class="mod_information_dzhan_d error_add1" style="overflow: hidden;">
						<div class="mod_information_d2">
							<label>应收金额：</label><span>${totalMoneyStr }</span>
						</div>
						<div class="mod_information_d2 ">
							<label>已收金额：</label><span>${payedMoneyStr }</span>
						</div>
						<div class="mod_information_d2 " style="width: 40%">
							<label>未收金额：</label><span>${noPayMoneyStr }</span>
						</div>
					</div>
				</div>
			</div>
			<!--费用结算结束-->
			<!--旅客信息开始-->
			<div class="ydbz_tit"><!-- <span class="ydExpand" data-target="#passengerInfo"></span>-->游客信息</div>
			<div id="passengerInfo">
				<table id="passengerInfoTable" class="table activitylist_bodyer_table_new contentTable_preventive" 	style="min-width: 1600px; display: ;">
					<thead>
						<tr>
							<th width="3%">序号</th>
							<th width="6%"><span class="xing">*</span>姓名</th>
							<th width="3%">英文姓名</th>
							<th width="3%">舱位等级</th>
							<th width="8%">游客类型</th>
							<th width="7%">性别</th>
							<th width="12%">签证国家及类型</th>
							<th width="25%">证件类型/证件号码/有效期</th>
							<th width="13%">价格</th>
							<th width="8%">备注</th>
							<th width="6%">资料上传</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${travelerList }" var="traveler" varStatus="status">
						<tr>
							<td class="tc">${status.index + 1 }</td>
							<td class="tc">
								<input type="hidden" name="travelerUuid" id="travelerUuid" value="${traveler.uuid }" />
								${traveler.name }
							</td>
							<td class="tc">${traveler.nameSpell }</td>
							<td class="tc">
								<c:forEach items="${spaceLevelList }" var="spaceLevel">
									<c:if test="${spaceLevel.spaceLevel == traveler.spaceLevel}">${spaceLevel.spaceLevelStr }</c:if>
								</c:forEach>
							</td>
							<td class="tc">
								<c:forEach items="${travelerTypeList }" var="travelerType">
									<c:if test="${travelerType.travelerType == traveler.personType }"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${travelerType.travelerType}"/></c:if>
								</c:forEach>
							</td>
							<td class="tc">
								<c:if test="${traveler.sex == '1' }">男</c:if>
								<c:if test="${traveler.sex == '2' }">女</c:if>
							</td>
							<td class="tc table_padings_none" id="travelerVisa_td">
								<c:forEach items="${traveler.islandTravelervisaList }" var="travelerVisa">
									<p>
										<input type="hidden" name="travelerVisaUuid" id="travelerVisaUuid" value="${travelerVisa.uuid }">
										<select name="countrySelect" id="countrySelect" class="w40b" disabled="disabled">
											<c:forEach items="${sysGeographyList }" var="sysGeography">
												<option value="${sysGeography.uuid}" <c:if test="${travelerVisa.country == sysGeography.uuid }">selected="selected"</c:if> >${sysGeography.nameCn }</option>
											</c:forEach>
										</select>
										<select name="visaTypeSelect" id="visaTypeSelect" class="w40b" disabled="disabled">
											<c:forEach items="${visaTypes }" var="visa">
												<option value="${visa.id }" <c:if test="${visa.id == travelerVisa.visaTypeId }">selected="selected"</c:if>>${visa.label}</option>
											</c:forEach>
										</select>
									</p>
								</c:forEach>
							</td>
							<td id="travelerPapers_td">
								<c:forEach items="${traveler.islandTravelerPapersTypeList }" var="travelerPapers">
									<p>
										<input type="hidden" name="travelerPapersUuid" id="travelerPapersUuid" value="${travelerPapers.uuid }" />
										<select name="papersTypeSelect" id="papersTypeSelect" class="w80" disabled="disabled">
											<option value="0" <c:if test="${travelerPapers.papersType == '0' }">selected="selected"</c:if> >请选择</option>
											<option value="1" <c:if test="${travelerPapers.papersType == '1' }">selected="selected"</c:if> >身份证</option>
											<option value="2" <c:if test="${travelerPapers.papersType == '2' }">selected="selected"</c:if> >护照</option>
											<option value="3" <c:if test="${travelerPapers.papersType == '3' }">selected="selected"</c:if> >警官证</option>
											<option value="4" <c:if test="${travelerPapers.papersType == '4' }">selected="selected"</c:if> >军官证</option>
											<option value="5" <c:if test="${travelerPapers.papersType == '5' }">selected="selected"</c:if> >其他</option>
										</select> 
										<input type="text" disabled="disabled" class="w130 input_pad" name="idCard" id="idCard" value="${travelerPapers.idCard }" /> 
										<input name="validityDate" id="validityDate" disabled="disabled" type="text" onclick="WdatePicker()" class="dateinput required w90 input_pad"  value="<fmt:formatDate value="${travelerPapers.validityDate }" pattern="yyyy-MM-dd"/>" /> 
									</p>
								</c:forEach>
							</td>
							<td class="tc" id="moneyAmount_td">
								<c:forEach items="${traveler.islandMoneyAmountList }" var="moneyAmount">
									<p>
										<input type="hidden" name="travelerMoneyUuid" id="travelerMoneyUuid"  value="${moneyAmount.uuid }" />
										<select name="currencyIdSelect" id="currencyIdSelect" class="w30b" disabled="disabled">
											<c:forEach items="${currencyList }" var="currency">
												<option value="${currency.id }" <c:if test="${moneyAmount.currencyId == currency.id }">selected="selected"</c:if> data-currency="${currency.currencyMark}">${currency.currencyName}</option>
											</c:forEach>
										</select> 
										<input type="text" disabled="disabled" name="travelerMoney" id="travelerMoney" data-type="float" class="w30b " value='<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${moneyAmount.amount}" />' />
									</p>
								</c:forEach>
							</td>
							<td class="tc">${traveler.remark }</td>
							<td class="tc" id="file_td">
								<a name="#" class="btn_addBlue_file" id="addcost" onclick="up_files_pop(this);">附件管理</a> <!--上传附件弹窗层开始-->
								<div class="up_files_pop" style="display: none;" id="up_files_pop">
									<ul style="margin-left: 0;">
										<c:forEach items="${traveler.islandTravelerFilesList}" var="travelerFiles">
											<li>
												<a class="padr10" href="javascript:void(0)" onclick="downloads('${travelerFiles.docId}')" >${travelerFiles.docName }</a> 
												<input type="hidden" name="hotelAnnexUuid" value="${travelerFiles.uuid }" />
											</li>
										</c:forEach>
									</ul>
								</div> <!--上传附件弹窗层开始-->
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				<!--旅客信息结束-->
			</div>
			
			<div flag="messageDiv" class="ydbz2_lxr">
					<p class="hotel_discount_count_mar20">
						<label>备注：</label>
						<textarea  name="hotelOrderRemark" id="hotelOrderRemark" readonly="readonly">${islandOrder.remark }</textarea>
					</p>
				</div>
			
			<!--旅客信息结束-->
			<div class="release_next_add">
				<input type="button" value="关闭" onclick="window.close()" class="btn btn-primary" />
			</div>
			
		</div>
</body>
</html>