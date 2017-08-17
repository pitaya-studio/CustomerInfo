<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机票成本录入详情页</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/modules/cost/cost.js" type="text/javascript"></script>
<style type="text/css">
		.td-extend .handle {
			background-image: none;
		}
		.xuanfu {
			position: absolute;
			top: 0px;
			left: 0;
			right:130px;
		}
		#hoverWindow {
			text-align:left;
			width:35em;
			top: 29%;
			left: 0%;
			word-wrap: break-word;
		}
</style>
<script type="text/javascript">	
	$(document).delegate(".downloadzfpz","click",function(){
		window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
	});

	var deleteCost = function(id, classType){
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/cost/manager/delete",
					cache:false,
					async:false,
					data:{id : id,
						type : classType,groupId : "", orderType : "",visaId : ""},
					success: function(e){
						window.location.reload();
					},
					error : function(e){
						top.$.jBox.tip('请求失败。','error');
						return false;
					}
				 });
			}
		});
	};

	var cancelCost = function(id, classType){
		$.jBox.confirm("确定要取消成本审批吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/costReview/activity/cancel",
					cache:false,
					async:false,
					data:{id : id,
						type : classType},
					success: function(e){
						window.location.reload();
					},
					error : function(e){
						top.$.jBox.tip('请求失败。','error');
						return false;
					}
				 });
			}
		});
	};

	function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}

	function addCostHQX(budgetType) {
		var ht = ($(window).height())*0.7;
		var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${activityAirTicket.id}' +"/"+ '${activityAirTicket.id}' +"/"+ budgetType  +"/7/"+ '${deptId}';
		$.jBox(iframe, {
			title: "成本录入",
			width: 380,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function addOtherCostHQX(budgetType) {
		var ht = ($(window).height())*0.7;
		var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${activityAirTicket.id}' +"/"+ '${activityAirTicket.id}' +"/"+ budgetType  +"/7" +"/"+ '${deptId}';
		$.jBox(iframe, {
			title: "其它收入录入",
			width: 380,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function updateCostHQX(costid) {
		var ht = ($(window).height())*0.7;
		var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${activityAirTicket.id}' +"/"+ '${activityAirTicket.id}' +"/"+ costid  +"/7/"+ '${deptId}';
		$.jBox(iframe, {
			title: "成本修改",
			width: 380,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function updateOtherCostHQX(costid) {
		var ht = ($(window).height())*0.7;
		var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${activityAirTicket.id}' +"/"+ '${activityAirTicket.id}' +"/"+ costid  +"/7" +"/"+ '${deptId}';
		$.jBox(iframe, {
			title: "其它收入修改",
			width: 380,
			height: ht,
			persistent:true,
			buttons: {}
		}).find('#jbox-content').css('overflow-y','hidden');
	}

	function saveCheckBox(id,budgetType){
		var tmp=0;
		var msg = "";
		$("input[name='"+id+"']").each(function(){ 
			if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
				tmp=tmp +","+$(this).attr('value');
				if(${companyUuid eq DHJQ}) {
					if ($(this).parent().next().html() == "") {
						msg = "请上传附件后重新提交";
					}
				}
			}
		});
		if(tmp=="0"){
			if (budgetType=='0'){
				alert("请选择预算成本");
			}else{
				alert("请选择实际成本");
			}
			return false;
		}
		if(msg != ""){
			alert(msg);
			return false;
		}
		$.ajax({
			type: "POST",
			url: "${ctx}/costReview/airticket/airticketCostApply",
			cache:false,
			async:false,
			data:{ 
				costList:tmp,visaIds:"",groupId:"",orderType:"",activityId : '${activityAirTicket.id}',deptId : '${deptId}' },
			success: function(data){
				$.jBox.tip(data, 'success');
				window.location.reload();
			},
			error : function(e){
				alert('请求失败。');
				return false;
			}
		});
	}

	function payCheckBox(id,budgetType){	
		var tmp= '';
		$("input[name='"+id+"']").each(function(){
			if ($(this).is(":checked")){
				if(!tmp){
					tmp = $(this).val();
				}else{
					tmp = tmp + "," + $(this).val();
				}
			}
		});
		if(!tmp){
	    	alert("请选择需要付款审批的成本项!");
	    	return false;
	    }
		$.ajax({
			type: "POST",
			url: "${ctx}/review/airticket/payment/apply",
			cache:false,
			dataType:"json",
			async:false,
			data:{items:tmp},
			success: function(data){
				if(data.flag){
					$.jBox.tip('申请付款成功', 'success');
					window.location.reload();
				}else{
					$.jBox.tip('付款申请失败，' + data.msg, 'error');
					return false;
				}
			},
			error : function(e){
				$.jBox.tip('申请付款失败', 'error');
				 return false;
			}
		});
	}
	
	var cancelPayCost = function(reviewId){
		$.jBox.confirm("确定要取消付款审批吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/review/payment/web/cancelReview",
					cache:false,
					async:false,
					data:{reviewId:reviewId},
					success: function(data){
						if(data.flag){
							$.jBox.tip('取消成功', 'success');
							window.location.reload();
						}else{
							$.jBox.tip('取消失败，' + data.msg, 'error');
							return false;
						}
					},
					error : function(e){
						top.$.jBox.tip('请求失败。','error');
						return false;
					}
				});
			}
		});
	}

function milliFormat(s){//添加千位符
	if(/[^0-9\.\-]/.test(s)) return "invalid value";
	s=s.replace(/^(\d*)$/,"$1.");
	s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	s=s.replace(".",",");
	var re=/(\d)(\d{3},)/;
	while(re.test(s)){
		s=s.replace(re,"$1,$2");
	}
	s=s.replace(/,(\d\d)$/,".$1");
	return s.replace(/^\./,"0.")
}

function expand(child,obj,id) {
		$.ajax({
			url:"${ctx}/cost/manager/payedRecord/",
			type:"POST",
			data:{id:id},
			success:function(data){
			 var htmlstr="";
			 var num = data.length;
			 if(num>0){
				var str1='';
				for(var i =0;i<num;i++){
					var str = data[i].payvoucher.split("|")
					var idstr = data[i].ids.split("|");
					var index = str.length;
					if(index>0){
						for(var a=0;a<index;a++){
							str1+="<a class=\"downloadzfpz\" lang=\""+idstr[a]+"\">"+str[a]+"</a><br/>"
						}
					}
					htmlstr+="<tr><td class='tc'>"+data[i].payTypeName+"</td><td class='tc'>"+data[i].currency_mark+milliFormat(parseFloat(data[i].amount).toFixed(2))+"</td><td class='tc'>"+data[i].createDate+"</td><td class='tc'>"+"其它收入"+
					"</td><td class='tc'>";
					if(data[i].isAsAccount == null) {
						htmlstr+="待收款";
					}else if(data[i].isAsAccount == 1) {
						htmlstr+="已达账";
					}else if(data[i].isAsAccount == 101) {
						htmlstr+="已撤销";
					}else if(data[i].isAsAccount == 102) {
						htmlstr+="<div class='pr xuanfudiv'>已驳回";
						if(data[i].rejectReason!=null && data[i].rejectReason!="") {
							htmlstr+="<div class='ycq xuanfu' style='width: 24px;'>备注</div><div class='hover-title team_top hide' id='hoverWindow'>"+data[i].rejectReason+"</div>";
						}
					}
					htmlstr+="</td><td class='tc'>"+str1+"</td>"+"</tr>";
					str1='';
				 }
			 }
			 $("#rpi").html(htmlstr);
			}
		});
		if ($(child).is(":hidden")) {
			$(obj).html("收起收款记录");
			$(obj).parents("tr").addClass("tr-hover");
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
		} else {
			if (!$(child).is(":hidden")) {
				$(obj).parents("tr").removeClass("tr-hover");
				$(child).hide();
				$(obj).parents("td").removeClass("td-extend");
				$(obj).html("收款记录");
			}
		}
	}

	function iFrameHeight() {
		var ifm= document.getElementById("iframepage");
		var subWeb = document.frames ? document.frames["iframepage"].document : ifm.contentDocument;
		if(ifm != null && subWeb != null) {
			ifm.height = subWeb.body.scrollHeight;
			ifm.width = subWeb.body.scrollWidth;
		}
	}
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="mod_nav">运控 > 产品成本录入 > 机票录入详情页</div>
			
	<div class="produceDiv">
		<div class="orderdetails_tit">机票产品信息</div>
		<div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden">
		<p class="ydbz_mc">产品编号: ${activityAirTicket.productCode}, ${activityAirTicket.departureCityLabel() }
			&mdash;
			<c:forEach items="${areas}" var="tn">
				<c:if test="${tn.id == activityAirTicket.arrivedCity}">${tn.name}</c:if>
			</c:forEach>：
			<c:if test="${activityAirTicket.airType eq 1}">多段</c:if>
			<c:if test="${activityAirTicket.airType eq 2}">往返</c:if>
			<c:if test="${activityAirTicket.airType eq 3}">单程</c:if>
		</p>
		
		<c:if test="${ activityAirTicket.airType eq 1}">
			<c:forEach items="${activityAirTicket.flightInfos}" var="flightInfos" varStatus="s">
				<div class="title_samil">第${flightInfos.number}段：
					<c:if test="${flightInfo.ticket_area_type eq '3'}">内陆</c:if>
					<c:if test="${flightInfo.ticket_area_type eq '2'}">国际</c:if>
					<c:if test="${flightInfo.ticket_area_type eq '1'}">内陆+国际</c:if>
				</div>
				<table border="0" width="90%">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发机场：</td>
							<td class="mod_details2_d2">
								<c:forEach items="${airportlist}" var="airportlist">
									<c:if test="${airportlist.id eq flightInfos.leaveAirport}">${airportlist.airportName}</c:if>
								</c:forEach>
							</td>
							<td class="mod_details2_d1">到达机场：</td>
							<td class="mod_details2_d2">
								<c:forEach items="${airportlist}" var="airportlist">
									<c:if test="${airportlist.id eq flightInfos.destinationAirpost}">${airportlist.airportName}</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">出发时刻：</td>
							<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfos.startTime }"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfos.arrivalTime }"/></td>
							<td class="mod_details2_d1">航班号：</td>
							<td class="mod_details2_d2">${flightInfos.flightNumber }</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${flightInfos.airlinesLabel2() }</td>
							<td class="mod_details2_d1">舱位等级：</td>
							<td class="mod_details2_d2">${flightInfos.spaceGradeLabel() }</td>
							<td class="mod_details2_d1">舱位：</td>
							<td class="mod_details2_d2">${flightInfos.airspaceLabel() }</td>
						</tr>
					</tbody>
				</table>
			</c:forEach>
		</c:if>

		<c:if test="${ activityAirTicket.airType eq 2}">
			<div class="title_samil">去程：</div>
			<table border="0" width="90%">
				<tbody>
					<tr>
						<td class="mod_details2_d1">出发机场：</td>
						<td class="mod_details2_d2">
							<c:forEach items="${airportlist}" var="airportlist">
								<c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].leaveAirport}">
									${airportlist.airportName}
								</c:if>
							</c:forEach>
						</td>
						<td class="mod_details2_d1">到达机场：</td>
						<td class="mod_details2_d2">
							<c:forEach items="${airportlist}" var="airportlist">
								<c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].destinationAirpost}">
									${airportlist.airportName}
								</c:if>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">出发时刻：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].startTime }"/></td>
						<td class="mod_details2_d1">到达时刻：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].arrivalTime }"/></td>
						<td class="mod_details2_d1">航班号：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].flightNumber }</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">航空公司：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel2() }</td>
						<td class="mod_details2_d1">舱位等级：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].spaceGradeLabel() }</td>
						<td class="mod_details2_d1">舱位：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airspaceLabel() }</td>
					</tr>
				</tbody>
			</table>
			<div class="title_samil">返程：</div>
			<table border="0" width="90%">
				<tbody>
					<tr>
						<td class="mod_details2_d1">出发机场：</td>
						<td class="mod_details2_d2">
							<c:forEach items="${airportlist}" var="airportlist">
								<c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].leaveAirport}">
									${airportlist.airportName}
								</c:if>
							</c:forEach>
						</td>
						<td class="mod_details2_d1">到达机场：</td>
						<td class="mod_details2_d2">
							<c:forEach items="${airportlist}" var="airportlist">
								<c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].leaveAirport}">
									${airportlist.airportName}
								</c:if>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">出发时刻：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[1].startTime }"/></td>
						<td class="mod_details2_d1">到达时刻：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[1].arrivalTime }"/></td>
						<td class="mod_details2_d1">航班号：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[1].flightNumber }</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">航空公司：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airlinesLabel2() }</td>
						<td class="mod_details2_d1">舱位等级：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[1].spaceGradeLabel() }</td>
						<td class="mod_details2_d1">舱位：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airspaceLabel() }</td>
					</tr>
				</tbody>
			</table>
		</c:if>

		<c:if test="${activityAirTicket.airType eq 3}">
			<table border="0" width="90%">
				<tbody>
					<tr>
						<td class="mod_details2_d1">出发机场：</td>
						<td class="mod_details2_d2">
							<c:forEach items="${airportlist}" var="airportlist">
								<c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].leaveAirport}">
									${airportlist.airportName}
								</c:if>
							</c:forEach>
						</td>
						<td class="mod_details2_d1">到达机场：</td>
						<td class="mod_details2_d2">
							<c:forEach items="${airportlist}" var="airportlist">
								<c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].destinationAirpost}">
									${airportlist.airportName}
								</c:if>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">出发时刻：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].startTime }"/></td>
						<td class="mod_details2_d1">到达时刻：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].arrivalTime }"/></td>
						<td class="mod_details2_d1">航班号：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].flightNumber }</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">航空公司：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel2() }</td>
						<td class="mod_details2_d1">舱位等级：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].spaceGradeLabel() }</td>
						<td class="mod_details2_d1">舱位：</td>
						<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airspaceLabel() }</td>
					</tr>
				</tbody>
			</table>
		</c:if>
		</div>

		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">订单列表  &nbsp; <a class="zksxs">收起筛选</a></div>
			</div>
		</div>
		<div class="ydxbds">
			<table class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
				<thead>
					<tr>
						<th width="8%">预定渠道</th>
						<th width="8%">订单号</th>
						<th width="7%">销售</th>
						<th width="13%">预订/剩余时间</th>
						<th width="9%">出/截团日期</th>
						<th width="7%">机票类型</th>
						<th width="4%">人数</th>
						<th width="8%">订单状态</th>
						<th width="7%">订单总额</th>
						<th width="8%">已收金额<br/>到账金额</th>
						<th width="8%">备注</th>
						<th width="5%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${airTicketOrderList}" var="airTicketOrder">
						<tr>
							<td class="tc">
							<c:choose>
							   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && airTicketOrder.agentName eq '非签约渠道'}">
							       直客
							   </c:when>
							   <c:otherwise>${airTicketOrder.agentName}</c:otherwise>
						</c:choose> 
							</td>
							<td class="tc">${airTicketOrder.orderNo}</td>
							<td class="tc">${airTicketOrder.orderUserName}</td>
							<td class="p0">
								<div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airTicketOrder.createDate }"/></div>
								<div class="close-date">${airTicketOrder.leftDays }</div>
							</td>
							<td style="padding: 0px;">
								<div class="out-date">${airTicketOrder.groupOpenDate}</div>
								<div class="close-date">${airTicketOrder.groupCloseDate}</div>
							</td>
							<td class="tc">
								<c:if test="${airTicketOrder.airType eq '3'}">单程</c:if>
								<c:if test="${airTicketOrder.airType eq '1'}">多段</c:if>
								<c:if test="${airTicketOrder.airType eq '2'}">往返</c:if>
							</td>
							<td class="tr">${airTicketOrder.personNum}</td>
							<td class="tc">${fns:getDictLabel(airTicketOrder.order_state, "order_pay_status", "")}</td>
							<td class="tr"><span class="tdorange fbold">${airTicketOrder.totalMoney} </span></td>
							<td class="p0 tr">	
								<div class="yfje_dd">	
									<span class="fbold">${airTicketOrder.payedMoney}</span>
								</div>
								<div class="dzje_dd">
									<span class="fbold">${airTicketOrder.accountedMoney}</span>
								</div>
							</td>
							<td>${airTicketOrder.comments}</td>
							<td class="tc"><a target="_blank" href="${ctx}/order/manage/airticketOrderDetail?orderId=${airTicketOrder.orderId}" onclick="">详情</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="costSum clearfix" style="width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
			<ul class="cost-ul" data-total="cost">
				<ul class="cost-ul" data-total="income">
					<li>订单总收入：&nbsp;¥<fmt:formatNumber type="currency" pattern="#,##0.00"
							value="${fns:getSum(incomeList,'totalMoney') }" /></li>
				</ul>
				<li>订单总人数：&nbsp;${fns:getSum(airTicketOrderList,'personNum') }</li>
			</ul>
		</div>

		<iframe id="iframepage" width="100%" height="100%" frameborder="0" src="${ctx}/cost/common/getCostRecordList/${activityAirTicket.id }/7" onLoad="iFrameHeight()"></iframe>

		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">审批日志</div>
			</div>
		</div>
		<div style="margin:0 auto; width:98%;">
			<ul class="spdtai">
				<c:forEach items="${costLog}" var="log" varStatus="status">
					<li><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${log.createDate}"/>&#12288;【${log.costName}】<c:if test="${log.result==-1}"><span class="invoice_back">审核已撤销</span></c:if><c:if test="${log.result==0}"><span class="invoice_back">审核未通过</span></c:if><c:if test="${log.result==1}"><span class="invoice_yes">审核通过</span></c:if>&#12288;【${log.name}】&#12288; <c:if test="${!empty log.remark}"><font color="red">批注:</font>&nbsp;${log.remark} </c:if></li>
				</c:forEach>
			</ul>
		</div>		
		<%@ include file="/WEB-INF/views/review/common/cost_payment_review_log.jsp"%>
	</div>

<script type="text/javascript">
//运控-成本录入-添加项目--小计
function costSums(obj,objshow,ordertype){
	var objMoney = {};
	 obj.each(function(index, element) {
	//var currencyName = $(element).find("td[name='tdCurrencyName']").text();
	var thisAccount = $(element).find("td[name='tdAccount']").text();
	if(thisAccount == '') {
		thisAccount = 1;
	}
	var thisPrice = $(element).find("td[name='tdPrice']").text();	
	if(thisPrice.indexOf('-')!=-1) thisPrice = $(element).find("td[name='tdPrice']").next().next().next().text();
	var thisReview = $(element).find("td[name='tdReview']").text();	
	var border=2;
	//去掉两边空格
	thisPrice=thisPrice.replace(/(^\s*)|(\s*$)/g, "");
	//找到金额中第一个数字位置
	for(var i=0;i<thisPrice.length;i++){
		if(thisPrice.substring(i,i+1).match(/^[0-9].*$/)){
			border=i;
			break;
		}
	}
	var currencyName =thisPrice.substring(0,border).trim();
	//金额去掉第一个字符(币种)
	thisPrice=thisPrice.substring(border);
	if(ordertype==2 || (ordertype==0 && trimStr(thisReview) != '已取消' && trimStr(thisReview) != '取消申请')|| (ordertype==1 && trimStr(thisReview) != '已取消' && (trimStr(thisReview) != '已驳回'&&trimStr(thisReview) != '审核失败(驳回)' && trimStr(thisReview) != '取消申请' && trimStr(thisReview) != '审批驳回') )){
		if(typeof objMoney[currencyName] == "undefined"){
			objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(new RegExp(",","gm"),""),10);
		}else{
			objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(new RegExp(",","gm"),""),10);
		}
	}
	});
	//输出结果
	var strCurrency = "";
	var sign = " + ";
	for(var i in objMoney){
		var isNegative = /^\-/.test(objMoney[i]);
		if(isNegative){
			sign = " - ";
		}
		if(strCurrency != '' || (strCurrency == '' && isNegative)){
			strCurrency += sign;
		}
		strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');
	}
	if(objshow.length>0) objshow.text("  "+strCurrency);
}

$(function(){
	costSums($('tr.otherCost'),$('#otherCostShow'),2);
	//实际成本录入-境内小计
	costSums($('tr.budgetInCost'),$('#budgetInShow'),0);
	//实际成本录入-境外小计  
	costSums($('tr.budgetOutCost'),$("#budgetOutShow"),0);

	costSums($('tr.actualInCost'),$("#actualInShow"),1);
	
	costSums($('tr.actualOutCost'),$("#actualOutShow"),1); 

});
</script>
	
</body>
</html>