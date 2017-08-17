<!-- 
author:yunpeng.zhang
describe:单团类订单退团申请页，适用于单团订单，散拼订单，游学订单，大客户订单，自由行订单功能列表
createDate：2015年11月18日
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>申请退团</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.number.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript">

	var travelerList = eval('${travelerListJsonArray}');

$(function(){

	// 初始化游客数据
	initTravelerInfo();
	// 填充展示游客价格信息
	fillTravelerPrice();

    if("${param.saveinvoiceMSG}" =="1") {
		top.$.jBox.tip('操作已成功!','success');	
	}
    $(".qtip").tooltip({
        track: true
    });
    
    $(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    
	<%-- 前端js效果部分 --%>
			
    $("#contentTable").delegate("ul.caption > li","click",function() {
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
		
    $('.handle').hover(function() {
    	if(0 != $(this).find('a').length){
    		$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
    	}
	},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
	
	$("#allChk").click(function(){
		if($(this).attr('checked') == 'checked'){
			$("input[name='activityId']").attr('checked','checked');
		}else{
			$("input[name='activityId']:checked").removeAttr('checked');
		}
	});
  		
});

function checkall(obj){
  if($(obj).attr('checked'=='checked')){
  $("input[name='activityId']").attr('checked','checked');
  
  }else{
  $("input[name='activityId']:checked").removeAttr('checked');
  }
}

var submitTimes = 1;

/**
 * 新的退团
 */
function submitForm() {
	
	$("input[name='activityId']:checked").each(function(index, obj) {
		var afterExitGroupMoney = $(this).parent().parent().find("span[name=afterExitGroupMoney]").text();		
		if (afterExitGroupMoney == undefined || afterExitGroupMoney == null || afterExitGroupMoney == "") {
			jBox.tip("退团后应收金额不能为空", 'error');
			return;
		}
	});
	
	var str=[];//退团原因
	var travelerId=[];//游客id
	var travelerName=[];//游客姓名
	var afterExitGroupMoneyArr = [];  // 退团后应收
	var refundMoneyArr = [];  // 退款金额
	var flag=true;
 	$("input[name='activityId']:checked").each(function(){
   		var exitReason =$($(this).parent().parent()).find("input[name='exitReason']").val().replace(/\s+/g,"");
   		var travelerIds=$($(this).parent().parent()).find("input[name='travelerId']").val();
   		var travelerNames=$($(this).parent().parent()).find("input[name='travelerName']").val();
   		if(undefined==exitReason||""==exitReason){
   			exitReason=" ";
   		}
   		str.push(exitReason);
   		travelerId.push(travelerIds);
   		travelerName.push(travelerNames);
 	});
	// 依据选中游客的id，获取游客退团信息
	for (var i = 0; i < travelerId.length; i ++) {
		for (var j = 0; j < travelerInfoArray.length; j++) {
			if (travelerId[i].toString() == travelerInfoArray[j].id.toString()) {
				afterExitGroupMoneyArr.push(travelerId[i] + "#" + travelerInfoArray[j].afterPrice.nowCurrencyIds + "#" + travelerInfoArray[j].afterPrice.nowPrices);
				refundMoneyArr.push(travelerId[i] + "#" + travelerInfoArray[j].refundMoney.nowCurrencyIds + "#" + travelerInfoArray[j].refundMoney.nowPrices);
				break;
			}
		}
	}
	// 进行退团互斥验证
	if(str.length>0){
		if(submitTimes == 1) {
			submitTimes++;
			$.ajax({
				type: "POST",
				url: "${ctx}/singleOrder/exitGroup/checkSingleGroupExitGroup",
				data: {
					travelerIds : travelerId.join(','),
					productType : "${productType}",
					orderId : "${orderId}"
				},
				success: function(data){
					if(data.flag == false) {
						$.jBox.tip(data.msg, "error");
						return;
					}
					if(data.isMutexBoolean == false){
						//保存退团信息，发起审批流程
						startSingleGroupExitGroup(str, afterExitGroupMoneyArr, refundMoneyArr, travelerId, travelerName);
					}else{
						if(data.canCancel == false){
							$.jBox.tip(data.showMsg, "warning");
							submitTimes--;
						} else {
							top.$.jBox.confirm(data.showMsg,'系统提示',function(v){
								submitTimes--;
								if(v=='ok'){
									$.ajax({
										type: "POST",
										url: "${ctx}/singleOrder/exitGroup/cancelOtherReview",
										data: {
											ids : data.ids, 
											oldReviewIds : data.oldReviewIds
										},
										async : false,
										success: function(result){
											if(result.flag == 'success'){
												//保存退团信息，发起审批流程
												startSingleGroupExitGroup(str, afterExitGroupMoneyArr, refundMoneyArr, travelerId, travelerName);
											}else{
												$.jBox.tip(result.msg.description, "warning");
											}
										}
									});
								}
							},{buttonsFocus:1,persistent:true,showClose:false});
						}
					}
				}
			});
		} else {
			$.jBox.tip("请勿多次提交！如果正常提交无法进行，请刷新页面重试。", "warn");
			return;
		}
	} else {
		top.$.jBox.tip('请选择需退团的游客！', 'error', { focusId: 'name' });
	}

}

/**
 * 退团申请
 * @param str
 * @param afterExitGroupMoneyArr
 * @param travelerId
 * @param travelerName
 */
function startSingleGroupExitGroup(str, afterExitGroupMoneyArr, refundMoneyArr, travelerId, travelerName) {
	$.ajax({
		type: "POST",
		url: "${ctx}/singleOrder/exitGroup/startSingleGroupExitGroup",
		data: {
			exitReasons : str.join(','),
			afterExitGroupMoneys : afterExitGroupMoneyArr.join('##'),
			refundMoneys : refundMoneyArr.join('##'),
			travelerIds : travelerId.join(','),
			travelerNames : travelerName.join(','),
			productType : "${productType}",
			orderId : "${orderId}"
		},
		success: function(result){
			if(result.msg == "success"){
				$.jBox.tip("退团申请成功！");
				window.location.href ="${ctx}/singleOrder/exitGroup/newExitGroupList?orderId=${orderId}&productType=${productType}";
			}else {
				$.jBox.tip(result.reply);
				window.location.href ="${ctx}/singleOrder/exitGroup/newExitGroupDetails?id=${orderId}&productType=${productType}";
			}

		}
	});
}

/**
 * 填写退款金额，计算退团后应收
 */
function calAfterExitGroupMoney(obj) {
	// 对于负数开头数字的输入不置为空串 forbug
	if(isNaN(obj.value) && "-" != obj.value){
		var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		var txt = ms.split(".");
		obj.value  = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	// 退款
	var oneRefundMoney = new Object();
	var refundMoneyCurrencyId = $(obj).prev().val();
	var refundMoenyPrice = $(obj).val();
	oneRefundMoney.price = refundMoenyPrice;
	var travelerId = $(obj).closest("tr").find("input[name=travelerId]").val();
	var travelerIndex = 0;
	for ( var i = 0; i < travelerInfoArray.length; i++) {
		if (travelerInfoArray[i].id.toString() == travelerId.toString()) {
			travelerIndex = i;
			var travelerPriceArray = travelerInfoArray[i].payPrice.priceArray;
			for ( var j = 0; j < travelerPriceArray.length; j++) {
				if (travelerPriceArray[j].currency.id.toString() == refundMoneyCurrencyId.toString()) {
					oneRefundMoney.currency = travelerPriceArray[j].currency;
					// 退团前应收金额
					var prePrice = Number(travelerPriceArray[j].price);
					var calAfterPrice = Number(travelerPriceArray[j].price) - Number(refundMoenyPrice);  // 相减
					if(prePrice < 0){  // forbug针对费用为负数的情况
						// 不用校验？
						/* if(calAfterPrice > 0) {
							$.jBox.tip("您输入的退款金额大于应收金额，请重新输入！");
							$(obj).val("0");
							calAfterExitGroupMoney(obj)
							return;
						} */
					}else{
						// 校验，给出提示
						if(calAfterPrice < 0) {
							$.jBox.tip("您输入的退款金额大于应收金额，请重新输入！");
							$(obj).val("0");
							calAfterExitGroupMoney(obj)
							return;
						}
					}
					calAfterPrice = calAfterPrice.toFixed(2);
					travelerInfoArray[i].afterPrice.priceArray[j].price = calAfterPrice;  // 调整游客退后应收
					travelerInfoArray[i].refundMoney.priceArray[j] = oneRefundMoney;  // 调整游客退款
					break;
				}
			}
			break;
		}
	}
	// 组织退后
	var afterMoneyStr = "";
	var afterPrices = "";
	for ( var k = 0; k < travelerInfoArray[travelerIndex].afterPrice.priceArray.length; k++) {
		var tempPrice = travelerInfoArray[travelerIndex].afterPrice.priceArray[k];
		if (k != 0) {
			afterMoneyStr += "+";
			afterPrices += ",";
		}
		afterMoneyStr += tempPrice.currency.mark + $(document).commafy(tempPrice.price);
		afterPrices += tempPrice.price;
	}
	$(obj).parent().parent().siblings("td").find("span[name=afterExitGroupMoney]").text(afterMoneyStr)
	travelerInfoArray[travelerIndex].afterPrice.nowPrices = afterPrices;
	// 组织退款
	var refundPrices = "";
	for ( var k = 0; k < travelerInfoArray[travelerIndex].refundMoney.priceArray.length; k++) {
		var tempPrice = travelerInfoArray[travelerIndex].refundMoney.priceArray[k];
		if (k != 0) {
			refundPrices += ",";
		}
		refundPrices += tempPrice.price;
	}
	travelerInfoArray[travelerIndex].refundMoney.nowPrices = refundPrices;
}

/**
 * 把游客结算价金额按照选中的币种对应的汇率进行换算
 */
function getMaxPriceUseSelectedCurrency(travelerId, selectedCurrencyId){
	$.ajax({
		type: "POST",
		url: "${ctx}/singleOrder/exitGroup/getMaxPriceUseSelectedCurrency",
		data: {
			travelerId : travelerId,
			selectedCurrencyId : selectedCurrencyId
		},
		success: function(result){
			if(result.msg == "success"){
				return result.data;
			}else {
				$.jBox.tip("计算失败！");
				return null;
			}
		}
	});
}

var travelerInfoArray = new Array();
/**
 * 初始化游客信息
 */
function initTravelerInfo () {
	for (var i=0; i < travelerList.length; i++) {
		var travelerInfo = new Object();
		travelerInfo.id = travelerList[i].id;  // id
		travelerInfo.name = travelerList[i].name;  // name
		travelerInfo.payPrice = getPayPrice(travelerList[i], "pay");  // 结算价
		travelerInfo.afterPrice = getPayPrice(travelerList[i], "after");  // 退团后
		travelerInfo.refundMoney = getPayPrice(travelerList[i], "refund");  // 退款
		travelerInfo.reason = null;
		travelerInfo.asPayCurrency = travelerList[i].asPayCurrency;
		travelerInfoArray.push(travelerInfo);
	}
}

/**
 * 组织游客金额 type: 求不同价格类型
 */
function getPayPrice (traveler, type) {
	var travelerPayPrice = new Object();
	var priceArr = new Array();
	var payPriceArr = traveler.payPrices.toString().split(",");
	var currencyIdArr = traveler.currencyIds.toString().split(",");
	var currencyNameArr = traveler.currencyName.toString().split(",");
	var currencyMarkArr = traveler.currencyMark.toString().split(",");

	var showStrName = "";
	var showStrMark = "";
	if (currencyIdArr.length == currencyNameArr.length &&
		currencyNameArr.length == currencyMarkArr.length &&
		currencyMarkArr.length == payPriceArr.length) {
		for ( var j = 0; j < currencyIdArr.length; j++) {
			var payPrice = new Object();
			var currency = new Object();
			currency.id = Number(currencyIdArr[j]);
			currency.name = currencyNameArr[j];
			currency.mark = currencyMarkArr[j];
			if(type == "refund") {
				payPrice.price = 0;
			} else {
				payPrice.price = payPriceArr[j];
			}
			payPrice.currency = currency;
			priceArr.push(payPrice);
			if (j != 0) {
				showStrName += "+";
				showStrMark += "+";
			}
			showStrName += currencyNameArr[j] + $(document).commafy(payPriceArr[j]);
			showStrMark += currencyMarkArr[j] + $(document).commafy(payPriceArr[j]);
		}
	}
	travelerPayPrice.priceArray = priceArr;
	travelerPayPrice.showStrName = showStrName;
	travelerPayPrice.showStrMark = showStrMark;
	if(type == "refund") {	
		travelerPayPrice.nowCurrencyIds = traveler.currencyIds;
		var refundPriceStr = "";
		for ( var index in priceArr) {
			if(index > 0) {
				refundPriceStr += ",";
			}
			refundPriceStr += priceArr[index].price;
		}
		travelerPayPrice.nowPrices = refundPriceStr;
	} else {	
		travelerPayPrice.nowCurrencyIds = traveler.currencyIds;
		travelerPayPrice.nowPrices = traveler.payPrices;
	}
	return travelerPayPrice;
}

/**
 * 初始填充游客价格信息
 */
function fillTravelerPrice () {
	$("input[name=travelerId]").each(function(index, element){
		for (var i = 0; i < travelerInfoArray.length; i++) {
			if ($(element).val() == travelerInfoArray[i].id.toString()) {
				$(element).closest("tr").find("span[name=payPrice]").text(travelerInfoArray[i].payPrice.showStrMark);
				$(element).closest("tr").find("span[name=afterExitGroupMoney]").text(travelerInfoArray[i].afterPrice.showStrMark);
				continue;
			}
		}
	});
}

</script>

<style type="text/css">
a {
	display: inline-block;
}

* {
	margin: 0px;
	padding: 0px;
}

body {
	background: #fff;
	margin: 0px auto;
}

.pop_gj {
	padding: 10px 24px;
	margin: 0px;
	border-bottom: #b3b3b3 1px dashed;
	overflow: hidden;
}

.pop_gj dt {
	float: left;
	width: 100%;
}

.pop_gj dt span {
	float: left;
	width: 80px;
	text-align: right;
	color: #333;
	font-size: 12px;
	overflow: hidden;
	height: 25px;
	line-height: 180%;
}

.pop_gj dt p {
	float: left;
	width: 300px;
	color: #000;
	font-size: 12px;
	line-height: 180%;
}

.pop_xg {
	padding: 10px 4px;
	margin: 0px;
	overflow: hidden;
}

.pop_xg dt {
	float: left;
	width: 100%;
	margin-top: 10px;
	height: 30px;
}

.pop_xg dt span {
	float: left;
	width: 100px;
	text-align: right;
	color: #333;
	font-size: 12px;
	overflow: hidden;
	height: 30px;
	line-height: 30px;
}

.pop_xg dt p {
	float: left;
	width: 110px;
	color: #333;
	font-size: 12px;
	height: 30px;
	line-height: 30px;
	overflow: hidden;
	position: relative;
}

.pop_xg dt p font {
	color: #e60012;
	font-size: 12px;
}

.pop_xg dt p input {
	width: 60px;
	height: 28px;
	line-height: 28px;
	padding: 0px 5px 0px 18px;
	color: #403738;
	font-size: 12px;
	position: relative;
	z-index: 3;
}

.pop_xg dt p i {
	position: absolute;
	height: 28px;
	top: 2px;
	width: 10px;
	text-align: center;
	left: 5px;
	z-index: 5;
	font-style: normal;
	line-height: 28px;
}

.release_next_add button {
	cursor: pointer;
	border-radius: 4px;
}

label {
	cursor: inherit;
}

.activitylist_bodyer_table tr td {
	text-align: center;
}
</style>

</head>

<body>
	<div id="sea">

		<!-- 顶部参数 -->
		<div class="activitylist_bodyer_right_team_co_bgcolor"
			style="float: left; width: 100%">

			<!--右侧内容部分开始-->
			<div class="ydbzbox fs">
				<div class="orderdetails">

					<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
					<div class="ydbz_tit">申请退团</div>
					<form id="submitForm" method="post">
						<table id="contentTable" class="table activitylist_bodyer_table">
							<thead>
								<tr>
									<th class="table_borderLeftN" width="5%">全选<input
										name="allChk" id="allChk" type="checkbox"></th>
									<th width="10%">游客</th>
									<th width="15%">下单时间</th>
									<th width="15%">应收金额</th>
									<th width="15%">退团后应收</th>
									<th width="15%">退款金额</th>
									<th width="15%">退团原因</th>
								</tr>
							</thead>

							<tbody>
								<c:forEach items="${travelerList}" var="tra" varStatus="s">

									<tr>
										<td class="table_borderLeftN"><input name="activityId" type="checkbox"></td>
										<td><c:if test="${tra.delFlag == 4 }">
												<div class="ycq yj" style="margin-top: 1px;">转团审批中</div>
											</c:if>${tra.name}</td>
										<td><fmt:formatDate value="${tra.orderTime}"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td class="tc"><span class="tdgreen" name="payPrice"></span></td>
										<td class="tc"><span class="tdred" name="afterExitGroupMoney"></span></td>
										<td>
											<c:set value="${tra.currencyIds }" var="tempCurrencyId"></c:set>
											<c:forEach items="${fn:split(tempCurrencyId , ',')  }" var="item">
												<div style="margin-bottom:2px;margin-top:2px;">
													<label style="width:30%;"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_name" value="${item }" /></label>
													<input type="hidden" name="refundCurrencyId" value="${item }">
													<input style="width:40%" name="refundMoney" type="text"  onkeyup="calAfterExitGroupMoney(this);" onafterpase="calAfterExitGroupMoney(this);" maxlength="12" >
												</div>
											</c:forEach>
										</td>
										<td>
											<input type="text" name="exitReason" maxlength="100" />
											<input name="travelerId" value="${tra.id}" type="hidden">
											<input name="travelerName" value="${tra.name}" type="hidden">
										</td>
									</tr>
								</c:forEach>
							</tbody>

						</table>
					</form>
				</div>
				<div class="ydBtn ydBtn2">
					<a class="ydbz_s gray"
						href="javascript:window.opener=null;window.close();">关闭</a><a
						class="ydbz_s" href="javascript:submitForm()">提交</a>
				</div>
			</div>

		</div>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>
