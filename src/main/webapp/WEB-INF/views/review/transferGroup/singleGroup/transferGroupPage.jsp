<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>转团操作</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/modules/order/transfergroup/transferGroupApply.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			//搜索条件筛选
			launch();
			//操作浮框
			operateHandler();
			//团号和产品切换
			switchNumAndPro();
		});
	</script>
</head>
<body>
	<page:applyDecorator name="show_head">
    	<page:param name="desc">转团操作</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<div class="mod_nav"> 订单 > ${orderStatus} > 转团</div>
	<input type="hidden"  id="orderStatusNum"  value="${orderStatusNum}"/>
	<input type="hidden"  id="orderID"  value="${order.id}"/>
	<input type="hidden"  id="priceType"  value="${order.priceType}"/>
	<input type="hidden"  id="productType"  value="${order.orderStatus}"/>
	<div class="ydbzbox fs">
		<div class="orderdetails">
			<div class="orderdetails_tit">订单信息</div>
			<div class="orderdetails1">
				<table border="0" style="margin-left: 25px" width="98%">
					<tbody>
						<tr>
							<td class="mod_details2_d1">下单人：</td>
					        <td class="mod_details2_d2">${order.createBy.name}</td>
					        <td class="mod_details2_d1">销售：</td>
					        <td class="mod_details2_d2">${order.salerName}</td>
							<td class="mod_details2_d1">下单时间：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
							<td class="mod_details2_d1">团队类型：</td>
							<td class="mod_details2_d2">${orderStatus}</td>
						</tr>
						<tr> 
							<td class="mod_details2_d1">订单编号：</td>
							<td class="mod_details2_d2">${order.orderNum }</td>
							<td class="mod_details2_d1">订单团号：</td>
							<td class="mod_details2_d2" id="oldGroupCode">${group.groupCode }</td>
							<td class="mod_details2_d1">订单总额：</td>
							<td class="mod_details2_d2"><em class="tdred">${totalMoney }</em></td>
							<td class="mod_details2_d1">订单状态：</td>
							<td class="mod_details2_d2">
								<c:if test="${order.payStatus==1 }">未收全款</c:if>
								<c:if test="${order.payStatus==2 }">未收订金</c:if>
								<c:if test="${order.payStatus==3 }">已占位</c:if>
								<c:if test="${order.payStatus==4 }">已收订金</c:if>
								<c:if test="${order.payStatus==5 }">已收全款</c:if>
								<c:if test="${order.payStatus==99 }">已取消</c:if>
							</td>	 
						</tr>
						<tr>
							<td class="mod_details2_d1">操作人：</td>
							<td class="mod_details2_d2">${user.name }</td>     
						</tr>
					</tbody>
				</table>
			</div>
			<div class="orderdetails_tit">产品信息</div>
			<div class="orderdetails2">
				<p class="ydbz_mc">${product.acitivityName }</p>
				<ul class="ydbz_info">
					<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
					<li>
						<span>出团日期：</span> 
						<fmt:formatDate value="${group.groupOpenDate }"  pattern="yyyy-MM-dd"/>
					</li>
					<li><span>行程天数：</span>${product.activityDuration }天</li>
					<li id="mddtargetAreaNames" title="${product.targetAreaNames}" class="orderdetails2_text"><span >目的地：</span>
						${product.targetAreaNames}
					</li>
				</ul>
				<ul class="ydbz_dj specialPrice">
					<li>
						<span class="ydtips">单价</span>
                        <p>成人：${settlementAdultPrice}</p>
                        <p>儿童：${settlementcChildPrice}</p>
                        <p>特殊人群：${settlementSpecialPrice}</p>
                     </li>
                     <li>
						<span class="ydtips"> 出行人数</span>
                        <p>成人：<span> <c:if test="${not empty order.orderPersonNumAdult }">${order.orderPersonNumAdult }</c:if> 
                        									<c:if test="${empty order.orderPersonNumAdult }">0</c:if> </span> 人</p>
                        <p>儿童：<span> <c:if test="${not empty order.orderPersonNumChild }">${order.orderPersonNumChild }</c:if> 
                        									<c:if test="${empty order.orderPersonNumChild }">0</c:if></span> 人</p>
                        <p>特殊人群：<span><c:if test="${not empty order.orderPersonNumSpecial }">${order.orderPersonNumSpecial }</c:if> 
                        									<c:if test="${empty order.orderPersonNumSpecial }">0</c:if> </span> 人</p>
					</li>
				</ul>
			</div>
			<div class="orderdetails_tit">游客转团</div>
			<form id="">
				<table class="activitylist_bodyer_table">
					<thead>
						<tr>
							<th width="5%" class="table_borderLeftN">全选<input type="checkbox" name="allChk" /></th>
							<th width="7%">姓名</th>
							<th width="10%">签证状态</th>
							<th width="10%">实际约签时间</th>
							<th width="10%">应收金额</th>
							<th width="20%">转团后应收</th>
							<th width="10%">护照号</th>
							<th width="10%">护照有效期</th>
							<th width="18%">转团原因</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty travelList }">
							<c:forEach  items="${travelList }"  var="travel"  varStatus = "code">
								<tr>
									<td class="table_borderLeftN"><input type="checkbox"  name="${travel.id}"  class="funCheckBox"></td>
									<td>
										<input type="hidden" name="theTravelName${travel.id}"  value="${travel.name }"/>
										<input type="hidden" name="theTravelId${travel.id}"  value="${travel.id }"/>
										${travel.name }
									</td>
									<td>
										<c:if test="${travel.visa.visaStatus==0 }">未送签</c:if>
										<c:if test="${travel.visa.visaStatus==1 }">送签</c:if>			
										<c:if test="${travel.visa.visaStatus==2 }">约签</c:if>			
										<c:if test="${travel.visa.visaStatus==3 }">出签</c:if>			
										<c:if test="${travel.visa.visaStatus==4 }">申请撤签</c:if>			
										<c:if test="${travel.visa.visaStatus==5 }">撤签成功</c:if>			
										<c:if test="${travel.visa.visaStatus==6 }">撤签失败</c:if>			
										<c:if test="${travel.visa.visaStatus==7 }">拒签</c:if>	
										<c:if test="${empty travel.visa.visaStatus }">无签证</c:if>										 
									</td>
									<td>${travel.visa.contract }</td>
									<td>${travel.payPriceSerialNumInfo }</td>
									<td>
										<select name="subtractCurrency" class="selectrefund" style="width: 80px">
											<c:set value="${travel.subtractMoneySerialNum }" var="tempCurrencyId"></c:set>
											<c:forEach items="${fn:split(tempCurrencyId , ',')  }" var="item">
												<option value="${item }"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_name" value="${item }"/></option>
											</c:forEach>
										</select>
										<input type="text" name="subtractMoney" maxlength="9" onkeyup="refundInput2(this)" onafterpast="refundInput2(this)" value="0" style="width: 100px">
									</td>
									<td>${travel.passportCode }</td>
									<td> <fmt:formatDate value="${travel.passportValidity }"  pattern="yyyy-MM-dd"/></td>
									<td><input type="text" name="theRemark${travel.id }"   value=""/></td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</form>
			<div class="changeGroup">转入团号：<input type="text"  id="intoGroupId"/><input type="button" value="查找" onclick="changeGroups(this)" class="btn btn-primary ydbz_x">
				<div id="hiddenGroup"  style="display:none">
					<p class="ydbz_tit">转团信息</p>
					<form id="changeGroupForm">
					</form>
					<table class="ml25" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">转团人:</td>
								<td class="mod_details2_d2"  id="travelName" colspan="9"></td>
							</tr>
							<tr>
								<td class="mod_details2_d1"  noWrap="noWrap">团队类型:</td>
								<td class="mod_details2_d2" id="groupType" noWrap="noWrap"></td> 
								<td class="mod_details2_d1" noWrap="noWrap">团号:</td>
								<td class="mod_details2_d2" id="groupCode" noWrap="noWrap"></td>
								<td class="mod_details2_d1"  noWrap="noWrap">支付方式:</td>
								<td class="mod_details2_d2" noWrap="noWrap" >
									<select  id="payType"></select>
									<div id="remDay" style='display:none'></div>
								</td>
								<td class="mod_details2_d1" noWrap="noWrap"></td>
								<td class="mod_details2_d2"  noWrap="noWrap"></td>
								<td class="mod_details2_d1" noWrap="noWrap"></td>
								<td class="mod_details2_d2"  noWrap="noWrap"></td>
							</tr>
							<tr>
								<td class="mod_details2_d1" noWrap="noWrap">操作人:</td>
								<td class="mod_details2_d2"  id="createBy" noWrap="noWrap">${user.name }</td>
								<td class="mod_details2_d1" noWrap="noWrap">产品名称:</td>
								<td class="mod_details2_d2"  id="productName" noWrap="noWrap"></td>
								<td class="mod_details2_d1" noWrap="noWrap">余位:</td>
								<td class="mod_details2_d2" id="leaveSit"  colspan="5"  noWrap="noWrap"></td>
							</tr>
						</tbody>
					</table>    
				</div>   
			</div>
		</div>
		<div class="ydBtn ydBtn2">
			<a class="ydbz_s" onClick="window.close();">关闭</a><a class="ydbz_s" onclick="upForm();" id="formSubmit"  style="display:none">申请转团</a>
		</div>        
	</div>
</body>
</html>
