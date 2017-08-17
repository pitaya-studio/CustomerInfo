<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>报名-补位申请记录</title>
	<!-- 页面左边和上边的装饰 -->
	<meta name="decorator" content="wholesaler" />
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/modules/groupcover/groupCoverPage.js"></script>
	<script src="${ctxStatic}/modules/order/single/activityListForOrder.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			launch();
			//操作浮框
			operateHandler();
		});
		/**
		 * 取消补位申请
		 */
		function cancelCover(coverId) {
			$.jBox.confirm("确定取消该补位？", "提示", function(v, h, f) {
				if (v == 'ok') {
					$.ajax({                 
						type : "POST",                 
						url : "${ctx}/groupCover/cancelCover",                 
						data : {
							coverId : coverId
						},                
						error: function(request) {                     
							top.$.jBox.tip("取消失败","warning");
						},                 
						success: function(data) { 
							if (data.result == "success") {
								top.$.jBox.tip("取消成功","success");	
								window.location.href = "${ctx}/groupCover/list/${groupId}";
							} else {
								top.$.jBox.tip("取消失败","warning");
							} 
						}             
					});
				}
			});	
		}
	</script>
</head>
<body>
	<div id="sea">
		<div class="mod_nav">报名 > 补位申请 > 补位申请记录</div>
		<div class="ydbz_tit orderdetails_titpr">补位申请记录
			<a class="ydbz_x" href="${ctx}/groupCover/groupCoverPage/${groupId}">申请补位</a>
		</div>
		<input type="hidden" name="companyUuid" id="companyUuid" value="${companyUuid }">
		<input type="hidden" name="isAddAgent" id="isAddAgent" value="${isAddAgent }">
		<input id="quauqBookOrderPermission" name="quauqBookOrderPermission" type="hidden" value="${user.quauqBookOrderPermission }" />
		<span>团号：${group.groupCode}</span>
		<span>计调：${group.createBy.name}</span>
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="12%">补位号</th>
					<th width="8%">补位人数</th>
					<th width="8%">状态</th>
					<th width="10%">申请提交时间</th>
					<th width="10%">补位申请人</th>
					<th width="10%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${groupCoverList}" var="groupCover">
					<tr>
						<td>${groupCover.coverCode}</td>
						<td>${groupCover.coverPosition}</td>
						<td><span class="fbold" style="color:#eb0301">${groupCover.coverStatusName}<span></td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${groupCover.createDate}"/></td>
						<td  class="tc">${groupCover.createBy.name}</td>
						<td class="p0">
							<dl class="handle">
								<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"/></dt>
								<dd class="">
									<p>
										<span></span> 
										<a href="${ctx}/groupCover/groupCoverInfo/${groupCover.id}" target="_blank">补位详情</a>
										<c:if test="${fns:getUser().id eq groupCover.createBy.id && (groupCover.coverStatus eq 1 or groupCover.coverStatus eq 2) }">
									   	 	<a href="javascript:cancelCover('${groupCover.id}');">取消补位 </a>
								   	 	</c:if>
								   	 	<c:if test="${fns:getUser().id eq groupCover.createBy.id && groupCover.coverStatus eq 2}">
									   	 	<a href="javascript:bookByCover('${groupCover.id}');">预定</a>
								   	 	</c:if>
									</p>
								</dd>
							</dl> 
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="ydBtn"><a onclick="window.close();" class="ydbz_s gray">关闭</a></div>
	<div class="xt-activitylist" style="display:none;">
        <select name="agentSourceType" id="agentSourceTypeTemp" class="typeSelected" onchange="changeAgentSource(this);">
        	<option value="1" selected="selected">非实时渠道</option>
        	<option value="2">实时连通渠道</option>
        </select>
        <select name="quauqAgent" id="quauqAgentTemp" class="typeSelected">
        	<c:forEach var="agentinfo" items="${quauqAgentinfoList }">
                <option value="${agentinfo.id }">${agentinfo.agentName }</option>
            </c:forEach>
        </select>
    </div>
</body>
</html>
