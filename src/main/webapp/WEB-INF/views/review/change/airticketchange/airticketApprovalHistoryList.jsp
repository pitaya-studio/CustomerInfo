<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler" />
<title>产品-机票产品改签记录</title>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>

<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript">
var root = "${ctx}";
function page(pn,ps){
	   $("#pageNo").val(pn);
	   $("#pageSize").val(ps);
	   $("#searchForm").submit();
	}
function cancelApp(id,status){
	$.ajax({    
			type: "POST",                 
			url:root+"/airticketChange/airChange/cancelApp",                 
			error: function(request) {                     
			},      
			dataType:"json",   
			data:{
				'reviewId':id,
				'status' :status
			},        
			success: function(msg) {    
				 alert(msg.result);
				 window.location.href=root+"/airticketChange/airChange/airticketApprovalHistoryList?orderId="+$("#orderId").val();
			}             
		});
}

$(function(){
	//展开筛选按钮
	launch();
	operateHandler();
	//展开筛选按钮
	//搜索聚焦失焦
	inputTips();
});
</script>
</head>
<body>
	<div class="ydbzbox fs">
		<input type='hidden' id='orderId' value="${params.orderId}">
		<!--右侧内容部分开始-->
		<div class="mod_nav">订单 > 机票 > 改签记录</div>
		<div class="ydbz_tit orderdetails_titpr">
			改签记录
			<a class="ydbz_x" href="${ctx}/airticketChange/airChange/airticketOrderDetailChange?orderId=${params.orderId}" target="_blank">申请改签</a>
		</div>
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="8%">游客</th>
					<th width="8%">报批日期</th>
					<th width="15%">改签出发地机场</th>
					<th width="12%">改签起飞时间</th>
					<th width="12%">改签到达机场</th>
					<th width="10%">原因备注</th>
					<th width="8%">状态</th>
					<th width="5%">操作</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${page}" var="cell">
					<tr>
						<td>${cell.travellerName }</td>
						<td class="tc">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${cell.createDate }"/>
						</td>
						<td class="tc">
							${cell.leaveAirport }
						</td>
						<td class="tc">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${cell.startDate }"/>
						</td>
						<td class="tc">
							${cell.destinationAirpost }
						</td>
						<td class="tc">
							${cell.remark }
						</td>
						<td class="invoice_yes tc">
						
							${fns:getChineseReviewStatus(cell.status,cell.currentReviewer)}

						</td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img src="${ctxStatic}/images/handle_cz.png" title="操作">
								</dt>
								<dd>
									<p>
										<span></span> <a target="_blank"
											href="${ctx}/airticketChange/airChange/airticketApprovalDetail?orderId=${cell.orderId}&reviewId=${cell.id}&trvalerId=${travellerId}">订单详情</a>
										<c:if test="${fns:isShowCancel(cell.id)}">
											<a target="_blank" id='cancelApp'
												onClick="javascript:cancelApp('${cell.id}',${cell.status });">取消申请</a>
										</c:if>
									</p>
								</dd>
							</dl>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>