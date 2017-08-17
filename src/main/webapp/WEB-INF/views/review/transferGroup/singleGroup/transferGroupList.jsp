<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>转团记录</title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript">
		$(function(){
			//操作浮框
			operateHandler();
		});

		/**
		 * 取消转团申请
		 */
		function cancelTransferGroup(reviewId, travelerId) {
			$.jBox.confirm("确定要取消转团申请吗？", "提示", function(v, h, f) {
				if (v == 'ok') {
					$.ajax({                 
						type : "POST",                 
						url : "${ctx}/newTransferGroup/cancelTransferGroup",                 
						data : {
							reviewId : reviewId,
							travelerId : travelerId
						},                
						error: function(request) {                     
							top.$.jBox.tip("取消失败","warning");
						},                 
						success: function(data) { 
							if (data.result == "success") {
								top.$.jBox.tip("取消成功","success");	
								window.location.href = "${ctx}/newTransferGroup/list/${orderId}";
							} else {
								top.$.jBox.tip("取消失败","warning");
							} 
						}             
					});
				}
			});	
		}
		
		function showContentdiv(divId,num){
          var sp_show = document.getElementById("sp_show"+num);
          var inner_cont = document.getElementById("inner_cont"+num);
          sp_show.innerHTML = inner_cont.innerHTML;
          sp_show.style.display="block";
        }
		function hideContent(obj) {
		  var sp_id_name = $(obj).attr('id');
		  var sp_show = document.getElementById(sp_id_name);
		  sp_show.style.display="none";
		}
	</script>
	<style  type="text/css">
		.tip-content{
		    display: none; 
		    position: absolute;
		    top: 0px;
		  	white-space: nowrap;
		  	left: 0px;
		  	height:100%;
		  	min-width:250px;
		  	max-width:350px;
		  	text-align: left;
		    z-index: 10;
		    border: 1px solid #d9d9d9;
		    background-color: white;
		    padding: 8px;
		    min-width: 100%;
		}
		.inner_cont{
			overflow: hidden;
			text-overflow:ellipsis;
			white-space: nowrap;
		}
	</style>
</head>
<body>
	<page:applyDecorator name="show_head">
    	<page:param name="desc">转团记录</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<div class="mod_nav">订单 > ${orderTypeStr} > 转团记录</div>
	<div class="ydbz_tit orderdetails_titpr">转团记录
		<a class="ydbz_x" href="${ctx}/newTransferGroup/transferGroupPage?orderId=${orderId}&productType=${productType}">申请转团</a>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table" style="width:100%; table-layout:fixed;">
		<thead>
			<tr>
				<th style="width:10%;">游客</th>
				<th style="width:15%;">下单时间</th>
				<th style="width:10%;">报批日期</th>
				<th style="width:15%;">应收金额</th>
				<th style="width:15%;">转团后应收</th>
				<th style="width:15%;">转团原因</th>
				<th style="width:12%;">状态</th>
				<th style="width:8%;">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${transferGroupList}"  var="transferGroup">
				<tr>
					<td class="tc inner_cont" title="${transferGroup.travellerName}">${transferGroup.travellerName}</td>
					<td class="tc"><fmt:formatDate value="${transferGroup.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
					<td class="tc"><fmt:formatDate value="${transferGroup.createDate}" pattern="yyyy-MM-dd"/> </td>
					<td class="tc inner_cont tdorange fbold" title="${transferGroup.payPriceSumString}">${transferGroup.payPriceSumString}</td>
					<td class="tc inner_cont tdorange fbold" title="${transferGroup.subtractSumString}">${transferGroup.subtractSumString}</td>
					<c:set var="remarkTitle" value=""></c:set>
					<c:forEach items="${transferGroup.travelerMapList}" var="travelerMap" varStatus="status">
						<c:set var="remarkTitle" value="${remarkTitle}&#10;${travelerMap.travellerName }：${travelerMap.remark }"></c:set>
					</c:forEach>
					<td class="tc inner_cont" title="${remarkTitle }">
						<c:forEach items="${transferGroup.travelerMapList}" var="travelerMap" varStatus="status">
							<span>${travelerMap.travellerName }：${travelerMap.remark }</span>
						</c:forEach>
					</td>
					<td class="invoice_yes">
						${fns:getChineseReviewStatus(transferGroup.status, transferGroup.currentReviewer)}
					</td>
					<td class="p0">
						<dl class="handle">
							<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
							<dd>
								<p>
									<span></span>
									<a href="${ctx}/newTransferGroup/transferGroupInfo?reviewId=${transferGroup.id}">转团详情</a>
									<c:if test="${fns:isShowCancel(transferGroup.id)}">
										<a href="javascript:cancelTransferGroup('${transferGroup.id}','${transferGroup.transGroupTravelerIDs}');">取消申请 </a>
									</c:if>
								</p>
							</dd>
						</dl>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty transferGroupList }">
				<td class="tc" colspan="8">暂无转团信息</td>
			</c:if>
		</tbody>
	</table>
	<div class="ydBtn"><a onclick="window.close();" class="ydbz_s gray">关闭</a></div>
	<!--右侧内容部分结束-->
</body>
</html>
