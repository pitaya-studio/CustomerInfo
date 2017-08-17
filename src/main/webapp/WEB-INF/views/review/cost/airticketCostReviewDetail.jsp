<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>
	<c:choose>
		<c:when test="${budgetType eq 0 }">
			<c:choose>
				<c:when test="${read eq 1 }">预算成本-详情</c:when>
				<c:otherwise>预算成本-审批</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${budgetType eq 1 }">
			<c:choose>
				<c:when test="${read eq 1 }">实际成本-详情</c:when>
				<c:otherwise>实际成本-审批</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			机票成本详情页
		</c:otherwise>
	</c:choose>
</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/modules/cost/cost.js" type="text/javascript"></script>
<script type="text/javascript">
	//成本审核-拒绝某条成本
	function denyCost(dom,id){
		$.jBox($("#batch-verify-list").html(), {
			title: "备注：", buttons: {'取消': 0,'提交': 1}, submit: function (v, h, f) {
				if(v ==1) {
					var reason = f.reason;
					$.ajax({
						type:"POST",
						url:"${ctx}/costReview/activity/approveOrReject",
						data:{reviewId:id,reason:reason,type:v},
						success:function(data){
							window.location.reload();
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

	//成本审核-通过某条成本
	function passCost(dom,id){
		$.jBox($("#batch-verify-list").html(), {
			title: "备注：", buttons: {'取消': 0,'提交': 2}, submit: function (v, h, f) {
				if(v == 2) {
					var reason = f.reason;
					$.ajax({
						type:"POST",
						url:"${ctx}/costReview/activity/approveOrReject",
						data:{reviewId:id,reason:reason,type:v},
						success:function(data){
							window.location.reload();
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


	function saveCheckBox(id){
		var tmp=0;
		$("input[name='"+id+"']").each(function(){ 
			if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
				tmp=tmp +","+$(this).attr('value');
			}
		});	
		if(tmp=="0"){
			alert("请选择成本");
		 	return;
		}
			$.jBox($("#batch-verify-list").html(), {
				title: "备注：", buttons: {'取消': 0,'提交': 2}, submit: function (v, h, f) {
					var reason = f.reason;
					$.ajax({
						type:"POST",
						url:"${ctx}/costReview/activity/batchApproveOrReject",
						data:{reviewIds:tmp,reason:reason,type:v},
						success:function(data){
							window.location.reload();
						},
						error:function(){
							alert('返回数据失败');
						}
					});
					
				}, height: 250, width: 350
			});
			inquiryCheckBOX();
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
						htmlstr+="已驳回";
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
	<c:if test="${reviewLevel==1}">
		<page:applyDecorator name="cost_review_head">
			<page:param name="current">airticket</page:param>
		</page:applyDecorator>
	</c:if>
	<c:if test="${reviewLevel>=2}">
		<page:applyDecorator name="cost_review_manager">
			<page:param name="current">airticket</page:param>
		</page:applyDecorator>
	</c:if>
	<input type="hidden" name="airTicketId" id="airTicketId" value="${airticket.id}"/>
	<input type="hidden" name="budgetType" id="budgetType" value="1"/>
	<!--右侧内容部分开始-->
	<div class="mod_nav">
		<c:choose>
			<c:when test="${head eq 1 }">财务 > 结算管理 > 成本付款 > 机票详情页</c:when>
			<c:when test="${head eq 2 }">财务 > 团队管理 > 机票详情页</c:when>
			<c:otherwise>审批 > 成本审批 > 机票录入审批详情</c:otherwise>
		</c:choose>
	</div>
			
	<div class="produceDiv">
		<div class="orderdetails_tit"><span>1</span>机票产品信息</div>
		<div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden">
			<p class="ydbz_mc">
				产品编号: ${activityAirTicket.productCode}, ${activityAirTicket.departureCityLabel()}&mdash;
				<c:forEach items="${areas}" var="arrivedcity">
					<c:if test="${arrivedcity.id eq activityAirTicket.arrivedCity}">
						${arrivedcity.name}：
					</c:if>
				</c:forEach>
				<c:if test="${ activityAirTicket.airType ==1}">多段</c:if>
				<c:if test="${ activityAirTicket.airType ==2}">往返</c:if>
				<c:if test="${ activityAirTicket.airType ==3}">单程</c:if>
			</p>

			<c:if test="${ activityAirTicket.airType ==1}">
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
										<c:if test="${airportlist.id eq flightInfos.leaveAirport}">
											${airportlist.airportName}
										</c:if>
									</c:forEach>
								</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">
									<c:forEach items="${airportlist}" var="airportlist">
										<c:if test="${airportlist.id eq flightInfos.destinationAirpost}">
											${airportlist.airportName}
										</c:if>
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
								<td class="mod_details2_d2">${flightInfos.airlinesLabel() }</td>
								<td class="mod_details2_d1">舱位等级：</td>
								<td class="mod_details2_d2">${flightInfos.spaceGradeLabel() }</td>
								<td class="mod_details2_d1">舱位：</td>
								<td class="mod_details2_d2">${flightInfos.airspaceLabel() }</td>
							</tr>

						</tbody>
					</table>
				</c:forEach>
			</c:if>

			<c:if test="${ activityAirTicket.airType ==2}">
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
							<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() }</td>
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
							<td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airlinesLabel() }</td>
							<td class="mod_details2_d1">舱位等级：</td>
							<td class="mod_details2_d2">${activityAirTicket.flightInfos[1].spaceGradeLabel() }</td>
							<td class="mod_details2_d1">舱位：</td>
							<td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airspaceLabel() }</td>
						</tr>
					</tbody>
				</table>
			</c:if>

			<c:if test="${ activityAirTicket.airType ==3}">
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
							<td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() }</td>
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
				<div class="ydbz_tit">订单列表</div>
			</div>
		</div>
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
							   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && airTicketOrder.agentName=='非签约渠道'}"> 
							       直客
							   </c:when>
							   <c:otherwise>
							       ${airTicketOrder.agentName}
							   </c:otherwise>
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
							<c:if test="${airTicketOrder.airType=='3'}">单程</c:if>
							<c:if test="${airTicketOrder.airType=='1'}">多段</c:if>
							<c:if test="${airTicketOrder.airType=='2'}">往返</c:if>
						</td>
						<td class="tr">${airTicketOrder.personNum}</td>
						<td class="tc">${fns:getDictLabel2(airTicketOrder.order_state, "order_pay_status", "")}</td>
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
		<%--增加订单总收入和订单总人数,需求0311:yudong.xu--%>
		<div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
			<ul class="cost-ul">
				<ul class="cost-ul">
					<li>订单总收入：&nbsp;￥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${totalMoneySum }"/></li>
				</ul>
				<li>订单总人数：&nbsp;${orderPesonSum}&nbsp;人</li>
			</ul>
		</div>

		<iframe id="iframepage" width="100%" height="100%" frameborder="0" src="${ctx}/cost/common/getCostRecordList/${activityAirTicket.id }/7/?costId=${costId }&flag=1&read=${read}&budgetType=${budgetType}" onLoad="iFrameHeight()"></iframe>

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
	$(function(){
		costSums($('tr.otherCost'),$('#otherCostShow'),2);
		//预算成本录入-境内小计
		costSums($('tr.budgetInCost'),$('#budgetInShow'),0);
		//预算成本录入-境外小计  
		costSums($('tr.budgetOutCost'),$("#budgetOutShow"),0);
		costSums($('tr.actualInCost'),$("#actualInShow"),1);
		costSums($('tr.actualOutCost'),$("#actualOutShow"),1); 
	});
</script>

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