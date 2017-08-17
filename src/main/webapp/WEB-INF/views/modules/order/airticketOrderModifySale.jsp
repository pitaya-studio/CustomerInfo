<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta>
<title>订单-销售机票订单修改</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<!-- 静态资源 -->
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/airticket/manageorder.js" type="text/javascript"></script>
<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent;
    border:0px;
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}
</style>
<script type="text/javascript">
	
	//页面加载完执行的js
	$(function(){
	
		//各块信息展开与收起
		$(".ydClose").click(function(){
			var obj_this = $(this);
			if(obj_this.attr("class").match("ydExpand")) {
				obj_this.removeClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
			} else {
				obj_this.addClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
			}
		});
		
		ydbz2interradio();//联运按钮
		
	});
	
	//单个保存游客	 新加flag标志 1标示提交数据 0标示不提交
	function saveTravel(element, nFlag){
		
		//获取游客信息
		var _this = $(element);
		var travelForm = _this.closest(".travelerTable");
		var orderId = $("#orderId").val();
		var airticketId = $("#airticketId").val();
		var travelId = travelForm.find(":input[name='travelId']").val();
		var travelName = travelForm.find(":input[name='travelerName']").val();
		var travelNamePinyin = travelForm.find(":input[name='travelerPinyin']").val();
		var travelerSex = travelForm.find(":input[name='travelerSex']").val();
		var nationality = travelForm.find(":input[name='nationality']").val();
		var birthDay = travelForm.find(":input[name='birthDay']").val();
		var telephone = travelForm.find(":input[name='telephone']").val();
		var passportCode = travelForm.find(":input[name='passportCode']").val();
		var passportValidity = travelForm.find(":input[name='passportValidity']").val();
		var idCard = travelForm.find(":input[name='idCard']").val();
		var remarks = travelForm.find(":input[name='remarks']").val();
		var personType = travelForm.find(":input[name='personTypeinner']:checked").val();
		var passportType = travelForm.find(":input[name='passportType']").val();
		var intermodalType = travelForm.find(":input[name='ydbz2intermodalType']:checked").val();
		var currencyId = $("#currencyId").val();
		var payPrice = travelForm.find(":input[name='payPrice']").val();//游客结算价
		var intermodalId = null;
		
		//需要联运
		if(intermodalType == 1){
			intermodalId = travelForm.find(":input[name='intermodal'] :selected").attr("id");
		}
		
		var obj = element;
		
		if($(obj).text()=="保存"){
			//游客姓名
			$(obj).parent().parent().find("input[name='travelerName']").each(function(index, obj) {
				if($(this).val() == "") {
					var name = $(this).parent().children("label").text();
					top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
	                $(this).focus();
	                throw "error！"; 
				}
			});
			if(birthDay != null && birthDay != ""){
				var curDate = new Date();
				if (new Date(birthDay) > curDate) {
					$.jBox.tip('出生日期不能大于当前时间', 'error', { focusId: 'birthDay' });
					return false;
				}
			}
			//护照号
			$(obj).parent().parent().find("input[name='passportCode']").each(function(index, obj) {
				if($(this).val() == "") {
					var name = $(this).parent().children("label").text();
					top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
	                $(this).focus();
	                throw "error！"; 
				}
			});
			//护照有效期
			$(obj).parent().parent().find("input[name='passportValidity']").each(function(index, obj) {
				if($(this).val() == "") {
					var name = $(this).parent().children("label").text();
					top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
					$(this).focus();
					throw "error！"; 
				}
			});
//			//结算数据
//			var prices = $("#traveler input[name='payPrice']");
//			var totalPrice = 0;
//			$.each(prices, function(key, value) {
//			var tempnum = $(value).val();
//			if (tempnum != "") {
//				totalPrice += Number(tempnum);
//			}
//			});
//			if (totalPrice > topPrice) {
//				top.$.jBox.tip('成人/儿童/特殊人群与初始值不匹配请修改','warning');
//				throw "error！"; 
//			}
			//如果游客护照有效期不足半年，则提醒
//			$(obj).parent().parent().find("input[name='validityDate']").each(function(index, obj) {
//				var nowDate = new Date();
//				var validityDate = new Date($(this).val());
//				var diff = validityDate.getTime() - nowDate.getTime();
//				if(diff/(24*60*60*1000) < 183) {
//					var name = $(obj).parents("div.tourist-left").children().children().children("input[name=travelerName]").val();
//					alert('游客 ' + name + ' 护照有效期不足半年，请注意');
//				}
//			});
			
			
			
			//选择联运时身份证号码必填
			$(obj).parent().parent().find("input[name='idCard']").each(function(index, obj) {
				var ly=$(this).closest("form").find("input[name='ydbz2intermodalType']:checked").val();//联运
				if(ly=="1"&&$(this).val() == "") {
					var name = $(this).parent().children("label").text();
					top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
	                $(this).focus();
	                throw "error！"; 
				}
			});
			var adultNum = $("#orderPersonNumAdult").val();
			var childNum = $("#orderPersonNumChild").val();
			var specialNum = $("#orderPersonNumSpecial").val();
			if (countAdult() > adultNum) {
				top.$.jBox.tip('成人人数与初始值不匹配请修改', 'error');
				throw "error！";
			}
			if (countChild() > childNum) {
				top.$.jBox.tip('儿童人数与初始值不匹配请修改', 'error');
				throw "error！";
			}
			if (countSpecial() > specialNum) {
				top.$.jBox.tip('特殊人数与初始值不匹配请修改', 'error');
				throw "error！";
			}
//			if(!flag) {
//				return false;
//			} 
			
			//保存数据
			//saveTravelerInfo(obj);
		}
		
		var data = data || {};
		data['orderId']=orderId;
		data['airticketId']=airticketId;
		data['travelId']=travelId;
		data['travelName']=travelName;
		data['travelNamePinyin']=travelNamePinyin;
		data['travelerSex']=travelerSex;
		data['nationality']=nationality;
		data['birthDay']=birthDay;
		data['telephone']=telephone;
		data['passportCode']=passportCode;
		data['passportValidity']=passportValidity;
		data['idCard']=idCard;
		data['remarks']=remarks;
		data['personType']=personType;
		data['passportType']=passportType;
		data['intermodalType'] = intermodalType;
		data['intermodalId']= intermodalId;
		data['currencyId'] = currencyId;
		data['payPrice'] = payPrice;
		if(nFlag == 0){
			if(_this.text()=="保存"){
			    _this.text("修改");
				_this.parent().prev().hide();
				_this.parent().parent().find('.tourist-t-off').css("display","inline");
				_this.parent().parent().find('.tourist-t-off em').html(travelName);
				_this.parent().parent().find('.tourist-t-off').find(".ydFont2").text(payPrice);
				_this.parent().parent().find('.tourist-t-on').hide();
				_this.parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd')
			}else{
				_this.text("保存");
				_this.parent().prev().show();
				_this.parent().parent().find('.tourist-t-off').css("display","none");
				_this.parent().parent().find('.tourist-t-on').show();
				_this.parent().parent().find('.tourist-t-on').find("input[name='personTypeinner']").attr("disabled","disabled");//游客中的人员类型不允许修改
				_this.parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd')
			}
			return;
		}
		//ajax请求
		$.ajax({
			type:"POST",
			url:"${ctx}/order/manage/airticketOrderTravel",
			data:data,
			success:function(data){
				if(data.result == 'success' && data.msg != ""){
					travelForm.find(":input[name='travelId']").val(data.msg);
				}
				var input=_this.parent().parent().find("input");
				var textarea=_this.parent().parent().find("textarea");
				var selects=_this.parent().parent().find("select");
				if($(input).prop("disabled")){
						    $(input).removeAttr("disabled","disabled");
						}else{
							$(input).attr("disabled","disabled");
						}
				if($(textarea).prop("disabled")){
						    $(textarea).removeAttr("disabled","disabled");
						}else{
							$(textarea).attr("disabled","disabled");
						}
				if($(selects).prop("disabled")){
						    $(selects).removeAttr("disabled","disabled");
						}else{
							$(selects).attr("disabled","disabled");
						}
// 				if(_this.text()=="保存"){
// 					    _this.text("修改");
// 						_this.parent().prev().hide();
// 						_this.parent().parent().find('.tourist-t-off').css("display","inline");
// 						_this.parent().parent().find('.tourist-t-off em').html(travelName);
// 						_this.parent().parent().find('.tourist-t-off').find(".ydFont2").text(payPrice);
// 						_this.parent().parent().find('.tourist-t-on').hide();
// 						_this.parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd')
// 					}else{
// 						_this.text("保存");
// 						_this.parent().prev().show();
// 						_this.parent().parent().find('.tourist-t-off').css("display","none");
// 						_this.parent().parent().find('.tourist-t-on').show();
// 						_this.parent().parent().find('.tourist-t-on').find("input[name='personTypeinner']").attr("disabled","disabled");//游客中的人员类型不允许修改
// 						_this.parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd')
// 					}
			}
		});
	}
	
	//选择不同人群，改变结算价
	function changePayprice(element){
		var _this = $(element);
		var travelForm = _this.closest(".travelerTable");
		var personType = _this.val();
		var aPrice = $("#aPrice").val();//成人价
		var cPrice = $("#cPrice").val();//儿童jia
		var sPrice = $("#sPrice").val();
		var jsarray=new Array();
				var isly=_this.closest("form").find("input[name='ydbz2intermodalType']:checked").val();//是否联运
				if("1"==isly){
					var thisSelect=_this.closest("form").find("select[name='intermodal'] option:selected")
					var currencyValue=Number(thisSelect.val());
					var currencyName=thisSelect.attr("currenyname");
					var currenymark=thisSelect.attr("currenymark");
					if(personType == 1){
						var curMark = /^\D+(?=\d)/.exec(aPrice)[0];//币种
						var curValue= rmoney(aPrice.split(curMark)[1]);//钱数信息
					}else if(personType == 2){
						var curMark = /^\D+(?=\d)/.exec(cPrice)[0];
						var curValue= rmoney(cPrice.split(curMark)[1]);
					}else if(personType == 3){
						var curMark = /^\D+(?=\d)/.exec(sPrice)[0];
						var curValue=rmoney(sPrice.split(curMark)[1]);
					}
					var thisVlaue=0;
					if(curMark==currenymark){
						thisVlaue=currenymark+milliFormat(currencyValue+=curValue,'1');
					}else{
						if(currencyValue==0){
						thisVlaue=curMark+milliFormat(curValue,'1');
						}else{
						thisVlaue=curMark+milliFormat(curValue,'1')+"+"+currenymark+milliFormat(currencyValue,'1');
						}
						
					}
					travelForm.find(":input[name='payPrice']").val(thisVlaue)
					travelForm.find(":input[name='payPrice']").prev().html(thisVlaue);
				}else{
					if(personType == 1){
						travelForm.find(":input[name='payPrice']").val(aPrice)
						travelForm.find(":input[name='payPrice']").prev().html(aPrice);
					}else if(personType == 2){
						travelForm.find(":input[name='payPrice']").val(cPrice)
						travelForm.find(":input[name='payPrice']").prev().html(cPrice);
					}else if(personType == 3){
						travelForm.find(":input[name='payPrice']").val(sPrice)
						travelForm.find(":input[name='payPrice']").prev().html(sPrice);
					}
				}
		
	}
	
	
	//保存航班备注和预订人信息
	function saveRemarkAndAgentInfo(){
		$("#traveler form").each(function(index, element) {
			var _this=$(element).find(".rightBtn a");
			saveTravel(_this, 1);
		})
		var orderId = $("#orderId").val();
		var airticketId = $("#airticketId").val();
		var flightRemark = $(":input[name='remark']").val();//航班备注
		var agentId = $(":input[name='agent']").val();
		var agentName = $(":input[name='agentName']").val();
		var agentContact = $(":input[name='agentContact']").val();
		var agentTel = $(":input[name='agentTel']").val();
		var agentAddress = $(":input[name='agentAddress']").val();
		var agentFax = $(":input[name='agentFax']").val();
		var agentQQ = $(":input[name='agentQQ']").val();
		var agentEmail = $(":input[name='agentEmail']").val();
		var agentRemarks = $(":input[name='agentRemarks']").val();
		var orderAllP = $(":input[name='orderAllP']").val();
		
		var data = {};
		data['orderId']=orderId;
		data['airticketId']=airticketId;
		data['flightRemark']=flightRemark;
		data['agentId']=agentId;
		data['agentName']=agentName;
		data['agentContact']=agentContact;
		data['agentTel'] = agentTel;
		data['agentAddress']=agentAddress;
		data['agentFax']=agentFax;
		data['agentQQ']=agentQQ;
		data['agentEmail']=agentEmail;
		data['agentRemarks']=agentRemarks;
		data['orderAllP']=orderAllP;
		
		$.ajax({
			type:"POST",
			url:"${ctx}/order/manage/airticketOrderAgent",
			data:data,
			success:function(data){
				if(data.result == "success"){
					top.$.jBox.tip("保存成功。。。");
					window.location.href="${ctx}/airticketOrderList/manage/airticketOrderList/1";
				}
			}
		});
	}
	
	//改变预订人信息
	function changeAgent(element){
		var _this = $(element);
		var agentForm = _this.parent().parent();
		var agentId = _this.val();
		$.ajax({
			type:"POST",
			url:"${ctx}/order/manage/airticketOrderChangeAgent",
			data:{agentId:agentId},
			success:function(data){
				
				agentForm.find(":input[name='agentContact']").val(data.agentContact);
				agentForm.find(":input[name='agentTel']").val(data.agentTel);
				agentForm.find(":input[name='agentAddress']").val(data.agentAddress);
				agentForm.find(":input[name='agentFax']").val(data.agentFax);
				agentForm.find(":input[name='agentQQ']").val(data.agentQQ);
				agentForm.find(":input[name='agentEmail']").val(data.agentEmail);
				agentForm.find(":input[name='agentRemarks']").val(data.agentRemarks);
			}
		});
	}
	
</script>
</head>

<body>
	<!-- tab -->
	<page:applyDecorator name="airticket_order_modify">
	</page:applyDecorator>
	
	<!--右侧内容部分开始-->
	<div class="ydbzbox fs">
		<div class="tr">
		</div>
		<div class="orderdetails">
			<div class="orderdetails_tit">
				<span>1</span>订单信息
				<input id="orderId" type="hidden" value="${orderDetailInfoMap.orderId }">
				<input id="airticketId" type="hidden" value="${orderDetailInfoMap.airticketId }">
			</div>
			<div class="orderdetails1">
				<table border="0" width="90%" style="margin-left:0;">
					<tbody>
						<tr>
							<td class="mod_details2_d1">下单人：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.userName }</td>
							<td class="mod_details2_d1">销售：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.salerName }</td>
							<td class="mod_details2_d1">下单时间：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${orderDetailInfoMap.orderCreateDate }" pattern="yyyy-MM-dd HH:mm"/></td>
							<td class="mod_details2_d1">操作人：</td>
							<td class="mod_details2_d2">${fns:getUserNameById(orderDetailInfoMap.createBy)}</td>

						</tr>
						<tr>
							<td class="mod_details2_d1">订单编号：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.orderNo }</td>
							<td class="mod_details2_d1">订单团号：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.orderGroupCode }</td>
							<td class="mod_details2_d1">团队类型：</td>
							<td class="mod_details2_d2">
								<c:choose>
									<c:when test="${orderDetailInfoMap.type == 1 }">单办</c:when>
									<c:when test="${orderDetailInfoMap.type == 2 }">参团</c:when>
								</c:choose>
							</td>
						</tr>
						
						<tr>
							<td class="mod_details2_d1">参团订单编号：</td>
							<td class="mod_details2_d2">${porder.orderNum }</td>
							<td class="mod_details2_d1">参团订单团号：</td>
							<td class="mod_details2_d2">${groupNum }</td>
							
						</tr>
						<c:if test="${orderDetailInfoMap.type == 2 }">
							<tr>
								<td class="mod_details2_d1">参团订单编号：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.mainOrderId }</td>
								<td class="mod_details2_d1">参团团号：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.activityGroupCode }</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
			
			
			
			<div class="mod_information_dzhan" style="overflow:hidden;">
					<div class="mod_information_dzhan_d mod_details2_d">
							<c:choose>
								<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) == 2 }">
									<!--往返-->
									<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（往返）</span>
									<div class="mod_information_d7"></div>
									
									<table width="90%" border="0">
										<tbody>
											<tr>
												<td class="mod_details2_d1">出发城市：</td>
												<td class="mod_details2_d2">
												${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
												</td>
												<td class="mod_details2_d1">到达城市：</td>
												<td class="mod_details2_d2">
												<c:forEach items="${arrivedareas}" var="arrivedareas">
													<c:if test="${arrivedareas.id == orderDetailInfoMap.arrivedCity}">
														${arrivedareas.name}
													</c:if>
												</c:forEach>
												</td>
												<td class="mod_details2_d1">预收人数：</td>
												<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
											 </tr>
										 </tbody>
									 </table>
									 
									<c:forEach items="${orderDetailInfoMap.flightInfoList}" var="flightInfo">
										<div class="title_samil">
											<c:choose>
												<c:when test="${flightInfo.orderNumber==1 }">去程：</c:when>
												<c:when test="${flightInfo.orderNumber==2 }">回程：</c:when>
											</c:choose>
										</div>
										
										<table width="90%" border="0">
										<tbody><tr>
											<td class="mod_details2_d1">出发机场：</td>
											<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
											<td class="mod_details2_d1">到达机场：</td>
											<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
											
										 </tr>
										 <tr>
											<td class="mod_details2_d1">出发时刻：</td>
											<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
											<td class="mod_details2_d1">到达时刻：</td>
											<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										 </tr>
										 <tr>
											<td class="mod_details2_d1">航空公司：</td>
											<td class="mod_details2_d2">${fns:getDictDescription(flightInfo.airlines,"traffic_name" , "")}</td>
											<td class="mod_details2_d1">舱位等级：</td>
											<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
											<td class="mod_details2_d1">舱位：</td>
											<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
										 </tr>
										</tbody></table>
										
									</c:forEach> 
								</c:when>
								<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) == 1 }">
									<!--单程-->
									<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（单程）</span>
									<div class="mod_information_d7"></div>
									
									<table width="90%" border="0">
										<tbody>
											<tr>
												<td class="mod_details2_d1">出发城市：</td>
												<td class="mod_details2_d2">
												${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
												</td>
												<td class="mod_details2_d1">到达城市：</td>
												<td class="mod_details2_d2">
												<c:forEach items="${arrivedareas}" var="arrivedareas">
													<c:if test="${arrivedareas.id == orderDetailInfoMap.arrivedCity}">
														${arrivedareas.name}
													</c:if>
												</c:forEach>
												</td>
												<td class="mod_details2_d1">预收人数：</td>
												<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
											 </tr>
										 </tbody>
									 </table>
									 
									 <c:forEach items="${orderDetailInfoMap.flightInfoList}" var="flightInfo">
										<table width="90%" border="0">
										<tbody><tr>
											<td class="mod_details2_d1">出发机场：</td>
											<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
											<td class="mod_details2_d1">到达机场：</td>
											<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
											
										 </tr>
										 <tr>
											<td class="mod_details2_d1">出发时刻：</td>
											<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
											<td class="mod_details2_d1">到达时刻：</td>
											<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										 </tr>
										 <tr>
											<td class="mod_details2_d1">航空公司：</td>
											<td class="mod_details2_d2">${fns:getDictDescription(flightInfo.airlines,"traffic_name" , "")}</td>
											<td class="mod_details2_d1">舱位等级：</td>
											<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
											<td class="mod_details2_d1">舱位：</td>
											<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
										 </tr>
										</tbody></table>
									</c:forEach>
								</c:when>
								<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) >= 3 }">
									<!--多段-->
									<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（多段）</span>
									<div class="mod_information_d7"></div>
									
									<table width="90%" border="0">
										<tbody>
											<tr>
												<td class="mod_details2_d1">出发城市：</td>
												<td class="mod_details2_d2">
												${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
												</td>
												<td class="mod_details2_d1">到达城市：</td>
												<td class="mod_details2_d2">
												<c:forEach items="${arrivedareas}" var="arrivedareas">
													<c:if test="${arrivedareas.id == orderDetailInfoMap.arrivedCity}">
														${arrivedareas.name}
													</c:if>
												</c:forEach>
												</td>
												<td class="mod_details2_d1">预收人数：</td>
												<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
											 </tr>
										 </tbody>
									 </table>
									 <c:forEach items="${orderDetailInfoMap.flightInfoList }" var="flightInfo">
										 <div class="title_samil">第${flightInfo.orderNumber }段：
										 	<c:choose>
										 		<c:when test="${flightInfo.ticketAreaType == 1 }">内陆</c:when>
										 		<c:when test="${flightInfo.ticketAreaType == 2 }">国际</c:when>
										 		<c:when test="${flightInfo.ticketAreaType == 3 }">内陆+国际</c:when>
										 	</c:choose>
										 </div>
										 <table width="90%" border="0">
										<tbody><tr>
											<td class="mod_details2_d1">出发机场：</td>
											<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
											<td class="mod_details2_d1">到达机场：</td>
											<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
											
										 </tr>
										 <tr>
											<td class="mod_details2_d1">出发时刻：</td>
											<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
											<td class="mod_details2_d1">到达时刻：</td>
											<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										 </tr>
										 <tr>
											<td class="mod_details2_d1">航空公司：</td>
											<td class="mod_details2_d2">${fns:getDictDescription(flightInfo.airlines,"traffic_name" , "")}</td>
											<td class="mod_details2_d1">舱位等级：</td>
											<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
											<td class="mod_details2_d1">舱位：</td>
											<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
										 </tr>
										</tbody></table>
									</c:forEach>
								</c:when>
							</c:choose>
						<div class="mod_information_d7"></div>
						
						<ul class="ydbz_dj specialPrice">
							<li style="display: none;">
                   			<input type="text" class="required" value="${orderDetailInfoMap.personNum}" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" id="orderPersonelNum">
                        </li>
							<li>
								<span class="ydtips">单价</span>
								<input id="currencyId" type="hidden" value="${orderDetailInfoMap.currencyId }">
								<input id="aPrice" type="hidden" value="${orderDetailInfoMap.adultTaxPrice }">
								<input id="cPrice" type="hidden" value="${orderDetailInfoMap.childTaxPrice }">
								<input id="sPrice" type="hidden" value="${orderDetailInfoMap.specialTaxPrice }">
								<p>成人：<font color="#FF0000">${orderDetailInfoMap.adultPrice }</font></p>
								<p>儿童：<font color="#FF0000">${orderDetailInfoMap.childPrice }</font></p>
								<p>特殊人群：<font color="#FF0000">${orderDetailInfoMap.specialPrice }</font></p>
							</li>
							 <li><span class="ydtips"> 出行人数</span>
								<input id="orderPersonNumAdult" type="hidden" value="${orderDetailInfoMap.adultNum }">
								<input id="orderPersonNumChild" type="hidden" value="${orderDetailInfoMap.childNum }">
								<input id="orderPersonNumSpecial" type="hidden" value="${orderDetailInfoMap.specialNum }">
								<p>成人：<span >${orderDetailInfoMap.adultNum }</span> 人</p>
								<p>儿童：<span>${orderDetailInfoMap.childNum }</span> 人</p>
								<p>特殊人群：<span>${orderDetailInfoMap.specialNum }</span> 人</p>
							 </li>
							 <li class="ydbz_single">
							 <span class="">税费：</span>${orderDetailInfoMap.taxamt }/人
							 </li>
						</ul>
					</div>
				</div>
			
			<div class="orderdetails_tit">
				<span>2</span>机票
			</div>
			
			<div flag="messageDiv" class="ydbz2_lxr">
				<form class="contactTable">
					<p>
						<label>航班备注：</label>
						<span>${orderDetailInfoMap.remark }</span>
					</p>
				</form>
			</div>
			
			<div class="orderdetails_tit">
				<span>3</span>预订人信息
			</div>
			
			<!-- 预订人部分start -->
			<div flag="messageDiv">
				<form id="orderpersonMesdtail">
					<p class="ydbz_qdmc">预订渠道：
						<select name="agentShow" onChange="changeAgent(this)" disabled="disabled">
						<option value="${agentinfo.id }" <c:if test="${orderDetailInfoMap.agentId == -1  }">selected="selected"</c:if> >非签约渠道</option>
							<c:if test="${not empty agentinfoList }">
								<c:forEach items="${agentinfoList}" var="agentinfo">
									<option value="${agentinfo.id }" <c:if test="${orderDetailInfoMap.agentId == agentinfo.id  }">selected="selected"</c:if> >${agentinfo.agentName }</option>
								</c:forEach>
								
							</c:if>
						</select>
					</p>
					<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
					<input type="hidden" value = "${orderDetailInfoMap.agentId}" name="agent"/>
						<li><label><span class="xing">*</span>渠道联系人：</label><input maxlength="10" type="text" name="agentContact" value="${orderDetailInfoMap.agentContact }" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/></li>
						<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input maxlength="15" type="text" name="agentTel" value="${orderDetailInfoMap.agentTel }" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div></li>
						<div flag="messageDiv" style="display:none">
							<li><label>渠道地址：</label><input maxlength="" name="agentAddress" type="text" value="${orderDetailInfoMap.agentAddress }"/></li>
							<li><label>传真：</label><input maxlength="" name="agentFax" type="text" value="${orderDetailInfoMap.agentFax }"/></li>
							<li><label>QQ：</label><input maxlength="" name="agentQQ" type="text" value="${orderDetailInfoMap.agentQQ }"/></li>
							<li><label>Email：</label><input maxlength="" name="agentEmail" type="text" value="${orderDetailInfoMap.agentEmail }"/></li>
							<li><label>其他：</label><input maxlength="" name="agentRemarks" type="text" value="${orderDetailInfoMap.agentRemarks }"/></li>
						</div>
					</ul>
				</form>
			</div>
			<!-- 预订人部分end -->

			<!-- 未完成 -->

			<div class="orderdetails_tit orderdetails_titpr">
				<span>4</span>游客信息
			</div>
			
			<!-- 游客部分start -->
				<div id="traveler">
				<c:if test="${not empty orderDetailInfoMap.travelInfoList }">
				<c:forEach items="${orderDetailInfoMap.travelInfoList }" var="travelInfo">
				
						<form class="travelerTable">
							<div class="tourist">
								<div class="tourist-t">
									<input id="travelId" type="hidden" name ="travelId" value="${travelInfo.id }"  class="traveler" >
									<span class="add_seachcheck" onclick="boxCloseOnAdd(this,'1')"></span><label class="ydLable">游客<em class="travelerIndex"></em>:</label>
									<div class="tourist-t-off">${travelInfo.travelName }<span class="fr">结算价：<span class="gray14">${travelInfo.payPrice}</span><span class="ydFont2"></span></span></div>
									<div class="tourist-t-on">
										<label><input type="radio" class="traveler" name="personTypeinner" value="1" <c:if test="${travelInfo.personType == 1 }">checked="checked"</c:if> disabled="disabled" onClick="changePayprice(this)"/>成人</label>
										<label><input type="radio" class="traveler" name="personTypeinner" value="2" <c:if test="${travelInfo.personType == 2 }">checked="checked"</c:if> disabled="disabled" onClick="changePayprice(this)"/>儿童</label>
										<label><input type="radio" class="traveler" name="personTypeinner" value="3" <c:if test="${travelInfo.personType == 3 }">checked="checked"</c:if> disabled="disabled" onClick="changePayprice(this)"/>特殊人群</label>
								    	
									    	<div class="tourist-t-r"> 是否联运：
												<label><input id="intermodalNotNeed" type="radio" class="ydbz2intermodal1" name="ydbz2intermodalType" value="0"  <c:if test="${travelInfo.intermodalType == 0 }">checked="checked"</c:if>  onclick="ydbz2intermodalSale(this)" disabled="disabled" />不需要</label>
												<label><input id="intermodalNeed" type="radio" name="ydbz2intermodalType" value="1"  <c:if test="${travelInfo.intermodalType == 1 }">checked="checked"</c:if> onclick="ydbz2intermodalSale(this)" disabled="disabled"/>需要</label>
												<span>
													<select onchange="ydbz2interselectSale(this)" name="intermodal">
														<c:forEach items="${fns:getIntermodalStrategyList(orderDetailInfoMap.airticketId) }" var="intermodalInfo">
															<option id="${intermodalInfo.id }" value="${intermodalInfo.price }"  currenyMark="${intermodalInfo.priceCurrency.currencyMark}" currenyName="${intermodalInfo.priceCurrency.currencyName}"<c:if test="${travelInfo.intermodalId == intermodalInfo.id }">selected="selected"</c:if> >${intermodalInfo.groupPart }</option>
														</c:forEach>
													</select>
													<em2 style = 'display:none'>${travelInfo.price }</em2>
												币种：<cur>${fns:getCurrencyInfo(travelInfo.icurrency,0,'name') }</cur>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联运价格：<em>${travelInfo.price }</em>
												</span>
											</div>
										
									</div>
									
								</div>
								<div class="tourist-con" flag="messageDiv">
									<!--游客信息左侧开始-->
									<div class="tourist-left">
									<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
										<ul class="tourist-info1 clearfix" flag="messageDiv">
											<li>
												<label class="ydLable"><span class="xing">*</span>姓名：</label>
												<input type="text" maxlength="30" name="travelerName" value="${travelInfo.travelName }" class="traveler" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
											</li>
											
											<li>
												<label class="ydLable">英文／拼音：</label>
												<input type="text" maxlength="30" name="travelerPinyin" value="${travelInfo.travelEName }" class="traveler">
											</li>
											<li>
												<label class="ydLable"><span class="xing">*</span>性别：</label>
												<select name="travelerSex" class="selSex">
													<option value="1"  <c:if test="${travelInfo.sex == 1 }"> selected="selected"</c:if> >男</option>
													<option value="2"  <c:if test="${travelInfo.sex == 2 }"> selected="selected"</c:if> >女</option>
												</select> 
											</li>
											<li>
												<label class="ydLable">国籍：</label>
												<select name="nationality" class="selCountry">
													<c:forEach items="${countryList }" var="country">
														<option value="${country.id}" <c:if test="${travelInfo.nationality == country.id }">selected="selected"</c:if>  >${country.countryName_cn}</option>
													</c:forEach>
												</select>
											</li>
											<li>
												<label class="ydLable">出生日期：</label>
												<input type="text" maxlength="" name="birthDay" value="${travelInfo.birthDay }" class="traveler dateinput" onclick="WdatePicker()">
											</li>
											<li>
												<label class="ydLable"><span class="xing"></span>联系电话：</label>
												<input type="text" name="telephone" value="${travelInfo.telephone }" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
											</li>
											
											
											<li>
												<label class="ydLable"><span class="xing">*</span>护照号：</label>
												<input type="text" name="passportCode" value="${travelInfo.passportCode }" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
											</li>
											
											<li>
												<label class="ydLable"><span class="xing">*</span>护照有效期：</label>
												<input type="text" maxlength="" name="passportValidity" value='<fmt:formatDate value="${travelInfo.passportValidity}" pattern="yyyy-MM-dd"/>' class="traveler dateinput" onclick="WdatePicker()">
											</li>
											
											<li>
												<label class="ydLable"><span class="xing"></span>身份证号：</label>
												<input type="text" name="idCard" value="${travelInfo.idCard }" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
											</li>
											<li>
												<label class="ydLable">护照类型：</label>
												<select name="passportType" class="selCountry">
													<option value="1"  <c:if test="${travelInfo.passportType == 1 }">selected</c:if> >因公护照</option>
													<option value="2" <c:if test="${travelInfo.passportType == 2 }">selected</c:if> >因私护照</option>
												</select>
											</li>	                
										</ul>
										<div class="ydbz_tit ydbz_tit_child">备注：<textarea name="remarks" class="textarea_long" value="${travelInfo.remark }">${travelInfo.remark }</textarea></div>
										
									</div>
									<!--游客信息左侧结束-->
									<div class="kong"></div>
									<div class="fr" style="padding-right:10px">结算价：<span class="gray14"></span>
										<span class="ydFont2 priceOne">${travelInfo.payPrice }</span>
										<input type="hidden" value="${travelInfo.payPrice }" class="traveler" name="payPrice">
									</div>
									
								</div>
								
								<!--保存、取消按钮开始-->
								<div class="rightBtn"><a class="btn" onclick="saveTravel(this, 0)"/>保存</a></div>
								<!--保存、取消按钮结束-->
							</div>
						</form>
				</c:forEach>
				</c:if>	
				</div>

			<!-- 游客部分 end -->
			
			<!-- 添加游客模板start -->
			<div id="travelerTemplate" style="display:none;">
				<form class="travelerTable">
					<div class="tourist">
						<div class="tourist-t">
							<a class="btn-del" style="cursor:pointer;" name="deleteTraveler" >删除aa</a>
							<input type="hidden" name ="travelId" value=""  class="traveler" >
							<span class="add_seachcheck" onclick="boxCloseOnAdd(this,'1')"></span><label class="ydLable">游客<em class="travelerIndex"></em>:</label>
							<div class="tourist-t-off"><em></em><span class="fr">结算价：<span class="gray14"></span><span class="ydFont2"></span></span></div>
							<div class="tourist-t-on">
								<label><input type="radio" class="traveler" name="personTypeinner" value="1" checked="checked" onClick="changePayprice(this)"/>成人</label>
								<label><input type="radio" class="traveler" name="personTypeinner" value="2" onClick="changePayprice(this)"/>儿童</label>
								<label><input type="radio" class="traveler" name="personTypeinner" value="3" onClick="changePayprice(this)"/>特殊人群</label>
								
									<div class="tourist-t-r"> 是否联运：
										<label><input type="radio" class="ydbz2intermodal1" name="ydbz2intermodalType" value="0" checked="checked" onclick="ydbz2intermodalSale(this)"/>不需要</label>
										<c:if test = "${orderDetailInfoMap.intermodalType != 0}">
											<label><input type="radio" name="ydbz2intermodalType" value="1" onclick="ydbz2intermodalSale(this)"/>需要</label>
										</c:if>
										<span style="display:none;">
										<select id="intermodalAreaChoose" onchange="ydbz2interselectSale(this)" name="intermodal">
													<option id="-1" value="0" currenyMark="" currenyName="无">请选择555</option>
											<c:forEach items="${fns:getIntermodalStrategyList(orderDetailInfoMap.airticketId) }" var="intermodalInfo">
													<option id="${intermodalInfo.id }" value="${intermodalInfo.price }" currenyMark="${intermodalInfo.priceCurrency.currencyMark}" currenyName="${intermodalInfo.priceCurrency.currencyName}">${intermodalInfo.groupPart }</option>
											</c:forEach>
										</select>
											币种：<cur>无</cur>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联运价格：<em>0</em>
											<em2 style = 'display:none'>0</em2>
										</span>
									</div>
								
							</div>
							
						</div>
						
						<div class="tourist-con" flag="messageDiv">
							<!--游客信息左侧开始-->
							<div class="tourist-left">
							<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
								<ul class="tourist-info1 clearfix" flag="messageDiv">
									<li>
										<label class="ydLable"><span class="xing">*</span>姓名：</label>
										<input type="text" maxlength="30" name="travelerName" class="traveler" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
									</li>
									
									<li>
										<label class="ydLable">英文／拼音：</label>
										<input type="text" maxlength="30" name="travelerPinyin" class="traveler">
									</li>
									<li>
										<label class="ydLable"><span class="xing">*</span>性别：</label>
										<select name="travelerSex" class="selSex">
											<option value="1" selected="selected">男</option>
											<option value="2" >女</option>
										</select> 
									</li>
									<li>
										<label class="ydLable">国籍：</label>
										<select name="nationality" class="selCountry">
											<c:forEach items="${countryList }" var="country">
												<option value="${country.id}">${country.countryName_cn}</option>
											</c:forEach>
										</select>
									</li>
									<li>
										<label class="ydLable">出生日期：</label>
										<input type="text" maxlength="" name="birthDay" class="traveler dateinput" onclick="WdatePicker()">
									</li>
									<li>
										<label class="ydLable"><span class="xing"></span>联系电话：</label>
										<input type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
									</li>
									
									
									<li>
										<label class="ydLable"><span class="xing">*</span>护照号：</label>
										<input type="text" name="passportCode" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
									</li>
									
									<li>
										<label class="ydLable"><span class="xing">*</span>护照有效期：</label>
										<input type="text" maxlength="" name="passportValidity" class="traveler dateinput" onclick="WdatePicker()">
									</li>
									
									<li>
										<label class="ydLable"><span class="xing"></span>身份证号：</label>
										<input type="text" name="idCard" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
									</li>
									<li>
										<label class="ydLable">护照类型：</label>
										<select name="nationality" class="selCountry">
											<option value="1">因公护照</option>
											<option value="2">因私护照</option>
										</select>
									</li>	                
								</ul>
								<div class="ydbz_tit ydbz_tit_child">　　备注：<textarea name="remarks" class="textarea_long"></textarea></div>
								
							</div>
							<!--游客信息左侧结束-->
							<div class="kong"></div>
							<div class="fr" style="padding-right:10px">结算价：<span class="gray14"></span>
								<span class="ydFont2 priceOne">${orderDetailInfoMap.adultTaxPrice }</span> 
								<input type="hidden" value="${orderDetailInfoMap.adultTaxPrice }" class="traveler" name="payPrice">
							</div>
							
						</div>
						<!--保存、取消按钮开始-->
						<div class="rightBtn"><a class="btn" onclick="saveTravel(this, 0)"/>保存</a></div>
						<!--保存、取消按钮结束-->
					</div>
				</form>
			</div>
			<!-- 添加游客模板end -->
			
			<!-- 添加游客按钮start -->
			<div class="touristBtn"><a class="btn-addGrey" id="addTraveler">添加游客</a></div>
			<!-- 添加游客按钮end -->
			
			<!-- 支付部分start -->
			<div class="orderdetails_tit">
				<span>5</span>支付信息
			</div>
			
			<c:if test="${not empty orderDetailInfoMap.orderPayInfoList }">
				<c:forEach items="${orderDetailInfoMap.orderPayInfoList }" var="payInfo">
					<p class="orderdetails6">
						<span>付款方式：${payInfo.payTypeName }</span> 
						<span>支付款类型：${fns:getDictLabel(payInfo.payPriceType,"payprice_Type" , "")}</span> 
						<span>付款金额：${payInfo.payPrice }</span> 
						<span>付款时间： <fmt:formatDate value="${payInfo.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></span> 
						<span>付款凭证： <a lang="4453" class="showpayVoucher">${payInfo.docName }</a> </span>
					</p>
				</c:forEach>
			</c:if>	
					<div class="payment_information">
						<p class="orderdetails6">
							<span>成人：${orderDetailInfoMap.adultPrice }/成人 x ${orderDetailInfoMap.adultNum }人</span> 
							<span>儿童：${orderDetailInfoMap.childPrice }/儿童 x ${orderDetailInfoMap.childNum }人</span> 
							<span>特殊人群：${orderDetailInfoMap.specialPrice }/特殊人群 x ${orderDetailInfoMap.specialNum }人</span> 
							<!-- <span>内陆机票：￥33/人x1</span>  -->
							<span>其他费用：${orderDetailInfoMap.otherFee }</span>
						</p>
						<c:if test="${orderDetailInfoMap.type == 1 }">
							<div class="ordermoney ordermoney2">
								应收总计：<em class="gray14"></em><span class="orderAllPrice">${orderDetailInfoMap.totalMoney }</span><br />达账金额：<em class="gray14"></em><span>${orderDetailInfoMap.accountedMoney }</span>
								<input type = "hidden" value = "${orderDetailInfoMap.totalMoney }" name = "orderAllP">
							</div>
						</c:if>
					</div>
					
					<div class="ydbz_sxb" id="secondDiv" style="display: block;">
						<div class="ydBtn ydBtn2">
 							<a class="ydbz_s gray" onclick="closeCurWindow()">取消</a><input type="button" class="btn btn-primary fl" value="确定" onClick="saveRemarkAndAgentInfo()">
						</div>
					</div>
					

			<!-- 支付部分end -->
			
		</div>
	</div>
	<!--右侧内容部分结束-->
	
	
<!-- js -->
<script type="text/javascript">
/*预定第二步联运显示价格*/
function ydbz2interselectSale(obj){
	var value=$(obj).find("option:selected").attr('currenyMark')+$(obj).find("option:selected").val();
	var name=$(obj).find("option:selected").attr('currenyName');
	$(obj).parent().find('cur').html(name);
	$(obj).parent().find('em').html(value);
	var originaleEmvalue = $(obj).parent().find('em2').html();//改变之前的联运价格
	var ppvalue = $(obj).parent().parent().parent().parent().parent().find('.priceOne').html();
	//计算更改后的结算价 start 
	//算法  当前结算价 + 当前联运价 - 改变之前的联运价格
	var ppvalues = ppvalue.split('+');//结算价拆分为多个币种的数组
	var curFlag = false;//计算当前联运价格标志
	var oriFlag = false;//计算原始联运价格标志
	var curValue2 = "";
	var monValue2 = "";
	//当前联运价 rmoney
	if(value != "" && value != 0){
		var nV = value.indexOf('-');
		if(nV == -1){
			curValue2 = /^\D+(?=\d)/.exec(value)[0];//币种	
		} else {
			curValue2 = value.substr(0, nV);
		}
		monValue2 = value.split(curValue2)[1];//钱数信息
	} else {
		curFlag = true;
	}
	var curValue3 = "";
	var monValue3 = "";
	//改变之前联运价
	if(originaleEmvalue != 0){
		var nO = originaleEmvalue.indexOf('-');
		if(nO == -1){
			curValue3 = /^\D+(?=\d)/.exec(originaleEmvalue)[0];//币种
		}else {
			curValue3 = originaleEmvalue.substr(0,nO);
		}
		monValue3 = originaleEmvalue.split(curValue3)[1];//钱数信息
	} else {
		oriFlag = true;
	}
	var resultMoney = "";
	for(var i = 0; i < ppvalues.length; i++){
		var tempValue = ppvalues[i].trim();
		if(tempValue){ 
 			var n1 = tempValue.indexOf('-');
 			var curValue;
 			var rollFlag = false;
 			if(n1 == -1){
			
 				curValue = /^\D+(?=\d)/.exec(tempValue)[0];//币种
 			}else{
 				curValue = tempValue.substr(0,n1);
 				rollFlag = true;
 			}
			var monValue = tempValue.split(curValue)[1];//钱数信息
			if(curValue == curValue2){//币种相同 相加
				if(rollFlag == true){
					monValue = monValue2 - (monValue.substr(1));
				}else{
					monValue =  rmoney(monValue) + rmoney(monValue2);
					monValue = milliFormat(monValue,'1');
				}
				curFlag = true;
			}
			if(curValue == curValue3){//币种相同 相减
				if(rollFlag == true){
					monValue = - monValue3 - (monValue.substr(1)) ;
				} else {
					monValue = rmoney(monValue) - rmoney(monValue3);
					monValue = milliFormat(monValue,'1');
				}
				oriFlag = true;
			}
			if(monValue == 0){
				ppvalues[i] = "";
			} else {
				ppvalues[i] = curValue + monValue;
				if(resultMoney == ""){
					resultMoney += ppvalues[i];
				} else {
					resultMoney += "+" + ppvalues[i];
				}
			}
		}
	}
	
	if(curFlag == false && oriFlag == false){
		if(curValue2 == curValue3){
			if((monValue2 - monValue3) != 0){
				resultMoney +=  "+" + curValue2 + (monValue2 - monValue3);
			}
		} else {
			resultMoney +=  "+" + curValue2 + monValue2 + "+" + curValue3 + (-monValue3);
		}
	} else if (curFlag == false){
		resultMoney +=  "+" + curValue2 + monValue2;
	} else if (oriFlag == false){
		resultMoney +=  "+" + (curValue3 + (-monValue3));
	}
	//计算更改后的结算价 end
	$(obj).parent().parent().parent().parent().parent().find('.priceOne').html(resultMoney);
	$(obj).closest("form").find("input[name='payPrice']").val(resultMoney);
	$(obj).parent().find('em2').html(value == "" ? 0 : value);
	orderAllPrice(curValue2,curValue3,monValue2,monValue3)
};

function orderAllPrice(curValue2,curValue3,monValue2,monValue3){
	var curFlag = false;//计算当前联运价格标志
	var oriFlag = false;//计算原始联运价格标志
	var ppvalue = $(".orderAllPrice").html();
	var ppvalues = ppvalue.split('+');
	var resultMoney = "";
	for(var i = 0; i < ppvalues.length; i++){
		var tempValue = ppvalues[i].trim();
		if(tempValue){ 
			var n1 = tempValue.indexOf('-');
			var curValue;
			var rollFlag = false;
			if(n1 == -1){
				curValue = /^\D+(?=\d)/.exec(tempValue)[0];//币种
			}else{
				curValue = tempValue.substr(0,n1);
				rollFlag = true;
			}
			var monValue = tempValue.split(curValue)[1];//钱数信息
			if(curValue == curValue2){//币种相同 相加
				if(rollFlag == true){
					monValue = monValue2 - (monValue.substr(1));
				}else{
					monValue =  rmoney(monValue) + rmoney(monValue2);
					monValue = milliFormat(monValue,'1');
				}
				curFlag = true;
			}
			if(curValue == curValue3){//币种相同 相减
				if(rollFlag == true){
					monValue = - monValue3 - (monValue.substr(1)) ;
				} else {
					monValue = rmoney(monValue) - rmoney(monValue3);
					monValue = milliFormat(monValue,'1');
				}
				oriFlag = true;
			}
			if(monValue == 0){
				ppvalues[i] = "";
			} else {
				ppvalues[i] = curValue + monValue;
				if(resultMoney == ""){
					resultMoney += ppvalues[i];
				} else {
					resultMoney += "+" + ppvalues[i];
				}
			}
		}
	}
	if(curFlag == false && oriFlag == false){
		if(curValue2 == curValue3){
		  if((monValue2 - monValue3) != 0){
			  resultMoney +=  "+" + curValue2 + (monValue2 - monValue3);
		  }
		} else if(monValue3==""){
		  resultMoney +=  "+" + curValue2 + monValue2 ;
		}else {
		  resultMoney +=  "+" + curValue2 + monValue2 + "+" + curValue3 + (-monValue3);
		}
	} else if (curFlag == false){
		if(curValue2!=""){resultMoney +=  "+" + curValue2 + monValue2};
	} else if (oriFlag == false){
		if(monValue3!=""){resultMoney +=  "+" + (curValue3 + (-monValue3));}
	}
	//计算更改后的结算价 end
	$(".orderAllPrice").html(resultMoney);	
	$("input[name='orderAllP']").val(resultMoney);
}
//预定第二步是否联运
// function ydbz2intermodalSale(obj){
// 	if($(obj).val()==0){
// 		    $(obj).parent().parent().find('span').hide();
// 		} else{
// 			$(obj).parent().parent().find('span').show();
// 		}
	
// 	if ($.isFunction(window.lyChange)){
// 		lyChange(obj);
//  	}	
//  	var value = $("#intermodalAreaChoose").find("option:selected").attr('value');
//  	var name = $("#intermodalAreaChoose").find("option:selected").attr('currenyName');
//  	$("#intermodalAreaChoose").parent().find('cur').html(name);
//  	$("#intermodalAreaChoose").parent().find('em').html(value);
// };
function ydbz2intermodalSale(obj){
	if($(obj).val()==0){
		    $(obj).parent().parent().find('span').hide();
		} else{
			$(obj).parent().parent().find('span').show();
		}
	var thisPersonType=$(obj).closest("form").find("input[name='personTypeinner']:checked");
	changePayprice(thisPersonType);
}

	function closeCurWindow(){
		this.close();
	}
	$(document).ready(function(e) {
	    var leftmenuid = $("#leftmenuid").val();
	    $(".main-nav").find("li").each(function(index, element) {
	        if($(this).attr("menuid")==leftmenuid){
	            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
	        }
	    });
	});
	$(function(){
	    $('.closeNotice').click(function(){
	        var par = $(this).parent().parent();
	        par.hide();
	        par.prev().removeClass('border-bottom');
	        par.prev().find('.notice-date').show();
	    });
	    $('.showNotice').click(function(){
	        $(this).parent().hide();
	        var par = $(this).parent().parent();
	        par.addClass('border-bottom');
	        par.next().show();
	    });
	});
	$(function(){
		$('.main-nav li').click(function(){
			$(this).addClass('select').siblings().removeClass('select');
		})
	})
	
	String.prototype.formatNumberMoney= function(pattern){
		  var strarr = this?this.toString().split('.'):['0'];   
		  var fmtarr = pattern?pattern.split('.'):[''];   
		  var retstr='';   
		  var str = strarr[0];
		  var fmt = fmtarr[0];
		  var i = str.length-1;     
		  var comma = false;   
		  for(var f=fmt.length-1;f>=0;f--){   
		    switch(fmt.substr(f,1)){   
		      case '#':   
		        if(i>=0 ) retstr = str.substr(i--,1) + retstr;   
		        break;   
		      case '0':   
		        if(i>=0) retstr = str.substr(i--,1) + retstr;   
		        else retstr = '0' + retstr;   
		        break;   
		      case ',':   
		         comma = true;   
		         retstr=','+retstr;   
		        break;   
		     }   
		   }   
		  if(i>=0){   
		    if(comma){   
		      var l = str.length;   
		      for(;i>=0;i--){   
		         retstr = str.substr(i,1) + retstr;   
		        if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;   
		       }   
		     }   
		    else retstr = str.substr(0,i+1) + retstr;   
		   }   
		  
		   retstr = retstr+'.';   
		   
		   str=strarr.length>1?strarr[1]:'';   
		   fmt=fmtarr.length>1?fmtarr[1]:'';   
		   i=0;   
		  for(var f=0;f<fmt.length;f++){   
		    switch(fmt.substr(f,1)){   
		      case '#':   
		        if(i<str.length) retstr+=str.substr(i++,1);   
		        break;   
		      case '0':   
		        if(i<str.length) retstr+= str.substr(i++,1);   
		        else retstr+='0';   
		        break;   
		     }   
		   }   
		  return retstr.replace(/^,+/,'').replace(/\.$/,'');   
	}
	
	String.prototype.replaceSpecialChars=function(regEx){
		if(!regEx){
			regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
		}
		return this.replace(regEx,'');
		
	};
</script>
</body>
</html>