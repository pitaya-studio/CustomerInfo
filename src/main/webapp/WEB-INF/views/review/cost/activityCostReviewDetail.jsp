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
			${typename}成本详情页
		</c:otherwise>
		</c:choose>
	</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/modules/cost/cost.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function(){
		$('.zksx').click(function(){
			if($('.ydxbd').is(":hidden")==true) {
				$('.ydxbd').show();
				$(this).text('收起筛选');
				$(this).addClass('zksx-on');
			}else{
				$('.ydxbd').hide();
				$(this).text('展开筛选');
				$(this).removeClass('zksx-on');
			}
		});
		
		//团号产品名称切换
		$("#contentTable").delegate("ul.caption > li","click",function(){
			var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
			$(this).addClass("on").siblings().removeClass('on');
			$(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
		});
	
		$("#contentTable").delegate(".tuanhao","click",function(){
			$(this).addClass("on").siblings().removeClass('on');
			$('.chanpin_cen').removeClass('onshow');
			$('.tuanhao_cen').addClass('onshow');
			$("div.groupCode").show();
			$("div.acitivityName").hide();
		});
		
		$("#contentTable").delegate(".chanpin","click",function(){
			$(this).addClass("on").siblings().removeClass('on');
			$('.tuanhao_cen').removeClass('onshow');
			$('.chanpin_cen').addClass('onshow');
			$("div.groupCode").hide();
			$("div.acitivityName").show();
		});		
		var ids = "";
		var names = "";
		<c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
			ids = ids + "${data.id}"+",";
			names = names +"${data.name}"+",";
		</c:forEach>
	
		$("#targetAreaName").text(names.toString().substring(0,names.length-1));
	});
	
	//成本审核-拒绝某条成本
	function denyCost(dom,id,reviewLevel){
// 		$.ajax({
// 			type: "POST",
// 			url: "${ctx}/costReview/activity/approveOrReject",
// 			cache:false,
//			dataType:"json",
// 			async:false,
// 			data:{reviewId:id, reason:'', type:1},
// 			success: function (data){
// 				top.$.jBox.tip(data,'success');
// 				window.location.reload();
// 			},
// 			error: function (){
// 				alert('返回数据失败');
// 			}
// 		});
		$.jBox($("#batch-verify-list").html(), {
			title: "备注：", buttons: {'取消': 0,'提交': 1}, submit: function (v, h, f) {
				if(v == 1) {
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
// 		$.ajax({
// 			type: "POST",
// 			url: "${ctx}/costReview/activity/approveOrReject",
// 			cache:false,
//			dataType:"json",
// 			async:false,
// 			data:{reviewId:id, reason:'', type:2},
// 			success: function (data){
// 				top.$.jBox.tip(data,'success');
// 				window.location.reload();
// 			},
// 			error: function (){
// 				alert('返回数据失败');
// 			}
// 		});
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
// 		$.ajax({
// 			type: "POST",
// 			url: "${ctx}/costReview/activity/batchApproveOrReject",
// 			cache:false,
//			dataType:"json",
// 			async:false,
// 			data:{reviewIds:tmp,type:2},
// 			success: function(data){
// 				top.$.jBox.tip(data,'success');
// 				window.location.reload();
// 			},
// 			error : function(e){
// 				alert('请求失败。');
// 				return false;
// 			}
// 		});
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
				var htmlstr=""
				var num = data.length;
				if(num>0){
					var str1='';
					for(var i =0;i<num;i++){
						var str = data[i].payvoucher.split("|")
						var idstr = data[i].ids.split("|")
						var index = str.length;
						if(index>0){
							for(var a=0;a<index;a++){
								str1+="<a class=\"downloadzfpz\" lang=\""+idstr[a]+"\">"+str[a]+"</a><br/>"
							}
						}
						htmlstr+="<tr><td class='tc'>"+data[i].payTypeName+"</td><td class='tc'>"+data[i].currency_mark+ milliFormat(parseFloat(data[i].amount).toFixed(2)) +"</td><td class='tc'>"+data[i].createDate+"</td><td class='tc'>"+"其它收入"+
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
						//if(data[i].isAsAccount == 1) {
							//htmlstr+="是";
						//}else{
							//htmlstr+="否";
						//}
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
			<page:param name="current"><c:choose><c:when test="${typeId==1}">single</c:when><c:when test="${typeId==2}">loose</c:when><c:when test="${typeId==3}">study</c:when><c:when test="${typeId==5}">free</c:when><c:when test="${typeId==4}">bigCustomer</c:when><c:when test="${typeId==10}">cruise</c:when></c:choose></page:param>
		</page:applyDecorator>
	</c:if>
	<c:if test="${reviewLevel>=2}">
		<page:applyDecorator name="cost_review_manager">
			<page:param name="current"><c:choose><c:when test="${typeId==1}">single</c:when><c:when test="${typeId==2}">loose</c:when><c:when test="${typeId==3}">study</c:when><c:when test="${typeId==5}">free</c:when><c:when test="${typeId==4}">bigCustomer</c:when><c:when test="${typeId==10}">cruise</c:when></c:choose></page:param>
		</page:applyDecorator>
	</c:if>	
	<div class="mod_nav">
		<c:choose>
			<c:when test="${head eq 1 }">财务 > 结算管理 > 成本付款 > ${typename }详情页</c:when>
			<c:when test="${head eq 2 }">财务 > 团队管理 > ${typename }详情页</c:when>
			<c:otherwise>审批 > 成本审批 > ${typename}录入审批详情</c:otherwise>
		</c:choose>
	</div>
	<div class="produceDiv">
		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">产品基本信息</div>
			</div>
		</div>
		<div class="mod_information_dzhan">
			<div class="mod_information_dzhan_d mod_details2_d">
				<span style="color: #3a7851; font-size: 16px; font-weight: bold;">${travelActivity.acitivityName}</span>
				<div class="mod_information_d7"></div>
				<table border="0" width="90%">
					<tr>
						<td class="mod_details2_d1">产品编号：</td>
						<td class="mod_details2_d2">
							<c:choose> 
								<c:when test="${fn:length(travelActivity.activitySerNum) > 20}"> 
									<a style="text-decoration: none; color:inherit; cursor:default;" title="${travelActivity.activitySerNum}"><c:out value="${fn:substring(travelActivity.activitySerNum, 0, 20)}..." /></a> 
								</c:when> 
								<c:otherwise> 
									<c:out value="${travelActivity.activitySerNum}" /> 
								</c:otherwise>
							</c:choose>
						</td>
						<td class="mod_details2_d1">产品分类：</td>
						<td class="mod_details2_d2">
							<c:choose>
								<c:when test="${travelActivity.overseasFlag eq 1}">国外</c:when>
								<c:otherwise>国内</c:otherwise>
							</c:choose>
						</td>
						<td class="mod_details2_d1">旅游类型：</td>
						<td class="mod_details2_d2">${travelActivity.travelTypeName}</td>
						<td class="mod_details2_d1">出发城市：</td>
						<td class="mod_details2_d2">${travelActivity.fromAreaName}</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">目的地：</td>
						<td class="mod_details2_d2" id="targetAreaName" title=""></td>
						<td class="mod_details2_d1">交通方式：</td>
						<td class="mod_details2_d2">
							${travelActivity.trafficModeName}
							<c:if test="${!empty travelActivity.trafficName and fn:indexOf(relevanceFlagId,travelActivity.trafficMode)>=0 }">
								&nbsp;&nbsp;|&nbsp;&nbsp;${travelActivity.trafficNameDesc}
							</c:if>
						</td>
						<td class="mod_details2_d1">产品系列：</td>
						<td class="mod_details2_d2">${travelActivity.activityLevelName}</td>
						<td class="mod_details2_d1">团号：</td>
						<td class="mod_details2_d2">${activityGroup.groupCode}</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">产品类型：</td>
						<td class="mod_details2_d2">${travelActivity.activityTypeName}</td>
						<td class="mod_details2_d1">行程天数：</td>
						<td class="mod_details2_d2" id="activityDuration">${travelActivity.activityDuration}</td>
						<td class="mod_details2_d1">付款方式：</td>
						<td colspan="3" class="mod_details2_d2">
							<c:if test="${travelActivity.payMode_deposit eq 1}">
								订金占位
								<c:if test="${travelActivity.remainDays_deposit != '' && travelActivity.remainDays_deposit != null}">
									（保留${travelActivity.remainDays_deposit}天）&nbsp;&nbsp;
								</c:if>
							</c:if>
							<c:if test="${travelActivity.payMode_advance eq 1}">
								预占位（保留${travelActivity.remainDays_advance}天）&nbsp;&nbsp;
							</c:if>
							<c:if test="${travelActivity.payMode_full eq 1}">
								全款支付
							</c:if>
							<c:if test="${travelActivity.payMode_op eq 1}">
								计调确认占位
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="mod_details2_d1">计调人员：</td>
						<td class="mod_details2_d2">${travelActivity.createBy.name}</td>
						<td class="mod_details2_d1">创建时间：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${travelActivity.createDate }"/></td>
						<td class="mod_details2_d1">更新时间：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${travelActivity.updateDate }"/></td>
						<td class="mod_details2_d1">出团日期：</td>
						<td class="mod_details2_d2"><fmt:formatDate value="${activityGroup.groupOpenDate }" pattern="yyyy-MM-dd"/></td>
					</tr>
				</table>
				<div class="kong"></div>
			</div>
		</div>	

		<div class="mod_information">
			<div class="mod_information_d">
				<div class="ydbz_tit">订单列表</div>
			</div>
		</div>
		<table class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
			<thead>
				<tr>
					<th width="7%">预定渠道</th>
					<th width="9%">订单号</th>
					<th width="11%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
					<th width="9%">预订时间</th>
					<th width="8%">销售</th>
					<th width="8%">出/截团日期</th>
					<th width="4%">人数</th>
					<th width="6%">订单状态</th>
					<th width="8%">订单总额</th>
					<th width="8%">已收金额<br>到账金额</th>
					<th width="7%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${orderList}" var="groupOrder">
					<tr>
						<td class="tc">
							<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
							<c:choose>
							   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'  && groupOrder.orderCompanyName=='非签约渠道'}"> 
							       直客
							   </c:when>
							   <c:otherwise>
							      ${groupOrder.orderCompanyName}
							   </c:otherwise>
							</c:choose> 
						</td>
						<td class="tc">${groupOrder.orderNum}</td>
						<td class="tc"><div class="groupCode">${groupOrder.groupCode}</div><div style="display:none;" class="acitivityName">${groupOrder.acitivityName}</div></td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${groupOrder.orderTime }"/></td>
						<td class="tc">${groupOrder.orderSalerId}</td>
						<td style="padding: 0px;">
							<div class="out-date">${groupOrder.groupOpenDate}</div>
							<div class="close-date">${groupOrder.groupCloseDate}</div>
						</td>
						<td class="tr">${groupOrder.orderPersonNum}</td>
						<td class="tc">${fns:getDictLabel(groupOrder.payStatus, "order_pay_status", "")}</td>
						<td class="tr"><span class="tdorange fbold">${groupOrder.totalMoney} </span></td>
						<td class="p0 tr">
							<div class="yfje_dd"> 
								<span class="fbold">${groupOrder.payedMoney}</span>
							</div>
							<div class="dzje_dd">
								<span class="fbold">${groupOrder.accountedMoney}</span>
							</div>
						</td>
						<td class="tc">
							<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/orderCommon/manage/orderDetail/'+${groupOrder.id},'_blank')">详情 </a>
						</td>
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

		<iframe id="iframepage" width="100%" height="100%" frameborder="0" src="${ctx}/cost/common/getCostRecordList/${activityGroup.id }/${typeId }?costId=${costId }&flag=1&read=${read}&budgetType=${budgetType}" onLoad="iFrameHeight()"></iframe>

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

		<div class="release_next_add">
			<input class="btn btn-primary" type="button" value="关 闭"  onclick="javascript:window.opener=null;window.open('','_self');window.close();" /> 
		</div>

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
